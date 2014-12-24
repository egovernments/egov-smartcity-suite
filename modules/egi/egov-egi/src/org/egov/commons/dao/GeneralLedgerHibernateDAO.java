/*
 * @(#)GeneralLedgerHibernateDAO.java 3.0, 11 Jun, 2013 3:45:32 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.commons.CGeneralLedger;
import org.egov.exceptions.EGOVException;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

public class GeneralLedgerHibernateDAO extends GenericHibernateDAO implements GeneralLedgerDAO {
	private static final Logger LOG = LoggerFactory.getLogger(GeneralLedgerHibernateDAO.class);

	public GeneralLedgerHibernateDAO() {
		super(CGeneralLedger.class, null);
	}

	public GeneralLedgerHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	/**
	 * This method will calculate the Actuals for the previous year.
	 */
	@Override
	public String getActualsPrev(final String accCode, final String functionId, final String budgetingType) throws Exception {
		final FinancialYearDAO fiscal = CommonsDaoFactory.getDAOFactory().getFinancialYearDAO();
		final String financialperiodId = fiscal.getPrevYearFiscalId();
		final FiscalPeriodDAO fiscalperiod = CommonsDaoFactory.getDAOFactory().getFiscalPeriodDAO();
		final String fiscalperiodId = fiscalperiod.getFiscalPeriodIds(financialperiodId);
		String result = "";
		String hqlQuery = "";
		ArrayList list = new ArrayList();
		/*
		 * Budgeting type is hardcoded here to frame the query. 1 - sum(debitamount) - sum(creditamount) 2 - sum(creditamount) 3 - sum(debitamount) Based on the budgeting type the query will differ.
		 */
		if (!functionId.equalsIgnoreCase("0")) {
			if (budgetingType.equalsIgnoreCase("1")) {
				hqlQuery = "select sum(cgeneralledger.debitAmount)-sum(cgeneralledger.creditAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.voucherHeaderId.id=cvoucherheader.id and cvoucherheader.fiscalPeriodId in ("
						+ fiscalperiodId + ") and cgeneralledger.functionId='" + functionId + "'  and cgeneralledger.glcode like '" + accCode + "'|| '%'";
			} else if (budgetingType.equalsIgnoreCase("2")) {
				hqlQuery = "select sum(cgeneralledger.creditAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.id.voucherHeaderId=cvoucherheader.id and cvoucherheader.fiscalPeriodId in (" + fiscalperiodId
						+ ") and cgeneralledger.functionId='" + functionId + "'  and cgeneralledger.glcode like '" + accCode + "'|| '%'";
			} else if (budgetingType.equalsIgnoreCase("3")) {
				hqlQuery = "select sum(cgeneralledger.debitAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.id.voucherHeaderId=cvoucherheader.id and cvoucherheader.fiscalPeriodId in (" + fiscalperiodId
						+ ") and cgeneralledger.functionId='" + functionId + "'  and cgeneralledger.glcode like '" + accCode + "'|| '%'";
			}
		} else if (functionId.equalsIgnoreCase("0")) {
			if (budgetingType.equalsIgnoreCase("1")) {
				hqlQuery = "select sum(cgeneralledger.debitAmount)-sum(cgeneralledger.creditAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.voucherHeaderId.id=cvoucherheader.id and cvoucherheader.fiscalPeriodId in ("
						+ fiscalperiodId + ") and cgeneralledger.glcode like '" + accCode + "'|| '%'";
			} else if (budgetingType.equalsIgnoreCase("2")) {
				hqlQuery = "select sum(cgeneralledger.creditAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.voucherHeaderId.id=cvoucherheader.id and cvoucherheader.fiscalPeriodId in (" + fiscalperiodId
						+ ") and cgeneralledger.glcode like '" + accCode + "'|| '%'";
			} else if (budgetingType.equalsIgnoreCase("3")) {
				hqlQuery = "select sum(cgeneralledger.debitAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.voucherHeaderId.id=cvoucherheader.id and cvoucherheader.fiscalPeriodId in (" + fiscalperiodId
						+ ")  and cgeneralledger.glcode like '" + accCode + "'|| '%'";
			}
		}
		try {
			final Query query = getSession().createQuery(hqlQuery);
			list = (ArrayList) query.list();
		} catch (final Exception e) {
			LOG.error("Error occurred while getting Actuals Prev", e);
			throw new EGOVException("Error occurred while getting Actuals Prev", e);
		}
		if (list.size() > 0) {
			if (list.get(0) == null) {
				return 0.0 + "";
			} else {
				result = list.get(0).toString();
			}
		} else {
			return 0.0 + "";
		}

		if (result.startsWith("-")) {
			result = result.substring(1, result.length());
		}
		return result;
	}

	/**
	 * This method will calculate the Actuals upto december of the current year.
	 */
	@Override
	public String getActualsDecCurr(final String accCode, final String functionId, final String budgetingType) throws Exception {
		final FinancialYearDAO fiscal = CommonsDaoFactory.getDAOFactory().getFinancialYearDAO();
		String startdate = fiscal.getCurrYearStartDate();
		final String temp[] = startdate.split("-");
		final String temp1[] = temp[2].split(" ");
		final Date dt = new Date();
		final Date dt1 = new Date();

		final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(dt1);
		calendar.set(Calendar.YEAR, Integer.parseInt(temp[0]));
		calendar.set(Calendar.MONTH, Integer.parseInt(temp[1]) - 1);
		calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(temp1[0]));
		startdate = formatter.format(calendar.getTime());
		calendar.setTime(dt);
		calendar.set(Calendar.YEAR, Integer.parseInt(temp[0]));
		/*
		 * Here we have hardcoded the month(december) and day(31). Calendar month starts from 0 and hence december will be 11.
		 */
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		final String endDate = formatter.format(calendar.getTime());
		String result = "";
		String hqlQuery = "";
		ArrayList list = new ArrayList();
		/*
		 * Budgeting type is hardcoded here to frame the query. 1 - sum(debitamount) - sum(creditamount) 2 - sum(creditamount) 3 - sum(debitamount) Based on the budgeting type the query will differ.
		 */
		if (!functionId.equalsIgnoreCase("0")) {
			if (budgetingType.equalsIgnoreCase("1")) {
				hqlQuery = "select sum(cgeneralledger.debitAmount)-sum(cgeneralledger.creditAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.voucherHeaderId.id=cvoucherheader.id and cvoucherheader.voucherDate >='"
						+ startdate + "' and cvoucherheader.voucherDate <='" + endDate + "' and cgeneralledger.functionId='" + functionId + "'  and cgeneralledger.glcode like '" + accCode + "'|| '%'";
			} else if (budgetingType.equalsIgnoreCase("2")) {
				hqlQuery = "select sum(cgeneralledger.creditAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.voucherHeaderId.id=cvoucherheader.id and cvoucherheader.voucherDate >='" + startdate
						+ "' and cvoucherheader.voucherDate <='" + endDate + "' and cgeneralledger.functionId='" + functionId + "'  and cgeneralledger.glcode like '" + accCode + "'|| '%'";
			} else if (budgetingType.equalsIgnoreCase("3")) {
				hqlQuery = "select sum(cgeneralledger.debitAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.voucherHeaderId.id=cvoucherheader.id and cvoucherheader.voucherDate >='" + startdate
						+ "' and cvoucherheader.voucherDate <='" + endDate + "' and cgeneralledger.functionId='" + functionId + "'  and cgeneralledger.glcode like '" + accCode + "'|| '%'";
			}
		} else if (functionId.equalsIgnoreCase("0")) {
			if (budgetingType.equalsIgnoreCase("1")) {
				hqlQuery = "select sum(cgeneralledger.debitAmount)-sum(cgeneralledger.creditAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.voucherHeaderId.id=cvoucherheader.id and cvoucherheader.voucherDate >='"
						+ startdate + "' and cvoucherheader.voucherDate <='" + endDate + "' and cgeneralledger.glcode like '" + accCode + "'|| '%'";
			} else if (budgetingType.equalsIgnoreCase("2")) {
				hqlQuery = "select sum(cgeneralledger.creditAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.voucherHeaderId.id=cvoucherheader.id and cvoucherheader.voucherDate >='" + startdate
						+ "' and cvoucherheader.voucherDate <='" + endDate + "' and cgeneralledger.glcode like '" + accCode + "'|| '%'";
			} else if (budgetingType.equalsIgnoreCase("3")) {
				hqlQuery = "select sum(cgeneralledger.debitAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.voucherHeaderId.id=cvoucherheader.id and cvoucherheader.voucherDate >='" + startdate
						+ "' and cvoucherheader.voucherDate <='" + endDate + "' and cgeneralledger.glcode like '" + accCode + "'|| '%'";
			}
		}
		try {
			final Query query = getSession().createQuery(hqlQuery);
			list = (ArrayList) query.list();
		} catch (final Exception e) {
			LOG.error("Error occurred while getting Actuals upto december", e);
			throw new EGOVException("Error occurred while getting Actuals upto december", e);
		}
		if (list.size() > 0) {
			if (list.get(0) == null) {
				return 0.0 + "";
			} else {
				result = list.get(0).toString();
			}
		} else {
			return 0.0 + "";
		}

		if (result.startsWith("-")) {
			result = result.substring(1, result.length());
		}
		return result;
	}

	@Override
	public List<CGeneralLedger> findCGeneralLedgerByVoucherHeaderId(final Long voucherHeaderId) {
		final Query qry = getSession().createQuery("from CGeneralLedger gen where gen.voucherHeaderId.id = :voucherHeaderId");
		qry.setString("voucherHeaderId", voucherHeaderId.toString());
		return qry.list();
	}

	@Override
	public String getCBillDeductionAmtByVhId(final Long voucherHeaderId) {
		final String result = "0";
		final Query qry = getSession().createQuery("select sum(gl.creditAmount) from CGeneralLedger gl where gl.voucherHeaderId.id = :voucherHeaderId " + "and gl.glcodeId not in(select id from CChartOfAccounts where purposeId=28) ");
		qry.setString("voucherHeaderId", voucherHeaderId.toString());
		if (qry.uniqueResult() != null) {
			return qry.uniqueResult().toString();
		} else {
			return result;
		}
	}

	@Override
	public BigDecimal getGlAmountForBudgetingType(final Long budType, final List glcodeList, final String finYearID, final String functionId, final String schemeId, final String subSchemeId, final String asOnDate) throws Exception {
		try {
			Query qry = null;
			final StringBuffer qryStr = new StringBuffer();
			final BigDecimal result = new BigDecimal("0.00");
			if (budType == 1) {
				qryStr.append("select abs(sum(cgeneralledger.debitAmount)-sum(cgeneralledger.creditAmount)) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader ");
			} else if (budType == 2) {
				qryStr.append("select abs(sum(cgeneralledger.creditAmount)) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader ");
			} else if (budType == 3) {
				qryStr.append("select abs(sum(cgeneralledger.debitAmount)) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader ");
			}

			String frmTab = "";
			String whrCond = "";
			String dateCond = "";
			String funcStr = "";
			String schStr = "";
			String subSchStr = "";
			String cond = "";

			cond = " where cgeneralledger.voucherHeaderId.id=cvoucherheader.id and cvoucherheader.fiscalPeriodId in ( select cfiscalperiod.id from CFiscalPeriod cfiscalperiod where cfiscalperiod.financialYearId =:finYearID ) and cgeneralledger.glcode in ( :glcodeList) ";

			if (!(functionId == null || "".equals(functionId))) {
				funcStr = " and cgeneralledger.functionId =:functionId";
			}

			if ((!(schemeId == null)) && (subSchemeId == null || "".equals(subSchemeId))) {
				schStr = "  and vouchermis.schemeid =:schemeId";
				frmTab = " ,Vouchermis vouchermis ";
				whrCond = " and cvoucherheader.id=vouchermis.voucherheaderid ";
			}

			if ((!(schemeId == null || "".equals(schemeId))) && (!(subSchemeId == null || "".equals(subSchemeId)))) {
				schStr = "  and vouchermis.schemeid =:schemeId";
				subSchStr = " and vouchermis.subschemeid =:subSchemeId";
				frmTab = " ,Vouchermis vouchermis ";
				whrCond = " and cvoucherheader.id=vouchermis.voucherheaderid ";
			}

			if (!(asOnDate == null || "".equals(asOnDate))) {
				dateCond = " and cvoucherheader.voucherDate <=:asOnDate";
			}

			qryStr.append(frmTab);
			qryStr.append(cond);
			qryStr.append(whrCond);
			qryStr.append(funcStr);
			qryStr.append(schStr);
			qryStr.append(subSchStr);
			qryStr.append(dateCond);

			qry = getSession().createQuery(qryStr.toString());
			if (!(functionId == "" || functionId == null)) {
				qry.setString("functionId", functionId);
			}
			if ((!(schemeId == "" || schemeId == null)) && (subSchemeId == "" || subSchemeId == null)) {
				qry.setString("schemeId", schemeId);
			}
			if ((!(schemeId == "" || schemeId == null)) && (!(subSchemeId == "" || subSchemeId == null))) {
				qry.setString("schemeId", schemeId);
				qry.setString("subSchemeId", subSchemeId);
			}
			if (!(asOnDate == "" || asOnDate == null)) {
				qry.setString("asOnDate", asOnDate);
			}
			qry.setString("finYearID", finYearID);
			qry.setParameterList("glcodeList", glcodeList);

			if (qry.uniqueResult() != null) {
				return new BigDecimal(qry.uniqueResult().toString());
			} else {
				return result;
			}
		} catch (final Exception e) {
			LOG.error("Error occurred while getting Amount for Budgetting Type", e);
			throw new EGOVException("Error occurred while getting Amount for Budgetting Type", e);
		}
	}

	@Override
	public BigDecimal getGlAmountbyGlcodeList(final List glCodeList, final BigDecimal glAmount) throws Exception {
		BigDecimal amount = glAmount;
		Query qry = null;
		try {
			for (final Iterator i = glCodeList.iterator(); i.hasNext();) {
				final String glCode = (String) i.next();
				qry = getSession().createQuery("from CGeneralLedger gl where gl.glcode =:glCode order by gl.id desc");
				qry.setString("glCode", glCode);
				if (qry.list() != null) {
					final Iterator iterator = qry.iterate();
					if (iterator.hasNext()) {
						CGeneralLedger ob;
						ob = (CGeneralLedger) iterator.next();

						final Double debitamount = ob.getDebitAmount();
						final Double creditamount = ob.getCreditAmount();

						if (!debitamount.equals(0.0)) {
							amount = amount.subtract(new BigDecimal(debitamount.toString()));
						} else {
							amount = amount.subtract(new BigDecimal(creditamount.toString()));
						}
					}
				}

			}

		} catch (final Exception e) {
			LOG.error("Error occurred while getting GL Amount By GLCode", e);
			throw new EGOVException("Error occurred while getting GL Amount By GLCode", e);
		}
		return amount;
	}
}
