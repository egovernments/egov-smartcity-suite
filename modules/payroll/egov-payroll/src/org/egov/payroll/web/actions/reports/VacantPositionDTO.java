package org.egov.payroll.web.actions.reports;

import java.math.BigDecimal;

public class VacantPositionDTO {
	private String designationName;
	private BigDecimal  sanctionPosts;
	private BigDecimal  workingPosts;
	private BigDecimal  vacantPosts;   
	public String getDesignationName() {
		return designationName;
	}
	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}
	public BigDecimal getSanctionPosts() {
		return sanctionPosts;
	}
	public void setSanctionPosts(BigDecimal sanctionPosts) {
		this.sanctionPosts = sanctionPosts;
	}
	public BigDecimal getWorkingPosts() {
		return workingPosts;
	}
	public void setWorkingPosts(BigDecimal workingPosts) {
		this.workingPosts = workingPosts;
	}
	public BigDecimal getVacantPosts() {
		return vacantPosts;
	}
	public void setVacantPosts(BigDecimal vacantPosts) {
		this.vacantPosts = vacantPosts;
	}

}
