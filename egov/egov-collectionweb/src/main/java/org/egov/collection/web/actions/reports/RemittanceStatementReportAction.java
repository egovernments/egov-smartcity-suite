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

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.service.CollectionReportService;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.eis.entity.Employee;
import org.egov.eis.entity.Jurisdiction;
import org.egov.eis.service.EmployeeService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportRequest.ReportDataSourceType;
import org.egov.infra.web.struts.actions.ReportFormAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CollectionReportService reportService;

    private final Map<String, String> paymentModes = createPaymentModeList();

    @Override
    public void prepare() {
        setReportFormat(FileFormat.PDF);
        setDataSourceType(ReportDataSourceType.SQL);
    }

    @Override
    @Action(value = "/reports/remittanceStatementReport-criteria")
    public String criteria() {

        addDropdownData("collectionServiceList",
                persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_COLLECTION_SERVICS));
        addDropdownData("collectionFundList",
                persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_ALL_FUND));
        setReportParam(EGOV_FROM_DATE, new Date());
        setReportParam(EGOV_TO_DATE, new Date());
        addDropdownData("bankList", Collections.EMPTY_LIST);
        addDropdownData("bankAccountList", Collections.EMPTY_LIST);
        final User user = collectionsUtil.getLoggedInUser();
        final List<Boundary> boundaryList = new ArrayList<Boundary>();
        final Employee employee = employeeService.getEmployeeById(user.getId());
        for (final Jurisdiction element : employee.getJurisdictions())
            boundaryList.add(element.getBoundary());
        addDropdownData("boundaryList", boundaryList);
        return INDEX;
    }

    @Override
    @Action(value = "/reports/remittanceStatementReport-report")
    public String report() {
        final User user = collectionsUtil.getLoggedInUser();

        setReportParam(SELECTED_DEPT_ID, getDeptId());

        final Integer bounaryId = getDeptId();

        final StringBuilder jurValuesId = new StringBuilder();

        jurValuesId.append(bounaryId);
        new ArrayList<Boundary>();
        final Employee employee = employeeService.getEmployeeById(user.getId());

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
            setReportParam(EGOV_DEPT_ID, null);
        else
            setReportParam(EGOV_DEPT_ID, jurValuesId.toString());

        return super.report();
    }

    @Override
    protected String getReportTemplateName() {

        return CollectionConstants.REPORT_TEMPLATE_REMITTANCE_STATEMENT;
    }

    public Date getFromDate() {
        return (Date) getReportParam(EGOV_FROM_DATE);
    }

    public void setFromDate(final Date fromDate) {
        setReportParam(EGOV_FROM_DATE, fromDate);
    }

    public Date getToDate() {
        return (Date) getReportParam(EGOV_TO_DATE);
    }

    public void setToDate(final Date toDate) {
        setReportParam(EGOV_TO_DATE, toDate);
    }

    public Long getServiceId() {
        return (Long) getReportParam(EGOV_SERVICE_ID);
    }

    public void setServiceId(final Long serviceId) {
        setReportParam(EGOV_SERVICE_ID, serviceId);
    }

    public Integer getFundId() {
        return (Integer) getReportParam(EGOV_FUND_ID);
    }

    public void setFundId(final Integer fundId) {
        setReportParam(EGOV_FUND_ID, fundId);
    }

    public Integer getBranchId() {
        return (Integer) getReportParam(EGOV_BANKBRANCH_ID);
    }

    public void setBranchId(final Integer branchId) {
        setReportParam(EGOV_BANKBRANCH_ID, branchId);
    }

    public Integer getBankaccountId() {
        return (Integer) getReportParam(EGOV_BANKACCOUNT_ID);
    }

    public void setBankaccountId(final Integer bankAccountId) {
        setReportParam(EGOV_BANKACCOUNT_ID, bankAccountId);
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
            setReportParam(EGOV_PAYMENT_MODE, paymentMode);
        else
            setReportParam(EGOV_PAYMENT_MODE, null);

    }

    public Integer getDeptId() {
        return (Integer) getReportParam(EGOV_DEPT_ID);
    }

    public void setDeptId(final Integer deptId) {
        setReportParam(EGOV_DEPT_ID, deptId);
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
        //paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_BANK, CollectionConstants.INSTRUMENTTYPE_BANK);
        paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_ONLINE, CollectionConstants.INSTRUMENTTYPE_ONLINE);
        paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_CARD, CollectionConstants.INSTRUMENTTYPE_CARD);
        //paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_ATM, CollectionConstants.INSTRUMENTTYPE_ATM);
        return paymentModesMap;
    }

}
