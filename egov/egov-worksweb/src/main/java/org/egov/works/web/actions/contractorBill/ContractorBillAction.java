/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.works.web.actions.contractorBill;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.assets.model.Asset;
import org.egov.assets.service.AssetService;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.EgPartytype;
import org.egov.commons.EgwStatus;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.EgPartytypeHibernateDAO;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.commons.dao.FunctionaryHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.commons.dao.SchemeHibernateDAO;
import org.egov.commons.dao.SubSchemeHibernateDAO;
import org.egov.egf.commons.EgovCommon;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.NumberUtil;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.entity.WorkflowAction;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.models.EgChecklists;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregistermis;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.recoveries.Recovery;
import org.egov.pims.service.EmployeeServiceOld;
import org.egov.services.budget.BudgetService;
import org.egov.services.recoveries.RecoveryService;
import org.egov.services.voucher.VoucherService;
import org.egov.works.abstractestimate.entity.FinancialDetail;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.milestone.entity.TrackMilestone;
import org.egov.works.models.contractorBill.AssetForBill;
import org.egov.works.models.contractorBill.DeductionTypeForBill;
import org.egov.works.models.contractorBill.StatutoryDeductionsForBill;
import org.egov.works.models.contractorBill.WorkCompletionDetailInfo;
import org.egov.works.models.contractorBill.WorkCompletionInfo;
import org.egov.works.models.measurementbook.MBForCancelledBill;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.tender.TenderResponse;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.ContractorBillService;
import org.egov.works.services.WorksService;
import org.egov.works.services.contractoradvance.ContractorAdvanceService;
import org.egov.works.services.impl.MeasurementBookServiceImpl;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.script.ScriptContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
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

@ParentPackage("egov")
@Results({ @Result(name = ContractorBillAction.PRINT, type = "stream", location = "CompletionCertificatePDF", params = {
        "inputName", "CompletionCertificatePDF", "contentType", "application/pdf", "contentDisposition",
        "no-cache;filename=ContractorBill_CompletionCertificate.pdf" }),
        @Result(name = ContractorBillAction.NEW, location = "contractorBill-new.jsp") })
public class ContractorBillAction extends BaseFormAction {

    private static final long serialVersionUID = -2842467886385709531L;
    private static final Logger logger = Logger.getLogger(ContractorBillAction.class);
    private ContractorBillService contractorBillService;
    private PersistenceService<MBForCancelledBill, Long> cancelBillService;
    private WorksService worksService;
    private ContractorBillRegister contractorBillRegister = new ContractorBillRegister();
    private WorkOrder workOrder;
    private Long id;
    private Long workOrderId;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;
    @Autowired
    private EgPartytypeHibernateDAO egPartytypeHibernateDAO;
    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;
    @Autowired
    private AssetService assetService;
    private MeasurementBookServiceImpl measurementBookService;
    private EgovCommon egovCommon;
    @Autowired
    private UserService userService;
    private PersistenceService<EgChecklists, Long> checklistService;
    private List<StatutoryDeductionsForBill> actionStatutorydetails = new LinkedList<StatutoryDeductionsForBill>();
    private List<AssetForBill> accountDetailsForBill = new ArrayList<AssetForBill>();
    private Map<Long, String> contratorCoaPayableMap = new HashMap<Long, String>();
    private String disp;
    private BigDecimal totalAdvancePaid = BigDecimal.ZERO;
    private BigDecimal totalPendingBalance = BigDecimal.ZERO;
    private BigDecimal advaceAdjustmentCreditAmount = BigDecimal.ZERO;
    private BigDecimal billAmount = BigDecimal.ZERO;
    private BigDecimal utilizedAmount = BigDecimal.ZERO;
    private BigDecimal sanctionedBudget = BigDecimal.ZERO;
    private PersistenceService<FinancialDetail, Long> financialDetailService;
    private Long netPayableCode;
    private BigDecimal netPayableAmount;
    private String partyBillNumber;
    private Date partyBillDate;
    private Date completionDate;
    private Integer partbillNo;
    private String sourcepage = "";
    private List<String> checklistValues = new LinkedList<String>();
    private List<AppConfigValues> finalBillChecklist = new LinkedList<AppConfigValues>();
    private List<CChartOfAccounts> standardDeductionAccountList = new LinkedList<CChartOfAccounts>();
    private List<CChartOfAccounts> customDeductionAccountList = new LinkedList<CChartOfAccounts>();
    private List<CChartOfAccounts> retentionMoneyAccountList = new LinkedList<CChartOfAccounts>();
    private List<String> standardDeductionConfValues = new LinkedList<String>();
    private List<DeductionTypeForBill> standardDeductions = new LinkedList<DeductionTypeForBill>();
    private List<EgBilldetails> customDeductions = new LinkedList<EgBilldetails>();
    private List<EgBilldetails> retentionMoneyDeductions = new LinkedList<EgBilldetails>();
    private List<MBHeader> mbHeaderList = new LinkedList<MBHeader>();
    private Long[] appConfigValueId;
    private String[] selectedchecklistValue;
    private String[] remarks;
    private static final String ASSET_LIST = "assestList";
    private static final String SOURCE_INBOX = "inbox";
    private static final String COA_LIST = "coaList";
    private static final String CAPITAL_WORKS = "Capital Works";
    private static final String IMPROVEMENT_WORKS = "Improvement Works";
    private static final String REPAIR_AND_MAINTENANCE = "Repairs and maintenance";
    private static final String WORKS_NETPAYABLE_CODE = "WORKS_NETPAYABLE_CODE";
    private static final String RETENTION_MONEY_PURPOSE = "RETENTION_MONEY_PURPOSE";
    private static final String KEY_CWIP = "WORKS_CWIP_CODE";
    private static final String KEY_REPAIRS = "WORKS_REPAIRS_AND_MAINTENANCE";
    private static final String KEY_DEPOSIT = "WORKS_DEPOSIT_OTHER_WORKS";
    private static final String BILL_STATUS = "APPROVED";
    private static final String ACCOUNTDETAIL_TYPE_CONTRACTOR = "contractor";
    private static final String EXPENDITURE_TYPE = "Works";
    private static final String BILL_MODULE_KEY = "CONTRACTORBILL";
    private static final String AMOUNT_ERROR = "amount.lessthan.zero";
    @Autowired
    private DepartmentService departmentService;
    private WorkflowService<ContractorBillRegister> workflowService;
    private static final String SAVE_ACTION = "save";
    private String messageKey;
    private static final String ACTION_NAME = "actionName";
    private String nextEmployeeName;
    private String nextDesignation;
    @Autowired
    @Qualifier("recoveryPersistenceService")
    private RecoveryService recoveryService;
    private Long workOrderEstimateId;
    private List<WorkOrderEstimate> workOrderEstimateList = new ArrayList<WorkOrderEstimate>();
    private WorkOrderEstimate workOrderEstimate = new WorkOrderEstimate();
    public static final String PRINT = "print";
    private InputStream completionCertificatePDF;
    private ReportService reportService;
    private static final String APPCONFIG_KEY_NAME = "SKIP_BUDGET_CHECK";
    private VoucherService voucherService;
    private ScriptService scriptExecutionService;
    private WorkCompletionInfo completionInfo;
    private List<WorkCompletionDetailInfo> completionDetailInfoList = new ArrayList<WorkCompletionDetailInfo>();
    private boolean skipBudget;
    private static final String CHECK_BUDGET = "CHECK_BUDGET";
    private static final String ACCOUNTDETAIL_TYPE_PROJECTCODE = "PROJECTCODE";

    private String isRetMoneyAutoCalculated;
    private String isRetMoneyEditable;
    private String percDeductForRetMoneyPartBill;
    private String percDeductForRetMoneyFinalBill;
    private String retMoneyFinalBillPerOnValue;
    private TenderResponse tenderResponse;
    private String rebatePremLevel;

    // Added for Automatic calculation of statutory recoveries in bill
    private List<EgPartytype> subPartyTypeDtls = new ArrayList<EgPartytype>();
    private List<EgwTypeOfWork> typeOfWorkDtls = new ArrayList<EgwTypeOfWork>();
    public static final String BILL_STATUTORYDEDUCTIONS_SHOW_PARTYSUBTYPE = "BILL_STATUTORYDEDUCTIONS_SHOW_PARTYSUBTYPE";
    public static final String BILL_STATUTORYDEDUCTIONS_SHOW_SERVICETYPE = "BILL_STATUTORYDEDUCTIONS_SHOW_SERVICETYPE";
    public static final String BILL_STATUTORYDEDUCTIONS_EDITABLE = "BILL_STATUTORYDEDUCTIONS_EDITABLE";
    private String showSubPartyType;
    private String showTypeOfWork;
    private String editStatutoryAmount;
    public static final String PARTY_TYPE_CODE = "Contractor";
    private Long estimateId;
    private String percTenderType;
    private String isRebatePremLevelBill;
    private BigDecimal grossAmount = BigDecimal.ZERO;
    private BigDecimal tenderedItemsAmount = BigDecimal.ZERO;
    @Autowired
    private FunctionaryHibernateDAO functionaryHibDao;
    private BudgetService budgetService;
    private final String PENDING_FOR_VERIFICATION = "Pending for Verification";
    private final String PENDING_FOR_VALIDATION = "Pending for Validation";
    private final String PENDING_FOR_APPROVAL = "Pending for Approval";
    private final String PENDING_FOR_RECTIFICATION = "Pending for Rectification";
    private final String PENDING_FOR_CHECK = "Pending for Check";
    private Integer workflowFunctionaryId;
    private Long[] mbHeaderId;
    private List<MBHeader> mbHeaderListForBillId = new LinkedList<MBHeader>();
    private ContractorAdvanceService contractorAdvanceService;
    @Autowired
    private EmployeeServiceOld employeeService;
    private String isRCEstimate = "";
    private String dwCategory = "";
    private Date restorationEndDate;
    private String showValidationMsg = "";
    @Autowired
    private FundHibernateDAO fundHibernateDao;
    @Autowired
    private FunctionHibernateDAO functionHibernateDao;
    @Autowired
    private SchemeHibernateDAO schemeHibernateDAO;
    @Autowired
    private SubSchemeHibernateDAO subschemeHibernateDAO;
    @Autowired
    private FinancialYearHibernateDAO finHibernateDao;
    private String allowForward = "";
    private Date latestMBDate;
    private String refNo;

    public ContractorBillAction() {

    }

    @Override
    public void prepare() {
        super.prepare();
        if (id != null) {
            contractorBillRegister = contractorBillService.findById(id, false);
            if (contractorBillRegister.getBillstatus().equals("CANCELLED")) {

                for (final MBForCancelledBill mbCancelBillObj : contractorBillService.getMbListForCancelBill(id))
                    if (!mbHeaderListForBillId.contains(mbCancelBillObj.getMbHeader()))
                        mbHeaderListForBillId.add(mbCancelBillObj.getMbHeader());
            } else
                mbHeaderListForBillId = measurementBookService.findAllByNamedQuery("getAllMBsForBillId",
                        WorksConstants.APPROVED, id);
            if (mbHeaderListForBillId != null && !mbHeaderListForBillId.isEmpty()) {
                workOrder = mbHeaderListForBillId.get(0).getWorkOrder();
                workOrderId = workOrder.getId();
                workOrderEstimate = mbHeaderListForBillId.get(0).getWorkOrderEstimate();
            }
        }

        if (workOrderId != null) {
            workOrder = (WorkOrder) persistenceService.find("from WorkOrder where id=?", workOrderId);
            workOrderEstimateList.addAll(getPersistenceService().findAllByNamedQuery(
                    "getWorkOrderEstimateByWorkOrderId", workOrderId));
            loadTenderDetails();
        }
        if (id != null) {
            if (StringUtils.isNotBlank(isRebatePremLevelBill) && isRebatePremLevelBill.equals("yes")
                    || isRCEstimate.equalsIgnoreCase(WorksConstants.YES))
                billAmount = contractorBillService.getApprovedMBAmountforBill(contractorBillRegister);

            if (!isRCEstimate.equalsIgnoreCase(WorksConstants.YES))
                tenderedItemsAmount = contractorBillService
                        .getApprovedMBAmountOfTenderedItemsForBill(contractorBillRegister);
        }
        if (workOrderEstimateId != null)
            workOrderEstimate = (WorkOrderEstimate) persistenceService.find("from WorkOrderEstimate where id=?",
                    workOrderEstimateId);
        if (workOrderEstimateList.isEmpty())
            addDropdownData("workOrderEstimateList", Collections.EMPTY_LIST);
        else {
            workOrderEstimateList = measurementBookService.getWOEstForBillExludingLegacyMB(workOrderEstimateList);
            addDropdownData("workOrderEstimateList", workOrderEstimateList);
        }

        if (workOrderEstimateList.size() == 1 && id == null) {
            workOrderEstimate = workOrderEstimateList.get(0);
            mbHeaderList = measurementBookService.getApprovedMBList(workOrder.getId(), workOrderEstimate.getId(),
                    new Date());
        }

        try {
            if (workOrderEstimate != null && workOrderEstimate.getId() != null) {
                isSkipBudgetCheck();
                addDropdownData(ASSET_LIST, workOrderEstimate.getAssetValues());
                final String accountCodeFromBudgetHead = worksService
                        .getWorksConfigValue("BILL_DEFAULT_BUDGETHEAD_ACCOUNTCODE");
                if (workOrderEstimate.getEstimate().getNatureOfWork().getCode().equals(CAPITAL_WORKS)
                        || workOrderEstimate.getEstimate().getNatureOfWork().getCode().equals(IMPROVEMENT_WORKS)) {
                    if (StringUtils.isNotBlank(accountCodeFromBudgetHead) && "no".equals(accountCodeFromBudgetHead)
                            && StringUtils.isNotBlank(worksService.getWorksConfigValue(KEY_CWIP)))
                        addDropdownData(COA_LIST, chartOfAccountsHibernateDAO.getAccountCodeByPurpose(Integer.valueOf(worksService
                                .getWorksConfigValue(KEY_CWIP))));
                    else if (StringUtils.isNotBlank(accountCodeFromBudgetHead)
                            && "yes".equals(accountCodeFromBudgetHead)) {
                        final List<BudgetGroup> budgetGroupList = new ArrayList<BudgetGroup>();
                        if (workOrderEstimate.getEstimate().getFinancialDetails().get(0).getBudgetGroup() != null)
                            budgetGroupList.add(workOrderEstimate.getEstimate().getFinancialDetails().get(0)
                                    .getBudgetGroup());
                        final List<CChartOfAccounts> coaList = budgetService
                                .getAccountCodeForBudgetHead(budgetGroupList);
                        addDropdownData(COA_LIST, coaList);
                    } else
                        addDropdownData(COA_LIST, Collections.EMPTY_LIST);
                } else if (workOrderEstimate.getEstimate().getNatureOfWork().getCode().equals(REPAIR_AND_MAINTENANCE)) {
                    if (StringUtils.isNotBlank(accountCodeFromBudgetHead) && "no".equals(accountCodeFromBudgetHead)
                            && StringUtils.isNotBlank(worksService.getWorksConfigValue(KEY_REPAIRS)))
                        addDropdownData(COA_LIST, chartOfAccountsHibernateDAO.getAccountCodeByPurpose(Integer.valueOf(worksService
                                .getWorksConfigValue(KEY_REPAIRS))));
                    else if (StringUtils.isNotBlank(accountCodeFromBudgetHead)
                            && "yes".equals(accountCodeFromBudgetHead)) {
                        final List<BudgetGroup> budgetGroupList = new ArrayList<BudgetGroup>();
                        if (workOrderEstimate.getEstimate().getFinancialDetails().get(0).getBudgetGroup() != null)
                            budgetGroupList.add(workOrderEstimate.getEstimate().getFinancialDetails().get(0)
                                    .getBudgetGroup());
                        final List<CChartOfAccounts> coaList = budgetService
                                .getAccountCodeForBudgetHead(budgetGroupList);
                        addDropdownData(COA_LIST, coaList);
                    } else
                        addDropdownData(COA_LIST, Collections.EMPTY_LIST);
                } else if (getAppConfigValuesToSkipBudget().contains(
                        workOrderEstimate.getEstimate().getNatureOfWork().getName()))
                    if (StringUtils.isNotBlank(worksService.getWorksConfigValue(KEY_DEPOSIT)) && !skipBudget)
                        // Story# 806 - Show all the CWIP codes in the
                        // contractor bill screen where we show the deposit COA
                        // code now
                        // addDropdownData(COA_LIST,
                        // commonsService.getAccountCodeByPurpose(Integer.valueOf(worksService.getWorksConfigValue(KEY_DEPOSIT))));
                        addDropdownData(COA_LIST, chartOfAccountsHibernateDAO.getAccountCodeByPurpose(Integer.valueOf(worksService
                                .getWorksConfigValue(KEY_CWIP))));
                    else
                        addDropdownData(COA_LIST, Collections.EMPTY_LIST);

            }
            populateOtherDeductionList();
            setRetentionMoneyConfigValues();
        } catch (final ApplicationException v) {
            logger.error("Unable to COA for WorkOrder" + v);
            addFieldError("COA.notfound", "Unable to COA for WorkOrder");
        }
        if (contractorBillRegister != null && contractorBillRegister.getBilldate() == null
                && workOrderEstimateList.size() == 1) {
            workOrderEstimateId = workOrderEstimateList.get(0).getId();
            setBudgetDetails(workOrderId, workOrderEstimateId, new Date());
        }
        if (workOrderEstimate.getId() != null && workOrderId != null && id == null)
            tenderedItemsAmount = contractorBillService.getApprovedMBAmountOfTenderedItems(workOrderId,
                    workOrderEstimate.getId(), new Date());
        /*
         * else if(contractorBillRegister!=null && workOrderEstimateList.size()>1) {
         * setBudgetDetails(workOrderId,workOrderEstimateId ,contractorBillRegister.getBilldate()); }
         */
        addDropdownData("billTypeList", contractorBillService.getBillType());
        final List<Recovery> statutoryDeductionsList = recoveryService.getAllTdsByPartyType("Contractor");
        addDropdownData("statutoryDeductionsList", statutoryDeductionsList);
        addDropdownData("executingDepartmentList", departmentService.getAllDepartments());
        /*
         * if (abstractEstimateService.getLatestAssignmentForCurrentLoginUser() != null)
         * contractorBillRegister.setWorkflowDepartmentId(abstractEstimateService
         * .getLatestAssignmentForCurrentLoginUser().getDepartment().getId());
         */
        // Setting the functionary as UAC for the workflow
        if (contractorBillRegister != null
                && contractorBillRegister.getId() != null
                && contractorBillRegister.getState() != null
                && contractorBillRegister.getState().getNextAction() != null
                && (contractorBillRegister.getState().getNextAction().equalsIgnoreCase(PENDING_FOR_VERIFICATION)
                        || contractorBillRegister.getState().getNextAction().equalsIgnoreCase(PENDING_FOR_VALIDATION)
                        || contractorBillRegister.getState().getNextAction().equalsIgnoreCase(PENDING_FOR_APPROVAL)
                        || contractorBillRegister.getState().getNextAction()
                                .equalsIgnoreCase(PENDING_FOR_RECTIFICATION)
                        || contractorBillRegister.getState()
                                .getNextAction().equalsIgnoreCase(PENDING_FOR_CHECK)
                                && contractorBillRegister.getState().getValue().equalsIgnoreCase("REJECTED"))) {
            final Functionary func = (Functionary) persistenceService.find(" from  Functionary where upper(name) = ?",
                    "UAC");
            workflowFunctionaryId = func.getId();
        }

        showSubPartyType = worksService.getWorksConfigValue(BILL_STATUTORYDEDUCTIONS_SHOW_PARTYSUBTYPE);
        logger.debug("showSubPartyType>>>>>> " + showSubPartyType);
        showTypeOfWork = worksService.getWorksConfigValue(BILL_STATUTORYDEDUCTIONS_SHOW_SERVICETYPE);
        logger.debug("showTypeOfWork>>>>>> " + showTypeOfWork);
        editStatutoryAmount = worksService.getWorksConfigValue(BILL_STATUTORYDEDUCTIONS_EDITABLE);
        logger.debug("editStatutoryAmount>>>>>>> " + editStatutoryAmount);

        List<EgPartytype> subPartyTypeList = new ArrayList<EgPartytype>();
        List<EgwTypeOfWork> typeOfWorkList = new ArrayList<EgwTypeOfWork>();
        if (showSubPartyType != null && showSubPartyType != "") {
            subPartyTypeList = egPartytypeHibernateDAO.getSubPartyTypesForCode(PARTY_TYPE_CODE);
            addDropdownData("subPartyTypeList", subPartyTypeList);
        } else
            addDropdownData("subPartyTypeList", subPartyTypeList);

        if (showTypeOfWork != null && showTypeOfWork != "") {
            typeOfWorkList = getPersistenceService().findAllBy("from EgwTypeOfWork wt where wt.parentid is null");
            addDropdownData("typeOfWorkList", typeOfWorkList);
        } else
            addDropdownData("typeOfWorkList", typeOfWorkList);
        getLastCreatedMBDate();
    }

    private void getLastCreatedMBDate() {
        if (id == null
                && workOrderEstimateList.size() == 1
                || contractorBillRegister.getStatus() != null
                        && (contractorBillRegister.getStatus().getCode().equalsIgnoreCase(WorksConstants.NEW)
                                || contractorBillRegister
                                        .getStatus().getCode().equalsIgnoreCase(WorksConstants.REJECTED))) {
            final Object[] mbDateRefNo = contractorBillService.getLatestMBCreatedDateAndRefNo(workOrderId,
                    workOrderEstimate.getEstimate().getId());
            if (mbDateRefNo != null && mbDateRefNo.length > 0) {
                refNo = (String) mbDateRefNo[0];
                latestMBDate = (Date) mbDateRefNo[1];
            }
        }
    }

    private void checkForBillsInWorkflowForDepositWorks() {
        allowForward = WorksConstants.YES;
        if (workOrderEstimate.getEstimate().getDepositCode() != null && skipBudget
                && StringUtils.isNotBlank(worksService.getWorksConfigValue(KEY_DEPOSIT)))
            // Checking for bills in workflow whether the account code selected
            // is same as in the mapping table. If different, set the
            // allowForward variable which will be used to hide the
            // forward/approve button
            if (!sourcepage.equalsIgnoreCase("search")
                    && contractorBillRegister.getStatus() != null
                    && !(contractorBillRegister.getStatus().getCode().equalsIgnoreCase(WorksConstants.NEW)
                            || contractorBillRegister.getStatus().getCode()
                                    .equalsIgnoreCase(ContractorBillRegister.BillStatus.REJECTED.toString())
                            || contractorBillRegister.getStatus().getCode()
                                    .equalsIgnoreCase(ContractorBillRegister.BillStatus.APPROVED.toString())
                            || contractorBillRegister
                                    .getStatus().getCode()
                                    .equalsIgnoreCase(ContractorBillRegister.BillStatus.CANCELLED.toString())))
                allowForward = contractorBillService.validateForBudgetHeadInWorkflow(
                        contractorBillRegister.getEgBilldetailes(), workOrderEstimate.getEstimate());
    }

    private void populateBudgetHeadForDepositWorksEstimate() {
        if (StringUtils.isNotBlank(worksService.getWorksConfigValue(KEY_DEPOSIT)) && skipBudget
                && workOrderEstimate.getEstimate() != null
                && workOrderEstimate.getEstimate().getFinancialDetails() != null
                && workOrderEstimate.getEstimate().getFinancialDetails().get(0) != null
                && workOrderEstimate.getEstimate().getFinancialDetails().get(0).getCoa() != null
                && workOrderEstimate.getEstimate().getDepositCode() != null)
            if (id == null
                    || contractorBillRegister.getStatus() != null
                            && (contractorBillRegister.getStatus().getCode().equalsIgnoreCase(WorksConstants.NEW)
                                    || contractorBillRegister
                                            .getStatus().getCode().equalsIgnoreCase(WorksConstants.REJECTED))) {
                final List<CChartOfAccounts> mappedBudgetHeadList = contractorBillService
                        .getBudgetHeadForDepositCOA(workOrderEstimate.getEstimate());
                if (mappedBudgetHeadList.isEmpty()) {
                    showValidationMsg = WorksConstants.YES;
                    addDropdownData(COA_LIST, Collections.EMPTY_LIST);
                    addFieldError("contractoBill.depositCOA.budgetHead.mapping.error",
                            getText("contractoBill.depositCOA.budgetHead.mapping.error"));
                } else
                    addDropdownData(COA_LIST, mappedBudgetHeadList);
            } else
                addDropdownData(COA_LIST, chartOfAccountsHibernateDAO.getAccountCodeByPurpose(Integer.valueOf(worksService
                        .getWorksConfigValue(KEY_CWIP))));
    }

    private void loadTenderDetails() {
        tenderResponse = (TenderResponse) persistenceService.find(
                "from TenderResponse tr where tr.negotiationNumber=? and " + " tr.egwStatus.code !=? ",
                workOrder.getNegotiationNumber(), TenderResponse.TenderResponseStatus.CANCELLED.toString());
        rebatePremLevel = worksService.getWorksConfigValue("REBATE_PREMIUM_LEVEL");
        if (rebatePremLevel.equalsIgnoreCase(WorksConstants.BILL))
            isRebatePremLevelBill = WorksConstants.YES;
        else
            isRebatePremLevelBill = WorksConstants.NO;
        final List<String> tenderTypeList = worksService.getTendertypeList();
        if (tenderTypeList != null && !tenderTypeList.isEmpty())
            percTenderType = tenderTypeList.get(0);
    }

    private void setRetentionMoneyConfigValues() {
        isRetMoneyAutoCalculated = worksService.getWorksConfigValue("IS_RETENTION_MONEY_AUTOCALCULATED");
        isRetMoneyEditable = worksService.getWorksConfigValue("IS_RETENTION_MONEY_EDITABLE");
        percDeductForRetMoneyPartBill = worksService
                .getWorksConfigValue("PERCENTAGE_DEDUCTION_FOR_RETENTION_MONEY_PART_BILL");
        percDeductForRetMoneyFinalBill = worksService
                .getWorksConfigValue("PERCENTAGE_DEDUCTION_FOR_RETENTION_MONEY_FINAL_BILL");
        retMoneyFinalBillPerOnValue = worksService.getWorksConfigValue("RETENTION_MONEY_PERCENTAGE_ON_VALUE");
    }

    @SkipValidation
    public String edit() throws Exception {

        if (SOURCE_INBOX.equalsIgnoreCase(sourcepage)) {
            final User user = userService.getUserById(worksService.getCurrentLoggedInUserId());
            final boolean isValidUser = worksService.validateWorkflowForUser(contractorBillRegister, user);
            if (isValidUser)
                throw new ApplicationRuntimeException("Error: Invalid Owner - No permission to view this page.");
        } else if (StringUtils.isEmpty(sourcepage))
            sourcepage = "search";

        checklistValues.add("Yes");
        checklistValues.add("No");
        contractorBillRegister = getContractorbillregisterforbillnumber(contractorBillRegister.getBillnumber());// contractorBillService.findByNamedQuery("getContractorBillRegister",contractorBillRegister.getBillnumber());
        if (contractorBillRegister != null) {
            setId(contractorBillRegister.getId());

            if (!isRCEstimate.equalsIgnoreCase(WorksConstants.YES))
                if (!(isRebatePremLevelBill.equals("yes") && tenderResponse.getTenderEstimate().getTenderType()
                        .equals(getPercTenderType())))
                    setBillAmount(contractorBillRegister.getPassedamount());
            setCompletionDate(workOrderEstimate.getWorkCompletionDate());
            setPartyBillNumber(contractorBillRegister.getEgBillregistermis().getPartyBillNumber());
            setPartyBillDate(contractorBillRegister.getEgBillregistermis().getPartyBillDate());

            if (contractorBillRegister.getBillstatus().equals("CANCELLED")) {

                for (final MBForCancelledBill mbCancelBillObj : contractorBillService.getMbListForCancelBill(id))
                    if (!mbHeaderList.contains(mbCancelBillObj.getMbHeader()))
                        mbHeaderList.add(mbCancelBillObj.getMbHeader());
            } else
                for (final MBHeader mbObj : contractorBillService.getMbListForBillAndWorkordrId(workOrderId, id))
                    if (!mbHeaderList.contains(mbObj))
                        mbHeaderList.add(mbObj);

            workOrderEstimate = mbHeaderList.get(0).getWorkOrderEstimate();
            populateBudgetHeadForDepositWorksEstimate();
            checkForBillsInWorkflowForDepositWorks();
            totalAdvancePaid = contractorAdvanceService.getTotalAdvancePaymentMadeByWOEstimateId(
                    workOrderEstimate.getId(), contractorBillRegister.getBilldate());
            setTotalPendingBalance(contractorBillService.calculateTotalPendingAdvance(totalAdvancePaid,
                    contractorBillRegister.getBilldate(), workOrderEstimate, contractorBillRegister.getId()));
            setAdvaceAdjustmentCreditAmount(contractorBillService.getAdvanceAdjustmentAmountForBill(id,
                    workOrderEstimate.getId()));
            setNetPayableCode(contractorBillService.getNetPaybleCode(id).longValue());
            setNetPayableAmount(contractorBillService.getNetPayableAmountForGlCodeId(id));
            disp = "yes";
            // setUtilizedAmount(contractorBillService.getTotalUtilizedAmount(wortionStatutorydetails::"+actionStatutorydetails);
            contractorBillService.setAllViewLists(id, workOrderId, workOrderEstimate.getId(), actionStatutorydetails,
                    standardDeductions, customDeductions, retentionMoneyDeductions, accountDetailsForBill);
            final List<EgChecklists> list = contractorBillService.getEgcheckList(id);
            int i = 0;
            selectedchecklistValue = new String[list.size()];
            for (final EgChecklists egChecklists : list) {
                finalBillChecklist.add(egChecklists.getAppconfigvalue());
                selectedchecklistValue[i] = egChecklists.getChecklistvalue();
                i++;
            }
        }
        return EDIT;
    }

    @SkipValidation
    public String viewCompletionCertificate() {
        ReportRequest reportRequest = null;
        final Map<String, Object> reportParams = new HashMap<String, Object>();
        reportParams.put(WorksConstants.PARAMETERNAME_WORKCOMPLETIONINFO, getWorkcompletionInfo());
        reportRequest = new ReportRequest(WorksConstants.TEMPLATENAME_COMPLETIONCERTIFICATE,
                getWorkCompletionDetailInfo(), reportParams);
        final ReportOutput reportOutput = reportService.createReport(reportRequest);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            completionCertificatePDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
        return PRINT;
    }

    public List<WorkCompletionDetailInfo> getWorkCompletionDetailInfo() {
        final List<MBHeader> mbHeaderList = persistenceService.findAllByNamedQuery(
                WorksConstants.QUERY_GETALLMBHEADERSBYBILLID, contractorBillRegister.getId());
        if (!mbHeaderList.isEmpty())
            return contractorBillService.setWorkCompletionDetailInfoList(mbHeaderList.get(0).getWorkOrderEstimate());
        return new ArrayList<WorkCompletionDetailInfo>();
    }

    public WorkCompletionInfo getWorkcompletionInfo() {
        final List<MBHeader> mbHeaderList = persistenceService.findAllByNamedQuery(
                WorksConstants.QUERY_GETALLMBHEADERSBYBILLID, contractorBillRegister.getId());
        if (!mbHeaderList.isEmpty())
            return contractorBillService.setWorkCompletionInfoFromBill(contractorBillRegister, mbHeaderList.get(0)
                    .getWorkOrderEstimate());
        return new WorkCompletionInfo();
    }

    public ContractorBillRegister getContractorbillregisterforbillnumber(final String billNumber) {
        return contractorBillService.findByNamedQuery("getContractorBillRegister", billNumber);
    }

    @Override
    public Object getModel() {
        return contractorBillRegister;
    }

    @SkipValidation
    public String view() {
        return "view";
    }

    public void setModel(final ContractorBillRegister contractorBillRegister) {
        this.contractorBillRegister = contractorBillRegister;
    }

    @Override
    public String execute() {
        return SUCCESS;
    }

    @SkipValidation
    @Action(value = "/contractorBill/contractorBill-newform")
    public String newform() {
        if (!contractorBillService.getBillType().isEmpty()) {
            final List<MBHeader> objList = measurementBookService.getPartBillList(workOrderId, contractorBillService
                    .getBillType().get(0).toString());
            contractorBillRegister.setBillSequenceNumber(objList.size() + 1);
        }
        populateBudgetHeadForDepositWorksEstimate();
        return NEW;
    }

    private void validateMilestoneCompletion() {
        if (id == null) {
            List<TrackMilestone> tm = null;
            if (contractorBillRegister.getBilltype().equals(contractorBillService.getBillType().get(1).toString())) {
                tm = persistenceService
                        .findAllBy(
                                " select trmls from WorkOrderEstimate as woe left join woe.milestone mls left join mls.trackMilestone trmls where trmls.egwStatus.code='APPROVED' and woe.workOrder.id = ? and trmls.total=100 ",
                                workOrderId);
                if (tm == null || tm.isEmpty() || tm.get(0) == null)
                    addActionError(getText("contactor.final.bill.milestone.ajaxmsg"));
            }
            if (contractorBillRegister.getBilltype().equals(contractorBillService.getBillType().get(0).toString())) {
                tm = persistenceService
                        .findAllBy(
                                " select trmls from WorkOrderEstimate as woe left join woe.milestone mls left join mls.trackMilestone trmls where trmls.egwStatus.code='APPROVED' and woe.workOrder.id = ? and trmls.total>0 ",
                                workOrderId);
                if (tm == null || tm.isEmpty() || tm.get(0) == null)
                    addActionError(getText("contactor.final.bill.milestone.ajaxmsg"));
            }
        }

    }

    public String save() throws Exception {
        String actionName = "";
        if (parameters.get(ACTION_NAME) != null && parameters.get(ACTION_NAME)[0] != null)
            actionName = parameters.get(ACTION_NAME)[0];
        if (contractorBillRegister.getStatus() == null
                || WorksConstants.REJECTED.equalsIgnoreCase(contractorBillRegister.getStatus().getCode())
                        && StringUtils.isBlank(contractorBillRegister.getState().getNextAction())
                || NEW.equalsIgnoreCase(contractorBillRegister.getStatus().getCode())) {
            contractorBillRegister.getEgBilldetailes().clear();
            contractorBillRegister.getStatutoryDeductionsList().clear();
            contractorBillRegister.getDeductionTypeList().clear();
            contractorBillRegister.getAssetDetailsList().clear();
        }
        workOrderEstimate = (WorkOrderEstimate) persistenceService.find("from WorkOrderEstimate where id=?",
                workOrderEstimateId);
        validateMilestoneCompletion();

        if (id == null)
            mbHeaderList = measurementBookService.getApprovedMBList(workOrderId, workOrderEstimateId,
                    contractorBillRegister.getBilldate());
        else
            mbHeaderList = contractorBillService.getMbListForBillAndWorkordrId(workOrderId, id);
        if (mbHeaderList == null || mbHeaderList.isEmpty())
            throw new ValidationException(Arrays.asList(new ValidationError("error", getText(
                    "no.approved.mb.for.billdate",
                    new String[] { new SimpleDateFormat("dd/MM/yyyy").format(contractorBillRegister.getBilldate()) }))));

        if (!actionName.equalsIgnoreCase("reject")) {
            validateARFInWorkflow();
            // Validation is done only for deposit works estimates on save and
            // submit
            if (workOrderEstimate.getEstimate() != null && workOrderEstimate.getEstimate().getDepositCode() != null) {
                validateExpenditureForDepositCode(actionName);
                validateBudgetHeadForBillsInWorkflow();
            }
        }

        ContractorBillRegister contractorBill = null;
        try {
            contractorBill = setEgBillRegister();
        } catch (final ValidationException sequenceException) {
            final List<ValidationError> errorList = sequenceException.getErrors();
            for (final ValidationError error : errorList)
                if (error.getMessage().contains("DatabaseSequenceFirstTimeException")) {
                    prepare();
                    throw new ValidationException(Arrays.asList(new ValidationError("error", error.getMessage())));
                }
            throw sequenceException;
        }

        // Story #806 - IF the budget check is enabled to the CWIP code then
        // budget check should happen for Deposit works
        // if(!skipBudget)
        final User user = userService.getUserById(worksService.getCurrentLoggedInUserId());
        if (contractorBillRegister.getStatus() == null
                || contractorBillRegister.getStatus() != null
                        && (contractorBillRegister.getStatus().getCode().equalsIgnoreCase(WorksConstants.NEW)
                                || contractorBillRegister
                                        .getStatus().getCode().equalsIgnoreCase(WorksConstants.REJECTED)
                                        && user == contractorBillRegister.getCreatedBy())) {
            if (logger.isDebugEnabled())
                logger.debug("entered condition for budgetary appropriation >>>>>>>>>>>>>> ");
            isSkipBudgetCheck();
            checkBudgetandGenerateNumber(contractorBill);
        }

        if (contractorBillRegister.getStatus() == null)
            contractorBillRegister.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(BILL_MODULE_KEY,
                    WorksConstants.NEW));
        contractorBillService.persist(contractorBillRegister);
        if (contractorBillRegister.getBilltype().equals(contractorBillService.getBillType().get(1).toString())) {
            workOrderEstimate.setWorkCompletionDate(getCompletionDate());
            // workOrder.setWorkCompletionDate(getCompletionDate());
            contractorBillRegister.setBillSequenceNumber(null);
            int i = 0;
            for (final WorkCompletionDetailInfo workCompletionDetailInfo : getWorkCompletionDetailInfo()) {
                if (getRemarks() != null && getRemarks().length > i && getRemarks()[i] != null)
                    workCompletionDetailInfo.getWorkOrderActivity().setRemarks(getRemarks()[i]);
                i++;
            }
        }
        for (final MBHeader mbObj : mbHeaderList)
            mbObj.setEgBillregister(contractorBillRegister);
        if (appConfigValueId != null && selectedchecklistValue != null && appConfigValueId.length > 0
                && selectedchecklistValue.length > 0)
            for (int i = 0; i < appConfigValueId.length; i++)
                if (appConfigValueId[i] != null && !selectedchecklistValue[i].equals("-1")) {
                    final EgChecklists checklist = new EgChecklists();
                    checklist.setAppconfigvalue((AppConfigValues) getPersistenceService().find(
                            "from AppConfigValues where id=?", Integer.valueOf(appConfigValueId[i].toString())));
                    checklist.setChecklistvalue(selectedchecklistValue[i]);
                    checklist.setObjectid(contractorBillRegister.getId());
                    checklistService.persist(checklist);
                }
        /*
         * contractorBillRegister = workflowService.transition(actionName, contractorBillRegister,
         * contractorBillRegister.getWorkflowapproverComments());
         */

        // TODO - Need to know alternative for how to find previous when
        // workflow ended
        /*
         * if(contractorBillRegister.getCurrentState()!=null && contractorBillRegister.getCurrentState().getPrevious()!=null &&
         * contractorBillRegister.getCurrentState().getValue()!=null && (APPROVED
         * .equalsIgnoreCase(contractorBillRegister.getCurrentState().getPrevious ().getValue()) ||
         * CANCELLED.equalsIgnoreCase(contractorBillRegister.getCurrentState ().getPrevious().getValue()))){
         * messageKey="bill.approved"; contractorBillRegister .setBillstatus(contractorBillRegister.getCurrentState
         * ().getPrevious().getValue()); contractorBillRegister.setStatus(commonsService
         * .getStatusByModuleAndCode(BILL_MODULE_KEY, contractorBillRegister.getCurrentState().getPrevious().getValue())); } else
         */ {
            messageKey = "bill.save.success";
            contractorBillRegister.setBillstatus(contractorBillRegister.getCurrentState().getValue());
            contractorBillRegister.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(BILL_MODULE_KEY,
                    contractorBillRegister.getCurrentState().getValue()));
        }
        getPersistenceService().getSession().flush();
        getPersistenceService().getSession().refresh(contractorBillRegister);
        contractorBillRegister.getEgBillregistermis().setSourcePath(
                "/egworks/contractorBill/contractorBill!edit.action?id=" + contractorBillRegister.getId()
                        + "&workOrderId=" + workOrderId + "&billnumber=" + contractorBillRegister.getBillnumber()
                        + "&sourcepage=search");

        addActionMessage(getText(messageKey, messageKey));
        getDesignation(contractorBillRegister);

        if (SAVE_ACTION.equals(actionName))
            sourcepage = "inbox";
        return SAVE_ACTION.equals(actionName) ? edit() : SUCCESS;
    }

    private String isSkipBudgetCheck() {
        final List<AppConfigValues> appConfigValuesList = worksService.getAppConfigValue("Works", APPCONFIG_KEY_NAME);
        logger.info("lenght of appconfig values>>>>>> " + appConfigValuesList.size());
        if (workOrderEstimate != null && workOrderEstimate.getId() != null) {
            for (final AppConfigValues appValues : appConfigValuesList)
                if (appValues.getValue().equals(workOrderEstimate.getEstimate().getNatureOfWork().getName())) {
                    skipBudget = true;
                    return CHECK_BUDGET;
                }
            skipBudget = false;
        }
        return CHECK_BUDGET;
    }

    private void validateBudgetHeadForBillsInWorkflow() {
        if (StringUtils.isNotBlank(allowForward) && allowForward.equalsIgnoreCase(WorksConstants.NO)) {
            populateBudgetHeadForDepositWorksEstimate();
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "contractoBill.depositCOA.budgetHead.mapping.workflow.error", getText(
                            "contractoBill.depositCOA.budgetHead.mapping.workflow.error",
                            new String[] { workOrderEstimate.getEstimate().getEstimateNumber() }))));
        }
    }

    private ContractorBillRegister checkBudgetandGenerateNumber(final ContractorBillRegister contractorBill) {
        final ScriptContext scriptContext = ScriptService.createContext("voucherService", voucherService, "bill",
                contractorBill);
        scriptExecutionService.executeScript("egf.bill.budgetcheck", scriptContext);
        return contractorBill;
    }

    public void getDesignation(final ContractorBillRegister bill) {
        if (contractorBillRegister.getCurrentState() != null
                && !"NEW".equalsIgnoreCase(bill.getCurrentState().getValue())) {
            final String result = worksService.getEmpNameDesignation(contractorBillRegister.getState()
                    .getOwnerPosition(), contractorBillRegister.getState().getCreatedDate());
            if (result != null && !"@".equalsIgnoreCase(result)) {
                final String empName = result.substring(0, result.lastIndexOf('@'));
                final String designation = result.substring(result.lastIndexOf('@') + 1, result.length());
                setNextEmployeeName(empName);
                setNextDesignation(designation);
            }
        }
    }

    public String cancel() {
        parameters.get("actionName");
        if (contractorBillRegister.getId() != null) {
            /*
             * contractorBillRegister = workflowService.transition(actionName, contractorBillRegister,
             * contractorBillRegister.getWorkflowapproverComments());
             */
            contractorBillRegister.setBillstatus(WorksConstants.CANCELLED_STATUS);
            contractorBillRegister.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(BILL_MODULE_KEY,
                    WorksConstants.CANCELLED_STATUS));
            contractorBillService.persist(contractorBillRegister);
            final List<MBHeader> mbHeaderListForBillIdForCancellingBill = measurementBookService.findAllByNamedQuery(
                    "getAllMBsForBillId", WorksConstants.APPROVED, contractorBillRegister.getId());
            if (StringUtils.isNotBlank(isRebatePremLevelBill) && isRebatePremLevelBill.equals("yes")
                    && tenderResponse.getTenderEstimate().getTenderType().equals(getPercTenderType())
                    || isRCEstimate.equalsIgnoreCase(WorksConstants.YES)) {
                contractorBillRegister.setPassedamount(grossAmount);
                contractorBillRegister.setBillamount(grossAmount);
            } else
                contractorBillRegister.setPassedamount(contractorBillRegister.getBillamount());
            for (final MBHeader mbObj : mbHeaderListForBillIdForCancellingBill) {
                final MBForCancelledBill mbCB = new MBForCancelledBill();
                mbCB.setEgBillregister(mbObj.getEgBillregister());
                mbCB.setMbHeader(mbObj);
                cancelBillService.persist(mbCB);

            }

        }
        return SUCCESS;
    }

    @Override
    public void validate() {
        String actionName = "";
        if (parameters.get(ACTION_NAME) != null && parameters.get(ACTION_NAME)[0] != null)
            actionName = parameters.get(ACTION_NAME)[0];

        if (!(actionName.equalsIgnoreCase("reject") || actionName.equalsIgnoreCase("cancel"))) {
            if (contractorBillRegister.getBilltype().equals("-1"))
                addFieldError("billType", getText("billType.not.found"));
            else if (contractorBillRegister.getBilltype().equals(contractorBillService.getBillType().get(1).toString())
                    && completionDate == null)
                addFieldError("completionDate", getText("completionDate.not.found"));
            if (StringUtils.isBlank(partyBillNumber))
                addFieldError("partyBillNumber", getText("partyBillNumber.not.found"));
            if (!worksService.checkBigDecimalValue(contractorBillRegister.getBillamount(), BigDecimal.valueOf(0.00)))
                addFieldError("billamount", getText("no.pending.bills"));
            if (partyBillDate == null)
                addFieldError("partyBillDate", getText("partyBillDate.not.found"));
            if (!DateUtils.compareDates(new Date(), partyBillDate))
                addFieldError("partyBillDate", getText("partyBillDate.greaterthan.currentDate"));
            if (!DateUtils.compareDates(contractorBillRegister.getBilldate(), completionDate))
                addFieldError("completionDate", getText("billdate.lessthan.completionDate"));
            if (!DateUtils.compareDates(contractorBillRegister.getBilldate(), workOrder.getWorkOrderDate()))
                addFieldError("workorderdate", getText("billdate.lessthan.workorderdate"));
            if (contractorBillRegister.getId() == null)
                if (!DateUtils.compareDates(contractorBillRegister.getBilldate(), partyBillDate))
                    addFieldError("partyBillDate", getText("partybilldate.greaterthan.billdate"));
            if (contractorBillRegister.getId() == null)
                validateMBAmountAndBillAmount(mbHeaderList);
            if (contractorBillRegister.getStatus() != null
                    && contractorBillRegister.getStatus().getCode().equalsIgnoreCase(WorksConstants.NEW))
                validateMBAmountAndBillAmount(mbHeaderListForBillId);
            // validation to avoid duplication of bill generation
            if (contractorBillRegister.getId() == null && contractorBillRegister.getStatus() == null)
                if (mbHeaderId != null && mbHeaderId.length > 0) {
                    final List<MBHeader> mbHeaderWithBill = new ArrayList<MBHeader>();

                    for (final Long mbId : mbHeaderId) {
                        final MBHeader mbHeader = measurementBookService.findById(mbId, false);
                        if (mbHeader.getEgBillregister() != null
                                && !mbHeader.getEgBillregister().getStatus().getCode().equalsIgnoreCase("CANCELLED"))
                            mbHeaderWithBill.add(mbHeader);
                    }

                    final StringBuilder billStr = new StringBuilder(200);
                    final StringBuilder mbStr = new StringBuilder(200);
                    for (final MBHeader mbh : mbHeaderWithBill) {
                        if (!billStr.toString().contains(mbh.getEgBillregister().getBillnumber()))
                            billStr.append(mbh.getEgBillregister().getBillnumber()).append(",");
                        mbStr.append(mbh.getMbRefNo()).append(",");
                        if (mbHeaderWithBill.size() == 1)
                            addActionError(getText("contractorBill.uniqueCheck.message1", new String[] {
                                    mbh.getEgBillregister().getBillnumber(), mbh.getMbRefNo() }));
                    }
                    if (mbHeaderWithBill.size() > 1)
                        addActionError(getText("contractorBill.uniqueCheck.message2", new String[] {
                                billStr.toString(), mbStr.toString() }));
                }
            if (contractorBillRegister.getId() == null) {
                Date latestBillDate = null;
                if (workOrderEstimate != null && workOrderEstimate.getId() != null)
                    latestBillDate = measurementBookService.getLatestBillDateForMBPassingWOEstimate(workOrderEstimate
                            .getId());
                else
                    latestBillDate = measurementBookService.getLatestBillDateForMB(workOrderId);
                if (latestBillDate != null
                        && !DateUtils.compareDates(contractorBillRegister.getBilldate(), latestBillDate))
                    addFieldError("billdate", getText("billdate.lessthan.olderbill"));

            }
            if (Long.valueOf(0).equals(netPayableCode))
                addFieldError("netPayableCode", getText("netPayableCode.not.found"));
            if (!worksService.checkBigDecimalValue(netPayableAmount, BigDecimal.valueOf(0.00)))
                addFieldError("netPayableAmount", getText("netPayableAmount.lessthan.zero"));
            validateAssetDetails();
            validateStatutoryDeduction();
            validateStandardDeduction();
            validateCustomDeduction();

            // Story# 806 - Application should not check for deposit code
            // appropriation
            /*
             * String actionName = ""; if (parameters.get(ACTION_NAME) != null && parameters.get(ACTION_NAME)[0] != null)
             * actionName = parameters.get(ACTION_NAME)[0]; if(skipBudget && ((contractorBillRegister.getStatus()==null &&
             * !actionName.equalsIgnoreCase("save")) || (contractorBillRegister.getStatus()!=null && (contractorBillRegister
             * .getStatus().getCode().equalsIgnoreCase("NEW")|| contractorBillRegister
             * .getStatus().getCode().equalsIgnoreCase("REJECTED")) && (!actionName.equalsIgnoreCase("save") &&
             * !actionName.equalsIgnoreCase("Cancel"))))){ validateGlcodeBalForDepositWorks(); }
             */
            if (!checkForCOADuplicatesInAccDet())
                addFieldError("accountcode", getText("duplicate.accountcode"));
            if (!checkForCOADuplicatesInCustomDed())
                addFieldError("glcodeId", getText("duplicate.glcodeId"));
            // to re-populate the form data if any field error- only for edit
            // mode
            if (id == null
                    || contractorBillRegister.getStatus() != null
                            && (contractorBillRegister.getStatus().getCode().equalsIgnoreCase(WorksConstants.NEW)
                                    || contractorBillRegister
                                            .getStatus().getCode().equalsIgnoreCase(WorksConstants.REJECTED))
                            && StringUtils.isNotBlank(contractorBillRegister.getBilltype())
                            && contractorBillRegister.getBilltype().equalsIgnoreCase(WorksConstants.FINAL_BILL))
                if (latestMBDate != null && completionDate != null && latestMBDate.after(completionDate))
                    addFieldError("work.compltion.date.less.than.lstMB.date",
                            getText("work.compltion.date.less.than.lstMB.date") + " " + refNo + ", "
                                    + new SimpleDateFormat("dd/MM/yyyy").format(latestMBDate)
                                    + getText("pls.enter.valid.date"));
            if (id != null && !getFieldErrors().isEmpty())
                try {
                    edit();
                } catch (final Exception e) {
                    logger.error(e.getMessage());

                }
        }
    }

    private void validateARFInWorkflow() {
        final AjaxContractorBillAction ajaxContractorBillAction = new AjaxContractorBillAction();
        ajaxContractorBillAction.setPersistenceService(getPersistenceService());
        ajaxContractorBillAction.setEmployeeService(employeeService);
        ajaxContractorBillAction.setContractorAdvanceService(contractorAdvanceService);
        ajaxContractorBillAction.setWorkOrderEstimateId(workOrderEstimate.getId());
        ajaxContractorBillAction.advanceRequisitionInWorkflowCheck();

        if (ajaxContractorBillAction.getArfInWorkFlowCheck().equalsIgnoreCase("invalid"))
            throw new ValidationException(Arrays.asList(new ValidationError("arf.workflow.verification.for.bill.msg",
                    getText("arf.workflow.verification.for.bill.msg",
                            new String[] { ajaxContractorBillAction.getAdvanceRequisitionNo(),
                                    ajaxContractorBillAction.getEstimateNo(), ajaxContractorBillAction.getOwner() }))));
    }

    private void validateExpenditureForDepositCode(final String actionName) {
        if (workOrderEstimate.getEstimate() != null && workOrderEstimate.getEstimate().getDepositCode() != null)
            if (StringUtils.isNotBlank(actionName)
                    && (actionName.equalsIgnoreCase(WorksConstants.ACTION_SUBMIT_FOR_APPROVAL)
                            || actionName.equalsIgnoreCase(WorksConstants.ACTION_APPROVAL) || actionName
                                    .equalsIgnoreCase(WorksConstants.ACTION_APPROVE))) {
                final FinancialDetail financialDetails = workOrderEstimate.getEstimate().getFinancialDetails().get(0);
                if (financialDetails != null) {
                    BigDecimal expenditureAmount = BigDecimal.ZERO;
                    BigDecimal totalExpAmount = BigDecimal.ZERO;
                    final Fund fund = financialDetails.getFund();
                    final CChartOfAccounts coa = financialDetails.getCoa();
                    final Accountdetailtype accountdetailtype = (Accountdetailtype) persistenceService.find(
                            "from Accountdetailtype where name=?", "DEPOSITCODE");
                    if (fund != null && fund.getId() != null && coa != null && coa.getId() != null
                            && workOrderEstimate.getEstimate().getDepositCode().getId() != null) {
                        final BigDecimal totalAmountDeposited = egovCommon.getDepositAmountForDepositCode(new Date(),
                                coa.getGlcode(), fund.getCode(), accountdetailtype.getId(), workOrderEstimate
                                        .getEstimate().getDepositCode().getId().intValue());
                        final List<Integer> projectCodeIdsList = contractorBillService
                                .getProjCodeIdsListForDepositCode(fund.getId(), coa.getId(), workOrderEstimate
                                        .getEstimate().getDepositCode().getId());
                        if (!projectCodeIdsList.isEmpty())
                            expenditureAmount = contractorBillService.getTotalExpenditure(projectCodeIdsList,
                                    WorksConstants.PROJECTCODE);
                        // In case id is null, include the current bill amount
                        if (id == null)
                            totalExpAmount = expenditureAmount.add(contractorBillRegister.getBillamount());
                        else
                            totalExpAmount = expenditureAmount;
                        final BigDecimal finalExpenditureAmount = totalExpAmount.setScale(2, RoundingMode.HALF_UP);
                        final BigDecimal totalDepositedAmount = totalAmountDeposited.setScale(2, RoundingMode.HALF_UP);

                        if (totalDepositedAmount.compareTo(finalExpenditureAmount) == -1) {
                            populateBudgetHeadForDepositWorksEstimate();
                            throw new ValidationException(Arrays.asList(new ValidationError(
                                    "contractoBill.dwEstimate.expenditure.amount.insufficient", getText(
                                            "contractoBill.dwEstimate.expenditure.amount.insufficient",
                                            new String[] { workOrderEstimate.getEstimate().getDepositCode().getCode(),
                                                    NumberUtil.formatNumber(finalExpenditureAmount),
                                                    NumberUtil.formatNumber(totalDepositedAmount) }))));
                        }
                    }
                }
            }
    }

    private void validateMBAmountAndBillAmount(final List<MBHeader> mbHeaderList) {
        BigDecimal totalMBAmount = BigDecimal.ZERO;
        for (final MBHeader mbh : mbHeaderList)
            totalMBAmount = totalMBAmount.add(mbh.getTotalMBAmount());

        if (!(billAmount.setScale(2, RoundingMode.HALF_UP).compareTo(totalMBAmount.setScale(2, RoundingMode.HALF_UP)) == 0))
            addFieldError("billAmount.compare.total.mbAmount", getText("billAmount.compare.total.mbAmount"));
    }

    protected void validateStandardDeduction() {
        for (final DeductionTypeForBill stanDetails : standardDeductions)
            if (stanDetails != null) {
                if (stanDetails.getDeductionType() == null || stanDetails.getGlcodeid().equals(BigDecimal.valueOf(0)))
                    addFieldError("standardDeductions", getText("please.select.standardDeductions"));
                if (!worksService.checkBigDecimalValue(stanDetails.getCreditamount(), BigDecimal.valueOf(0.00)))
                    addFieldError("amount", getText(AMOUNT_ERROR));
            }
    }

    protected void validateCustomDeduction() {
        for (final EgBilldetails customDetails : customDeductions)
            if (customDetails != null) {
                if (customDetails.getGlcodeid().equals(BigDecimal.valueOf(0)))
                    addFieldError("customDeductions", getText("please.select.customDeductions"));
                if (!worksService.checkBigDecimalValue(customDetails.getCreditamount(), BigDecimal.valueOf(0.00)))
                    addFieldError("customDeductions.amount", getText(AMOUNT_ERROR));
            }
    }

    protected void validateRetentionDeduction() {
        for (final EgBilldetails retentionMoney : retentionMoneyDeductions)
            if (retentionMoney != null) {
                if (retentionMoney.getGlcodeid().equals(BigDecimal.valueOf(0)))
                    addFieldError("customDeductions", getText("please.select.retentionMoney"));
                if (!worksService.checkBigDecimalValue(retentionMoney.getCreditamount(), BigDecimal.valueOf(0.00)))
                    addFieldError("retentionMoney.amount", getText(AMOUNT_ERROR));
            }
    }

    protected void validateAssetDetails() {
        for (final AssetForBill adb : accountDetailsForBill) {
            if (workOrderEstimate.getAssetValues().isEmpty()) {
                if (adb != null && (adb.getCoa() == null || adb.getCoa().getId() == 0L))
                    addFieldError("coa", getText("please.select.asset"));
            } else if (adb != null && (adb.getAsset() == null || adb.getAsset().getId() == 0L)
                    && (adb.getCoa() == null || adb.getCoa().getId() == 0L))
                addFieldError("asset", getText("please.select.asset"));
            if (adb != null && !worksService.checkBigDecimalValue(adb.getAmount(), BigDecimal.valueOf(0.00)))
                addFieldError("asset.amount", getText(AMOUNT_ERROR));
        }
    }

    protected void validateStatutoryDeduction() {
        for (final StatutoryDeductionsForBill adb : actionStatutorydetails)
            if (adb != null) {
                if (adb.getEgBillPayeeDtls().getRecovery() == null
                        || adb.getEgBillPayeeDtls().getRecovery().getId() == 0)
                    addFieldError("actionStatutorydetails", getText("please.select.actionStatutorydetails"));
                if (!worksService.checkBigDecimalValue(adb.getEgBillPayeeDtls().getCreditAmount(),
                        BigDecimal.valueOf(0.00)))
                    addFieldError("actionStatutorydetails.amount", getText(AMOUNT_ERROR));
            }
    }

    public boolean checkForCOADuplicatesInAccDet() {
        final Collection<AssetForBill> assetAndAccountDetails = contractorBillService
                .getAssetAndAccountDetails(accountDetailsForBill);
        if (!assetAndAccountDetails.isEmpty() && workOrderEstimate.getAssetValues().isEmpty()) {
            final Set<Long> coaSet = new HashSet<Long>();
            for (final AssetForBill assetForBill : assetAndAccountDetails)
                if (assetForBill.getCoa() != null && assetForBill.getCoa().getId() > 0L) {
                    if (coaSet.contains(assetForBill.getCoa().getId()))
                        return false;
                    coaSet.add(assetForBill.getCoa().getId());
                }
        }
        return true;
    }

    public boolean checkForCOADuplicatesInCustomDed() {
        final Collection<EgBilldetails> customDeductionTypes = contractorBillService.getCustomDeductionTypes(customDeductions);
        if (!customDeductionTypes.isEmpty()) {
            final Set<BigDecimal> coaSet = new HashSet<BigDecimal>();
            for (final EgBilldetails egBilldetails : customDeductionTypes)
                if (egBilldetails.getGlcodeid() != null
                        && worksService.checkBigDecimalValue(egBilldetails.getGlcodeid(), BigDecimal.valueOf(0))) {
                    if (coaSet.contains(egBilldetails.getGlcodeid()))
                        return false;
                    coaSet.add(egBilldetails.getGlcodeid());
                }
        }
        return true;
    }

    private ContractorBillRegister setEgBillRegister() throws Exception {
        boolean shouldAddAccountDetails = false;
        if (contractorBillRegister.getStatus() == null
                || WorksConstants.REJECTED.equalsIgnoreCase(contractorBillRegister.getStatus().getCode())
                        && StringUtils.isBlank(contractorBillRegister.getState().getNextAction())
                || NEW.equalsIgnoreCase(contractorBillRegister.getStatus().getCode()))
            shouldAddAccountDetails = true;
        if (contractorBillRegister.getBillnumber() == null)
            contractorBillRegister.setBillnumber(contractorBillService.generateContractorBillNumber(
                    contractorBillRegister));
        contractorBillRegister.setBillstatus(BILL_STATUS);
        if (StringUtils.isNotBlank(isRebatePremLevelBill) && isRebatePremLevelBill.equals("yes")
                && tenderResponse.getTenderEstimate().getTenderType().equals(getPercTenderType())
                || isRCEstimate.equalsIgnoreCase(WorksConstants.YES)) {
            contractorBillRegister.setPassedamount(grossAmount);
            contractorBillRegister.setBillamount(grossAmount);
        } else
            contractorBillRegister.setPassedamount(contractorBillRegister.getBillamount());
        contractorBillRegister.setExpendituretype(EXPENDITURE_TYPE);
        contractorBillRegister.setWorkordernumber(workOrder.getWorkOrderNumber());
        contractorBillRegister.setWorkorderdate(workOrder.getWorkOrderDate());
        if (advaceAdjustmentCreditAmount.intValue() > 0)
            contractorBillRegister.setAdvanceadjusted(advaceAdjustmentCreditAmount);
        final List<FinancialDetail> fdList = financialDetailService.findAllByNamedQuery(
                "FINANCIALDETAILS_BY_ESTIMATEID", workOrderEstimate.getEstimate().getId());
        contractorBillRegister.setEgBillregistermis(setEgBillregistermis(fdList));
        if (shouldAddAccountDetails)
            addAccountDetails(fdList);
        return contractorBillRegister;
    }

    private void addAccountDetails(final List<FinancialDetail> fdList) throws Exception {
        final Map<String, BigDecimal> debitAmountMap = getGlcodesForDebit();
        for (final Map.Entry<String, BigDecimal> entry : debitAmountMap.entrySet())
            contractorBillRegister.getEgBilldetailes().add(
                    getBillDetailsRegister(contractorBillRegister, fdList, entry.getKey(), entry.getValue(), true,
                            false));
        final Map<String, BigDecimal> creditAmountMap = getGlcodesForstanDed();
        for (final Map.Entry<String, BigDecimal> entry : creditAmountMap.entrySet())
            contractorBillRegister.getEgBilldetailes().add(
                    getBillDetailsRegister(contractorBillRegister, fdList, entry.getKey(), entry.getValue(), false,
                            false));
        for (final EgBilldetails rbillDetails : contractorBillService.getRetentionMoneyTypes(retentionMoneyDeductions))
            if (rbillDetails != null
                    && worksService.checkBigDecimalValue(rbillDetails.getGlcodeid(), BigDecimal.valueOf(0))) {
                final EgBilldetails egbillDetails = getBillDetailsRegister(contractorBillRegister, fdList, rbillDetails
                        .getGlcodeid().toString(), rbillDetails.getCreditamount(), false, false);
                egbillDetails.setNarration(rbillDetails.getNarration());
                contractorBillRegister.getEgBilldetailes().add(egbillDetails);
            }
        for (final EgBilldetails billDetails : customDeductions)
            if (billDetails != null
                    && worksService.checkBigDecimalValue(billDetails.getGlcodeid(), BigDecimal.valueOf(0))) {
                final EgBilldetails egbillDetails = getBillDetailsRegister(contractorBillRegister, fdList, billDetails
                        .getGlcodeid().toString(), billDetails.getCreditamount(), false, false);
                egbillDetails.setNarration(billDetails.getNarration());
                contractorBillRegister.getEgBilldetailes().add(egbillDetails);
            }
        final CChartOfAccounts advanceCOA = contractorAdvanceService
                .getContractorAdvanceAccountcodeForWOE(workOrderEstimate.getId());
        if (advaceAdjustmentCreditAmount != null && advaceAdjustmentCreditAmount.intValue() > 0 && advanceCOA != null)
            contractorBillRegister.getEgBilldetailes().add(
                    getBillDetailsRegister(contractorBillRegister, fdList, advanceCOA.getId().toString(),
                            advaceAdjustmentCreditAmount, false, false));
        final Map<String, BigDecimal> creditAmountStatMap = getGlcodesForStatDed();
        for (final Map.Entry<String, BigDecimal> entry : creditAmountStatMap.entrySet())
            contractorBillRegister.getEgBilldetailes().add(
                    getBillDetailsRegister(contractorBillRegister, fdList, entry.getKey(), entry.getValue(), false,
                            true));

        contractorBillRegister.getEgBilldetailes().add(
                getBillDetailsRegister(contractorBillRegister, fdList, getNetPayableCode().toString(),
                        getNetPayableAmount(), false, false));
    }

    private EgBilldetails getBillDetailsRegister(final ContractorBillRegister billregister,
            final List<FinancialDetail> fdList, final String glcode, final BigDecimal amount, final boolean isDebit,
            final boolean isTds) throws Exception {
        final EgBilldetails billDetails = new EgBilldetails();
        if (fdList != null && !fdList.isEmpty() && fdList.get(0).getFunction() != null
                && fdList.get(0).getFunction().getId() != null) {
            final CFunction fun = functionHibernateDao.getFunctionById(fdList.get(0).getFunction().getId());
            billDetails.setFunctionid(BigDecimal.valueOf(fun.getId()));
        }
        CChartOfAccounts coa = null;
        if (StringUtils.isNotBlank(glcode) && Long.parseLong(glcode) > 0)
            coa = chartOfAccountsHibernateDAO.findById(Long.valueOf(glcode), false);
        if (coa != null && coa.getId() != null)
            billDetails.setGlcodeid(BigDecimal.valueOf(coa.getId()));
        if (isDebit)
            billDetails.setDebitamount(amount);
        else
            billDetails.setCreditamount(amount);
        billDetails.setEgBillregister(billregister);
        if (coa != null && coa.getGlcode() != null) {
            final List<Accountdetailtype> detailCode = chartOfAccountsHibernateDAO
                    .getAccountdetailtypeListByGLCode(coa.getGlcode());
            if (detailCode != null && !detailCode.isEmpty()) {
                Accountdetailtype adt1 = null;
                final Accountdetailtype adt2 = null;
                Accountdetailtype adt3 = null;
                if (isDebit)     // Story# 806 - Enable code to create an contractor
                // bill
                // with project code as sub-ledger for the debit COA for
                // Deposit Works
                /*
                 * if(skipBudget) { adt2 = chartOfAccountsHibernateDAO.getAccountDetailTypeIdByName (coa.getGlcode(),
                 * ACCOUNTDETAIL_TYPE_DEPOSITCODE); if(adt2!=null){ if(isTds) addTdsDetails(amount, isDebit, isTds, billDetails,
                 * adt2, Integer.valueOf(workOrderEstimate .getEstimate().getDepositCode().getId().toString())); else
                 * billDetails.getEgBillPaydetailes().add(getEgPayeeDetails (billDetails
                 * ,adt2.getId(),amount,isDebit,false,null,null,Integer .valueOf
                 * (workOrderEstimate.getEstimate().getDepositCode(). getId().toString()))); } } else
                 */ {
                    adt3 = chartOfAccountsHibernateDAO.getAccountDetailTypeIdByName(coa.getGlcode(),
                            ACCOUNTDETAIL_TYPE_PROJECTCODE);
                    if (adt3 != null)
                        if (isTds)
                            addTdsDetails(amount, isDebit, isTds, billDetails, adt3, Integer.valueOf(workOrderEstimate
                                    .getEstimate().getProjectCode().getId().toString()));
                        else
                            billDetails.getEgBillPaydetailes().add(
                                    getEgPayeeDetails(
                                            billDetails,
                                            adt3.getId(),
                                            amount,
                                            isDebit,
                                            false,
                                            null,
                                            null,
                                            Integer.valueOf(workOrderEstimate.getEstimate().getProjectCode().getId()
                                                    .toString())));
                } else {
                    adt1 = chartOfAccountsHibernateDAO.getAccountDetailTypeIdByName(coa.getGlcode(),
                            ACCOUNTDETAIL_TYPE_CONTRACTOR);
                    if (adt1 != null)
                        if (isTds)
                            addTdsDetails(amount, isDebit, isTds, billDetails, adt1,
                                    Integer.valueOf(workOrder.getContractor().getId().toString()));
                        else
                            billDetails.getEgBillPaydetailes().add(
                                    getEgPayeeDetails(billDetails, adt1.getId(), amount, isDebit, false, null, null,
                                            Integer.valueOf(workOrder.getContractor().getId().toString())));

                }
                if (adt1 == null && adt2 == null && adt3 == null) {
                    final List<ValidationError> errors = new ArrayList<ValidationError>();
                    errors.add(new ValidationError("contractorBill.validate_glcode_for_subledger", getText(
                            "contractorBill.validate_glcode_for_subledger", new String[] { coa.getGlcode() })));
                    throw new ValidationException(errors);
                }
            }
        }
        return billDetails;
    }

    private void addTdsDetails(final BigDecimal amount, final boolean isDebit, final boolean isTds,
            final EgBilldetails billDetails, final Accountdetailtype adt, final Integer adkId) {
        for (final StatutoryDeductionsForBill payeeDetails : getActionStatutorydetails())
            if (payeeDetails != null
                    && payeeDetails.getEgBillPayeeDtls().getRecovery() != null
                    && payeeDetails.getEgBillPayeeDtls().getRecovery().getId() != null
                    && payeeDetails.getEgBillPayeeDtls().getRecovery().getChartofaccounts() != null
                    && payeeDetails.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId() != null
                    && payeeDetails.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId()
                            .equals(Long.valueOf(billDetails.getGlcodeid().toString()))) {
                // Added for card 1078 : Automatic calculation of statutory
                // recoveries in bill
                final EgBillPayeedetails egPayeeDtls = getEgPayeeDetails(billDetails, adt.getId(), amount, isDebit,
                        isTds, payeeDetails.getEgBillPayeeDtls().getRecovery().getId(), payeeDetails
                                .getEgBillPayeeDtls().getNarration(),
                        adkId);
                billDetails.getEgBillPaydetailes().add(egPayeeDtls);
                addStatutoryDeductionsForBill(payeeDetails, egPayeeDtls);
            }
    }

    private void addStatutoryDeductionsForBill(final StatutoryDeductionsForBill statutoryDeductionBill,
            final EgBillPayeedetails egPayeeDtls) {
        final StatutoryDeductionsForBill sdb = new StatutoryDeductionsForBill();
        if (statutoryDeductionBill.getSubPartyType().getId() != null)
            sdb.setSubPartyType((EgPartytype) persistenceService.find("from EgPartytype where id=?",
                    statutoryDeductionBill.getSubPartyType().getId()));
        if (statutoryDeductionBill.getTypeOfWork().getId() != null)
            sdb.setTypeOfWork((EgwTypeOfWork) persistenceService.find("from EgwTypeOfWork where id=?",
                    statutoryDeductionBill.getTypeOfWork().getId()));
        sdb.setEgBillPayeeDtls(egPayeeDtls);
        sdb.setEgBillReg(contractorBillRegister);
        contractorBillRegister.addStatutoryDeductions(sdb);
    }

    private EgBillPayeedetails getEgPayeeDetails(final EgBilldetails billDetails, final Integer adtId,
            final BigDecimal amount, final boolean isDebit, final boolean isTds, final Long tdsId,
            final String narration, final Integer adkId) {
        final EgBillPayeedetails egBillPaydetail = new EgBillPayeedetails();
        egBillPaydetail.setAccountDetailKeyId(adkId);
        egBillPaydetail.setAccountDetailTypeId(adtId);
        if (isDebit)
            egBillPaydetail.setDebitAmount(amount);
        else
            egBillPaydetail.setCreditAmount(amount);
        if (isTds)
            egBillPaydetail.setRecovery(recoveryService.getTdsById(tdsId));
        if (narration != null)
            egBillPaydetail.setNarration(narration);
        egBillPaydetail.setEgBilldetailsId(billDetails);
        return egBillPaydetail;
    }

    private EgBillregistermis setEgBillregistermis(final List<FinancialDetail> fdList) throws ApplicationException {
        EgBillregistermis egBillRegisterMis = null;
        if (id == null)
            egBillRegisterMis = new EgBillregistermis();
        else
            egBillRegisterMis = contractorBillRegister.getEgBillregistermis();
        egBillRegisterMis.setEgBillregister(contractorBillRegister);
        egBillRegisterMis.setPayto(workOrder.getContractor().getName());
        egBillRegisterMis.setFieldid(workOrderEstimate.getEstimate().getWard());
        egBillRegisterMis.setPartyBillDate(partyBillDate);
        egBillRegisterMis.setPartyBillNumber(partyBillNumber);
        if (fdList != null && !fdList.isEmpty()) {
            if (fdList.get(0).getFund() != null && fdList.get(0).getFund().getId() != null)
                egBillRegisterMis.setFund(fundHibernateDao.fundById(fdList.get(0).getFund().getId(), false));
            if (fdList.get(0).getFunctionary() != null && fdList.get(0).getFunctionary().getId() != null)
                egBillRegisterMis.setFunctionaryid(functionaryHibDao.functionaryById(fdList.get(0).getFunctionary()
                        .getId().intValue()));
            if (fdList.get(0).getScheme() != null && fdList.get(0).getScheme().getId() != null)
                egBillRegisterMis.setScheme(schemeHibernateDAO.getSchemeById(fdList.get(0).getScheme().getId()));
            if (fdList.get(0).getSubScheme() != null && fdList.get(0).getSubScheme().getId() != null)
                egBillRegisterMis.setSubScheme(subschemeHibernateDAO.getSubSchemeById(fdList.get(0).getSubScheme().getId()));
            if (!fdList.get(0).getFinancingSources().isEmpty())
                egBillRegisterMis.setFundsource(fdList.get(0).getFinancingSources().get(0).getFundSource());
            if (fdList.get(0).getFunction() != null)
                egBillRegisterMis.setFunction(fdList.get(0).getFunction());
        }
        egBillRegisterMis.setEgDepartment(workOrderEstimate.getEstimate().getUserDepartment());
        final String finyearId = finHibernateDao.getFinancialYearByDate(contractorBillRegister.getBilldate()).getId()
                .toString();
        egBillRegisterMis.setFinancialyear(finHibernateDao.getFinancialYearById(Long.valueOf(finyearId)));
        egBillRegisterMis.setLastupdatedtime(new Date());
        return egBillRegisterMis;
    }

    public Map<String, BigDecimal> getGlcodesForDebit() {
        final Map<String, BigDecimal> debitGlcodeAndAmountMap = new HashMap<String, BigDecimal>();
        final Set<Long> coaSet = new HashSet<Long>();
        for (final AssetForBill assetBill : accountDetailsForBill)
            if (assetBill != null && assetBill.getCoa() != null && assetBill.getCoa().getId() != null) {
                if (coaSet.contains(assetBill.getCoa().getId())) {
                    if (debitGlcodeAndAmountMap.containsKey(assetBill.getCoa().getId().toString())) {
                        BigDecimal amount = debitGlcodeAndAmountMap.get(assetBill.getCoa().getId().toString());
                        amount = amount.add(assetBill.getAmount());
                        debitGlcodeAndAmountMap.put(assetBill.getCoa().getId().toString(), amount);
                    }
                } else {
                    debitGlcodeAndAmountMap.put(assetBill.getCoa().getId().toString(), assetBill.getAmount());
                    coaSet.add(assetBill.getCoa().getId());
                }
                addAssetForBill(assetBill);
            }
        return debitGlcodeAndAmountMap;
    }

    private void addAssetForBill(final AssetForBill adb) {
        if (!workOrderEstimate.getAssetValues().isEmpty() && adb.getAsset() != null) {
            final AssetForBill assetForBill = new AssetForBill();
            final Asset assetNew = assetService.findOne(adb.getAsset().getId());
            final String value = worksService.getWorksConfigValue("WORKS_ASSET_STATUS");
            if (StringUtils.isBlank(value)) {
                final List<ValidationError> errors = new ArrayList<ValidationError>();
                errors.add(new ValidationError("asset.status", "asset.status.configvalue"));
                throw new ValidationException(errors);
            }
            if (assetNew.getStatus().getDescription().equals(value.split(",")[0])) {
                final EgwStatus status = egwStatusHibernateDAO.getStatusByModuleAndCode("ASSET", value.split(",")[1]);
                assetNew.setStatus(status);
            }
            assetForBill.setEgbill(contractorBillRegister);
            assetForBill.setCoa(chartOfAccountsHibernateDAO.findById(adb.getCoa().getId(), false));
            assetForBill.setWorkOrderEstimate(workOrderEstimate);
            assetForBill.setAmount(adb.getAmount());
            assetForBill.setDescription(adb.getDescription());
            assetForBill.setAsset(assetNew);
            contractorBillRegister.addAssetDetails(assetForBill);
        }
    }

    public Map<String, BigDecimal> getGlcodesForstanDed() {
        final Map<String, BigDecimal> debitGlcodeAndAmountMap = new HashMap<String, BigDecimal>();
        final Set<BigDecimal> coaSet = new HashSet<BigDecimal>();
        for (final DeductionTypeForBill deductionBill : standardDeductions)
            if (deductionBill != null && deductionBill.getGlcodeid() != null) {
                if (coaSet.contains(deductionBill.getGlcodeid())) {
                    if (debitGlcodeAndAmountMap.containsKey(deductionBill.getGlcodeid().toString())) {
                        BigDecimal amount = debitGlcodeAndAmountMap.get(deductionBill.getGlcodeid().toString());
                        amount = amount.add(deductionBill.getCreditamount());
                        debitGlcodeAndAmountMap.put(deductionBill.getGlcodeid().toString(), amount);
                    }
                } else {
                    debitGlcodeAndAmountMap
                            .put(deductionBill.getGlcodeid().toString(), deductionBill.getCreditamount());
                    coaSet.add(deductionBill.getGlcodeid());
                }
                addDeductionTypeBill(deductionBill);
            }
        return debitGlcodeAndAmountMap;
    }

    private void addDeductionTypeBill(final DeductionTypeForBill deductionBill) {
        final DeductionTypeForBill deductionTypeForBill = new DeductionTypeForBill();
        deductionTypeForBill.setEgbill(contractorBillRegister);
        deductionTypeForBill.setWorkOrder(workOrder);
        deductionTypeForBill.setNarration(deductionBill.getNarration());
        deductionTypeForBill.setCoa(chartOfAccountsHibernateDAO.findById(Long.valueOf(deductionBill.getGlcodeid()
                .toString()), false));
        deductionTypeForBill.setCreditamount(deductionBill.getCreditamount());
        deductionTypeForBill.setDeductionType(deductionBill.getDeductionType());
        contractorBillRegister.addDeductionType(deductionTypeForBill);
    }

    public Map<String, BigDecimal> getGlcodesForStatDed() {
        final Map<String, BigDecimal> debitGlcodeAndAmountMap = new HashMap<String, BigDecimal>();
        final Set<Long> coaSet = new HashSet<Long>();
        for (final StatutoryDeductionsForBill bpd : contractorBillService.getStatutoryDeductions(actionStatutorydetails))
            if (bpd != null && bpd.getEgBillPayeeDtls().getRecovery() != null
                    && bpd.getEgBillPayeeDtls().getRecovery().getId() != null
                    && bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts() != null
                    && bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId() != null)
                if (coaSet.contains(bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId())) {
                    if (debitGlcodeAndAmountMap.containsKey(bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts()
                            .getId().toString())) {
                        BigDecimal amount = debitGlcodeAndAmountMap.get(bpd.getEgBillPayeeDtls().getRecovery()
                                .getChartofaccounts().getId().toString());
                        amount = amount.add(bpd.getEgBillPayeeDtls().getCreditAmount());
                        debitGlcodeAndAmountMap.put(bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId()
                                .toString(), amount);
                    }
                } else {
                    debitGlcodeAndAmountMap.put(bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId()
                            .toString(), bpd.getEgBillPayeeDtls().getCreditAmount());
                    coaSet.add(bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId());
                }
        return debitGlcodeAndAmountMap;
    }

    public void populateOtherDeductionList() throws ApplicationException {
        final char[] charTypes = getBillAccountTypesFromConfig();
        if (charTypes != null)
            for (final char charType : charTypes) {
                final List<CChartOfAccounts> pList = chartOfAccountsHibernateDAO.getActiveAccountsForType(charType);

                final List<CChartOfAccounts> coaContPayableList = chartOfAccountsHibernateDAO.getAccountCodeByPurpose(Integer
                        .valueOf(worksService.getWorksConfigValue(WORKS_NETPAYABLE_CODE)));
                if (pList != null && coaContPayableList != null)
                    pList.removeAll(coaContPayableList);

                if (pList != null)
                    customDeductionAccountList.addAll(pList);
            }
        if (!contractorBillService.getStandardDeductionsFromConfig().isEmpty())
            standardDeductionConfValues = new LinkedList<String>(contractorBillService
                    .getStandardDeductionsFromConfig().keySet());

        if (StringUtils.isNotBlank(worksService.getWorksConfigValue(RETENTION_MONEY_PURPOSE)))
            retentionMoneyAccountList = chartOfAccountsHibernateDAO.getAccountCodeByPurpose(Integer.valueOf(worksService
                    .getWorksConfigValue(RETENTION_MONEY_PURPOSE)));
    }

    private char[] getBillAccountTypesFromConfig() {
        final String strTypes = worksService.getWorksConfigValue("CUSTOM_DEDUCTION");
        char[] charArr = null;
        if (StringUtils.isNotBlank(strTypes)) {
            final String[] strArrayTypes = strTypes.split(",");
            charArr = new char[strArrayTypes.length];
            for (int i = 0; i < strArrayTypes.length; i++)
                charArr[i] = strArrayTypes[i].charAt(0);
        }
        return charArr;
    }

    public void setBudgetDetails(final Long workOrderId, final Long workOrderEstimateId, final Date billDate) {
        billAmount = contractorBillService.getApprovedMBAmount(workOrderId, workOrderEstimateId, billDate);
        // utilizedAmount =
        // contractorBillService.getTotalUtilizedAmount(workOrderId, billDate);
        // sanctionedBudget =
        // contractorBillService.getBudgetedAmtForYear(workOrderId, billDate);
    }

    public Map<Long, String> getContratorCoaPayableMap() throws NumberFormatException, ApplicationException {
        if (StringUtils.isNotBlank(worksService.getWorksConfigValue(WORKS_NETPAYABLE_CODE))) {
            final List<CChartOfAccounts> coaPayableList = chartOfAccountsHibernateDAO.getAccountCodeByPurpose(Integer
                    .valueOf(worksService.getWorksConfigValue(WORKS_NETPAYABLE_CODE)));
            for (final CChartOfAccounts coa : coaPayableList)
                contratorCoaPayableMap.put(coa.getId(), coa.getGlcode() + "-" + coa.getName());
        }
        return contratorCoaPayableMap;
    }

    public void setContratorCoaPayableMap(final Map<Long, String> contratorCoaPayableMap) {
        this.contratorCoaPayableMap = contratorCoaPayableMap;
    }

    public String getDisp() {
        return disp;
    }

    public void setDisp(final String disp) {
        this.disp = disp;
    }

    public List<AssetForBill> getAccountDetailsForBill() {
        return accountDetailsForBill;
    }

    public void setAccountDetailsForBill(final List<AssetForBill> accountDetailsForBill) {
        this.accountDetailsForBill = accountDetailsForBill;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {

        this.id = id;
    }

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(final Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(final WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public void setContractorBillService(final ContractorBillService contractorBillService) {
        this.contractorBillService = contractorBillService;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
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

    public void setActionStatutorydetails(final List<StatutoryDeductionsForBill> actionStatutorydetails) {
        this.actionStatutorydetails = actionStatutorydetails;
    }

    public List<DeductionTypeForBill> getStandardDeductions() {
        return standardDeductions;
    }

    public void setStandardDeductions(final List<DeductionTypeForBill> standardDeductions) {
        this.standardDeductions = standardDeductions;
    }

    public List<EgBilldetails> getCustomDeductions() {
        return customDeductions;
    }

    public void setCustomDeductions(final List<EgBilldetails> customDeductions) {
        this.customDeductions = customDeductions;
    }

    public BigDecimal getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(final BigDecimal billAmount) {
        this.billAmount = billAmount;
    }

    public BigDecimal getUtilizedAmount() {
        return utilizedAmount;
    }

    public void setUtilizedAmount(final BigDecimal utilizedAmount) {
        this.utilizedAmount = utilizedAmount;
    }

    public BigDecimal getSanctionedBudget() {
        return sanctionedBudget;
    }

    public void setSanctionedBudget(final BigDecimal sanctionedBudget) {
        this.sanctionedBudget = sanctionedBudget;
    }

    public void setFinancialDetailService(final PersistenceService<FinancialDetail, Long> financialDetailService) {
        this.financialDetailService = financialDetailService;
    }

    public Long getNetPayableCode() {
        return netPayableCode;
    }

    public void setNetPayableCode(final Long netPayableCode) {
        this.netPayableCode = netPayableCode;
    }

    public BigDecimal getNetPayableAmount() {
        return netPayableAmount;
    }

    public void setNetPayableAmount(final BigDecimal netPayableAmount) {
        this.netPayableAmount = netPayableAmount;
    }

    public BigDecimal getAdvaceAdjustmentCreditAmount() {
        return advaceAdjustmentCreditAmount;
    }

    public void setAdvaceAdjustmentCreditAmount(final BigDecimal advaceAdjustmentCreditAmount) {
        this.advaceAdjustmentCreditAmount = advaceAdjustmentCreditAmount;
    }

    public String getPartyBillNumber() {
        return partyBillNumber;
    }

    public void setPartyBillNumber(final String partyBillNumber) {
        this.partyBillNumber = partyBillNumber;
    }

    public Date getPartyBillDate() {
        return partyBillDate;
    }

    public void setPartyBillDate(final Date partyBillDate) {
        this.partyBillDate = partyBillDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(final Date completionDate) {
        this.completionDate = completionDate;
    }

    public void setCommonAssetsService(final AssetService assetService) {
        this.assetService = assetService;
    }

    public ContractorBillRegister getContractorBillRegister() {
        return contractorBillRegister;
    }

    public void setContractorBillRegister(final ContractorBillRegister contractorBillRegister) {
        this.contractorBillRegister = contractorBillRegister;
    }

    public void setMeasurementBookService(final MeasurementBookServiceImpl measurementBookService) {
        this.measurementBookService = measurementBookService;
    }

    public Integer getPartbillNo() {
        return partbillNo;
    }

    public void setPartbillNo(final Integer partbillNo) {
        this.partbillNo = partbillNo;
    }

    public void setStandardDeductionAccountList(final List<CChartOfAccounts> standardDeductionAccountList) {
        this.standardDeductionAccountList = standardDeductionAccountList;
    }

    public void setCustomDeductionAccountList(final List<CChartOfAccounts> customDeductionAccountList) {
        this.customDeductionAccountList = customDeductionAccountList;
    }

    public BigDecimal getTotalPendingBalance() {
        return totalPendingBalance;
    }

    public void setTotalPendingBalance(final BigDecimal totalPendingBalance) {
        this.totalPendingBalance = totalPendingBalance;
    }

    public String getSourcepage() {
        return sourcepage;
    }

    public void setSourcepage(final String sourcepage) {
        this.sourcepage = sourcepage;
    }

    public List<MBHeader> getMbHeaderList() {
        return mbHeaderList;
    }

    public void setMbHeaderList(final List<MBHeader> mbHeaderList) {
        this.mbHeaderList = mbHeaderList;
    }

    public Long[] getAppConfigValueId() {
        return appConfigValueId;
    }

    public void setAppConfigValueId(final Long[] appConfigValueId) {
        this.appConfigValueId = appConfigValueId;
    }

    public String[] getSelectedchecklistValue() {
        return selectedchecklistValue;
    }

    public void setSelectedchecklistValue(final String[] selectedchecklistValue) {
        this.selectedchecklistValue = selectedchecklistValue;
    }

    public void setChecklistService(final PersistenceService<EgChecklists, Long> checklistService) {
        this.checklistService = checklistService;
    }

    public List<AppConfigValues> getFinalBillChecklist() {
        return finalBillChecklist;
    }

    public void setFinalBillChecklist(final List<AppConfigValues> finalBillChecklist) {
        this.finalBillChecklist = finalBillChecklist;
    }

    public List<String> getChecklistValues() {
        return checklistValues;
    }

    public void setChecklistValues(final List<String> checklistValues) {
        this.checklistValues = checklistValues;
    }

    public void setDepartmentService(final DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public List<WorkflowAction> getValidActions() {
        return workflowService.getValidActions(contractorBillRegister);
    }

    public void setContractorBillWorkflowService(final WorkflowService<ContractorBillRegister> workflow) {
        workflowService = workflow;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(final String messageKey) {
        this.messageKey = messageKey;
    }

    public String getNextEmployeeName() {
        return nextEmployeeName;
    }

    public void setNextEmployeeName(final String nextEmployeeName) {
        this.nextEmployeeName = nextEmployeeName;
    }

    public String getNextDesignation() {
        return nextDesignation;
    }

    public void setNextDesignation(final String nextDesignation) {
        this.nextDesignation = nextDesignation;
    }

    public Long getWorkOrderEstimateId() {
        return workOrderEstimateId;
    }

    public void setWorkOrderEstimateId(final Long workOrderEstimateId) {
        this.workOrderEstimateId = workOrderEstimateId;
    }

    public WorkOrderEstimate getWorkOrderEstimate() {
        return workOrderEstimate;
    }

    public void setWorkOrderEstimate(final WorkOrderEstimate workOrderEstimate) {
        this.workOrderEstimate = workOrderEstimate;
    }

    public ReportService getReportService() {
        return reportService;
    }

    public void setReportService(final ReportService reportService) {
        this.reportService = reportService;
    }

    public InputStream getCompletionCertificatePDF() {
        return completionCertificatePDF;
    }

    public void setCompletionCertificatePDF(final InputStream completionCertificatePDF) {
        this.completionCertificatePDF = completionCertificatePDF;
    }

    public void setVoucherService(final VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    public void setScriptExecutionService(final ScriptService scriptExecutionService) {
        this.scriptExecutionService = scriptExecutionService;
    }

    public void setAbstractEstimateService(final AbstractEstimateService abstractEstimateService) {
    }

    public WorkCompletionInfo getCompletionInfo() {
        return completionInfo;
    }

    public void setCompletionInfo(final WorkCompletionInfo completionInfo) {
        this.completionInfo = completionInfo;
    }

    public List<WorkCompletionDetailInfo> getCompletionDetailInfoList() {
        return completionDetailInfoList;
    }

    public void setCompletionDetailInfoList(final List<WorkCompletionDetailInfo> completionDetailInfoList) {
        this.completionDetailInfoList = completionDetailInfoList;
    }

    public String[] getRemarks() {
        return remarks;
    }

    public void setRemarks(final String[] remarks) {
        this.remarks = remarks;
    }

    public boolean getSkipBudget() {
        return skipBudget;
    }

    public void setSkipBudget(final boolean skipBudget) {
        this.skipBudget = skipBudget;
    }

    public void setEgovCommon(final EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }

    public List<CChartOfAccounts> getRetentionMoneyAccountList() {
        return retentionMoneyAccountList;
    }

    public void setRetentionMoneyAccountList(final List<CChartOfAccounts> retentionMoneyAccountList) {
        this.retentionMoneyAccountList = retentionMoneyAccountList;
    }

    public List<EgBilldetails> getRetentionMoneyDeductions() {
        return retentionMoneyDeductions;
    }

    public void setRetentionMoneyDeductions(final List<EgBilldetails> retentionMoneyDeductions) {
        this.retentionMoneyDeductions = retentionMoneyDeductions;
    }

    public String getIsRetMoneyAutoCalculated() {
        return isRetMoneyAutoCalculated;
    }

    public void setIsRetMoneyAutoCalculated(final String isRetMoneyAutoCalculated) {
        this.isRetMoneyAutoCalculated = isRetMoneyAutoCalculated;
    }

    public String getIsRetMoneyEditable() {
        return isRetMoneyEditable;
    }

    public void setIsRetMoneyEditable(final String isRetMoneyEditable) {
        this.isRetMoneyEditable = isRetMoneyEditable;
    }

    public String getPercDeductForRetMoneyPartBill() {
        return percDeductForRetMoneyPartBill;
    }

    public void setPercDeductForRetMoneyPartBill(final String percDeductForRetMoneyPartBill) {
        this.percDeductForRetMoneyPartBill = percDeductForRetMoneyPartBill;
    }

    public String getPercDeductForRetMoneyFinalBill() {
        return percDeductForRetMoneyFinalBill;
    }

    public void setPercDeductForRetMoneyFinalBill(final String percDeductForRetMoneyFinalBill) {
        this.percDeductForRetMoneyFinalBill = percDeductForRetMoneyFinalBill;
    }

    public String getRetMoneyFinalBillPerOnValue() {
        return retMoneyFinalBillPerOnValue;
    }

    public void setRetMoneyFinalBillPerOnValue(final String retMoneyFinalBillPerOnValue) {
        this.retMoneyFinalBillPerOnValue = retMoneyFinalBillPerOnValue;
    }

    public TenderResponse getTenderResponse() {
        return tenderResponse;
    }

    public void setTenderResponse(final TenderResponse tenderResponse) {
        this.tenderResponse = tenderResponse;
    }

    public String getRebatePremLevel() {
        return rebatePremLevel;
    }

    public void setRebatePremLevel(final String rebatePremLevel) {
        this.rebatePremLevel = rebatePremLevel;
    }

    public List<EgPartytype> getSubPartyTypeDtls() {
        return subPartyTypeDtls;
    }

    public void setSubPartyTypeDtls(final List<EgPartytype> subPartyTypeDtls) {
        this.subPartyTypeDtls = subPartyTypeDtls;
    }

    public List<EgwTypeOfWork> getTypeOfWorkDtls() {
        return typeOfWorkDtls;
    }

    public void setTypeOfWorkDtls(final List<EgwTypeOfWork> typeOfWorkDtls) {
        this.typeOfWorkDtls = typeOfWorkDtls;
    }

    public String getShowSubPartyType() {
        return showSubPartyType;
    }

    public void setShowSubPartyType(final String showSubPartyType) {
        this.showSubPartyType = showSubPartyType;
    }

    public String getShowTypeOfWork() {
        return showTypeOfWork;
    }

    public void setShowTypeOfWork(final String showTypeOfWork) {
        this.showTypeOfWork = showTypeOfWork;
    }

    public String getEditStatutoryAmount() {
        return editStatutoryAmount;
    }

    public void setEditStatutoryAmount(final String editStatutoryAmount) {
        this.editStatutoryAmount = editStatutoryAmount;
    }

    public Long getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(final Long estimateId) {
        this.estimateId = estimateId;
    }

    public String getPercTenderType() {
        return percTenderType;
    }

    public void setPercTenderType(final String percTenderType) {
        this.percTenderType = percTenderType;
    }

    public String getIsRebatePremLevelBill() {
        return isRebatePremLevelBill;
    }

    public void setIsRebatePremLevelBill(final String isRebatePremLevelBill) {
        this.isRebatePremLevelBill = isRebatePremLevelBill;
    }

    public void setCancelBillService(final PersistenceService<MBForCancelledBill, Long> cancelBillService) {
        this.cancelBillService = cancelBillService;
    }

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(final BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public void setBudgetService(final BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    public List<String> getAppConfigValuesToSkipBudget() {
        return worksService.getNatureOfWorkAppConfigValues("Works", APPCONFIG_KEY_NAME);
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    public BigDecimal getTenderedItemsAmount() {
        return tenderedItemsAmount;
    }

    public void setTenderedItemsAmount(final BigDecimal tenderedItemsAmount) {
        this.tenderedItemsAmount = tenderedItemsAmount;
    }

    public Integer getWorkflowFunctionaryId() {
        return workflowFunctionaryId;
    }

    public Long[] getMbHeaderId() {
        return mbHeaderId;
    }

    public void setMbHeaderId(final Long[] mbHeaderId) {
        this.mbHeaderId = mbHeaderId;
    }

    public List<MBHeader> getMbHeaderListForBillId() {
        return mbHeaderListForBillId;
    }

    public void setMbHeaderListForBillId(final List<MBHeader> mbHeaderListForBillId) {
        this.mbHeaderListForBillId = mbHeaderListForBillId;
    }

    public BigDecimal getTotalAdvancePaid() {
        return totalAdvancePaid;
    }

    public void setTotalAdvancePaid(final BigDecimal totalAdvancePaid) {
        this.totalAdvancePaid = totalAdvancePaid;
    }

    public void setContractorAdvanceService(final ContractorAdvanceService contractorAdvanceService) {
        this.contractorAdvanceService = contractorAdvanceService;
    }

    public void setEmployeeService(final EmployeeServiceOld employeeService) {
        this.employeeService = employeeService;
    }

    public String getIsRCEstimate() {
        return isRCEstimate;
    }

    public void setIsRCEstimate(final String isRCEstimate) {
        this.isRCEstimate = isRCEstimate;
    }

    public Date getRestorationEndDate() {
        return restorationEndDate;
    }

    public void setRestorationEndDate(final Date restorationEndDate) {
        this.restorationEndDate = restorationEndDate;
    }

    public String getDwCategory() {
        return dwCategory;
    }

    public void setDwCategory(final String dwCategory) {
        this.dwCategory = dwCategory;
    }

    public String getAllowForward() {
        return allowForward;
    }

    public void setAllowForward(final String allowForward) {
        this.allowForward = allowForward;
    }

    public String getShowValidationMsg() {
        return showValidationMsg;
    }

    public void setShowValidationMsg(final String showValidationMsg) {
        this.showValidationMsg = showValidationMsg;
    }

    public Date getLatestMBDate() {
        return latestMBDate;
    }

    public void setLatestMBDate(final Date latestMBDate) {
        this.latestMBDate = latestMBDate;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(final String refNo) {
        this.refNo = refNo;
    }
}
