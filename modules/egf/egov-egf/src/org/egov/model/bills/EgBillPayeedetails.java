package org.egov.model.bills;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.model.recoveries.Recovery;



public class EgBillPayeedetails implements java.io.Serializable {

	Integer id;
	EgBilldetails egBilldetailsId;
	Integer accountDetailTypeId;
	Integer accountDetailKeyId;
	BigDecimal debitAmount;
	BigDecimal creditAmount;
	Date lastUpdatedTime;
	Recovery recovery;
	String narration;
	
	public Integer getAccountDetailKeyId() {
		return accountDetailKeyId;
	}
	public void setAccountDetailKeyId(Integer accountDetailKeyId) {
		this.accountDetailKeyId = accountDetailKeyId;
	}
	
	public EgBilldetails getEgBilldetailsId() {
		return egBilldetailsId;
	}
	public void setEgBilldetailsId(EgBilldetails egBilldetailsId) {
		this.egBilldetailsId = egBilldetailsId;
	}
	public BigDecimal getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}
	public BigDecimal getDebitAmount() {
		return debitAmount;
	}
	public void setDebitAmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}
	public void setLastUpdatedTime(Date lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}
	public Integer getAccountDetailTypeId() {
		return accountDetailTypeId;
	}
	public void setAccountDetailTypeId(Integer accountDetailTypeId) {
		this.accountDetailTypeId = accountDetailTypeId;
	}

	public Recovery getRecovery() {
		return recovery;
	}
	public void setRecovery(Recovery recovery) {
		this.recovery = recovery;
	}
	
	public String getNarration() {
		return narration;
	}
	public void setNarration(String narration) {
		this.narration = narration;
	}
	
	
}
