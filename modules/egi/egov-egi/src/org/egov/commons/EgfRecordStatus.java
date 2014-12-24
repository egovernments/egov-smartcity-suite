/*
 * @(#)EgfRecordStatus.java 3.0, 6 Jun, 2013 3:21:13 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.util.Date;

public class EgfRecordStatus implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private CVoucherHeader voucherheader;
	private String recordType;
	private Integer status;
	private Date updatedtime;
	private Integer userid;

	public EgfRecordStatus() {
		//For hibernate to work
	}

	public EgfRecordStatus(CVoucherHeader voucherheader, String recordType, Integer status, Date updatedtime, Integer userid) {
		this.voucherheader = voucherheader;
		this.recordType = recordType;
		this.status = status;
		this.updatedtime = updatedtime;
		this.userid = userid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CVoucherHeader getVoucherheader() {
		return voucherheader;
	}

	public void setVoucherheader(CVoucherHeader voucherheader) {
		this.voucherheader = voucherheader;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getUpdatedtime() {
		return updatedtime;
	}

	public void setUpdatedtime(Date updatedtime) {
		this.updatedtime = updatedtime;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

}