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
 * Created on Jan 19, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.reports;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.exilant.exility.common.TaskFailedException;

/**
 * @author Lakshmi
 * <p>
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
@Service
public class CommnFunctions {
    private static final Logger LOGGER = Logger.getLogger(CommnFunctions.class);
    private static TaskFailedException taskExc;
    /**/
    public String reqFundId[];
    public String reqFundName[];
    Query pstmt = null;
    private List<Object[]> resultset;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method extracts all the fund ids and fund names.
     *
     * @param fundId :The id of the fund for which the opening balances has to be calculated
     * @param startDate :The start date of the financial year for which opening balance has to be calculated
     * @param endDate :The end date of the financial year for which opening balance has to be calculated.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void getFundList(final String fundId, final String startDate, final String endDate) throws Exception {
        String fundCondition = "";
        if (!fundId.equalsIgnoreCase(""))
            fundCondition = " AND Id = :fundId ";
        try {
            final StringBuilder query = new StringBuilder("select id, name")
                    .append(" from fund")
                    .append(" where isactive = true and isnotleaf != true ")
                    .append(fundCondition)
                    .append(" order by id");
            if (LOGGER.isInfoEnabled())
                LOGGER.info("getFundList: " + query);
            pstmt = entityManager.createNativeQuery(query.toString());
            if (!fundId.equalsIgnoreCase(""))
                pstmt.setParameter("fundId", fundId);
            resultset = pstmt.getResultList();
            int resSize = 0, i = 0;
            resSize = resultset.size();
            reqFundId = new String[resSize];
            reqFundName = new String[resSize];
            if (LOGGER.isInfoEnabled())
                LOGGER.info("resSize  " + resSize);
            for (final Object[] element : resultset) {
                reqFundId[i] = element[0].toString();
                reqFundName[i] = element[1].toString();
                i += 1;
            }
        } catch (final Exception ex) {
            LOGGER.error("ERROR in FundList coomonfun", ex);
            LOGGER.error("Error in getting fund list");
            throw taskExc;
        }
    }

    /**
     * Get the opening balance for the list of account codes for a particular fund if fund is passed as a parameter for a
     * particular financial year. Else get the opening balance of all the account codes irrespective of fund for a particular
     * financial year
     *
     * @param fundId :The id of the fund for which the opening balances has to be calculated
     * @param type1 :The type of account code (A,I)
     * @param type2 :The type of account code (L,E)
     * @param substringVal :The no of digits to pick from the glcode for grouping. (2 for major code, 3 for minor code)
     * @param startDate :The start date of the financial year for which opening balance has to be calculated
     * @param endDate :The end date of the financial year for which opening balance has to be calculated.
     * @param classification:Classification of the code for which opening balance has to be calculated
     * @param reqFundId1 :List of fundIds
     * @param openingBal :This parameter is called by reference so it acts as input & output
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void getOpeningBalance(final String fundId, final String type1, final String type2, final String substringVal,
            final String startDate,
            final String endDate, final int classification, final String reqFundId1[],
            final Map<String, Map<String, Double>> openingBal)
            throws Exception {
        String fundCondition = "";
        if (!fundId.equalsIgnoreCase(""))
            fundCondition = " AND f.Id = :fundId";
        String glcode = "", fuId = "";
        final StringBuilder query = new StringBuilder("SELECT substr(coa.glcode, 0, ").append(substringVal)
                .append(" ) as \"glcode\", ts.fundid as \"fundid\", case when coa.type = :type2 then sum(ts.openingcreditbalance)")
                .append(" - sum(ts.openingdebitbalance)")
                .append(" else sum(ts.openingdebitbalance)-sum(ts.openingcreditbalance) end as \"amount\"")
                .append(" FROM transactionsummary ts,  chartofaccounts coa,fund  f")
                .append(" WHERE (coa.TYPE = :type1 OR coa.TYPE = :type2) and coa.id = ts.glcodeid ")
                .append(" AND financialyearid = (SELECT ID FROM financialyear WHERE startingdate <= :startDate AND endingdate >= :endDate)")
                .append(fundCondition).append(" and f.id = ts.fundid and f.isactive = true and f.isnotleaf != true")
                .append(" GROUP BY substr(coa.glcode, 0, ").append(substringVal)
                .append("), fundid, coa.type ");
        if (LOGGER.isInfoEnabled())
            LOGGER.info("query " + query);
        try {
            getFundList(fundId, startDate, endDate);
            pstmt = entityManager.createNativeQuery(query.toString())
                    .setParameter("type2", type2)
                    .setParameter("type1", type1)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate);
            if (!fundId.equalsIgnoreCase(""))
                pstmt.setParameter("fundId", fundId);
            resultset = pstmt.getResultList();
            Double opeBal = null;
            HashMap<String, Double> openingBalsubList = null;
            for (final Object[] element : resultset) {
                glcode = element[0].toString();
                fuId = element[1].toString();
                opeBal = Double.parseDouble(element[2].toString());
                if (!openingBal.containsKey(glcode)) {
                    openingBalsubList = new HashMap<>();
                    for (final String element2 : reqFundId1)
                        if (element2.equalsIgnoreCase(fuId))
                            openingBalsubList.put(element2, opeBal);
                        else
                            openingBalsubList.put(element2, new Double(0));
                    openingBal.put(glcode, openingBalsubList);
                } else
                    openingBal.get(glcode).put(fuId, opeBal);

            }
        } catch (final Exception e) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Error in getOpeningBalance", e);
            throw taskExc;
        }
    }

    /**
     * Get the sum of debit amount minus the sum of credit amounts for the list of account codes for a particular fund from the GL
     * if fund is passed as a parameter for a particular financial year. Else get the sum of debit amount minus the sum of credit
     * amounts of all the account codes irrespective of fund for a particular financial year
     *
     * @param fundid :The id of the fund for which the opening balances has to be calculated
     * @param type1 :The type of account code (A,E,L,I)
     * @param type2 :The type of account code (A,E,L,I)
     * @param substringVal :The no of digits to pick from the glcode for grouping. (2 for major code, 3 for minor code)
     * @param startDate :The start date of the financial year for which opening balance has to be calculated
     * @param endDate :The end date of the financial year for which opening balance has to be calculated
     * @param classification :Classification of the code for which opening balance has to be calculated
     * @param effFilter :The Effective Date Filter
     * @param txnBalancehasmap:Since HashMap is called by Reference same list will get effected in called method
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void getTxnBalance(final Connection conn, final String fundid, final String type1, final String type2,
            final String substringVal, final String startDate,
            final String endDate, final int classification, final String effFilter,
            final Map<String, Map<String, Object>> txnBalancehasmap)
            throws Exception {
        String fundCondition = " ";
        if (fundid != null && !fundid.equals(""))
            fundCondition = " and vh.fundid = :fundId";

        final StringBuilder query1 = new StringBuilder(
                "SELECT SUBSTR(coa.GLCODE,1,2) as \"glCode\", vh.fundid as \"fundId\", case when sum(gl.debitamount)")
                        .append(" - sum(gl.creditAmount) = null")
                        .append(" then 0 else sum(gl.debitamount) - sum(gl.creditAmount) as \"amount\"")
                        .append(" FROM chartofaccounts coa, generalledger gl, voucherHeader vh")
                        .append(" WHERE coa.TYPE = :type1 and vh.ID = gl.VOUCHERHEADERID AND gl.glcode=coa.glcode")
                        .append(" AND vh.VOUCHERDATE >= :startDate AND vh.VOUCHERDATE <= :endDate")
                        .append(fundCondition).append("  ").append(effFilter);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getI: " + query1);
        try {
            pstmt = entityManager.createNativeQuery(query1.toString())
                    .setParameter("type1", type1)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate);
            if (fundid != null && !fundid.equals(""))
                pstmt.setParameter("fundId", fundid);
            resultset = pstmt.getResultList();
            final Object[] firstElement = resultset != null && resultset.size() > 0 ? resultset.get(1) : null;
            for (final Object[] element : resultset) {
                final String accntCode = element[0].toString();
                final String fund = element[1].toString();
                final String amt = element[2].toString();
                final Map<String, Object> txnBalance = new HashMap<>();
                /** Storing fund and amount pairs of same GLCode into HashMap **/
                if (!txnBalancehasmap.containsKey(accntCode))// accntCode2.equalsIgnoreCase(accntCode))//Loops until GlCode
                // changes
                {
                    for (final String element2 : reqFundId)
                        if (element2.equalsIgnoreCase(fund))
                            txnBalance.put(fund, amt);// openingBalsubList.put(reqFundId1[i],opeBal);
                        else
                            txnBalance.put(element2, new Double(0));

                    txnBalancehasmap.put(accntCode, txnBalance);
                } else
                    txnBalancehasmap.get(accntCode).put(fund, amt);
                if (firstElement.equals(element))
                    break;
                /** Storing GLCode and (fund-amount paired HashMap) pairs into HashMap **/
                txnBalancehasmap.put(accntCode, txnBalance);
            }

        } catch (final Exception se) {
            LOGGER.error("Error in getschedulewiseOB", se);
            throw taskExc;
        }
    }

    /**
     * @param type1 :The type of account code (A,E,L,I)
     * @param type2 :The type of account code (A,E,L,I)
     * @param substringVal :The no of digits to pick from the glcode for grouping. (2 for major code, 3 for minor code)
     * @param startDate :The start date of the financial year for which opening balance has to be calculated
     * @param endDate :The end date of the financial year for which opening balance has to be calculated
     * @param classification :Classification of the code for which opening balance has to be calculated
     * @param txnCreditBalance :This parameter is called by reference so it acts as input & output
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void getTxnCreditBalance(final Connection conn, final String fundId, final String type1, final String type2,
            final String substringVal,
            final String startDate, final String endDate, final int classification,
            final Map<String, Map<String, Double>> txnCreditBalance)
            throws Exception {
        String fundCondition = "";
        if (!fundId.equalsIgnoreCase(""))
            fundCondition = " AND f.Id = :fundId ";
        String glcode = "", fuId = "";
        String type = " (coa.TYPE = :type1 OR coa.TYPE = :type2) and";
        if (type1 == null || type1.trim().equals(""))
            type = "";
        final StringBuilder query = new StringBuilder("SELECT substr(coa.glcode,0,").append(substringVal)
                .append(") as \"glcode\", ts.fundid as \"fundid\" , sum(ts.openingcreditbalance) as \"amount\"")
                .append(" FROM transactionsummary ts, chartofaccounts coa, fund f")
                .append(" WHERE ").append(type)
                .append(" coa.id = ts.glcodeid AND financialyearid = (SELECT ID FROM financialyear")
                .append(" WHERE startingdate <= :startDate AND endingdate >= :endDate)")
                .append(fundCondition).append(" and f.id = ts.fundid and f.isactive = true and f.isnotleaf != true ")
                .append(" GROUP BY substr(coa.glcode, 0, ").append(substringVal).append("), fundid, coa.type");
        if (LOGGER.isInfoEnabled())
            LOGGER.info("query " + query);
        try {
            pstmt = entityManager.createNativeQuery(query.toString());
            if (type1 == null || type1.trim().equals(""))
                pstmt.setParameter("type1", type1)
                        .setParameter("type2", type2);
            pstmt.setParameter("startDate", startDate)
                    .setParameter("endDate", endDate);
            if (!fundId.equalsIgnoreCase(""))
                pstmt.setParameter("fundId", fundId);
            resultset = pstmt.getResultList();
            Double opeBal = null;
            Map<String, Double> creditBalsubList = null;
            for (final Object[] element : resultset) {
                glcode = element[0].toString();
                fuId = element[1].toString();
                opeBal = Double.parseDouble(element[2].toString());
                if (!txnCreditBalance.containsKey(glcode)) {
                    creditBalsubList = new HashMap<>();
                    for (final String element2 : reqFundId)
                        if (element2.equalsIgnoreCase(fuId))
                            creditBalsubList.put(element2, opeBal);
                        else
                            creditBalsubList.put(element2, new Double(0));
                    txnCreditBalance.put(glcode, creditBalsubList);
                } else
                    txnCreditBalance.get(glcode).put(fuId, opeBal);
            }
        } catch (final Exception e) {
            LOGGER.error("Error in getCreditBalance");
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Exp = ", e);
            throw new Exception();
        }
    }

    /**
     * @param fundId :The id of the fund for which the opening balances has to be calculated
     * @param type1 :The type of account code (A,E,L,I)
     * @param type2 :The type of account code (A,E,L,I)
     * @param substringVal :The no of digits to pick from the glcode for grouping. (2 for major code, 3 for minor code)
     * @param startDate :The start date of the financial year for which opening balance has to be calculated
     * @param endDate :The end date of the financial year for which opening balance has to be calculated
     * @param classification :classification of the code for which opening balance has to be calculated
     * @param txnDebitBalance:This parameter is called by reference so it acts as input & output
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void getTxnDebitBalance(final Connection conn, final String fundId, final String type1, final String type2,
            final String substringVal,
            final String startDate, final String endDate, final int classification,
            final Map<String, Map<String, Double>> txnDebitBalance)
            throws Exception {
        String fundCondition = "";
        if (!fundId.equalsIgnoreCase(""))
            fundCondition = " AND f.Id = :fundId ";
        String glcode = "", fuId = "";
        final StringBuilder query = new StringBuilder("SELECT substr(coa.glcode, 0, ").append(substringVal)
                .append(" ) as \"glcode\", ts.fundid as \"fundid\", sum(ts.openingdebitbalance) as \"amount\"")
                .append(" FROM transactionsummary ts, chartofaccounts coa, fund f")
                .append(" WHERE (coa.TYPE = :type1 OR coa.TYPE = :type2) and coa.id = ts.glcodeid")
                .append(" AND financialyearid = (SELECT ID FROM financialyear WHERE startingdate <= :startDate AND endingdate >= :endDate) ")
                .append(fundCondition).append(" and f.id = ts.fundid and f.isactive = true and f.isnotleaf != true")
                .append(" GROUP BY substr(coa.glcode, 0, ").append(substringVal).append("), fundid, coa.type");
        if (LOGGER.isInfoEnabled())
            LOGGER.info("query " + query);
        try {
            pstmt = entityManager.createNativeQuery(query.toString())
                    .setParameter("type1", type1)
                    .setParameter("type2", type2)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate);
            if (!fundId.equalsIgnoreCase(""))
                pstmt.setParameter("fundId", fundId);
            resultset = pstmt.getResultList();
            Double opeBal = null;
            Map<String, Double> debitBalsubList = null;
            for (final Object[] element : resultset) {
                glcode = element[0].toString();
                fuId = element[1].toString();
                opeBal = Double.parseDouble(element[2].toString());
                if (!txnDebitBalance.containsKey(glcode)) {
                    debitBalsubList = new HashMap<>();
                    for (final String element2 : reqFundId)
                        if (element2.equalsIgnoreCase(fuId))
                            debitBalsubList.put(element2, opeBal);
                        else
                            debitBalsubList.put(element2, new Double(0));
                    txnDebitBalance.put(glcode, debitBalsubList);
                } else
                    txnDebitBalance.get(glcode).put(fuId, opeBal);
            }
        } catch (final Exception e) {
            LOGGER.error("Error in getDebitBalance", e);
            throw taskExc;
        }
    }

    /**
     * convert amount in rupeese to thousands or lakhs
     *
     * @param amt :amount to be converted
     * @param amt_In : to "thousands" or "lakhs"
     * @return
     */

    public String formatAmt(final String amt, final String amt_In) {
        BigDecimal ammt = new BigDecimal(0.000);
        NumberFormat formatter;
        formatter = new DecimalFormat("##############0.00");
        final int val = amt_In.equalsIgnoreCase("thousand") ? 1 : amt_In.equalsIgnoreCase("lakhs") ? 2 : 3;
        switch (val) {
        case 1:
            ammt = BigDecimal.valueOf(Double.parseDouble(amt) / 1000);
            ammt = ammt.setScale(2, BigDecimal.ROUND_HALF_UP);
            break;

        case 2:
            ammt = BigDecimal.valueOf(Double.parseDouble(amt) / 100000);
            ammt = ammt.setScale(2, BigDecimal.ROUND_HALF_UP);
            break;
        default:
            ammt = BigDecimal.valueOf(Double.valueOf(amt));
            BigDecimal tmpAmt = new BigDecimal(ammt.toBigInteger());
            tmpAmt = tmpAmt.add(BigDecimal.valueOf(0.5));
            if (ammt.doubleValue() > tmpAmt.doubleValue())
                ammt = ammt.setScale(0, BigDecimal.ROUND_HALF_UP);

        }

        return formatter.format(Double.valueOf(ammt.toString()));
    }

    @SuppressWarnings("unchecked")
    public String getStartDate(final int finYearId) throws TaskFailedException {
        String startDate = "";
        final StringBuilder query = new StringBuilder("SELECT TO_CHAR(startingdate,'DD/MM/YYYY')")
                .append(" FROM FINANCIALYEAR")
                .append(" WHERE id = :finYearId");
        try {
            List<Object> list = entityManager.createNativeQuery(query.toString())
                    .setParameter("finYearId", finYearId)
                    .getResultList();
            if (list != null)
                startDate = list.get(0).toString();

        } catch (final Exception sql) {
            LOGGER.error("Exp in getStartDate :", sql);
            throw taskExc;
        }
        return startDate;
    }

    /**
     * function to get financial year end Date
     *
     * @param finYearId
     * @return
     */
    @SuppressWarnings("unchecked")
    public String getEndDate(final int finYearId) throws TaskFailedException {
        String endDate = "";
        final StringBuilder query = new StringBuilder("SELECT TO_CHAR(endingdate,'DD/MM/YYYY')")
                .append(" FROM FINANCIALYEAR")
                .append(" WHERE id = :finYearId");
        try {
            resultset = entityManager.createNativeQuery(query.toString())
                    .setParameter("finYearId", finYearId)
                    .getResultList();
            for (final Object[] element : resultset)
                endDate = element[0].toString();
        } catch (final Exception sql) {
            LOGGER.error("error inside getEndDate", sql);
            throw taskExc;
        }
        return endDate;
    }

    /**
     * function to format amount into to Indaian rupees format
     *
     * @param strNumberToConvert
     * @return
     */
    public StringBuffer numberToString(final String strNumberToConvert) {
        String strNumber = "", signBit = "";
        if (strNumberToConvert.startsWith("-")) {
            strNumber = strNumberToConvert.substring(1, strNumberToConvert.length());
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

    // used to format the report schedule
    /*
     * tagName is the string[] containing <schedlue>lessGlcodes
     */
    public String addToAddLessSubTotals(final String glcode, final BigDecimal amt, final String[] tagName, final int index,
            final BigDecimal[] addSubtotals,
            final BigDecimal[] lessSubtotals, final String status) {
        String returnStatus = "";

        for (final String element : tagName) {
            if (element.equals(glcode)) {
                if (lessSubtotals[index] == null)
                    lessSubtotals[index] = BigDecimal.ZERO;
                lessSubtotals[index] = lessSubtotals[index].add(amt);
            } else {
                if (addSubtotals[index] == null)
                    addSubtotals[index] = BigDecimal.ZERO;
                addSubtotals[index] = addSubtotals[index].add(amt);
            }
            // find when to insert addSubtotals & lessSubtotals
            if (tagName[0].equals(tagName[tagName.length - 1]) && tagName[0].equals(glcode))
                returnStatus = "addBothSubtotals";
            else if (tagName[0].equals(glcode) && !status.equals("added-addSubtotals"))
                returnStatus = "addSubtotals";
            else if (tagName[tagName.length - 1].equals(glcode))
                returnStatus = "lessSubtotals";
        }
        return returnStatus;
    }

}