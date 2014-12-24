package org.egov.payroll.services.payslipApprove;

import java.math.BigDecimal;

import org.egov.model.recoveries.Recovery;


public class TdsAmount {
		
	Recovery tds;
	BigDecimal amount;
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Recovery getTds() {
		return tds;
	}
	public void setTds(Recovery tds) {
		this.tds = tds;
	}
	
	
	
}
