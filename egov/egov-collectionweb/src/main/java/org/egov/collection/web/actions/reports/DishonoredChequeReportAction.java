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

package org.egov.collection.web.actions.reports;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.web.struts.actions.ReportFormAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

@Results({ @Result(name = BankRemittanceReportAction.INDEX, location = "dishonoredChequeReport-index.jsp"),
        @Result(name = BankRemittanceReportAction.REPORT, location = "dishonoredChequeReport-report.jsp") })
public class DishonoredChequeReportAction extends ReportFormAction {

    private static final long serialVersionUID = 1L;
    private static final String COLLECTION_DISHONORED_CHEQUE_REPORT = "collection_dishonored_cheque_report";
    private static final String EGOV_FROM_DATE = "EGOV_FROM_DATE";
    private static final String EGOV_TO_DATE = "EGOV_TO_DATE";
    private static final String EGOV_PAYMENT_MODE = "EGOV_PAYMENT_MODE";
    private static final String EGOV_SERVICE_ID = "EGOV_SERVICE_ID";
    private static final String EGOV_STATUS_ID = "EGOV_STATUS_ID";
    private static final String EGOV_LOCATION_ID = "EGOV_LOCATION_ID";
    private static final String EGOV_TRANSACTION_FROM_DATE = "EGOV_TRANSACTION_FROM_DATE";
    private static final String EGOV_TRANSACTION_TO_DATE = "EGOV_TRANSACTION_TO_DATE";
    private static final String EGOV_FUND_ID = "EGOV_FUND_ID";
    private static final String EGOV_USER_ID = "EGOV_USER_ID";
    private static final String EGOV_INSTRUMENT_NUMBER = "EGOV_INSTRUMENT_NUMBER";

    private CollectionsUtil collectionsUtil;
    private Map<String, String> paymentModes;
    @Autowired
    private CityService cityService;

    @Override
    public Object getModel() {

        return null;
    }

    @Override
    @Action(value = "/reports/dishonoredChequeReport-criteria")
    public String criteria() {

        setReportParam(EGOV_FROM_DATE, new Date());
        setReportParam(EGOV_TO_DATE, new Date());
        addDropdownData("servicetypeList",
                getPersistenceService().findAllByNamedQuery(CollectionConstants.QUERY_COLLECTION_SERVICS));
        addDropdownData(CollectionConstants.DROPDOWN_DATA_LOCATION_LIST, collectionsUtil.getAllLocations());
        addDropdownData("statusList",
                getPersistenceService().findAllByNamedQuery(CollectionConstants.QUERY_INSTRUMENT_DISHONOR_STATUSES));
        paymentModes = CollectionConstants.INSTRUMENT_MODES_MAP;
        return INDEX;
    }

    @Action(value = "/reports/dishonoredChequeReport-generateReport")
    public String generateReport() {
        setReportParam(CollectionConstants.LOGO_PATH, cityService.getCityLogoAsStream());
        return report();
    }

    @Override
    protected String getReportTemplateName() {

        return COLLECTION_DISHONORED_CHEQUE_REPORT;
    }

    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

    public static String getCollectionDishonoredChequeReport() {
        return COLLECTION_DISHONORED_CHEQUE_REPORT;
    }

    public Long getServiceId() {
        return (Long) getReportParam(EGOV_SERVICE_ID);
    }

    public void setServiceId(final Long serviceId) {
        setReportParam(EGOV_SERVICE_ID, serviceId);
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

    public Date getTransactionFromDate() {
        return (Date) getReportParam(EGOV_TRANSACTION_FROM_DATE);
    }

    public void setTransactionFromDate(final Date transactionFromDate) {
        setReportParam(EGOV_TRANSACTION_FROM_DATE, transactionFromDate);
    }

    public Date getTransactionToDate() {
        return (Date) getReportParam(EGOV_TRANSACTION_TO_DATE);
    }

    public void setTransactionToDate(final Date transactionToDate) {
        setReportParam(EGOV_TRANSACTION_TO_DATE, transactionToDate);
    }

    public String getPaymentMode() {
        return (String) getReportParam(EGOV_PAYMENT_MODE);
    }

    public void setPaymentMode(final String paymentMode) {
        setReportParam(EGOV_PAYMENT_MODE, paymentMode);
    }

    public Map<String, String> getPaymentModes() {
        return paymentModes;
    }

    public Integer getStatusId() {
        return (Integer) getReportParam(EGOV_STATUS_ID);
    }

    public void setStatusId(final Integer statusId) {
        setReportParam(EGOV_STATUS_ID, statusId);
    }

    public void setLocationId(final Long locationId) {
        setReportParam(EGOV_LOCATION_ID, locationId);
    }

    public Long getLocationId() {
        return (Long) getReportParam(EGOV_LOCATION_ID);
    }

    public void setFundId(final Long fundId) {
        setReportParam(EGOV_FUND_ID, fundId);
    }

    public Long getFundId() {
        return (Long) getReportParam(EGOV_FUND_ID);
    }

    public void setUserId(final Long userId) {
        setReportParam(EGOV_USER_ID, userId);
    }

    public Long getUserId() {
        return (Long) getReportParam(EGOV_USER_ID);
    }

    public void setInstrumentNumber(final String instrumentNumber) {
        setReportParam(EGOV_INSTRUMENT_NUMBER, instrumentNumber);
    }

    public String getInstrumentNumber() {
        return (String) getReportParam(EGOV_INSTRUMENT_NUMBER);
    }

}
