package org.egov.eb.domain.master.bean;

import java.math.BigDecimal;
import java.util.Date;

public class RtgsPaymentReportBean {
	
	private String rtgsDate;
	private String rtgsNumber;
	private String region;
	private BigDecimal paymentAmount;
	private String month;
	private BigDecimal IOBPaidAmount;
	private BigDecimal unPaidAmount;
	private Integer numOfBillsUnpaid;
	private String finYearRange;
	
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getRtgsDate() {
		return rtgsDate;
	}
	public void setRtgsDate(String rtgsDate) {
		this.rtgsDate = rtgsDate;
	}
	public String getRtgsNumber() {
		return rtgsNumber;
	}
	public void setRtgsNumber(String rtgsNumber) {
		this.rtgsNumber = rtgsNumber;
	}
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public BigDecimal getIOBPaidAmount() {
		return IOBPaidAmount;
	}
	public void setIOBPaidAmount(BigDecimal iOBPaidAmount) {
		IOBPaidAmount = iOBPaidAmount;
	}
	public BigDecimal getUnPaidAmount() {
		return unPaidAmount;
	}
	public void setUnPaidAmount(BigDecimal unPaidAmount) {
		this.unPaidAmount = unPaidAmount;
	}
	public Integer getNumOfBillsUnpaid() {
		return numOfBillsUnpaid;
	}
	public void setNumOfBillsUnpaid(Integer numOfBillsUnpaid) {
		this.numOfBillsUnpaid = numOfBillsUnpaid;
	}
	public String getFinYearRange() {
		return finYearRange;
	}
	public void setFinYearRange(String finYearRange) {
		this.finYearRange = finYearRange;
	}

}
