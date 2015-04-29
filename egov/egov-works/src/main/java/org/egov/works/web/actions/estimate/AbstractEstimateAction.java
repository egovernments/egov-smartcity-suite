/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.web.actions.estimate;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.asset.model.Asset;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.Fundsource;
import org.egov.commons.service.CommonsService;
import org.egov.egf.commons.EgovCommon;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commonMasters.EgUom;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.workflow.Action;
import org.egov.model.budget.BudgetUsage;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.Activity;
import org.egov.works.models.estimate.AssetsForEstimate;
import org.egov.works.models.estimate.MultiYearEstimate;
import org.egov.works.models.estimate.OverheadValue;
import org.egov.works.models.estimate.WorkType;
import org.egov.works.models.masters.DepositCode;
import org.egov.works.models.masters.Overhead;
import org.egov.works.models.masters.ScheduleOfRate;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.ContractorBillService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Results({
	@Result(name=AbstractEstimateAction.PRINT,type="StreamResult.class",location="XlsInputStream", params={"inputName","XlsInputStream","contentType","application/xls","contentDisposition","no-cache;filename=AbstractEstimate-BillOfQuantites.xls"})
})
public class AbstractEstimateAction extends BaseFormAction {
	private static final Logger logger = Logger.getLogger(AbstractEstimateAction.class);
	private static final String CANCEL_ACTION = "cancel";
	private static final String SAVE_ACTION = "save";
	private static final Object REJECT_ACTION = "reject";
	private static final String SOURCE_SEARCH = "search";
	private static final String SOURCE_INBOX = "inbox";
	private static final String MODULE_NAME = "Works";
	private static final String KEY_NAME = "SKIP_BUDGET_CHECK";
	private static final String MAPS = "maps";
	private AbstractEstimate abstractEstimate = new AbstractEstimate();
	private List<Activity> sorActivities = new LinkedList<Activity>();
	private List<Activity> nonSorActivities = new LinkedList<Activity>();
	private List<OverheadValue> actionOverheadValues = new LinkedList<OverheadValue>();
	private List<AssetsForEstimate> actionAssetValues = new LinkedList<AssetsForEstimate>();
	private List<MultiYearEstimate> actionMultiYearEstimateValues = new LinkedList<MultiYearEstimate>();
	private AbstractEstimateService abstractEstimateService;
	@Autowired
        private EmployeeService employeeService;
	@Autowired
        private UserService userService;
	private EmployeeView estimatePreparedByView;
	private WorkflowService<AbstractEstimate> workflowService;
	private String messageKey;
	private String sourcepage="";
	private String assetStatus;  
	private Integer approverUserId;
	@Autowired
        private CommonsService commonsService;
	private Date financialYearStartDate;
	private Long departmentId;  
	private Integer designationId; 
	private String approverComments;
	private Long stateValue;
	private String estimateValue;
	private List usersInOldAndNewExecutingDepartment;
	private ReportService reportService;
	public static final String BOQ= "Bill Of Qunatities";
	private InputStream xlsInputStream;
	public static final String PRINT = "print"; 
	private String mode="";
	private boolean isAllowEstDateModify=false;	
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",new Locale("en","IN"));
	private static final String ALLOW_ESTDATE_EDIT_KEYNAME = "ALLOW_EST_DATE_EDIT";
	private static final String ALLOW_ESTDATE_EDIT_YES = "YES";
	private static final String ALLOW_ESTDATE_EDIT_NO = "NO";
	private String loggedInUserEmployeeCode;
	
	/* added by prashanth on 2nd nov 09 for disp user and desgination in success page*/
	String employeeName;
	String designation;
	private WorksService worksService; 
	private PersonalInformationService personalInformationService;
	
	private Long estimateId;
	private String cancellationReason;
	private String cancelRemarks;
	private String errorCode;
	private String mapMode;
	private String latitude;
	private String longitude;
	private EisUtilService eisService;
	private boolean digitalSign;
	private List<Object> woDetails;
	private List<Object> wpDetails;
	private BigDecimal paymentReleased =BigDecimal.ZERO;
	private EgovCommon egovCommon;
	private ContractorBillService contractorBillService;

	public String getMessageKey() {
		return messageKey;
	}

	private String currentFinancialYearId;	  
	private Long id;
	private double utilizedAmount=0d;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AbstractEstimateAction() {
		addRelatedEntity("fundSource",Fundsource.class);
		addRelatedEntity("userDepartment", Department.class);
		addRelatedEntity("executingDepartment", Department.class);
		addRelatedEntity("ward", Boundary.class);
		addRelatedEntity("type", WorkType.class);
		addRelatedEntity("category", EgwTypeOfWork.class);
		addRelatedEntity("parentCategory", EgwTypeOfWork.class);
		addRelatedEntity("depositCode", DepositCode.class);
	}

	public String edit(){
		
		if(SOURCE_INBOX.equalsIgnoreCase(sourcepage)){
			User user=userService.getUserById(worksService.getCurrentLoggedInUserId());
			boolean isValidUser=worksService.validateWorkflowForUser(abstractEstimate,user);
			if(isValidUser){
					throw new EGOVRuntimeException("Error: Invalid Owner - No permission to view this page.");
			}
		}
		else if(StringUtils.isEmpty(sourcepage)){
			sourcepage="search";
		}
		getWorkOrderDetails();
		return NEW;
	}
	
	private void getWorkOrderDetails(){
		ArrayList<Integer> projectCodeIdList = new ArrayList<Integer>();
		if(WorksConstants.ADMIN_SANCTIONED_STATUS.equalsIgnoreCase(abstractEstimate.getEgwStatus().getCode())){
			woDetails = abstractEstimateService.getWODetailsForEstimateId(id);
			wpDetails = abstractEstimateService.getWPDetailsForEstimateId(id);
			if(abstractEstimate.getProjectCode() != null){
				projectCodeIdList.add(abstractEstimate.getProjectCode().getId().intValue());
				paymentReleased = contractorBillService.getTotalExpenditure(projectCodeIdList,WorksConstants.PROJECTCODE);
			}
		}
	}
	
	@SkipValidation
	public String viewBillOfQuantitiesXls() throws JRException,Exception{
		AbstractEstimate estimate= abstractEstimateService.findById(id, false); 
		ReportRequest reportRequest = new ReportRequest("BillOfQuantities",estimate.getSORActivities() , 
				createHeaderParams(estimate,BOQ));
		reportRequest.setReportFormat(FileFormat.XLS);
		ReportOutput reportOutput = reportService.createReport(reportRequest); 
		if(reportOutput!=null && reportOutput.getReportOutputData()!=null)
			xlsInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData()); 
		return PRINT;  
	}
	
	private Map createHeaderParams(AbstractEstimate estimate, String type){
		Map<String,Object> reportParams = new HashMap<String,Object>();
		if(type.equalsIgnoreCase(BOQ)){
			reportParams.put("workName", estimate.getName());
			reportParams.put("deptName",estimate.getExecutingDepartment().getName());
			reportParams.put("estimateNo", estimate.getEstimateNumber());
			reportParams.put("activitySize", (estimate.getSORActivities()==null?0:estimate.getSORActivities().size()));
			reportParams.put("corpName", getText("reports.title.corporation_name"));
			reportParams.put("NonSOR_Activities", estimate.getNonSORActivities() );
			reportParams.put("grandTotalAmt", getGrandTotalForEstimate(estimate));
		}
		return reportParams; 
	}
	
	private Double getGrandTotalForEstimate(AbstractEstimate estimate)
	{
		Double total= 0d;
		for(Activity act: estimate.getActivities())
		{
			total +=act.getAmount().getValue();
		}
		return total;
	}
	
	public String workflowHistory(){
		return "history";
	}
	
	public Object getModel() {
		return abstractEstimate;
	}
	
	protected void setModel(AbstractEstimate abstractEstimate) {
		this.abstractEstimate = abstractEstimate;
	}

	public void prepare() {
		AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
		ajaxEstimateAction.setPersistenceService(getPersistenceService());
		ajaxEstimateAction.setEmployeeService(employeeService);
		ajaxEstimateAction.setPersonalInformationService(personalInformationService);
		ajaxEstimateAction.setEisService(eisService);
		
		if (id != null && EDIT.equals("edit")) {
			 abstractEstimate= abstractEstimateService.findById(id, false);
			 abstractEstimate = abstractEstimateService.merge(abstractEstimate);
			 if(abstractEstimate!=null && abstractEstimate.getEstimatePreparedBy()!=null){
				 estimatePreparedByView = (EmployeeView) getPersistenceService().find("from EmployeeView where id = ?", abstractEstimate.getEstimatePreparedBy().getIdPersonalInformation());
			 }
		 }
		
		super.prepare();
		
		// estimatePreparedBy has to be handled differently (as its id is idPersonalInformation)
		//do not set estimate prepared by if the request is from search/inbox page
		if(!(SOURCE_SEARCH.equals(getSourcepage()) || SOURCE_INBOX.equals(getSourcepage()))){
		 setEstimatePreparedBy(getIdPersonalInformationFromParams());
		}
		 
		 CFinancialYear financialYear = getCurrentFinancialYear();
		 if(financialYear!=null) {
			 currentFinancialYearId=financialYear.getId().toString();
		 }
		 
		setupDropdownDataExcluding("ward","category","parentCategory","fundSource","depositCode");
		 
		addDropdownData("parentCategoryList", getPersistenceService().findAllBy("from EgwTypeOfWork etw1 where etw1.parentid is null"));
		List<EgUom> uomList = getPersistenceService().findAllBy("from EgUom  order by upper(uom)");
		if ((id == null && abstractEstimate.getEgwStatus() == null) || ("roadCutDepositWorks".equals(sourcepage)) ||
				(!SOURCE_SEARCH.equals(sourcepage) && (abstractEstimate.getEgwStatus() != null && abstractEstimate.getEgwStatus().getCode().equals("REJECTED")))
				|| (id != null && abstractEstimate.getEgwStatus() != null && abstractEstimate.getEgwStatus().getCode().equals("NEW"))) {
			uomList = abstractEstimateService.prepareUomListByExcludingSpecialUoms(uomList);
		}
		addDropdownData("uomList", uomList);
		addDropdownData("financialYearList", getPersistenceService().findAllBy("from CFinancialYear where isActive=1"));
		addDropdownData("scheduleCategoryList", getPersistenceService().findAllBy("from ScheduleCategory order by upper(code)")); 
		
		Assignment latestAssignment = abstractEstimateService.getLatestAssignmentForCurrentLoginUser();
		if(latestAssignment != null) {
			departmentId=latestAssignment.getDeptId().getId();
			loggedInUserEmployeeCode = latestAssignment.getEmployee().getCode();
		}
		populateCategoryList(ajaxEstimateAction, abstractEstimate.getParentCategory() != null);
		populatePreparedByList(ajaxEstimateAction, abstractEstimate.getExecutingDepartment() != null);
		populateOverheadsList(ajaxEstimateAction, abstractEstimate.getEstimateDate()!=null);
		
		try {
			addDropdownData("fundSourceList",commonsService.getAllActiveIsLeafFundSources());
		} catch (EGOVException e) {
			logger.error("Unable to load fund source information >>>"+e.getMessage());
			addFieldError("fundsourceunavailable", "Unable to load fund source information");
		}
		
		if(abstractEstimate!=null && abstractEstimate.getEgwStatus()!=null 
				&& abstractEstimate.getEgwStatus().getCode().equals(AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString())) {
			getUsersInOldAndNewExecutingDepartment();
		}
		// Estimate Prepared by drop-down will show the logged in user Name
		if(abstractEstimate!=null && abstractEstimate.getId()==null && abstractEstimate.getExecutingDepartment()==null && abstractEstimate.getEstimatePreparedBy()==null) {
			PersonalInformation LoggedInEmp = employeeService.getEmpForUserId(worksService.getCurrentLoggedInUserId());
			Assignment assignment= employeeService.getAssignmentByEmpAndDate(new Date(), LoggedInEmp.getIdPersonalInformation());
			abstractEstimate.setExecutingDepartment(assignment.getDeptId());
			abstractEstimate.setEstimatePreparedBy(LoggedInEmp);
			estimatePreparedByView = (EmployeeView) getPersistenceService().find("from EmployeeView where id = ?", abstractEstimate.getEstimatePreparedBy().getIdPersonalInformation());
			if(assignment != null && assignment.getEmployee() != null)
				loggedInUserEmployeeCode = assignment.getEmployee().getCode();
			populatePreparedByList(ajaxEstimateAction, abstractEstimate.getExecutingDepartment() != null);
		}
	}

	protected Integer getIdPersonalInformationFromParams() {
		String[] ids = parameters.get("estimatePreparedBy");
		if (ids != null && ids.length > 0) {
			parameters.remove("estimatePreparedBy");
			String id = ids[0];
			if (id != null && id.length() > 0) {
				return Integer.parseInt(id);
			}
		}
		return null;
	}

	public String moveEstimate() {
		String actionName = parameters.get("actionName")[0];
		if (actionName != null
				&& !(actionName.equals("reject") || actionName.equals("save") || actionName
						.equals("submit_for_approval"))) {
			validateNonSorActivities();
		}
		if(!actionName.equals("reject"))
			validateDeptForDepositWorks();
		
		if(!(CANCEL_ACTION.equals(actionName) && abstractEstimate.getId()==null)) {
			if(abstractEstimate.getEgwStatus()!=null && !((abstractEstimate.getEgwStatus().getCode().equals("REJECTED") && (actionName.equals("submit_for_approval") || actionName.equals("save")))|| abstractEstimate.getEgwStatus().getCode().equals("NEW"))) {
				//If Estimate is in work flow other than Rejected or in Drafts case then do nothing(do not delete child tables and insert again)
			}
			else
				saveEstimate(actionName);
		}

		if(actionName.equals("submit_for_approval")) {
			validateForAssetSelection();
			validateForLatLongSelection();
		}
		
		try{
			abstractEstimateService.setEstimateNumber(abstractEstimate);
			abstractEstimate=abstractEstimateService.persist(abstractEstimate);
		}
		catch (ValidationException sequenceException) {
			List<ValidationError> errorList=sequenceException.getErrors();
			for(ValidationError error:errorList){
			  if(error.getMessage().contains("DatabaseSequenceFirstTimeException")){
				  prepare();
				  abstractEstimate.getActivities().clear();
				  abstractEstimate.getOverheadValues().clear();
				  abstractEstimate.getAssetValues().clear();
				  abstractEstimate.getMultiYearEstimates().clear();
					
				  populateSorActivities();
				  populateNonSorActivities();
				  populateActivities();
				  populateOverheads();
				  populateAssets();
				  populateMultiYearEstimates();

				  throw new ValidationException(Arrays.asList(new ValidationError("error",error.getMessage())));
			  }
		    }
	     }


		
		abstractEstimate = (AbstractEstimate) workflowService.transition(actionName, abstractEstimate,approverComments);
		if(abstractEstimate.getType()!=null && abstractEstimate.getType().getExpenditureType()!=null 
				&& abstractEstimate.getEgwStatus()!=null 
				&& abstractEstimate.getEgwStatus().getCode().equals(AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString())) {		
			try{
				abstractEstimateService.setProjectCode(abstractEstimate);
				abstractEstimate.setApprovedDate(new Date());
			}
			catch (ValidationException sequenceException) {
				setSourcepage("inbox");
				List<ValidationError> errorList=sequenceException.getErrors();
				for(ValidationError error:errorList){
				  if(error.getMessage().contains("DatabaseSequenceFirstTimeException")){
					  prepare();
					  throw new ValidationException(Arrays.asList(new ValidationError("error",error.getMessage())));
				  }
			    }
		     }
		}
		abstractEstimate=abstractEstimateService.persist(abstractEstimate);	
		
		messageKey="estimate."+actionName; 
		addActionMessage(getText(messageKey,"The estimate was saved successfully"));
		
		getDesignation(abstractEstimate);	
		if(SAVE_ACTION.equals(actionName)){
			sourcepage="inbox";
		}
		return SAVE_ACTION.equals(actionName)?EDIT:SUCCESS;		
	}
	
	private void validateForAssetSelection() {
		if(abstractEstimate.getType()!=null && !(abstractEstimate.getType().getName().equals(WorksConstants.DEPOSIT_WORKS_THIRDPARTY_ASSET)
				|| abstractEstimate.getType().getName().equals(WorksConstants.DEPOSIT_WORKS_NO_ASSET_CREATED))) {
			String isAssetRequired = worksService.getWorksConfigValue("ASSET_MANDATORY");
			if(isAssetRequired.equals("yes") && actionAssetValues!=null) {
				boolean isAssetPresent = false;
				for(AssetsForEstimate assetValue: actionAssetValues) {
					if(assetValue != null && assetValue.getAsset()!=null && assetValue.getAsset().getId()!=null) { 
						isAssetPresent = true;
						break;
					}
				}
				if(!isAssetPresent) {
					throw new ValidationException(Arrays.asList(new ValidationError("estimate.asset.required", "estimate.asset.required")));
				}					
			}
		}
	}
	
	private void validateForLatLongSelection()
	{
		if(abstractEstimate.getLat()==null || abstractEstimate.getLon()==null || StringUtils.isBlank(abstractEstimate.getLocation()))
		{
			String cutOffDateStr = worksService.getWorksConfigValue("LAT_LON_CUT_OFF_DATE");
			if(StringUtils.isNotBlank(cutOffDateStr))
			{
				try {
					Date cutOffDate = formatter.parse(cutOffDateStr);
					Date createdDate = (abstractEstimate.getCreatedDate()==null?new Date():abstractEstimate.getCreatedDate().toDate() );
					if(createdDate.after(cutOffDate))
					{
						throw new ValidationException(Arrays.asList(new ValidationError("estimate.latlon.required", "estimate.latlon.required")));
					}
				} catch (ParseException e) {
					logger.error("Unable to parse estimate lat, lon estimate cut off date");
				}
			}
		}
	}
	
	public String maps()
	{
		return MAPS;
	}
	
	private void validateNonSorActivities() {
		Set<String> exceptionSor = worksService.getExceptionSOR().keySet();
		for (Activity activity : nonSorActivities) {
			if (activity != null && activity.getNonSor().getUom() != null) {
				EgUom nonSorUom = (EgUom) getPersistenceService().find("from EgUom where id = ?",
						activity.getNonSor().getUom().getId());
				if (nonSorUom != null && exceptionSor.contains(nonSorUom.getUom())) {
					setSourcepage("inbox");
					throw new ValidationException(Arrays.asList(new ValidationError("validate.nonSor.uom",
							"validate.nonSor.uom")));
				}
			}
		}
	}

	public void getDesignation(AbstractEstimate abstractEstimate){
		/* start for customizing workflow message display */
		if(abstractEstimate.getEgwStatus()!=null 
				&& !"NEW".equalsIgnoreCase(abstractEstimate.getEgwStatus().getCode())) {
			String result = worksService.getEmpNameDesignation(abstractEstimate.getState().getOwnerPosition(), abstractEstimate.getState().getCreatedDate().toDate());
			if(result != null && !"@".equalsIgnoreCase(result)) {
				String empName = result.substring(0,result.lastIndexOf('@'));
				String designation =result.substring(result.lastIndexOf('@')+1,result.length());
				setEmployeeName(empName);
				setDesignation(designation);
			}
		}
		/* end */	
	}
	
	public String cancel(){
		if(abstractEstimate.getId()!=null){
			workflowService.transition(AbstractEstimate.Actions.CANCEL.toString(), abstractEstimate,approverComments);
			String oldEstimateNo = abstractEstimate.getEstimateNumber();
			abstractEstimate.setEstimateNumber(oldEstimateNo.concat("/C"));
			getBudgetUsageListForEstimateNumber(oldEstimateNo);
			abstractEstimate=abstractEstimateService.persist(abstractEstimate);
		}
		messageKey="estimate.cancel";	 
		getDesignation(abstractEstimate);
		return SUCCESS;
	}	
	
	public String reject(){
		workflowService.transition(AbstractEstimate.Actions.REJECT.toString(),abstractEstimate,approverComments);
		abstractEstimate=abstractEstimateService.persist(abstractEstimate);
		messageKey="estimate.reject";	
		getDesignation(abstractEstimate);
		return SUCCESS;
	}	
	
	
	public String downloadTemplate() {
		return "template";
	}
	
	protected void populateCategoryList(
			AjaxEstimateAction ajaxEstimateAction, boolean categoryPopulated) {
		if (categoryPopulated) {
			ajaxEstimateAction.setCategory(abstractEstimate.getParentCategory().getId());
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
					abstractEstimate.getExecutingDepartment().getId());
			if(id==null || (abstractEstimate.getEgwStatus()!=null 
					&& (abstractEstimate.getEgwStatus().getCode().equals(AbstractEstimate.EstimateStatus.REJECTED.toString()) 
							|| abstractEstimate.getEgwStatus().getCode().equals("NEW")))) {
				if(StringUtils.isNotBlank(loggedInUserEmployeeCode) && 
						(estimatePreparedByView==null || loggedInUserEmployeeCode.equalsIgnoreCase(estimatePreparedByView.getEmployeeCode()))) {
					//Extra condition is added since estimate can be rejected in 2 usecases
					ajaxEstimateAction.setEmployeeCode(loggedInUserEmployeeCode);
				}
			}
			
			ajaxEstimateAction.usersInExecutingDepartment();		
			addDropdownData(
					"preparedByList", ajaxEstimateAction.getUsersInExecutingDepartment());
		}
		else {
			addDropdownData("preparedByList", Collections.emptyList());
		}
	}
	
	protected void populateOverheadsList(AjaxEstimateAction ajaxEstimateAction,	boolean estimateDatePresent) {
		if (estimateDatePresent) {
			ajaxEstimateAction.setEstDate(abstractEstimate.getEstimateDate());
			ajaxEstimateAction.overheads();
			addDropdownData("overheadsList", ajaxEstimateAction.getOverheads());
		}
		else {
			ajaxEstimateAction.setEstDate(new Date());
			ajaxEstimateAction.overheads();
			addDropdownData("overheadsList", ajaxEstimateAction.getOverheads());
		}
	}

	public String execute()
	{
	     return SUCCESS;
	}
	 
	 public String newform(){
		return NEW;
	 }
	 
	 
	private void saveEstimate(String actionName) {
		abstractEstimate.getActivities().clear();
		abstractEstimate.getOverheadValues().clear();
		abstractEstimate.getAssetValues().clear();
		abstractEstimate.getMultiYearEstimates().clear();
		
		populateSorActivities();
		populateNonSorActivities();
		populateActivities();
		populateOverheads();
		populateAssets();
		populateMultiYearEstimates();
		
		AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
		ajaxEstimateAction.setPersistenceService(getPersistenceService());
		populateOverheadsList(ajaxEstimateAction, abstractEstimate.getEstimateDate()!=null);
		
		if(!(SAVE_ACTION.equals(actionName)||CANCEL_ACTION.equals(actionName)||REJECT_ACTION.equals(actionName)) && abstractEstimate.getWorkValue().getValue()<=0.0){
			errorCode = "estimate.workvalue.null";
			throw new ValidationException(Arrays.asList(new ValidationError("estimate.workvalue.null","estimate.workvalue.null")));
			
		}	
		if(SAVE_ACTION.equals(actionName) && abstractEstimate.getEgwStatus()==null) {
			abstractEstimate.setEgwStatus(commonsService.getStatusByModuleAndCode("AbstractEstimate","NEW"));
		}
		abstractEstimate = abstractEstimateService.persist(abstractEstimate);   
	}
	 
	 protected void setEstimatePreparedBy(Integer idPersonalInformation) {
		 
		 if (validEstimatePreparedBy(idPersonalInformation)) {
			 abstractEstimate.setEstimatePreparedBy(employeeService.getEmloyeeById(idPersonalInformation));
			 estimatePreparedByView = (EmployeeView) getPersistenceService().find("from EmployeeView where id = ?", idPersonalInformation);
		 }
		 else{
			 abstractEstimate.setEstimatePreparedBy(null);
			 estimatePreparedByView = null;
		 }
	 }
	 
	 protected boolean validEstimatePreparedBy(Integer idPersonalInformation) {
		 if (idPersonalInformation != null && idPersonalInformation > 0) {
			 return true;
		 }
		 
		 return false;
	 }
	 
	 protected void populateSorActivities() {
		 for(Activity activity: sorActivities) {
			 if (validSorActivity(activity)) {
				 activity.setSchedule((ScheduleOfRate) getPersistenceService().find("from ScheduleOfRate where id = ?", activity.getSchedule().getId()));
				 activity.setUom(activity.getSchedule().getUom());
				 abstractEstimate.addActivity(activity);
			 }
		 }
	 }
	 
	 protected boolean validSorActivity(Activity activity) {
		 if (activity != null && activity.getSchedule() != null && activity.getSchedule().getId() != null) {
			 return true;
		 }
		 
		 return false;
	 }
	 
	 protected void populateNonSorActivities() {
		 for (Activity activity: nonSorActivities) { 
			 if (activity!=null) {
				 activity.setUom(activity.getNonSor().getUom());
				 abstractEstimate.addActivity(activity);
			 }
		 }
	 }
	 
	
	 private void populateActivities() {
		 for(Activity activity: abstractEstimate.getActivities()) {
			 activity.setAbstractEstimate(abstractEstimate);
		 }
	 }
	 
	 protected void populateOverheads() {
		 for(OverheadValue overheadValue: actionOverheadValues) {
			 if (validOverhead(overheadValue)) {
				 overheadValue.setOverhead((Overhead) getPersistenceService().find("from Overhead where id = ?", overheadValue.getOverhead().getId()));
				 overheadValue.setAbstractEstimate(abstractEstimate);
				 abstractEstimate.addOverheadValue(overheadValue);
			 }
		 }
	 }
	 
	 protected boolean validOverhead(OverheadValue overheadValue) {
		 if (overheadValue != null && overheadValue.getOverhead() != null && overheadValue.getOverhead().getId() != null && overheadValue.getOverhead().getId()!=-1 && overheadValue.getOverhead().getId()!=0) {
			 return true;
		 }
		 return false;
	 }

	 protected void populateAssets() {
		 List<ValidationError> valErrList = new LinkedList<ValidationError>();
		 List<String> strStatus = getStatusList();
		 Set<String> validAssetCodes = new HashSet<String>(); 
		 for(AssetsForEstimate assetValue: actionAssetValues) {
			if (validAsset(assetValue)) {
				 Asset lAsset = (Asset) getPersistenceService().find("from Asset where code = ?", assetValue.getAsset().getCode());
				 if(lAsset==null){
					 String message = "Asset code \'" + assetValue.getAsset().getCode() + "\' does not exist. Please create the asset before link.";  
					 valErrList.add(new ValidationError(message,message));
				 }
				 else{
					 if(!checkValidStatus(lAsset,strStatus)){
						 String message = "Asset code \'" + assetValue.getAsset().getCode() + "\' can't be link for selected nature of work.";  
						 valErrList.add(new ValidationError(message,message));
					 }
					 if(validAssetCodes.contains(lAsset.getCode())){
						 String message = "Please remove the duplicate entry for Asset code \'" + lAsset.getCode() + "\'";  
						 valErrList.add(new ValidationError(message,message));
					 }
					 else{
						 validAssetCodes.add(lAsset.getCode());
					 }
					 assetValue.setAsset(lAsset);
					 assetValue.setAbstractEstimate(abstractEstimate);
					 abstractEstimate.addAssetValue(assetValue);
				 }
			 }
		 }
		 if(!valErrList.isEmpty())
			throw new ValidationException(valErrList);
		 
		 
	 }
	 
	private List<String> getStatusList(){
		List<String> strStatus = null;
		if(assetStatus==null)
			strStatus = new ArrayList<String>();
		else 
			strStatus = Arrays.asList(assetStatus.split(","));
		
		return strStatus;
	 }
	
	private boolean checkValidStatus(Asset ass, List<String> strStatus){
		for(String desc : strStatus)
			if(desc.trim().equalsIgnoreCase(ass.getStatus().getDescription()))
				return true;
		return false;
	}
	 
	 protected boolean validAsset(AssetsForEstimate assetValue) {
		 if (assetValue != null && assetValue.getAsset() != null && assetValue.getAsset().getCode() != null && !(assetValue.getAsset().getCode().isEmpty())) {
			 return true;
		 }
		 return false;
	 }
	 
	 protected void populateMultiYearEstimates() {
		 int count=1;
		 double totalPerc=0.0;
		 for(MultiYearEstimate multiYearEstimate: actionMultiYearEstimateValues) {
			 if (validMultiYearEstimate(multiYearEstimate)) {
				 multiYearEstimate.setFinancialYear((CFinancialYear) getPersistenceService().find("from CFinancialYear where id = ?", multiYearEstimate.getFinancialYear().getId()));
				 multiYearEstimate.setAbstractEstimate(abstractEstimate);
				 totalPerc=totalPerc+multiYearEstimate.getPercentage();				
				 abstractEstimate.addMultiYearEstimate(multiYearEstimate);
			 }
			  if(multiYearEstimate!=null && (actionMultiYearEstimateValues.size())==count && (totalPerc!=0.0 && totalPerc<100)){
					throw new ValidationException(Arrays.asList(new ValidationError("percentage","multiYearEstimate.percentage.percentage_equals_100")));
			 }
			  
			  if((multiYearEstimate!=null && multiYearEstimate.getFinancialYear()==null) || (multiYearEstimate!=null && multiYearEstimate.getFinancialYear()!=null && multiYearEstimate.getFinancialYear().getId()!=null && multiYearEstimate.getFinancialYear().getId()==0)) {				  
				  throw new ValidationException(Arrays.asList(new ValidationError("financialYear","multiYeareEstimate.financialYear.null")));		
			}			 
			 count++;
		 }
	 }
 
	public void getUsersInOldAndNewExecutingDepartment() {
		try {
			HashMap<String,Object> criteriaParams =new HashMap<String,Object>();
			criteriaParams.put("departmentId", abstractEstimate.getExecutingDepartment().getId());
			//criteriaParams.put("isPrimary", "Y");
			usersInOldAndNewExecutingDepartment=personalInformationService.getListOfEmployeeViewBasedOnCriteria(criteriaParams, -1, -1);
			if(abstractEstimate.getOldExecDepartment()!=null) {
				criteriaParams.clear();
				criteriaParams.put("departmentId", abstractEstimate.getOldExecDepartment().getId());
				//criteriaParams.put("isPrimary", "Y");
				usersInOldAndNewExecutingDepartment.addAll(personalInformationService.getListOfEmployeeViewBasedOnCriteria(criteriaParams, -1, -1));
			}
			if(usersInOldAndNewExecutingDepartment==null || usersInOldAndNewExecutingDepartment.size()==0) {
				usersInOldAndNewExecutingDepartment=Collections.EMPTY_LIST;
			}
		} catch (Exception e) {
			throw new EGOVRuntimeException("user.find.error", e);
		}
		addDropdownData("preparedByList", usersInOldAndNewExecutingDepartment);
	}

	 protected boolean validMultiYearEstimate(MultiYearEstimate multiYearEstimate) {
		 if (multiYearEstimate!= null && multiYearEstimate.getFinancialYear() != null && multiYearEstimate.getFinancialYear().getId() != null && multiYearEstimate.getFinancialYear().getId()!=0 &&
				 multiYearEstimate.getPercentage() >= 0.0) {
			 return true;
		 }
		 
		 return false;
	 }
	 
	 public String cancelApprovedEstimate() {  
		String cancellationText = null; 
		abstractEstimate = abstractEstimateService.findById(estimateId, false);
		
		//Check whether any Rate contract WO exists for the give estimate
		String woNumber = (String) getPersistenceService().find("select woe.workOrder.workOrderNumber from WorkOrderEstimate woe where woe.workOrder.rateContract is not null " +
				"and woe.estimate.rateContract is not null and  woe.estimate.id=? and woe.estimate.egwStatus.code=? and woe.workOrder.egwStatus.code<>?"
				,estimateId, AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString(),WorksConstants.CANCELLED_STATUS.toString());
		if(woNumber != null && !woNumber.equals("")) {
			messageKey = getText("cancelEstimate.rc.wo.created.message.part1").concat(woNumber).concat(" ").concat(getText("cancelEstimate.rc.wo.created.message.part2"));
		} 
		else {
			String oldEstimateNo = abstractEstimate.getEstimateNumber();
	
			PersonalInformation prsnlInfo=employeeService.getEmpForUserId(worksService.getCurrentLoggedInUserId());			
			String empName="";
			if(prsnlInfo.getEmployeeFirstName()!=null)
				empName=prsnlInfo.getEmployeeFirstName();
			if(prsnlInfo.getEmployeeLastName()!=null)
				empName=empName.concat(" ").concat(prsnlInfo.getEmployeeLastName()); 			
	
			if(cancelRemarks != null  && StringUtils.isNotBlank(cancelRemarks))
				cancellationText = cancellationReason+" : "+cancelRemarks+". "+getText("estimate.cancel.cancelledby")+": "+empName;
			else
				cancellationText = cancellationReason+". "+getText("estimate.cancel.cancelledby")+": "+empName;
			
			//Generate Budget Rejection no here 
			abstractEstimate.setBudgetRejectionNo("BC/"+abstractEstimate.getBudgetApprNo()); 
			
			//suffix /C for estimate number
			String newEstNo = abstractEstimate.getEstimateNumber()+"/C"; 
			abstractEstimate.setEstimateNumber(newEstNo);
			
			//If type is deposit works then release Deposit works amount 
			if(isSkipBudgetCheck()) {
				abstractEstimateService.releaseDepositWorksAmountOnReject(abstractEstimate.getFinancialDetails().get(0));
			}
			else {
				//If it is Budget work then release latest budget consumed
				abstractEstimateService.releaseBudgetOnReject(abstractEstimate.getFinancialDetails().get(0));	      
			    abstractEstimate.setBudgetAvailable(abstractEstimateService.getBudgetAvailable(abstractEstimate,abstractEstimateService.getLatestApprYearEndDate(abstractEstimate.getFinancialDetails().get(0))));  
			}
			//Make corresponding project code as inactive
			abstractEstimate.getProjectCode().setIsActive(false);
			
			//TODO - The setter methods of variables in State.java are protected. Need to alternative way to solve this issue.
			//Set the status and workflow state to cancelled
			/*****State oldEndState = abstractEstimate.getCurrentState();
			Position owner = prsnlInfo.getAssignment(new Date()).getPosition();
			oldEndState.setCreatedBy(prsnlInfo.getUserMaster());
			oldEndState.setModifiedBy(prsnlInfo.getUserMaster());
			oldEndState.setCreatedDate(new Date());
			oldEndState.setModifiedDate(new Date());
			oldEndState.setOwner(owner);
			oldEndState.setValue(AbstractEstimate.EstimateStatus.CANCELLED.toString());
			oldEndState.setText1(cancellationText);
			
			abstractEstimate.changeState("END", owner, null); *******/
			
			abstractEstimate.setEgwStatus(commonsService.getStatusByModuleAndCode("AbstractEstimate",AbstractEstimate.EstimateStatus.CANCELLED.toString()));
			getBudgetUsageListForEstimateNumber(oldEstimateNo);
			
			messageKey="estimate.cancel";
		}
		
		return SUCCESS;
	}
	
	private void getBudgetUsageListForEstimateNumber(String estimateNo){
		List<BudgetUsage> budgetUsageList = abstractEstimateService.getBudgetUsageListForEstNo(estimateNo);
		if(budgetUsageList!=null && !budgetUsageList.isEmpty()){
			for(BudgetUsage bu : budgetUsageList){
				bu.setReferenceNumber(abstractEstimate.getEstimateNumber());
			}
		}
	}
	 
	private void validateDeptForDepositWorks() {
		if(isDepositWorksType())
		{
			if(abstractEstimate.getUserDepartment()!=null 
					&& abstractEstimate.getUserDepartment().getId()!=null
					&& abstractEstimate.getExecutingDepartment()!=null
					&& abstractEstimate.getExecutingDepartment().getId()!=null
					&& abstractEstimate.getExecutingDepartment().getId()!=abstractEstimate.getUserDepartment().getId())
			{
				abstractEstimate.getActivities().clear();
				abstractEstimate.getAssetValues().clear();
				abstractEstimate.getOverheadValues().clear();
				abstractEstimate.getMultiYearEstimates().clear();
				populateSorActivities();
				populateNonSorActivities();
				populateActivities();
				populateOverheads();
				populateAssets();
				populateMultiYearEstimates();
				errorCode = "estimate.depositworks.dept.check";
				throw new ValidationException(Arrays.asList(new ValidationError("estimate.depositworks.dept.check","estimate.depositworks.dept.check")));
			}
		}
	} 
	
	private Boolean isDepositWorksType()
	{
		boolean isDepositWorks=false;
		List<String> depositTypeList=getAppConfigValuesToSkipBudget();			
		logger.info("lenght of appconfig values>>>>>> "+depositTypeList.size());
		for(String type:depositTypeList){
			if(type.equals(abstractEstimate.getType().getName())){
				isDepositWorks=true;
			}
		}
		return isDepositWorks;
	}
	
	private Boolean isSkipBudgetCheck(){
		boolean skipBudget=false;
		if(abstractEstimate!=null && abstractEstimate.getId()!=null){
			skipBudget=isDepositWorksType();
		}	
		return skipBudget;
	}
 	
	public AbstractEstimateService getAbstractEstimateService() {
		return abstractEstimateService;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public List<Activity> getSorActivities() {
		return sorActivities;
	}

	public void setSorActivities(List<Activity> sorActivities) {
		this.sorActivities = sorActivities;
	}

	public List<Activity> getNonSorActivities() {
		return nonSorActivities;
	}

	public void setNonSorActivities(List<Activity> nonSorActivities) {
		this.nonSorActivities = nonSorActivities;
	}

	public List<OverheadValue> getActionOverheadValues() {
		return actionOverheadValues;
	}

	public void setActionOverheadValues(List<OverheadValue> actionOverheadValues) {
		this.actionOverheadValues = actionOverheadValues;
	}
	public List<AssetsForEstimate> getActionAssetValues() {
		return actionAssetValues;
	}

	public void setActionAssetValues(List<AssetsForEstimate> actionAssetValues) {
		this.actionAssetValues = actionAssetValues;
	}

	public String getCurrentFinancialYearId() {
		return currentFinancialYearId;
	}

	public void setCurrentFinancialYearId(String currentFinancialYearId) {
		this.currentFinancialYearId = currentFinancialYearId;
	}
	 
	protected CFinancialYear getCurrentFinancialYear() {
		if(abstractEstimate.getEstimateDate()!=null){
			return commonsService.getFinYearByDate(abstractEstimate.getEstimateDate());
		}
		else{
			return commonsService.getFinYearByDate(new Date());
		}
	} 

	public EmployeeService getemployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public EmployeeView getEstimatePreparedByView() {
		return estimatePreparedByView;
	}

	public void setEstimatePreparedByView(EmployeeView estimatePreparedByView) {
		this.estimatePreparedByView = estimatePreparedByView;
	}

	public List<MultiYearEstimate> getActionMultiYearEstimateValues() {
		return actionMultiYearEstimateValues;
	}

	public void setActionMultiYearEstimateValues(
			List<MultiYearEstimate> actionMultiYearEstimateValues) {
		this.actionMultiYearEstimateValues = actionMultiYearEstimateValues;
	}
	
	public List<Action> getValidActions(){
		return workflowService.getValidActions(abstractEstimate);
	}

	public void setEstimateWorkflowService(WorkflowService<AbstractEstimate> workflow) {
		this.workflowService = workflow;
	}

	public String getSourcepage() {
		return sourcepage;
	}

	public void setSourcepage(String sourcepage) {
		this.sourcepage = sourcepage;
	}

	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}

	/**
	 * @param employeeName the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	/**
	 * @return the designation
	 */
	public String getDesignation() {
		return designation;
	}

	/**
	 * @param designation the designation to set
	 */
	public void setDesignation(String designation) {
		this.designation = designation;
	}

	/**
	 * @return the worksService
	 */
	public WorksService getWorksService() {
		return worksService;
	}

	/**
	 * @param worksService the worksService to set
	 */
	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public String getAssetStatus() {
		return assetStatus;
	}

	public void setAssetStatus(String assetStatus) {
		this.assetStatus = assetStatus;
	}

	public CommonsService getCommonsService() {
		return commonsService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public Date getFinancialYearStartDate() {
		financialYearStartDate = commonsService.getFinancialYearByFinYearRange(worksService.getWorksConfigValue("FINANCIAL_YEAR_RANGE")).getStartingDate();
		return financialYearStartDate;
	}

	public void setFinancialYearStartDate(Date financialYearStartDate) {
		this.financialYearStartDate = financialYearStartDate;
	}

	public Integer getApproverUserId() {
		return approverUserId;
	}

	public void setApproverUserId(Integer approverUserId) {
		this.approverUserId = approverUserId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	public String getApproverComments() {
		return approverComments;
	}

	public void setApproverComments(String approverComments) {
		this.approverComments = approverComments;
	}

	public Long getStateValue() {
		return stateValue;
	}

	public void setStateValue(Long stateValue) {
		this.stateValue = stateValue;
	}
	
	public List<String> getAppConfigValuesToSkipBudget(){
		return worksService.getNatureOfWorkAppConfigValues(MODULE_NAME, KEY_NAME);
	}

	public String getEstimateValue() {
		return estimateValue;
	}

	public void setEstimateValue(String estimateValue) {
		this.estimateValue = estimateValue;
	}
	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	
	public InputStream getXlsInputStream() {
		return xlsInputStream;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Long getEstimateId() {
		return estimateId;
	}

	public void setEstimateId(Long estimateId) {
		this.estimateId = estimateId;
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

	public String getErrorCode() {
		return errorCode;
	}

	public boolean getIsAllowEstDateModify() {
		return isAllowEstDateModify;
	}

	public void setIsAllowEstDateModify(boolean isAllowEstDateModify) {
		this.isAllowEstDateModify = isAllowEstDateModify;
	}

	public String getMapMode() {
		return mapMode;
	}

	public void setMapMode(String mapMode) {
		this.mapMode = mapMode;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

	public String getLoggedInUserEmployeeCode() {
		return loggedInUserEmployeeCode;
	}

	public void setLoggedInUserEmployeeCode(String loggedInUserEmployeeCode) {
		this.loggedInUserEmployeeCode = loggedInUserEmployeeCode;
	}


	public double getUtilizedAmount() {
		return utilizedAmount;
	}

	public void setUtilizedAmount(double utilizedAmount) {
		this.utilizedAmount = utilizedAmount;
	}

	public boolean getDigitalSign() {
		return digitalSign;
	}

	public void setDigitalSign(boolean digitalSign) {
		this.digitalSign = digitalSign;
	}

	public EgovCommon getEgovCommon() {
		return egovCommon;
	}

	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}

	public BigDecimal getPaymentReleased() {
		return paymentReleased;
	}

	public void setPaymentReleased(BigDecimal paymentReleased) {
		this.paymentReleased = paymentReleased;
	}

	public ContractorBillService getContractorBillService() {
		return contractorBillService;
	}

	public void setContractorBillService(ContractorBillService contractorBillService) {
		this.contractorBillService = contractorBillService;
	}

	public List<Object> getWoDetails() {
		return woDetails;
	}

	public void setWoDetails(List<Object> woDetails) {
		this.woDetails = woDetails;
	}

	public List<Object> getWpDetails() {
		return wpDetails;
	}

	public void setWpDetails(List<Object> wpDetails) {
		this.wpDetails = wpDetails;
	}

}
