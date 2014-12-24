package org.egov.pims.empLeave.model;

import org.egov.commons.CFinancialYear;

public class FinYrandDate implements java.io.Serializable
{
	java.util.Date fromDate;
	java.util.Date toDate;
	CFinancialYear financial;
	public CFinancialYear getFinancial() {
		return financial;
	}
	public void setFinancial(CFinancialYear financial) {
		this.financial = financial;
	}
	public java.util.Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(java.util.Date fromDate) {
		this.fromDate = fromDate;
	}
	public java.util.Date getToDate() {
		return toDate;
	}
	public void setToDate(java.util.Date toDate) {
		this.toDate = toDate;
	}
	
}
