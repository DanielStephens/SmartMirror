package com.example.magicmirror;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;




import android.content.ContentResolver;
//import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract;

public class CalendarModule {
	public int maxItems = 3;
	
	public interface CalendarListener {
        void onCalendarUpdate(List<String> eventDetails);
    }
	
	public static String displayTime(Calendar c) {
		String s = "";
		if (c.get(Calendar.MINUTE) == 0)
        	s = new SimpleDateFormat("HH", Locale.UK).format(c.getTime());
        else 
        	s = new SimpleDateFormat("HH:mm", Locale.UK).format(c.getTime());
        return s;
	}
	
	public static void getCalendarEvents(final ContentResolver context, final CalendarListener calendarListener) {
        new AsyncTask<Void, Void, Void>() {
            List<String> eventDetails = new ArrayList<String>();
            @Override
            protected void onPostExecute(Void aVoid) {
            	calendarListener.onCalendarUpdate(eventDetails);
            }

            @Override
            protected Void doInBackground(Void... params) {
            	
                Cursor cursor;
                
                //ContentResolver contentResolver = context.getContentResolver();
                final String[] colsToQuery = new String[] 
                		{CalendarContract.Instances.CALENDAR_ID, CalendarContract.Instances.TITLE,
                        CalendarContract.Instances.DESCRIPTION, CalendarContract.Instances.BEGIN,
                        CalendarContract.Instances.END, CalendarContract.Instances.EVENT_LOCATION,
                        CalendarContract.Instances.EVENT_ID, CalendarContract.Instances.EVENT_TIMEZONE};                
                
                long now = System.currentTimeMillis();
                long tomorrow = now + 2*(86400l * 1000l);
                now -= (86400l * 1000l);  // to deal with timezone issues, get several days' worth and then remove unneeded events 
                
                String selection = CalendarContract.Instances.BEGIN + " >= " + now + " and " + CalendarContract.Instances.BEGIN 
                        + " <= " + tomorrow + " and " + CalendarContract.Instances.VISIBLE + " = 1"; 
                String sort = CalendarContract.Instances.BEGIN + " ASC";
                
                Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI.buildUpon();
                ContentUris.appendId(eventsUriBuilder, Long.MIN_VALUE);
                ContentUris.appendId(eventsUriBuilder, Long.MAX_VALUE);
                
                Uri eventsUri = eventsUriBuilder.build();
                
                cursor = context.query(eventsUri, colsToQuery, selection, null, sort);
                
                List<EventDetail> deets = new ArrayList<EventDetail>();
                
                
                // pull all the events and put them in a list of event details
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                    	while (cursor.moveToNext()) {
	                        String title = cursor.getString(1);
	                        if (title.length() > 49)
	                        	title = title.substring(0,44)+"...";
	                        Calendar startTime = Calendar.getInstance();
	                        Calendar endTime = Calendar.getInstance();
	                        
	                        // adjust for timezone because, for some weird reason, all-day events are in GMT regardless of your tz
	                        TimeZone eventTz = TimeZone.getTimeZone(cursor.getString(7));
	                        TimeZone localTz = TimeZone.getDefault();
	                        int diffTz = localTz.getOffset(new Date().getTime()) - eventTz.getOffset(new Date().getTime()); 
	                        startTime.setTimeInMillis(cursor.getLong(3) - diffTz);
	                        endTime.setTimeInMillis(cursor.getLong(4) - diffTz);
	                        
	                        EventDetail e = new EventDetail(startTime, endTime, title);
	                        if(!Contains(deets, e)){
	                        	deets.add(e);
	                        }
                       	}
                    }
                cursor.close();
                }
	            // Now that the time zones are adjusted, re-order and prepare display strings for the events
                for (int i = 1; i < deets.size(); i++) {
                	if (deets.get(i).start.before(deets.get(i-1).start)) {
                		EventDetail e = deets.get(i);
                		deets.remove(i);
                		deets.add(i-1, e);
                		i = 1;
                	}
                }
                
                // Build a list of strings for the mainactivity routine to display
                now = System.currentTimeMillis();
                tomorrow = now + (86400l * 1000l);
                
                for (EventDetail d : deets) {
                	if (d.end.getTimeInMillis() > now && d.start.getTimeInMillis() < tomorrow) {
                		//String timeformat = "h:mm";
                    	String time = displayTime(d.start);
                        if (d.end.getTimeInMillis() != d.start.getTimeInMillis()) 
                        	time += "-" + displayTime(d.end);
                        String displaytext = "\u2022 "+ time + ": " + d.name;
                        
                        // Assume any 24-hour appointment is an all-day reminder and remove the time.
                        if (d.end.getTimeInMillis() >= d.start.getTimeInMillis() + 86400l*1000l) {
                        	displaytext = "\u2022 "+ d.name;
                        }
                        eventDetails.add(displaytext);
                	}
                }
                
                
                return null;
            }
            
            boolean Similar(String n1, String n2){
        		String[] words1 = n1.split(" ");
        		String[] words2 = n2.split(" ");
        		
        		int count = 0;
        		for(int i = 0; i < words1.length; i++){
        			for(int j = 0; j < words2.length; j++){
        				if(words1[i].contains(words2[j]) || words2[j].contains(words1[i])){
        					count ++;
        				}
        			}
        		}
        		
        		if((float)count/(float)words1.length > 0.8f){
        			return true;
        		}
        		
        		return false;
        	}
            
            boolean Approximate(long l1, long l2){
            	return Math.abs(l1-l2)<900000;
            }
            
            boolean Contains(List<EventDetail> es, EventDetail e){
        		for(int i = 0; i < es.size(); i ++){
        			EventDetail e2 = es.get(i);
        			if(Approximate(e2.end.getTimeInMillis(), e.end.getTimeInMillis())
        					&& Approximate(e2.start.getTimeInMillis(), e.start.getTimeInMillis())
        					&& Similar(e2.name, e.name)){
        				return true;
        			}
        		}
        		return false;
        	}
        }.execute();
    }
}
