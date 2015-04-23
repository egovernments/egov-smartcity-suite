
/* 
 * Created on April 20, 2006
 * @author Prabhu
 */
package com.exilant.eGov.src.reports;

public class ReceiptPaymentBean
{
	private String period;
	private String fundId;
	private String tempFund;
	private String month;
	private String finYear;
	private String finYearId;
	private String rupees;
	private String prevPeriodEndDate;
	private String prevPeriodStartDate;
	private String toDate;



// for displaying  in the report (Using display tag)
	private String paySchId;
	private String recSchId;
	private String accCodeRec;
	private String headAccRec;
	private String schNoRec;
	private String effFilter;
	private String generalRec;
	private String waterSupplyRec;
	private String enterpriseRec;
	private String currPeriodRec;
	private String prevPeriodRec;
	private String accCodePay;
	private String headAccPay;
	private String schNoPay;
	private String generalPay;
	private String waterSupplyPay;
	private String enterprisePay;
	private String currPeriodPay;
	private String prevPeriodPay;
	private String scheduleNo;
	


	/**
	 *
	 */
	public ReceiptPaymentBean() {

		this.period = "";
		this.month = "";
		this.finYear= "";
		this.finYearId="";
		this.rupees= "";
		this.prevPeriodEndDate="";

		this.paySchId="";
		this.recSchId="";
		this.accCodeRec = "";
		this.headAccRec = "";
		this.schNoRec="";
		this.generalRec="";
		this.waterSupplyRec = "";
		this.enterpriseRec="";
		this.prevPeriodRec = "";
		this.accCodePay = "";
		this.headAccPay = "";
		this.schNoPay="";
		this.generalPay = "";
		this.waterSupplyPay = "";
		this.enterprisePay = "";
		this.prevPeriodPay = "";
		this.scheduleNo = "";
 
	}

	public String getPaySchId() {
		return paySchId;
	}

	public void setPaySchId(String paySchId) {
		this.paySchId = paySchId;
	}
	public String getTempFund() {
		return tempFund;
	}

	public void setTempFund(String tempFund) {
		this.tempFund = tempFund;
	}

	public String getRecSchId() {
		return recSchId;
	}

	public void setRecSchId(String recSchId) {
		this.recSchId = recSchId;
	}
	
	public String getEffFilter() {
		return effFilter;
	}
	public void setEffFilter(String effFilter) {
		this.effFilter = effFilter;
	}
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month= month;
	}

	public String getFinYear() {
		return finYear;
	}

	public void setFinYear(String finYear) {
		this.finYear= finYear;
	}
	public String getFinYearId() {
		return finYearId;
	}

	public void setFinYearId(String finYearId) {
		this.finYearId= finYearId;
	}
	public String getFundId() {
		return fundId;
	}

	public void setFundId(String fundId) {
		this.fundId = fundId;
	}

	public String getRupees() {
		return rupees;
	}

	public void setRupees(String rupees) {
		this.rupees = rupees;
	}
	
	public String getPrevPeriodEndDate() {
		return prevPeriodEndDate;
	}

	public void setPrevPeriodEndDate(String prevPeriodEndDate) {
		this.prevPeriodEndDate = prevPeriodEndDate;
	}

// Getter and Setter methods for displaying the report

	public String getAccCodeRec() {
		return accCodeRec;
	}

	public void setAccCodeRec(String accCodeRec) {
		this.accCodeRec = accCodeRec;
	}


	public String getHeadAccRec() {
		return headAccRec;
	}

	public void setHeadAccRec(String headAccRec) {
		this.headAccRec = headAccRec;
	}
	public String getSchNoRec() {
		return schNoRec;
	}

	public void setSchNoRec(String schNoRec) {
		this.schNoRec = schNoRec;
	}

	public String getGeneralRec() {
		return generalRec;
	}

	public void setGeneralRec(String generalRec) {
		this.generalRec = generalRec;
	}
	public String getWaterSupplyRec() {
		return waterSupplyRec;
	}

	public void setWaterSupplyRec(String waterSupplyRec) {
		this.waterSupplyRec = waterSupplyRec;
	}
	
	public String getEnterpriseRec() {
		return enterpriseRec;
	}

	public void setEnterpriseRec(String enterpriseRec) {
		this.enterpriseRec = enterpriseRec;
	}
	public String getCurrPeriodRec() {
		return currPeriodRec;
	}

	public void setCurrPeriodRec(String currPeriodRec) {
		this.currPeriodRec = currPeriodRec;
	}

	public String getPrevPeriodRec() {
		return prevPeriodRec;
	}

	public void setPrevPeriodRec(String prevPeriodRec) {
		this.prevPeriodRec = prevPeriodRec;
	}

	public String getAccCodePay() {
			return accCodePay;
	}

	public void setAccCodePay(String accCodePay) {
		this.accCodePay = accCodePay;
	}


	public String getHeadAccPay() {
			return headAccPay;
	}

	public void setHeadAccPay(String headAccPay) {
			this.headAccPay = headAccPay;
	}
	
	public String getSchNoPay() {
		return schNoPay;
	}

	public void setSchNoPay(String schNoPay) {
		this.schNoPay = schNoPay;
	}

	public String getGeneralPay() {
		return generalPay;
	}

	public void setGeneralPay(String generalPay) {
		this.generalPay = generalPay;
	}
	public String getWaterSupplyPay() {
		return waterSupplyPay;
	}

	public void setWaterSupplyPay(String waterSupplyPay) {
		this.waterSupplyPay = waterSupplyPay;
	}
	
	public String getEnterprisePay() {
		return enterprisePay;
	}

	public void setEnterprisePay(String enterprisePay) {
		this.enterprisePay = enterprisePay;
	}

	public String getCurrPeriodPay() {
		return currPeriodPay;
	}

	public void setCurrPeriodPay(String currPeriodPay) {
		this.currPeriodPay = currPeriodPay;
	}
	
	public String getPrevPeriodPay() {
			return prevPeriodPay;
	}

	public void setPrevPeriodPay(String prevPeriodPay) {
			this.prevPeriodPay = prevPeriodPay;

	}

	/**
	 * @return the toDate
	 */
	public String getToDate()
	{
		return toDate;
	}

	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(String toDate)
	{
		this.toDate = toDate;
	}

	public String getPrevPeriodStartDate() {
		return prevPeriodStartDate;
	}

	public void setPrevPeriodStartDate(String prevPeriodStartDate) {
		this.prevPeriodStartDate = prevPeriodStartDate;
	}

	public String getScheduleNo() {
		return scheduleNo;
	}

	public void setScheduleNo(String scheduleNo) {
		this.scheduleNo = scheduleNo;
	}




}



