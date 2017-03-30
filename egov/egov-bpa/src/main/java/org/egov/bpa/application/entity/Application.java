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
package org.egov.bpa.application.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.egov.bpa.application.entity.enums.Occupancy;
import org.egov.commons.entity.Source;
import org.egov.demand.model.EgDemand;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGBPA_APPLICATION")
@SequenceGenerator(name = Application.SEQ_APPLICATION, sequenceName = Application.SEQ_APPLICATION, allocationSize = 1)
public class Application extends AbstractAuditable {

    private static final long serialVersionUID = -361205348191992865L;
    public static final String SEQ_APPLICATION = "SEQ_EGBPA_APPLICATION";

    @Id
    @GeneratedValue(generator = SEQ_APPLICATION, strategy = GenerationType.SEQUENCE)
    private Long id;


    @Length(min = 1, max = 128)
    private String buildingplanapprovalnumber;
    @Temporal(value = TemporalType.DATE)
    private Date buildingPlanApprovalDate;
    @NotNull
    @Length(min = 1, max = 128)
    private String applicationNumber;
    @NotNull
    @Temporal(value = TemporalType.DATE)
    private Date applicationDate;
    @Temporal(value = TemporalType.DATE)
    private Date approvalDate;

    @Length(min = 1, max = 128)
    private String assessmentNumber;
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Source source;
    @NotNull
    @Length(min = 1, max = 128)
    private String applicantType;
    // same as source
    @NotNull
    @Length(min = 1, max = 128)
    private String applicantMode;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "serviceType")
    private ServiceType serviceType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "status")
    private BpaStatus status;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User owner;
    @Length(min = 1, max = 128)
    private String planPermissionNumber;
    @Temporal(value = TemporalType.DATE)
    private Date planPermissionDate;
    @Length(min = 1, max = 128)
    private String oldApplicationNumber;
    @Length(min = 1, max = 128)
    private String tapalNumber;

    @Enumerated(EnumType.ORDINAL)
    private Occupancy occupancy;
    @Length(min = 1, max = 128)
    private String governmentType;// Government or Quasi Govt

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "demand")
    private EgDemand demand;

    @Length(min = 1, max = 128)
    private String tapanNumber;
    @Length(min = 1, max = 128)
    private String remarks;
    @Length(min = 1, max = 128)
    private String projectName;
    @Length(min = 1, max = 128)
    private String groupDevelopment;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "siteDetail")
    private SiteDetail siteDetail;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "buildingDetail")
    private BuildingDetail buildingDetail;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "documentHistory")
    private DocumentHistory documentHistory;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "permittedFloorDetail")
    private PermittedFloorDetail permittedFloorDetail;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AutoDcrMap> autoDcr = new ArrayList<>();
    @OneToMany(mappedBy = "stakeHolder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ApplicationStakeHolder> stakeHolder = new ArrayList<>(0);
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ApplicationDocument> applicationDocument = new ArrayList<>(0);
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ApplicationNocDocument> applicationNOCDocument = new ArrayList<>(0);
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inspection> inspections = new ArrayList<>();
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LettertoParty> lettertoParty = new ArrayList<>();

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ApplicationFee> applicationFee = new ArrayList<>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getBuildingplanapprovalnumber() {
        return buildingplanapprovalnumber;
    }

    public void setBuildingplanapprovalnumber(final String buildingplanapprovalnumber) {
        this.buildingplanapprovalnumber = buildingplanapprovalnumber;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(final String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(final Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getAssessmentNumber() {
        return assessmentNumber;
    }

    public void setAssessmentNumber(final String assessmentNumber) {
        this.assessmentNumber = assessmentNumber;
    }

    public String getApplicantType() {
        return applicantType;
    }

    public void setApplicantType(final String applicantType) {
        this.applicantType = applicantType;
    }

    public String getApplicantMode() {
        return applicantMode;
    }

    public void setApplicantMode(final String applicantMode) {
        this.applicantMode = applicantMode;
    }

    public BpaStatus getStatus() {
        return status;
    }

    public void setStatus(final BpaStatus status) {
        this.status = status;
    }

    public String getPlanPermissionNumber() {
        return planPermissionNumber;
    }

    public void setPlanPermissionNumber(final String planPermissionNumber) {
        this.planPermissionNumber = planPermissionNumber;
    }

    public Date getPlanPermissionDate() {
        return planPermissionDate;
    }

    public void setPlanPermissionDate(final Date planPermissionDate) {
        this.planPermissionDate = planPermissionDate;
    }

    public String getOldApplicationNumber() {
        return oldApplicationNumber;
    }

    public void setOldApplicationNumber(final String oldApplicationNumber) {
        this.oldApplicationNumber = oldApplicationNumber;
    }

    public String getTapalNumber() {
        return tapalNumber;
    }

    public void setTapalNumber(final String tapalNumber) {
        this.tapalNumber = tapalNumber;
    }

    public Occupancy getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(final Occupancy occupancy) {
        this.occupancy = occupancy;
    }

    public String getGovernmentType() {
        return governmentType;
    }

    public void setGovernmentType(final String governmentType) {
        this.governmentType = governmentType;
    }

    public EgDemand getDemand() {
        return demand;
    }

    public void setDemand(final EgDemand demand) {
        this.demand = demand;
    }

    public String getTapanNumber() {
        return tapanNumber;
    }

    public void setTapanNumber(final String tapanNumber) {
        this.tapanNumber = tapanNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(final String projectName) {
        this.projectName = projectName;
    }

    public String getGroupDevelopment() {
        return groupDevelopment;
    }

    public void setGroupDevelopment(String groupDevelopment) {
        this.groupDevelopment = groupDevelopment;
    }

    public List<AutoDcrMap> getAutoDcr() {
        return autoDcr;
    }

    public void setAutoDcr(List<AutoDcrMap> autoDcr) {
        this.autoDcr = autoDcr;
    }

    public List<Inspection> getInspections() {
        return inspections;
    }

    public void setInspections(List<Inspection> inspections) {
        this.inspections = inspections;
    }

    public BuildingDetail getBuildingDetail() {
        return buildingDetail;
    }

    public void setBuildingDetail(BuildingDetail buildingDetail) {
        this.buildingDetail = buildingDetail;
    }

    public DocumentHistory getDocumentHistory() {
        return documentHistory;
    }

    public void setDocumentHistory(DocumentHistory documentHistory) {
        this.documentHistory = documentHistory;
    }

    public PermittedFloorDetail getPermittedFloorDetail() {
        return permittedFloorDetail;
    }

    public void setPermittedFloorDetail(PermittedFloorDetail permittedFloorDetail) {
        this.permittedFloorDetail = permittedFloorDetail;
    }

    public List<LettertoParty> getLettertoParty() {
        return lettertoParty;
    }

    public void setLettertoParty(List<LettertoParty> lettertoParty) {
        this.lettertoParty = lettertoParty;
    }

    public List<ApplicationFee> getApplicationFee() {
        return applicationFee;
    }

    public void setApplicationFee(List<ApplicationFee> applicationFee) {
        this.applicationFee = applicationFee;
    }

    public List<ApplicationStakeHolder> getStakeHolder() {
        return stakeHolder;
    }

    public void setStakeHolder(List<ApplicationStakeHolder> stakeHolder) {
        this.stakeHolder = stakeHolder;
    }


    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public SiteDetail getSiteDetail() {
        return siteDetail;
    }

    public void setSiteDetail(SiteDetail siteDetail) {
        this.siteDetail = siteDetail;
    }

    public List<ApplicationDocument> getApplicationDocument() {
        return applicationDocument;
    }

    public void setApplicationDocument(List<ApplicationDocument> applicationDocument) {
        this.applicationDocument = applicationDocument;
    }

    public List<ApplicationNocDocument> getApplicationNOCDocument() {
        return applicationNOCDocument;
    }

    public void setApplicationNOCDocument(List<ApplicationNocDocument> applicationNOCDocument) {
        this.applicationNOCDocument = applicationNOCDocument;
    }

    public Date getBuildingPlanApprovalDate() {
        return buildingPlanApprovalDate;
    }

    public void setBuildingPlanApprovalDate(Date buildingPlanApprovalDate) {
        this.buildingPlanApprovalDate = buildingPlanApprovalDate;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

}