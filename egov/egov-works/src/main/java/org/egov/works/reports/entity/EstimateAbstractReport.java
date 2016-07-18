/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.reports.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.infra.admin.master.entity.Department;

public class EstimateAbstractReport {
    private Long financialYear;
    private Long department;
    private Date adminSanctionFromDate;
    private Date adminSanctionToDate;
    private Integer scheme;
    private Integer subScheme;
    private Long natureOfWork;
    private String workCategory;
    private String typeOfSlum;
    private String beneficiary;
    private boolean spillOverFlag;
    private Long currentFinancialYearId;
    private Long typeOfWork;
    private Long subTypeOfWork;
    private String workStatus;

    private String departmentName;
    private String lineEstimates;
    private String adminSanctionedEstimates;
    private String leAdminSanctionedAmountInCrores;
    private String aeAdminSanctionedAmountInCrores;
    private String workValueOfAdminSanctionedAEInCrores;
    private String technicalSanctionedEstimates;
    private String loaCreated;
    private String loaNotCreated;
    private String workNotCommenced;
    private String agreementValueInCrores;
    private String workInProgress;
    private String WorkCompleted;
    private String billsCreated;
    private String BillValueInCrores;
    private String createdDate;
    private String typeOfWorkName;
    private String subTypeOfWorkName;
    private Set<Department> departments = new HashSet<>();

    public Long getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(Long financialYear) {
        this.financialYear = financialYear;
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public Date getAdminSanctionFromDate() {
        return adminSanctionFromDate;
    }

    public void setAdminSanctionFromDate(Date adminSanctionFromDate) {
        this.adminSanctionFromDate = adminSanctionFromDate;
    }

    public Date getAdminSanctionToDate() {
        return adminSanctionToDate;
    }

    public void setAdminSanctionToDate(Date adminSanctionToDate) {
        this.adminSanctionToDate = adminSanctionToDate;
    }

    public Integer getScheme() {
        return scheme;
    }

    public void setScheme(Integer scheme) {
        this.scheme = scheme;
    }

    public Integer getSubScheme() {
        return subScheme;
    }

    public void setSubScheme(Integer subScheme) {
        this.subScheme = subScheme;
    }

    public Long getNatureOfWork() {
        return natureOfWork;
    }

    public void setNatureOfWork(Long natureOfWork) {
        this.natureOfWork = natureOfWork;
    }

    public String getWorkCategory() {
        return workCategory;
    }

    public void setWorkCategory(String workCategory) {
        this.workCategory = workCategory;
    }

    public String getTypeOfSlum() {
        return typeOfSlum;
    }

    public void setTypeOfSlum(String typeOfSlum) {
        this.typeOfSlum = typeOfSlum;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public boolean isSpillOverFlag() {
        return spillOverFlag;
    }

    public void setSpillOverFlag(boolean spillOverFlag) {
        this.spillOverFlag = spillOverFlag;
    }

    public Long getCurrentFinancialYearId() {
        return currentFinancialYearId;
    }

    public void setCurrentFinancialYearId(Long currentFinancialYearId) {
        this.currentFinancialYearId = currentFinancialYearId;
    }

    public Long getTypeOfWork() {
        return typeOfWork;
    }

    public void setTypeOfWork(Long typeOfWork) {
        this.typeOfWork = typeOfWork;
    }

    public Long getSubTypeOfWork() {
        return subTypeOfWork;
    }

    public void setSubTypeOfWork(Long subTypeOfWork) {
        this.subTypeOfWork = subTypeOfWork;
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getLineEstimates() {
        return lineEstimates;
    }

    public void setLineEstimates(String lineEstimates) {
        this.lineEstimates = lineEstimates;
    }

    public String getAdminSanctionedEstimates() {
        return adminSanctionedEstimates;
    }

    public void setAdminSanctionedEstimates(String adminSanctionedEstimates) {
        this.adminSanctionedEstimates = adminSanctionedEstimates;
    }

    public String getLeAdminSanctionedAmountInCrores() {
        return leAdminSanctionedAmountInCrores;
    }

    public void setLeAdminSanctionedAmountInCrores(String leAdminSanctionedAmountInCrores) {
        this.leAdminSanctionedAmountInCrores = leAdminSanctionedAmountInCrores;
    }

    public String getAeAdminSanctionedAmountInCrores() {
        return aeAdminSanctionedAmountInCrores;
    }

    public void setAeAdminSanctionedAmountInCrores(String aeAdminSanctionedAmountInCrores) {
        this.aeAdminSanctionedAmountInCrores = aeAdminSanctionedAmountInCrores;
    }

    public String getWorkValueOfAdminSanctionedAEInCrores() {
        return workValueOfAdminSanctionedAEInCrores;
    }

    public void setWorkValueOfAdminSanctionedAEInCrores(String workValueOfAdminSanctionedAEInCrores) {
        this.workValueOfAdminSanctionedAEInCrores = workValueOfAdminSanctionedAEInCrores;
    }

    public String getTechnicalSanctionedEstimates() {
        return technicalSanctionedEstimates;
    }

    public void setTechnicalSanctionedEstimates(String technicalSanctionedEstimates) {
        this.technicalSanctionedEstimates = technicalSanctionedEstimates;
    }

    public String getLoaCreated() {
        return loaCreated;
    }

    public void setLoaCreated(String loaCreated) {
        this.loaCreated = loaCreated;
    }

    public String getLoaNotCreated() {
        return loaNotCreated;
    }

    public void setLoaNotCreated(String loaNotCreated) {
        this.loaNotCreated = loaNotCreated;
    }

    public String getWorkNotCommenced() {
        return workNotCommenced;
    }

    public void setWorkNotCommenced(String workNotCommenced) {
        this.workNotCommenced = workNotCommenced;
    }

    public String getAgreementValueInCrores() {
        return agreementValueInCrores;
    }

    public void setAgreementValueInCrores(String agreementValueInCrores) {
        this.agreementValueInCrores = agreementValueInCrores;
    }

    public String getWorkInProgress() {
        return workInProgress;
    }

    public void setWorkInProgress(String workInProgress) {
        this.workInProgress = workInProgress;
    }

    public String getWorkCompleted() {
        return WorkCompleted;
    }

    public void setWorkCompleted(String workCompleted) {
        WorkCompleted = workCompleted;
    }

    public String getBillsCreated() {
        return billsCreated;
    }

    public void setBillsCreated(String billsCreated) {
        this.billsCreated = billsCreated;
    }

    public String getBillValueInCrores() {
        return BillValueInCrores;
    }

    public void setBillValueInCrores(String billValueInCrores) {
        BillValueInCrores = billValueInCrores;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getTypeOfWorkName() {
        return typeOfWorkName;
    }

    public void setTypeOfWorkName(String typeOfWorkName) {
        this.typeOfWorkName = typeOfWorkName;
    }

    public String getSubTypeOfWorkName() {
        return subTypeOfWorkName;
    }

    public void setSubTypeOfWorkName(String subTypeOfWorkName) {
        this.subTypeOfWorkName = subTypeOfWorkName;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }

}
