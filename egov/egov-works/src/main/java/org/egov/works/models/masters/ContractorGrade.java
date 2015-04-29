package org.egov.works.models.masters;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.Min;

import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.validation.regex.Constants;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.hibernate.validator.constraints.Length;

@Unique(fields={"grade"},id="id",tableName="EGW_CONTRACTOR_GRADE",columnName={"GRADE"},message="contractorGrade.grade.isunique")
public class ContractorGrade extends BaseModel {
	
	@Required(message="contractorGrade.grade.null")
	@Length(max=20,message="contractorGrade.grade.length")
	@OptionalPattern(regex=Constants.ALPHANUMERIC_WITHSPACE,message="contractorGrade.grade.alphaNumeric")
	private String grade;
	 
	@Required(message="contractorGrade.description.null")
	@Length(max=100,message="contractorGrade.description.length")
	private String description;
	 
	@Required(message="contractorGrade.minAmount.null")
	@Min(value=0, message="contractorGrade.minAmount.valid")
	@OptionalPattern(regex=Constants.NUMERIC,message="contractorGrade.minAmount.numeric")
	private double minAmount;
	
	@Required(message="contractorGrade.maxAmount.null")
	@Min(value=0, message="contractorGrade.maxAmount.valid")
	@OptionalPattern(regex=Constants.NUMERIC,message="contractorGrade.maxAmount.numeric")
	private double maxAmount;
	
	private String maxAmountString;
	private String minAmountString;

	public ContractorGrade() {}
	
	public ContractorGrade(String grade, String description, double minAmount, double maxAmount) {
		this.grade=grade;
		this.description=description;
		this.minAmount=minAmount;
		this.maxAmount=maxAmount;
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

	public double getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(double minAmount) {
		this.minAmount = minAmount;
	}

	public double getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(double maxAmount) {
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

	public List<ValidationError> validate()	{
		List<ValidationError> errorList = null;
		if(maxAmount <= minAmount) {
			return Arrays.asList(new ValidationError("maxAmount","contractor.grade.maxamount.invalid"));
		}
		
		 return errorList;
				
	}			
		
}


