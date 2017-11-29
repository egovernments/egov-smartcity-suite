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
import org.egov.commons.Fund;
import org.egov.egf.model.IEStatementEntry;
import org.egov.egf.model.Statement;
import org.egov.egf.model.StatementEntry;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.Constants;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class IncomeExpenditureScheduleService extends ScheduleService {
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;

    private static final String IE = "IE";
    private static final String I = "I";
    private IncomeExpenditureService incomeExpenditureService;
    private static final Logger LOGGER = Logger.getLogger(IncomeExpenditureScheduleService.class);

    public void populateDataForLedgerSchedule(final Statement statement, final String majorCode) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Getting ledger details for selected schedlue");
        voucherStatusToExclude = getAppConfigValueFor("EGF", "statusexcludeReport");
        minorCodeLength = Integer.valueOf(incomeExpenditureService.getAppConfigValueFor(Constants.EGF, "coa_minorcode_length"));
        final Date fromDate = incomeExpenditureService.getFromDate(statement);
        final Date toDate = incomeExpenditureService.getToDate(statement);
        final String filterQuery = incomeExpenditureService.getFilterQuery(statement);
        final CChartOfAccounts coa = (CChartOfAccounts) find("from CChartOfAccounts where glcode=?", majorCode);
        populateCurrentYearAmountForDetail(statement, toDate, fromDate, majorCode, coa.getType(), filterQuery);
        incomeExpenditureService.removeFundsWithNoDataIE(statement);
        computeAndAddScheduleTotals(statement);
    }

    public void populateDataForAllSchedules(final Statement statement) {
        voucherStatusToExclude = getAppConfigValueFor("EGF", "statusexcludeReport");
        minorCodeLength = Integer.valueOf(incomeExpenditureService.getAppConfigValueFor(Constants.EGF, "coa_minorcode_length"));
        final Date fromDate = incomeExpenditureService.getFromDate(statement);
        final Date toDate = incomeExpenditureService.getToDate(statement);
        final List<Fund> fundList = statement.getFunds();
        populateCurrentYearAmountForAllSchedules(statement, fundList,
                amountPerFundQueryForAllSchedules(incomeExpenditureService.getFilterQuery(statement), toDate, fromDate, IE));
        populatePreviousYearTotalsForAllSchedules(statement, incomeExpenditureService.getFilterQuery(statement), toDate, fromDate);
        incomeExpenditureService.removeFundsWithNoData(statement);
        incomeExpenditureService.computeCurrentYearTotals(statement, Constants.LIABILITIES, Constants.ASSETS);
        computeAndAddTotals(statement);
    }

    private Query populatePreviousYearTotals(final Statement statement, final Date toDate, final Date fromDate,
            final String majorCode,
            final String filterQuery, final String fundId) {
        String formattedToDate = "";
        final String voucherStatusToExclude = getAppConfigValueFor("EGF", "statusexcludeReport");
        String majorCodeQuery = "";
        if (!(majorCodeQuery.equals("") || majorCodeQuery.isEmpty()))
            majorCodeQuery = " and c.majorcode = '" + majorCode + "' ";

        if (LOGGER.isInfoEnabled())
            LOGGER.info("Getting previous year Details");
        if ("Yearly".equalsIgnoreCase(statement.getPeriod()))
            formattedToDate = incomeExpenditureService.getFormattedDate(fromDate);
        else
            formattedToDate = incomeExpenditureService.getFormattedDate(incomeExpenditureService.getPreviousYearFor(toDate));
        final Query query = persistenceService.getSession()
                .createSQLQuery(
                        "select c.glcode,c.name ,sum(g.debitamount)-sum(g.creditamount),v.fundid ,c.type ,c.majorcode  from "
                                +
                                "generalledger g,chartofaccounts c,voucherheader v ,vouchermis mis where v.id=mis.voucherheaderid and  v.fundid in"
                                + fundId +
                                " and v.id=g.voucherheaderid " +
                                " and c.id=g.glcodeid and v.status not in(" + voucherStatusToExclude + ")  AND v.voucherdate < '"
                                + formattedToDate + "' and v.voucherdate >='" +
                                incomeExpenditureService.getFormattedDate(incomeExpenditureService.getPreviousYearFor(fromDate))
                                + "'" + majorCodeQuery +
                                filterQuery
                                + " group by c.glcode, v.fundid,c.name ,c.type ,c.majorcode order by c.glcode,v.fundid,c.type");
        if (LOGGER.isInfoEnabled())
            LOGGER.info("prevoius year to Date=" + formattedToDate + " and from Date="
                    + incomeExpenditureService.getPreviousYearFor(fromDate));
        return query;
    }

    public void populateDetailcode(final Statement statement) {
        final Date fromDate = incomeExpenditureService.getFromDate(statement);
        final Date toDate = incomeExpenditureService.getToDate(statement);
        // List<Fund> fundList = statement.getFunds();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("preparing list to load all detailcode");
        populateAmountForAllSchedules(statement, toDate, fromDate, "('I','E')");
        incomeExpenditureService.removeFundsWithNoDataIE(statement);

    }

    boolean isIEContainsScheduleEntry(final List<Object[]> accountCodeList, final String majorCode) {
        for (final Object[] row : accountCodeList)
            if (row[3] != null && majorCode.equals(row[3].toString()))
                return true;
        return false;
    }

    @SuppressWarnings({ "unused", "unchecked" })
    private void populateAmountForAllSchedules(final Statement statement, final Date toDate, final Date fromDate,
            final String reportType) {
        boolean addrow = false;
        final BigDecimal divisor = statement.getDivisor();
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal preAmount = BigDecimal.ZERO;
        new HashMap<String, String>();
        final IEStatementEntry scheduleEntry = new IEStatementEntry();
        Map<String, BigDecimal> currentYearScheduleTotal = new HashMap<String, BigDecimal>();
        ;
        Map<String, BigDecimal> previousYearScheduleTotal = new HashMap<String, BigDecimal>();
        ;
        new HashMap<String, BigDecimal>();
        new HashMap<String, BigDecimal>();
        new HashMap<String, BigDecimal>();
        new HashMap<String, BigDecimal>();

        boolean addschedulerow = false;
        final String forAllCOA = "";
        final String filterQuery = incomeExpenditureService.getFilterQuery(statement);
        final String fundId = incomeExpenditureService.getfundList(statement.getFunds());
        majorCodeLength = Integer.valueOf(incomeExpenditureService.getAppConfigValueFor(Constants.EGF, "coa_majorcode_length"));
        final List<Object[]> previousLedgerBalance = populatePreviousYearTotals(statement, toDate, fromDate, forAllCOA,
                filterQuery,
                fundId).list();
        final List<Object[]> CurrentYearLedgerDetail = getAllLedgerTransaction(forAllCOA, toDate, fromDate, fundId, filterQuery); // current
        // year
        // transaction
        // detail
        final List<Object[]> schduleMap = getAllGlCodesForSchedule(reportType);
        for (final Object[] row : schduleMap) {
            row[0].toString();
            if (!statement.containsMajorCodeEntry(row[0].toString().substring(1, majorCodeLength)))
                if (statement.getIeEntries().size() > 1) {
                    statement.addIE(new IEStatementEntry(null, "Schedule Total", currentYearScheduleTotal,
                            previousYearScheduleTotal, true));
                    statement.addIE(new IEStatementEntry("Schedule " + row[1].toString() + ":", row[2].toString(), "", row[0]
                            .toString().substring(1, majorCodeLength), true));
                    currentYearScheduleTotal = new HashMap<String, BigDecimal>();
                    previousYearScheduleTotal = new HashMap<String, BigDecimal>();
                } else
                    statement.addIE(new IEStatementEntry("Schedule " + row[1].toString() + ":", row[2].toString(), "", row[0]
                            .toString().substring(1, majorCodeLength), true));
            if (!statement.containsIEStatementEntry(row[0].toString())) {
                final IEStatementEntry ieEntry = new IEStatementEntry();
                if (ieContains(CurrentYearLedgerDetail, row[0].toString()))
                    for (final Object[] cur : CurrentYearLedgerDetail) {
                        final String fundnm = incomeExpenditureService.getFundNameForId(statement.getFunds(),
                                Integer.valueOf(cur[3].toString()));
                        if (cur[0].toString().equals(row[0].toString())) {
                            addrow = true;
                            if (I.equalsIgnoreCase(cur[4].toString()))
                                amount = ((BigDecimal)cur[2]).multiply(NEGATIVE);
                            /*
                             * if(currentYearTotalIncome.containsKey(fundnm) && currentYearTotalIncome.get(fundnm)!=null)
                             * currentYearTotalIncome .put(fundnm,currentYearTotalIncome.get(fundnm).add(incomeExpenditureService
                             * .divideAndRound(amount, divisor))); else
                             * currentYearTotalIncome.put(fundnm,incomeExpenditureService.divideAndRound(amount, divisor));
                             */
                            else
                                amount = (BigDecimal)cur[2] ;
                            /*
                             * if(currentYearTotalExpense.containsKey(fundnm))
                             * currentYearTotalExpense.put(fundnm,currentYearTotalExpense
                             * .get(fundnm).add(incomeExpenditureService.divideAndRound(amount, divisor))); else
                             * currentYearTotalExpense.put(fundnm,incomeExpenditureService.divideAndRound(amount, divisor));
                             */
                            if (currentYearScheduleTotal.containsKey(fundnm))
                                currentYearScheduleTotal.put(
                                        fundnm,
                                        currentYearScheduleTotal.get(fundnm).add(
                                                zeroOrValue(incomeExpenditureService.divideAndRound(amount, divisor))));
                            else
                                currentYearScheduleTotal.put(fundnm, incomeExpenditureService.divideAndRound(amount, divisor));
                            ieEntry.getNetAmount().put(fundnm, incomeExpenditureService.divideAndRound(amount, divisor));
                        }
                    }
                if (ieContains(previousLedgerBalance, row[0].toString()))
                    for (final Object[] pre : previousLedgerBalance)
                        if (pre[0].toString().equals(row[0].toString())) {
                            final String fundnm = incomeExpenditureService.getFundNameForId(statement.getFunds(),
                                    Integer.valueOf(pre[3].toString()));
                            addrow = true;
                            if (I.equalsIgnoreCase(pre[4].toString()))
                                preAmount = ((BigDecimal)pre[2]).multiply(NEGATIVE);
                            /*
                             * if(previousYearTotalIncome.containsKey(fundnm))
                             * previousYearTotalIncome.put(fundnm,previousYearTotalIncome
                             * .get(fundnm).add(incomeExpenditureService.divideAndRound(preAmount, divisor))); else
                             * previousYearTotalIncome.put(fundnm,incomeExpenditureService.divideAndRound(preAmount, divisor));
                             */
                            else
                                preAmount = (BigDecimal)pre[2] ;
                            /*
                             * if(previousYearTotalExpense.containsKey(fundnm))
                             * previousYearTotalExpense.get(fundnm).add(incomeExpenditureService.divideAndRound(preAmount,
                             * divisor)); else
                             * previousYearTotalExpense.put(fundnm,incomeExpenditureService.divideAndRound(preAmount, divisor));
                             */

                            if (previousYearScheduleTotal.containsKey(fundnm))
                                previousYearScheduleTotal.put(
                                        fundnm,
                                        previousYearScheduleTotal.get(fundnm).add(
                                                zeroOrValue(incomeExpenditureService.divideAndRound(preAmount, divisor))));
                            else
                                previousYearScheduleTotal
                                .put(fundnm, incomeExpenditureService.divideAndRound(preAmount, divisor));
                            ieEntry.getPreviousYearAmount().put(fundnm,
                                    incomeExpenditureService.divideAndRound(preAmount, divisor));
                        }
                if (addschedulerow)
                    if (!statement.containsIEStatementEntry(row[0].toString()))
                        statement.addIE(scheduleEntry);
                if (addrow) {
                    ieEntry.setGlCode(row[0].toString());
                    ieEntry.setAccountName(row[4].toString());
                    ieEntry.setMajorCode(row[2] != null ? row[2].toString() : "");
                    statement.addIE(ieEntry);
                }
                addrow = false;
                addschedulerow = false;
            }

        }
        final int lastIndex = statement.getIeEntries().size();
        if (statement.getIE(lastIndex-1 ).getGlCode().contains("Schedule")
                && !statement.getIE(lastIndex - 1).getGlCode().contains("Schedule Total"))
            statement.getIeEntries().remove(lastIndex - 1);
    }

    /* for detailed */
    /*
     * void computeAndAddTotalsForSchedules(Statement statement) { BigDecimal currentTotal = BigDecimal.ZERO; BigDecimal
     * previousTotal = BigDecimal.ZERO; for(StatementEntry entry : statement.getEntries()){
     * if(entry.getAccountName().equals("Schedule Total")){ entry.setCurrentYearTotal(currentTotal);
     * entry.setPreviousYearTotal(previousTotal); currentTotal = BigDecimal.ZERO; previousTotal = BigDecimal.ZERO; }else{
     * if(entry.getCurrentYearTotal() != null) currentTotal = currentTotal.add(entry.getCurrentYearTotal());
     * if(entry.getPreviousYearTotal() != null) previousTotal = previousTotal.add(entry.getPreviousYearTotal()); } } }
     */

    /* for detailed */
    @Override
    void computeAndAddTotalsForSchedules(final Statement statement) {
        BigDecimal currentTotal = BigDecimal.ZERO;
        BigDecimal previousTotal = BigDecimal.ZERO;
        final Map<String, BigDecimal> fundNetTotals = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> fundPreTotals = new HashMap<String, BigDecimal>();
        // int lastInd, size = statement.getIeEntries().size();

        for (final IEStatementEntry entry : statement.getIeEntries())
            if (entry.getAccountName().equals("Schedule Total")) {
                for (final Entry<String, BigDecimal> row : fundNetTotals.entrySet())
                    if (fundNetTotals.get(row.getKey()) == null)
                        entry.getNetAmount().put(row.getKey(), fundNetTotals.get(row.getKey()));
                for (final Entry<String, BigDecimal> row : fundPreTotals.entrySet())
                    if (fundNetTotals.get(row.getKey()) == null)
                        entry.getPreviousYearAmount().put(row.getKey(), fundPreTotals.get(row.getKey()));

                currentTotal = BigDecimal.ZERO;
                previousTotal = BigDecimal.ZERO;
            } else {
                for (final Entry<String, BigDecimal> row : entry.getNetAmount().entrySet()) {
                    if (fundNetTotals.get(row.getKey()) == null)
                        fundNetTotals.put(row.getKey(), BigDecimal.ZERO);
                    currentTotal = zeroOrValue(row.getValue());
                    fundNetTotals.put(row.getKey(), currentTotal.add(zeroOrValue(fundNetTotals.get(row.getKey()))));
                }
                for (final Entry<String, BigDecimal> prerow : entry.getPreviousYearAmount().entrySet()) {
                    if (fundPreTotals.get(prerow.getKey()) == null)
                        fundPreTotals.put(prerow.getKey(), BigDecimal.ZERO);
                    previousTotal = zeroOrValue(prerow.getValue());
                    fundPreTotals.put(prerow.getKey(), previousTotal.add(zeroOrValue(fundPreTotals.get(prerow.getKey()))));
                }
            }
    }

    /*
     * Add ledger details for current year and previous year. We dont calculate for openeing balance for income and expenditure
     * codes
     */

    @SuppressWarnings("unused")
    private void populateCurrentYearAmountForDetail(final Statement statement, final Date toDate, final Date fromDate,
            final String majorCode,
            final Character type, final String filterQuery) {
        boolean addrow = false;
        final BigDecimal divisor = statement.getDivisor();
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal preAmount = BigDecimal.ZERO;
        final String fundId = incomeExpenditureService.getfundList(statement.getFunds());
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Getting All ledger codes ..");
        final List<Object[]> AllLedger = persistenceService.getSession()
                .createSQLQuery(
                        "select coa.glcode,coa.name from chartofaccounts coa where coa.majorcode='" + majorCode
                        + "' and coa.classification=4 and coa.type='" + type + "'  order by coa.glcode").list();
        final List<Object[]> previousLedgerBalance = populatePreviousYearTotals(statement, toDate, fromDate, majorCode,
                filterQuery,
                fundId).list();
        final List<Object[]> allGlCodes = getAllGlCodesForSubSchedule(majorCode, type, IE); // for schedule name AND MINOR code

        final List<Object[]> CurrentYearLedgerDetail = getAllLedgerTransaction(majorCode, toDate, fromDate, fundId, filterQuery); // current
        // year
        // transaction
        // detail
        addRowForIESchedule(statement, allGlCodes);

        for (final Object[] row : AllLedger)
            if (!statement.containsIEStatementEntry(row[0].toString())) {
                final IEStatementEntry ieEntry = new IEStatementEntry();
                if (ieContains(CurrentYearLedgerDetail, row[0].toString()))
                    for (final Object[] cur : CurrentYearLedgerDetail)
                        if (cur[0].toString().equals(row[0].toString())) {
                            addrow = true;
                            if (I.equalsIgnoreCase(type.toString()))
                                amount = ((BigDecimal)cur[2]).multiply(NEGATIVE);
                            else
                                amount =(BigDecimal)cur[2];
                            ieEntry.getNetAmount()
                            .put(incomeExpenditureService.getFundNameForId(statement.getFunds(),
                                    Integer.valueOf(cur[3].toString())),
                                    incomeExpenditureService.divideAndRound(amount, divisor));

                        }
                if (ieContains(previousLedgerBalance, row[0].toString()))
                    for (final Object[] pre : previousLedgerBalance)
                        if (pre[0].toString().equals(row[0].toString())) {
                            addrow = true;
                            if (I.equalsIgnoreCase(type.toString()))
                                preAmount = ((BigDecimal)pre[2]) .multiply(NEGATIVE);
                            else
                                preAmount = (BigDecimal) pre[2];
                            ieEntry.getPreviousYearAmount().put(incomeExpenditureService.getFundNameForId(statement.getFunds(),
                                    Integer.valueOf(pre[3].toString())),
                                    incomeExpenditureService.divideAndRound(preAmount, divisor));

                        }
                if (addrow) {
                    ieEntry.setGlCode(row[0].toString());
                    ieEntry.setAccountName(row[1].toString());
                    statement.addIE(ieEntry);
                }
                addrow = false;
            }
    }

    private void populateCurrentYearAmountForAllSchedules(final Statement statement, final List<Fund> fundList,
            final List<Object[]> currentYearAmounts) {
        final BigDecimal divisor = statement.getDivisor();
        final Map<String, Schedules> scheduleToGlCodeMap = getScheduleToGlCodeMap(IE, "('I','E')");
        for (final Entry<String, Schedules> entry : scheduleToGlCodeMap.entrySet()) {
            final String scheduleNumber = entry.getValue().scheduleNumber;
            final String scheduleName = entry.getValue().scheduleName;
            final String type = entry.getValue().chartOfAccount.size() > 0 ? entry.getValue().chartOfAccount.iterator().next().type
                    : "";
            statement.add(new StatementEntry(scheduleNumber, scheduleName, "", null, null, true));
            for (final Object[] row : currentYearAmounts) {
                final String glCode = row[2].toString();
                if (entry.getValue().contains(glCode))
                    if (!statement.containsBalanceSheetEntry(glCode)) {
                        final StatementEntry balanceSheetEntry = new StatementEntry();
                        if (row[0] != null && row[1] != null) {
                            BigDecimal total = (BigDecimal)row[0];
                            if (I.equalsIgnoreCase(type))
                                total = total.multiply(NEGATIVE);
                            balanceSheetEntry.getFundWiseAmount().put(
                                    incomeExpenditureService.getFundNameForId(fundList, new Integer(row[1].toString())),
                                    incomeExpenditureService.divideAndRound(total, divisor));
                        }
                        balanceSheetEntry.setGlCode(glCode);
                        balanceSheetEntry.setAccountName(entry.getValue().getCoaName(glCode));
                        statement.add(balanceSheetEntry);
                    } else
                        for (int index = 0; index < statement.size(); index++) {
                            BigDecimal amount = incomeExpenditureService.divideAndRound((BigDecimal)row[0], divisor);
                            if (I.equalsIgnoreCase(type))
                                amount = amount.multiply(NEGATIVE);
                            if (statement.get(index).getGlCode() != null
                                    && row[2].toString().equals(statement.get(index).getGlCode())) {
                                final String fundNameForId = incomeExpenditureService.getFundNameForId(fundList,
                                        new Integer(row[1].toString()));
                                if (statement.get(index).getFundWiseAmount().get(fundNameForId) == null)
                                    statement
                                    .get(index)
                                    .getFundWiseAmount()
                                    .put(incomeExpenditureService.getFundNameForId(fundList,
                                            new Integer(row[1].toString())), amount);
                                else
                                    statement
                                    .get(index)
                                    .getFundWiseAmount()
                                    .put(incomeExpenditureService.getFundNameForId(fundList,
                                            new Integer(row[1].toString())),
                                            statement
                                            .get(index)
                                            .getFundWiseAmount()
                                            .get(incomeExpenditureService.getFundNameForId(fundList, new Integer(
                                                    row[1].toString()))).add(amount));
                            }
                        }
            }
            for (final ChartOfAccount s : entry.getValue().chartOfAccount)
                if (!statement.containsBalanceSheetEntry(s.glCode)) {
                    final StatementEntry statementEntry = new StatementEntry();
                    statementEntry.setGlCode(s.glCode);
                    statementEntry.setAccountName(s.name);
                    statement.add(statementEntry);
                }
        }
    }

    boolean ieContains(final List<Object[]> previousBalance, final String glCode) {
        for (final Object[] row : previousBalance)
            if (row[0] != null && glCode.equals(row[0].toString()))
                return true;
        return false;
    }

    private void populatePreviousYearTotalsForAllSchedules(final Statement statement, final String filterQuery,
            final Date toDate, final Date fromDate) {
        Date formattedToDate = null;
        final BigDecimal divisor = statement.getDivisor();
        if ("Yearly".equalsIgnoreCase(statement.getPeriod()))
            formattedToDate = fromDate;
        else
            formattedToDate = incomeExpenditureService.getPreviousYearFor(toDate);
        final List<Object[]> resultMap = amountPerFundQueryForAllSchedules(filterQuery, formattedToDate,
                incomeExpenditureService.getPreviousYearFor(fromDate), IE);
        final List<Object[]> allGlCodes = getAllGlCodesForAllSchedule(IE, "('I','E')");
        for (final Object[] obj : allGlCodes)
            for (final Object[] row : resultMap) {
                final String glCode = row[2].toString();
                if (glCode.substring(1, 3).equals(obj[0].toString())) {
                    final String type = obj[3].toString();
                    if (!statement.containsBalanceSheetEntry(row[2].toString()))
                        addRowToStatement(statement, row, glCode);
                    else
                        for (int index = 0; index < statement.size(); index++) {
                            BigDecimal amount = incomeExpenditureService.divideAndRound(((BigDecimal)row[0] ) , divisor);
                            if (I.equalsIgnoreCase(type))
                                amount = amount.multiply(NEGATIVE);
                            if (statement.get(index).getGlCode() != null
                                    && row[2].toString().equals(statement.get(index).getGlCode()))
                                statement.get(index).setPreviousYearTotal(amount);
                        }
                }
            }
    }

    void computeAndAddScheduleTotals(final Statement statement) {
        final Map<String, BigDecimal> fundNetTotals = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> fundPreTotals = new HashMap<String, BigDecimal>();
        BigDecimal netAmount = BigDecimal.ZERO;
        BigDecimal preAmount = BigDecimal.ZERO;
        for (final IEStatementEntry entry : statement.getIeEntries()) {

            for (final Entry<String, BigDecimal> row : entry.getNetAmount().entrySet()) {
                if (fundNetTotals.get(row.getKey()) == null)
                    fundNetTotals.put(row.getKey(), BigDecimal.ZERO);
                netAmount = zeroOrValue(row.getValue());
                fundNetTotals.put(row.getKey(), netAmount.add(zeroOrValue(fundNetTotals.get(row.getKey()))));
            }
            for (final Entry<String, BigDecimal> prerow : entry.getPreviousYearAmount().entrySet()) {
                if (fundPreTotals.get(prerow.getKey()) == null)
                    fundPreTotals.put(prerow.getKey(), BigDecimal.ZERO);
                preAmount = zeroOrValue(prerow.getValue());
                fundPreTotals.put(prerow.getKey(), preAmount.add(zeroOrValue(fundPreTotals.get(prerow.getKey()))));
            }
        }

        statement.addIE(new IEStatementEntry(null, "Total", fundNetTotals, fundPreTotals, true));
    }

    private BigDecimal zeroOrValue(final BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    public void setIncomeExpenditureService(final IncomeExpenditureService incomeExpenditureService) {
        this.incomeExpenditureService = incomeExpenditureService;
    }

}