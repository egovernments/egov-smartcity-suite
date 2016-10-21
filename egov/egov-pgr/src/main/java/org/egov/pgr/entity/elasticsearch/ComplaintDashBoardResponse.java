package org.egov.pgr.entity.elasticsearch;

public class ComplaintDashBoardResponse {
	private String districtName;
	private String ulbName;
	private String ulbGrade;
	private String wardName;
	private String functionaryName;
	private String departmentName;
	private String ComplaintTypeName;
	private long TotalComplaintCount;
	private long OpenComplaintCount;
	private long ClosedComplaintCount;
	private long OpenWithinSLACount;
	private long OpenOutSideSLACount;
	private long ClosedWithinSLACount;
	private long ClosedOutSideSLACount;
	private double AvgSatisfactionIndex;
	private long AgeingGroup1;
	private long AgeingGroup2;
	private long AgeingGroup3;
	private long AgeingGroup4;
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getUlbName() {
		return ulbName;
	}
	public void setUlbName(String ulbName) {
		this.ulbName = ulbName;
	}
	public String getUlbGrade() {
		return ulbGrade;
	}
	public void setUlbGrade(String ulbGrade) {
		this.ulbGrade = ulbGrade;
	}
	public String getWardName() {
		return wardName;
	}
	public void setWardName(String wardName) {
		this.wardName = wardName;
	}
	public String getFunctionaryName() {
		return functionaryName;
	}
	public void setFunctionaryName(String functionaryName) {
		this.functionaryName = functionaryName;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getComplaintTypeName() {
		return ComplaintTypeName;
	}
	public void setComplaintTypeName(String complaintTypeName) {
		ComplaintTypeName = complaintTypeName;
	}
	public long getTotalComplaintCount() {
		return TotalComplaintCount;
	}
	public void setTotalComplaintCount(long totalComplaintCount) {
		TotalComplaintCount = totalComplaintCount;
	}
	public long getOpenComplaintCount() {
		return OpenComplaintCount;
	}
	public void setOpenComplaintCount(long openComplaintCount) {
		OpenComplaintCount = openComplaintCount;
	}
	public long getClosedComplaintCount() {
		return ClosedComplaintCount;
	}
	public void setClosedComplaintCount(long closedComplaintCount) {
		ClosedComplaintCount = closedComplaintCount;
	}
	public long getOpenWithinSLACount() {
		return OpenWithinSLACount;
	}
	public void setOpenWithinSLACount(long openWithinSLACount) {
		OpenWithinSLACount = openWithinSLACount;
	}
	public long getOpenOutSideSLACount() {
		return OpenOutSideSLACount;
	}
	public void setOpenOutSideSLACount(long openOutSideSLACount) {
		OpenOutSideSLACount = openOutSideSLACount;
	}
	public long getClosedWithinSLACount() {
		return ClosedWithinSLACount;
	}
	public void setClosedWithinSLACount(long closedWithinSLACount) {
		ClosedWithinSLACount = closedWithinSLACount;
	}
	public long getClosedOutSideSLACount() {
		return ClosedOutSideSLACount;
	}
	public void setClosedOutSideSLACount(long closedOutSideSLACount) {
		ClosedOutSideSLACount = closedOutSideSLACount;
	}
	public double getAvgSatisfactionIndex() {
		return AvgSatisfactionIndex;
	}
	public void setAvgSatisfactionIndex(double avgSatisfactionIndex) {
		AvgSatisfactionIndex = avgSatisfactionIndex;
	}
	public long getAgeingGroup1() {
		return AgeingGroup1;
	}
	public void setAgeingGroup1(long ageingGroup1) {
		AgeingGroup1 = ageingGroup1;
	}
	public long getAgeingGroup2() {
		return AgeingGroup2;
	}
	public void setAgeingGroup2(long ageingGroup2) {
		AgeingGroup2 = ageingGroup2;
	}
	public long getAgeingGroup3() {
		return AgeingGroup3;
	}
	public void setAgeingGroup3(long ageingGroup3) {
		AgeingGroup3 = ageingGroup3;
	}
	public long getAgeingGroup4() {
		return AgeingGroup4;
	}
	public void setAgeingGroup4(long ageingGroup4) {
		AgeingGroup4 = ageingGroup4;
	}
}
