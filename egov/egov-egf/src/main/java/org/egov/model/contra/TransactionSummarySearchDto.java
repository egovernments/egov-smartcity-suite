package org.egov.model.contra;

import java.util.List;

import org.egov.commons.Accountdetailtype;

public class TransactionSummarySearchDto {
	
	private TransactionSummary transactionSummary;
	
	private List<Accountdetailtype> accountdetailtypes;
	
	private String entityCode;
	
	public TransactionSummarySearchDto() {
	}

	public TransactionSummary getTransactionSummary() {
		return transactionSummary;
	}

	public void setTransactionSummary(TransactionSummary transactionSummary) {
		this.transactionSummary = transactionSummary;
	}

	public List<Accountdetailtype> getAccountdetailtypes() {
		return accountdetailtypes;
	}

	public void setAccountdetailtypes(List<Accountdetailtype> accountdetailtypes) {
		this.accountdetailtypes = accountdetailtypes;
	}

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}
}
