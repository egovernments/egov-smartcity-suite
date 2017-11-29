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
package org.egov.works.services;

import org.egov.works.models.measurementbook.MBDetails;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This class will have all business logic related to MB.
 *
 * @author prashant.gaurav
 */
public interface MeasurementBookService extends BaseService<MBHeader, Long> {

    /**
     * This method will search list of mbheader based on input criteria. Search Criteria :
     * WORKORDER_NO,CONTRACTOR_ID,CREATE_DATE,MB_REF_NO,MB_PAGE_NO,STATUS Story #436 - Search MB-View MB
     *
     * @param criteriaMap
     * @param parameterList TODO
     * @return
     */
    List<String> searchMB(Map<String, Object> criteriaMap, List<Object> parameterList);

    /**
     * Get previous cumulative amount(approved, approval pending and draft entries) based on workorder activity Id. This will
     * search list of MBDetail and then get cmulative amount.
     *
     * @param woActivityId
     * @return
     */
    double prevCumulativeQuantity(Long woActivityId, Long mbHeaderId);

    /**
     * Populate all the cumulative fields related to mbdetail line item
     *
     * @param woActivityId
     * @return
     */
    MBHeader calculateMBDetails(MBHeader mbHeader, boolean isPersistedObject);

    /**
     * This method will return workorderestimates objects pending for MB
     *
     * @param workOrderEstimateList
     * @param mbHeader
     * @return List<WorkOrderEstimate>
     */
    List<WorkOrderEstimate> getWorkOrderEstimatesForMB(List<WorkOrderEstimate> workOrderEstimateList);

    /**
     * This method will return workorderestimates objects pending for Bill
     *
     * @param workOrderEstimateList
     * @param mbHeader
     * @return List<WorkOrderEstimate>
     */
    List<WorkOrderEstimate> getWorkOrderEstimatesForBill(List<WorkOrderEstimate> workOrderEstimateList);

    /**
     * This method will return workorderestimates objects pending for Bill , but it will not consider Legacy MBs
     *
     * @param workOrderEstimateList
     * @param mbHeader
     * @return List<WorkOrderEstimate>
     */
    List<WorkOrderEstimate> getWOEstForBillExludingLegacyMB(List<WorkOrderEstimate> workOrderEstimateList);

    /**
     * @param workOrderNumber ,lineItemId
     * @return boolean
     */

    boolean isMBExistForLineItem(String workOrderNumber, long lineItemId);

    /**
     * Check if mb entries are within approved limit or not.
     *
     * @param mbHeader
     * @return
     */
    Boolean approvalLimitCrossed(MBDetails mbDetails);

    /**
     * List of all approved MB's for which Bill is not generated or bill is cancelled.
     *
     * @param workOrderId
     * @param asOnDate
     * @return
     */
    List<MBHeader> getApprovedMBList(Long workOrderId, Long workOrderEstimateId, Date asOnDate);

    /**
     * List of all MB's were the bill is created and bill type is part bill
     *
     * @param workOrderId
     * @param billtype
     * @return
     */
    public List<MBHeader> getPartBillList(Long workOrderId, String billtype);

    /**
     * returns latest bill date for MB
     *
     * @param workOrderId
     * @return
     */
    public Date getLatestBillDateForMB(Long workOrderId);

    /**
     * returns latest bill date for MB
     *
     * @param workOrderEstimateId
     * @return
     */
    public Date getLatestBillDateForMBPassingWOEstimate(Long workOrderEstimateId);

    /**
     * Returns the total estimated quantity for a particular work order activity
     *
     * @param woActivityId
     * @param estimateId
     * @param activityId
     * @param workOrder
     * @return
     */
    public double totalEstimatedQuantityForRE(Long woActivityId, Long estimateId, Long activityId, WorkOrder workOrder);

    public double prevCumulativeQuantityIncludingCQ(Long woActivityId, Long mbHeaderId, Long activityId,
            WorkOrder workOrder);

    /**
     * Similar to totalEstimatedQuantityForRE but will consider only previous REs and not all REs
     *
     * @param woActivityId ,estimateId,activityId,workOrder
     * @return
     */
    double totalEstimatedQuantityForPreviousREs(Long woActivityId, Long estimateId, Long activityId, WorkOrder workOrder);

    /**
     * This is used by MB screens. This returns the estimated quantity for an work order activity This returns original activity
     * quantity + all change quantities of the activity for all associated REs
     *
     * @param woActivityId ,mbHeaderId,activityId,workOrder
     * @return
     */
    public double totalEstimatedQuantity(Long woActivityId, Long mbHeaderId, Long activityId, WorkOrder workOrder);

    /**
     * This is used in validating the sum of all MBs against the estimate wise WO amount This returns sum of all previous MBs
     *
     * @param workOrderEstimate ,negoPerc,tenderType
     * @return
     */
    public BigDecimal getTotalMBAmountForPrevMBs(WorkOrderEstimate workOrderEstimate, double negoPerc, String tenderType);

    /**
     * This returns sum of MB amount for the passed workOrderId and estimateId
     *
     * @param workOrderId ,estimateId
     * @return
     */
    public BigDecimal getTotalMBAmount(Long workOrderId, Long estimateId);

    /**
     * retrurns work commenced date
     *
     * @param workOrderID
     * @return
     */
    public Date getWorkCommencedDate(Long woId);

    /**
     * returns last MB created date
     *
     * @param EstimateId , workOrderId
     * @return
     */
    public Date getLastMBCreatedDate(Long woId, Long estId);
}
