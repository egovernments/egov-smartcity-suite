package org.egov.model.cheque;

import java.util.Date;

import org.egov.commons.Bankaccount;
import org.egov.infstr.models.BaseModel;
import org.egov.infra.admin.master.entity.Department;

public class AccountCheques extends BaseModel {
	private static final long	serialVersionUID	= 1L;
	private Bankaccount			bankAccountId;
	private String				fromChequeNumber;
	private String				toChequeNumber;
	private Date				receivedDate;
	private Boolean				isExhausted;
	private String				nextChqNo;
	private String              serialNo;

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Bankaccount getBankAccountId() {
		return bankAccountId;
	}
	
	public String getFromChequeNumber() {
		return fromChequeNumber;
	}
	
	public Boolean getIsExhausted() {
		return isExhausted;
	}
	
	public String getNextChqNo() {
		return nextChqNo;
	}
	
	public Date getReceivedDate() {
		return receivedDate;
	}
	
	public String getToChequeNumber() {
		return toChequeNumber;
	}
	
	public void setBankAccountId(final Bankaccount bankAccountId) {
		this.bankAccountId = bankAccountId;
	}
	
	public void setFromChequeNumber(final String fromChequeNumber) {
		this.fromChequeNumber = fromChequeNumber;
	}
	
	public void setIsExhausted(final Boolean isExhausted) {
		this.isExhausted = isExhausted;
	}
	
	public void setNextChqNo(final String nextChqNo) {
		this.nextChqNo = nextChqNo;
	}
	
	public void setReceivedDate(final Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	
	public void setToChequeNumber(final String toChequeNumber) {
		this.toChequeNumber = toChequeNumber;
	}
}
