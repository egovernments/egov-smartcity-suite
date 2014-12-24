/*
 * @(#)GeoLocation.java 3.0, 17 Jun, 2013 2:48:55 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.models;

import java.util.Map;

public class GeoLocation {

	private GeoLatLong geoLatLong;

	private String info1;

	private String info2;

	private String info3;

	private String info4;

	private String urlRedirect;

	private String urlDisplay;

	// Meta data information for the marker option
	private Map<String, Object> markerOptionData;

	public GeoLatLong getGeoLatLong() {
		return geoLatLong;
	}

	public String getInfo1() {
		return info1;
	}

	public String getInfo2() {
		return info2;
	}

	public String getInfo3() {
		return info3;
	}

	public String getInfo4() {
		return info4;
	}

	public String getUrlRedirect() {
		return urlRedirect;
	}

	public String getUrlDisplay() {
		return urlDisplay;
	}

	public void setGeoLatLong(GeoLatLong geoLatLong) {
		this.geoLatLong = geoLatLong;
	}

	public void setInfo1(String info1) {
		this.info1 = info1;
	}

	public void setInfo2(String info2) {
		this.info2 = info2;
	}

	public void setInfo3(String info3) {
		this.info3 = info3;
	}

	public void setInfo4(String info4) {
		this.info4 = info4;
	}

	public void setUrlRedirect(String urlRedirect) {
		this.urlRedirect = urlRedirect;
	}

	public void setUrlDisplay(String urlDisplay) {
		this.urlDisplay = urlDisplay;
	}

	public Map<String, Object> getMarkerOptionData() {
		return markerOptionData;
	}

	public void setMarkerOptionData(Map<String, Object> markerOptionData) {
		this.markerOptionData = markerOptionData;
	}

}
