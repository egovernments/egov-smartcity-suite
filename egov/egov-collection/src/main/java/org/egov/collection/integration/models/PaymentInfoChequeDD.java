/**
 * 
 */
package org.egov.collection.integration.models;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author rishi
 *
 */
public class PaymentInfoChequeDD implements PaymentInfo {
	
	private Long bankId;
	private String branchName;
	private Date instrumentDate;
	private String instrumentNumber;
	private TYPE instrumentType;
	private BigDecimal instrumentAmount;
	
	/**
	 * Default constructor
	 */
	public PaymentInfoChequeDD(){
	}
	
	public PaymentInfoChequeDD(Long bankId, String branchName, Date instrumentDate,
			String instrumentNumber,TYPE instrumentType,
			BigDecimal instrumentAmount){
		this.bankId=bankId;
		this.branchName=branchName;
		this.instrumentDate=instrumentDate;
		this.instrumentNumber=instrumentNumber;
		this.instrumentType=instrumentType;
		this.instrumentAmount=instrumentAmount;
	}
	
	public Long getBankId() {
		return bankId;
	}

	public String getBranchName() {
		return branchName;
	}

	@Override
	public BigDecimal getInstrumentAmount() {
		return instrumentAmount;
	}

	public Date getInstrumentDate() {
		return instrumentDate;
	}

	
	public String getInstrumentNumber() {
		return instrumentNumber;
	}

	@Override
	public TYPE getInstrumentType() {
		return instrumentType;
	}

	/**
	 * @param bankId the bankId to set
	 */
	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	/**
	 * @param branchName the branchName to set
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	/**
	 * @param instrumentDate the instrumentDate to set
	 */
	public void setInstrumentDate(Date instrumentDate) {
		this.instrumentDate = instrumentDate;
	}

	/**
	 * @param instrumentNumber the instrumentNumber to set
	 */
	public void setInstrumentNumber(String instrumentNumber) {
		this.instrumentNumber = instrumentNumber;
	}

	/**
	 * @param instrumentAmount the instrumentAmount to set
	 */
	public void setInstrumentAmount(BigDecimal instrumentAmount) {
		this.instrumentAmount = instrumentAmount;
	}

	/**
	 * @param instrumentType the instrumentType to set
	 */
	public void setInstrumentType(TYPE instrumentType) {
		this.instrumentType = instrumentType;
	}
}
