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
/*
 * Created on March 28, 2007
 * @author Girish
 */
package com.exilant.eGov.src.reports;


import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EGovConfig;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

class OpBalance {
    public double dr;
    public double cr;
}

public class CashBook {
    Connection connection = null;

    Query pstmt = null;
    List<Object[]> resultset = null;
    List<Object[]> resultset1 = null;
    TaskFailedException taskExc;
    String startDate, endDate, effTime, rType = "gl";
    NumberFormat numberformatter = new DecimalFormat("##############0.00");
    private final CommnFunctions commonFun = new CommnFunctions();
    private static final Logger LOGGER = Logger.getLogger(CashBook.class);
   
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired
    private FinancialYearHibernateDAO financialYearDAO;
    final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    final SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
    private @Autowired EGovernCommon eGovernCommon; 
    private @Autowired  ReportEngine engine;

    public CashBook() {
    }

    public CashBook(final Connection con) {
        connection = con;
    }

    public LinkedList getGeneralLedgerList(final GeneralLedgerReportBean reportBean)
            throws TaskFailedException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside getGeneralLedgerList");
        final LinkedList dataList = new LinkedList();
        try {
            String isconfirmed = "";
            String glCode1 = "";
            String glCode2 = "";
            taskExc = new TaskFailedException();
            final String cashPId = EGovConfig.getProperty("egf_config.xml",
                    "PURPOSEID", "", "CashInHand");

            final String boundryId = reportBean.getBoundary();
            final String ulbname = getUlbDetails();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("ulbname:" + ulbname + "boundryId " + boundryId);
            reportBean.setUlbName(ulbname);
            final String glcodes[] = getGlcode(boundryId);
            glCode1 = glcodes[0];
            glCode2 = glcodes[1];

            final String maxlength = EGovConfig.getProperty("egf_config.xml",
                    "glcodeMaxLength", "", "AccountCode");
            if (glCode1.length() != Integer.parseInt(maxlength)
                    && glCode2.length() != Integer.parseInt(maxlength)) {
                glCode1 = getMinCode(glCode1);
                glCode2 = getMaxCode(glCode2);
            }

            try {
                final String snapShotDateTime = reportBean.getSnapShotDateTime();
                if (snapShotDateTime.equalsIgnoreCase(""))
                    effTime = "";
                else
                    effTime = eGovernCommon.getEffectiveDateFilter(snapShotDateTime);
            } catch (final Exception ex) {
                LOGGER.error("exception in getGeneralLedgerList", ex);
                throw taskExc;
            }

            String formstartDate = "", formendDate = "";
            Date dt = new Date();
            final String fundId = reportBean.getFund_id();
            final String fundSourceId = "";
            final String endDate1 = reportBean.getEndDate();
            if (LOGGER.isInfoEnabled())
                LOGGER.info(" fundId:" + fundId + " fundSourceId:"
                        + fundSourceId);
            
            isCurDate(endDate1);
            try {
                endDate = reportBean.getEndDate();
                dt = sdf.parse(endDate);
                formendDate = formatter1.format(dt);
            } catch (final Exception e) {
                LOGGER.error("inside the try-startdate", e);
                throw taskExc;
            }
            try {
                startDate = reportBean.getStartDate();
                if (!startDate.equalsIgnoreCase("null")) {
                    dt = sdf.parse(startDate);
                    formstartDate = formatter1.format(dt);
                }
            } catch (final Exception e) {
                LOGGER.error("inside the try-startdate", e);
                throw taskExc;
            }

            if (startDate.equalsIgnoreCase("null")) {
            	
                try {
					dt = sdf.parse(endDate);
				} catch (ParseException e) {


				}
                CFinancialYear finYearByDate = financialYearDAO.getFinYearByDate(dt);
               // final String finId = commonFun.getFYID(formendDate);
                //startDate = commonFun.getStartDate(Integer.parseInt(finId));
                if(finYearByDate!=null)
            	{
                		startDate =sdf.format(finYearByDate.getStartingDate());
            	}
                
            } else
                startDate = formstartDate;
            // if(LOGGER.isInfoEnabled()) LOGGER.info("startDate22 "+startDate);
            endDate = formendDate;
            final String startDateformat = startDate;
            String startDateformat1 = "";
            try {
                dt = formatter1.parse(startDateformat);
                startDateformat1 = sdf.format(dt);
            } catch (final Exception e) {
                LOGGER.error("Parse Exception", e);
                throw taskExc;
            }

            
            Date dt1=new Date();
			try {
				dt1 = sdf.parse(endDate);
			} catch (ParseException e1) {
			}
            CFinancialYear finYearByDate = financialYearDAO.getFinYearByDate(dt1);
            final String fyId=finYearByDate.getId().toString();
            //final String fyId = commonFun.getFYID(endDate);
            if (fyId.equalsIgnoreCase("")) {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("Financial Year Not Valid");
                throw taskExc;
            }
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("before query");
            // double txnDrSum=0, txnCrSum=0, closingBalance = 0;
            BigDecimal cashOpeningBalance = new BigDecimal("0.00");
            BigDecimal chequeOpeningBalance = new BigDecimal("0.00");

            final ReportEngineBean reBean = engine
                    .populateReportEngineBean(reportBean);
            final String engineQry = engine.getVouchersListQuery(reBean);

            final String query = getQuery(engineQry);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("**************QUERY: " + query);

            // try{

            try {
                pstmt = persistenceService.getSession().createSQLQuery(query);
            } catch (final Exception e) {
                LOGGER.error("Exception in creating statement:", e);
                throw taskExc;
            }
            resultset1 = pstmt.list();
            String accCode = "", vcNum = "", vcDate = "";
            StringBuffer detail = new StringBuffer();
            StringBuffer amount = new StringBuffer();
            StringBuffer accCodebuffer = new StringBuffer();
            int vhId = 0, VhidPrevious = 0;
            BigDecimal currentDebit = new BigDecimal("0.00");
            BigDecimal currentCredit = new BigDecimal("0.00");
            BigDecimal cashdebitTotal = new BigDecimal("0.00");
            BigDecimal chequedebitTotal = new BigDecimal("0.00");
            BigDecimal cashcreditTotal = new BigDecimal("0.00");
            BigDecimal chequecreditTotal = new BigDecimal("0.00");
            String code = "", vhType = "", funcCode = "", bgtCode = "", srcOfFinance = "", cgn = "", currVhDate, purposeid = "";
            /**
             * When using ResultSet.TYPE_SCROLL_INSENSITIVE in createStatement if no records are there, rs.next() will return true
             * but when trying to access (rs.getXXX()), it will throw an error
             **/
            int totalCount = 0, isConfirmedCount = 0;
            String vn2 = "";
            OpBalance opbal = getOpeningBalance(glCode1, fundId, fundSourceId,
                    fyId, startDate);
            cashOpeningBalance = BigDecimal.valueOf(opbal.dr - opbal.cr);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("cashOpeningBalance:" + cashOpeningBalance);
            opbal = getOpeningBalance(glCode2, fundId, fundSourceId, fyId,
                    startDate);
            chequeOpeningBalance = BigDecimal.valueOf(opbal.dr - opbal.cr);
            /*
             * left side of the report is receipts so we are assigning (opbal.dr - opbal.cr) to creditTotal of cash and cheque
             * respectively and displaying opening balance
             */
            cashcreditTotal = cashOpeningBalance;
            chequecreditTotal = chequeOpeningBalance;
            final GeneralLedgerReportBean glbeanOpBal = new GeneralLedgerReportBean();
            glbeanOpBal.setRcptParticulars("<B>To Opening Balance</B>");
            glbeanOpBal.setRcptcashInHandAmt("<B>"
                    + numberToString(cashOpeningBalance.toString()) + "</B>");
            glbeanOpBal.setRcptChqInHandAmt("<B>"
                    + numberToString(chequeOpeningBalance.toString()) + "</B>");
            dataList.add(glbeanOpBal);

            int count2skip1stRow = 0;
            for (final Object[] element : resultset1)
                // if(LOGGER.isInfoEnabled()) LOGGER.info(" inside resultset");
                try {

                    code = element[0].toString();
                    isconfirmed = element[20].toString();
                    // 9 is the dummy value used in the query
                    // To display X in Y are unconfirmed
                    if (isconfirmed != null
                            && !isconfirmed.equalsIgnoreCase("")
                            && !isconfirmed.equalsIgnoreCase("9")) {
                        final String vn1 = element[11].toString();
                        if (!vn1.equalsIgnoreCase(vn2)) {
                            vn2 = vn1;
                            totalCount = totalCount + 1;
                            if (isconfirmed.equalsIgnoreCase("0"))
                                isConfirmedCount = isConfirmedCount + 1;
                        }
                    }

                    vhId = Integer.parseInt(element[8].toString());
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("check1>>vhId:" + vhId + " VhidPrevious:"
                                + VhidPrevious + " code:" + code + " accCode:"
                                + accCode);

                    if (vhId != VhidPrevious) {
                        if (LOGGER.isInfoEnabled())
                            LOGGER.info("inside vhId!=VhidPrevious & vhType="
                                    + vhType);
                        final GeneralLedgerReportBean glbean = new GeneralLedgerReportBean(
                                "&nbsp;");

                        if (currentCredit.doubleValue() > 0) {
                            if (LOGGER.isInfoEnabled())
                                LOGGER.info("inside Receipt>>>>>>" + accCode);
                            glbean.setRcptVchrNo(vcNum);
                            if (vcDate != null && !vcDate.equalsIgnoreCase(""))
                                glbean.setRcptVchrDate(vcDate);
                            if (bgtCode != null
                                    && !bgtCode.equalsIgnoreCase(""))
                                glbean.setRcptBgtCode(bgtCode);
                            if (funcCode != null
                                    && !funcCode.equalsIgnoreCase(""))
                                glbean.setRcptFuncCode(funcCode);
                            if (purposeid.equalsIgnoreCase(cashPId)) {
                                if (amount != null && !amount.equals(""))
                                    glbean.setRcptcashInHandAmt(amount
                                            .toString());
                            } else if (amount != null && !amount.equals(""))
                                glbean.setRcptChqInHandAmt(amount
                                        .toString());
                            if (detail != null && !detail.equals(""))
                                glbean.setRcptParticulars(detail.toString());
                            if (srcOfFinance != null
                                    && !srcOfFinance.equals(""))
                                glbean.setRcptSrcOfFinance(srcOfFinance);
                            // if(accCode!=null && !accCode.equals(""))
                            // glbean.setRcptAccCode(accCode);
                            if (accCodebuffer != null
                                    && !accCodebuffer.equals(""))
                                glbean.setRcptAccCode(accCodebuffer.toString());

                        } else {
                            if (LOGGER.isInfoEnabled())
                                LOGGER.info("inside Payment>>>>>>" + accCode);
                            glbean.setPmtVchrNo(vcNum);
                            if (vcDate != null && !vcDate.equalsIgnoreCase(""))
                                glbean.setpmtVchrDate(vcDate);
                            if (bgtCode != null
                                    && !bgtCode.equalsIgnoreCase(""))
                                glbean.setPmtBgtCode(bgtCode);
                            if (funcCode != null
                                    && !funcCode.equalsIgnoreCase(""))
                                glbean.setPmtFuncCode(funcCode);
                            if (purposeid.equalsIgnoreCase(cashPId)) {
                                if (amount != null && !amount.equals(""))
                                    glbean.setPmtCashInHandAmt(amount
                                            .toString());
                            } else if (amount != null && !amount.equals(""))
                                glbean.setPmtChqInHandAmt(amount.toString());
                            if (detail != null && !detail.equals(""))
                                glbean.setPmtParticulars(detail.toString());
                            if (srcOfFinance != null
                                    && !srcOfFinance.equals(""))
                                glbean.setPmtSrcOfFinance(srcOfFinance);
                            // if(accCode!=null && !accCode.equals(""))
                            // glbean.setPmtAccCode(accCode);
                            if (accCodebuffer != null
                                    && !accCodebuffer.equals(""))
                                glbean.setPmtAccCode(accCodebuffer.toString());

                        }
                        if (LOGGER.isInfoEnabled())
                            LOGGER.info("cgn before adding: " + cgn);
                        glbean.setCGN(cgn);
                        reportBean.setStartDate(startDateformat1);
                        reportBean.setTotalCount(Integer.toString(totalCount));
                        reportBean.setIsConfirmedCount(Integer
                                .toString(isConfirmedCount));
                        if (count2skip1stRow != 0)
                            dataList.add(glbean);// skip to insert blank row at
                        // the top
                        count2skip1stRow++;
                        currVhDate = element[10].toString();
                        if (LOGGER.isInfoEnabled())
                            LOGGER.info("vcDate:" + vcDate + " currVhDate:"
                                    + currVhDate);
                        if (!vcDate.equalsIgnoreCase(currVhDate)
                                && !vcDate.equalsIgnoreCase("")) {

                            final GeneralLedgerReportBean glbeanCb = new GeneralLedgerReportBean(
                                    "&nbsp;");
                            glbeanCb.setPmtParticulars("<B>Closing: By balance c/d</B>");
                            glbeanCb.setPmtCashInHandAmt("<B>"
                                    + numberToString(cashcreditTotal.subtract(
                                            cashdebitTotal).toString())
                                            + "</B>");
                            glbeanCb.setPmtChqInHandAmt("<B>"
                                    + numberToString(chequecreditTotal
                                            .subtract(chequedebitTotal)
                                            .toString()) + "</B>");
                            dataList.add(glbeanCb);
                            final GeneralLedgerReportBean glbean1 = new GeneralLedgerReportBean(
                                    "<hr>&nbsp;</hr>");
                            glbean1.setRcptVchrDate("<hr><B>Total</B></hr>");
                            glbean1.setRcptcashInHandAmt("<hr><B>"
                                    + numberToString(cashcreditTotal.toString())
                                    + "</B></hr>");
                            glbean1.setPmtCashInHandAmt("<hr><B>"
                                    + numberToString(cashdebitTotal.add(
                                            cashcreditTotal
                                            .subtract(cashdebitTotal))
                                            .toString()) + "</B></hr>");
                            glbean1.setRcptChqInHandAmt("<hr><B>"
                                    + numberToString(chequecreditTotal
                                            .toString()) + "</B></hr>");
                            glbean1.setPmtChqInHandAmt("<hr><B>"
                                    + numberToString(chequedebitTotal
                                            .add(chequecreditTotal
                                                    .subtract(chequedebitTotal))
                                                    .toString()) + "</B></hr>");
                            dataList.add(glbean1);
                            if (LOGGER.isInfoEnabled())
                                LOGGER.info(cashcreditTotal + ":crDr: "
                                        + cashdebitTotal);
                            final GeneralLedgerReportBean glbeanOb = new GeneralLedgerReportBean(
                                    "<hr>&nbsp;</hr>");
                            glbeanOb.setRcptParticulars("<hr><B>Opening: To balance b/d</B></hr>");
                            // glbeanOb.setRcptcashInHandAmt("<hr><B>"+(numberformatter.format(cashcreditTotal.subtract(cashdebitTotal).doubleValue()))+"</B></hr>");
                            // glbeanOb.setRcptChqInHandAmt("<hr><B>"+(numberformatter.format(chequecreditTotal.subtract(chequedebitTotal).doubleValue()))+"</B></hr>");
                            glbeanOb.setRcptcashInHandAmt("<hr><B>"
                                    + numberToString(cashcreditTotal.subtract(
                                            cashdebitTotal).toString())
                                            + "</B></hr>");
                            glbeanOb.setRcptChqInHandAmt("<hr><B>"
                                    + numberToString(chequecreditTotal
                                            .subtract(chequedebitTotal)
                                            .toString()) + "</B></hr>");
                            dataList.add(glbeanOb);
                            cashcreditTotal = cashcreditTotal
                                    .subtract(cashdebitTotal);
                            chequecreditTotal = chequecreditTotal
                                    .subtract(chequedebitTotal);
                            cashdebitTotal = new BigDecimal("0.00");
                            chequedebitTotal = new BigDecimal("0.00");
                        }
                        vcNum = vcDate = bgtCode = funcCode = srcOfFinance = accCode = cgn = vhType = "";
                        amount.delete(0, amount.length());
                        detail.delete(0, detail.length());
                        accCodebuffer.delete(0, accCodebuffer.length());
                    }

                    accCode = element[12].toString();
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("check2>>vhId:" + vhId + " VhidPrevious:"
                                + VhidPrevious + " code:" + code + " accCode:"
                                + accCode);
                    if (vhId == VhidPrevious && !code.equalsIgnoreCase(accCode)) {
                        if (LOGGER.isInfoEnabled())
                            LOGGER.info("inside vhId==VhidPrevious ");
                        vhType = element[1].toString();
                        // vhName=resultset1.getString("vhname");
                        String bLine = "<Br>";
                        currentDebit = new BigDecimal("0.00");
                        currentCredit = new BigDecimal("0.00");
                        cgn = element[2].toString();
                        vcDate = element[10].toString();
                        vcNum = element[11].toString();
                        funcCode = element[7].toString();
                        // bgtCode=resultset1.getString("BGCODE");
                        srcOfFinance = element[6].toString();
                        final String name[] = element[13].toString().split(" ");
                        int wordLength = 0;
                        String formatedName = "";
                        // String formatedAccCode="";
                        for (final String element2 : name) {
                            wordLength = element2.length();
                            if (formatedName.length()
                                    - formatedName.lastIndexOf("<Br>") + wordLength < 25)
                                formatedName = formatedName + " " + element2;
                            else {
                                formatedName = formatedName.concat("<Br>"
                                        + element2);
                                bLine = bLine.concat("<Br>");
                            }
                        }
                        detail = detail.append(" " + formatedName + "<br>");
                        accCodebuffer = accCodebuffer.append(" " + accCode
                                + bLine);
                        currentDebit = new BigDecimal(element[17].toString());
                        currentCredit = new BigDecimal(element[18].toString());
                        if (LOGGER.isInfoEnabled())
                            LOGGER.info("currentCredit:" + currentCredit
                                    + " currentDebit:" + currentDebit
                                    + " chequedebitTotal:" + chequedebitTotal
                                    + "chequecreditTotal:" + chequecreditTotal);
                        if (LOGGER.isInfoEnabled())
                            LOGGER.info(" BEFORE>>>>cashdebitTotal:"
                                    + cashdebitTotal + "cashcreditTotal:"
                                    + cashcreditTotal);
                        if (currentDebit.doubleValue() > 0) {
                            if (LOGGER.isInfoEnabled())
                                LOGGER.info("if purposeid:" + purposeid
                                        + ">>cashPId:" + cashPId);
                            // amount=amount.append(" " +
                            // numberformatter.format(currentDebit.doubleValue())
                            // + bLine);
                            amount = amount.append(" "
                                    + numberToString(currentDebit.toString())
                                    + bLine);
                            if (purposeid.equalsIgnoreCase(cashPId))
                                cashdebitTotal = cashdebitTotal
                                .add(currentDebit);
                            else
                                chequedebitTotal = chequedebitTotal
                                .add(currentDebit);
                        } else {
                            if (LOGGER.isInfoEnabled())
                                LOGGER.info("else purposeid:" + purposeid
                                        + ">>cashPId:" + cashPId);
                            // amount=amount.append(" " +
                            // numberformatter.format(currentCredit.doubleValue())
                            // + bLine);
                            amount = amount.append(" "
                                    + numberToString(currentCredit.toString())
                                    + bLine);
                            if (purposeid.equalsIgnoreCase(cashPId))
                                cashcreditTotal = cashcreditTotal
                                .add(currentCredit);
                            else
                                chequecreditTotal = chequecreditTotal
                                .add(currentCredit);
                        }
                        if (LOGGER.isInfoEnabled())
                            LOGGER.info("after adding currentCredit:"
                                    + currentCredit + " currentDebit:"
                                    + currentDebit + " chequedebitTotal:"
                                    + chequedebitTotal + "chequecreditTotal:"
                                    + chequecreditTotal);
                        if (LOGGER.isInfoEnabled())
                            LOGGER.info(" AFTER>>>>cashdebitTotal:"
                                    + cashdebitTotal + "cashcreditTotal:"
                                    + cashcreditTotal);
                        cgn = element[2].toString();
                        // if(LOGGER.isInfoEnabled()) LOGGER.info("cgn: "+cgn);

                    } else
                        purposeid = element[3].toString();
                    VhidPrevious = vhId;
                    if (element.equals(resultset1.get(resultset1.size() - 1))) {
                        final GeneralLedgerReportBean glbean = new GeneralLedgerReportBean(
                                "&nbsp;");

                        if (currentCredit.doubleValue() > 0) {
                            if (LOGGER.isInfoEnabled())
                                LOGGER.info("inside Receipt>>>>>>" + accCode);
                            glbean.setRcptVchrNo(vcNum);
                            if (vcDate != null && !vcDate.equalsIgnoreCase(""))
                                glbean.setRcptVchrDate(vcDate);
                            if (bgtCode != null
                                    && !bgtCode.equalsIgnoreCase(""))
                                glbean.setRcptBgtCode(bgtCode);
                            if (funcCode != null
                                    && !funcCode.equalsIgnoreCase(""))
                                glbean.setRcptFuncCode(funcCode);
                            if (purposeid.equalsIgnoreCase(cashPId)) {
                                if (amount != null && !amount.equals(""))
                                    glbean.setRcptcashInHandAmt(amount
                                            .toString());
                            } else if (amount != null && !amount.equals(""))
                                glbean.setRcptChqInHandAmt(amount
                                        .toString());
                            if (detail != null && !detail.equals(""))
                                glbean.setRcptParticulars(detail.toString());
                            if (srcOfFinance != null
                                    && !srcOfFinance.equals(""))
                                glbean.setRcptSrcOfFinance(srcOfFinance);
                            if (accCodebuffer != null
                                    && !accCodebuffer.equals(""))
                                glbean.setRcptAccCode(accCodebuffer.toString());
                            // if(accCode!=null && !accCode.equals(""))
                            // glbean.setRcptAccCode(accCode);

                        } else {
                            if (LOGGER.isInfoEnabled())
                                LOGGER.info("inside Payment>>>>>>" + accCode);
                            glbean.setPmtVchrNo(vcNum);
                            if (vcDate != null && !vcDate.equalsIgnoreCase(""))
                                glbean.setpmtVchrDate(vcDate);
                            if (bgtCode != null
                                    && !bgtCode.equalsIgnoreCase(""))
                                glbean.setPmtBgtCode(bgtCode);
                            if (funcCode != null
                                    && !funcCode.equalsIgnoreCase(""))
                                glbean.setPmtFuncCode(funcCode);
                            if (purposeid.equalsIgnoreCase(cashPId)) {
                                if (amount != null && !amount.equals(""))
                                    glbean.setPmtCashInHandAmt(amount
                                            .toString());
                            } else if (amount != null && !amount.equals(""))
                                glbean.setPmtChqInHandAmt(amount.toString());
                            if (detail != null && !detail.equals(""))
                                glbean.setPmtParticulars(detail.toString());
                            if (srcOfFinance != null
                                    && !srcOfFinance.equals(""))
                                glbean.setPmtSrcOfFinance(srcOfFinance);
                            // if(accCode!=null && !accCode.equals(""))
                            // glbean.setPmtAccCode(accCode);
                            if (accCodebuffer != null
                                    && !accCodebuffer.equals(""))
                                glbean.setPmtAccCode(accCodebuffer.toString());

                        }
                        glbean.setCGN(cgn);
                        reportBean.setStartDate(startDateformat1);
                        reportBean.setTotalCount(Integer.toString(totalCount));
                        reportBean.setIsConfirmedCount(Integer
                                .toString(isConfirmedCount));
                        dataList.add(glbean);
                        currVhDate = element[10].toString();
                        {
                            final GeneralLedgerReportBean glbeanCb = new GeneralLedgerReportBean(
                                    "&nbsp;");
                            glbeanCb.setPmtParticulars("<B>Closing: By balance c/d</B>");
                            glbeanCb.setPmtCashInHandAmt("<B>"
                                    + numberToString(cashcreditTotal.subtract(
                                            cashdebitTotal).toString())
                                            + "</B>");
                            glbeanCb.setPmtChqInHandAmt("<B>"
                                    + numberToString(chequecreditTotal
                                            .subtract(chequedebitTotal)
                                            .toString()) + "</B>");
                            dataList.add(glbeanCb);

                            final GeneralLedgerReportBean glbean1 = new GeneralLedgerReportBean(
                                    "<hr>&nbsp;</hr>");
                            glbean1.setRcptVchrDate("<hr><B>Total</B></hr>");
                            glbean1.setRcptcashInHandAmt("<hr><B>"
                                    + numberToString(cashcreditTotal.toString())
                                    + "</B></hr>");
                            glbean1.setPmtCashInHandAmt("<hr><B>"
                                    + numberToString(cashdebitTotal.add(
                                            cashcreditTotal
                                            .subtract(cashdebitTotal))
                                            .toString()) + "</B></hr>");
                            glbean1.setRcptChqInHandAmt("<hr><B>"
                                    + numberToString(chequecreditTotal
                                            .toString()) + "</B></hr>");
                            glbean1.setPmtChqInHandAmt("<hr><B>"
                                    + numberToString(chequedebitTotal
                                            .add(chequecreditTotal
                                                    .subtract(chequedebitTotal))
                                                    .toString()) + "</B></hr>");
                            dataList.add(glbean1);
                            final GeneralLedgerReportBean glbeanOb = new GeneralLedgerReportBean(
                                    "<hr>&nbsp;</hr>");
                            glbeanOb.setRcptParticulars("<hr><B>Opening: To balance b/d</B></hr>");
                            glbeanOb.setRcptcashInHandAmt("<hr><B>"
                                    + numberToString(cashcreditTotal.subtract(
                                            cashdebitTotal).toString())
                                            + "</B></hr>");
                            glbeanOb.setRcptcashInHandAmt("<hr><B>"
                                    + numberToString(cashcreditTotal.subtract(
                                            cashdebitTotal).toString())
                                            + "</B></hr>");
                            glbeanOb.setRcptChqInHandAmt("<hr><B>"
                                    + numberToString(chequecreditTotal
                                            .subtract(chequedebitTotal)
                                            .toString()) + "</B></hr>");
                            glbeanOb.setRcptChqInHandAmt("<hr><B>"
                                    + numberToString(chequecreditTotal
                                            .subtract(chequedebitTotal)
                                            .toString()) + "</B></hr>");
                            dataList.add(glbeanOb);
                        }
                    }
                } catch (final Exception e) {

                    LOGGER.error(
                            "error in resultset processing" + e.getMessage(), e);
                    throw taskExc;
                }

        } catch (final SQLException ex) {
            LOGGER.error("ERROR in  getGeneralLedgerList " + ex.getMessage(),
                    ex);
            throw taskExc;
        }
        if (LOGGER.isInfoEnabled())
            LOGGER.info("returning list");
        return dataList;
    }

    private String getQuery(final String engineQry) {
        return "SELECT distinct gl1.glcode as \"code\",vh.type as \"vhType\",vh.cgn as \"CGN\",coa.purposeid as \"purposeid\",case  coa.purposeid when 4 then 1 when 5 then 1 else 0 end as \"order\","
                + "(select ca.type from chartofaccounts ca where glcode=gl1.glcode) as \"glType\","
                + " (select name from fundsource where id=vh.FUNDSOURCEID) as \"fundsource\",(select name from function where id=gl.FUNCTIONID) as \"function\","
                + " vh.id AS \"vhid\", vh.voucherDate AS \"vDate\", "
                + "to_char(vh.voucherDate, 'dd-Mon-yyyy') AS \"voucherdate\", "
                + "vh.voucherNumber AS \"vouchernumber\", gl.glCode AS \"glcode\", "
                + "coa.name||case  vh.STATUS when 1 then '(Reversed)' when 2 then '(Reversal)' else '' end AS \"name\",case when gl.debitAmount = 0 then (case (gl.creditamount) when 0 then gl.creditAmount||'.00cr' when floor(gl.creditamount)    then gl.creditAmount||'.00cr' else  gl.creditAmount||'cr'  end ) else (case (gl.debitamount) when 0 then gl.debitamount||'.00dr' when floor(gl.debitamount)    then gl.debitamount||'.00dr' else  gl.debitamount||'dr' 	 end ) end AS \"amount\", "
                + "gl.description AS \"narration\", vh.name AS \"vhname\", "
                + "gl.debitamount  AS \"debitamount\", gl.creditamount  AS \"creditamount\",f.name as \"fundName\",  vh.isconfirmed as \"isconfirmed\"  "
                + "FROM generalLedger gl, voucherHeader vh, chartOfAccounts coa, generalLedger gl1, fund f "
                + "WHERE coa.glCode = gl.glCode AND gl.voucherHeaderId = vh.id AND gl.voucherHeaderId = vh.id "
                + " AND gl.voucherHeaderId = gl1.voucherHeaderId AND f.id=vh.fundId "
                + effTime
                + " AND gl1.glcode in (SELECT GLCODE FROM CHARTOFACCOUNTS WHERE PURPOSEID=4 or purposeid=5) "
                + " AND vh.id in ("
                + engineQry
                + " )"
                + " AND (gl.debitamount>0 OR gl.creditamount>0)  "
                + " order by \"vDate\",\"vhid\",\"order\" desc ";
    }

    private OpBalance getOpeningBalance(final String glCode, final String fundId,
            final String fundSourceId, final String fyId, final String tillDate)
                    throws TaskFailedException, SQLException {
        String fundCondition = "";
        String fundSourceCondition = "";
        double opDebit = 0, opCredit = 0;

        /** opening balance of the Year **/
        if (!fundId.equalsIgnoreCase(""))
            fundCondition = "fundId = ? AND ";
        if (!fundSourceId.equalsIgnoreCase(""))
            fundSourceCondition = "fundSourceId = ? AND ";
        final String queryYearOpBal = "SELECT case when sum(openingDebitBalance) = null then 0 else sum(openingDebitBalance) end AS \"openingDebitBalance\", "
                + "case when sum(openingCreditBalance) = null then 0 else sum(openingCreditBalance) AS \"openingCreditBalance\" "
                + "FROM transactionSummary WHERE "
                + fundCondition
                + fundSourceCondition
                + " financialYearId=? "
                + "AND glCodeId = (SELECT id FROM chartOfAccounts WHERE glCode in(?))";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("***********: OPBAL for glcode -->" + glCode
                    + " is-->: " + queryYearOpBal);

        int j = 1;
        pstmt = persistenceService.getSession()
                .createSQLQuery(queryYearOpBal);
        if (!fundId.equalsIgnoreCase(""))
            pstmt.setString(j++, fundId);
        if (!fundSourceId.equalsIgnoreCase(""))
            pstmt.setString(j++, fundSourceCondition);
        pstmt.setString(j++, fyId);
        pstmt.setString(j++, glCode);

        resultset = null;
        resultset = pstmt.list();
        for (final Object[] element : resultset) {
            opDebit = Double.parseDouble(element[0].toString());
            opCredit = Double.parseDouble(element[1].toString());
        }

        /** opening balance till the date from the start of the Year **/
        if (rType.equalsIgnoreCase("gl")) {
            final String startDate = commonFun.getStartDate(Integer.parseInt(fyId));
            if (!fundId.equalsIgnoreCase(""))
                fundCondition = "AND vh.fundId = ? ";
            if (!fundSourceId.equalsIgnoreCase(""))
                fundSourceCondition = "AND vh.fundId = ? ";
            String queryTillDateOpBal = "";
            // if(showRev.equalsIgnoreCase("on")){

            queryTillDateOpBal = "SELECT case when sum(gl.debitAmount) = null then 0 else sum(gl.debitAmount) end AS \"debitAmount\", "
                    + "case when sum(gl.creditAmount)  = null then 0 else sum(gl.creditAmount) end AS \"creditAmount\" "
                    + "FROM generalLedger gl, voucherHeader vh "
                    + "WHERE vh.id = gl.voucherHeaderId "
                    + "AND gl.glCode in(?) "
                    + fundCondition
                    + fundSourceCondition
                    + effTime
                    + "AND vh.voucherDate >= ? AND vh.voucherDate < ? AND vh.status<>4";

            if (LOGGER.isInfoEnabled())
                LOGGER.info("***********: tilldate OPBAL for glcode -->"
                        + glCode + " is-->: " + queryTillDateOpBal);

            int i = 1;
            pstmt = persistenceService.getSession().createSQLQuery(
                    queryTillDateOpBal);
            pstmt.setString(i++, glCode);
            if (!fundId.equalsIgnoreCase(""))
                pstmt.setString(i++, fundId);
            if (!fundSourceId.equalsIgnoreCase(""))
                pstmt.setString(i++, fundSourceId);
            pstmt.setString(i++, startDate);
            pstmt.setString(i++, tillDate);
            resultset = null;
            resultset = pstmt.list();
            for (final Object[] element : resultset) {
                opDebit = opDebit + Double.parseDouble(element[0].toString());
                opCredit = opCredit + Double.parseDouble(element[1].toString());
            }
        }
        final OpBalance opBal = new OpBalance();
        opBal.dr = opDebit;
        opBal.cr = opCredit;
        if (LOGGER.isInfoEnabled())
            LOGGER.info("opBal.dr:" + opBal.dr + " opBal.cr:" + opBal.cr);
        resultset = null;
        return opBal;
    }
    /**
     * 
     * @param startDate
     * @param endDate
     * @throws TaskFailedException
     * if start date is not provided then set financial year startdate
     * if end date is not provided then set financial year end date
     * 
     *        
     *     
     */    
    

    

    public String getMinCode(final String minGlCode) throws TaskFailedException {
        // if(LOGGER.isInfoEnabled()) LOGGER.info("coming");
        String minCode = "";
        try {
            final String query = "select glcode from chartofaccounts where glcode like ?|| '%' and classification = 4 order by glcode asc";
            pstmt = persistenceService.getSession().createSQLQuery(query);
            pstmt.setString(0, minGlCode);
            final List<Object[]> rset = pstmt.list();
            for (final Object[] element : rset)
                minCode = element[0].toString();
        } catch (final Exception sqlex) {
            LOGGER.error(
                    "Exception while getting minGlCode" + sqlex.getMessage(),
                    sqlex);
            throw taskExc;
        }
        return minCode;
    }

    public String getMaxCode(final String maxGlCode) throws TaskFailedException {
        String maxCode = "";
        try {
            final String query = "  select glcode from chartofaccounts where glcode like ?|| '%' and classification = 4 order by glcode desc";
            pstmt = persistenceService.getSession().createSQLQuery(query);
            pstmt.setString(0, maxGlCode);
            final List<Object[]> rset = pstmt.list();
            for (final Object[] element : rset)
                maxCode = element[0].toString();
        } catch (final Exception sqlex) {
            LOGGER.error(
                    "Exception while getting maxGlCode" + sqlex.getMessage(),
                    sqlex);
            throw taskExc;
        }
        return maxCode;
    }

    public String getCGN(final String id) throws TaskFailedException {
        String cgn = "";
        pstmt = null;
        List<Object[]> rsCgn = null;
        if (!id.equals(""))
            try {
                final String queryCgn = "select CGN from VOUCHERHEADER where id=?";
                pstmt = persistenceService.getSession().createSQLQuery(
                        queryCgn);
                pstmt.setString(0, id);
                rsCgn = pstmt.list();
                for (final Object[] element : rsCgn)
                    cgn = element[0].toString();

            } catch (final Exception sqlex) {
                LOGGER.error("cgnCatch#" + sqlex.getMessage(), sqlex);
                throw taskExc;
            }

        return cgn;
    }

    public void isCurDate(final String VDate) throws TaskFailedException {

        try {

            final String today = eGovernCommon.getCurrentDate();
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
                                                throw new Exception();

        } catch (final Exception ex) {
            LOGGER.error("Exception in isCurDate():" + ex.getMessage(), ex);
            throw new TaskFailedException(
                    "Date Should be within the today's date");
        }

    }

    private String[] getGlcode(final String bId) throws TaskFailedException {
        final String glcode[] = new String[2];
        List<Object[]> rs = null;

        try {

            final String query = "select glcode as \"glcode\" from chartofaccounts where id in (select cashinhand from codemapping where eg_boundaryid=?)";
            if (LOGGER.isInfoEnabled())
                LOGGER.info(query);
            pstmt = persistenceService.getSession().createSQLQuery(query);
            pstmt.setString(0, bId);
            rs = pstmt.list();
            for (final Object[] element : rs)
                glcode[0] = element[0].toString();
            final String str = "select glcode from chartofaccounts where id in (select chequeinHand from codemapping where eg_boundaryid=?)";
            pstmt = persistenceService.getSession().createSQLQuery(str);
            pstmt.setString(0, bId);
            rs = pstmt.list();
            for (final Object[] element : rs)
                glcode[1] = element[0].toString();
        } catch (final Exception e) {
            LOGGER.error("Inside getGlcode", e);
            throw taskExc;
        }
        return glcode;
    }

    private String getUlbDetails() throws TaskFailedException {

        List<Object[]> rs = null;
        String ulbName = "";
        Query pstmt = null;
        try {

            final String query = "select name as \"name\" from companydetail";
            pstmt = persistenceService.getSession().createSQLQuery(query);
            if (LOGGER.isInfoEnabled())
                LOGGER.info(query);
            rs = pstmt.list();
            for (final Object[] element : rs)
                ulbName = element[0].toString();
        } catch (final Exception e) {
            LOGGER.error("Inside getUlbDetails", e);
            throw taskExc;
        }
        return ulbName;
    }

    public static StringBuffer numberToString(final String strNumberToConvert) {
        String strNumber = "", signBit = "";
        if (strNumberToConvert.startsWith("-")) {
            strNumber = ""
                    + strNumberToConvert.substring(1,
                            strNumberToConvert.length());
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