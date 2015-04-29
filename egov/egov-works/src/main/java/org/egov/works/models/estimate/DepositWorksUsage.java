package org.egov.works.models.estimate;



import java.math.BigDecimal;
import java.util.Date;

import org.egov.commons.CChartOfAccounts;
import org.egov.infstr.models.BaseModel;
import org.egov.works.models.masters.DepositCode;

/**
 * @author Sathish P
 *
 */
public class DepositWorksUsage extends BaseModel {
	
	private AbstractEstimate abstractEstimate;
	private BigDecimal totalDepositAmount;
	private BigDecimal consumedAmount;
	private BigDecimal releasedAmount;
	private String appropriationNumber;
	private Date appropriationDate;
	private Integer financialYearId;
	private DepositCode depositCode;
	private CChartOfAccounts coa;
	
	public AbstractEstimate getAbstractEstimate() {
		return abstractEstimate;
	}
	public void setAbstractEstimate(AbstractEstimate abstractEstimate) {
		this.abstractEstimate = abstractEstimate;
	}
	public BigDecimal getTotalDepositAmount() {
		return totalDepositAmount;
	}
	public void setTotalDepositAmount(BigDecimal totalDepositAmount) {
		this.totalDepositAmount = totalDepositAmount;
	}
	public BigDecimal getConsumedAmount() {
		return consumedAmount;
	}
	public void setConsumedAmount(BigDecimal consumedAmount) {
		this.consumedAmount = consumedAmount;
	}
	public BigDecimal getReleasedAmount() {
		return releasedAmount;
	}
	public void setReleasedAmount(BigDecimal releasedAmount) {
		this.releasedAmount = releasedAmount;
	}
	public String getAppropriationNumber() {
		return appropriationNumber;
	}
	public void setAppropriationNumber(String appropriationNumber) {
		this.appropriationNumber = appropriationNumber;
	}
	public Date getAppropriationDate() {
		return appropriationDate;
	}
	public void setAppropriationDate(Date appropriationDate) {
		this.appropriationDate = appropriationDate;
	}

	public Integer getFinancialYearId() {
		return financialYearId;
	}

	public void setFinancialYearId(Integer financialYearId) {
		this.financialYearId = financialYearId;
	}
	public DepositCode getDepositCode() {
		return depositCode;
	}
	public void setDepositCode(DepositCode depositCode) {
		this.depositCode = depositCode;
	}
	public CChartOfAccounts getCoa() {
		return coa;
	}
	public void setCoa(CChartOfAccounts coa) {
		this.coa = coa;
	}
	

}
