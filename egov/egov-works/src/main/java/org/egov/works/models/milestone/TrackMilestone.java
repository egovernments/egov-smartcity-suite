package org.egov.works.models.milestone;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.egov.commons.EgwStatus;
import org.egov.infstr.ValidationError;
import org.egov.works.models.workflow.WorkFlow;

public class TrackMilestone extends WorkFlow  {
	
	public enum TrackMilestoneStatus{
		CREATED,APPROVED,REJECTED,CANCELLED,RESUBMITTED
	}

	public enum Actions{
		SUBMIT_FOR_APPROVAL,APPROVE,REJECT,CANCEL;

		public String toString() {
			return this.name().toLowerCase();
		}
	}
	
	private Milestone milestone;
	private BigDecimal total; 
	private EgwStatus egwStatus;
	private Boolean isProjectCompleted;
	private String ownerName;
	
	
	@Valid
	private List<TrackMilestoneActivity> activities = new LinkedList<TrackMilestoneActivity>();
	


	public List<TrackMilestoneActivity> getActivities() {
		return activities;
	}

	public void setActivities(
			List<TrackMilestoneActivity> activities) {
		this.activities = activities;
	}

	public void addActivity(TrackMilestoneActivity activity) {
		this.activities.add(activity);
	}

	
	public Milestone getMilestone() {
		return milestone;
	}

	public void setMilestone(Milestone milestone) {
		this.milestone = milestone;
	}

	public EgwStatus getEgwStatus() {
		return egwStatus;
	}

	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}
	
	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	@Override
	public String getStateDetails() {
		return "Estimate Number : "+this.milestone.getWorkOrderEstimate().getEstimate().getEstimateNumber();
	}
	
	public Collection<TrackMilestoneActivity> getStages() {
		return CollectionUtils.select(activities, new Predicate(){
			public boolean evaluate(Object activity) {
				return true;
			}});
	}
	
	public List<ValidationError> validateActivities() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		for(TrackMilestoneActivity activity: activities) {
			validationErrors.addAll(activity.validate());
		}
		return validationErrors;
	}

	public List<ValidationError> validate() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		validationErrors.addAll(validateActivities());
		return validationErrors;
	}

	public Boolean getIsProjectCompleted() {
		return isProjectCompleted;
	}

	public void setIsProjectCompleted(Boolean isProjectCompleted) {
		this.isProjectCompleted = isProjectCompleted;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
}

