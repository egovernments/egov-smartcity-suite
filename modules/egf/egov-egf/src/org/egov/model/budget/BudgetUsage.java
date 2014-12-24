package org.egov.model.budget;

import java.sql.Timestamp;

/**
 * @author eGov
 * Model class for BudgetUsage
 */
public class BudgetUsage {
	public BudgetUsage()
	{
		super();
	}
	private Long id;
	private Integer financialYearId;
	private Integer moduleId;
	private String referenceNumber;
	private Double consumedAmount;
	private Double releasedAmount;
	private Timestamp updatedTime;
	private Integer createdby;
	private BudgetDetail budgetDetail;
	private String appropriationnumber;
	
	/**
	 * @return id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return
	 */
	public Integer getFinancialYearId() {
		return financialYearId;
	}
	/**
	 * @param financialYearid
	 */
	public void setFinancialYearId(Integer financialYearId) {
		this.financialYearId = financialYearId;
	}
	/**
	 * @return
	 */
	public Integer getModuleId() {
		return moduleId;
	}
	/**
	 * @param moduleId
	 */
	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}
	/**
	 * @return
	 */
	public String getReferenceNumber() {
		return referenceNumber;
	}
	/**
	 * @param referenceNumber
	 */
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	/**
	 * @return
	 */
	public Double getConsumedAmount() {
		return consumedAmount;
	}
	/**
	 * @param consumedAmount
	 */
	public void setConsumedAmount(Double consumedAmount) {
		this.consumedAmount = consumedAmount;
	}
	/**
	 * @return
	 */
	public Double getReleasedAmount() {
		return releasedAmount;
	}
	/**
	 * @param releasedAmount
	 */
	public void setReleasedAmount(Double releasedAmount) {
		this.releasedAmount = releasedAmount;
	}
	/**
	 * @return
	 */
	public Timestamp getUpdatedTime() {
		return updatedTime;
	}
	/**
	 * @param updatedTime
	 */
	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}
	/**
	 * @return
	 */
	public Integer getCreatedby() {
		return createdby;
	}
	/**
	 * @param createdby
	 */
	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}
	/**
	 * @return budgetDetail
	 */
	public BudgetDetail getBudgetDetail() {
		return budgetDetail;
	}
	/**
	 * @param budgetDetail the budgetDetail to set
	 */
	public void setBudgetDetail(BudgetDetail budgetDetail) {
		this.budgetDetail = budgetDetail;
	}
	public String getAppropriationnumber() {
		return appropriationnumber;
	}
	public void setAppropriationnumber(String appropriationnumber) {
		this.appropriationnumber = appropriationnumber;
	}
	
	
}
