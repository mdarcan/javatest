package com.lab.test;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.lab.conf.RELoadConfig;
import com.lab.main.REBotLogin;

public class test {

	public test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		 	    
		Integer sched1Hour = Integer.valueOf(((RELoadConfig.getInstance().getParameterValue("schedule1")).split(":"))[0]);
		Integer sched1Minute = Integer.valueOf(RELoadConfig.getInstance().getParameterValue("schedule1").split(":")[1]);
		Integer sched1Second = Integer.valueOf(RELoadConfig.getInstance().getParameterValue("schedule1").split(":")[2]);
		
		Integer sched2Hour = Integer.valueOf(((RELoadConfig.getInstance().getParameterValue("schedule2")).split(":"))[0]);
		Integer sched2Minute = Integer.valueOf(RELoadConfig.getInstance().getParameterValue("schedule2").split(":")[1]);
		Integer sched2Second = Integer.valueOf(RELoadConfig.getInstance().getParameterValue("schedule2").split(":")[2]);
		
		
		LocalTime sched1Time = LocalTime.of(sched1Hour,sched1Minute,sched1Second,0);
		LocalTime sched2Time = LocalTime.of(sched2Hour,sched2Minute,sched2Second,0);
				
    	REBotLogin task = new REBotLogin(2, "[Schedulazione " + RELoadConfig.getInstance().getParameterValue("schedule1") + "]");
    	Calendar firstSchedule = Calendar.getInstance(TimeZone.getTimeZone(RELoadConfig.getInstance().getParameterValue("timezone")));
    	firstSchedule.set(Calendar.HOUR_OF_DAY, sched1Time.getHour());
    	firstSchedule.set(Calendar.MINUTE, sched1Time.getMinute());
    	firstSchedule.set(Calendar.SECOND, sched1Time.getSecond());
    	firstSchedule.set(Calendar.MILLISECOND, 0);
    	
    	Date ddd = firstSchedule.getTime();
    	
    	REBotLogin task2 = new REBotLogin(3, "[Schedulazione " + RELoadConfig.getInstance().getParameterValue("schedule1") + "]");
    	Calendar secondSchedule = Calendar.getInstance(TimeZone.getTimeZone(RELoadConfig.getInstance().getParameterValue("timezone")));
    	secondSchedule.set(Calendar.HOUR_OF_DAY, sched2Time.getHour());
    	secondSchedule.set(Calendar.MINUTE, sched2Time.getMinute());
    	secondSchedule.set(Calendar.SECOND, sched2Time.getSecond());
    	secondSchedule.set(Calendar.MILLISECOND, 0);	        	
    	
    	long initialFirstInterval = firstSchedule.getTimeInMillis()-System.currentTimeMillis();
    	long initialSecondInterval = secondSchedule.getTimeInMillis()-System.currentTimeMillis();
    	
    	if (initialFirstInterval < 1) {
    		initialFirstInterval += 24 * 60 * 60 * 1000;	
    	}
    	
    	if (initialSecondInterval < 1) {
    		initialSecondInterval += 24 * 60 * 60 * 1000;	
    	}	        	
    	
		System.out.println("Schedule #1 - at " + RELoadConfig.getInstance().getParameterValue("schedule1") + " start in " + 
		        TimeUnit.MILLISECONDS.toHours(initialFirstInterval) %24 + "h " +
		        TimeUnit.MILLISECONDS.toMinutes(initialFirstInterval) %60 + "m " + 
		        TimeUnit.MILLISECONDS.toSeconds(initialFirstInterval) %60 + "s");   


		System.out.println("Schedule #2 - at " + RELoadConfig.getInstance().getParameterValue("schedule2") + " start in " + 
	        TimeUnit.MILLISECONDS.toHours(initialSecondInterval) %24 + "h " +
	        TimeUnit.MILLISECONDS.toMinutes(initialSecondInterval) %60 + "m " + 
	        TimeUnit.MILLISECONDS.toSeconds(initialSecondInterval) %60 + "s");    	


	}

}
