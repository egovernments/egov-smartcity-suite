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
package org.egov.works.models.milestone;

import org.apache.commons.collections.CollectionUtils;
import org.egov.commons.EgwStatus;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.works.models.workflow.WorkFlow;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TrackMilestone extends WorkFlow {

    private static final long serialVersionUID = 765700374368314216L;

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

    private Milestone milestone;
    private BigDecimal total;
    private EgwStatus egwStatus;
    private Boolean isProjectCompleted;
    private String ownerName;
    private Date approvedDate;

    @Valid
    private List<TrackMilestoneActivity> activities = new LinkedList<TrackMilestoneActivity>();

    public List<TrackMilestoneActivity> getActivities() {
        return activities;
    }

    public void setActivities(final List<TrackMilestoneActivity> activities) {
        this.activities = activities;
    }

    public void addActivity(final TrackMilestoneActivity activity) {
        activities.add(activity);
    }

    public Milestone getMilestone() {
        return milestone;
    }

    public void setMilestone(final Milestone milestone) {
        this.milestone = milestone;
    }

    public EgwStatus getEgwStatus() {
        return egwStatus;
    }

    public void setEgwStatus(final EgwStatus egwStatus) {
        this.egwStatus = egwStatus;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(final BigDecimal total) {
        this.total = total;
    }

    @Override
    public String getStateDetails() {
        return "Estimate Number : " + milestone.getWorkOrderEstimate().getEstimate().getEstimateNumber();
    }

    public Collection<TrackMilestoneActivity> getStages() {
        return CollectionUtils.select(activities, activity -> true);
    }

    public List<ValidationError> validateActivities() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        for (final TrackMilestoneActivity activity : activities)
            validationErrors.addAll(activity.validate());
        return validationErrors;
    }

    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        validationErrors.addAll(validateActivities());
        return validationErrors;
    }

    public Boolean getIsProjectCompleted() {
        return isProjectCompleted;
    }

    public void setIsProjectCompleted(final Boolean isProjectCompleted) {
        this.isProjectCompleted = isProjectCompleted;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(final Date approvedDate) {
        this.approvedDate = approvedDate;
    }
}
