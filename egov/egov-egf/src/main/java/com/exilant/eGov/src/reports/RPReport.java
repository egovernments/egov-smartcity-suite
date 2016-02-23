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
/*
 * Created on Jan 11, 2008
 * @author Sathish P
 */
package com.exilant.eGov.src.reports;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.transactions.CommonMethodsImpl;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.dataservice.DatabaseConnectionException;

public class RPReport
{
    private final static String errConnOpenString = "Unable to get a connection from Pool Please make sure that the connection pool is set up properly";
    Statement statement;
    List<Object[]> resultset;
    TaskFailedException taskExc;
    Connection conn;
    String effFilter;
    public String reqFundId[];
    public String reqFundName[];
    CommnFunctions cf = new CommnFunctions();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat dtformatter = new SimpleDateFormat("dd-MMM-yyyy");
    java.util.Date dt = new java.util.Date();
    public String startDate = "", endDate = "", prevStartDate = "", prevEndDate = "", fundId = "", obStartDate = "",
            obEndDate = "", period = "", prevObEndDate;
    EGovernCommon egc = new EGovernCommon();
    CommonMethodsImpl cm = new CommonMethodsImpl();
    List col1 = new ArrayList();
    List col2 = new ArrayList();
    List fundList = new ArrayList();
    ArrayList prevYrAmtList = new ArrayList();
    BigDecimal totalPrevRcpt = new BigDecimal("0.00");
    BigDecimal totalPrevPymnt = new BigDecimal("0.00");
    private static final Logger LOGGER = Logger.getLogger(RPReport.class);
    String[] cashCode = null;
    String cashPId = EGovConfig.getProperty("egf_config.xml", "PURPOSEID", "", "CashInHand");
    int minorCodeLength = Integer.parseInt(EGovConfig.getProperty("egf_config.xml", "minorcodevalue", "", "AccountCode"));
    int maxCodeLength = Integer.parseInt(EGovConfig.getProperty("egf_config.xml", "majorcodevalue", "", "AccountCode"));
    String month = "";

    public ArrayList getRPReport(final ReceiptPaymentBean reportBean) throws TaskFailedException
    {
        col1 = new ArrayList();
        col2 = new ArrayList();
        fundList = new ArrayList();
        reqFundName = null;
        reqFundId = null;
        // reqFundId
        totalPrevRcpt = new BigDecimal("0.00");
        totalPrevPymnt = new BigDecimal("0.00");
        period = reportBean.getPeriod();
        month = reportBean.getMonth();
        fundId = reportBean.getFundId();
        final String year = reportBean.getFinYear();
        final String amountIn = reportBean.getRupees();
        final String toDate = reportBean.getToDate();

        try
        {
            cashCode = cm.getCodeName(cashPId).split("#");
        } catch (final Exception exception)
        {
            throw new DatabaseConnectionException(errConnOpenString, exception);
        }
        final int finId = Integer.parseInt(year);
        int nextYear;
        startDate = cf.getStartDate(finId);
        obStartDate = startDate;
        nextYear = Integer.parseInt(startDate.split("/")[2]) + 1;
        prevStartDate = startDate.split("/")[0] + "/" + startDate.split("/")[1] + "/"
                + (Integer.parseInt(startDate.split("/")[2]) - 1);
        if (period.equals("1"))
        {
            if (toDate != null)
                endDate = toDate;
            else
                endDate = cf.getEndDate(finId);
            prevEndDate = endDate.split("/")[0] + "/" + endDate.split("/")[1] + "/"
                    + (Integer.parseInt(endDate.split("/")[2]) - 1);
        }
        else if (period.equals("2"))
        {
            endDate = EGovConfig.getProperty("egf_config.xml", "HalfYear", "", "ReportDate");
            endDate = endDate.split("/")[0] + "/" + endDate.split("/")[1] + "/" + startDate.split("/")[2];
            prevEndDate = endDate.split("/")[0] + "/" + endDate.split("/")[1] + "/"
                    + (Integer.parseInt(endDate.split("/")[2]) - 1);
        }
        else if (period.equals("3"))
        {
            final Calendar c = Calendar.getInstance();
            if (Integer.parseInt(month) > 3)
            {
                startDate = "01/" + month + "/" + startDate.split("/")[2];
                c.set(Calendar.MONTH, Integer.parseInt(month) - 1);
                endDate = c.getActualMaximum(Calendar.DAY_OF_MONTH) + "/" + month + "/" + startDate.split("/")[2];
            }
            else
            {
                startDate = "01/" + month + "/" + nextYear;
                c.set(Calendar.MONTH, Integer.parseInt(month) - 1);
                endDate = c.getActualMaximum(Calendar.DAY_OF_MONTH) + "/" + month + "/" + nextYear;
            }
            prevStartDate = obStartDate.split("/")[0] + "/" + obStartDate.split("/")[1] + "/"
                    + (Integer.parseInt(obStartDate.split("/")[2]) - 1) + "";
            prevEndDate = endDate.split("/")[0] + "/" + endDate.split("/")[1] + "/"
                    + (Integer.parseInt(endDate.split("/")[2]) - 1) + "";
        }
        if (LOGGER.isInfoEnabled())
            LOGGER.info("satrtDate:" + startDate + " endDate:" + endDate + " prevStartDate:" + prevStartDate + " prevEndDate:"
                    + prevEndDate);

        if (isCurDate(endDate) == -1)
        {
            endDate = egc.getCurrentDate();
            if (period.equals("3"))
                if (Integer.parseInt(month) > 3)
                    prevEndDate = endDate.split("/")[0] + "/" + month + "/" + (Integer.parseInt(endDate.split("/")[2]) - 1) + "";
                else
                {
                    endDate = endDate.split("/")[0] + "/" + month + "/" + nextYear;
                    prevEndDate = endDate.split("/")[0] + "/" + month + "/" + (Integer.parseInt(endDate.split("/")[2]) - 1) + "";
                }
            endDate = egc.getCurrentDate();
        }
        try
        {
            if (StringUtils.isNotEmpty(reportBean.getScheduleNo()))
                return (ArrayList) getRPSchedule(amountIn, reportBean.getScheduleNo());
            getFundList(dtformatter.format(sdf.parse(prevStartDate)), dtformatter.format(sdf.parse(prevEndDate)));
            // if(LOGGER.isDebugEnabled()) LOGGER.debug("reqFundId length==============="+reqFundId.length);
            getPrevYearReport(null, dtformatter.format(sdf.parse(prevStartDate)), dtformatter.format(sdf.parse(prevEndDate)),
                    cashCode, maxCodeLength, minorCodeLength, amountIn, reqFundId);

            getFundList(dtformatter.format(sdf.parse(startDate)), dtformatter.format(sdf.parse(endDate)));
            // if(LOGGER.isDebugEnabled()) LOGGER.debug("reqFundId length second ==============="+reqFundId.length);
            final CallableStatement cstmt = conn.prepareCall("{? = call EGF_REPORT.RCPPYMNT(?, ?, ?, ?)}");
            cstmt.setFetchSize(1000);
            // This fix is for Phoenix Migration.
            // cstmt.registerOutParameter(1,OracleTypes.CURSOR);
            cstmt.setString(1, dtformatter.format(sdf.parse(startDate)));
            cstmt.setString(2, dtformatter.format(sdf.parse(endDate)));
            cstmt.setString(3, cashCode[0].substring(0, maxCodeLength));
            cstmt.setInt(4, minorCodeLength);
            cstmt.executeQuery();
            final ResultSet rs = (ResultSet) cstmt.getObject(1);
            HashMap data = new HashMap();
            String rcptQuery = "select distinct RECEIPTSCHEDULEID from chartofaccounts where glcode=?";
            if (LOGGER.isInfoEnabled())
                LOGGER.info("rcptQuery-->" + rcptQuery);
            Query psmt = HibernateUtil.getCurrentSession().createSQLQuery(rcptQuery);
            psmt.setString(1, cashCode[0].substring(0, minorCodeLength));
            List<Object[]> rsRcpt = psmt.list();
            String cashOBScheduleId = null;
            String cashOBScheduleNo = "";
            String cashOBScheduleName = "";
            for (final Object[] element : rsRcpt)
                cashOBScheduleId = element[0].toString();

            BigDecimal rowTotal = new BigDecimal("0.00");
            if (cashOBScheduleId != null)
            {
                final String schQuery = "select schedule, schedulename from schedulemapping where id= ?";
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("schQuery-->" + schQuery);
                final Query psmt1 = HibernateUtil.getCurrentSession().createSQLQuery(schQuery);
                psmt1.setString(1, cashOBScheduleId);
                final List<Object[]> rsSch = psmt1.list();

                for (final Object[] element : rsSch) {
                    cashOBScheduleNo = element[0].toString();
                    cashOBScheduleName = element[1].toString();
                }
                data = new HashMap();
                data.put("accountCode", cashCode[0].substring(0, maxCodeLength));
                data.put("schedulename", cashOBScheduleName);
                data.put("scheduleNo", cashOBScheduleNo);
                for (final String element2 : reqFundId)
                    data.put(element2, "&nbsp;");
                data.put("rowTotal", "&nbsp;");

                final HashMap opBal = new HashMap();
                if (period.equalsIgnoreCase("3"))
                {
                    final String tDate = startDate;
                    // String obMonth=tDate.split("/")[1];
                    final String obdate = obStartDate;
                    final Calendar c = Calendar.getInstance();
                    if (Integer.parseInt(month) >= 2 && Integer.parseInt(month) <= 3)
                    {
                        c.set(Calendar.MONTH, Integer.parseInt(tDate.split("/")[1]) - 2);
                        obEndDate = c.getActualMaximum(Calendar.DAY_OF_MONTH) + "/" + (Integer.parseInt(tDate.split("/")[1]) - 1)
                                + "/" + (Integer.parseInt(obdate.split("/")[2]) + 1);
                    }
                    else if (Integer.parseInt(month) == 1)
                        obEndDate = "31/12/" + Integer.parseInt(obdate.split("/")[2]);
                    else if (Integer.parseInt(month) == 4)
                        obEndDate = obStartDate;
                    else
                    {
                        c.set(Calendar.MONTH, Integer.parseInt(tDate.split("/")[1]) - 2);
                        obEndDate = c.getActualMaximum(Calendar.DAY_OF_MONTH) + "/" + (Integer.parseInt(tDate.split("/")[1]) - 1)
                                + "/" + Integer.parseInt(obdate.split("/")[2]);
                    }
                    prevObEndDate = obEndDate.split("/")[0] + "/" + obEndDate.split("/")[1] + "/"
                            + (Integer.parseInt(obEndDate.split("/")[2]) - 1) + "";
                    final String snapShotDateTime = egc.getCurDateTime();
                    effFilter = egc.getEffectiveDateFilter(snapShotDateTime);
                    dt = sdf.parse(obEndDate);
                    obEndDate = dtformatter.format(dt);
                    dt = sdf.parse(obStartDate);
                    obStartDate = dtformatter.format(dt);
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("R-01 obStartDate:" + obStartDate + " obEndDate:" + obEndDate);
                    getOpeningBalanceForCash("", "A", "", cashCode[0].substring(0, maxCodeLength),
                            Integer.toString(minorCodeLength), obStartDate, obEndDate, 1, reqFundId, opBal);
                }
                else
                {
                    final Date dt = sdf.parse(startDate);
                    startDate = dtformatter.format(dt);
                    cf.getOpeningBalance("", "A", "", Integer.toString(minorCodeLength), startDate, startDate, 1, reqFundId,
                            opBal);
                }
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("opbal:" + opBal);
                final String query1 = "select distinct rowconcat('select distinct glcode from chartofaccounts coa1,schedulemapping sm1 where "
                        + " coa1.RECEIPTSCHEDULEID='|| sm.ID || ' or coa1.paymentscheduleid='|| sm.ID)  AS \"accCode\",sm.id as \"schId\" from"
                        + " schedulemapping sm,chartofaccounts coa where reporttype='RP' AND (coa.receiptscheduleid = sm.ID"
                        + " OR coa.paymentscheduleid = sm.ID) AND sm.ID =? GROUP BY sm.schedule, sm.ID";
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("query1:" + query1);
                final HashMap amount = new HashMap();
                String cashAccCode = null;
                try
                {
                    final Query pst = HibernateUtil.getCurrentSession().createSQLQuery(query1);
                    pst.setString(0, cashOBScheduleId);
                    final List<Object[]> rs1 = pst.list();
                    for (final Object[] element : rs1) {
                        cashAccCode = element[0].toString();
                        final StringTokenizer accList = new StringTokenizer(element[0].toString(), ",");
                        while (accList.hasMoreTokens())
                        {
                            final String accntCode = accList.nextToken();
                            if (opBal.containsKey(accntCode))
                            {
                                final HashMap opBal1 = (HashMap) opBal.get(accntCode);
                                for (final String element2 : reqFundId)
                                    if (amount.containsKey(element2))
                                    {
                                        final BigDecimal amt = new BigDecimal(amount.get(element2).toString())
                                                .add(new BigDecimal(
                                                        opBal1.get(element2).toString()));
                                        amount.put(element2, amt);
                                    }
                                    else
                                        amount.put(element2, opBal1.get(element2));
                            }// end if
                        }// end while
                    }// end while
                } catch (final Exception e)
                {
                    LOGGER.error("Error in getOBForCashAndBank");
                    throw taskExc;
                }
                rowTotal = new BigDecimal("0.00");
                for (final String element : reqFundId)
                    if (amount.containsKey(element))
                    {
                        rowTotal = rowTotal.add(new BigDecimal(amount.get(element).toString()));
                        data.put(element, cf.formatAmt(amount.get(element).toString(), amountIn));
                    }
                    else
                        data.put(element, "0.00");
                data.put("rowTotal", cf.formatAmt(rowTotal.toString(), amountIn));
                final StringTokenizer accList = new StringTokenizer(cashAccCode, ",");
                final HashMap prevOpBal = new HashMap();
                if (period.equalsIgnoreCase("3"))
                    // cf.getOpeningBalance(conn,"","A","",Integer.toString(minorCodeLength),dtformatter.format(sdf.parse(startDate)),dtformatter.format(sdf.parse(startDate)),1,reqFundId,prevOpBal);
                    getOpeningBalanceForCash("", "A", "", cashCode[0].substring(0, maxCodeLength),
                            Integer.toString(minorCodeLength), dtformatter.format(sdf.parse(prevStartDate)),
                            dtformatter.format(sdf.parse(prevObEndDate)), 1, reqFundId, prevOpBal);
                else
                    cf.getOpeningBalance("", "A", "", Integer.toString(minorCodeLength),
                            dtformatter.format(sdf.parse(prevStartDate)), dtformatter.format(sdf.parse(prevStartDate)), 1,
                            reqFundId, prevOpBal);
                BigDecimal prevCashBal = new BigDecimal("0.00");
                while (accList.hasMoreTokens())
                {
                    final String accntCode = accList.nextToken();
                    if (prevOpBal.containsKey(accntCode))
                    {
                        final HashMap opBal1 = (HashMap) prevOpBal.get(accntCode);
                        for (final String element : reqFundId)
                            prevCashBal = prevCashBal.add(new BigDecimal(opBal1.get(element).toString()));
                    }
                }// end while
                data.put("prevYrAmt", cf.formatAmt(prevCashBal.toString(), amountIn));
                totalPrevRcpt = totalPrevRcpt.add(new BigDecimal(cf.formatAmt(prevCashBal.toString(), amountIn)));
                col1.add(data);
            }

            data = new HashMap();
            data.put("accountCode", "&nbsp;");
            data.put("schedulename", "<b>Operating Receipts</b>");
            data.put("scheduleNo", "&nbsp;");
            for (final String element2 : reqFundId)
                data.put(element2, "&nbsp;");
            data.put("rowTotal", "&nbsp;");
            data.put("prevYrAmt", "&nbsp;");
            col1.add(data);

            data = new HashMap();
            data.put("accountCode", "&nbsp;");
            data.put("schedulename", "&nbsp;<br>&nbsp;");
            data.put("scheduleNo", "&nbsp;");
            for (final String element2 : reqFundId)
                data.put(element2, "&nbsp;");
            data.put("rowTotal", "&nbsp;");
            data.put("prevYrAmt", "&nbsp;");
            col2.add(data);

            data = new HashMap();
            data.put("accountCode", "&nbsp;");
            data.put("schedulename", "<b>Operating Payments</b>");
            data.put("scheduleNo", "&nbsp;");
            for (final String element2 : reqFundId)
                data.put(element2, "&nbsp;");
            data.put("rowTotal", "&nbsp;");
            data.put("prevYrAmt", "&nbsp;");
            col2.add(data);
            String prevRcptSubHead = "";
            String prevPymntSubHead = "";
            while (rs.next())
            {
                final String[] fund = new String[reqFundName.length];
                String rcptAccountCode = "";
                String rcptScheduleNo = "";
                String rcptSchedulename = "";
                String rcptScheduleId = "";
                String pymntAccountCode = "";
                String pymntScheduleNo = "";
                String pymntSchedulename = "";
                String pymntScheduleId = "";
                if (rs.getString("repsubtype").equalsIgnoreCase("ROP"))
                {
                    data = new HashMap();
                    if (rs.getString("receiptscheduleid") != null)
                        rcptScheduleId = rs.getString("receiptscheduleid");
                    if (rs.getString("schedule") != null)
                        rcptScheduleNo = rs.getString("schedule");
                    if (rs.getString("schedulename") != null)
                        rcptSchedulename = rs.getString("schedulename");
                    rcptQuery = "select distinct glcode from chartofaccounts where RECEIPTSCHEDULEID=?";
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("rcptQuery-->" + rcptQuery);
                    final Query psmt2 = HibernateUtil.getCurrentSession().createSQLQuery(rcptQuery);
                    psmt2.setString(0, rcptScheduleId);
                    rsRcpt = psmt2.list();
                    for (final Object[] element : rsRcpt)
                        rcptAccountCode = element[0].toString().substring(0, maxCodeLength);

                    data.put("accountCode", rcptAccountCode);
                    data.put("schedulename", rcptSchedulename);
                    data.put("scheduleNo", rcptScheduleNo);
                    rowTotal = new BigDecimal(0);
                    for (int i = 0; i < reqFundId.length; i++)
                    {
                        if (rs.getString(i + 5) != null)
                            fund[i] = rs.getString(i + 5);
                        else
                            fund[i] = "0";
                        fund[i] = cf.formatAmt(fund[i], amountIn);
                        rowTotal = rowTotal.add(new BigDecimal(fund[i]));
                        data.put(reqFundId[i], fund[i]);
                    }
                    data.put("rowTotal", rowTotal.toString());

                    final Iterator itr = prevYrAmtList.iterator();
                    while (itr.hasNext())
                    {
                        final HashMap hm = (HashMap) itr.next();
                        final Set keySet = hm.keySet();
                        final Iterator itr1 = keySet.iterator();
                        while (itr1.hasNext())
                        {
                            final String str = (String) itr1.next();
                            if (str.equals(rcptScheduleId))
                            {
                                data.put("prevYrAmt", hm.get(rcptScheduleId));
                                totalPrevRcpt = totalPrevRcpt.add(new BigDecimal(hm.get(rcptScheduleId).toString()));
                            }
                        }
                    }
                    col1.add(data);
                }
                else if (rs.getString("repsubtype").equalsIgnoreCase("RNOP"))
                {
                    if (rs.getString("receiptscheduleid") != null)
                        rcptScheduleId = rs.getString("receiptscheduleid");
                    if (rs.getString("schedule") != null)
                        rcptScheduleNo = rs.getString("schedule");
                    if (rs.getString("schedulename") != null)
                        rcptSchedulename = rs.getString("schedulename");
                    rcptQuery = "select distinct glcode from chartofaccounts where RECEIPTSCHEDULEID=?";
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("rcptQuery-->" + rcptQuery);
                    final Query psmt2 = HibernateUtil.getCurrentSession().createSQLQuery(rcptQuery);
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("rcptQuery-->" + rcptQuery);
                    psmt2.setString(0, rcptScheduleId);
                    rsRcpt = psmt2.list();
                    for (final Object[] element : rsRcpt)
                        rcptAccountCode = element[0].toString().substring(0, maxCodeLength);

                    if (!prevRcptSubHead.equals("Non-Operating Receipts"))
                    {
                        data = new HashMap();
                        data.put("accountCode", "&nbsp;");
                        data.put("schedulename", "<b>Non-Operating Receipts</b>");
                        data.put("scheduleNo", "&nbsp;");
                        for (final String element : reqFundId)
                            data.put(element, "&nbsp;");
                        data.put("rowTotal", "&nbsp;");
                        data.put("prevYrAmt", "&nbsp;");
                        col1.add(data);
                        prevRcptSubHead = "Non-Operating Receipts";
                    }

                    data = new HashMap();
                    data.put("accountCode", rcptAccountCode);
                    data.put("schedulename", rcptSchedulename);
                    data.put("scheduleNo", rcptScheduleNo);
                    rowTotal = new BigDecimal(0);
                    for (int i = 0; i < reqFundId.length; i++)
                    {
                        if (rs.getString(i + 5) != null)
                            fund[i] = rs.getString(i + 5);
                        else
                            fund[i] = "0";
                        fund[i] = cf.formatAmt(fund[i], amountIn);
                        rowTotal = rowTotal.add(new BigDecimal(fund[i]));
                        data.put(reqFundId[i], fund[i]);
                    }
                    data.put("rowTotal", rowTotal.toString());

                    final Iterator itr = prevYrAmtList.iterator();
                    while (itr.hasNext())
                    {
                        final HashMap hm = (HashMap) itr.next();
                        final Set keySet = hm.keySet();
                        final Iterator itr1 = keySet.iterator();
                        while (itr1.hasNext())
                        {
                            final String str = (String) itr1.next();
                            if (str.equals(rcptScheduleId))
                            {
                                data.put("prevYrAmt", hm.get(rcptScheduleId));
                                totalPrevRcpt = totalPrevRcpt.add(new BigDecimal(hm.get(rcptScheduleId).toString()));
                            }
                        }
                    }
                    col1.add(data);
                }

                else if (rs.getString("repsubtype").equalsIgnoreCase("POP"))
                {
                    data = new HashMap();
                    if (rs.getString("paymentscheduleid") != null)
                        pymntScheduleId = rs.getString("paymentscheduleid");
                    final String pymntQuery = "select distinct glcode from chartofaccounts where PAYMENTSCHEDULEID=?";
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("pymntQuery-->" + pymntQuery);
                    psmt = HibernateUtil.getCurrentSession().createSQLQuery(pymntQuery);
                    psmt.setString(0, pymntScheduleId);
                    final List<Object[]> rsPymnt = psmt.list();
                    for (final Object[] element : rsPymnt)
                        pymntAccountCode = element[0].toString().substring(0, maxCodeLength);
                    final String remissionQuery = "select isremission from schedulemapping where id=?";
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("remissionQuery-->" + remissionQuery);
                    psmt = HibernateUtil.getCurrentSession().createSQLQuery(remissionQuery);
                    psmt.setString(0, pymntScheduleId);
                    final List<Object[]> rsRemission = psmt.list();
                    for (final Object[] element : rsRemission)
                        if (element[0].toString().equals("1"))
                            pymntAccountCode = "*";

                    if (rs.getString("schedule") != null)
                        pymntScheduleNo = rs.getString("schedule");
                    if (rs.getString("schedulename") != null)
                        pymntSchedulename = rs.getString("schedulename");
                    data.put("schedulename", pymntSchedulename);
                    data.put("scheduleNo", pymntScheduleNo);
                    data.put("accountCode", pymntAccountCode);
                    rowTotal = new BigDecimal("0.00");
                    for (int i = 0; i < reqFundId.length; i++)
                    {
                        if (rs.getString(reqFundId.length + i + 5) != null)
                            fund[i] = rs.getString(reqFundId.length + i + 5);
                        else
                            fund[i] = "0";
                        fund[i] = cf.formatAmt(fund[i], amountIn);
                        rowTotal = rowTotal.add(new BigDecimal(fund[i]));
                        data.put(reqFundId[i], fund[i]);
                    }
                    data.put("rowTotal", rowTotal.toString());

                    final Iterator itr = prevYrAmtList.iterator();
                    while (itr.hasNext())
                    {
                        final HashMap hm = (HashMap) itr.next();
                        final Set keySet = hm.keySet();
                        final Iterator itr1 = keySet.iterator();
                        while (itr1.hasNext())
                        {
                            final String str = (String) itr1.next();
                            if (str.equals(pymntScheduleId))
                            {
                                data.put("prevYrAmt", hm.get(pymntScheduleId));
                                totalPrevPymnt = totalPrevPymnt.add(new BigDecimal(hm.get(pymntScheduleId).toString()));
                            }
                        }
                    }
                    col2.add(data);
                }
                else if (rs.getString("repsubtype").equalsIgnoreCase("PNOP"))
                {
                    if (rs.getString("paymentscheduleid") != null)
                        pymntScheduleId = rs.getString("paymentscheduleid");
                    final String pymntQuery = "select distinct glcode from chartofaccounts where PAYMENTSCHEDULEID=?";
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("pymntQuery-->" + pymntQuery);
                    final Query psmt1 = HibernateUtil.getCurrentSession().createSQLQuery(pymntQuery);
                    psmt1.setString(0, pymntScheduleId);
                    final List<Object[]> rsPymnt = psmt1.list();
                    for (final Object[] element : rsPymnt)
                        pymntAccountCode = element[0].toString().substring(0, maxCodeLength);

                    if (!prevPymntSubHead.equals("Non-Operating Payments"))
                    {
                        data = new HashMap();
                        data.put("accountCode", "&nbsp;");
                        data.put("schedulename", "<b>Non-Operating Payments</b>");
                        data.put("scheduleNo", "&nbsp;");
                        for (final String element : reqFundId)
                            data.put(element, "&nbsp;");
                        data.put("rowTotal", "&nbsp;");
                        data.put("prevYrAmt", "&nbsp;");
                        col2.add(data);
                        prevPymntSubHead = "Non-Operating Payments";
                    }
                    data = new HashMap();
                    if (rs.getString("schedule") != null)
                        pymntScheduleNo = rs.getString("schedule");
                    if (rs.getString("schedulename") != null)
                        pymntSchedulename = rs.getString("schedulename");
                    data.put("schedulename", pymntSchedulename);
                    data.put("scheduleNo", pymntScheduleNo);
                    data.put("accountCode", pymntAccountCode);
                    rowTotal = new BigDecimal(0);
                    for (int i = 0; i < reqFundId.length; i++)
                    {
                        if (rs.getString(reqFundId.length + i + 5) != null)
                            fund[i] = rs.getString(reqFundId.length + i + 5);
                        else
                            fund[i] = "0";
                        fund[i] = cf.formatAmt(fund[i], amountIn);
                        rowTotal = rowTotal.add(new BigDecimal(fund[i]));
                        data.put(reqFundId[i], fund[i]);
                    }
                    data.put("rowTotal", rowTotal.toString());

                    final Iterator itr = prevYrAmtList.iterator();
                    while (itr.hasNext())
                    {
                        final HashMap hm = (HashMap) itr.next();
                        final Set keySet = hm.keySet();
                        final Iterator itr1 = keySet.iterator();
                        while (itr1.hasNext())
                        {
                            final String str = (String) itr1.next();
                            if (str.equals(pymntScheduleId))
                            {
                                data.put("prevYrAmt", hm.get(pymntScheduleId));
                                totalPrevPymnt = totalPrevPymnt.add(new BigDecimal(hm.get(pymntScheduleId).toString()));
                            }
                        }
                    }
                    col2.add(data);
                }
            }

            for (final String element : reqFundName) {
                data = new HashMap();
                data.put("fund", element);
                fundList.add(data);
            }
            for (final String element : reqFundId) {
                data = new HashMap();
                data.put("fundId", element);
                fundList.add(data);
            }

            data = new HashMap();
            data.put("accountCode", "*");
            data.put("schedulename", "Other Receipts");
            data.put("scheduleNo", "&nbsp;");

            final HashMap otherRcptAmt = new HashMap();
            if (period.equals("3"))
                startDate = dtformatter.format(sdf.parse(startDate));
            getOtherReceiptsOrPayments("Receipt", cashCode[0].substring(0, maxCodeLength), Integer.toString(minorCodeLength),
                    startDate, dtformatter.format(sdf.parse(endDate)), otherRcptAmt);
            rowTotal = new BigDecimal(0);
            for (final String element : reqFundId)
                if (otherRcptAmt.containsKey(element))
                {
                    data.put(element, cf.formatAmt(otherRcptAmt.get(element).toString(), amountIn));
                    rowTotal = rowTotal.add(new BigDecimal(otherRcptAmt.get(element).toString()));
                } else
                    data.put(element, "0.00");
            data.put("rowTotal", cf.formatAmt(rowTotal.toString(), amountIn));

            final HashMap prevOtherRcptAmt = new HashMap();
            getOtherReceiptsOrPayments("Receipt", cashCode[0].substring(0, maxCodeLength), Integer.toString(minorCodeLength),
                    dtformatter.format(sdf.parse(prevStartDate)), dtformatter.format(sdf.parse(prevEndDate)), prevOtherRcptAmt);
            rowTotal = new BigDecimal(0);
            for (final String element : reqFundId)
                if (prevOtherRcptAmt.containsKey(element))
                    rowTotal = rowTotal.add(new BigDecimal(prevOtherRcptAmt.get(element).toString()));
            data.put("prevYrAmt", cf.formatAmt(rowTotal.toString(), amountIn));
            totalPrevRcpt = totalPrevRcpt.add(new BigDecimal(cf.formatAmt(rowTotal.toString(), amountIn)));
            col1.add(data);

            data = new HashMap();
            data.put("accountCode", "*");
            data.put("schedulename", "Other Payments");
            data.put("scheduleNo", "&nbsp;");

            final HashMap otherPymntAmt = new HashMap();
            getOtherReceiptsOrPayments("Payment", cashCode[0].substring(0, maxCodeLength), Integer.toString(minorCodeLength),
                    startDate, dtformatter.format(sdf.parse(endDate)), otherPymntAmt);
            rowTotal = new BigDecimal(0);
            for (final String element : reqFundId)
                if (otherPymntAmt.containsKey(element))
                {
                    data.put(element, cf.formatAmt(otherPymntAmt.get(element).toString(), amountIn));
                    rowTotal = rowTotal.add(new BigDecimal(otherPymntAmt.get(element).toString()));
                } else
                    data.put(element, "0.00");
            data.put("rowTotal", cf.formatAmt(rowTotal.toString(), amountIn));
            final HashMap prevOtherPymntAmt = new HashMap();
            getOtherReceiptsOrPayments("Payment", cashCode[0].substring(0, maxCodeLength), Integer.toString(minorCodeLength),
                    dtformatter.format(sdf.parse(prevStartDate)), dtformatter.format(sdf.parse(prevEndDate)), prevOtherPymntAmt);
            rowTotal = new BigDecimal(0);
            for (final String element : reqFundId)
                if (prevOtherPymntAmt.containsKey(element))
                    rowTotal = rowTotal.add(new BigDecimal(prevOtherPymntAmt.get(element).toString()));
            data.put("prevYrAmt", cf.formatAmt(rowTotal.toString(), amountIn));
            totalPrevPymnt = totalPrevPymnt.add(new BigDecimal(cf.formatAmt(rowTotal.toString(), amountIn)));
            col2.add(data);
        } catch (final Exception e)
        {
            LOGGER.error("Error in getReceiptPaymentReport");
            throw taskExc;
        }
        final HashMap totalRcpt = new HashMap();
        Iterator itr = col1.iterator();
        while (itr.hasNext())
        {
            final HashMap hm = (HashMap) itr.next();
            final Set keySet = hm.keySet();
            final Iterator itr1 = keySet.iterator();
            while (itr1.hasNext())
            {
                final String str = (String) itr1.next();
                for (int i = 0; i < reqFundId.length; i++)
                    if (totalRcpt.containsKey(reqFundId[i]))
                    {
                        BigDecimal temp = new BigDecimal(0);
                        if (!hm.get(reqFundId[i]).equals("&nbsp;") && str.equals(reqFundId[i]))
                            temp = new BigDecimal(totalRcpt.get(reqFundId[i]).toString()).add(new BigDecimal(hm.get(reqFundId[i])
                                    .toString()));
                        else
                            temp = new BigDecimal(totalRcpt.get(reqFundId[i]).toString());
                        totalRcpt.put(reqFundId[i], temp);
                    } else if (str.equals(reqFundId[i]) && !hm.get(reqFundId[i]).equals("&nbsp;"))
                        totalRcpt.put(reqFundId[i], hm.get(reqFundId[i]));
            }// end while
        }// end while

        final HashMap totalPymnt = new HashMap();
        itr = col2.iterator();
        while (itr.hasNext())
        {
            final HashMap hm = (HashMap) itr.next();
            final Set keySet = hm.keySet();
            final Iterator itr1 = keySet.iterator();
            while (itr1.hasNext())
            {
                final String str = (String) itr1.next();
                for (int i = 0; i < reqFundId.length; i++)
                    if (totalPymnt.containsKey(reqFundId[i]))
                    {
                        BigDecimal temp = new BigDecimal(0);
                        if (!hm.get(reqFundId[i]).equals("&nbsp;") && str.equals(reqFundId[i]))
                            temp = new BigDecimal(totalPymnt.get(reqFundId[i]).toString()).add(new BigDecimal(hm
                                    .get(reqFundId[i]).toString()));
                        else
                            temp = new BigDecimal(totalPymnt.get(reqFundId[i]).toString());
                        totalPymnt.put(reqFundId[i], temp);
                    } else if (str.equals(reqFundId[i]) && !hm.get(reqFundId[i]).equals("&nbsp;"))
                        totalPymnt.put(reqFundId[i], hm.get(reqFundId[i]));
            }// end while
        }// end while

        HashMap data = new HashMap();
        data.put("accountCode", "&nbsp;");
        data.put("schedulename", "<b>Total Payments</b>");
        data.put("scheduleNo", "&nbsp;");
        BigDecimal rowTotal = new BigDecimal(0);
        for (final String element : reqFundId)
            if (totalPymnt.containsKey(element))
            {
                data.put(element, "<b>" + totalPymnt.get(element).toString() + "</b>");
                rowTotal = rowTotal.add(new BigDecimal(totalPymnt.get(element).toString()));
            }
        data.put("rowTotal", "<b>" + rowTotal.toString() + "</b>");
        data.put("prevYrAmt", "<b>" + totalPrevPymnt.toString() + "</b>");
        col2.add(data);

        data = new HashMap();
        data.put("accountCode", "&nbsp;");
        data.put("schedulename", "<b>Closing Cash & Bank</b>");
        data.put("scheduleNo", "R-37");
        rowTotal = new BigDecimal(0);
        for (final String element : reqFundId)
            if (totalRcpt.containsKey(element) && totalPymnt.containsKey(element))
            {
                data.put(
                        element,
                        "<b>"
                                + new BigDecimal(totalRcpt.get(element).toString()).subtract(
                                        new BigDecimal(totalPymnt.get(element).toString())).toString() + "</b>");
                rowTotal = rowTotal.add(new BigDecimal(totalRcpt.get(element).toString()).subtract(new BigDecimal(totalPymnt
                        .get(element).toString())));
            }
        data.put("rowTotal", "<b>" + rowTotal.toString() + "</b>");
        data.put("prevYrAmt", "<b>" + totalPrevRcpt.subtract(totalPrevPymnt).toString() + "</b>");
        col2.add(data);
        data = new HashMap();
        data.put("accountCode", "&nbsp;");
        data.put("schedulename", "<b>GRAND TOTAL</b>");
        data.put("scheduleNo", "&nbsp;");
        rowTotal = new BigDecimal(0);
        for (final String element : reqFundId)
            if (totalRcpt.containsKey(element))
            {
                data.put(element, "<b>" + totalRcpt.get(element).toString() + "</b>");
                rowTotal = rowTotal.add(new BigDecimal(totalRcpt.get(element).toString()));
            }
        data.put("rowTotal", "<b>" + rowTotal.toString() + "</b>");
        data.put("prevYrAmt", "<b>" + totalPrevRcpt.toString() + "</b>");
        col1.add(data);

        data = new HashMap();
        data.put("accountCode", "&nbsp;");
        data.put("schedulename", "<b>GRAND TOTAL</b>");
        data.put("scheduleNo", "&nbsp;");
        rowTotal = new BigDecimal(0);
        for (final String element : reqFundId)
            if (totalPymnt.containsKey(element))
            {
                data.put(element, "<b>" + totalRcpt.get(element).toString() + "</b>");
                rowTotal = rowTotal.add(new BigDecimal(totalRcpt.get(element).toString()));
            }
        data.put("rowTotal", "<b>" + rowTotal.toString() + "</b>");
        data.put("prevYrAmt", "<b>" + totalPrevRcpt.toString() + "</b>");
        col2.add(data);

        final ArrayList rpList = (ArrayList) col1;
        itr = col2.iterator();
        while (itr.hasNext())
        {
            final HashMap hm = (HashMap) itr.next();
            rpList.add(hm);
        }
        reportBean.setPrevPeriodStartDate(prevStartDate);
        reportBean.setPrevPeriodEndDate(prevEndDate);
        return rpList;
    }

    // check whether report date is less than toda'y date if so return 1 else -1
    // if equal 0
    public int isCurDate(final String VDate) throws TaskFailedException
    {
        int ret;
        try
        {
            final String today = egc.getCurrentDate();
            final String[] dt2 = today.split("/");
            final String[] dt1 = VDate.split("/");

            ret = Integer.parseInt(dt2[2]) > Integer.parseInt(dt1[2]) ? 1
                    : Integer.parseInt(dt2[2]) < Integer.parseInt(dt1[2]) ? -1
                            : Integer.parseInt(dt2[1]) > Integer
                            .parseInt(dt1[1]) ? 1 : Integer
                                    .parseInt(dt2[1]) < Integer
                                    .parseInt(dt1[1]) ? -1 : Integer
                                            .parseInt(dt2[0]) > Integer
                                            .parseInt(dt1[0]) ? 1 : Integer
                                                    .parseInt(dt2[0]) < Integer
                                                    .parseInt(dt1[0]) ? -1 : 0;

        } catch (final Exception ex)
        {
            LOGGER.error("Exception in isCurDate " + ex.getMessage());
            throw new TaskFailedException("Date Should be within the today's date");
        }
        return ret;
    }

    public void getFundList(final String fromDate, final String toDate) throws SQLException
    {
        Query psmt = null;
        try
        {
            // String query = "select distinct id,name from fund where id in (select fundid from voucherheader)";

            final String query = " SELECT distinct id,name,code FROM FUND WHERE id IN (SELECT fundid FROM VOUCHERHEADER WHERE voucherdate>=? AND voucherdate  <=?  AND status<>4 ) order by code ";

            final Date parse = dtformatter.parse(fromDate);
            new java.sql.Date(parse.getTime());
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Voucher Date" + fromDate);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Voucher Date" + toDate);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("getFundList: " + query);
            psmt = HibernateUtil.getCurrentSession().createSQLQuery(query);
            psmt.setString(0, fromDate);
            psmt.setString(1, toDate);
            // psmt.setString(3,fundId);
            final List<Object[]> resultset = psmt.list();

            int resSize = 0, i = 0;
            if (fundId == null) {
                resSize = resultset.size();
                reqFundId = new String[resSize];
                reqFundName = new String[resSize];
                for (final Object[] element : resultset) {
                    reqFundId[i] = element[0].toString();
                    reqFundName[i] = element[1].toString();
                    i += 1;
                }
            } else {
                reqFundId = new String[1];
                reqFundName = new String[1];
                for (final Object[] element : resultset)
                    if (element[0].toString().equals(fundId)) {
                        reqFundId[i] = element[0].toString();
                        reqFundName[i] = element[1].toString();
                        // i += 1;
                    }
            }
        } catch (final Exception e)
        {
            LOGGER.error("Error in getting fund list" + e.getMessage());
            throw new SQLException();
        }
    }

    public void getOpeningBalanceForCash(final String fundId, final String type1, final String type2, final String cashCode,
            final String substringVal,
            final String startDate, final String endDate, final int classification, final String reqFundId1[],
            final HashMap openingBal) throws Exception
    {
        String fundCondition = "", tillDateOB = "";
        Query psmt = null;
        if (!fundId.equalsIgnoreCase(""))
        {
            System.out.println("Helllo m in>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            fundCondition = "AND f.Id=?";
        }
        if (!startDate.equalsIgnoreCase(endDate))
            tillDateOB = " union select substr(gl.glcode,1," + substringVal
            + ") glcode,vh.fundid fundid, sum(debitamount)-sum(creditamount) amount"
            + " from generalledger gl, chartofaccounts coa, voucherheader vh where vh.id = gl.voucherheaderid "
            + " and vh.status<>4 and vh.VOUCHERDATE >=? and vh.VOUCHERDATE<=?" + fundCondition + " and "
            + " substr(gl.glcode,1," + substringVal + ")=coa.glcode and substr(gl.glcode,1," + substringVal + ") like '"
            + cashCode + "%' group by substr(gl.glcode,1," + substringVal + "),vh.fundid ";
        String glcode = "", fuId = "";
        final String query = "SELECT glcode as \"glcode\", fundid as \"fundid\", SUM(amount) as \"amount\" FROM (SELECT substr(coa.glcode,1,"
                + substringVal
                + ") glcode,ts.fundid fundid ,"
                + " case when coa.type ='"
                + type2
                + "'  then sum(ts.openingcreditbalance)-sum(ts.openingdebitbalance) else sum(ts.openingdebitbalance)-sum(ts.openingcreditbalance) end amount "
                + " FROM transactionsummary ts,  chartofaccounts coa,fund  f   WHERE (coa.TYPE = '"
                + type1
                + "' OR coa.TYPE = '"
                + type2
                + "') and coa.id = ts.glcodeid "
                + " AND financialyearid =(SELECT ID FROM financialyear WHERE startingdate <= ? AND endingdate >= ?)  "
                + fundCondition
                + " and f.id=ts.fundid and f.isactive=true and f.isnotleaf!=true "
                + " and substr(coa.glcode,1,"
                + substringVal
                + ") like '"
                + cashCode
                + "%' GROUP BY substr(coa.glcode,1,"
                + substringVal + "), fundid ,coa.type "
                + tillDateOB + " ) WHERE amount is not null GROUP BY glcode, fundid";
        if (LOGGER.isInfoEnabled())
            LOGGER.info("getOpeningBalanceForCash query " + query);
        try
        {
            cf.getFundList(fundId, startDate, endDate);
            int j = 1;
            psmt = HibernateUtil.getCurrentSession().createSQLQuery(query);
            psmt.setString(j++, startDate);
            psmt.setString(j++, endDate);
            if (!fundCondition.equalsIgnoreCase(""))
                psmt.setString(j++, fundId);
            psmt.setString(j++, startDate);
            psmt.setString(j++, endDate);
            if (!fundCondition.equalsIgnoreCase(""))
                psmt.setString(j++, fundId);
            final List<Object[]> resultset = psmt.list();
            Double opeBal = null;
            HashMap openingBalsubList = null;
            for (final Object[] element : resultset) {
                glcode = element[0].toString();
                fuId = element[1].toString();
                opeBal = Double.parseDouble(element[2].toString());
                if (!openingBal.containsKey(glcode))
                {
                    openingBalsubList = new HashMap();
                    for (final String element2 : reqFundId1)
                        if (element2.equalsIgnoreCase(fuId))
                            openingBalsubList.put(element2, opeBal);
                        else
                            openingBalsubList.put(element2, new Double(0));
                    openingBal.put(glcode, openingBalsubList);
                } else
                    ((HashMap) openingBal.get(glcode)).put(fuId, opeBal);
            }
        } catch (final Exception e)
        {
            LOGGER.error("Error in getOpeningBalanceForCash" + e.getMessage());
            throw taskExc;
        }
    }

    public void getPrevYearReport(final Connection conn, final String prevStartDate, final String prevEndDate,
            final String[] cashCode,
            final int maxCodeLength, final int minorCodeLength, final String amountIn, final String[] reqFundId)
                    throws TaskFailedException {
        try
        {
            prevYrAmtList = new ArrayList();
            final CallableStatement cstmt1 = conn.prepareCall("{? = call EGF_REPORT.RCPPYMNT(?, ?, ?, ?)}");
            cstmt1.setFetchSize(1000);
            // This fix is for Phoenix Migration.
            // cstmt1.registerOutParameter(1,OracleTypes.CURSOR);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("prevStartDate-->" + prevStartDate + ":::prevEndDate-->" + prevEndDate);
            cstmt1.setString(2, prevStartDate);
            cstmt1.setString(3, prevEndDate);
            cstmt1.setString(4, cashCode[0].substring(0, maxCodeLength));
            cstmt1.setInt(5, minorCodeLength);
            cstmt1.executeQuery();
            final ResultSet rs1 = (ResultSet) cstmt1.getObject(1);

            HashMap data = new HashMap();
            BigDecimal rowTotal = new BigDecimal(0);
            while (rs1.next())
            {
                final String[] fund = new String[reqFundId.length];
                String rcptScheduleId = "";
                String pymntScheduleId = "";
                if (rs1.getString("repsubtype").equalsIgnoreCase("ROP"))
                {
                    data = new HashMap();
                    if (rs1.getString("receiptscheduleid") != null)
                        rcptScheduleId = rs1.getString("receiptscheduleid");
                    rowTotal = new BigDecimal(0);

                    System.out
                    .println("---------------------------fund length -----------------------------------------------------"
                            + reqFundId.length);
                    for (int i = 0; i < reqFundId.length; i++)
                    {
                        if (rs1.getString(i + 5) != null)
                            fund[i] = rs1.getString(i + 5);
                        else
                            fund[i] = "0";
                        fund[i] = cf.formatAmt(fund[i], amountIn);
                        rowTotal = rowTotal.add(new BigDecimal(fund[i]));
                    }
                    data.put(rcptScheduleId, rowTotal.toString());
                    prevYrAmtList.add(data);
                }
                else if (rs1.getString("repsubtype").equalsIgnoreCase("RNOP"))
                {
                    data = new HashMap();
                    if (rs1.getString("receiptscheduleid") != null)
                        rcptScheduleId = rs1.getString("receiptscheduleid");
                    rowTotal = new BigDecimal(0);
                    for (int i = 0; i < reqFundId.length; i++)
                    {
                        if (rs1.getString(i + 5) != null)
                            fund[i] = rs1.getString(i + 5);
                        else
                            fund[i] = "0";
                        fund[i] = cf.formatAmt(fund[i], amountIn);
                        rowTotal = rowTotal.add(new BigDecimal(fund[i]));
                    }
                    data.put(rcptScheduleId, rowTotal.toString());
                    prevYrAmtList.add(data);
                }
                else if (rs1.getString("repsubtype").equalsIgnoreCase("POP"))
                {
                    data = new HashMap();
                    if (rs1.getString("paymentscheduleid") != null)
                        pymntScheduleId = rs1.getString("paymentscheduleid");
                    rowTotal = new BigDecimal(0);
                    for (int i = 0; i < reqFundId.length; i++)
                    {
                        if (rs1.getString(reqFundId.length + i + 5) != null)
                            fund[i] = rs1.getString(reqFundId.length + i + 5);
                        else
                            fund[i] = "0";
                        fund[i] = cf.formatAmt(fund[i], amountIn);
                        rowTotal = rowTotal.add(new BigDecimal(fund[i]));
                    }
                    data.put(pymntScheduleId, rowTotal.toString());
                    prevYrAmtList.add(data);
                }
                else if (rs1.getString("repsubtype").equalsIgnoreCase("PNOP"))
                {
                    data = new HashMap();
                    if (rs1.getString("paymentscheduleid") != null)
                        pymntScheduleId = rs1.getString("paymentscheduleid");
                    rowTotal = new BigDecimal(0);
                    for (int i = 0; i < reqFundId.length; i++)
                    {
                        if (rs1.getString(reqFundId.length + i + 5) != null)
                            fund[i] = rs1.getString(reqFundId.length + i + 5);
                        else
                            fund[i] = "0";
                        fund[i] = cf.formatAmt(fund[i], amountIn);
                        rowTotal = rowTotal.add(new BigDecimal(fund[i]));
                    }
                    data.put(pymntScheduleId, rowTotal.toString());
                    prevYrAmtList.add(data);
                }
            }
        } catch (final Exception e)
        {
            LOGGER.error("Error in getPrevYearReport" + e.getMessage());
            throw taskExc;
        }
    }

    public void getOtherReceiptsOrPayments(final String type, final String cashMajorCode, final String substringVal,
            final String startDate,
            final String endDate, final HashMap otherAmt) throws Exception
    {
        String amountType = "", scheduleCondition = "", vhCondition = "";
        if (type.equalsIgnoreCase("Receipt"))
        {
            amountType = "sum(creditamount)";
            scheduleCondition = " AND coa.RECEIPTSCHEDULEID IS NULL";
            vhCondition = " AND vh.id IN (SELECT DISTINCT vh.id FROM VOUCHERHEADER vh, GENERALLEDGER gl WHERE vh.id=gl.voucherheaderid AND vh.TYPE NOT IN ('Contra') AND vh.status<>4 AND gl.glcode LIKE ('"
                    + cashMajorCode + "%') AND gl.debitamount>0)";
        }
        else if (type.equalsIgnoreCase("Payment"))
        {
            amountType = "sum(debitamount)";
            scheduleCondition = " AND coa.PAYMENTSCHEDULEID IS NULL";
            vhCondition = " AND vh.id IN(SELECT DISTINCT vh.id FROM VOUCHERHEADER vh, GENERALLEDGER gl WHERE vh.id=gl.voucherheaderid AND vh.TYPE NOT IN ('Contra') AND vh.status<>4 AND gl.glcode LIKE ('"
                    + cashMajorCode + "%') AND gl.creditamount>0)";
        }
        String fuId = "";
        final String query = "SELECT f.id as \"fundid\", " + amountType
                + " as \"amount\" FROM GENERALLEDGER gl, VOUCHERHEADER vh, CHARTOFACCOUNTS coa, FUND f"
                + " WHERE vh.ID=gl.voucherheaderid AND SUBSTR(gl.glcode,1," + substringVal
                + ")=coa.glcode AND vh.voucherdate>=? AND vh.voucherdate<=?"
                + " " + scheduleCondition + " AND vh.status<>4 AND vh.fundid=f.id AND coa.glcode NOT LIKE ('" + cashMajorCode
                + "%') " + vhCondition + " GROUP BY f.id";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getOtherReceiptsOrPayments query " + query);
        try
        {
            final Query psmt = HibernateUtil.getCurrentSession().createSQLQuery(query);
            psmt.setString(0, startDate);
            psmt.setString(1, endDate);
            resultset = psmt.list();
            Double amount = null;
            for (final Object[] element : resultset) {
                fuId = element[0].toString();
                amount = Double.parseDouble(element[1].toString());
                for (final String element2 : reqFundId)
                    if (element2.equalsIgnoreCase(fuId))
                        otherAmt.put(element2, amount);
            }
        } catch (final Exception e)
        {
            LOGGER.error("Error in getOtherReceiptsOrPayments" + e.getMessage());
            throw taskExc;
        }
    }

    /*
     * returns RP schedules list
     */
    Boolean addSlNoRow = true;

    List getRPSchedule(final String amountIn, final String scheduleNo) throws TaskFailedException, SQLException, ParseException {
        List dataList = new ArrayList();
        List<Object[]> rs = null;
        getFundList(dtformatter.format(sdf.parse(startDate)), dtformatter.format(sdf.parse(endDate)));
        if (scheduleNo.equals("viewAllSchedules")) {
            rs = HibernateUtil.getCurrentSession()
                    .createSQLQuery("select id,schedule  schedule from schedulemapping where reporttype='RP' order by schedule")
                    .list();
            // rs=statement.executeQuery("select id,schedule  schedule from schedulemapping where reporttype='RP' and schedule in('R-01','R-37') order by schedule");
            if (LOGGER.isInfoEnabled())
                LOGGER.info("All schedules SQL:"
                        + "select id,schedule from schedulemapping where reporttype='RP' order by schedule");
            for (final Object[] element : rs)
                if (element[1].toString().equals(scheduleR_01) || element[1].toString().equals(scheduleR_37))
                    dataList.addAll(getRP_R01_R37_Schedule(amountIn, element[1].toString()));
                else
                    dataList.addAll(getRPSingleSchedule(amountIn, element[1].toString()));
        } else if (scheduleNo.equals(scheduleR_01))
            dataList = getRP_R01_R37_Schedule(amountIn, scheduleNo);
        else if (scheduleNo.equals(scheduleR_37))
            dataList = getRP_R01_R37_Schedule(amountIn, scheduleNo);
        else
            dataList = getRPSingleSchedule(amountIn, scheduleNo);
        return dataList;
    }

    /*
     * this method returns datalist for single schedule for scheduleNo
     */
    List getRPSingleSchedule(final String amountIn, final String scheduleNo) throws TaskFailedException, SQLException,
            ParseException {
        final List dataList = new ArrayList();
        Query psmt = null;
        try {

            final String sql = getRPScheduleSQL(scheduleNo);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Inside RPSingleSchedule");
            psmt = HibernateUtil.getCurrentSession().createSQLQuery(sql);
            resultset = psmt.list();

            BigDecimal rowTotal = BigDecimal.ZERO;
            BigDecimal amt = BigDecimal.ZERO;
            // BigDecimal tempAmt = BigDecimal.ZERO;;
            Boolean addHeaderRow = true;
            final BigDecimal[] fundTotals = new BigDecimal[reqFundId.length];
            BigDecimal prevYearTotals = BigDecimal.ZERO;
            for (final Object[] element : resultset) {
                if (addSlNoRow) {
                    dataList.add(putColNoToRPSchedule());
                    addSlNoRow = false;
                }
                if (addHeaderRow) {
                    // dataList.add(putColNoToRPSchedule());
                    dataList.add(putScheduleNoNameToRPSchedule(element[1].toString(), element[2].toString()));
                    addHeaderRow = false;
                }
                final Map map = new HashMap();
                // map.put("rowHeader", "rowHeader");
                map.put("accountcode", element[5].toString());
                map.put("particulars", element[6].toString());
                for (int i = 0; i < reqFundId.length; i++) {
                    // Phoenix amt=resultset.getBigDecimal(reqFundName[i]);
                    map.put(reqFundName[i], cf.formatAmt(amt.toPlainString(), amountIn));
                    rowTotal = rowTotal.add(amt);
                    fundTotals[i] = fundTotals[i] == null ? BigDecimal.ZERO.add(amt) : fundTotals[i].add(amt);
                }
                map.put("total", cf.formatAmt(rowTotal.toPlainString(), amountIn));
                amt = new BigDecimal(element[8].toString());
                map.put("prevYearTotal", cf.formatAmt(amt.toPlainString(), amountIn));
                prevYearTotals = prevYearTotals == null ? amt : prevYearTotals.add(amt);
                rowTotal = BigDecimal.ZERO;
                // if(LOGGER.isInfoEnabled()) LOGGER.info("add to dataList:"+dataList.size());
                dataList.add(map);
            }
            // totals- last row
            final Map map = new HashMap();
            map.put("particulars", "Total");
            rowTotal = BigDecimal.ZERO;
            for (int i = 0; i < reqFundName.length; i++) {
                fundTotals[i] = fundTotals[i] == null ? BigDecimal.ZERO : fundTotals[i];
                map.put(reqFundName[i], cf.formatAmt(fundTotals[i].toPlainString(), amountIn));
                rowTotal = rowTotal.add(fundTotals[i]);
            }
            map.put("total", cf.formatAmt(rowTotal.toPlainString(), amountIn));
            map.put("prevYearTotal", cf.formatAmt(prevYearTotals.toPlainString(), amountIn));
            map.put("rowBold", "rowBold");
            dataList.add(map);
        } catch (final SQLException e) {
            LOGGER.error("Exception in getRPSingleSchedule:" + e.getMessage(), e);
            throw taskExc;
        } finally {
            if (resultset != null) {
            }
        }
        if (LOGGER.isInfoEnabled())
            LOGGER.info("dataList.size()---------------->" + dataList.size());
        if (dataList.size() > 1)
            return dataList;
        else
            return new ArrayList();
    }

    /*
     * returns query for RP schedule
     */
    String getRPScheduleSQL(final String scheduleNo) throws TaskFailedException, SQLException, ParseException {
        final StringBuffer sbRcp = new StringBuffer(3000);
        final StringBuffer sbPymt = new StringBuffer(3000);
        Query psmt = null;
        String repsubtype = "";
        String qry = "";
        try {
            qry = "select repsubtype from schedulemapping where schedule=?";
            psmt = HibernateUtil.getCurrentSession().createSQLQuery(qry);
            psmt.setString(0, scheduleNo);
            resultset = psmt.list();
            for (final Object[] element : resultset)
                repsubtype = element[0].toString();
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new TaskFailedException();
        }

        sbRcp.append("SELECT DISTINCT sm.id,sm.schedule schedule, sm.schedulename, case when coa.receiptscheduleid=null then coa.paymentscheduleid else  coa.receiptscheduleid end scheduleid,coa.id coaid,coa.glcode glcode,coa.name coaname,repsubtype ");
        for (int i = 0; i < reqFundId.length; i++)
        {
            sbRcp.append(", ");
            sbRcp.append(" nvl( round( (SELECT case sm.repsubtype when 'ROP' then SUM (gl.creditamount) when  'RNOP' then SUM (gl.creditamount) else 0 end ");
            sbRcp.append("        FROM generalledger gl, ");
            sbRcp.append("                         voucherheader vh ");
            sbRcp.append("                   WHERE vh.ID = gl.voucherheaderid ");
            sbRcp.append("                     AND SUBSTR (gl.glcode, 1,").append(minorCodeLength).append(") = coa.glcode ");
            sbRcp.append(" 					AND vh.voucherdate >= TO_DATE ('").append(dtformatter.format(sdf.parse(startDate))).append("')");
            sbRcp.append("                     AND vh.voucherdate <= TO_DATE ('").append(dtformatter.format(sdf.parse(endDate)))
            .append("')");
            sbRcp.append("                     AND vh.ID IN ( ");
            sbRcp.append(" 										SELECT DISTINCT vh1.ID ");
            sbRcp.append("                                       FROM voucherheader vh1, generalledger gl1 ");
            sbRcp.append("                                      WHERE vh1.ID = gl1.voucherheaderid ");
            sbRcp.append(" 										AND vh1.status <> 4  AND gl1.glcode LIKE ('")
            .append(cashCode[0].substring(0, maxCodeLength)).append("%') AND gl1.debitamount > 0 ");
            sbRcp.append(" 									) ");
            sbRcp.append("                     AND vh.fundid = ").append(reqFundId[i]);
            sbRcp.append("                     AND vh.status <> 4) ");
            sbRcp.append(" 					,2) ");
            sbRcp.append(" 					,0)AS ");
            sbRcp.append("\"").append(reqFundName[i]).append("\"");
        }
        sbRcp.append("  , NVL");
        sbRcp.append("                   (ROUND");
        sbRcp.append("                       ((SELECT case sm.repsubtype when 'ROP'then SUM (gl.creditamount)  when 'RNOP' then SUM (gl.creditamount) else 0 end ");
        sbRcp.append("                           FROM generalledger gl, voucherheader vh");
        sbRcp.append("                          WHERE vh.ID = gl.voucherheaderid");
        sbRcp.append("                            AND SUBSTR (gl.glcode, 1, ").append(minorCodeLength).append(") = coa.glcode");
        sbRcp.append("                            AND vh.voucherdate >= TO_DATE ('")
        .append(dtformatter.format(sdf.parse(prevStartDate))).append("')");
        sbRcp.append("                            AND vh.voucherdate <= TO_DATE ('")
        .append(dtformatter.format(sdf.parse(prevEndDate))).append("')");
        sbRcp.append("                            AND vh.ID IN (");
        sbRcp.append("                                   SELECT DISTINCT vh1.ID");
        sbRcp.append("                                              FROM voucherheader vh1,");
        sbRcp.append("                                                   generalledger gl1");
        sbRcp.append("                                             WHERE vh1.ID =");
        sbRcp.append("                                                           gl1.voucherheaderid");
        sbRcp.append("                                               AND vh1.status <> 4");
        sbRcp.append("                                               AND gl1.glcode LIKE ('")
        .append(cashCode[0].substring(0, maxCodeLength)).append("%')");
        sbRcp.append("                                               AND gl1.debitamount > 0)");
        sbRcp.append("                            AND vh.status <> 4),");
        sbRcp.append("                        2");
        sbRcp.append("                       ),");
        sbRcp.append("                    0");
        sbRcp.append("                   )  AS \"PrevYearTotal\"");

        sbRcp.append("			FROM schedulemapping sm, chartofaccounts coa, chartofaccounts coa1 ");
        sbRcp.append("	          WHERE sm.ID IN (coa.receiptscheduleid, coa.paymentscheduleid)  ");
        sbRcp.append("	            AND coa.parentid = coa1.ID  ");
        sbRcp.append("	            AND coa1.glcode NOT LIKE ('").append(cashCode[0].substring(0, maxCodeLength))
        .append("%') and sm.schedule='").append(scheduleNo).append("' ");
        sbRcp.append("	        ORDER BY repsubtype DESC, schedule, glcode , coaname ");
        // sb.append(" union ");
        sbPymt.append("SELECT DISTINCT sm.id,sm.schedule schedule, sm.schedulename, case when coa.receiptscheduleid=null then coa.paymentscheduleid else coa.receiptscheduleid end scheduleid,coa.id coaid,coa.glcode glcode,coa.name coaname,repsubtype ");
        for (int i = 0; i < reqFundId.length; i++)
        {
            sbPymt.append(", ");
            sbPymt.append(" nvl( round( (SELECT  case sm.repsubtype when 'POP'then SUM (gl.debitamount) when 'PNOP' then SUM (gl.debitamount) else 0 end ");
            sbPymt.append("        FROM generalledger gl, ");
            sbPymt.append("                         voucherheader vh ");
            sbPymt.append("                   WHERE vh.ID = gl.voucherheaderid ");
            sbPymt.append("                     AND SUBSTR (gl.glcode, 1,").append(minorCodeLength).append(") = coa.glcode ");
            sbPymt.append(" 					AND vh.voucherdate >= TO_DATE ('").append(dtformatter.format(sdf.parse(startDate))).append("')");
            sbPymt.append("                     AND vh.voucherdate <= TO_DATE ('").append(dtformatter.format(sdf.parse(endDate)))
            .append("')");
            sbPymt.append("                     AND vh.ID IN ( ");
            sbPymt.append(" 										SELECT DISTINCT vh1.ID ");
            sbPymt.append("                                       FROM voucherheader vh1, generalledger gl1 ");
            sbPymt.append("                                      WHERE vh1.ID = gl1.voucherheaderid ");
            sbPymt.append(" 										AND vh1.status <> 4  AND gl1.glcode LIKE ('")
            .append(cashCode[0].substring(0, maxCodeLength)).append("%') AND gl1.creditamount > 0 ");
            sbPymt.append(" 									) ");
            sbPymt.append("                     AND vh.fundid = ").append(reqFundId[i]);
            sbPymt.append("                     AND vh.status <> 4) ");
            sbPymt.append(" 					,2) ");
            sbPymt.append(" 					,0)AS ");
            sbPymt.append("\"").append(reqFundName[i]).append("\"");
        }
        sbPymt.append(",NVL");
        sbPymt.append("                   (ROUND");
        sbPymt.append("                       ((SELECT case sm.repsubtype when 'POP' then SUM (gl.debitamount) when 'PNOP' then SUM (gl.debitamount) else 0 end ");
        sbPymt.append("                           FROM generalledger gl, voucherheader vh");
        sbPymt.append("                          WHERE vh.ID = gl.voucherheaderid");
        sbPymt.append("                            AND SUBSTR (gl.glcode, 1, ").append(minorCodeLength).append(") = coa.glcode");
        sbPymt.append("                            AND vh.voucherdate >= TO_DATE ('")
        .append(dtformatter.format(sdf.parse(prevStartDate))).append("')");
        sbPymt.append("                            AND vh.voucherdate <= TO_DATE ('")
        .append(dtformatter.format(sdf.parse(prevEndDate))).append("')");
        sbPymt.append("                            AND vh.ID IN (");
        sbPymt.append("                                   SELECT DISTINCT vh1.ID");
        sbPymt.append("                                              FROM voucherheader vh1,");
        sbPymt.append("                                                   generalledger gl1");
        sbPymt.append("                                             WHERE vh1.ID =");
        sbPymt.append("                                                           gl1.voucherheaderid");
        sbPymt.append("                                               AND vh1.status <> 4");
        sbPymt.append("                                               AND gl1.glcode LIKE ('")
        .append(cashCode[0].substring(0, maxCodeLength)).append("%')");
        sbPymt.append("                                               AND gl1.creditamount > 0)");
        sbPymt.append("                          ");
        sbPymt.append("                            AND vh.status <> 4),");
        sbPymt.append("                        2");
        sbPymt.append("                       ),");
        sbPymt.append("                    0");
        sbPymt.append("                   ) AS \"PrevYearTotal\"");

        sbPymt.append("			FROM schedulemapping sm, chartofaccounts coa, chartofaccounts coa1 ");
        sbPymt.append("	          WHERE sm.ID IN (coa.receiptscheduleid, coa.paymentscheduleid)  ");
        sbPymt.append("	            AND coa.parentid = coa1.ID  ");
        sbPymt.append("	            AND coa1.glcode NOT LIKE ('").append(cashCode[0].substring(0, maxCodeLength))
        .append("%') and sm.schedule='").append(scheduleNo).append("' ");
        sbPymt.append("	        ORDER BY repsubtype DESC, schedule, glcode , coaname ");
        if (repsubtype.equals("ROP") || repsubtype.equals("RNOP")) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("RPschedule:" + scheduleNo + "-->" + sbRcp.toString());
            return sbRcp.toString();
        } else {// if(repsubtype.equals("POP") || repsubtype.equals("PNOP")){
            if (LOGGER.isInfoEnabled())
                LOGGER.info("RPschedule:" + scheduleNo + "-->" + sbRcp.toString());
            return sbPymt.toString();
        }
    }

    /*
     * returns SlNo row as map
     */
    Map putColNoToRPSchedule() {
        final Map map = new HashMap();
        map.put("rowHeader", "rowHeader");
        map.put("accountcode", "1");
        map.put("particulars", "2");
        for (int i = 0; i < reqFundId.length; i++)
            map.put(reqFundName[i], 3 + i + "");
        map.put("total", 3 + reqFundId.length + "");
        map.put("prevYearTotal", 4 + reqFundId.length + "");
        return map;
    }

    /*
     * returns schedule Number and schedule name row as map
     */
    Map putScheduleNoNameToRPSchedule(final String schedule, final String scheduleName) {
        final Map map = new HashMap();
        // map.put("rowHeader", "rowHeader");
        map.put("accountcode", "Schedule " + schedule);
        map.put("particulars", scheduleName);

        return map;
    }

    String scheduleR_01 = "R-01", scheduleR_37 = "R-37";

    /*
     * returns data list for schedule R-01 or R-37 based on input scheduleNO
     */

    List getRP_R01_R37_Schedule(final String amountIn, final String scheduleNo) throws TaskFailedException, SQLException,
            ParseException {
        final List dataList = new ArrayList();
        final Map opBalance = new HashMap();
        Query psmt = null;
        try {
            // current year opbalance starts
            if (period.equalsIgnoreCase("3"))
            {
                final String tDate = startDate;
                String obdate = obStartDate;
                final DateUtils du = new DateUtils();
                if (LOGGER.isInfoEnabled())
                    LOGGER.info(obdate + " datevalidator:" + du.checkDateFormat("01-Apr-2007"));
                if (LOGGER.isInfoEnabled())
                    LOGGER.info(new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("dd-MMM-yyyy")
                    .parse("01-Apr-2007")));
                if (!du.checkDateFormat(obdate))
                    obdate = new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("dd-MMM-yyyy").parse(obdate));

                final Calendar c = Calendar.getInstance();
                if (Integer.parseInt(month) >= 2 && Integer.parseInt(month) <= 3)
                {
                    c.set(Calendar.MONTH, Integer.parseInt(tDate.split("/")[1]) - 2);
                    obEndDate = c.getActualMaximum(Calendar.DAY_OF_MONTH) + "/" + (Integer.parseInt(tDate.split("/")[1]) - 1)
                            + "/" + (Integer.parseInt(obdate.split("/")[2]) + 1);
                }
                else if (Integer.parseInt(month) == 1)
                    obEndDate = "31/12/" + Integer.parseInt(obdate.split("/")[2]);
                else if (Integer.parseInt(month) == 4)
                    obEndDate = obStartDate;
                else
                {
                    c.set(Calendar.MONTH, Integer.parseInt(tDate.split("/")[1]) - 2);
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("obdate:" + obdate);
                    obEndDate = c.getActualMaximum(Calendar.DAY_OF_MONTH) + "/" + (Integer.parseInt(tDate.split("/")[1]) - 1)
                            + "/" + Integer.parseInt(obdate.split("/")[2]);
                }
                prevObEndDate = obEndDate.split("/")[0] + "/" + obEndDate.split("/")[1] + "/"
                        + (Integer.parseInt(obEndDate.split("/")[2]) - 1) + "";
                final String snapShotDateTime = egc.getCurDateTime();
                effFilter = egc.getEffectiveDateFilter(snapShotDateTime);
                dt = sdf.parse(obEndDate);
                obEndDate = dtformatter.format(dt);
                dt = sdf.parse(obdate);
                obStartDate = dtformatter.format(dt);
                if (LOGGER.isInfoEnabled())
                    LOGGER.info(scheduleNo + " obStartDate:" + obStartDate + " obEndDate:" + obEndDate);
                if (scheduleR_01.equals(scheduleNo))
                    getOpeningBalanceForCash("", "A", "", cashCode[0].substring(0, maxCodeLength),
                            Integer.toString(minorCodeLength), obStartDate, obEndDate, 1, reqFundId, (HashMap) opBalance);
                else
                    // cf.getOpeningBalance(conn,"","A","",Integer.toString(minorCodeLength),dtformatter.format(sdf.parse(startDate)),dtformatter.format(sdf.parse(endDate)),1,reqFundId,(HashMap)opBalance);
                    getOpeningBalanceForCash("", "A", "", cashCode[0].substring(0, maxCodeLength),
                            Integer.toString(minorCodeLength), obStartDate, dtformatter.format(sdf.parse(endDate)), 1, reqFundId,
                            (HashMap) opBalance);
            }
            else
            {
                final Date dt = sdf.parse(startDate);
                startDate = dtformatter.format(dt);
                if (scheduleR_01.equals(scheduleNo))
                    cf.getOpeningBalance("", "A", "", Integer.toString(minorCodeLength), startDate, startDate, 1, reqFundId,
                            (HashMap) opBalance);
                else
                    // cf.getOpeningBalance(conn,"","A","",Integer.toString(minorCodeLength),startDate,dtformatter.format(sdf.parse(endDate)),1,reqFundId,(HashMap)opBalance);
                    getOpeningBalanceForCash("", "A", "", cashCode[0].substring(0, maxCodeLength),
                            Integer.toString(minorCodeLength), dtformatter.format(sdf.parse(obStartDate)),
                            dtformatter.format(sdf.parse(endDate)), 1, reqFundId, (HashMap) opBalance);
            }
            // current year opbalance ends
            // prev year obbalance starts
            final Map prevOpBal = new HashMap();
            if (period.equalsIgnoreCase("3"))
            {
                if (scheduleR_01.equals(scheduleNo))
                    getOpeningBalanceForCash("", "A", "", cashCode[0].substring(0, maxCodeLength),
                            Integer.toString(minorCodeLength), dtformatter.format(sdf.parse(prevStartDate)),
                            dtformatter.format(sdf.parse(prevObEndDate)), 1, reqFundId, (HashMap) prevOpBal);
                else
                    getOpeningBalanceForCash("", "A", "", cashCode[0].substring(0, maxCodeLength),
                            Integer.toString(minorCodeLength), dtformatter.format(sdf.parse(prevStartDate)),
                            dtformatter.format(sdf.parse(prevEndDate)), 1, reqFundId, (HashMap) prevOpBal);
            } else if (scheduleR_01.equals(scheduleNo))
                cf.getOpeningBalance("", "A", "", Integer.toString(minorCodeLength),
                        dtformatter.format(sdf.parse(prevStartDate)), dtformatter.format(sdf.parse(prevStartDate)), 1,
                        reqFundId, (HashMap) prevOpBal);
            else
                // cf.getOpeningBalance(conn,"","A","",Integer.toString(minorCodeLength),dtformatter.format(sdf.parse(prevStartDate)),dtformatter.format(sdf.parse(prevEndDate)),1,reqFundId,(HashMap)prevOpBal);
                getOpeningBalanceForCash("", "A", "", cashCode[0].substring(0, maxCodeLength),
                        Integer.toString(minorCodeLength), dtformatter.format(sdf.parse(prevStartDate)),
                        dtformatter.format(sdf.parse(prevEndDate)), 1, reqFundId, (HashMap) prevOpBal);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("opbalance:" + opBalance);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("prevOpBal:" + prevOpBal);
            // prev year obbalance ends
            final String sql = "select coa.glcode glcode ,coa.name coaname, sm.schedule schedule,sm.schedulename schedulename, sm.REPSUBTYPE repsubtype from chartofaccounts coa ,schedulemapping sm where sm.id in (receiptscheduleid, paymentscheduleid) and sm.schedule=? order by sm.schedule";
            if (LOGGER.isInfoEnabled())
                LOGGER.info("getRP" + scheduleNo + "Schedule SQL:" + sql);
            psmt = HibernateUtil.getCurrentSession().createSQLQuery(sql);
            psmt.setString(0, scheduleNo);
            resultset = psmt.list();
            final BigDecimal[] fundTotals = new BigDecimal[reqFundId.length];
            BigDecimal totAmt = BigDecimal.ZERO, tempAmt = BigDecimal.ZERO, prevTotAmt = BigDecimal.ZERO;
            Boolean addHeaderRow = true;
            for (final Object[] element : resultset) {
                if (addSlNoRow) {
                    dataList.add(putColNoToRPSchedule());
                    addSlNoRow = false;
                }
                if (addHeaderRow) {
                    // dataList.add(putColNoToRPSchedule());
                    dataList.add(putScheduleNoNameToRPSchedule(element[2].toString(), element[3].toString()));
                    addHeaderRow = false;
                }
                final Map map = new HashMap();
                map.put("accountcode", element[0].toString());
                map.put("particulars", element[1].toString());
                totAmt = BigDecimal.ZERO;
                tempAmt = BigDecimal.ZERO;
                for (int i = 0; i < reqFundId.length; i++) {
                    tempAmt = new BigDecimal(opBalance.get(element[0].toString()) == null ? "0.00"
                            : ((HashMap) opBalance.get(element[0].toString())).get(reqFundId[i]).toString());
                    map.put(reqFundName[i], cf.formatAmt(tempAmt.toPlainString(), amountIn));
                    totAmt = totAmt.add(tempAmt);
                    fundTotals[i] = fundTotals[i] == null ? BigDecimal.ZERO.add(tempAmt) : fundTotals[i].add(tempAmt);
                }
                map.put("total", cf.formatAmt(totAmt.toPlainString(), amountIn));
                // prevyear
                totAmt = BigDecimal.ZERO;
                tempAmt = BigDecimal.ZERO;
                for (final String element2 : reqFundId) {
                    tempAmt = new BigDecimal(prevOpBal.get(element[0].toString()) == null ? "0.00"
                            : ((HashMap) prevOpBal.get(element[0].toString())).get(element2).toString());
                    // map.put(reqFundName[i],cf.formatAmt(tempAmt.toPlainString(),amountIn) );
                    totAmt = totAmt.add(tempAmt);
                    prevTotAmt = prevTotAmt.add(tempAmt);
                    // fundTotals[i]=fundTotals[i]==null?BigDecimal.ZERO.add(tempAmt):fundTotals[i].add(tempAmt);
                }

                map.put("prevYearTotal", cf.formatAmt(totAmt.toPlainString(), amountIn));
                dataList.add(map);
            }
            // totals- last row
            final Map map = new HashMap();
            map.put("particulars", "Total");
            totAmt = BigDecimal.ZERO;
            for (int i = 0; i < reqFundName.length; i++) {
                fundTotals[i] = fundTotals[i] == null ? BigDecimal.ZERO : fundTotals[i];
                map.put(reqFundName[i], cf.formatAmt(fundTotals[i].toPlainString(), amountIn));
                totAmt = totAmt.add(fundTotals[i]);
            }
            map.put("total", cf.formatAmt(totAmt.toPlainString(), amountIn));
            map.put("prevYearTotal", cf.formatAmt(prevTotAmt.toPlainString(), amountIn));
            map.put("rowBold", "rowBold");
            dataList.add(map);
        } catch (final Exception e) {
            LOGGER.error("inside getRP_R01_R37_Schedule:" + e.getMessage(), e);
            throw taskExc;
        }
        return dataList;
    }

}
