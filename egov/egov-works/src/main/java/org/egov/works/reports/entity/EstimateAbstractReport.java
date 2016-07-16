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
    private Long lineEstimates;
    private Long adminSanctionedEstimates;
    private String leAdminSanctionedAmountInCrores;
    private String aeAdminSanctionedAmountInCrores;
    private String workValueOfAdminSanctionedAEInCrores;
    private Long technicalSanctionedEstimates;
    private Long loaCreated;
    private Long loaNotCreated;
    private Long workNotCommenced;
    private String agreementValueInCrores;
    private Long workInProgress;
    private Long WorkCompleted;
    private Long billsCreated;
    private String BillValueInCrores;
    private Date createdDate;
    private String typeOfWorkName;
    private String subTypeOfWorkName;
    private Set<Department> departments = new HashSet<>();

    public Long getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(final Long financialYear) {
        this.financialYear = financialYear;
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(final Long department) {
        this.department = department;
    }

    public Date getAdminSanctionFromDate() {
        return adminSanctionFromDate;
    }

    public void setAdminSanctionFromDate(final Date adminSanctionFromDate) {
        this.adminSanctionFromDate = adminSanctionFromDate;
    }

    public Date getAdminSanctionToDate() {
        return adminSanctionToDate;
    }

    public void setAdminSanctionToDate(final Date adminSanctionToDate) {
        this.adminSanctionToDate = adminSanctionToDate;
    }

    public Integer getScheme() {
        return scheme;
    }

    public void setScheme(final Integer scheme) {
        this.scheme = scheme;
    }

    public Integer getSubScheme() {
        return subScheme;
    }

    public void setSubScheme(final Integer subScheme) {
        this.subScheme = subScheme;
    }

    public String getTypeOfSlum() {
        return typeOfSlum;
    }

    public void setTypeOfSlum(final String typeOfSlum) {
        this.typeOfSlum = typeOfSlum;
    }

    public Long getNatureOfWork() {
        return natureOfWork;
    }

    public void setNatureOfWork(final Long natureOfWork) {
        this.natureOfWork = natureOfWork;
    }

    public String getWorkCategory() {
        return workCategory;
    }

    public void setWorkCategory(final String workCategory) {
        this.workCategory = workCategory;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(final String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public boolean isSpillOverFlag() {
        return spillOverFlag;
    }

    public void setSpillOverFlag(final boolean spillOverFlag) {
        this.spillOverFlag = spillOverFlag;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(final String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getLineEstimates() {
        return lineEstimates;
    }

    public void setLineEstimates(final Long lineEstimates) {
        this.lineEstimates = lineEstimates;
    }

    public Long getAdminSanctionedEstimates() {
        return adminSanctionedEstimates;
    }

    public void setAdminSanctionedEstimates(final Long adminSanctionedEstimates) {
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

    public Long getTechnicalSanctionedEstimates() {
        return technicalSanctionedEstimates;
    }

    public void setTechnicalSanctionedEstimates(final Long technicalSanctionedEstimates) {
        this.technicalSanctionedEstimates = technicalSanctionedEstimates;
    }

    public Long getLoaCreated() {
        return loaCreated;
    }

    public void setLoaCreated(final Long loaCreated) {
        this.loaCreated = loaCreated;
    }

    public String getAgreementValueInCrores() {
        return agreementValueInCrores;
    }

    public void setAgreementValueInCrores(final String agreementValueInCrores) {
        this.agreementValueInCrores = agreementValueInCrores;
    }

    public Long getWorkInProgress() {
        return workInProgress;
    }

    public void setWorkInProgress(final Long workInProgress) {
        this.workInProgress = workInProgress;
    }

    public Long getWorkCompleted() {
        return WorkCompleted;
    }

    public void setWorkCompleted(final Long workCompleted) {
        WorkCompleted = workCompleted;
    }

    public Long getBillsCreated() {
        return billsCreated;
    }

    public void setBillsCreated(final Long billsCreated) {
        this.billsCreated = billsCreated;
    }

    public String getBillValueInCrores() {
        return BillValueInCrores;
    }

    public void setBillValueInCrores(final String billValueInCrores) {
        BillValueInCrores = billValueInCrores;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getCurrentFinancialYearId() {
        return currentFinancialYearId;
    }

    public void setCurrentFinancialYearId(final Long currentFinancialYearId) {
        this.currentFinancialYearId = currentFinancialYearId;
    }

    public Long getTypeOfWork() {
        return typeOfWork;
    }

    public void setTypeOfWork(final Long typeOfWork) {
        this.typeOfWork = typeOfWork;
    }

    public Long getSubTypeOfWork() {
        return subTypeOfWork;
    }

    public void setSubTypeOfWork(final Long subTypeOfWork) {
        this.subTypeOfWork = subTypeOfWork;
    }

    public String getTypeOfWorkName() {
        return typeOfWorkName;
    }

    public void setTypeOfWorkName(final String typeOfWorkName) {
        this.typeOfWorkName = typeOfWorkName;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(final Set<Department> departments) {
        this.departments = departments;
    }

    public String getSubTypeOfWorkName() {
        return subTypeOfWorkName;
    }

    public void setSubTypeOfWorkName(final String subTypeOfWorkName) {
        this.subTypeOfWorkName = subTypeOfWorkName;
    }

    public String getWorkValueOfAdminSanctionedAEInCrores() {
        return workValueOfAdminSanctionedAEInCrores;
    }

    public void setWorkValueOfAdminSanctionedAEInCrores(String workValueOfAdminSanctionedAEInCrores) {
        this.workValueOfAdminSanctionedAEInCrores = workValueOfAdminSanctionedAEInCrores;
    }

    public Long getLoaNotCreated() {
        return loaNotCreated;
    }

    public void setLoaNotCreated(Long loaNotCreated) {
        this.loaNotCreated = loaNotCreated;
    }

    public Long getWorkNotCommenced() {
        return workNotCommenced;
    }

    public void setWorkNotCommenced(Long workNotCommenced) {
        this.workNotCommenced = workNotCommenced;
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

}
