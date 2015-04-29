package org.egov.works.models.estimate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringEscapeUtils;
import org.egov.commons.EgwTypeOfWork;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;

@Unique(fields={"code"},id="id",tableName="EGW_ESTIMATE_TEMPLATE",columnName={"CODE"},message="estimateTemplate.code.isunique")
public class EstimateTemplate extends BaseModel {
	
	
	@Required(message="estimatetemplate.code.not.null")
	private String code;
	@Required(message="estimatetemplate.name.not.null")
	private String name;
	@Required(message="estimatetemplate.description.not.null")
	private String description;
	private int status;
	@Valid
	@Required(message="estimatetemplate.workType.not.null")
	private EgwTypeOfWork workType;
	private EgwTypeOfWork subType;
	
	@Valid
	private List<EstimateTemplateActivity> activities = new LinkedList<EstimateTemplateActivity>();
	
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
		this.name =StringEscapeUtils.unescapeHtml(name);
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = StringEscapeUtils.unescapeHtml(description);
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
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
	public List<EstimateTemplateActivity> getActivities() {
		return activities;
	}
	public void setActivities(List<EstimateTemplateActivity> activities) {
		this.activities = activities;
	}
	
	public void addActivity(EstimateTemplateActivity activity) {
		this.activities.add(activity);
	}
	
	public Collection<EstimateTemplateActivity> getSORActivities() {
		return CollectionUtils.select(activities, new Predicate(){
			public boolean evaluate(Object activity) {
				return ((EstimateTemplateActivity)activity).getSchedule()!=null;
			}});
	}
	
	public Collection<EstimateTemplateActivity> getNonSORActivities() {
		return CollectionUtils.select(activities, new Predicate(){
			public boolean evaluate(Object activity) {
				return ((EstimateTemplateActivity)activity).getNonSor()!=null;
			}});
	}
	
	public List<ValidationError> validateActivities() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		for(EstimateTemplateActivity activity: activities) {
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
