package org.egov.ptis.bean;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author subhash
 *
 */

public class RecoveryInfo {
	private Date fromDate;
	private Date toDate;
	private BigDecimal genTax = ZERO;
	private BigDecimal waterTax = ZERO;
	private BigDecimal fireTax = ZERO;
	private BigDecimal sewerageTax = ZERO;
	private BigDecimal lightTax = ZERO;
	private BigDecimal eduCess = ZERO;
	private BigDecimal bigBldgTax = ZERO;
	private BigDecimal egsCess = ZERO;
	private BigDecimal totCurrYearColl = ZERO;
	private BigDecimal totPrevYearColl = ZERO;
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
	public BigDecimal getGenTax() {
		return genTax;
	}
	public void setGenTax(BigDecimal genTax) {
		this.genTax = genTax;
	}
	public BigDecimal getWaterTax() {
		return waterTax;
	}
	public void setWaterTax(BigDecimal waterTax) {
		this.waterTax = waterTax;
	}
	public BigDecimal getFireTax() {
		return fireTax;
	}
	public void setFireTax(BigDecimal fireTax) {
		this.fireTax = fireTax;
	}
	public BigDecimal getSewerageTax() {
		return sewerageTax;
	}
	public void setSewerageTax(BigDecimal sewerageTax) {
		this.sewerageTax = sewerageTax;
	}
	public BigDecimal getLightTax() {
		return lightTax;
	}
	public void setLightTax(BigDecimal lightTax) {
		this.lightTax = lightTax;
	}
	public BigDecimal getEduCess() {
		return eduCess;
	}
	public void setEduCess(BigDecimal eduCess) {
		this.eduCess = eduCess;
	}
	public BigDecimal getBigBldgTax() {
		return bigBldgTax;
	}
	public void setBigBldgTax(BigDecimal bigBldgTax) {
		this.bigBldgTax = bigBldgTax;
	}
	public BigDecimal getEgsCess() {
		return egsCess;
	}
	public void setEgsCess(BigDecimal egsCess) {
		this.egsCess = egsCess;
	}
	public BigDecimal getTotCurrYearColl() {
		return totCurrYearColl;
	}
	public void setTotCurrYearColl(BigDecimal totCurrYearColl) {
		this.totCurrYearColl = totCurrYearColl;
	}
	public BigDecimal getTotPrevYearColl() {
		return totPrevYearColl;
	}
	public void setTotPrevYearColl(BigDecimal totPrevYearColl) {
		this.totPrevYearColl = totPrevYearColl;
	}
	
}
