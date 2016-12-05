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
package org.egov.works.workorder.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.entity.component.Money;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.contractoradvance.entity.ContractorAdvanceRequisition;
import org.egov.works.mb.entity.MBHeader;
import org.egov.works.milestone.entity.Milestone;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "EGW_WORKORDER_ESTIMATE")
@NamedQueries({
        @NamedQuery(name = WorkOrderEstimate.GETWORKORDERESTIMATEBYWORKORDERID, query = " from WorkOrderEstimate woe where woe.workOrder.id = ? "),
        @NamedQuery(name = WorkOrderEstimate.GETWORKORDERESTIMATEBYID, query = " from WorkOrderEstimate woe where woe.estimate.id = ? "),
        @NamedQuery(name = WorkOrderEstimate.GETWORKORDERESTIMATEBYESTANDWO, query = "  from WorkOrderEstimate woe where woe.estimate.id = ? and woe.workOrder.id = ? ") })
@SequenceGenerator(name = WorkOrderEstimate.SEQ_WORKORDER_ESTIMATE, sequenceName = WorkOrderEstimate.SEQ_WORKORDER_ESTIMATE, allocationSize = 1)
public class WorkOrderEstimate extends AbstractAuditable {

    private static final long serialVersionUID = 2083096871794612166L;

    public static final String SEQ_WORKORDER_ESTIMATE = "SEQ_WORKORDER_ESTIMATE";
    public static final String GETWORKORDERESTIMATEBYWORKORDERID = "getWorkOrderEstimateByWorkOrderId";
    public static final String GETWORKORDERESTIMATEBYID = "getWorkOrderEstimateById";
    public static final String GETWORKORDERESTIMATEBYESTANDWO = "getWorkOrderEstimateByEstAndWO";

    @Id
    @GeneratedValue(generator = SEQ_WORKORDER_ESTIMATE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKORDER_ID", nullable = false)
    private WorkOrder workOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ABSTRACTESTIMATE_ID", nullable = false)
    private AbstractEstimate estimate;

    @Column(name = "WORK_COMPLETION_DATE")
    @Temporal(value = TemporalType.DATE)
    private Date workCompletionDate;

    @NotNull
    @Column(name = "ESTIMATE_WO_AMOUNT")
    private double estimateWOAmount;

    @JsonIgnore
    @OrderBy("id")
    @OneToMany(mappedBy = "workOrderEstimate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = WorkOrderActivity.class)
    private List<WorkOrderActivity> workOrderActivities = new ArrayList<WorkOrderActivity>(0);

    @Valid
    @JsonIgnore
    @OrderBy("id")
    @OneToMany(mappedBy = "workOrderEstimate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = AssetsForWorkOrder.class)
    private List<AssetsForWorkOrder> assetValues = new ArrayList<AssetsForWorkOrder>(0);

    @JsonIgnore
    @OrderBy("id")
    @OneToMany(mappedBy = "workOrderEstimate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = Milestone.class)
    private List<Milestone> milestone = new ArrayList<Milestone>(0);

    @Transient
    private Milestone latestMilestone;

    @JsonIgnore
    @OneToMany(mappedBy = "workOrderEstimate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = ContractorAdvanceRequisition.class)
    private List<ContractorAdvanceRequisition> contractorAdvanceRequisitions = new ArrayList<ContractorAdvanceRequisition>();

    @JsonIgnore
    @OneToMany(mappedBy = "workOrderEstimate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = MBHeader.class)
    private List<MBHeader> mbHeaders = new ArrayList<MBHeader>(0);

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(final WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public AbstractEstimate getEstimate() {
        return estimate;
    }

    public void setEstimate(final AbstractEstimate estimate) {
        this.estimate = estimate;
    }

    public List<WorkOrderActivity> getWorkOrderActivities() {
        return workOrderActivities;
    }

    public void setWorkOrderActivities(final List<WorkOrderActivity> workOrderActivities) {
        this.workOrderActivities = workOrderActivities;
    }

    public void addWorkOrderActivity(final WorkOrderActivity workOrderActivity) {
        workOrderActivities.add(workOrderActivity);
    }

    public Money getTotalWorkValue() {
        double amt = 0;
        for (final WorkOrderActivity workOrderActivity : workOrderActivities)
            amt += workOrderActivity.getApprovedAmount();
        return new Money(amt);
    }

    public List<MBHeader> getMbHeaders() {
        return mbHeaders;
    }

    public void setMbHeaders(final List<MBHeader> mbHeaders) {
        this.mbHeaders = mbHeaders;
    }

    public Date getWorkCompletionDate() {
        return workCompletionDate;
    }

    public void setWorkCompletionDate(final Date workCompletionDate) {
        this.workCompletionDate = workCompletionDate;
    }

    public List<AssetsForWorkOrder> getAssetValues() {
        return assetValues;
    }

    public void setAssetValues(final List<AssetsForWorkOrder> assetValues) {
        this.assetValues = assetValues;
    }

    public void addAssetValue(final AssetsForWorkOrder assetValue) {
        assetValues.add(assetValue);
    }

    public List<Milestone> getMilestone() {
        return milestone;
    }

    public void setMilestone(final List<Milestone> milestone) {
        this.milestone = milestone;
    }

    public Milestone getLatestMilestone() {
        final List<Milestone> milestoneList = new ArrayList<Milestone>();
        milestoneList.addAll(getMilestone());
        if (!milestoneList.isEmpty()) {
            Collections.sort(milestoneList, Milestone.milestoneComparator);
            latestMilestone = milestoneList.get(milestoneList.size() - 1);
        }
        return latestMilestone;
    }

    public void setLatestMilestone(final Milestone latestMilestone) {
        this.latestMilestone = latestMilestone;
    }

    public List<ContractorAdvanceRequisition> getContractorAdvanceRequisitions() {
        return contractorAdvanceRequisitions;
    }

    public void setContractorAdvanceRequisitions(
            final List<ContractorAdvanceRequisition> contractorAdvanceRequisitions) {
        this.contractorAdvanceRequisitions = contractorAdvanceRequisitions;
    }

    public double getEstimateWOAmount() {
        return estimateWOAmount;
    }

    public void setEstimateWOAmount(final double estimateWOAmount) {
        this.estimateWOAmount = estimateWOAmount;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

}
