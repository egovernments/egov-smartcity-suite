/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.models.estimate;

public class EstimateAbstractReport {

	private String typeOfWork;
	private String subTypeOfWork;
	private String department;
	private Integer created;
	private Integer techSanctioned;
	private Integer budgetAppDone;
	private Integer adminSanctioned;
	private Integer rejected;
	private Integer cancelled;
	private Integer totalCreated;
	private Integer totalTechSan;
	private Integer totalBudgetAppDone;
	private Integer totalAdminSan;
	private Integer totalReject;
	private Integer totalCancel;

	public String getTypeOfWork() {
		return typeOfWork;
	}
	public void setTypeOfWork(String typeOfWork) {
		this.typeOfWork = typeOfWork;
	}
	public String getSubTypeOfWork() {
		return subTypeOfWork;
	}
	public void setSubTypeOfWork(String subTypeOfWork) {
		this.subTypeOfWork = subTypeOfWork;
	}
	
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public Integer getCreated() {
		return created;
	}
	public void setCreated(Integer created) {
		this.created = created;
	}
	public Integer getTechSanctioned() {
		return techSanctioned;
	}
	public void setTechSanctioned(Integer techSanctioned) {
		this.techSanctioned = techSanctioned;
	}
	public Integer getBudgetAppDone() {
		return budgetAppDone;
	}
	public void setBudgetAppDone(Integer budgetAppDone) {
		this.budgetAppDone = budgetAppDone;
	}
	public Integer getAdminSanctioned() {
		return adminSanctioned;
	}
	public void setAdminSanctioned(Integer adminSanctioned) {
		this.adminSanctioned = adminSanctioned;
	}
	public Integer getRejected() {
		return rejected;
	}
	public void setRejected(Integer rejected) {
		this.rejected = rejected;
	}
	public Integer getCancelled() {
		return cancelled;
	}
	public void setCancelled(Integer cancelled) {
		this.cancelled = cancelled;
	}
	public Integer getTotalCreated() {
		return totalCreated;
	}
	public void setTotalCreated(Integer totalCreated) {
		this.totalCreated = totalCreated;
	}
	public Integer getTotalTechSan() {
		return totalTechSan;
	}
	public void setTotalTechSan(Integer totalTechSan) {
		this.totalTechSan = totalTechSan;
	}
	public Integer getTotalBudgetAppDone() {
		return totalBudgetAppDone;
	}
	public void setTotalBudgetAppDone(Integer totalBudgetAppDone) {
		this.totalBudgetAppDone = totalBudgetAppDone;
	}
	public Integer getTotalAdminSan() {
		return totalAdminSan;
	}
	public void setTotalAdminSan(Integer totalAdminSan) {
		this.totalAdminSan = totalAdminSan;
	}
	public Integer getTotalReject() {
		return totalReject;
	}
	public void setTotalReject(Integer totalReject) {
		this.totalReject = totalReject;
	}
	public Integer getTotalCancel() {
		return totalCancel;
	}
	public void setTotalCancel(Integer totalCancel) {
		this.totalCancel = totalCancel;
	}

}
