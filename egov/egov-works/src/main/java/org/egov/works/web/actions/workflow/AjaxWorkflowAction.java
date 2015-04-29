package org.egov.works.web.actions.workflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.script.ScriptContext;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.Functionary;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infstr.services.ScriptService;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.service.EisUtilService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.workflow.WorkFlow;
import org.egov.works.services.WorksService;
import org.springframework.beans.factory.annotation.Autowired;

public class AjaxWorkflowAction extends BaseFormAction {

	private WorkFlow workflow = new WorkFlow();
	private static final String WORKFLOW_USER_LIST = "workflowUsers";
	private static final String WORKFLOW_DESIG_LIST = "workflowDesignations";
	private Long objectId;
	private String scriptName;
	private List<DesignationMaster> workflowDesigList=new ArrayList<DesignationMaster>();
	private EisUtilService eisService;
	private WorksService worksService;
	@Autowired
        private ScriptService scriptService;
	@Autowired
	private DepartmentService departmentService;
	
	public Object getModel() {
		return workflow;
	}
	
	public void setModel(WorkFlow workflow) {
		this.workflow=workflow;
	}
	
	public String execute(){
		return SUCCESS;
	}

	
	public String getWorkFlowUsers() {
		return WORKFLOW_USER_LIST;
	}
	
	public List<EmployeeView> getApproverUserList() {
		List roleList;
		Integer funcId;
		if(workflow.getWorkflowDepartmentId()!=null && workflow.getWorkflowDepartmentId()!=-1 && 
				workflow.getWorkflowDesignationId()!=null && workflow.getWorkflowDesignationId()!=-1){
			HashMap<String,Object> paramMap = new HashMap<String, Object>();
			if(workflow.getWorkflowWardId()!=null && workflow.getWorkflowWardId()!=-1)
				paramMap.put("boundaryId",workflow.getWorkflowWardId().toString());
			paramMap.put("designationId", workflow.getWorkflowDesignationId().toString());
			if(workflow.getWorkflowDepartmentId()!=null && workflow.getWorkflowDepartmentId()!=-1)
				paramMap.put("departmentId",workflow.getWorkflowDepartmentId().toString());
			if(workflow.getWorkflowFunctionaryId()!=null && StringUtils.isNotBlank(workflow.getWorkflowFunctionaryId().toString()) && workflow.getWorkflowFunctionaryId()!=-1 )
				paramMap.put("functionaryId",workflow.getWorkflowFunctionaryId().toString());
			if(paramMap.get("functionaryId")!=null)
			{
				funcId = Integer.parseInt( (String) paramMap.get("functionaryId")); 
				Functionary func = (Functionary) persistenceService.find(" from  Functionary where id = ?",funcId);
				if(func!=null && func.getName().equalsIgnoreCase("UAC"))
				{	
					roleList=worksService.getWorksRoles();
					roleList.add("ContractorBill Approver");
				}
				else
					roleList=worksService.getWorksRoles();
			}
			else
				roleList=worksService.getWorksRoles();	
			if(roleList!=null)
				paramMap.put("roleList", roleList);
			return eisService.getEmployeeInfoList(paramMap); 
		}
		return Collections.emptyList();
	}
	
	public  String getDesgByDeptAndType() {
		String departmentName="";
		if(workflow.getWorkflowDepartmentId()!=null && workflow.getWorkflowDepartmentId()!=-1) {
			departmentName =(String)departmentService.getDepartmentById(workflow.getWorkflowDepartmentId()).getName();
		}
		 ScriptContext scriptContext = ScriptService.createContext("department",departmentName,"objectId",objectId,"genericService",getPersistenceService());
		 List<String> desglist = (List<String>)scriptService.executeScript("works.estimatenumber.generator", scriptContext);
		
	        /* List<Script> scriptList = persistenceService.findAllByNamedQuery(Script.BY_NAME,scriptName+".nextDesignation");
		if(!scriptList.isEmpty()){
			List<String> desglist = (List<String>) scriptList.get(0).eval(Script.createContext("department",departmentName,
					"objectId",objectId,"genericService",getPersistenceService()));	*/
			List<String> desgListUpper = new ArrayList<String>();
			for(String desgNames:desglist){
				desgListUpper.add(desgNames.toUpperCase());
			}
			workflowDesigList.addAll(getPersistenceService().findAllByNamedQuery("getDesignationForListOfDesgNames", desgListUpper));
		//}
		return WORKFLOW_DESIG_LIST;
	}


	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public List<DesignationMaster> getWorkflowDesigList() {
		return workflowDesigList;
	}

	public void setWorkflowDesigList(List<DesignationMaster> workflowDesigList) {
		this.workflowDesigList = workflowDesigList;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

}
