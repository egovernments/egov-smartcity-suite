package org.egov.web.actions.report;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.commons.CFinancialYear;
import org.egov.commons.Fund;

public class DepartmentwiseExpenditureReport {

	
	private String departmentName;    
	private Integer fundId;
	private String month;
	private Fund fund;
	private Date concurrenceDate;
	private Date fromDate;
	private Date toDate;
	private Long financialYearId;
	private CFinancialYear finyearObj=null;
	private String reportType;
	private String period ;
	private String assetCode;
	private Date previousYearConcurrenceGivenUptoDate;
	private Date currentYearConcurrenceGivenUptoDate;
	private Date previousYearConcurrenceGivenTillDate;
	Set<String> concurrenceDateSet=new LinkedHashSet<String>();
	Set<String> previousConcurrenceDateSet=new LinkedHashSet<String>();
	Map<String, Boolean> rowToBeRemoved = new HashMap<String, Boolean>();
	private String exportType;
	//private Map<String,DepartmentwiseExpenditureResult> currentyearDepartmentMap =new LinkedHashMap<String,DepartmentwiseExpenditureResult>();
	private List<DepartmentwiseExpenditureResult> currentyearDepartmentList=new LinkedList<DepartmentwiseExpenditureResult>();
	private List<DepartmentwiseExpenditureResult> previousyearDepartmentList=new LinkedList<DepartmentwiseExpenditureResult>();
	
	private BigDecimal 	concurrenceAmount;
	
	public void reset(){
		this.departmentName=null;
		this.fundId=null;
		this.month=null;
		this.fromDate=null;
		this.toDate=null;
		this.reportType=null;
		this.period=null;
	}
	
	public String getDepartmentName() {
		return departmentName;
	}
	
	
	public String getMonth() {
		return month;
	}
	public Date getConcurrenceDate() {
		return concurrenceDate;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	
	public String getReportType() {
		return reportType;
	}
		public BigDecimal getConcurrenceAmount() {
		return concurrenceAmount;
	}
	
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	    
	
	public void setMonth(String month) {
		this.month = month;
	}
	public void setConcurrenceDate(Date concurrenceDate) {
		this.concurrenceDate = concurrenceDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	
	public void setConcurrenceAmount(BigDecimal concurrenceAmount) {
		this.concurrenceAmount = concurrenceAmount;
	}
	public List<DepartmentwiseExpenditureResult> getCurrentyearDepartmentList() {
		return currentyearDepartmentList;
	}
	public void setCurrentyearDepartmentList(
			List<DepartmentwiseExpenditureResult> currentyearDepartmentList) {
		this.currentyearDepartmentList = currentyearDepartmentList;
	}
	public Set<String> getConcurrenceDateSet() {
		return concurrenceDateSet;
	}
	public void setConcurrenceDateSet(Set<String> concurrenceDateSet) {
		this.concurrenceDateSet = concurrenceDateSet;
	}
	
	public boolean containsDepartmentInResultList(String department,List<DepartmentwiseExpenditureResult> resList) {
		if(department == null) return false;
		for (DepartmentwiseExpenditureResult entry :resList) {
			if(department.equalsIgnoreCase(entry.getDepartmentNm()))
				return true;
		}
		return false;
	}
	public Integer getFundId() {
		return fundId;
	}
	public Long getFinancialYearId() {
		return financialYearId;
	}
	public void setFundId(Integer fundId) {
		this.fundId = fundId;
	}
	public void setFinancialYearId(Long financialYearId) {
		this.financialYearId = financialYearId;
	}
	public CFinancialYear getFinyearObj() {
		return finyearObj;
	}
	public void setFinyearObj(CFinancialYear finyearObj) {
		this.finyearObj = finyearObj;
	}
	public String getExportType() {
		return exportType;
	}
	public void setExportType(String exportType) {
		this.exportType = exportType;
	}
	
	public void addDepartmentToResultSet(DepartmentwiseExpenditureResult entry){
		//if(this.getFromDate())
		if(this.getPeriod().equalsIgnoreCase("current")){
			this.getCurrentyearDepartmentList().add(entry);
		}
		else if(this.getPeriod().equalsIgnoreCase("previous")){
			this.getPreviousyearDepartmentList().add(entry);
		}
	}
	
	public Date getRestrictedDepartmentDate(){
		final Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR, 2013);
		date.set(Calendar.MONTH, 02);
		date.set(Calendar.DATE, 31);
		//date.set(Calendar.DAY_OF_MONTH, date.getActualMaximum(Calendar.DAY_OF_MONTH));  
		return date.getTime();
	}
	
	public Date getPreviousDateFor(Date date) {
		
		GregorianCalendar previousDate = new GregorianCalendar();
		previousDate.setTime(date);
		int prevDt = previousDate.get(Calendar.DATE) - 1;
		previousDate.set(Calendar.DATE, prevDt);
		return previousDate.getTime();
	}

	public void addDepartmentwiseExistingEntry(DepartmentwiseExpenditureResult entry) {
		for(DepartmentwiseExpenditureResult obj:getCurrentyearDepartmentList() ){
			if(obj.getDepartmentNm().equals(entry.getDepartmentNm())){
				
			}
		}
		
	}
	public List<DepartmentwiseExpenditureResult> getPreviousyearDepartmentList() {
		return previousyearDepartmentList;
	}
	public void setPreviousyearDepartmentList(
			List<DepartmentwiseExpenditureResult> previousyearDepartmentList) {
		this.previousyearDepartmentList = previousyearDepartmentList;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public Set<String> getPreviousConcurrenceDateSet() {
		return previousConcurrenceDateSet;
	}
	public void setPreviousConcurrenceDateSet(Set<String> previousConcurrenceDateSet) {
		this.previousConcurrenceDateSet = previousConcurrenceDateSet;
	}
	
	public Date getStartOfMonth(){
		String year="";
		Date startDate=null;
		final Calendar date = Calendar.getInstance();
		if(this.month.equals("01") || this.month.equals("02")|| this.month.equals("03") ){
			year=this.getFinyearObj().getEndingDate().toString().substring(0,4);
		}else{
			year=this.getFinyearObj().getStartingDate().toString().substring(0,4);
		}
		
		date.set(Calendar.YEAR, Integer.parseInt(year));
		date.set(Calendar.MONTH, Integer.parseInt(this.month));
		date.set(Calendar.DATE, 1);
		date.set(Calendar.DAY_OF_MONTH, date.getActualMaximum(Calendar.DAY_OF_MONTH));  
		//date.setTime(date);
		return date.getTime();
	}
	public Date getEndMonth(){
		String year="";
		Date startDate=null;
		final Calendar date = Calendar.getInstance();
		if(this.month.equals("01") || this.month.equals("02")|| this.month.equals("03") ){
			year=this.getFinyearObj().getEndingDate().toString().substring(0,4);
		}else{
			year=this.getFinyearObj().getStartingDate().toString().substring(0,4);
		}
		
		date.set(Calendar.YEAR, Integer.parseInt(year));
		date.set(Calendar.MONTH, Integer.parseInt(this.month));
		date.set(Calendar.DATE, 1);
		date.set(Calendar.DAY_OF_MONTH, date.getActualMaximum(Calendar.DAY_OF_MONTH));  
		//date.setTime(date);
		return date.getTime();
	}
	public Date getPreviousYearConcurrenceGivenUptoDate() {
		return previousYearConcurrenceGivenUptoDate;
	}
	public Date getPreviousYearConcurrenceGivenTillDate() {
		return previousYearConcurrenceGivenTillDate;
	}
	public void setPreviousYearConcurrenceGivenUptoDate(
			Date previousYearConcurrenceGivenUptoDate) {
		this.previousYearConcurrenceGivenUptoDate = previousYearConcurrenceGivenUptoDate;
	}
	public void setPreviousYearConcurrenceGivenTillDate(
			Date previousYearConcurrenceGivenTillDate) {
		this.previousYearConcurrenceGivenTillDate = previousYearConcurrenceGivenTillDate;
	}
	public Date getCurrentYearConcurrenceGivenUptoDate() {
		return currentYearConcurrenceGivenUptoDate;
	}
	public void setCurrentYearConcurrenceGivenUptoDate(
			Date currentYearConcurrenceGivenUptoDate) {
		this.currentYearConcurrenceGivenUptoDate = currentYearConcurrenceGivenUptoDate;
	}

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public Fund getFund() {
		return fund;
	}

	public void setFund(Fund fund) {
		this.fund = fund;
	}

	public Map<String, Boolean> getRowToBeRemoved() {
		return rowToBeRemoved;
	}

	public void setRowToBeRemoved(Map<String, Boolean> rowToBeRemoved) {
		this.rowToBeRemoved = rowToBeRemoved;
	}

	
	

	
}
