package com.example.magicmirror;

public interface AsyncResponse {
	void processFinished(String[] output);
	void dailyUpdateFinished(DailyResponse output);
}
