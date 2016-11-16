package org.egov.pgr.entity.es;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.HashMap;
import java.util.List;

@JsonInclude(value = Include.NON_NULL)
public class ComplaintSourceResponse {

    private String regionName;
    private String districtName;
    private String ulbCode;
    private String ulbName;
    private String ulbGrade;
    private String wardName;
    private String domainURL;
    private String functionaryName;
    private String functionaryMobileNumber;
    private String localityName;
    private String departmentName;
    private String complaintTypeName;
    private List<HashMap<String, Long>> sourceList;

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

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(String ulbCode) {
        this.ulbCode = ulbCode;
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

    public String getDomainURL() {
        return domainURL;
    }

    public void setDomainURL(String domainURL) {
        this.domainURL = domainURL;
    }

    public String getFunctionaryName() {
        return functionaryName;
    }

    public void setFunctionaryName(String functionaryName) {
        this.functionaryName = functionaryName;
    }

    public String getFunctionaryMobileNumber() {
        return functionaryMobileNumber;
    }

    public void setFunctionaryMobileNumber(String functionaryMobileNumber) {
        this.functionaryMobileNumber = functionaryMobileNumber;
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

    public String getComplaintTypeName() {
        return complaintTypeName;
    }

    public void setComplaintTypeName(String complaintTypeName) {
        this.complaintTypeName = complaintTypeName;
    }

    public List<HashMap<String, Long>> getSourceList() {
        return sourceList;
    }

    public void setSourceList(List<HashMap<String, Long>> sourceList) {
        this.sourceList = sourceList;
    }
}
