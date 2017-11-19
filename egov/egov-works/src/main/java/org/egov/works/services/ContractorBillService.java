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

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.infra.exception.ApplicationException;
import org.egov.infstr.models.EgChecklists;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.models.contractorBill.AssetForBill;
import org.egov.works.models.contractorBill.DeductionTypeForBill;
import org.egov.works.models.contractorBill.StatutoryDeductionsForBill;
import org.egov.works.models.contractorBill.WorkCompletionDetailInfo;
import org.egov.works.models.contractorBill.WorkCompletionInfo;
import org.egov.works.models.measurementbook.MBForCancelledBill;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class will have all business logic related to Contractor Bill.
 *
 * @author Sathish P
 */
public interface ContractorBillService extends BaseService<ContractorBillRegister, Long> {

    /**
     * This method will get the bill type from App Config Value.
     *
     * @return
     */
    List getBillType();

    /**
     * The method return true if the bill number has to be re-generated
     *
     * @param bill an instance of <code>EgBillregister</code> containing the bill date
     * @param financialYear an instance of <code>CFinancialYear</code> representing the financial year for the estimate date.
     * @return a boolean value indicating if the bill number change is required.
     */
    boolean contractorBillNumberChangeRequired(EgBillregister bill, WorkOrder workOrder, CFinancialYear financialYear);

    /**
     * The method return number if the bill number has to be generated
     *
     * @param bill an instance of <code>EgBillregister</code> containing the bill date representing the financial year.
     * @param workOrder an instance of <code>WorkOrder</code> representing the executing department.
     * @return a boolean value indicating if the bill number change is required.
     */
    public String generateContractorBillNumber(ContractorBillRegister contractorBillRegister);

    /**
     * Get utilized amount amount for a given workorder, including approved, unapproved bill(Bill other than cancelled and
     * approved) and approved MB
     *
     * @param workOrderId
     * @return
     */
    // BigDecimal getTotalUtilizedAmount(Long workOrderId,Date asOnDate);

    /**
     * Get utilized amount amount for a given workorder in approved Bill.
     *
     * @return
     */
    // BigDecimal getUtilizedAmountForBill(Long workOrderId,Date asOnDate);

    /**
     * Get sum of all bill of given work orders which are still unapproved.
     *
     * @param workOrderId
     * @return
     */
    BigDecimal getUtlizedAmountForUnArrovedBill(Long workOrderId, Date asOnDate);

    /**
     * This method will return cumulative amount for all approved MB for a given workorder
     *
     * @param workOrderId
     * @return
     */

    BigDecimal getApprovedMBAmount(Long workOrderId, Long estimateId, Date asOnDate);

    /**
     * The method return BigDecimal
     *
     * @param totalAdvancePaid , billDate, workOrderEstimate
     * @param workOrder an instance of <code>WorkOrder</code>.
     * @return a BigDecimal value indicating total Advance pending for given Work Order Estimate as on bill date before this
     * current bill
     */
    public BigDecimal calculateTotalPendingAdvance(BigDecimal totalAdvancePaid, Date billDate,
            WorkOrderEstimate workOrderEstimate, Long billId);

    /**
     * API will returns the Standard deduction types as key and its mapped COA as map values
     *
     * @return map containing deduction type as key and string array of coa glcodes
     */
    public Map<String, String[]> getStandardDeductionsFromConfig();

    /**
     * returns the sanctioned budget for the year
     *
     * @param paramMap
     * @return
     * @throws ValidationException
     */
    // public BigDecimal getBudgetedAmtForYear(Long workOrderId,Date asOnDate)
    // throws ValidationException;

    /**
     * Get the list of eligible bills based on parameters provided
     *
     * @param paramsMap
     * @param paramList TODO
     * @return
     */
    List<String> searchContractorBill(Map<String, Object> paramsMap, List<Object> paramList);

    /**
     * API will returns the Total value for the workorder upto billdate
     *
     * @return BigDecimal
     */
    public BigDecimal getTotalValueWoForUptoBillDate(Date billDate, Long workOrderId, Long workOrderEstimateId);

    /**
     * API will returns the required order deduction names list from appconfig based on key such as statutory/standard deduction
     *
     * @return List containing statutory deduction names
     */
    public List<String> getSortedDeductionsFromConfig(String Key);

    /**
     * API will returns the type of Deduction list for a given bill Id and type
     *
     * @return List containing Statutory deduction names
     */
    public List<StatutoryDeductionsForBill> getStatutoryListForBill(Long billId);

    /**
     * API will returns the type of Deduction list for a given bill Id and type
     *
     * @return List containing Statutory deduction names
     */
    public List<StatutoryDeductionsForBill> getStatutoryDeductionSortedOrder(List<String> requiredOrder,
            List<StatutoryDeductionsForBill> givenStatutoryList);

    /**
     * API will returns the standard deduction list for a given bill Id
     *
     * @return List containing Statutory deduction names
     */
    public List<DeductionTypeForBill> getStandardDeductionForBill(Long billId);

    /**
     * API will returns the sorted Deduction list for a given bill Id
     *
     * @return List containing Statutory deduction names
     */
    public List<DeductionTypeForBill> getStandardDeductionSortedOrder(List<String> requiredOrder,
            List<DeductionTypeForBill> givenStandardList);

    /**
     * API will returns the AssetForBill list for a given bill Id
     *
     * @return List containing AssetForBill
     */
    public List<AssetForBill> getAssetForBill(Long billId);

    /**
     * API will returns the Advance adjustment amount for a given bill Id
     *
     * @return BigDecimal
     */
    public BigDecimal getAdvanceAdjustmentAmountForBill(Long billId, Long workOrderEstimateId);

    /**
     * API will returns the custom deduction list of egbilldetails excluding glcode
     *
     * @return BigDecimal
     */
    public List<EgBilldetails> getCustomDeductionListforglcodes(List<BigDecimal> glcodeIdList, Long billId);

    /**
     * API will returns the Net Payable Amount for netpayable code coaId
     *
     * @return BigDecimal
     */
    public BigDecimal getNetPayableAmountForGlCodeId(Long billId) throws NumberFormatException, ApplicationException;

    /**
     * API will returns the Total Amount for Statutory deduction for workorder upto billdate for that dedcution
     *
     * @return BigDecimal
     */
    public BigDecimal getTotAmtForStatutory(Date billDate, Long workOrderId,
            StatutoryDeductionsForBill statDeductionBilldetail, Long workOrderEstimateId);

    /**
     * API will returns the Total Amount for advanceAjustment deduction for work order estimate upto billdate
     *
     * @return BigDecimal
     */
    public BigDecimal getTotAmtForAdvanceAdjustment(Date billDate, Long workOrderId, Long workOrderEstimateId);

    /**
     * API will returns the Total Amount for custom deduction for workorder upto billdate
     *
     * @return BigDecimal
     */
    public BigDecimal getTotAmtForStandard(Date billDate, Long workOrderId, DeductionTypeForBill deductionTypeForBill,
            Long workOrderEstimateId);

    /**
     * API will returns the Total Amount for custom deduction for workorder upto billdate
     *
     * @return BigDecimal
     */
    public BigDecimal getTotAmtForCustom(Date billDate, Long workOrderId, EgBilldetails egBilldetails,
            Long workOrderEstimateId);

    /**
     * Get the list of custom dedcution based on glcodes of custom deduction
     *
     * @param billId ,workOrderEstimateId,statutoryList,standardDeductionList, retentionMoneyDeductionList
     * @return List
     * @throws ApplicationException
     * @throws NumberFormatException
     */
    public List<EgBilldetails> getCustomDeductionList(Long billId, Long workOrderEstimateId,
            List<StatutoryDeductionsForBill> statutoryList, List<DeductionTypeForBill> standardDeductionList,
            List<EgBilldetails> retentionMoneyDeductionList) throws NumberFormatException, ApplicationException;

    /**
     * Get the list of retention money dedcution based on glcodes of retention deduction
     *
     * @param ContractorBillRegister
     * @return List
     * @throws ApplicationException
     * @throws NumberFormatException
     */
    public List<EgBilldetails> getRetentionMoneyDeductionList(Long billId,
            List<StatutoryDeductionsForBill> statutoryList, List<DeductionTypeForBill> standardDeductionList)
                    throws NumberFormatException, ApplicationException;

    /**
     * Get the netpayblecode
     *
     * @param billId ,glcodeIdList
     * @return BigDecimal
     * @throws ApplicationException
     * @throws NumberFormatException
     */
    public BigDecimal getNetPaybleCode(Long billId) throws Exception;

    /**
     * Get the mbList
     *
     * @param workOrderId , billid
     * @return List
     */
    public List<MBHeader> getMbListForBillAndWorkordrId(Long workOrderId, Long billId);

    /**
     * Get the mbList
     *
     * @param workOrderId , billid
     * @return List
     */
    public List<MBForCancelledBill> getMbListForCancelBill(Long billId);

    /**
     * Get the accountDetails
     *
     * @param statutoryList ,standardDeductionList,customDeductionList,workOrderEstimateId
     * @return List
     */
    public List<EgBilldetails> getAccountDetailsList(Long billId, Long workOrderEstimateId,
            List<StatutoryDeductionsForBill> statutoryList, List<DeductionTypeForBill> standardDeductionList,
            List<EgBilldetails> customDeductionList, List<EgBilldetails> retentionMoneyDeductionList)
                    throws NumberFormatException, ApplicationException;

    /**
     * @param statutoryList ,standardDeductionList,customDeductionList,workOrderEstimateId ,workOrderId, id(billId)
     * @return
     */
    public void setAllViewLists(Long id, Long workOrderId, Long workOrderEstimateId,
            List<StatutoryDeductionsForBill> actionStatutorydetails, List<DeductionTypeForBill> standardDeductions,
            List<EgBilldetails> customDeductions, List<EgBilldetails> retentionMoneyDeductions,
            List<AssetForBill> accountDetailsForBill) throws NumberFormatException, ApplicationException;

    /**
     * Get the EgChecklists
     *
     * @param billid
     * @return List
     */
    public List<EgChecklists> getEgcheckList(Long billId) throws NumberFormatException, ApplicationException;

    /**
     * Get the WorkCompletionInfo for completion certificate
     *
     * @param ContractorBillRegister
     * @param WorkOrderEstimate
     * @return WorkCompletionInfo
     */
    public WorkCompletionInfo setWorkCompletionInfoFromBill(ContractorBillRegister contractorBillRegister,
            WorkOrderEstimate workOrderEstimate);

    /**
     * Get the List of WorkCompletionDetailInfo for completion certificate
     *
     * @param WorkOrderEstimate
     * @return List<WorkCompletionDetailInfo>
     */
    public List<WorkCompletionDetailInfo> setWorkCompletionDetailInfoList(WorkOrderEstimate workOrderEstimate);

    /**
     * This method will return Bill amount for a given BIll
     *
     * @param billId
     * @return
     */
    public BigDecimal getApprovedMBAmountforBill(ContractorBillRegister contractorBillRegister);

    /**
     * This method will return Bill amount for a given BIll considering only tendered items
     *
     * @param billId
     * @return
     */
    public BigDecimal getApprovedMBAmountOfTenderedItemsForBill(ContractorBillRegister contractorBillRegister);

    /**
     * This method returns total expense incurred including overheads for the project if project is closed till asonDate,otherwise
     * sum of all approved bills for the project till given date.
     *
     * @author vikas
     * @param estimate ,date
     * @return sum of total expense till date
     */
    public Double getTotalActualExpenseForProject(AbstractEstimate estimate, Date asonDate);

    /**
     * This method will return List of Approved Bill for a given Estimate and date
     *
     * @author vikas
     * @param estimate ,date
     * @return List of apporoved bills till date
     */
    public List<EgBillregister> getListOfApprovedBillforEstimate(AbstractEstimate estimate, Date date);

    /**
     * This method will return List of Not cancelled for a given Estimate and date
     *
     * @author julian.prabhakar
     * @param estimate ,date
     * @return List of approved bills till date
     */
    public List<EgBillregister> getListOfNonCancelledBillsforEstimate(AbstractEstimate estimate, Date date);

    /**
     * This method returns total billed amount including overheads for the project till today in the current financial year.
     *
     * @author vikas
     * @param admin sanctioned estimate
     * @return BigDecimal
     */

    public BigDecimal getBilledAmount(AbstractEstimate estimate);

    /**
     * This method returns total billed amount including overheads for the project till asOnDate across financial years.
     *
     * @author vikas
     * @param admin sanctioned estimate
     * @return BigDecimal
     */

    public BigDecimal getBilledAmountForDate(AbstractEstimate estimate, Date asOnDate);

    /**
     * This method will return cumulative tendered items amount for all approved MB for a given workorder
     *
     * @param workOrderId
     * @return
     */
    public BigDecimal getApprovedMBAmountOfTenderedItems(Long workOrderId, Long workOrderEstimateId, Date billDate);

    /**
     * This method will return the work done budget head from the mapping table, based on the deposit COA in the financial details
     * of the estimate
     *
     * @param estimate
     * @return List<CChartOfAccounts>
     */
    public List<CChartOfAccounts> getBudgetHeadForDepositCOA(AbstractEstimate estimate);

    /**
     * This method will set the flag which is used to hide the Forward/Approve buttons for the bills in workflow where the budget
     * heads do not match
     *
     * @param Set <EgBilldetails> billDetails, estimate
     * @return string
     */
    public String validateForBudgetHeadInWorkflow(Set<EgBilldetails> billDetails, AbstractEstimate estimate);

    /**
     * @description -This method returns the list of project code ids for a fund-coa-deposit code combination
     * @param - search fundId, coaId, depositCodeId
     * @return - returns list of project codes
     */
    public List<Integer> getProjCodeIdsListForDepositCode(Integer fundId, Long coaId, Long depositCodeId);

    /**
     * @description -This method returns the total expenditure incurred for the project codes
     * @param - search projectCodeIdsList, accDetailType
     * @return - returns expenditure incurred
     */
    public BigDecimal getTotalExpenditure(List<Integer> projectCodeIdsList, String accDetailType);

    /**
     * returns latest MB created date and its reference no
     *
     * @param estId ,woId
     * @return
     */
    public Object[] getLatestMBCreatedDateAndRefNo(Long woId, Long estId);

    public Collection<StatutoryDeductionsForBill> getStatutoryDeductions(List<StatutoryDeductionsForBill> actionStatutorydetails);

    public Collection<EgBilldetails> getCustomDeductionTypes(List<EgBilldetails> customDeductions);

    public Collection<EgBilldetails> getRetentionMoneyTypes(List<EgBilldetails> retentionMoneyDeductions);

    public Collection<AssetForBill> getAssetAndAccountDetails(List<AssetForBill> accountDetailsForBill);

    public Collection<DeductionTypeForBill> getStandardDeductionTypes(List<DeductionTypeForBill> standardDeductions);
}
