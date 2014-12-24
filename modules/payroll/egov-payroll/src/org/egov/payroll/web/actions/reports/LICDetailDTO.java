package org.egov.payroll.web.actions.reports;

import java.math.BigDecimal;

public class LICDetailDTO {

	private String policyNo;
	private BigDecimal licAmount;

	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public BigDecimal getLicAmount() {
		return licAmount;
	}
	public void setLicAmount(BigDecimal licAmount) {
		this.licAmount = licAmount;
	}
}
