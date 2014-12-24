package org.egov.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.egov.infstr.ValidationException;

import com.opensymphony.xwork2.validator.ValidationInterceptor;

/**
 * This annotation is used to annotate, action's method which possibly 
 * throws a {@link ValidationException}. {@link ValidationInterceptor}
 * will invoke the {@link ValidationErrorPageForward#forwarderMethod()}
 * and the returned result name will be used forward the request.
 * The value for {@link ValidationErrorPageForward#forwarderMethod()}
 * must follow certain rules as follows
 * (a) It must be a valid method name 
 * (b) Method must exist in the same or parent class.
 * (c) Method should be public
 * (d) Method should always return a String value
 * (e) The returning string value must be a valid result name
 * (f) Method signature should not contain parameter.
 * e.g.
 * <code>
 * @ValidationErrorPageForward(forwarderMethod="getResult")
 * public String save() {
 * 		//.......
 * 		//do your code
 * }
 * 
 * public String getResult() {
 * 		//.... do your code
 * 		return your result name;
 * }
 * </code>  
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ValidationErrorPageForward {
	String forwarderMethod();
}