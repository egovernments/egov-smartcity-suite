package org.egov.eventnotification.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class EventnotificationUtil {
	
	public static Map<Integer, Integer> getAllHour() {
		final Map<Integer, Integer> hoursMap = new HashMap<>();
		for(int i=0;i<24;i++) {
			hoursMap.put(i, i);
		}
        return hoursMap;
    }
	
	public static Map<Integer, Integer> getAllMinute() {
		final Map<Integer, Integer> minutesMap = new HashMap<>();
		for(int i=0;i<60;i++) {
			minutesMap.put(i, i);
		}
        return minutesMap;
    }

}
