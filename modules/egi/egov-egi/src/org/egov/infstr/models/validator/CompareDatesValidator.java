/*
 * @(#)CompareDatesValidator.java 3.0, 17 Jun, 2013 2:43:48 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.models.validator;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.egov.exceptions.EGOVRuntimeException;

public class CompareDatesValidator implements ConstraintValidator<CompareDates, Date> {

	private CompareDates compareDates;

	@Override
	public void initialize(final CompareDates compareDates) {
		this.compareDates = compareDates;
	}

	@Override
	public boolean isValid(final Date date, final ConstraintValidatorContext arg1) {
		if (this.compareDates.fields1() == null || this.compareDates.fields2() == null || this.compareDates.dateFormat() == null) {
			return false;
		}

		return this.dateValidation(date, this.compareDates.fields1(), this.compareDates.fields2());
	}

	private boolean dateValidation(final Date date, final String field1, final String field2) {
		final Date fromDate = this.getValue(date, field1);
		final Date toDate = this.getValue(date, field2);
		if (fromDate == null || toDate == null) {
			return false;
		}

		return fromDate.before(toDate);
	}

	private Date getValue(final Date target, final String field) {
		try {
			final BeanInfo info = java.beans.Introspector.getBeanInfo(target.getClass());
			final PropertyDescriptor[] props = info.getPropertyDescriptors();
			for (final PropertyDescriptor propertyDescriptor : props) {
				if (propertyDescriptor.getName().equals(field)) {
					return (Date) propertyDescriptor.getReadMethod().invoke(target);
				}
			}
			return null;
		} catch (final Exception e) {
			throw new EGOVRuntimeException(e.getMessage(), e);
		}
	}
}
