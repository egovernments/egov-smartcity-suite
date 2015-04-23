package org.egov.model.budget;

import java.util.Date;

import org.egov.commons.CFinancialYear;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.workflow.entity.StateAware;
import org.hibernate.validator.constraints.Length;

@Unique(fields="name",id="id",columnName="NAME",tableName="EGF_BUDGET",message="budget.name.isunique")
public class Budget extends StateAware  
{
	private String name;
	private String isbere;
	private CFinancialYear  financialYear;
	private Budget parent;
	@Length(max=250,message="Max 250 characters are allowed for description")
	private String description;
	private Date asOnDate;
	private boolean isActiveBudget;
	private boolean isPrimaryBudget;
	private String materializedPath;
	private Budget referenceBudget;
	private Long documentNumber;
	
	public Budget getParent() {
		return parent;
	}

	public void setParent(final Budget parent) {
		this.parent = parent;
	}

	public Date getAsOnDate() {
		return asOnDate;
	}

	public void setAsOnDate(final Date asOnDate) {
		this.asOnDate = asOnDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String desc) {
		this.description = desc;
	}
	@Required(message="Financial Year is required")
	public CFinancialYear getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(final CFinancialYear finYear) {
		this.financialYear = finYear;
	}
	@Required(message="Name should not be empty")
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
	/**
	 * @return isbere
	 */
	@Required(message="BE/RE is required")
	public String getIsbere() {
		if(isbere==null)isbere="BE";
		return isbere;
	}

	/**
	 * @param isbere the isbere to set
	 */
	public void setIsbere(final String isbere) {
		this.isbere = isbere;
	}
	/**
	 * @return isActiveBudget
	 */
	public boolean getIsActiveBudget() {
		return isActiveBudget;
	}

	/**
	 * @param isActiveBudget the isActiveBudget to set
	 */
	public void setIsActiveBudget(final boolean isActiveBudget) {
		this.isActiveBudget = isActiveBudget;
	}

	/**
	 * @return isPrimaryBudget
	 */
	public boolean getIsPrimaryBudget() {
		return isPrimaryBudget;
	}

	/**
	 * @param isPrimaryBudget the isPrimaryBudget to set
	 */
	public void setIsPrimaryBudget(final boolean isPrimaryBudget) {
		this.isPrimaryBudget = isPrimaryBudget;
	}

	

	@Override
	public String getStateDetails() {
		return name;
	}

	/**
	 * @return the materialized_path
	 */
	public String getMaterializedPath()
	{
		return materializedPath;
	}
	


	/**
	 * @param materialized_path the materialized_path to set
	 */
	public void setMaterializedPath(final String materializedPath)
	{
		this.materializedPath = materializedPath;
	}

	public void setReferenceBudget(Budget reference) {
		this.referenceBudget = reference;
	}

	public Budget getReferenceBudget() {
		return referenceBudget;
	}

	public void setDocumentNumber(Long documentNumber) {
		this.documentNumber = documentNumber;
	}

	public Long getDocumentNumber() {
		return documentNumber;
	}

}
