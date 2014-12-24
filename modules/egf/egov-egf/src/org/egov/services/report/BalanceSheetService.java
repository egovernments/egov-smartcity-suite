package org.egov.services.report;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.egov.commons.Fund;
import org.egov.utils.Constants;
import org.egov.web.actions.report.Statement;
import org.egov.web.actions.report.StatementEntry;
import org.egov.web.actions.report.StatementResultObject;
import org.hibernate.Query;

public class BalanceSheetService extends ReportService{
	private static final String BS = "BS";
	private static final String L = "L";
	private static final BigDecimal NEGATIVE = new BigDecimal(-1);

	protected void addRowsToStatement(Statement balanceSheet,Statement assets, Statement liabilities) {
		if(liabilities.size()>0){
			balanceSheet.add(new StatementEntry(null,Constants.LIABILITIES,"",null,null,true));
			balanceSheet.addAll(liabilities);
			balanceSheet.add(new StatementEntry(null,Constants.TOTAL_LIABILITIES,"",null,null,true));
		}
		if(assets.size()>0){
			balanceSheet.add(new StatementEntry(null,Constants.ASSETS,"",null,null,true));
			balanceSheet.addAll(assets);
			balanceSheet.add(new StatementEntry(null,Constants.TOTAL_ASSETS,"",null,null,true));
		}
	}
	
	public void addCurrentOpeningBalancePerFund(Statement balanceSheet,List<Fund> fundList,String transactionQuery) {
		BigDecimal divisor = balanceSheet.getDivisor();
		Query query = getSession().createSQLQuery("select sum(openingdebitbalance)- sum(openingcreditbalance),ts.fundid,coa.majorcode,coa.type FROM transactionsummary ts,chartofaccounts coa  WHERE ts.glcodeid = coa.ID  AND ts.financialyearid="+balanceSheet.getFinancialYear().getId()+transactionQuery+" GROUP BY ts.fundid,coa.majorcode,coa.type");
		List<Object[]> openingBalanceAmountList = query.list();
		for(Object[] obj :openingBalanceAmountList){
			if(obj[0]!=null && obj[1]!=null){
				BigDecimal total = (BigDecimal)obj[0];
				if(L.equals(obj[3].toString())){
					total = total.multiply(NEGATIVE);
				}
				for (StatementEntry entry : balanceSheet.getEntries()) {
					if(obj[2].toString().equals(entry.getGlCode())){
						if(entry.getFundWiseAmount().isEmpty()){
							entry.getFundWiseAmount().put(getFundNameForId(fundList, new Integer(obj[1].toString())), divideAndRound(total, divisor));
						}else{
							for (Entry<String, BigDecimal> object : entry.getFundWiseAmount().entrySet()) {
								if(object.getKey().equalsIgnoreCase(getFundNameForId(fundList, new Integer(obj[1].toString())))){
									entry.getFundWiseAmount().put(object.getKey(), object.getValue().add(divideAndRound(total, divisor)));
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void addExcessIEForCurrentYear(Statement balanceSheet,List<Fund> fundList, String glCodeForExcessIE,String filterQuery,Date toDate,Date fromDate) {
		BigDecimal divisor = balanceSheet.getDivisor();
		Query query = getSession().createSQLQuery("select sum(g.creditamount)-sum(g.debitamount),vh.fundid from voucherheader vh," +
				"generalledger g, chartofaccounts coa  ,vouchermis mis where vh.id=mis.voucherheaderid and vh.ID=g.VOUCHERHEADERID and " +
				"vh.status not in("+voucherStatusToExclude+") and  vh.voucherdate>='"+getFormattedDate(fromDate)+"' and vh.voucherdate<='"+getFormattedDate(toDate)+
				"' and coa.ID=g.glcodeid and coa.type in ('I','E') "+filterQuery+" group by vh.fundid");
		List<Object[]> excessieAmountList = query.list();
		for (StatementEntry entry : balanceSheet.getEntries()) {
			if(entry.getGlCode()!=null && glCodeForExcessIE.equals(entry.getGlCode())){
				for(Object[] obj :excessieAmountList){
					if(obj[0]!=null && obj[1]!=null){
						String fundNameForId = getFundNameForId(fundList, Integer.valueOf(obj[1].toString()));
						if(entry.getFundWiseAmount().containsKey(fundNameForId))
							entry.getFundWiseAmount().put(fundNameForId, entry.getFundWiseAmount().get(fundNameForId).add(divideAndRound((BigDecimal)obj[0], divisor)));							
						else
							entry.getFundWiseAmount().put(fundNameForId, divideAndRound((BigDecimal)obj[0], divisor));
					}
				}
			}
		}
	}
	
	public void addExcessIEForPreviousYear(Statement balanceSheet,List<Fund> fundList, String glCodeForExcessIE,String filterQuery,Date toDate,Date fromDate) {
		BigDecimal divisor = balanceSheet.getDivisor();
		BigDecimal sum = BigDecimal.ZERO;
		String formattedToDate = "";
		if("Yearly".equalsIgnoreCase(balanceSheet.getPeriod()))
			formattedToDate = getFormattedDate(fromDate);
		else
			formattedToDate = getFormattedDate(getPreviousYearFor(toDate));
		Query query = getSession().createSQLQuery("select sum(g.creditamount)-sum(g.debitamount),vh.fundid  from voucherheader vh,generalledger g, " +
				"chartofaccounts coa  ,vouchermis mis where vh.id=mis.voucherheaderid and vh.ID=g.VOUCHERHEADERID and vh.status not in("+voucherStatusToExclude+") and  " +
				"vh.voucherdate>='"+getFormattedDate(getPreviousYearFor(fromDate))+"' and vh.voucherdate<='"+formattedToDate+"' and coa.ID=g.glcodeid " +
						"and coa.type in ('I','E') "+filterQuery+" group by vh.fundid");
		List<Object[]> excessieAmountList = query.list();
		for(Object[] obj :excessieAmountList){
			sum = sum.add((BigDecimal) obj[0]);
		}
		for(int index=0;index<balanceSheet.size();index++){
			if(balanceSheet.get(index).getGlCode()!=null && glCodeForExcessIE.equals(balanceSheet.get(index).getGlCode())){
				balanceSheet.get(index).setPreviousYearTotal(balanceSheet.get(index).getPreviousYearTotal().add(divideAndRound(sum, divisor)));
			}
		}
	}

	public void populateBalanceSheet(Statement balanceSheet){
		minorCodeLength = Integer.valueOf(getAppConfigValueFor(Constants.EGF,"coa_minorcode_length"));
		coaType.add('A');
		coaType.add('L');
		Date fromDate = getFromDate(balanceSheet);
		Date toDate = getToDate(balanceSheet);
		voucherStatusToExclude = getAppConfigValueFor("finance", "statusexcludeReport");
		List<Fund> fundList = balanceSheet.getFunds();
		String filterQuery = getFilterQuery(balanceSheet);
		populateCurrentYearAmountPerFund(balanceSheet,fundList,filterQuery,toDate,fromDate,BS);
		populatePreviousYearTotals(balanceSheet,filterQuery,toDate,fromDate,BS,"'L','A'");
		addCurrentOpeningBalancePerFund(balanceSheet, fundList,getTransactionQuery(balanceSheet));
		String glCodeForExcessIE = getGlcodeForPurposeCode(7);
		addExcessIEForCurrentYear(balanceSheet, fundList,glCodeForExcessIE,filterQuery,toDate,fromDate);
		addExcessIEForPreviousYear(balanceSheet, fundList,glCodeForExcessIE,filterQuery,toDate,fromDate);
		computeCurrentYearTotals(balanceSheet,Constants.LIABILITIES,Constants.ASSETS);
		populateSchedule(balanceSheet,BS);
		removeFundsWithNoData(balanceSheet);
		groupBySubSchedule(balanceSheet);
		computeTotalAssetsAndLiabilities(balanceSheet);
	}
	
	private void computeTotalAssetsAndLiabilities(Statement balanceSheet) {
		BigDecimal currentYearTotal = BigDecimal.ZERO; 
		BigDecimal previousYearTotal = BigDecimal.ZERO; 
		for (int index=0;index<balanceSheet.size();index++) {
			if(Constants.TOTAL.equalsIgnoreCase(balanceSheet.get(index).getAccountName()) || Constants.LIABILITIES.equals(balanceSheet.get(index).getAccountName()) || Constants.ASSETS.equals(balanceSheet.get(index).getAccountName()))
				continue;
			if(Constants.TOTAL_LIABILITIES.equalsIgnoreCase(balanceSheet.get(index).getAccountName()) || Constants.TOTAL_ASSETS.equalsIgnoreCase(balanceSheet.get(index).getAccountName())){
				balanceSheet.get(index).setCurrentYearTotal(currentYearTotal);
				currentYearTotal = BigDecimal.ZERO; 
				balanceSheet.get(index).setPreviousYearTotal(previousYearTotal);
				previousYearTotal = BigDecimal.ZERO; 
			}else{
				if(balanceSheet.get(index).getCurrentYearTotal() !=null)
					currentYearTotal = currentYearTotal.add(balanceSheet.get(index).getCurrentYearTotal());
				if(balanceSheet.get(index).getPreviousYearTotal() !=null)
					previousYearTotal = previousYearTotal.add(balanceSheet.get(index).getPreviousYearTotal());
			}
		}
	}

	private void groupBySubSchedule(Statement balanceSheet) {
		List<StatementEntry> list = new LinkedList<StatementEntry>();
		Map<String,String> schedueNumberToNameMap=getSubSchedule(BS);
		Set<String> grouped = new HashSet<String>();
		BigDecimal previousTotal = BigDecimal.ZERO;
		BigDecimal currentTotal = BigDecimal.ZERO;
		Map<String,BigDecimal> fundTotals = new HashMap<String, BigDecimal>();
		boolean isLastEntryAHeader=true;
		//this loop assumes entries are ordered by major codes and have implicit grouping
		for (StatementEntry entry : balanceSheet.getEntries()) {
			if(!grouped.contains(schedueNumberToNameMap.get(entry.getScheduleNo()))){
				//hack to take care of liabilities and asset rows 
				if(!isLastEntryAHeader){
					StatementEntry balanceSheetEntry = new StatementEntry(null,Constants.TOTAL,"",previousTotal,currentTotal,true);
					balanceSheetEntry.setFundWiseAmount(fundTotals);
					fundTotals = new HashMap<String, BigDecimal>();
					list.add(balanceSheetEntry);
				}
				//the current schedule number is not grouped yet, we'll start grouping it now.
				//Before starting the group we have to add total row for the last group
				addTotalRowToPreviousGroup(list, schedueNumberToNameMap, entry);
				previousTotal = BigDecimal.ZERO;
				currentTotal = BigDecimal.ZERO;
				//now this is grouped, so add it to to grouped set
				grouped.add(schedueNumberToNameMap.get(entry.getScheduleNo()));
			}
			if(Constants.TOTAL_LIABILITIES.equalsIgnoreCase(entry.getAccountName())){
				StatementEntry balanceSheetEntry = new StatementEntry(null,Constants.TOTAL,"",previousTotal,currentTotal,true);
				balanceSheetEntry.setFundWiseAmount(fundTotals);
				fundTotals = new HashMap<String, BigDecimal>();
				list.add(balanceSheetEntry);
			}
			list.add(entry);
			addFundAmount(entry,fundTotals);
			previousTotal = previousTotal.add(zeroOrValue(entry.getPreviousYearTotal()));
			currentTotal = currentTotal.add(zeroOrValue(entry.getCurrentYearTotal()));
			isLastEntryAHeader=entry.getGlCode()==null;
			if(Constants.TOTAL_LIABILITIES.equalsIgnoreCase(entry.getAccountName())){
				previousTotal = BigDecimal.ZERO;
				currentTotal = BigDecimal.ZERO;
			}
		}
		//add the total row for the last grouping
		StatementEntry sheetEntry = new StatementEntry(null,Constants.TOTAL,"",previousTotal,currentTotal,true);
		sheetEntry.setFundWiseAmount(fundTotals);
		list.add(list.size()-1, sheetEntry);
		balanceSheet.setEntries(list);
	}
	
	public void populateCurrentYearAmountPerFund(Statement statement,List<Fund> fundList,String filterQuery,Date toDate,Date fromDate,String scheduleReportType){
		Statement assets = new Statement();
		Statement liabilities = new Statement();
		BigDecimal divisor = statement.getDivisor();
		List<StatementResultObject> allGlCodes = getAllGlCodesFor(scheduleReportType);
		List<StatementResultObject> results = getTransactionAmount(filterQuery, toDate, fromDate,"'L','A'","BS");
		for(StatementResultObject queryObject :allGlCodes){
			if(queryObject.getGlCode() == null)
				queryObject.setGlCode("");
			List<StatementResultObject> rows = getRowWithGlCode(results,queryObject.getGlCode());
			if(rows.isEmpty()) {
				if(queryObject.isLiability())
					liabilities.add(new StatementEntry(queryObject.getGlCode(),queryObject.getScheduleName(),queryObject.getScheduleNumber(),BigDecimal.ZERO,BigDecimal.ZERO,false));
				else
					assets.add(new StatementEntry(queryObject.getGlCode(),queryObject.getScheduleName(),queryObject.getScheduleNumber(),BigDecimal.ZERO,BigDecimal.ZERO,false));
			}else{
				for (StatementResultObject row : rows) {
					if(row.isLiability()){
						row.negateAmount();
					}
					if(liabilities.containsBalanceSheetEntry(row.getGlCode()) || assets.containsBalanceSheetEntry(row.getGlCode())){
						if(row.isLiability()){
							addFundAmount(fundList, liabilities, divisor, row);
						}
						else
							addFundAmount(fundList, assets, divisor, row);
					}else{
						StatementEntry balanceSheetEntry = new StatementEntry();
						if(row.getAmount()!=null && row.getFundId()!=null){
							balanceSheetEntry.getFundWiseAmount().put(getFundNameForId(fundList,Integer.valueOf(row.getFundId())), divideAndRound(row.getAmount(), divisor));
						}
						if(queryObject.getGlCode()!=null){
							balanceSheetEntry.setGlCode(queryObject.getGlCode());
							balanceSheetEntry.setAccountName(queryObject.getScheduleName());
							balanceSheetEntry.setScheduleNo(queryObject.getScheduleNumber());
						}
						if(row.isLiability())
							liabilities.add(balanceSheetEntry);
						else
							assets.add(balanceSheetEntry);
					}
				}
			}
		}
		addRowsToStatement(statement, assets, liabilities);
	}

	public void populatePreviousYearTotals(Statement balanceSheet,String filterQuery,Date toDate,Date fromDate,String reportSubType,String coaType){
		boolean newbalanceSheet = balanceSheet.size()>2?false:true;
		BigDecimal divisor = balanceSheet.getDivisor();
		Statement assets = new Statement();
		Statement liabilities = new Statement();
		Date formattedToDate;
		if("Yearly".equalsIgnoreCase(balanceSheet.getPeriod()))
			formattedToDate = fromDate;
		else
			formattedToDate = getPreviousYearFor(toDate);
		List<StatementResultObject> results = getTransactionAmount(filterQuery, formattedToDate, getPreviousYearFor(fromDate),coaType,reportSubType);
		for(StatementResultObject row :results){
			if(balanceSheet.containsBalanceSheetEntry(row.getGlCode())){
				for(int index=0;index< balanceSheet.size();index++) {
					if(balanceSheet.get(index).getGlCode() != null && row.getGlCode().equals(balanceSheet.get(index).getGlCode())) {
						if(row.isLiability())
							row.negateAmount();
						balanceSheet.get(index).setPreviousYearTotal(divideAndRound(row.getAmount(), divisor));
					}
				}
			}else{
				if(row.isLiability())
					row.negateAmount();
				StatementEntry balanceSheetEntry = new StatementEntry();
				if(row.getAmount()!=null && row.getFundId()!=null){
					balanceSheetEntry.setPreviousYearTotal(divideAndRound(row.getAmount(), divisor));
					balanceSheetEntry.setCurrentYearTotal(BigDecimal.ZERO);
				}
				if(row.getGlCode()!=null){
					balanceSheetEntry.setGlCode(row.getGlCode());
				}
				if(row.isLiability())
					liabilities.add(balanceSheetEntry);
				else
					assets.add(balanceSheetEntry);
			}
		}
		if(newbalanceSheet){
			addRowsToStatement(balanceSheet, assets, liabilities);
		}
	}

}
