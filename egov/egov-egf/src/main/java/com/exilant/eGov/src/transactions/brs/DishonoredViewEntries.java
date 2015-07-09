/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
/*
 * DishonoredViewEntries.java  Created on May 31, 2008
 *
 *  Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.exilant.eGov.src.transactions.brs;

/**
 * @author Iliyaraja S
 *
 * @Version 1.00
 */
public class DishonoredViewEntries
{
	//For voucher header details
	private String vouHName;
	private String voucherNumber;
	private String vouDate;
	private String fund;
	private String refNo;// chqno
	private String refDate;//chqdate
	private String reason;

	//For Account code details
	private String reversalAccCode;
	private String reversalDescn;
	private String reversalDebitAmount;
	private String reversalCreditAmount;


	public String getFund() {
		return fund;
	}
	public void setFund(String fund) {
		this.fund = fund;
	}

	public String getRefDate() {
		return refDate;
	}
	public void setRefDate(String refDate) {
		this.refDate = refDate;
	}
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public String getVoucherNumber() {
		return voucherNumber;
	}
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	public String getVouDate() {
		return vouDate;
	}
	public void setVouDate(String vouDate) {
		this.vouDate = vouDate;
	}
	public String getReversalAccCode() {
		return reversalAccCode;
	}
	public void setReversalAccCode(String reversalAccCode) {
		this.reversalAccCode = reversalAccCode;
	}
	public String getReversalCreditAmount() {
		return reversalCreditAmount;
	}
	public void setReversalCreditAmount(String reversalCreditAmount) {
		this.reversalCreditAmount = reversalCreditAmount;
	}
	public String getReversalDebitAmount() {
		return reversalDebitAmount;
	}
	public void setReversalDebitAmount(String reversalDebitAmount) {
		this.reversalDebitAmount = reversalDebitAmount;
	}
	public String getReversalDescn() {
		return reversalDescn;
	}
	public void setReversalDescn(String reversalDescn) {
		this.reversalDescn = reversalDescn;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getVouHName() {
		return vouHName;
	}
	public void setVouHName(String vouHName) {
		this.vouHName = vouHName;
	}


}
