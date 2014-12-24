package org.egov.model.advance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.egov.commons.Accountdetailtype;
import org.egov.model.recoveries.Recovery;

public class EgAdvanceReqPayeeDetails implements Serializable{
	private Long id;
	private Date lastUpdatedTime;
	private EgAdvanceRequisitionDetails egAdvanceRequisitionDetails;
	private Recovery recovery;
	private Accountdetailtype accountDetailType;
	private Integer accountdetailKeyId;
	private BigDecimal debitAmount;
	private BigDecimal creditAmount;	
	private String narration;
	
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}


	public void setLastUpdatedTime(Date lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}


	public EgAdvanceRequisitionDetails getEgAdvanceRequisitionDetails() {
		return egAdvanceRequisitionDetails;
	}


	public void setEgAdvanceRequisitionDetails(
			EgAdvanceRequisitionDetails egAdvanceRequisitionDetails) {
		this.egAdvanceRequisitionDetails = egAdvanceRequisitionDetails;
	}

	public Recovery getRecovery() {
		return recovery;
	}

	public void setRecovery(Recovery recovery) {
		this.recovery = recovery;
	}

	public Accountdetailtype getAccountDetailType() {
		return accountDetailType;
	}


	public void setAccountDetailType(Accountdetailtype accountDetailType) {
		this.accountDetailType = accountDetailType;
	}


	public Integer getAccountdetailKeyId() {
		return accountdetailKeyId;
	}


	public void setAccountdetailKeyId(Integer accountdetailKeyId) {
		this.accountdetailKeyId = accountdetailKeyId;
	}


	public BigDecimal getDebitAmount() {
		return debitAmount;
	}


	public void setDebitAmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}


	public BigDecimal getCreditAmount() {
		return creditAmount;
	}


	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}


	public String getNarration() {
		return narration;
	}


	public void setNarration(String narration) {
		this.narration = narration;
	}


	public EgAdvanceReqPayeeDetails(Long id, Date lastUpdatedTime,
			EgAdvanceRequisitionDetails egAdvanceRequisitionDetails, Recovery recovery,
			Accountdetailtype accountDetailType, Integer accountdetailKeyId,
			BigDecimal debitAmount, BigDecimal creditAmount, String narration) {
		super();
		this.id = id;
		this.lastUpdatedTime = lastUpdatedTime;
		this.egAdvanceRequisitionDetails = egAdvanceRequisitionDetails;
		this.recovery = recovery;
		this.accountDetailType = accountDetailType;
		this.accountdetailKeyId = accountdetailKeyId;
		this.debitAmount = debitAmount;
		this.creditAmount = creditAmount;
		this.narration = narration;
	}


	public EgAdvanceReqPayeeDetails() {
		super();
		
	}
	
	
}
