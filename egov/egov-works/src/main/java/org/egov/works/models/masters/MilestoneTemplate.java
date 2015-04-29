package org.egov.works.models.masters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.egov.commons.EgwStatus;
import org.egov.commons.EgwTypeOfWork;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infstr.ValidationError;
import org.egov.works.models.workflow.WorkFlow;

/**
 * @author vikas
 */


@Unique(fields={"code"},id="id",tableName="EGW_MILESTONE_TEMPLATE",columnName={"CODE"},message="milestonetemplate.code.isunique")
public class MilestoneTemplate extends WorkFlow  {
	
	public enum MilestoneTemplateStatus{
		CREATED,APPROVED,REJECTED,CANCELLED,RESUBMITTED
	}

	public enum Actions{
		SUBMIT_FOR_APPROVAL,APPROVE,REJECT,CANCEL;

		public String toString() {
			return this.name().toLowerCase();
		}
	}
	
	@Required(message="milestonetemplate.code.not.null")
	private String code;
	@Required(message="milestonetemplate.name.not.null")
	private String name;
	@Required(message="milestonetemplate.description.not.null")
	private String description;
	private Integer status;
	@Required(message="milestonetemplate.workType.not.null")
	private EgwTypeOfWork workType;
	private EgwTypeOfWork subType;
	
	private EgwStatus egwStatus;
	
	@Valid
	private List<MilestoneTemplateActivity> activities = new LinkedList<MilestoneTemplateActivity>();
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public EgwTypeOfWork getWorkType() {
		return workType;
	}
	public void setWorkType(EgwTypeOfWork workType) {
		this.workType = workType;
	}
	public EgwTypeOfWork getSubType() {
		return subType;
	}
	public void setSubType(EgwTypeOfWork subType) {
		this.subType = subType;
	}

	@Override
	public String getStateDetails() {
		return "Milestone Template Code : "+this.code;
	}

	public List<MilestoneTemplateActivity> getActivities() {
		return activities;
	}

	public void setActivities(
			List<MilestoneTemplateActivity> activities) {
		this.activities = activities;
	}

	public void addActivity(MilestoneTemplateActivity activity) {
		this.activities.add(activity);
	}

	
	public EgwStatus getEgwStatus() {
		return egwStatus;
	}

	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}

	public Collection<MilestoneTemplateActivity> getStages() {
		return CollectionUtils.select(activities, new Predicate(){
			public boolean evaluate(Object activity) {
				return true;
			}});
	}
	
	public List<ValidationError> validateActivities() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		for(MilestoneTemplateActivity activity: activities) {
			validationErrors.addAll(activity.validate());
		}
		return validationErrors;
	}

	public List<ValidationError> validate() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		validationErrors.addAll(validateActivities());
		return validationErrors;
	}

}
