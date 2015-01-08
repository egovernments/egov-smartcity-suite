/*
 * @(#)CompareDates.java 3.0, 17 Jun, 2013 2:43:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.models.validator;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ TYPE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CompareDatesValidator.class)
public @interface CompareDates {
	String fields1();

	String fields2();

	String dateFormat();

	String message() default "{validator.compareDates}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}