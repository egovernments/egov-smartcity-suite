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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.utils.EGovConfig;
import org.springframework.beans.factory.annotation.Autowired;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;

@SuppressWarnings("deprecation")
class OpBalance {
    public double dr;
    public double cr;
}

public class CashBook {
    private static final Logger LOGGER = Logger.getLogger(CashBook.class);
    final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    final SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
    private final CommnFunctions commonFun = new CommnFunctions();
    Connection connection = null;
    Query pstmt = null;
    List<Object[]> resultset = null;
    List<Object[]> resultset1 = null;
    TaskFailedException taskExc;
    String startDate, endDate, effTime, rType = "gl";
    NumberFormat numberformatter = new DecimalFormat("##############0.00");
    @Autowired
    private FinancialYearHibernateDAO financialYearDAO;
    private @Autowired EGovernCommon eGovernCommon;
    private @Autowired ReportEngine engine;

    @PersistenceContext
    private EntityManager entityManager;

    public CashBook() {
    }

    public CashBook(final Connection con) {
        connection = con;
    }

    public static StringBuffer numberToString(final String strNumberToConvert) {
        String strNumber = "", signBit = "";
        if (strNumberToConvert.startsWith("-")) {
            strNumber = String.valueOf(strNumberToConvert.substring(1, strNumberToConvert.length()));
            signBit = "-";
        } else
            strNumber = String.valueOf(strNumberToConvert);
        final DecimalFormat dft = new DecimalFormat("##############0.00");
        final String strtemp = String.valueOf(dft.format(Double.parseDouble(strNumber)));
        StringBuffer strbNumber = new StringBuffer(strtemp);
        final int intLen = strbNumber.length();

        for (int i = intLen - 6; i > 0; i = i - 2)
            strbNumber.insert(i, ',');
        if (signBit.equals("-"))
            strbNumber = strbNumber.insert(0, "-");
        return strbNumber;
    }

    @SuppressWarnings({ "deprecation", "unchecked" })
    public List<GeneralLedgerReportBean> getGeneralLedgerList(final GeneralLedgerReportBean reportBean)
            throws TaskFailedException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside getGeneralLedgerList");
        final List<GeneralLedgerReportBean> dataList = new LinkedList<>();
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
                // startDate = commonFun.getStartDate(Integer.parseInt(finId));
                if (finYearByDate != null)
                    startDate = sdf.format(finYearByDate.getStartingDate());

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

            Date dt1 = new Date();
            try {
                dt1 = sdf.parse(endDate);
            } catch (ParseException e1) {
            }
            CFinancialYear finYearByDate = financialYearDAO.getFinYearByDate(dt1);
            final String fyId = finYearByDate.getId().toString();
            // final String fyId = commonFun.getFYID(endDate);
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

            ReportEngineBean reBean = engine
                    .populateReportEngineBean(reportBean);
            engine.getVouchersListQuery(reBean);

            final String query = getQuery(reBean.getQuery());
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("**************QUERY: " + query);

            try {
                pstmt = entityManager.createNativeQuery(query);
                reBean.getParams().entrySet().forEach(entry -> pstmt.setParameter(entry.getKey(), entry.getValue()));
            } catch (final Exception e) {
                LOGGER.error("Exception in creating statement:", e);
                throw taskExc;
            }
            resultset1 = pstmt.getResultList();
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
            glbeanOpBal.setRcptcashInHandAmt(
                    "<B>".concat(numberToString(String.valueOf(cashOpeningBalance)).toString()).concat("</B>"));
            glbeanOpBal.setRcptChqInHandAmt(
                    "<B>".concat(numberToString(String.valueOf(chequeOpeningBalance)).toString()).concat("</B>"));
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
                            glbeanCb.setPmtCashInHandAmt("<B>".concat(String.valueOf(numberToString(cashcreditTotal.subtract(
                                    cashdebitTotal).toString()))).concat("</B>"));
                            glbeanCb.setPmtChqInHandAmt("<B>".concat(String.valueOf(numberToString(chequecreditTotal
                                    .subtract(chequedebitTotal).toString()))).concat("</B>"));
                            dataList.add(glbeanCb);
                            final GeneralLedgerReportBean glbean1 = new GeneralLedgerReportBean(
                                    "<hr>&nbsp;</hr>");
                            glbean1.setRcptVchrDate("<hr><B>Total</B></hr>");
                            glbean1.setRcptcashInHandAmt(
                                    "<hr><B>".concat(String.valueOf(numberToString(cashcreditTotal.toString())))
                                            .concat("</B></hr>"));
                            glbean1.setPmtCashInHandAmt("<hr><B>"
                                    .concat(String.valueOf(numberToString(
                                            cashdebitTotal.add(cashcreditTotal.subtract(cashdebitTotal)).toString())))
                                    .concat("</B></hr>"));
                            glbean1.setRcptChqInHandAmt("<hr><B>"
                                    .concat(String.valueOf(numberToString(chequecreditTotal.toString()))).concat("</B></hr>"));
                            glbean1.setPmtChqInHandAmt(
                                    "<hr><B>".concat(String.valueOf(numberToString(chequedebitTotal.add(chequecreditTotal
                                            .subtract(chequedebitTotal)).toString()))).concat("</B></hr>"));
                            dataList.add(glbean1);
                            if (LOGGER.isInfoEnabled())
                                LOGGER.info(cashcreditTotal + ":crDr: "
                                        + cashdebitTotal);
                            final GeneralLedgerReportBean glbeanOb = new GeneralLedgerReportBean(
                                    "<hr>&nbsp;</hr>");
                            glbeanOb.setRcptParticulars("<hr><B>Opening: To balance b/d</B></hr>");
                            // glbeanOb.setRcptcashInHandAmt("<hr><B>"+(numberformatter.format(cashcreditTotal.subtract(cashdebitTotal).doubleValue()))+"</B></hr>");
                            // glbeanOb.setRcptChqInHandAmt("<hr><B>"+(numberformatter.format(chequecreditTotal.subtract(chequedebitTotal).doubleValue()))+"</B></hr>");
                            glbeanOb.setRcptcashInHandAmt("<hr><B>".concat(String.valueOf(numberToString(cashcreditTotal.subtract(
                                    cashdebitTotal).toString()))).concat("</B></hr>"));
                            glbeanOb.setRcptChqInHandAmt(
                                    "<hr><B>".concat(String.valueOf(numberToString(chequecreditTotal.subtract(chequedebitTotal)
                                            .toString()))).concat("</B></hr>"));
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
                                formatedName = formatedName.concat(element2);
                            else {
                                formatedName = formatedName.concat("<Br>".concat(element2));
                                bLine = bLine.concat("<Br>");
                            }
                        }
                        detail = detail.append(formatedName.concat("<br>"));
                        accCodebuffer = accCodebuffer.append(accCode.concat(bLine));
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
                            amount = amount.append(numberToString(currentDebit.toString()) + bLine);
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
                            glbeanCb.setPmtCashInHandAmt("<B>".concat(String.valueOf(numberToString(cashcreditTotal
                                    .subtract(cashdebitTotal).toString()))).concat("</B>"));
                            glbeanCb.setPmtChqInHandAmt("<B>".concat(String.valueOf(numberToString(chequecreditTotal
                                    .subtract(chequedebitTotal).toString()))).concat("</B>"));
                            dataList.add(glbeanCb);

                            final GeneralLedgerReportBean glbean1 = new GeneralLedgerReportBean(
                                    "<hr>&nbsp;</hr>");
                            glbean1.setRcptVchrDate("<hr><B>Total</B></hr>");
                            glbean1.setRcptcashInHandAmt(
                                    "<hr><B>".concat(String.valueOf(numberToString(cashcreditTotal.toString())))
                                            .concat("</B></hr>"));
                            glbean1.setPmtCashInHandAmt("<hr><B>".concat(
                                    String.valueOf(numberToString(cashdebitTotal.add(cashcreditTotal.subtract(cashdebitTotal))
                                            .toString())))
                                    .concat("</B></hr>"));
                            glbean1.setRcptChqInHandAmt("<hr><B>".concat(String.valueOf(numberToString(chequecreditTotal
                                    .toString()))).concat("</B></hr>"));
                            glbean1.setPmtChqInHandAmt(
                                    "<hr><B>".concat(String.valueOf(numberToString(chequedebitTotal.add(chequecreditTotal
                                            .subtract(chequedebitTotal)).toString()))).concat("</B></hr>"));
                            dataList.add(glbean1);
                            final GeneralLedgerReportBean glbeanOb = new GeneralLedgerReportBean(
                                    "<hr>&nbsp;</hr>");
                            glbeanOb.setRcptParticulars("<hr><B>Opening: To balance b/d</B></hr>");
                            glbeanOb.setRcptcashInHandAmt("<hr><B>".concat(String.valueOf(numberToString(cashcreditTotal.subtract(
                                    cashdebitTotal).toString()))).concat("</B></hr>"));
                            glbeanOb.setRcptcashInHandAmt("<hr><B>".concat(String.valueOf(numberToString(cashcreditTotal.subtract(
                                    cashdebitTotal).toString()))).concat("</B></hr>"));
                            glbeanOb.setRcptChqInHandAmt("<hr><B>".concat(String.valueOf(numberToString(chequecreditTotal
                                    .subtract(chequedebitTotal).toString()))).concat("</B></hr>"));
                            glbeanOb.setRcptChqInHandAmt("<hr><B>".concat(String.valueOf(numberToString(chequecreditTotal
                                    .subtract(chequedebitTotal).toString()))).concat("</B></hr>"));
                            dataList.add(glbeanOb);
                        }
                    }
                } catch (final Exception e) {

                    LOGGER.error(
                            "error in resultset processing", e);
                    throw taskExc;
                }

        } catch (final SQLException ex) {
            LOGGER.error("ERROR in  getGeneralLedgerList ", ex);
            throw taskExc;
        }
        if (LOGGER.isInfoEnabled())
            LOGGER.info("returning list");
        return dataList;
    }

    private String getQuery(final String engineQry) {
        return new StringBuilder(
                "SELECT distinct gl1.glcode as \"code\", vh.type as \"vhType\", vh.cgn as \"CGN\", coa.purposeid as \"purposeid\",")
                        .append(" case coa.purposeid when 4 then 1 when 5 then 1 else 0 end as \"order\",")
                        .append(" (select ca.type from chartofaccounts ca where glcode=gl1.glcode) as \"glType\",")
                        .append(" (select name from fundsource where id=vh.FUNDSOURCEID) as \"fundsource\",(select name")
                        .append(" from function where id=gl.FUNCTIONID) as \"function\",")
                        .append(" vh.id AS \"vhid\", vh.voucherDate AS \"vDate\", to_char(vh.voucherDate, 'dd-Mon-yyyy') AS \"voucherdate\",")
                        .append(" vh.voucherNumber AS \"vouchernumber\", gl.glCode AS \"glcode\", coa.name||case vh.STATUS when 1")
                        .append(" then '(Reversed)' when 2 then '(Reversal)' else '' end AS \"name\",")
                        .append(" case when gl.debitAmount = 0 then (case (gl.creditamount) when 0 then gl.creditAmount||'.00cr'")
                        .append(" when floor(gl.creditamount) then gl.creditAmount||'.00cr' else  gl.creditAmount||'cr'  end )")
                        .append(" else (case (gl.debitamount) when 0 then gl.debitamount||'.00dr'")
                        .append(" when floor(gl.debitamount) then gl.debitamount||'.00dr' else gl.debitamount||'dr' end ) end AS \"amount\", ")
                        .append(" gl.description AS \"narration\", vh.name AS \"vhname\", gl.debitamount  AS \"debitamount\",")
                        .append(" gl.creditamount  AS \"creditamount\", f.name as \"fundName\",")
                        .append(" vh.isconfirmed as \"isconfirmed\"")
                        .append(" FROM generalLedger gl, voucherHeader vh, chartOfAccounts coa, generalLedger gl1, fund f")
                        .append(" WHERE coa.glCode = gl.glCode AND gl.voucherHeaderId = vh.id AND gl.voucherHeaderId = vh.id")
                        .append(" AND gl.voucherHeaderId = gl1.voucherHeaderId AND f.id = vh.fundId ")
                        .append(effTime)
                        .append(" AND gl1.glcode in (SELECT GLCODE FROM CHARTOFACCOUNTS WHERE PURPOSEID = 4 or purposeid = 5) ")
                        .append(" AND exists (").append(engineQry)
                        .append(" and voucher.id = vh.id ) AND (gl.debitamount > 0 OR gl.creditamount > 0)")
                        .append(" order by \"vDate\", \"vhid\", \"order\" desc")
                        .toString();
    }

    @SuppressWarnings("unchecked")
    private OpBalance getOpeningBalance(final String glCode, final String fundId,
            final String fundSourceId, final String fyId, final String tillDate)
            throws TaskFailedException, SQLException {
        String fundCondition = "";
        String fundSourceCondition = "";
        double opDebit = 0, opCredit = 0;

        /** opening balance of the Year **/
        if (!fundId.equalsIgnoreCase(""))
            fundCondition = "fundId = :fundId AND ";
        if (!fundSourceId.equalsIgnoreCase(""))
            fundSourceCondition = "fundSourceId = :fundSourceId AND ";
        final StringBuilder queryYearOpBal = new StringBuilder(
                "SELECT case when sum(openingDebitBalance) = null then 0 else sum(openingDebitBalance) end AS \"openingDebitBalance\", ")
                        .append("case when sum(openingCreditBalance) = null then 0 else sum(openingCreditBalance)")
                        .append(" AS \"openingCreditBalance\" ")
                        .append(" FROM transactionSummary")
                        .append(" WHERE ")
                        .append(fundCondition)
                        .append(fundSourceCondition)
                        .append(" financialYearId = :financialYearId ")
                        .append(" AND glCodeId = (SELECT id FROM chartOfAccounts WHERE glCode in ( :glcode))");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("***********: OPBAL for glcode -->" + glCode
                    + " is-->: " + queryYearOpBal);

        pstmt = entityManager.createNativeQuery(queryYearOpBal.toString());
        if (!fundId.equalsIgnoreCase(""))
            pstmt.setParameter("fundId", fundId);
        if (!fundSourceId.equalsIgnoreCase(""))
            pstmt.setParameter("fundSourceId", fundSourceId);
        pstmt.setParameter("financialYearId", fyId)
                .setParameter("glcode", glCode);

        resultset = pstmt.getResultList();
        for (final Object[] element : resultset) {
            opDebit = Double.parseDouble(element[0].toString());
            opCredit = Double.parseDouble(element[1].toString());
        }

        /** opening balance till the date from the start of the Year **/
        if (rType.equalsIgnoreCase("gl")) {
            final String startDate = commonFun.getStartDate(Integer.parseInt(fyId));
            if (!fundId.equalsIgnoreCase(""))
                fundCondition = " AND vh.fundId = :fundId";
            if (!fundSourceId.equalsIgnoreCase(""))
                fundSourceCondition = " AND vh.fundId = :fundId";
            String queryTillDateOpBal = "";
            // if(showRev.equalsIgnoreCase("on")){

            queryTillDateOpBal = new StringBuilder(
                    "SELECT case when sum(gl.debitAmount) = null then 0 else sum(gl.debitAmount) end AS \"debitAmount\",")
                            .append(" case when sum(gl.creditAmount) = null then 0 else sum(gl.creditAmount) end AS \"creditAmount\"")
                            .append(" FROM generalLedger gl, voucherHeader vh")
                            .append(" WHERE vh.id = gl.voucherHeaderId AND gl.glCode in(:glcode) ")
                            .append(fundCondition).append(fundSourceCondition).append(effTime)
                            .append(" AND vh.voucherDate >= :vStartDate AND vh.voucherDate < :vEndDate AND vh.status <> 4")
                            .toString();

            if (LOGGER.isInfoEnabled())
                LOGGER.info("***********: tilldate OPBAL for glcode -->"
                        + glCode + " is-->: " + queryTillDateOpBal);

            pstmt = entityManager.createNativeQuery(queryTillDateOpBal);
            pstmt.setParameter("glcode", glCode);
            if (!fundId.equalsIgnoreCase(""))
                pstmt.setParameter("fundId", fundId);
            if (!fundSourceId.equalsIgnoreCase(""))
                pstmt.setParameter("fundSourceId", fundSourceId);
            pstmt.setParameter("vStartDate", startDate);
            pstmt.setParameter("vEndDate", tillDate);
            resultset = pstmt.getResultList();
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
     * @param minGlCode
     * @throws TaskFailedException if start date is not provided then set financial year startdate if end date is not provided
     * then set financial year end date
     */
    @SuppressWarnings("unchecked")
    public String getMinCode(final String minGlCode) throws TaskFailedException {

        String minCode = "";
        try {
            final StringBuilder query = new StringBuilder("select glcode")
                    .append(" from chartofaccounts")
                    .append(" where glcode like :glCode|| '%' and classification = 4 order by glcode asc");
            final List<Object[]> rset = entityManager.createNativeQuery(query.toString())
                    .setParameter("glCode", minGlCode)
                    .getResultList();
            for (final Object[] element : rset)
                minCode = element[0].toString();
        } catch (final Exception sqlex) {
            LOGGER.error(
                    "Exception while getting minGlCode",
                    sqlex);
            throw taskExc;
        }
        return minCode;
    }

    @SuppressWarnings("unchecked")
    public String getMaxCode(final String maxGlCode) throws TaskFailedException {
        String maxCode = "";
        try {
            final StringBuilder query = new StringBuilder("select glcode")
                    .append(" from chartofaccounts")
                    .append(" where glcode like :glCode|| '%' and classification = 4 order by glcode desc");
            final List<Object[]> rset = entityManager.createNativeQuery(query.toString())
                    .setParameter("glCode", maxGlCode)
                    .getResultList();
            for (final Object[] element : rset)
                maxCode = element[0].toString();
        } catch (final Exception sqlex) {
            LOGGER.error(
                    "Exception while getting maxGlCode",
                    sqlex);
            throw taskExc;
        }
        return maxCode;
    }

    @SuppressWarnings("unchecked")
    public String getCGN(final String id) throws TaskFailedException {
        String cgn = "";
        pstmt = null;
        if (!id.equals(""))
            try {
                final StringBuilder queryCgn = new StringBuilder("select CGN")
                        .append(" from VOUCHERHEADER")
                        .append(" where id = :id");
                List<Object[]> rsCgn = entityManager.createNativeQuery(queryCgn.toString())
                        .setParameter("id", id)
                        .getResultList();
                for (final Object[] element : rsCgn)
                    cgn = element[0].toString();

            } catch (final Exception sqlex) {
                LOGGER.error("cgnCatch#", sqlex);
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
                                    .parseInt(dt1[1]) ? 1
                                            : Integer
                                                    .parseInt(dt2[1]) < Integer
                                                            .parseInt(dt1[1]) ? -1
                                                                    : Integer
                                                                            .parseInt(dt2[0]) > Integer
                                                                                    .parseInt(dt1[0]) ? 1
                                                                                            : Integer
                                                                                                    .parseInt(dt2[0]) < Integer
                                                                                                            .parseInt(dt1[0]) ? -1
                                                                                                                    : 0;
            if (ret == -1)
                throw new Exception();

        } catch (final Exception ex) {
            LOGGER.error("Exception in isCurDate():", ex);
            throw new TaskFailedException(
                    "Date Should be within the today's date");
        }

    }

    @SuppressWarnings("unchecked")
    private String[] getGlcode(final String bId) throws TaskFailedException {
        final String glcode[] = new String[2];
        try {
            final StringBuilder query = new StringBuilder("select glcode as \"glcode\"")
                    .append(" from chartofaccounts")
                    .append(" where id in (select cashinhand from codemapping where eg_boundaryid = :boundaryId)");
            if (LOGGER.isInfoEnabled())
                LOGGER.info(query);
            List<Object[]> rs = entityManager.createNativeQuery(query.toString())
                    .setParameter("boundaryId", bId)
                    .getResultList();
            for (final Object[] element : rs)
                glcode[0] = element[0].toString();
            final StringBuilder str = new StringBuilder("select glcode")
                    .append(" from chartofaccounts")
                    .append(" where id in (select chequeinHand from codemapping where eg_boundaryid = :boundaryId)");
            rs = entityManager.createNativeQuery(str.toString())
                    .setParameter("boundaryId", bId)
                    .getResultList();
            for (final Object[] element : rs)
                glcode[1] = element[0].toString();
        } catch (final Exception e) {
            LOGGER.error("Inside getGlcode", e);
            throw taskExc;
        }
        return glcode;
    }

    @SuppressWarnings("unchecked")
    private String getUlbDetails() throws TaskFailedException {

        List<Object[]> rs = null;
        String ulbName = "";
        try {

            final StringBuilder query = new StringBuilder("select name as \"name\"")
                    .append(" from companydetail");
            if (LOGGER.isInfoEnabled())
                LOGGER.info(query);
            rs = entityManager.createNativeQuery(query.toString())
                    .getResultList();
            for (final Object[] element : rs)
                ulbName = element[0].toString();
        } catch (final Exception e) {
            LOGGER.error("Inside getUlbDetails", e);
            throw taskExc;
        }
        return ulbName;
    }

}
