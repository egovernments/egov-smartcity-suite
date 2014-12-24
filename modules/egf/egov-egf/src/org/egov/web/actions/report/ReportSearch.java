package org.egov.web.actions.report;

import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;

public class ReportSearch {
	private DepartmentImpl department;
	private Fund fund;
	private CFunction function;
	private Functionary functionary;
	private Fundsource fundsource;
	private BoundaryImpl  field;
	private Scheme scheme;
	private SubScheme subScheme;
	private String startDate;
	private String endDate;
	private String incExp;
	public DepartmentImpl getDepartment() {
		return department;
	}
	public void setDepartment(DepartmentImpl department) {
		this.department = department;
	}
	public Fund getFund() {
		return fund;
	}
	public void setFund(Fund fund) {
		this.fund = fund;
	}
	public CFunction getFunction() {
		return function;
	}
	public void setFunction(CFunction function) {
		this.function = function;
	}
	public Functionary getFunctionary() {
		return functionary;
	}
	public void setFunctionary(Functionary functionary) {
		this.functionary = functionary;
	}
	public Fundsource getFundsource() {
		return fundsource;
	}
	public void setFundsource(Fundsource fundsource) {
		this.fundsource = fundsource;
	}
	public BoundaryImpl getField() {
		return field;
	}
	public void setField(BoundaryImpl field) {
		this.field = field;
	}
	public Scheme getScheme() {
		return scheme;
	}
	public void setScheme(Scheme scheme) {
		this.scheme = scheme;
	}
	public SubScheme getSubScheme() {
		return subScheme;
	}
	public void setSubScheme(SubScheme subScheme) {
		this.subScheme = subScheme;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getIncExp() {
		return incExp;
	}
	public void setIncExp(String incExp) {
		this.incExp = incExp;
	}
}
