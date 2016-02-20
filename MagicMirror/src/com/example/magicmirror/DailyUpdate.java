package com.example.magicmirror;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import android.os.AsyncTask;

public class DailyUpdate extends AsyncTask<String, Void, DailyResponse> {

    //private Exception exception;
	
	public AsyncResponse delegate = null;

    protected DailyResponse doInBackground(String... urls) {
    	try {
    		//WORD OF THE DAY
    		URL url1 = new URL("http://dictionary.reference.com/wordoftheday/");
			InputStream is = url1.openStream();
			FullscreenActivity.networkAccessed ++;
			int ptr = 0;
			StringBuffer buffer = new StringBuffer();
			while ((ptr = is.read()) != -1) {
			    buffer.append((char)ptr);
			}
			
			String html = buffer.toString();
			
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
			
			//NEWS ITEMS
			ArrayList<NewsItem> newsItems = new ArrayList<NewsItem>();
			String[] urlList = new String[]{"http://www.bbc.co.uk/news", "http://www.bbc.co.uk/news/uk", "http://www.bbc.co.uk/news/world", "http://www.bbc.co.uk/news/politics", "http://www.bbc.co.uk/news/technology", "http://www.bbc.co.uk/news/science_and_environment", "http://www.bbc.co.uk/news/health"};
			int[] topicList = new int[]{1, 1, 1, 1, 1, 3, 3};
			
			for(int i = 0; i < urlList.length; i++){
				html = "";
				try{
			    	url1 = new URL(urlList[i]);
					is = url1.openStream();
					FullscreenActivity.networkAccessed ++;
					ptr = 0;
					buffer = new StringBuffer();
					while ((ptr = is.read()) != -1) {
					    buffer.append((char)ptr);
					}
					
					html = buffer.toString();
		    	}catch(IOException e){
		    		//
		    	}
				for(int j = 0; j < topicList[i]; j++){
					NewsItem n = null;
					while(true){
						n = null;
						if(j > 2){
							break;
						}
						n = GetBBCStory(html, j);
						if(n == null){
							continue;
						}
						
						boolean isNewStory = true;
						for(int k = 0; k < newsItems.size(); k++){
							if(NewsItem.Similar(n, newsItems.get(k))){
								isNewStory = false;
							}
						}
						if(isNewStory){
							newsItems.add(n);
							break;
						}else{
							j++;
						}
					}
				}
			}
			
			
			
			
			
			
			
			
			DailyResponse dR = new DailyResponse();
			
			dR.WordOfDay = word.substring(0, 1).toUpperCase(Locale.UK) + word.substring(1);
			dR.Definitions = defCompilation;
			dR.newsItems = newsItems.toArray(new NewsItem[1]);
			
			return dR;
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return null;
    }
    
    public NewsItem GetBBCStory(String html, int i){
    	if(i < 0 || i > 2){
    		return null;
    	}
    	
    	NewsItem n = null;
    	
    	if(html.equals("")){
    		return n;
    	}
    	
    	String source = html.split("container--primary-and-secondary-columns column-clearfix")[1];
    	source = source.split("column--single-bottom")[0];
    			
    	switch(i){
    	case 0:
    		n = GetFirstStory(source);
    		break;
    	case 1:
    		n = GetSecondStory(source);
    		break;
    	case 2:
    		n = GetThirdStory(source);
    		break;
    	}
    	
    	return n;
    }
    
    public NewsItem GetFirstStory(String source){
    	String title = source.split("title-link__title-text\">")[1];
    	title = title.split("</span>")[0];
    	
    	String body = source.split("buzzard__summary\">")[1];
    	body = body.split("</p>")[0];
    	
    	return new NewsItem(title, body);
    }
    
    public NewsItem GetSecondStory(String source){
    	String title = source.split("title-link__title-text\">")[2];
    	title = title.split("</span>")[0];
    	
    	String body = source.split("pigeon-item__summary\">")[1];
    	body = body.split("</p>")[0];
    	
    	return new NewsItem(title, body);
    }
    
    public NewsItem GetThirdStory(String source){
    	String title = source.split("title-link__title-text\">")[3];
    	title = title.split("</span>")[0];
    	
    	String body = source.split("pigeon-item__summary\">")[2];
    	body = body.split("</p>")[0];
    	
    	return new NewsItem(title, body);
    }
    
    protected void onPostExecute(DailyResponse result) {
    	delegate.dailyUpdateFinished(result);
    }
}
