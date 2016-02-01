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
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.tl.entity.objection.LicenseObjection;
import org.egov.tl.entity.transfer.LicenseTransfer;
import org.egov.tl.utils.Constants;
import org.egov.tl.utils.LicenseUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
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
    //this should be NatureOfBusiness only which means it is Permanent or temporary.
    protected NatureOfBusiness buildingType;
    protected Date dateOfCreation;
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
    protected LicenseSubCategory tradeName;
    protected List<LicenseObjection> objections;
    protected Set<LicenseStatusValues> licenseStatusValuesSet;
    protected LicenseTransfer licenseTransfer;
    protected String licenseCheckList;
    protected BigDecimal deduction;
    protected Set<LicenseDemand> demandSet;
    protected BigDecimal swmFee;
    // PWD
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
    //this field is not received from application
    // this is identified by the screen like is it "New" Apllication or Renewal of Application
    private LicenseAppType licenseAppType;
    private String officeEmailId;
    private String propertyNo;
    private String ownershipType;
    private String address;
    private LicenseCategory category;

    private BigDecimal tradeArea_weight;

    private boolean legacy;
    private Date startDate;
    private Date agreementDate;
    private String agreementDocNo;

    public abstract String generateApplicationNumber(String runningNumber);

    public abstract String generateLicenseNumber(Serializable runningNumber);

    public abstract void setCreationAndExpiryDate();

    public abstract void setCreationAndExpiryDateForEnterLicense();

    public abstract void updateExpiryDate(Date renewalDate);

    public abstract List<LicenseDocument> getDocuments();

    public abstract void setDocuments(List<LicenseDocument> documents);

    public LicenseDemand getLicenseDemand() {
        return this.licenseDemand;
    }

    public void setLicenseDemand(LicenseDemand licenseDemand) {
        this.licenseDemand = licenseDemand;
    }

    public Date getApplicationDate() {
        return this.applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getApplicationNumber() {
        return this.applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public Boundary getBoundary() {
        return this.boundary;
    }

    public void setBoundary(Boundary boundary) {
        this.boundary = boundary;
    }

    public String getCompanyDetails() {
        return this.companyDetails;
    }

    public void setCompanyDetails(String companyDetails) {
        this.companyDetails = companyDetails;
    }

    public Date getDateOfCreation() {
        return this.dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Date getDateOfExpiry() {
        return this.dateOfExpiry;
    }

    public void setDateOfExpiry(Date dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    public BigDecimal getDeduction() {
        return this.deduction;
    }

    public void setDeduction(BigDecimal deduction) {
        this.deduction = deduction;
    }

    public Set<LicenseDemand> getDemandSet() {
        return this.demandSet;
    }

    public void setDemandSet(Set<LicenseDemand> demandSet) {
        this.demandSet = demandSet;
    }

    public String getFeeTypeStr() {
        return this.feeTypeStr;
    }

    public void setFeeTypeStr(String feeTypeStr) {
        this.feeTypeStr = feeTypeStr;
    }

    public String getInspectionDetails() {
        return this.inspectionDetails;
    }

    public void setInspectionDetails(String inspectionDetails) {
        this.inspectionDetails = inspectionDetails;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Licensee getLicensee() {
        return this.licensee;
    }

    public void setLicensee(Licensee licensee) {
        this.licensee = licensee;
    }

    public Long getLicenseeZoneId() {
        return this.licenseeZoneId;
    }

    public void setLicenseeZoneId(Long licenseeZoneId) {
        this.licenseeZoneId = licenseeZoneId;
    }

    public String getLicenseNumber() {
        return this.licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Long getLicenseZoneId() {
        return this.licenseZoneId;
    }

    public void setLicenseZoneId(Long licenseZoneId) {
        this.licenseZoneId = licenseZoneId;
    }

    public String getNameOfEstablishment() {
        return this.nameOfEstablishment;
    }

    public void setNameOfEstablishment(String nameOfEstablishment) {
        this.nameOfEstablishment = nameOfEstablishment;
    }

    public Integer getNoOfRooms() {
        return this.noOfRooms;
    }

    public void setNoOfRooms(Integer noOfRooms) {
        this.noOfRooms = noOfRooms;
    }

    public String getOldLicenseNumber() {
        return this.oldLicenseNumber;
    }

    public void setOldLicenseNumber(String oldLicenseNumber) {
        this.oldLicenseNumber = oldLicenseNumber;
    }

    public BigDecimal getOtherCharges() {
        return this.otherCharges;
    }

    public void setOtherCharges(BigDecimal otherCharges) {
        this.otherCharges = otherCharges;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getRentPaid() {
        return this.rentPaid;
    }

    public void setRentPaid(BigDecimal rentPaid) {
        this.rentPaid = rentPaid;
    }

    public String getServicetaxNumber() {
        return this.servicetaxNumber;
    }

    public void setServicetaxNumber(String servicetaxNumber) {
        this.servicetaxNumber = servicetaxNumber;
    }

    public LicenseStatus getStatus() {
        return this.status;
    }

    public void setStatus(LicenseStatus status) {
        this.status = status;
    }

    public String getTempLicenseNumber() {
        return this.tempLicenseNumber;
    }

    public void setTempLicenseNumber(String tempLicenseNumber) {
        this.tempLicenseNumber = tempLicenseNumber;
    }

    public String getTinNumber() {
        return this.tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public LicenseSubCategory getTradeName() {
        return this.tradeName;
    }

    public void setTradeName(LicenseSubCategory tradeName) {
        this.tradeName = tradeName;
    }

    public boolean isRentalAgreement() {
        return this.rentalAgreement;
    }

    public void setRentalAgreement(boolean rentalAgreement) {
        this.rentalAgreement = rentalAgreement;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Set<LicenseStatusValues> getLicenseStatusValuesSet() {
        return this.licenseStatusValuesSet;
    }

    public void setLicenseStatusValuesSet(Set<LicenseStatusValues> licenseStatusValuesSet) {
        this.licenseStatusValuesSet = licenseStatusValuesSet;
    }

    public void addLicenseStatusValuesSet(LicenseStatusValues licenseStatusValues) {
        this.licenseStatusValuesSet.add(licenseStatusValues);
    }

    public String getLicenseCheckList() {
        return this.licenseCheckList;
    }

    public void setLicenseCheckList(String licenseCheckList) {
        this.licenseCheckList = licenseCheckList;
    }

    public LicenseTransfer getLicenseTransfer() {
        return this.licenseTransfer;
    }

    public void setLicenseTransfer(LicenseTransfer licenseTransfer) {
        this.licenseTransfer = licenseTransfer;
    }

    public BigDecimal getSwmFee() {
        return this.swmFee;
    }

    public void setSwmFee(BigDecimal swmFee) {
        this.swmFee = swmFee;
    }

    public String getDocImageNumber() {
        return this.docImageNumber;
    }

    public void setDocImageNumber(String docImageNumber) {
        this.docImageNumber = docImageNumber;
    }

    public String getCompanyPanNumber() {
        return this.companyPanNumber;
    }

    public void setCompanyPanNumber(String companyPanNumber) {
        this.companyPanNumber = companyPanNumber;
    }

    public String getVatNumber() {
        return this.vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getNamePowerOfAttorney() {
        return this.namePowerOfAttorney;
    }

    public void setNamePowerOfAttorney(String namePowerOfAttorney) {
        this.namePowerOfAttorney = namePowerOfAttorney;
    }

    public String getFeeExemption() {
        return this.feeExemption;
    }

    public void setFeeExemption(String feeExemption) {
        this.feeExemption = feeExemption;
    }

    public BigDecimal getMinSolvency() {
        return this.minSolvency;
    }

    public void setMinSolvency(BigDecimal minSolvency) {
        this.minSolvency = minSolvency;
    }

    public BigDecimal getAvgAnnualTurnover() {
        return this.avgAnnualTurnover;
    }

    public void setAvgAnnualTurnover(BigDecimal avgAnnualTurnover) {
        this.avgAnnualTurnover = avgAnnualTurnover;
    }

    public String getBankIfscCode() {
        return this.bankIfscCode;
    }

    public void setBankIfscCode(String bankIfscCode) {
        this.bankIfscCode = bankIfscCode;
    }

    public String getOfficeEmailId() {
        return this.officeEmailId;
    }

    public void setOfficeEmailId(String officeEmailId) {
        this.officeEmailId = officeEmailId;
    }

    public List<LicenseObjection> getObjections() {
        return this.objections;
    }

    public void setObjections(List<LicenseObjection> objections) {
        this.objections = objections;
    }

    public BigDecimal getViolationFee() {
        return this.violationFee;
    }

    public void setViolationFee(BigDecimal violationFee) {
        this.violationFee = violationFee;
    }

    public String getAuditDetails() {
        return "";
    }

    public String getPropertyNo() {
        return this.propertyNo;
    }

    public void setPropertyNo(String propertyNo) {
        this.propertyNo = propertyNo;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LicenseCategory getCategory() {
        return this.category;
    }

    public void setCategory(LicenseCategory category) {
        this.category = category;
    }

    public String getOwnershipType() {
        return this.ownershipType;
    }

    public void setOwnershipType(String ownershipType) {
        this.ownershipType = ownershipType;
    }

    public BigDecimal getTradeArea_weight() {
        return this.tradeArea_weight;
    }

    public void setTradeArea_weight(BigDecimal tradeArea_weight) {
        this.tradeArea_weight = tradeArea_weight;
    }

    public NatureOfBusiness getBuildingType() {
        return this.buildingType;
    }

    public void setBuildingType(NatureOfBusiness buildingType) {
        this.buildingType = buildingType;
    }

    public LicenseAppType getLicenseAppType() {
        return this.licenseAppType;
    }

    public void setLicenseAppType(LicenseAppType licenseAppType) {
        this.licenseAppType = licenseAppType;
    }

    public boolean isLegacy() {
        return this.legacy;
    }

    public void setLegacy(boolean legacy) {
        this.legacy = legacy;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public License create(List<FeeMatrix> feeList, LicenseAppType appType, NatureOfBusiness nature,
                          Installment installment, Set<EgDemandReasonMaster> egDemandReasonMasters, BigDecimal totalAmount,
                          String runningNumber, String feeType, Module module) {
        this.raiseNewDemand(feeList, nature, appType, installment, egDemandReasonMasters, totalAmount, module);
        this.generateApplicationNumber(runningNumber);
        return this;
    }

    public License renew(List<FeeMatrix> feeList, LicenseAppType appType, NatureOfBusiness nature,
                         Installment installment, Set<EgDemandReasonMaster> egDemandReasonMasters, BigDecimal totalAmount,
                         String runningNumber, String feeType, Module module, Date renewalDate) {
        this.raiseDemandForRenewal(feeList, nature, appType, installment, egDemandReasonMasters, totalAmount, module, renewalDate);
        return this;
    }

    public void raiseNewDemand(List<FeeMatrix> feeMatrixList, NatureOfBusiness nature, LicenseAppType applType,
                               Installment installment, Set<EgDemandReasonMaster> egDemandReasonMasters, BigDecimal totalAmount,
                               Module module) {
        this.demandSet = new LinkedHashSet<LicenseDemand>();
        LicenseDemand licenseDemand = new LicenseDemand();
        this.demandSet.add(licenseDemand.createDemand(feeMatrixList, nature, applType, installment, this, egDemandReasonMasters,
                totalAmount, module));
        this.setDemandSet(this.demandSet);
    }

    public License raiseDemandForRenewal(List<FeeMatrix> feeMatrixList, NatureOfBusiness nature,
                                         LicenseAppType applType, Installment installment, Set<EgDemandReasonMaster> egDemandReasonMasters,
                                         BigDecimal totalAmount, Module module, Date renewalDate) {
        List<EgDemandDetails> oldDetails = new ArrayList<EgDemandDetails>();
        for (LicenseDemand demand : this.getDemandSet()) {
            if (demand.getIsHistory().equalsIgnoreCase("N"))
                oldDetails = this.addOldDemandDetailsToCurrent(demand);
            demand.setIsHistory("Y");
        }
        this.getDemandSet().add(
                new LicenseDemand().renewDemand(feeMatrixList, nature, applType, installment, this, egDemandReasonMasters,
                        totalAmount, module, renewalDate, oldDetails));
        return this;
    }

    public License raiseDemandForViolationFee(Installment installment, License license) {
        for (LicenseDemand demand : this.getDemandSet())
            if (demand.getIsHistory().equalsIgnoreCase("N"))
                this.getDemandSet()
                        .add(demand.setViolationFeeForHawker(installment, license, license.getTradeName().getLicenseType()
                                .getModule()));
        return this;
    }

    public List<EgDemandDetails> addOldDemandDetailsToCurrent(LicenseDemand demand) {
        List<EgDemandDetails> oldDetails = new ArrayList<EgDemandDetails>();
        if (demand.getIsHistory().equalsIgnoreCase("N"))
            for (EgDemandDetails dd : demand.getEgDemandDetails())
                oldDetails.add((EgDemandDetails) dd.clone());
        return oldDetails;
    }

    /**
     * will give the difference to expiry date if date is passed expiry date then it will give no of months passed with isExpired
     * set to true if date is prioror to the date of expiry then it will give no of months to expire and isExpired set to false
     *
     * @param date
     * @return
     */
    public String getDateDiffToExpiryDate(Date date) {
        boolean isExpired = false;
        int monthDiff;
        if (date.after(this.dateOfExpiry)) {
            isExpired = true;
            monthDiff = LicenseUtils.getNumberOfMonths(this.dateOfExpiry, date);
        } else {
            isExpired = false;
            monthDiff = LicenseUtils.getNumberOfMonths(date, this.dateOfExpiry);
        }
        return isExpired + "/" + monthDiff;
    }

    public void updateStatus(LicenseStatus currentStatus) {
        this.setStatus(currentStatus);
        LicenseStatusValues statusValues = new LicenseStatusValues();
        statusValues.setLicenseStatus(currentStatus);
    }

    public License acceptTransfer() {
        String tempApplicationNumber = this.getApplicationNumber();
        this.setApplicationNumber(this.getLicenseTransfer().getOldApplicationNumber());
        this.getLicenseTransfer().setOldApplicationNumber(tempApplicationNumber);

        String tempApplicantName = this.licensee.getApplicantName();
        this.getLicensee().setApplicantName(this.getLicenseTransfer().getOldApplicantName());
        this.getLicenseTransfer().setOldApplicantName(tempApplicantName);

        String tempNameOfEstalishment = this.getNameOfEstablishment();
        this.setNameOfEstablishment(this.getLicenseTransfer().getOldNameOfEstablishment());
        this.getLicenseTransfer().setOldNameOfEstablishment(tempNameOfEstalishment);

        //TODO -- Commented for Phoenix migration
       /* final Address tempAddress = licensee.getAddress();
        //TODO -- Commented for Phoenix migration
        //getLicensee().setAddress(getLicenseTransfer().getOldAddress());
        getLicenseTransfer().setOldAddress(tempAddress);
        */
        Boundary tempBoundary = this.getLicensee().getBoundary();
        this.getLicensee().setBoundary(this.getLicenseTransfer().getBoundary());
        this.getLicenseTransfer().setBoundary(tempBoundary);

        String tempPhoneNumber = this.getPhoneNumber();
        this.setPhoneNumber(this.getLicenseTransfer().getOldPhoneNumber());
        this.getLicenseTransfer().setOldPhoneNumber(tempPhoneNumber);

        String tempHomePhoneNumber = this.getLicensee().getPhoneNumber();
        this.getLicensee().setPhoneNumber(this.getLicenseTransfer().getOldHomePhoneNumber());
        this.getLicenseTransfer().setOldHomePhoneNumber(tempHomePhoneNumber);

        String tempMobilePhoneNumber = this.getLicensee().getMobilePhoneNumber();
        this.getLicensee().setMobilePhoneNumber(this.getLicenseTransfer().getOldMobileNumber());
        this.getLicenseTransfer().setOldMobileNumber(tempMobilePhoneNumber);

        String tempEmailId = this.getLicensee().getEmailId();
        this.getLicensee().setEmailId(this.getLicenseTransfer().getOldEmailId());
        this.getLicenseTransfer().setOldEmailId(tempEmailId);

        String tempUniqueId = this.getLicensee().getUid();
        this.getLicensee().setUid(this.getLicenseTransfer().getOldUid());
        this.getLicenseTransfer().setOldUid(tempUniqueId);
        return this;
    }

    public String getWorkflowIdentityForTransfer() {
        StringBuilder workflowIdentity = new StringBuilder();
        workflowIdentity.append("ApplNo:").append(this.getLicenseTransfer().getOldApplicationNumber());
        workflowIdentity.append(",LicenseNo:").append(this.getLicenseNumber());
        workflowIdentity.append(",LicenseId:").append(this.getId());
        workflowIdentity.append(",LicenseTransferId:").append(this.getLicenseTransfer().getId());
        return workflowIdentity.toString();
    }

    public String getWorkflowIdentityForCreate() {
        StringBuilder workflowIdentity = new StringBuilder();
        workflowIdentity.append("ApplNo:").append(this.getLicenseTransfer().getOldApplicationNumber());
        workflowIdentity.append(",LicenseId:").append(this.getId());
        return workflowIdentity.toString();
    }

    public String getWorkflowIdentityForModify() {
        StringBuilder workflowIdentity = new StringBuilder();
        workflowIdentity.append("ApplNo:").append(this.getLicenseTransfer().getOldApplicationNumber());
        workflowIdentity.append(",LicenseNo:").append(this.getLicenseNumber());
        workflowIdentity.append(",LicenseId:").append(this.getId());
        return workflowIdentity.toString();
    }

    public License updateCollectedForExisting(License license) {

        EgDemand licenseDemand = license.getCurrentDemand();
        // Installment licIntallment=licenseDemand.getEgInstallmentMaster();
        if (licenseDemand != null) {
            Set<EgDemandDetails> demanddetails = licenseDemand.getEgDemandDetails();
            BigDecimal tot_amt = BigDecimal.ZERO;
            for (EgDemandDetails dd : demanddetails) {
                BigDecimal demandAmount = dd.getAmount().subtract(dd.getAmtRebate());
                tot_amt = tot_amt.add(demandAmount);
                dd.setAmtCollected(demandAmount);
            }
            licenseDemand.setAmtCollected(tot_amt);
        }

        return this;
    }

    public EgDemand getCurrentDemand() {
        EgDemand currentDemand = null;
        for (EgDemand demand : this.demandSet)
            if (demand.getIsHistory().equalsIgnoreCase("N")) {
                currentDemand = demand;
                break;
            }
        return currentDemand;
    }

    public boolean isPaid() {
        return this.getTotalBalance().equals(BigDecimal.ZERO);
    }

    public boolean isViolationFeePending() {
        boolean paid = false;
        BigDecimal totBal = BigDecimal.ZERO;
        for (EgDemand demand : this.demandSet)
            if (demand.getIsHistory().equals("N"))
                for (EgDemandDetails dd : demand.getEgDemandDetails())
                    if (dd.getEgDemandReason().getEgDemandReasonMaster().getCode().equals(Constants.VIOLATION_FEE_DEMAND_REASON)) {
                        if (!dd.getAmount().subtract(dd.getAmtCollected()).equals(BigDecimal.ZERO))
                            totBal = totBal.add(dd.getAmount().subtract(dd.getAmtCollected()));
                        if (!totBal.equals(BigDecimal.ZERO))
                            paid = true;
                    }
        return paid;
    }

    public boolean isWorkFlowStateRejected() {
        boolean workFlowStateRejected = false;
        if (this.getState() != null && this.getState().getValue().contains("Rejected"))
            workFlowStateRejected = this.getState().getValue().contains("Rejected");
        return workFlowStateRejected;
    }

    public BigDecimal getTotalBalance() {
        BigDecimal totBal = BigDecimal.ZERO;

        for (EgDemand demand : this.demandSet)
            if (demand.getIsHistory().equals("N"))
                for (EgDemandDetails dd : demand.getEgDemandDetails()) {
                    if (!dd.getAmount().subtract(dd.getAmtCollected()).equals(BigDecimal.ZERO))
                        totBal = totBal.add(dd.getAmount().subtract(dd.getAmtCollected()));
                    if (!dd.getAmtRebate().equals(BigDecimal.ZERO))
                        totBal = totBal.subtract(dd.getAmtRebate());
                }
        return totBal;
    }

    public EgwStatus getEgwStatus() {
        return egwStatus;
    }

    public void setEgwStatus(EgwStatus egwStatus) {
        this.egwStatus = egwStatus;
    }

    public Boundary getParentBoundary() {
        return parentBoundary;
    }

    public void setParentBoundary(Boundary parentBoundary) {
        this.parentBoundary = parentBoundary;
    }

    public Date getAgreementDate() {
        return agreementDate;
    }

    public void setAgreementDate(Date agreementDate) {
        this.agreementDate = agreementDate;
    }

    public String getAgreementDocNo() {
        return agreementDocNo;
    }

    public void setAgreementDocNo(String agreementDocNo) {
        this.agreementDocNo = agreementDocNo;
    }
    
}
