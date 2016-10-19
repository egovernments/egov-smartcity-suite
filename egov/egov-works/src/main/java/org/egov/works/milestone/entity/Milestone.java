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
package org.egov.works.milestone.entity;

import java.util.ArrayList;
import java.util.Comparator;
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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.works.workorder.entity.WorkOrderEstimate;

@Entity
@Table(name = "EGW_MILESTONE")
@Unique(id = "id", tableName = "EGW_MILESTONE")
@SequenceGenerator(name = Milestone.SEQ_EGW_MILESTONE, sequenceName = Milestone.SEQ_EGW_MILESTONE, allocationSize = 1)
public class Milestone extends StateAware implements Comparable {

    private static final long serialVersionUID = -366602348464540736L;

    public static final String SEQ_EGW_MILESTONE = "SEQ_EGW_MILESTONE";

    public enum MilestoneStatus {
        CREATED, APPROVED, REJECTED, CANCELLED, RESUBMITTED
    }

    @Id
    @GeneratedValue(generator = SEQ_EGW_MILESTONE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date approvedDate;

    private Long documentNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", nullable = false)
    private EgwStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workOrderEstimate", nullable = false)
    private WorkOrderEstimate workOrderEstimate;

    @OrderBy("id")
    @OneToMany(mappedBy = "milestone", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = MilestoneActivity.class)
    private final List<MilestoneActivity> activities = new ArrayList<MilestoneActivity>(0);

    @OrderBy("id")
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "milestone", targetEntity = TrackMilestone.class)
    private List<TrackMilestone> trackMilestone = new ArrayList<TrackMilestone>(0);

    private transient String ownerName;

    @Transient
    private String approvalComent;

    private String cancellationReason;

    private String cancellationRemarks;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(final Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(final EgwStatus status) {
        this.status = status;
    }

    public WorkOrderEstimate getWorkOrderEstimate() {
        return workOrderEstimate;
    }

    public void setWorkOrderEstimate(final WorkOrderEstimate workOrderEstimate) {
        this.workOrderEstimate = workOrderEstimate;
    }

    public static Comparator milestoneComparator = (milestone1, milestone2) -> {
        final Long msObj1 = ((Milestone) milestone1).getId();
        final Long msObj2 = ((Milestone) milestone2).getId();
        return msObj1.compareTo(msObj2);
    };

    @Override
    public int compareTo(final Object o) {

        return 0;
    }

    public List<TrackMilestone> getTrackMilestone() {
        return trackMilestone;
    }

    public void setTrackMilestone(final List<TrackMilestone> trackMilestone) {
        this.trackMilestone = trackMilestone;
    }

    @Override
    public String getStateDetails() {
        return "Estimate Number : " + getWorkOrderEstimate().getEstimate().getEstimateNumber();
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public List<MilestoneActivity> getActivities() {
        return activities;
    }

    public void addActivity(final MilestoneActivity activity) {
        activities.add(activity);
    }

    public String getApprovalComent() {
        return approvalComent;
    }

    public void setApprovalComent(final String approvalComent) {
        this.approvalComent = approvalComent;
    }

    public Long getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(final Long documentNumber) {
        this.documentNumber = documentNumber;
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

}
