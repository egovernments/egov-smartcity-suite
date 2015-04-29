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
