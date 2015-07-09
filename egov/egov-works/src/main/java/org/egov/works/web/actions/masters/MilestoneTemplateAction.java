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
package org.egov.works.web.actions.masters;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.service.CommonsService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.masters.MilestoneTemplate;
import org.egov.works.models.masters.MilestoneTemplateActivity;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author vikas
 *
 */
@ParentPackage("egov")
public class MilestoneTemplateAction extends SearchFormAction{


	private static final long serialVersionUID = 1L;
	private MilestoneTemplate template = new MilestoneTemplate();
	private PersistenceService<MilestoneTemplate,Long> milestoneTemplateService;
	private Long id; 
	private String mode=" ";
	private WorkflowService<MilestoneTemplate> milestoneTemplateWorkflowService;
	private static final String SAVE_ACTION = "save";
	private String messageKey;
	private static final String MILESTONE_TEMPLATE_MODULE_KEY = "MilestoneTemplate";
	@Autowired
        private CommonsService commonsService;
	private String actionName;
	private String sourcepage;
	private String nextEmployeeName;
	private String nextDesignation;
	private String designation;
	private WorksService worksService;
	@Autowired
	private UserService userService;
	private List<MilestoneTemplateActivity> templateActivities = new LinkedList<MilestoneTemplateActivity>();
	private static final String SOURCE_INBOX = "inbox";
	private static final String MODE_MODIFY="modify";
	


	public MilestoneTemplateAction() {
		addRelatedEntity("workType", EgwTypeOfWork.class);
		addRelatedEntity("subType", EgwTypeOfWork.class);
	}

	public void prepare(){
		if(id != null){
			template=milestoneTemplateService.findById(id, false);
		}
		AjaxEstimateAction ajaxEstimateAction =new AjaxEstimateAction();
		ajaxEstimateAction.setPersistenceService(getPersistenceService());
		super.prepare();
		setupDropdownDataExcluding("workType","subType");
		addDropdownData("parentCategoryList",getPersistenceService().findAllBy("from EgwTypeOfWork etw where etw.parentid is null"));
		populateCategoryList(ajaxEstimateAction, template.getWorkType() != null);
		addDropdownData("executingDepartmentList",getPersistenceService().findAllBy("from Department order by upper(dept)")); 
	 }
	
	@Override
	public Object getModel() {
	
		return template;
	}
	
	@SkipValidation
	public String newform(){
		return "new";
	}
	
	public String save(){
		String actionName = parameters.get("actionName")[0];
		
		if(id==null){
			template.setEgwStatus(commonsService.getStatusByModuleAndCode(MILESTONE_TEMPLATE_MODULE_KEY,"NEW"));
		}
		
		if((mode.equalsIgnoreCase("modify")) && template.getEgwStatus().getCode().equalsIgnoreCase("APPROVED")){
			template.setEgwStatus(commonsService.getStatusByModuleAndCode(MILESTONE_TEMPLATE_MODULE_KEY,"NEW"));
			//TODO - check for application for commenting out this line for any issues
			//**template.setState(null);
		}

		template = milestoneTemplateService.persist(template);
		milestoneTemplateWorkflowService.transition(actionName, template, template.getWorkflowapproverComments());
		template = milestoneTemplateService.persist(template);
		messageKey="milestone.template."+actionName;
		addActionMessage(getText(messageKey,"The Milestone Template was saved successfully"));
		getDesignation(template);
		mode="";
		
		if(SAVE_ACTION.equals(actionName)){
			sourcepage="inbox";
		}

		return SAVE_ACTION.equals(actionName)?EDIT:SUCCESS;

	}
	
	public String cancel(){
		if(template.getId()!=null){
			milestoneTemplateWorkflowService.transition(MilestoneTemplate.Actions.CANCEL.toString(), template,template.getWorkflowapproverComments());
			template=milestoneTemplateService.persist(template);
		}
		messageKey="milestone.template.cancel";	
		getDesignation(template);
		return SUCCESS;
	}	

	public String reject(){
		milestoneTemplateWorkflowService.transition(MilestoneTemplate.Actions.REJECT.toString(),template,template.getWorkflowapproverComments());
		template=milestoneTemplateService.persist(template);
		messageKey="milestone.template.reject";	
		getDesignation(template);
		return SUCCESS;
	}	

	public void getDesignation(MilestoneTemplate template){
		if(template.getEgwStatus()!= null 
				&& !(WorksConstants.NEW).equalsIgnoreCase(template.getEgwStatus().getCode())) {
			String result = worksService.getEmpNameDesignation(template.getState().getOwnerPosition(), template.getState().getCreatedDate().toDate());
			if(result != null && !"@".equalsIgnoreCase(result)) {
				String empName = result.substring(0,result.lastIndexOf('@'));
				String designation =result.substring(result.lastIndexOf('@')+1,result.length());
				setNextEmployeeName(empName);
				setNextDesignation(designation);
			}
		}
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getSourcepage() {
		return sourcepage;
	}

	public void setSourcepage(String sourcepage) {
		this.sourcepage = sourcepage;
	}

	public String getNextEmployeeName() {
		return nextEmployeeName;
	}

	public void setNextEmployeeName(String nextEmployeeName) {
		this.nextEmployeeName = nextEmployeeName;
	}

	public String getNextDesignation() {
		return nextDesignation;
	}

	public void setNextDesignation(String nextDesignation) {
		this.nextDesignation = nextDesignation;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	
	@SkipValidation
	public String edit(){
		if(((SOURCE_INBOX.equalsIgnoreCase(sourcepage) || MODE_MODIFY.equalsIgnoreCase(mode)) 
				&& (template.getEgwStatus()!=null && 
					(!template.getEgwStatus().getCode().equals(MilestoneTemplate.MilestoneTemplateStatus.APPROVED) 
							&& !template.getEgwStatus().getCode().equals(MilestoneTemplate.MilestoneTemplateStatus.CANCELLED)))) 
			|| (template.getEgwStatus()!=null &&
						template.getEgwStatus().getCode().equals(WorksConstants.NEW))){
			User user=userService.getUserById(Long.valueOf(EgovThreadLocals.getUserId()));
			boolean isValidUser=worksService.validateWorkflowForUser(template,user);
			if(isValidUser){
					throw new EGOVRuntimeException("Error: Invalid Owner - No permission to view this page.");
			}
		}
		else if(StringUtils.isEmpty(sourcepage)){
			sourcepage="search";
		}

		return "edit";
	}
	
	@SkipValidation
	public String search(){

		return "search";
	}

	@SkipValidation
	public String searchTemplate(){
		if("searchForMilestone".equalsIgnoreCase(sourcepage)){
			template.setStatus(1);
		}
		return "search";
	}

	
	@SkipValidation
	public String searchDetails(){
		 if(template.getWorkType()==null || template.getWorkType().getId()==-1){
			 String messageKey = "milestone.template.search.workType.error";
			 addActionError(getText(messageKey));
			 return "search";
		 }
         setPageSize(WorksConstants.PAGE_SIZE);
		 super.search();
		 return "search";
	 }
	 
	public void validate() {
		populateActivities();
		
		if(null == template.getMilestoneTemplateActivities() || template.getMilestoneTemplateActivities().size() ==0){
			 
			addFieldError("milestone.activity.missing", "Template Activity is not added");
		}
		BigDecimal percentage = BigDecimal.ZERO;
		for (MilestoneTemplateActivity templateActivities : template.getMilestoneTemplateActivities()) {
			if(templateActivities.getPercentage()!=null){
				percentage = percentage.add(templateActivities.getPercentage());
			}
		}
		if(percentage.compareTo(BigDecimal.valueOf(100)) !=0){
			addFieldError("milestone.activity.total.percentage", "Total activity percentage should be equal to 100%");
			 
		}
	}
	
	 protected void populateCategoryList(
				AjaxEstimateAction ajaxEstimateAction, boolean categoryPopulated) {
			if (categoryPopulated) {
				ajaxEstimateAction.setCategory(template.getWorkType().getId());
				ajaxEstimateAction.subcategories();
				addDropdownData("categoryList", ajaxEstimateAction.getSubCategories());		
			}
			else {
				addDropdownData("categoryList", Collections.emptyList());
			}
		}


	 public void populateActivities(){
		template.getMilestoneTemplateActivities().clear();
		for (MilestoneTemplateActivity activity :templateActivities)
		{
			if(activity!=null){
			template.addMilestoneTemplateActivity(activity);
			}
		}
	 }

	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder){
		String dynQuery =" from MilestoneTemplate mt where mt.id is not null and mt.egwStatus.code!='NEW' ";
		List<Object> paramList = new ArrayList<Object>();
		if(template.getWorkType()!=null && template.getWorkType().getId()!=-1){
			dynQuery = dynQuery + " and mt.workType.id = ? ";
			paramList.add(template.getWorkType().getId());
		}
		if(template.getSubType() != null && template.getSubType().getId() !=-1){
			dynQuery = dynQuery + " and mt.subType.id = ? ";
			paramList.add(template.getSubType().getId());
		} 
		if(StringUtils.isNotBlank(template.getCode().trim())){
			dynQuery = dynQuery + " and UPPER(mt.code) like '%'||?||'%'";
			paramList.add(template.getCode().trim().toUpperCase());
		}
		if(StringUtils.isNotBlank(template.getName().trim())){
			dynQuery = dynQuery + " and UPPER(mt.name) like '%'||?||'%'";
			paramList.add(template.getName().trim().toUpperCase());
		}
		if(StringUtils.isNotBlank(template.getDescription().trim())){
			dynQuery = dynQuery + " and UPPER(mt.description) like '%'||?||'%'";
			paramList.add(template.getDescription().trim().toUpperCase());
		}
		if(template.getStatus()!=null && template.getStatus() !=-1){
			dynQuery = dynQuery + " and mt.status = ? ";
			paramList.add(template.getStatus());
		}
		String countQuery = "select distinct count(mt) " + dynQuery;
		return new SearchQueryHQL(dynQuery, countQuery, paramList);
	}
		
	public MilestoneTemplate getTemplate() {
		return template;
	}
	public void setTemplate(MilestoneTemplate template) {
		this.template = template;
	}
	public PersistenceService<MilestoneTemplate, Long> getMilestoneTemplateService() {
		return milestoneTemplateService;
	}
	public void setMilestoneTemplateService(
			PersistenceService<MilestoneTemplate, Long> milestoneTemplateService) {
		this.milestoneTemplateService = milestoneTemplateService;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getMode() {
		return mode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public List<org.egov.infstr.workflow.Action> getValidActions(){
		return milestoneTemplateWorkflowService.getValidActions(template); 		
	}
	
	public void setMilestoneTemplateWorkflowService(WorkflowService<MilestoneTemplate> milestoneTemplateWorkflowService) {
		this.milestoneTemplateWorkflowService = milestoneTemplateWorkflowService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public List<MilestoneTemplateActivity> getTemplateActivities() {
		return templateActivities;
	}

	public void setTemplateActivities(
			List<MilestoneTemplateActivity> templateActivities) {
		this.templateActivities = templateActivities;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
