/*
 * @(#)EventCallBack.java 3.0, 17 Jun, 2013 12:01:15 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.events.processing;

import java.util.Map;

public interface EventCallBack {
	public void execute(Map<String, String> params);

}
