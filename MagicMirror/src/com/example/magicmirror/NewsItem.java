package com.example.magicmirror;

public class NewsItem {
	public String Title;
	public String Summary;
	
	public NewsItem(String t, String s){
		Title = t.replaceAll("&quot;", "\"");
		Summary = s.replaceAll("&quot;", "\"");
	}
	
	static boolean Similar(NewsItem n1, NewsItem n2){
		String[] words1 = n1.Title.split(" ");
		String[] words2 = n2.Title.split(" ");
		
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
}
