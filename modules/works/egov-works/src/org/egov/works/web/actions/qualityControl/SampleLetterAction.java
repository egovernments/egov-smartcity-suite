package org.egov.works.web.actions.qualityControl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.service.CommonsService;
import org.egov.dms.services.FileManagementService;
import org.egov.infstr.ValidationException;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.rjbac.user.User;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.web.actions.workflow.GenericWorkFlowAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.works.models.qualityControl.SampleLetterDetails;
import org.egov.works.models.qualityControl.SampleLetterHeader;
import org.egov.works.models.qualityControl.TestSheetDetails;
import org.egov.works.models.qualityControl.TestSheetHeader;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksService;
import org.egov.works.services.qualityControl.SampleLetterService;
import org.egov.works.services.qualityControl.TestSheetHeaderService;

@Results({
@Result(name=SampleLetterAction.EXPORT_SAMPLE_LETTER_PDF,type="stream",location="PdfInputStream", params={"inputName","PdfInputStream","contentType","application/pdf","contentDisposition","no-cache;filename=SampleLetter.pdf"}),
@Result(name=SampleLetterAction.EXPORT_COVERING_LETTER_PDF,type="stream",location="PdfInputStream", params={"inputName","PdfInputStream","contentType","application/pdf","contentDisposition","no-cache;filename=CoveringLetter.pdf"})
})
public class SampleLetterAction extends GenericWorkFlowAction{
	
	private SampleLetterHeader sampleLetterHeader= new SampleLetterHeader();
	private String sourcePage;
	private Long testSheetId;
	private TestSheetHeader tSheetHeader;
	private TestSheetHeaderService testSheetHeaderService;
	private Long id;
	private EisUtilService eisService;
	private Integer departmentId;
	private static final String ASSIGNED_USER_LIST1 = "assignedUserList1";
	private List<SampleLetterDetails> actionSampleLetterDetails = new LinkedList<SampleLetterDetails>();
	private static final Logger LOGGER = Logger.getLogger(SampleLetterAction.class);
	private CommonsService commonsService;
	private EmployeeService employeeService;
	private AbstractEstimateService abstractEstimateService;
	private WorkflowService<SampleLetterHeader> workflowService;
	private SampleLetterService sampleLetterService;
	private static final String SAMPLECOLLECTED_USER_LIST1 = "sampleCollectedBy1";
	private static final String SAMPLECOLLECTED_USER_LIST2 = "sampleCollectedBy2";
	private static final String FILENOTIFY_USER_LIST2 = "fileNotifyUsersList";
	private String workOrderNumber;
	private PersonalInformationService personalInformationService;
	private static final String SAVE_ACTION = "Save";
	private static final String APPROVE_ACTION = "Approve";
	private static final String CANCEL_ACTION = "Cancel";
	private String messageKey;
	private WorksService worksService; 
	private Integer defaultDesgnId1;
	private Integer defaultDesgnId2;
	
	private Long slId;
	private ReportService reportService;
	private InputStream pdfInputStream;
	private static final String COMMA = ",";
	public static final String  EXPORT_SAMPLE_LETTER_PDF = "exportSampleLetterPdf";
	public static final String  EXPORT_COVERING_LETTER_PDF = "exportCoveringLetterPdf";
	private FileManagementService fileMgmtService;
	private String fileNotifySuccessMessage;
	private Integer notifyUserId;
	
	@Override
	public StateAware getModel() {
		// TODO Auto-generated method stub
		return sampleLetterHeader;
	}
	
	@SkipValidation
	public String newform(){  
		return NEW;  
	} 
	
	public String edit(){
		return NEW;
	}	
	
	public SampleLetterAction() {
        addRelatedEntity("sampleCollectedBy1", PersonalInformation.class);
        addRelatedEntity("sampleCollectedBy2", PersonalInformation.class);
	} 
	
	public void prepare() {
	   AjaxSampleLetterAction ajaxSampleLetterAction = new AjaxSampleLetterAction();
	   if(slId!=null)
		   return;
	   if(id!=null){
		   sampleLetterHeader=sampleLetterService.findById(id, false);
		   tSheetHeader=sampleLetterHeader.getTestSheetHeader();
	   }
	   
	   if(testSheetId != null && sourcePage.equalsIgnoreCase("createSampleLetter")){
		   tSheetHeader=testSheetHeaderService.findById(testSheetId, false);
			List<AppConfigValues> appConfigList = worksService.getAppConfigValue("Works","SAMPLELETTER_DEFAULT_DESIGNATIONS");
			List designationNames = new LinkedList();
			if(appConfigList!=null && !appConfigList.isEmpty()){
				if(appConfigList.get(0).getValue()!="" && appConfigList.get(0).getValue()!=null){
					String[] desgnVals=appConfigList.get(0).getValue().split(",");
					for(int i=0; i<desgnVals.length;i++)
						designationNames.add(desgnVals[i]);
				}
			}
			if(designationNames.get(0)!="" || designationNames.get(0)!=null){
				DesignationMaster dsm=(DesignationMaster)persistenceService.find("from DesignationMaster where designationName=?", designationNames.get(0));
				if(dsm!=null)
					defaultDesgnId1=dsm.getDesignationId();
			} 
			if(designationNames.get(1)!="" || designationNames.get(1)!=null){
				DesignationMaster dsm=(DesignationMaster)persistenceService.find("from DesignationMaster where designationName=?", designationNames.get(1));
				if(dsm!=null)
					defaultDesgnId2=dsm.getDesignationId();
			}
	   }
		
		workOrderNumber=tSheetHeader.getWorkOrder().getWorkOrderNumber();
		departmentId=tSheetHeader.getWorkOrder().getWorkOrderEstimates().get(0).getEstimate().getExecutingDepartment().getId();
		List<DesignationMaster> designationMasters=new ArrayList<DesignationMaster>();
		designationMasters=eisService.getAllDesignationByDept(departmentId, new Date());
		super.prepare();
		setupDropdownDataExcluding("sampleCollectedBy1","sampleCollectedBy2");
		addDropdownData("designationMasterList", designationMasters);
		addDropdownData("designationMasterList1", designationMasters);

	     setDesignation1(getDesignation1FromParams());
	     setDesignation2(getDesignation2FromParams());
	     
		 populateUsersList1(ajaxSampleLetterAction,sampleLetterHeader.getDesignation1()!=null,departmentId!=null);
	     populateUsersList2(ajaxSampleLetterAction,sampleLetterHeader.getDesignation2()!=null,departmentId!=null);
	     
	     populateFileNotifyUsersList(ajaxSampleLetterAction);	   
	}
	
	private void populateUsersList1(AjaxSampleLetterAction ajaxSampleLetterAction, boolean desgId,boolean executingDeptPopulated){
        if (desgId && executingDeptPopulated && departmentId>0) {
        	ajaxSampleLetterAction.setPersonalInformationService(personalInformationService); 
            ajaxSampleLetterAction.setDesgId(Long.valueOf(sampleLetterHeader.getDesignation1().getDesignationId()));
            ajaxSampleLetterAction.setExecutingDepartment(departmentId);
            ajaxSampleLetterAction.getUsersForDesg();
            addDropdownData(SAMPLECOLLECTED_USER_LIST1,ajaxSampleLetterAction.getUserList());
        }
        else {
            addDropdownData(SAMPLECOLLECTED_USER_LIST1,Collections.EMPTY_LIST);
        }
    }
   
    private void populateUsersList2(AjaxSampleLetterAction ajaxSampleLetterAction, boolean desgId,boolean executingDeptPopulated){
        if (desgId && executingDeptPopulated && departmentId>0) {
        	ajaxSampleLetterAction.setPersonalInformationService(personalInformationService);
            ajaxSampleLetterAction.setDesgId(Long.valueOf(sampleLetterHeader.getDesignation2().getDesignationId()));
            ajaxSampleLetterAction.setExecutingDepartment(departmentId);
            ajaxSampleLetterAction.getUsersForDesg();
            addDropdownData(SAMPLECOLLECTED_USER_LIST2,ajaxSampleLetterAction.getUserList());
        }
        else {
            addDropdownData(SAMPLECOLLECTED_USER_LIST2,Collections.EMPTY_LIST);
        }
    }
    
    private void populateFileNotifyUsersList(AjaxSampleLetterAction ajaxSampleLetterAction){      
    	ajaxSampleLetterAction.setPersonalInformationService(personalInformationService);
    	DesignationMaster designationMaster = getDesignationForFileNotification();
    	if(designationMaster != null) {
	        ajaxSampleLetterAction.setDesgId(Long.valueOf(designationMaster.getDesignationId()));
	        ajaxSampleLetterAction.setExecutingDepartment(null);
	        ajaxSampleLetterAction.getUsersForDesg();
	        addDropdownData(FILENOTIFY_USER_LIST2,ajaxSampleLetterAction.getUserList());
    	}
    	else {
    		addDropdownData(FILENOTIFY_USER_LIST2,Collections.EMPTY_LIST);
    	}
    }
    
    private DesignationMaster getDesignationForFileNotification() {
		String designationName = worksService.getWorksConfigValue("SAMPLELETTER_NOTIFICATION_DESIGNATION");
		return (DesignationMaster)persistenceService.find("from DesignationMaster where designationName = ?", designationName);
	}
    
    protected void setDesignation1(Integer designationId) {
		if(designationId!=null)
			sampleLetterHeader.setDesignation1((DesignationMaster)persistenceService.find("from DesignationMaster where designationId=?", designationId));
	 }
    
    protected void setDesignation2(Integer designationId) {
		if(designationId!=null)
	    	sampleLetterHeader.setDesignation2((DesignationMaster)persistenceService.find("from DesignationMaster where designationId=?", designationId));
		 }
	 
    protected Integer getDesignation1FromParams() { 
		String[] ids = parameters.get("designation1");
		if (ids != null && ids.length > 0) {
			parameters.remove("designation1");
			String id = ids[0];
			if (id != null && id.length() > 0) {
				return Integer.parseInt(id);
			}
		}
		return null;
	}
    protected Integer getDesignation2FromParams() {
		String[] ids = parameters.get("designation2");
		if (ids != null && ids.length > 0) {
			parameters.remove("designation2");
			String id = ids[0];
			if (id != null && id.length() > 0) {
				return Integer.parseInt(id);
			}
		}
		return null;
	}
	
	@ValidationErrorPage(value=NEW)
	public String save() 
	{
		String actionName = parameters.get("actionName")[0]; 
		try{
			populateSampleLetterDetails(actionName);
			String status=null;
			if(getModel().getCurrentState()!=null)
				status=getModel().getCurrentState().getValue();
			else
				status="NEW";
			sampleLetterHeader.setTestSheetHeader(tSheetHeader);
			if(actionName.equalsIgnoreCase(SAVE_ACTION)){
				sampleLetterHeader.setEgwStatus(commonsService.getStatusByModuleAndCode("SampleLetter","NEW")); 
				if(id ==null){
					Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
					sampleLetterHeader.setSampleLetterNumber(sampleLetterService.generateSampleLetterNumber(sampleLetterHeader));
					sampleLetterHeader.setCoveringLetterNumber(sampleLetterService.generateCoveringLetterNumber(sampleLetterHeader));
					sampleLetterHeader = (SampleLetterHeader) workflowService.start(sampleLetterHeader, pos, "Sample Letter created.");
				}
				sampleLetterService.persist(sampleLetterHeader);
				addActionMessage("Sample Letter was Saved Successfully. Sample Letter Number : "+sampleLetterHeader.getSampleLetterNumber()+" Covering Letter Number : "+sampleLetterHeader.getCoveringLetterNumber()); 
			}
			else{  
				if(id ==null){
					sampleLetterHeader.setEgwStatus(commonsService.getStatusByModuleAndCode("SampleLetter","NEW")); 
					Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
					sampleLetterHeader.setSampleLetterNumber(sampleLetterService.generateSampleLetterNumber(sampleLetterHeader));
					sampleLetterHeader.setCoveringLetterNumber(sampleLetterService.generateCoveringLetterNumber(sampleLetterHeader));
					sampleLetterHeader = (SampleLetterHeader) workflowService.start(sampleLetterHeader, pos, approverComments);
					sampleLetterService.persist(sampleLetterHeader);
				}
				workflowService.transition(actionName, sampleLetterHeader, approverComments);
				sampleLetterService.persist(sampleLetterHeader);
				if(actionName.equalsIgnoreCase(APPROVE_ACTION)) {
					notifyOnApproval();
					addActionMessage("Sample Letter was Approved Successfully. Sample Letter Number : "+sampleLetterHeader.getSampleLetterNumber()+" Covering Letter Number : "+sampleLetterHeader.getCoveringLetterNumber());
				}
				else if(actionName.equalsIgnoreCase(CANCEL_ACTION))
					addActionMessage("Sample Letter "+sampleLetterHeader.getSampleLetterNumber()+" was Cancelled Successfully ");
			}
		
		}
		catch (ValidationException exception) {
			LOGGER.error("Error in SampleLetter save--"+exception.getStackTrace());
			exception.printStackTrace(); 
			throw exception;
		}  
		return SAVE_ACTION.equalsIgnoreCase(actionName)?EDIT:SUCCESS;
	}
	
	 protected void populateSampleLetterDetails(String actionName) { 
		 sampleLetterHeader.getSampleLetterDetails().clear();
		 for(SampleLetterDetails slDtls: actionSampleLetterDetails) { 
			 if(!(actionName.equalsIgnoreCase(APPROVE_ACTION) && slDtls.getSampleQuantity()==0)){
				 slDtls.setTestSheetDetails((TestSheetDetails)getPersistenceService().find("from TestSheetDetails where id = ?",slDtls.getTestSheetDetails().getId()));
				 sampleLetterHeader.addSampleLetterDetails(slDtls);
			 }
		 } 
	 }

	 public Map<String,Object> createHeaderParams(String mode)
		{
			Map<String,Object> reportParams = new HashMap<String,Object>();
			if(slId==null)
				return reportParams;
			SampleLetterHeader sl = (SampleLetterHeader) persistenceService.getSession().get(SampleLetterHeader.class, slId);
			List<SampleLetterDetails> sampleLetterDetailsList = sl.getSampleLetterDetails();
			StringBuffer sampleType = new StringBuffer("");
			if(sampleLetterDetailsList!=null && !sampleLetterDetailsList.isEmpty())
			{
				//THESE PARAMS ARE COMMON TO BOTH SAMPLE AND COVERING LETTERS
				reportParams.put("jurisdiction",sl.getTestSheetHeader().getWorkOrder().getWorkOrderEstimates().get(0).getEstimate().getWard().getName());
				reportParams.put("dept", sl.getTestSheetHeader().getWorkOrder().getWorkOrderEstimates().get(0).getEstimate().getExecutingDepartment().getDeptName());
				reportParams.put("date",new Date());
				reportParams.put("workName",sl.getTestSheetHeader().getWorkOrder().getWorkOrderEstimates().get(0).getEstimate().getName());
				reportParams.put("location", sl.getLocation());
				reportParams.put("contractor", sl.getTestSheetHeader().getWorkOrder().getContractor().getName());
				reportParams.put("samplingDate", sl.getSamplingDate());
				
				for(SampleLetterDetails det:sampleLetterDetailsList)
				{
					if(det!=null)
					{
						if(!sampleType.toString().contains(det.getTestSheetDetails().getTestMaster().getMaterialType().getName()))
						{
							sampleType.append(det.getTestSheetDetails().getTestMaster().getMaterialType().getName());
							sampleType.append(COMMA);
						}
					}
				}
				if(sampleType.toString().contains(COMMA))
				{
					sampleType.deleteCharAt(sampleType.lastIndexOf(COMMA));
				}
				reportParams.put("sampleType",sampleType.toString());
				
				//SPECIFIC PARAMS
				if(mode.equalsIgnoreCase("SAMPLE"))
				{
					reportParams.put("letterNo",sl.getSampleLetterNumber());
					reportParams.put("woNumber",sl.getTestSheetHeader().getWorkOrder().getWorkOrderNumber());
					reportParams.put("woDate",sl.getTestSheetHeader().getWorkOrder().getWorkOrderDate());
					reportParams.put("emp1", sl.getSampleCollectedBy1().getEmployeeName());
					reportParams.put("emp2", sl.getSampleCollectedBy2().getEmployeeName());
					reportParams.put("desig1", sl.getDesignation1().getDesignationName());
					reportParams.put("desig2", sl.getDesignation2().getDesignationName());
					reportParams.put("dispatchDate", sl.getDispatchDate());
				}
				else
				{
					reportParams.put("coveringLetterNo",sl.getCoveringLetterNumber());
					reportParams.put("castingDate",sl.getCastingDate());
				}
			}
			return reportParams;
		}	
		
		public String generateSampleLetterPdf() throws JRException,Exception{
			ReportRequest reportRequest; 
			reportRequest = new ReportRequest("SampleLetter",Collections.EMPTY_LIST, createHeaderParams("SAMPLE"));
			ReportOutput reportOutput = reportService.createReport(reportRequest); 
			if(reportOutput!=null && reportOutput.getReportOutputData()!=null)
				pdfInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData()); 
			return EXPORT_SAMPLE_LETTER_PDF; 
		}
		
		public String generateCoveringLetterPdf() throws JRException,Exception{
			ReportRequest reportRequest; 
			reportRequest = new ReportRequest("CoveringLetter", getReportData(), createHeaderParams("COVERING"));
			ReportOutput reportOutput = reportService.createReport(reportRequest); 
			if(reportOutput!=null && reportOutput.getReportOutputData()!=null)
				pdfInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData()); 
			return EXPORT_COVERING_LETTER_PDF; 
		}
		
		public List<Map<String, Object>> getReportData()
		{
			SampleLetterHeader sl = (SampleLetterHeader) persistenceService.getSession().get(SampleLetterHeader.class, slId);
			List<SampleLetterDetails> sampleLetterDetailsList = sl.getSampleLetterDetails();
			List<Map<String,Object>> reportMapList = new ArrayList<Map<String,Object>>();
			Map<String,Object>  reportMap = null;
			Map<String,Double> materialSampleNoMap = new HashMap<String, Double>();
			Map<String,String> materialTestsMap = new HashMap<String, String>();
			Double sampleNumber = 0d;
			String test = "";
			String materialName = "";
			for(SampleLetterDetails det:sampleLetterDetailsList)
			{
				if(det!=null)
				{
					materialName = det.getTestSheetDetails().getTestMaster().getMaterialType().getName();
					if(materialSampleNoMap.containsKey(materialName))
					{
						sampleNumber = materialSampleNoMap.get(materialName);
						sampleNumber+=det.getSampleQuantity();
						materialSampleNoMap.put(materialName , sampleNumber);
						test = materialTestsMap.get(materialName);
						test = test +COMMA+ det.getTestSheetDetails().getTestMaster().getTestName();
						materialTestsMap.put(materialName , test);
					}
					else
					{
						materialSampleNoMap.put(materialName, new Double (det.getSampleQuantity()));
						materialTestsMap.put(materialName, det.getTestSheetDetails().getTestMaster().getTestName());
					}
				}
			}
			for (Map.Entry<String, Double> entry : materialSampleNoMap.entrySet()) {
				reportMap = new HashMap<String, Object>();
				reportMap.put("material", entry.getKey());
				reportMap.put("sampleNo", entry.getValue());
				reportMap.put("tests", materialTestsMap.get(entry.getKey()));
				reportMapList.add(reportMap);
			}
			return reportMapList;
		} 
	 
	private void notifyOnApproval() throws ValidationException {
		final HashMap<String, String> fileDetails = new HashMap<String, String>();
		fileDetails.put("fileCategory", "INTER DEPARTMENT");
		fileDetails.put("filePriority", "MEDIUM");
		fileDetails.put("fileHeading", getText("sampleLetter.fileNotification.fileHeading"));
		fileDetails.put("fileSummary", getText("sampleLetter.fileNotification.fileSummary",new String[]{sampleLetterHeader.getSampleLetterNumber(), sampleLetterHeader.getCoveringLetterNumber()}));
		fileDetails.put("fileSource", "INTER DEPARTMENT");
		fileDetails.put("senderAddress", "");
		if(sampleLetterHeader.getCreatedBy().getMiddleName() != null && sampleLetterHeader.getCreatedBy().getLastName() != null)
			fileDetails.put("senderName", sampleLetterHeader.getCreatedBy().getFirstName()+" "+sampleLetterHeader.getCreatedBy().getMiddleName()+" "+sampleLetterHeader.getCreatedBy().getLastName());
		else if(sampleLetterHeader.getCreatedBy().getMiddleName() != null && sampleLetterHeader.getCreatedBy().getLastName() == null)
			fileDetails.put("senderName", sampleLetterHeader.getCreatedBy().getFirstName()+" "+sampleLetterHeader.getCreatedBy().getMiddleName());
		else if(sampleLetterHeader.getCreatedBy().getMiddleName() == null && sampleLetterHeader.getCreatedBy().getLastName() != null)
			fileDetails.put("senderName", sampleLetterHeader.getCreatedBy().getFirstName()+" "+sampleLetterHeader.getCreatedBy().getLastName());
		else
			fileDetails.put("senderName", sampleLetterHeader.getCreatedBy().getFirstName());
		fileDetails.put("senderPhone", "");
		fileDetails.put("senderEmail", "");
		User user = null ;
		PersonalInformation emp = null;
		if(notifyUserId != null) {
			emp = employeeService.getEmloyeeById(notifyUserId); 
			user = emp.getUserMaster();
			fileMgmtService.generateFileNotification(fileDetails, user.getId().toString()); 
			fileNotifySuccessMessage=getText("sampleLetter.approved.fileNotified", new String[]{emp.getName()});
		}		
	}
		
	public SampleLetterHeader getSampleLetterHeader() {
		return sampleLetterHeader;
	}

	public void setSampleLetterHeader(SampleLetterHeader sampleLetterHeader) {
		this.sampleLetterHeader = sampleLetterHeader;
	}

	public String getSourcePage() {
		return sourcePage;
	}

	public void setSourcePage(String sourcePage) { 
		this.sourcePage = sourcePage;
	}

	public Long getTestSheetId() {
		return testSheetId;
	}

	public void setTestSheetId(Long testSheetId) {
		this.testSheetId = testSheetId;
	}

	public TestSheetHeader getTSheetHeader() {
		return tSheetHeader;
	}

	public void setTSheetHeader(TestSheetHeader tSheetHeader) {
		this.tSheetHeader = tSheetHeader;
	}

	public void setTestSheetHeaderService(
			TestSheetHeaderService testSheetHeaderService) {
		this.testSheetHeaderService = testSheetHeaderService;
	}

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public List<SampleLetterDetails> getActionSampleLetterDetails() {
		return actionSampleLetterDetails;
	}

	public void setActionSampleLetterDetails(
			List<SampleLetterDetails> actionSampleLetterDetails) {
		this.actionSampleLetterDetails = actionSampleLetterDetails;
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
	
	public void setSampleLetterWorkFlowService(WorkflowService<SampleLetterHeader> workflowService) {
		this.workflowService = workflowService;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey; 
	}

	public WorksService getWorksService() {
		return worksService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public Integer getDefaultDesgnId1() {
		return defaultDesgnId1;
	}

	public void setDefaultDesgnId1(Integer defaultDesgnId1) {
		this.defaultDesgnId1 = defaultDesgnId1;
	}

	public void setSlId(Long slId) {
		this.slId = slId;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public InputStream getPdfInputStream() {
		return pdfInputStream;
	}

	public Integer getDefaultDesgnId2() {
		return defaultDesgnId2;
	}

	public void setDefaultDesgnId2(Integer defaultDesgnId2) {
		this.defaultDesgnId2 = defaultDesgnId2;
	}

	public void setFileMgmtService(FileManagementService fileMgmtService) {
		this.fileMgmtService = fileMgmtService;
	}

	public String getFileNotifySuccessMessage() {
		return fileNotifySuccessMessage;
	}

	public void setFileNotifySuccessMessage(String fileNotifySuccessMessage) {
		this.fileNotifySuccessMessage = fileNotifySuccessMessage;
	}

	public Integer getNotifyUserId() {
		return notifyUserId;
	}

	public void setNotifyUserId(Integer notifyUserId) {
		this.notifyUserId = notifyUserId;
	}

}