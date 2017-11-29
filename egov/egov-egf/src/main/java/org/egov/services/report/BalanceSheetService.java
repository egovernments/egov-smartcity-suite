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


import org.egov.commons.CFinancialYear;
import org.egov.commons.Fund;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.egf.model.Statement;
import org.egov.egf.model.StatementEntry;
import org.egov.egf.model.StatementResultObject;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BalanceSheetService extends ReportService {
    private static final String BS = "BS";
    private static final String L = "L";
    private static final BigDecimal NEGATIVE = new BigDecimal(-1);
    private String removeEntrysWithZeroAmount = "";
   
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired
    private  FinancialYearHibernateDAO financialYearDAO;

    @Override
    protected void addRowsToStatement(final Statement balanceSheet, final Statement assets, final Statement liabilities) {
        if (liabilities.size() > 0) {
            balanceSheet.add(new StatementEntry(null, Constants.LIABILITIES, "", null, null, true));
            balanceSheet.addAll(liabilities);
            balanceSheet.add(new StatementEntry(null, Constants.TOTAL_LIABILITIES, "", null, null, true));
        }
        if (assets.size() > 0) {
            balanceSheet.add(new StatementEntry(null, Constants.ASSETS, "", null, null, true));
            balanceSheet.addAll(assets);
            balanceSheet.add(new StatementEntry(null, Constants.TOTAL_ASSETS, "", null, null, true));
        }
    }

    public void addCurrentOpeningBalancePerFund(final Statement balanceSheet, final List<Fund> fundList,
            final String transactionQuery) {
        final BigDecimal divisor = balanceSheet.getDivisor();
        final Query query = persistenceService.getSession()
                .createSQLQuery(
                        "select sum(openingdebitbalance)- sum(openingcreditbalance),ts.fundid,coa.majorcode,coa.type FROM transactionsummary ts,chartofaccounts coa  WHERE ts.glcodeid = coa.ID  AND ts.financialyearid="
                                + balanceSheet.getFinancialYear().getId()
                       
                                + transactionQuery
                                + " GROUP BY ts.fundid,coa.majorcode,coa.type");
        final List<Object[]> openingBalanceAmountList = query.list();
        for (final Object[] obj : openingBalanceAmountList)
            if (obj[0] != null && obj[1] != null) {
                BigDecimal total = (BigDecimal)obj[0];
                if (L.equals(obj[3].toString()))
                    total = total.multiply(NEGATIVE);
                for (final StatementEntry entry : balanceSheet.getEntries())
                    if (obj[2].toString().equals(entry.getGlCode()))
                        if (entry.getFundWiseAmount().isEmpty())
                            entry.getFundWiseAmount().put(getFundNameForId(fundList, new Integer(obj[1].toString())),
                                    divideAndRound(total, divisor));
                        else {
                            boolean shouldAddNewFund = true;
                            for (final Entry<String, BigDecimal> object : entry.getFundWiseAmount().entrySet())
                                if (object.getKey().equalsIgnoreCase(getFundNameForId(fundList, new Integer(obj[1].toString())))) {
                                    entry.getFundWiseAmount().put(object.getKey(),
                                            object.getValue().add(divideAndRound(total, divisor)));
                                    shouldAddNewFund = false;
                                }
                            if (shouldAddNewFund)
                                entry.getFundWiseAmount().put(getFundNameForId(fundList, new Integer(obj[1].toString())),
                                        divideAndRound(total, divisor));
                        }
            }
    }

    public void addOpeningBalancePrevYear(final Statement balanceSheet, final String transactionQuery, final Date fromDate) {
        try {
            final BigDecimal divisor = balanceSheet.getDivisor();
           final CFinancialYear prevFinancialYr = financialYearDAO.getPreviousFinancialYearByDate(fromDate);
            final String prevFinancialYearId = prevFinancialYr.getId().toString();
            final Query query = persistenceService.getSession()
                    .createSQLQuery(
                            "select sum(openingdebitbalance)- sum(openingcreditbalance),coa.majorcode,coa.type FROM transactionsummary ts,chartofaccounts coa  WHERE ts.glcodeid = coa.ID  AND ts.financialyearid="
                                    + prevFinancialYearId + transactionQuery + " GROUP BY coa.majorcode,coa.type");
            final List<Object[]> openingBalanceAmountList = query.list();
            for (final Object[] obj : openingBalanceAmountList)
                if (obj[0] != null && obj[1] != null) {
                  BigDecimal total =(BigDecimal) obj[0];
                    if (L.equals(obj[2].toString()))
                        total = total.multiply(NEGATIVE);
                    for (final StatementEntry entry : balanceSheet.getEntries())
                        if (obj[1].toString().equals(entry.getGlCode())) {
                            BigDecimal prevYrTotal = entry.getPreviousYearTotal();
                            prevYrTotal = prevYrTotal == null ? BigDecimal.ZERO : prevYrTotal;
                            entry.setPreviousYearTotal(prevYrTotal.add(divideAndRound(total, divisor)));
                        }
                }
        } catch (final Exception exp)
        {

        }
    }

    public void addExcessIEForCurrentYear(final Statement balanceSheet, final List<Fund> fundList,
            final String glCodeForExcessIE,
            final String filterQuery, final Date toDate, final Date fromDate) {
        final BigDecimal divisor = balanceSheet.getDivisor();
    String    voucherStatusToExclude = getAppConfigValueFor("EGF", "statusexcludeReport");
        StringBuffer qry = new StringBuffer(256);
        //TODO- We are only grouping by fund here. Instead here grouping should happen based on the filter like -department and Function also
        qry = qry.append("select sum(g.creditamount)-sum(g.debitamount),v.fundid from voucherheader v,");
        if (balanceSheet.getDepartment() != null && balanceSheet.getDepartment().getId() != -1)
            qry.append("VoucherMis mis ,");
        qry.append("generalledger g, chartofaccounts coa   where  v.ID=g.VOUCHERHEADERID and " +
                "v.status not in(" + voucherStatusToExclude + ") and  v.voucherdate>='" + getFormattedDate(fromDate)
                + "' and v.voucherdate<='" + getFormattedDate(toDate) + "'");
        if (balanceSheet.getDepartment() != null && balanceSheet.getDepartment().getId() != -1)
            qry.append(" and v.id= mis.voucherheaderid  and mis.departmentid= " + balanceSheet.getDepartment().getId());
        qry.append(" and coa.ID=g.glcodeid and coa.type in ('I','E') " + filterQuery + " group by v.fundid");
        final Query query = persistenceService.getSession().createSQLQuery(qry.toString());
        final List<Object[]> excessieAmountList = query.list();
        
        for (final StatementEntry entry : balanceSheet.getEntries())
            if (entry.getGlCode() != null && glCodeForExcessIE.equals(entry.getGlCode()))
                for (final Object[] obj : excessieAmountList){
                	
                    if (obj[0] != null && obj[1] != null) {
                        final String fundNameForId = getFundNameForId(fundList, Integer.valueOf(obj[1].toString()));
                        if (entry.getFundWiseAmount().containsKey(fundNameForId))
                            entry.getFundWiseAmount().put(
                                    fundNameForId,
                                    entry.getFundWiseAmount().get(fundNameForId)
                                    .add(divideAndRound((BigDecimal)obj[0], divisor)));
                        else
                            entry.getFundWiseAmount().put(fundNameForId, divideAndRound((BigDecimal)obj[0], divisor));
                    }}
    }

    public void addExcessIEForPreviousYear(final Statement balanceSheet, final List<Fund> fundList,
            final String glCodeForExcessIE,
            final String filterQuery, final Date toDate, final Date fromDate) {
        final BigDecimal divisor = balanceSheet.getDivisor();
        BigDecimal sum = BigDecimal.ZERO;
        String formattedToDate = "";
    String   voucherStatusToExclude = getAppConfigValueFor("EGF", "statusexcludeReport");
        if ("Yearly".equalsIgnoreCase(balanceSheet.getPeriod()))
        {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(fromDate);
            cal.add(Calendar.DATE, -1);
            formattedToDate = getFormattedDate(cal.getTime());
        }
        else 
            formattedToDate = getFormattedDate(getPreviousYearFor(toDate));
        StringBuffer qry = new StringBuffer(256);
        qry = qry.append("		select sum(g.creditamount)-sum(g.debitamount),v.fundid  from voucherheader v,generalledger g, ");
        if (balanceSheet.getDepartment() != null && balanceSheet.getDepartment().getId() != -1)
            qry.append("  VoucherMis mis ,");
        qry.append(" chartofaccounts coa   where  v.ID=g.VOUCHERHEADERID and v.status not in(" + voucherStatusToExclude
                + ") and  " +
                "v.voucherdate>='" + getFormattedDate(getPreviousYearFor(fromDate)) + "' and v.voucherdate<='" + formattedToDate
                + "' and coa.ID=g.glcodeid ");
        if (balanceSheet.getDepartment() != null && balanceSheet.getDepartment().getId() != -1)
            qry.append(" and v.id= mis.voucherheaderid");

        qry.append(" and coa.type in ('I','E') " + filterQuery + " group by v.fundid,g.functionid");
        final Query query = persistenceService.getSession().createSQLQuery(qry.toString());
        final List<Object[]> excessieAmountList = query.list();
        for (final Object[] obj : excessieAmountList)
            sum = sum.add((BigDecimal) obj[0]);
        for (int index = 0; index < balanceSheet.size(); index++)
            if (balanceSheet.get(index).getGlCode() != null && glCodeForExcessIE.equals(balanceSheet.get(index).getGlCode())) {
                BigDecimal prevYrTotal = balanceSheet.get(index).getPreviousYearTotal();
                prevYrTotal = prevYrTotal == null ? BigDecimal.ZERO : prevYrTotal;
                balanceSheet.get(index).setPreviousYearTotal(prevYrTotal.add(divideAndRound(sum, divisor)));
            }
    }

    public void populateBalanceSheet(final Statement balanceSheet) {
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
        minorCodeLength = Integer.valueOf(getAppConfigValueFor(Constants.EGF, "coa_minorcode_length"));
        coaType.add('A');
        coaType.add('L');
        final Date fromDate = getFromDate(balanceSheet);
        final Date toDate = getToDate(balanceSheet);
        String   voucherStatusToExclude = getAppConfigValueFor("EGF", "statusexcludeReport");
        final List<Fund> fundList = balanceSheet.getFunds();
        final String filterQuery = getFilterQuery(balanceSheet);
        populateCurrentYearAmountPerFund(balanceSheet, fundList, filterQuery, toDate, fromDate, BS);
        populatePreviousYearTotals(balanceSheet, filterQuery, toDate, fromDate, BS, "'L','A'");
        addCurrentOpeningBalancePerFund(balanceSheet, fundList, getTransactionQuery(balanceSheet));
        addOpeningBalancePrevYear(balanceSheet, getTransactionQuery(balanceSheet), fromDate);
        final String glCodeForExcessIE = getGlcodeForPurposeCode(7);//purpose is ExcessIE
        addExcessIEForCurrentYear(balanceSheet, fundList, glCodeForExcessIE, filterQuery, toDate, fromDate);
        addExcessIEForPreviousYear(balanceSheet, fundList, glCodeForExcessIE, filterQuery, toDate, fromDate);
        computeCurrentYearTotals(balanceSheet, Constants.LIABILITIES, Constants.ASSETS);
        populateSchedule(balanceSheet, BS);
        removeFundsWithNoData(balanceSheet);
        groupBySubSchedule(balanceSheet);
        computeTotalAssetsAndLiabilities(balanceSheet);
        if (removeEntrysWithZeroAmount.equalsIgnoreCase("Yes"))
            removeEntrysWithZeroAmount(balanceSheet);
    }

    private void computeTotalAssetsAndLiabilities(final Statement balanceSheet) {
        BigDecimal currentYearTotal = BigDecimal.ZERO;
        BigDecimal previousYearTotal = BigDecimal.ZERO;
        for (int index = 0; index < balanceSheet.size(); index++) {
            if (Constants.TOTAL.equalsIgnoreCase(balanceSheet.get(index).getAccountName())
                    || Constants.LIABILITIES.equals(balanceSheet.get(index).getAccountName())
                    || Constants.ASSETS.equals(balanceSheet.get(index).getAccountName()))
                continue;
            if (Constants.TOTAL_LIABILITIES.equalsIgnoreCase(balanceSheet.get(index).getAccountName())
                    || Constants.TOTAL_ASSETS.equalsIgnoreCase(balanceSheet.get(index).getAccountName())) {
                balanceSheet.get(index).setCurrentYearTotal(currentYearTotal);
                currentYearTotal = BigDecimal.ZERO;
                balanceSheet.get(index).setPreviousYearTotal(previousYearTotal);
                previousYearTotal = BigDecimal.ZERO;
            } else {
                if (balanceSheet.get(index).getCurrentYearTotal() != null)
                    currentYearTotal = currentYearTotal.add(balanceSheet.get(index).getCurrentYearTotal());
                if (balanceSheet.get(index).getPreviousYearTotal() != null)
                    previousYearTotal = previousYearTotal.add(balanceSheet.get(index).getPreviousYearTotal());
            }
        }
    }

    private void groupBySubSchedule(final Statement balanceSheet) {
        final List<StatementEntry> list = new LinkedList<StatementEntry>();
        final Map<String, String> schedueNumberToNameMap = getSubSchedule(BS);
        final Set<String> grouped = new HashSet<String>();
        BigDecimal previousTotal = BigDecimal.ZERO;
        BigDecimal currentTotal = BigDecimal.ZERO;
        Map<String, BigDecimal> fundTotals = new HashMap<String, BigDecimal>();
        boolean isLastEntryAHeader = true;
        // this loop assumes entries are ordered by major codes and have implicit grouping
        for (final StatementEntry entry : balanceSheet.getEntries()) {
            if (!grouped.contains(schedueNumberToNameMap.get(entry.getScheduleNo()))) {
                // hack to take care of liabilities and asset rows
                if (!isLastEntryAHeader) {
                    final StatementEntry balanceSheetEntry = new StatementEntry(null, Constants.TOTAL, "", previousTotal,
                            currentTotal,
                            true);
                    balanceSheetEntry.setFundWiseAmount(fundTotals);
                    fundTotals = new HashMap<String, BigDecimal>();
                    list.add(balanceSheetEntry);
                }
                // the current schedule number is not grouped yet, we'll start grouping it now.
                // Before starting the group we have to add total row for the last group
                addTotalRowToPreviousGroup(list, schedueNumberToNameMap, entry);
                previousTotal = BigDecimal.ZERO;
                currentTotal = BigDecimal.ZERO;
                // now this is grouped, so add it to to grouped set
                grouped.add(schedueNumberToNameMap.get(entry.getScheduleNo()));
            }
            if (Constants.TOTAL_LIABILITIES.equalsIgnoreCase(entry.getAccountName())) {
                final StatementEntry balanceSheetEntry = new StatementEntry(null, Constants.TOTAL, "", previousTotal,
                        currentTotal,
                        true);
                balanceSheetEntry.setFundWiseAmount(fundTotals);
                fundTotals = new HashMap<String, BigDecimal>();
                list.add(balanceSheetEntry);
            }
            list.add(entry);
            addFundAmount(entry, fundTotals);
            previousTotal = previousTotal.add(zeroOrValue(entry.getPreviousYearTotal()));
            currentTotal = currentTotal.add(zeroOrValue(entry.getCurrentYearTotal()));
            isLastEntryAHeader = entry.getGlCode() == null;
            if (Constants.TOTAL_LIABILITIES.equalsIgnoreCase(entry.getAccountName())) {
                previousTotal = BigDecimal.ZERO;
                currentTotal = BigDecimal.ZERO;
            }
        }
        // add the total row for the last grouping
        final StatementEntry sheetEntry = new StatementEntry(null, Constants.TOTAL, "", previousTotal, currentTotal, true);
        sheetEntry.setFundWiseAmount(fundTotals);
        list.add(list.size() - 1, sheetEntry);
        balanceSheet.setEntries(list);
    }

    private void removeEntrysWithZeroAmount(final Statement balanceSheet) {
        final List<StatementEntry> list = new LinkedList<StatementEntry>();
        Boolean check;
        Map<String, BigDecimal> FundWiseAmount = new HashMap<String, BigDecimal>();
        for (final StatementEntry entry : balanceSheet.getEntries())
            if (entry.getGlCode() != null && !entry.getGlCode().equalsIgnoreCase("")) {
                FundWiseAmount = entry.getFundWiseAmount();
                if (FundWiseAmount != null) {
                    check = false;
                    for (final String keyGroup : FundWiseAmount.keySet())
                        if (!(entry.getPreviousYearTotal() != null
                        && FundWiseAmount.get(keyGroup).compareTo(BigDecimal.ZERO) == 0 && entry.getPreviousYearTotal()
                        .compareTo(BigDecimal.ZERO) == 0)) {
                            check = true;
                            break;
                        }
                    if (check.equals(true))
                        list.add(entry);
                } else
                    list.add(entry);
            } else
                list.add(entry);
        balanceSheet.setEntries(new LinkedList<StatementEntry>());
        balanceSheet.setEntries(list);
    }

    public void removeScheduleEntrysWithZeroAmount(final Statement balanceSheet) {
        final List<StatementEntry> list = new ArrayList<StatementEntry>();
        for (final StatementEntry entry : balanceSheet.getEntries())
            if (entry.getGlCode() != null && !entry.getGlCode().equalsIgnoreCase("")) {
                if (!(entry.getCurrentYearTotal() != null && entry.getPreviousYearTotal() != null
                        && entry.getCurrentYearTotal().compareTo(BigDecimal.ZERO) == 0 && entry.getPreviousYearTotal().compareTo(
                                BigDecimal.ZERO) == 0))
                    list.add(entry);
            } else
                list.add(entry);
        balanceSheet.setEntries(new LinkedList<StatementEntry>());
        balanceSheet.setEntries(list);
    }

    public void populateCurrentYearAmountPerFund(final Statement statement, final List<Fund> fundList, final String filterQuery,
            final Date toDate,
            final Date fromDate, final String scheduleReportType) {
        final Statement assets = new Statement();
        final Statement liabilities = new Statement();
        final BigDecimal divisor = statement.getDivisor();
        final List<StatementResultObject> allGlCodes = getAllGlCodesFor(scheduleReportType);
        final List<StatementResultObject> results = getTransactionAmount(filterQuery, toDate, fromDate, "'L','A'", "BS");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("row.getGlCode()--row.getFundId()--row.getAmount()--row.getBudgetAmount()");
        for (final StatementResultObject queryObject : allGlCodes) {
            if (queryObject.getGlCode() == null)
                queryObject.setGlCode("");
            final List<StatementResultObject> rows = getRowWithGlCode(results, queryObject.getGlCode());

            if (rows.isEmpty()) {
                if (queryObject.isLiability())
                    liabilities.add(new StatementEntry(queryObject.getGlCode(), queryObject.getScheduleName(), queryObject
                            .getScheduleNumber(), BigDecimal.ZERO, BigDecimal.ZERO, false));
                else
                    assets.add(new StatementEntry(queryObject.getGlCode(), queryObject.getScheduleName(), queryObject
                            .getScheduleNumber(), BigDecimal.ZERO, BigDecimal.ZERO, false));
            } else
                for (final StatementResultObject row : rows) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug(row.getGlCode() + "--" + row.getFundId() + "--" + row.getAmount() + "--"
                                + row.getBudgetAmount());
                    if (row.isLiability())
                        row.negateAmount();
                    if (liabilities.containsBalanceSheetEntry(row.getGlCode())
                            || assets.containsBalanceSheetEntry(row.getGlCode())) {
                        if (row.isLiability())
                            addFundAmount(fundList, liabilities, divisor, row);
                        else
                            addFundAmount(fundList, assets, divisor, row);
                    } else {
                        final StatementEntry balanceSheetEntry = new StatementEntry();
                        if (row.getAmount() != null && row.getFundId() != null)
                            balanceSheetEntry.getFundWiseAmount().put(
                                    getFundNameForId(fundList, Integer.valueOf(row.getFundId())),
                                    divideAndRound(row.getAmount(), divisor));
                        if (queryObject.getGlCode() != null) {
                            balanceSheetEntry.setGlCode(queryObject.getGlCode());
                            balanceSheetEntry.setAccountName(queryObject.getScheduleName());
                            balanceSheetEntry.setScheduleNo(queryObject.getScheduleNumber());
                        }

                        if (row.isLiability())
                            liabilities.add(balanceSheetEntry);
                        else
                            assets.add(balanceSheetEntry);
                    }
                }
        }
        addRowsToStatement(statement, assets, liabilities);
    }

    public void populatePreviousYearTotals(final Statement balanceSheet, final String filterQuery, final Date toDate,
            final Date fromDate,
            final String reportSubType, final String coaType) {
        final boolean newbalanceSheet = balanceSheet.size() > 2 ? false : true;
        final BigDecimal divisor = balanceSheet.getDivisor();
        final Statement assets = new Statement();
        final Statement liabilities = new Statement();
        Date formattedToDate;
        final Calendar cal = Calendar.getInstance();

        if ("Yearly".equalsIgnoreCase(balanceSheet.getPeriod()))
        {
            cal.setTime(fromDate);
            cal.add(Calendar.DATE, -1);
            formattedToDate = cal.getTime();
        }
        else
            formattedToDate = getPreviousYearFor(toDate);
        final List<StatementResultObject> results = getTransactionAmount(filterQuery, formattedToDate,
                getPreviousYearFor(fromDate),
                coaType, reportSubType);
        for (final StatementResultObject row : results)
            if (balanceSheet.containsBalanceSheetEntry(row.getGlCode())) {
                for (int index = 0; index < balanceSheet.size(); index++)
                    if (balanceSheet.get(index).getGlCode() != null
                    && row.getGlCode().equals(balanceSheet.get(index).getGlCode())) {
                        if (row.isLiability())
                            row.negateAmount();
                        BigDecimal prevYrTotal = balanceSheet.get(index).getPreviousYearTotal();
                        prevYrTotal = prevYrTotal == null ? BigDecimal.ZERO : prevYrTotal;
                        balanceSheet.get(index).setPreviousYearTotal(prevYrTotal.add(divideAndRound(row.getAmount(), divisor)));
                    }
            } else {
                if (row.isLiability())
                    row.negateAmount();
                final StatementEntry balanceSheetEntry = new StatementEntry();
                if (row.getAmount() != null && row.getFundId() != null) {
                    balanceSheetEntry.setPreviousYearTotal(divideAndRound(row.getAmount(), divisor));
                    balanceSheetEntry.setCurrentYearTotal(BigDecimal.ZERO);
                }
                if (row.getGlCode() != null)
                    balanceSheetEntry.setGlCode(row.getGlCode());
                if (row.isLiability())
                    liabilities.add(balanceSheetEntry);
                else
                    assets.add(balanceSheetEntry);
            }
        if (newbalanceSheet)
            addRowsToStatement(balanceSheet, assets, liabilities);
    }

    public String getRemoveEntrysWithZeroAmount() {
        return removeEntrysWithZeroAmount;
    }

    public void setRemoveEntrysWithZeroAmount(final String removeEntrysWithZeroAmount) {
        this.removeEntrysWithZeroAmount = removeEntrysWithZeroAmount;
    }

}