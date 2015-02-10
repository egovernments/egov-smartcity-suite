package org.egov.pims.web.actions.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.StringUtils;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.EisUtilService;
import org.egov.web.actions.workflow.GenericWorkFlowAction;


/**
 * 
 * @author Vaibhav.K
 *
 */
@SuppressWarnings("serial")
@Result(name = "workFlowError", type = "stream", location = "returnStream", params = {"contentType", "text/plain" }) 
public abstract class EisCommonWorkflowAction extends GenericWorkFlowAction{
	private static final Logger LOGGER = Logger.getLogger(EisCommonWorkflowAction.class);
	private static final String WORKFLOWERROR = "workFlowError";
	protected static final String FORWARD = "Forward";
	protected Long currentStateId;
	protected EisUtilService eisService;
	protected EisCommonsService eisCommonsService;
	protected EmployeeService employeeService;
	protected CommonsService commonsService;
	protected Position pos;
	protected String returnStream = "Inbox item does not belong to the current user ";
	protected Integer approverPositionId;
	protected abstract SimpleWorkflowService workflowService();
	protected StateAware model;
	protected Integer empId;
	protected String mode="";
	//Used to get the designation coz DesignationMaster pojo doesn't contain getId() method
	protected Integer designationId;
	
	//this method should define the actions done on final approval of workflow object
	protected abstract void saveToOriginal();
	//protected Integer deptId;
	
   public void validateSave()
   {
		if(null == getCurrentUserPosition())
		{
			addActionError(getText("user.not.mapped.to.position"));
		}
   }
	@Override
	public StateAware getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public InputStream getReturnStream() {
		final ByteArrayInputStream is = new ByteArrayInputStream(this.returnStream.getBytes());
		return is;
	}
	
	public void prepare()
	{
		super.prepare();
		addDropdownData("designationlist", (ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-DesignationMaster"));
	    addDropdownData("departmentlist", (ArrayList)EgovMasterDataCaching.getInstance().get("egi-department"));
	}
	
	
	/**
	 * this method depends on validateSave for getting current user position
	 * also this fireworkflow should be called from save method of the extending action class
	 */
	protected void fireWorkFlow()
	{
		
		if(workFlowAction.equalsIgnoreCase("approve"))
		{
			saveToOriginal();
		}
		if(null!=approverPositionId)
		{
			getModel().setApproverPositionId(approverPositionId);
		}	
		if (null == getModel().getState()) {
			workflowService().start(getModel(), pos,approverComments);
		}
		if (null != workFlowAction && StringUtils.isNotEmpty(workFlowAction) && StringUtils.isNotBlank(workFlowAction)) {
			String comments = (null == approverComments || "".equals(approverComments.trim())) ? "" : approverComments;
			workflowService().transition(workFlowAction.toLowerCase(),	getModel(), comments);
		}
		
	}
	
	

	private Position getCurrentUserPosition() {
		pos = eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		return pos;
	}

	
	public String inbox()
	{
		
		if(null != getModel())
		{
			if(!validateInboxItemForUser(getModel(),Integer.valueOf(EGOVThreadLocals.getUserId())))
				return WORKFLOWERROR;
		}
		return EDIT;
	}

	/**
	* This method validate whether the work flow object is belongs to
	* given user or not .If workFlow object is belongs to given user returns
	* boolean value true else returns false.
	* 
	* @param wfObj - StateAware object
	* @param userid - id of UserImpl
	* @return Boolean value.
	**/
	
	private Boolean validateInboxItemForUser(StateAware wfItem, Integer userId) {
		Boolean validateObjectStatus = Boolean.FALSE;
		if (null != userId && null!= wfItem.getCurrentState() && !org.egov.infstr.models.State.END.equalsIgnoreCase(wfItem.getCurrentState().getValue())) {
			List<Position> positionList = eisService.getPositionsForUser(userId,DateUtils.today());
			if(positionList.contains(wfItem.getCurrentState().getOwner()))
				validateObjectStatus = Boolean.TRUE;
		}
		return validateObjectStatus;
	}
	
	
	
	public Long getCurrentStateId() {
		return currentStateId;
	}

	public void setCurrentStateId(Long currentStateId) {
		this.currentStateId = currentStateId;
	}

	public EisUtilService getEisService() {
		return eisService;
	}

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

	public EisCommonsService getEisCommonsService() {
		return eisCommonsService;
	}

	public void setEisCommonsService(EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public CommonsService getCommonsService() {
		return commonsService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public Integer getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	public Integer getApproverPositionId() {
		return approverPositionId;
	}

	public void setApproverPositionId(Integer approverPositionId) {
		this.approverPositionId = approverPositionId;
	}
	
}
