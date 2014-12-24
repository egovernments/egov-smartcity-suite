/*
 * @(#)License.java 3.0, 29 Jul, 2013 1:24:27 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.entity;

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
import org.egov.infstr.commons.Module;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.models.validator.CheckDateFormat;
import org.egov.infstr.models.validator.OptionalPattern;
import org.egov.infstr.models.validator.Required;
import org.egov.lib.address.model.Address;
import org.egov.lib.admbndry.Boundary;
import org.egov.license.domain.entity.objection.LicenseObjection;
import org.egov.license.domain.entity.transfer.LicenseTransfer;
import org.egov.license.utils.Constants;
import org.egov.license.utils.LicenseUtils;

public abstract class License extends StateAware {

	private static final long serialVersionUID = 1L;
	protected Address address;
	@Required(message = "license.applicationdate.err.required")
	@CheckDateFormat(message = "license.applicationdate.err.properdate")
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

	public abstract String generateApplicationNumber(String runningNumber);

	public abstract String generateLicenseNumber(String runningNumber);

	public abstract void setCreationAndExpiryDate();

	public abstract void setCreationAndExpiryDateForEnterLicense();

	public abstract void updateExpiryDate(Date renewalDate);

	public LicenseDemand getLicenseDemand() {
		return this.licenseDemand;
	}

	public void setLicenseDemand(final LicenseDemand licenseDemand) {
		this.licenseDemand = licenseDemand;
	}

	public License create(final List<FeeMatrix> feeList, final LicenseAppType appType, final NatureOfBusiness nature, final Installment installment, final Set<EgDemandReasonMaster> egDemandReasonMasters, final BigDecimal totalAmount,
			final String runningNumber, final String feeType, final Module module) {
		this.raiseNewDemand(feeList, nature, appType, installment, egDemandReasonMasters, totalAmount, module);
		this.generateApplicationNumber(runningNumber);
		return this;
	}

	public License renew(final List<FeeMatrix> feeList, final LicenseAppType appType, final NatureOfBusiness nature, final Installment installment, final Set<EgDemandReasonMaster> egDemandReasonMasters, final BigDecimal totalAmount,
			final String runningNumber, final String feeType, final Module module, final Date renewalDate) {
		this.raiseDemandForRenewal(feeList, nature, appType, installment, egDemandReasonMasters, totalAmount, module, renewalDate);
		return this;
	}

	public Address getAddress() {
		return this.address;
	}

	public Date getApplicationDate() {
		return this.applicationDate;
	}

	public String getApplicationNumber() {
		return this.applicationNumber;
	}

	public Boundary getBoundary() {
		return this.boundary;
	}

	public String getBuildingType() {
		return this.buildingType;
	}

	public String getCompanyDetails() {
		return this.companyDetails;
	}

	public Date getDateOfCreation() {
		return this.dateOfCreation;
	}

	public Date getDateOfExpiry() {
		return this.dateOfExpiry;
	}

	public BigDecimal getDeduction() {
		return this.deduction;
	}

	public Set<LicenseDemand> getDemandSet() {
		return this.demandSet;
	}

	public String getFeeTypeStr() {
		return this.feeTypeStr;
	}

	public String getInspectionDetails() {
		return this.inspectionDetails;
	}

	public boolean getIsActive() {
		return this.isActive;
	}

	public Licensee getLicensee() {
		return this.licensee;
	}

	public Long getLicenseeZoneId() {
		return this.licenseeZoneId;
	}

	public String getLicenseNumber() {
		return this.licenseNumber;
	}

	public Long getLicenseZoneId() {
		return this.licenseZoneId;
	}

	public String getNameOfEstablishment() {
		return this.nameOfEstablishment;
	}

	public Integer getNoOfRooms() {
		return this.noOfRooms;
	}

	public String getOldLicenseNumber() {
		return this.oldLicenseNumber;
	}

	public BigDecimal getOtherCharges() {
		return this.otherCharges;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public BigDecimal getRentPaid() {
		return this.rentPaid;
	}

	public String getServicetaxNumber() {
		return this.servicetaxNumber;
	}

	public LicenseStatus getStatus() {
		return this.status;
	}

	public String getTempLicenseNumber() {
		return this.tempLicenseNumber;
	}

	public String getTinNumber() {
		return this.tinNumber;
	}

	public SubCategory getTradeName() {
		return this.tradeName;
	}

	public boolean isRentalAgreement() {
		return this.rentalAgreement;
	}

	public void raiseNewDemand(final List<FeeMatrix> feeMatrixList, final NatureOfBusiness nature, final LicenseAppType applType, final Installment installment, final Set<EgDemandReasonMaster> egDemandReasonMasters, final BigDecimal totalAmount,
			final Module module) {
		this.demandSet = new LinkedHashSet<LicenseDemand>();
		final LicenseDemand licenseDemand = new LicenseDemand();
		this.demandSet.add(licenseDemand.createDemand(feeMatrixList, nature, applType, installment, this, egDemandReasonMasters, totalAmount, module));
		this.setDemandSet(this.demandSet);
	}

	public License raiseDemandForRenewal(final List<FeeMatrix> feeMatrixList, final NatureOfBusiness nature, final LicenseAppType applType, final Installment installment, final Set<EgDemandReasonMaster> egDemandReasonMasters, final BigDecimal totalAmount,
			final Module module, final Date renewalDate) {
		List<EgDemandDetails> oldDetails = new ArrayList<EgDemandDetails>();
		for (final LicenseDemand demand : this.getDemandSet()) {
			if (demand.getIsHistory().equalsIgnoreCase("N")) {
				oldDetails = addOldDemandDetailsToCurrent(demand);
			}
			demand.setIsHistory("Y");
		}
		this.getDemandSet().add(new LicenseDemand().renewDemand(feeMatrixList, nature, applType, installment, this, egDemandReasonMasters, totalAmount, module, renewalDate, oldDetails));
		return this;
	}

	public List<EgDemandDetails> addOldDemandDetailsToCurrent(final LicenseDemand demand) {
		// TODO: Code was reviewed by Satyam, No changes required
		final List<EgDemandDetails> oldDetails = new ArrayList<EgDemandDetails>();
		if (demand.getIsHistory().equalsIgnoreCase("N")) {
			for (final EgDemandDetails dd : demand.getEgDemandDetails()) {
				oldDetails.add((EgDemandDetails) dd.clone());
			}
		}
		return oldDetails;
	}

	/**
	 * will give the difference to expiry date if date is passed expiry date then it will give no of months passed with isExpired set to true if date is prioror to the date of expiry then it will give no of months to expire and isExpired set to false
	 * @param date
	 * @return
	 */
	public String getDateDiffToExpiryDate(final Date date) {
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

	public void setActive(final boolean isActive) {
		this.isActive = isActive;
	}

	public void setAddress(final Address address) {
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
		return this.licenseStatusValuesSet;
	}

	public void setLicenseStatusValuesSet(final Set<LicenseStatusValues> licenseStatusValuesSet) {
		this.licenseStatusValuesSet = licenseStatusValuesSet;
	}

	public void addLicenseStatusValuesSet(final LicenseStatusValues licenseStatusValues) {
		this.licenseStatusValuesSet.add(licenseStatusValues);
	}

	public String getLicenseCheckList() {
		return this.licenseCheckList;
	}

	public void setLicenseCheckList(final String licenseCheckList) {
		this.licenseCheckList = licenseCheckList;
	}

	public LicenseTransfer getLicenseTransfer() {
		return this.licenseTransfer;
	}

	public void setLicenseTransfer(final LicenseTransfer licenseTransfer) {
		this.licenseTransfer = licenseTransfer;
	}

	public String getDocNumber() {
		return this.docNumber;
	}

	public void setDocNumber(final String docNumber) {
		this.docNumber = docNumber;
	}

	public BigDecimal getSwmFee() {
		return this.swmFee;
	}

	public void setSwmFee(final BigDecimal swmFee) {
		this.swmFee = swmFee;
	}

	public String getBioMedicalWasteReg() {
		return this.bioMedicalWasteReg;
	}

	public void setBioMedicalWasteReg(final String bioMedicalWasteReg) {
		this.bioMedicalWasteReg = bioMedicalWasteReg;
	}

	public String getDocImageNumber() {
		return this.docImageNumber;
	}

	public void setDocImageNumber(final String docImageNumber) {
		this.docImageNumber = docImageNumber;
	}

	public String getCompanyPanNumber() {
		return this.companyPanNumber;
	}

	public String getVatNumber() {
		return this.vatNumber;
	}

	public String getTypeOfFirm() {
		return this.typeOfFirm;
	}

	public String getNamePowerOfAttorney() {
		return this.namePowerOfAttorney;
	}

	public String getNameSoleProprietor() {
		return this.nameSoleProprietor;
	}

	public String getBankerName() {
		return this.bankerName;
	}

	public String getFeeExemption() {
		return this.feeExemption;
	}

	public BigDecimal getMinSolvency() {
		return this.minSolvency;
	}

	public String getExecuteWorksWorth() {
		return this.executeWorksWorth;
	}

	public void setMinSolvency(final BigDecimal minSolvency) {
		this.minSolvency = minSolvency;
	}

	public void setExecuteWorksWorth(final String executeWorksWorth) {
		this.executeWorksWorth = executeWorksWorth;
	}

	public BigDecimal getAvgAnnualTurnover() {
		return this.avgAnnualTurnover;
	}

	public BigDecimal getCostOfWorksHand() {
		return this.costOfWorksHand;
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
		return this.bankIfscCode;
	}

	public void setBankIfscCode(final String bankIfscCode) {
		this.bankIfscCode = bankIfscCode;
	}

	public Character getIsUpgrade() {
		return this.isUpgrade;
	}

	public void setIsUpgrade(final Character isUpgrade) {
		this.isUpgrade = isUpgrade;
	}

	public String getBankAddress() {
		return this.bankAddress;
	}

	public void setBankAddress(final String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getPlaceOfBusiness() {
		return this.placeOfBusiness;
	}

	public void setPlaceOfBusiness(final String placeOfBusiness) {
		this.placeOfBusiness = placeOfBusiness;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("License={");
		str.append("  licenseNumber=").append(this.licenseNumber == null ? "null" : this.licenseNumber.toString());
		str.append("  dateOfCreation=").append(this.dateOfCreation == null ? "null" : this.dateOfCreation.toString());
		str.append("  dateOfExpiry=").append(this.dateOfExpiry == null ? "null" : this.dateOfExpiry.toString());
		str.append("  applicationNumber=").append(this.applicationNumber == null ? "null" : this.applicationNumber.toString());
		str.append("  applicationDate=").append(this.applicationDate == null ? "null" : this.applicationDate.toString());
		str.append("  address=").append(this.address == null ? "null" : this.address.toString());
		str.append("  servicetaxNumber=").append(this.servicetaxNumber == null ? "null" : this.servicetaxNumber.toString());
		str.append("  isActive=").append(this.isActive);
		str.append("  status=").append(this.status == null ? "null" : this.status.toString());
		str.append("  tinNumber=").append(this.tinNumber == null ? "null" : this.tinNumber.toString());
		str.append("  boundary=").append(this.boundary == null ? "null" : this.boundary.toString());
		str.append("  tradeName=").append(this.tradeName == null ? "null" : this.tradeName.toString());
		str.append("  tempLicenseNumber=").append(this.tempLicenseNumber == null ? "null" : this.tempLicenseNumber.toString());
		str.append("  inspectionDetails=").append(this.inspectionDetails == null ? "null" : this.inspectionDetails.toString());
		str.append("  nameOfEstablishment=").append(this.nameOfEstablishment == null ? "null" : this.nameOfEstablishment.toString());
		str.append("  remarks=").append(this.remarks == null ? "null" : this.remarks.toString());
		str.append("  companyDetails=").append(this.companyDetails == null ? "null" : this.companyDetails.toString());
		str.append("  rentalAgreement=").append(this.rentalAgreement);
		str.append("  rentPaid=").append(this.rentPaid == null ? "null" : this.rentPaid.toString());
		str.append("  buildingType=").append(this.buildingType == null ? "null" : this.buildingType.toString());
		str.append("  licenseDemand=").append(this.licenseDemand == null ? "null" : this.licenseDemand.toString());
		str.append("  licensee=").append(this.licensee == null ? "null" : this.licensee.toString());
		str.append("  phoneNumber=").append(this.phoneNumber == null ? "null" : this.phoneNumber.toString());
		str.append("  noOfRooms=").append(this.noOfRooms == null ? "null" : this.noOfRooms.toString());
		str.append("  oldLicenseNumber=").append(this.oldLicenseNumber == null ? "null" : this.oldLicenseNumber.toString());
		str.append("  otherCharges=").append(this.otherCharges == null ? "null" : this.otherCharges.toString());
		str.append("  deduction=").append(this.deduction == null ? "null" : this.deduction.toString());
		str.append("  licenseZoneId=").append(this.licenseZoneId);
		str.append("  licenseeZoneId=").append(this.licenseeZoneId);
		str.append("  licenseTransfer=").append(this.licenseTransfer == null ? "null" : this.licenseTransfer.toString());
		str.append("  licenseCheckList=").append(this.licenseCheckList == null ? "null" : this.licenseCheckList.toString());
		str.append("  docNumber=").append(this.docNumber == null ? "null" : this.docNumber.toString());
		str.append("  demandSet=").append(this.demandSet == null ? "null" : this.demandSet.toString());
		str.append("  feeTypeStr=").append(this.feeTypeStr == null ? "null" : this.feeTypeStr.toString());
		str.append("  SwmFee=").append(this.swmFee == null ? "null" : this.swmFee.toString());
		str.append("  bioMedicalWasteReg=").append(this.bioMedicalWasteReg == null ? "null" : this.bioMedicalWasteReg.toString());
		str.append("  docImageNumber=").append(this.docImageNumber == null ? "null" : this.docImageNumber.toString());
		str.append("  bankAddress=").append(this.bankAddress == null ? "null" : this.bankAddress.toString());
		str.append("  placeOfBusiness=").append(this.placeOfBusiness == null ? "null" : this.placeOfBusiness.toString());
		str.append("}");
		return str.toString();
	}

	public void updateStatus(final LicenseStatus currentStatus) {
		this.setStatus(currentStatus);
		final LicenseStatusValues statusValues = new LicenseStatusValues();
		statusValues.setLicenseStatus(currentStatus);
	}

	public String getOfficeEmailId() {
		return this.officeEmailId;
	}

	public void setOfficeEmailId(final String officeEmailId) {
		this.officeEmailId = officeEmailId;
	}

	public String getContractorCode() {
		return this.contractorCode;
	}

	public void setContractorCode(final String contractorCode) {
		this.contractorCode = contractorCode;
	}

	public List<LicenseObjection> getObjections() {
		return this.objections;
	}

	public void setObjections(final List<LicenseObjection> objections) {
		this.objections = objections;
	}

	public License getEgLicense() {
		return this.egLicense;
	}

	public void setEgLicense(final License egLicense) {
		this.egLicense = egLicense;
	}

	public LicenseSubType getLicenseSubType() {
		return this.licenseSubType;
	}

	public void setLicenseSubType(final LicenseSubType licenseSubType) {
		this.licenseSubType = licenseSubType;
	}

	public List<LicenseSubType> getLicenseSubTypeList() {
		return this.licenseSubTypeList;
	}

	public void setLicenseSubTypeList(final List<LicenseSubType> licenseSubTypeList) {
		this.licenseSubTypeList = licenseSubTypeList;
	}

	public License acceptTransfer() {
		final String tempApplicationNumber = this.getApplicationNumber();
		this.setApplicationNumber(this.getLicenseTransfer().getOldApplicationNumber());
		this.getLicenseTransfer().setOldApplicationNumber(tempApplicationNumber);

		final String tempApplicantName = this.licensee.getApplicantName();
		this.getLicensee().setApplicantName(this.getLicenseTransfer().getOldApplicantName());
		this.getLicenseTransfer().setOldApplicantName(tempApplicantName);

		final String tempNameOfEstalishment = this.getNameOfEstablishment();
		this.setNameOfEstablishment(this.getLicenseTransfer().getOldNameOfEstablishment());
		this.getLicenseTransfer().setOldNameOfEstablishment(tempNameOfEstalishment);

		final Address tempAddress = this.licensee.getAddress();
		this.getLicensee().setAddress(this.getLicenseTransfer().getOldAddress());
		this.getLicenseTransfer().setOldAddress(tempAddress);

		final Boundary tempBoundary = this.getLicensee().getBoundary();
		this.getLicensee().setBoundary(this.getLicenseTransfer().getBoundary());
		this.getLicenseTransfer().setBoundary(tempBoundary);

		final String tempPhoneNumber = this.getPhoneNumber();
		this.setPhoneNumber(this.getLicenseTransfer().getOldPhoneNumber());
		this.getLicenseTransfer().setOldPhoneNumber(tempPhoneNumber);

		final String tempHomePhoneNumber = this.getLicensee().getPhoneNumber();
		this.getLicensee().setPhoneNumber(this.getLicenseTransfer().getOldHomePhoneNumber());
		this.getLicenseTransfer().setOldHomePhoneNumber(tempHomePhoneNumber);

		final String tempMobilePhoneNumber = this.getLicensee().getMobilePhoneNumber();
		this.getLicensee().setMobilePhoneNumber(this.getLicenseTransfer().getOldMobileNumber());
		this.getLicenseTransfer().setOldMobileNumber(tempMobilePhoneNumber);

		final String tempEmailId = this.getLicensee().getEmailId();
		this.getLicensee().setEmailId(this.getLicenseTransfer().getOldEmailId());
		this.getLicenseTransfer().setOldEmailId(tempEmailId);

		final String tempUniqueId = this.getLicensee().getUid();
		this.getLicensee().setUid(this.getLicenseTransfer().getOldUid());
		this.getLicenseTransfer().setOldUid(tempUniqueId);
		return this;
	}

	public String getWorkflowIdentityForTransfer() {
		final StringBuilder workflowIdentity = new StringBuilder();
		workflowIdentity.append("ApplNo:").append(this.getLicenseTransfer().getOldApplicationNumber());
		workflowIdentity.append(",LicenseNo:").append(this.getLicenseNumber());
		workflowIdentity.append(",LicenseId:").append(this.getId());
		workflowIdentity.append(",LicenseTransferId:").append(this.getLicenseTransfer().getId());
		return workflowIdentity.toString();
	}

	public String getWorkflowIdentityForCreate() {
		final StringBuilder workflowIdentity = new StringBuilder();
		workflowIdentity.append("ApplNo:").append(this.getLicenseTransfer().getOldApplicationNumber());
		workflowIdentity.append(",LicenseId:").append(this.getId());
		return workflowIdentity.toString();
	}

	public String getWorkflowIdentityForModify() {
		final StringBuilder workflowIdentity = new StringBuilder();
		workflowIdentity.append("ApplNo:").append(this.getLicenseTransfer().getOldApplicationNumber());
		workflowIdentity.append(",LicenseNo:").append(this.getLicenseNumber());
		workflowIdentity.append(",LicenseId:").append(this.getId());
		return workflowIdentity.toString();
	}

	public License updateCollectedForExisting(final License license) {

		final EgDemand licenseDemand = license.getCurrentDemand();
		// Installment licIntallment=licenseDemand.getEgInstallmentMaster();
		if (licenseDemand != null) {
			final Set<EgDemandDetails> demanddetails = licenseDemand.getEgDemandDetails();
			BigDecimal tot_amt = BigDecimal.ZERO;
			// TODO: Code was reviewed by Satyam, No changes required
			for (final EgDemandDetails dd : demanddetails) {
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
		for (final EgDemand demand : this.demandSet) {
			if (demand.getIsHistory().equalsIgnoreCase("N")) {
				currentDemand = demand;
				break;
			}
		}
		return currentDemand;
	}

	public boolean isPaid() {
		boolean paid = false;
		final BigDecimal totBal = getTotalBalance();
		if (totBal.equals(BigDecimal.ZERO)) {
			paid = true;
		}
		return paid;
	}

	public boolean isWorkFlowStateRejected() {
		final boolean workFlowStateRejected = this.getState().getValue().contains("Rejected");
		return workFlowStateRejected;
	}

	public BigDecimal getTotalBalance() {
		BigDecimal totBal = BigDecimal.ZERO;

		for (final EgDemand demand : this.demandSet) {
			// TODO: Code was reviewed by Satyam, No changes required
			if (demand.getIsHistory().equals("N")) {
				for (final EgDemandDetails dd : demand.getEgDemandDetails()) {
					if (!(dd.getAmount().subtract(dd.getAmtCollected()).equals(BigDecimal.ZERO))) {
						totBal = totBal.add(dd.getAmount().subtract(dd.getAmtCollected()));
					}
					if (!dd.getAmtRebate().equals(BigDecimal.ZERO)) {
						totBal = totBal.subtract(dd.getAmtRebate());
					}
				}
			}
		}
		return totBal;
	}

	public String getAuditDetails() {
		return "";
	}

}