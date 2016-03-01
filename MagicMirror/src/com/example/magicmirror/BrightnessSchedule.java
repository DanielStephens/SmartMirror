package com.example.magicmirror;

import android.provider.Settings.SettingNotFoundException;

public class BrightnessSchedule {
	public int wakeTime = 7;
	public int sleepTime = 22;
	
	public int sunsetH;
	public int sunsetM;
	public int a;
	public int b;
	
	public int decreaseBrightnessTime;
	
	public void SetBrightness(int hh, int mm, int ss){
		/*
		int tt = hh*3600+mm*60+ss;
		
		if(!FullscreenActivity.isSleeping && tt >= a && tt <= b){
			FullscreenActivity.instance.Sleep(true);
			return;
		}else if(FullscreenActivity.isSleeping){
			if(tt > b || tt < a){
				FullscreenActivity.instance.Sleep(false);
				return;
			}
		}
		*/
		
		if(FullscreenActivity.isSleeping && hh >= wakeTime && hh <= sleepTime){
			FullscreenActivity.instance.Sleep(false);
			return;
		}else if(!FullscreenActivity.isSleeping){
			if(hh >= sleepTime || hh < wakeTime){
				FullscreenActivity.instance.Sleep(true);
				return;
			}
		}
		
		
		if(FullscreenActivity.isSleeping) return;
		if(sunsetH == 0 && sunsetM == 0) return;
		
		int start = (sunsetH-1)*60+sunsetM;
		int end = (sunsetH+1)*60+sunsetM;
		
		int t = hh*60 + mm;
		
		float dimProgress = Lerp(start, end, t);
		dimProgress = 1 - dimProgress*0.9f;
		
		try {
			FullscreenActivity.instance.SetBright(dimProgress);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	float Lerp(int start, int end, int t){
		if(t <= start) return 0;
		if(t >= end) return 1;
		
		
		float t2 = t-start;
		float range = end-start;
		float val = t2/range;
		
		return val;
	}
}
