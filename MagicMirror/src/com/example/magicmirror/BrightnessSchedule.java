package com.example.magicmirror;

public class BrightnessSchedule {
	public int wakeTime = 7;
	public int sleepTime = 22;
	
	public int sunsetH;
	public int sunsetM;
	
	public int decreaseBrightnessTime;
	
	public void SetBrightness(int hh, int mm, int ss){
		
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
		dimProgress = dimProgress*0.9f;
		
		FullscreenActivity.instance.SetOverlay((int)(255f*dimProgress));
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