package org.egov.infstr.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.NoSuchObjectTypeException;
import org.egov.infstr.junit.EgovHibernateTest;
import org.hibernate.criterion.Restrictions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SequenceGeneratorTest {

	private static final String DEFAULT_TYPE = "ABSTRACTESTIMATE";
	private EgovHibernateTest hibernateSupport;
	private SequenceGenerator numberUtil;

	@Before
	public void setUp() throws Exception {
		this.hibernateSupport = new EgovHibernateTest();
		this.hibernateSupport.setUp();
		this.numberUtil = new SequenceGenerator();
	}

	@After
	public void tearDown() throws Exception {
		this.hibernateSupport.tearDown();
	}

	@Test(expected = NoSuchObjectTypeException.class)
	public void noRowInDataBase() {
		final String objectType = "property";
		this.numberUtil.getNextNumber(objectType);
	}

	@Test
	public void generateNextNumber() {
		final Sequence generatedNumber = this.numberUtil
				.getNextNumber(DEFAULT_TYPE);
		final Number number = (Number) HibernateUtil.getCurrentSession()
				.createCriteria(Number.class)
				.add(Restrictions.eq("objectType", DEFAULT_TYPE.toUpperCase()))
				.uniqueResult();
		assertSame(number.getNumber(), generatedNumber.getNumber().longValue());

	}

	@Test
	public void resetNumber() {
		final long numberToReset = 0;
		this.numberUtil.resetNumber(DEFAULT_TYPE, numberToReset);
		final Number number = (Number) HibernateUtil.getCurrentSession()
				.createCriteria(Number.class)
				.add(Restrictions.eq("objectType", DEFAULT_TYPE.toUpperCase()))
				.uniqueResult();
		assertSame(number.getNumber(), numberToReset);
	}

	@Test(expected = NoSuchObjectTypeException.class)
	public void noRowInDataBaseForResetNumber() {
		final String objectType = "property";
		final long numberToReset = 0;
		this.numberUtil.resetNumber(objectType, numberToReset);
	}

	@Test
	public void discardNumberAfterGetNextNumber() {
		final String objectType = DEFAULT_TYPE;

		final Sequence genNumber = this.numberUtil.getNextNumber(objectType);
		final long discardedNumber = this.numberUtil.discardNumber(objectType);
		assertSame(discardedNumber, genNumber.getNumber() - 1);
	}

	@Test
	public void insertNumberSuccess() {
		this.numberUtil.insertObjectType("P1", new Long(1));
		assertEquals("2", this.numberUtil.getNextNumber("P1").getNumber()
				.toString());
	}

	@Test(expected = EGOVRuntimeException.class)
	public void insertNumberObjTypeNull() {
		this.numberUtil.insertObjectType(null, new Long(1));

	}

	@Test
	public void generateNextNumberWithFormat() {
		final int StrLength = 4;
		final Character valueToAppend = '0';
		final Sequence formattedNumber = this.numberUtil
				.getNextNumberWithFormat(DEFAULT_TYPE, StrLength,
						valueToAppend.charValue());
		assertTrue(formattedNumber.getFormattedNumber().length() == 4);
	}

	@Test
	public void generateNextNumberWithPadding() {
		final boolean flag = true;
		final String valueToAppend = "ABC";
		final Sequence formattedNumber = this.numberUtil.getNextNumberWithFormat(DEFAULT_TYPE, flag, valueToAppend);
		assertTrue(formattedNumber.getFormattedNumber().length() == 4);
	}

	@Test
	public void testCheckObjectTypeTrue() {
		final boolean flag = true;
		assertEquals(flag, this.numberUtil.checkObjectType(DEFAULT_TYPE));

	}

	public void testCheckObjectTypeFalse() {
		final String objectType = "XXXXXX";
		final boolean flag = false;
		assertEquals(flag, this.numberUtil.checkObjectType(objectType));
	}

}
