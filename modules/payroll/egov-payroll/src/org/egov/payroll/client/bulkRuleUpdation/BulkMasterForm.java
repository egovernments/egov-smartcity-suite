package org.egov.payroll.client.bulkRuleUpdation;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.egov.payroll.model.PayGenUpdationRule;

public class BulkMasterForm extends ActionForm 
{
	String ruleId="";
	String categoryType="";
	String payHead="";
	String calType="";
	String percentage="";
	String amount="";
	String monthId="";
	String finYear="";
	String empGrpMstr="";
	
	public String getEmpGrpMstr() {
		return empGrpMstr;
	}
	public void setEmpGrpMstr(String empGrpMstr) {
		this.empGrpMstr = empGrpMstr;
	}
	public String getMonthId() {
		return monthId;
	}
	public void setRuleUpdation(PayGenUpdationRule ruleUpdation) {
		//FIXME: from the ruleUpdation object get values and set to each property.
		//Use BeanUtils to do this
		
		this.ruleId=ruleUpdation.getId().toString();
		this.categoryType=ruleUpdation.getSalaryCodes().getCategoryMaster().getCatType();
		this.payHead=ruleUpdation.getSalaryCodes().getId().toString();
		this.calType=ruleUpdation.getSalaryCodes().getCalType();
		if(ruleUpdation.getPercentage()!=null)
			{
			this.percentage=ruleUpdation.getPercentage().toString();
			}
		if(ruleUpdation.getMonthlyAmt()!=null)
			{
			this.amount=ruleUpdation.getMonthlyAmt().toString();
			}
		this.monthId=ruleUpdation.getMonth().toString();
		this.finYear=ruleUpdation.getFinancialyear().getId().toString();
		this.empGrpMstr=ruleUpdation.getEmpGroupMstrs()==null?"":ruleUpdation.getEmpGroupMstrs().getId().toString();
		
	}

	public void setMonthId(String monthId) {
		this.monthId = monthId;
	}

	public String getFinYear() {
		return finYear;
	}

	public void setFinYear(String finYear) {
		this.finYear = finYear;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCalType() {
		return calType;
	}

	public void setCalType(String calType) {
		this.calType = calType;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getPayHead() {
		return payHead;
	}

	public void setPayHead(String payHead) {
		this.payHead = payHead;
	}
	public String getRuleId() {
		return ruleId;
	}
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	
}
