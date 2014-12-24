package org.egov.web.actions.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;

public class Statement {
	String period;
	CFinancialYear financialYear;
	Date asOndate;
	String currency;
	DepartmentImpl department;
	Functionary functionary;
	CFunction function;
	BoundaryImpl field;
	List<Fund> fundList=new ArrayList<Fund>();
	private List<StatementEntry> entries = new ArrayList<StatementEntry>();
	
	public void setEntries(List<StatementEntry> entries) {
		this.entries = entries;
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
	}
	public void setDepartment(DepartmentImpl department) {
		this.department = department;
	}
	public void setFunctionary(Functionary functionary) {
		this.functionary = functionary;
	}
	public void setFunction(CFunction function) {
		this.function = function;
	}
	public void setField(BoundaryImpl field) {
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
	public DepartmentImpl getDepartment() {
		return department;
	}
	public Functionary getFunctionary() {
		return functionary;
	}
	public CFunction getFunction() {
		return function;
	}
	public BoundaryImpl getField() {
		return field;
	}
	public List<Fund> getFunds() {
		return fundList;
	}
	public void setFunds(List<Fund> list) {
		this.fundList = list;
	}
	
	public void add(StatementEntry entry){
		this.entries.add(entry);
	}
	
	public List<StatementEntry> getEntries(){
		return entries;
	}
	public int size() {
		return entries.size();
	}
	public StatementEntry get(int index) {
		return entries.get(index);
	}
	public void addAll(Statement balanceSheet) {
		entries.addAll(balanceSheet.getEntries());
	}
	
	public BigDecimal getDivisor() {
		if("Thousands".equalsIgnoreCase(currency))
			return new BigDecimal(1000);
		if("Lakhs".equalsIgnoreCase(currency))
			return new BigDecimal(100000);
		return BigDecimal.ONE;
	}
	
	public boolean containsBalanceSheetEntry(String glCode) {
		if(glCode == null) return false;
		for (StatementEntry balanceSheetEntry : getEntries()) {
			if(balanceSheetEntry.getGlCode() != null && glCode.equals(balanceSheetEntry.getGlCode()))
				return true;
		}
		return false;
	}

}
