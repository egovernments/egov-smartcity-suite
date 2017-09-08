/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */
/*
 * Created on Dec 21, 2005
 * @author Sumit
 */
package com.exilant.eGov.src.reports;

import com.exilant.GLEngine.GeneralLedgerBean;
import com.exilant.eGov.src.chartOfAccounts.CodeValidator;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.transactions.ExilPrecision;
import com.exilant.eGov.src.transactions.OpBal;
import com.exilant.exility.common.TaskFailedException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class GeneralLedgerReport {
    Query pstmt = null;
    List<Object[]> resultset = null;
    List<Object[]> resultset1 = null;
    String accEntityId = null;
    String accEntityKey = null;
    BigDecimal slDrAmount = new BigDecimal("0.00");
    BigDecimal slCrAmount = new BigDecimal("0.00");
    String engineQry = null;
    private static TaskFailedException taskExc;
    String startDate, endDate, effTime, rType = "gl";
    private static final Logger LOGGER = Logger.getLogger(GeneralLedgerReport.class);
    com.exilant.eGov.src.transactions.OpBal OpBal = new com.exilant.eGov.src.transactions.OpBal();
    DecimalFormat dft = new DecimalFormat("##############0.00");

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    EGovernCommon eGovernCommon;
    @Autowired
    private AppConfigValueService appConfigValuesService;
    @Autowired
    private ReportEngine engine;
    @Autowired
    private FinancialYearHibernateDAO financialYearDAO;
    private @Autowired CommnFunctions commnFunctions;

    public GeneralLedgerReport() {
    }

    /**
     * glcode2 is not used at all
     *
     * @param reportBean
     * @return
     * @throws TaskFailedException
     */

    public LinkedList getGeneralLedgerList(final GeneralLedgerReportBean reportBean) throws TaskFailedException {
        final LinkedList dataList = new LinkedList();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Indise the loop..........");
        new CashBook(null);

        String isconfirmed = "";
        String glCode1 = "";
        glCode1 = reportBean.getGlCode1();
        try {
            final String snapShotDateTime = reportBean.getSnapShotDateTime();
            if (snapShotDateTime.equalsIgnoreCase(""))
                effTime = "";
            else
                effTime = eGovernCommon.getEffectiveDateFilter(snapShotDateTime);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw taskExc;
        }
        final String fundId = reportBean.getFund_id();
        final String deptId = reportBean.getDepartmentId();
        final String fundSourceId = reportBean.getFundSource_id();
        reportBean.setFundName(getFundName(fundId));
        reportBean.setAccountCode(getAccountName(glCode1));
        String formstartDate = "";
        String formendDate = "";
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
        Date dt = new Date();
        final String endDate1 = reportBean.getEndDate();
        isCurDate(endDate1);
        try {
            endDate = reportBean.getEndDate();
            dt = sdf.parse(endDate);
            formendDate = formatter1.format(dt);
        } catch (final Exception e) {
            LOGGER.error("inside the try-startdate" + e, e);
            throw taskExc;
        }
        try {
            startDate = reportBean.getStartDate();
            if (!startDate.equalsIgnoreCase("null")) {
                dt = sdf.parse(startDate);
                formstartDate = formatter1.format(dt);
            }

            if (startDate.equalsIgnoreCase("null")) {
                final CFinancialYear finYearByDate = financialYearDAO.getFinYearByDate(dt);
                if (finYearByDate != null)
                    startDate = formatter1.format(finYearByDate.getStartingDate());
                // SETTING START DATE IN reportBean
                reportBean.setStartDate(startDate);
                final Date dtOBj = sdf.parse(startDate);
                startDate = formatter1.format(dtOBj);
            } else
                startDate = formstartDate;
        } catch (final Exception e) {
            LOGGER.error("inside the try-startdate" + e, e);
            throw taskExc;
        }

        accEntityId = reportBean.getAccEntityId();
        accEntityKey = reportBean.getAccEntityKey();
        endDate = formendDate;
        final String startDateformat = startDate;
        String startDateformat1 = "";
        try {
            dt = formatter1.parse(startDateformat);
            startDateformat1 = sdf.format(dt);
        } catch (final Exception e) {
            LOGGER.error("Parse Exception" + e, e);
            throw taskExc;
        }
        Date dd = new Date();

        final String endDateformat = endDate;

        try {
            dd = formatter1.parse(endDateformat);
        } catch (final ParseException e1) {
        }
        final CFinancialYear finYearByDate = financialYearDAO.getFinYearByDate(dd);

        final String fyId = finYearByDate.getId().toString();

        if (fyId.equalsIgnoreCase("")) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Financial Year Not Valid");
            throw taskExc;
        }
        CodeValidator.getInstance();
        /*
         * if (!cv.isValidCode(glCode1)) { LOGGER.error(glCode1 + " Not Valid");
         * throw taskExc; }
         */
        double txnDrSum = 0, txnCrSum = 0, openingBalance = 0, closingBalance = 0;

        // String query = getQuery(glCode1,fundId, fundSourceId, startDate,
        // endDate);

        // engine.setAppConfigValuesService(appConfigValuesService);
        final ReportEngineBean reBean = engine.populateReportEngineBean(reportBean);
        engineQry = engine.getVouchersListQuery(reBean);

        final String query = getQuery(glCode1, startDate, endDate, accEntityId, accEntityKey, reportBean.getFieldId(),
                reBean.getFunctionId());
        final String functionId = reBean.getFunctionId();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("**************QUERY: " + query);
        try {

            try {
                pstmt = persistenceService.getSession().createSQLQuery(query);
            } catch (final Exception e) {
                LOGGER.error("Exception in creating statement:" + pstmt, e);
                throw taskExc;
            }

            final List list = pstmt.list();
            resultset1 = list;
            list.toArray();
            final ArrayList data = new ArrayList();
            String accCode = "", vcNum = "", vcDate = "", narration = "", vcTypeName = "", voucherHeaderId = "";
            StringBuffer detail = new StringBuffer();
            StringBuffer amount = new StringBuffer();
            int vhId = 0, curVHID = 0, cout = 0, VhidPrevious = 0;
            final int lenAfterAppend = 0, lenBeforeAppend = 0, lenDetailBefore = 0, lenDetailAfter = 0;
            double txnDebit = 0, txnCredit = 0, previousDebit = 0, previousCredit = 0;
            String code = "", currCode = "", accCodePrevious = "", cgn = "";
            /**
             * When using ResultSet.TYPE_SCROLL_INSENSITIVE in createStatement
             * if no records are there, rs.next() will return true but when
             * trying to access (rs.getXXX()), it will throw an error
             **/
            int totalCount = 0, isConfirmedCount = 0;
            String vn2 = "";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("resultset1---------------------------->" + resultset1);
            if (resultset1 == null || resultset1.size() == 0) { // Will consider
                // the startdate
                // of report as
                // the end date
                // of the
                // opening
                // balance.
                // Actually it considers 1 date less than startdate or you can
                // say
                // opb<startdate
                startDate = sdf.format(formatter1.parse(startDate));
                final OpBal opbal = getOpeningBalance(glCode1, fundId, fundSourceId, fyId, accEntityId, accEntityKey,
                        startDate, functionId, deptId);
                final String arr[] = new String[15];
                openingBalance = opbal.dr - opbal.cr;
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("openingBalance--------------->" + openingBalance);

                final String sqlString = "select name as \"glname\" from chartofaccounts where glcode=?";
                pstmt = persistenceService.getSession().createSQLQuery(sqlString);
                pstmt.setString(0, glCode1);
                final List res = pstmt.list();
                String aName = "";
                if (res != null)
                    aName = res.get(0).toString();
                arr[1] = "";
                arr[2] = arr[3] = arr[6] = arr[7] = arr[10] = arr[11] = arr[12] = arr[13] = "";
                arr[14] = "";
                if (vhId == 0)
                    arr[8] = "";
                arr[9] = glCode1 + "-" + aName;
                if (openingBalance > 0) {
                    arr[4] = "" + numberToString(((Double) Math.abs(openingBalance)).toString()).toString() + "";
                    arr[5] = "";

                } else if (openingBalance < 0) {
                    arr[4] = "";
                    arr[5] = "" + numberToString(((Double) Math.abs(openingBalance)).toString()).toString() + "";
                } else {
                    arr[4] = "";
                    arr[5] = "";
                }
                arr[0] = "Opening Balance";
                if (vhId == 0 && !(openingBalance > 0 || openingBalance < 0)) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("Inside if condition");
                } else
                    data.add(arr);

                final String arr2[] = new String[15];
                closingBalance = openingBalance;
                if (closingBalance > 0) {
                    arr2[4] = "";
                    arr2[5] = "" + numberToString(((Double) Math.abs(closingBalance)).toString()).toString() + "";
                } else if (closingBalance < 0) {
                    arr2[4] = "" + numberToString(((Double) Math.abs(closingBalance)).toString()).toString() + "";
                    arr2[5] = "";
                } else {
                    arr2[4] = "";
                    arr2[5] = "";
                }
                arr2[2] = "";
                arr2[0] = "Closing Balance";
                arr2[1] = "";
                arr2[3] = arr2[6] = arr2[7] = arr2[8] = arr2[9] = arr2[10] = arr2[11] = arr[12] = arr[13] = "";
                arr2[14] = "";
                data.add(arr2);
            }
            for (final Object[] element : resultset1) {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info(" inside resultset");
                try {
                    code = element[0].toString();
                    if (element[14] == null)
                        isconfirmed = "";
                    else
                        isconfirmed = element[14].toString();
                    // 9 is the dummy value used in the query
                    // To display X in Y are unconfirmed
                    if (isconfirmed != null && !isconfirmed.equalsIgnoreCase("") && !isconfirmed.equalsIgnoreCase("9")) {
                        final String vn1 = element[5].toString();
                        if (!vn1.equalsIgnoreCase(vn2)) {
                            vn2 = vn1;
                            totalCount = totalCount + 1;
                            if (isconfirmed.equalsIgnoreCase("0"))
                                isConfirmedCount = isConfirmedCount + 1;
                        }
                    }

                    // cout1=0;
                    vhId = Integer.parseInt(element[2].toString());

                    /**
                     * When the main GLCODES are changing.We need to get the
                     * opening balance first.
                     */
                    if (!code.equals(currCode)) {
                        // glType=resultset1.getString("glType");
                        final String arr[] = new String[15];
                        startDate = sdf.format(formatter1.parse(startDate));
                        final OpBal opbal = getOpeningBalance(code, fundId, fundSourceId, fyId, accEntityId,
                                accEntityKey, startDate, functionId, deptId);
                        openingBalance = opbal.dr - opbal.cr;
                        String fundName = "";
                        if (element[13].toString() != null)
                            fundName = element[13].toString();
                        final String sqlString1 = "select name as \"glname\" from chartofaccounts where glcode=?";
                        pstmt = persistenceService.getSession().createSQLQuery(sqlString1);
                        pstmt.setString(0, code);
                        final List res = pstmt.list();
                        String aName = "";
                        if (res != null)
                            aName = res.get(0).toString();

                        arr[1] = "";
                        arr[2] = arr[3] = arr[6] = arr[7] = arr[10] = arr[11] = arr[12] = arr[13] = "";
                        arr[14] = "";
                        if (vhId == 0)
                            arr[8] = "";
                        else
                            arr[8] = fundName;
                        arr[9] = code + "-" + aName;
                        if (openingBalance > 0) {
                            arr[4] = "" + numberToString(((Double) Math.abs(openingBalance)).toString()).toString()
                                    + "";
                            arr[5] = "";
                        } else if (openingBalance < 0) {
                            arr[4] = "";
                            arr[5] = "" + numberToString(((Double) Math.abs(openingBalance)).toString()).toString()
                                    + "";
                        } else {
                            arr[4] = "";
                            arr[5] = "";
                        }
                        arr[0] = "Opening Balance";
                        if (vhId == 0 && !(openingBalance > 0 || openingBalance < 0)) {
                            if (LOGGER.isDebugEnabled())
                                LOGGER.debug("Inside if");
                        } else
                            data.add(arr);

                        currCode = code;
                    }// End If glcodes changing
                } catch (final Exception ex) {
                    LOGGER.error("ERROR (not an error): ResultSet is Empty", ex);
                    throw taskExc;
                }
                // Vouchers are changing
                if (curVHID > 0 && vhId != curVHID && cout == 0 && vhId != 0) {

                    if (txnDebit > 0) {
                        previousDebit = 0;
                        previousCredit = 0;
                        final String arr9[] = new String[15];
                        arr9[0] = vcDate;
                        arr9[1] = vcNum;
                        arr9[14] = voucherHeaderId;

                        arr9[2] = detail.toString();
                        arr9[3] = "";
                        arr9[4] = numberToString(((Double) txnDebit).toString()) + "";
                        arr9[5] = "";
                        if (narration != null)
                            arr9[6] = "" + narration;
                        else
                            arr9[6] = "";
                        arr9[7] = cgn;
                        txnDrSum = txnDrSum + txnDebit;
                        txnCrSum = txnCrSum + txnCredit;

                        arr9[10] = "";
                        arr9[11] = "";

                        // End
                        arr9[8] = arr9[9] = "";
                        arr9[12] = vcTypeName;
                        arr9[13] = "";
                        data.add(arr9);
                    } else if (txnCredit > 0) {
                        previousDebit = 0;
                        previousCredit = 0;
                        final String arr9[] = new String[15];
                        arr9[0] = "";
                        arr9[1] = "";
                        arr9[2] = "";
                        arr9[3] = detail.toString();
                        arr9[5] = numberToString(((Double) txnCredit).toString()) + "";
                        arr9[4] = "";
                        if (narration != null)
                            arr9[6] = "" + narration;
                        else
                            arr9[6] = "";
                        arr9[7] = cgn;
                        txnDrSum = txnDrSum + txnDebit;
                        txnCrSum = txnCrSum + txnCredit;
                        arr9[10] = vcDate;
                        arr9[11] = vcNum;
                        arr9[12] = "";
                        arr9[13] = vcTypeName;
                        arr9[14] = voucherHeaderId;
                        // End
                        arr9[8] = arr9[9] = "";
                        data.add(arr9);
                    }
                    detail.delete(0, detail.length());
                    amount.delete(0, amount.length());
                    // cnt = 0;
                    vcDate = vcNum = voucherHeaderId = accCode = narration = vcTypeName = "";
                }// End If
                curVHID = vhId;
                cout = 0;
                accCode = element[6].toString();
                String detailId = null;
                if (!accEntityKey.equals(""))
                    detailId = element[15].toString();
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("accEntityKey---->" + accEntityKey);
                if (!accCode.equalsIgnoreCase(accCodePrevious)) {
                    previousDebit = 0;
                    previousCredit = 0;
                }

                if (accCode.equalsIgnoreCase(code)) {
                    if (detailId != null && !detailId.equals(accEntityKey)) {
                        slDrAmount = slDrAmount.add(new BigDecimal(element[11].toString()));
                        slCrAmount = slCrAmount.add(new BigDecimal(element[12].toString()));
                    }
                } else if (!accEntityKey.equals("")) {
                    /*
                     * if(slCrAmount.compareTo(BigDecimal.ZERO)!=0) { detail=
                     * detail.append(" " + glCode1+"&nbsp;&nbsp;&nbsp;"+
                     * element[8].toString()); slCrAmount=new
                     * BigDecimal("0.00"); } else
                     * if(slDrAmount.compareTo(BigDecimal.ZERO)!=0) { detail=
                     * detail.append(" " + glCode1+"&nbsp;&nbsp;&nbsp;"+
                     * element[8].toString()); slDrAmount=new
                     * BigDecimal("0.00"); }
                     */
                    // detail= detail.append(" " + glCode1+"&nbsp;&nbsp;&nbsp;"+
                    // element[8].toString());
                    slCrAmount = new BigDecimal("0.00");
                    slDrAmount = new BigDecimal("0.00");
                }

                if (vhId != 0 && (detailId == null || detailId.equals(accEntityKey)) && !accEntityKey.equals("")) {
                    // get the details other than patriculars
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("detailId-->" + detailId + "accCode-->" + accCode + "::code:" + code);
                    if (accCode.equalsIgnoreCase(code)) {
                        if (LOGGER.isDebugEnabled())
                            LOGGER.debug("accCode...................." + accCode);
                        double currentDebit = 0, currentCredit = 0, debit = 0, credit = 0;
                        if (vhId == VhidPrevious && accCode.equalsIgnoreCase(accCodePrevious)) {
                            if (LOGGER.isDebugEnabled())
                                LOGGER.debug("vhId:::::::::::::::::" + vhId);
                            vcDate = element[4].toString();
                            vcNum = element[5].toString();
                            voucherHeaderId = element[2].toString();
                            vcTypeName = element[16].toString();
                            final String vhId1 = element[2].toString();
                            if (LOGGER.isInfoEnabled())
                                LOGGER.info("vhId1:::" + vhId1);
                            // cgn = cashbook.getCGN(vhId1);
                            // type = resultset1.getString("type");
                            if (detailId != null) {
                                currentDebit = Double.parseDouble(element[11].toString());
                                currentCredit = Double.parseDouble(element[12].toString());
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
                                previousDebit = previousDebit + currentDebit;
                                previousCredit = previousCredit + currentCredit;
                            }
                            narration = element[9] != null ? element[9].toString() : StringUtils.EMPTY;
                        } else {
                            vcDate = element[4].toString();
                            vcNum = element[5].toString();
                            voucherHeaderId = element[2].toString();
                            vcTypeName = element[16].toString();
                            final String vhId1 = element[2].toString();
                            if (LOGGER.isInfoEnabled())
                                LOGGER.info("vhId1:::" + vhId1);
                            // cgn = cashbook.getCGN(vhId1);
                            // type = resultset1.getString("type");
                            if (detailId != null) {
                                txnDebit = Double.parseDouble(element[11].toString());
                                previousDebit = txnDebit;
                                txnCredit = Double.parseDouble(element[12].toString());
                                previousCredit = txnCredit;
                            }
                            narration = element[9] != null ? element[9].toString() : StringUtils.EMPTY;
                        }
                    } else if (vhId == VhidPrevious && accCode.equalsIgnoreCase(accCodePrevious)) {
                        double currentDebit = 0, currentCredit = 0, debit = 0, credit = 0;
                        String debitAmount = "", creditAmount = "";
                        amount.delete(lenBeforeAppend, lenAfterAppend);
                        detail.delete(lenDetailBefore, lenDetailAfter);

                        detail = detail.append(" " + element[6].toString() + "" + element[8].toString());
                        currentDebit = Double.parseDouble(element[11].toString());
                        currentCredit = Double.parseDouble(element[12].toString());
                        debit = previousDebit + currentDebit - (previousCredit + currentCredit);
                        if (debit > 0) {
                            debitAmount = "Dr." + ExilPrecision.convertToString(debit, 2) + "0";
                            amount = amount.append(" " + debitAmount);
                        }
                        credit = previousCredit + currentCredit - (previousDebit + currentDebit);
                        if (credit > 0) {
                            creditAmount = "Cr." + ExilPrecision.convertToString(credit, 2) + "0";
                            amount = amount.append(" " + creditAmount);
                        }

                    } else {
                        detail = detail.append(" " + element[6].toString() + "" + element[8].toString());
                        previousDebit = Double.parseDouble(element[11].toString());
                        previousCredit = Double.parseDouble(element[12].toString());
                    }
                } else if (vhId != 0 && accEntityKey.equals("")) {
                    // if(LOGGER.isDebugEnabled())
                    // LOGGER.debug(" inside vhId != 0");
                    // get the details other than patriculars
                    if (accCode.equalsIgnoreCase(code)) {
                        double currentDebit = 0, currentCredit = 0, debit = 0, credit = 0;
                        if (vhId == VhidPrevious && accCode.equalsIgnoreCase(accCodePrevious) // &&
                                // (StringUtils.isEmpty(reBean.getFunctionId())
                                // ||
                                // reBean.getFunctionId().equals(resultset1.getString("functionid")))
                                ) {
                            vcDate = element[4].toString();
                            vcNum = element[5].toString();
                            voucherHeaderId = element[2].toString();
                            vcTypeName = element[16].toString();
                            final String vhId1 = element[2].toString();
                            if (LOGGER.isInfoEnabled())
                                LOGGER.info("vhId1:::" + vhId1);
                            // cgn = cashbook.getCGN(vhId1);
                            // type = resultset1.getString("type");
                            currentDebit = Double.parseDouble(element[11].toString());
                            currentCredit = Double.parseDouble(element[12].toString());
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
                            narration = element[9] != null ? element[9].toString() : StringUtils.EMPTY;
                            /*
                             * previousDebit=currentDebit;
                             * previousCredit=currentCredit;
                             */
                            previousDebit = txnDebit;
                            previousCredit = txnCredit;
                        } else // if
                            // (StringUtils.isEmpty(reBean.getFunctionId())
                            // ||
                            // reBean.getFunctionId().equals(resultset1.getString("functionid")))
                        {
                            vcDate = element[4].toString();
                            vcNum = element[5].toString();
                            voucherHeaderId = element[2].toString();
                            vcTypeName = element[16].toString();
                            final String vhId1 = element[2].toString();
                            if (LOGGER.isInfoEnabled())
                                LOGGER.info("vhId1:::" + vhId1);
                            cgn = "";
                            // type = resultset1.getString("type");
                            txnDebit = Double.parseDouble(element[11].toString());
                            previousDebit = txnDebit;
                            txnCredit = Double.parseDouble(element[12].toString());
                            previousCredit = txnCredit;
                            narration = element[9] != null ? element[9].toString() : StringUtils.EMPTY;
                        }
                        /*
                         * else { detail= detail.append(" " +
                         * element[6].toString()+"&nbsp;&nbsp;&nbsp;"+
                         * element[8].toString()); }
                         */
                    } else if (vhId == VhidPrevious && accCode.equalsIgnoreCase(accCodePrevious) // &&
                            // (StringUtils.isEmpty(reBean.getFunctionId())
                            // ||
                            // reBean.getFunctionId().equals(resultset1.getString("functionid")))
                            ) {
                        double currentDebit = 0, currentCredit = 0, debit = 0, credit = 0;
                        String debitAmount = "", creditAmount = "";

                        amount.delete(lenBeforeAppend, lenAfterAppend);
                        detail.delete(lenDetailBefore, lenDetailAfter);

                        detail = detail.append(" " + element[6].toString() + "" + element[8].toString());
                        currentDebit = Double.parseDouble(element[11].toString());
                        currentCredit = Double.parseDouble(element[12].toString());
                        debit = previousDebit + currentDebit - (previousCredit + currentCredit);
                        if (debit > 0) {
                            debitAmount = "Dr." + ExilPrecision.convertToString(debit, 2) + "0";
                            amount = amount.append(" " + debitAmount);
                        }
                        credit = previousCredit + currentCredit - (previousDebit + currentDebit);
                        if (credit > 0) {
                            creditAmount = "Cr." + ExilPrecision.convertToString(credit, 2) + "0";
                            amount = amount.append(" " + creditAmount);
                        }

                    } else {
                        detail = detail.append(" " + element[6].toString() + "" + element[8].toString());
                        previousDebit = Double.parseDouble(element[11].toString());
                        previousCredit = Double.parseDouble(element[12].toString());
                    }
                } else if (vhId != 0 && !accEntityKey.equals(""))
                    detail = detail.append(" " + element[6].toString() + "" + element[8].toString());

                accCodePrevious = accCode;
                VhidPrevious = vhId;
                if (element.equals(resultset1.get(resultset1.size() - 1))) {

                    if (txnDebit > 0) {
                        final String arr[] = new String[15];
                        arr[0] = vcDate;
                        arr[1] = vcNum;
                        arr[14] = voucherHeaderId;
                        arr[2] = detail.toString();
                        arr[3] = "";
                        arr[4] = numberToString(((Double) txnDebit).toString()) + "";
                        arr[5] = "";

                        if (narration != null)
                            arr[6] = "" + narration;
                        else
                            arr[6] = "";
                        txnDrSum = txnDrSum + txnDebit;
                        txnCrSum = txnCrSum + txnCredit;
                        arr[8] = arr[9] = "";
                        arr[4] = arr[4].equalsIgnoreCase(".00") ? "" : arr[4];
                        arr[7] = cgn;
                        arr[10] = "";
                        arr[11] = "";
                        arr[12] = vcTypeName;
                        arr[13] = "";

                        data.add(arr);
                    } else if (txnCredit > 0) {
                        final String arr[] = new String[15];
                        arr[0] = "";
                        arr[1] = "";
                        arr[2] = "";
                        arr[3] = detail.toString();
                        arr[4] = "";
                        arr[5] = numberToString(((Double) txnCredit).toString()) + "";
                        if (narration != null)
                            arr[6] = "" + narration;
                        else
                            arr[6] = "";
                        txnDrSum = txnDrSum + txnDebit;
                        txnCrSum = txnCrSum + txnCredit;
                        arr[8] = arr[9] = "";
                        arr[5] = arr[5].equalsIgnoreCase(".00") ? "" : arr[5];
                        arr[7] = cgn;
                        arr[10] = vcDate;
                        arr[11] = vcNum;
                        arr[12] = "";
                        arr[13] = vcTypeName;
                        arr[14] = voucherHeaderId;
                        data.add(arr);
                    }
                    detail.delete(0, detail.length());
                    amount.delete(0, amount.length());
                    // cnt = 0;
                    vcDate = vcNum = voucherHeaderId = accCode = narration = "";
                    final String arr2[] = new String[15];
                    if (openingBalance > 0)
                        txnDrSum = txnDrSum + Math.abs(openingBalance);
                    else
                        txnCrSum = txnCrSum + Math.abs(openingBalance);
                    closingBalance = txnDrSum - txnCrSum;
                    if (closingBalance > 0) {
                        txnCrSum = txnCrSum + Math.abs(closingBalance);
                        arr2[4] = "";
                        arr2[5] = "" + numberToString(((Double) Math.abs(closingBalance)).toString()).toString() + "";
                    } else if (closingBalance < 0) {
                        txnDrSum = txnDrSum + Math.abs(closingBalance);
                        arr2[4] = "" + numberToString(((Double) Math.abs(closingBalance)).toString()).toString() + "";
                        arr2[5] = "";
                    } else {
                        arr2[4] = "";
                        arr2[5] = "";
                    }
                    arr2[2] = "";
                    arr2[0] = "Closing Balance";
                    arr2[1] = "";
                    arr2[3] = arr2[6] = arr2[7] = arr2[8] = arr2[9] = arr2[10] = arr2[11] = arr2[12] = arr2[13] = "";
                    data.add(arr2);
                    final String arr1[] = new String[15];
                    if (txnDrSum > 0)
                        arr1[4] = "" + numberToString(((Double) txnDrSum).toString()) + "";
                    else
                        arr1[4] = "";
                    if (txnCrSum > 0)
                        arr1[5] = "" + numberToString(((Double) txnDrSum).toString()) + "";
                    else
                        arr1[5] = "";
                    arr1[2] = "";
                    arr1[0] = "Total";
                    arr1[1] = "";
                    arr1[3] = arr1[6] = arr1[7] = arr1[8] = arr1[9] = arr1[10] = arr1[11] = arr1[12] = arr1[13] = "";
                    data.add(arr1);
                    txnDrSum = 0;
                    txnCrSum = 0;
                }// End If last
            }// End While

            // Adding data to 2 dimension array to pass to Linkedlist
            final String gridData[][] = new String[data.size() + 1][15];
            gridData[0][0] = "voucherdate";
            gridData[0][1] = "vouchernumber";
            gridData[0][2] = "debitparticular";
            gridData[0][3] = "creditparticular";
            gridData[0][4] = "debitamount";
            gridData[0][5] = "creditamount";
            gridData[0][6] = "narration";
            gridData[0][7] = "cgn";
            gridData[0][8] = "fund";
            gridData[0][9] = "glcode";
            gridData[0][10] = "creditdate";
            gridData[0][11] = "creditvouchernumber";
            gridData[0][12] = "debitVoucherTypeName";
            gridData[0][13] = "creditVoucherTypeName";
            gridData[0][14] = "vhId";
            for (int i = 1; i <= data.size(); i++)
                gridData[i] = (String[]) data.get(i - 1);

            for (int i = 1; i <= data.size(); i++) {
                final GeneralLedgerBean generalLedgerBean = new GeneralLedgerBean();
                generalLedgerBean.setGlcode(gridData[i][9]);
                generalLedgerBean.setVoucherdate(gridData[i][0]);
                generalLedgerBean.setVouchernumber(gridData[i][1]);
                int counter = 0;

                final String testTemp = gridData[i][2];
                final char testArrayTemp[] = testTemp.toCharArray();

                for (counter = 0; counter < testArrayTemp.length; counter++)
                    if (testArrayTemp[counter] == '<'
                    && (testArrayTemp[counter + 1] == 'b' || testArrayTemp[counter + 1] == 'B'))
                        break;
                generalLedgerBean.setDebitparticular(gridData[i][2]);
                final String test = gridData[i][7];
                final char testArray[] = test.toCharArray();

                for (counter = 0; counter < testArray.length; counter++)
                    if (testArray[counter] == 'r')
                        break;

                generalLedgerBean.setNarration(gridData[i][6]);
                generalLedgerBean.setCreditparticular(gridData[i][3]);
                generalLedgerBean.setDebitamount(gridData[i][4]);
                generalLedgerBean.setCreditamount(gridData[i][5]);
                generalLedgerBean.setFund(gridData[i][8]);
                if (i == data.size())
                    generalLedgerBean.setCGN("");
                else
                    generalLedgerBean.setCGN(gridData[i][7]);
                generalLedgerBean.setCreditdate(gridData[i][10]);
                generalLedgerBean.setCreditvouchernumber(gridData[i][11]);
                generalLedgerBean.setDebitVoucherTypeName(gridData[i][12]);
                generalLedgerBean.setCreditVoucherTypeName(gridData[i][13]);
                generalLedgerBean.setVhId(gridData[i][14]);
                reportBean.setStartDate(startDateformat1);
                reportBean.setTotalCount(Integer.toString(totalCount));
                reportBean.setIsConfirmedCount(Integer.toString(isConfirmedCount));
                dataList.add(generalLedgerBean);
            }

        } catch (final Exception ex) {
            LOGGER.error("ERROR in getGeneralLedgerList " + ex.toString(), ex);
            throw taskExc;
        }
        return dataList;
    }

    @SuppressWarnings("unchecked")
    private String getQuery(final String glCode1, final String startDate, final String endDate,
            final String accEntityId, final String accEntityKey, final String fieldId, final String functionId)
                    throws TaskFailedException {
        String addTableToQuery = "";
        String entityCondition = "";
        String functionCondition = "";

        if (!accEntityId.equalsIgnoreCase("") && !accEntityKey.equalsIgnoreCase(""))
            entityCondition = " AND gl.id=gldet.generalledgerid  AND gldet.detailtypeid=" + accEntityId
            + " AND cdet.detailtypeid = " + accEntityId + " AND gldet.detailkeyid=" + accEntityKey + "";
        if (addTableToQuery.trim().equals("") && null != fieldId && !fieldId.trim().equals(""))
            addTableToQuery = ", vouchermis vmis ";
        if (!StringUtils.isEmpty(functionId))
            functionCondition = " and gl.functionid=" + functionId;
        if (!accEntityKey.equals(""))
            return "SELECT  gl.glcode as \"code\",(select ca.type from chartofaccounts ca where glcode=gl.glcode) as \"glType\" ,"
            + " vh.id AS \"vhid\",vh.voucherDate AS \"vDate\",TO_CHAR(vh.voucherDate ,'dd-Mon-yyyy') "
            + " AS \"voucherdate\",vh.voucherNumber AS \"vouchernumber\",gl.glCode AS \"glcode\",coa.name||"
            + " (CASE WHEN (GLDET.GENERALLEDGERID=GL.ID) THEN '-['||(CASE WHEN gldet.detailtypeid = (select id from accountdetailtype where name='Creditor') "
            + "THEN (select name from Supplier where id=gldet.detailkeyid ) "
            + "ELSE (CASE WHEN gldet.detailtypeid = (select id from accountdetailtype where name='EMPLOYEE') "
            + "THEN (select name from eg_user where id=gldet.detailkeyid) "
            + "ELSE (select name from accountentitymaster where id=gldet.detailkeyid) END) END)||']'"
            + " ELSE NULL END) as \"Name\",CASE WHEN gl.glcode = '"
            + glCode1
            + "' THEN (CASE WHEN gl.DEBITAMOUNT = 0 THEN (gldet.amount||'.00cr') ELSE (gldet.amount||'.00dr') END)"
            + "ELSE (CASE WHEN gl.DEBITAMOUNT = 0 THEN (gl.creditamount||'.00cr') ELSE (gl.debitamount||'.00dr') END) END"
            + " as \"amount\",gl.description as \"narration\",vh .type || '-' || vh.name||CASE WHEN status = 1 THEN '(Reversed)' ELSE (CASE WHEN status = 2 THEN '(Reversal)' ELSE '' END) END"
            + " AS \"type\","
            + " CASE WHEN gl.glcode = '"
            + glCode1
            + "' THEN (CASE WHEN gl.debitAMOUNT = 0 THEN 0 ELSE gldet.amount END) ELSE (CASE WHEN gl.debitAMOUNT = 0 THEN 0 ELSE gl.debitamount END) END"
            + " as \"debitamount\",CASE WHEN gl.glcode = '"
            + glCode1
            + "' THEN (CASE WHEN gl.creditAMOUNT = 0 THEN 0 ELSE gldet.amount END) ELSE (CASE WHEN gl.debitAMOUNT = 0 THEN 0 ELSE gl.creditamount END) END"
            + " as \"creditamount\","
            + " f.name as \"fundName\",vh.isconfirmed as \"isconfirmed\",case when (gldet.generalledgerid=gl.id) "
            + " then gldet.detailkeyid else null end as \"DetailKeyId\",vh.type||'-'||vh.name as \"vouchertypename\" "
            + " FROM generalLedger gl, voucherHeader vh, chartOfAccounts coa,"
            + " generalledgerdetail gldet, chartofaccountdetail cdet ,"
            + " fund f WHERE coa.glCode = gl.glCode AND gl.voucherHeaderId = vh.id "
            + " and cdet.glcodeid=coa.id "
            + " and gl.glcode='"
            + glCode1
            + "'"
            + " AND f.id= vh.fundId "
            + entityCondition
            + ""
            + " and vh.id in ("
            + engineQry
            + " )"
            + " AND (gl .debitamount>0 OR gl .creditamount>0) " + " order by vh.id asc ,gl.glCode";
        else {

            final StringBuffer sql = new StringBuffer(
                    "SELECT  gl.glcode as \"code\",(select ca.type from chartofaccounts ca where glcode=gl.glcode) as \"glType\",vh.id AS \"vhid\", vh.voucherDate AS \"vDate\", ")
            .append(" TO_CHAR(vh.voucherDate, 'dd-Mon-yyyy') AS voucherdate, ")
            .append(" vh.voucherNumber AS \"vouchernumber\", gl.glCode AS \"glcode\", ")
            .append(" coa.name AS \"name\",CASE WHEN gl.debitAmount = 0 THEN (case (gl.creditamount) when 0 then gl.creditAmount||'.00cr' when floor(gl.creditamount) then gl.creditAmount||'.00cr' else  gl.creditAmount||'cr'  end ) ELSE (case (gl.debitamount) when 0 then gl.debitamount||'.00dr' when floor(gl.debitamount) then gl.debitamount||'.00dr' else  gl.debitamount||'dr' end ) END"
                    + " AS \"amount\", ")
                    .append(" gl.description AS \"narration\", vh.type || '-' || vh.name||CASE WHEN status = 1 THEN '(Reversed)' ELSE (CASE WHEN status = 2 THEN '(Reversal)' ELSE '' END) END AS \"type\", ")
                    .append(" gl.debitamount  AS \"debitamount\", gl.creditamount  AS \"creditamount\",f.name as \"fundName\",  vh.isconfirmed as \"isconfirmed\",gl.functionid as \"functionid\",vh.type||'-'||vh.name as \"vouchertypename\" ")
                    .append(" FROM generalLedger gl, voucherHeader vh, chartOfAccounts coa,  fund f " + addTableToQuery
                            + "").append(" WHERE coa.glCode = gl.glCode AND gl.voucherHeaderId = vh.id  ")
                            .append(" AND	f.id=vh.fundid ").append(" AND gl.glcode ='" + glCode1 + "'")
                            .append(" AND (gl.debitamount>0 OR gl.creditamount>0) ").append(functionCondition)
                            .append(" and vh.id in (" + engineQry + " )")
                            .append(" group by vh.id,gl.glcode,vh.voucherDate ,vh.voucherNumber,coa.name,gl.description,  vh.type || '-' || vh.name||CASE WHEN status = 1 THEN '(Reversed)' ELSE (CASE WHEN status = 2 THEN '(Reversal)' ELSE '' END) END, gl.debitamount , gl.creditamount  ,f.name, vh.isconfirmed, vh.type  ||'-'  ||vh.name, gl.functionid   ")
                            .append(" order by \"code\",\"vDate\" ");
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("____________________________________________________________" + sql.toString());
            return sql.toString();
        }
    }

    private OpBal getOpeningBalance(final String glCode, final String fundId, final String fundSourceId,
            final String fyId, final String accEntityId, final String accEntityKey, final String tillDate,
            final String functionId, final String deptId) throws TaskFailedException {
        String fundCondition = "";
        String fundSourceCondition = "";
        String accEntityCondition = "";
        String functionCondition = "";
        String deptCondition = "";
        String deptFromCondition = "";
        String deptWhereCondition = "";

        double opDebit = 0, opCredit = 0;

        /** opening balance of the Year **/
        if (!fundId.equalsIgnoreCase(""))
            fundCondition = "fundId = ? AND ";
        if (deptId != null && !deptId.equalsIgnoreCase("")) {
            deptCondition = "DEPARTMENTID = ? AND ";
            deptFromCondition = ", vouchermis mis";
            deptWhereCondition = " mis.voucherheaderid =vh.id   and mis.DepartmentId = ? and ";
        }
        if (!fundSourceId.equalsIgnoreCase(""))
            fundSourceCondition = "fundSourceId = ? AND ";
        if (!accEntityId.equalsIgnoreCase(""))
            accEntityCondition = "accountDetailTypeid=? AND accountDetailKey=? AND ";
        if (!StringUtils.isEmpty(functionId))
            functionCondition = " functionid=? AND ";
        final String queryYearOpBal = "SELECT CASE WHEN sum(openingDebitBalance) is null THEN 0 ELSE sum(openingDebitBalance) END AS \"openingDebitBalance\", "
                + "CASE WHEN sum(openingCreditBalance) is null THEN 0 ELSE sum(openingCreditBalance) END AS \"openingCreditBalance\" "
                + "FROM transactionSummary WHERE "
                + fundCondition
                + fundSourceCondition
                + functionCondition
                + accEntityCondition
                + deptCondition
                + " financialYearId=? "
                + "AND glCodeId = (SELECT id FROM chartOfAccounts WHERE glCode in(?))";
        if (LOGGER.isInfoEnabled())
            LOGGER.info("**********************: OPBAL: " + queryYearOpBal);
        try {
            int i = 0;
            pstmt = persistenceService.getSession().createSQLQuery(queryYearOpBal);
            if (!fundId.equalsIgnoreCase(""))
                pstmt.setLong(i++, Long.valueOf(fundId));
            if (!fundSourceId.equalsIgnoreCase(""))
                pstmt.setLong(i++, Long.valueOf(fundSourceId));
            if (!StringUtils.isEmpty(functionId))
                pstmt.setLong(i++, Long.valueOf(functionId));
            if (!accEntityId.equalsIgnoreCase("")) {
                pstmt.setLong(i++, Long.valueOf(accEntityId));
                pstmt.setLong(i++, Long.valueOf(accEntityKey));
            }
            if (deptId != null && !deptId.equalsIgnoreCase(""))
                pstmt.setLong(i++, Long.valueOf(deptId));
            pstmt.setLong(i++, Long.valueOf(fyId));
            pstmt.setString(i++, glCode);
            resultset = null;
            final List list = pstmt.list();
            resultset = list;
            for (final Object[] element : resultset) {
                opDebit = Double.parseDouble(element[0] != null ? element[0].toString() : "0");
                opCredit = Double.parseDouble(element[1] != null ? element[1].toString() : "0");
            }

        } catch (final Exception ex) {
            LOGGER.error("Error GeneralLedger->getOpeningBalance() For the year: " + ex.toString(), ex);
            throw taskExc;
        }

        /** opening balance till the date from the start of the Year **/
        final String startDate = commnFunctions.getStartDate(Integer.parseInt(fyId));
        if (!fundId.equalsIgnoreCase(""))
            fundCondition = "AND vh.fundId = ? ";
        if (!fundSourceId.equalsIgnoreCase(""))
            fundSourceCondition = "AND vh.fundId = ? ";
        if (!StringUtils.isEmpty(functionId))
            functionCondition = " and gl.functionid=? ";
        String queryTillDateOpBal = "";
        String defaultStatusExclude = null;
        final List<AppConfigValues> listAppConfVal = appConfigValuesService.getConfigValuesByModuleAndKey("EGF",
                "statusexcludeReport");
        if (null != listAppConfVal)
            defaultStatusExclude = listAppConfVal.get(0).getValue();
        else
            throw new ApplicationRuntimeException("Exlcude statusses not  are not defined for Reports");

        if (!accEntityId.equalsIgnoreCase("") && !accEntityKey.equalsIgnoreCase("")) {
            // addGldtlTableToQuery=", generalledgerdetail gldet ";
            accEntityCondition = " AND gl.id=gldet.generalledgerid  AND gldet.detailtypeid=? AND gldet.detailkeyid=? ";

            queryTillDateOpBal = "SELECT coa.glcode,(SELECT SUM(gldet.amount) FROM generalLedger gl, voucherHeader vh "
                    + deptFromCondition
                    + " , generalledgerdetail gldet "
                    + " WHERE vh.id = gl.voucherHeaderId AND gl.glcodeid IN (coa.id) "
                    + fundCondition
                    + fundSourceCondition
                    + functionCondition
                    + accEntityCondition
                    + " AND "
                    + deptWhereCondition
                    + " vh.voucherDate >= to_date(?,'dd/mm/yyyy')  AND vh.voucherDate < to_date(?,'dd/mm/yyyy') AND vh.status not in ("
                    + defaultStatusExclude
                    + ")"
                    + " AND "
                    + " gl.DEBITamount>0) AS \"debitAmount\",(SELECT SUM(gldet.amount) FROM generalLedger gl, voucherHeader vh "
                    + deptFromCondition
                    + " , "
                    + " generalledgerdetail gldet WHERE vh.id = gl.voucherHeaderId AND "
                    + deptWhereCondition
                    + " "
                    + " gl.glcodeid IN (coa.id) "
                    + fundCondition
                    + fundSourceCondition
                    + functionCondition
                    + accEntityCondition
                    + " AND vh.voucherDate >= to_date(?,'dd/mm/yyyy') AND vh.voucherDate <to_date(?,'dd/mm/yyyy') AND vh.status not in ("
                    + defaultStatusExclude + ") AND"
                    + " gl.CREDITamount>0) AS \"creditAmount\" FROM chartofaccounts coa WHERE 	coa.glcode IN (?)";

        } else
            queryTillDateOpBal = "SELECT CASE WHEN sum(gl.debitAmount) is null THEN 0 ELSE sum(gl.debitAmount) END AS \"debitAmount\", "
                    + " CASE WHEN sum(gl.creditAmount) is null THEN 0 ELSE sum(gl.creditAmount) END AS \"creditAmount\" "
                    + " FROM generalLedger gl, voucherHeader vh "
                    + deptFromCondition
                    + " WHERE vh.id = gl.voucherHeaderId AND "
                    + deptWhereCondition
                    + " gl.glCode IN (?)"
                    + " "
                    + fundCondition
                    + fundSourceCondition
                    + functionCondition
                    + " AND vh.voucherDate >= to_date(?,'dd/MM/YYYY') AND vh.voucherDate <to_date(?,'dd/MM/YYYY') AND vh.status not in ("
                    + defaultStatusExclude + ")";
        if (LOGGER.isInfoEnabled())
            LOGGER.info("***********: OPBAL: " + queryTillDateOpBal);
        try {
            pstmt = persistenceService.getSession().createSQLQuery(queryTillDateOpBal);
            int i = 0;
            if (!accEntityId.equalsIgnoreCase("") && !accEntityKey.equalsIgnoreCase("")) {
                if (!fundId.equalsIgnoreCase(""))
                    pstmt.setLong(i++, Long.parseLong(fundId));
                if (!fundSourceId.equalsIgnoreCase(""))
                    pstmt.setLong(i++, Long.parseLong(fundSourceId));
                if (!StringUtils.isEmpty(functionId))
                    pstmt.setLong(i++, Long.parseLong(functionId));
                if (!accEntityId.equalsIgnoreCase("")) {
                    pstmt.setLong(i++, Long.parseLong(accEntityId));
                    pstmt.setLong(i++, Long.parseLong(accEntityKey));
                }
                if (deptId != null && !deptId.equalsIgnoreCase(""))
                    pstmt.setLong(i++, Long.parseLong(deptId));
                pstmt.setString(i++, startDate);
                pstmt.setString(i++, tillDate);
                if (deptId != null && !deptId.equalsIgnoreCase(""))
                    pstmt.setLong(i++, Long.parseLong(deptId));
                if (!fundId.equalsIgnoreCase(""))
                    pstmt.setLong(i++, Long.parseLong(fundId));
                if (!fundSourceId.equalsIgnoreCase(""))
                    pstmt.setLong(i++, Long.parseLong(fundSourceId));
                if (!StringUtils.isEmpty(functionId))
                    pstmt.setLong(i++, Long.parseLong(functionId));
                if (!accEntityId.equalsIgnoreCase("")) {
                    pstmt.setLong(i++, Long.parseLong(accEntityId));
                    pstmt.setLong(i++, Long.parseLong(accEntityKey));
                }
                pstmt.setString(i++, startDate);
                pstmt.setString(i++, tillDate);
                pstmt.setString(i++, glCode);
            } else {

                if (deptId != null && !deptId.equalsIgnoreCase(""))
                    pstmt.setLong(i++, Long.parseLong(deptId));
                pstmt.setString(i++, glCode);
                if (!fundId.equalsIgnoreCase(""))
                    pstmt.setLong(i++, Long.parseLong(fundId));
                if (!fundSourceId.equalsIgnoreCase(""))
                    pstmt.setLong(i++, Long.parseLong(fundSourceId));
                if (!StringUtils.isEmpty(functionId))
                    pstmt.setLong(i++, Long.parseLong(functionId));
                pstmt.setString(i++, startDate);
                pstmt.setString(i++, tillDate);
            }
            resultset = null;
            final List<Object[]> list = pstmt.list();
            resultset = list;
            if (!accEntityId.equalsIgnoreCase("") && !accEntityKey.equalsIgnoreCase(""))
                for (final Object[] element : resultset) {
                    if (element[1] != null)
                        opDebit = opDebit + Double.parseDouble(element[1].toString());
                    if (element[2] != null)
                        opCredit = opCredit + Double.parseDouble(element[2].toString());
                }
            else
                for (final Object[] element : resultset) {
                    if (element[0] != null)
                        opDebit = opDebit + Double.parseDouble(element[0].toString());
                    if (element[1] != null)
                        opCredit = opCredit + Double.parseDouble(element[1].toString());
                }
        } catch (final Exception ex) {
            LOGGER.error("Error GeneralLedger->getOpeningBalance() till the date: " + ex.toString(), ex);
            throw taskExc;
        }
        final OpBal opBal = new OpBal();
        opBal.dr = opDebit;
        opBal.cr = opCredit;
        resultset = null;
        return opBal;
    }

    private String getAccountName(final String glCode) throws TaskFailedException {
        String accountName = "";
        Query pst = null;
        try {
            final String query = "select name as \"name\" from  CHARTOFACCOUNTS where GLCODE=?";
            pst = persistenceService.getSession().createSQLQuery(query);
            pst.setString(0, glCode);
            final List list = pst.list();
            final Object[] objects = list.toArray();
            accountName = objects[0].toString();

        } catch (final Exception e) {
            LOGGER.error("Exp in getAccountName:" + e.getMessage(), e);
            throw taskExc;
        }

        return accountName;
    }

    private String getFundName(final String fundId) throws TaskFailedException {
        String fundName = "";
        Query pst = null;
        try {
            final String query = "select name  as \"name\" from fund where id=?";
            pst = persistenceService.getSession().createSQLQuery(query);
            if (fundId.isEmpty())
                pst.setInteger(0, 0);
            else
                pst.setInteger(0, Integer.valueOf(fundId));
            final List<Object[]> list = pst.list();
            final Object[] objects = list.toArray();
            if (objects.length == 0)
                fundName = "";
            else
                fundName = objects[0].toString();

        } catch (final Exception e) {
            LOGGER.error("Exp in getFundName" + e.getMessage(), e);
            throw taskExc;
        }
        return fundName;
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

    public void isCurDate(final String VDate) throws TaskFailedException {

        try {
            final String today = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            final String[] dt2 = today.split("/");
            final String[] dt1 = VDate.split("/");

            final int ret = Integer.parseInt(dt2[2]) > Integer.parseInt(dt1[2]) ? 1
                    : Integer.parseInt(dt2[2]) < Integer.parseInt(dt1[2]) ? -1 : Integer.parseInt(dt2[1]) > Integer
                            .parseInt(dt1[1]) ? 1 : Integer.parseInt(dt2[1]) < Integer.parseInt(dt1[1]) ? -1 : Integer
                                    .parseInt(dt2[0]) > Integer.parseInt(dt1[0]) ? 1 : Integer.parseInt(dt2[0]) < Integer
                                            .parseInt(dt1[0]) ? -1 : 0;
                                    if (ret == -1)
                                        throw taskExc;

        } catch (final Exception ex) {
            LOGGER.error("Exception in isCurDate():" + ex, ex);
            throw new TaskFailedException("Date Should be within the today's date");
        }

    }

}