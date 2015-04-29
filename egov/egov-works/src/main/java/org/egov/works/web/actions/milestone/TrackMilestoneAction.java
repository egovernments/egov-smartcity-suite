package org.egov.works.web.actions.milestone;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.service.CommonsService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.milestone.Milestone;
import org.egov.works.models.milestone.MilestoneActivity;
import org.egov.works.models.milestone.TrackMilestone;
import org.egov.works.models.milestone.TrackMilestoneActivity;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author vikas
 *
 */
@ParentPackage("egov")
public class TrackMilestoneAction extends BaseFormAction{


	private static final long serialVersionUID = 1L;
	private TrackMilestone trackMilestone = new TrackMilestone();
	private PersistenceService<TrackMilestone,Long> trackMilestoneService;
	private Long id;
	private Long stateValue;
	private Long woEstimateId;
	private String mode="";
	private WorkflowService<TrackMilestone> trackMilestoneWorkflowService;
	private static final String SAVE_ACTION = "save";
	private String messageKey;
	private static final String TRACK_MILESTONE_MODULE_KEY = "TrackMilestone";
	@Autowired
        private CommonsService commonsService;
        @Autowired
        private UserService userService;
	private String actionName;
	private String sourcepage;
	private String nextEmployeeName;
	private String nextDesignation;
	private String designation;
	private WorksService worksService;
	private List<TrackMilestoneActivity> trackMilestoneActivities = new LinkedList<TrackMilestoneActivity>();
	private static final String SOURCE_INBOX = "inbox";
	private static final String MODE_MODIFY = "modify";
	private Long milestoneId;

	

	public TrackMilestoneAction() {
		addRelatedEntity("milestone", Milestone.class);
		addRelatedEntity("milestoneActivity", MilestoneActivity.class);
	}

	public void prepare(){

		if(id != null){
			trackMilestone=trackMilestoneService.findById(id, false);
		}
		
		if(woEstimateId != null){
			if(getTrackedMilestone(woEstimateId)==null || mode.equalsIgnoreCase("view")){
				List<TrackMilestoneActivity> trackMilestoneActivityList=new ArrayList<TrackMilestoneActivity>();
				Milestone milestone;
				if(milestoneId!=null && mode.equalsIgnoreCase("view")){
					milestone = (Milestone) getPersistenceService().find("from Milestone m where m.id= ? ",milestoneId);
				}
				else{
					milestone=(Milestone)getPersistenceService().find("from Milestone m where m.id=(select max(m1.id) from Milestone m1 where m1.workOrderEstimate.id=?)",woEstimateId);
				}
			
				if(mode.equalsIgnoreCase("view") && milestone.getTrackMilestone()!=null && milestone.getTrackMilestone().size()!=0){
					TrackMilestone tempTrackMilestone;
					if(milestoneId!=null){
						tempTrackMilestone	=(TrackMilestone)getPersistenceService().find("from TrackMilestone tm where tm.milestone.id = ? )",milestoneId);
					}
					else{
						tempTrackMilestone=(TrackMilestone)getPersistenceService().find("from TrackMilestone tm where tm.egwStatus.code<>? and tm.id=(select max(tm1.id) from TrackMilestone tm1 where tm1.milestone.id=?)","NEW",milestone.getId());
					}
						if(tempTrackMilestone!=null){
							trackMilestone=tempTrackMilestone;
							stateValue=trackMilestone.getState().getId();
						}
						else{
							trackMilestone.setMilestone(milestone);
						}
				}
				else{
					trackMilestone.setMilestone(milestone);
				}
				
				
				if(trackMilestone.getMilestone()!=null && trackMilestone.getId()==null){
					for(MilestoneActivity milestoneActivity:trackMilestone.getMilestone().getActivities()){
						TrackMilestoneActivity trackMilestoneActivity=new TrackMilestoneActivity();
						trackMilestoneActivity.setMilestoneActivity(milestoneActivity);
						trackMilestoneActivityList.add(trackMilestoneActivity);
					}
					trackMilestone.setActivities(trackMilestoneActivityList);
					if(mode.equalsIgnoreCase("view")){
						stateValue=milestone.getState().getId();
					}
				}
		
			}
			else{
				trackMilestone=getTrackedMilestone(woEstimateId);
				mode="modify";
			}
		}
		super.prepare();
		AjaxEstimateAction ajaxEstimateAction =new AjaxEstimateAction();
		ajaxEstimateAction.setPersistenceService(getPersistenceService());
		setupDropdownDataExcluding("milestone","milestoneActivity");
		addDropdownData("executingDepartmentList",getPersistenceService().findAllBy("from DepartmentImpl order by upper(deptName)")); 
	 }
	
	@Override
	public Object getModel() {
	
		return trackMilestone;
	}
	
	@SkipValidation
	public String newform(){
		if(trackMilestone.getEgwStatus()!=null &&
				trackMilestone.getEgwStatus().getCode().equals(WorksConstants.NEW)){
			User user=userService.getUserById(worksService.getCurrentLoggedInUserId());
			boolean isValidUser=worksService.validateWorkflowForUser(trackMilestone,user);
			if(isValidUser){
					throw new EGOVRuntimeException("Error: Invalid Owner - No permission to view this page.");
			}
		}
		return NEW;
	}

	@SkipValidation
	public String view(){
		
		return NEW;
	}

	public String save(){
		String actionName = parameters.get("actionName")[0];
		
		if(id==null){
			trackMilestone.setEgwStatus(commonsService.getStatusByModuleAndCode(TRACK_MILESTONE_MODULE_KEY,"NEW"));
		}
		
		if((mode.equalsIgnoreCase("modify")) && trackMilestone.getEgwStatus().getCode().equalsIgnoreCase("APPROVED")){
			trackMilestone.setEgwStatus(commonsService.getStatusByModuleAndCode(TRACK_MILESTONE_MODULE_KEY,"NEW"));
			//TODO - check for application for commenting out this line for any issues
			//trackMilestone.setState(null);
		}
		if(trackMilestone.getIsProjectCompleted()==null){
			trackMilestone.setIsProjectCompleted(Boolean.FALSE);
		}
		trackMilestone = trackMilestoneService.persist(trackMilestone);
		trackMilestoneWorkflowService.transition(actionName, trackMilestone, trackMilestone.getWorkflowapproverComments());
		trackMilestone = trackMilestoneService.persist(trackMilestone);
		messageKey="trackMilestone."+actionName;
		addActionMessage("Estimate - "+trackMilestone.getMilestone().getWorkOrderEstimate().getEstimate().getEstimateNumber());
		addActionMessage(getText(messageKey));
		getDesignation(trackMilestone);
		mode="";
		
		if(SAVE_ACTION.equals(actionName)){
			sourcepage="inbox";
		}
		
		return SAVE_ACTION.equals(actionName)?EDIT:SUCCESS;

	}
	
	public String cancel(){
		if(trackMilestone.getId()!=null){
			trackMilestoneWorkflowService.transition(TrackMilestone.Actions.CANCEL.toString(), trackMilestone,trackMilestone.getWorkflowapproverComments());
			trackMilestone=trackMilestoneService.persist(trackMilestone);
		}
		messageKey="trackMilestone.cancel";	
		getDesignation(trackMilestone);
		return SUCCESS;
	}	

	public String reject(){
		trackMilestoneWorkflowService.transition(TrackMilestone.Actions.REJECT.toString(),trackMilestone,trackMilestone.getWorkflowapproverComments());
		trackMilestone=trackMilestoneService.persist(trackMilestone);
		messageKey="trackMilestone.reject";	
		getDesignation(trackMilestone);
		return SUCCESS;
	}	

	private TrackMilestone getTrackedMilestone(Long woEstimateId){

		TrackMilestone trackMilestone=(TrackMilestone)getPersistenceService().find("from TrackMilestone tm where tm.milestone.workOrderEstimate.id=? and tm.egwStatus.code<>?  ",woEstimateId,"CANCELLED");
		return trackMilestone;
	}
		
	public void getDesignation(TrackMilestone trackMilestone){
		if(trackMilestone.getEgwStatus()!= null 
				&& !(WorksConstants.NEW).equalsIgnoreCase(trackMilestone.getEgwStatus().getCode())) {
			String result = worksService.getEmpNameDesignation(trackMilestone.getState().getOwnerPosition(), trackMilestone.getState().getCreatedDate().toDate());
			if(result != null && !"@".equalsIgnoreCase(result)) {
				String empName = result.substring(0,result.lastIndexOf('@'));
				String designation =result.substring(result.lastIndexOf('@')+1,result.length());
				setNextEmployeeName(empName);
				setNextDesignation(designation);
			}
		}
	}

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
		if(((SOURCE_INBOX.equalsIgnoreCase(sourcepage) || MODE_MODIFY.equalsIgnoreCase(mode)) 
				&& (trackMilestone.getEgwStatus()!=null &&
					    (!trackMilestone.getEgwStatus().getCode().equals(TrackMilestone.TrackMilestoneStatus.APPROVED.toString()) 
					    		&&  !trackMilestone.getEgwStatus().getCode().equals(TrackMilestone.TrackMilestoneStatus.CANCELLED.toString())))) 
				|| (trackMilestone.getEgwStatus()!=null &&
						trackMilestone.getEgwStatus().getCode().equals(WorksConstants.NEW))) {
			User user=userService.getUserById(worksService.getCurrentLoggedInUserId());
			boolean isValidUser=worksService.validateWorkflowForUser(trackMilestone,user);
			if(isValidUser){
					throw new EGOVRuntimeException("Error: Invalid Owner - No permission to view this page.");
			}
		}
		else if(StringUtils.isEmpty(sourcepage)){
			sourcepage="search";
		}

		return EDIT;
	}
	
	@SkipValidation
	public String search(){
		
		return "search";
	}

	@SkipValidation
	public String workflowHistory(){
		return "history";
	}

	 
	public void validate() {
		populateActivities();
		
		if(null == trackMilestone.getActivities() || trackMilestone.getActivities().size() ==0){
			 
			addFieldError("milestone.activity.missing", "Milestone Activity is not added");
		}
		BigDecimal percentage = BigDecimal.ZERO;
		for (TrackMilestoneActivity trackmilestoneActivity : trackMilestone.getActivities()) {
			if(trackmilestoneActivity.getComplPercentage()!=null){
				percentage = trackmilestoneActivity.getComplPercentage();
			}
			if(percentage.compareTo(BigDecimal.valueOf(100))==1){
				addFieldError("milestone.activity.total.percentage", "Total activity percentage should not be greater than 100%");
				break;
				 
			}
		}
	}

	 public void populateActivities(){
		 trackMilestone.getActivities().clear();

		for (TrackMilestoneActivity activity : trackMilestoneActivities) {
			if(activity!=null){
				MilestoneActivity milestoneActivity=(MilestoneActivity)getPersistenceService().find("from MilestoneActivity where id=?",activity.getMilestoneActivity().getId());
				activity.setMilestoneActivity(milestoneActivity);
				trackMilestone.addActivity(activity);
			}
		}
	 }


	public TrackMilestone getTrackMilestone() {
		return trackMilestone;
	}
	public void setTrackMilestone(TrackMilestone trackMilestone) {
		this.trackMilestone = trackMilestone;
	}
	public PersistenceService<TrackMilestone, Long> getTrackMilestoneService() {
		return trackMilestoneService;
	}
	public void setTrackMilestoneService(
			PersistenceService<TrackMilestone, Long> trackMilestoneService) {
		this.trackMilestoneService = trackMilestoneService;
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
		return trackMilestoneWorkflowService.getValidActions(trackMilestone); 		
	}
	
	public void setTrackMilestoneWorkflowService(WorkflowService<TrackMilestone> trackMilestoneWorkflowService) {
		this.trackMilestoneWorkflowService = trackMilestoneWorkflowService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public List<TrackMilestoneActivity> getTrackMilestoneActivities() {
		return trackMilestoneActivities;
	}

	public void setTrackMilestoneActivities(
			List<TrackMilestoneActivity> trackMilestoneActivities) {
		this.trackMilestoneActivities = trackMilestoneActivities;
	}

	public Long getWoEstimateId() {
		return woEstimateId;
	}

	public void setWoEstimateId(Long woEstimateId) {
		this.woEstimateId = woEstimateId;
	}

	public Long getStateValue() {
		return stateValue;
	}

	public void setStateValue(Long stateValue) {
		this.stateValue = stateValue;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Long getMilestoneId() {
		return milestoneId;
	}

	public void setMilestoneId(Long milestoneId) {
		this.milestoneId = milestoneId;
	}
}
