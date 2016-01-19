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
 * Created on Dec 21, 2005
 * @author Sumit
 */
package com.exilant.eGov.src.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;

import com.exilant.GLEngine.GeneralLedgerBean;
import com.exilant.eGov.src.chartOfAccounts.CodeValidator;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.transactions.ExilPrecision;
import com.exilant.exility.common.TaskFailedException;

class OpBal {
    public double dr;
    public double cr;
}

public class GeneralLedgerReportList {
    Connection connection = null;
    PreparedStatement pstmt = null;
    ResultSet resultset = null;
    ResultSet resultset1 = null;
    TaskFailedException taskExc;
    String startDate, endDate, effTime, rType = "gl";
    private static final Logger LOGGER = Logger.getLogger(GeneralLedgerReportList.class);

    // private final static String datasource = "java:/ezgovOraclePool";
    public GeneralLedgerReportList() {
    }

    public LinkedList getGeneralLedgerList(final GeneralLedgerReportBean reportBean)
            throws TaskFailedException {

        final LinkedList dataList = new LinkedList();

        try
        {
            connection = null;
            // if(LOGGER.isDebugEnabled()) LOGGER.debug("connection"+connection);

        } catch (final Exception e)
        {
            LOGGER.error(e.getMessage(), e);
            throw new TaskFailedException();
        }
        String isconfirmed = "";
        String glCode1 = "";
        String glCode2 = "";
        taskExc = new TaskFailedException();
        final EGovernCommon egc = new EGovernCommon();

        NumberFormat formatter = new DecimalFormat();
        formatter = new DecimalFormat("###############.00");

        glCode1 = reportBean.getGlCode1();
        glCode2 = reportBean.getGlCode2();
        final String maxlength = EGovConfig.getProperty("egf_config.xml", "glcodeMaxLength", "", "AccountCode");
        if (glCode1.length() != Integer.parseInt(maxlength) && glCode2.length() != Integer.parseInt(maxlength)) {
            glCode1 = getMinCode(glCode1);
            glCode2 = getMaxCode(glCode2);

        }
        try {
            final String snapShotDateTime = reportBean.getSnapShotDateTime();
            if (snapShotDateTime.equalsIgnoreCase(""))
                effTime = "";
            else
                effTime = egc.getEffectiveDateFilter(snapShotDateTime);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw taskExc;
        }

        // showRev=reportBean.getForRevEntry();
        // if(LOGGER.isDebugEnabled()) LOGGER.debug("showRev "+showRev);
        final String fundId = reportBean.getFund_id();
        final String deptId = reportBean.getDepartmentId();
        final String fundSourceId = reportBean.getFundSource_id();

        String formstartDate = "";
        String formendDate = "";
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
        Date dt = new Date();
        final String endDate1 = reportBean.getEndDate();
        isCurDate(connection, endDate1);
        try {
            endDate = reportBean.getEndDate();
            dt = sdf.parse(endDate);
            formendDate = formatter1.format(dt);
        } catch (final Exception e) {
            LOGGER.error("inside the try-startdate" + e);
        }
        try
        {
            startDate = reportBean.getStartDate();
            if (!startDate.equalsIgnoreCase("null")) {
                dt = sdf.parse(startDate);
                formstartDate = formatter1.format(dt);
            }
        }

        catch (final Exception e) {
            LOGGER.error("inside the try-startdate" + e, e);
        }

        if (startDate.equalsIgnoreCase("null")) {
            final String finId = getFYID(formendDate);
            startDate = getStartDate(finId);
        } else
            startDate = formstartDate;
        // if(LOGGER.isDebugEnabled()) LOGGER.debug("startDate22 "+startDate);
        endDate = formendDate;
        final String startDateformat = startDate;
        String startDateformat1 = "";
        try {
            dt = formatter1.parse(startDateformat);
            startDateformat1 = sdf.format(dt);
        } catch (final Exception e) {
            LOGGER.error("Parse Exception" + e, e);
        }
        setDates(startDate, endDate);
        final String fyId = getFYID(endDate);
        if (fyId.equalsIgnoreCase("")) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Financial Year Not Valid");
            throw taskExc;
        }

        if (!isValidCode(glCode1))
        {
            if (LOGGER.isInfoEnabled())
                LOGGER.info(glCode1 + " Not Valid");

            throw taskExc;
        }
        if (!isValidCode(glCode2))
        {
            if (LOGGER.isInfoEnabled())
                LOGGER.info(glCode2 + " Not Valid");

            throw taskExc;
        }

        double txnDrSum = 0, txnCrSum = 0, openingBalance = 0, closingBalance = 0;
        final String query = getQuery(glCode1, glCode2, fundId, fundSourceId, startDate, endDate);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("**************QUERY: " + query);

        try {

            pstmt = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultset1 = pstmt.executeQuery();
            final ArrayList data = new ArrayList();
            String accCode = "", vcNum = "", vcDate = "", type = "", narration = "";
            StringBuffer detail = new StringBuffer();
            StringBuffer amount = new StringBuffer();
            int vhId = 0, curVHID = 0, cout = 0, cout1 = 0, VhidPrevious = 0, lenAfterAppend = 0, lenBeforeAppend = 0, lenDetailBefore = 0, lenDetailAfter = 0;
            double txnDebit = 0, txnCredit = 0, previousDebit = 0, previousCredit = 0;
            String code = "", currCode = "", accCodePrevious = "";
            /**
             * When using ResultSet.TYPE_SCROLL_INSENSITIVE in createStatement if no records are there, rs.next() will return true
             * but when trying to access (rs.getXXX()), it will throw an error
             **/
            // nameOfGrid = "gridBookDetail";
            // int cnt = 0;
            int totalCount = 0, isConfirmedCount = 0;
            String vn2 = "";
            String glType = "";
            while (resultset1.next())
            {
                // if(LOGGER.isDebugEnabled()) LOGGER.debug(" inside resultset");
                try {
                    code = resultset1.getString("code");
                    isconfirmed = resultset1.getString("isconfirmed");
                    // 9 is the dummy value used in the query
                    // To display X in Y are unconfirmed
                    if (isconfirmed != null && !isconfirmed.equalsIgnoreCase("") && !isconfirmed.equalsIgnoreCase("9"))
                    {
                        final String vn1 = resultset1.getString("vouchernumber");
                        if (!vn1.equalsIgnoreCase(vn2))
                        {
                            vn2 = vn1;
                            totalCount = totalCount + 1;
                            if (isconfirmed.equalsIgnoreCase("0"))
                                isConfirmedCount = isConfirmedCount + 1;
                        }
                    }
                    /**
                     * GLCODES changing here.Paint the previous GLCODE particular txn and the clsoing balance. Dnt take the cout1
                     * = 1 record.
                     */
                    if (!currCode.trim().equals("") && !code.equals(currCode) && cout1 == 0)
                    {
                        cout = 1;
                        final String arrl[] = new String[13]; // 12th item in array is used for chequeinhand report to display
                        // cheque-payeename list
                        arrl[0] = "&nbsp;" + vcDate;
                        arrl[1] = vcNum;
                        arrl[2] = detail.toString();
                        arrl[3] = "&nbsp;" + type;
                        arrl[4] = numberToString(((Double) txnDebit).toString()) + "";
                        arrl[5] = numberToString(((Double) txnCredit).toString()) + "";
                        if (narration == null)
                            arrl[6] = "&nbsp;";
                        else
                            arrl[6] = "&nbsp;" + narration;
                        txnDrSum = txnDrSum + txnDebit;
                        txnCrSum = txnCrSum + txnCredit;
                        arrl[8] = arrl[9] = "&nbsp;";
                        arrl[4] = arrl[4].equalsIgnoreCase(".00") ? "&nbsp;" : arrl[4];
                        arrl[5] = arrl[5].equalsIgnoreCase(".00") ? "&nbsp;" : arrl[5];
                        if (amount.length() > 2)
                            arrl[7] = amount.toString().substring(0, amount.length() - 4);
                        else
                            arrl[7] = "&nbsp;";

                        // To show running Balance
                        if (reportBean.getReportType().equalsIgnoreCase("generalledger")
                                && reportBean.getForRunningBalance().equalsIgnoreCase("on")
                                && (glType.equalsIgnoreCase("A") || glType.equalsIgnoreCase("E")))
                        {
                            // if(LOGGER.isDebugEnabled()) LOGGER.debug(" getReportType().equalsIgnoreCase(generalledger) ");

                            final double runningBal = openingBalance + txnDrSum - txnCrSum;
                            arrl[10] = formatter.format(runningBal) + "";
                            // if(LOGGER.isDebugEnabled()) LOGGER.debug(" arrl[10] "+arrl[10]);
                        }
                        if (reportBean.getReportType().equalsIgnoreCase("generalledger")
                                && reportBean.getForRunningBalance().equalsIgnoreCase("on")
                                && (glType.equalsIgnoreCase("I") || glType.equalsIgnoreCase("L")))
                        {
                            // if(LOGGER.isDebugEnabled()) LOGGER.debug(" getReportType().equalsIgnoreCase(generalledger) ");

                            final double runningBal = -openingBalance + txnCrSum - txnDrSum;
                            arrl[10] = formatter.format(runningBal) + "";
                            // if(LOGGER.isDebugEnabled()) LOGGER.debug(" arrl[10] "+arrl[10]);
                        }
                        arrl[11] = "&nbsp;";
                        arrl[12] = "&nbsp;";
                        // End

                        data.add(arrl);// **
                        detail.delete(0, detail.length());
                        amount.delete(0, amount.length());
                        // cnt = 0;
                        vcDate = vcNum = accCode = type = narration = "";
                        final String arr[] = new String[13];
                        if (openingBalance > 0)
                            txnDrSum = txnDrSum + Math.abs(openingBalance);
                        else
                            txnCrSum = txnCrSum + Math.abs(openingBalance);
                        closingBalance = txnDrSum - txnCrSum;
                        // if(LOGGER.isDebugEnabled()) LOGGER.debug("Opening Balance Dr-Cr: " + formatter.format(openingBalance));
                        // if(LOGGER.isDebugEnabled()) LOGGER.debug("Closing Balance Dr-Cr: " + formatter.format(closingBalance));
                        if (closingBalance > 0) {
                            txnCrSum = txnCrSum + Math.abs(closingBalance);
                            arr[4] = "&nbsp;";
                        } else if (closingBalance < 0) {
                            txnDrSum = txnDrSum + Math.abs(closingBalance);
                            arr[4] = numberToString(((Double) Math.abs(closingBalance)).toString()).toString();
                            arr[5] = "&nbsp;";
                        } else {
                            arr[4] = "&nbsp;";
                            arr[5] = "&nbsp;";
                        }
                        arr[2] = "Closing Balance";
                        arr[1] = "";
                        arr[0] = arr[3] = arr[6] = arr[7] = arr[8] = arr[9] = arr[10] = arr[11] = arr[12] = "&nbsp;";
                        data.add(arr);

                    }
                    if (!currCode.trim().equals("") && !code.equals(currCode) && cout1 == 0)
                    {
                        final String arr[] = new String[13];
                        if (txnDrSum > 0)
                            arr[4] = "<b>" + numberToString(((Double) txnDrSum).toString()) + "</b>";
                        else
                            arr[4] = "&nbsp;";
                        if (txnCrSum > 0)
                            arr[5] = "<b>" + numberToString(((Double) txnDrSum).toString()) + "</b>";
                        else
                            arr[5] = "&nbsp;";
                        arr[2] = "<b>Total</b>";
                        arr[1] = "";
                        arr[0] = arr[3] = arr[6] = arr[7] = arr[8] = arr[9] = arr[10] = arr[11] = arr[12] = "&nbsp;";
                        data.add(arr);
                        txnDrSum = 0;
                        txnCrSum = 0;

                    }
                    cout1 = 0;
                    vhId = resultset1.getInt("vhid");
                    /**
                     * When the main GLCODES are changing.We need to get the opening balance first.
                     */
                    if (!code.equals(currCode))
                    {
                        glType = resultset1.getString("glType");
                        // if(LOGGER.isDebugEnabled()) LOGGER.debug(" inside code.equals(currCode)");
                        final String arr[] = new String[13];
                        final OpBal opbal = getOpeningBalance(code, fundId, fundSourceId, fyId, startDate, deptId);
                        openingBalance = opbal.dr - opbal.cr;
                        String fundName = "";
                        if (resultset1.getString(14) != null)
                            fundName = resultset1.getString(14);
                        final String str = "select name as \"glname\" from chartofaccounts where glcode=?";
                        pstmt = connection.prepareStatement(str);
                        pstmt.setString(0, code);
                        final ResultSet res = pstmt.executeQuery();
                        String aName = "";
                        if (res.next()) {
                            if (res.getString("glname") != null)
                                aName = res.getString("glname");
                        }
                        else if (LOGGER.isDebugEnabled())
                            LOGGER.debug("In else block of getting glname");
                        res.close();
                        arr[1] = "";
                        arr[0] = arr[2] = arr[3] = arr[6] = arr[7] = arr[10] = arr[11] = arr[12] = "&nbsp;";
                        if (vhId == 0)
                            arr[8] = "&nbsp;";
                        else
                            arr[8] = fundName;
                        arr[9] = code + "-" + aName;
                        if (openingBalance > 0) {
                            arr[4] = numberToString(((Double) Math.abs(openingBalance)).toString()).toString();
                            arr[5] = "&nbsp;";
                        }
                        else if (openingBalance < 0) {
                            arr[4] = "&nbsp;";
                            arr[5] = numberToString(((Double) Math.abs(openingBalance)).toString()).toString();
                        } else {
                            arr[4] = "&nbsp;";
                            arr[5] = "&nbsp;";
                        }
                        arr[2] = "Opening Balance";
                        if (vhId == 0 && !(openingBalance > 0 || openingBalance < 0))
                        {
                            if (LOGGER.isDebugEnabled())
                                LOGGER.debug("Inside if...");
                        }
                        else
                            data.add(arr);
                        /**
                         * Those glcodes for which there is no transaction in the general ledger for the date range.We are getting
                         * the closing balance. cout1 is set to 1.
                         */
                        if (vhId == 0)
                        {
                            final String arr1[] = new String[13];
                            if (openingBalance > 0)
                            {
                                arr1[4] = "&nbsp;";
                                arr1[5] = numberToString(((Double) Math.abs(openingBalance)).toString()).toString();
                            }
                            else if (openingBalance < 0) {
                                arr1[4] = numberToString(((Double) Math.abs(openingBalance)).toString()).toString();
                                arr1[5] = "&nbsp;";
                            } else {
                                arr1[4] = "0";
                                arr1[5] = "0";
                            }
                            arr1[2] = "Closing Balance";
                            arr1[1] = "";
                            arr1[0] = arr1[3] = arr1[6] = arr1[7] = arr1[8] = arr1[9] = arr1[10] = arr1[11] = arr1[12] = "&nbsp;";
                            if (openingBalance > 0 || openingBalance < 0)
                                data.add(arr1);

                            final String arr2[] = new String[13];
                            if (openingBalance > 0)
                            {
                                arr2[4] = numberToString(((Double) Math.abs(openingBalance)).toString()).toString();
                                arr2[5] = numberToString(((Double) Math.abs(openingBalance)).toString()).toString();
                            }
                            else if (openingBalance < 0)
                            {
                                arr2[4] = numberToString(((Double) Math.abs(openingBalance)).toString()).toString();
                                arr2[5] = numberToString(((Double) Math.abs(openingBalance)).toString()).toString();
                            } else {
                                arr2[4] = "0";
                                arr2[5] = "0";
                            }
                            arr2[2] = "<b>Total</b>";
                            arr2[1] = "";
                            arr2[0] = arr2[3] = arr2[6] = arr2[7] = arr2[8] = arr2[9] = arr2[10] = arr2[11] = arr2[12] = "&nbsp;";
                            if (openingBalance > 0 || openingBalance < 0)
                                data.add(arr2);
                            cout1 = 1;
                            continue;
                        }
                        // assign to temp varable,
                        currCode = code;
                    }// End If glcodes changing

                } catch (final SQLException ex) {
                    LOGGER.error("ERROR (not an error): ResultSet is Empty", ex);
                }
                // Vouchers are changing
                if (curVHID > 0 && vhId != curVHID && cout == 0 && vhId != 0)
                {
                    // if(LOGGER.isDebugEnabled()) LOGGER.debug("  reportBean.getReportType()  "+reportBean.getReportType());
                    previousDebit = 0;
                    previousCredit = 0;
                    PreparedStatement pstmt1 = null;
                    lenAfterAppend = 0;
                    lenBeforeAppend = 0;
                    lenDetailBefore = 0;
                    lenDetailAfter = 0;
                    final String arr9[] = new String[13];
                    arr9[0] = vcDate;
                    arr9[1] = vcNum;
                    String cheList = "";
                    if (reportBean.getReportType().equalsIgnoreCase("ChequeInHand"))
                    {
                        // if(LOGGER.isDebugEnabled()) LOGGER.debug("   @@@@@@@@@@  inside");
                        ResultSet r1 = null;
                        try {
                            final String strQry = "select distinct chequenumber,payto from chequedetail c,voucherheader v where v.id=c.voucherheaderid and v.status<>4 and v.vouchernumber= ? and v.voucherdate= ? ";
                            pstmt1 = connection.prepareStatement(strQry);
                            pstmt1.setString(0, vcNum);
                            pstmt1.setString(1, vcDate);
                            r1 = pstmt1.executeQuery();

                            while (r1.next())
                                cheList = cheList
                                + "<br>"
                                + r1.getString(1)
                                + (r1.getString(2) == null || r1.getString(2).equals("") ? ", " : "-" + r1.getString(2)
                                        + ", ");

                            if (LOGGER.isInfoEnabled())
                                LOGGER.info("   @@@@@@@@@@  cheList  " + cheList);
                        }

                        catch (final Exception e) {
                            LOGGER.error("Exp=" + e.getMessage(), e);
                        } finally {
                            r1.close();
                            pstmt1.close();
                        }
                        if (!cheList.trim().equals(""))
                            cheList = cheList.trim().substring(0, cheList.length() - 1);
                    }
                    arr9[2] = detail.toString();
                    arr9[3] = type;
                    arr9[4] = numberToString(((Double) txnDebit).toString()) + "";
                    arr9[5] = numberToString(((Double) txnCredit).toString()) + "";
                    if (reportBean.getReportType().equalsIgnoreCase("ChequeInHand")) {
                        arr9[6] = "&nbsp;" + narration;
                        arr9[12] = "&nbsp;" + cheList;
                    } else {
                        if (narration != null)
                            arr9[6] = "&nbsp;" + narration + " - Cheque#:" + cheList;
                        else
                            arr9[6] = "&nbsp;";
                        arr9[12] = "&nbsp;";
                    }
                    if (amount.length() > 2)
                        arr9[7] = amount.toString().substring(0, amount.length() - 4);
                    else
                        arr9[7] = "&nbsp;";

                    txnDrSum = txnDrSum + txnDebit;
                    txnCrSum = txnCrSum + txnCredit;

                    // To show running Balance
                    if (reportBean.getReportType().equalsIgnoreCase("generalledger")
                            && reportBean.getForRunningBalance().equalsIgnoreCase("on")
                            && (glType.equalsIgnoreCase("A") || glType.equalsIgnoreCase("E")))
                    {
                        // if(LOGGER.isDebugEnabled()) LOGGER.debug(" getReportType().equalsIgnoreCase(generalledger) ");

                        final double runningBal = openingBalance + txnDrSum - txnCrSum;
                        arr9[10] = numberToString(((Double) runningBal).toString()) + "";
                        // if(LOGGER.isDebugEnabled()) LOGGER.debug(" arr9[10] "+arr9[10]);
                    }
                    if (reportBean.getReportType().equalsIgnoreCase("generalledger")
                            && reportBean.getForRunningBalance().equalsIgnoreCase("on")
                            && (glType.equalsIgnoreCase("I") || glType.equalsIgnoreCase("L")))
                    {
                        // if(LOGGER.isDebugEnabled()) LOGGER.debug(" getReportType().equalsIgnoreCase(generalledger) ");

                        final double runningBal = -openingBalance + txnCrSum - txnDrSum;
                        arr9[10] = numberToString(((Double) runningBal).toString()) + "";
                        // if(LOGGER.isDebugEnabled()) LOGGER.debug(" arr9[10] "+arr9[10]);
                    }
                    arr9[11] = "&nbsp;";

                    // End

                    arr9[8] = arr9[9] = "&nbsp;";
                    data.add(arr9);
                    arr9[4] = arr9[4].equalsIgnoreCase(".00") ? "&nbsp;" : arr9[4];
                    arr9[5] = arr9[5].equalsIgnoreCase(".00") ? "&nbsp;" : arr9[5];

                    detail.delete(0, detail.length());
                    amount.delete(0, amount.length());
                    // cnt = 0;
                    vcDate = vcNum = accCode = type = narration = "";
                    // if(LOGGER.isDebugEnabled()) LOGGER.debug("  ENDING %%%%");

                }// End If
                curVHID = vhId;
                cout = 0;
                accCode = resultset1.getString("glcode");
                if (vhId != 0)
                    // if(LOGGER.isDebugEnabled()) LOGGER.debug(" inside vhId != 0");
                    // get the details other than patriculars
                    if (accCode.equalsIgnoreCase(code))
                    {
                        double currentDebit = 0, currentCredit = 0, debit = 0, credit = 0;
                        if (vhId == VhidPrevious && accCode.equalsIgnoreCase(accCodePrevious)) {
                            vcDate = resultset1.getString("voucherdate");
                            vcNum = resultset1.getString("vouchernumber");
                            type = resultset1.getString("type");
                            currentDebit = resultset1.getDouble("debitamount");
                            currentCredit = resultset1.getDouble("creditamount");
                            debit = previousDebit + currentDebit - (previousCredit + currentCredit);
                            if (debit > 0)
                                txnDebit = debit;
                            else
                                txnDebit = 0;
                            credit = previousCredit + currentCredit - (previousDebit + currentDebit);
                            if (credit > 0)
                                txnCredit = credit;
                            else
                                txnCredit = 0;
                            narration = resultset1.getString("narration");
                            previousDebit = currentDebit;
                            previousCredit = currentCredit;
                        }
                        else {
                            vcDate = resultset1.getString("voucherdate");
                            vcNum = resultset1.getString("vouchernumber");
                            type = resultset1.getString("type");
                            txnDebit = resultset1.getDouble("debitamount");
                            previousDebit = txnDebit;
                            txnCredit = resultset1.getDouble("creditamount");
                            previousCredit = txnCredit;
                            narration = resultset1.getString("narration");
                        }
                    } else if (vhId == VhidPrevious && accCode.equalsIgnoreCase(accCodePrevious)) {
                        double currentDebit = 0, currentCredit = 0, debit = 0, credit = 0;
                        String debitAmount = "", creditAmount = "";
                        String bLine = "<br><br>";
                        final String lengthName = resultset1.getString("name");
                        final int len = lengthName.length();
                        for (int i = 30; i < len; i = i + 30)
                            bLine = bLine + "<br>";
                        amount.delete(lenBeforeAppend, lenAfterAppend);
                        detail.delete(lenDetailBefore, lenDetailAfter);
                        detail = detail.append(" " + resultset1.getString("name") + "<br><br>");
                        currentDebit = resultset1.getDouble("debitamount");
                        currentCredit = resultset1.getDouble("creditamount");
                        debit = previousDebit + currentDebit - (previousCredit + currentCredit);
                        if (debit > 0) {
                            debitAmount = "Dr." + ExilPrecision.convertToString(debit, 2) + "0";
                            amount = amount.append(" " + debitAmount + bLine);
                        }
                        credit = previousCredit + currentCredit - (previousDebit + currentDebit);
                        if (credit > 0) {
                            creditAmount = "Cr." + ExilPrecision.convertToString(credit, 2) + "0";
                            amount = amount.append(" " + creditAmount + bLine);
                        }

                    } else {
                        String bLine = "<br><br>";
                        final String lengthName = resultset1.getString("name");
                        final int len = lengthName.length();
                        for (int i = 30; i < len; i = i + 30)
                            bLine = bLine + "<br>";
                        lenDetailBefore = detail.length();
                        detail = detail.append(" " + resultset1.getString("name") + "<br><br>");
                        lenDetailAfter = detail.length();
                        lenBeforeAppend = amount.length();
                        amount = amount.append(" " + resultset1.getString("amount") + bLine);
                        lenAfterAppend = amount.length();
                        previousDebit = resultset1.getDouble("debitamount");
                        previousCredit = resultset1.getDouble("creditamount");
                    }

                accCodePrevious = accCode;
                VhidPrevious = vhId;
                if (resultset1.isLast())
                {

                    final String arr[] = new String[13];
                    arr[0] = vcDate;
                    arr[1] = vcNum;
                    String cheList = "";
                    /*
                     * if(narration!=null && !narration.equalsIgnoreCase("") && narration.trim().length()>0) { cheList=" - "; }
                     */if (reportBean.getReportType().equalsIgnoreCase("ChequeInHand"))
                     {
                         PreparedStatement pstmt2 = null;
                         ResultSet r2 = null;
                         final String cheqInHandQry = "select distinct chequenumber,payto from chequedetail c,voucherheader v where v.id=c.voucherheaderid and v.vouchernumber=? and v.voucherdate=? ";
                         try {
                             pstmt2 = connection.prepareStatement(cheqInHandQry);
                             pstmt2.setString(0, vcNum);
                             pstmt2.setString(1, vcDate);

                             r2 = pstmt2.executeQuery();
                             // String temp="";
                             while (r2.next())
                                 cheList = cheList
                                 + "<br>"
                                 + r2.getString(1)
                                 + (r2.getString(2) == null || r2.getString(2).equals("") ? ", " : "-" + r2.getString(2)
                                         + ", ");
                             if (LOGGER.isInfoEnabled())
                                 LOGGER.info("chequelist:" + cheList);
                             // if(temp.length()>0)
                             // cheList = cheList+"Cheque# :"+temp;
                         } catch (final Exception e) {
                             LOGGER.error("Exp=" + e.getMessage(), e);
                         } finally {

                             r2.close();
                             pstmt2.close();
                         }
                         if (!cheList.trim().equals(""))
                             cheList = cheList.trim().substring(0, cheList.length() - 1);
                     }
                     arr[2] = detail.toString();
                     arr[3] = type;
                     arr[4] = numberToString(((Double) txnDebit).toString()) + "";
                     arr[5] = numberToString(((Double) txnCredit).toString()) + "";
                     if (reportBean.getReportType().equalsIgnoreCase("ChequeInHand")) {
                         arr[6] = "&nbsp;" + narration;
                         arr[12] = "&nbsp;" + cheList;
                     } else {
                         if (narration != null)
                             arr[6] = "&nbsp;" + narration + " - Cheque#:" + cheList;
                         else
                             arr[6] = "&nbsp;";
                         arr[12] = "&nbsp;";
                     }

                     txnDrSum = txnDrSum + txnDebit;
                     txnCrSum = txnCrSum + txnCredit;
                     arr[8] = arr[9] = "&nbsp;";
                     arr[4] = arr[4].equalsIgnoreCase(".00") ? "&nbsp;" : arr[4];
                     arr[5] = arr[5].equalsIgnoreCase(".00") ? "&nbsp;" : arr[5];
                     if (amount.length() > 2)
                         arr[7] = amount.toString().substring(0, amount.length() - 4);
                     else
                         arr[7] = "&nbsp;";

                     // To show running Balance
                     if (reportBean.getReportType().equalsIgnoreCase("generalledger")
                             && reportBean.getForRunningBalance().equalsIgnoreCase("on")
                             && (glType.equalsIgnoreCase("A") || glType.equalsIgnoreCase("E")))
                     {
                         final double runningBal = openingBalance + txnDrSum - txnCrSum;
                         arr[10] = numberToString(((Double) runningBal).toString()) + "";
                         // if(LOGGER.isDebugEnabled()) LOGGER.debug(" arr[10] "+arr[10]);
                     }
                     if (reportBean.getReportType().equalsIgnoreCase("generalledger")
                             && reportBean.getForRunningBalance().equalsIgnoreCase("on")
                             && (glType.equalsIgnoreCase("I") || glType.equalsIgnoreCase("L")))
                     {
                         final double runningBal = -openingBalance + txnCrSum - txnDrSum;
                         arr[10] = numberToString(((Double) runningBal).toString()) + "";
                         // if(LOGGER.isDebugEnabled()) LOGGER.debug(" arr[10] "+arr[10]);
                     }
                     arr[11] = "&nbsp;";

                     // End
                     data.add(arr);
                     detail.delete(0, detail.length());
                     amount.delete(0, amount.length());
                     // cnt = 0;
                     vcDate = vcNum = accCode = type = narration = "&nbsp;";

                     final String arr2[] = new String[13];
                     if (openingBalance > 0)
                         txnDrSum = txnDrSum + Math.abs(openingBalance);
                     else
                         txnCrSum = txnCrSum + Math.abs(openingBalance);
                     closingBalance = txnDrSum - txnCrSum;
                     if (closingBalance > 0) {
                         txnCrSum = txnCrSum + Math.abs(closingBalance);
                         arr2[4] = "&nbsp;";
                         arr2[5] = numberToString(((Double) Math.abs(closingBalance)).toString()).toString();
                     } else if (closingBalance < 0) {
                         txnDrSum = txnDrSum + Math.abs(closingBalance);
                         arr2[4] = numberToString(((Double) Math.abs(closingBalance)).toString()).toString();
                         arr2[5] = "&nbsp;";
                     } else {
                         arr2[4] = "&nbsp;";
                         arr2[5] = "&nbsp;";
                     }
                     arr2[2] = "Closing Balance";
                     arr2[1] = "";
                     arr2[0] = arr2[3] = arr2[6] = arr2[7] = arr2[8] = arr2[9] = arr2[10] = arr2[11] = arr2[12] = "&nbsp;";
                     data.add(arr2);

                     final String arr1[] = new String[13];
                     if (txnDrSum > 0)
                         arr1[4] = "<hr><b>" + numberToString(((Double) txnDrSum).toString()) + "</b><hr>";
                     else
                         arr1[4] = "<hr>&nbsp;<hr>";
                     if (txnCrSum > 0)
                         arr1[5] = "<hr><b>" + numberToString(((Double) txnDrSum).toString()) + "</b><hr>";
                     else
                         arr1[5] = "<hr>&nbsp;<hr>";
                     arr1[2] = "<hr><b>Total</b><hr>";
                     arr1[0] = arr1[1] = arr1[3] = arr1[6] = arr1[7] = arr1[8] = arr1[9] = arr1[10] = arr1[11] = arr1[12] = "<hr>&nbsp;<hr>";
                     data.add(arr1);
                     txnDrSum = 0;
                     txnCrSum = 0;

                }// End If last
            }// End While

            // Adding data to 2 dimension array to pass to dc
            final String gridData[][] = new String[data.size() + 1][13];
            gridData[0][0] = "voucherdate";
            gridData[0][1] = "vouchernumber";
            // gridData[0][2] = "glcode";
            gridData[0][2] = "name";
            gridData[0][3] = "type";
            gridData[0][4] = "debitamount";
            gridData[0][5] = "creditamount";
            gridData[0][6] = "narration";
            gridData[0][7] = "amount";
            gridData[0][8] = "fund";
            gridData[0][9] = "glcode";
            gridData[0][10] = "debitamountR";
            gridData[0][11] = "creditamountR";
            gridData[0][12] = "chequeslist-payeename";// for chequeinhand report
            for (int i = 1; i <= data.size(); i++)
                gridData[i] = (String[]) data.get(i - 1);

            // dc.addGrid(nameOfGrid, gridData);
            // if(LOGGER.isDebugEnabled()) LOGGER.debug("\n\nGRID DATA:");
            for (int i = 1; i <= data.size(); i++)
            {
                final GeneralLedgerBean generalLedgerBean = new GeneralLedgerBean();
                generalLedgerBean.setGlcode(gridData[i][9]);
                generalLedgerBean.setVoucherdate(gridData[i][0]);
                generalLedgerBean.setVouchernumber(gridData[i][1]);
                int counter = 0;

                final String testTemp = gridData[i][2];
                final char testArrayTemp[] = testTemp.toCharArray();

                for (counter = 0; counter < testArrayTemp.length; counter++)
                    if (testArrayTemp[counter] == '<' && (testArrayTemp[counter + 1] == 'b' || testArrayTemp[counter + 1] == 'B'))
                        break;
                // String testTempSubstring = testTemp.substring(0,counter);
                generalLedgerBean.setName(gridData[i][2]);
                final String test = gridData[i][7];
                final char testArray[] = test.toCharArray();

                // int flag = 0;
                for (counter = 0; counter < testArray.length; counter++)
                    if (testArray[counter] == 'r')
                        // flag = 1;
                        break;

                // String testSubstring = test.substring(0,counter);

                generalLedgerBean.setAmount(gridData[i][7]);
                generalLedgerBean.setNarration(gridData[i][6]);
                generalLedgerBean.setType(gridData[i][3]);
                generalLedgerBean.setDebitamount(gridData[i][4]);
                generalLedgerBean.setCreditamount(gridData[i][5]);
                generalLedgerBean.setFund(gridData[i][8]);
                if (i == data.size())
                    generalLedgerBean.setCGN(getCGN("", ""));
                else
                    generalLedgerBean.setCGN(getCGN(gridData[i][1], gridData[i][0]));
                generalLedgerBean.setRunningDrCr(gridData[i][10]);
                // generalLedgerBean.setRunningCr(gridData[i][11]);
                // if(LOGGER.isDebugEnabled()) LOGGER.debug("  gridData[i][10]  "+gridData[i][10]);
                generalLedgerBean.setCheques(gridData[i][12]);
                reportBean.setStartDate(startDateformat1);
                reportBean.setTotalCount(Integer.toString(totalCount));
                reportBean.setIsConfirmedCount(Integer.toString(isConfirmedCount));
                dataList.add(generalLedgerBean);
            }

        } catch (final SQLException ex) {
            LOGGER.error("ERROR: " + ex.toString(), ex);
            throw taskExc;
        } catch (final Exception ex) {
            LOGGER.error("EXP=" + ex.getMessage(), ex);
            throw taskExc;
        }

        return dataList;
    }

    private String getQuery(final String glCode1, final String glCode2,
            final String fundId,
            final String fundSourceId,
            final String startDate,
            final String endDate) {
        String fundCondition = "";
        String fundSourceCondition = "";
        String addTableToQuery = "";

        if (!fundId.equalsIgnoreCase(""))
            fundCondition = "AND vh.fundId = " + fundId + " ";
        if (!fundSourceId.equalsIgnoreCase(""))
        {
            addTableToQuery = ", vouchermis vmis ";
            fundSourceCondition = "AND vmis.voucherheaderid=vh.id AND vmis.fundSourceId = " + fundSourceId + " ";
        }

        return "SELECT distinct gl1.glcode as \"code\",(select ca.type from chartofaccounts ca where glcode=gl1.glcode) as \"glType\",vh.id AS \"vhid\", vh.voucherDate AS \"vDate\", "
        +
        "to_char(vh.voucherDate, 'dd-Mon-yyyy') AS \"voucherdate\", "
        +
        "vh.voucherNumber AS \"vouchernumber\", gl.glCode AS \"glcode\", "
        +
        "coa.name AS \"name\",case when gl.debitAmount = 0 then (case (gl.creditamount) when 0 then gl.creditAmount||'.00cr' when floor(gl.creditamount)    then gl.creditAmount||'.00cr' else  gl.creditAmount||'cr'  end ) else  (case (gl.debitamount) when 0 then gl.debitamount||'.00dr' when floor(gl.debitamount)    then gl.debitamount||'.00dr' else  gl.debitamount||'dr' 	 end ) end AS \"amount\", "
        +
        "gl.description AS \"narration\", vh.type || '-' || vh.name||case when status = 1 then '(Reversed)' else (case when status = 2 then '(Reversal)' else '' end) end AS \"type\", "
        +
        "gl.debitamount  AS \"debitamount\", gl.creditamount  AS \"creditamount\",f.name as \"fundName\",  vh.isconfirmed as \"isconfirmed\"  "
        +
        "FROM generalLedger gl, voucherHeader vh, chartOfAccounts coa, generalLedger gl1, fund f "
        + addTableToQuery
        +
        "WHERE coa.glCode = gl.glCode AND gl.voucherHeaderId = vh.id AND gl.voucherHeaderId = vh.id "
        +
        "AND gl.voucherHeaderId = gl1.voucherHeaderId AND f.id=vh.fundId "
        + effTime
        +
        "AND gl1.glcode >='"
        + glCode1
        + "' AND gl1.glcode <='"
        + glCode2
        + "'"
        + fundCondition
        + fundSourceCondition
        +
        "AND vh.voucherDate >= '"
        + startDate
        + "' AND vh.voucherDate <= '"
        + endDate
        + "' "
        +
        "AND vh.status <> 4 "
        +
        "AND (gl.debitamount>0 OR gl.creditamount>0)  "
        +
        "union "
        +
        "SELECT coa.glcode as \"code\",coa.type as \"glType\", 0 as  \"vhid\", to_date('') AS \"vDate\", "
        +
        "'' AS \"voucherdate\", '' AS \"vouchernumber\", "
        +
        "'' AS \"glcode\", coa.name AS \"name\",null  AS \"amount\", '' AS \"narration\", "
        +
        "'' AS \"type\", null   AS \"debitamount\",  null  AS \"creditamount\",'' as \"fundName\",  9 as \"isconfirmed\" FROM  chartOfAccounts coa WHERE "
        +
        " coa.glCode not in(select glcode from generalledger gl,voucherheader vh where gl.VOUCHERHEADERID=vh.id AND vh.status <> 4 AND vh.voucherDate >= '"
        + startDate
        + "' AND vh.voucherDate <= '"
        + endDate
        + "') and glcode >='"
        + glCode1
        + "' AND glcode <='"
        + glCode2 + "' order by \"code\",\"vDate\" ";

    }

    private String getFYID(final String sDate) {
        String fyId = "";
        PreparedStatement pstmt = null;
        try {
            // for accross the financial year
            final String finYearQry = "SELECT id FROM financialYear WHERE startingDate<= ? AND endingDate>= ?";
            pstmt = connection.prepareStatement(finYearQry);
            pstmt.setString(0, sDate);
            pstmt.setString(1, sDate);
            resultset = pstmt.executeQuery();
            if (resultset.next())
                fyId = resultset.getString("id");
            resultset.close();
            pstmt.close();
        } catch (final SQLException ex) {
            fyId = "";
            LOGGER.error("Error GeneralLedger->getFYID(): " + ex.toString(), ex);
        }
        // if(LOGGER.isDebugEnabled()) LOGGER.debug("fyId: " + fyId);
        return fyId;
    }

    private OpBal getOpeningBalance(final String glCode,
            final String fundId,
            final String fundSourceId,
            final String fyId,
            final String tillDate, final String deptId) throws TaskFailedException {
        String fundCondition = "";
        String deptCondition = "";
        String fundSourceCondition = "";
        double opDebit = 0, opCredit = 0;

        /** opening balance of the Year **/
        if (!fundId.equalsIgnoreCase(""))
            fundCondition = "fundId = " + fundId + " AND ";
        if (!deptId.equalsIgnoreCase(""))
            deptCondition = "DEPARTMENTID = " + deptId + " AND ";
        if (!fundSourceId.equalsIgnoreCase(""))
            fundSourceCondition = "fundSourceId = " + fundSourceId + " AND ";

        final String queryYearOpBal = "SELECT case when sum(openingDebitBalance) = null then 0 else sum(openingDebitBalance) end  AS \"openingDebitBalance\", "
                +
                "case when sum(openingCreditBalance) = null then 0 else sum(openingCreditBalance) end  AS \"openingCreditBalance\" "
                +
                "FROM transactionSummary WHERE "
                + fundCondition
                + fundSourceCondition
                + deptCondition
                + " financialYearId="
                + fyId + " " +
                "AND glCodeId = (SELECT id FROM chartOfAccounts WHERE glCode in('" + glCode + "'))";
        // if(LOGGER.isDebugEnabled()) LOGGER.debug("***********: OPBAL: " + queryYearOpBal);
        try {
            pstmt = connection.prepareStatement(queryYearOpBal);
            resultset = null;
            resultset = pstmt.executeQuery();

            if (resultset.next()) {
                opDebit = resultset.getDouble("openingDebitBalance");
                opCredit = resultset.getDouble("openingCreditBalance");
            }
            pstmt.close();
        } catch (final SQLException ex) {
            LOGGER.error("Error GeneralLedger->getOpeningBalance() For the year: " + ex.toString(), ex);
            throw taskExc;
        }

        /** opening balance till the date from the start of the Year **/
        if (rType.equalsIgnoreCase("gl"))
        {
            final String startDate = getStartDate(fyId);
            if (!fundId.equalsIgnoreCase(""))
                fundCondition = "AND vh.fundId = " + fundId + " ";
            if (!fundSourceId.equalsIgnoreCase(""))
                fundSourceCondition = "AND vh.fundId = " + fundSourceId + " ";
            String queryTillDateOpBal = "";
            // if(showRev.equalsIgnoreCase("on")){

            queryTillDateOpBal = "SELECT case when sum(gl.debitAmount) = null then 0 else sum(gl.debitAmount) AS \"debitAmount\", " +
                    "case when sum(gl.creditAmount) = null then 0 else sum(gl.creditAmount) end AS \"creditAmount\" " +
                    "FROM generalLedger gl, voucherHeader vh " +
                    "WHERE vh.id = gl.voucherHeaderId " +
                    "AND gl.glCode in('" + glCode + "') " + fundCondition + fundSourceCondition + effTime +
                    "AND vh.voucherDate >= '" + startDate + "' AND vh.voucherDate < '" + tillDate + "' AND vh.status<>4";

            if (LOGGER.isInfoEnabled())
                LOGGER.info("***********: OPBAL: " + queryTillDateOpBal);
            try {
                pstmt = connection.prepareStatement(queryTillDateOpBal);
                resultset = null;
                resultset = pstmt.executeQuery();
                if (resultset.next()) {
                    opDebit = opDebit + resultset.getDouble("debitAmount");
                    opCredit = opCredit + resultset.getDouble("creditAmount");
                }
                pstmt.close();
            } catch (final SQLException ex) {
                LOGGER.error("Error GeneralLedger->getOpeningBalance() till the date: " + ex.toString(), ex);
                throw taskExc;
            }
        }
        /*********************************/
        final OpBal opBal = new OpBal();
        opBal.dr = opDebit;
        opBal.cr = opCredit;
        resultset = null;
        return opBal;
    }

    private String getStartDate(final String fyId) {

        String startDate = "";
        resultset = null;
        final String finQry = "SELECT to_char(startingDate, 'dd-Mon-yyyy') AS \"startingDate\" FROM financialYear WHERE id = ?";
        try {
            PreparedStatement pstmt = null;
            pstmt = connection.prepareStatement(finQry);
            pstmt.setString(0, fyId);
            resultset = pstmt.executeQuery();
            if (resultset.next())
                startDate = resultset.getString("startingDate");
            else if (LOGGER.isDebugEnabled())
                LOGGER.debug("Inside else ...");
            pstmt.close();
        } catch (final SQLException ex) {
            startDate = "";
            LOGGER.error("Error GeneralLedger->getStartDate(): " + ex.toString(), ex);
        } finally {
            try {
                resultset.close();
            } catch (final Exception e)
            {
                LOGGER.error("Exp in finally..", e);
            }
        }
        // if(LOGGER.isDebugEnabled()) LOGGER.debug("startDate: " + startDate);
        return startDate;
    }

    private boolean isValidCode(final String glc) throws TaskFailedException {
        final CodeValidator cv = CodeValidator.getInstance();
        if (!cv.isValidCode(glc))
            return false;

        return true;
    }

    private void setDates(String startDate, String endDate) throws TaskFailedException {
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement pstmt = null;
        String formstartDate = "";
        String formendDate = "";
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
        try
        {
            formstartDate = formatter1.format(sdf.parse(startDate));
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw taskExc;
        }

        try {

            formendDate = formatter1.format(sdf.parse(endDate));
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw taskExc;
        }
        startDate = formstartDate;
        endDate = formendDate;
        if ((formstartDate == null || formstartDate.equalsIgnoreCase(""))
                && (formendDate == null || formendDate.equalsIgnoreCase("")))
            try {
                final String query = "SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') AS \"startingDate\" FROM financialYear WHERE startingDate <= SYSDATE AND endingDate >= SYSDATE";
                pstmt = connection.prepareStatement(query);
                rs = pstmt.executeQuery();
                if (rs.next())
                    formstartDate = rs.getString("startingDate");
                rs.close();
                final String query1 = "SELECT TO_CHAR(sysdate, 'dd-Mon-yyyy') AS \"endingDate\" FROM dual";
                rs1 = pstmt.executeQuery(query1);
                if (rs1.next())
                    formendDate = rs1.getString("endingDate");
                rs1.close();
                pstmt.close();
            } catch (final SQLException ex)
        {
                LOGGER.error(ex.getMessage(), ex);
                throw taskExc;
        }
        if ((formstartDate == null || formstartDate.equalsIgnoreCase(""))
                && (formendDate != null || !formendDate.equalsIgnoreCase("")))
            try {
                final String query = "SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') AS \"startingDate\" FROM financialYear WHERE startingDate <= ? AND endingDate >= ?";
                pstmt = connection.prepareStatement(query);
                pstmt.setString(0, formendDate);
                pstmt.setString(1, formendDate);
                rs = pstmt.executeQuery();
                if (rs.next())
                    formstartDate = rs.getString("startingDate");
                rs.close();
                pstmt.close();
            } catch (final SQLException e) {
                LOGGER.error(e.getMessage(), e);
                throw taskExc;
            }

        if ((formendDate == null || formendDate.equalsIgnoreCase(""))
                && (formstartDate != null || !formstartDate.equalsIgnoreCase("")))
            try {
                PreparedStatement pstmt1 = null;
                final String query = "SELECT TO_CHAR(endingDate, 'dd-Mon-yyyy') AS \"endingDate\" " +
                        "FROM financialYear WHERE startingDate <= '" + formstartDate + "' AND endingDate >= '" + formstartDate
                        + "'";
                pstmt1 = connection.prepareStatement(query);
                rs = pstmt1.executeQuery(query);
                // if(rs.next()) endDate = rs.getString("endingDate");
                rs.close();
                pstmt1.close();
            } catch (final SQLException e) {
                LOGGER.error(e.getMessage(), e);
                throw taskExc;
            }

    }

    public String getULBName() {
        String ulbName = "";

        try {
            final ResultSet rset = pstmt.executeQuery("select name from companydetail");
            if (rset.next())
                ulbName = rset.getString(1);
            else if (LOGGER.isDebugEnabled())
                LOGGER.debug("No result for companydetails");
            rset.close();
            pstmt.close();
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
        return ulbName;
    }

    public String getMinCode(final String minGlCode)
    {// if(LOGGER.isDebugEnabled()) LOGGER.debug("coming");
        String minCode = "";
        final String minQry = "select glcode from chartofaccounts where glcode like ? || '%' and classification = 4 order by glcode asc";
        try {
            pstmt = connection.prepareStatement(minQry);
            pstmt.setString(0, minGlCode);
            final ResultSet rset = pstmt.executeQuery();
            if (rset.next())
                minCode = rset.getString(1);
            else if (LOGGER.isDebugEnabled())
                LOGGER.debug("No glcodes for this mincode");
            rset.close();
            pstmt.close();
        } catch (final Exception sqlex) {
            LOGGER.error("Exception while getting minGlCode" + sqlex, sqlex);
        }
        return minCode;
    }

    public String getMaxCode(final String maxGlCode)
    {
        PreparedStatement pstmt = null;
        String maxCode = "";
        final String strQry = "select glcode from chartofaccounts where glcode like ? || '%' and classification = 4 order by glcode desc";
        try {
            pstmt = connection.prepareStatement(strQry);
            pstmt.setString(0, maxGlCode);
            final ResultSet rset = pstmt.executeQuery();
            if (rset.next())
                maxCode = rset.getString(1);
            else if (LOGGER.isDebugEnabled())
                LOGGER.debug("No glcodes for this maxcode");
            rset.close();
            pstmt.close();
        } catch (final Exception sqlex) {
            LOGGER.error("Exception while getting maxGlCode" + sqlex, sqlex);
        }
        return maxCode;
    }

    public String getCGN(final String vhn, final String vdate)
    {
        String cgn = "";
        pstmt = null;
        ResultSet rsCgn = null;
        if (!vhn.equals(""))
            try {
                final String queryCgn = "select CGN from VOUCHERHEADER where VOUCHERNUMBER= ? and voucherdate=to_date('" + vdate
                        + "','dd-Mon-yyyy')";
                pstmt = connection.prepareStatement(queryCgn);
                pstmt.setString(0, vhn);
                rsCgn = pstmt.executeQuery();
                rsCgn.next();
                cgn = rsCgn.getString("CGN");

            } catch (final Exception sqlex) {
                LOGGER.error("Error in cgnCatch#" + sqlex, sqlex);
                return null;
            } finally
            {
                try
                {
                    rsCgn.close();
                } catch (final Exception ExCgn) {
                    LOGGER.error("Exp in finally...", ExCgn);
                }
            }

        return cgn;
    }

    public static StringBuffer numberToString(final String strNumberToConvert)
    {
        String strNumber = "", signBit = "";
        if (strNumberToConvert.startsWith("-"))
        {
            strNumber = "" + strNumberToConvert.substring(1, strNumberToConvert.length());
            signBit = "-";
        }
        else
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

    public void isCurDate(final Connection conn, final String VDate) throws TaskFailedException
    {

        final EGovernCommon egc = new EGovernCommon();
        try {

            final String today = egc.getCurrentDate();
            final String[] dt2 = today.split("/");
            final String[] dt1 = VDate.split("/");

            final int ret = Integer.parseInt(dt2[2]) > Integer.parseInt(dt1[2]) ? 1 : Integer.parseInt(dt2[2]) < Integer
                    .parseInt(dt1[2]) ? -1 : Integer.parseInt(dt2[1]) > Integer.parseInt(dt1[1]) ? 1 : Integer
                            .parseInt(dt2[1]) < Integer.parseInt(dt1[1]) ? -1
                                    : Integer.parseInt(dt2[0]) > Integer.parseInt(dt1[0]) ? 1 : Integer.parseInt(dt2[0]) < Integer
                                            .parseInt(dt1[0]) ? -1 : 0;
                                    if (ret == -1)
                                        throw new Exception();

        } catch (final Exception ex) {
            LOGGER.error("Exception in isCurDate():" + ex, ex);
            throw new TaskFailedException("Date Should be within the today's date");
        }

    }
}
