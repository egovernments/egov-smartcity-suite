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
package org.egov.web.actions.report;

import java.math.BigDecimal;
import java.util.List;

import org.egov.model.instrument.InstrumentVoucher;
import org.springframework.transaction.annotation.Transactional;
@Transactional(readOnly=true)
public class BankBookViewEntry {
	String receiptVoucherDate;
	String receiptVoucherNumber;
	String receiptParticulars;
	BigDecimal receiptAmount;
	String receiptChequeDetail;
	String paymentVoucherDate;
	String paymentVoucherNumber;
	String paymentParticulars;
	BigDecimal paymentAmount;
	String paymentChequeDetail;
	String instrumentStatus;
	String glCode;
	private Long voucherId;
	//string of cheque number and dates 
	private String chequeNumber;
	

	private List<InstrumentVoucher>	instrumentVouchers;
	
  
	public List<InstrumentVoucher> getInstrumentVouchers() {
		return instrumentVouchers;
	}

	public void setInstrumentVouchers(List<InstrumentVoucher> instrumentVouchers) {
		this.instrumentVouchers = instrumentVouchers;
	}

	public BankBookViewEntry(){};
	
	public BankBookViewEntry(String voucherNumber,String voucherDate, String particulars,BigDecimal amount, String chequeDetail,String type) {
		super();
		if("Payment".equalsIgnoreCase(type)){
			this.paymentVoucherDate = voucherDate;
			this.paymentVoucherNumber = voucherNumber;
			this.paymentParticulars = particulars;
			this.paymentAmount = amount;
			this.paymentChequeDetail = chequeDetail;
		}else{
			this.receiptVoucherDate = voucherDate;
			this.receiptVoucherNumber = voucherNumber;
			this.receiptParticulars = particulars;
			this.receiptAmount = amount;
			this.receiptChequeDetail = chequeDetail;
		}
	}
	
	public BankBookViewEntry(String voucherNumber,String voucherDate, String particulars,BigDecimal amount, String chequeDetail,String type,String chequeNumber) {
		super();
		if("Payment".equalsIgnoreCase(type)){
			this.paymentVoucherDate = voucherDate;
			this.paymentVoucherNumber = voucherNumber;
			this.paymentParticulars = particulars;
			this.paymentAmount = amount;
			this.paymentChequeDetail = chequeDetail;
			this.instrumentVouchers=instrumentVouchers;
			this.chequeNumber=chequeNumber;
		}else{
			this.receiptVoucherDate = voucherDate;
			this.receiptVoucherNumber = voucherNumber;
			this.receiptParticulars = particulars;
			this.receiptAmount = amount;
			this.receiptChequeDetail = chequeDetail;
			this.instrumentVouchers=instrumentVouchers;
			this.chequeNumber=chequeNumber;
		}
	}

	public String getChequeNumber() {
		return chequeNumber;
	}

	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}
	
	public String getReceiptVoucherDate() {
		return receiptVoucherDate;
	}
	public void setReceiptVoucherDate(String receiptVoucherDate) {
		this.receiptVoucherDate = receiptVoucherDate;
	}
	public String getReceiptVoucherNumber() {
		return receiptVoucherNumber;
	}
	public void setReceiptVoucherNumber(String receiptVoucherNumber) {
		this.receiptVoucherNumber = receiptVoucherNumber;
	}
	public String getReceiptParticulars() {
		return receiptParticulars;
	}
	public void setReceiptParticulars(String receiptParticulars) {
		this.receiptParticulars = receiptParticulars;
	}
	public BigDecimal getReceiptAmount() {
		return receiptAmount;
	}
	public void setReceiptAmount(BigDecimal receiptAmount) {
		this.receiptAmount = receiptAmount;
	}
	public String getReceiptChequeDetail() {
		return receiptChequeDetail;
	}
	public void setReceiptChequeDetail(String receiptChequeDetail) {
		this.receiptChequeDetail = receiptChequeDetail;
	}
	public String getPaymentVoucherDate() {
		return paymentVoucherDate;
	}
	public void setPaymentVoucherDate(String paymentVoucherDate) {
		this.paymentVoucherDate = paymentVoucherDate;
	}
	public String getPaymentVoucherNumber() {
		return paymentVoucherNumber;
	}
	public void setPaymentVoucherNumber(String paymentVoucherNumber) {
		this.paymentVoucherNumber = paymentVoucherNumber;
	}
	public String getPaymentParticulars() {
		return paymentParticulars;
	}
	public void setPaymentParticulars(String paymentParticulars) {
		this.paymentParticulars = paymentParticulars;
	}
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getPaymentChequeDetail() {
		return paymentChequeDetail;
	}
	public void setPaymentChequeDetail(String paymentChequeDetail) {
		this.paymentChequeDetail = paymentChequeDetail;
	}
	public String getInstrumentStatus() {
		return instrumentStatus;
	}
	public void setInstrumentStatus(String instrumentStatus) {
		this.instrumentStatus = instrumentStatus;
	}
	public String getGlCode() {
		return glCode;
	}
	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}

	public void setVoucherId(Long voucherId) {
		this.voucherId = voucherId;
	}

	public Long getVoucherId() {
		return voucherId;
	}
}
