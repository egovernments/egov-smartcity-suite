package org.egov.assets.model;

import java.util.List;

public class VoucherInput {
	
	private List<AccountInfo> creaditAccounts;
	private List<AccountInfo> debitAccounts;
	private HeaderInfo headerInfo;
	private SubledgerInfo subledgerInfo;
	private String narration;
	public List<AccountInfo> getCreaditAccounts() {
		return creaditAccounts;
	}
	public void setCreaditAccounts(List<AccountInfo> creaditAccounts) {
		this.creaditAccounts = creaditAccounts;
	}
	public List<AccountInfo> getDebitAccounts() {
		return debitAccounts;
	}
	public void setDebitAccounts(List<AccountInfo> debitAccounts) {
		this.debitAccounts = debitAccounts;
	}
	public HeaderInfo getHeaderInfo() {
		return headerInfo;
	}
	public void setHeaderInfo(HeaderInfo headerInfo) {
		this.headerInfo = headerInfo;
	}
	public SubledgerInfo getSubledgerInfo() {
		return subledgerInfo;
	}
	public void setSubledgerInfo(SubledgerInfo subledgerInfo) {
		this.subledgerInfo = subledgerInfo;
	}
	public String getNarration() {
		return narration;
	}
	public void setNarration(String narration) {
		this.narration = narration;
	}

}
