package org.egov.works.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.revisionEstimate.RevisionAbstractEstimate;
import org.egov.works.models.measurementbook.MBDetails;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrderEstimate;

/**
 * This class will have all business logic related to MB.
 * @author prashant.gaurav
 *
 */
public interface MeasurementBookService extends BaseService<MBHeader,Long> {
	
	/**
	 * This method will search list of mbheader based on input criteria.
	 * Search Criteria : WORKORDER_NO,CONTRACTOR_ID,CREATE_DATE,MB_REF_NO,MB_PAGE_NO,STATUS
	 * Story #436 - Search MB-View MB 
	 * @param criteriaMap
	 * @return
	 */
	List<MBHeader> searchMB(Map<String,Object> criteriaMap); 
	
	
	/**
	 * Get previous cumulative amount(approved, approval pending and draft entries) based on workorder activity Id.
	 * This will search list of MBDetail and then get cmulative amount.
	 * @param woActivityId
	 * @return
	 */
	double prevCumulativeQuantity(Long woActivityId,Long mbHeaderId, Long activityId);
	
	/**
	 * Get estimated quantity for Change in quantity of RE for given work order activity
	 * @param woActivityId,mbHeaderId,activityId
	 * @return
	 */
	double totalEstimatedQuantity(Long woActivityId,Long mbHeaderId, Long activityId);
	
	/**
	 * Populate all the cumulative fields related to mbdetail line item
	 * @param woActivityId
	 * @return
	 */
	MBHeader calculateMBDetails(MBHeader mbHeader,boolean isPersistedObject,boolean isTenderPercApplicable);
	
	
	/**
	 * This method will return workorderestimates objects pending for MB
	 * @param workOrderEstimateList
	 * @param mbHeader
	 * @return List<WorkOrderEstimate>
	 */
	List<WorkOrderEstimate> getWorkOrderEstimatesForMB(List<WorkOrderEstimate> workOrderEstimateList);
	
	/**
	 * This method will return workorderestimates objects pending for Bill
	 * @param workOrderEstimateList
	 * @param mbHeader
	 * @return List<WorkOrderEstimate>
	 */
	List<WorkOrderEstimate> getWorkOrderEstimatesForBill(List<WorkOrderEstimate> workOrderEstimateList);
	
	/**
	 * 
	 * @param workOrderNumber,lineItemId
	 * @return boolean
	 */
	
	boolean isMBExistForLineItem(String workOrderNumber,long lineItemId);
	
	/**
	 * Check if mb entries are within approved limit or not.
	 * @param mbHeader
	 * @return
	 */
	boolean approvalLimitCrossed(MBDetails mbDetails);
	
	/**
	 * List of all approved MB's for which Bill is not generated or bill is cancelled.
	 * @param workOrderId
	 * @param asOnDate
	 * @return
	 */
	List<MBHeader> getApprovedMBList(Long workOrderId, Long workOrderEstimateId, Date asOnDate);
	
	/**
	 * List of all MB's were the bill is created and bill type is part bill
	 * @param workOrderId
	 * @param billtype
	 * @return
	 */
	public List<MBHeader> getPartBillList(Long workOrderId,String billtype);
	
	/**
	 * returns latest bill date for MB
	 * @param workOrderId
	 * @return
	 */
	public Date getLatestBillDateForMB(Long workOrderId);


	public BigDecimal getRevisionEstimateMBAmount(AbstractEstimate abstractEstimate);
	
	public MBHeader getRevisionEstimateMB(RevisionAbstractEstimate revisionAbstractEstimate);
	
	/**
	 * Get estimated quantity for Change in quantity of RE for given work order activity
	 * @param woActivityId,estimateId,activityId
	 * @return
	 */
	double totalEstimatedQuantityForRE(Long woActivityId,Long estimateId, Long activityId);

}
