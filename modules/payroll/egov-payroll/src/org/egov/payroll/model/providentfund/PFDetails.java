package org.egov.payroll.model.providentfund;

import java.math.BigDecimal;

public class PFDetails implements java.io.Serializable
{
	private Integer detailId = null;
	private Integer pfHeaderId = null;
	private java.util.Date fromDate = null;
	private java.util.Date toDate = null;
	private BigDecimal annualRateOfInterest = null;
	
	public PFDetails()
	{}
	
	
	public Integer getDetailId() {
		return detailId;
	}

	public void setDetailId(Integer detailId) {
		this.detailId = detailId;
	}

	public Integer getPfHeaderId() {
		return pfHeaderId;
	}
	public void setPfHeaderId(Integer pfHeaderId) {
		this.pfHeaderId = pfHeaderId;
	}
	

	public java.util.Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(java.util.Date fromDate) {
		this.fromDate = fromDate;
	}

	public java.util.Date getToDate() {
		return toDate;
	}

	public void setToDate(java.util.Date toDate) {
		this.toDate = toDate;
	}

	public BigDecimal getAnnualRateOfInterest() {
		return annualRateOfInterest;
	}
	public void setAnnualRateOfInterest(BigDecimal annualRateOfInterest) {
		this.annualRateOfInterest = annualRateOfInterest;
	}
	
}
