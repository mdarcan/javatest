package com.lab.main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.lab.conf.RELoadConfig;

@WebListener
public class ContextListener implements ServletContextListener {

	private ScheduledExecutorService scheduler1;
	private ScheduledExecutorService scheduler2;	
	private ScheduledExecutorService scheduler3;
	private ScheduledExecutorService scheduler4;		
	
	@Override
    public void contextInitialized(ServletContextEvent sce) {      	
    	
    	    try {
    			Integer sched1Hour = Integer.valueOf(((RELoadConfig.getInstance().getParameterValue("schedule1")).split(":"))[0]);
    			Integer sched1Minute = Integer.valueOf(RELoadConfig.getInstance().getParameterValue("schedule1").split(":")[1]);
    			Integer sched1Second = Integer.valueOf(RELoadConfig.getInstance().getParameterValue("schedule1").split(":")[2]);
    			
    			Integer sched2Hour = Integer.valueOf(((RELoadConfig.getInstance().getParameterValue("schedule2")).split(":"))[0]);
    			Integer sched2Minute = Integer.valueOf(RELoadConfig.getInstance().getParameterValue("schedule2").split(":")[1]);
    			Integer sched2Second = Integer.valueOf(RELoadConfig.getInstance().getParameterValue("schedule2").split(":")[2]);
    			
    			Integer sched3Hour = Integer.valueOf(((RELoadConfig.getInstance().getParameterValue("schedule3")).split(":"))[0]);
    			Integer sched3Minute = Integer.valueOf(RELoadConfig.getInstance().getParameterValue("schedule3").split(":")[1]);
    			Integer sched3Second = Integer.valueOf(RELoadConfig.getInstance().getParameterValue("schedule3").split(":")[2]);    			
    	
    			Integer sched4Hour = Integer.valueOf(((RELoadConfig.getInstance().getParameterValue("schedule4")).split(":"))[0]);
    			Integer sched4Minute = Integer.valueOf(RELoadConfig.getInstance().getParameterValue("schedule4").split(":")[1]);
    			Integer sched4Second = Integer.valueOf(RELoadConfig.getInstance().getParameterValue("schedule4").split(":")[2]);    			
       			
    			
    			LocalTime sched1Time = LocalTime.of(sched1Hour,sched1Minute,sched1Second,0);
    			LocalTime sched2Time = LocalTime.of(sched2Hour,sched2Minute,sched2Second,0);
    			LocalTime sched3Time = LocalTime.of(sched3Hour,sched3Minute,sched3Second,0);
    			LocalTime sched4Time = LocalTime.of(sched4Hour,sched4Minute,sched4Second,0);    			
    					
    	    	REBotLogin task = new REBotLogin(1, "[Schedulazione " + RELoadConfig.getInstance().getParameterValue("schedule1") + "]");
    	    	Calendar firstSchedule = Calendar.getInstance(TimeZone.getTimeZone(RELoadConfig.getInstance().getParameterValue("timezone")));
    	    	firstSchedule.set(Calendar.HOUR_OF_DAY, sched1Time.getHour());
    	    	firstSchedule.set(Calendar.MINUTE, sched1Time.getMinute());
    	    	firstSchedule.set(Calendar.SECOND, sched1Time.getSecond());
    	    	firstSchedule.set(Calendar.MILLISECOND, 0);
    	    	   	    	
    	    	REBotLogin task2 = new REBotLogin(2, "[Schedulazione " + RELoadConfig.getInstance().getParameterValue("schedule2") + "]");
    	    	Calendar secondSchedule = Calendar.getInstance(TimeZone.getTimeZone(RELoadConfig.getInstance().getParameterValue("timezone")));
    	    	secondSchedule.set(Calendar.HOUR_OF_DAY, sched2Time.getHour());
    	    	secondSchedule.set(Calendar.MINUTE, sched2Time.getMinute());
    	    	secondSchedule.set(Calendar.SECOND, sched2Time.getSecond());
    	    	secondSchedule.set(Calendar.MILLISECOND, 0);	 
    	    	
    	    	REBotLogin task3 = new REBotLogin(3, "[Schedulazione " + RELoadConfig.getInstance().getParameterValue("schedule3") + "]");
    	    	Calendar thirdSchedule = Calendar.getInstance(TimeZone.getTimeZone(RELoadConfig.getInstance().getParameterValue("timezone")));
    	    	thirdSchedule.set(Calendar.HOUR_OF_DAY, sched3Time.getHour());
    	    	thirdSchedule.set(Calendar.MINUTE, sched3Time.getMinute());
    	    	thirdSchedule.set(Calendar.SECOND, sched3Time.getSecond());
    	    	thirdSchedule.set(Calendar.MILLISECOND, 0);
    	    	
    	    	REBotLogin task4 = new REBotLogin(4, "[Schedulazione " + RELoadConfig.getInstance().getParameterValue("schedule4") + "]");
    	    	Calendar fourthSchedule = Calendar.getInstance(TimeZone.getTimeZone(RELoadConfig.getInstance().getParameterValue("timezone")));
    	    	fourthSchedule.set(Calendar.HOUR_OF_DAY, sched4Time.getHour());
    	    	fourthSchedule.set(Calendar.MINUTE, sched4Time.getMinute());
    	    	fourthSchedule.set(Calendar.SECOND, sched4Time.getSecond());
    	    	fourthSchedule.set(Calendar.MILLISECOND, 0);    	    	
    	    	
    	    	long initialFirstInterval = firstSchedule.getTimeInMillis()-System.currentTimeMillis();
    	    	long initialSecondInterval = secondSchedule.getTimeInMillis()-System.currentTimeMillis();
    	    	long initialThirdInterval = thirdSchedule.getTimeInMillis()-System.currentTimeMillis();
    	    	long initialFourthInterval = fourthSchedule.getTimeInMillis()-System.currentTimeMillis();    	    	
    	    	
    	    	if (initialFirstInterval < 1) {
    	    		initialFirstInterval += 24 * 60 * 60 * 1000;	
    	    	}
    	    	
    	    	if (initialSecondInterval < 1) {
    	    		initialSecondInterval += 24 * 60 * 60 * 1000;	
    	    	}
    	    	
    	    	if (initialThirdInterval < 1) {
    	    		initialThirdInterval += 24 * 60 * 60 * 1000;	
    	    	}	        	

    	    	if (initialFourthInterval < 1) {
    	    		initialFourthInterval += 24 * 60 * 60 * 1000;	
    	    	}	        	
    	    	

	        	scheduler1= Executors.newSingleThreadScheduledExecutor();
	        	scheduler1.scheduleAtFixedRate(task, initialFirstInterval, 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
	        	
	    		System.out.println("Schedule #1 - at Timezone -> " + RELoadConfig.getInstance().getParameterValue("schedule1") + " & Local -> " + new SimpleDateFormat("HH:mm:ss").format(firstSchedule.getTime()) + " Local Actual Time -> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date(System.currentTimeMillis())) + " start in " + 
	    		        TimeUnit.MILLISECONDS.toHours(initialFirstInterval) %24 + "h " +
	    		        TimeUnit.MILLISECONDS.toMinutes(initialFirstInterval) %60 + "m " + 
	    		        TimeUnit.MILLISECONDS.toSeconds(initialFirstInterval) %60 + "s");   

	    		
	        	scheduler2= Executors.newSingleThreadScheduledExecutor();
	        	scheduler2.scheduleAtFixedRate(task2, initialSecondInterval, 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
	        	
	    		System.out.println("Schedule #2 - at Timezone -> " + RELoadConfig.getInstance().getParameterValue("schedule2") + " & Local -> " + new SimpleDateFormat("HH:mm:ss").format(secondSchedule.getTime()) + " Local Actual Time -> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date(System.currentTimeMillis())) + " start in " + 
	    		        TimeUnit.MILLISECONDS.toHours(initialSecondInterval) %24 + "h " +
	    		        TimeUnit.MILLISECONDS.toMinutes(initialSecondInterval) %60 + "m " + 
	    		        TimeUnit.MILLISECONDS.toSeconds(initialSecondInterval) %60 + "s");   
	    		
	        	scheduler3= Executors.newSingleThreadScheduledExecutor();
	        	scheduler3.scheduleAtFixedRate(task3, initialThirdInterval, 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
	        	
	    		System.out.println("Schedule #3 - at Timezone -> " + RELoadConfig.getInstance().getParameterValue("schedule3") + " & Local -> " + new SimpleDateFormat("HH:mm:ss").format(thirdSchedule.getTime()) + " Local Actual Time -> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date(System.currentTimeMillis())) + " start in " + 
	    		        TimeUnit.MILLISECONDS.toHours(initialThirdInterval) %24 + "h " +
	    		        TimeUnit.MILLISECONDS.toMinutes(initialThirdInterval) %60 + "m " + 
	    		        TimeUnit.MILLISECONDS.toSeconds(initialThirdInterval) %60 + "s"); 
	    		
	        	scheduler4= Executors.newSingleThreadScheduledExecutor();
	        	scheduler4.scheduleAtFixedRate(task4, initialFourthInterval, 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
	        	
	    		System.out.println("Schedule #4 - at Timezone -> " + RELoadConfig.getInstance().getParameterValue("schedule4") + " & Local -> " + new SimpleDateFormat("HH:mm:ss").format(fourthSchedule.getTime()) + " Local Actual Time -> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date(System.currentTimeMillis())) + " start in " + 
	    		        TimeUnit.MILLISECONDS.toHours(initialFourthInterval) %24 + "h " +
	    		        TimeUnit.MILLISECONDS.toMinutes(initialFourthInterval) %60 + "m " + 
	    		        TimeUnit.MILLISECONDS.toSeconds(initialFourthInterval) %60 + "s"); 	    		
        	
			} catch (IOException e) {
				e.printStackTrace();
			}        	
        }

    @Override
    public void contextDestroyed(ServletContextEvent sce){
    	scheduler1.shutdownNow();
   	//scheduler2.shutdownNow();
    }
}