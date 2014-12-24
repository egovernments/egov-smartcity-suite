/*
 * @(#)Unique.java 3.0, 17 Jun, 2013 2:44:37 PM
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
@Constraint(validatedBy = IsUniqueValidator.class)
public @interface Unique {
	String[] fields() default {};

	String id();

	String tableName();

	String[] columnName() default {};

	String message() default "{validator.unique}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}