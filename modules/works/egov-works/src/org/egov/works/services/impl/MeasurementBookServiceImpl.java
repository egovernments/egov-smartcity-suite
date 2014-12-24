package org.egov.works.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.revisionEstimate.RevisionAbstractEstimate;
import org.egov.works.models.revisionEstimate.RevisionType;
import org.egov.works.models.measurementbook.MBBill;
import org.egov.works.models.measurementbook.MBDetails;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.MeasurementBookService;
import org.egov.works.services.WorksService;

/**
 * This class will expose all measurment book related operations.
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
	public List<MBHeader> searchMB(Map<String,Object> criteriaMap){
		logger.info("-------Inside searchMB method-----------------------");
		List<MBHeader> mbHeaderList;
		String dynQuery = "select distinct mbh from MBHeader mbh left join mbh.mbBills mbBills where mbh.id != null " ;
		Object[] params;
		List<Object> paramList = new ArrayList<Object>();

		if(criteriaMap.get(WORKORDER_NO) != null){
			dynQuery = dynQuery + " and mbh.workOrder.workOrderNumber like '%"
								+ criteriaMap.get(WORKORDER_NO)
								+ "%'";
		}
		if(criteriaMap.get(EST_NO) != null){
			dynQuery = dynQuery + " and mbh.workOrderEstimate.estimate.estimateNumber like '%"
								+ criteriaMap.get(EST_NO)
								+ "%'";
		}
		if(criteriaMap.get(CONTRACTOR_ID) != null && !"-1".equals(criteriaMap.get(CONTRACTOR_ID))) {
			dynQuery = dynQuery + " and mbh.workOrder.contractor.id = ?";
			paramList.add(criteriaMap.get(CONTRACTOR_ID));
		}
		if(criteriaMap.get(CREATE_DATE) != null) {
			dynQuery = dynQuery + " and mbh.mbDate = ?";
			paramList.add(criteriaMap.get(CREATE_DATE));
		}
		if(criteriaMap.get(FROM_DATE) != null && criteriaMap.get(TO_DATE)==null) {
			dynQuery = dynQuery + " and mbh.mbDate >= ? ";
			paramList.add(criteriaMap.get(FROM_DATE));

		}else if(criteriaMap.get(TO_DATE) != null && criteriaMap.get(FROM_DATE)==null) {
			dynQuery = dynQuery + " and mbh.mbDate <= ? ";
			paramList.add(criteriaMap.get(TO_DATE));
		}else if(criteriaMap.get(FROM_DATE) != null && criteriaMap.get(TO_DATE)!=null) {
			dynQuery = dynQuery + " and mbh.mbDate between ? and ? ";
			paramList.add(criteriaMap.get(FROM_DATE));
			paramList.add(criteriaMap.get(TO_DATE));
		}
		
		
		if(criteriaMap.get(MB_REF_NO) != null) {
			dynQuery = dynQuery + " and mbh.mbRefNo = ?";
			paramList.add(criteriaMap.get(MB_REF_NO));
		}
		if(criteriaMap.get(MB_PAGE_NO) != null) {
			dynQuery = dynQuery + " and ? between mbh.fromPageNo and mbh.toPageNo ";
			paramList.add(criteriaMap.get(MB_PAGE_NO));
		}
		if("1".equals(criteriaMap.get(ALL_STATUS))){
			dynQuery = dynQuery + " and mbBills.egBillregister.id is not null"; 
		}
		else{
			if(!"-1".equals(criteriaMap.get(STATUS)) && criteriaMap.get(STATUS) != null && (criteriaMap.get(STATUS).equals(MBHeader.MeasurementBookStatus.APPROVED.toString()) || criteriaMap.get(STATUS).equals(MBHeader.MeasurementBookStatus.CANCELLED.toString()))) {
				dynQuery = dynQuery + " and mbh.mbDate.state.previous.value = ?";
				paramList.add(criteriaMap.get(STATUS));
			}
			else if(!"-1".equals(criteriaMap.get(STATUS)) && criteriaMap.get(STATUS) != null){
				dynQuery = dynQuery + " and mbh.mbDate.state.value = ?"; 
				paramList.add(criteriaMap.get(STATUS));
			}
		}
		//Adding criteria for search bill-Sreekanth D.
		if(criteriaMap.get(BILLDATE) != null){
			dynQuery = dynQuery + " and trunc(mbBills.egBillregister.billdate) = ?";
			paramList.add(criteriaMap.get(BILLDATE));
		}
		if(criteriaMap.get(BILLSTATUS) != null && !criteriaMap.get(BILLSTATUS).equals("-1")){
			dynQuery = dynQuery + " and mbBills.egBillregister.state.value like '%"
								+ criteriaMap.get(BILLSTATUS)
								+ "%'";
		}
		if(criteriaMap.get(BILLNO) != null){
			dynQuery = dynQuery + " and mbBills.egBillregister.billnumber= ? ";
			paramList.add(criteriaMap.get(BILLNO));
		}
		
		if(paramList.isEmpty())
			mbHeaderList 	= genericService.findAllBy(dynQuery);
		else{
			params 			= new Object[paramList.size()];
			params 			= paramList.toArray(params);
			mbHeaderList 	= genericService.findAllBy(dynQuery,params);
		}

		return mbHeaderList;
	}

	/**
	 * Get previous cumulative amount(approved, approval pending and draft entries) based on workorder activity Id.
	 * This will search list of MBDetail and then get cmulative amount.
	 * @param woActivityId
	 * @return
	 */
	public double prevCumulativeQuantity(Long woActivityId,Long mbHeaderId, Long activityId){
		if(mbHeaderId==null)
			mbHeaderId=-1l;
		Object[] params = new Object[]{mbHeaderId,mbHeaderId,woActivityId, activityId};
		Double pQuant = (Double) genericService.findByNamedQuery("prevCumulativeQuantity",params);
		params = new Object[]{mbHeaderId,mbHeaderId, activityId};
		Double pQuantRE = (Double) genericService.findByNamedQuery("prevCumulativeQuantityForRE",params);
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
	 * Get estimated quantity for Change in quantity of RE for given work order activity
	 * @param woActivityId,mbHeaderId,activityId
	 * @return
	 */
	public double totalEstimatedQuantity(Long woActivityId,Long mbHeaderId, Long activityId){
		if(mbHeaderId==null)
			mbHeaderId=-1l; 
		
		Object[] params = null;		
		Double estQuantity = null;
		/*if(mbHeaderId!=-1) {
			Object[] params = new Object[]{mbHeaderId,mbHeaderId,woActivityId, activityId};	
			estQuantity = (Double) genericService.findByNamedQuery("totalEstimatedQuantityInView",params);
		}
		else */{  
			params = new Object[]{woActivityId, activityId};
			estQuantity = (Double) genericService.findByNamedQuery("totalEstimatedQuantity",params);
		}
		
		Double estQuantityRE = null;
		/*if(mbHeaderId!=-1) {
			params = new Object[]{mbHeaderId,mbHeaderId, activityId};
			estQuantityRE = (Double) genericService.findByNamedQuery("totalEstimatedQuantityForREinView",params);
		}
		else */{  
			params = new Object[]{activityId};
			estQuantityRE = (Double) genericService.findByNamedQuery("totalEstimatedQuantityForRE",params);
		}
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
	 * Get estimated quantity for Change in quantity of RE for given work order activity
	 * @param woActivityId,estimateId,activityId
	 * @return
	 */
	public double totalEstimatedQuantityForRE(Long woActivityId,Long estimateId, Long activityId){
		if(estimateId==null)
			estimateId=-1l; 
		
		Object[] params = null;		
		Double estQuantity = null;
		
		params = new Object[]{estimateId,woActivityId, activityId};
		estQuantity = (Double) genericService.findByNamedQuery("totalEstimatedQuantityInRE",params);
		
		Double estQuantityRE = null;
		
		params = new Object[]{estimateId, activityId};
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
			
			Object[] params = new Object[]{woe.getEstimate().getId()};
			Double reApprovedQty = (Double)genericService.findByNamedQuery("getRevisedTotalQuantityForWOE",params);			
			if(reApprovedQty!=null)
				approvedQuantity+=reApprovedQty;
			
			if(extraPercentage.doubleValue()>0)
				approvedQuantity=approvedQuantity*(1+(extraPercentage/100));
			List<MBHeader> mbHeaderList =  findAllByNamedQuery("getMBbyWorkOrderEstID", woe.getId());
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
				if(mbh.getState()!=null && mbh.getState().getPrevious()!=null &&
						mbh.getCurrentState().getPrevious().getValue().
						equals(MBHeader.MeasurementBookStatus.CANCELLED.toString()) && !woEstimateList.contains(woe)){
					woEstimateList.add(woe);
				}
			}
		}
	}

	private void addUnlimitedWorkOrderEstimatesForMB(
			List<WorkOrderEstimate> woEstimateList,List<WorkOrderEstimate> usedWOEstimateList,WorkOrderEstimate woe,
			MBHeader mbHeader, MBDetails mbDetails) {
		if(woe.getWorkOrderActivities().contains(mbDetails.getWorkOrderActivity())
				&& mbHeader.getState()!=null && mbHeader.getState().getPrevious()!=null &&
				mbHeader.getState().getPrevious().getValue().
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
		if(usedQuantity < approvedQuantity //&& woe.getWorkOrderActivities().contains(mbDetails.getWorkOrderActivity())
				&& mbHeader.getState()!=null && mbHeader.getState().getPrevious()!=null &&
				mbHeader.getState().getPrevious().getValue().
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
			if(mbHeader!=null && mbHeader.getState()!=null && mbHeader.getState().getPrevious()!=null &&
					!mbHeader.getState().getPrevious().getValue().
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
			List<WorkOrderActivity> woaWithExtraItems = genericService.findAllBy("from WorkOrderActivity woa where woa.workOrderEstimate.workOrder.parent is not null " +
					"and woa.workOrderEstimate.workOrder.parent.id=? and woa.workOrderEstimate.workOrder.egwStatus.code='APPROVED' and woa.activity.revisionType='EXTRA_ITEM'",woe.getWorkOrder().getId());
			woaWithExtraItems.addAll(woe.getWorkOrderActivities());
			List<MBHeader> mbHeaderList =  findAllByNamedQuery("getMBbyWorkOrderEstID", woe.getId());
			if(mbHeaderList!=null && !mbHeaderList.isEmpty()){				
				for(MBHeader mbHeader:mbHeaderList){
					List validBills = new ArrayList();
					boolean validBillexists=false;
					if (mbHeader.getMbBills() != null && !mbHeader.getMbBills().isEmpty()){
						for(MBBill mbr:mbHeader.getMbBills()){
							if(mbr.getEgBillregister().getBillstatus()!=null && !mbr.getEgBillregister().getBillstatus().equalsIgnoreCase("CANCELLED")){
								validBillexists=true;
								validBills.add(mbr);
							}
						}
						if(!woEstimateList.contains(woe) && validBillexists==false){
							woEstimateList.add(woe);
						}
					}
										
					for(MBDetails mbDetails:mbHeader.getMbDetails()){
						if((woaWithExtraItems !=null && !woaWithExtraItems.isEmpty() && woaWithExtraItems.contains(mbDetails.getWorkOrderActivity()))
								&& mbHeader.getState().getPrevious().getValue().
								equalsIgnoreCase(MBHeader.MeasurementBookStatus.APPROVED.toString()) && mbHeader.getMbBills().isEmpty() && !woEstimateList.contains(woe)){							
								woEstimateList.add(woe); 
							}
						if(mbHeader.getRevisionEstimate()!=null && mbHeader.getRevisionEstimate().getActivities().contains(mbDetails.getWorkOrderActivity().getActivity())
								&& mbHeader.getState().getPrevious().getValue().
								equalsIgnoreCase(MBHeader.MeasurementBookStatus.APPROVED.toString()) && 
								mbHeader.getRevisionEstimate().getEgwStatus().getCode().equals(AbstractEstimate.EstimateStatus.APPROVED.toString())&& !woEstimateList.contains(woe)){
							if (mbHeader.getMbBills() != null && !mbHeader.getMbBills().isEmpty()){
								for(MBBill mbr:mbHeader.getMbBills()){
									if(mbr.getEgBillregister().getBillstatus()!=null && !mbr.getEgBillregister().getBillstatus().equalsIgnoreCase("CANCELLED")){
										validBillexists=true;
									}
								}
							}
								
							if(!woEstimateList.contains(woe) && validBillexists==false){
								woEstimateList.add(woe);
							}							
						}
						if(mbDetails.getPartRate()>0 && mbHeader.getState().getPrevious().getValue().
								equalsIgnoreCase(MBHeader.MeasurementBookStatus.APPROVED.toString()) && validBills.size()<=1 && !woEstimateList.contains(woe)){
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
	public MBHeader calculateMBDetails(MBHeader mbHeader,boolean isPersistedObject,boolean isTenderPercApplicable){
		List<MBDetails> mbDetailList = mbHeader.getMbDetails();
		double lPrevCumlvQuant = 0;
		for(MBDetails detail:mbDetailList){			
			if(detail.getWorkOrderActivity().getActivity().getParent()==null) {
				lPrevCumlvQuant = prevCumulativeQuantity(detail.getWorkOrderActivity().getId(),mbHeader.getId(),detail.getWorkOrderActivity().getActivity().getId());
				detail.setTotalEstQuantity(totalEstimatedQuantity(detail.getWorkOrderActivity().getId(),mbHeader.getId(),detail.getWorkOrderActivity().getActivity().getId()));
			}			
			else {
				detail.getWorkOrderActivity().setParent((WorkOrderActivity)genericService.find("from WorkOrderActivity where activity.id=? and (workOrderEstimate.id=? or workOrderEstimate.estimate.parent.id=?)", detail.getWorkOrderActivity().getActivity().getParent().getId(),mbHeader.getWorkOrderEstimate().getId(),mbHeader.getWorkOrderEstimate().getEstimate().getId()));				
				lPrevCumlvQuant = prevCumulativeQuantity(detail.getWorkOrderActivity().getId(),mbHeader.getId(),detail.getWorkOrderActivity().getActivity().getParent().getId());
				detail.setTotalEstQuantity(totalEstimatedQuantity(detail.getWorkOrderActivity().getId(),mbHeader.getId(),detail.getWorkOrderActivity().getActivity().getParent().getId()));				
			}
			
			if(detail.getTotalEstQuantity()==0 && detail.getWorkOrderActivity().getParent()!=null && detail.getWorkOrderActivity().getParent().getActivity().getQuantity()!=0)
				detail.setTotalEstQuantity(detail.getWorkOrderActivity().getApprovedQuantity());
//			if(isPersistedObject){
//				lPrevCumlvQuant = lPrevCumlvQuant - detail.getQuantity();
//			}

			if(detail.getWorkOrderActivity().getActivity().getRevisionType()!=null && !detail.getWorkOrderActivity().getActivity().getRevisionType().equals(RevisionType.EXTRA_ITEM))
				detail.getWorkOrderActivity().setApprovedAmount(detail.getTotalEstQuantity()*detail.getWorkOrderActivity().getApprovedRate());
			detail.setPrevCumlvQuantity(lPrevCumlvQuant);	
			detail.setCurrCumlvQuantity(lPrevCumlvQuant+detail.getQuantity());
			if(isTenderPercApplicable &&  detail.getReducedRate()==0){
				detail.setAmtForCurrQuantity(detail.getQuantity()*detail.getWorkOrderActivity().getActivity().getRate().getValue());
				detail.setCumlvAmtForCurrCumlvQuantity((lPrevCumlvQuant+detail.getQuantity())*detail.getWorkOrderActivity().getActivity().getRate().getValue());
			}
			else if(detail.getReducedRate()>0){
				detail.setAmtForCurrQuantity(detail.getQuantity()*detail.getReducedRate());
				detail.setCumlvAmtForCurrCumlvQuantity((lPrevCumlvQuant+detail.getQuantity())*detail.getReducedRate());

			}
			else{
				detail.setAmtForCurrQuantity(detail.getQuantity()*detail.getWorkOrderActivity().getApprovedRate());
				detail.setCumlvAmtForCurrCumlvQuantity((lPrevCumlvQuant+detail.getQuantity())*detail.getWorkOrderActivity().getApprovedRate());
			}
			
		}
		return mbHeader;
	}

	/**
	 *
	 * @param workOrderNumber,lineItemId
	 * @return boolean
	 */

	public boolean isMBExistForLineItem(String workOrderNumber,long lineItemId){
		boolean flag=false;
		List<MBHeader> mbHeaderList = null;

		String dynQuery = "select distinct mbHeader from MBHeader mbHeader, WorkOrder wo "
						 +" join wo.workOrderActivities woa left join woa.activity.schedule schedule left join woa.activity.nonSor nonSor"
						 +" where mbHeader.id !=null"
						 +" and mbHeader.workOrder.workOrderNumber like '"+ workOrderNumber +"'"
						 + " and mbHeader.state.value like '%NEW%'";

		if(lineItemId > 0)
			dynQuery = dynQuery + " and (schedule.id = "+lineItemId + "or nonSor.id = "+lineItemId +")";


		logger.debug("1--inside action dynquery is"+dynQuery);

		mbHeaderList=genericService.findAllBy(dynQuery);

		if(mbHeaderList!=null && !mbHeaderList.isEmpty())
			flag=true;

		return flag;
	}


	/**
	 * Check if mb entries are within approved limit or not.
	 * @param mbHeader
	 * @return
	 */
	//@todo remove cancelled mb quantity
	public boolean approvalLimitCrossed(MBDetails details){
		boolean result = false;
		Double approvedQuantity = 0D;
		Double extraPercentage = worksService.getConfigval();
		//for(MBDetails details:mbHeader.getMbDetails()){
		if(extraPercentage.doubleValue()>0)
			approvedQuantity = details.getWorkOrderActivity().getApprovedQuantity()*(1+(extraPercentage/100));
		else
			approvedQuantity = details.getWorkOrderActivity().getApprovedQuantity();
		if((details.getPrevCumlvQuantity()+details.getQuantity())
				> approvedQuantity)
			result = true;
		//}
		return result;
	}
	

	/**
	 * List of all approved MB's for which Bill is not generated or bill is cancelled.
	 * @param workOrderId
	 * @param asOnDate
	 * @return
	 */
	public List<MBHeader> getApprovedMBList(Long workOrderId, Long workOrderEstimateId, Date asOnDate){
		Object[] params = new Object[]{asOnDate,workOrderId,workOrderEstimateId};
		List<MBHeader> mbList= persistenceService.findAllByNamedQuery("getApprovedMBList", params);
		List<MBHeader> mbListForBill=new ArrayList<MBHeader>();
		if(mbList!=null && !mbList.isEmpty()){				
			for(MBHeader mbHeader:mbList){ 
				if(mbHeader.getMbBills()!=null  && !mbHeader.getMbBills().isEmpty()){
					List validBills = new ArrayList();
					boolean validBillexists=false;
					for(MBBill mbr:mbHeader.getMbBills()){
						if(mbr.getEgBillregister().getBillstatus()!=null && !mbr.getEgBillregister().getBillstatus().equals("CANCELLED")){
							validBillexists=true;
							validBills.add(mbr);
						}
					}
					if(!mbListForBill.contains(mbHeader) && validBillexists==false){
						mbListForBill.add(mbHeader);
					}
					else if(!mbListForBill.contains(mbHeader) && validBillexists==true){
						for(MBDetails mbd:mbHeader.getMbDetails()){
							if(!mbListForBill.contains(mbHeader) && mbd.getPartRate()>0 && validBills.size()<=1){
								mbListForBill.add(mbHeader);
							}
						}
					}
				}
								
				if(mbHeader.getMbBills().isEmpty() && ((mbHeader.getRevisionEstimate()==null) || (mbHeader.getRevisionEstimate()!=null && mbHeader.getRevisionEstimate().getEgwStatus().getCode().equals(AbstractEstimate.EstimateStatus.APPROVED.toString())))){				
					mbListForBill.add(mbHeader);
				}
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
		return persistenceService.findAllByNamedQuery("getPartBillList",workOrderId,billtype);
	}
	
	/**
	 * returns latest bill date for MB
	 * @param workOrderId
	 * @return
	 */
	public Date getLatestBillDateForMB(Long workOrderId){
		return (Date) genericService.findByNamedQuery("getAllBilledMBs",workOrderId);
	}
	
	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}
	
	public String getFinalBillTypeConfigValue() {		
		return worksService.getWorksConfigValue("FinalBillType");
	}

	public BigDecimal getRevisionEstimateMBAmount(AbstractEstimate abstractEstimate){ 
		BigDecimal amount =new BigDecimal(0.00);
		List<MBHeader> mbheader=new LinkedList<MBHeader>(); 		
		mbheader=persistenceService.findAllBy("from MBHeader mbh where mbh.egwStatus.code='APPROVED' and  mbh.egwStatus.code!='CANCELLED' and mbh.workOrderEstimate.estimate.id="+abstractEstimate.getId());
		for(MBHeader mbh:mbheader){
			for(MBDetails mbd:mbh.getMbDetails()){
				amount=amount.add(BigDecimal.valueOf(mbd.getAmount().getValue()));  
			}
		}
		return amount;  
	}
	public MBHeader getRevisionEstimateMB(RevisionAbstractEstimate revisionAbstractEstimate){
		List<MBHeader> mbheaderList=new LinkedList<MBHeader>(); 		
		mbheaderList=persistenceService.findAllBy("from MBHeader mbh where mbh.egwStatus.code='APPROVED' and  mbh.egwStatus.code!='CANCELLED' and mbh.revisionEstimate.id="+revisionAbstractEstimate.getId());
		return mbheaderList.get(0);
		
	}
}

