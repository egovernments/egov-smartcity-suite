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

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.service.CollectionReportService;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.collection.web.actions.receipts.AjaxBankRemittanceAction;
import org.egov.eis.entity.Employee;
import org.egov.eis.service.EmployeeService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportRequest.ReportDataSourceType;
import org.egov.infra.web.struts.actions.ReportFormAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Results({ @Result(name = RemittanceVoucherReportAction.INDEX, location = "remittanceVoucherReport-index.jsp"),
    @Result(name = RemittanceVoucherReportAction.REPORT, location = "remittanceVoucherReport-report.jsp") })

@ParentPackage("egov")
public class RemittanceVoucherReportAction extends ReportFormAction {

    private static final long serialVersionUID = 1L;

    private static final String EGOV_VOUCHER_TYPE = "EGOV_VOUCHER_TYPE";
    private static final String EGOV_FROM_DATE = "EGOV_FROM_DATE";
    private static final String EGOV_TO_DATE = "EGOV_TO_DATE";
/*    private static final String EGOV_BOUNDARY_ID = "EGOV_BOUNDARY_ID";
*/    private static final String EGOV_BANKACCOUNT_ID = "EGOV_BANKACCOUNT_ID";
     private static final String EGOV_BRANCH_ID = "EGOV_BRANCH_ID";
    private static final String EGOV_CREATEDBY_ID = "EGOV_CREATEDBY_ID";
    private static final String EGOV_SERVICE_ID = "EGOV_SERVICE_ID";
 /*   private static final String EGOV_FUND_ID = "EGOV_FUND_ID";*/
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CollectionsUtil collectionsUtil;
    @Autowired
    private CollectionReportService reportService;
    private Integer branchId;
    private static final String ACCOUNT_NUMBER_LIST = "accountNumberList";



    /*
     * (non-Javadoc)
     * 
     * @see org.egov.web.actions.BaseFormAction#prepare()
     */
    public void prepare() {
        setReportFormat(FileFormat.PDF);
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
        final Employee employee = employeeService.getEmployeeById(user.getId());
        populateBankAccountList();
        addDropdownData("collectionServiceList", Collections.EMPTY_LIST);
        addDropdownData("remittanceVoucherCreatorList", usersList);
        // Set default values of criteria fields
        setReportParam(EGOV_FROM_DATE, new Date());
        setReportParam(EGOV_TO_DATE, new Date());
        return INDEX;
    }

    /**
     * @return the from date
     */
    public Date getFromDate() {
        return (Date) getReportParam(EGOV_FROM_DATE);
    }
    @Action(value = "/reports/remittanceVoucherReport-report")
    public String report() {
        return super.report();
    }
    /**
     * @param fromDate
     *            the from date to set
     */
    public void setFromDate(Date fromDate) {
        setReportParam(EGOV_FROM_DATE, fromDate);
    }

    /**
     * @return the do date
     */
    public Date getToDate() {
        return (Date) getReportParam(EGOV_TO_DATE);
    }

    /**
     * @param toDate
     *            the to date to set
     */
    public void setToDate(Date toDate) {
        setReportParam(EGOV_TO_DATE, toDate);
    }

    @Override
    protected String getReportTemplateName() {
        return CollectionConstants.REPORT_TEMPLATE_REMITTANCE_VOUCHER;
    }

    public Integer getBankAccountId() {
        return (Integer) getReportParam(EGOV_BANKACCOUNT_ID);
    }

    public void setBankAccountId(Integer bankAccountId) {
        setReportParam(EGOV_BANKACCOUNT_ID, bankAccountId);
    }


    public Integer getCreatedById() {
        return (Integer) getReportParam(EGOV_CREATEDBY_ID);
    }

    public void setCreatedById(Integer createdById) {
        setReportParam(EGOV_CREATEDBY_ID, createdById);
    }

    public Long getServiceId() {
        return (Long) getReportParam(EGOV_SERVICE_ID);
    }

    public void setServiceId(Long serviceId) {
        setReportParam(EGOV_SERVICE_ID, serviceId);
    }

    public Integer getBranchId() {
        return (Integer) getReportParam(EGOV_BRANCH_ID);
    }

    public void setBranchId(Integer branchId) {
        setReportParam(EGOV_BRANCH_ID, branchId);
    }



}
