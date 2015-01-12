/*
 * @(#)Area.java 3.0, 6 Jun, 2013 2:43:17 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

public class Area {

	private Float length = null;
	private Float breadth = null;
	private Float area = null;

	/**
	 * @return Returns the length.
	 */
	public Float getLength() {
		return length;
	}

	/**
	 * @param length The length to set.
	 */
	public void setLength(Float length) {
		this.length = length;
	}

	/**
	 * @return Returns the breadth.
	 */
	public Float getBreadth() {
		return breadth;
	}

	/**
	 * @param breadth The breadth to set.
	 */
	public void setBreadth(Float breadth) {
		this.breadth = breadth;
	}

	/**
	 * @return Returns the area.
	 */
	public Float getArea() {
		Float TotalArea = null;
		if ((length != null) && (breadth != null))
			TotalArea = length * breadth;
		else
			TotalArea = area;
		return TotalArea;
	}

	/**
	 * @param area The area to set.
	 */
	public void setArea(Float area) {
		this.area = area;
	}

}
