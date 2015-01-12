/*
 * @(#)DateValidator.java 3.0, 17 Jun, 2013 2:43:57 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.models.validator;

import java.util.Calendar;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateValidator implements ConstraintValidator<ValidateDate, Date> {

	private ValidateDate validateDate;

	@Override
	public void initialize(final ValidateDate validateDate) {
		this.validateDate = validateDate;
	}

	@Override
	public boolean isValid(final Date value, final ConstraintValidatorContext arg1) {
		if (value == null) {
			return true;
		}
		return this.dateValidation(value);
	}

	private boolean dateValidation(final Date date) {
		if (this.validateDate.dateFormat() == null) {
			return false;
		}
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		if (this.validateDate.allowPast()) {
			return date.before(cal.getTime());
		} else {
			return cal.getTime().equals(date);
		}
	}

}
