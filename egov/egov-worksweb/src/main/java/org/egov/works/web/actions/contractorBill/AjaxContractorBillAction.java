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
import org.apache.struts2.ServletActionContext;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgPartytype;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.EgPartytypeHibernateDAO;
import org.egov.commons.dao.EgwTypeOfWorkHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.egf.commons.EgovCommon;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.model.bills.EgBillregister;
import org.egov.model.budget.BudgetGroup;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeServiceOld;
import org.egov.services.budget.BudgetService;
import org.egov.services.recoveries.RecoveryService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.AbstractEstimateAppropriation;
import org.egov.works.abstractestimate.entity.FinancialDetail;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.milestone.entity.TrackMilestone;
import org.egov.works.models.contractorBill.WorkCompletionDetailInfo;
import org.egov.works.models.contractorBill.WorkCompletionInfo;
import org.egov.works.models.contractoradvance.ContractorAdvanceRequisition;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.AssetsForWorkOrder;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.ContractorBillService;
import org.egov.works.services.WorksService;
import org.egov.works.services.contractoradvance.ContractorAdvanceService;
import org.egov.works.services.impl.MeasurementBookServiceImpl;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AjaxContractorBillAction extends BaseFormAction {

    private static final long serialVersionUID = -3316045051992569984L;
    private static final Logger logger = Logger.getLogger(AjaxContractorBillAction.class);
    private static final String ADVANCE_AJUSTMENT = "advanceAdjustment";
    private static final String CHECK_LIST = "checklist";
    private static final String STANDARD_DEDUCTION_ACCOUNTS = "standardDeductionAccounts";
    private static final String TOTAL_WORK_VALUE = "totalWorkValue";
    private static final String CUMULATIVE_BILL_VALUE = "cumulativeBillValue";
    private static final String LATEST_APPROVED_BILL_DATE = "latestBillDate";
    private static final String COMPLETION_DETAILS = "completionDetails";
    private static final String APPCONFIG_KEY_NAME = "SKIP_BUDGET_CHECK";
    private WorkCompletionInfo workcompletionInfo;
    private List<WorkCompletionDetailInfo> workCompletionDetailInfo = new LinkedList<WorkCompletionDetailInfo>();;
    private ContractorBillService contractorBillService;
    private EgBillregister egBillregister;
    private WorkOrder workOrder;
    private Long id;
    private Long workOrderId;
    private Date billDate;
    private String workOrderNumber;
    private String billType;
    private String deductionType;
    private String rowId;
    private Long workOrderEstimateId;
    private List<CChartOfAccounts> standardDeductionAccountList = new LinkedList<CChartOfAccounts>();
    private boolean noMBsPresent;
    private String latestBillDateStr;
    private Long billId;
    @Autowired
    private FinancialYearHibernateDAO finHibernateDao;
    private Date latestMBDate;
    private Long estId;
    private Long woId;
    private String refNo;

    public void setStandardDeductionAccountList(
            final List<CChartOfAccounts> standardDeductionAccountList) {
        this.standardDeductionAccountList = standardDeductionAccountList;
    }

    private List<AppConfigValues> finalBillChecklist = new LinkedList<AppConfigValues>();
    private List<String> checklistValues = new LinkedList<String>();
    private String[] selectedchecklistvalues;
    private List<MBHeader> approvedMBHeaderList = new LinkedList<MBHeader>();
    private BigDecimal totalWorkValueRecorded = BigDecimal.ZERO;
    private BigDecimal totalTenderedItemsAmt = BigDecimal.ZERO;
    private BigDecimal totalUtilizedAmount = BigDecimal.ZERO;
    private BigDecimal budgetSanctionAmount = BigDecimal.ZERO;
    private WorksService worksService;
    private MeasurementBookServiceImpl measurementBookService;
    @Autowired
    private EgPartytypeHibernateDAO egPartytypeHibernateDAO;

    @Autowired
    private EgwTypeOfWorkHibernateDAO egwTypeOfWorkHibernateDAO;

    private BigDecimal totalPendingBalance = new BigDecimal("0.00");
    private BigDecimal totalAdvancePaid = new BigDecimal("0.00");
    private String source;
    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    private List<CChartOfAccounts> coaList = new LinkedList<CChartOfAccounts>();
    private List<AssetsForWorkOrder> assetList = new LinkedList<AssetsForWorkOrder>();
    private static final String SKIP_BUDGCHECK_APPCONF_KEYNAME = "SKIP_BUDGET_CHECK";
    private Long glCode;
    private BigDecimal budgBalance;
    private BigDecimal budgAmount;
    private FinancialDetail financialDetail = new FinancialDetail();
    private BigDecimal totalGrantAmount = BigDecimal.ZERO;
    private BudgetDetailsDAO budgetDetailsDAO;
    private static final String BUDGET_DETAILS = "budgetDetails";
    private BigDecimal actualAmount = BigDecimal.ZERO;
    private boolean checkBudget;
    private Long estimateId;
    private Long glCodeId;
    private String errorMsg;
    private EgovCommon egovCommon;
    private static final String WORKS = "Works";
    private static final String TRACK_MLS_CHECK = "trackMlsCheckForBillCreation";
    private static final String STATUTORY_DEDUCTION_AMOUNT = "statutoryDeductionAmount";
    private static final String PARTY_TYPE_CONTRACTOR = "Contractor";

    @Autowired
    @Qualifier("recoveryPersistenceService")
    private RecoveryService recoveryService;

    private String subPartyType;
    private String typeOfWork;
    private BigDecimal grossAmount;
    private BigDecimal statutoryAmount = BigDecimal.ZERO;
    private String recoveryType;
    private BigDecimal cumulativeBillValue;
    private BudgetService budgetService;
    private Long contractorBillId;
    private String estimateNumber;
    private String query = "";
    private List<WorkOrder> workOrderList = new LinkedList<WorkOrder>();
    private List<ContractorBillRegister> contractorBillList = new LinkedList<ContractorBillRegister>();
    private String trackMlsCheck;
    private List<AbstractEstimate> estimateList = new LinkedList<AbstractEstimate>();
    private String yearEndApprCheck;
    private String estimateNo;
    private static final String VALID = "valid";
    private static final String INVALID = "invalid";
    private ContractorAdvanceService contractorAdvanceService;
    private String advanceRequisitionNo;
    private String owner = "";
    private String arfInWorkFlowCheck;
    @Autowired
    private EmployeeServiceOld employeeService;
    private static final String ARF_IN_WORKFLOW_CHECK = "arfInWorkflowCheck";
    private String showValidationMsg = "";

    public void setBudgetService(final BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @Override
    public Object getModel() {
        return null;
    }

    public String totalWorkValue() {
        totalWorkValueRecorded = contractorBillService.getApprovedMBAmount(workOrderId, workOrderEstimateId, billDate);
        // totalUtilizedAmount = contractorBillService.getTotalUtilizedAmount(workOrderId, billDate);
        approvedMBHeaderList.addAll(measurementBookService.getApprovedMBList(workOrderId, workOrderEstimateId, billDate));
        // budgetSanctionAmount = contractorBillService.getBudgetedAmtForYear(workOrderId, billDate);
        totalTenderedItemsAmt = contractorBillService.getApprovedMBAmountOfTenderedItems(workOrderId, workOrderEstimateId,
                billDate);
        isSkipBudgetCheck(workOrderEstimateId);
        if (approvedMBHeaderList.isEmpty())
            noMBsPresent = true;
        return TOTAL_WORK_VALUE;
    }

    public String getLastBillDate() {
        List<EgBillregister> billList;
        Date latestBillDt;
        latestBillDateStr = "";
        final WorkOrderEstimate workOrderEstimate = (WorkOrderEstimate) persistenceService.find(
                "from WorkOrderEstimate where id=?",
                workOrderEstimateId);
        if (workOrderEstimate != null) {
            billList = contractorBillService.getListOfNonCancelledBillsforEstimate(workOrderEstimate.getEstimate(), new Date());
            if (billList != null && !billList.isEmpty()) {
                latestBillDt = billList.get(0).getBilldate();
                for (final EgBillregister bill : billList)
                    if (latestBillDt.compareTo(bill.getBilldate()) < 0)
                        latestBillDt = bill.getBilldate();
                latestBillDateStr = new SimpleDateFormat("dd/MM/yyyy").format(latestBillDt);
            }
        }
        return LATEST_APPROVED_BILL_DATE;
    }

    public String totalCumulativeBillValue() {
        final WorkOrderEstimate workOrderEstimate = (WorkOrderEstimate) persistenceService.find(
                "from WorkOrderEstimate where id=?",
                workOrderEstimateId);
        cumulativeBillValue = contractorBillService.getTotalValueWoForUptoBillDate(billDate,
                workOrderEstimate.getWorkOrder().getId(), workOrderEstimate.getId());
        return CUMULATIVE_BILL_VALUE;
    }

    public String yearEndApprCheckForBillCreation() {
        final AbstractEstimate estimate = (AbstractEstimate) persistenceService
                .find("select woe.estimate from WorkOrderEstimate woe where woe.id = ? ", workOrderEstimateId);
        yearEndApprCheck = VALID;
        estimateNo = estimate.getEstimateNumber();
        Long billDateFinYearId = 0L;
        CFinancialYear billDateFinYear;
        try {
            billDateFinYear = finHibernateDao.getFinancialYearByDate(billDate);
        } catch (final Exception e) {
            throw new ValidationException(
                    Arrays.asList(new ValidationError("yrEnd.appr.verification.for.bill.financialyear.invalid",
                            "yrEnd.appr.verification.for.bill.financialyear.invalid")));
        }

        if (billDateFinYear != null)
            billDateFinYearId = billDateFinYear.getId();

        if (estimate.getDepositCode() == null) {
            final AbstractEstimateAppropriation aeaObj = (AbstractEstimateAppropriation) persistenceService
                    .findByNamedQuery("getLatestBudgetUsageForEstimate", estimate.getId());
            if (aeaObj != null && aeaObj.getBudgetUsage().getConsumedAmount() > 0) {
                if (aeaObj.getBudgetUsage().getFinancialYearId().intValue() != billDateFinYearId.intValue())
                    yearEndApprCheck = INVALID;
            } else
                yearEndApprCheck = INVALID;
        }

        return "yearEndApprForBillCreationCheck";
    }

    public String advanceRequisitionInWorkflowCheck() {
        arfInWorkFlowCheck = VALID;
        if (workOrderEstimateId != null) {
            final ContractorAdvanceRequisition arf = contractorAdvanceService
                    .getContractorARFInWorkflowByWOEId(workOrderEstimateId);
            if (arf != null) {
                arfInWorkFlowCheck = INVALID;
                advanceRequisitionNo = arf.getAdvanceRequisitionNumber();
                estimateNo = arf.getWorkOrderEstimate().getEstimate().getEstimateNumber();
                final PersonalInformation emp = employeeService.getEmployeeforPosition(arf.getCurrentState().getOwnerPosition());
                if (emp != null)
                    owner = emp.getUserMaster().getName();
            }
        }
        return ARF_IN_WORKFLOW_CHECK;
    }

    private void isSkipBudgetCheck(final Long workOrderEstimateId) {
        final List<AppConfigValues> appConfigValuesList = worksService.getAppConfigValue(WORKS, SKIP_BUDGCHECK_APPCONF_KEYNAME);
        final WorkOrderEstimate workOrderEstimate = (WorkOrderEstimate) persistenceService.find(
                "from WorkOrderEstimate where id=?",
                workOrderEstimateId);
        logger.info("length of appconfig values>>>>>> " + appConfigValuesList.size());
        for (final AppConfigValues appValues : appConfigValuesList)
            if (appValues.getValue().equals(workOrderEstimate.getEstimate().getNatureOfWork().getName())) {
                checkBudget = false;
                return;
            }
        checkBudget = true;
    }

    public String completionInfo() {
        final WorkOrderEstimate workOrderEstimate = (WorkOrderEstimate) persistenceService.find(
                "from WorkOrderEstimate where id=?",
                workOrderEstimateId);
        workcompletionInfo = contractorBillService.setWorkCompletionInfoFromBill(null, workOrderEstimate);
        workCompletionDetailInfo.addAll(contractorBillService.setWorkCompletionDetailInfoList(workOrderEstimate));
        return COMPLETION_DETAILS;
    }

    /**
     * calculate contractor total pending balance
     * @return String
     */
    public String calculateAdvanceAdjustment() {
        final WorkOrderEstimate workOrderEstimate = (WorkOrderEstimate) persistenceService.find(
                "from WorkOrderEstimate where id=?",
                workOrderEstimateId);
        totalAdvancePaid = contractorAdvanceService.getTotalAdvancePaymentMadeByWOEstimateId(workOrderEstimate.getId(), billDate);
        totalPendingBalance = contractorBillService.calculateTotalPendingAdvance(totalAdvancePaid, billDate, workOrderEstimate,
                billId);
        return ADVANCE_AJUSTMENT;
    }

    public String billCheckListDetails() throws NumberFormatException, ApplicationException {
        final WorkOrderEstimate workOrderEstimate = (WorkOrderEstimate) persistenceService.find(
                "from WorkOrderEstimate where id=?",
                workOrderEstimateId);
        checklistValues.add("N/A");
        checklistValues.add("Yes");
        checklistValues.add("No");

        try {
            if ((workOrderEstimate.getEstimate().getNatureOfWork().getCode().equals("Improvement Works")
                    || workOrderEstimate.getEstimate().getNatureOfWork().getCode().equals("Capital Works"))
                    && billType.contains("Final"))
                finalBillChecklist = worksService.getAppConfigValue(WORKS, "CONTRACTOR_Bill_FinalChecklist");
            else if ((workOrderEstimate.getEstimate().getNatureOfWork().getCode().equals("Improvement Works")
                    || workOrderEstimate.getEstimate().getNatureOfWork().getCode().equals("Capital Works"))
                    && billType.contains("Part"))
                finalBillChecklist = worksService.getAppConfigValue(WORKS, "CONTRACTOR_Bill_RunChecklist");
            else if (workOrderEstimate.getEstimate().getNatureOfWork().getCode().equals("Repairs and maintenance")
                    && (billType.contains("Final") || billType.contains("Part")))
                finalBillChecklist = worksService.getAppConfigValue(WORKS, "CONTRACTOR_Bill_MaintananceChecklist");

        } catch (final Exception e) {
            logger.error("--------------Error in check bill List-------------");
        }

        return CHECK_LIST;
    }

    public String trackMilestoneCheckForFinalBill() {
        final List<TrackMilestone> tm = persistenceService.findAllBy(
                " select trmls from WorkOrderEstimate as woe left join woe.milestone mls left join mls.trackMilestone trmls where trmls.egwStatus.code='APPROVED' and woe.id = ? and trmls.total=100 ",
                workOrderEstimateId);
        trackMlsCheck = "invalid";
        if (tm != null && !tm.isEmpty() && tm.get(0) != null)
            trackMlsCheck = "valid";
        return TRACK_MLS_CHECK;
    }

    public String trackMilestoneCheckForPartBill() {
        final List<TrackMilestone> tm = persistenceService.findAllBy(
                " select trmls from WorkOrderEstimate as woe left join woe.milestone mls left join mls.trackMilestone trmls where trmls.egwStatus.code='APPROVED' and woe.id = ? and trmls.total>0 ",
                workOrderEstimateId);
        trackMlsCheck = "invalid";
        if (tm != null && !tm.isEmpty() && tm.get(0) != null)
            trackMlsCheck = "valid";
        return TRACK_MLS_CHECK;
    }

    public String populateAccountHeadsForDeductionType() {
        getStandardDeductionTypes();
        return STANDARD_DEDUCTION_ACCOUNTS;
    }

    public void setDespositWorksAccBal() throws Exception {
        final CChartOfAccounts coaObj = (CChartOfAccounts) persistenceService.find("from CChartOfAccounts where id=?",
                getGlCodeId());
        final AbstractEstimate estimate = (AbstractEstimate) persistenceService.find("from AbstractEstimate where id=?",
                getEstimateId());
        final Accountdetailtype adt = chartOfAccountsHibernateDAO.getAccountDetailTypeIdByName(coaObj.getGlcode(), "DEPOSITCODE");
        String fundCode = null;
        if (estimate != null && estimate.getFinancialDetails().get(0) != null) {
            financialDetail = estimate.getFinancialDetails().get(0);
            fundCode = financialDetail.getFund().getCode();
        }
        if (adt == null)
            throw new ValidationException("", "The AccountDetailType is not  defined for Desposit Works ");
        else if (estimate.getDepositCode() == null)
            throw new ValidationException("", "The Deposit code is not  defined for Estimate ");
        final BigDecimal accBalAmount = egovCommon.getAccountBalanceTillDate(getBillDate(), coaObj.getGlcode(),
                fundCode, adt.getId(), estimate.getDepositCode().getId().intValue(), null);
        final BigDecimal billAccBalAmount = egovCommon.getBillAccountBalanceforDate(getBillDate(), coaObj.getGlcode(), fundCode,
                adt.getId(),
                estimate.getDepositCode().getId().intValue());
        budgBalance = accBalAmount.add(billAccBalAmount);
        budgAmount = egovCommon.getDepositAmountForDepositCode(getBillDate(), coaObj.getGlcode(), fundCode, adt.getId(),
                estimate.getDepositCode().getId().intValue());

    }

    public String populateStandardDeductionType() {
        final List<CChartOfAccounts> coaStandardDedList = getStandardDeductionTypes();
        getServletResponse().setContentType("text/xml");
        getServletResponse().setHeader("Cache-Control", "no-cache");
        String coaString = null;
        for (final CChartOfAccounts coa : coaStandardDedList)
            if (coaString == null)
                coaString = coa.getId() + "/" + coa.getGlcode() + "-" + coa.getName();
            else
                coaString = coaString + "$" + coa.getId() + "/" + coa.getGlcode() + "-" + coa.getName();
        try {

            getServletResponse().getWriter().write(coaString);
        } catch (final IOException ioex) {
            logger.error("Error while writing to response --from getByResponseAware()");
        }
        return null;
    }

    /**
     * Convenience method to get the response
     *
     * @return current response
     */

    public HttpServletResponse getServletResponse() {
        return ServletActionContext.getResponse();
    }

    public List<CChartOfAccounts> getStandardDeductionTypes() {
        List<CChartOfAccounts> pList = null;// new ArrayList<CChartOfAccounts>();
        try {
            final Map<String, String[]> typeAndCodesMap = contractorBillService.getStandardDeductionsFromConfig();
            final String[] codes = typeAndCodesMap.get(deductionType);
            if (codes != null && codes.length != 0)
                for (final String code : codes) {
                    pList = chartOfAccountsHibernateDAO.getListOfDetailCode(code);
                    if (pList != null)
                        standardDeductionAccountList.addAll(pList);
                }
        } catch (final Exception e) {
            logger.error("--------------Error in fetching other deduction - AccountHeadsForStandardDeductionType-------------");
        }
        return standardDeductionAccountList;
    }

    public String getDebitAccountCodes() {
        final WorkOrderEstimate workOrderEstimate = (WorkOrderEstimate) persistenceService.find(
                "from WorkOrderEstimate where id=?",
                workOrderEstimateId);
        if (workOrderEstimate != null && workOrderEstimate.getId() != null) {
            isSkipBudgetCheck(workOrderEstimate.getId());
            addDropdownData(WorksConstants.ASSET_LIST, workOrderEstimate.getAssetValues());
            assetList = workOrderEstimate.getAssetValues();
            final String accountCodeFromBudgetHead = worksService.getWorksConfigValue("BILL_DEFAULT_BUDGETHEAD_ACCOUNTCODE");
            showValidationMsg = WorksConstants.NO;
            if (workOrderEstimate.getEstimate().getNatureOfWork().getCode().equals(WorksConstants.CAPITAL_WORKS)
                    || workOrderEstimate.getEstimate().getNatureOfWork().getCode().equals(WorksConstants.IMPROVEMENT_WORKS)) {
                if (StringUtils.isNotBlank(accountCodeFromBudgetHead) && "no".equals(accountCodeFromBudgetHead)
                        && StringUtils.isNotBlank(worksService.getWorksConfigValue(WorksConstants.KEY_CWIP))) {
                    coaList = chartOfAccountsHibernateDAO.getAccountCodeByPurpose(
                            Integer.valueOf(worksService.getWorksConfigValue(WorksConstants.KEY_CWIP)));
                    addDropdownData(WorksConstants.COA_LIST, coaList);
                } else if (StringUtils.isNotBlank(accountCodeFromBudgetHead) && "yes".equals(accountCodeFromBudgetHead)) {
                    final List<BudgetGroup> budgetGroupList = new ArrayList<BudgetGroup>();
                    if (workOrderEstimate.getEstimate().getFinancialDetails().get(0).getBudgetGroup() != null)
                        budgetGroupList.add(workOrderEstimate.getEstimate().getFinancialDetails().get(0).getBudgetGroup());
                    coaList = budgetService.getAccountCodeForBudgetHead(budgetGroupList);
                    addDropdownData(WorksConstants.COA_LIST, coaList);
                } else
                    coaList = Collections.EMPTY_LIST;
            } else if (workOrderEstimate.getEstimate().getNatureOfWork().getCode()
                    .equals(WorksConstants.REPAIR_AND_MAINTENANCE)) {
                if (StringUtils.isNotBlank(accountCodeFromBudgetHead) && "no".equals(accountCodeFromBudgetHead)
                        && StringUtils.isNotBlank(worksService.getWorksConfigValue(WorksConstants.KEY_REPAIRS))) {
                    coaList = chartOfAccountsHibernateDAO.getAccountCodeByPurpose(
                            Integer.valueOf(worksService.getWorksConfigValue(WorksConstants.KEY_REPAIRS)));
                    addDropdownData(WorksConstants.COA_LIST, coaList);
                } else if (StringUtils.isNotBlank(accountCodeFromBudgetHead) && "yes".equals(accountCodeFromBudgetHead)) {
                    final List<BudgetGroup> budgetGroupList = new ArrayList<BudgetGroup>();
                    if (workOrderEstimate.getEstimate().getFinancialDetails().get(0).getBudgetGroup() != null)
                        budgetGroupList.add(workOrderEstimate.getEstimate().getFinancialDetails().get(0).getBudgetGroup());
                    coaList = budgetService.getAccountCodeForBudgetHead(budgetGroupList);
                    addDropdownData(WorksConstants.COA_LIST, coaList);
                } else
                    coaList = Collections.EMPTY_LIST;
            } else if (getAppConfigValuesToSkipBudget().contains(workOrderEstimate.getEstimate().getNatureOfWork().getName()))
                if (StringUtils.isNotBlank(worksService.getWorksConfigValue(WorksConstants.KEY_DEPOSIT)) && checkBudget) {
                    // Story# 806 - Show all the CWIP codes in the contractor bill screen where we show the deposit COA code
                    // now
                    // coaList =
                    // chartOfAccountsHibernateDAO.getAccountCodeByPurpose(Integer.valueOf(worksService.getWorksConfigValue(WorksConstants.KEY_DEPOSIT)));
                    coaList = chartOfAccountsHibernateDAO.getAccountCodeByPurpose(
                            Integer.valueOf(worksService.getWorksConfigValue(WorksConstants.KEY_CWIP)));
                    addDropdownData(WorksConstants.COA_LIST, coaList);
                }
                // In case of deposit works the budget head is loaded based on the mapping table
                else if (StringUtils.isNotBlank(worksService.getWorksConfigValue(WorksConstants.KEY_DEPOSIT)) && !checkBudget
                        &&
                        workOrderEstimate.getEstimate().getFinancialDetails().get(0).getCoa() != null) {
                    // Story# 806 - Show all the CWIP codes in the contractor bill screen where we show the deposit COA code
                    // now
                    // coaList =
                    // Arrays.asList(chartOfAccountsHibernateDAO.getCChartOfAccountsByGlCode(workOrderEstimate.getEstimate().getFinancialDetails().get(0).getCoa().getGlcode()));
                    coaList = contractorBillService.getBudgetHeadForDepositCOA(workOrderEstimate.getEstimate());
                    if (!coaList.isEmpty())
                        addDropdownData(WorksConstants.COA_LIST, coaList);
                    else
                        coaList = Collections.EMPTY_LIST;
                    if (coaList.isEmpty()) {
                        showValidationMsg = WorksConstants.YES;
                        return WorksConstants.COA_LIST;
                    }
                } else
                    coaList = Collections.EMPTY_LIST;
        }
        return WorksConstants.COA_LIST;
    }

    public String getBudgetDetails() throws Exception {
        final WorkOrderEstimate workOrderEstimate = (WorkOrderEstimate) persistenceService
                .find("from WorkOrderEstimate woe where woe.estimate.id=?", getEstimateId());
        isSkipBudgetCheck(workOrderEstimate.getId());
        // if(checkBudget){
        setBudgetAmtAndBalForCapitalWorks(workOrderEstimate);
        /*
         * else{ setDespositWorksAccBal(); }
         */
        return BUDGET_DETAILS;
    }

    private void setBudgetAmtAndBalForCapitalWorks(final WorkOrderEstimate workOrderEstimate) {
        final Map<String, Object> queryParamMap = new HashMap<String, Object>();
        try {

            if (workOrderEstimate.getEstimate() != null && workOrderEstimate.getEstimate().getFinancialDetails().get(0) != null)
                financialDetail = workOrderEstimate.getEstimate().getFinancialDetails().get(0);

            if (financialDetail != null && financialDetail.getFund() != null && financialDetail.getFund().getId() != null &&
                    financialDetail.getFund().getId() != -1)
                queryParamMap.put("fundid", financialDetail.getFund().getId());

            if (financialDetail != null && financialDetail.getScheme() != null && financialDetail.getScheme().getId() != null &&
                    financialDetail.getScheme().getId() != -1)
                queryParamMap.put("schemeid", financialDetail.getScheme().getId());

            if (financialDetail != null && financialDetail.getSubScheme() != null
                    && financialDetail.getSubScheme().getId() != null &&
                    financialDetail.getSubScheme().getId() != -1)
                queryParamMap.put("subschemeid", financialDetail.getSubScheme().getId());

            if (financialDetail != null && financialDetail.getFunctionary() != null
                    && financialDetail.getFunctionary().getId() != null &&
                    financialDetail.getFunctionary().getId() != -1)
                queryParamMap.put("functionaryid", financialDetail.getFunctionary().getId());

            if (financialDetail != null && financialDetail.getAbstractEstimate().getWard() != null)
                queryParamMap.put("boundaryid", financialDetail.getAbstractEstimate().getWard().getId());

            if (financialDetail != null && financialDetail.getFunction() != null && financialDetail.getFunction().getId() != null
                    &&
                    financialDetail.getFunction().getId() != -1)
                queryParamMap.put("functionid", financialDetail.getFunction().getId());

            if (financialDetail != null && financialDetail.getAbstractEstimate().getUserDepartment() != null)
                queryParamMap.put("deptid", financialDetail.getAbstractEstimate().getUserDepartment().getId());

            if (getBillDate() != null) {
                final CFinancialYear finyear = getCurrentFinancialYear(getBillDate());
                if (finyear != null && finyear.getId() != null)
                    queryParamMap.put("financialyearid", finyear.getId());
                queryParamMap.put("toDate", getBillDate());
                queryParamMap.put("asondate", getBillDate());
            }
            if (getGlCodeId() != null) {
                final CChartOfAccounts coaObj = (CChartOfAccounts) persistenceService.find("from CChartOfAccounts where id=?",
                        getGlCodeId());
                queryParamMap.put("glcode", coaObj.getGlcode());
                queryParamMap.put("glcodeid", getGlCodeId());
                try {
                    final List<BudgetGroup> bgList = budgetDetailsDAO.getBudgetHeadByGlcode(coaObj);
                    if (bgList == null || bgList.isEmpty())
                        throw new ValidationException("",
                                "The Budget balance is not specified for the account code: " + coaObj.getGlcode());
                    queryParamMap.put("budgetheadid", bgList);
                } catch (final ValidationException valEx) {
                    errorMsg = "The Budget balance is not specified for the account code: " + coaObj.getGlcode();
                    logger.error(errorMsg);
                }
            }
            if (logger.isDebugEnabled())
                logger.debug("queryParamMap size >>>>>>>>>> " + queryParamMap.size());
            if (!queryParamMap.isEmpty() && getFieldErrors().isEmpty())
                try {
                    totalGrantAmount = budgetDetailsDAO.getBudgetedAmtForYear(queryParamMap);
                    totalUtilizedAmount = budgetDetailsDAO.getActualBudgetUtilizedForBudgetaryCheck(queryParamMap);
                    actualAmount = budgetDetailsDAO.getBillAmountForBudgetCheck(queryParamMap);
                    final BigDecimal budgetBalance = totalGrantAmount.subtract(totalUtilizedAmount.add(actualAmount));
                    budgAmount = totalGrantAmount;
                    budgBalance = budgetBalance;
                } catch (final ValidationException valEx) {
                    throw valEx;
                }
        } catch (final Exception e) {
            logger.error("--------------Error in fetching -------------" + e.getMessage());
        }
        if (logger.isDebugEnabled())
            logger.debug("totalGrantAmount>>>>>>>>> " + totalGrantAmount);
        if (logger.isDebugEnabled())
            logger.debug("totalUtilizedAmount>>>>>>>>> " + totalUtilizedAmount);
        if (logger.isDebugEnabled())
            logger.debug("actual amount>>>>>> " + actualAmount);
    }

    public CFinancialYear getCurrentFinancialYear(final Date billDate) {
        return finHibernateDao.getFinYearByDate(billDate);
    }

    public String getStatutoryDeductionAmount() throws Exception {
        getDeductionAmount();
        return STATUTORY_DEDUCTION_AMOUNT;
    }

    public void getDeductionAmount() throws Exception {
        String subPartyTypeCode = null;
        String typeOfWorkCode = null;
        try {
            try {
                if (!"0".equals(subPartyType)) {
                    EgPartytype egPartyType = new EgPartytype();
                    egPartyType = egPartytypeHibernateDAO.findById(Integer.valueOf(subPartyType));
                    subPartyTypeCode = egPartyType.getCode();
                }
                if (!"0".equals(typeOfWork)) {
                    EgwTypeOfWork egwTOW = new EgwTypeOfWork();
                    egwTOW = egwTypeOfWorkHibernateDAO.getTypeOfWorkById(Long.valueOf(typeOfWork));
                    typeOfWorkCode = egwTOW.getCode();
                }
                statutoryAmount = recoveryService.getDeductionAmount(recoveryType, PARTY_TYPE_CONTRACTOR, subPartyTypeCode,
                        typeOfWorkCode, grossAmount, billDate);
                if (logger.isDebugEnabled())
                    logger.debug("statutoryAmount>>>>>>>>> " + statutoryAmount);
            } catch (final ValidationException e) {
                errorMsg = e.getErrors().get(0).getMessage();
                logger.error(errorMsg);
            }
        } catch (final Exception e) {
            logger.error("--------------Error in fetching -------------" + e.getMessage());
            throw e;
        }
    }

    public String getProjectClosureEstimateForBill() {
        final WorkOrderEstimate woe = (WorkOrderEstimate) getPersistenceService()
                .find("select distinct mb.workOrderEstimate from MBHeader mb where mb.egBillregister.id=? and " +
                        " mb.egBillregister.billstatus='APPROVED' and mb.workOrderEstimate.estimate.projectCode.egwStatus.code='CLOSED'",
                        contractorBillId);
        if (woe != null)
            estimateNumber = woe.getEstimate().getEstimateNumber();
        else
            estimateNumber = "";
        return "projectClosureEstimate";
    }

    public String searchWorkOrderNumber() {
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(query)) {
            strquery = "select distinct mbh.workOrder from MBHeader mbh where mbh.workOrder.parent is null and mbh.egBillregister is not null "
                    +
                    "and mbh.egBillregister.billstatus <> ? and mbh.workOrder.workOrderNumber like '%'||?||'%' ";
            params.add("NEW");
            params.add(query.toUpperCase());

            workOrderList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return "workOrderNoSearchResults";
    }

    public String searchContractorBillNo() {
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(query)) {
            strquery = " from ContractorBillRegister cbr where cbr.billnumber like '%'||?||'%' and cbr.billstatus <> ? ";
            params.add(query.toUpperCase());
            params.add("NEW");
            contractorBillList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return "billNumberSearchResults";
    }

    public String searchEstimateNumber() {
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(query)) {
            strquery = "select woe.estimate from WorkOrderEstimate woe where woe.workOrder.parent is null and woe.estimate.estimateNumber like '%'||?||'%' "
                    +
                    " and woe.id in (select distinct mbh.workOrderEstimate.id from MBHeader mbh where mbh.egBillregister.id in (select egbr.id from EgBillregister egbr where egbr.billstatus <> ?) )";
            params.add(query.toUpperCase());
            params.add("NEW");
            estimateList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return "estimateNoSearchResults";
    }

    public String getLatestMBDateCreatedAndRefNum() {
        final Object[] mbDateRefNo = contractorBillService.getLatestMBCreatedDateAndRefNo(woId, estId);
        refNo = (String) mbDateRefNo[0];
        latestMBDate = (Date) mbDateRefNo[1];
        return "mblatestDateResult";
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(final Date billDate) {
        this.billDate = billDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {

        this.id = id;
    }

    public EgBillregister getEgBillregister() {
        return egBillregister;
    }

    public void setEgBillregister(final EgBillregister egBillregister) {
        this.egBillregister = egBillregister;
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

    public BigDecimal getTotalPendingBalance() {
        return totalPendingBalance;
    }

    public void setDeductionType(final String deductionType) {
        this.deductionType = deductionType;
    }

    public List<CChartOfAccounts> getStandardDeductionAccountList() {
        return standardDeductionAccountList;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(final String rowId) {
        this.rowId = rowId;
    }

    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(final String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(final String billType) {
        this.billType = billType;
    }

    public void setMeasurementBookService(
            final MeasurementBookServiceImpl measurementBookService) {
        this.measurementBookService = measurementBookService;
    }

    public BigDecimal getTotalUtilizedAmount() {
        return totalUtilizedAmount;
    }

    public void setTotalUtilizedAmount(final BigDecimal totalUtilizedAmount) {
        this.totalUtilizedAmount = totalUtilizedAmount;
    }

    public List<AppConfigValues> getFinalBillChecklist() {
        return finalBillChecklist;
    }

    public void setFinalBillChecklist(final List<AppConfigValues> finalBillChecklist) {
        this.finalBillChecklist = finalBillChecklist;
    }

    public BigDecimal getTotalWorkValueRecorded() {
        return totalWorkValueRecorded;
    }

    public void setTotalWorkValueRecorded(final BigDecimal totalWorkValueRecorded) {
        this.totalWorkValueRecorded = totalWorkValueRecorded;
    }

    public List<MBHeader> getApprovedMBHeaderList() {
        return approvedMBHeaderList;
    }

    public void setApprovedMBHeaderList(final List<MBHeader> approvedMBHeaderList) {
        this.approvedMBHeaderList = approvedMBHeaderList;
    }

    public BigDecimal getBudgetSanctionAmount() {
        return budgetSanctionAmount;
    }

    public void setBudgetSanctionAmount(final BigDecimal budgetSanctionAmount) {
        this.budgetSanctionAmount = budgetSanctionAmount;
    }

    public List<String> getChecklistValues() {
        return checklistValues;
    }

    public void setChecklistValues(final List<String> checklistValues) {
        this.checklistValues = checklistValues;
    }

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public String[] getSelectedchecklistvalues() {
        return selectedchecklistvalues;
    }

    public void setSelectedchecklistvalues(final String[] selectedchecklistvalues) {
        this.selectedchecklistvalues = selectedchecklistvalues;
    }

    public Long getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(final Long estimateId) {
        this.estimateId = estimateId;
    }

    public List<AssetsForWorkOrder> getAssetList() {
        return assetList;
    }

    public void setAssetList(final List<AssetsForWorkOrder> assetList) {
        this.assetList = assetList;
    }

    public List<CChartOfAccounts> getCoaList() {
        return coaList;
    }

    public void setCoaList(final List<CChartOfAccounts> coaList) {
        this.coaList = coaList;
    }

    public WorkCompletionInfo getWorkcompletionInfo() {
        return workcompletionInfo;
    }

    public void setWorkcompletionInfo(final WorkCompletionInfo workcompletionInfo) {
        this.workcompletionInfo = workcompletionInfo;
    }

    public List<WorkCompletionDetailInfo> getWorkCompletionDetailInfo() {
        return workCompletionDetailInfo;
    }

    public void setWorkCompletionDetailInfo(
            final List<WorkCompletionDetailInfo> workCompletionDetailInfo) {
        this.workCompletionDetailInfo = workCompletionDetailInfo;
    }

    public Long getGlCode() {
        return glCode;
    }

    public void setGlCode(final Long glCode) {
        this.glCode = glCode;
    }

    public BigDecimal getBudgBalance() {
        return budgBalance;
    }

    public void setBudgBalance(final BigDecimal budgBalance) {
        this.budgBalance = budgBalance;
    }

    public BigDecimal getBudgAmount() {
        return budgAmount;
    }

    public void setBudgAmount(final BigDecimal budgAmount) {
        this.budgAmount = budgAmount;
    }

    public boolean getCheckBudget() {
        return checkBudget;
    }

    public void setCheckBudget(final boolean checkBudget) {
        this.checkBudget = checkBudget;
    }

    public void setBudgetDetailsDAO(final BudgetDetailsDAO budgetDetailsDAO) {
        this.budgetDetailsDAO = budgetDetailsDAO;
    }

    public Long getWorkOrderEstimateId() {
        return workOrderEstimateId;
    }

    public void setWorkOrderEstimateId(final Long workOrderEstimateId) {
        this.workOrderEstimateId = workOrderEstimateId;
    }

    public Long getGlCodeId() {
        return glCodeId;
    }

    public void setGlCodeId(final Long glCodeId) {
        this.glCodeId = glCodeId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(final String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setEgovCommon(final EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }

    public String getSubPartyType() {
        return subPartyType;
    }

    public void setSubPartyType(final String subPartyType) {
        this.subPartyType = subPartyType;
    }

    public String getTypeOfWork() {
        return typeOfWork;
    }

    public void setTypeOfWork(final String typeOfWork) {
        this.typeOfWork = typeOfWork;
    }

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(final BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public void setStatutoryAmount(final BigDecimal statutoryAmount) {
        this.statutoryAmount = statutoryAmount;
    }

    public BigDecimal getStatutoryAmount() {
        return statutoryAmount;
    }

    public String getRecoveryType() {
        return recoveryType;
    }

    public void setRecoveryType(final String recoveryType) {
        this.recoveryType = recoveryType;
    }

    public BigDecimal getCumulativeBillValue() {
        return cumulativeBillValue;
    }

    public void setCumulativeBillValue(final BigDecimal cumulativeBillValue) {
        this.cumulativeBillValue = cumulativeBillValue;
    }

    public List<String> getAppConfigValuesToSkipBudget() {
        return worksService.getNatureOfWorkAppConfigValues("Works", APPCONFIG_KEY_NAME);
    }

    public BigDecimal getTotalTenderedItemsAmt() {
        return totalTenderedItemsAmt;
    }

    public void setTotalTenderedItemsAmt(final BigDecimal totalTenderedItemsAmt) {
        this.totalTenderedItemsAmt = totalTenderedItemsAmt;
    }

    public Long getContractorBillId() {
        return contractorBillId;
    }

    public void setContractorBillId(final Long contractorBillId) {
        this.contractorBillId = contractorBillId;
    }

    public String getEstimateNumber() {
        return estimateNumber;
    }

    public void setEstimateNumber(final String estimateNumber) {
        this.estimateNumber = estimateNumber;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public List<WorkOrder> getWorkOrderList() {
        return workOrderList;
    }

    public void setWorkOrderList(final List<WorkOrder> workOrderList) {
        this.workOrderList = workOrderList;
    }

    public List<ContractorBillRegister> getContractorBillList() {
        return contractorBillList;
    }

    public void setContractorBillList(
            final List<ContractorBillRegister> contractorBillList) {
        this.contractorBillList = contractorBillList;
    }

    public boolean getNoMBsPresent() {
        return noMBsPresent;
    }

    public void setNoMBsPresent(final boolean noMBsPresent) {
        this.noMBsPresent = noMBsPresent;
    }

    public String getLatestBillDateStr() {
        return latestBillDateStr;
    }

    public String getTrackMlsCheck() {
        return trackMlsCheck;
    }

    public List<AbstractEstimate> getEstimateList() {
        return estimateList;
    }

    public void setEstimateList(final List<AbstractEstimate> estimateList) {
        this.estimateList = estimateList;
    }

    public String getYearEndApprCheck() {
        return yearEndApprCheck;
    }

    public void setYearEndApprCheck(final String yearEndApprCheck) {
        this.yearEndApprCheck = yearEndApprCheck;
    }

    public String getEstimateNo() {
        return estimateNo;
    }

    public void setEstimateNo(final String estimateNo) {
        this.estimateNo = estimateNo;
    }

    public void setContractorAdvanceService(
            final ContractorAdvanceService contractorAdvanceService) {
        this.contractorAdvanceService = contractorAdvanceService;
    }

    public BigDecimal getTotalAdvancePaid() {
        return totalAdvancePaid;
    }

    public void setTotalAdvancePaid(final BigDecimal totalAdvancePaid) {
        this.totalAdvancePaid = totalAdvancePaid;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(final Long billId) {
        this.billId = billId;
    }

    public String getAdvanceRequisitionNo() {
        return advanceRequisitionNo;
    }

    public void setAdvanceRequisitionNo(final String advanceRequisitionNo) {
        this.advanceRequisitionNo = advanceRequisitionNo;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public String getArfInWorkFlowCheck() {
        return arfInWorkFlowCheck;
    }

    public void setArfInWorkFlowCheck(final String arfInWorkFlowCheck) {
        this.arfInWorkFlowCheck = arfInWorkFlowCheck;
    }

    public void setEmployeeService(final EmployeeServiceOld employeeService) {
        this.employeeService = employeeService;
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

    public Long getEstId() {
        return estId;
    }

    public void setEstId(final Long estId) {
        this.estId = estId;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(final String refNo) {
        this.refNo = refNo;
    }

    public Long getWoId() {
        return woId;
    }

    public void setWoId(final Long woId) {
        this.woId = woId;
    }

}