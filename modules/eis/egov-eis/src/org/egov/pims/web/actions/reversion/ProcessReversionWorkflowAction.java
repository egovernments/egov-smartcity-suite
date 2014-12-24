package org.egov.pims.web.actions.reversion;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFunction;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.AssignmentPrd;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.model.Reversion;
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

public class ProcessReversionWorkflowAction extends EisCommonWorkflowAction
{
	private static final Logger LOGGER = Logger.getLogger(ProcessReversionWorkflowAction.class);
	
	private Reversion reversion = new Reversion();
	private SimpleWorkflowService<Reversion> reversionWorkflowService ;
	private Long id;
	
	public ProcessReversionWorkflowAction()
	{
		addRelatedEntity("dept",DepartmentImpl.class);
		addRelatedEntity("status", EgwStatus.class);
		addRelatedEntity("fund",Fund.class);
		addRelatedEntity("function", CFunction.class);
		addRelatedEntity("position",Position.class);
		addRelatedEntity("employee", PersonalInformation.class);
	}
	
	public void prepare()
	{
		super.prepare();
	    addDropdownData("fundlist",(ArrayList)EgovMasterDataCaching.getInstance().get("egi-fund"));
	    addDropdownData("functionlist",(ArrayList)EgovMasterDataCaching.getInstance().get("egi-function"));
	    if(empId!=null && mode.equalsIgnoreCase("create")){
	    	reversion.setEmpDetails((EmployeeView)persistenceService.getSession().load(EmployeeView.class, empId));	    	
	    }	
	    if(id!=null && !mode.equalsIgnoreCase("create"))
	    {
	    	reversion = (Reversion) persistenceService.find("from Reversion where id=?", id);
	    	reversion.setEmpDetails((EmployeeView)persistenceService.getSession().load(EmployeeView.class, reversion.getEmployee().getIdPersonalInformation()));
	    }
	}
	
	@Override
	public StateAware getModel() {
		
		return reversion;
	}
	
	@SkipValidation
	public String beforeCreate()
	{
		this.mode="create";
		if(empId!=null){
	    	reversion.setEmpDetails((EmployeeView)persistenceService.getSession().load(EmployeeView.class, empId));	    	
	    }

		return EDIT;
	}
	
	@SkipValidation
	@ValidationErrorPage(EDIT)
	public String inbox()
	{
		LOGGER.debug("Inside inbox method");
		this.mode= "modify";
		reversion = (Reversion) persistenceService.find("from Reversion where id=?", id);
		if(null != reversion)
		{
		    setDesignationId(reversion.getDesig().getDesignationId());
		}
		return super.inbox();
	}
	
	@ValidationErrorPage(EDIT)
	public String save()
	{
		if(mode.equalsIgnoreCase("create"))
		{
			workFlowAction = FORWARD;
			if(null == reversion.getStatus())
				reversion.setStatus((EgwStatus)commonsService.getStatusByModuleAndCode(EisConstants.REVERSION,EisConstants.REV_CREATED ));
		}	 
		reversion.setDesig((DesignationMaster)persistenceService.getSession().load(DesignationMaster.class, Integer.valueOf(getDesignationId())));
		fireWorkFlow();
		
		this.mode="view";
		return EDIT;
	}
	
	
	public void validate()
	{
	}
	
	public SimpleWorkflowService<Reversion> workflowService() {
		return reversionWorkflowService;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	

	public Integer getApproverPositionId() {
		return approverPositionId;
	}

	public void setApproverPositionId(Integer approverPositionId) {
		this.approverPositionId = approverPositionId;
	}

	public Long getCurrentStateId() {
		return currentStateId;
	}

	public void setCurrentStateId(Long currentStateId) {
		this.currentStateId = currentStateId;
	}

	public void setReversionWorkflowService(
			SimpleWorkflowService<Reversion> reversionWorkflowService) {
		this.reversionWorkflowService = reversionWorkflowService;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	protected void saveToOriginal() {
		
		Assignment assignment = employeeService.getAssignmentByEmpAndDate(new Date(),reversion.getEmployee().getIdPersonalInformation());
		assignment.getAssignmentPrd().setToDate(DateUtils.yesterday());
		persistenceService.getSession().saveOrUpdate(assignment);
		  
		Assignment newAssignment = new Assignment();
		AssignmentPrd assignPrd = new AssignmentPrd();
		assignPrd.setEmployeeId(reversion.getEmployee());
		assignPrd.setFromDate(reversion.getReversionEffFrom());
		assignPrd.setToDate(reversion.getEmployee().getRetirementDate());
		
		newAssignment.setDeptId(reversion.getDept());
		newAssignment.setDesigId(reversion.getDesig());
		newAssignment.setFunctionId(reversion.getFunction());
		newAssignment.setFundId(reversion.getFund());
		newAssignment.setPosition(reversion.getPosition());
		newAssignment.setIsPrimary('Y');
		newAssignment.setAssignmentPrd(assignPrd);
		reversion.setAssignment(newAssignment);
		
		
	}


}
