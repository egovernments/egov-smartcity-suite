/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
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
package org.egov.works.models.milestone;

import org.apache.commons.collections.CollectionUtils;
import org.egov.commons.EgwStatus;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.works.models.workflow.WorkFlow;
import org.egov.works.models.workorder.WorkOrderEstimate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Milestone extends WorkFlow implements Comparable {

    private static final long serialVersionUID = -6044897968763004490L;

    public enum MilestoneStatus {
        CREATED, APPROVED, REJECTED, CANCELLED, RESUBMITTED
    }

    public enum Actions {
        SUBMIT_FOR_APPROVAL, APPROVE, REJECT, CANCEL;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    private WorkOrderEstimate workOrderEstimate;

    private EgwStatus egwStatus;

    private Long documentNumber;
    private String ownerName;
    private Date approvedDate;

    @Valid
    private List<MilestoneActivity> activities = new LinkedList<MilestoneActivity>();

    private Set<TrackMilestone> trackMilestone = new HashSet<TrackMilestone>();

    public List<MilestoneActivity> getActivities() {
        return activities;
    }

    public void setActivities(final List<MilestoneActivity> activities) {
        this.activities = activities;
    }

    public void addActivity(final MilestoneActivity activity) {
        activities.add(activity);
    }

    public WorkOrderEstimate getWorkOrderEstimate() {
        return workOrderEstimate;
    }

    public void setWorkOrderEstimate(final WorkOrderEstimate workOrderEstimate) {
        this.workOrderEstimate = workOrderEstimate;
    }

    public EgwStatus getEgwStatus() {
        return egwStatus;
    }

    public void setEgwStatus(final EgwStatus egwStatus) {
        this.egwStatus = egwStatus;
    }

    public Long getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(final Long documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getContractor() {
        return workOrderEstimate.getWorkOrder().getContractor().getCode() + "-"
                + workOrderEstimate.getWorkOrder().getContractor().getName();
    }

    @Override
    public String getStateDetails() {
        return "Estimate Number : " + getWorkOrderEstimate().getEstimate().getEstimateNumber();
    }

    public Collection<MilestoneActivity> getStages() {
        return CollectionUtils.select(activities, activity -> true);
    }

    public List<ValidationError> validateActivities() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        for (final MilestoneActivity activity : activities)
            validationErrors.addAll(activity.validate());
        return validationErrors;
    }

    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        validationErrors.addAll(validateActivities());
        return validationErrors;
    }

    public Set<TrackMilestone> getTrackMilestone() {
        return trackMilestone;
    }

    public void setTrackMilestone(final Set<TrackMilestone> trackMilestone) {
        this.trackMilestone = trackMilestone;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public static Comparator milestoneComparator = (milestone1, milestone2) -> {
        final Long msObj1 = ((Milestone) milestone1).getId();
        final Long msObj2 = ((Milestone) milestone2).getId();
        return msObj1.compareTo(msObj2);
    };

    @Override
    public int compareTo(final Object o) {
        // TODO Auto-generated method stub
        return 0;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(final Date approvedDate) {
        this.approvedDate = approvedDate;
    }
}
