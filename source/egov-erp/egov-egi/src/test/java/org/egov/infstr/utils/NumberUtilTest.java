/**
 * 
 */
package org.egov.infstr.utils;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.egov.infstr.utils.NumberUtil.NumberFormatStyle;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for number utilities
 */
public class NumberUtilTest {

	@Before
	public void setUp() {
	}

	@Test
	public void testAmountInWords() {
		assertEquals("Rupees One Hundred  Only ",
				NumberUtil.amountInWords(BigDecimal.valueOf(100)));
		assertEquals("Rupees One Hundred  and Fifty Paise Only",
				NumberUtil.amountInWords(BigDecimal.valueOf(100.5)));
		assertEquals("Rupees One Hundred  and Fifty Paise Only",
				NumberUtil.amountInWords(BigDecimal.valueOf(100.50)));
		assertEquals("Rupees One Hundred  and Fifty Six Paise Only",
				NumberUtil.amountInWords(BigDecimal.valueOf(100.555)));

		assertEquals(
				"Rupees Ten One Thousand  One Hundred Eleven Crores Eleven Lakhs Eleven Thousands One Hundred Eleven and Fifty Five Paise Only",
				NumberUtil.amountInWords(BigDecimal.valueOf(111111111111.55)));
	}

	@Test
	public void testFormatNumber() {
		assertEquals("0.00", NumberUtil.formatNumber(BigDecimal.valueOf(0)));
		assertEquals("-1.50", NumberUtil.formatNumber(BigDecimal.valueOf(-1.5)));
		assertEquals("1.23", NumberUtil.formatNumber(BigDecimal.valueOf(1.23)));
		assertEquals("12.34",
				NumberUtil.formatNumber(BigDecimal.valueOf(12.34)));
		assertEquals("123.45",
				NumberUtil.formatNumber(BigDecimal.valueOf(123.45)));
		assertEquals("1,234.50",
				NumberUtil.formatNumber(BigDecimal.valueOf(1234.5)));
		assertEquals("12,345.67",
				NumberUtil.formatNumber(BigDecimal.valueOf(12345.67)));
		assertEquals("1,23,456.78",
				NumberUtil.formatNumber(BigDecimal.valueOf(123456.78)));
		assertEquals("12,34,567.89",
				NumberUtil.formatNumber(BigDecimal.valueOf(1234567.89)));
		assertEquals("1,23,45,678.90",
				NumberUtil.formatNumber(BigDecimal.valueOf(12345678.9)));
		assertEquals("1,23,45,67,890.12",
				NumberUtil.formatNumber(BigDecimal.valueOf(1234567890.12)));
		assertEquals("12,34,56,78,901.23",
				NumberUtil.formatNumber(BigDecimal.valueOf(12345678901.23)));
		assertEquals("1,23,45,67,89,012.34",
				NumberUtil.formatNumber(BigDecimal.valueOf(123456789012.34)));
		assertEquals("80,00,00,000.00",
				NumberUtil.formatNumber(BigDecimal.valueOf(800000000)));
		assertEquals("79,00,502.00",
				NumberUtil.formatNumber(BigDecimal.valueOf(7900502)));

		assertEquals("-1.23",
				NumberUtil.formatNumber(BigDecimal.valueOf(-1.23)));
		assertEquals("-12.34",
				NumberUtil.formatNumber(BigDecimal.valueOf(-12.34)));
		assertEquals("-123.45",
				NumberUtil.formatNumber(BigDecimal.valueOf(-123.45)));
		assertEquals("-1,234.50",
				NumberUtil.formatNumber(BigDecimal.valueOf(-1234.5)));
		assertEquals("-12,345.67",
				NumberUtil.formatNumber(BigDecimal.valueOf(-12345.67)));
		assertEquals("-1,23,456.78",
				NumberUtil.formatNumber(BigDecimal.valueOf(-123456.78)));
		assertEquals("-12,34,567.89",
				NumberUtil.formatNumber(BigDecimal.valueOf(-1234567.89)));
		assertEquals("-1,23,45,678.90",
				NumberUtil.formatNumber(BigDecimal.valueOf(-12345678.9)));
		assertEquals("-1,23,45,67,890.12",
				NumberUtil.formatNumber(BigDecimal.valueOf(-1234567890.12)));
		assertEquals("-12,34,56,78,901.23",
				NumberUtil.formatNumber(BigDecimal.valueOf(-12345678901.23)));
		assertEquals("-1,23,45,67,89,012.34",
				NumberUtil.formatNumber(BigDecimal.valueOf(-123456789012.34)));
		assertEquals("-80,00,000.00",
				NumberUtil.formatNumber(BigDecimal.valueOf(-8000000)));
		assertEquals("-79,00,502.00",
				NumberUtil.formatNumber(BigDecimal.valueOf(-7900502)));

		assertEquals("0.00", NumberUtil.formatNumber(BigDecimal.valueOf(0),
				NumberFormatStyle.MILLIONS));
		assertEquals("-1.50", NumberUtil.formatNumber(BigDecimal.valueOf(-1.5),
				NumberFormatStyle.MILLIONS));
		assertEquals("1.23", NumberUtil.formatNumber(BigDecimal.valueOf(1.23),
				NumberFormatStyle.MILLIONS));
		assertEquals("12.34", NumberUtil.formatNumber(
				BigDecimal.valueOf(12.34), NumberFormatStyle.MILLIONS));
		assertEquals("123.45", NumberUtil.formatNumber(
				BigDecimal.valueOf(123.45), NumberFormatStyle.MILLIONS));
		assertEquals("1,234.50", NumberUtil.formatNumber(
				BigDecimal.valueOf(1234.5), NumberFormatStyle.MILLIONS));
		assertEquals("12,345.67", NumberUtil.formatNumber(
				BigDecimal.valueOf(12345.67), NumberFormatStyle.MILLIONS));
		assertEquals("123,456.78", NumberUtil.formatNumber(
				BigDecimal.valueOf(123456.78), NumberFormatStyle.MILLIONS));
		assertEquals("1,234,567.89", NumberUtil.formatNumber(
				BigDecimal.valueOf(1234567.89), NumberFormatStyle.MILLIONS));
		assertEquals("12,345,678.90", NumberUtil.formatNumber(
				BigDecimal.valueOf(12345678.9), NumberFormatStyle.MILLIONS));
		assertEquals("1,234,567,890.12", NumberUtil.formatNumber(
				BigDecimal.valueOf(1234567890.12), NumberFormatStyle.MILLIONS));
		assertEquals("12,345,678,901.23", NumberUtil.formatNumber(
				BigDecimal.valueOf(12345678901.23), NumberFormatStyle.MILLIONS));
		assertEquals("123,456,789,012.34",
				NumberUtil.formatNumber(BigDecimal.valueOf(123456789012.34),
						NumberFormatStyle.MILLIONS));

		assertEquals("-1.23", NumberUtil.formatNumber(
				BigDecimal.valueOf(-1.23), NumberFormatStyle.MILLIONS));
		assertEquals("-12.34", NumberUtil.formatNumber(
				BigDecimal.valueOf(-12.34), NumberFormatStyle.MILLIONS));
		assertEquals("-123.45", NumberUtil.formatNumber(
				BigDecimal.valueOf(-123.45), NumberFormatStyle.MILLIONS));
		assertEquals("-1,234.50", NumberUtil.formatNumber(
				BigDecimal.valueOf(-1234.5), NumberFormatStyle.MILLIONS));
		assertEquals("-12,345.67", NumberUtil.formatNumber(
				BigDecimal.valueOf(-12345.67), NumberFormatStyle.MILLIONS));
		assertEquals("-123,456.78", NumberUtil.formatNumber(
				BigDecimal.valueOf(-123456.78), NumberFormatStyle.MILLIONS));
		assertEquals("-1,234,567.89", NumberUtil.formatNumber(
				BigDecimal.valueOf(-1234567.89), NumberFormatStyle.MILLIONS));
		assertEquals("-12,345,678.90", NumberUtil.formatNumber(
				BigDecimal.valueOf(-12345678.9), NumberFormatStyle.MILLIONS));
		assertEquals("-1,234,567,890.12", NumberUtil.formatNumber(
				BigDecimal.valueOf(-1234567890.12), NumberFormatStyle.MILLIONS));
		assertEquals("-12,345,678,901.23",
				NumberUtil.formatNumber(BigDecimal.valueOf(-12345678901.23),
						NumberFormatStyle.MILLIONS));
		assertEquals("-123,456,789,012.34", NumberUtil.formatNumber(
				BigDecimal.valueOf(-123456789012.34),
				NumberFormatStyle.MILLIONS));
	}
}
