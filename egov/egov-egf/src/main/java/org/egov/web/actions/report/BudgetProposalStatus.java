package org.egov.web.actions.report;

import org.egov.commons.CFunction;
import org.egov.infra.admin.master.entity.Department;

public class BudgetProposalStatus {
	
	private Department department;
	private CFunction function;
	private String hod="";
	private String asstBud="";
	private String smBud="";
	private String aoBud="";
	private String caoBud="";
	private String asstAdmin="";
	private String smAdmin="";
	
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public CFunction getFunction() {
		return function;
	}
	public void setFunction(CFunction function) {
		this.function = function;
	}
	public String getHod() {
		return hod;
	}
	public void setHod(String hod) {
		this.hod = hod;
	}
	public String getAsstBud() {
		return asstBud;
	}
	public void setAsstBud(String asstBud) {
		this.asstBud = asstBud;
	}
	public String getSmBud() {
		return smBud;
	}
	public void setSmBud(String smBud) {
		this.smBud = smBud;
	}
	public String getAoBud() {
		return aoBud;
	}
	public void setAoBud(String aoBud) {
		this.aoBud = aoBud;
	}
	public String getCaoBud() {
		return caoBud;
	}
	public void setCaoBud(String caoBud) {
		this.caoBud = caoBud;
	}
	public String getAsstAdmin() {
		return asstAdmin;
	}
	public void setAsstAdmin(String asstAdmin) {
		this.asstAdmin = asstAdmin;
	}
	public String getSmAdmin() {
		return smAdmin;
	}
	public void setSmAdmin(String smAdmin) {
		this.smAdmin = smAdmin;
	}

}
