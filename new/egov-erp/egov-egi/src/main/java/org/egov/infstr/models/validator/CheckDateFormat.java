/*
 * @(#)CheckDateFormat.java 3.0, 17 Jun, 2013 2:43:41 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.models.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = DateFormatValidator.class)
public @interface CheckDateFormat {
	String message() default "{validator.dateFormat}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
