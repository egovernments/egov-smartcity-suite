package org.egov.web.actions.payment;

import org.egov.commons.Bankaccount;
import org.egov.commons.CChartOfAccounts;
import org.egov.infra.admin.master.entity.Department;
import org.egov.pims.commons.DrawingOfficer;

public class BankAccountRemittanceCOA{
   
	private Bankaccount bankAccount;
	private CChartOfAccounts remittanceCOA;
	public Bankaccount getBankAccount() {
		return bankAccount;  
	}
	public void setBankAccount(Bankaccount bankAccount) {
		this.bankAccount = bankAccount;
	}
	public CChartOfAccounts getRemittanceCOA() {
		return remittanceCOA;
	}
	public void setRemittanceCOA(CChartOfAccounts remittanceCOA) {
		this.remittanceCOA = remittanceCOA;
	}
	
}
