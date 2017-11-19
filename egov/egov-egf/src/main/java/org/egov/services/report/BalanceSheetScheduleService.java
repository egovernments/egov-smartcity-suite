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
package org.egov.services.report;


import org.apache.log4j.Logger;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Fund;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.egf.model.Statement;
import org.egov.egf.model.StatementEntry;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BalanceSheetScheduleService extends ScheduleService {
    private static final String BS = "BS";
    private static final String L = "L";
    private BalanceSheetService balanceSheetService;
    private String removeEntrysWithZeroAmount = "";
    private static final Logger LOGGER = Logger.getLogger(BalanceSheetScheduleService.class);

   
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired
    private  FinancialYearHibernateDAO financialYearDAO;
    
   
    public void setBalanceSheetService(final BalanceSheetService balanceSheetService) {
        this.balanceSheetService = balanceSheetService;
    }
    
    public void populateDataForSchedule(final Statement balanceSheet, final String majorCode) {
        getAppConfigValueForRemoveEntrysWithZeroAmount();
        voucherStatusToExclude = getAppConfigValueFor("EGF", "statusexcludeReport");
        minorCodeLength = Integer.valueOf(balanceSheetService.getAppConfigValueFor(Constants.EGF, "coa_minorcode_length"));
        majorCodeLength = Integer.valueOf(balanceSheetService.getAppConfigValueFor(Constants.EGF, "coa_majorcode_length"));
        final Date fromDate = balanceSheetService.getFromDate(balanceSheet);
        final Date toDate = balanceSheetService.getToDate(balanceSheet);
        final CChartOfAccounts coa = (CChartOfAccounts) find("from CChartOfAccounts where glcode=?", majorCode);
        final List<Fund> fundList = balanceSheet.getFunds();
        populateCurrentYearAmountForSchedule(balanceSheet, fundList, balanceSheetService.getFilterQuery(balanceSheet), toDate,
                fromDate, majorCode, coa.getType());
        addCurrentOpeningBalancePerFund(balanceSheet, fundList, balanceSheetService.getTransactionQuery(balanceSheet));
        populatePreviousYearTotalsForSchedule(balanceSheet, balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate,
                majorCode, coa.getType());
        addOpeningBalanceForPreviousYear(balanceSheet, balanceSheetService.getTransactionQuery(balanceSheet), fromDate);
        balanceSheetService.addExcessIEForCurrentYear(balanceSheet, fundList, getGlcodeForPurposeCode7(),
                balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate);
        balanceSheetService.addExcessIEForPreviousYear(balanceSheet, fundList, getGlcodeForPurposeCode7(),
                balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate);
        balanceSheetService.removeFundsWithNoData(balanceSheet);
        balanceSheetService.computeCurrentYearTotals(balanceSheet, Constants.LIABILITIES, Constants.ASSETS);
        computeAndAddTotals(balanceSheet);
        if (removeEntrysWithZeroAmount.equalsIgnoreCase("Yes"))
            balanceSheetService.removeScheduleEntrysWithZeroAmount(balanceSheet);
    }

    public void addCurrentOpeningBalancePerFund(final Statement balanceSheet, final List<Fund> fundList,
            final String transactionQuery) {
        final BigDecimal divisor = balanceSheet.getDivisor();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("addCurrentOpeningBalancePerFund");
        final Query query = persistenceService.getSession()
                .createSQLQuery(
                        "select sum(openingdebitbalance)- sum(openingcreditbalance),ts.fundid,coa.glcode,coa.type FROM transactionsummary ts,chartofaccounts coa  WHERE ts.glcodeid = coa.ID  AND ts.financialyearid="
                                + balanceSheet.getFinancialYear().getId()
                                + transactionQuery
                                + " GROUP BY ts.fundid,coa.glcode,coa.type");
        final List<Object[]> openingBalanceAmountList = query.list();
        for (final Object[] obj : openingBalanceAmountList)
            if (obj[0] != null && obj[1] != null) {
                BigDecimal total = (BigDecimal)obj[0];
                if (L.equals(obj[3].toString()))
                    total = total.multiply(NEGATIVE);
                for (final StatementEntry entry : balanceSheet.getEntries())
                    if (obj[2].toString().equals(entry.getGlCode())) {
                        if (LOGGER.isDebugEnabled())
                            LOGGER.debug(entry.getGlCode() + "==================" + total);
                        if (entry.getFundWiseAmount().isEmpty())
                            entry.getFundWiseAmount().put(
                                    balanceSheetService.getFundNameForId(fundList, new Integer(obj[1].toString())),
                                    balanceSheetService.divideAndRound(total, divisor));
                        else {
                            boolean shouldAddNewFund = true;
                            for (final Entry<String, BigDecimal> object : entry.getFundWiseAmount().entrySet())
                                if (object.getKey().equalsIgnoreCase(
                                        balanceSheetService.getFundNameForId(fundList, new Integer(obj[1].toString())))) {
                                    entry.getFundWiseAmount().put(object.getKey(),
                                            object.getValue().add(balanceSheetService.divideAndRound(total, divisor)));
                                    shouldAddNewFund = false;
                                }
                            if (shouldAddNewFund)
                                entry.getFundWiseAmount().put(
                                        balanceSheetService.getFundNameForId(fundList, new Integer(obj[1].toString())),
                                        balanceSheetService.divideAndRound(total, divisor));
                        }
                    }
            }
    }

    public void addOpeningBalanceForPreviousYear(final Statement balanceSheet, final String transactionQuery, final Date fromDate) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("addOpeningBalanceForPreviousYear");
        final BigDecimal divisor = balanceSheet.getDivisor();
        final CFinancialYear prevFinanciaYr = financialYearDAO.getPreviousFinancialYearByDate(fromDate);
        final String prevFinancialYrId = prevFinanciaYr.getId().toString();
        final Query query = persistenceService.getSession()
                .createSQLQuery(
                        "select sum(openingdebitbalance)- sum(openingcreditbalance),coa.glcode,coa.type FROM transactionsummary ts,chartofaccounts coa  WHERE ts.glcodeid = coa.ID  AND ts.financialyearid="
                                + prevFinancialYrId + transactionQuery + " GROUP BY coa.glcode,coa.type");
        final List<Object[]> openingBalanceAmountList = query.list();
        for (final Object[] obj : openingBalanceAmountList)
            if (obj[0] != null && obj[1] != null) {

                BigDecimal total = (BigDecimal)obj[0];

                if (L.equals(obj[2].toString()))
                    total = total.multiply(NEGATIVE);
                for (final StatementEntry entry : balanceSheet.getEntries())
                    if (obj[1].toString().equals(entry.getGlCode())) {
                        if (LOGGER.isDebugEnabled())
                            LOGGER.debug(entry.getGlCode() + "==================" + total);
                        BigDecimal prevYrTotal = entry.getPreviousYearTotal();
                        prevYrTotal = prevYrTotal == null ? BigDecimal.ZERO : prevYrTotal;
                        entry.setPreviousYearTotal(prevYrTotal.add(balanceSheetService.divideAndRound(total, divisor)));
                    }
            }
    }
    private String getGlcodeForPurposeCode7() {
        final Query query = persistenceService.getSession().createSQLQuery(
                "select glcode from chartofaccounts where purposeid=7");
        final List list = query.list();
        String glCode = "";
        if (list.get(0) != null)
            glCode = list.get(0).toString();
        return glCode;
    }

    private String getGlcodeForPurposeCode7MinorCode() {
        final Query query = persistenceService.getSession().createSQLQuery(
                "select substr(glcode,1," + minorCodeLength + ") from chartofaccounts where purposeid=7");
        final List list = query.list();
        String glCode = "";
        if (list.get(0) != null)
            glCode = list.get(0).toString();
        return glCode;
    }

    /* For Detailed */
    private String getGlcodeForPurposeCode7DetailedCode() {
        final Query query = persistenceService.getSession().createSQLQuery(
                "select substr(glcode,1," + detailCodeLength + ") from chartofaccounts where purposeid=7");
        final List list = query.list();
        String glCode = "";
        if (list.get(0) != null)
            glCode = list.get(0).toString();
        return glCode;
    }

    private void populatePreviousYearTotalsForSchedule(final Statement balanceSheet, final String filterQuery, final Date toDate,
            final Date fromDate,
            final String majorCode, final Character type) {
        String formattedToDate = "";
        if ("Yearly".equalsIgnoreCase(balanceSheet.getPeriod()))
        {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(fromDate);
            cal.add(Calendar.DATE, -1);
            formattedToDate = balanceSheetService.getFormattedDate(cal.getTime());
        }
        else
            formattedToDate = balanceSheetService.getFormattedDate(balanceSheetService.getPreviousYearFor(toDate));
        final StringBuffer qry = new StringBuffer(512);
        qry.append("select sum(debitamount)-sum(creditamount),c.glcode from generalledger g,chartofaccounts c,voucherheader v   ");
        if (balanceSheet.getDepartment() != null && balanceSheet.getDepartment().getId() != -1)
            qry.append(", VoucherMis mis ");
        qry.append(" where  v.id=g.voucherheaderid and c.id=g.glcodeid and v.status not in(" + voucherStatusToExclude + ") ");
        if (balanceSheet.getDepartment() != null && balanceSheet.getDepartment().getId() != -1)
            qry.append(" and v.id= mis.voucherheaderid ");
        qry.append(" AND v.voucherdate <= '" + formattedToDate + "' and v.voucherdate >='"
                +balanceSheetService.getFormattedDate(balanceSheetService.getPreviousYearFor(fromDate)) +
                "' and c.glcode in (select distinct coad.glcode from chartofaccounts coa2, schedulemapping s " +
                ",chartofaccounts coad where s.id=coa2.scheduleid and coa2.classification=2 and s.reporttype = 'BS'" +
                " and coa2.glcode=SUBSTR(coad.glcode,1," + minorCodeLength + ") and coad.classification=4 and coad.majorcode='"
                + majorCode + "')  and c.majorcode='" + majorCode + "' and c.classification=4 " + filterQuery
                + " group by c.glcode");
        final Query query = persistenceService.getSession().createSQLQuery(qry.toString());
        final List<Object[]> result = query.list();
        for (final Object[] row : result)
            for (int index = 0; index < balanceSheet.size(); index++)
                if (balanceSheet.get(index).getGlCode() != null
                && row[1].toString().equalsIgnoreCase(balanceSheet.get(index).getGlCode())) {

                    BigDecimal previousYearTotal = new BigDecimal(row[0].toString());
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug(row[1] + "-----------------------------------" + previousYearTotal);
                    if (L.equalsIgnoreCase(type.toString()))
                        previousYearTotal = previousYearTotal.multiply(NEGATIVE);
                    previousYearTotal = balanceSheetService.divideAndRound(previousYearTotal, balanceSheet.getDivisor());
                    balanceSheet.get(index).setPreviousYearTotal(previousYearTotal);
                }
    }

    private void populateCurrentYearAmountForSchedule(final Statement balanceSheet, final List<Fund> fundList,
            final String filterQuery,
            final Date toDate, final Date fromDate, final String majorCode, final Character type) {
        final BigDecimal divisor = balanceSheet.getDivisor();
        final List<Object[]> allGlCodes = getAllDetailGlCodesForSubSchedule(majorCode, type, BS);
        // addRowForSchedule(balanceSheet, allGlCodes);
        final List<Object[]> resultMap = currentYearAmountQuery(filterQuery, toDate, fromDate, majorCode, BS);
        for (final Object[] obj : allGlCodes)
            if (!contains(resultMap, obj[0].toString()))
                balanceSheet.add(new StatementEntry(obj[0].toString(), obj[1].toString(), "", BigDecimal.ZERO, BigDecimal.ZERO,
                        false));
            else {
                final List<Object[]> rowsForGlcode = getRowsForGlcode(resultMap, obj[0].toString());
                for (final Object[] row : rowsForGlcode)
                    if (!balanceSheet.containsBalanceSheetEntry(row[2].toString())) {
                        final StatementEntry balanceSheetEntry = new StatementEntry();
                        if (row[0] != null && row[1] != null) {
                            BigDecimal total = (BigDecimal)row[0];
                            if (LOGGER.isDebugEnabled())
                                LOGGER.debug(row[0] + "-----" + row[1] + "------------------------------" + total);
                            if (L.equalsIgnoreCase(type.toString()))
                                total = total.multiply(NEGATIVE);
                            balanceSheetEntry.getFundWiseAmount().put(
                                    balanceSheetService.getFundNameForId(fundList, new Integer(row[1].toString())),
                                    balanceSheetService.divideAndRound(total, divisor));
                        }
                        if (row[2] != null)
                            balanceSheetEntry.setGlCode(row[2].toString());
                        balanceSheetEntry.setAccountName(obj[1].toString());
                        balanceSheet.add(balanceSheetEntry);
                    } else
                        for (int index = 0; index < balanceSheet.size(); index++) {
                            BigDecimal amount = balanceSheetService.divideAndRound((BigDecimal)row[0], divisor);
                            if (LOGGER.isDebugEnabled())
                                LOGGER.debug(row[0] + "-----" + row[1] + "------------------------------" + amount);
                            if (L.equalsIgnoreCase(type.toString()))
                                amount = amount.multiply(NEGATIVE);
                            if (balanceSheet.get(index).getGlCode() != null
                                    && row[2].toString().equals(balanceSheet.get(index).getGlCode())) {
                                final String fundNameForId = balanceSheetService.getFundNameForId(fundList,
                                        new Integer(row[1].toString()));
                                if (balanceSheet.get(index).getFundWiseAmount().get(fundNameForId) == null)
                                    balanceSheet
                                    .get(index)
                                    .getFundWiseAmount()
                                    .put(balanceSheetService.getFundNameForId(fundList, new Integer(row[1].toString())),
                                            amount);
                                else
                                    balanceSheet
                                    .get(index)
                                    .getFundWiseAmount()
                                    .put(balanceSheetService.getFundNameForId(fundList, new Integer(row[1].toString())),
                                            balanceSheet
                                            .get(index)
                                            .getFundWiseAmount()
                                            .get(balanceSheetService.getFundNameForId(fundList, new Integer(
                                                    row[1].toString()))).add(amount));
                            }
                        }
            }
    }

    /* For detailed */
    public void populateDataForAllSchedulesDetailed(final Statement balanceSheet) {
        getAppConfigValueForRemoveEntrysWithZeroAmount();
        voucherStatusToExclude = getAppConfigValueFor("EGF", "statusexcludeReport");
        minorCodeLength = Integer.valueOf(balanceSheetService.getAppConfigValueFor(Constants.EGF, "coa_minorcode_length"));
        majorCodeLength = Integer.valueOf(balanceSheetService.getAppConfigValueFor(Constants.EGF, "coa_majorcode_length"));
        detailCodeLength = Integer.valueOf(balanceSheetService.getAppConfigValueFor(Constants.EGF, "coa_detailcode_length"));
        final Date fromDate = balanceSheetService.getFromDate(balanceSheet);
        final Date toDate = balanceSheetService.getToDate(balanceSheet);
        final List<Fund> fundList = balanceSheet.getFunds();
        populateCurrentYearAmountForAllSchedulesDetailed(balanceSheet, fundList,
                amountPerFundQueryForAllSchedulesDetailed(balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate, BS));
        addCurrentOpeningBalancePerFund(balanceSheet, fundList, balanceSheetService.getTransactionQuery(balanceSheet));
        populatePreviousYearTotalsForScheduleForAllSchedulesDetailed(balanceSheet,
                balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate);
        addOpeningBalanceForPreviousYear(balanceSheet, balanceSheetService.getTransactionQuery(balanceSheet), fromDate);
        balanceSheetService.addExcessIEForCurrentYear(balanceSheet, fundList, getGlcodeForPurposeCode7DetailedCode(),
                balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate);
        balanceSheetService.addExcessIEForPreviousYear(balanceSheet, fundList, getGlcodeForPurposeCode7DetailedCode(),
                balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate);
        balanceSheetService.removeFundsWithNoData(balanceSheet);
        balanceSheetService.computeCurrentYearTotals(balanceSheet, Constants.LIABILITIES, Constants.ASSETS);
        computeAndAddTotals(balanceSheet);
        computeAndAddTotalsForSchedules(balanceSheet);
        if (removeEntrysWithZeroAmount.equalsIgnoreCase("Yes"))
            balanceSheetService.removeScheduleEntrysWithZeroAmount(balanceSheet);
    }

    public void populateDataForAllSchedules(final Statement balanceSheet) {
        getAppConfigValueForRemoveEntrysWithZeroAmount();
        voucherStatusToExclude = getAppConfigValueFor("EGF", "statusexcludeReport");
        minorCodeLength = Integer.valueOf(balanceSheetService.getAppConfigValueFor(Constants.EGF, "coa_minorcode_length"));
        majorCodeLength = Integer.valueOf(balanceSheetService.getAppConfigValueFor(Constants.EGF, "coa_majorcode_length"));
        detailCodeLength = Integer.valueOf(balanceSheetService.getAppConfigValueFor(Constants.EGF, "coa_detailcode_length"));
        final Date fromDate = balanceSheetService.getFromDate(balanceSheet);
        final Date toDate = balanceSheetService.getToDate(balanceSheet);
        final List<Fund> fundList = balanceSheet.getFunds();
        populateCurrentYearAmountForAllSchedules(balanceSheet, fundList,
                amountPerFundQueryForAllSchedules(balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate, BS));
        addCurrentOpeningBalancePerFund(balanceSheet, fundList, balanceSheetService.getTransactionQuery(balanceSheet));
        populatePreviousYearTotalsForScheduleForAllSchedules(balanceSheet, balanceSheetService.getFilterQuery(balanceSheet),
                toDate, fromDate);
        addOpeningBalanceForPreviousYear(balanceSheet, balanceSheetService.getTransactionQuery(balanceSheet), fromDate);
        balanceSheetService.addExcessIEForCurrentYear(balanceSheet, fundList, getGlcodeForPurposeCode7MinorCode(),
                balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate);
        balanceSheetService.addExcessIEForPreviousYear(balanceSheet, fundList, getGlcodeForPurposeCode7MinorCode(),
                balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate);
        balanceSheetService.removeFundsWithNoData(balanceSheet);
        balanceSheetService.computeCurrentYearTotals(balanceSheet, Constants.LIABILITIES, Constants.ASSETS);
        computeAndAddTotals(balanceSheet);
        if (removeEntrysWithZeroAmount.equalsIgnoreCase("Yes"))
            balanceSheetService.removeScheduleEntrysWithZeroAmount(balanceSheet);
    }

    private void populatePreviousYearTotalsForScheduleForAllSchedules(final Statement balanceSheet, final String filterQuery,
            final Date toDate, final Date fromDate) {
        Date formattedToDate = null;
        final BigDecimal divisor = balanceSheet.getDivisor();
        if ("Yearly".equalsIgnoreCase(balanceSheet.getPeriod()))
        {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(fromDate);
            cal.add(Calendar.DATE, -1);
            formattedToDate = cal.getTime();
        }
        else
            formattedToDate = balanceSheetService.getPreviousYearFor(toDate);
        final List<Object[]> resultMap = amountPerFundQueryForAllSchedules(filterQuery, formattedToDate,
                balanceSheetService.getPreviousYearFor(fromDate), BS);
        final List<Object[]> allGlCodes = getAllGlCodesForAllSchedule(BS, "('A','L')");
        for (final Object[] obj : allGlCodes)
            for (final Object[] row : resultMap) {
                final String glCode = row[2].toString();
                if (glCode.substring(1, majorCodeLength).equals(obj[0].toString())) {
                    final String type = obj[3].toString();
                    if (!balanceSheet.containsBalanceSheetEntry(row[2].toString()))
                        addRowToStatement(balanceSheet, row, glCode);
                    else
                        for (int index = 0; index < balanceSheet.size(); index++) {
                            BigDecimal amount = balanceSheetService.divideAndRound((BigDecimal)row[0], divisor);
                            if (L.equalsIgnoreCase(type))
                                amount = amount.multiply(NEGATIVE);
                            if (balanceSheet.get(index).getGlCode() != null
                                    && row[2].toString().equals(balanceSheet.get(index).getGlCode())) {
                                BigDecimal prevYrTotal = balanceSheet.get(index).getPreviousYearTotal();
                                prevYrTotal = prevYrTotal == null ? BigDecimal.ZERO : prevYrTotal;
                                balanceSheet.get(index).setPreviousYearTotal(prevYrTotal.add(amount));
                            }
                        }
                }
            }
    }

    /* for detailed */
    private void populatePreviousYearTotalsForScheduleForAllSchedulesDetailed(final Statement balanceSheet,
            final String filterQuery,
            final Date toDate, final Date fromDate) {
        Date formattedToDate = null;
        final BigDecimal divisor = balanceSheet.getDivisor();
        if ("Yearly".equalsIgnoreCase(balanceSheet.getPeriod()))
        {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(fromDate);
            cal.add(Calendar.DATE, -1);
            formattedToDate = cal.getTime();
        }
        else
            formattedToDate = balanceSheetService.getPreviousYearFor(toDate);
        final List<Object[]> resultMap = amountPerFundQueryForAllSchedulesDetailed(filterQuery, formattedToDate,
                balanceSheetService.getPreviousYearFor(fromDate), BS);
        final List<Object[]> allGlCodes = getAllGlCodesForAllSchedule(BS, "('A','L')");
        for (final Object[] obj : allGlCodes)
            for (final Object[] row : resultMap) {
                final String glCode = row[2].toString();
                if (glCode.substring(1, majorCodeLength).equals(obj[0].toString())) {
                    final String type = obj[3].toString();
                    if (!balanceSheet.containsBalanceSheetEntry(row[2].toString()))
                        addRowToStatement(balanceSheet, row, glCode);
                    else
                        for (int index = 0; index < balanceSheet.size(); index++) {
                            BigDecimal amount = balanceSheetService.divideAndRound((BigDecimal)row[0], divisor);
                            if (L.equalsIgnoreCase(type))
                                amount = amount.multiply(NEGATIVE);
                            if (balanceSheet.get(index).getGlCode() != null
                                    && row[2].toString().equals(balanceSheet.get(index).getGlCode())) {
                                BigDecimal prevYrTotal = balanceSheet.get(index).getPreviousYearTotal();
                                prevYrTotal = prevYrTotal == null ? BigDecimal.ZERO : prevYrTotal;
                                balanceSheet.get(index).setPreviousYearTotal(prevYrTotal.add(amount));
                            }
                        }
                }
            }
    }

    private void populateCurrentYearAmountForAllSchedules(final Statement balanceSheet, final List<Fund> fundList,
            final List<Object[]> currentYearAmounts) {
        final BigDecimal divisor = balanceSheet.getDivisor();
        final Map<String, Schedules> scheduleToGlCodeMap = getScheduleToGlCodeMap(BS, "('A','L')");
        for (final Entry<String, Schedules> entry : scheduleToGlCodeMap.entrySet()) {
            final String scheduleNumber = entry.getValue().scheduleNumber;
            final String scheduleName = entry.getValue().scheduleName;
            final String type = entry.getValue().chartOfAccount.size() > 0 ? entry.getValue().chartOfAccount.iterator().next().type
                    : "";
            balanceSheet.add(new StatementEntry(scheduleNumber, scheduleName, "", null, null, true));
            for (final Object[] row : currentYearAmounts) {
                final String glCode = row[2].toString();
                if (entry.getValue().contains(glCode))
                    if (!balanceSheet.containsBalanceSheetEntry(glCode)) {
                        final StatementEntry balanceSheetEntry = new StatementEntry();
                        if (row[0] != null && row[1] != null) {
                            BigDecimal total = (BigDecimal)row[0];
                            if (L.equalsIgnoreCase(type))
                                total = total.multiply(NEGATIVE);
                            balanceSheetEntry.getFundWiseAmount().put(
                                    balanceSheetService.getFundNameForId(fundList, new Integer(row[1].toString())),
                                    balanceSheetService.divideAndRound(total, divisor));
                        }
                        balanceSheetEntry.setGlCode(glCode);
                        balanceSheetEntry.setAccountName(entry.getValue().getCoaName(glCode));
                        balanceSheet.add(balanceSheetEntry);
                    } else
                        for (int index = 0; index < balanceSheet.size(); index++) {
                            BigDecimal amount = balanceSheetService.divideAndRound((BigDecimal)row[0], divisor);
                            if (L.equalsIgnoreCase(type))
                                amount = amount.multiply(NEGATIVE);
                            if (balanceSheet.get(index).getGlCode() != null
                                    && row[2].toString().equals(balanceSheet.get(index).getGlCode())) {
                                final String fundNameForId = balanceSheetService.getFundNameForId(fundList,
                                        new Integer(row[1].toString()));
                                if (balanceSheet.get(index).getFundWiseAmount().get(fundNameForId) == null)
                                    balanceSheet.get(index).getFundWiseAmount().put(fundNameForId, amount);
                                else
                                    balanceSheet
                                    .get(index)
                                    .getFundWiseAmount()
                                    .put(fundNameForId,
                                            balanceSheet.get(index).getFundWiseAmount().get(fundNameForId).add(amount));
                            }
                        }
            }
            for (final ChartOfAccount s : entry.getValue().chartOfAccount)
                if (!balanceSheet.containsBalanceSheetEntry(s.glCode)) {
                    final StatementEntry balanceSheetEntry = new StatementEntry();
                    balanceSheetEntry.setGlCode(s.glCode);
                    balanceSheetEntry.setAccountName(s.name);
                    balanceSheet.add(balanceSheetEntry);
                }
        }
    }

    /* for detailed */
    private void populateCurrentYearAmountForAllSchedulesDetailed(final Statement balanceSheet, final List<Fund> fundList,
            final List<Object[]> currentYearAmounts) {
        final BigDecimal divisor = balanceSheet.getDivisor();
        final Map<String, Schedules> scheduleToGlCodeMap = getScheduleToGlCodeMapDetailed(BS, "('A','L')");
        for (final Entry<String, Schedules> entry : scheduleToGlCodeMap.entrySet()) {
            final String scheduleNumber = entry.getValue().scheduleNumber;
            final String scheduleName = entry.getValue().scheduleName;
            final String type = entry.getValue().chartOfAccount.size() > 0 ? entry.getValue().chartOfAccount.iterator().next().type
                    : "";
            balanceSheet.add(new StatementEntry(scheduleNumber, scheduleName, "", null, null, true));
            for (final Object[] row : currentYearAmounts) {
                final String glCode = row[2].toString();
                if (entry.getValue().contains(glCode))
                    if (!balanceSheet.containsBalanceSheetEntry(glCode)) {
                        final StatementEntry balanceSheetEntry = new StatementEntry();
                        if (row[0] != null && row[1] != null) {
                            BigDecimal total = (BigDecimal)row[0];
                            if (L.equalsIgnoreCase(type))
                                total = total.multiply(NEGATIVE);
                            balanceSheetEntry.getFundWiseAmount().put(
                                    balanceSheetService.getFundNameForId(fundList, new Integer(row[1].toString())),
                                    balanceSheetService.divideAndRound(total, divisor));
                        }
                        balanceSheetEntry.setGlCode(glCode);
                        balanceSheetEntry.setAccountName(entry.getValue().getCoaName(glCode));
                        balanceSheet.add(balanceSheetEntry);
                    } else
                        for (int index = 0; index < balanceSheet.size(); index++) {
                            BigDecimal amount = balanceSheetService.divideAndRound((BigDecimal)row[0], divisor);
                            if (L.equalsIgnoreCase(type))
                                amount = amount.multiply(NEGATIVE);
                            if (balanceSheet.get(index).getGlCode() != null
                                    && row[2].toString().equals(balanceSheet.get(index).getGlCode())) {
                                final String fundNameForId = balanceSheetService.getFundNameForId(fundList,
                                        new Integer(row[1].toString()));
                                if (balanceSheet.get(index).getFundWiseAmount().get(fundNameForId) == null)
                                    balanceSheet.get(index).getFundWiseAmount().put(fundNameForId, amount);
                                else
                                    balanceSheet
                                    .get(index)
                                    .getFundWiseAmount()
                                    .put(fundNameForId,
                                            balanceSheet.get(index).getFundWiseAmount().get(fundNameForId).add(amount));
                            }
                        }
            }
            for (final ChartOfAccount s : entry.getValue().chartOfAccount)
                if (!balanceSheet.containsBalanceSheetEntry(s.glCode)) {
                    final StatementEntry balanceSheetEntry = new StatementEntry();
                    balanceSheetEntry.setGlCode(s.glCode);
                    balanceSheetEntry.setAccountName(s.name);
                    balanceSheet.add(balanceSheetEntry);
                }
            balanceSheet.add(new StatementEntry("", "Schedule Total", "", null, null, true));
        }
    }

    private void getAppConfigValueForRemoveEntrysWithZeroAmount() {
        try {
            final List<AppConfigValues> configValues = appConfigValuesService.
                    getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,
                            FinancialConstants.REMOVE_ENTRIES_WITH_ZERO_AMOUNT_IN_REPORT);

            for (final AppConfigValues appConfigVal : configValues)
                removeEntrysWithZeroAmount = appConfigVal.getValue();
        } catch (final Exception e) {
            throw new ApplicationRuntimeException(
                    "Appconfig value for remove entries with zero amount in report is not defined in the system");
        }
    }

    public String getRemoveEntrysWithZeroAmount() {
        return removeEntrysWithZeroAmount;
    }

    public void setRemoveEntrysWithZeroAmount(final String removeEntrysWithZeroAmount) {
        this.removeEntrysWithZeroAmount = removeEntrysWithZeroAmount;
    }

}