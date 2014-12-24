package org.egov.works.web.actions.qualityControl;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.ValidationException;
import org.egov.infstr.mail.Email;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.utils.NumberUtil;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.pims.commons.Position;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.workflow.GenericWorkFlowAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.works.models.qualityControl.JobDetails;
import org.egov.works.models.qualityControl.JobHeader;
import org.egov.works.models.qualityControl.SampleLetterDetails;
import org.egov.works.models.qualityControl.SampleLetterHeader;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksService;
import org.egov.works.services.qualityControl.AllocateJobService;
import org.egov.works.services.qualityControl.SampleLetterService;

public class AllocateJobAction extends GenericWorkFlowAction{
	
	private JobHeader jobHeader = new JobHeader();
	private String sourcePage;
	private Long sampleLetterId;
	private Long id;
	private SampleLetterHeader slHeader;
	private SampleLetterService sampleLetterService;
	private String workOrderNumber;
	private String testSheetNumber;
	private String sampleLetterNumber;
	private String coveringLetterNumber;
	private List<JobDetails> actionJobDetails = new LinkedList<JobDetails>();
	private static final String SAVE_ACTION = "Save";
	private static final String APPROVE_ACTION = "Approve";
	private static final String CANCEL_ACTION = "Cancel";
	private static final Logger LOGGER = Logger.getLogger(AllocateJobAction.class);
	private CommonsService commonsService;
	private EmployeeService employeeService;
	private AbstractEstimateService abstractEstimateService;
	private AllocateJobService allocateJobService;
	private WorkflowService<JobHeader> workflowService;
	private WorksService worksService; 
	private Long workOrderId;
	private Long testSheetId;

	@Override
	public StateAware getModel() {
		// TODO Auto-generated method stub
		return jobHeader;
	}
	
	public void prepare() {
		if(sampleLetterId != null && sourcePage.equalsIgnoreCase("allocateJob")){
			slHeader=sampleLetterService.findById(sampleLetterId, false);
		}
		if(id!=null){
			jobHeader=allocateJobService.findById(id, false);
			slHeader=jobHeader.getSampleLetterHeader();
		}
		if(slHeader!=null){
			workOrderNumber=slHeader.getTestSheetHeader().getWorkOrder().getWorkOrderNumber();
			testSheetNumber=slHeader.getTestSheetHeader().getTestSheetNumber();
			sampleLetterNumber=slHeader.getSampleLetterNumber();
			coveringLetterNumber=slHeader.getCoveringLetterNumber();
			workOrderId=slHeader.getTestSheetHeader().getWorkOrder().getId();
			testSheetId=slHeader.getTestSheetHeader().getId();
			sampleLetterId=slHeader.getId();
		}
		super.prepare();
	}
	
	@SkipValidation
	public String newform(){  
		return NEW;  
	} 
	
	public String edit(){
		return NEW;
	}
	
	@ValidationErrorPage(value=NEW)
	public String save() 
	{
		String actionName = parameters.get("actionName")[0]; 
		try{
			populateJobDetails(actionName);
			String status=null;
			if(getModel().getCurrentState()!=null)
				status=getModel().getCurrentState().getValue();
			else
				status="NEW";
			jobHeader.setSampleLetterHeader(slHeader);
			if(actionName.equalsIgnoreCase(SAVE_ACTION)){
				jobHeader.setEgwStatus(commonsService.getStatusByModuleAndCode("AllocateJob","NEW")); 
				if(id ==null){
					Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
					jobHeader.setJobNumber(allocateJobService.generateJobNumber(jobHeader));
					jobHeader = (JobHeader) workflowService.start(jobHeader, pos,"Job Number Allocated");
				}
				allocateJobService.persist(jobHeader);
				worksService.createAccountDetailKey(jobHeader.getId(), "JOBNUMBER"); // Persists an Entry in ACCOUNTDETAILKEY Table
				addActionMessage(getText("jobNumber.save.success",new String[]{jobHeader.getJobNumber()}));
			}
			else{
				if(id ==null){ 
					jobHeader.setEgwStatus(commonsService.getStatusByModuleAndCode("AllocateJob","NEW")); 
					Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
					jobHeader.setJobNumber(allocateJobService.generateJobNumber(jobHeader));
					jobHeader = (JobHeader) workflowService.start(jobHeader, pos,approverComments);
					allocateJobService.persist(jobHeader);
					worksService.createAccountDetailKey(jobHeader.getId(), "JOBNUMBER"); // Persists an Entry in ACCOUNTDETAILKEY Table
				}
				workflowService.transition(actionName, jobHeader, approverComments);
				allocateJobService.persist(jobHeader);
				if(actionName.equalsIgnoreCase(APPROVE_ACTION) && jobHeader.getEgwStatus().getCode().equalsIgnoreCase("APPROVED")){
					String emailMsg;
					if(jobHeader.getSampleLetterHeader().getTestSheetHeader().getWorkOrder().getContractor().getEmail()!=null && StringUtils.isNotEmpty(jobHeader.getSampleLetterHeader().getTestSheetHeader().getWorkOrder().getContractor().getEmail())){
						emailMsg=sendEmail();
					}
					else
						emailMsg= getText("jobNumber.email.failure.msg2"); 
					addActionMessage(getText("jobNumber.approve.success",new String[]{jobHeader.getJobNumber()}).concat("<br><br>").concat(emailMsg));
				}
				else if(actionName.equalsIgnoreCase(CANCEL_ACTION))
					addActionMessage(getText("jobNumber.cancel.success",new String[]{jobHeader.getJobNumber()}));
			} 
		}
		catch (ValidationException exception) {
			LOGGER.error("Error in Allocate Job Number--"+exception.getStackTrace());
			exception.printStackTrace();  
			throw exception;
		}  
		return SAVE_ACTION.equalsIgnoreCase(actionName)?EDIT:SUCCESS; 
	}
	
	private String sendEmail() {
		String message;
		try{
			String body="";
			body=getText("jobNumber.approve.email.body",new String[]{jobHeader.getSampleLetterHeader().getTestSheetHeader().getWorkOrder().getContractor().getName(),NumberUtil.formatNumber(BigDecimal.valueOf(jobHeader.getJhTotalAmount())),jobHeader.getJobNumber(),getText("reports.title.corporation_name")});
			Email email = new Email.Builder(jobHeader.getSampleLetterHeader().getTestSheetHeader().getWorkOrder().getContractor().getEmail(),body)
			.subject(getText("JobNumber.email.subject",new String[]{jobHeader.getJobNumber()}))
			.build();
			email.send();
			message=getText("jobNumber.email.success.msg",new String[]{jobHeader.getSampleLetterHeader().getTestSheetHeader().getWorkOrder().getContractor().getName()});
		}catch(EGOVRuntimeException egovExp){
			message=getText("jobNumber.email.failure.msg1");
		}
		return message;
	}
	
	public void populateJobDetails(String actionName) { 
		jobHeader.getJobDetails().clear();
		 for(JobDetails jobDtls: actionJobDetails) { 
			 if(!(actionName.equalsIgnoreCase(APPROVE_ACTION) && jobDtls.getReceivedQuantity()==0)){
				 jobDtls.setSampleLetterDetails((SampleLetterDetails)getPersistenceService().find("from SampleLetterDetails where id = ?",jobDtls.getSampleLetterDetails().getId()));
				 jobHeader.addJobDetails(jobDtls);
			 }
		 } 
	 }

	public JobHeader getJobHeader() {
		return jobHeader;
	}

	public void setJobHeader(JobHeader jobHeader) {
		this.jobHeader = jobHeader;
	}

	public String getSourcePage() {
		return sourcePage;
	}

	public void setSourcePage(String sourcePage) {
		this.sourcePage = sourcePage;
	}

	public Long getSampleLetterId() {
		return sampleLetterId;
	}

	public void setSampleLetterId(Long sampleLetterId) {
		this.sampleLetterId = sampleLetterId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setSampleLetterService(SampleLetterService sampleLetterService) {
		this.sampleLetterService = sampleLetterService;
	}

	public String getWorkOrderNumber() {
		return workOrderNumber;
	}

	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
	}

	public String getTestSheetNumber() {
		return testSheetNumber;
	}

	public void setTestSheetNumber(String testSheetNumber) {
		this.testSheetNumber = testSheetNumber;
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

	public SampleLetterHeader getSlHeader() {
		return slHeader;
	}

	public void setSlHeader(SampleLetterHeader slHeader) {
		this.slHeader = slHeader;
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

	public void setAllocateJobService(AllocateJobService allocateJobService) {
		this.allocateJobService = allocateJobService;
	}

	public void setAllocateJobWorkFlowService(WorkflowService<JobHeader> workflowService) {
		this.workflowService = workflowService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public List<JobDetails> getActionJobDetails() {
		return actionJobDetails;
	}

	public void setActionJobDetails(List<JobDetails> actionJobDetails) {
		this.actionJobDetails = actionJobDetails;
	}

	public Long getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(Long workOrderId) {
		this.workOrderId = workOrderId;
	}

	public Long getTestSheetId() {
		return testSheetId;
	}

	public void setTestSheetId(Long testSheetId) {
		this.testSheetId = testSheetId;
	}
}
