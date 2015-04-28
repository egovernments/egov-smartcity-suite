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
 * Created on Jan 21,2008
 * @author Nandini
 */
package com.exilant.eGov.src.reports;
public class BankTransactionReportBean
{

		String vhDate=""; 
		String paymentAmt="";
		String receiptAmt="";
		String chqDate="";
		String amount="";
		String vhNo="";
		String chqNo="";
		String bankAccount_id="";
		String startDate="";
		String endDate="";
		String type="";
		String cgn="";
		String serialNo="";
		String transactioType="";
		String payinslipNo ="";
		String payinslipDate = "";
		public String getCgn() {
			return cgn;
		}
		public void setCgn(String cgn) {
			this.cgn = cgn;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getAmount() {
			return amount;
		}
		public void setAmount(String amount) {
			this.amount = amount;
		}
		public String getChqDate() {
			return chqDate;
		}
		public void setChqDate(String chqDate) {
			this.chqDate = chqDate;
		}
		public String getChqNo() {
			return chqNo;
		}
		public void setChqNo(String chqNo) {
			this.chqNo = chqNo;
		}
		
		public String getVhDate() {
			return vhDate;
		}
		public void setVhDate(String vhDate) {
			this.vhDate = vhDate;
		}
		public String getVhNo() {
			return vhNo;
		}
		public void setVhNo(String vhNo) {
			this.vhNo = vhNo;
		}
		public String getBankAccount_id() {
			return bankAccount_id;
		}
		public void setBankAccount_id(String bankAccount_id) {
			this.bankAccount_id = bankAccount_id;
		}
		public String getEndDate() {
			return endDate;
		}
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
		public String getStartDate() {
			return startDate;
		}
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}
		public String getSerialNo() {
			return serialNo;
		}
		public void setSerialNo(String serialNo) {
			this.serialNo = serialNo;
		}
		public String getPaymentAmt() {
			return paymentAmt;
		}
		public void setPaymentAmt(String paymentAmt) {
			this.paymentAmt = paymentAmt;
		}
		public String getReceiptAmt() {
			return receiptAmt;
		}
		public void setReceiptAmt(String receiptAmt) {
			this.receiptAmt = receiptAmt;
		}
		
		/**
		 * @return Returns the transactioType.
		 */
		public String getTransactioType() {
			return transactioType;
		}
		/**
		 * @param transactioType The transactioType to set.
		 */
		public void setTransactioType(String transactioType) {
			this.transactioType = transactioType;
		}
		/**
		 * @return Returns the payinslipDate.
		 */
		public String getPayinslipDate() {
			return payinslipDate;
		}
		/**
		 * @param payinslipDate The payinslipDate to set.
		 */
		public void setPayinslipDate(String payinslipDate) {
			this.payinslipDate = payinslipDate;
		}
		/**
		 * @return Returns the payinslipNo.
		 */
		public String getPayinslipNo() {
			return payinslipNo;
		}
		/**
		 * @param payinslipNo The payinslipNo to set.
		 */
		public void setPayinslipNo(String payinslipNo) {
			this.payinslipNo = payinslipNo;
		}
		}
	
    
    
	
	




