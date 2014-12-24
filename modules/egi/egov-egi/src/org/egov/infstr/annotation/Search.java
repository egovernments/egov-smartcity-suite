/*
 * @(#)Search.java 3.0, 6 Jun, 2013 3:15:27 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(METHOD)
@Retention(RUNTIME)
public @interface Search {
    enum Operator{equals, startsWith, endsWith, between, contains};
    Operator searchOp() default org.egov.infstr.annotation.Search.Operator.equals;
}
