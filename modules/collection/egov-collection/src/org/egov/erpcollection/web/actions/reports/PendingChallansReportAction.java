/**
 * 
 */
package org.egov.erpcollection.web.actions.reports;

import java.util.Date;

import org.egov.erpcollection.util.CollectionsUtil;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportRequest.ReportDataSourceType;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.web.actions.ReportFormAction;

/**
 * Action class for pending challans report action
 */
public class PendingChallansReportAction extends ReportFormAction {
	private static final long serialVersionUID = 1L;
	// Report parameter names
	private static final String EGOV_DEPT_ID = "EGOV_DEPT_ID";
	private static final String EGOV_FROM_DATE = "EGOV_FROM_DATE";
	private static final String EGOV_TO_DATE = "EGOV_TO_DATE";
	private static final String EGOV_CHALLAN_SERVICE_ID = "EGOV_CHALLAN_SERVICE_ID";

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

		// Add dropdown data for challan services (serviceList)
		addDropdownData(CollectionConstants.DROPDOWN_DATA_SERVICE_LIST,
				collectionsUtil.getChallanServiceList());

		// Set default values of criteria fields
		Department dept = collectionsUtil
				.getDepartmentOfLoggedInUser(getSession());
		if (dept != null) {
			setReportParam(EGOV_DEPT_ID, dept.getId());
		}
		
		// default value for from/to date = today
		setReportParam(EGOV_FROM_DATE, new Date());
		setReportParam(EGOV_TO_DATE, new Date());

		return INDEX;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.egov.erpcollection.web.actions.reports.ReportFormAction#
	 * getReportTemplateName()
	 */
	@Override
	protected String getReportTemplateName() {
		return CollectionConstants.REPORT_TEMPLATE_PENDING_CHALLANS;
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
	public Integer getDeptId() {
		return (Integer) getReportParam(EGOV_DEPT_ID);
	}

	/**
	 * @param deptId
	 *            the department id to set
	 */
	public void setDeptId(Integer deptId) {
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
	 * @return The challan service id
	 */
	public Long getChallanServiceId() {
		return (Long) getReportParam(EGOV_CHALLAN_SERVICE_ID);
	}

	/**
	 * @param challanServiceId
	 *            The challan service id to set
	 */
	public void setChallanServiceId(Long challanServiceId) {
		setReportParam(EGOV_CHALLAN_SERVICE_ID, challanServiceId);
	}
}
