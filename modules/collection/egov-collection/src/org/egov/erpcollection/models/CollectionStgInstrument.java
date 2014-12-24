package org.egov.erpcollection.models;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.infstr.models.BaseModel;

/**
 * CollectionStgInstrument entity. @author MyEclipse Persistence Tools
 */

public class CollectionStgInstrument extends BaseModel {
	private static final long serialVersionUID = 1L;
	private CollectionStgReceipt collectionStgReceipt;
	private String collMode;
	private BigDecimal amount;
	private String instrNo;
	private Date instrDate;
	private String bank;
	private String branch;
	private Character status;
	private Date bounceDate;
	private String bankAccount;

	public CollectionStgReceipt getCollectionStgReceipt() {
		return this.collectionStgReceipt;
	}

	public void setCollectionStgReceipt(CollectionStgReceipt collectionStgReceipt) {
		this.collectionStgReceipt = collectionStgReceipt;
	}

	public String getCollMode() {
		return this.collMode;
	}

	public void setCollMode(String collMode) {
		this.collMode = collMode;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getInstrNo() {
		return this.instrNo;
	}

	public void setInstrNo(String instrNo) {
		this.instrNo = instrNo;
	}

	public Date getInstrDate() {
		return this.instrDate;
	}

	public void setInstrDate(Date instrDate) {
		this.instrDate = instrDate;
	}

	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBranch() {
		return this.branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	/**
	 * @return the status
	 */
	public Character getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Character status) {
		this.status = status;
	}

	/**
	 * @return the bounceDate
	 */
	public Date getBounceDate() {
		return bounceDate;
	}

	/**
	 * @param bounceDate the bounceDate to set
	 */
	public void setBounceDate(Date bounceDate) {
		this.bounceDate = bounceDate;
	}

	/**
	 * @return the bankAccount
	 */
	public String getBankAccount() {
		return bankAccount;
	}

	/**
	 * @param bankAccount the bankAccount to set
	 */
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	


}