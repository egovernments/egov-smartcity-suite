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

package org.egov.tl.entity;

import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.tl.utils.Constants;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public abstract class License extends StateAware {

    private static final long serialVersionUID = 1L;

    protected Long id;
    @Required(message = "license.applicationdate.err.required")
    protected Date applicationDate;
    protected String applicationNumber;
    protected EgwStatus egwStatus;
    protected Boundary boundary;
    protected Boundary parentBoundary;
    protected NatureOfBusiness natureOfBusiness;
    protected Date dateOfExpiry;
    protected boolean isActive;
    protected LicenseDemand licenseDemand;
    protected Licensee licensee;
    protected String licenseNumber;
    protected String nameOfEstablishment;
    protected String oldLicenseNumber;
    protected String remarks;
    protected LicenseStatus status;
    protected String tempLicenseNumber;
    @NotNull
    protected LicenseSubCategory tradeName;
    protected LicenseAppType licenseAppType;
    protected String ownershipType;
    protected String address;
    @NotNull
    private LicenseCategory category;
    private BigDecimal tradeArea_weight;
    private boolean legacy;
    private Date commencementDate;

    private Date agreementDate;
    private String agreementDocNo;
    private String digiSignedCertFileStoreId;
    private String assessmentNo;
    public abstract String generateLicenseNumber(Serializable runningNumber);

    public abstract List<LicenseDocument> getDocuments();

    public abstract void setDocuments(List<LicenseDocument> documents);

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public LicenseDemand getLicenseDemand() {
        return licenseDemand;
    }

    public void setLicenseDemand(final LicenseDemand licenseDemand) {
        this.licenseDemand = licenseDemand;
    }

    @Audited
    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(final Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    @Audited
    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(final String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(final Boundary boundary) {
        this.boundary = boundary;
    }

    public Date getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(final Date dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(final boolean isActive) {
        this.isActive = isActive;
    }

    public Licensee getLicensee() {
        return licensee;
    }

    public void setLicensee(final Licensee licensee) {
        this.licensee = licensee;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(final String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    @Audited
    public String getNameOfEstablishment() {
        return nameOfEstablishment;
    }

    public void setNameOfEstablishment(final String nameOfEstablishment) {
        this.nameOfEstablishment = nameOfEstablishment;
    }

    public String getOldLicenseNumber() {
        return oldLicenseNumber;
    }

    public void setOldLicenseNumber(final String oldLicenseNumber) {
        this.oldLicenseNumber = oldLicenseNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public LicenseStatus getStatus() {
        return status;
    }

    public void setStatus(final LicenseStatus status) {
        this.status = status;
    }

    public String getTempLicenseNumber() {
        return tempLicenseNumber;
    }

    public void setTempLicenseNumber(final String tempLicenseNumber) {
        this.tempLicenseNumber = tempLicenseNumber;
    }

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    public LicenseSubCategory getTradeName() {
        return tradeName;
    }

    public void setTradeName(final LicenseSubCategory tradeName) {
        this.tradeName = tradeName;
    }

    public void setActive(final boolean isActive) {
        this.isActive = isActive;
    }

    @Audited
    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    public LicenseCategory getCategory() {
        return category;
    }

    public void setCategory(final LicenseCategory category) {
        this.category = category;
    }

    @Audited
    public String getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(final String ownershipType) {
        this.ownershipType = ownershipType;
    }

    @Audited
    public BigDecimal getTradeArea_weight() {
        return tradeArea_weight;
    }

    public void setTradeArea_weight(final BigDecimal tradeArea_weight) {
        this.tradeArea_weight = tradeArea_weight;
    }

    public NatureOfBusiness getNatureOfBusiness() {
        return natureOfBusiness;
    }

    public void setNatureOfBusiness(NatureOfBusiness natureOfBusiness) {
        this.natureOfBusiness = natureOfBusiness;
    }

    public LicenseAppType getLicenseAppType() {
        return licenseAppType;
    }

    public void setLicenseAppType(final LicenseAppType licenseAppType) {
        this.licenseAppType = licenseAppType;
    }

    public boolean isLegacy() {
        return legacy;
    }

    public void setLegacy(final boolean legacy) {
        this.legacy = legacy;
    }

    @Audited
    public Date getCommencementDate() {
        return commencementDate;
    }

    public void setCommencementDate(final Date commencementDate) {
        this.commencementDate = commencementDate;
    }

    public EgwStatus getEgwStatus() {
        return egwStatus;
    }

    public void setEgwStatus(final EgwStatus egwStatus) {
        this.egwStatus = egwStatus;
    }

    public Boundary getParentBoundary() {
        return parentBoundary;
    }

    public void setParentBoundary(final Boundary parentBoundary) {
        this.parentBoundary = parentBoundary;
    }

    @Audited
    public Date getAgreementDate() {
        return agreementDate;
    }

    public void setAgreementDate(final Date agreementDate) {
        this.agreementDate = agreementDate;
    }

    @Audited
    public String getAgreementDocNo() {
        return agreementDocNo;
    }

    public void setAgreementDocNo(final String agreementDocNo) {
        this.agreementDocNo = agreementDocNo;
    }

    public String getDigiSignedCertFileStoreId() {
        return this.digiSignedCertFileStoreId;
    }

    public void setDigiSignedCertFileStoreId(String digiSignedCertFileStoreId) {
        this.digiSignedCertFileStoreId = digiSignedCertFileStoreId;
    }

    public LicenseDemand getCurrentDemand() {
        return getLicenseDemand();
    }

    public BigDecimal getCurrentLicenseFee() {
        return getCurrentDemand().getEgDemandDetails().stream()
                .filter(dd -> dd.getEgDemandReason().getEgInstallmentMaster().equals(getCurrentDemand().getEgInstallmentMaster()))
                .findAny().get().getAmount();
    }

    public boolean isPaid() {
        return getTotalBalance().compareTo(BigDecimal.ZERO) == 0;
    }

    public BigDecimal getTotalBalance() {
        return licenseDemand.getBaseDemand().subtract(licenseDemand.getAmtCollected());
    }

    public boolean isStateRejected() {
        return getState() != null && getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED);
    }

    public String getAssessmentNo() {
        return assessmentNo;
    }

    public void setAssessmentNo(String assessmentNo) {
        this.assessmentNo = assessmentNo;
    }

}