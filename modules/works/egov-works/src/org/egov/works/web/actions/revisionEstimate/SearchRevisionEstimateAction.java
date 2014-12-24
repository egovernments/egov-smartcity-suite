/**
 * 
 */
package org.egov.works.web.actions.revisionEstimate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.EgwStatus;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.web.actions.SearchFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.WorkType;
import org.egov.works.models.revisionEstimate.RevisionAbstractEstimate;
import org.egov.works.models.revisionEstimate.RevisionWorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;

/**
 * @author manoranjan
 *
 */
@ParentPackage("egov")
public class SearchRevisionEstimateAction extends SearchFormAction { 

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(SearchRevisionEstimateAction.class);
	private AbstractEstimate estimates = new AbstractEstimate();
	private String searchType;
	private Date fromDate;
	private Date toDate;
	private EmployeeService employeeService;
	private PersonalInformationService personalInformationService;
	private EgwStatus status;
	public static final Locale LOCALE = new Locale("en","IN");
	public static final SimpleDateFormat  DDMMYYYYFORMATS= new SimpleDateFormat("dd/MM/yyyy",LOCALE);
	private List<RevisionAbstractEstimate> revEstimateList;
	private WorksService worksService;
	private String source;
	private PersistenceService<RevisionAbstractEstimate,Long> revisionAbstractEstimateService;
	private WorkOrderEstimate workOrderEstimate;
	private String estimateNumber;
	private String workOrderNumber;
	private Integer execDept=-1;
	private String reStatus;
	private Long workOrderId; 
	private CommonsService commonsService;
	private String cancelRemarks;
	private String revisionEstimateNumber;
	private PersistenceService<RevisionWorkOrder,Long> revisionWorkOrderService;
	private String messageKey;
	public SearchRevisionEstimateAction(){
		
		addRelatedEntity("category", EgwTypeOfWork.class);
		addRelatedEntity("parentCategory", EgwTypeOfWork.class);
		addRelatedEntity("executingDepartment", DepartmentImpl.class);
		addRelatedEntity("estimatePreparedBy", PersonalInformation.class);
		addRelatedEntity("type", WorkType.class);
		addRelatedEntity("egwStatus", EgwStatus.class);
		
	}

	@Override
	public Object getModel() {
	
		return estimates;
	}
	@Override
	public void prepare() {
		super.prepare(); 
		AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
		ajaxEstimateAction.setPersistenceService(getPersistenceService());
		ajaxEstimateAction.setEmployeeService(employeeService);
		ajaxEstimateAction.setPersonalInformationService(personalInformationService);
		addDropdownData("statusList", persistenceService.findAllBy("from EgwStatus s where moduletype=? and code in ('CREATED','TECH_SANCTIONED','REJECTED','RESUBMITTED','CANCELLED','FINANCIALLY_SANCTIONED','APPROVED')order by orderId",AbstractEstimate.class.getSimpleName()));
		addDropdownData("executingDepartmentList", persistenceService.findAllBy("from DepartmentImpl dt"));
		addDropdownData("preparedByList", Collections.EMPTY_LIST);
		addDropdownData("typeList", persistenceService.findAllBy("from WorkType dt"));
		addDropdownData("parentCategoryList", getPersistenceService().findAllBy("from EgwTypeOfWork etw1 where etw1.parentid is null")); 
		addDropdownData("categoryList", Collections.emptyList());
		populateCategoryList(ajaxEstimateAction, estimates.getParentCategory() != null);
		populatePreparedByList(ajaxEstimateAction,execDept!=null);

		if("cancelRE".equals(source)) {
			EgwStatus egwstat=(EgwStatus) persistenceService.find("from EgwStatus s where moduletype=? and code = 'APPROVED' order by orderId",AbstractEstimate.class.getSimpleName());
			setReStatus(egwstat.getId().toString());
		}
	}
	public String beforeSearch(){
		
		return "search";
	}
	public String searchForRWO(){
		
		return "createRWO";
	}
	
	public String searchRE(){
		return "search";
	}
	
	public String ajaxFilterRE(){
		EgwStatus status = (EgwStatus)persistenceService.find("from EgwStatus where moduletype='AbstractEstimate' and description='APPROVED'");
		revEstimateList = revisionAbstractEstimateService.findAllBy("from  RevisionAbstractEstimate where  parent is not null and estimateNumber like ? and egwStatus=?",parameters.get("estimateNumber")[0]+"%",status);
	
	return "revEstnum";
}
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		if("createRevWorkOrder".equals(source)){
			StringBuffer query= new StringBuffer();
			query.append("from WorkOrderEstimate woe where woe.estimate.parent is not null " +
					"and ((woe.workOrder.egwStatus.code='NEW' and woe.workOrder.state is null) or (woe.workOrder.egwStatus.code='CANCELLED')) and woe.estimate.egwStatus.code='APPROVED' ");
			if(null != fromDate){
				query.append(" and woe.estimate.estimateDate >= to_date('"+DDMMYYYYFORMATS.format(fromDate)+"','dd/MM/yyyy')");
			}
			if(null != toDate){
				query.append(" and woe.estimate.estimateDate <= to_date('"+DDMMYYYYFORMATS.format(toDate)+"','dd/MM/yyyy')"); 
			}
			if(null !=estimates.getEstimateNumber() && StringUtils.isNotEmpty(estimates.getEstimateNumber()) ){
				query.append(" and woe.estimate.estimateNumber like '%" +estimates.getEstimateNumber()+"%' order by woe.id desc");
			}
			
			/*query.append(" and woe.estimate.id not in (select woe.estimate.id from WorkOrderEstimate woe1 where woe1.estimate.id=woe.estimate.id and woe1.estimate.egwStatus.code='CANCELLED')");
			query.append(" and woe.workOrder.id not in (select woe.workOrder.id from WorkOrderEstimate woe2 where woe2.workOrder.id=woe.workOrder.id and woe2.workOrder.egwStatus.code='CANCELLED')");*/
			LOGGER.debug("SearchRevisionEstimateForRevisionWorkOrder | prepareQuery | query >>>> "+query.toString());
			return new SearchQueryHQL("select woe "+query.toString(), "select count(distinct woe.id) "+query.toString(), null);
			
		} else {
			StringBuffer query = new StringBuffer(300);
			query.append("from WorkOrderEstimate woeP, WorkOrderEstimate woeC where woeP.estimate.id= woeC.estimate.parent.id and woeC.estimate.egwStatus.code!='NEW' ");
			if(reStatus != null && !"".equalsIgnoreCase(reStatus) && !"-1".equals(reStatus)){
			query.append(" and woeC.estimate.egwStatus.id="+getReStatus());
			}
			if(getExecDept()!=null && getExecDept()!= -1){
				query.append(" and woeC.estimate.executingDepartment.id= " +getExecDept());
			}
			if(null !=estimates.getEstimateNumber() && StringUtils.isNotEmpty(estimates.getEstimateNumber()) ){
				query.append(" and woeC.estimate.estimateNumber like '%" +estimates.getEstimateNumber()+"%'");
			}
			if(estimates.getType()!=null){
				query.append(" and woeP.estimate.type.id="+estimates.getType().getId());
			}
			if(null != fromDate && getFieldErrors().isEmpty()){
				query.append(" and woeC.estimate.estimateDate >= to_date('"+DDMMYYYYFORMATS.format(fromDate)+"','dd/MM/yyyy')");
			}
			if(null != toDate && getFieldErrors().isEmpty()){
				query.append(" and woeC.estimate.estimateDate <= to_date('"+DDMMYYYYFORMATS.format(toDate)+"','dd/MM/yyyy')"); 
			}
			if(estimates.getCategory()!=null){
				query.append(" and woeP.estimate.category.id="+estimates.getCategory().getId());
			}
			if(estimates.getParentCategory()!=null){
				query.append(" and woeP.estimate.parentCategory.id="+estimates.getParentCategory().getId());
			}
			if(null !=estimates.getEstimatePreparedBy() && estimates.getEstimatePreparedBy().getId() != -1){
				query.append(" and woeC.estimate.estimatePreparedBy.idPersonalInformation="+estimates.getEstimatePreparedBy().getId());	
			}
			if(null !=workOrderNumber ){
				query.append(" and woeC.workOrder.workOrderNumber like '%" +workOrderNumber+"%'");
			}
				
			LOGGER.debug("SearchRevisionEstimate | prepareQuery | query >>>> "+query.toString()); 
			return new SearchQueryHQL("select woeC "+query.toString(), "select count(distinct woeC.id) "+query.toString(), null);
		}
	}
	
	

	public String cancelApprovedRE() {  
		RevisionWorkOrder workOrder = revisionWorkOrderService.findById(workOrderId, false);
		RevisionAbstractEstimate rae= revisionAbstractEstimateService.findById(workOrder.getWorkOrderEstimates().get(0).getEstimate().getId(), false);
		
		if(workOrder.getCreationType()==null  && workOrder.getEgwStatus().getCode().equals("APPROVED")) {
			workOrder.setEgwStatus(commonsService.getStatusByModuleAndCode("WorkOrder","CANCELLED")); 
			PersonalInformation prsnlInfo=employeeService.getEmpForUserId(Integer.valueOf(getLoggedInUserId()));			
			String empName="";
			rae.getCurrentState().getPrevious().setValue("CANCELLED");
			rae.setEgwStatus(commonsService.getStatusByModuleAndCode("AbstractEstimate","CANCELLED"));
			if(prsnlInfo.getEmployeeFirstName()!=null)
				empName=prsnlInfo.getEmployeeFirstName();
			if(prsnlInfo.getEmployeeLastName()!=null)
				empName=empName.concat(" ").concat(prsnlInfo.getEmployeeLastName()); 			
			rae.getCurrentState().getPrevious().setText1(cancelRemarks+". Revision Estimate Cancelled by: "+empName);
		}
		else if(workOrder.getCreationType()==null && workOrder.getEgwStatus().getCode().equals("NEW") && workOrder.getCurrentState()==null){
			workOrder.setEgwStatus(commonsService.getStatusByModuleAndCode("WorkOrder","CANCELLED"));
			rae.getCurrentState().getPrevious().setValue("CANCELLED");
			rae.setEgwStatus(commonsService.getStatusByModuleAndCode("AbstractEstimate","CANCELLED"));
			
			PersonalInformation prsnlInfo=employeeService.getEmpForUserId(Integer.valueOf(getLoggedInUserId()));			
			String empName="";
			if(prsnlInfo.getEmployeeFirstName()!=null)
				empName=prsnlInfo.getEmployeeFirstName();
			if(prsnlInfo.getEmployeeLastName()!=null)
				empName=empName.concat(" ").concat(prsnlInfo.getEmployeeLastName()); 			
			rae.getCurrentState().getPrevious().setText1(cancelRemarks+". Revision Estimate Cancelled by: "+empName);
		}
		else{
			rae.getCurrentState().getPrevious().setValue("CANCELLED");
			rae.setEgwStatus(commonsService.getStatusByModuleAndCode("AbstractEstimate","CANCELLED"));
			
			PersonalInformation prsnlInfo=employeeService.getEmpForUserId(Integer.valueOf(getLoggedInUserId()));			
			String empName="";
			if(prsnlInfo.getEmployeeFirstName()!=null)
				empName=prsnlInfo.getEmployeeFirstName();
			if(prsnlInfo.getEmployeeLastName()!=null)
				empName=empName.concat(" ").concat(prsnlInfo.getEmployeeLastName()); 			
			rae.getCurrentState().getPrevious().setText1(cancelRemarks+". Revision Estimate Cancelled by: "+empName);
		}
		revisionEstimateNumber=rae.getEstimateNumber(); 
		messageKey=revisionEstimateNumber+": The Revision Estimate was Cancelled successfully"; 
		return SUCCESS;
	}
	
	public int getLoggedInUserId() {
		return Integer.parseInt(EGOVThreadLocals.getUserId());
	}
	
	public String list(){
		boolean isError=false;
		if(fromDate!=null && toDate==null){
			addFieldError("enddate",getText("search.endDate.null"));
			isError=true;
		}
		if(toDate!=null && fromDate==null){
			addFieldError("startdate",getText("search.startDate.null"));		
			isError=true;
		}
		
		if(!DateUtils.compareDates(getToDate(),getFromDate())){
			addFieldError("enddate",getText("greaterthan.endDate.fromDate"));
			isError=true;
		}
		
		if(isError){
			return "search";
		}
		setPageSize(WorksConstants.PAGE_SIZE);
		search();
		return "search";
	}
	
	public String listOfRevEst(){
		boolean isError=false;
		if(fromDate!=null && toDate==null){
			addFieldError("enddate",getText("search.endDate.null"));
			isError=true;
		}
		if(toDate!=null && fromDate==null){
			addFieldError("startdate",getText("search.startDate.null"));		
			isError=true;
		}
		
		if(!DateUtils.compareDates(getToDate(),getFromDate())){
			addFieldError("enddate",getText("greaterthan.endDate.fromDate"));
			isError=true;
		}
		
		if(isError){
			return "createRWO";
		}
		setPageSize(WorksConstants.PAGE_SIZE);
		super.search();
		return "createRWO";
		}
	
	public List<String> getActionsList() { 
		String actions = worksService.getWorksConfigValue("REVEST_SHOW_ACTIONS");
		if(actions!=null)
		  return Arrays.asList(actions.split(","));
		return new ArrayList<String>();
	}
	
	protected void populateCategoryList(
			AjaxEstimateAction ajaxEstimateAction, boolean categoryPopulated) {
		if (categoryPopulated) {
			ajaxEstimateAction.setCategory(estimates.getParentCategory().getId());
			ajaxEstimateAction.subcategories();
			addDropdownData("categoryList", ajaxEstimateAction.getSubCategories());		
		}
		else {
			addDropdownData("categoryList", Collections.emptyList());
		}
	}
	protected void populatePreparedByList(AjaxEstimateAction ajaxEstimateAction, boolean executingDeptPopulated){
		if (executingDeptPopulated) {
			ajaxEstimateAction.setExecutingDepartment(execDept);
			ajaxEstimateAction.setWorksService(worksService);
			ajaxEstimateAction.usersInExecutingDepartment();
			
			addDropdownData("preparedByList", ajaxEstimateAction.getUsersInExecutingDepartment());
		}
		else {
			addDropdownData("preparedByList", Collections.emptyList());
		}
	}
	
	public AbstractEstimate getEstimates() {
		return estimates;
	}

	public void setEstimates(AbstractEstimate estimates) {
		this.estimates = estimates;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}

	public EgwStatus getStatus() {
		return status;
	}

	public void setStatus(EgwStatus status) {
		this.status = status;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public List<RevisionAbstractEstimate> getRevEstimateList() {
		return revEstimateList;
	}

	public void setRevEstimateList(List<RevisionAbstractEstimate> revEstimateList) {
		this.revEstimateList = revEstimateList;
	}

	public void setRevisionAbstractEstimateService(
			PersistenceService<RevisionAbstractEstimate, Long> revisionAbstractEstimateService) {
		this.revisionAbstractEstimateService = revisionAbstractEstimateService;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public WorkOrderEstimate getWorkOrderEstimate() {
		return workOrderEstimate;
	}

	public void setWorkOrderEstimate(WorkOrderEstimate workOrderEstimate) {
		this.workOrderEstimate = workOrderEstimate;
	}

	public String getEstimateNumber() {
		return estimateNumber;
	}

	public void setEstimateNumber(String estimateNumber) {
		this.estimateNumber = estimateNumber;
	}

	public Integer getExecDept() {
		return execDept;
	}

	public void setExecDept(Integer execDept) {
		this.execDept = execDept;
	}

	public String getReStatus() {
		return reStatus;
	}

	public void setReStatus(String reStatus) {
		this.reStatus = reStatus;
	}

	public String getWorkOrderNumber() {
		return workOrderNumber;
	}

	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
	}

	public Long getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(Long workOrderId) {
		this.workOrderId = workOrderId;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setRevisionWorkOrderService(
			PersistenceService<RevisionWorkOrder, Long> revisionWorkOrderService) {
		this.revisionWorkOrderService = revisionWorkOrderService;
	}

	public String getCancelRemarks() {
		return cancelRemarks;
	}

	public void setCancelRemarks(String cancelRemarks) {
		this.cancelRemarks = cancelRemarks;
	}

	public String getRevisionEstimateNumber() {
		return revisionEstimateNumber;
	}

	public void setRevisionEstimateNumber(String revisionEstimateNumber) {
		this.revisionEstimateNumber = revisionEstimateNumber;
	}

	public String getMessageKey() {
		return messageKey;
	}

}
