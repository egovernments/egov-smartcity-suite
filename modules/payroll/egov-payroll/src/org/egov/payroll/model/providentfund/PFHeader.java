package org.egov.payroll.model.providentfund;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.infstr.workflow.Action;
import org.egov.model.recoveries.Recovery;

public class PFHeader implements java.io.Serializable
{
	private Long id = null;
	private CChartOfAccounts pfAccount = null;
	private CChartOfAccounts pfIntExpAccount = null;
	private String frequency = null;
	private Recovery tds=null;
	private Action ruleScript;
	private String pfType=null;
	private Integer month=null;
	private CFinancialYear financialYear =null;
	
	public CChartOfAccounts getPfAccount() {
		return pfAccount;
	}
	public void setPfAccount(CChartOfAccounts pfAccount) {
		this.pfAccount = pfAccount;
	}
	
	public PFHeader()
	{
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public Recovery getTds() {
		return tds;
	}
	public void setTds(Recovery tds) {
		this.tds = tds;
	}
	public Action getRuleScript() {
		return ruleScript;
	}
	public void setRuleScript(Action ruleScript) {
		this.ruleScript = ruleScript;
	}
	public String getPfType() {
		return pfType;
	}
	public void setPfType(String pfType) {
		this.pfType = pfType;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public CFinancialYear getFinancialYear() {
		return financialYear;
	}
	public void setFinancialYear(CFinancialYear financialYear) {
		this.financialYear = financialYear;
	}
	public CChartOfAccounts getPfIntExpAccount() {
		return pfIntExpAccount;
	}
	public void setPfIntExpAccount(CChartOfAccounts pfIntExpAccount) {
		this.pfIntExpAccount = pfIntExpAccount;
	}
}
