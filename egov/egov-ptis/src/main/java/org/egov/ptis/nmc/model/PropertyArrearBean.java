package org.egov.ptis.nmc.model;

import java.math.BigDecimal;

public class PropertyArrearBean {
	private String year;
	private BigDecimal taxAmount;

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

}
