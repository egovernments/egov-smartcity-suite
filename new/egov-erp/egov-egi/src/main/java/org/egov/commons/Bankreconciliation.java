/*
 * @(#)Bankreconciliation.java 3.0, 6 Jun, 2013 2:48:18 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.math.BigDecimal;

public class Bankreconciliation implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private Bankaccount bankaccount;

	private BigDecimal amount;

	private String transactiontype;

	private Long instrumentHeaderId;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the bankaccount
	 */
	public Bankaccount getBankaccount() {
		return bankaccount;
	}

	/**
	 * @param bankaccount the bankaccount to set
	 */
	public void setBankaccount(Bankaccount bankaccount) {
		this.bankaccount = bankaccount;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the transactiontype
	 */
	public String getTransactiontype() {
		return transactiontype;
	}

	/**
	 * @param transactiontype the transactiontype to set
	 */
	public void setTransactiontype(String transactiontype) {
		this.transactiontype = transactiontype;
	}

	/**
	 * @return the instrumentHeaderId
	 */
	public Long getInstrumentHeaderId() {
		return instrumentHeaderId;
	}

	/**
	 * @param instrumentHeaderId the instrumentHeaderId to set
	 */
	public void setInstrumentHeaderId(Long instrumentHeaderId) {
		this.instrumentHeaderId = instrumentHeaderId;
	}

}
