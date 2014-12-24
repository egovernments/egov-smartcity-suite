package org.egov.services.report;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.Fund;
import org.egov.utils.Constants;
import org.egov.web.actions.report.Statement;
import org.egov.web.actions.report.StatementEntry;
import org.hibernate.Query;

public class BalanceSheetScheduleService extends ScheduleService{
	private static final String BS = "BS";
	private static final String L = "L";
	private BalanceSheetService balanceSheetService;
	
	public void setBalanceSheetService(BalanceSheetService balanceSheetService) {
		this.balanceSheetService = balanceSheetService;
	}

	public void populateDataForSchedule(Statement balanceSheet,String majorCode) {
		voucherStatusToExclude = getAppConfigValueFor("finance", "statusexcludeReport");
		minorCodeLength = Integer.valueOf(balanceSheetService.getAppConfigValueFor(Constants.EGF,"coa_minorcode_length"));
		majorCodeLength = Integer.valueOf(balanceSheetService.getAppConfigValueFor(Constants.EGF,"coa_majorcode_length"));
		Date fromDate = balanceSheetService.getFromDate(balanceSheet);
		Date toDate = balanceSheetService.getToDate(balanceSheet);
		CChartOfAccounts coa = (CChartOfAccounts) find("from CChartOfAccounts where glcode=?", majorCode);
		List<Fund> fundList = balanceSheet.getFunds();
		populateCurrentYearAmountForSchedule(balanceSheet,fundList,balanceSheetService.getFilterQuery(balanceSheet),toDate,fromDate,majorCode,coa.getType());
		addCurrentOpeningBalancePerFund(balanceSheet, fundList,balanceSheetService.getTransactionQuery(balanceSheet));
		populatePreviousYearTotalsForSchedule(balanceSheet,balanceSheetService.getFilterQuery(balanceSheet),toDate,fromDate,majorCode,coa.getType());
		balanceSheetService.addExcessIEForCurrentYear(balanceSheet, fundList, getGlcodeForPurposeCode7(), balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate);
		balanceSheetService.addExcessIEForPreviousYear(balanceSheet, fundList, getGlcodeForPurposeCode7(), balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate);
		balanceSheetService.removeFundsWithNoData(balanceSheet);
		balanceSheetService.computeCurrentYearTotals(balanceSheet,Constants.LIABILITIES,Constants.ASSETS);
		computeAndAddTotals(balanceSheet);
	}
	
	public void addCurrentOpeningBalancePerFund(Statement balanceSheet,List<Fund> fundList,String transactionQuery) {
		BigDecimal divisor = balanceSheet.getDivisor();
		Query query = getSession().createSQLQuery("select sum(openingdebitbalance)- sum(openingcreditbalance),ts.fundid,substr(coa.glcode,0,"+minorCodeLength+"),coa.type FROM transactionsummary ts,chartofaccounts coa  WHERE ts.glcodeid = coa.ID  AND ts.financialyearid="+balanceSheet.getFinancialYear().getId()+transactionQuery+" GROUP BY ts.fundid,substr(coa.glcode,0,"+minorCodeLength+"),coa.type");
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
							entry.getFundWiseAmount().put(balanceSheetService.getFundNameForId(fundList, new Integer(obj[1].toString())), balanceSheetService.divideAndRound(total, divisor));
						}else{
							for (Entry<String, BigDecimal> object : entry.getFundWiseAmount().entrySet()) {
								if(object.getKey().equalsIgnoreCase(balanceSheetService.getFundNameForId(fundList, new Integer(obj[1].toString())))){
									entry.getFundWiseAmount().put(object.getKey(), object.getValue().add(balanceSheetService.divideAndRound(total, divisor)));
								}
							}
						}
					}
				}
			}
		}
	}

	private String getGlcodeForPurposeCode7() {
		Query query = getSession().createSQLQuery("select substr(glcode,0,"+minorCodeLength+") from chartofaccounts where purposeid=7");
		List list = query.list();
		String glCode = "";
		if(list.get(0) != null)
			glCode = list.get(0).toString();
		return glCode;
	}

	private void populatePreviousYearTotalsForSchedule(Statement balanceSheet, String filterQuery, Date toDate,Date fromDate,String majorCode,Character type) {
		String formattedToDate = "";
		if("Yearly".equalsIgnoreCase(balanceSheet.getPeriod()))
			formattedToDate = balanceSheetService.getFormattedDate(fromDate);
		else
			formattedToDate = balanceSheetService.getFormattedDate(balanceSheetService.getPreviousYearFor(toDate));
		Query query = getSession().createSQLQuery("select sum(debitamount)-sum(creditamount),substr(c.glcode,0,"+
				minorCodeLength+") from generalledger g,chartofaccounts c,voucherheader v ,vouchermis mis where " +
						"v.id=mis.voucherheaderid and v.id=g.voucherheaderid and c.id=g.glcodeid and v.status not in("+voucherStatusToExclude+") " +
						" AND v.voucherdate <= '"+formattedToDate+"' and v.voucherdate >='"+balanceSheetService.getFormattedDate(balanceSheetService.getPreviousYearFor(fromDate))+
						"' and substr(c.glcode,0,"+minorCodeLength+") in (select distinct coa2.glcode from chartofaccounts coa2, schedulemapping " +
						"s where s.id=coa2.scheduleid and coa2.classification=2 and s.reporttype = 'BS')  and substr(c.glcode,0,3)='"+
						majorCode+"' "+filterQuery+" group by substr(c.glcode,0,"+minorCodeLength+")");
		List<Object[]> result = query.list();
		for (Object[] row : result) {
			for (int index = 0; index < balanceSheet.size(); index++) {
				if(balanceSheet.get(index).getGlCode()!=null && row[1].toString().equalsIgnoreCase(balanceSheet.get(index).getGlCode())) {
					BigDecimal previousYearTotal = new BigDecimal(row[0].toString());
					if(L.equalsIgnoreCase(type.toString()))
						previousYearTotal = previousYearTotal.multiply(NEGATIVE);
					previousYearTotal = balanceSheetService.divideAndRound(previousYearTotal, balanceSheet.getDivisor());
					balanceSheet.get(index).setPreviousYearTotal(previousYearTotal);
				}
			}
		}
	}

	private void populateCurrentYearAmountForSchedule(Statement balanceSheet, List<Fund> fundList,String filterQuery, Date toDate,Date fromDate,String majorCode,Character type) {
		BigDecimal divisor = balanceSheet.getDivisor();
		List<Object[]> allGlCodes =  getAllGlCodesForSubSchedule(majorCode,type,BS);
		addRowForSchedule(balanceSheet, allGlCodes);
		List<Object[]> resultMap = currentYearAmountQuery(filterQuery, toDate,fromDate, majorCode,BS);
		for(Object[] obj :allGlCodes){
			if(!contains(resultMap,obj[0].toString())) {
				balanceSheet.add(new StatementEntry(obj[0].toString(),obj[1].toString(),"",BigDecimal.ZERO,BigDecimal.ZERO,false));
			}else{
				List<Object[]> rowsForGlcode = getRowsForGlcode(resultMap,obj[0].toString());
				for (Object[] row : rowsForGlcode) {
					if(!balanceSheet.containsBalanceSheetEntry(row[2].toString())){
						StatementEntry balanceSheetEntry = new StatementEntry();
						if(row[0]!=null && row[1]!=null){
							BigDecimal total = (BigDecimal)row[0];
							if(L.equalsIgnoreCase(type.toString()))
								total = total.multiply(NEGATIVE);
							balanceSheetEntry.getFundWiseAmount().put(balanceSheetService.getFundNameForId(fundList,new Integer(row[1].toString())), balanceSheetService.divideAndRound(total, divisor));
						}
						if(row[2]!=null){
							balanceSheetEntry.setGlCode(row[2].toString());
						}
						balanceSheetEntry.setAccountName(obj[1].toString());
						balanceSheet.add(balanceSheetEntry);
					}else{
						for(int index=0;index< balanceSheet.size();index++) {
							BigDecimal amount = balanceSheetService.divideAndRound((BigDecimal)row[0],divisor);
							if(L.equalsIgnoreCase(type.toString()))
								amount = amount.multiply(NEGATIVE);
							if(balanceSheet.get(index).getGlCode() != null && row[2].toString().equals(balanceSheet.get(index).getGlCode())) {
								String fundNameForId = balanceSheetService.getFundNameForId(fundList,new Integer(row[1].toString()));
								if(balanceSheet.get(index).getFundWiseAmount().get(fundNameForId) == null)
									balanceSheet.get(index).getFundWiseAmount().put(balanceSheetService.getFundNameForId(fundList,new Integer(row[1].toString())), amount);
								else
									balanceSheet.get(index).getFundWiseAmount().put(balanceSheetService.getFundNameForId(fundList,new Integer(row[1].toString())), balanceSheet.get(index).getFundWiseAmount().get(balanceSheetService.getFundNameForId(fundList,new Integer(row[1].toString()))).add(amount));
							}
						}
					}
				}
			}
		}
	}

	public void populateDataForAllSchedules(Statement balanceSheet) {
		voucherStatusToExclude = getAppConfigValueFor("finance", "statusexcludeReport");
		minorCodeLength = Integer.valueOf(balanceSheetService.getAppConfigValueFor(Constants.EGF,"coa_minorcode_length"));
		majorCodeLength = Integer.valueOf(balanceSheetService.getAppConfigValueFor(Constants.EGF,"coa_majorcode_length"));
		Date fromDate = balanceSheetService.getFromDate(balanceSheet);
		Date toDate = balanceSheetService.getToDate(balanceSheet);
		List<Fund> fundList = balanceSheet.getFunds();
		populateCurrentYearAmountForAllSchedules(balanceSheet,fundList,amountPerFundQueryForAllSchedules(balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate,BS));
		addCurrentOpeningBalancePerFund(balanceSheet, fundList,balanceSheetService.getTransactionQuery(balanceSheet));
		populatePreviousYearTotalsForScheduleForAllSchedules(balanceSheet,balanceSheetService.getFilterQuery(balanceSheet),toDate,fromDate);
		balanceSheetService.removeFundsWithNoData(balanceSheet);
		balanceSheetService.addExcessIEForCurrentYear(balanceSheet, fundList, getGlcodeForPurposeCode7(), balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate);
		balanceSheetService.addExcessIEForPreviousYear(balanceSheet, fundList, getGlcodeForPurposeCode7(), balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate);
		balanceSheetService.computeCurrentYearTotals(balanceSheet,Constants.LIABILITIES,Constants.ASSETS);
		computeAndAddTotals(balanceSheet);
	}

	private void populatePreviousYearTotalsForScheduleForAllSchedules(Statement balanceSheet,String filterQuery,
			Date toDate, Date fromDate) {
		Date formattedToDate = null;
		BigDecimal divisor = balanceSheet.getDivisor();
		if("Yearly".equalsIgnoreCase(balanceSheet.getPeriod()))
			formattedToDate = fromDate;
		else
			formattedToDate = balanceSheetService.getPreviousYearFor(toDate);
		List<Object[]> resultMap = amountPerFundQueryForAllSchedules(filterQuery, formattedToDate, balanceSheetService.getPreviousYearFor(fromDate),BS);
		List<Object[]> allGlCodes =  getAllGlCodesForAllSchedule(BS,"('A','L')");
		for(Object[] obj :allGlCodes){
			for (Object[] row : resultMap) {
				String glCode = row[2].toString();
				if(glCode.substring(0,majorCodeLength).equals(obj[0].toString())){
					String type = obj[3].toString(); 
					if(!balanceSheet.containsBalanceSheetEntry(row[2].toString())){
						addRowToStatement(balanceSheet, row, glCode);
					}else{
						for(int index=0;index< balanceSheet.size();index++) {
							BigDecimal amount = balanceSheetService.divideAndRound((BigDecimal)row[0],divisor);
							if(L.equalsIgnoreCase(type))
								amount = amount.multiply(NEGATIVE);
							if(balanceSheet.get(index).getGlCode() != null && row[2].toString().equals(balanceSheet.get(index).getGlCode())) {
								balanceSheet.get(index).setPreviousYearTotal(amount);
							}
						}
					}
				}
			}
		}
	}

	private void populateCurrentYearAmountForAllSchedules(Statement balanceSheet, List<Fund> fundList, List<Object[]> currentYearAmounts) {
		BigDecimal divisor = balanceSheet.getDivisor();
		Map<String, Schedules> scheduleToGlCodeMap = getScheduleToGlCodeMap(BS,"('A','L')");
		for(Entry<String,Schedules> entry :scheduleToGlCodeMap.entrySet()){
			String scheduleNumber = entry.getValue().scheduleNumber;
			String scheduleName = entry.getValue().scheduleName;
			String type = entry.getValue().chartOfAccount.size()>0?entry.getValue().chartOfAccount.iterator().next().type:"";
			balanceSheet.add(new StatementEntry(scheduleNumber,scheduleName,"",null,null,true));
			for (Object[] row : currentYearAmounts) {
				String glCode = row[2].toString();
				if(entry.getValue().contains(glCode)){
					if(!balanceSheet.containsBalanceSheetEntry(glCode)){
						StatementEntry balanceSheetEntry = new StatementEntry();
						if(row[0]!=null && row[1]!=null){
							BigDecimal total = (BigDecimal)row[0];
							if(L.equalsIgnoreCase(type))
								total = total.multiply(NEGATIVE);
							balanceSheetEntry.getFundWiseAmount().put(balanceSheetService.getFundNameForId(fundList,new Integer(row[1].toString())), balanceSheetService.divideAndRound(total, divisor));
						}
						balanceSheetEntry.setGlCode(glCode);
						balanceSheetEntry.setAccountName(entry.getValue().getCoaName(glCode));
						balanceSheet.add(balanceSheetEntry);
					}else{
						for(int index=0;index< balanceSheet.size();index++) {
							BigDecimal amount = balanceSheetService.divideAndRound((BigDecimal)row[0],divisor);
							if(L.equalsIgnoreCase(type))
								amount = amount.multiply(NEGATIVE);
							if(balanceSheet.get(index).getGlCode() != null && row[2].toString().equals(balanceSheet.get(index).getGlCode())) {
								String fundNameForId = balanceSheetService.getFundNameForId(fundList,new Integer(row[1].toString()));
								if(balanceSheet.get(index).getFundWiseAmount().get(fundNameForId) == null)
									balanceSheet.get(index).getFundWiseAmount().put(balanceSheetService.getFundNameForId(fundList,new Integer(row[1].toString())), amount);
								else
									balanceSheet.get(index).getFundWiseAmount().put(balanceSheetService.getFundNameForId(fundList,new Integer(row[1].toString())), balanceSheet.get(index).getFundWiseAmount().get(balanceSheetService.getFundNameForId(fundList,new Integer(row[1].toString()))).add(amount));
							}
						}
					}
				}
			}
			for (ChartOfAccount s : entry.getValue().chartOfAccount) {
				if(!balanceSheet.containsBalanceSheetEntry(s.glCode)){
					StatementEntry balanceSheetEntry = new StatementEntry();
					balanceSheetEntry.setGlCode(s.glCode);
					balanceSheetEntry.setAccountName(s.name);
					balanceSheet.add(balanceSheetEntry);
				}
			}
		}
	}

}
