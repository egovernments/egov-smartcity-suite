/*
 * @(#)ApplicationSecurityEventAction.java 3.0, 12 Jul, 2013 6:05:23 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.spring.event.actions;

import org.springframework.context.ApplicationEvent;

public interface ApplicationSecurityEventAction<T extends ApplicationEvent> {
	void doAction(T event); 
}
