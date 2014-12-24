package org.egov.payroll.client.payhead;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.egov.payroll.model.PayheadRule;
import org.egov.payroll.model.SalaryCodes;

public class PayheadRuleForm extends ActionForm{
	
	private String salarycode;
	
	private String[] description;
	private String[] effectiveFrom;
	private String[] payheadRuleScript;
	
	private String payheadRuleId;
	private String[] payRuleId;
	
	private List<SalaryCodes> salarycodeList;
	private PayheadRule payheadRule;
	private List<PayheadRule> ruleList;
	public List<PayheadRule> getRuleList() {
		return ruleList;
	}
	public void setRuleList(List<PayheadRule> ruleList) {
		this.ruleList = ruleList;
	}
	public PayheadRule getPayheadRule() {
		return payheadRule;
	}
	public void setPayheadRule(PayheadRule payheadRule) {
		this.payheadRule = payheadRule;
	}	
	public String getSalarycode() {
		return salarycode;
	}
	public void setSalarycode(String salarycode) {
		this.salarycode = salarycode;
	}
	public String[] getDescription() {
		return description;
	}
	public void setDescription(String[] description) {
		this.description = description;
	}
	public String[] getEffectiveFrom() {
		return effectiveFrom;
	}
	public void setEffectiveFrom(String[] effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}
	public String[] getPayheadRuleScript() {
		return payheadRuleScript;
	}
	public void setPayheadRuleScript(String[] payheadRuleScript) {
		this.payheadRuleScript = payheadRuleScript;
	}
	
	public String getPayheadRuleId() {
		return payheadRuleId;
	}
	public void setPayheadRuleId(String payheadRuleId) {
		this.payheadRuleId = payheadRuleId;
	}
	public String[] getPayRuleId() {
		return payRuleId;
	}
	public void setPayRuleId(String[] payRuleId) {
		this.payRuleId = payRuleId;
	}
	public List<SalaryCodes> getSalarycodeList() {
		return salarycodeList;
	}
	public void setSalarycodeList(List<SalaryCodes> salarycodeList) {
		this.salarycodeList = salarycodeList;
	}
	
	
}
