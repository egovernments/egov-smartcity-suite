/*
 * @(#)EgSurrenderedCheques.java 3.0, 6 Jun, 2013 3:36:21 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.util.Date;

public class EgSurrenderedCheques implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private Bankaccount bankaccount;

	private String chequenumber;

	private Date chequedate;

	private CVoucherHeader voucherheader;

	private Date lastmodifieddate;

	public EgSurrenderedCheques() {
		//For hibernate to work
	}

	public EgSurrenderedCheques(Bankaccount bankaccount, String chequenumber, Date chequedate, CVoucherHeader voucherheader, Date lastmodifieddate) {
		this.bankaccount = bankaccount;
		this.chequenumber = chequenumber;
		this.chequedate = chequedate;
		this.voucherheader = voucherheader;
		this.lastmodifieddate = lastmodifieddate;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Bankaccount getBankaccount() {
		return this.bankaccount;
	}

	public void setBankaccount(Bankaccount bankaccount) {
		this.bankaccount = bankaccount;
	}

	public String getChequenumber() {
		return this.chequenumber;
	}

	public void setChequenumber(String chequenumber) {
		this.chequenumber = chequenumber;
	}

	public Date getChequedate() {
		return this.chequedate;
	}

	public void setChequedate(Date chequedate) {
		this.chequedate = chequedate;
	}

	public CVoucherHeader getVoucherheader() {
		return this.voucherheader;
	}

	public void setVoucherheader(CVoucherHeader voucherheader) {
		this.voucherheader = voucherheader;
	}

	public Date getLastmodifieddate() {
		return this.lastmodifieddate;
	}

	public void setLastmodifieddate(Date lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
	}

}
