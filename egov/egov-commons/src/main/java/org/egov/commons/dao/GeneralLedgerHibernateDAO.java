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
package org.egov.commons.dao;

import org.egov.commons.CGeneralLedger;
import org.egov.infra.exception.ApplicationException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

public class GeneralLedgerHibernateDAO  implements GeneralLedgerDAO {
    
    @Autowired
    private FinancialYearHibernateDAO financialYearHibernateDAO;

    @Autowired
    private FiscalPeriodHibernateDAO fiscalPeriodHibernateDAO;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private static final Logger LOG = LoggerFactory.getLogger(GeneralLedgerHibernateDAO.class);
    
    @Transactional
    public CGeneralLedger update(final CGeneralLedger entity) {
        getCurrentSession().update(entity);
        return entity;
    }

    @Transactional
    public CGeneralLedger create(final CGeneralLedger entity) {
        getCurrentSession().persist(entity);
        return entity;
    }

    @Transactional
    public void delete(CGeneralLedger entity) {
        getCurrentSession().delete(entity);
    }

    public CGeneralLedger findById(Number id, boolean lock) {
        return (CGeneralLedger) getCurrentSession().load(CGeneralLedger.class, id);
    }

    public List<CGeneralLedger> findAll() {
        return (List<CGeneralLedger>) getCurrentSession().createCriteria(CGeneralLedger.class).list();
    }

  
    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    
    /**
     * This method will calculate the Actuals for the previous year.
     */
    @Override
    public String getActualsPrev(final String accCode, final String functionId, final String budgetingType)
            throws Exception {
        final FinancialYearDAO fiscal = financialYearHibernateDAO;
        final String financialperiodId = fiscal.getPrevYearFiscalId();
        final FiscalPeriodDAO fiscalperiod = fiscalPeriodHibernateDAO;
        final String fiscalperiodId = fiscalperiod.getFiscalPeriodIds(financialperiodId);
        String result = "";
        String hqlQuery = "";
        ArrayList list = new ArrayList();
        /*
         * Budgeting type is hardcoded here to frame the query. 1 -
         * sum(debitamount) - sum(creditamount) 2 - sum(creditamount) 3 -
         * sum(debitamount) Based on the budgeting type the query will differ.
         */
        if (!functionId.equalsIgnoreCase("0")) {
            if (budgetingType.equalsIgnoreCase("1")) {
                hqlQuery = "select sum(cgeneralledger.debitAmount)-sum(cgeneralledger.creditAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.voucherHeaderId.id=cvoucherheader.id and cvoucherheader.fiscalPeriodId in ("
                        + fiscalperiodId
                        + ") and cgeneralledger.functionId='"
                        + functionId
                        + "'  and cgeneralledger.glcode like '" + accCode + "'|| '%'";
            } else if (budgetingType.equalsIgnoreCase("2")) {
                hqlQuery = "select sum(cgeneralledger.creditAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.id.voucherHeaderId=cvoucherheader.id and cvoucherheader.fiscalPeriodId in ("
                        + fiscalperiodId
                        + ") and cgeneralledger.functionId='"
                        + functionId
                        + "'  and cgeneralledger.glcode like '" + accCode + "'|| '%'";
            } else if (budgetingType.equalsIgnoreCase("3")) {
                hqlQuery = "select sum(cgeneralledger.debitAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.id.voucherHeaderId=cvoucherheader.id and cvoucherheader.fiscalPeriodId in ("
                        + fiscalperiodId
                        + ") and cgeneralledger.functionId='"
                        + functionId
                        + "'  and cgeneralledger.glcode like '" + accCode + "'|| '%'";
            }
        } else if (functionId.equalsIgnoreCase("0")) {
            if (budgetingType.equalsIgnoreCase("1")) {
                hqlQuery = "select sum(cgeneralledger.debitAmount)-sum(cgeneralledger.creditAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.voucherHeaderId.id=cvoucherheader.id and cvoucherheader.fiscalPeriodId in ("
                        + fiscalperiodId + ") and cgeneralledger.glcode like '" + accCode + "'|| '%'";
            } else if (budgetingType.equalsIgnoreCase("2")) {
                hqlQuery = "select sum(cgeneralledger.creditAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.voucherHeaderId.id=cvoucherheader.id and cvoucherheader.fiscalPeriodId in ("
                        + fiscalperiodId + ") and cgeneralledger.glcode like '" + accCode + "'|| '%'";
            } else if (budgetingType.equalsIgnoreCase("3")) {
                hqlQuery = "select sum(cgeneralledger.debitAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.voucherHeaderId.id=cvoucherheader.id and cvoucherheader.fiscalPeriodId in ("
                        + fiscalperiodId + ")  and cgeneralledger.glcode like '" + accCode + "'|| '%'";
            }
        }
        try {
            final Query query = getCurrentSession().createQuery(hqlQuery);
            list = (ArrayList) query.list();
        } catch (final Exception e) {
            LOG.error("Error occurred while getting Actuals Prev", e);
            throw new ApplicationException("Error occurred while getting Actuals Prev", e);
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
    public String getActualsDecCurr(final String accCode, final String functionId, final String budgetingType)
            throws Exception {
        final FinancialYearDAO fiscal = financialYearHibernateDAO;
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
         * Here we have hardcoded the month(december) and day(31). Calendar
         * month starts from 0 and hence december will be 11.
         */
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        final String endDate = formatter.format(calendar.getTime());
        String result = "";
        String hqlQuery = "";
        ArrayList list = new ArrayList();
        /*
         * Budgeting type is hardcoded here to frame the query. 1 -
         * sum(debitamount) - sum(creditamount) 2 - sum(creditamount) 3 -
         * sum(debitamount) Based on the budgeting type the query will differ.
         */
        if (!functionId.equalsIgnoreCase("0")) {
            if (budgetingType.equalsIgnoreCase("1")) {
                hqlQuery = "select sum(cgeneralledger.debitAmount)-sum(cgeneralledger.creditAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.voucherHeaderId.id=cvoucherheader.id and cvoucherheader.voucherDate >='"
                        + startdate
                        + "' and cvoucherheader.voucherDate <='"
                        + endDate
                        + "' and cgeneralledger.functionId='"
                        + functionId
                        + "'  and cgeneralledger.glcode like '"
                        + accCode + "'|| '%'";
            } else if (budgetingType.equalsIgnoreCase("2")) {
                hqlQuery = "select sum(cgeneralledger.creditAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.voucherHeaderId.id=cvoucherheader.id and cvoucherheader.voucherDate >='"
                        + startdate
                        + "' and cvoucherheader.voucherDate <='"
                        + endDate
                        + "' and cgeneralledger.functionId='"
                        + functionId
                        + "'  and cgeneralledger.glcode like '"
                        + accCode + "'|| '%'";
            } else if (budgetingType.equalsIgnoreCase("3")) {
                hqlQuery = "select sum(cgeneralledger.debitAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.voucherHeaderId.id=cvoucherheader.id and cvoucherheader.voucherDate >='"
                        + startdate
                        + "' and cvoucherheader.voucherDate <='"
                        + endDate
                        + "' and cgeneralledger.functionId='"
                        + functionId
                        + "'  and cgeneralledger.glcode like '"
                        + accCode + "'|| '%'";
            }
        } else if (functionId.equalsIgnoreCase("0")) {
            if (budgetingType.equalsIgnoreCase("1")) {
                hqlQuery = "select sum(cgeneralledger.debitAmount)-sum(cgeneralledger.creditAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.voucherHeaderId.id=cvoucherheader.id and cvoucherheader.voucherDate >='"
                        + startdate
                        + "' and cvoucherheader.voucherDate <='"
                        + endDate
                        + "' and cgeneralledger.glcode like '" + accCode + "'|| '%'";
            } else if (budgetingType.equalsIgnoreCase("2")) {
                hqlQuery = "select sum(cgeneralledger.creditAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.voucherHeaderId.id=cvoucherheader.id and cvoucherheader.voucherDate >='"
                        + startdate
                        + "' and cvoucherheader.voucherDate <='"
                        + endDate
                        + "' and cgeneralledger.glcode like '" + accCode + "'|| '%'";
            } else if (budgetingType.equalsIgnoreCase("3")) {
                hqlQuery = "select sum(cgeneralledger.debitAmount) from CGeneralLedger cgeneralledger,CVoucherHeader cvoucherheader where cgeneralledger.voucherHeaderId.id=cvoucherheader.id and cvoucherheader.voucherDate >='"
                        + startdate
                        + "' and cvoucherheader.voucherDate <='"
                        + endDate
                        + "' and cgeneralledger.glcode like '" + accCode + "'|| '%'";
            }
        }
        try {
            final Query query = getCurrentSession().createQuery(hqlQuery);
            list = (ArrayList) query.list();
        } catch (final Exception e) {
            LOG.error("getActualsDecCurr Exception", e.getMessage());
            throw new ApplicationException("Error occurred while getting Actuals upto december", e);
        }
        if (list.size() > 0) {
            if (list.get(0) == null) {
                return "0.0";
            } else {
                result = list.get(0).toString();
            }
        } else {
            return "0.0";
        }

        if (result.startsWith("-")) {
            result = result.substring(1, result.length());
        }
        return result;
    }

    @Override
    public List<CGeneralLedger> findCGeneralLedgerByVoucherHeaderId(final Long voucherHeaderId) {
        final Query qry = getCurrentSession().createQuery(
                "from CGeneralLedger gen where gen.voucherHeaderId.id = :voucherHeaderId");
        qry.setString("voucherHeaderId", voucherHeaderId.toString());
        return qry.list();
    }

    @Override
    public String getCBillDeductionAmtByVhId(final Long voucherHeaderId) {
        final String result = "0";
        final Query qry = getCurrentSession().createQuery(
                "select sum(gl.creditAmount) from CGeneralLedger gl where gl.voucherHeaderId.id = :voucherHeaderId "
                        + "and gl.glcodeId not in(select id from CChartOfAccounts where purposeId=28) ");
        qry.setString("voucherHeaderId", voucherHeaderId.toString());
        if (qry.uniqueResult() != null) {
            return qry.uniqueResult().toString();
        } else {
            return result;
        }
    }

    @Override
    public BigDecimal getGlAmountForBudgetingType(final Long budType, final List glcodeList, final String finYearID,
            final String functionId, final String schemeId, final String subSchemeId, final String asOnDate)
            throws Exception {
        try {
            Query qry = null;
            final StringBuffer qryStr = new StringBuffer(1000);
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

            if ((schemeId != null) && (subSchemeId == null || subSchemeId.isEmpty())) {
                schStr = "  and vouchermis.schemeid =:schemeId";
                frmTab = " ,Vouchermis vouchermis ";
                whrCond = " and cvoucherheader.id=vouchermis.voucherheaderid ";
            }

            if ((!(schemeId == null || schemeId.isEmpty())) && !(subSchemeId == null || (subSchemeId).isEmpty())) {
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

            qry = getCurrentSession().createQuery(qryStr.toString());
            if (!(functionId == null ||functionId.equals(""))) {
                qry.setString("functionId", functionId);
            }
            if (!(schemeId == null || schemeId .equals("")) && (subSchemeId == null || subSchemeId .equals(""))) {
                qry.setString("schemeId", schemeId);
            }
            if (!(schemeId == null || schemeId .equals("")) && !(subSchemeId == null) || subSchemeId .equals("")) {
                qry.setString("schemeId", schemeId);
                qry.setString("subSchemeId", subSchemeId);
            }
            if (!(asOnDate == null || asOnDate .equals(""))) {
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
            LOG.error("Error occurred getGlAmountForBudgetingType ", e.getMessage());
            throw new ApplicationException("Error occurred while getting Amount for Budgetting Type", e);
        }
    }

    @Override
    public BigDecimal getGlAmountbyGlcodeList(final List glCodeList, final BigDecimal glAmount) throws Exception {
        BigDecimal amount = glAmount;
        Query qry = null;
      
        try {
            for (final Iterator i = glCodeList.iterator(); i.hasNext();) {
                final String glCode = (String) i.next();
                qry = getCurrentSession().createQuery(
                        "from CGeneralLedger gl where gl.glcode =:glCode order by gl.id desc");
                qry.setString("glCode", glCode);
                if (qry.list() != null) {
                    final Iterator iterator = qry.iterate();
                    if (iterator.hasNext()) {
                        CGeneralLedger ob;
                        ob = (CGeneralLedger) iterator.next();

                        final BigDecimal debitamount = BigDecimal.valueOf(ob.getDebitAmount());
                        final BigDecimal creditamount = BigDecimal.valueOf(ob.getCreditAmount());
                        
                        if (!debitamount.equals(0.0)) {
                            amount = amount.subtract(debitamount);
                        } else {
                            amount = amount.subtract(creditamount);
                        }
                    }
                }

            }

        } catch (final Exception e) {
            LOG.error("Error occurred while getGlAmountbyGlcodeList ", e.getMessage());
            throw new ApplicationException("Error occurred while getting GL Amount By GLCode", e);
        }
        return amount;
    }
}