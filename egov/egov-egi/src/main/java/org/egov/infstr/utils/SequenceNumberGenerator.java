/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infstr.utils;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.exception.NoSuchObjectTypeException;
import org.egov.infstr.utils.seqgen.DatabaseSequence;
import org.egov.infstr.utils.seqgen.DatabaseSequenceException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fetches a sequence number from a named sequence. THIS CLASS SUPERSEDES THE OLD org.egov.infstr.utils.SequenceGenerator.
 * The class should be injected as a dependency through Spring to avoid future incompatibilities.
 * Use the bean id "sequenceNumberGenerator". Infra's "globalApplicationContext.xml"
 * defines this bean as "sequenceNumberGenerator". Since the implementation is based on database sequences,
 * a number once fetched is effectively "used" and will not be available again.
 * This means that there will almost definitely be gaps in the numbers fetched, since if a transaction fails,
 * the number that it fetched will be lost for good.
 */
@Service
@Transactional(readOnly = true)
public class SequenceNumberGenerator {

    private SessionFactory sessionFactory;

    public SequenceNumberGenerator(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

	/**
	 * Return the next sequence for the given object type
	 * @param objectType - The type for which you need the next sequence
	 * @return Sequence
	 */
	public Sequence getNextNumber(final String objectType) {
		try {
			final String sequenceName = sequenceNameFromObjectType(objectType);
			final long nextVal = DatabaseSequence.named(sequenceName, getSession()).nextVal();
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
		throw new ApplicationRuntimeException("No row with the given objectType " + objectType + " Found ");

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
		throw new ApplicationRuntimeException("No row with the given objectType " + objectType + " Found ");

	}

	public Sequence getNextNumberWithFormat(final String objectType, final int strLength, final String valueToAppend) {
		final Sequence seq = getNextNumber(objectType);
		if (seq != null) {
			final String formattedNumber = StringUtils.leftPad(seq.getFormattedNumber(), strLength, valueToAppend);
			return new Sequence(seq.getObjectType(), seq.getNumber(), formattedNumber);
		}
		throw new ApplicationRuntimeException("No row with the given objectType " + objectType + " Found ");

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
		throw new ApplicationRuntimeException("No row with the given objectType " + objectType + " Found ");
	}

	/**
	 * Fetches the next number, creating the sequence with the given startValue if it doesn't yet exist. The startValue should NOT be 0 - it should be minimum 1. If you put 0, it will try to do 'CREATE SEQUENCE xyz START WITH 0' - which will fail.
	 * @param objectType
	 * @param startValue
	 * @return
	 */
	public Sequence getNextNumber(final String objectType, final long startValue) {
		final String sequenceName = sequenceNameFromObjectType(objectType);
		final long nextVal = DatabaseSequence.named(sequenceName, getSession()).createIfNecessary().startingWith(startValue).nextVal();
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
		return sessionFactory.getCurrentSession();

	}

	private String sequenceNameFromObjectType(final String objectType) {
		return DatabaseSequence.SEQUENCE_NAME_PREFIX + DatabaseSequence.replaceBadChars(objectType);
	}
}
