package org.egov.works.web.actions.tender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.utils.DateUtils;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.SearchFormAction;
import org.egov.works.models.tender.WorksPackage;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;

public class SearchWorksPackageAction extends SearchFormAction { 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private WorksPackage worksPackage = new WorksPackage();
	private List<WorksPackage> results=new LinkedList<WorksPackage>();
	private EmployeeService employeeService;
	private CommonsService commonsService;
	private Date fromDate;
	private Date toDate;
	private String status;
	private String setStatus;
	
	private AbstractEstimateService abstractEstimateService;
	private WorksService worksService; 
	private String negoCreatedBy;
	private String statusReq;
	private Integer execDept=-1;
	private String source;
	private List<String> worksPackageActions;
	private String option="";
	private String estimateOrWpSearchReq;
	private static final String OBJECT_TYPE = "WorksPackage";
	
	public Object getModel() {
		return worksPackage;
	}

	public SearchWorksPackageAction()
	{
		addRelatedEntity("userDepartment", DepartmentImpl.class);
	}
	
	public void prepare(){
		super.prepare();
		setupDropdownDataExcluding();
		if("createNegotiationForWP".equalsIgnoreCase(source)){
			perform();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void perform(){
		
		if(abstractEstimateService.getLatestAssignmentForCurrentLoginUser()!=null) {
			execDept=abstractEstimateService.getLatestAssignmentForCurrentLoginUser().getDeptId().getId();			
		}
		negoCreatedBy=worksService.getWorksConfigValue("TENDER_NEGOTIATION_CREATED_BY_SELECTION");	
		statusReq=worksService.getWorksConfigValue("WorksPackage.laststatus");
		estimateOrWpSearchReq=worksService.getWorksConfigValue("ESTIMATE_OR_WP_SEARCH_REQ");
		
		List<DepartmentImpl> deptValues = (List<DepartmentImpl>) getPersistenceService().findAllBy("select distinct userDepartment "+
				"from WorksPackage wp where wp.id in(select stat.objectId from SetStatus stat where stat.egwStatus.code='" +statusReq+"')"+
				" and wp.id not in(select " +
				"tr.tenderEstimate.worksPackage.id from TenderResponse tr where " +
				"tr.state.previous.value !='CANCELLED' and wp.id=tr.tenderEstimate.worksPackage.id)");
		addDropdownData("userDepartmentList",deptValues);
		if(StringUtils.isNotBlank(statusReq)){
			setStatus(statusReq);
		}		
	}
	
	public String execute(){
		return INDEX;
	}	
	
	@SuppressWarnings("unchecked")
	protected void getPositionAndUser(){
		List<WorksPackage> wpList = new ArrayList<WorksPackage>();
		
		Iterator i= searchResult.getList().iterator();
		
		while(i.hasNext()){
			WorksPackage wp=(WorksPackage) i.next();
			
			if(wp.getCurrentState()!=null){
				PersonalInformation emp = employeeService.getEmployeeforPosition(wp.getCurrentState().getOwner());
				wp.setEmployeeName(emp.getEmployeeName());
				wpList.add(wp);
				String approved = getApprovedValue();
				String actions = worksService.getWorksConfigValue("WORKSPACKAGE_SEARCH_ACTIONS");
				worksPackageActions = new ArrayList<String>();
				if(StringUtils.isNotBlank(actions)){					
					worksPackageActions.addAll(Arrays.asList(actions.split(",")));					
				}
				String setStatus = worksService.getWorksConfigValue("WORKS_SETSTATUS_VALUE");
				if(StringUtils.isNotBlank(approved) && wp.getCurrentState().getPrevious()!=null &&
						approved.equals(wp.getCurrentState().getPrevious().getValue()) && StringUtils.isNotBlank(setStatus)){					
						worksPackageActions.add(setStatus);
				}
			}
		}
		searchResult.getList().clear();
		searchResult.getList().addAll(wpList);
		if(worksPackageActions==null)worksPackageActions = new ArrayList<String>();
	}

	public String getApprovedValue() {
		return worksService.getWorksConfigValue("WORKS_PACKAGE_STATUS");
	}

	public List<String> getWorksPackageActions() { 
			return worksPackageActions;
	}
	
	public List<EgwStatus> getPackageStatuses() {
		String wpStatus = worksService.getWorksConfigValue("WP_STATUS_SEARCH");
		String status = worksService.getWorksConfigValue("WorksPackage.setstatus");
		String lastStatus = worksService.getWorksConfigValue("WorksPackage.laststatus");  
		List<String> statList = new ArrayList<String>();
		if(StringUtils.isNotBlank(wpStatus))
		   statList.add(wpStatus);
		if(StringUtils.isNotBlank(status) && StringUtils.isNotBlank(lastStatus)){
			List<String> statusList = Arrays.asList(status.split(","));
			for(String stat: statusList){
				if(stat.equals(lastStatus)){
					statList.add(stat);
					break;
				}
				else{
					statList.add(stat);
				}
			}
		}
		return commonsService.getStatusListByModuleAndCodeList(WorksPackage.class.getSimpleName(), statList);
	}
	
	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<WorksPackage> getResults() {
		return results;
	}

	public void setResults(List<WorksPackage> results) {
		this.results = results;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	public void setModel(WorksPackage worksPackage) {
		this.worksPackage = worksPackage;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public String getNegoCreatedBy() {
		return negoCreatedBy;
	}

	public String getStatusReq() {
		return statusReq;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public void setNegoCreatedBy(String negoCreatedBy) {
		this.negoCreatedBy = negoCreatedBy;
	}

	public void setStatusReq(String statusReq) {
		this.statusReq = statusReq;
	}

	public Integer getExecDept() {
		return execDept;
	}

	public void setExecDept(Integer execDept) {
		this.execDept = execDept;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSetStatus() {
		return setStatus;
	}

	public void setSetStatus(String setStatus) {
		this.setStatus = setStatus;
	}

	public String getEstimateOrWpSearchReq() {
		return estimateOrWpSearchReq;
	}

	public void setEstimateOrWpSearchReq(String estimateOrWpSearchReq) {
		this.estimateOrWpSearchReq = estimateOrWpSearchReq;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		StringBuilder sb = new StringBuilder(300);

		if("createNegotiationForWP".equals(source)){
			sb.append("from WorksPackage as wp where wp.id in(select stat.objectId from " +
					"SetStatus stat where stat.egwStatus.code='" +getStatus()+"' and stat.id = (select" +
							" max(stat1.id) from SetStatus stat1 where wp.id=stat1.objectId)) ");
			
		}else{
			if(StringUtils.isNotBlank(getStatus()) && (getStatus().equals
					(WorksPackage.WorkPacakgeStatus.APPROVED.toString()) || 
					getStatus().equals(WorksPackage.WorkPacakgeStatus.CANCELLED.toString()))){				
				sb.append("from WorksPackage as wp where wp.state.previous.value='" +getStatus()+"' and " +
				" wp.id not in (select objectId from SetStatus where objectType='"+OBJECT_TYPE+"')");
			}
			else if(StringUtils.isNotBlank(getStatus()) && !getStatus().equals("-1")){ 
				sb.append("from WorksPackage as wp where wp.state.value='" +getStatus()+"' or wp.id in(select stat.objectId from " +
						"SetStatus stat where stat.egwStatus.code='" +getStatus()+"' and stat.id = (select" +
				" max(stat1.id) from SetStatus stat1 where wp.id=stat1.objectId) and stat.objectType='"+OBJECT_TYPE+"') ");
			} 
			else if(StringUtils.isNotBlank(getStatus()) && getStatus().equals("-1")){
				sb.append("from WorksPackage as wp where wp.state is not null");
			}
		}
		
		if(worksPackage.getUserDepartment()!=null && worksPackage.getUserDepartment().getId()!=null)
			sb.append(" and wp.userDepartment.id= " +worksPackage.getUserDepartment().getId());
		if(StringUtils.isNotBlank(worksPackage.getWpNumber()))
			sb.append(" and wp.wpNumber like '%" +worksPackage.getWpNumber()+"%'");
		if(fromDate!=null && toDate==null)addFieldError("enddate",getText("search.endDate.null"));
		if(toDate!=null && fromDate==null)addFieldError("startdate",getText("search.startDate.null"));
		if(!DateUtils.compareDates(getToDate(),getFromDate())) addFieldError("enddate",getText("greaterthan.endDate.fromDate"));
		if(fromDate!=null && toDate!=null && getFieldErrors().isEmpty())
			sb.append(" and wp.packageDate between '"+DateUtils.getFormattedDate(fromDate,"dd-MMM-yyyy" )+"' " +
					"and '"+DateUtils.getFormattedDate(toDate,"dd-MMM-yyyy" )+"'");


		if(StringUtils.isNotBlank(worksPackage.getTenderFileNumber()))
			sb.append(" and wp.tenderFileNumber like '%" +worksPackage.getTenderFileNumber()+"%'");


		if("createNegotiationForWP".equals(source)){
			sb.append(" and wp.id not in(select " +
					"tr.tenderEstimate.worksPackage.id from TenderResponse tr where " +
			"tr.state.previous.value !='CANCELLED' and wp.id=tr.tenderEstimate.worksPackage.id)"+
			" and wp.id not in(select " +
			"tr1.tenderEstimate.worksPackage.id from TenderResponse tr1 where " +
	"tr1.state.value ='NEW' and wp.id=tr1.tenderEstimate.worksPackage.id)");
		}	

		String query = sb.toString();
		String countQuery = "select count(*) " + query;
		return new SearchQueryHQL(query, countQuery, null);
	}
	
	public String search()
	{
		setPageSize(WorksConstants.PAGE_SIZE);
		String retVal=super.search();
		
		getPositionAndUser();
		if(searchResult.getFullListSize()==0)
			addFieldError("result not found", "No results found for search parameters");
		return retVal;
	}
}
