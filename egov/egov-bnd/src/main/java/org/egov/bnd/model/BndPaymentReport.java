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

import java.math.BigDecimal;
import java.util.Date;

/**
 * BndPaymentReportId entity. @author MyEclipse Persistence Tools
 */

public class BndPaymentReport implements java.io.Serializable {

    // Fields

    /**
     *
     */
    private static final long serialVersionUID = -8155017703727416212L;
    private Long reportid;
    private String type;
    private Date collectiondate;
    private Long billid;
    private BigDecimal noOfCopies;
    private String applicantname;
    private String applicantaddress;
    private String remarks;
    private BigDecimal totalamount;
    private String receiptNumber;
    private Date receiptDate;
    private BnDCitizen citizenid;
    private String feeTypeCode;
    private BndNameChange nameChangeId;
    private String registrationno;

    // Property accessors

    public BnDCitizen getCitizenid() {
        return citizenid;
    }

    public String getFeeTypeCode() {
        return feeTypeCode;
    }

    public void setFeeTypeCode(final String feeTypeCode) {
        this.feeTypeCode = feeTypeCode;
    }

    public void setCitizenid(final BnDCitizen citizenid) {
        this.citizenid = citizenid;
    }

    public Long getReportid() {
        return reportid;
    }

    public void setReportid(final Long reportid) {
        this.reportid = reportid;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Date getCollectiondate() {
        return collectiondate;
    }

    public void setCollectiondate(final Date collectiondate) {
        this.collectiondate = collectiondate;
    }

    public Long getBillid() {
        return billid;
    }

    public void setBillid(final Long billid) {
        this.billid = billid;
    }

    public BigDecimal getNoOfCopies() {
        return noOfCopies;
    }

    public void setNoOfCopies(final BigDecimal noOfCopies) {
        this.noOfCopies = noOfCopies;
    }

    public String getApplicantname() {
        return applicantname;
    }

    public void setApplicantname(final String applicantname) {
        this.applicantname = applicantname;
    }

    public String getApplicantaddress() {
        return applicantaddress;
    }

    public void setApplicantaddress(final String applicantaddress) {
        this.applicantaddress = applicantaddress;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(final BigDecimal totalamount) {
        this.totalamount = totalamount;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(final String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(final Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getRegistrationno() {
        return registrationno;
    }

    public void setRegistrationno(final String registrationno) {
        this.registrationno = registrationno;
    }

    public BndNameChange getNameChangeId() {
        return nameChangeId;
    }

    public void setNameChangeId(final BndNameChange nameChangeId) {
        this.nameChangeId = nameChangeId;
    }

}
