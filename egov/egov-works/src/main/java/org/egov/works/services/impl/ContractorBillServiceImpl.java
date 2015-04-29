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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.asset.service.AssetService;
import org.egov.asset.service.CommonAssetsService;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CommonsService;
import org.egov.dao.bills.BillsDaoFactory;
import org.egov.dao.bills.EgBilldetailsDAO;
import org.egov.egf.commons.EgovCommon;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.EgChecklists;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.utils.FinancialConstants;
import org.egov.works.models.contractorBill.AssetForBill;
import org.egov.works.models.contractorBill.ContractorBillNumberGenerator;
import org.egov.works.models.contractorBill.ContractorBillRegister;
import org.egov.works.models.contractorBill.DeductionTypeForBill;
import org.egov.works.models.contractorBill.StatutoryDeductionsForBill;
import org.egov.works.models.contractorBill.WorkCompletionDetailInfo;
import org.egov.works.models.contractorBill.WorkCompletionInfo;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.measurementbook.MBDetails;
import org.egov.works.models.measurementbook.MBForCancelledBill;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.tender.SetStatus;
import org.egov.works.models.tender.TenderResponse;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.ContractorBillService;
import org.egov.works.services.TenderResponseService;
import org.egov.works.services.WorksService;
import org.egov.works.services.contractoradvance.ContractorAdvanceService;
import org.egov.works.utils.DateConversionUtil;
import org.egov.works.utils.WorksConstants;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;

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
	private EgovCommon egovCommon;
	@Autowired
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
	private TenderResponseService tenderResponseService;
	public static final String PROJECT_STATUS_CLOSED = "CLOSED";
	public static final String BILL_DEPT_ID		  ="BILL_DEPT_ID";
	public static final String EXEC_DEPT_ID		  ="EXEC_DEPT_ID";
	public static final String EST_NO          ="EST_NO";
	private ContractorAdvanceService contractorAdvanceService;
	

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
	 * @param totalAdvancePaid, billDate, workOrderEstimate
	 * @param workOrder an instance of <code>WorkOrder</code>.
	 * @return a BigDecimal value indicating total Advance pending for given Work Order Estimate as on bill date before this current bill
	 */
	public BigDecimal calculateTotalPendingAdvance(BigDecimal totalAdvancePaid, Date billDate, WorkOrderEstimate workOrderEstimate, Long billId) {		 
		 CChartOfAccounts advanceCOA = contractorAdvanceService.getContractorAdvanceAccountcodeForWOE(workOrderEstimate.getId());
		
		 BigDecimal totalPendingBalance=BigDecimal.ZERO;		 
		 BigDecimal totalAdvanceAdjusted = BigDecimal.ZERO;	
		 if(advanceCOA != null && totalAdvancePaid!=null && totalAdvancePaid.compareTo(BigDecimal.ZERO)>0){			
			 totalAdvanceAdjusted = getTotalAdvanceAdjustedForWOE(billDate, workOrderEstimate.getId(), advanceCOA.getId(), billId);			
			 if(totalAdvanceAdjusted != null && totalAdvanceAdjusted.compareTo(BigDecimal.ZERO)>0
					 && totalAdvanceAdjusted.compareTo(BigDecimal.ZERO)>0) 
				 totalPendingBalance=totalAdvanceAdjusted.subtract(totalAdvanceAdjusted);
			 else
				 totalPendingBalance = totalAdvancePaid;
		 }
		 
		 return totalPendingBalance;
	}	
	
	/**
	 * API will returns the Total Amount for advance Adjusted  up to billdate for specific Work Order Estimate before this current bill
	 * @return  BigDecimal 
	 */
	public BigDecimal getTotalAdvanceAdjustedForWOE(Date billDate, Long workOrderEstimateId, Long advanceGlCodeId, Long billId){
		BigDecimal advanceAdjustment=BigDecimal.ZERO;
		List<Long> billIdList=getBillIdListUptoBillDate(billDate, workOrderEstimateId, billId);
		
		if(billIdList==null || billIdList.isEmpty())
			billIdList.add(null);
	
		List<EgBilldetails> egBilldetailsList=genericService.findAllByNamedQuery("getAdvanceAjustementTotAmt",new BigDecimal(advanceGlCodeId),billIdList);
		
		for(EgBilldetails egBilldetails:egBilldetailsList){
			if(egBilldetails.getCreditamount()!=null)
				advanceAdjustment=advanceAdjustment.add(egBilldetails.getCreditamount());
		}

		return advanceAdjustment;
	}
	
	/**
	 * API will returns the billId list for the contractor upto billdate, for giver Work Order Estimate before this current bill 
	 * @return  BigDecimal 
	 */
	public List<Long> getBillIdListUptoBillDate(Date billDate,Long workOrderEstimateId,Long billId){
		List<Long> billIdList=new ArrayList<Long>();
		ArrayList<Object> params=new ArrayList<Object>();
		String whereClause="";
		params.add(workOrderEstimateId);
		params.add(billDate);

		if(billId!=null){
			EgBillregister egbr = (EgBillregister) genericService.find("from EgBillregister egbr where egbr.id = ? ", billId);
			if(egbr.getBillstatus().equalsIgnoreCase("CANCELLED")){
				whereClause=" mbh.egBillregister.billdate <= ? and  mbh.egBillregister.id<? )";
				params.add(billId);
			}
			else{
				whereClause=" mbh.egBillregister.billdate <= ? and mbh.egBillregister.billstatus!='CANCELLED' and mbh.egBillregister.id<? )";
				params.add(billId);
			}
		}
		else {
			whereClause=" mbh.egBillregister.billdate <= ? and mbh.egBillregister.billstatus!='CANCELLED')"; 
		}
		
		
		List<EgBillregister> egBillregisterList=genericService.findAllBy("select distinct mbh.egBillregister from MBHeader mbh where mbh.workOrderEstimate.id = ? " +
				" and "+whereClause,params.toArray());
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
		Object[] params = new Object[]{WorksConstants.CANCELLED_STATUS,workOrderId,asOnDate};
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
		Object[] params = new Object[]{WorksConstants.APPROVED,workOrderId,workOrderEstimateId, asOnDate};
		Double queryVal = (Double) genericService.findByNamedQuery("totalApprovedMBAmount",params);
		params = new Object[]{WorksConstants.APPROVED,workOrderId,workOrderEstimateId, asOnDate,WorksConstants.CANCELLED_STATUS};
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
		Object[] params = new Object[]{WorksConstants.APPROVED,workOrderId,workOrderEstimateId, asOnDate};
		// NOTE -- Here we will not consider legacy MBs -- the named query below has been modified for this purpose
		List<Object[]> approvedMBsList=genericService.findAllByNamedQuery("gettotalApprovedMBs", params);
		
		params = new Object[]{WorksConstants.APPROVED,workOrderId,workOrderEstimateId, asOnDate,WorksConstants.CANCELLED_STATUS};
		// NOTE -- Here we will not consider legacy MBs -- the named query below has been modified for this purpose
		List<Object[]> approvedMBsForCancelledBillList=genericService.findAllByNamedQuery("gettotalApprovedMBsForCancelledBill", params); 
		
		Double amount=0.0;
		Iterator iter1;
		Iterator iter2;
		Iterator iter3;
		Iterator iter4;
		
		List<Long> woaIdsListForApprovedMBs = new ArrayList<Long>();
		iter1 = approvedMBsList.iterator();
		
		while(iter1.hasNext()){
			Object[] obj =  (Object[]) iter1.next();
			woaIdsListForApprovedMBs.add((Long) obj[0]);
		}

		if(!woaIdsListForApprovedMBs.isEmpty()){
			List<WorkOrderActivity> woaListForApprovedMBs = getWorkOrderActivityListForIds(woaIdsListForApprovedMBs);
			iter2 = approvedMBsList.iterator();
				while(iter2.hasNext()){
					Object[] obj =  (Object[]) iter2.next();
					Long woaId = (Long) obj[0];
					double mbQuantity = (Double) obj[1];
					for(WorkOrderActivity woa : woaListForApprovedMBs){
						if(woaId.equals(woa.getId())){
							if(woa.getActivity().getNonSor()==null){
								amount = woa.getApprovedRate()*mbQuantity*woa.getConversionFactor();
							}
							else{
								amount=woa.getApprovedRate()*mbQuantity;
							}
								result=result.add(BigDecimal.valueOf(amount));
								break;
						}
						
					}
				}
		}

			List<Long> woaIdsListForCancelledBills = new ArrayList<Long>();
			iter3 = approvedMBsForCancelledBillList.iterator();

			while(iter3.hasNext()){
				Object[] obj =  (Object[]) iter3.next();
				woaIdsListForCancelledBills.add((Long) obj[0]);
			}
			if(!woaIdsListForCancelledBills.isEmpty()){
				List<WorkOrderActivity> woaListForCancelledBills = getWorkOrderActivityListForIds(woaIdsListForCancelledBills);
				iter4 = approvedMBsForCancelledBillList.iterator();

				while(iter4.hasNext()){
					Object[] obj =  (Object[]) iter4.next();
					Long woaId = (Long) obj[0];
					double mbQuantity = (Double) obj[1];
					for(WorkOrderActivity woa : woaListForCancelledBills){
						if(woaId.equals(woa.getId())){
							if(woa.getActivity().getNonSor()==null){
								amount = woa.getApprovedRate()*mbQuantity*woa.getConversionFactor();
								
							}
							else{
								amount=woa.getApprovedRate()*mbQuantity;
							}
								result=result.add(BigDecimal.valueOf(amount));
								break;
						}
					}
				}
		}
		return result;
	}
	
	/**
	 * This method will return tendered Items cumulative amount for all approved MB for a given workorder 
	 * (Tendered Items don't have a revision type)   
	 * @param workOrderId
	 * @return
	 */
	public BigDecimal getApprovedMBAmountOfTenderedItems(Long workOrderId,Long workOrderEstimateId, Date asOnDate) {
		BigDecimal result = BigDecimal.ZERO;
		Object[] params = new Object[]{WorksConstants.APPROVED,workOrderId,workOrderEstimateId, asOnDate};
		// NOTE -- Here we will not consider legacy MBs -- the named query below has been modified for this purpose
		List<Object[]> approvedMBsList=genericService.findAllByNamedQuery("gettotalApprovedMBs", params);
		
		params = new Object[]{WorksConstants.APPROVED,workOrderId,workOrderEstimateId, asOnDate,WorksConstants.CANCELLED_STATUS};
		// NOTE -- Here we will not consider legacy MBs -- the named query below has been modified for this purpose
		List<Object[]> approvedMBsForCancelledBillList=genericService.findAllByNamedQuery("gettotalApprovedMBsForCancelledBill", params);
		
		Double amount=0.0;
		Iterator iter1;
		Iterator iter2;
		Iterator iter3;
		Iterator iter4;

			List<Long> woaIdsListForApprovedMBs = new ArrayList<Long>();
			iter1 = approvedMBsList.iterator();
			
			while(iter1.hasNext()){
				Object[] obj =  (Object[]) iter1.next();
				woaIdsListForApprovedMBs.add((Long) obj[0]);
			}
			if(!woaIdsListForApprovedMBs.isEmpty()){
				List<WorkOrderActivity> woaListForApprovedMBs = getWorkOrderActivityListForIds(woaIdsListForApprovedMBs);
				iter2 = approvedMBsList.iterator();
				while(iter2.hasNext()){
					Object[] obj =  (Object[]) iter2.next();
					Long woaId = (Long) obj[0];
					double mbQuantity = (Double) obj[1];
					for(WorkOrderActivity woa : woaListForApprovedMBs){
						if(woaId.equals(woa.getId())){
							if(woa.getActivity().getNonSor()==null){
								amount = woa.getApprovedRate()*mbQuantity*woa.getConversionFactor();
							}
							else{
								amount=woa.getApprovedRate()*mbQuantity;
							}
							if(woa.getActivity().getRevisionType()==null){
								result=result.add(BigDecimal.valueOf(amount));
								break;
							}
						}
						
					}
					
				}
		}

			List<Long> woaIdsListForCancelledBills = new ArrayList<Long>();
			iter3 = approvedMBsForCancelledBillList.iterator();
	
			while(iter3.hasNext()){
				Object[] obj =  (Object[]) iter3.next();
				woaIdsListForCancelledBills.add((Long) obj[0]);
			}
	
			if(!woaIdsListForCancelledBills.isEmpty()){
				List<WorkOrderActivity> woaListForCancelledBills = getWorkOrderActivityListForIds(woaIdsListForCancelledBills);
				iter4 = approvedMBsForCancelledBillList.iterator();
				while(iter4.hasNext()){
					Object[] obj =  (Object[]) iter4.next();
					Long woaId = (Long) obj[0];
					double mbQuantity = (Double) obj[1];
					for(WorkOrderActivity woa : woaListForCancelledBills){
						if(woaId.equals(woa.getId())){
							if(woa.getActivity().getNonSor()==null){
								amount = woa.getApprovedRate()*mbQuantity*woa.getConversionFactor();
							}
							else{
								amount=woa.getApprovedRate()*mbQuantity;
							}
							if(woa.getActivity().getRevisionType()==null){
								result=result.add(BigDecimal.valueOf(amount));
								break;
							}
						}
						
					}
			}
		}
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
		String finyearId = commonsService.getFinancialYearId(DateUtils.getFormattedDate(asOnDate,"dd-MMM-yyyy"));
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
		List<EgBillregister> egBillregisterList=genericService.findAllBy("select distinct mbh.egBillregister from MBHeader mbh where mbh.egBillregister.id in (select egBillRegister.id from org.egov.model.bills.EgBillregister egBillRegister " +
								 " where egBillRegister.billdate <= ? and egBillRegister.billstatus <>'CANCELLED') and mbh.workOrder.id = ? and mbh.workOrderEstimate.id=?",billDate,workOrderId,workOrderEstimateId);		
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
	 * API will returns the Advance adjustment amount for a given bill Id
	 * @return  BigDecimal 
	 */
	public BigDecimal getAdvanceAdjustmentAmountForBill(Long billId, Long workOrderEstimateId){
		
		BigDecimal advanceAdjustment=BigDecimal.ZERO;
		CChartOfAccounts advanceCOA = contractorAdvanceService.getContractorAdvanceAccountcodeForWOE(workOrderEstimateId);
		if(advanceCOA != null) {
			EgBilldetails egBilldetails=(EgBilldetails)genericService.find("from EgBilldetails ebd where ebd.glcodeid=? and " +
						"ebd.egBillregister.id=?",new BigDecimal(advanceCOA.getId()),billId);
				if(egBilldetails!=null && egBilldetails.getCreditamount() != null)
					advanceAdjustment = egBilldetails.getCreditamount();
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
	 * API will returns the Total Amount for advanceAjustment deduction for work order estimate upto billdate
	 * @return  BigDecimal 
	 */
	public BigDecimal getTotAmtForAdvanceAdjustment(Date billDate, Long workOrderId, Long workOrderEstimateId){
		BigDecimal totDeductionAmt=BigDecimal.ZERO;
		CChartOfAccounts advanceCOA = contractorAdvanceService.getContractorAdvanceAccountcodeForWOE(workOrderEstimateId);
		if(advanceCOA != null)
			totDeductionAmt=getAdvanceAdjustmentDeductionTotAmount(billDate,workOrderId,advanceCOA.getId(),workOrderEstimateId);
		return totDeductionAmt;
	}
	
	/**
	 *  API will returns the Total Amount for advanceAjustment deduction for work order estimate upto billdate
	 * @return  BigDecimal 
	 */
	public BigDecimal getAdvanceAdjustmentDeductionTotAmount(Date billDate,Long workOrderId, Long advanceCOAId, Long workOrderEstimateId){
		BigDecimal advanceAdjustment=BigDecimal.ZERO;
		List<Long> billIdList=getBillIdListForWoUptoBillDate(billDate,workOrderId, workOrderEstimateId);
		
		if(billIdList==null || billIdList.isEmpty())
			billIdList.add(null);
		
		List<EgBilldetails> egBilldetailsList=genericService.findAllByNamedQuery("getAdvanceAjustementTotAmt",new BigDecimal(advanceCOAId),billIdList);
		
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
		
		List<StatutoryDeductionsForBill> egBillPayeedetailsList = new ArrayList<StatutoryDeductionsForBill>();
		if(billIdList!=null && !billIdList.isEmpty())
			egBillPayeedetailsList=genericService.findAllByNamedQuery("getStatutoryTotAmt",statDeductionBilldetail.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId(),billIdList);
		
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

		List<DeductionTypeForBill> standardDeductionList = new ArrayList<DeductionTypeForBill>();
		if(billIdList!=null && !billIdList.isEmpty())			
			standardDeductionList=genericService.findAllByNamedQuery("getStandardTotAmt",deductionTypeForBill1.getCoa().getId(),billIdList);
		
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
		List<EgBilldetails> customDeductionList = new ArrayList<EgBilldetails>();
		if(billIdList!=null && !billIdList.isEmpty())
			customDeductionList=genericService.findAllByNamedQuery("getCustomDeductionTotAmt",egBilldetails1.getGlcodeid(),billIdList);			
		
		for(EgBilldetails egBilldetails:customDeductionList){
			if(egBilldetails.getCreditamount()!=null)
				totalCustomDeductionAmount=totalCustomDeductionAmount.add(egBilldetails.getCreditamount());
		}
		
		return totalCustomDeductionAmount;
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
		
		List<EgBillregister> egBillregisterList=genericService.findAllBy("select distinct mbh.egBillregister from MBHeader mbh " +
				"where mbh.egBillregister.billdate <=? and mbh.egBillregister.billstatus<>'CANCELLED' and mbh.workOrder.id=? and mbh.workOrderEstimate.id=?",billDate,workOrderId,workOrderEstimateId);
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
	public List<String> searchContractorBill(Map<String, Object> paramsMap, List<Object> paramList){
		
		Object[] params;
		List<String> QueryObj = new ArrayList<String>();
		StringBuffer commonQry=new StringBuffer();
		String countQry="select count(distinct cbr) from ContractorBillRegister cbr where cbr.id != null and cbr.billstatus != ? " ;
		
		String dynQuery = "select distinct cbr from ContractorBillRegister cbr where cbr.id != null and cbr.billstatus != ? " ;
		paramList.add(WorksConstants.NEW);
		
		if(paramsMap.get(WORKORDER_NO) != null){
			commonQry=commonQry.append("  and cbr.workordernumber like ?");
			paramList.add("%"+paramsMap.get(WORKORDER_NO)+"%");
		}
		if(paramsMap.get(CONTRACTOR_ID) != null && !"-1".equals(paramsMap.get(CONTRACTOR_ID))) {
			commonQry=commonQry.append(" and (cbr.id in (select mbh.egBillregister.id from MBHeader mbh where mbh.egBillregister.id=cbr.id and mbh.workOrder.contractor.id = ?)" + " OR cbr.id in (select mbcb.egBillregister.id from MBForCancelledBill mbcb where mbcb.egBillregister.id=cbr.id and mbcb.mbHeader.workOrder.contractor.id = ?))");
			paramList.add(paramsMap.get(CONTRACTOR_ID));
			paramList.add(paramsMap.get(CONTRACTOR_ID));
			
		}
		if(paramsMap.get(FROM_DATE) != null && paramsMap.get(TO_DATE)==null) {
			commonQry=commonQry.append( " and cbr.billdate >= ? ");
			paramList.add(paramsMap.get(FROM_DATE));

		}else if(paramsMap.get(TO_DATE) != null && paramsMap.get(FROM_DATE)==null) {
			commonQry=commonQry.append( " and cbr.billdate <= ? ");
			paramList.add(paramsMap.get(TO_DATE));
		}else if(paramsMap.get(FROM_DATE) != null && paramsMap.get(TO_DATE)!=null) {
			commonQry=commonQry.append( " and cbr.billdate between ? and ? ");
			paramList.add(paramsMap.get(FROM_DATE));
			paramList.add(paramsMap.get(TO_DATE));
		}
		if(paramsMap.get(BILLSTATUS) != null && !paramsMap.get(BILLSTATUS).equals("-1")){
				commonQry=commonQry.append( " and cbr.billstatus=?");
			paramList.add(paramsMap.get(BILLSTATUS));
		}
		if(paramsMap.get(BILLNO) != null){
			commonQry=commonQry.append( " and cbr.billnumber like ?");
			paramList.add("%"+paramsMap.get(BILLNO)+"%");
		}
		
		if(paramsMap.get(BILL_DEPT_ID) != null && !"-1".equals(paramsMap.get(BILL_DEPT_ID))) {
			commonQry=commonQry.append( " and cbr.egBillregistermis.egDepartment.id = ? ");  
			paramList.add(paramsMap.get(BILL_DEPT_ID));			
		}
		if(paramsMap.get(EXEC_DEPT_ID) != null && !"-1".equals(paramsMap.get(EXEC_DEPT_ID))) {
			commonQry=commonQry.append(" and (cbr.id in (select mbh.egBillregister.id from MBHeader mbh where mbh.egBillregister.id=cbr.id " +
					"and mbh.workOrderEstimate.estimate.executingDepartment.id = ?) OR cbr.id in (select mbcb.egBillregister.id from MBForCancelledBill mbcb where" +
							" mbcb.egBillregister.id=cbr.id and mbcb.mbHeader.workOrderEstimate.estimate.executingDepartment.id = ?))");
			paramList.add(paramsMap.get(EXEC_DEPT_ID));
			paramList.add(paramsMap.get(EXEC_DEPT_ID));
			
		}
		if(paramsMap.get(EST_NO) != null) {
			commonQry=commonQry.append(" and (EXISTS (select mbh.egBillregister.id from MBHeader mbh where mbh.egBillregister.id=cbr.id " +
					"and mbh.workOrderEstimate.estimate.estimateNumber like ? ) OR EXISTS (select mbcb.egBillregister.id from MBForCancelledBill mbcb where" +
							" mbcb.egBillregister.id=cbr.id and mbcb.mbHeader.workOrderEstimate.estimate.estimateNumber like ? ))");
			paramList.add("%"+paramsMap.get(EST_NO)+"%");
			paramList.add("%"+paramsMap.get(EST_NO)+"%");
			
		}
		
		commonQry=commonQry.append(" order by cbr.billdate");
		
		QueryObj.add(dynQuery+commonQry);
		QueryObj.add(countQry+commonQry);
		return QueryObj;
	}
	
	/**
	 * Get the list of custom dedcution based on glcodes of custom deduction
	 * @param ContractorBillRegister
	 * @return List
	 * @throws EGOVException 
	 * @throws NumberFormatException 
	 */
	public List<EgBilldetails> getCustomDeductionList(Long billId,Long workOrderEstimateId,List<StatutoryDeductionsForBill> statutoryList,List<DeductionTypeForBill> standardDeductionList, List<EgBilldetails> retentionMoneyDeductionList) throws NumberFormatException, EGOVException{
		List<BigDecimal> glcodeIdList=new ArrayList<BigDecimal>();		
		addStatutoryDeductionGlcode(glcodeIdList,statutoryList);
		addStandardDeductionGlcode(glcodeIdList,standardDeductionList);
		String advanceAdjstglCodeId = "";
		CChartOfAccounts advanceCOA = contractorAdvanceService.getContractorAdvanceAccountcodeForWOE(workOrderEstimateId);
		if(advanceCOA != null)
			advanceAdjstglCodeId = advanceCOA.getId().toString();
		addRetentionMoneyDeductionGlcode(glcodeIdList,retentionMoneyDeductionList);
		addGlCodeForNetPayable(glcodeIdList);
		if(StringUtils.isNotBlank(advanceAdjstglCodeId))
			glcodeIdList.add(new BigDecimal(advanceAdjstglCodeId));
		return getCustomDeductionListforglcodes(glcodeIdList,billId);
	}
	
	public List<EgBilldetails> getRetentionMoneyDeductionList(Long billId,List<StatutoryDeductionsForBill> statutoryList,List<DeductionTypeForBill> standardDeductionList) throws NumberFormatException, EGOVException{
		List<BigDecimal> retentionGlcodeIdList=new ArrayList<BigDecimal>();		
		getAllRetentionMoneyGlcodeList(retentionGlcodeIdList);
		return getRetentionMoneyListforglcodes(retentionGlcodeIdList,billId);
	}
	
	public List<EgBilldetails> getAccountDetailsList(Long billId, Long workOrderEstimateId, List<StatutoryDeductionsForBill> statutoryList,List<DeductionTypeForBill> standardDeductionList, List<EgBilldetails> customDeductionList,List<EgBilldetails> retentionMoneyDeductionList ) throws NumberFormatException, EGOVException{
		List<BigDecimal> glcodeIdList=new ArrayList<BigDecimal>();		
		addStatutoryDeductionGlcode(glcodeIdList,statutoryList);
		addStandardDeductionGlcode(glcodeIdList,standardDeductionList);
		String advanceAdjstglCodeId = "";
		CChartOfAccounts advanceCOA = contractorAdvanceService.getContractorAdvanceAccountcodeForWOE(workOrderEstimateId);
		if(advanceCOA != null)
			advanceAdjstglCodeId = advanceCOA.getId().toString();
		addRetentionMoneyDeductionGlcode(glcodeIdList,retentionMoneyDeductionList);
		addCustomDeductionGlcode(glcodeIdList,customDeductionList);
		addGlCodeForNetPayable(glcodeIdList);
		if(StringUtils.isNotBlank(advanceAdjstglCodeId))
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
			netpaybleCode=egbillDetails.getGlcodeid();
		return netpaybleCode;
	 }
	
	 public List<MBHeader>  getMbListForBillAndWorkordrId(Long workOrderId,Long billId){
	return	 genericService.findAllBy("from MBHeader mbHeader where mbHeader.workOrder.id=? and mbHeader.egBillregister.id=?",workOrderId,billId);
	
	 }
	 
	 public List<MBForCancelledBill>  getMbListForCancelBill(Long billId){
		 List<MBForCancelledBill> list=genericService.findAllBy("from MBForCancelledBill mbcb where  mbcb.egBillregister.id=?",billId);
			return	list; 
			
			 }
	 
	 public void setAllViewLists(Long id,Long workOrderId,Long workOrderEstimateId,List<StatutoryDeductionsForBill> actionStatutorydetails,
			 List<DeductionTypeForBill> standardDeductions, List<EgBilldetails> customDeductions, List<EgBilldetails> retentionMoneyDeductions,
			 List<AssetForBill> accountDetailsForBill) 
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
		customDeductions.clear();
		customDeductions.addAll(getCustomDeductionList(id, workOrderEstimateId,actionStatutorydetails, standardDeductions,retentionMoneyDeductions)); 
		List<EgBilldetails>	accountDetailsForassetandbill = getAccountDetailsList(id,workOrderEstimateId,actionStatutorydetails,standardDeductions,
				customDeductions,retentionMoneyDeductions);
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
				 
		 List<String> mbNumberList=(List<String>)genericService.findAllByNamedQuery(WorksConstants.QUERY_GETALLMBNOSBYWORKORDERESTIMATE,WorksConstants.APPROVED, workOrderEstimate.getId());
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
		 
		 List<StateHistory> history=null;
			if(contractorBillRegister!=null && contractorBillRegister.getCurrentState()!=null && contractorBillRegister.getCurrentState().getHistory()!=null)
			history = contractorBillRegister.getCurrentState().getHistory();
		 
		 workCompletionInfo=new WorkCompletionInfo(workOrderEstimate,mbNumbers);
		 workCompletionInfo.setWorkCommencedOn(workCommencedDate);
		 workCompletionInfo.setWorkflowHistory(history);
		 return workCompletionInfo;
	 }
	 
	 public List<WorkCompletionDetailInfo> setWorkCompletionDetailInfoList(WorkOrderEstimate workOrderEstimate){
		 List<WorkCompletionDetailInfo> workCompletionDetailInfoList=new ArrayList<WorkCompletionDetailInfo>();
		 TenderResponse tenderResponse = tenderResponseService.find("from TenderResponse tr where tr.negotiationNumber=?",workOrderEstimate.getWorkOrder().getNegotiationNumber());
		 String rebatePremLevel = worksService.getWorksConfigValue("REBATE_PREMIUM_LEVEL");
		

		 List<Object[]> workOrderActivityIdList=(List<Object[]>)genericService.findAllByNamedQuery(WorksConstants.QUERY_GETALLWORKORDERACTIVITYWITHMB, workOrderEstimate.getId(),WorksConstants.APPROVED);
			for(Object[] object:workOrderActivityIdList){
				WorkOrderActivity woa=(WorkOrderActivity)genericService.find("from WorkOrderActivity woa where woa.id=?",Long.parseLong(object[0].toString()));
				WorkCompletionDetailInfo workCompletionDetailInfo = new WorkCompletionDetailInfo(woa,Double.parseDouble(object[1].toString()));
				double executionRate=0.0;
				
					List<String> tenderTypeList=worksService.getTendertypeList();
					if (tenderTypeList!=null && !tenderTypeList.isEmpty()
						&& tenderResponse.getTenderEstimate().getTenderType()
								.equals(tenderTypeList.get(0))
						&& rebatePremLevel.equalsIgnoreCase(WorksConstants.BILL)) {
						double rebpremRate=woa.getApprovedRate()*(Math.abs(tenderResponse.getPercNegotiatedAmountRate())/100);
						if(tenderResponse.getPercNegotiatedAmountRate()>0){
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
			  		workCompletionDetailInfo.setTenderAmount((woa.getActivity().getQuantity()*woa.getScheduleOfRate())/result);

			  		workCompletionDetailInfo.setExecutionAmount((executionRate*(Double.parseDouble(object[1].toString())))/result);
			
				}
				workCompletionDetailInfoList.add(workCompletionDetailInfo);
			 }
			 List<WorkOrderActivity> workOrderActivityWithoutMBList=(List<WorkOrderActivity>)genericService.
			 		findAllByNamedQuery(WorksConstants.QUERY_GETALLWORKORDERACTIVITYWITHOUTMB, workOrderEstimate.getId(),WorksConstants.CANCELLED_STATUS,workOrderEstimate.getId(),WorksConstants.APPROVED);
			 for(WorkOrderActivity woa:workOrderActivityWithoutMBList){
				WorkCompletionDetailInfo workCompletionDetailInfo = new WorkCompletionDetailInfo(woa,Double.parseDouble("0"));
				workCompletionDetailInfo.setTenderAmount(woa.getApprovedRate()*woa.getApprovedQuantity());
				workCompletionDetailInfo.setExecutionAmount(Double.parseDouble("0"));
				workCompletionDetailInfoList.add(workCompletionDetailInfo);
			 }
			
		 return workCompletionDetailInfoList;
	 }
	
	/**
	 * This method will return Bill amount for a given Bill 
	 * @param workOrderId
	 * @return
	 */
	public BigDecimal getApprovedMBAmountforBill(ContractorBillRegister contractorBillRegister) {
		BigDecimal result = BigDecimal.ZERO; 
		Object[] params = new Object[]{WorksConstants.APPROVED,contractorBillRegister.getId()};
		List<MBHeader> approvedMBsForCancelledBillList=new ArrayList<MBHeader>();
		List<Object[]> approvedMBsList=new ArrayList<Object[]>();
		// NOTE -- Here we will not consider legacy MBs -- the named query below has been modified for this purpose
		approvedMBsList=genericService.findAllByNamedQuery("getMBAmountForBill", params);
		params = new Object[]{contractorBillRegister.getId(),WorksConstants.CANCELLED_STATUS};
		// NOTE -- Here also we will not consider legacy MBs -- the named query below has been modified for this purpose
		approvedMBsForCancelledBillList=genericService.findAllByNamedQuery("getMBListForCancelledBill", params);
		
			if(approvedMBsForCancelledBillList.isEmpty()){
				Double amount=0.0;
				Iterator iter1;
				Iterator iter2;
				List<Long> woaIdsListForApprovedMBs = new ArrayList<Long>();
				iter1 = approvedMBsList.iterator();
				
				while(iter1.hasNext()){
					Object[] obj =  (Object[]) iter1.next();
					woaIdsListForApprovedMBs.add((Long) obj[0]);
				}
			
				List<WorkOrderActivity> woaListForApprovedMBs = getWorkOrderActivityListForIds(woaIdsListForApprovedMBs);
				iter2 = approvedMBsList.iterator();
					while(iter2.hasNext()){
						Object[] obj =  (Object[]) iter2.next();
						Long woaId = (Long) obj[0];
						double mbQuantity = (Double) obj[1];
						for(WorkOrderActivity woa : woaListForApprovedMBs){
							if(woaId.equals(woa.getId())){
								if(woa.getActivity().getNonSor()==null){
									amount = woa.getApprovedRate()*mbQuantity*woa.getConversionFactor();
								}
								else{
									amount=woa.getApprovedRate()*mbQuantity;
								}
									result=result.add(BigDecimal.valueOf(amount));
									break;
							}
							
						}
					}
			}
			else{
				for(MBHeader mbh:approvedMBsForCancelledBillList){
					List<MBDetails> mbdetails=new ArrayList<MBDetails>();
					mbdetails=mbh.getMbDetails();
					for(MBDetails mbd:mbdetails){ 
						Double amount=0.0;
						if(mbd.getWorkOrderActivity().getActivity().getNonSor()==null){
							amount=mbd.getWorkOrderActivity().getApprovedRate()*mbd.getQuantity()*mbd.getWorkOrderActivity().getConversionFactor();
						}
						else{
							amount=mbd.getWorkOrderActivity().getApprovedRate()*mbd.getQuantity();
						}
						result=result.add(BigDecimal.valueOf(amount));
					}
				}
			}
			return result;
	}
	
	/**
	 * This method will return Bill amount for a given Bill 
	 * @param workOrderId
	 * @return
	 */
	public BigDecimal getApprovedMBAmountOfTenderedItemsForBill(ContractorBillRegister contractorBillRegister) {
		BigDecimal result = BigDecimal.ZERO; 
		Object[] params = new Object[]{WorksConstants.APPROVED,contractorBillRegister.getId()};
		List<MBHeader> approvedMBsForCancelledBillList=new ArrayList<MBHeader>();
		List<Object[]> approvedMBsList=new ArrayList<Object[]>();
		// NOTE -- Here we will not consider legacy MBs -- the named query below has been modified for this purpose
		approvedMBsList=genericService.findAllByNamedQuery("getMBAmountForBill", params);
		params = new Object[]{contractorBillRegister.getId(),WorksConstants.CANCELLED_STATUS};
		// NOTE -- Here also we will not consider legacy MBs -- the named query below has been modified for this purpose
		approvedMBsForCancelledBillList=genericService.findAllByNamedQuery("getMBListForCancelledBill", params);
		
		Double amount=0.0;
			if(approvedMBsForCancelledBillList.isEmpty()){
				List<Long> woaIdsListForApprovedMBs = new ArrayList<Long>();
				Iterator iter1 = approvedMBsList.iterator();
				
				while(iter1.hasNext()){
					Object[] obj =  (Object[]) iter1.next();
					woaIdsListForApprovedMBs.add((Long) obj[0]);
				}
				
				List<WorkOrderActivity> woaListForApprovedMBs = getWorkOrderActivityListForIds(woaIdsListForApprovedMBs);
				Iterator iter2 = approvedMBsList.iterator();
				while(iter2.hasNext()){
					Object[] obj =  (Object[]) iter2.next();
					Long woaId = (Long) obj[0];
					double mbQuantity = (Double) obj[1];
					for(WorkOrderActivity woa : woaListForApprovedMBs){
						if(woaId.equals(woa.getId())){
							if(woa.getActivity().getNonSor()==null){
								amount = woa.getApprovedRate()*mbQuantity*woa.getConversionFactor();
							}
							else{
								amount=woa.getApprovedRate()*mbQuantity;
							}
							if(woa.getActivity().getRevisionType()==null){
								result=result.add(BigDecimal.valueOf(amount));
								break;
							}
						}
						
					}
					
				}
			}
			else{
				for(MBHeader mbh:approvedMBsForCancelledBillList){
					List<MBDetails> mbdetails=new ArrayList<MBDetails>();
					mbdetails=mbh.getMbDetails();
					for(MBDetails mbd:mbdetails){ 
		
						if(mbd.getWorkOrderActivity().getActivity().getNonSor()==null){
							amount=mbd.getWorkOrderActivity().getApprovedRate()*mbd.getQuantity()*mbd.getWorkOrderActivity().getConversionFactor();
						}
						else{
							amount=mbd.getWorkOrderActivity().getApprovedRate()*mbd.getQuantity();
						}
						if(mbd.getWorkOrderActivity().getActivity().getRevisionType()==null)
							result=result.add(BigDecimal.valueOf(amount));
					}
				}
			}
			return result;
	}
	
	 
	 private Map<String, Integer> getSpecialUoms() {
			return worksService.getExceptionSOR();
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
	public void setTenderResponseService(TenderResponseService tenderResponseService) {
		this.tenderResponseService = tenderResponseService;
	}

	public Double getTotalActualExpenseForProject(AbstractEstimate estimate,Date asonDate){
		Double totalExpense=0.0;
		if(estimate==null || asonDate==null){
			throw new EGOVRuntimeException("Invalid Arguments passed to getTotalActualExpenseForProject()");
		}
		else {
			logger.debug("Start of getTotalActualExpenseForProject() ||estimate="+estimate.getEstimateNumber()+"||asonDate=||"+asonDate);
		}

		if(estimate.getProjectCode()!=null && estimate.getProjectCode().getEgwStatus()!=null && PROJECT_STATUS_CLOSED.equalsIgnoreCase(estimate.getProjectCode().getEgwStatus().getCode()) && !DateConversionUtil.isBeforeByDate(asonDate,estimate.getProjectCode().getCompletionDate())){
			logger.debug("Project code <<"+estimate.getProjectCode().getCode()+">> is closed");
			totalExpense=estimate.getProjectCode().getProjectValue();
		}
		else {
			if(estimate.getProjectCode()!=null) {
				logger.debug("Project having project code <<"+estimate.getProjectCode().getCode()+">> is running");
			}
			else {
				logger.debug("Project having estimate number <<"+estimate.getEstimateNumber()+">> is in the workflow");
			}
			for(EgBillregister egbr:getListOfApprovedBillforEstimate(estimate,asonDate)) {
					totalExpense=totalExpense+egbr.getBillamount().doubleValue();
			}
		}
		if(estimate.getProjectCode()!=null) {
			logger.debug("Actual Expense for the project "+estimate.getProjectCode().getCode()+"||expense amount "+totalExpense);
		}
		logger.debug("End of getTotalActualExpenseForProject() ");
		return totalExpense==null?0.0d:totalExpense;
	}

	public List<EgBillregister> getListOfApprovedBillforEstimate(AbstractEstimate estimate, Date date) {
		List<EgBillregister> egBillRegisterList=null;
		Query query=null;
		if(estimate==null || date==null){
			throw new EGOVRuntimeException("Invalid Arguments passed to getApprovedBillAmountforEstimate()");
		}
		else {
			logger.debug("Arguments passed to getListOfApprovedBillforEstimate() ||estimate "+estimate.getEstimateNumber()+"||date="+date);
		}
		if(estimate.getDepositCode()!=null){
			logger.debug("Estimate is of DEPOSIT WORKS|| estimate Number "+estimate.getEstimateNumber());
			query=persistenceService.getSession().createQuery("select distinct egbr from MBHeader as mbh left outer join mbh.egBillregister egbr left outer join egbr.egBillregistermis egbrmis where mbh.workOrderEstimate.estimate.id=:estimateId and egbr.status.code=:code and trunc(egbr.billdate)<=trunc(:date) ");
			query.setLong("estimateId", estimate.getId());
			query.setDate("date", date);
			query.setString("code", "APPROVED");
			egBillRegisterList=(List<EgBillregister>) query.list();
		}
		else{
			logger.debug("Estimate is of CAPITAL WORKS|| estimate Number "+estimate.getEstimateNumber());
			query=persistenceService.getSession().createQuery("select distinct egbr from MBHeader as mbh left outer join mbh.egBillregister egbr where mbh.workOrderEstimate.estimate.id=:estimateId " +
					"and egbr.status.code=:code and trunc(egbr.billdate)<=trunc(:date) ");
			query.setLong("estimateId", estimate.getId());
			query.setDate("date", date);
			query.setString("code", "APPROVED");
			egBillRegisterList=(List<EgBillregister>) query.list();
		}
		if(egBillRegisterList==null) {
			egBillRegisterList=Collections.EMPTY_LIST;
		}

		logger.debug("Number of Approved bills for ||estimate "+estimate.getEstimateNumber()+"||date="+date+"||is "+egBillRegisterList.size());
		logger.debug(">>>>>>End of getListOfApprovedBillforEstimate()>>>>>>");
		return egBillRegisterList;
		
	}
	//for multiyear estimate appropriation
	public BigDecimal getBilledAmountForDate(AbstractEstimate estimate,Date asOnDate){
		logger.debug("<<<<<<<<<<<<<<< Start of getBilledAmountForDate(AbstractEstimate estimate,Date asOnDate >>>>>>>>>>>>>");
		if(estimate==null || asOnDate==null){
			throw new EGOVRuntimeException("Invalid Arguments passed to getApprovedBillAmountforEstimate()");
		}
		else {
			logger.debug("Arguments passed to getBilledAmountForDate(AbstractEstimate estimate,Date asOnDate) ||estimate "+estimate+"||asOnDate="+ asOnDate);
		}
		List<Map<String, String>>  voucherDetails=egovCommon.getExpenditureDetailsforProject(estimate.getProjectCode().getId(), asOnDate);
		logger.debug("total voucher created for project code  <<"+estimate.getProjectCode().getCode()+">> is "+voucherDetails);
		ArrayList<String> voucherNumbers=new ArrayList<String>();
		BigDecimal totalVoucherAmount=BigDecimal.ZERO;
		if(voucherDetails!=null && voucherDetails.size()>0){
			for (Map<String,String> voucher:voucherDetails) {
				voucherNumbers.add(voucher.get("VoucherNumber"));
				totalVoucherAmount=totalVoucherAmount.add(new BigDecimal(Double.parseDouble(voucher.get("Amount"))));
			}
		}
		logger.debug("Total amount of vouchers(Contractor bills including overheads) | "+totalVoucherAmount);
		String queryString="select sum(egbr.billamount) from MBHeader as mbh left outer join mbh.egBillregister egbr left outer join egbr.egBillregistermis egbrmis where mbh.workOrderEstimate.estimate.id=:estimateId " +
			"and trunc(egbr.billdate)<=trunc(:date) and egbr.status.code=:code";
		if(voucherNumbers.size()>0) {
			queryString=queryString + " and egbrmis.voucherHeader.voucherNumber not in (:voucherNumbers)";
		}
		queryString=queryString+" group by mbh.workOrderEstimate.estimate.id";
		
		Query query=persistenceService.getSession().createQuery(queryString);
		query.setLong("estimateId", estimate.getId());
		query.setDate("date",new Date());
		query.setString("code", "APPROVED");
		if(voucherNumbers.size()>0) {
			query.setParameterList("voucherNumbers", voucherNumbers);
		}
		BigDecimal totalBillAmount=(BigDecimal)query.uniqueResult();

		logger.debug("Total amount of contractor bills (Vouchers amount not included in this contractor bill amount) | "+totalBillAmount);

		if(totalBillAmount==null){
			totalBillAmount=BigDecimal.ZERO;
		}
		logger.debug("End of getBilledAmountForDate(AbstractEstimate estimate,Date asOnDate) ||returned value is (including voucher amount and contractor bill)"+totalBillAmount.add(totalVoucherAmount));

		return totalBillAmount.add(totalVoucherAmount);
	}
	

	public BigDecimal getBilledAmount(AbstractEstimate estimate){
		if(estimate==null){
			throw new EGOVRuntimeException("Invalid Arguments passed to getApprovedBillAmountforEstimate()");
		}
		logger.debug("Arguments passed to getBilledAmount(AbstractEstimate estimate) ||estimate "+estimate.getEstimateNumber()+"||today date="+ new Date());
		List<Map<String, String>>  voucherDetails=egovCommon.getExpenditureDetailsforProjectforFinYear(estimate.getProjectCode().getId(), new Date());
		logger.debug("total voucher created for project code  <<"+estimate.getProjectCode().getCode()+">> is "+voucherDetails);
		ArrayList<String> voucherNumbers=new ArrayList<String>();
		BigDecimal totalVoucherAmount=BigDecimal.ZERO;
		if(voucherDetails!=null && voucherDetails.size()>0){
			for (Map<String,String> voucher:voucherDetails) {
				voucherNumbers.add(voucher.get("VoucherNumber"));
				totalVoucherAmount=totalVoucherAmount.add(new BigDecimal(Double.parseDouble(voucher.get("Amount"))));
			}
		}
		logger.debug("Total amount of vouchers(Contractor bills including overheads) | "+totalVoucherAmount);
		String queryString="select sum(egbr.billamount) from MBHeader as mbh left outer join mbh.egBillregister egbr left outer join egbr.egBillregistermis egbrmis where mbh.workOrderEstimate.estimate.id=:estimateId " +
			"and EXISTS (select 'true' from CFinancialYear cfinancialyear where trunc(cfinancialyear.startingDate)<=trunc(:date) and trunc(cfinancialyear.endingDate)>=trunc(:date) " +
			"and cfinancialyear.id=egbrmis.financialyear.id) and egbr.status.code=:code";
		if(voucherNumbers.size()>0) {
			queryString=queryString + " and egbrmis.voucherHeader.voucherNumber not in (:voucherNumbers)";
		}
		queryString=queryString+" group by mbh.workOrderEstimate.estimate.id";
		
		Query query=persistenceService.getSession().createQuery(queryString);
		query.setLong("estimateId", estimate.getId());
		query.setDate("date",new Date());
		query.setString("code", "APPROVED");
		if(voucherNumbers.size()>0) {
			query.setParameterList("voucherNumbers", voucherNumbers);
		}
		BigDecimal totalBillAmount=(BigDecimal)query.uniqueResult();

		logger.debug("Total amount of contractor bills (Vouchers amount not included in this contractor bill amount) | "+totalBillAmount);

		if(totalBillAmount==null){
			totalBillAmount=BigDecimal.ZERO;
		}
		logger.debug("End of getBilledAmount(AbstractEstimate estimate) ||returned value is (including voucher amount and contractor bill)"+totalBillAmount.add(totalVoucherAmount));

		return totalBillAmount.add(totalVoucherAmount);
	}
	
	public List<EgBillregister> getListOfNonCancelledBillsforEstimate(AbstractEstimate estimate, Date date) {
		List<EgBillregister> egBillRegisterList=null;
		Query query=null;
		if(estimate==null || date==null){
			throw new EGOVRuntimeException("Invalid Arguments passed to getApprovedBillAmountforEstimate()");
		}
		else {
			logger.debug("Arguments passed to getListOfApprovedBillforEstimate() ||estimate "+estimate.getEstimateNumber()+"||date="+date);
		}
		if(estimate.getDepositCode()!=null){
			logger.debug("Estimate is of DEPOSIT WORKS|| estimate Number "+estimate.getEstimateNumber());
			query=persistenceService.getSession().createQuery("select egbr from MBHeader as mbh left outer join mbh.egBillregister egbr left outer join egbr.egBillregistermis egbrmis where mbh.workOrderEstimate.estimate.id=:estimateId and egbr.status.code!=:code and trunc(egbr.billdate)<=trunc(:date) ");
			query.setLong("estimateId", estimate.getId());
			query.setDate("date", date);
			query.setString("code", "CANCELLED");
			egBillRegisterList=(List<EgBillregister>) query.list();
		}
		else{
			logger.debug("Estimate is of CAPITAL WORKS|| estimate Number "+estimate.getEstimateNumber());
			query=persistenceService.getSession().createQuery("select egbr from MBHeader as mbh left outer join mbh.egBillregister egbr where mbh.workOrderEstimate.estimate.id=:estimateId " +
					"and egbr.status.code!=:code and trunc(egbr.billdate)<=trunc(:date) ");
			query.setLong("estimateId", estimate.getId());
			query.setDate("date", date);
			query.setString("code", "CANCELLED");
			egBillRegisterList=(List<EgBillregister>) query.list();
		}
		if(egBillRegisterList==null) {
			egBillRegisterList=Collections.EMPTY_LIST;
		}

		logger.debug("Number of Approved bills for ||estimate "+estimate.getEstimateNumber()+"||date="+date+"||is "+egBillRegisterList.size());
		logger.debug(">>>>>>End of getListOfApprovedBillforEstimate()>>>>>>");
		return egBillRegisterList;
		
	}
	private List<WorkOrderActivity> getWorkOrderActivityListForIds(List<Long> woaIds){
		Query createQuery = persistenceService.getSession().createQuery(" from WorkOrderActivity woa where woa.id in (:woActivityIds) ");
		createQuery.setParameterList("woActivityIds",woaIds);
		 return createQuery.list();
	}

	public void setContractorAdvanceService(
			ContractorAdvanceService contractorAdvanceService) {
		this.contractorAdvanceService = contractorAdvanceService;
	}
	
	public String getBudgetHeadFromMappingObject(String depositCOA){
		String mappingGLCode = (String) genericService.find("select workDoneBudgetGroup from DepositCOABudgetHead where depositCOA = ?", depositCOA);
		return mappingGLCode;
	}
	
	public List<CChartOfAccounts> getBudgetHeadForDepositCOA(AbstractEstimate estimate){
		List<CChartOfAccounts> coaList = new ArrayList<CChartOfAccounts>();
		String estimateDepositCOA = estimate.getFinancialDetails().get(0).getCoa().getGlcode();
		String mappingGLCode = getBudgetHeadFromMappingObject(estimateDepositCOA); 
		if(StringUtils.isNotBlank(mappingGLCode)){
			coaList = Arrays.asList(commonsService.getCChartOfAccountsByGlCode(mappingGLCode));
		}
		return coaList;
	}
	
	public String validateForBudgetHeadInWorkflow(Set<EgBilldetails> billDetails,AbstractEstimate estimate){
		String allowForward = WorksConstants.YES;
		String mappingBudgetHead = getBudgetHeadFromMappingObject(estimate.getFinancialDetails().get(0).getCoa().getGlcode());
		if(StringUtils.isNotBlank(mappingBudgetHead)){
			for(EgBilldetails details : billDetails){
				if(details.getDebitamount()!=null && ((details.getDebitamount().compareTo(BigDecimal.ZERO))==1)){
					CChartOfAccounts coaObj = commonsService.getCChartOfAccountsById(Long.valueOf(details.getGlcodeid().toString()));
					if(coaObj!=null && StringUtils.isNotBlank(coaObj.getGlcode())){
						if(!mappingBudgetHead.equalsIgnoreCase(coaObj.getGlcode())){
							allowForward = WorksConstants.NO;
							break;
						}
					}
				}
			}
		}
		return allowForward;
	}
	
	/**
	 * @description -This method returns the list of project code ids for a fund-coa-deposit code combination
	 * @param - search fundId, coaId, depositCodeId
	 * @return - returns list of project codes
	 */
	public List<Integer> getProjCodeIdsListForDepositCode(Integer fundId,Long coaId,Long depositCodeId){
		List<Long> pcIds = genericService.findAllBy("select distinct fd.abstractEstimate.projectCode.id from FinancialDetail fd where fd.abstractEstimate.egwStatus.code = ?"
				+ " and fd.abstractEstimate.depositCode.id = ? and fd.fund.id = ? and fd.coa.id = ?",AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString(),
				depositCodeId,fundId,coaId);
		List<Integer> projCodeIds = new ArrayList<Integer>();
		if(pcIds!=null && !pcIds.isEmpty()){
			for(Long id:pcIds){
				projCodeIds.add(id.intValue());
			}
		}
		return projCodeIds;
	}
	
	/**
	 * @description -This method returns the total expenditure incurred for the project codes
	 * @param - search projectCodeIdsList, accDetailType
	 * @return - returns expenditure incurred
	 */
	
	public BigDecimal getTotalExpenditure(List<Integer> projectCodeIdsList,String accDetailType){
		BigDecimal totalExpenditureAmount = BigDecimal.ZERO;
		Integer accDetailTypeId = (Integer) genericService.find("select id from Accountdetailtype where name=?", accDetailType);
		BigDecimal totalBillAmt = getTotalBillAmount(new Date(),projectCodeIdsList,accDetailTypeId);
		BigDecimal voucherExpdAmount = egovCommon.getVoucherExpenditureByEntities(accDetailTypeId,projectCodeIdsList);
		BigDecimal dbpExpdAmount = egovCommon.getDirectBankPaymentExpenditureByEntities(accDetailTypeId,projectCodeIdsList);
		
		totalExpenditureAmount = totalBillAmt.add(voucherExpdAmount).add(dbpExpdAmount);
		return totalExpenditureAmount;
	}
	
	/**
	 * @description -This method calculates the sum of bill amount for bills where voucher is not present
	 * @param - search current date, list of project code ids, accDetailTypeId
	 * @return - returns sum of bill amount
	 */
	public BigDecimal getTotalBillAmount(Date asOnDate,List<Integer> projectCodeIdsList,Integer accDetailTypeId){
		List billAmountResult;
		BigDecimal totalBillAmount=BigDecimal.ZERO;

		String payQuery=" select nvl(sum(br.BILLAMOUNT),0) as \"Total Bill Amount\" FROM EG_BILLPAYEEDETAILS bpd, EG_BILLDETAILS bd, EG_BILLREGISTER br, EG_BILLREGISTERMIS mis "
				+" WHERE bpd.BILLDETAILID = bd.ID AND bd.BILLID = br.ID AND br.ID = mis.BILLID AND br.BILLSTATUS != '"+WorksConstants.CANCELLED_STATUS+"' "
				+ "AND bpd.ACCOUNTDETAILTYPEID=(SELECT ID FROM ACCOUNTDETAILTYPE WHERE NAME='PROJECTCODE') AND bpd.ACCOUNTDETAILKEYID IN (:projCodeIds) AND TRUNC(br.BILLDATE) <=:date "
				+ "AND ((mis.VOUCHERHEADERID   IS NULL) OR (mis.VOUCHERHEADERID IS NOT NULL and EXISTS (select id from voucherheader where id=mis.VOUCHERHEADERID and status="+FinancialConstants.CANCELLEDVOUCHERSTATUS+")))";
				
		
		Query query = persistenceService.getSession().createSQLQuery(payQuery);
		query.setParameterList("projCodeIds",projectCodeIdsList);
		query.setDate("date", asOnDate);
		billAmountResult = query.list();
		
		for(Object obj : billAmountResult){
			totalBillAmount = BigDecimal.valueOf(Double.valueOf(obj.toString()));
		}
		return totalBillAmount;
	}
	
	public Object[] getLatestMBCreatedDateAndRefNo(Long woId , Long estId){
		Object[] mbDateRefNo = (Object[]) persistenceService.getSession()
				.createQuery("select mbRefNo,mbDate from MBHeader where id = "
						+ "(select max(mbh.id) from MBHeader mbh where mbh.egwStatus.code = ? and "
						+ "mbh.workOrder.id= ? and mbh.workOrderEstimate.estimate.id=? and "
						+ "mbh.workOrderEstimate.estimate.egwStatus.code= ? )")
					.setParameter(0,WorksConstants.APPROVED)
						.setParameter(1, woId)
							.setParameter(2,estId)
								.setParameter(3, WorksConstants.ADMIN_SANCTIONED_STATUS)
									.uniqueResult();
		return mbDateRefNo;
	}
}
