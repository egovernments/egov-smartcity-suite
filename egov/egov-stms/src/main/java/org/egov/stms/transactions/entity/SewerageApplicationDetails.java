/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2017  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.stms.transactions.entity;

import org.egov.commons.EgwStatus;
import org.egov.demand.model.EgDemand;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.pims.commons.Position;
import org.egov.stms.masters.entity.SewerageApplicationType;
import org.egov.stms.notice.entity.SewerageNotice;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "egswtax_applicationdetails")
@SequenceGenerator(name = SewerageApplicationDetails.SEQ_APPLICATIONDETAILS, sequenceName = SewerageApplicationDetails.SEQ_APPLICATIONDETAILS, allocationSize = 1)
public class SewerageApplicationDetails extends StateAware<Position> {

    public static final String SEQ_APPLICATIONDETAILS = "SEQ_EGSWTAX_APPLICATIONDETAILS";
    private static final long serialVersionUID = 734228413287149091L;
    @Id
    @GeneratedValue(generator = SEQ_APPLICATIONDETAILS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "applicationtype", nullable = false)
    private SewerageApplicationType applicationType;

    @ManyToOne(cascade = CascadeType.ALL)
    @Valid
    @NotNull
    @JoinColumn(name = "connection", nullable = false)
    private SewerageConnection connection;

    @SafeHtml
    private String applicationNumber;

    @Temporal(value = TemporalType.DATE)
    private Date applicationDate;

    @Temporal(value = TemporalType.DATE)
    private Date disposalDate;

    @ManyToOne
    @JoinColumn(name = "status", nullable = false)
    private EgwStatus status;

    @SafeHtml
    @Length(min = 3, max = 50)
    private String estimationNumber;

    @Temporal(value = TemporalType.DATE)
    private Date estimationDate;

    @SafeHtml
    private String workOrderNumber;

    @Temporal(value = TemporalType.DATE)
    private Date workOrderDate;

    @SafeHtml
    @Length(min = 3, max = 50)
    private String rejectionNumber;

    @Temporal(value = TemporalType.DATE)
    private Date rejectionDate;

    @SafeHtml
    private String closureNoticeNumber;

    @Temporal(value = TemporalType.DATE)
    private Date closureNoticeDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private SewerageApplicationDetails parent;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @NotNull
    @JoinColumn(name = "connectiondetail", nullable = false)
    private SewerageConnectionDetail connectionDetail;

    // TODO : Remove this after supporting multiple document attachments
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "filestoreid")
    private FileStoreMapper fileStore;

    private String source;

    @OneToMany(mappedBy = "applicationDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SewerageApplicationDetailsDocument> appDetailsDocument = new ArrayList<>(
            0);

    private boolean isActive;

    @OrderBy("id desc")
    @OneToMany(mappedBy = "applicationDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SewerageConnectionEstimationDetails> estimationDetails = new ArrayList<>(
            0);

    @OrderBy("id desc")
    @OneToMany(mappedBy = "applicationDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SewerageFieldInspection> fieldInspections = new ArrayList<>();

    @OrderBy("id desc")
    @OneToMany(mappedBy = "applicationDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SewerageConnectionFee> connectionFees = new ArrayList<>();

    @OrderBy("id desc")
    @OneToMany(mappedBy = "applicationDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SewerageDemandConnection> demandConnections = new ArrayList<>();

    @Transient
    private List<SewerageConnectionEstimationDetails> estimationDetailsForUpdate = new ArrayList<>();

    @OneToMany(mappedBy = "applicationDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SewerageNotice> sewerageNotice = new HashSet<>();

    @Transient
    private Long approvalDepartment;

    @Transient
    private String approvalComent;

    @Transient
    private String ownerName;

    @Transient
    private List<SewerageDemandDetail> demandDetailBeanList = new ArrayList<>();

    private String closeConnectionReason;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public SewerageApplicationType getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(final SewerageApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    public SewerageConnection getConnection() {
        return connection;
    }

    public void setConnection(final SewerageConnection connection) {
        this.connection = connection;
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

    public Date getDisposalDate() {
        return disposalDate;
    }

    public void setDisposalDate(final Date disposalDate) {
        this.disposalDate = disposalDate;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(final EgwStatus status) {
        this.status = status;
    }

    public String getEstimationNumber() {
        return estimationNumber;
    }

    public void setEstimationNumber(final String estimationNumber) {
        this.estimationNumber = estimationNumber;
    }

    public Date getEstimationDate() {
        return estimationDate;
    }

    public void setEstimationDate(final Date estimationDate) {
        this.estimationDate = estimationDate;
    }

    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(final String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    public Date getWorkOrderDate() {
        return workOrderDate;
    }

    public void setWorkOrderDate(final Date workOrderDate) {
        this.workOrderDate = workOrderDate;
    }

    public FileStoreMapper getFileStore() {
        return fileStore;
    }

    public void setFileStore(final FileStoreMapper fileStore) {
        this.fileStore = fileStore;
    }

    public List<SewerageConnectionEstimationDetails> getEstimationDetails() {
        return estimationDetails;
    }

    public void setEstimationDetails(final List<SewerageConnectionEstimationDetails> estimationDetails) {
        this.estimationDetails = estimationDetails;
    }

    public void deleteEstimationDetails(SewerageConnectionEstimationDetails estimationDetails) {
        if (this.estimationDetails != null && !this.estimationDetails.isEmpty())
            this.estimationDetails.remove(estimationDetails);
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

    @Override
    public String myLinkId() {
        return applicationNumber != null ? applicationNumber : connection.getShscNumber();

    }

    @Override
    public String getStateDetails() {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return String.format("Sewerage Application Number %s with application date %s.",
                applicationNumber != null ? applicationNumber : connection.getShscNumber(),
                applicationDate != null ? formatter.format(applicationDate) : formatter.format(new Date()));
    }

    public List<SewerageConnectionFee> getConnectionFees() {
        return connectionFees;
    }

    public void setConnectionFees(final List<SewerageConnectionFee> connectionFees) {
        this.connectionFees = connectionFees;
    }

    public void addConnectionFees(SewerageConnectionFee connectionFees) {
        this.connectionFees.add(connectionFees);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(final boolean isActive) {
        this.isActive = isActive;
    }

    public List<SewerageFieldInspection> getFieldInspections() {
        return fieldInspections;
    }

    public void setFieldInspections(final List<SewerageFieldInspection> fieldInspections) {
        this.fieldInspections = fieldInspections;
    }

    public List<SewerageApplicationDetailsDocument> getAppDetailsDocument() {
        return appDetailsDocument;
    }

    public void setAppDetailsDocument(List<SewerageApplicationDetailsDocument> appDetailsDocument) {
        this.appDetailsDocument = appDetailsDocument;
    }

    public SewerageApplicationDetails getParent() {
        return parent;
    }

    public void setParent(SewerageApplicationDetails parent) {
        this.parent = parent;
    }

    public SewerageConnectionDetail getConnectionDetail() {
        return connectionDetail;
    }

    public void setConnectionDetail(SewerageConnectionDetail connectionDetail) {
        this.connectionDetail = connectionDetail;
    }

    public List<SewerageDemandConnection> getDemandConnections() {
        return demandConnections;
    }

    public void setDemandConnections(List<SewerageDemandConnection> demandConnections) {
        this.demandConnections = demandConnections;
    }

    public void addDemandConnections(final SewerageDemandConnection demandConnections) {
        getDemandConnections().add(demandConnections);
    }

    public List<SewerageConnectionEstimationDetails> getEstimationDetailsForUpdate() {
        return estimationDetailsForUpdate;
    }

    public void setEstimationDetailsForUpdate(List<SewerageConnectionEstimationDetails> estimationDetailsForUpdate) {
        this.estimationDetailsForUpdate = estimationDetailsForUpdate;
    }

    public EgDemand getCurrentDemand() {
        for (final SewerageDemandConnection sdc : this.getDemandConnections())
            if (sdc.getDemand().getIsHistory().equalsIgnoreCase(SewerageTaxConstants.DEMANDISHISTORY)) {
                return sdc.getDemand();
            }
        return null;
    }

    public Set<SewerageNotice> getSewerageNotice() {
        return sewerageNotice;
    }

    public void setSewerageNotice(Set<SewerageNotice> sewerageNotice) {
        this.sewerageNotice = sewerageNotice;
    }

    public void addNotice(final SewerageNotice swNotice) {
        getSewerageNotice().add(swNotice);
    }

    public void removeNotice(final SewerageNotice swNotice) {
        getSewerageNotice().remove(swNotice);
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public List<SewerageDemandDetail> getDemandDetailBeanList() {
        return demandDetailBeanList;
    }

    public void setDemandDetailBeanList(List<SewerageDemandDetail> demandDetailBeanList) {
        this.demandDetailBeanList = demandDetailBeanList;
    }

    public String getCloseConnectionReason() {
        return closeConnectionReason;
    }

    public void setCloseConnectionReason(String closeConnectionReason) {
        this.closeConnectionReason = closeConnectionReason;
    }

    public String getClosureNoticeNumber() {
        return closureNoticeNumber;
    }

    public void setClosureNoticeNumber(String closureNoticeNumber) {
        this.closureNoticeNumber = closureNoticeNumber;
    }

    public Date getClosureNoticeDate() {
        return closureNoticeDate;
    }

    public void setClosureNoticeDate(Date closureNoticeDate) {
        this.closureNoticeDate = closureNoticeDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRejectionNumber() {
        return rejectionNumber;
    }

    public void setRejectionNumber(String rejectionNumber) {
        this.rejectionNumber = rejectionNumber;
    }

    public Date getRejectionDate() {
        return rejectionDate;
    }

    public void setRejectionDate(Date rejectionDate) {
        this.rejectionDate = rejectionDate;
    }

    public enum WorkFlowState {
        CREATED, CHECKED, APPROVED, REJECTED, CANCELLED;
    }


}