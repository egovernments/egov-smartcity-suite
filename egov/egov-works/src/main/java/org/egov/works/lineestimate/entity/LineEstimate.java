/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.works.lineestimate.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.egov.commons.CFunction;
import org.egov.commons.EgwStatus;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.model.budget.BudgetGroup;
import org.egov.works.lineestimate.entity.enums.Beneficiary;
import org.egov.works.lineestimate.entity.enums.WorkCategory;
import org.egov.works.models.masters.NatureOfWork;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "EGW_LINEESTIMATE")
@Unique(id = "id", tableName = "EGW_LINEESTIMATE", columnName = { "lineestimatenumber" }, fields = {
        "lineEstimateNumber" }, enableDfltMsg = true)
@SequenceGenerator(name = LineEstimate.SEQ_EGW_LINEESTIMATE, sequenceName = LineEstimate.SEQ_EGW_LINEESTIMATE, allocationSize = 1)
public class LineEstimate extends StateAware {

    private static final long serialVersionUID = -366602348464540736L;

    public static final String SEQ_EGW_LINEESTIMATE = "SEQ_EGW_LINEESTIMATE";

    @Id
    @GeneratedValue(generator = SEQ_EGW_LINEESTIMATE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @SafeHtml
    @Length(max = 50)
    @Column(unique = true)
    private String lineEstimateNumber;

    @NotNull
    @SafeHtml
    @Length(max = 256)
    private String subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fund", nullable = false)
    private Fund fund;

    @NotNull
    @SafeHtml
    @Length(max = 1024)
    private String reference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "function", nullable = false)
    private CFunction function;

    @NotNull
    @SafeHtml
    @Length(max = 1024)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budgethead", nullable = false)
    private BudgetGroup budgetHead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme")
    private Scheme scheme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscheme")
    private SubScheme subScheme;

    @NotNull
    @Temporal(value = TemporalType.DATE)
    private Date lineEstimateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executingdepartment", nullable = false)
    private Department executingDepartment;

    @SafeHtml
    @Length(max = 50)
    private String adminSanctionNumber;

    @Temporal(TemporalType.DATE)
    private Date adminSanctionDate;

    private String adminSanctionBy;

    @OrderBy("id")
    @OneToMany(mappedBy = "lineEstimate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = LineEstimateDetails.class)
    private final List<LineEstimateDetails> lineEstimateDetails = new ArrayList<LineEstimateDetails>(0);

    private final transient List<DocumentDetails> documentDetails = new ArrayList<DocumentDetails>(0);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", nullable = false)
    private EgwStatus status;

    @Transient
    private Long approvalDepartment;

    @Transient
    private String approvalComent;

    @Enumerated(EnumType.STRING)
    private Beneficiary beneficiary;

    @NotNull
    private String modeOfAllotment;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typeofwork")
    private EgwTypeOfWork typeOfWork;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subtypeofwork")
    private EgwTypeOfWork subTypeOfWork;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "natureofwork", nullable = false)
    private NatureOfWork natureOfWork;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward", nullable = false)
    private Boundary ward;

    @SafeHtml
    @Length(max = 50)
    private String technicalSanctionNumber;

    @Temporal(TemporalType.DATE)
    private Date technicalSanctionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technicalSanctionBy")
    private User technicalSanctionBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location")
    private Boundary location;

    @NotNull
    @Enumerated(EnumType.STRING)
    private WorkCategory workCategory;

    private String councilResolutionNumber;

    @Temporal(TemporalType.DATE)
    private Date councilResolutionDate;

    private boolean workOrderCreated;

    private boolean billsCreated;

    private boolean spillOverFlag;

    private String cancellationReason;

    private String cancellationRemarks;

    @Transient
    private List<LineEstimateDetails> tempLineEstimateDetails = new ArrayList<LineEstimateDetails>(0);

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getLineEstimateNumber() {
        return lineEstimateNumber;
    }

    public void setLineEstimateNumber(final String lineEstimateNumber) {
        this.lineEstimateNumber = lineEstimateNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(final String reference) {
        this.reference = reference;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(final CFunction function) {
        this.function = function;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public BudgetGroup getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(final BudgetGroup budgetHead) {
        this.budgetHead = budgetHead;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(final Scheme scheme) {
        this.scheme = scheme;
    }

    public SubScheme getSubScheme() {
        return subScheme;
    }

    public void setSubScheme(final SubScheme subScheme) {
        this.subScheme = subScheme;
    }

    public Date getLineEstimateDate() {
        return lineEstimateDate;
    }

    public void setLineEstimateDate(final Date lineEstimateDate) {
        this.lineEstimateDate = lineEstimateDate;
    }

    public Department getExecutingDepartment() {
        return executingDepartment;
    }

    public void setExecutingDepartment(final Department executingDepartment) {
        this.executingDepartment = executingDepartment;
    }

    public List<LineEstimateDetails> getLineEstimateDetails() {
        return lineEstimateDetails;
    }

    public void setLineEstimateDetails(final List<LineEstimateDetails> lineEstimateDetails) {
        this.lineEstimateDetails.clear();
        if (lineEstimateDetails != null)
            this.lineEstimateDetails.addAll(lineEstimateDetails);
    }

    public List<DocumentDetails> getDocumentDetails() {
        return documentDetails;
    }

    public void setDocumentDetails(final List<DocumentDetails> documentDetails) {
        this.documentDetails.clear();
        if (documentDetails != null)
            this.documentDetails.addAll(documentDetails);
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(final EgwStatus status) {
        this.status = status;
    }

    @Override
    public String getStateDetails() {
        return "Estimate Number : " + getLineEstimateNumber();
    }

    public String getAdminSanctionNumber() {
        return adminSanctionNumber;
    }

    public void setAdminSanctionNumber(final String adminSanctionNumber) {
        this.adminSanctionNumber = adminSanctionNumber;
    }

    public Date getAdminSanctionDate() {
        return adminSanctionDate;
    }

    public void setAdminSanctionDate(final Date adminSanctionDate) {
        this.adminSanctionDate = adminSanctionDate;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(final Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getModeOfAllotment() {
        return modeOfAllotment;
    }

    public void setModeOfAllotment(final String modeOfAllotment) {
        this.modeOfAllotment = modeOfAllotment;
    }

    public EgwTypeOfWork getTypeOfWork() {
        return typeOfWork;
    }

    public void setTypeOfWork(final EgwTypeOfWork typeOfWork) {
        this.typeOfWork = typeOfWork;
    }

    public NatureOfWork getNatureOfWork() {
        return natureOfWork;
    }

    public void setNatureOfWork(final NatureOfWork natureOfWork) {
        this.natureOfWork = natureOfWork;
    }

    public Boundary getWard() {
        return ward;
    }

    public void setWard(final Boundary ward) {
        this.ward = ward;
    }

    public EgwTypeOfWork getSubTypeOfWork() {
        return subTypeOfWork;
    }

    public void setSubTypeOfWork(final EgwTypeOfWork subTypeOfWork) {
        this.subTypeOfWork = subTypeOfWork;
    }

    public Boundary getLocation() {
        return location;
    }

    public void setLocation(final Boundary location) {
        this.location = location;
    }

    public WorkCategory getWorkCategory() {
        return workCategory;
    }

    public void setWorkCategory(final WorkCategory workCategory) {
        this.workCategory = workCategory;
    }

    public Long getApprovalDepartment() {
        return approvalDepartment;
    }

    public void setApprovalDepartment(final Long approvalDepartment) {
        this.approvalDepartment = approvalDepartment;
    }

    public String getApprovalComent() {
        return approvalComent;
    }

    public void setApprovalComent(final String approvalComent) {
        this.approvalComent = approvalComent;
    }

    public String getTechnicalSanctionNumber() {
        return technicalSanctionNumber;
    }

    public void setTechnicalSanctionNumber(final String technicalSanctionNumber) {
        this.technicalSanctionNumber = technicalSanctionNumber;
    }

    public Date getTechnicalSanctionDate() {
        return technicalSanctionDate;
    }

    public void setTechnicalSanctionDate(final Date technicalSanctionDate) {
        this.technicalSanctionDate = technicalSanctionDate;
    }

    public User getTechnicalSanctionBy() {
        return technicalSanctionBy;
    }

    public void setTechnicalSanctionBy(final User technicalSanctionBy) {
        this.technicalSanctionBy = technicalSanctionBy;
    }

    public String getCouncilResolutionNumber() {
        return councilResolutionNumber;
    }

    public void setCouncilResolutionNumber(final String councilResolutionNumber) {
        this.councilResolutionNumber = councilResolutionNumber;
    }

    public Date getCouncilResolutionDate() {
        return councilResolutionDate;
    }

    public void setCouncilResolutionDate(final Date councilResolutionDate) {
        this.councilResolutionDate = councilResolutionDate;
    }

    public boolean isWorkOrderCreated() {
        return workOrderCreated;
    }

    public void setWorkOrderCreated(final boolean workOrderCreated) {
        this.workOrderCreated = workOrderCreated;
    }

    public boolean isBillsCreated() {
        return billsCreated;
    }

    public void setBillsCreated(final boolean billsCreated) {
        this.billsCreated = billsCreated;
    }

    public boolean isSpillOverFlag() {
        return spillOverFlag;
    }

    public void setSpillOverFlag(final boolean spillOverFlag) {
        this.spillOverFlag = spillOverFlag;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(final String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getCancellationRemarks() {
        return cancellationRemarks;
    }

    public void setCancellationRemarks(final String cancellationRemarks) {
        this.cancellationRemarks = cancellationRemarks;
    }

    public List<LineEstimateDetails> getTempLineEstimateDetails() {
        return tempLineEstimateDetails;
    }

    public void setTempLineEstimateDetails(final List<LineEstimateDetails> tempLineEstimateDetails) {
        this.tempLineEstimateDetails = tempLineEstimateDetails;
    }

    public String getAdminSanctionBy() {
        return adminSanctionBy;
    }

    public void setAdminSanctionBy(final String adminSanctionBy) {
        this.adminSanctionBy = adminSanctionBy;
    }
}
