package org.egov.works.web.actions.estimate;


import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.EgwTypeOfWork;
import org.egov.infstr.commonMasters.EgUom;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.web.actions.SearchFormAction;
import org.egov.works.models.estimate.EstimateTemplate;
import org.egov.works.models.estimate.EstimateTemplateActivity;
import org.egov.works.models.masters.ScheduleOfRate;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;

public class EstimateTemplateAction extends SearchFormAction{
	
	private static final String VIEW = "view";
	private EstimateTemplate estimateTemplate=new EstimateTemplate();
	private List<EstimateTemplateActivity> sorActivities = new LinkedList<EstimateTemplateActivity>();
	private List<EstimateTemplateActivity> nonSorActivities = new LinkedList<EstimateTemplateActivity>();
	@Autowired
        private EmployeeService employeeService;
	private PersonalInformationService personalInformationService;
	private PersistenceService<EstimateTemplate,Long> estimateTemplateService;
	private String mode=null;
	private Long id; 
	private String sourcePage=null;
	private Long typeOfWork;
	private String estimateTemplateCode;
	private Long subTypeOfWork;
	
	private String checkDWRelatedSORs;
	public String getCheckDWRelatedSORs() {
		return checkDWRelatedSORs;
	}
	public void setCheckDWRelatedSORs(String checkDWRelatedSORs) {
		this.checkDWRelatedSORs = checkDWRelatedSORs;
	}

	private AbstractEstimateService abstractEstimateService;
	public EstimateTemplateAction() {
		addRelatedEntity("workType", EgwTypeOfWork.class);
		addRelatedEntity("subType", EgwTypeOfWork.class);
	}
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return estimateTemplate;
	}
	
	protected void setModel(EstimateTemplate estimateTemplate) {
		this.estimateTemplate = estimateTemplate;
	}
	
	public String edit(){
		return EDIT;
	}
	public void prepare(){
		if(id != null){
			estimateTemplate=estimateTemplateService.findById(id, false);
		}
		AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
		ajaxEstimateAction.setPersistenceService(getPersistenceService());
		ajaxEstimateAction.setEmployeeService(employeeService);
		ajaxEstimateAction.setPersonalInformationService(personalInformationService);
		super.prepare();
		setupDropdownDataExcluding("workType","subType");
		addDropdownData("parentCategoryList", getPersistenceService().findAllBy("from EgwTypeOfWork etw1 where etw1.parentid is null"));
		List<EgUom> uomList = getPersistenceService().findAllBy("from EgUom  order by upper(uom)");
		if (!VIEW.equals(mode)) {
			uomList = abstractEstimateService.prepareUomListByExcludingSpecialUoms(uomList);
		}
		addDropdownData("uomList", uomList);
		addDropdownData("scheduleCategoryList", getPersistenceService().findAllBy("from ScheduleCategory order by upper(code)"));
		populateCategoryList(ajaxEstimateAction, estimateTemplate.getWorkType() != null);
			
	 }
	 public String newform(){
			return NEW;
	 }
	 
	public String save(){
		 estimateTemplate.getActivities().clear();
		 populateSorActivities();
		 populateNonSorActivities();
		 populateActivities();
		 if(estimateTemplate.getId()==null){
			 estimateTemplate.setStatus(1);
		 }
		 else{
			 setMode("edit");
		 }
		 estimateTemplate = estimateTemplateService.persist(estimateTemplate);  
		 return "success";
	 }
	 protected void populateSorActivities() {
		 for(EstimateTemplateActivity activity: sorActivities) {
			 if (validSorActivity(activity)) {
				 activity.setSchedule((ScheduleOfRate) getPersistenceService().find("from ScheduleOfRate where id = ?", activity.getSchedule().getId()));
				 activity.setUom(activity.getSchedule().getUom());
				 estimateTemplate.addActivity(activity);
			 }
		 }
	 }
	 protected boolean validSorActivity(EstimateTemplateActivity activity) {
		 if (activity != null && activity.getSchedule() != null && activity.getSchedule().getId() != null) {
			 return true;
		 }
		 
		 return false;
	 }
	 protected void populateNonSorActivities() {
		 for (EstimateTemplateActivity activity: nonSorActivities) {
			 if (activity!=null) {
				 activity.setUom(activity.getNonSor().getUom());
				 estimateTemplate.addActivity(activity);
			 }
		 }
	 }
	 private void populateActivities() {
		 for(EstimateTemplateActivity activity: estimateTemplate.getActivities()) {
			 activity.setEstimateTemplate(estimateTemplate);
		 }
	 }
	 protected void populateCategoryList(
				AjaxEstimateAction ajaxEstimateAction, boolean categoryPopulated) {
			if (categoryPopulated) {
				ajaxEstimateAction.setCategory(estimateTemplate.getWorkType().getId());
				ajaxEstimateAction.subcategories();
				addDropdownData("categoryList", ajaxEstimateAction.getSubCategories());		
			}
			else {
				addDropdownData("categoryList", Collections.emptyList());
			}
		}
	public boolean validCode() {
		boolean status=false;
		if(estimateTemplate!=null && estimateTemplate.getCode()!=null){
			AjaxEstimateTemplateAction ajaxEstimateTemplateAction=new AjaxEstimateTemplateAction();
			ajaxEstimateTemplateAction.setCode(estimateTemplate.getCode());
			ajaxEstimateTemplateAction.setPersistenceService(persistenceService);
			if(ajaxEstimateTemplateAction.getCodeCheck()){
				status= true;
			}
		}
		return status;
	}
	 
	public String search(){
		 estimateTemplate.setStatus(1);
		 return "search";
	 }
	 
	 public String searchDetails(){
		 if(estimateTemplate.getWorkType()==null || estimateTemplate.getWorkType().getId()==-1){
			 String messageKey = "estimate.template.search.workType.error";
			 addActionError(getText(messageKey));
			 return "search";
		 }
         setPageSize(WorksConstants.PAGE_SIZE);
		 super.search();
		 return "search";
	 }
	 
	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}
	public List<EstimateTemplateActivity> getSorActivities() {
		return sorActivities;
	}
	public void setSorActivities(List<EstimateTemplateActivity> sorActivities) {
		this.sorActivities = sorActivities;
	}
	public List<EstimateTemplateActivity> getNonSorActivities() {
		return nonSorActivities;
	}
	public void setNonSorActivities(List<EstimateTemplateActivity> nonSorActivities) {
		this.nonSorActivities = nonSorActivities;
	}
	public PersistenceService<EstimateTemplate, Long> getEstimateTemplateService() {
		return estimateTemplateService;
	}
	public void setEstimateTemplateService(
			PersistenceService<EstimateTemplate, Long> estimateTemplateService) {
		this.estimateTemplateService = estimateTemplateService;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
   	public Long getId() {
		return id;
	}
	public void setId(Long id) {
	    this.id = id;
	}
		
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder){
		String dynQuery =" from EstimateTemplate et where et.id is not null ";
		List<Object> paramList = new ArrayList<Object>();
		dynQuery = dynQuery + " and et.status = ?";
		paramList.add(estimateTemplate.getStatus());
		if(estimateTemplate.getWorkType()!=null && estimateTemplate.getWorkType().getId()!=-1){
			dynQuery = dynQuery + " and et.workType.id = ? ";
			paramList.add(estimateTemplate.getWorkType().getId());
		}
		if(estimateTemplate.getSubType() != null && estimateTemplate.getSubType().getId() !=-1){
			dynQuery = dynQuery + " and et.subType.id = ? ";
			paramList.add(estimateTemplate.getSubType().getId());
		}
		if(StringUtils.isNotBlank(estimateTemplate.getCode().trim())){
			dynQuery = dynQuery + " and UPPER(et.code) like '%"+estimateTemplate.getCode().trim().toUpperCase()+"%'";
		}
		if(StringUtils.isNotBlank(estimateTemplate.getName().trim())){
			dynQuery = dynQuery + " and UPPER(et.name) like '%"+estimateTemplate.getName().trim().toUpperCase()+"%'";
		}
		if(StringUtils.isNotBlank(estimateTemplate.getDescription().trim())){
			dynQuery = dynQuery + " and UPPER(et.description) like '%"+estimateTemplate.getDescription().trim().toUpperCase()+"%'";
		}
		String countQuery = "select distinct count(et) " + dynQuery;
		return new SearchQueryHQL(dynQuery, countQuery, paramList);
	}
	public String getSourcePage() {
		return sourcePage;
	}
	public void setSourcePage(String sourcePage) {
		this.sourcePage = sourcePage;
	}
	public Long getTypeOfWork() {
		return typeOfWork;
	}
	public void setTypeOfWork(Long typeOfWork) {
		this.typeOfWork = typeOfWork;
	}
	public String getEstimateTemplateCode() {
		return estimateTemplateCode;
	}
	public void setEstimateTemplateCode(String estimateTemplateCode) {
		this.estimateTemplateCode = estimateTemplateCode;
	}
	public Long getSubTypeOfWork() {
		return subTypeOfWork;
	}
	public void setSubTypeOfWork(Long subTypeOfWork) {
		this.subTypeOfWork = subTypeOfWork;
	}
	public AbstractEstimateService getAbstractEstimateService() {
		return abstractEstimateService;
	}
	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}
	
}