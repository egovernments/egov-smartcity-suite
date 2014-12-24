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

public class IncomeExpenditureScheduleService extends ScheduleService{
	private static final String IE = "IE";
	private static final String I = "I";
	private IncomeExpenditureService incomeExpenditureService;
	
	public void setIncomeExpenditureService(IncomeExpenditureService incomeExpenditureService) {
		this.incomeExpenditureService = incomeExpenditureService;
	}

	public void populateDataForSchedule(Statement statement,String majorCode) {
		voucherStatusToExclude = getAppConfigValueFor("finance", "statusexcludeReport");
		minorCodeLength = Integer.valueOf(incomeExpenditureService.getAppConfigValueFor(Constants.EGF,"coa_minorcode_length"));
		Date fromDate = incomeExpenditureService.getFromDate(statement);
		Date toDate = incomeExpenditureService.getToDate(statement);
		CChartOfAccounts coa = (CChartOfAccounts) find("from CChartOfAccounts where glcode=?", majorCode);
		populateCurrentYearAmountForSchedule(statement,toDate,fromDate,majorCode,coa.getType());
		addCurrentOpeningBalancePerFund(statement,incomeExpenditureService.getTransactionQuery(statement));
		populatePreviousYearTotalsForSchedule(statement,toDate,fromDate,majorCode,coa.getType());
		incomeExpenditureService.removeFundsWithNoData(statement);
		incomeExpenditureService.computeCurrentYearTotals(statement,Constants.INCOME,Constants.EXPENDITURE);
		computeAndAddTotals(statement);
	}
	
	public void addCurrentOpeningBalancePerFund(Statement statement,String transactionQuery) {
		BigDecimal divisor = statement.getDivisor();
		Query query = getSession().createSQLQuery("select sum(openingdebitbalance)- sum(openingcreditbalance),ts.fundid,substr(coa.glcode,0,"+
				minorCodeLength+"),coa.type FROM transactionsummary ts,chartofaccounts coa  WHERE ts.glcodeid = coa.ID  AND ts.financialyearid="+
				statement.getFinancialYear().getId()+transactionQuery+" GROUP BY ts.fundid,substr(coa.glcode,0,"+minorCodeLength+"),coa.type");
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
							entry.getFundWiseAmount().put(incomeExpenditureService.getFundNameForId(statement.getFunds(), new Integer(obj[1].toString())), incomeExpenditureService.divideAndRound(total, divisor));
						}else{
							for (Entry<String, BigDecimal> object : entry.getFundWiseAmount().entrySet()) {
								if(object.getKey().equalsIgnoreCase(incomeExpenditureService.getFundNameForId(statement.getFunds(), new Integer(obj[1].toString())))){
									entry.getFundWiseAmount().put(object.getKey(), object.getValue().add(incomeExpenditureService.divideAndRound(total, divisor)));
								}
							}
						}
					}
				}
			}
		}
	}

	private void populatePreviousYearTotalsForSchedule(Statement statement, Date toDate,Date fromDate,String majorCode,Character type) {
		String formattedToDate = "";
		if("Yearly".equalsIgnoreCase(statement.getPeriod()))
			formattedToDate = incomeExpenditureService.getFormattedDate(fromDate);
		else
			formattedToDate = incomeExpenditureService.getFormattedDate(incomeExpenditureService.getPreviousYearFor(toDate));
		Query query = getSession().createSQLQuery("select sum(debitamount)-sum(creditamount),substr(c.glcode,0,"+minorCodeLength+") from " +
				"generalledger g,chartofaccounts c,voucherheader v ,vouchermis mis where v.id=mis.voucherheaderid and v.id=g.voucherheaderid " +
				"and c.id=g.glcodeid and v.status not in("+voucherStatusToExclude+")  AND v.voucherdate <= '"+formattedToDate+"' and v.voucherdate >='"+
				incomeExpenditureService.getFormattedDate(incomeExpenditureService.getPreviousYearFor(fromDate))+"' and substr(c.glcode,0,"+
				minorCodeLength+") in (select distinct coa2.glcode from chartofaccounts coa2, schedulemapping s where s.id=coa2.scheduleid " +
						"and coa2.classification=2 and s.reporttype = 'BS')  and c.majorcode='"+majorCode+"' "+incomeExpenditureService.getFilterQuery(statement)
						+" group by substr(c.glcode,0,"+minorCodeLength+")");
		List<Object[]> result = query.list();
		for (Object[] row : result) {
			for (int index = 0; index < statement.size(); index++) {
				if(statement.get(index).getGlCode()!=null && row[1].toString().equalsIgnoreCase(statement.get(index).getGlCode())) {
					BigDecimal previousYearTotal = new BigDecimal(row[0].toString());
					if(I.equalsIgnoreCase(type.toString()))
						previousYearTotal = previousYearTotal.multiply(NEGATIVE);
					previousYearTotal = incomeExpenditureService.divideAndRound(previousYearTotal, statement.getDivisor());
					statement.get(index).setPreviousYearTotal(previousYearTotal);
				}
			}
		}
	}

	private void populateCurrentYearAmountForSchedule(Statement statement, Date toDate,Date fromDate,String majorCode,Character type) {
		BigDecimal divisor = statement.getDivisor();
		List<Object[]> allGlCodes =  getAllGlCodesForSubSchedule(majorCode,type,IE);
		addRowForSchedule(statement, allGlCodes);
		List<Object[]> resultMap = currentYearAmountQuery(incomeExpenditureService.getFilterQuery(statement), toDate,fromDate, majorCode,IE);
		for(Object[] obj :allGlCodes){
			if(!contains(resultMap,obj[0].toString())) {
				statement.add(new StatementEntry(obj[0].toString(),obj[1].toString(),"",BigDecimal.ZERO,BigDecimal.ZERO,false));
			}else{
				List<Object[]> rowsForGlcode = getRowsForGlcode(resultMap,obj[0].toString());
				for (Object[] row : rowsForGlcode) {
					if(!statement.containsBalanceSheetEntry(row[2].toString())){
						StatementEntry entry = new StatementEntry();
						if(row[0]!=null && row[1]!=null){
							BigDecimal total = (BigDecimal)row[0];
							if(I.equalsIgnoreCase(type.toString()))
								total = total.multiply(NEGATIVE);
							entry.getFundWiseAmount().put(incomeExpenditureService.getFundNameForId(statement.getFunds(),new Integer(row[1].toString())), incomeExpenditureService.divideAndRound(total, divisor));
						}
						if(row[2]!=null){
							entry.setGlCode(row[2].toString());
						}
						entry.setAccountName(obj[1].toString());
						statement.add(entry);
					}else{
						for(int index=0;index< statement.size();index++) {
							BigDecimal amount = incomeExpenditureService.divideAndRound((BigDecimal)row[0],divisor);
							if(I.equalsIgnoreCase(type.toString()))
								amount = amount.multiply(NEGATIVE);
							if(statement.get(index).getGlCode() != null && row[2].toString().equals(statement.get(index).getGlCode())) {
								String fundNameForId = incomeExpenditureService.getFundNameForId(statement.getFunds(),new Integer(row[1].toString()));
								if(statement.get(index).getFundWiseAmount().get(fundNameForId) == null)
									statement.get(index).getFundWiseAmount().put(incomeExpenditureService.getFundNameForId(statement.getFunds(),new Integer(row[1].toString())), amount);
								else
									statement.get(index).getFundWiseAmount().put(incomeExpenditureService.getFundNameForId(statement.getFunds(),
											new Integer(row[1].toString())), statement.get(index).getFundWiseAmount().get(incomeExpenditureService.getFundNameForId(statement.getFunds(),new Integer(row[1].toString()))).add(amount));
							}
						}
					}
				}
			}
		}
	}

	public void populateDataForAllSchedules(Statement statement) {
		voucherStatusToExclude = getAppConfigValueFor("finance", "statusexcludeReport");
		minorCodeLength = Integer.valueOf(incomeExpenditureService.getAppConfigValueFor(Constants.EGF,"coa_minorcode_length"));
		Date fromDate = incomeExpenditureService.getFromDate(statement);
		Date toDate = incomeExpenditureService.getToDate(statement);
		List<Fund> fundList = statement.getFunds();
		populateCurrentYearAmountForAllSchedules(statement,fundList,amountPerFundQueryForAllSchedules(incomeExpenditureService.getFilterQuery(statement), toDate, fromDate,IE));
		addCurrentOpeningBalancePerFund(statement,incomeExpenditureService.getTransactionQuery(statement));
		populatePreviousYearTotalsForAllSchedules(statement,incomeExpenditureService.getFilterQuery(statement),toDate,fromDate);
		incomeExpenditureService.removeFundsWithNoData(statement);
		incomeExpenditureService.computeCurrentYearTotals(statement,Constants.LIABILITIES,Constants.ASSETS);
		computeAndAddTotals(statement);
	}

	private void populatePreviousYearTotalsForAllSchedules(Statement statement,String filterQuery,
			Date toDate, Date fromDate) {
		Date formattedToDate = null;
		BigDecimal divisor = statement.getDivisor();
		if("Yearly".equalsIgnoreCase(statement.getPeriod()))
			formattedToDate = fromDate;
		else
			formattedToDate = incomeExpenditureService.getPreviousYearFor(toDate);
		List<Object[]> resultMap = amountPerFundQueryForAllSchedules(filterQuery, formattedToDate, incomeExpenditureService.getPreviousYearFor(fromDate),IE);
		List<Object[]> allGlCodes =  getAllGlCodesForAllSchedule(IE,"('I','E')");
		for(Object[] obj :allGlCodes){
			for (Object[] row : resultMap) {
				String glCode = row[2].toString();
				if(glCode.substring(0,3).equals(obj[0].toString())){
					String type = obj[3].toString(); 
					if(!statement.containsBalanceSheetEntry(row[2].toString())){
						addRowToStatement(statement, row, glCode);
					}else{
						for(int index=0;index< statement.size();index++) {
							BigDecimal amount = incomeExpenditureService.divideAndRound((BigDecimal)row[0],divisor);
							if(I.equalsIgnoreCase(type))
								amount = amount.multiply(NEGATIVE);
							if(statement.get(index).getGlCode() != null && row[2].toString().equals(statement.get(index).getGlCode())) {
								statement.get(index).setPreviousYearTotal(amount);
							}
						}
					}
				}
			}
		}
	}

	private void populateCurrentYearAmountForAllSchedules(Statement statement, List<Fund> fundList, List<Object[]> currentYearAmounts) {
		BigDecimal divisor = statement.getDivisor();
		Map<String, Schedules> scheduleToGlCodeMap = getScheduleToGlCodeMap(IE,"('I','E')");
		for(Entry<String,Schedules> entry :scheduleToGlCodeMap.entrySet()){
			String scheduleNumber = entry.getValue().scheduleNumber;
			String scheduleName = entry.getValue().scheduleName;
			String type = entry.getValue().chartOfAccount.size()>0?entry.getValue().chartOfAccount.iterator().next().type:"";
			statement.add(new StatementEntry(scheduleNumber,scheduleName,"",null,null,true));
			for (Object[] row : currentYearAmounts) {
				String glCode = row[2].toString();
				if(entry.getValue().contains(glCode)){
					if(!statement.containsBalanceSheetEntry(glCode)){
						StatementEntry balanceSheetEntry = new StatementEntry();
						if(row[0]!=null && row[1]!=null){
							BigDecimal total = (BigDecimal)row[0];
							if(I.equalsIgnoreCase(type))
								total = total.multiply(NEGATIVE);
							balanceSheetEntry.getFundWiseAmount().put(incomeExpenditureService.getFundNameForId(fundList,new Integer(row[1].toString())), incomeExpenditureService.divideAndRound(total, divisor));
						}
						balanceSheetEntry.setGlCode(glCode);
						balanceSheetEntry.setAccountName(entry.getValue().getCoaName(glCode));
						statement.add(balanceSheetEntry);
					}else{
						for(int index=0;index< statement.size();index++) {
							BigDecimal amount = incomeExpenditureService.divideAndRound((BigDecimal)row[0],divisor);
							if(I.equalsIgnoreCase(type))
								amount = amount.multiply(NEGATIVE);
							if(statement.get(index).getGlCode() != null && row[2].toString().equals(statement.get(index).getGlCode())) {
								String fundNameForId = incomeExpenditureService.getFundNameForId(fundList,new Integer(row[1].toString()));
								if(statement.get(index).getFundWiseAmount().get(fundNameForId) == null)
									statement.get(index).getFundWiseAmount().put(incomeExpenditureService.getFundNameForId(fundList,new Integer(row[1].toString())), amount);
								else
									statement.get(index).getFundWiseAmount().put(incomeExpenditureService.getFundNameForId(fundList,new Integer(row[1].toString())), statement.get(index).getFundWiseAmount().get(incomeExpenditureService.getFundNameForId(fundList,new Integer(row[1].toString()))).add(amount));
							}
						}
					}
				}
			}
			for (ChartOfAccount s : entry.getValue().chartOfAccount) {
				if(!statement.containsBalanceSheetEntry(s.glCode)){
					StatementEntry statementEntry = new StatementEntry();
					statementEntry.setGlCode(s.glCode);
					statementEntry.setAccountName(s.name);
					statement.add(statementEntry);
				}
			}
		}
	}

}
