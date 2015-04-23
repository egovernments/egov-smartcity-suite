package org.egov.web.actions.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class IEStatementEntry {
	private String glCode;
	//private String glCode;
	private String accountName;
	private String scheduleNo;
	private BigDecimal budgetAmount;
	private String majorCode;
	private Map<String ,BigDecimal> scheduleWiseTotal= new HashMap<String, BigDecimal>();
	private Map<String,BigDecimal> netAmount = new HashMap<String, BigDecimal>();
	private Map<String,BigDecimal> previousYearAmount = new HashMap<String, BigDecimal>();
	
	private boolean displayBold = false;
	
	public IEStatementEntry() {
		
	}
	
	public IEStatementEntry(String accountCode,String accountName,Map<String,BigDecimal> netAmount,Map<String,BigDecimal> previousYearAmount,boolean displayBold) {
		this.glCode = accountCode;
		this.accountName = accountName;
		this.previousYearAmount=previousYearAmount;
		this.netAmount=netAmount;
		this.displayBold = displayBold;
	}
	
   public IEStatementEntry(String accountCode,String accountName,String scheduleNo,boolean displayBold) {
		this.glCode = accountCode;
		this.accountName = accountName;
		this.scheduleNo = scheduleNo;
		this.displayBold = displayBold;
	}
   public IEStatementEntry(String accountCode,String accountName,String scheduleNo,String majorCode,boolean displayBold) {
		this.glCode = accountCode;
		this.accountName = accountName;
		this.scheduleNo = scheduleNo;
		this.majorCode=majorCode;
		this.displayBold = displayBold;
	}
   
   public IEStatementEntry(String glcode,String accountName,boolean displayBold) {
		this.glCode = glcode;
		this.accountName = accountName;
		this.displayBold = displayBold;
	}
   public BigDecimal getBudgetAmount() {
		return budgetAmount;
	}

	public void setBudgetAmount(BigDecimal budgetAmount) {
		this.budgetAmount = budgetAmount;
	}
	
	public String getGlCode() {
		return glCode;
	}
	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}
	

	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getScheduleNo() {
		return scheduleNo;
	}
	public void setScheduleNo(String scheduleNo) {
		this.scheduleNo = scheduleNo;
	}
	public Map<String, BigDecimal> getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Map<String, BigDecimal> netAmount) {
		this.netAmount = netAmount;
	}

	public Map<String, BigDecimal> getPreviousYearAmount() {
		return previousYearAmount;
	}

	public void setPreviousYearAmount(Map<String, BigDecimal> previousYearAmount) {
		this.previousYearAmount = previousYearAmount;
	}

	public boolean isDisplayBold() {
		return displayBold;
	}
	public void setDisplayBold(boolean displayBold) {
		this.displayBold = displayBold;
	}

	public String getMajorCode() {
		return majorCode;
	}

	public void setMajorCode(String majorCode) {
		this.majorCode = majorCode;
	}
}
