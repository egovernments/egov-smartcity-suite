/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.adtax.service.collection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.adtax.entity.AdvertisementAdditionalTaxRate;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.entity.AgencyWiseCollection;
import org.egov.adtax.entity.AgencyWiseCollectionDetail;
import org.egov.adtax.repository.AdvertisementPermitDetailRepository;
import org.egov.adtax.service.AdvertisementAdditinalTaxRateService;
import org.egov.adtax.service.AdvertisementDemandService;
import org.egov.adtax.service.AdvertisementPermitDetailService;
import org.egov.adtax.service.AdvertisementService;
import org.egov.adtax.service.AgencyWiseCollectionService;
import org.egov.adtax.service.es.AdvertisementPermitDetailUpdateIndexService;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.BillReceiptInfoImpl;
import org.egov.collection.integration.models.ReceiptAccountInfo;
import org.egov.collection.integration.models.ReceiptAmountInfo;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.integration.TaxCollection;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdvertisementTaxCollection extends TaxCollection {
    private static final Logger LOGGER = Logger.getLogger(AdvertisementTaxCollection.class);

    @Autowired
    private EgBillDao egBillDAO;
    @Autowired
    private AdvertisementDemandService advertisementDemandService;
    @Autowired
    AgencyWiseCollectionService agencyWiseCollectionService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private AdvertisementPermitDetailRepository advertisementPermitDetailRepository;

    @Autowired
    private AdvertisementPermitDetailService advertisementPermitDetailService;

    @Autowired
    private AdvertisementPermitDetailUpdateIndexService advertisementPermitDetailUpdateIndexService;

    @Autowired
    private AdvertisementAdditinalTaxRateService advertisementAdditinalTaxRateService;
    
    private final Map<String, String> additionalTaxes = new HashMap<String, String>();

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public List<ReceiptDetail> reconstructReceiptDetail(final String billReferenceNumber,
            final BigDecimal actualAmountPaid, final List<ReceiptDetail> receiptDetailList) {

        return null;
    }

    /**
     * Collection will be possible either by agency wise or individual hoarding wise. Calling common api to update demand. If
     * demand present in agency wise collection object, then we will consider collection happened by Agency wise.
     */
    @Override
    @Transactional
    public void updateDemandDetails(final BillReceiptInfo billRcptInfo) {
        final BigDecimal totalAmount = billRcptInfo.getTotalAmount();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("updateDemandDetails : Demand updation for advertisement started. ");

        final List<AdvertisementAdditionalTaxRate> additionalTaxRates = advertisementAdditinalTaxRateService
                .getAllActiveAdditinalTaxRates();

        for (final AdvertisementAdditionalTaxRate taxRates : additionalTaxRates)
            additionalTaxes.put(taxRates.getTaxType(), taxRates.getReasonCode());

        EgDemand demand = getDemandByBillReferenceNumber(Long.valueOf(billRcptInfo.getBillReferenceNum()));
        final AgencyWiseCollection agencyWiseCollection = agencyWiseCollectionService.getAgencyWiseCollectionByDemand(demand);

        if (agencyWiseCollection != null) {

            agencyWiseCollection.setAmountCollected(Boolean.TRUE);
            /*
             * We are using demandupdated flag to check whether demand updated or not. We can use scheduler to update these
             * records in bulk if required.
             */
            agencyWiseCollection.setDemandUpdated(Boolean.TRUE);

            if (billRcptInfo.getEvent().equals(EVENT_RECEIPT_CREATED))
                updateAgencyWiseCollectionOnCreate(billRcptInfo, agencyWiseCollection, totalAmount);
            else if (billRcptInfo.getEvent().equals(EVENT_RECEIPT_CANCELLED))
                updateAgencyWiseCollectionOnCancel(demand, billRcptInfo, agencyWiseCollection, totalAmount);

        } else {
            demand = generalDemandUpdationForAdvertisement(billRcptInfo, totalAmount);
            updateWorkflowState(demand);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("updateDemandDetails : Demand updation processed. ");

    }

    /**
     * Iterate each agency detail and update collected amount.Assumption is full amount will be collected from collection system.
     * Penalty we need to add as fresh entry in demand detail.
     * @param totalAmount
     */
    @Transactional
    private void updateAgencyWiseCollectionOnCreate(final BillReceiptInfo billRcptInfo,
            final AgencyWiseCollection agencyWiseCollection, final BigDecimal totalAmount) {

        /**
         * Whether we will adjust arrears of all demand first ? or financial year wise first.. and then last financial year ? or
         * clear individual advertisement wise ? make it configurable. final HashMap<EgDemandReason, BigDecimal>
         * demandReasonWiseList;
         *
         * 1. Get List of agencywise collections group by demandid. Sort by arrears,tax and based on year. 2. Check whether full
         * amount we are collected for this demand. If yes check whether record in workflow, then close workflow. 3. If no, mean..
         * amount remaining is less than amount to be collected for advertisement. Here we need to collect arrears , tax,
         * encroachment fee in order. The percentage of penalty we need to get based on demand detail and penalty calculation
         * date. Pending amount > balance + penalty then use that demand detail. else for pending amount decide penalty to be
         * adjust + tax to be adjust.
         *
         * Check remaining amount is required to adjust with other demand detail. Add penalty to map by financial year and update
         * demand detail on final stage.
         */

        // group by arrears, last financial year and current year
        // group by demandid return values.

        for (final AgencyWiseCollectionDetail agencyDtl : agencyWiseCollection.getAgencyWiseCollectionDetails()) {
            // if (agencyDtl.getDemandDetail() != null ) {

            if (agencyDtl.getDemandDetail() != null
                    && !AdvertisementTaxConstants.DEMANDREASON_PENALTY.equalsIgnoreCase(agencyDtl.getDemandreason()
                            .getEgDemandReasonMaster().getReasonMaster())
                    && !additionalTaxes.isEmpty() && !additionalTaxes.containsValue(agencyDtl
                            .getDemandreason().getEgDemandReasonMaster().getCode())) {

                agencyDtl.getDemandDetail().setAmtCollected(
                        agencyDtl.getDemandDetail().getAmtCollected().add(agencyDtl.getAmount()));

                // TODO: IF PENALTY IS PART OF DEMAND REASON, THEN WE NEED TO HANDLE IN THIS LOOP. HERE AMOUNT COLLECTED WILL BE
                // MORE IN THAT CASE.
                persistCollectedReceipts(agencyDtl.getDemandDetail(), billRcptInfo.getReceiptNum(), totalAmount,
                        billRcptInfo.getReceiptDate(), agencyDtl.getAmount());
                agencyDtl.getDemand().addCollected(agencyDtl.getAmount());
            } else {
                final List<EgDemandDetails> penaltyDmtDtails = advertisementDemandService
                        .getDemandDetailByPassingDemandDemandReason(agencyDtl.getDemand(),
                                agencyDtl.getDemandreason());
                /*
                 * Check whether penalty reason already present in current demand.
                 */
                if (penaltyDmtDtails != null && penaltyDmtDtails.size() > 0) {

                    penaltyDmtDtails.get(0).setAmount(
                            penaltyDmtDtails.get(0).getAmount().add(agencyDtl.getAmount()));
                    penaltyDmtDtails.get(0).setAmtCollected(
                            penaltyDmtDtails.get(0).getAmtCollected().add(agencyDtl.getAmount()));
                    persistCollectedReceipts(penaltyDmtDtails.get(0), billRcptInfo.getReceiptNum(), totalAmount,
                            billRcptInfo.getReceiptDate(), agencyDtl.getAmount());
                } else {
                    /*
                     * Create new demand detail entry eg:for penalty
                     */
                    final EgDemandDetails demandDetail = advertisementDemandService.createDemandDetails(
                            agencyDtl.getAmount(), agencyDtl.getDemandreason(), agencyDtl.getAmount());
                    agencyDtl.getDemand().addEgDemandDetails(demandDetail);
                    agencyDtl.getDemand().addBaseDemand(agencyDtl.getAmount());
                    agencyDtl.getDemand().addCollected(agencyDtl.getAmount());
                    getCurrentSession().flush();
                    persistCollectedReceipts(demandDetail, billRcptInfo.getReceiptNum(), totalAmount,
                            billRcptInfo.getReceiptDate(), agencyDtl.getAmount());
                }
            }
            // }
            /**
             * If for new application, commissioner approved record and payment collection is pending. If user using agency wise
             * collection screen then we need to update workflow.
             */
            if (agencyDtl.getDemand() != null)
                updateWorkflowState(agencyDtl.getDemand());
        }
    }

    /**
     * @param billRcptInfo
     * @param totalAmount
     * @return
     */
    @Transactional
    private EgDemand generalDemandUpdationForAdvertisement(final BillReceiptInfo billRcptInfo, final BigDecimal totalAmount) {
        final EgDemand demand = getDemandByBillReferenceNumber(Long.valueOf(billRcptInfo.getBillReferenceNum()));
        final String indexNo = ((BillReceiptInfoImpl) billRcptInfo).getReceiptMisc().getReceiptHeader()
                .getConsumerCode();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("updateDemandDetails : Demand before proceeding : " + demand);
            LOGGER.debug("updateDemandDetails : collection back update started for property : " + indexNo
                    + " and receipt event is " + billRcptInfo.getEvent() + ". Total Receipt amount is." + totalAmount
                    + " with receipt no." + billRcptInfo.getReceiptNum());
        }
        if (billRcptInfo.getEvent().equals(EVENT_INSTRUMENT_BOUNCED))
            updateReceiptStatusWhenCancelled(billRcptInfo.getReceiptNum());
        else if (billRcptInfo.getEvent().equals(EVENT_RECEIPT_CREATED))
            updateDemandWithcollectdTaxDetails(demand, billRcptInfo, EVENT_RECEIPT_CREATED, totalAmount);
        else if (billRcptInfo.getEvent().equals(EVENT_RECEIPT_CANCELLED))
            updateDemandWithcollectdTaxDetails(demand, billRcptInfo, EVENT_RECEIPT_CANCELLED, totalAmount);
        // updateReceiptStatusWhenCancelled(billRcptInfo.getReceiptNum());
        return demand;
    }

    /**
     * @param demand
     * @param billReceiptInfo
     * @param eventType
     * @param totalAmount
     * @return
     */
    @Transactional
    private BigDecimal updateDemandWithcollectdTaxDetails(final EgDemand demand, final BillReceiptInfo billReceiptInfo,
            final String eventType, final BigDecimal totalAmount) {

        BigDecimal totalAmountCollected = BigDecimal.ZERO;
        List<String> taxTypeDescList = buildAdditionalTaxDetails();
        for (final ReceiptAccountInfo recAccInfo : billReceiptInfo.getAccountDetails()) {
            String demandMasterReasonDesc = null;
            String financialYearDesc = null;
            if (recAccInfo.getDescription() != null) {
                demandMasterReasonDesc = recAccInfo
                        .getDescription()
                        .substring(
                                0,
                                recAccInfo.getDescription().indexOf(
                                        AdvertisementTaxConstants.COLL_RECEIPTDETAIL_DESC_PREFIX))
                        .trim();
                if (!taxTypeDescList.isEmpty() && taxTypeDescList.contains(demandMasterReasonDesc)) {

                    financialYearDesc = advertisementDemandService.getCurrentInstallment().getDescription();

                } else {
                    financialYearDesc = recAccInfo.getDescription()
                            .substring(
                                    recAccInfo.getDescription().indexOf(AdvertisementTaxConstants.COLL_RECEIPTDETAIL_DESC_PREFIX)
                                            + AdvertisementTaxConstants.COLL_RECEIPTDETAIL_DESC_PREFIX.length())
                            .trim();
                }

                if (eventType.equals(EVENT_RECEIPT_CREATED))
                    totalAmountCollected = totalAmountCollected
                            .add(createOrUpdateDemandDetails(demandMasterReasonDesc, financialYearDesc,
                                    demand, billReceiptInfo, recAccInfo, totalAmount));
            }
        }
        LOGGER.info("Demand before updateDemandDetails() processing: " + demand.getAmtCollected() + demand);

        if (eventType.equals(EVENT_RECEIPT_CANCELLED)) {
            cancelBill(Long.valueOf(billReceiptInfo.getBillReferenceNum()));
            demand.setAmtCollected(demand.getAmtCollected().subtract(totalAmount));
            updateDmdDetForRcptCancel(demand, billReceiptInfo);
        }
        demand.setModifiedDate(new Date());
        return totalAmountCollected;
    }

    private List<String> buildAdditionalTaxDetails() {
        List<String> taxTypeDescList = new ArrayList<>();
        List<AdvertisementAdditionalTaxRate> additionalTaxRates = advertisementAdditinalTaxRateService
                .getAllActiveAdditinalTaxRates();
        for (AdvertisementAdditionalTaxRate advertisementAdditionalTaxRate : additionalTaxRates) {
            if (advertisementAdditionalTaxRate != null) {
                taxTypeDescList.add(advertisementAdditionalTaxRate.getTaxType());
            }
        }
        return taxTypeDescList;
    }

    /*
     * Iterate each agency detail and update collected amount. Assumption is full amount will be collected from collection system.
     ** @param totalAmount
     */
    @Transactional
    private void updateAgencyWiseCollectionOnCancel(final EgDemand demand, final BillReceiptInfo billRcptInfo,
            final AgencyWiseCollection agencyWiseCollection, final BigDecimal totalAmount) {

        for (final AgencyWiseCollectionDetail agencyDtl : agencyWiseCollection.getAgencyWiseCollectionDetails())
            if (agencyDtl.getDemandDetail() != null)
                updateCancelledDemandDetailAmount(agencyDtl.getDemandDetail(), agencyDtl.getDemand(), agencyDtl.getAmount());
            else {
                final List<EgDemandDetails> demandDetail = advertisementDemandService
                        .getDemandDetailByPassingDemandDemandReason(agencyDtl.getDemand(),
                                agencyDtl.getDemandreason());
                if (demandDetail != null && demandDetail.size() > 0)
                    updateCancelledDemandDetailAmount(demandDetail.get(0), demandDetail.get(0).getEgDemand(),
                            agencyDtl.getAmount());
            }
        cancelBill(Long.valueOf(billRcptInfo.getBillReferenceNum()));
        demand.setAmtCollected(demand.getAmtCollected().subtract(totalAmount));
        updateReceiptStatusWhenCancelled(billRcptInfo.getReceiptNum());

    }

    @Transactional
    private void updateCancelledDemandDetailAmount(final EgDemandDetails demandDtl, final EgDemand demand,
            final BigDecimal amount) {

        // RESET PENALTY AMOUNT AS WE CANCELLED RECEIPT.
        if (demandDtl != null && (AdvertisementTaxConstants.DEMANDREASON_PENALTY.equalsIgnoreCase(demandDtl.getEgDemandReason()
                .getEgDemandReasonMaster().getReasonMaster()) || !additionalTaxes.isEmpty() &&
                        additionalTaxes.containsValue(demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()))) {
            demand.setBaseDemand(demand.getBaseDemand().subtract(amount));
            demandDtl.setAmount(demandDtl.getAmount().subtract(amount));
        }

        if (demandDtl != null && demandDtl.getAmtCollected().compareTo(amount) < 0)
            throw new ApplicationRuntimeException(
                    "updateDmdDetForRcptCancel : Exception while updating cancel receipt, "
                            + "to be deducted amount " + amount
                            + " is greater than the collected amount " + demandDtl.getAmtCollected());

        demandDtl.setAmtCollected(demandDtl.getAmtCollected().subtract(amount));
        demand.setAmtCollected(demand.getAmtCollected().subtract(amount));
    }

    /**
     * @param demand
     * @param billRcptInfo
     */
    @Transactional
    private void updateDmdDetForRcptCancel(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
        LOGGER.debug("Entering method updateDmdDetForRcptCancel");
        String demandMasterReasonDesc = null;
        String financialYearDesc = null;
        List<String> taxTypeDescList = buildAdditionalTaxDetails();
        for (final ReceiptAccountInfo rcptAccInfo : billRcptInfo.getAccountDetails())
            if (rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1
                    && !rcptAccInfo.getIsRevenueAccount())
                if (rcptAccInfo.getDescription() != null) {
                    demandMasterReasonDesc = rcptAccInfo
                            .getDescription().substring(0, rcptAccInfo.getDescription().indexOf(
                                    AdvertisementTaxConstants.COLL_RECEIPTDETAIL_DESC_PREFIX))
                            .trim();
                    //If demand reason  of service tax and cess type, then current installment will be used default.
                    if (!taxTypeDescList.isEmpty() && taxTypeDescList.contains(demandMasterReasonDesc)) {
                        financialYearDesc = advertisementDemandService.getCurrentInstallment().getDescription();
                    } else {
                        financialYearDesc = rcptAccInfo.getDescription()
                                .substring(rcptAccInfo.getDescription()
                                        .indexOf(AdvertisementTaxConstants.COLL_RECEIPTDETAIL_DESC_PREFIX)
                                        + AdvertisementTaxConstants.COLL_RECEIPTDETAIL_DESC_PREFIX.length())
                                .trim();
                    }
                    
                    for (final EgDemandDetails demandDetail : demand.getEgDemandDetails())
                        if (demandMasterReasonDesc.equalsIgnoreCase(demandDetail.getEgDemandReason()
                                .getEgDemandReasonMaster().getReasonMaster()) && financialYearDesc != null &&
                                demandDetail.getEgDemandReason()
                                        .getEgInstallmentMaster().getFinYearRange().equalsIgnoreCase(financialYearDesc)) {
                            if (demandDetail.getAmtCollected().compareTo(rcptAccInfo.getCrAmount()) < 0)
                                throw new ApplicationRuntimeException(
                                        "updateDmdDetForRcptCancel : Exception while updating cancel receipt, "
                                                + "to be deducted amount " + rcptAccInfo.getCrAmount()
                                                + " is greater than the collected amount " + demandDetail.getAmtCollected()
                                                + " for demandDetail " + demandDetail);

                            if (AdvertisementTaxConstants.DEMANDREASON_PENALTY.equalsIgnoreCase(demandMasterReasonDesc)
                                    || !additionalTaxes.isEmpty() &&
                                            additionalTaxes.containsValue(
                                                    demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                                demandDetail.setAmount(demandDetail.getAmount().subtract(rcptAccInfo.getCrAmount()));

                            demandDetail
                                    .setAmtCollected(demandDetail.getAmtCollected().subtract(rcptAccInfo.getCrAmount()));

                        }
                }
        updateReceiptStatusWhenCancelled(billRcptInfo.getReceiptNum());
        LOGGER.debug("Exiting method updateDmdDetForRcptCancel");
    }

    @Transactional
    private void cancelBill(final Long billId) {
        if (billId != null) {
            final EgBill egBill = egBillDAO.findById(billId, false);
            egBill.setIs_Cancelled("Y");
        }
    }

    /**
     * @param demandMasterReasonDesc
     * @param financialYearDesc
     * @param demand
     * @param billReceiptInfo
     * @param recAccInfo
     * @param totalAmount
     * @return
     */
    @Transactional
    private BigDecimal createOrUpdateDemandDetails(final String demandMasterReasonDesc, final String financialYearDesc,
            final EgDemand demand,
            final BillReceiptInfo billReceiptInfo, final ReceiptAccountInfo recAccInfo, final BigDecimal totalAmount) {
        BigDecimal totalAmountCollected = BigDecimal.ZERO;

        Boolean demandReasonPartOfDemand = false;

        if (recAccInfo.getCrAmount() != null && recAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) > 0) {
            // updating the existing demand detail..
            for (final EgDemandDetails demandDetail : demand.getEgDemandDetails())
                if (demandDetail.getEgDemandReason() != null
                        && demandDetail.getEgDemandReason().getEgDemandReasonMaster() != null
                        && demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster().trim()
                                .equalsIgnoreCase(demandMasterReasonDesc)
                        && financialYearDesc != null
                        && financialYearDesc.equalsIgnoreCase(demandDetail.getEgDemandReason().getEgInstallmentMaster()
                                .getFinYearRange())) {
                    // && (demandDetail.getAmount().compareTo(BigDecimal.ZERO) >
                    // 0)) {

                    if (AdvertisementTaxConstants.DEMANDREASON_PENALTY.equalsIgnoreCase(demandMasterReasonDesc)
                            || !additionalTaxes.isEmpty() &&
                                    additionalTaxes
                                            .containsValue(demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                        demandDetail.setAmount(demandDetail.getAmount().add(recAccInfo.getCrAmount()));
                    demandDetail.addCollected(recAccInfo.getCrAmount());
                    /*
                     * Save bill detail and demand deatail relation in the intermediate table.
                     */
                    persistCollectedReceipts(demandDetail, billReceiptInfo.getReceiptNum(), totalAmount,
                            billReceiptInfo.getReceiptDate(), recAccInfo.getCrAmount());

                    demand.setAmtCollected(demand.getAmtCollected().add(recAccInfo.getCrAmount()));
                    totalAmountCollected = totalAmountCollected.add(recAccInfo.getCrAmount());
                    demandDetail.setModifiedDate(new Date());
                    demandReasonPartOfDemand = true;
                }
            if (!demandReasonPartOfDemand) {
                // Add new entry as part of demand. Eg: penalty is collected as
                // part of collection system.
                final EgDemandDetails demandDetail;
                if (additionalTaxes.containsKey(demandMasterReasonDesc))
                    demandDetail = advertisementDemandService.createDemandDetails(recAccInfo
                            .getCrAmount(), advertisementDemandService.getDemandReasonByCodeAndInstallment(
                                    additionalTaxes.get(demandMasterReasonDesc),
                                    advertisementDemandService.getInstallmentByDescription(financialYearDesc)),
                            recAccInfo
                                    .getCrAmount());
                else
                    demandDetail = advertisementDemandService.createDemandDetails(recAccInfo
                            .getCrAmount(), advertisementDemandService.getDemandReasonByCodeAndInstallment(
                                    demandMasterReasonDesc,
                                    advertisementDemandService.getInstallmentByDescription(financialYearDesc)),
                            recAccInfo
                                    .getCrAmount());
                demand.addEgDemandDetails(demandDetail);
                getCurrentSession().flush();
                persistCollectedReceipts(demandDetail, billReceiptInfo.getReceiptNum(), totalAmount,
                        billReceiptInfo.getReceiptDate(), recAccInfo.getCrAmount());

            }
            demand.setModifiedDate(new Date());
        }
        return totalAmountCollected;
    }

    /**
     * @param billId
     * @return
     */
    private EgDemand getDemandByBillReferenceNumber(final Long billId) {
        EgDemand egDemand = null;
        if (billId != null) {
            final EgBill egBill = egBillDAO.findById(billId, false);
            if (egBill != null)
                egDemand = egBill.getEgDemand();
        }
        return egDemand;
    }

    @Override
    protected Module module() {

        return null;
    }

    @Override
    public String constructAdditionalInfoForReceipt(final BillReceiptInfo billReceiptInfo) {

        return null;
    }

    @Transactional
    private void updateWorkflowState(final EgDemand demand) {

        if (demand != null) {
            final AdvertisementPermitDetail advertisementPermitDetail = advertisementService.getAdvertisementByDemand(demand)
                    .getActiveAdvertisementPermit();
            /**
             * If the current status of advertisement permit is approved, then only call next level workflow. Assumption: Payment
             * collection is pending in this stage.
             */
            if (advertisementPermitDetail != null
                    && advertisementPermitDetail.getState() != null
                    && advertisementPermitDetail.getStatus() != null
                    && advertisementPermitDetail.getStatus().getCode()
                            .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_APPROVED)) {

                advertisementPermitDetailService.updateStateTransition(advertisementPermitDetail, Long.valueOf(0),
                        AdvertisementTaxConstants.COLLECTION_REMARKS,
                        advertisementPermitDetail.getPreviousapplicationid() != null
                                ? AdvertisementTaxConstants.RENEWAL_ADDITIONAL_RULE
                                : AdvertisementTaxConstants.CREATE_ADDITIONAL_RULE,
                        AdvertisementTaxConstants.WF_DEMANDNOTICE_BUTTON);
                advertisementPermitDetailRepository.saveAndFlush(advertisementPermitDetail);
            } else
                advertisementPermitDetailUpdateIndexService.updateAdvertisementPermitDetailIndexes(advertisementPermitDetail);
        }
    }

    @Override
    public ReceiptAmountInfo receiptAmountBifurcation(final BillReceiptInfo billReceiptInfo) {
        return new ReceiptAmountInfo();
    }

}
