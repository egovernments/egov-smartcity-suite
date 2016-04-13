/**
 * 
 */
package org.egov.collection.web.actions.reports;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

@Results({ @Result(name = RemittanceVoucherReportAction.INDEX, location = "remittanceVoucherReport-index.jsp"),
    @Result(name = RemittanceVoucherReportAction.REPORT, location = "remittanceVoucherReport-report.jsp") })

@ParentPackage("egov")
public class RemittanceVoucherReportAction extends ReportFormAction {

    private static final long serialVersionUID = 1L;

    private static final String EGOV_VOUCHER_TYPE = "EGOV_VOUCHER_TYPE";
    private static final String EGOV_FROM_DATE = "EGOV_FROM_DATE";
    private static final String EGOV_TO_DATE = "EGOV_TO_DATE";
    private static final String EGOV_BOUNDARY_ID = "EGOV_BOUNDARY_ID";
    private static final String EGOV_BANKACCOUNT_ID = "EGOV_BANKACCOUNT_ID";
    private static final String EGOV_CREATEDBY_ID = "EGOV_CREATEDBY_ID";
    private static final String EGOV_SERVICE_ID = "EGOV_SERVICE_ID";
    private static final String EGOV_FUND_ID = "EGOV_FUND_ID";
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CollectionsUtil collectionsUtil;
    @Autowired
    private CollectionReportService reportService;


    private final Map<String, String> voucherTypes = createVoucherTypeList();

    private Map<String, String> createVoucherTypeList() {
        Map<String, String> paymentModesMap = new HashMap<String, String>();
        paymentModesMap.put(CollectionConstants.REMITTANCEVOUCHERREPORT_BRV,
                CollectionConstants.REMITTANCEVOUCHERREPORT_BRV);
        paymentModesMap.put(CollectionConstants.REMITTANCEVOUCHERREPORT_CSL,
                CollectionConstants.REMITTANCEVOUCHERREPORT_CSL);
        return paymentModesMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.egov.web.actions.BaseFormAction#prepare()
     */
    public void prepare() {
        setReportFormat(FileFormat.PDF);
        setDataSourceType(ReportDataSourceType.SQL);
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

        List<Boundary> boundaryList = new ArrayList<Boundary>();
        final User user = collectionsUtil.getLoggedInUser();
        final Employee employee = employeeService.getEmployeeById(user.getId());

        for (final Jurisdiction element : employee.getJurisdictions()) {
            boundaryList.add(element.getBoundary());
        }

        addDropdownData("collectionServiceList", persistenceService
                .findAllByNamedQuery(CollectionConstants.QUERY_COLLECTION_SERVICS));
        addDropdownData("collectionFundList", persistenceService
                .findAllByNamedQuery(CollectionConstants.QUERY_ALL_FUND));
        addDropdownData("remittanceBankAccountList", persistenceService
                .findAllByNamedQuery(CollectionConstants.QUERY_ALL_REMITTANCE_BANKACCOUNT_LIST));
        addDropdownData("remittanceVoucherCreatorList", usersList);
        addDropdownData("remittanceVoucherBoundaryList", boundaryList);
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

    public String getVoucherType() {
        return (String) getReportParam(EGOV_VOUCHER_TYPE);
    }

    public void setVoucherType(String voucherType) {
        setReportParam(EGOV_VOUCHER_TYPE, voucherType);
    }

    public Integer getBoundaryId() {
        return (Integer) getReportParam(EGOV_BOUNDARY_ID);
    }

    public void setBoundaryId(Integer boundaryId) {
        setReportParam(EGOV_BOUNDARY_ID, boundaryId);
    }

    public Integer getBankaccountId() {
        return (Integer) getReportParam(EGOV_BANKACCOUNT_ID);
    }

    public void setBankaccountId(Integer bankAccountId) {
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

    public Integer getFundId() {
        return (Integer) getReportParam(EGOV_FUND_ID);
    }

    public void setFundId(Integer fundId) {
        setReportParam(EGOV_FUND_ID, fundId);
    }

    public Map<String, String> getVoucherTypes() {
        return voucherTypes;
    }

}
