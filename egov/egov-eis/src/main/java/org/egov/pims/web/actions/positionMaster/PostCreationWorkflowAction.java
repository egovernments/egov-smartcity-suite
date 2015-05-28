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
package org.egov.pims.web.actions.positionMaster;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.pims.commons.DeptDesig;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.pims.model.EmpPosition;
import org.egov.pims.utils.EisConstants;
import org.egov.pims.web.actions.common.EisCommonWorkflowAction;
import org.egov.web.annotation.ValidationErrorPage;

/**
 * 
 * @author Vaibhav.K
 *
 */
@ParentPackage("egov")
@SuppressWarnings("serial")

public class PostCreationWorkflowAction extends EisCommonWorkflowAction{

	private static final Logger LOGGER = Logger.getLogger(PostCreationWorkflowAction.class);
	private String mode="";
	private Boolean checkForUniquePosition;
	private String postCurrentState="";
	private Long id;
	
	
	private SimpleWorkflowService<EmpPosition>  empPositionSimpleWorkflowService ;
	private CommonsService commonsService;
	
	//workflow related
	
	private Long currentStateId;
	
	private EmpPosition empPosition =  new EmpPosition();
	
	//Used for unique checking of position name
	private String positionName;

	//Used to get the designation coz DesignationMaster pojo doesn't contain getId() method
	private Long designationId;
	
	public PostCreationWorkflowAction()
	{
		
		addRelatedEntity("status", EgwStatus.class);
		addRelatedEntity("deptId",Department.class);
	}
	
	public void prepare()
	{
		super.prepare();
		
	    if(null!=id && !mode.equalsIgnoreCase("create") && !mode.equalsIgnoreCase(""))
	    {
	    	empPosition = (EmpPosition) persistenceService.getSession().get(EmpPosition.class, id);
	    	postCurrentState = empPosition.getState().getValue();
	    }
	}
	
	@Override
	public StateAware getModel() {
		return empPosition;
	}
	
	@SkipValidation
	@ValidationErrorPage(EDIT)
	public String beforeCreate()
	{
		this.mode="create";

		return EDIT;
	}
	
	@SkipValidation
	public String inbox()
	{
		
		LOGGER.debug("Inside prepare inbox method");
		this.mode= "modify";
		empPosition = (EmpPosition) persistenceService.getSession().load(EmpPosition.class, getModel().getId());
		if(null != empPosition)
		{
		    setDesignationId(empPosition.getDesigId().getId());
			currentStateId = empPosition.getCurrentState().getId();
		}
	return	super.inbox();
	}
	
	@ValidationErrorPage(EDIT)
	public String save()
	{
		if(mode.equalsIgnoreCase("create"))
		{
			workFlowAction = FORWARD;
			if(null == empPosition.getStatus())
				empPosition.setStatus((EgwStatus)commonsService.getStatusByModuleAndCode(EisConstants.EMP_POSITION,EisConstants.EMPPOS_CREATED ));
			empPosition.setPosition(null);
		}	
		empPosition.setDesigId((Designation)persistenceService.getSession().load(Designation.class, Long.valueOf(getDesignationId())));
		fireWorkFlow();
		
		if(empPosition.getStatus().getCode().equalsIgnoreCase("approved"))
		{	
			addActionMessage(getText("post.approved"));
			session().remove("idTemp");
		}
		else 
		{
			addActionMessage(getText("post.forwarded.rejected"));
			session().remove("idTemp");
		}
		
		this.mode="view";
		return EDIT;
	}
	
	protected void saveToOriginal()
	{
			DeptDesig deptDesig=(DeptDesig)persistenceService.find(
					" from DeptDesig where deptId.id=? and desigId.designationId=? ",empPosition.getDeptId().getId(),Long.valueOf(getDesignationId()));
			
			if(null!=deptDesig)
			{
				deptDesig.setSanctionedPosts(deptDesig.getSanctionedPosts()+1);
			}
			else
			{
				deptDesig = new DeptDesig();
				deptDesig.setSanctionedPosts(1);
				deptDesig.setOutsourcedPosts(0);
				deptDesig.setDepartment(empPosition.getDeptId());
				deptDesig.setDesignation((Designation)getPersistenceService().getSession().load(Designation.class, Long.valueOf(getDesignationId())));
			}
			Position position = new Position();
			position.setName(empPosition.getPostName());
			position.setDeptDesig(deptDesig);
			//position.setLastModifiedBy(empPosition.getLastModifiedBy());
			//position.setLastModifiedDate(empPosition.getLastModifiedDate().toDate());
			empPosition.setPosition(position);
			
	}
	
	public void validate()
	{
		if(null!=eisCommonsService.getPositionByName(empPosition.getPostName()))
		{
			addActionError(getText("post.position.exists"));
		}
	}
	
	

	public EmpPosition getEmpPosition() {
		return empPosition;
	}

	public void setEmpPosition(EmpPosition empPosition) {
		this.empPosition = empPosition;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	
	public void setEmpPositionSimpleWorkflowService(
			SimpleWorkflowService<EmpPosition> empPositionSimpleWorkflowService) {
		this.empPositionSimpleWorkflowService = empPositionSimpleWorkflowService;
	}

	public SimpleWorkflowService<EmpPosition> workflowService() {
		return empPositionSimpleWorkflowService;
	}

	public Integer getApproverPositionId() {
		return approverPositionId;
	}

	public void setApproverPositionId(Integer approverPositionId) {
		this.approverPositionId = approverPositionId;
	}

	public Long getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Long designationId) {
		this.designationId = designationId;
	}

	public Long getCurrentStateId() {
		return currentStateId;
	}

	public void setCurrentStateId(Long currentStateId) {
		this.currentStateId = currentStateId;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}


	public CommonsService getCommonsService() {
		return commonsService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public Boolean getCheckForUniquePosition() {
		return checkForUniquePosition;
	}

	public void setCheckForUniquePosition(Boolean checkForUniquePosition) {
		this.checkForUniquePosition = checkForUniquePosition;
	}

	public String getPostCurrentState() {
		return postCurrentState;
	}

	public void setPostCurrentState(String postCurrentState) {
		this.postCurrentState = postCurrentState;
	}

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@SkipValidation
	public String isUniquePostName()
	{
		Position position = eisCommonsService.getPositionByName(positionName);
		
		checkForUniquePosition=(null!=position?true:false);
		
		return "uniqueCheck";
		
	}
}
