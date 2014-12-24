package org.egov.works.services.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CommonsService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.models.State;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.admbndry.Boundary;
import org.egov.model.budget.BudgetGroup;
import org.egov.tender.BidType;
import org.egov.tender.TenderableType;
import org.egov.tender.model.GenericTenderResponse;
import org.egov.tender.model.TenderResponseLine;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.Activity;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.revisionEstimate.RevisionType;
import org.egov.works.models.tender.EstimateLineItemsForWP;
import org.egov.works.models.tender.WorksPackage;
import org.egov.works.models.workorder.AbstractEstimateForWp;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.models.workorder.WorkOrderNumberGenerator;
import org.egov.works.services.MeasurementBookService;
import org.egov.works.services.RetentionMoneyRefundService;
import org.egov.works.services.ReturnSecurityDepositService;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksPackageService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
import org.hibernate.Query;

public class WorkOrderServiceImpl extends BaseServiceImpl<WorkOrder, Long> 
				implements WorkOrderService {
	private static final Logger logger = Logger.getLogger(WorkOrderServiceImpl.class);
	
	private PersistenceService<Contractor, Long> contractorService;
	private WorksService worksService;
	private MeasurementBookService measurementBookService;
	private WorkOrderNumberGenerator workOrderNumberGenerators;	

	//Serach parameters
	public static final String CONTRACTOR_ID 	= "CONTRACTOR_ID";
	public static final String CREATE_DATE 		= "CREATE_DATE";
	public static final String FROM_DATE 		= "FROM_DATE";
	public static final String TO_DATE 		    = "TO_DATE";
	public static final String STATUS 		    = "STATUS";
	public static final String TENDER_NO 		= "TENDER_NO";
	public static final String WORKORDER_NO 	= "WORKORDER_NO";
	public static final String WORKORDER_ID 	= "WORKORDER_ID";
	public static final String MB_CREATION 	= "MB_CREATION";
	public static final String WORKORDER_ESTIMATE_ID 	= "WORKORDER_ESTIMATE_ID";
	public static final String PROJECT_CODE 	= "PROJECT_CODE";
	public static final String ACTIVITY_DESC 	= "ACTIVITY_DESC";
	public static final String ACTIVITY_CODE 	= "ACTIVITY_CODE";
	public static final String ACTION_FLAG			= "ACTION_FLAG";
	public static final String ESTIMATE_NO = "ESTIMATE_NO";
	public static final String WP_NO = "WP_NO";
	public static final String TENDER_FILE_NO = "TENDER_FILE_NO";
	
	private WorksPackageService workspackageService;
	private ReturnSecurityDepositService returnSecurityDepositService;
	private RetentionMoneyRefundService retentionMoneyRefundService;

	private CommonsService commonsService;
	
	public WorkOrderServiceImpl(PersistenceService<WorkOrder, Long> persistenceService) {
		super(persistenceService);
	}
	
	public Double getSecurityDepositConfValue() {	
		String securityDepConfValue = worksService.getWorksConfigValue("SECURITY_DEPOSIT_MULTIPLIER");
		if(StringUtils.isNotBlank(securityDepConfValue)){
			return Double.valueOf(securityDepConfValue);
		}
		return 0.0;
	}
	
	public Double getLabourWelfareFundConfValue() {		
		String labourWelfareConfValue = worksService.getWorksConfigValue("LWF_MULTIPLIER");
		if(StringUtils.isNotBlank(labourWelfareConfValue)){
			return Double.valueOf(labourWelfareConfValue);
		}
		return 0.0;
	}
	
	/**
	 * This method will set workorder number to the work order object
	 * @param entity
	 * @param workOrder
	 */
	public WorkOrder setWorkOrderNumber(AbstractEstimate abstractEstimate,WorkOrder workOrder,WorksPackage worksPackage) {	
		CFinancialYear financialYear = getCurrentFinancialYear(workOrder.getWorkOrderDate());
		if(workOrder.getWorkOrderNumber()==null){
				workOrder.setWorkOrderNumber(workOrderNumberGenerators.getWorkOrderNumberGenerator(abstractEstimate, financialYear,worksPackage,workOrder, persistenceService));		
		}
		return workOrder;
	}

	/**
	 * This method will return all the contractors which are having active work orders.
	 * @return List of ContractorDetail
	 */
	public List<Contractor> getContractorsWithWO(){
		logger.info("-------------------------Inside getContractorsWithWO---------------------");
		List<Contractor> contractorList = null;
		
		contractorList = contractorService.findAllByNamedQuery("getContractorsWithWO");
		
		return contractorList;
	}
	
	/**
	 * This method will search list of WO's for the given criteria and eligible for MB.
	 * CriteriaMap will have:CONTRACTOR_ID,CREATE_DATE,TENDER_NO,WORKORDER_NO
	 * Filter:
	 * 1)isApprovalLimitReachedForWO
	 * 2)isMBCreatedAndPendingForWO
	 * 3)isFinalBillApprovedForWO
	 * @param criteriaMap
	 * @return
	 */
	public List<WorkOrder> searchWOForMB(Map<String,Object> criteriaMap){
		logger.info("---------------------------Inside searchWOForMB----------------------------");
		List<WorkOrder> filteredList 	= new ArrayList<WorkOrder>();
		criteriaMap.put(ACTION_FLAG, "searchWOForMB");
		//Filter list for approval limit
		for(WorkOrder workorder:searchWO(criteriaMap)){
			if(!isApprovalLimitReachedForWO(workorder.getId()))
				filteredList.add(workorder);
		}
		
		return filteredList;
		
	}
	
	/**
	 * This method will search list of WO's for the given criteria and eligible for MB.
	 * CriteriaMap will have:CONTRACTOR_ID,CREATE_DATE,TENDER_NO,WORKORDER_NO,PROJECT_CODE
	 * Filter:
	 * 1)An existing bill with status in "New� or �approval pending� or �Rejected will NOT be retrieved
	 * 2)Work orders for which the final bill is generated will NOT be retrieved for selection in the search result set. 
	 * 2)Work orders with existing bill with status � Approved� with no existing bill can be retrieved for selection 
	 * @param criteriaMap
	 * @return
	 */
	public List<WorkOrder> searchWOForBilling(Map<String,Object> criteriaMap){
		logger.info("-------------------------Inside searchWOForBilling-----------------------");
		List<WorkOrder> filteredList 	= new ArrayList<WorkOrder>();
		criteriaMap.put(ACTION_FLAG, "searchWOForBilling");
		//Filter list for approval limit
		for(WorkOrder workorder:searchWO(criteriaMap)){
			if(!isWOValidforBill(workorder.getId()))
				filteredList.add(workorder);
		}
		
		return filteredList;
		
	}

	
	/**
	 * This method will search list of WO's for the given criteria and eligible to be view.
	 * CriteriaMap may have:CONTRACTOR_ID,FROM_DATE,TO_DATE,WORKORDER_NO,STATUS
	 * @param criteriaMap
	 * @return List<WorkOrder> 
	 */
	public List<WorkOrder> searchWOToView(Map<String,Object> criteriaMap){
		logger.info("-------------------------Inside searchWOToView-----------------------");

		return searchWO(criteriaMap);
	}
	
	/**
	 * This method will search list of WO's for the given criteria.
	 * CriteriaMap will have:CONTRACTOR_ID,CREATE_DATE,TENDER_NO,WORKORDER_NO,PROJECT_CODE
	 * 
	 * Date of creation ~ WorkOrder.workOrderDate
	 * Contractor ~ WorkOrder.contractor
	 * Tender number ~ WorkOrder.abstractEstimate ~ TenderEstimate.tendernumber
	 * Work order number ~ WorkOrder.workOrderNumber
	 * Project code ~ WorkOrder.AbstractEstimate.projectCode. 
	 * 
	 * approved quantity ~ WorkOrder.WorkOrderActivity.approvedQuantity
	 * a pre-defined % ~ APP_CONFIG
	 * line items in a work order ~ WorkOrder.WorkOrderActivity
	 * final bill is approved ~ @todo
	 * MB is in a � approval pending� ~ MBHeader.currentStat
	 * bill with status �New� or � approval pending� or � Rejected� ~ @todo
	 * final bill is generated ~ @todo
	 * existing bill with status � Approved� ~@todo
	 * 
	 * @param criteriaMap
	 * @return
	 */
	public List<WorkOrder> searchWO(Map<String,Object> criteriaMap){
		logger.info("-------------------------Inside searchWO---------------------------------");
		List<WorkOrder> wolList = null;
		
		String dynQuery ="select distinct wo from WorkOrder wo left join wo.workOrderEstimates workOrderEstimate" 
		+ " where wo.id is not null and wo.parent is null " ;
		Object[] params;
		List<Object> paramList = new ArrayList<Object>();
		String setStat = worksService.getWorksConfigValue("WorkOrder.setstatus");
		if(criteriaMap.get(STATUS) != null) {
			if(criteriaMap.get(STATUS).equals("APPROVED") || 
					criteriaMap.get(STATUS).equals("CANCELLED")){
				dynQuery = dynQuery + " and wo.state.previous.value = ? and " +
						" wo.id not in (select objectId from SetStatus where objectType='WorkOrder')";
				paramList.add(criteriaMap.get(STATUS));
			}else if(!criteriaMap.get(STATUS).equals("-1") && Arrays.asList(setStat.split(",")).contains(criteriaMap.get(STATUS)))
			{
			dynQuery = dynQuery + " and wo.id in(select stat.objectId from " +
						"SetStatus stat where stat.egwStatus.code=? and stat.id = (select" +
				" max(stat1.id) from SetStatus stat1 where wo.id=stat1.objectId and stat1.objectType='WorkOrder') and stat.objectType='WorkOrder')";
			paramList.add(criteriaMap.get(STATUS));
			}
			else if(!criteriaMap.get(STATUS).equals("-1") && !Arrays.asList(setStat.split(",")).contains(criteriaMap.get(STATUS)))
			{
			dynQuery = dynQuery + " and wo.state.value = ?";
			paramList.add(criteriaMap.get(STATUS));
			}
		}
		if(criteriaMap.get(CREATE_DATE) != null) {
			dynQuery = dynQuery + " and wo.workOrderDate = ? ";
			paramList.add(criteriaMap.get(CREATE_DATE));
		}
						
		if(criteriaMap.get(FROM_DATE) != null && criteriaMap.get(TO_DATE)==null) {
			dynQuery = dynQuery + " and wo.workOrderDate >= ? ";
			paramList.add(criteriaMap.get(FROM_DATE));

		}else if(criteriaMap.get(TO_DATE) != null && criteriaMap.get(FROM_DATE)==null) {
			dynQuery = dynQuery + " and wo.workOrderDate <= ? ";
			paramList.add(criteriaMap.get(TO_DATE));
		}else if(criteriaMap.get(FROM_DATE) != null && criteriaMap.get(TO_DATE)!=null) {
			dynQuery = dynQuery + " and wo.workOrderDate between ? and ? ";
			paramList.add(criteriaMap.get(FROM_DATE));
			paramList.add(criteriaMap.get(TO_DATE));
		}
		if(criteriaMap.get(WORKORDER_NO) != null){
			dynQuery = dynQuery + " and UPPER(wo.workOrderNumber) like '%"+criteriaMap.get(WORKORDER_NO).toString().trim().toUpperCase()+"%'";
		}
		if(criteriaMap.get(ESTIMATE_NO) != null){
			dynQuery = dynQuery + " and wo.id in (select distinct woe.workOrder.id from WorkOrderEstimate woe where " +
			"UPPER(woe.estimate.estimateNumber) like '%"+criteriaMap.get("ESTIMATE_NO").toString().trim().toUpperCase()+"%')";
		}
/*		if(criteriaMap.get(WP_NO) != null){
			dynQuery = dynQuery + " and UPPER(wo.packageNumber) like '%"+criteriaMap.get(WP_NO).toString().trim().toUpperCase()+"%'";
		}
*//*		if(criteriaMap.get(TENDER_FILE_NO) != null){
			dynQuery = dynQuery + " and wo.negotiationNumber in (select tr1.negotiationNumber from TenderResponse tr1 where " +
		"UPPER(tr1.tenderEstimate.worksPackage.tenderFileNumber) like '%"+criteriaMap.get(TENDER_FILE_NO).toString().trim().toUpperCase()+"%')";
		}
*/		if(criteriaMap.get(CONTRACTOR_ID) != null){
			dynQuery = dynQuery + " and wo.contractor.id = ? ";
			paramList.add(criteriaMap.get(CONTRACTOR_ID));
		}
		if(criteriaMap.get("DEPT_ID") != null){
			dynQuery = dynQuery + " and wo.id in (select we.workOrder.id from WorkOrderEstimate we where we.workOrder.id=wo.id and " +
					" we.estimate.executingDepartment.id = ?) ";
			paramList.add(criteriaMap.get("DEPT_ID"));
		}
/*		if(criteriaMap.get(TENDER_NO) != null && !"".equalsIgnoreCase((String)criteriaMap.get(TENDER_NO))){
			logger.debug("-------TENDER_NO-----------"+criteriaMap.get(TENDER_NO));
			dynQuery = dynQuery + " and wo.abstractEstimate.id in "+
			"(select te.abstractEstimate.id from TenderEstimate te where te.tenderHeader.tenderNo = ? ) ";
			paramList.add(criteriaMap.get(TENDER_NO));
		}
*/		
		if(criteriaMap.get(PROJECT_CODE) != null){
			//dynQuery = dynQuery + " and wo.abstractEstimate.projectCode.code = ? ";
			//paramList.add(criteriaMap.get(PROJECT_CODE));
			dynQuery = dynQuery + " and wo.abstractEstimate.projectCode.code like '%" +criteriaMap.get(PROJECT_CODE) +"%'";
		}	
	
		if(criteriaMap.get(ACTION_FLAG) != null && criteriaMap.get(ACTION_FLAG).toString().equalsIgnoreCase("searchWOForMB")) {
				dynQuery = dynQuery + " and workOrderEstimate.id not in (select distinct mbh.workOrderEstimate.id " 
												+ "from MBHeader mbh where mbh.state.value=? " 
												+ " or mbh.state.value=?"
												+ " or mbh.state.value=?"
												+ " or mbh.state.value=? or mbh.state.value=? )" +
												"and workOrderEstimate.id not in (select distinct mbh.workOrderEstimate.id " +
												"from MBHeader mbh left join mbh.mbBills mbBills where " +
												" mbh.state.previous.value = ? and  mbBills.egBillregister.billstatus = ? and " +
												" mbBills.egBillregister.billtype=?)";
				paramList.add(MBHeader.MeasurementBookStatus.CREATED.toString());
				paramList.add(MBHeader.MeasurementBookStatus.CHECKED.toString());
				paramList.add(MBHeader.MeasurementBookStatus.RESUBMITTED.toString());
				paramList.add(MBHeader.MeasurementBookStatus.REJECTED.toString());
				paramList.add(MBHeader.MeasurementBookStatus.NEW.toString()); 
				paramList.add(MBHeader.MeasurementBookStatus.APPROVED.toString());	
				paramList.add(MBHeader.MeasurementBookStatus.APPROVED.toString());	
				paramList.add(getFinalBillTypeConfigValue());  
		}   
	
		if(criteriaMap.get(ACTION_FLAG) != null && criteriaMap.get(ACTION_FLAG).toString().equalsIgnoreCase("searchWOForBilling")) {
			dynQuery = dynQuery
					+ " and workOrderEstimate.id not in"
					+ " (select distinct mbh.workOrderEstimate.id from MBHeader mbh left join mbh.mbBills mbBills where mbh.state.previous.value = ? "
					+ " and (mbBills.egBillregister.billstatus <> ? and mbBills.egBillregister.billtype = ?)"
					+ " and mbh.workOrderEstimate.workOrder.state.previous.value=? and"
					+ " mbh.workOrderEstimate.estimate.state.previous.value=?)";
				//	+ " and workOrderEstimate.workOrder.id not in (select wo1.parent from WorkOrder wo1 where wo1.parent is not null and wo1.egwStatus.code not in('APPROVED','CANCELLED'))";

		paramList.add(MBHeader.MeasurementBookStatus.APPROVED.toString());		
		paramList.add(MBHeader.MeasurementBookStatus.CANCELLED.toString());	
		paramList.add(getFinalBillTypeConfigValue());
		paramList.add(criteriaMap.get(STATUS));
		paramList.add(AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString());
		if(criteriaMap.get("MB_PREPARED_BY")!=null && criteriaMap.get("MBREF_NO")!=null) {
			dynQuery = dynQuery + " and workOrderEstimate.id in " + 
				"(select distinct mbh1.workOrderEstimate.id from MBHeader mbh1 where mbh1.mbPreparedBy.idPersonalInformation=? and mbh1.mbRefNo like ?) ";
			paramList.add(criteriaMap.get("MB_PREPARED_BY"));
			paramList.add("%"+criteriaMap.get("MBREF_NO")+"%");
		}
		else 
		{ 
			if(criteriaMap.get("MB_PREPARED_BY")!=null) {
				dynQuery = dynQuery + " and workOrderEstimate.id in " + 
				"(select distinct mbh1.workOrderEstimate.id from MBHeader mbh1 where mbh1.mbPreparedBy.idPersonalInformation=?) ";
				paramList.add(criteriaMap.get("MB_PREPARED_BY"));
			}
			if(criteriaMap.get("MBREF_NO")!=null) {
				dynQuery = dynQuery + " and workOrderEstimate.id in " + 
				"(select distinct mbh1.workOrderEstimate.id from MBHeader mbh1 where mbh1.mbRefNo like ?) ";
				paramList.add("%"+criteriaMap.get("MBREF_NO")+"%");
			}
		}
		
	}
		if(criteriaMap.get(ACTION_FLAG) != null && criteriaMap.get(ACTION_FLAG).toString().equalsIgnoreCase("searchWOForReturnSD")) {
			dynQuery = dynQuery + " and workOrderEstimate.id  in " + 
					"(select distinct mbh.workOrderEstimate.id from MBHeader mbh left join mbh.mbBills mbBills join mbBills.egBillregister.egBilldetailes bd where mbh.state.previous.value = ? "
						 + " and (mbBills.egBillregister.billstatus = ?  " +
						 		"and bd.glcodeid in(:glcodelist))) "+
						 		"and wo.id not in (select rsd.workOrder.id from ReturnSecurityDeposit rsd where rsd.workOrder.id=wo.id and egwStatus.code not in ( 'APPROVED'  , 'CANCELLED' ) )  "+
						 		"and workOrderEstimate.id in (select distinct mbh.workOrderEstimate.id from MBHeader mbh left join mbh.mbBills mbBills where mbBills.egBillregister.billtype = :finalBillType) ";
						 		

			paramList.add(MBHeader.MeasurementBookStatus.APPROVED.toString());
			paramList.add(MBHeader.MeasurementBookStatus.APPROVED.toString());
			
			List coaList=new ArrayList();
			try{
				for(CChartOfAccounts coa:returnSecurityDepositService.getSDCOAList()){
					coaList.add(BigDecimal.valueOf(coa.getId()));
				}
				paramList.add(coaList);
			}
			catch(Exception e){
				logger.error("Exception in getting SD COA List");
			}
			
		}
		if(criteriaMap.get(ACTION_FLAG) != null && criteriaMap.get(ACTION_FLAG).toString().equalsIgnoreCase("searchWOForRetentionMR")) {
			dynQuery = dynQuery + " and workOrderEstimate.id  in " + 
					"(select mbh.workOrderEstimate.id from MBHeader mbh left join mbh.mbBills mbBills join mbBills.egBillregister.egBilldetailes bd where mbh.state.previous.value = ? "
						 + " and (mbBills.egBillregister.billstatus = ?  " +
						 		"and bd.glcodeid in(:glcodelist)) group by mbh.workOrderEstimate.id having sum(nvl(bd.creditamount,0))-sum(nvl(bd.debitamount,0))  >0  ) "+
						 		"and wo.id not in (select rmr.workOrder.id from RetentionMoneyRefund rmr where rmr.workOrder.id=wo.id and egwStatus.code not in ( 'APPROVED'  , 'CANCELLED' ) )  "+
						 		"and workOrderEstimate.id in (select distinct mbh.workOrderEstimate.id from MBHeader mbh left join mbh.mbBills mbBills where mbBills.egBillregister.billtype = :finalBillType) ";
						 		

			paramList.add(MBHeader.MeasurementBookStatus.APPROVED.toString());
			paramList.add(MBHeader.MeasurementBookStatus.APPROVED.toString());
			
			List coaList=new ArrayList();
			try{
				
				for(CChartOfAccounts coa:retentionMoneyRefundService.getRetentionMRCOAList()){
					coaList.add(BigDecimal.valueOf(coa.getId()));
				}
				paramList.add(coaList);
			}
			catch(Exception e){
				logger.error("Exception in getting SD COA List");
			}
			
		}
			
		//@Todo check action_flag
		
		logger.debug("Query is ::"+dynQuery);
		if(paramList.isEmpty())
			wolList = findAllBy(dynQuery);
		else{
			params = new Object[paramList.size()];
			params = paramList.toArray(params);
			if(criteriaMap.get(ACTION_FLAG) != null && (criteriaMap.get(ACTION_FLAG).toString().equalsIgnoreCase("searchWOForReturnSD")||criteriaMap.get(ACTION_FLAG).toString().equalsIgnoreCase("searchWOForRetentionMR"))) {
				 Query q = HibernateUtil.getCurrentSession().createQuery(dynQuery);
				 int index = 0;
				 for (Object param : params) {
				 if(param instanceof Collection)
					 q.setParameterList(String.valueOf("glcodelist"), (Collection)param);
				 else
					 q.setParameter(index, param);
				 index++;
				 }
				 q.setString("finalBillType",getFinalBillTypeConfigValue());
				wolList = q.list();
			}
			else{
				wolList = findAllBy(dynQuery,params);
				
			}	
		}
		return wolList;
		
	}
	
	
	/**
	 * This method will check whether approval limit is already used for all line item for the WO.
	 * Sum of WorkOrder.WorkOrderActivity.approvedQuantity <= ??Quantity??
	 * @param woId
	 * @return
	 */  
	public Boolean isApprovalLimitReachedForWO(Long woId){
		Boolean result = false;
		WorkOrder wo = findById(woId, false);  
		if(measurementBookService.getWorkOrderEstimatesForMB(wo.getWorkOrderEstimates()).isEmpty())
				result=true;
		return result;
	}
	
	public Boolean isWOValidforBill(Long woId){
		Boolean result = false;
		WorkOrder wo = findById(woId, false);    
		if(measurementBookService.getWorkOrderEstimatesForBill(wo.getWorkOrderEstimates()).isEmpty())
				result=true;
		return result;
	}

		
	/**
	 * This method will search and return list of woactivity based on searched criteria.
	 * Search criteria: WORKORDER_NO,ACTIVITY_DESC,LINEITEM_CODE
	 * Story #567 Search Line item to record measurement 
	 * @param criteriaMap
	 * @return
	 */
	public List<WorkOrderActivity> searchWOActivities(Map<String,Object> criteriaMap){
		logger.info("-------------------------Inside searchWOActivities-----------------------");
		List<WorkOrderActivity> woActivityList;
		
		String dynQuery = "select distinct woa from WorkOrderActivity woa left join woa.activity.schedule schedule" 
							+ " left join woa.activity.nonSor nonSor where woa.id != null " ;
		Object[] params;
		List<Object> paramList = new ArrayList<Object>();
		
		/*if(criteriaMap.get(WORKORDER_NO) != null){
			dynQuery = dynQuery + " and woa.workOrderEstimate.workOrder.workOrderNumber = ? ";
			paramList.add(criteriaMap.get(WORKORDER_NO));
		}*/
		if(criteriaMap.get(ACTIVITY_DESC) != null) {
			dynQuery = dynQuery + " and (" 
											+ "(UPPER(schedule.description) like '%" 
											+ ((String)criteriaMap.get(ACTIVITY_DESC)).toUpperCase()
											+ "%') or (" 
											+ " UPPER(nonSor.description)  like '%" 
											+ ((String)criteriaMap.get(ACTIVITY_DESC)).toUpperCase()
											+ "%' ))";
		}
		if(criteriaMap.get(ACTIVITY_CODE) != null) {
			dynQuery = dynQuery + " and " 
											+ "UPPER(schedule.code) like '%" 
											+ ((String)criteriaMap.get(ACTIVITY_CODE)).toUpperCase()
											+ "%'";
		}
		if(criteriaMap.get(WORKORDER_ESTIMATE_ID) != null){
			dynQuery = dynQuery + " and (woa.workOrderEstimate.estimate.id = ? and woa.workOrderEstimate.workOrder.egwStatus.code=?) or ((woa.workOrderEstimate.estimate.egwStatus is not null and woa.workOrderEstimate.estimate.egwStatus.code=?)" +
					" and (woa.workOrderEstimate.estimate.parent is not null and woa.workOrderEstimate.estimate.parent.id = ? ))";
			paramList.add(criteriaMap.get(WORKORDER_ESTIMATE_ID));
			paramList.add("APPROVED"); 
			paramList.add(AbstractEstimate.EstimateStatus.APPROVED.toString());
			paramList.add(criteriaMap.get(WORKORDER_ESTIMATE_ID));
		}
		if(criteriaMap.get(WORKORDER_ID) != null){
			dynQuery = dynQuery + " and (woa.workOrderEstimate.workOrder.id = ?) or ((woa.workOrderEstimate.workOrder.egwStatus is not null and woa.workOrderEstimate.workOrder.egwStatus.code=?)" +
					" and (woa.workOrderEstimate.workOrder.parent is not null and woa.workOrderEstimate.workOrder.parent.id = ? ))";
			paramList.add(criteriaMap.get(WORKORDER_ID));
			paramList.add("APPROVED");
			paramList.add(criteriaMap.get(WORKORDER_ID));
		}
		//@Todo state not in approved and cancelled
		dynQuery = dynQuery + "and woa.id not in (select distinct mbd.workOrderActivity.id from MBDetails mbd where " 
							+ "mbd.mbHeader.state.previous.value not in (?,?) and mbd.workOrderActivity.id = woa.id)";
		paramList.add(MBHeader.MeasurementBookStatus.APPROVED.toString());
		paramList.add(MBHeader.MeasurementBookStatus.CANCELLED.toString());
		

		Double extraPercentage = worksService.getConfigval(); 
		double factor=1;
		if(extraPercentage.doubleValue()>0)
			factor = 1+(extraPercentage/100);
		//@Todo ignore quantity of cancelled mb 
		if(!"Required".equals(worksService.getWorksConfigValue("ORDER_NUMBER_REQUIRED"))){
			dynQuery = dynQuery + "and ((woa.approvedQuantity*? > (select sum(mbd.quantity) as sumq from MBDetails mbd " 
			+ " where mbd.mbHeader.state.previous.value != ? group by mbd.workOrderActivity " 
			+ "having mbd.workOrderActivity.id = woa.id)) or (select sum(mbd.quantity) as sumq from MBDetails mbd " 
			+ " where mbd.mbHeader.state.previous.value != ? group by mbd.workOrderActivity " 
			+ "having mbd.workOrderActivity.id = woa.id) is null)";
			paramList.add(factor);
			paramList.add(MBHeader.MeasurementBookStatus.CANCELLED.toString());
			paramList.add(MBHeader.MeasurementBookStatus.CANCELLED.toString());
		}
		params 			= new Object[paramList.size()];
		params 			= paramList.toArray(params);
		dynQuery=dynQuery+" order by woa.activity.id asc";
		woActivityList 	= genericService.findAllBy(dynQuery,params);
	

		return woActivityList;
	}
	

	/**
	 * This method will return toPageNo for a line item from the last mb entry. 
	 * @param workOrderActivity
	 * @return
	 */
	public MBHeader findLastMBPageNoForLineItem(WorkOrderActivity workOrderActivity,Long mbHeaderId){
		logger.info("-------------------------Inside findLastMBPageNoForLineItem--------------");
				
		String query = "select distinct mbh from MBHeader mbh join mbh.mbDetails as mbDetail ";
		
//		if(workOrderActivity.getActivity().getSchedule() != null)
//			query = query + "where mbDetail.workOrderActivity.activity.schedule.id = "+workOrderActivity.getActivity().getSchedule().getId();
//		if(workOrderActivity.getActivity().getNonSor() != null)
//			query = query + "where mbDetail.workOrderActivity.activity.nonSor.id = "+workOrderActivity.getActivity().getNonSor().getId();
		
		query = query 	+ " where mbDetail.workOrderActivity.id = "+workOrderActivity.getId()
						+ " and mbh.id != " +mbHeaderId+" and mbh.state.previous.value='APPROVED' "
						+ " and mbh.modifiedDate < (select modifiedDate from MBHeader where id = "+mbHeaderId+")"
						+ " order by mbh.modifiedDate desc";
		
		List<MBHeader> mbHeaderList  = genericService.findAllBy(query);
		MBHeader result = null;
		if(mbHeaderList != null && !mbHeaderList.isEmpty())
			result = mbHeaderList.get(0);
		
		return result;
	}
	
	/**
	 * This method will check whether MB is created and pending for approval for the given WO.
	 * final bill is not approved for workorder??
	 * @param woId
	 * @return
	 */
//	public Boolean isMBCreatedAndPendingForWO(Long woId){
//		Boolean result = false;
//		
//		return result;
//	}
	

	/**
	 * This method will check whether final bill is already approved for wo or not.
	 * No MB is in a � approval pending� for the work order.
	 * @param woId
	 * @return
	 */
//	public Boolean isFinalBillApprovedForWO(Long woId){
//		Boolean result = false;
//		
//		return result;
//		
//	}
	
	
	public void setContractorService(
			PersistenceService<Contractor, Long> contractorService) {
		this.contractorService = contractorService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}
	
	/**
	 * The method return true if the work number has to be re-generated
	 * 
	 * @param entity an instance of <code>AbstractEstimate</code> containing the 
	 * estimate date 
	 * 
	 * @param financialYear an instance of <code>CFinancialYear</code> representing the 
	 * financial year for the estimate date.
	 * 
	 * @return a boolean value indicating if the estimate number change is required.
	 */
	/*private boolean workOrderNumberChangeRequired(AbstractEstimate entity, 
			CFinancialYear financialYear,WorkOrder workOrder){
		String[] workOrderNum = workOrder.getWorkOrderNumber().split("/");
		if(entity!=null && workOrderNum[0].equals(entity.getExecutingDepartment().getDeptCode()) && 
				workOrderNum[2].equals(financialYear.getFinYearRange())) {
			return false;
		}
		return true;
	}*/
	
	// end work order generation logic
	
	/**
	 * 
	 * @param workOrderNumberGenerators
	 */
	public void setWorkOrderNumberGenerators(WorkOrderNumberGenerator workOrderNumberGenerators) {
		this.workOrderNumberGenerators = workOrderNumberGenerators;
	}
	
	
	/*
	 * 
	 * @return Contractor List 
	 */
	public List<Contractor> getAllContractorForWorkOrder(){
		logger.info("-------------------------Inside getAllContractorForWorkOrder---------------------");
		//Assuming that status is inserted using db script
		String status = worksService.getWorksConfigValue("CONTRACTOR_STATUS");
		List<Contractor> contractorList = null;		
		contractorList = contractorService.findAllByNamedQuery("GET_All_CONTRACTORS",status);	 	
		return contractorList;
	}	
	

	/**
	 * Check whether any MB entry is pending for approval for the given WorkOrder
	 * @param woId
	 * @return
	 */
	public Boolean isMBInApprovalPendingForWO(String woNumber){
		Boolean result = false;
		Map<String,Object> criteriaMap = new HashMap<String, Object>();
		criteriaMap.put(WORKORDER_NO, woNumber);
		criteriaMap.put(ACTION_FLAG, "searchWOForMB");
		if(searchWO(criteriaMap).isEmpty())
			result = true;
		
		return result;
	}
	
	/**
	 * This method will return ActivitiesForWorkorder.
	 * @param tenderResponse
	 * @return Collection<EstimateLineItemsForWP>
	 */
	
/*	public Collection<EstimateLineItemsForWP> getActivitiesForWorkorder(TenderResponse tenderResponse){
		
		Map<Long,EstimateLineItemsForWP> resultMap = new HashMap<Long, EstimateLineItemsForWP>();
		Map<String, Integer> exceptionaSorMap = getSpecialUoms();
		List<String> tenderTypeList=worksService.getTendertypeList();
		String percTenderType="";
		if(tenderTypeList!=null && !tenderTypeList.isEmpty()){
			percTenderType=tenderTypeList.get(0);
		}
		for(TenderResponseActivity tenAct:tenderResponse.getTenderResponseActivities())
		{			
			EstimateLineItemsForWP estlineItem = new EstimateLineItemsForWP();
			if(tenAct.getActivity().getSchedule()!=null){
				if(resultMap.containsKey(tenAct.getActivity().getSchedule().getId())){
					EstimateLineItemsForWP preEstlineItem = resultMap.get(tenAct.getActivity().getSchedule().getId());
					preEstlineItem.setQuantity(tenAct.getNegotiatedQuantity() + preEstlineItem.getQuantity());
						if(DateUtils.compareDates(tenAct.getActivity().getAbstractEstimate().getEstimateDate(),
				  			preEstlineItem.getEstimateDate())){
							if(tenderResponse.getTenderEstimate().getTenderType().equals(percTenderType)){
								preEstlineItem.setRate(tenAct.getNegotiatedRate());
							}else{
								preEstlineItem.setRate(tenAct.getActivity().getSORCurrentRate().getValue());
							}
					  		double result=1;
					  		if(tenAct.getActivity().getSchedule()!=null && exceptionaSorMap.containsKey(tenAct.getActivity().getUom().getUom())){
					  			 result = exceptionaSorMap.get(tenAct.getActivity().getUom().getUom()); 
					  			 preEstlineItem.setAmt((preEstlineItem.getQuantity()*preEstlineItem.getRate())/result);
					  		}else{
					  			preEstlineItem.setAmt((preEstlineItem.getQuantity()*preEstlineItem.getRate()));
					  		}
				  		preEstlineItem.setActivity(tenAct.getActivity());
				  	}
				  	resultMap.put(tenAct.getActivity().getSchedule().getId(), preEstlineItem);
				}
				else{
					
					addEstLineItem(tenAct, estlineItem);
					resultMap.put(tenAct.getActivity().getSchedule().getId(), estlineItem); 
				}
			}
			if(tenAct.getActivity().getNonSor()!=null)
			{
				addEstLineItem(tenAct, estlineItem);
				resultMap.put(tenAct.getActivity().getNonSor().getId(), estlineItem);
			}
		}
		return getEstLineItemsWithSrlNo(resultMap.values());
	}
*/	/**
	 * This method will return ActivitiesForWorkorder.
	 * @param tenderResponse
	 * @return Collection<EstimateLineItemsForWP>
	 */
	
	public Collection<EstimateLineItemsForWP> getActivitiesForWorkorder(GenericTenderResponse tenderResponse,AbstractEstimate estimate){
		
		Map<Long,EstimateLineItemsForWP> resultMap = new HashMap<Long, EstimateLineItemsForWP>();
		Map<String, Integer> exceptionaSorMap = getSpecialUoms();
		
		for(TenderResponseLine trl:tenderResponse.getResponseLines())
		{			
			EstimateLineItemsForWP estlineItem = new EstimateLineItemsForWP();
			Activity activity=getActivityFromTenderResponseLineAndEstimate(trl,estimate);
			if(activity.getSchedule()!=null){
				if(resultMap.containsKey(activity.getSchedule().getId())){
					EstimateLineItemsForWP preEstlineItem = resultMap.get(activity.getSchedule().getId());
					preEstlineItem.setQuantity(trl.getQuantityByUom().doubleValue()+ preEstlineItem.getQuantity());
						if(DateUtils.compareDates(estimate.getEstimateDate(),
								preEstlineItem.getEstimateDate())){
							if(tenderResponse.getBidType().equals(BidType.PERCENTAGE)){
								preEstlineItem.setRate(trl.getBidRateByUom().doubleValue());
							}else{
								preEstlineItem.setRate(activity.getSORCurrentRate().getValue());
							}
							double result=1;
							if(activity.getSchedule()!=null && exceptionaSorMap.containsKey(activity.getUom().getUom())){
								result = exceptionaSorMap.get(activity.getUom().getUom()); 
								preEstlineItem.setAmt((preEstlineItem.getQuantity()*preEstlineItem.getRate())/result);
							}else{
								preEstlineItem.setAmt((preEstlineItem.getQuantity()*preEstlineItem.getRate()));
							}
							preEstlineItem.setActivity(activity);
						}
						resultMap.put(activity.getSchedule().getId(), preEstlineItem);
				}
				else{
				
					addEstLineItem(trl,estimate,activity, estlineItem);
					resultMap.put(activity.getSchedule().getId(), estlineItem); 
				}
			}
			if(activity.getNonSor()!=null)
			{
				addEstLineItem(trl,estimate,activity, estlineItem);
				resultMap.put(activity.getNonSor().getId(), estlineItem);
			}
		}
		return getEstLineItemsWithSrlNo(resultMap.values());
	}
	
	public Activity getActivityFromTenderResponseLineAndEstimate(TenderResponseLine trl,AbstractEstimate estimate){
		Activity activity=null;
		if(trl.getTenderableEntity().getTenderableType().equals(TenderableType.WORKS_ACTIVITY_SOR)){
			
		String[] SOR_number=trl.getTenderableEntity().getNumber().split("\\^");//spitting categoryCode and SOR Code
		activity=(Activity) genericService.find("from Activity act where act.abstractEstimate.id=? and act.schedule.category.code=? and act.schedule.code=?", estimate.getId(),SOR_number[0],SOR_number[1]);
		}
		else if(trl.getTenderableEntity().getTenderableType().equals(TenderableType.WORKS_ACTIVITY_NONSOR))
		{
			activity=(Activity) genericService.find("from Activity act where act.abstractEstimate.id=? and act.nonSor.id=?",estimate.getId()
					,new Long(trl.getTenderableEntity().getNumber()));
		}
		return activity;
	}

	private Map<String, Integer> getSpecialUoms() {
		AjaxEstimateAction estaction = new AjaxEstimateAction();
        estaction.setWorksService(worksService);
        return estaction.getExceptionSOR();
	}
	
	private Collection<EstimateLineItemsForWP> getEstLineItemsWithSrlNo(Collection<EstimateLineItemsForWP> actList)
	{
		int i=1;
		Collection<EstimateLineItemsForWP> latestEstLineItemList = new ArrayList<EstimateLineItemsForWP>();
		for(EstimateLineItemsForWP act:actList){
		   act.setSrlNo(i);
		   latestEstLineItemList.add(act);
			i++;
		}
		return latestEstLineItemList;
	}
	
	private void addEstLineItem(TenderResponseLine trl,AbstractEstimate estimate,Activity activity,EstimateLineItemsForWP estlineItem) {
		if(activity.getSchedule()==null){
			estlineItem.setCode("");
			estlineItem.setSummary("");
			estlineItem.setDescription(activity.getNonSor().getDescription());
			estlineItem.setRate(trl.getBidRateByUom().doubleValue());
			estlineItem.setAmt(trl.getQuantityByUom().doubleValue()*trl.getBidRateByUom().doubleValue());
		}
		else{
		estlineItem.setCode(activity.getSchedule().getCode());
		estlineItem.setDescription(activity.getSchedule().getDescription());
		estlineItem.setRate(trl.getBidRateByUom().doubleValue());
		estlineItem.setSummary(activity.getSchedule().getSummary());
		Map<String, Integer> exceptionaSorMap = getSpecialUoms();
		double result=1;
  		if(exceptionaSorMap.containsKey(activity.getUom().getUom())){
  			 result = exceptionaSorMap.get(activity.getUom().getUom());
  		}
  		estlineItem.setAmt((trl.getQuantityByUom().doubleValue()*trl.getBidRateByUom().doubleValue())/result);
		}	
		estlineItem.setActivity(activity);
		estlineItem.setQuantity(trl.getQuantityByUom().doubleValue());
		estlineItem.setUom(activity.getUom().getUom());
		estlineItem.setConversionFactor(activity.getConversionFactor());
	}
	
/*	private void addEstLineItem(TenderResponseActivity act,EstimateLineItemsForWP estlineItem) {
		if(act.getActivity().getSchedule()==null){
			estlineItem.setCode("");
			estlineItem.setSummary("");
			estlineItem.setDescription(act.getActivity().getNonSor().getDescription());
			estlineItem.setRate(act.getNegotiatedRate());
			estlineItem.setAmt(act.getNegotiatedQuantity()*act.getNegotiatedRate());
		}
		else{
		estlineItem.setCode(act.getActivity().getSchedule().getCode());
		estlineItem.setDescription(act.getActivity().getSchedule().getDescription());
		estlineItem.setRate(act.getNegotiatedRate());
		estlineItem.setSummary(act.getActivity().getSchedule().getSummary());
		Map<String, Integer> exceptionaSorMap = getSpecialUoms();
		double result=1;
  		if(exceptionaSorMap.containsKey(act.getActivity().getUom().getUom())){
  			 result = exceptionaSorMap.get(act.getActivity().getUom().getUom());
  		}
  		estlineItem.setAmt((act.getNegotiatedQuantity()*act.getNegotiatedRate())/result);
		}	
		estlineItem.setActivity(act.getActivity());
		estlineItem.setQuantity(act.getNegotiatedQuantity());
		estlineItem.setUom(act.getActivity().getUom().getUom());
		estlineItem.setConversionFactor(act.getActivity().getConversionFactor());
	}
*/	
	/**
	 * This method will return ActivitiesForWorkorder.
	 * @param WorkOrder
	 * @return Collection<EstimateLineItemsForWP>
	 */
	
	public Collection<EstimateLineItemsForWP> getActivitiesForWorkorder(WorkOrder workOrder){
		
		Map<Long,EstimateLineItemsForWP> resultMap = new HashMap<Long, EstimateLineItemsForWP>();
		Map<String, Integer> exceptionaSorMap = getSpecialUoms();
        for(WorkOrderEstimate workOrderEstimate:workOrder.getWorkOrderEstimates()){
        for(WorkOrderActivity woAct:workOrderEstimate.getWorkOrderActivities())
		{			
			EstimateLineItemsForWP estlineItem = new EstimateLineItemsForWP();
			if(woAct.getActivity().getSchedule()!=null){
				if(resultMap.containsKey(woAct.getActivity().getSchedule().getId())){
					EstimateLineItemsForWP preEstlineItem = resultMap.get(woAct.getActivity().getSchedule().getId());
					preEstlineItem.setQuantity(woAct.getApprovedQuantity() + preEstlineItem.getQuantity());
						if(DateUtils.compareDates(woAct.getActivity().getAbstractEstimate().getEstimateDate(),
				  			preEstlineItem.getEstimateDate())){
							preEstlineItem.setRate(woAct.getActivity().getSORCurrentRate().getValue());
							double result=1;
					  		if(woAct.getActivity().getSchedule()!=null && exceptionaSorMap.containsKey(woAct.getActivity().getUom().getUom())){
					  			 result = exceptionaSorMap.get(woAct.getActivity().getUom().getUom()); 
					  			 preEstlineItem.setAmt((preEstlineItem.getQuantity()*preEstlineItem.getRate())/result);
					  		}else{
					  			preEstlineItem.setAmt((preEstlineItem.getQuantity()*preEstlineItem.getRate()));
					  		}
				  		preEstlineItem.setActivity(woAct.getActivity());
				  	}
				  	resultMap.put(woAct.getActivity().getSchedule().getId(), preEstlineItem);
				}
				else{
					
					addEstLineItem(woAct, estlineItem);
					resultMap.put(woAct.getActivity().getSchedule().getId(), estlineItem); 
				}
			}
			if(woAct.getActivity().getNonSor()!=null)
			{
				addEstLineItem(woAct, estlineItem);
				resultMap.put(woAct.getActivity().getNonSor().getId(), estlineItem);
			}
		}
	}
		return getEstLineItemsWithSrlNo(resultMap.values());
	}
	
	
	
	private void addEstLineItem(WorkOrderActivity act,EstimateLineItemsForWP estlineItem) {
		if(act.getActivity().getSchedule()==null){
			estlineItem.setCode("");
			estlineItem.setSummary("");
			estlineItem.setDescription(act.getActivity().getNonSor().getDescription());
			estlineItem.setRate(act.getApprovedRate());
			estlineItem.setAmt((act.getApprovedQuantity()*act.getApprovedRate()));
		}
		else{
		estlineItem.setCode(act.getActivity().getSchedule().getCode());
		estlineItem.setDescription(act.getActivity().getSchedule().getDescription());
		estlineItem.setRate(act.getApprovedRate());
		estlineItem.setSummary(act.getActivity().getSchedule().getSummary());
		Map<String, Integer> exceptionaSorMap = getSpecialUoms();
		double result=1;
  		if(exceptionaSorMap.containsKey(act.getActivity().getUom().getUom())){
  			 result = exceptionaSorMap.get(act.getActivity().getUom().getUom());
  		}
		estlineItem.setAmt((act.getApprovedQuantity()*act.getApprovedRate())/result);
		}	
		estlineItem.setActivity(act.getActivity());
		estlineItem.setQuantity(act.getApprovedQuantity());
		estlineItem.setUom(act.getActivity().getUom().getUom());
		estlineItem.setConversionFactor(act.getActivity().getConversionFactor());
	}
	/**
	 * returns headermap for pdf
	 * @param workOrder
	 * @return
	 */
	
	public Map createHeaderParams(WorkOrder workOrder,String type){
		Map<String,Object> reportParams = new HashMap<String,Object>();
		DecimalFormat df = new DecimalFormat("###.##");
		if(workOrder!=null) {
			if("estimate".equalsIgnoreCase(type)){
				for(WorkOrderEstimate workOrderEstimate:workOrder.getWorkOrderEstimates()){
					if(workOrderEstimate!=null && workOrderEstimate.getEstimate()!=null){
						reportParams.put("deptName", workOrderEstimate.getEstimate().getExecutingDepartment().getDeptName());
						Boundary b = getTopLevelBoundary(workOrderEstimate.getEstimate().getWard());
						reportParams.put("cityName",b==null?"":b.getName());
					
						reportParams.put("deptAddress",workOrderEstimate.getEstimate().getExecutingDepartment().getDeptAddress());
						reportParams.put("aeWorkNameForEstimate",workOrderEstimate.getEstimate().getName());
						reportParams.put("negotiatedAmtForEstimate",workOrder.getWorkOrderAmount());
						reportParams.put("estimateNo",workOrderEstimate.getEstimate().getEstimateNumber());
						reportParams.put("estimateDate",workOrderEstimate.getEstimate().getEstimateDate());
						
						BudgetGroup bgtgrp = workOrderEstimate.getEstimate().getFinancialDetails().get(0).getBudgetGroup();
						if(bgtgrp!=null)
						{
							reportParams.put("budgetHead",bgtgrp.getName());
						}				
						if(workOrderEstimate.getEstimate().getProjectCode()!=null)
							reportParams.put("projectCode",workOrderEstimate.getEstimate().getProjectCode().getCode());
					}
			  }
			} else {
				//reportParams.put("grandTotal", getGrandTotal(aeForWp));
				List<WorkOrderEstimate> aeList=getAbstractEstimateListForWp(workOrder);
				String projectCode=getProjectCodeListForAe(aeList);
				reportParams.put("projectCodeList",projectCode);
				WorksPackage wp=workspackageService.findByNamedQuery("GET_WORKSPACKAGE_PACKAGENUMBER", workOrder.getPackageNumber());
				if(wp!=null)
					reportParams.put("workPackageDate",wp.getCreatedDate());
				if(wp!=null)
					reportParams.put("tenderFileNumber",wp.getTenderFileNumber());
				
				reportParams.put("workPackageNo", workOrder.getPackageNumber());
				if(aeList!=null && !aeList.isEmpty()){
					reportParams.put("deptName", aeList.get(0).getEstimate().getExecutingDepartment().getDeptName());
					Boundary b = getTopLevelBoundary(aeList.get(0).getEstimate().getWard());
					reportParams.put("cityName",b==null?"":b.getName());
					reportParams.put("deptAddress",aeList.get(0).getEstimate().getExecutingDepartment().getDeptAddress());
				}
			}
		}
		if(workOrder!=null && workOrder.getContractor()!=null){
			String contractorAddress = workOrder.getContractor().getName()+"  ,  "+workOrder.getContractor().getCode();
			if(workOrder.getContractor().getPaymentAddress()!=null)
				contractorAddress = contractorAddress+"  ,  "+workOrder.getContractor().getPaymentAddress();
			reportParams.put("contractorAddress",contractorAddress);
		}
		List<State> history=null;
		AbstractEstimate estimate = workOrder.getWorkOrderEstimates().get(0).getEstimate();
		if(estimate.getIsSpillOverWorks()){
			reportParams.put("estFinancialSancDate", DateUtils.getFormattedDate(estimate.getCreatedDate(),"dd/MM/yyyy"));
		}
		else
		{
			if(estimate!=null && estimate.getCurrentState()!=null && estimate.getCurrentState().getHistory()!=null)
				history = estimate.getCurrentState().getHistory();
			if(!history.isEmpty() && history!=null){
				for(State state : history){
					if(state.getValue().equalsIgnoreCase("FINANCIALLY_SANCTIONED")){
						reportParams.put("estFinancialSancDate", DateUtils.getFormattedDate(state.getCreatedDate(),"dd/MM/yyyy"));
					}
				}
			}
		}

		reportParams.put("estimateAmount",new BigDecimal(df.format(workOrder.getWorkOrderEstimates().get(0).getEstimate().getEstimatedCost())));
		reportParams.put("jurisdiction",workOrder.getWorkOrderEstimates().get(0).getEstimate().getWard().getName());
		reportParams.put("WorkOrderObj",workOrder);
		 	
		return reportParams;
	}
	
	public double getGrandTotal(List<AbstractEstimateForWp> aeForWp){
		double grandTotal=0.00;
		for(AbstractEstimateForWp aeforWp:aeForWp){
			grandTotal=grandTotal+aeforWp.getNegotiatedAmtForWp();
		}
		return grandTotal;
	}
	
	protected Boundary getTopLevelBoundary(Boundary boundary) {
		Boundary b = boundary;
		while(b!=null && b.getParent()!=null){
			b=b.getParent();
		}
		return b;
	}
	
	protected String getProjectCodeListForAe(List<WorkOrderEstimate> aeList){
		String projectCodes="";
		int i=0;
		for(WorkOrderEstimate ae:aeList){
			if(ae.getEstimate().getProjectCode()!=null && ae.getEstimate().getProjectCode().getCode()!=null)
				projectCodes=projectCodes+ae.getEstimate().getProjectCode().getCode();
			
			if(i<aeList.size()-1)
				projectCodes=projectCodes.concat(", ");
				i++;
		}
		return projectCodes;
	}
	
	
	
	/**
	 * returns AbstractEstimateForWp list
	 * @param workOrder
	 * @return
	 */
	protected List<AbstractEstimateForWp>  getAeForWp(List<WorkOrderEstimate> aeList){
		List<AbstractEstimateForWp>  aeForWpList=new ArrayList<AbstractEstimateForWp>();
		int srlNo=0;
		for(WorkOrderEstimate ae:aeList){
			AbstractEstimateForWp aeForWp=new AbstractEstimateForWp();
			aeForWp.setSrlNo(++srlNo);
			aeForWp.setAeWorkNameForWp(ae.getEstimate().getName());
			aeForWp.setNegotiatedAmtForWp(getWorkOrderEstimateAmount(ae));
			aeForWpList.add(aeForWp);
		}
		return aeForWpList;
	}
	
	public double getWorkOrderEstimateAmount(WorkOrderEstimate ae)
	{
		double totalAmt=0.0;
		for(WorkOrderActivity wact: ae.getWorkOrderActivities()){
			totalAmt+=wact.getApprovedAmount();
		}
		return totalAmt;
	}
	
	public List<AbstractEstimateForWp>  getAeForWp(WorkOrder workOrder){
		List<WorkOrderEstimate> aeList=getAbstractEstimateListForWp(workOrder);
		return getAeForWp(aeList);
	}
	
	
	/**
	 * returns WorkOrderEstimate list
	 * @param workOrder
	 * @return
	 */
	public List<WorkOrderEstimate> getAbstractEstimateListForWp(WorkOrder workOrder){
		List<WorkOrderEstimate> aeList=new ArrayList<WorkOrderEstimate>();
		List<WorkOrderEstimate> workOrderEstimateList=workOrder.getWorkOrderEstimates();
		for(WorkOrderEstimate workOrderEstimate:workOrderEstimateList){
			aeList.add(workOrderEstimate);
		}
		return aeList;
	}

	public void setWorkspackageService(WorksPackageService workspackageService) {
		this.workspackageService = workspackageService;
	}

	public void setMeasurementBookService(
			MeasurementBookService measurementBookService) {
		this.measurementBookService = measurementBookService;
	}
	
	public String getFinalBillTypeConfigValue() {		
		return worksService.getWorksConfigValue("FinalBillType");
	}
	
	/**
	 * This method will search list of WO's for the given criteria and eligible for returning Security Deposit.
	 * CriteriaMap will have:CONTRACTOR_ID,CREATE_DATE,TENDER_NO,WORKORDER_NO,PROJECT_CODE
	 * Filter:
	 * 1)Work orders for which the approved final bill is not generated will NOT be retrieved for selection in the search result set. 
	 * 2)Get all the Work Order where SD_deducted is null or <SD_deducted - SD_refunded> is greater than zero
	 * @param criteriaMap
	 * @return
	 */
	public List<WorkOrder> searchWOForReturnSD(Map<String,Object> criteriaMap){
		logger.info("-------------------------Inside searchWOForReturnSD-----------------------");
		List<WorkOrder> filteredList 	= new ArrayList<WorkOrder>();
		criteriaMap.put(ACTION_FLAG, "searchWOForReturnSD");
		//Filter list for approval limit
		for(WorkOrder workorder:searchWO(criteriaMap)){
			if(isWOValidforReturnSD(workorder.getId()))
				filteredList.add(workorder);
		}
		
		return filteredList;
		
	}
	
	public List<WorkOrder> searchWOForRetentionMR(Map<String,Object> criteriaMap){
		logger.info("-------------------------Inside searchWOForRetentionMR-----------------------");
		List<WorkOrder> filteredList 	= new ArrayList<WorkOrder>();
		criteriaMap.put(ACTION_FLAG, "searchWOForRetentionMR");
		//Filter list for approval limit
		for(WorkOrder workorder:searchWO(criteriaMap)){
			if(isWOValidforRetentionMR(workorder.getId()))
				filteredList.add(workorder);
		}
		
		return filteredList;
		
	}
	
	public Boolean isWOValidforReturnSD(Long woId){
		Boolean result = false;
		WorkOrder wo = findById(woId, false);  
			if(wo.getSdDeducted()==0 || (wo.getSdDeducted()!=0 && (wo.getSdDeducted()-wo.getSdRefunded()>0))){
				result=true;
			}
		return result;
	}
	
	private Boolean isWOValidforRetentionMR(Long woId){
		Boolean result = false;
		WorkOrder wo = findById(woId, false);  
			if(wo.getRetentionMoneyFromBill()==0 || (wo.getRetentionMoneyFromBill()!=0 && (wo.getRetentionMoneyFromBill()-wo.getRetentionMoneyRefunded()>0))){
				result=true;
			}
		return result;
	}

	public void setReturnSecurityDepositService(
			ReturnSecurityDepositService returnSecurityDepositService) {
		this.returnSecurityDepositService = returnSecurityDepositService;
	}
	
	public WorkOrder getWOForBillId(Long billId){
		if(billId==null)
			throw new EGOVRuntimeException("billId is Null");
		return (WorkOrder) persistenceService.findByNamedQuery("GET_WORKORDERWITHBILLID", billId); 
	
	}
	
	
	public BigDecimal getRevisionEstimateWOAmount(WorkOrder workOrder){
		BigDecimal amount=new BigDecimal(0);
		List<WorkOrder> workOrderList=new LinkedList<WorkOrder>(); 
		workOrderList=persistenceService.findAllBy("from WorkOrder wo where wo.egwStatus.code='APPROVED' and wo.parent is not null and wo.parent.id="+workOrder.getId());
		for(WorkOrder wo:workOrderList){
			amount=amount.add(BigDecimal.valueOf(wo.getWorkOrderAmount()));
		}
		amount=amount.add(BigDecimal.valueOf(workOrder.getWorkOrderAmount())); 
		return amount;
	}
	
	public List<WorkOrder> getWOForCompletionDateChange(Date currentDate){
		List<WorkOrder> workOrderList=new ArrayList<WorkOrder>();
		String noOfDays=worksService.getWorksConfigValue(WorksConstants.NOOFDAYSBEFOREWOCOMPLETIONDATENOTIFICATION);
		Date currentCompletionDate=DateUtils.add(currentDate,Calendar.DAY_OF_MONTH , Integer.parseInt(noOfDays));
		String finalBillType=worksService.getWorksConfigValue(WorksConstants.BILL_TYPE_FINALBILL);
		Object[] params = new Object[]{currentCompletionDate,finalBillType,finalBillType};
		workOrderList=persistenceService.findAllByNamedQuery("GET_WOFORCOMPLETIONNOTIFICATION", params);
		return workOrderList;
	}
	
	/**
	 * Populate all the cumulative fields related to WOA line item
	 * @param workOrderEstimate
	 * @return
	 */
	public WorkOrderEstimate calculateCumulativeDetails(WorkOrderEstimate workOrderEstimate){
		List<WorkOrderActivity> woaList = workOrderEstimate.getWorkOrderActivities();
		double lPrevCumlvQuant = 0;
		for(WorkOrderActivity detail:woaList){	
			if(detail.getActivity().getParent()==null) {
				lPrevCumlvQuant = measurementBookService.prevCumulativeQuantity(detail.getId(),null,detail.getActivity().getId());
				detail.setTotalEstQuantity(measurementBookService.totalEstimatedQuantity(detail.getId(),null,detail.getActivity().getId()));
			}			
			else {
				detail.setParent((WorkOrderActivity)genericService.find("from WorkOrderActivity where activity.id=? and workOrderEstimate.estimate.id=?", detail.getActivity().getParent().getId(),workOrderEstimate.getEstimate().getParent().getId()));
				lPrevCumlvQuant = measurementBookService.prevCumulativeQuantity(detail.getId(),null,detail.getActivity().getParent().getId());
				detail.setTotalEstQuantity(measurementBookService.totalEstimatedQuantity(detail.getId(),null,detail.getActivity().getParent().getId()));				
			}
			
			if(detail.getTotalEstQuantity()==0 && detail.getParent()!=null && detail.getParent().getActivity().getQuantity()!=0)
				detail.setTotalEstQuantity(detail.getApprovedQuantity());


			if(detail.getActivity().getRevisionType()!=null && !detail.getActivity().getRevisionType().equals(RevisionType.EXTRA_ITEM))
				detail.setApprovedAmount(detail.getTotalEstQuantity()*detail.getApprovedRate());
			detail.setPrevCumlvQuantity(lPrevCumlvQuant);			
		}
		return workOrderEstimate;
	}
	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setRetentionMoneyRefundService(
			RetentionMoneyRefundService retentionMoneyRefundService) {
		this.retentionMoneyRefundService = retentionMoneyRefundService;
	}
	
	/**
	 * Populate all the cumulative fields related to WOA line item
	 * @param workOrderEstimate
	 * @return 
	 */
	public WorkOrderEstimate calculateCumulativeDetailsForRE(WorkOrderEstimate workOrderEstimate){
		List<WorkOrderActivity> woaList = workOrderEstimate.getWorkOrderActivities();
		double lPrevCumlvQuant = 0;
		for(WorkOrderActivity detail:woaList){	
			if(detail.getActivity().getParent()==null) {
				lPrevCumlvQuant = measurementBookService.prevCumulativeQuantity(detail.getId(),null,detail.getActivity().getId());
				detail.setTotalEstQuantity(measurementBookService.totalEstimatedQuantityForRE(detail.getId(),workOrderEstimate.getEstimate().getId(),detail.getActivity().getId()));
			}			
			else {
				WorkOrderActivity woa = null; 
				if(detail.getActivity().getParent().getRevisionType()!=null && detail.getActivity().getParent().getRevisionType().equals(RevisionType.EXTRA_ITEM))
					woa = (WorkOrderActivity)genericService.find("from WorkOrderActivity where activity.id=? and workOrderEstimate.estimate.id=?", detail.getActivity().getParent().getId(),detail.getActivity().getParent().getAbstractEstimate().getId());
				else
					woa = (WorkOrderActivity)genericService.find("from WorkOrderActivity where activity.id=? and workOrderEstimate.estimate.id=?", detail.getActivity().getParent().getId(),workOrderEstimate.getEstimate().getParent().getId());
				
				detail.setParent(woa);
				lPrevCumlvQuant = measurementBookService.prevCumulativeQuantity(detail.getId(),null,detail.getActivity().getParent().getId());
				detail.setTotalEstQuantity(measurementBookService.totalEstimatedQuantityForRE(detail.getId(),workOrderEstimate.getEstimate().getId(),detail.getActivity().getParent().getId()));				
			}
			
			if(detail.getTotalEstQuantity()==0 && detail.getParent()!=null && detail.getParent().getActivity().getQuantity()!=0)
				detail.setTotalEstQuantity(detail.getApprovedQuantity());


			if(detail.getActivity().getRevisionType()!=null && !detail.getActivity().getRevisionType().equals(RevisionType.EXTRA_ITEM))
				detail.setApprovedAmount(detail.getTotalEstQuantity()*detail.getApprovedRate());
			detail.setPrevCumlvQuantity(lPrevCumlvQuant);			
		}
		return workOrderEstimate;
	}

}
	
