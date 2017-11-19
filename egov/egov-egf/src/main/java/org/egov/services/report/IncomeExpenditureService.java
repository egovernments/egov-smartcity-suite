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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.egov.commons.Fund;
import org.egov.egf.model.IEStatementEntry;
import org.egov.egf.model.Statement;
import org.egov.egf.model.StatementResultObject;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.Constants;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class IncomeExpenditureService extends ReportService {
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;

    private static final String I = "I";
    private static final String E = "E";
    private static final String IE = "IE";
   // Date fromDate;
    //Date toDate;
    private static final BigDecimal NEGATIVE = new BigDecimal(-1);
    private FunctionwiseIEService functionwiseIEService;

    public FunctionwiseIEService getFunctionwiseIEService() {
        return functionwiseIEService;
    }

    public void setFunctionwiseIEService(final FunctionwiseIEService functionwiseIEService) {
        this.functionwiseIEService = functionwiseIEService;
    }

    @Override
    protected void addRowsToStatement(final Statement balanceSheet, final Statement assets, final Statement liabilities) {
        IEStatementEntry incomeEntry = new IEStatementEntry();
        IEStatementEntry expenseEntry = new IEStatementEntry();
        List<IEStatementEntry> totalIncomeOverExpense = new ArrayList<IEStatementEntry>();

        if (liabilities.sizeIE() > 0) {
            balanceSheet.addIE(new IEStatementEntry(null, Constants.INCOME, "", true));
            incomeEntry = getTotalIncomeFundwise(liabilities);
            balanceSheet.addAllIE(liabilities);
            balanceSheet.addIE(incomeEntry);
        }
        if (assets.sizeIE() > 0) {
            balanceSheet.addIE(new IEStatementEntry(null, Constants.EXPENDITURE, "", true));
            expenseEntry = getTotalExpenseFundwise(assets);
            balanceSheet.addAllIE(assets);
            balanceSheet.addIE(expenseEntry);
        }
        totalIncomeOverExpense = computeTotalsIncomeExpense(incomeEntry, expenseEntry);
        for (final IEStatementEntry exp : totalIncomeOverExpense)
            balanceSheet.addIE(exp);

    }

    public void populateIEStatement(Statement ie) {
        minorCodeLength = Integer.valueOf(getAppConfigValueFor(Constants.EGF, "coa_minorcode_length"));
        coaType.add('I');
        coaType.add('E');
        Date  fromDate = getFromDate(ie);
        Date  toDate = getToDate(ie);
        final String filterQuery = getFilterQuery(ie);
        populateCurrentYearAmountPerFund(ie, filterQuery, toDate, fromDate, IE);
        // populateSchedule(ie,IE);
        ie = addBudgetDetails(ie);
        removeFundsWithNoDataIE(ie);
    }

    private Statement addBudgetDetails(final Statement ie) {
        final List<StatementResultObject> budgetForMajorCodes = getBudgetForMajorCodes(ie);
        // if(LOGGER.isDebugEnabled())
        LOGGER.error("Budget Amounts.................................");
        print(budgetForMajorCodes);
        final List<StatementResultObject> budgetReappForMajorCodes = getBudgetReappMinorCodes(ie);
        // if(LOGGER.isDebugEnabled())
        LOGGER.error("Budget Reapp Amounts...........................");
        print(budgetReappForMajorCodes);
        BigDecimal totalBudget = BigDecimal.ZERO;
        for (final StatementResultObject ent : budgetForMajorCodes)
            for (final StatementResultObject stm : budgetReappForMajorCodes)
                if (ent.getGlCode() != null && ent.getGlCode().equalsIgnoreCase(stm.getGlCode()))
                    if (ent.getAmount() != null)
                    {
                        if (stm.getAmount() != null)
                            ent.setAmount(ent.getAmount().add(stm.getAmount()));
                    } else if (stm.getAmount() != null)
                        ent.setAmount(stm.getAmount());

        for (final IEStatementEntry ent : ie.getIeEntries())
            inner: for (final StatementResultObject stm : budgetForMajorCodes)
                if (ent.getGlCode() != null && ent.getGlCode().equalsIgnoreCase(stm.getGlCode()))
                {
                    ent.setBudgetAmount(stm.getAmount().setScale(2));
                    totalBudget = totalBudget.add(ent.getBudgetAmount());
                }

        for (final IEStatementEntry ent : ie.getIeEntries())
            if (ent.getAccountName() != null && ent.getAccountName().equalsIgnoreCase(Constants.TOTAL_EXPENDITURE))
                ent.setBudgetAmount(totalBudget);
        return ie;
    }

    private void print(final List<StatementResultObject> list) {

        for (final StatementResultObject stm : list)
            LOGGER.error(stm.getGlCode() + "         " + stm.getAmount());
    }

    // add previous year amount and current year amount. Opening balance is not added for IE codes
    public void populateCurrentYearAmountPerFund(final Statement statement, final String filterQuery, final Date toDate,
            final Date fromDate,
            final String scheduleReportType) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" inside populateCurrentYearAmountPerFund ");
        final BigDecimal divisor = statement.getDivisor();
        final Statement expenditure = new Statement();
        final Statement income = new Statement();
        final List<StatementResultObject> allGlCodes = getAllGlCodesFor(scheduleReportType);  // has all the IE schedule codes

        // get all the net amount total fundwise for each major code

        final List<StatementResultObject> results = getTransactionAmount(filterQuery, toDate, fromDate, "'I','E'", IE);

        final List<StatementResultObject> PreYearResults = getTransactionAmount(filterQuery, getPreviousYearFor(toDate),
                getPreviousYearFor(fromDate), "'I','E'", scheduleReportType);

        for (final StatementResultObject queryObject : allGlCodes) {

            if (queryObject.getGlCode() == null)
                queryObject.setGlCode("");
            final List<StatementResultObject> rows = getRowWithGlCode(results, queryObject.getGlCode());
            if (rows.isEmpty() && queryObject.getGlCode() != null) {
                if (contains(PreYearResults, queryObject.getGlCode())) {
                    final List<StatementResultObject> preRow = getRowWithGlCode(PreYearResults, queryObject.getGlCode());
                    final IEStatementEntry preentry = new IEStatementEntry();
                    for (final StatementResultObject pre : preRow)
                        if (I.equalsIgnoreCase(queryObject.getType().toString())) {
                            if (pre.isIncome())
                                pre.negateAmount();
                            preentry.getPreviousYearAmount().put(
                                    getFundNameForId(statement.getFunds(), Integer.valueOf(pre.getFundId())),
                                    divideAndRound(pre.getAmount(), divisor));
                        } else if (E.equalsIgnoreCase(queryObject.getType().toString())) {
                            if (pre.isIncome())
                                pre.negateAmount();
                            preentry.getPreviousYearAmount().put(
                                    getFundNameForId(statement.getFunds(), Integer.valueOf(pre.getFundId())),
                                    divideAndRound(pre.getAmount(), divisor));
                        }
                    if (queryObject.getGlCode() != null) {
                        preentry.setGlCode(queryObject.getGlCode());
                        preentry.setAccountName(queryObject.getScheduleName());
                        preentry.setScheduleNo(queryObject.getScheduleNumber());
                    }
                    if (I.equalsIgnoreCase(queryObject.getType().toString()))
                        income.addIE(preentry);
                    else if (E.equalsIgnoreCase(queryObject.getType().toString()))
                        expenditure.addIE(preentry);
                }
            } else
                for (final StatementResultObject row : rows) {
                    if (row.isIncome())
                        row.negateAmount();
                    if (income.containsIEStatementEntry(row.getGlCode()) || expenditure.containsIEStatementEntry(row.getGlCode())) {
                        if (I.equalsIgnoreCase(row.getType().toString()))
                            addFundAmountIE(statement.getFunds(), income, divisor, row);
                        else if (E.equalsIgnoreCase(row.getType().toString()))
                            addFundAmountIE(statement.getFunds(), expenditure, divisor, row);
                    } else {
                        final IEStatementEntry entry = new IEStatementEntry();
                        if (row.getAmount() != null && row.getFundId() != null) {
                            entry.getNetAmount().put(getFundNameForId(statement.getFunds(), Integer.valueOf(row.getFundId())),
                                    divideAndRound(row.getAmount(), divisor));
                            if (queryObject.getGlCode() != null && contains(PreYearResults, row.getGlCode())) {
                                final List<StatementResultObject> preRow = getRowWithGlCode(PreYearResults,
                                        queryObject.getGlCode());
                                for (final StatementResultObject pre : preRow) {
                                    if (pre.isIncome())
                                        pre.negateAmount();
                                    if (pre.getGlCode() != null && pre.getGlCode().equals(row.getGlCode()))
                                        entry.getPreviousYearAmount().put(
                                                getFundNameForId(statement.getFunds(), Integer.valueOf(pre.getFundId())),
                                                divideAndRound(pre.getAmount(), divisor));
                                }
                            }
                        }
                        if (queryObject.getGlCode() != null) {
                            entry.setGlCode(queryObject.getGlCode());
                            entry.setAccountName(queryObject.getScheduleName());
                            entry.setScheduleNo(queryObject.getScheduleNumber());
                        }
                        if (I.equalsIgnoreCase(row.getType().toString()))
                            income.addIE(entry);
                        else if (E.equalsIgnoreCase(row.getType().toString()))
                            expenditure.addIE(entry);
                    }
                }

        }
        addRowsToStatement(statement, expenditure, income);

    }

    /*
     * Computes income over expenditure and vise versa for current year amount and previous year amount
     */
    private List<IEStatementEntry> computeTotalsIncomeExpense(final IEStatementEntry incomeFundTotals,
            final IEStatementEntry expenditureFundTotals) {
        final Map<String, BigDecimal> netTotal = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> preTotal = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> netTotalin_ex = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> preTotalin_ex = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> netTotalex_in = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> preTotalex_in = new HashMap<String, BigDecimal>();
        Set<String> netFundSet = new HashSet<String>();
        Set<String> preFundSet = new HashSet<String>();
        BigDecimal curAmount = BigDecimal.ZERO;
        final String prevoius = "PREVIOUS";
        final String current = "CURRENT";
        netFundSet = getAllKey(incomeFundTotals, expenditureFundTotals, current);
        preFundSet = getAllKey(incomeFundTotals, expenditureFundTotals, prevoius);
        // Entry<String, BigDecimal> prerow;
        final IEStatementEntry income = new IEStatementEntry();
        final IEStatementEntry expense = new IEStatementEntry();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Calculating income over expenses");
        final List<IEStatementEntry> incomeOverExpenditure = new ArrayList<IEStatementEntry>();
        for (final String str : netFundSet)
            if (incomeFundTotals.getNetAmount().containsKey(str)) {
                final BigDecimal amount = zeroOrValue(incomeFundTotals.getNetAmount().get(str));
                netTotal.put(str, amount.subtract(zeroOrValue(expenditureFundTotals.getNetAmount().get(str))));
            } else if (expenditureFundTotals.getNetAmount().containsKey(str)
                    && !incomeFundTotals.getNetAmount().containsKey(str)) {
                final BigDecimal amount = zeroOrValue(incomeFundTotals.getNetAmount().get(str));
                netTotal.put(str, amount.subtract(zeroOrValue(expenditureFundTotals.getNetAmount().get(str))));
            }
        for (final String pstr : preFundSet)
            if (incomeFundTotals.getPreviousYearAmount().containsKey(pstr)) {
                final BigDecimal amount = zeroOrValue(incomeFundTotals.getPreviousYearAmount().get(pstr));
                preTotal.put(pstr, amount.subtract(zeroOrValue(expenditureFundTotals.getPreviousYearAmount().get(pstr))));
            } else if (expenditureFundTotals.getPreviousYearAmount().containsKey(pstr)
                    && !incomeFundTotals.getPreviousYearAmount().containsKey(pstr)) {
                zeroOrValue(incomeFundTotals.getPreviousYearAmount().get(pstr));
                preTotal.put(pstr, expenditureFundTotals.getPreviousYearAmount().get(pstr));
            }

        for (final String str : netFundSet) {
            final int isIncome = netTotal.get(str).signum();
            if (isIncome > 0) {
                netTotalin_ex.put(str, netTotal.get(str));
                income.setGlCode("A-B");
                income.setAccountName("Income Over Expenditure");
                income.setDisplayBold(true);
                income.setNetAmount(netTotalin_ex);
            } else {
                curAmount = zeroOrValue(netTotal.get(str)).negate();
                netTotalex_in.put(str, curAmount);
                expense.setGlCode("B-A");
                expense.setAccountName("Expenditure Over Income");
                expense.setNetAmount(netTotalex_in);
                expense.setDisplayBold(true);
            }

        }

        for (final String str : preFundSet) {
            final int isIncome = preTotal.get(str).signum();
            if (isIncome > 0) {
                if (income.getGlCode() != null) {
                    preTotalin_ex.put(str, preTotal.get(str));
                    income.setPreviousYearAmount(preTotalin_ex);
                } else {
                    preTotalin_ex.put(str, preTotal.get(str));
                    income.setPreviousYearAmount(preTotalin_ex);
                    income.setGlCode("A-B");
                    income.setAccountName("Income Over Expenditure");
                    income.setDisplayBold(true);
                    preTotalin_ex.put(str, preTotal.get(str));
                    income.setPreviousYearAmount(preTotalin_ex);
                }
            } else if (expense.getGlCode() != null) {
                preTotalex_in.put(str, preTotal.get(str).negate());
                expense.setPreviousYearAmount(preTotalex_in);
            } else {
                curAmount = zeroOrValue(preTotal.get(str)).negate();
                preTotalex_in.put(str, curAmount);
                expense.setGlCode("B-A");
                expense.setAccountName("Expenditure Over Income");
                expense.setDisplayBold(true);
                preTotalex_in.put(str, curAmount);
                expense.setPreviousYearAmount(preTotalex_in);

            }
        }
        incomeOverExpenditure.add(income);
        incomeOverExpenditure.add(expense);
        return incomeOverExpenditure;
    }

    boolean contains(final List<StatementResultObject> result, final String glCode) {
        for (final StatementResultObject row : result)
            if (row.getGlCode() != null && row.getGlCode().equalsIgnoreCase(glCode))
                return true;
        return false;
    }

    void addFundPreviousAmountIE(final List<Fund> fundList, final Statement type, final BigDecimal divisor,
            final StatementResultObject row) {
        for (int index = 0; index < type.size(); index++) {
            final BigDecimal amount = divideAndRound(row.getAmount(), divisor);
            if (type.get(index).getGlCode() != null
                    && row.getGlCode().equals(type.get(index).getGlCode()))
                type.getIE(index).getPreviousYearAmount().put(
                        getFundNameForId(fundList, Integer.valueOf(row
                                .getFundId())), amount);
        }
    }

    /*
     * Calculate total Income of current year and previous year
     */
    private IEStatementEntry getTotalIncomeFundwise(final Statement income_expense) {
        final Map<String, BigDecimal> fundNetTotals = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> fundPreTotals = new HashMap<String, BigDecimal>();
        BigDecimal netAmount = BigDecimal.ZERO;
        BigDecimal preAmount = BigDecimal.ZERO;
        for (final IEStatementEntry entry : income_expense.getIeEntries()) {

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
        return new IEStatementEntry("A", Constants.TOTAL_INCOME, fundNetTotals, fundPreTotals, true);
    }

    /*
     * Calculate total Expenditure of current year and previous year
     */
    private IEStatementEntry getTotalExpenseFundwise(final Statement income_expense) {

        final Map<String, BigDecimal> fundNetTotals = new HashMap<String, BigDecimal>();
        final Map<String, BigDecimal> fundPreTotals = new HashMap<String, BigDecimal>();
        BigDecimal netAmount = BigDecimal.ZERO;
        BigDecimal preAmount = BigDecimal.ZERO;
        for (final IEStatementEntry entry : income_expense.getIeEntries()) {

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
        return new IEStatementEntry("B", Constants.TOTAL_EXPENDITURE, fundNetTotals, fundPreTotals, true);
    }

    /*
     * Returns All Fund id for which transaction is made previous year or this year
     */
    private HashSet<String> getAllKey(final IEStatementEntry incomeFundTotals, final IEStatementEntry expenditureFundTotals,
            final String amtType) {

        final Set<String> allFundSet = new HashSet<String>();
        if (amtType.equals("CURRENT")) {
            for (final Entry<String, BigDecimal> row : incomeFundTotals.getNetAmount().entrySet())
                allFundSet.add(row.getKey());
            for (final Entry<String, BigDecimal> row : expenditureFundTotals.getNetAmount().entrySet())
                allFundSet.add(row.getKey());
        } else {
            for (final Entry<String, BigDecimal> row : incomeFundTotals.getPreviousYearAmount().entrySet())
                allFundSet.add(row.getKey());
            for (final Entry<String, BigDecimal> row : expenditureFundTotals.getPreviousYearAmount().entrySet())
                allFundSet.add(row.getKey());
        }

        return (HashSet) allFundSet;

    }

    private List<StatementResultObject> getBudgetForMajorCodes(final Statement incomeExpenditureStatement) {

        final StringBuffer queryStr = new StringBuffer(1024);

        queryStr.append(" select coa.majorCode as glcode, sum(bd.approvedamount) as amount ");

        queryStr.append(" from egf_budgetdetail bd , egf_budgetgroup bg,egf_budget b, chartofaccounts coa, eg_wf_states wfs ");

        queryStr.append("where ((bg.maxcode<=coa.id and bg.mincode>=coa.id) or bg.majorcode=coa.id ) and bd.budgetgroup= bg.id "
                +
                " and bd.budget=b.id and  bd.state_id=wfs.id  and wfs.value='END'  and b.isbere=:isBeRe and b.financialyearid=:finYearId   ");
        if (incomeExpenditureStatement.getFund() != null && incomeExpenditureStatement.getFund().getId() != null
                && incomeExpenditureStatement.getFund().getId() != 0)
            queryStr.append(" and bd.fund=" + incomeExpenditureStatement.getFund().getId());
        if (incomeExpenditureStatement.getDepartment() != null && incomeExpenditureStatement.getDepartment().getId() != 0)
            queryStr.append(" and bd.executing_department=" + incomeExpenditureStatement.getDepartment().getId());
        if (incomeExpenditureStatement.getFunction() != null && incomeExpenditureStatement.getFunction().getId() != null
                && incomeExpenditureStatement.getFunction().getId() != 0)
            queryStr.append("  and bd.function= " + incomeExpenditureStatement.getFunction().getId());

        queryStr.append(" and coa.majorcode is not null  group by coa.majorCode ");

        queryStr.append(" order by 1");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("query is " + queryStr.toString());
        final SQLQuery budgteQuery = persistenceService.getSession().createSQLQuery(queryStr.toString());
        budgteQuery.addScalar("glCode").addScalar("amount")
        .setResultTransformer(Transformers.aliasToBean(StatementResultObject.class));
        budgteQuery.setLong("finYearId", incomeExpenditureStatement.getFinancialYear().getId())
        .setString("isBeRe", "RE");
        final List<StatementResultObject> list = budgteQuery.list();
        return list;

    }

    private List<StatementResultObject> getBudgetReappMinorCodes(final Statement incomeExpenditureStatement) {
        final StringBuffer queryStr = new StringBuffer(1024);

        queryStr.append(" select coa.majorcode as glCode, sum(bdr.addition_amount- bdr.deduction_amount) as amount ");

        queryStr.append(" from egf_budgetdetail bd , egf_budgetgroup bg,egf_budget b, chartofaccounts coa,eg_wf_states wfs,egf_budget_reappropriation bdr where ((bg.maxcode<=coa.id and bg.mincode>=coa.id) or bg.majorcode=coa.id ) and bd.budgetgroup= bg.id "
                +
                "  and bdr.budgetdetail=bd.id and bd.budget=b.id and bdr.state_id=wfs.id  and wfs.value='END' and b.isbere=:isBeRe and b.financialyearid=:finYearId  ");

        if (incomeExpenditureStatement.getFund() != null && incomeExpenditureStatement.getFund().getId() != null
                && incomeExpenditureStatement.getFund().getId() != 0)
            queryStr.append(" and bd.fund=" + incomeExpenditureStatement.getFund().getId());
        if (incomeExpenditureStatement.getDepartment() != null && incomeExpenditureStatement.getDepartment().getId() != 0)
            queryStr.append(" and bd.executing_department=" + incomeExpenditureStatement.getDepartment().getId());
        if (incomeExpenditureStatement.getFunction() != null && incomeExpenditureStatement.getFunction().getId() != null
                && incomeExpenditureStatement.getFunction().getId() != 0)
            queryStr.append("  and bd.function= " + incomeExpenditureStatement.getFunction().getId());
        queryStr.append("  group by coa.majorCode ");

        queryStr.append(" order by 1 asc");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("query is " + queryStr.toString());
        final SQLQuery budgteReappQuery = persistenceService.getSession().createSQLQuery(queryStr.toString());
        budgteReappQuery.addScalar("glCode").addScalar("amount")
        .setResultTransformer(Transformers.aliasToBean(StatementResultObject.class));
        budgteReappQuery.setLong("finYearId", incomeExpenditureStatement.getFinancialYear().getId())
        .setString("isBeRe", "RE");
        final List<StatementResultObject> list = budgteReappQuery.list();
        return list;
    }

    
}