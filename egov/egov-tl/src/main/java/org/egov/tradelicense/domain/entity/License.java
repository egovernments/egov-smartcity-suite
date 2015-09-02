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
package org.egov.tradelicense.domain.entity;

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
import org.egov.tradelicense.domain.entity.objection.LicenseObjection;
import org.egov.tradelicense.domain.entity.transfer.LicenseTransfer;
import org.egov.tradelicense.utils.Constants;
import org.egov.tradelicense.utils.LicenseUtils;

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
	
	//PWD
	protected String servicetaxNumber;
	@OptionalPattern(regex=Constants.alphaNumericwithspecialchar,message="license.tin.number.alphaNumeric") 
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

	public LicenseDemand getLicenseDemand() {
		return this.licenseDemand;
	}

	public void setLicenseDemand(LicenseDemand licenseDemand) {
		this.licenseDemand = licenseDemand;
	}
	
	public License create(List<FeeMatrix> feeList, LicenseAppType appType, NatureOfBusiness nature, Installment installment, Set<EgDemandReasonMaster> egDemandReasonMasters, BigDecimal totalAmount, String runningNumber, String feeType, Module module) {
		this.raiseNewDemand(feeList, nature, appType, installment, egDemandReasonMasters, totalAmount, module);
		this.generateApplicationNumber(runningNumber);
		return this;
	}

	public License renew(List<FeeMatrix> feeList, LicenseAppType appType, NatureOfBusiness nature, Installment installment, Set<EgDemandReasonMaster> egDemandReasonMasters, BigDecimal totalAmount, String runningNumber, String feeType, Module module, Date renewalDate) {
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
	
	public void raiseNewDemand(List<FeeMatrix> feeMatrixList, NatureOfBusiness nature, LicenseAppType applType, Installment installment, Set<EgDemandReasonMaster> egDemandReasonMasters, BigDecimal totalAmount, Module module) {
		this.demandSet = new LinkedHashSet<LicenseDemand>();
		final LicenseDemand licenseDemand = new LicenseDemand();
		this.demandSet.add(licenseDemand.createDemand(feeMatrixList, nature, applType, installment, this, egDemandReasonMasters, totalAmount, module));
		this.setDemandSet(this.demandSet);
	}

	public License raiseDemandForRenewal(List<FeeMatrix> feeMatrixList, NatureOfBusiness nature, LicenseAppType applType, Installment installment, Set<EgDemandReasonMaster> egDemandReasonMasters, BigDecimal totalAmount, Module module, Date renewalDate) {
		List<EgDemandDetails> oldDetails=new ArrayList<EgDemandDetails>();
		for (final LicenseDemand demand : this.getDemandSet()) {
			if(demand.getIsHistory().equalsIgnoreCase("N"))
			{
				oldDetails= addOldDemandDetailsToCurrent(demand);
			}
			demand.setIsHistory("Y");
		}     
		this.getDemandSet().add(new LicenseDemand().renewDemand(feeMatrixList, nature, applType, installment, this, egDemandReasonMasters, totalAmount, module, renewalDate,oldDetails));
		return this;    
	}

	
	public License raiseDemandForViolationFee(Installment installment, License license)
	{
		for (final LicenseDemand demand : this.getDemandSet()) {
			if(demand.getIsHistory().equalsIgnoreCase("N"))
			{
				this.getDemandSet().add(demand.setViolationFeeForHawker(installment, license, license.getTradeName().getLicenseType().getModule()));
			}
		}     
		return this; 
	}
	

	public List<EgDemandDetails> addOldDemandDetailsToCurrent(LicenseDemand demand) {
		List<EgDemandDetails> oldDetails = new ArrayList<EgDemandDetails>();
		if (demand.getIsHistory().equalsIgnoreCase("N")) {
			for (EgDemandDetails dd : demand.getEgDemandDetails()) {
				oldDetails.add((EgDemandDetails) dd.clone());
			}
		}
		return oldDetails;
	}

	/**
	 * will give the difference to expiry date if date is passed expiry date then it will give no of months passed with isExpired set to true if date is prioror to the date of expiry then it will give no of months to expire and isExpired set to false
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

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public void setBoundary(Boundary boundary) {
		this.boundary = boundary;
	}

	public void setBuildingType(String buildingType) {
		this.buildingType = buildingType;
	}

	public void setCompanyDetails(String companyDetails) {
		this.companyDetails = companyDetails;
	}

	public void setDateOfCreation(Date dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}

	public void setDateOfExpiry(Date dateOfExpiry) {
		this.dateOfExpiry = dateOfExpiry;
	}

	public void setDeduction(BigDecimal deduction) {
		this.deduction = deduction;
	}

	public void setDemandSet(Set<LicenseDemand> demandSet) {
		this.demandSet = demandSet;
	}

	public void setFeeTypeStr(String feeTypeStr) {
		this.feeTypeStr = feeTypeStr;
	}

	public void setInspectionDetails(String inspectionDetails) {
		this.inspectionDetails = inspectionDetails;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setLicensee(Licensee licensee) {
		this.licensee = licensee;
	}

	public void setLicenseeZoneId(Long licenseeZoneId) {
		this.licenseeZoneId = licenseeZoneId;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public void setLicenseZoneId(Long licenseZoneId) {
		this.licenseZoneId = licenseZoneId;
	}

	public void setNameOfEstablishment(String nameOfEstablishment) {
		this.nameOfEstablishment = nameOfEstablishment;
	}

	public void setNoOfRooms(Integer noOfRooms) {
		this.noOfRooms = noOfRooms;
	}

	public void setOldLicenseNumber(String oldLicenseNumber) {
		this.oldLicenseNumber = oldLicenseNumber;
	}

	public void setOtherCharges(BigDecimal otherCharges) {
		this.otherCharges = otherCharges;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setRentalAgreement(boolean rentalAgreement) {
		this.rentalAgreement = rentalAgreement;
	}

	public void setRentPaid(BigDecimal rentPaid) {
		this.rentPaid = rentPaid;
	}

	public void setServicetaxNumber(String servicetaxNumber) {
		this.servicetaxNumber = servicetaxNumber;
	}

	public void setStatus(LicenseStatus status) {
		this.status = status;
	}

	public void setTempLicenseNumber(String tempLicenseNumber) {
		this.tempLicenseNumber = tempLicenseNumber;
	}

	public void setTinNumber(String tinNumber) {
		this.tinNumber = tinNumber;
	}

	public void setTradeName(SubCategory tradeName) {
		this.tradeName = tradeName;
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

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}
	
	public BigDecimal getSwmFee() {
		return swmFee;
	}
	
	public void setSwmFee(BigDecimal swmFee) {
		this.swmFee = swmFee;
	}
	
	public String getBioMedicalWasteReg() {
		return bioMedicalWasteReg;
	}

	public void setBioMedicalWasteReg(String bioMedicalWasteReg) {
		this.bioMedicalWasteReg = bioMedicalWasteReg;
	}
	public String getDocImageNumber() {
		return docImageNumber;
	}

	public void setDocImageNumber(String docImageNumber) {
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

	public void setMinSolvency(BigDecimal minSolvency) {
		this.minSolvency = minSolvency;
	}

	public void setExecuteWorksWorth(String executeWorksWorth) {
		this.executeWorksWorth = executeWorksWorth;
	}

	public BigDecimal getAvgAnnualTurnover() {
		return avgAnnualTurnover;
	}

	public BigDecimal getCostOfWorksHand() {
		return costOfWorksHand;
	}

	public void setAvgAnnualTurnover(BigDecimal avgAnnualTurnover) {
		this.avgAnnualTurnover = avgAnnualTurnover;
	}

	public void setCostOfWorksHand(BigDecimal costOfWorksHand) {
		this.costOfWorksHand = costOfWorksHand;
	}

	public void setCompanyPanNumber(String companyPanNumber) {
		this.companyPanNumber = companyPanNumber;
	}

	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}

	public void setTypeOfFirm(String typeOfFirm) {
		this.typeOfFirm = typeOfFirm;
	}

	public void setNamePowerOfAttorney(String namePowerOfAttorney) {
		this.namePowerOfAttorney = namePowerOfAttorney;
	}

	public void setNameSoleProprietor(String nameSoleProprietor) {
		this.nameSoleProprietor = nameSoleProprietor;
	}

	public void setBankerName(String bankerName) {
		this.bankerName = bankerName;
	}


	public void setFeeExemption(String feeExemption) {
		this.feeExemption = feeExemption;
	}
	
	public String getBankIfscCode() {
		return bankIfscCode;
	}

	public void setBankIfscCode(String bankIfscCode) {
		this.bankIfscCode = bankIfscCode;
	}

	public Character getIsUpgrade() {
		return isUpgrade;
	}
	
	public void setIsUpgrade(Character isUpgrade) {
		this.isUpgrade = isUpgrade;
	}
	
	
	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	
	public String getPlaceOfBusiness() {
		return placeOfBusiness;
	}

	public void setPlaceOfBusiness(String placeOfBusiness) {
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
		str.append("  feeTypeStr=").append(this.feeTypeStr==null ? "null" : this.feeTypeStr.toString());
		str.append("  SwmFee=").append(this.swmFee==null ? "null" : this.swmFee.toString());
		str.append("  bioMedicalWasteReg=").append(this.bioMedicalWasteReg==null ? "null" : this.bioMedicalWasteReg.toString());
		str.append("  docImageNumber=").append(this.docImageNumber==null ? "null" : this.docImageNumber.toString());
		str.append("  bankAddress=").append(this.bankAddress==null ? "null" : this.bankAddress.toString());
		str.append("  placeOfBusiness=").append(this.placeOfBusiness==null ? "null" : this.placeOfBusiness.toString());
		str.append("}");
		return str.toString();
	}
	
	public void updateStatus(LicenseStatus currentStatus) {
		this.setStatus(currentStatus);
		final LicenseStatusValues statusValues = new LicenseStatusValues();
		statusValues.setLicenseStatus(currentStatus);
	}
	
	public String getOfficeEmailId() {
    	return officeEmailId;
    }

	public void setOfficeEmailId(String officeEmailId) {
    	this.officeEmailId = officeEmailId;
    }

	public String getContractorCode() {
		return contractorCode;
	}

	public void setContractorCode(String contractorCode) {
		this.contractorCode = contractorCode;
	}

	public List<LicenseObjection> getObjections() {
		return this.objections;
	}

	public void setObjections(List<LicenseObjection> objections) {
		this.objections = objections;
	}

	public License getEgLicense() {
		return egLicense;
	}
	
	public void setEgLicense(License egLicense) {
		this.egLicense = egLicense;
	}
	
	public LicenseSubType getLicenseSubType() {
		return licenseSubType;
	}
	
	public void setLicenseSubType(LicenseSubType licenseSubType) {
		this.licenseSubType = licenseSubType;
	}
	
	public List<LicenseSubType> getLicenseSubTypeList() {
		return licenseSubTypeList;
	}
	
	public void setLicenseSubTypeList(List<LicenseSubType> licenseSubTypeList) {
		this.licenseSubTypeList = licenseSubTypeList;
	}

	public BigDecimal getViolationFee() {
		return violationFee;
	}

	public void setViolationFee(BigDecimal violationFee) {
		this.violationFee = violationFee;
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

	public License updateCollectedForExisting(License license) {
		
		EgDemand licenseDemand=license.getCurrentDemand();
		//Installment licIntallment=licenseDemand.getEgInstallmentMaster();
		if(licenseDemand!=null)
		{
		Set<EgDemandDetails> demanddetails=	licenseDemand.getEgDemandDetails();
		BigDecimal tot_amt=BigDecimal.ZERO;
		for (EgDemandDetails dd : demanddetails)
		{
				BigDecimal demandAmount=dd.getAmount().subtract(dd.getAmtRebate());
				tot_amt=tot_amt.add(demandAmount);
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
		BigDecimal totBal=getTotalBalance();
		if(totBal.equals(BigDecimal.ZERO))
			paid=true;
		return paid;
	}
	
	public boolean isViolationFeePending() {
		boolean paid = false;
		BigDecimal totBal = BigDecimal.ZERO;
		for (final EgDemand demand : this.demandSet) {
			if (demand.getIsHistory().equals("N")) {
				for (final EgDemandDetails dd : demand.getEgDemandDetails()) {
					if (dd.getEgDemandReason().getEgDemandReasonMaster().getCode().equals(Constants.VIOLATION_FEE_DEMAND_REASON)) {
						if (!(dd.getAmount().subtract(dd.getAmtCollected()).equals(BigDecimal.ZERO)))
							totBal = totBal.add(dd.getAmount().subtract(dd.getAmtCollected()));
						if (!totBal.equals(BigDecimal.ZERO))
							paid = true;
					}
				}
			}
		}
		return paid;
	}
	
	public boolean isWorkFlowStateRejected() {
		boolean workFlowStateRejected = false;
		if (this.getState()!=null && this.getState().getValue().contains("Rejected")) {
			workFlowStateRejected = this.getState().getValue().contains("Rejected");
		}
		return workFlowStateRejected;
	}
	
	public BigDecimal getTotalBalance()
	{
		BigDecimal totBal=BigDecimal.ZERO;
	
		for (final EgDemand demand : this.demandSet) {
			if (demand.getIsHistory().equals("N")) {
				for (final EgDemandDetails dd : demand.getEgDemandDetails()) {
						if(!(dd.getAmount().subtract(dd.getAmtCollected()).equals(BigDecimal.ZERO)))
							totBal=totBal.add(dd.getAmount().subtract(dd.getAmtCollected()));
						if(!dd.getAmtRebate().equals(BigDecimal.ZERO))
							totBal = totBal.subtract(dd.getAmtRebate());
				}
			}
		}
 		return totBal;
	}
	
	public String getAuditDetails(){   
		return "";
	}
}
