/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.measurementbook.MBDetails;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.MeasurementBookService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.joda.time.DateTime;

/**
 * This class will expose all measurment book related operations.
 * 
 * NOTE ::: Suffix CQ and RE denote Change Quantity and Revision Estimate Respectively
 * 
 * @author prashant.gaurav
 *
 */
public class MeasurementBookServiceImpl extends BaseServiceImpl<MBHeader, Long>
										implements MeasurementBookService{

	private static final Logger logger 			= Logger.getLogger(MeasurementBookServiceImpl.class);
	public static final String WORKORDER_NO		= "WORKORDER_NO";
	public static final String CONTRACTOR_ID	= "CONTRACTOR_ID";
	public static final String CREATE_DATE		= "CREATE_DATE";
	public static final String MB_REF_NO		= "MB_REF_NO";
	public static final String MB_PAGE_NO		= "MB_PAGE_NO";
	public static final String STATUS			= "STATUS";
	public static final String BILLDATE			= "BILLDATE";
	public static final String BILLSTATUS		= "BILLSTATUS";
	public static final String BILLNO			= "BILLNO";
	public static final String ALL_STATUS       ="ALL_STATUS";
	public static final String FROM_DATE        ="FROM_DATE";
	public static final String TO_DATE          ="TO_DATE";
	public static final String EST_NO          ="EST_NO";
	public static final String DEPT_ID         ="DEPT_ID";
	private WorksService worksService;
	

	public MeasurementBookServiceImpl(PersistenceService<MBHeader, Long> persistenceService) {
		super(persistenceService);
	}

	/**
	 * This method will search list of mbheader based on input criteria.
	 * Search Criteria : WORKORDER_NO,CONTRACTOR_ID,CREATE_DATE,MB_REF_NO,MB_PAGE_NO,STATUS
	 * Story #436 - Search MB-View MB
	 * @param criteriaMap
	 * @return
	 */
	public List<String> searchMB(Map<String,Object> criteriaMap, List<Object> paramList){ 
		logger.debug("-------Inside searchMB method-----------------------");
		List<String> mbHeaderQryList=new ArrayList<String>();
		String countQry= "select count(distinct mbh) from MBHeader mbh where mbh.id != null and mbh.egwStatus.code != 'NEW' " ;
		String resultQry= "select distinct mbh from MBHeader mbh where mbh.id != null and mbh.egwStatus.code != 'NEW'" ;
		StringBuffer commonFilter =new StringBuffer();
			
		
		//List<Object> paramList = new ArrayList<Object>();

		if(criteriaMap.get(WORKORDER_NO) != null){
			commonFilter = commonFilter.append( " and mbh.workOrder.workOrderNumber like ?");
			paramList.add("%"+criteriaMap.get(WORKORDER_NO)+"%");
		}
		if(criteriaMap.get(CONTRACTOR_ID) != null && !"-1".equals(criteriaMap.get(CONTRACTOR_ID))) {
			commonFilter = commonFilter.append( " and mbh.workOrder.contractor.id = ?");
			paramList.add(criteriaMap.get(CONTRACTOR_ID));
		}
		if(criteriaMap.get(DEPT_ID) != null && !"-1".equals(criteriaMap.get(DEPT_ID))) {
			commonFilter = commonFilter.append( " and mbh.workOrderEstimate.estimate.executingDepartment.id = ?");
			paramList.add(criteriaMap.get(DEPT_ID));
		}
		if(criteriaMap.get(CREATE_DATE) != null) {
			commonFilter = commonFilter.append( " and mbh.mbDate = ?");
			paramList.add(criteriaMap.get(CREATE_DATE));
		}
		if(criteriaMap.get(FROM_DATE) != null && criteriaMap.get(TO_DATE)==null) {
			commonFilter = commonFilter.append( " and mbh.mbDate >= ? ");
			paramList.add(criteriaMap.get(FROM_DATE));

		}else if(criteriaMap.get(TO_DATE) != null && criteriaMap.get(FROM_DATE)==null) {
			commonFilter = commonFilter.append( " and mbh.mbDate <= ? ");
			paramList.add(criteriaMap.get(TO_DATE));
		}else if(criteriaMap.get(FROM_DATE) != null && criteriaMap.get(TO_DATE)!=null) {
			commonFilter = commonFilter.append( " and mbh.mbDate between ? and ? ");
			paramList.add(criteriaMap.get(FROM_DATE));
			paramList.add(criteriaMap.get(TO_DATE));
		}
		
		if(criteriaMap.get(EST_NO) != null){
			commonFilter = commonFilter.append( " and mbh.workOrderEstimate.estimate.estimateNumber like ?");
			paramList.add("%"+criteriaMap.get(EST_NO)+"%");
		}
		
		if(criteriaMap.get(MB_REF_NO) != null) {
			commonFilter = commonFilter.append( " and mbh.mbRefNo = ?");
			paramList.add(criteriaMap.get(MB_REF_NO));
		}
		if(criteriaMap.get(MB_PAGE_NO) != null) {
			commonFilter = commonFilter.append( " and ? between mbh.fromPageNo and mbh.toPageNo ");
			paramList.add(criteriaMap.get(MB_PAGE_NO));
		}
		if("1".equals(criteriaMap.get(ALL_STATUS))){
			commonFilter = commonFilter.append( " and mbh.egBillregister.id is not null"); 
		}
		else{
			if(!"-1".equals(criteriaMap.get(STATUS)) && criteriaMap.get(STATUS) != null  
					&& (criteriaMap.get(STATUS).equals(MBHeader.MeasurementBookStatus.APPROVED.toString()) 
							|| criteriaMap.get(STATUS).equals(MBHeader.MeasurementBookStatus.CANCELLED.toString()))) {
				commonFilter = commonFilter.append( " and mbh.egwStatus.code = ?"); 
				paramList.add(criteriaMap.get(STATUS));
			}
			else if(!"-1".equals(criteriaMap.get(STATUS)) && criteriaMap.get(STATUS) != null){
				commonFilter = commonFilter.append( " and mbh.egwStatus.code = ?"); 
				paramList.add(criteriaMap.get(STATUS));
			}
		}
		//Adding criteria for search bill-Sreekanth D.
		if(criteriaMap.get(BILLDATE) != null){
			commonFilter = commonFilter.append( " and trunc(mbh.egBillregister.billdate) = ?");
			paramList.add(criteriaMap.get(BILLDATE));
		}
		if(criteriaMap.get(BILLSTATUS) != null && !criteriaMap.get(BILLSTATUS).equals("-1")){
			commonFilter = commonFilter.append( " and mbh.egBillregister.status.code like ?");
			paramList.add("%"+criteriaMap.get(BILLSTATUS)+"%");
		}
		if(criteriaMap.get(BILLNO) != null){
			commonFilter = commonFilter.append( " and mbh.egBillregister.billnumber= ? ");
			paramList.add(criteriaMap.get(BILLNO));
		}
		
		commonFilter = commonFilter.append( " order by mbh.mbDate ");
		
		String searchQry=resultQry + commonFilter;
		String countResultQry=countQry + commonFilter;
		mbHeaderQryList.add(searchQry);
		mbHeaderQryList.add(countResultQry);

		return mbHeaderQryList;
	}

	/**
	 * Get previous cumulative amount(approved, approval pending and draft entries) based on workorder activity Id.
	 * This will search list of MBDetail and then get cmulative amount.
	 * @param woActivityId
	 * @return
	 */
	public double prevCumulativeQuantity(Long woActivityId,Long mbHeaderId){
		if(mbHeaderId==null)
			mbHeaderId=-1l;
		Object[] params = new Object[]{mbHeaderId,mbHeaderId,WorksConstants.CANCELLED_STATUS,woActivityId};
		Double pQuant = (Double) genericService.findByNamedQuery("prevCumulativeQuantity",params);
		if(pQuant==null)
			return 0.0d;
		else
			return pQuant.doubleValue();
	}

	public double prevCumulativeQuantityIncludingCQ(Long woActivityId,Long mbHeaderId, Long activityId, WorkOrder workOrder){
		if(mbHeaderId==null)
			mbHeaderId=-1l;
		Object[] params = new Object[]{mbHeaderId,mbHeaderId,WorksConstants.CANCELLED_STATUS, workOrder,workOrder, woActivityId, activityId};
		Double pQuant = (Double) genericService.findByNamedQuery("prevCumulativeQuantityForCQ",params);
		params = new Object[]{mbHeaderId,mbHeaderId, WorksConstants.CANCELLED_STATUS ,activityId};
		Double pQuantRE = (Double) genericService.findByNamedQuery("prevCumulativeQuantityForREs",params);
		if(pQuant!=null && pQuantRE!=null)
			pQuant=pQuant+pQuantRE;
		if(pQuant==null && pQuantRE!=null)
			pQuant=pQuantRE;
		if(pQuant==null)
			return 0.0d;
		else
			return pQuant.doubleValue();
	}
	
	/**
	 * This is used by RE screens. 
	 * Get estimated quantity for Change in quantity of RE for given work order activity.
	 * Here work order activities of only non-cancelled Abstract Estimates/REs are considered 
	 * @param woActivityId,estimateId,activityId
	 * @return
	 */
	public double totalEstimatedQuantityForRE(Long woActivityId,Long estimateId, Long activityId, WorkOrder workOrder){
		if(estimateId==null)
			estimateId=-1l; 
		
		Object[] params = null;		
		Double estQuantity = null;
		
		params = new Object[]{estimateId,workOrder,workOrder,woActivityId, activityId};
		estQuantity = (Double) genericService.findByNamedQuery("totalEstimatedQuantityInRE",params);
		
		Double estQuantityRE = null;  
		
		if(workOrder.getParent() != null)
			params = new Object[]{estimateId,workOrder.getParent(),workOrder.getParent(), activityId};
		else
			params = new Object[]{estimateId,workOrder,workOrder, activityId};
		estQuantityRE = (Double) genericService.findByNamedQuery("totalEstimatedQuantityForREinRE",params);
		
		if(estQuantity!=null && estQuantityRE!=null)
			estQuantity=estQuantity+estQuantityRE;
		if(estQuantity==null && estQuantityRE!=null)
			estQuantity=estQuantityRE;
		if(estQuantity==null)
			return 0.0d;
		else
			return estQuantity.doubleValue();
	}
	
	/**
	 * Similar to totalEstimatedQuantityForRE but will consider only previous REs and not all REs
	 * @param woActivityId,estimateId,activityId, workOrder
	 * @return
	 */
	public double totalEstimatedQuantityForPreviousREs(Long woActivityId,Long estimateId, Long activityId, WorkOrder workOrder){
		if(estimateId==null)
			estimateId=-1l; 
		
		Object[] params = null;		
		Double estQuantity = null;
		
		params = new Object[]{estimateId,workOrder,workOrder, woActivityId, activityId};
		estQuantity = (Double) genericService.findByNamedQuery("totalEstimatedQuantityInRE",params);
		
		Double estQuantityRE = null;
		
		params = new Object[]{estimateId, workOrder,workOrder, activityId};
		estQuantityRE = (Double) genericService.findByNamedQuery("totalEstimatedQuantityForPreviousREs",params);
		
		if(estQuantity!=null && estQuantityRE!=null)
			estQuantity=estQuantity+estQuantityRE;
		if(estQuantity==null && estQuantityRE!=null)
			estQuantity=estQuantityRE;
		if(estQuantity==null)
			return 0.0d;
		else
			return estQuantity.doubleValue();
	}
	
	
	/**
	 * This method will return workorderestimates objects pending for MB
	 * @param workOrderEstimateList
	 * @param mbHeader
	 * @return List<WorkOrderEstimate>
	 */
	public List<WorkOrderEstimate> getWorkOrderEstimatesForMB(List<WorkOrderEstimate> workOrderEstimateList){
		List<WorkOrderEstimate> woEstimateList = new ArrayList<WorkOrderEstimate>();
		List<WorkOrderEstimate> usedWOEstimateList = new ArrayList<WorkOrderEstimate>();
		Double approvedQuantity = 0D;
		Double usedQuantity 	= 0D;
		Double extraPercentage = worksService.getConfigval();
		//Approved quantity for workorder
		for(WorkOrderEstimate woe:workOrderEstimateList) {
			for(WorkOrderActivity woActivity:woe.getWorkOrderActivities()){
				approvedQuantity += woActivity.getApprovedQuantity();
			}
			if(extraPercentage.doubleValue()>0)
				approvedQuantity=approvedQuantity*(1+(extraPercentage/100));
			List<MBHeader> mbHeaderList =  findAllByNamedQuery("getMBbyWorkOrderEstID", woe.getId(),WorksConstants.CANCELLED_STATUS,getFinalBillTypeConfigValue(), WorksConstants.CANCELLED_STATUS);
			if(mbHeaderList!=null && !mbHeaderList.isEmpty()){
				usedQuantity = getUsedQuantity(usedQuantity, mbHeaderList);
				for(MBHeader mbHeader:mbHeaderList){
					for(MBDetails mbDetails:mbHeader.getMbDetails()){
						if("Required".equals(worksService.getWorksConfigValue("ORDER_NUMBER_REQUIRED"))){
							addUnlimitedWorkOrderEstimatesForMB(woEstimateList,usedWOEstimateList, woe,
									mbHeader, mbDetails);
						}
						else{
							addLimitedWorkOrderEstimatesForMB(woEstimateList,usedWOEstimateList,
									approvedQuantity, usedQuantity, woe,
									mbHeader, mbDetails);
						}
					}
				}
			}
			else if(!woEstimateList.contains(woe)){
				addNewWorkOrderEstimatesForMB(woEstimateList, woe);
			}
		}
		for(WorkOrderEstimate woe:usedWOEstimateList){
			woEstimateList.remove(woe);
		}
		return woEstimateList;
	}

	private void addNewWorkOrderEstimatesForMB(
			List<WorkOrderEstimate> woEstimateList, WorkOrderEstimate woe) {
		if(woe.getMbHeaders().isEmpty()){
			woEstimateList.add(woe);
		}else{
			for(MBHeader mbh:woe.getMbHeaders()){
				if(mbh.getEgwStatus()!=null 
						&& mbh.getEgwStatus().getCode().equals(MBHeader.MeasurementBookStatus.CANCELLED.toString()) 
						&& !woEstimateList.contains(woe)){
					woEstimateList.add(woe);
				}
			}
		}
	}

	private void addUnlimitedWorkOrderEstimatesForMB(
			List<WorkOrderEstimate> woEstimateList,List<WorkOrderEstimate> usedWOEstimateList,WorkOrderEstimate woe,
			MBHeader mbHeader, MBDetails mbDetails) {
		if(//woe.getWorkOrderActivities().contains(mbDetails.getWorkOrderActivity()) &&
				mbHeader.getEgwStatus()!=null 
				&& mbHeader.getEgwStatus().getCode().
					equalsIgnoreCase(MBHeader.MeasurementBookStatus.APPROVED.toString())){
			if(!woEstimateList.contains(woe)){
				woEstimateList.add(woe);
			}
		}
		else
		{
			if(woEstimateList.contains(woe)){
				usedWOEstimateList.add(woe);
			}
		}
	}

	private void addLimitedWorkOrderEstimatesForMB(
			List<WorkOrderEstimate> woEstimateList,List<WorkOrderEstimate> usedWOEstimateList, Double approvedQuantity,
			Double usedQuantity, WorkOrderEstimate woe, MBHeader mbHeader,
			MBDetails mbDetails) {
		if(usedQuantity < approvedQuantity && woe.getWorkOrderActivities().contains(mbDetails.getWorkOrderActivity())
				&& mbHeader.getEgwStatus()!=null 
				&& mbHeader.getEgwStatus().getCode().
					equalsIgnoreCase(MBHeader.MeasurementBookStatus.APPROVED.toString())){
			if(!woEstimateList.contains(woe)){
				woEstimateList.add(woe);
			}
		}
		else
		{
			if(woEstimateList.contains(woe)){
				usedWOEstimateList.add(woe);
			}
		}
	}

	private Double getUsedQuantity(Double usedQuantity,
			List<MBHeader> mbHeaderList) {
		Double usedQty 	= usedQuantity;
		for(MBHeader mbHeader:mbHeaderList){
			if(mbHeader!=null && mbHeader.getEgwStatus()!=null 
					&& !mbHeader.getEgwStatus().getCode().
							equalsIgnoreCase(MBHeader.MeasurementBookStatus.CANCELLED.toString())) {
				for(MBDetails mbDetails:mbHeader.getMbDetails()){
					usedQty += mbDetails.getQuantity();
				}
			}
		}
		return usedQty;
	}
	
	/**
	 * This method will return workorderestimates objects pending for Bill
	 * @param workOrderEstimateList
	 * @param mbHeader
	 * @return List<WorkOrderEstimate>
	 */
	public List<WorkOrderEstimate> getWorkOrderEstimatesForBill(List<WorkOrderEstimate> workOrderEstimateList){
		List<WorkOrderEstimate> woEstimateList = new ArrayList<WorkOrderEstimate>();
		//Approved quantity for workorder
		for(WorkOrderEstimate woe:workOrderEstimateList) {			
			List<MBHeader> mbHeaderList =  findAllByNamedQuery("getMBbyWorkOrderEstID", woe.getId(),WorksConstants.CANCELLED_STATUS,getFinalBillTypeConfigValue(), WorksConstants.CANCELLED_STATUS);
			if(mbHeaderList!=null && !mbHeaderList.isEmpty()){				
				for(MBHeader mbHeader:mbHeaderList){
					if (mbHeader.getEgBillregister() != null
							&& mbHeader
									.getEgBillregister()
									.getBillstatus()
									.equals(
											MBHeader.MeasurementBookStatus.CANCELLED
													.toString()) && !woEstimateList.contains(woe)) {
							woEstimateList.add(woe);
					}
					for(MBDetails mbDetails:mbHeader.getMbDetails()){
						if(woe.getWorkOrderActivities().contains(mbDetails.getWorkOrderActivity())
								&& mbHeader.getEgwStatus() != null 
								&& mbHeader.getEgwStatus().getCode().
									equalsIgnoreCase(MBHeader.MeasurementBookStatus.APPROVED.toString()) 
								&& mbHeader.getEgBillregister()==null && !woEstimateList.contains(woe)) {							
								woEstimateList.add(woe);
							}
					}
				}
			}
			
		}
		return woEstimateList;
	}
	
	/**
	 * This method will return workorderestimates objects pending for Bill , but it will not consider  Legacy MBs
	 * @param workOrderEstimateList
	 * @param mbHeader
	 * @return List<WorkOrderEstimate>
	 */
	public List<WorkOrderEstimate> getWOEstForBillExludingLegacyMB(List<WorkOrderEstimate> workOrderEstimateList){
		List<WorkOrderEstimate> woEstimateList = new ArrayList<WorkOrderEstimate>();
		//Approved quantity for workorder
		for(WorkOrderEstimate woe:workOrderEstimateList) {			
			List<MBHeader> mbHeaderList =  findAllByNamedQuery("getMBWithoutLegacyByWOEstID", woe.getId(),WorksConstants.CANCELLED_STATUS,getFinalBillTypeConfigValue(), WorksConstants.CANCELLED_STATUS);
			if(mbHeaderList!=null && !mbHeaderList.isEmpty()){				
				for(MBHeader mbHeader:mbHeaderList){
					if (mbHeader.getEgBillregister() != null
							&& mbHeader
									.getEgBillregister()
									.getBillstatus()
									.equals(
											MBHeader.MeasurementBookStatus.CANCELLED
													.toString()) && !woEstimateList.contains(woe)) {
							woEstimateList.add(woe);
					}
					for(MBDetails mbDetails:mbHeader.getMbDetails()){
						if((woe.getWorkOrderActivities().contains(mbDetails.getWorkOrderActivity()) 
								|| (mbDetails.getWorkOrderActivity().getActivity().getRevisionType()!=null))
								&& (mbHeader.getEgwStatus()!=null 
								&& mbHeader.getEgwStatus().getCode().equalsIgnoreCase(MBHeader.MeasurementBookStatus.APPROVED.toString())) 
								&& mbHeader.getEgBillregister()==null && !woEstimateList.contains(woe)){							
								woEstimateList.add(woe);
							}
					}
				}
			}
			
		}
		return woEstimateList;
	}
	
	/**
	 * Populate all the cumulative fields related to mbdetail line item
	 * @param woActivityId
	 * @return
	 */
	public MBHeader calculateMBDetails(MBHeader mbHeader,boolean isPersistedObject){
		List<MBDetails> mbDetailList = mbHeader.getMbDetails();
		double lPrevCumlvQuant = 0;
		for(MBDetails detail:mbDetailList){			
			if(detail.getWorkOrderActivity().getActivity().getParent()==null) {
				lPrevCumlvQuant = prevCumulativeQuantityIncludingCQ(detail.getWorkOrderActivity().getId(),mbHeader.getId(),detail.getWorkOrderActivity().getActivity().getId(), detail.getWorkOrderActivity().getWorkOrderEstimate().getWorkOrder());
				detail.setTotalEstQuantity(totalEstimatedQuantity(detail.getWorkOrderActivity().getId(),mbHeader.getId(),detail.getWorkOrderActivity().getActivity().getId(), detail.getWorkOrderActivity().getWorkOrderEstimate().getWorkOrder()));
			}			
			else {
				detail.getWorkOrderActivity().setParent((WorkOrderActivity)genericService.find("from WorkOrderActivity where activity.id=? and (workOrderEstimate.id=? or workOrderEstimate.estimate.parent.id=?)", detail.getWorkOrderActivity().getActivity().getParent().getId(),mbHeader.getWorkOrderEstimate().getId(),mbHeader.getWorkOrderEstimate().getEstimate().getId()));				
				lPrevCumlvQuant = prevCumulativeQuantityIncludingCQ(detail.getWorkOrderActivity().getId(),mbHeader.getId(),detail.getWorkOrderActivity().getActivity().getParent().getId(), detail.getWorkOrderActivity().getWorkOrderEstimate().getWorkOrder());
				detail.setTotalEstQuantity(totalEstimatedQuantity(detail.getWorkOrderActivity().getId(),mbHeader.getId(),detail.getWorkOrderActivity().getActivity().getParent().getId(), detail.getWorkOrderActivity().getWorkOrderEstimate().getWorkOrder()));				
			}
			detail.setPrevCumlvQuantity(lPrevCumlvQuant);	
			detail.setCurrCumlvQuantity(lPrevCumlvQuant+detail.getQuantity());
			detail.setAmtForCurrQuantity(detail.getQuantity()*detail.getWorkOrderActivity().getApprovedRate());
			detail.setCumlvAmtForCurrCumlvQuantity((lPrevCumlvQuant+detail.getQuantity())*detail.getWorkOrderActivity().getApprovedRate());
		}
		return mbHeader;
	}
	
	/**
	 * This is used by MB screens. This returns the estimated quantity for an work order activity
	 * This returns original activity quantity + all change quantities of the activity for all associated REs
	 * Work order activities of only approved REs are considered
	 * @param woActivityId,mbHeaderId,activityId, workOrder
	 * @return
	 */
	public double totalEstimatedQuantity(Long woActivityId,Long mbHeaderId, Long activityId, WorkOrder workOrder){
		DateTime currentTimestamp = null;
		MBHeader mbHeader = null;
		if(mbHeaderId==null)
		{
			mbHeaderId = -1l;
			currentTimestamp = new DateTime(new Date());
		}
		else
		{
			mbHeader = persistenceService.find(" from MBHeader where id = ?",mbHeaderId);
			currentTimestamp = mbHeader.getCreatedDate();
		}
		
		Object[] params = null;		
		Double estQuantity = null;
		params = new Object[]{workOrder, woActivityId, activityId};
		estQuantity = (Double) genericService.findByNamedQuery("totalEstimatedQuantity",params);
		
		Double estQuantityRE = null;
		if(workOrder.getParent() !=null) 
			params = new Object[]{currentTimestamp,workOrder.getParent(),workOrder.getParent(),activityId};
		else
			params = new Object[]{currentTimestamp,workOrder,workOrder,activityId};
		estQuantityRE = (Double) genericService.findByNamedQuery("totalEstimatedQuantityForRE",params); 
		if(estQuantity!=null && estQuantityRE!=null)
			estQuantity=estQuantity+estQuantityRE;
		if(estQuantity==null && estQuantityRE!=null)
			estQuantity=estQuantityRE;
		if(estQuantity==null)
			return 0.0d;
		else
			return estQuantity.doubleValue();
	}

	/**
	 *
	 * @param workOrderNumber,lineItemId
	 * @return boolean
	 */

	public boolean isMBExistForLineItem(String workOrderNumber,long lineItemId){
		boolean flag=false;
		List<MBHeader> mbHeaderList = null;
		Object[] params;
		List<Object> paramList = new ArrayList<Object>();
		String dynQuery = "select distinct mbHeader from MBHeader mbHeader, WorkOrder wo "
						 +" join wo.workOrderActivities woa left join woa.activity.schedule schedule left join woa.activity.nonSor nonSor"
						 +" where mbHeader.id !=null"
						 +" and mbHeader.workOrder.workOrderNumber like ?"
						 + " and mbHeader.egwStatus.code like ?";
		paramList.add("%"+workOrderNumber+"%");
		paramList.add("NEW");
		if(lineItemId > 0){
			dynQuery = dynQuery + " and (schedule.id = ? or nonSor.id = ?)";
			paramList.add(lineItemId);
			paramList.add(lineItemId);
		}


		logger.debug("1--inside action dynquery is"+dynQuery);

		if(paramList.isEmpty())
			mbHeaderList 	= genericService.findAllBy(dynQuery);
		else{
			params 			= new Object[paramList.size()];
			params 			= paramList.toArray(params);
			mbHeaderList 	= genericService.findAllBy(dynQuery,params);
		}
		
		if(mbHeaderList!=null && !mbHeaderList.isEmpty())
			flag=true;

		return flag;
	}

	/**
	 * Check if mb entries are within approved limit or not.
	 * If Current quantity + prev cumulative quantity is greater than 100% and less than (100+Extra percentage)% 
	 * 	of work order  activity approved quantity, true is returned
	 * If Current quantity + prev cumulative quantity > (100+Extra percentage)% of work order activity approved quantity, null is returned
	 * If Current quantity + prev cumulative quantity <= 100% work order activity approved quantity then false is returned.
	 * 
	 * Eg: If Extra percentage=25
	 * If Current quantity + prev cumulative quantity is greater than 100% and less than 125% 
	 * 	of work order  activity approved quantity, true is returned
	 * If Current quantity + prev cumulative quantity > 125% of work order activity approved quantity, null is returned
	 * If Current quantity + prev cumulative quantity <= 100% work order activity approved quantity then false is returned.
	 * @param mbHeader
	 * @return
	 */
	//@todo remove cancelled mb quantity
	public Boolean approvalLimitCrossed(MBDetails details){
		Boolean result = false;
		Double approvedQuantity = 0D;
		Double approvedQuantityWithoutPercentage = totalEstimatedQuantity(details.getWorkOrderActivity().getId(), details.getMbHeader()==null?null:details.getMbHeader().getId(), details.getWorkOrderActivity().getActivity().getId(), details.getWorkOrderActivity().getWorkOrderEstimate().getWorkOrder());
		Double extraPercentage = worksService.getConfigval();
		if(extraPercentage.doubleValue()>0)
			approvedQuantity = approvedQuantityWithoutPercentage*(1+(extraPercentage/100));
		else
			approvedQuantity = approvedQuantityWithoutPercentage;
		if((details.getPrevCumlvQuantity()+details.getQuantity()) > approvedQuantityWithoutPercentage)
			result = true;
		if((details.getPrevCumlvQuantity()+details.getQuantity())
				> approvedQuantity)
			result = null;
		return result;
	}
	

	/**
	 * List of all approved MB's for which Bill is not generated or bill is cancelled. 
	 * NOTE --- THIS WILL NOT CONSIDER LEGACY MBs 
	 * @param workOrderId
	 * @param asOnDate
	 * @return 
	 */
	public List<MBHeader> getApprovedMBList(Long workOrderId, Long workOrderEstimateId, Date asOnDate){
		Object[] params = new Object[]{WorksConstants.APPROVED,asOnDate,workOrderId,workOrderEstimateId};
		List<MBHeader> mbList= persistenceService.findAllByNamedQuery("getApprovedMBList", params);
		List<MBHeader> mbListForBill=new ArrayList<MBHeader>();
		if(mbList!=null && !mbList.isEmpty()){				
			for(MBHeader mbHeader:mbList){
				if(mbHeader.getEgBillregister()!=null 
						&& mbHeader.getEgBillregister().getStatus()!=null 
						&& mbHeader.getEgBillregister().getStatus().getCode().equals("CANCELLED")){					
					mbListForBill.add(mbHeader);
				}
				if(mbHeader.getEgBillregister()==null)
					mbListForBill.add(mbHeader);
			}
		}
		return mbListForBill;
	}

	/**
	 * List of all MB's were the bill is created and bill type is part bill
	 * @param workOrderId
	 * @param billtype
	 * @return
	 */
	public List<MBHeader> getPartBillList(Long workOrderId,String billtype){
		Object[] params = new Object[]{WorksConstants.APPROVED,workOrderId,WorksConstants.CANCELLED_STATUS,billtype};
		return persistenceService.findAllByNamedQuery("getPartBillList",params);
	
	}
	
	/**
	 * returns latest bill date for MB
	 * @param workOrderId
	 * @return
	 */
	public Date getLatestBillDateForMB(Long workOrderId){
		return (Date) genericService.findByNamedQuery("getAllBilledMBs",WorksConstants.APPROVED,workOrderId);
	}
	
	/**
	 * returns latest bill date for MB
	 * @param workOrderEstimateId
	 * @return
	 */
	public Date getLatestBillDateForMBPassingWOEstimate(Long workOrderEstimateId){
		return (Date) genericService.findByNamedQuery("getAllBilledMBsForWOEstimate",WorksConstants.APPROVED,workOrderEstimateId);
	}
	
	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}
	
	public String getFinalBillTypeConfigValue() {		
		return worksService.getWorksConfigValue("FinalBillType");
	}
	
	public BigDecimal getTotalMBAmountForPrevMBs(WorkOrderEstimate workOrderEstimate,double negoPerc,String tenderType){
		List<MBHeader> mbHeaderList = persistenceService.findAllByNamedQuery("getAllApprovedMBHeaders", WorksConstants.APPROVED,workOrderEstimate.getWorkOrder().getId(),workOrderEstimate.getEstimate().getId());
		BigDecimal tenderedAmount = BigDecimal.ZERO;
		BigDecimal mbAmount = BigDecimal.ZERO;
		BigDecimal tenderedMBAmount = BigDecimal.ZERO;
		BigDecimal totalMBAmount = BigDecimal.ZERO;
		
		if(tenderType.equalsIgnoreCase(WorksConstants.PERC_TENDER)){
			for(MBHeader mbhObj : mbHeaderList){
				for(MBDetails mbd : mbhObj.getMbDetails()){
					if(mbd.getWorkOrderActivity().getActivity().getRevisionType()==null){
						tenderedAmount=tenderedAmount.add(BigDecimal.valueOf(mbd.getAmount()));
					}
				}
				mbAmount = mbAmount.add(mbhObj.getTotalMBAmount());
			}
			//applying percentage on tendered items
			if(tenderedAmount!=null){
				tenderedMBAmount = tenderedAmount.add(tenderedAmount.multiply(BigDecimal.valueOf(negoPerc/100)));
			}
			// adding tendered amount with the non tendered items amount, to get the total mb amount
			totalMBAmount =tenderedMBAmount.add(mbAmount.subtract(tenderedAmount));
		}
		else{
			for(MBHeader mbhObj : mbHeaderList){
				totalMBAmount = totalMBAmount.add(mbhObj.getTotalMBAmount());
			}
		}
		return totalMBAmount.setScale(2, RoundingMode.HALF_UP);
	}
	
	public BigDecimal getTotalMBAmount(Long workOrderId,Long estimateId){
		BigDecimal totalMBAmount = BigDecimal.ZERO; 
		totalMBAmount = (BigDecimal) genericService.find("select sum(mbAmount) from MBHeader where egwStatus.code != 'CANCELLED' and  workOrderEstimate.workOrder.id=? and  workOrderEstimate.estimate.id= ? ",workOrderId, estimateId);
		return totalMBAmount;
	}
	
	public Date getWorkCommencedDate(Long woId) {
		Date workCommencedDate =  (Date) genericService.find(" select stat.statusDate from SetStatus stat "
				 + "where stat.objectId = ? and stat.objectType = ? and stat.egwStatus.code = ? ",
						woId,"WorkOrder",WorksConstants.WO_STATUS_WOCOMMENCED);
		return workCommencedDate;
	}
	
	public Date getLastMBCreatedDate(Long woId ,Long estId){
		Date latestMBDate = (Date) genericService.find(" select max(mbh.mbDate) from MBHeader mbh "
				+ "where mbh.workOrder.id= ? and mbh.workOrderEstimate.estimate.id=? and mbh.workOrderEstimate.estimate.egwStatus.code= ? and mbh.egwStatus.code = ? ",
				woId,estId,WorksConstants.ADMIN_SANCTIONED_STATUS,WorksConstants.APPROVED);
		return latestMBDate;
	}
}

