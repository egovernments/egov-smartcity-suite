package org.egov.model.report;

import java.util.Date;

public class ReportBean {
	
private	 Integer fundId;
	private Integer functionId;
	private Integer departmentId;
	private  Integer functionaryId;
	private   Integer divisionId;
	private   Date fromDate;
	private Date toDate;
	private String functionName;
	
	private String reportType;
	private String exportType;
	public String getExportType() {
		return exportType;
	}
	public void setExportType(String exportType) {
		this.exportType = exportType;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public Integer getFundId() {
		return fundId;
	}
	public Integer getFunctionId() {
		return functionId;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}
	
	public Integer getFunctionaryId() {
		return functionaryId;
	}
	public void setFunctionaryId(Integer functionaryId) {
		this.functionaryId = functionaryId;
	}
	public Integer getDivisionId() {
		return divisionId;
	}
	public void setDivisionId(Integer divisionId) {
		this.divisionId = divisionId;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setFundId(Integer fundId) {
		this.fundId = fundId;
	}
	public void setFunctionId(Integer functionId) {
		this.functionId = functionId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public void setFunctinaryId(Integer functinaryId) {
		this.functionaryId = functinaryId;
	}
	public void setBoundaryId(Integer boundaryId) {
		this.divisionId = boundaryId;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
    


}
