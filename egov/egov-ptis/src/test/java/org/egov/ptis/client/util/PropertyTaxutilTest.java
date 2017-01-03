package org.egov.ptis.client.util;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Test;

public class PropertyTaxutilTest {

    @Test
    public void sameDayShouldReturnZero() {
        DateTime date = new DateTime();
        long noofdays = PropertyTaxUtil.getNumberOfDays(date.toDate(), date.toDate());
        assertEquals(noofdays, 0L);
    }

    
    public void consecutiveDays() {
        DateTime date = new DateTime();
        long noofdays = PropertyTaxUtil.getNumberOfDays(date.toDate(), date.withDayOfYear(date.getDayOfYear() + 1).toDate());
        assertEquals(noofdays, 1L);
    }

    @Test
    public void differentDates() {
        DateTime start = new DateTime(2016, 8, 1, 12, 0, 0, 0);
        DateTime end = new DateTime(2016, 8, 30, 12, 0, 0, 0);
        long noofdays = PropertyTaxUtil.getNumberOfDays(start.toDate(), end.toDate());
        assertEquals(noofdays, 29L);
    }

    @Test
    public void negativeResult() {
        DateTime start = new DateTime(2016, 7, 15, 12, 0, 0, 0);
        DateTime end = new DateTime(2016, 7, 1, 12, 0, 0, 0);
        long noofdays = PropertyTaxUtil.getNumberOfDays(start.toDate(), end.toDate());
        assertEquals(noofdays, 0L);
    }

}
