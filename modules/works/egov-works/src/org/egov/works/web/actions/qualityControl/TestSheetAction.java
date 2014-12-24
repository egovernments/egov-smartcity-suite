package org.egov.works.web.actions.qualityControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.ValidationException;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.workflow.GenericWorkFlowAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.qualityControl.TestMaster;
import org.egov.works.models.qualityControl.TestSheetDetails;
import org.egov.works.models.qualityControl.TestSheetHeader;
import org.egov.works.models.tender.SetStatus;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;
import org.egov.works.services.qualityControl.TestSheetHeaderService;
import org.egov.works.utils.WorksConstants;

public class TestSheetAction extends GenericWorkFlowAction{

	private static final String SEARCH_WO="searchWorkOrder"; 
	private CommonsService commonsService;
	private WorkOrderService workOrderService;
	private DepartmentService departmentService;
	private String estimateNumber;
	private String status;
	private Date fromDate;
	private Date toDate;
	private Integer deptId;  
	private Long contractorId;
	private Map<String,Object> criteriaMap=null;
	private String workOrderNumber;
	private static final String DATE_FORMAT="dd-MMM-yyyy";
	private List<WorkOrder> workOrderList=null;
	private EmployeeService employeeService;
	private static final String WF_APPROVED="APPROVED";
	private static final String CANCELLED="CANCELLED";
	private final static String APPROVED="APPROVED";
	private WorksService worksService;
	private Long workOrderId;
	private WorkOrder workOrder;
	private String sourcePage;
	private Long id;
	private TestSheetHeader testSheetHeader = new TestSheetHeader(); 
	private List<TestSheetDetails> actionTestSheetDetails = new LinkedList<TestSheetDetails>();
	private TestSheetHeaderService testSheetHeaderService;
	private static final Logger LOGGER = Logger.getLogger(TestSheetAction.class);
	private String tSheetNumber;
	private String woNumber;
	private PersistenceService<Contractor, Long> contractorService;
	private static final String SAVE_ACTION = "Save";
	private AbstractEstimateService abstractEstimateService;
	private String designation;
	private String employeeName;
	private String messageKey;
	private WorkflowService<TestSheetHeader> workflowService;
	
	@Override
	public StateAware getModel() {
		// TODO Auto-generated method stub
		return testSheetHeader; 
	}
	
	@SkipValidation
	public String newform(){  
		return NEW;  
	} 
	
	public String edit(){
		return NEW;
	}
	
	public TestSheetAction() {
	}
	
	@ValidationErrorPage(value=SEARCH_WO)
	public String searchWorkOrder(){
		return SEARCH_WO;
	}
	
	public List<EgwStatus> getWorkOrderStatuses() {
		List<EgwStatus> woStatusList=commonsService.getStatusByModule(WorkOrder.class.getSimpleName());
		List<EgwStatus> latestStatusList = new ArrayList<EgwStatus>();
		if(!woStatusList.isEmpty()){
			for(EgwStatus egwStatus : woStatusList){
				if(egwStatus.getCode().equals("APPROVED")){
					latestStatusList.add(egwStatus);
				}
			}
		}
		return latestStatusList;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}
	
	public void prepare() {
		super.prepare();
		addDropdownData("deptListForSearch",departmentService.getAllDepartments());
		addDropdownData("materialTypeList", persistenceService.findAllBy(" select distinct(tm.materialType) from TestMaster tm where tm.materialType.isActive=1 order by name "));
		
		if(workOrderId != null && (sourcePage!=null && sourcePage!="" && sourcePage.equalsIgnoreCase("createTestSheet"))){
			workOrder=workOrderService.findById(workOrderId, false);
			workOrderNumber=workOrder.getWorkOrderNumber();
		}
		
		if (id != null ) {
			testSheetHeader= testSheetHeaderService.findById(id, false);
			workOrder = testSheetHeader.getWorkOrder();
			workOrderNumber = workOrder.getWorkOrderNumber();
			if(sourcePage.equalsIgnoreCase("search") || sourcePage.equalsIgnoreCase("inbox")) {
				populateTestNames();
			}
		}
		
	}
	
	private void populateTestNames() {
		if(testSheetHeader.getTestSheetDetails()!=null && testSheetHeader.getTestSheetDetails().size()!=0){
			 List<TestMaster>	tmList = new ArrayList<TestMaster>();
			 Map<Long,List<TestMaster>> tnMap = new HashMap<Long,List<TestMaster>>();
			for(TestSheetDetails tsh : testSheetHeader.getTestSheetDetails()){
				if(!tnMap.containsKey(tsh.getTestMaster().getMaterialType().getId())){
					tmList=persistenceService.findAllBy("from TestMaster where materialType.id = ?",tsh.getTestMaster().getMaterialType().getId());
					tnMap.put(tsh.getTestMaster().getMaterialType().getId(), tmList);
				}
				tsh.setTestNamesIdList(tnMap.get(tsh.getTestMaster().getMaterialType().getId())); 
			}
		}
	}
	
	public Map<String,Object> getContractorForApprovedWorkOrder() {
		Map<String,Object> contractorsList = new LinkedHashMap<String, Object>();
		if(sourcePage!=null && sourcePage.equalsIgnoreCase("searchTSheetForSampleLetter")){
			List<Contractor> cntractrList=contractorService.findAllByNamedQuery("getTestSheetContractorsWithNoSampleLetter");
			if(cntractrList!=null && !cntractrList.isEmpty()) {
				for(Contractor contractor :cntractrList){
					contractorsList.put(contractor.getId()+"", contractor.getCode()+" - "+contractor.getName());
				}			
			}
		} 
		else{
			Map<String,Object> contractorsWithWOList = new LinkedHashMap<String, Object>();		
			if(workOrderService.getContractorsWithWO()!=null) {
				for(Contractor contractor :workOrderService.getContractorsWithWO()){
					contractorsList.put(contractor.getId()+"", contractor.getCode()+" - "+contractor.getName());
				}			
			}
		}
		
		return contractorsList; 
	}
	
	@ValidationErrorPage(value=SEARCH_WO)
	public String searchWorkOrderDetails(){
		criteriaMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(status) && !getStatus().equals("-1"))	{
			criteriaMap.put("STATUS",status);
		}
		if(StringUtils.isNotBlank(workOrderNumber))
			criteriaMap.put("WORKORDER_NO",workOrderNumber);
		if(StringUtils.isNotBlank(getEstimateNumber()))
			criteriaMap.put("ESTIMATE_NO",getEstimateNumber());
		if(getDeptId()!=null && getDeptId()>0)
			criteriaMap.put("DEPT_ID",getDeptId()); 
		if(contractorId!=null && contractorId!=-1)
			criteriaMap.put("CONTRACTOR_ID", contractorId);
		if(fromDate!=null && toDate!=null && !DateUtils.compareDates(getToDate(),getFromDate()))
			addFieldError("enddate",getText("greaterthan.endDate.fromDate"));
		if(toDate!=null && !DateUtils.compareDates(new Date(),getToDate()))
			addFieldError("enddate",getText("greaterthan.endDate.currentdate"));
		if(!getFieldErrors().isEmpty())
			return SEARCH_WO;
		if(fromDate!=null && toDate==null){
			criteriaMap.put("FROM_DATE",new Date(DateUtils.getFormattedDate(getFromDate(),DATE_FORMAT )));
		}else if(toDate!=null && fromDate==null){
			 criteriaMap.put("TO_DATE",new Date(DateUtils.getFormattedDate(getToDate(),DATE_FORMAT )));
		}else if(fromDate!=null && toDate!=null && getFieldErrors().isEmpty()){
			criteriaMap.put("FROM_DATE", new Date(DateUtils.getFormattedDate(getFromDate(),DATE_FORMAT )));
		    criteriaMap.put("TO_DATE",new Date(DateUtils.getFormattedDate(getToDate(),DATE_FORMAT)));
		}
		
		setPageSize(WorksConstants.PAGE_SIZE);
		search();
		if(searchResult.getFullListSize() !=0){
            workOrderList = getPositionAndUser(searchResult.getList());
		    searchResult.getList().clear();
		    searchResult.getList().addAll(workOrderList);
		}
		return SEARCH_WO;
	}
	
	
	@ValidationErrorPage(value=SEARCH_WO)
	public String searchTestSheetDetails(){
		criteriaMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(tSheetNumber))
			criteriaMap.put("TESTSHEET_NO",tSheetNumber);
		if(StringUtils.isNotBlank(woNumber))
			criteriaMap.put("WORKORDER_NO",woNumber);
		if(StringUtils.isNotBlank(getEstimateNumber()))
			criteriaMap.put("ESTIMATE_NO",getEstimateNumber());
		if(getDeptId()!=null && getDeptId()>0)
			criteriaMap.put("DEPT_ID",getDeptId()); 
		if(contractorId!=null && contractorId!=-1)
			criteriaMap.put("CONTRACTOR_ID", contractorId);
		if(fromDate!=null && toDate!=null && !DateUtils.compareDates(getToDate(),getFromDate()))
			addFieldError("enddate",getText("greaterthan.endDate.fromDate"));
		if(toDate!=null && !DateUtils.compareDates(new Date(),getToDate()))
			addFieldError("enddate",getText("greaterthan.endDate.currentdate"));
		if(!getFieldErrors().isEmpty())
			return SEARCH_WO;
		if(fromDate!=null && toDate==null){
			criteriaMap.put("FROM_DATE",new Date(DateUtils.getFormattedDate(getFromDate(),DATE_FORMAT )));
		}else if(toDate!=null && fromDate==null){
			 criteriaMap.put("TO_DATE",new Date(DateUtils.getFormattedDate(getToDate(),DATE_FORMAT )));
		}else if(fromDate!=null && toDate!=null && getFieldErrors().isEmpty()){
			criteriaMap.put("FROM_DATE", new Date(DateUtils.getFormattedDate(getFromDate(),DATE_FORMAT )));
		    criteriaMap.put("TO_DATE",new Date(DateUtils.getFormattedDate(getToDate(),DATE_FORMAT)));
		}
			setPageSize(WorksConstants.PAGE_SIZE);
			search();
		return SEARCH_WO;
	}
	
	protected List<WorkOrder> getPositionAndUser(List<WorkOrder> results){
		List<WorkOrder> workOrderList = new ArrayList<WorkOrder>();
		for(WorkOrder workOrder :results){
			PersonalInformation emp = employeeService.getEmployeeforPosition(workOrder.getCurrentState().getOwner());
			if(emp!=null)
				workOrder.setOwner(emp.getEmployeeName());
			workOrderList.add(workOrder);
			
			if(workOrder.getState()!=null && workOrder.getState().getPrevious()!=null &&
					workOrder.getState().getPrevious().getValue().equals(WF_APPROVED)){
				SetStatus set_status=((SetStatus)persistenceService.findByNamedQuery("getmaxStatusByObjectId_Type",workOrder.getId(),
						workOrder.getId(),WorkOrder.class.getSimpleName(),WorkOrder.class.getSimpleName()));
				if(set_status==null)
					workOrder.setStatus(workOrder.getState().getPrevious().getValue());
				else
					workOrder.setStatus(set_status.getEgwStatus().getCode());
			}
			else{
				if(workOrder.getState()!=null && workOrder.getState().getPrevious()!=null && 
						(workOrder.getState().getPrevious().getValue().equals(APPROVED) || workOrder.getState().getPrevious().getValue().equals(CANCELLED))){
					workOrder.setStatus(workOrder.getState().getPrevious().getValue());
				}
				else{
					workOrder.setStatus(workOrder.getState().getValue());
				}
			}		
		}	
		
		return workOrderList;
	}
		
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder){
		List<Object> paramList = new ArrayList<Object>();
		String queryString;
		String countQuery;
		if(sourcePage!=null && sourcePage.equalsIgnoreCase("searchTSheetForSampleLetter")){
			String dynQuery =" from TestSheetHeader tsh" 
					+ " where tsh.id is not null and tsh.egwStatus.code='APPROVED' and tsh.id not in (select testSheetHeader.id from SampleLetterHeader" +
					" where egwStatus.code!='CANCELLED') " ;
				
				if(criteriaMap.get("FROM_DATE") != null && criteriaMap.get("TO_DATE")==null) {
					dynQuery = dynQuery + " and tsh.testSheetDate >= ? ";
					paramList.add(criteriaMap.get("FROM_DATE"));

				}else if(criteriaMap.get("TO_DATE") != null && criteriaMap.get("FROM_DATE")==null) {
					dynQuery = dynQuery + " and tsh.testSheetDate <= ? ";
					paramList.add(criteriaMap.get("TO_DATE"));
				}else if(criteriaMap.get("FROM_DATE") != null && criteriaMap.get("TO_DATE")!=null) {
					dynQuery = dynQuery + " and tsh.testSheetDate between ? and ? ";
					paramList.add(criteriaMap.get("FROM_DATE")); 
					paramList.add(criteriaMap.get("TO_DATE")); 
				}
				if(criteriaMap.get("WORKORDER_NO") != null){
					dynQuery = dynQuery + " and UPPER(tsh.workOrder.workOrderNumber) like '%"+criteriaMap.get("WORKORDER_NO").toString().trim().toUpperCase()+"%'";
				}
				if(criteriaMap.get("ESTIMATE_NO") != null){
					dynQuery = dynQuery + " and tsh.workOrder.id in (select distinct woe.workOrder.id from WorkOrderEstimate woe where " +
				"UPPER(woe.estimate.estimateNumber) like '%"+criteriaMap.get("ESTIMATE_NO").toString().trim().toUpperCase()+"%')";
				}
				if(criteriaMap.get("TESTSHEET_NO") != null){
					dynQuery = dynQuery + " and UPPER(tsh.testSheetNumber) like '%"+criteriaMap.get("TESTSHEET_NO").toString().trim().toUpperCase()+"%'";
				}
				if(criteriaMap.get("CONTRACTOR_ID") != null){
					dynQuery = dynQuery + " and tsh.workOrder.contractor.id = ? ";
					paramList.add(criteriaMap.get("CONTRACTOR_ID"));
				}
				if(criteriaMap.get("DEPT_ID") != null){
					dynQuery = dynQuery + " and tsh.workOrder.id in (select we.workOrder.id from WorkOrderEstimate we where we.workOrder.id=tsh.workOrder.id and " +
							" we.estimate.executingDepartment.id = ?) ";
					paramList.add(criteriaMap.get("DEPT_ID"));
				}
				
			 queryString="select distinct tsh "+	dynQuery;
			 countQuery = "select distinct count(tsh) " + dynQuery;
		}
		else{
		String dynQuery =" from WorkOrder wo left join wo.workOrderEstimates workOrderEstimate" 
				+ " where wo.id is not null and wo.parent is null and wo.state.value<>'NEW' " ;
		
			String setStat = worksService.getWorksConfigValue("WorkOrder.setstatus");
			
			if(criteriaMap.get("STATUS") != null) {
				if(criteriaMap.get("STATUS").equals("APPROVED") || 
						criteriaMap.get("STATUS").equals("CANCELLED")){
					dynQuery = dynQuery + " and wo.state.previous.value = ? and " +
							" wo.id not in (select objectId from SetStatus where objectType='WorkOrder')";
					paramList.add(criteriaMap.get("STATUS"));
				}else if(!criteriaMap.get("STATUS").equals("-1") && Arrays.asList(setStat.split(",")).contains(criteriaMap.get("STATUS")))
				{
				dynQuery = dynQuery + " and wo.id in(select stat.objectId from " +
							"SetStatus stat where stat.egwStatus.code=? and stat.id = (select" +
					" max(stat1.id) from SetStatus stat1 where wo.id=stat1.objectId and stat1.objectType='WorkOrder') and stat.objectType='WorkOrder')";
				paramList.add(criteriaMap.get("STATUS"));
				}
				else if(!criteriaMap.get("STATUS").equals("-1") && !Arrays.asList(setStat.split(",")).contains(criteriaMap.get("STATUS")))
				{
				dynQuery = dynQuery + " and wo.state.value = ?";
				paramList.add(criteriaMap.get("STATUS"));
				}
			}
			if(criteriaMap.get("FROM_DATE") != null && criteriaMap.get("TO_DATE")==null) {
				dynQuery = dynQuery + " and wo.workOrderDate >= ? ";
				paramList.add(criteriaMap.get("FROM_DATE"));

			}else if(criteriaMap.get("TO_DATE") != null && criteriaMap.get("FROM_DATE")==null) {
				dynQuery = dynQuery + " and wo.workOrderDate <= ? ";
				paramList.add(criteriaMap.get("TO_DATE"));
			}else if(criteriaMap.get("FROM_DATE") != null && criteriaMap.get("TO_DATE")!=null) {
				dynQuery = dynQuery + " and wo.workOrderDate between ? and ? ";
				paramList.add(criteriaMap.get("FROM_DATE")); 
				paramList.add(criteriaMap.get("TO_DATE"));
			}
			if(criteriaMap.get("WORKORDER_NO") != null){
				dynQuery = dynQuery + " and UPPER(wo.workOrderNumber) like '%"+criteriaMap.get("WORKORDER_NO").toString().trim().toUpperCase()+"%'";
			}
			if(criteriaMap.get("ESTIMATE_NO") != null){
				dynQuery = dynQuery + " and wo.id in (select distinct woe.workOrder.id from WorkOrderEstimate woe where " +
			"UPPER(woe.estimate.estimateNumber) like '%"+criteriaMap.get("ESTIMATE_NO").toString().trim().toUpperCase()+"%')";
			}
			if(criteriaMap.get("CONTRACTOR_ID") != null){
				dynQuery = dynQuery + " and wo.contractor.id = ? ";
				paramList.add(criteriaMap.get("CONTRACTOR_ID"));
			}
			if(criteriaMap.get("DEPT_ID") != null){
				dynQuery = dynQuery + " and wo.id in (select we.workOrder.id from WorkOrderEstimate we where we.workOrder.id=wo.id and " +
						" we.estimate.executingDepartment.id = ?) ";
				paramList.add(criteriaMap.get("DEPT_ID"));
			}
			
			dynQuery = dynQuery + " and workOrderEstimate.id not in (select distinct mbh.workOrderEstimate.id " +
					"from MBHeader mbh left join mbh.mbBills mbBills where " +
					" mbh.state.previous.value = ? and  mbBills.egBillregister.billstatus = ? and " +
					" mbBills.egBillregister.billtype=?)";
			paramList.add(MBHeader.MeasurementBookStatus.APPROVED.toString());	
			paramList.add(MBHeader.MeasurementBookStatus.APPROVED.toString());	
			paramList.add(getFinalBillTypeConfigValue());  
	
			queryString="select distinct wo "+	dynQuery;
			countQuery = "select distinct count(wo) " + dynQuery;
		}
		return new SearchQueryHQL(queryString, countQuery, paramList);
    }
	
	public String getFinalBillTypeConfigValue() {		
		return worksService.getWorksConfigValue("FinalBillType"); 
	}
	
	@ValidationErrorPage(value=NEW)
	public String save() {
		
		String actionName = parameters.get("actionName")[0]; 
		try{
			populateTestSheet();
			testSheetHeader.setWorkOrder(workOrder);
			
			if(actionName.equalsIgnoreCase(SAVE_ACTION)){
				testSheetHeader.setEgwStatus(commonsService.getStatusByModuleAndCode("TestSheetHeader","NEW")); 
				if(id ==null){
					Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
					testSheetHeaderService.setTestSheetNumber(testSheetHeader);
					testSheetHeader = (TestSheetHeader) workflowService.start(testSheetHeader, pos, "Test Sheet created.");
				}
				testSheetHeaderService.persist(testSheetHeader);
				messageKey="testSheet."+actionName;
				addActionMessage(getText(messageKey,"The Test Sheet was saved successfully "));
				getDesignation(testSheetHeader);
				populateTestNames();
			}
			else{  
				if(id ==null){
					testSheetHeader.setEgwStatus(commonsService.getStatusByModuleAndCode("TestSheetHeader","NEW")); 
					Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
					testSheetHeaderService.setTestSheetNumber(testSheetHeader);
					testSheetHeader = (TestSheetHeader) workflowService.start(testSheetHeader, pos, "Test Sheet created.");
				}
				workflowService.transition(actionName, testSheetHeader, approverComments);
				testSheetHeaderService.persist(testSheetHeader);
				messageKey="testSheet."+actionName;
				getDesignation(testSheetHeader);
			}
		}
		catch (ValidationException exception) {
			LOGGER.error("Error in TestSheet save--"+exception.getStackTrace());
			exception.printStackTrace(); 
			throw exception;
		}
			
		return SAVE_ACTION.equalsIgnoreCase(actionName)?EDIT:SUCCESS;
	}
	
	public void getDesignation(TestSheetHeader testSheetHeader){
		/* start for customizing workflow message display */
		if(testSheetHeader.getCurrentState()!= null 
				&& !"NEW".equalsIgnoreCase(testSheetHeader.getCurrentState().getValue())) {
			String result = worksService.getEmpNameDesignation(testSheetHeader.getState().getOwner(),testSheetHeader.getState().getCreatedDate());
			if(result != null && !"@".equalsIgnoreCase(result)) {
				String empName = result.substring(0,result.lastIndexOf('@'));
				String designation =result.substring(result.lastIndexOf('@')+1,result.length());
				setEmployeeName(empName);
				setDesignation(designation);
			}
		}
		/* end */	
	}
	
	 protected void populateTestSheet() { 
		 testSheetHeader.getTestSheetDetails().clear();
		 for(TestSheetDetails tsDtls: actionTestSheetDetails) {
			    tsDtls.setTestMaster((TestMaster)getPersistenceService().find("from TestMaster where id = ?",tsDtls.getTestMaster().getId()));
				testSheetHeader.addTestSheetDetails(tsDtls);
		 } 
	 }
	 
	 @ValidationErrorPage(value=SEARCH_WO)
		public String searchTestSheet(){
			return SEARCH_WO;
		}
	 
	
	public String getApprovedValue() {
		return worksService.getWorksConfigValue("WORKS_PACKAGE_STATUS");
	}
	
	@Override
	public String getPendingActions() {  
		return testSheetHeader==null?"":(testSheetHeader.getCurrentState()==null?"":testSheetHeader.getCurrentState().getNextAction());
	}

	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public String getEstimateNumber() {
		return estimateNumber;
	}

	public void setEstimateNumber(String estimateNumber) {
		this.estimateNumber = estimateNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public String getWorkOrderNumber() {
		return workOrderNumber;
	}

	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
	}

	public List<WorkOrder> getWorkOrderList() {
		return workOrderList;
	}

	public void setWorkOrderList(List<WorkOrder> workOrderList) {
		this.workOrderList = workOrderList;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public Long getContractorId() {
		return contractorId;
	}

	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}

	public Long getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(Long workOrderId) {
		this.workOrderId = workOrderId;
	}

	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}

	public String getSourcePage() {
		return sourcePage;
	}

	public void setSourcePage(String sourcePage) {
		this.sourcePage = sourcePage;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TestSheetHeader getTestSheetHeader() {
		return testSheetHeader;
	}

	public void setTestSheetHeader(TestSheetHeader testSheetHeader) {
		this.testSheetHeader = testSheetHeader;
	}

	public List<TestSheetDetails> getActionTestSheetDetails() {
		return actionTestSheetDetails;
	}

	public void setActionTestSheetDetails(List<TestSheetDetails> actionTestSheetDetails) {
		this.actionTestSheetDetails = actionTestSheetDetails;
	}

	public void setTestSheetHeaderService(
			TestSheetHeaderService testSheetHeaderService) {
		this.testSheetHeaderService = testSheetHeaderService;
	}

	public String gettSheetNumber() {
		return tSheetNumber;
	}

	public void settSheetNumber(String tSheetNumber) {
		this.tSheetNumber = tSheetNumber;
	}

	public String getWoNumber() {
		return woNumber;
	}

	public void setWoNumber(String woNumber) {
		this.woNumber = woNumber;
	}

	public void setContractorService(
			PersistenceService<Contractor, Long> contractorService) {
		this.contractorService = contractorService;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	} 
	
	public void setTestSheetWorkFlowService(WorkflowService<TestSheetHeader> workflowService) {
		this.workflowService = workflowService;
	}

}
