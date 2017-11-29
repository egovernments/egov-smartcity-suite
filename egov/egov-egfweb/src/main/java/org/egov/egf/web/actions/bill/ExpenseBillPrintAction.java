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
package org.egov.egf.web.actions.bill;


import net.sf.jasperreports.engine.JRException;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.utils.EntityType;
import org.egov.dao.budget.BudgetDetailsHibernateDAO;
import org.egov.egf.budget.model.BudgetControlType;
import org.egov.egf.budget.service.BudgetControlTypeService;
import org.egov.egf.commons.EgovCommon;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.reporting.util.ReportUtil;
import org.egov.infra.utils.NumberToWordConverter;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.EgBillregistermis;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.voucher.VoucherDetails;
import org.egov.pims.commons.Position;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Results(value = {

        @Result(name = "PDF", type = "stream", location = "inputStream", params = {"inputName", "inputStream", "contentType",
                "application/pdf", "contentDisposition", "no-cache;filename=ExpenseJournalVoucherReport.pdf"}),
        @Result(name = "XLS", type = "stream", location = "inputStream", params = {"inputName", "inputStream", "contentType",
                "application/xls", "contentDisposition", "no-cache;filename=ExpenseJournalVoucherReport.xls"}),
        @Result(name = "HTML", type = "stream", location = "inputStream", params = {"inputName", "inputStream", "contentType",
                "text/html"})
})
@org.apache.struts2.convention.annotation.ParentPackage("egov")
public class ExpenseBillPrintAction extends BaseFormAction {
    private static final Logger LOGGER = Logger.getLogger(ExpenseBillPrintAction.class);
    private static final long serialVersionUID = 1L;
    private static final String PRINT = "print";
    private static final String ACCDETAILTYPEQUERY = " from Accountdetailtype where id=?";
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private static final String jasperpath = "/reports/templates/expenseBillReport.jasper";
    private static final String subReportPath = "/reports/templates/budgetAppropriationDetail.jasper";
    private String functionName;
    @Autowired
    private transient AppConfigValueService appConfigValuesService;
    private transient Map<String, Object> budgetDataMap = new HashMap<>();
    private transient Map<String, Object> paramMap = new HashMap<>();
    private transient List<Object> billReportList = new ArrayList<>();
    private transient InputStream inputStream;
    private transient ReportHelper reportHelper;
    private Long id;
    private transient EgBillregistermis billRegistermis;
    private transient EgBillregister cbill = new EgBillregister();
    @Autowired
    private transient EisCommonService eisCommonService;
    private transient BudgetDetailsHibernateDAO budgetDetailsDAO;
    @Autowired
    private transient FinancialYearDAO financialYearDAO;
    @Autowired
    private transient EgovCommon egovCommon;
    private transient CVoucherHeader voucher = new CVoucherHeader();
    @Autowired
    private transient BudgetControlTypeService budgetControlTypeService;

    public BudgetDetailsHibernateDAO getBudgetDetailsDAO() {
        return budgetDetailsDAO;
    }

    public void setBudgetDetailsDAO(final BudgetDetailsHibernateDAO budgetDetailsDAO) {
        this.budgetDetailsDAO = budgetDetailsDAO;
    }

    public String getFunctionName() {
        return functionName;
    }

    /**
     * @param name
     */
    private void setFunctionName(final String name) {
        functionName = name;

    }

    public List<Object> getBillReportList() {
        return billReportList;
    }

    public void setBillReportList(final List<Object> billReportList) {
        this.billReportList = billReportList;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setReportHelper(final ReportHelper helper) {
        reportHelper = helper;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public String execute() {
        return print();
    }

    @SkipValidation
    @Action(value = "/bill/expenseBillPrint-ajaxPrint")
    public String ajaxPrint() {
        return exportHtml();
    }

    @Override
    public Object getModel() {
        return voucher;
    }

    @Action(value = "/bill/expenseBillPrint-print")
    public String print() {
        return PRINT;
    }

    private void populateBill() {
        if (parameters.get("id") != null && !parameters.get("id")[0].isEmpty()) {
            cbill = (EgBillregister) persistenceService.find("from EgBillregister where id=?",
                    Long.valueOf(parameters.get("id")[0]));
            billRegistermis = cbill.getEgBillregistermis();
        }

        generateVoucherReportList();

    }

    private void generateVoucherReportList() {
        prepareForPrint();
    }

    @Action(value = "/bill/expenseBillPrint-exportPdf")
    public String exportPdf() throws JRException, IOException {
        populateBill();
        inputStream = reportHelper.exportPdf(inputStream, jasperpath, getParamMap(), billReportList);
        return "PDF";
    }

    public String exportHtml() {
        populateBill();
        inputStream = reportHelper.exportHtml(inputStream, jasperpath, getParamMap(), billReportList, "px");
        return "HTML";
    }

    @Action(value = "/bill/expenseBillPrint-exportXls")
    public String exportXls() throws JRException, IOException {
        populateBill();
        inputStream = reportHelper.exportXls(inputStream, jasperpath, getParamMap(), billReportList);
        return "XLS";
    }

    protected Map<String, Object> getParamMap() {

        paramMap.put("billNumber", cbill.getBillnumber());
        if (cbill.getBilldate() != null)
            paramMap.put("billDate", sdf.format(cbill.getBilldate()));
        paramMap.put("voucherDescription", getVoucherDescription());
        if (cbill.getState() != null)
            loadInboxHistoryData(cbill.getStateHistory(), paramMap);

        if (billRegistermis != null) {
            paramMap.put("billDate", Constants.DDMMYYYYFORMAT2.format(billRegistermis.getEgBillregister().getBilldate()));
            paramMap.put("partyBillNumber", billRegistermis.getPartyBillNumber());
            paramMap.put("serviceOrder", billRegistermis.getEgBillregister().getNarration());
            paramMap.put("partyName", billRegistermis.getPayto());
            if (billRegistermis.getPartyBillDate() != null)
                paramMap.put("partyBillDate", sdf.format(billRegistermis.getPartyBillDate()));
            paramMap.put("netAmount", cbill.getPassedamount());
            final BigDecimal amt = cbill.getPassedamount().setScale(2, BigDecimal.ROUND_HALF_EVEN);
            String amountInWords = NumberToWordConverter.amountInWordsWithCircumfix(amt);
            amountInWords = "(" + amountInWords + " )";
            amountInWords = "Bill is in order. Sanction is accorded for Rs." + amt + "/-" + amountInWords;
            paramMap.put("netAmountText", amountInWords);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("amountInWords" + amountInWords);
            paramMap.put("netAmountInWords", amountInWords);
            paramMap.put("billNumber", billRegistermis.getEgBillregister().getBillnumber());
            paramMap.put("functionName", getFunctionName());
            paramMap.put("departmentName", billRegistermis.getEgDepartment().getName());
            paramMap.put("fundName", billRegistermis.getFund().getName());
            billRegistermis.getEgBillregister().getBillamount();
            paramMap.put("budgetApprNumber", billRegistermis.getBudgetaryAppnumber());
            paramMap.put("budgetAppropriationDetailJasper", reportHelper.getClass().getResourceAsStream(subReportPath));
        }
        paramMap.put("ulbName", ReportUtil.getCityName());
        return paramMap;
    }

    /**
     * @param paramMap
     * @return
     */
    private Map<String, Object> getBudgetDetails(final CChartOfAccounts coa, final EgBilldetails billDetail,
                                                 final String functionName) {
        final Map<String, Object> budgetApprDetailsMap = new HashMap<>();
        budgetDataMap.put(Constants.FUNCTIONID, Long.valueOf(billDetail.getFunctionid().toString()));
        if (cbill.getEgBillregistermis().getVoucherHeader() != null)
            budgetDataMap.put(Constants.ASONDATE, cbill.getEgBillregistermis().getVoucherHeader().getVoucherDate());// this date
            // plays
            // important
            // roles
        else
            budgetDataMap.put(Constants.ASONDATE, cbill.getBilldate());

        Date billDate = cbill.getBilldate();
        final CFinancialYear financialYearById = financialYearDAO.getFinYearByDate(billDate);

        budgetApprDetailsMap.put("financialYear", "BE-" + financialYearById.getFinYearRange() + " & Addl Funds(Rs)");
        budgetDataMap.put("fromdate", financialYearById.getStartingDate());

        budgetDataMap.put("glcode", coa.getGlcode());
        budgetDataMap.put("glcodeid", coa.getId());
        final List<BudgetGroup> budgetHeadByGlcode = budgetDetailsDAO.getBudgetHeadByGlcode(coa);
        budgetDataMap.put("budgetheadid", budgetHeadByGlcode);
        final BigDecimal budgetedAmtForYear = budgetDetailsDAO.getBudgetedAmtForYear(budgetDataMap);
        paramMap.put("budgetedAmtForYear", budgetedAmtForYear);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("budgetedAmtForYear .......... " + budgetedAmtForYear);

        budgetDataMap.put("budgetApprNumber", cbill.getEgBillregistermis().getBudgetaryAppnumber());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Getting actuals .............................. for print");

        final BigDecimal actualAmtFromVoucher = budgetDetailsDAO.getActualBudgetUtilizedForBudgetaryCheck(budgetDataMap); // get
        // actual
        // amount
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("actualAmtFromVoucher .............................. " + actualAmtFromVoucher);
        budgetDataMap.put(Constants.ASONDATE, cbill.getBilldate());
        final BigDecimal actualAmtFromBill = budgetDetailsDAO.getBillAmountForBudgetCheck(budgetDataMap); // get actual amount
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("actualAmtFromBill .............................. " + actualAmtFromBill);

        BigDecimal currentBillAmount;
        BigDecimal soFarAppropriated;
        BigDecimal actualAmount = actualAmtFromVoucher != null ? actualAmtFromVoucher : BigDecimal.ZERO;
        actualAmount = actualAmtFromBill != null ? actualAmount.add(actualAmtFromBill) : actualAmount;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("actualAmount ...actualAmtFromVoucher+actualAmtFromBill........ " + actualAmount);

        if (billDetail.getDebitamount() != null && billDetail.getDebitamount().compareTo(BigDecimal.ZERO) != 0) {
            actualAmount = actualAmount.subtract(billDetail.getDebitamount());
            currentBillAmount = billDetail.getDebitamount();

        } else {
            actualAmount = actualAmount.subtract(billDetail.getCreditamount());
            currentBillAmount = billDetail.getCreditamount();
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("actualAmount ...actualAmount-billamount........ " + actualAmount);
        BigDecimal balance = budgetedAmtForYear;

        balance = balance.subtract(actualAmount);
        soFarAppropriated = actualAmount;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("soFarAppropriated ...actualAmount==soFarAppropriated........ " + soFarAppropriated);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("balance ...budgetedAmtForYear-actualAmount........ " + balance);
        final BigDecimal cumilativeIncludingCurrentBill = soFarAppropriated.add(currentBillAmount);
        final BigDecimal currentBalanceAvailable = balance.subtract(currentBillAmount);
        budgetApprDetailsMap.put("budgetApprNumber", cbill.getEgBillregistermis().getBudgetaryAppnumber());
        budgetApprDetailsMap.put("budgetedAmtForYear", budgetedAmtForYear);
        budgetApprDetailsMap.put("soFarAppropriated", soFarAppropriated);
        budgetApprDetailsMap.put("balance", balance);
        budgetApprDetailsMap.put("cumilativeIncludingCurrentBill", cumilativeIncludingCurrentBill);
        budgetApprDetailsMap.put("currentBalanceAvailable", currentBalanceAvailable);
        budgetApprDetailsMap.put("currentBillAmount", currentBillAmount);
        budgetApprDetailsMap.put("AccountCode", coa.getGlcode());

        budgetApprDetailsMap.put("departmentName", cbill.getEgBillregistermis().getEgDepartment().getName());
        budgetApprDetailsMap.put("functionName", functionName);
        budgetApprDetailsMap.put("fundName", cbill.getEgBillregistermis().getFund().getName());

        return budgetApprDetailsMap;

    }

    /**
     * @param cbill will set data in budgetDataMap will be called only once per bill
     */
    private void getRequiredDataForBudget(final EgBillregister cbill) {
        Date billDate = cbill.getBilldate();
        final CFinancialYear financialYearById = financialYearDAO.getFinYearByDate(billDate);

        budgetDataMap.put("financialyearid", financialYearById.getId());

        budgetDataMap.put(Constants.DEPTID, cbill.getEgBillregistermis().getEgDepartment().getId());
        if (cbill.getEgBillregistermis().getFunctionaryid() != null)
            budgetDataMap.put(Constants.FUNCTIONARYID, cbill.getEgBillregistermis().getFunctionaryid().getId());
        if (cbill.getEgBillregistermis().getScheme() != null)
            budgetDataMap.put(Constants.SCHEMEID, cbill.getEgBillregistermis().getScheme().getId());
        if (cbill.getEgBillregistermis().getSubScheme() != null)
            budgetDataMap.put(Constants.SUBSCHEMEID, cbill.getEgBillregistermis().getSubScheme().getId());
        budgetDataMap.put(Constants.FUNDID, cbill.getEgBillregistermis().getFund().getId());
        budgetDataMap.put(Constants.BOUNDARYID, cbill.getDivision());

    }

    public Map<String, Object> getAccountDetails(final Integer detailtypeid, final Integer detailkeyid,
                                                 final Map<String, Object> tempMap) throws ApplicationException {
        final Accountdetailtype detailtype = (Accountdetailtype) getPersistenceService().find(ACCDETAILTYPEQUERY, detailtypeid);
        tempMap.put("detailtype", detailtype.getName());
        tempMap.put("detailtypeid", detailtype.getId());
        tempMap.put("detailkeyid", detailkeyid);
        egovCommon.setPersistenceService(persistenceService);
        final EntityType entityType = egovCommon.getEntityType(detailtype, detailkeyid);
        tempMap.put(Constants.DETAILKEY, entityType.getName());
        tempMap.put(Constants.DETAILCODE, entityType.getCode());
        return tempMap;
    }

    private String getVoucherDescription() {
        return voucher == null || voucher.getDescription() == null ? "" : voucher.getDescription();
    }

    void loadInboxHistoryData(List<StateHistory<Position>> stateHistory,
                              final Map<String, Object> paramMap) {
        final List<String> history = new ArrayList<>();
        final List<String> workFlowDate = new ArrayList<>();

        if (!stateHistory.isEmpty()) {
            for (final StateHistory historyState : stateHistory)

                if (!"NEW".equalsIgnoreCase(historyState.getValue())) {
                    history.add(historyState.getSenderName());
                    workFlowDate.add(Constants.DDMMYYYYFORMAT2
                            .format(historyState.getLastModifiedDate()));
                    if (historyState.getValue().equalsIgnoreCase("Rejected")) {
                        history.clear();
                        workFlowDate.clear();
                    }
                }

            history.add(cbill.getState().getSenderName());
            workFlowDate.add(Constants.DDMMYYYYFORMAT2.format(cbill.getState()
                    .getLastModifiedDate()));
        } else {
            history.add(cbill.getState().getSenderName());
            workFlowDate.add(Constants.DDMMYYYYFORMAT2.format(cbill.getState()
                    .getLastModifiedDate()));
        }
        for (int i = 0; i < history.size(); i++) {
            paramMap.put("workFlow_" + i, history.get(i));
            paramMap.put("workFlowDate_" + i, workFlowDate.get(i));
        }

    }

    private void prepareForPrint() {

        final Set<EgBilldetails> egBilldetailes = cbill.getEgBilldetailes();
        boolean budgetcheck = false;

        if (!BudgetControlType.BudgetCheckOption.NONE.toString().equalsIgnoreCase(budgetControlTypeService.getConfigValue())) {
            budgetcheck = true;
            getRequiredDataForBudget(cbill);
        }

        final List<Map<String, Object>> budget = new ArrayList<>();
        for (final EgBilldetails detail : egBilldetailes)
            if (detail.getDebitamount() != null && detail.getDebitamount().compareTo(BigDecimal.ZERO) != 0) {
                CFunction functionById = null;
                Map<String, Object> budgetApprDetails = null;

                final VoucherDetails vd = new VoucherDetails();
                final BigDecimal glcodeid = detail.getGlcodeid();
                if (detail.getFunctionid() != null) {
                    functionById = (CFunction) persistenceService.find("from CFunction where id=?",
                            Long.valueOf(detail.getFunctionid().toString()));
                    setFunctionName(functionById.getName());
                    paramMap.put("functionName", functionById.getName());
                }
                final CChartOfAccounts coa = (CChartOfAccounts) persistenceService.find("from CChartOfAccounts where id=?",
                        Long.valueOf(glcodeid.toString()));
                if (budgetcheck && coa.getBudgetCheckReq() != null && coa.getBudgetCheckReq()) {
                    budgetApprDetails = getBudgetDetails(coa, detail, functionById.getName());
                    budget.add(budgetApprDetails);
                }
                vd.setGlcodeDetail(coa.getGlcode());
                vd.setGlcodeIdDetail(coa.getId());
                vd.setAccounthead(coa.getName());
                vd.setCreditAmountDetail(BigDecimal.ZERO);
                vd.setDebitAmountDetail(detail.getDebitamount());
                final Set<EgBillPayeedetails> egBillPaydetailes = detail.getEgBillPaydetailes();
                for (final EgBillPayeedetails payeedetail : egBillPaydetailes) {
                    try {
                        EntityType entity = null;
                        final Accountdetailtype detailType = (Accountdetailtype) persistenceService.find(
                                "from Accountdetailtype where id=? order by name", payeedetail.getAccountDetailTypeId());
                        vd.setDetailTypeName(detailType.getName());

                        final Class<?> service = Class.forName(detailType.getFullQualifiedName());
                        // getting the entity type service.
                        final String detailTypeName = service.getSimpleName();
                        String dataType = "";
                        final java.lang.reflect.Method method = service.getMethod("getId");
                        dataType = method.getReturnType().getSimpleName();
                        if (dataType.equals("Long"))
                            entity = (EntityType) persistenceService.find(
                                    "from " + detailTypeName + " where id=? order by name", payeedetail.getAccountDetailKeyId()
                                            .longValue());
                        else
                            entity = (EntityType) persistenceService.find(
                                    "from " + detailTypeName + " where id=? order by name", payeedetail.getAccountDetailKeyId());
                        vd.setDetailKey(entity.getCode());
                        vd.setDetailName(entity.getName());
                    } catch (final Exception e) {
                        final List<ValidationError> errors = new ArrayList<>();
                        errors.add(new ValidationError("exp", e.getMessage()));
                        throw new ValidationException(errors);
                    }

                }

                final BillReport billReport = new BillReport(persistenceService, vd, cbill, budgetApprDetails);
                billReportList.add(billReport);
            }
        for (final EgBilldetails detail : egBilldetailes)
            if (detail.getCreditamount() != null && detail.getCreditamount().compareTo(BigDecimal.ZERO) != 0) {
                CFunction functionById = null;
                Map<String, Object> budgetApprDetails = null;

                final VoucherDetails vd = new VoucherDetails();
                final BigDecimal glcodeid = detail.getGlcodeid();
                if (detail.getFunctionid() != null) {
                    functionById = (CFunction) persistenceService.find("from CFunction where id=?",
                            Long.valueOf(detail.getFunctionid().toString()));
                    setFunctionName(functionById.getName());
                    paramMap.put("functionName", functionById.getName());
                }
                final CChartOfAccounts coa = (CChartOfAccounts) persistenceService.find("from CChartOfAccounts where id=?",
                        Long.valueOf(glcodeid.toString()));
                if (budgetcheck && coa.getBudgetCheckReq() != null && coa.getBudgetCheckReq()) {
                    budgetApprDetails = getBudgetDetails(coa, detail, functionName);
                    budget.add(budgetApprDetails);
                }
                vd.setGlcodeDetail(coa.getGlcode());
                vd.setGlcodeIdDetail(coa.getId());
                vd.setAccounthead(coa.getName());
                vd.setCreditAmountDetail(detail.getCreditamount());
                vd.setDebitAmountDetail(BigDecimal.ZERO);
                final Set<EgBillPayeedetails> egBillPaydetailes = detail.getEgBillPaydetailes();
                for (final EgBillPayeedetails payeedetail : egBillPaydetailes) {

                    try {
                        EntityType entity = null;
                        final Accountdetailtype detailType = (Accountdetailtype) persistenceService.find(
                                "from Accountdetailtype where id=? order by name", payeedetail.getAccountDetailTypeId());
                        vd.setDetailTypeName(detailType.getName());

                        final Class<?> service = Class.forName(detailType.getFullQualifiedName());
                        // getting the entity type service.
                        final String detailTypeName = service.getSimpleName();
                        String dataType = "";
                        final java.lang.reflect.Method method = service.getMethod("getId");
                        dataType = method.getReturnType().getSimpleName();
                        if (dataType.equals("Long"))
                            entity = (EntityType) persistenceService.find(
                                    "from " + detailTypeName + " where id=? order by name", payeedetail.getAccountDetailKeyId()
                                            .longValue());
                        else
                            entity = (EntityType) persistenceService.find(
                                    "from " + detailTypeName + " where id=? order by name", payeedetail.getAccountDetailKeyId());
                        vd.setDetailKey(entity.getCode());
                        vd.setDetailName(entity.getName());
                    } catch (final Exception e) {
                        final List<ValidationError> errors = new ArrayList<>();
                        errors.add(new ValidationError("exp", e.getMessage()));
                        throw new ValidationException(errors);
                    }
                }

                final BillReport billReport = new BillReport(persistenceService, vd, cbill, budgetApprDetails);
                billReportList.add(billReport);
            }
        paramMap.put("budgetDetail", budget);

    }

    public AppConfigValueService getAppConfigValuesService() {
        return appConfigValuesService;
    }

    public void setAppConfigValuesService(
            AppConfigValueService appConfigValuesService) {
        this.appConfigValuesService = appConfigValuesService;
    }

    public EisCommonService getEisCommonService() {
        return eisCommonService;
    }

    public void setEisCommonService(EisCommonService eisCommonService) {
        this.eisCommonService = eisCommonService;
    }

    public FinancialYearDAO getFinancialYearDAO() {
        return financialYearDAO;
    }

    public void setFinancialYearDAO(FinancialYearDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

}