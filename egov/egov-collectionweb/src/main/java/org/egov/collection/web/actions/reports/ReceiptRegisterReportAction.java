/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.collection.web.actions.reports;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportRequest.ReportDataSourceType;
import org.egov.infra.web.struts.actions.ReportFormAction;
import org.springframework.transaction.annotation.Transactional;

/**
 * Action class for the receipt register report
 */
@ParentPackage("egov")	
@Transactional(readOnly=true)
public class ReceiptRegisterReportAction extends ReportFormAction {

	private static final long serialVersionUID = 1L;

	private static final String EGOV_FROM_DATE = "EGOV_FROM_DATE";
	private static final String EGOV_TO_DATE = "EGOV_TO_DATE";
	private static final String EGOV_DEPT_ID = "EGOV_DEPT_ID";
	private static final String EGOV_PAYMENT_MODE = "EGOV_PAYMENT_MODE";
	private static final String EGOV_STATUS_ID = "EGOV_STATUS_ID";
	
	private final Map<String, String> paymentModes = createPaymentModeList();
	private CollectionsUtil collectionsUtil;

	/**
	 * @return the payment mode list to be shown to user in criteria screen
	 */
	private Map<String, String> createPaymentModeList() {
		Map<String, String> paymentModesMap = new HashMap<String, String>();
		paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_CASH, CollectionConstants.INSTRUMENTTYPE_CASH);
		paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_CHEQUEORDD, CollectionConstants.INSTRUMENTTYPE_CHEQUEORDD);
		paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_CARD, CollectionConstants.INSTRUMENTTYPE_CARD);
		paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_BANK, CollectionConstants.INSTRUMENTTYPE_BANK);
		paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_ONLINE, CollectionConstants.INSTRUMENTTYPE_ONLINE);
		return paymentModesMap;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.egov.infra.web.struts.actions.BaseFormAction#prepare()
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
	 * @return the department id
	 */
	public Integer getStatusId() {
		return (Integer) getReportParam(EGOV_STATUS_ID);
	}

	/**
	 * @param deptId
	 *            the department id to set
	 */
	public void setStatusId(Integer statusId) {
		setReportParam(EGOV_STATUS_ID, statusId);
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
	@Action(value="/reports/receiptRegisterReport!criteria", results = { @Result(name = INDEX,type="redirect")})
	public String criteria() {
		// Setup drop down data for department list
		addRelatedEntity("department", Department.class, "deptName");
		addRelatedEntity("status", EgwStatus.class, "description");
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
		return CollectionConstants.REPORT_TEMPLATE_RECEIPT_REGISTER;
	}
	
	public List getReceiptStatuses () {
		return persistenceService.findAllBy(
				"from EgwStatus s where moduletype=? order by description",
				ReceiptHeader.class.getSimpleName());
	}
}
