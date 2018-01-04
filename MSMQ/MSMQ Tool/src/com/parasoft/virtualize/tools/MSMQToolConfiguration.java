package com.parasoft.virtualize.tools;

import com.parasoft.api.tool.*;

public class MSMQToolConfiguration implements IMSMQConfiguration {

    public final static String IP = "ip"; 
    public final static String IN_QUEUE = "inqueue";
    public final static String OUT_QUEUE = "outqueue"; 
    public final static String CORRELATION_ID = "correlationid";
    public final static String TIMEOUT = "timeout";
    public final static int DEFAULT_TIMEOUT = 30000;
 //   public final static String TRANSACTIONAL = "Is Transactional? (true/false)"; 
    
    private ICustomToolConfiguration config;

    public MSMQToolConfiguration(ICustomToolConfiguration config) {
        this.config = config;
    }

    public String getIP() {
    	MSMQCommon.debug("IP is : " + config.getString(IP));
    	if (config.getString(IP) == null)
    	{
    		return "";
    	}
        return config.getString(IP);
    }

    public String getInQueue() {
    	if (config.getString(IN_QUEUE) == null)
    	{
    		return "";
    	}
        return config.getString(IN_QUEUE);
    }

    public String getOutQueue() {
    	if (config.getString(OUT_QUEUE) == null)
    	{
    		return "";
    	}
        return config.getString(OUT_QUEUE);
    }
    
    public String getCorrelationID() {
    	if (config.getString(CORRELATION_ID) == null || config.getString(CORRELATION_ID).equals(""))
    	{
    		return "L:none";
    	}
        return config.getString(CORRELATION_ID);
    }
    
    public int getTimeout() {
    	if (config.getString(TIMEOUT) == null || config.getString(TIMEOUT).equals("") )
    	{
    		return DEFAULT_TIMEOUT;
    	}
    	
    	String rVal = config.getString(TIMEOUT);
    	int iVal = new Integer(rVal).intValue();
        return iVal;
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
    
    
}
