package org.egov.pgr.utils;

import org.joda.time.DateTime;

import java.util.Date;

public class DateUtils {

    public static Date getEnd(final Date date) {
        return new DateTime(date).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toDate();
    }

    public static Date getStart(final Date date) {
        return new DateTime().withTimeAtStartOfDay().toDate();
    }

    public static Date[] getStartAndEnd(final Date startDate, final Date endDate) {
        return new Date[]{getStart(startDate), getEnd(endDate)};
    }
}
