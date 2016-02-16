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
 * Created on Apr 24,2008
 * @author Iliyaraja S
 */
package com.exilant.eGov.src.reports;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;

public class AdvanceRegister {
    Connection con;
    List<Object[]> resultset;
    Query statement;
    Query pstmt = null;
    TaskFailedException taskExc;
    String endDate, startDate;
    String paramdetailtypeid = "", paramdetailkeyid = "";
    String arMajorGLCode = "";
    public String reqSubLedgerId[];
    public String reqSubLedgerName[];
    public String reqSubLedgerOB[];
    boolean subLedgerDetails = false;

    BigDecimal colTotalPrevCB = new BigDecimal("0.00");
    BigDecimal colTotalPrevPaymentValue = new BigDecimal("0.00");
    BigDecimal colTotalPrevRecoveryValue = new BigDecimal("0.00");
    BigDecimal colTotalCBValue = new BigDecimal("0.00");

    ArrayList arList = new ArrayList();
    java.util.Date dt = new java.util.Date();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
    EGovernCommon egc = new EGovernCommon();
    CommnFunctions cf = new CommnFunctions();
    private static final Logger LOGGER = Logger
            .getLogger(AdvanceRegister.class);

    public AdvanceRegister() {
    }

    public ArrayList getAdvanceRegisterReport(final AdvanceRegisterBean arBean)
            throws TaskFailedException {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Inside getAdvanceRegisterReport()");

        if (!arBean.getEndDate().equals(""))
            isCurDate(arBean.getEndDate());
        try {

            if (LOGGER.isInfoEnabled())
                LOGGER.info("From date---->" + arBean.getStartDate());
            if (LOGGER.isInfoEnabled())
                LOGGER.info("To date------>" + arBean.getEndDate());
            if (LOGGER.isInfoEnabled())
                LOGGER.info("party detail type id--->"
                        + arBean.getPartytype_id());
            if (LOGGER.isInfoEnabled())
                LOGGER.info("party detail key id---->"
                        + arBean.getAccEntityKey());

            if (!arBean.getStartDate().equals("")) {
                dt = sdf.parse(arBean.getStartDate());
                startDate = formatter.format(dt);
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("After convert From date is--->" + startDate);
            }

            if (!arBean.getEndDate().equals("")) {
                dt = sdf.parse(arBean.getEndDate());
                endDate = formatter.format(dt);

                if (LOGGER.isInfoEnabled())
                    LOGGER.info("After convert To date is--->" + endDate);
            }
            if (!arBean.getPartytype_id().equals(""))
                paramdetailtypeid = arBean.getPartytype_id();

            if (!arBean.getAccEntityKey().equals(""))
                paramdetailkeyid = arBean.getAccEntityKey();

            arMajorGLCode = EGovConfig.getProperty("egf_config.xml",
                    "ARGLCode", "", "AdvanceRegisterCode");
            if (LOGGER.isInfoEnabled())
                LOGGER.info("AdvanceRegister arMajorGLCode--->" + arMajorGLCode);
            getOpeningBalanceForAdvanceRegister();
            getSubLedgerListForAdvRegister();
        } catch (final Exception exception) {
            LOGGER.error("EXP in getAdvanceRegisterReport"
                    + exception.getMessage());
            throw taskExc;
        }

        return arList;

    }

    /**
     * This function executes the Query-get the Opening Balance For Advance Register.
     */

    private void getOpeningBalanceForAdvanceRegister() throws Exception {

        try {
            final StringBuffer basicquery1 = new StringBuffer(
                    " SELECT slid AS \"slid\", slname AS \"slname\",opgdebitbal - opgcreditbal + prevdebit- prevcredit AS \"OpeningBalance\""
                            + " FROM (SELECT DISTINCT adk.detailkey slid,NVL((SELECT SUM(ts.openingcreditbalance)"
                            + " FROM chartofaccounts coa,financialyear fy,transactionsummary ts,accountdetailkey adk"
                            + " WHERE ts.glcodeid=coa.id AND ts.financialyearid=fy.id AND ts.accountdetailkey=adk.detailkey "
                            + " AND ts.accountdetailtypeid=adk.detailtypeid AND adk.detailtypeid="
                            + paramdetailtypeid
                            + " AND adk.detailkey="
                            + paramdetailkeyid
                            + " AND fy.startingdate <= TO_DATE ('"
                            + startDate
                            + "') AND fy.endingdate >= TO_DATE ('"
                            + startDate
                            + "')),0) OpgCreditBal,"
                            + " NVL((SELECT SUM(ts.openingdebitbalance) FROM chartofaccounts coa,financialyear fy,transactionsummary ts,"
                            + " accountdetailkey adk WHERE ts.glcodeid=coa.id AND ts.financialyearid=fy.id AND ts.accountdetailkey=adk.detailkey "
                            + " AND ts.accountdetailtypeid=adk.detailtypeid AND adk.detailtypeid="
                            + paramdetailtypeid
                            + " AND adk.detailkey="
                            + paramdetailkeyid
                            + " AND fy.startingdate <= TO_DATE ('"
                            + startDate
                            + "') AND fy.endingdate >= TO_DATE ('"
                            + startDate
                            + "')),0)OpgDebitBal,"
                            + " NVL((SELECT SUM (gld.amount)FROM generalledgerdetail gld,generalledger gl,voucherheader vh"
                            + " WHERE gld.generalledgerid = gl.ID AND gl.voucherheaderid = vh.ID AND gld.detailkeyid = adk.detailkey"
                            + " AND gld.detailtypeid = adk.detailtypeid AND vh.voucherdate >= fy.startingdate"
                            + " AND vh.voucherdate <=TO_DATE (TO_DATE ('"
                            + startDate
                            + "') - 1)AND vh.status NOT IN (4)AND gl.debitamount > 0),0) prevdebit,"
                            + " NVL((SELECT SUM (gld.amount)FROM generalledgerdetail gld,generalledger gl,voucherheader vh"
                            + " WHERE gld.generalledgerid = gl.ID AND gl.voucherheaderid = vh.ID AND gld.detailkeyid = adk.detailkey"
                            + " AND gld.detailtypeid = adk.detailtypeid AND vh.voucherdate >= fy.startingdate "
                            + " AND vh.voucherdate <=TO_DATE (TO_DATE ('"
                            + startDate
                            + "') - 1)AND vh.status NOT IN (4)AND gl.creditamount > 0),0) prevcredit,"
                            + " (SELECT DISTINCT case   adt.tablename  when  'accountEntityMaster' then (SELECT name FROM accountentitymaster"
                            + " WHERE id= adk.detailkey ) when 'relation' (SELECT name FROM relation WHERE id = adk.detailkey )  when   'eg_employee',"
                            + " (SELECT emp_firstname FROM eg_employee WHERE id= adk.detailkey ) else '' end  FROM accountdetailkey adk,accountdetailtype adt"
                            + " WHERE adt.id = adk.detailtypeid AND adk.detailtypeid="
                            + paramdetailtypeid
                            + " AND adk.detailkey="
                            + paramdetailkeyid
                            + ") slname"
                            + " FROM chartofaccounts coa,financialyear fy,accountdetailkey adk WHERE fy.isactiveforposting = true"
                            + " AND fy.startingdate <= TO_DATE ('"
                            + startDate
                            + "') AND fy.endingdate >= TO_DATE ('"
                            + startDate
                            + "')"
                            + " AND adk.detailtypeid="
                            + paramdetailtypeid
                            + " AND adk.detailkey="
                            + paramdetailkeyid + ") ");

            final String query = new StringBuffer().append(basicquery1).toString();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Query for getOpeningBalanceForAdvanceRegister---->"
                        + query);

            // AdvanceRegisterBean arBean=null;

            statement = HibernateUtil.getCurrentSession().createSQLQuery(query);
            resultset = statement.list();

            int resSize = 0, i = 0;
            resSize = resultset.size();

            reqSubLedgerId = new String[resSize];
            reqSubLedgerName = new String[resSize];
            reqSubLedgerOB = new String[resSize];

            for (final Object[] element : resultset) {
                reqSubLedgerId[i] = element[0].toString();
                reqSubLedgerName[i] = element[1].toString();
                // reqSubLedgerOB[i] =
                // cf.numberToString(resultset.getString("OpeningBalance")).toString();
                reqSubLedgerOB[i] = element[3].toString();
                i += 1;
            }
        } catch (final Exception sqlE) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(
                        "Exception in getOpeningBalanceForAdvanceRegister ",
                        sqlE);
            throw taskExc;
        }

    }

    /**
     * This function executes the Query-get all the subledger type related with Advance Register,retrieves the values from the
     * Query into local variables.
     */
    private void getSubLedgerListForAdvRegister() throws Exception {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Inside getSubLedgerListForAdvRegister");
        try {
            StringBuffer wherequery3 = new StringBuffer();
            StringBuffer wherequery4 = new StringBuffer();
            StringBuffer wherequery5 = new StringBuffer();

            if (endDate != null && !endDate.equals(""))
                wherequery3 = wherequery3.append(" AND bvh.voucherdate <=")
                .append("TO_DATE('" + endDate + "')");
            if (paramdetailtypeid != null && !paramdetailtypeid.equals("")
                    && paramdetailkeyid != null && !paramdetailkeyid
                    .equals("")) {
                wherequery4 = wherequery4.append(" AND adk.detailtypeid=")
                        .append(paramdetailtypeid);
                wherequery4 = wherequery4.append(" AND adk.detailkey=").append(
                        paramdetailkeyid);
            }
            wherequery5 = wherequery5
                    .append(" order by sltypename,sltype,slid,slname,vhid,Particulars DESC");

            final StringBuffer basicquery1 = new StringBuffer(
                    "SELECT coa.NAME sltypename,bvh.id AS vhid,adk.detailtypeid AS sltype,adk.detailkey AS slid,bvh.description as \"remarks\","
                            + " TO_CHAR(bvh.voucherdate) vDate, case  adt.tablename  when  'accountEntityMaster' then  "
                            + " (SELECT name FROM accountentitymaster WHERE id= adk.detailkey ) when 'relation' then  (SELECT name FROM relation WHERE id = adk.detailkey )  "
                            + " when 'eg_employee' then (SELECT emp_firstname FROM eg_employee WHERE id= adk.detailkey ) else '' end  slname,"
                            + " case when gl.debitamount = 0  then NULL else CONCAT(bvh.vouchernumber,CONCAT('-',bvh.voucherdate)) end AS \"PaymentOrderNumberDate\",null as \"BPVNumberDate\","
                            + " concat(gl.glcode,concat('-',coa.NAME)) Particulars,case when gl.debitamount = 0 then NULL else gld.amount end as \"PaymentAmount\","
                            + " case when gl.creditamount = 0 then NULL else CONCAT(bvh.vouchernumber,CONCAT('-',bvh.voucherdate)) end AS \"RecoveryNumberdate\","
                            + " case when gl.creditamount = 0 then NULL else gld.amount end Recoveryamount FROM generalledgerdetail gld,generalledger gl,"
                            + " voucherheader bvh left join paymentheader ph ON   ph.voucherheaderid = bvh.ID,"
                            + " voucherheader bvh1 left join otherbilldetail obd ON obd.payvhid = bvh1.ID,chartofaccounts coa,"
                            + " accountdetailkey adk,chartofaccountdetail cod ,accountdetailtype adt WHERE gld.detailtypeid = adk.detailtypeid AND gld.detailkeyid = adk.detailkey"
                            + " AND bvh.ID = bvh1.ID AND gld.generalledgerid = gl.ID AND gl.glcodeid = coa.ID AND gl.voucherheaderid = bvh.ID"
                            + " AND bvh.voucherdate >=TO_DATE ('"
                            + startDate
                            + "') AND adt.id = adk.detailtypeid"
                            + " and bvh.status <> 4 AND coa.ID = cod .glcodeid AND cod.detailtypeid = adk .detailtypeid AND coa.ID IN (SELECT ID FROM chartofaccounts WHERE glcode like '"
                            + arMajorGLCode + "%" + "')");

            final String query = new StringBuffer().append(basicquery1)
                    .append(wherequery3).append(wherequery4)
                    .append(wherequery5).toString();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Query for getSubLedgerListForAdvRegister---->"
                        + query);
            statement = HibernateUtil.getCurrentSession().createSQLQuery(query);
            resultset = statement.list();
            if (LOGGER.isInfoEnabled())
                LOGGER.info("After Execute Query");
            int i = 1;

            String slDetailKeyId = "";
            for (final Object[] element : resultset) {
                subLedgerDetails = true;
                String sltype = "", slid = "", remarks = "", vdate = "", slname = "", payordernumberdate = "", bankpayvnumberdate = "";
                String particulars = "", payamount = "", recovnumberdate = "", recovamount = "";

                if (slDetailKeyId.equals("")) {
                    slDetailKeyId = element[3].toString();
                    // for Opening Balance Header for each subledger name
                    HashMap data = new HashMap();

                    if (element[6].toString() != null)
                        slname = element[6].toString();
                    else
                        slname = "&nbsp;";

                    data.put("serialNo", "&nbsp;");
                    data.put("sltype", "&nbsp;");
                    data.put("slid", "&nbsp;");
                    data.put("vdate", "&nbsp;");

                    data.put("slname", slname);
                    data.put("payordernumberdate", "&nbsp;");
                    data.put("bankpayvnumberdate", "&nbsp;");
                    data.put("particulars", "Opening Balance");

                    for (int k = 0; k < reqSubLedgerId.length; k++)
                        if (reqSubLedgerId[k].equals(slDetailKeyId)) {
                            data.put("payamount",
                                    formatAmtTwoDecimal(reqSubLedgerOB[k])
                                    .toString());
                            data.put("closingBal",
                                    formatAmtTwoDecimal(reqSubLedgerOB[k])
                                    .toString());

                            if (!reqSubLedgerOB[k].equals("0")
                                    && !reqSubLedgerOB[k].equals("")) {
                                if (LOGGER.isInfoEnabled())
                                    LOGGER.info("Inside If 1");
                                colTotalCBValue = new BigDecimal(
                                        reqSubLedgerOB[k]);
                            } else {
                                if (LOGGER.isInfoEnabled())
                                    LOGGER.info("Inside else 1");
                                colTotalCBValue = new BigDecimal("0.00");
                            }
                        }

                    data.put("recovnumberdate", "&nbsp;");
                    data.put("recovamount", "&nbsp;");
                    data.put("remarks", "&nbsp;");

                    data.put("serialNo", i + "");
                    i++;

                    arList.add(data);

                    // first record start for particular subledger name

                    data = new HashMap();

                    if (element[2].toString() != null)
                        sltype = element[2].toString();
                    else
                        sltype = "&nbsp;";

                    if (element[3].toString() != null)
                        slid = element[3].toString();
                    else
                        slid = "&nbsp;";

                    // if(LOGGER.isInfoEnabled())
                    // LOGGER.info("remarks value is---"+element[4].toString());

                    if (element[4].toString() != null) {
                        if (element[4].toString().trim().length() > 0)
                            remarks = element[4].toString();
                        else
                            remarks = "&nbsp;";
                    } else
                        remarks = "&nbsp;";

                    if (element[5].toString() != null)
                        vdate = element[5].toString();
                    else
                        vdate = "&nbsp;";

                    if (element[6].toString() != null)
                        slname = element[6].toString();
                    else
                        slname = "&nbsp;";
                    if (element[7].toString() != null)
                        payordernumberdate = element[7].toString();
                    else
                        payordernumberdate = "&nbsp;";

                    if (element[8].toString() != null)
                        bankpayvnumberdate = element[8].toString();
                    else
                        bankpayvnumberdate = "&nbsp;";

                    if (element[9].toString() != null)
                        particulars = element[9].toString();
                    else
                        particulars = "&nbsp;";

                    if (element[10].toString() != null) {
                        // if(LOGGER.isInfoEnabled())
                        // LOGGER.info("Inside if 1");
                        payamount = formatAmtTwoDecimal(element[10].toString())
                                .toString();
                        colTotalPrevPaymentValue = new BigDecimal(
                                element[10].toString());

                    } else {
                        // if(LOGGER.isInfoEnabled())
                        // LOGGER.info("Inside else 1");
                        payamount = "&nbsp;";
                        colTotalPrevPaymentValue = new BigDecimal("0.00");
                    }

                    if (element[11].toString() != null)
                        recovnumberdate = element[11].toString();
                    else
                        recovnumberdate = "&nbsp;";

                    if (element[12].toString() != null) {
                        // if(LOGGER.isInfoEnabled())
                        // LOGGER.info("Inside if 2");
                        recovamount = formatAmtTwoDecimal(
                                element[12].toString()).toString();
                        colTotalPrevRecoveryValue = new BigDecimal(
                                element[12].toString());

                    } else {
                        // if(LOGGER.isInfoEnabled())
                        // LOGGER.info("Inside else 2");
                        recovamount = "&nbsp;";
                        colTotalPrevRecoveryValue = new BigDecimal("0.00");
                    }
                    colTotalPrevCB = colTotalPrevCB.add(colTotalCBValue);
                    colTotalPrevCB = colTotalPrevCB
                            .add(colTotalPrevPaymentValue);
                    colTotalPrevCB = colTotalPrevCB
                            .subtract(colTotalPrevRecoveryValue);
                    data.put("serialNo", "&nbsp;");
                    data.put("sltype", sltype);
                    data.put("slid", slid);
                    data.put("vdate", vdate);
                    data.put("slname", "&nbsp;");
                    data.put("payordernumberdate", payordernumberdate);
                    data.put("bankpayvnumberdate", bankpayvnumberdate);
                    data.put("particulars", particulars);
                    data.put("payamount", payamount);
                    data.put("recovnumberdate", recovnumberdate);
                    data.put("recovamount", recovamount);
                    data.put("remarks", remarks);
                    data.put("closingBal",
                            formatAmtTwoDecimal(colTotalPrevCB.toString()));

                    arList.add(data);
                }
                // for categorize same subledger name
                else if (slDetailKeyId.equalsIgnoreCase(element[3].toString())) {

                    final HashMap data = new HashMap();

                    if (element[2].toString() != null)
                        sltype = element[2].toString();
                    else
                        sltype = "&nbsp;";

                    if (element[3].toString() != null)
                        slid = element[3].toString();
                    else
                        slid = "&nbsp;";

                    // if(LOGGER.isInfoEnabled())
                    // LOGGER.info("remarks value is---"+element[4].toString());
                    if (element[4].toString() != null) {
                        if (element[4].toString().trim().length() > 0)
                            remarks = element[4].toString();
                        else
                            remarks = "&nbsp;";
                    } else
                        remarks = "&nbsp;";

                    if (element[5].toString() != null)
                        vdate = element[5].toString();
                    else
                        vdate = "&nbsp;";

                    if (element[6].toString() != null)
                        slname = element[6].toString();
                    else
                        slname = "&nbsp;";
                    if (element[7].toString() != null)
                        payordernumberdate = element[7].toString();
                    else
                        payordernumberdate = "&nbsp;";

                    if (element[8].toString() != null)
                        bankpayvnumberdate = element[8].toString();
                    else
                        bankpayvnumberdate = "&nbsp;";

                    if (element[9].toString() != null)
                        particulars = element[9].toString();
                    else
                        particulars = "&nbsp;";

                    if (element[10].toString() != null) {
                        payamount = formatAmtTwoDecimal(element[10].toString())
                                .toString();
                        colTotalPrevPaymentValue = new BigDecimal(
                                element[10].toString());
                    } else {
                        payamount = "&nbsp;";
                        colTotalPrevPaymentValue = new BigDecimal("0.00");
                    }

                    if (element[11].toString() != null)
                        recovnumberdate = element[11].toString();
                    else
                        recovnumberdate = "&nbsp;";

                    if (element[12].toString() != null) {
                        recovamount = formatAmtTwoDecimal(
                                element[12].toString()).toString();
                        colTotalPrevRecoveryValue = new BigDecimal(
                                element[12].toString());

                    } else {
                        recovamount = "&nbsp;";
                        colTotalPrevRecoveryValue = new BigDecimal("0.00");
                    }

                    colTotalPrevCB = colTotalPrevCB
                            .add(colTotalPrevPaymentValue);
                    colTotalPrevCB = colTotalPrevCB
                            .subtract(colTotalPrevRecoveryValue);

                    data.put("serialNo", "&nbsp;");
                    data.put("sltype", sltype);
                    data.put("slid", slid);
                    data.put("vdate", vdate);

                    data.put("slname", "&nbsp;");
                    data.put("payordernumberdate", payordernumberdate);
                    data.put("bankpayvnumberdate", bankpayvnumberdate);
                    data.put("particulars", particulars);

                    data.put("payamount", payamount);
                    data.put("recovnumberdate", recovnumberdate);
                    data.put("recovamount", recovamount);
                    data.put("remarks", remarks);
                    data.put("closingBal",
                            formatAmtTwoDecimal(colTotalPrevCB.toString()));

                    arList.add(data);

                }
                // Next record-different subledger name start
                else {
                    slDetailKeyId = element[3].toString();
                    colTotalPrevCB = new BigDecimal("0.00");
                    colTotalPrevPaymentValue = new BigDecimal("0.00");
                    colTotalPrevRecoveryValue = new BigDecimal("0.00");

                    // for Opening Balance Header for each subledger name
                    HashMap data = new HashMap();

                    if (element[6].toString() != null)
                        slname = element[6].toString();
                    else
                        slname = "&nbsp;";

                    data.put("serialNo", "&nbsp;");
                    data.put("sltype", "&nbsp;");
                    data.put("slid", "&nbsp;");
                    data.put("vdate", "&nbsp;");

                    data.put("slname", slname);
                    data.put("payordernumberdate", "&nbsp;");
                    data.put("bankpayvnumberdate", "&nbsp;");
                    data.put("particulars", "Opening Balance");

                    for (int k = 0; k < reqSubLedgerId.length; k++)
                        if (reqSubLedgerId[k].equals(slDetailKeyId)) {
                            data.put("payamount",
                                    formatAmtTwoDecimal(reqSubLedgerOB[k])
                                    .toString());
                            data.put("closingBal",
                                    formatAmtTwoDecimal(reqSubLedgerOB[k])
                                    .toString());

                            if (!reqSubLedgerOB[k].equals("0")
                                    && !reqSubLedgerOB[k].equals("")) {
                                if (LOGGER.isInfoEnabled())
                                    LOGGER.info("Inside If 2");
                                colTotalCBValue = new BigDecimal(
                                        reqSubLedgerOB[k]);
                            } else {
                                if (LOGGER.isInfoEnabled())
                                    LOGGER.info("Inside else 2");
                                colTotalCBValue = new BigDecimal("0.00");
                            }
                        }

                    data.put("recovnumberdate", "&nbsp;");
                    data.put("recovamount", "&nbsp;");
                    data.put("remarks", "&nbsp;");

                    data.put("serialNo", i + "");
                    i++;

                    arList.add(data);

                    // first record start for particular subledger name

                    data = new HashMap();

                    if (element[2].toString() != null)
                        sltype = element[2].toString();
                    else
                        sltype = "&nbsp;";

                    if (element[3].toString() != null)
                        slid = element[3].toString();
                    else
                        slid = "&nbsp;";

                    // if(LOGGER.isInfoEnabled())
                    // LOGGER.info("remarks value is---"+element[4].toString());

                    if (element[4].toString() != null) {
                        if (element[4].toString().trim().length() > 0)
                            remarks = element[4].toString();
                        else
                            remarks = "&nbsp;";
                    } else
                        remarks = "&nbsp;";

                    if (element[5].toString() != null)
                        vdate = element[5].toString();
                    else
                        vdate = "&nbsp;";

                    if (element[6].toString() != null)
                        slname = element[6].toString();
                    else
                        slname = "&nbsp;";
                    if (element[7].toString() != null)
                        payordernumberdate = element[7].toString();
                    else
                        payordernumberdate = "&nbsp;";

                    if (element[8].toString() != null)
                        bankpayvnumberdate = element[8].toString();
                    else
                        bankpayvnumberdate = "&nbsp;";

                    if (element[9].toString() != null)
                        particulars = element[9].toString();
                    else
                        particulars = "&nbsp;";

                    if (element[10].toString() != null) {
                        payamount = formatAmtTwoDecimal(element[10].toString())
                                .toString();
                        colTotalPrevPaymentValue = new BigDecimal(
                                element[10].toString());
                    } else {
                        payamount = "&nbsp;";
                        colTotalPrevPaymentValue = new BigDecimal("0.00");
                    }

                    if (element[11].toString() != null)
                        recovnumberdate = element[11].toString();
                    else
                        recovnumberdate = "&nbsp;";

                    if (element[12].toString() != null) {
                        recovamount = formatAmtTwoDecimal(
                                element[12].toString()).toString();
                        colTotalPrevRecoveryValue = new BigDecimal(
                                element[12].toString());

                    } else {
                        recovamount = "&nbsp;";
                        colTotalPrevRecoveryValue = new BigDecimal("0.00");
                    }

                    colTotalPrevCB = colTotalPrevCB.add(colTotalCBValue);

                    colTotalPrevCB = colTotalPrevCB
                            .add(colTotalPrevPaymentValue);
                    colTotalPrevCB = colTotalPrevCB
                            .subtract(colTotalPrevRecoveryValue);

                    data.put("serialNo", "&nbsp;");
                    data.put("sltype", sltype);
                    data.put("slid", slid);
                    data.put("vdate", vdate);

                    data.put("slname", "&nbsp;");
                    data.put("payordernumberdate", payordernumberdate);
                    data.put("bankpayvnumberdate", bankpayvnumberdate);
                    data.put("particulars", particulars);

                    data.put("payamount", payamount);
                    data.put("recovnumberdate", recovnumberdate);
                    data.put("recovamount", recovamount);
                    data.put("remarks", remarks);
                    data.put("closingBal",
                            formatAmtTwoDecimal(colTotalPrevCB.toString()));

                    arList.add(data);

                }
            }// Main while

            // This is for those subledger having only opening balance details
            // --Starts
            if (!subLedgerDetails) {

                if (LOGGER.isInfoEnabled())
                    LOGGER.info("This is for those subledger having only opening balance details");
                final HashMap data = new HashMap();

                data.put("serialNo", "1");
                data.put("sltype", "&nbsp;");
                data.put("slid", "&nbsp;");
                data.put("vdate", "&nbsp;");

                data.put("payordernumberdate", "&nbsp;");
                data.put("bankpayvnumberdate", "&nbsp;");
                data.put("particulars", "Opening Balance");

                for (int k = 0; k < reqSubLedgerId.length; k++) {
                    data.put("slname", reqSubLedgerName[k].toString());
                    data.put("payamount",
                            formatAmtTwoDecimal(reqSubLedgerOB[k]).toString());
                    data.put("closingBal",
                            formatAmtTwoDecimal(reqSubLedgerOB[k]).toString());
                }
                data.put("recovnumberdate", "&nbsp;");
                data.put("recovamount", "&nbsp;");
                data.put("remarks", "&nbsp;");
                arList.add(data);

            }// This is for those subledger having only opening balance details
            // ----Ends

        } catch (final Exception sqlE) {
            LOGGER.error("Exception in getSubLedgerListForAdvRegister :" + sqlE);
            throw taskExc;
        }

    }

    public void isCurDate(final String VDate) throws TaskFailedException {

        final EGovernCommon egc = new EGovernCommon();
        try {
            final String today = egc.getCurrentDate();
            final String[] dt2 = today.split("/");
            final String[] dt1 = VDate.split("/");
            final int ret = Integer.parseInt(dt2[2]) > Integer.parseInt(dt1[2]) ? 1
                    : Integer.parseInt(dt2[2]) < Integer.parseInt(dt1[2]) ? -1
                            : Integer.parseInt(dt2[1]) > Integer
                            .parseInt(dt1[1]) ? 1 : Integer
                                    .parseInt(dt2[1]) < Integer
                                    .parseInt(dt1[1]) ? -1 : Integer
                                            .parseInt(dt2[0]) > Integer
                                            .parseInt(dt1[0]) ? 1 : Integer
                                                    .parseInt(dt2[0]) < Integer
                                                    .parseInt(dt1[0]) ? -1 : 0;
                                            if (ret == -1)
                                                throw taskExc;
        } catch (final Exception ex) {
            LOGGER.error("Exception in isCurDate " + ex);
            throw new TaskFailedException(
                    "Date Should be within the today's date");
        }
    }

    public String formatAmtTwoDecimal(final String amt) {
        NumberFormat formatter;
        formatter = new DecimalFormat("##############0.00");
        return formatter.format(new BigDecimal(amt));
    }

}
