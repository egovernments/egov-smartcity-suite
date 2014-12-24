package org.egov.works.web.actions.revisionEstimate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.revisionEstimate.RevisionWOCreationType;
import org.egov.works.models.revisionEstimate.RevisionWorkOrder;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;

@ParentPackage("egov")
public class SearchRevisionWorkOrderAction extends SearchFormAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(SearchRevisionEstimateAction.class);
	private static final String SEARCH_RWO="search";
	private WorkOrderService workOrderService;
	private WorkOrder workOrders= new WorkOrder();
	private String estimateNumber;
	private String workOrderNumber;
	private Date fromDate;
	private Date toDate;
	private Long contractorId;
	private String woStatus;
	private List<WorkOrderEstimate> workOrderEstimatesList;
	private EmployeeService employeeService;
	private WorksService worksService;
	public static final Locale LOCALE = new Locale("en","IN");
	public static final SimpleDateFormat  DDMMYYYYFORMATS= new SimpleDateFormat("dd/MM/yyyy",LOCALE);
	private RevisionWorkOrder revisionWorkOrder = new RevisionWorkOrder();
	private String source;
	private PersistenceService<RevisionWorkOrder,Long> revisionWorkOrderService;
	private Long workOrderId; 
	private String cancelRemarks;
	private CommonsService commonsService;
	private String revisionWorkOrderNumber;
	private String messageKey;
	public SearchRevisionWorkOrderAction() {
		addRelatedEntity("contractor", Contractor.class);
		addRelatedEntity("egwStatus", EgwStatus.class);
	} 
	
	@Override
	public Object getModel() {
		return workOrders;
	}

	@Override
	public void prepare() {
		addDropdownData("statusList", persistenceService.findAllBy("from EgwStatus s where moduletype=? and code in ('CREATED','CHECKED','APPROVED','REJECTED','RESUBMITTED','CANCELLED') order by orderId",WorkOrder.class.getSimpleName()));
		if("cancelRWO".equals(source)) {
			setWoStatus("APPROVED"); 
		}
	}
	@ValidationErrorPage(value=SEARCH_RWO)
	public String beforeSearch() {

		return SEARCH_RWO;
	}
	
	public String searchRWO(){
		return "search";
	}
	
	public Map<String,Object> getContractorForApprovedWorkOrder() {
		Map<String,Object> contractorsWithWOList = new LinkedHashMap<String, Object>();		
		if(workOrderService.getContractorsWithWO()!=null) {
			for(Contractor contractor :workOrderService.getContractorsWithWO()){
				contractorsWithWOList.put(contractor.getId()+"", contractor.getCode()+" - "+contractor.getName());
			}			
		}
		return contractorsWithWOList; 
	} 
	private Map getQuery(){
		StringBuffer query = new StringBuffer(300);
		List<Object> paramList = new ArrayList<Object>();
		HashMap<String,Object> queryAndParams=new HashMap<String,Object>();
		Map queryAndParms=null;
		if("cancelRWO".equals(source)){
			query.append("from WorkOrderEstimate woe where  woe.workOrder.parent is not null and woe.workOrder.egwStatus.code='APPROVED'");
		}
		else
			query.append("from WorkOrderEstimate woe where  woe.workOrder.parent is not null and woe.workOrder.egwStatus.code<>'NEW'");
				
		if(woStatus != null && !"".equalsIgnoreCase(woStatus) && !"-1".equals(woStatus)){ 
			query.append(" and UPPER(woe.workOrder.egwStatus.code)=?");
			paramList.add(StringUtils.trim(getWoStatus()).toUpperCase());
			}
		if(null !=getEstimateNumber() && StringUtils.isNotEmpty(getEstimateNumber()) ){
			query.append(" and UPPER(woe.estimate.estimateNumber) like '%'||?||'%'");
			paramList.add(StringUtils.trim(getEstimateNumber()).toUpperCase());
		}
		
		if(null !=getWorkOrderNumber() && StringUtils.isNotEmpty(getWorkOrderNumber()) ){
			query.append(" and UPPER(woe.workOrder.workOrderNumber) like '%'||?||'%'");
			paramList.add(StringUtils.trim(getWorkOrderNumber()).toUpperCase());
		}
		
		if(fromDate!=null && toDate!=null && getFieldErrors().isEmpty()){
			query.append(" and woe.workOrder.workOrderDate between ? and ? ");
			paramList.add(fromDate);
			paramList.add(toDate);
		}
		if(getContractorId()!= -1){
			query.append(" and woe.workOrder.contractor.id=? ");
			paramList.add(Long.valueOf(getContractorId()));
		}
		query.append(" and woe.workOrder.id in (select id from RevisionWorkOrder where creationType=?)");
		paramList.add(RevisionWOCreationType.EXTERNAL);
		queryAndParams.put("query", query.toString());
		queryAndParams.put("params", paramList);
		return queryAndParams;
	}
	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		String query = null;
		String countQuery = null;
		Map queryAndParms = null;
		List<Object> paramList = new ArrayList<Object>();
		queryAndParms = getQuery();
		paramList = (List<Object>) queryAndParms.get("params");
		query = (String) queryAndParms.get("query");
		countQuery = "select count(distinct woe.id) " + query;
		query = "select distinct woe " + query;
		return new SearchQueryHQL(query, countQuery, paramList);
	}
	
	
	public String searchWorkOrders() {
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
			return SEARCH_RWO;
		}

		setPageSize(WorksConstants.PAGE_SIZE);
		if(getFieldErrors().isEmpty()) {
			setPageSize(WorksConstants.PAGE_SIZE);
			super.search();
			if(searchResult.getFullListSize() !=0){
				workOrderEstimatesList = getPositionAndUser(searchResult.getList());
			    searchResult.getList().clear();
			    searchResult.getList().addAll(workOrderEstimatesList);
			}
		}
		return SEARCH_RWO;
	}
	
	 protected List<WorkOrderEstimate> getPositionAndUser(List<WorkOrderEstimate> results){
			List<WorkOrderEstimate> woeList = new ArrayList<WorkOrderEstimate>();
			for(WorkOrderEstimate woe :results){
				PersonalInformation emp = employeeService.getEmployeeforPosition(woe.getWorkOrder().getCurrentState().getOwner());
				if(emp!=null)
					woe.getWorkOrder().setOwner(emp.getEmployeeName());
				woeList.add(woe);
			}	
			return woeList;
		}
	 
	public List<String> getActionsList() {
		String actions = worksService.getWorksConfigValue("REVWO_SHOW_ACTIONS");
		if (actions != null)
			return Arrays.asList(actions.split(","));
		return new ArrayList<String>();
	}
	
	
	public String cancelApprovedRWO() {  
		RevisionWorkOrder workOrder = revisionWorkOrderService.findById(workOrderId, false);
		
			workOrder.getCurrentState().getPrevious().setValue("CANCELLED");
			workOrder.setEgwStatus(commonsService.getStatusByModuleAndCode("WorkOrder","CANCELLED"));
			
			PersonalInformation prsnlInfo=employeeService.getEmpForUserId(Integer.valueOf(getLoggedInUserId()));			
			String empName="";
			if(prsnlInfo.getEmployeeFirstName()!=null)
				empName=prsnlInfo.getEmployeeFirstName();
			if(prsnlInfo.getEmployeeLastName()!=null)
				empName=empName.concat(" ").concat(prsnlInfo.getEmployeeLastName()); 			
			workOrder.getCurrentState().getPrevious().setText1(cancelRemarks+". Revision WorkOrder Cancelled by: "+empName);

			revisionWorkOrderNumber=workOrder.getWorkOrderNumber(); 
		messageKey=revisionWorkOrderNumber+": The Revision WorkOrder was Cancelled successfully"; 
		return SUCCESS;
	}
	
	public int getLoggedInUserId() {
		return Integer.parseInt(EGOVThreadLocals.getUserId());
	}
 
	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}
	public String getEstimateNumber() {
		return estimateNumber;
	}
	public void setEstimateNumber(String estimateNumber) {
		this.estimateNumber = estimateNumber;
	}
	public String getWorkOrderNumber() {
		return workOrderNumber;
	}
	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	public Long getContractorId() {
		return contractorId;
	}

	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}

	public String getWoStatus() {
		return woStatus;
	}

	public void setWoStatus(String woStatus) {
		this.woStatus = woStatus;
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Long getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(Long workOrderId) {
		this.workOrderId = workOrderId;
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

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public String getRevisionWorkOrderNumber() {
		return revisionWorkOrderNumber;
	}

	public void setRevisionWorkOrderNumber(String revisionWorkOrderNumber) {
		this.revisionWorkOrderNumber = revisionWorkOrderNumber;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

}
