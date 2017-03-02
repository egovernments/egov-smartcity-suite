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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.commons.EgwStatus;
import org.egov.demand.model.EgDemand;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.mrs.masters.entity.MarriageFee;
import org.egov.mrs.masters.entity.MarriageRegistrationUnit;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;
import org.egov.mrs.domain.entity.MrApplicant; 

/**
 * 
 * @author nayeem
 *
 */
@Entity
@Table(name = "egmrs_reissue")
@SequenceGenerator(name = ReIssue.SEQ_REISSUE, sequenceName = ReIssue.SEQ_REISSUE, allocationSize = 1)
public class ReIssue extends StateAware {

    public enum ReIssueStatus {
        CREATED, APPROVED, REJECTED, CANCELLED, CERTIFICATEREISSUED, DIGITALSIGNED
    }

    private static final long serialVersionUID = 7398043339748917008L;

    public static final String SEQ_REISSUE = "SEQ_EGMRS_REISSUE";

    @Id
    @GeneratedValue(generator = SEQ_REISSUE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String applicationNo;

    @NotNull
    private Date applicationDate;

    private Date reIssueDate;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone")
    private Boundary zone;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrationUnit")
    private MarriageRegistrationUnit marriageRegistrationUnit;

    @NotNull
    @Valid
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "applicant")
    private MrApplicant applicant;

    @NotNull
    @Valid
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "registration")
    private MarriageRegistration registration;

    @ManyToOne
    @JoinColumn(name = "feecriteria", nullable = false)
    private MarriageFee feeCriteria;

    @NotNull
    private Double feePaid;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "demand")
    private EgDemand demand;

    @ManyToOne
    @JoinColumn(name = "status", nullable = false)
    private EgwStatus status;

    private boolean isActive;

    @Length(max = 256)
    private String rejectionReason;

    @Length(max = 256)
    private String remarks;

    @Transient
    private Long approvalDepartment;

    @Transient
    private String approvalComent;
    
    @SafeHtml
    @Length(max = 15)
    private String source;

    @OneToMany(mappedBy = "reIssue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MarriageCertificate> marriageCertificate = new ArrayList<>();

    @Override
    public String getStateDetails() {
        return "Marriage registration re-issue application no : " + applicationNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MrApplicant getApplicant() {
        return applicant;
    }

    public void setApplicant(MrApplicant applicant) {
        this.applicant = applicant;
    }

    public MarriageRegistration getRegistration() {
        return registration;
    }

    public void setRegistration(MarriageRegistration registration) {
        this.registration = registration;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Date getReIssueDate() {
        return reIssueDate;
    }

    public void setReIssueDate(Date reIssueDate) {
        this.reIssueDate = reIssueDate;
    }

    public MarriageFee getFeeCriteria() {
        return feeCriteria;
    }

    public void setFeeCriteria(MarriageFee feeCriteria) {
        this.feeCriteria = feeCriteria;
    }

    public Double getFeePaid() {
        return feePaid;
    }

    public void setFeePaid(Double feePaid) {
        this.feePaid = feePaid;
    }

    public EgDemand getDemand() {
        return demand;
    }

    public void setDemand(EgDemand demand) {
        this.demand = demand;
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

    public Long getApprovalDepartment() {
        return approvalDepartment;
    }

    public void setApprovalDepartment(Long approvalDepartment) {
        this.approvalDepartment = approvalDepartment;
    }

    public String getApprovalComent() {
        return approvalComent;
    }

    public void setApprovalComent(String approvalComent) {
        this.approvalComent = approvalComent;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(EgwStatus status) {
        this.status = status;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public List<MarriageCertificate> getMarriageCertificate() {
        return marriageCertificate;
    }

    public void setMarriageCertificate(List<MarriageCertificate> marriageCertificate) {
        this.marriageCertificate = marriageCertificate;
    }

    public void addCertificate(final MarriageCertificate certificate) {
        getMarriageCertificate().add(certificate);
    }

    public void removeCertificate(final MarriageCertificate certificate) {
        getMarriageCertificate().remove(certificate);
    }

    public boolean isFeeCollected() {
        return demand.getBaseDemand().compareTo(demand.getAmtCollected()) <= 0 ? true : false; 
    }

    public Boundary getZone() {
        return zone;
    }

    public void setZone(Boundary zone) {
        this.zone = zone;
    }

    public MarriageRegistrationUnit getMarriageRegistrationUnit() {
        return marriageRegistrationUnit;
    }

    public void setMarriageRegistrationUnit(
            MarriageRegistrationUnit marriageRegistrationUnit) {
        this.marriageRegistrationUnit = marriageRegistrationUnit;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    
}
