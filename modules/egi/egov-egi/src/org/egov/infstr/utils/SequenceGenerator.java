/*
 * @(#)SequenceGenerator.java 3.0, 18 Jun, 2013 12:12:25 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

import org.apache.commons.lang.StringUtils;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.NoSuchObjectTypeException;
import org.egov.infstr.services.SessionFactory;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * The class should be injected as a dependency through Spring to avoid future incompatibilities. 
 * Use the bean id "sequenceGenerator". Infra's applicationContext.xml defines this bean as "sequenceGenerator" 
 * Do not cache the instances across HTTP sessions. The instances are not serializable either.
 * <p>
 * <b>IMPORTANT: </b>SequenceGenerator does not do pessimistic locking as the application becomes 
 * a serial single threaded app which affects application stability. If two transactions simultaneously 
 * call the nextNumber APIs and ask for a number they will get a same number 23. If transaction 2
 * commits the number will be consumed by Transacton 2. If Transcation 1 now tries to commit, 
 * the transaction should be rolled back(by the calling Application, This would be responsibility of the calling Application)
 */
public class SequenceGenerator {

	private Session session;
	private SessionFactory sessionFactory;
	private static ThreadLocal<Number> threadLocal = new ThreadLocal<Number>();

	public SequenceGenerator() {
		this.session = HibernateUtil.getCurrentSession();
	}

	public SequenceGenerator(final SessionFactory factory) {
		this.sessionFactory = factory;
	}

	/**
	 * Return the next sequence for the given object type
	 * @param objectType - The type for which you need the next sequence
	 * @return Sequence
	 */
	public Sequence getNextNumber(final String objectType) {

		final Number number = (Number) getSession().createCriteria(Number.class).add(Restrictions.eq("objectType", objectType.toUpperCase())).uniqueResult();
		if (number != null) {
			number.setNumber(number.getNumber() + 1);
			getSession().update(number);
			threadLocal.set(number);
			number.setFormattedNumber(Long.toString(number.getNumber()));
			return new Sequence(number.getObjectType(), number.getNumber(), number.getFormattedNumber());
		}
		throw new NoSuchObjectTypeException("No row with the given objectType " + objectType + " Found ");

	}

	/**
	 * Return the next sequence for the given object type
	 * @param objectType - The type for which you need the next sequence
	 * @return Sequence flag = true prefix the value to the generated number or false to vice versa valueToAppend = specify the elements to append for the generated number
	 */
	public Sequence getNextNumberWithFormat(final String objectType, final boolean flag, final String valueToAppend) {
		final Sequence seq = getNextNumber(objectType);
		if (seq != null) {
			String formattedNumber = seq.getFormattedNumber();
			if (flag) {
				formattedNumber = valueToAppend + formattedNumber;
			} else {
				formattedNumber = formattedNumber + valueToAppend;
			}
			return new Sequence(seq.getObjectType(), seq.getNumber(), formattedNumber);
		}
		throw new EGOVRuntimeException("No row with the given objectType " + objectType + " Found ");

	}

	/**
	 * Return the next sequence for the given object type
	 * @param objectType - The type for which you need the next sequence
	 * @return Sequence StrLength= Specify the length of the String valueToAppend = specify the element to append for the generated number
	 */
	public Sequence getNextNumberWithFormat(final String objectType, final int strLength, final Character valueToAppend) {
		final Sequence seq = getNextNumber(objectType);
		if (seq != null) {
			final String formattedNumber = StringUtils.leftPad(seq.getFormattedNumber(), strLength, valueToAppend.charValue());
			return new Sequence(seq.getObjectType(), seq.getNumber(), formattedNumber);
		}
		throw new EGOVRuntimeException("No row with the given objectType " + objectType + " Found ");

	}

	public Sequence getNextNumberWithFormat(final String objectType, final int strLength, final String valueToAppend) {
		final Sequence seq = getNextNumber(objectType);
		if (seq != null) {
			final String formattedNumber = StringUtils.leftPad(seq.getFormattedNumber(), strLength, valueToAppend);
			return new Sequence(seq.getObjectType(), seq.getNumber(), formattedNumber);
		}
		throw new EGOVRuntimeException("No row with the given objectType " + objectType + " Found ");

	}

	/**
	 * This method generates a sequence number of given length, padded with given value, for the given string value . If the string value does not exist, it is created with the given start value.
	 * @param objectType a <code>String</code> for which the sequence is generated.
	 * @param strLength the required length of the generated sequence number
	 * @param valueToAppend the value which should be appended to the generated sequence number
	 * @param startValue the start value, from the sequence generation should begin, if the string object does not exist.
	 * @return a <code>Sequence</code> instance containing the generated sequence number
	 */
	public Sequence getNextNumberWithFormat(final String objectType, final int strLength, final Character valueToAppend, final long startValue) {
		final Sequence seq = getNextNumber(objectType, startValue);
		if (seq != null) {
			final String formattedNumber = StringUtils.leftPad(seq.getFormattedNumber(), strLength, valueToAppend.charValue());
			return new Sequence(seq.getObjectType(), seq.getNumber(), formattedNumber);
		}
		throw new EGOVRuntimeException("No row with the given objectType " + objectType + " Found ");

	}

	/**
	 * Reset the sequence for the given type to the given number
	 * @param objectType
	 * @param numberToReset
	 */
	public void resetNumber(final String objectType, final long numberToReset) {

		final Number number = (Number) getSession().createCriteria(Number.class).add(Restrictions.eq("objectType", objectType.toUpperCase())).uniqueResult();
		if (number != null) {
			number.setNumber(numberToReset);
			getSession().update(number);
		} else {
			throw new NoSuchObjectTypeException("No row with the given objectType " + objectType + " Found ");
		}
	}

	/**
	 * Discard the number generated in the current transaction for the given type and revert back to the old number WARNING: This will throw an EGovRuntimeException if there was no previous call to getNextNumber within the same thread
	 * @param objectType
	 * @return
	 */
	public long discardNumber(final String objectType) {
		final Number current = threadLocal.get();
		if (current == null) {
			throw new EGOVRuntimeException("discardNumber() can not be called without getNextNumber()");
		}
		current.setNumber(current.getNumber() - 1);
		getSession().update(current);
		return current.getNumber();
	}

	// to insertNumber into DB with ObjectType and Value
	public void insertObjectType(final String objectType, final long value) {
		final Number number = new Number();
		try {
			if (objectType == null) {
				throw new EGOVRuntimeException("the requested objectType is null");
			}
			number.setNumber(value);
			number.setObjectType(objectType);
			getSession().save(number);
		} catch (final Exception e) {
			throw new EGOVRuntimeException("the requested objectType" + objectType + "not saved to DB");
		}
	}

	/**
	 * This checks if the given object type exists in Number
	 * @param objectType to check
	 * @return true if objectType exists otherwise false
	 */
	public boolean checkObjectType(final String objectType) {
		final Number number = (Number) getSession().createCriteria(Number.class).add(Restrictions.eq("objectType", objectType.toUpperCase())).uniqueResult();
		if (number == null) {
			return false;
		} else {
			return true;
		}
	}

	protected Session getSession() {
		if (this.sessionFactory != null) {
			return this.sessionFactory.getSession();
		}
		return this.session;

	}

	public Sequence getNextNumber(final String objectType, final long startValue) {
		try {
			return getNextNumber(objectType);
		} catch (final NoSuchObjectTypeException e) {
			final Number number = new Number();
			number.setNumber(startValue < 1 ? 1 : startValue);
			threadLocal.set(number);
			number.setFormattedNumber(Long.toString(number.getNumber()));
			number.setObjectType(objectType);
			getSession().save(number);
			return new Sequence(number.getObjectType(), number.getNumber(), number.getFormattedNumber());
		}
	}

}
