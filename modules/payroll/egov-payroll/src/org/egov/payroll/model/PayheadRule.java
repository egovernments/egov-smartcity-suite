package org.egov.payroll.model;

import java.util.Date;

import org.egov.infstr.workflow.Action;

public class PayheadRule implements java.io.Serializable{
	
	private Integer id;
	
	private SalaryCodes salarycode;
	
	private Date effectiveFrom;
	
	private String description;
	
	private Action ruleScript;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SalaryCodes getSalarycode() {
		return salarycode;
	}

	public void setSalarycode(SalaryCodes salarycode) {
		this.salarycode = salarycode;
	}

	public Date getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Action getRuleScript() {
		return ruleScript;
	}

	public void setRuleScript(Action ruleScript) {
		this.ruleScript = ruleScript;
	}

}
