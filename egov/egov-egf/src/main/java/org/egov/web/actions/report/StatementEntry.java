package org.egov.web.actions.report;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatementEntry {
	private String glCode;
	private String accountName;
	private String scheduleNo;
	// FundCode is added  for RP report
	private String fundCode;
	private BigDecimal previousYearTotal = BigDecimal.ZERO;
	private BigDecimal currentYearTotal = BigDecimal.ZERO;
	private Map<String,BigDecimal> fundWiseAmount = new HashMap<String, BigDecimal>();
	private boolean displayBold = false;
	
	public boolean isDisplayBold() {
		return displayBold;
	}

	public void setDisplayBold(boolean displayBold) {
		this.displayBold = displayBold;
	}

	public StatementEntry(){}

	public void setGlCode(String accountCode) {
		this.glCode = accountCode;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setScheduleNo(String scheduleNo) {
		this.scheduleNo = scheduleNo;
	}

	public void setPreviousYearTotal(BigDecimal previousYearTotal) {
		this.previousYearTotal = previousYearTotal;
	}

	public void setCurrentYearTotal(BigDecimal currentYearTotal) {
		this.currentYearTotal = currentYearTotal;
	}

	public void setFundWiseAmount(Map<String, BigDecimal> fundWiseAmount) {
		this.fundWiseAmount = fundWiseAmount;
	}

	public StatementEntry(String accountCode,String accountName,String scheduleNo,BigDecimal previousYearTotal,BigDecimal currentYearTotal,boolean displayBold) {
		this.glCode = accountCode;
		this.accountName = accountName;
		this.scheduleNo = scheduleNo;
		this.previousYearTotal = previousYearTotal;
		this.currentYearTotal = currentYearTotal;
		this.displayBold = displayBold;
	}
	public StatementEntry(String accountCode,String accountName) {
		this.glCode = accountCode;
		this.accountName = accountName;
	}
	public StatementEntry(String accountName,Map<String,BigDecimal> fundWiseAmount,boolean displayBold) {
		this.accountName = accountName;
		this.fundWiseAmount = fundWiseAmount;
		this.displayBold = displayBold;
	}

	public String getGlCode() {
		return glCode;
	}
	
	public Map<String,BigDecimal> getFundWiseAmount() {
		return fundWiseAmount;
	}
	
	public void putFundWiseAmount(String fundName,BigDecimal amount) {
		fundWiseAmount.put(fundName, amount);
	}

	public String getAccountName() {
		return accountName;
	}
	public String getScheduleNo() {
		return scheduleNo;
	}
	public BigDecimal getPreviousYearTotal() {
		return previousYearTotal;
	}
	public BigDecimal getCurrentYearTotal() {
		return currentYearTotal;
	}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

}
