package com.parasoft.virtualize.tools;
import java.io.*;
import java.net.*;
import java.util.*;


import com.parasoft.api.*;
import com.parasoft.api.responder.*;
import com.parasoft.api.tool.DefaultTextInput;

import ionic.Msmq.*;
import ionic.Msmq.Queue;

public class MSMQListenerWorker {

    private Thread worker;
    private boolean active = false;
    MSMQConfiguration config;
    IMessageHandler handler;


//    private String ipAddr = "";
//	private Queue queue= null; 
//	private Queue outQueue= null; 
    
    public MSMQListenerWorker(IMessageHandler handler, MSMQConfiguration config) {
        this.config = config;
        this.handler = handler;
    }

    public void startup() {
//    	this.ipAddr = config.getIP();
    	//Queue queue= null; //config.getInQueue();
    	active = true;
        worker = createWorker();
        worker.start();
    }

    private Thread createWorker() {
        Thread t = new Thread() 
        {
            
            public void run() 
            {
            	MSMQCommon.debug("createWorker:run");
            	try
            	{
            		while (active)
            		{
            			receive();
            		}
            	}
            	catch (java.io.UnsupportedEncodingException use)
            	{
            		MSMQCommon.debug(use.getMessage());
            	}
         	} 
        };
        
        t.setDaemon(true);
        return t;
    }

    public void shutDown() {
        if (worker != null && worker.isAlive()) {
            try {
                worker.join(5000);
            } catch (InterruptedException e) {
            } finally {
                if (worker.isAlive()) {
                    worker.interrupt();
                    
                }
                active = false;
            }
        }
        worker = null;
        
    }


   
    
    private void receive()
            throws java.io.UnsupportedEncodingException
        {
            
	    	MSMQCommon.debug("receive: started");
		 	
		 	String [] response = MSMQCommon.receive(MSMQCommon.RECEIVE, config);
	               
		 	 // Send the results back to an OUtput tool ....
		 	 if (response.length == 2) 
		    // handle message
            try
            {
            	if ( response[1].length() > 0) {
            		ICustomMessage<String> request =
            				new DefaultCustomMessage<String>( response[1], null);
            		byte[] response2 = handler.handleMessage(request);
            		
            		if (! new String(response2).equals("") || ! config.getOutQueue().equals(""))
            		{
            			send(new String(response2));
            		}
            	}
            }
            catch (MessageHandlerException mhe)
            {
            	MSMQCommon.debug(mhe.toString());
            }
            
        }


        private void send(String msgOut)
            throws java.io.UnsupportedEncodingException
        {
        	MSMQCommon.debug("sendMessage: started");
        	MSMQCommon.sendMessage(msgOut,  MSMQCommon.SEND, config); 
        }
}
