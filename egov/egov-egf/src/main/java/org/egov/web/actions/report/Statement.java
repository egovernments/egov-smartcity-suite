package org.egov.web.actions.report;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;

public class Statement {
	String period;
	CFinancialYear financialYear;
	Date asOndate;
	Date fromDate;
	Date toDate;
	String currency;
	BigDecimal currencyInAmount;
	Department department;
	Functionary functionary;
	CFunction function;
	Boundary field;
	Fund fund;
	List<Fund> fundList=new ArrayList<Fund>();
	private List<IEStatementEntry> ieEntries = new ArrayList<IEStatementEntry>();
	private List<StatementEntry> entries = new ArrayList<StatementEntry>();

	public void setEntries(List<StatementEntry> entries) {
		this.entries = entries;
	}
	public List<IEStatementEntry> getIeEntries() {
		return ieEntries;
	}
	public void setIeEntries(List<IEStatementEntry> ieEntries) {
		this.ieEntries = ieEntries;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public void setFinancialYear(CFinancialYear financialYear) {
		this.financialYear = financialYear;
	}
	public void setAsOndate(Date asOndate) {
		this.asOndate = asOndate;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
		if(this.currency.equalsIgnoreCase("rupees"))
			this.currencyInAmount = new BigDecimal(1);
		if(this.currency.equalsIgnoreCase("thousands"))
			this.currencyInAmount = new BigDecimal(1000);
		if(this.currency.equalsIgnoreCase("lakhs"))
			this.currencyInAmount = new BigDecimal(100000);
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public void setFunctionary(Functionary functionary) {
		this.functionary = functionary;
	}
	public void setFunction(CFunction function) {
		this.function = function;
 	}
	public void setField(Boundary field) {
		this.field = field;
	}
	public String getPeriod() {
		return period;
	}
	public CFinancialYear getFinancialYear() {
		return financialYear;
	}
	public Date getAsOndate() {
		return asOndate;
	}
	public String getCurrency() {
		return currency;
	}
	public Department getDepartment() {
		return department;
	}
	public Functionary getFunctionary() {
		return functionary;
	}
	public CFunction getFunction() {
		return function;
	}
	public Boundary getField() {
		return field;
	}
	public List<Fund> getFunds() {
		return fundList;
	}
	public Fund getFund() {
		return fund;
	}
	public void setFund(Fund fund) {
		this.fund = fund;
	}
	public void setFunds(List<Fund> list) {
		this.fundList = list;
	}
	
	public void add(StatementEntry entry){
		this.entries.add(entry);
	}
	
	public void addIE(IEStatementEntry entry){
		this.ieEntries.add(entry);
	}
	
	public List<StatementEntry> getEntries(){
		return entries;
	}
	public int size() {
		return entries.size();
	}
	public int sizeIE() {
		return ieEntries.size();
	}
	public StatementEntry get(int index) {
		return entries.get(index);
	}
	
	public void addAll(Statement balanceSheet) {
		entries.addAll(balanceSheet.getEntries());
	}
	public void addAllIE(Statement balanceSheet) {
		ieEntries.addAll(balanceSheet.getIeEntries());
	}
	public IEStatementEntry getIE(int index) {
		return ieEntries.get(index);
	}
	
	public BigDecimal getDivisor() {
		if("Thousands".equalsIgnoreCase(currency))
			return new BigDecimal(1000);
		if("Lakhs".equalsIgnoreCase(currency))
			return new BigDecimal(100000);
		return BigDecimal.ONE;
	}
	public boolean containsStatementEntryScheduleNo(String scheduleNo) {
		if(scheduleNo == null) return false;
		for (StatementEntry StatementEntryObj : getEntries()) {
			if(StatementEntryObj.getScheduleNo() != null && scheduleNo.equals(StatementEntryObj.getScheduleNo()))
				return true;
		}
		return false;
	}
	
	public boolean containsStatementEntryOfDetailedCode(String glcode) {
		if(glcode == null) return false;
		for (StatementEntry StatementEntryObj : getEntries()) {
			if(StatementEntryObj.getGlCode() != null && glcode.equals(StatementEntryObj.getGlCode()))
				return true;
		}
		return false;
	}
	
	public boolean containsBalanceSheetEntry(String glCode) {
		if(glCode == null) return false;
		for (StatementEntry balanceSheetEntry : getEntries()) {
			if(balanceSheetEntry.getGlCode() != null && glCode.equals(balanceSheetEntry.getGlCode()))
				return true;
		}
		return false;
	}
	public boolean containsIEStatementEntry(String glCode) {
		if(glCode == null) return false;
		for (IEStatementEntry balanceSheetEntry :getIeEntries()) {
			if(balanceSheetEntry.getGlCode() != null && glCode.equals(balanceSheetEntry.getGlCode()))
				return true;
		}
		return false;
	}
	public boolean containsMajorCodeEntry(String majorcode) {
		if(majorcode == null) return false;            
		for (IEStatementEntry balanceSheetEntry :getIeEntries()) {
			if(balanceSheetEntry.getMajorCode() != null && majorcode.equals(balanceSheetEntry.getMajorCode()))
				return true;
		}
		return false;
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
	public BigDecimal getCurrencyInAmount() {
		return currencyInAmount;
	}
	
	
	
}
