package org.egov.works.entity.letterofacceptance;

import java.sql.Date;

import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.models.Money;
import org.egov.works.models.estimate.ProjectCode;
import org.egov.works.models.masters.NatureOfWork;
import org.egov.works.models.workorder.WorkOrder;

public class SearchLetterOfAcceptanceRequest {

    private String estimateNumber;
    private Department department;
    private Date adminSanctionFromDate;
    private String workIdentificationNumber;
    private String LOANumber;
    private Date adminSanctionToDate;
    private ProjectCode projectCode;
    private WorkOrder workOrder;
    private NatureOfWork natureOfWork;
    private String estimateDate;
    private Money workValue;
    private WorkOrder workOrderNumber;
    private WorkOrder workOrderDate;

    public SearchLetterOfAcceptanceRequest() {
    }
    
    public String getEstimateNumber() {
        return estimateNumber;
    }

    public void setEstimateNumber(final String estimateNumber) {
        this.estimateNumber = estimateNumber;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public Date getAdminSanctionFromDate() {
        return adminSanctionFromDate;
    }

    public void setAdminSanctionFromDate(final Date adminSanctionFromDate) {
        this.adminSanctionFromDate = adminSanctionFromDate;
    }

    public String getWorkIdentificationNumber() {
        return workIdentificationNumber;
    }

    public void setWorkIdentificationNumber(final String workIdentificationNumber) {
        this.workIdentificationNumber = workIdentificationNumber;
    }

    public String getLOANumber() {
        return LOANumber;
    }

    public void setLOANumber(final String lOANumber) {
        LOANumber = lOANumber;
    }

    public Date getAdminSanctionToDate() {
        return adminSanctionToDate;
    }

    public void setAdminSanctionToDate(final Date adminSanctionToDate) {
        this.adminSanctionToDate = adminSanctionToDate;
    }

    public ProjectCode getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(final ProjectCode projectCode) {
        this.projectCode = projectCode;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(final WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public NatureOfWork getNatureOfWork() {
        return natureOfWork;
    }

    public void setNatureOfWork(final NatureOfWork natureOfWork) {
        this.natureOfWork = natureOfWork;
    }

    public String getEstimateDate() {
        return estimateDate;
    }

    public void setEstimateDate(final String estimateDate) {
        this.estimateDate = estimateDate;
    }

    public Money getWorkValue() {
        return workValue;
    }

    public void setWorkValue(final Money workValue) {
        this.workValue = workValue;
    }

    public WorkOrder getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(final WorkOrder workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    public WorkOrder getWorkOrderDate() {
        return workOrderDate;
    }

    public void setWorkOrderDate(final WorkOrder workOrderDate) {
        this.workOrderDate = workOrderDate;
    }

}
