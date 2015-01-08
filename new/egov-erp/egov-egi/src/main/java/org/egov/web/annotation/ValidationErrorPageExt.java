/*
 * @(#)ValidationErrorPageExt.java 3.0, 14 Jun, 2013 12:37:00 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.annotation;

import static com.opensymphony.xwork2.Action.SUCCESS;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Purpose of this annotation is same as {@link ValidationErrorPage}, In addition to 
 * main functionality, this annotation also provide two more annotation parameter<br/>
 * toMethod:methodName<br/>
 * makeCall:boolean<br/>
 * Setting makeCall to true and mentioning methodName (any method name in the same action)
 * will cause the action to invoke the given method after ValidationException is processed.
 * 
 * This is useful incase of re populating dropdowns and other dynamic field in the page after 
 * if there is a ValidationException is thrown from an Action method.
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ValidationErrorPageExt {
	String action() default SUCCESS;
	String toMethod() default "";
	
	boolean makeCall() default false;
}
