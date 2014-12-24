package org.egov.works.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.assets.model.Asset;
import org.egov.assets.model.CapitaliseAsset;
import org.egov.commons.CFinancialYear;
import org.egov.exceptions.EGOVException;
import org.egov.infstr.models.EgChecklists;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.works.models.contractorBill.AssetForBill;
import org.egov.works.models.contractorBill.ContractorBillRegister;
import org.egov.works.models.contractorBill.DeductionTypeForBill;
import org.egov.works.models.contractorBill.StatutoryDeductionsForBill;
import org.egov.works.models.contractorBill.WorkCompletionDetailInfo;
import org.egov.works.models.contractorBill.WorkCompletionInfo;
import org.egov.works.models.measurementbook.MBForCancelledBill;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;


/**
 * This class will have all business logic related to Contractor Bill.
 * @author Sathish P
 *
 */
public interface ContractorBillService extends BaseService<ContractorBillRegister,Long> {
	
	/**
	 * This method will get the bill type from App Config Value.	
	 * @return
	 */
	List getBillType();
	
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
	boolean contractorBillNumberChangeRequired(EgBillregister bill, WorkOrder workOrder, CFinancialYear financialYear);
	
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
	public String generateContractorBillNumber(EgBillregister bill, WorkOrder workOrder, WorkOrderEstimate workOrderEstimate);

	/**
	 * Get utilized amount amount for a given workorder, including approved, unapproved bill(Bill other than cancelled and approved)
	 * and approved MB
	 * @param workOrderId
	 * @return
	 */
	//BigDecimal getTotalUtilizedAmount(Long workOrderId,Date asOnDate);
	
	/**
	 * Get utilized amount amount for a given workorder in approved Bill.
	 * @return
	 */
	//BigDecimal getUtilizedAmountForBill(Long workOrderId,Date asOnDate);
	
	/**
	 * Get sum of all bill of given work orders which are still unapproved.
	 * @param workOrderId
	 * @return
	 */
	BigDecimal getUtlizedAmountForUnArrovedBill(Long workOrderId,Date asOnDate);
	
	/**
	 * This method will return cumulative amount for all approved MB for a given workorder 
	 * @param workOrderId
	 * @return
	 */
	BigDecimal getApprovedMBAmount(Long workOrderId,Long estimateId, Date asOnDate);
	/**
	 * The method return BigDecimal 
	 * 
	 * @param egBillregister an instance of <code>EgBillregister</code>.
	 * 
	 * @param workOrder an instance of <code>WorkOrder</code>.
	 * 
	 * @return a BigDecimal value indicating contractor total pending balance.
	 */
	public BigDecimal calculateContractorTotalPendingBalance(Date billDate,WorkOrder workOrder, WorkOrderEstimate workOrderEstimate, Long billId );
	
	/**
	 * API will returns the Standard deduction types as key and its mapped COA as map values
	 * @return map containing deduction type as key and string array of coa glcodes 
	 */
	public Map<String,String[]> getStandardDeductionsFromConfig();
	
	/**
	 * returns the sanctioned budget for the year
	 * @param paramMap
	 * @return
	 * @throws ValidationException
	 */
	//public BigDecimal getBudgetedAmtForYear(Long workOrderId,Date asOnDate) throws ValidationException;

	
	/**
	 * Get the list of eligible bills based on parameters provided
	 * @param paramsMap
	 * @return
	 */
	List<ContractorBillRegister> searchContractorBill(Map<String, Object> paramsMap);

	
	
	/**
	 * API will returns the Total value for the workorder upto billdate
	 * @return BigDecimal
	 */
	public BigDecimal getTotalValueWoForUptoBillDate(Date billDate,Long workOrderId, Long workOrderEstimateId);	
	
	/**
	 * API will returns the Total value for the workorder excluding the withheld release amount upto billdate
	 * @return BigDecimal
	 */
	public BigDecimal getWoValueExcludingWitheldReleaseAmt(Date billDate,Long workOrderId, Long workOrderEstimateId);

	/**
	 * API will returns the required order deduction names list from appconfig based on key such as statutory/standard deduction
	 * @return List containing statutory deduction names
	 */
	public List<String> getSortedDeductionsFromConfig(String Key);
	
	
	/**
	 * API will returns the type of Deduction list for a given bill Id and type
	 * @return List containing Statutory deduction names
	 */
	public List<StatutoryDeductionsForBill> getStatutoryListForBill(Long billId);
	
	/**
	 * API will returns the type of Deduction list for a given bill Id and type
	 * @return List containing Statutory deduction names
	 */
	public List<StatutoryDeductionsForBill> getStatutoryDeductionSortedOrder(List<String> requiredOrder,List<StatutoryDeductionsForBill> givenStatutoryList);

	
    /**
	   * API will returns the standard deduction list for a given bill Id 
	   * @return List containing Statutory deduction names
	*/
	public List<DeductionTypeForBill> getStandardDeductionForBill(Long billId);
	
	/**
	 * API will returns the sorted Deduction list for a given bill Id 
	 * @return List containing Statutory deduction names
	 */
	public List<DeductionTypeForBill> getStandardDeductionSortedOrder(List<String> requiredOrder,List<DeductionTypeForBill> givenStandardList);
	
	
	/**
	 * API will returns the AssetForBill list for a given bill Id 
	 * @return List containing AssetForBill
	 */
	public List<AssetForBill> getAssetForBill(Long billId);
	
	/**
	 * API will returns the Advance adjustment amount for a given bill Id and glcode
	 * @return  BigDecimal 
	 */
	public BigDecimal getAdvanceAdjustmentAmountForBill(Long billId);
	
	
	/**
	 * API will returns the custom deduction list of egbilldetails excluding glcode
	 * @return  BigDecimal 
	 */
	public List<EgBilldetails> getCustomDeductionListforglcodes(List<BigDecimal> glcodeIdList,Long billId);
	
	/**
	 * API will returns the Net Payable Amount for netpayable code coaId
	 * @return  BigDecimal 
	 */
	public BigDecimal getNetPayableAmountForGlCodeId(Long billId) throws NumberFormatException, EGOVException;
	
	/**
	 * API will returns the Total Amount for Statutory deduction for workorder upto billdate for that dedcution
	 * @return  BigDecimal 
	 */
	public BigDecimal getTotAmtForStatutory(Date billDate, Long workOrderId,StatutoryDeductionsForBill statDeductionBilldetail,Long workOrderEstimateId);
	
	/**
	 * API will returns the Total Amount for advanceAjustment deduction for workorder upto billdate
	 * @return  BigDecimal 
	 */
	public BigDecimal getTotAmtForAdvanceAdjustment(Date billDate, Long workOrderId,Long workOrderEstimateId);	
	
	/**
	 * API will returns the Total Amount for custom deduction for workorder upto billdate
	 * @return  BigDecimal 
	 */
	public BigDecimal getTotAmtForStandard(Date billDate,Long workOrderId,DeductionTypeForBill deductionTypeForBill,Long workOrderEstimateId);
	
	/**
	 * API will returns the Total Amount for custom deduction for workorder upto billdate
	 * @return  BigDecimal 
	 */
	public BigDecimal getTotAmtForCustom(Date billDate,Long workOrderId,EgBilldetails egBilldetails,Long workOrderEstimateId);
	

	/**
	 * Get the list of custom dedcution based on glcodes of custom deduction
	 * @param ContractorBillRegister
	 * @return List
	 * @throws EGOVException 
	 * @throws NumberFormatException 
	 */
	public List<EgBilldetails> getCustomDeductionList(Long billId,List<StatutoryDeductionsForBill> statutoryList,List<DeductionTypeForBill> standardDeductionList, List<EgBilldetails> retentionMoneyDeductionList) throws NumberFormatException, EGOVException;
	
	/**
	 * Get the list of retention money dedcution based on glcodes of retention deduction
	 * @param ContractorBillRegister
	 * @return List
	 * @throws EGOVException 
	 * @throws NumberFormatException 
	 */
	public List<EgBilldetails> getRetentionMoneyDeductionList(Long billId,List<StatutoryDeductionsForBill> statutoryList,List<DeductionTypeForBill> standardDeductionList) throws NumberFormatException, EGOVException;
	
	/**
	 * Get the netpayblecode
	 * @param billId,glcodeIdList
	 * @return BigDecimal
	 * @throws EGOVException 
	 * @throws NumberFormatException 
	 */
	public BigDecimal getNetPaybleCode(Long billId)throws Exception;
	
	
	/**
	 * Get the mbList
	 * @param workOrderId, billid
	 * @return List
	 */
	public List<MBHeader>  getMbListForBillAndWorkordrId(Long workOrderId,Long billId);
	
	/**
	 * Get the mbList
	 * @param workOrderId, billid
	 * @return List
	 */
	public List<MBForCancelledBill>  getMbListForCancelBill(Long billId);
	
	
	/**
	 * Get the accountDetails
	 * @param statutoryList,standardDeductionList,customDeductionList
	 * @return List
	 */
	public List<EgBilldetails> getAccountDetailsList(Long billId,List<StatutoryDeductionsForBill> statutoryList,
			List<DeductionTypeForBill> standardDeductionList, List<EgBilldetails> customDeductionList, List<EgBilldetails> retentionMoneyDeductionList,List<EgBilldetails> releaseWithHeldAmountDeductionsList) 
			throws NumberFormatException, EGOVException;
	
	
	/**
	 * 
	 * @param statutoryList,standardDeductionList,customDeductionList
	 * @return 
	 */
	public void setAllViewLists(Long id,Long workOrderId,List<StatutoryDeductionsForBill> actionStatutorydetails,
			List<DeductionTypeForBill> standardDeductions, List<EgBilldetails> customDeductions, List<EgBilldetails> retentionMoneyDeductions,
			List<AssetForBill>	accountDetailsForBill,List<EgBilldetails> releaseWithHeldAmountDeductions) 
	throws NumberFormatException, EGOVException;
	
	/**
	 * Get the EgChecklists
	 * @param billid
	 * @return List
	 */
	public List<EgChecklists> getEgcheckList(Long billId) 
			throws NumberFormatException, EGOVException;
	
	/**
	 * Get the WorkCompletionInfo for completion certificate
	 * @param ContractorBillRegister
	 * @param WorkOrderEstimate
	 * @return WorkCompletionInfo
	 */
	public WorkCompletionInfo setWorkCompletionInfoFromBill(ContractorBillRegister contractorBillRegister,WorkOrderEstimate workOrderEstimate);
	
	/**
	 * Get the List of WorkCompletionDetailInfo for completion certificate
	 * @param WorkOrderEstimate
	 * @return List<WorkCompletionDetailInfo>
	 */
	public List<WorkCompletionDetailInfo> setWorkCompletionDetailInfoList(WorkOrderEstimate workOrderEstimate);
	
	
	/**
	 * Get the List of Assets for capitalisation 
	 * @param Map<String, Object> paramsMap
	 * @return List<Object[]>
	 */
	public List<CapitaliseAsset> getAssetsForCapitalisationList(Map<String, Object> paramsMap);
	
	/**
	 * Capitalise Asset 
	 * @param Asset asset that has to capitalise
	 */
	public void capitaliseAsset(Asset asset, Date dateOfCapitalisation,BigDecimal assetIndrctExpns,Integer projectType);
	
	/**
	 * Get details for Capitalise Asset 
	 * @param Asset asset that has to capitalise
	 */
	public List<AssetForBill> getAssetCapitalisationDetails(Asset asset);

	/**
	 * This method will return Bill amount for a given BIll  
	 * @param billId
	 * @return
	 */
	public BigDecimal getApprovedMBAmountforBill(ContractorBillRegister contractorBillRegister); 
	
	/**
	 * API will returns the release with held list of egbilldetails excluding glcode
	 * @return  BigDecimal 
	 */
	public List<EgBilldetails> getReleaseWithHoldAmountListforglcodes(List<BigDecimal> glcodeIdList,Long billId);
	
	/**
	 * API will returns the Total Released WithHeld Amount  for workorder upto billdate
	 * @return  BigDecimal 
	 */
	public BigDecimal getTotReleasedWHAmt(Date billDate,Long workOrderId,EgBilldetails egBilldetails1,Long workOrderEstimateId);
	
	
	/**
	 * Get the WorkCompletionInfo for contract certificate
	 * @param ContractorBillRegister
	 * @param WorkOrderEstimate
	 * @return WorkCompletionInfo 
	 */
	public WorkCompletionInfo setWorkContractCertInfoFromBill(ContractorBillRegister contractorBillRegister,WorkOrderEstimate workOrderEstimate);
	
	/**
	 * Get the List of WorkCompletionDetailInfo for contract certificate
	 * @param ContractorBillRegister
	 * @param WorkOrderEstimate
	 * @return List<WorkCompletionDetailInfo>
	 */
	public List<WorkCompletionDetailInfo> setWorkContractCertDetailInfoList(ContractorBillRegister contractorBillRegister,WorkOrderEstimate workOrderEstimate);

	public String generateContractCertificateNumber(EgBillregister bill, WorkOrderEstimate workOrderEstimate);

}
