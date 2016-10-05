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

import java.util.Date;

import javax.persistence.CascadeType;
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
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.demand.model.EgDemand;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.mrs.domain.enums.ApplicationStatus;
import org.hibernate.validator.constraints.Length;

/**
 * 
 * @author nayeem
 *
 */
@Entity
@Table(name = "egmrs_reissue")
@SequenceGenerator(name = ReIssue.SEQ_REISSUE, sequenceName = ReIssue.SEQ_REISSUE, allocationSize = 1)
public class ReIssue extends StateAware {
    
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
    @Valid
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "applicant")    
    private Applicant applicant;
    
    @NotNull
    @Valid
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "registration")
    private MarriageRegistration registration;
    
    @NotNull
    private String feeCriteria;
    
    @NotNull
    private Double feePaid;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "demand")
    private EgDemand demand;

    @NotNull
    @Length(max = 30)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Length(max = 256)
    private String rejectionReason;

    @Length(max = 256)
    private String remarks;

    private boolean certificateIssued;

    @Transient
    private Long approvalDepartment;

    @Transient
    private String approvalComent;

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
    
    public Applicant getApplicant() {
        return applicant;
    }
    
    public void setApplicant(Applicant applicant) {
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
    
    public String getFeeCriteria() {
        return feeCriteria;
    }
    
    public void setFeeCriteria(String feeCriteria) {
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
    
    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
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
    
    public boolean isCertificateIssued() {
        return certificateIssued;
    }
    
    public void setCertificateIssued(boolean certificateIssued) {
        this.certificateIssued = certificateIssued;
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
}
