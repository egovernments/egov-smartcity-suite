package org.egov.works.web.actions.estimate;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.assets.model.Asset;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.service.CommonsService;
import org.egov.dao.budget.BudgetGroupDAO;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.auditing.model.AuditEvent;
import org.egov.infstr.auditing.model.AuditModule;
import org.egov.infstr.auditing.service.AuditEventService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.EgChecklists;
import org.egov.infstr.models.State;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.budget.BudgetGroup;
import org.egov.pims.commons.Position;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.utils.Constants;
import org.egov.web.actions.workflow.GenericWorkFlowAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.Activity;
import org.egov.works.models.estimate.AssetsForEstimate;
import org.egov.works.models.estimate.EstimateRateContract;
import org.egov.works.models.estimate.EstimateRateContractDetail;
import org.egov.works.models.estimate.FinancialDetail;
import org.egov.works.models.estimate.FinancingSource;
import org.egov.works.models.estimate.MeasurementSheet;
import org.egov.works.models.estimate.MultiYearEstimate;
import org.egov.works.models.estimate.NonSor;
import org.egov.works.models.estimate.OverheadValue;
import org.egov.works.models.estimate.TechnicalSanction;
import org.egov.works.models.estimate.WorkType;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.masters.DepositCode;
import org.egov.works.models.masters.Overhead;
import org.egov.works.models.masters.ScheduleOfRate;
import org.egov.works.models.rateContract.RateContract;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.DateConversionUtil;

@Results({
@Result(name=AbstractEstimateAction.PRINT,type="stream",location="PdfInputStream", params={"inputName","PdfInputStream","contentType","application/pdf","contentDisposition","no-cache"}),
@Result(name=AbstractEstimateAction.PRINTDRAFTRESOLUTION,type="stream",location="inputStream", params={"inputName","inputStream","contentType","application/msexcel","contentDisposition","no-cache"})
})
public class AbstractEstimateAction extends GenericWorkFlowAction{	
	private static final Logger logger = Logger.getLogger(AbstractEstimateAction.class);
	private static final String CANCEL_ACTION = "cancel";
	private static final String SAVE_ACTION = "save";
	private static final Object REJECT_ACTION = "reject";
	private static final String STATE_ADMIN_SANCTIONED = "ADMIN_SANCTIONED";
	private static final String ADMIN_CHECKED = "ADMIN_CHECKED";
	private static final String SOURCE_SEARCH = "search"; 
	private static final String SOURCE_INBOX = "inbox";
	private static final String MODULE_NAME = "Works";
	private static final String KEY_NAME = "SKIP_BUDGET_CHECK";
	private static final String WORKS="Works";
	private static final String PUBLIC_WORKS_DEPARTMENT="Public Work";
	
	private AbstractEstimate abstractEstimate = new AbstractEstimate();
	private List<Activity> sorActivities = new LinkedList<Activity>();
	private List<Activity> nonSorActivities = new LinkedList<Activity>();
	private List<OverheadValue> actionOverheadValues = new LinkedList<OverheadValue>();
	private List<AssetsForEstimate> actionAssetValues = new LinkedList<AssetsForEstimate>();
	private List<MultiYearEstimate> actionMultiYearEstimateValues = new LinkedList<MultiYearEstimate>();
	private AbstractEstimateService abstractEstimateService;
	private PersistenceService<TechnicalSanction, Long> technicalSanctionService=new PersistenceService<TechnicalSanction, Long>();
	public void setTechnicalSanctionService(
			PersistenceService<TechnicalSanction, Long> technicalSanctionService) {
		this.technicalSanctionService = technicalSanctionService;
	}

	private EmployeeService employeeService;
	private EmployeeView estimatePreparedByView;
	private WorkflowService<AbstractEstimate> workflowService;
	private String messageKey;
	private String sourcepage="";
	private String assetStatus;  
	private Integer approverUserId;
   	private CommonsService commonsService;
	private Date financialYearStartDate;
	private Integer departmentId;  
	private Integer designationId; 
	private String approverComments;
	private Long stateValue;
	private String estimateValue;
	private Long sorId;
	private String nonsorId="";
	private String recordId;
	private String sorDesc="";
	public List<MeasurementSheet> measurementSheetList = new LinkedList<MeasurementSheet>();
	private Double totalMSheetQty;
	private static final String MSHEET = "measurementSheet";
	private String estimateUOM;
	private String estimateLineItem;
	private String nonsorDesc;
	private CFinancialYear financialYear;
	
	/* added by prashanth on 2nd nov 09 for disp user and desgination in success page*/
	String employeeName;
	String designation;
	private WorksService worksService; 
	private PersonalInformationService personalInformationService;
	private InputStream pdfInputStream;
	private InputStream inputStream;
	private ReportService reportService;
	public static final String PRINT = "print"; 
	public static final String PRINTDRAFTRESOLUTION = "printDraftResolution";
	public static final String BOQ= "Bill Of Qunatities";
	private boolean isSpillOverWorks;
	private boolean isEmergencyWorks;
		
	private static final String COA_LIST="coaList";
	private static final String BUDGET_GROUP_SEARCH_LIST = "budgetHeadList";
	private static final String KEY_DEPOSIT ="WORKS_DEPOSIT_OTHER_WORKS";
	private static final String BUDGET_GROUP_LIST = "budgetGroupList"; 
	
	private PersistenceService<DepositCode,Long> depositCodeService;
	private FinancialDetail financialDetail= new FinancialDetail();
	private List<FinancingSource> financingSourceList = new LinkedList<FinancingSource>();
	private List<Fundsource> fundSourceList;	
	private List<FinancialDetail> actionFinancialDetails = new LinkedList<FinancialDetail>();
	private Long depositCodeId;
	private BudgetGroupDAO budgetGroupDAO;	
	private boolean skipBudget=false;
	private String additionalRuleValue;
	private BigDecimal amountRuleValue;
	private boolean isRateContract;
	private String rcType;
	public List<Contractor> contractorList = new LinkedList<Contractor>();
	private boolean showDraftResolution;
	private List<AppConfigValues>  estimateChecklist= new LinkedList<AppConfigValues>(); 
	private Long[] appConfigValueId;
	private String[]  selectedchecklistValue;
	private PersistenceService<EgChecklists, Long> checklistService;
	private List<String>  checklistValues= new LinkedList<String>(); 
	private String[]  checkListremarks;
	
	private static final String CHECKLIST_VALUE1 = "Details drawings attached with proposal";
	
	private static final String CHECKLIST_VALUE2 = "Export Regarding Transfer,N.O.C etc Taken?";
	
	private static final String CHECKLIST_VALUE3 = "Is this work proposed on NMC Land?";
	
	private static final String CHECKLIST_VALUE4 = "Proposed work is a Part of another work?";
	
	private static final String CHECKLIST_VALUE5 = "Work will be done Stage Wise?";
	
	private static final String CHECKLIST_VALUE6 = "if proposed work is new building, construction provision of Electrification and Water Supply is Made?";
	
	
	private PersistenceService<NonSor,Long> nonSorService;
	
	private String allowFutureDate;
		
	public List<EstimateRateContract> actionEstimateRateContractList = new LinkedList<EstimateRateContract>();
	private static final String DRAFT_RESOLUTION = "draftResolution";
	private boolean showResolutionDetails;
	private boolean hideWorkFlowInfo=false;
	private String cancelRemarks;
	private AuditEventService auditEventService;
	private String preparedByTF = "";

	public boolean isHideWorkFlowInfo() {
		return hideWorkFlowInfo;
	}

	public void setHideWorkFlowInfo(boolean hideWorkFlowInfo) {
		this.hideWorkFlowInfo = hideWorkFlowInfo;
	}

	public String getMessageKey() {
		return messageKey;
	}

	private String currentFinancialYearId;	  
	private Long id;
	private Long estimateId;
	
	public Long getEstimateId() {
		return estimateId;
	}

	public void setEstimateId(Long estimateId) {
		this.estimateId = estimateId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AbstractEstimateAction() {
		addRelatedEntity("fundSource",Fundsource.class);
		addRelatedEntity("userDepartment", DepartmentImpl.class);
		addRelatedEntity("executingDepartment", DepartmentImpl.class);
		addRelatedEntity("ward", BoundaryImpl.class);
		addRelatedEntity("type", WorkType.class);
		addRelatedEntity("category", EgwTypeOfWork.class);
		addRelatedEntity("parentCategory", EgwTypeOfWork.class);
	}

	public String edit(){
		return NEW;
	}
	
	public String cancelApprovedEstimate() {  
		abstractEstimate = abstractEstimateService.findById(estimateId, false);
		
		PersonalInformation prsnlInfo=employeeService.getEmpForUserId(Integer.valueOf(getLoggedInUserId()));			
		String empName="";
		if(prsnlInfo.getEmployeeFirstName()!=null)
			empName=prsnlInfo.getEmployeeFirstName();
		if(prsnlInfo.getEmployeeLastName()!=null)
			empName=empName.concat(" ").concat(prsnlInfo.getEmployeeLastName()); 			

		abstractEstimate.getCurrentState().getPrevious().setText1(cancelRemarks+". Abstract Estimate Cancelled by: "+empName);
		abstractEstimate.getCurrentState().getPrevious().setValue("CANCELLED");
		abstractEstimate.setEgwStatus(commonsService.getStatusByModuleAndCode("AbstractEstimate","CANCELLED"));

		messageKey="estimate."+abstractEstimate.getEgwStatus().getCode();
		return SUCCESS;
	}

	public int getLoggedInUserId() {
		return Integer.parseInt(EGOVThreadLocals.getUserId());
	}
	public String workflowHistory(){
		return "history";
	}
	
	public StateAware getModel() {
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
		if (id != null && EDIT.equals("edit") && !"msheetPDF".equals(sourcepage) && !"boqPDF".equals(sourcepage) ) {
			 abstractEstimate= abstractEstimateService.findById(id, false); 
			 abstractEstimate = abstractEstimateService.merge(abstractEstimate);
			 if(abstractEstimate!=null && abstractEstimate.getEstimatePreparedBy()!=null){
				 estimatePreparedByView = (EmployeeView) getPersistenceService().find("from EmployeeView where id = ?", abstractEstimate.getEstimatePreparedBy().getIdPersonalInformation());
			 }			 
			 if(!abstractEstimate.getEstimateRateContractList().isEmpty()){
				 isRateContract=true;
			 	 rcType=abstractEstimate.getEstimateRateContractList().get(0).getRateContract().getIndent().getIndentType();
			 }
			 else{
				 isRateContract=false;
		 }
		 }
		super.prepare();
				
		if((SOURCE_INBOX.equals(getSourcepage()) || SOURCE_SEARCH.equals(getSourcepage()) || id!=null) && measurementSheetList.isEmpty()){ 
			List<Activity> activityList =abstractEstimate.getActivities();
			Iterator iterator=activityList.iterator();
			Activity activity;
			int count=1;
            while(iterator.hasNext()){
                    activity=(Activity) iterator.next();
                    if(activity.getNonSor()!=null)
                            activity.setSrlNo(count++);
                    measurementSheetList.addAll(activity.getMeasurementSheetList());
            }
		}
				
		// estimatePreparedBy has to be handled differently (as its id is idPersonalInformation)
		//do not set estimate prepared by if the request is from search/inbox page
		if(!sourcepage.equals("techSanctionPDF")){
			if(!(SOURCE_SEARCH.equals(getSourcepage()) || SOURCE_INBOX.equals(getSourcepage()))){
				setEstimatePreparedBy(getIdPersonalInformationFromParams());
			}
		}
		 
		 CFinancialYear financialYear = getCurrentFinancialYear();
		 if(financialYear!=null) {
			 currentFinancialYearId=financialYear.getId().toString();
		 }
		
		setupDropdownDataExcluding("ward","category","parentCategory","fundSource");
		addDropdownData("parentCategoryList", getPersistenceService().findAllBy("from EgwTypeOfWork etw1 where etw1.parentid is null"));
		addDropdownData("uomList", getPersistenceService().findAllBy("from EgUom  order by upper(uom)"));
		addDropdownData("financialYearList", getPersistenceService().findAllBy("from CFinancialYear where isActive=1"));
		addDropdownData("scheduleCategoryList", getPersistenceService().findAllBy("from ScheduleCategory order by upper(code)")); 
		
		addDropdownData("fundList", commonsService.getAllActiveIsLeafFunds()); 
		addDropdownData("functionList", commonsService.getAllFunction()); 
		addDropdownData("functionaryList", commonsService.getActiveFunctionaries());
		AjaxFinancialDetailAction ajaxFinancialDetailAction = 
			new AjaxFinancialDetailAction();
		ajaxFinancialDetailAction.setPersistenceService(getPersistenceService());
		ajaxFinancialDetailAction.setBudgetGroupDAO(budgetGroupDAO);
		
		if(!abstractEstimate.getFinancialDetails().isEmpty())
			financialDetail=abstractEstimate.getFinancialDetails().get(0);
		abstractEstimateService.setBudgetGroupDAO(budgetGroupDAO);
		try {
			addDropdownData(BUDGET_GROUP_LIST, 
					(List<BudgetGroup>) budgetGroupDAO.getBudgetGroupList());
			addDropdownData(BUDGET_GROUP_SEARCH_LIST,new ArrayList<BudgetGroup>());
		} catch (Exception e1) {
			addFieldError("budgetunavailable", "Unable to load budget information");
		}
		populateSchemeList(ajaxFinancialDetailAction, financialDetail!=null && financialDetail.getFund()!=null,
				financialDetail!=null && financialDetail.getAbstractEstimate()!=null);
		populateSubSchemeList(ajaxFinancialDetailAction, 
				financialDetail!=null && financialDetail.getScheme() != null,
				financialDetail!=null && financialDetail.getAbstractEstimate()!=null);
				
		try {
			populateBudgetGroupList(ajaxFinancialDetailAction, 
					financialDetail!=null && financialDetail.getFunction()!=null, 
					financialDetail!=null && financialDetail.getAbstractEstimate()!=null);
		} catch (EGOVException e) {
			addFieldError("budgetunavailable", "Unable to load budget data");
		} catch (Exception e) {
			addFieldError("budgetunavailable", "Unable to load budget data from database");
		}
		if(getDropdownData().get(BUDGET_GROUP_LIST)==null){
			addDropdownData(BUDGET_GROUP_LIST, new ArrayList<BudgetGroup>());
		}
		
		try {
			fundSourceList = commonsService.getAllActiveIsLeafFundSources();
		} catch (EGOVException e) {
			addFieldError("fundsourceunavailable", "Unable to load fund source information");
		}
		try {			
			if(StringUtils.isNotBlank(worksService.getWorksConfigValue(KEY_DEPOSIT))){
				addDropdownData(COA_LIST, commonsService.getAccountCodeByPurpose(Integer.valueOf(worksService.getWorksConfigValue(KEY_DEPOSIT))));
			}
			else  
				addDropdownData(COA_LIST,Collections.EMPTY_LIST);			
		}
		catch(EGOVException v) {		
			logger.error("Unable to load Account Codes for Deposit Works" + v.getMessage());
			addFieldError("accountcodes.notfound", "abstractEstimate.deposit.accountcodes.error");
		}
		
		populateCategoryList(ajaxEstimateAction, abstractEstimate.getParentCategory() != null);
		populatePreparedByList(ajaxEstimateAction, abstractEstimate.getExecutingDepartment() != null);
		populateOverheadsList(ajaxEstimateAction, abstractEstimate.getEstimateDate()!=null);
		if(abstractEstimateService.getLatestAssignmentForCurrentLoginUser()!=null) {
			departmentId=abstractEstimateService.getLatestAssignmentForCurrentLoginUser().getDeptId().getId();
		}
		
		try {
			addDropdownData("fundSourceList",commonsService.getAllActiveIsLeafFundSources());
		} catch (EGOVException e) {
			addFieldError("fundsourceunavailable", "Unable to load fund source information");
		}
		if(sorId!=null){
			ScheduleOfRate sorObject=(ScheduleOfRate)persistenceService.find("from ScheduleOfRate where id=?", sorId);
			sorDesc=sorObject.getDescription();
		}
		
// For Draft Resolution Report - Generated only if the totalamount is more than 10lakhs
		if(id!=null && ((SOURCE_INBOX.equals(sourcepage) || SOURCE_SEARCH.equals(sourcepage) || "".equals(sourcepage)) && 
		  (abstractEstimate.getCurrentState()!= null && ((ADMIN_CHECKED.equalsIgnoreCase(abstractEstimate.getCurrentState().getValue())) || 
			(abstractEstimate.getCurrentState().getPrevious()!=null && STATE_ADMIN_SANCTIONED.equalsIgnoreCase(abstractEstimate.getCurrentState().getPrevious().getValue())))))){
				List<AppConfigValues> appConfigList = worksService.getAppConfigValue("Works","ESTIMATE_DRAFTRESOLUTION_AMOUNT");
				if(appConfigList!=null && !appConfigList.isEmpty() && appConfigList.get(0).getValue()!=null && 
						abstractEstimate.getTotalAmount().getValue()>Double.parseDouble(appConfigList.get(0).getValue()))
					showDraftResolution=true; 
				else
					showDraftResolution=false;
				
				Map<String,Integer> draftResolutionRules = getDraftResolutionRules();
				double result = 1;
				if(draftResolutionRules.containsKey(abstractEstimate.getExecutingDepartment().getDeptName())){
					result = draftResolutionRules.get(abstractEstimate.getExecutingDepartment().getDeptName());
					if(abstractEstimate.getWorkValueIncludingTaxes().getValue()>result){
						showResolutionDetails=true;
					}
					else
						showResolutionDetails=false;
				}
		}
		
		if(id!=null){
			checklistValues.add("N/A");
			checklistValues.add("Yes");
			checklistValues.add("No");
			String appConfValueId="";
			
			List<AppConfigValues>  acvObj= new LinkedList<AppConfigValues>();
			acvObj=worksService.getAppConfigValue(WORKS,"Estimate-CheckList");
			Integer appConfigId=null;
			if(acvObj!=null && acvObj.size()!=0){
				appConfigId=acvObj.get(0).getKey().getId();
	}

			List<EgChecklists> list = persistenceService.findAllByNamedQuery("checklist.by.appconfigid.and.objectid", id, appConfigId);
			if(!list.isEmpty() && list.size()!=0){
				int i=0;
				selectedchecklistValue =new String[list.size()];
				checkListremarks =new String[list.size()];
				for(EgChecklists egChecklists: list)
				{
					estimateChecklist.add(egChecklists.getAppconfigvalue());
					selectedchecklistValue[i]=egChecklists.getChecklistvalue();
					checkListremarks[i]=egChecklists.getRemarks(); 
					i++;
				}
			}
			else{
				try{
					estimateChecklist= worksService.getAppConfigValue(WORKS,"Estimate-CheckList");
				} catch (Exception e) {
					logger.error("--------------Error in check bill List-------------");
				}
			}
		}
		else{
			checklistValues.add("N/A");
			checklistValues.add("Yes");
			checklistValues.add("No");
				
			try {
				estimateChecklist= worksService.getAppConfigValue(WORKS,"Estimate-CheckList");
			} catch (Exception e) {
				logger.error("--------------Error in check bill List-------------");
	}
		}
		// Estimate Prepared by drop-down will show the logged in user Name
		if(abstractEstimate!=null && abstractEstimate.getId()==null && abstractEstimate.getExecutingDepartment()==null && abstractEstimate.getEstimatePreparedBy()==null) {
			PersonalInformation LoggedInEmp = employeeService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
			Assignment assignment= employeeService.getAssignmentByEmpAndDate(new Date(), LoggedInEmp.getIdPersonalInformation());
			abstractEstimate.setExecutingDepartment(assignment.getDeptId());
			abstractEstimate.setEstimatePreparedBy(LoggedInEmp);
			estimatePreparedByView = (EmployeeView) getPersistenceService().find("from EmployeeView where id = ?", abstractEstimate.getEstimatePreparedBy().getIdPersonalInformation());
			populatePreparedByList(ajaxEstimateAction, abstractEstimate.getExecutingDepartment() != null);
		}
		
		List<AppConfigValues>  apconfVal = worksService.getAppConfigValue("Works","ESTIMATE_ALLOW_FUTUREDATE");
		if(apconfVal!=null && !apconfVal.isEmpty())
			allowFutureDate=apconfVal.get(0).getValue();
	}
	

	public Map<String,Integer> getDraftResolutionRules() {
		List<AppConfigValues> appConfigList = worksService.getAppConfigValue("Works","ESTIMATE_DRAFTRESOLUTION_RULES");
		Map<String,Integer> resultMap = new HashMap<String, Integer>();
		for(AppConfigValues configValue:appConfigList){
			String value[] = configValue.getValue().split(",");
			resultMap.put(value[0], Integer.valueOf(value[1]));
		}
		return resultMap;
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
	
	public boolean budgetCheckRequired(){
		boolean budgetCheck=false;
		List<AppConfigValues> list =worksService.getAppConfigValue("EGF","budgetCheckRequired");
		if(!list.isEmpty())
		{
			String value = list.get(0).getValue();
			if(value.equalsIgnoreCase("Y"))
			{
				budgetCheck=true;      
			}
		}
		return budgetCheck;
	}
	
	public String moveEstimate() {
		abstractEstimate.setIsBudgetCheckRequired(budgetCheckRequired());
		abstractEstimate.setAmountWfRule(getAmountRule());
		String actionName = parameters.get("actionName")[0]; 			
		if(!(CANCEL_ACTION.equalsIgnoreCase(actionName) && abstractEstimate.getId()==null)) { 
			saveEstimate(actionName);
		}
		//abstractEstimateService.getCurrentFinancialYear(abstractEstimate.getEstimateDate());
		/**
		 * for the year end process getCurrentFinancialYear API should return the next CFinancialYear object
		 */
		getCurrentFinancialYear();
		
		if(abstractEstimate.getIsSpillOverWorks()){
			if(isSkipBudgetCheck()){
				abstractEstimate.setAdditionalWfRule("spillOverDepositCodeApp");
			}else{
				abstractEstimate.setAdditionalWfRule("spillOverBudgetApp");
			}
		}else{
			abstractEstimate.setAdditionalWfRule(getAdditionalRule());
		}

		abstractEstimate.setAmountWfRule(getAmountRule());
		
		if(actionName.equalsIgnoreCase("save")){
			abstractEstimate.setEgwStatus(commonsService.getStatusByModuleAndCode("AbstractEstimate","NEW"));
			if(id ==null){
			Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
			abstractEstimate = (AbstractEstimate) workflowService.start(abstractEstimate, pos, "Abstract Estimate created.");
			abstractEstimateService.setEstimateNumber(abstractEstimate);
			}
			messageKey="estimate."+abstractEstimate.getEgwStatus().getCode();
			addActionMessage(getText(messageKey,"The Abstract Estimate was saved successfully"));
			abstractEstimate = abstractEstimateService.persist(abstractEstimate);
			getDesignation(abstractEstimate);
		}
		else{
			if(id ==null){
				abstractEstimate.setEgwStatus(commonsService.getStatusByModuleAndCode("AbstractEstimate","NEW"));
				Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
				abstractEstimate = (AbstractEstimate) workflowService.start(abstractEstimate, pos, "Abstract Estimate created.");
				abstractEstimateService.setEstimateNumber(abstractEstimate);
				abstractEstimate = abstractEstimateService.persist(abstractEstimate);
			}
			workflowService.transition(actionName, abstractEstimate, approverComments);
			abstractEstimate = abstractEstimateService.persist(abstractEstimate);
			messageKey="estimate."+abstractEstimate.getEgwStatus().getCode();
			getDesignation(abstractEstimate);
		}
		String estimateStatus = abstractEstimate.getEgwStatus().getCode();
		this.createEstimateAuditTrail(abstractEstimate,estimateStatus);
		//abstractEstimate = (AbstractEstimate) workflowService.transition(actionName, abstractEstimate,approverComments);
		/*if(abstractEstimate.getCurrentState()!= null && abstractEstimate.getCurrentState().getPrevious()!=null && STATE_ADMIN_SANCTIONED.equalsIgnoreCase(
				abstractEstimate.getCurrentState().getPrevious().getValue())
				&&(abstractEstimate.getType()!=null &&  
			    abstractEstimate.getType().getExpenditureType()!=null )){		
			abstractEstimateService.setProjectCode(abstractEstimate); 
		}*/ 
		
		//abstractEstimateService.setEstimateNumber(abstractEstimate);
/*		abstractEstimate=abstractEstimateService.persist(abstractEstimate);
		messageKey="estimate."+actionName; 
		addActionMessage(getText(messageKey,"The estimate was saved successfully"));
		
		getDesignation(abstractEstimate);	
		return SAVE_ACTION.equals(actionName)?EDIT:SUCCESS;		
*/		
		
		return SAVE_ACTION.equalsIgnoreCase(actionName)?EDIT:SUCCESS;
	}
	
	private void createEstimateAuditTrail(AbstractEstimate estimate, String estimateStatus){
		
		String estimateAuditDetails1 = "";
		String estimateAuditDetails2 = "";
		DecimalFormat df = new DecimalFormat("###.##");
		
		estimateAuditDetails1 = getText("audit.trail.estimate.amount")+ new BigDecimal(df.format(estimate.getTotalAmount().getValue())).setScale(2)+
							"," +getText("audit.trail.estimate.jurisdiction")+ estimate.getWard().getName()+","+getText("audit.trail.estimate.natureOfWork")+estimate.getType().getName()+
							","+getText("audit.trail.estimate.typeOfWork")+estimate.getParentCategory().getDescription();
		
		estimateAuditDetails2=getText("audit.trail.estimate.status")+(estimate.getEgwStatus().getCode())+","+
							getText("audit.trail.estimate.fund")+(estimate.getFinancialDetails().get(0).getFund().getName())+","+
							getText("audit.trail.estimate.budgetHead")+(estimate.getFinancialDetails().get(0).getBudgetGroup().getName());
				
		generateAuditEventForEstimate(estimateStatus,  estimateAuditDetails1, estimateAuditDetails2);
		}
	
	public void generateAuditEventForEstimate(String action, String estimateAuditDetails1, String estimateAuditDetails2) {
		final AuditEvent auditEvent = new AuditEvent(AuditModule.WORKS, AuditEntity.WORKS_ESTIMATE, 
				action, abstractEstimate.getEstimateNumber(), estimateAuditDetails1);
		auditEvent.setPkId(abstractEstimate.getId());
		auditEvent.setDetails2(estimateAuditDetails2);
		this.auditEventService.createAuditEvent(auditEvent, AbstractEstimate.class);
	}

	public void getDesignation(AbstractEstimate abstractEstimate){
		/* start for customizing workflow message display */
		if(abstractEstimate.getCurrentState()!= null 
				&& !"NEW".equalsIgnoreCase(abstractEstimate.getCurrentState().getValue())) {
			String result = worksService.getEmpNameDesignation(abstractEstimate.getState().getOwner(),abstractEstimate.getState().getCreatedDate());
			
			 setHideWorkFlowInfo(worksService.getEmpList(abstractEstimate.getState().getCreatedDate(), abstractEstimate.getState().getOwner()));
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
			ajaxEstimateAction.setWorksService(worksService);
			if((sourcepage.equalsIgnoreCase("inbox") 
					&& abstractEstimate!=null && abstractEstimate.getCurrentState()!=null 
					&& abstractEstimate.getEstimatePreparedBy()!=null 
					&& !abstractEstimate.getCurrentState().getValue().equalsIgnoreCase("NEW") 
					&& !abstractEstimate.getCurrentState().getValue().equalsIgnoreCase("REJECTED"))
					|| sourcepage.equalsIgnoreCase("search"))
			{	
				ajaxEstimateAction.userWithEmpCodeInDeptOnDate(abstractEstimate.getEstimatePreparedBy().getEmployeeCode(),
						abstractEstimate.getCreatedDate());
				List <EmployeeView> empViewList = ajaxEstimateAction.getUsersInExecutingDepartment();
				if(empViewList!=null && !empViewList.isEmpty())
					preparedByTF = empViewList.get(0).getEmployeeName() +"-"+ empViewList.get(0).getDesigId().getDesignationName();
				else
					preparedByTF = abstractEstimate.getEstimatePreparedBy().getEmployeeName();
			}
			else
				ajaxEstimateAction.usersInExecutingDepartment();
			addDropdownData("preparedByList", ajaxEstimateAction.getUsersInExecutingDepartment());
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
		String status=null;
		abstractEstimate.setIsEmergencyWorks(isEmergencyWorks);
		abstractEstimate.setIsSpillOverWorks(isSpillOverWorks);

		abstractEstimate.getEstimateRateContractList().clear();
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
		populateFinancialDetail();
	
		AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
		ajaxEstimateAction.setPersistenceService(getPersistenceService());
		populateOverheadsList(ajaxEstimateAction, abstractEstimate.getEstimateDate()!=null);
		
		if(getModel().getCurrentState()!=null){
			status=getModel().getCurrentState().getValue();
		}
		else{
			status="NEW";
		}
		
		if((actionName.equalsIgnoreCase("Forward")||actionName.equalsIgnoreCase("Approve")) && customizedWorkFlowService.getWfMatrix(getModel().getStateType(), getWorkFlowDepartment(), null, getAdditionalRule(),status, getPendingActions())==null && !abstractEstimate.getIsSpillOverWorks()){
			String msg="Workflow is not available for "+getWorkFlowDepartment();
			throw new ValidationException(Arrays.asList(new ValidationError(null,msg)));
		}
				
		if(!(SAVE_ACTION.equalsIgnoreCase(actionName)||CANCEL_ACTION.equalsIgnoreCase(actionName)||REJECT_ACTION.equals(actionName)) && abstractEstimate.getActivities().isEmpty()){
			throw new ValidationException(Arrays.asList(new ValidationError("estimate.activities.empty","estimate.activities.empty")));		
		}
		
		if(!(SAVE_ACTION.equalsIgnoreCase(actionName)||CANCEL_ACTION.equalsIgnoreCase(actionName)||REJECT_ACTION.equals(actionName)) && abstractEstimate.getWorkValue().getValue()<=0.0){
			throw new ValidationException(Arrays.asList(new ValidationError("estimate.workvalue.null","estimate.workvalue.null")));			
		}	
			
		if("Approve".equalsIgnoreCase(actionName) && abstractEstimate.getIsBudgetCheckRequired() && abstractEstimate.getIsSpillOverWorks()){
			if(isSkipBudgetCheck()){
				if(abstractEstimate.getTotalAmount().getValue() > abstractEstimateService.getDepositWorksBalance(abstractEstimate).doubleValue()){    
					throw new ValidationException(Arrays.asList(new ValidationError("abstractEstimate.estimate.validate.deposit.amount","abstractEstimate.estimate.validate.deposit.amount")));
		}		
			}else{
				if(abstractEstimate.getTotalAmount().getValue() > abstractEstimateService.getBudgetAvailable(abstractEstimate).doubleValue()){  
					throw new ValidationException(Arrays.asList(new ValidationError("abstractEstimate.estimate.validate.budget.amount","abstractEstimate.estimate.validate.budget.amount")));
				}
			}
		}			
		try {
			abstractEstimate = abstractEstimateService.persist(abstractEstimate);
		}
		catch(ValidationException validExp) {
			sourcepage="inbox";
			throw validExp;
		}
		
		
		if(appConfigValueId!=null && selectedchecklistValue!=null && appConfigValueId.length>0 && selectedchecklistValue.length>0)
		{
			deleteChecklist(appConfigValueId[0],abstractEstimate);
			for(int i=0;i<appConfigValueId.length;i++)
			{
				if(appConfigValueId[i]!=null && !selectedchecklistValue[i].equals("-1")){
					EgChecklists checklist = new EgChecklists();
					checklist.setAppconfigvalue((AppConfigValues)getPersistenceService().find("from AppConfigValues where id=?", Integer.valueOf(appConfigValueId[i].toString())));
					checklist.setChecklistvalue(selectedchecklistValue[i]);
					checklist.setObjectid(abstractEstimate.getId());
					checklist.setRemarks(StringEscapeUtils.unescapeHtml(checkListremarks[i]));
					checklistService.persist(checklist);
				}
			}
		}
		
		populateEstimateRateContracts(); 
	}
	 
	private void populateFinancialDetail(){  
		if(depositCodeId!=null && depositCodeId!=-1) {
			abstractEstimate.setDepositCode(depositCodeService.findById(depositCodeId, false));
		}
		else
			abstractEstimate.setDepositCode(null);
		
		abstractEstimate.getFinancialDetails().clear();
		for(FinancialDetail financialDetail: actionFinancialDetails) {
			 if (validFinancialDetails(financialDetail)) {
				 financialDetail.setFund((Fund) getPersistenceService().find("from Fund where id = ? ",financialDetail.getFund().getId()));
				 financialDetail.setFunction((CFunction) getPersistenceService().find("from CFunction where id = ? ",financialDetail.getFunction().getId()));
				 if(financialDetail.getBudgetGroup().getId()==-1)
					 financialDetail.setBudgetGroup(null);
				 else
					 financialDetail.setBudgetGroup((BudgetGroup) getPersistenceService().find("from BudgetGroup where id = ? ",financialDetail.getBudgetGroup().getId()));
				 
				 if(financialDetail.getCoa().getId()==-1)
					 financialDetail.setCoa(null);
				 else 
					 financialDetail.setCoa((CChartOfAccounts) getPersistenceService().find("from CChartOfAccounts where id = ? ",financialDetail.getCoa().getId()));
				 if(financialDetail.getFunctionary().getId()==-1)
					 financialDetail.setFunctionary(null);
				 else 
					 financialDetail.setFunctionary((Functionary) getPersistenceService().find("from Functionary where id = ? ",financialDetail.getFunctionary().getId()));
				 if(financialDetail.getScheme().getId()==-1)
					 financialDetail.setScheme(null);
				 else 
					 financialDetail.setScheme((Scheme) getPersistenceService().find("from Scheme where id = ? ",financialDetail.getScheme().getId()));
				 if(financialDetail.getSubScheme().getId()==-1)
					 financialDetail.setSubScheme(null);
				 else 
					 financialDetail.setSubScheme((SubScheme) getPersistenceService().find("from SubScheme where id = ? ",financialDetail.getSubScheme().getId()));
				 financialDetail=populateFinancingSourceDetails(financialDetail);
				 financialDetail.setAbstractEstimate(abstractEstimate);
				 abstractEstimate.addFinancialDetails(financialDetail);
			 }			 
		 }	
	}
	
	protected FinancialDetail populateFinancingSourceDetails(FinancialDetail financialDetail){
		for(FinancingSource finSource: financingSourceList){
			if(validFinancingSource(finSource)){
			finSource.setFundSource((Fundsource) getPersistenceService().find("from Fundsource where id = ? ",finSource.getFundSource().getId()));
			financialDetail.addFinancingSource(finSource);
			}
		}
		return financialDetail;
	}
	
	protected boolean validFinancialDetails(FinancialDetail financialDetail){
		if(financialDetail!=null && financialDetail.getFund()!=null && financialDetail.getFund().getId()!=null){
			return true;
		}		
		return false;
	}
	 
	
	protected boolean validFinancingSource(FinancingSource finSource){
		if(finSource!=null && finSource.getFundSource()!=null && 
				finSource.getFundSource().getId()!=null){
			return true;
		}		
		return false;
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
				 if(activity.getNonSor().getId()!=null && activity.getNonSor().getId()!=0 && activity.getNonSor().getId()!=1) {
					NonSor nonsor=(NonSor)getPersistenceService().find("from NonSor where id = ?", activity.getNonSor().getId());
					if(nonsor==null) {//In case of error on save of estimate, the nonsor is not yet persisted .
						activity.getNonSor().setId(null);//To clear the id which got created through previous persist
						 nonSorService.persist(activity.getNonSor()); 
                     }
					else{
						 activity.setNonSor(nonsor);
					}
					
				 }
				 else{
					 if(activity.getNonSor()!=null) {
						 nonSorService.persist(activity.getNonSor()); 
						 // getPersistenceService().getSession().flush();
						 // getPersistenceService().getSession().refresh(activity.getNonSor());
					 }
				 }			 
				 
				 abstractEstimate.addActivity(activity);
			 }
		 }
	 }
		
	 private void populateActivities() {
		 int count=1;
		 for(Activity activity: abstractEstimate.getActivities()) {
			 activity.setAbstractEstimate(abstractEstimate);
			 if(!measurementSheetList.isEmpty()){
				 for (MeasurementSheet ms: measurementSheetList) { 
					   	 if (ms!=null) {
							if(((ms.getActivity().getSchedule()!=null && ms.getActivity().getSchedule().getId() !=null 
									 && activity.getSchedule()!=null && activity.getSchedule().getId()!=null && 
									 activity.getSchedule().getId().equals(ms.getActivity().getSchedule().getId())))) {
										 ms.setActivity(activity);
										 activity.addMeasurementSheet(ms);
							 }
							 else if( ms.getActivity()!=null  && ms.getSlNo()!=null && activity!=null && activity.getSchedule()==null){
										 if(activity.getSrlNo().equals(ms.getSlNo())) {
											 ms.setActivity(activity);
											 activity.addMeasurementSheet(ms);
										 }								 
								 } 
							}
					 	
				 } 
			 }

			 if(activity.getSrlNo()!=null) {				 
                 activity.setSrlNo(count++);
			 }
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
	
	 
	 protected void populateEstimateRateContracts() {
		 for(EstimateRateContract estimateRateContract: getEstimateRateContractLists()) { 
			 if (validEstimateRateContract(estimateRateContract)) {
				 for(Activity activity: abstractEstimate.getActivities()) {	
					 if(activity.getRcId()==estimateRateContract.getRateContract().getId()){
						 EstimateRateContractDetail ercd = new EstimateRateContractDetail();
						 ercd.setEstimateRC(estimateRateContract);
						 ercd.setActivity(activity);
						 estimateRateContract.addEstimateRateContractDetail(ercd);
	 }
				 }				
				 estimateRateContract.setRateContract((RateContract) getPersistenceService().find("from RateContract where id = ?", estimateRateContract.getRateContract().getId()));
				 estimateRateContract.setEstimate(abstractEstimate);	
				 abstractEstimate.addEstimateRateContract(estimateRateContract);
			 }
		 }
	 
	 }
	 	 
	 public Collection<EstimateRateContract> getEstimateRateContractLists(){
			return CollectionUtils.select(actionEstimateRateContractList, new Predicate(){
				public boolean evaluate(Object sstimateRateContract) {
					return ((EstimateRateContract)sstimateRateContract)!=null;
				}});
		}
	 
	 protected boolean validEstimateRateContract(EstimateRateContract estimateRateContract) {
		 if (estimateRateContract!= null && estimateRateContract.getRateContract() != null && estimateRateContract.getRateContract().getId() != null) {
			 return true;
		 }
		 
		 return false;
	 }
	 
	 @SkipValidation
	 public String viewTechSanctionEstimatePDF() throws JRException,Exception{
		 	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Constants.LOCALE);
		 	ArrayList<Object> al = new ArrayList<Object>();
		 	al.add("Dummy Text");
			Map<String,Object> reportParams = new HashMap<String,Object>();
			if(abstractEstimate != null){
				reportParams.put("estimate",abstractEstimate);
				reportParams.put("finyear", abstractEstimate.getMultiYearEstimates().isEmpty()?financialYear:abstractEstimate.getMultiYearEstimates().get(0).getFinancialYear().getFinYearRange());
				reportParams.put("budgetHead", abstractEstimate.getFinancialDetails().get(0).getBudgetGroup().getName()); 
				Double amount = abstractEstimate.getTotalAmount().getValue();
				reportParams.put("estimatedAmount", amount.toString());
				if(abstractEstimate.getProjectCode()!=null)
				reportParams.put("projectDate", sdf.format(abstractEstimate.getProjectCode().getCreatedDate()).toString());
				TechnicalSanction ts=technicalSanctionService.findByNamedQuery("getLatestTechSanctionNumber",abstractEstimate.getId());
				if(abstractEstimate.getCurrentState().getPrevious().getValue().equals("ADMIN_SANCTIONED")){
					reportParams.put("AADate", sdf.format(abstractEstimate.getCurrentState().getCreatedDate()).toString());
				}
				String techSanctionBy=""; 
				String estCheckedBy=""; 
				String estPreparedBy=""; 
				
				List<State> history=abstractEstimate.getCurrentState().getHistory();
				
				if(history!=null){
					for (State ad : history){
						PersonalInformation emp=employeeService.getEmpForUserId(ad.getCreatedBy().getId());
						if(ad.getValue().equals("TECH_SANCTIONED")) { 	
							if(emp.getEmployeeLastName()!=null)
								techSanctionBy=emp.getEmployeeFirstName()+" "+emp.getEmployeeLastName()+" / "+ad.getPrevious().getOwner().getDeptDesigId().getDesigId().getDesignationName();
							else
								techSanctionBy=emp.getEmployeeFirstName()+" / "+ad.getPrevious().getOwner().getDeptDesigId().getDesigId().getDesignationName();
						}
						if(ad.getValue().equals("TECH_SANCTION_CHECKED")) {
							if(emp.getEmployeeLastName()!=null)
								estCheckedBy=emp.getEmployeeFirstName()+" "+emp.getEmployeeLastName()+" / "+ad.getPrevious().getOwner().getDeptDesigId().getDesigId().getDesignationName();
							else
								estCheckedBy=emp.getEmployeeFirstName()+" / "+ad.getPrevious().getOwner().getDeptDesigId().getDesigId().getDesignationName(); 
								
						}
						if(abstractEstimate.getIsSpillOverWorks()){
							if(ad.getValue().equals("ADMIN_SANCTIONED")) {
								if(emp.getEmployeeLastName()!=null)
									techSanctionBy=emp.getEmployeeFirstName()+" "+emp.getEmployeeLastName()+" / "+ad.getOwner().getDeptDesigId().getDesigId().getDesignationName();
								else
									techSanctionBy=emp.getEmployeeFirstName()+" / "+ad.getOwner().getDeptDesigId().getDesigId().getDesignationName();
								estCheckedBy=techSanctionBy;
							}
						}	
					}
				}
				
				if(abstractEstimate.getEstimatePreparedBy().getEmployeeLastName()!=null)
					estPreparedBy=abstractEstimate.getEstimatePreparedBy().getEmployeeFirstName()+" "+abstractEstimate.getEstimatePreparedBy().getEmployeeLastName()+" / "+employeeService.getPresentDesignation(abstractEstimate.getEstimatePreparedBy().getId()).getDesignationName();
				else
					estPreparedBy=abstractEstimate.getEstimatePreparedBy().getEmployeeFirstName()+" / "+employeeService.getPresentDesignation(abstractEstimate.getEstimatePreparedBy().getId()).getDesignationName();
					
				if(ts!=null){
				reportParams.put("techSansactionDate", sdf.format(ts.getTechSanctionDate()).toString());
				reportParams.put("techSanctionNum", ts.getTechSanctionNumber());
				}
				getDesignation(abstractEstimate);
				reportParams.put("estPreparedBy", estPreparedBy);
				reportParams.put("estCheckedBy", estCheckedBy);
				reportParams.put("techSanctionBy", techSanctionBy);
				reportParams.put("SORYear", getSORYear());
				
				List<AppConfigValues>  acvObj= new LinkedList<AppConfigValues>();
				acvObj=worksService.getAppConfigValue(WORKS,"Estimate-CheckList");
				Integer appConfigId=null;
				if(acvObj!=null && acvObj.size()!=0){
					appConfigId=acvObj.get(0).getKey().getId();
			}
				List<EgChecklists> list = persistenceService.findAllByNamedQuery("checklist.by.appconfigid.and.objectid", abstractEstimate.getId(), appConfigId);
				String s1="",s2="",s3="",s4="",s5="",s6="",temp="";
				if(!list.isEmpty() && list.size()!=0){
					for(EgChecklists egChecklists: list)
					{
						if(egChecklists.getRemarks()!=null && egChecklists.getRemarks().length()>0){
						if(egChecklists.getAppconfigvalue().getValue().replaceAll(" ", "").equalsIgnoreCase(CHECKLIST_VALUE1.replaceAll(" ", "")))
							s1=egChecklists.getRemarks();
						else if(egChecklists.getAppconfigvalue().getValue().replaceAll(" ", "").equalsIgnoreCase(CHECKLIST_VALUE3.replaceAll(" ", "")))
							s3=egChecklists.getRemarks();
						else if(egChecklists.getAppconfigvalue().getValue().replaceAll(" ", "").equalsIgnoreCase(CHECKLIST_VALUE2.replaceAll(" ", "")))
							s2=egChecklists.getRemarks();
						else if(egChecklists.getAppconfigvalue().getValue().replaceAll(" ", "").equalsIgnoreCase(CHECKLIST_VALUE5.replaceAll(" ", "")))
							s5=egChecklists.getRemarks();
						else if(egChecklists.getAppconfigvalue().getValue().replaceAll(" ", "").equalsIgnoreCase(CHECKLIST_VALUE4.replaceAll(" ", "")))
							s4=egChecklists.getRemarks();
						else if(egChecklists.getAppconfigvalue().getValue().replaceAll(" ", "").equalsIgnoreCase(CHECKLIST_VALUE6.replaceAll(" ", "")))
							s6=egChecklists.getRemarks();
						}
					}
				}
				reportParams.put("remarks1", s1);
				if(s3.length()>0)
					temp=s3;
				if(s2.length()>0){
					if(temp.length()>0) 
						temp=temp.concat(",  ").concat(s2);
					else 
						temp=s2;
				}
				reportParams.put("remarks2", temp);
				
				temp="";
				if(s5.length()>0)
					temp=s5;
				if(s4.length()>0){
					if(temp.length()>0) 
						temp=temp.concat(",  ").concat(s4);
					else 
						temp=s4;
				}
				reportParams.put("remarks3", temp); 
				reportParams.put("remarks4", s6);
			}
			ReportRequest reportInput = new ReportRequest("TechSanction",al, reportParams);
			ReportOutput reportOutput = reportService.createReport(reportInput);   
			if (reportOutput != null && reportOutput.getReportOutputData() != null)
				pdfInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
			return PRINT;
		}
	 
	 private String getSORYear() {
			String year="";
			List<AppConfigValues> appConfigList = worksService.getAppConfigValue("Works","SCHEDULE_RATE_YEAR");
			for(AppConfigValues configValue:appConfigList){
				String value[] = configValue.getValue().split(",");
				String date[] = value[0].split("-");
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",new Locale("en","IN"));
				try {
					Date startDate=df.parse(date[0]);
					Date endDate=df.parse(date[1]);
					Date estDate=df.parse(df.format(abstractEstimate.getEstimateDate()));
					if(DateConversionUtil.isWithinDateRange(estDate, startDate, endDate))
						year=value[1];
				}
				catch (ParseException pe) {
					logger.error("Error in parsing date"+pe.getMessage());
				}			
			}
			return year;
		}
	 
	 	/**
		 * print pdf
		 * @throws JRException,Exception 
		 */
		@SkipValidation
		public String viewMeasurementSheetPdf() throws JRException,Exception{
			AbstractEstimate estimate= abstractEstimateService.findById(id, false); 
			ReportRequest reportRequest = new ReportRequest("MeasurementSheet",getMeasurementSheetForEstimate(estimate),
					createHeaderParams(estimate,MSHEET));
			ReportOutput reportOutput = reportService.createReport(reportRequest); 
			if(reportOutput!=null && reportOutput.getReportOutputData()!=null)
				pdfInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData()); 
			return PRINT; 
		}
	 
		public Map createHeaderParams(AbstractEstimate estimate, String type){
			Map<String,Object> reportParams = new HashMap<String,Object>();
			List<AbstractEstimate> ae=new LinkedList<AbstractEstimate>() ;
			if(type.equals(MSHEET)){
				reportParams.put("workName", estimate.getName());
				reportParams.put("estimateNo", estimate.getEstimateNumber());
				List<MeasurementSheet> tempList=(List<MeasurementSheet>) getMeasurementSheetForEstimate(estimate);
				reportParams.put("activitySize", tempList.size());
				ae= abstractEstimateService.findAllByNamedQuery("HAS_REVISION_ESTIMATES", estimate.getId());
				reportParams.put("RE_Estimates", ae);
			}
			else if(type.equals(DRAFT_RESOLUTION)){
				reportParams.put("amount", estimate.getTotalAmount().getFormattedString());
				if(estimate.getWard().getBoundaryNum()!=null)
					reportParams.put("ward",estimate.getWard().getBoundaryNum().toString().concat(", ").concat(estimate.getWard().getName()));
				else
					reportParams.put("ward",estimate.getWard().getName());
			}
			else{
				reportParams.put("workName", estimate.getName());
				reportParams.put("deptName",estimate.getExecutingDepartment().getDeptName());
				reportParams.put("estimateNo", estimate.getEstimateNumber());
				reportParams.put("activitySize", estimate.getActivities().size());
				ae= abstractEstimateService.findAllByNamedQuery("HAS_REVISION_ESTIMATES", estimate.getId());
				reportParams.put("RE_Estimates", ae);
			}
			return reportParams; 
		}
		
		// For Draft Resolution Report - Generated only if the totalamount is more than 10lakhs
		@SkipValidation
		public String viewDraftResolution() throws JRException,Exception{
			AbstractEstimate estimate= abstractEstimateService.findById(id, false); 
			List<MeasurementSheet> finalList = new LinkedList<MeasurementSheet>();
			ReportRequest reportRequest = new ReportRequest("DraftResolution",new ArrayList(),
					createHeaderParams(estimate,DRAFT_RESOLUTION));
			reportRequest.setReportFormat(FileFormat.XLS);
			ReportOutput reportOutput = reportService.createReport(reportRequest); 
			if(reportOutput!=null && reportOutput.getReportOutputData()!=null)
				inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());  
			return PRINTDRAFTRESOLUTION;
		}
		
		@SkipValidation
		public String viewBillOfQuantitiesPdf() throws JRException,Exception{
			AbstractEstimate estimate= abstractEstimateService.findById(id, false); 
			ReportRequest reportRequest = new ReportRequest("BillOfQuantities",estimate.getActivities(), 
					createHeaderParams(estimate,BOQ));
			ReportOutput reportOutput = reportService.createReport(reportRequest); 
			if(reportOutput!=null && reportOutput.getReportOutputData()!=null)
				pdfInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData()); 
			return PRINT;  
		}
		
		private Collection<MeasurementSheet> getMeasurementSheetForEstimate(AbstractEstimate estimate){
			List<MeasurementSheet> finalList = new LinkedList<MeasurementSheet>();
			MeasurementSheet estimateMSheet; 
			int slno=1;
			for(Activity act:estimate.getActivities())
			{	
					if(act.getMeasurementSheetList().size()!=0){
						double sumOfQuantity=0;
						finalList.add(addActivityName(act,slno++)); 
						for(MeasurementSheet ms:act.getMeasurementSheetList()){
							ms.setSlNo(null); 
							//ms.setQuantity(ms.calculateQuantity());
										if(ms.getIdentifier()=='D')
											sumOfQuantity=sumOfQuantity-ms.getQuantity(); 
										else
											sumOfQuantity=sumOfQuantity+ms.getQuantity();
							if(!(act.getMeasurementSheetList().size()>1)){
								ms.setUom(act.getUom().getUom());
							}
							finalList.add(ms);
						}
						if(act.getMeasurementSheetList().size()>1){
							estimateMSheet=new MeasurementSheet();
							estimateMSheet.setSlNo(null);
							estimateMSheet.setNo(null);
							estimateMSheet.setRemarks(null);
							estimateMSheet.setUomLength(0);
							estimateMSheet.setWidth(0);
							estimateMSheet.setDepthOrHeight(0);
							estimateMSheet.setQuantity(sumOfQuantity);
							estimateMSheet.setUom(act.getUom().getUom());
							finalList.add(estimateMSheet); 
						}
					}
					else
						finalList.add(addActivityName(act,slno++));
			}
			return finalList;
		}
		
		private MeasurementSheet addActivityName(Activity activity,int slNo){
			MeasurementSheet estimateMSheet=new MeasurementSheet();
			estimateMSheet.setSlNo(slNo);
			estimateMSheet.setNo(null);
			if(activity.getSchedule()!=null)
				estimateMSheet.setRemarks(activity.getSchedule().getDescription());
			if(activity.getNonSor()!=null)
				estimateMSheet.setRemarks(activity.getNonSor().getDescription());
			estimateMSheet.setUomLength(0);
			estimateMSheet.setWidth(0);
			estimateMSheet.setDepthOrHeight(0); 
			
			if(activity.getMeasurementSheetList().size()!=0) 
				estimateMSheet.setQuantity(0); 
			else{
					estimateMSheet.setQuantity(activity.getQuantity());
					estimateMSheet.setUom(activity.getUom().getUom());
				}
			return estimateMSheet;  
		}
 
	 protected boolean validMultiYearEstimate(MultiYearEstimate multiYearEstimate) {
		 if (multiYearEstimate!= null && multiYearEstimate.getFinancialYear() != null && multiYearEstimate.getFinancialYear().getId() != null && multiYearEstimate.getFinancialYear().getId()!=0 &&
				 multiYearEstimate.getPercentage() >= 0.0) {
			 return true;
		 }
		 
		 return false;
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
		String finyearRange = worksService.getWorksConfigValue("FINANCIAL_YEAR_RANGE");
		if(StringUtils.isNotBlank(finyearRange))
			return commonsService.getFinancialYearByFinYearRange(finyearRange);
		else
			return commonsService.getFinancialYearById(Long.valueOf(commonsService.getCurrYearFiscalId()));
	} 

	public EmployeeService getEmployeeService() {
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
	
/*	public List<Action> getValidActions(){
		return workflowService.getValidActions(abstractEstimate);
	}
*/
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

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
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
		this.approverComments = StringEscapeUtils.unescapeHtml(approverComments);
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

	public Long getSorId() {
		return sorId;
	}

	public void setSorId(Long sorId) {
		this.sorId = sorId;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getSorDesc() {
		return sorDesc;
	}

	public void setSorDesc(String sorDesc) {
		this.sorDesc = sorDesc;
	}

	public List<MeasurementSheet> getMeasurementSheetList() {
		return measurementSheetList;
	}

	public void setMeasurementSheetList(List<MeasurementSheet> measurementSheetList) {
		this.measurementSheetList = measurementSheetList; 
	}

	public Double getTotalMSheetQty() {
		return totalMSheetQty;
	}

	public void setTotalMSheetQty(Double totalMSheetQty) {
		this.totalMSheetQty = totalMSheetQty;
	}

	public String getEstimateUOM() {
		return estimateUOM;
	}

	public void setEstimateUOM(String estimateUOM) {
		this.estimateUOM = estimateUOM;
	}

	public String getEstimateLineItem() {
		return estimateLineItem;
	}

	public void setEstimateLineItem(String estimateLineItem) {
		this.estimateLineItem = estimateLineItem;
	}

	public String getNonsorDesc() {
		return nonsorDesc;
	}

	public void setNonsorDesc(String nonsorDesc) {
		this.nonsorDesc = nonsorDesc;
	}

	public String getNonsorId() {
		return nonsorId;
	}

	public void setNonsorId(String nonsorId) {
		this.nonsorId = nonsorId;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public InputStream getPdfInputStream() {
		return pdfInputStream;
	}

	public void setIsSpillOverWorks(boolean isSpillOverWorks) {
		this.isSpillOverWorks = isSpillOverWorks;
	}

	public void setIsEmergencyWorks(boolean isEmergencyWorks) {
		this.isEmergencyWorks = isEmergencyWorks;
	}
	
	public void setBudgetGroupDAO(BudgetGroupDAO budgetGroupDAO) {
		this.budgetGroupDAO = budgetGroupDAO;
	}
	
	protected void populateSubSchemeList(
			AjaxFinancialDetailAction ajaxFinancialDetailAction, 
			boolean schemePopulated,boolean datePresent) {
		if (schemePopulated && datePresent) {
			ajaxFinancialDetailAction.setSchemeId(financialDetail.getScheme().getId());
			ajaxFinancialDetailAction.setEstimateDate(
					financialDetail.getAbstractEstimate().getEstimateDate());
			ajaxFinancialDetailAction.loadSubSchemes();
			addDropdownData("subSchemeList", ajaxFinancialDetailAction.getSubSchemes());		
		}
		else {
			addDropdownData("subSchemeList", Collections.emptyList());
		}
	}
	
	protected void populateSchemeList(AjaxFinancialDetailAction ajaxFinancialDetailAction,
			boolean fundPopulated, boolean datePresent) {
		if (fundPopulated && datePresent) {
			ajaxFinancialDetailAction.setFundId(financialDetail.getFund().getId());
			ajaxFinancialDetailAction.setEstimateDate(
					financialDetail.getAbstractEstimate().getEstimateDate());
			ajaxFinancialDetailAction.loadSchemes();
			addDropdownData("schemeList", ajaxFinancialDetailAction.getSchemes());		
		}
		else {
			addDropdownData("schemeList", Collections.emptyList());
		}
	}
		
	protected void populateBudgetGroupList(
			AjaxFinancialDetailAction ajaxFinancialDetailAction, 
			boolean functionPopulated, boolean datePresent) throws Exception {
		if (functionPopulated && datePresent) {
			ajaxFinancialDetailAction.setFunctionId(
					financialDetail.getFunction().getId());
			ajaxFinancialDetailAction.setEstimateDate(
					financialDetail.getAbstractEstimate().getEstimateDate());
			ajaxFinancialDetailAction.loadBudgetGroups();
			addDropdownData(
					BUDGET_GROUP_LIST, ajaxFinancialDetailAction.getBudgetGroups());		
		}
		else if(!functionPopulated){
			try {
				addDropdownData(BUDGET_GROUP_LIST, 
						(List<BudgetGroup>) budgetGroupDAO.getBudgetGroupList());
			} catch (EGOVRuntimeException e) {
				addFieldError("budgetheadexception", "Unable to load budget head ");
			}
		}
	}
	
/*	public Boolean isSkipBudgetCheck(){
		List<String> depositTypeList=getAppConfigValuesToSkipBudget();
		if(abstractEstimate!=null && abstractEstimate.getId()!=null){
			for(String type:depositTypeList){
				if(type.equals(abstractEstimate.getType().getName())){
					skipBudget=true;
				}
			}
		}	
		return skipBudget;
	}
*/
	public Boolean isSkipBudgetCheck(){
		List<String> depositTypeList=getAppConfigValuesToSkipBudget();
		if(abstractEstimate!=null && abstractEstimate.getType()!=null && abstractEstimate.getType().getName()!=null){
			for(String type:depositTypeList){
				if(type.equals(abstractEstimate.getType().getName())){
					skipBudget=true;
				}
			}
		}	
		return skipBudget;
	}


	protected void deleteChecklist(Long appConfValue, AbstractEstimate entity){
		if(appConfValue!=null && entity!=null){
			AppConfigValues acvEntity=(AppConfigValues)getPersistenceService().find("from AppConfigValues acv where acv.id=?", Integer.valueOf(appConfValue.intValue()));
			List<EgChecklists> list = persistenceService.findAllByNamedQuery("checklist.by.appconfigid.and.objectid", entity.getId(), acvEntity.getKey().getId());
			for(EgChecklists eclEntity: list){
				checklistService.delete(eclEntity); 
			}
		}
	}
	
	public List<Fundsource> getFundSourceList() {
		return fundSourceList;
	}

	public void setFundSourceList(List<Fundsource> fundSourceList) {
		this.fundSourceList = fundSourceList;
	}

	public List<FinancialDetail> getActionFinancialDetails() {
		return actionFinancialDetails;
	}

	public void setActionFinancialDetails(
			List<FinancialDetail> actionFinancialDetails) {
		this.actionFinancialDetails = actionFinancialDetails;
	}

	public List<FinancingSource> getFinancingSourceList() {
		return financingSourceList;
	}

	public void setFinancingSourceList(List<FinancingSource> financingSourceList) {
		this.financingSourceList = financingSourceList;
	}

	public FinancialDetail getFinancialDetail() {
		return financialDetail;
	}

	public void setFinancialDetail(FinancialDetail financialDetail) {
		this.financialDetail = financialDetail;
	}

	public Long getDepositCodeId() {
		return depositCodeId;
	}

	public void setDepositCodeId(Long depositCodeId) {
		this.depositCodeId = depositCodeId;
	}

	public PersistenceService<DepositCode, Long> getDepositCodeService() {
		return depositCodeService;
	}

	public void setDepositCodeService(
			PersistenceService<DepositCode, Long> depositCodeService) {
		this.depositCodeService = depositCodeService;
	}

	public boolean isSkipBudget() {
		return skipBudget;
	}

	public void setSkipBudget(boolean skipBudget) {
		this.skipBudget = skipBudget;
	}

	public String getPendingActions()
	{
		return abstractEstimate==null?"":(abstractEstimate.getCurrentState()==null?"":abstractEstimate.getCurrentState().getNextAction());
	}

	public String getWorkFlowDepartment(){
		return abstractEstimate==null?"":(abstractEstimate.getExecutingDepartment()==null?"":abstractEstimate.getExecutingDepartment().getDeptName());
	}
	
	protected BigDecimal getAmountRule() {
		return abstractEstimate==null?null:(abstractEstimate.getTotalAmount()==null?null:new BigDecimal(abstractEstimate.getTotalAmount().getValue()));
	}
	
	protected String getAdditionalRule() {
		additionalRuleValue = "";
        /*if(StringUtils.isNotBlank(getWorkFlowDepartment()) && PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(getWorkFlowDepartment())){
        	additionalRuleValue=abstractEstimate==null?"":(isSkipBudgetCheck()==true?"depositCodeApp":"budgetApp");
        	List functionCodes = getFunctionCodes(); 
			if(functionCodes!=null && !functionCodes.isEmpty() && functionCodes.contains(abstractEstimate.getFinancialDetails().get(0).getFunction().getCode())){
				if("depositCodeApp".equalsIgnoreCase(additionalRuleValue)){
	                    additionalRuleValue="zonalDepositCodeApp";
	                }
	                else{
	                    additionalRuleValue="zonalBudgetApp";
	                }
			}
			else{
				if("depositCodeApp".equalsIgnoreCase(additionalRuleValue)){
                    additionalRuleValue="HQDepositCodeApp";
                }
                else{
                    additionalRuleValue="HQBudgetApp";
                }
			}
        }else{
            additionalRuleValue=abstractEstimate==null?"":(isSkipBudgetCheck()==true?"depositCodeApp":"budgetApp");
        }*/
        return additionalRuleValue;
	}

	public String getAdditionalRuleValue() { 
	    if(StringUtils.isNotBlank(getWorkFlowDepartment()) && PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(getWorkFlowDepartment())){
			additionalRuleValue=abstractEstimate==null?"":(isSkipBudgetCheck()==true?"depositCodeApp":"budgetApp");
			List functionCodes = getFunctionCodes();
			if(functionCodes!=null && !functionCodes.isEmpty() && functionCodes.contains(abstractEstimate.getFinancialDetails().get(0).getFunction().getCode())){
				if("depositCodeApp".equalsIgnoreCase(additionalRuleValue)){
	                    additionalRuleValue="zonalDepositCodeApp";
	                }
	                else{
	                    additionalRuleValue="zonalBudgetApp";
	                }
			}
			else{
				if("depositCodeApp".equalsIgnoreCase(additionalRuleValue)){
	                additionalRuleValue="HQDepositCodeApp";
	            }
	            else{
	                additionalRuleValue="HQBudgetApp";
	            }
			}
	    }else{
	        additionalRuleValue=abstractEstimate==null?"":(isSkipBudgetCheck()==true?"depositCodeApp":"budgetApp");
	    }
		return additionalRuleValue;
	}
	
	public List getFunctionCodes(){
		List<AppConfigValues> appConfigList = worksService.getAppConfigValue("Works","WORKS_PWD_FUNCTIONWISE_WF");
		List functionCodes = new LinkedList();
		if(appConfigList!=null && !appConfigList.isEmpty()){
			if(appConfigList.get(0).getValue()!="" && appConfigList.get(0).getValue()!=null){
				String[] configVals=appConfigList.get(0).getValue().split(",");
				for(int i=0; i<configVals.length;i++)
					functionCodes.add(configVals[i]);
			}
		}
		return functionCodes;
	}


	public void setAdditionalRuleValue(String additionalRuleValue) {
		this.additionalRuleValue = additionalRuleValue;
	}

	public BigDecimal getAmountRuleValue() {
		amountRuleValue=abstractEstimate==null?null:(abstractEstimate.getTotalAmount()==null?null:new BigDecimal(abstractEstimate.getTotalAmount().getValue()));
		return amountRuleValue;
	}

	public void setAmountRuleValue(BigDecimal amountRuleValue) {
		this.amountRuleValue = amountRuleValue;
	}

	public void setIsRateContract(boolean isRateContract) {
		this.isRateContract = isRateContract;
	}

	public List<EstimateRateContract> getActionEstimateRateContractList() {
		return actionEstimateRateContractList;
	}

	public void setActionEstimateRateContractList(
			List<EstimateRateContract> actionEstimateRateContractList) {
		this.actionEstimateRateContractList = actionEstimateRateContractList;
	}

	public List<Contractor> getContractorList() {
		if(abstractEstimate.getId()!=null && !abstractEstimate.getEstimateRateContractList().isEmpty())
			return contractorList=(List<Contractor>)getPersistenceService().findAllBy("select distinct erc.rateContract.contractor from EstimateRateContract erc where erc.estimate.id = ?", abstractEstimate.getId());
		else
			return new ArrayList<Contractor>();
	}

	public void setContractorList(List<Contractor> contractorList) {
		this.contractorList = contractorList;
	}

	public PersistenceService<NonSor, Long> getNonSorService() {
		return nonSorService;
	}

	public void setNonSorService(PersistenceService<NonSor, Long> nonSorService) {
		this.nonSorService = nonSorService;
	}
	    
	public String getRcType() {
		return rcType;
	}

	public void setRcType(String rcType) {
		this.rcType = rcType;
	}

	public boolean getIsRateContract() {
		return isRateContract;
	}
	public List<EstimateRateContractDetail> getEstimateRCDetails() {
		return (List<EstimateRateContractDetail>)getPersistenceService().findAllBy("from EstimateRateContractDetail ercd where ercd.estimateRC.estimate.id = ? order by ercd.activity.id", abstractEstimate.getId());
	}

	public void setShowDraftResolution(boolean showDraftResolution) {
		this.showDraftResolution = showDraftResolution;
	}
	public boolean getShowDraftResolution() {
		return showDraftResolution;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public List<AppConfigValues> getEstimateChecklist() {
		return estimateChecklist;
	}

	public void setEstimateChecklist(List<AppConfigValues> estimateChecklist) {
		this.estimateChecklist = estimateChecklist;
	}

	public Long[] getAppConfigValueId() {
		return appConfigValueId;
	}

	public void setAppConfigValueId(Long[] appConfigValueId) {
		this.appConfigValueId = appConfigValueId;
	}

	public String[] getSelectedchecklistValue() {
		return selectedchecklistValue;
	}

	public void setSelectedchecklistValue(String[] selectedchecklistValue) {
		this.selectedchecklistValue = selectedchecklistValue;
	}

	public PersistenceService<EgChecklists, Long> getChecklistService() {
		return checklistService;
	}

	public void setChecklistService(
			PersistenceService<EgChecklists, Long> checklistService) { 
		this.checklistService = checklistService;
	}

	public List<String> getChecklistValues() {
		return checklistValues;
	}

	public void setChecklistValues(List<String> checklistValues) {
		this.checklistValues = checklistValues;
	}

	public String[] getCheckListremarks() {
		return checkListremarks;
	}

	public void setCheckListremarks(String[] checkListremarks) {
		this.checkListremarks = checkListremarks;
	}

	public boolean getShowResolutionDetails() {
		return showResolutionDetails;
	}

	public void setShowResolutionDetails(boolean showResolutionDetails) {
		this.showResolutionDetails = showResolutionDetails;
	}

	public String getCancelRemarks() {
		return cancelRemarks;
	}

	public void setCancelRemarks(String cancelRemarks) {
		this.cancelRemarks = cancelRemarks;
	}

	public String getAllowFutureDate() {
		return allowFutureDate;
	}

	public void setAllowFutureDate(String allowFutureDate) {
		this.allowFutureDate = allowFutureDate;
	}
	
	public void setAuditEventService(AuditEventService auditEventService) {
  		this.auditEventService = auditEventService;
	}
	
	public String getPreparedByTF() {
		return preparedByTF;
	}

	public void setPreparedByTF(String preparedByTF) {
		this.preparedByTF = preparedByTF;
	}

/*	@SkipValidation
	public String auditReport() {
		return "auditReport";
	}
*/
}
