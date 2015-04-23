package org.egov.web.actions.report;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class DepartmentwiseExpenditureResult {

	private int slNo;
	private String departmentNm;
	private BigDecimal concurrenceGiven;
	private boolean departmentWithNodata;
	private BigDecimal totalConcurrenceGivenTillDate;
	private Map<String,BigDecimal> dayAmountMap =new LinkedHashMap<String,BigDecimal>();
	
	public DepartmentwiseExpenditureResult(){  }
	
	public DepartmentwiseExpenditureResult(String department,BigDecimal concurrenceGiven) {
		this.departmentNm = department;
		this.concurrenceGiven = concurrenceGiven;
	}
	
	public DepartmentwiseExpenditureResult(String department,BigDecimal concurrenceGiven,int slNo) {
		this.departmentNm = department;
		this.concurrenceGiven = concurrenceGiven;
		this.slNo=slNo;
	}
	
	public DepartmentwiseExpenditureResult( String departmentNm,
			BigDecimal concurrenceGiven,int slNo, boolean departmentWithNodata			) {
		super();
		this.slNo = slNo;
		this.departmentNm = departmentNm;
		this.concurrenceGiven = concurrenceGiven;
		this.departmentWithNodata = departmentWithNodata;
		//this.totalConcurrenceGivenTillDate = totalConcurrenceGivenTillDate;
		//this.dayAmountMap = dayAmountMap;
	}

	public BigDecimal getConcurrenceGiven() {
		return concurrenceGiven;
	}
	public void setConcurrenceGiven(BigDecimal concurrenceGiven) {
		this.concurrenceGiven = concurrenceGiven;
	}
	
	public String getDepartmentNm() {
		return departmentNm;
	}
	public void setDepartmentNm(String departmentNm) {
		this.departmentNm = departmentNm;
	}
	
	public BigDecimal getTotalConcurrenceGivenTillDate() {
		return totalConcurrenceGivenTillDate;
	}
	public void setTotalConcurrenceGivenTillDate(
			BigDecimal totalConcurrenceGivenTillDate) {
		this.totalConcurrenceGivenTillDate = totalConcurrenceGivenTillDate;
	}

	public int getSlNo() {
		return slNo;
	}

	public void setSlNo(int slNo) {
		this.slNo = slNo;
	}

	public Map<String, BigDecimal> getDayAmountMap() {
		return dayAmountMap;
	}

	public void setDayAmountMap(Map<String, BigDecimal> dayAmountMap) {
		this.dayAmountMap = dayAmountMap;
	}

	public boolean isDepartmentWithNodata() {
		return departmentWithNodata;
	}

	public void setDepartmentWithNodata(boolean departmentWithNodata) {
		this.departmentWithNodata = departmentWithNodata;
	}   
	
	
}