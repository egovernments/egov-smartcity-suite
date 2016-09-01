package org.egov.eis.reports.entity;

import java.util.Date;
import java.util.Map;

public class EmployeeAssignmentSearch {

    private String employeeCode;
    private String employeeName;
    private Long department;
    private Long designation;
    private Long position;
    private Date assignmentDate;
    private String departmentName;
    private String designationName;
    private String positionName;
    private String dateRange;
    private Map<String,String> tempPositionDetails;

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(final String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(final String employeeName) {
        this.employeeName = employeeName;
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(final Long department) {
        this.department = department;
    }

    public Long getDesignation() {
        return designation;
    }

    public void setDesignation(final Long designation) {
        this.designation = designation;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(final Long position) {
        this.position = position;
    }

    public Date getAssignmentDate() {
        return assignmentDate;
    }

    public void setAssignmentDate(Date assignmentDate) {
        this.assignmentDate = assignmentDate;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public Map<String, String> getTempPositionDetails() {
        return tempPositionDetails;
    }

    public void setTempPositionDetails(Map<String, String> tempPositionDetails) {
        this.tempPositionDetails = tempPositionDetails;
    }

}
