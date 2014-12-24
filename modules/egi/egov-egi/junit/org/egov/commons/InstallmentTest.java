package org.egov.commons;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;

import org.egov.infstr.junit.EgovHibernateTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InstallmentTest {
	private final EgovHibernateTest egovHibernateTest = new EgovHibernateTest();
	private java.text.SimpleDateFormat sdf;

	@Before
	public void setUp() throws Exception {
		this.sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
		this.egovHibernateTest.setUp();
	}

	@After
	public void tearDown() throws Exception {
		this.sdf = null;
	}

	@Test
	public void CompareToWithInputNull() {
		final Installment inst1 = new Installment();
		final int value = inst1.compareTo(null);
		assertEquals(1, value);
	}

	@Test
	public void CompareToWithDifferentInstallments() throws ParseException {
		int value;
		final Installment inst2000 = new Installment();
		final Installment inst1999 = new Installment();
		final Installment inst2007 = new Installment();
		final Installment inst2007same = new Installment();
		inst2000.setFromDate(this.sdf.parse("23/10/2000"));
		inst1999.setFromDate(this.sdf.parse("12/01/1999"));
		inst2007.setFromDate(this.sdf.parse("29/12/2007"));
		inst2007same.setFromDate(this.sdf.parse("29/12/2007"));

		value = inst1999.compareTo(inst2000);
		assertEquals(-1, value);

		value = inst2007.compareTo(inst1999);
		assertEquals(1, value);

		value = inst2007same.compareTo(inst2007);
		assertEquals(0, value);
	}
}
