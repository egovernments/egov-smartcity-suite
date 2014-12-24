package org.egov.services.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.egov.utils.Constants;
import org.egov.web.actions.report.Statement;
import org.egov.web.actions.report.StatementEntry;
import org.egov.web.actions.report.StatementResultObject;
import org.hibernate.Query;

public class IncomeExpenditureService extends ReportService{
	private static final String I = "I";
	private static final String IE = "IE";
	private static final BigDecimal NEGATIVE = new BigDecimal(-1);

	protected void addRowsToStatement(Statement balanceSheet,Statement assets, Statement liabilities) {
		if(liabilities.size()>0){
			balanceSheet.add(new StatementEntry(null,Constants.INCOME,"",null,null,true));
			balanceSheet.addAll(liabilities);
		}
		if(assets.size()>0){
			balanceSheet.add(new StatementEntry(null,Constants.EXPENDITURE,"",null,null,true));
			balanceSheet.addAll(assets);
		}
	}
	
	public void addCurrentOpeningBalancePerFund(Statement statement,String transactionQuery) {
		BigDecimal divisor = statement.getDivisor();
		Query query = getSession().createSQLQuery("select sum(openingdebitbalance)- sum(openingcreditbalance),ts.fundid,coa.majorcode,coa.type FROM transactionsummary ts,chartofaccounts coa  WHERE ts.glcodeid = coa.ID  AND ts.financialyearid="+statement.getFinancialYear().getId()+transactionQuery+" GROUP BY ts.fundid,coa.majorcode,coa.type");
		List<Object[]> openingBalanceAmountList = query.list();
		for(Object[] obj :openingBalanceAmountList){
			if(obj[0]!=null && obj[1]!=null){
				BigDecimal total = (BigDecimal)obj[0];
				if(I.equals(obj[3].toString())){
					total = total.multiply(NEGATIVE);
				}
				for (StatementEntry entry : statement.getEntries()) {
					if(obj[2].toString().equals(entry.getGlCode())){
						if(entry.getFundWiseAmount().isEmpty()){
							entry.getFundWiseAmount().put(getFundNameForId(statement.getFunds(), new Integer(obj[1].toString())), divideAndRound(total, divisor));
						}else{
							for (Entry<String, BigDecimal> object : entry.getFundWiseAmount().entrySet()) {
								if(object.getKey().equalsIgnoreCase(getFundNameForId(statement.getFunds(), new Integer(obj[1].toString())))){
									entry.getFundWiseAmount().put(object.getKey(), object.getValue().add(divideAndRound(total, divisor)));
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void populateIEStatement(Statement ie){
		minorCodeLength = Integer.valueOf(getAppConfigValueFor(Constants.EGF,"coa_minorcode_length"));
		coaType.add('I');
		coaType.add('E');
		Date fromDate = getFromDate(ie);
		Date toDate = getToDate(ie);
		String filterQuery = getFilterQuery(ie);
		populateCurrentYearAmountPerFund(ie,filterQuery,toDate,fromDate,IE);
		populatePreviousYearTotals(ie,filterQuery,toDate,fromDate,IE,"'I','E'");
		addCurrentOpeningBalancePerFund(ie,getTransactionQuery(ie));
		computeCurrentYearTotals(ie,Constants.INCOME,Constants.EXPENDITURE);
		populateSchedule(ie,IE);
		removeFundsWithNoData(ie);
		groupBySubSchedule(ie);
	}
	
	public void populateCurrentYearAmountPerFund(Statement statement,String filterQuery,Date toDate,Date fromDate,String scheduleReportType){
		Statement expenditure = new Statement();
		Statement income = new Statement();
		BigDecimal divisor = statement.getDivisor();
		List<StatementResultObject> allGlCodes = getAllGlCodesFor(scheduleReportType);
		List<StatementResultObject> results = getTransactionAmount(filterQuery, toDate, fromDate,"'I','E'",IE);
		for(StatementResultObject queryObject :allGlCodes){
			if(queryObject.getGlCode() == null)
				queryObject.setGlCode("");
			List<StatementResultObject> rows = getRowWithGlCode(results,queryObject.getGlCode());
			if(rows.isEmpty()) {
				if(I.equalsIgnoreCase(queryObject.getType().toString()))
					income.add(new StatementEntry(queryObject.getGlCode(),queryObject.getScheduleName(),queryObject.getScheduleNumber(),BigDecimal.ZERO,BigDecimal.ZERO,false));
				else
					expenditure.add(new StatementEntry(queryObject.getGlCode(),queryObject.getScheduleName(),queryObject.getScheduleNumber(),BigDecimal.ZERO,BigDecimal.ZERO,false));
			}else{
				for (StatementResultObject row : rows) {
					if(row.isIncome()){
						row.negateAmount();
					}
					if(income.containsBalanceSheetEntry(row.getGlCode()) || expenditure.containsBalanceSheetEntry(row.getGlCode())){
						if(I.equalsIgnoreCase(row.getType().toString())){
							addFundAmount(statement.getFunds(), income, divisor, row);
						}
						else
							addFundAmount(statement.getFunds(), expenditure, divisor, row);
					}else{
						StatementEntry entry = new StatementEntry();
						if(row.getAmount()!=null && row.getFundId()!=null){
							entry.getFundWiseAmount().put(getFundNameForId(statement.getFunds(),Integer.valueOf(row.getFundId())), divideAndRound(row.getAmount(), divisor));
						}
						if(queryObject.getGlCode()!=null){
							entry.setGlCode(queryObject.getGlCode());
							entry.setAccountName(queryObject.getScheduleName());
							entry.setScheduleNo(queryObject.getScheduleNumber());
						}
						if(I.equalsIgnoreCase(row.getType().toString()))
							income.add(entry);
						else
							expenditure.add(entry);
					}
				}
			}
		}
		addRowsToStatement(statement, expenditure, income);
	}

	private void groupBySubSchedule(Statement balanceSheet) {
		List<StatementEntry> list = new LinkedList<StatementEntry>();
		Map<String,String> schedueNumberToNameMap=getSubSchedule(IE);
		Map<String,String> addLessCodes = populateAddLessCodes();
		Set<String> grouped = new HashSet<String>();
		BigDecimal previousTotal = BigDecimal.ZERO;
		BigDecimal currentTotal = BigDecimal.ZERO;
		BigDecimal totalPreviousIncome = BigDecimal.ZERO;
		BigDecimal totalPreviousExpenditure = BigDecimal.ZERO;
		BigDecimal totalCurrentIncome = BigDecimal.ZERO;
		BigDecimal totalCurrentExpenditure = BigDecimal.ZERO;
		BigDecimal previousTotalForAddLessCodes = BigDecimal.ZERO;
		BigDecimal currentTotalForAddLessCodes = BigDecimal.ZERO;
		Map<String,BigDecimal> fundTotals = new HashMap<String, BigDecimal>();
		Map<String,BigDecimal> incomeFundTotals = new HashMap<String, BigDecimal>();
		Map<String,BigDecimal> expenditureFundTotals = new HashMap<String, BigDecimal>();
		List<StatementEntry> extraRowsToBeAdded = new ArrayList<StatementEntry>();
		//this loop assumes entries are ordered by major codes and have implicit grouping
		for (StatementEntry entry : balanceSheet.getEntries()) {
			if(addLessCodes.get(entry.getGlCode()) == null){
				if(!grouped.contains(schedueNumberToNameMap.get(entry.getScheduleNo()))){
					//the current schedule number is not grouped yet, we'll start grouping it now.
					//Before starting the group we have to add total row for the last group
					addTotalRowToPreviousGroup(list, schedueNumberToNameMap, entry);
					previousTotal = BigDecimal.ZERO;
					currentTotal = BigDecimal.ZERO;
					//now this is grouped, so add it to to grouped set
					grouped.add(schedueNumberToNameMap.get(entry.getScheduleNo()));
				}
				if(Constants.EXPENDITURE.equalsIgnoreCase(entry.getAccountName())){
					addTotalRow("A",Constants.TOTAL_INCOME,list, previousTotal, currentTotal, fundTotals);
					totalPreviousIncome = previousTotal;
					totalCurrentIncome = currentTotal;
					incomeFundTotals = fundTotals;
					fundTotals = new HashMap<String, BigDecimal>();
				}
				list.add(entry);
				addFundAmount(entry,fundTotals);
				previousTotal = previousTotal.add(zeroOrValue(entry.getPreviousYearTotal()));
				currentTotal = currentTotal.add(zeroOrValue(entry.getCurrentYearTotal()));
				if(Constants.EXPENDITURE.equalsIgnoreCase(entry.getAccountName())){
					previousTotal = BigDecimal.ZERO;
					currentTotal = BigDecimal.ZERO;
				}
			}else{
				extraRowsToBeAdded.add(entry);
			}
		}
		addTotalRow("B",Constants.TOTAL_EXPENDITURE,list, previousTotal, currentTotal, fundTotals);
		totalPreviousExpenditure = previousTotal;
		totalCurrentExpenditure = currentTotal;
		expenditureFundTotals = fundTotals;
		addIncomeOverExpenseDetails(list, addLessCodes, totalPreviousIncome,totalPreviousExpenditure, totalCurrentIncome,
				totalCurrentExpenditure, previousTotalForAddLessCodes,currentTotalForAddLessCodes, computeFundTotals(incomeFundTotals,expenditureFundTotals), extraRowsToBeAdded);
		
		balanceSheet.setEntries(list);
	}

	private Map<String, BigDecimal> computeFundTotals(Map<String, BigDecimal> incomeFundTotals,Map<String, BigDecimal> expenditureFundTotals) {
		Map<String, BigDecimal> total = new HashMap<String, BigDecimal>();
		for (Entry<String, BigDecimal> row : incomeFundTotals.entrySet()) {
			BigDecimal amount = zeroOrValue(row.getValue());
			total.put(row.getKey(), amount.subtract(zeroOrValue(expenditureFundTotals.get(row.getKey()))));
		}
		return total;
	}

	private void addTotalRow(String glCode,String accountName,List<StatementEntry> list,BigDecimal previousTotal, BigDecimal currentTotal,
			Map<String, BigDecimal> fundTotals) {
		StatementEntry sheetEntry = new StatementEntry(glCode,accountName,"",previousTotal,currentTotal,true);
		sheetEntry.setFundWiseAmount(fundTotals);
		list.add(list.size(), sheetEntry);
	}

	private void addIncomeOverExpenseDetails(List<StatementEntry> list,Map<String, String> addLessCodes, BigDecimal totalPreviousIncome,
			BigDecimal totalPreviousExpenditure, BigDecimal totalCurrentIncome,
			BigDecimal totalCurrentExpenditure,BigDecimal previousTotalForAddLessCodes,
			BigDecimal currentTotalForAddLessCodes,Map<String, BigDecimal> fundTotals,List<StatementEntry> extraRowsToBeAdded) {
		Map<String,BigDecimal> fundTotalsForEachEntry = new HashMap<String, BigDecimal>();
		BigDecimal netPreviousTotal = totalPreviousIncome.subtract(totalPreviousExpenditure);
		BigDecimal netCurrentTotal = totalCurrentIncome.subtract(totalCurrentExpenditure);
		StatementEntry incomeOverExpenditure = new StatementEntry("A-B","Income Over Expenditure","",netPreviousTotal,netCurrentTotal,true);
		incomeOverExpenditure.setFundWiseAmount(fundTotals);
		list.add(list.size(), incomeOverExpenditure);
		
		for (StatementEntry entry : extraRowsToBeAdded) {
			if(addLessCodes.get(entry.getGlCode()) != null){
				entry.setAccountName(addLessCodes.get(entry.getGlCode()).concat(":").concat(entry.getAccountName()));
				list.add(entry);
				previousTotalForAddLessCodes = previousTotalForAddLessCodes.add(zeroOrValue(entry.getPreviousYearTotal()));
				currentTotalForAddLessCodes = currentTotalForAddLessCodes.add(zeroOrValue(entry.getCurrentYearTotal()));
				addFundAmount(entry,fundTotalsForEachEntry);
			}
		}
		StatementEntry net = new StatementEntry(null,"Net balance","",netPreviousTotal.subtract(previousTotalForAddLessCodes)
				,netCurrentTotal.subtract(currentTotalForAddLessCodes),true);
		for (Entry<String, BigDecimal> row : fundTotals.entrySet()) {
			BigDecimal amount = zeroOrValue(row.getValue());
			fundTotalsForEachEntry.put(row.getKey(), amount.subtract(zeroOrValue(fundTotalsForEachEntry.get(row.getKey()))));
		}
		net.setFundWiseAmount(fundTotalsForEachEntry);
		list.add(list.size(),net);
	}

	private Map<String,String> populateAddLessCodes() {
		Map<String,String> addLessCodes = new HashMap<String,String>();
		String value = getAppConfigValueFor(Constants.EGF,"add_less_codes_for_ie_report");
		String[] list = value.split(",");
		for (int i = 0; i < list.length; i++) {
			String[] data = list[i].split("-");
			addLessCodes.put(data[0], data[1]);
		}
		return addLessCodes;
	}

	public void populatePreviousYearTotals(Statement statement,String filterQuery,Date toDate,Date fromDate,String reportSubType,String coaType){
		boolean newbalanceSheet = statement.size()>2?false:true;
		BigDecimal divisor = statement.getDivisor();
		Statement expenditure = new Statement();
		Statement income = new Statement();
		Date formattedToDate;
		if("Yearly".equalsIgnoreCase(statement.getPeriod()))
			formattedToDate = fromDate;
		else
			formattedToDate = getPreviousYearFor(toDate);
		List<StatementResultObject> results = getTransactionAmount(filterQuery, formattedToDate, getPreviousYearFor(fromDate),coaType,reportSubType);
		for(StatementResultObject row :results){
			if(statement.containsBalanceSheetEntry(row.getGlCode())){
				for(int index=0;index< statement.size();index++) {
					if(statement.get(index).getGlCode() != null && row.getGlCode().equals(statement.get(index).getGlCode())) {
						if(row.isIncome())
							row.negateAmount();
						statement.get(index).setPreviousYearTotal(divideAndRound(row.getAmount(), divisor));
					}
				}
			}else{
				if(row.isIncome())
					row.negateAmount();
				StatementEntry entry = new StatementEntry();
				if(row.getAmount()!=null && row.getFundId()!=null){
					entry.setPreviousYearTotal(divideAndRound(row.getAmount(), divisor));
					entry.setCurrentYearTotal(BigDecimal.ZERO);
				}
				if(row.getGlCode()!=null){
					entry.setGlCode(row.getGlCode());
				}
				if(row.isIncome())
					income.add(entry);
				else
					expenditure.add(entry);
			}
		}
		if(newbalanceSheet){
			addRowsToStatement(statement, expenditure, income);
		}
	}

}
