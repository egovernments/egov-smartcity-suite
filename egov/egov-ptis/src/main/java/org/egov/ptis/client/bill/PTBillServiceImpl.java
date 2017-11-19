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
package org.egov.ptis.client.bill;

import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_FIRST_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_SECOND_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_PENALTY_FINES;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_REBATE;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_ADVANCE;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODE_FOR_ADVANCE;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODE_FOR_MUTATION_FEE;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODE_FOR_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.MAX_ADVANCES_ALLOWED;
import static org.egov.ptis.constants.PropertyTaxConstants.MUTATION_FEE_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.egov.collection.integration.models.BillAccountDetails.PURPOSE;
import org.egov.commons.Installment;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.interfaces.BillServiceInterface;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.demand.utils.DemandConstants;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.client.model.PenaltyAndRebate;
import org.egov.ptis.client.util.FinancialUtil;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class PTBillServiceImpl extends BillServiceInterface {
    private static final Logger LOGGER = Logger.getLogger(PTBillServiceImpl.class);

    @Autowired
    PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private PtDemandDao ptDemandDAO;

    @Autowired
    private DemandGenericDao demandGenericDAO;

    @Autowired
    private ModuleService moduleDao;

    @Autowired
    private EgBillDao egBillDAO;

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @Autowired
    private FinancialUtil financialUtil;

    @Override
    public String getBillXML(final Billable billObj) {
        if (billObj == null)
            throw new ApplicationRuntimeException("Exception in getBillXML....Billable is null");
        return super.getBillXML(billObj);
    }

    /**
     * Setting the EgBillDetails to generate XML as a part of Erpcollection
     * Integration
     *
     * @see org.egov.demand.interfaces.BillServiceInterface
     */
    @Override
    public List<EgBillDetails> getBilldetails(final Billable billObj) {
        final List<EgBillDetails> billDetails = new ArrayList();
        LOGGER.debug("Entered method getBilldetails : " + billObj);
        EgBillDetails billdetail;
        final PropertyTaxBillable billable = (PropertyTaxBillable) billObj;

        if (billable.isMutationFeePayment()) {
            final Installment currInstallment = propertyTaxCommonUtils.getCurrentInstallment();
            billdetail = new EgBillDetails();
            billdetail.setOrderNo(1);
            billdetail.setCreateDate(new Date());
            billdetail.setModifiedDate(new Date());
            billdetail.setCrAmount(billable.getMutationFee());
            billdetail.setDrAmount(BigDecimal.ZERO);
            billdetail.setGlcode(GLCODE_FOR_MUTATION_FEE);
            billdetail.setDescription(MUTATION_FEE_STR);
            billdetail.setAdditionalFlag(Integer.valueOf(0));
            billdetail.setEgInstallmentMaster(currInstallment);
            billdetail.setAdditionalFlag(Integer.valueOf(1));
            billdetail.setPurpose(PURPOSE.OTHERS.toString());
            billdetail.setFunctionCode(financialUtil.getFunctionCode());
            billDetails.add(billdetail);
            return billDetails;
        }

        String key;
        BigDecimal balance;
        BigDecimal earlyPayRebate = BigDecimal.ZERO;
        DateTime installmentDate;
        BillDetailBean billDetailBean;
        EgDemandReason reason;
        Installment installment;
        String reasonMasterCode;
        final BasicProperty basicProperty = billable.getBasicProperty();
        final Property activeProperty = basicProperty.getProperty();
        TreeMap<Installment, PenaltyAndRebate> installmentPenaltyAndRebate = (TreeMap<Installment, PenaltyAndRebate>) billable.getCalculatedPenalty();
        Map<String, Installment> currInstallments = propertyTaxUtil.getInstallmentsForCurrYear(new Date());

        Date advanceStartDate = DateUtils.addYears(currInstallments.get(CURRENTYEAR_FIRST_HALF).getFromDate(), 1);
        List<Installment> advanceInstallments = propertyTaxCommonUtils.getAdvanceInstallmentsList(advanceStartDate);

        billable.setInstTaxBean(installmentPenaltyAndRebate);
        if (installmentPenaltyAndRebate.get(currInstallments.get(CURRENTYEAR_FIRST_HALF)) != null) {
            earlyPayRebate = installmentPenaltyAndRebate.get(currInstallments.get(CURRENTYEAR_FIRST_HALF)).getRebate();
        }
        final Ptdemand ptDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(activeProperty);
        final HashMap<String, Integer> orderMap = propertyTaxUtil
                .generateOrderForDemandDetails(ptDemand.getEgDemandDetails(), billable, advanceInstallments);

        for (final EgDemandDetails demandDetail : ptDemand.getEgDemandDetails()) {
            balance = demandDetail.getAmount().subtract(demandDetail.getAmtCollected());
            reason = demandDetail.getEgDemandReason();
            installment = reason.getEgInstallmentMaster();
            reasonMasterCode = reason.getEgDemandReasonMaster().getCode();
            if (balance.compareTo(BigDecimal.ZERO) == 1) {
                installmentDate = new DateTime(installment.getInstallmentYear().getTime());

                if (isNotPenalty(reasonMasterCode)) {
                    key = installmentDate.getMonthOfYear() + "/" + installmentDate.getYear() + "-" + reasonMasterCode;
                    billDetailBean = new BillDetailBean(installment, orderMap.get(key), key,
                            demandDetail.getAmount().subtract(demandDetail.getAmtCollected()),
                            demandDetail.getEgDemandReason().getGlcodeId().getGlcode(),
                            reason.getEgDemandReasonMaster().getReasonMaster(), Integer.valueOf(1),
                            definePurpose(demandDetail));

                    billDetails.add(createBillDet(billDetailBean));
                }
            }
        }
        addBillDetailsForRebate(billDetails, earlyPayRebate, currInstallments, orderMap);

        getPenaltyAndRebateDmd(billDetails, billable, installmentPenaltyAndRebate, ptDemand, orderMap);

        // Get the demand for current year second half and use it in advance
        // collection
        BigDecimal currentInstDemand = BigDecimal.ZERO;
        for (EgDemandDetails dmdDet : ptDemand.getEgDemandDetails()) {
            if (isDmdForCurrYrSecHalf(currInstallments, dmdDet)) {
                currentInstDemand = currentInstDemand.add(dmdDet.getAmount());
            }
        }
        // Advance Bill details only if current tax is greater than zero.
        if (isCurrTaxGrtZero(currentInstDemand)) {
            createAdvanceBillDetails(billDetails, currentInstDemand, orderMap, ptDemand, billable, advanceInstallments,
                    currInstallments.get(CURRENTYEAR_SECOND_HALF));
        }

        LOGGER.debug("Exiting method getBilldetails : " + billDetails);
        return billDetails;
    }

    private void getPenaltyAndRebateDmd(final List<EgBillDetails> billDetails, final PropertyTaxBillable billable,
            TreeMap<Installment, PenaltyAndRebate> installmentPenaltyAndRebate, final Ptdemand ptDemand,
            final HashMap<String, Integer> orderMap) {
        EgDemandDetails penaltyDemandDetail;
        for (final Map.Entry<Installment, PenaltyAndRebate> penaltyAndRebate : installmentPenaltyAndRebate.entrySet()) {
            penaltyDemandDetail = insertPenaltyAndBillDetails(billDetails, billable, orderMap,
                    penaltyAndRebate.getValue().getPenalty(), penaltyAndRebate.getKey());
            if (penaltyDemandDetail != null)
                ptDemand.getEgDemandDetails().add(penaltyDemandDetail);
        }
    }

    private void addBillDetailsForRebate(final List<EgBillDetails> billDetails, BigDecimal earlyPayRebate,
            Map<String, Installment> currInstallments, final HashMap<String, Integer> orderMap) {
        String key;
        DateTime installmentDate;
        BillDetailBean billDetailBean;
        if (isCurrTaxGrtZero(earlyPayRebate)) {
            installmentDate = new DateTime(currInstallments.get(CURRENTYEAR_FIRST_HALF).getInstallmentYear().getTime());
            key = installmentDate.getMonthOfYear() + "/" + installmentDate.getYear() + "-" + DEMANDRSN_CODE_REBATE;
            billDetailBean = new BillDetailBean(currInstallments.get(CURRENTYEAR_FIRST_HALF), orderMap.get(key), key,
                    earlyPayRebate, PropertyTaxConstants.GLCODE_FOR_TAXREBATE, DEMANDRSN_CODE_REBATE,
                    Integer.valueOf(1), PURPOSE.REBATE.toString());
            billDetails.add(createBillDet(billDetailBean));
        }
    }

    private boolean isDmdForCurrYrSecHalf(Map<String, Installment> currInstallments, EgDemandDetails dmdDet) {
        return dmdDet.getInstallmentStartDate().equals(currInstallments.get(CURRENTYEAR_SECOND_HALF).getFromDate());
    }

    private boolean isCurrTaxGrtZero(BigDecimal currentInstDemand) {
        return currentInstDemand.compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean isNotPenalty(String reasonMasterCode) {
        return !reasonMasterCode.equalsIgnoreCase(PropertyTaxConstants.DEMANDRSN_CODE_PENALTY_FINES);
    }

    /**
     * Creates the advance bill details
     * 
     * @param billDetails
     * @param orderMap
     * @param currentInstallmentDemand
     * @param demandDetail
     * @param reason
     * @param installment
     */
    private void createAdvanceBillDetails(List<EgBillDetails> billDetails, BigDecimal currentInstallmentDemand,
            HashMap<String, Integer> orderMap, Ptdemand ptDemand, PropertyTaxBillable billable,
            List<Installment> advanceInstallments, Installment dmdDetInstallment) {

        BillDetailBean billDetailBean = null;
        /*
         * Advance will be created with current year second half installment.
         * While fetching advance collection, we will pass current year second
         * half installment
         */
        BigDecimal advanceCollection = demandGenericDAO.getBalanceByDmdMasterCodeInst(ptDemand, DEMANDRSN_CODE_ADVANCE,
                getModule(), dmdDetInstallment);

        if (advanceCollection.compareTo(BigDecimal.ZERO) < 0) {
            advanceCollection = advanceCollection.abs();
        }

        BigDecimal partiallyCollectedAmount = advanceCollection.remainder(currentInstallmentDemand);

        Integer noOfAdvancesPaid = (advanceCollection.subtract(partiallyCollectedAmount)
                .divide(currentInstallmentDemand)).intValue();

        LOGGER.debug(
                "getBilldetails - advanceCollection = " + advanceCollection + ", noOfAdvancesPaid=" + noOfAdvancesPaid);

        String key = null;
        DateTime installmentDate = null;
        Installment installment = null;
        if (noOfAdvancesPaid < MAX_ADVANCES_ALLOWED) {
            for (int i = noOfAdvancesPaid; i < advanceInstallments.size(); i++) {
                installment = advanceInstallments.get(i);
                installmentDate = new DateTime(installment.getInstallmentYear().getTime());
                key = installmentDate.getMonthOfYear() + "/" + installmentDate.getYear() + "-" + DEMANDRSN_CODE_ADVANCE;

                billDetailBean = new BillDetailBean(installment, orderMap.get(key), key,
                        i == noOfAdvancesPaid ? currentInstallmentDemand.subtract(partiallyCollectedAmount)
                                : currentInstallmentDemand,
                        GLCODE_FOR_ADVANCE, DEMANDRSN_STR_ADVANCE, Integer.valueOf(0),
                        PURPOSE.ADVANCE_AMOUNT.toString());
                billDetails.add(createBillDet(billDetailBean));
            }
        } else {
            LOGGER.debug("getBillDetails - All advances are paid...");
        }
    }

    private EgDemandDetails insertPenaltyAndBillDetails(final List<EgBillDetails> billDetails,
            final PropertyTaxBillable billable, final HashMap<String, Integer> orderMap, BigDecimal penalty,
            final Installment installment) {

        LOGGER.info("Entered into prepareDmdAndBillDetails");
        LOGGER.info("preapreDmdAndBillDetails- Installment : " + installment + ", Penalty Amount: " + penalty);
        String key = null;
        final EgDemandDetails penDmdDtls = getPenaltyDmdDtls(billable, installment);
        EgDemandDetails insertPenDmdDetail = null;

        final boolean thereIsPenalty = penalty != null
                && !(penalty.equals(BigDecimal.ZERO) || penalty.equals(BigDecimal.valueOf(0.0))) ? true : false;
        final DateTime installmentDate = new DateTime(installment.getInstallmentYear().getTime());

        // Checking whether to impose penalty or not
        if (billable.getLevyPenalty()) {
            /* do not create penalty demand details if penalty is zero */
            if (penDmdDtls == null && thereIsPenalty)
                insertPenDmdDetail = insertPenaltyDmdDetail(installment, penalty);
            else if (penDmdDtls != null)
                penalty = penDmdDtls.getAmount().subtract(penDmdDtls.getAmtCollected());
            if (thereIsPenalty) {
                key = installmentDate.getMonthOfYear() + "/" + installmentDate.getYear() + "-"
                        + DEMANDRSN_CODE_PENALTY_FINES;
                final BillDetailBean billDetailBean = new BillDetailBean(installment, orderMap.get(key), key, penalty,
                        GLCODE_FOR_PENALTY, PropertyTaxConstants.DEMANDRSN_STR_PENALTY_FINES, Integer.valueOf(1),
                        definePurpose(penDmdDtls));
                billDetails.add(createBillDet(billDetailBean));
            }
        }
        return insertPenDmdDetail;
    }

    private EgDemandDetails insertPenaltyDmdDetail(final Installment inst, final BigDecimal lpAmt) {
        return insertDemandDetails(DEMANDRSN_CODE_PENALTY_FINES, lpAmt, inst);
    }

    private EgDemandDetails getPenaltyDmdDtls(final Billable billObj, final Installment inst) {
        return getDemandDetail(billObj.getCurrentDemand(), inst, DEMANDRSN_CODE_PENALTY_FINES);
    }

    /**
     * Finds the demand-detail for the given installment and reason.
     *
     * @param egDemand
     * @param instl
     * @param code
     * @return
     */
    public EgDemandDetails getDemandDetail(final EgDemand egDemand, final Installment instl, final String code) {
        EgDemandDetails dmdDet = null;
        final List<EgDemandDetails> dmdDetList = demandGenericDAO.getDmdDetailList(egDemand, instl, module(),
                getDemandReasonMaster(code));
        if (!dmdDetList.isEmpty())
            dmdDet = dmdDetList.get(0);
        return dmdDet;
    }

    protected EgDemandReasonMaster getDemandReasonMaster(final String code) {
        return demandGenericDAO.getDemandReasonMasterByCode(code, module());
    }

    protected Module module() {
        return moduleDao.getModuleByName(PTMODULENAME);
    }

    /**
     * Method used to insert demand details in EgDemandDetail table.
     * 
     * @param demandReason
     * @param amount
     * @param inst
     * @return
     */
    public EgDemandDetails insertDemandDetails(final String demandReason, final BigDecimal amount,
            final Installment inst) {
        EgDemandDetails demandDetail = null;
        Module ptModule = null;

        if (amount != null && isCurrTaxGrtZero(amount)) {

            ptModule = module();
            final EgDemandReasonMaster egDemandReasonMaster = demandGenericDAO.getDemandReasonMasterByCode(demandReason,
                    ptModule);

            if (egDemandReasonMaster == null)
                throw new ApplicationRuntimeException(
                        demandReason + " Demand reason Master is null in method  insertDemandDetails");

            final EgDemandReason egDemandReason = demandGenericDAO
                    .getDmdReasonByDmdReasonMsterInstallAndMod(egDemandReasonMaster, inst, ptModule);

            if (egDemandReason == null)
                throw new ApplicationRuntimeException(
                        demandReason + " Demand reason is null in method  insertDemandDetails ");

            if (DEMANDRSN_CODE_ADVANCE.equals(egDemandReason.getEgDemandReasonMaster().getCode())) {
                demandDetail = createDemandDetails(egDemandReason, amount, BigDecimal.ZERO);
            } else {
                demandDetail = createDemandDetails(egDemandReason, BigDecimal.ZERO, amount);
            }
        }
        return demandDetail;
    }

    public EgDemandDetails createDemandDetails(final EgDemandReason egDemandReason, final BigDecimal amtCollected,
            final BigDecimal dmdAmount) {
        return EgDemandDetails.fromReasonAndAmounts(dmdAmount, egDemandReason, amtCollected);
    }

    /**
     * @param installment
     * @param orderNo
     * @param billDetAmt
     * @param rebateAmt
     * @param glCode
     * @param description
     * @return
     */
    EgBillDetails createBillDet(final BillDetailBean billDetailBean) {
        LOGGER.debug("Entered into createBillDet, billDetailBean=" + billDetailBean);

        if (billDetailBean.invalidData())
            throw new ApplicationRuntimeException("Invalid bill details...");

        final EgBillDetails billdetail = new EgBillDetails();
        billdetail.setOrderNo(billDetailBean.getOrderNo());
        billdetail.setCreateDate(new Date());
        billdetail.setModifiedDate(new Date());

        if (billDetailBean.isRebate()) {
            billdetail.setDrAmount(billDetailBean.getAmount());
            billdetail.setCrAmount(BigDecimal.ZERO);
        } else {
            billdetail.setCrAmount(billDetailBean.getAmount());
            billdetail.setDrAmount(BigDecimal.ZERO);
        }

        billdetail.setGlcode(billDetailBean.getGlCode());
        billdetail.setDescription(billDetailBean.getDescription());
        billdetail.setAdditionalFlag(billDetailBean.getIsActualDemand());
        // In case of currentInstallment >12, then currentInstallment - 13 is
        // stored.
        billdetail.setEgInstallmentMaster(billDetailBean.getInstallment());
        billdetail.setFunctionCode(financialUtil.getFunctionCode());
        billdetail.setPurpose(billDetailBean.getPurpose());
        LOGGER.debug("Exiting from createBillDet");
        return billdetail;
    }

    public EgBill updateBillWithLatest(final Long billId) {
        LOGGER.debug("updateBillWithLatest billId " + billId);
        final EgBill bill = egBillDAO.findById(billId, false);
        LOGGER.debug("updateBillWithLatest old bill " + bill);
        if (bill == null)
            throw new ApplicationRuntimeException("No bill found with bill reference no :" + billId);
        bill.getEgBillDetails().clear();
        final PropertyTaxBillable propertyTaxBillable = (PropertyTaxBillable) context.getBean("propertyTaxBillable");
        propertyTaxBillable.setReceiptDate(bill.getCreateDate());
        propertyTaxBillable.setLevyPenalty(true);
        propertyTaxBillable
                .setBasicProperty(basicPropertyDAO.getBasicPropertyByPropertyID(bill.getConsumerId().trim()));
        final List<EgBillDetails> egBillDetails = getBilldetails(propertyTaxBillable);
        for (final EgBillDetails billDetail : egBillDetails) {
            bill.addEgBillDetails(billDetail);
            billDetail.setEgBill(bill);
        }
        egBillDAO.update(bill);
        LOGGER.debug("Bill update with bill details for property tax " + bill.getConsumerId() + " as billdetails "
                + egBillDetails);
        return bill;
    }

    @Override
    public void cancelBill() {
    }

    public Module getModule() {
        return moduleService.getModuleByName(PropertyTaxConstants.PTMODULENAME);
    }

    public void setPropertyTaxUtil(final PropertyTaxUtil propertyTaxUtil) {
        this.propertyTaxUtil = propertyTaxUtil;
    }

    public String definePurpose(EgDemandDetails demandDetail) {
        String purpose = PURPOSE.OTHERS.toString();
       if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode().equalsIgnoreCase(PropertyTaxConstants.DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY)) {
            return PURPOSE.CHEQUE_BOUNCE_PENALTY.toString();
        }
        Map<String, Installment> currInstallments = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
        Date instStartDate = demandDetail.getInstallmentStartDate();
        if (instStartDate.equals(currInstallments.get(CURRENTYEAR_FIRST_HALF).getFromDate())
                || instStartDate.equals(currInstallments.get(CURRENTYEAR_SECOND_HALF).getFromDate())) {
            if (demandDetail.getReasonCategory().equalsIgnoreCase(DemandConstants.DEMAND_REASON_CATEGORY_TAX))
                return PURPOSE.CURRENT_AMOUNT.toString();
            else if (demandDetail.getReasonCategory().equalsIgnoreCase(DemandConstants.DEMAND_REASON_CATEGORY_FINES))
                return PURPOSE.CURRENT_LATEPAYMENT_CHARGES.toString();
            else
                return purpose;
        } else {
            if (demandDetail.getReasonCategory().equalsIgnoreCase(DemandConstants.DEMAND_REASON_CATEGORY_TAX))
                return PURPOSE.ARREAR_AMOUNT.toString();
            else if (demandDetail.getReasonCategory().equalsIgnoreCase(DemandConstants.DEMAND_REASON_CATEGORY_FINES))
                return PURPOSE.ARREAR_LATEPAYMENT_CHARGES.toString();
            else
                return purpose;
        }
    }
}
