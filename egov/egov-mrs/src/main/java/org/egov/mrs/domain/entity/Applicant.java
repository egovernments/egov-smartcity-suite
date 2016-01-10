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

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.mrs.domain.enums.RelationStatus;
import org.egov.mrs.domain.enums.ReligionPractice;
import org.egov.mrs.masters.entity.Religion;
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

    @Embedded
    private Name name;

    @SafeHtml
    @Length(max = 20)
    private String otherName;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "religion")
    private Religion religion;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReligionPractice religionPractice;

    @NotNull
    private Integer ageInYearsAsOnMarriage;

    @NotNull
    @Length(min = 1)
    private Integer ageInMonthsAsOnMarriage;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RelationStatus presentRelation;

    @NotNull
    @SafeHtml
    @Length(max = 60)
    private String occupation;

    @SafeHtml
    @Length(max = 20)
    private String aadhaarNo;

    private byte[] photo;
    private byte[] signature;

    @NotNull
    @Valid
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "proofsattached")
    private IdentityProof proofsAttached;

    @Embedded
    private Contact contactInfo;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
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

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(final String occupation) {
        this.occupation = occupation;
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

    public Name getName() {
        return name;
    }

    public void setName(final Name name) {
        this.name = name;
    }

    public IdentityProof getProofsAttached() {
        return proofsAttached;
    }

    public void setProofsAttached(final IdentityProof proofsAttached) {
        this.proofsAttached = proofsAttached;
    }

    public Contact getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(final Contact contactInfo) {
        this.contactInfo = contactInfo;
    }
}
