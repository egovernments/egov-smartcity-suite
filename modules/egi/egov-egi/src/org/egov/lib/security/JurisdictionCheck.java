/*
 * @(#)JurisdictionCheck.java 3.0, 14 Jun, 2013 3:05:55 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface JurisdictionCheck {
	
	boolean checkBefore() default true;
	
	boolean isCollection() default false;
	
}
