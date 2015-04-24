package org.egov.erpcollection.web.actions.reports;  
  
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.egov.erpcollection.util.CollectionsUtil;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.engine.ReportRequest.ReportDataSourceType;
import org.egov.infstr.reporting.util.ReportUtil;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.lib.rjbac.user.User;
import org.egov.web.actions.BaseFormAction;
  
  
@ParentPackage("egov")  
public class ChequeCollectionReportAction extends BaseFormAction{  
	
private static final long serialVersionUID = 1L;
	
	public Map<String, Object> critParams = new HashMap<String, Object>();
	private ReportService reportService;
	private CollectionsUtil collectionsUtil;
	private Integer reportId = -1;
	
	public static final String REPORT = "report";
	private static final String EGOV_COUNTER_OPERATOR_ID = "EGOV_COUNTER_OPERATOR_ID";
	private static final String EGOV_COUNTER_ID = "EGOV_COUNTER_ID";
	private static final String EGOV_FROM_DATE = "EGOV_FROM_DATE";
	private static final String EGOV_TO_DATE = "EGOV_TO_DATE";
	private static final String EGOV_BOUNDARY_ID = "EGOV_BOUNDARY_ID";
	private static final String EGOV_RECEIPT_IDS = "EGOV_RECEIPT_IDS";
	private static final String CHEQUE_COLLETION_TEMPLATE = "chequeCollectionReport";

	public Object getModel() {
		return null;
	}
	
	public void setFromDate(Date fromDate) {
		critParams.put(EGOV_FROM_DATE, fromDate);
	}

	public void setToDate(Date toDate) {
		critParams.put(EGOV_TO_DATE, toDate);
	}

	public void setCounterId(Long counterId) {
		critParams.put(EGOV_COUNTER_ID, counterId);
	}
	
	public void setUserId(Long userId) {
		critParams.put(EGOV_COUNTER_OPERATOR_ID, userId);
	}
	
	public void setBoundaryId(Long boundaryId) {
		critParams.put(EGOV_BOUNDARY_ID, boundaryId);
	}

	public Date getFromDate() {
		return (Date)critParams.get(EGOV_FROM_DATE);
	}

	public Date getToDate() {
		return (Date)critParams.get(EGOV_TO_DATE);
	}

	public Long getCounterId() {
		return (Long)critParams.get(EGOV_COUNTER_ID);
	}
	
	public Long getUserId() {
		return (Long)critParams.get(EGOV_COUNTER_OPERATOR_ID);
	}
	
	public Long getBoundaryId() {
		return (Long)critParams.get(EGOV_BOUNDARY_ID);
	}

	/**
	 * @return the reportId
	 */
	public Integer getReportId() {
		return reportId;
	}

	private void initializeCriteriaMap() {
		critParams.clear();
		critParams.put(EGOV_COUNTER_OPERATOR_ID, Long.valueOf(-1L));
		critParams.put(EGOV_COUNTER_ID, Long.valueOf(-1L));
		critParams.put(EGOV_FROM_DATE, new Date());
		critParams.put(EGOV_TO_DATE, new Date());
		critParams.put(EGOV_BOUNDARY_ID, Long.valueOf(-1L));
	}

	public void prepare() {
		super.prepare();
		setupDropdownDataExcluding();
		addDropdownData(CollectionConstants.DROPDOWN_DATA_COUNTER_LIST,
				collectionsUtil.getAllCounters());
		addDropdownData(CollectionConstants.DROPDOWN_DATA_RECEIPT_CREATOR_LIST,
				collectionsUtil.getReceiptCreators());
		addDropdownData(CollectionConstants.DROPDOWN_DATA_RECEIPTZONE_LIST,
				collectionsUtil.getReceiptZoneList());
		initializeCriteriaMap();
	}

	/**
	 * Action method to create the cheque submission report
	 * 
	 * @return report
	 */
	public String submissionReport() {
		Map<String, Object> session = getSession();
		User user = collectionsUtil.getLoggedInUser(getSession());

		Date today = ReportUtil.today();
		critParams.put(EGOV_FROM_DATE, today);
		critParams.put(EGOV_TO_DATE, today);
		
		critParams.put(EGOV_COUNTER_OPERATOR_ID, user.getId().longValue());
		critParams.put(EGOV_COUNTER_ID, collectionsUtil.getLocationOfUser(
				getSession()).getId().longValue());
		critParams.put(EGOV_RECEIPT_IDS, Arrays.asList((Long[])session
				.get(CollectionConstants.SESSION_VAR_RECEIPT_IDS)));

		return report();
	}

	/**
	 * Action method that creates the report
	 * 
	 * @return view
	 */
	public String report() {
		ReportRequest reportInput = new ReportRequest(CHEQUE_COLLETION_TEMPLATE,
				critParams, ReportDataSourceType.SQL);
		ReportOutput reportOutput = reportService.createReport(reportInput);
		reportId = ReportViewerUtil.addReportToSession(reportOutput, getSession());
		return REPORT;
	}

	public String criteria() {
		return INDEX;
	}

	/**
	 * @param reportService
	 *            the reportService to set
	 */
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	/**
	 * @param critParams the critParams to set
	 */
	public void setCritParams(Map<String, Object> critParams) {
		this.critParams = critParams;
	}

	/**
	 * @return the critParams
	 */
	public Map<String, Object> getCritParams() {
		return critParams;
	}
	
	/**
	 * @param collectionsUtil
	 *            the Collections Utility object to set
	 */
	public void setCollectionsUtil(CollectionsUtil collectionsUtil) {
		this.collectionsUtil = collectionsUtil;
	}


	
}