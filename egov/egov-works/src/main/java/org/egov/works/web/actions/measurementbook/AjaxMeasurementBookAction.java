package org.egov.works.web.actions.measurementbook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.script.ScriptContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.services.ScriptService;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.dao.DesignationMasterDAO;
import org.egov.pims.model.Assignment;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.services.MeasurementBookService;
import org.egov.works.services.WorksService;
import org.springframework.beans.factory.annotation.Autowired;

public class AjaxMeasurementBookAction extends BaseFormAction{
	private static final Logger logger = Logger.getLogger(AjaxMeasurementBookAction.class);
	private static final String USERS_IN_DEPT = "usersInDept";
	private static final String DESIGN_FOR_EMP = "designForEmp";
	private static final String WORK_ORDER_DETAILS = "workOrderDetails";
	private static final String ACTIVITY_DETAILS = "activityDetails";
	private static final String WORKFLOW_USER_LIST = "workflowUsers";
	private static final String WORKFLOW_DESIG_LIST = "workflowDesignations";
	private static final String CHANGE_QUANTITY  = "CHANGE_QUANTITY";
	
	private MeasurementBookService measurementBookService;
	@Autowired
        private EmployeeService employeeService;
	private Assignment assignment;
	private Integer empID;
	private Long executingDepartment;
	private List usersInExecutingDepartment;
	private WorkOrder workOrder;
	private String workOrderNumber;
	private Long woActivityId;
	private Long mbHeaderId;
	private WorkOrderActivity workOrderActivity;
	private Double prevCulmEntry;
	private Double totalEstQuantity;
	
	//-----------------------Manual Workflow ----------------------------
	private EisUtilService eisService;
	private Integer departmentId; 
	private Integer designationId; 
	private String scriptName;
	private String stateName;
	private Long modelId;
	private String modelType;
	private Integer wardId;
	private List workflowKDesigList;
	private List workflowUsers;
	private PersonalInformationService personalInformationService;
	private WorksService worksService;
	private String activityRemarks;
	//-------------------------------------------------------------------
	
	private String query = "";
	private List<AbstractEstimate> estimateList = new LinkedList<AbstractEstimate>();
	private List<WorkOrder> workOrderList = new LinkedList<WorkOrder>();
	private List<MBHeader> mbHeaderList = new LinkedList<MBHeader>();
	private Long estId;
	private Date latestMBDate;
	private Long woId;
	@Autowired
        private ScriptService scriptService;
	
	public Object getModel() {
		return null;
	}
	
	public String designationForUser(){
		try {
			assignment = employeeService.getLatestAssignmentForEmployee(empID);
		} catch (Exception e) {
			throw new EGOVRuntimeException("user.find.error", e);
		}
		return DESIGN_FOR_EMP;
	}
	
	public String usersInExecutingDepartment() {
		try {
			HashMap<String,Object> criteriaParams =new HashMap<String,Object>();
			criteriaParams.put("departmentId", executingDepartment);
			if(executingDepartment==null || executingDepartment==-1)
				usersInExecutingDepartment=Collections.EMPTY_LIST;
			else
				usersInExecutingDepartment=personalInformationService.getListOfEmployeeViewBasedOnCriteria(criteriaParams, -1, -1);
		} catch (Exception e) {
			throw new EGOVRuntimeException("user.find.error", e);
		}
		logger.info("Success ajax call to 'usersInExecutingDepartment' ----------------------------------------------------------");
		return USERS_IN_DEPT;
	}
	
	public String workOrderDetails() {
		try {
			workOrder = (WorkOrder) persistenceService.find("from WorkOrder where workOrderNumber=?",workOrderNumber);
			if(workOrder!=null){
				HashMap<String,Object> criteriaParams =new HashMap<String,Object>();
				criteriaParams.put("departmentId", executingDepartment);
				criteriaParams.put("isPrimary", "Y");
				if(executingDepartment==null || executingDepartment==-1)
					usersInExecutingDepartment=Collections.EMPTY_LIST;
				else
					usersInExecutingDepartment=personalInformationService.getListOfEmployeeViewBasedOnCriteria(criteriaParams, -1, -1);
			}
		} catch (Exception e) {
			throw new EGOVRuntimeException("workorder.find.error", e);
		}
		logger.info("Success ajax call to 'workOrderDetails' ----------------------------------------------------------");
		return WORK_ORDER_DETAILS;
	}
	
	public String activityDetails() {
		prevCulmEntry = null;
		activityRemarks = "";
		try{
			workOrderActivity = (WorkOrderActivity) persistenceService.find("from WorkOrderActivity where id=?",woActivityId);
			prevCulmEntry = measurementBookService.prevCumulativeQuantityIncludingCQ(woActivityId,mbHeaderId,workOrderActivity.getActivity().getId(),workOrderActivity.getWorkOrderEstimate().getWorkOrder());
			if(modelType!=null && modelType.equalsIgnoreCase("MB"))
				totalEstQuantity=measurementBookService.totalEstimatedQuantity(woActivityId,null,workOrderActivity.getActivity().getId(),workOrderActivity.getWorkOrderEstimate().getWorkOrder()); //This considers work order activities where the associated Est/REs are approved
			else
				totalEstQuantity=measurementBookService.totalEstimatedQuantityForRE(woActivityId,null,workOrderActivity.getActivity().getId(), workOrderActivity.getWorkOrderEstimate().getWorkOrder()); //This considers work order activities where the associated Est/REs are not cancelled
			WorkOrderActivity  revWorkOrderActivity = (WorkOrderActivity) persistenceService.find("from WorkOrderActivity where activity.parent.id=? and workOrderEstimate.estimate.egwStatus.code !='CANCELLED'  ",workOrderActivity.getActivity().getId());
			if(revWorkOrderActivity!=null)
				activityRemarks = CHANGE_QUANTITY;
		} 
		catch (Exception e) {
			throw new EGOVRuntimeException("activity.find.error", e);
		}
		
		return ACTIVITY_DETAILS;
	}
	
	public String getWorkFlowUsers() {
		if(designationId!=-1){
			HashMap<String,Object> paramMap = new HashMap<String, Object>();
			if(departmentId!=null && departmentId!=-1)
				paramMap.put("departmentId",departmentId.toString());
			if(wardId!=null && wardId!=-1)
				paramMap.put("boundaryId",wardId.toString());
			
			paramMap.put("designationId", designationId.toString());
			List roleList=worksService.getWorksRoles();	
			if(roleList!=null)
				paramMap.put("roleList", roleList);
			workflowUsers = eisService.getEmployeeInfoList(paramMap); 
		}
		return WORKFLOW_USER_LIST;
	}
	
	public  String getDesgByDeptAndType() {
		workflowKDesigList=new ArrayList<DesignationMaster>();
		String departmentName="";
		Department department=null;
		if(departmentId!=-1) {
			department =(Department)getPersistenceService().find("from DepartmentImpl where id=?", departmentId);
			departmentName=department.getName();
		}		
		DesignationMaster designation=null;
		MBHeader mbHeader = null;
		if(modelId!=null) {
			mbHeader = (MBHeader)getPersistenceService().find("from MBHeader where id=?", modelId);
		}
		ScriptContext scriptContext = ScriptService.createContext("state",stateName,"department",departmentName,"wfItem",mbHeader);
		List<String> list = (List<String>)scriptService.executeScript(scriptName, scriptContext);
		//Script validScript = (Script) persistenceService.findAllByNamedQuery(Script.BY_NAME,scriptName).get(0);
		//List<String> list = (List<String>) validScript.eval(Script.createContext("state",stateName,"department",departmentName,"wfItem",mbHeader));		
		
		for (String desgName : list) {
			if(desgName.trim().length()!=0){
				try {
					designation =new DesignationMasterDAO().getDesignationByDesignationName(desgName);
					workflowKDesigList.add(designation);
				}
				catch (NoSuchObjectException e) {
					logger.error(e);
				}
			}
		}
		return WORKFLOW_DESIG_LIST;
	}
	
	public String searchEstimateNumber(){
		String strquery="";
		ArrayList<Object> params=new ArrayList<Object>();
		if(!StringUtils.isEmpty(query)) {
			strquery="select woe.estimate from WorkOrderEstimate woe where woe.workOrder.parent is null and woe.estimate.estimateNumber like '%'||?||'%' " +
			" and woe.id in (select distinct mbh.workOrderEstimate.id from MBHeader mbh where mbh.egwStatus.code <> ? )";
			params.add(query.toUpperCase());
			params.add("NEW");
			estimateList = getPersistenceService().findAllBy(strquery,params.toArray());
		}
		return "estimateNoSearchResults";
	}
	
	public String searchWorkOrderNumber(){
		String strquery="";
		ArrayList<Object> params=new ArrayList<Object>();
		if(!StringUtils.isEmpty(query)) {
			strquery="select distinct mbh.workOrder from MBHeader mbh where mbh.workOrder.parent is null and mbh.workOrder.workOrderNumber like '%'||?||'%' " +
			"and mbh.egwStatus.code <> ? ";
			params.add(query.toUpperCase());
			params.add("NEW");		
			workOrderList = getPersistenceService().findAllBy(strquery,params.toArray());
		}
		return "workOrderNoSearchResults";
	}
	
	public String searchMbRefNo(){
		String strquery="";
		ArrayList<Object> params=new ArrayList<Object>();
		if(!StringUtils.isEmpty(query)) {
			strquery=" from MBHeader mbh where mbh.mbRefNo like '%'||?||'%' and mbh.egwStatus.code <> ? ";
			params.add(query.toUpperCase());
			params.add("NEW");		
			mbHeaderList = getPersistenceService().findAllBy(strquery,params.toArray());
		}
		return "mbRefNoSearchResults";
	}
	
	public String getLatestMBDateforSelectedEstimate(){
		latestMBDate = measurementBookService.getLastMBCreatedDate(woId ,estId);
		return "mblatestDateResult";
	}
	
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	public void setEmpID(Integer empID) {
		this.empID = empID;
	}
	
	public List getUsersInExecutingDepartment() {
		return usersInExecutingDepartment;
	}
	
	public void setExecutingDepartment(Long executingDepartment) {
		this.executingDepartment = executingDepartment;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
	}

	public void setWoActivityId(Long woActivityId) {
		this.woActivityId = woActivityId;
	}

	public WorkOrderActivity getWorkOrderActivity() {
		return workOrderActivity;
	}

	public Double getPrevCulmEntry() {
		return prevCulmEntry;
	}
	
	public void setMbHeaderId(Long mbHeaderId) {
		this.mbHeaderId = mbHeaderId;
	}

	public void setMeasurementBookService(
			MeasurementBookService measurementBookService) {
		this.measurementBookService = measurementBookService;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

	public List getWorkflowKDesigList() {
		return workflowKDesigList;
	}

	public List getWorkflowUsers() {
		return workflowUsers;
	}
	
	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<AbstractEstimate> getEstimateList() {
		return estimateList;
	}

	public void setEstimateList(List<AbstractEstimate> estimateList) {
		this.estimateList = estimateList;
	}

	public List<WorkOrder> getWorkOrderList() {
		return workOrderList;
	}

	public void setWorkOrderList(List<WorkOrder> workOrderList) {
		this.workOrderList = workOrderList;
	}

	public List<MBHeader> getMbHeaderList() {
		return mbHeaderList;
	}

	public void setMbHeaderList(List<MBHeader> mbHeaderList) {
		this.mbHeaderList = mbHeaderList;
	}

	public Double getTotalEstQuantity() {
		return totalEstQuantity;
	}

	public String getActivityRemarks() {
		return activityRemarks;
	}

	public Long getEstId() {
		return estId;
	}

	public void setEstId(Long estId) {
		this.estId = estId;
	}

	public Date getLatestMBDate() {
		return latestMBDate;
	}

	public void setLatestMBDate(Date latestMBDate) {
		this.latestMBDate = latestMBDate;
	}

	public Long getWoId() {
		return woId;
	}

	public void setWoId(Long woId) {
		this.woId = woId;
	}
}
