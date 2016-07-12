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
package org.egov.lcms.transactions.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LegalCaseReportResult {
	private String caseNumber;
	private String lcNumber;
	private Date caseFromDate;
	private Date caseToDate;
	private String standingCouncil;
	private String casecategory;
	private String courttype;
	private String petitionType;
	private String courtType;
	private String courtName;
	private String govtDept;
	private String caseTitle;
	private String petName;
	private Boolean isStatusExcluded;
	private String caseStatus;
	private List<String>actionList=new ArrayList<String>();
private String assignDept;
	
	public String getStandingCouncil() {
		return standingCouncil;
	}

	public void setStandingCouncil(String standingCouncil) {
		this.standingCouncil = standingCouncil;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	public String getLcNumber() {
		return lcNumber;
	}

	public void setLcNumber(String lcNumber) {
		this.lcNumber = lcNumber;
	}

	public Date getCaseFromDate() {
		return caseFromDate;
	}

	public void setCaseFromDate(Date caseFromDate) {
		this.caseFromDate = caseFromDate;
	}

	public Date getCaseToDate() {
		return caseToDate;
	}

	public void setCaseToDate(Date caseToDate) {
		this.caseToDate = caseToDate;
	}

	public String getCasecategory() {
		return casecategory;
	}

	public void setCasecategory(String casecategory) {
		this.casecategory = casecategory;
	}

	public String getCourttype() {
		return courttype;
	}

	public void setCourttype(String courttype) {
		this.courttype = courttype;
	}

	public String getPetitionType() {
		return petitionType;
	}

	public void setPetitionType(String petitionType) {
		this.petitionType = petitionType;
	}

	public String getCourtType() {
		return courtType;
	}

	public void setCourtType(String courtType) {
		this.courtType = courtType;
	}

	public String getCourtName() {
		return courtName;
	}

	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}

	public String getGovtDept() {
		return govtDept;
	}

	public void setGovtDept(String govtDept) {
		this.govtDept = govtDept;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public List<String> getActionList() {
		return actionList;
	}

	public void setActionList(List<String> actionList) {
		this.actionList = actionList;
	}

	public String getCaseTitle() {
		return caseTitle;
	}

	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}

	public String getAssignDept() {
		return assignDept;
	}

	public void setAssignDept(String assignDept) {
		this.assignDept = assignDept;
	}

	public Boolean getIsStatusExcluded() {
		return isStatusExcluded;
	}

	public void setIsStatusExcluded(Boolean isStatusExcluded) {
		this.isStatusExcluded = isStatusExcluded;
	}

	
}
