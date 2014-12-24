/*
 * @(#)RequiredValidator.java 3.0, 17 Jun, 2013 2:44:33 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.models.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RequiredValidator implements ConstraintValidator<Required, Object> {

	@Override
	public void initialize(final Required parameters) {
		// Unused
	}

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext arg1) {
		if (value == null) {
			return false;
		} else if (value instanceof String) {
			return org.apache.commons.lang.StringUtils.isNotBlank((String) value);
		} else {
			return true;
		}
	}
}
