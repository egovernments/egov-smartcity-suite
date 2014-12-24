package org.egov.tender.web.actions.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.egov.infstr.services.ScriptService;
import org.egov.infstr.workflow.Action;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.service.EisUtilService;
import org.egov.tender.services.common.TenderCommonService;
import org.egov.tender.utils.TenderConstants;
import org.egov.web.actions.BaseFormAction;

@SuppressWarnings("serial")
public abstract class TenderWorkFlowAction extends BaseFormAction {
	
	protected static final String VIEW        = "view";
	protected static final String MODIFY      = "modify";
	protected static final String NOTMODIFY   = "notmodify";
	protected String  approverDepartmentId;
	protected String  approverDesg;
	protected Integer approverName;
	protected String  comments;
	protected String  wfStatus;
	protected String  workFlowType;
	protected String  mode;
	protected Long    idTemp;
	protected TenderCommonService tenderCommonService;
	protected ScriptService scriptService;
	protected EisUtilService eisService;
	
	public void prepare()
	{
		super.prepare();
		addDropdownData("approverDepartmentList",persistenceService.findAllBy("from DepartmentImpl order by deptName"));
		List<DesignationMaster> designationList = Collections.emptyList();
		if(approverDepartmentId!=null && !"-1".equals(approverDepartmentId) && !"".equals(approverDepartmentId))
			designationList=eisService.getAllDesignationByDept(Integer.parseInt(approverDepartmentId), new Date());
		addDropdownData("approverList", populateApprover());
		addDropdownData("desgnationList", designationList);
	}
	
	public List<EmployeeView> populateApprover()
	{
		List<EmployeeView> approverList = Collections.emptyList();
		if(approverDesg!=null && !"-1".equals(approverDesg) && !"".equals(approverDesg)){

			HashMap<String,String> paramMap = new HashMap<String, String>();
			if(approverDepartmentId!=null && !approverDepartmentId.equals(""))
				paramMap.put("departmentId",approverDepartmentId);
			if(approverDesg!=null && !approverDesg.equals(""))			
				paramMap.put("designationId", approverDesg);
			approverList = eisService.getEmployeeInfoList(paramMap);
		}
		return approverList;
	}
	
	public Position getPosition()
	{
		if(approverName==null || approverName ==-1 || TenderConstants.ACTION_SAVE.equals(workFlowType))
			return null;
		else
			return (Position)persistenceService.find("from Position where id=?",approverName);
	}

	@SuppressWarnings("unchecked")
	public List<Action> getValidActions(String wfstatus,String type,String scriptName){
		List<Action> validButtons = new ArrayList<Action>();
		List<String> list = (List<String>) scriptService.executeScript(scriptName, ScriptService.createContext("wfstatus",wfstatus));
		for(String buttons:list)
		{
			Action action = (Action) persistenceService.find(" from org.egov.infstr.workflow.Action where type=?  and name=?",type,buttons);
			validButtons.add(action);
		}
		return validButtons;
	}
	
	public String getApproverDepartmentId() {
		return approverDepartmentId;
	}

	public void setApproverDepartmentId(String approverDepartmentId) {
		this.approverDepartmentId = approverDepartmentId;
	}

	public String getApproverDesg() {
		return approverDesg;
	}

	public void setApproverDesg(String approverDesg) {
		this.approverDesg = approverDesg;
	}

	public Integer getApproverName() {
		return approverName;
	}

	public void setApproverName(Integer approverName) {
		this.approverName = approverName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getWfStatus() {
		return wfStatus;
	}

	public void setWfStatus(String wfStatus) {
		this.wfStatus = wfStatus;
	}
	
	public String getWorkFlowType() {
		return workFlowType;
	}

	public void setWorkFlowType(String workFlowType) {
		this.workFlowType = workFlowType;
	}
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Long getIdTemp() {
		return idTemp;
	}

	public void setIdTemp(Long idTemp) {
		this.idTemp = idTemp;
	}
	
	protected String getMessage(String key)
	{
		return getText(key);
	}
	
	protected String getMessageWithParam(String key,String[] args)
	{
		return getText(key, args);
	}
	
	public void setTenderCommonService(TenderCommonService tenderCommonService) {
		this.tenderCommonService = tenderCommonService;
    }

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

	public void setScriptService(ScriptService scriptService) {
		this.scriptService = scriptService;
	}

}
