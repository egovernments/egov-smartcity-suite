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

    private static final int HOURS_MAX_NUMBER_OF_REQUESTS = 24;
    private static final int MINUTES_MAX_NUMBER_OF_REQUESTS = 60;
    private static final int MAX_NUMBER_OF_REQUESTS = 10;

    /**
     * This method generate the hours list
     * @return List<String>
     */
    public List<String> getAllHour() {
        final List<String> hoursList = new ArrayList<>();
        for (int i = 0; i < HOURS_MAX_NUMBER_OF_REQUESTS; i++)
            if (i < MAX_NUMBER_OF_REQUESTS)
                hoursList.add("0" + i);
            else
                hoursList.add(String.valueOf(i));
        return hoursList;
    }

    /**
     * This method generate the minute list
     * @return
     */
    public List<String> getAllMinute() {
        final List<String> minutesList = new ArrayList<>();
        for (int i = 0; i < MINUTES_MAX_NUMBER_OF_REQUESTS; i += 15)
            if (i < MAX_NUMBER_OF_REQUESTS)
                minutesList.add("0" + i);
            else
                minutesList.add(String.valueOf(i));
        return minutesList;
    }

}
