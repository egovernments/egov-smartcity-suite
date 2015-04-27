/**
 * 
 */
package org.egov.collection.web.actions.reports;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportRequest.ReportDataSourceType;
import org.egov.web.actions.ReportFormAction;

/**
 * Action class for the cash collection summary report
 */
@ParentPackage("egov")	
public class CollectionSummaryAction extends ReportFormAction {

	private static final long serialVersionUID = 1L;

	private static final String EGOV_FROM_DATE = "EGOV_FROM_DATE";
	private static final String EGOV_TO_DATE = "EGOV_TO_DATE";
	private static final String EGOV_DEPT_ID = "EGOV_DEPT_ID";
	private static final String EGOV_PAYMENT_MODE = "EGOV_PAYMENT_MODE";
	private static final String COLLETION_SUMMARY_TEMPLATE = "collection_summary";

	private final Map<String, String> paymentModes = createPaymentModeList();
	private CollectionsUtil collectionsUtil;

	/**
	 * @return the payment mode list to be shown to user in criteria screen
	 */
	private Map<String, String> createPaymentModeList() {
		Map<String, String> paymentModesMap = new HashMap<String, String>();
		paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_CASH, CollectionConstants.INSTRUMENTTYPE_CASH);
		paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_CHEQUEORDD, CollectionConstants.INSTRUMENTTYPE_CHEQUEORDD);
		paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_BANK, CollectionConstants.INSTRUMENTTYPE_BANK);
		paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_ONLINE, CollectionConstants.INSTRUMENTTYPE_ONLINE);
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
	 * @param collectionsUtil the collections utility object to set
	 */
	public void setCollectionsUtil(CollectionsUtil collectionsUtil) {
		this.collectionsUtil = collectionsUtil;
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
	 * @return the payment mode (cash/cheque)
	 */
	public String getPaymentMode() {
		return (String) getReportParam(EGOV_PAYMENT_MODE);
	}
	
	/**
	 * @param paymentMode the payment mode to set (cash/cheque)
	 */
	public void setPaymentMode(String paymentMode) {
		setReportParam(EGOV_PAYMENT_MODE, paymentMode);
	}

	/**
	 * @return the payment modes
	 */
	public Map<String, String> getPaymentModes() {
		return paymentModes;
	}

	/**
	 * Action method for criteria screen
	 * 
	 * @return index
	 */
	public String criteria() {
		// Setup drop down data for department list
		addRelatedEntity("department", Department.class, "deptName");
		setupDropdownDataExcluding();
		
		// Set default values of criteria fields
		setReportParam(EGOV_FROM_DATE, new Date());
		setReportParam(EGOV_TO_DATE, new Date());
		
		Department dept = collectionsUtil.getDepartmentOfLoggedInUser(getSession());
		if(dept != null) {
			setReportParam(EGOV_DEPT_ID, dept.getId());
		}

		return INDEX;
	}

	@Override
	protected String getReportTemplateName() {
		return COLLETION_SUMMARY_TEMPLATE;
	}
}
