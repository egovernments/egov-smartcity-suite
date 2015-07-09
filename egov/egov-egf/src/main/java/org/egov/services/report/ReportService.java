/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.services.report;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Fund;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.utils.Constants;
import org.egov.web.actions.report.IEStatementEntry;
import org.egov.web.actions.report.Statement;
import org.egov.web.actions.report.StatementEntry;
import org.egov.web.actions.report.StatementResultObject;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ReportService  {
	@Autowired AppConfigValuesDAO appConfigValuesDAO;
	int minorCodeLength;
	List<Character> coaType = new ArrayList<Character>();
	private FinancialYearHibernateDAO financialYearDAO;
	String voucherStatusToExclude;
	final static Logger LOGGER = Logger.getLogger(ReportService.class);

	public void setFinancialYearDAO(FinancialYearHibernateDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}

	public Date getPreviousYearFor(Date date) {
		GregorianCalendar previousYearToDate = new GregorianCalendar();
		previousYearToDate.setTime(date);
		int prevYear = previousYearToDate.get(Calendar.YEAR) - 1;
		previousYearToDate.set(Calendar.YEAR, prevYear);
		return previousYearToDate.getTime();
	}

	public List<Fund> getFunds() {
		Criteria voucherHeaderCriteria =HibernateUtil.getCurrentSession().createCriteria(
				CVoucherHeader.class);
		List fundIdList = voucherHeaderCriteria.setProjection(
				Projections.distinct(Projections.property("fundId.id"))).list();
		if (!fundIdList.isEmpty())
			return HibernateUtil.getCurrentSession().createCriteria(Fund.class).add(
					Restrictions.in("id", fundIdList)).list();
		return new ArrayList<Fund>();
	}

	public String getGlcodeForPurposeCode(Integer purposeId) {
		Query query =HibernateUtil.getCurrentSession().createSQLQuery(
				"select majorcode from chartofaccounts where purposeid="
						+ purposeId);
		List list = query.list();
		String glCode = "";
		if (list.get(0) != null)
			glCode = list.get(0).toString();
		return glCode;
	}

	protected String getFilterQuery(Statement balanceSheet) {
		String query = "";
		if (balanceSheet.getDepartment() != null
				&& balanceSheet.getDepartment().getId() != null
				&& balanceSheet.getDepartment().getId() != 0)
			query = query + " and mis.departmentid="
					+ balanceSheet.getDepartment().getId().toString();
		if (balanceSheet.getFunction() != null
				&& balanceSheet.getFunction().getId() != null
				&& balanceSheet.getFunction().getId() != 0)
			query = query + " and g.functionid="
					+ balanceSheet.getFunction().getId().toString();
		if (balanceSheet.getFunctionary() != null
				&& balanceSheet.getFunctionary().getId() != null
				&& balanceSheet.getFunctionary().getId() != 0)
			query = query + " and mis.functionaryid="
					+ balanceSheet.getFunctionary().getId().toString();
		if (balanceSheet.getField() != null
				&& balanceSheet.getField().getId() != null
				&& balanceSheet.getField().getId() != 0)
			query = query + " and mis.divisionid="
					+ balanceSheet.getField().getId().toString();
		if (balanceSheet.getFund() != null
				&& balanceSheet.getFund().getId() != null
				&& balanceSheet.getFund().getId() != 0)
			query = query + " and v.fundid="
					+ balanceSheet.getFund().getId().toString();
		return query;
	}

	public String getFundNameForId(List<Fund> fundList, Integer id) {
		for (Fund fund : fundList) {
			if (id.equals(fund.getId()))
				return fund.getName();
		}
		return "";
	}
	public String getfundList(List<Fund> fundList){
		StringBuffer fundId=new StringBuffer();
		fundId.append("(");
		for (Fund fund : fundList) {
			fundId.append(fund.getId()).append(",");
		}fundId.setLength(fundId.length()-1);
		fundId.append(")");
		return fundId.toString();
	}

	public BigDecimal divideAndRound(BigDecimal value, BigDecimal divisor) {
		value = value.divide(divisor, 2, BigDecimal.ROUND_HALF_UP);
		return value;
	}

	protected String getTransactionQuery(Statement balanceSheet) {
		String query = "";
		if (balanceSheet.getDepartment() != null
				&& balanceSheet.getDepartment().getId() != null
				&& balanceSheet.getDepartment().getId() != 0)
			query = query + " and ts.departmentid="
					+ balanceSheet.getDepartment().getId().toString();
		if (balanceSheet.getFunction() != null
				&& balanceSheet.getFunction().getId() != null
				&& balanceSheet.getFunction().getId() != 0)
			query = query + " and ts.functionid="
					+ balanceSheet.getFunction().getId().toString();
		if (balanceSheet.getFunctionary() != null
				&& balanceSheet.getFunctionary().getId() != null
				&& balanceSheet.getFunctionary().getId() != 0)
			query = query + " and ts.functionaryid="
					+ balanceSheet.getFunctionary().getId().toString();
		if (balanceSheet.getField() != null
				&& balanceSheet.getField().getId() != null
				&& balanceSheet.getField().getId() != 0)
			query = query + " and ts.divisionid="
					+ balanceSheet.getField().getId().toString();
		if (balanceSheet.getFund() != null
				&& balanceSheet.getFund().getId() != null
				&& balanceSheet.getFund().getId() != 0)
			query = query + " and ts.fundid="
					+ balanceSheet.getFund().getId().toString();
		return query;
	}

	public String getFormattedDate(Date date) {
		SimpleDateFormat formatter = Constants.DDMMYYYYFORMAT1;
		return formatter.format(date);
	}

	public String getAppConfigValueFor(String module, String key) {
		try {
			return appConfigValuesDAO
					.getConfigValuesByModuleAndKey(module, key).get(0)
					.getValue();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			new ValidationException(Arrays.asList(new ValidationError(key
					+ "is not defined in appconfig", key
					+ "is not defined in appconfig")));
		}
		return "";
	}

	void addFundAmount(List<Fund> fundList, Statement type, BigDecimal divisor,
			StatementResultObject row) {
		for (int index = 0; index < type.size(); index++) {
			BigDecimal amount = divideAndRound(row.getAmount(), divisor);
			if (type.get(index).getGlCode() != null
					&& row.getGlCode().equals(type.get(index).getGlCode()))
				type.get(index).getFundWiseAmount().put(
						getFundNameForId(fundList, Integer.valueOf((row
								.getFundId()))), amount);
		}
	}
	void addFundAmountIE(List<Fund> fundList, Statement type, BigDecimal divisor,
			StatementResultObject row) {
		for (int index = 0; index < type.sizeIE(); index++) {
			BigDecimal amount = divideAndRound(row.getAmount(), divisor);
			
			if (type.getIE(index).getGlCode() != null && row.getGlCode().equals(type.getIE(index).getGlCode()))
				type.getIE(index).getNetAmount().put(getFundNameForId(fundList, Integer.valueOf((row.getFundId()))), amount);
		}
	}
	
	List<StatementResultObject> getRowWithGlCode(
			List<StatementResultObject> results, String glCode) {
		List<StatementResultObject> resultList = new ArrayList<StatementResultObject>();
		for (StatementResultObject balanceSheetQueryObject : results) {
			if (glCode.equalsIgnoreCase(balanceSheetQueryObject.getGlCode()) && balanceSheetQueryObject.getAmount().compareTo(BigDecimal.ZERO)!=0)
				resultList.add(balanceSheetQueryObject);
		}
		return resultList;
	}

	protected abstract void addRowsToStatement(Statement balanceSheet,
			Statement assets, Statement liabilities);

	protected List<StatementResultObject> getAllGlCodesFor(
			String scheduleReportType) {
		Query query =HibernateUtil.getCurrentSession()
				.createSQLQuery(
						"select distinct coa.majorcode as glCode,s.schedule as scheduleNumber,"
								+ "s.schedulename as scheduleName,coa.type as type from chartofaccounts coa, schedulemapping s "
								+ "where s.id=coa.scheduleid and coa.classification=2 and s.reporttype = '"
								+ scheduleReportType
								+ "' order by coa.majorcode").addScalar(
						"glCode").addScalar("scheduleNumber").addScalar(
						"scheduleName").addScalar("type").setResultTransformer(
						Transformers.aliasToBean(StatementResultObject.class));
		return query.list();
	}

	List<StatementResultObject> getTransactionAmount(String filterQuery,
			Date toDate, Date fromDate, String coaType, String subReportType) {
		voucherStatusToExclude = getAppConfigValueFor("finance",
				"statusexcludeReport");
		Query query =HibernateUtil.getCurrentSession()
				.createSQLQuery(
						"select c.majorcode as glCode,v.fundid as fundId,c.type as type,sum(debitamount)-sum(creditamount) as amount"
								+ " from generalledger g,chartofaccounts c,voucherheader v ,vouchermis mis where v.id=mis.voucherheaderid and "
								+ "v.id=g.voucherheaderid and c.type in("
								+ coaType
								+ ") and c.id=g.glcodeid and v.status not in("
								+ voucherStatusToExclude
								+ ")  AND v.voucherdate <= '"
								+ getFormattedDate(toDate)
								+ "' and v.voucherdate >='"
								+ getFormattedDate(fromDate)
								+ "' and substr(c.glcode,0,"
								+ minorCodeLength
								+ ") in "
								+ "(select distinct coa2.glcode from chartofaccounts coa2, schedulemapping s where s.id=coa2.scheduleid and "
								+ "coa2.classification=2 and s.reporttype = '"
								+ subReportType
								+ "') "
								+ filterQuery
								+ " group by c.majorcode,v.fundid,c.type order by c.majorcode")
				.addScalar("glCode").addScalar("fundId").addScalar("type")
				.addScalar("amount").setResultTransformer(
						Transformers.aliasToBean(StatementResultObject.class));
		return query.list();
	}

	protected Map<String, String> getSubSchedule(String subReportType) {
		Map<String, String> scheduleNumberToName = new HashMap<String, String>();
		List<Object[]> rows =HibernateUtil.getCurrentSession()
				.createSQLQuery(
						"select s.schedule,sub.subschedulename from egf_subschedule sub,schedulemapping s "
								+ "where sub.reporttype='"
								+ subReportType
								+ "' and sub.SUBSCHNAME=s.REPSUBTYPE").list();
		for (Object[] row : rows) {
			scheduleNumberToName.put(row[0].toString(), row[1].toString());
		}
		return scheduleNumberToName;
	}

	public Date getFromDate(Statement statement) {
		CFinancialYear financialYear = null;
		if ("Date".equalsIgnoreCase(statement.getPeriod())
				&& statement.getAsOndate() != null) {
			String financialYearId = financialYearDAO.getFinancialYearId(getFormattedDate(statement.getAsOndate()));
			financialYear = (CFinancialYear) financialYearDAO
					.getFinancialYearById(Long.valueOf(financialYearId));
			statement.setFinancialYear(financialYear);
		} else {
			financialYear = statement.getFinancialYear();
		}
		return financialYear.getStartingDate();
	}

	public Date getToDate(Statement statement) {
		if ("Date".equalsIgnoreCase(statement.getPeriod())
				&& statement.getAsOndate() != null)
			return statement.getAsOndate();
		if ("Half Yearly".equalsIgnoreCase(statement.getPeriod())) {
			String halfYearly = getAppConfigValueFor("EGF",
					"bs_report_half_yearly");
			String[] halfYearComponents = halfYearly.split("/");
			Calendar fin = Calendar.getInstance();
			fin.setTime(statement.getFinancialYear().getStartingDate());
			Calendar calendar = Calendar.getInstance();
			calendar.set(fin.get(Calendar.YEAR), Integer
					.parseInt(halfYearComponents[1]) - 1, Integer
					.parseInt(halfYearComponents[0]));
			return calendar.getTime();
		}
		return statement.getFinancialYear().getEndingDate();
	}

	void addFundAmount(StatementEntry entry, Map<String, BigDecimal> fundTotals) {
		for (Entry<String, BigDecimal> row : entry.getFundWiseAmount()
				.entrySet()) {
			String key = row.getKey();
			if (!fundTotals.containsKey(key))
				fundTotals.put(key, BigDecimal.ZERO);
			fundTotals.put(key, row.getValue().add(fundTotals.get(key)));
		}
	}
	
	

	void addTotalRowToPreviousGroup(List<StatementEntry> list,
			Map<String, String> schedueNumberToNameMap, StatementEntry entry) {
		list.add(new StatementEntry("", schedueNumberToNameMap.get(entry
				.getScheduleNo()), "", null, null, true));
	}
	
	void addTotalRowToPreviousGroupIE(List<IEStatementEntry> list,
			Map<String, String> schedueNumberToNameMap, IEStatementEntry entry) {
		list.add(new IEStatementEntry("", schedueNumberToNameMap.get(entry
				.getScheduleNo()),true));
	}

	void removeFundsWithNoDataIE(Statement statement) {
		Map<String, Boolean> fundToBeRemoved = new HashMap<String, Boolean>();
		for (Fund fund : statement.getFunds()) {
			fundToBeRemoved.put(fund.getName(), Boolean.TRUE);
		}
		for (Iterator<Fund> fund = statement.getFunds().iterator(); fund
				.hasNext();) {
			Fund next = fund.next();
			for (IEStatementEntry balanceSheetEntry : statement.getIeEntries()) {
				if (balanceSheetEntry.getNetAmount().containsKey(
						next.getName()) || balanceSheetEntry.getPreviousYearAmount().containsKey(
								next.getName()))
					fundToBeRemoved.put(next.getName(), Boolean.FALSE);
			}
			if (fundToBeRemoved.get(next.getName()).booleanValue())
				fund.remove();
		}
		
	}
	void removeFundsWithNoData(Statement statement) {
		Map<String, Boolean> fundToBeRemoved = new HashMap<String, Boolean>();
		for (Fund fund : statement.getFunds()) {
			fundToBeRemoved.put(fund.getName(), Boolean.TRUE);
		}
		for (Iterator<Fund> fund = statement.getFunds().iterator(); fund
				.hasNext();) {
			Fund next = fund.next();
			for (StatementEntry balanceSheetEntry : statement.getEntries()) {
				if (balanceSheetEntry.getFundWiseAmount().containsKey(
						next.getName()))
					fundToBeRemoved.put(next.getName(), Boolean.FALSE);
			}
			if (fundToBeRemoved.get(next.getName()).booleanValue())
				fund.remove();
		}
	}

	protected void populateSchedule(Statement statement, String reportSubType) {
		Query query =HibernateUtil.getCurrentSession()
				.createSQLQuery(
						"select c.majorcode,s.schedulename,s.schedule from chartofaccounts c,schedulemapping s "
								+ "where s.id=c.scheduleid and s.reporttype = '"
								+ reportSubType
								+ "' and c.type in(:coaType) group by c.majorcode,s.schedulename,s.schedule ORDER BY c.majorcode")
				.setParameter("coaType", coaType);
		List<Object[]> scheduleList = query.list();
		for (Object[] obj : scheduleList) {
			for (int index = 0; index < statement.size(); index++) {
				if (obj[0] == null)
					obj[0] = "";
				if (statement.get(index).getGlCode() != null
						&& obj[0].toString().equals(
								statement.get(index).getGlCode())) {
					statement.get(index).setAccountName(obj[1].toString());
					statement.get(index).setScheduleNo(obj[2].toString());
				}
			}
		}
	}

	protected BigDecimal zeroOrValue(BigDecimal value) {
		return value == null ? BigDecimal.ZERO : value;
	}

	protected void computeCurrentYearTotals(Statement statement, String type1,
			String type2) {
		for (StatementEntry balanceSheetEntry : statement.getEntries()) {
			if (type1.equals(balanceSheetEntry.getAccountName())
					|| type2.equals(balanceSheetEntry.getAccountName())
					|| balanceSheetEntry.isDisplayBold())
				continue;
			BigDecimal currentYearTotal = BigDecimal.ZERO;
			for (Entry<String, BigDecimal> entry : balanceSheetEntry
					.getFundWiseAmount().entrySet()) {
				currentYearTotal = currentYearTotal.add(entry.getValue());
			}
			balanceSheetEntry.setCurrentYearTotal(currentYearTotal);
		}
	}
	
}
