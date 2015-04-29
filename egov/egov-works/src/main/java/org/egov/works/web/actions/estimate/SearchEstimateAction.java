package org.egov.works.web.actions.estimate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.service.CommonsService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.workflow.entity.State;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.utils.DateUtils;
import org.egov.lib.rrbac.model.Action;
import org.egov.lib.rrbac.services.RbacService;
import org.egov.pims.commons.Position;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.works.models.contractorBill.ContractorBillRegister;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.WorkType;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.milestone.Milestone;
import org.egov.works.models.milestone.TrackMilestone;
import org.egov.works.models.tender.WorksPackage;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.workorder.AjaxWorkOrderAction;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
public class SearchEstimateAction extends SearchFormAction { 

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SearchEstimateAction.class);
	private String status;
	private Integer expenditureType=-1;
	private String estimateNumber ="";
	private String projCode ="";
	private String workOrderNo ="";
	private Integer engineerIncharge;
	private Integer engineerIncharge2;
	private Long assignedTo1;
	private Long assignedTo2; 
	private String source;
	private Long execDept;
	private AbstractEstimateService abstractEstimateService;
	private List<AbstractEstimate> results=new LinkedList<AbstractEstimate>();
	private AbstractEstimate estimates = new AbstractEstimate();
	@Autowired
        private EmployeeService employeeService;
	private Integer empId;
	private String wpdate;
	
	private WorksService worksService; 
	private String negoCreatedBy;
	private String statusReq;
	private Date fromDate;
	private Date toDate;
	private String estimateOrWpSearchReq;
	public static final String dateFormat="dd-MMM-yyyy";
	private String option="";
	private boolean selectedorder;
	private Integer expenditureTypeid;	
	public static final String RESULTS="results";
	public static final String SEARCH_ESTIMATE_FOR_WO="SearchEstimateforWO";
	public static final String UNCHECKED="unchecked";
	private PersonalInformationService personalInformationService;
	private EisUtilService eisService;
	@Autowired
	private DepartmentService departmentService;
	private boolean checkWO;
	private String cancellationReason;
	private String cancelRemarks;
	private static final String ASSIGNED_TO_LIST = "assignedToList";
	private static final String ASSIGNED_USER_LIST1 = "assignedUserList1";
	private static final String ASSIGNED_USER_LIST2 = "assignedUserList2";
	public static final String SEARCH_ESTIMATE_FOR_MILESTONE="searchEstimateForMilestone";
	public static final String VIEW_MILESTONE="viewMilestone";
	public static final String CANCEL_MILESTONE="cancelMilestone";
	public static final String MILESTONE_STATUS_APPRD="Approved";
	public static final String MS_OBJECT_TYPE="Milestone";
	public static final String TMS_OBJECT_TYPE="TrackMilestone";
	private String workOrdEstIds;
	@Autowired
        private CommonsService commonsService;
	@Autowired
        private UserService userService;
	private String messageKey;
	private String ward="";
	private String loginUserDeptName="";
	@Autowired
	private RbacService rbacService;
	private List<Role> roles = new ArrayList<Role>();
	private String milestoneStatus;
	private String status2;

	public Integer getExpenditureTypeid() {
		return expenditureTypeid;
	}

	public void setExpenditureTypeid(Integer expenditureTypeid) {
		this.expenditureTypeid = expenditureTypeid;
	}

	public SearchEstimateAction() {
		addRelatedEntity("category", EgwTypeOfWork.class);
		addRelatedEntity("parentCategory", EgwTypeOfWork.class);
		addRelatedEntity("ward", Boundary.class);
	}  
	
	public Object getModel() {
		return estimates;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String execute(){
		return INDEX;
	}
	
	/**
	 * @return List of abstract estimates with "positionAndUserName" populated
	 */
	@SuppressWarnings(UNCHECKED)
	private void populatePositionAndUserName() {
		List<AbstractEstimate> abEstimateList = new LinkedList<AbstractEstimate>();
		
		Iterator iter = searchResult.getList().iterator();
		while(iter.hasNext()) {
			Object row = (Object)iter.next();
			AbstractEstimate estimate = (AbstractEstimate) row;
			if(!estimate.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.ADMIN_SANCTIONED_STATUS) && !estimate.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.CANCELLED_STATUS)){
				String posName = estimate.getState().getOwnerPosition().getName();
				PersonalInformation emp = employeeService.getEmployeeforPosition(estimate.getState().getOwnerPosition());
				if(emp!=null)
					estimate.setPositionAndUserName(posName + " / " +  emp.getEmployeeName());
				else
					estimate.setPositionAndUserName(posName );
			}
			abEstimateList.add(estimate);
		}
		searchResult.getList().clear();
		HashSet<AbstractEstimate> uniqueAbsEstimateList=new HashSet<AbstractEstimate>(abEstimateList);
		searchResult.getList().addAll(uniqueAbsEstimateList);
	}
	
	@SuppressWarnings(UNCHECKED)
	private void showOwnerName() {
		List<WorkOrderEstimate> woeList = new LinkedList<WorkOrderEstimate>();
		
		Iterator iter = searchResult.getList().iterator();
		while(iter.hasNext()) {
			Object row = (Object)iter.next();
			WorkOrderEstimate woe = (WorkOrderEstimate) row;
			Milestone lastestMilestoneObj = woe.getLatestMilestone();
			if(lastestMilestoneObj!=null){
				if(!lastestMilestoneObj.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.APPROVED) && !lastestMilestoneObj.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.CANCELLED_STATUS)){
					PersonalInformation emp = employeeService.getEmployeeforPosition(lastestMilestoneObj.getState().getOwnerPosition());
					if(emp.getUserMaster()!=null){
						lastestMilestoneObj.setOwnerName(emp.getUserMaster().getName());
					}
				}
			}
			woeList.add(woe);
		}
		searchResult.getList().clear();
		HashSet<WorkOrderEstimate> uniqueWOEstimateList=new HashSet<WorkOrderEstimate>(woeList);
		searchResult.getList().addAll(uniqueWOEstimateList);
	}
		
	@SuppressWarnings(UNCHECKED)
	public void showOwnerNameForViewMilestone() {
		List<Object[]> tempList = new LinkedList<Object[]>();
		
		Iterator iter = searchResult.getList().iterator();
		while(iter.hasNext()) {
			Object[] obj =  (Object[]) iter.next();
			Milestone msObj = (Milestone) obj[1];
			TrackMilestone tmObj = (TrackMilestone) obj[2];
			if(msObj!=null){
				if(!msObj.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.APPROVED) && !msObj.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.CANCELLED_STATUS)){
					PersonalInformation emp = employeeService.getEmployeeforPosition(msObj.getState().getOwnerPosition());
					if(emp.getUserMaster()!=null){
						msObj.setOwnerName(emp.getUserMaster().getName());
					}
				}
			}
			if(tmObj!=null){
				if(!tmObj.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.APPROVED) && !tmObj.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.CANCELLED_STATUS)){
					PersonalInformation emp = employeeService.getEmployeeforPosition(tmObj.getState().getOwnerPosition());
					if(emp.getUserMaster()!=null){
						tmObj.setOwnerName(emp.getUserMaster().getName());
					}
				}
			}
			tempList.add(obj);
		}
		searchResult.getList();
	}
	
	public String printpage(){
		search();
		return "print";
	}	
	
	private void populateWorkOrderAssignedToList(AjaxWorkOrderAction ajaxWorkOrderAction, boolean executingDeptPopulated){
		if (executingDeptPopulated && execDept != null && execDept>0) {
			ajaxWorkOrderAction.setDepartmentName(departmentService.getDepartmentById(Long.valueOf(execDept)).getName());
			ajaxWorkOrderAction.getDesignationByDeptId();
			addDropdownData(ASSIGNED_TO_LIST,ajaxWorkOrderAction.getWorkOrderDesigList());
		}
		else {
			addDropdownData(ASSIGNED_TO_LIST,Collections.EMPTY_LIST);
		}
	}
	
	private void populateWorkOrderUsersList1(AjaxWorkOrderAction ajaxWorkOrderAction, boolean desgId,boolean executingDeptPopulated){
		if (desgId && executingDeptPopulated && execDept != null && execDept>0) {
			ajaxWorkOrderAction.setDesgId((getassignedTo1().longValue()));
			ajaxWorkOrderAction.setExecutingDepartment(execDept);
			ajaxWorkOrderAction.getUsersForDesg();
			addDropdownData(ASSIGNED_USER_LIST1,ajaxWorkOrderAction.getUserList());
		}
		else {
			addDropdownData(ASSIGNED_USER_LIST1,Collections.EMPTY_LIST);
		}
	}
	
	private void populateWorkOrderUsersList2(AjaxWorkOrderAction ajaxWorkOrderAction, boolean desgId, boolean executingDeptPopulated){
		if (desgId && executingDeptPopulated && execDept>0) {
			ajaxWorkOrderAction.setDesgId(assignedTo2.longValue());
			ajaxWorkOrderAction.setExecutingDepartment(execDept);
			ajaxWorkOrderAction.getUsersForDesg();
			addDropdownData(ASSIGNED_USER_LIST2,ajaxWorkOrderAction.getUserList());
		}
		else {
			addDropdownData(ASSIGNED_USER_LIST2,Collections.EMPTY_LIST);
		}
	}
			
	@SuppressWarnings(UNCHECKED)
	public List getEstimateStatuses() {
		return persistenceService.findAllBy("from EgwStatus s where moduletype=? and code not in ('NEW','APPROVED','BUDGETARY_APPR_VALIDATED') order by orderId",AbstractEstimate.class.getSimpleName());
	}
	
	@SuppressWarnings(UNCHECKED)
	public void prepare(){
		AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
		AjaxWorkOrderAction ajaxWorkOrderAction = new AjaxWorkOrderAction();
		ajaxWorkOrderAction.setPersistenceService(getPersistenceService());
		ajaxWorkOrderAction.setPersonalInformationService(personalInformationService);
		ajaxEstimateAction.setPersistenceService(getPersistenceService());
		ajaxEstimateAction.setEmployeeService(employeeService);
		ajaxEstimateAction.setPersonalInformationService(personalInformationService);
		ajaxEstimateAction.setEisService(eisService);
		super.prepare();
		setupDropdownDataExcluding("ward");
		List<Department> values = (List<Department>) getPersistenceService().findAllBy("from DepartmentImpl dt");
		addDropdownData("executingDepartmentList",values);
		List<WorkType> worktypeList = (List<WorkType>) getPersistenceService().findAllBy("from WorkType dt");
		addDropdownData("typeList", worktypeList);
		List<String> milestoneStatusList = new ArrayList<String>();
		milestoneStatusList.add("Milestone Created");
		milestoneStatusList.add("Milestone Tracked");
		milestoneStatusList.add("Project/Work Completed");
		addDropdownData("msStatusList", milestoneStatusList);
		List<String> statList = persistenceService.findAllBy("select s.code from EgwStatus s where s.moduletype=? and s.code in ('NEW','CREATED','APPROVED','CANCELLED')order by s.orderId",Milestone.class.getSimpleName());
		addDropdownData("statusList", statList);
		addDropdownData("parentCategoryList", getPersistenceService().findAllBy("from EgwTypeOfWork etw1 where etw1.parentid is null"));
		populateCategoryList(ajaxEstimateAction, estimates.getParentCategory() != null);
		populatePreparedByList(ajaxEstimateAction,execDept != null);
		if("wp".equals(source))
			setStatus(AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString());		
		
		if(SEARCH_ESTIMATE_FOR_WO.equals(source)){
			String status=worksService.getWorksConfigValue("NEGOTIATIONSTMT_WO_STATUS");
			if(StringUtils.isNotBlank(status)){ 
				setStatus(status); 
			}
			setToDate(new Date());
			perform();			
		}		
		
		if(SEARCH_ESTIMATE_FOR_MILESTONE.equalsIgnoreCase(source) || VIEW_MILESTONE.equalsIgnoreCase(source) 
				|| CANCEL_MILESTONE.equalsIgnoreCase(source)){
			populateWorkOrderAssignedToList(ajaxWorkOrderAction,execDept!=null) ;
			populateWorkOrderUsersList1(ajaxWorkOrderAction,assignedTo1!=null,execDept!=null) ;
			populateWorkOrderUsersList2(ajaxWorkOrderAction,assignedTo2!=null,execDept!=null) ;
		}
		if("createNegotiationNew".equalsIgnoreCase(source)){
			perform();	
		}	
		if(CANCEL_MILESTONE.equalsIgnoreCase(source)) {
			setStatus(MILESTONE_STATUS_APPRD);
		}
		
		if("cancelEstimate".equals(source)){
			status=AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString();
		}
		getLoginUserDept();
		getLoginUserRoles();
	}
	
	@SuppressWarnings(UNCHECKED)
	public void perform(){
			if(abstractEstimateService.getLatestAssignmentForCurrentLoginUser()!=null) {
				execDept=abstractEstimateService.getLatestAssignmentForCurrentLoginUser().getDeptId().getId();			
			}
			negoCreatedBy=worksService.getWorksConfigValue("TENDER_NEGOTIATION_CREATED_BY_SELECTION");
			
			if(SEARCH_ESTIMATE_FOR_WO.equals(source)){
				List<Department> deptValues = (List<Department>) getPersistenceService().findAllBy("select distinct ae.executingDepartment" +
						" from AbstractEstimate ae where ae.id in ("   +
						" ( select tr.tenderEstimate.abstractEstimate.id " +
						" from TenderResponse tr where tr.egwStatus.code=? ) ) or  ae.id in ( " +
						" select wd.estimate.id from WorksPackageDetails wd where wd.worksPackage.id in " +
						" (select tr.tenderEstimate.worksPackage.id from TenderResponse tr " +
						" where tr.tenderEstimate.abstractEstimate.id=null and  tr.tenderEstimate.worksPackage.id!=null " +
						" and tr.egwStatus.code=?))" +
						" and ae.id not in (select wo.abstractEstimate.id from WorkOrder wo)",status,status);
				addDropdownData("executingDepartmentList",deptValues);
			}
			else{			
			estimateOrWpSearchReq=worksService.getWorksConfigValue("ESTIMATE_OR_WP_SEARCH_REQ");
			 
			statusReq=worksService.getWorksConfigValue("ESTIMATE_STATUS");
			
			if(StringUtils.isNotBlank(statusReq)){
				setStatus(statusReq);
			}
		}
	}

	private Map getEstimateForMilestoneQuery(){
		StringBuffer query = new StringBuffer(700);
		List<Object> paramList = new ArrayList<Object>();
		HashMap<String,Object> queryAndParams=new HashMap<String,Object>();
		if(SEARCH_ESTIMATE_FOR_MILESTONE.equalsIgnoreCase(source)){
			query.append("from WorkOrderEstimate  as woe where woe.workOrder.parent is null and woe.workOrder.egwStatus.code=? ");
			paramList.add("APPROVED");
			query.append(" and woe.id not in (select m.workOrderEstimate.id from Milestone as m where m.egwStatus.code not in (?,?))");
			paramList.add("APPROVED");
			paramList.add("CANCELLED");
			query.append(" and woe.id not in (select tm.milestone.workOrderEstimate.id from TrackMilestone as tm where tm.egwStatus.code not in (?,?) or (tm.isProjectCompleted=? and tm.egwStatus.code<>?))");
			paramList.add("APPROVED");
			paramList.add("CANCELLED");
			paramList.add(Boolean.TRUE);
			paramList.add("CANCELLED");
			query.append("and woe.estimate.projectCode.egwStatus.code!=?");
			paramList.add("CLOSED");
		}
		else if(CANCEL_MILESTONE.equalsIgnoreCase(source)) {
			query.append("from WorkOrderEstimate  as woe left outer join woe.milestone milestone left outer join milestone.trackMilestone trackMilestone");
			query.append(" where woe.id in (select workOrderEstimate.id from Milestone m where m.egwStatus.code=?) ");
			paramList.add(getStatus().toUpperCase());
		}
		else{
			query.append("from WorkOrderEstimate  as woe left outer join woe.milestone milestone left outer join milestone.trackMilestone trackMilestone where woe.workOrder.egwStatus.code=? ");
			paramList.add("APPROVED");
			query.append(" and woe.id in (select workOrderEstimate.id from Milestone m1) ");
		}
		if(getExecDept()!=null && getExecDept()!= -1){		
			query.append(" and woe.estimate.executingDepartment.id=? ");
			paramList.add(getExecDept());
		}			
		if(getExpenditureType()!= -1){
			query.append(" and woe.estimate.type.id=? ");
			paramList.add(Long.valueOf(getExpenditureType()));
		}
		if(StringUtils.isNotBlank(getEstimatenumber())){
			query.append(" and UPPER(woe.estimate.estimateNumber) like '%'||?||'%'");
			paramList.add(StringUtils.trim(getEstimatenumber()).toUpperCase());
		}

		if(VIEW_MILESTONE.equalsIgnoreCase(source)){
			if(StringUtils.isNotBlank(milestoneStatus) && !milestoneStatus.equalsIgnoreCase("-1")){
				if(milestoneStatus.equalsIgnoreCase("Milestone Created") || milestoneStatus.equalsIgnoreCase("Milestone Tracked")){
					if(StringUtils.isNotBlank(status2) && !status2.equalsIgnoreCase("-1") ){
						if(milestoneStatus.equalsIgnoreCase("Milestone Created") && !status2.equalsIgnoreCase("CREATED")){
							query.append(" and milestone.egwStatus.code = ? and milestone.id not in (select tm.milestone.id from TrackMilestone tm ) ");
							paramList.add(status2);
						}
						else if(milestoneStatus.equalsIgnoreCase("Milestone Created") && status2.equalsIgnoreCase("CREATED")){
							query.append(" and milestone.egwStatus.code in ('CREATED','REJECTED','RESUBMITTED') and milestone.id not in (select tm.milestone.id from TrackMilestone tm ) ");
						}
						
						else if(milestoneStatus.equalsIgnoreCase("Milestone Tracked") && !status2.equalsIgnoreCase("CREATED")){
							query.append(" and trackMilestone.egwStatus.code = ? ");
							paramList.add(getStatus2());
						}
						else if(milestoneStatus.equalsIgnoreCase("Milestone Tracked") && status2.equalsIgnoreCase("CREATED")){
							query.append(" and trackMilestone.egwStatus.code in  ('CREATED','REJECTED','RESUBMITTED') ");
						}
					}
					if(milestoneStatus.equalsIgnoreCase("Milestone Created") && status2.equalsIgnoreCase("-1")){
						query.append(" and milestone.egwStatus.code in (?,?,?,?,?,?) and milestone.id not in (select tm.milestone.id from TrackMilestone tm )");
						paramList.add("NEW");
						paramList.add("CREATED");
						paramList.add("APPROVED");
						paramList.add("REJECTED");
						paramList.add("RESUBMITTED");
						paramList.add("CANCELLED");
					}
					if(milestoneStatus.equalsIgnoreCase("Milestone Tracked") && status2.equalsIgnoreCase("-1")){
						query.append(" and trackMilestone.egwStatus.code in (?,?,?,?,?,?) and trackMilestone.isProjectCompleted!=1  ");
						paramList.add("NEW");
						paramList.add("CREATED");
						paramList.add("APPROVED");
						paramList.add("REJECTED");
						paramList.add("RESUBMITTED");
						paramList.add("CANCELLED");
					}
				}	
				else{
						query.append(" and trackMilestone.isProjectCompleted = 1 and trackMilestone.egwStatus.code = ?");
						paramList.add("APPROVED");
				}
			}
		}
		
		if(SEARCH_ESTIMATE_FOR_MILESTONE.equalsIgnoreCase(source) || VIEW_MILESTONE.equalsIgnoreCase(source)){
			if(empId!=null && empId != -1){
				query.append(" and woe.estimate.estimatePreparedBy.idPersonalInformation = ?");
				paramList.add(empId);
			}
		}
		
		if(StringUtils.isNotBlank(getProjCode())){
			query.append(" and UPPER(woe.estimate.projectCode.code) like '%'||?||'%'");
			paramList.add(StringUtils.trim(getProjCode()).toUpperCase());
		}
		
		if(StringUtils.isNotBlank(getWorkOrderNo())){
			query.append(" and UPPER(woe.workOrder.workOrderNumber) like '%'||?||'%'");
			paramList.add(StringUtils.trim(getWorkOrderNo()).toUpperCase());
		}

		if(engineerIncharge!=null && engineerIncharge != -1){
			query.append(" and woe.workOrder.engineerIncharge.idPersonalInformation=?");
			paramList.add(engineerIncharge);
		}

		if(engineerIncharge2!=null && engineerIncharge2 != -1){
			query.append(" and woe.workOrder.engineerIncharge2.idPersonalInformation=?");
			paramList.add(engineerIncharge2);
		}

		if(estimates.getCategory()!=null){
			query.append(" and woe.estimate.category.id=?");
			paramList.add(estimates.getCategory().getId());
		}
		if(estimates.getParentCategory()!=null){
			query.append(" and woe.estimate.parentCategory.id=?");
			paramList.add(estimates.getParentCategory().getId());
		}
		
		if(fromDate!=null && toDate!=null && getFieldErrors().isEmpty()){
			query.append(" and woe.estimate.estimateDate between ? and ? ");
			paramList.add(fromDate);
			paramList.add(toDate);
		}
		
		if(VIEW_MILESTONE.equalsIgnoreCase(source)){
			if(estimates.getWard()!=null){
				query.append(" and woe.estimate.ward.id = ?  ");
				paramList.add(estimates.getWard().getId());
			}
		}
	
		queryAndParams.put("query", query.toString());
		queryAndParams.put("params", paramList);
		
		return queryAndParams;
	}
		
	public List<AbstractEstimate> getResults() {
		return results;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getExpenditureType() {
		return expenditureType;
	}

	public void setExpenditureType(Integer expenditureType) {
		this.expenditureType = expenditureType;
	}

	public String getEstimatenumber() {
		return estimateNumber;
	}

	public void setEstimatenumber(String estimatenumber) {
		this.estimateNumber = estimatenumber;
	}

	public AbstractEstimate getEstimates() {
		return estimates;
	}

	public void setEstimates(AbstractEstimate estimates) {
		this.estimates = estimates;
	}

	public Long getExecDept() {
		return execDept;
	}

	public void setExecDept(Long execDept) {
		this.execDept = execDept;
	}
	
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
		
	public List<String> getEstimateActions() { 
		String actions="";
		List<Role> copyEstActionRoles = new ArrayList<Role>();
		boolean allowCopyEst=false;
		List<String> actionList = new ArrayList<String>();
		String copyEstActionName;
		
		actions = worksService.getWorksConfigValue("ESTIMATES_SEARCH_ACTIONS");
		if(actions!=null) {
			actionList = new ArrayList(Arrays.asList(actions.split(",")));
			copyEstActionName = actionList.get(actionList.size() - 1);
	
			//get the roles for the Copy Estimate action
			Action copyEstaction = rbacService.getActionByName(copyEstActionName);
			copyEstActionRoles.addAll(copyEstaction.getRoles());
			
			//check if the userroles contains the copy estimate action roles
			for(Role copyEstrole : copyEstActionRoles) {
				if(roles.contains(copyEstrole)) {
					allowCopyEst = true;
					break;
				}
			}
			
			//To remove the last element(ie.Copy Estimate) from action list
			if(!allowCopyEst) {
				List<String> tempActionList = new ArrayList<String>();
				for(String action : actionList) {
					if(!action.equals(copyEstActionName)) {
						tempActionList.add(action);
					}
				}
				actionList.retainAll(tempActionList);
			} 
		}
		
		return actionList;
	}
	
	protected void populateCategoryList(
			AjaxEstimateAction ajaxEstimateAction, boolean categoryPopulated) {
		if (categoryPopulated) {
			ajaxEstimateAction.setCategory(estimates.getParentCategory().getId());
			ajaxEstimateAction.subcategories();
			addDropdownData("categoryList", ajaxEstimateAction.getSubCategories());		
		}
		else {
			addDropdownData("categoryList", Collections.emptyList());
		}
	}
	
	protected void populatePreparedByList(AjaxEstimateAction ajaxEstimateAction, boolean executingDeptPopulated){
		if (executingDeptPopulated) {
			ajaxEstimateAction.setExecutingDepartment(
					execDept);
			ajaxEstimateAction.usersInExecutingDepartment();
			
			addDropdownData(
					"preparedByList", ajaxEstimateAction.getUsersInExecutingDepartment());
		}
		else {
			addDropdownData("preparedByList", Collections.emptyList());
		}
	}
	
	public String displayEstimaeOrWpSearch(){
		if(StringUtils.isNotBlank(estimateOrWpSearchReq) && ("both".equalsIgnoreCase(estimateOrWpSearchReq) || 
			 "estimate".equalsIgnoreCase(estimateOrWpSearchReq))) {
			return RESULTS;
		}else{
		  return "wpSearch";	
		}
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public String getWpdate() {
		return wpdate;
	}

	public void setWpdate(String wpdate) {
		this.wpdate = wpdate;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public String getNegoCreatedBy() {
		return negoCreatedBy;
	}

	public void setNegoCreatedBy(String negoCreatedBy) {  
		this.negoCreatedBy = negoCreatedBy;
	}

	public String getStatusReq() {
		return statusReq;
	}

	public void setStatusReq(String statusReq) {
		this.statusReq = statusReq;
	}

	public Date getFromDate() {
		return fromDate; 
	}

	public Date getToDate() {
		return toDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}


	public String getEstimateOrWpSearchReq() {
		return estimateOrWpSearchReq;
	}

	public void setEstimateOrWpSearchReq(String estimateOrWpSearchReq) {
		this.estimateOrWpSearchReq = estimateOrWpSearchReq;
	}

	public boolean getSelectedorder() {
		return selectedorder;
	}

	public void setSelectedorder(boolean selectedorder) {
		this.selectedorder = selectedorder;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		// prepare the query string
		List<Object> paramList = new ArrayList<Object>();
		String query=null;
		String countQuery=null;
		String baseQuery =null;
		String OrderBy=null;
		StringBuilder sb = new StringBuilder(500);
		Map queryAndParms=null;
		
		if(SEARCH_ESTIMATE_FOR_MILESTONE.equalsIgnoreCase(source) || VIEW_MILESTONE.equalsIgnoreCase(source) 
				|| CANCEL_MILESTONE.equalsIgnoreCase(source)){
			queryAndParms=getEstimateForMilestoneQuery();
			paramList=(List<Object>)queryAndParms.get("params");
			query=(String) queryAndParms.get("query");
			//countQuery="select count(distinct woe.id) " + query;
			if(CANCEL_MILESTONE.equalsIgnoreCase(source) || SEARCH_ESTIMATE_FOR_MILESTONE.equalsIgnoreCase(source)){
				countQuery="select count(distinct woe.id) " + query;
			}
			if(CANCEL_MILESTONE.equalsIgnoreCase(source)){
				query="select distinct woe "+query;
			}
			if(VIEW_MILESTONE.equalsIgnoreCase(source)){
				countQuery="select count(woe.id) " + query;
				query="select woe , milestone , trackMilestone "+query;
			}
		}
		else{
			baseQuery = "from AbstractEstimate as ae where ae.parent is null  ";
			boolean isError=false;
			OrderBy="asc";
			if(selectedorder)
				OrderBy="desc";
			if(SEARCH_ESTIMATE_FOR_WO.equals(source)){
				if(StringUtils.isNotBlank(status)){
					sb.append(baseQuery);
					sb.append(" and ( ae.id in ( " +
							" ( select tr.tenderEstimate.abstractEstimate.id " +
							" from TenderResponse tr where tr.egwStatus.code=? ) ) or  ae.id in ( " +
							" select wd.estimate.id from WorksPackageDetails wd where wd.worksPackage.id in " +
							" (select tr.tenderEstimate.worksPackage.id from TenderResponse tr " +
							" where tr.tenderEstimate.abstractEstimate.id=null and  tr.tenderEstimate.worksPackage.id!=null " +
							" and tr.egwStatus.code=? )))" +
							" and ae.id not in (select wo.abstractEstimate.id from WorkOrder wo where wo.parent is null)"); 
					paramList.add(status);
					paramList.add(status);
				}
			}
			else{
				if(StringUtils.isNotBlank(getStatus()) && (getStatus().equals(AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString()) || getStatus().equals(AbstractEstimate.EstimateStatus.CANCELLED.toString()))){
					sb.append(baseQuery);
					sb.append("and ae.egwStatus.code = ?");
					paramList.add(getStatus());
				}
				else if(StringUtils.isNotBlank(getStatus()) && !getStatus().equals("-1")){
					sb.append(baseQuery);
					sb.append("and ae.egwStatus.code=? ");
					paramList.add(getStatus());
				}
				else if(StringUtils.isNotBlank(getStatus()) && getStatus().equals("-1")){
					sb.append(baseQuery);
					sb.append("and ae.egwStatus.code not in ('NEW')");
				}
			}

			if(getExecDept()!=null && getExecDept()!= -1){		
				sb.append(" and ae.executingDepartment.id= ? ");
				paramList.add(getExecDept());
			}			
			if(getExpenditureType()!= -1){
				sb.append(" and ae.type.id= ? ");
				paramList.add(Long.valueOf(getExpenditureType()));
			}
			if(StringUtils.isNotBlank(getEstimatenumber())){
				sb.append(" and ae.estimateNumber like ? ");
				paramList.add("%" +getEstimatenumber()+"%");
			}
			if(StringUtils.isNotBlank(projCode)){
				sb.append(" and ae.projectCode.code like ? ");
				paramList.add("%" +projCode+"%");
			}
			
			if(empId!=null && empId!=-1){
				sb.append(" and ae.estimatePreparedBy.idPersonalInformation=? ");
				paramList.add(empId);
			}
			if(estimates.getCategory()!=null){
				sb.append(" and ae.category.id= ? ");
				paramList.add(estimates.getCategory().getId());
			}
			if(estimates.getParentCategory()!=null){
				sb.append(" and ae.parentCategory.id= ? ");
				paramList.add(estimates.getParentCategory().getId());
			}
			if(estimates.getDescription()!=null && StringUtils.isNotBlank(estimates.getDescription())){
				sb.append(" and UPPER(ae.description) like ? ");
				paramList.add("%" +estimates.getDescription().toUpperCase()+"%");
			}
		
			if(StringUtils.isNotBlank(wpdate)){
				Date workspacDate=null;
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",new Locale("en","IN"));
				try{
					workspacDate = formatter.parse(wpdate);
				}catch(ParseException e){
					isError=true;
					logger.error("Date Conversion Error :"+e.getMessage());
					addFieldError("parse exception", "Date Conversion Error");  
				}
				sb.append(" and trunc(ae.state.createdDate)<=? ");
				paramList.add(workspacDate); 
			}
			
			if("wp".equals(source) && !isError){
				sb.append(" and ae.id not in(select tr.tenderEstimate.abstractEstimate.id from TenderResponse tr where " +
						"tr.egwStatus.code !='CANCELLED' and tr.tenderEstimate.abstractEstimate is not null and ae.id=tr.tenderEstimate.abstractEstimate.id)" +
						" and ae.id not in(select wpd.estimate.id from WorksPackageDetails wpd where wpd.estimate.id=ae.id " +
						" and wpd.worksPackage.egwStatus.code !='CANCELLED')"); 
			}		
			if("createNegotiationNew".equals(source) && !isError){
				sb.append(" and ae.id not in(select tr.tenderEstimate.abstractEstimate.id from TenderResponse tr where " +
						"tr.egwStatus.code !='CANCELLED' and tr.tenderEstimate.abstractEstimate is not null and ae.id=tr.tenderEstimate.abstractEstimate.id)" +
						" and ae.id not in(select tr.tenderEstimate.abstractEstimate.id from TenderResponse tr where " +
						"tr.egwStatus.code ='NEW' and tr.tenderEstimate.abstractEstimate.id is not null)" +
						" and ae.id not in(select wpd.estimate.id from WorksPackageDetails wpd where wpd.estimate.id=ae.id)");
			}			
			if(SEARCH_ESTIMATE_FOR_WO.equals(source)) {
				if(fromDate!=null && toDate==null) setToDate(new Date()); 
				if(toDate!=null && fromDate==null){
					sb.append(" and ae.estimateDate <= ? ");
					paramList.add(toDate);
				}
			}
			else{
				if(fromDate!=null && toDate==null)addFieldError("enddate",getText("search.endDate.null"));
				if(toDate!=null && fromDate==null)addFieldError("startdate",getText("search.startDate.null"));		
			}
		
			if(!DateUtils.compareDates(getToDate(),getFromDate())) addFieldError("enddate",getText("greaterthan.endDate.fromDate"));
			if(fromDate!=null && toDate!=null && getFieldErrors().isEmpty()){
				sb.append(" and ae.estimateDate between ? and ? ");
				paramList.add(fromDate); 
				paramList.add(toDate); 
			}
		
			if(sb.length()>0 && !isError && getFieldErrors().isEmpty() && !"menu".equalsIgnoreCase(option)){
				sb.append(" order by  ae.egwStatus.code  "+OrderBy+" ");
			}
		
			query = sb.toString();
			countQuery = "select count(distinct ae.id) " + query; 
			
		}

		
		return new SearchQueryHQL(query, countQuery, paramList);
	}
	
	public String viewMilestone(){

		return INDEX;
	}
	
	/* (non-Javadoc)
	 * @see org.egov.web.actions.SearchFormAction#search()
	 */
	@Override
	public String search() {
		boolean isError=false;

		if((SEARCH_ESTIMATE_FOR_MILESTONE.equalsIgnoreCase(source) 
				|| CANCEL_MILESTONE.equalsIgnoreCase(source)) && execDept==-1){
			addFieldError("execDept",getText("search.execDept.null"));
			isError=true;
		}
		if(fromDate!=null && toDate==null){
			addFieldError("enddate",getText("search.endDate.null"));
			isError=true;
		}
		if(toDate!=null && fromDate==null){
			addFieldError("startdate",getText("search.startDate.null"));		
			isError=true;
		}
		
		if(!DateUtils.compareDates(getToDate(),getFromDate())){
			addFieldError("enddate",getText("greaterthan.endDate.fromDate"));
			isError=true;
		}
		
		if(isError){
			return SUCCESS;
		}
		
		setPageSize(WorksConstants.PAGE_SIZE);
		String retVal = super.search();
		if(!SEARCH_ESTIMATE_FOR_MILESTONE.equalsIgnoreCase(source) && !VIEW_MILESTONE.equalsIgnoreCase(source) 
				&& !CANCEL_MILESTONE.equalsIgnoreCase(source)){
			populatePositionAndUserName();
		}
		if(SEARCH_ESTIMATE_FOR_MILESTONE.equalsIgnoreCase(source)){
			showOwnerName();
		}
		if(VIEW_MILESTONE.equalsIgnoreCase(source)){
			showOwnerNameForViewMilestone();
		}
		if((VIEW_MILESTONE.equalsIgnoreCase(source) || CANCEL_MILESTONE.equalsIgnoreCase(source)) 
			&& searchResult.getFullListSize()!=0){
				List<WorkOrderEstimate> woeList= new ArrayList<WorkOrderEstimate>();
				woeList.addAll((List<WorkOrderEstimate>) searchResult.getList());
				searchResult.getList().clear();
				searchResult.getList().addAll(woeList);
		}
		if( searchResult.getFullListSize()==0) {
			WorksPackage wp = null;
			if("wp".equals(source) && StringUtils.isNotBlank(getEstimatenumber()))
				wp = (WorksPackage) persistenceService.find("from WorksPackage wp where wp.id in (select wpd.worksPackage.id from WorksPackageDetails wpd where wpd.estimate.estimateNumber = ? ) and wp.egwStatus.code<>'CANCELLED'", getEstimatenumber());
			if(wp!=null){
				if("NEW".equalsIgnoreCase(wp.getEgwStatus().getCode())){
					PersonalInformation emp = employeeService.getEmployeeforPosition(wp.getCurrentState().getOwnerPosition());
					addFieldError("result not found", "Work package is already created for the Estimate with Work Package No "+wp.getWpNumber()+" dated on "+DateUtils.getFormattedDate(wp.getPackageDate(),"dd/MM/yyyy")+" and " +
							"it is drafts of "+emp.getEmployeeName());
				}
				else
					addFieldError("result not found", "Work package is already created for the Estimate with Work Package No: "+wp.getWpNumber()+" dated on "+DateUtils.getFormattedDate(wp.getPackageDate(),"dd/MM/yyyy")+" and it is in "+wp.getEgwStatus().getDescription()+" status");
			}
			else
				addFieldError("result not found", "No results found for search parameters");
		}
		return retVal;
	}
	
	@ValidationErrorPage (value=INDEX)
	public String cancelApprdMilestones() {
		StringBuilder estimateNum = new StringBuilder(200);
		String workOrderEstIdsStr[] = workOrdEstIds.split(",");
		PersonalInformation prsnlInfo = employeeService.getEmpForUserId(worksService.getCurrentLoggedInUserId());
		String empName = "";
		if(prsnlInfo.getEmployeeFirstName()!=null)
			empName=prsnlInfo.getEmployeeFirstName();
		if(prsnlInfo.getEmployeeLastName()!=null)
			empName=empName.concat(" ").concat(prsnlInfo.getEmployeeLastName()); 	
		
		StringBuilder cancelComments = new StringBuilder(200);
		if(cancelRemarks != null  && StringUtils.isNotBlank(cancelRemarks)) {
			 cancelComments.append(cancellationReason).append(" : ").append(cancelRemarks).append(". ").append(getText("milestone.cancel.cancelledby")).append(": ").append(empName);
		 } else {
			 cancelComments.append(cancellationReason).append(". ").append(getText("milestone.cancel.cancelledby")).append(": ").append(empName);
		 }
		
		for(String workOrderIdStr : workOrderEstIdsStr) {
			 WorkOrderEstimate woe = (WorkOrderEstimate) getPersistenceService().find("from WorkOrderEstimate woe where woe.id=?", Long.valueOf(workOrderIdStr));
			 List<MBHeader> mbHeaderList =(List<MBHeader>) getPersistenceService().findAllBy("from MBHeader where egBillregister is not null and egBillregister.status.code!=? and workOrderEstimate.id = ? ",ContractorBillRegister.BillStatus.CANCELLED.toString(),woe.getId());
			 if(mbHeaderList!=null && !mbHeaderList.isEmpty())
			 {
				 String billList="";
				 for(MBHeader mbh:mbHeaderList)
				 {
					 if(billList.equalsIgnoreCase(""))
						 billList = mbh.getEgBillregister().getBillnumber();
					 else
						 billList = billList+","+mbh.getEgBillregister().getBillnumber();
				 }
				 source=CANCEL_MILESTONE;
				 prepare();
				 throw new ValidationException(Arrays.asList(new ValidationError("error",getText("milestone.cancel.bill.validation",new String[]{woe.getEstimate().getEstimateNumber(),billList}))));
			 }
		}
		Position owner = prsnlInfo.getAssignment(new Date()).getPosition();
		State oldEndState = null;
		 for(String workOrderIdStr : workOrderEstIdsStr) {
			 WorkOrderEstimate woe = (WorkOrderEstimate) getPersistenceService().find("from WorkOrderEstimate woe where woe.id=?", Long.valueOf(workOrderIdStr));
			 
				 for(Milestone milestone : woe.getMilestone()) {
					if((WorksConstants.APPROVED).equalsIgnoreCase(milestone.getEgwStatus().getCode())) {
					 milestone.setEgwStatus(commonsService.getStatusByModuleAndCode(WorksConstants.MILESTONE_MODULE_KEY, 
							 WorksConstants.CANCELLED_STATUS));
					         //TODO - The setter methods of variables in State.java are protected. Need to alternative way to solve this issue.
					 	/*******oldEndState = milestone.getCurrentState();
						oldEndState.setCreatedBy(prsnlInfo.getUserMaster());
						oldEndState.setModifiedBy(prsnlInfo.getUserMaster());
						oldEndState.setCreatedDate(new Date());
						oldEndState.setModifiedDate(new Date());
						oldEndState.setOwner(owner);
						oldEndState.setValue(WorksConstants.CANCELLED_STATUS);
						oldEndState.setText1(cancelComments.toString());
						
						milestone.changeState("END", owner, null); *******/
					 
					 for(TrackMilestone tms : milestone.getTrackMilestone()) {
						 if(!(WorksConstants.CANCELLED_STATUS).equalsIgnoreCase(tms.getEgwStatus().getCode())) {
							 tms.setEgwStatus(commonsService.getStatusByModuleAndCode(WorksConstants.TRACK_MILESTONE_MODULE_KEY, 
									 WorksConstants.CANCELLED_STATUS));
							 
							 State currentState = tms.getCurrentState();
							//TODO - The setter methods of variables in State.java are protected. Need to alternative way to solve this issue.
							 /*******	 if(currentState.getPrevious() !=null 
									 && currentState.getPrevious().getValue().equals(WorksConstants.APPROVED)) {
								 currentState.setCreatedBy(prsnlInfo.getUserMaster());
								 currentState.setModifiedBy(prsnlInfo.getUserMaster());
								 currentState.setCreatedDate(new Date());
								 currentState.setModifiedDate(new Date());
								 currentState.setOwner(owner);
								 currentState.setValue(WorksConstants.CANCELLED_STATUS);
								 currentState.setText1(cancelComments.toString());
								 tms.changeState("END", owner, null);
							 } else {
								 tms.changeState(WorksConstants.CANCELLED_STATUS, currentState.getOwner(), cancelComments.toString());
								 tms.changeState("END", owner, null);
							 }**/
						 }
					 }
				 }
			 }
			if(!estimateNum.toString().contains(woe.getEstimate().getEstimateNumber())) {
				estimateNum.append(woe.getEstimate().getEstimateNumber()).append(",");
			}
		 }
		 messageKey=getText("milestone.cancel.success.msg",new String[]{estimateNum.toString()}); 
		 
   		 return "successMSCancel";
	 }
	 
	public List<String> getMilestoneActions() { 
		String actions = worksService.getWorksConfigValue("MILESTONE_SEARCH_ACTIONS");
		if(actions!=null)
		  return Arrays.asList(actions.split(","));
		return new ArrayList<String>();
	}
	
	//Get the login user department
	private void getLoginUserDept() {
		PersonalInformation LoggedInEmp = employeeService.getEmpForUserId(worksService.getCurrentLoggedInUserId());
		Assignment assignment= employeeService.getAssignmentByEmpAndDate(new Date(), LoggedInEmp.getIdPersonalInformation());
		setLoginUserDeptName(assignment.getDeptId().getName());
	}
	
	//Get the roles for the logged in user
	private void getLoginUserRoles() {
	        User user = userService.getUserById(worksService.getCurrentLoggedInUserId());
		if(user != null && !user.getRoles().isEmpty()) {
			roles.addAll(user.getRoles());
		}
	}
	
	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}

	public String getProjCode() {
		return projCode;
	}

	public void setProjCode(String projCode) {
		this.projCode = projCode;
	}

	public String getEstimateNumber() {
		return estimateNumber;
	}

	public void setEstimateNumber(String estimateNumber) {
		this.estimateNumber = estimateNumber;
	}

	public String getWorkOrderNo() {
		return workOrderNo;
	}

	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}

	public void setEngineerIncharge(Integer engineerIncharge) {
		this.engineerIncharge = engineerIncharge;
	}

	public Integer getEngineerIncharge2() {
		return engineerIncharge2;
	}

	public Integer getEngineerIncharge() {
		return engineerIncharge;
	}

	public void setEngineerIncharge2(Integer engineerIncharge2) {
		this.engineerIncharge2 = engineerIncharge2;
	}

	public Long getassignedTo1() {
		return assignedTo1;
	}

	public void setassignedTo1(Long assignedTo1) {
		this.assignedTo1 = assignedTo1;
	}

	public Long getAssignedTo2() {
		return assignedTo2;
	}

	public void setAssignedTo2(Long assignedTo2) {
		this.assignedTo2 = assignedTo2;
	}

	public Long getAssignedTo1() {
		return assignedTo1;
	}

	public void setAssignedTo1(Long assignedTo1) {
		this.assignedTo1 = assignedTo1;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

	public boolean isCheckWO() {
		return checkWO;
	}

	public void setCheckWO(boolean checkWO) {
		this.checkWO = checkWO;
	}

	public String getWorkOrdEstIds() {
		return workOrdEstIds;
	}

	public void setWorkOrdEstIds(String workOrdEstIds) {
		this.workOrdEstIds = workOrdEstIds;
	}

	public CommonsService getCommonsService() {
		return commonsService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public String getCancelRemarks() {
		return cancelRemarks;
	}

	public void setCancelRemarks(String cancelRemarks) {
		this.cancelRemarks = cancelRemarks;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getLoginUserDeptName() {
		return loginUserDeptName;
	}

	public void setLoginUserDeptName(String loginUserDeptName) {
		this.loginUserDeptName = loginUserDeptName;
	}

	public RbacService getRbacService() {
		return rbacService;
	}

	public void setRbacService(RbacService rbacService) {
		this.rbacService = rbacService;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getStatus2() {
		return status2;
	}

	public void setStatus2(String status2) {
		this.status2 = status2;
	}
	
   	public String getMilestoneStatus() {
		return milestoneStatus;
	}

	public void setMilestoneStatus(String milestoneStatus) {
		this.milestoneStatus = milestoneStatus;
	}
}
