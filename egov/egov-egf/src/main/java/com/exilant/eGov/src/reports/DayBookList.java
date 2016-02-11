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
package com.exilant.eGov.src.reports;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;

import com.exilant.GLEngine.DayBook;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;

public class DayBookList
{
    private double dAmount = 0.0;
    private double cAmount = 0.0;
    private static TaskFailedException taskexp;
    protected static final Logger LOGGER = Logger.getLogger(DayBookList.class);
    private Query pstmt = null;
    //VoucherHeader voucherheader=new VoucherHeader();

    public DayBookList() {
    }

    /**
     * @return Returns the cAmount.
     */
    public double getCAmount() {
        return cAmount;
    }

    /**
     * @param amount The cAmount to set.
     */
    public void setCAmount(final double amount) {
        cAmount = amount;
    }

    /**
     * @return Returns the dAmount.
     */
    public double getDAmount() {
        return dAmount;
    }

    /**
     * @param amount The dAmount to set.
     */
    public void setDAmount(final double amount) {
        dAmount = amount;
    }

    private String covertDate(final String changeDate)
    {
        String dateChanged = "";
        String dd = "";
        String mm = "";
        String yy = "";
        final int dateLenght = changeDate.length();
        if (dateLenght == 9)
        {
            dd = changeDate.substring(0, 1);
            mm = month(changeDate.substring(2, 4));

            yy = changeDate.substring(5, 9);
        }
        if (dateLenght == 10)
        {
            dd = changeDate.substring(0, 2);
            mm = month(changeDate.substring(3, 5));
            yy = changeDate.substring(6, 10);
        }
        dateChanged = dd + "-" + mm + "-" + yy;

        return dateChanged;
    }

    private String month(final String changeMonth)
    {
        final int m = Integer.parseInt(changeMonth);
        String monthChanged = "";
        switch (m)
        {
        case 1:
            monthChanged = "Jan";
            break;
        case 2:
            monthChanged = "Feb";
            break;
        case 3:
            monthChanged = "Mar";
            break;
        case 4:
            monthChanged = "Apr";
            break;
        case 5:
            monthChanged = "May";
            break;
        case 6:
            monthChanged = "Jun";
            break;
        case 7:
            monthChanged = "Jul";
            break;
        case 8:
            monthChanged = "Aug";
            break;
        case 9:
            monthChanged = "Sep";
            break;
        case 10:
            monthChanged = "Oct";
            break;
        case 11:
            monthChanged = "Nov";
            break;
        case 12:
            monthChanged = "Dec";
            break;
        default:
            monthChanged = "";
        }

        return monthChanged;
    }

    public void isCurDate(final String VDate) throws TaskFailedException {

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
            LOGGER.error("Exception " + ex, ex);
            throw new TaskFailedException("Date Should be within today's date");
        }

    }

    public LinkedList getDayBookList(final DayBookReportBean reportBean) throws TaskFailedException
    {
    	SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
        List<Object[]> rs = null;
        String dateStart = "";
        String dateEnd = "";
        String isconfirmed = "";
        double crTotal = 0;
        double dbTotal = 0;
        final LinkedList links = new LinkedList();

        final Integer fundId = Integer.parseInt(reportBean.getFundId());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("fundid: " + fundId);
        final String endDate1 = reportBean.getEndDate();
        isCurDate(endDate1);
        try
        {
            dateStart = covertDate(reportBean.getStartDate());
            dateEnd = covertDate(reportBean.getEndDate());
            if (fundId > 0) {
                final String queryString = "SELECT voucherdate as vdate, TO_CHAR(voucherdate, 'dd-Mon-yyyy')  AS "
                        + " voucherdate"
                        + ", vouchernumber , gd.glcode AS "
                        + " glcode"
                        + ",  ca.name AS "
                        + "particulars"
                        + ",vh.name ||' - '|| vh.TYPE AS "
                        + "type"
                        + ", "
                      +  " CASE WHEN vh.description is null THEN ' ' ELSE vh.description END AS narration, " 
+" CASE  WHEN status=0 THEN ( 'Approved') "
+" ELSE ( case WHEN status=1 THEN 'Reversed' else (case WHEN status=2 THEN 'Reversal' else ' ' END) END ) END as \"status\" , debitamount  , "
                        +
                        "creditamount,vh.CGVN ,vh.isconfirmed as \"isconfirmed\",vh.id as vhId FROM voucherheader vh, generalledger gd, chartofaccounts ca WHERE vh.ID=gd.VOUCHERHEADERID"
                        + " AND ca.GLCODE=gd.GLCODE AND voucherdate between ? AND ? and vh.status not in (4,5) " +
                        " and vh.fundid = ? ORDER BY vdate,vouchernumber";
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("queryString :" + queryString);

                pstmt = HibernateUtil.getCurrentSession().createSQLQuery(queryString);

                pstmt.setDate(0, new java.sql.Date(sdf.parse(reportBean.getStartDate()).getTime()));
                pstmt.setDate(1, new java.sql.Date(sdf.parse(reportBean.getEndDate()).getTime()));
                pstmt.setLong(2,fundId.longValue());
                List list = pstmt.list();
                rs=list;
                Object[] array = list.toArray();
                
                //rs=pstmt.list();
            }
/*            else {
                final String queryString = "SELECT voucherdate as vdate, TO_CHAR(voucherdate, 'dd-Mon-yyyy')  AS "
                        + " voucherdate"
                        + ", vouchernumber , gd.glcode AS "
                        + " glcode"
                        + ",  ca.name AS "
                        + "particulars"
                        + ",vh.name ||' - '|| vh.TYPE AS "
                        + "type"
                        + ", CASE WHEN vh.description = null THEN ' ' ELSE vh.description END AS "
                        + "narration"
                        + ", "
                        +
                        " CASE WHEN vh.description = null THEN ' ' ELSE vh.description END AS narration,  "
+" CASE  WHEN status=0 THEN ( CASE WHEN vh.isconfirmed=0 THEN 'Unconfirmed'  "
+" ELSE ( case WHEN vh.isconfirmed=1 THEN 'Confirmed' else ' '  END ) END) "
+" ELSE ( case WHEN status=1 THEN 'Reversed' else (case WHEN status=2 THEN 'Reversal' else ' ' END) END ) END as \"status\" , debitamount  , "
                        +
                        "creditamount,vh.CGVN ,vh.isconfirmed as \"isconfirmed\",vh.id as vhId FROM voucherheader vh, generalledger gd, chartofaccounts ca WHERE vh.ID=gd.VOUCHERHEADERID"
                        + " AND ca.GLCODE=gd.GLCODE AND voucherdate between ? AND ?  and vh.status not in (4,5) " +
                        "ORDER BY vdate,vouchernumber";
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("queryString :" + queryString);

                pstmt = HibernateUtil.getCurrentSession().createSQLQuery(queryString);
                pstmt.setDate(0, new java.sql.Date(sdf.parse(reportBean.getStartDate()).getTime()));
                pstmt.setDate(1, new java.sql.Date(sdf.parse(reportBean.getEndDate()).getTime()));
                List list = pstmt.list();
                rs=list;
                Object[] array = list.toArray();
            }*/
        } catch (final Exception e)
        {
            LOGGER.error("Error in Day book report:" + e.getMessage(), e);
            throw taskexp;
        }
        try
        {
            int totalCount = 0, isConfirmedCount = 0;
            String vn2 = "";
            // added by raja
            String tempVD = "", tempVN = "", tempTY = "", tempN = "", tempST = "", tempGL = "", tempPS = "", tempDA = "", tempCA = "";
            for (final Object[] element : rs) {
                final DayBook dBook = new DayBook();
                tempVD = element[1].toString();
                tempVN = element[2].toString();
                tempTY = element[5].toString();
                tempN = element[6].toString();
                tempST = element[7].toString();
                tempGL = element[3].toString();
                tempPS = element[4].toString();
                tempDA = element[8].toString();
                tempCA = element[9].toString();

                dBook.setStatus(tempST);
                if (tempVD.equals(" "))
                    dBook.setVoucherdate("");
                else
                    dBook.setVoucherdate(tempVD);
                if (tempVN.equals(" "))
                    dBook.setVoucher("");
                else
                    dBook.setVoucher(tempVN);
                if (tempTY.equals(" "))
                    dBook.setType("");
                else
                    dBook.setType(tempTY);
                if (tempN.equals(" "))
                    dBook.setNarration("");
                else
                    dBook.setNarration(tempN);
                if (tempGL.equals(" "))
                    dBook.setGlcode("");
                else
                    dBook.setGlcode(tempGL);
                if (tempPS.equals(" "))
                    dBook.setParticulars("");
                else
                    dBook.setParticulars(tempPS);
                if (tempDA.equals("0"))
                    dBook.setDebitamount("");
                else
                    dBook.setDebitamount(""
                            + numberToString(new BigDecimal(Double.parseDouble(tempDA)).setScale(2, BigDecimal.ROUND_HALF_UP)
                                    .toString()));
                if (tempCA.equals("0"))
                    dBook.setCreditamount("");
                else
                    dBook.setCreditamount(""
                            + numberToString(new BigDecimal(Double.parseDouble(tempCA)).setScale(2, BigDecimal.ROUND_HALF_UP)
                                    .toString()));
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("AFTER The tempDA value is:" + tempDA + "tempCA : " + tempCA);
                dbTotal += Double.parseDouble(element[8].toString());
                crTotal += Double.parseDouble(element[9].toString());
                dBook.setCgn(element[10].toString());
                dBook.setVhId(element[12].toString());
               isconfirmed = element[11] == null ? "" : element[11].toString();
                
                
                
                
                    final String vn1 = element[2].toString();
                    if (!vn1.equalsIgnoreCase(vn2))
                    {
                        vn2 = vn1;
                        totalCount = totalCount + 1;
                    }
               
                reportBean.setTotalCount(Integer.toString(totalCount));

                links.add(dBook);

            }   // While loop

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("dbTotal:" + dbTotal);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("crTotal:" + crTotal);
            final DayBook dBook1 = new DayBook();
            dBook1.setStatus("");
            dBook1.setVoucherdate("");
            dBook1.setVoucher("");
            dBook1.setParticulars("");
            dBook1.setType("<hr><b>Total</b><hr>");
            dBook1.setNarration("");
            dBook1.setGlcode("");
            dBook1.setDebitamount("<hr><b>"
                    + numberToString(new BigDecimal(dbTotal).setScale(2, BigDecimal.ROUND_HALF_UP).toString()).toString()
                    + "</b><hr>");
            dBook1.setCreditamount("<hr><b>"
                    + numberToString(new BigDecimal(crTotal).setScale(2, BigDecimal.ROUND_HALF_UP).toString()).toString()
                    + "</b><hr>");
            dBook1.setCgn("");
            links.add(dBook1);

            setCAmount(Math.round(crTotal));
            setDAmount(Math.round(dbTotal));
        } catch (final Exception e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Eror While preparing report:" + e.getMessage(), e);
            throw taskexp;
        }
        return links;
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

}
