/*
 * @(#)ContractorGrade.java 3.0, 6 Jun, 2013 3:13:06 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.Min;

import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.OptionalPattern;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.models.validator.Unique;
import org.egov.infstr.models.validator.constants.ValidatorConstants;
import org.hibernate.validator.constraints.Length;

@Unique(fields = { "grade" }, id = "id", tableName = "EGW_CONTRACTOR_GRADE", columnName = { "GRADE" }, message = "contractorGrade.grade.isunique")
public class ContractorGrade extends BaseModel {

	@Required(message = "contractorGrade.grade.null")
	@Length(max = 20, message = "contractorGrade.grade.length")
	@OptionalPattern(regex = ValidatorConstants.alphaNumericwithSpace, message = "contractorGrade.grade.alphaNumeric")
	private String grade;

	@Required(message = "contractorGrade.description.null")
	@Length(max = 100, message = "contractorGrade.description.length")
	private String description;

	@Required(message = "contractorGrade.minAmount.null")
	@Min(value = 0, message = "contractorGrade.minAmount.valid")
	@OptionalPattern(regex = ValidatorConstants.numeric, message = "contractorGrade.minAmount.numeric")
	private BigDecimal minAmount;

	@Required(message = "contractorGrade.maxAmount.null")
	@Min(value = 0, message = "contractorGrade.maxAmount.valid")
	@OptionalPattern(regex = ValidatorConstants.numeric, message = "contractorGrade.maxAmount.numeric")
	private BigDecimal maxAmount;

	private String maxAmountString;
	private String minAmountString;
	private String code;
	private BigDecimal minSolvency;
	private BigDecimal avgAnnualTurnOver;
	private BigDecimal costOfWorks;
	private BigDecimal emd;
	private String minTechQualification;

	public ContractorGrade() {
		//For hibernate to work
	}

	public ContractorGrade(String grade, String description, BigDecimal minAmount, BigDecimal maxAmount) {
		this.grade = grade;
		this.description = description;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}

	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}

	public String getMaxAmountString() {
		return maxAmountString;
	}

	public void setMaxAmountString(String maxAmountString) {
		this.maxAmountString = maxAmountString;
	}

	public String getMinAmountString() {
		return minAmountString;
	}

	public void setMinAmountString(String minAmountString) {
		this.minAmountString = minAmountString;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getMinSolvency() {
		return minSolvency;
	}

	public void setMinSolvency(BigDecimal minSolvency) {
		this.minSolvency = minSolvency;
	}

	public BigDecimal getAvgAnnualTurnOver() {
		return avgAnnualTurnOver;
	}

	public void setAvgAnnualTurnOver(BigDecimal avgAnnualTurnOver) {
		this.avgAnnualTurnOver = avgAnnualTurnOver;
	}

	public BigDecimal getCostOfWorks() {
		return costOfWorks;
	}

	public void setCostOfWorks(BigDecimal costOfWorks) {
		this.costOfWorks = costOfWorks;
	}

	public BigDecimal getEmd() {
		return emd;
	}

	public void setEmd(BigDecimal emd) {
		this.emd = emd;
	}

	public String getMinTechQualification() {
		return minTechQualification;
	}

	public void setMinTechQualification(String minTechQualification) {
		this.minTechQualification = minTechQualification;
	}

	public List<ValidationError> validate() {
		List<ValidationError> errorList = null;
		if (maxAmount.compareTo(minAmount) == -1) {
			return Arrays.asList(new ValidationError("maxAmount", "contractor.grade.maxamount.invalid"));
		}
		return errorList;

	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContractorGrade [grade=").append(grade).append(", description=").append(description).append(", minAmount=").append(minAmount).append(", maxAmount=").append(maxAmount).append(", maxAmountString=").append(maxAmountString)
				.append(", minAmountString=").append(minAmountString).append(", code=").append(code).append(", minSolvency=").append(minSolvency).append(", avgAnnualTurnOver=").append(avgAnnualTurnOver).append(", costOfWorks=").append(costOfWorks)
				.append(", emd=").append(emd).append("]");
		return builder.toString();
	}

}
