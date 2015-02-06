package org.egov.infra.utils;

import org.joda.time.DateTime;

import java.util.Date;

public class DateUtils {

    public static Date endOfDay(final Date date) {
        return new DateTime(date).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toDate();
    }

    public static Date startOfDay(final Date date) {
        return new DateTime().withTimeAtStartOfDay().toDate();
    }

    public static Date[] getStartAndEndOfDay(final Date startDate, final Date endDate) {
        return new Date[]{startOfDay(startDate), endOfDay(endDate)};
    }
}
