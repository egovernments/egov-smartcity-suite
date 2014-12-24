package org.egov.infstr.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.egov.exceptions.EGOVRuntimeException;
import org.junit.Before;
import org.junit.Test;

public class EgovUtilsTest {

	private EgovUtils egovUtils;

	@Before
	public void setup() {
		this.egovUtils = new EgovUtils();
	}

	@Test
	public void egovUtilTest() {
		String actual = this.egovUtils.toString();
		assertNotNull(actual);
		actual = this.egovUtils.toString(this.egovUtils);
		assertNotNull(actual);
		final BigDecimal firstamount = new BigDecimal(1);
		final BigDecimal secondamount = new BigDecimal(1);

		BigDecimal amount = EgovUtils.add(firstamount, secondamount);
		assertEquals("2", amount.toPlainString());

		amount = EgovUtils.divide(firstamount, secondamount);
		assertEquals("1", amount.toPlainString());

		amount = EgovUtils.multiply(firstamount, secondamount);
		assertEquals("1", amount.toPlainString());

		amount = EgovUtils.subtract(firstamount, secondamount);
		assertEquals("0", amount.toPlainString());

		amount = EgovUtils.roundOff(new BigDecimal("1.23"));
		assertEquals("1", amount.toPlainString());

		amount = EgovUtils.roundOffTwo(new BigDecimal("1.236"));
		assertEquals("1.24", amount.toPlainString());

		assertTrue(EgovUtils.hasNumber("ew3"));
		assertFalse(EgovUtils.hasNumber("ew"));

		assertTrue(EgovUtils.isInteger("999"));
		assertFalse(EgovUtils.isInteger("www"));

		actual = EgovUtils
				.getDomainName("http://www.ramanagaracity.gov.in/egbnd");
		assertEquals("ramanagaracity", actual);

		actual = EgovUtils
				.getDomainName("https://www.ramanagaracity:8180.gov.in/egbnd");
		assertEquals("ramanagaracity", actual);

		actual = EgovUtils.getDomainName("https://ramanagaracity.gov.in/egbnd");
		assertEquals("ramanagaracity", actual);

		actual = EgovUtils.getDomainName("https://ramanagaracity/egbnd");
		assertEquals("ramanagaracity", actual);

		actual = EgovUtils.getDomainName("http://192.168.0.1:8180/egi");
		assertEquals("ip192:168:0:1", actual);
		try {
			actual = EgovUtils.getDomainName(null);
		} catch (final Exception e) {
			assertTrue(e instanceof EGOVRuntimeException);
		}

		EgovUtils.rollBackTransaction();

		actual = EgovUtils
				.getCompleteDomainName("http://www.ramanagaracity.gov.in/egi");
		assertEquals("www.ramanagaracity.gov.in", actual);

		actual = EgovUtils.getCompleteDomainName("http://localhost:8180/egbnd");
		assertEquals("localhost", actual);

		actual = EgovUtils.getPrincipalName("prinName<:1>pwd");
		assertEquals("prinName", actual);
		actual = EgovUtils.getPrincipalName("prinName");
		assertEquals("prinName", actual);

		final ArrayList<String> list = new ArrayList<String>();
		list.add("A");

		assertEquals("A", EgovUtils.getFirstObject(list).toString());

	}

}
