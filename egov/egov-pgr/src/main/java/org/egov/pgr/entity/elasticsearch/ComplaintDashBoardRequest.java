package org.egov.pgr.entity.elasticsearch;


public class ComplaintDashBoardRequest {
	
	String districtCode;
	String ulbCode;
	String wardNo;
	String departmentCode;
	String fromDate;
	String toDate;
	
	public String getDistrictCode() {
		return districtCode;
	}
	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}
	public String getUlbCode() {
		return ulbCode;
	}
	public void setUlbCode(String ulbCode) {
		this.ulbCode = ulbCode;
	}
	public String getWardNo() {
		return wardNo;
	}
	public void setWardNo(String wardNo) {
		this.wardNo = wardNo;
	}
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public String getFromDate() {
		
//		DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
//		return format.parseDateTime(fromDate);
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
//		DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
//		return format.parseDateTime(toDate);
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
}
