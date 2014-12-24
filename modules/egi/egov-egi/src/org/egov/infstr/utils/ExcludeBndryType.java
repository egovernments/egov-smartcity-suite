/*
 * @(#)ExcludeBndryType.java 3.0, 18 Jun, 2013 12:02:49 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

import java.util.ArrayList;
import java.util.List;

public class ExcludeBndryType {
	private static ArrayList excludeType = new ArrayList();

	static {
		final String[] excludeList = EGovConfig.getArray("EXCLUDE_LEVEL", new String[] {}, "ADMBNDRY");
		for (final String type : excludeList) {
			excludeType.add(type);
		}

	}

	public static List getExcludeType() {
		return excludeType;
	}

}