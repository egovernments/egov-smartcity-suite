/*
 * @(#)OptionalPatternValidator.java 3.0, 17 Jun, 2013 2:44:26 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.models.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OptionalPatternValidator implements ConstraintValidator<OptionalPattern, Object> {

	private OptionalPattern optionalPattern;

	@Override
	public void initialize(final OptionalPattern optionalPattern) {
		this.optionalPattern = optionalPattern;
	}

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext arg1) {
		if (value == null || org.apache.commons.lang.StringUtils.isBlank(String.valueOf(value))) {
			return true;
		}
		return Pattern.compile(this.optionalPattern.regex(), this.optionalPattern.flags()).matcher(String.valueOf(value)).matches();
	}

}
