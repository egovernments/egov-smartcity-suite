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
package org.egov.bnd.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Administrator TODO To change the template for this generated type
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */
public class CertPayment {

    private Integer id = null;
    private Integer reportId; // could be either the birth/death id,
    // null in case of non-availability certificates
    private String receiptNo;
    private Integer certType;
    private double searchFees;
    private double otherFees;
    private double certificateFees;
    private double nameInclusionFees;
    private double postageFees;
    private double serviceCharge;
    private String description;
    private String moduleType;
    private String dispatchNo;
    private Date dispatchDate;
    private Date voucherDate;
    private BnDCitizen requestedCitizen;
    private String paymentMode;// make this as string=CashTransactionode
    private Timestamp createdDate;
    private Integer createdBy;
    private Integer noOfCopies;
    private double totalFees;
    private String userName;
    private String chequeNo;
    // private Timestamp chequeDate;
    private String terminalName;
    private String chequePaidTo;
    private String drawnOnBank;
    private String ccbankTransactionid;
    private String merchantTransid;
    private String registrationNo;
    private String regUnitDesc;
    private String bankChallanNumber;
    private Date challanDate;
    private String collectedBank;

    private String nameInclntrans;
    private BnDCitizen newChildName;
    private String amountInRupee;
    private Date registrationDate;

    private String childOrDeceasedName;
    private String sex;
    private String acknowledgeNumber;

    public double getOtherFees() {
        return otherFees;
    }

    public void setOtherFees(final double otherFees) {
        this.otherFees = otherFees;
    }

    public String getAcknowledgeNumber() {
        return acknowledgeNumber;
    }

    public void setAcknowledgeNumber(final String acknowledgeNumber) {
        this.acknowledgeNumber = acknowledgeNumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(final String sex) {
        this.sex = sex;
    }

    public String getChildOrDeceasedName() {
        return childOrDeceasedName;
    }

    public void setChildOrDeceasedName(final String childOrDeceasedName) {
        this.childOrDeceasedName = childOrDeceasedName;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(final Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getAmountInRupee() {
        return amountInRupee;
    }

    public void setAmountInRupee(final String amountInRupee) {
        this.amountInRupee = amountInRupee;
    }

    /**
     * @return the nameInclntrans
     */
    public String getNameInclntrans() {
        return nameInclntrans;
    }

    /**
     * @param nameInclntrans
     *            the nameInclntrans to set
     */
    public void setNameInclntrans(final String nameInclntrans) {
        this.nameInclntrans = nameInclntrans;
    }

    /**
     * @return Returns the bankChallanNumber.
     */
    public String getBankChallanNumber() {
        return bankChallanNumber;
    }

    /**
     * @param bankChallanNumber
     *            The bankChallanNumber to set.
     */
    public void setBankChallanNumber(final String bankChallanNumber) {
        this.bankChallanNumber = bankChallanNumber;
    }

    /**
     * @return Returns the collectedBank.
     */
    public String getCollectedBank() {
        return collectedBank;
    }

    /**
     * @param collectedBank
     *            The collectedBank to set.
     */
    public void setCollectedBank(final String collectedBank) {
        this.collectedBank = collectedBank;
    }

    /**
     * @return Returns the paymentMode.
     */
    public String getPaymentMode() {
        return paymentMode;
    }

    /**
     * @param paymentMode
     *            The paymentMode to set.
     */
    public void setPaymentMode(final String paymentMode) {
        this.paymentMode = paymentMode;
    }

    /**
     * @return Returns the registrationNo.
     */
    public String getRegistrationNo() {
        return registrationNo;
    }

    /**
     * @param registrationNo
     *            The registrationNo to set.
     */
    public void setRegistrationNo(final String registrationNo) {
        this.registrationNo = registrationNo;
    }

    /**
     * @return Returns the drawnOnBank.
     */
    public String getDrawnOnBank() {
        return drawnOnBank;
    }

    /**
     * @param drawnOnBank
     *            The drawnOnBank to set.
     */
    public void setDrawnOnBank(final String drawnOnBank) {
        this.drawnOnBank = drawnOnBank;
    }

    /**
     * @return Returns the chequePaidTo.
     */
    public String getChequePaidTo() {
        return chequePaidTo;
    }

    /**
     * @param chequePaidTo
     *            The chequePaidTo to set.
     */
    public void setChequePaidTo(final String chequePaidTo) {
        this.chequePaidTo = chequePaidTo;
    }

    /**
     * @return Returns the remarks.
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks
     *            The remarks to set.
     */
    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    private String remarks;

    /**
     * @return Returns the totalFees.
     */
    public double getTotalFees() {
        return totalFees;
    }

    /**
     * @param totalFees
     *            The totalFees to set.
     */
    public void setTotalFees(final double totalFees) {
        this.totalFees = totalFees;
    }

    /**
     * @return Returns the certificateFees.
     */
    public double getCertificateFees() {
        return certificateFees;
    }

    /**
     * @param certificateFees
     *            The certificateFees to set.
     */
    public void setCertificateFees(final double certificateFees) {
        this.certificateFees = certificateFees;
    }

    /**
     * @return Returns the certType.
     */
    public Integer getCertType() {
        return certType;
    }

    /**
     * @param certType
     *            The certType to set.
     */
    public void setCertType(final Integer certType) {
        this.certType = certType;
    }

    /**
     * @return Returns the createdBy.
     */
    public Integer getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy
     *            The createdBy to set.
     */
    public void setCreatedBy(final Integer createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return Returns the createdDate.
     */
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate
     *            The createdDate to set.
     */
    public void setCreatedDate(final Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return Returns the id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     * @return Returns the postageFees.
     */
    public double getPostageFees() {
        return postageFees;
    }

    /**
     * @param postageFees
     *            The postageFees to set.
     */
    public void setPostageFees(final double postageFees) {
        this.postageFees = postageFees;
    }

    /**
     * @return Returns the reportId.
     */
    public Integer getReportId() {
        return reportId;
    }

    /**
     * @param reportId
     *            The reportId to set.
     */
    public void setReportId(final Integer reportId) {
        this.reportId = reportId;
    }

    /**
     * @return Returns the receiptNo.
     */
    public String getReceiptNo() {
        return receiptNo;
    }

    /**
     * @param receiptNo
     *            The receiptNo to set.
     */
    public void setReceiptNo(final String receiptNo) {
        this.receiptNo = receiptNo;
    }

    /**
     * @return Returns the requestedCitizen.
     */
    public BnDCitizen getRequestedCitizen() {
        return requestedCitizen;
    }

    /**
     * @param requestedCitizen
     *            The requestedCitizen to set.
     */
    public void setRequestedCitizen(final BnDCitizen requestedCitizen) {
        this.requestedCitizen = requestedCitizen;
    }

    /**
     * @return Returns the searchFees.
     */
    public double getSearchFees() {
        return searchFees;
    }

    /**
     * @param searchFees
     *            The searchFees to set.
     */
    public void setSearchFees(final double searchFees) {
        this.searchFees = searchFees;
    }

    /**
     * @return Returns the noOfCopies.
     */
    public Integer getNoOfCopies() {
        return noOfCopies;
    }

    /**
     * @param noOfCopies
     *            The noOfCopies to set.
     */
    public void setNoOfCopies(final Integer noOfCopies) {
        this.noOfCopies = noOfCopies;
    }

    /**
     * @return Returns the userName.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     *            The userName to set.
     */
    public void setUserName(final String userName) {
        this.userName = userName;
    }

    /**
     * @return Returns the chequeNo.
     */
    public String getChequeNo() {
        return chequeNo;
    }

    /**
     * @param chequeNo
     *            The chequeNo to set.
     */
    public void setChequeNo(final String chequeNo) {
        this.chequeNo = chequeNo;
    }

    /**
     * @return Returns the terminalName.
     */
    public String getTerminalName() {
        return terminalName;
    }

    /**
     * @param terminalName
     *            The terminalName to set.
     */
    public void setTerminalName(final String terminalName) {
        this.terminalName = terminalName;
    }

    /**
     * @param chequeDate
     *            The chequeDate to set.
     */
    /*
     * public void setChequeDate(Timestamp chequeDate) { this.chequeDate =
     * chequeDate; }
     */
    /**
     * @return Returns the nameInclusionFees.
     */
    public double getNameInclusionFees() {
        return nameInclusionFees;
    }

    /**
     * @param nameInclusionFees
     *            The nameInclusionFees to set.
     */
    public void setNameInclusionFees(final double nameInclusionFees) {
        this.nameInclusionFees = nameInclusionFees;
    }

    /**
     * @return Returns the serviceCharge.
     */
    public double getServiceCharge() {
        return serviceCharge;
    }

    /**
     * @param serviceCharge
     *            The serviceCharge to set.
     */
    public void setServiceCharge(final double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            The description to set.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * @return Returns the moduleType.
     */
    public String getModuleType() {
        return moduleType;
    }

    /**
     * @param moduleType
     *            The moduleType to set.
     */
    public void setModuleType(final String moduleType) {
        this.moduleType = moduleType;
    }

    /**
     * @return Returns the dispatchNo.
     */
    public String getDispatchNo() {
        return dispatchNo;
    }

    /**
     * @param dispatchNo
     *            The dispatchNo to set.
     */
    public void setDispatchNo(final String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    /**
     * @return Returns the dispatchDate.
     */
    public Date getDispatchDate() {
        return dispatchDate;
    }

    /**
     * @param dispatchDate
     *            The dispatchDate to set.
     */
    public void setDispatchDate(final Date dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    /**
     * @return Returns the ccbankTransactionid.
     */
    public String getCcbankTransactionid() {
        return ccbankTransactionid;
    }

    /**
     * @param ccbankTransactionid
     *            The ccbankTransactionid to set.
     */
    public void setCcbankTransactionid(final String ccbankTransactionid) {
        this.ccbankTransactionid = ccbankTransactionid;
    }

    /**
     * @return Returns the merchantTransid.
     */
    public String getMerchantTransid() {
        return merchantTransid;
    }

    /**
     * @param merchantTransid
     *            The merchantTransid to set.
     */
    public void setMerchantTransid(final String merchantTransid) {
        this.merchantTransid = merchantTransid;
    }

    /**
     * @return Returns the voucherDate.
     */
    public Date getVoucherDate() {
        return voucherDate;
    }

    /**
     * @param voucherDate
     *            The voucherDate to set.
     */
    public void setVoucherDate(final Date voucherDate) {
        this.voucherDate = voucherDate;
    }

    /**
     * @return Returns the regUnitDesc.
     */
    public String getRegUnitDesc() {
        return regUnitDesc;
    }

    /**
     * @param regUnitDesc
     *            The regUnitDesc to set.
     */
    public void setRegUnitDesc(final String regUnitDesc) {
        this.regUnitDesc = regUnitDesc;
    }

    /**
     * @return Returns the challanDate.
     */
    public Date getChallanDate() {
        return challanDate;
    }

    /**
     * @param challanDate
     *            The challanDate to set.
     */
    public void setChallanDate(final Date challanDate) {
        this.challanDate = challanDate;
    }

    /**
     * @return the newChildName
     */
    public BnDCitizen getNewChildName() {
        return newChildName;
    }

    /**
     * @param newChildName
     *            the newChildName to set
     */
    public void setNewChildName(final BnDCitizen newChildName) {
        this.newChildName = newChildName;
    }

}
