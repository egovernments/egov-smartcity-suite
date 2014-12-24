package org.egov.works.web.actions.tender;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.Money;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.pims.commons.Position;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.web.actions.workflow.GenericWorkFlowAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.masters.NewsPaper;
import org.egov.works.models.rateContract.Indent;
import org.egov.works.models.rateContract.IndentDetail;
import org.egov.works.models.tender.TenderFile;
import org.egov.works.models.tender.TenderFileDetail;
import org.egov.works.models.tender.TenderFileIndentDetail;
import org.egov.works.models.tender.TenderFileNewsPapers;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.TenderFileService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
@ParentPackage("egov")
@Result(name=TenderFileAction.PRINT,type="stream",location="PdfInputStream", params={"inputName","PdfInputStream","contentType","application/pdf","contentDisposition","no-cache"})
public class TenderFileAction extends GenericWorkFlowAction{
	public static final String PRINT = "print";
	private ReportService reportService;
	private InputStream pdfInputStream;
	private TenderFile tenderFile=new TenderFile();
	private Date fromDate;
	private Date toDate;
	private WorksService worksService;
	private String createdBySelection;
	private Integer deptId;
	private String editableDate;
	private String actionName;
	private String messageKey;
	private Long[] estId;
	private Long[] indntId;
	private Long id;
	private Long tenderFileId;
	private String sourcepage="";
	private Money worktotalValue;
	private String nextEmployeeName;
	private String nextDesignation;
	private String designation;
	private Long stateValue;
	private Integer rowId;
	private Integer approverId;
	
	private DepartmentService departmentService;
	private EmployeeService employeeService;
	private TenderFileService tenderFileService;
	private WorkflowService<TenderFile> workflowService;
	private CommonsService commonsService;
	private AbstractEstimateService abstractEstimateService;
	private List<AbstractEstimate> abstractEstimateList=new ArrayList<AbstractEstimate>();
	private List<Indent> indentList=new ArrayList<Indent>();

	private List<TenderFile> tenderFileLst=null;

	private PersonalInformationService personalInformationService;

	private static final String DATE_FORMAT="dd-MMM-yyyy";
	private static final String MODULE_TYPE="TenderFile";
	private static final String PREPARED_BY_LIST = "preparedByList";
	private static final String DEPARTMENT_LIST = "departmentList";
	private static final String SAVE_ACTION = "save";
	private static final String TENDER_MODULE_CODE = "APPROVED";
	private boolean quotation;
	private boolean isSpillOverWorks;
	private String cancelRemarks;
	private Long fileId;
	
	private Long[] newsPaperId;
	private static final String PUBLIC_WORKS_DEPARTMENT="Public Work";
	
	private String estNumber; 
	
	private BigDecimal amountRuleValue;
	private String additionalRuleValue;
	
	public String getAdditionalRuleValue() {
		return getAdditionalRule();
	}

	public Long[] getNewsPaperId() {
		return newsPaperId;
	}

	public void setNewsPaperId(Long[] newsPaperId) {
		this.newsPaperId = newsPaperId;
	}

	public TenderFileAction() {
        addRelatedEntity("department", DepartmentImpl.class);
        addRelatedEntity("egwStatus",EgwStatus.class);
        addRelatedEntity("preparedBy", PersonalInformation.class);
	}

	public String execute()	{
	     return SUCCESS;
	}

	public String newform(){
		PersonalInformation pi = getEmployee();
		Assignment assignment = getAssignment(pi);
		if(assignment!=null && "no".equalsIgnoreCase(getCreatedBy())){
			tenderFile.setPreparedBy(pi);
			setDesignation(assignment.getDesigId().getDesignationName());
			tenderFile.setDepartment(assignment.getDeptId());
		}
		return NEW;
	}

	public String edit(){
		//prepare() method has been called for time being due to setter problem with variable id.
		if(!tenderFile.getTenderFileNewsPapers().isEmpty()) {
			newsPaperId=new Long[tenderFile.getTenderFileNewsPapers().size()]; 
			int i=0;
			for(TenderFileNewsPapers tenderFileNewsPaper:tenderFile.getTenderFileNewsPapers()) { 
				newsPaperId[i] = tenderFileNewsPaper.getNewsPaper().getId();
				i++;
			}
		}
		
		if(isSpillOverWorks)
			tenderFile.setAdditionalWfRule("spillOverWorks");
		else if(!tenderFile.getTenderFileDetails().isEmpty() && tenderFile.getTenderFileDetails().get(0).getIndent() != null) {			
				tenderFile.setAdditionalWfRule("indent");
		}		
		else{
			 if(PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(getWorkFlowDepartment()) && getAdditionalRule().equalsIgnoreCase("ZonalPublicWork"))
					 tenderFile.setAdditionalWfRule("ZonalPublicWork");
			 else if(PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(getWorkFlowDepartment())){
				 tenderFile.setAdditionalWfRule("HQPublicWork");
			 }
			 else {
					if(tenderFile.getQuotationFlag()){
						tenderFile.setAdditionalWfRule("quotation");
					}else{
					tenderFile.setAdditionalWfRule("noQuotation");
				}
			 }	
		}
		prepare();
		return NEW;
	}

	public String cancelApprovedTenderFile() {  
		tenderFile = tenderFileService.findById(fileId, false);
		
		PersonalInformation prsnlInfo=employeeService.getEmpForUserId(Integer.valueOf(getLoggedInUserId()));			
		String empName="";
		if(prsnlInfo.getEmployeeFirstName()!=null)
			empName=prsnlInfo.getEmployeeFirstName();
		if(prsnlInfo.getEmployeeLastName()!=null)
			empName=empName.concat(" ").concat(prsnlInfo.getEmployeeLastName()); 			

		tenderFile.getCurrentState().getPrevious().setText1(cancelRemarks+". Tender File Cancelled by: "+empName);
		tenderFile.getCurrentState().getPrevious().setValue("CANCELLED");
		tenderFile.setEgwStatus(commonsService.getStatusByModuleAndCode("TenderFile","CANCELLED"));

		messageKey="tenderFile."+tenderFile.getEgwStatus().getCode();
		return SUCCESS;
	}

	public int getLoggedInUserId() {
		return Integer.parseInt(EGOVThreadLocals.getUserId());
	}

	public void prepare(){
		AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
		if(tenderFile!=null){
			if(tenderFile.getId()!=null){
				id=tenderFile.getId();
				tenderFileId=tenderFile.getId();
			}
		}
		if (id != null || tenderFileId !=null) {
			if(tenderFileId!=null){
				id=tenderFileId;
			}
			tenderFile= tenderFileService.findById(id, false);
			abstractEstimateList=tenderFileService.getAbstractEstimateListByTenderFile(tenderFile);
			indentList=tenderFileService.getIndentListByTenderFile(tenderFile);
			setWorktotalValue(abstractEstimateService.getWorkValueExcludingTaxesForEstList(abstractEstimateList));
			if(getAssignment(tenderFile.getPreparedBy())!=null)
				setDesignation(getAssignment(tenderFile.getPreparedBy()).getDesigId().getDesignationName());
		}
		super.prepare();
        setupDropdownDataExcluding("egwStatus","preparedBy");
        ArrayList<EgwStatus> statusList=(ArrayList<EgwStatus>) commonsService.getStatusByModule(MODULE_TYPE);
        statusList.remove(commonsService.getStatusByModuleAndCode(MODULE_TYPE, "NEW"));
        addDropdownData("newsPaperList", persistenceService.findAllByNamedQuery("ACTIVE_NEWSPAPERS"));	
        if(sourcepage.equals("rcSourcePage")){
        	addDropdownData("statusList", Arrays.asList(commonsService.getStatusByModuleAndCode(MODULE_TYPE,TENDER_MODULE_CODE)));
        }else{
        	addDropdownData("statusList", statusList);
        }

        if(tenderFile.getDepartment()!=null && tenderFile.getDepartment().getId()!=null)
        	deptId=tenderFile.getDepartment().getId();
		if(StringUtils.isNotBlank(getCreatedBy()) && "yes".equalsIgnoreCase(getCreatedBy())){
			setCreatedBySelection(getCreatedBy());
			addDropdownData(DEPARTMENT_LIST,departmentService.getAllDepartments());
			ajaxEstimateAction.setPersonalInformationService(personalInformationService);
			populatePreparedByList(ajaxEstimateAction,deptId!=null) ;
		}
		else {
			addDropdownData(PREPARED_BY_LIST,Arrays.asList(getEmployee()));
			addDropdownData(DEPARTMENT_LIST,Arrays.asList(getAssignment(getEmployee()).getDeptId()));
			setCreatedBySelection(getCreatedBy());
		}

		if(StringUtils.isNotBlank(getPastDate()))
			setEditableDate(getPastDate());
		addDropdownData("executingDepartmentList",departmentService.getAllDepartments());
/*		if(abstractEstimateService.getLatestAssignmentForCurrentLoginUser()!=null) {
			tenderFile.setWorkflowDepartmentId(abstractEstimateService.getLatestAssignmentForCurrentLoginUser().getDeptId().getId());
		}
*/
	getAdditionalRule();
		}

	private PersonalInformation getEmployee() {
		if(tenderFile.getPreparedBy()==null)
			return employeeService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		else
			return tenderFile.getPreparedBy();
	}

	private Assignment getAssignment(PersonalInformation pi) {
		if(tenderFile.getPreparedBy()==null)
			return employeeService.getAssignmentByEmpAndDate(new Date(), pi.getIdPersonalInformation());
		else return employeeService.getAssignmentByEmpAndDate(new Date(), tenderFile.getPreparedBy().getIdPersonalInformation());
	}

	public String getCreatedBy() {
		return worksService.getWorksConfigValue("TENDERFILE_PREPAREDBY");
	}

	public String getPastDate() {
		return worksService.getWorksConfigValue("TENDERFILE_PASTDATE");
	}

	private void populatePreparedByList(AjaxEstimateAction ajaxEstimateAction, boolean executingDeptPopulated){
		if (executingDeptPopulated) {
			ajaxEstimateAction.setExecutingDepartment(deptId);
			ajaxEstimateAction.setWorksService(worksService);
			ajaxEstimateAction.setPersistenceService(persistenceService);
			ajaxEstimateAction.usersInExecutingDepartment();
			addDropdownData(PREPARED_BY_LIST,ajaxEstimateAction.getUsersInExecutingDepartment());
		}
		else {
			addDropdownData(PREPARED_BY_LIST,Collections.EMPTY_LIST);
		}
	}

	@Override
	public StateAware getModel() {
		// TODO Auto-generated method stub
		return tenderFile;
	}

	public String save(){
		prepare();
		String wfStatus=null;
		if(approverId!=null){
			tenderFile.setApproverPositionId(approverId);
		}
		String actionName = parameters.get("actionName")[0];
		populateTenderFileList(estId, indntId);
		populateNewsPaperList(newsPaperId);
		
		if(getModel().getCurrentState()!=null){
			wfStatus=getModel().getCurrentState().getValue();
		}
		else{
			wfStatus="NEW";
		}
		if(isSpillOverWorks)
			tenderFile.setAdditionalWfRule("spillOverWorks");
		else if(!tenderFile.getTenderFileDetails().isEmpty() && tenderFile.getTenderFileDetails().get(0).getIndent() != null) {			
			tenderFile.setAdditionalWfRule("indent");
		}	
		else{
			 if(PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(getWorkFlowDepartment()) && getAdditionalRule().equalsIgnoreCase("ZonalPublicWork"))
					 tenderFile.setAdditionalWfRule("ZonalPublicWork");
			 else if(PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(getWorkFlowDepartment())){
				 tenderFile.setAdditionalWfRule("HQPublicWOrk");
			 }
			 else {
					if(tenderFile.getQuotationFlag()){
			tenderFile.setAdditionalWfRule("quotation");
		}else{
			tenderFile.setAdditionalWfRule("noQuotation");
		}
			 }	
		}
		
		if((actionName.equalsIgnoreCase("Forward")||actionName.equalsIgnoreCase("Approve")) && customizedWorkFlowService.getWfMatrix(getModel().getStateType(), getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),wfStatus, getPendingActions())==null && !isSpillOverWorks){
			String msg="Workflow is not available for "+getWorkFlowDepartment();
			throw new ValidationException(Arrays.asList(new ValidationError(null,msg)));
		}
		
		
		
		if(actionName.equalsIgnoreCase("save")){
			tenderFile.setEgwStatus(commonsService.getStatusByModuleAndCode("TenderFile","NEW"));
			tenderFile=tenderFileService.persist(tenderFile);
			if(id ==null){
				Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
				tenderFile = (TenderFile) workflowService.start(tenderFile, pos, "Tender File created.");
				}
			tenderFileService.setTenderFileNumber(tenderFile,tenderFileService.getCurrentFinancialYear(tenderFile.getFileDate()));
			messageKey="tenderFile."+tenderFile.getEgwStatus().getCode();
			addActionMessage(getText(messageKey,"The Tender File was saved successfully"));
			tenderFile = tenderFileService.persist(tenderFile);
		}
		else{
			if(id==null){
				tenderFile.setEgwStatus(commonsService.getStatusByModuleAndCode("TenderFile","NEW"));
				Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
				tenderFile = (TenderFile) workflowService.start(tenderFile, pos, "Tender File created.");
				tenderFileService.setTenderFileNumber(tenderFile,tenderFileService.getCurrentFinancialYear(tenderFile.getFileDate()));

			}
			tenderFile=tenderFileService.persist(tenderFile);
			workflowService.transition(actionName, tenderFile, approverComments);
			tenderFileService.setTenderFileNumber(tenderFile,tenderFileService.getCurrentFinancialYear(tenderFile.getFileDate()));
			tenderFile=tenderFileService.persist(tenderFile);
			messageKey="tenderFile."+tenderFile.getEgwStatus().getCode();
		}
		getDesignation(tenderFile);
		return SAVE_ACTION.equalsIgnoreCase(actionName)?EDIT:SUCCESS;
	}

	protected void populateTenderFileList(Long[] estimateID, Long[] indentId) {
		tenderFile.getTenderFileDetails().clear();
		if(estimateID!=null && estimateID.length>0){
			abstractEstimateList=abstractEstimateService.getAbEstimateListById(StringUtils.join(estId,"`~`"));
			for(AbstractEstimate ab:abstractEstimateList) {
				TenderFileDetail tenderFileDetail = new TenderFileDetail();
				tenderFileDetail.setAbstractEstimate(ab);
				tenderFileDetail.setTenderFile(tenderFile);
				tenderFile.addTenderFileDetail(tenderFileDetail);
			}
		}
		if(indentId!=null && indentId.length>0){
			AjaxTenderFileAction ajaxTenderFileAction = new AjaxTenderFileAction();
			ajaxTenderFileAction.setPersistenceService(getPersistenceService());
			indentList=ajaxTenderFileAction.getIndentListById(StringUtils.join(indntId,"`~`"));
			for(Indent indent:indentList) {
				TenderFileDetail tenderFileDetail = new TenderFileDetail();
				tenderFileDetail.setIndent(indent);
				for(IndentDetail indentDetail:indent.getIndentDetails()) {
					TenderFileIndentDetail  tenderFileIndentDetail = new TenderFileIndentDetail();
					tenderFileIndentDetail.setIndentDetail(indentDetail);
					tenderFileDetail.addTenderFileIndentDetails(tenderFileIndentDetail);
					tenderFileIndentDetail.setTenderFileDetail(tenderFileDetail);
				}

				tenderFileDetail.setTenderFile(tenderFile);
				tenderFile.addTenderFileDetail(tenderFileDetail);
			}
		}
	}

	protected void populateNewsPaperList(Long[] newsPaperId) {
		tenderFile.getTenderFileNewsPapers().clear();
		if(newsPaperId!=null && newsPaperId.length>0){
			List<NewsPaper> newsPaperList=persistenceService.findAllByNamedQuery("GETNEWSPAPERSBYID", Arrays.asList(newsPaperId));
			for(NewsPaper np:newsPaperList) {
				TenderFileNewsPapers tenderFileNewsPapers = new TenderFileNewsPapers();
				tenderFileNewsPapers.setNewsPaper(np);
				tenderFileNewsPapers.setTenderFile(tenderFile);
				tenderFile.addTenderFileNewsPapers(tenderFileNewsPapers);
			}
		}		
	}
	
/*	public String cancel(){
		if(tenderFile.getId()!=null){
			workflowService.transition(TenderFile.Actions.CANCEL.toString(), tenderFile,tenderFile.getWorkflowapproverComments());
			tenderFile=tenderFileService.persist(tenderFile);
		}
		messageKey="tenderFile.cancel";
		getDesignation(tenderFile);
		return SUCCESS;
	}
*/
	public String search(){
		 prepare();
		 if("cancelTenderFile".equalsIgnoreCase(sourcepage)) {
			 tenderFile.setEgwStatus(commonsService.getStatusByModuleAndCode("TenderFile","APPROVED"));
		 }
		 return "search";
	 }

	 public String searchDetails(){
		prepare();
		String actions = worksService.getWorksConfigValue("TENDERFILE_SHOW_ACTIONS");

		if(toDate!=null && !DateUtils.compareDates(new Date(),getToDate())){
			addFieldError("toDate",getText("greaterthan.toDate.currentdate"));
		}
		if(fromDate!=null && !DateUtils.compareDates(new Date(),getFromDate())){
			addFieldError("toDate",getText("greaterthan.fromDate.currentdate"));
		}
	 	if(fromDate!=null && toDate!=null && !DateUtils.compareDates(getToDate(),getFromDate())){
			addFieldError("fromDate",getText("greaterthan.toDate.fromDate"));
	 	}
		if(!getFieldErrors().isEmpty())
			return "search";

		setPageSize(WorksConstants.PAGE_SIZE);
		super.search();
		if(sourcepage.equals("rcSourcePage")){
		tenderFileLst=(ArrayList<TenderFile>) searchResult.getList();
		}
		else{
		if(searchResult.getFullListSize() !=0){
			List<TenderFile> tenderFileList=new ArrayList<TenderFile>();
		      Iterator i = searchResult.getList().iterator();
				while(i.hasNext()){
					TenderFile tenderFile=(TenderFile) i.next();
					if(StringUtils.isNotBlank(actions)){
						tenderFile.getTenderFileActions().addAll(Arrays.asList(actions.split(",")));
					}
					tenderFileList.add(tenderFile);
				}
			tenderFileList = getPositionAndUser(searchResult.getList());	
			searchResult.getList().clear();
			searchResult.getList().addAll(tenderFileList);
		}
		}
		return "search";
	 }
	 
	 protected List<TenderFile> getPositionAndUser(List<TenderFile> results){
			List<TenderFile> tenderfileList = new ArrayList<TenderFile>();
			for(TenderFile tf :results){
				PersonalInformation emp = employeeService.getEmployeeforPosition(tf.getCurrentState().getOwner());
				if(emp!=null)
					tf.setOwner(emp.getEmployeeName());
				tenderfileList.add(tf);
			}	
			return tenderfileList;
		}
	 
	 public void validate(){
		 boolean isHQbudget=false;
		 boolean isZonal=false;
		 if(ServletActionContext.getRequest().getParameterValues("estId") != null) {
			 List<Long> estIds = new ArrayList<Long>();
			 for (String id : ServletActionContext.getRequest().getParameterValues("estId")) {
				 estIds.add(Long.valueOf(id));
			 }
		abstractEstimateList = abstractEstimateService.findAllByNamedQuery("getAbEstimateListById", estIds);
		if(tenderFile.getDepartment()!=null && PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(tenderFile.getDepartment().getDeptName()) && abstractEstimateList.size()>1){
		 for(AbstractEstimate abstractEstimate : abstractEstimateList){
			 List functionCodes = getFunctionCodes();	 
			 if(functionCodes!=null && !functionCodes.isEmpty() && !functionCodes.contains(abstractEstimate.getFinancialDetails().get(0).getFunction().getCode()) && (!abstractEstimate.getIsSpillOverWorks())){
				  isHQbudget=true;
			 }
			 else
				 isZonal=true;
			 if(isHQbudget && isZonal)
				addActionError("Estimates selected has two different budget heads one from zonal and one from HQ");
			}
		 
		 }
	}
}
	public TenderFile getTenderFile() {
		return tenderFile;
	}

	public void setTenderFile(TenderFile tenderFile) {
		this.tenderFile = tenderFile;
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

	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder){
		StringBuffer tenderFileSql = new StringBuffer();
		List<Object> paramList = new ArrayList<Object>();
		EgwStatus egwStatus=(EgwStatus) commonsService.getStatusByModuleAndCode("TenderFile","NEW");
		tenderFileSql.append(" from TenderFile tf where tf.id is not null and tf.egwStatus.id<>"+egwStatus.getId());
		if(sourcepage.equals("rcSourcePage")){
			tenderFileSql.append(" and tf.id in (select tfd.tenderFile.id from TenderFileDetail tfd where tfd.indent.id is not null)");
		}
		if(toDate==null && fromDate!=null){
			tenderFileSql.append(" and tf.fileDate >= ? ");
			paramList.add(new Date(DateUtils.getFormattedDate(getFromDate(),DATE_FORMAT )));
		}else if(toDate!=null && fromDate==null){
			tenderFileSql.append(" and tf.fileDate <= ? ");
			paramList.add(new Date(DateUtils.getFormattedDate(getToDate(),DATE_FORMAT )));
		}else if(fromDate!=null && toDate!=null){
			tenderFileSql.append(" and tf.fileDate between ? and ? ");
			paramList.add(new Date(DateUtils.getFormattedDate(getFromDate(),DATE_FORMAT )));
			paramList.add(new Date(DateUtils.getFormattedDate(getToDate(),DATE_FORMAT)));
		}
		if(tenderFile.getDepartment()!=null && tenderFile.getDepartment().getId()!=-1){
			tenderFileSql.append(" and tf.department.id = ? ");
			paramList.add(tenderFile.getDepartment().getId());
		}
		if(estNumber!=null && !"".equals(estNumber)){ 
			tenderFileSql.append(" and tf.id in (select tfd.tenderFile.id from TenderFileDetail tfd where tfd.abstractEstimate is not null and UPPER(tfd.abstractEstimate.estimateNumber) like '%"+estNumber.toUpperCase()+"%')");
		} 
		
		if(!sourcepage.equals("rcSourcePage")){
			if(StringUtils.isNotBlank(tenderFile.getFileNumber())){
				tenderFileSql.append(" and UPPER(tf.fileNumber) like '%"+tenderFile.getFileNumber().toString().trim().toUpperCase()+"%'"); 
			}
		}
		if(tenderFile.getEgwStatus()!=null && tenderFile.getEgwStatus().getId()!=-1){ 
			tenderFileSql.append(" and tf.egwStatus.id=?");
			paramList.add(tenderFile.getEgwStatus().getId());
		}
		if(quotation){
			tenderFileSql.append(" and tf.quotationFlag=1");
		}
		else{
			tenderFileSql.append(" and tf.quotationFlag=0");
		}
		tenderFileSql.append(" order by tf.id");

		String countQuery = "select count(*) " + tenderFileSql.toString();;
		return new SearchQueryHQL(tenderFileSql.toString(), countQuery, paramList);
	}

	public String getCreatedBySelection() {
		return createdBySelection;
	}

	public void setCreatedBySelection(String createdBySelection) {
		this.createdBySelection = createdBySelection;
	}

	public WorksService getWorksService() {
		return worksService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public String getEditableDate() {
		return editableDate;
	}

	public void setEditableDate(String editableDate) {
		this.editableDate = editableDate;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public void setTenderFileService(TenderFileService tenderFileService) {
		this.tenderFileService = tenderFileService;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public String getActionName() {
		return actionName;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public List<AbstractEstimate> getAbstractEstimateList() {
		return abstractEstimateList;
	}

	public void setAbstractEstimateList(List<AbstractEstimate> abstractEstimateList) {
		this.abstractEstimateList = abstractEstimateList;
	}

	public Long[] getEstId() {
		return estId;
	}

	public void setEstId(Long[] estId) {
		this.estId = estId;
	}

/*	public List<org.egov.infstr.workflow.Action> getValidActions(){
		return workflowService.getValidActions(tenderFile);
	}
*/
	public void setTenderFileWorkflowService(WorkflowService<TenderFile> workflow) {
		this.workflowService = workflow;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSourcepage() {
		return sourcepage;
	}

	public void setSourcepage(String sourcepage) {
		this.sourcepage = sourcepage;
	}

	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}

	public Money getWorktotalValue() {
		return worktotalValue;
	}

	public void setWorktotalValue(Money worktotalValue) {
		this.worktotalValue = worktotalValue;
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

	public void getDesignation(TenderFile tenderFile){
		if(tenderFile.getCurrentState()!= null
				&& !"NEW".equalsIgnoreCase(tenderFile.getCurrentState().getValue())) {
			String result = worksService.getEmpNameDesignation(tenderFile.getState().getOwner(),tenderFile.getState().getCreatedDate());
			if(result != null && !"@".equalsIgnoreCase(result)) {
				String empName = result.substring(0,result.lastIndexOf('@'));
				String designation =result.substring(result.lastIndexOf('@')+1,result.length());
				setNextEmployeeName(empName);
				setNextDesignation(designation);
			}
		}
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Long getStateValue() {
		return stateValue;
	}

	public void setStateValue(Long stateValue) {
		this.stateValue = stateValue;
	}

	public String workflowHistory(){
		return "history";
	}

	public List<Indent> getIndentList() {
		return indentList;
	}

	public void setIndentList(List<Indent> indentList) {
		this.indentList = indentList;
	}

	public Long[] getIndntId() {
		return indntId;
	}

	public void setIndntId(Long[] indntId) {
		this.indntId = indntId;
	}



	/**
	 * print PDF for TenderFile
	 * @throws JRException,Exception
	 */
	@SkipValidation
	public String viewTenderFilePdf() throws JRException,Exception{
		//prepare() method has been called for time being due to setter problem with variable id.
		prepare();
		TenderFile tenderHeader = tenderFileService.findById(tenderFileId, false);
		Map<String,Object> reportParams = new HashMap<String,Object>();
		reportParams.put("tenderHeader", tenderHeader);
		ReportRequest reportRequest = new ReportRequest("TenderFile", tenderHeader.getTenderFileDetails(), reportParams);
		ReportOutput reportOutput = reportService.createReport(reportRequest);
	 	if(reportOutput!=null && reportOutput.getReportOutputData()!=null)
   		   pdfInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());

		return PRINT;
	}

	public InputStream getPdfInputStream() {
		return pdfInputStream;
	}

	public void setPdfInputStream(InputStream pdfInputStream) {
		this.pdfInputStream = pdfInputStream;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public List<TenderFile> getTenderFileLst() {
		return tenderFileLst;
	}

	public Integer getRowId() {
		return rowId;
	}

	public Long getTenderFileId() {
		return tenderFileId;
	}

	public void setTenderFileId(Long tenderFileId) {
		this.tenderFileId = tenderFileId;
	}
	public String getPendingActions()
	{
		return tenderFile==null?"":
			(tenderFile.getCurrentState()==null?"":tenderFile.getCurrentState().getNextAction());
	}

	public String getWorkFlowDepartment(){
		return tenderFile==null?"":(tenderFile.getDepartment()==null?"":tenderFile.getDepartment().getDeptName());
	}

	public Integer getApproverId() {
		return approverId;
	}

	public void setApproverId(Integer approverId) {
		this.approverId = approverId;
	}

	public void setQuotation(boolean quotation) {
		this.quotation = quotation;
	}

	public boolean getQuotation() {
		return quotation;
	}

	protected String getAdditionalRule() {
		String rule = "";
		if(ServletActionContext.getRequest().getParameterValues("estId") != null) {
			 List<Long> estIds = new ArrayList<Long>();
			 for (String id : ServletActionContext.getRequest().getParameterValues("estId")) {
				 estIds.add(Long.valueOf(id));
			 }
		
			 abstractEstimateList = abstractEstimateService.findAllByNamedQuery("getAbEstimateListById", estIds);
		}
		
		if(tenderFile.getDepartment()!=null && PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(tenderFile.getDepartment().getDeptName())){
			 for(AbstractEstimate abstractEstimate : abstractEstimateList){
				 if(!abstractEstimate.getIsSpillOverWorks()){
					 List<String> functionCodes = getFunctionCodes();
					if(!functionCodes.isEmpty() && functionCodes.contains(abstractEstimate.getFinancialDetails().get(0).getFunction().getCode())) {				
						rule = "ZonalPublicWork";
						tenderFile.setAdditionalWfRule(rule);
					}	else {
								rule="HQPublicWork";
								tenderFile.setAdditionalWfRule(rule);
					}
					}
				} 
			 }
		else if(isSpillOverWorks){
			rule="spillOverWorks";
			tenderFile.setAdditionalWfRule(rule);
		}
		else if(!tenderFile.getTenderFileDetails().isEmpty() && tenderFile.getTenderFileDetails().get(0).getIndent() != null) {			
			tenderFile.setAdditionalWfRule("indent");
		}		
		 else { 
			rule = (tenderFile == null ? "" : (tenderFile.getQuotationFlag() ? "quotation" : "noQuotation"));
			tenderFile.setAdditionalWfRule(rule);
		}
		return rule;
}

	
	public List<String> getFunctionCodes() {
		List<AppConfigValues> appConfigList = worksService.getAppConfigValue("Works", "WORKS_PWD_FUNCTIONWISE_WF");
		List<String> functionCodes = new LinkedList<String>();
		if (!appConfigList.isEmpty()) {
			if (appConfigList.get(0).getValue() != "" && appConfigList.get(0).getValue() != null) {
				String[] configVals = appConfigList.get(0).getValue().split(",");
				for (String configVal : configVals) {
					functionCodes.add(configVal);
				}
			}
		}
		return functionCodes;
	}	

	public void setIsSpillOverWorks(boolean isSpillOverWorks) {
		this.isSpillOverWorks = isSpillOverWorks;
	}

	public boolean getIsSpillOverWorks() {
		return isSpillOverWorks;
	}

	public String getCancelRemarks() {
		return cancelRemarks;
	}

	public void setCancelRemarks(String cancelRemarks) {
		this.cancelRemarks = cancelRemarks;
	}

	public Long getFileId() {
		return fileId; 
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId; 
	}

	public String getEstNumber() {
		return estNumber;
	}

	public void setEstNumber(String estNumber) { 
		this.estNumber = estNumber;
	}
	
	protected BigDecimal getAmountRule() {
	    double amountValue = 0;		
		if(abstractEstimateList != null && !abstractEstimateList.isEmpty() && tenderFile.getAdditionalWfRule().equalsIgnoreCase("ZonalPublicWork")){
			for(AbstractEstimate abstractEstimate : abstractEstimateList){
				if(abstractEstimate != null && !abstractEstimate.getIsSpillOverWorks() && abstractEstimate.getExecutingDepartment() != null &&  PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(abstractEstimate.getExecutingDepartment().getDeptName())) {
					amountValue = amountValue+(abstractEstimate.getTotalAmount() == null ? 0 : abstractEstimate.getTotalAmount().getValue());
				}
			}
		}
		BigDecimal amountRuleValues = new BigDecimal(amountValue);
		tenderFile.setAmountWfRule(amountRuleValues.equals(BigDecimal.ZERO) ? null : amountRuleValues);
		return amountRuleValues.equals(BigDecimal.ZERO) ? null : amountRuleValues;
	}
	
	public BigDecimal getAmountRuleValue() {
		return getAmountRule();
	}

	public void setAmountRuleValue(BigDecimal amountRuleValue) {
		this.amountRuleValue = amountRuleValue;
	}
	
}