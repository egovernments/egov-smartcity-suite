package org.egov.works.web.actions.report;

import org.egov.lib.admbndry.Boundary;

public class GisReportBean {

	Boundary boundary;
	Double budgetAvailable;
	Double budgetConsumed;

	public Boundary getBoundary() {
		return boundary;
	}
	public void setBoundary(Boundary boundary) {
		this.boundary = boundary;
	}
	public Double getBudgetAvailable() {
		return budgetAvailable;
	}
	public void setBudgetAvailable(Double budgetAvailable) {
		this.budgetAvailable = budgetAvailable;
	}
	public Double getBudgetConsumed() {
		return budgetConsumed;
	}
	public void setBudgetConsumed(Double budgetConsumed) {
		this.budgetConsumed = budgetConsumed;
	}
}
