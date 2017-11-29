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

package org.egov.council.entity;

import org.egov.common.entity.EducationalQualification;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.entity.enums.Gender;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static org.egov.council.entity.CouncilMember.SEQ_COUNCILMEMBER;

@Entity
@Unique(fields = {"name"}, enableDfltMsg = true)
@Table(name = "egcncl_members")
@SequenceGenerator(name = SEQ_COUNCILMEMBER, sequenceName = SEQ_COUNCILMEMBER, allocationSize = 1)
public class CouncilMember extends AbstractAuditable {

    public static final String SEQ_COUNCILMEMBER = "seq_egcncl_members";
    private static final long serialVersionUID = 8227838067322332444L;
    @Id
    @GeneratedValue(generator = SEQ_COUNCILMEMBER, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "electionWard")
    private Boundary electionWard;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "designation")
    private CouncilDesignation designation;

    @ManyToOne
    @JoinColumn(name = "qualification")
    private EducationalQualification qualification;
    @ManyToOne
    @JoinColumn(name = "caste")
    private CouncilCaste caste;

    @ManyToOne
    @JoinColumn(name = "partyAffiliation")
    private CouncilParty partyAffiliation;
    
    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private Gender gender;


    private Date birthDate;

    private Date electionDate;

    private Date oathDate;


    @Length(max = 15)
    private String mobileNumber;


    @Length(max = 52)
    private String emailId;

    @NotNull
    @Length(min = 2, max = 100)
    private String name;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private CouncilMemberStatus status = CouncilMemberStatus.ACTIVE;

    @NotNull
    private String residentialAddress;


    @Transient
    private MultipartFile attachments;

    @Transient
    private Boolean checked;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "filestoreid")
    private FileStoreMapper photo;
    
    private String category;
    
    private Date dateOfJoining;
    
    public FileStoreMapper getPhoto() {
        return photo;
    }

    public void setPhoto(FileStoreMapper photo) {
        this.photo = photo;
    }

    public MultipartFile getAttachments() {
        return attachments;
    }

    public void setAttachments(MultipartFile attachments) {
        this.attachments = attachments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boundary getElectionWard() {
        return electionWard;
    }

    public void setElectionWard(Boundary electionWard) {
        this.electionWard = electionWard;
    }

    public CouncilDesignation getDesignation() {
        return designation;
    }

    public void setDesignation(CouncilDesignation designation) {
        this.designation = designation;
    }

    public EducationalQualification getQualification() {
        return qualification;
    }

    public void setQualification(EducationalQualification qualification) {
        this.qualification = qualification;
    }

    public CouncilCaste getCaste() {
        return caste;
    }

    public void setCaste(CouncilCaste caste) {
        this.caste = caste;
    }

    public CouncilParty getPartyAffiliation() {
        return partyAffiliation;
    }

    public void setPartyAffiliation(CouncilParty partyAffiliation) {
        this.partyAffiliation = partyAffiliation;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getElectionDate() {
        return electionDate;
    }

    public void setElectionDate(Date electionDate) {
        this.electionDate = electionDate;
    }

    public Date getOathDate() {
        return oathDate;
    }

    public void setOathDate(Date oathDate) {
        this.oathDate = oathDate;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }


    public CouncilMemberStatus getStatus() {
        return status;
    }

    public void setStatus(CouncilMemberStatus status) {
        this.status = status;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(Date dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    
    
}
