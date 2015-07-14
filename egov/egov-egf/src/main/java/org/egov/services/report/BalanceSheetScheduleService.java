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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Fund;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.report.Statement;
import org.egov.web.actions.report.StatementEntry;
import org.hibernate.Query;

public class BalanceSheetScheduleService extends ScheduleService{
	private static final String BS = "BS";
	private static final String L = "L";
	private BalanceSheetService balanceSheetService;
	private String removeEntrysWithZeroAmount = "";
	private static final Logger	LOGGER	= Logger.getLogger(BalanceSheetScheduleService.class);
	
	public void setBalanceSheetService(BalanceSheetService balanceSheetService) {
		this.balanceSheetService = balanceSheetService;
	}

	public void populateDataForSchedule(Statement balanceSheet,String majorCode) {
		getAppConfigValueForRemoveEntrysWithZeroAmount();
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
		addOpeningBalanceForPreviousYear(balanceSheet,balanceSheetService.getTransactionQuery(balanceSheet),fromDate);
		balanceSheetService.addExcessIEForCurrentYear(balanceSheet, fundList, getGlcodeForPurposeCode7(), balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate);
		balanceSheetService.addExcessIEForPreviousYear(balanceSheet, fundList, getGlcodeForPurposeCode7(), balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate);
		balanceSheetService.removeFundsWithNoData(balanceSheet);
		balanceSheetService.computeCurrentYearTotals(balanceSheet,Constants.LIABILITIES,Constants.ASSETS);
		computeAndAddTotals(balanceSheet);
		if(removeEntrysWithZeroAmount.equalsIgnoreCase("Yes")){
		balanceSheetService.removeScheduleEntrysWithZeroAmount(balanceSheet);
		}
	}

	public void addCurrentOpeningBalancePerFund(Statement balanceSheet,List<Fund> fundList,String transactionQuery) {
		BigDecimal divisor = balanceSheet.getDivisor();
	if(LOGGER.isDebugEnabled())     LOGGER.debug("addCurrentOpeningBalancePerFund");
		Query query = HibernateUtil.getCurrentSession().createSQLQuery("select sum(openingdebitbalance)- sum(openingcreditbalance),ts.fundid,coa.glcode,coa.type FROM transactionsummary ts,chartofaccounts coa  WHERE ts.glcodeid = coa.ID  AND ts.financialyearid="+balanceSheet.getFinancialYear().getId()+transactionQuery+" GROUP BY ts.fundid,coa.glcode,coa.type");
		List<Object[]> openingBalanceAmountList = query.list();
		for(Object[] obj :openingBalanceAmountList){
			if(obj[0]!=null && obj[1]!=null){
				BigDecimal total = (BigDecimal)obj[0];
				if(L.equals(obj[3].toString())){
					total = total.multiply(NEGATIVE);
				}
				for (StatementEntry entry : balanceSheet.getEntries()) {
					if(obj[2].toString().equals(entry.getGlCode())){
						if(LOGGER.isDebugEnabled())     LOGGER.debug(entry.getGlCode()+"=================="+total);
						if(entry.getFundWiseAmount().isEmpty()){
							entry.getFundWiseAmount().put(balanceSheetService.getFundNameForId(fundList, new Integer(obj[1].toString())), balanceSheetService.divideAndRound(total, divisor));
						}else{
							boolean shouldAddNewFund=true;
							for (Entry<String, BigDecimal> object : entry.getFundWiseAmount().entrySet()) {
								if(object.getKey().equalsIgnoreCase(balanceSheetService.getFundNameForId(fundList, new Integer(obj[1].toString())))){
									entry.getFundWiseAmount().put(object.getKey(), object.getValue().add(balanceSheetService.divideAndRound(total, divisor)));
								shouldAddNewFund=false;
								}
							}
							if(shouldAddNewFund)
								entry.getFundWiseAmount().put(balanceSheetService.getFundNameForId(fundList, new Integer(obj[1].toString())), balanceSheetService.divideAndRound(total, divisor));
						}
					}
				}
			}
		}
	}
	
	public void addOpeningBalanceForPreviousYear(Statement balanceSheet,String transactionQuery,Date fromDate) {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("addOpeningBalanceForPreviousYear");
		BigDecimal divisor = balanceSheet.getDivisor();
		FinancialYearHibernateDAO finYrHibernate=new FinancialYearHibernateDAO(CFinancialYear.class,HibernateUtil.getCurrentSession());
		CFinancialYear prevFinanciaYr = finYrHibernate.getPreviousFinancialYearByDate(fromDate);
		String prevFinancialYrId=prevFinanciaYr.getId().toString();
		Query query =HibernateUtil.getCurrentSession().createSQLQuery("select sum(openingdebitbalance)- sum(openingcreditbalance),coa.glcode,coa.type FROM transactionsummary ts,chartofaccounts coa  WHERE ts.glcodeid = coa.ID  AND ts.financialyearid="+prevFinancialYrId+transactionQuery+" GROUP BY coa.glcode,coa.type");
		List<Object[]> openingBalanceAmountList = query.list();
		for(Object[] obj :openingBalanceAmountList){
			if(obj[0]!=null && obj[1]!=null){
				
				BigDecimal total = (BigDecimal)obj[0];
				
				if(L.equals(obj[2].toString())){
					total = total.multiply(NEGATIVE);
				}
				for (StatementEntry entry : balanceSheet.getEntries()) {
					if(obj[1].toString().equals(entry.getGlCode())){
						if(LOGGER.isDebugEnabled())     LOGGER.debug(entry.getGlCode()+"=================="+total);
						BigDecimal prevYrTotal= entry.getPreviousYearTotal();
						prevYrTotal=(prevYrTotal==null?BigDecimal.ZERO:prevYrTotal);
						entry.setPreviousYearTotal(prevYrTotal.add(balanceSheetService.divideAndRound(total, divisor)));
					}
				}
			}
		}
	}                 


	private String getGlcodeForPurposeCode7() {
		Query query =HibernateUtil.getCurrentSession().createSQLQuery("select glcode from chartofaccounts where purposeid=7");
		List list = query.list();
		String glCode = "";
		if(list.get(0) != null)
			glCode = list.get(0).toString();
		return glCode;
	}
	
	
	private String getGlcodeForPurposeCode7MinorCode() {
		Query query =HibernateUtil.getCurrentSession().createSQLQuery("select substr(glcode,0,"+minorCodeLength+") from chartofaccounts where purposeid=7");
		List list = query.list();
		String glCode = "";
		if(list.get(0) != null)
			glCode = list.get(0).toString();
		return glCode;
	}
	/*For Detailed*/
	private String getGlcodeForPurposeCode7DetailedCode() {
		Query query =HibernateUtil.getCurrentSession().createSQLQuery("select substr(glcode,0,"+detailCodeLength+") from chartofaccounts where purposeid=7");
		List list = query.list();
		String glCode = "";
		if(list.get(0) != null)
			glCode = list.get(0).toString();
		return glCode;
	}

	private void populatePreviousYearTotalsForSchedule(Statement balanceSheet, String filterQuery, Date toDate,Date fromDate,String majorCode,Character type) {
		String formattedToDate = "";
		if("Yearly".equalsIgnoreCase(balanceSheet.getPeriod()))
		{	
			Calendar cal=Calendar.getInstance();
			cal.setTime(fromDate);
			cal.add(Calendar.DATE, -1);
			formattedToDate = balanceSheetService.getFormattedDate(cal.getTime());
		}
		else
			formattedToDate = balanceSheetService.getFormattedDate(balanceSheetService.getPreviousYearFor(toDate));
		StringBuffer qry=new StringBuffer(512);
		qry.append("select sum(debitamount)-sum(creditamount),c.glcode from generalledger g,chartofaccounts c,voucherheader v   " );
		if(balanceSheet.getDepartment()!=null && balanceSheet.getDepartment().getId()!=-1)
			qry.append(", VoucherMis mis ");		
		qry.append(" where  v.id=g.voucherheaderid and c.id=g.glcodeid and v.status not in("+voucherStatusToExclude+") " );
		if(balanceSheet.getDepartment()!=null && balanceSheet.getDepartment().getId()!=-1)
			qry.append(" and v.id= mis.voucherheaderid ");	
		qry.append(" AND v.voucherdate <= '"+formattedToDate+"' and v.voucherdate >='"+balanceSheetService.getFormattedDate(balanceSheetService.getPreviousYearFor(fromDate))+
				"' and c.glcode in (select distinct coad.glcode from chartofaccounts coa2, schedulemapping s " +
				",chartofaccounts coad where s.id=coa2.scheduleid and coa2.classification=2 and s.reporttype = 'BS'" +
				" and coa2.glcode=SUBSTR(coad.glcode,0,"+minorCodeLength+") and coad.classification=4 and coad.majorcode='"+majorCode+"')  and c.majorcode='"+majorCode+"' and c.classification=4 "+filterQuery+ " group by c.glcode");
		Query query =HibernateUtil.getCurrentSession().createSQLQuery(qry.toString());
		List<Object[]> result = query.list();
		for (Object[] row : result) {  
			for (int index = 0; index < balanceSheet.size(); index++) {
				if(balanceSheet.get(index).getGlCode()!=null && row[1].toString().equalsIgnoreCase(balanceSheet.get(index).getGlCode())) {
					
					BigDecimal previousYearTotal = new BigDecimal(row[0].toString());
					if(LOGGER.isDebugEnabled())     LOGGER.debug(row[1]+"-----------------------------------"+previousYearTotal);
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
		List<Object[]> allGlCodes =  getAllDetailGlCodesForSubSchedule(majorCode,type,BS);
		//addRowForSchedule(balanceSheet, allGlCodes);
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
							if(LOGGER.isDebugEnabled())     LOGGER.debug(row[0]+"-----"+row[1]+"------------------------------"+total);
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
							if(LOGGER.isDebugEnabled())     LOGGER.debug(row[0]+"-----"+row[1]+"------------------------------"+amount);
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
	/*For detailed*/
	public void populateDataForAllSchedulesDetailed(Statement balanceSheet) {
		getAppConfigValueForRemoveEntrysWithZeroAmount();
		voucherStatusToExclude = getAppConfigValueFor("finance", "statusexcludeReport");
		minorCodeLength = Integer.valueOf(balanceSheetService.getAppConfigValueFor(Constants.EGF,"coa_minorcode_length"));
		majorCodeLength = Integer.valueOf(balanceSheetService.getAppConfigValueFor(Constants.EGF,"coa_majorcode_length"));
		detailCodeLength = Integer.valueOf(balanceSheetService.getAppConfigValueFor(Constants.EGF,"coa_detailcode_length"));
		Date fromDate = balanceSheetService.getFromDate(balanceSheet);
		Date toDate = balanceSheetService.getToDate(balanceSheet);
		List<Fund> fundList = balanceSheet.getFunds();
		populateCurrentYearAmountForAllSchedulesDetailed(balanceSheet,fundList,amountPerFundQueryForAllSchedulesDetailed(balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate,BS));
		addCurrentOpeningBalancePerFund(balanceSheet, fundList,balanceSheetService.getTransactionQuery(balanceSheet));
		populatePreviousYearTotalsForScheduleForAllSchedulesDetailed(balanceSheet,balanceSheetService.getFilterQuery(balanceSheet),toDate,fromDate);
		addOpeningBalanceForPreviousYear(balanceSheet, balanceSheetService.getTransactionQuery(balanceSheet), fromDate);
		balanceSheetService.addExcessIEForCurrentYear(balanceSheet, fundList, getGlcodeForPurposeCode7DetailedCode(), balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate);
		balanceSheetService.addExcessIEForPreviousYear(balanceSheet, fundList, getGlcodeForPurposeCode7DetailedCode(), balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate);
		balanceSheetService.removeFundsWithNoData(balanceSheet);
		balanceSheetService.computeCurrentYearTotals(balanceSheet,Constants.LIABILITIES,Constants.ASSETS);
		computeAndAddTotals(balanceSheet);
		computeAndAddTotalsForSchedules(balanceSheet);
		if(removeEntrysWithZeroAmount.equalsIgnoreCase("Yes")){
			balanceSheetService.removeScheduleEntrysWithZeroAmount(balanceSheet);
			}
	}
	
	public void populateDataForAllSchedules(Statement balanceSheet) {
		getAppConfigValueForRemoveEntrysWithZeroAmount();
		voucherStatusToExclude = getAppConfigValueFor("finance", "statusexcludeReport");
		minorCodeLength = Integer.valueOf(balanceSheetService.getAppConfigValueFor(Constants.EGF,"coa_minorcode_length"));
		majorCodeLength = Integer.valueOf(balanceSheetService.getAppConfigValueFor(Constants.EGF,"coa_majorcode_length"));
		detailCodeLength = Integer.valueOf(balanceSheetService.getAppConfigValueFor(Constants.EGF,"coa_detailcode_length"));
		Date fromDate = balanceSheetService.getFromDate(balanceSheet);
		Date toDate = balanceSheetService.getToDate(balanceSheet);
		List<Fund> fundList = balanceSheet.getFunds();
		populateCurrentYearAmountForAllSchedules(balanceSheet,fundList,amountPerFundQueryForAllSchedules(balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate,BS));
		addCurrentOpeningBalancePerFund(balanceSheet, fundList,balanceSheetService.getTransactionQuery(balanceSheet));
		populatePreviousYearTotalsForScheduleForAllSchedules(balanceSheet,balanceSheetService.getFilterQuery(balanceSheet),toDate,fromDate);
		addOpeningBalanceForPreviousYear(balanceSheet, balanceSheetService.getTransactionQuery(balanceSheet), fromDate);
		balanceSheetService.addExcessIEForCurrentYear(balanceSheet, fundList, getGlcodeForPurposeCode7MinorCode(), balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate);
		balanceSheetService.addExcessIEForPreviousYear(balanceSheet, fundList, getGlcodeForPurposeCode7MinorCode(), balanceSheetService.getFilterQuery(balanceSheet), toDate, fromDate);
		balanceSheetService.removeFundsWithNoData(balanceSheet);
		balanceSheetService.computeCurrentYearTotals(balanceSheet,Constants.LIABILITIES,Constants.ASSETS);
		computeAndAddTotals(balanceSheet);
		if(removeEntrysWithZeroAmount.equalsIgnoreCase("Yes")){
			balanceSheetService.removeScheduleEntrysWithZeroAmount(balanceSheet);
			}
	}
	
	private void populatePreviousYearTotalsForScheduleForAllSchedules(Statement balanceSheet,String filterQuery,
			Date toDate, Date fromDate) {
		Date formattedToDate = null;
		BigDecimal divisor = balanceSheet.getDivisor();
		if("Yearly".equalsIgnoreCase(balanceSheet.getPeriod()))
		{
            Calendar cal = Calendar.getInstance();
            cal.setTime(fromDate);
            cal.add(Calendar.DATE, -1);
            formattedToDate =cal.getTime();
        }			
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
								BigDecimal prevYrTotal=balanceSheet.get(index).getPreviousYearTotal();
								prevYrTotal = (prevYrTotal==null?BigDecimal.ZERO:prevYrTotal);
								balanceSheet.get(index).setPreviousYearTotal(prevYrTotal.add(amount));
							}
						}
					}
				}
			}
		}
	}
	
	/*for detailed*/
	private void populatePreviousYearTotalsForScheduleForAllSchedulesDetailed(Statement balanceSheet,String filterQuery,
			Date toDate, Date fromDate) {
		Date formattedToDate = null;
		BigDecimal divisor = balanceSheet.getDivisor();
		if("Yearly".equalsIgnoreCase(balanceSheet.getPeriod()))
		{
            Calendar cal = Calendar.getInstance();
            cal.setTime(fromDate);
            cal.add(Calendar.DATE, -1);
            formattedToDate =cal.getTime();
        }			
		else
			formattedToDate = balanceSheetService.getPreviousYearFor(toDate);
		List<Object[]> resultMap = amountPerFundQueryForAllSchedulesDetailed(filterQuery, formattedToDate, balanceSheetService.getPreviousYearFor(fromDate),BS);
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
								BigDecimal prevYrTotal=balanceSheet.get(index).getPreviousYearTotal();
								prevYrTotal = (prevYrTotal==null?BigDecimal.ZERO:prevYrTotal);
								balanceSheet.get(index).setPreviousYearTotal(prevYrTotal.add(amount));
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
									balanceSheet.get(index).getFundWiseAmount().put(fundNameForId, amount);
								else
									balanceSheet.get(index).getFundWiseAmount().put(fundNameForId, balanceSheet.get(index).getFundWiseAmount().get(fundNameForId).add(amount));
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
	
	/*for detailed*/
	private void populateCurrentYearAmountForAllSchedulesDetailed(Statement balanceSheet, List<Fund> fundList, List<Object[]> currentYearAmounts) {
		BigDecimal divisor = balanceSheet.getDivisor();
		Map<String, Schedules> scheduleToGlCodeMap = getScheduleToGlCodeMapDetailed(BS,"('A','L')");
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
									balanceSheet.get(index).getFundWiseAmount().put(fundNameForId, amount);
								else
									balanceSheet.get(index).getFundWiseAmount().put(fundNameForId, balanceSheet.get(index).getFundWiseAmount().get(fundNameForId).add(amount));
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
			balanceSheet.add(new StatementEntry("","Schedule Total","",null,null,true));
		}
	}
private void getAppConfigValueForRemoveEntrysWithZeroAmount(){
	try{
		List<AppConfigValues> configValues =appConfigValuesService.
				getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,FinancialConstants.REMOVE_ENTRIES_WITH_ZERO_AMOUNT_IN_REPORT); 
		
		for (AppConfigValues appConfigVal : configValues) {
			removeEntrysWithZeroAmount = appConfigVal.getValue();
				 }
		} catch (Exception e) {
			 throw new EGOVRuntimeException("Appconfig value for remove entries with zero amount in report is not defined in the system");
		}
}
	public String getRemoveEntrysWithZeroAmount() {
		return removeEntrysWithZeroAmount;
	}

	public void setRemoveEntrysWithZeroAmount(String removeEntrysWithZeroAmount) {
		this.removeEntrysWithZeroAmount = removeEntrysWithZeroAmount;
	}

}
