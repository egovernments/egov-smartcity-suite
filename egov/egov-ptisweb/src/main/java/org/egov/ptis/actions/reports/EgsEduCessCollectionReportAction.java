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
package org.egov.ptis.actions.reports;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Installment;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.ptis.bean.DemandCollInfo;
import org.egov.ptis.bean.ReportInfo;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Calendar.APRIL;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MARCH;
import static org.egov.ptis.constants.PropertyTaxConstants.EDU_EGS_CESS_GLCODE_LIST;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODE_FOR_EDU_CESS_ARREARS;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODE_FOR_EDU_CESS_CURRENT;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODE_FOR_EGS_CESS_ARREARS;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODE_FOR_EGS_CESS_CURRENT;
import static org.egov.ptis.constants.PropertyTaxConstants.MONTHS_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_START_DATE;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_TEMPLATENAME_EGS_EDU_CESS_COLLECTION;
import static org.egov.ptis.constants.PropertyTaxConstants.STR_EDU_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.STR_EGS_CESS;

/**
 * 
 * Used to generate EGS&EDU Cess Collection Report
 * 
 * @author subhash
 * 
 */

public class EgsEduCessCollectionReportAction extends BaseFormAction {

	private static final Logger LOGGER = Logger
			.getLogger(EgsEduCessCollectionReportAction.class);
	private ReportService reportService;
	private static final String SRCH_FORM = "searchForm";
	private static final String REPORT = "report";
	private String reportId;
	private Map<Integer, String> monthsMap;
	private Date day;
	private Integer month;
	private String year;
	ReportInfo reportInfo = new ReportInfo();
	private Date fromDate = new Date();
	private Date toDate = new Date();
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
	private List<Object[]> list;
	private Boolean recordsExist = TRUE;

	@Autowired
	private ReportViewerUtil reportViewerUtil;

	public void validate() {
		LOGGER.debug("Inside Validate Method, Day : " + day + "-Month : "
				+ month + "-Year : " + year);
		if ((day == null || day.equals("")) && (month == null || month == -1)
				&& (year == null || year.equals("-1"))) {
			addActionError("Please select anyone of the search criteria");
		}

		if (month != null && month != -1) {
			if (year == null || year.equals("-1")) {
				addActionError("Please select year for month");
			}
		}
		LOGGER.debug("Exit from validate method");
	}

	@Override
	public Object getModel() {
		return null;
	}

	public void prepare() {
		setMonthsMap(MONTHS_MAP);
		addDropdownData("yearsList", prepareYearsList());
	}

	@ValidationErrorPage(value = SRCH_FORM)
	public String generateReport() {
		LOGGER.debug("Inside generateReport method");
		Map<String, String> srchParams = new HashMap<String, String>();
		StringTokenizer yearTokenizer = new StringTokenizer(year, "-");
		String years[] = new String[2];
		int i = 0;
		while (yearTokenizer.hasMoreTokens()) {
			years[i++] = yearTokenizer.nextToken();
		}
		StringBuffer qryStr = new StringBuffer(
				"select ca.glcode, sum(ch.totalamount) "
						+ "from EGCL_COLLECTIONDETAILS cd, egcl_collectionheader ch, chartofaccounts ca "
						+ "where cd.id_collectionheader = ch.id "
						+ "and ca.glcode in (:glcodeList) "
						+ "and cd.id_accounthead = ca.id ");
		if (day != null && !day.equals("")) {
			String dateStr = new SimpleDateFormat("dd/MM/yyyy").format(day);
			qryStr.append("and to_char(ch.created_date, 'dd/MM/yyyy') = :day ");
			srchParams.put("day", dateStr);
			reportInfo.setDateString("Dated : " + dateStr);
		} else if (month != null && month != -1) {
			String yearStr;
			StringBuffer dateStr = new StringBuffer();
			if (month.intValue() < APRIL) {
				yearStr = String
						.valueOf(Integer.valueOf(years[0]).intValue() + 1);
			} else {
				yearStr = years[0];
			}
			String monthStr = StringUtils.leftPad(String.valueOf(month + 1), 2,
					"0");
			dateStr.append(monthStr).append("/");
			dateStr.append(yearStr);
			qryStr.append("and to_char(ch.created_date, 'MM') || '/' || to_char(ch.created_date, 'yyyy') = :month ");
			srchParams.put("month", dateStr.toString());
			Calendar calendar = Calendar.getInstance();
			calendar.set(Integer.valueOf(yearStr), month, 01);
			int maxDay = calendar.getActualMaximum(DAY_OF_MONTH);
			int minDay = calendar.getActualMinimum(DAY_OF_MONTH);
			StringBuffer fromDate = new StringBuffer();
			StringBuffer toDate = new StringBuffer();
			fromDate.append(minDay).append("/").append(monthStr).append("/")
					.append(yearStr);
			toDate.append(maxDay).append("/").append(monthStr).append("/")
					.append(yearStr);
			reportInfo.setDateString("From : " + fromDate.toString()
					+ "  To : " + toDate.toString());
		} else {
			fromDate.setYear(Integer.valueOf(years[0]).intValue());
			fromDate.setMonth(APRIL);
			fromDate.setDate(1);
			toDate.setYear(Integer.valueOf(years[0]).intValue() + 1);
			toDate.setMonth(MARCH);
			toDate.setDate(31);
			
			qryStr.append("and to_date(to_char(ch.created_date,'dd/MM/yy'),'dd/MM/yy') >= to_date(:fromDate,'dd/MM/yy') ");
			qryStr.append("and to_date(to_char(ch.created_date,'dd/MM/yy'),'dd/MM/yy') <= to_date(:toDate,'dd/MM/yy') ");
			srchParams.put("fromDate", dateFormat.format(fromDate));
			srchParams.put("toDate", dateFormat.format(toDate));
			reportInfo.setDateString("From : " + dateFormat.format(fromDate)
					+ " To : " + dateFormat.format(toDate));
		}
		
		qryStr.append("group by ca.glcode");
		SQLQuery qry = getPersistenceService().getSession().createSQLQuery(
				qryStr.toString());
		qry.setParameterList("glcodeList", EDU_EGS_CESS_GLCODE_LIST);
		for (String srchParam : srchParams.keySet()) {
			qry.setParameter(srchParam, srchParams.get(srchParam));
		}
		
		list = qry.list();
		if (list != null && !list.isEmpty()) {
			ReportRequest reportInput = prepareReportData();
			reportInput.setPrintDialogOnOpenReport(true);
			reportId = reportViewerUtil.addReportToTempCache(
					reportService.createReport(reportInput));
		} else {
			recordsExist = FALSE;
			return SRCH_FORM;
		}
		LOGGER.debug("Exit from generateReport method");
		return REPORT;
	}

	private ReportRequest prepareReportData() {

		LOGGER.debug("Inside prepareReportData method");
		List<DemandCollInfo> dmdCollInfoList = new ArrayList<DemandCollInfo>();
		DemandCollInfo dmdCollInfoEgs = new DemandCollInfo();
		dmdCollInfoEgs.setTaxType(STR_EGS_CESS);
		DemandCollInfo dmdCollInfoEdu = new DemandCollInfo();
		dmdCollInfoEdu.setTaxType(STR_EDU_CESS);
		for (Object[] obj : list) {
			if (obj[0].equals(GLCODE_FOR_EGS_CESS_ARREARS)) {
				dmdCollInfoEgs.setArrColl(new BigDecimal(obj[1].toString()));
			} else if (obj[0].equals(GLCODE_FOR_EGS_CESS_CURRENT)) {
				dmdCollInfoEgs.setCurColl(new BigDecimal(obj[1].toString()));
			} else if (obj[0].equals(GLCODE_FOR_EDU_CESS_ARREARS)) {
				dmdCollInfoEdu.setArrColl(new BigDecimal(obj[1].toString()));
			} else if (obj[0].equals(GLCODE_FOR_EDU_CESS_CURRENT)) {
				dmdCollInfoEdu.setCurColl(new BigDecimal(obj[1].toString()));
			}
		}
		dmdCollInfoList.add(dmdCollInfoEdu);
		dmdCollInfoList.add(dmdCollInfoEgs);
		if (dmdCollInfoList != null && !dmdCollInfoList.isEmpty()) {
			reportInfo.setDemandCollInfoList(dmdCollInfoList);
		}
		LOGGER.debug("Exit from prepareReportData method");
		return new ReportRequest(REPORT_TEMPLATENAME_EGS_EDU_CESS_COLLECTION,
				reportInfo, null);
	}

	private List<String> prepareYearsList() {
		StringBuffer qryStr = new StringBuffer(
				"select * from eg_installment_master "
						+ "where id_module = (select id_module from eg_module where module_name = 'Property Tax') "
						+ "and start_date >= to_date('" + REPORT_START_DATE
						+ "','dd/MM/yyyy') " + "and start_date <= sysdate");
		SQLQuery qry = getPersistenceService().getSession()
				.createSQLQuery(qryStr.toString()).addEntity(Installment.class);
		return qry.list();
	}

	@SkipValidation
	public String searchForm() {
		return SRCH_FORM;
	}

	public Map<Integer, String> getMonthsMap() {
		return monthsMap;
	}

	public void setMonthsMap(Map<Integer, String> monthsMap) {
		this.monthsMap = monthsMap;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public String getReportId() {
		return reportId;
	}

	public ReportInfo getReportInfo() {
		return reportInfo;
	}

	public void setReportInfo(ReportInfo reportInfo) {
		this.reportInfo = reportInfo;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Boolean getRecordsExist() {
		return recordsExist;
	}

	public void setRecordsExist(Boolean recordsExist) {
		this.recordsExist = recordsExist;
	}

}
