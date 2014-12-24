/*
 * @(#)GreaterThanValidator.java 3.0, 17 Jun, 2013 2:44:04 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.models.validator;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GreaterThanValidator implements ConstraintValidator<GreaterThan, Object> {

	private GreaterThan greaterThan;

	@Override
	public void initialize(final GreaterThan greaterThan) {
		this.greaterThan = greaterThan;
	}

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext arg1) {
		if (value == null) {
			return true;
		}
		if (value instanceof String) {
			final BigDecimal dv = new BigDecimal((String) value);
			return dv.compareTo(BigDecimal.valueOf(this.greaterThan.value())) > 0;
		} else if (value instanceof Double || value instanceof Float) {
			final double dv = ((Number) value).doubleValue();
			return dv > this.greaterThan.value();
		} else if (value instanceof Number) {
			final long lv = ((Number) value).longValue();
			return lv > this.greaterThan.value();
		} else {
			return false;
		}
	}
}
