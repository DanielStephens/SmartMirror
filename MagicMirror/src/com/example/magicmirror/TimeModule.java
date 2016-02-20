package com.example.magicmirror;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeModule {
	public interface TimeListener {
		void onMilliUpdate(float milli);
		void onTimeUpdate(String t, String s, int hh, int mm, int ss);
		void onDateUpdate(String[] d);
        void onMagnitudeUpdate(int magnitude);
    }
	
	static int milliseconds;
	static int seconds;
	static int minutes;
	static int hours;
    
	static int prevSeconds;
	static int prevMinutes;
	static int prevHours;
    
	static int prevDate;
	static int date;
    
	static String formattedTime;
	static String formattedSeconds;
	static String[] formattedDate = new String[3];
	
	public static void getTime(final TimeListener timeListener){
    	Calendar c = Calendar.getInstance();
    	
    	milliseconds = c.get(Calendar.MILLISECOND);
    	seconds = c.get(Calendar.SECOND);
    	minutes = c.get(Calendar.MINUTE);
    	hours = c.get(Calendar.HOUR_OF_DAY);
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.UK);
    	formattedTime = sdf.format(c.getTime());
    	
    	sdf = new SimpleDateFormat("ss", Locale.UK);
    	formattedSeconds = sdf.format(c.getTime());
    	
    	String day = new SimpleDateFormat("EEEE", Locale.UK).format(c.getTime());
	    String month = new SimpleDateFormat("MMMM", Locale.UK).format(c.getTime());
	    date = c.get(Calendar.DAY_OF_MONTH);
	    
	    String ending = "th";
	    if((date - 1) % 10 == 0){
	    	ending = "st";
	    }else if((date - 2) % 10 == 0){
	    	ending = "nd";
	    }else if((date - 3) % 10 == 0){
	    	ending = "rd";
	    }
	    
	    formattedDate[0] = day + " the " + date;
	    formattedDate[1] = ending;
	    formattedDate[2] = " of " + month;
    	
	    onPostExecute(timeListener);
    }
	
	static void onPostExecute(TimeListener timeListener) {
    	if(FullscreenActivity.isSleeping){
    		if(prevSeconds != seconds){
        		timeListener.onTimeUpdate(formattedTime, formattedSeconds, hours, minutes, seconds);
        		//prevSeconds = seconds;
    		}
    		return;
    	}
    	
    	timeListener.onMilliUpdate(((float)milliseconds)/1000f);
    	
    	int mag = 0;
    	
    	if(prevSeconds != seconds){
    		timeListener.onTimeUpdate(formattedTime, formattedSeconds, hours, minutes, seconds);
    		mag = seconds % 10 == 0 ? 2 : 1;
    		prevSeconds = seconds;
    		
    		if(prevMinutes != minutes){
    			mag = minutes % 10 == 0 ? 4 : 3;
    			prevMinutes = minutes;
    			
    			if(prevHours != hours){
    				mag = hours % 10 == 0 ? 6 : 5;
    				prevHours = hours;
    				
    				if(prevDate != date){
            			timeListener.onDateUpdate(formattedDate);
            			prevDate = date;
            		}
    				
    			}
    		}
    		
    		timeListener.onMagnitudeUpdate(mag);
    	}
    	
    	//if something
    	//onMagnitudeUpdate
    }
}
