package com.parasoft.virtualize.tools;

import com.parasoft.api.*;
import com.parasoft.api.responder.ICustomMessageListener;
import com.parasoft.api.responder.ICustomMessageListenerConfiguration;
import com.parasoft.api.responder.IMessageHandler;

import java.util.*;

public class MSMQListener implements ICustomMessageListener {

	// TODO
	// work with Non-Transactional Queues - DONE
	// work with Transactional Queues - Not Required ... ??
	// Security?
	// Send an XML payload	- Done
	// Do some processing within Virtualize - Done	
	

//	private boolean active = false;
	private List<MSMQListenerWorker> workers;

	public MSMQListener() {
        workers = new ArrayList<MSMQListenerWorker>();
    }
	
	
	@Override
	public boolean isReady(ICustomMessageListenerConfiguration arg0) {
		
		MSMQConfiguration config = new MSMQConfiguration(arg0);
		
		if (config.getInQueue().equals("") || config.getIP().equals(""))
		{
			return false;
		}
		
		return true;
		
	}

	@Override
	public void shutdown(ICustomMessageListenerConfiguration arg0) {
	//	active = false;
		shutdown(workers.toArray(new MSMQListenerWorker[] {}));
		if (workers != null)
			workers.clear();
        workers = null;
        Application.showMessage("MSMQ listener stopped");
      
	}

	
	private static void shutdown(MSMQListenerWorker ... workers) {
        Vector<Thread> shutdownThreads = new Vector<Thread>(workers.length);
        for (final MSMQListenerWorker worker : workers) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    worker.shutDown();
                }
            };
            t.start();
            shutdownThreads.add(t);
        }
        for (Thread thread : shutdownThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) { }
        }
    }

	
	
	
	@Override
	public void startup(ICustomMessageListenerConfiguration arg0,	IMessageHandler arg1) {
		
		MSMQConfiguration config = new MSMQConfiguration(arg0);

//		active = false;
		
		MSMQListenerWorker worker = new MSMQListenerWorker(arg1, config);
        worker.startup();
        
        if (workers == null)
        {
        	workers = new ArrayList<MSMQListenerWorker>();
        }
        MSMQCommon.debug("Workers is "  + workers);
        
        workers.add(worker);
		
	} 

}


