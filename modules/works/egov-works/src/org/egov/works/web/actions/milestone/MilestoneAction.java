package org.egov.works.web.actions.milestone;


import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.milestone.Milestone;
import org.egov.works.models.milestone.MilestoneActivity;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.WorksService;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
import org.apache.commons.lang.StringUtils;

/**
 * @author vikas
 *
 */
@ParentPackage("egov")
public class MilestoneAction extends BaseFormAction{

	private static final long serialVersionUID = 1L;
	private Milestone milestone = new Milestone();
	private PersistenceService<Milestone,Long> milestoneService;
	private Long id;
	private Long woEstimateId;
	private String mode="";
	private WorkflowService<Milestone> milestoneWorkflowService;
	private static final String SAVE_ACTION = "save";
	private String messageKey;
	private static final String MILESTONE_MODULE_KEY = "Milestone";
	private CommonsService commonsService;
	private UserDAO userDao;
	private String actionName;
	private String sourcepage;
	private String nextEmployeeName;
	private String nextDesignation;
	private String designation;
	private WorksService worksService;
	private List<MilestoneActivity> milestoneActivities = new LinkedList<MilestoneActivity>();
	private static final String SOURCE_INBOX = "inbox";


	public MilestoneAction() {
		addRelatedEntity("workOrderEstimate", WorkOrderEstimate.class);
		addRelatedEntity("workType", EgwTypeOfWork.class);
		addRelatedEntity("subType", EgwTypeOfWork.class);
	}

	public void prepare(){

		if(id != null){
			milestone=milestoneService.findById(id, false);
		}
		
		if(woEstimateId != null){
			milestone.setWorkOrderEstimate((WorkOrderEstimate)getPersistenceService().find("from WorkOrderEstimate where id=?",woEstimateId));
		}
		super.prepare();
		AjaxEstimateAction ajaxEstimateAction =new AjaxEstimateAction();
		ajaxEstimateAction.setPersistenceService(getPersistenceService());
		setupDropdownDataExcluding("workType","subType","workOrderEstimate");
		addDropdownData("parentCategoryList",getPersistenceService().findAllBy("from EgwTypeOfWork etw where etw.parentid is null"));
		addDropdownData("categoryList", Collections.emptyList());
		addDropdownData("executingDepartmentList",getPersistenceService().findAllBy("from DepartmentImpl order by upper(deptName)")); 
	 }
	
	@Override
	public Object getModel() {
	
		return milestone;
	}
	
	@SkipValidation
	public String newform(){
		
		return "new";
	}
	
	public String save(){
		//commented to skip workflow
/*		String actionName = parameters.get("actionName")[0];
		
		if(id==null){
			milestone.setEgwStatus(commonsService.getStatusByModuleAndCode(MILESTONE_MODULE_KEY,"APPROVED"));
		}

		milestone = milestoneService.persist(milestone);
		milestoneWorkflowService.transition(actionName, milestone, milestone.getWorkflowapproverComments());
		milestone = milestoneService.persist(milestone);
		messageKey="milestone."+actionName;
		addActionMessage(getText(messageKey,"The Milestone was saved successfully"));
		getDesignation(milestone);
		
		if(SAVE_ACTION.equals(actionName)){
			sourcepage="inbox";
		}

		return SAVE_ACTION.equals(actionName)?EDIT:SUCCESS;
*/
		
		milestone.setEgwStatus(commonsService.getStatusByModuleAndCode(MILESTONE_MODULE_KEY,"APPROVED"));
		milestone = milestoneService.persist(milestone);
		return SUCCESS;
	}
	//commented to disbale workflow
/*	public String cancel(){
		if(milestone.getId()!=null){
			milestoneWorkflowService.transition(Milestone.Actions.CANCEL.toString(), milestone,milestone.getWorkflowapproverComments());
			milestone=milestoneService.persist(milestone);
		}
		messageKey="milestone.cancel";	
		getDesignation(milestone);
		return SUCCESS;
	}	

	public String reject(){
		milestoneWorkflowService.transition(Milestone.Actions.REJECT.toString(),milestone,milestone.getWorkflowapproverComments());
		milestone=milestoneService.persist(milestone);
		messageKey="milestone.reject";	
		getDesignation(milestone);
		return SUCCESS;
	}	*/

/*	public void getDesignation(Milestone milestone){
		if(milestone.getCurrentState()!= null 
				&& !"NEW".equalsIgnoreCase(milestone.getCurrentState().getValue())) {
			String result = worksService.getEmpNameDesignation(milestone.getState().getOwner(),milestone.getState().getCreatedDate());
			if(result != null && !"@".equalsIgnoreCase(result)) {
				String empName = result.substring(0,result.lastIndexOf('@'));
				String designation =result.substring(result.lastIndexOf('@')+1,result.length());
				setNextEmployeeName(empName);
				setNextDesignation(designation);
			}
		}
	}*/

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getSourcepage() {
		return sourcepage;
	}

	public void setSourcepage(String sourcepage) {
		this.sourcepage = sourcepage;
	}

	public String getNextEmployeeName() {
		return nextEmployeeName;
	}

	public void setNextEmployeeName(String nextEmployeeName) {
		this.nextEmployeeName = nextEmployeeName;
	}

	public String getNextDesignation() {
		return nextDesignation;
	}

	public void setNextDesignation(String nextDesignation) {
		this.nextDesignation = nextDesignation;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	
	@SkipValidation
	public String edit(){
/*		if(SOURCE_INBOX.equalsIgnoreCase(sourcepage) || (milestone.getCurrentState()!=null &&
				milestone.getCurrentState().getValue().equals(State.NEW))){
			User user=userDao.getUserByID(Integer.valueOf(EGOVThreadLocals.getUserId()));
			boolean isValidUser=worksService.validateWorkflowForUser(milestone,user);
			if(isValidUser){
					throw new EGOVRuntimeException("Error: Invalid Owner - No permission to view this page.");
			}
		}
		else if(StringUtils.isEmpty(sourcepage)){
			sourcepage="search";
		}
*/
		if(StringUtils.isEmpty(sourcepage)){
			sourcepage="search";
		}
		return "edit";
	}
	
	@SkipValidation
	public String search(){
		
		return "search";
	}


	 
	public void validate() {
		populateActivities();
		
		if(null == milestone.getActivities() || milestone.getActivities().size() ==0){
			 
			addFieldError("milestone.activity.missing", "Milestone Activity is not added");
		}
		BigDecimal percentage = BigDecimal.ZERO;
		for (MilestoneActivity milestoneActivity : milestone.getActivities()) {
			if(milestoneActivity.getPercentage()!=null){
				percentage = percentage.add(milestoneActivity.getPercentage());
			}
		}
		if(percentage.compareTo(BigDecimal.valueOf(100)) !=0){
			addFieldError("milestone.activity.total.percentage", "Total activity percentage should be equal to 100%");
			 
		}
	}

	 public void populateActivities(){
		 milestone.getActivities().clear();

		for (MilestoneActivity activity : milestoneActivities) {
			if(activity!=null){
				milestone.addActivity(activity);
			}
		}
	 }


	public Milestone getMilestone() {
		return milestone;
	}
	public void setMilestone(Milestone milestone) {
		this.milestone = milestone;
	}
	public PersistenceService<Milestone, Long> getMilestoneService() {
		return milestoneService;
	}
	public void setMilestoneService(
			PersistenceService<Milestone, Long> milestoneService) {
		this.milestoneService = milestoneService;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getMode() {
		return mode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public List<org.egov.infstr.workflow.Action> getValidActions(){
		return milestoneWorkflowService.getValidActions(milestone); 		
	}
	
	public void setMilestoneWorkflowService(WorkflowService<Milestone> milestoneWorkflowService) {
		this.milestoneWorkflowService = milestoneWorkflowService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public List<MilestoneActivity> getMilestoneActivities() {
		return milestoneActivities;
	}

	public void setMilestoneActivities(
			List<MilestoneActivity> milestoneActivities) {
		this.milestoneActivities = milestoneActivities;
	}

	public Long getWoEstimateId() {
		return woEstimateId;
	}

	public void setWoEstimateId(Long woEstimateId) {
		this.woEstimateId = woEstimateId;
	}

	public void setUserDao(UserDAO userDao) {
		this.userDao = userDao;
	}

}
