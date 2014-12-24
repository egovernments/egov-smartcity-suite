package org.egov.model.budget;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import org.egov.commons.CChartOfAccounts;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.models.validator.Unique;
import org.egov.utils.BudgetAccountType;
import org.egov.utils.BudgetingType;
import org.hibernate.validator.constraints.Length;

@Unique(fields="name",id="id",columnName="NAME",tableName="EGF_BUDGETGROUP",message="budgetgroup.name.isunique")
public class BudgetGroup extends BaseModel {
	@Required(message="Name should not be empty")
	private String name;
	@Length(max=250,message="Max 250 characters are allowed for description")
	private String description;
	private CChartOfAccounts majorCode;
	private CChartOfAccounts maxCode;
	private CChartOfAccounts minCode;
	@Enumerated(value = EnumType.STRING)
	private BudgetAccountType accountType;
	@Enumerated(value = EnumType.STRING)
	private BudgetingType budgetingType;
	private boolean isActive;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public CChartOfAccounts getMajorCode() {
		return majorCode;
	}
	public void setMajorCode(CChartOfAccounts majorCode) {
		this.majorCode = majorCode;
	}
	public CChartOfAccounts getMaxCode() {
		return maxCode;
	}
	public void setMaxCode(CChartOfAccounts maxCode) {
		this.maxCode = maxCode;
	}
	public CChartOfAccounts getMinCode() {
		return minCode;
	}
	public void setMinCode(CChartOfAccounts minCode) {
		this.minCode = minCode;
	}
	@NotNull(message="Please select accounttype")
	public BudgetAccountType getAccountType() {
		return accountType;
	}
	public void setAccountType(BudgetAccountType accountType) {
		this.accountType = accountType;
	}
	@NotNull(message="Please select budgetingtype") 
	public BudgetingType getBudgetingType() {
		return budgetingType;
	}
	public void setBudgetingType(BudgetingType budgetingType) {
		this.budgetingType = budgetingType;
	}
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
}
