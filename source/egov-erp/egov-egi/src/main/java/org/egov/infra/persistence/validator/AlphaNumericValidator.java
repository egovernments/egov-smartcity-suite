/*
 * @(#)AlphaNumericValidator.java 3.0, 17 Jun, 2013 2:43:35 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infra.persistence.validator;

import static org.apache.commons.lang.StringUtils.isBlank;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.egov.infra.persistence.validator.annotation.AlphaNumeric;
import org.egov.infra.validation.regex.Constants;

public class AlphaNumericValidator implements ConstraintValidator<AlphaNumeric, String> {

	@Override
	public void initialize(final AlphaNumeric alphaNumeric) {
		// Unused
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext constraintValidatorCtxt) {
		return isBlank(value) ? true : value.trim().matches(Constants.ALPHANUMERIC_WITHSPACE);
	}
}
