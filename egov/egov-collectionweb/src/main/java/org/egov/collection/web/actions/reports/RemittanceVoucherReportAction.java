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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.CollectionBankRemittanceReport;
import org.egov.collection.entity.CollectionRemittanceReportResult;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.entity.Remittance;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.collection.web.actions.receipts.AjaxBankRemittanceAction;
import org.egov.commons.dao.BankBranchHibernateDAO;
import org.egov.commons.dao.BankaccountHibernateDAO;
import org.egov.eis.service.EmployeeService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportDataSourceType;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.web.struts.actions.ReportFormAction;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;
import org.springframework.beans.factory.annotation.Autowired;

@Results({ @Result(name = RemittanceVoucherReportAction.INDEX, location = "remittanceVoucherReport-index.jsp"),
        @Result(name = RemittanceVoucherReportAction.REPORT, location = "remittanceVoucherReport-report.jsp") })
@ParentPackage("egov")
public class RemittanceVoucherReportAction extends ReportFormAction {

    private static final long serialVersionUID = 1L;

    private static final String EGOV_FROM_DATE = "EGOV_FROM_DATE";
    private static final String EGOV_TO_DATE = "EGOV_TO_DATE";

    static final String EGOV_BANKACCOUNT_ID = "EGOV_BANKACCOUNT_ID";
    private static final String EGOV_BRANCH_ID = "EGOV_BRANCH_ID";
    private static final String EGOV_CREATEDBY_ID = "EGOV_CREATEDBY_ID";
    private static final String EGOV_SERVICE_ID = "EGOV_SERVICE_ID";
    private static final String EGOV_VOUCHER_NUMBER = "EGOV_VOUCHER_NUMBER";
    private static final String EGOV_BANK_NAME = "EGOV_BANK_NAME";
    private static final String EGOV_BANKACCOUNT_NAME = "EGOV_BANKACCOUNT_NAME";
    private static final String EGOV_CREATED_BY_NAME = "EGOV_CREATED_BY_NAME";
    private static final String EGOV_SERVICE_NAME = "EGOV_SERVICE_NAME";

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CollectionsUtil collectionsUtil;
    @Autowired
    private ReportService reportService;
    private Integer branchId;
    private static final String ACCOUNT_NUMBER_LIST = "accountNumberList";
    private static final String RECEIPTDETAILSLIST = "receiptdetailslist";
    private static final String RECIEPT_DETAILS_TEMPLATE = "collection_receiptdetails_main_report";
    private String reportId;
    @Autowired
    private ReportViewerUtil reportViewerUtil;
    private String voucherNumber;
    private final Map<String, Object> critParams = new HashMap<String, Object>(0);
    private List<CollectionBankRemittanceReport> bankRemittanceList;
    private Long srvId;
    private Long bankAcctId;
    private Long createdId;
    private Integer bankBranchId;
    @Autowired
    private BankaccountHibernateDAO bankAccountHibernateDAO;
    @Autowired
    private BankBranchHibernateDAO bankbranchDAO;
    @Autowired
    private UserService userservice;
    private PersistenceService<ServiceDetails, Long> serviceDetailsService;

    /*
     * (non-Javadoc)
     * @see org.egov.web.actions.BaseFormAction#prepare()
     */
    public void prepare() {
        setReportFormat(ReportFormat.PDF);
        setDataSourceType(ReportDataSourceType.SQL);
    }

    public void populateBankAccountList() {
        final AjaxBankRemittanceAction ajaxBankRemittanceAction = new AjaxBankRemittanceAction();
        ajaxBankRemittanceAction.setPersistenceService(getPersistenceService());
        ajaxBankRemittanceAction.bankBranchListOfService();
        addDropdownData("bankBranchList", ajaxBankRemittanceAction.getBankBranchArrayList());
        if (branchId != null) {
            ajaxBankRemittanceAction.setBranchId(branchId);
            ajaxBankRemittanceAction.accountListOfService();
            addDropdownData(ACCOUNT_NUMBER_LIST, ajaxBankRemittanceAction.getBankAccountArrayList());
        } else
            addDropdownData(ACCOUNT_NUMBER_LIST, Collections.EMPTY_LIST);
    }

    /**
     * Action method for criteria screen
     * 
     * @return index
     */
    @Action(value = "/reports/remittanceVoucherReport-criteria")
    public String criteria() {

        List<User> usersList = persistenceService
                .findAllByNamedQuery(CollectionConstants.QUERY_REMITTANCEVOUCHER_CREATOR_LIST);
        final User user = collectionsUtil.getLoggedInUser();
        employeeService.getEmployeeById(user.getId());
        populateBankAccountList();
        addDropdownData("collectionServiceList", Collections.EMPTY_LIST);
        addDropdownData("remittanceVoucherCreatorList", usersList);
        // Set default values of criteria fields
        setReportParam(EGOV_FROM_DATE, new Date());
        setReportParam(EGOV_TO_DATE, new Date());
        critParams.put(EGOV_FROM_DATE, new Date());
        critParams.put(EGOV_TO_DATE, new Date());
        return INDEX;
    }

    @Action(value = "/reports/remittanceVoucherReport-reportReceiptDetails")
    public String reportReceiptDetails() {

        final Remittance remittanceObj = (Remittance) persistenceService
                .findByNamedQuery(CollectionConstants.REMITTANCE_BY_VOUCHER_NUMBER, voucherNumber);
        critParams.put(EGOV_FROM_DATE, new Date());
        critParams.put(EGOV_TO_DATE, new Date());
        final List<CollectionBankRemittanceReport> reportList = new ArrayList<CollectionBankRemittanceReport>(0);
        if (remittanceObj != null)
        {
            for (final ReceiptHeader receiptHead : remittanceObj.getCollectionRemittance()) {
                final Iterator<InstrumentHeader> itr = receiptHead.getReceiptInstrument().iterator();
                while (itr.hasNext()) {
                    final CollectionBankRemittanceReport collBankRemitReport = new CollectionBankRemittanceReport();
                    final InstrumentHeader instHead = (InstrumentHeader) itr.next();
                    collBankRemitReport.setPaymentMode(instHead.getInstrumentType().getType());
                    collBankRemitReport.setAmount(instHead.getInstrumentAmount().doubleValue());
                    collBankRemitReport.setReceiptNumber(receiptHead.getReceiptnumber());
                    collBankRemitReport.setReceiptDate(receiptHead.getReceiptDate());
                    collBankRemitReport.setServiceType(receiptHead.getService().getName());
                    reportList.add(collBankRemitReport);
                }
            }
        }
        ServiceDetails service = serviceDetailsService.findById(srvId, false);
        setEgovVoucherNumber(voucherNumber);
        critParams.put(EGOV_SERVICE_NAME, service != null ? service.getName() : null);
        critParams.put(EGOV_BANK_NAME, bankBranchId != -1 ? bankbranchDAO.findById(bankBranchId, false).getBank().getName()
                : null);
        critParams.put(EGOV_BANKACCOUNT_NAME, bankAcctId != -1 ? bankAccountHibernateDAO.findById(bankAcctId, false)
                .getBankbranch().getBank().getName()
                + "-" + bankAccountHibernateDAO.findById(bankAcctId, false).getAccountnumber() : null);
        critParams.put(EGOV_CREATED_BY_NAME, createdId != -1 ? userservice.getUserById(createdId).getName() : null);
        final CollectionRemittanceReportResult collReportResult = new CollectionRemittanceReportResult();
        collReportResult.setCollectionBankRemittanceReportList(reportList);
        final ReportRequest reportInput = new ReportRequest(RECIEPT_DETAILS_TEMPLATE, collReportResult, critParams);
        final ReportOutput reportOutput = reportService.createReport(reportInput);
        reportId = reportViewerUtil.addReportToTempCache(reportOutput);
        return REPORT;
    }

    /**
     * @return the from date
     */
    public Date getFromDate() {
        return (Date) getReportParam(EGOV_FROM_DATE);
    }

    @Action(value = "/reports/remittanceVoucherReport-report")
    public String report() {
        final ReportRequest reportInput = new ReportRequest(getReportTemplateName(), critParams,
                ReportDataSourceType.SQL);
        final ReportOutput reportOutput = reportService.createReport(reportInput);
        reportId = reportViewerUtil.addReportToTempCache(reportOutput);
        return REPORT;
    }

    /**
     * @param fromDate the from date to set
     */
    public void setFromDate(final Date fromDate) {
        critParams.put(EGOV_FROM_DATE, fromDate);
    }

    /**
     * @return the do date
     */
    public Date getToDate() {
        return (Date) getReportParam(EGOV_TO_DATE);
    }

    public static String getReceiptdetailslist() {
        return RECEIPTDETAILSLIST;
    }

    /**
     * @param toDate the to date to set
     */
    public void setToDate(Date toDate) {
        critParams.put(EGOV_TO_DATE, toDate);
    }

    @Override
    protected String getReportTemplateName() {
        return CollectionConstants.REPORT_TEMPLATE_REMITTANCE_VOUCHER;
    }

    public Integer getBankAccountId() {
        return (Integer) getReportParam(EGOV_BANKACCOUNT_ID);
    }

    public void setBankAccountId(Integer bankAccountId) {
        critParams.put(EGOV_BANKACCOUNT_ID, bankAccountId);
    }

    public Integer getCreatedById() {
        return (Integer) getReportParam(EGOV_CREATEDBY_ID);
    }

    public void setCreatedById(Integer createdById) {
        critParams.put(EGOV_CREATEDBY_ID, createdById);
    }

    public Long getServiceId() {
        return (Long) getReportParam(EGOV_SERVICE_ID);
    }

    public void setServiceId(Long serviceId) {
        critParams.put(EGOV_SERVICE_ID, serviceId);
    }

    public String getEgovVoucherNumber() {
        return (String) getReportParam(EGOV_VOUCHER_NUMBER);
    }

    public void setEgovVoucherNumber(String voucherNumber) {
        critParams.put(EGOV_VOUCHER_NUMBER, voucherNumber);
    }

    public Integer getBranchId() {
        return (Integer) getReportParam(EGOV_BRANCH_ID);
    }

    public void setBranchId(Integer branchId) {
        critParams.put(EGOV_BRANCH_ID, branchId);
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    @Override
    public String getReportId() {
        return reportId;
    }

    public List<CollectionBankRemittanceReport> getBankRemittanceList() {
        return bankRemittanceList;
    }

    public void setBankRemittanceList(final List<CollectionBankRemittanceReport> bankRemittanceList) {
        this.bankRemittanceList = bankRemittanceList;
    }

    public Long getSrvId() {
        return srvId;
    }

    public void setSrvId(Long srvId) {
        this.srvId = srvId;
    }

    public Long getBankAcctId() {
        return bankAcctId;
    }

    public void setBankAcctId(Long bankAcctId) {
        this.bankAcctId = bankAcctId;
    }

    public Long getCreatedId() {
        return createdId;
    }

    public void setCreatedId(Long createdId) {
        this.createdId = createdId;
    }

    public Integer getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(Integer bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public String getEgovBankName() {
        return (String) getReportParam(EGOV_BANK_NAME);
    }

    public void setEgovBankName(String branchName) {
        critParams.put(EGOV_BANK_NAME, branchName);
    }

    public String getEgovBankaccountName() {
        return (String) getReportParam(EGOV_BANKACCOUNT_NAME);
    }

    public void setEgovBankaccountName(String branchAccountName) {
        critParams.put(EGOV_BANKACCOUNT_NAME, branchAccountName);
    }

    public String getEgovCreatedByName() {
        return (String) getReportParam(EGOV_CREATED_BY_NAME);
    }

    public void setEgovCreatedByName(String createdByName) {
        critParams.put(EGOV_CREATED_BY_NAME, createdByName);
    }

    public String getEgovServiceName() {
        return (String) getReportParam(EGOV_SERVICE_NAME);
    }

    public void setEgovServiceName(String serviceName) {
        critParams.put(EGOV_SERVICE_NAME, serviceName);
    }

    public void setServiceDetailsService(final PersistenceService<ServiceDetails, Long> serviceDetailsService) {
        this.serviceDetailsService = serviceDetailsService;
    }
}
