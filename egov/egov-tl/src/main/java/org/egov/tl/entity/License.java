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
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.tl.entity.objection.LicenseObjection;
import org.egov.tl.entity.transfer.LicenseTransfer;
import org.egov.tl.utils.Constants;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

public abstract class License extends StateAware {

    private static final long serialVersionUID = 1L;
    @Required(message = "license.applicationdate.err.required")
    protected Date applicationDate;
    protected String applicationNumber;
    protected EgwStatus egwStatus;
    protected Boundary boundary;
    protected Boundary parentBoundary;
    // this should be NatureOfBusiness only which means it is Permanent or temporary.
    protected NatureOfBusiness natureOfBusiness;
    protected Date dateOfExpiry;
    protected String feeTypeStr;
    protected String inspectionDetails;
    protected boolean isActive;
    protected LicenseDemand licenseDemand;
    protected Licensee licensee;
    protected Long licenseeZoneId;
    protected String licenseNumber;
    protected Long licenseZoneId;
    protected String nameOfEstablishment;
    protected Integer noOfRooms;
    protected String oldLicenseNumber;
    protected BigDecimal otherCharges;// will be stored in demand
    protected String phoneNumber;
    protected String remarks;
    protected boolean rentalAgreement;
    protected BigDecimal rentPaid;
    protected LicenseStatus status;
    protected String tempLicenseNumber;
    @NotNull
    protected LicenseSubCategory tradeName;
    protected List<LicenseObjection> objections;
    protected Set<LicenseStatusValues> licenseStatusValuesSet;
    protected LicenseTransfer licenseTransfer;
    protected String licenseCheckList;
    protected BigDecimal deduction;
    protected BigDecimal swmFee;
    protected String servicetaxNumber;
    @OptionalPattern(regex = Constants.alphaNumericwithspecialchar, message = "license.tin.number.alphaNumeric")
    protected String tinNumber;
    protected String companyPanNumber;
    protected String vatNumber;
    protected String companyDetails;
    protected String namePowerOfAttorney;
    protected String bankIfscCode;
    protected BigDecimal minSolvency;
    protected BigDecimal avgAnnualTurnover;
    protected String feeExemption;
    protected BigDecimal violationFee;
    private String docImageNumber;
    private LicenseAppType licenseAppType;
    private String officeEmailId;
    private String propertyNo;
    private String ownershipType;
    private String address;
    @NotNull
    private LicenseCategory category;

    private BigDecimal tradeArea_weight;

    private boolean legacy;
    private Date commencementDate;

    private Date agreementDate;
    private String agreementDocNo;
    private FileStoreMapper fileStore;

    public abstract String generateApplicationNumber(String runningNumber);

    public abstract String generateLicenseNumber(Serializable runningNumber);

    public abstract List<LicenseDocument> getDocuments();

    public abstract void setDocuments(List<LicenseDocument> documents);

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

    public String getCompanyDetails() {
        return companyDetails;
    }

    public void setCompanyDetails(final String companyDetails) {
        this.companyDetails = companyDetails;
    }

    public Date getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(final Date dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    public BigDecimal getDeduction() {
        return deduction;
    }

    public void setDeduction(final BigDecimal deduction) {
        this.deduction = deduction;
    }

    public String getFeeTypeStr() {
        return feeTypeStr;
    }

    public void setFeeTypeStr(final String feeTypeStr) {
        this.feeTypeStr = feeTypeStr;
    }

    public String getInspectionDetails() {
        return inspectionDetails;
    }

    public void setInspectionDetails(final String inspectionDetails) {
        this.inspectionDetails = inspectionDetails;
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

    public Long getLicenseeZoneId() {
        return licenseeZoneId;
    }

    public void setLicenseeZoneId(final Long licenseeZoneId) {
        this.licenseeZoneId = licenseeZoneId;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(final String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Long getLicenseZoneId() {
        return licenseZoneId;
    }

    public void setLicenseZoneId(final Long licenseZoneId) {
        this.licenseZoneId = licenseZoneId;
    }

    @Audited
    public String getNameOfEstablishment() {
        return nameOfEstablishment;
    }

    public void setNameOfEstablishment(final String nameOfEstablishment) {
        this.nameOfEstablishment = nameOfEstablishment;
    }

    public Integer getNoOfRooms() {
        return noOfRooms;
    }

    public void setNoOfRooms(final Integer noOfRooms) {
        this.noOfRooms = noOfRooms;
    }

    public String getOldLicenseNumber() {
        return oldLicenseNumber;
    }

    public void setOldLicenseNumber(final String oldLicenseNumber) {
        this.oldLicenseNumber = oldLicenseNumber;
    }

    public BigDecimal getOtherCharges() {
        return otherCharges;
    }

    public void setOtherCharges(final BigDecimal otherCharges) {
        this.otherCharges = otherCharges;
    }

    @Audited
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getRentPaid() {
        return rentPaid;
    }

    public void setRentPaid(final BigDecimal rentPaid) {
        this.rentPaid = rentPaid;
    }

    public String getServicetaxNumber() {
        return servicetaxNumber;
    }

    public void setServicetaxNumber(final String servicetaxNumber) {
        this.servicetaxNumber = servicetaxNumber;
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

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(final String tinNumber) {
        this.tinNumber = tinNumber;
    }

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    public LicenseSubCategory getTradeName() {
        return tradeName;
    }

    public void setTradeName(final LicenseSubCategory tradeName) {
        this.tradeName = tradeName;
    }

    public boolean isRentalAgreement() {
        return rentalAgreement;
    }

    public void setRentalAgreement(final boolean rentalAgreement) {
        this.rentalAgreement = rentalAgreement;
    }

    public void setActive(final boolean isActive) {
        this.isActive = isActive;
    }

    public Set<LicenseStatusValues> getLicenseStatusValuesSet() {
        return licenseStatusValuesSet;
    }

    public void setLicenseStatusValuesSet(final Set<LicenseStatusValues> licenseStatusValuesSet) {
        this.licenseStatusValuesSet = licenseStatusValuesSet;
    }

    public void addLicenseStatusValuesSet(final LicenseStatusValues licenseStatusValues) {
        licenseStatusValuesSet.add(licenseStatusValues);
    }

    public String getLicenseCheckList() {
        return licenseCheckList;
    }

    public void setLicenseCheckList(final String licenseCheckList) {
        this.licenseCheckList = licenseCheckList;
    }

    public LicenseTransfer getLicenseTransfer() {
        return licenseTransfer;
    }

    public void setLicenseTransfer(final LicenseTransfer licenseTransfer) {
        this.licenseTransfer = licenseTransfer;
    }

    public BigDecimal getSwmFee() {
        return swmFee;
    }

    public void setSwmFee(final BigDecimal swmFee) {
        this.swmFee = swmFee;
    }

    public String getDocImageNumber() {
        return docImageNumber;
    }

    public void setDocImageNumber(final String docImageNumber) {
        this.docImageNumber = docImageNumber;
    }

    public String getCompanyPanNumber() {
        return companyPanNumber;
    }

    public void setCompanyPanNumber(final String companyPanNumber) {
        this.companyPanNumber = companyPanNumber;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(final String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getNamePowerOfAttorney() {
        return namePowerOfAttorney;
    }

    public void setNamePowerOfAttorney(final String namePowerOfAttorney) {
        this.namePowerOfAttorney = namePowerOfAttorney;
    }

    public String getFeeExemption() {
        return feeExemption;
    }

    public void setFeeExemption(final String feeExemption) {
        this.feeExemption = feeExemption;
    }

    public BigDecimal getMinSolvency() {
        return minSolvency;
    }

    public void setMinSolvency(final BigDecimal minSolvency) {
        this.minSolvency = minSolvency;
    }

    public BigDecimal getAvgAnnualTurnover() {
        return avgAnnualTurnover;
    }

    public void setAvgAnnualTurnover(final BigDecimal avgAnnualTurnover) {
        this.avgAnnualTurnover = avgAnnualTurnover;
    }

    public String getBankIfscCode() {
        return bankIfscCode;
    }

    public void setBankIfscCode(final String bankIfscCode) {
        this.bankIfscCode = bankIfscCode;
    }

    public String getOfficeEmailId() {
        return officeEmailId;
    }

    public void setOfficeEmailId(final String officeEmailId) {
        this.officeEmailId = officeEmailId;
    }

    public List<LicenseObjection> getObjections() {
        return objections;
    }

    public void setObjections(final List<LicenseObjection> objections) {
        this.objections = objections;
    }

    public BigDecimal getViolationFee() {
        return violationFee;
    }

    public void setViolationFee(final BigDecimal violationFee) {
        this.violationFee = violationFee;
    }

    public String getPropertyNo() {
        return propertyNo;
    }

    public void setPropertyNo(final String propertyNo) {
        this.propertyNo = propertyNo;
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

    public FileStoreMapper getFileStore() {
        return fileStore;
    }

    public void setFileStore(final FileStoreMapper fileStore) {
        this.fileStore = fileStore;
    }
    
    public void updateStatus(final LicenseStatus currentStatus) {
        setStatus(currentStatus);
        final LicenseStatusValues statusValues = new LicenseStatusValues();
        statusValues.setLicenseStatus(currentStatus);
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

}