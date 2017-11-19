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
package org.egov.works.milestone.entity;

import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.pims.commons.Position;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "EGW_TRACK_MILESTONE")
@Unique(id = "id", tableName = "EGW_TRACK_MILESTONE")
@SequenceGenerator(name = TrackMilestone.SEQ_EGW_TRACK_MILESTONE, sequenceName = TrackMilestone.SEQ_EGW_TRACK_MILESTONE, allocationSize = 1)
public class TrackMilestone extends StateAware<Position> {

    public static final String SEQ_EGW_TRACK_MILESTONE = "SEQ_EGW_TRACK_MILESTONE";
    private static final long serialVersionUID = -366602348464540736L;
    @Id
    @GeneratedValue(generator = SEQ_EGW_TRACK_MILESTONE, strategy = GenerationType.SEQUENCE)
    private Long id;

    private BigDecimal totalPercentage;

    private boolean projectCompleted;

    @Temporal(TemporalType.DATE)
    private Date approvedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", nullable = false)
    private EgwStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milestone", nullable = false)
    private Milestone milestone;

    @OrderBy("id")
    @OneToMany(mappedBy = "trackMilestone", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = TrackMilestoneActivity.class)
    private List<TrackMilestoneActivity> activities = new LinkedList<>();

    private transient String ownerName;

    @Transient
    private String approvalComent;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public BigDecimal getTotalPercentage() {
        return totalPercentage;
    }

    public void setTotalPercentage(BigDecimal totalPercentage) {
        this.totalPercentage = totalPercentage;
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

    public Milestone getMilestone() {
        return milestone;
    }

    public void setMilestone(final Milestone milestone) {
        this.milestone = milestone;
    }

    @Override
    public String getStateDetails() {
        return "Estimate Number : " + milestone.getWorkOrderEstimate().getEstimate().getEstimateNumber();
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public List<TrackMilestoneActivity> getActivities() {
        return activities;
    }

    public void setActivities(final List<TrackMilestoneActivity> activities) {
        this.activities = activities;
    }

    public void addActivity(final TrackMilestoneActivity activity) {
        activities.add(activity);
    }

    public String getApprovalComent() {
        return approvalComent;
    }

    public void setApprovalComent(final String approvalComent) {
        this.approvalComent = approvalComent;
    }

    public boolean isProjectCompleted() {
        return projectCompleted;
    }

    public void setProjectCompleted(final boolean projectCompleted) {
        this.projectCompleted = projectCompleted;
    }

    public enum TrackMilestoneStatus {
        CREATED, APPROVED, REJECTED, CANCELLED, RESUBMITTED
    }

    public enum Actions {
        SUBMIT_FOR_APPROVAL, APPROVE, REJECT, CANCEL;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

}
