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

package org.egov.mrs.entity.es;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;

import static org.egov.infra.utils.ApplicationConstant.DEFAULT_TIMEZONE;
import static org.egov.infra.utils.ApplicationConstant.ES_DATE_FORMAT;

/**
 * Model used in Search Registration and Reports
 *
 * @author Vinoth
 *
 */
@Document(indexName = "marriageregistration", type = "marriageregistration")
public class MarriageRegistrationIndex {

    @Id
    private String id;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String consumerNumber;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String applicationNo;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String applicationType;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String registrationNo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date dateOfMarriage;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String placeOfMarriage;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String zone;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String marriageAct;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String marriageFeeCriteria;

    @Field(type = FieldType.Double, index = FieldIndex.not_analyzed)
    private BigDecimal marriageFeeAmount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date registrationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date toDate;

    @Field(type = FieldType.Double)
    private BigDecimal feePaid;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String applicationStatus;

    @Field(type = FieldType.Boolean)
    private boolean active;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String rejectionReason;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String remarks;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date applicationCreatedDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date applicationLastModifiedDate;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String applicationCreatedBy;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String husbandName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String husbandReligion;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String husbandReligionPractice;

    @Field(type = FieldType.Double)
    private Double husbandAgeInYearsAsOnMarriage;

    @Field(type = FieldType.Double)
    private Double husbandAgeInMonthsAsOnMarriage;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String husbandMaritalStatus;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String husbandOccupation;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String husbandAadhaarNo;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String husbandPhoneNo;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String husbandResidencyAddress;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String husbandOfficeAddress;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String husbandEmail;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String wifeName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String wifeReligion;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String wifeReligionPractice;

    @Field(type = FieldType.Double)
    private Double wifeAgeInYearsAsOnMarriage;

    @Field(type = FieldType.Double)
    private Double wifeAgeInMonthsAsOnMarriage;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String wifeMaritalStatus;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String wifeOccupation;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String wifeAadhaarNo;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String wifePhoneNo;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String wifeResidencyAddress;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String wifeOfficeAddress;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String wifeEmail;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String stateType;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String witness1Name;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String witness1Occupation;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String witness1RelationshipWithApplicant;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String witness1AadhaarNo;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String witness1Address;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String witness2Name;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String witness2Occupation;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String witness2RelationshipWithApplicant;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String witness2AadhaarNo;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String witness2Address;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String witness3Name;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String witness3Occupation;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String witness3RelationshipWithApplicant;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String witness3AadhaarNo;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String witness3Address;
    
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String witness4Name;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String witness4Occupation;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String witness4RelationshipWithApplicant;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String witness4AadhaarNo;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String witness4Address;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String priestName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String priestAge;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String priestAddress;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String priestReligion;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String certificateNo;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String certificateType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date certificateDate;

    @Field(type = FieldType.Boolean)
    private boolean certificateIssued;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ulbName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String districtName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String regionName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ulbGrade;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ulbCode;

    @Field(type = FieldType.Boolean)
    private boolean husbandHandicapped;

    @Field(type = FieldType.Boolean)
    private boolean wifeHandicapped;
    
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String serialNo;
    
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String pageNo;
    
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String source;

    public MarriageRegistrationIndex() {
        //
    }

    public MarriageRegistrationIndex(final String applicationNo, final String ulbName, final String ulbCode,
            final Date createdDate, final String districtName, final String regionName, final String ulbGrade) {
        this.applicationNo = applicationNo;
        this.ulbName = ulbName;
        applicationCreatedDate = createdDate;
        this.districtName = districtName;
        this.regionName = regionName;
        this.ulbGrade = ulbGrade;
        this.ulbCode = ulbCode;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getConsumerNumber() {
        return consumerNumber;
    }

    public void setConsumerNumber(final String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(final String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(final String applicationType) {
        this.applicationType = applicationType;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(final String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public Date getDateOfMarriage() {
        return dateOfMarriage;
    }

    public void setDateOfMarriage(final Date dateOfMarriage) {
        this.dateOfMarriage = dateOfMarriage;
    }

    public String getPlaceOfMarriage() {
        return placeOfMarriage;
    }

    public void setPlaceOfMarriage(final String placeOfMarriage) {
        this.placeOfMarriage = placeOfMarriage;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(final String zone) {
        this.zone = zone;
    }

    public String getMarriageAct() {
        return marriageAct;
    }

    public void setMarriageAct(final String marriageAct) {
        this.marriageAct = marriageAct;
    }

    public String getMarriageFeeCriteria() {
        return marriageFeeCriteria;
    }

    public void setMarriageFeeCriteria(final String marriageFeeCriteria) {
        this.marriageFeeCriteria = marriageFeeCriteria;
    }

    public BigDecimal getMarriageFeeAmount() {
        return marriageFeeAmount;
    }

    public void setMarriageFeeAmount(final BigDecimal marriageFeeAmount) {
        this.marriageFeeAmount = marriageFeeAmount;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(final Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public BigDecimal getFeePaid() {
        return feePaid;
    }

    public void setFeePaid(final BigDecimal feePaid) {
        this.feePaid = feePaid;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(final String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(final String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public Date getApplicationCreatedDate() {
        return applicationCreatedDate;
    }

    public void setApplicationCreatedDate(final Date applicationCreatedDate) {
        this.applicationCreatedDate = applicationCreatedDate;
    }

    public Date getApplicationLastModifiedDate() {
        return applicationLastModifiedDate;
    }

    public void setApplicationLastModifiedDate(final Date applicationLastModifiedDate) {
        this.applicationLastModifiedDate = applicationLastModifiedDate;
    }

    public String getApplicationCreatedBy() {
        return applicationCreatedBy;
    }

    public void setApplicationCreatedBy(final String applicationCreatedBy) {
        this.applicationCreatedBy = applicationCreatedBy;
    }

    public String getHusbandName() {
        return husbandName;
    }

    public void setHusbandName(final String husbandName) {
        this.husbandName = husbandName;
    }

    public String getHusbandReligion() {
        return husbandReligion;
    }

    public void setHusbandReligion(final String husbandReligion) {
        this.husbandReligion = husbandReligion;
    }

    public String getHusbandReligionPractice() {
        return husbandReligionPractice;
    }

    public void setHusbandReligionPractice(final String husbandReligionPractice) {
        this.husbandReligionPractice = husbandReligionPractice;
    }

    public Double getHusbandAgeInYearsAsOnMarriage() {
        return husbandAgeInYearsAsOnMarriage;
    }

    public void setHusbandAgeInYearsAsOnMarriage(final Double husbandAgeInYearsAsOnMarriage) {
        this.husbandAgeInYearsAsOnMarriage = husbandAgeInYearsAsOnMarriage;
    }

    public Double getHusbandAgeInMonthsAsOnMarriage() {
        return husbandAgeInMonthsAsOnMarriage;
    }

    public void setHusbandAgeInMonthsAsOnMarriage(final Double husbandAgeInMonthsAsOnMarriage) {
        this.husbandAgeInMonthsAsOnMarriage = husbandAgeInMonthsAsOnMarriage;
    }

    public String getHusbandMaritalStatus() {
        return husbandMaritalStatus;
    }

    public void setHusbandMaritalStatus(final String husbandMaritalStatus) {
        this.husbandMaritalStatus = husbandMaritalStatus;
    }

    public String getHusbandOccupation() {
        return husbandOccupation;
    }

    public void setHusbandOccupation(final String husbandOccupation) {
        this.husbandOccupation = husbandOccupation;
    }

    public String getHusbandAadhaarNo() {
        return husbandAadhaarNo;
    }

    public void setHusbandAadhaarNo(final String husbandAadhaarNo) {
        this.husbandAadhaarNo = husbandAadhaarNo;
    }

    public String getHusbandPhoneNo() {
        return husbandPhoneNo;
    }

    public void setHusbandPhoneNo(final String husbandPhoneNo) {
        this.husbandPhoneNo = husbandPhoneNo;
    }

    public String getHusbandResidencyAddress() {
        return husbandResidencyAddress;
    }

    public void setHusbandResidencyAddress(final String husbandResidencyAddress) {
        this.husbandResidencyAddress = husbandResidencyAddress;
    }

    public String getHusbandOfficeAddress() {
        return husbandOfficeAddress;
    }

    public void setHusbandOfficeAddress(final String husbandOfficeAddress) {
        this.husbandOfficeAddress = husbandOfficeAddress;
    }

    public String getHusbandEmail() {
        return husbandEmail;
    }

    public void setHusbandEmail(final String husbandEmail) {
        this.husbandEmail = husbandEmail;
    }

    public String getWifeName() {
        return wifeName;
    }

    public void setWifeName(final String wifeName) {
        this.wifeName = wifeName;
    }

    public String getWifeReligion() {
        return wifeReligion;
    }

    public void setWifeReligion(final String wifeReligion) {
        this.wifeReligion = wifeReligion;
    }

    public String getWifeReligionPractice() {
        return wifeReligionPractice;
    }

    public void setWifeReligionPractice(final String wifeReligionPractice) {
        this.wifeReligionPractice = wifeReligionPractice;
    }

    public Double getWifeAgeInYearsAsOnMarriage() {
        return wifeAgeInYearsAsOnMarriage;
    }

    public void setWifeAgeInYearsAsOnMarriage(final Double wifeAgeInYearsAsOnMarriage) {
        this.wifeAgeInYearsAsOnMarriage = wifeAgeInYearsAsOnMarriage;
    }

    public Double getWifeAgeInMonthsAsOnMarriage() {
        return wifeAgeInMonthsAsOnMarriage;
    }

    public void setWifeAgeInMonthsAsOnMarriage(final Double wifeAgeInMonthsAsOnMarriage) {
        this.wifeAgeInMonthsAsOnMarriage = wifeAgeInMonthsAsOnMarriage;
    }

    public String getWifeMaritalStatus() {
        return wifeMaritalStatus;
    }

    public void setWifeMaritalStatus(final String wifeMaritalStatus) {
        this.wifeMaritalStatus = wifeMaritalStatus;
    }

    public String getWifeOccupation() {
        return wifeOccupation;
    }

    public void setWifeOccupation(final String wifeOccupation) {
        this.wifeOccupation = wifeOccupation;
    }

    public String getWifeAadhaarNo() {
        return wifeAadhaarNo;
    }

    public void setWifeAadhaarNo(final String wifeAadhaarNo) {
        this.wifeAadhaarNo = wifeAadhaarNo;
    }

    public String getWifePhoneNo() {
        return wifePhoneNo;
    }

    public void setWifePhoneNo(final String wifePhoneNo) {
        this.wifePhoneNo = wifePhoneNo;
    }

    public String getWifeResidencyAddress() {
        return wifeResidencyAddress;
    }

    public void setWifeResidencyAddress(final String wifeResidencyAddress) {
        this.wifeResidencyAddress = wifeResidencyAddress;
    }

    public String getWifeOfficeAddress() {
        return wifeOfficeAddress;
    }

    public void setWifeOfficeAddress(final String wifeOfficeAddress) {
        this.wifeOfficeAddress = wifeOfficeAddress;
    }

    public String getWifeEmail() {
        return wifeEmail;
    }

    public void setWifeEmail(final String wifeEmail) {
        this.wifeEmail = wifeEmail;
    }

    public String getStateType() {
        return stateType;
    }

    public void setStateType(final String stateType) {
        this.stateType = stateType;
    }

    public String getWitness1Name() {
        return witness1Name;
    }

    public void setWitness1Name(final String witness1Name) {
        this.witness1Name = witness1Name;
    }

    public String getWitness1Occupation() {
        return witness1Occupation;
    }

    public void setWitness1Occupation(final String witness1Occupation) {
        this.witness1Occupation = witness1Occupation;
    }

    public String getWitness1RelationshipWithApplicant() {
        return witness1RelationshipWithApplicant;
    }

    public void setWitness1RelationshipWithApplicant(final String witness1RelationshipWithApplicant) {
        this.witness1RelationshipWithApplicant = witness1RelationshipWithApplicant;
    }

    public String getWitness1AadhaarNo() {
        return witness1AadhaarNo;
    }

    public void setWitness1AadhaarNo(final String witness1AadhaarNo) {
        this.witness1AadhaarNo = witness1AadhaarNo;
    }

    public String getWitness1Address() {
        return witness1Address;
    }

    public void setWitness1Address(final String witness1Address) {
        this.witness1Address = witness1Address;
    }

    public String getWitness2Name() {
        return witness2Name;
    }

    public void setWitness2Name(final String witness2Name) {
        this.witness2Name = witness2Name;
    }

    public String getWitness2Occupation() {
        return witness2Occupation;
    }

    public void setWitness2Occupation(final String witness2Occupation) {
        this.witness2Occupation = witness2Occupation;
    }

    public String getWitness2RelationshipWithApplicant() {
        return witness2RelationshipWithApplicant;
    }

    public void setWitness2RelationshipWithApplicant(final String witness2RelationshipWithApplicant) {
        this.witness2RelationshipWithApplicant = witness2RelationshipWithApplicant;
    }

    public String getWitness2AadhaarNo() {
        return witness2AadhaarNo;
    }

    public void setWitness2AadhaarNo(final String witness2AadhaarNo) {
        this.witness2AadhaarNo = witness2AadhaarNo;
    }

    public String getWitness2Address() {
        return witness2Address;
    }

    public void setWitness2Address(final String witness2Address) {
        this.witness2Address = witness2Address;
    }

    public String getWitness3Name() {
        return witness3Name;
    }

    public void setWitness3Name(final String witness3Name) {
        this.witness3Name = witness3Name;
    }

    public String getWitness3Occupation() {
        return witness3Occupation;
    }

    public void setWitness3Occupation(final String witness3Occupation) {
        this.witness3Occupation = witness3Occupation;
    }

    public String getWitness3RelationshipWithApplicant() {
        return witness3RelationshipWithApplicant;
    }

    public void setWitness3RelationshipWithApplicant(final String witness3RelationshipWithApplicant) {
        this.witness3RelationshipWithApplicant = witness3RelationshipWithApplicant;
    }

    public String getWitness3AadhaarNo() {
        return witness3AadhaarNo;
    }

    public void setWitness3AadhaarNo(final String witness3AadhaarNo) {
        this.witness3AadhaarNo = witness3AadhaarNo;
    }

    public String getWitness3Address() {
        return witness3Address;
    }

    public void setWitness3Address(final String witness3Address) {
        this.witness3Address = witness3Address;
    }

    public String getPriestName() {
        return priestName;
    }

    public void setPriestName(final String priestName) {
        this.priestName = priestName;
    }

    public String getPriestAge() {
        return priestAge;
    }

    public void setPriestAge(final String priestAge) {
        this.priestAge = priestAge;
    }

    public String getPriestAddress() {
        return priestAddress;
    }

    public void setPriestAddress(final String priestAddress) {
        this.priestAddress = priestAddress;
    }

    public String getPriestReligion() {
        return priestReligion;
    }

    public void setPriestReligion(final String priestReligion) {
        this.priestReligion = priestReligion;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(final String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(final String certificateType) {
        this.certificateType = certificateType;
    }

    public Date getCertificateDate() {
        return certificateDate;
    }

    public void setCertificateDate(final Date certificateDate) {
        this.certificateDate = certificateDate;
    }

    public boolean isCertificateIssued() {
        return certificateIssued;
    }

    public void setCertificateIssued(final boolean certificateIssued) {
        this.certificateIssued = certificateIssued;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(final String ulbName) {
        this.ulbName = ulbName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(final String districtName) {
        this.districtName = districtName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(final String regionName) {
        this.regionName = regionName;
    }

    public String getUlbGrade() {
        return ulbGrade;
    }

    public void setUlbGrade(final String ulbGrade) {
        this.ulbGrade = ulbGrade;
    }

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(final String ulbCode) {
        this.ulbCode = ulbCode;
    }

    public boolean isHusbandHandicapped() {
        return husbandHandicapped;
    }

    public void setHusbandHandicapped(final boolean husbandHandicapped) {
        this.husbandHandicapped = husbandHandicapped;
    }

    public boolean isWifeHandicapped() {
        return wifeHandicapped;
    }

    public void setWifeHandicapped(final boolean wifeHandicapped) {
        this.wifeHandicapped = wifeHandicapped;
    }

    public String getWitness4Name() {
        return witness4Name;
    }

    public void setWitness4Name(String witness4Name) {
        this.witness4Name = witness4Name;
    }

    public String getWitness4Occupation() {
        return witness4Occupation;
    }

    public void setWitness4Occupation(String witness4Occupation) {
        this.witness4Occupation = witness4Occupation;
    }

    public String getWitness4RelationshipWithApplicant() {
        return witness4RelationshipWithApplicant;
    }

    public void setWitness4RelationshipWithApplicant(String witness4RelationshipWithApplicant) {
        this.witness4RelationshipWithApplicant = witness4RelationshipWithApplicant;
    }

    public String getWitness4AadhaarNo() {
        return witness4AadhaarNo;
    }

    public void setWitness4AadhaarNo(String witness4AadhaarNo) {
        this.witness4AadhaarNo = witness4AadhaarNo;
    }

    public String getWitness4Address() {
        return witness4Address;
    }

    public void setWitness4Address(String witness4Address) {
        this.witness4Address = witness4Address;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    
}