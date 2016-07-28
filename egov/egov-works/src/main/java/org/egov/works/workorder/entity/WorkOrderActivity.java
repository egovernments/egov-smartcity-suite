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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.GreaterThan;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.revisionestimate.entity.enums.RevisionType;

@Entity
@Table(name = "EGW_WORKORDER_ACTIVITY")
@NamedQueries({
        @NamedQuery(name = WorkOrderActivity.GETALLWORKORDERACTIVITYWITHMB, query = " Select mbDetails.workOrderActivity.id,sum(mbDetails.quantity) from MBDetails mbDetails where mbDetails.mbHeader.workOrderEstimate.id=? and mbDetails.mbHeader.egwStatus.code = ? group by mbDetails.workOrderActivity "),
        @NamedQuery(name = WorkOrderActivity.GETALLWORKORDERACTIVITYWITHOUTMB, query = "from WorkOrderActivity woe where woe.workOrderEstimate.id=? and woe.workOrderEstimate.workOrder.egwStatus.code!=? and woe.id not in  (Select distinct(mbDetails.workOrderActivity.id) from MBDetails mbDetails where mbDetails.mbHeader.workOrderEstimate.id=?  and mbDetails.mbHeader.egwStatus.code = ?) "),
        @NamedQuery(name = WorkOrderActivity.GETASSIGNEDQUANTITYFORACTIVITY, query = " Select sum(woa.approvedQuantity) from WorkOrderActivity woa where woa.workOrderEstimate.workOrder.negotiationNumber=? AND woa.workOrderEstimate.workOrder.egwStatus.code !=? group by woa.activity having woa.activity.id = ? "),
        @NamedQuery(name = WorkOrderActivity.GETTOTALQUANTITYFORWO, query = " Select sum(woa.approvedQuantity) from WorkOrderActivity woa where woa.workOrderEstimate.workOrder.negotiationNumber=? AND woa.workOrderEstimate.workOrder.egwStatus.code !=? "),
        @NamedQuery(name = WorkOrderActivity.GETTOTALQUANTITYFORNEWWO, query = "  Select sum(woa.approvedQuantity) from WorkOrderActivity woa where woa.workOrderEstimate.workOrder.negotiationNumber=? AND woa.workOrderEstimate.workOrder.egwStatus.code =?  ") })
@SequenceGenerator(name = WorkOrderActivity.SEQ_EGW_WORKORDER_ACTIVITY, sequenceName = WorkOrderActivity.SEQ_EGW_WORKORDER_ACTIVITY, allocationSize = 1)
public class WorkOrderActivity extends AbstractAuditable {

    private static final long serialVersionUID = -5986495021099638251L;

    public static final String SEQ_EGW_WORKORDER_ACTIVITY = "SEQ_EGW_WORKORDER_ACTIVITY";
    public static final String GETALLWORKORDERACTIVITYWITHMB = "getallWorkOrderActivityWithMB";
    public static final String GETALLWORKORDERACTIVITYWITHOUTMB = "getallWorkOrderActivityWithoutMB";
    public static final String GETASSIGNEDQUANTITYFORACTIVITY = "getAssignedQuantityForActivity";
    public static final String GETTOTALQUANTITYFORWO = "getTotalQuantityForWO";
    public static final String GETTOTALQUANTITYFORNEWWO = "getTotalQuantityForNewWO";

    @Id
    @GeneratedValue(generator = SEQ_EGW_WORKORDER_ACTIVITY, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKORDER_ESTIMATE_ID", nullable = false)
    private WorkOrderEstimate workOrderEstimate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESTIMATE_ACTIVITY_ID", nullable = false)
    private Activity activity;

    @Required(message = "WorkOrderActivity.approvedRate.not.null")
    @GreaterThan(value = 0, message = "WorkOrderActivity.approvedRate.non.negative")
    @Column(name = "APPROVED_RATE")
    private double approvedRate;

    @Required(message = "WorkOrderActivity.approvedQuantity.not.null")
    @GreaterThan(value = 0, message = "WorkOrderActivity.approvedQuantity.non.negative")
    @Column(name = "APPROVED_QUANTITY")
    private double approvedQuantity;

    @Column(name = "APPROVED_AMOUNT")
    private double approvedAmount;

    private String remarks;

    // Used in new/cancelled WO (for validating the approvedquantity)
    @Transient
    private double unAssignedQuantity;

    // in-memory variable for Change in quantity
    @Transient
    private WorkOrderActivity parent;

    @Transient
    private double totalEstQuantity;

    @Transient
    private double prevCumlvQuantity;

    private transient Long mbHeaderId;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "woActivity", targetEntity = WorkOrderMeasurementSheet.class)
    private List<WorkOrderMeasurementSheet> workOrderMeasurementSheets = new ArrayList<WorkOrderMeasurementSheet>();

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(final Activity activity) {
        this.activity = activity;
    }

    public double getApprovedRate() {
        return approvedRate;
    }

    public void setApprovedRate(final double approvedRate) {
        this.approvedRate = approvedRate;
    }

    public double getApprovedQuantity() {
        return approvedQuantity;
    }

    public void setApprovedQuantity(final double approvedQuantity) {
        this.approvedQuantity = approvedQuantity;
    }

    public double getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(final double approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public WorkOrderEstimate getWorkOrderEstimate() {
        return workOrderEstimate;
    }

    public void setWorkOrderEstimate(final WorkOrderEstimate workOrderEstimate) {
        this.workOrderEstimate = workOrderEstimate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public double getUnAssignedQuantity() {
        return unAssignedQuantity;
    }

    public void setUnAssignedQuantity(final double unAssignedQuantity) {
        this.unAssignedQuantity = unAssignedQuantity;
    }

    public double getConversionFactor() {
        if (workOrderEstimate.getWorkOrder().getParent() != null && activity.getRevisionType() != null
                && (activity.getRevisionType().toString().equalsIgnoreCase(RevisionType.NON_TENDERED_ITEM.toString())
                        || activity.getRevisionType().toString()
                                .equalsIgnoreCase(RevisionType.LUMP_SUM_ITEM.toString())))
            return activity.getConversionFactorForRE(workOrderEstimate.getWorkOrder().getParent().getWorkOrderDate());
        else if (workOrderEstimate.getWorkOrder().getParent() != null
                && workOrderEstimate.getEstimate().getParent() != null && activity.getRevisionType() != null
                && (activity.getRevisionType().toString().equalsIgnoreCase(RevisionType.ADDITIONAL_QUANTITY.toString())
                        || activity.getRevisionType().toString()
                                .equalsIgnoreCase(RevisionType.REDUCED_QUANTITY.toString())))
            return activity.getConversionFactorForRE(workOrderEstimate.getEstimate().getParent().getEstimateDate());
        else
            return activity.getConversionFactor();
    }

    /**
     * This method is used to return the ScheduleOfRate based on if its AbstractEstimate(estimateDate is used) or
     * RevisionEstimate(original parent workorderDate is used)
     *
     * @return a double value of sorRate
     */
    public double getScheduleOfRate() {
        double sorRate = 0.0;
        if (getActivity().getAbstractEstimate().getParent() == null)
            // Original AbstractEstimate
            sorRate = getActivity().getEstimateRate();
        else {
            Date workOrderDate = new Date();
            // RevisionEstimate
            // If parent is null or if revision type is ADDITIONAL_QUANTITY or
            // REDUCED_QUANTITY then its original WorkOrder
            // else if its Revision WorkOrder and revision type is
            // NON_TENDERED_ITEM or LUMP_SUM_ITEM then, get the WorkOrderDate
            // from parent
            workOrderDate = getWorkOrderEstimate().getWorkOrder().getParent().getWorkOrderDate();
            if (activity.getRevisionType() != null && (activity.getRevisionType().toString()
                    .equalsIgnoreCase(RevisionType.NON_TENDERED_ITEM.toString())
                    || activity.getRevisionType().toString().equalsIgnoreCase(RevisionType.LUMP_SUM_ITEM.toString())))
                sorRate = getActivity().getSORRateForDate(workOrderDate).getValue();
            else if (getActivity().getAbstractEstimate().getParent() != null && activity.getRevisionType() != null
                    && (activity.getRevisionType().toString()
                            .equalsIgnoreCase(RevisionType.ADDITIONAL_QUANTITY.toString())
                            || activity.getRevisionType().toString()
                                    .equalsIgnoreCase(RevisionType.REDUCED_QUANTITY.toString())))
                sorRate = getActivity().getSORRateForDate(workOrderEstimate.getEstimate().getParent().getEstimateDate())
                        .getValue();
            else
                sorRate = getActivity().getEstimateRate();
        }
        return sorRate;
    }

    public WorkOrderActivity getParent() {
        return parent;
    }

    public void setParent(final WorkOrderActivity parent) {
        this.parent = parent;
    }

    public double getTotalEstQuantity() {
        return totalEstQuantity;
    }

    public double getPrevCumlvQuantity() {
        return prevCumlvQuantity;
    }

    public void setTotalEstQuantity(final double totalEstQuantity) {
        this.totalEstQuantity = totalEstQuantity;
    }

    public void setPrevCumlvQuantity(final double prevCumlvQuantity) {
        this.prevCumlvQuantity = prevCumlvQuantity;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public Long getMbHeaderId() {
        return mbHeaderId;
    }

    public void setMbHeaderId(final Long mbHeaderId) {
        this.mbHeaderId = mbHeaderId;
    }

    public List<WorkOrderMeasurementSheet> getWorkOrderMeasurementSheets() {
        return workOrderMeasurementSheets;
    }

    public void setWorkOrderMeasurementSheets(final List<WorkOrderMeasurementSheet> workOrderMeasurementSheets) {
        this.workOrderMeasurementSheets = workOrderMeasurementSheets;
    }

}
