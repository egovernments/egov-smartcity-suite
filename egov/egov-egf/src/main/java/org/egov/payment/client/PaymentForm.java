/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
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
    /**
     *
     */
    private static final long serialVersionUID = -3804519926901709638L;
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
    public Boolean isChqSurrendered = false;
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
    public Boolean isRTGSPayment = false;
    public String payTo;
    public String departmentId;
    public String functionaryId;

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final String departmentId) {
        this.departmentId = departmentId;
    }

    public String getFunctionaryId() {
        return functionaryId;
    }

    public void setFunctionaryId(final String functionaryId) {
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
    public void setBank(final String bank)
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
    public void setBankAccount(final String bankAccount)
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
    public void setChequeDate(final String chequeDate)
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
    public void setChequeNo(final String chequeNo)
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
    public void setIsChqSurrendered(final Boolean isChqSurrendered)
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
    public void setNewChequeDate(final String newChequeDate)
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
    public void setNewChequeNo(final String newChequeNo)
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
    public void setPymntVhDate(final String pymntVhDate)
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
    public void setPymntVhNo(final String pymntVhNo)
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
    public void setPymntVhNoPrefix(final String pymntVhNoPrefix)
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
    public void setConBillNetAmt(final String[] conBillNetAmt)
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
    public void setContingentBillNetAmt(final String[] contingentBillNetAmt)
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
    public void setSalBillNetAmt(final String[] salBillNetAmt)
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
    public void setSupBillNetAmt(final String[] supBillNetAmt)
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
    public void setBillVhNoFrom(final String billVhNoFrom)
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
    public void setBillVhNoTo(final String billVhNoTo)
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
    public void setMonth(final String month)
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
    public void setConBillVhNoFrom(final String conBillVhNoFrom)
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
    public void setConBillVhNoTo(final String conBillVhNoTo)
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
    public void setContractor(final String contractor)
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
    public void setSalBillVhNoFrom(final String salBillVhNoFrom)
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
    public void setSalBillVhNoTo(final String salBillVhNoTo)
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
    public void setSupBillVhNoFrom(final String supBillVhNoFrom)
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
    public void setSupBillVhNoTo(final String supBillVhNoTo)
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
    public void setSupplier(final String supplier)
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
    public void setSection(final String section)
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
    public void setPay(final String pay)
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
    public void setFundId(final String fundId)
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
    public void setFundName(final String fundName)
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
    public void setBillFromDate(final String billFromDate)
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
    public void setBillToDate(final String billToDate)
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
    public void setVoucherFromDate(final String voucherFromDate)
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
    public void setVoucherToDate(final String voucherToDate)
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
    public void setFundSourceId(final String fundSourceId)
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
    public void setBankFund(final String bankFund)
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
    public void setPaidBy(final String paidBy)
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
    public void setJvNo(final String jvNo)
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
    public void setJvNoPrefix(final String jvNoPrefix)
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
    public void setContingentBillId(final String[] contingentBillId)
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
    public void setContractorBillId(final String[] contractorBillId)
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
    public void setPayCBill(final String[] payCBill)
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
    public void setPayContBill(final String[] payContBill)
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
    public void setPaySalBill(final String[] paySalBill)
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
    public void setPaySupBill(final String[] paySupBill)
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
    public void setSalBillId(final String[] salBillId)
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
    public void setSupplierBillId(final String[] supplierBillId)
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
    public void setConBillTotal(final String conBillTotal)
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
    public void setContingentBillTotal(final String contingentBillTotal)
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
    public void setGrandTotal(final String grandTotal)
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
    public void setSalBillTotal(final String salBillTotal)
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
    public void setSupBillTotal(final String supBillTotal)
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
    public void setScheme(final String scheme)
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
    public void setSubScheme(final String subScheme)
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
    public void setVhDescription(final String vhDescription)
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
    public void setRevJvNo(final String revJvNo)
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
    public void setRevPymntVhDate(final String revPymntVhDate)
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
    public void setRevPymntVhNo(final String revPymntVhNo)
    {
        this.revPymntVhNo = revPymntVhNo;
    }

    public Boolean getIsRTGSPayment() {
        return isRTGSPayment;
    }

    public void setIsRTGSPayment(final Boolean isRTGSPayment) {
        this.isRTGSPayment = isRTGSPayment;
    }

    public String getPayTo()
    {
        return payTo;
    }

    public void setPayTo(final String payto)
    {
        payTo = payto;
    }
}
