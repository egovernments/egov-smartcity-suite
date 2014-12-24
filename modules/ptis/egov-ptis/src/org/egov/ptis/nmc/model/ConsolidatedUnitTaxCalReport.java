package org.egov.ptis.nmc.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * ConsolidatedUnitTaxCalReport class is used to provide consolidated Unit tax
 * calculation information in generation of Calculation Sheet
 * 
 * @author subhash
 * 
 */
public class ConsolidatedUnitTaxCalReport {

	private BigDecimal annualLettingValue;
	private BigDecimal monthlyRent;
	private BigDecimal annualRentBeforeDeduction;
	private BigDecimal deductionAmount;
	private String instDate;
	private List<UnitTaxCalculationInfo> unitTaxCalInfo;
	
	public BigDecimal getAnnualLettingValue() {
		return annualLettingValue;
	}
	
	public BigDecimal getMonthlyRent() {
		return monthlyRent;
	}

	public void setMonthlyRent(BigDecimal monthlyRent) {
		this.monthlyRent = monthlyRent;
	}

	public BigDecimal getAnnualRentBeforeDeduction() {
		return annualRentBeforeDeduction;
	}

	public void setAnnualRentBeforeDeduction(BigDecimal annualRentBeforeDeduction) {
		this.annualRentBeforeDeduction = annualRentBeforeDeduction;
	}

	public BigDecimal getDeductionAmount() {
		return deductionAmount;
	}

	public void setDeductionAmount(BigDecimal deductionAmount) {
		this.deductionAmount = deductionAmount;
	}

	public void setAnnualLettingValue(BigDecimal annualLettingValue) {
		this.annualLettingValue = annualLettingValue;
	}
	public List<UnitTaxCalculationInfo> getUnitTaxCalInfo() {
		return unitTaxCalInfo;
	}
	public void setUnitTaxCalInfo(List<UnitTaxCalculationInfo> unitTaxCalInfo) {
		this.unitTaxCalInfo = unitTaxCalInfo;
	}
	
	public void addUnitTaxCalInfo(UnitTaxCalculationInfo unitTaxCalInfo) {
		this.getUnitTaxCalInfo().add(unitTaxCalInfo);
	}

	public String getInstDate() {
		return instDate;
	}

	public void setInstDate(String instDate) {
		this.instDate = instDate;
	}
	
}
