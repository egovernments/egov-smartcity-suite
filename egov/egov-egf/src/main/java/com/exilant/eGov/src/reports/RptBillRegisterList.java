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
/*this file generates returns Linked list of Yearly Bill register
 * Created on Jul 29, 2006
 * @author Chiranjeevi
 */
package com.exilant.eGov.src.reports;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.utils.FinancialConstants;
import org.hibernate.Query;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;

public class RptBillRegisterList {
    List<Object[]> resultset = null;
    List<Object[]> resultset1 = null;
    Date sDate, eDate;
    int totalCount = 0;
    private static TaskFailedException taskExc;
    String fundId, functionaryId, fieldId;
    private final static Logger LOGGER = Logger.getLogger(RptBillRegisterList.class);
    CommnFunctions commonFun = new CommnFunctions();

    public RptBillRegisterList() {
    }

    // this function add the data corresponding to setted financial year and contractor/Supplier type to datalist
    public LinkedList getRptBillRegisterList(final RptBillRegisterBean reportBean)
            throws TaskFailedException
    {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Enter<<<<<");
        final LinkedList dataList = new LinkedList();

        /** Bug fix for 8532 */
        fundId = reportBean.getFundId();
        functionaryId = reportBean.getFunctionaryId();
        reportBean.setFundName(getFundname(fundId));
        fieldId = reportBean.getFieldId();
        String formstartDate = "";
        String formendDate = "";
        String startDate, endDate;
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
        Date dt = new Date();

        try
        {
            startDate = reportBean.getStartDate();
            dt = sdf.parse(startDate);
            formstartDate = formatter1.format(dt);
        } catch (final Exception e) {
            LOGGER.error("inside the try-startdate" + e, e);
        }

        try
        {
            endDate = reportBean.getEndDate();
            if (LOGGER.isInfoEnabled())
                LOGGER.info("endDate " + endDate);
            if (endDate == null || endDate.equals("")) {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("endDate is empty ");
                // defaulted to from date if not provided the end date
                formendDate = formstartDate;
            } else {
                dt = sdf.parse(endDate);
                formendDate = formatter1.format(dt);
            }
        } catch (final Exception e) {
            LOGGER.error("inside the try-endDate" + e, e);
            throw taskExc;
        }

        startDate = formstartDate;
        endDate = formendDate;
        if (LOGGER.isInfoEnabled())
            LOGGER.info("startDate : " + startDate + " endDate " + endDate);
        final String fyId = commonFun.getFYID(endDate);
        if (LOGGER.isInfoEnabled())
            LOGGER.info(" fyId  " + fyId);
        if (fyId.equalsIgnoreCase("")) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Financial Year Not Valid");
            throw taskExc;
        }
        if (LOGGER.isInfoEnabled())
            LOGGER.info("get qry >>>>>>>>");
        final String query = getQuery(fundId, functionaryId, fieldId, startDate, endDate);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("data Query : " + query);

        try {
            final Query pstmt = HibernateUtil.getCurrentSession().createSQLQuery(query);
            resultset = pstmt.list();
        } catch (final Exception e) {
            LOGGER.error("Exp=" + e.getMessage(), e);
        }

        final ArrayList data = new ArrayList();
        String prevBillId = null, billId = null;
        int canAddToList = 1;

        try {
            for (final Object[] element : resultset) {
                final String arr[] = new String[14];
                totalCount += 1;
                // initializing array elements to empty
                String billDate, conSupName, particulars, approvedBy, paymentDate, remarks, sanctionedDate, voucherNo, paidAmt;
                BigDecimal billAmount, sanctionedAmount, disallowedAmount, balanceAmount;
                for (int i = 0; i < arr.length; i++)
                    arr[i] = "";

                // Assigning value to the array elements
                arr[0] = Integer.valueOf(totalCount).toString();
                billDate = element[2].toString();
                if (billDate != null && !billDate.trim().equals(""))
                    arr[1] = billDate;
                else
                    arr[1] = "&nbsp;";
                conSupName = element[3].toString();
                if (conSupName != null && !conSupName.trim().equals(""))
                    arr[2] = conSupName;
                else
                    arr[2] = "&nbsp;";
                particulars = element[5].toString();
                if (particulars != null && !particulars.trim().equals(""))
                    arr[3] = particulars;
                else
                    arr[3] = "&nbsp;";
                billAmount = new BigDecimal(element[6].toString());
                if (billAmount != null)
                    arr[4] = OpeningBalance.numberToString(billAmount.toString()).toString();
                else
                    arr[4] = "&nbsp;";
                approvedBy = element[7].toString();
                if (approvedBy != null)
                    arr[5] = approvedBy;
                else
                    arr[5] = "&nbsp;";
                sanctionedDate = element[8].toString();
                if (sanctionedDate != null)
                    arr[6] = sanctionedDate;
                else
                    arr[6] = "&nbsp;";

                voucherNo = element[16].toString();
                if (voucherNo != null)
                    arr[7] = voucherNo;
                else
                    arr[7] = "&nbsp;";

                sanctionedAmount = new BigDecimal(element[9].toString());
                if (sanctionedAmount != null)
                    arr[8] = OpeningBalance.numberToString(sanctionedAmount.toString()).toString();
                else
                    arr[8] = "&nbsp;";
                disallowedAmount = new BigDecimal(element[12].toString());
                if (disallowedAmount != null)
                    arr[9] = OpeningBalance.numberToString(disallowedAmount.toString()).toString();
                else
                    arr[9] = "&nbsp;";
                paidAmt = element[14].toString();
                if (paidAmt != null)
                    arr[10] = OpeningBalance.numberToString(paidAmt).toString();
                else
                    arr[10] = "&nbsp;";
                paymentDate = element[10].toString();
                if (paymentDate != null)
                    arr[11] = paymentDate;
                else
                    arr[11] = "&nbsp;";

                balanceAmount = new BigDecimal(element[13].toString());
                if (balanceAmount != null)
                    arr[12] = OpeningBalance.numberToString(balanceAmount.toString()).toString();
                else
                    arr[12] = "&nbsp;";
                remarks = element[15].toString();
                if (remarks != null && !remarks.trim().equals(""))
                    arr[13] = remarks;
                else
                    arr[13] = "&nbsp;";
                billId = element[0].toString();

                /* for contigency bill and payments */
                if (billId == null)
                {
                    data.add(arr);
                    prevBillId = billId;
                    canAddToList = 1;
                    continue;
                }

                /* for supplier/contractor bill and payment */
                if (billId != null)
                    if (!billId.equals(prevBillId))
                    {
                        data.add(arr);
                        prevBillId = billId;
                        canAddToList = 1;
                        continue;
                    }
                    else if (billId.equals(prevBillId))
                    {
                        if (canAddToList >= 2) {
                            canAddToList++;
                            continue;
                        }
                        data.add(arr);
                        prevBillId = billId;
                        canAddToList++;
                    }
            }

            // Adding data to 2 dimension array to pass to bean

            final String gridData[][] = new String[data.size() + 1][14];
            gridData[0][0] = "Sl No";
            gridData[0][1] = "Date of presentation by the Supplier/Dept";
            gridData[0][2] = "Name of Party/Dept";
            gridData[0][3] = "Particulars";
            gridData[0][4] = "Amount of Bill (Rs)";
            gridData[0][5] = "Initials of Authorised Officer";
            gridData[0][6] = "Date of Sanction";
            gridData[0][7] = "Voucher No ";
            gridData[0][8] = "Amount Sanctioned";
            // gridData[0][8] = "Date of Payment or issue of cheque";
            gridData[0][9] = "Amount Disallowed (Rs)";
            gridData[0][10] = "Paid Amount";
            gridData[0][11] = "Cheque No/Date";
            gridData[0][12] = "Balance outstanding at the end of the year (Rs)";
            gridData[0][13] = "Remarks";

            for (int i = 1; i <= data.size(); i++)
                gridData[i] = (String[]) data.get(i - 1);

            for (int i = 1; i <= data.size(); i++)
            {
                final RptBillRegisterBean reportBean1 = new RptBillRegisterBean();
                reportBean1.setSlno(gridData[i][0]);
                reportBean1.setBillDate(gridData[i][1]);
                reportBean1.setConSupName(gridData[i][2]);
                reportBean1.setParticulars(gridData[i][3]);
                reportBean1.setBillAmount(gridData[i][4]);
                reportBean1.setApprovedBy(gridData[i][5]);
                reportBean1.setSanctionedDate(gridData[i][6]);

                /*
                 * reportBean1.setSanctionedAmount( (gridData[i][7])); reportBean1.setPaymentDate( (gridData[i][8]));
                 * reportBean1.setDisallowedAmount( (gridData[i][9])); reportBean1.setBalanceAmount( (gridData[i][10]));
                 * reportBean1.setDelayReasons( (gridData[i][11]));
                 */
                reportBean1.setVoucherNo(gridData[i][7]);
                reportBean1.setSanctionedAmount(gridData[i][8]);
                reportBean1.setDisallowedAmount(gridData[i][9]);
                reportBean1.setPaidAmt(gridData[i][10]);
                reportBean1.setPaymentDate(gridData[i][11]);
                reportBean1.setBalanceAmount(gridData[i][12]);
                reportBean1.setRemarks(gridData[i][13]);
                reportBean1.setUlbname(getULBname());
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("fundId getFundName : " + reportBean1.getFundName() + fundId + "ulb " + reportBean1.getUlbname()
                            + getULBname());

                if (fundId != null)
                    reportBean1.setFundName(getFundname(fundId) + " Fund");
                if (fieldId != null)
                    reportBean1.setFieldName(getFieldname(fieldId) + " Field");
                if (functionaryId != null)
                    reportBean1.setFunctionaryName(getFunctionaryname(functionaryId) + " Functionary");

                dataList.add(reportBean1);

            }
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Datalist is filled");

        } catch (final Exception ex)
        {
            LOGGER.error("ERROR in getRptBillRegisterList " + ex.toString(), ex);
            throw taskExc;
        }
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Exit>>>>");
        return dataList;
    }

    // this function create the query string depending on code and type of Contractor/Supplier
    String getQuery(final int finId)
    {
        return " select cbd.id  as \"billId\",  cbd.billdate AS \"billDateForSort\", to_char(cbd.billdate,'dd-Mon-yyyy')  as \"billDate\", rel.name as \"conSupName\",rel.id as \"conSupId\", vh.description as \"particulars\", "
                +
                " cbd.BILLAMOUNT as \"billAmount\", cbd.APPROVEDBY as \"approvedBy\", to_char(cbd.billdate,'dd-Mon-yyyy') as \"sanctionedDate\", "
                +
                " cbd.PASSEDAMOUNT as \"sanctionedAmount\", '' as \"paymentDate\", null as \"pymtdateSort\", abs(cbd.BILLAMOUNT-cbd.PASSEDAMOUNT) as \"disallowedAmount\", "
                +
                " abs(cbd.PASSEDAMOUNT-cbd.PAIDAMOUNT) as \"balanceAmount\",  0  as \"paidAmt\", vh.DESCRIPTION as \"delayReasons\" "
                +
                " from contractorbilldetail cbd,relation rel,voucherheader vh,financialyear fy " +
                " where cbd.VOUCHERHEADERID=vh.id and cbd.CONTRACTORID=rel.ID " +
                " and vh.VOUCHERDATE>=fy.STARTINGDATE  and vh.VOUCHERDATE<=fy.ENDINGDATE and fy.id="
                + finId
                +
                " and vh.status not in (1,2,4) "
                +
                " union "
                +
                " select  cbd.id  as \"billId\", cbd.billdate AS \"billDateForSort\", to_char(cbd.billdate,'dd-Mon-yyyy')  as \"billDate\", rel.name as \"conSupName\",rel.id as \"conSupId\", vh.description as \"particulars\", "
                +
                " cbd.BILLAMOUNT as \"billAmount\", cbd.APPROVEDBY as \"approvedBy\", to_char(cbd.billdate,'dd-Mon-yyyy') as \"sanctionedDate\", "
                +
                " cbd.PASSEDAMOUNT as \"sanctionedAmount\",to_char(vh.VOUCHERDATE,'dd-Mon-yyyy') as \"paymentDate\", vh.voucherdate as \"pymtdateSort\", abs(cbd.BILLAMOUNT-cbd.PASSEDAMOUNT) as \"disallowedAmount\", "
                +
                " abs(cbd.PASSEDAMOUNT-cbd.PAIDAMOUNT) as \"balanceAmount\",  slph.PAIDAMOUNT as \"paidAmt\", vh.DESCRIPTION as \"delayReasons\" "
                +
                " from contractorbilldetail cbd,relation rel,voucherheader vh,financialyear fy,subledgerpaymentheader slph "
                +
                " where  cbd.CONTRACTORID=rel.ID "
                +
                " and vh.VOUCHERDATE>=fy.STARTINGDATE  and vh.VOUCHERDATE<=fy.ENDINGDATE and fy.id="
                + finId
                +
                " and vh.status not in (1,2,4) "
                +
                "and slph.VOUCHERHEADERID=vh.ID and slph.CONTRACTORBILLID=cbd.ID"
                +

                " UNION "
                +

                " select  sbd.id  as \"billId\", sbd.billdate AS \"billDateForSort\",to_char(sbd.billdate,'dd-Mon-yyyy')  as \"billDate\", rel.name as \"conSupName\",rel.id as \"conSupId\", vh.description as \"particulars\", "
                +
                " sbd.BILLAMOUNT as \"billAmount\", sbd.APPROVEDBY as \"approvedBy\", to_char(sbd.billdate,'dd-Mon-yyyy') as \"sanctionedDate\", "
                +
                " sbd.PASSEDAMOUNT as \"sanctionedAmount\",'' as \"paymentDate\", null as \"pymtdateSort\", abs(sbd.BILLAMOUNT-sbd.PASSEDAMOUNT) as \"disallowedAmount\", "
                +
                " abs(sbd.PASSEDAMOUNT-sbd.PAIDAMOUNT) as \"balanceAmount\",  0  as \"paidAmt\", vh.DESCRIPTION as \"delayReasons\" "
                +
                " from supplierbilldetail sbd,relation rel,voucherheader vh,financialyear fy  "
                +
                " where sbd.VOUCHERHEADERID=vh.id and sbd.SUPPLIERID=rel.ID "
                +
                " and vh.VOUCHERDATE>=fy.STARTINGDATE  and vh.VOUCHERDATE<=fy.ENDINGDATE and fy.id="
                + finId
                +
                " and vh.status not in (1,2,4) "
                +
                " union "
                +
                " select  sbd.id as \"billId\", sbd.billdate AS \"billDateForSort\", to_char(sbd.billdate,'dd-Mon-yyyy')  as \"billDate\", rel.name as \"conSupName\",rel.id as \"conSupId\", vh.description as \"particulars\", "
                +
                " sbd.BILLAMOUNT as \"billAmount\", sbd.APPROVEDBY as \"approvedBy\", to_char(sbd.billdate,'dd-Mon-yyyy') as \"sanctionedDate\", "
                +
                " sbd.PASSEDAMOUNT as \"sanctionedAmount\",to_char(vh.VOUCHERDATE,'dd-Mon-yyyy') as \"paymentDate\", vh.voucherdate as \"pymtdateSort\", abs(sbd.BILLAMOUNT-sbd.PASSEDAMOUNT) as \"disallowedAmount\", "
                +
                " abs(sbd.PASSEDAMOUNT-sbd.PAIDAMOUNT) as \"balanceAmount\",  slph.PAIDAMOUNT as \"paidAmt\", vh.DESCRIPTION as \"delayReasons\" "
                +
                " from supplierbilldetail sbd,relation rel,voucherheader vh,financialyear fy,subledgerpaymentheader slph  "
                +
                " where  sbd.SUPPLIERID=rel.ID "
                +
                " and vh.VOUCHERDATE>=fy.STARTINGDATE  and vh.VOUCHERDATE<=fy.ENDINGDATE and fy.id="
                + finId
                +
                " and vh.status not in (1,2,4) "
                +
                "and slph.VOUCHERHEADERID=vh.ID and slph.SUPPLIERBILLID=sbd.ID"
                +

                " UNION "
                +

                " SELECT   distinct null  as \"billId\",  egbr.billdate AS \"billDateForSort\", TO_CHAR (egbr.billdate, 'dd-Mon-yyyy') AS \"billDate\",  '' AS \"conSupName\",null as \"conSupId\", egbr.narration AS \"particulars\", egbr.billamount AS \"billAmount\", "
                +
                " '' AS \"approvedBy\", TO_CHAR (egbr.billdate, 'dd-Mon-yyyy') AS \"sanctionedDate\", egbr.passedamount AS \"sanctionedAmount\", '' AS \"paymentDate\", null as \"pymtdateSort\", "
                +
                " ABS (egbr.billamount - egbr.passedamount) AS \"disallowedAmount\",0 AS \"balanceAmount\",  0  as \"paidAmt\",  '' AS \"delayReasons\" "
                +
                " FROM eg_billregister egbr, otherbilldetail obd,voucherheader vh,financialyear fy "
                +
                " WHERE   vh.voucherdate >= fy.startingdate AND vh.voucherdate <= fy.endingdate AND fy.ID = "
                + finId
                + " AND vh.status <> 4 AND obd.billid=egbr.id AND egbr.EXPENDITURETYPE='"
                + FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT
                + "' "
                +
                " and vh.status not in (1,2,4) "
                +
                " UNION "
                +
                " SELECT distinct  null  as \"billId\", egbr.billdate AS \"billDateForSort\", TO_CHAR (egbr.billdate, 'dd-Mon-yyyy') AS \"billDate\",'' AS \"conSupName\",null as \"conSupId\", vh.description AS \"particulars\",egbr.billamount AS \"billAmount\", '' AS \"approvedBy\", "
                +
                " TO_CHAR (egbr.billdate, 'dd-Mon-yyyy') AS \"sanctionedDate\",egbr.passedamount AS \"sanctionedAmount\",TO_CHAR (vh.voucherdate, 'dd-Mon-yyyy') AS \"paymentDate\", vh.voucherdate as \"pymtdateSort\", "
                +
                " ABS (egbr.billamount - egbr.passedamount) AS \"disallowedAmount\",0 AS \"balanceAmount\", 0 as \"paidAmt\",vh.description AS \"delayReasons\" "
                +
                " FROM eg_billregister egbr,otherbilldetail obd,voucherheader vh,financialyear fy "
                +
                " WHERE vh.voucherdate >= fy.startingdate AND vh.voucherdate <= fy.endingdate AND fy.ID ="
                + finId
                + " AND vh.status <> 4 AND obd.billid=egbr.id AND egbr.EXPENDITURETYPE='"
                + FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT
                + "' AND obd.voucherheaderid = vh.ID "
                +
                " and vh.status not in (1,2,4) "
                +
                " order by \"billDateForSort\",\"conSupName\",\"conSupId\" NULLS LAST , \"billId\" ,\"pymtdateSort\" desc NULLS FIRST  "
                +
                " UNION " +
                " ";
    }

    // this function create the query string depending on code and type of Contractor/Supplier
    String getQuery(final String fundId, final String functionaryId, final String fieldId, final String startDate,
            final String endDate)
    {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("getqry ....");
        String fundCondition = "";
        String functionaryCondition = "";
        String fieldCondition = "";
        String addTableToQuery = "";
        if (LOGGER.isInfoEnabled())
            LOGGER.info("fundId " + fundId + "functionaryId " + functionaryId + " fieldId " + fieldId + "startDate " + startDate
                    + "endDate " + endDate);
        if (!(fundId == null) && !fundId.equalsIgnoreCase(""))
            fundCondition = " AND vh.fundId = " + fundId + " ";
        if (!(functionaryId == null) && !functionaryId.equalsIgnoreCase(""))
            // addTableToQuery=", vouchermis vhm ";
            // chk from where to get this value
            functionaryCondition = " AND  vh.FUNCTIONID = " + functionaryId + " ";
        if (!(fieldId == null) && !fieldId.equalsIgnoreCase(""))
        {
            addTableToQuery = " , vouchermis vhm ";
            fieldCondition = " AND vhm.VOUCHERHEADERID = vh.id  and vhm.DIVISIONID = " + fieldId + " ";
        }
        return "SELECT eb.id AS \"billId\",eb.billdate AS \"billDateForSort\",TO_CHAR (eb.billdate, 'dd-Mon-yyyy') AS \"billDate\",ebm.PAYTO AS \"conSupName\", NULL AS \"conSupId\",CONCAT (gl.glcode, CONCAT ('-', ' Cr')) AS \"particulars\",eb.billamount AS \"billAmount\",case when md.approvedby = 'null' then '' else md.approvedby end \"approvedBy\", "
        + " TO_CHAR (eb.billdate, 'dd-Mon-yyyy') AS \"sanctionedDate\",eb.PASSEDAMOUNT AS \"sanctionedAmount\",CONCAT( CONCAT(cd.CHEQUENUMBER,'/' ), TO_CHAR (vh.voucherdate, 'dd-Mon-yyyy')) AS \"paymentDate\",vh.voucherdate AS \"pymtdateSort\", "
        + " ABS (eb.billamount - eb.passedamount) AS \"disallowedAmount\",ABS (eb.billamount - md.passedamount) AS \"balanceAmount\", md.passedamount AS \"paidAmt\", '' AS \"remarks\" ,vh.VOUCHERNUMBER as \"voucherNo\" "
        + " FROM EG_BILLREGISTER eb,EG_BILLREGISTERMIS ebm ,VOUCHERHEADER vh,OTHERBILLDETAIL obd,MISCBILLDETAIL md , PAYMENTHEADER ph,CHEQUEDETAIL cd,generalledger gl  "
        + " WHERE eb.billdate >= '"
        + startDate
        + "'  AND eb.billdate <= '"
        + endDate
        + "'   "
        + fundCondition
        + functionaryCondition
        + fieldCondition
        + " AND vh.status <> 4 AND eb.id=ebm.BILLID AND eb.EXPENDITURETYPE ='"
        + FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT
        + "' AND eb.id=obd.BILLID  AND obd.PAYVHID = vh.id AND ph.VOUCHERHEADERID =vh.id AND md.ID = ph.MISCBILLDETAILID AND cd.id=ph.CHEQUEID AND gl.VOUCHERHEADERID = vh.id AND gl.CREDITAMOUNT > 0 "
        + " UNION "
        + " SELECT   cbd.billid AS \"billId\", cbd.billdate AS \"billDateForSort\",TO_CHAR (cbd.billdate, 'dd-Mon-yyyy') AS \"billDate\",rel.NAME AS \"conSupName\", rel.ID AS \"conSupId\" ,"
        + " CONCAT (gl.glcode, CONCAT ('-', ' Cr')) AS \"particulars\", cbd.billamount AS \"billAmount\",case when cbd.approvedby  = 'null' then '' else cbd.approvedby end  AS \"approvedBy\",TO_CHAR (cbd.billdate, 'dd-Mon-yyyy') AS \"sanctionedDate\",cbd.passedamount AS \"sanctionedAmount\","
        + " CONCAT( CONCAT(cd.CHEQUENUMBER,'/' ), TO_CHAR (vh.voucherdate, 'dd-Mon-yyyy')) AS \"paymentDate\",vh.voucherdate AS \"pymtdateSort\", ABS (cbd.billamount - cbd.passedamount) AS \"disallowedAmount\",ABS (cbd.passedamount - cbd.paidamount) AS \"balanceAmount\",slph.paidamount AS \"paidAmt\", vh.description AS \"remarks\" ,vh.VOUCHERNUMBER as \"voucherNo\""
        + " FROM  CONTRACTORBILLDETAIL cbd,RELATION rel, VOUCHERHEADER vh,SUBLEDGERPAYMENTHEADER slph,CHEQUEDETAIL cd,generalledger gl  "
        + addTableToQuery
        + " "
        + " WHERE  cbd.billdate >= '"
        + startDate
        + "' AND cbd.billdate <= '"
        + endDate
        + "' "
        + fundCondition
        + functionaryCondition
        + fieldCondition
        + " AND vh.status <> 4 AND slph.voucherheaderid = vh.ID AND slph.contractorbillid = cbd.ID AND cbd.contractorid = rel.ID AND cd.id=slph.CHEQUEID AND gl.VOUCHERHEADERID = vh.id AND gl.CREDITAMOUNT > 0 "
        + " UNION "
        + " SELECT sbd.billid AS \"billId\", sbd.billdate AS \"billDateForSort\",TO_CHAR (sbd.billdate, 'dd-Mon-yyyy') AS \"billDate\",rel.NAME AS \"conSupName\", rel.ID AS \"conSupId\",CONCAT (gl.glcode, CONCAT ('-', ' Cr')) AS \"particulars\", sbd.billamount AS \"billAmount\", "
        + " case when sbd.approvedby = 'null' then '' else sbd.approvedby end AS \"approvedBy\",TO_CHAR (sbd.billdate, 'dd-Mon-yyyy') AS \"sanctionedDate\",sbd.passedamount AS \"sanctionedAmount\",CONCAT( CONCAT(cd.CHEQUENUMBER,'/' ), TO_CHAR (vh.voucherdate, 'dd-Mon-yyyy')) AS \"paymentDate\", vh.voucherdate AS \"pymtdateSort\","
        + " ABS (sbd.billamount - sbd.passedamount) AS \"disallowedAmount\", ABS (sbd.passedamount - sbd.paidamount) AS \"balanceAmount\",slph.paidamount AS \"paidAmt\", vh.description AS \"remarks\" ,vh.VOUCHERNUMBER as \"voucherNo\""
        + " FROM  supplierBILLDETAIL sbd,RELATION rel,VOUCHERHEADER vh,SUBLEDGERPAYMENTHEADER slph,CHEQUEDETAIL cd ,generalledger gl "
        + addTableToQuery
        + " WHERE  "
        + " sbd.SUPPLIERID = rel.ID AND sbd.billdate >= '"
        + startDate
        + "' AND sbd.billdate <= '"
        + endDate
        + "' "
        + fundCondition
        + functionaryCondition
        + fieldCondition
        + " AND vh.status <> 4 AND slph.voucherheaderid = vh.ID AND slph.SUPPLIERBILLID = sbd.ID  AND cd.id=slph.CHEQUEID AND gl.VOUCHERHEADERID = vh.id AND gl.CREDITAMOUNT > 0 "
        + " ORDER BY \"billDateForSort\", \"conSupName\",\"conSupId\" NULLS LAST,\"billId\", \"pymtdateSort\" DESC NULLS FIRST";

    }

    public void isCurDate(final String VDate) throws TaskFailedException
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

    public String getFundname(final String fName) {
        String fundName = "";
        List<Object[]> rset = null;
        try {
            final String query = "select name from fund where id=?";
            final Query pstmt = HibernateUtil.getCurrentSession().createSQLQuery(query);
            pstmt.setString(0, fName);
            rset = pstmt.list();
            for (final Object[] element : rset)
                fundName = element[0].toString();

        } catch (final Exception sqlex) {
            LOGGER.error("Inside getFundName" + sqlex.getMessage(), sqlex);
            return null;
        }
        return fundName;
    }

    public String getFieldname(final String fName) {
        String fundName = "";
        List<Object[]> rset = null;
        try {
            final String query = "SELECT name FROM EG_BOUNDARY WHERE id_bndry =? and ID_BNDRY_TYPE = (SELECT id_bndry_type FROM EG_BOUNDARY_TYPE WHERE LOWER(name)= 'ward' ";
            final Query pstmt = HibernateUtil.getCurrentSession().createSQLQuery(query);
            pstmt.setString(0, fName);
            rset = pstmt.list();
            for (final Object[] element : rset)
                fundName = element[0].toString();

        } catch (final Exception sqlex) {
            LOGGER.error(sqlex.getMessage(), sqlex);
            return null;
        }
        return fundName;
    }

    public String getFunctionaryname(final String fName) {
        String fundName = "";
        List<Object[]> rset = null;
        try {
            final String query = "SELECT name  FROM functionaryname  where id = ?";
            final Query pstmt = HibernateUtil.getCurrentSession().createSQLQuery(query);
            pstmt.setString(0, fName);
            rset = pstmt.list();
            for (final Object[] element : rset)
                fundName = element[0].toString();

        } catch (final Exception sqlex) {
            LOGGER.error(sqlex.getMessage(), sqlex);
            return null;
        }
        return fundName;
    }

    public String getULBname() {
        String fundName = "";
        try {
            final Query pstmt = HibernateUtil.getCurrentSession().createSQLQuery("select name FROM companyDetail");
            final List<Object[]> rset = pstmt.list();
            for (final Object[] element : rset)
                fundName = element[0].toString();

        } catch (final Exception sqlex) {
            LOGGER.error(sqlex.getMessage(), sqlex);
            return null;
        }
        return fundName;
    }
}
