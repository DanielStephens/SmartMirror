package com.example.magicmirror;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.os.AsyncTask;

public class NetworkWeather extends AsyncTask<String, Void, String[]> {

    //private Exception exception;
	
	public AsyncResponse delegate = null;

    protected String[] doInBackground(String... urls) {
    	try {
			String[] strs = new String[4];
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
			strs[3] = temp[1].split("units-value temperature-value temperature-value-unit-c\">")[1].split("<")[0]; //html.contains("(<span class=\"units-values temperature-units-values\"><span data-unit=\"c\" class=\"temperature-value-unit-c\">°C</span><span class=\"unit-types-separator\">/</span><span data-unit=\"f\" class=\"temperature-value-unit-f\">°F</span></span>)") ? "true" : "false";// temp[1];//.split("class=\"units-value temperature-value temperature-value-unit-c\">")[1];
			return strs;
			//tempValue.setText(temp);
			//°C
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return null;
    }
    
    protected void onPostExecute(String[] result) {
    	delegate.processFinished(result);
    }
}
