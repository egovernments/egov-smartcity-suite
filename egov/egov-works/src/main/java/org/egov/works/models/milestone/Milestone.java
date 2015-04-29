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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.egov.commons.EgwStatus;
import org.egov.infstr.ValidationError;
import org.egov.works.models.workflow.WorkFlow;
import org.egov.works.models.workorder.WorkOrderEstimate;

public class Milestone extends WorkFlow implements Comparable {
	
	public enum MilestoneStatus{
		CREATED,APPROVED,REJECTED,CANCELLED,RESUBMITTED
	}

	public enum Actions{
		SUBMIT_FOR_APPROVAL,APPROVE,REJECT,CANCEL;

		public String toString() {
			return this.name().toLowerCase();
		}
	}
	
	private WorkOrderEstimate workOrderEstimate;
	
	private EgwStatus egwStatus;
	
	private Long documentNumber;
	private String ownerName;

	
	@Valid
	private List<MilestoneActivity> activities = new LinkedList<MilestoneActivity>();
	
	private Set<TrackMilestone> trackMilestone = new HashSet<TrackMilestone>();

	public List<MilestoneActivity> getActivities() {
		return activities;
	}

	public void setActivities(
			List<MilestoneActivity> activities) {
		this.activities = activities;
	}

	public void addActivity(MilestoneActivity activity) {
		this.activities.add(activity);
	}

	
	public WorkOrderEstimate getWorkOrderEstimate() {
		return workOrderEstimate;
	}

	public void setWorkOrderEstimate(WorkOrderEstimate workOrderEstimate) {
		this.workOrderEstimate = workOrderEstimate;
	}

	public EgwStatus getEgwStatus() {
		return egwStatus;
	}

	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}
	
	public Long getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(Long documentNumber) {
		this.documentNumber = documentNumber;
	}
	
	public String getContractor(){
		return workOrderEstimate.getWorkOrder().getContractor().getCode()+"-"+workOrderEstimate.getWorkOrder().getContractor().getName();
	}
	
	@Override
	public String getStateDetails() {
		return "Estimate Number : "+this.getWorkOrderEstimate().getEstimate().getEstimateNumber();
	}
	
	public Collection<MilestoneActivity> getStages() {
		return CollectionUtils.select(activities, new Predicate(){
			public boolean evaluate(Object activity) {
				return true;
			}});
	}
	
	public List<ValidationError> validateActivities() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		for(MilestoneActivity activity: activities) {
			validationErrors.addAll(activity.validate());
		}
		return validationErrors;
	}

	public List<ValidationError> validate() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		validationErrors.addAll(validateActivities());
		return validationErrors;
	}

	public Set<TrackMilestone> getTrackMilestone() {
		return trackMilestone;
	}
	public void setTrackMilestone(Set<TrackMilestone> trackMilestone) {
		this.trackMilestone = trackMilestone;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public static Comparator milestoneComparator = new Comparator() {
		public int compare(Object milestone1, Object milestone2) {
			Long msObj1 = ((Milestone)milestone1).getId();
			Long msObj2 = ((Milestone)milestone2).getId();
			return msObj1.compareTo(msObj2);
		}
	};
	
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}
}

