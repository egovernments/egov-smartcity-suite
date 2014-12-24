package org.egov.infstr.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class DateUtilTest {

	@Test
	public void DateUtilsTest() {
		final FinancialYear finYear = DateUtils.getFinancialYear();
		assertNotNull(finYear);

		assertNotNull(finYear.getCurrentYear());
		assertNotNull(finYear.getEndOnOnDate());
		assertNotNull(finYear.getStartOnDate());
		assertNotNull(finYear.toString());

		final Date dt = new Date();
		int value = DateUtils.getNumberOfMonthsPassesInYear(dt, dt);
		assertEquals(0, value);

		value = DateUtils.getNumberOfYearPassesed(dt, dt);
		assertEquals(0, value);

		value = DateUtils.getNumberOfMonthsPassedinCurrFinYear();
		final GregorianCalendar calendar = new GregorianCalendar();
		final int startMonth = calendar.get(Calendar.MONTH);
		int expect = 0;
		if (startMonth < 3) {
			expect = startMonth + 9;
		} else {
			expect = startMonth - 3;
		}
		assertEquals(expect, value);

		value = DateUtils.getNumberOfMonths(dt, dt);
		assertEquals(-1, value);

		value = DateUtils.getLastFinYear();
		assertEquals(calendar.get(Calendar.YEAR) - 1, value);

		final Date dts = DateUtils.getLastFinYearStartDate();
		assertNotNull(dts);
		assertTrue(DateUtils.compareDates(null, dt));
		assertTrue(DateUtils.compareDates(dt, null));
		assertTrue(DateUtils.compareDates(dt, dt));
		calendar.setTime(new Date(System.currentTimeMillis() + 100000000));
		assertFalse(DateUtils.compareDates(dt, calendar.getTime()));
		assertTrue(DateUtils.compareDates(calendar.getTime(), dt));

		final DateUtils dtsutil = new DateUtils();
		assertTrue(dtsutil.checkDateFormat("10/10/2000"));

		assertTrue(dtsutil.checkDateFormat(""));

		assertNotNull(DateUtils.createDate(2000));

		String frmtdDate = DateUtils.getFormattedDate(dt, "dd/MM/yyyy");
		assertTrue(dtsutil.checkDateFormat(frmtdDate));

		frmtdDate = DateUtils.getDefaultFormattedDate(dt);
		assertTrue(dtsutil.checkDateFormat(frmtdDate));
	}

	private Calendar getCalendarForDate(final Date date) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	@Test
	public void testGetDate() {
		final int dayExpected = 01;
		final int monthExpected = 02;
		final int yearExpected = 2000;

		final Date actualDate = DateUtils.getDate(yearExpected, monthExpected,
				dayExpected);
		final Calendar calendarActual = getCalendarForDate(actualDate);

		assertEquals(dayExpected, calendarActual.get(Calendar.DAY_OF_MONTH));
		assertEquals(monthExpected, calendarActual.get(Calendar.MONTH));
		assertEquals(yearExpected, calendarActual.get(Calendar.YEAR));
	}

	@Test
	public void testAdd() {
		final Date today = DateUtils.today();
		final Date expectedTomorrow = DateUtils.tomorrow();
		final Date actualTomorrow = DateUtils.add(today, Calendar.DAY_OF_MONTH,
				1);

		final Calendar calendarExpected = getCalendarForDate(expectedTomorrow);
		final Calendar calendarActual = getCalendarForDate(actualTomorrow);

		assertEquals(calendarExpected.get(Calendar.DAY_OF_MONTH),
				calendarActual.get(Calendar.DAY_OF_MONTH));
		assertEquals(calendarExpected.get(Calendar.MONTH),
				calendarActual.get(Calendar.MONTH));
		assertEquals(calendarExpected.get(Calendar.YEAR),
				calendarActual.get(Calendar.YEAR));
	}

	@Test
	public void testNow() {
		final Calendar calendarExpected = getCalendarForDate(new Date());
		final Calendar calendarActual = getCalendarForDate(DateUtils.now());

		assertEquals(calendarExpected.get(Calendar.DAY_OF_MONTH),
				calendarActual.get(Calendar.DAY_OF_MONTH));
		assertEquals(calendarExpected.get(Calendar.MONTH),
				calendarActual.get(Calendar.MONTH));
		assertEquals(calendarExpected.get(Calendar.YEAR),
				calendarActual.get(Calendar.YEAR));
		assertEquals(calendarExpected.get(Calendar.HOUR_OF_DAY),
				calendarActual.get(Calendar.HOUR_OF_DAY));
		assertEquals(calendarExpected.get(Calendar.MINUTE),
				calendarActual.get(Calendar.MINUTE));

		/*
		 * To account for the time between executing the first two statements
		 * (that get the expected and actual calendars), we allow a max
		 * difference of 1 second
		 */
		final int secActual = calendarActual.get(Calendar.SECOND);
		final int secExpected = calendarExpected.get(Calendar.SECOND);
		if (secActual != secExpected && secActual - secExpected > 1) {
			Assert.fail();
		}
	}

	@Test
	public void testConstructDateRange() throws ParseException {
		final DateUtils dateUtils = new DateUtils();
		assertNotNull(dateUtils);
		final Date[] dates = DateUtils.constructDateRange(new Date(),
				new Date());
		assertTrue(dates[0].before(dates[1]));
		final Date[] date1 = DateUtils.constructDateRange("01/01/2010",
				"01/01/2010");
		assertTrue(date1[0].before(date1[1]));
	}

	@Test
	public void testGetAllMonths() {
		final Map<Integer, String> months = DateUtils.getAllMonths();
		assertEquals(12, months.size());
	}

	@Test
	public void testGetAllMonthsWithFullNames() {
		final Map<Integer, String> months = DateUtils
				.getAllMonthsWithFullNames();
		assertEquals(12, months.size());
	}

	@Test
	public void testConvertToWords() throws ParseException {
		final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		final Date dt = formatter.parse("01-01-2010");
		assertEquals("First January Two Thousand Ten",
				DateUtils.convertToWords(dt));
	}
}
