package org.egov.ptis.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * @author subhash
 *
 */

public class CollectionInfo {

	private String operator;
	private BigDecimal collByCash;
	private BigDecimal collByCheque;
	private BigDecimal otherColl;
	private BigDecimal eduEgsCurrColl;
	private BigDecimal eduEgsArrColl;
	private BigDecimal totalCurrColl;
	private BigDecimal totalArrColl;
	private BigDecimal grandTotal;
	private List<ReceiptInfo> rcptInfoList;
	public BigDecimal getCollByCash() {
		return collByCash;
	}
	public void setCollByCash(BigDecimal collByCash) {
		this.collByCash = collByCash;
	}
	public BigDecimal getCollByCheque() {
		return collByCheque;
	}
	public void setCollByCheque(BigDecimal collByCheque) {
		this.collByCheque = collByCheque;
	}
	public BigDecimal getOtherColl() {
		return otherColl;
	}
	public void setOtherColl(BigDecimal otherColl) {
		this.otherColl = otherColl;
	}
	public BigDecimal getEduEgsCurrColl() {
		return eduEgsCurrColl;
	}
	public void setEduEgsCurrColl(BigDecimal eduEgsCurrColl) {
		this.eduEgsCurrColl = eduEgsCurrColl;
	}
	public BigDecimal getEduEgsArrColl() {
		return eduEgsArrColl;
	}
	public void setEduEgsArrColl(BigDecimal eduEgsArrColl) {
		this.eduEgsArrColl = eduEgsArrColl;
	}
	public BigDecimal getTotalCurrColl() {
		return totalCurrColl;
	}
	public void setTotalCurrColl(BigDecimal totalCurrColl) {
		this.totalCurrColl = totalCurrColl;
	}
	public BigDecimal getTotalArrColl() {
		return totalArrColl;
	}
	public void setTotalArrColl(BigDecimal totalArrColl) {
		this.totalArrColl = totalArrColl;
	}
	public BigDecimal getGrandTotal() {
		return grandTotal;
	}
	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}
	public List<ReceiptInfo> getRcptInfoList() {
		return rcptInfoList;
	}
	public void setRcptInfoList(List<ReceiptInfo> rcptInfoList) {
		this.rcptInfoList = rcptInfoList;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
}
