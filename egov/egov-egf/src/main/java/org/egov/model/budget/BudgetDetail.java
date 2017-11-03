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
package org.egov.model.budget;

import org.egov.commons.CFunction;
import org.egov.commons.EgwStatus;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.pims.commons.Position;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.egov.model.budget.BudgetDetail.SEQ_BUDGETDETAIL;

@Entity
@Table(name = "EGF_BUDGETDETAIL")
@SequenceGenerator(name = SEQ_BUDGETDETAIL, sequenceName = SEQ_BUDGETDETAIL, allocationSize = 1)
public class BudgetDetail extends StateAware<Position> {
    public static final String SEQ_BUDGETDETAIL = "SEQ_EGF_BUDGETDETAIL";
    private static final long serialVersionUID = 5908792258911500512L;
    @Id
    @GeneratedValue(generator = SEQ_BUDGETDETAIL, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Transient
    private Long nextYrId = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budgetgroup")
    private BudgetGroup budgetGroup;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget")
    private Budget budget;

    private BigDecimal originalAmount = new BigDecimal("0.0");
    private BigDecimal approvedAmount = new BigDecimal("0.0");

    @Transient
    private BigDecimal nextYroriginalAmount = new BigDecimal("0.0");

    @Transient
    private BigDecimal nextYrapprovedAmount = new BigDecimal("0.0");
    private BigDecimal budgetAvailable = new BigDecimal("0.0");

    @Column(name = "anticipatory_amount")
    private BigDecimal anticipatoryAmount = new BigDecimal("0.0");

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "using_department")
    private Department usingDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executing_department")
    private Department executingDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "function")
    private CFunction function;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme")
    private Scheme scheme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fund")
    private Fund fund;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subScheme")
    private SubScheme subScheme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "functionary")
    private Functionary functionary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boundary")
    private Boundary boundary;

    @Length(max = 10)
    private String materializedPath;

    @OneToMany(mappedBy = "budgetDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<BudgetReAppropriation> budgetReAppropriations = new HashSet<>(
            0);

    @Column(name = "document_number")
    private Long documentNumber;

    @Length(max = 32)
    private String uniqueNo;
    private BigDecimal planningPercent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status")
    private EgwStatus status;

    @Transient
    private String comment;

    public Set<BudgetReAppropriation> getBudgetReAppropriations() {
        return budgetReAppropriations;
    }

    public void setBudgetReAppropriations(
            final Set<BudgetReAppropriation> budgetReAppropriations) {
        this.budgetReAppropriations = budgetReAppropriations;
    }

    public BigDecimal getAnticipatoryAmount() {
        return anticipatoryAmount;
    }

    public void setAnticipatoryAmount(final BigDecimal anticipatoryAmount) {
        this.anticipatoryAmount = anticipatoryAmount;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public BigDecimal getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(final BigDecimal fixedAmount) {
        approvedAmount = fixedAmount;
    }

    public Department getUsingDepartment() {
        return usingDepartment;
    }

    public void setUsingDepartment(final Department department) {
        usingDepartment = department;
    }

    public Department getExecutingDepartment() {
        return executingDepartment;
    }

    public void setExecutingDepartment(final Department department) {
        executingDepartment = department;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(final CFunction function) {
        this.function = function;
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

    public void setSubScheme(final SubScheme subscheme) {
        subScheme = subscheme;
    }

    public Functionary getFunctionary() {
        return functionary;
    }

    public void setFunctionary(final Functionary functionary) {
        this.functionary = functionary;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(final Budget budget) {
        this.budget = budget;
    }

    public BigDecimal getBudgetAvailable() {
        return budgetAvailable;
    }

    public void setBudgetAvailable(final BigDecimal budgetAvailable) {
        this.budgetAvailable = budgetAvailable;
    }

    public BudgetGroup getBudgetGroup() {
        return budgetGroup;
    }

    public void setBudgetGroup(final BudgetGroup budgetGroup) {
        this.budgetGroup = budgetGroup;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(final BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(final Boundary boundaryID) {
        boundary = boundaryID;
    }

    @Override
    public String getStateDetails() {
        return getBudget().getName() + "-" + getFunction().getName();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    /**
     * @return the materializedPath
     */
    public String getMaterializedPath() {
        return materializedPath;
    }

    /**
     * @param materializedPath the materializedPath to set
     */
    public void setMaterializedPath(final String materializedPath) {
        this.materializedPath = materializedPath;
    }

    public void copyFrom(final BudgetDetail detail) {
        budget = detail.getBudget();
        budgetGroup = detail.getBudgetGroup();
        executingDepartment = detail.getExecutingDepartment();
        usingDepartment = detail.getUsingDepartment();
        function = detail.getFunction();
        functionary = detail.getFunctionary();
        boundary = detail.getBoundary();
        fund = detail.getFund();
        scheme = detail.getScheme();
        subScheme = detail.getSubScheme();
    }

    public List<BudgetReAppropriation> getNonApprovedReAppropriations() {
        final List<BudgetReAppropriation> reAppList = new ArrayList<>();
        budgetReAppropriations = budgetReAppropriations == null
                ? new HashSet<>()
                : budgetReAppropriations;
        for (final BudgetReAppropriation entry : budgetReAppropriations)
            if (!entry.getStatus().getDescription()
                    .equalsIgnoreCase("Approved"))
                reAppList.add(entry);
        return reAppList;
    }

    public BigDecimal getApprovedReAppropriationsTotal() {
        BigDecimal total = BigDecimal.ZERO;
        budgetReAppropriations = budgetReAppropriations == null
                ? new HashSet<>()
                : budgetReAppropriations;
        for (final BudgetReAppropriation entry : budgetReAppropriations)
            if (!entry.getStatus().getDescription()
                    .equalsIgnoreCase("Cancelled"))
                if ((entry.getAdditionAmount() != null)
                        && BigDecimal.ZERO
                        .compareTo(entry.getAdditionAmount()) != 0)
                    total = total.add(entry.getAdditionAmount());
                else
                    total = total.subtract(entry.getDeductionAmount());
        return total;
    }

    public BigDecimal getApprovedReAppropriationsTotalAsOnDate(
            final Date asOnDate) {
        BigDecimal total = BigDecimal.ZERO;
        budgetReAppropriations = budgetReAppropriations == null
                ? new HashSet<>()
                : budgetReAppropriations;
        for (final BudgetReAppropriation entry : budgetReAppropriations)
            if (!entry.getStatus().getDescription()
                    .equalsIgnoreCase("Cancelled")
                    && entry.getCreatedDate().before(asOnDate))
                if ((entry.getAdditionAmount() != null)
                        && BigDecimal.ZERO
                        .compareTo(entry.getAdditionAmount()) != 0)
                    total = total.add(entry.getAdditionAmount());
                else
                    total = total.subtract(entry.getDeductionAmount());
        return total;
    }

    public boolean compareTo(final BudgetDetail other) {
        boolean same = true;
        if ((budgetGroup != null) && (other.budgetGroup != null)
                && !budgetGroup.getId().equals(other.getBudgetGroup().getId()))
            same = false;
        if ((function != null) && (other.function != null)
                && !function.getId().equals(other.getFunction().getId()))
            same = false;
        if ((fund != null) && (other.fund != null)
                && !fund.getId().equals(other.getFund().getId()))
            same = false;
        if ((functionary != null) && (other.functionary != null)
                && !functionary.getId().equals(other.getFunctionary().getId()))
            same = false;
        if ((boundary != null) && (other.boundary != null)
                && !boundary.getId().equals(other.getBoundary().getId()))
            same = false;
        if ((executingDepartment != null) && (other.executingDepartment != null)
                && !executingDepartment.getId()
                .equals(other.getExecutingDepartment().getId()))
            same = false;
        if ((scheme != null) && (other.scheme != null)
                && !scheme.getId().equals(other.getScheme().getId()))
            same = false;
        if ((subScheme != null) && (other.subScheme != null)
                && !subScheme.getId().equals(other.getSubScheme().getId()))
            same = false;
        return same;
    }

    public void addApprovedReAppropriationAmount() {
        final BigDecimal reAppAmount = getApprovedReAppropriationsTotal();
        approvedAmount.add(reAppAmount == null ? BigDecimal.ZERO : reAppAmount);
    }

    public Long getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(final Long documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getUniqueNo() {
        return uniqueNo;
    }

    public void setUniqueNo(final String uniqueNo) {
        this.uniqueNo = uniqueNo;
    }

    public BigDecimal getNextYroriginalAmount() {
        return nextYroriginalAmount;
    }

    public void setNextYroriginalAmount(final BigDecimal nextYroriginalAmount) {
        this.nextYroriginalAmount = nextYroriginalAmount;
    }

    public BigDecimal getNextYrapprovedAmount() {
        return nextYrapprovedAmount;
    }

    public void setNextYrapprovedAmount(final BigDecimal nextYrapprovedAmount) {
        this.nextYrapprovedAmount = nextYrapprovedAmount;
    }

    public Long getNextYrId() {
        return nextYrId;
    }

    public void setNextYrId(final Long nextYrId) {
        this.nextYrId = nextYrId;
    }

    public BigDecimal getPlanningPercent() {
        return planningPercent;
    }

    public void setPlanningPercent(final BigDecimal planningPercent) {
        this.planningPercent = planningPercent;
    }

    @Override
    public String myLinkId() {
        return getBudget().getId().toString();
    }

    public void setWfState(final State state) {
        //Won't work
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(final EgwStatus status) {
        this.status = status;
    }

}
