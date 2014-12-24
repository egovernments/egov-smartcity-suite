package org.egov.works.web.actions.securityDeposit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.EgChecklists;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.model.bills.EgBillregister;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.workflow.GenericWorkFlowAction;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.securityDeposit.ReturnSecurityDeposit;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.ReturnSecurityDepositService;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;



@ParentPackage("egov")
public class ReturnSecurityDepositAction extends GenericWorkFlowAction{
	private static final Logger logger = Logger.getLogger(ReturnSecurityDepositAction.class);
	private static final String SAVE_ACTION = "save";
	public final static String APPROVED="APPROVED";
	private static final String SD_COA_LIST = "sdcoaList";
	private static final String SDDEDUCTEDAMOUNT="SDdeductedAmount";
	private static final String RETURNSECURITYDEPO_MODULE_KEY = "ReturnSecurityDeposit";
	private static final String WORKS="Works";
	private static final String SEARCH="search";
	private static final String RSD_CHECKLIST_APPCONFIG = "Return-Security-Deposit-CheckList";
	private static final String DATE_FORMAT="dd-MMM-yyyy";
	
	private ReturnSecurityDeposit returnSecurityDeposit=new ReturnSecurityDeposit();
	private Long id;
	private Long workOrderId;
	private Long contractorId;
	private ReturnSecurityDepositService returnSecurityDepositService;
	private double sdDeductedAmount;
	
	private Long glcodeId;
	private WorkOrder workOrder;
	private WorkflowService<ReturnSecurityDeposit> returnSecurityDepositWorkflowService;
	private String messageKey;
	private WorksService worksService;
	private String designation;
	private String employeeName;
	private PersistenceService<EgBillregister, Long> egBillregisterService;
	private String sourcepage="";
	private CommonsService commonsService;
	
	private EmployeeService employeeService;
	private WorkflowService<ReturnSecurityDeposit> workflowService;
	private AbstractEstimateService abstractEstimateService;
	private String mode="";
	private String additionalRuleValue;
	private String departmentName;
	private double oldReturnSecurityDepositAmount;
	private List<MBHeader>  mbHeaderList= new LinkedList<MBHeader>();
	private List<AppConfigValues>  securityDepositChecklist= new LinkedList<AppConfigValues>();
	private List<String>  checklistValues= new LinkedList<String>(); 
	private String[]  checkListremarks;
	private WorkOrderService workOrderService;
	private Double totalWorkValue = 0d;
	private PersistenceService<EgChecklists, Long> checklistService;
	private Long[] appConfigValueId;
	private String[]  selectedchecklistValue;
	private Integer deptId;	
	
	private String estimateNumber;
	private Date fromDate;
	private Date toDate;
	
	@Override
	public StateAware getModel() {
		return returnSecurityDeposit;
	}
	public ReturnSecurityDepositAction(){
		addRelatedEntity("glcode", CChartOfAccounts.class);
	}
	public void prepare(){
		super.prepare();
		setupDropdownDataExcluding("glcode");
		addDropdownData("executingDepartmentList",getPersistenceService().findAllBy("from DepartmentImpl order by upper(deptName)"));
		if(id==null && sourcepage.equalsIgnoreCase(SEARCH))
			return;
		if (id != null) {
			returnSecurityDeposit=returnSecurityDepositService.findById(id, false);
			workOrder=returnSecurityDeposit.getWorkOrder();
			oldReturnSecurityDepositAmount=returnSecurityDeposit.getReturnSecurityDepositAmount();
			
			checklistValues.add("No");
			checklistValues.add("Yes");
			
			List<AppConfigValues>  acvObj= new LinkedList<AppConfigValues>();
			acvObj=worksService.getAppConfigValue(WORKS, RSD_CHECKLIST_APPCONFIG);
			Integer appConfigId=null;
			if(acvObj!=null && acvObj.size()!=0){
				appConfigId=acvObj.get(0).getKey().getId();
			}

			List<EgChecklists> list = persistenceService.findAllByNamedQuery("checklist.by.appconfigid.and.objectid", id, appConfigId);
			if(!list.isEmpty() && list.size()!=0){
				int i=0;
				selectedchecklistValue =new String[list.size()];
				checkListremarks =new String[list.size()];
				for(EgChecklists egChecklists: list)
				{
					securityDepositChecklist.add(egChecklists.getAppconfigvalue());
					selectedchecklistValue[i]=egChecklists.getChecklistvalue();
					checkListremarks[i]=egChecklists.getRemarks(); 
					i++;
				}
			}
			else{
				try{
					securityDepositChecklist= worksService.getAppConfigValue(WORKS,RSD_CHECKLIST_APPCONFIG);
				} catch (Exception e) {
					logger.error("Error-- ReturnSecurityDepositAction ------"+e.getMessage());
				}
			}
		}
		else{
			workOrder=(WorkOrder) persistenceService.find("from WorkOrder where id=?",workOrderId);
			Contractor contractor=workOrder.getContractor();
			returnSecurityDeposit.setContractor(contractor);
			returnSecurityDeposit.setWorkOrder(workOrder);
			departmentName=workOrder.getWorkOrderEstimates().get(0).getEstimate().getExecutingDepartment().getDeptName();
			
			checklistValues.add("No");
			checklistValues.add("Yes");
			
			try{
				securityDepositChecklist= worksService.getAppConfigValue(WORKS,RSD_CHECKLIST_APPCONFIG);
			} catch (Exception e) {
				logger.error("Error-- ReturnSecurityDepositAction ------"+e.getMessage());
			}
		}
		PersonalInformation emp = employeeService.getEmployeeforPosition(workOrder.getCurrentState().getOwner());
		if(emp!=null)
			workOrder.setOwner(emp.getEmployeeName());
		try {
			addDropdownData(SD_COA_LIST,returnSecurityDepositService.getSDCOAList());
		}catch (Exception e) {
			addFieldError("glcode", "Unable to load coa list");
		}
		if(!getWorkFlowDepartment().equals("")){
			departmentName=getWorkFlowDepartment();
		}
		mbHeaderList = (List<MBHeader>) persistenceService.findAllBy("select distinct mbh from MBHeader mbh where mbh.state.previous.value = 'APPROVED' " +
						"and mbh.workOrder.id = ? and mbh.workOrderEstimate.id = ?", workOrder.getId(), workOrder.getWorkOrderEstimates().get(0).getId());
		
		BigDecimal workOrderAmount =  workOrder.getTotalWorkOrderAmount();
		totalWorkValue = (workOrderAmount==null)?0d:workOrderAmount.doubleValue();
		totalWorkValue += workOrderService.getRevisionEstimateWOAmount(workOrder).doubleValue();
		
	}
	
	public String newform(){
		return NEW;
	}
	
	public String viewSearch(){
		return "viewSearch";
	}
	
	public String searchList(){
		search();
		setOwnerAndActions(searchResult.getList());
		return "viewSearch";
	}
	
	private void setOwnerAndActions(List<ReturnSecurityDeposit>  rsdList) {
		for(ReturnSecurityDeposit secDeposit:rsdList)
		{
			String actions = worksService.getWorksConfigValue("RETURNSECURITYDEPOSIT_SHOW_ACTIONS");
			if(actions!=null && ! actions.equals(""))
				secDeposit.getReturnSecurityDepositActions().addAll(Arrays.asList(actions.split(",")));
			PersonalInformation emp = employeeService.getEmployeeforPosition(secDeposit.getCurrentState().getOwner());
			if(emp!=null)
				secDeposit.setOwner(emp.getEmployeeName());
		}
	}
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		String query = " from ReturnSecurityDeposit rsd  where rsd.id is not null ";
		WorkOrder workOrder = returnSecurityDeposit.getWorkOrder();
		List<Object> paramList = new ArrayList<Object>();
		if( workOrder !=null )
		{
			if(workOrder.getContractor()!= null && workOrder.getContractor().getId()!=null && workOrder.getContractor().getId()!=-1)
			{
				query =query +  " and rsd.workOrder.contractor.id = ? ";
				paramList.add(workOrder.getContractor().getId());
			}
			if(workOrder.getWorkOrderNumber()!=null && !workOrder.getWorkOrderNumber().equals(""))
			{
				query =query +  " and UPPER(rsd.workOrder.workOrderNumber) like '%"+workOrder.getWorkOrderNumber().trim().toUpperCase()+"%'";
			}
		}
		if(returnSecurityDeposit.getEgwStatus()!=null 
				&& returnSecurityDeposit.getEgwStatus().getCode()!=null
				&& !returnSecurityDeposit.getEgwStatus().getCode().equals("-1") 
				&& !returnSecurityDeposit.getEgwStatus().getCode().equals("") )
		{
			query =query +  " and rsd.egwStatus.code = ? ";
			paramList.add(returnSecurityDeposit.getEgwStatus().getCode());
		}
		if(fromDate!=null)
		{
			query =query +  " and  trunc(rsd.createdDate) >= '"+DateUtils.getFormattedDate(fromDate,DATE_FORMAT )+"' ";
		}
		if(toDate!=null)
		{
			query = query +  " and trunc(rsd.createdDate)  <= '"+DateUtils.getFormattedDate(toDate,DATE_FORMAT )+"' ";
		}
		if(deptId!=null && deptId!=-1)
		{
			query = query + " and rsd.workOrder.id in (select we.workOrder.id from WorkOrderEstimate we where we.workOrder.id=rsd.workOrder.id and " +
					" we.estimate.executingDepartment.id = ?) ";
			paramList.add(deptId);
		}
		if(estimateNumber!=null && !estimateNumber.equalsIgnoreCase(""))
		{
			query = query + "  and rsd.workOrder.id in (select distinct woe.workOrder.id from WorkOrderEstimate woe where " +
					"UPPER(woe.estimate.estimateNumber) like '%"+estimateNumber.trim().toUpperCase()+"%')";
		}
		String actions = worksService.getWorksConfigValue("RETURNSECURITYDEPOSIT_SHOW_ACTIONS");
		workOrder.getWorkOrderActions().addAll(Arrays.asList(actions.split(",")));
		
		setPageSize(WorksConstants.PAGE_SIZE);
		String workOrderSearchQuery="select distinct rsd "+	query;
		String countQuery = "select distinct count(rsd) " + query;
		return new SearchQueryHQL(workOrderSearchQuery, countQuery, paramList);
		
	}
	public String findSDdeductedAmountAjax(){
		sdDeductedAmount=returnSecurityDepositService.getTotalSDAmountDeducted(workOrder,glcodeId,true);
		return SDDEDUCTEDAMOUNT;
	}
	public String edit(){
		return EDIT;
	}
	
	public String save() throws Exception{
		String actionName = parameters.get("actionName")[0]; 
		/*
		returnSecurityDeposit=returnSecurityDepositService.persist(returnSecurityDeposit);
		workOrder.setSdDeducted(returnSecurityDepositService.getTotalSDAmountDeducted(workOrder,returnSecurityDeposit.getGlcode().getId(),false));
		workOrder.setSdRefunded(workOrder.getSdRefunded()+returnSecurityDeposit.getReturnSecurityDepositAmount());
		returnSecurityDeposit=(ReturnSecurityDeposit)returnSecurityDepositWorkflowService.transition(actionName,returnSecurityDeposit,returnSecurityDeposit.getWorkflowapproverComments());
		if(returnSecurityDeposit.getCurrentState()!=null && returnSecurityDeposit.getCurrentState().getValue()!=null
				&&  "END".equalsIgnoreCase(returnSecurityDeposit.getCurrentState().getValue()) && APPROVED.equalsIgnoreCase(returnSecurityDeposit.getCurrentState().getPrevious().getValue())){
			EgBillregister expenseBill = returnSecurityDepositService.createExpenseBill(returnSecurityDeposit);
			expenseBill=egBillregisterService.persist(expenseBill);
			egBillregisterService.getSession().flush();
			egBillregisterService.getSession().refresh(expenseBill);
			expenseBill.getEgBillregistermis().setSourcePath("/egworks/securityDeposit/returnSecurityDeposit!edit.action?id="+returnSecurityDeposit.getId()+"&sourcepage=search");
			messageKey="returnSecurityDeposit.approved";
		}
		else{
			messageKey="returnSecurityDeposit.save.success";
		}	
		addActionMessage(getText(messageKey,messageKey));
		getDesignation(returnSecurityDeposit);
		return SAVE_ACTION.equals(actionName)?EDIT:SUCCESS;
		*/
		
		//String deptName=getWorkFlowDepartment();
		returnSecurityDeposit.setAdditionalWfRule(getAdditionalRule());
		returnSecurityDeposit.setAmountWfRule(getAmountRule());
		String curStatus;
		if(returnSecurityDeposit.getCurrentState()!=null){
			curStatus=returnSecurityDeposit.getCurrentState().getValue(); 
		}
		else
			curStatus="NEW";
		if((actionName.equalsIgnoreCase("Forward")||actionName.equalsIgnoreCase("Approve")) && customizedWorkFlowService.getWfMatrix(getModel().getStateType(), getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),curStatus, getPendingActions())==null){
			throw new ValidationException(Arrays.asList(new ValidationError("returnSecurityDeposit.workflow.notdefined",getText("returnSecurityDeposit.workflow.notdefined",new String[]{getWorkFlowDepartment()}))));
		} 
	
		if(actionName.equalsIgnoreCase("save")){
			returnSecurityDeposit.setEgwStatus(commonsService.getStatusByModuleAndCode(RETURNSECURITYDEPO_MODULE_KEY,"NEW"));
			if(id ==null){
				Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
				returnSecurityDeposit = (ReturnSecurityDeposit)workflowService.start(returnSecurityDeposit,pos, "Return Security Deposit Created.");
			}
			messageKey="rsd."+actionName;
			addActionMessage(getText(messageKey,"The Return Security Deposit was saved successfully"));
			returnSecurityDeposit = returnSecurityDepositService.persist(returnSecurityDeposit);
			getDesignation(returnSecurityDeposit);
			generateCheckList();
		}
		else if(mode!=null && !mode.equals("edit")){
			
			if(actionName.equalsIgnoreCase("Forward")  || actionName.equalsIgnoreCase("Approve") )
				validateDate();
			if(id==null){
				returnSecurityDeposit.setEgwStatus(commonsService.getStatusByModuleAndCode(RETURNSECURITYDEPO_MODULE_KEY,"NEW"));
				Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
				returnSecurityDeposit = (ReturnSecurityDeposit) workflowService.start(returnSecurityDeposit, pos, "Return Security Deposit created.");
			}
			workflowService.transition(actionName, returnSecurityDeposit, approverComments);
			returnSecurityDeposit = returnSecurityDepositService.persist(returnSecurityDeposit);
			if(actionName.equalsIgnoreCase("Forward") || actionName.equalsIgnoreCase("Approve") )
				generateCheckList();
			messageKey="rsd."+actionName;
			getDesignation(returnSecurityDeposit);
		}
		workOrder.setSdDeducted(returnSecurityDepositService.getTotalSDAmountDeducted(workOrder,returnSecurityDeposit.getGlcode().getId(),false));
		if(actionName.equalsIgnoreCase("Cancel"))
			workOrder.setSdRefunded(workOrder.getSdRefunded()-returnSecurityDeposit.getReturnSecurityDepositAmount());
		else
			workOrder.setSdRefunded(workOrder.getSdRefunded()-oldReturnSecurityDepositAmount+returnSecurityDeposit.getReturnSecurityDepositAmount());
		
		if(actionName.equalsIgnoreCase("Approve")){
			EgBillregister expenseBill = returnSecurityDepositService.createExpenseBill(returnSecurityDeposit);
			expenseBill=egBillregisterService.persist(expenseBill);
			egBillregisterService.getSession().flush();
			egBillregisterService.getSession().refresh(expenseBill);
			expenseBill.getEgBillregistermis().setSourcePath("/egworks/securityDeposit/returnSecurityDeposit!edit.action?id="+returnSecurityDeposit.getId()+"&sourcepage=search");
			returnSecurityDeposit.setEgBillregister(expenseBill);
			messageKey=getText("rsd.generatedBill.No",new String[]{expenseBill.getBillnumber()});
				//"rsd.generatedBill.No"+expenseBill.getBillnumber();
		}
		
		return SAVE_ACTION.equalsIgnoreCase(actionName)?EDIT:SUCCESS;
	}
	
	private void generateCheckList() {
		if(appConfigValueId!=null && selectedchecklistValue!=null && appConfigValueId.length>0 && selectedchecklistValue.length>0)
		{
			deleteChecklist(appConfigValueId[0],returnSecurityDeposit);
			for(int i=0;i<appConfigValueId.length;i++)
			{
				if(appConfigValueId[i]!=null && !selectedchecklistValue[i].equals("-1")){
					EgChecklists checklist = new EgChecklists();
					checklist.setAppconfigvalue((AppConfigValues)getPersistenceService().find("from AppConfigValues where id=?", Integer.valueOf(appConfigValueId[i].toString())));
					checklist.setChecklistvalue(selectedchecklistValue[i]);
					checklist.setObjectid(returnSecurityDeposit.getId());
					checklist.setRemarks(StringEscapeUtils.unescapeHtml(checkListremarks[i]));
					checklistService.persist(checklist);
				}
			}
		}
		
	}
	
	private void validateDate() {
		Calendar cal = Calendar.getInstance();
		Calendar todaysCalendar = Calendar.getInstance();
		cal.setTime(workOrder.getWorkOrderEstimates().get(0).getWorkCompletionDate());
		double defectLP;
		int days;
		defectLP = workOrder.getDefectLiabilityPeriod();
		if(defectLP==0)
		{
			defectLP = returnSecurityDeposit.getDefectLiabilityPeriod();
		}
		days = (int) (30*defectLP);
		cal.add(Calendar.DAY_OF_YEAR, days);
		clearMessages();
		List<ValidationError> errors=new ArrayList<ValidationError>();
		errors.add(new ValidationError("exp","Cannot return security period as liability period is not over"));
		if(todaysCalendar.compareTo(cal)<=0)
			throw new ValidationException(errors);
	}
	
	protected String getAdditionalRule() {
		//String deptName=getWorkFlowDepartment();
		if(departmentName.equals("Garden"))
			additionalRuleValue=returnSecurityDeposit.getWorkOrder().getWorkOrderEstimates().get(0).getEstimate().getParentCategory().getCode().equals("CW")?"CivilWorks":returnSecurityDeposit.getWorkOrder().getWorkOrderEstimates().get(0).getEstimate().getParentCategory().getCode().equals("HW")?"HorticultureWork":"";
		return additionalRuleValue;
	}
	
	public void getDesignation(ReturnSecurityDeposit returnSecurityDeposit){
		/* start for customizing workflow message display */
		if(returnSecurityDeposit.getCurrentState()!= null 
				&& !"NEW".equalsIgnoreCase(returnSecurityDeposit.getCurrentState().getValue())) {
			String result = worksService.getEmpNameDesignation(returnSecurityDeposit.getState().getOwner(),returnSecurityDeposit.getState().getCreatedDate());
			if(result != null && !"@".equalsIgnoreCase(result)) {
				String empName = result.substring(0,result.lastIndexOf('@'));
				String designation =result.substring(result.lastIndexOf('@')+1,result.length());
				setEmployeeName(empName);
				setDesignation(designation);
			}
		}
		/* end */	
	}
	
	public String cancel(){
		String actionName = parameters.get("actionName")[0]; 
		if(returnSecurityDeposit.getId()!=null){
			workflowService.transition(actionName, returnSecurityDeposit,approverComments);
			returnSecurityDeposit=returnSecurityDepositService.persist(returnSecurityDeposit);
		}
		messageKey="rateContract.Cancel";		
		return SUCCESS;
	} 
	
	protected void deleteChecklist(Long appConfValue, ReturnSecurityDeposit entity){
		if(appConfValue!=null && entity!=null){
			AppConfigValues acvEntity=(AppConfigValues)getPersistenceService().find("from AppConfigValues acv where acv.id=?", Integer.valueOf(appConfValue.intValue()));
			List<EgChecklists> list = persistenceService.findAllByNamedQuery("checklist.by.appconfigid.and.objectid", entity.getId(), acvEntity.getKey().getId());
			for(EgChecklists eclEntity: list){
				checklistService.delete(eclEntity); 
			}
		}
	}
	
	public List<EgwStatus> getSecurityDepositStatuses() {
		List<EgwStatus> rsdStatusList=commonsService.getStatusByModule(ReturnSecurityDeposit.class.getSimpleName());
		rsdStatusList.remove(commonsService.getStatusByModuleAndCode(ReturnSecurityDeposit.class.getSimpleName(), "NEW"));
		return rsdStatusList;
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
	
	public String getPendingActions()
	{
		return returnSecurityDeposit==null?"":
			(returnSecurityDeposit.getCurrentState()==null?"":returnSecurityDeposit.getCurrentState().getNextAction());
	}

	public String getWorkFlowDepartment(){
		return returnSecurityDeposit==null?"":(returnSecurityDeposit.getWorkOrder()==null?"":(returnSecurityDeposit.getWorkOrder().getWorkOrderEstimates()==null?"":(returnSecurityDeposit.getWorkOrder().getWorkOrderEstimates().get(0).getEstimate()==null?"":(returnSecurityDeposit.getWorkOrder().getWorkOrderEstimates().get(0).getEstimate().getExecutingDepartment()==null?"":returnSecurityDeposit.getWorkOrder().getWorkOrderEstimates().get(0).getEstimate().getExecutingDepartment().getDeptName()))));
	}
	
	
	protected BigDecimal getAmountRule() {
		return returnSecurityDeposit==null?null:(new BigDecimal(returnSecurityDeposit.getReturnSecurityDepositAmount())==null?null:(new BigDecimal(returnSecurityDeposit.getReturnSecurityDepositAmount())));
	}
	
	public ReturnSecurityDeposit getReturnSecurityDeposit() {
		return returnSecurityDeposit;
	}
	public void setReturnSecurityDeposit(ReturnSecurityDeposit returnSecurityDeposit) {
		this.returnSecurityDeposit = returnSecurityDeposit;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getWorkOrderId() {
		return workOrderId;
	}
	public void setWorkOrderId(Long workOrderId) {
		this.workOrderId = workOrderId;
	}
	public Long getContractorId() {
		return contractorId;
	}
	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}
	public ReturnSecurityDepositService getReturnSecurityDepositService() {
		return returnSecurityDepositService;
	}
	public void setReturnSecurityDepositService(
			ReturnSecurityDepositService returnSecurityDepositService) {
		this.returnSecurityDepositService = returnSecurityDepositService;
	}
	public Long getGlcodeId() {
		return glcodeId;
	}
	public void setGlcodeId(Long glcodeId) {
		this.glcodeId = glcodeId;
	}
	public double getSdDeductedAmount() {
		return sdDeductedAmount;
	}
	public void setSdDeductedAmount(double sdDeductedAmount) {
		this.sdDeductedAmount = sdDeductedAmount;
	}
	public WorkOrder getWorkOrder() {
		return workOrder;
	}
	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}
	public void setReturnSecurityDepositWorkflowService(
			WorkflowService<ReturnSecurityDeposit> returnSecurityDepositWorkflowService) {
		this.workflowService = returnSecurityDepositWorkflowService;
	}
	public String getMessageKey() {
		return messageKey;
	}
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}
	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}
	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public void setEgBillregisterService(
			PersistenceService<EgBillregister, Long> egBillregisterService) {
		this.egBillregisterService = egBillregisterService;
	}
	public String getSourcepage() {
		return sourcepage;
	}
	public void setSourcepage(String sourcepage) {
		this.sourcepage = sourcepage;
	}
	public CommonsService getCommonsService() {
		return commonsService;
	}
	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}
	public EmployeeService getEmployeeService() {
		return employeeService;
	}
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getAdditionalRuleValue() { 
		getAdditionalRule();
		return additionalRuleValue;
	}
	public void setAdditionalRuleValue(String additionalRuleValue) {
		this.additionalRuleValue = additionalRuleValue;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public double getOldReturnSecurityDepositAmount() {
		return oldReturnSecurityDepositAmount;
	}
	public void setOldReturnSecurityDepositAmount(
			double oldReturnSecurityDepositAmount) {
		this.oldReturnSecurityDepositAmount = oldReturnSecurityDepositAmount;
	}
	public List<AppConfigValues> getSecurityDepositChecklist() {
		return securityDepositChecklist;
	}
	public void setSecurityDepositChecklist(
			List<AppConfigValues> securityDepositChecklist) {
		this.securityDepositChecklist = securityDepositChecklist;
	}
	public List<MBHeader> getMbHeaderList() {
		return mbHeaderList;
	}
	public List<String> getChecklistValues() {
		return checklistValues;
	}
	public String[] getCheckListremarks() {
		return checkListremarks;
	}
	public void setChecklistValues(List<String> checklistValues) {
		this.checklistValues = checklistValues;
	}
	public void setCheckListremarks(String[] checkListremarks) {
		this.checkListremarks = checkListremarks;
	}
	public Double getTotalWorkValue() {
		return totalWorkValue;
	}
	public void setTotalWorkValue(Double totalWorkValue) {
		this.totalWorkValue = totalWorkValue;
	}
	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}
	public PersistenceService<EgChecklists, Long> getChecklistService() {
		return checklistService;
	}

	public void setChecklistService(
			PersistenceService<EgChecklists, Long> checklistService) { 
		this.checklistService = checklistService;
	}
	public Long[] getAppConfigValueId() {
		return appConfigValueId;
	}

	public void setAppConfigValueId(Long[] appConfigValueId) {
		this.appConfigValueId = appConfigValueId;
	}
	public String[] getSelectedchecklistValue() {
		return selectedchecklistValue;
	}

	public void setSelectedchecklistValue(String[] selectedchecklistValue) {
		this.selectedchecklistValue = selectedchecklistValue;
	}
	public Integer getDeptId() {
		return deptId;
	}
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
	public String getEstimateNumber() {
		return estimateNumber;
	}
	public void setEstimateNumber(String estimateNumber) {
		this.estimateNumber = estimateNumber;
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
	
}
