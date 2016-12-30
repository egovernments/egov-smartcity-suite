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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.tl.utils.Constants;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "EGTL_LICENSE")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = License.SEQUENCE, sequenceName = License.SEQUENCE, allocationSize = 1)
@Unique(fields = {"licenseNumber", "applicationNumber", "oldLicenseNumber"}, enableDfltMsg = true, isSuperclass = true)
public class License extends StateAware {

    public static final String SEQUENCE = "SEQ_EGTL_LICENSE";
    private static final long serialVersionUID = -4621190785979222546L;
    @Id
    @GeneratedValue(generator = SEQUENCE, strategy = GenerationType.SEQUENCE)
    @Expose
    protected Long id;

    @NotBlank
    @SafeHtml
    @Length(max = 128)
    @Column(name = "APPL_NUM")
    @Audited
    protected String applicationNumber;

    @SafeHtml
    @Length(max = 50)
    @Column(name = "LICENSE_NUMBER")
    protected String licenseNumber;

    @SafeHtml
    @NotBlank
    @Length(max = 256)
    @Column(name = "NAME_OF_ESTAB")
    @Audited
    protected String nameOfEstablishment;

    @SafeHtml
    @Length(max = 50)
    @Column(name = "OLD_LICENSE_NUMBER")
    protected String oldLicenseNumber;

    @SafeHtml
    @Length(max = 512)
    @Column(name = "REMARKS")
    protected String remarks;

    @SafeHtml
    @NotBlank
    @Length(max = 120)
    @Column(name = "OWNERSHIP_TYPE")
    @Audited
    protected String ownershipType;

    @SafeHtml
    @NotBlank
    @Length(max = 250)
    @Column(name = "ADDRESS")
    @Audited
    protected String address;

    @SafeHtml
    @Length(max = 100)
    @Column(name = "TEMP_LICENSE_NUMBER")
    protected String tempLicenseNumber;

    @SafeHtml
    @Length(max = 50)
    @Column(name = "AGREEMENT_DOCUMENT_NO")
    @Audited
    protected String agreementDocNo;

    @SafeHtml
    @Length(max = 40)
    @Column(name = "digisignedcertfilestoreid")
    protected String digiSignedCertFileStoreId;

    @SafeHtml
    @Length(max = 64)
    @Column(name = "ASSESSMENTNO")
    protected String assessmentNo;

    @NotNull
    @Column(name = "APPL_DATE")
    @Temporal(TemporalType.DATE)
    @Audited
    protected Date applicationDate;

    @Column(name = "COMMENCEMENTDATE")
    @Temporal(TemporalType.DATE)
    @Audited
    protected Date commencementDate;

    @Column(name = "AGREEMENT_DATE")
    @Temporal(TemporalType.DATE)
    @Audited
    protected Date agreementDate;

    @Column(name = "DATEOFEXPIRY")
    @Temporal(TemporalType.DATE)
    protected Date dateOfExpiry;

    @Column(name = "IS_ACTIVE")
    protected boolean isActive;

    @Column(name = "ISLEGACY")
    protected boolean legacy;

    @Column(name = "TRADE_AREA_WEIGHT")
    @Audited
    protected BigDecimal tradeArea_weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EGWSTATUSID")
    protected EgwStatus egwStatus;

    @ManyToOne
    @JoinColumn(name = "ID_ADM_BNDRY")
    protected Boundary boundary;

    @ManyToOne
    @JoinColumn(name = "ID_PARENT_BNDRY")
    protected Boundary parentBoundary;

    @ManyToOne
    @JoinColumn(name = "NATUREOFBUSINESS")
    protected NatureOfBusiness natureOfBusiness;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_demand")
    protected LicenseDemand licenseDemand;

    @Valid
    @OneToOne(mappedBy = "license", cascade = CascadeType.ALL)
    protected Licensee licensee;

    @ManyToOne
    @JoinColumn(name = "ID_STATUS")
    protected LicenseStatus status;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ID_SUB_CATEGORY")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    protected LicenseSubCategory tradeName;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "licenseAppType")
    protected LicenseAppType licenseAppType;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ID_CATEGORY")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    protected LicenseCategory category;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "license")
    protected List<LicenseDocument> documents = new ArrayList<>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public LicenseDemand getLicenseDemand() {
        return licenseDemand;
    }

    public void setLicenseDemand(final LicenseDemand licenseDemand) {
        this.licenseDemand = licenseDemand;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(final Date applicationDate) {
        this.applicationDate = applicationDate;
    }

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

    public LicenseSubCategory getTradeName() {
        return tradeName;
    }

    public void setTradeName(final LicenseSubCategory tradeName) {
        this.tradeName = tradeName;
    }

    public void setActive(final boolean isActive) {
        this.isActive = isActive;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public LicenseCategory getCategory() {
        return category;
    }

    public void setCategory(final LicenseCategory category) {
        this.category = category;
    }

    public String getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(final String ownershipType) {
        this.ownershipType = ownershipType;
    }

    public BigDecimal getTradeArea_weight() {
        return tradeArea_weight;
    }

    public void setTradeArea_weight(final BigDecimal tradeAreaweight) {
        tradeArea_weight = tradeAreaweight;
    }

    public NatureOfBusiness getNatureOfBusiness() {
        return natureOfBusiness;
    }

    public void setNatureOfBusiness(final NatureOfBusiness natureOfBusiness) {
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

    public Date getAgreementDate() {
        return agreementDate;
    }

    public void setAgreementDate(final Date agreementDate) {
        this.agreementDate = agreementDate;
    }

    public String getAgreementDocNo() {
        return agreementDocNo;
    }

    public void setAgreementDocNo(final String agreementDocNo) {
        this.agreementDocNo = agreementDocNo;
    }

    public String getDigiSignedCertFileStoreId() {
        return digiSignedCertFileStoreId;
    }

    public void setDigiSignedCertFileStoreId(final String digiSignedCertFileStoreId) {
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

    public boolean isRejected() {
        return hasState() && getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED);
    }

    public String getAssessmentNo() {
        return assessmentNo;
    }

    public void setAssessmentNo(final String assessmentNo) {
        this.assessmentNo = assessmentNo;
    }

    public List<LicenseDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(final List<LicenseDocument> documents) {
        this.documents = documents;
    }

    @Override
    public String getStateDetails() {
        return "";
    }

    public boolean isApproved() {
        return hasState() && getState().getValue().equals(Constants.WF_STATE_COMMISSIONER_APPROVED_STR);
    }

    public boolean isAcknowledged() {
        return getStatus().getStatusCode().equals(Constants.STATUS_ACKNOLEDGED);
    }

    public boolean canCollectFee() {
        return !isPaid() && !isRejected() && isAcknowledged() || isApproved();
    }

    public boolean isStatusActive() {
        return getStatus().getStatusCode().equalsIgnoreCase(Constants.STATUS_ACTIVE);
    }
}