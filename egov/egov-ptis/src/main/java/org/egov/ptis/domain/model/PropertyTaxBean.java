package org.egov.ptis.domain.model;

import java.math.BigDecimal;

public class PropertyTaxBean {
	private BigDecimal mrv;
	private BigDecimal arv;
	private BigDecimal currentTax;
	private BigDecimal arrearTax;
	private BigDecimal currentCollection;
	private BigDecimal arrearCollection;
	private BigDecimal advanceCollection;

	public BigDecimal getMrv() {
		return mrv;
	}

	public void setMrv(BigDecimal mrv) {
		this.mrv = mrv;
	}

	public BigDecimal getArv() {
		return arv;
	}

	public void setArv(BigDecimal arv) {
		this.arv = arv;
	}

	public BigDecimal getCurrentTax() {
		return currentTax;
	}

	public void setCurrentTax(BigDecimal currentTax) {
		this.currentTax = currentTax;
	}

	public BigDecimal getArrearTax() {
		return arrearTax;
	}

	public void setArrearTax(BigDecimal arrearTax) {
		this.arrearTax = arrearTax;
	}

	public BigDecimal getCurrentCollection() {
		return currentCollection;
	}

	public void setCurrentCollection(BigDecimal currentCollection) {
		this.currentCollection = currentCollection;
	}

	public BigDecimal getArrearCollection() {
		return arrearCollection;
	}

	public void setArrearCollection(BigDecimal arrearCollection) {
		this.arrearCollection = arrearCollection;
	}

	public BigDecimal getAdvanceCollection() {
		return advanceCollection;
	}

	public void setAdvanceCollection(BigDecimal advanceCollection) {
		this.advanceCollection = advanceCollection;
	}

}
