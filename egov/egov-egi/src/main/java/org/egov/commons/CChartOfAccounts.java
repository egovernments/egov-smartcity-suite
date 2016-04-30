/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.commons;

import org.egov.infstr.models.BaseModel;

import java.util.HashSet;
import java.util.Set;

public class CChartOfAccounts extends BaseModel {
	private static final long serialVersionUID = 1L;

	//private Long id = null;
	private String glcode;
	private String name;
	private Long purposeId;
	private String desc;
	private Boolean isActiveForPosting;
	private Long parentId;
	private Long schedule;
	private Character operation;
	private Character type;
	private Long classification;
	private Boolean functionReqd;
	private Boolean budgetCheckReq;
	private String majorCode;
	private Long myClass;
	private Set<CChartOfAccountDetail> chartOfAccountDetails = new HashSet<CChartOfAccountDetail>();

	public Set<CChartOfAccountDetail> getChartOfAccountDetails() {
		return chartOfAccountDetails;
	}

	public void setChartOfAccountDetails(Set<CChartOfAccountDetail> chartOfAccountDetail) {
		this.chartOfAccountDetails = chartOfAccountDetail;
	}

	public String getMajorCode() {
		return majorCode;
	}

	public void setMajorCode(String majorCode) {
		this.majorCode = majorCode;
	}

	/**
	 * @return Returns the myClass.
	 */
	public Long getMyClass() {
		return myClass;
	}

	/**
	 * @param myClass The myClass to set.
	 */
	public void setMyClass(Long myClass) {
		this.myClass = myClass;
	}

	/**
	 * @return Returns the glcode.
	 */
	public String getGlcode() {
		return glcode;
	}

	/**
	 * @param glcode The glcode to set.
	 */
	public void setGlcode(String glcode) {
		this.glcode = glcode;
	}

	/**
	 * @return Returns the id.
	 
	public Long getId() {
		return id;
	}
*/
	/**
	 * @param id The id to set.
	 
	public void setId(Long id) {
		this.id = id;
	}*/

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the purposeId.
	 */
	public Long getPurposeId() {
		return purposeId;
	}

	/**
	 * @param purposeId The purposeId to set.
	 */
	public void setPurposeId(Long purposeId) {
		this.purposeId = purposeId;
	}

	/**
	 * @return Returns the classification.
	 */
	public Long getClassification() {
		return classification;
	}

	/**
	 * @param classification The classification to set.
	 */
	public void setClassification(Long classification) {
		this.classification = classification;
	}

	/**
	 * @return Returns the desc.
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc The desc to set.
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return Returns the functionReqd.
	 */
	public Boolean getFunctionReqd() {

		return functionReqd;

	}

	/**
	 * @param functionReqd The functionReqd to set.
	*/
	public void setFunctionReqd(Boolean functionReqd) {
		this.functionReqd = functionReqd;
	}

	/**public void setFunctionReqd(boolean functionReqd) {
		if (functionReqd == true)
			this.functionReqd = Long.valueOf("1");
		else
			this.functionReqd = Long.valueOf("0");
	} */

	/**
	 * @return Returns the isActiveForPosting.
	 */
	public Boolean getIsActiveForPosting() {
		return isActiveForPosting;
	}

	/**
	 * @param isActiveForPosting The isActiveForPosting to set.
	 */
	public void setIsActiveForPosting(Boolean isActiveForPosting) {
		this.isActiveForPosting = isActiveForPosting;
	}

	/*public void setIsActiveForPosting(boolean isActiveForPosting) {
		if (isActiveForPosting == true)
			this.isActiveForPosting = Long.valueOf("1");
		else
			this.isActiveForPosting = Long.valueOf("0");
	}*/

	/**
	 * @return Returns the operation.
	 */
	public Character getOperation() {
		return operation;
	}

	/**
	 * @param operation The operation to set.
	 */
	public void setOperation(Character operation) {
		this.operation = operation;
	}

	/**
	 * @return Returns the parentId.
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId The parentId to set.
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return Returns the schedule.
	 */
	public Long getSchedule() {
		return schedule;
	}

	/**
	 * @param schedule The schedule to set.
	 */
	public void setSchedule(Long schedule) {
		this.schedule = schedule;
	}

	/**
	 * @return Returns the type.
	 */
	public Character getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(Character type) {
		this.type = type;
	}

	public boolean equals(Object o) {
		if ((o instanceof CChartOfAccounts) && (((CChartOfAccounts) o).getId().equals(this.getId()))) {
			return true;
		} else {
			return false;
		}
	}

	public int hashCode() {
		return (Integer.valueOf(glcode!=null?glcode:"0"));
	}

	public Boolean getBudgetCheckReq() {
		return budgetCheckReq;
	}

	public void setBudgetCheckReq(Boolean budgetCheckReq) {
		this.budgetCheckReq = budgetCheckReq;
	}

/*	public void setBudgetCheckReq(boolean budgetCheckReq) {
		if (budgetCheckReq == true)
			this.budgetCheckReq = Long.valueOf("1");
		else
			this.budgetCheckReq = Long.valueOf("0");
	}*/
}
