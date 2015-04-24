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
package org.egov.collection.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.infstr.models.BaseModel;

/**
 * CollectionStgInstrument entity. @author MyEclipse Persistence Tools
 */

public class CollectionStgInstrument extends BaseModel {
	private static final long serialVersionUID = 1L;
	private CollectionStgReceipt collectionStgReceipt;
	private String collMode;
	private BigDecimal amount;
	private String instrNo;
	private Date instrDate;
	private String bank;
	private String branch;
	private Character status;
	private Date bounceDate;
	private String bankAccount;

	public CollectionStgReceipt getCollectionStgReceipt() {
		return this.collectionStgReceipt;
	}

	public void setCollectionStgReceipt(CollectionStgReceipt collectionStgReceipt) {
		this.collectionStgReceipt = collectionStgReceipt;
	}

	public String getCollMode() {
		return this.collMode;
	}

	public void setCollMode(String collMode) {
		this.collMode = collMode;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getInstrNo() {
		return this.instrNo;
	}

	public void setInstrNo(String instrNo) {
		this.instrNo = instrNo;
	}

	public Date getInstrDate() {
		return this.instrDate;
	}

	public void setInstrDate(Date instrDate) {
		this.instrDate = instrDate;
	}

	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBranch() {
		return this.branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	/**
	 * @return the status
	 */
	public Character getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Character status) {
		this.status = status;
	}

	/**
	 * @return the bounceDate
	 */
	public Date getBounceDate() {
		return bounceDate;
	}

	/**
	 * @param bounceDate the bounceDate to set
	 */
	public void setBounceDate(Date bounceDate) {
		this.bounceDate = bounceDate;
	}

	/**
	 * @return the bankAccount
	 */
	public String getBankAccount() {
		return bankAccount;
	}

	/**
	 * @param bankAccount the bankAccount to set
	 */
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	


}