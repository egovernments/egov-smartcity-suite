/**
 * 
 */
package org.egov.erpcollection.web.actions.reports;

import org.egov.erpcollection.util.CollectionsUtil;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportRequest.ReportDataSourceType;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.web.actions.ReportFormAction;

/**
 * Action class for the bank remittance report
 */
public class BankRemittanceReportAction extends ReportFormAction {

	private static final long serialVersionUID = 1L;
	
	private static final String EGOV_DEPT_ID = "EGOV_DEPT_ID";
	private static final String BANK_REMITTANCE_REPORT_TEMPLATE = "bank_remittance";
	
	private CollectionsUtil collectionsUtil;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.egov.web.actions.BaseFormAction#prepare()
	 */
	public void prepare() {
		setReportFormat(FileFormat.PDF);
		setDataSourceType(ReportDataSourceType.HQL);
	}
	
	/**
	 * @param collectionsUtil the collections utility object to set
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
	
	
	/* (non-Javadoc)
	 * @see org.egov.erpcollection.web.actions.reports.ReportFormAction#criteria()
	 */
	@Override
	public String criteria() {
		// Setup drop down data for department list
		addRelatedEntity("department", DepartmentImpl.class, "deptName");
		setupDropdownDataExcluding();
		
		// Set default values of criteria fields
		Department dept = collectionsUtil.getDepartmentOfLoggedInUser(getSession());
		if(dept != null) {
			setReportParam(EGOV_DEPT_ID, dept.getId());
		}
		
		return INDEX;
	}

	/* (non-Javadoc)
	 * @see org.egov.erpcollection.web.actions.reports.ReportFormAction#getReportTemplateName()
	 */
	@Override
	protected String getReportTemplateName() {
		return BANK_REMITTANCE_REPORT_TEMPLATE;
	}
}
