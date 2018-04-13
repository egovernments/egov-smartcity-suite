package org.egov.eventnotification.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class EventnotificationUtil {
	
	public static List<String> getAllHour() {
		final List<String> hoursList = new ArrayList<>();
		for(int i=0;i<24;i++) {
			if(i < 10) {
				hoursList.add("0"+i);
			}else {
				hoursList.add(String.valueOf(i));
			}
			
		}
        return hoursList;
    }
	
	public static List<String> getAllMinute() {
		final List<String> minutesList = new ArrayList<>();
		for(int i=0;i<60;i++) {
			if(i < 10) {
				minutesList.add("0"+i);
			}else {
				minutesList.add(String.valueOf(i));
			}
		}
        return minutesList;
    }

}
