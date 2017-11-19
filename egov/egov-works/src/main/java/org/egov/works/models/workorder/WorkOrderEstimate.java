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

import org.egov.infra.persistence.entity.component.Money;
import org.egov.infstr.models.BaseModel;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.milestone.entity.Milestone;
import org.egov.works.models.contractoradvance.ContractorAdvanceRequisition;
import org.egov.works.models.measurementbook.MBHeader;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class WorkOrderEstimate extends BaseModel {

    private static final long serialVersionUID = 2083096871794612166L;
    private WorkOrder workOrder;
    private AbstractEstimate estimate;
    private Date workCompletionDate;
    private double estimateWOAmount;

    private List<WorkOrderActivity> workOrderActivities = new LinkedList<WorkOrderActivity>();
    @Valid
    private List<AssetsForWorkOrder> assetValues = new LinkedList<AssetsForWorkOrder>();

    private Set<Milestone> milestone = new HashSet<Milestone>();

    private Milestone latestMilestone;

    private Set<ContractorAdvanceRequisition> contractorAdvanceRequisitions = new HashSet<ContractorAdvanceRequisition>();

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

    private Set<MBHeader> mbHeaders = new HashSet<MBHeader>();

    public Set<MBHeader> getMbHeaders() {
        return mbHeaders;
    }

    public void setMbHeaders(final Set<MBHeader> mbHeaders) {
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

    public Set<Milestone> getMilestone() {
        return milestone;
    }

    public void setMilestone(final Set<Milestone> milestone) {
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

    public Set<ContractorAdvanceRequisition> getContractorAdvanceRequisitions() {
        return contractorAdvanceRequisitions;
    }

    public void setContractorAdvanceRequisitions(final Set<ContractorAdvanceRequisition> contractorAdvanceRequisitions) {
        this.contractorAdvanceRequisitions = contractorAdvanceRequisitions;
    }

    public double getEstimateWOAmount() {
        return estimateWOAmount;
    }

    public void setEstimateWOAmount(final double estimateWOAmount) {
        this.estimateWOAmount = estimateWOAmount;
    }
}
