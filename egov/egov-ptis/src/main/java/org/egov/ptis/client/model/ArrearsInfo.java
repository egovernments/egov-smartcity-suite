package org.egov.ptis.client.model;

import java.math.BigDecimal;

public class ArrearsInfo {

	private BigDecimal generalTax;
	private BigDecimal vacantLandTax;
	private BigDecimal libraryCess;
	private BigDecimal educationCess;
	private BigDecimal unauthorizedPenalty;
	private BigDecimal latePaymentPenalty;
	
	public BigDecimal getGeneralTax() {
		return generalTax;
	}
	public void setGeneralTax(BigDecimal generalTax) {
		this.generalTax = generalTax;
	}
	public BigDecimal getVacantLandTax() {
		return vacantLandTax;
	}
	public void setVacantLandTax(BigDecimal vacantLandTax) {
		this.vacantLandTax = vacantLandTax;
	}
	public BigDecimal getLibraryCess() {
		return libraryCess;
	}
	public void setLibraryCess(BigDecimal libraryCess) {
		this.libraryCess = libraryCess;
	}
	public BigDecimal getEducationCess() {
		return educationCess;
	}
	public void setEducationCess(BigDecimal educationCess) {
		this.educationCess = educationCess;
	}
	public BigDecimal getUnauthorizedPenalty() {
		return unauthorizedPenalty;
	}
	public void setUnauthorizedPenalty(BigDecimal unauthorizedPenalty) {
		this.unauthorizedPenalty = unauthorizedPenalty;
	}
	public BigDecimal getLatePaymentPenalty() {
		return latePaymentPenalty;
	}
	public void setLatePaymentPenalty(BigDecimal latePaymentPenalty) {
		this.latePaymentPenalty = latePaymentPenalty;
	}
}
