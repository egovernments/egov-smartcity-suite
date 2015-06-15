/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.pims.web.actions.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.commons.service.CommonsService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.actions.workflow.GenericWorkFlowAction;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.StringUtils;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.pims.service.EmployeeServiceOld;
import org.egov.pims.service.EisUtilService;


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
	protected EmployeeServiceOld employeeService;
	protected CommonsService commonsService;
	protected Position pos;
	protected String returnStream = "Inbox item does not belong to the current user ";
	protected Integer approverPositionId;
	protected abstract SimpleWorkflowService workflowService();
	protected StateAware model;
	protected Integer empId;
	protected String mode="";
	//Used to get the designation coz DesignationMaster pojo doesn't contain getId() method
	protected Long designationId;
	
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
		    //FIXME new JPA Entity mapping does not support optional field 
		    //@Basic(optional=true) is also not solving the issue
		    //So create a class which extend StateAware with  approverPositionId and extend that 
		    //class those needs approverPositionId
		    //getModel().setApproverPositionId(approverPositionId);
		}	
		if (null == getModel().getState()) {
			//workflowService().start(getModel(), pos,approverComments);
		    getModel().transition().start().withOwner(pos).withComments(approverComments);
		}
		if (null != workFlowAction && StringUtils.isNotEmpty(workFlowAction) && StringUtils.isNotBlank(workFlowAction)) {
			String comments = (null == approverComments || "".equals(approverComments.trim())) ? "" : approverComments;
			workflowService().transition(workFlowAction.toLowerCase(),	getModel(), comments);
		}
		
	}
	
	

	private Position getCurrentUserPosition() {
		pos = eisCommonsService.getPositionByUserId(EgovThreadLocals.getUserId());
		return pos;
	}

	
	public String inbox()
	{
		
		if(null != getModel())
		{
			if(!validateInboxItemForUser(getModel(),EgovThreadLocals.getUserId()))
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
	* @param userid - id of User
	* @return Boolean value.
	**/
	
	private Boolean validateInboxItemForUser(StateAware wfItem, Long userId) {
		Boolean validateObjectStatus = Boolean.FALSE;
		if (null != userId && null!= wfItem.getCurrentState() && !org.egov.infra.workflow.entity.State.DEFAULT_STATE_VALUE_CLOSED.equalsIgnoreCase(wfItem.getCurrentState().getValue())) {
			List<Position> positionList = eisService.getPositionsForUser(userId,DateUtils.today());
			if(positionList.contains(wfItem.getCurrentState().getOwnerPosition()))
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

	public EmployeeServiceOld getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeServiceOld employeeService) {
		this.employeeService = employeeService;
	}

	public CommonsService getCommonsService() {
		return commonsService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public Long getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Long designationId) {
		this.designationId = designationId;
	}

	public Integer getApproverPositionId() {
		return approverPositionId;
	}

	public void setApproverPositionId(Integer approverPositionId) {
		this.approverPositionId = approverPositionId;
	}
	
}
