package org.egov.model.bills;

import org.egov.commons.CChartOfAccounts;
import org.egov.infstr.models.BaseModel;

public class EgSalaryCodes extends BaseModel{
	private String head;
	private CChartOfAccounts chartOfAccount;
	private String salType;

	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public CChartOfAccounts getChartOfAccount() {
		return chartOfAccount;
	}
	public void setChartOfAccount(CChartOfAccounts chartOfAccounts) {
		this.chartOfAccount = chartOfAccounts;
	}
	public String getSalType() {
		return salType;
	}
	public void setSalType(String salType) {
		this.salType = salType;
	}
}
