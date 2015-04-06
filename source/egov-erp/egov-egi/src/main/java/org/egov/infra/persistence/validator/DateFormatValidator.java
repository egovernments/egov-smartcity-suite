/*
 * @(#)DateFormatValidator.java 3.0, 17 Jun, 2013 2:43:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infra.persistence.validator;

import java.util.Date;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.egov.infra.persistence.validator.annotation.CheckDateFormat;
import org.egov.infra.validation.regex.Constants;
import org.egov.infstr.utils.DateUtils;

public class DateFormatValidator implements ConstraintValidator<CheckDateFormat, Date> {

	@Override
	public void initialize(final CheckDateFormat checkDateFormat) {
		// Unused

	}

	@Override
	public boolean isValid(final Date date, final ConstraintValidatorContext context) {
		return date == null ? true : Pattern.compile(Constants.DATEFORMAT).matcher(DateUtils.getFormattedDate(date, "dd/MM/yyyy")).matches();
	}

}
