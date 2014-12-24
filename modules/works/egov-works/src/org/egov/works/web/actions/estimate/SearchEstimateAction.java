package org.egov.works.web.actions.estimate;

import java.text.MessageFormat;
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
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.EgwTypeOfWork;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.DateUtils;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.model.masters.AccountCodePurpose;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.web.actions.SearchFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.EstimateRateContract;
import org.egov.works.models.estimate.WorkType;
import org.egov.works.models.estimate.AbstractEstimate.EstimateStatus;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.workorder.AjaxWorkOrderAction;

@ParentPackage("egov")
public class SearchEstimateAction extends SearchFormAction { 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String status;
	private String techSanctionNumber;
	private Integer expenditureType=-1;
	private String estimateNumber ="";
	private String source;
	private Integer execDept=-1;
	private AbstractEstimateService abstractEstimateService;
	private List<AbstractEstimate> results=new LinkedList<AbstractEstimate>();
	private AbstractEstimate estimates = new AbstractEstimate();
	private EmployeeService employeeService;
	private EisUtilService eisService;
	private Integer empId;
	private String wpdate;
	private String tenderFileDate;
	private String estimateType="notype";
	
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
	private boolean isSpillOverWorks;
	private boolean rateContract;
	private String contractor;
	private DepartmentService departmentService;
	private String contractorName;
	private EstimateRateContract estimateRateContract;
	private List<EstimateRateContract> estimateRateContractList = new ArrayList<EstimateRateContract>();
	private Integer parentCategory;
	private Integer category;
	private String description;
	private String projCode ="";
	private String workOrderNo ="";
	private Integer engineerIncharge;
	private Integer engineerIncharge2;
	private Long assignedTo1;
	private Long assignedTo2; 

	private static final String ASSIGNED_TO_LIST = "assignedToList";
	private static final String ASSIGNED_USER_LIST1 = "assignedUserList1";
	private static final String ASSIGNED_USER_LIST2 = "assignedUserList2";
	public static final String SEARCH_ESTIMATE_FOR_MILESTONE="searchEstimateForMilestone";
	public static final String VIEW_MILESTONE="viewMilestone";
	private transient ScriptService scriptExecutionService;
	
	public SearchEstimateAction() {
		addRelatedEntity("category", EgwTypeOfWork.class);
		addRelatedEntity("parentCategory", EgwTypeOfWork.class);
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
	
	public String getTechSanctionNumber() {
		return techSanctionNumber;
	}

	public void setTechSanctionNumber(String techSanctionNumber) {
		this.techSanctionNumber = techSanctionNumber;
	}

	public String execute(){
		return INDEX;
	}

	private void populateWorkOrderAssignedToList(AjaxWorkOrderAction ajaxWorkOrderAction, boolean executingDeptPopulated){
		if (executingDeptPopulated && execDept>0) {
			ajaxWorkOrderAction.setDepartmentName(departmentService.getDepartmentById(Long.valueOf(execDept)).getDeptName());
			ajaxWorkOrderAction.setScriptExecutionService(scriptExecutionService);
			ajaxWorkOrderAction.getDesignationByDeptId();
			addDropdownData(ASSIGNED_TO_LIST,ajaxWorkOrderAction.getWorkOrderDesigList());
		}
		else {
			addDropdownData(ASSIGNED_TO_LIST,Collections.EMPTY_LIST);
		}
	}
	
	private void populateWorkOrderUsersList1(AjaxWorkOrderAction ajaxWorkOrderAction, boolean desgId,boolean executingDeptPopulated){
		if (desgId && executingDeptPopulated && execDept>0) {
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

	public StringBuilder filterRcEstimate(){
		StringBuilder query = new StringBuilder(800);
		String baseQuery = "from EstimateRateContract as erc where erc.estimate.state.value<>'NEW' and erc.estimate.parent.id is null "+
							" and erc.id not in (select erc1.id from EstimateRateContract as erc1,WorkOrderEstimate as woe,RateContract rc1 where erc1.estimate.id=woe.estimate.id and rc1.id=erc1.rateContract.id and rc1.contractor.id=woe.workOrder.contractor.id and (woe.workOrder.egwStatus.code!='CANCELLED'))";
		boolean isError=false;
		String OrderBy="asc";
		if(selectedorder)
			OrderBy="desc";
		if(StringUtils.isNotBlank(techSanctionNumber)){
			baseQuery = "from EstimateRateContract as erc,  TechnicalSanction as ts where ts.abstractEstimate.id=erc.estimate.id and  erc.estimate.state.value<>'NEW' and erc.estimate.parent.id is null " +
			" and erc.id not in (select erc1.id from EstimateRateContract as erc1,WorkOrderEstimate as woe,RateContract rc1 where erc1.estimate.id=woe.estimate.id and rc1.id=erc1.rateContract.id and rc1.contractor.id=woe.workOrder.contractor.id and (woe.workOrder.egwStatus.code!='CANCELLED'))"+	
						" and ts.id in (select max(ts1.id) from TechnicalSanction as ts1,AbstractEstimate as ae1 where ts1.abstractEstimate.id=ae1.id group by ts1.abstractEstimate.id) " +
						" and ts.techSanctionNumber like '%"+techSanctionNumber.trim()+"%' ";
		}
			if(StringUtils.isNotBlank(getStatus()) && (getStatus().equals(AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString()) || getStatus().equals(AbstractEstimate.EstimateStatus.CANCELLED.toString()))){
				query.append(baseQuery);
				query.append("and erc.estimate.state.previous.value='" +getStatus()+"'");
			}
			else if(StringUtils.isNotBlank(getStatus()) && !getStatus().equals("-1")){
				query.append(baseQuery);
				query.append("and erc.estimate.state.value='" +getStatus()+"'");
			}
			else if(StringUtils.isNotBlank(getStatus()) && getStatus().equals("-1")){
				query.append(baseQuery);
				query.append("and erc.estimate.state.value not in ('NEW')");
			}

		if(getExecDept()!=null && getExecDept()!= -1){		
			query.append(" and erc.estimate.executingDepartment.id= " +getExecDept());
		}			
		if(getExpenditureType()!= -1){
			query.append(" and erc.estimate.type.id="+Long.valueOf(getExpenditureType()));
		}
		if(StringUtils.isNotBlank(getEstimatenumber())){
			query.append(" and erc.estimate.estimateNumber like '%" +getEstimatenumber()+"%'");
		}
		
		if(empId!=null && empId!=-1)
			query.append(" and erc.estimate.estimatePreparedBy.idPersonalInformation="+empId);
		if(estimates.getCategory()!=null)
			query.append(" and erc.estimate.category.id="+estimates.getCategory().getId());
		if(estimates.getParentCategory()!=null)
			query.append(" and erc.estimate.parentCategory.id="+estimates.getParentCategory().getId());
		if(StringUtils.isNotBlank(estimates.getDescription()))
			query.append(" and UPPER(erc.estimate.description) like '%"+estimates.getDescription().toUpperCase()+"%' ");
				
		if(StringUtils.isNotBlank(wpdate)){
			Date workspacDate=null;
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",new Locale("en","IN"));
			try{
				workspacDate = formatter.parse(wpdate);
			}catch(ParseException e){
				isError=true;
				addFieldError("parse exception", "Date Conversion Error");  
			}
			query.append(" and trunc(erc.estimate.state.previous.createdDate)<='"+DateUtils.getFormattedDate(workspacDate,dateFormat )+"' ");
		}
		
		if(StringUtils.isNotBlank(tenderFileDate)){
			Date fileDate=null;
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",new Locale("en","IN"));
			try{
				fileDate = formatter.parse(tenderFileDate);
			}catch(ParseException e){
				isError=true;
				addFieldError("parse exception", "Date Conversion Error");  
			}
			query.append(" and trunc(erc.estimate.state.previous.createdDate)<='"+DateUtils.getFormattedDate(fileDate,dateFormat )+"' ");
		}		

		if(fromDate!=null && toDate==null)addFieldError("enddate",getText("search.endDate.null"));
		if(toDate!=null && fromDate==null)addFieldError("startdate",getText("search.startDate.null"));		
		
		if(!DateUtils.compareDates(getToDate(),getFromDate())) addFieldError("enddate",getText("greaterthan.endDate.fromDate"));
		if(fromDate!=null && toDate!=null && getFieldErrors().isEmpty())
			query.append(" and erc.estimate.estimateDate between '"+DateUtils.getFormattedDate(fromDate,dateFormat )+"' " +

					"and '"+DateUtils.getFormattedDate(toDate,dateFormat )+"'");		
		
		if(StringUtils.isNotBlank(contractor)){
			query.append(" and erc.rateContract.contractor.name = '"+contractor+"' ") ;
		}
			
		if(query.length()>0 && !isError && getFieldErrors().isEmpty() && !"menu".equalsIgnoreCase(option)){
			query.append(" order by  erc.estimate.state.value  "+OrderBy+" ");
		}
		
		return query;
	}
	/**
	 * @return List of abstract estimates with "positionAndUserName" populated
	 */
	@SuppressWarnings(UNCHECKED)
	private void populatePositionAndUserName() {
		List<AbstractEstimate> abEstimateList = new LinkedList<AbstractEstimate>();
		List<EstimateRateContract> estimateRateContractList=new LinkedList<EstimateRateContract>();
		
		Iterator iter = searchResult.getList().iterator();
		boolean isArrayOfObjects = false;
		if(searchResult.getList()!=null && searchResult.getList().size()>0 && searchResult.getList().get(0) instanceof Object[])
			isArrayOfObjects = true;
		
		EstimateRateContract estimateRateContract;
		Object[] row = null;
		Object singleRow = null;
		AbstractEstimate estimate = null;
		while(iter.hasNext()) {
			if(isArrayOfObjects)
				row = (Object[])iter.next();
			else
				singleRow = (Object)iter.next();

			if(rateContract){
				if(isArrayOfObjects)
					estimateRateContract = (EstimateRateContract) row[0];
				else
					estimateRateContract = (EstimateRateContract) singleRow;
				String posName = estimateRateContract.getEstimate().getState().getOwner().getName();
				PersonalInformation emp = employeeService.getEmployeeforPosition(estimateRateContract.getEstimate().getState().getOwner());
				if(emp!=null)
					estimateRateContract.getEstimate().setPositionAndUserName(posName + " / " + emp.getEmployeeName());
				else
					estimateRateContract.getEstimate().setPositionAndUserName(posName);
				
				estimateRateContractList.add(estimateRateContract);
				
			}else{
				if(isArrayOfObjects)
					estimate = (AbstractEstimate) row[0];
				else
					estimate = (AbstractEstimate) singleRow;
				String posName = estimate.getState().getOwner().getName();
				PersonalInformation emp = employeeService.getEmployeeforPosition(estimate.getState().getOwner());
				if(emp!=null)
					estimate.setPositionAndUserName(posName + " / " +  emp.getEmployeeName());
				else
					estimate.setPositionAndUserName(posName );
				
				abEstimateList.add(estimate);
			}
		}

		searchResult.getList().clear();

		if(rateContract){
			HashSet<EstimateRateContract> uniqueEstimateRateContractList=new HashSet<EstimateRateContract>(estimateRateContractList);
			searchResult.getList().addAll(uniqueEstimateRateContractList);
		}else{
			HashSet<AbstractEstimate> uniqueAbsEstimateList=new HashSet<AbstractEstimate>(abEstimateList);
			searchResult.getList().addAll(uniqueAbsEstimateList);
		}
	}
	
	public String printpage(){
		search();
		return "print";
	}	
		
	protected String filterMyEstimates(){
		String namedQuery = getNamedQuery();
		//hack to workaround int ids in UserImpl
		Object currentUserId=getCurrentUserId();
		if(EstimateStatus.CREATED.name().equals(status)){
			currentUserId=getCurrentUserIdAsLong();
		}
		if(namedQuery!=null){
			results=abstractEstimateService.findAllByNamedQuery(namedQuery, currentUserId);
		}
		return RESULTS;
	}
	
	private int getCurrentUserId() {
		return Integer.parseInt(EGOVThreadLocals.getUserId());
	}
	
	private long getCurrentUserIdAsLong() {
		return Long.parseLong(EGOVThreadLocals.getUserId());
	}
	
	protected String getNamedQuery(){
		try{
			return MessageFormat.format("MY_{0}_ESTIMATES", EstimateStatus.valueOf(status));
		}catch(IllegalArgumentException e){
			return null;
		}
	}
	
	@SuppressWarnings(UNCHECKED)
	public List getEstimateStatuses() {
		return persistenceService.findAllBy("from EgwStatus s where moduletype=? and code<>'NEW' order by orderId",AbstractEstimate.class.getSimpleName());
	}
	
	@SuppressWarnings(UNCHECKED)
	public void prepare(){
		AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
		AjaxWorkOrderAction ajaxWorkOrderAction = new AjaxWorkOrderAction();
		ajaxWorkOrderAction.setPersistenceService(getPersistenceService());
		ajaxWorkOrderAction.setEisService(eisService);
		ajaxWorkOrderAction.setPersonalInformationService(personalInformationService);
		ajaxEstimateAction.setPersistenceService(getPersistenceService());
		ajaxEstimateAction.setEmployeeService(employeeService);
		ajaxEstimateAction.setPersonalInformationService(personalInformationService);
		super.prepare();
		setupDropdownDataExcluding();
		List<DepartmentImpl> values = (List<DepartmentImpl>) getPersistenceService().findAllBy("from DepartmentImpl dt");
		addDropdownData("executingDepartmentList",values);
		List<WorkType> worktypeList = (List<WorkType>) getPersistenceService().findAllBy("from WorkType dt");
		addDropdownData("typeList", worktypeList);
		addDropdownData("parentCategoryList", getPersistenceService().findAllBy("from EgwTypeOfWork etw1 where etw1.parentid is null"));
		populateCategoryList(ajaxEstimateAction, estimates.getParentCategory() != null);
		
		populatePreparedByList(ajaxEstimateAction,execDept != null);
		if("wp".equals(source) || "tenderFile".equals(source)  || "searchRCEstimate".equals(source))
			setStatus(AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString());		
/*		if(SEARCH_ESTIMATE_FOR_WO.equals(source)){
			String status=worksService.getWorksConfigValue("NEGOTIATIONSTMT_WO_STATUS");
			if(StringUtils.isNotBlank(status)){ 
				setStatus(status); 
			}
			setToDate(new Date());
			perform();			
		}		
		
		if("createNegotiationNew".equalsIgnoreCase(source)){
			perform();	
		}	
*/		if(SEARCH_ESTIMATE_FOR_MILESTONE.equalsIgnoreCase(source) || VIEW_MILESTONE.equalsIgnoreCase(source)){
			populateWorkOrderAssignedToList(ajaxWorkOrderAction,execDept!=null) ;
			populateWorkOrderUsersList1(ajaxWorkOrderAction,assignedTo1!=null,execDept!=null) ;
			populateWorkOrderUsersList2(ajaxWorkOrderAction,assignedTo2!=null,execDept!=null) ;
		}

		if("createServiceOrderNew".equalsIgnoreCase(source)){
			List<DepartmentImpl> deptValues = (List<DepartmentImpl>) persistenceService.findAllBy("select distinct ae.executingDepartment" +
					 " from AbstractEstimate ae");
			addDropdownData("executingDepartmentList",deptValues);
			setStatus(AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString());		
			estimateOrWpSearchReq=worksService.getWorksConfigValue("ESTIMATE_OR_WP_SEARCH_REQ"); 
		}	

		if("cancelEstimate".equals(source)){
			status="ADMIN_SANCTIONED";
		}

	}
	
	@SuppressWarnings(UNCHECKED)
/*	public void perform(){
			if(abstractEstimateService.getLatestAssignmentForCurrentLoginUser()!=null) {
				execDept=abstractEstimateService.getLatestAssignmentForCurrentLoginUser().getDeptId().getId();		
			}
			negoCreatedBy=worksService.getWorksConfigValue("TENDER_NEGOTIATION_CREATED_BY_SELECTION");
			
			if(SEARCH_ESTIMATE_FOR_WO.equals(source)){
				List<DepartmentImpl> deptValues = (List<DepartmentImpl>) getPersistenceService().findAllBy("select distinct ae.executingDepartment" +
						" from AbstractEstimate ae where ae.id in ("   +
						" ( select tr.tenderEstimate.abstractEstimate.id " +
						" from TenderResponse tr where tr.state.previous.value='" +status+"') ) or  ae.id in ( " +
						" select wd.estimate.id from WorksPackageDetails wd where wd.worksPackage.id in " +
						" (select tr.tenderEstimate.worksPackage.id from TenderResponse tr " +
						" where tr.tenderEstimate.abstractEstimate.id=null and  tr.tenderEstimate.worksPackage.id!=null " +
						" and tr.state.previous.value='" +status+"'))" +
						" and ae.id not in (select wo.abstractEstimate.id from WorkOrder wo)"); 
				addDropdownData("executingDepartmentList",deptValues);
			}
			else{			
			estimateOrWpSearchReq=worksService.getWorksConfigValue("ESTIMATE_OR_WP_SEARCH_REQ"); 
			 
			statusReq=worksService.getWorksConfigValue("ESTIMATE_STATUS");
			
			List<DepartmentImpl> deptValues = (List<DepartmentImpl>) getPersistenceService().findAllBy("select distinct ae.executingDepartment" +
					" from AbstractEstimate ae where ae.state.previous.value='" +statusReq+"'"+
			" and ae.id not in(select " +
			"tr.tenderEstimate.abstractEstimate.id from TenderResponse tr where " +
			"tr.state.previous.value !='CANCELLED' and ae.id=tr.tenderEstimate.abstractEstimate.id)" +
	" and ae.id not in(select wpd.estimate.id from WorksPackageDetails wpd where wpd.estimate.id=ae.id)");
			
			// Added to list all the departments
			addDropdownData("executingDepartmentList",departmentService.getAllDepartments());
			if(StringUtils.isNotBlank(statusReq)){
				setStatus(statusReq);
			}
		}
	}
*/
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

	public Integer getExecDept() {
		return execDept;
	}

	public void setExecDept(Integer execDept) {
		this.execDept = execDept;
	}
	
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	/**
	 * @param results
	 * @return
	 * @deprecated Not being used now. To be removed after thorough testing
	 */
	protected List<AbstractEstimate> getPositionAndUser(List<AbstractEstimate> results){
		List<AbstractEstimate> abstractEstimateList = new ArrayList<AbstractEstimate>();
		for(AbstractEstimate abstractEstimate :results){
			PersonalInformation emp = employeeService.getEmployeeforPosition(abstractEstimate.getCurrentState().getOwner());
			abstractEstimate.setPositionAndUserName(abstractEstimate.getCurrentState().getOwner().getName()+" / "+emp.getEmployeeName());
			abstractEstimateList.add(abstractEstimate);
		}
		return abstractEstimateList;
	}
	
	public List<String> getEstimateActions() { 
		String actions = worksService.getWorksConfigValue("ESTIMATES_SEARCH_ACTIONS");
		if(actions!=null)
		  return Arrays.asList(actions.split(","));
		return new ArrayList<String>();
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
			ajaxEstimateAction.setWorksService(worksService);
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

	private Map getEstimateForMilestoneQuery(){
		StringBuffer query = new StringBuffer(700);
		List<Object> paramList = new ArrayList<Object>();
		HashMap<String,Object> queryAndParams=new HashMap<String,Object>();
		if(SEARCH_ESTIMATE_FOR_MILESTONE.equalsIgnoreCase(source)){
			query.append("from WorkOrderEstimate  as woe where woe.workOrder.state.previous.value=? ");
			paramList.add("APPROVED");
			query.append(" and woe.id not in (select m.workOrderEstimate.id from Milestone as m where m.egwStatus.code not in (?,?))");
			paramList.add("APPROVED");
			paramList.add("CANCELLED");
			query.append(" and woe.id not in (select tm.milestone.workOrderEstimate.id from TrackMilestone as tm where tm.egwStatus.code not in (?,?) or (tm.isProjectCompleted=? and tm.egwStatus.code<>?))");
			paramList.add("APPROVED");
			paramList.add("CANCELLED");
			paramList.add(Boolean.TRUE);
			paramList.add("CANCELLED");
		}
		else{
			query.append("from WorkOrderEstimate  as woe left outer join woe.milestone milestone left outer join milestone.trackMilestone trackMilestone where woe.workOrder.state.previous.value=? ");
			paramList.add("APPROVED");
			query.append(" and woe.id not in (select workOrderEstimate.id from Milestone m where m.egwStatus.code=?) ");
			paramList.add("NEW");
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
		
	
		queryAndParams.put("query", query.toString());
		queryAndParams.put("params", paramList);
		
		return queryAndParams;
	}
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		// prepare the query string
		String query =null;
		String countQuery = null;
		StringBuilder sb = new StringBuilder(500);
		Map queryAndParms=null;
		List<Object> paramList = new ArrayList<Object>();

		if(SEARCH_ESTIMATE_FOR_MILESTONE.equalsIgnoreCase(source) || VIEW_MILESTONE.equalsIgnoreCase(source)){
			queryAndParms=getEstimateForMilestoneQuery();
			paramList=(List<Object>)queryAndParms.get("params");
			query=(String) queryAndParms.get("query");
			countQuery="select count(distinct woe.id) " + query;
			if(VIEW_MILESTONE.equalsIgnoreCase(source)){
				query="select distinct woe "+query;
			}
		}
		else if(rateContract){
			query=filterRcEstimate().toString();
			countQuery="select count(distinct erc.id) " + query;
		}else{
			String baseQuery = "from AbstractEstimate as ae where  ae.state.value<>'NEW' and ae.parent.id is null ";
			boolean isError=false;
			String OrderBy="asc";
			if(selectedorder)
				OrderBy="desc";
			if(StringUtils.isNotBlank(techSanctionNumber)){
				baseQuery = "from AbstractEstimate as ae,  TechnicalSanction as ts where ts.abstractEstimate.id=ae.id and  ae.state.value<>'NEW' and ae.parent.id is null " +
							" and ts.id in (select max(ts1.id) from TechnicalSanction as ts1,AbstractEstimate as ae1 where ts1.abstractEstimate.id=ae1.id group by ts1.abstractEstimate.id) " +
								" and ts.techSanctionNumber like '%"+techSanctionNumber.trim()+"%' ";
			}
/*		if(SEARCH_ESTIMATE_FOR_WO.equals(source)){
			if(StringUtils.isNotBlank(status)){
				sb.append(baseQuery);
				sb.append(" and ( ae.id in ( " +
						" ( select tr.tenderEstimate.abstractEstimate.id " +
						" from TenderResponse tr where tr.state.previous.value='" +status+"') ) or  ae.id in ( " +
						" select wd.estimate.id from WorksPackageDetails wd where wd.worksPackage.id in " +
						" (select tr.tenderEstimate.worksPackage.id from TenderResponse tr " +
						" where tr.tenderEstimate.abstractEstimate.id=null and  tr.tenderEstimate.worksPackage.id!=null " +
						" and tr.state.previous.value='" +status+"')))" +
						" and ae.id not in (select wo.abstractEstimate.id from WorkOrder wo)"); 
			}
		}else*/ if("createServiceOrderNew".equals(source)){
					AccountCodePurpose purpose = (AccountCodePurpose)persistenceService.findAllBy(" from AccountCodePurpose where name=?", "Architect Fee Code").get(0);
					sb.append("from AbstractEstimate as ae,  OverheadValue as ov where  ov.abstractEstimate.id=ae.id ");
					sb.append("and ae.state.previous.value='" +AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString()+"' and ov.amount > 0");
					if(purpose!=null){
						sb.append(" and ov.overhead.account.purposeId= " +purpose.getId());
				}
				}
				else{
					if(StringUtils.isNotBlank(getStatus()) && (getStatus().equals(AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString()) || getStatus().equals(AbstractEstimate.EstimateStatus.CANCELLED.toString()))){
						sb.append(baseQuery);
						sb.append("and ae.state.previous.value='" +getStatus()+"'");
					}
					else if(StringUtils.isNotBlank(getStatus()) && !getStatus().equals("-1")){
						sb.append(baseQuery);
						sb.append("and ae.state.value='" +getStatus()+"'");
					}
					else if(StringUtils.isNotBlank(getStatus()) && getStatus().equals("-1")){
						sb.append(baseQuery);
						sb.append("and ae.state.value not in ('NEW')");
					}
				}

			if(getExecDept()!=null && getExecDept()!= -1){		
				sb.append(" and ae.executingDepartment.id= " +getExecDept());
			}			
			if(getExpenditureType()!= -1){
				sb.append(" and ae.type.id="+Long.valueOf(getExpenditureType()));
			}
			if(StringUtils.isNotBlank(getEstimatenumber())){
				sb.append(" and ae.estimateNumber like '%" +getEstimatenumber()+"%'");
			}
		
			if(empId!=null && empId!=-1)
				sb.append(" and ae.estimatePreparedBy.idPersonalInformation="+empId);
			if(estimates.getCategory()!=null)
				sb.append(" and ae.category.id="+estimates.getCategory().getId());
			if(estimates.getParentCategory()!=null)
				sb.append(" and ae.parentCategory.id="+estimates.getParentCategory().getId());
			if(estimates.getDescription()!=null)
				sb.append(" and UPPER(ae.description) like '%"+estimates.getDescription().toUpperCase()+"%' ");
				
			if(StringUtils.isNotBlank(wpdate)){
				Date workspacDate=null;
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",new Locale("en","IN"));
				try{
					workspacDate = formatter.parse(wpdate);
				}catch(ParseException e){
					isError=true;
					addFieldError("parse exception", "Date Conversion Error");  
				}
				sb.append(" and trunc(ae.state.previous.createdDate)<='"+DateUtils.getFormattedDate(workspacDate,dateFormat )+"' ");
			}
		
			if(StringUtils.isNotBlank(tenderFileDate)){
				Date fileDate=null;
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",new Locale("en","IN"));
				try{
					fileDate = formatter.parse(tenderFileDate);
				}catch(ParseException e){
					isError=true;
					addFieldError("parse exception", "Date Conversion Error");  
				}
				sb.append(" and trunc(ae.state.previous.createdDate)<='"+DateUtils.getFormattedDate(fileDate,dateFormat )+"' ");
			}		
		
			if("wp".equals(source) && !isError){
				sb.append(" and ae.id not in(select tr.tenderEstimate.abstractEstimate.id from TenderResponse tr where " +
						"tr.state.previous.value !='CANCELLED' and ae.id=tr.tenderEstimate.abstractEstimate.id)" +
						" and ae.id not in(select wpd.estimate.id from WorksPackageDetails wpd where wpd.estimate.id=ae.id " +
				" and wpd.worksPackage.state.previous.value !='CANCELLED')");
			}
			if("tenderFile".equals(source) && !isError){
					sb.append(" and ae.id not in(select tfd.abstractEstimate.id from TenderFileDetail tfd where tfd.abstractEstimate.id=ae.id " +
							" and tfd.tenderFile.egwStatus.code !='CANCELLED') and isSpillOverWorks= "+isSpillOverWorks+" " +
					" and ae.id not in (select distinct erc.estimate.id from EstimateRateContract erc)");
			}


/*		if("tenderFile".equals(source) && !isError){
			sb.append(" and ae.id not in(select tr.tenderEstimate.abstractEstimate.id from TenderResponse tr where " +
					"tr.state.previous.value !='CANCELLED' and ae.id=tr.tenderEstimate.abstractEstimate.id)" +
			" and ae.id not in(select tfd.abstractEstimate.id from TenderFileDetail tfd where tfd.abstractEstimate.id=ae.id " +
			" and tfd.tenderFile.egwStatus.code !='CANCELLED') and isSpillOverWorks= "+isSpillOverWorks);
		}
		if("createNegotiationNew".equals(source) && !isError){
			sb.append(" and ae.id not in(select tr.tenderEstimate.abstractEstimate.id from TenderResponse tr where " +
					"tr.state.previous.value !='CANCELLED' and ae.id=tr.tenderEstimate.abstractEstimate.id)" +
					" and ae.id not in(select tr.tenderEstimate.abstractEstimate.id from TenderResponse tr where " +
					"tr.state.value ='NEW' and tr.tenderEstimate.abstractEstimate.id is not null)" +
			" and ae.id not in(select wpd.estimate.id from WorksPackageDetails wpd where wpd.estimate.id=ae.id)");
			sb.append("and ae.parent is null");
		}	
*/			if( ("createServiceOrderNew".equals(source) ) && !isError){
				sb.append(" and ae.id not in(select soobj.abstractEstimate.id from ServiceOrderObjectDetail soobj where soobj.serviceOrder").
				append(".status.id <> (select id from EgwStatus where moduletype='SERVICEORDER' and description='ServiceOrderCancelled'))");
			}	
/*		if(SEARCH_ESTIMATE_FOR_WO.equals(source)) {
			if(fromDate!=null && toDate==null) setToDate(new Date()); 
			if(toDate!=null && fromDate==null){
					sb.append(" and ae.estimateDate <= '"+DateUtils.getFormattedDate(toDate,dateFormat )+"' ");
			}
		}
		else{*/
			if(fromDate!=null && toDate==null)addFieldError("enddate",getText("search.endDate.null"));
			if(toDate!=null && fromDate==null)addFieldError("startdate",getText("search.startDate.null"));		
//		}
		
			if(!DateUtils.compareDates(getToDate(),getFromDate())) addFieldError("enddate",getText("greaterthan.endDate.fromDate"));
			if(fromDate!=null && toDate!=null && getFieldErrors().isEmpty())
				sb.append(" and ae.estimateDate between '"+DateUtils.getFormattedDate(fromDate,dateFormat )+"' " +
					"and '"+DateUtils.getFormattedDate(toDate,dateFormat )+"'");		

			if(sb.length()>0 && !isError && getFieldErrors().isEmpty() && !"menu".equalsIgnoreCase(option)){
				sb.append(" order by  ae.state.value  "+OrderBy+" ");
			}
		
			query = sb.toString();
			countQuery = "select count(distinct ae.id) " + query;
		}
		if(SEARCH_ESTIMATE_FOR_MILESTONE.equalsIgnoreCase(source) || VIEW_MILESTONE.equalsIgnoreCase(source)){
			return new SearchQueryHQL(query, countQuery, paramList);
		}
		else {
			return new SearchQueryHQL(query, countQuery, null);
		}
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

		if(SEARCH_ESTIMATE_FOR_MILESTONE.equalsIgnoreCase(source) && execDept==-1){
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
		if(!SEARCH_ESTIMATE_FOR_MILESTONE.equalsIgnoreCase(source) && !VIEW_MILESTONE.equalsIgnoreCase(source)){
			populatePositionAndUserName();
		}
		if(VIEW_MILESTONE.equalsIgnoreCase(source) && searchResult.getFullListSize()!=0){
			ArrayList<WorkOrderEstimate> woeList=new ArrayList<WorkOrderEstimate>((ArrayList<WorkOrderEstimate>)searchResult.getList());
			searchResult.getList().clear();
			searchResult.getList().addAll(woeList);
		}
		if( searchResult.getFullListSize()==0) {
			addFieldError("result not found", "No results found for search parameters");
		}

		return retVal;
	}
	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}

	public String getTenderFileDate() {
		return tenderFileDate;
	}

	public void setTenderFileDate(String tenderFileDate) {
		this.tenderFileDate = tenderFileDate;
	}

	public boolean getIsSpillOverWorks() {
		return isSpillOverWorks;
	}

	public void setIsSpillOverWorks(boolean isSpillOverWorks) {
		this.isSpillOverWorks = isSpillOverWorks;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public boolean isRateContract() {
		return rateContract;
	}

	public void setRateContract(boolean rateContract) {
		this.rateContract = rateContract;
	}

	public String getContractor() {
		return contractor;
	}

	public void setContractor(String contractor) {
		this.contractor = contractor;
	}

	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}

	public String getContractorName() {
		return contractorName;
	}
	
	public List<EstimateRateContract> getEstimateRateContractList() {
		return estimateRateContractList;
	}

	public void setEstimateRateContractList(List<EstimateRateContract> estimateRateContractList) {
		this.estimateRateContractList = estimateRateContractList;
	}
	public EstimateRateContract getEstimateRateContract() {
		return estimateRateContract;
	}

	public void setEstimateRateContract(EstimateRateContract estimateRateContract) {
		this.estimateRateContract = estimateRateContract;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(Integer parentCategory) {
		this.parentCategory = parentCategory;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public Integer getExpenditureTypeid() {
		return expenditureTypeid;
	}

	public void setExpenditureTypeid(Integer expenditureTypeid) {
		this.expenditureTypeid = expenditureTypeid;
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
	public String getProjCode() {
		return projCode;
	}

	public void setProjCode(String projCode) {
		this.projCode = projCode;
	}

	public String getEstimateType() {
		return estimateType;
	}

	public void setEstimateType(String estimateType) {
		this.estimateType = estimateType;
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}
	
}