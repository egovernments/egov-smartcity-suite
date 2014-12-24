/*
 * @(#)NumericValidator.java 3.0, 17 Jun, 2013 2:44:19 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.models.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.egov.infstr.models.validator.constants.ValidatorConstants;

public class NumericValidator implements ConstraintValidator<Numeric, Object> {

	@Override
	public void initialize(final Numeric numeric) {
		// Unused
	}

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext arg1) {

		if (value == null) {
			return true;
		} else {
			final String stringVal = String.valueOf(value);
			if (org.apache.commons.lang.StringUtils.isBlank(stringVal)) {
				return true;
			}

			return stringVal.trim().matches(ValidatorConstants.numeric);
		}

	}

}
