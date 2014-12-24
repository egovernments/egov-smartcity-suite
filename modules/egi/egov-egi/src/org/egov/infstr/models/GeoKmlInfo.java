/*
 * @(#)GeoKmlInfo.java 3.0, 17 Jun, 2013 2:48:30 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.models;

import java.util.HashMap;
import java.util.Map;

public class GeoKmlInfo {

	private Map<String, String> wardWiseColor = new HashMap<String, String>();

	private Map<String, String> colorCodes = new HashMap<String, String>();

	public Map<String, String> getWardWiseColor() {
		return wardWiseColor;
	}

	public void setWardWiseColor(Map<String, String> wardWiseColor) {
		this.wardWiseColor = wardWiseColor;
	}

	public Map<String, String> getColorCodes() {
		return colorCodes;
	}

	public void setColorCodes(Map<String, String> colorCodes) {
		this.colorCodes = colorCodes;
	}

}
