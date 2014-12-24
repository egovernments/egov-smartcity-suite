/*
 * @(#)GradeMaster.java 3.0, 7 Jun, 2013 8:33:58 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pims.model;

public class GradeMaster implements GenericMaster {
	private static final long serialVersionUID = 1L;
	public Integer id;
	public Integer age;
	public String name;
	public java.util.Date fromDate;
	public java.util.Date toDate;
	public Integer orderNo;
	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public java.util.Date getFromDate() {
		return fromDate;
	}
	
	public void setFromDate(java.util.Date fromDate) {
		this.fromDate = fromDate;
	}
	
	public java.util.Date getToDate() {
		return toDate;
	}
	
	public void setToDate(java.util.Date toDate) {
		this.toDate = toDate;
	}
	
	public Integer getAge() {
		return age;
	}
	
	public void setAge(Integer age) {
		this.age = age;
	}

	/**
	 * @return the orderNo
	 */
	public Integer getOrderNo() {
		return orderNo;
	}

	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	
}
