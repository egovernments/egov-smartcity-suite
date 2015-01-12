/*
 * @(#)SequenceNumberGenerator.java 3.0, 18 Jun, 2013 12:13:17 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

import org.apache.commons.lang.StringUtils;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.NoSuchObjectTypeException;
import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.seqgen.DatabaseSequence;
import org.egov.infstr.utils.seqgen.DatabaseSequenceException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * Fetches a sequence number from a named sequence. THIS CLASS SUPERSEDES THE OLD org.egov.infstr.utils.SequenceGenerator. 
 * The class should be injected as a dependency through Spring to avoid future incompatibilities. 
 * Use the bean id "sequenceNumberGenerator". Infra's "globalApplicationContext.xml"
 * defines this bean as "sequenceNumberGenerator". Since the implementation is based on database sequences, 
 * a number once fetched is effectively "used" and will not be available again. 
 * This means that there will almost definitely be gaps in the numbers fetched, since if a transaction fails, 
 * the number that it fetched will be lost for good.
 */
public class SequenceNumberGenerator {

	private Session session;
	private SessionFactory sessionFactory;

	public SequenceNumberGenerator() {
		this.session = HibernateUtil.getCurrentSession();
	}

	public SequenceNumberGenerator(final SessionFactory factory) {
		this.sessionFactory = factory;
	}

	/**
	 * Return the next sequence for the given object type
	 * @param objectType - The type for which you need the next sequence
	 * @return Sequence
	 */
	public Sequence getNextNumber(final String objectType) {
		try {
			final String sequenceName = sequenceNameFromObjectType(objectType);
			final long nextVal = DatabaseSequence.named(sequenceName).nextVal();
			return new Sequence(objectType, nextVal, Long.toString(nextVal));
		} catch (final DatabaseSequenceException dse) {
			// throwing this to preserve backward compatibility; existing clients might be using
			// this exception
			throw new NoSuchObjectTypeException("Could not find/call sequence for the given objectType " + objectType, dse);
		}
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
	 * Fetches the next number, creating the sequence with the given startValue if it doesn't yet exist. The startValue should NOT be 0 - it should be minimum 1. If you put 0, it will try to do 'CREATE SEQUENCE xyz START WITH 0' - which will fail.
	 * @param objectType
	 * @param startValue
	 * @return
	 */
	public Sequence getNextNumber(final String objectType, final long startValue) {
		final String sequenceName = sequenceNameFromObjectType(objectType);
		final long nextVal = DatabaseSequence.named(sequenceName).createIfNecessary().startingWith(startValue).nextVal();
		return new Sequence(objectType, nextVal, Long.toString(nextVal));
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

	protected Session getSession() {
		if (this.sessionFactory != null) {
			return this.sessionFactory.getSession();
		}
		return this.session;

	}

	private String sequenceNameFromObjectType(final String objectType) {
		return DatabaseSequence.SEQUENCE_NAME_PREFIX + DatabaseSequence.replaceBadChars(objectType);
	}
}
