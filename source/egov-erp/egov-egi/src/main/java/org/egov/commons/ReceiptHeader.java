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
package org.egov.commons;

import java.math.BigDecimal;

public class ReceiptHeader implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private CVoucherHeader voucherHeaderId;
	private String type;
	private Integer wardId;
	private Bank bankId;
	private Bankbranch bankBranchId;
	private Bankaccount bankAccNoId;
	private String modeOfCollection;
	private Chequedetail chequeId;
	private BigDecimal cashAmount;

	private String narration;
	private String revenueSource;
	private int isReversed;
	private String cashier;
	private String receiptNo;
	private String manualReceiptNo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CVoucherHeader getVoucherHeaderId() {
		return voucherHeaderId;
	}

	public void setVoucherHeaderId(CVoucherHeader voucherHeaderId) {
		this.voucherHeaderId = voucherHeaderId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Bank getBankId() {
		return bankId;
	}

	public void setBankId(Bank bankId) {
		this.bankId = bankId;
	}

	public Bankbranch getBankBranchId() {
		return bankBranchId;
	}

	public void setBankBranchId(Bankbranch bankBranchId) {
		this.bankBranchId = bankBranchId;
	}

	public Bankaccount getBankAccNoId() {
		return bankAccNoId;
	}

	public void setBankAccNoId(Bankaccount bankAccNoId) {
		this.bankAccNoId = bankAccNoId;
	}

	public String getModeOfCollection() {
		return modeOfCollection;
	}

	public void setModeOfCollection(String modeOfCollection) {
		this.modeOfCollection = modeOfCollection;
	}

	public Chequedetail getChequeId() {
		return chequeId;
	}

	public void setChequeId(Chequedetail chequeId) {
		this.chequeId = chequeId;
	}

	public BigDecimal getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(BigDecimal cashAmount) {
		this.cashAmount = cashAmount;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getRevenueSource() {
		return revenueSource;
	}

	public void setRevenueSource(String revenueSource) {
		this.revenueSource = revenueSource;
	}

	public int getIsReversed() {
		return isReversed;
	}

	public void setIsReversed(int isReversed) {
		this.isReversed = isReversed;
	}

	public String getCashier() {
		return cashier;
	}

	public void setCashier(String cashier) {
		this.cashier = cashier;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getManualReceiptNo() {
		return manualReceiptNo;
	}

	public void setManualReceiptNo(String manualReceiptNo) {
		this.manualReceiptNo = manualReceiptNo;
	}

}
