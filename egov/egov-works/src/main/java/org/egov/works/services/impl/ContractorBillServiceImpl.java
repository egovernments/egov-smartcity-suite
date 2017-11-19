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

package org.egov.works.services.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.dao.bills.EgBilldetailsHibernateDAO;
import org.egov.egf.commons.EgovCommon;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.models.EgChecklists;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.pims.commons.Position;
import org.egov.utils.FinancialConstants;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.autonumber.ContractorBillNumberGenerator;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.models.contractorBill.AssetForBill;
import org.egov.works.models.contractorBill.DeductionTypeForBill;
import org.egov.works.models.contractorBill.StatutoryDeductionsForBill;
import org.egov.works.models.contractorBill.WorkCompletionDetailInfo;
import org.egov.works.models.contractorBill.WorkCompletionInfo;
import org.egov.works.models.measurementbook.MBDetails;
import org.egov.works.models.measurementbook.MBForCancelledBill;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.tender.OfflineStatus;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ContractorBillServiceImpl extends BaseServiceImpl<ContractorBillRegister, Long> implements
        ContractorBillService {
    private static final Logger LOGGER = Logger.getLogger(ContractorBillServiceImpl.class);
    public static final String WORKORDER_NO = "WORKORDER_NO";
    public static final String CONTRACTOR_ID = "CONTRACTOR_ID";
    public static final String BILLSTATUS = "BILLSTATUS";
    public static final String BILLNO = "BILLNO";
    public static final String FROM_DATE = "FROM_DATE";
    public static final String TO_DATE = "TO_DATE";
    public static final String FROMDATE = "fromDate";
    public static final String TODATE = "toDate";
    public static final String PARAM = "param_";
    public static final String PROJECT_STATUS_CLOSED = "CLOSED";
    public static final String BILL_DEPT_ID = "BILL_DEPT_ID";
    public static final String EXEC_DEPT_ID = "EXEC_DEPT_ID";
    public static final String EST_NO = "EST_NO";

    private static final String WORKS_NETPAYABLE_CODE = "WORKS_NETPAYABLE_CODE";
    private static final String RETENTION_MONEY_PURPOSE = "RETENTION_MONEY_PURPOSE";
    private WorksService worksService;
    @Autowired
    private PersistenceService<EgChecklists, Long> checklistService;
    @Autowired
    private AutonumberServiceBeanResolver beanResolver;
    @Autowired
    private EgovCommon egovCommon;
    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;
    private TenderResponseService tenderResponseService;
    private ContractorAdvanceService contractorAdvanceService;

    @Autowired
    private EgBilldetailsHibernateDAO egBilldetailsHibernateDAO;

    public ContractorBillServiceImpl(final PersistenceService<ContractorBillRegister, Long> persistenceService) {
        super(persistenceService);
    }

    /**
     * Check if Contractor Bill entries are within approved limit or not.
     *
     * @param mbHeader
     * @return
     */
    @Override
    public List getBillType() {

        final String configVal = worksService.getWorksConfigValue("BILLTYPE");
        final List billTypeList = new LinkedList();

        if (StringUtils.isNotBlank(configVal)) {
            final String[] configVals = configVal.split(",");
            for (final String configVal2 : configVals)
                billTypeList.add(configVal2);
        }
        return billTypeList;

    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    /**
     * The method return true if the bill number has to be re-generated
     *
     * @param bill          an instance of <code>EgBillregister</code> containing the bill date
     * @param financialYear an instance of <code>CFinancialYear</code> representing the financial year for the estimate date.
     * @return a boolean value indicating if the bill number change is required.
     */
    @Override
    public boolean contractorBillNumberChangeRequired(final EgBillregister bill, final WorkOrder workOrder,
                                                      final CFinancialYear financialYear) {

        return true;
    }

    /**
     * The method return number if the bill number has to be generated
     *
     * @param bill      an instance of <code>EgBillregister</code> containing the bill date representing the financial year.
     * @param workOrder an instance of <code>WorkOrder</code> representing the executing department.
     * @return a boolean value indicating if the bill number change is required.
     */
    @Override
    public String generateContractorBillNumber(final ContractorBillRegister contractorBillRegister) {
        return beanResolver.getAutoNumberServiceFor(ContractorBillNumberGenerator.class).getNextNumber(contractorBillRegister);
    }

    /**
     * The method return BigDecimal
     *
     * @param totalAdvancePaid , billDate, workOrderEstimate
     * @param workOrder        an instance of <code>WorkOrder</code>.
     * @return a BigDecimal value indicating total Advance pending for given Work Order Estimate as on bill date before this
     * current bill
     */
    @Override
    public BigDecimal calculateTotalPendingAdvance(final BigDecimal totalAdvancePaid, final Date billDate,
                                                   final WorkOrderEstimate workOrderEstimate, final Long billId) {
        final CChartOfAccounts advanceCOA = contractorAdvanceService
                .getContractorAdvanceAccountcodeForWOE(workOrderEstimate.getId());

        BigDecimal totalPendingBalance = BigDecimal.ZERO;
        if (advanceCOA != null && totalAdvancePaid != null && totalAdvancePaid.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal totalAdvanceAdjusted = getTotalAdvanceAdjustedForWOE(billDate, workOrderEstimate.getId(),
                    advanceCOA.getId(), billId);
            if (totalAdvanceAdjusted != null && totalAdvanceAdjusted.compareTo(BigDecimal.ZERO) > 0)
                totalPendingBalance = totalAdvanceAdjusted.subtract(totalAdvanceAdjusted);
            else
                totalPendingBalance = totalAdvancePaid;
        }

        return totalPendingBalance;
    }

    /**
     * API will returns the Total Amount for advance Adjusted up to billdate for specific Work Order Estimate before this current
     * bill
     *
     * @return BigDecimal
     */
    public BigDecimal getTotalAdvanceAdjustedForWOE(final Date billDate, final Long workOrderEstimateId,
                                                    final Long advanceGlCodeId, final Long billId) {
        BigDecimal advanceAdjustment = BigDecimal.ZERO;
        final List<Long> billIdList = getBillIdListUptoBillDate(billDate, workOrderEstimateId, billId);

        if (billIdList.isEmpty())
            billIdList.add(null);

        final List<EgBilldetails> egBilldetailsList = genericService.findAllByNamedQuery("getAdvanceAjustementTotAmt",
                new BigDecimal(advanceGlCodeId), billIdList);

        for (final EgBilldetails egBilldetails : egBilldetailsList)
            if (egBilldetails.getCreditamount() != null)
                advanceAdjustment = advanceAdjustment.add(egBilldetails.getCreditamount());

        return advanceAdjustment;
    }

    /**
     * API will returns the billId list for the contractor upto billdate, for giver Work Order Estimate before this current bill
     *
     * @return BigDecimal
     */
    public List<Long> getBillIdListUptoBillDate(final Date billDate, final Long workOrderEstimateId, final Long billId) {
        final List<Long> billIdList = new ArrayList<>();
        final ArrayList<Object> params = new ArrayList<>();
        String whereClause = "";
        params.add(workOrderEstimateId);
        params.add(billDate);

        if (billId != null) {
            final EgBillregister egbr = (EgBillregister) genericService.find(
                    "from EgBillregister egbr where egbr.id = ? ", billId);
            if (egbr.getBillstatus().equalsIgnoreCase("CANCELLED")) {
                whereClause = " mbh.egBillregister.billdate <= ? and  mbh.egBillregister.id<? )";
                params.add(billId);
            } else {
                whereClause = " mbh.egBillregister.billdate <= ? and mbh.egBillregister.billstatus!='CANCELLED' and mbh.egBillregister.id<? )";
                params.add(billId);
            }
        } else
            whereClause = " mbh.egBillregister.billdate <= ? and mbh.egBillregister.billstatus!='CANCELLED')";

        final List<EgBillregister> egBillregisterList = genericService.findAllBy(
                "select distinct mbh.egBillregister from MBHeader mbh where mbh.workOrderEstimate.id = ? " + " and "
                        + whereClause,
                params.toArray());
        if (!egBillregisterList.isEmpty())
            for (final EgBillregister egBillregister : egBillregisterList)
                billIdList.add(egBillregister.getId());
        return billIdList;
    }

    /**
     * Get sum of all bill of given work orders which are still unapproved.
     *
     * @param workOrderId
     * @return
     */
    @Override
    public BigDecimal getUtlizedAmountForUnArrovedBill(final Long workOrderId, final Date asOnDate) {
        BigDecimal result = BigDecimal.ZERO;
        final Object[] params = new Object[]{WorksConstants.CANCELLED_STATUS, workOrderId, asOnDate};
        final BigDecimal queryVal = (BigDecimal) genericService.findByNamedQuery("getUtlizedAmountForUnArrovedBill",
                params);
        if (queryVal != null)
            result = result.add(queryVal);

        return result;
    }

    /**
     * This method will return cumulative amount for all approved MB for a given workorder
     *
     * @param workOrderId
     * @return
     */
    public BigDecimal getApprovedMBAmountOld(final Long workOrderId, final Long workOrderEstimateId, final Date asOnDate) {
        BigDecimal result = BigDecimal.ZERO;
        Object[] params = new Object[]{WorksConstants.APPROVED, workOrderId, workOrderEstimateId, asOnDate};
        final Double queryVal = (Double) genericService.findByNamedQuery("totalApprovedMBAmount", params);
        params = new Object[]{WorksConstants.APPROVED, workOrderId, workOrderEstimateId, asOnDate,
                WorksConstants.CANCELLED_STATUS};
        final Double queryVal2 = (Double) genericService.findByNamedQuery("totalApprovedMBAmountForCancelledBill",
                params);
        if (queryVal != null)
            result = result.add(BigDecimal.valueOf(queryVal));
        if (queryVal2 != null)
            result = result.add(BigDecimal.valueOf(queryVal2));

        return result;
    }

    /**
     * This method will return cumulative amount for all approved MB for a given workorder
     *
     * @param workOrderId
     * @return
     */
    @Override
    public BigDecimal getApprovedMBAmount(final Long workOrderId, final Long workOrderEstimateId, final Date asOnDate) {
        BigDecimal result = BigDecimal.ZERO;
        Object[] params = new Object[]{WorksConstants.APPROVED, workOrderId, workOrderEstimateId, asOnDate};
        // NOTE -- Here we will not consider legacy MBs -- the named query below
        // has been modified for this purpose
        final List<Object[]> approvedMBsList = genericService.findAllByNamedQuery("gettotalApprovedMBs", params);

        params = new Object[]{WorksConstants.APPROVED, workOrderId, workOrderEstimateId, asOnDate,
                WorksConstants.CANCELLED_STATUS};
        // NOTE -- Here we will not consider legacy MBs -- the named query below
        // has been modified for this purpose
        final List<Object[]> approvedMBsForCancelledBillList = genericService.findAllByNamedQuery(
                "gettotalApprovedMBsForCancelledBill", params);

        Double amount;
        Iterator iter1;
        Iterator iter2;
        Iterator iter3;
        Iterator iter4;

        final List<Long> woaIdsListForApprovedMBs = new ArrayList<>();
        iter1 = approvedMBsList.iterator();

        while (iter1.hasNext()) {
            final Object[] obj = (Object[]) iter1.next();
            woaIdsListForApprovedMBs.add((Long) obj[0]);
        }

        if (!woaIdsListForApprovedMBs.isEmpty()) {
            final List<WorkOrderActivity> woaListForApprovedMBs = getWorkOrderActivityListForIds(woaIdsListForApprovedMBs);
            iter2 = approvedMBsList.iterator();
            while (iter2.hasNext()) {
                final Object[] obj = (Object[]) iter2.next();
                final Long woaId = (Long) obj[0];
                final double mbQuantity = (Double) obj[1];
                for (final WorkOrderActivity woa : woaListForApprovedMBs)
                    if (woaId.equals(woa.getId())) {
                        if (woa.getActivity().getNonSor() == null)
                            amount = woa.getApprovedRate() * mbQuantity * woa.getConversionFactor();
                        else
                            amount = woa.getApprovedRate() * mbQuantity;
                        result = result.add(BigDecimal.valueOf(amount));
                        break;
                    }
            }
        }

        final List<Long> woaIdsListForCancelledBills = new ArrayList<>();
        iter3 = approvedMBsForCancelledBillList.iterator();

        while (iter3.hasNext()) {
            final Object[] obj = (Object[]) iter3.next();
            woaIdsListForCancelledBills.add((Long) obj[0]);
        }
        if (!woaIdsListForCancelledBills.isEmpty()) {
            final List<WorkOrderActivity> woaListForCancelledBills = getWorkOrderActivityListForIds(woaIdsListForCancelledBills);
            iter4 = approvedMBsForCancelledBillList.iterator();

            while (iter4.hasNext()) {
                final Object[] obj = (Object[]) iter4.next();
                final Long woaId = (Long) obj[0];
                final double mbQuantity = (Double) obj[1];
                for (final WorkOrderActivity woa : woaListForCancelledBills)
                    if (woaId.equals(woa.getId())) {
                        if (woa.getActivity().getNonSor() == null)
                            amount = woa.getApprovedRate() * mbQuantity * woa.getConversionFactor();
                        else
                            amount = woa.getApprovedRate() * mbQuantity;
                        result = result.add(BigDecimal.valueOf(amount));
                        break;
                    }
            }
        }
        return result;
    }

    /**
     * This method will return tendered Items cumulative amount for all approved MB for a given workorder (Tendered Items don't
     * have a revision type)
     *
     * @param workOrderId
     * @return
     */
    @Override
    public BigDecimal getApprovedMBAmountOfTenderedItems(final Long workOrderId, final Long workOrderEstimateId,
                                                         final Date asOnDate) {
        BigDecimal result = BigDecimal.ZERO;
        Object[] params = new Object[]{WorksConstants.APPROVED, workOrderId, workOrderEstimateId, asOnDate};
        // NOTE -- Here we will not consider legacy MBs -- the named query below
        // has been modified for this purpose
        final List<Object[]> approvedMBsList = genericService.findAllByNamedQuery("gettotalApprovedMBs", params);

        params = new Object[]{WorksConstants.APPROVED, workOrderId, workOrderEstimateId, asOnDate,
                WorksConstants.CANCELLED_STATUS};
        // NOTE -- Here we will not consider legacy MBs -- the named query below
        // has been modified for this purpose
        final List<Object[]> approvedMBsForCancelledBillList = genericService.findAllByNamedQuery(
                "gettotalApprovedMBsForCancelledBill", params);

        Double amount;
        Iterator iter1;
        Iterator iter2;
        Iterator iter3;
        Iterator iter4;

        final List<Long> woaIdsListForApprovedMBs = new ArrayList<>();
        iter1 = approvedMBsList.iterator();

        while (iter1.hasNext()) {
            final Object[] obj = (Object[]) iter1.next();
            woaIdsListForApprovedMBs.add((Long) obj[0]);
        }
        if (!woaIdsListForApprovedMBs.isEmpty()) {
            final List<WorkOrderActivity> woaListForApprovedMBs = getWorkOrderActivityListForIds(woaIdsListForApprovedMBs);
            iter2 = approvedMBsList.iterator();
            while (iter2.hasNext()) {
                final Object[] obj = (Object[]) iter2.next();
                final Long woaId = (Long) obj[0];
                final double mbQuantity = (Double) obj[1];
                for (final WorkOrderActivity woa : woaListForApprovedMBs)
                    if (woaId.equals(woa.getId())) {
                        if (woa.getActivity().getNonSor() == null)
                            amount = woa.getApprovedRate() * mbQuantity * woa.getConversionFactor();
                        else
                            amount = woa.getApprovedRate() * mbQuantity;
                        if (woa.getActivity().getRevisionType() == null) {
                            result = result.add(BigDecimal.valueOf(amount));
                            break;
                        }
                    }

            }
        }

        final List<Long> woaIdsListForCancelledBills = new ArrayList<>();
        iter3 = approvedMBsForCancelledBillList.iterator();

        while (iter3.hasNext()) {
            final Object[] obj = (Object[]) iter3.next();
            woaIdsListForCancelledBills.add((Long) obj[0]);
        }

        if (!woaIdsListForCancelledBills.isEmpty()) {
            final List<WorkOrderActivity> woaListForCancelledBills = getWorkOrderActivityListForIds(woaIdsListForCancelledBills);
            iter4 = approvedMBsForCancelledBillList.iterator();
            while (iter4.hasNext()) {
                final Object[] obj = (Object[]) iter4.next();
                final Long woaId = (Long) obj[0];
                final double mbQuantity = (Double) obj[1];
                for (final WorkOrderActivity woa : woaListForCancelledBills)
                    if (woaId.equals(woa.getId())) {
                        if (woa.getActivity().getNonSor() == null)
                            amount = woa.getApprovedRate() * mbQuantity * woa.getConversionFactor();
                        else
                            amount = woa.getApprovedRate() * mbQuantity;
                        if (woa.getActivity().getRevisionType() == null) {
                            result = result.add(BigDecimal.valueOf(amount));
                            break;
                        }
                    }
            }
        }
        return result;
    }

    /**
     * API will returns the Standard deduction types as key and its mapped COA as map values
     *
     * @return map containing deduction type as key and string array of coa glcodes
     */
    @Override
    public Map<String, String[]> getStandardDeductionsFromConfig() {
        final String strDec = worksService.getWorksConfigValue("STANDARD_DEDUCTION");
        final Map<String, String[]> map = new HashMap<>();
        final String[] splitedMainArr = strDec.split("\\|"); // get Type:Codes
        // pairs
        for (final String element : splitedMainArr) {
            final String[] splitedSubArr = element.split(":"); // split
            // Type:Codes
            // pair
            final String[] splitedACCodesArr = splitedSubArr[1].split(","); // split
            // Codes
            // for
            // Type
            map.put(splitedSubArr[0], splitedACCodesArr);
        }
        return map;
    }

     /**
     * for pdf starts here API will returns the Total value for the workorder upto billdate
     *
     * @return Double
     */
    @Override
    public BigDecimal getTotalValueWoForUptoBillDate(final Date billDate, final Long workOrderId,
                                                     final Long workOrderEstimateId) {
        BigDecimal totalWorkValue = BigDecimal.ZERO;
        final List<EgBillregister> egBillregisterList = genericService
                .findAllBy(
                        "select distinct mbh.egBillregister from MBHeader mbh where mbh.egBillregister.id in (select egBillRegister.id from org.egov.model.bills.EgBillregister egBillRegister "
                                + " where egBillRegister.billdate <= ? and egBillRegister.billstatus <>'CANCELLED') and mbh.workOrder.id = ? and mbh.workOrderEstimate.id=?",
                        billDate, workOrderId, workOrderEstimateId);
        if (!egBillregisterList.isEmpty())
            for (final EgBillregister egBillregister : egBillregisterList)
                totalWorkValue = totalWorkValue.add(egBillregister.getBillamount());
        return totalWorkValue;
    }

    /**
     * API will returns the sorted deduction list from appconfig
     *
     * @return List containing deduction type
     */
    @Override
    public List<String> getSortedDeductionsFromConfig(final String Key) {
        final String strDec = worksService.getWorksConfigValue(Key);
        final List<String> sortedDedcutionList = new ArrayList<>();
        final String[] splitedMainArr = strDec.split("\\|");
        for (final String element : splitedMainArr)
            sortedDedcutionList.add(element);
        return sortedDedcutionList;
    }

    /**
     * API will returns the sorted list for a given list values
     *
     * @return List containing sorted deduction names
     */
    @Override
    public List<StatutoryDeductionsForBill> getStatutoryDeductionSortedOrder(final List<String> requiredOrder,
                                                                             final List<StatutoryDeductionsForBill> givenEgBillPayeedetails) {
        final List<StatutoryDeductionsForBill> orderedResults = new ArrayList<>();
        for (final String caseStatus : requiredOrder)
            for (final StatutoryDeductionsForBill statDeductionDetails : givenEgBillPayeedetails)
                if (caseStatus.equals(statDeductionDetails.getEgBillPayeeDtls().getRecovery().getType()))
                    orderedResults.add(statDeductionDetails);
        return orderedResults;
    }

    @Override
    public List<DeductionTypeForBill> getStandardDeductionSortedOrder(final List<String> requiredOrder,
                                                                      final List<DeductionTypeForBill> givenStandardList) {
        final List<DeductionTypeForBill> orderedResults = new ArrayList<>();
        for (final String caseStatus : requiredOrder)
            for (final DeductionTypeForBill deductionTypeForBill : givenStandardList)
                if (caseStatus.equals(deductionTypeForBill.getDeductionType()))
                    orderedResults.add(deductionTypeForBill);
        return orderedResults;
    }

    /**
     * API will returns the statutory list for a given bill Id and type
     *
     * @return List containing Statutory deduction names
     */
    @Override
    public List<StatutoryDeductionsForBill> getStatutoryListForBill(final Long billId) {
        return genericService.findAllBy(
                "from StatutoryDeductionsForBill epd where epd.egBillPayeeDtls.egBilldetailsId.egBillregister.id=? "
                        + "and epd.egBillPayeeDtls.recovery.id is not null",
                billId);
    }

    /*
     * * API will returns the standard deduction list for a given bill Id and type
     * @return List containing Statutory deduction names
     */
    @Override
    public List<DeductionTypeForBill> getStandardDeductionForBill(final Long billId) {
        return genericService.findAllBy("from DeductionTypeForBill dtb where dtb.egbill.id=?", billId);
    }

    @Override
    public List<AssetForBill> getAssetForBill(final Long billId) {
        return genericService.findAllBy("from AssetForBill assetForBill where assetForBill.egbill.id=?", billId);
    }

    /**
     * API will returns the Advance adjustment amount for a given bill Id
     *
     * @return BigDecimal
     */
    @Override
    public BigDecimal getAdvanceAdjustmentAmountForBill(final Long billId, final Long workOrderEstimateId) {

        BigDecimal advanceAdjustment = BigDecimal.ZERO;
        final CChartOfAccounts advanceCOA = contractorAdvanceService
                .getContractorAdvanceAccountcodeForWOE(workOrderEstimateId);
        if (advanceCOA != null) {
            final EgBilldetails egBilldetails = (EgBilldetails) genericService.find(
                    "from EgBilldetails ebd where ebd.glcodeid=? and " + "ebd.egBillregister.id=?", new BigDecimal(
                            advanceCOA.getId()),
                    billId);
            if (egBilldetails != null && egBilldetails.getCreditamount() != null)
                advanceAdjustment = egBilldetails.getCreditamount();
        }
        return advanceAdjustment;
    }

    /**
     * API will returns the custom deduction list of egbilldetails excluding glcode
     *
     * @return BigDecimal
     */
    @Override
    public List<EgBilldetails> getCustomDeductionListforglcodes(final List<BigDecimal> glcodeIdList, final Long billId) {
        return genericService.findAllByNamedQuery("CustomDeductionList", billId, glcodeIdList);
    }

    public List<EgBilldetails> getRetentionMoneyListforglcodes(final List<BigDecimal> glcodeIdList, final Long billId) {
        return genericService.findAllByNamedQuery("RetentionMoneyDeductionList", billId, glcodeIdList);
    }

    public List<EgBilldetails> getAccountDetailsList(final List<BigDecimal> glcodeIdList, final Long billId) {
        return genericService.findAllByNamedQuery("AccountDetailsList", billId, glcodeIdList);
    }

    /**
     * API will returns the Net Payable Amount for netpayable code coaId
     *
     * @return BigDecimal
     * @throws ApplicationException
     * @throws NumberFormatException
     */
    @Override
    public BigDecimal getNetPayableAmountForGlCodeId(final Long billId) throws ApplicationException {
        BigDecimal netPayableAmount = BigDecimal.ZERO;
        final List<CChartOfAccounts> coaPayableList = chartOfAccountsHibernateDAO.getAccountCodeByPurpose(Integer
                .valueOf(worksService.getWorksConfigValue(WORKS_NETPAYABLE_CODE)));

        for (final CChartOfAccounts coa : coaPayableList) {
            final List<EgBilldetails> egBillDetails = genericService.findAllBy(
                    "from EgBilldetails ebd where ebd.glcodeid=? and ebd.egBillregister.id=?",
                    new BigDecimal(coa.getId()), billId);
            if (!egBillDetails.isEmpty())
                netPayableAmount = egBillDetails.get(0).getCreditamount();
        }

        return netPayableAmount;
    }

    /**
     * API will returns the Total Amount for advanceAjustment deduction for work order estimate upto billdate
     *
     * @return BigDecimal
     */
    @Override
    public BigDecimal getTotAmtForAdvanceAdjustment(final Date billDate, final Long workOrderId,
                                                    final Long workOrderEstimateId) {
        BigDecimal totDeductionAmt = BigDecimal.ZERO;
        final CChartOfAccounts advanceCOA = contractorAdvanceService
                .getContractorAdvanceAccountcodeForWOE(workOrderEstimateId);
        if (advanceCOA != null)
            totDeductionAmt = getAdvanceAdjustmentDeductionTotAmount(billDate, workOrderId, advanceCOA.getId(),
                    workOrderEstimateId);
        return totDeductionAmt;
    }

    /**
     * API will returns the Total Amount for advanceAjustment deduction for work order estimate upto billdate
     *
     * @return BigDecimal
     */
    public BigDecimal getAdvanceAdjustmentDeductionTotAmount(final Date billDate, final Long workOrderId,
                                                             final Long advanceCOAId, final Long workOrderEstimateId) {
        BigDecimal advanceAdjustment = BigDecimal.ZERO;
        final List<Long> billIdList = getBillIdListForWoUptoBillDate(billDate, workOrderId, workOrderEstimateId);

        if (billIdList.isEmpty())
            billIdList.add(null);

        final List<EgBilldetails> egBilldetailsList = genericService.findAllByNamedQuery("getAdvanceAjustementTotAmt",
                new BigDecimal(advanceCOAId), billIdList);

        for (final EgBilldetails egBilldetails : egBilldetailsList)
            if (egBilldetails.getCreditamount() != null)
                advanceAdjustment = advanceAdjustment.add(egBilldetails.getCreditamount());

        return advanceAdjustment;
    }

    /**
     * API will returns the Total Amount for Statutory deduction for workorder upto billdate for that dedcution
     *
     * @return BigDecimal
     */
    @Override
    public BigDecimal getTotAmtForStatutory(final Date billDate, final Long workOrderId,
                                            final StatutoryDeductionsForBill statDeductionBilldetail, final Long workOrderEstimateId) {
        BigDecimal totalStatutoryAmount = BigDecimal.ZERO;
        final List<Long> billIdList = getBillIdListForWoUptoBillDate(billDate, workOrderId, workOrderEstimateId);

        List<StatutoryDeductionsForBill> egBillPayeedetailsList = new ArrayList<>();
        if (billIdList != null && !billIdList.isEmpty())
            egBillPayeedetailsList = genericService.findAllByNamedQuery("getStatutoryTotAmt", statDeductionBilldetail
                    .getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId(), billIdList);

        for (final StatutoryDeductionsForBill egBillPayeedetails : egBillPayeedetailsList)
            if (egBillPayeedetails.getEgBillPayeeDtls().getCreditAmount() != null)
                totalStatutoryAmount = totalStatutoryAmount.add(egBillPayeedetails.getEgBillPayeeDtls()
                        .getCreditAmount());

        return totalStatutoryAmount;
    }

    /**
     * API will returns the Total Amount for custom deduction for workorder upto billdate
     *
     * @return BigDecimal
     */
    @Override
    public BigDecimal getTotAmtForStandard(final Date billDate, final Long workOrderId,
                                           final DeductionTypeForBill deductionTypeForBill1, final Long workOrderEstimateId) {
        BigDecimal totalStandarDeductionAmount = BigDecimal.ZERO;
        final List<Long> billIdList = getBillIdListForWoUptoBillDate(billDate, workOrderId, workOrderEstimateId);
        // deductionTypeForBill1.getCoa().getId()

        List<DeductionTypeForBill> standardDeductionList = new ArrayList<>();
        if (billIdList != null && !billIdList.isEmpty())
            standardDeductionList = genericService.findAllByNamedQuery("getStandardTotAmt", deductionTypeForBill1
                    .getCoa().getId(), billIdList);

        for (final DeductionTypeForBill deductionTypeForBill : standardDeductionList)
            if (deductionTypeForBill.getCreditamount() != null)
                totalStandarDeductionAmount = totalStandarDeductionAmount.add(deductionTypeForBill.getCreditamount());

        return totalStandarDeductionAmount;
    }

    /**
     * API will returns the Total Amount for custom deduction for workorder upto billdate
     *
     * @return BigDecimal
     */
    @Override
    public BigDecimal getTotAmtForCustom(final Date billDate, final Long workOrderId,
                                         final EgBilldetails egBilldetails1, final Long workOrderEstimateId) {
        BigDecimal totalCustomDeductionAmount = BigDecimal.ZERO;
        final List<Long> billIdList = getBillIdListForWoUptoBillDate(billDate, workOrderId, workOrderEstimateId);
        // glcodeIdList
        List<EgBilldetails> customDeductionList = new ArrayList<>();
        if (billIdList != null && !billIdList.isEmpty())
            customDeductionList = genericService.findAllByNamedQuery("getCustomDeductionTotAmt",
                    egBilldetails1.getGlcodeid(), billIdList);

        for (final EgBilldetails egBilldetails : customDeductionList)
            if (egBilldetails.getCreditamount() != null)
                totalCustomDeductionAmount = totalCustomDeductionAmount.add(egBilldetails.getCreditamount());

        return totalCustomDeductionAmount;
    }

    /**
     * API will returns the billId list for the workorder upto billdate
     *
     * @return BigDecimal
     */
    public List<Long> getBillIdListForWoUptoBillDate(final Date billDate, final Long workOrderId,
                                                     final Long workOrderEstimateId) {
        final List<Long> billIdList = new ArrayList<>();
        LOGGER.debug("---inside getBillIdListForWoUptoBillDate----");

        final List<EgBillregister> egBillregisterList = genericService
                .findAllBy(
                        "select distinct mbh.egBillregister from MBHeader mbh "
                                + "where mbh.egBillregister.billdate <=? and mbh.egBillregister.billstatus<>'CANCELLED' and mbh.workOrder.id=? and mbh.workOrderEstimate.id=?",
                        billDate, workOrderId, workOrderEstimateId);
        if (!egBillregisterList.isEmpty())
            for (final EgBillregister egBillregister : egBillregisterList)
                billIdList.add(egBillregister.getId());

        LOGGER.debug("---atend getBillIdListForWoUptoBillDate ");
        return billIdList;
    }

    /**
     * Get the list of eligible bills based on parameters provided
     *
     * @param paramsMap
     * @return
     */
    @Override
    public List<String> searchContractorBill(final Map<String, Object> paramsMap, final List<Object> paramList) {

        final List<String> QueryObj = new ArrayList<>();
        StringBuilder commonQry = new StringBuilder();
        final String countQry = "select count(distinct cbr) from ContractorBillRegister cbr where cbr.id != null and cbr.billstatus != ? ";

        final String dynQuery = "select distinct cbr from ContractorBillRegister cbr where cbr.id != null and cbr.billstatus != ? ";
        paramList.add(WorksConstants.NEW);

        if (paramsMap.get(WORKORDER_NO) != null) {
            commonQry = commonQry.append("  and cbr.workordernumber like ?");
            paramList.add("%" + paramsMap.get(WORKORDER_NO) + "%");
        }
        if (paramsMap.get(CONTRACTOR_ID) != null && !"-1".equals(paramsMap.get(CONTRACTOR_ID))) {
            commonQry = commonQry
                    .append(" and (cbr.id in (select mbh.egBillregister.id from MBHeader mbh where mbh.egBillregister.id=cbr.id and mbh.workOrder.contractor.id = ?)"
                            + " OR cbr.id in (select mbcb.egBillregister.id from MBForCancelledBill mbcb where mbcb.egBillregister.id=cbr.id and mbcb.mbHeader.workOrder.contractor.id = ?))");
            paramList.add(paramsMap.get(CONTRACTOR_ID));
            paramList.add(paramsMap.get(CONTRACTOR_ID));

        }
        if (paramsMap.get(FROM_DATE) != null && paramsMap.get(TO_DATE) == null) {
            commonQry = commonQry.append(" and cbr.billdate >= ? ");
            paramList.add(paramsMap.get(FROM_DATE));

        } else if (paramsMap.get(TO_DATE) != null && paramsMap.get(FROM_DATE) == null) {
            commonQry = commonQry.append(" and cbr.billdate <= ? ");
            paramList.add(paramsMap.get(TO_DATE));
        } else if (paramsMap.get(FROM_DATE) != null && paramsMap.get(TO_DATE) != null) {
            commonQry = commonQry.append(" and cbr.billdate between ? and ? ");
            paramList.add(paramsMap.get(FROM_DATE));
            paramList.add(paramsMap.get(TO_DATE));
        }
        if (paramsMap.get(BILLSTATUS) != null && !paramsMap.get(BILLSTATUS).equals("-1")) {
            commonQry = commonQry.append(" and cbr.billstatus=?");
            paramList.add(paramsMap.get(BILLSTATUS));
        }
        if (paramsMap.get(BILLNO) != null) {
            commonQry = commonQry.append(" and cbr.billnumber like ?");
            paramList.add("%" + paramsMap.get(BILLNO) + "%");
        }

        if (paramsMap.get(BILL_DEPT_ID) != null && !"-1".equals(paramsMap.get(BILL_DEPT_ID))) {
            commonQry = commonQry.append(" and cbr.egBillregistermis.egDepartment.id = ? ");
            paramList.add(paramsMap.get(BILL_DEPT_ID));
        }
        if (paramsMap.get(EXEC_DEPT_ID) != null && !"-1".equals(paramsMap.get(EXEC_DEPT_ID))) {
            commonQry = commonQry
                    .append(" and (cbr.id in (select mbh.egBillregister.id from MBHeader mbh where mbh.egBillregister.id=cbr.id "
                            + "and mbh.workOrderEstimate.estimate.executingDepartment.id = ?) OR cbr.id in (select mbcb.egBillregister.id from MBForCancelledBill mbcb where"
                            + " mbcb.egBillregister.id=cbr.id and mbcb.mbHeader.workOrderEstimate.estimate.executingDepartment.id = ?))");
            paramList.add(paramsMap.get(EXEC_DEPT_ID));
            paramList.add(paramsMap.get(EXEC_DEPT_ID));

        }
        if (paramsMap.get(EST_NO) != null) {
            commonQry = commonQry
                    .append(" and (EXISTS (select mbh.egBillregister.id from MBHeader mbh where mbh.egBillregister.id=cbr.id "
                            + "and mbh.workOrderEstimate.estimate.estimateNumber like ? ) OR EXISTS (select mbcb.egBillregister.id from MBForCancelledBill mbcb where"
                            + " mbcb.egBillregister.id=cbr.id and mbcb.mbHeader.workOrderEstimate.estimate.estimateNumber like ? ))");
            paramList.add("%" + paramsMap.get(EST_NO) + "%");
            paramList.add("%" + paramsMap.get(EST_NO) + "%");

        }

        commonQry = commonQry.append(" order by cbr.billdate");

        QueryObj.add(dynQuery + commonQry);
        QueryObj.add(countQry + commonQry);
        return QueryObj;
    }

    /**
     * Get the list of custom dedcution based on glcodes of custom deduction
     *
     * @param ContractorBillRegister
     * @return List
     * @throws ApplicationException
     * @throws NumberFormatException
     */
    @Override
    public List<EgBilldetails> getCustomDeductionList(final Long billId, final Long workOrderEstimateId,
                                                      final List<StatutoryDeductionsForBill> statutoryList,
                                                      final List<DeductionTypeForBill> standardDeductionList,
                                                      final List<EgBilldetails> retentionMoneyDeductionList) throws ApplicationException {
        final List<BigDecimal> glcodeIdList = new ArrayList<>();
        addStatutoryDeductionGlcode(glcodeIdList, statutoryList);
        addStandardDeductionGlcode(glcodeIdList, standardDeductionList);
        String advanceAdjstglCodeId = "";
        final CChartOfAccounts advanceCOA = contractorAdvanceService
                .getContractorAdvanceAccountcodeForWOE(workOrderEstimateId);
        if (advanceCOA != null)
            advanceAdjstglCodeId = advanceCOA.getId().toString();
        addRetentionMoneyDeductionGlcode(glcodeIdList, retentionMoneyDeductionList);
        addGlCodeForNetPayable(glcodeIdList);
        if (StringUtils.isNotBlank(advanceAdjstglCodeId))
            glcodeIdList.add(new BigDecimal(advanceAdjstglCodeId));
        return getCustomDeductionListforglcodes(glcodeIdList, billId);
    }

    @Override
    public List<EgBilldetails> getRetentionMoneyDeductionList(final Long billId,
                                                              final List<StatutoryDeductionsForBill> statutoryList, final List<DeductionTypeForBill> standardDeductionList)
            throws ApplicationException {
        final List<BigDecimal> retentionGlcodeIdList = new ArrayList<>();
        getAllRetentionMoneyGlcodeList(retentionGlcodeIdList);
        return getRetentionMoneyListforglcodes(retentionGlcodeIdList, billId);
    }

    @Override
    public List<EgBilldetails> getAccountDetailsList(final Long billId, final Long workOrderEstimateId,
                                                     final List<StatutoryDeductionsForBill> statutoryList,
                                                     final List<DeductionTypeForBill> standardDeductionList, final List<EgBilldetails> customDeductionList,
                                                     final List<EgBilldetails> retentionMoneyDeductionList) throws ApplicationException {
        final List<BigDecimal> glcodeIdList = new ArrayList<>();
        addStatutoryDeductionGlcode(glcodeIdList, statutoryList);
        addStandardDeductionGlcode(glcodeIdList, standardDeductionList);
        String advanceAdjstglCodeId = "";
        final CChartOfAccounts advanceCOA = contractorAdvanceService
                .getContractorAdvanceAccountcodeForWOE(workOrderEstimateId);
        if (advanceCOA != null)
            advanceAdjstglCodeId = advanceCOA.getId().toString();
        addRetentionMoneyDeductionGlcode(glcodeIdList, retentionMoneyDeductionList);
        addCustomDeductionGlcode(glcodeIdList, customDeductionList);
        addGlCodeForNetPayable(glcodeIdList);
        if (StringUtils.isNotBlank(advanceAdjstglCodeId))
            glcodeIdList.add(new BigDecimal(advanceAdjstglCodeId));
        return getAccountDetailsList(glcodeIdList, billId);
    }

    public void addStatutoryDeductionGlcode(final List<BigDecimal> glcodeIdList,
                                            final List<StatutoryDeductionsForBill> sortedStatutorySortedList) {
        if (!sortedStatutorySortedList.isEmpty())
            for (final StatutoryDeductionsForBill bpd : sortedStatutorySortedList)
                if (bpd != null && bpd.getEgBillPayeeDtls().getRecovery() != null
                        && bpd.getEgBillPayeeDtls().getRecovery().getId() != null
                        && bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts() != null
                        && bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId() != null)
                    glcodeIdList
                            .add(new BigDecimal(bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId()));
    }

    public void addStandardDeductionGlcode(final List<BigDecimal> glcodeIdList,
                                           final List<DeductionTypeForBill> sortedStandardDeductionList) {
        if (!sortedStandardDeductionList.isEmpty())
            for (final DeductionTypeForBill deductionTypeForBill : sortedStandardDeductionList)
                if (deductionTypeForBill.getCoa() != null && deductionTypeForBill.getCoa().getId() != null)
                    glcodeIdList.add(new BigDecimal(deductionTypeForBill.getCoa().getId()));
    }

    public void addRetentionMoneyDeductionGlcode(final List<BigDecimal> glcodeIdList,
                                                 final List<EgBilldetails> retentionMoneyDeductionList) {
        if (!retentionMoneyDeductionList.isEmpty())
            for (final EgBilldetails deductionTypeForBill : retentionMoneyDeductionList)
                if (deductionTypeForBill.getGlcodeid() != null)
                    glcodeIdList.add(deductionTypeForBill.getGlcodeid());
    }

    private void getAllRetentionMoneyGlcodeList(final List<BigDecimal> retentionGlcodeIdList) {

        if (StringUtils.isNotBlank(worksService.getWorksConfigValue(RETENTION_MONEY_PURPOSE))) {
            final List<CChartOfAccounts> tempAllRetAccList = chartOfAccountsHibernateDAO.getAccountCodeByPurpose(Integer
                    .valueOf(worksService.getWorksConfigValue(RETENTION_MONEY_PURPOSE)));
            for (final CChartOfAccounts acc : tempAllRetAccList)
                retentionGlcodeIdList.add(new BigDecimal(acc.getId()));
        }
    }

    public void addCustomDeductionGlcode(final List<BigDecimal> glcodeIdList,
                                         final List<EgBilldetails> customDeductionList) {
        if (!customDeductionList.isEmpty())
            for (final EgBilldetails deductionTypeForBill : customDeductionList)
                if (deductionTypeForBill.getGlcodeid() != null)
                    glcodeIdList.add(deductionTypeForBill.getGlcodeid());
    }

    public void addGlCodeForNetPayable(final List<BigDecimal> glcodeIdList) {
        final List<CChartOfAccounts> coaPayableList = chartOfAccountsHibernateDAO.getAccountCodeByPurpose(Integer
                .valueOf(worksService.getWorksConfigValue(WORKS_NETPAYABLE_CODE)));
        if (coaPayableList != null)
            for (final CChartOfAccounts coa : coaPayableList)
                if (coa.getId() != null)
                    glcodeIdList.add(new BigDecimal(coa.getId()));
    }

    @Override
    public BigDecimal getNetPaybleCode(final Long billId) throws Exception {
        final List<BigDecimal> glcodeIdList = new ArrayList<>();
        final List<CChartOfAccounts> coaPayableList = chartOfAccountsHibernateDAO.getAccountCodeByPurpose(Integer
                .valueOf(worksService.getWorksConfigValue(WORKS_NETPAYABLE_CODE)));
        if (coaPayableList != null)
            for (final CChartOfAccounts coa : coaPayableList)
                if (coa.getId() != null)
                    glcodeIdList.add(new BigDecimal(coa.getId()));

        return egBilldetailsHibernateDAO.getBillDetails(billId, glcodeIdList).getGlcodeid();
    }

    @Override
    public List<MBHeader> getMbListForBillAndWorkordrId(final Long workOrderId, final Long billId) {
        return genericService.findAllBy(
                "from MBHeader mbHeader where mbHeader.workOrder.id=? and mbHeader.egBillregister.id=?", workOrderId,
                billId);

    }

    @Override
    public List<MBForCancelledBill> getMbListForCancelBill(final Long billId) {
        return genericService.findAllBy(
                "from MBForCancelledBill mbcb where  mbcb.egBillregister.id=?", billId);
    }

    @Override
    public void setAllViewLists(final Long id, final Long workOrderId, final Long workOrderEstimateId,
                                final List<StatutoryDeductionsForBill> actionStatutorydetails,
                                final List<DeductionTypeForBill> standardDeductions, final List<EgBilldetails> customDeductions,
                                final List<EgBilldetails> retentionMoneyDeductions, final List<AssetForBill> accountDetailsForBill)
            throws ApplicationException {
        actionStatutorydetails.clear();
        actionStatutorydetails.addAll(getStatutoryListForBill(id));
        standardDeductions.clear();
        accountDetailsForBill.clear();
        for (final DeductionTypeForBill deductionTypeForBill : getStandardDeductionForBill(id)) {
            deductionTypeForBill.setGlcodeid(BigDecimal.valueOf(deductionTypeForBill.getCoa().getId()));
            standardDeductions.add(deductionTypeForBill);
        }
        retentionMoneyDeductions.clear();
        retentionMoneyDeductions.addAll(getRetentionMoneyDeductionList(id, actionStatutorydetails, standardDeductions));
        customDeductions.clear();
        customDeductions.addAll(getCustomDeductionList(id, workOrderEstimateId, actionStatutorydetails,
                standardDeductions, retentionMoneyDeductions));
        final List<EgBilldetails> accountDetailsForassetandbill = getAccountDetailsList(id, workOrderEstimateId,
                actionStatutorydetails, standardDeductions, customDeductions, retentionMoneyDeductions);
        accountDetailsForBill.addAll(getAssetForBill(id));
        if (accountDetailsForBill.isEmpty())
            for (final EgBilldetails egBilldetails : accountDetailsForassetandbill) {
                final CChartOfAccounts coa = chartOfAccountsHibernateDAO.findById(egBilldetails.getGlcodeid()
                        .longValue(), false);
                if (coa != null) {
                    coa.setId(egBilldetails.getGlcodeid().longValue());
                    final AssetForBill assetforBill = new AssetForBill();
                    assetforBill.setCoa(coa);
                    assetforBill.setDescription(coa.getName());
                    assetforBill.setAmount(egBilldetails.getDebitamount());
                    if (!accountDetailsForBill.contains(assetforBill))
                        accountDetailsForBill.add(assetforBill);
                }
            }
    }

    @Override
    public List<EgChecklists> getEgcheckList(final Long billId) throws ApplicationException {
        return checklistService.findAllBy("from EgChecklists egChecklists  where egChecklists.objectid=?", billId);
    }

    @Override
    public WorkCompletionInfo setWorkCompletionInfoFromBill(final ContractorBillRegister contractorBillRegister,
                                                            final WorkOrderEstimate workOrderEstimate) {
        WorkCompletionInfo workCompletionInfo = null;
        String mbNumbers = "";

        final List<String> mbNumberList = genericService
                .findAllByNamedQuery(WorksConstants.QUERY_GETALLMBNOSBYWORKORDERESTIMATE, WorksConstants.APPROVED,
                        workOrderEstimate.getId());
        for (final String mbNumber : mbNumberList)
            mbNumbers = mbNumbers.concat(mbNumber).concat(",");
        final int strLen = mbNumbers.length();
        if (strLen > 0)
            mbNumbers = mbNumbers.substring(0, strLen - 1);
        String workCommenced = "";
        final List<AppConfigValues> appConfigValuesList = worksService.getAppConfigValue(WorksConstants.WORKS,
                WorksConstants.WORKORDER_LASTSTATUS);
        if (appConfigValuesList != null && !appConfigValuesList.isEmpty()
                && appConfigValuesList.get(0).getValue() != null)
            workCommenced = appConfigValuesList.get(0).getValue();
        Date workCommencedDate = null;
        final OfflineStatus woStatus = (OfflineStatus) genericService.findByNamedQuery(
                WorksConstants.QUERY_GETSTATUSDATEBYOBJECTID_TYPE_DESC, workOrderEstimate.getWorkOrder().getId(),
                WorkOrder.class.getSimpleName(), workCommenced);
        if (woStatus != null)
            workCommencedDate = woStatus.getStatusDate();

        List<StateHistory<Position>> history = null;
        if (contractorBillRegister != null && contractorBillRegister.getCurrentState() != null
                && contractorBillRegister.getCurrentState().getHistory() != null)
            history = contractorBillRegister.getStateHistory();

        workCompletionInfo = new WorkCompletionInfo(workOrderEstimate, mbNumbers);
        workCompletionInfo.setWorkCommencedOn(workCommencedDate);
        workCompletionInfo.setWorkflowHistory(history);
        return workCompletionInfo;
    }

    @Override
    public List<WorkCompletionDetailInfo> setWorkCompletionDetailInfoList(final WorkOrderEstimate workOrderEstimate) {
        final List<WorkCompletionDetailInfo> workCompletionDetailInfoList = new ArrayList<>();
        final TenderResponse tenderResponse = tenderResponseService.find(
                "from TenderResponse tr where tr.negotiationNumber=?", workOrderEstimate.getWorkOrder()
                        .getNegotiationNumber());
        final String rebatePremLevel = worksService.getWorksConfigValue("REBATE_PREMIUM_LEVEL");

        final List<Object[]> workOrderActivityIdList = genericService.findAllByNamedQuery(
                WorksConstants.QUERY_GETALLWORKORDERACTIVITYWITHMB, workOrderEstimate.getId(), WorksConstants.APPROVED);
        for (final Object[] object : workOrderActivityIdList) {
            final WorkOrderActivity woa = (WorkOrderActivity) genericService.find(
                    "from WorkOrderActivity woa where woa.id=?", Long.parseLong(object[0].toString()));
            final WorkCompletionDetailInfo workCompletionDetailInfo = new WorkCompletionDetailInfo(woa,
                    Double.parseDouble(object[1].toString()));
            double executionRate;

            final List<String> tenderTypeList = worksService.getTendertypeList();
            if (tenderTypeList != null && !tenderTypeList.isEmpty()
                    && tenderResponse.getTenderEstimate().getTenderType().equals(tenderTypeList.get(0))
                    && rebatePremLevel.equalsIgnoreCase(WorksConstants.BILL)) {
                final double rebpremRate = woa.getApprovedRate()
                        * (Math.abs(tenderResponse.getPercNegotiatedAmountRate()) / 100);
                if (tenderResponse.getPercNegotiatedAmountRate() > 0)
                    executionRate = woa.getApprovedRate() + rebpremRate;
                else
                    executionRate = woa.getApprovedRate() - rebpremRate;
            } else
                executionRate = woa.getApprovedRate();
            workCompletionDetailInfo.setExecutionRate(executionRate);

            if (woa.getActivity().getSchedule() == null) {
                workCompletionDetailInfo.setTenderAmount(woa.getActivity().getQuantity()
                        * woa.getActivity().getRate());
                workCompletionDetailInfo.setExecutionAmount(executionRate * Double.parseDouble(object[1].toString()));
            } else {

                final Map<String, Integer> exceptionaSorMap = getSpecialUoms();
                double result = 1;
                if (exceptionaSorMap.containsKey(woa.getActivity().getUom().getUom()))
                    result = exceptionaSorMap.get(woa.getActivity().getUom().getUom());
                workCompletionDetailInfo.setTenderAmount(woa.getActivity().getQuantity() * woa.getScheduleOfRate()
                        / result);

                workCompletionDetailInfo.setExecutionAmount(executionRate * Double.parseDouble(object[1].toString())
                        / result);

            }
            workCompletionDetailInfoList.add(workCompletionDetailInfo);
        }
        final List<WorkOrderActivity> workOrderActivityWithoutMBList = genericService.findAllByNamedQuery(
                WorksConstants.QUERY_GETALLWORKORDERACTIVITYWITHOUTMB, workOrderEstimate.getId(),
                WorksConstants.CANCELLED_STATUS, workOrderEstimate.getId(), WorksConstants.APPROVED);
        for (final WorkOrderActivity woa : workOrderActivityWithoutMBList) {
            final WorkCompletionDetailInfo workCompletionDetailInfo = new WorkCompletionDetailInfo(woa,
                    Double.parseDouble("0"));
            workCompletionDetailInfo.setTenderAmount(woa.getApprovedRate() * woa.getApprovedQuantity());
            workCompletionDetailInfo.setExecutionAmount(Double.parseDouble("0"));
            workCompletionDetailInfoList.add(workCompletionDetailInfo);
        }

        return workCompletionDetailInfoList;
    }

    /**
     * This method will return Bill amount for a given Bill
     *
     * @param workOrderId
     * @return
     */
    @Override
    public BigDecimal getApprovedMBAmountforBill(final ContractorBillRegister contractorBillRegister) {
        BigDecimal result = BigDecimal.ZERO;
        Object[] params = new Object[]{WorksConstants.APPROVED, contractorBillRegister.getId()};
        // NOTE -- Here we will not consider legacy MBs -- the named query below
        // has been modified for this purpose
        List<Object[]> approvedMBsList = genericService.findAllByNamedQuery("getMBAmountForBill", params);
        params = new Object[]{contractorBillRegister.getId(), WorksConstants.CANCELLED_STATUS};
        // NOTE -- Here also we will not consider legacy MBs -- the named query
        // below has been modified for this purpose
        List<MBHeader> approvedMBsForCancelledBillList = genericService.findAllByNamedQuery("getMBListForCancelledBill", params);

        if (approvedMBsForCancelledBillList.isEmpty()) {
            Double amount;
            Iterator iter1;
            Iterator iter2;
            final List<Long> woaIdsListForApprovedMBs = new ArrayList<>();
            iter1 = approvedMBsList.iterator();

            while (iter1.hasNext()) {
                final Object[] obj = (Object[]) iter1.next();
                woaIdsListForApprovedMBs.add((Long) obj[0]);
            }

            final List<WorkOrderActivity> woaListForApprovedMBs = getWorkOrderActivityListForIds(woaIdsListForApprovedMBs);
            iter2 = approvedMBsList.iterator();
            while (iter2.hasNext()) {
                final Object[] obj = (Object[]) iter2.next();
                final Long woaId = (Long) obj[0];
                final double mbQuantity = (Double) obj[1];
                for (final WorkOrderActivity woa : woaListForApprovedMBs)
                    if (woaId.equals(woa.getId())) {
                        if (woa.getActivity().getNonSor() == null)
                            amount = woa.getApprovedRate() * mbQuantity * woa.getConversionFactor();
                        else
                            amount = woa.getApprovedRate() * mbQuantity;
                        result = result.add(BigDecimal.valueOf(amount));
                        break;
                    }
            }
        } else
            for (final MBHeader mbh : approvedMBsForCancelledBillList) {
                List<MBDetails> mbdetails = mbh.getMbDetails();
                for (final MBDetails mbd : mbdetails) {
                    Double amount;
                    if (mbd.getWorkOrderActivity().getActivity().getNonSor() == null)
                        amount = mbd.getWorkOrderActivity().getApprovedRate() * mbd.getQuantity()
                                * mbd.getWorkOrderActivity().getConversionFactor();
                    else
                        amount = mbd.getWorkOrderActivity().getApprovedRate() * mbd.getQuantity();
                    result = result.add(BigDecimal.valueOf(amount));
                }
            }
        return result;
    }

    /**
     * This method will return Bill amount for a given Bill
     *
     * @param workOrderId
     * @return
     */
    @Override
    public BigDecimal getApprovedMBAmountOfTenderedItemsForBill(final ContractorBillRegister contractorBillRegister) {
        BigDecimal result = BigDecimal.ZERO;
        Object[] params = new Object[]{WorksConstants.APPROVED, contractorBillRegister.getId()};
        // NOTE -- Here we will not consider legacy MBs -- the named query below
        // has been modified for this purpose
        List<Object[]> approvedMBsList = genericService.findAllByNamedQuery("getMBAmountForBill", params);
        params = new Object[]{contractorBillRegister.getId(), WorksConstants.CANCELLED_STATUS};
        // NOTE -- Here also we will not consider legacy MBs -- the named query
        // below has been modified for this purpose
        List<MBHeader> approvedMBsForCancelledBillList = genericService.findAllByNamedQuery("getMBListForCancelledBill", params);

        Double amount;
        if (approvedMBsForCancelledBillList.isEmpty()) {
            final List<Long> woaIdsListForApprovedMBs = new ArrayList<>();
            final Iterator iter1 = approvedMBsList.iterator();

            while (iter1.hasNext()) {
                final Object[] obj = (Object[]) iter1.next();
                woaIdsListForApprovedMBs.add((Long) obj[0]);
            }

            final List<WorkOrderActivity> woaListForApprovedMBs = getWorkOrderActivityListForIds(woaIdsListForApprovedMBs);
            final Iterator iter2 = approvedMBsList.iterator();
            while (iter2.hasNext()) {
                final Object[] obj = (Object[]) iter2.next();
                final Long woaId = (Long) obj[0];
                final double mbQuantity = (Double) obj[1];
                for (final WorkOrderActivity woa : woaListForApprovedMBs)
                    if (woaId.equals(woa.getId())) {
                        if (woa.getActivity().getNonSor() == null)
                            amount = woa.getApprovedRate() * mbQuantity * woa.getConversionFactor();
                        else
                            amount = woa.getApprovedRate() * mbQuantity;
                        if (woa.getActivity().getRevisionType() == null) {
                            result = result.add(BigDecimal.valueOf(amount));
                            break;
                        }
                    }

            }
        } else
            for (final MBHeader mbh : approvedMBsForCancelledBillList) {
                List<MBDetails> mbdetails = mbh.getMbDetails();
                for (final MBDetails mbd : mbdetails) {

                    if (mbd.getWorkOrderActivity().getActivity().getNonSor() == null)
                        amount = mbd.getWorkOrderActivity().getApprovedRate() * mbd.getQuantity()
                                * mbd.getWorkOrderActivity().getConversionFactor();
                    else
                        amount = mbd.getWorkOrderActivity().getApprovedRate() * mbd.getQuantity();
                    if (mbd.getWorkOrderActivity().getActivity().getRevisionType() == null)
                        result = result.add(BigDecimal.valueOf(amount));
                }
            }
        return result;
    }

    private Map<String, Integer> getSpecialUoms() {
        return worksService.getExceptionSOR();
    }

    public void setEgovCommon(final EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }

    public void setChecklistService(final PersistenceService<EgChecklists, Long> checklistService) {
        this.checklistService = checklistService;
    }

    public String getFinalBillTypeConfigValue() {
        return worksService.getWorksConfigValue("FinalBillType");
    }

    public void setTenderResponseService(final TenderResponseService tenderResponseService) {
        this.tenderResponseService = tenderResponseService;
    }

    @Override
    public Double getTotalActualExpenseForProject(final AbstractEstimate estimate, final Date asonDate) {
        Double totalExpense = 0.0;
        if (estimate == null || asonDate == null)
            throw new ApplicationRuntimeException("Invalid Arguments passed to getTotalActualExpenseForProject()");
        else
            LOGGER.debug("Start of getTotalActualExpenseForProject() ||estimate=" + estimate.getEstimateNumber()
                    + "||asonDate=||" + asonDate);

        if (estimate.getProjectCode() != null && estimate.getProjectCode().getEgwStatus() != null
                && PROJECT_STATUS_CLOSED.equalsIgnoreCase(estimate.getProjectCode().getEgwStatus().getCode())
                && !DateConversionUtil.isBeforeByDate(asonDate, estimate.getProjectCode().getCompletionDate())) {
            LOGGER.debug("Project code <<" + estimate.getProjectCode().getCode() + ">> is closed");
            totalExpense = estimate.getProjectCode().getProjectValue();
        } else {
            if (estimate.getProjectCode() != null)
                LOGGER.debug("Project having project code <<" + estimate.getProjectCode().getCode() + ">> is running");
            else
                LOGGER.debug("Project having estimate number <<" + estimate.getEstimateNumber()
                        + ">> is in the workflow");
            for (final EgBillregister egbr : getListOfApprovedBillforEstimate(estimate, asonDate))
                totalExpense = totalExpense + egbr.getBillamount().doubleValue();
        }
        if (estimate.getProjectCode() != null)
            LOGGER.debug("Actual Expense for the project " + estimate.getProjectCode().getCode() + "||expense amount "
                    + totalExpense);
        LOGGER.debug("End of getTotalActualExpenseForProject() ");
        return totalExpense == null ? 0.0d : totalExpense;
    }

    @Override
    public List<EgBillregister> getListOfApprovedBillforEstimate(final AbstractEstimate estimate, final Date date) {
        List<EgBillregister> egBillRegisterList = null;
        Query query = null;
        if (estimate == null || date == null)
            throw new ApplicationRuntimeException("Invalid Arguments passed to getApprovedBillAmountforEstimate()");
        else
            LOGGER.debug("Arguments passed to getListOfApprovedBillforEstimate() ||estimate "
                    + estimate.getEstimateNumber() + "||date=" + date);
        if (estimate.getDepositCode() != null) {
            LOGGER.debug("Estimate is of DEPOSIT WORKS|| estimate Number " + estimate.getEstimateNumber());
            query = persistenceService
                    .getSession()
                    .createQuery(
                            "select distinct egbr from MBHeader as mbh left outer join mbh.egBillregister egbr left outer join egbr.egBillregistermis egbrmis where mbh.workOrderEstimate.estimate.id=:estimateId and egbr.status.code=:code and trunc(egbr.billdate)<=trunc(:date) ");
            query.setLong("estimateId", estimate.getId());
            query.setDate("date", date);
            query.setString("code", "APPROVED");
            egBillRegisterList = query.list();
        } else {
            LOGGER.debug("Estimate is of CAPITAL WORKS|| estimate Number " + estimate.getEstimateNumber());
            query = persistenceService
                    .getSession()
                    .createQuery(
                            "select distinct egbr from MBHeader as mbh left outer join mbh.egBillregister egbr where mbh.workOrderEstimate.estimate.id=:estimateId "
                                    + "and egbr.status.code=:code and trunc(egbr.billdate)<=trunc(:date) ");
            query.setLong("estimateId", estimate.getId());
            query.setDate("date", date);
            query.setString("code", "APPROVED");
            egBillRegisterList = query.list();
        }
        if (egBillRegisterList == null)
            egBillRegisterList = Collections.emptyList();

        LOGGER.debug("Number of Approved bills for ||estimate " + estimate.getEstimateNumber() + "||date=" + date
                + "||is " + egBillRegisterList.size());
        LOGGER.debug(">>>>>>End of getListOfApprovedBillforEstimate()>>>>>>");
        return egBillRegisterList;

    }

    // for multiyear estimate appropriation
    @Override
    public BigDecimal getBilledAmountForDate(final AbstractEstimate estimate, final Date asOnDate) {
        LOGGER.debug("<<<<<<<<<<<<<<< Start of getBilledAmountForDate(AbstractEstimate estimate,Date asOnDate >>>>>>>>>>>>>");
        if (estimate == null || asOnDate == null)
            throw new ApplicationRuntimeException("Invalid Arguments passed to getApprovedBillAmountforEstimate()");
        else
            LOGGER.debug("Arguments passed to getBilledAmountForDate(AbstractEstimate estimate,Date asOnDate) ||estimate "
                    + estimate + "||asOnDate=" + asOnDate);
        final List<Map<String, String>> voucherDetails = egovCommon.getExpenditureDetailsforProject(estimate
                .getProjectCode().getId(), asOnDate);
        LOGGER.debug("total voucher created for project code  <<" + estimate.getProjectCode().getCode() + ">> is "
                + voucherDetails);
        final ArrayList<String> voucherNumbers = new ArrayList<>();
        BigDecimal totalVoucherAmount = BigDecimal.ZERO;
        if (voucherDetails != null && !voucherDetails.isEmpty())
            for (final Map<String, String> voucher : voucherDetails) {
                voucherNumbers.add(voucher.get("VoucherNumber"));
                totalVoucherAmount = totalVoucherAmount.add(BigDecimal.valueOf(Double.parseDouble(voucher.get("Amount"))));
            }
        LOGGER.debug("Total amount of vouchers(Contractor bills including overheads) | " + totalVoucherAmount);
        String queryString = "select sum(egbr.billamount) from MBHeader as mbh left outer join mbh.egBillregister egbr left outer join egbr.egBillregistermis egbrmis where mbh.workOrderEstimate.estimate.id=:estimateId "
                + "and trunc(egbr.billdate)<=trunc(:date) and egbr.status.code=:code";
        if (!voucherNumbers.isEmpty())
            queryString = queryString + " and egbrmis.voucherHeader.voucherNumber not in (:voucherNumbers)";
        queryString = queryString + " group by mbh.workOrderEstimate.estimate.id";

        final Query query = persistenceService.getSession().createQuery(queryString);
        query.setLong("estimateId", estimate.getId());
        query.setDate("date", new Date());
        query.setString("code", "APPROVED");
        if (!voucherNumbers.isEmpty())
            query.setParameterList("voucherNumbers", voucherNumbers);
        BigDecimal totalBillAmount = (BigDecimal) query.uniqueResult();

        LOGGER.debug("Total amount of contractor bills (Vouchers amount not included in this contractor bill amount) | "
                + totalBillAmount);

        if (totalBillAmount == null)
            totalBillAmount = BigDecimal.ZERO;
        LOGGER.debug(
                "End of getBilledAmountForDate(AbstractEstimate estimate,Date asOnDate) ||returned value is (including voucher amount and contractor bill)"
                        + totalBillAmount.add(totalVoucherAmount));

        return totalBillAmount.add(totalVoucherAmount);
    }

    @Override
    public BigDecimal getBilledAmount(final AbstractEstimate estimate) {
        if (estimate == null)
            throw new ApplicationRuntimeException("Invalid Arguments passed to getApprovedBillAmountforEstimate()");
        LOGGER.debug("Arguments passed to getBilledAmount(AbstractEstimate estimate) ||estimate "
                + estimate.getEstimateNumber() + "||today date=" + new Date());
        final List<Map<String, String>> voucherDetails = egovCommon.getExpenditureDetailsforProjectforFinYear(estimate
                .getProjectCode().getId(), new Date());
        LOGGER.debug("total voucher created for project code  <<" + estimate.getProjectCode().getCode() + ">> is "
                + voucherDetails);
        final ArrayList<String> voucherNumbers = new ArrayList<>();
        BigDecimal totalVoucherAmount = BigDecimal.ZERO;
        if (voucherDetails != null && !voucherDetails.isEmpty())
            for (final Map<String, String> voucher : voucherDetails) {
                voucherNumbers.add(voucher.get("VoucherNumber"));
                totalVoucherAmount = totalVoucherAmount.add(BigDecimal.valueOf(Double.parseDouble(voucher.get("Amount"))));
            }
        LOGGER.debug("Total amount of vouchers(Contractor bills including overheads) | " + totalVoucherAmount);
        String queryString = "select sum(egbr.billamount) from MBHeader as mbh left outer join mbh.egBillregister egbr left outer join egbr.egBillregistermis egbrmis where mbh.workOrderEstimate.estimate.id=:estimateId "
                + "and EXISTS (select 'true' from CFinancialYear cfinancialyear where trunc(cfinancialyear.startingDate)<=trunc(:date) and trunc(cfinancialyear.endingDate)>=trunc(:date) "
                + "and cfinancialyear.id=egbrmis.financialyear.id) and egbr.status.code=:code";
        if (!voucherNumbers.isEmpty())
            queryString = queryString + " and egbrmis.voucherHeader.voucherNumber not in (:voucherNumbers)";
        queryString = queryString + " group by mbh.workOrderEstimate.estimate.id";

        final Query query = persistenceService.getSession().createQuery(queryString);
        query.setLong("estimateId", estimate.getId());
        query.setDate("date", new Date());
        query.setString("code", "APPROVED");
        if (!voucherNumbers.isEmpty())
            query.setParameterList("voucherNumbers", voucherNumbers);
        BigDecimal totalBillAmount = (BigDecimal) query.uniqueResult();

        LOGGER.debug("Total amount of contractor bills (Vouchers amount not included in this contractor bill amount) | "
                + totalBillAmount);

        if (totalBillAmount == null)
            totalBillAmount = BigDecimal.ZERO;
        LOGGER.debug(
                "End of getBilledAmount(AbstractEstimate estimate) ||returned value is (including voucher amount and contractor bill)"
                        + totalBillAmount.add(totalVoucherAmount));

        return totalBillAmount.add(totalVoucherAmount);
    }

    @Override
    public List<EgBillregister> getListOfNonCancelledBillsforEstimate(final AbstractEstimate estimate, final Date date) {
        List<EgBillregister> egBillRegisterList = null;
        Query query = null;
        if (estimate == null || date == null)
            throw new ApplicationRuntimeException("Invalid Arguments passed to getApprovedBillAmountforEstimate()");
        else
            LOGGER.debug("Arguments passed to getListOfApprovedBillforEstimate() ||estimate "
                    + estimate.getEstimateNumber() + "||date=" + date);
        if (estimate.getDepositCode() != null) {
            LOGGER.debug("Estimate is of DEPOSIT WORKS|| estimate Number " + estimate.getEstimateNumber());
            query = persistenceService
                    .getSession()
                    .createQuery(
                            "select egbr from MBHeader as mbh left outer join mbh.egBillregister egbr left outer join egbr.egBillregistermis egbrmis where mbh.workOrderEstimate.estimate.id=:estimateId and egbr.status.code!=:code and trunc(egbr.billdate)<=trunc(:date) ");
            query.setLong("estimateId", estimate.getId());
            query.setDate("date", date);
            query.setString("code", "CANCELLED");
            egBillRegisterList = query.list();
        } else {
            LOGGER.debug("Estimate is of CAPITAL WORKS|| estimate Number " + estimate.getEstimateNumber());
            query = persistenceService
                    .getSession()
                    .createQuery(
                            "select egbr from MBHeader as mbh left outer join mbh.egBillregister egbr where mbh.workOrderEstimate.estimate.id=:estimateId "
                                    + "and egbr.status.code!=:code and trunc(egbr.billdate)<=trunc(:date) ");
            query.setLong("estimateId", estimate.getId());
            query.setDate("date", date);
            query.setString("code", "CANCELLED");
            egBillRegisterList = query.list();
        }
        if (egBillRegisterList == null)
            egBillRegisterList = Collections.emptyList();

        LOGGER.debug("Number of Approved bills for ||estimate " + estimate.getEstimateNumber() + "||date=" + date
                + "||is " + egBillRegisterList.size());
        LOGGER.debug(">>>>>>End of getListOfApprovedBillforEstimate()>>>>>>");
        return egBillRegisterList;

    }

    private List<WorkOrderActivity> getWorkOrderActivityListForIds(final List<Long> woaIds) {
        final Query createQuery = persistenceService.getSession().createQuery(
                " from WorkOrderActivity woa where woa.id in (:woActivityIds) ");
        createQuery.setParameterList("woActivityIds", woaIds);
        return createQuery.list();
    }

    public void setContractorAdvanceService(final ContractorAdvanceService contractorAdvanceService) {
        this.contractorAdvanceService = contractorAdvanceService;
    }

    public String getBudgetHeadFromMappingObject(final String depositCOA) {
        return (String) genericService.find(
                "select workDoneBudgetGroup from DepositCOABudgetHead where depositCOA = ?", depositCOA);
    }

    @Override
    public List<CChartOfAccounts> getBudgetHeadForDepositCOA(final AbstractEstimate estimate) {
        List<CChartOfAccounts> coaList = new ArrayList<>();
        final String estimateDepositCOA = estimate.getFinancialDetails().get(0).getCoa().getGlcode();
        final String mappingGLCode = getBudgetHeadFromMappingObject(estimateDepositCOA);
        if (StringUtils.isNotBlank(mappingGLCode))
            coaList = Arrays.asList(chartOfAccountsHibernateDAO.getCChartOfAccountsByGlCode(mappingGLCode));
        return coaList;
    }

    @Override
    public String validateForBudgetHeadInWorkflow(final Set<EgBilldetails> billDetails, final AbstractEstimate estimate) {
        String allowForward = WorksConstants.YES;
        final String mappingBudgetHead = getBudgetHeadFromMappingObject(estimate.getFinancialDetails().get(0).getCoa()
                .getGlcode());
        if (StringUtils.isNotBlank(mappingBudgetHead))
            for (final EgBilldetails details : billDetails)
                if (details.getDebitamount() != null && details.getDebitamount().compareTo(BigDecimal.ZERO) > 0) {
                    final CChartOfAccounts coaObj = chartOfAccountsHibernateDAO.findById(details
                            .getGlcodeid(), false);
                    if (coaObj != null && StringUtils.isNotBlank(coaObj.getGlcode())
                            && !mappingBudgetHead.equalsIgnoreCase(coaObj.getGlcode())) {
                            allowForward = WorksConstants.NO;
                            break;
                    }
                }
        return allowForward;
    }

    /**
     * @param - search fundId, coaId, depositCodeId
     * @return - returns list of project codes
     * @description -This method returns the list of project code ids for a fund-coa-deposit code combination
     */
    @Override
    public List<Integer> getProjCodeIdsListForDepositCode(final Integer fundId, final Long coaId,
                                                          final Long depositCodeId) {
        final List<Long> pcIds = genericService
                .findAllBy(
                        "select distinct fd.abstractEstimate.projectCode.id from FinancialDetail fd where fd.abstractEstimate.egwStatus.code = ?"
                                + " and fd.abstractEstimate.depositCode.id = ? and fd.fund.id = ? and fd.coa.id = ?",
                        AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString(), depositCodeId, fundId, coaId);
        final List<Integer> projCodeIds = new ArrayList<>();
        if (pcIds != null && !pcIds.isEmpty())
            for (final Long id : pcIds)
                projCodeIds.add(id.intValue());
        return projCodeIds;
    }

    /**
     * @param - search projectCodeIdsList, accDetailType
     * @return - returns expenditure incurred
     * @description -This method returns the total expenditure incurred for the project codes
     */

    @Override
    public BigDecimal getTotalExpenditure(final List<Integer> projectCodeIdsList, final String accDetailType) {
        final Integer accDetailTypeId = (Integer) genericService.find("select id from Accountdetailtype where name=?",
                accDetailType);
        final BigDecimal totalBillAmt = getTotalBillAmount(new Date(), projectCodeIdsList);
        final BigDecimal voucherExpdAmount = egovCommon.getVoucherExpenditureByEntities(accDetailTypeId,
                projectCodeIdsList);
        final BigDecimal dbpExpdAmount = egovCommon.getDirectBankPaymentExpenditureByEntities(accDetailTypeId,
                projectCodeIdsList);

        return totalBillAmt.add(voucherExpdAmount).add(dbpExpdAmount);
    }

    /**
     * @param - search current date, list of project code ids, accDetailTypeId
     * @return - returns sum of bill amount
     * @description -This method calculates the sum of bill amount for bills where voucher is not present
     */
    public BigDecimal getTotalBillAmount(final Date asOnDate, final List<Integer> projectCodeIdsList) {
        List billAmountResult;
        BigDecimal totalBillAmount = BigDecimal.ZERO;

        final String payQuery = " SELECT coalesce(sum(br.BILLAMOUNT),0) AS \"Total Bill Amount\" FROM EG_BILLPAYEEDETAILS bpd, EG_BILLDETAILS bd, EG_BILLREGISTER br, EG_BILLREGISTERMIS mis "
                + " WHERE bpd.BILLDETAILID = bd.ID AND bd.BILLID = br.ID AND br.ID = mis.BILLID AND br.BILLSTATUS != '"
                + WorksConstants.CANCELLED_STATUS
                + "' "
                + "AND bpd.ACCOUNTDETAILTYPEID=(SELECT ID FROM ACCOUNTDETAILTYPE WHERE NAME='PROJECTCODE') AND bpd.ACCOUNTDETAILKEYID IN (:projCodeIds) AND br.BILLDATE <=:date "
                + "AND ((mis.VOUCHERHEADERID   IS NULL) OR (mis.VOUCHERHEADERID IS NOT NULL AND EXISTS (SELECT id FROM voucherheader WHERE id=mis.VOUCHERHEADERID AND status="
                + FinancialConstants.CANCELLEDVOUCHERSTATUS + ")))";

        final Query query = persistenceService.getSession().createSQLQuery(payQuery);
        query.setParameterList("projCodeIds", projectCodeIdsList);
        query.setDate("date", asOnDate);
        billAmountResult = query.list();

        for (final Object obj : billAmountResult)
            totalBillAmount = BigDecimal.valueOf(Double.valueOf(obj.toString()));
        return totalBillAmount;
    }

    @Override
    public Object[] getLatestMBCreatedDateAndRefNo(final Long woId, final Long estId) {
        return (Object[]) persistenceService
                .getSession()
                .createQuery(
                        "select mbRefNo,mbDate from MBHeader where id = "
                                + "(select max(mbh.id) from MBHeader mbh where mbh.egwStatus.code = ? and "
                                + "mbh.workOrder.id= ? and mbh.workOrderEstimate.estimate.id=? and "
                                + "mbh.workOrderEstimate.estimate.egwStatus.code= ? )")
                .setParameter(0, WorksConstants.APPROVED).setParameter(1, woId).setParameter(2, estId)
                .setParameter(3, WorksConstants.ADMIN_SANCTIONED_STATUS).uniqueResult();
    }

    @Override
    public Collection<StatutoryDeductionsForBill> getStatutoryDeductions(
            final List<StatutoryDeductionsForBill> actionStatutorydetails) {
        return CollectionUtils.select(actionStatutorydetails,
                statutoryDeductionsForBill -> (StatutoryDeductionsForBill) statutoryDeductionsForBill != null);
    }

    @Override
    public Collection<EgBilldetails> getCustomDeductionTypes(final List<EgBilldetails> customDeductions) {
        return CollectionUtils.select(customDeductions, egBilldetails -> (EgBilldetails) egBilldetails != null);
    }

    @Override
    public Collection<EgBilldetails> getRetentionMoneyTypes(final List<EgBilldetails> retentionMoneyDeductions) {
        return CollectionUtils.select(retentionMoneyDeductions, egBilldetails -> (EgBilldetails) egBilldetails != null);
    }

    @Override
    public Collection<AssetForBill> getAssetAndAccountDetails(final List<AssetForBill> accountDetailsForBill) {
        return CollectionUtils.select(accountDetailsForBill, assetForBill -> (AssetForBill) assetForBill != null);
    }

    @Override
    public Collection<DeductionTypeForBill> getStandardDeductionTypes(final List<DeductionTypeForBill> standardDeductions) {
        return CollectionUtils.select(standardDeductions,
                deductionTypeForBill -> (DeductionTypeForBill) deductionTypeForBill != null);
    }
}