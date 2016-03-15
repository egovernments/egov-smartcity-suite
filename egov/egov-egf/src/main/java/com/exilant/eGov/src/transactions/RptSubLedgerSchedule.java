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
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
/*
 * Created on Oct 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.transactions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.utils.EntityType;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.exilant.GLEngine.GeneralLedgerBean;
import com.exilant.exility.common.TaskFailedException;

/**
 * @author Administrator TODO To change the template for this generated type
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */
@Service
public class RptSubLedgerSchedule {
    double totalDr, totalCr, totalOpgBal, totalClosingBal;
    List<Object[]> resultset;
    NumberFormat formatter;
    TaskFailedException taskExc;
    String glCode, accEntityId, fundId, fyId, deptId;
    private CFinancialYear fyObj;
    String subLedgerTable;
    HashMap hm_opBal;
    LinkedList dataList;
    private static final Logger LOGGER = Logger.getLogger(RptSubLedgerSchedule.class);
    private boolean isStartDateFirstApril = false;
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    private FinancialYearHibernateDAO financialYearDAO;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    Query pst;

    public RptSubLedgerSchedule() {
    }

    // code for SubLedger type
    public LinkedList getSubLedgerTypeSchedule(final GeneralLedgerBean reportBean) throws TaskFailedException {
        formatter = new DecimalFormat();
        formatter = new DecimalFormat("###############.00");
        glCode = reportBean.getGlcode();
        fundId = reportBean.getFund_id();
        deptId = reportBean.getDeptId();
        accEntityId = reportBean.getAccEntityId();

        reportBean.setAccEntityId(accEntityId);
        String startDate = "";
        String endDate = "";
        String formstartDate = "";
        String formendDate = "";
        String startDateDBFormat = "";

        final SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        final SimpleDateFormat formatter1 = new SimpleDateFormat("dd/mm/yyyy");
        Date dt = new Date();
        try {
            endDate = reportBean.getEndDate();
            dt = sdf.parse(endDate);
            formendDate = formatter1.format(dt);

            startDate = reportBean.getStartDate();
            if (!startDate.equalsIgnoreCase("null")) {
                dt = sdf.parse(startDate);
                formstartDate = formatter1.format(dt);
            }
        } catch (final Exception e) {
            LOGGER.error("Parse Error" + e);
        }
        startDateDBFormat = formstartDate;
        endDate = formendDate;

        fyObj = financialYearDAO.getFinYearByDate(dt);
        fyId = fyObj.getId().toString();
        // fyObj=cms.getFinancialYearById(Long.parseLong(fyId));

        final Date finYrStartingDate = fyObj.getStartingDate();
        final String formatedDateStr = formatter1.format(finYrStartingDate);
        if (LOGGER.isInfoEnabled())
            LOGGER.info(".............The formated date is " + formatedDateStr);
        if (startDateDBFormat.equalsIgnoreCase(formatedDateStr))
            isStartDateFirstApril = true;
        try {
            getSubQuery(startDateDBFormat, endDate);
            formatSLTypeReport();
            reportBean.setAccName(getAccountname(glCode));
        } catch (final Exception exception) {
            LOGGER.error("Exp=" + exception.getMessage());
        }
        return dataList;
    }

    private void getSubQuery(final String startDate, final String endDate) throws Exception {
        String defaultStatusExclude = null;
        String departmentFromCondition = "";
        String departmentWhereCondition = "";
        String departmentConditionTran = "";
        dataList = new LinkedList();
        totalCr = 0.0;
        totalDr = 0.0;
        totalOpgBal = 0.0;
        totalClosingBal = 0.0;
        if (deptId != null && !deptId.equalsIgnoreCase("")) {
            departmentConditionTran = " and DEPARTMENTID=? ";
            departmentFromCondition = ",vouchermis vmis";
            departmentWhereCondition = "AND vh.id = vmis.voucherheaderid and vmis.departmentid=? ";
        }
        final List<AppConfigValues> listAppConfVal = appConfigValuesService.getConfigValuesByModuleAndKey("EGF",
                "statusexcludeReport");
        if (null != listAppConfVal)
            defaultStatusExclude = listAppConfVal.get(0).getValue();
        else
            throw new ApplicationRuntimeException("Exlcude statusses not  are not defined for Reports");

        String query;// DEFINED STUFF startDate endDate fundId
        // defaultStatusExclude glCode accEntityId
        if (isStartDateFirstApril) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Inside the First April block");
            query = "SELECT case when a1.detkeyid is null then a3.detkeyid ELSE a1.detkeyid end as slid,coalesce(a3.OpgCreditBal,0) as OpgCreditBal,coalesce(a3.OpgDebitBal,0)  as OpgDebitBal,0 as PrevDebit, "
                    + " 0 as PrevCredit,coalesce(a1.Credit,0) as Credit,coalesce(a1.Debit,0) as Debit "
                    + " FROM ( select detkeyid,sum(Debit)as Debit,sum(Credit)as Credit FROM ("
                    + " SELECT gld.detailkeyid AS detkeyid, SUM (gld.amount)  AS Debit , 0 AS Credit "
                    + " FROM generalledgerdetail gld, generalledger gl,voucherheader vh "
                    + departmentFromCondition
                    + " "
                    + " WHERE gld.detailtypeid  = ? AND gld.generalledgerid = gl .ID AND gl.glcodeid=(SELECT ID FROM chartofaccounts WHERE glcode = ?) "
                    + " AND gl.debitamount > 0  AND gl.voucherheaderid = vh .ID AND vh.voucherdate >= to_date(?,'dd/mm/yyyy') "
                    + " AND vh.voucherdate <= to_date(?,'dd/mm/yyyy')"
                    + departmentWhereCondition
                    + " AND vh.fundid= ? AND vh.status NOT IN ("
                    + defaultStatusExclude
                    + ") GROUP BY gld.detailkeyid "
                    + " UNION ALL "
                    + " SELECT gld.detailkeyid AS detkeyid,0 AS Debit, SUM (gld.amount)  AS Credit "
                    + " FROM generalledgerdetail gld, generalledger gl,voucherheader vh "
                    + departmentFromCondition
                    + " "
                    + " WHERE gld.detailtypeid  = ? AND gld.generalledgerid = gl .ID AND gl.glcodeid=(SELECT ID FROM chartofaccounts WHERE glcode = ?) "
                    + " AND gl.creditamount > 0  AND gl.voucherheaderid = vh .ID AND vh.voucherdate >= to_date(?,'dd/mm/yyyy') "
                    + " AND vh.voucherdate <= to_date(?,'dd/mm/yyyy')"
                    + departmentWhereCondition
                    + " AND vh.fundid= ? AND vh.status NOT IN ("
                    + defaultStatusExclude
                    + ") GROUP BY gld.detailkeyid "
                    + ") a2 GROUP BY detkeyid ORDER BY detkeyid "
                    + ") a1 FULL OUTER JOIN  "
                    + "(SELECT ACCOUNTDETAILKEY    AS detkeyid , SUM(openingcreditbalance) AS OpgCreditBal , SUM(openingdebitbalance)  AS OpgDebitBal "
                    + "FROM transactionsummary WHERE glcodeid=(SELECT ID FROM chartofaccounts WHERE glcode = ?) AND (openingcreditbalance > 0 OR openingdebitbalance > 0) "
                    + "AND accountdetailtypeid= ? AND fundid= ? AND financialyearid= ? "
                    + departmentConditionTran
                    + " GROUP BY ACCOUNTDETAILKEY ORDER BY accountdetailkey) a3 ON a1.detkeyid=a3.detkeyid";

            int i = 0;
            pst = HibernateUtil.getCurrentSession().createSQLQuery(query);
            pst.setLong(i++, Long.parseLong(accEntityId));
            pst.setString(i++, glCode);
            pst.setString(i++, startDate);
            pst.setString(i++, endDate);
            if (deptId != null && !deptId.equalsIgnoreCase(""))
                pst.setLong(i++, Long.parseLong(deptId));
            pst.setLong(i++, Long.parseLong(fundId));
            pst.setLong(i++, Long.parseLong(accEntityId));
            pst.setString(i++, glCode);
            pst.setString(i++, startDate);
            pst.setString(i++, endDate);
            if (deptId != null && !deptId.equalsIgnoreCase(""))
                pst.setLong(i++, Long.parseLong(deptId));
            pst.setLong(i++, Long.parseLong(fundId));

            pst.setString(i++, glCode);
            pst.setLong(i++, Long.parseLong(accEntityId));
            pst.setLong(i++, Long.parseLong(fundId));
            pst.setLong(i++, Long.parseLong(fyId));
            if (deptId != null && !deptId.equalsIgnoreCase(""))
                pst.setLong(i++, Long.parseLong(deptId));
        } else {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Inside the Else block");
            query = "SELECT case when a1.detkeyid is null then a3.detkeyid else a1.detkeyid end as slid,coalesce(a3.OpgCreditBal,0) as OpgCreditBal,coalesce(a3.OpgDebitBal,0)  as OpgDebitBal,coalesce(a2.PrevDebit,0) as PrevDebit, "
                    + "coalesce(a2.PrevCredit,0) as PrevCredit,coalesce(a1.Credit,0) as Credit,coalesce(a1.Debit,0) as Debit"
                    + " FROM  (select detkeyid,sum(Debit)as Debit,sum(Credit)as Credit FROM("
                    + " SELECT gld.detailkeyid AS detkeyid,SUM (gld.amount)  AS Debit , 0 AS Credit "
                    + " FROM generalledgerdetail gld, generalledger gl,voucherheader vh "
                    + departmentFromCondition
                    + " WHERE gld.detailtypeid  = ? AND gld.generalledgerid = gl .ID AND gl.glcodeid=(SELECT ID FROM chartofaccounts WHERE glcode = ?) "
                    + "AND gl.debitamount > 0 AND gl.voucherheaderid = vh .ID AND vh.voucherdate >= to_date(?,'dd/mm/yyyy') AND vh.voucherdate <= to_date(?,'dd/mm/yyyy') "
                    + departmentWhereCondition
                    + " AND vh.fundid= ? AND vh.status NOT IN ("
                    + defaultStatusExclude
                    + ") GROUP BY gld.detailkeyid "
                    + " UNION ALL"
                    + " SELECT gld.detailkeyid AS detkeyid,0 AS Debit,SUM (gld.amount)  AS Credit"
                    + " FROM generalledgerdetail gld, generalledger gl,voucherheader vh "
                    + departmentFromCondition
                    + " WHERE gld.detailtypeid  = ? AND gld.generalledgerid = gl .ID AND gl.glcodeid=(SELECT ID FROM chartofaccounts WHERE glcode = ?) "
                    + "AND gl.creditamount > 0 AND gl.voucherheaderid = vh .ID AND vh.voucherdate >= to_date(?,'dd/mm/yyyy') AND vh.voucherdate <= to_date(?,'dd/mm/yyyy') "
                    + departmentWhereCondition
                    + " AND vh.fundid= ? AND vh.status NOT IN ("
                    + defaultStatusExclude
                    + ") GROUP BY gld.detailkeyid "
                    + " ) a4 group by  detkeyid "
                    + "ORDER BY detkeyid ) a1 "
                    + "FULL OUTER JOIN ("
                    + " SELECT detkeyid AS detkeyid , SUM (PrevDebit )  AS PrevDebit , SUM (PrevCredit) AS PrevCredit From("
                    + " SELECT gld.detailkeyid AS detkeyid ,coalesce( SUM (gld.amount ),0)  AS PrevDebit , 0 AS PrevCredit "
                    + " FROM generalledgerdetail gld, generalledger gl, voucherheader vh "
                    + departmentFromCondition
                    + " WHERE gld.detailtypeid  = ? "
                    + "AND gld.generalledgerid = gl.ID AND gl.glcodeid=(SELECT ID FROM chartofaccounts WHERE glcode = ?) AND gl.debitamount > 0 "
                    + "AND gl.voucherheaderid = vh .ID AND vh.voucherdate >=(SELECT startingdate FROM financialyear WHERE startingdate <= to_date(?,'dd/mm/yyyy') "
                    + "AND endingdate >= to_date(?,'dd/mm/yyyy')  AND vh.voucherdate <= to_date(?,'dd/mm/yyyy')-1 )  "
                    + departmentWhereCondition
                    + "AND vh.fundid = ? AND vh.status NOT  IN ("
                    + defaultStatusExclude
                    + ") GROUP BY gld.detailkeyid "
                    + " UNION ALL"
                    + " SELECT gld.detailkeyid AS detkeyid ,0 AS PrevDebit, coalesce(SUM (gld.amount ),0) AS PrevCredit"
                    + " FROM generalledgerdetail gld, generalledger gl, voucherheader vh "
                    + departmentFromCondition
                    + " WHERE gld.detailtypeid  = ? "
                    + "AND gld.generalledgerid = gl.ID AND gl.glcodeid=(SELECT ID FROM chartofaccounts WHERE glcode = ?) AND gl.creditamount> 0 "
                    + "AND gl.voucherheaderid = vh .ID AND vh.voucherdate >=(SELECT startingdate FROM financialyear WHERE startingdate <= to_date(?,'dd/mm/yyyy') "
                    + "AND endingdate >= to_date(?,'dd/mm/yyyy') AND vh.voucherdate <= to_date(?,'dd/mm/yyyy')-1 )"
                    + departmentWhereCondition
                    + "AND vh.fundid = ? AND vh.status NOT  IN ("
                    + defaultStatusExclude
                    + ") GROUP BY gld.detailkeyid "
                    + " ) a1 group by detkeyid"
                    + " ORDER BY detkeyid ) a2 ON a1.detkeyid=a2.detkeyid "
                    + "FULL OUTER JOIN (SELECT ACCOUNTDETAILKEY    AS detkeyid , SUM(openingcreditbalance) AS OpgCreditBal , SUM(openingdebitbalance)  AS OpgDebitBal "
                    + "FROM transactionsummary WHERE glcodeid=(SELECT ID FROM chartofaccounts WHERE glcode = ?) AND (openingcreditbalance > 0 OR openingdebitbalance > 0) "
                    + "AND accountdetailtypeid= ? AND fundid= ? AND financialyearid= ? "
                    + departmentConditionTran
                    + " GROUP BY ACCOUNTDETAILKEY ORDER BY accountdetailkey) a3 ON a2.detkeyid=a3.detkeyid";

            int i = 0;
            pst = HibernateUtil.getCurrentSession().createSQLQuery(query);
            pst.setLong(i++, Long.parseLong(accEntityId));
            pst.setString(i++, glCode);
            pst.setString(i++, startDate);
            pst.setString(i++, endDate);
            if (deptId != null && !deptId.equalsIgnoreCase(""))
                pst.setLong(i++, Long.parseLong(deptId));
            pst.setLong(i++, Long.parseLong(fundId));

            pst.setLong(i++, Long.parseLong(accEntityId));
            pst.setString(i++, glCode);
            pst.setString(i++, startDate);
            pst.setString(i++, endDate);
            if (deptId != null && !deptId.equalsIgnoreCase(""))
                pst.setLong(i++, Long.parseLong(deptId));
            pst.setLong(i++, Long.parseLong(fundId));

            pst.setLong(i++, Long.parseLong(accEntityId));
            pst.setString(i++, glCode);
            pst.setString(i++, startDate);
            pst.setString(i++, endDate);
            pst.setString(i++, startDate);
            if (deptId != null && !deptId.equalsIgnoreCase(""))
                pst.setLong(i++, Long.parseLong(deptId));
            pst.setLong(i++, Long.parseLong(fundId));

            pst.setLong(i++, Long.parseLong(accEntityId));
            pst.setString(i++, glCode);
            pst.setString(i++, startDate);
            pst.setString(i++, endDate);
            pst.setString(i++, startDate);
            if (deptId != null && !deptId.equalsIgnoreCase(""))
                pst.setLong(i++, Long.parseLong(deptId));
            pst.setLong(i++, Long.parseLong(fundId));

            pst.setString(i++, glCode);
            pst.setLong(i++, Long.parseLong(accEntityId));
            pst.setLong(i++, Long.parseLong(fundId));
            pst.setLong(i++, Long.parseLong(fyId));
            if (deptId != null && !deptId.equalsIgnoreCase(""))
                pst.setLong(i++, Long.parseLong(deptId));
        }

        if (LOGGER.isInfoEnabled())
            LOGGER.info("QUERY..." + query);
        try {
            GeneralLedgerBean gb = null;
            resultset = pst.list();
            // final PersistenceService ps = new PersistenceService();
            final Accountdetailtype accountdetailtype = (Accountdetailtype) persistenceService.find(
                    " from Accountdetailtype where id=?", Integer.valueOf(accEntityId));
            EntityType entity;
            for (final Object[] element : resultset) {
                gb = new GeneralLedgerBean();
                double openingBal = 0.0;
                double closingBal = 0.0;
                double opgCredit = 0.0;
                double opgDebit = 0.0;
                double prevDebit = 0.0;
                double prevCredit = 0.0;
                double debitamount = 0.0;
                double creditamount = 0.0;
                try {
                    entity = (EntityType) persistenceService.find(" from " + accountdetailtype.getFullQualifiedName()
                            + " where id=? ", Long.valueOf(element[0].toString()));
                } catch (final Exception ee) {
                    LOGGER.error(ee.getMessage(), ee);
                    entity = (EntityType) persistenceService.find(" from " + accountdetailtype.getFullQualifiedName()
                            + " where id=? ", Long.valueOf(element[0].toString()));
                }
                if (entity != null) {
                    gb.setCode(entity.getCode());
                    gb.setName(entity.getName());
                } else {
                    gb.setCode("");
                    gb.setName("");
                }
                gb.setAccEntityKey(element[0].toString());
                if (element[5].toString() != null)
                    creditamount = Double.parseDouble(element[5].toString());
                if (element[6].toString() != null)
                    debitamount = Double.parseDouble(element[6].toString());
                if (element[1].toString() != null)
                    opgCredit = Double.parseDouble(element[1].toString());
                if (element[2].toString() != null)
                    opgDebit = Double.parseDouble(element[2].toString());
                if (element[3].toString() != null)
                    prevDebit = Double.parseDouble(element[3].toString());
                if (element[4].toString() != null)
                    prevCredit = Double.parseDouble(element[4].toString());

                openingBal = opgCredit + prevCredit - (opgDebit + prevDebit);
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Hello............. " + openingBal + "==");
                if (openingBal > 0) {
                    gb.setOpeningBal("" + numberToString(((Double) openingBal).toString()) + " Cr");
                    totalOpgBal = totalOpgBal + openingBal;
                } else if (openingBal < 0) {
                    totalOpgBal = totalOpgBal + openingBal;
                    final double openingBal1 = openingBal * -1;
                    gb.setOpeningBal("" + numberToString(((Double) openingBal1).toString()) + " Dr");
                } else
                    gb.setOpeningBal("&nbsp;");

                closingBal = openingBal + creditamount - debitamount;
                if (closingBal > 0)
                    gb.setClosingBal("" + numberToString(((Double) closingBal).toString()) + " Cr");
                else if (closingBal < 0) {
                    final double closingBal1 = closingBal * -1;
                    gb.setClosingBal("" + numberToString(((Double) closingBal1).toString()) + " Dr");
                } else
                    gb.setClosingBal("&nbsp;");

                if (debitamount > 0) {
                    gb.setDebitamount("" + numberToString(((Double) debitamount).toString()));
                    totalDr = totalDr + debitamount;
                } else
                    gb.setDebitamount("&nbsp;");
                if (creditamount > 0) {
                    gb.setCreditamount("" + numberToString(((Double) creditamount).toString()));
                    totalCr = totalCr + creditamount;
                } else
                    gb.setCreditamount("&nbsp;");
                gb.setAccEntityId(accEntityId);
                totalClosingBal = totalOpgBal + totalCr - totalDr;
                dataList.add(gb);
            }
        } catch (final Exception e) {
            LOGGER.error("Error in getReport==" + e.getMessage());
            throw new Exception();
        }
    }

    private void formatSLTypeReport() {
        final GeneralLedgerBean gb = new GeneralLedgerBean();
        gb.setAccEntityKey("");
        gb.setCode("<hr noshade color=black size=1><b>Total:<hr noshade color=black size=1></b>");
        gb.setName("");

        if (totalOpgBal > 0)
            gb.setOpeningBal("<hr noshade color=black size=1><b>" + numberToString(((Double) totalOpgBal).toString())
                    + " Cr<hr noshade color=black size=1></b>");
        else if (totalOpgBal < 0) {
            totalOpgBal = totalOpgBal * -1;
            gb.setOpeningBal("<hr noshade color=black size=1><b>" + numberToString(((Double) totalOpgBal).toString())
                    + " Dr<hr noshade color=black size=1></b>");
        } else if (totalOpgBal == 0.0)
            gb.setOpeningBal("");
        if (totalClosingBal > 0)
            gb.setClosingBal("<hr noshade color=black size=1><b>"
                    + numberToString(((Double) totalClosingBal).toString()) + " Cr<hr noshade color=black size=1></b>");
        else if (totalClosingBal < 0) {
            totalClosingBal = totalClosingBal * -1;
            gb.setClosingBal("<hr noshade color=black size=1><b>"
                    + numberToString(((Double) totalClosingBal).toString()) + " Dr<hr noshade color=black size=1></b>");
        } else if (totalClosingBal == 0.0)
            gb.setClosingBal("");
        gb.setDebitamount("<hr noshade color=black size=1><b>" + numberToString(((Double) totalDr).toString())
                + "<hr noshade color=black size=1></b>");
        gb.setCreditamount("<hr noshade color=black size=1><b>" + numberToString(((Double) totalCr).toString())
                + "<hr noshade color=black size=1></b>");

        dataList.add(gb);
    }

    private String getAccountname(final String glCode) {
        String accName = "";
        try {
            final String query = "select name from chartofaccounts where glCode= ?";
            pst = HibernateUtil.getCurrentSession().createSQLQuery(query);
            pst.setString(0, glCode);
            /*
             * final List<Object[]> rset = pst.list(); for (final Object[]
             * element : rset) accName = element[0].toString();
             */

            /*
             * final Query query =
             * HibernateUtil.getCurrentSession().createSQLQuery(
             * "select name from chartofaccounts where glCode=" + glCode);
             */
            final List list = pst.list();
            if (list.get(0) != null)
                accName = list.get(0).toString();

        } catch (final Exception sqlex) {
            LOGGER.error("Exp=" + sqlex.getMessage(), sqlex);
            return null;
        }
        return accName;
    }

    public static StringBuffer numberToString(final String strNumberToConvert) {
        String strNumber = "", signBit = "";
        if (strNumberToConvert.startsWith("-")) {
            strNumber = "" + strNumberToConvert.substring(1, strNumberToConvert.length());
            signBit = "-";
        } else
            strNumber = "" + strNumberToConvert;
        final DecimalFormat dft = new DecimalFormat("##############0.00");
        final String strtemp = "" + dft.format(Double.parseDouble(strNumber));
        StringBuffer strbNumber = new StringBuffer(strtemp);
        final int intLen = strbNumber.length();

        for (int i = intLen - 6; i > 0; i = i - 2)
            strbNumber.insert(i, ',');
        if (signBit.equals("-"))
            strbNumber = strbNumber.insert(0, "-");
        return strbNumber;
    }

}