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

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "EGW_TRACK_MILESTONE_ACTIVITY")
@Unique(id = "id", tableName = "EGW_TRACK_MILESTONE_ACTIVITY")
@SequenceGenerator(name = TrackMilestoneActivity.EGW_TRACK_MILESTONE_ACTIVITY, sequenceName = TrackMilestoneActivity.EGW_TRACK_MILESTONE_ACTIVITY, allocationSize = 1)
public class TrackMilestoneActivity extends AbstractAuditable{

    private static final long serialVersionUID = -4386325007110227524L;

    public static final String EGW_TRACK_MILESTONE_ACTIVITY = "SEQ_EGW_TRACK_MILESTONE_ACTIVITY";

    @Id
    @GeneratedValue(generator = EGW_TRACK_MILESTONE_ACTIVITY, strategy = GenerationType.SEQUENCE)
    private Long id;

    private String status;

    private double completedPercentage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trackMilestone", nullable = false)
    private TrackMilestone trackMilestone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milestoneactivity", nullable = false)
    private MilestoneActivity milestoneActivity;

    private String remarks;

    private Date completionDate;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public TrackMilestone getTrackMilestone() {
        return trackMilestone;
    }

    public void setTrackMilestone(final TrackMilestone trackMilestone) {
        this.trackMilestone = trackMilestone;
    }

    public MilestoneActivity getMilestoneActivity() {
        return milestoneActivity;
    }

    public void setMilestoneActivity(final MilestoneActivity milestoneActivity) {
        this.milestoneActivity = milestoneActivity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(final Date completionDate) {
        this.completionDate = completionDate;
    }

    public double getCompletedPercentage() {
        return completedPercentage;
    }

    public void setCompletedPercentage(final double completedPercentage) {
        this.completedPercentage = completedPercentage;
    }

}
