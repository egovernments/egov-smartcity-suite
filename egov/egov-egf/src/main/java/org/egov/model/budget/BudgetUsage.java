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
