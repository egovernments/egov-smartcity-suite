/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
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
import org.egov.infstr.utils.HibernateUtil;
import org.egov.utils.Constants;
import org.egov.web.actions.report.IEStatementEntry;
import org.egov.web.actions.report.Statement;
import org.egov.web.actions.report.StatementResultObject;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

public class IncomeExpenditureService extends ReportService{
	private static final String I = "I";
	private static final String E = "E";
	private static final String IE = "IE";
	Date fromDate ;
	Date toDate;
	private static final BigDecimal NEGATIVE = new BigDecimal(-1);
	private FunctionwiseIEService functionwiseIEService;
	

	public FunctionwiseIEService getFunctionwiseIEService() {
		return functionwiseIEService;
	}

	public void setFunctionwiseIEService(FunctionwiseIEService functionwiseIEService) {
		this.functionwiseIEService = functionwiseIEService;
	}

	protected void addRowsToStatement(Statement balanceSheet,Statement assets, Statement liabilities) {
		IEStatementEntry incomeEntry = new IEStatementEntry();
		IEStatementEntry expenseEntry = new IEStatementEntry();
		List<IEStatementEntry>  totalIncomeOverExpense = new ArrayList<IEStatementEntry>();
		
		if(liabilities.sizeIE()>0){
			balanceSheet.addIE(new IEStatementEntry(null,Constants.INCOME,"",true));
			incomeEntry=getTotalIncomeFundwise(liabilities);
			balanceSheet.addAllIE(liabilities);
			balanceSheet.addIE(incomeEntry);
		}
		if(assets.sizeIE()>0){
			balanceSheet.addIE(new IEStatementEntry(null,Constants.EXPENDITURE,"",true));
			expenseEntry=getTotalExpenseFundwise(assets);
			balanceSheet.addAllIE(assets);
			balanceSheet.addIE(expenseEntry);
		}
		totalIncomeOverExpense=computeTotalsIncomeExpense(incomeEntry,expenseEntry);
		for(IEStatementEntry exp:totalIncomeOverExpense){
			balanceSheet.addIE(exp);
		}
		
	}
	
	public void populateIEStatement(Statement ie){
		minorCodeLength = Integer.valueOf(getAppConfigValueFor(Constants.EGF,"coa_minorcode_length"));
		coaType.add('I');
		coaType.add('E');
		fromDate = getFromDate(ie);
		toDate = getToDate(ie);
		String filterQuery = getFilterQuery(ie);
		populateCurrentYearAmountPerFund(ie,filterQuery,toDate,fromDate,IE);
		//populateSchedule(ie,IE);   
		ie=	addBudgetDetails(ie);
		removeFundsWithNoDataIE(ie);
	}
	private Statement addBudgetDetails(Statement ie) {
		List<StatementResultObject> budgetForMajorCodes = getBudgetForMajorCodes(ie);
		//if(LOGGER.isDebugEnabled())
		LOGGER.error("Budget Amounts.................................");
		print(budgetForMajorCodes);
		List<StatementResultObject> budgetReappForMajorCodes = getBudgetReappMinorCodes(ie);
		//if(LOGGER.isDebugEnabled())
		LOGGER.error("Budget Reapp Amounts...........................");
		print(budgetReappForMajorCodes);
		BigDecimal totalBudget=BigDecimal.ZERO;
		for(StatementResultObject ent:budgetForMajorCodes)
		{
			for(StatementResultObject stm:budgetReappForMajorCodes)
			{
				if(ent.getGlCode()!=null && ent.getGlCode().equalsIgnoreCase(stm.getGlCode()))
				{
					
					if(ent.getAmount()!=null)
					{
						if(stm.getAmount()!=null)
						{
							ent.setAmount(ent.getAmount().add(stm.getAmount()));
							
						}
					}else 
					{
						if(stm.getAmount()!=null)
						{
							ent.setAmount(stm.getAmount());
							
						}
					}
				}
			  }
			}

		for( IEStatementEntry ent:ie.getIeEntries())
		{
inner:	for(StatementResultObject stm:budgetForMajorCodes)
			{
			if(ent.getGlCode()!=null && ent.getGlCode().equalsIgnoreCase(stm.getGlCode()))
			{
			ent.setBudgetAmount(stm.getAmount().setScale(2));
			totalBudget=totalBudget.add(ent.getBudgetAmount());
			}
			
			
			
			}
		}
		
		for( IEStatementEntry ent:ie.getIeEntries())
		{
		if(ent.getAccountName()!=null && ent.getAccountName().equalsIgnoreCase(Constants.TOTAL_EXPENDITURE))
		{
			ent.setBudgetAmount(totalBudget);
		}
		}
	return ie;	
	}

	private void print(List<StatementResultObject> list) {
		
		for(StatementResultObject stm:list)
		{
			LOGGER.error(stm.getGlCode()+"         "+stm.getAmount());
		}
	}

	// add previous year amount and current year amount. Opening balance is not added for IE codes
	public void populateCurrentYearAmountPerFund(Statement statement,String filterQuery,Date toDate,Date fromDate,String scheduleReportType){
		if(LOGGER.isDebugEnabled())     LOGGER.debug(" inside populateCurrentYearAmountPerFund ");
		BigDecimal divisor = statement.getDivisor();
		Statement expenditure = new Statement();
		Statement income = new Statement();
		List<StatementResultObject> allGlCodes = getAllGlCodesFor(scheduleReportType);  // has all the IE schedule codes
		
		//get all the net amount total fundwise for each major code
		
		List<StatementResultObject> results = getTransactionAmount(filterQuery, toDate, fromDate,"'I','E'",IE);
		
	
						
		List<StatementResultObject> PreYearResults =getTransactionAmount(filterQuery, getPreviousYearFor(toDate), getPreviousYearFor(fromDate),"'I','E'",scheduleReportType);
		
		for(StatementResultObject queryObject :allGlCodes){
			
			if(queryObject.getGlCode() == null)
				queryObject.setGlCode("");
			List<StatementResultObject> rows = getRowWithGlCode(results,queryObject.getGlCode());
			if(rows.isEmpty() && queryObject.getGlCode()!=null){
				if(contains(PreYearResults,queryObject.getGlCode())){
				  List<StatementResultObject> preRow = getRowWithGlCode(PreYearResults,queryObject.getGlCode());
				  IEStatementEntry preentry = new IEStatementEntry();
				  for (StatementResultObject pre : preRow) {
					   if(I.equalsIgnoreCase(queryObject.getType().toString())){
						  if(pre.isIncome()){ pre.negateAmount();}
							  preentry.getPreviousYearAmount().put(getFundNameForId(statement.getFunds(),Integer.valueOf(pre.getFundId())), divideAndRound(pre.getAmount(), divisor));
						  }else if(E.equalsIgnoreCase(queryObject.getType().toString())){
						   if(pre.isIncome()){ pre.negateAmount();}
						   	  preentry.getPreviousYearAmount().put(getFundNameForId(statement.getFunds(),Integer.valueOf(pre.getFundId())), divideAndRound(pre.getAmount(), divisor));
					     }
				  }
				  if(queryObject.getGlCode()!=null){
					  preentry.setGlCode(queryObject.getGlCode());
					  preentry.setAccountName(queryObject.getScheduleName());
					  preentry.setScheduleNo(queryObject.getScheduleNumber());
				  }
				  if(I.equalsIgnoreCase(queryObject.getType().toString()))
					  income.addIE(preentry);
				  else if(E.equalsIgnoreCase(queryObject.getType().toString()))
					  expenditure.addIE(preentry);
				}	 
			}else{
				for (StatementResultObject row : rows) {
					if(row.isIncome()){	row.negateAmount();}
					if(income.containsIEStatementEntry(row.getGlCode()) || expenditure.containsIEStatementEntry(row.getGlCode())){
						if(I.equalsIgnoreCase(row.getType().toString())){
							 	 addFundAmountIE(statement.getFunds(), income, divisor, row); 
						}else if(E.equalsIgnoreCase(row.getType().toString())){
								 addFundAmountIE(statement.getFunds(), expenditure, divisor, row); 
						}
					}else{
						IEStatementEntry entry = new IEStatementEntry();
						if(row.getAmount()!=null && row.getFundId()!=null){
							entry.getNetAmount().put(getFundNameForId(statement.getFunds(),Integer.valueOf(row.getFundId())), divideAndRound(row.getAmount(), divisor));
							if(queryObject.getGlCode()!=null && contains(PreYearResults,row.getGlCode())){
								List<StatementResultObject> preRow = getRowWithGlCode(PreYearResults,queryObject.getGlCode());
								 for (StatementResultObject pre : preRow) {
									if(pre.isIncome()){pre.negateAmount();}
									if(pre.getGlCode()!=null && pre.getGlCode().equals(row.getGlCode())){
									 entry.getPreviousYearAmount().put(getFundNameForId(statement.getFunds(),Integer.valueOf(pre.getFundId())), divideAndRound(pre.getAmount(), divisor));
									}
								 }
							}
						}
						if(queryObject.getGlCode()!=null){
							entry.setGlCode(queryObject.getGlCode());
							entry.setAccountName(queryObject.getScheduleName());
							entry.setScheduleNo(queryObject.getScheduleNumber());
						}
						if(I.equalsIgnoreCase(row.getType().toString()))
							income.addIE(entry);
						else if(E.equalsIgnoreCase(row.getType().toString())){
							expenditure.addIE(entry);
						}
					}
				}
			}
		
	}addRowsToStatement(statement, expenditure, income);
	
}

	
/*
 * Computes  income over expenditure and vise versa for current year amount and previous year amount
 */
	private List<IEStatementEntry> computeTotalsIncomeExpense(IEStatementEntry incomeFundTotals,IEStatementEntry expenditureFundTotals) {
		Map<String, BigDecimal> netTotal = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> preTotal = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> netTotalin_ex = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> preTotalin_ex = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> netTotalex_in = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> preTotalex_in = new HashMap<String, BigDecimal>();
		Set<String> netFundSet=new HashSet<String>();
		Set<String> preFundSet=new HashSet<String>();
		BigDecimal preAmount=BigDecimal.ZERO;
		BigDecimal curAmount=BigDecimal.ZERO;
		String prevoius="PREVIOUS";
		String current="CURRENT";
		netFundSet=getAllKey(incomeFundTotals, expenditureFundTotals,current);
		preFundSet=getAllKey(incomeFundTotals, expenditureFundTotals,prevoius);
		//Entry<String, BigDecimal> prerow;
		IEStatementEntry income = new IEStatementEntry();
		IEStatementEntry expense = new IEStatementEntry();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Calculating income over expenses");
		List<IEStatementEntry> incomeOverExpenditure=new ArrayList <IEStatementEntry>();
		for(String str:netFundSet){
			if(incomeFundTotals.getNetAmount().containsKey(str)){
				BigDecimal amount = zeroOrValue(incomeFundTotals.getNetAmount().get(str));
				netTotal.put(str, amount.subtract(zeroOrValue(expenditureFundTotals.getNetAmount().get(str))));
			}else if(expenditureFundTotals.getNetAmount().containsKey(str) && !(incomeFundTotals.getNetAmount().containsKey(str))){
				BigDecimal amount = zeroOrValue(incomeFundTotals.getNetAmount().get(str));
				netTotal.put(str,amount.subtract(zeroOrValue(expenditureFundTotals.getNetAmount().get(str))));
		    }
		}
		for(String pstr:preFundSet){
			if(incomeFundTotals.getPreviousYearAmount().containsKey(pstr)){
				BigDecimal amount = zeroOrValue(incomeFundTotals.getPreviousYearAmount().get(pstr));
				preTotal.put(pstr, amount.subtract(zeroOrValue(expenditureFundTotals.getPreviousYearAmount().get(pstr))));
			}else if(expenditureFundTotals.getPreviousYearAmount().containsKey(pstr) && !(incomeFundTotals.getPreviousYearAmount().containsKey(pstr))){
				BigDecimal amount = zeroOrValue(incomeFundTotals.getPreviousYearAmount().get(pstr));
				preTotal.put(pstr, expenditureFundTotals.getPreviousYearAmount().get(pstr));
			}
		}
	
		for(String str:netFundSet){
			int isIncome=netTotal.get(str).signum();
			if(isIncome>0){
				netTotalin_ex.put(str, netTotal.get(str));
				income.setGlCode("A-B");
				income.setAccountName("Income Over Expenditure");
				income.setDisplayBold(true);
				income.setNetAmount(netTotalin_ex);
			}else{
				curAmount= zeroOrValue(netTotal.get(str)).negate();
				netTotalex_in.put(str,curAmount);
				expense.setGlCode("B-A");
				expense.setAccountName("Expenditure Over Income");
				expense.setNetAmount(netTotalex_in);
				expense.setDisplayBold(true);
			}
			
		}

		for(String str:preFundSet){
			int isIncome=preTotal.get(str).signum();
			if(isIncome>0){
				if(income.getGlCode()!=null){
					preTotalin_ex.put(str, preTotal.get(str));
					income.setPreviousYearAmount(preTotalin_ex);
				}else{
					preTotalin_ex.put(str, preTotal.get(str));
					income.setPreviousYearAmount(preTotalin_ex);
					income.setGlCode("A-B");
					income.setAccountName("Income Over Expenditure");
					income.setDisplayBold(true);
					preTotalin_ex.put(str, preTotal.get(str));
					income.setPreviousYearAmount(preTotalin_ex);
				}
			}
			else{
				if(expense.getGlCode()!=null){
				preTotalex_in.put(str, preTotal.get(str).negate());
				expense.setPreviousYearAmount(preTotalex_in);
			}else{
					curAmount= zeroOrValue(preTotal.get(str)).negate();
					preTotalex_in.put(str,curAmount);
					expense.setGlCode("B-A");
					expense.setAccountName("Expenditure Over Income");
					expense.setDisplayBold(true);
					preTotalex_in.put(str,curAmount);
					expense.setPreviousYearAmount(preTotalex_in);
				
			}
		}
		}
		incomeOverExpenditure.add(income);
		incomeOverExpenditure.add(expense);
		return incomeOverExpenditure;
	}

	boolean contains(List<StatementResultObject> result,String glCode){
		for (StatementResultObject row : result) {
			
			if(row.getGlCode()!=null && row.getGlCode().equalsIgnoreCase(glCode))
				return true;
		}
		return false;
	}
	void addFundPreviousAmountIE(List<Fund> fundList, Statement type, BigDecimal divisor,
			StatementResultObject row) {
		for (int index = 0; index < type.size(); index++) {
			BigDecimal amount = divideAndRound(row.getAmount(), divisor);
			if (type.get(index).getGlCode() != null
					&& row.getGlCode().equals(type.get(index).getGlCode()))
				type.getIE(index).getPreviousYearAmount().put(
						getFundNameForId(fundList, Integer.valueOf((row
								.getFundId()))), amount);
		}
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
	
	/*
	 * Calculate total Income of current year and previous year 
	 */
	private IEStatementEntry getTotalIncomeFundwise(Statement income_expense){
		Map<String,BigDecimal> fundNetTotals = new HashMap<String, BigDecimal>();
		Map<String,BigDecimal> fundPreTotals = new HashMap<String, BigDecimal>();
		BigDecimal netAmount=BigDecimal.ZERO;
		BigDecimal preAmount=BigDecimal.ZERO;
		for (IEStatementEntry entry : income_expense.getIeEntries()) {
		  
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
		return (new IEStatementEntry("A",Constants.TOTAL_INCOME,fundNetTotals,fundPreTotals,true));
	}
	
	/*
	 * Calculate total Expenditure of current year and previous year 
	 */
	private IEStatementEntry getTotalExpenseFundwise(Statement income_expense){
		
		Map<String,BigDecimal> fundNetTotals = new HashMap<String, BigDecimal>();
		Map<String,BigDecimal> fundPreTotals = new HashMap<String, BigDecimal>();
		BigDecimal netAmount=BigDecimal.ZERO;
		BigDecimal preAmount=BigDecimal.ZERO;
		for (IEStatementEntry entry : income_expense.getIeEntries()) {
		  
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
		 return(new IEStatementEntry("B",Constants.TOTAL_EXPENDITURE,fundNetTotals,fundPreTotals,true));
	}

 /*
 * 	Returns All Fund id for which transaction is made previous year or this year
 * 
 */
	private HashSet<String> getAllKey(IEStatementEntry incomeFundTotals,IEStatementEntry expenditureFundTotals,String amtType){
	
		Set<String> allFundSet=new HashSet<String>();
		if(amtType.equals("CURRENT")){
			for (Entry<String, BigDecimal> row : incomeFundTotals.getNetAmount().entrySet()) {
				allFundSet.add(row.getKey());
			}
			for (Entry<String, BigDecimal> row : expenditureFundTotals.getNetAmount().entrySet()) {
				allFundSet.add(row.getKey());
			}
		}else{
			for (Entry<String, BigDecimal> row : incomeFundTotals.getPreviousYearAmount().entrySet()) {
				allFundSet.add(row.getKey());
			}
			for (Entry<String, BigDecimal> row : expenditureFundTotals.getPreviousYearAmount().entrySet()) {
				allFundSet.add(row.getKey());
			}
		}
		
		return (HashSet)allFundSet;
		
	}
	
	private List<StatementResultObject> getBudgetForMajorCodes(Statement incomeExpenditureStatement) {
		
		StringBuffer queryStr=new StringBuffer(1024);
			
		queryStr.append(" select coa.majorCode as glcode, sum(bd.approvedamount) as amount "); 
		
		queryStr.append(" from egf_budgetdetail bd , egf_budgetgroup bg,egf_budget b, chartofaccounts coa, eg_wf_states wfs " );
		
				
		queryStr.append("where ((bg.maxcode<=coa.id and bg.mincode>=coa.id) or bg.majorcode=coa.id ) and bd.budgetgroup= bg.id "+
				" and bd.budget=b.id and  bd.state_id=wfs.id  and wfs.value='END'  and b.isbere=:isBeRe and b.financialyearid=:finYearId   " );
		if(incomeExpenditureStatement.getFund()!=null && incomeExpenditureStatement.getFund().getId()!=null && incomeExpenditureStatement.getFund().getId()!=0)
		{
			queryStr.append(" and bd.fund="+incomeExpenditureStatement.getFund().getId());
		}
		if(incomeExpenditureStatement.getDepartment()!=null && incomeExpenditureStatement.getDepartment().getId()!=0)
		{
			queryStr.append(" and bd.executing_department="+incomeExpenditureStatement.getDepartment().getId());
		}
		if(incomeExpenditureStatement.getFunction()!=null && incomeExpenditureStatement.getFunction().getId()!=null && incomeExpenditureStatement.getFunction().getId()!=0)
		{
			queryStr.append("  and bd.function= "+incomeExpenditureStatement.getFunction().getId());
		}
		
			queryStr.append(" and coa.majorcode is not null  group by coa.majorCode ");
		
		queryStr.append(" order by 1");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("query is "+queryStr.toString());
		SQLQuery budgteQuery =HibernateUtil.getCurrentSession().createSQLQuery(queryStr.toString());
		budgteQuery.addScalar("glCode").addScalar("amount").setResultTransformer(Transformers.aliasToBean(StatementResultObject.class));
		budgteQuery.setLong("finYearId", incomeExpenditureStatement.getFinancialYear().getId())
		.setString("isBeRe", "RE");
		List<StatementResultObject> list =(List<StatementResultObject>) budgteQuery.list();
		return list;
		
	}
	private List<StatementResultObject> getBudgetReappMinorCodes(Statement incomeExpenditureStatement) {
		StringBuffer queryStr=new StringBuffer(1024);
	
		queryStr.append(" select coa.majorcode as glCode, sum(bdr.addition_amount- bdr.deduction_amount) as amount "); 
		
		queryStr.append(" from egf_budgetdetail bd , egf_budgetgroup bg,egf_budget b, chartofaccounts coa,eg_wf_states wfs,egf_budget_reappropriation bdr where ((bg.maxcode<=coa.id and bg.mincode>=coa.id) or bg.majorcode=coa.id ) and bd.budgetgroup= bg.id "+
				"  and bdr.budgetdetail=bd.id and bd.budget=b.id and bdr.state_id=wfs.id  and wfs.value='END' and b.isbere=:isBeRe and b.financialyearid=:finYearId  ");
		
		if( incomeExpenditureStatement.getFund()!=null && incomeExpenditureStatement.getFund().getId()!=null && incomeExpenditureStatement.getFund().getId()!=0)
		{
			queryStr.append(" and bd.fund="+incomeExpenditureStatement.getFund().getId());
		}
		if(incomeExpenditureStatement.getDepartment()!=null && incomeExpenditureStatement.getDepartment().getId()!=0)
		{
			queryStr.append(" and bd.executing_department="+incomeExpenditureStatement.getDepartment().getId());
		}
		if(incomeExpenditureStatement.getFunction()!=null && incomeExpenditureStatement.getFunction().getId()!=null && incomeExpenditureStatement.getFunction().getId()!=0)
		{
			queryStr.append("  and bd.function= "+incomeExpenditureStatement.getFunction().getId());
		}
		queryStr.append("  group by coa.majorCode ");
		

		queryStr.append(" order by 1 asc");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("query is "+queryStr.toString());
		SQLQuery budgteReappQuery =HibernateUtil.getCurrentSession().createSQLQuery(queryStr.toString());
		budgteReappQuery.addScalar("glCode").addScalar("amount").setResultTransformer(Transformers.aliasToBean(StatementResultObject.class));
		budgteReappQuery.setLong("finYearId", incomeExpenditureStatement.getFinancialYear().getId())
		.setString("isBeRe", "RE");
		List<StatementResultObject> list =(List<StatementResultObject>) budgteReappQuery.list();
		return list;
	}

	
	
	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}


}
