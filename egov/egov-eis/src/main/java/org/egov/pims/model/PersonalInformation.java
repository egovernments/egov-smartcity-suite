/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.pims.model;


import org.apache.log4j.Logger;
import org.egov.commons.EgwStatus;
import org.egov.commons.utils.EntityType;
import org.egov.eis.entity.EmployeeType;
import org.egov.infra.admin.master.entity.User;
import org.egov.pims.service.EmployeeServiceOld;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 * @author DivyaShree
 *
 */
public class PersonalInformation implements java.io.Serializable,EntityType
{

	private static final long serialVersionUID = 1L;
	public final static Logger LOGGER = Logger.getLogger(PersonalInformation.class.getClass());
	private PersonalInformation employeeId;
	private EmployeeServiceOld employeeService;
	User	userMaster =null;
	private Integer idPersonalInformation;

	private LanguagesQulifiedMaster langQulMstr;

	private ReligionMaster religionMstr;

	private LanguagesKnownMaster languagesKnownMstr;

	private RecruimentMaster modeOfRecruimentMstr;

	private CommunityMaster communityMstr;

	private CategoryMaster categoryMstr;//is this required?

	private BloodGroupMaster bloodGroupMstr;

	private TypeOfRecruimentMaster recruitmentTypeMstr;

	private String employeeCode;
	private String employeeName;
	private  EmployeeType employeeTypeMaster;
	private EgwStatus StatusMaster;
	private String employeeFirstName;
	private String employeeMiddleName;
	private String employeeLastName;
	private String fatherHusbandFirstName;
	private String fatherHusbandMiddleName;
	private String fatherHusbandLastName;
	private String identificationMarks2;
	private String panNumber;
	private Date dateOfBirth;
	/*
	 * death date
	 */
	private Date deathDate;
	/*
	 * deputation date
	 */
	private Date dateOfjoin;
	
	/*
	 * Retirement date moved from grade
	 */
	private Date retirementDate;

	private String motherTonuge;

	private Character gender= Character.valueOf('0');


	private Integer isActive= Integer.valueOf(1);

	private Character isHandicapped= Character.valueOf('0');

	private Character isMedReportAvailable=Character.valueOf('0');

	private Date dateOfFirstAppointment;

	private String identificationMarks1;

	private Character status= Character.valueOf('0');

	private String gpfAcNumber;//Should this come under EIS ?

	private Integer retirementAge;

	private String location;//Where are we using this?

	private User createdBy;
	
	private Date createdTime;
	/*
	 * New column added for last modified date
	 */
	
	private Date lastmodifieddate;
	
	private String email;
	
	private String phoneNumber;
	
	private String permanentAddress;
	
	private String correspondenceAddress;
	
	public String getPermanentAddress() {
		return permanentAddress;
	}
	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}
	public String getCorrespondenceAddress() {
		return correspondenceAddress;
	}
	public void setCorrespondenceAddress(String correspondenceAddress) {
		this.correspondenceAddress = correspondenceAddress;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public User getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	private Set<LangKnown> egpimsLangKnowns = new HashSet<LangKnown>(0);//should we keep it now?
	private Set<org.egov.eis.entity.Assignment> egpimsAssignment = new HashSet<org.egov.eis.entity.Assignment>(0);

	public Set<org.egov.eis.entity.Assignment> getEgpimsAssignment() {
		return egpimsAssignment;
	}
	public void setEgpimsAssignment(
			Set<org.egov.eis.entity.Assignment> egpimsAssignment) {
		this.egpimsAssignment = egpimsAssignment;
	}
	public PersonalInformation()
	{
	}


	public Integer getIdPersonalInformation()
	{
		return this.idPersonalInformation;
	}
	
	public Integer getId()
	{
		return this.idPersonalInformation;
	}
	
	public void setIdPersonalInformation(Integer idPersonalInformation)
	{
		this.idPersonalInformation = idPersonalInformation;
	}
public BloodGroupMaster getBloodGroupMstr()
	{
		return this.bloodGroupMstr;
	}

	public void setBloodGroupMstr(BloodGroupMaster bloodGroupMstr)
	{
		this.bloodGroupMstr = bloodGroupMstr;
	}

	public ReligionMaster getReligionMstr()
	{
		return this.religionMstr;
	}

	public void setReligionMstr(ReligionMaster religionMstr)
	{
		this.religionMstr = religionMstr;
	}

	public LanguagesKnownMaster getLanguagesKnownMstr()
	{
		return this.languagesKnownMstr;
	}

	public void setLanguagesKnownMstr(
			LanguagesKnownMaster languagesKnownMstr)
	{
		this.languagesKnownMstr = languagesKnownMstr;
	}

	public CommunityMaster getCommunityMstr()
	{
		return this.communityMstr;
	}
	public void setCommunityMstr(CommunityMaster communityMstr)
	{
		this.communityMstr = communityMstr;
	}
	public CategoryMaster getCategoryMstr()
	{
		return this.categoryMstr;
	}
	public void setCategoryMstr(CategoryMaster categoryMstr)
	{
		this.categoryMstr = categoryMstr;
	}
	public TypeOfRecruimentMaster getRecruitmentTypeMstr()
	{
		return this.recruitmentTypeMstr;
	}
	public void setRecruitmentTypeMstr(
			TypeOfRecruimentMaster recruitmentTypeMstr)
	{
		this.recruitmentTypeMstr = recruitmentTypeMstr;
	}

	public String getEmployeeCode()
	{
		return this.employeeCode;
	}

	public void setEmployeeCode(Integer employeeCode)
	{
		this.employeeCode = String.valueOf(employeeCode);
	}

	public void setEmployeeCode(String employeeCode)
	{
		this.employeeCode = employeeCode;
	}

	public Date getDateOfBirth()
	{
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}


	public String getMotherTonuge()
	{
		return this.motherTonuge;
	}

	public void setMotherTonuge(String motherTonuge)
	{
		this.motherTonuge = motherTonuge;
	}

	public Character getGender()
	{
		return this.gender;
	}

	public void setGender(Character gender)
	{
		this.gender = gender;
	}

	public Character getIsHandicapped()
	{
		return this.isHandicapped;
	}

	public void setIsHandicapped(Character isHandicapped)
	{
		this.isHandicapped = isHandicapped;
	}

	public Character getIsMedReportAvailable()
	{
		return this.isMedReportAvailable;
	}

	public void setIsMedReportAvailable(Character isMedReportAvailable)
	{
		this.isMedReportAvailable = isMedReportAvailable;
	}

	public Date getDateOfFirstAppointment()
	{
		return this.dateOfFirstAppointment;
	}

	public void setDateOfFirstAppointment(Date dateOfFirstAppointment)
	{
		this.dateOfFirstAppointment = dateOfFirstAppointment;
	}

	public String getIdentificationMarks1()
	{
		return this.identificationMarks1;
	}

	public void setIdentificationMarks1(String identificationMarks1)
	{
		this.identificationMarks1 = identificationMarks1;
	}

	public Character getStatus()
	{
		return this.status;
	}

	public void setStatus(Character status)
	{
		this.status = status;
	}


	public String getGpfAcNumber()
	{
		return this.gpfAcNumber;
	}

	public void setGpfAcNumber(String gpfAcNumber)
	{
		this.gpfAcNumber = gpfAcNumber;
	}

	

	public Integer getRetirementAge() {
		return retirementAge;
	}
	public void setRetirementAge(Integer retirementAge) {
		this.retirementAge = retirementAge;
	}

	public String getLocation()
	{
		return this.location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public Set<LangKnown> getEgpimsLangKnowns()
	{
		return this.egpimsLangKnowns;
	}

	public void setEgpimsLangKnowns(Set<LangKnown> egpimsLangKnowns)
	{
		this.egpimsLangKnowns = egpimsLangKnowns;
	}
	public void addLangKnown(LangKnown egpimsLangKnown)
	{

		if(getEgpimsLangKnowns()!=null)
		getEgpimsLangKnowns().add(egpimsLangKnown);
	}
	public void removeLangKnown(LangKnown egpimsLangKnown)
	{

		getEgpimsLangKnowns().remove(egpimsLangKnown);
	}

	/**
	 * @return Returns the isActive.
	 */
	public Integer getIsActive() {
		return isActive;
	}
	/**
	 * @param isActive The isActive to set.
	 */
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
			/**
			 * @return Returns the userMaster.
			 */
			public User getUserMaster() {
				return userMaster;
			}
			/**
			 * @param userMaster The userMaster to set.
			 */
			public void setUserMaster(User userMaster) {
				this.userMaster = userMaster;
			}
public String getEmployeeFirstName() {
	return employeeFirstName;
}
public void setEmployeeFirstName(String employeeFirstName) {
	this.employeeFirstName = employeeFirstName;
}
public String getEmployeeLastName() {
	return employeeLastName;
}
public void setEmployeeLastName(String employeeLastName) {
	this.employeeLastName = employeeLastName;
}
public String getEmployeeMiddleName() {
	return employeeMiddleName;
}
public void setEmployeeMiddleName(String employeeMiddleName) {
	this.employeeMiddleName = employeeMiddleName;
}
public String getFatherHusbandFirstName() {
	return fatherHusbandFirstName;
}
public void setFatherHusbandFirstName(String fatherHusbandFirstName) {
	this.fatherHusbandFirstName = fatherHusbandFirstName;
}
public String getFatherHusbandLastName() {
	return fatherHusbandLastName;
}
public void setFatherHusbandLastName(String fatherHusbandLastName) {
	this.fatherHusbandLastName = fatherHusbandLastName;
}
public String getFatherHusbandMiddleName() {
	return fatherHusbandMiddleName;
}
public void setFatherHusbandMiddleName(String fatherHusbandMiddleName) {
	this.fatherHusbandMiddleName = fatherHusbandMiddleName;
}
public String getIdentificationMarks2() {
	return identificationMarks2;
}
public void setIdentificationMarks2(String identificationMarks2) {
	this.identificationMarks2 = identificationMarks2;
}
public String getPanNumber() {
	return panNumber;
}
public void setPanNumber(String panNumber) {
	this.panNumber = panNumber;
}

public LanguagesQulifiedMaster getLangQulMstr() {
	return langQulMstr;
}
public void setLangQulMstr(LanguagesQulifiedMaster langQulMstr) {
	this.langQulMstr = langQulMstr;
}
public RecruimentMaster getModeOfRecruimentMstr() {
	return modeOfRecruimentMstr;
}
public void setModeOfRecruimentMstr(RecruimentMaster modeOfRecruimentMstr) {
	this.modeOfRecruimentMstr = modeOfRecruimentMstr;
}
public void setEmployeeService(EmployeeServiceOld employeeService) {
	this.employeeService = employeeService;
}
public PersonalInformation getEmployeeId() {
	return employeeId;
}
public void setEmployeeId(PersonalInformation employeeId) {
	this.employeeId = employeeId;
}
public String getEmployeeName() {
	return employeeName;
}
public void setEmployeeName(String employeeName) {
	this.employeeName = employeeName;
}

public EmployeeType getEmployeeTypeMaster() {
	return employeeTypeMaster;
}
public void setEmployeeTypeMaster(EmployeeType employeeTypeMaster) {
	this.employeeTypeMaster = employeeTypeMaster;
}
	public EgwStatus getStatusMaster() {
		return StatusMaster;
	}
	public void setStatusMaster(EgwStatus statusMaster) {
		StatusMaster = statusMaster;
	}
	public Date getDeathDate() {
		return deathDate;
	}
	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}
	public Date getLastmodifieddate() {
		return lastmodifieddate;
	}
	public void setLastmodifieddate(Date lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
	}
	public Date getDateOfjoin() {
		return dateOfjoin;
	}
	public void setDateOfjoin(Date dateOfjoin) {
		this.dateOfjoin = dateOfjoin;
	}
	
	public Date getRetirementDate() {
		return retirementDate;
	}
	public void setRetirementDate(Date retirementDate) {
		this.retirementDate = retirementDate;
	}
	
	public String getBankaccount() {
		
		return null;
	}
	
	public String getBankname() {
		
		return null;
	}
	
	public String getCode() {
		if(employeeCode!=null)
		{
		   return this.employeeCode.toString();
		}
		else
		{
			return null;
		}
	}
	
	public String getIfsccode() {
		
		return null;
	}
	
	public String getModeofpay() {

		return null;
	}
	
	public String getName() {

		return this.employeeName;
	}
	
	public String getPanno() {

		return this.panNumber;
	}
	
	public String getTinno() {

		return null;
	}
	public Integer getEntityId()
	{
		return Integer.valueOf(idPersonalInformation);
	}
	@Override
	public String getEntityDescription() {
		
		return getName();
	}
	
	@Override
	public EgwStatus getEgwStatus() {

		return null;
	}
}
