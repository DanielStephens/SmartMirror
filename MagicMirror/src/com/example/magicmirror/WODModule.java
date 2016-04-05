package com.example.magicmirror;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import android.R.string;
import android.os.AsyncTask;

public class WODModule {
	public interface WODListener {
        void onWODUpdate(String[] wodDetails);
    }
	
	public static void getWODEvents(final WODListener wodListener) {
        new AsyncTask<Void, Void, Void>() {
            String[] wodDetails;
            
            @Override
            protected void onPostExecute(Void aVoid) {
            	wodListener.onWODUpdate(wodDetails);
            }

            @Override
            protected Void doInBackground(Void... params){
            	String[] strs = new String[2];
            	
            	try {
            		String html = "";
            		
            		while(html == ""){
	            		//WORD OF THE DAY
	            		URL url1 = new URL("http://dictionary.reference.com/wordoftheday/");
	        			InputStream is = url1.openStream();
	        			FullscreenActivity.networkAccessed ++;
	        			int ptr = 0;
	        			StringBuffer buffer = new StringBuffer();
	        			while ((ptr = is.read()) != -1) {
	        			    buffer.append((char)ptr);
	        			}
	        			
	        			html = buffer.toString();
            		}
        			
        			String[] temp = html.split("Definitions for <strong>");
        			String word = temp[1].split("<")[0];
        			String[] list = temp[1].split("</ol>")[0].split("<span>");
        			
        			int listItems = Math.min(list.length-1, 3);
        			
        			String defCompilation = "";
        			
        			for(int i = 0; i < listItems; i++){
        				defCompilation += "\u2022 ";
        				
        				defCompilation += list[i+1].split("</span>")[0];
        				
        				if(i < listItems-1){
        					defCompilation += "\n";
        				}
        			}
        			
        			defCompilation = defCompilation.replaceAll("<em>", "(");
        			defCompilation = defCompilation.replaceAll("</em>", ")");
        			
        			strs[0] = word.substring(0, 1).toUpperCase(Locale.UK) + word.substring(1);
        			
        			String[] substr = defCompilation.split("<+/+.+?>");
        			defCompilation = "";
        			for(int i = 0; i < substr.length; i++){
        				defCompilation += substr[i];
        			}
        			substr = defCompilation.split("<+.+?>");
        			defCompilation = "";
        			for(int i = 0; i < substr.length; i++){
        				defCompilation += substr[i];
        			}
        			
        			
        			
        			int l = 120;
        			if(defCompilation.length() > l){
        				defCompilation = defCompilation.substring(0, l-3)+"...";
        			}
        			strs[1] = defCompilation;
            	}catch(IOException e){
            		
            	}
            	
            	wodDetails = strs;
            	return null;
            }
        }.execute();
    }
}
