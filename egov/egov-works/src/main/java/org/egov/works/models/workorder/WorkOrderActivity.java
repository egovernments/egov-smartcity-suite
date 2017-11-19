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
package org.egov.works.models.workorder;

import org.egov.infra.persistence.validator.annotation.GreaterThan;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infstr.models.BaseModel;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.revisionestimate.entity.enums.RevisionType;

import java.util.Date;

public class WorkOrderActivity extends BaseModel {

    private static final long serialVersionUID = -5986495021099638251L;
    private WorkOrderEstimate workOrderEstimate;
    private Activity activity;

    @Required(message = "WorkOrderActivity.approvedRate.not.null")
    @GreaterThan(value = 0, message = "WorkOrderActivity.approvedRate.non.negative")
    private double approvedRate;

    @Required(message = "WorkOrderActivity.approvedQuantity.not.null")
    @GreaterThan(value = 0, message = "WorkOrderActivity.approvedQuantity.non.negative")
    private double approvedQuantity;

    private double approvedAmount;
    private String remarks;

    // Used in new/cancelled WO (for validating the approvedquantity)
    private double unAssignedQuantity;

    // in-memory variable for Change in quantity
    private WorkOrderActivity parent;
    private double totalEstQuantity;
    private double prevCumlvQuantity;

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
        if (workOrderEstimate.getWorkOrder().getParent() != null
                && activity.getRevisionType() != null
                && (activity.getRevisionType().toString().equalsIgnoreCase(RevisionType.NON_TENDERED_ITEM.toString()) || activity
                        .getRevisionType().toString().equalsIgnoreCase(RevisionType.LUMP_SUM_ITEM.toString())))
            return activity.getConversionFactorForRE(workOrderEstimate.getWorkOrder().getParent().getWorkOrderDate());
        else if (workOrderEstimate.getWorkOrder().getParent() != null
                && workOrderEstimate.getEstimate().getParent() != null
                && activity.getRevisionType() != null
                && (activity.getRevisionType().toString().equalsIgnoreCase(RevisionType.ADDITIONAL_QUANTITY.toString())
                        || activity
                                .getRevisionType().toString().equalsIgnoreCase(RevisionType.REDUCED_QUANTITY.toString())))
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
            sorRate = getActivity().getSORCurrentRate().getValue();
        else {
            Date workOrderDate = new Date();
            // RevisionEstimate
            // If parent is null or if revision type is ADDITIONAL_QUANTITY or
            // REDUCED_QUANTITY then its original WorkOrder
            // else if its Revision WorkOrder and revision type is
            // NON_TENDERED_ITEM or LUMP_SUM_ITEM then, get the WorkOrderDate
            // from parent
            workOrderDate = getWorkOrderEstimate().getWorkOrder().getParent().getWorkOrderDate();
            if (activity.getRevisionType() != null
                    && (activity.getRevisionType().toString()
                            .equalsIgnoreCase(RevisionType.NON_TENDERED_ITEM.toString()) || activity.getRevisionType()
                                    .toString().equalsIgnoreCase(RevisionType.LUMP_SUM_ITEM.toString())))
                sorRate = getActivity().getSORRateForDate(workOrderDate).getValue();
            else if (getActivity().getAbstractEstimate().getParent() != null
                    && activity.getRevisionType() != null
                    && (activity.getRevisionType().toString()
                            .equalsIgnoreCase(RevisionType.ADDITIONAL_QUANTITY.toString()) || activity
                                    .getRevisionType().toString().equalsIgnoreCase(RevisionType.REDUCED_QUANTITY.toString())))
                sorRate = getActivity()
                        .getSORRateForDate(workOrderEstimate.getEstimate().getParent().getEstimateDate()).getValue();
            else
                sorRate = getActivity().getSORCurrentRate().getValue();
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

}
