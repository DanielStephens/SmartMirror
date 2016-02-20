package com.example.magicmirror;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.os.AsyncTask;

public class WeatherModule {
	public interface WeatherListener {
		void onWeatherUpdate(String[] weatherDetails);
        void onSunsetUpdate(int hh, int mm);
    }
	
	public static void getWeatherEvents(final WeatherListener weatherListener) {
        new AsyncTask<Void, Void, Void>() {
            String[] weatherDetails;
            
            @Override
            protected void onPostExecute(Void aVoid) {
            	weatherListener.onWeatherUpdate(weatherDetails);
            }

            @Override
            protected Void doInBackground(Void... params){
            	String[] strs = new String[4];
            	
            	try {
            		URL url1 = new URL("http://www.bbc.co.uk/weather/2654675");
        			InputStream is = url1.openStream();
        			FullscreenActivity.networkAccessed ++;
        			int ptr = 0;
        			StringBuffer buffer = new StringBuffer();
        			while ((ptr = is.read()) != -1) {
        			    buffer.append((char)ptr);
        			}
        			
        			String html = buffer.toString();
        			String[] temp = html.split("<span class=\"units-values temperature-units-values\"><span data-unit=\"c\" class=\"units-value temperature-value temperature-value-unit-c\">");
        			strs[0] = temp[0].split("<td class=\"weather\">")[1].split("<p>")[1].split("</p>")[0];
        			strs[1] = temp[1].split("<")[0];
        			strs[2] = temp[2].split("<")[0];
        			temp = html.split("<tr class=\"temperature\">");
        			strs[3] = temp[1].split("units-value temperature-value temperature-value-unit-c\">")[1].split("<")[0];
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
            	
            	weatherDetails = strs;
            	return null;
            }
        }.execute();
    }
	
	public static void getSunset(final WeatherListener weatherListener) {
        new AsyncTask<Void, Void, Void>() {
        	int sunsetH;
            int sunsetM;
            
            @Override
            protected void onPostExecute(Void aVoid) {
            	weatherListener.onSunsetUpdate(sunsetH, sunsetM);
            }

            @Override
            protected Void doInBackground(Void... params){
            	try {
            		URL url1 = new URL("http://www.bbc.co.uk/weather/2654675");
        			InputStream is = url1.openStream();
        			FullscreenActivity.networkAccessed ++;
        			int ptr = 0;
        			StringBuffer buffer = new StringBuffer();
        			while ((ptr = is.read()) != -1) {
        			    buffer.append((char)ptr);
        			}
        			
        			String html = buffer.toString();
        			String time = html.split("\"sunset\">Sunset ")[1];
        			time = time.split("<")[0];
        			
        			sunsetH = Integer.parseInt(time.split(":")[0]);
        			sunsetM = Integer.parseInt(time.split(":")[1]);
        			
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
            	return null;
            }
        }.execute();
    }
}
