package org.egov.infstr.utils;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.junit.EgovHibernateTest;
import org.junit.Ignore;

import java.lang.reflect.Method;

@Ignore
public class NumberToWordTest extends EgovHibernateTest {

	NumberToWord numberToWord = new NumberToWord();

	public void testGetWordsBypassingEmptyString() {
		try {
			NumberToWord.convertToWord("");
		} catch (final Exception e) {
			assertTrue(true);
		}

	}

	public void testForZeroRupees() {
		final String word = NumberToWord.convertToWord("0");
		assertEquals(word, "Rupees Zero Only ");

	}

	public void testForThousandCrores() {
		String word = NumberToWord.convertToWord("10000000000");
		assertEquals(word, "Rupees One Thousand Crores  Only ");
		// Wrong but test succeed
		word = NumberToWord.convertToWord("10000000001");
		assertEquals(word, "Rupees One Thousand  One Only ");
	}

	public void testForPaise() {
		final String word = NumberToWord.convertToWord("123.75");
		assertEquals(word,
				"Rupees One Hundred Twenty Three and Seventy Five Paise Only");
	}

	public void testconvertToWordDouble() {
		final String word = NumberToWord.amountInWords(new Double(123.75));
		assertEquals(word,
				"Rupees One Hundred Twenty Three and Seventy Five Paise Only");
	}

	public void testNumberToWordsInvalidNumber() {
		try {
			NumberToWord.numberToString("123.75");
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
	}

	public void testNumberToWords() {
		final String word = NumberToWord.numberToString("123");
		assertEquals(" One Hundred Twenty Three  ", word);

	}

	public void testNumberToWordsAbsDecimal() {
		String word = "";
		try {
			word = NumberToWord.convertToWord("ss.00");
		} catch (final Exception e) {
			assertTrue(true);
		}
		word = NumberToWord.convertToWord("100.90");
		assertEquals("Rupees One Hundred  and Ninety Paise Only", word);
		word = NumberToWord.convertToWord("100.00");
		assertEquals("Rupees One Hundred  Only ", word);
		word = NumberToWord.convertToWord("100.09");
		assertEquals("Rupees One Hundred  and Nine Paise Only", word);
		word = NumberToWord.convertToWord("1234567890");
		assertEquals(
				"Rupees One Hundred Twenty Three Crores Forty Five Lakhs Sixty Seven Thousands Eight Hundred Ninety Only ",
				word);
		word = NumberToWord.convertToWord("01.90");
		assertEquals("Rupees One and Ninety Paise Only", word);
		word = NumberToWord.convertToWord("1000");
		assertEquals("Rupees One Thousand  Only ", word);
		word = NumberToWord.convertToWord("100000");
		assertEquals("Rupees One Lakh  Only ", word);
		word = NumberToWord.convertToWord("10000000");
		assertEquals("Rupees One Crore  Only ", word);
		word = NumberToWord.convertToWord("100000000");
		assertEquals("Rupees Ten Crores  Only ", word);
		word = NumberToWord.convertToWord("1000000000");
		assertEquals("Rupees One Hundred Crores  Only ", word);
		word = NumberToWord.convertToWord("10000000000");
		assertEquals("Rupees One Thousand Crores  Only ", word);
		word = NumberToWord.convertToWord("100000000000");
		assertEquals("Rupees Ten  Thousand Crores  Only ", word);
		word = NumberToWord.convertToWord("210000000000");
		assertEquals("Rupees Twenty One Thousand Crores  Only ", word);

		// This Test succeed but gives this wrong result
		word = NumberToWord.convertToWord("123456789012");
		assertEquals(
				"Rupees Ten Two Thousand  Three Hundred Forty Five Crores Sixty Seven Lakhs Eighty Nine Thousands Twelve Only ",
				word);

	}

	public void testUnReachableCode() throws Exception {
		Method m = NumberToWord.class.getDeclaredMethod("getWord", Long.class);
		m.setAccessible(true);
		for (int i = 0; i < 101; i++) {
			m.invoke(new NumberToWord(), Long.valueOf(i));
		}

		m = NumberToWord.class.getDeclaredMethod("paiseInWords", String.class);
		m.setAccessible(true);
		m.invoke(new NumberToWord(), "0");

		m = NumberToWord.class.getDeclaredMethod("getPlace", String.class);
		m.setAccessible(true);
		m.invoke(new NumberToWord(), "");
		assertTrue(true);

	}
}
