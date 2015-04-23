package org.egov.model.brs;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.infstr.models.BaseModel;

public class AutoReconcileBean extends BaseModel {
	private static final long serialVersionUID = -3495802029238920474L;
	private String accountnumber;
	private Long accountId;
	private Date txDate;
	private String txDateStr;
	private String Type;
	private String instrumentNo;
	private BigDecimal debit;
	private BigDecimal credit;
	private BigDecimal balance;
	private String narration;
	private String CSLno;
	private Date createdDate;
	private String action;
	private Date reconciliationDate;
	private String errorCode;
	private String errorMessage;
	private String noDetailsFound;

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Date getReconciliationDate() {
		return reconciliationDate;
	}

	public void setReconciliationDate(Date reconciliationDate) {
		this.reconciliationDate = reconciliationDate;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getAccountnumber() {
		return accountnumber;
	}

	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber;
	}

	public Long getAccountId() {
		return accountId;
	}

	public Date getTxDate() {
		return txDate;
	}

	public void setTxDate(Date txDate) {
		this.txDate = txDate;
	}

	
	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getInstrumentNo() {
		return instrumentNo;
	}

	public void setInstrumentNo(String instrumentNo) {
		this.instrumentNo = instrumentNo;
	}

	public BigDecimal getDebit() {
		return debit;
	}

	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getCSLno() {
		return CSLno;
	}

	public void setCSLno(String lno) {
		CSLno = lno;
	}

	@Override
	public Date getCreatedDate() {
		return createdDate;
	}

	@Override
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	public String getTxDateStr() {
		return txDateStr;
	}

	public void setTxDateStr(String txDateStr) {
		this.txDateStr = txDateStr;
	}
	
	public String toString()
	{
		StringBuffer str=new StringBuffer(1024);
		str.append(" id :").append(id)
		.append(" txDateStr:").append(txDateStr)
		.append(" instrumentNo:").append(instrumentNo)
		.append(" narration:").append(narration)
		.append(" debit:").append(debit)
		.append(" credit:").append(credit)
		.append(" balance:").append(balance);
		return str.toString();
		
		
		
	}

	public String getNoDetailsFound() {
		return noDetailsFound;
	}

	public void setNoDetailsFound(String noDetailsFound) {
		this.noDetailsFound = noDetailsFound;
	}

	

}
