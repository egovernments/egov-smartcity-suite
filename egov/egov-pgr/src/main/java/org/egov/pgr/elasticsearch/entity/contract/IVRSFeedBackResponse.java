package org.egov.pgr.elasticsearch.entity.contract;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class IVRSFeedBackResponse {
    private String region = EMPTY;
    private String district = EMPTY;
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
    private long totalFeedback = 0;
    private long good = 0;
    private long average = 0;
    private long bad = 0;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
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
}
