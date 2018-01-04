package com.parasoft.virtualize.tools;

import ionic.Msmq.Message;
import ionic.Msmq.MessageQueueException;
import ionic.Msmq.Queue;

import com.parasoft.api.Application;


/*
 * Provides some common utilities across both Listener and Tool classes
 */

public final class MSMQCommon {
	
	protected final static int SEND = 1;
	protected final static int RECEIVE = 2;
	protected final static int V_SEND = 3;
	protected final static int V_RECEIVE = 4;
	protected final static boolean debug = false;
	
	/*
	 * Open an MSMQ queue
	 * @param Queue 				the queue object to open
	 * @param int					SEND or RECEIVE
	 * @param IMSMQConfiguration	MSMQConfiguration or MSMQToolConfiguration object
	 * @return	Queue 				the opened Queue 		
	 */
	protected static Queue open(int type, IMSMQConfiguration config)
    {
		debug("open: started");
		Queue thisQueue = null;
        try 
        {
      	  String qname = "";
      	  
      	  if (thisQueue == null ) {
            	
               	if (type == SEND)
	            {
	          	  	qname = config.getOutQueue();
	            }
	            else if (type == RECEIVE)
	            {
	          	  	qname = config.getInQueue();
	            }
	          
	            String hostname= config.getIP();
	            String fullname= getQueueFullName(hostname,qname);
	            debug("open (" + fullname + ")");
	            thisQueue = new ionic.Msmq.Queue(fullname);
	            debug("open: OK.");
            }
            
        }
        catch (Exception ex1) {
        	Application.showMessage("MSMQCommon:Queue open failure : " + ex1.getMessage());
        }
        finally
        {
        	debug("MSMQCommon:Queue open: " + thisQueue);
        }
        
        return thisQueue;
    }

	/*
	 * Close an MSMQ queue
	 * @param Queue		The queue to close
	 */
	 protected static void close(Queue queue) {
		 debug("close: started");
	        try {
	            if (checkOpen(queue))
	            {
	            	debug("close " + queue);
	            	queue.close();
	            	queue= null;
	            	debug("close: OK.");
	            }
	        }
	        catch (MessageQueueException ex1) {
	        	Application.showMessage("close failure: " + ex1);
	        }
	    }
	 
	 
	  
	 /*
	  * Check if this queue an instantiated object
	  * @param Queue	The queue to check 
	  */
	    private static boolean checkOpen(Queue thisQueue) throws MessageQueueException {
	    		debug("checkOpen: started");
	            if (thisQueue==null)
	            {
	            	return false;
	            }
	            else
	            {
	            	return true;
	            }
	        }
	
	    /*
	     * Construct the full queue name
	     * @param String 	The name of the host or IP address
	     * @param String	The short name for the queue
	     * @param String	The constructed queue name
	     */
	 private static String getQueueFullName( String hostname, String queueShortName ) {
		 	debug("getQueueFullName: started");
		 	debug("~~" + hostname);
	   	 	String h1= hostname;
	   	 	String a1= "OS";
	   	 	if ((h1==null) || h1.equals("")) h1=".";
	   	 	char[] c= h1.toCharArray();
	   	 	if ((c[0]>='1')
	           && (c[0]<='9')) a1= "TCP";

	   	 	String rVal = "DIRECT=" + a1 + ":" + h1 + "\\private$\\" + queueShortName;
	   	 	debug(rVal);
	   	 	return rVal;
	   }
	 
	 
	 protected static void debug(String message)
	 {
		 if (debug)
			 Application.showMessage(message);
		 
	 }
	 
	 /*
	  * Receive a message from the specified Queue
	  */
	 protected static String [] receive(int type, IMSMQConfiguration config)  throws java.io.UnsupportedEncodingException
	 	{
		 	MSMQCommon.debug("receive: started");
		 	Queue queue = null;
		 	String header = "";
		 	String message = "";
		 	
            try {
            	MSMQCommon.debug("receive");
            	
                queue = open(type, config);
                if (queue != null)
                {          
	                Message msg= queue.receive(config.getTimeout()); 
	                
	                header = msg.getLabel();
                	message = msg.getBodyAsString();
	                
	                MSMQCommon.debug(" ==> message: " + message);
	                MSMQCommon.debug("     label:   " + header);
	                
	                String correlationID = msg.getCorrelationIdAsString();
	                MSMQCommon.debug("correlationID:   " + correlationID);

                 }
            }
            catch (MessageQueueException ex1) 
            {
                MSMQCommon.debug("Tired of waiting: " + ex1);
            }
            finally
            {
            	MSMQCommon.close(queue);
            }
            
            return new String [] {header, message};
        }
	 
	 /*
	     * Place a message on the specified output queue
	     * @param String	The message to send
	     *      
	     */
	    protected static boolean sendMessage(String msgOut, int type, IMSMQConfiguration config)
	            throws java.io.UnsupportedEncodingException
	        {
	    		MSMQCommon.debug("sendMessage: started");
	    		boolean rVal = false;
	    		Queue outQueue = null;
	            try {
	            	outQueue = MSMQCommon.open(MSMQCommon.SEND, config);      
	            	if (outQueue != null)
	            	{
	                    // the transaction flag must agree with the transactional flavor of the queue.
		                String mLabel="inserted by Virtualize";	// New config item ??
		
		                String correlationID= config.getCorrelationID();
		                java.util.Calendar cal= java.util.Calendar.getInstance(); // current time & date
		                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:sszzz");
		                String body= "[from:Java] [time:" +    df.format(cal.getTime()) + "]" + config.getIP();
		                MSMQCommon.debug("send (" + body + ")");
		                MSMQCommon.debug("correlationID (" + correlationID + ")");
		                Message msg= new Message(body, mLabel, correlationID);
		                msg.setBodyAsString(msgOut);
		                MSMQCommon.debug("send (" + msgOut + ")");
		                outQueue.send(msg);
		                
		                rVal = true;
	            	}
	            }
	            catch (MessageQueueException ex1) {
	            	Application.showMessage("Put failure: " + ex1);
	                
	            }
	            finally
	            {
	            	MSMQCommon.close(outQueue);
	            }
	            
	            return rVal;
	        }
}
