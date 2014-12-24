/*
 * PaymentForm.java Created on Mar 3, 2008
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.payment.client;

import org.apache.struts.action.ActionForm;

/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Sathish P
 * @version 1.00 
 *  
 */

public class PaymentForm extends ActionForm
{
	public String fundId;
	public String fundSourceId;
	public String fundName;
	public String voucherFromDate;
	public String voucherToDate;
	public String billFromDate;
	public String billToDate;
	public String section;
	public String billVhNoFrom;
	public String billVhNoTo;
	public String month;
	public String pay;
	public String[] payCBill;
	public String[] paySalBill;
	public String[] payContBill;
	public String[] paySupBill;
	public String contractor;
	public String supplier;
	public String salBillVhNoFrom;
	public String salBillVhNoTo;
	public String conBillVhNoFrom;
	public String conBillVhNoTo;
	public String supBillVhNoFrom;
	public String supBillVhNoTo;
	public String[] contingentBillId;
	public String[] contractorBillId;
	public String[] salBillId;
	public String[] supplierBillId;
	public String[] contingentBillNetAmt;
	public String[] salBillNetAmt;
	public String[] conBillNetAmt;
	public String[] supBillNetAmt;
	public String contingentBillTotal;
	public String salBillTotal;
	public String conBillTotal;
	public String supBillTotal;
	public String grandTotal;
	public String pymntVhNoPrefix;
	public String pymntVhNo;
	public String pymntVhDate;
	public String bank;
	public String bankAccount;
	public String chequeNo;
	public String chequeDate;
	public Boolean isChqSurrendered=false;
	public String newChequeNo;
	public String newChequeDate;
	public String paidBy;
	public String bankFund;
	public String jvNoPrefix;
	public String jvNo;
	public String vhDescription;
	public String scheme;
	public String subScheme;
	public String revPymntVhNo;
	public String revPymntVhDate;
	public String revJvNo;
	public Boolean isRTGSPayment =false;
	public String payTo;
	public String departmentId;
	public String functionaryId;
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public String getFunctionaryId() {
		return functionaryId;
	}
	public void setFunctionaryId(String functionaryId) {
		this.functionaryId = functionaryId;
	}
	/**
	 * @return the bank
	 */
	public String getBank()
	{
		return bank;
	}
	/**
	 * @param bank the bank to set
	 */
	public void setBank(String bank)
	{
		this.bank = bank;
	}
	/**
	 * @return the bankAccount
	 */
	public String getBankAccount()
	{
		return bankAccount;
	}
	/**
	 * @param bankAccount the bankAccount to set
	 */
	public void setBankAccount(String bankAccount)
	{
		this.bankAccount = bankAccount;
	}
	/**
	 * @return the chequeDate
	 */
	public String getChequeDate()
	{
		return chequeDate;
	}
	/**
	 * @param chequeDate the chequeDate to set
	 */
	public void setChequeDate(String chequeDate)
	{
		this.chequeDate = chequeDate;
	}
	/**
	 * @return the chequeNo
	 */
	public String getChequeNo()
	{
		return chequeNo;
	}
	/**
	 * @param chequeNo the chequeNo to set
	 */
	public void setChequeNo(String chequeNo)
	{
		this.chequeNo = chequeNo;
	}
	/**
	 * @return the isChqSurrendered
	 */
	public Boolean getIsChqSurrendered()
	{
		return isChqSurrendered;
	}
	/**
	 * @param isChqSurrendered the isChqSurrendered to set
	 */
	public void setIsChqSurrendered(Boolean isChqSurrendered)
	{
		this.isChqSurrendered = isChqSurrendered;
	}
	/**
	 * @return the newChequeDate
	 */
	public String getNewChequeDate()
	{
		return newChequeDate;
	}
	/**
	 * @param newChequeDate the newChequeDate to set
	 */
	public void setNewChequeDate(String newChequeDate)
	{
		this.newChequeDate = newChequeDate;
	}
	/**
	 * @return the newChequeNo
	 */
	public String getNewChequeNo()
	{
		return newChequeNo;
	}
	/**
	 * @param newChequeNo the newChequeNo to set
	 */
	public void setNewChequeNo(String newChequeNo)
	{
		this.newChequeNo = newChequeNo;
	}
	/**
	 * @return the pymntVhDate
	 */
	public String getPymntVhDate()
	{
		return pymntVhDate;
	}
	/**
	 * @param pymntVhDate the pymntVhDate to set
	 */
	public void setPymntVhDate(String pymntVhDate)
	{
		this.pymntVhDate = pymntVhDate;
	}
	/**
	 * @return the pymntVhNo
	 */
	public String getPymntVhNo()
	{
		return pymntVhNo;
	}
	/**
	 * @param pymntVhNo the pymntVhNo to set
	 */
	public void setPymntVhNo(String pymntVhNo)
	{
		this.pymntVhNo = pymntVhNo;
	}
	/**
	 * @return the pymntVhNoPrefix
	 */
	public String getPymntVhNoPrefix()
	{
		return pymntVhNoPrefix;
	}
	/**
	 * @param pymntVhNoPrefix the pymntVhNoPrefix to set
	 */
	public void setPymntVhNoPrefix(String pymntVhNoPrefix)
	{
		this.pymntVhNoPrefix = pymntVhNoPrefix;
	}
	
	/**
	 * @return the conBillNetAmt
	 */
	public String[] getConBillNetAmt()
	{
		return conBillNetAmt;
	}
	/**
	 * @param conBillNetAmt the conBillNetAmt to set
	 */
	public void setConBillNetAmt(String[] conBillNetAmt)
	{
		this.conBillNetAmt = conBillNetAmt;
	}
	/**
	 * @return the contingentBillNetAmt
	 */
	public String[] getContingentBillNetAmt()
	{
		return contingentBillNetAmt;
	}
	/**
	 * @param contingentBillNetAmt the contingentBillNetAmt to set
	 */
	public void setContingentBillNetAmt(String[] contingentBillNetAmt)
	{
		this.contingentBillNetAmt = contingentBillNetAmt;
	}
	/**
	 * @return the salBillNetAmt
	 */
	public String[] getSalBillNetAmt()
	{
		return salBillNetAmt;
	}
	/**
	 * @param salBillNetAmt the salBillNetAmt to set
	 */
	public void setSalBillNetAmt(String[] salBillNetAmt)
	{
		this.salBillNetAmt = salBillNetAmt;
	}
	/**
	 * @return the supBillNetAmt
	 */
	public String[] getSupBillNetAmt()
	{
		return supBillNetAmt;
	}
	/**
	 * @param supBillNetAmt the supBillNetAmt to set
	 */
	public void setSupBillNetAmt(String[] supBillNetAmt)
	{
		this.supBillNetAmt = supBillNetAmt;
	}
	/**
	 * @return the billVhNoFrom
	 */
	public String getBillVhNoFrom()
	{
		return billVhNoFrom;
	}
	/**
	 * @param billVhNoFrom the billVhNoFrom to set
	 */
	public void setBillVhNoFrom(String billVhNoFrom)
	{
		this.billVhNoFrom = billVhNoFrom;
	}
	/**
	 * @return the billVhNoTo
	 */
	public String getBillVhNoTo()
	{
		return billVhNoTo;
	}
	/**
	 * @param billVhNoTo the billVhNoTo to set
	 */
	public void setBillVhNoTo(String billVhNoTo)
	{
		this.billVhNoTo = billVhNoTo;
	}
	/**
	 * @return the month
	 */
	public String getMonth()
	{
		return month;
	}
	/**
	 * @param month the month to set
	 */
	public void setMonth(String month)
	{
		this.month = month;
	}
	
	/**
	 * @return the conBillVhNoFrom
	 */
	public String getConBillVhNoFrom()
	{
		return conBillVhNoFrom;
	}
	/**
	 * @param conBillVhNoFrom the conBillVhNoFrom to set
	 */
	public void setConBillVhNoFrom(String conBillVhNoFrom)
	{
		this.conBillVhNoFrom = conBillVhNoFrom;
	}
	/**
	 * @return the conBillVhNoTo
	 */
	public String getConBillVhNoTo()
	{
		return conBillVhNoTo;
	}
	/**
	 * @param conBillVhNoTo the conBillVhNoTo to set
	 */
	public void setConBillVhNoTo(String conBillVhNoTo)
	{
		this.conBillVhNoTo = conBillVhNoTo;
	}
	/**
	 * @return the contractor
	 */
	public String getContractor()
	{
		return contractor;
	}
	/**
	 * @param contractor the contractor to set
	 */
	public void setContractor(String contractor)
	{
		this.contractor = contractor;
	}
	/**
	 * @return the salBillVhNoFrom
	 */
	public String getSalBillVhNoFrom()
	{
		return salBillVhNoFrom;
	}
	/**
	 * @param salBillVhNoFrom the salBillVhNoFrom to set
	 */
	public void setSalBillVhNoFrom(String salBillVhNoFrom)
	{
		this.salBillVhNoFrom = salBillVhNoFrom;
	}
	/**
	 * @return the salBillVhNoTo
	 */
	public String getSalBillVhNoTo()
	{
		return salBillVhNoTo;
	}
	/**
	 * @param salBillVhNoTo the salBillVhNoTo to set
	 */
	public void setSalBillVhNoTo(String salBillVhNoTo)
	{
		this.salBillVhNoTo = salBillVhNoTo;
	}
	/**
	 * @return the supBillVhNoFrom
	 */
	public String getSupBillVhNoFrom()
	{
		return supBillVhNoFrom;
	}
	/**
	 * @param supBillVhNoFrom the supBillVhNoFrom to set
	 */
	public void setSupBillVhNoFrom(String supBillVhNoFrom)
	{
		this.supBillVhNoFrom = supBillVhNoFrom;
	}
	/**
	 * @return the supBillVhNoTo
	 */
	public String getSupBillVhNoTo()
	{
		return supBillVhNoTo;
	}
	/**
	 * @param supBillVhNoTo the supBillVhNoTo to set
	 */
	public void setSupBillVhNoTo(String supBillVhNoTo)
	{
		this.supBillVhNoTo = supBillVhNoTo;
	}
	/**
	 * @return the supplier
	 */
	public String getSupplier()
	{
		return supplier;
	}
	/**
	 * @param supplier the supplier to set
	 */
	public void setSupplier(String supplier)
	{
		this.supplier = supplier;
	}
	/**
	 * @return the section
	 */
	public String getSection()
	{
		return section;
	}
	/**
	 * @param section the section to set
	 */
	public void setSection(String section)
	{
		this.section = section;
	}
	/**
	 * @return the pay
	 */
	public String getPay()
	{
		return pay;
	}
	/**
	 * @param pay the pay to set
	 */
	public void setPay(String pay)
	{
		this.pay = pay;
	}	
	/**
	 * @return the fundId
	 */
	public String getFundId()
	{
		return fundId;
	}
	/**
	 * @param fundId the fundId to set
	 */
	public void setFundId(String fundId)
	{
		this.fundId = fundId;
	}
	/**
	 * @return the fundName
	 */
	public String getFundName()
	{
		return fundName;
	}
	/**
	 * @param fundName the fundName to set
	 */
	public void setFundName(String fundName)
	{
		this.fundName = fundName;
	}	
	/**
	 * @return the billFromDate
	 */
	public String getBillFromDate()
	{
		return billFromDate;
	}
	/**
	 * @param billFromDate the billFromDate to set
	 */
	public void setBillFromDate(String billFromDate)
	{
		this.billFromDate = billFromDate;
	}
	/**
	 * @return the billToDate
	 */
	public String getBillToDate()
	{
		return billToDate;
	}
	/**
	 * @param billToDate the billToDate to set
	 */
	public void setBillToDate(String billToDate)
	{
		this.billToDate = billToDate;
	}
	/**
	 * @return the voucherFromDate
	 */
	public String getVoucherFromDate()
	{
		return voucherFromDate;
	}
	/**
	 * @param voucherFromDate the voucherFromDate to set
	 */
	public void setVoucherFromDate(String voucherFromDate)
	{
		this.voucherFromDate = voucherFromDate;
	}
	/**
	 * @return the voucherToDate
	 */
	public String getVoucherToDate()
	{
		return voucherToDate;
	}
	/**
	 * @param voucherToDate the voucherToDate to set
	 */
	public void setVoucherToDate(String voucherToDate)
	{
		this.voucherToDate = voucherToDate;
	}
	/**
	 * @return the fundSourceId
	 */
	public String getFundSourceId()
	{
		return fundSourceId;
	}
	/**
	 * @param fundSourceId the fundSourceId to set
	 */
	public void setFundSourceId(String fundSourceId)
	{
		this.fundSourceId = fundSourceId;
	}
	/**
	 * @return the bankFund
	 */
	public String getBankFund()
	{
		return bankFund;
	}
	/**
	 * @param bankFund the bankFund to set
	 */
	public void setBankFund(String bankFund)
	{
		this.bankFund = bankFund;
	}
	
	/**
	 * @return the paidBy
	 */
	public String getPaidBy()
	{
		return paidBy;
	}
	/**
	 * @param paidBy the paidBy to set
	 */
	public void setPaidBy(String paidBy)
	{
		this.paidBy = paidBy;
	}
	
	/**
	 * @return the jvNo
	 */
	public String getJvNo()
	{
		return jvNo;
	}
	/**
	 * @param jvNo the jvNo to set
	 */
	public void setJvNo(String jvNo)
	{
		this.jvNo = jvNo;
	}
	/**
	 * @return the jvNoPrefix
	 */
	public String getJvNoPrefix()
	{
		return jvNoPrefix;
	}
	/**
	 * @param jvNoPrefix the jvNoPrefix to set
	 */
	public void setJvNoPrefix(String jvNoPrefix)
	{
		this.jvNoPrefix = jvNoPrefix;
	}
	/**
	 * @return the contingentBillId
	 */
	public String[] getContingentBillId()
	{
		return contingentBillId;
	}
	/**
	 * @param contingentBillId the contingentBillId to set
	 */
	public void setContingentBillId(String[] contingentBillId)
	{
		this.contingentBillId = contingentBillId;
	}
	/**
	 * @return the contractorBillId
	 */
	public String[] getContractorBillId()
	{
		return contractorBillId;
	}
	/**
	 * @param contractorBillId the contractorBillId to set
	 */
	public void setContractorBillId(String[] contractorBillId)
	{
		this.contractorBillId = contractorBillId;
	}
	/**
	 * @return the payCBill
	 */
	public String[] getPayCBill()
	{
		return payCBill;
	}
	/**
	 * @param payCBill the payCBill to set
	 */
	public void setPayCBill(String[] payCBill)
	{
		this.payCBill = payCBill;
	}
	/**
	 * @return the payContBill
	 */
	public String[] getPayContBill()
	{
		return payContBill;
	}
	/**
	 * @param payContBill the payContBill to set
	 */
	public void setPayContBill(String[] payContBill)
	{
		this.payContBill = payContBill;
	}
	/**
	 * @return the paySalBill
	 */
	public String[] getPaySalBill()
	{
		return paySalBill;
	}
	/**
	 * @param paySalBill the paySalBill to set
	 */
	public void setPaySalBill(String[] paySalBill)
	{
		this.paySalBill = paySalBill;
	}
	/**
	 * @return the paySupBill
	 */
	public String[] getPaySupBill()
	{
		return paySupBill;
	}
	/**
	 * @param paySupBill the paySupBill to set
	 */
	public void setPaySupBill(String[] paySupBill)
	{
		this.paySupBill = paySupBill;
	}
	/**
	 * @return the salBillId
	 */
	public String[] getSalBillId()
	{
		return salBillId;
	}
	/**
	 * @param salBillId the salBillId to set
	 */
	public void setSalBillId(String[] salBillId)
	{
		this.salBillId = salBillId;
	}
	/**
	 * @return the supplierBillId
	 */
	public String[] getSupplierBillId()
	{
		return supplierBillId;
	}
	/**
	 * @param supplierBillId the supplierBillId to set
	 */
	public void setSupplierBillId(String[] supplierBillId)
	{
		this.supplierBillId = supplierBillId;
	}
	/**
	 * @return the conBillTotal
	 */
	public String getConBillTotal()
	{
		return conBillTotal;
	}
	/**
	 * @param conBillTotal the conBillTotal to set
	 */
	public void setConBillTotal(String conBillTotal)
	{
		this.conBillTotal = conBillTotal;
	}
	/**
	 * @return the contingentBillTotal
	 */
	public String getContingentBillTotal()
	{
		return contingentBillTotal;
	}
	/**
	 * @param contingentBillTotal the contingentBillTotal to set
	 */
	public void setContingentBillTotal(String contingentBillTotal)
	{
		this.contingentBillTotal = contingentBillTotal;
	}
	/**
	 * @return the grandTotal
	 */
	public String getGrandTotal()
	{
		return grandTotal;
	}
	/**
	 * @param grandTotal the grandTotal to set
	 */
	public void setGrandTotal(String grandTotal)
	{
		this.grandTotal = grandTotal;
	}
	/**
	 * @return the salBillTotal
	 */
	public String getSalBillTotal()
	{
		return salBillTotal;
	}
	/**
	 * @param salBillTotal the salBillTotal to set
	 */
	public void setSalBillTotal(String salBillTotal)
	{
		this.salBillTotal = salBillTotal;
	}
	/**
	 * @return the supBillTotal
	 */
	public String getSupBillTotal()
	{
		return supBillTotal;
	}
	/**
	 * @param supBillTotal the supBillTotal to set
	 */
	public void setSupBillTotal(String supBillTotal)
	{
		this.supBillTotal = supBillTotal;
	}
	/**
	 * @return the scheme
	 */
	public String getScheme()
	{
		return scheme;
	}
	/**
	 * @param scheme the scheme to set
	 */
	public void setScheme(String scheme)
	{
		this.scheme = scheme;
	}
	/**
	 * @return the subScheme
	 */
	public String getSubScheme()
	{
		return subScheme;
	}
	/**
	 * @param subScheme the subScheme to set
	 */
	public void setSubScheme(String subScheme)
	{
		this.subScheme = subScheme;
	}
	/**
	 * @return the vhDescription
	 */
	public String getVhDescription()
	{
		return vhDescription;
	}
	/**
	 * @param vhDescription the vhDescription to set
	 */
	public void setVhDescription(String vhDescription)
	{
		this.vhDescription = vhDescription;
	}
	/**
	 * @return the revJvNo
	 */
	public String getRevJvNo()
	{
		return revJvNo;
	}
	/**
	 * @param revJvNo the revJvNo to set
	 */
	public void setRevJvNo(String revJvNo)
	{
		this.revJvNo = revJvNo;
	}
	/**
	 * @return the revPymntVhDate
	 */
	public String getRevPymntVhDate()
	{
		return revPymntVhDate;
	}
	/**
	 * @param revPymntVhDate the revPymntVhDate to set
	 */
	public void setRevPymntVhDate(String revPymntVhDate)
	{
		this.revPymntVhDate = revPymntVhDate;
	}
	/**
	 * @return the revPymntVhNo
	 */
	public String getRevPymntVhNo()
	{
		return revPymntVhNo;
	}
	/**
	 * @param revPymntVhNo the revPymntVhNo to set
	 */
	public void setRevPymntVhNo(String revPymntVhNo)
	{
		this.revPymntVhNo = revPymntVhNo;
	}
	
	
	public Boolean getIsRTGSPayment() {
		return isRTGSPayment;
	}
	public void setIsRTGSPayment(Boolean isRTGSPayment) {
		this.isRTGSPayment = isRTGSPayment;
	}
	public String getPayTo()
	{
		return payTo;
	}
	public void setPayTo(String payto)
	{
		this.payTo =payto;
	}
}

