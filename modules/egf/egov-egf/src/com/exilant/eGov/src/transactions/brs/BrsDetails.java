/*
 * BrsDetails.java  Created on Aug 17, 2006
 *
 *  Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.exilant.eGov.src.transactions.brs;

import java.util.List;

/**
 * @author Tilak
 *
 * @Version 1.00
 */
public class BrsDetails {

	public String getCgnum() {
		return cgnum;
	}
	public void setCgnum(String cgnum) {
		this.cgnum = cgnum;
	}
	String voucherNumber;
	String cgnum;
	String voucherDate;
	String type;
	String chequeNumber;
	String chequeDate;
	int recordId;
	String chequeAmount;
	String txnType;
	String instrumentHeaderId;
	List<String> VoucherNumbers;
	/**
	 * @return the voucherNumbers
	 */
	public List<String> getVoucherNumbers() {
		return VoucherNumbers;
	}
	/**
	 * @param voucherNumbers the voucherNumbers to set
	 */
	public void setVoucherNumbers(List<String> voucherNumbers) {
		VoucherNumbers = voucherNumbers;
	}
	/**
	 * @return the voucherHeaderIds
	 */
	public List<Long> getVoucherHeaderIds() {
		return voucherHeaderIds;
	}
	/**
	 * @param voucherHeaderIds the voucherHeaderIds to set
	 */
	public void setVoucherHeaderIds(List<Long> voucherHeaderIds) {
		this.voucherHeaderIds = voucherHeaderIds;
	}
	/**
	 * @return the voucherDates
	 */
	public List<String> getVoucherDates() {
		return voucherDates;
	}
	/**
	 * @param voucherDates the voucherDates to set
	 */
	public void setVoucherDates(List<String> voucherDates) {
		this.voucherDates = voucherDates;
	}
	List<Long> voucherHeaderIds;
	List <String> voucherDates;


	/**
	 * @return the instrumentHeaderId
	 */
	public String getInstrumentHeaderId() {
		return instrumentHeaderId;
	}
	/**
	 * @param instrumentHeaderId the instrumentHeaderId to set
	 */
	public void setInstrumentHeaderId(String instrumentHeaderId) {
		this.instrumentHeaderId = instrumentHeaderId;
	}
	/**
	 * @return Returns the chequeAmount.
	 */
	public String getChequeAmount() {
		return chequeAmount;
	}
	/**
	 * @param chequeAmount The chequeAmount to set.
	 */
	public void setChequeAmount(String chequeAmount) {
		this.chequeAmount = chequeAmount;
	}
	/**
	 * @return Returns the chequeDate.
	 */
	public String getChequeDate() {
		return chequeDate;
	}
	/**
	 * @param chequeDate The chequeDate to set.
	 */
	public void setChequeDate(String chequeDate) {
		this.chequeDate = chequeDate;
	}
	/**
	 * @return Returns the chequeNumber.
	 */
	public String getChequeNumber() {
		return chequeNumber;
	}
	/**
	 * @param chequeNumber The chequeNumber to set.
	 */
	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}
	/**
	 * @return Returns the recordId.
	 */
	public int getRecordId() {
		return recordId;
	}
	/**
	 * @param recordId The recordId to set.
	 */
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return Returns the voucherDate.
	 */
	public String getVoucherDate() {
		return voucherDate;
	}
	/**
	 * @param voucherDate The voucherDate to set.
	 */
	public void setVoucherDate(String voucherDate) {
		this.voucherDate = voucherDate;
	}
	/**
	 * @return Returns the voucherNumber.
	 */
	public String getVoucherNumber() {
		return voucherNumber;
	}
	/**
	 * @param voucherNumber The voucherNumber to set.
	 */
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	/**
	 * @return Returns the txnType.
	 */
	public String getTxnType() {
		return txnType;
	}
	/**
	 * @param txnType The txnType to set.
	 */
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
}
