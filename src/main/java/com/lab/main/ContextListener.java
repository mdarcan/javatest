package com.lab.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class ContextListener implements ServletContextListener {

	private ScheduledExecutorService scheduler1;
		
	@Override
    public void contextInitialized(ServletContextEvent sce) {      	
    	
    	    REBotLogin task = new REBotLogin(1, "[Schedulazione oraria]");

			scheduler1= Executors.newSingleThreadScheduledExecutor();
			scheduler1.scheduleAtFixedRate(task, 0, 60 * 60 * 1000, TimeUnit.MILLISECONDS);
			
			System.out.println("Schedule #1 - Local Actual Time -> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date(System.currentTimeMillis())));        	
        }

    @Override
    public void contextDestroyed(ServletContextEvent sce){
    	scheduler1.shutdownNow();
   	//scheduler2.shutdownNow();
    }
}