/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
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
