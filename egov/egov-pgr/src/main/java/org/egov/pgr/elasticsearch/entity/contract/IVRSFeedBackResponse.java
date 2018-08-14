package org.egov.pgr.elasticsearch.entity.contract;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.List;

public class IVRSFeedBackResponse {
    private String regionName = EMPTY;
    private String districtName = EMPTY;
    private String districtCode = EMPTY;
    private String ulbName = EMPTY;
    private String ulbCode = EMPTY;
    private String ulbGrade = EMPTY;
    private String wardName = EMPTY;
    private String wardCode = EMPTY;
    private String localityName = EMPTY;
    private String departmentName = EMPTY;
    private String departmentCode = EMPTY;
    private String functionaryName = EMPTY;
    private String functionaryMobileNo = EMPTY;
    private long totalComplaint = 0;
    private long totalIvrsUpdated = 0;
    private long todaysClosed = 0;
    private long totalFeedback = 0;
    private long good = 0;
    private long average = 0;
    private long bad = 0;
    private String compalintType = EMPTY;
    private String complaintCategory = EMPTY;
    private long responded = 0;
    private long notResponded = 0;
    private List<MonthlyFeedbackCounts> monthlyCounts;

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(String ulbName) {
        this.ulbName = ulbName;
    }

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(String ulbCode) {
        this.ulbCode = ulbCode;
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

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getFunctionaryName() {
        return functionaryName;
    }

    public void setFunctionaryName(String functionaryName) {
        this.functionaryName = functionaryName;
    }

    public String getFunctionaryMobileNo() {
        return functionaryMobileNo;
    }

    public void setFunctionaryMobileNo(String functionaryMobileNo) {
        this.functionaryMobileNo = functionaryMobileNo;
    }

    public long getTotalComplaint() {
        return totalComplaint;
    }

    public void setTotalComplaint(long totalComplaint) {
        this.totalComplaint = totalComplaint;
    }

    public long getTotalFeedback() {
        return totalFeedback;
    }

    public void setTotalFeedback(long totalFeedback) {
        this.totalFeedback = totalFeedback;
    }

    public long getGood() {
        return good;
    }

    public void setGood(long good) {
        this.good = good;
    }

    public long getAverage() {
        return average;
    }

    public void setAverage(long avarage) {
        this.average = avarage;
    }

    public long getBad() {
        return bad;
    }

    public void setBad(long bad) {
        this.bad = bad;
    }

    public String getCompalintType() {
        return compalintType;
    }

    public void setCompalintType(String compalintType) {
        this.compalintType = compalintType;
    }

    public String getComplaintCategory() {
        return complaintCategory;
    }

    public void setComplaintCategory(String complaintCategory) {
        this.complaintCategory = complaintCategory;
    }

    public long getResponded() {
        return responded;
    }

    public void setResponded(long responded) {
        this.responded = responded;
    }

    public long getNotResponded() {
        return notResponded;
    }

    public void setNotResponded(long notResponded) {
        this.notResponded = notResponded;
    }

    public List<MonthlyFeedbackCounts> getMonthlyCounts() {
        return monthlyCounts;
    }

    public void setMonthlyCounts(List<MonthlyFeedbackCounts> monthlyCounts) {
        this.monthlyCounts = monthlyCounts;
    }

    public long getTodaysClosed() {
        return todaysClosed;
    }

    public void setTodaysClosed(long todaysClosed) {
        this.todaysClosed = todaysClosed;
    }

    public long getTotalIvrsUpdated() {
        return totalIvrsUpdated;
    }

    public void setTotalIvrsUpdated(long totalIvrsUpdated) {
        this.totalIvrsUpdated = totalIvrsUpdated;
    }
}
