package com.parasoft.virtualize.tools;



import com.parasoft.api.tool.*;


public class MSMQTool implements ICustomTool {
	

	MSMQToolConfiguration config= null;

	private static String charset ="UTF-8";
	private static String mimeType ="text/plain";		// Do we need to make this text/xml ??
	private IToolContext context = null;
	String header = null;
	 String message = null;
	
	 @Override
	 public boolean execute(IToolInput input, IToolContext context) throws com.parasoft.api.tool.CustomToolException
	 {
		 MSMQCommon.debug("execute: started");
		 String msg = input.toString();
		 boolean sent = false;
		 boolean received = false;
		 this.context = context;
		 
		 if (config == null)
			 return false;
		 
		 // Only send a message if the queue is specified
		 try{
			if (!config.getOutQueue().equals(""))
			{
				 MSMQCommon.debug("Sending");
				 sent = sendMessage(msg);
			}
			else 
			{
				// set the flag for later use.
				sent = true;
			}
			 
			// Only receive if the receive queue is specified
			if (sent && !config.getInQueue().equals(""))
			 {
					 MSMQCommon.debug("Receiving");
					 received = receive();
			 }
			
		 	else 
		 	{
		 		// set the flag for later use.
		 		received = true;
		 	} 
			
			MSMQCommon.debug("Completed");
		 }
		 catch (Exception e)
		 { 
			 MSMQCommon.debug("execute failure : " + e);
		 }
		 finally
		 {
			
		 }
		 		 
		 
		 return (sent && received);
	 }
	
	 /*
	  * Receive a message from the specified Queue
	  */
	 private boolean receive()  throws java.io.UnsupportedEncodingException
	 	{
		 	MSMQCommon.debug("receive: started");
		 	
		 	String [] response = MSMQCommon.receive(MSMQCommon.RECEIVE, config);
	               
		 	 // Send the results back to an OUtput tool ....
		 	 if (response.length == 2) 
			 try
			 {
				 MSMQCommon.debug("header:" + header);
				 DefaultTextInput headerOutput = new DefaultTextInput(response[0], charset, mimeType);
				 
				 MSMQCommon.debug("## " + headerOutput.getString());
				 
				 context.getOutputManager().runOutput("output1", headerOutput, context);
			
				 MSMQCommon.debug("message:" + message);
				 DefaultTextInput msgOutput = new DefaultTextInput(response[1], charset, mimeType);
				 context.getOutputManager().runOutput("output2", msgOutput, context);
			 }
			 catch (Exception e)
			 {
				 MSMQCommon.debug("execute failure 2 : " + e);
			 }
	            
	            return (response != null);
	 }
	
	 public boolean isValidConfig(ICustomToolConfiguration arg0) {
		 MSMQCommon.debug("isValidConfig: started");
		config = new MSMQToolConfiguration(arg0);
		
		if (config.getIP().equals(""))
		{
			return false;
		}
		
		if (config.getInQueue().equals("") && config.getOutQueue().equals(""))
		{
			return false;
		}
		
		return true;
		
	}
	
    public boolean   acceptsInput(IToolInput input , ICustomToolConfiguration config )
    {
    	MSMQCommon.debug("acceptsInput: started");
   	 	return true;
    }
        
    /*
     * Place a message on the specified output queue
     * @param String	The message to send
     *      
     */
    private boolean sendMessage(String msgOut)
            throws java.io.UnsupportedEncodingException
        {
    		MSMQCommon.debug("sendMessage: started");
    		boolean rVal = MSMQCommon.sendMessage(msgOut,  MSMQCommon.SEND, config);   
                        
            return rVal;
        }
}

