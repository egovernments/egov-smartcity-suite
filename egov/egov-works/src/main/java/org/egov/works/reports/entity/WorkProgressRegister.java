/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.works.reports.entity;

import org.egov.commons.CFunction;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.model.budget.BudgetGroup;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.entity.enums.Beneficiary;
import org.egov.works.lineestimate.entity.enums.WorkCategory;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.masters.NatureOfWork;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "EGW_MV_WORK_PROGRESS_REGISTER")
@SequenceGenerator(name = WorkProgressRegister.SEQ_EGW_WORKPROGREEREGISTER, sequenceName = WorkProgressRegister.SEQ_EGW_WORKPROGREEREGISTER, allocationSize = 1)
public class WorkProgressRegister extends AbstractAuditable {

    private static final long serialVersionUID = 5548463818994931623L;

    public static final String SEQ_EGW_WORKPROGREEREGISTER = "SEQ_EGW_MV_WORK_PROGRESS_REGISTER";

    @Id
    @GeneratedValue(generator = SEQ_EGW_WORKPROGREEREGISTER, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward", nullable = false)
    private Boundary ward;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location", nullable = false)
    private Boundary location;

    @Enumerated(EnumType.STRING)
    private WorkCategory workCategory;

    @Enumerated(EnumType.STRING)
    private Beneficiary beneficiary;

    @SafeHtml
    @Length(max = 1024)
    private String nameOfWork;

    @SafeHtml
    @Length(max = 256)
    private String winCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fund", nullable = false)
    private Fund fund;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "function", nullable = false)
    private CFunction function;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budgethead", nullable = false)
    private BudgetGroup budgetHead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typeofwork")
    private EgwTypeOfWork typeOfWork;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subtypeofwork")
    private EgwTypeOfWork subTypeOfWork;

    @Temporal(TemporalType.DATE)
    private Date adminSanctionDate;

    private String adminSanctionBy;

    private BigDecimal adminSanctionAmount;

    @Temporal(TemporalType.DATE)
    private Date technicalSanctionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technicalSanctionBy")
    private User technicalSanctionBy;

    private BigDecimal estimateAmount;

    private String modeOfAllotment;

    @SafeHtml
    private String agreementNumber;

    @Temporal(TemporalType.DATE)
    private Date agreementDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractor")
    private Contractor contractor;

    private BigDecimal agreementAmount;

    @SafeHtml
    private String latestMbNumber;

    @Temporal(TemporalType.DATE)
    private Date latestMbDate;

    @SafeHtml
    private String latestBillNumber;

    @Temporal(TemporalType.DATE)
    private Date latestBillDate;

    @SafeHtml
    private String billtype;

    private BigDecimal billamount;

    private BigDecimal totalBillAmount;

    private BigDecimal totalBillPaidSoFar;

    private BigDecimal balanceValueOfWorkToBill;

    private boolean spillOverFlag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme")
    private Scheme scheme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subScheme")
    private SubScheme subScheme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "natureOfWork")
    private NatureOfWork natureOfWork;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leid")
    private LineEstimate lineEstimate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ledid")
    private LineEstimateDetails lineEstimateDetails;

    @Length(max = 50)
    private String lineEstimateStatus;

    @Length(max = 50)
    private String departmentName;

    @Length(max = 50)
    private String woStatusCode;

    private boolean workOrderCreated;

    private boolean workCompleted;

    @Length(max = 50)
    private String typeOfWorkName;

    @Length(max = 50)
    private String subTypeOfWorkName;

    private Double milestonePercentageCompleted;

    @Override
    protected void setId(final Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Boundary getWard() {
        return ward;
    }

    public void setWard(final Boundary ward) {
        this.ward = ward;
    }

    public Boundary getLocation() {
        return location;
    }

    public void setLocation(final Boundary location) {
        this.location = location;
    }

    public WorkCategory getWorkCategory() {
        return workCategory;
    }

    public void setWorkCategory(final WorkCategory workCategory) {
        this.workCategory = workCategory;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(final Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getNameOfWork() {
        return nameOfWork;
    }

    public void setNameOfWork(final String nameOfWork) {
        this.nameOfWork = nameOfWork;
    }

    public String getWinCode() {
        return winCode;
    }

    public void setWinCode(final String winCode) {
        this.winCode = winCode;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(final CFunction function) {
        this.function = function;
    }

    public BudgetGroup getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(final BudgetGroup budgetHead) {
        this.budgetHead = budgetHead;
    }

    public EgwTypeOfWork getTypeOfWork() {
        return typeOfWork;
    }

    public void setTypeOfWork(final EgwTypeOfWork typeOfWork) {
        this.typeOfWork = typeOfWork;
    }

    public EgwTypeOfWork getSubTypeOfWork() {
        return subTypeOfWork;
    }

    public void setSubTypeOfWork(final EgwTypeOfWork subTypeOfWork) {
        this.subTypeOfWork = subTypeOfWork;
    }

    public Date getAdminSanctionDate() {
        return adminSanctionDate;
    }

    public void setAdminSanctionDate(final Date adminSanctionDate) {
        this.adminSanctionDate = adminSanctionDate;
    }

    public BigDecimal getAdminSanctionAmount() {
        return adminSanctionAmount;
    }

    public void setAdminSanctionAmount(final BigDecimal adminSanctionAmount) {
        this.adminSanctionAmount = adminSanctionAmount;
    }

    public Date getTechnicalSanctionDate() {
        return technicalSanctionDate;
    }

    public void setTechnicalSanctionDate(final Date technicalSanctionDate) {
        this.technicalSanctionDate = technicalSanctionDate;
    }

    public User getTechnicalSanctionBy() {
        return technicalSanctionBy;
    }

    public void setTechnicalSanctionBy(final User technicalSanctionBy) {
        this.technicalSanctionBy = technicalSanctionBy;
    }

    public BigDecimal getEstimateAmount() {
        return estimateAmount;
    }

    public void setEstimateAmount(final BigDecimal estimateAmount) {
        this.estimateAmount = estimateAmount;
    }

    public String getModeOfAllotment() {
        return modeOfAllotment;
    }

    public void setModeOfAllotment(final String modeOfAllotment) {
        this.modeOfAllotment = modeOfAllotment;
    }

    public String getAgreementNumber() {
        return agreementNumber;
    }

    public void setAgreementNumber(final String agreementNumber) {
        this.agreementNumber = agreementNumber;
    }

    public Date getAgreementDate() {
        return agreementDate;
    }

    public void setAgreementDate(final Date agreementDate) {
        this.agreementDate = agreementDate;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(final Contractor contractor) {
        this.contractor = contractor;
    }

    public BigDecimal getAgreementAmount() {
        return agreementAmount;
    }

    public void setAgreementAmount(final BigDecimal agreementAmount) {
        this.agreementAmount = agreementAmount;
    }

    public String getLatestMbNumber() {
        return latestMbNumber;
    }

    public void setLatestMbNumber(final String latestMbNumber) {
        this.latestMbNumber = latestMbNumber;
    }

    public Date getLatestMbDate() {
        return latestMbDate;
    }

    public void setLatestMbDate(final Date latestMbDate) {
        this.latestMbDate = latestMbDate;
    }

    public String getLatestBillNumber() {
        return latestBillNumber;
    }

    public void setLatestBillNumber(final String latestBillNumber) {
        this.latestBillNumber = latestBillNumber;
    }

    public Date getLatestBillDate() {
        return latestBillDate;
    }

    public void setLatestBillDate(final Date latestBillDate) {
        this.latestBillDate = latestBillDate;
    }

    public String getBilltype() {
        return billtype;
    }

    public void setBilltype(final String billtype) {
        this.billtype = billtype;
    }

    public BigDecimal getBillamount() {
        return billamount;
    }

    public void setBillamount(final BigDecimal billamount) {
        this.billamount = billamount;
    }

    public BigDecimal getTotalBillAmount() {
        return totalBillAmount;
    }

    public void setTotalBillAmount(final BigDecimal totalBillAmount) {
        this.totalBillAmount = totalBillAmount;
    }

    public BigDecimal getTotalBillPaidSoFar() {
        return totalBillPaidSoFar;
    }

    public void setTotalBillPaidSoFar(final BigDecimal totalBillPaidSoFar) {
        this.totalBillPaidSoFar = totalBillPaidSoFar;
    }

    public BigDecimal getBalanceValueOfWorkToBill() {
        return balanceValueOfWorkToBill;
    }

    public void setBalanceValueOfWorkToBill(final BigDecimal balanceValueOfWorkToBill) {
        this.balanceValueOfWorkToBill = balanceValueOfWorkToBill;
    }

    public boolean isSpillOverFlag() {
        return spillOverFlag;
    }

    public void setSpillOverFlag(final boolean spillOverFlag) {
        this.spillOverFlag = spillOverFlag;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(final Scheme scheme) {
        this.scheme = scheme;
    }

    public SubScheme getSubScheme() {
        return subScheme;
    }

    public void setSubScheme(final SubScheme subScheme) {
        this.subScheme = subScheme;
    }

    public NatureOfWork getNatureOfWork() {
        return natureOfWork;
    }

    public void setNatureOfWork(final NatureOfWork natureOfWork) {
        this.natureOfWork = natureOfWork;
    }

    public LineEstimate getLineEstimate() {
        return lineEstimate;
    }

    public void setLineEstimate(final LineEstimate lineEstimate) {
        this.lineEstimate = lineEstimate;
    }

    public LineEstimateDetails getLineEstimateDetails() {
        return lineEstimateDetails;
    }

    public void setLineEstimateDetails(final LineEstimateDetails lineEstimateDetails) {
        this.lineEstimateDetails = lineEstimateDetails;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(final String departmentName) {
        this.departmentName = departmentName;
    }

    public String getWoStatusCode() {
        return woStatusCode;
    }

    public void setWoStatusCode(final String woStatusCode) {
        this.woStatusCode = woStatusCode;
    }

    public boolean isWorkOrderCreated() {
        return workOrderCreated;
    }

    public void setWorkOrderCreated(final boolean workOrderCreated) {
        this.workOrderCreated = workOrderCreated;
    }

    public boolean isWorkCompleted() {
        return workCompleted;
    }

    public void setWorkCompleted(final boolean workCompleted) {
        this.workCompleted = workCompleted;
    }

    public String getTypeOfWorkName() {
        return typeOfWorkName;
    }

    public void setTypeOfWorkName(final String typeOfWorkName) {
        this.typeOfWorkName = typeOfWorkName;
    }

    public String getSubTypeOfWorkName() {
        return subTypeOfWorkName;
    }

    public void setSubTypeOfWorkName(final String subTypeOfWorkName) {
        this.subTypeOfWorkName = subTypeOfWorkName;
    }

    public String getLineEstimateStatus() {
        return lineEstimateStatus;
    }

    public void setLineEstimateStatus(final String lineEstimateStatus) {
        this.lineEstimateStatus = lineEstimateStatus;
    }

    public Double getMilestonePercentageCompleted() {
        return milestonePercentageCompleted;
    }

    public void setMilestonePercentageCompleted(final Double milestonePercentageCompleted) {
        this.milestonePercentageCompleted = milestonePercentageCompleted;
    }

    public String getAdminSanctionBy() {
        return adminSanctionBy;
    }

    public void setAdminSanctionBy(final String adminSanctionBy) {
        this.adminSanctionBy = adminSanctionBy;
    }

}
