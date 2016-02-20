package com.example.magicmirror;

import java.util.Calendar;

public class EventDetail { // utility class to hold details of an event
	Calendar start, end;
	String name;
	public EventDetail(Calendar s, Calendar e, String n) {
		start = s; end = e; name = n;
	}
	public String toString() {
		return name + CalendarModule.displayTime(start) + "-" + CalendarModule.displayTime(end);
	}
}
