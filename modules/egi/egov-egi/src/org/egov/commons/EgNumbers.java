/*
 * @(#)EgNumbers.java 3.0, 6 Jun, 2013 3:32:18 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.math.BigDecimal;

public class EgNumbers implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String vouchertype;

	private BigDecimal vouchernumber;

	private BigDecimal fiscialperiodid;

	private BigDecimal month;

	public EgNumbers() {
		// For hibernate to work
	}

	public EgNumbers(Long id, String vouchertype, BigDecimal vouchernumber, BigDecimal fiscialperiodid) {
		this.id = id;
		this.vouchertype = vouchertype;
		this.vouchernumber = vouchernumber;
		this.fiscialperiodid = fiscialperiodid;
	}

	public EgNumbers(Long id, String vouchertype, BigDecimal vouchernumber, BigDecimal fiscialperiodid, BigDecimal month) {
		this.id = id;
		this.vouchertype = vouchertype;
		this.vouchernumber = vouchernumber;
		this.fiscialperiodid = fiscialperiodid;
		this.month = month;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVouchertype() {
		return this.vouchertype;
	}

	public void setVouchertype(String vouchertype) {
		this.vouchertype = vouchertype;
	}

	public BigDecimal getVouchernumber() {
		return this.vouchernumber;
	}

	public void setVouchernumber(BigDecimal vouchernumber) {
		this.vouchernumber = vouchernumber;
	}

	public BigDecimal getFiscialperiodid() {
		return this.fiscialperiodid;
	}

	public void setFiscialperiodid(BigDecimal fiscialperiodid) {
		this.fiscialperiodid = fiscialperiodid;
	}

	public BigDecimal getMonth() {
		return this.month;
	}

	public void setMonth(BigDecimal month) {
		this.month = month;
	}

}
