package org.egov.works.web.actions.rateContract;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.ContractorGrade;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.commons.Position;
import org.egov.pims.service.EmployeeService;
import org.egov.tender.model.GenericTenderResponse;
import org.egov.tender.model.TenderResponseLine;
import org.egov.tender.services.common.GenericTenderService;
import org.egov.web.actions.workflow.GenericWorkFlowAction;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.rateContract.Indent;
import org.egov.works.models.rateContract.RateContract;
import org.egov.works.models.rateContract.RateContractDetail;
import org.egov.works.models.tender.TenderFile;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.RateContractService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;

@Result(name=RateContractAction.PRINT,type="stream",location="PdfInputStream", params={"inputName","PdfInputStream","contentType","application/pdf","contentDisposition","no-cache"})
public class RateContractAction extends GenericWorkFlowAction {
   	private InputStream pdfInputStream;   
	private ReportService reportService;    
	public static final String PRINT = "print"; 
	
	private  RateContract rateContract=new RateContract();
	private String responseNumber="";
	private Long contractorId;
	private String tenderFileNum;
	private GenericTenderService genericTenderService;
	private TenderFile tenderFile;
	private GenericTenderResponse tenderResponse;
	private Long indentId;
	private Indent ind;
	private String mode="";
	private String sourcepage="";
	private Long id;
	private Integer deptId;
	private Long contractorGradeId;
	private String indentType;
	private Set<TenderResponseLine> responseLine = new HashSet<TenderResponseLine>() ;
	private List<RateContractDetail> sorRcDetails = new LinkedList<RateContractDetail>();
	private CommonsService commonsService;
	private static final String RATECONTRACT_MODULE_KEY = "Rate_Contract";
	private static final String DATE_FORMAT = "dd-MMM-yyyy";
	private static final String SEARCH = "search";
	private RateContractService rateContractService;
	private AbstractEstimateService abstractEstimateService;
	private Date fromCreateDate;
	private Date toCreateDate;
	private WorksService worksService;
	private List<GenericTenderResponse> responseList;
	private static final String INDENT_MODULE_CODE = "CANCELLED";
	private static final String TENDER_MODULE_CODE = "APPROVED";
	private static final String SAVE_ACTION = "save";
	private String fileNumber=null;
	private String noticeNumber=null;
	private String fileNum;
	private String actionName="";
	private EmployeeService employeeService;
	private WorkflowService<RateContract> workflowService;
	private String messageKey;
	private String nextEmployeeName;
	private String nextDesignation;
	private String designation;
	private Long stateValue;
	private String departmentName;
	private String noticeNumberTemp;
	private String responseNumberTemp;
		
	public RateContractAction() {
		addRelatedEntity("indent", Indent.class);
		addRelatedEntity("contractor", Contractor.class);
		addRelatedEntity("department", DepartmentImpl.class);
		addRelatedEntity("contractorGrade", ContractorGrade.class);
	}
	
	@Override
	public StateAware getModel() {
		// TODO Auto-generated method stub
		return rateContract;
	}
	
	public void prepare(){
		super.prepare();
		setupDropdownDataExcluding("contractor","indent");
		AjaxRateContractAction ajaxIndentsAction = new AjaxRateContractAction();
		if (id != null) 
		{
			rateContract = rateContractService.findById(id, false);
			if(rateContract.getResponseNumber()!=null)
			{
				noticeNumber = genericTenderService.getGenericResponseByNumber(rateContract.getResponseNumber()).getTenderUnit().getTenderNotice().getNumber();
			}
		}
		else
		{
			if(fileNum!=null){
				ajaxIndentsAction.setFileNum(fileNum);
				ajaxIndentsAction.loadIndents();
				addDropdownData("indentNumList", ajaxIndentsAction.getIndent());
			}else if(mode.equals("search"))
			{
				addDropdownData("indentNumList",getPersistenceService().findAllBy("select distinct tfd.indent from TenderFileDetail tfd where tfd.indent is not null and tfd.tenderFile.fileNumber=?",tenderFileNum));
			}
			else
			{
				addDropdownData("indentNumList", Collections.emptyList());
			}
			
			if(!mode.equals("search") && responseNumber!="")
			{
				tenderResponse = genericTenderService.getGenericResponseByNumber(responseNumber);
				if(tenderResponse!=null)
				{
					responseLine = tenderResponse.getResponseLines();					
				}
				
				if(indentId!=null)
				{
					rateContract.setIndent((Indent) getPersistenceService().find("from Indent where id=?", indentId));
					departmentName=rateContract.getIndent().getDepartment().getDeptName();
					tenderFile = (TenderFile) getPersistenceService().find("from TenderFile where fileNumber=?", tenderFileNum);
					noticeNumber = genericTenderService.getTenderNoticeByTenderFile(tenderFile).getNumber();
					rateContract.setContractor((Contractor) getPersistenceService().find("from Contractor where id=?", contractorId));
				}
			}
		}
		if(!getWorkFlowDepartment().equals("")){
			departmentName=getWorkFlowDepartment();
		}
		if(responseNumber!=null && !"".equals(responseNumber)) {
			responseNumberTemp=responseNumber;
		}
		if(noticeNumber!=null && !"".equals(noticeNumber)) {
			noticeNumberTemp=noticeNumber;
		}

	}
	
	public String getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}

	@SuppressWarnings("unchecked")
	public String newForm(){
		return NEW;
	}
	
	public String save(){
		String actionName = parameters.get("actionName")[0];
		rateContract.getRateContractDetails().clear();
		populateRcDetails();
		String deptName=getWorkFlowDepartment();
		String curStatus;
		if(rateContract.getCurrentState()!=null){
			curStatus=rateContract.getCurrentState().getValue();
		}
		else
			curStatus="NEW";
		if((actionName.equalsIgnoreCase("Forward")||actionName.equalsIgnoreCase("Approve")) && customizedWorkFlowService.getWfMatrix(getModel().getStateType(), deptName, getAmountRule(), getAdditionalRule(),curStatus, getPendingActions())==null){
			throw new ValidationException(Arrays.asList(new ValidationError("ratecontract.workflow.notdefined",getText("ratecontract.workflow.notdefined",new String[]{deptName}))));
		} 
		
		if(actionName.equalsIgnoreCase("save")){
			rateContract.setEgwStatus(commonsService.getStatusByModuleAndCode(RATECONTRACT_MODULE_KEY,"NEW"));
			rateContractService.setRateContractNumber(rateContract, rateContractService.getCurrentFinancialYear(rateContract.getRcDate())); 
			if(id ==null){
				Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
				rateContract = (RateContract)workflowService.start(rateContract,pos, "RateContract Created.");
				noticeNumber = genericTenderService.getGenericResponseByNumber(rateContract.getResponseNumber()).getTenderUnit().getTenderNotice().getNumber();
		}
			messageKey="rateContract."+actionName;
			addActionMessage(getText(messageKey,"The RateContract was saved successfully"));
			rateContract = rateContractService.persist(rateContract);
			getDesignation(rateContract);
		}
		else if(mode!=null && !mode.equals("edit")){
			if(id ==null){
				rateContract.setEgwStatus(commonsService.getStatusByModuleAndCode(RATECONTRACT_MODULE_KEY,"NEW"));
				rateContractService.setRateContractNumber(rateContract, rateContractService.getCurrentFinancialYear(rateContract.getRcDate()));
				Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
				rateContract = (RateContract) workflowService.start(rateContract, pos, "RateContract created.");
			}
			rateContract = rateContractService.persist(rateContract);
			workflowService.transition(actionName, rateContract, approverComments);
			rateContract = rateContractService.persist(rateContract);
			messageKey="rateContract."+actionName;
			getDesignation(rateContract);
		}
		return SAVE_ACTION.equalsIgnoreCase(actionName)?EDIT:SUCCESS;
	}
	
	public void getDesignation(RateContract rateContract){
		if(rateContract.getCurrentState()!= null  
				&& !"NEW".equalsIgnoreCase(rateContract.getCurrentState().getValue())) {
			String result = worksService.getEmpNameDesignation(rateContract.getState().getOwner(),rateContract.getState().getCreatedDate());
			if(result != null && !"@".equalsIgnoreCase(result)) {
				String empName = result.substring(0,result.lastIndexOf('@'));
				String designation =result.substring(result.lastIndexOf('@')+1,result.length());
				setNextEmployeeName(empName);
				setNextDesignation(designation);
			} 
		}
	}
	
	public String cancel(){
		String actionName = parameters.get("actionName")[0]; 
		if(rateContract.getId()!=null){
			workflowService.transition(actionName, rateContract,approverComments);
			rateContract=rateContractService.persist(rateContract);
		}
		messageKey="rateContract.Cancel";		
		return SUCCESS;
	}
	
	public List<EgwStatus> getRateContractStatuses() {
		List<EgwStatus> rcStatusList=commonsService.getStatusByModule(RateContract.class.getSimpleName());
		rcStatusList.remove(commonsService.getStatusByModuleAndCode(RateContract.class.getSimpleName(), "NEW"));
		return rcStatusList;
	}
	
	public String getPendingActions()
	{
		return rateContract==null?"":
			(rateContract.getCurrentState()==null?"":rateContract.getCurrentState().getNextAction());
	}

	public String getWorkFlowDepartment(){
		return rateContract==null?"":(rateContract.getIndent()==null?"":(rateContract.getIndent().getDepartment()==null?"":rateContract.getIndent().getDepartment().getDeptName()));
	}
	
	
	protected void populateRcDetails() {
		for(RateContractDetail rcDetail : sorRcDetails){
			rcDetail.setIndentDetail(rateContractService.getActivityFromRateContractDetailAndIndent(rcDetail, rateContract.getIndent()));
			rcDetail.setRcRate(rcDetail.getRcRate());
			rcDetail.setRateContract(rateContract);
			rateContract.addRateContractDetails(rcDetail);
		}
	}

	public String searchDetails(){
		String actions = worksService.getWorksConfigValue("RATECONTRACT_SHOW_ACTIONS");
		if(toCreateDate!=null && !DateUtils.compareDates(new Date(),getToCreateDate())){
			addFieldError("toCreateDate",getText("greater.toCreateDate.currentdate"));
		}
		if(fromCreateDate!=null && !DateUtils.compareDates(new Date(),getFromCreateDate())){
			addFieldError("toCreateDate",getText("greater.fromCreateDate.currentdate"));
		}
	 	if(fromCreateDate!=null && toCreateDate!=null && !DateUtils.compareDates(getToCreateDate(),getFromCreateDate())){
			addFieldError("fromCreateDate",getText("greater.toCreateDate.fromCreateDate"));
	 	}
		if(!getFieldErrors().isEmpty())
			return "searchRC";

		setPageSize(WorksConstants.PAGE_SIZE);
		super.search();
		if(searchResult.getFullListSize() !=0){
			List<RateContract> rcList=new ArrayList<RateContract>();
			  for(RateContract rateContract:(ArrayList<RateContract>) searchResult.getList()){
				if(StringUtils.isNotBlank(actions)){
					rateContract.getRcActions().addAll(Arrays.asList(actions.split(",")));
				}
				rcList.add(rateContract);
			  }
			searchResult.getList().clear();
			searchResult.getList().addAll(rcList);
		}

		return "searchRC";
	 }
	 
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		StringBuffer rcSql = new StringBuffer();
		List<Object> paramList = new ArrayList<Object>();
		EgwStatus egwStatus=(EgwStatus) commonsService.getStatusByModuleAndCode(RATECONTRACT_MODULE_KEY,"NEW");
		
		rcSql.append(" from RateContract rc where rc.id is not null and rc.egwStatus.id<>"+egwStatus.getId());

		if(fromCreateDate!=null && toCreateDate==null){
			rcSql.append(" and rc.rcDate >= ? ");
			paramList.add(new Date(DateUtils.getFormattedDate(getFromCreateDate(),DATE_FORMAT )));
		}else if(fromCreateDate!=null && toCreateDate!=null){
			rcSql.append(" and rc.rcDate between ? and ? ");
			paramList.add(new Date(DateUtils.getFormattedDate(getFromCreateDate(),DATE_FORMAT )));
			paramList.add(new Date(DateUtils.getFormattedDate(getToCreateDate(),DATE_FORMAT)));
		}
		if(deptId!=null && deptId !=-1){
				rcSql.append(" and rc.indent.department.id = ? ");
				paramList.add(deptId);
			
		}
		if(StringUtils.isNotBlank(rateContract.getRcNumber())){
			rcSql.append(" and UPPER(rc.rcNumber) like '%"+rateContract.getRcNumber().toString().trim().toUpperCase()+"%'");
		}
			
		if(indentType!=null && !indentType.equals("-1")){
				rcSql.append(" and rc.indent.indentType=?");
				paramList.add(indentType);
		}
		
		if(contractorGradeId!=null && contractorGradeId!=-1){
				rcSql.append(" and rc.indent.contractorGrade.id=?");
				paramList.add(contractorGradeId);
		}
		
		rcSql.append(" order by rc.id");

		String countQuery = "select count(*) " + rcSql.toString();
		return new SearchQueryHQL(rcSql.toString(), countQuery, paramList);
	}
	
	@SuppressWarnings("unchecked")
	public String searchTenderResponse(){
		responseList = genericTenderService.getAcceptedTenderResponse(fileNumber, rateContract.getIndent());
		List<GenericTenderResponse> responseListTemp = new ArrayList<GenericTenderResponse>();
		responseListTemp.addAll(responseList);
		if(responseListTemp.size()>0)
		{
			for(GenericTenderResponse gtr : responseListTemp)
			{		
				rateContract = (RateContract)getPersistenceService().find("from RateContract where responseNumber=? and egwStatus.code!=?",gtr.getNumber(), INDENT_MODULE_CODE);
				if(rateContract!=null)
				{
					responseList.remove(gtr);
					if(responseList.size()==0)
					{
						return SEARCH; 
					}
				}
			}
		}
		return SEARCH;
	 }
	
	public String edit(){
		return EDIT;
	}
	
	public String search(){
		 return SEARCH;
	 }

	public String searchRC(){
		 return "searchRC";
	 }

	public String workflowHistory(){
		return "history";
	}
	
	
	public RateContract getRateContract() {
		return rateContract;
	}
	public void setRateContract(RateContract rateContract) {
		this.rateContract = rateContract;
	}
	
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getResponseNumber() {
		return responseNumber;
	}

	public void setResponseNumber(String responseNumber) {
		this.responseNumber = responseNumber;
	}

	public Long getContractorId() {
		return contractorId;
	}

	public String getTenderFileNum() {
		return tenderFileNum;
	}

	public void setTenderFileNum(String tenderFileNum) {
		this.tenderFileNum = tenderFileNum;
	}

	public TenderFile getTenderFile() {
		return tenderFile;
	}

	public void setTenderFile(TenderFile tenderFile) {
		this.tenderFile = tenderFile;
	}

	public Long getIndentId() {
		return indentId;
	}

	public void setIndentId(Long indentId) {
		this.indentId = indentId;
	}

	public Set<TenderResponseLine> getResponseLine() {
		return responseLine;
	}

	public void setResponseLine(Set<TenderResponseLine> responseLine) {
		this.responseLine = responseLine;
	}

	public GenericTenderResponse getTenderResponse() {
		return tenderResponse;
	}

	public void setTenderResponse(GenericTenderResponse tenderResponse) {
		this.tenderResponse = tenderResponse;
	}

	public GenericTenderService getGenericTenderService() {
		return genericTenderService;
	}

	public void setGenericTenderService(GenericTenderService genericTenderService) {
		this.genericTenderService = genericTenderService;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CommonsService getCommonsService() {
		return commonsService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public RateContractService getRateContractService() {
		return rateContractService;
	}

	public void setRateContractService(
			RateContractService rateContractService) {
		this.rateContractService = rateContractService;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public AbstractEstimateService getAbstractEstimateService() {
		return abstractEstimateService;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public Date getFromCreateDate() {
		return fromCreateDate;
	}

	public void setFromCreateDate(Date fromCreateDate) {
		this.fromCreateDate = fromCreateDate;
	}

	public Date getToCreateDate() {
		return toCreateDate;
	}

	public void setToCreateDate(Date toCreateDate) {
		this.toCreateDate = toCreateDate;
	}

	public WorksService getWorksService() {
		return worksService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public Long getContractorGradeId() {
		return contractorGradeId;
	}

	public void setContractorGradeId(Long contractorGradeId) {
		this.contractorGradeId = contractorGradeId;
	}

	public String getIndentType() {
		return indentType;
	}

	public void setIndentType(String indentType) {
		this.indentType = indentType;
	}


	public String getNoticeNumber() {
		return noticeNumber;
	}

	public String getSourcepage() {
		return sourcepage;
	}

	public void setSourcepage(String sourcepage) {
		this.sourcepage = sourcepage;
	}

	public List<GenericTenderResponse> getResponseList() {
		return responseList;
	}

	public void setResponseList(List<GenericTenderResponse> responseList) {
		this.responseList = responseList;
	}

	public Indent getInd() {
		return ind;
	}

	public void setInd(Indent ind) {
		this.ind = ind;
	}

	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}

	public void setNoticeNumber(String noticeNumber) {
		this.noticeNumber = noticeNumber;
	}
	public List<RateContractDetail> getSorRcDetails() {
		return sorRcDetails;
	}

	public void setSorRcDetails(List<RateContractDetail> sorRcDetails) {
		this.sorRcDetails = sorRcDetails;
	}
	/**
	 * print PDF for RateContract
	 * @throws JRException,Exception 
	 */
	@SkipValidation
	public String viewRateContractPDF() throws JRException, Exception{
		rateContract =    rateContractService.findById(id, false);
		List<RateContractDetail> rateContractDetailSorList=new ArrayList<RateContractDetail>();
		List<RateContractDetail> rateContractDetailNonSorList=new ArrayList<RateContractDetail>();
		for(RateContractDetail rateContractDetail:rateContract.getRateContractDetails()){
			if(rateContractDetail.getIndentDetail().getScheduleOfRate()!=null){
				rateContractDetailSorList.add(rateContractDetail);
			}else{
				rateContractDetailNonSorList.add(rateContractDetail);
			}
		}
		rateContract.getRateContractDetails().clear();
		rateContract.getRateContractDetails().addAll(rateContractDetailSorList);
		rateContract.getRateContractDetails().addAll(rateContractDetailNonSorList);
		Indent indent = (Indent) getPersistenceService().find("from Indent where indentNumber=?", rateContract.getIndent().getIndentNumber());
		Map<String,Object> reportParams = new HashMap<String,Object>();
		if(indent != null){
			reportParams.put("indent",indent);
		}
		if(rateContract != null){
			reportParams.put("rateContract", rateContract);
		}
		reportParams.put("sorSize", rateContractDetailSorList.size());
		reportParams.put("nonsorSize", rateContractDetailNonSorList.size());
		ReportRequest reportInput = new ReportRequest("RC",new ArrayList<Object>(), reportParams);  
		ReportOutput reportOutput = reportService.createReport(reportInput);   
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			pdfInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
		return PRINT;
	}
	
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	
	public InputStream getPdfInputStream() {
		return pdfInputStream;
	}

	public void setPdfInputStream(InputStream pdfInputStream) {
		this.pdfInputStream = pdfInputStream;
	}

	public void setFileNum(String fileNum) {
		this.fileNum = fileNum;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setRateContractWorkflowService(WorkflowService<RateContract> workflowService) {
		this.workflowService = workflowService;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}	
	
	public void setNextEmployeeName(String nextEmployeeName) {
		this.nextEmployeeName = nextEmployeeName;
	}
	
	public void setNextDesignation(String nextDesignation) {
		this.nextDesignation = nextDesignation;
	}

	public String getNextEmployeeName() {
		return nextEmployeeName;
	}

	public String getNextDesignation() {
		return nextDesignation;
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

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getNoticeNumberTemp() {
		return noticeNumberTemp;
	}

	public void setNoticeNumberTemp(String noticeNumberTemp) {
		this.noticeNumberTemp = noticeNumberTemp;
	}

	public String getResponseNumberTemp() {
		return responseNumberTemp;
	}

	public void setResponseNumberTemp(String responseNumberTemp) {
		this.responseNumberTemp = responseNumberTemp;
	}
	
	
}
