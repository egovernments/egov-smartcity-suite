package org.egov.works.models.masters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringEscapeUtils;
import org.egov.commons.EgwStatus;
import org.egov.commons.EgwTypeOfWork;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.validator.OptionalPattern;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.models.validator.Unique;
import org.egov.infstr.models.validator.constants.ValidatorConstants;
import org.egov.works.models.workflow.WorkFlow;
import org.hibernate.validator.constraints.Length;
import javax.validation.Valid;

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
	@Length(max=25,message="milestonetemplate.code.length")
	@OptionalPattern(regex=ValidatorConstants.alphaNumericwithSpace,message="milestonetemplate.code.alphaNumeric")
	private String code;
	@Required(message="milestonetemplate.name.not.null")
	@Length(max=256,message="milestonetemplate.name.length")
	private String name;
	@Required(message="milestonetemplate.description.not.null")
	@Length(max=1024,message="milestonetemplate.description.length")
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
		this.code = StringEscapeUtils.unescapeHtml(code);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = StringEscapeUtils.unescapeHtml(name);
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = StringEscapeUtils.unescapeHtml(description);
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
