package org.egov.ptis.nmc.model;

import java.math.BigDecimal;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("taxdetail")
public class MiscellaneousTaxDetail {
    // %value or amount value
    private BigDecimal taxValue;
    private BigDecimal actualTaxValue;
    private BigDecimal calculatedTaxValue;
    private Integer noOfDays;
    
    @XStreamAsAttribute
    private Date fromDate;
    
    @XStreamAsAttribute
    private Character isHistory = 'N';

    
    public MiscellaneousTaxDetail() {}
    
    public MiscellaneousTaxDetail(MiscellaneousTaxDetail taxDetail) {
    	this.taxValue = taxDetail.getTaxValue();
    	this.actualTaxValue = taxDetail.getActualTaxValue();
    	this.calculatedTaxValue = taxDetail.getCalculatedTaxValue();
    	this.fromDate = taxDetail.getFromDate();
    	this.noOfDays = taxDetail.getNoOfDays();
    	this.isHistory = taxDetail.getIsHistory();
    }
    
	public BigDecimal getTaxValue() {
		return taxValue;
	}

	public void setTaxValue(BigDecimal taxValue) {
		this.taxValue = taxValue;
	}

	public BigDecimal getActualTaxValue() {
		return actualTaxValue;
	}

	public void setActualTaxValue(BigDecimal actualTaxValue) {
		this.actualTaxValue = actualTaxValue;
	}

	public BigDecimal getCalculatedTaxValue() {
		return calculatedTaxValue;
	}

	public void setCalculatedTaxValue(BigDecimal calculatedTaxValue) {
		this.calculatedTaxValue = calculatedTaxValue;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}  
	
	public Integer getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(Integer noOfDays) {
		this.noOfDays = noOfDays;
	}
	
	public Character getIsHistory() {
		return isHistory;
	}

	public void setIsHistory(Character isHistory) {
		this.isHistory = isHistory;
	}

	@Override
	public String toString() {
		return new StringBuilder().append("MiscellaneousTaxDetail [")
				.append("taxValue=").append(getTaxValue())
				.append(", actualTaxValue=").append(getActualTaxValue())
				.append(", calculatedTaxValue=").append(getCalculatedTaxValue())
				.append(", fromDate=").append(getFromDate())
				.append(", noOfDays=").append(getNoOfDays())
				.append(", isHistory=").append(getIsHistory())
				.append("]").toString();
	}
}
