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
