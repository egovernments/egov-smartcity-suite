package org.egov.works.web.actions.qualityControl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.service.CommonsService;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.ValidationException;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.model.instrument.InstrumentType;
import org.egov.pims.commons.Position;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.workflow.GenericWorkFlowAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.qualityControl.JobDetails;
import org.egov.works.models.qualityControl.JobHeader;
import org.egov.works.models.qualityControl.TestResultDetails;
import org.egov.works.models.qualityControl.TestResultHeader;
import org.egov.works.models.qualityControl.TestResultMis;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksService;

public class TestResultAction extends GenericWorkFlowAction{
	private static final long serialVersionUID = 1L;
	private TestResultHeader testResultHeader = new TestResultHeader();
	private String testSheetNumber;
	private String workordernumber;
	private String jobNumber;
	private Long contractorId;
	private String sampleLetterNumber;
	private String coveringLetterNumber;
	private Date fromDate;
	private Date toDate;
	private PersistenceService<Contractor, Long> contractorService;
	private WorksService worksService;
	private List<Object> paramList;
	private static final Logger LOGGER = Logger.getLogger(TestResultAction.class);
	private Long jobHeaderId;
	private JobHeader jobHeaderObj;
	private String sourcePage;
	private CommonsService commonsService;
	private AbstractEstimateService abstractEstimateService;
	private WorkflowService<TestResultHeader> workflowService;
	private String messageKey;
	private String designation;
	private String employeeName;
	private PersistenceService<TestResultHeader,Long> testResultHeaderService;
	
	private static final String SEARCH_JOB = "searchJob";
	private static final String SAVE_ACTION = "Save";
	private static final String CLOSE_ACTION = "Close";
	private EmployeeService employeeService;
	private Long headerId;
	private EgovCommon egovCommon;

	@Override
	public StateAware getModel() {
		return testResultHeader;
	}
	
	@SkipValidation
	public String searchJob(){  
		return SEARCH_JOB;  
	}
	
	@SkipValidation
	public String newform(){  
		return NEW;  
	} 
	
	public String edit(){
		return NEW;
	}
	
	public void prepare()
	{
		super.prepare();
		if(jobHeaderId != null && sourcePage.equalsIgnoreCase("createTestResult")){
			jobHeaderObj=(JobHeader) persistenceService.find(" from JobHeader where id=? ",jobHeaderId);
			jobHeaderObj.getJobDetails();
			TestResultDetails trd = null;
			List<TestResultDetails> trDetailsList = new ArrayList<TestResultDetails>();
			if(jobHeaderObj!=null && jobHeaderObj.getJobDetails()!=null)
			{
				for(JobDetails jd : jobHeaderObj.getJobDetails() )
				{
					trd = new TestResultDetails();
					trd.setJobDetails(jd);
					trDetailsList.add(trd);
				}
			}
			testResultHeader.setTestResultDetails(trDetailsList);
			//Set Receipt details
			testResultHeader.setTestResultMis(getListWithReceiptDetails(jobHeaderId));
		}
		if(headerId!=null )
		{
			testResultHeader = (TestResultHeader) persistenceService.find(" from TestResultHeader where id=? ", headerId);
			jobHeaderObj = testResultHeader.getJobHeader(); 
		}
		addDropdownData("instrumentTypeList", persistenceService.findAllBy(" from InstrumentType where isActive='1' and type in ('cheque','cash','dd')  ") );
	}
	
	private List<TestResultMis> getListWithReceiptDetails(Long jhId)
	{
		List<TestResultMis> misList = new ArrayList<TestResultMis>();
		TestResultMis trMis = null;
		List<Map<String, Object>> receiptDetls = null;
		receiptDetls = getReceiptDetailsForJobHeaderId(jobHeaderId);
		BigDecimal amountRcv;
		String instrumentType;
		Long instrumentNo;
		Date instrumentDt;
		
		if (receiptDetls != null && !receiptDetls.isEmpty()) {
			for(Map<String, Object> instrumentMap: receiptDetls) {
				trMis = new TestResultMis();
				amountRcv = (BigDecimal) instrumentMap.get("amount");
				instrumentType = (String) instrumentMap.get("type");
				instrumentDt = (Date) instrumentMap.get("date");
				instrumentNo = (Long) instrumentMap.get("number");
				trMis.setAmountReceived(amountRcv.doubleValue());
				trMis.setChequeDate(instrumentDt);
				trMis.setChequeNumber(instrumentNo);
				trMis.setInstrumentType((InstrumentType) persistenceService.find("from InstrumentType where type = ?",instrumentType ));
				misList.add(trMis);
			}
		}
		
		return misList;
	}
	
	public String listJobs()
	{
		super.search();
		return SEARCH_JOB;
	}
	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		String reportSearchQuery = getSearchQuery();
		String countQry =  " select count(*)  " + reportSearchQuery;
		setPageSize(30);
		return new SearchQueryHQL(reportSearchQuery, countQry, paramList);
	}
	
	@ValidationErrorPage(value=NEW)
	public String save()
	{
		String actionName = parameters.get("actionName")[0];
		JobHeader jobHeader = null;
		JobDetails jobDetails;
		InstrumentType instrumentType;
		
		List<TestResultDetails> testResultDetailsList = null;
		List<TestResultMis> testResultMisList = null;
		List<TestResultDetails> newTestResultDetailsList = new ArrayList<TestResultDetails>();
		List<TestResultMis> newTestResultMisList = new ArrayList<TestResultMis>();
		if(jobHeaderId!=null)
			jobHeader = (JobHeader) persistenceService.find(" from JobHeader where id =? ", jobHeaderId);
		if(jobHeader==null && testResultHeader.getJobHeader()!=null && testResultHeader.getJobHeader().getId()!=null)
			jobHeader = (JobHeader) persistenceService.find(" from JobHeader where id =? ", testResultHeader.getJobHeader().getId());
		try{
			if(actionName.equalsIgnoreCase(SAVE_ACTION)){
				testResultHeader.setEgwStatus(commonsService.getStatusByModuleAndCode("TestResultHeader","NEW"));
				testResultHeader.setJobHeader(jobHeader);
				testResultDetailsList = testResultHeader.getTestResultDetails();
				testResultMisList = testResultHeader.getTestResultMis();
				if(testResultDetailsList!=null && ! testResultDetailsList.isEmpty())
				{
					for(TestResultDetails trDtl :testResultDetailsList)
					{
						if(trDtl!=null && trDtl.getJobDetails()!=null && trDtl.getJobDetails().getId()!=null)
						{
							jobDetails = (JobDetails) persistenceService.find(" from JobDetails where id = ? ", trDtl.getJobDetails().getId() );
							trDtl.setJobDetails(jobDetails);
							newTestResultDetailsList.add(trDtl);
						}
					}
				}
				if(testResultMisList!=null && ! testResultMisList.isEmpty())
				{
					for(TestResultMis trMis :testResultMisList)
					{
						if(trMis!=null && trMis.getInstrumentType()!=null && trMis.getInstrumentType().getId()!=null)
						{
							instrumentType = (InstrumentType) persistenceService.find(" from InstrumentType where id = ? ", trMis.getInstrumentType().getId());
							trMis.setInstrumentType(instrumentType);
							newTestResultMisList.add(trMis);
						}
					}
				}
				testResultHeader.setTestResultDetails(newTestResultDetailsList);
				testResultHeader.setTestResultMis(newTestResultMisList);
				if(headerId ==null){
					Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
					testResultHeader = (TestResultHeader) workflowService.start(testResultHeader, pos, "Test Result created.");
				}
				testResultHeaderService.persist(testResultHeader);
				messageKey="testResult."+actionName;
				getDesignation(testResultHeader);
				addActionMessage(getText(messageKey,"The Test Result was saved successfully "));
			}
			else{  
				if(headerId ==null){
					
					testResultHeader.setJobHeader(jobHeader);
					testResultDetailsList = testResultHeader.getTestResultDetails();
					testResultMisList = testResultHeader.getTestResultMis();
					if(testResultDetailsList!=null && ! testResultDetailsList.isEmpty())
					{
						for(TestResultDetails trDtl :testResultDetailsList)
						{
							if(trDtl!=null && trDtl.getJobDetails()!=null && trDtl.getJobDetails().getId()!=null)
							{
								jobDetails = (JobDetails) persistenceService.find(" from JobDetails where id = ? ", trDtl.getJobDetails().getId() );
								trDtl.setJobDetails(jobDetails);
								newTestResultDetailsList.add(trDtl);
							}
						}
					}
					if(testResultMisList!=null && ! testResultMisList.isEmpty())
					{
						for(TestResultMis trMis :testResultMisList)
						{
							if(trMis!=null && trMis.getInstrumentType()!=null && trMis.getInstrumentType().getId()!=null)
							{
								instrumentType = (InstrumentType) persistenceService.find(" from InstrumentType where id = ? ", trMis.getInstrumentType().getId());
								trMis.setInstrumentType(instrumentType);
								newTestResultMisList.add(trMis);
							}
						}
					}
					testResultHeader.setTestResultDetails(newTestResultDetailsList);
					testResultHeader.setTestResultMis(newTestResultMisList);
					
					testResultHeader.setEgwStatus(commonsService.getStatusByModuleAndCode("TestResultHeader","NEW")); 
					Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
					testResultHeader = (TestResultHeader) workflowService.start(testResultHeader, pos, "Test Result created.");
				}
				workflowService.transition(actionName, testResultHeader, approverComments);
				testResultHeaderService.persist(testResultHeader);
				getDesignation(testResultHeader);
				messageKey="testResult."+actionName;
			}
		}
		catch (ValidationException exception) {
			LOGGER.error("Error in TestResult save--"+exception.getStackTrace());
			exception.printStackTrace(); 
			throw exception;
		}
		if(actionName.equalsIgnoreCase(CLOSE_ACTION))
			messageKey="The Test Result was closed";
		return SAVE_ACTION.equalsIgnoreCase(actionName)?EDIT:SUCCESS;
	}
	
	private String getSearchQuery() {
		LOGGER.debug("---------Beginning of getSearchQuery---------");
		paramList = new ArrayList<Object>();
		StringBuilder dynQuery = new StringBuilder(800);
		dynQuery.append(" from JobHeader jh where jh.id is not null and jh.id not in (select tr.jobHeader.id from TestResultHeader tr where tr.jobHeader.id=jh.id and tr.egwStatus.code!='CANCELLED') ");
		if(testSheetNumber!=null && !StringUtils.isBlank(testSheetNumber))
		{
			dynQuery.append(" and UPPER(jh.sampleLetterHeader.testSheetHeader.testSheetNumber) like '%'||?||'%'");
			paramList.add(StringUtils.trim(testSheetNumber).toUpperCase());
		}
		if(workordernumber!=null && !StringUtils.isBlank(workordernumber))
		{
			dynQuery.append(" and UPPER(jh.sampleLetterHeader.testSheetHeader.workOrder.workOrderNumber) like '%'||?||'%'");
			paramList.add(StringUtils.trim(workordernumber).toUpperCase());
		}
		if(jobNumber!=null && !StringUtils.isBlank(jobNumber))
		{
			dynQuery.append(" and UPPER(jh.jobNumber) like '%'||?||'%'");
			paramList.add(StringUtils.trim(jobNumber).toUpperCase());
		}
		if(sampleLetterNumber!=null && !StringUtils.isBlank(sampleLetterNumber))
		{
			dynQuery.append(" and UPPER(jh.sampleLetterHeader.sampleLetterNumber) like '%'||?||'%'");
			paramList.add(StringUtils.trim(sampleLetterNumber).toUpperCase());
		}
		if(coveringLetterNumber!=null && !StringUtils.isBlank(coveringLetterNumber))
		{
			dynQuery.append(" and UPPER(jh.sampleLetterHeader.coveringLetterNumber) like '%'||?||'%'");
			paramList.add(StringUtils.trim(coveringLetterNumber).toUpperCase());
		}
		if(contractorId!=null && contractorId!=-1)
		{
			dynQuery.append(" and jh.sampleLetterHeader.testSheetHeader.workOrder.contractor.id=? ");
			paramList.add(contractorId);
		}
		if(fromDate!=null && toDate!=null)
		{
			dynQuery.append(" and jh.jobDate between ? and ? ");
			paramList.add(fromDate);
			paramList.add(toDate);
		}
		LOGGER.debug("---------End of getSearchQuery---------");
		return dynQuery.toString();
	}

	public Map<String,Object> getContractorForTestResult() {
		Map<String,Object> contractorsList = new LinkedHashMap<String, Object>();
		List<Contractor> contractorList=contractorService.findAllByNamedQuery("getContractorsWithJobNo");
		
		if(contractorList!=null && !contractorList.isEmpty()) {
			for(Contractor contractor :contractorList){
				contractorsList.put(contractor.getId()+"", contractor.getCode()+" - "+contractor.getName());
			}			
		}
		return contractorsList; 
	}
	
	public void getDesignation(TestResultHeader testResultHeader){
		/* start for customizing workflow message display */
		if(testResultHeader.getCurrentState()!= null 
				&& !"NEW".equalsIgnoreCase(testResultHeader.getCurrentState().getValue())) {
			String result = worksService.getEmpNameDesignation(testResultHeader.getState().getOwner(),testResultHeader.getState().getCreatedDate());
			if(result != null && !"@".equalsIgnoreCase(result)) {
				String empName = result.substring(0,result.lastIndexOf('@'));
				String designation =result.substring(result.lastIndexOf('@')+1,result.length());
				setEmployeeName(empName);
				setDesignation(designation);
			}
		}
		/* end */	
	}
	
	public List<Map<String, Object>> getReceiptDetailsForJobHeaderId(Long jobHeaderId) {
		Integer accountdetailType = null;
		Accountdetailtype accountdetailtypeObj=worksService.getAccountdetailtypeByName("JOBNUMBER");
		if(accountdetailtypeObj!=null && accountdetailtypeObj.getId()!=null)
			accountdetailType=accountdetailtypeObj.getId();
		
		return egovCommon.getInstrumentsDetailsForSubledgerTypeAndKey(accountdetailType, jobHeaderId.intValue(), new Date());	
	}

	public String getTestSheetNumber() {
		return testSheetNumber;
	}

	public void setTestSheetNumber(String testSheetNumber) {
		this.testSheetNumber = testSheetNumber;
	}

	public String getWorkordernumber() {
		return workordernumber;
	}

	public void setWorkordernumber(String workordernumber) {
		this.workordernumber = workordernumber;
	}

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public Long getContractorId() {
		return contractorId;
	}

	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}

	public String getSampleLetterNumber() {
		return sampleLetterNumber;
	}

	public void setSampleLetterNumber(String sampleLetterNumber) {
		this.sampleLetterNumber = sampleLetterNumber;
	}

	public String getCoveringLetterNumber() {
		return coveringLetterNumber;
	}

	public void setCoveringLetterNumber(String coveringLetterNumber) {
		this.coveringLetterNumber = coveringLetterNumber;
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

	public void setContractorService(
			PersistenceService<Contractor, Long> contractorService) {
		this.contractorService = contractorService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public String getSourcePage() {
		return sourcePage;
	}

	public void setSourcePage(String sourcePage) {
		this.sourcePage = sourcePage;
	}

	public JobHeader getJobHeaderObj() {
		return jobHeaderObj;
	}

	public void setJobHeaderObj(JobHeader jobHeaderObj) {
		this.jobHeaderObj = jobHeaderObj;
	}

	public Long getJobHeaderId() {
		return jobHeaderId;
	}

	public void setJobHeaderId(Long jobHeaderId) {
		this.jobHeaderId = jobHeaderId;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public Long getHeaderId() {
		return headerId;
	}

	public void setHeaderId(Long headerId) {
		this.headerId = headerId;
	}
	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
		
	public void setTestResultWorkFlowService(WorkflowService<TestResultHeader> workflowService) {
		this.workflowService = workflowService;
	}

	public void setTestResultHeaderService(
			PersistenceService<TestResultHeader, Long> testResultHeaderService) {
		this.testResultHeaderService = testResultHeaderService;
	}

	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}
	
	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	} 

}