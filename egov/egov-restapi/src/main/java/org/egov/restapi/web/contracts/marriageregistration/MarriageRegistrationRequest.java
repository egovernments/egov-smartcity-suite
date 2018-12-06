/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
package org.egov.restapi.web.contracts.marriageregistration;

import static org.egov.infra.validation.constants.ValidationRegex.ALPHABETS_WITH_SPACE;

import java.util.Date;

import javax.persistence.Embedded;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.egov.mrs.domain.entity.Contact;
import org.egov.mrs.domain.entity.Name;
import org.egov.mrs.domain.enums.MaritalStatus;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

public class MarriageRegistrationRequest {

    @NotNull(message = "provide date of marriage")
    private Date dateOfMarriage;

    @NotNull(message = "provide place of marriage")
    @SafeHtml
    private String placeOfMarriage;

    @NotNull(message = "Provide venue")
    @SafeHtml
    private String venue;

    @NotNull(message = "Provide street")
    @SafeHtml
    private String street;

    @NotNull(message = "Provide city")
    @SafeHtml
    private String city;

    @NotNull(message = "Provide locality")
    private Long locality;

    // husband details
    @NotNull(message = "Provide bridegroom's full name")
    @Embedded
    @Valid
    private Name husbandName;

    @NotNull(message = "Provide bridegroom's religion")
    @SafeHtml
    private String husbandreligion;

    @NotNull(message = "Provide bridegroom's Age in Years")
    private Integer husbnadAgeInYearsAsOnMarriage;

    @NotNull(message = "Provide bridegroom's Age in months")
    private Integer husbandAgeInMonthsAsOnMarriage;

    @NotNull(message = "Provide marital status of bridegroom")
    private MaritalStatus husbandMaritalStatus;

    @SafeHtml
    private String husbandOccupation;

    @NotNull(message = "Provide Aadhaar No. of bridegroom")
    @SafeHtml
    @Length(min=12,max=12,message="BrideGroom AadhaarNo should be 12 digit")
    private String husbandAadhaarNo;

    @NotNull(message = "Provide bridegroom's parents name")
    @SafeHtml
    @Length(max = 110,message="Parents name should be less than 110 character")
    @Pattern(regexp = ALPHABETS_WITH_SPACE, message = "Invalid bridegroom's parent's name")
    private String husbandparentsName;

    @NotNull(message = "Provide bridegroom's eduaction qualification")
    @SafeHtml
    private String husbandQualification;

    @NotNull(message = "Provide bridegroom's Street")
    @SafeHtml
    private String husbandStreet;

    @NotNull(message = "Provide bridegroom's Locality")
    @SafeHtml
    private String husbandLocality;

    @NotNull(message = "Provide bridegroom's city")
    @SafeHtml
    private String husbandCity;

    @NotNull(message = "Provide bridegroom's contact information(mobileNo,email,residenceAddress)")
    private Contact husbandContactInfo;

    private boolean husbandHandicapped;

    // wife details
    @NotNull(message = "Provide bride's full name")
    @Embedded
    @Valid
    private Name wifeName;

    @NotNull(message = "Provide bride's religion")
    @SafeHtml
    private String wifereligion;

    @NotNull(message = "Provide bride's Age in years")
    private Integer wifeAgeInYearsAsOnMarriage;

    @NotNull(message = "Provide bride's age in months")
    private Integer wifeAgeInMonthsAsOnMarriage;

    @NotNull(message = "Provide bride's Marital status")
    private MaritalStatus wifeMaritalStatus;

    @SafeHtml
    private String wifeOccupation;

    @NotNull(message = "Provide bride's Locality")
    @SafeHtml
    @Length(min=12,max=12,message="Bride AadhaarNo should be 12 digit")
    private String wifeAadhaarNo;

    @NotNull(message = "Provide bride's parents name")
    @SafeHtml
    @Length(max = 110,message="Parents name should be less than 110 character")
    @Pattern(regexp = ALPHABETS_WITH_SPACE, message = "Invalid bride's parent's name")
    private String wifeparentsName;

    @NotNull(message = "Provide bride's education qualification")
    @SafeHtml
    private String wifeQualification;

    @NotNull(message = "Provide bride's street")
    @SafeHtml
    private String wifeStreet;

    @NotNull(message = "Provide bride's Locality")
    @SafeHtml
    private String wifeLocality;

    @NotNull(message = "Provide bride's city")
    @SafeHtml
    private String wifeCity;

    private boolean wifeHandicapped;

    @NotNull(message = "Provide bride's contact information")
    private Contact wifeContactInfo;

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

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getHusbnadAgeInYearsAsOnMarriage() {
        return husbnadAgeInYearsAsOnMarriage;
    }

    public void setHusbnadAgeInYearsAsOnMarriage(Integer husbnadAgeInYearsAsOnMarriage) {
        this.husbnadAgeInYearsAsOnMarriage = husbnadAgeInYearsAsOnMarriage;
    }

    public Integer getHusbandAgeInMonthsAsOnMarriage() {
        return husbandAgeInMonthsAsOnMarriage;
    }

    public void setHusbandAgeInMonthsAsOnMarriage(Integer husbandAgeInMonthsAsOnMarriage) {
        this.husbandAgeInMonthsAsOnMarriage = husbandAgeInMonthsAsOnMarriage;
    }

    public MaritalStatus getHusbandMaritalStatus() {
        return husbandMaritalStatus;
    }

    public void setHusbandMaritalStatus(MaritalStatus husbandMaritalStatus) {
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

    public String getHusbandparentsName() {
        return husbandparentsName;
    }

    public void setHusbandparentsName(String husbandparentsName) {
        this.husbandparentsName = husbandparentsName;
    }

    public String getHusbandQualification() {
        return husbandQualification;
    }

    public void setHusbandQualification(String husbandQualification) {
        this.husbandQualification = husbandQualification;
    }

    public String getHusbandStreet() {
        return husbandStreet;
    }

    public void setHusbandStreet(String husbandStreet) {
        this.husbandStreet = husbandStreet;
    }

    public String getHusbandLocality() {
        return husbandLocality;
    }

    public void setHusbandLocality(String husbandLocality) {
        this.husbandLocality = husbandLocality;
    }

    public String getHusbandCity() {
        return husbandCity;
    }

    public void setHusbandCity(String husbandCity) {
        this.husbandCity = husbandCity;
    }

    public Name getHusbandName() {
        return husbandName;
    }

    public void setHusbandName(Name husbandName) {
        this.husbandName = husbandName;
    }

    public String getHusbandreligion() {
        return husbandreligion;
    }

    public void setHusbandreligion(String husbandreligion) {
        this.husbandreligion = husbandreligion;
    }

    public boolean isHusbandHandicapped() {
        return husbandHandicapped;
    }

    public void setHusbandHandicapped(boolean husbandHandicapped) {
        this.husbandHandicapped = husbandHandicapped;
    }

    public Name getWifeName() {
        return wifeName;
    }

    public void setWifeName(Name wifeName) {
        this.wifeName = wifeName;
    }

    public String getWifereligion() {
        return wifereligion;
    }

    public void setWifereligion(String wifereligion) {
        this.wifereligion = wifereligion;
    }

    public Integer getWifeAgeInYearsAsOnMarriage() {
        return wifeAgeInYearsAsOnMarriage;
    }

    public void setWifeAgeInYearsAsOnMarriage(Integer wifeAgeInYearsAsOnMarriage) {
        this.wifeAgeInYearsAsOnMarriage = wifeAgeInYearsAsOnMarriage;
    }

    public Integer getWifeAgeInMonthsAsOnMarriage() {
        return wifeAgeInMonthsAsOnMarriage;
    }

    public void setWifeAgeInMonthsAsOnMarriage(Integer wifeAgeInMonthsAsOnMarriage) {
        this.wifeAgeInMonthsAsOnMarriage = wifeAgeInMonthsAsOnMarriage;
    }

    public MaritalStatus getWifeMaritalStatus() {
        return wifeMaritalStatus;
    }

    public void setWifeMaritalStatus(MaritalStatus wifeMaritalStatus) {
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

    public String getWifeparentsName() {
        return wifeparentsName;
    }

    public void setWifeparentsName(String wifeparentsName) {
        this.wifeparentsName = wifeparentsName;
    }

    public String getWifeQualification() {
        return wifeQualification;
    }

    public void setWifeQualification(String wifeQualification) {
        this.wifeQualification = wifeQualification;
    }

    public String getWifeStreet() {
        return wifeStreet;
    }

    public void setWifeStreet(String wifeStreet) {
        this.wifeStreet = wifeStreet;
    }

    public String getWifeLocality() {
        return wifeLocality;
    }

    public void setWifeLocality(String wifeLocality) {
        this.wifeLocality = wifeLocality;
    }

    public String getWifeCity() {
        return wifeCity;
    }

    public void setWifeCity(String wifeCity) {
        this.wifeCity = wifeCity;
    }

    public boolean isWifeHandicapped() {
        return wifeHandicapped;
    }

    public void setWifeHandicapped(boolean wifeHandicapped) {
        this.wifeHandicapped = wifeHandicapped;
    }

    public Long getLocality() {
        return locality;
    }

    public void setLocality(Long locality) {
        this.locality = locality;
    }

    public Contact getHusbandContactInfo() {
        return husbandContactInfo;
    }

    public void setHusbandContactInfo(Contact husbandContactInfo) {
        this.husbandContactInfo = husbandContactInfo;
    }

    public Contact getWifeContactInfo() {
        return wifeContactInfo;
    }

    public void setWifeContactInfo(Contact wifeContactInfo) {
        this.wifeContactInfo = wifeContactInfo;
    }

}
