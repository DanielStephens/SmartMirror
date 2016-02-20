package com.example.magicmirror;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;

public class NewsModule {
	public interface NewsListener {
        void onNewsUpdate(ArrayList<NewsItem> newsItems);
    }
	
	public static void getNewsEvents(final NewsListener newsListener) {
        new AsyncTask<Void, Void, Void>() {
        	ArrayList<NewsItem> newsItems;
            
            @Override
            protected void onPostExecute(Void aVoid) {
            	newsListener.onNewsUpdate(newsItems);
            }

            @Override
            protected Void doInBackground(Void... params){
            	//NEWS ITEMS
    			newsItems = new ArrayList<NewsItem>();
    			
    			String[] urlList = new String[]{"http://www.bbc.co.uk/news", "http://www.bbc.co.uk/news/uk", "http://www.bbc.co.uk/news/world", "http://www.bbc.co.uk/news/politics", "http://www.bbc.co.uk/news/technology", "http://www.bbc.co.uk/news/science_and_environment", "http://www.bbc.co.uk/news/health"};
    			int[] topicList = new int[]{1, 1, 1, 1, 1, 3, 3};
    			String html;
    			URL url1;
    			InputStream is;
    			int ptr;
    			StringBuffer buffer;
    			
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
    							j++;
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
    							continue;
    						}
    					}
    				}
    			}
    			
    			if(newsItems.size() == 0){
    				newsItems.add(new NewsItem("No news parsed", ""));
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
            	
            	String source = html;//.split("container--primary-and-secondary-columns column-clearfix")[1];
            	//source = source.split("column--single-bottom")[0];
            			
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
            	try {
					String title = source.split("title-link__title-text\">")[1];
					title = title.split("</span>")[0];
					
					String body = source.split("__summary\">")[1];
					body = body.split("</p>")[0];
					
					return new NewsItem(title, body);
				} catch (Exception e) {
					return null;
				}
            }
            
            public NewsItem GetSecondStory(String source){
            	try {
					String title = source.split("__summary\">")[1];
					String[] strs = title.split("title-link__title-text\">");
					title = strs[strs.length-1].split("</span>")[0];
					
					String body = source.split("__summary\">")[2];
					body = body.split("</p>")[0];
					
					return new NewsItem(title, body);
				} catch (Exception e) {
					return null;
				}
            }
            
            public NewsItem GetThirdStory(String source){
            	try {
            		String title = source.split("__summary\">")[2];
					String[] strs = title.split("title-link__title-text\">");
					title = strs[strs.length-1].split("</span>")[0];
					
					String body = source.split("__summary\">")[3];
					body = body.split("</p>")[0];
					
					return new NewsItem(title, body);
				} catch (Exception e) {
					return null;
				}
            }
        }.execute();
    }
}
