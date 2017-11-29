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
/* *
 Created on June 20, 2006
 * @author Tilak
 */
package com.exilant.eGov.src.reports;

import com.exilant.exility.common.TaskFailedException;
import org.apache.log4j.Logger;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Service
public class OpeningBalance
{
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;


    List<Object[]> resultset;
    Query pstmt = null;
    private String fundId = "", finYear = "", deptId = "";
    private double grandTotalDr = 0.0, grandTotalCr = 0.0;
    private String fund = "", checkFund = "";
    private String glcode = "", name = "", narration = "" , deptcode= "",functioncode = "";
    private Double debit, credit, balance;
    ArrayList al = new ArrayList();

    private static final Logger LOGGER = Logger.getLogger(OpeningBalance.class);

    // This method is called by the OpeningBalance.jsp
    public ArrayList getOBReport(final OpeningBalanceInputBean OPBean) throws TaskFailedException
    {
        // String asOnDate1=OPBean.getAsOnDate();
        // isCurDate(con,asOnDate1);
        try {
            final String fuId = OPBean.getObFund_id();
            if (fuId != null)
                fundId = OPBean.getObFund_id();
            final String deptid = OPBean.getDeptId();
            if (deptid != null)
                deptId = OPBean.getDeptId();
            finYear = OPBean.getFinYear();
            if (LOGGER.isInfoEnabled())
                LOGGER.info("finYear --> " + finYear + " fundid  " + fundId);
            getReport();
            formatReport();
        } catch (final SQLException exception) {
            LOGGER.error("EXP=" + exception.getMessage(), exception);
        }

        return al;

    }

    private void getReport() throws SQLException {
        String query = " ";
        String fundCondition = "";
        String deptCondition = "";
        double totalDr = 0.0, totalCr = 0.0;
        new DecimalFormat();
        new DecimalFormat("###############.00");
        if (!fundId.equalsIgnoreCase(""))
            fundCondition = " and b.id=? ";
        if (!deptId.equalsIgnoreCase(""))
            deptCondition = " and a.DEPARTMENTID=? ";
        query = "SELECT b.name AS \"fund\",c.glcode AS \"accountcode\",c.name AS \"accountname\",'' as \"narration\",SUM(a.openingdebitbalance) AS \"debit\","
                + " SUM(a.openingcreditbalance)AS \"credit\",dept.code AS \"deptcode\",fn.code AS \"functioncode\"  FROM TRANSACTIONSUMMARY a,FUND  b,CHARTOFACCOUNTS c, eg_department dept,function fn "
                + " WHERE c.id in (select glcodeid from chartofaccountdetail  ) and a.departmentid= dept.id and fn.id = a.functionid and  a.financialyearid=? "
                + fundCondition
                + deptCondition
                + " AND a.fundid=b.id AND a.glcodeid=c.id AND (a.openingdebitbalance>0 OR a.openingcreditbalance>0) GROUP BY b.name, c.glcode,c.name,dept.code,fn.code union";
        query = query + " SELECT b.name AS \"fund\",c.glcode AS \"accountcode\",c.name AS \"accountname\",a.narration as \"narration\",SUM(a.openingdebitbalance) AS \"debit\","
                + " SUM(a.openingcreditbalance)AS \"credit\",dept.code AS \"deptcode\",fn.code AS \"functioncode\"  FROM TRANSACTIONSUMMARY a,FUND  b,CHARTOFACCOUNTS c, eg_department dept,function fn "
                + " WHERE c.id not in (select glcodeid from chartofaccountdetail  ) and a.departmentid= dept.id and fn.id = a.functionid and  a.financialyearid=? "
                + fundCondition
                + deptCondition
                + " AND a.fundid=b.id AND a.glcodeid=c.id AND (a.openingdebitbalance>0 OR a.openingcreditbalance>0) GROUP BY b.name, c.glcode,c.name,dept.code,fn.code, a.narration ";
       if (LOGGER.isDebugEnabled())
            LOGGER.debug("Opening balance Query ...." + query);

        try {
            OpeningBalanceBean ob = null;
            pstmt = persistenceService.getSession().createSQLQuery(query);
            int i = 0;
            pstmt.setLong(i++, Long.valueOf(finYear));
            if (!fundId.equalsIgnoreCase(""))
                pstmt.setLong(i++, Long.valueOf(fundId));
            if (!deptId.equalsIgnoreCase(""))
                pstmt.setLong(i++,Long.valueOf( deptId));
            pstmt.setLong(i++, Long.valueOf(finYear));
            if (!fundId.equalsIgnoreCase(""))
                pstmt.setLong(i++, Long.valueOf(fundId));
            if (!deptId.equalsIgnoreCase(""))
                pstmt.setLong(i++,Long.valueOf( deptId));
            List<Object[]> list= pstmt.list();
            resultset =list;
            
            for (final Object[] element : resultset) {
                if (!checkFund.equalsIgnoreCase(element[0].toString())
                        && !checkFund.equalsIgnoreCase("")) {
                    final OpeningBalanceBean opeBalDiff = new OpeningBalanceBean();
                    opeBalDiff.setFund("&nbsp;");
                    opeBalDiff.setAccCode("&nbsp;");
                    opeBalDiff.setAccName("<b>&nbsp;&nbsp;&nbsp; Difference&nbsp;&nbsp;</b>");
                    final double diff = totalDr - totalCr;
                    if (diff > 0)
                    {
                        opeBalDiff.setDebit("&nbsp;");
                        opeBalDiff.setCredit("<b>" + numberToString(((Double) diff).toString()).toString() + "</b>");
                    }
                    else
                    {
                        opeBalDiff.setDebit("<b>" + numberToString(((Double) diff).toString()).toString() + "</b>");
                        opeBalDiff.setCredit("&nbsp;");
                    }
                    al.add(opeBalDiff);
                    final OpeningBalanceBean opeBal = new OpeningBalanceBean();
                    opeBal.setFund("&nbsp;");
                    opeBal.setAccCode("&nbsp;");
                    opeBal.setAccName("<b>&nbsp;&nbsp;&nbsp; Total:&nbsp;&nbsp;</b>");
                    if (diff > 0)
                    {
                        totalCr = totalCr + diff;
                        opeBal.setDebit("<b>" + numberToString(((Double) totalDr).toString()).toString() + "</b>");
                        opeBal.setCredit("<b>" + numberToString(((Double) totalCr).toString()).toString() + "</b>");
                    }
                    else
                    {
                        totalDr = totalDr + diff * -1;
                        opeBal.setDebit("<b>" + numberToString(((Double) totalDr).toString()).toString() + "</b>");
                        opeBal.setCredit("<b>" + numberToString(((Double) totalCr).toString()).toString() + "</b>");
                    }
                    al.add(opeBal);
                    totalDr = 0.0;
                    totalCr = 0.0;
                }
                // if(LOGGER.isDebugEnabled()) LOGGER.debug("totalDr  "+totalDr+"  totalCr  "+totalCr);
                fund = element[0].toString();
                glcode = element[1].toString();
                name = element[2].toString();
                if(element[3]!=null)
                    narration = formatStringToFixedLength(element[3].toString(), 30);
                debit = Double.parseDouble(element[4].toString());
                credit = Double.parseDouble(element[5].toString());
                deptcode = element[6].toString();
                functioncode = element[7].toString();
                ob = new OpeningBalanceBean();
                ob.setFund(fund);
                ob.setAccCode(glcode);
                ob.setAccName(name);
                ob.setDescription(narration);
                ob.setDeptcode(deptcode);
                ob.setFunctioncode(functioncode);

                if (debit != null && credit != null)
                {
                    balance = debit - credit;
                    if (balance > 0)
                    {
                        ob.setDebit(numberToString(balance.toString()).toString());
                        ob.setCredit("&nbsp;");
                    }
                    else
                    {
                        balance = credit - debit;
                        ob.setDebit("&nbsp;");
                        ob.setCredit(numberToString(balance.toString()).toString());
                    }
                }
                /*
                 * if(debit!= null && debit>0) ob.setDebit(numberToString(((Double)debit).toString()).toString()); else
                 * ob.setDebit("&nbsp;");
                 */
                totalDr = totalDr + debit;
                grandTotalDr = grandTotalDr + debit;
                /*
                 * if(credit != null && credit>0) ob.setCredit(numberToString(((Double)credit).toString()).toString()); else
                 * ob.setCredit("&nbsp;");
                 */
                totalCr = totalCr + credit;
                grandTotalCr = grandTotalCr + credit;
                al.add(ob);
                checkFund = fund;
            }
            final OpeningBalanceBean opeBalDiff = new OpeningBalanceBean();
            opeBalDiff.setFund("&nbsp;");
            opeBalDiff.setAccCode("&nbsp;");
            opeBalDiff.setAccName("<b>&nbsp;&nbsp;&nbsp; Difference&nbsp;&nbsp;</b>");
            opeBalDiff.setDescription("&nbsp;");
            opeBalDiff.setDeptcode("&nbsp;");
            opeBalDiff.setFunctioncode("&nbsp;");
            final double diff = totalDr - totalCr;
            if (diff > 0)
            {
                opeBalDiff.setDebit("&nbsp;");
                opeBalDiff.setCredit("<b>" + numberToString(((Double) diff).toString()).toString() + "</b>");
            }
            else
            {
                opeBalDiff.setDebit("<b>" + numberToString(((Double) diff).toString()).toString() + "</b>");
                opeBalDiff.setCredit("&nbsp;");
            }
            al.add(opeBalDiff);
            final OpeningBalanceBean opeBal = new OpeningBalanceBean();
            opeBal.setFund("&nbsp;");
            opeBal.setAccCode("&nbsp;");
            opeBal.setAccName("<b>&nbsp;&nbsp;&nbsp; Total:&nbsp;&nbsp;</b>");
            opeBal.setDescription("&nbsp;");
            opeBal.setDeptcode("&nbsp;");
            opeBal.setFunctioncode("&nbsp;");
            if (diff > 0)
            {
                totalCr = totalCr + diff;
                opeBal.setDebit("<b>" + numberToString(((Double) totalDr).toString()).toString() + "</b>");
                opeBal.setCredit("<b>" + numberToString(((Double) totalCr).toString()).toString() + "</b>");
            }
            else
            {
                totalDr = totalDr + diff * -1;
                opeBal.setDebit("<b>" + numberToString(((Double) totalDr).toString()).toString() + "</b>");
                opeBal.setCredit("<b>" + numberToString(((Double) totalCr).toString()).toString() + "</b>");
            }
            al.add(opeBal);

        } catch (final Exception e)
        {
            LOGGER.error("Error in getReport", e);
            throw new SQLException();
        }
    }

    private void formatReport()
    {
        new DecimalFormat();
        // formatter = new DecimalFormat("##,##,##,##,##,##,###.00");
        final double diff = grandTotalDr - grandTotalCr;
        final OpeningBalanceBean ob = new OpeningBalanceBean();
        ob.setFund("<hr>&nbsp;<hr>");
        ob.setAccCode("<hr>&nbsp;<hr>");
        ob.setAccName("<hr><b>&nbsp;&nbsp;&nbsp;Grand Total:</b><hr>");
        ob.setDescription("<hr>&nbsp;<hr>");
        ob.setDeptcode("&nbsp;");
        ob.setFunctioncode("&nbsp;");
        if (diff > 0)
        {
            grandTotalCr = grandTotalCr + diff;
            ob.setDebit("<hr>&nbsp;<b>" + numberToString(((Double) grandTotalDr).toString()).toString() + "</b><hr>");
            ob.setCredit("<hr>&nbsp;<b>" + numberToString(((Double) grandTotalCr).toString()).toString() + "</b><hr>");
        }
        else
        {
            grandTotalDr = grandTotalDr + diff * -1;
            ob.setDebit("<hr>&nbsp;<b>" + numberToString(((Double) grandTotalDr).toString()).toString() + "</b><hr>");
            ob.setCredit("<hr>&nbsp;<b>" + numberToString(((Double) grandTotalCr).toString()).toString() + "</b><hr>");
        }
        al.add(ob);
    }

    public void isCurDate(final Connection conn, final String VDate) throws TaskFailedException {

        try {
            final String today = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
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
            LOGGER.error("Exception " + ex, ex);
            throw new TaskFailedException("Date Should be within the today's date");
        }

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

    /**
     * this function inserts html line break at the interval of fixedLength value in String str
     * @param str
     * @param fixedLength
     * @return
     */
    public String formatStringToFixedLength(String str, final int fixedLength)
    {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("insidde formatStringToFixedLength");

        str = str == null ? "&nbsp;" : str.trim().equalsIgnoreCase("") ? "&nbsp;" : str;
        if (str.equalsIgnoreCase("&nbsp;") || str.length() <= fixedLength)
            return str;
        int sIndex = 0;
        String formattedString = "";
        while (sIndex < str.length())
        {
            if (sIndex + fixedLength >= str.length())
                formattedString = formattedString + str.substring(sIndex, str.length());
            else
                formattedString = formattedString + str.substring(sIndex, sIndex + fixedLength) + "<BR>";
            sIndex = sIndex + fixedLength;
        }
        return formattedString;
    }

}