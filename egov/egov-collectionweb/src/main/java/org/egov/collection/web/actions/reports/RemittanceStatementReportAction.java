/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

/**
 *
 */
package org.egov.collection.web.actions.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.CollectionBankRemittanceReport;
import org.egov.collection.entity.CollectionRemittanceReportResult;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.entity.Remittance;
import org.egov.collection.entity.RemittanceDetail;
import org.egov.collection.service.RemittanceServiceImpl;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.eis.entity.Employee;
import org.egov.eis.entity.Jurisdiction;
import org.egov.eis.service.EmployeeService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportDataSourceType;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.web.struts.actions.ReportFormAction;
import org.egov.model.masters.AccountCodePurpose;
import org.springframework.beans.factory.annotation.Autowired;

@Results({ @Result(name = RemittanceStatementReportAction.INDEX, location = "remittanceStatementReport-index.jsp"),
        @Result(name = RemittanceStatementReportAction.REPORT, location = "remittanceStatementReport-report.jsp") })
@ParentPackage("egov")
public class RemittanceStatementReportAction extends ReportFormAction {

    private static final long serialVersionUID = 1L;
    private CollectionsUtil collectionsUtil;
    private static final String EGOV_FROM_DATE = "EGOV_FROM_DATE";
    private static final String EGOV_TO_DATE = "EGOV_TO_DATE";
    private static final String EGOV_SERVICE_ID = "EGOV_SERVICE_ID";
    private static final String EGOV_FUND_ID = "EGOV_FUND_ID";
    private static final String EGOV_BANKBRANCH_ID = "EGOV_BANKBRANCH_ID";
    private static final String EGOV_BANKACCOUNT_ID = "EGOV_BANKACCOUNT_ID";
    private static final String EGOV_PAYMENT_MODE = "EGOV_PAYMENT_MODE";
    private static final String SELECTED_DEPT_ID = "SELECTED_DEPT_ID";
    private static final String EGOV_DEPT_ID = "EGOV_DEPT_ID";
    private static final String EGOV_CASH_AMOUNT = "EGOV_CASH_AMOUNT";
    private static final String EGOV_CHEQUE_AMOUNT = "EGOV_CHEQUE_AMOUNT";
    private static final String EGOV_BANK = "EGOV_BANK";
    private static final String EGOV_BANK_ACCOUNT = "EGOV_BANK_ACCOUNT";
    private static final String EGOV_REMITTANCE_VOUCHER = "EGOV_REMITTANCE_VOUCHER";
    private static final String EGOV_REMITTANCE_DATE = "EGOV_REMITTANCE_DATE";
    private static final String PRINT_BANK_CHALLAN_TEMPLATE = "collection_remittance_bankchallan_report";
    private final Map<String, Object> critParams = new HashMap<String, Object>(0);
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private ReportViewerUtil reportViewerUtil;
    private String reportId;

    private final Map<String, String> paymentModes = createPaymentModeList();
    private List<CollectionBankRemittanceReport> bankRemittanceList;
    private Double totalCashAmount;
    private Double totalChequeAmount;
    private String bank;
    private String bankAccount;
    private Date remittanceDate;
    private String voucherNumber;
    private RemittanceServiceImpl remittanceService;

    @Override
    public void prepare() {
        setReportFormat(ReportFormat.PDF);
        setDataSourceType(ReportDataSourceType.SQL);
    }

    @Override
    @Action(value = "/reports/remittanceStatementReport-criteria")
    public String criteria() {

        addDropdownData("collectionServiceList",
                persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_COLLECTION_SERVICS));
        addDropdownData("collectionFundList",
                persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_ALL_FUND));
        critParams.put(EGOV_FROM_DATE, new Date());
        critParams.put(EGOV_TO_DATE, new Date());
        setReportParam(EGOV_FROM_DATE, new Date());
        setReportParam(EGOV_TO_DATE, new Date());
        addDropdownData("bankList", Collections.EMPTY_LIST);
        addDropdownData("bankAccountList", Collections.EMPTY_LIST);
        final User user = collectionsUtil.getLoggedInUser();
        final List<Boundary> boundaryList = new ArrayList<Boundary>();
        final Employee employee = employeeService.getEmployeeById(user.getId());
        if (employee != null)
            for (final Jurisdiction element : employee.getJurisdictions())
                boundaryList.add(element.getBoundary());
        addDropdownData("boundaryList", boundaryList);
        return INDEX;
    }

    @Override
    @Action(value = "/reports/remittanceStatementReport-report")
    public String report() {
        final User user = collectionsUtil.getLoggedInUser();

        critParams.put(SELECTED_DEPT_ID, getDeptId());

        final Integer bounaryId = getDeptId();

        final StringBuilder jurValuesId = new StringBuilder();

        jurValuesId.append(bounaryId);
        new ArrayList<Boundary>();
        final Employee employee = employeeService.getEmployeeById(user.getId());

        if (employee != null)
            for (final Jurisdiction element : employee.getJurisdictions()) {
                if (jurValuesId.length() > 0)
                    jurValuesId.append(',');
                jurValuesId.append(element.getBoundary().getId());

                for (final Boundary boundary : element.getBoundary().getChildren()) {
                    jurValuesId.append(',');
                    jurValuesId.append(boundary.getId());
                }
            }
        if (null == jurValuesId.toString() || StringUtils.isEmpty(jurValuesId.toString())
                || "-1".equals(jurValuesId.toString()))
            critParams.put(EGOV_DEPT_ID, null);
        else
            critParams.put(EGOV_DEPT_ID, jurValuesId.toString());

        final ReportRequest reportInput = new ReportRequest(getReportTemplateName(), critParams,
                ReportDataSourceType.SQL);
        final ReportOutput reportOutput = reportService.createReport(reportInput);
        reportId = reportViewerUtil.addReportToTempCache(reportOutput);
        return REPORT;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/reports/remittanceStatementReport-printBankChallan")
    public String printBankChallan() {
        critParams.put(EGOV_CASH_AMOUNT, totalCashAmount);
        critParams.put(EGOV_CHEQUE_AMOUNT, totalChequeAmount);
        critParams.put(EGOV_BANK, bank);
        critParams.put(EGOV_BANK_ACCOUNT, bankAccount);
        critParams.put(EGOV_REMITTANCE_DATE, remittanceDate == null ? new Date() : remittanceDate);
        final CollectionRemittanceReportResult collReportResult = new CollectionRemittanceReportResult();
        bankRemittanceList = (List<CollectionBankRemittanceReport>) getSession().get("REMITTANCE_LIST");
        critParams.put(EGOV_REMITTANCE_VOUCHER,
                bankRemittanceList.isEmpty() ? "" : bankRemittanceList.get(0).getVoucherNumber());
        collReportResult.setCollectionBankRemittanceReportList(bankRemittanceList);
        final ReportRequest reportInput = new ReportRequest(PRINT_BANK_CHALLAN_TEMPLATE, collReportResult, critParams);
        final ReportOutput reportOutput = reportService.createReport(reportInput);
        reportId = reportViewerUtil.addReportToTempCache(reportOutput);
        return REPORT;
    }

    @Action(value = "/reports/remittanceStatementReport-reportPrintBankChallan")
    public String reportPrintBankChallan() {
        final Remittance remittanceObj = (Remittance) persistenceService
                .findByNamedQuery(CollectionConstants.REMITTANCE_BY_VOUCHER_NUMBER, voucherNumber);
        final List<ReceiptHeader> remittanceList = new ArrayList<ReceiptHeader>(
                remittanceObj.getCollectionRemittance());
        double totalCashAmt = 0;
        double totalChequeAmt = 0;
        bankRemittanceList = remittanceService.prepareBankRemittanceReport(remittanceList);
        for (final RemittanceDetail rd : remittanceObj.getRemittanceDetails()) {
            if (rd.getChartOfAccount()
                    .getPurposeId() == getAccountCodePurpose(CollectionConstants.PURPOSE_NAME_CASH_IN_HAND).getId())
                totalCashAmt = rd.getCreditAmount().doubleValue();

            if (rd.getChartOfAccount().getPurposeId()
                    .equals(getAccountCodePurpose(CollectionConstants.PURPOSE_NAME_CHEQUE_IN_HAND).getId()))
                totalChequeAmt = rd.getCreditAmount().doubleValue();
        }
        critParams.put(EGOV_CASH_AMOUNT, totalCashAmt);
        critParams.put(EGOV_CHEQUE_AMOUNT, totalChequeAmt);
        critParams.put(EGOV_REMITTANCE_VOUCHER, remittanceObj == null ? "" : remittanceObj.getReferenceNumber());
        critParams.put(EGOV_REMITTANCE_DATE, remittanceObj == null ? new Date() : remittanceObj.getReferenceDate());
        critParams.put(EGOV_BANK, remittanceObj.getBankAccount() != null
                ? remittanceObj.getBankAccount().getBankbranch().getBank().getName() : "");
        critParams.put(EGOV_BANK_ACCOUNT,
                remittanceObj.getBankAccount() != null ? remittanceObj.getBankAccount().getAccountnumber() : "");
        final CollectionRemittanceReportResult collReportResult = new CollectionRemittanceReportResult();
        collReportResult.setCollectionBankRemittanceReportList(bankRemittanceList);
        final ReportRequest reportInput = new ReportRequest(PRINT_BANK_CHALLAN_TEMPLATE, collReportResult, critParams);
        final ReportOutput reportOutput = reportService.createReport(reportInput);
        reportId = reportViewerUtil.addReportToTempCache(reportOutput);
        return REPORT;
    }

    public AccountCodePurpose getAccountCodePurpose(final String purposeName) {
        final AccountCodePurpose acctCodePurposeObj = (AccountCodePurpose) getPersistenceService()
                .find(" from AccountCodePurpose" + " where name = ? ", purposeName);
        return acctCodePurposeObj;
    }

    @Override
    protected String getReportTemplateName() {
        return CollectionConstants.REPORT_TEMPLATE_REMITTANCE_STATEMENT;
    }

    public Date getFromDate() {
        return (Date) getReportParam(EGOV_FROM_DATE);
    }

    public void setFromDate(final Date fromDate) {
        critParams.put(EGOV_FROM_DATE, fromDate);
    }

    public Date getToDate() {
        return (Date) getReportParam(EGOV_TO_DATE);
    }

    public void setToDate(final Date toDate) {
        critParams.put(EGOV_TO_DATE, toDate);
    }

    public Long getServiceId() {
        return (Long) getReportParam(EGOV_SERVICE_ID);
    }

    public void setServiceId(final Long serviceId) {
        critParams.put(EGOV_SERVICE_ID, serviceId);
    }

    public Integer getFundId() {
        return (Integer) getReportParam(EGOV_FUND_ID);
    }

    public void setFundId(final Integer fundId) {
        critParams.put(EGOV_FUND_ID, fundId);
    }

    public Integer getBranchId() {
        return (Integer) getReportParam(EGOV_BANKBRANCH_ID);
    }

    public void setBranchId(final Integer branchId) {
        critParams.put(EGOV_BANKBRANCH_ID, branchId);
    }

    public Integer getBankaccountId() {
        return (Integer) getReportParam(EGOV_BANKACCOUNT_ID);
    }

    public void setBankaccountId(final Integer bankAccountId) {
        critParams.put(EGOV_BANKACCOUNT_ID, bankAccountId);
    }

    public String getPaymentMode() {
        final String modeOfPayment = (String) getReportParam(EGOV_PAYMENT_MODE);
        return null == modeOfPayment ? "-1" : modeOfPayment;
    }

    /**
     * @param paymentMode
     *            the payment mode to set (cash/cheque)
     */
    public void setPaymentMode(final String paymentMode) {
        if (null != paymentMode && !"-1".equals(paymentMode))
            critParams.put(EGOV_PAYMENT_MODE, paymentMode);
        else
            critParams.put(EGOV_PAYMENT_MODE, null);

    }

    public Integer getDeptId() {
        return (Integer) getReportParam(EGOV_DEPT_ID);
    }

    public void setDeptId(final Integer deptId) {
        critParams.put(EGOV_DEPT_ID, deptId);
    }

    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

    /**
     * @return the payment modes
     */
    public Map<String, String> getPaymentModes() {
        return paymentModes;
    }

    private Map<String, String> createPaymentModeList() {
        final Map<String, String> paymentModesMap = new HashMap<String, String>();
        paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_CASH, CollectionConstants.INSTRUMENTTYPE_CASH);
        paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_CHEQUEORDD,
                CollectionConstants.INSTRUMENTTYPE_CHEQUEORDD);
        // paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_BANK,
        // CollectionConstants.INSTRUMENTTYPE_BANK);
        paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_ONLINE, CollectionConstants.INSTRUMENTTYPE_ONLINE);
        paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_CARD, CollectionConstants.INSTRUMENTTYPE_CARD);
        // paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_ATM,
        // CollectionConstants.INSTRUMENTTYPE_ATM);
        return paymentModesMap;
    }

    @Override
    public String getReportId() {
        return reportId;
    }

    public Double getTotalCashAmount() {
        return totalCashAmount;
    }

    public void setTotalCashAmount(final Double totalCashAmount) {
        this.totalCashAmount = totalCashAmount;
    }

    public Double getTotalChequeAmount() {
        return totalChequeAmount;
    }

    public void setTotalChequeAmount(final Double totalChequeAmount) {
        this.totalChequeAmount = totalChequeAmount;
    }

    public List<CollectionBankRemittanceReport> getBankRemittanceList() {
        return bankRemittanceList;
    }

    public void setBankRemittanceList(final List<CollectionBankRemittanceReport> bankRemittanceList) {
        this.bankRemittanceList = bankRemittanceList;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(final String bank) {
        this.bank = bank;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(final String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Date getRemittanceDate() {
        return remittanceDate;
    }

    public void setRemittanceDate(final Date remittanceDate) {
        this.remittanceDate = remittanceDate;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(final String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public void setRemittanceService(final RemittanceServiceImpl remittanceService) {
        this.remittanceService = remittanceService;
    }

}
