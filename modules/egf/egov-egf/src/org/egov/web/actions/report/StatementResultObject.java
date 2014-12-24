package org.egov.web.actions.report;

import java.math.BigDecimal;

public class StatementResultObject {
	private static final BigDecimal NEGATIVE = new BigDecimal(-1);
	BigDecimal amount;
	Integer fundId;
	String glCode = "";
	Character type;
	String scheduleNumber = "";
	String scheduleName = "";

	public String getScheduleNumber() {
		return scheduleNumber;
	}
	public void setScheduleNumber(String scheduleNumber) {
		this.scheduleNumber = scheduleNumber;
	}
	public String getScheduleName() {
		return scheduleName;
	}
	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Integer getFundId() {
		return fundId;
	}
	public void setFundId(BigDecimal fundId) {
		this.fundId = Integer.valueOf(fundId.intValue());
	}
	public String getGlCode() {
		return glCode;
	}
	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}
	public Character getType() {
		return type;
	}
	public void setType(Character type) {
		this.type = type;
	}
	
	public boolean isLiability(){
		return type!=null?"L".equalsIgnoreCase(type.toString()):false;
	}
	public boolean isIncome(){
		return type!=null?"I".equalsIgnoreCase(type.toString()):false;
	}

	public void negateAmount(){
		amount = amount.multiply(NEGATIVE);
	}
}
