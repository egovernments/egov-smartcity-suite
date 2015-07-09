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
package com.exilant.eGov.src.reports;

public class DepositeRegisterReportBean {
	
	public String sno;
	public String voucherDate;
	public String PayeeName;
	public String mode;
	public String reciptnumber;
	public String amount;
	public String voucherNumber;
	public String income;
	public String year;
	public String bdeposite;
	public String chequeAmount;
	
	
	public DepositeRegisterReportBean()
	{
		this.sno="";
		this.reciptnumber="";
		
	    this.voucherNumber="";
	    this.PayeeName="";
	    this.voucherDate="";
	    
	    this. income="";
	    this.year="";
	    this.amount="";
	    this.bdeposite="";
	   this.chequeAmount="";
	    	
	}


	public String getPayeeName() {
		return PayeeName;
	}


	public void setPayeeName(String payeeName) {
		PayeeName = payeeName;
	}


	public String getReciptnumber() {
		return reciptnumber;
	}


	public void setReciptnumber(String reciptnumber) {
		this.reciptnumber = reciptnumber;
	}


	public String getSno() {
		return sno;
	}


	public void setSno(String sno) {
		this.sno = sno;
	}


	

	public String getVoucherDate() {
		return voucherDate;
	}


	public void setVoucherDate(String voucherDate) {
		this.voucherDate = voucherDate;
	}


	public String getVoucherNumber() {
		return voucherNumber;
	}


	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}


	public String getMode() {
		return mode;
	}


	public void setMode(String mode) {
		this.mode = mode;
	}


	public String getAmount() {
		return amount;
	}


	public void setAmount(String amount) {
		this.amount = amount;
	}


	public String getBdeposite() {
		return bdeposite;
	}


	public void setBdeposite(String bdeposite) {
		this.bdeposite = bdeposite;
	}


	public String getIncome() {
		return income;
	}


	public void setIncome(String income) {
		this.income = income;
	}


	


	public String getChequeAmount() {
		return chequeAmount;
	}


	public void setChequeAmount(String chequeAmount) {
		this.chequeAmount = chequeAmount;
	}


	public String getYear() {
		return year;
	}


	public void setYear(String year) {
		this.year = year;
	}
}
