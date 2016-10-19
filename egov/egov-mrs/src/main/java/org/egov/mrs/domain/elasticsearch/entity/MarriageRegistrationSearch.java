/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.mrs.domain.elasticsearch.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.search.elastic.Indexable;
import org.egov.search.domain.Searchable;

/**
 * Model used in Search Registration and Reports
 *
 * @author Vinoth
 *
 */
public class MarriageRegistrationSearch implements Indexable {

	@Override
	public String getIndexId() {
		return ApplicationThreadLocals.getCityCode() + "-" + applicationNo;
	}

	@Searchable(name = "consumernumber", group = Searchable.Group.SEARCHABLE)
	private String consumerNumber;

	@Searchable(name = "applicationnumber", group = Searchable.Group.CLAUSES)
	private String applicationNo;

	@Searchable(name = "applicationtype", group = Searchable.Group.CLAUSES)
	private String applicationType;

	@Searchable(name = "registrationnumber", group = Searchable.Group.SEARCHABLE)
	private String registrationNo;

	@Searchable(name = "dateofmarriage", group = Searchable.Group.CLAUSES)
	private Date dateOfMarriage;

	@Searchable(name = "placeofmarriage", group = Searchable.Group.SEARCHABLE)
	private String placeOfMarriage;

	@Searchable(name = "zone", group = Searchable.Group.CLAUSES)
	private String zone;

	@Searchable(name = "marriageact", group = Searchable.Group.CLAUSES)
	private String marriageAct;

	@Searchable(name = "marriagefeecriteria", group = Searchable.Group.CLAUSES)
	private String marriageFeeCriteria;

	@Searchable(name = "marriagefee", group = Searchable.Group.CLAUSES)
	private BigDecimal marriageFeeAmount;

	@Searchable(name = "registrationdate", group = Searchable.Group.CLAUSES)
	private Date registrationDate;

	@Searchable(name = "fromdate", group = Searchable.Group.CLAUSES)
	private Date fromDate;

	@Searchable(name = "todate", group = Searchable.Group.CLAUSES)
	private Date toDate;

	@Searchable(name = "feepaid", group = Searchable.Group.CLAUSES)
	private BigDecimal feePaid;

	@Searchable(name = "applicationstatus", group = Searchable.Group.CLAUSES)
	private String applicationStatus;

	@Searchable(name = "isactive", group = Searchable.Group.CLAUSES)
	private boolean active;

	@Searchable(name = "rejectionreason", group = Searchable.Group.SEARCHABLE)
	private String rejectionReason;

	@Searchable(name = "remarks", group = Searchable.Group.SEARCHABLE)
	private String remarks;

	@Searchable(name = "createddate", group = Searchable.Group.COMMON)
	private Date applicationCreatedDate;

	@Searchable(name = "lastmodifieddate", group = Searchable.Group.COMMON)
	private Date applicationLastModifiedDate;

	@Searchable(name = "createdby", group = Searchable.Group.CLAUSES)
	private String applicationCreatedBy;

	@Searchable(name = "husbandname", group = Searchable.Group.SEARCHABLE)
	private String husbandName;

	@Searchable(name = "husbandreligion", group = Searchable.Group.CLAUSES)
	private String husbandReligion;

	@Searchable(name = "husbandreligionpractice", group = Searchable.Group.CLAUSES)
	private String husbandReligionPractice;

	@Searchable(name = "husbandageinyearsasonmarriage", group = Searchable.Group.CLAUSES)
	private Double husbandAgeInYearsAsOnMarriage;

	@Searchable(name = "husbandageinmonthsasonmarriage", group = Searchable.Group.CLAUSES)
	private Double husbandAgeInMonthsAsOnMarriage;

	@Searchable(name = "husbandmaritalstatus", group = Searchable.Group.CLAUSES)
	private String husbandMaritalStatus;

	@Searchable(name = "husbandoccupation", group = Searchable.Group.SEARCHABLE)
	private String husbandOccupation;

	@Searchable(name = "husbandaadhaarnumber", group = Searchable.Group.CLAUSES)
	private String husbandAadhaarNo;

	@Searchable(name = "husbandmobilenumber", group = Searchable.Group.CLAUSES)
	private String husbandPhoneNo;

	@Searchable(name = "husbandresidencyaddress", group = Searchable.Group.SEARCHABLE)
	private String husbandResidencyAddress;

	@Searchable(name = "husbandofficeaddress", group = Searchable.Group.SEARCHABLE)
	private String husbandOfficeAddress;

	@Searchable(name = "husbandemail", group = Searchable.Group.CLAUSES)
	private String husbandEmail;

	@Searchable(name = "wifename", group = Searchable.Group.SEARCHABLE)
	private String wifeName;

	@Searchable(name = "wifereligion", group = Searchable.Group.CLAUSES)
	private String wifeReligion;

	@Searchable(name = "wifereligionpractice", group = Searchable.Group.CLAUSES)
	private String wifeReligionPractice;

	@Searchable(name = "wifeageinyearsasonmarriage", group = Searchable.Group.CLAUSES)
	private Double wifeAgeInYearsAsOnMarriage;

	@Searchable(name = " wifeageinmonthsasonmarriage", group = Searchable.Group.CLAUSES)
	private Double wifeAgeInMonthsAsOnMarriage;

	@Searchable(name = "wifemaritalstatus", group = Searchable.Group.CLAUSES)
	private String wifeMaritalStatus;

	@Searchable(name = "wifeoccupation", group = Searchable.Group.SEARCHABLE)
	private String wifeOccupation;

	@Searchable(name = "wifeaadhaarnumber", group = Searchable.Group.CLAUSES)
	private String wifeAadhaarNo;

	@Searchable(name = "wifemobilenumber", group = Searchable.Group.CLAUSES)
	private String wifePhoneNo;

	@Searchable(name = "wiferesidencyaddress", group = Searchable.Group.SEARCHABLE)
	private String wifeResidencyAddress;

	@Searchable(name = "wifeofficeaddress", group = Searchable.Group.SEARCHABLE)
	private String wifeOfficeAddress;

	@Searchable(name = "wifeemail", group = Searchable.Group.CLAUSES)
	private String wifeEmail;

	@Searchable(name = "statetype", group = Searchable.Group.CLAUSES)
	private String stateType;

	@Searchable(name = "witness1name", group = Searchable.Group.SEARCHABLE)
	private String witness1Name;

	@Searchable(name = "witness1occupation", group = Searchable.Group.SEARCHABLE)
	private String witness1Occupation;

	@Searchable(name = "witness1relationshipwithapplicant", group = Searchable.Group.SEARCHABLE)
	private String witness1RelationshipWithApplicant;

	@Searchable(name = "witness1aadhaarnumber", group = Searchable.Group.CLAUSES)
	private String witness1AadhaarNo;

	@Searchable(name = "witness1address", group = Searchable.Group.SEARCHABLE)
	private String witness1Address;

	@Searchable(name = "witness2name", group = Searchable.Group.SEARCHABLE)
	private String witness2Name;

	@Searchable(name = "witness2occupation", group = Searchable.Group.SEARCHABLE)
	private String witness2Occupation;

	@Searchable(name = "witness2relationshipwithapplicant", group = Searchable.Group.SEARCHABLE)
	private String witness2RelationshipWithApplicant;

	@Searchable(name = "witness2aadhaarnumber", group = Searchable.Group.CLAUSES)
	private String witness2AadhaarNo;

	@Searchable(name = "witness2address", group = Searchable.Group.SEARCHABLE)
	private String witness2Address;

	@Searchable(name = "witness3name", group = Searchable.Group.SEARCHABLE)
	private String witness3Name;

	@Searchable(name = "witness3occupation", group = Searchable.Group.SEARCHABLE)
	private String witness3Occupation;

	@Searchable(name = "witness3relationshipwithapplicant", group = Searchable.Group.SEARCHABLE)
	private String witness3RelationshipWithApplicant;

	@Searchable(name = "witness3aadhaarnumber", group = Searchable.Group.CLAUSES)
	private String witness3AadhaarNo;

	@Searchable(name = "witness3address", group = Searchable.Group.SEARCHABLE)
	private String witness3Address;

	@Searchable(name = "priestname", group = Searchable.Group.SEARCHABLE)
	private String priestName;

	@Searchable(name = "priestage", group = Searchable.Group.CLAUSES)
	private String priestAge;

	@Searchable(name = "priestaddress", group = Searchable.Group.SEARCHABLE)
	private String priestAddress;

	@Searchable(name = "priestreligion", group = Searchable.Group.CLAUSES)
	private String priestReligion;

	@Searchable(name = "certificatenumber", group = Searchable.Group.SEARCHABLE)
	private String certificateNo;

	@Searchable(name = "certificatetype", group = Searchable.Group.CLAUSES)
	private String certificateType;

	@Searchable(name = "certificatedate", group = Searchable.Group.CLAUSES)
	private Date certificateDate;

	@Searchable(name = "certificateissued", group = Searchable.Group.CLAUSES)
	private boolean certificateIssued;

	@Searchable(name = "cityname", group = Searchable.Group.CLAUSES)
	private String ulbName;

	@Searchable(name = "districtname", group = Searchable.Group.CLAUSES)
	private String districtName;

	@Searchable(name = "regionname", group = Searchable.Group.CLAUSES)
	private String regionName;

	@Searchable(name = "citygrade", group = Searchable.Group.CLAUSES)
	private String ulbGrade;

	@Searchable(name = "citycode", group = Searchable.Group.CLAUSES)
	private String ulbCode;

	public MarriageRegistrationSearch(final String applicationNo, final String ulbName, final String ulbCode,
			final Date createdDate, final String districtName, final String regionName, final String ulbGrade) {
		this.applicationNo = applicationNo;
		this.ulbName = ulbName;
		this.applicationCreatedDate = createdDate;
		this.districtName = districtName;
		this.regionName = regionName;
		this.ulbGrade = ulbGrade;
		this.ulbCode = ulbCode;
	}

	public String getConsumerNumber() {
		return consumerNumber;
	}

	public void setConsumerNumber(String consumerNumber) {
		this.consumerNumber = consumerNumber;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	public String getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	public Date getDateOfMarriage() {
		return dateOfMarriage;
	}

	public void setDateOfMarriage(Date dateOfMarriage) {
		this.dateOfMarriage = dateOfMarriage;
	}

	public String getPlaceOfMarriage() {
		return placeOfMarriage;
	}

	public void setPlaceOfMarriage(String placeOfMarriage) {
		this.placeOfMarriage = placeOfMarriage;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getMarriageAct() {
		return marriageAct;
	}

	public void setMarriageAct(String marriageAct) {
		this.marriageAct = marriageAct;
	}

	public String getMarriageFeeCriteria() {
		return marriageFeeCriteria;
	}

	public void setMarriageFeeCriteria(String marriageFeeCriteria) {
		this.marriageFeeCriteria = marriageFeeCriteria;
	}

	public BigDecimal getMarriageFeeAmount() {
		return marriageFeeAmount;
	}

	public void setMarriageFeeAmount(BigDecimal marriageFeeAmount) {
		this.marriageFeeAmount = marriageFeeAmount;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public BigDecimal getFeePaid() {
		return feePaid;
	}

	public void setFeePaid(BigDecimal feePaid) {
		this.feePaid = feePaid;
	}

	public String getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getApplicationCreatedDate() {
		return applicationCreatedDate;
	}

	public void setApplicationCreatedDate(Date applicationCreatedDate) {
		this.applicationCreatedDate = applicationCreatedDate;
	}

	public Date getApplicationLastModifiedDate() {
		return applicationLastModifiedDate;
	}

	public void setApplicationLastModifiedDate(Date applicationLastModifiedDate) {
		this.applicationLastModifiedDate = applicationLastModifiedDate;
	}

	public String getApplicationCreatedBy() {
		return applicationCreatedBy;
	}

	public void setApplicationCreatedBy(String applicationCreatedBy) {
		this.applicationCreatedBy = applicationCreatedBy;
	}

	public String getHusbandName() {
		return husbandName;
	}

	public void setHusbandName(String husbandName) {
		this.husbandName = husbandName;
	}

	public String getHusbandReligion() {
		return husbandReligion;
	}

	public void setHusbandReligion(String husbandReligion) {
		this.husbandReligion = husbandReligion;
	}

	public String getHusbandReligionPractice() {
		return husbandReligionPractice;
	}

	public void setHusbandReligionPractice(String husbandReligionPractice) {
		this.husbandReligionPractice = husbandReligionPractice;
	}

	public Double getHusbandAgeInYearsAsOnMarriage() {
		return husbandAgeInYearsAsOnMarriage;
	}

	public void setHusbandAgeInYearsAsOnMarriage(Double husbandAgeInYearsAsOnMarriage) {
		this.husbandAgeInYearsAsOnMarriage = husbandAgeInYearsAsOnMarriage;
	}

	public Double getHusbandAgeInMonthsAsOnMarriage() {
		return husbandAgeInMonthsAsOnMarriage;
	}

	public void setHusbandAgeInMonthsAsOnMarriage(Double husbandAgeInMonthsAsOnMarriage) {
		this.husbandAgeInMonthsAsOnMarriage = husbandAgeInMonthsAsOnMarriage;
	}

	public String getHusbandMaritalStatus() {
		return husbandMaritalStatus;
	}

	public void setHusbandMaritalStatus(String husbandMaritalStatus) {
		this.husbandMaritalStatus = husbandMaritalStatus;
	}

	public String getHusbandOccupation() {
		return husbandOccupation;
	}

	public void setHusbandOccupation(String husbandOccupation) {
		this.husbandOccupation = husbandOccupation;
	}

	public String getHusbandAadhaarNo() {
		return husbandAadhaarNo;
	}

	public void setHusbandAadhaarNo(String husbandAadhaarNo) {
		this.husbandAadhaarNo = husbandAadhaarNo;
	}

	public String getHusbandPhoneNo() {
		return husbandPhoneNo;
	}

	public void setHusbandPhoneNo(String husbandPhoneNo) {
		this.husbandPhoneNo = husbandPhoneNo;
	}

	public String getHusbandResidencyAddress() {
		return husbandResidencyAddress;
	}

	public void setHusbandResidencyAddress(String husbandResidencyAddress) {
		this.husbandResidencyAddress = husbandResidencyAddress;
	}

	public String getHusbandOfficeAddress() {
		return husbandOfficeAddress;
	}

	public void setHusbandOfficeAddress(String husbandOfficeAddress) {
		this.husbandOfficeAddress = husbandOfficeAddress;
	}

	public String getHusbandEmail() {
		return husbandEmail;
	}

	public void setHusbandEmail(String husbandEmail) {
		this.husbandEmail = husbandEmail;
	}

	public String getWifeName() {
		return wifeName;
	}

	public void setWifeName(String wifeName) {
		this.wifeName = wifeName;
	}

	public String getWifeReligion() {
		return wifeReligion;
	}

	public void setWifeReligion(String wifeReligion) {
		this.wifeReligion = wifeReligion;
	}

	public String getWifeReligionPractice() {
		return wifeReligionPractice;
	}

	public void setWifeReligionPractice(String wifeReligionPractice) {
		this.wifeReligionPractice = wifeReligionPractice;
	}

	public Double getWifeAgeInYearsAsOnMarriage() {
		return wifeAgeInYearsAsOnMarriage;
	}

	public void setWifeAgeInYearsAsOnMarriage(Double wifeAgeInYearsAsOnMarriage) {
		this.wifeAgeInYearsAsOnMarriage = wifeAgeInYearsAsOnMarriage;
	}

	public Double getWifeAgeInMonthsAsOnMarriage() {
		return wifeAgeInMonthsAsOnMarriage;
	}

	public void setWifeAgeInMonthsAsOnMarriage(Double wifeAgeInMonthsAsOnMarriage) {
		this.wifeAgeInMonthsAsOnMarriage = wifeAgeInMonthsAsOnMarriage;
	}

	public String getWifeMaritalStatus() {
		return wifeMaritalStatus;
	}

	public void setWifeMaritalStatus(String wifeMaritalStatus) {
		this.wifeMaritalStatus = wifeMaritalStatus;
	}

	public String getWifeOccupation() {
		return wifeOccupation;
	}

	public void setWifeOccupation(String wifeOccupation) {
		this.wifeOccupation = wifeOccupation;
	}

	public String getWifeAadhaarNo() {
		return wifeAadhaarNo;
	}

	public void setWifeAadhaarNo(String wifeAadhaarNo) {
		this.wifeAadhaarNo = wifeAadhaarNo;
	}

	public String getWifePhoneNo() {
		return wifePhoneNo;
	}

	public void setWifePhoneNo(String wifePhoneNo) {
		this.wifePhoneNo = wifePhoneNo;
	}

	public String getWifeResidencyAddress() {
		return wifeResidencyAddress;
	}

	public void setWifeResidencyAddress(String wifeResidencyAddress) {
		this.wifeResidencyAddress = wifeResidencyAddress;
	}

	public String getWifeOfficeAddress() {
		return wifeOfficeAddress;
	}

	public void setWifeOfficeAddress(String wifeOfficeAddress) {
		this.wifeOfficeAddress = wifeOfficeAddress;
	}

	public String getWifeEmail() {
		return wifeEmail;
	}

	public void setWifeEmail(String wifeEmail) {
		this.wifeEmail = wifeEmail;
	}

	public String getStateType() {
		return stateType;
	}

	public void setStateType(String stateType) {
		this.stateType = stateType;
	}

	public String getWitness1Name() {
		return witness1Name;
	}

	public void setWitness1Name(String witness1Name) {
		this.witness1Name = witness1Name;
	}

	public String getWitness1Occupation() {
		return witness1Occupation;
	}

	public void setWitness1Occupation(String witness1Occupation) {
		this.witness1Occupation = witness1Occupation;
	}

	public String getWitness1RelationshipWithApplicant() {
		return witness1RelationshipWithApplicant;
	}

	public void setWitness1RelationshipWithApplicant(String witness1RelationshipWithApplicant) {
		this.witness1RelationshipWithApplicant = witness1RelationshipWithApplicant;
	}

	public String getWitness1AadhaarNo() {
		return witness1AadhaarNo;
	}

	public void setWitness1AadhaarNo(String witness1AadhaarNo) {
		this.witness1AadhaarNo = witness1AadhaarNo;
	}

	public String getWitness1Address() {
		return witness1Address;
	}

	public void setWitness1Address(String witness1Address) {
		this.witness1Address = witness1Address;
	}

	public String getWitness2Name() {
		return witness2Name;
	}

	public void setWitness2Name(String witness2Name) {
		this.witness2Name = witness2Name;
	}

	public String getWitness2Occupation() {
		return witness2Occupation;
	}

	public void setWitness2Occupation(String witness2Occupation) {
		this.witness2Occupation = witness2Occupation;
	}

	public String getWitness2RelationshipWithApplicant() {
		return witness2RelationshipWithApplicant;
	}

	public void setWitness2RelationshipWithApplicant(String witness2RelationshipWithApplicant) {
		this.witness2RelationshipWithApplicant = witness2RelationshipWithApplicant;
	}

	public String getWitness2AadhaarNo() {
		return witness2AadhaarNo;
	}

	public void setWitness2AadhaarNo(String witness2AadhaarNo) {
		this.witness2AadhaarNo = witness2AadhaarNo;
	}

	public String getWitness2Address() {
		return witness2Address;
	}

	public void setWitness2Address(String witness2Address) {
		this.witness2Address = witness2Address;
	}

	public String getWitness3Name() {
		return witness3Name;
	}

	public void setWitness3Name(String witness3Name) {
		this.witness3Name = witness3Name;
	}

	public String getWitness3Occupation() {
		return witness3Occupation;
	}

	public void setWitness3Occupation(String witness3Occupation) {
		this.witness3Occupation = witness3Occupation;
	}

	public String getWitness3RelationshipWithApplicant() {
		return witness3RelationshipWithApplicant;
	}

	public void setWitness3RelationshipWithApplicant(String witness3RelationshipWithApplicant) {
		this.witness3RelationshipWithApplicant = witness3RelationshipWithApplicant;
	}

	public String getWitness3AadhaarNo() {
		return witness3AadhaarNo;
	}

	public void setWitness3AadhaarNo(String witness3AadhaarNo) {
		this.witness3AadhaarNo = witness3AadhaarNo;
	}

	public String getWitness3Address() {
		return witness3Address;
	}

	public void setWitness3Address(String witness3Address) {
		this.witness3Address = witness3Address;
	}

	public String getPriestName() {
		return priestName;
	}

	public void setPriestName(String priestName) {
		this.priestName = priestName;
	}

	public String getPriestAge() {
		return priestAge;
	}

	public void setPriestAge(String priestAge) {
		this.priestAge = priestAge;
	}

	public String getPriestAddress() {
		return priestAddress;
	}

	public void setPriestAddress(String priestAddress) {
		this.priestAddress = priestAddress;
	}

	public String getPriestReligion() {
		return priestReligion;
	}

	public void setPriestReligion(String priestReligion) {
		this.priestReligion = priestReligion;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public String getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}

	public Date getCertificateDate() {
		return certificateDate;
	}

	public void setCertificateDate(Date certificateDate) {
		this.certificateDate = certificateDate;
	}

	public boolean isCertificateIssued() {
		return certificateIssued;
	}

	public void setCertificateIssued(boolean certificateIssued) {
		this.certificateIssued = certificateIssued;
	}

	public String getUlbName() {
		return ulbName;
	}

	public void setUlbName(String ulbName) {
		this.ulbName = ulbName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getUlbGrade() {
		return ulbGrade;
	}

	public void setUlbGrade(String ulbGrade) {
		this.ulbGrade = ulbGrade;
	}

	public String getUlbCode() {
		return ulbCode;
	}

	public void setUlbCode(String ulbCode) {
		this.ulbCode = ulbCode;
	}

}
