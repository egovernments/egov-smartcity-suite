package org.egov.payroll.services.payslipApprove;

import org.egov.commons.CChartOfAccounts;

public class SalaryBillAccountAgtgregate {

	private String type;
	private CChartOfAccounts chartOfAccount;
	public CChartOfAccounts getChartOfAccount() {
		return chartOfAccount;
	}
	public void setChartOfAccount(CChartOfAccounts chartOfAccount) {
		this.chartOfAccount = chartOfAccount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public boolean equals(Object o) {
		if ((o instanceof SalaryBillAccountAgtgregate) && (((SalaryBillAccountAgtgregate)o).getType().equals(this.getType()))
				&& ((SalaryBillAccountAgtgregate)o).getChartOfAccount() == this.getChartOfAccount()) {
		return true;
		} else {
		return false;
		}
	}
	
	public int hashCode() {
		return (Integer.valueOf(type) + Integer.valueOf(chartOfAccount.getGlcode())); 
		}
	
}
