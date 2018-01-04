package com.parasoft.virtualize.tools;


/*
 * Provides a common Interface for both Tool and Listener configurations 
 */

public interface IMSMQConfiguration {
	
	    public String getIP();
	    
	    public String getInQueue();

	    public String getOutQueue();
	    
	    public String getCorrelationID();

	    public int getTimeout();
}
