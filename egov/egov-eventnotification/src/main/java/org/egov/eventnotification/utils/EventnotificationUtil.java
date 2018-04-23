package org.egov.eventnotification.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * This is util class for common task like generate list of values
 * @author somvit
 *
 */
@Service
public class EventnotificationUtil {

    /**
     * This method generate the hours list
     * @return List<String>
     */
    public static List<String> getAllHour() {
        final List<String> hoursList = new ArrayList<>();
        for (int i = 0; i < 24; i++)
            if (i < 10)
                hoursList.add("0" + i);
            else
                hoursList.add(String.valueOf(i));
        return hoursList;
    }

    /**
     * This method generate the minute list
     * @return
     */
    public static List<String> getAllMinute() {
        final List<String> minutesList = new ArrayList<>();
        for (int i = 0; i < 60; i++)
            if (i < 10)
                minutesList.add("0" + i);
            else
                minutesList.add(String.valueOf(i));
        return minutesList;
    }

}
