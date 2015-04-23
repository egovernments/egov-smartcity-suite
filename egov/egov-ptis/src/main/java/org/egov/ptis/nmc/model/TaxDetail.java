package org.egov.ptis.nmc.model;

import java.math.BigDecimal;
import java.util.Date;

public class TaxDetail {
	private String taxName;
	private BigDecimal calculatedTax;
	private Integer noOfDays;
	private Date fromDate;
	/**
	 * @return the taxName
	 */
	public String getTaxName() {
		return taxName;
	}
	/**
	 * @param taxName the taxName to set
	 */
	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}
	/**
	 * @return the calculatedTax
	 */
	public BigDecimal getCalculatedTax() {
		return calculatedTax;
	}
	/**
	 * @param calculatedTax the calculatedTax to set
	 */
	public void setCalculatedTax(BigDecimal calculatedTax) {
		this.calculatedTax = calculatedTax;
	}
	/**
	 * @return the noOfDays
	 */
	public Integer getNoOfDays() {
		return noOfDays;
	}
	/**
	 * @param noOfDays the noOfDays to set
	 */
	public void setNoOfDays(Integer noOfDays) {
		this.noOfDays = noOfDays;
	}
	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return fromDate;
	}
	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	
	@Override
	public String toString() {
		return new StringBuilder(100)
			.append("TaxDetail [ taxName=").append(taxName)
			.append(", calculatedTax=").append(calculatedTax)
			.append(", noOfDays=").append(noOfDays)
			.append(", fromDate=").append(fromDate)
			.append("]").toString();
	}
}
