package com.parasoft.virtualize.tools;

import com.parasoft.api.*;
import com.parasoft.api.responder.*;

public class MSMQConfiguration implements IMSMQConfiguration {

    public final static String IP = "MSMQ Server IP"; 
    public final static String IN_QUEUE = "In QUEUE";
    public final static String OUT_QUEUE = "Out QUEUE"; 
    public final static String TRANSACTIONAL = "Is Transactional? (true/false)"; 
    public final static int    DEFAULT_TIMEOUT = 30000;  // Default timeout for listening to a queue 
    
    private ICustomMessageListenerConfiguration config;

    public MSMQConfiguration(ICustomMessageListenerConfiguration config) {
        this.config = config;
    }

    public String getIP() {
    	if (config.getValue(IP) == null)
    	{
    		return "";
    	}
        return config.getValue(IP);
    }

    public String getInQueue() {
    	if (config.getValue(IN_QUEUE) == null)
    	{
    		return "";
    	}
        return config.getValue(IN_QUEUE);
    }

    public String getOutQueue() {
    	if (config.getValue(OUT_QUEUE) == null)
    	{
    		return "";
    	}
        return config.getValue(OUT_QUEUE);
    }
	
  /*  public boolean isTransactional() {
    	if (config.getValue(TRANSACTIONAL) == null)
    	{
    		// give a default
    		return false;
    	}
        return ( config.getValue(TRANSACTIONAL).toLowerCase().equals("true"));
    }
    */
    
    // Not required for Listener, but here to satisfy Interface
    public String getCorrelationID() {
    	
    		return "L:none";
    	
    }
    
    // Not set in Listener config
    public int getTimeout()
    {
    	return DEFAULT_TIMEOUT;
    }
    
    
}
