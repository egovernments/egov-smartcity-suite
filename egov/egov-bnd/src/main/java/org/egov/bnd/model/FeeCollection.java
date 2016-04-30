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

import org.apache.log4j.Logger;
import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.User;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class FeeCollection {

    public FeeCollection() {
        super();
        logger.info("inside FeeCollection");
    }

    public static final String AUTO = "AUTO";
    protected final Logger logger = Logger.getLogger(getClass().getName());
    private Long id = null;
    private Date updatedTime = null;
    private Integer no_Of_copies;
    private String type;
    private Long reportId;
    private String remarks;
    private User createdBy;
    private String applicantName;
    private String applicantAddress;
    private EgwStatus statusType;
    private Date collectionDate;
    private String certificateNumber;
    private Boolean isFreeCertificate = Boolean.FALSE;

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(final String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public Boolean getIsFreeCertificate() {
        return isFreeCertificate;
    }

    public void setIsFreeCertificate(final Boolean isFreeCertificate) {
        this.isFreeCertificate = isFreeCertificate;
    }

    private Set<FeeCollectionDetails> feeCollectionDetails = new HashSet<FeeCollectionDetails>(0);
    private BigDecimal totalAmount;

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Date getCollectionDate() {
        return collectionDate;
    }

    public void setTotalAmount(final BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setCollectionDate(final Date collectionDate) {
        this.collectionDate = collectionDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(final Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getNo_Of_copies() {
        return no_Of_copies;
    }

    public void setNo_Of_copies(final Integer noOfCopies) {
        no_Of_copies = noOfCopies;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(final Long reportId) {
        this.reportId = reportId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final User createdBy) {
        this.createdBy = createdBy;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(final String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantAddress() {
        return applicantAddress;
    }

    public void setApplicantAddress(final String applicantAddress) {
        this.applicantAddress = applicantAddress;
    }

    public EgwStatus getStatusType() {
        return statusType;
    }

    public void setStatusType(final EgwStatus statusType) {
        this.statusType = statusType;
    }

    public void addFeeCollectionDetail(final FeeCollectionDetails feeCollectionDetails) {
        this.feeCollectionDetails.add(feeCollectionDetails);
    }

    public void removeFeeCollectionDetail(final FeeCollectionDetails feeCollectionDetails) {
        this.feeCollectionDetails.remove(feeCollectionDetails);
    }

    public Set<FeeCollectionDetails> getFeeCollectionDetails() {
        return feeCollectionDetails;
    }

    public void setFeeCollectionDetails(final Set<FeeCollectionDetails> feeCollectionDetails) {
        this.feeCollectionDetails = feeCollectionDetails;
    }

}
