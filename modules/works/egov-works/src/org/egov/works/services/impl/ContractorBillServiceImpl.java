package org.egov.works.services.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.assets.model.AccountInfo;
import org.egov.assets.model.Asset;
import org.egov.assets.model.CapitaliseAsset;
import org.egov.assets.model.HeaderInfo;
import org.egov.assets.model.SubledgerInfo;
import org.egov.assets.model.VoucherInput;
import org.egov.assets.service.AssetService;
import org.egov.assets.service.CommonAssetsService;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.service.CommonsService;
import org.egov.dao.bills.BillsDaoFactory;
import org.egov.dao.bills.EgBilldetailsDAO;
import org.egov.egf.commons.EgovCommon;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.EgChecklists;
import org.egov.infstr.models.State;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.tender.BidType;
import org.egov.tender.model.GenericTenderResponse;
import org.egov.tender.services.common.GenericTenderService;
import org.egov.tender.services.tenderresponse.TenderResponseService;
import org.egov.works.models.contractorBill.AssetForBill;
import org.egov.works.models.contractorBill.ContractCertificateNumberGenerator;
import org.egov.works.models.contractorBill.ContractorBillNumberGenerator;
import org.egov.works.models.contractorBill.ContractorBillRegister;
import org.egov.works.models.contractorBill.DeductionTypeForBill;
import org.egov.works.models.contractorBill.StatutoryDeductionsForBill;
import org.egov.works.models.contractorBill.WorkCompletionDetailInfo;
import org.egov.works.models.contractorBill.WorkCompletionInfo;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.measurementbook.MBBill;
import org.egov.works.models.measurementbook.MBDetails;
import org.egov.works.models.measurementbook.MBForCancelledBill;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.revisionEstimate.RevisionType;
import org.egov.works.models.tender.SetStatus;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.ContractorBillService;
import org.egov.works.services.MeasurementBookService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
import org.hibernate.Query;
import org.hibernate.Session;




/**
 * This class will expose all Contractor Bill related operations.
 * @author Sathish P
 *
 */
public class ContractorBillServiceImpl extends BaseServiceImpl<ContractorBillRegister, Long>
										implements ContractorBillService{
	private static final Logger logger = Logger.getLogger(ContractorBillServiceImpl.class);	
	
	private WorksService worksService;
	private PersistenceService<EgChecklists, Long> checklistService;
	private AssetService assetService;
	private CommonAssetsService commonAssetsService;
	private ContractorBillNumberGenerator contractorBillNumberGenerator;
	private ContractCertificateNumberGenerator contractCertificateNumberGenerator; 
	private EgovCommon egovCommon;
	
	private CommonsService commonsService;
	private static final String WORKS_NETPAYABLE_CODE ="WORKS_NETPAYABLE_CODE";
	private static final String CONTRACTOR_ADVANCE_CODE="CONTRACTOR_ADVANCE_CODE";
	private static final String RETENTION_MONEY_PURPOSE="RETENTION_MONEY_PURPOSE";
	private static final String EGF="EGF";
	public static final String WORKORDER_NO		= "WORKORDER_NO";
	public static final String CONTRACTOR_ID	= "CONTRACTOR_ID";
	public static final String BILLSTATUS		= "BILLSTATUS";
	public static final String BILLNO			= "BILLNO";
	public static final String FROM_DATE        ="FROM_DATE";
	public static final String TO_DATE          ="TO_DATE";
	public static final String FROMDATE         ="fromDate";
	public static final String TODATE           ="toDate";
	public static final String PARAM           ="param_";
	private TenderResponseService genericTenderResponseService;
	private MeasurementBookService measurementBookService;
	private GenericTenderService genericTenderService;
	private ContractorBillRegister egBillRegister;
	private MBHeader mbHeader;
	private Long workOrderId; 
	private List<StatutoryDeductionsForBill> sortedStatutorySortedList;
	private List<DeductionTypeForBill> sortedStandardDeductionList;
	private List<EgBilldetails> customDeductionList;	
	private List<EgBilldetails> releaseWithHeldList;
	private List<BigDecimal> glcodeIdList;
	private List<BigDecimal> retentionMoneyglcodeIdList ;
	private BigDecimal netPayableAmount=BigDecimal.ZERO;
	private BigDecimal advanceAdjustment;
	private BigDecimal totalAmount = new BigDecimal(0);
	private WorkOrderEstimate woEstimate; 

	public ContractorBillServiceImpl(PersistenceService<ContractorBillRegister, Long> persistenceService) {
		super(persistenceService);
	}
	
	/**
	 * Check if Contractor Bill entries are within approved limit or not.
	 * @param mbHeader
	 * @return
	 */
	public List getBillType(){

		String configVal = worksService.getWorksConfigValue("BILLTYPE");
		List billTypeList = new LinkedList();

		if(StringUtils.isNotBlank(configVal)){
			String[] configVals=configVal.split(",");
			for(int i=0; i<configVals.length;i++)
				billTypeList.add(configVals[i]);
		}
		return billTypeList;

	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}
	
	/**
	 * The method return true if the bill number has to be re-generated
	 * 
	 * @param bill an instance of <code>EgBillregister</code> containing the 
	 * bill date 
	 * 
	 * @param financialYear an instance of <code>CFinancialYear</code> representing the 
	 * financial year for the estimate date.
	 * 
	 * @return a boolean value indicating if the bill number change is required.
	 */
	public boolean contractorBillNumberChangeRequired(EgBillregister bill, WorkOrder workOrder, CFinancialYear financialYear){
		//String[] estNum = bill.getBillnumber().split("/");
		
		/*if(estNum[0].equals(workOrder.getAbstractEstimate().getExecutingDepartment().getDeptCode()) && 
				estNum[2].equals(financialYear.getFinYearRange())){
			return false;
		}*/
		return true;
	}
	
	/**
	 * The method return number if the bill number has to be generated
	 * 
	 * @param bill an instance of <code>EgBillregister</code> containing the 
	 * bill date representing the financial year.
	 * 
	 * @param workOrder an instance of <code>WorkOrder</code> representing the 
	 * executing department.
	 * 
	 * @return a boolean value indicating if the bill number change is required.
	 */
	public String generateContractorBillNumber(EgBillregister bill, WorkOrder workOrder, WorkOrderEstimate workOrderEstimate) {
		CFinancialYear financialYear = getCurrentFinancialYear(bill.getBilldate());
		return contractorBillNumberGenerator.getBillNumber(workOrder, financialYear, workOrderEstimate);
	}

	public void setContractorBillNumberGenerator(
			ContractorBillNumberGenerator contractorBillNumberGenerator) {
		this.contractorBillNumberGenerator = contractorBillNumberGenerator;
	}
	

	/**
	 * Get utilized amount amount for a given workorder, including approved, unapproved bill(Bill other than cancelled and approved)
	 * and approved MB
	 * @param workOrderId
	 * @return
	 */
	/*public BigDecimal getTotalUtilizedAmount(Long workOrderId,Date asOnDate) {
		
		BigDecimal as = getUtilizedAmountForBill(workOrderId,asOnDate)
				.add(getUtlizedAmountForUnArrovedBill(workOrderId,asOnDate))
				.add(getApprovedMBAmount(workOrderId,asOnDate));
		return as;
	}*/

	/**
	 * Get utilized amount amount for a given workorder, including approved and unapproved bill(Bill other than cancelled and approved).
	 * @return
	 */
	/*public BigDecimal getUtilizedAmountForBill(Long workOrderId,Date asOnDate){
		BigDecimal result = BigDecimal.ZERO;
		WorkOrder workOrder = workOrderService.findById(workOrderId, false);
		Map<String,Object> searchMap = new HashMap<String, Object>();
		//searchMap.put("budgetheadid", workOrder.getAbstractEstimate().getFinancialDetails().get(0).getBudgetGroup().getId());
		searchMap.put("asondate", asOnDate);
		
		BigDecimal queryVal = null;
		try {
			queryVal = budgetDetailsDAO.getActualBudgetUtilized(searchMap);
		} catch(ValidationException valEx){
			logger.error(valEx);
		}

		if(queryVal != null)
			result = result.add(queryVal);
		
		return result;
	}*/
	
	/**
	 * The method return BigDecimal 
	 * 
	 * @param egBillregister an instance of <code>EgBillregister</code>.
	 * 
	 * @param workOrder an instance of <code>WorkOrder</code>.
	 * 
	 * @return a BigDecimal value indicating contractor total pending balance.
	 */
	public BigDecimal calculateContractorTotalPendingBalance(Date billDate,WorkOrder workOrder,WorkOrderEstimate workOrderEstimate, Long billId){ 		
		 Integer accountdetailType=null;
		 Integer accountdetailkey=null;		 
		 List<AppConfigValues> appConfigValuesList=worksService.getAppConfigValue(EGF,CONTRACTOR_ADVANCE_CODE);
		 String glCode=appConfigValuesList.get(0).getValue();
		 Accountdetailtype accountdetailtypeObj=worksService.getAccountdetailtypeByName("contractor");		
		 if(accountdetailtypeObj!=null && accountdetailtypeObj.getId()!=null)
			 accountdetailType=accountdetailtypeObj.getId();		 
		 accountdetailkey=new Integer(workOrder.getContractor().getId().toString());		 
		 String fundCode=workOrderEstimate.getEstimate().getFinancialDetails().get(0).getFund().getCode();
		 BigDecimal totalPendingBalance=BigDecimal.ZERO;
		 logger.debug("--bill-date"+billDate+"---glCode-----"+glCode+"---------fundCode----"+fundCode+"--accountdetailType--"+accountdetailType+"--accountdetailkey---"+accountdetailkey);
		 try{
		  totalPendingBalance=egovCommon.getAccountBalanceforDate(billDate, glCode, fundCode, accountdetailType,accountdetailkey);
		  logger.debug("1------totalPendingBalancetotalPendingBalancetotalPendingBalance  "+totalPendingBalance);
		 }catch(ValidationException e){			
			 logger.error("Exception---2--------"+e);			 
			 totalPendingBalance=new BigDecimal("0.00");
			// throw new ValidationException(Arrays.asList(new ValidationError("Unable to process","Unable to process")));
		 }catch(Exception e1){			
			 logger.error("Exception----3-------"+e1);
			 totalPendingBalance=new BigDecimal("0.00");
		 }
		 
		 if(totalPendingBalance!=null && totalPendingBalance.compareTo(BigDecimal.ZERO)>0){			
			 BigDecimal totalAdvanceAjustmentAmt=getAdvanceAdjstDeductionTotAmountForContractor(billDate,EGF,CONTRACTOR_ADVANCE_CODE,workOrder.getContractor().getCode(),billId);
			 if(totalAdvanceAjustmentAmt!=null && totalAdvanceAjustmentAmt.compareTo(BigDecimal.ZERO)>0
					 && totalPendingBalance.compareTo(totalAdvanceAjustmentAmt)>0)
				 totalPendingBalance=totalPendingBalance.subtract(totalAdvanceAjustmentAmt);
		}
		 
		 return totalPendingBalance;
	}	
	
	/**
	 * API will returns the Total Amount for advanceajustment  upto billdate for specific contractor
	 * @return  BigDecimal 
	 */
	public BigDecimal getAdvanceAdjstDeductionTotAmountForContractor(Date billDate,String moduleName, String contractorAdvaceCode, String contractorCode, Long billId){
		logger.debug("1---getAdvanceAdjustmentDeductionTotAmount-----bill date "+billDate+"-----moduleName--"+moduleName+"------contractorCode-----"+contractorCode);
		BigDecimal advanceAdjustment=BigDecimal.ZERO;
		List<Long> billIdList=getBillIdListUptoBillDate(billDate,contractorCode, billId);
		
		if(billIdList==null || billIdList.isEmpty())
			billIdList.add(null);
		String glCode="";
		List<AppConfigValues> appConfigValuesList=worksService.getAppConfigValue(moduleName,contractorAdvaceCode);
		if(appConfigValuesList!=null && !appConfigValuesList.isEmpty() && 
		  appConfigValuesList.get(0).getValue()!=null){
		   glCode=appConfigValuesList.get(0).getValue();
		}
		
		Long coaId=null;
		List<CChartOfAccounts>  coaList=genericService.findAllBy("from CChartOfAccounts coa where coa.glcode=?",glCode);
		if(!coaList.isEmpty())
			coaId=coaList.get(0).getId();	
		
		if(coaId==null)
			coaId=new Long("0");			
		
		List<EgBilldetails> egBilldetailsList=genericService.findAllByNamedQuery("getAdvanceAjustementTotAmt",new BigDecimal(coaId),billIdList);
		
		for(EgBilldetails egBilldetails:egBilldetailsList){
			if(egBilldetails.getCreditamount()!=null)
				advanceAdjustment=advanceAdjustment.add(egBilldetails.getCreditamount());
		}

		return advanceAdjustment;
	}
		
	/**
	 * API will returns the billId list for the contractor upto billdate 
	 * @return  BigDecimal 
	 */
	public List<Long> getBillIdListUptoBillDate(Date billDate,String contractorCode,Long billId){
		List<Long> billIdList=new ArrayList<Long>();
		logger.debug("---inside getBillIdListForWoUptoBillDate----");
		ArrayList<Object> params=new ArrayList<Object>();
		String whereClause="";
		params.add(contractorCode);
		params.add(billDate);
		if(billId!=null) {
			whereClause=" egBillRegister.billdate <= ? and egBillRegister.billstatus='APPROVED' and egBillRegister.id!=?)";
			params.add(billId);
		}
		else {
			whereClause=" egBillRegister.billdate <= ? and egBillRegister.billstatus='APPROVED')";
		}
		
		
		List<EgBillregister> egBillregisterList=genericService.findAllBy("select distinct mbBills.egBillregister from MBHeader mbh left join mbh.mbBills mbBills " +
					"where mbh.workOrder.contractor.code=? and mbBills.egBillregister.id in (select egBillRegister.id from org.egov.model.bills.EgBillregister egBillRegister " +
					" where"+whereClause,params.toArray());
		if(!egBillregisterList.isEmpty()){	
			for(EgBillregister egBillregister:egBillregisterList){
			billIdList.add(egBillregister.getId());
			}
		}
		return billIdList;
	}
	


	/**
	 * Get sum of all bill of given work orders which are still unapproved.
	 * @param workOrderId
	 * @return
	 */
	public BigDecimal getUtlizedAmountForUnArrovedBill(Long workOrderId,Date asOnDate) {
		BigDecimal result = BigDecimal.ZERO;
		Object[] params = new Object[]{workOrderId,asOnDate};
		BigDecimal queryVal = (BigDecimal) genericService.findByNamedQuery("getUtlizedAmountForUnArrovedBill",params);
		if(queryVal != null)
			result = result.add(queryVal);
		
		return result;
	}
	
	/**
	 * This method will return cumulative amount for all approved MB for a given workorder 
	 * @param workOrderId
	 * @return
	 */
	public BigDecimal getApprovedMBAmountOld(Long workOrderId,Long workOrderEstimateId, Date asOnDate) {
		BigDecimal result = BigDecimal.ZERO;
		Object[] params = new Object[]{workOrderId,workOrderEstimateId, asOnDate};
		Double queryVal = (Double) genericService.findByNamedQuery("totalApprovedMBAmount",params);
		Double queryVal2 = (Double) genericService.findByNamedQuery("totalApprovedMBAmountForCancelledBill",params);
		if(queryVal != null)
			result = result.add(BigDecimal.valueOf(queryVal));
		if(queryVal2 != null)
			result = result.add(BigDecimal.valueOf(queryVal2));		
		
		return result;
	}
	
	/**
	 * This method will return cumulative amount for all approved MB for a given workorder 
	 * @param workOrderId
	 * @return
	 */
	public BigDecimal getApprovedMBAmount(Long workOrderId,Long workOrderEstimateId, Date asOnDate) {
		BigDecimal result = BigDecimal.ZERO;
		Object[] params = new Object[]{workOrderId,workOrderEstimateId, asOnDate};
		List<MBDetails> approvedMBsList=genericService.findAllByNamedQuery("gettotalApprovedMBs", params);		
		//List<MBDetails> approvedMBsForCancelledBillList=genericService.findAllByNamedQuery("gettotalApprovedMBsForCancelledBill", params);
		
		WorkOrderEstimate workOrderEstimate=(WorkOrderEstimate)genericService.find("from WorkOrderEstimate where id=?",workOrderEstimateId);
		params = new Object[]{workOrderEstimate.getEstimate().getId(), asOnDate};
		List<MBDetails> approvedMBDsForOldREList=genericService.findAllByNamedQuery("gettotalApprovedMBDsFromOldRE", params);
		params = new Object[]{workOrderEstimate.getEstimate().getId(),workOrderEstimate.getEstimate().getId(), asOnDate};
		List<MBDetails> approvedREMBDsList=genericService.findAllByNamedQuery("gettotalREApprovedMBDs", params);	
		if(approvedMBDsForOldREList != null && !approvedMBDsForOldREList.isEmpty())
			approvedMBsList.addAll(approvedMBDsForOldREList);
		if(approvedREMBDsList != null && !approvedREMBDsList.isEmpty())
			approvedMBsList.addAll(approvedREMBDsList);
		//List<MBDetails> approvedREMBDsForCancelledBillList=genericService.findAllByNamedQuery("gettotalREApprovedMBDsForCancelledBill", params);
		//if(approvedREMBDsForCancelledBillList != null && !approvedREMBDsForCancelledBillList.isEmpty())
		//	approvedMBsForCancelledBillList.addAll(approvedREMBDsForCancelledBillList);
		
		GenericTenderResponse tenderResponse=genericTenderService.getGenericResponseByNumber(workOrderEstimate.getWorkOrder().getNegotiationNumber());
		String mbPercentagelevel=worksService.getWorksConfigValue(WorksConstants.MBPERCENTAPPCONFIGKEY);
		
		for(MBDetails mbd:approvedMBsList){
			boolean validBill=checkCancelBill(mbd);
			if(validBill==true){
			boolean partRateApplicable=checkPartRateApplicable(mbd);//To Check part Rate Applicable for the 2nd bill 
			if(mbd.getMbHeader().getRevisionEstimate()==null || (mbd.getMbHeader().getRevisionEstimate()!=null && mbd.getMbHeader().getRevisionEstimate().getEgwStatus().getCode().equals(AbstractEstimate.EstimateStatus.APPROVED.toString()))) {
				Double amount=0.0;
				if(mbPercentagelevel.equalsIgnoreCase(WorksConstants.MBPERCENTCONFIGVALUE) && tenderResponse!=null && tenderResponse.getBidType().equals(BidType.PERCENTAGE)){
					if(mbd.getPartRate()==0 && mbd.getReducedRate()==0 && partRateApplicable==false){
						if(mbd.getWorkOrderActivity().getActivity().getNonSor()==null){					
							amount=mbd.getWorkOrderActivity().getActivity().getRate().getValue()*mbd.getQuantity()*mbd.getWorkOrderActivity().getActivity().getConversionFactor();
						}
						else{
							amount=mbd.getWorkOrderActivity().getActivity().getRate().getValue()*mbd.getQuantity();
						}
					}
					else if(mbd.getPartRate()>0 && partRateApplicable==false){
						if(mbd.getWorkOrderActivity().getActivity().getNonSor()==null){					
							amount=mbd.getPartRate()*mbd.getQuantity()*mbd.getWorkOrderActivity().getActivity().getConversionFactor();
						}
						else{
							amount=mbd.getPartRate()*mbd.getQuantity();
						}
					}
					else if(mbd.getPartRate()>0 && partRateApplicable==true){
						if(mbd.getWorkOrderActivity().getActivity().getNonSor()==null){					
							amount=(mbd.getWorkOrderActivity().getActivity().getRate().getValue()-mbd.getPartRate())*mbd.getQuantity()*mbd.getWorkOrderActivity().getActivity().getConversionFactor();
						}
						else{
							amount=(mbd.getWorkOrderActivity().getActivity().getRate().getValue()-mbd.getPartRate())*mbd.getQuantity();
						}
					}
					else if(mbd.getReducedRate()>0 && partRateApplicable==false){
						if(mbd.getWorkOrderActivity().getActivity().getNonSor()==null){					
							amount=mbd.getReducedRate()*mbd.getQuantity()*mbd.getWorkOrderActivity().getActivity().getConversionFactor();
						}
						else{
							amount=mbd.getReducedRate()*mbd.getQuantity();
						}
					}
					if(mbd.getWorkOrderActivity().getActivity().getRevisionType()!=null &&mbd.getWorkOrderActivity().getActivity().getRevisionType().equals(RevisionType.EXTRA_ITEM)){
						result=result.add(BigDecimal.valueOf(amount));
					}
					else{
						if(tenderResponse.getPercentage().compareTo(new BigDecimal(0))>=0){
							amount=amount+(amount*tenderResponse.getPercentage().doubleValue()/100);
						}
						else{
							amount=amount-(amount*-1*tenderResponse.getPercentage().doubleValue()/100);
						}
						result=result.add(BigDecimal.valueOf(amount));
					}
					
				}
				else{
					if(mbd.getPartRate()==0 && mbd.getReducedRate()==0 && partRateApplicable==false){
						if(mbd.getWorkOrderActivity().getActivity().getNonSor()==null){					
							amount=mbd.getWorkOrderActivity().getApprovedRate()*mbd.getQuantity()*mbd.getWorkOrderActivity().getActivity().getConversionFactor();
						}
						else{
							amount=mbd.getWorkOrderActivity().getApprovedRate()*mbd.getQuantity();
						}
					}else if(mbd.getPartRate()>0 && partRateApplicable==false){
						if(mbd.getWorkOrderActivity().getActivity().getNonSor()==null){					
							amount=mbd.getPartRate()*mbd.getQuantity()*mbd.getWorkOrderActivity().getActivity().getConversionFactor();
						}
						else{
							amount=mbd.getPartRate()*mbd.getQuantity();
						}
					}
					else if(mbd.getPartRate()>0 && partRateApplicable==true){
						if(mbd.getWorkOrderActivity().getActivity().getNonSor()==null){					
							amount=(mbd.getWorkOrderActivity().getApprovedRate()-mbd.getPartRate())*mbd.getQuantity()*mbd.getWorkOrderActivity().getActivity().getConversionFactor();
						}
						else{
							amount=(mbd.getWorkOrderActivity().getApprovedRate()-mbd.getPartRate())*mbd.getQuantity();
						}
					}
					else if(mbd.getReducedRate()>0 && partRateApplicable==false){
						if(mbd.getWorkOrderActivity().getActivity().getNonSor()==null){					
							amount=mbd.getReducedRate()*mbd.getQuantity()*mbd.getWorkOrderActivity().getActivity().getConversionFactor();
						}
						else{
							amount=mbd.getReducedRate()*mbd.getQuantity();
						}
					}
					result=result.add(BigDecimal.valueOf(amount));
				}
				
			}
		}
		}
		/*for(MBDetails mbd:approvedMBsForCancelledBillList){
			Double amount=0.0;
			boolean validCancelBill=checkCancelBill(mbd);
			boolean partRateApplicable=checkPartRateApplicable(mbd);//To Check part Rate Applicable for the 2nd bill 
			if(validCancelBill==true){
				if(mbPercentagelevel.equalsIgnoreCase(WorksConstants.MBPERCENTCONFIGVALUE) && tenderResponse.getBidType().equals(BidType.PERCENTAGE)){
					if(mbd.getPartRate()==0 && mbd.getReducedRate()==0 && partRateApplicable==false){
						if(mbd.getWorkOrderActivity().getActivity().getNonSor()==null){
							amount=mbd.getWorkOrderActivity().getApprovedRate()*mbd.getQuantity()*mbd.getWorkOrderActivity().getActivity().getConversionFactor();
						}
						else{
							amount=mbd.getWorkOrderActivity().getApprovedRate()*mbd.getQuantity();
						}
					}
					else if(mbd.getPartRate()>0 && partRateApplicable==false){
						if(mbd.getWorkOrderActivity().getActivity().getNonSor()==null){
							amount=mbd.getPartRate()*mbd.getQuantity()*mbd.getWorkOrderActivity().getActivity().getConversionFactor();
						}
						else{
							amount=mbd.getPartRate()*mbd.getQuantity();
						}
					}
					else if(mbd.getPartRate()>0 && partRateApplicable==true){
						if(mbd.getWorkOrderActivity().getActivity().getNonSor()==null){
							amount=(mbd.getWorkOrderActivity().getApprovedRate()-mbd.getPartRate())*mbd.getQuantity()*mbd.getWorkOrderActivity().getActivity().getConversionFactor();
						}
						else{
							amount=(mbd.getWorkOrderActivity().getApprovedRate()-mbd.getPartRate())*mbd.getQuantity();
						}
					}
					else if(mbd.getReducedRate()>0 && partRateApplicable==false){
						if(mbd.getWorkOrderActivity().getActivity().getNonSor()==null){
							amount=mbd.getReducedRate()*mbd.getQuantity()*mbd.getWorkOrderActivity().getActivity().getConversionFactor();
						}
						else{
							amount=mbd.getReducedRate()*mbd.getQuantity();
						}
					}
				
					if(mbd.getMbHeader().getRevisionEstimate()!=null && mbd.getWorkOrderActivity().getActivity().getRevisionType()!=null &&mbd.getWorkOrderActivity().getActivity().getRevisionType().equals(RevisionType.EXTRA_ITEM)){
						result=result.add(BigDecimal.valueOf(amount));
					}
					else{
						if(tenderResponse.getPercentage().compareTo(new BigDecimal(0))>=0){
							amount=amount+(amount*tenderResponse.getPercentage().doubleValue()/100);
						}
						else{
							amount=amount-(amount*-1*tenderResponse.getPercentage().doubleValue()/100);
						}
						result=result.add(BigDecimal.valueOf(amount));
					}
				}
				else{
					if(mbd.getPartRate()==0 && mbd.getReducedRate()==0 && partRateApplicable==false){
						if(mbd.getWorkOrderActivity().getActivity().getNonSor()==null){
							amount=mbd.getWorkOrderActivity().getApprovedRate()*mbd.getQuantity()*mbd.getWorkOrderActivity().getActivity().getConversionFactor();
						}
						else{
							amount=mbd.getWorkOrderActivity().getApprovedRate()*mbd.getQuantity();
						}
					}
					if(mbd.getPartRate()>0 && partRateApplicable==false){
						if(mbd.getWorkOrderActivity().getActivity().getNonSor()==null){
							amount=mbd.getPartRate()*mbd.getQuantity()*mbd.getWorkOrderActivity().getActivity().getConversionFactor();
						}
						else{
							amount=mbd.getPartRate()*mbd.getQuantity();
						}
					}
					if(mbd.getPartRate()>0 &&  partRateApplicable==true){
						if(mbd.getWorkOrderActivity().getActivity().getNonSor()==null){
							amount=(mbd.getWorkOrderActivity().getApprovedRate()-mbd.getPartRate())*mbd.getQuantity()*mbd.getWorkOrderActivity().getActivity().getConversionFactor();
						}
						else{
							amount=(mbd.getWorkOrderActivity().getApprovedRate()-mbd.getPartRate())*mbd.getQuantity();
						}
					}
					if( mbd.getReducedRate()>0 && partRateApplicable==false){
						if(mbd.getWorkOrderActivity().getActivity().getNonSor()==null){
							amount=mbd.getReducedRate()*mbd.getQuantity()*mbd.getWorkOrderActivity().getActivity().getConversionFactor();
						}
						else{
							amount=mbd.getReducedRate()*mbd.getQuantity();
						}
					}
					result=result.add(BigDecimal.valueOf(amount));
				}
			}
		}*/
		return result;
	}
	
	
	
	/**
	 * API will returns the Standard deduction types as key and its mapped COA as map values
	 * @return map containing deduction type as key and string array of coa glcodes 
	 */
	public Map<String,String[]> getStandardDeductionsFromConfig(){
		String strDec = worksService.getWorksConfigValue("STANDARD_DEDUCTION");
		Map<String,String[]> map = new HashMap<String,String[]>();
		String[] splitedMainArr = strDec.split("\\|"); // get Type:Codes pairs
		for(int i=0;i<splitedMainArr.length;i++){
			String[] splitedSubArr = splitedMainArr[i].split(":"); // split Type:Codes pair
			String[] splitedACCodesArr = splitedSubArr[1].split(","); // split Codes for Type
			map.put(splitedSubArr[0], splitedACCodesArr);
		}
		return map;
	}

	/**
	 * returns the sanctioned budget for the year
	 * @param paramMap
	 * @return
	 * @throws ValidationException
	 */
	/*public BigDecimal getBudgetedAmtForYear(Long workOrderId,Date asOnDate)
	throws ValidationException {
		BigDecimal val = BigDecimal.ZERO;
		Map<String,Object> searchMap = new HashMap<String, Object>();
		WorkOrder wo =workOrderService.findById(workOrderId, false);
		List<FinancialDetail> fdList=new ArrayList<FinancialDetail>();
		List<FinancialDetail> fdList = financialDetailService.findAllByNamedQuery("getFinancialDetailByEstimateId",
				wo.getAbstractEstimate().getId());
		String finyearId = commonsService.getFinYearByDate(DateUtils.getFormattedDate(asOnDate,"dd-MMM-yyyy"));
		searchMap.put("financialyearid", Long.valueOf(finyearId));
		if(fdList!=null && !fdList.isEmpty()){
			if(fdList.get(0).getFunction()!=null && fdList.get(0).getFunction().getId()!=null)
				searchMap.put("functionid", fdList.get(0).getFunction().getId());
			if(fdList.get(0).getFunctionary()!=null && fdList.get(0).getFunctionary().getId()!=null)
				searchMap.put("functionaryid", fdList.get(0).getFunctionary().getId());
			if(fdList.get(0).getFund()!=null && fdList.get(0).getFund().getId()!=null)
				searchMap.put("fundid", fdList.get(0).getFund().getId());
			if(fdList.get(0).getBudgetGroup()!=null && fdList.get(0).getBudgetGroup().getId()!=null)
				searchMap.put("budgetheadid", fdList.get(0).getBudgetGroup().getId());
			if(fdList.get(0).getScheme()!=null && fdList.get(0).getScheme().getId()!=null)
				searchMap.put("schemeid", fdList.get(0).getScheme().getId());
			if(fdList.get(0).getSubScheme()!=null && fdList.get(0).getSubScheme().getId()!=null)
				searchMap.put("subschemeid", fdList.get(0).getSubScheme().getId());
			if(wo.getAbstractEstimate().getExecutingDepartment()!=null)
				searchMap.put("deptid", wo.getAbstractEstimate().getExecutingDepartment().getId());
		}
		//searchMap.put("boundaryid", wo.getAbstractEstimate().getWard().getId());
		try{
			val = budgetDetailsDAO.getBudgetedAmtForYear(searchMap);
		}
		catch(ValidationException valEx){
			logger.error(valEx);
		}
		return val;
	}*/
	

	
	/**
	 * for pdf starts here
	 * API will returns the Total value for the workorder upto billdate
	 * @return Double
	 */
	public BigDecimal getTotalValueWoForUptoBillDate(Date billDate,Long workOrderId, Long workOrderEstimateId){
		BigDecimal totalWorkValue=BigDecimal.ZERO;		
		List<EgBillregister> egBillregisterList=genericService.findAllBy("select distinct br from MBHeader mbh left join mbh.mbBills mbBills left join mbBills.egBillregister br  where  br.id in (select egBillRegister.id from org.egov.model.bills.EgBillregister egBillRegister " +
								 " where egBillRegister.billdate <= ?) and mbh.workOrder.id = ? and mbh.workOrderEstimate.id=?",billDate,workOrderId,workOrderEstimateId);		
		if(!egBillregisterList.isEmpty()){			
			for(EgBillregister egBillregister : egBillregisterList){	
				totalWorkValue=totalWorkValue.add(egBillregister.getBillamount());
			}
		}	
		return totalWorkValue;
	}
	
	public BigDecimal getWoValueExcludingWitheldReleaseAmt(Date billDate,Long workOrderId, Long workOrderEstimateId){
		BigDecimal totalWorkValue=BigDecimal.ZERO;
		ContractorBillRegister cbr = null;
		List<EgBillregister> egBillregisterList=genericService.findAllBy("select distinct br from MBHeader mbh left join mbh.mbBills mbBills left join mbBills.egBillregister br  where  br.id in (select egBillRegister.id from org.egov.model.bills.EgBillregister egBillRegister " +
								 " where egBillRegister.billdate <= ?) and mbh.workOrder.id = ? and mbh.workOrderEstimate.id=?",billDate,workOrderId,workOrderEstimateId);		
		if(!egBillregisterList.isEmpty()){			
			for(EgBillregister egBillregister : egBillregisterList){
				cbr = (ContractorBillRegister) genericService.find(" from ContractorBillRegister cbr  where id=? ",egBillregister.getId());
				if(cbr!=null)
					totalWorkValue=totalWorkValue.add(cbr.getWorkRecordedAmount());
			}
		}	
		return totalWorkValue;
	}
	
	public BigDecimal getTotalValueWoForUptoBillDateWithoutCancel(Date billDate,Long workOrderId, Long workOrderEstimateId, Long contractorBillRegId){
		BigDecimal totalWorkValue=BigDecimal.ZERO;		
		List<EgBillregister> egBillregisterList=genericService.findAllBy("select distinct br from MBHeader mbh left join mbh.mbBills mbBills left join mbBills.egBillregister br  where  br.id in (select egBillRegister.id from org.egov.model.bills.EgBillregister egBillRegister " +
								 " where egBillRegister.billdate <= ? and egBillRegister.id <= ? and egBillRegister.status.code!='CANCELLED' ) and mbh.workOrder.id = ? and mbh.workOrderEstimate.id=?",billDate,contractorBillRegId,workOrderId,workOrderEstimateId);		
		if(!egBillregisterList.isEmpty()){			
			for(EgBillregister egBillregister : egBillregisterList){	
				totalWorkValue=totalWorkValue.add(egBillregister.getBillamount());
			}
		}	
		return totalWorkValue;
	}
	
	/**
	 * API will returns the sorted  deduction list from appconfig
	 * @return List containing deduction type 
	 */
	public List<String> getSortedDeductionsFromConfig(String Key){
		String strDec = worksService.getWorksConfigValue(Key);
		List<String> sortedDedcutionList = new ArrayList<String>();
		String[] splitedMainArr = strDec.split("\\|"); 
		for(int i=0;i<splitedMainArr.length;i++){
			sortedDedcutionList.add(splitedMainArr[i]);
		}
		return sortedDedcutionList;
	}
	
	/**
	 * API will returns the sorted list for a given list values
	 * @return List containing sorted deduction names
	 */
	public List<StatutoryDeductionsForBill> getStatutoryDeductionSortedOrder(List<String> requiredOrder,List<StatutoryDeductionsForBill> givenEgBillPayeedetails){ 
		List<StatutoryDeductionsForBill> orderedResults = new ArrayList<StatutoryDeductionsForBill>();
    	for(String caseStatus : requiredOrder){     		
    		 for (StatutoryDeductionsForBill statDeductionDetails : givenEgBillPayeedetails) {
    			  if(caseStatus.equals(statDeductionDetails.getEgBillPayeeDtls().getRecovery().getType())){
    				 orderedResults.add(statDeductionDetails);
    			 }  
    		 } 
    	}
    	return orderedResults; 
	}
	
	
	public List<DeductionTypeForBill> getStandardDeductionSortedOrder(List<String> requiredOrder,List<DeductionTypeForBill> givenStandardList){
		List<DeductionTypeForBill> orderedResults = new ArrayList<DeductionTypeForBill>();
		for(String caseStatus : requiredOrder){     		
    		 for (DeductionTypeForBill deductionTypeForBill : givenStandardList) {
    			  if(caseStatus.equals(deductionTypeForBill.getDeductionType())){
    				 orderedResults.add(deductionTypeForBill);
    			 } 
    		 } 
    	}
    	return orderedResults; 
	}
	
	

	/**
	 * API will returns the statutory list for a given bill Id and type
	 * @return List containing Statutory deduction names 
	 */
	public List<StatutoryDeductionsForBill> getStatutoryListForBill(Long billId){
		return genericService.findAllBy("from StatutoryDeductionsForBill epd where epd.egBillPayeeDtls.egBilldetailsId.egBillregister.id=? " +
				"and epd.egBillPayeeDtls.recovery.id is not null",billId); 
	}
	
	/*
	 * * API will returns the standard deduction list for a given bill Id and type
	 * @return List containing Statutory deduction names 
	 */
	public List<DeductionTypeForBill> getStandardDeductionForBill(Long billId){
		return genericService.findAllBy("from DeductionTypeForBill dtb where dtb.egbill.id=?",billId);
	}
	
	public List<AssetForBill> getAssetForBill(Long billId){
		return genericService.findAllBy("from AssetForBill assetForBill where assetForBill.egbill.id=?",billId);
	}
	
	/**
	 * API will returns the Advance adjustment amount for a given bill Id and glcode
	 * @return  BigDecimal 
	 */
	public BigDecimal getAdvanceAdjustmentAmountForBill(Long billId){
		List<AppConfigValues> contractorGlcodeList = worksService.getAppConfigValue(EGF, CONTRACTOR_ADVANCE_CODE);
		BigDecimal advanceAdjustment=BigDecimal.ZERO;
		if(!contractorGlcodeList.isEmpty())
		{
			CChartOfAccounts coa =  commonsService.getCChartOfAccountsByGlCode(contractorGlcodeList.get(0).getValue());
			EgBilldetails egBilldetails=(EgBilldetails)genericService.find("from EgBilldetails ebd where ebd.glcodeid=? and " +
						"ebd.egBillregister.id=?",new BigDecimal(coa.getId()),billId);
				if(egBilldetails!=null && egBilldetails.getCreditamount()!=null)
					advanceAdjustment =egBilldetails.getCreditamount();
		}
		return advanceAdjustment;
	}
	
	/**
	 * API will returns the custom deduction list of egbilldetails excluding glcode
	 * @return  BigDecimal 
	 */
	public List<EgBilldetails> getCustomDeductionListforglcodes(List<BigDecimal> glcodeIdList,Long billId){		
		return genericService.findAllByNamedQuery("CustomDeductionList",billId,glcodeIdList);
	}
	public List<EgBilldetails> getRetentionMoneyListforglcodes(List<BigDecimal> glcodeIdList,Long billId){		
		return genericService.findAllByNamedQuery("RetentionMoneyDeductionList",billId,glcodeIdList);
	}
	public List<EgBilldetails> getAccountDetailsList(List<BigDecimal> glcodeIdList,Long billId){		
		return genericService.findAllByNamedQuery("AccountDetailsList",billId,glcodeIdList);
	}
	
	public List<EgBilldetails> getReleaseWithHoldAmountListforglcodes(List<BigDecimal> glcodeIdList,Long billId){		
		return genericService.findAllByNamedQuery("ReleaseWithHoldAmountList",billId,glcodeIdList);
	}
	/**
	 * API will returns the Net Payable Amount for netpayable code coaId
	 * @return  BigDecimal 
	 * @throws EGOVException 
	 * @throws NumberFormatException 
	 */
	public BigDecimal getNetPayableAmountForGlCodeId(Long billId) throws NumberFormatException, EGOVException{
		BigDecimal netPayableAmount=BigDecimal.ZERO;
		 List<CChartOfAccounts> coaPayableList = commonsService.getAccountCodeByPurpose(Integer.valueOf(worksService.getWorksConfigValue(WORKS_NETPAYABLE_CODE)));
		
		 for(CChartOfAccounts coa:coaPayableList){
			 List<EgBilldetails> egBillDetails=genericService.findAllBy("from EgBilldetails ebd where ebd.glcodeid=? and ebd.egBillregister.id=?", new BigDecimal(coa.getId()),billId);
			 if(!egBillDetails.isEmpty()){
					netPayableAmount=egBillDetails.get(0).getCreditamount();
				}	 
		 }
		return netPayableAmount;
	}
	
	/**
	 * API will returns the Total Amount for a deduction based on type and  workorder upto billdate 
	 * @return  BigDecimal 
	 */
	public BigDecimal getTotAmtForAdvanceAdjustment(Date billDate, Long workOrderId, Long workOrderEstimateId){
		BigDecimal totDeductionAmt=BigDecimal.ZERO;
		totDeductionAmt=getAdvanceAdjustmentDeductionTotAmount(billDate,workOrderId,workOrderEstimateId);
		return totDeductionAmt;
	}
	
	/**
	 * API will returns the Total Amount for advanceajustment  for workorder upto billdate 
	 * @return  BigDecimal 
	 */
	public BigDecimal getAdvanceAdjustmentDeductionTotAmount(Date billDate,Long workOrderId, Long workOrderEstimateId){
		logger.debug("1---getAdvanceAdjustmentDeductionTotAmount-----bill date "+billDate+"-----------workOrderId--"+workOrderId);
		BigDecimal advanceAdjustment=BigDecimal.ZERO;
		List<Long> billIdList=getBillIdListForWoUptoBillDate(billDate,workOrderId, workOrderEstimateId);
		
		if(billIdList==null || billIdList.isEmpty())
			billIdList.add(null);
		
		logger.debug("1.1---getAdvanceAdjustmentDeductionTotAmount-");
		String glCode="";
		List<AppConfigValues> appConfigValuesList=worksService.getAppConfigValue(EGF,CONTRACTOR_ADVANCE_CODE);
		if(appConfigValuesList!=null && !appConfigValuesList.isEmpty() && 
		  appConfigValuesList.get(0).getValue()!=null){
		   glCode=appConfigValuesList.get(0).getValue();
		}
		logger.debug("1.2---getAdvanceAdjustmentDeductionTotAmount-");
		Long coaId=null;
		List<CChartOfAccounts>  coaList=genericService.findAllBy("from CChartOfAccounts coa where coa.glcode=?",glCode);
		if(!coaList.isEmpty())
			coaId=coaList.get(0).getId();	
		
		logger.debug("1.3---getAdvanceAdjustmentDeductionTotAmount-");
		if(coaId==null)
			coaId=new Long("0");
			
		
		List<EgBilldetails> egBilldetailsList=genericService.findAllByNamedQuery("getAdvanceAjustementTotAmt",new BigDecimal(coaId),billIdList);
		
		logger.debug("1.4---getAdvanceAdjustmentDeductionTotAmount-");
		for(EgBilldetails egBilldetails:egBilldetailsList){
			if(egBilldetails.getCreditamount()!=null)
				advanceAdjustment=advanceAdjustment.add(egBilldetails.getCreditamount());
		}

		return advanceAdjustment;
	}
	
	/**
	 * API will returns the Total Amount for Statutory deduction for workorder upto billdate for that dedcution
	 * @return  BigDecimal 
	 */
	public BigDecimal getTotAmtForStatutory(Date billDate, Long workOrderId,StatutoryDeductionsForBill statDeductionBilldetail,Long workOrderEstimateId){
		BigDecimal totalStatutoryAmount=BigDecimal.ZERO;
		List<Long> billIdList=getBillIdListForWoUptoBillDate(billDate,workOrderId,workOrderEstimateId);
		
		List<StatutoryDeductionsForBill> egBillPayeedetailsList=genericService.findAllByNamedQuery("getStatutoryTotAmt",statDeductionBilldetail.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId(),billIdList);
		
		for(StatutoryDeductionsForBill egBillPayeedetails:egBillPayeedetailsList){
			if(egBillPayeedetails.getEgBillPayeeDtls().getCreditAmount()!=null)
				totalStatutoryAmount=totalStatutoryAmount.add(egBillPayeedetails.getEgBillPayeeDtls().getCreditAmount());
		}
		
		return totalStatutoryAmount;
	}
	

	/**
	 * API will returns the Total Amount for custom deduction for workorder upto billdate
	 * @return  BigDecimal 
	 */
	public BigDecimal getTotAmtForStandard(Date billDate,Long workOrderId,DeductionTypeForBill deductionTypeForBill1, Long workOrderEstimateId){
		BigDecimal totalStandarDeductionAmount=BigDecimal.ZERO;
		List<Long> billIdList=getBillIdListForWoUptoBillDate(billDate,workOrderId,workOrderEstimateId);
		//deductionTypeForBill1.getCoa().getId()
		List<DeductionTypeForBill> standardDeductionList=genericService.findAllByNamedQuery("getStandardTotAmt",deductionTypeForBill1.getCoa().getId(),billIdList);
		
		for(DeductionTypeForBill deductionTypeForBill:standardDeductionList){
			if(deductionTypeForBill.getCreditamount()!=null)
				totalStandarDeductionAmount=totalStandarDeductionAmount.add(deductionTypeForBill.getCreditamount());
		}
		
		return totalStandarDeductionAmount;
	}
	
	
	/**
	 * API will returns the Total Amount for custom deduction for workorder upto billdate
	 * @return  BigDecimal 
	 */
	public BigDecimal getTotAmtForCustom(Date billDate,Long workOrderId,EgBilldetails egBilldetails1,Long workOrderEstimateId){
		BigDecimal totalCustomDeductionAmount=BigDecimal.ZERO;
		List<Long> billIdList=getBillIdListForWoUptoBillDate(billDate,workOrderId,workOrderEstimateId);	
		//glcodeIdList
		List<EgBilldetails> customDeductionList=genericService.findAllByNamedQuery("getCustomDeductionTotAmt",egBilldetails1.getGlcodeid(),billIdList);	
		
		
		for(EgBilldetails egBilldetails:customDeductionList){
			if(egBilldetails.getCreditamount()!=null)
				totalCustomDeductionAmount=totalCustomDeductionAmount.add(egBilldetails.getCreditamount());
		}
		
		return totalCustomDeductionAmount;
	}
	
	/**
	 * API will returns the Total Released WithHeld Amount  for workorder upto billdate
	 * @return  BigDecimal 
	 */
	public BigDecimal getTotReleasedWHAmt(Date billDate,Long workOrderId,EgBilldetails egBilldetails1,Long workOrderEstimateId){
		BigDecimal totalReleaseWHAmount=BigDecimal.ZERO;
		List<Long> billIdList=getBillIdListForWoUptoBillDate(billDate,workOrderId,workOrderEstimateId);		 
		//glcodeIdList
		List<EgBilldetails> releaseWHList=genericService.findAllByNamedQuery("getReleaseWithHoldAmountTotalList",egBilldetails1.getGlcodeid(),billIdList);	
		
		
		for(EgBilldetails egBilldetails:releaseWHList){
			if(egBilldetails.getDebitamount()!=null)
				totalReleaseWHAmount=totalReleaseWHAmount.add(egBilldetails.getDebitamount());
		}
		
		return totalReleaseWHAmount;
	}
	
	
	/**
	 * API will returns the billId list for the workorder upto billdate 
	 * @return  BigDecimal 
	 */
	public List<Long> getBillIdListForWoUptoBillDate(Date billDate,Long workOrderId,Long workOrderEstimateId){
		List<Long> billIdList=new ArrayList<Long>();
		logger.debug("---inside getBillIdListForWoUptoBillDate----");
		/*List<EgBillregister> egBillregisterList=genericService.findAllBy("select distinct mbh.egBillregister from MBHeader mbh " +
					"where mbh.egBillregister.id in (select egBillRegister.id from org.egov.model.bills.EgBillregister egBillRegister " +
					" where egBillRegister.billdate <= ?) and mbh.workOrder.id = ?",billDate,workOrderId);*/
		
		List<EgBillregister> egBillregisterList=genericService.findAllBy("select distinct mbBills.egBillregister from MBHeader mbh left join mbh.mbBills mbBills " +
				"where mbBills.egBillregister.billdate <=? and mbh.workOrder.id=? and mbh.workOrderEstimate.id=?",billDate,workOrderId,workOrderEstimateId);
		if(!egBillregisterList.isEmpty()){	
			for(EgBillregister egBillregister:egBillregisterList){
			billIdList.add(egBillregister.getId());
			}
		}
		
		logger.debug("---atend getBillIdListForWoUptoBillDate ");
		return billIdList;
	}
	
	/**
	 * Get the list of eligible bills based on parameters provided
	 * @param paramsMap
	 * @return
	 */
	public List<ContractorBillRegister> searchContractorBill(Map<String, Object> paramsMap){
		List<ContractorBillRegister> ContractorBillList;
		String dynQuery = "select distinct cbr from ContractorBillRegister cbr where cbr.id != null and cbr.state.value!='NEW' " ;
		Object[] params;
		List<Object> paramList = new ArrayList<Object>();

		if(paramsMap.get(WORKORDER_NO) != null){
			dynQuery = dynQuery + " and cbr.workordernumber like '%"
								+ paramsMap.get(WORKORDER_NO)
								+ "%'";
		}
		if(paramsMap.get(CONTRACTOR_ID) != null && !"-1".equals(paramsMap.get(CONTRACTOR_ID))) {
			dynQuery = dynQuery + " and (cbr.id in (select mbBills.egBillregister.id from MBHeader mbh left join mbh.mbBills mbBills where mbBills.egBillregister.id=cbr.id and mbh.workOrder.contractor.id = ?)" + " OR cbr.id in (select mbcb.egBillregister.id from MBForCancelledBill mbcb where mbcb.egBillregister.id=cbr.id and mbcb.mbHeader.workOrder.contractor.id = ?))";
			paramList.add(paramsMap.get(CONTRACTOR_ID));
			paramList.add(paramsMap.get(CONTRACTOR_ID));
			
		}
		if(paramsMap.get(FROM_DATE) != null && paramsMap.get(TO_DATE)==null) {
			dynQuery = dynQuery + " and cbr.billdate >= ? ";
			paramList.add(paramsMap.get(FROM_DATE));

		}else if(paramsMap.get(TO_DATE) != null && paramsMap.get(FROM_DATE)==null) {
			dynQuery = dynQuery + " and cbr.billdate <= ? ";
			paramList.add(paramsMap.get(TO_DATE));
		}else if(paramsMap.get(FROM_DATE) != null && paramsMap.get(TO_DATE)!=null) {
			dynQuery = dynQuery + " and cbr.billdate between ? and ? ";
			paramList.add(paramsMap.get(FROM_DATE));
			paramList.add(paramsMap.get(TO_DATE));
		}
		if(paramsMap.get(BILLSTATUS) != null && !paramsMap.get(BILLSTATUS).equals("-1")){
			if(paramsMap.get(BILLSTATUS).equals(ContractorBillRegister.BillStatus.APPROVED.toString()) ||
					paramsMap.get(BILLSTATUS).equals(ContractorBillRegister.BillStatus.CANCELLED.toString()))
				dynQuery = dynQuery + " and cbr.state.previous.value=?";
			else dynQuery = dynQuery + " and cbr.state.value=?";
			paramList.add(paramsMap.get(BILLSTATUS));
			logger.info("Workflow for Bills is not implemented. So No condition is specified......");
		}
		if(paramsMap.get(BILLNO) != null){
			dynQuery = dynQuery + " and cbr.billnumber= ? ";
			paramList.add(paramsMap.get(BILLNO));
		}
		
		if(paramList.isEmpty())
			ContractorBillList 	= genericService.findAllBy(dynQuery);
		else{
			params 			= new Object[paramList.size()];
			params 			= paramList.toArray(params);
			ContractorBillList 	= genericService.findAllBy(dynQuery,params);
		}
		return ContractorBillList;
	}
	
	/**
	 * Get the list of custom dedcution based on glcodes of custom deduction
	 * @param ContractorBillRegister
	 * @return List
	 * @throws EGOVException 
	 * @throws NumberFormatException 
	 */
	public List<EgBilldetails> getCustomDeductionList(Long billId,List<StatutoryDeductionsForBill> statutoryList,List<DeductionTypeForBill> standardDeductionList, List<EgBilldetails> retentionMoneyDeductionList) throws NumberFormatException, EGOVException{
		List<BigDecimal> glcodeIdList=new ArrayList<BigDecimal>();		
		addStatutoryDeductionGlcode(glcodeIdList,statutoryList);
		addStandardDeductionGlcode(glcodeIdList,standardDeductionList);
		String advanceAdjstglCodeId=getAdvanceAdjustmentGlcode();
		addRetentionMoneyDeductionGlcode(glcodeIdList,retentionMoneyDeductionList);
		addGlCodeForNetPayable(glcodeIdList);
		glcodeIdList.add(new BigDecimal(advanceAdjstglCodeId));
		return getCustomDeductionListforglcodes(glcodeIdList,billId);
	}
	
	public List<EgBilldetails> getReleaseWithHoldAmountList(Long billId) throws NumberFormatException, EGOVException{
		List<BigDecimal> retentionGlcodeIdList=new ArrayList<BigDecimal>();		
		getAllRetentionMoneyGlcodeList(retentionGlcodeIdList);
		return getReleaseWithHoldAmountListforglcodes(retentionGlcodeIdList,billId);
	}
	
	public List<EgBilldetails> getRetentionMoneyDeductionList(Long billId,List<StatutoryDeductionsForBill> statutoryList,List<DeductionTypeForBill> standardDeductionList) throws NumberFormatException, EGOVException{
		List<BigDecimal> retentionGlcodeIdList=new ArrayList<BigDecimal>();		
		getAllRetentionMoneyGlcodeList(retentionGlcodeIdList);
		return getRetentionMoneyListforglcodes(retentionGlcodeIdList,billId);
	}
	
	public List<EgBilldetails> getAccountDetailsList(Long billId,List<StatutoryDeductionsForBill> statutoryList,List<DeductionTypeForBill> standardDeductionList, List<EgBilldetails> customDeductionList,List<EgBilldetails> retentionMoneyDeductionList,List<EgBilldetails> releaseWithHeldAmountDeductionsList ) throws NumberFormatException, EGOVException{
		List<BigDecimal> glcodeIdList=new ArrayList<BigDecimal>();		
		addStatutoryDeductionGlcode(glcodeIdList,statutoryList);
		addStandardDeductionGlcode(glcodeIdList,standardDeductionList);
		String advanceAdjstglCodeId=getAdvanceAdjustmentGlcode();
		addRetentionMoneyDeductionGlcode(glcodeIdList,retentionMoneyDeductionList);
		addReleaseWithHoldGlcode(glcodeIdList,releaseWithHeldAmountDeductionsList);
		addCustomDeductionGlcode(glcodeIdList,customDeductionList);
		addGlCodeForNetPayable(glcodeIdList);
		glcodeIdList.add(new BigDecimal(advanceAdjstglCodeId));
		return getAccountDetailsList(glcodeIdList,billId);
	}
	  
	 public void addStatutoryDeductionGlcode(List<BigDecimal> glcodeIdList,List<StatutoryDeductionsForBill> sortedStatutorySortedList){
			if(!sortedStatutorySortedList.isEmpty()){
				for(StatutoryDeductionsForBill bpd:sortedStatutorySortedList){
					if(bpd!=null && bpd.getEgBillPayeeDtls().getRecovery()!=null && bpd.getEgBillPayeeDtls().getRecovery().getId()!=null &&
							bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts()!=null && bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId()!=null){
						glcodeIdList.add(new BigDecimal(bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId()));
					}
			}
		}
	 }
	 
	 public void addStandardDeductionGlcode(List<BigDecimal> glcodeIdList,List<DeductionTypeForBill> sortedStandardDeductionList){
		if(!sortedStandardDeductionList.isEmpty()){
			for(DeductionTypeForBill deductionTypeForBill:sortedStandardDeductionList){
				if( deductionTypeForBill.getCoa()!=null && deductionTypeForBill.getCoa().getId()!=null){
					glcodeIdList.add(new BigDecimal(deductionTypeForBill.getCoa().getId()));
				}
			}
		}
	 }
	 
	 public void addRetentionMoneyDeductionGlcode(List<BigDecimal> glcodeIdList,List<EgBilldetails> retentionMoneyDeductionList){
			if(!retentionMoneyDeductionList.isEmpty()){
				for(EgBilldetails deductionTypeForBill:retentionMoneyDeductionList){
					if(deductionTypeForBill.getGlcodeid()!=null && deductionTypeForBill.getGlcodeid()!=null){
						glcodeIdList.add(deductionTypeForBill.getGlcodeid());
					}
				}
			}
		 }
	 
	 public void addReleaseWithHoldGlcode(List<BigDecimal> glcodeIdList,List<EgBilldetails> releaseWithHeldAmountDeductionsList){
			if(!releaseWithHeldAmountDeductionsList.isEmpty()){
				for(EgBilldetails deductionTypeForBill:releaseWithHeldAmountDeductionsList){
					if(deductionTypeForBill.getGlcodeid()!=null && deductionTypeForBill.getGlcodeid()!=null){
						glcodeIdList.add(deductionTypeForBill.getGlcodeid());
					}
				}
			}
		 }
	 
	 private void getAllRetentionMoneyGlcodeList(List<BigDecimal> retentionGlcodeIdList) throws EGOVException{
		 
		 if(StringUtils.isNotBlank(worksService.getWorksConfigValue(RETENTION_MONEY_PURPOSE))){
			 List<CChartOfAccounts> tempAllRetAccList = commonsService.getAccountCodeByPurpose(Integer.valueOf(worksService.getWorksConfigValue(RETENTION_MONEY_PURPOSE)));
			 for(CChartOfAccounts acc:tempAllRetAccList){
				 retentionGlcodeIdList.add(new BigDecimal(acc.getId()));
			 }
		 }
	 }
	 
	 public void addCustomDeductionGlcode(List<BigDecimal> glcodeIdList,List<EgBilldetails> customDeductionList){
			if(!customDeductionList.isEmpty()){
				for(EgBilldetails deductionTypeForBill:customDeductionList){
					if(deductionTypeForBill.getGlcodeid()!=null && deductionTypeForBill.getGlcodeid()!=null){
						glcodeIdList.add(deductionTypeForBill.getGlcodeid());
					}
				}
			}
		 }
	 
	 public void addGlCodeForNetPayable(List<BigDecimal> glcodeIdList) throws NumberFormatException, EGOVException{
		 List<CChartOfAccounts> coaPayableList = commonsService.getAccountCodeByPurpose(Integer.valueOf(worksService.getWorksConfigValue(WORKS_NETPAYABLE_CODE)));
		 if(coaPayableList!=null){
			for(CChartOfAccounts coa :coaPayableList){
				if(coa.getId()!=null){
					glcodeIdList.add(new BigDecimal(coa.getId()));
				}
			}	
		 }
	 }
	 public BigDecimal getNetPaybleCode(Long billId) throws Exception{
		 List<BigDecimal> glcodeIdList=new ArrayList<BigDecimal>();
		 BigDecimal netpaybleCode=BigDecimal.ZERO;
		 List<CChartOfAccounts> coaPayableList = commonsService.getAccountCodeByPurpose(Integer.valueOf(worksService.getWorksConfigValue(WORKS_NETPAYABLE_CODE)));
		 if(coaPayableList!=null){
			for(CChartOfAccounts coa :coaPayableList){
				if(coa.getId()!=null){
					glcodeIdList.add(new BigDecimal(coa.getId()));
				}
			}	
		 }
		EgBilldetailsDAO ebd = BillsDaoFactory.getDAOFactory().getEgBilldetailsDAO();
		EgBilldetails egbillDetails=ebd.getBillDetails(billId, glcodeIdList);
		if(egbillDetails!=null)
			netpaybleCode=egbillDetails.getGlcodeid();
		return netpaybleCode;
	 }
	 public String getAdvanceAdjustmentGlcode(){
		 String glCode="";
		 List<AppConfigValues> appConfigValuesList=worksService.getAppConfigValue(EGF,CONTRACTOR_ADVANCE_CODE);
			if(appConfigValuesList!=null && !appConfigValuesList.isEmpty() && 
					appConfigValuesList.get(0).getValue()!=null){
				glCode=appConfigValuesList.get(0).getValue();
			}
			List<CChartOfAccounts>  coaList=genericService.findAllBy("from CChartOfAccounts coa where coa.glcode=?",glCode);
			if(!coaList.isEmpty()){
				glCode=coaList.get(0).getId().toString();
			}
			return glCode;
	 }
	
	 public List<MBHeader>  getMbListForBillAndWorkordrId(Long workOrderId,Long billId){
	return	 genericService.findAllBy("select mbHeader from MBHeader mbHeader left join mbHeader.mbBills mbBills where mbHeader.workOrder.id=? and mbBills.egBillregister.id=?",workOrderId,billId);
	
	 }
	 
	 public List<MBForCancelledBill>  getMbListForCancelBill(Long billId){
		 List<MBForCancelledBill> list=genericService.findAllBy("from MBForCancelledBill mbcb where  mbcb.egBillregister.id=?",billId);
			return	list; 
			
			 }
	 
	 public void setAllViewLists(Long id,Long workOrderId,List<StatutoryDeductionsForBill> actionStatutorydetails,
			 List<DeductionTypeForBill> standardDeductions, List<EgBilldetails> customDeductions, List<EgBilldetails> retentionMoneyDeductions,
			 List<AssetForBill> accountDetailsForBill,List<EgBilldetails> releaseWithHeldAmountDeductions) 
	 throws NumberFormatException, EGOVException{
		 actionStatutorydetails.clear();
		actionStatutorydetails.addAll(getStatutoryListForBill(id));
		standardDeductions.clear();
		accountDetailsForBill.clear();
		for(DeductionTypeForBill deductionTypeForBill:getStandardDeductionForBill(id)){
			deductionTypeForBill.setGlcodeid(BigDecimal.valueOf(deductionTypeForBill.getCoa().getId())); 
			standardDeductions.add(deductionTypeForBill);
		}
		retentionMoneyDeductions.clear();
		retentionMoneyDeductions.addAll(getRetentionMoneyDeductionList(id, actionStatutorydetails, standardDeductions)); 
		releaseWithHeldAmountDeductions.clear();
		releaseWithHeldAmountDeductions.addAll(getReleaseWithHoldAmountList(id));
		customDeductions.clear();
		customDeductions.addAll(getCustomDeductionList(id, actionStatutorydetails, standardDeductions,retentionMoneyDeductions)); 
		List<EgBilldetails>	accountDetailsForassetandbill = getAccountDetailsList(id,actionStatutorydetails,standardDeductions,
				customDeductions,retentionMoneyDeductions,releaseWithHeldAmountDeductions);
		accountDetailsForBill.addAll(getAssetForBill(id));
		if(accountDetailsForBill.isEmpty()){
			for(EgBilldetails egBilldetails:accountDetailsForassetandbill){
				CChartOfAccounts coa = commonsService.getCChartOfAccountsById(egBilldetails.getGlcodeid().longValue());
				if(coa!=null){
					coa.setId(egBilldetails.getGlcodeid().longValue());
					AssetForBill assetforBill=new AssetForBill();
					assetforBill.setCoa(coa);
					assetforBill.setDescription(coa.getName());
					assetforBill.setAmount(egBilldetails.getDebitamount());
					if(!accountDetailsForBill.contains(assetforBill))
						accountDetailsForBill.add(assetforBill);
				}
			}
		}
	}
	
	 public List<EgChecklists> getEgcheckList(Long billId) 
		throws NumberFormatException, EGOVException{
		return checklistService.findAllBy("from EgChecklists egChecklists  where egChecklists.objectid=?",billId);
	}
	 
	 public WorkCompletionInfo setWorkCompletionInfoFromBill(ContractorBillRegister contractorBillRegister,WorkOrderEstimate workOrderEstimate){
		 WorkCompletionInfo workCompletionInfo=null;
		 String mbNumbers="";
				 
		 List<String> mbNumberList=(List<String>)genericService.findAllByNamedQuery(WorksConstants.QUERY_GETALLMBNOSBYWORKORDERESTIMATE, workOrderEstimate.getId());
		 for(String mbNumber:mbNumberList){
			 mbNumbers=mbNumbers.concat(mbNumber).concat(",");
		 }
		 int strLen = mbNumbers.length();
		 if(strLen>0){
			 mbNumbers = mbNumbers.substring(0, strLen - 1);
		 }
		 String workCommenced="";
		 List<AppConfigValues> appConfigValuesList=worksService.getAppConfigValue(WorksConstants.WORKS,WorksConstants.WORKORDER_LASTSTATUS);
			if(appConfigValuesList!=null && !appConfigValuesList.isEmpty() && 
					appConfigValuesList.get(0).getValue()!=null){
				workCommenced=appConfigValuesList.get(0).getValue();
			}
		 Date workCommencedDate=null;
		 SetStatus woStatus=(SetStatus)genericService.findByNamedQuery(WorksConstants.QUERY_GETSTATUSDATEBYOBJECTID_TYPE_DESC, workOrderEstimate.getWorkOrder().getId(),WorkOrder.class.getSimpleName(),workCommenced);
		 if(woStatus!=null){
			 workCommencedDate=woStatus.getStatusDate();
		 }
		 
		 List<State> history=null;
			if(contractorBillRegister!=null && contractorBillRegister.getCurrentState()!=null && contractorBillRegister.getCurrentState().getHistory()!=null)
			history = contractorBillRegister.getCurrentState().getHistory();
		 
		 workCompletionInfo=new WorkCompletionInfo(workOrderEstimate,mbNumbers);
		 workCompletionInfo.setWorkCommencedOn(workCommencedDate);
		 workCompletionInfo.setWorkflowHistory(history);
		 return workCompletionInfo;
	 }
	 
	 public List<WorkCompletionDetailInfo> setWorkCompletionDetailInfoList(WorkOrderEstimate workOrderEstimate){
		 List<WorkCompletionDetailInfo> workCompletionDetailInfoList=new ArrayList<WorkCompletionDetailInfo>();
		// TenderResponse tenderResponse = tenderResponseService.find("from TenderResponse tr where tr.negotiationNumber=?",workOrderEstimate.getWorkOrder().getNegotiationNumber());
		 GenericTenderResponse tenderResponse = genericTenderResponseService.find("from org.egov.tender.model.GenericTenderResponse tr where tr.number=?",workOrderEstimate.getWorkOrder().getNegotiationNumber());
		 String rebatePremLevel = worksService.getWorksConfigValue("REBATE_PREMIUM_LEVEL");

		 List<Object[]> workOrderActivityIdList=(List<Object[]>)genericService.findAllByNamedQuery(WorksConstants.QUERY_GETALLWORKORDERACTIVITYWITHMB, workOrderEstimate.getId());
			for(Object[] object:workOrderActivityIdList){
				WorkOrderActivity woa=(WorkOrderActivity)genericService.find("from WorkOrderActivity woa where woa.id="+object[0].toString());
				WorkCompletionDetailInfo workCompletionDetailInfo = new WorkCompletionDetailInfo(woa,Double.parseDouble(object[1].toString()));
				double executionRate=0.0;
				List<String> tenderTypeList=worksService.getTendertypeList();
				if (tenderTypeList!=null && !tenderTypeList.isEmpty()
					&& tenderResponse!=null && tenderResponse.getBidType().name()
							.equals(tenderTypeList.get(0))
					&& rebatePremLevel.equalsIgnoreCase(WorksConstants.BILL)) {
					double rebpremRate=woa.getApprovedRate()*(Math.abs(tenderResponse.getPercentage().doubleValue())/100);
					if(tenderResponse.getPercentage().doubleValue()>0){
						executionRate=woa.getApprovedRate()+rebpremRate;
					}
					else{
						executionRate=woa.getApprovedRate()-rebpremRate;
					}
				}
				else{
					executionRate=woa.getApprovedRate();
				}
				workCompletionDetailInfo.setExecutionRate(executionRate);
				
				if(woa.getActivity().getSchedule()==null){
					workCompletionDetailInfo.setTenderAmount(woa.getActivity().getQuantity()*woa.getActivity().getRate().getValue());
					workCompletionDetailInfo.setExecutionAmount(executionRate*(Double.parseDouble(object[1].toString())));
				}
				else{
					
					Map<String, Integer> exceptionaSorMap = getSpecialUoms();
					double result=1;
			  		if(exceptionaSorMap.containsKey(woa.getActivity().getUom().getUom())){
			  			 result = exceptionaSorMap.get(woa.getActivity().getUom().getUom());
			  		}
			  		workCompletionDetailInfo.setTenderAmount((woa.getActivity().getQuantity()*woa.getActivity().getSORCurrentRate().getValue())/result);
					workCompletionDetailInfo.setExecutionAmount((executionRate*(Double.parseDouble(object[1].toString())))/result);
			
				}
				workCompletionDetailInfoList.add(workCompletionDetailInfo);
			 }
			 List<WorkOrderActivity> workOrderActivityWithoutMBList=(List<WorkOrderActivity>)genericService.
			 		findAllByNamedQuery(WorksConstants.QUERY_GETALLWORKORDERACTIVITYWITHOUTMB, workOrderEstimate.getId(),workOrderEstimate.getId());
			 for(WorkOrderActivity woa:workOrderActivityWithoutMBList){
				WorkCompletionDetailInfo workCompletionDetailInfo = new WorkCompletionDetailInfo(woa,Double.parseDouble("0"));
				workCompletionDetailInfo.setTenderAmount(woa.getApprovedRate()*woa.getApprovedQuantity());
				workCompletionDetailInfo.setExecutionAmount(Double.parseDouble("0"));
				workCompletionDetailInfoList.add(workCompletionDetailInfo);
			 }
			
		 return workCompletionDetailInfoList;
	 }
	 
	 public void getDeductionsList() throws EGOVException
	 {
		List<String> requiredStatutoryList=null;
		requiredStatutoryList=getSortedDeductionsFromConfig("StatutoryDeductionKey");
		List<StatutoryDeductionsForBill> currentStatutoryList=getStatutoryListForBill(egBillRegister.getId());
		sortedStatutorySortedList=getStatutoryDeductionSortedOrder(requiredStatutoryList, currentStatutoryList);
		List<AppConfigValues> appConfigValuesList=worksService.getAppConfigValue("EGF","CONTRACTOR_ADVANCE_CODE");
			if(appConfigValuesList!=null && !appConfigValuesList.isEmpty() && appConfigValuesList.get(0).getValue()!=null){
				advanceAdjustment=getAdvanceAdjustmentAmountForBill(egBillRegister.getId());
			}
		 List<String> requiredStandardList=getSortedDeductionsFromConfig("StandardDeductionKey");
		 getStandardDeductionList(egBillRegister.getId(),requiredStandardList);
		 getCustomDeductionList(egBillRegister);
	 }
	 
	 public WorkCompletionInfo setWorkContractCertInfoFromBill(ContractorBillRegister contractorBillRegister,WorkOrderEstimate workOrderEstimate){
		 egBillRegister=contractorBillRegister;
		 woEstimate=workOrderEstimate;
	 
		 WorkCompletionInfo workCompletionInfo=null;
	 	 try 
		 {
			getDeductionsList();
		 }
		 catch (EGOVException e) {
			e.printStackTrace();
		 }
		
		 List<Map<String,Object>> mbHeaderReportMapList = getMBHeaderList(contractorBillRegister);
		 List<Map<String, Object>> deductionMapList = getDeductions(contractorBillRegister);
		
		 BigDecimal netPayAmount = netPayableAmount;
		 BigDecimal totalBillAmtUptBill=getTotalValueWoForUptoBillDateWithoutCancel(egBillRegister.getBilldate(), workOrderEstimate.getWorkOrder().getId(), workOrderEstimate.getId(),egBillRegister.getId());

		 workCompletionInfo=new WorkCompletionInfo(contractorBillRegister,workOrderEstimate);
		 workCompletionInfo.setTotalWorkValue(totalBillAmtUptBill);
		 workCompletionInfo.setNetPayAmount(netPayAmount);
		 
		 List<Long> billList = genericService.findAllByNamedQuery("getAllBills", workOrderEstimate.getId());
		 		 
		 if(contractorBillRegister.getPartbillNo()>1)
		 {
			 for(int j=0;j<billList.size();j++)
			 {
				 if(contractorBillRegister.getId()>billList.get(j))
				 {
					 ContractorBillRegister contractorBillRegObj = persistenceService.find("Select egbr from ContractorBillRegister egbr where egbr.id = ? ", billList.get(j));
					 if(contractorBillRegObj.getStatus().getCode().equalsIgnoreCase("Approved"))
					 {
						 
						 String prevCCNumber = contractorBillRegObj.getContractCertificateNumber();
						 Date prevBillDate= contractorBillRegObj.getBilldate();
						 workCompletionInfo.setPrevBilldate(prevBillDate);
						 workCompletionInfo.setPrevCCNumber(prevCCNumber);
						 break;
					 }
				 }
			 }
		 } 
		 
		workCompletionInfo.setMbHeaderMapList(mbHeaderReportMapList);
		workCompletionInfo.setDeductionsMapList(deductionMapList);
		return workCompletionInfo;
	 }
	 
	 public List<Map<String, Object>> getMBHeaderList(ContractorBillRegister contractorBillRegister)
	 {
		 Map<String,Object>  reportMap;
		 List<Map<String,Object>> mbDetailsMapList = new ArrayList<Map<String,Object>>();
		 
		 List<MBHeader> mbHeaderList=(List<MBHeader>)genericService.findAllByNamedQuery("getAllMBsForBillId",contractorBillRegister.getId());
		 for(MBHeader mbh : mbHeaderList)
		 {
			 reportMap = new HashMap<String, Object>();
			 reportMap.put("mbDate", DateUtils.getFormattedDate(mbh.getMbDate(),"dd/MM/yyyy"));
			 reportMap.put("fromPage",mbh.getFromPageNo());
			 reportMap.put("toPage",mbh.getToPageNo());
			 reportMap.put("mbNo",mbh.getMbRefNo());
			 mbDetailsMapList.add(reportMap);
		 }
		 return mbDetailsMapList;
		 
	 }
	 
	 public List<Map<String, Object>> getDeductions(ContractorBillRegister contractorBillRegister)
	 {
		 Map<String,Object>  dedReportMap;
		 Integer i = 0;
		 List<Map<String,Object>> deductionsReportMapList = new ArrayList<Map<String,Object>>();
		 List<Map<String, Object>> deductionMapList = getDeductionsListData(contractorBillRegister,sortedStatutorySortedList, sortedStandardDeductionList,customDeductionList);
		 for(Map<String, Object> listMap: deductionMapList)
		 {
			 dedReportMap = new HashMap<String, Object>();
			 dedReportMap.put("slNo", i+1);
			 dedReportMap.put("glCode", listMap.get("glcode"));
			 dedReportMap.put("pctg", listMap.get("percentage"));
			 dedReportMap.put("remarks",listMap.get("remarks"));
			 dedReportMap.put("amount", listMap.get("amount"));
			 deductionsReportMapList.add(dedReportMap);
			 i++;
		 } 
		 return deductionsReportMapList;
	 }
	 
	 protected BigDecimal getPercentage(BigDecimal creditAmount){
		 Double amount = creditAmount.doubleValue();
		 BigDecimal billAmount = egBillRegister.getBillamount();
		 Double totalBillAmount = billAmount.doubleValue();
		 Double percent = (amount * 100.0)/totalBillAmount;
		 BigDecimal percentage = new BigDecimal(percent);
		 return percentage;
	 }
	 
	 public List<WorkCompletionDetailInfo> setWorkContractCertDetailInfoList(ContractorBillRegister contractorBillRegister,WorkOrderEstimate workOrderEstimate){
		 DecimalFormat df = new DecimalFormat("###.###");
		 List<WorkCompletionDetailInfo> workCompletionDetailInfoList=new ArrayList<WorkCompletionDetailInfo>();
		 //TenderResponse tenderResponse = tenderResponseService.find("from TenderResponse tr where tr.negotiationNumber=?",workOrderEstimate.getWorkOrder().getNegotiationNumber());
		 GenericTenderResponse tenderResponse = genericTenderResponseService.find("from org.egov.tender.model.GenericTenderResponse tr where tr.number=?",workOrderEstimate.getWorkOrder().getNegotiationNumber());
		 String rebatePremLevel = worksService.getWorksConfigValue("REBATE_PREMIUM_LEVEL");
		List<MBHeader> mbHeaderList = null;
		List<MBHeader> mbHeaderListTillDate = null;
		
		mbHeaderListTillDate=(List<MBHeader>)genericService.findAllByNamedQuery("getAllMBHeadersbyBillIdTillDate", workOrderEstimate.getId(),contractorBillRegister.getId());
		mbHeaderList=(List<MBHeader>)genericService.findAllByNamedQuery(WorksConstants.QUERY_GETALLMBHEADERSBYBILLID, contractorBillRegister.getId());

		 List<Object[]> workOrderActivityIdList=(List<Object[]>)genericService.findAllByNamedQuery(WorksConstants.QUERY_GETALLWORKORDERACTIVITYWITHMB, workOrderEstimate.getId());
		 
		 for(Object[] object:workOrderActivityIdList){
				WorkOrderActivity woa=(WorkOrderActivity)genericService.find("from WorkOrderActivity woa where woa.id="+object[0].toString());
				double executionQuantity=0;
				for(MBHeader mbh:mbHeaderListTillDate)
				{
					for(MBDetails mbdetails : mbh.getMbDetails())
					{
						if(woa.getId().equals(mbdetails.getWorkOrderActivity().getId()))
						{
							executionQuantity=executionQuantity+mbdetails.getQuantity();
						}
					}
				}
				WorkCompletionDetailInfo workCompletionDetailInfo = new WorkCompletionDetailInfo(woa,executionQuantity);
				double executionRate=0.0;
				List<String> tenderTypeList=worksService.getTendertypeList();
				if (tenderTypeList!=null && !tenderTypeList.isEmpty() &&
						tenderResponse!=null && tenderResponse.getBidType().name()
							.equals(tenderTypeList.get(0))
					&& rebatePremLevel.equalsIgnoreCase(WorksConstants.BILL)) {
					double rebpremRate=woa.getApprovedRate()*(Math.abs(tenderResponse.getPercentage().doubleValue())/100);
					if(tenderResponse.getPercentage().doubleValue()>0){
						executionRate=woa.getApprovedRate()+rebpremRate;
					}
					else{
						executionRate=woa.getApprovedRate()-rebpremRate;
					}
				}
				else{
					executionRate=woa.getApprovedRate();
				}
				workCompletionDetailInfo.setExecutionRate(new Double(df.format(executionRate)));
				Map<String, Integer> exceptionaSorMap = getSpecialUoms();
				double result=1;
		  		if(exceptionaSorMap.containsKey(woa.getActivity().getUom().getUom())){
		  			 result = exceptionaSorMap.get(woa.getActivity().getUom().getUom());
		  		}
				if(woa.getActivity().getSchedule()==null){
					workCompletionDetailInfo.setTenderAmount(woa.getActivity().getQuantity()*woa.getActivity().getRate().getValue());
					workCompletionDetailInfo.setExecutionAmount(executionRate*executionQuantity);
				}
				else{
					
					workCompletionDetailInfo.setTenderAmount((woa.getActivity().getQuantity()*woa.getActivity().getSORCurrentRate().getValue())/result);
			  		workCompletionDetailInfo.setExecutionAmount((executionRate*executionQuantity)/result);
			  						
				}
				
				double lastExecutionAmount=0;
				double lastExecutionQuantity=0;
				for(MBHeader mbh:mbHeaderList){
					for(MBDetails mbd:mbh.getMbDetails()){
						if(woa.getId().equals(mbd.getWorkOrderActivity().getId())){
							lastExecutionQuantity=lastExecutionQuantity+mbd.getQuantity();
							double amount=0;
							if(woa.getActivity().getSchedule()==null){
								amount=executionRate*mbd.getQuantity();
							}
							else{
								amount=(executionRate*mbd.getQuantity())/result;
							}
							lastExecutionAmount=lastExecutionAmount+amount;
						}
					}
				}
				workCompletionDetailInfo.setLastExecutionAmount(lastExecutionAmount);
				workCompletionDetailInfo.setLastExecutionQuantity(lastExecutionQuantity);
				workCompletionDetailInfoList.add(workCompletionDetailInfo);
			 }
		 
			 List<WorkOrderActivity> workOrderActivityWithoutMBList=(List<WorkOrderActivity>)genericService.
			 		findAllByNamedQuery(WorksConstants.QUERY_GETALLWORKORDERACTIVITYWITHOUTMB, workOrderEstimate.getId(),workOrderEstimate.getId());
			 for(WorkOrderActivity woa:workOrderActivityWithoutMBList){
				WorkCompletionDetailInfo workCompletionDetailInfo = new WorkCompletionDetailInfo(woa,Double.parseDouble("0"));
				workCompletionDetailInfo.setTenderAmount(woa.getApprovedRate()*woa.getApprovedQuantity());
				workCompletionDetailInfo.setExecutionAmount(Double.parseDouble("0"));
				workCompletionDetailInfoList.add(workCompletionDetailInfo);
			 }
			
		 return workCompletionDetailInfoList;
	 }
	 
	 public BigDecimal getTotalExecutionQuantity(){
		 BigDecimal totalExecutionQuantity=null;
		 
		 return totalExecutionQuantity;
	 }
	 
	 public List<CapitaliseAsset> getAssetsForCapitalisationList(Map<String, Object> paramsMap){
		 Object[] params;
		List<Object> paramList = new ArrayList<Object>();
		String query="select ab1.asset.id,sum(ab1.amount) from AssetForBill ab1 where ab1.egbill.billtype=:param_0 and ab1.egbill.status.id=:param_1 and (ab1.egbill.egBillregistermis.voucherHeader is not null and ab1.egbill.egBillregistermis.voucherHeader.status=0)"+
			" and (select count(ab2.id) from AssetForBill ab2 where ab1.asset.id=ab2.asset.id and ab1.egbill.billtype=ab2.egbill.billtype and ab1.workOrderEstimate.id=ab2.workOrderEstimate.id) "+ 
			" =(select count(aw1.id) from AssetsForWorkOrder aw1 where aw1.asset.id=ab1.asset.id and aw1.workOrderEstimate.id=ab1.workOrderEstimate.id) "+ 
			" and (select count(aw2.id) from AssetsForWorkOrder aw2 where aw2.asset.id=ab1.asset.id and aw2.workOrderEstimate.id=ab1.workOrderEstimate.id) "+ 
			" =(select count(ae1.id) from AssetsForEstimate ae1 where ae1.asset.id=ab1.asset.id and ae1.abstractEstimate.id=ab1.workOrderEstimate.estimate.id) "+ 
			" and (select count(ae2.id) from AssetsForEstimate ae2 where ae2.asset.id=ab1.asset.id) "+ 
			" =(select count(ab3.id) from AssetForBill ab3 where ab3.egbill.billtype=:param_2  and ab3.egbill.status.id=:param_3 and (ab1.egbill.egBillregistermis.voucherHeader is not null and ab1.egbill.egBillregistermis.voucherHeader.status=0) and ab3.asset.id=ab1.asset.id and "+ 
			" ab3.workOrderEstimate.estimate.id in(select ae3.abstractEstimate.id from AssetsForEstimate ae3 where ae3.asset.id=ab1.asset.id)) "+ 
			" and ab1.workOrderEstimate.estimate.type.name  in (:param_4) ";
		 String natureOfWorkConfigValue = null;
		 if(Integer.valueOf(paramsMap.get("projectType").toString()).equals(Integer.valueOf(0))){
			 natureOfWorkConfigValue=EGovConfig.getAppConfigValue(WorksConstants.WORKS, WorksConstants.NATUREOFWORKFORASSETCAPITALISATION, WorksConstants.NATUREOFWORKFORASSETCAPITALISATION_DEFAULTVALUE);
		 }else if(Integer.valueOf(paramsMap.get("projectType").toString()).equals(Integer.valueOf(1))){
			 natureOfWorkConfigValue=EGovConfig.getAppConfigValue(WorksConstants.WORKS, WorksConstants.NATUREOFWORKFORASSETIMPROVMENT, WorksConstants.NATUREOFWORKFORASSETIMPROVMENT_DEFAULTVALUE);
		 }else if(Integer.valueOf(paramsMap.get("projectType").toString()).equals(Integer.valueOf(2))){
			 natureOfWorkConfigValue=EGovConfig.getAppConfigValue(WorksConstants.WORKS, WorksConstants.NATUREOFWORKFORASSETREPAIRANDMAINTAINANCE, WorksConstants.NATUREOFWORKFORASSETREPAIRANDMAINTAINANCE_DEFAULTVALUE);
		 }
		
		 List<String> natureOfWorkList=Arrays.asList(natureOfWorkConfigValue.split(","));
		 String billAprvdStsQry = "from EgwStatus  where moduletype='CONTRACTORBILL' and description='Approved'";
		 Session session = HibernateUtil.getCurrentSession();
		 EgwStatus billStatus  = (EgwStatus)session.createQuery(billAprvdStsQry).uniqueResult();
		 paramList.add(getFinalBillTypeConfigValue());
		 paramList.add(billStatus.getId());
		 paramList.add(getFinalBillTypeConfigValue());
		 paramList.add(billStatus.getId());
		 paramList.add(natureOfWorkList);
		 
		 if(paramsMap.get("parentId") != null && !"-1".equals(paramsMap.get("parentId"))){
			 Long parentId=Long.valueOf(paramsMap.get("parentId").toString());
			 query=query+" and ab1.asset.assetCategory.id in ("+ getAllChilds(parentId)+  ")";
		 }
		 if(paramsMap.get("catTypeId")!=null){
			 query=query+" and ab1.asset.assetCategory.assetType.id = "+ (Long)paramsMap.get("catTypeId");
		 }
		 if(paramsMap.get("departmentId")!=null){
			 query=query+" and ab1.asset.department.id = "+ (Integer)paramsMap.get("departmentId");
		 }
		 if(paramsMap.get("zoneId")!=null && (Integer)paramsMap.get("zoneId") !=-1 && paramsMap.get("locationId") == null){
			 query=query+" and ab1.asset.ward.parent.id = "+(Integer)paramsMap.get("zoneId");
	     }
		 if(paramsMap.get("locationId")!=null){
			query=query+" and ab1.asset.ward.id = "+(Integer)paramsMap.get("locationId");
		 }
		 if(paramsMap.get("code")!=null && !paramsMap.get("code").toString().trim().equalsIgnoreCase("")){
			query=query+" and UPPER(ab1.asset.code) like '%" + paramsMap.get("code").toString().toUpperCase()+ "%'";
		 }
		 if(paramsMap.get("description")!=null && !paramsMap.get("description").toString().trim().equalsIgnoreCase("")){
		 	query=query+" and UPPER(ab1.asset.description) like '%" + paramsMap.get("description").toString().toUpperCase()+ "%'";
		 }
		 if(paramsMap.get("statusId")!=null){
			 List statusIdList=(List)paramsMap.get("statusId");
			 if(!statusIdList.isEmpty()){
				 query=query+" and ab1.asset.status.id in ("; 
				 for(int i=0,len=statusIdList.size(); i<len;i++){
					 query=query+ statusIdList.get(i);
					 if(i<len-1){
						 query=query+',';
					 }
				 }			 
				 query=query+')';	
			 }
		 }
		 if(paramsMap.get(FROMDATE)!=null && paramsMap.get(TODATE)==null){
			 query=query+" and ab1.asset.dateOfCreation >=:fromDate";
		 }
		 if(paramsMap.get(TODATE)!=null && paramsMap.get(FROMDATE)==null){
			 query=query+" and ab1.asset.dateOfCreation <= :toDate";
		 }
        if(paramsMap.get(FROMDATE)!=null && paramsMap.get(TODATE)!=null){
        	query = query + " and ab1.asset.dateOfCreation between :fromDate and :toDate";
		 }
		 query+=" group by ab1.asset.id";
		 params 			= new Object[paramList.size()];
		 params 			= paramList.toArray(params);
			
		 Query q = HibernateUtil.getCurrentSession().createQuery(query);
		 int index = 0;
		 for (Object param : params) {
		 if(param instanceof Collection)
		 q.setParameterList(String.valueOf(PARAM+index), (Collection)param);
		 else
		 q.setParameter(String.valueOf(PARAM+index), param);
		 index++;
		 }
		 if(paramsMap.get(FROMDATE)!=null){
			 q.setParameter(FROMDATE, paramsMap.get(FROMDATE));
		 }
		 if(paramsMap.get(TODATE)!=null ){
			 q.setParameter(TODATE, paramsMap.get(TODATE));
		 }
		 List<Object[]> assetsList=(List<Object[]>)q.list();
		 List<CapitaliseAsset> assetList = new LinkedList<CapitaliseAsset>();
		 for(Object[] object:assetsList){
			 Asset asset = commonAssetsService.getAssetById(Long.valueOf(object[0].toString()));
			 //object[0]=asset;
			 //assetsForCapitalisationList.add(object);
			 CapitaliseAsset ca = new CapitaliseAsset();
			 ca.setAsset(asset);
			 ca.setCapitalisationValue(new BigDecimal(object[1].toString()));
			 assetList.add(ca);
		 }
		 return assetList;
	 }
	 private String getAllChilds(Long parentId){
		 StringBuffer assetCatIdStr = new StringBuffer(100);
		 Query query = HibernateUtil.getCurrentSession().getNamedQuery("ParentChildCategories");
	     query.setParameter("assetcatId",parentId);
	     List assetChildCategoryList=query.list();
	     
	     for(int i=0,len=assetChildCategoryList.size(); i<len;i++){
	    	 assetCatIdStr.append(assetChildCategoryList.get(i).toString());
			 if(i<len-1)
				 assetCatIdStr.append(',');
		}
	     return assetCatIdStr.toString();
		}
	 
	 public void capitaliseAsset(Asset asset, Date dateOfCapitalisation,BigDecimal assetIndrctExpns,Integer projectType){
		 List<VoucherInput> voucherInputList = null;
		 try{	
				voucherInputList = getVoucherInputList(asset,assetIndrctExpns);
				assetService.capitaliseAsset(asset,voucherInputList,dateOfCapitalisation,assetIndrctExpns,projectType);
		} catch (EGOVRuntimeException e) {
			logger.error(e.getMessage());
			throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
		} catch (ValidationException e) {
			logger.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
		}
	 }
	 
	 private List<VoucherInput> getVoucherInputList(Asset asset,BigDecimal assetIndrctExpns){
		 List<Object> paramList = new ArrayList<Object>();
		 Object[] params;
		 String query="select ab.coa.glcode,sum(ab.amount),ab.egbill.egBillregistermis.fund.id, " +
			" ab.egbill.egBillregistermis.fieldid.id,ab.egbill.egBillregistermis.egDepartment.id," +
			" ab.egbill.egBillregistermis.scheme.id,ab.egbill.egBillregistermis.subScheme.id," +
			" ab.egbill.egBillregistermis.fundsource.id, bd.functionid,bdp.accountDetailTypeId,bdp.accountDetailKeyId,bdp.debitAmount " +
			" from AssetForBill as ab, EgBilldetails bd,EgBillPayeedetails bdp " +
			" where ab.asset.id=:param_0 and ab.egbill.id = bd.egBillregister.id and bdp.egBilldetailsId.id=bd.id and " +
			" bd.debitamount>0 and ab.coa.id = bd.glcodeid " +
			" group by ab.egbill.egBillregistermis.fund.id,ab.egbill.egBillregistermis.fieldid.id," +
			" ab.egbill.egBillregistermis.egDepartment.id,ab.egbill.egBillregistermis.scheme.id," +
			" ab.egbill.egBillregistermis.subScheme.id, " +
			" ab.egbill.egBillregistermis.fundsource.id, ab.coa.glcode, bd.functionid ,bdp.accountDetailTypeId,bdp.accountDetailKeyId,bdp.debitAmount";

		 paramList.add(asset.getId());
		 params = new Object[paramList.size()];
		 params = paramList.toArray(params);
		 Query q = HibernateUtil.getCurrentSession().createQuery(query);
		 int index = 0;
		 for (Object param : params) {
			 if(param instanceof Collection)
			 q.setParameterList(String.valueOf(PARAM+index), (Collection)param);
			 else
			 q.setParameter(String.valueOf(PARAM+index), param);
			 index++;
		 }
			
		 List<Object[]> resultList=(List<Object[]>)q.list();
		 
		 LinkedHashMap<String,VoucherInput> resultMap = new LinkedHashMap<String,VoucherInput>();
		 boolean assetIndrctExpnsVoucher =true;// added to track  asset indirect expenses
		 for(Object[] object:resultList){
			 String key = (object[2]==null?"null":object[2].toString()) + ":" + (object[3]==null?"null":object[3].toString()) + 
			 				":" + (object[4]==null?"null":object[4].toString()) + 
			 				":" + (object[5]==null?"null":object[5].toString()) + ":" + (object[6]==null?"null":object[6].toString()) + 
							":" + (object[7]==null?"null":object[7].toString()) + ":" + (object[8]==null?"null":object[8].toString());
			 if(resultMap.containsKey(key)){
				 VoucherInput voucherInput = resultMap.get(key);
				 List<AccountInfo> creaditAccList = voucherInput.getCreaditAccounts();
				 AccountInfo accInfo = new AccountInfo();
				 accInfo.setGlcode((String)object[0]);
				 accInfo.setCreditAmount(object[1].toString());
				 creaditAccList.add(accInfo);
			 }
			 else{
				 VoucherInput voucherInput = new VoucherInput();
				 HeaderInfo vHeader = new HeaderInfo();
				 if(object[2]!=null){
				 Fund fund = (Fund)genericService.find("from Fund where id=?",object[2]);
				 vHeader.setFund(fund.getCode());
				 }if(object[3]!=null){
				 BoundaryImpl field = (BoundaryImpl)genericService.find("from BoundaryImpl where id=?",object[3]);
				 vHeader.setDivision(field.getId());
				 }if(object[4]!=null){
				 DepartmentImpl dept = (DepartmentImpl)genericService.find("from DepartmentImpl where id=?",object[4]);
				 vHeader.setDepartment(dept.getDeptCode());
				 }if(object[5]!=null){
				 Scheme scheme = (Scheme)genericService.find("from Scheme where id=?",object[5]);
				 vHeader.setScheme(scheme.getCode());
				 }if(object[6]!=null){
				 SubScheme subScheme = (SubScheme)genericService.find("from SubScheme where id=?",object[6]);
				 vHeader.setSubScheme(subScheme.getCode());
				 }if(object[7]!=null){
				 Fundsource fundsource = (Fundsource)genericService.find("from Fundsource where id=?",object[7]);
				 vHeader.setFundSource(fundsource.getCode());
				 }
				 voucherInput.setHeaderInfo(vHeader);
				// added to track  asset indirect expenses
				 if(assetIndrctExpnsVoucher && !assetIndrctExpns.equals(BigDecimal.ZERO)) { 
					 VoucherInput vi = new VoucherInput();	 
					 vi.setNarration("Asset Indirect Expense");
					 vi.setHeaderInfo(vHeader);
					 List<AccountInfo> creaditAccList = new ArrayList<AccountInfo>();
					 AccountInfo accInfo = new AccountInfo();
					 accInfo.setGlcode((String)object[0]);
					 accInfo.setCreditAmount(assetIndrctExpns.toString());
					 if(object[8]!=null){
					 CFunction function = (CFunction)genericService.find("from CFunction where id=?",((BigDecimal)object[8]).longValue());
					 accInfo.setFunction(function.getCode());
					 
					 }
					 creaditAccList.add(accInfo);
					 vi.setCreaditAccounts(creaditAccList);
					 if(null != object[11]){
						 SubledgerInfo subledgerInfo = new SubledgerInfo();
						 subledgerInfo.setGlcode((String)object[0]);
						 subledgerInfo.setDetailAmount(assetIndrctExpns.toString());
						 subledgerInfo.setDetailType(object[9].toString());
						 subledgerInfo.setDetailKey(object[10].toString());
						 vi.setSubledgerInfo(subledgerInfo);
					 }
					 resultMap.put("assetIndrctExpns", vi);
					 assetIndrctExpnsVoucher = false;
				 } // End - added to track  asset indirect expenses
				 List<AccountInfo> creaditAccList = new ArrayList<AccountInfo>();
				 AccountInfo accInfo = new AccountInfo();
				 accInfo.setGlcode((String)object[0]);
				 accInfo.setCreditAmount(object[1].toString());
				 if(object[8]!=null){
				 CFunction function = (CFunction)genericService.find("from CFunction where id=?",((BigDecimal)object[8]).longValue());
				 accInfo.setFunction(function.getCode());
				 }
				 creaditAccList.add(accInfo);
				 voucherInput.setCreaditAccounts(creaditAccList);
				 if(null != object[11]){
					 SubledgerInfo subledgerInfo = new SubledgerInfo();
					 subledgerInfo.setGlcode((String)object[0]);
					 subledgerInfo.setDetailAmount(object[1].toString());
					 subledgerInfo.setDetailType(object[9].toString());
					 subledgerInfo.setDetailKey(object[10].toString());
					 voucherInput.setSubledgerInfo(subledgerInfo);
				 }
				 
				 resultMap.put(key, voucherInput);
			 }
		 }
		 
		 return new ArrayList<VoucherInput>(resultMap.values());
	 }
	 
	 /**
		 * This method will return Bill amount for a given Bill 
		 * @param workOrderId
		 * @return
		 */
		public BigDecimal getApprovedMBAmountforBill(ContractorBillRegister contractorBillRegister) {
			BigDecimal result = BigDecimal.ZERO; 
			Object[] params = new Object[]{contractorBillRegister.getId()};
			List<MBHeader> approvedMBsForCancelledBillList=new ArrayList<MBHeader>();
			List<MBDetails> approvedMBsList=new ArrayList<MBDetails>();
			approvedMBsForCancelledBillList=genericService.findAllByNamedQuery("getMBListForCancelledBill", params);
			approvedMBsList=genericService.findAllByNamedQuery("getMBAmountForBill", params); 
			
			
			GenericTenderResponse tenderResponse=null;
			if(!approvedMBsList.isEmpty())
				tenderResponse=genericTenderService.getGenericResponseByNumber(approvedMBsList.get(0).getMbHeader().getWorkOrder().getNegotiationNumber());
			String mbPercentagelevel=worksService.getWorksConfigValue(WorksConstants.MBPERCENTAPPCONFIGKEY);
			
			
			if(approvedMBsForCancelledBillList.isEmpty()){
				for(MBDetails mbd:approvedMBsList){
					Double amount=0.0;
					if(mbPercentagelevel.equalsIgnoreCase(WorksConstants.MBPERCENTCONFIGVALUE) && tenderResponse.getBidType().equals(BidType.PERCENTAGE)){
						if(mbd.getWorkOrderActivity().getActivity().getNonSor()==null){
							amount=(Double)mbd.getWorkOrderActivity().getApprovedRate()*mbd.getQuantity()*mbd.getWorkOrderActivity().getActivity().getConversionFactor();
						}
						else{
							amount=mbd.getWorkOrderActivity().getApprovedRate()*mbd.getQuantity();
						}
						if(mbd.getMbHeader().getRevisionEstimate()!=null && mbd.getWorkOrderActivity().getActivity().getRevisionType()!=null &&mbd.getWorkOrderActivity().getActivity().getRevisionType().equals(RevisionType.EXTRA_ITEM)){
							result=result.add(BigDecimal.valueOf(amount));
						}
						else{
							if(tenderResponse.getPercentage().compareTo(new BigDecimal(0))>=0){
								amount=amount+(amount*tenderResponse.getPercentage().doubleValue()/100);
							}
							else{
								amount=amount-(amount*-1*tenderResponse.getPercentage().doubleValue()/100);
							}
							result=result.add(BigDecimal.valueOf(amount));
						}
					}
					else{
						if(mbd.getWorkOrderActivity().getActivity().getNonSor()==null){
							amount=(Double)mbd.getWorkOrderActivity().getApprovedRate()*mbd.getQuantity()*mbd.getWorkOrderActivity().getActivity().getConversionFactor();
						}
						else{
							amount=mbd.getWorkOrderActivity().getApprovedRate()*mbd.getQuantity();
						}
						result=result.add(BigDecimal.valueOf(amount));
					}
				}
			}
			else{
				for(MBHeader mbh:approvedMBsForCancelledBillList){
					List<MBDetails> mbdetails=new ArrayList<MBDetails>();
					mbdetails=mbh.getMbDetails();
					for(MBDetails mbd:mbdetails){ 
						Double amount=0.0;
						if(mbPercentagelevel.equalsIgnoreCase(WorksConstants.MBPERCENTCONFIGVALUE) && tenderResponse.getBidType().equals(BidType.PERCENTAGE)){
							if(mbd.getWorkOrderActivity().getActivity().getNonSor()==null){
								amount=mbd.getWorkOrderActivity().getApprovedRate()*mbd.getQuantity()*mbd.getWorkOrderActivity().getActivity().getConversionFactor();
							}
							else{
								amount=mbd.getWorkOrderActivity().getApprovedRate()*mbd.getQuantity();
							}
							if(mbd.getMbHeader().getRevisionEstimate()!=null && mbd.getWorkOrderActivity().getActivity().getRevisionType()!=null &&mbd.getWorkOrderActivity().getActivity().getRevisionType().equals(RevisionType.EXTRA_ITEM)){
								result=result.add(BigDecimal.valueOf(amount));
							}
							else{
								if(tenderResponse.getPercentage().compareTo(new BigDecimal(0))>=0){
									amount=amount+(amount*tenderResponse.getPercentage().doubleValue()/100);
								}
								else{
									amount=amount-(amount*-1*tenderResponse.getPercentage().doubleValue()/100);
								}
								result=result.add(BigDecimal.valueOf(amount));
							}
						}
						else{
							if(mbd.getWorkOrderActivity().getActivity().getNonSor()==null){
								amount=mbd.getWorkOrderActivity().getApprovedRate()*mbd.getQuantity()*mbd.getWorkOrderActivity().getActivity().getConversionFactor();
							}
							else{
								amount=mbd.getWorkOrderActivity().getApprovedRate()*mbd.getQuantity();
							}
							result=result.add(BigDecimal.valueOf(amount));
						}
					}
				}
			}
			return result;
		}
		
				
	@Override
	public List<AssetForBill> getAssetCapitalisationDetails(Asset asset) {
		List<AssetForBill> capitaliseAssetDetails = null;
		capitaliseAssetDetails = genericService.findAllBy("from AssetForBill afb where afb.asset.id=?",asset.getId());
		return capitaliseAssetDetails;
	}
	 
	 
	 private Map<String, Integer> getSpecialUoms() {
			AjaxEstimateAction estaction = new AjaxEstimateAction();
	        estaction.setWorksService(worksService);
			return estaction.getExceptionSOR();
		}
	
	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}
	
	public void setCommonsService(CommonsService commonsService) { 
		this.commonsService = commonsService;
	}

	public void setChecklistService(
			PersistenceService<EgChecklists, Long> checklistService) {
		this.checklistService = checklistService;
	}
	public void setCommonAssetsService(CommonAssetsService commonAssetsService) {
		this.commonAssetsService = commonAssetsService;
	}
	public String getFinalBillTypeConfigValue() {		
		return worksService.getWorksConfigValue("FinalBillType");
	}

	public void setAssetService(AssetService assetService) {
		this.assetService = assetService;
	}
	public void setGenericTenderResponseService(TenderResponseService genericTenderResponseService) {
		this.genericTenderResponseService = genericTenderResponseService;
	}
	
	public void setMeasurementBookService(MeasurementBookService measurementBookService) {
		this.measurementBookService = measurementBookService;
	}

	public void setGenericTenderService(GenericTenderService genericTenderService) {
		this.genericTenderService = genericTenderService;
	}
	
	public boolean checkPartRateApplicable(MBDetails mbd){
		boolean partRateApplicableFor2ndBill =false;
		List<MBBill> partRateAppliedBills=new ArrayList<MBBill>();
		MBBill partRateBill=null;
		if(!mbd.getMbHeader().getMbBills().isEmpty()){
			for(MBBill mbBill:mbd.getMbHeader().getMbBills()){
				if (!mbBill.getEgBillregister().getBillstatus().equalsIgnoreCase("CANCELLED") && mbd.getPartRate()>0){
					partRateAppliedBills.add(mbBill);
				}
			}
			if(mbd.getPartRate()>0 && partRateAppliedBills.size()==1){//Checking if already one partbill has been created. if yes then 2nd one can be created. if 2 has been created, then it wont be able to create again
					partRateApplicableFor2ndBill=true;
			}
		}
		return partRateApplicableFor2ndBill;
	}
	public boolean checkCancelBill(MBDetails mbd){
		boolean validCancelledBill=false;
		List validBills=new ArrayList();
		if(!mbd.getMbHeader().getMbBills().isEmpty()){
			for(MBBill mbBill:mbd.getMbHeader().getMbBills()){
				if (mbBill.getEgBillregister().getBillstatus()!=null && !mbBill.getEgBillregister().getBillstatus().equalsIgnoreCase("CANCELLED"))
					validBills.add(mbBill);
			}
			if(validBills.isEmpty() || (mbd.getPartRate()>0 && validBills.size()==1))
				validCancelledBill=true;
		}else{
			validCancelledBill=true;
		}
		return validCancelledBill;
	}

	public String generateContractCertificateNumber(EgBillregister bill, WorkOrderEstimate workOrderEstimate) {
		CFinancialYear financialYear = getCurrentFinancialYear(bill.getBilldate());
		return contractCertificateNumberGenerator.getContractCertificateNumber(financialYear, workOrderEstimate);
	}
	public void setContractCertificateNumberGenerator(
			ContractCertificateNumberGenerator contractCertificateNumberGenerator) {
		this.contractCertificateNumberGenerator = contractCertificateNumberGenerator;
	}
	
	public BigDecimal getTotStandardAmountForDeduction(DeductionTypeForBill deductionTypeForBill){
		 BigDecimal totStandardAmt=BigDecimal.ZERO;
		 totStandardAmt=getTotAmtForStandard(egBillRegister.getBilldate(), woEstimate.getWorkOrder().getId(),deductionTypeForBill, woEstimate.getId());
		 return totStandardAmt;
	 }
	
	public BigDecimal getTotStandardAmountForDeduction(EgBilldetails egBilldetails){
		 BigDecimal totCustomAmt=BigDecimal.ZERO;
		 totCustomAmt=getTotAmtForCustom(egBillRegister.getBilldate(), woEstimate.getWorkOrder().getId(),egBilldetails, woEstimate.getId());
		 return totCustomAmt;
	 }
	
	public BigDecimal getTotStatoryAmountForDeduction(StatutoryDeductionsForBill egBillPayeedetail){
		 BigDecimal totalStatoryAmount=BigDecimal.ZERO;
		 totalStatoryAmount=getTotAmtForStatutory(egBillRegister.getBilldate(), woEstimate.getWorkOrder().getId(),egBillPayeedetail,woEstimate.getId());
		 return totalStatoryAmount;
	 }
	
	public void getStandardDeductionList(Long billId, List<String> requiredStandardList){
		 List<DeductionTypeForBill> currentStandardDeductionList = getStandardDeductionForBill(billId);
		 sortedStandardDeductionList=getStandardDeductionSortedOrder(requiredStandardList, currentStandardDeductionList);
	 }
	
	public  void getCustomDeductionList(ContractorBillRegister contractorBillRegister) throws EGOVException{
		customDeductionList = new ArrayList<EgBilldetails>();
		glcodeIdList=new ArrayList<BigDecimal>();		
		retentionMoneyglcodeIdList=new ArrayList<BigDecimal>();
		getStatutoryDeductionGlcode();
		getStandardDeductionGlcode();
		String advanceAdjstglCodeId=getAdvanceAdjustmentGlcode();
		getGlCodeForNetPayable();
		glcodeIdList.add(new BigDecimal(advanceAdjstglCodeId));
		getRetentionMoneyGlCodeList();
		customDeductionList=getCustomDeductionListforglcodes(glcodeIdList,contractorBillRegister.getId());
	 }
	
	public void getStatutoryDeductionGlcode(){
		if(!sortedStatutorySortedList.isEmpty()){
			for(StatutoryDeductionsForBill bpd:sortedStatutorySortedList){
				if(bpd!=null && bpd.getEgBillPayeeDtls().getRecovery()!=null && bpd.getEgBillPayeeDtls().getRecovery().getId()!=null &&
						bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts()!=null && bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId()!=null){
					glcodeIdList.add(new BigDecimal(bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId()));
				}
			}
		}
	}	
	
	public void getStandardDeductionGlcode()
	{
			if(!sortedStandardDeductionList.isEmpty()){
				for(DeductionTypeForBill deductionTypeForBill:sortedStandardDeductionList){
					if(deductionTypeForBill.getCoa()!=null && deductionTypeForBill.getCoa().getId()!=null){
						glcodeIdList.add(new BigDecimal(deductionTypeForBill.getCoa().getId()));
				}
			}
		}
	 }
 
	public void getGlCodeForNetPayable() throws NumberFormatException, EGOVException{
		ContractorBillRegister contractorBillRegister;
		 List<CChartOfAccounts> coaPayableList = commonsService.getAccountCodeByPurpose(Integer.valueOf(worksService.getWorksConfigValue(WORKS_NETPAYABLE_CODE)));
		// if(!coaPayableList.isEmpty()){
		 if(coaPayableList!=null){
			for(CChartOfAccounts coa :coaPayableList){
				if(coa.getId()!=null){
					netPayableAmount=getNetPayableAmountForGlCodeId(egBillRegister.getId());
					glcodeIdList.add(new BigDecimal(coa.getId()));
				}
			}	
		 }
	 }
	
	public void getRetentionMoneyGlCodeList() throws NumberFormatException, EGOVException{
		List<CChartOfAccounts> retentionMoneyAccountList =new ArrayList<CChartOfAccounts>();
		if(StringUtils.isNotBlank(worksService.getWorksConfigValue(RETENTION_MONEY_PURPOSE))){
				retentionMoneyAccountList = commonsService.getAccountCodeByPurpose(Integer.valueOf(worksService.getWorksConfigValue(RETENTION_MONEY_PURPOSE)));
		}
		for(CChartOfAccounts coa:retentionMoneyAccountList){
				retentionMoneyglcodeIdList.add(BigDecimal.valueOf(coa.getId()));
		}
		
	 }
	
	public BigDecimal getTotAmountForAdvanceAdjustment(){
		 BigDecimal totalDeductionAmt=BigDecimal.ZERO;
		 totalDeductionAmt=getTotAmtForAdvanceAdjustment(egBillRegister.getBilldate(), woEstimate.getWorkOrder().getId(), woEstimate.getId());
		 return totalDeductionAmt;
	 }
	
	public List<Map<String, Object>> getDeductionsListData(ContractorBillRegister contractorBillRegister, List<StatutoryDeductionsForBill> sortedStatutorySortedList, List<DeductionTypeForBill> sortedStandardDeductionList, List<EgBilldetails> customDeductionList)
	 {
		 Map<String,Object>  deducMap;
		 Map<String,Object>  deducMap2;
		 Map<String,Object>  advanceAdjustDeducMap;
		 Map<String,Object>  cusDeducMap;
		 BigDecimal totalAmt = new BigDecimal(0);

		 List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
		 DecimalFormat df = new DecimalFormat("###.##");
		 
		 if(sortedStatutorySortedList!=null && !sortedStatutorySortedList.isEmpty())
		 {
			 for(StatutoryDeductionsForBill egBillPayeedetail:sortedStatutorySortedList){
				 deducMap = new HashMap<String, Object>();
				 BigDecimal creditAmount = egBillPayeedetail.getEgBillPayeeDtls().getCreditAmount();
				 deducMap.put("glcode",egBillPayeedetail.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getGlcode()+" ~ "+ egBillPayeedetail.getEgBillPayeeDtls().getRecovery().getType());
				 BigDecimal percentage = getPercentage(creditAmount);
				 deducMap.put("percentage", new BigDecimal(df.format(percentage)).setScale(2));
				 deducMap.put("remarks", egBillPayeedetail.getEgBillPayeeDtls().getNarration());
				 deducMap.put("amount", creditAmount);
				 mapList.add(deducMap);
			 }
		 }
		 
		String type="advanceAjustment";
		if("advanceAjustment".equalsIgnoreCase(type))
		{
				advanceAdjustDeducMap = new HashMap<String, Object>();
				BigDecimal creditAmount = advanceAdjustment;
				BigDecimal percentage = getPercentage(creditAmount);
				advanceAdjustDeducMap.put("glcode", "Advance Adjustments");
				advanceAdjustDeducMap.put("percentage", new BigDecimal(df.format(percentage)).setScale(2));
				advanceAdjustDeducMap.put("amount", creditAmount);
		}
			
		 if(sortedStandardDeductionList!=null && !sortedStandardDeductionList.isEmpty())
		 {
			 for(DeductionTypeForBill deductionTypeForBill:sortedStandardDeductionList)
			 {
				 deducMap2 = new HashMap<String, Object>();
				 BigDecimal creditAmount = deductionTypeForBill.getCreditamount();
				 totalAmt.add(creditAmount);
				 deducMap2.put("glcode",deductionTypeForBill.getCoa().getGlcode()+" ~ "+deductionTypeForBill.getDeductionType());
				 BigDecimal percentage = getPercentage(creditAmount);
				 deducMap2.put("percentage", new BigDecimal(df.format(percentage)).setScale(2));
				 deducMap2.put("remarks", deductionTypeForBill.getNarration());
				 deducMap2.put("amount", creditAmount);
				 mapList.add(deducMap2);
			 }
		 }

		 if(customDeductionList!=null && !customDeductionList.isEmpty())
		 {
			 for(EgBilldetails egBilldetails:customDeductionList){
				 cusDeducMap = new HashMap<String, Object>();
				 BigDecimal creditAmount = egBilldetails.getCreditamount();
				 cusDeducMap.put("glcode", commonsService.getCChartOfAccountsById(Long.valueOf(egBilldetails.getGlcodeid().toString())).getGlcode()+" ~ "+commonsService.getCChartOfAccountsById(Long.valueOf(egBilldetails.getGlcodeid().toString())).getName());
				 BigDecimal percentage = getPercentage(creditAmount);
				 cusDeducMap.put("percentage", new BigDecimal(df.format(percentage)).setScale(2));
				 cusDeducMap.put("remarks", egBilldetails.getNarration());
				 cusDeducMap.put("amount", creditAmount);
				 mapList.add(cusDeducMap);
			 }
		 }
		 return mapList;
	 }
}

