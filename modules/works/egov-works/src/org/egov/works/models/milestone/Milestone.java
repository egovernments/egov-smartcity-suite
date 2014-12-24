package org.egov.works.models.milestone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.egov.commons.EgwStatus;
import org.egov.infstr.ValidationError;
import org.egov.works.models.workflow.WorkFlow;
import org.egov.works.models.workorder.WorkOrderEstimate;
import javax.validation.Valid;

public class Milestone extends WorkFlow  {
	
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
	
}

