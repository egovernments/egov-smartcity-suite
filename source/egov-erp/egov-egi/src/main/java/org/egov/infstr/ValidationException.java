/*
 * @(#)ValidationException.java 3.0, 17 Jun, 2013 3:09:12 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final List<ValidationError> errors;

	public ValidationException(final List<ValidationError> errors) {
		this.errors = errors;
	}

	public List<ValidationError> getErrors() {
		return this.errors;
	}

	public ValidationException(final String key, final String defaultMsg, final String... args) {
		this.errors = new ArrayList<ValidationError>();
		this.errors.add(new ValidationError(key, defaultMsg, args));
	}
}
