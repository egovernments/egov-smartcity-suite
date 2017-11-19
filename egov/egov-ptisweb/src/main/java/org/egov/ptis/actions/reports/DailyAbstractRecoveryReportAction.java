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

import org.apache.struts2.convention.annotation.Action;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.ptis.bean.RecoveryInfo;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.egov.ptis.constants.PropertyTaxConstants.*;

/**
 * 
 * @author subhash
 * 
 */ 
@Transactional(readOnly = true)
public class DailyAbstractRecoveryReportAction extends BaseFormAction {

	private static final String REPORT = "report";
	private static final long serialVersionUID = 1L;
	private ReportService reportService;
	private String reportId;

	@Autowired
	private ReportViewerUtil reportViewerUtil;

	@Override
	public Object getModel() {
		return null;
	}

	@Action(value = "/reports/dailyAbstractRecoveryReport.action")
	public String execute() {
		List<RecoveryInfo> resultList = new ArrayList<RecoveryInfo>();
		StringBuffer qryString = new StringBuffer("select CREATED_DATE, ")
				.append("SUM(decode(glcode, '")
				.append(GLCODE_FOR_GENERAL_TAX_ARREARS)
				.append("', amount,'")
				.append(GLCODE_FOR_GENERAL_TAX_CURRENT)
				.append("', amount, 0)) as GeneralTax, ")
				.append("SUM(decode(glcode, '")
				.append(GLCODE_FOR_FIRE_SERVICE_TAX_ARREARS)
				.append("', amount,'")
				.append(GLCODE_FOR_FIRE_SERVICE_TAX_CURRENT)
				.append("', amount, 0)) as FireTax, ")
				.append("SUM(decode(glcode, '")
				.append(GLCODE_FOR_GENERAL_WATER_TAX_ARREARS)
				.append("', amount,'")
				.append(GLCODE_FOR_GENERAL_WATER_TAX_CURRENT)
				.append("', amount, 0)) as WaterTax, ")
				.append("SUM(decode(glcode, '")
				.append(GLCODE_FOR_SEWERAGE_TAX_ARREARS)
				.append("', amount,'")
				.append(GLCODE_FOR_SEWERAGE_TAX_CURRENT)
				.append("', amount, 0)) as SewerageTax, ")
				.append("SUM(decode(glcode, '")
				.append(GLCODE_FOR_LIGHTINGTAX_ARREARS)
				.append("', amount,'")
				.append(GLCODE_FOR_LIGHTINGTAX_CURRENT)
				.append("', amount, 0)) as LightTax, ")
				.append("SUM(decode(glcode, '")
				.append(GLCODE_FOR_EDU_CESS_ARREARS)
				.append("', amount,'")
				.append(GLCODE_FOR_EDU_CESS_CURRENT)
				.append("', amount, 0)) as EduCessTax, ")
				.append("SUM(decode(glcode, '")
				.append(GLCODE_FOR_BIG_RESIDENTIAL_BLDG_TAX_ARREARS)
				.append("', amount,'")
				.append(GLCODE_FOR_BIG_RESIDENTIAL_BLDG_TAX_CURRENT)
				.append("', amount, 0)) as BigBldgTax, ")
				.append("SUM(decode(glcode, '")
				.append(GLCODE_FOR_EGS_CESS_ARREARS)
				.append("', amount,'")
				.append(GLCODE_FOR_EGS_CESS_CURRENT)
				.append("', amount, 0)) as EgsTax ")
				.append("FROM ")
				.append("(SELECT TRUNC(CH.created_date) AS CREATED_DATE, COA.glcode AS GLCODE, SUM(cd.cramount) AS AMOUNT ")
				.append("FROM EGCL_COLLECTIONHEADER CH, EGCL_COLLECTIONDETAILS CD, EGW_STATUS ST, EG_SERVICEDETAILS SD, chartofaccounts COA ")
				.append("WHERE CH.id = cd.id_collectionheader ")
				.append("AND CH.id_status = ST.ID AND ST.MODULETYPE = 'ReceiptHeader' AND ST.description = 'Approved' ")
				.append("AND CH.id_service = SD.ID ").append("AND SD.code = 'PT' ")
				.append("AND CD.id_accounthead = COA.ID ");
		StringBuffer groupByString = new StringBuffer("GROUP BY TRUNC(CH.created_date), COA.glcode) ").append(
				"GROUP BY CREATED_DATE ").append("ORDER BY CREATED_DATE DESC ");
		resultList.add(prepareRowOne(qryString, groupByString));
		resultList.add(prepareRowTwo(qryString, groupByString));
		resultList.add(prepareRowThree(qryString, groupByString));
		resultList.add(prepareRowFour(qryString, groupByString));
		resultList.add(prepareRowFive(qryString, groupByString));

		Map<String, Object> reportParams = prepareReportParams();
		ReportRequest reportRequest = new ReportRequest(REPORT_TEMPLATENAME_DAILY_ABSTRACT_RECOVERY_REPORT, resultList,
				reportParams);
		reportId = reportViewerUtil.addReportToTempCache(reportService.createReport(reportRequest));
		return REPORT;
	}

	private Map<String, Object> prepareReportParams() {
		Map<String, Object> reportParams = new HashMap<String, Object>();
		Calendar reportFromDate = Calendar.getInstance();
		Calendar reportToDate = Calendar.getInstance();
		int currMonth = reportFromDate.get(Calendar.MONTH);
		if (currMonth < 3) {
			reportFromDate.add(Calendar.YEAR, -1);
		}
		reportFromDate.set(Calendar.DATE, 1);
		reportFromDate.set(Calendar.MONTH, Calendar.APRIL);
		reportParams.put("reportFDate", reportFromDate.getTime());
		reportToDate.add(Calendar.DATE, -1);
		reportParams.put("reportTDate", reportToDate.getTime());
		Calendar currDate = Calendar.getInstance();
		int year = Integer.valueOf(String.valueOf(currDate.get(Calendar.YEAR)).substring(2)).intValue();
		String currFinYear = null;
		String prevFinYear = null;
		if (currMonth > 2) {
			currFinYear = String.valueOf(year) + "-" + String.valueOf(year + 1);
			prevFinYear = String.valueOf(year - 1) + "-" + String.valueOf(year);
		} else {
			currFinYear = String.valueOf(year - 1) + "-" + String.valueOf(year);
			prevFinYear = String.valueOf(year - 2) + "-" + String.valueOf(year - 1);
		}
		reportParams.put("currFinYear", currFinYear);
		reportParams.put("prevFinYear", prevFinYear);
		return reportParams;
	}

	private RecoveryInfo prepareRowOne(StringBuffer qry, StringBuffer groupByClause) {
		Calendar fromDate = Calendar.getInstance();
		Calendar toDate = Calendar.getInstance();
		int currMonth = fromDate.get(Calendar.MONTH);
		if (currMonth < 3) {
			fromDate.add(Calendar.YEAR, -1);
		}
		fromDate.set(Calendar.MONTH, Calendar.APRIL);
		fromDate.set(Calendar.DATE, 1);
		toDate.add(Calendar.MONTH, -1);
		toDate.set(Calendar.DATE, toDate.getActualMaximum(Calendar.DAY_OF_MONTH));
		String whereClause = "AND TRUNC(CH.created_date) >= :fromDate AND TRUNC(CH.created_date) <= :toDate ";
		StringBuffer sqlQry = new StringBuffer(qry).append(whereClause).append(groupByClause);
		return prepareReportData(sqlQry, fromDate, toDate);
	}

	private RecoveryInfo prepareRowTwo(StringBuffer qry, StringBuffer groupByClause) {
		Calendar fromDate = Calendar.getInstance();
		fromDate.add(Calendar.DATE, -1);
		String whereClause = "AND TRUNC(CH.created_date) = :fromDate ";
		StringBuffer sqlQry = new StringBuffer(qry).append(whereClause).append(groupByClause);
		return prepareReportData(sqlQry, fromDate, null);
	}

	private RecoveryInfo prepareRowThree(StringBuffer qry, StringBuffer groupByClause) {
		Calendar fromDate = Calendar.getInstance();
		Calendar toDate = Calendar.getInstance();
		fromDate.set(Calendar.DATE, 1);
		toDate.add(Calendar.DATE, -2);
		String whereClause = "AND TRUNC(CH.created_date) >= :fromDate AND TRUNC(CH.created_date) <= :toDate ";
		StringBuffer sqlQry = new StringBuffer(qry).append(whereClause).append(groupByClause);
		return prepareReportData(sqlQry, fromDate, toDate);
	}

	private RecoveryInfo prepareRowFour(StringBuffer qry, StringBuffer groupByClause) {
		Calendar.getInstance().set(Calendar.getInstance().get(Calendar.YEAR), Calendar.APRIL, 1);
		Calendar fromDate = Calendar.getInstance();
		Calendar toDate = Calendar.getInstance();
		fromDate.set(Calendar.DATE, 1);
		toDate.add(Calendar.DATE, -1);
		String whereClause = "AND TRUNC(CH.created_date) >= :fromDate AND TRUNC(CH.created_date) <= :toDate ";
		StringBuffer sqlQry = new StringBuffer(qry).append(whereClause).append(groupByClause);
		return prepareReportData(sqlQry, fromDate, toDate);
	}

	private RecoveryInfo prepareRowFive(StringBuffer qry, StringBuffer groupByClause) {
		Calendar fromDate = Calendar.getInstance();
		int currMonth = fromDate.get(Calendar.MONTH);
		if (currMonth < 3) {
			fromDate.add(Calendar.YEAR, -1);
		}
		Calendar toDate = Calendar.getInstance();
		fromDate.set(Calendar.MONTH, Calendar.APRIL);
		fromDate.set(Calendar.DATE, 1);
		toDate.add(Calendar.DATE, -1);
		String whereClause = "AND TRUNC(CH.created_date) >= :fromDate AND TRUNC(CH.created_date) <= :toDate ";
		StringBuffer sqlQry = new StringBuffer(qry).append(whereClause).append(groupByClause);
		return prepareReportData(sqlQry, fromDate, toDate);
	}

	@SuppressWarnings("unchecked")
	private RecoveryInfo prepareReportData(StringBuffer qry, Calendar fDate, Calendar tDate) {
		RecoveryInfo recInfo = new RecoveryInfo();

		Query sqlQry = persistenceService.getSession().createSQLQuery(qry.toString());
		if (fDate != null) {
			recInfo.setFromDate(fDate.getTime());
			sqlQry.setDate("fromDate", fDate.getTime());
		}
		if (tDate != null) {
			recInfo.setToDate(tDate.getTime());
			sqlQry.setDate("toDate", tDate.getTime());
		}
		List<Object[]> currFinYearResList = sqlQry.list();

		if (fDate != null) {
			fDate.add(Calendar.YEAR, -1);
			sqlQry.setDate("fromDate", fDate.getTime());
		}
		if (tDate != null) {
			tDate.add(Calendar.YEAR, -1);
			sqlQry.setDate("toDate", tDate.getTime());
		}
		List<Object[]> prevFinYearResList = sqlQry.list();

		for (Object[] currFinYearRec : currFinYearResList) {
			recInfo.setGenTax(recInfo.getGenTax().add((BigDecimal) currFinYearRec[1]));
			recInfo.setFireTax(recInfo.getFireTax().add((BigDecimal) currFinYearRec[2]));
			recInfo.setWaterTax(recInfo.getWaterTax().add((BigDecimal) currFinYearRec[3]));
			recInfo.setSewerageTax(recInfo.getSewerageTax().add((BigDecimal) currFinYearRec[4]));
			recInfo.setLightTax(recInfo.getLightTax().add((BigDecimal) currFinYearRec[5]));
			recInfo.setEduCess(recInfo.getEduCess().add((BigDecimal) currFinYearRec[6]));
			recInfo.setBigBldgTax(recInfo.getBigBldgTax().add((BigDecimal) currFinYearRec[7]));
			recInfo.setEgsCess(recInfo.getEgsCess().add((BigDecimal) currFinYearRec[8]));
		}
		recInfo.setTotCurrYearColl(recInfo.getGenTax().add(recInfo.getFireTax()).add(recInfo.getWaterTax())
				.add(recInfo.getSewerageTax()).add(recInfo.getLightTax()).add(recInfo.getEduCess())
				.add(recInfo.getBigBldgTax()).add(recInfo.getEgsCess()));
		BigDecimal total = BigDecimal.ZERO;
		for (Object[] prevFinYearRec : prevFinYearResList) {
			total = total.add((BigDecimal) prevFinYearRec[1]).add((BigDecimal) prevFinYearRec[2])
					.add((BigDecimal) prevFinYearRec[3]).add((BigDecimal) prevFinYearRec[4])
					.add((BigDecimal) prevFinYearRec[5]).add((BigDecimal) prevFinYearRec[6])
					.add((BigDecimal) prevFinYearRec[7]).add((BigDecimal) prevFinYearRec[8]);
		}
		recInfo.setTotPrevYearColl(total);
		return recInfo;
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
}
