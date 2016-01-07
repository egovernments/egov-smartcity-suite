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

package org.egov.mrs.domain.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.validation.regex.Constants;
import org.egov.mrs.domain.enums.RelationStatus;
import org.egov.mrs.domain.enums.ReligionPractice;
import org.egov.mrs.masters.entity.Religion;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "egmrs_applicant")
@SequenceGenerator(name = Applicant.SEQ_APPLICANT, sequenceName = Applicant.SEQ_APPLICANT, allocationSize = 1)
public class Applicant extends AbstractAuditable {

    private static final long serialVersionUID = -4678440835941976527L;
    public static final String SEQ_APPLICANT = "SEQ_EGMRS_APPLICANT";

    @Id
    @GeneratedValue(generator = SEQ_APPLICANT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @SafeHtml
    @Length(max = 30)
    private String firstName;

    @NotNull
    @SafeHtml
    @Length(max = 20)
    private String middleName;

    @NotNull
    @SafeHtml
    @Length(max = 20)
    private String lastName;

    @NotNull
    @SafeHtml
    @Length(max = 20)
    private String otherName;

    @NotNull
    private Religion religion;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReligionPractice religionPractice;

    @NotNull
    @Length(min = 1)
    private Integer ageInYearsAsOnMarriage;

    @NotNull
    private Integer ageInMonthsAsOnMarriage;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RelationStatus presentRelation;

    @NotNull
    @SafeHtml
    @Length(max = 150)
    private String residenceAddress;

    @NotNull
    @SafeHtml
    @Length(max = 150)
    private String officeAddress;

    @NotNull
    @SafeHtml
    @Length(max = 60)
    private String occupation;

    @Pattern(regexp = Constants.MOBILE_NUM)
    @SafeHtml
    @Length(max = 15)
    @Audited
    private Integer mobileNo;

    @SafeHtml
    @Audited
    @Length(max = 128)
    @Email(regexp = Constants.EMAIL)
    private String email;

    @SafeHtml
    @Length(max = 20)
    private String aadhaarNo;

    private byte[] photo;
    private byte[] signature;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(final String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(final String otherName) {
        this.otherName = otherName;
    }

    public Religion getReligion() {
        return religion;
    }

    public void setReligion(final Religion religion) {
        this.religion = religion;
    }

    public ReligionPractice getReligionPractice() {
        return religionPractice;
    }

    public void setReligionPractice(final ReligionPractice religionPractice) {
        this.religionPractice = religionPractice;
    }

    public Integer getAgeInYearsAsOnMarriage() {
        return ageInYearsAsOnMarriage;
    }

    public void setAgeInYearsAsOnMarriage(final Integer ageInYearsAsOnMarriage) {
        this.ageInYearsAsOnMarriage = ageInYearsAsOnMarriage;
    }

    public Integer getAgeInMonthsAsOnMarriage() {
        return ageInMonthsAsOnMarriage;
    }

    public void setAgeInMonthsAsOnMarriage(final Integer ageInMonthsAsOnMarriage) {
        this.ageInMonthsAsOnMarriage = ageInMonthsAsOnMarriage;
    }

    public RelationStatus getPresentRelation() {
        return presentRelation;
    }

    public void setPresentRelation(final RelationStatus presentRelation) {
        this.presentRelation = presentRelation;
    }

    public String getResidenceAddress() {
        return residenceAddress;
    }

    public void setResidenceAddress(final String residenceAddress) {
        this.residenceAddress = residenceAddress;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(final String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(final String occupation) {
        this.occupation = occupation;
    }

    public Integer getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(final Integer mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getAadhaarNo() {
        return aadhaarNo;
    }

    public void setAadhaarNo(final String aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(final byte[] signature) {
        this.signature = signature;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(final byte[] photo) {
        this.photo = photo;
    }

}
