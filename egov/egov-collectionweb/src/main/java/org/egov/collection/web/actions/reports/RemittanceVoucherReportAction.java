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

/**
 * 
 */
package org.egov.collection.web.actions.reports;

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
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.reporting.engine.ReportDataSourceType;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.web.struts.actions.ReportFormAction;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Results({ @Result(name = RemittanceVoucherReportAction.INDEX, location = "remittanceVoucherReport-index.jsp"),
        @Result(name = RemittanceVoucherReportAction.REPORT, location = "remittanceVoucherReport-report.jsp") })
@ParentPackage("egov")
public class RemittanceVoucherReportAction extends ReportFormAction {

    private static final long serialVersionUID = 1L;

    private static final String EGOV_VOUCHER_NUMBER = "EGOV_VOUCHER_NUMBER";
    private static final String EGOV_RECEIPT_NUMBER = "EGOV_RECEIPT_NUMBER";
    private static final String EGOV_REMITTANCE_NUMBER = "EGOV_REMITTANCE_NUMBER";
    private static final String EGOV_REMITTANCE_DATE = "EGOV_REMITTANCE_DATE";
    private static final String EGOV_BANKACCOUNT_ID = "EGOV_BANKACCOUNT_ID";
    private static final String EGOV_BRANCH_ID = "EGOV_BRANCH_ID";

    @Autowired
    private ReportService reportService;
    private Integer branchId;
    private String voucherNumber;
    private Long bankAcctId;
    private static final String ACCOUNT_NUMBER_LIST = "accountNumberList";
    private static final String RECEIPTDETAILSLIST = "receiptdetailslist";
    private static final String RECIEPT_DETAILS_TEMPLATE = "collection_receiptdetails_main_report";
    private String reportId;
    @Autowired
    private ReportViewerUtil reportViewerUtil;
    private final Map<String, Object> critParams = new HashMap<String, Object>(0);
    private List<CollectionBankRemittanceReport> bankRemittanceList;
    private Long srvId;
    private Integer bankBranchId;
    @Autowired
    private BankaccountHibernateDAO bankAccountHibernateDAO;
    @Autowired
    private BankBranchHibernateDAO bankbranchDAO;
    private PersistenceService<ServiceDetails, Long> serviceDetailsService;
    @Autowired
    private CollectionsUtil collectionsUtil;
    @Autowired
    private CityService cityService;

    /*
     * (non-Javadoc)
     * @see org.egov.web.actions.BaseFormAction#prepare()
     */
    @Override
    public void prepare() {
        setReportFormat(ReportFormat.PDF);
        setDataSourceType(ReportDataSourceType.SQL);
    }

    public void populateBankAccountList() {
        final AjaxBankRemittanceAction ajaxBankRemittanceAction = new AjaxBankRemittanceAction();
        ajaxBankRemittanceAction.setPersistenceService(getPersistenceService());
        ajaxBankRemittanceAction.setCollectionsUtil(collectionsUtil);
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
        populateBankAccountList();
        // Set default values of criteria fields
        setReportParam(EGOV_REMITTANCE_DATE, new Date());
        critParams.put(EGOV_REMITTANCE_DATE, new Date());
        return INDEX;
    }

    @Action(value = "/reports/remittanceVoucherReport-reportReceiptDetails")
    public String reportReceiptDetails() {

        final Remittance remittanceObj = (Remittance) persistenceService
                .findByNamedQuery(CollectionConstants.REMITTANCE_BY_VOUCHER_NUMBER, voucherNumber);
        critParams.put(EGOV_REMITTANCE_DATE, new Date());
        final List<CollectionBankRemittanceReport> reportList = new ArrayList<CollectionBankRemittanceReport>(0);
        if (remittanceObj != null) {
            for (final ReceiptHeader receiptHead : remittanceObj.getCollectionRemittance()) {
                final Iterator<InstrumentHeader> itr = receiptHead.getReceiptInstrument().iterator();
                while (itr.hasNext()) {
                    final CollectionBankRemittanceReport collBankRemitReport = new CollectionBankRemittanceReport();
                    final InstrumentHeader instHead = itr.next();
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
        // setEgovVoucherNumber(voucherNumber);
        /*
         * critParams.put(EGOV_SERVICE_NAME, service != null ? service.getName() : null); critParams.put(EGOV_BANK_NAME,
         * bankBranchId != -1 ? bankbranchDAO.findById(bankBranchId, false).getBank().getName() : null);
         * critParams.put(EGOV_BANKACCOUNT_NAME, bankAcctId != -1 ? bankAccountHibernateDAO.findById(bankAcctId, false)
         * .getBankbranch().getBank().getName() + "-" + bankAccountHibernateDAO.findById(bankAcctId, false).getAccountnumber() :
         * null); critParams.put(EGOV_CREATED_BY_NAME, createdId != -1 ? userservice.getUserById(createdId).getName() : null);
         */
        critParams.put(CollectionConstants.LOGO_PATH, cityService.getCityLogoURL());
        final CollectionRemittanceReportResult collReportResult = new CollectionRemittanceReportResult();
        collReportResult.setCollectionBankRemittanceReportList(reportList);
        final ReportRequest reportInput = new ReportRequest(RECIEPT_DETAILS_TEMPLATE, collReportResult, critParams);
        final ReportOutput reportOutput = reportService.createReport(reportInput);
        reportId = reportViewerUtil.addReportToTempCache(reportOutput);
        return REPORT;
    }

    @Action(value = "/reports/remittanceVoucherReport-report")
    public String report() {
        critParams.put(CollectionConstants.LOGO_PATH, cityService.getCityLogoURL());
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
        critParams.put(EGOV_REMITTANCE_DATE, fromDate);
    }

    public static String getReceiptdetailslist() {
        return RECEIPTDETAILSLIST;
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

    public Integer getBranchId() {
        return (Integer) getReportParam(EGOV_BRANCH_ID);
    }

    public void setBranchId(Integer branchId) {
        critParams.put(EGOV_BRANCH_ID, branchId);
    }

    /**
     * @return the remittance date
     */
    public Date getRemittanceDate() {
        return (Date) getReportParam(EGOV_REMITTANCE_DATE);
    }

    /**
     * @return the remittance date
     */
    public void setRemittanceDate(final Date remittanceDate) {
        critParams.put(EGOV_REMITTANCE_DATE, remittanceDate);
    }

    public String getReceiptNumber() {
        return (String) getReportParam(EGOV_RECEIPT_NUMBER);
    }

    public void setReceiptNumber(String receiptNumber) {
        critParams.put(EGOV_RECEIPT_NUMBER, receiptNumber);
    }

    public String getRemittanceNumber() {
        return (String) getReportParam(EGOV_REMITTANCE_NUMBER);
    }

    public void setRemittanceNumber(String remittanceNumber) {
        critParams.put(EGOV_REMITTANCE_NUMBER, remittanceNumber);
    }

    public String getVoucherNumber() {
        return (String) getReportParam(EGOV_VOUCHER_NUMBER);
    }

    public void setVoucherNumber(String voucherNumber) {
        critParams.put(EGOV_VOUCHER_NUMBER, voucherNumber);
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

    public Long getBankAcctId() {
        return bankAcctId;
    }

    public void setBankAcctId(Long bankAcctId) {
        this.bankAcctId = bankAcctId;
    }

    public Integer getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(Integer bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public void setServiceDetailsService(final PersistenceService<ServiceDetails, Long> serviceDetailsService) {
        this.serviceDetailsService = serviceDetailsService;
    }
}
