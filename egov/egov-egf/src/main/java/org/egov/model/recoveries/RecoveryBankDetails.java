package org.egov.model.recoveries;


import org.egov.commons.Fund;
import org.egov.commons.Bank;
import org.egov.commons.Bankbranch;
import org.egov.commons.Bankaccount;

public class RecoveryBankDetails implements java.io.Serializable
{
	private Long id;
	private Recovery tdsId;
	private Fund fundId;
	private Bank bankId;
	private Bankbranch branchId;
	private Bankaccount bankaccountId;
		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Recovery getTdsId() {
		return tdsId;
	}
	
	public void setTdsId(Recovery tdsId) {
		this.tdsId = tdsId;
	}
	
	public Fund getFundId() {
		return fundId;
	}
	public void setFundId(Fund fundId) {
		this.fundId = fundId;
	}
	
	public Bank getBankId() {
		return bankId;
	}
	public void setBankId(Bank bankId) {
		this.bankId = bankId;
	}

	public void setBranchId(Bankbranch branchId) {
		this.branchId = branchId;
	}
	
	public Bankbranch getBranchId() {
		return branchId;
	}

	public Bankaccount getBankaccountId() {
		return bankaccountId;
	}
	public void setBankaccountId(Bankaccount bankaccountId) {
		this.bankaccountId = bankaccountId;
	}
	
}


