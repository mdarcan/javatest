package com.lab.main;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.lab.conf.RELoadConfig;

@WebListener
public class ContextListener implements ServletContextListener {

	private ScheduledExecutorService scheduler;
	
	@Override
    public void contextInitialized(ServletContextEvent sce) {      	
    	
    	    try {
				RELoadConfig config = RELoadConfig.getInstance();   	    
	        	REBotLogin task = new REBotLogin();
	        	//Calendar date = Calendar.getInstance();
	        	//date.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
	        	//date.set(Calendar.HOUR, 0);
	        	//date.set(Calendar.MINUTE, 0);
	        	//date.set(Calendar.SECOND, 0);
	        	//date.set(Calendar.MILLISECOND, 0);
	        	//timer.schedule(new SampleTask(new Thread(myClass)), new Date(), 60000);
	        	scheduler = Executors.newSingleThreadScheduledExecutor();
	        	scheduler.scheduleAtFixedRate(task, 0, Integer.parseInt(config.getParameterValue("checkdelayseconds")), TimeUnit.SECONDS);
        	
			} catch (IOException e) {
				e.printStackTrace();
			}        	
        }

    @Override
    public void contextDestroyed(ServletContextEvent sce){
    	scheduler.shutdownNow();
    }
}