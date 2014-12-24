/*
 * @(#)MasterDataForm.java 3.0, 17 Jun, 2013 11:39:30 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.config.client;

import java.util.List;
import java.util.Set;

import org.apache.struts.action.ActionForm;
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;

public class MasterDataForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	private String[] keyName;
	private String[] description;
	private String[] values;
	private String[] bankEffectiveFrom;
	private String[] effectiveFrom;
	private String[] moduleName;
	private String[] bank;
	private String[] bankBranch;
	private String[] bankAccount;
	private List<Bankaccount> bankAccountList;
	private List<Bank> bankList;
	private List<Bankbranch> bankBranchList;
	private String[] count;
	private String[] keyId;
	private Set<String> moduleSet;

	public List<Bankbranch> getBankBranchList() {
		return this.bankBranchList;
	}

	public void setBankBranchList(final List<Bankbranch> bankBranchList) {
		this.bankBranchList = bankBranchList;
	}

	public List<Bank> getBankList() {
		return this.bankList;
	}

	public void setBankList(final List<Bank> bankList) {
		this.bankList = bankList;
	}

	public List<Bankaccount> getBankAccountList() {
		return this.bankAccountList;
	}

	public void setBankAccountList(final List<Bankaccount> bankAccountList) {
		this.bankAccountList = bankAccountList;
	}

	public String[] getDescription() {
		return this.description;
	}

	public void setDescription(final String[] description) {
		this.description = description;
	}

	public String[] getEffectiveFrom() {
		return this.effectiveFrom;
	}

	public void setEffectiveFrom(final String[] effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public String[] getKeyName() {
		return this.keyName;
	}

	public void setKeyName(final String[] keyName) {
		this.keyName = keyName;
	}

	public String[] getModuleName() {
		return this.moduleName;
	}

	public void setModuleName(final String[] moduleName) {
		this.moduleName = moduleName;
	}

	public String[] getValues() {
		return this.values;
	}

	public void setValues(final String[] values) {
		this.values = values;
	}

	public String[] getBank() {
		return this.bank;
	}

	public void setBank(final String[] bank) {
		this.bank = bank;
	}

	public String[] getBankAccount() {
		return this.bankAccount;
	}

	public void setBankAccount(final String[] bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String[] getBankBranch() {
		return this.bankBranch;
	}

	public void setBankBranch(final String[] bankBranch) {
		this.bankBranch = bankBranch;
	}

	public String[] getKeyId() {
		return this.keyId;
	}

	public void setKeyId(final String[] keyId) {
		this.keyId = keyId;
	}

	public String[] getCount() {
		return this.count;
	}

	public void setCount(final String[] count) {
		this.count = count;
	}

	public String[] getBankEffectiveFrom() {
		return this.bankEffectiveFrom;
	}

	public void setBankEffectiveFrom(final String[] bankEffectiveFrom) {
		this.bankEffectiveFrom = bankEffectiveFrom;
	}

	public Set<String> getModuleSet() {
		return this.moduleSet;
	}

	public void setModuleSet(final Set<String> moduleSet) {
		this.moduleSet = moduleSet;
	}

}
