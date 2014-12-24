/*
 * @(#)ParameterCheck.java 3.0, 14 Jun, 2013 3:08:56 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterCheck {
	/**
	 * the position of the parameter to be checked.
	 */
	int position() default 1;

	boolean isCollection() default false;

}
