package org.egov.ptis.client.model;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
public class TaxDetails implements Serializable {
	private BigDecimal generalTax = BigDecimal.ZERO;
	private BigDecimal totalTax = BigDecimal.ZERO;
	private BigDecimal eduCess = BigDecimal.ZERO;
	private BigDecimal libCess = BigDecimal.ZERO;
	private BigDecimal sewarageTax = BigDecimal.ZERO;
	private BigDecimal pubServiceCharge = BigDecimal.ZERO;
	private BigDecimal penalty = BigDecimal.ZERO;
	private BigDecimal unAuthPenalty = BigDecimal.ZERO;
	private String demandYear;
	public BigDecimal getGeneralTax() {
		return generalTax;
	}
	public void setGeneralTax(BigDecimal generalTax) {
		this.generalTax = generalTax;
	}
	public BigDecimal getTotalTax() {
		return getGeneralTax().add(getEduCess()).add(getLibCess()).add(getSewarageTax()).add(getPenalty()).add(getUnAuthPenalty());
	}
	public void setTotalTax(BigDecimal totalTax) {
		this.totalTax = totalTax;
	}
	public BigDecimal getEduCess() {
		return eduCess;
	}
	public void setEduCess(BigDecimal eduCess) {
		this.eduCess = eduCess;
	}
	public BigDecimal getLibCess() {
		return libCess;
	}
	public void setLibCess(BigDecimal libCess) {
		this.libCess = libCess;
	}
	public BigDecimal getSewarageTax() {
		return sewarageTax;
	}
	public void setSewarageTax(BigDecimal sewarageTax) {
		this.sewarageTax = sewarageTax;
	}
	public BigDecimal getPubServiceCharge() {
		return pubServiceCharge;
	}
	public void setPubServiceCharge(BigDecimal pubServiceCharge) {
		this.pubServiceCharge = pubServiceCharge;
	}
	public BigDecimal getPenalty() {
		return penalty;
	}
	public void setPenalty(BigDecimal penalty) {
		this.penalty = penalty;
	}
	public BigDecimal getUnAuthPenalty() {
		return unAuthPenalty;
	}
	public void setUnAuthPenalty(BigDecimal unAuthPenalty) {
		this.unAuthPenalty = unAuthPenalty;
	}
	public String getDemandYear() {
		return demandYear;
	}
	public void setDemandYear(String demandYear) {
		this.demandYear = demandYear;
	}
	@Override
	public String toString() {
		return "TaxDetails [generalTax=" + generalTax + ", totalTax=" + totalTax + ", eduCess=" + eduCess + ", libCess="
				+ libCess + ", sewarageTax=" + sewarageTax + ", pubServiceCharge=" + pubServiceCharge + ", penalty="
				+ penalty + ", unAuthPenalty=" + unAuthPenalty + ", demandYear=" + demandYear + "]";
	}
}
