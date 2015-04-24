/**
 * 
 */
package org.egov.erpcollection.web.actions.reports;

import java.util.Date;
import java.util.List;

import org.egov.commons.EgwStatus;
import org.egov.erpcollection.models.OnlinePayment;
import org.egov.erpcollection.util.CollectionsUtil;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportRequest.ReportDataSourceType;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.web.actions.ReportFormAction;

/**
 * Action class for Online Trasaction Report
 */
public class OnlineTransactionReportAction extends ReportFormAction {
	private static final long serialVersionUID = 1L;
	// Report parameter names
	private static final String EGOV_DEPT_ID = "EGOV_DEPT_ID";
	private static final String EGOV_FROM_DATE = "EGOV_FROM_DATE";
	private static final String EGOV_TO_DATE = "EGOV_TO_DATE";
	private static final String EGOV_BILLING_SERVICE_ID = "EGOV_BILLING_SERVICE_ID";
	private static final String EGOV_ONLINETRANSACTION_STATUS_ID = "EGOV_ONLINETRANSACTION_STATUS_ID";

	private CollectionsUtil collectionsUtil;

	@Override
	public void prepare() {
		super.prepare();

		setReportFormat(FileFormat.PDF);
		setDataSourceType(ReportDataSourceType.HQL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.egov.erpcollection.web.actions.reports.ReportFormAction#criteria()
	 */
	@Override
	public String criteria() {
		// Setup drop down data for department list
		addRelatedEntity("department", DepartmentImpl.class, "deptName");
		setupDropdownDataExcluding();
		
		// Add dropdown data for billing services (serviceList)
		addDropdownData(CollectionConstants.DROPDOWN_DATA_SERVICE_LIST,
				collectionsUtil.getBillingServiceList());
		addDropdownData(CollectionConstants.DROPDOWN_DATA_ONLINETRANSACTIONSTATUS_LIST,this.getOnlineReceiptStatuses());

		// default value for from/to date = today
		setReportParam(EGOV_FROM_DATE, new Date());
		setReportParam(EGOV_TO_DATE, new Date());

		return INDEX;
	}
	
	private List<EgwStatus> getOnlineReceiptStatuses () {
		return persistenceService.findAllByNamedQuery(
				CollectionConstants.QUERY_ALL_STATUSES_FOR_MODULE, 
				OnlinePayment.class.getSimpleName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.egov.erpcollection.web.actions.reports.ReportFormAction#
	 * getReportTemplateName()
	 */
	@Override
	protected String getReportTemplateName() {
		return CollectionConstants.REPORT_TEMPLATE_ONLINE_TRANSACTION;
	}

	/**
	 * @param collectionsUtil
	 *            The collections util object
	 */
	public void setCollectionsUtil(CollectionsUtil collectionsUtil) {
		this.collectionsUtil = collectionsUtil;
	}

	/**
	 * @return the department id
	 */
	public Integer getDepartmentId() {
		return (Integer) getReportParam(EGOV_DEPT_ID);
	}

	/**
	 * @param deptId
	 *            the department id to set
	 */
	public void setDepartmentId(Integer deptId) {
		setReportParam(EGOV_DEPT_ID, deptId);
	}

	/**
	 * @return the from date
	 */
	public Date getFromDate() {
		return (Date) getReportParam(EGOV_FROM_DATE);
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
	
	/**
	 * @return The billing service id
	 */
	public Long getBillingServiceId() {
		return (Long) getReportParam(EGOV_BILLING_SERVICE_ID);
	}

	/**
	 * @param billingServiceId
	 *            The Billing service id to set
	 */
	public void setBillingServiceId(Long challanServiceId) {
		setReportParam(EGOV_BILLING_SERVICE_ID, challanServiceId);
	}
	
	public Long getStatusId(){
		return (Long) getReportParam(EGOV_ONLINETRANSACTION_STATUS_ID);
	}
	
	public void setStatusId(Long statusId){
		setReportParam(EGOV_ONLINETRANSACTION_STATUS_ID, statusId);
	}
	

}
