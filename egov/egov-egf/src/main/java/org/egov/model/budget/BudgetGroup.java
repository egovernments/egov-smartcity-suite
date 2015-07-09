/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.model.budget;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import org.egov.commons.CChartOfAccounts;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infstr.models.BaseModel;
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
