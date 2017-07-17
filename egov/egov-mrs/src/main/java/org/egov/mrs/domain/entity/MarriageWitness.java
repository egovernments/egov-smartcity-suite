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
import javax.persistence.Column;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.mrs.domain.enums.MRApplicantType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "egmrs_witness")
@SequenceGenerator(name = MarriageWitness.SEQ_WITNESS, sequenceName = MarriageWitness.SEQ_WITNESS, allocationSize = 1)
public class MarriageWitness extends AbstractAuditable {

    private static final long serialVersionUID = 8963626310849299317L;
    public static final String SEQ_WITNESS = "SEQ_EGMRS_WITNESS";

    @Id
    @GeneratedValue(generator = SEQ_WITNESS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Embedded
    private Name name;

    @NotNull
    @SafeHtml
    @Length(max = 60)
    private String occupation;

    @NotNull
    @SafeHtml
    @Length(max = 30)
    private String relationshipWithApplicant;

    @NotNull
    @Length(min = 25)
    private Integer age;

    @NotNull
    @SafeHtml
    @Length(max = 30)
    private String witnessRelation;

    @NotNull
    @SafeHtml
    @Length(max = 70)
    private String relativeName;

    @SafeHtml
    @Length(max = 20)
    private String aadhaarNo;
    @Transient
    private byte[] signature;
    @Transient
    private byte[] photo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photoFileStore")
    private FileStoreMapper photoFileStore;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "signatureFileStore")
    private FileStoreMapper signatureFileStore;

    private transient MultipartFile photoFile;
    private transient MultipartFile signatureFile;
    private transient String encodedPhoto;
    private transient String encodedSignature;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration")
    private MarriageRegistration registration;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "applicantType")
    private MRApplicantType applicantType;

    @Embedded
    private Contact contactInfo;

    public String getFullName() {
        String fullName = getName().getFirstName();

        fullName += getName().getMiddleName() == null ? "" : " " + getName().getMiddleName();
        fullName += getName().getLastName() == null ? "" : " " + getName().getLastName();

        return fullName;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }

    public void setName(final Name name) {
        this.name = name;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(final String occupation) {
        this.occupation = occupation;
    }

    public String getRelationshipWithApplicant() {
        return relationshipWithApplicant;
    }

    public void setRelationshipWithApplicant(final String relationshipWithApplicant) {
        this.relationshipWithApplicant = relationshipWithApplicant;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(final Integer age) {
        this.age = age;
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

    public MarriageRegistration getRegistration() {
        return registration;
    }

    public void setRegistration(final MarriageRegistration registration) {
        this.registration = registration;
    }

    public Contact getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(final Contact contactInfo) {
        this.contactInfo = contactInfo;
    }

    public MultipartFile getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(final MultipartFile photoFile) {
        this.photoFile = photoFile;
    }

    public MultipartFile getSignatureFile() {
        return signatureFile;
    }

    public void setSignatureFile(final MultipartFile signatureFile) {
        this.signatureFile = signatureFile;
    }

    public String getEncodedPhoto() {
        return encodedPhoto;
    }

    public void setEncodedPhoto(final String encodedPhoto) {
        this.encodedPhoto = encodedPhoto;
    }

    public String getEncodedSignature() {
        return encodedSignature;
    }

    public void setEncodedSignature(final String encodedSignature) {
        this.encodedSignature = encodedSignature;
    }

    public FileStoreMapper getPhotoFileStore() {
        return photoFileStore;
    }

    public void setPhotoFileStore(final FileStoreMapper photoFileStore) {
        this.photoFileStore = photoFileStore;
    }

    public FileStoreMapper getSignatureFileStore() {
        return signatureFileStore;
    }

    public void setSignatureFileStore(final FileStoreMapper signatureFileStore) {
        this.signatureFileStore = signatureFileStore;
    }

    public MRApplicantType getApplicantType() {
        return applicantType;
    }

    public void setApplicantType(final MRApplicantType applicantType) {
        this.applicantType = applicantType;
    }

    public String getWitnessRelation() {
        return witnessRelation;
    }

    public void setWitnessRelation(final String witnessRelation) {
        this.witnessRelation = witnessRelation;
    }

    public String getRelativeName() {
        return relativeName;
    }

    public void setRelativeName(final String relativeName) {
        this.relativeName = relativeName;
    }

}
