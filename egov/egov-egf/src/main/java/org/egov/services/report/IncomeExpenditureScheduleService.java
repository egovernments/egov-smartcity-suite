package org.egov.services.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.Fund;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.utils.Constants;
import org.egov.web.actions.report.IEStatementEntry;
import org.egov.web.actions.report.Statement;
import org.egov.web.actions.report.StatementEntry;
import org.egov.web.actions.report.StatementResultObject;

import org.hibernate.Query;

public class IncomeExpenditureScheduleService extends ScheduleService{
	private static final String IE = "IE";
	private static final String I = "I";
	//private static final String I = "I";
	private BigDecimal fundTotal = BigDecimal.ZERO;
	private IncomeExpenditureService incomeExpenditureService;
	private static final Logger	LOGGER	= Logger.getLogger(IncomeExpenditureScheduleService.class);
	

	public void populateDataForLedgerSchedule(Statement statement,String majorCode) {
		if(LOGGER.isInfoEnabled())     LOGGER.info("Getting ledger details for selected schedlue");
		voucherStatusToExclude = getAppConfigValueFor("finance", "statusexcludeReport");
		minorCodeLength = Integer.valueOf(incomeExpenditureService.getAppConfigValueFor(Constants.EGF,"coa_minorcode_length"));
		Date fromDate = incomeExpenditureService.getFromDate(statement);
		Date toDate = incomeExpenditureService.getToDate(statement);
		String filterQuery=incomeExpenditureService.getFilterQuery(statement);
		CChartOfAccounts coa = null;//This fix is for Phoenix Migration. (CChartOfAccounts) find("from CChartOfAccounts where glcode=?", majorCode);
		populateCurrentYearAmountForDetail(statement,toDate,fromDate,majorCode,coa.getType(),filterQuery);
		incomeExpenditureService.removeFundsWithNoDataIE(statement);
		computeAndAddScheduleTotals(statement);
	}	
	
	
	public void populateDataForAllSchedules(Statement statement) {
		voucherStatusToExclude = getAppConfigValueFor("finance", "statusexcludeReport");
		minorCodeLength = Integer.valueOf(incomeExpenditureService.getAppConfigValueFor(Constants.EGF,"coa_minorcode_length"));
		Date fromDate = incomeExpenditureService.getFromDate(statement);
		Date toDate = incomeExpenditureService.getToDate(statement);
		List<Fund> fundList = statement.getFunds();
		populateCurrentYearAmountForAllSchedules(statement,fundList,amountPerFundQueryForAllSchedules(incomeExpenditureService.getFilterQuery(statement), toDate, fromDate,IE));
		populatePreviousYearTotalsForAllSchedules(statement,incomeExpenditureService.getFilterQuery(statement),toDate,fromDate);
		incomeExpenditureService.removeFundsWithNoData(statement);
		incomeExpenditureService.computeCurrentYearTotals(statement,Constants.LIABILITIES,Constants.ASSETS);
		computeAndAddTotals(statement);
	}
	
	private Query populatePreviousYearTotals(Statement statement, Date toDate,Date fromDate,String majorCode,String filterQuery, String fundId  ) {
		String formattedToDate = "";
		String voucherStatusToExclude = getAppConfigValueFor("finance", "statusexcludeReport");
		String majorCodeQuery="";
		if(!( majorCodeQuery.equals("") || majorCodeQuery.isEmpty())){
			majorCodeQuery=" and c.majorcode = '"+majorCode+"' ";
		}
			   
		if(LOGGER.isInfoEnabled())     LOGGER.info("Getting previous year Details");
		if("Yearly".equalsIgnoreCase(statement.getPeriod()))
			formattedToDate = incomeExpenditureService.getFormattedDate(fromDate);
		else
			formattedToDate = incomeExpenditureService.getFormattedDate(incomeExpenditureService.getPreviousYearFor(toDate));
		Query query =null;/*//This fix is for Phoenix Migration.HibernateUtil.getCurrentSession().createSQLQuery("select c.glcode,c.name ,sum(g.debitamount)-sum(g.creditamount),v.fundid ,c.type ,c.majorcode  from " +
				"generalledger g,chartofaccounts c,voucherheader v ,vouchermis mis where v.id=mis.voucherheaderid and  v.fundid in"+fundId +
				" and v.id=g.voucherheaderid " +
				" and c.id=g.glcodeid and v.status not in("+voucherStatusToExclude+")  AND v.voucherdate < '"+formattedToDate+"' and v.voucherdate >='"+
				incomeExpenditureService.getFormattedDate(incomeExpenditureService.getPreviousYearFor(fromDate))+"'"+majorCodeQuery+
				filterQuery+" group by c.glcode, v.fundid,c.name ,c.type ,c.majorcode order by c.glcode,v.fundid,c.type");*/
		if(LOGGER.isInfoEnabled())     LOGGER.info("prevoius year to Date="+formattedToDate+" and from Date="+incomeExpenditureService.getPreviousYearFor(fromDate));
		return query;
	}
	public void populateDetailcode(Statement statement) {
		Date fromDate = incomeExpenditureService.getFromDate(statement);                
		Date toDate = incomeExpenditureService.getToDate(statement);
		//List<Fund> fundList = statement.getFunds();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("preparing list to load all detailcode");
		populateAmountForAllSchedules(statement,toDate,fromDate,"('I','E')");
		incomeExpenditureService.removeFundsWithNoDataIE(statement);
				  
	}
	
	boolean isIEContainsScheduleEntry(List<Object[]> accountCodeList,String majorCode){
		for (Object[] row : accountCodeList) {
			if(row[3]!=null && majorCode.equals(row[3].toString()))
				return true;
		}
		return false;
	}
	@SuppressWarnings({ "unused", "unchecked" })
	private void populateAmountForAllSchedules(Statement statement, Date toDate,Date fromDate,String reportType) {
		boolean addrow=false;    
		BigDecimal divisor = statement.getDivisor();
		BigDecimal amount = BigDecimal.ZERO;
		BigDecimal preAmount = BigDecimal.ZERO;
		Map <String ,String> schduleTotalMap = new HashMap<String ,String>();
		IEStatementEntry scheduleEntry = new IEStatementEntry();
		Map<String,BigDecimal> currentYearScheduleTotal=new HashMap<String, BigDecimal>();;
		Map<String,BigDecimal> previousYearScheduleTotal=new HashMap<String, BigDecimal>();;
		Map<String,BigDecimal> currentYearTotalIncome=new HashMap<String, BigDecimal>() ;
		Map<String,BigDecimal> currentYearTotalExpense=new HashMap<String, BigDecimal>() ;
		Map<String,BigDecimal> previousYearTotalIncome=new HashMap<String, BigDecimal>() ;
		Map<String,BigDecimal> previousYearTotalExpense=new HashMap<String, BigDecimal>() ;
		                 
		boolean addschedulerow=false;    
		String forAllCOA="";       
		String filterQuery=incomeExpenditureService.getFilterQuery(statement);
		String fundId=incomeExpenditureService.getfundList(statement.getFunds());
		majorCodeLength = Integer.valueOf(incomeExpenditureService.getAppConfigValueFor(Constants.EGF,"coa_majorcode_length"));
		List<Object[]> previousLedgerBalance= populatePreviousYearTotals(statement,toDate,fromDate,forAllCOA,filterQuery,fundId).list();
		List<Object[]> CurrentYearLedgerDetail=getAllLedgerTransaction(forAllCOA,toDate,fromDate,fundId,filterQuery); // current year transaction detail
		List<Object[]> schduleMap =getAllGlCodesForSchedule(reportType);
		for (Object[] row : schduleMap){
			String glCode = row[0]!=null?row[0].toString():""; 
		if(!statement.containsMajorCodeEntry(row[0].toString().substring(0, majorCodeLength))){
			if(statement.getIeEntries().size()>1){    
				statement.addIE(new IEStatementEntry(null,"Schedule Total",currentYearScheduleTotal,previousYearScheduleTotal,true)); 
				statement.addIE(new IEStatementEntry("Schedule "+row[1].toString()+":",row[2].toString(),"",row[0].toString().substring(0, majorCodeLength),true));
				currentYearScheduleTotal=new HashMap<String, BigDecimal>();
				previousYearScheduleTotal=new HashMap<String, BigDecimal>();
			}else{
				statement.addIE(new IEStatementEntry("Schedule "+row[1].toString()+":",row[2].toString(),"",row[0].toString().substring(0, majorCodeLength),true));
			}
		}
		 if(!statement.containsIEStatementEntry(row[0].toString())){
			IEStatementEntry ieEntry = new IEStatementEntry();
			if(ieContains(CurrentYearLedgerDetail,row[0].toString())){
				for (Object[] cur : CurrentYearLedgerDetail){
					String fundnm=incomeExpenditureService.getFundNameForId(statement.getFunds(),Integer.valueOf(cur[3].toString()));
					if(cur[0].toString().equals(row[0].toString())){   
							addrow=true;
							if(I.equalsIgnoreCase(cur[4].toString())){ 
								  	amount=((BigDecimal)cur[2]).multiply(NEGATIVE);
								  	/*if(currentYearTotalIncome.containsKey(fundnm) && currentYearTotalIncome.get(fundnm)!=null)
								  		currentYearTotalIncome.put(fundnm,currentYearTotalIncome.get(fundnm).add(incomeExpenditureService.divideAndRound(amount, divisor)));
								  	else
								  		currentYearTotalIncome.put(fundnm,incomeExpenditureService.divideAndRound(amount, divisor));*/
								 
							}else{
							   	amount=((BigDecimal)cur[2]);
							   	/*if(currentYearTotalExpense.containsKey(fundnm))
							   		currentYearTotalExpense.put(fundnm,currentYearTotalExpense.get(fundnm).add(incomeExpenditureService.divideAndRound(amount, divisor)));
							  	else
							  		currentYearTotalExpense.put(fundnm,incomeExpenditureService.divideAndRound(amount, divisor));*/
							}
							if(currentYearScheduleTotal.containsKey(fundnm)){
								currentYearScheduleTotal.put(fundnm,currentYearScheduleTotal.get(fundnm).add(zeroOrValue(incomeExpenditureService.divideAndRound(amount, divisor))));
							  }else{
								  currentYearScheduleTotal.put(fundnm,incomeExpenditureService.divideAndRound(amount, divisor));
							}
							ieEntry.getNetAmount().put(fundnm,incomeExpenditureService.divideAndRound(amount, divisor));
					}
				}        
			}         
			if(ieContains(previousLedgerBalance,row[0].toString())){
				for (Object[] pre : previousLedgerBalance){	
					if(pre[0].toString().equals(row[0].toString())){
						String fundnm=incomeExpenditureService.getFundNameForId(statement.getFunds(),Integer.valueOf(pre[3].toString()));
						addrow=true;            
						if(I.equalsIgnoreCase(pre[4].toString())){ 
							preAmount=((BigDecimal)pre[2]).multiply(NEGATIVE);
							/*if(previousYearTotalIncome.containsKey(fundnm))            
								previousYearTotalIncome.put(fundnm,previousYearTotalIncome.get(fundnm).add(incomeExpenditureService.divideAndRound(preAmount, divisor)));
						  	else
						  		previousYearTotalIncome.put(fundnm,incomeExpenditureService.divideAndRound(preAmount, divisor));*/
						}else{
							preAmount=((BigDecimal)pre[2]);
							/*if(previousYearTotalExpense.containsKey(fundnm))            
								previousYearTotalExpense.get(fundnm).add(incomeExpenditureService.divideAndRound(preAmount, divisor));
						  	else
						  		previousYearTotalExpense.put(fundnm,incomeExpenditureService.divideAndRound(preAmount, divisor));*/
						}
						
						
						 if(previousYearScheduleTotal.containsKey(fundnm)){
							 previousYearScheduleTotal.put(fundnm,previousYearScheduleTotal.get(fundnm).add(zeroOrValue(incomeExpenditureService.divideAndRound(preAmount, divisor))));
						  }else{
							  previousYearScheduleTotal.put(fundnm,incomeExpenditureService.divideAndRound(preAmount, divisor));
						}              
						ieEntry.getPreviousYearAmount().put(fundnm,incomeExpenditureService.divideAndRound(preAmount, divisor));
					}                                           
				}
			}
			if(addschedulerow){
			if(!statement.containsIEStatementEntry(row[0].toString()))
				 statement.addIE(scheduleEntry);
			}
			if(addrow){
			 ieEntry.setGlCode(row[0].toString());
			 ieEntry.setAccountName(row[4].toString());
			 ieEntry.setMajorCode(row[2]!=null?row[2].toString():"");  
			 statement.addIE(ieEntry);
			}addrow=false;addschedulerow=false;
		}
	  
		}int lastIndex=statement.getIeEntries().size();
		if(statement.getIE(lastIndex-1).getGlCode().contains("Schedule") && !statement.getIE(lastIndex-1).getGlCode().contains("Schedule Total")){
			statement.getIeEntries().remove(lastIndex-1);
		}         
  }       
	/*for detailed*/
	/*void computeAndAddTotalsForSchedules(Statement statement) {
		BigDecimal currentTotal = BigDecimal.ZERO;
		BigDecimal previousTotal = BigDecimal.ZERO;
		for(StatementEntry entry : statement.getEntries()){
			if(entry.getAccountName().equals("Schedule Total")){
				entry.setCurrentYearTotal(currentTotal);
				entry.setPreviousYearTotal(previousTotal);
				currentTotal = BigDecimal.ZERO;
				previousTotal = BigDecimal.ZERO;
			}else{
				if(entry.getCurrentYearTotal() != null)
					currentTotal = currentTotal.add(entry.getCurrentYearTotal());
				if(entry.getPreviousYearTotal() != null)
					previousTotal = previousTotal.add(entry.getPreviousYearTotal());
			}
		}
	}*/
	
	
	/*for detailed*/
	void computeAndAddTotalsForSchedules(Statement statement) {
		BigDecimal currentTotal = BigDecimal.ZERO;
		BigDecimal previousTotal = BigDecimal.ZERO;
		Map<String,BigDecimal> fundNetTotals=new HashMap<String, BigDecimal>() ;
		Map<String,BigDecimal> fundPreTotals=new HashMap<String, BigDecimal>() ;
		//int lastInd, size = statement.getIeEntries().size();
		
		for(IEStatementEntry entry : statement.getIeEntries()){
			if(entry.getAccountName().equals("Schedule Total")){
				for (Entry<String, BigDecimal> row :fundNetTotals.entrySet()) {
					if(fundNetTotals.get(row.getKey())==null){
						entry.getNetAmount().put(row.getKey(),fundNetTotals.get(row.getKey()));
					}
				}
				for (Entry<String, BigDecimal> row :fundPreTotals.entrySet()) {
					if(fundNetTotals.get(row.getKey())==null){
						entry.getPreviousYearAmount().put(row.getKey(),fundPreTotals.get(row.getKey()));
					}
				}                   
				      
				currentTotal = BigDecimal.ZERO;
				previousTotal = BigDecimal.ZERO;
			}else{
				for (Entry<String, BigDecimal> row : entry.getNetAmount().entrySet()) {
					if(fundNetTotals.get(row.getKey())==null){
						fundNetTotals.put(row.getKey(),BigDecimal.ZERO);
					}
					currentTotal=zeroOrValue(row.getValue());
					 fundNetTotals.put(row.getKey(),currentTotal.add(zeroOrValue(fundNetTotals.get(row.getKey()))));
				}
				for (Entry<String, BigDecimal> prerow : entry.getPreviousYearAmount().entrySet()) {
					if(fundPreTotals.get(prerow.getKey())==null){
						fundPreTotals.put(prerow.getKey(),BigDecimal.ZERO);
					}
					previousTotal=zeroOrValue(prerow.getValue());
					 fundPreTotals.put(prerow.getKey(),previousTotal.add(zeroOrValue(fundPreTotals.get(prerow.getKey()))));
				}
			}
		}
	}
   /*
    * Add ledger details for current year and previous year. We dont calculate for openeing balance for 
    * income and expenditure codes  
    */
	
	@SuppressWarnings("unused")
	private void populateCurrentYearAmountForDetail(Statement statement, Date toDate,Date fromDate,String majorCode,Character type,String filterQuery) {
		boolean addrow=false;
		BigDecimal divisor = statement.getDivisor();
		BigDecimal amount = BigDecimal.ZERO;
		BigDecimal preAmount = BigDecimal.ZERO;
		String fundId=incomeExpenditureService.getfundList(statement.getFunds());
		if(LOGGER.isInfoEnabled())     LOGGER.info("Getting All ledger codes ..");
		List<Object[]> AllLedger =null;//This fix is for Phoenix Migration. HibernateUtil.getCurrentSession().createSQLQuery("select coa.glcode,coa.name from chartofaccounts coa where coa.majorcode="+majorCode+" and coa.classification=4 and coa.type='"+type+"'  order by coa.glcode").list();
		List<Object[]> previousLedgerBalance= populatePreviousYearTotals(statement,toDate,fromDate,majorCode,filterQuery,fundId).list();
		List<Object[]> allGlCodes =  getAllGlCodesForSubSchedule(majorCode,type,IE); // for schedule name AND MINOR code
		
		List<Object[]> CurrentYearLedgerDetail=getAllLedgerTransaction(majorCode,toDate,fromDate,fundId,filterQuery); // current year transaction detail
		addRowForIESchedule(statement, allGlCodes); 
		
		for (Object[] row : AllLedger){
		 if(!statement.containsIEStatementEntry(row[0].toString())){
			IEStatementEntry ieEntry = new IEStatementEntry();
			if(ieContains(CurrentYearLedgerDetail,row[0].toString())){
				for (Object[] cur : CurrentYearLedgerDetail){
					if(cur[0].toString().equals(row[0].toString())){
						addrow=true;
						if(I.equalsIgnoreCase(type.toString())){ 
						  	amount=((BigDecimal)cur[2]).multiply(NEGATIVE);
						}else{
						   	amount=((BigDecimal)cur[2]);
						}
						ieEntry.getNetAmount().put(incomeExpenditureService.getFundNameForId(statement.getFunds(),
									Integer.valueOf(cur[3].toString())),incomeExpenditureService.divideAndRound(amount, divisor));
							
						}
				}
			}
			if(ieContains(previousLedgerBalance,row[0].toString())){
				for (Object[] pre : previousLedgerBalance){	
					if(pre[0].toString().equals(row[0].toString())){
						addrow=true;
						if(I.equalsIgnoreCase(type.toString())){ 
							preAmount=((BigDecimal)pre[2]).multiply(NEGATIVE);
						}else{
							preAmount=((BigDecimal)pre[2]);
						}
						ieEntry.getPreviousYearAmount().put(incomeExpenditureService.getFundNameForId(statement.getFunds(),
									Integer.valueOf(pre[3].toString())),incomeExpenditureService.divideAndRound(preAmount, divisor));
								
					}
				}
			}    
			if(addrow){
			 ieEntry.setGlCode(row[0].toString());
			 ieEntry.setAccountName(row[1].toString());
			 statement.addIE(ieEntry);
			}addrow=false;
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



	
	boolean ieContains(List<Object[]> previousBalance,String glCode){
		for (Object[] row : previousBalance) {
			if(row[0]!=null && glCode.equals(row[0].toString()))
				return true;
		}
		return false;
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
	
	void computeAndAddScheduleTotals(Statement statement) {
		Map<String,BigDecimal> fundNetTotals = new HashMap<String, BigDecimal>();
		Map<String,BigDecimal> fundPreTotals = new HashMap<String, BigDecimal>();
		BigDecimal netAmount=BigDecimal.ZERO;
		BigDecimal preAmount=BigDecimal.ZERO;
		for (IEStatementEntry entry : statement.getIeEntries()) {
			
			for (Entry<String, BigDecimal> row : entry.getNetAmount().entrySet()) {
				if(fundNetTotals.get(row.getKey())==null){
					fundNetTotals.put(row.getKey(),BigDecimal.ZERO);
				}
				 netAmount=zeroOrValue(row.getValue());
				 fundNetTotals.put(row.getKey(),netAmount.add(zeroOrValue(fundNetTotals.get(row.getKey()))));
			}
			for (Entry<String, BigDecimal> prerow : entry.getPreviousYearAmount().entrySet()) {
				if(fundPreTotals.get(prerow.getKey())==null){
					fundPreTotals.put(prerow.getKey(),BigDecimal.ZERO);
				}
				 preAmount=zeroOrValue(prerow.getValue());
				 fundPreTotals.put(prerow.getKey(),preAmount.add(zeroOrValue(fundPreTotals.get(prerow.getKey()))));
			}
		  }	
		
		statement.addIE(new IEStatementEntry(null,"Total",fundNetTotals,fundPreTotals,true));
	}
	private BigDecimal zeroOrValue(BigDecimal value) {
		return value == null ? BigDecimal.ZERO : value;
	}
	
	public void setIncomeExpenditureService(IncomeExpenditureService incomeExpenditureService) {
		this.incomeExpenditureService = incomeExpenditureService;
	}	

	
	
}
