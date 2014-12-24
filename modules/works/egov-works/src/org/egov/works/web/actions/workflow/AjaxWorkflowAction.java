package org.egov.works.web.actions.workflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.egov.infstr.models.Script;
import org.egov.infstr.services.ScriptService;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.service.EisUtilService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.workflow.WorkFlow;
import org.egov.works.services.WorksService;

public class AjaxWorkflowAction extends BaseFormAction {

	private WorkFlow workflow = new WorkFlow();
	private static final String WORKFLOW_USER_LIST = "workflowUsers";
	private static final String WORKFLOW_DESIG_LIST = "workflowDesignations";
	private Long objectId;
	private String scriptName;
	private List<DesignationMaster> workflowDesigList=new ArrayList<DesignationMaster>();
	private EisUtilService eisService;
	private WorksService worksService;
	private transient ScriptService scriptExecutionService;
	
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
		if(workflow.getWorkflowDepartmentId()!=null && workflow.getWorkflowDepartmentId()!=-1 && 
				workflow.getWorkflowDesignationId()!=null && workflow.getWorkflowDesignationId()!=-1){
			HashMap<String,Object> paramMap = new HashMap<String, Object>();
			if(workflow.getWorkflowWardId()!=null && workflow.getWorkflowWardId()!=-1)
				paramMap.put("boundaryId",workflow.getWorkflowWardId().toString());
			paramMap.put("designationId", workflow.getWorkflowDesignationId().toString());
			if(workflow.getWorkflowDepartmentId()!=null && workflow.getWorkflowDepartmentId()!=-1)
				paramMap.put("departmentId",workflow.getWorkflowDepartmentId().toString());
			List roleList=worksService.getWorksRoles();	
			if(roleList!=null)
				paramMap.put("roleList", roleList);
			return eisService.getEmployeeInfoList(paramMap); 
		}
		return Collections.emptyList();
	}
	
	public  String getDesgByDeptAndType() {
		String departmentName="";
		if(workflow.getWorkflowDepartmentId()!=null && workflow.getWorkflowDepartmentId()!=-1) {
			departmentName =(String)getPersistenceService().find("select deptName from DepartmentImpl where id=?", workflow.getWorkflowDepartmentId());
		}
		List<Script> scriptList = persistenceService.findAllByNamedQuery(Script.BY_NAME,scriptName+".nextDesignation");
		if(!scriptList.isEmpty()){
			List<String> desglist = (List<String>) scriptExecutionService.executeScript(scriptList.get(0), ScriptService.createContext("department",departmentName,
					"objectId",objectId,"genericService",getPersistenceService()));
			List<String> desgListUpper = new ArrayList<String>();
			for(String desgNames:desglist){
				desgListUpper.add(desgNames.toUpperCase());
			}
			workflowDesigList.addAll(getPersistenceService().findAllByNamedQuery("getDesignationForListOfDesgNames", desgListUpper));
		}
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
