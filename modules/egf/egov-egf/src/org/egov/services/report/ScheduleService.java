package org.egov.services.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.Constants;
import org.egov.web.actions.report.Statement;
import org.egov.web.actions.report.StatementEntry;
import org.hibernate.Query;

public abstract class ScheduleService extends PersistenceService{
	static final BigDecimal NEGATIVE = new BigDecimal("-1");
	GenericHibernateDaoFactory genericDao;
	int minorCodeLength;
	int majorCodeLength;
	String voucherStatusToExclude;

	Map<String, Schedules> getScheduleToGlCodeMap(String reportType,String coaType) {
		Query query = getSession().createSQLQuery("select distinct coa.glcode,s.schedule,s.schedulename," +
				"coa.type,coa.name from chartofaccounts coa, schedulemapping s where s.id=coa.scheduleid and " +
				"coa.classification=2 and s.reporttype = '"+reportType+"' and coa.type in "+coaType+" " +
				"order by coa.glcode");
		List<Object[]> results = query.list();
		Map<String,Schedules> scheduleMap = new LinkedHashMap<String, Schedules>();
		for (Object[] row : results) {
			if(!scheduleMap.containsKey(row[1].toString())) scheduleMap.put(row[1].toString(), new Schedules(row[1].toString(),row[2].toString()));
			scheduleMap.get(row[1].toString()).addChartOfAccount(new ChartOfAccount(row[0].toString(),row[3].toString(),row[4].toString()));
		}
		return scheduleMap;
	}

	List<Object[]> getAllGlCodesForAllSchedule(String reportType,String coaType) {
		Query query = getSession().createSQLQuery("select distinct coa.majorcode,s.schedule,s.schedulename," +
				"coa.type from chartofaccounts coa, schedulemapping s where s.id=coa.scheduleid and " +
				"coa.classification=2 and s.reporttype = '"+reportType+"' and coa.type in "+coaType+" " +
				"group by coa.majorcode,s.schedule,s.schedulename,coa.type order by coa.majorcode");
		return query.list();
	}

	List<Object[]> amountPerFundQueryForAllSchedules(String filterQuery, Date toDate, Date fromDate,String reportType) {
		String voucherStatusToExclude = getAppConfigValueFor("finance", "statusexcludeReport");
		Query query = getSession().createSQLQuery("select sum(debitamount)-sum(creditamount),v.fundid,substr(c.glcode,0,"+minorCodeLength+")," +
				"c.name from generalledger g,chartofaccounts c,voucherheader v ,vouchermis mis where v.id=mis.voucherheaderid " +
				"and v.id=g.voucherheaderid and c.id=g.glcodeid and v.status not in("+voucherStatusToExclude+")  AND v.voucherdate <= '"+
				getFormattedDate(toDate)+"' and v.voucherdate >='"+getFormattedDate(fromDate)+
				"' and substr(c.glcode,0,"+minorCodeLength+") in (select distinct coa2.glcode from chartofaccounts coa2, " +
						"schedulemapping s where s.id=coa2.scheduleid and coa2.classification=2 and s.reporttype = '"+reportType+"') "+filterQuery+
						" group by v.fundid,substr(c.glcode,0,"+minorCodeLength+"),c.name order by substr(c.glcode,0,"+minorCodeLength+")");
		return query.list();
	}
	
	public String getAppConfigValueFor(String module,String key){
		return genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(module,key).get(0).getValue();
	}
	
	public String getFormattedDate(Date date){
		return Constants.DDMMYYYYFORMAT1.format(date);
	}

	void addRowToStatement(Statement statement, Object[] row,String glCode) {
		StatementEntry entry = new StatementEntry();
		entry.setGlCode(glCode);
		entry.setAccountName(row[3].toString());
		statement.add(entry);
	}

	protected List<Object[]> getAllGlCodesForSubSchedule(String majorCode,Character type,String reportType){
		Query query = getSession().createSQLQuery("select distinct coa.glcode,coa.name,s.schedule,s.schedulename from chartofaccounts coa, " +
				"schedulemapping s where s.id=coa.scheduleid and coa.classification=2 and s.reporttype = '"+reportType+"' and coa.majorcode='"+
				majorCode+"' and coa.type='"+type+"' order by coa.glcode");
		return query.list();
	}

	List<Object[]> getRowsForGlcode(List<Object[]> resultMap, String glCode) {
		List<Object[]> rows = new ArrayList<Object[]>();
		for (Object[] row : resultMap) {
			if(row[2].toString().equalsIgnoreCase(glCode))
				rows.add(row);
		}
		return rows;		
	}

	protected void addRowForSchedule(Statement statement,List<Object[]> allGlCodes) {
		if(!allGlCodes.isEmpty()){
			statement.add(new StatementEntry("Schedule "+allGlCodes.get(0)[2].toString()+":",allGlCodes.get(0)[3].toString(),"",null,null,true));
		}
	}

	boolean contains(List<Object[]> result,String glCode){
		for (Object[] row : result) {
			if(row[2].toString().equalsIgnoreCase(glCode))
				return true;
		}
		return false;
	}

	void computeAndAddTotals(Statement statement) {
		BigDecimal currentTotal = BigDecimal.ZERO;
		BigDecimal previousTotal = BigDecimal.ZERO;
		for (int index = 0; index < statement.size(); index++) {
			if(statement.get(index).getCurrentYearTotal()!=null)
				currentTotal = currentTotal.add(statement.get(index).getCurrentYearTotal());
			if(statement.get(index).getPreviousYearTotal()!=null)
				previousTotal = previousTotal.add(statement.get(index).getPreviousYearTotal());
		}
		statement.add(new StatementEntry(null,"Total","",previousTotal,currentTotal,true));
	}

	List<Object[]> currentYearAmountQuery(String filterQuery,Date toDate, Date fromDate, String majorCode,String reportType) {
		Query query = getSession().createSQLQuery("select sum(debitamount)-sum(creditamount),v.fundid,substr(c.glcode,0,"+minorCodeLength+") " +
				"from generalledger g,chartofaccounts c,voucherheader v ,vouchermis mis where v.id=mis.voucherheaderid " +
				"and v.id=g.voucherheaderid and c.id=g.glcodeid and v.status not in("+voucherStatusToExclude+")  AND v.voucherdate <= '"
				+getFormattedDate(toDate)+"' and v.voucherdate >='"+getFormattedDate(fromDate)+"' " +
				"and substr(c.glcode,0,"+minorCodeLength+") in (select distinct coa2.glcode from chartofaccounts coa2, schedulemapping s " +
				"where s.id=coa2.scheduleid and coa2.classification=2 and s.reporttype = '"+reportType+"')  and c.majorcode='"+majorCode+"' "+filterQuery+
				" group by v.fundid,substr(c.glcode,0,"+minorCodeLength+") order by substr(c.glcode,0,"+minorCodeLength+")");
		return query.list();
	}

	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}

}

class ChartOfAccount{
	public final String glCode;
	public final String type;
	public final String name;
	public ChartOfAccount(String glCode,String type, String name) {
		this.glCode = glCode;
		this.type = type;
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		return  31  + ((glCode == null) ? 0 : glCode.hashCode());
	}
	
	@Override
	public boolean equals(Object obj) {
		try{
			ChartOfAccount other = (ChartOfAccount) obj;
			return glCode.equals(other.glCode);
		}catch(Exception e){
			return false;
		}
	}
}

class Schedules{
	public final String scheduleNumber;
	public final String scheduleName;
	
	public final Set<ChartOfAccount> chartOfAccount = new HashSet<ChartOfAccount>();

	public Schedules(String scheduleNumber,String scheduleName) {
		this.scheduleNumber = scheduleNumber;
		this.scheduleName = scheduleName;
	}
	public boolean contains(String glCode) {
		return chartOfAccount.contains(new ChartOfAccount(glCode,null,null));
	}
	public String getCoaName(String glCode) {
		for (ChartOfAccount coa : chartOfAccount) {
			if(glCode.equalsIgnoreCase(coa.glCode))
				return coa.name;
		}
		return "";
	}
	public void addChartOfAccount(ChartOfAccount s){
		this.chartOfAccount.add(s);
	}
}