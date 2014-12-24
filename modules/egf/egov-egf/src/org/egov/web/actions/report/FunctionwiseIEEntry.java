package org.egov.web.actions.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class FunctionwiseIEEntry {
	private String slNo;
	private String functionCode;
	private String functionName;
	private BigDecimal totalIncome = BigDecimal.ZERO;
	private Map<String,BigDecimal> majorcodeWiseAmount = new HashMap<String, BigDecimal>();
	public String getSlNo() {
		return slNo;
	}
	public void setSlNo(String slNo) {
		this.slNo = slNo;
	}
	public String getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public BigDecimal getTotalIncome() {
		return totalIncome;
	}
	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}
	public Map<String, BigDecimal> getMajorcodeWiseAmount() {
		return majorcodeWiseAmount;
	}
	public void setMajorcodeWiseAmount(Map<String, BigDecimal> majorcodeWiseAmount) {
		this.majorcodeWiseAmount = majorcodeWiseAmount;
	}

}
