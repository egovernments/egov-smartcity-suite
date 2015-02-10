package org.egov.pims.web.actions.positionMaster;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.commons.DeptDesig;
import org.egov.pims.commons.DesignationMaster;
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
	private Integer designationId;
	
	public PostCreationWorkflowAction()
	{
		
		addRelatedEntity("status", EgwStatus.class);
		addRelatedEntity("deptId",DepartmentImpl.class);
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
		    setDesignationId(empPosition.getDesigId().getDesignationId());
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
		empPosition.setDesigId((DesignationMaster)persistenceService.getSession().load(DesignationMaster.class, Long.valueOf(getDesignationId())));
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
				deptDesig.setDeptId(empPosition.getDeptId());
				deptDesig.setDesigId((DesignationMaster)getPersistenceService().getSession().load(DesignationMaster.class, Long.valueOf(getDesignationId())));
			}
			Position position = new Position();
			position.setName(empPosition.getPostName());
			position.setDeptDesigId(deptDesig);
			position.setModifiedBy(empPosition.getModifiedBy());
			position.setModifiedDate(empPosition.getModifiedDate());
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

	public Integer getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Integer designationId) {
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
