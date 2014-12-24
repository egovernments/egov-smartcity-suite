package org.egov.works.web.actions.contractorBill;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.assets.model.Asset;
import org.egov.assets.service.CommonAssetsService;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.EgPartytype;
import org.egov.commons.EgwStatus;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.service.CommonsService;
import org.egov.egf.commons.EgovCommon;
import org.egov.exceptions.EGOVException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.EgChecklists;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregistermis;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.recoveries.Recovery;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.services.budget.BudgetService;
import org.egov.services.recoveries.RecoveryService;
import org.egov.services.voucher.VoucherService;
import org.egov.tender.model.GenericTenderResponse;
import org.egov.web.actions.workflow.GenericWorkFlowAction;
import org.egov.works.models.contractorBill.AssetForBill;
import org.egov.works.models.contractorBill.ContractorBillRegister;
import org.egov.works.models.contractorBill.DeductionTypeForBill;
import org.egov.works.models.contractorBill.StatutoryDeductionsForBill;
import org.egov.works.models.contractorBill.WorkCompletionDetailInfo;
import org.egov.works.models.contractorBill.WorkCompletionInfo;
import org.egov.works.models.estimate.FinancialDetail;
import org.egov.works.models.measurementbook.MBBill;
import org.egov.works.models.measurementbook.MBDetails;
import org.egov.works.models.measurementbook.MBForCancelledBill;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.ContractorBillService;
import org.egov.works.services.WorksService;
import org.egov.works.services.impl.MeasurementBookServiceImpl;
import org.egov.works.utils.WorksConstants;

import com.opensymphony.xwork2.validator.annotations.Validation;

@ParentPackage("egov")
@Results({
	@Result(name = ContractorBillAction.PRINT, type="stream", location = "CompletionCertificatePDF", params = {"inputName", "CompletionCertificatePDF", "contentType", "application/pdf","contentDisposition", "no-cache" }),
	@Result(name = ContractorBillAction.PRINTCONTRACTCERTIFICATE, type="stream", location = "ContractCertificatePDF", params = {"inputName", "ContractCertificatePDF", "contentType", "application/pdf","contentDisposition", "no-cache" }),
	@Result(name = ContractorBillAction.PRINTTRANSFERENTRYMEMO, type="stream", location = "TransferEntryMemoPDF", params = {"inputName", "TransferEntryMemoPDF", "contentType", "application/pdf","contentDisposition", "no-cache" })
})


@Validation()
public class ContractorBillAction extends GenericWorkFlowAction {
	private static final Logger logger = Logger.getLogger(ContractorBillAction.class);
	private ContractorBillService contractorBillService;
	private PersistenceService<MBForCancelledBill,Long> cancelBillService;
	private PersistenceService<MBBill,Long> mbBillService;
	private WorksService worksService;
	private ContractorBillRegister contractorBillRegister = new ContractorBillRegister();
	private WorkOrder workOrder;
	private Long id;
	private Long workOrderId;
	private CommonsService commonsService;
	private CommonAssetsService commonAssetsService;
	private MeasurementBookServiceImpl measurementBookService;
	private EgovCommon egovCommon;
	private PersistenceService<EgChecklists, Long> checklistService;
	private List<StatutoryDeductionsForBill> actionStatutorydetails = new LinkedList<StatutoryDeductionsForBill>();
	private List<AssetForBill>	accountDetailsForBill = new ArrayList<AssetForBill>();
	private Map<Long,String> contratorCoaPayableMap = new HashMap<Long,String>();
	private String disp;	
	private BigDecimal totalPendingBalance = BigDecimal.ZERO;
	private BigDecimal advaceAdjustmentCreditAmount= BigDecimal.ZERO;
	private BigDecimal billAmount = BigDecimal.ZERO;
	private BigDecimal utilizedAmount = BigDecimal.ZERO;
	private BigDecimal sanctionedBudget = BigDecimal.ZERO;
	private PersistenceService<FinancialDetail,Long> financialDetailService;
	private Long netPayableCode;
	private BigDecimal netPayableAmount;
	private String partyBillNumber;
	private Date partyBillDate;
	private Date completionDate;
	private Integer partbillNo;
	private String sourcepage="";
	private List<String>  checklistValues= new LinkedList<String>();
	private List<AppConfigValues>  finalBillChecklist= new LinkedList<AppConfigValues>();
	private List<CChartOfAccounts> standardDeductionAccountList = new LinkedList<CChartOfAccounts>();
	private List<CChartOfAccounts> customDeductionAccountList  = new LinkedList<CChartOfAccounts>();
	private List<CChartOfAccounts> retentionMoneyAccountList  = new LinkedList<CChartOfAccounts>();
	private List<String> standardDeductionConfValues  = new LinkedList<String>();
	private List<DeductionTypeForBill> standardDeductions  = new LinkedList<DeductionTypeForBill>();
	private List<EgBilldetails> customDeductions  = new LinkedList<EgBilldetails>();
	private List<EgBilldetails> retentionMoneyDeductions  = new LinkedList<EgBilldetails>();
	private List<EgBilldetails> releaseWithHeldAmountDeductions =new LinkedList<EgBilldetails>();
	private List<MBHeader>  mbHeaderList= new LinkedList<MBHeader>();
	private Long[] appConfigValueId;
	private String[]  selectedchecklistValue;
	private String[] remarks;
	private String[] contractCertRemarks;
	private static final String ASSET_LIST="assestList";
	private static final String COA_LIST="coaList";
	private static final String CAPITAL_WORKS = "Capital Works";
	private static final String IMPROVEMENT_WORKS = "Improvement Works";
	private static final String REPAIR_AND_MAINTENANCE = "Repairs and maintenance";
	private static final String DEPOSIT_NO_ASSET_CREATED = "Deposit Works No Asset Created";
	private static final String DEPOSIT_ASSET_CREATED = "Deposit Works Third Party Asset";
	private static final String DEPOSIT__OWN_ASSET_CREATED = "Deposit Works Own Asset";
	private static final String WORKS_NETPAYABLE_CODE ="WORKS_NETPAYABLE_CODE";
	private static final String RETENTION_MONEY_PURPOSE="RETENTION_MONEY_PURPOSE";
	private static final String KEY_CWIP = "WORKS_CWIP_CODE";
	private static final String KEY_REPAIRS ="WORKS_REPAIRS_AND_MAINTENANCE";
	private static final String KEY_DEPOSIT ="WORKS_DEPOSIT_OTHER_WORKS";
	private static final String BILL_STATUS = "APPROVED";
	private static final String ACCOUNTDETAIL_TYPE_CONTRACTOR = "contractor";
	private static final String EXPENDITURE_TYPE = "Works";
	private static final String BILL_MODULE_KEY = "CONTRACTORBILL";
	private static final String AMOUNT_ERROR = "amount.lessthan.zero";
	private DepartmentService departmentService;
	private WorkflowService<ContractorBillRegister> workflowService;	
	private static final String SAVE_ACTION = "save";
	private String messageKey;
	private static final String ACTION_NAME="actionName";
	private String nextEmployeeName;
	private String nextDesignation;
	private RecoveryService recoveryService;
	private Long workOrderEstimateId;
	private List<WorkOrderEstimate> workOrderEstimateList= new ArrayList<WorkOrderEstimate>();
	private WorkOrderEstimate workOrderEstimate= new WorkOrderEstimate();
	public static final String PRINT = "print";
	private InputStream completionCertificatePDF;
	private ReportService reportService;
	private final static String APPROVED = "APPROVED";
	private final static String CANCELLED = "CANCELLED";
	private static final String APPCONFIG_KEY_NAME = "SKIP_BUDGET_CHECK";
	private VoucherService voucherService;
	private ScriptService scriptExecutionService;	
	private AbstractEstimateService abstractEstimateService;
	private WorkCompletionInfo completionInfo;
	private List<WorkCompletionDetailInfo> completionDetailInfoList=new ArrayList<WorkCompletionDetailInfo>();	
	private boolean skipBudget;
	private static final String CHECK_BUDGET = "CHECK_BUDGET";
	private static final String ACCOUNTDETAIL_TYPE_DEPOSITCODE = "DEPOSITCODE";
	private static final String ACCOUNTDETAIL_TYPE_PROJECTCODE = "PROJECTCODE";
	
	private String isRetMoneyAutoCalculated;
	private String isRetMoneyEditable;
	private String percDeductForRetMoneyPartBill;
	private String percDeductForRetMoneyFinalBill;
	private String retMoneyFinalBillPerOnValue;
	private GenericTenderResponse tenderResponse;
	private String rebatePremLevel;
	
	// Added for Automatic calculation of statutory recoveries in bill 
	private List<EgPartytype> subPartyTypeDtls = new ArrayList<EgPartytype>();
	private List<EgwTypeOfWork> typeOfWorkDtls =new ArrayList<EgwTypeOfWork>();
	public static final String BILL_STATUTORYDEDUCTIONS_SHOW_PARTYSUBTYPE="BILL_STATUTORYDEDUCTIONS_SHOW_PARTYSUBTYPE";	
	public static final String BILL_STATUTORYDEDUCTIONS_SHOW_SERVICETYPE="BILL_STATUTORYDEDUCTIONS_SHOW_SERVICETYPE";
	public static final String BILL_STATUTORYDEDUCTIONS_EDITABLE="BILL_STATUTORYDEDUCTIONS_EDITABLE";
	private String showSubPartyType;
	private String showTypeOfWork;
	private String editStatutoryAmount;
	public static final String PARTY_TYPE_CODE="Contractor";
	private Long estimateId;
	private String percTenderType;
	private String isRebatePremLevelBill;
	private BigDecimal grossAmount = BigDecimal.ZERO;
	private BudgetService budgetService;
	private InputStream contractCertificatePDF;
	public static final String PRINTCONTRACTCERTIFICATE = "printContractCertificate";
	private InputStream transferEntryMemoPDF;
	public static final String PRINTTRANSFERENTRYMEMO = "printTransferEntryMemo";
	private boolean isSecurityDeposit=false;
	private String billGeneratedBy;
	private EmployeeService employeeService;
	private String departmentName;
	private boolean isSpillOverWorks;

	private String additionalRuleValue;
	
	public String getAdditionalRuleValue() {
		return getAdditionalRule();
	}

	private static final String PUBLIC_WORKS_DEPARTMENT="Public Work";
	
	public ContractorBillAction() {		
		
	}

	public void prepare(){		
		super.prepare();
		
		if(id==null){
			PersonalInformation prsnlInfo=employeeService.getEmpForUserId(Integer.valueOf(abstractEstimateService.getCurrentUserId()));
			if(prsnlInfo.getEmployeeFirstName()!=null)
				billGeneratedBy=prsnlInfo.getEmployeeFirstName();
			if(prsnlInfo.getEmployeeLastName()!=null)
				billGeneratedBy=billGeneratedBy.concat(" ").concat(prsnlInfo.getEmployeeLastName()); 
		}
		
		if (id != null) {			
			contractorBillRegister= contractorBillService.findById(id, false);
			List<MBHeader> mbHeaderListForBillId=new LinkedList<MBHeader>() ;
			if(contractorBillRegister.getBillstatus().equals("CANCELLED")){
				
				for(MBForCancelledBill mbCancelBillObj : contractorBillService.getMbListForCancelBill(id))
			    {
					if(!mbHeaderListForBillId.contains(mbCancelBillObj.getMbHeader()))
						mbHeaderListForBillId.add(mbCancelBillObj.getMbHeader());
			    }
			}
			
			else{
			 mbHeaderListForBillId  = measurementBookService.findAllByNamedQuery("getAllMBsForBillId", id);
			}
			 if(mbHeaderListForBillId!=null && !mbHeaderListForBillId.isEmpty()){
				workOrder = mbHeaderListForBillId.get(0).getWorkOrder();
				workOrderId =workOrder.getId();
				workOrderEstimate=mbHeaderListForBillId.get(0).getWorkOrderEstimate();				
			}
			
		}
		
		if(workOrderId!=null) {
		 workOrder = (WorkOrder) persistenceService.find("from WorkOrder where id=?",workOrderId);	
			workOrderEstimateList.addAll(getPersistenceService().findAllByNamedQuery("getWorkOrderEstimateByWorkOrderId", workOrderId));
			loadTenderDetails();
		}
		if (id != null) {
			//if(isRebatePremLevelBill.equals("yes") && tenderResponse.getTenderEstimate().getTenderType().equals(getPercTenderType()))
			if(isRebatePremLevelBill.equals("yes") && tenderResponse.getBidType().name().equals(getPercTenderType()))
				billAmount=contractorBillService.getApprovedMBAmountforBill(contractorBillRegister);
		}
		
		if(workOrderEstimateId!=null){
			workOrderEstimate=(WorkOrderEstimate)persistenceService.find("from WorkOrderEstimate where id=?",workOrderEstimateId);
		}		
		if(workOrderEstimateList.isEmpty()) {
			addDropdownData("workOrderEstimateList", Collections.EMPTY_LIST); 
		}
		else {
			workOrderEstimateList=measurementBookService.getWorkOrderEstimatesForBill(workOrderEstimateList);
			addDropdownData("workOrderEstimateList",workOrderEstimateList);
		}
			
		if(workOrderEstimateList.size()==1 && id == null) {
			
			workOrderEstimate=workOrderEstimateList.get(0);
			mbHeaderList = measurementBookService.getApprovedMBList(workOrder.getId(), workOrderEstimate.getId(),new Date());
		}
		try{
			if(workOrderEstimate!=null && workOrderEstimate.getId()!=null) {
				isSkipBudgetCheck();
				String accountCodeFromBudgetHead=worksService.getWorksConfigValue("BILL_DEFAULT_BUDGETHEAD_ACCOUNTCODE");
				if(workOrderEstimate.getEstimate().getType().getCode().equals(CAPITAL_WORKS)
						|| workOrderEstimate.getEstimate().getType().getCode().equals(IMPROVEMENT_WORKS))
				{
					addDropdownData(ASSET_LIST, workOrderEstimate.getAssetValues());
					if((StringUtils.isNotBlank(accountCodeFromBudgetHead) && "no".equals(accountCodeFromBudgetHead)) && StringUtils.isNotBlank(worksService.getWorksConfigValue(KEY_CWIP))){
						addDropdownData(COA_LIST, commonsService.getAccountCodeByPurpose(Integer.valueOf(worksService.getWorksConfigValue(KEY_CWIP))));
					}
					else if((StringUtils.isNotBlank(accountCodeFromBudgetHead) && "yes".equals(accountCodeFromBudgetHead))){ 
						List<BudgetGroup> budgetGroupList=new ArrayList<BudgetGroup>();
						if(workOrderEstimate.getEstimate().getFinancialDetails().get(0).getBudgetGroup()!=null)
							budgetGroupList.add(workOrderEstimate.getEstimate().getFinancialDetails().get(0).getBudgetGroup());
						List<CChartOfAccounts> coaList=budgetService.getAccountCodeForBudgetHead(budgetGroupList);
						addDropdownData(COA_LIST, coaList);
					}
					else addDropdownData(COA_LIST,Collections.EMPTY_LIST);
				}
				else if(workOrderEstimate.getEstimate().getType().getCode().
						equals(REPAIR_AND_MAINTENANCE)){
					addDropdownData(ASSET_LIST, workOrderEstimate.getAssetValues());
					if((StringUtils.isNotBlank(accountCodeFromBudgetHead) && "no".equals(accountCodeFromBudgetHead)) && StringUtils.isNotBlank(worksService.getWorksConfigValue(KEY_REPAIRS))){
						addDropdownData(COA_LIST, commonsService.getAccountCodeByPurpose(Integer.valueOf(worksService.getWorksConfigValue(KEY_REPAIRS))));
					}
					else if((StringUtils.isNotBlank(accountCodeFromBudgetHead) && "yes".equals(accountCodeFromBudgetHead))){ 
						List<BudgetGroup> budgetGroupList=new ArrayList<BudgetGroup>();
						if(workOrderEstimate.getEstimate().getFinancialDetails().get(0).getBudgetGroup()!=null)
							budgetGroupList.add(workOrderEstimate.getEstimate().getFinancialDetails().get(0).getBudgetGroup());
						List<CChartOfAccounts> coaList=budgetService.getAccountCodeForBudgetHead(budgetGroupList);
						addDropdownData(COA_LIST, coaList);
					}
					else  addDropdownData(COA_LIST,Collections.EMPTY_LIST);
				}
				else if(workOrderEstimate.getEstimate().getType().getCode().equals(DEPOSIT_NO_ASSET_CREATED)
						|| workOrderEstimate.getEstimate().getType().getCode().
						equals(DEPOSIT_ASSET_CREATED)
						|| workOrderEstimate.getEstimate().getType().getCode().equals(DEPOSIT__OWN_ASSET_CREATED))
				{
					addDropdownData(ASSET_LIST, workOrderEstimate.getAssetValues());
					if(StringUtils.isNotBlank(worksService.getWorksConfigValue(KEY_DEPOSIT)) && !skipBudget){
						addDropdownData(COA_LIST, commonsService.getAccountCodeByPurpose(Integer.valueOf(worksService.getWorksConfigValue(KEY_DEPOSIT))));
					}
					else if(StringUtils.isNotBlank(worksService.getWorksConfigValue(KEY_DEPOSIT)) && skipBudget &&
							workOrderEstimate.getEstimate().getFinancialDetails().get(0).getCoa()!=null){
						addDropdownData(COA_LIST, Arrays.asList(commonsService.getCChartOfAccountsByGlCode(workOrderEstimate.
								getEstimate().getFinancialDetails().get(0).getCoa().getGlcode())));
					} 
					else  addDropdownData(COA_LIST,Collections.EMPTY_LIST);
				}		
				
			}
			populateOtherDeductionList();
			setRetentionMoneyConfigValues();
		}
		catch(EGOVException v) {		
			logger.error("Unable to COA for WorkOrder" + v);
			addFieldError("COA.notfound", "Unable to COA for WorkOrder");
		}
		if(contractorBillRegister!=null && contractorBillRegister.getBilldate()==null && workOrderEstimateList.size()==1) {
			workOrderEstimateId=workOrderEstimateList.get(0).getId();
			setBudgetDetails(workOrderId,workOrderEstimateId,new Date());
		}
		/*else if(contractorBillRegister!=null && workOrderEstimateList.size()>1) {
			
			setBudgetDetails(workOrderId,workOrderEstimateId,contractorBillRegister.getBilldate());
		}*/
		
		addDropdownData("billTypeList", contractorBillService.getBillType());
		List<Recovery> statutoryDeductionsList=recoveryService.getAllTdsByPartyType("Contractor");
		addDropdownData("statutoryDeductionsList", statutoryDeductionsList);
		addDropdownData("executingDepartmentList",departmentService.getAllDepartments());
	/*	if(abstractEstimateService.getLatestAssignmentForCurrentLoginUser()!=null) {
			contractorBillRegister.setWorkflowDepartmentId(abstractEstimateService.getLatestAssignmentForCurrentLoginUser().getDeptId().getId());
		}
	*/	
		showSubPartyType=worksService.getWorksConfigValue(BILL_STATUTORYDEDUCTIONS_SHOW_PARTYSUBTYPE);
		logger.debug("showSubPartyType>>>>>> "+showSubPartyType);
		showTypeOfWork=worksService.getWorksConfigValue(BILL_STATUTORYDEDUCTIONS_SHOW_SERVICETYPE);
		logger.debug("showTypeOfWork>>>>>> "+showTypeOfWork); 
		editStatutoryAmount=worksService.getWorksConfigValue(BILL_STATUTORYDEDUCTIONS_EDITABLE);
		logger.debug("editStatutoryAmount>>>>>>> "+editStatutoryAmount);

		List<EgPartytype> subPartyTypeList=new ArrayList<EgPartytype>();
		List<EgwTypeOfWork> typeOfWorkList=new ArrayList<EgwTypeOfWork>();
		if(showSubPartyType!=null && showSubPartyType!=""){ 
			subPartyTypeList=commonsService.getSubPartyTypes(PARTY_TYPE_CODE); 
			addDropdownData("subPartyTypeList", subPartyTypeList);
		}
		else{
			addDropdownData("subPartyTypeList", subPartyTypeList);
		}
			
		if(showTypeOfWork!=null && showTypeOfWork!=""){
			typeOfWorkList=getPersistenceService().findAllBy("from EgwTypeOfWork wt where wt.parentid is null");
			addDropdownData("typeOfWorkList", typeOfWorkList); 
		}
		else{
			addDropdownData("typeOfWorkList", typeOfWorkList);
		}
		departmentName=getWorkFlowDepartment();
		if(workOrderEstimate!=null){
			if(workOrderEstimate.getEstimate()!=null){
				isSpillOverWorks=workOrderEstimate.getEstimate().getIsSpillOverWorks();
			}
		}else{
			isSpillOverWorks=false;
		}
		getAdditionalRule();
	}
	
	private void loadTenderDetails(){
		//tenderResponse = (TenderResponse) persistenceService.find("from TenderResponse tr where tr.negotiationNumber=?",workOrder.getNegotiationNumber());
		tenderResponse = (GenericTenderResponse) persistenceService.find("from org.egov.tender.model.GenericTenderResponse tr where tr.number=?",workOrder.getNegotiationNumber());
		rebatePremLevel = worksService.getWorksConfigValue("REBATE_PREMIUM_LEVEL");
		if(rebatePremLevel.equalsIgnoreCase(WorksConstants.BILL)){
			isRebatePremLevelBill=WorksConstants.YES;
		}
		else{
			isRebatePremLevelBill=WorksConstants.NO;
		}
		List<String> tenderTypeList=worksService.getTendertypeList();
		if(tenderTypeList!=null && !tenderTypeList.isEmpty()){
			percTenderType=tenderTypeList.get(0);
		}
	}
	
	
	private void setRetentionMoneyConfigValues(){
		isRetMoneyAutoCalculated=worksService.getWorksConfigValue("IS_RETENTION_MONEY_AUTOCALCULATED");
		isRetMoneyEditable=worksService.getWorksConfigValue("IS_RETENTION_MONEY_EDITABLE");
		percDeductForRetMoneyPartBill=worksService.getWorksConfigValue("PERCENTAGE_DEDUCTION_FOR_RETENTION_MONEY_PART_BILL");
		percDeductForRetMoneyFinalBill=worksService.getWorksConfigValue("PERCENTAGE_DEDUCTION_FOR_RETENTION_MONEY_FINAL_BILL");
		retMoneyFinalBillPerOnValue=worksService.getWorksConfigValue("RETENTION_MONEY_PERCENTAGE_ON_VALUE");
	}

	private void validateGlcodeBalForDepositWorks() {
		if(skipBudget && contractorBillRegister.getBilldate()!=null  && workOrderEstimate!=null
				&& workOrderEstimate.getEstimate().getFinancialDetails().get(0).getCoa()!=null){
			AjaxContractorBillAction ajaxContractorBillAction = new AjaxContractorBillAction();
			ajaxContractorBillAction.setPersistenceService(getPersistenceService());
			ajaxContractorBillAction.setCommonsService(commonsService);
			ajaxContractorBillAction.setEgovCommon(egovCommon);
			BigDecimal glcodeAmount = BigDecimal.ZERO;
			BigDecimal balGlcodeAmount = BigDecimal.ZERO;
			ajaxContractorBillAction.setGlCodeId(Long.valueOf(workOrderEstimate.
					getEstimate().getFinancialDetails().get(0).getCoa().getId()));
			ajaxContractorBillAction.setEstimateId(workOrderEstimate.getEstimate().getId());
			ajaxContractorBillAction.setBillDate(contractorBillRegister.getBilldate());
			glcodeAmount=contractorBillRegister.getBillamount();
			try{
			ajaxContractorBillAction.setDespositWorksAccBal();
			if(ajaxContractorBillAction.getBudgBalance()!=null){
				balGlcodeAmount = BigDecimal.valueOf(ajaxContractorBillAction.getBudgBalance().doubleValue()*-1);
				if(balGlcodeAmount.doubleValue()<glcodeAmount.doubleValue())
					addFieldError("Insufficient.funds", "Insufficient funds available. The bill cannot be passed");
			}
			}catch(Exception e){
				logger.error("error in  loading budget balance for desposit works" + e);
				addFieldError("error.loading.budget", "Error in loading Budget Details For Desposit Works");
			}
		}
	}
	
	@SkipValidation
	public String edit() throws Exception{
		checklistValues.add("Yes");
		checklistValues.add("No");
		contractorBillRegister=getContractorbillregisterforbillnumber(contractorBillRegister.getBillnumber());//contractorBillService.findByNamedQuery("getContractorBillRegister",contractorBillRegister.getBillnumber());
	   if(contractorBillRegister!=null){
		setId(contractorBillRegister.getId());
		if(!(isRebatePremLevelBill.equals("yes") && tenderResponse.getBidType().name().equals(getPercTenderType())))
			setBillAmount(contractorBillRegister.getPassedamount()); 
		setCompletionDate(workOrderEstimate.getWorkCompletionDate());
		setPartyBillNumber(contractorBillRegister.getEgBillregistermis().getPartyBillNumber());	
		setPartyBillDate(contractorBillRegister.getEgBillregistermis().getPartyBillDate());
			    
		if(contractorBillRegister.getBillstatus().equals("CANCELLED")){
		
			for(MBForCancelledBill mbCancelBillObj : contractorBillService.getMbListForCancelBill(id))
		    {
				if(!mbHeaderList.contains(mbCancelBillObj.getMbHeader())){
		    		mbHeaderList.add(mbCancelBillObj.getMbHeader());
				}
			}
		}
		else{
		for(MBHeader mbObj : contractorBillService.getMbListForBillAndWorkordrId(workOrderId,id)) {
	         if(!mbHeaderList.contains(mbObj)){
	    		mbHeaderList.add(mbObj);
		   }
		 }
	  }

		workOrderEstimate=mbHeaderList.get(0).getWorkOrderEstimate();
		setAdvaceAdjustmentCreditAmount(contractorBillService.getAdvanceAdjustmentAmountForBill(id));
		setTotalPendingBalance(contractorBillService.calculateContractorTotalPendingBalance(contractorBillRegister.getBilldate(),workOrder, workOrderEstimate,contractorBillRegister.getId()));
		
		
		if(worksService.checkBigDecimalValue(contractorBillService.getNetPaybleCode(id), BigDecimal.ZERO)){
			setNetPayableCode(contractorBillService.getNetPaybleCode(id).longValue());
			setNetPayableAmount(contractorBillService.getNetPayableAmountForGlCodeId(id));
		}
		
		//setUtilizedAmount(contractorBillService.getTotalUtilizedAmount(wortionStatutorydetails::"+actionStatutorydetails);
		contractorBillService.setAllViewLists(id,workOrderId,actionStatutorydetails,standardDeductions,customDeductions,retentionMoneyDeductions,
	    		accountDetailsForBill,releaseWithHeldAmountDeductions);
	    List<EgChecklists> list = contractorBillService.getEgcheckList(id);
		int i=0;
		selectedchecklistValue =new String[list.size()];
		for(EgChecklists egChecklists: list)
		{
			finalBillChecklist.add(egChecklists.getAppconfigvalue());
			selectedchecklistValue[i]=egChecklists.getChecklistvalue();
			i++;
		}
	   }
	
	  if(contractorBillRegister != null && contractorBillRegister.getId()!=null && getSecurityDepositDetails()!=null){
		   isSecurityDeposit=true;
	  }
	  
	  /*if(isSpillOverWorks){
			contractorBillRegister.setAdditionalWfRule("spillOverWorks");
		}
		else{
			 if(PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(getWorkFlowDepartment()) && getAdditionalRule().equalsIgnoreCase("ZonalPublicWork"))
				 contractorBillRegister.setAdditionalWfRule("ZonalPublicWork");
			 else if(PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(getWorkFlowDepartment())){
				 contractorBillRegister.setAdditionalWfRule("HQPublicWork");
			 }
			 }
*/	  
	  
	return EDIT;
	}
	
	@SkipValidation
	public String viewCompletionCertificate(){
		ReportRequest reportRequest = null;
		Map<String, Object> reportParams = new HashMap<String, Object>();
		WorkCompletionInfo workCompletionInfo= getWorkcompletionInfo();
		reportParams.put(WorksConstants.PARAMETERNAME_WORKCOMPLETIONINFO,workCompletionInfo);
		reportParams.put("completionCertificateInfo", getWorkCompletionDetailInfo()); 
		reportRequest = new ReportRequest(WorksConstants.TEMPLATENAME_COMPLETIONCERTIFICATE,workCompletionInfo, reportParams);
		ReportOutput reportOutput = reportService.createReport(reportRequest);
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			completionCertificatePDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
		return PRINT;
	}

	@SkipValidation
	public String viewContractCertificate(){
		ReportRequest reportRequest = null;
		Map<String, Object> reportParams = new HashMap<String, Object>();
		WorkCompletionInfo workCompletionInfo= getContractCertInfo();
		reportParams.put(WorksConstants.PARAMETERNAME_WORKCOMPLETIONINFO,workCompletionInfo);
		reportRequest = new ReportRequest(WorksConstants.TEMPLATENAME_CONTRACTCERTIFICATE,getContractCertDetailInfo(), reportParams);
		ReportOutput reportOutput = reportService.createReport(reportRequest);
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			contractCertificatePDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
		return PRINTCONTRACTCERTIFICATE;
	}
	
	@SkipValidation
	public String viewTransferEntryMemo() throws Exception{
		StringBuffer accountDetails=new StringBuffer();
		Map<String,Object> reportParams = new HashMap<String,Object>();
		if(workOrderEstimate != null && workOrder != null){
			String workName=workOrderEstimate.getEstimate().getName();
			String contractorDetail=workOrder.getContractor().getCode()+"-"+workOrder.getContractor().getName();
			reportParams.put("contractorName",contractorDetail);
			reportParams.put("workName",workName+" .");
		}
		List<EgBilldetails> accountDetailsForBill=persistenceService.findAllByNamedQuery("getAllAccountDetailList",id);
		if(accountDetailsForBill != null && !accountDetailsForBill.isEmpty()){
			for(EgBilldetails billDetails:accountDetailsForBill){
				CChartOfAccounts accountDetail=commonsService.getCChartOfAccountsById(billDetails.getGlcodeid().longValue());
				accountDetails.append(accountDetail.getGlcode()+"-"+accountDetail.getName()+"\n");
			}
			reportParams.put("accountDetails", accountDetails.toString());
		}
		EgBilldetails securityDepositAccountDetail=getSecurityDepositDetails();
		if(securityDepositAccountDetail != null){
			CChartOfAccounts caoForSecurityDeposit=commonsService.getCChartOfAccountsById(securityDepositAccountDetail.getGlcodeid().longValue());
			String functionCode=commonsService.getCFunctionById(securityDepositAccountDetail.getFunctionid().longValue()).getCode();
			reportParams.put("securityDepositAccount",caoForSecurityDeposit.getGlcode()+"-"+caoForSecurityDeposit.getName());
			reportParams.put("functionCode",functionCode);
			reportParams.put("amount",securityDepositAccountDetail.getCreditamount());
		}		
		ReportRequest reportInput = new ReportRequest("transferEntryMemo",accountDetails, reportParams);
		ReportOutput reportOutput = reportService.createReport(reportInput);   
		if (reportOutput != null && reportOutput.getReportOutputData() != null)
			transferEntryMemoPDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
		return PRINTTRANSFERENTRYMEMO;
	}
	
	public EgBilldetails getSecurityDepositDetails(){
		EgBilldetails securityDepositAccountDetail=null;
		String purposeId=String.valueOf(egovCommon.getAccountCodePurposeByName("Security Deposit").getId());
		for(EgBilldetails egBillDetail:contractorBillRegister.getEgBilldetailes()){
			if(commonsService.getCChartOfAccountsById(egBillDetail.getGlcodeid().longValue()).getPurposeId() != null && commonsService.getCChartOfAccountsById(egBillDetail.getGlcodeid().longValue()).getPurposeId().equals(purposeId)){
				securityDepositAccountDetail=egBillDetail;
			}
		}
		return securityDepositAccountDetail;
    }

	public List<WorkCompletionDetailInfo> getWorkCompletionDetailInfo() {
		List<MBHeader> mbHeaderList=(List<MBHeader>)persistenceService.findAllByNamedQuery(WorksConstants.QUERY_GETALLMBHEADERSBYBILLID, contractorBillRegister.getId());
		if(!mbHeaderList.isEmpty())
			return contractorBillService.setWorkCompletionDetailInfoList(mbHeaderList.get(0).getWorkOrderEstimate());
		return new ArrayList<WorkCompletionDetailInfo>();
	}

	public WorkCompletionInfo getWorkcompletionInfo() {
		List<MBHeader> mbHeaderList=(List<MBHeader>)persistenceService.findAllByNamedQuery(WorksConstants.QUERY_GETALLMBHEADERSBYBILLID, contractorBillRegister.getId());
		if(!mbHeaderList.isEmpty())
			return contractorBillService.setWorkCompletionInfoFromBill(contractorBillRegister,mbHeaderList.get(0).getWorkOrderEstimate());
		return new WorkCompletionInfo();		 
	}
	
	public List<WorkCompletionDetailInfo> getContractCertDetailInfo() {
		List<MBHeader> mbHeaderList=getMBHeaderListFromBill(contractorBillRegister);
		if(!mbHeaderList.isEmpty())
			return contractorBillService.setWorkContractCertDetailInfoList(contractorBillRegister,mbHeaderList.get(0).getWorkOrderEstimate());
		return new ArrayList<WorkCompletionDetailInfo>();
	}

	public WorkCompletionInfo getContractCertInfo() {
		List<MBHeader> mbHeaderList=getMBHeaderListFromBill(contractorBillRegister);
		if(!mbHeaderList.isEmpty())
			return contractorBillService.setWorkContractCertInfoFromBill(contractorBillRegister,mbHeaderList.get(0).getWorkOrderEstimate());
		return new WorkCompletionInfo();		 
	} 
	
	public List<MBHeader> getMBHeaderListFromBill(ContractorBillRegister contractorBillRegister){
		List<MBHeader> mbHeaderList=new ArrayList<MBHeader>();
		if(contractorBillRegister.getBillstatus().equals("CANCELLED")){
			List<MBForCancelledBill> mbForCancelledBillList=contractorBillService.getMbListForCancelBill(contractorBillRegister.getId());
			for(MBForCancelledBill mbForCancelledBill:mbForCancelledBillList){
				mbHeaderList.add(mbForCancelledBill.getMbHeader());
			}
			 
		}
		else {
			mbHeaderList=(List<MBHeader>)persistenceService.findAllByNamedQuery(WorksConstants.QUERY_GETALLMBHEADERSBYBILLID, contractorBillRegister.getId());
		}
		return mbHeaderList;
	}
	public ContractorBillRegister getContractorbillregisterforbillnumber(String billNumber){
		return contractorBillService.findByNamedQuery("getContractorBillRegister",billNumber);
	}

	public StateAware getModel() {
		return contractorBillRegister;
	}
	
	@SkipValidation
	public String view(){
		return "view";
	}
	
	public void setModel(ContractorBillRegister contractorBillRegister) {
		this.contractorBillRegister=contractorBillRegister;
	}

	public String execute()	{
	     return SUCCESS;
	}

    @SkipValidation
	public String newform(){
    	if(!contractorBillService.getBillType().isEmpty()){
    		List<MBHeader> objList = measurementBookService.getPartBillList(workOrderId,contractorBillService.getBillType().get(0).toString());
    		contractorBillRegister.setPartbillNo(objList.size()+1);
    	}
    	return NEW;
	}    
  
	public String save() throws Exception{
		String status=null;
		String actionName = "";
		if (parameters.get(ACTION_NAME) != null && parameters.get(ACTION_NAME)[0] != null) 
			actionName = parameters.get(ACTION_NAME)[0];
		contractorBillRegister.getEgBilldetailes().clear();
		contractorBillRegister.getStatutoryDeductionsList().clear();
		contractorBillRegister.getDeductionTypeList().clear();
		contractorBillRegister.getAssetDetailsList().clear();
		workOrderEstimate=(WorkOrderEstimate)persistenceService.find("from WorkOrderEstimate where id=?",workOrderEstimateId);
		contractorBillRegister.setWorkOrderId(workOrderEstimate.getWorkOrder().getId());
		if(id==null)
			mbHeaderList = measurementBookService.getApprovedMBList(workOrderId, workOrderEstimateId,contractorBillRegister.getBilldate());
		else
			mbHeaderList = contractorBillService.getMbListForBillAndWorkordrId(workOrderId,id);
					
		ContractorBillRegister contractorBill=setEgBillRegister(); 
		isSkipBudgetCheck();
		if(!skipBudget)	
			checkBudgetandGenerateNumber(contractorBill); 

		if(getModel().getCurrentState()!=null){
			status=getModel().getCurrentState().getValue();
		}
		else{
			status="NEW";
		}
		if((actionName.equalsIgnoreCase("Forward")||actionName.equalsIgnoreCase("Approve")) && customizedWorkFlowService.getWfMatrix(getModel().getStateType(), getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),status, getPendingActions())==null && !workOrderEstimate.getEstimate().getIsSpillOverWorks()){
			String msg="Workflow is not available for "+getWorkFlowDepartment();
			throw new ValidationException(Arrays.asList(new ValidationError(null,msg)));
		}
		
		double totalDrAmount = 0;
		double totalCrAmount = 0;
		
		if(!contractorBill.getEgBilldetailes().isEmpty()) {
			for(EgBilldetails billDetails:contractorBill.getEgBilldetailes()) {
				if(billDetails.getDebitamount()!=null)
					totalDrAmount = totalDrAmount + billDetails.getDebitamount().doubleValue();
				
				if(billDetails.getCreditamount()!=null)
					totalCrAmount = totalCrAmount + billDetails.getCreditamount().doubleValue();
			}
		} 
						
		if(Double.parseDouble(new DecimalFormat("#.##").format(totalDrAmount)) != Double.parseDouble(new DecimalFormat("#.##").format(totalCrAmount))) {	
			if(id==null)
				setBudgetDetails(workOrderId,workOrderEstimateId,contractorBillRegister.getBilldate());
			billAmount=contractorBillRegister.getBillamount();
			throw new ValidationException(Arrays.asList(new ValidationError("contractorBill.validate_totalDebitAndCredit","contractorBill.validate_totalDebitAndCredit")));
		}

		contractorBillService.persist(contractorBillRegister);
		if(contractorBillRegister.getBilltype().equals(contractorBillService.getBillType().get(1).toString())){
			workOrderEstimate.setWorkCompletionDate(getCompletionDate());
			//workOrder.setWorkCompletionDate(getCompletionDate());
			contractorBillRegister.setPartbillNo(null);
			int i=0;
			for(WorkCompletionDetailInfo workCompletionDetailInfo : getWorkCompletionDetailInfo()){
				if(getRemarks()!=null && getRemarks().length>i && getRemarks()[i]!=null)
					workCompletionDetailInfo.getWorkOrderActivity().setRemarks(getRemarks()[i]);
				    i++;
			}
		}
		if(contractorBillRegister.getBilltype().equals(contractorBillService.getBillType().get(0).toString())){
			int i=0;
			for(WorkCompletionDetailInfo workCompletionDetailInfo : getContractCertDetailInfo()){
				if(contractCertRemarks!=null && contractCertRemarks.length>i && contractCertRemarks[i]!=null){
					for(WorkOrderActivity woa:workOrderEstimate.getWorkOrderActivities()){
						if(woa.getId().equals(workCompletionDetailInfo.getWorkOrderActivity().getId()))
							woa.setRemarks(contractCertRemarks[i]);
					}
				}
				    i++;
			}
		}
		for(MBHeader mbObj : mbHeaderList){
			MBBill mbbill=null;
			if(contractorBillRegister.getId()!=null)
				mbbill =mbBillService.find("from MBBill where mbHeader.id=? and egBillregister.id=?", mbObj.getId(), contractorBillRegister.getId());
			if(mbbill==null){
				mbbill=new MBBill();
				mbbill.setEgBillregister(contractorBillRegister);
				mbbill.setMbHeader(mbObj);
				mbBillService.persist(mbbill);
			}
		}
		if(appConfigValueId!=null && selectedchecklistValue!=null && appConfigValueId.length>0 && selectedchecklistValue.length>0)
		{
			for(int i=0;i<appConfigValueId.length;i++)
			{
				
				if(appConfigValueId[i]!=null && !selectedchecklistValue[i].equals("-1")){
					EgChecklists checklist = new EgChecklists();
					checklist.setAppconfigvalue((AppConfigValues)getPersistenceService().find("from AppConfigValues where id=?", Integer.valueOf(appConfigValueId[i].toString())));
					checklist.setChecklistvalue(selectedchecklistValue[i]);
					checklist.setObjectid(contractorBillRegister.getId());
					checklistService.persist(checklist);
				}
			}
		}

		if(workOrderEstimate.getEstimate().getIsSpillOverWorks()){
			contractorBillRegister.setAdditionalWfRule("spillOverWorks");
		}
		else{
			 if(PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(getWorkFlowDepartment()) && getAdditionalRule().equalsIgnoreCase("ZonalPublicWork"))
				 contractorBillRegister.setAdditionalWfRule("ZonalPublicWork");
			 else if(PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(getWorkFlowDepartment())){
				 contractorBillRegister.setAdditionalWfRule("HQPublicWork");
			 }
		}
		
		if(actionName.equalsIgnoreCase("save")){
			System.out.println("insdie save");
			contractorBillRegister.setStatus(commonsService.getStatusByModuleAndCode(BILL_MODULE_KEY,"NEW"));
			contractorBillRegister.setBillstatus("NEW");
			if(id ==null){
			Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
			contractorBillRegister = (ContractorBillRegister) workflowService.start(contractorBillRegister, pos, "Contractor Bill created");
			}
			messageKey="contractorBill."+contractorBillRegister.getBillstatus();
			addActionMessage(getText(messageKey,"The Contractor Bill was saved successfully"));
			contractorBillRegister = contractorBillService.persist(contractorBillRegister);
			getDesignation(contractorBillRegister);

		}
		else{
			if(id ==null){
				contractorBillRegister.setStatus(commonsService.getStatusByModuleAndCode(BILL_MODULE_KEY,"NEW"));
				contractorBillRegister.setBillstatus("NEW");
				Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
				contractorBillRegister = (ContractorBillRegister) workflowService.start(contractorBillRegister, pos, "Contractor Bill created");
				contractorBillRegister = contractorBillService.persist(contractorBillRegister);
			}
			workflowService.transition(actionName, contractorBillRegister, approverComments);
			contractorBillRegister = contractorBillService.persist(contractorBillRegister);
			if(contractorBillRegister.getBillstatus().equals("CANCELLED")){
				cancel();
			}
			messageKey="contractorBill."+contractorBillRegister.getBillstatus();
			getDesignation(contractorBillRegister);
		}

	
		/*		contractorBillRegister= workflowService.transition(actionName, contractorBillRegister,approverComments);
		
		if(contractorBillRegister.getCurrentState()!=null && contractorBillRegister.getCurrentState().getPrevious()!=null && 
				contractorBillRegister.getCurrentState().getValue()!=null 
				&&  (APPROVED.equalsIgnoreCase(contractorBillRegister.getCurrentState().getPrevious().getValue()) || 
						CANCELLED.equalsIgnoreCase(contractorBillRegister.getCurrentState().getPrevious().getValue()))){
			messageKey="bill.approved";
			contractorBillRegister.setBillstatus(contractorBillRegister.getCurrentState().getPrevious().getValue());
			contractorBillRegister.setStatus(commonsService.getStatusByModuleAndCode(BILL_MODULE_KEY,
					contractorBillRegister.getCurrentState().getPrevious().getValue()));
		}
		else{
			messageKey="bill.save.success";
			contractorBillRegister.setBillstatus(contractorBillRegister.getCurrentState().getValue());
			contractorBillRegister.setStatus(commonsService.getStatusByModuleAndCode(BILL_MODULE_KEY,
					contractorBillRegister.getCurrentState().getValue()));
		}
*/		
		getPersistenceService().getSession().flush();
		getPersistenceService().getSession().refresh(contractorBillRegister);	
		contractorBillRegister.getEgBillregistermis().setSourcePath("/egworks/contractorBill/contractorBill!edit.action?id="+contractorBillRegister.getId()+"&workOrderId="+workOrderId+"&billnumber="+contractorBillRegister.getBillnumber()+"&sourcepage=search");
		
		//addActionMessage(getText(messageKey,messageKey));
		//getDesignation(contractorBillRegister);  
		return SAVE_ACTION.equalsIgnoreCase(actionName)?edit():SUCCESS;	
	}
	
	protected String getAdditionalRule() {
		if(workOrderEstimate!=null){
			if(workOrderEstimate.getEstimate()!=null && !workOrderEstimate.getEstimate().getIsSpillOverWorks() && workOrderEstimate.getEstimate().getExecutingDepartment()!=null && PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(workOrderEstimate.getEstimate().getExecutingDepartment().getDeptName())){
				List functionCodes = getFunctionCodes();
				if(functionCodes!=null && !functionCodes.isEmpty() && functionCodes.contains(workOrderEstimate.getEstimate().getFinancialDetails().get(0).getFunction().getCode())){				
					additionalRuleValue="ZonalPublicWork";
					contractorBillRegister.setAdditionalWfRule(additionalRuleValue);
					
				}
				else { 
						additionalRuleValue="HQPublicWork";
						contractorBillRegister.setAdditionalWfRule(additionalRuleValue);
				}
				
			}
			return additionalRuleValue;
		}
		return null;
	}
	
	public List getFunctionCodes() {
		List<AppConfigValues> appConfigList = worksService.getAppConfigValue(
				"Works", "WORKS_PWD_FUNCTIONWISE_WF");
		List functionCodes = new LinkedList();
		if (appConfigList != null && !appConfigList.isEmpty()) {
			if (appConfigList.get(0).getValue() != ""
					&& appConfigList.get(0).getValue() != null) {
				String[] configVals = appConfigList.get(0).getValue()
						.split(",");
				for (int i = 0; i < configVals.length; i++)
					functionCodes.add(configVals[i]);
			}
		}
		return functionCodes;
	} 
	
	private String isSkipBudgetCheck(){
		List<AppConfigValues> appConfigValuesList=worksService.getAppConfigValue("Works",APPCONFIG_KEY_NAME);
		logger.info("lenght of appconfig values>>>>>> "+appConfigValuesList.size());
		if(workOrderEstimate!=null && workOrderEstimate.getId()!=null){
			for(AppConfigValues appValues:appConfigValuesList){
				if(appValues.getValue().equals(workOrderEstimate.getEstimate().getType().getName()))
				{
					skipBudget=true;
					return CHECK_BUDGET;
				}
			}	
			skipBudget=false;
		}
		return CHECK_BUDGET;	
	}
	
	
	private ContractorBillRegister checkBudgetandGenerateNumber(ContractorBillRegister contractorBill) {
		ScriptContext scriptContext = ScriptService.createContext("voucherService",voucherService,"bill",contractorBill);
		scriptExecutionService.executeScript( "egf.bill.budgetcheck", scriptContext);
		return contractorBill;   
	}
	
	public void getDesignation(ContractorBillRegister bill){
		if(contractorBillRegister.getCurrentState()!= null 
				&& !"NEW".equalsIgnoreCase(bill.getCurrentState().getValue())) {
			String result = worksService.getEmpNameDesignation(contractorBillRegister.getState().getOwner(),contractorBillRegister.getState().getCreatedDate());
			if(result != null && !"@".equalsIgnoreCase(result)) {
				String empName = result.substring(0,result.lastIndexOf('@'));
				String designation =result.substring(result.lastIndexOf('@')+1,result.length());
				setNextEmployeeName(empName);
				setNextDesignation(designation);
			}
		}
	}
	
	public void cancel(){
		//String actionName = parameters.get("actionName")[0]; 
		if(contractorBillRegister.getId()!=null){
			//contractorBillRegister = workflowService.transition(actionName, contractorBillRegister,approverComments);
			//contractorBillRegister.setBillstatus(contractorBillRegister.getCurrentState().getPrevious().getValue());
			//contractorBillRegister.setStatus(commonsService.getStatusByModuleAndCode(BILL_MODULE_KEY,
				//	contractorBillRegister.getCurrentState().getPrevious().getValue()));
			//contractorBillService.persist(contractorBillRegister);
			List<MBHeader> mbHeaderListForBillId  = measurementBookService.findAllByNamedQuery("getAllMBsForBillId", contractorBillRegister.getId());
								
			for(MBHeader mbObj : mbHeaderListForBillId)
		    {
				for(MBBill mbBill:mbObj.getMbBills()){
					if(mbBill.getEgBillregister().getId().equals(contractorBillRegister.getId())){
				MBForCancelledBill mbCB=new MBForCancelledBill();
				       	mbCB.setEgBillregister(mbBill.getEgBillregister());
		    	mbCB.setMbHeader(mbObj);
		    	cancelBillService.persist(mbCB);
		    }
		}
				
		    }
		}
		//return SUCCESS;
	}	
	
	public Collection<StatutoryDeductionsForBill> getStatutoryDeductions() {
		return CollectionUtils.select(actionStatutorydetails, new Predicate(){
			public boolean evaluate(Object statutoryDeductionsForBill) {
				return ((StatutoryDeductionsForBill)statutoryDeductionsForBill)!=null;
			}});
	}
	
	public Collection<EgBilldetails> getCustomDeductionTypes() {
		return CollectionUtils.select(customDeductions, new Predicate(){
			public boolean evaluate(Object egBilldetails) {
				return ((EgBilldetails)egBilldetails)!=null;
			}});
	}
	
	public Collection<EgBilldetails> getRetentionMoneyTypes() {
		return CollectionUtils.select(retentionMoneyDeductions, new Predicate(){
			public boolean evaluate(Object egBilldetails) {
				return ((EgBilldetails)egBilldetails)!=null;
			}});
	}
	
	public Collection<AssetForBill> getAssestAndAccountDetails() {
		return CollectionUtils.select(accountDetailsForBill, new Predicate(){
			public boolean evaluate(Object assetForBill) {
				return ((AssetForBill)assetForBill)!=null;
			}});
	}
	
	public Collection<DeductionTypeForBill> getStandardDeductionTypes() {
		return CollectionUtils.select(standardDeductions, new Predicate(){
			public boolean evaluate(Object deductionTypeForBill) {
				return ((DeductionTypeForBill)deductionTypeForBill)!=null;
			}});
	}
	
	public Collection<EgBilldetails> getReleaseWHMoneyDetails() {
		return CollectionUtils.select(releaseWithHeldAmountDeductions, new Predicate(){
			public boolean evaluate(Object egBilldetails) {
				return ((EgBilldetails)egBilldetails)!=null;
			}});
	}
	
	public void validate(){		
		if(contractorBillRegister.getBilltype().equals("-1"))
			addFieldError("billType", getText("billType.not.found"));
		else if(contractorBillRegister.getBilltype().equals(contractorBillService.getBillType().get(1).toString()) && completionDate==null)
			addFieldError("completionDate", getText("completionDate.not.found"));
		if(StringUtils.isBlank(partyBillNumber))
			addFieldError("partyBillNumber", getText("partyBillNumber.not.found"));
		if(!worksService.checkBigDecimalValue(contractorBillRegister.getBillamount(),BigDecimal.valueOf(0.00)))
			addFieldError("billamount", getText("no.pending.bills"));
		if(partyBillDate==null)
			addFieldError("partyBillDate", getText("partyBillDate.not.found"));
		if(!DateUtils.compareDates(new Date(),partyBillDate))
			addFieldError("partyBillDate", getText("partyBillDate.greaterthan.currentDate"));
		if(!DateUtils.compareDates(contractorBillRegister.getBilldate(),completionDate))
			addFieldError("completionDate", getText("billdate.lessthan.completionDate"));
		if(!DateUtils.compareDates(contractorBillRegister.getBilldate(),workOrder.getWorkOrderDate()))
			addFieldError("workorderdate", getText("billdate.lessthan.workorderdate"));
		Date latestBillDate = measurementBookService.getLatestBillDateForMB(workOrderId);
		if(latestBillDate!=null && !DateUtils.compareDates(contractorBillRegister.getBilldate(),latestBillDate))
			addFieldError("billdate", getText("billdate.lessthan.olderbill"));
		if(worksService.checkBigDecimalValue(netPayableAmount,BigDecimal.valueOf(0.00))){
		if(Long.valueOf(0).equals(netPayableCode))
			addFieldError("netPayableCode", getText("netPayableCode.not.found"));
		}
		validateAssetDetails(); 
		validateStatutoryDeduction();
		validateStandardDeduction();
		validateCustomDeduction();
		String actionName = "";
		if (parameters.get(ACTION_NAME) != null && parameters.get(ACTION_NAME)[0] != null) 
			actionName = parameters.get(ACTION_NAME)[0];
		if(skipBudget && ((contractorBillRegister.getStatus()==null && !actionName.equalsIgnoreCase("save")) ||
				(contractorBillRegister.getStatus()!=null && (contractorBillRegister.getStatus().getCode().equalsIgnoreCase("NEW")|| contractorBillRegister.getStatus().getCode().equalsIgnoreCase("REJECTED")) && (!actionName.equalsIgnoreCase("save") && !actionName.equalsIgnoreCase("Cancel"))))){
			validateGlcodeBalForDepositWorks();
		}
		if(!checkForCOADuplicatesInAccDet())addFieldError("accountcode", getText("duplicate.accountcode"));
		if(!checkForCOADuplicatesInCustomDed())addFieldError("glcodeId", getText("duplicate.glcodeId"));
				
		// to re-populate the form data if any field error- only for edit mode
		if(id!=null && !getFieldErrors().isEmpty())
		{	try{
				edit();
			}catch(Exception e){
				logger.error(e.getMessage());
				
			}
		}
	}
	
	protected void validateStandardDeduction()
	{
		for(DeductionTypeForBill stanDetails:standardDeductions)
		{
			if(stanDetails!=null){
				if(stanDetails.getDeductionType()==null || stanDetails.getGlcodeid().equals(BigDecimal.valueOf(0))) 
					addFieldError("standardDeductions",getText("please.select.standardDeductions"));
				if(!worksService.checkBigDecimalValue(stanDetails.getCreditamount(),BigDecimal.valueOf(0.00))) {
					addFieldError("amount",getText(AMOUNT_ERROR));
				}
			}
		}
	}
	
	protected void validateCustomDeduction()
	{
		for(EgBilldetails customDetails:customDeductions)
		{
			if(customDetails!=null){
				if(customDetails.getGlcodeid().equals(BigDecimal.valueOf(0))) 
					addFieldError("customDeductions",getText("please.select.customDeductions"));
				if(!worksService.checkBigDecimalValue(customDetails.getCreditamount(),BigDecimal.valueOf(0.00)))
					addFieldError("customDeductions.amount",getText(AMOUNT_ERROR));
			}
		}
	}
	
	protected void validateRetentionDeduction()
	{
		for(EgBilldetails retentionMoney:retentionMoneyDeductions)
		{
			if(retentionMoney!=null){
				if(retentionMoney.getGlcodeid().equals(BigDecimal.valueOf(0))) 
					addFieldError("customDeductions",getText("please.select.retentionMoney"));
				if(!worksService.checkBigDecimalValue(retentionMoney.getCreditamount(),BigDecimal.valueOf(0.00)))
					addFieldError("retentionMoney.amount",getText(AMOUNT_ERROR));
			}
		}
	}
	
	protected void validateAssetDetails()
	{
		for(AssetForBill adb:accountDetailsForBill)
		{
			if(workOrderEstimate.getAssetValues().isEmpty()){
				if(adb!=null && (adb.getCoa()==null || adb.getCoa().getId()==0L)) 
					addFieldError("coa",getText("please.select.asset"));
			}
			else if(adb!=null && (adb.getAsset()==null || adb.getAsset().getId()==0L) 
					&& (adb.getCoa()==null || adb.getCoa().getId()==0L))
				addFieldError("asset",getText("please.select.asset"));
			if(adb!=null && !worksService.checkBigDecimalValue(adb.getAmount(),BigDecimal.valueOf(0.00)))
				addFieldError("asset.amount",getText(AMOUNT_ERROR));
		}
	}
	
	protected void validateStatutoryDeduction()
	{
		for(StatutoryDeductionsForBill adb:actionStatutorydetails)
		{
			if(adb!=null){
				if(adb.getEgBillPayeeDtls().getRecovery()==null || adb.getEgBillPayeeDtls().getRecovery().getId()==0) 
					addFieldError("actionStatutorydetails",getText("please.select.actionStatutorydetails"));
				if(!worksService.checkBigDecimalValue(adb.getEgBillPayeeDtls().getCreditAmount(),BigDecimal.valueOf(0.00)))
					addFieldError("actionStatutorydetails.amount",getText(AMOUNT_ERROR));
			}
		}
	}
	
	public boolean checkForCOADuplicatesInAccDet() {
		if(!getAssestAndAccountDetails().isEmpty() && workOrderEstimate.getAssetValues().isEmpty())
		{
			Set<Long> coaSet= new HashSet<Long>();
			for(AssetForBill assetForBill:getAssestAndAccountDetails())
			{
				if(assetForBill.getCoa()!=null && assetForBill.getCoa().getId()>0L)
				{
					if(coaSet.contains(assetForBill.getCoa().getId())){
						return false;
					}
					coaSet.add(assetForBill.getCoa().getId());
				}
			}
		}
		return true;
	}
	
	public boolean checkForCOADuplicatesInCustomDed() {
		if(!getCustomDeductionTypes().isEmpty())
		{
			Set<BigDecimal> coaSet= new HashSet<BigDecimal>();
			for(EgBilldetails egBilldetails:getCustomDeductionTypes())
			{
				if(egBilldetails.getGlcodeid()!=null && worksService.
						checkBigDecimalValue(egBilldetails.getGlcodeid(),BigDecimal.valueOf(0)))
				{
					if(coaSet.contains(egBilldetails.getGlcodeid())){
						return false;
					}
					coaSet.add(egBilldetails.getGlcodeid());
				}
			}
		}
		return true;
	}
	    
	public ContractorBillRegister setEgBillRegister() throws Exception
	{
		if(contractorBillRegister.getBillnumber()==null)
		{
			contractorBillRegister.setBillnumber(contractorBillService.generateContractorBillNumber(contractorBillRegister, workOrder, workOrderEstimate));
			contractorBillRegister.setContractCertificateNumber(contractorBillService.generateContractCertificateNumber(contractorBillRegister, workOrderEstimate));
			addActionMessage("Contract Certificate Number is - "+contractorBillRegister.getContractCertificateNumber());
		}
		if(id==null)
			contractorBillRegister.setBillstatus("NEW");
		if(isRebatePremLevelBill.equals("yes") && tenderResponse.getBidType().name().equals(getPercTenderType())){
			contractorBillRegister.setPassedamount(grossAmount);
			contractorBillRegister.setBillamount(grossAmount); 
		}
		else
			contractorBillRegister.setPassedamount(contractorBillRegister.getBillamount());
		contractorBillRegister.setExpendituretype(EXPENDITURE_TYPE);
		contractorBillRegister.setWorkordernumber(workOrder.getWorkOrderNumber());
		contractorBillRegister.setWorkorderdate(workOrder.getWorkOrderDate());
		if(advaceAdjustmentCreditAmount.intValue()>0)
			contractorBillRegister.setAdvanceadjusted(advaceAdjustmentCreditAmount);
		contractorBillRegister.setStatus(commonsService.getStatusByModuleAndCode(BILL_MODULE_KEY, BILL_STATUS));
		List<FinancialDetail> fdList = financialDetailService.findAllByNamedQuery("getFinancialDetailByEstimateId",workOrderEstimate.getEstimate().getId());	
		contractorBillRegister.setEgBillregistermis(setEgBillregistermis(fdList));
		addAccountDetails(fdList);
		return contractorBillRegister;
	}

	private void addAccountDetails(List<FinancialDetail> fdList)
			throws Exception {
		Map<String,BigDecimal> debitAmountMap = getGlcodesForDebit();
		for(Map.Entry<String, BigDecimal> entry : debitAmountMap.entrySet())
			contractorBillRegister.getEgBilldetailes().add(getBillDetailsRegister(contractorBillRegister,fdList,entry.getKey(),entry.getValue(),true,false));
		Map<String,BigDecimal> creditAmountMap = getGlcodesForstanDed();
		for(Map.Entry<String, BigDecimal> entry : creditAmountMap.entrySet())
			contractorBillRegister.getEgBilldetailes().add(getBillDetailsRegister(contractorBillRegister,fdList,entry.getKey(),entry.getValue(),false,false));
		for(EgBilldetails rbillDetails:getReleaseWithHeldAmountDeductions())
		{
			if(rbillDetails!=null && worksService.
					checkBigDecimalValue(rbillDetails.getGlcodeid(),BigDecimal.valueOf(0)) && worksService.
					checkBigDecimalValue(rbillDetails.getDebitamount(),BigDecimal.valueOf(0)))
			{
				EgBilldetails egbillDetails = getBillDetailsRegister(contractorBillRegister,fdList,rbillDetails.getGlcodeid().toString(),
						rbillDetails.getDebitamount(),true,false);
				contractorBillRegister.getEgBilldetailes().add(egbillDetails);
			}
		}
		for(EgBilldetails rbillDetails:getRetentionMoneyTypes())
		{
			if(rbillDetails!=null && worksService.
					checkBigDecimalValue(rbillDetails.getGlcodeid(),BigDecimal.valueOf(0)))
			{
				EgBilldetails egbillDetails = getBillDetailsRegister(contractorBillRegister,fdList,rbillDetails.getGlcodeid().toString(),
						rbillDetails.getCreditamount(),false,false);
				egbillDetails.setNarration(rbillDetails.getNarration());
				contractorBillRegister.getEgBilldetailes().add(egbillDetails);
			}
		}
		for(EgBilldetails billDetails:customDeductions)
		{
			if(billDetails!=null && worksService.
					checkBigDecimalValue(billDetails.getGlcodeid(),BigDecimal.valueOf(0)))
			{
				EgBilldetails egbillDetails = getBillDetailsRegister(contractorBillRegister,fdList,billDetails.getGlcodeid().toString(),
						billDetails.getCreditamount(),false,false);
				egbillDetails.setNarration(billDetails.getNarration());
				contractorBillRegister.getEgBilldetailes().add(egbillDetails);
			}
		}
		List<AppConfigValues> appConfigValuesList=worksService.getAppConfigValue("EGF","CONTRACTOR_ADVANCE_CODE");
		if(advaceAdjustmentCreditAmount!=null && advaceAdjustmentCreditAmount.intValue()>0 
				&& appConfigValuesList!=null && !appConfigValuesList.isEmpty() && 
				appConfigValuesList.get(0).getValue()!=null)
		{
			CChartOfAccounts coa = commonsService.getCChartOfAccountsByGlCode(appConfigValuesList.get(0).getValue());
			if(coa!=null && coa.getId()!=null){
				contractorBillRegister.getEgBilldetailes().add(getBillDetailsRegister(contractorBillRegister,fdList,coa.getId().toString(),
						advaceAdjustmentCreditAmount,false,false));
			}
		}
		Map<String,BigDecimal> creditAmountStatMap = getGlcodesForStatDed();
		for(Map.Entry<String, BigDecimal> entry : creditAmountStatMap.entrySet())
			contractorBillRegister.getEgBilldetailes().add(getBillDetailsRegister(contractorBillRegister,fdList,entry.getKey(),entry.getValue(),false,true));
		// Validations to ensure that total work value is always greater that sum of all deductions entered -- This check is important in the case of retention money refund withheld release
		double deductionTotal= 0d;
		for(EgBilldetails egBilldetails :contractorBillRegister.getEgBilldetailes())
		{
			if(egBilldetails!=null && egBilldetails.getCreditamount()!=null)
			{
				deductionTotal= deductionTotal+egBilldetails.getCreditamount().doubleValue();
			}
		}
		if(contractorBillRegister.getWorkRecordedAmount()!=null && deductionTotal>contractorBillRegister.getWorkRecordedAmount().doubleValue())
		{
			List<ValidationError> errors=new ArrayList<ValidationError>();
			errors.add(new ValidationError("contractorBill.deductionsGreaterThanWorkValue",getText("contractorBill.deductionsGreaterThanWorkValue")));
			clearMessages();
			throw new ValidationException(errors);
		}
		if(worksService.checkBigDecimalValue(netPayableAmount,BigDecimal.valueOf(0.00))){
			contractorBillRegister.getEgBilldetailes().add(getBillDetailsRegister(contractorBillRegister,fdList,getNetPayableCode().toString(),getNetPayableAmount(),false,false));
		}
		
	}

	private EgBilldetails getBillDetailsRegister(ContractorBillRegister billregister,List<FinancialDetail> fdList,String glcode,
			BigDecimal amount,boolean isDebit,boolean isTds) throws Exception {
		EgBilldetails billDetails = new EgBilldetails();
		if(fdList!=null && !fdList.isEmpty() && fdList.get(0).getFunction()!=null && fdList.get(0).getFunction().getId()!=null){
			CFunction fun = commonsService.getFunctionById(fdList.get(0).getFunction().getId());
			billDetails.setFunctionid(BigDecimal.valueOf(fun.getId()));
		}
		CChartOfAccounts coa=null;
		if(StringUtils.isNotBlank(glcode) && Long.parseLong(glcode)>0)
			coa = commonsService.getCChartOfAccountsById(Long.valueOf(glcode));
		if(coa!=null && coa.getId()!=null)
			billDetails.setGlcodeid(BigDecimal.valueOf(coa.getId()));
		if(isDebit)
			billDetails.setDebitamount(amount);
		else
			billDetails.setCreditamount(amount);
		billDetails.setEgBillregister(billregister);
		if(coa!=null && coa.getGlcode()!=null){
			List<Accountdetailtype> detailCode = commonsService.getAccountdetailtypeListByGLCode(coa.getGlcode());			
			if (detailCode!=null && !detailCode.isEmpty()) {	
				Accountdetailtype adt1 = null;
				Accountdetailtype adt2 = null;
				Accountdetailtype adt3 = null;
				if(isDebit) {
					if(skipBudget) {
						adt2 = commonsService.getAccountDetailTypeIdByName(coa.getGlcode(), ACCOUNTDETAIL_TYPE_DEPOSITCODE);
						if(adt2!=null){
							if(isTds)
								addTdsDetails(amount, isDebit, isTds, billDetails, adt2, Integer.valueOf(workOrderEstimate.getEstimate().getDepositCode().getId().toString()));
							else
								billDetails.getEgBillPaydetailes().add(getEgPayeeDetails(billDetails,adt2.getId(),amount,isDebit,false,null,null,Integer.valueOf(workOrderEstimate.getEstimate().getDepositCode().getId().toString())));	
						}
					}
					else {
						adt3 = commonsService.getAccountDetailTypeIdByName(coa.getGlcode(), ACCOUNTDETAIL_TYPE_PROJECTCODE);
						if(adt3!=null){
							if(isTds)
								addTdsDetails(amount, isDebit, isTds, billDetails, adt3, Integer.valueOf(workOrderEstimate.getEstimate().getProjectCode().getId().toString()));
							else
								billDetails.getEgBillPaydetailes().add(getEgPayeeDetails(billDetails,adt3.getId(),amount,isDebit,false,null,null,Integer.valueOf(workOrderEstimate.getEstimate().getProjectCode().getId().toString())));	
						}
						if(adt3==null && isAccountRetentionMoneyAccountType(coa)){
							adt1 = commonsService.getAccountDetailTypeIdByName(coa.getGlcode(), ACCOUNTDETAIL_TYPE_CONTRACTOR);
							if(adt1!=null){
								if(isTds)
									addTdsDetails(amount, isDebit, isTds, billDetails, adt1, Integer.valueOf(workOrder.getContractor().getId().toString()));
								else
									billDetails.getEgBillPaydetailes().add(getEgPayeeDetails(billDetails,adt1.getId(),amount,isDebit,false,null,null, Integer.valueOf(workOrder.getContractor().getId().toString())));	
					}
				}
					}
				}
				else {
					adt1 = commonsService.getAccountDetailTypeIdByName(coa.getGlcode(), ACCOUNTDETAIL_TYPE_CONTRACTOR);
					if(adt1!=null){
						if(isTds)
							addTdsDetails(amount, isDebit, isTds, billDetails, adt1, Integer.valueOf(workOrder.getContractor().getId().toString()));
						else
							billDetails.getEgBillPaydetailes().add(getEgPayeeDetails(billDetails,adt1.getId(),amount,isDebit,false,null,null, Integer.valueOf(workOrder.getContractor().getId().toString())));	
					}
					
				}
				if(adt1==null && adt2==null && adt3==null) {
					List<ValidationError> errors=new ArrayList<ValidationError>();
					errors.add(new ValidationError("contractorBill.validate_glcode_for_subledger",getText("contractorBill.validate_glcode_for_subledger",new String[]{coa.getGlcode()}))); 
					throw new ValidationException(errors);					
				}
			}
		}
		return billDetails;
	}

	private void addTdsDetails(BigDecimal amount, boolean isDebit,
			boolean isTds, EgBilldetails billDetails, Accountdetailtype adt, Integer adkId) {
		for(StatutoryDeductionsForBill payeeDetails:getActionStatutorydetails())
		{
			if(payeeDetails!=null && payeeDetails.getEgBillPayeeDtls().getRecovery()!=null && payeeDetails.getEgBillPayeeDtls().getRecovery().getId()!=null &&
					payeeDetails.getEgBillPayeeDtls().getRecovery().getChartofaccounts()!=null && 
					payeeDetails.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId()!=null && payeeDetails.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId().
					equals(Long.valueOf(billDetails.getGlcodeid().toString())))
			{
					// Added for card 1078 : Automatic calculation of statutory recoveries in bill 
				   	EgBillPayeedetails egPayeeDtls = getEgPayeeDetails(billDetails,adt.getId(),amount,isDebit,isTds,payeeDetails.getEgBillPayeeDtls().getRecovery().getId(),payeeDetails.getEgBillPayeeDtls().getNarration(),adkId);
					billDetails.getEgBillPaydetailes().add(egPayeeDtls);
					addStatutoryDeductionsForBill(payeeDetails,egPayeeDtls);
			}
			
		}
	}
	
	private void addStatutoryDeductionsForBill(StatutoryDeductionsForBill statutoryDeductionBill,EgBillPayeedetails egPayeeDtls){
		StatutoryDeductionsForBill sdb=new StatutoryDeductionsForBill();
		if(statutoryDeductionBill.getSubPartyType().getId()!=null)
			sdb.setSubPartyType((EgPartytype)persistenceService.find("from EgPartytype where id=?", statutoryDeductionBill.getSubPartyType().getId()));
		if(statutoryDeductionBill.getTypeOfWork().getId()!=null)
			sdb.setTypeOfWork((EgwTypeOfWork)persistenceService.find("from EgwTypeOfWork where id=?", statutoryDeductionBill.getTypeOfWork().getId()));
		sdb.setEgBillPayeeDtls(egPayeeDtls);
		sdb.setEgBillReg(contractorBillRegister);
		contractorBillRegister.addStatutoryDeductions(sdb); 
	}

	private EgBillPayeedetails getEgPayeeDetails(EgBilldetails billDetails,Integer adtId,
		BigDecimal amount,boolean isDebit,boolean isTds,Long tdsId,String narration, Integer adkId) {
		EgBillPayeedetails egBillPaydetail = new EgBillPayeedetails();
		egBillPaydetail.setAccountDetailKeyId(adkId);
		egBillPaydetail.setAccountDetailTypeId(adtId);
		if(isDebit)
			egBillPaydetail.setDebitAmount(amount);
		else
			egBillPaydetail.setCreditAmount(amount);
		if(isTds)
			egBillPaydetail.setRecovery(recoveryService.getTdsById(tdsId));
		if(narration!=null)
			egBillPaydetail.setNarration(narration);
		egBillPaydetail.setEgBilldetailsId(billDetails);
		return egBillPaydetail;
	}
	
	private EgBillregistermis setEgBillregistermis(List<FinancialDetail> fdList) throws EGOVException
	{
		EgBillregistermis egBillRegisterMis=null;
		if(id==null)
			egBillRegisterMis = new EgBillregistermis();
		else
			egBillRegisterMis = contractorBillRegister.getEgBillregistermis();
		egBillRegisterMis.setEgBillregister(contractorBillRegister);
		egBillRegisterMis.setPayto(workOrder.getContractor().getName());
		egBillRegisterMis.setFieldid((BoundaryImpl)workOrderEstimate.getEstimate().getWard());
		egBillRegisterMis.setPartyBillDate(partyBillDate);
		egBillRegisterMis.setPartyBillNumber(partyBillNumber);		
		if(fdList!=null && !fdList.isEmpty()){
			if(fdList.get(0).getFund()!=null && fdList.get(0).getFund().getId()!=null)
				egBillRegisterMis.setFund(commonsService.fundById(fdList.get(0).getFund().getId()));
			if(fdList.get(0).getFunctionary()!=null && fdList.get(0).getFunctionary().getId()!=null)
				egBillRegisterMis.setFunctionaryid(commonsService.getFunctionaryById(fdList.get(0).getFunctionary().getId().intValue()));
			if(fdList.get(0).getScheme()!=null && fdList.get(0).getScheme().getId()!=null)
				egBillRegisterMis.setScheme(commonsService.getSchemeById(fdList.get(0).getScheme().getId()));
			if(fdList.get(0).getSubScheme()!=null && fdList.get(0).getSubScheme().getId()!=null)
				egBillRegisterMis.setSubScheme(commonsService.getSubSchemeById(fdList.get(0).getSubScheme().getId()));
			if(!fdList.get(0).getFinancingSources().isEmpty())
				egBillRegisterMis.setFundsource(fdList.get(0).getFinancingSources().get(0).getFundSource());
		}
		egBillRegisterMis.setEgDepartment((DepartmentImpl)workOrderEstimate.getEstimate().getExecutingDepartment());
		egBillRegisterMis.setFinancialyear(commonsService.getFinancialYearByDate(contractorBillRegister.getBilldate()));
		return egBillRegisterMis;
	}
	
	public Map<String,BigDecimal> getGlcodesForDebit(){
		Map<String,BigDecimal> debitGlcodeAndAmountMap = new HashMap<String, BigDecimal>();
		Set<Long> coaSet = new HashSet<Long>();
		for(AssetForBill assetBill:accountDetailsForBill)
		{
			if(assetBill!=null && assetBill.getCoa()!=null && assetBill.getCoa().getId()!=null){
				if(coaSet.contains(assetBill.getCoa().getId()))
				{
					if(debitGlcodeAndAmountMap.containsKey(assetBill.getCoa().getId().toString()))
					{
						BigDecimal amount = debitGlcodeAndAmountMap.get(assetBill.getCoa().getId().toString());
						amount= amount.add(assetBill.getAmount());
						debitGlcodeAndAmountMap.put(assetBill.getCoa().getId().toString(),amount);
					}
				}
				else{
					debitGlcodeAndAmountMap.put(assetBill.getCoa().getId().toString(),assetBill.getAmount());
					coaSet.add(assetBill.getCoa().getId());
				}
				addAssetForBill(assetBill);
			}
		}
		return debitGlcodeAndAmountMap;
	}

	private void addAssetForBill(AssetForBill adb) {
		if(!workOrderEstimate.getAssetValues().isEmpty() && adb.getAsset()!=null)
		{
			AssetForBill assetForBill = new AssetForBill();
			Asset assetNew = commonAssetsService.getAssetById(adb.getAsset().getId());
			String value = worksService.getWorksConfigValue("WORKS_ASSET_STATUS");
			if(StringUtils.isBlank(value))
			{
				List<ValidationError> errors=new ArrayList<ValidationError>();
				errors.add(new ValidationError("asset.status","asset.status.configvalue")); 
				throw new ValidationException(errors);
			}
			if(assetNew.getStatus().getDescription().equals(value.split(",")[0]))
			{
				EgwStatus status = commonsService.getStatusByModuleAndCode("ASSET", value.split(",")[1]);
				assetNew.setStatus(status);    
			}
			assetForBill.setEgbill(contractorBillRegister);
			assetForBill.setCoa(commonsService.getCChartOfAccountsById(adb.getCoa().getId()));
			assetForBill.setWorkOrderEstimate(workOrderEstimate);
			assetForBill.setAmount(adb.getAmount());
			assetForBill.setDescription(adb.getDescription());
			assetForBill.setAsset(assetNew); 
			contractorBillRegister.addAssetDetails(assetForBill);
		}
	}
	
	public Map<String,BigDecimal> getGlcodesForstanDed(){
		Map<String,BigDecimal> debitGlcodeAndAmountMap = new HashMap<String, BigDecimal>();
		Set<BigDecimal> coaSet = new HashSet<BigDecimal>();
		for(DeductionTypeForBill deductionBill:standardDeductions)
		{
			if(deductionBill!=null && deductionBill.getGlcodeid()!=null)
			{
				if(coaSet.contains(deductionBill.getGlcodeid()))
				{
					if(debitGlcodeAndAmountMap.containsKey(deductionBill.getGlcodeid().toString()))
					{
						BigDecimal amount = debitGlcodeAndAmountMap.get(deductionBill.getGlcodeid().toString());
						amount= amount.add(deductionBill.getCreditamount());
						debitGlcodeAndAmountMap.put(deductionBill.getGlcodeid().toString(),amount);
					}
				}
				else{
					debitGlcodeAndAmountMap.put(deductionBill.getGlcodeid().toString(),deductionBill.getCreditamount());
					coaSet.add(deductionBill.getGlcodeid());
				}
					addDeductionTypeBill(deductionBill);
			}
		}
		return debitGlcodeAndAmountMap;
	}

	private void addDeductionTypeBill(DeductionTypeForBill deductionBill) {
		DeductionTypeForBill deductionTypeForBill = new DeductionTypeForBill();
		deductionTypeForBill.setEgbill(contractorBillRegister);
		deductionTypeForBill.setWorkOrder(workOrder);
		deductionTypeForBill.setNarration(deductionBill.getNarration());
		deductionTypeForBill.setCoa(commonsService.getCChartOfAccountsById(Long.valueOf(deductionBill.getGlcodeid().toString())));
		deductionTypeForBill.setCreditamount(deductionBill.getCreditamount());
		deductionTypeForBill.setDeductionType(deductionBill.getDeductionType());
		contractorBillRegister.addDeductionType(deductionTypeForBill); 
	}
	
	public Map<String,BigDecimal> getGlcodesForStatDed(){
		Map<String,BigDecimal> debitGlcodeAndAmountMap = new HashMap<String, BigDecimal>();
		Set<Long> coaSet = new HashSet<Long>();
		for(StatutoryDeductionsForBill bpd:getStatutoryDeductions())
		{
			if(bpd!=null && bpd.getEgBillPayeeDtls().getRecovery()!=null && bpd.getEgBillPayeeDtls().getRecovery().getId()!=null &&
					bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts()!=null && bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId()!=null)
			{
				if(coaSet.contains(bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId()))
				{
					if(debitGlcodeAndAmountMap.containsKey(bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId().toString()))
					{
						BigDecimal amount = debitGlcodeAndAmountMap.get(bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId().toString());
						amount= amount.add(bpd.getEgBillPayeeDtls().getCreditAmount());
						debitGlcodeAndAmountMap.put(bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId().toString(),amount);
					}
				}
				else{
					debitGlcodeAndAmountMap.put(bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId().toString(),bpd.getEgBillPayeeDtls().getCreditAmount());
					coaSet.add(bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId());
				}
			}
		}
		return debitGlcodeAndAmountMap;
	}
	
	public void populateOtherDeductionList() throws EGOVException{
		char[] charTypes = getBillAccountTypesFromConfig();
		if(charTypes!=null){
			for(int i=0;i<charTypes.length;i++){
				List<CChartOfAccounts> pList = commonsService.getActiveAccountsForType(charTypes[i]);
				if(pList!=null)
					customDeductionAccountList.addAll(pList);
			}
		}
		if(!contractorBillService.getStandardDeductionsFromConfig().isEmpty())
			standardDeductionConfValues = new LinkedList<String>(contractorBillService.getStandardDeductionsFromConfig().keySet());
		
		if(StringUtils.isNotBlank(worksService.getWorksConfigValue(RETENTION_MONEY_PURPOSE))){
			retentionMoneyAccountList = commonsService.getAccountCodeByPurpose(Integer.valueOf(worksService.getWorksConfigValue(RETENTION_MONEY_PURPOSE)));
		}
	}
	
	private char[] getBillAccountTypesFromConfig(){
		String strTypes = worksService.getWorksConfigValue("CUSTOM_DEDUCTION");
		char[] charArr = null;
		if(StringUtils.isNotBlank(strTypes)){
		String[] strArrayTypes = strTypes.split(",");
		charArr = new char[strArrayTypes.length];
		for(int i=0;i<strArrayTypes.length;i++)
			charArr[i] = strArrayTypes[i].charAt(0);
		}
		return charArr;
	}

	public void setBudgetDetails(Long workOrderId,Long workOrderEstimateId,Date billDate)     
	{
		billAmount = contractorBillService.getApprovedMBAmount(workOrderId, workOrderEstimateId, billDate);
		//utilizedAmount = contractorBillService.getTotalUtilizedAmount(workOrderId, billDate);
		//sanctionedBudget = contractorBillService.getBudgetedAmtForYear(workOrderId, billDate);
	}
	
	public Map<Long, String> getContratorCoaPayableMap() throws NumberFormatException, EGOVException {
		if(StringUtils.isNotBlank(worksService.getWorksConfigValue(WORKS_NETPAYABLE_CODE))){
			List<CChartOfAccounts> coaPayableList = commonsService.getAccountCodeByPurpose(Integer.valueOf(worksService.getWorksConfigValue(WORKS_NETPAYABLE_CODE)));
			for(CChartOfAccounts coa :coaPayableList)
				contratorCoaPayableMap.put(coa.getId(), coa.getGlcode()+"-"+coa.getName());
		}
		return contratorCoaPayableMap;
	}

	public void setContratorCoaPayableMap(Map<Long, String> contratorCoaPayableMap) {
		this.contratorCoaPayableMap = contratorCoaPayableMap;
	}

	public String getDisp() {
		return disp;
	}

	public void setDisp(String disp) {
		this.disp = disp;
	}
	
	public List<AssetForBill> getAccountDetailsForBill() {
		return accountDetailsForBill;
	}

	public void setAccountDetailsForBill(
			List<AssetForBill> accountDetailsForBill) {
		this.accountDetailsForBill = accountDetailsForBill;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		
		this.id = id;
	}

	public Long getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(Long workOrderId) {
		this.workOrderId = workOrderId;
	}

	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}

	public void setContractorBillService(ContractorBillService contractorBillService) {
		this.contractorBillService = contractorBillService;
	}
	
	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}
	
	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}
	
	public List<CChartOfAccounts> getStandardDeductionAccountList() {
		return standardDeductionAccountList;
	}

	public List<CChartOfAccounts> getCustomDeductionAccountList() {
		return customDeductionAccountList;
	}

	public List<String> getStandardDeductionConfValues() {
		return standardDeductionConfValues;
	}
	
	public List<StatutoryDeductionsForBill> getActionStatutorydetails() {
		return actionStatutorydetails;
	}

	
	public void setActionStatutorydetails(
			List<StatutoryDeductionsForBill> actionStatutorydetails) {
		this.actionStatutorydetails = actionStatutorydetails;
	}

	public List<DeductionTypeForBill> getStandardDeductions() {
		return standardDeductions; 
	}

	public void setStandardDeductions(List<DeductionTypeForBill> standardDeductions) {
		this.standardDeductions = standardDeductions;
	}

	public List<EgBilldetails> getCustomDeductions() {
		return customDeductions;
	}

	public void setCustomDeductions(List<EgBilldetails> customDeductions) {
		this.customDeductions = customDeductions;
	}
	
	public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}

	public BigDecimal getUtilizedAmount() {
		return utilizedAmount;
	}

	public void setUtilizedAmount(BigDecimal utilizedAmount) {
		this.utilizedAmount = utilizedAmount;
	}

	public BigDecimal getSanctionedBudget() {
		return sanctionedBudget;
	}

	public void setSanctionedBudget(BigDecimal sanctionedBudget) {
		this.sanctionedBudget = sanctionedBudget;
	}
	
	public void setFinancialDetailService(
			PersistenceService<FinancialDetail, Long> financialDetailService) {
		this.financialDetailService = financialDetailService;
	}
	
	public Long getNetPayableCode() {
		return netPayableCode;
	}

	public void setNetPayableCode(Long netPayableCode) {
		this.netPayableCode = netPayableCode;
	}

	public BigDecimal getNetPayableAmount() {
		return netPayableAmount;
	}

	public void setNetPayableAmount(BigDecimal netPayableAmount) {
		this.netPayableAmount = netPayableAmount;
	}

	public BigDecimal getAdvaceAdjustmentCreditAmount() {
		return advaceAdjustmentCreditAmount;
	}

	public void setAdvaceAdjustmentCreditAmount(
			BigDecimal advaceAdjustmentCreditAmount) {
		this.advaceAdjustmentCreditAmount = advaceAdjustmentCreditAmount;
	}

	public String getPartyBillNumber() {
		return partyBillNumber;
	}

	public void setPartyBillNumber(String partyBillNumber) {
		this.partyBillNumber = partyBillNumber;
	}

	public Date getPartyBillDate() {
		return partyBillDate;
	}

	public void setPartyBillDate(Date partyBillDate) {
		this.partyBillDate = partyBillDate;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public void setCommonAssetsService(CommonAssetsService commonAssetsService) {
		this.commonAssetsService = commonAssetsService;
	}

	public ContractorBillRegister getContractorBillRegister() {
		return contractorBillRegister;
	}

	public void setContractorBillRegister(
			ContractorBillRegister contractorBillRegister) {
		this.contractorBillRegister = contractorBillRegister;
	}

	public void setMeasurementBookService(
			MeasurementBookServiceImpl measurementBookService) {
		this.measurementBookService = measurementBookService;
	}

	public Integer getPartbillNo() {
		return partbillNo;
	}

	public void setPartbillNo(Integer partbillNo) {
		this.partbillNo = partbillNo;
	}

	public void setStandardDeductionAccountList(
			List<CChartOfAccounts> standardDeductionAccountList) {
		this.standardDeductionAccountList = standardDeductionAccountList;
	}

	public void setCustomDeductionAccountList(
			List<CChartOfAccounts> customDeductionAccountList) {
		this.customDeductionAccountList = customDeductionAccountList;
	}

	public BigDecimal getTotalPendingBalance() {
		return totalPendingBalance;
	}

	public void setTotalPendingBalance(BigDecimal totalPendingBalance) {
		this.totalPendingBalance = totalPendingBalance;
	}

	public String getSourcepage() {
		return sourcepage;
	}

	public void setSourcepage(String sourcepage) {
		this.sourcepage = sourcepage;
	}
	
	public List<MBHeader> getMbHeaderList() {
		return mbHeaderList;
	}

	public void setMbHeaderList(List<MBHeader> mbHeaderList) {
		this.mbHeaderList = mbHeaderList;
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

	public void setChecklistService(
			PersistenceService<EgChecklists, Long> checklistService) {
		this.checklistService = checklistService;
	}

	public List<AppConfigValues> getFinalBillChecklist() {
		return finalBillChecklist;
	}

	public void setFinalBillChecklist(List<AppConfigValues> finalBillChecklist) {
		this.finalBillChecklist = finalBillChecklist;
	}

	
	public List<String> getChecklistValues() {
		return checklistValues;
	}

	public void setChecklistValues(List<String> checklistValues) {
		this.checklistValues = checklistValues;
	}

	public RecoveryService getRecoveryService() {
		return recoveryService;
	}

	public void setRecoveryService(RecoveryService recoveryService) {
		this.recoveryService = recoveryService;
	}
	
	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

/*	public List<org.egov.infstr.workflow.Action> getValidActions(){
		return workflowService.getValidActions(contractorBillRegister);
	}	
*/	
	public void setContractorBillWorkflowService(WorkflowService<ContractorBillRegister> workflow) {
		this.workflowService = workflow;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
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

	public Long getWorkOrderEstimateId() {
		return workOrderEstimateId;
	}

	public void setWorkOrderEstimateId(Long workOrderEstimateId) {
		this.workOrderEstimateId = workOrderEstimateId;
	}

	public WorkOrderEstimate getWorkOrderEstimate() {
		return workOrderEstimate;
	}

	public void setWorkOrderEstimate(WorkOrderEstimate workOrderEstimate) {
		this.workOrderEstimate = workOrderEstimate;
	}
	
		public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	
	public InputStream getCompletionCertificatePDF() {
		return completionCertificatePDF;
	}

	public void setCompletionCertificatePDF(InputStream completionCertificatePDF) {
		this.completionCertificatePDF = completionCertificatePDF;
	}

	public void setVoucherService(VoucherService voucherService) {
		this.voucherService = voucherService;
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public WorkCompletionInfo getCompletionInfo() {
		return completionInfo;
	}

	public void setCompletionInfo(WorkCompletionInfo completionInfo) {
		this.completionInfo = completionInfo;
	}

	public List<WorkCompletionDetailInfo> getCompletionDetailInfoList() {
		return completionDetailInfoList;
	}

	public void setCompletionDetailInfoList(
			List<WorkCompletionDetailInfo> completionDetailInfoList) {
		this.completionDetailInfoList = completionDetailInfoList;
	}

	public String[] getRemarks() {
		return remarks;
	}

	public void setRemarks(String[] remarks) {
		this.remarks = remarks;
	}

	public boolean getSkipBudget() {
		return skipBudget;
	}

	public void setSkipBudget(boolean skipBudget) {
		this.skipBudget = skipBudget;
	}

	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}

	public List<CChartOfAccounts> getRetentionMoneyAccountList() {
		return retentionMoneyAccountList;
	}

	public void setRetentionMoneyAccountList(
			List<CChartOfAccounts> retentionMoneyAccountList) {
		this.retentionMoneyAccountList = retentionMoneyAccountList;
	}

	public List<EgBilldetails> getRetentionMoneyDeductions() {
		return retentionMoneyDeductions;
	}

	public void setRetentionMoneyDeductions(List<EgBilldetails> retentionMoneyDeductions) {
		this.retentionMoneyDeductions = retentionMoneyDeductions;
	}

	public String getIsRetMoneyAutoCalculated() {
		return isRetMoneyAutoCalculated;
	}

	public void setIsRetMoneyAutoCalculated(String isRetMoneyAutoCalculated) {
		this.isRetMoneyAutoCalculated = isRetMoneyAutoCalculated;
	}

	public String getIsRetMoneyEditable() {
		return isRetMoneyEditable;
	}

	public void setIsRetMoneyEditable(String isRetMoneyEditable) {
		this.isRetMoneyEditable = isRetMoneyEditable;
	}

	public String getPercDeductForRetMoneyPartBill() {
		return percDeductForRetMoneyPartBill;
	}

	public void setPercDeductForRetMoneyPartBill(
			String percDeductForRetMoneyPartBill) {
		this.percDeductForRetMoneyPartBill = percDeductForRetMoneyPartBill;
	}

	public String getPercDeductForRetMoneyFinalBill() {
		return percDeductForRetMoneyFinalBill;
	}

	public void setPercDeductForRetMoneyFinalBill(
			String percDeductForRetMoneyFinalBill) {
		this.percDeductForRetMoneyFinalBill = percDeductForRetMoneyFinalBill;
	}

	public String getRetMoneyFinalBillPerOnValue() {
		return retMoneyFinalBillPerOnValue;
	}

	public void setRetMoneyFinalBillPerOnValue(String retMoneyFinalBillPerOnValue) {
		this.retMoneyFinalBillPerOnValue = retMoneyFinalBillPerOnValue;
	}
	
/*	public TenderResponse getTenderResponse() {
		return tenderResponse;
	}

	public void setTenderResponse(TenderResponse tenderResponse) {
		this.tenderResponse = tenderResponse;
	}
*/
	public String getRebatePremLevel() {
		return rebatePremLevel;
	}

	public void setRebatePremLevel(String rebatePremLevel) {
		this.rebatePremLevel = rebatePremLevel;
	}
	

	public List<EgPartytype> getSubPartyTypeDtls() {
		return subPartyTypeDtls;
	}

	public void setSubPartyTypeDtls(List<EgPartytype> subPartyTypeDtls) {
		this.subPartyTypeDtls = subPartyTypeDtls;
	}

	public List<EgwTypeOfWork> getTypeOfWorkDtls() {
		return typeOfWorkDtls;
	}

	public void setTypeOfWorkDtls(List<EgwTypeOfWork> typeOfWorkDtls) {
		this.typeOfWorkDtls = typeOfWorkDtls;
	}

	public String getShowSubPartyType() {
		return showSubPartyType;
	}

	public void setShowSubPartyType(String showSubPartyType) {
		this.showSubPartyType = showSubPartyType;
	}

	public String getShowTypeOfWork() {
		return showTypeOfWork;
	}

	public void setShowTypeOfWork(String showTypeOfWork) {
		this.showTypeOfWork = showTypeOfWork;
	}

	public String getEditStatutoryAmount() {
		return editStatutoryAmount;
	}

	public void setEditStatutoryAmount(String editStatutoryAmount) {
		this.editStatutoryAmount = editStatutoryAmount;
	}

	public Long getEstimateId() {
		return estimateId;
	}

	public void setEstimateId(Long estimateId) {
		this.estimateId = estimateId;
	}

	public String getPercTenderType() {
		return percTenderType;
	}

	public void setPercTenderType(String percTenderType) {
		this.percTenderType = percTenderType;
	}

	public String getIsRebatePremLevelBill() {
		return isRebatePremLevelBill;
	}

	public void setIsRebatePremLevelBill(String isRebatePremLevelBill) {
		this.isRebatePremLevelBill = isRebatePremLevelBill;
	}

	public void setMbBillService(PersistenceService<MBBill, Long> mbBillService) {
		this.mbBillService = mbBillService;
	}
	public void setCancelBillService(PersistenceService<MBForCancelledBill, Long> cancelBillService) {
		this.cancelBillService = cancelBillService;
	}

	public BigDecimal getGrossAmount() {
		return grossAmount;
	}

	public void setGrossAmount(BigDecimal grossAmount) {
		this.grossAmount = grossAmount;
	}

	public void setBudgetService(BudgetService budgetService) {
		this.budgetService = budgetService;
	}
	
	public String getRetentionMoneyRequired() {		 
		return worksService.getWorksConfigValue("RETENTIONMONEY_REQUIRED");
	}

	public List<EgBilldetails> getReleaseWithHeldAmountDeductions() {
		return releaseWithHeldAmountDeductions;
	}

	public void setReleaseWithHeldAmountDeductions(
			List<EgBilldetails> releaseWithHeldAmountDeductions) {
		this.releaseWithHeldAmountDeductions = releaseWithHeldAmountDeductions;
	}
	
	public boolean isAccountRetentionMoneyAccountType(CChartOfAccounts coa){
		boolean retMoneyType=false;
		for(CChartOfAccounts account:retentionMoneyAccountList){
			if(coa.getId().equals(account.getId()))
				retMoneyType=true;
		}
		return retMoneyType;
	}

	public String[] getContractCertRemarks() {
		return contractCertRemarks;
	}

	public void setContractCertRemarks(String[] contractCertRemarks) {
		this.contractCertRemarks = contractCertRemarks;
	}
	
	public InputStream getContractCertificatePDF() {
		return contractCertificatePDF;
	}

	public void setContractCertificatePDF(InputStream contractCertificatePDF) {
		this.contractCertificatePDF = contractCertificatePDF;
	}

	public InputStream getTransferEntryMemoPDF() {
		return transferEntryMemoPDF;
	}

	public void setTransferEntryMemoPDF(InputStream transferEntryMemoPDF) {
		this.transferEntryMemoPDF = transferEntryMemoPDF;
	}

	public boolean getIsSecurityDeposit(){
		return isSecurityDeposit;
	}
	
	public void setIsSecurityDeposit(boolean isSecurityDeposit) {
		this.isSecurityDeposit = isSecurityDeposit;
	}

	public String getBillGeneratedBy() {
		return billGeneratedBy;
	}

	public void setBillGeneratedBy(String billGeneratedBy) {
		this.billGeneratedBy = billGeneratedBy;
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public String getPendingActions()
	{
		return contractorBillRegister==null?"":
			(contractorBillRegister.getCurrentState()==null?"":contractorBillRegister.getCurrentState().getNextAction());
	}

	public String getWorkFlowDepartment(){
		WorkOrder workorder;
		if(((contractorBillRegister!=null) && contractorBillRegister.getWorkordernumber()!=null)){
			workorder=(WorkOrder) getPersistenceService().find("from WorkOrder where workOrderNumber=?",contractorBillRegister.getWorkordernumber());
		}
		return workOrder==null?"":(workOrder.getWorkOrderEstimates()==null?"":(workOrder.getWorkOrderEstimates().get(0).getEstimate()==null?"":(workOrder.getWorkOrderEstimates().get(0).getEstimate().getIsSpillOverWorks()?null:(workOrder.getWorkOrderEstimates().get(0).getEstimate().getExecutingDepartment()==null?"":workOrder.getWorkOrderEstimates().get(0).getEstimate().getExecutingDepartment().getDeptName()))));
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public void setIsSpillOverWorks(boolean isSpillOverWorks) {
		this.isSpillOverWorks = isSpillOverWorks;
}

	public boolean getIsSpillOverWorks() {
		return isSpillOverWorks;
	}
	
	public boolean isMBWithPartRate(){
		boolean mbWithPartRate=false;
		if(mbHeaderList!=null){
			for(MBHeader mbh:mbHeaderList){
				List validBills = new ArrayList();
				boolean validBillexists=false;
				for(MBBill mbr:mbh.getMbBills()){
					if(mbr.getEgBillregister().getBillstatus()!=null && !mbr.getEgBillregister().getBillstatus().equals("CANCELLED")){
						validBillexists=true;
						validBills.add(mbr);
					}
				}
				for(MBDetails mbd:mbh.getMbDetails()){
					if(mbd.getPartRate()>0 && validBills.size()<1){
						mbWithPartRate=true;
					}
				}
			}
		}
		return mbWithPartRate;
	}

}
