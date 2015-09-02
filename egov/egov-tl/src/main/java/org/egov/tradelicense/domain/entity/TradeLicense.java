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
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.infstr.utils.DateUtils;
import org.egov.tradelicense.utils.Constants;

public class TradeLicense extends License {
	private static final Logger LOGGER = Logger.getLogger(TradeLicense.class);
	private static final long serialVersionUID = 1L;
	private List<MotorDetails> installedMotorList;
	private Boolean motorInstalled;
	private List<MotorMaster> motorMasterList;
	private BigDecimal totalHP;
	private List<String> hotelGradeList;
	private String hotelGrade;
	private List hotelSubCatList;
	public static final String[] HOTELGRADE = {"Grade A", "Grade B", "Grade C"};
	private List licenseZoneList;
	private BigDecimal sandBuckets;
	private BigDecimal waterBuckets;
	private BigDecimal dcpExtinguisher;
	private String nocNumber;
	private Boolean isCertificateGenerated;

	public Set<EgDemandDetails> additionalDemandDetails(final Set<EgDemandReasonMaster> egDemandReasonMasters, final Installment installment) {
		LOGGER.debug("Adding additinal Demand Details...");
		final Set<EgDemandDetails> addtionalDemandDetails = new LinkedHashSet<EgDemandDetails>();
		BigDecimal baseMotorFee = null;
		BigDecimal actualMotorFee = null;
		BigDecimal amountToaddBaseDemand = BigDecimal.ZERO;
		for (final MotorMaster mm : this.getMotorMasterList()) {
			if ((mm.getMotorHpFrom().compareTo(BigDecimal.ZERO) == 0) && (mm.getMotorHpTo().compareTo(BigDecimal.ZERO) == 0)) {
				baseMotorFee = mm.getUsingFee();
			}
			if ((this.getTotalHP() == null) || ((mm.getMotorHpFrom().compareTo(this.getTotalHP()) <= 0) && (mm.getMotorHpTo().compareTo(this.getTotalHP()) >= 0))) {
				actualMotorFee = mm.getUsingFee();
			}
		}
		LOGGER.debug("Adding Motor Fee Details...");
		for (final EgDemandReasonMaster dm : egDemandReasonMasters) {
			if (dm.getReasonMaster().equalsIgnoreCase("Motor Fee")) {
				for (final EgDemandReason reason : dm.getEgDemandReasons()) {
					if (reason.getEgInstallmentMaster().getId().equals(installment.getId())) {
						// check for current year installment only
						if (((this.getTotalHP() == null) || (this.getTotalHP().compareTo(BigDecimal.ZERO) == 0))) {
							addtionalDemandDetails.add(EgDemandDetails.fromReasonAndAmounts(baseMotorFee, reason, BigDecimal.ZERO));
							amountToaddBaseDemand = baseMotorFee;
						} else {
							addtionalDemandDetails.add(EgDemandDetails.fromReasonAndAmounts(actualMotorFee, reason, BigDecimal.ZERO));
							amountToaddBaseDemand = actualMotorFee;
						}
					}
				}
			}
		}
		LOGGER.debug("Adding Motor Fee completed.");
		LOGGER.debug("Addtional Demand Details size." + addtionalDemandDetails.size());
		LOGGER.debug("Adding additinal Demand Details done.");
		for (final LicenseDemand ld : this.getDemandSet()) {
			if (ld.getEgInstallmentMaster().getId() == installment.getId()) {
				ld.getEgDemandDetails().addAll(addtionalDemandDetails);
				ld.addBaseDemand(amountToaddBaseDemand);
				break;
			}
		}
		return addtionalDemandDetails;
	}

	@Override
	public String generateApplicationNumber(final String runningNumber) {
		this.setApplicationNumber("TL-APPL" + runningNumber);
		return this.getApplicationNumber();
	}

	@Override
	public String generateLicenseNumber(final String runningNumber) {
		LOGGER.debug("Generating License Number...");
		final StringBuilder licenseNumber = new StringBuilder(32);
		final Calendar cal = Calendar.getInstance();
		cal.setTime(super.getApplicationDate());
		licenseNumber.append(this.getFeeTypeStr()).append(Constants.BACKSLASH).append(runningNumber).append(Constants.BACKSLASH).append(Constants.monthName[cal.get(Calendar.MONTH)]).append("-").append(cal.get(Calendar.YEAR));
		this.setLicenseNumber(licenseNumber.toString());
		LOGGER.debug("Generated License Number =" + licenseNumber.toString());
		LOGGER.debug("Generating License Number completed.");
		return licenseNumber.toString();
	}
	
	public String generateNocNumber(final String runningNumber) {
		LOGGER.debug("Generating NOC Number...");
		final StringBuilder nocNumber = new StringBuilder(32);
		nocNumber.append("W.O.").append(Constants.BACKSLASH).append("PRO-NOC").append(Constants.BACKSLASH).append(runningNumber).append(Constants.BACKSLASH).append("LC.");
		this.setNocNumber(nocNumber.toString());
		LOGGER.debug("Generated NOC Number =" + nocNumber.toString());
		LOGGER.debug("Generating NOC Number completed.");
		return nocNumber.toString();
	}

	public List<MotorDetails> getInstalledMotorList() {
		return this.installedMotorList;
	}

	public Boolean getMotorInstalled() {
		return this.motorInstalled;
	}

	public List<MotorMaster> getMotorMasterList() {
		return this.motorMasterList;
	}
	
	public List<String> getHotelGradeList() {
		return hotelGradeList;
	}

	@Override
	public String getStateDetails() {
		StringBuffer details=new StringBuffer();
		if(this.getLicenseNumber()!=null && !this.getLicenseNumber().isEmpty())
			details.append(this.getLicenseNumber()).append("/");
		details.append(this.getApplicationNumber());
		return details.toString();
	}

	public BigDecimal getTotalHP() {
		return this.totalHP;
	}

	public Boolean isMotorInstalled() {
		return this.motorInstalled;
	}

	public void setInstalledMotorList(List<MotorDetails> installedMotorList) {
		this.installedMotorList = installedMotorList;
	}

	public void setMotorInstalled(Boolean motorInstalled) {
		this.motorInstalled = motorInstalled;
	}

	public void setMotorMasterList(List<MotorMaster> motorMasterList) {
		this.motorMasterList = motorMasterList;
	}

	public void setTotalHP(BigDecimal totalHP) {
		this.totalHP = totalHP;
	}
	
	public void setHotelGradeList(List<String> hotelGradeList) {
		this.hotelGradeList = hotelGradeList;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("TradeLicense={");
		str.append(super.toString());
		str.append("  motorInstalled=").append(this.motorInstalled);
		str.append("  totalHP=").append(this.totalHP == null ? "null" : this.totalHP.toString());
		str.append("  installedMotorList=").append(this.installedMotorList == null ? "null" : this.installedMotorList.toString());
		str.append("}");
		return str.toString();
	}

	@Override
	public void setCreationAndExpiryDate() {
		this.setDateOfCreation(new Date());
		updateExpiryDate(new Date());		
	}
	
	@Override
	public void setCreationAndExpiryDateForEnterLicense() {
		this.setDateOfCreation(getDateOfCreation());
		updateExpiryDate(getDateOfCreation());
	}

	@Override
	public void updateExpiryDate(Date renewalDate) {
		//this.setDateOfCreation(new Date());
		final Calendar instance = Calendar.getInstance();
		if ("PFA".equalsIgnoreCase(this.getFeeTypeStr())) {
			final Date dateOfExpiry = renewalDate;
			instance.setTime(dateOfExpiry);
			int month = instance.get(Calendar.MONTH);
			int year = instance.get(Calendar.YEAR);
			year = year + 5;
			if(month==Calendar.JANUARY || month==Calendar.FEBRUARY || month==Calendar.MARCH) {
				year = year-1;
			}
			instance.set(year, 2, 31);
			this.setDateOfExpiry(instance.getTime());
		} else if ("CNC".equalsIgnoreCase(this.getFeeTypeStr())) {
			final Date dateOfExpiry = renewalDate;
			instance.setTime(dateOfExpiry);
			int month = instance.get(Calendar.MONTH);
			int year = instance.get(Calendar.YEAR);
			year = year + 1;
			if(month==Calendar.JANUARY || month==Calendar.FEBRUARY || month==Calendar.MARCH) {
				year = year-1;
			}
			instance.set(year, 2, 31);
			this.setDateOfExpiry(instance.getTime());
		}
	}

	public List<String> populateHotelGradeList() {
		hotelGradeList = new ArrayList<String>();
		for(int i=0; i < HOTELGRADE.length; i++) {
			hotelGradeList.add(HOTELGRADE[i]);
		}
		return hotelGradeList;
	}

	public String getHotelGrade() {
		return hotelGrade;
	}

	public void setHotelGrade(String hotelGrade) {
		this.hotelGrade = hotelGrade;
	}
	
	public List getHotelSubCatList() {
		return hotelSubCatList;
	}

	public void setHotelSubCatList(List hotelSubCatList) {
		this.hotelSubCatList = hotelSubCatList;
	}

	public List getLicenseZoneList() {
		return licenseZoneList;
	}

	public void setLicenseZoneList(List licenseZoneList) {
		this.licenseZoneList = licenseZoneList;
	}

	public BigDecimal getSandBuckets() {
		return sandBuckets;
	}

	public void setSandBuckets(BigDecimal sandBuckets) {
		this.sandBuckets = sandBuckets;
	}

	public BigDecimal getWaterBuckets() {
		return waterBuckets;
	}

	public void setWaterBuckets(BigDecimal waterBuckets) {
		this.waterBuckets = waterBuckets;
	}

	public BigDecimal getDcpExtinguisher() {
		return dcpExtinguisher;
	}

	public void setDcpExtinguisher(BigDecimal dcpExtinguisher) {
		this.dcpExtinguisher = dcpExtinguisher;
	}

	public String getNocNumber() {
		return nocNumber;
	}

	public void setNocNumber(String nocNumber) {
		this.nocNumber = nocNumber;
	}
	
	public Boolean getIsCertificateGenerated() {
		return isCertificateGenerated;
	}
	
	public void setIsCertificateGenerated(Boolean isCertificateGenerated) {
		this.isCertificateGenerated = isCertificateGenerated;
	}
	
	//TODO: Reviewed by Satyam, suggested to rename the variable name, committing after changes
	public Boolean disablePrintCertificate() {
		Boolean disablePrintCert = false;
		if (this.getTradeName().isNocApplicable()!=null && this.getTradeName().isNocApplicable()) {
			final Calendar instance = Calendar.getInstance();
			final Date newDate = new Date();
			if (getDateOfCreation()!= null) {
				instance.setTime(getDateOfCreation());
				instance.add(Calendar.MONTH, 10);
				if (newDate.before(instance.getTime()))
					disablePrintCert = true;
			}
		}
		return disablePrintCert;
	}
	
	@Override
	public String getAuditDetails(){   
		return new StringBuffer("[Name of the Establishment : ").
				append(this.getNameOfEstablishment()).append(", Applicant Name : ").append(this.getLicensee().getApplicantName()).
				append(", Application Date : ").append(DateUtils.getDefaultFormattedDate(this.getApplicationDate())).
				append(", Address : ").append(this.licensee.getAddress().getHouseNo()).append(", pincode : ").append(this.licensee.getAddress().getPinCode()==null ? "":this.licensee.getAddress().getPinCode()).append(", Trade Name : ").append(this.getTradeName().getName()).append(" ]").toString();
		
	}


}
