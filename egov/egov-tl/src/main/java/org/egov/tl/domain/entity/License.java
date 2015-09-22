/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) <2015>  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *  	1) All versions of this program, verbatim or modified must carry this
 *  	   Legal Notice.
 *
 *  	2) Any misrepresentation of the origin of the material is prohibited. It
 *  	   is required that all modified versions of this material be marked in
 *  	   reasonable ways as different from the original version.
 *
 *  	3) This license does not grant any rights to any user of the program
 *  	   with regards to rights under trademark law for use of the trade names
 *  	   or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.tl.domain.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.egov.commons.Installment;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.persistence.entity.PermanentAddress;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.tl.domain.entity.objection.LicenseObjection;
import org.egov.tl.domain.entity.transfer.LicenseTransfer;
import org.egov.tl.utils.Constants;
import org.egov.tl.utils.LicenseUtils;

public abstract class License extends StateAware {

    private static final long serialVersionUID = 1L;
    protected PermanentAddress address;
    @Required(message = "license.applicationdate.err.required")
    @ValidateDate(message = "license.applicationdate.err.properdate", allowPast = true, dateFormat = "dd/MM/yyyy")
    protected Date applicationDate;
    protected String applicationNumber;
    protected Boundary boundary;
    protected String buildingType;
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
    protected SubCategory tradeName;
    protected List<LicenseObjection> objections;
    protected Set<LicenseStatusValues> licenseStatusValuesSet;
    protected LicenseTransfer licenseTransfer;
    protected String licenseCheckList;
    protected String docNumber;
    protected BigDecimal deduction;
    protected Set<LicenseDemand> demandSet;
    protected BigDecimal swmFee;
    protected String bioMedicalWasteReg;
    private String docImageNumber;

    // PWD
    protected String servicetaxNumber;
    @OptionalPattern(regex = Constants.alphaNumericwithspecialchar, message = "license.tin.number.alphaNumeric")
    protected String tinNumber;
    protected String companyPanNumber;
    protected String vatNumber;
    protected String companyDetails;
    protected String typeOfFirm;
    protected String namePowerOfAttorney;
    protected String nameSoleProprietor;
    protected String bankerName;
    protected String bankIfscCode;
    protected BigDecimal minSolvency;
    protected String executeWorksWorth;
    protected BigDecimal avgAnnualTurnover;
    protected BigDecimal costOfWorksHand;
    protected String feeExemption;
    private Character isUpgrade;
    private License egLicense;
    private String bankAddress;
    private String placeOfBusiness;
    private LicenseSubType licenseSubType;
    private List<LicenseSubType> licenseSubTypeList;
    private String officeEmailId;
    private String contractorCode;
    protected BigDecimal violationFee;

    public abstract String generateApplicationNumber(String runningNumber);

    public abstract String generateLicenseNumber(String runningNumber);

    public abstract void setCreationAndExpiryDate();

    public abstract void setCreationAndExpiryDateForEnterLicense();

    public abstract void updateExpiryDate(Date renewalDate);
    
    public abstract List<LicenseDocument> getDocuments();

    public abstract void setDocuments(final List<LicenseDocument> documents);

    public LicenseDemand getLicenseDemand() {
        return licenseDemand;
    }

    public void setLicenseDemand(final LicenseDemand licenseDemand) {
        this.licenseDemand = licenseDemand;
    }

    public License create(final List<FeeMatrix> feeList, final LicenseAppType appType, final NatureOfBusiness nature,
            final Installment installment, final Set<EgDemandReasonMaster> egDemandReasonMasters, final BigDecimal totalAmount,
            final String runningNumber, final String feeType, final Module module) {
        raiseNewDemand(feeList, nature, appType, installment, egDemandReasonMasters, totalAmount, module);
        generateApplicationNumber(runningNumber);
        return this;
    }

    public License renew(final List<FeeMatrix> feeList, final LicenseAppType appType, final NatureOfBusiness nature,
            final Installment installment, final Set<EgDemandReasonMaster> egDemandReasonMasters, final BigDecimal totalAmount,
            final String runningNumber, final String feeType, final Module module, final Date renewalDate) {
        raiseDemandForRenewal(feeList, nature, appType, installment, egDemandReasonMasters, totalAmount, module, renewalDate);
        return this;
    }

    public PermanentAddress getAddress() {
        return address;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public String getBuildingType() {
        return buildingType;
    }

    public String getCompanyDetails() {
        return companyDetails;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public Date getDateOfExpiry() {
        return dateOfExpiry;
    }

    public BigDecimal getDeduction() {
        return deduction;
    }

    public Set<LicenseDemand> getDemandSet() {
        return demandSet;
    }

    public String getFeeTypeStr() {
        return feeTypeStr;
    }

    public String getInspectionDetails() {
        return inspectionDetails;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public Licensee getLicensee() {
        return licensee;
    }

    public Long getLicenseeZoneId() {
        return licenseeZoneId;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public Long getLicenseZoneId() {
        return licenseZoneId;
    }

    public String getNameOfEstablishment() {
        return nameOfEstablishment;
    }

    public Integer getNoOfRooms() {
        return noOfRooms;
    }

    public String getOldLicenseNumber() {
        return oldLicenseNumber;
    }

    public BigDecimal getOtherCharges() {
        return otherCharges;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public BigDecimal getRentPaid() {
        return rentPaid;
    }

    public String getServicetaxNumber() {
        return servicetaxNumber;
    }

    public LicenseStatus getStatus() {
        return status;
    }

    public String getTempLicenseNumber() {
        return tempLicenseNumber;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public SubCategory getTradeName() {
        return tradeName;
    }

    public boolean isRentalAgreement() {
        return rentalAgreement;
    }

    public void raiseNewDemand(final List<FeeMatrix> feeMatrixList, final NatureOfBusiness nature, final LicenseAppType applType,
            final Installment installment, final Set<EgDemandReasonMaster> egDemandReasonMasters, final BigDecimal totalAmount,
            final Module module) {
        demandSet = new LinkedHashSet<LicenseDemand>();
        final LicenseDemand licenseDemand = new LicenseDemand();
        demandSet.add(licenseDemand.createDemand(feeMatrixList, nature, applType, installment, this, egDemandReasonMasters,
                totalAmount, module));
        setDemandSet(demandSet);
    }

    public License raiseDemandForRenewal(final List<FeeMatrix> feeMatrixList, final NatureOfBusiness nature,
            final LicenseAppType applType, final Installment installment, final Set<EgDemandReasonMaster> egDemandReasonMasters,
            final BigDecimal totalAmount, final Module module, final Date renewalDate) {
        List<EgDemandDetails> oldDetails = new ArrayList<EgDemandDetails>();
        for (final LicenseDemand demand : getDemandSet()) {
            if (demand.getIsHistory().equalsIgnoreCase("N"))
                oldDetails = addOldDemandDetailsToCurrent(demand);
            demand.setIsHistory("Y");
        }
        getDemandSet().add(
                new LicenseDemand().renewDemand(feeMatrixList, nature, applType, installment, this, egDemandReasonMasters,
                        totalAmount, module, renewalDate, oldDetails));
        return this;
    }

    public License raiseDemandForViolationFee(final Installment installment, final License license)
    {
        for (final LicenseDemand demand : getDemandSet())
            if (demand.getIsHistory().equalsIgnoreCase("N"))
                getDemandSet()
                        .add(demand.setViolationFeeForHawker(installment, license, license.getTradeName().getLicenseType()
                                .getModule()));
        return this;
    }

    public List<EgDemandDetails> addOldDemandDetailsToCurrent(final LicenseDemand demand) {
        final List<EgDemandDetails> oldDetails = new ArrayList<EgDemandDetails>();
        if (demand.getIsHistory().equalsIgnoreCase("N"))
            for (final EgDemandDetails dd : demand.getEgDemandDetails())
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
    public String getDateDiffToExpiryDate(final Date date) {
        boolean isExpired = false;
        int monthDiff;
        if (date.after(dateOfExpiry)) {
            isExpired = true;
            monthDiff = LicenseUtils.getNumberOfMonths(dateOfExpiry, date);
        } else {
            isExpired = false;
            monthDiff = LicenseUtils.getNumberOfMonths(date, dateOfExpiry);
        }
        return isExpired + "/" + monthDiff;
    }

    public void setActive(final boolean isActive) {
        this.isActive = isActive;
    }

    public void setAddress(final PermanentAddress address) {
        this.address = address;
    }

    public void setApplicationDate(final Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public void setApplicationNumber(final String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public void setBoundary(final Boundary boundary) {
        this.boundary = boundary;
    }

    public void setBuildingType(final String buildingType) {
        this.buildingType = buildingType;
    }

    public void setCompanyDetails(final String companyDetails) {
        this.companyDetails = companyDetails;
    }

    public void setDateOfCreation(final Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public void setDateOfExpiry(final Date dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    public void setDeduction(final BigDecimal deduction) {
        this.deduction = deduction;
    }

    public void setDemandSet(final Set<LicenseDemand> demandSet) {
        this.demandSet = demandSet;
    }

    public void setFeeTypeStr(final String feeTypeStr) {
        this.feeTypeStr = feeTypeStr;
    }

    public void setInspectionDetails(final String inspectionDetails) {
        this.inspectionDetails = inspectionDetails;
    }

    public void setIsActive(final boolean isActive) {
        this.isActive = isActive;
    }

    public void setLicensee(final Licensee licensee) {
        this.licensee = licensee;
    }

    public void setLicenseeZoneId(final Long licenseeZoneId) {
        this.licenseeZoneId = licenseeZoneId;
    }

    public void setLicenseNumber(final String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public void setLicenseZoneId(final Long licenseZoneId) {
        this.licenseZoneId = licenseZoneId;
    }

    public void setNameOfEstablishment(final String nameOfEstablishment) {
        this.nameOfEstablishment = nameOfEstablishment;
    }

    public void setNoOfRooms(final Integer noOfRooms) {
        this.noOfRooms = noOfRooms;
    }

    public void setOldLicenseNumber(final String oldLicenseNumber) {
        this.oldLicenseNumber = oldLicenseNumber;
    }

    public void setOtherCharges(final BigDecimal otherCharges) {
        this.otherCharges = otherCharges;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public void setRentalAgreement(final boolean rentalAgreement) {
        this.rentalAgreement = rentalAgreement;
    }

    public void setRentPaid(final BigDecimal rentPaid) {
        this.rentPaid = rentPaid;
    }

    public void setServicetaxNumber(final String servicetaxNumber) {
        this.servicetaxNumber = servicetaxNumber;
    }

    public void setStatus(final LicenseStatus status) {
        this.status = status;
    }

    public void setTempLicenseNumber(final String tempLicenseNumber) {
        this.tempLicenseNumber = tempLicenseNumber;
    }

    public void setTinNumber(final String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public void setTradeName(final SubCategory tradeName) {
        this.tradeName = tradeName;
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

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(final String docNumber) {
        this.docNumber = docNumber;
    }

    public BigDecimal getSwmFee() {
        return swmFee;
    }

    public void setSwmFee(final BigDecimal swmFee) {
        this.swmFee = swmFee;
    }

    public String getBioMedicalWasteReg() {
        return bioMedicalWasteReg;
    }

    public void setBioMedicalWasteReg(final String bioMedicalWasteReg) {
        this.bioMedicalWasteReg = bioMedicalWasteReg;
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

    public String getVatNumber() {
        return vatNumber;
    }

    public String getTypeOfFirm() {
        return typeOfFirm;
    }

    public String getNamePowerOfAttorney() {
        return namePowerOfAttorney;
    }

    public String getNameSoleProprietor() {
        return nameSoleProprietor;
    }

    public String getBankerName() {
        return bankerName;
    }

    public String getFeeExemption() {
        return feeExemption;
    }

    public BigDecimal getMinSolvency() {
        return minSolvency;
    }

    public String getExecuteWorksWorth() {
        return executeWorksWorth;
    }

    public void setMinSolvency(final BigDecimal minSolvency) {
        this.minSolvency = minSolvency;
    }

    public void setExecuteWorksWorth(final String executeWorksWorth) {
        this.executeWorksWorth = executeWorksWorth;
    }

    public BigDecimal getAvgAnnualTurnover() {
        return avgAnnualTurnover;
    }

    public BigDecimal getCostOfWorksHand() {
        return costOfWorksHand;
    }

    public void setAvgAnnualTurnover(final BigDecimal avgAnnualTurnover) {
        this.avgAnnualTurnover = avgAnnualTurnover;
    }

    public void setCostOfWorksHand(final BigDecimal costOfWorksHand) {
        this.costOfWorksHand = costOfWorksHand;
    }

    public void setCompanyPanNumber(final String companyPanNumber) {
        this.companyPanNumber = companyPanNumber;
    }

    public void setVatNumber(final String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public void setTypeOfFirm(final String typeOfFirm) {
        this.typeOfFirm = typeOfFirm;
    }

    public void setNamePowerOfAttorney(final String namePowerOfAttorney) {
        this.namePowerOfAttorney = namePowerOfAttorney;
    }

    public void setNameSoleProprietor(final String nameSoleProprietor) {
        this.nameSoleProprietor = nameSoleProprietor;
    }

    public void setBankerName(final String bankerName) {
        this.bankerName = bankerName;
    }

    public void setFeeExemption(final String feeExemption) {
        this.feeExemption = feeExemption;
    }

    public String getBankIfscCode() {
        return bankIfscCode;
    }

    public void setBankIfscCode(final String bankIfscCode) {
        this.bankIfscCode = bankIfscCode;
    }

    public Character getIsUpgrade() {
        return isUpgrade;
    }

    public void setIsUpgrade(final Character isUpgrade) {
        this.isUpgrade = isUpgrade;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(final String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getPlaceOfBusiness() {
        return placeOfBusiness;
    }

    public void setPlaceOfBusiness(final String placeOfBusiness) {
        this.placeOfBusiness = placeOfBusiness;
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("License={");
        str.append("  licenseNumber=").append(licenseNumber == null ? "null" : licenseNumber.toString());
        str.append("  dateOfCreation=").append(dateOfCreation == null ? "null" : dateOfCreation.toString());
        str.append("  dateOfExpiry=").append(dateOfExpiry == null ? "null" : dateOfExpiry.toString());
        str.append("  applicationNumber=").append(applicationNumber == null ? "null" : applicationNumber.toString());
        str.append("  applicationDate=").append(applicationDate == null ? "null" : applicationDate.toString());
        str.append("  address=").append(address == null ? "null" : address.toString());
        str.append("  servicetaxNumber=").append(servicetaxNumber == null ? "null" : servicetaxNumber.toString());
        str.append("  isActive=").append(isActive);
        str.append("  status=").append(status == null ? "null" : status.toString());
        str.append("  tinNumber=").append(tinNumber == null ? "null" : tinNumber.toString());
        str.append("  boundary=").append(boundary == null ? "null" : boundary.toString());
        str.append("  tradeName=").append(tradeName == null ? "null" : tradeName.toString());
        str.append("  tempLicenseNumber=").append(tempLicenseNumber == null ? "null" : tempLicenseNumber.toString());
        str.append("  inspectionDetails=").append(inspectionDetails == null ? "null" : inspectionDetails.toString());
        str.append("  nameOfEstablishment=").append(nameOfEstablishment == null ? "null" : nameOfEstablishment.toString());
        str.append("  remarks=").append(remarks == null ? "null" : remarks.toString());
        str.append("  companyDetails=").append(companyDetails == null ? "null" : companyDetails.toString());
        str.append("  rentalAgreement=").append(rentalAgreement);
        str.append("  rentPaid=").append(rentPaid == null ? "null" : rentPaid.toString());
        str.append("  buildingType=").append(buildingType == null ? "null" : buildingType.toString());
        str.append("  licenseDemand=").append(licenseDemand == null ? "null" : licenseDemand.toString());
        str.append("  licensee=").append(licensee == null ? "null" : licensee.toString());
        str.append("  phoneNumber=").append(phoneNumber == null ? "null" : phoneNumber.toString());
        str.append("  noOfRooms=").append(noOfRooms == null ? "null" : noOfRooms.toString());
        str.append("  oldLicenseNumber=").append(oldLicenseNumber == null ? "null" : oldLicenseNumber.toString());
        str.append("  otherCharges=").append(otherCharges == null ? "null" : otherCharges.toString());
        str.append("  deduction=").append(deduction == null ? "null" : deduction.toString());
        str.append("  licenseZoneId=").append(licenseZoneId);
        str.append("  licenseeZoneId=").append(licenseeZoneId);
        str.append("  licenseTransfer=").append(licenseTransfer == null ? "null" : licenseTransfer.toString());
        str.append("  licenseCheckList=").append(licenseCheckList == null ? "null" : licenseCheckList.toString());
        str.append("  docNumber=").append(docNumber == null ? "null" : docNumber.toString());
        str.append("  demandSet=").append(demandSet == null ? "null" : demandSet.toString());
        str.append("  feeTypeStr=").append(feeTypeStr == null ? "null" : feeTypeStr.toString());
        str.append("  SwmFee=").append(swmFee == null ? "null" : swmFee.toString());
        str.append("  bioMedicalWasteReg=").append(bioMedicalWasteReg == null ? "null" : bioMedicalWasteReg.toString());
        str.append("  docImageNumber=").append(docImageNumber == null ? "null" : docImageNumber.toString());
        str.append("  bankAddress=").append(bankAddress == null ? "null" : bankAddress.toString());
        str.append("  placeOfBusiness=").append(placeOfBusiness == null ? "null" : placeOfBusiness.toString());
        str.append("}");
        return str.toString();
    }

    public void updateStatus(final LicenseStatus currentStatus) {
        setStatus(currentStatus);
        final LicenseStatusValues statusValues = new LicenseStatusValues();
        statusValues.setLicenseStatus(currentStatus);
    }

    public String getOfficeEmailId() {
        return officeEmailId;
    }

    public void setOfficeEmailId(final String officeEmailId) {
        this.officeEmailId = officeEmailId;
    }

    public String getContractorCode() {
        return contractorCode;
    }

    public void setContractorCode(final String contractorCode) {
        this.contractorCode = contractorCode;
    }

    public List<LicenseObjection> getObjections() {
        return objections;
    }

    public void setObjections(final List<LicenseObjection> objections) {
        this.objections = objections;
    }

    public License getEgLicense() {
        return egLicense;
    }

    public void setEgLicense(final License egLicense) {
        this.egLicense = egLicense;
    }

    public LicenseSubType getLicenseSubType() {
        return licenseSubType;
    }

    public void setLicenseSubType(final LicenseSubType licenseSubType) {
        this.licenseSubType = licenseSubType;
    }

    public List<LicenseSubType> getLicenseSubTypeList() {
        return licenseSubTypeList;
    }

    public void setLicenseSubTypeList(final List<LicenseSubType> licenseSubTypeList) {
        this.licenseSubTypeList = licenseSubTypeList;
    }

    public BigDecimal getViolationFee() {
        return violationFee;
    }

    public void setViolationFee(final BigDecimal violationFee) {
        this.violationFee = violationFee;
    }

    public License acceptTransfer() {
        final String tempApplicationNumber = getApplicationNumber();
        setApplicationNumber(getLicenseTransfer().getOldApplicationNumber());
        getLicenseTransfer().setOldApplicationNumber(tempApplicationNumber);

        final String tempApplicantName = licensee.getApplicantName();
        getLicensee().setApplicantName(getLicenseTransfer().getOldApplicantName());
        getLicenseTransfer().setOldApplicantName(tempApplicantName);

        final String tempNameOfEstalishment = getNameOfEstablishment();
        setNameOfEstablishment(getLicenseTransfer().getOldNameOfEstablishment());
        getLicenseTransfer().setOldNameOfEstablishment(tempNameOfEstalishment);

        final Address tempAddress = licensee.getAddress();
        //TODO -- Commented for Phoenix migration
        //getLicensee().setAddress(getLicenseTransfer().getOldAddress());*/
        getLicenseTransfer().setOldAddress(tempAddress);

        final Boundary tempBoundary = getLicensee().getBoundary();
        getLicensee().setBoundary(getLicenseTransfer().getBoundary());
        getLicenseTransfer().setBoundary(tempBoundary);

        final String tempPhoneNumber = getPhoneNumber();
        setPhoneNumber(getLicenseTransfer().getOldPhoneNumber());
        getLicenseTransfer().setOldPhoneNumber(tempPhoneNumber);

        final String tempHomePhoneNumber = getLicensee().getPhoneNumber();
        getLicensee().setPhoneNumber(getLicenseTransfer().getOldHomePhoneNumber());
        getLicenseTransfer().setOldHomePhoneNumber(tempHomePhoneNumber);

        final String tempMobilePhoneNumber = getLicensee().getMobilePhoneNumber();
        getLicensee().setMobilePhoneNumber(getLicenseTransfer().getOldMobileNumber());
        getLicenseTransfer().setOldMobileNumber(tempMobilePhoneNumber);

        final String tempEmailId = getLicensee().getEmailId();
        getLicensee().setEmailId(getLicenseTransfer().getOldEmailId());
        getLicenseTransfer().setOldEmailId(tempEmailId);

        final String tempUniqueId = getLicensee().getUid();
        getLicensee().setUid(getLicenseTransfer().getOldUid());
        getLicenseTransfer().setOldUid(tempUniqueId);
        return this;
    }

    public String getWorkflowIdentityForTransfer() {
        final StringBuilder workflowIdentity = new StringBuilder();
        workflowIdentity.append("ApplNo:").append(getLicenseTransfer().getOldApplicationNumber());
        workflowIdentity.append(",LicenseNo:").append(getLicenseNumber());
        workflowIdentity.append(",LicenseId:").append(getId());
        workflowIdentity.append(",LicenseTransferId:").append(getLicenseTransfer().getId());
        return workflowIdentity.toString();
    }

    public String getWorkflowIdentityForCreate() {
        final StringBuilder workflowIdentity = new StringBuilder();
        workflowIdentity.append("ApplNo:").append(getLicenseTransfer().getOldApplicationNumber());
        workflowIdentity.append(",LicenseId:").append(getId());
        return workflowIdentity.toString();
    }

    public String getWorkflowIdentityForModify() {
        final StringBuilder workflowIdentity = new StringBuilder();
        workflowIdentity.append("ApplNo:").append(getLicenseTransfer().getOldApplicationNumber());
        workflowIdentity.append(",LicenseNo:").append(getLicenseNumber());
        workflowIdentity.append(",LicenseId:").append(getId());
        return workflowIdentity.toString();
    }

    public License updateCollectedForExisting(final License license) {

        final EgDemand licenseDemand = license.getCurrentDemand();
        // Installment licIntallment=licenseDemand.getEgInstallmentMaster();
        if (licenseDemand != null)
        {
            final Set<EgDemandDetails> demanddetails = licenseDemand.getEgDemandDetails();
            BigDecimal tot_amt = BigDecimal.ZERO;
            for (final EgDemandDetails dd : demanddetails)
            {
                final BigDecimal demandAmount = dd.getAmount().subtract(dd.getAmtRebate());
                tot_amt = tot_amt.add(demandAmount);
                dd.setAmtCollected(demandAmount);
            }
            licenseDemand.setAmtCollected(tot_amt);
        }

        return this;
    }

    public EgDemand getCurrentDemand() {
        EgDemand currentDemand = null;
        for (final EgDemand demand : demandSet)
            if (demand.getIsHistory().equalsIgnoreCase("N")) {
                currentDemand = demand;
                break;
            }
        return currentDemand;
    }

    public boolean isPaid() {
        boolean paid = false;
        final BigDecimal totBal = getTotalBalance();
        if (totBal.equals(BigDecimal.ZERO))
            paid = true;
        return paid;
    }

    public boolean isViolationFeePending() {
        boolean paid = false;
        BigDecimal totBal = BigDecimal.ZERO;
        for (final EgDemand demand : demandSet)
            if (demand.getIsHistory().equals("N"))
                for (final EgDemandDetails dd : demand.getEgDemandDetails())
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
        if (getState() != null && getState().getValue().contains("Rejected"))
            workFlowStateRejected = getState().getValue().contains("Rejected");
        return workFlowStateRejected;
    }

    public BigDecimal getTotalBalance()
    {
        BigDecimal totBal = BigDecimal.ZERO;

        for (final EgDemand demand : demandSet)
            if (demand.getIsHistory().equals("N"))
                for (final EgDemandDetails dd : demand.getEgDemandDetails()) {
                    if (!dd.getAmount().subtract(dd.getAmtCollected()).equals(BigDecimal.ZERO))
                        totBal = totBal.add(dd.getAmount().subtract(dd.getAmtCollected()));
                    if (!dd.getAmtRebate().equals(BigDecimal.ZERO))
                        totBal = totBal.subtract(dd.getAmtRebate());
                }
        return totBal;
    }

    public String getAuditDetails() {
        return "";
    }
}
