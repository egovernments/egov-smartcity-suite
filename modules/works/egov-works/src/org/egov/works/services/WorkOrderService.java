package org.egov.works.services;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.infstr.ValidationException;
import org.egov.tender.model.GenericTenderResponse;
import org.egov.tender.model.TenderResponseLine;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.Activity;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.tender.EstimateLineItemsForWP;
import org.egov.works.models.tender.WorksPackage;
import org.egov.works.models.workorder.AbstractEstimateForWp;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.models.workorder.WorkOrderEstimate;

/**
 * This class will declare all the API's related to work order model.
 * @author prashant.gaurav
 *
 */
public interface WorkOrderService extends BaseService<WorkOrder, Long> {
	

	/**
	 * This method will set workorder number to the work order object
	 * @param entity
	 * @param workOrder
	 */
	WorkOrder setWorkOrderNumber(AbstractEstimate entity,WorkOrder workOrder,WorksPackage worksPackage);

	
	/**
	 * This method will return all the contractors which are having active work orders.
	 * @return List of ContractorDetail
	 */
	List<Contractor> getContractorsWithWO();
	
	/**
	 * This method will search list of WO's for the given criteria and eligible for MB.
	 * CriteriaMap may have:CONTRACTOR_ID,CREATE_DATE,TENDER_NO,WORKORDER_NO
	 * @param criteriaMap
	 * @return
	 */
	List<WorkOrder> searchWOForMB(Map<String,Object> criteriaMap);
	
	/**
	 * This method will search list of WO's for the given criteria and eligible for MB.
	 * CriteriaMap may have:CONTRACTOR_ID,CREATE_DATE,TENDER_NO,WORKORDER_NO,PROJECT_CODE
	 * @param criteriaMap
	 * @return
	 */
	List<WorkOrder> searchWOForBilling(Map<String,Object> criteriaMap);
	
	/**
	 * This method will search list of WO's for the given criteria and eligible to be view.
	 * CriteriaMap may have:CONTRACTOR_ID,FROM_DATE,TO_DATE,WORKORDER_NO,STATUS
	 * @param criteriaMap
	 * @return List<WorkOrder> 
	 */
	List<WorkOrder> searchWOToView(Map<String,Object> criteriaMap);
	
	/**
	 * This method will search and return list of woactivity based on searched criteria.
	 * Search criteria: WORKORDER_NO,ACTIVITY_DESC,LINEITEM_CODE
	 * Story #567 Search Line item to record measurement 
	 * @param criteriaMap
	 * @return
	 */
	List<WorkOrderActivity> searchWOActivities(Map<String,Object> criteriaMap);
	
	
	/**
	 * This method will return toPageNo for a line item from the last mb entry. 
	 * @param workOrderActivity
	 * @return
	 */
	MBHeader findLastMBPageNoForLineItem(WorkOrderActivity workOrderActivity, Long mbHeaderId);
	
//	/**
//	 * This method will check whether approval limit is already used for all line item for the WO.
//	 * @param woId
//	 * @return
//	 */
//	Boolean isApprovalLimitReachedForWO(Long woId);
	
//	/**
//	 * This method will check whether MB is created and pending for approval for the given WO.
//	 * @param woId
//	 * @return
//	 */
//	Boolean isMBCreatedAndPendingForWO(Long woId);
	
//	/**
//	 * This method will check whether final bill is already approved for wo or not.
//	 * @param woId
//	 * @return
//	 */
//	Boolean isFinalBillApprovedForWO(Long woId);
	
	List<Contractor> getAllContractorForWorkOrder();
	
	/**
	 * Check whether any MB entry is pending for approval for the given WorkOrder
	 * @param woId
	 * @return
	 */
	Boolean isMBInApprovalPendingForWO(String woNumber);
	
	/**
	 * This method will return ActivitiesForWorkorder.
	 * @param tenderResponse
	 * @return Collection<EstimateLineItemsForWP>
	 */

	
	// Collection<EstimateLineItemsForWP> getActivitiesForWorkorder(TenderResponse tenderResponse);
	 
	 /**
		 * This method will return ActivitiesForWorkorder.
		 * @param GenericTenderResponse,AbstractEstimate
		 * @return Collection<EstimateLineItemsForWP>
		 */
	 
	 Collection<EstimateLineItemsForWP> getActivitiesForWorkorder(GenericTenderResponse tenderResponse,AbstractEstimate estimate);
	 
	 /**
		 * This method will return ActivitiesForWorkorder.
		 * @param WorkOrder
		 * @return Collection<EstimateLineItemsForWP>
		 */
	 Collection<EstimateLineItemsForWP> getActivitiesForWorkorder(WorkOrder workOrder);
	/**
	 * returns headermap for pdf
	 * @param workOrder
	 * @return
	 */
	 
	public Map<String,Object> createHeaderParams(WorkOrder workOrder,String type);
		

	/**
	 * returns AbstractEstimateForWp list
	 * @param workOrder
	 * @return
	 */
	public List<AbstractEstimateForWp>  getAeForWp(WorkOrder workOrder);
	
	/**
	 * gets the security desposit appconfig value
	 * @return Double
	 */
	public Double getSecurityDepositConfValue();
	
	/**
	 * gets the LabourWelfareFund appconfig value
	 * @return Double
	 */
	public Double getLabourWelfareFundConfValue();
	
	/**
	 * This method will search list of WO's for the given criteria and eligible for returning Security Deposit.
	 * CriteriaMap may have:CONTRACTOR_ID,CREATE_DATE,TENDER_NO,WORKORDER_NO,PROJECT_CODE
	 * @param criteriaMap
	 * @return
	 */
	List<WorkOrder> searchWOForReturnSD(Map<String,Object> criteriaMap);
	
	/**
	 * This method will search list of WO's for the given criteria and eligible for retention money refund.
	 * CriteriaMap may have:CONTRACTOR_ID,CREATE_DATE,TENDER_NO,WORKORDER_NO,PROJECT_CODE
	 * @param criteriaMap
	 * @return
	 */
	List<WorkOrder> searchWOForRetentionMR(Map<String,Object> criteriaMap);
	
	/**
	 * This method will search  WorkOrder for the given billId
	 * @param billid
	 * @return workOrder
	 */
	public WorkOrder getWOForBillId(Long billId);
	
	

	/**
	 * This method will return activity   for the given tenderesponseline and estimate
	 * @param TenderResponseLine trl,AbstractEstimate estimate
	 * @return Activity
	 */
	public Activity getActivityFromTenderResponseLineAndEstimate(TenderResponseLine trl,AbstractEstimate estimate);
	
	public BigDecimal getRevisionEstimateWOAmount(WorkOrder workOrder); 
	
	
	/**
	 * This method will return list of WO which has completion date after X days for the given date 
	 * @param Date currentDate
	 * @return List<WorkOrder>
	 */
	public List<WorkOrder> getWOForCompletionDateChange(Date currentDate);
	
	/**
	 * Populate all the cumulative fields related to WOA line item
	 * @param workOrderEstimate
	 * @return
	 */
	public WorkOrderEstimate calculateCumulativeDetails(WorkOrderEstimate workOrderEstimate);
	
	/**
	 * Populate all the cumulative fields related to WOA line item
	 * @param workOrderEstimate
	 * @return
	 */
	public WorkOrderEstimate calculateCumulativeDetailsForRE(WorkOrderEstimate workOrderEstimate);
	
	
}
