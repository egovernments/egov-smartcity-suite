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
package org.egov.ptis.service.collection;

import static org.egov.ptis.constants.PropertyTaxConstants.CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_FIRST_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_SECOND_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_VACANT_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_ADVANCE;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_LIBRARY_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_PENALTY_FINES;
import static org.egov.ptis.constants.PropertyTaxConstants.DMD_STATUS_CHEQUE_BOUNCED;
import static org.egov.ptis.constants.PropertyTaxConstants.FIRST_REBATETAX_PERC;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODEMAP_FOR_ARREARTAX;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODEMAP_FOR_CURRENTTAX;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODES_FOR_ARREARTAX;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODES_FOR_CURRENTTAX;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODE_FOR_TAXREBATE;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.SECOND_REBATETAX_PERC;
import static org.egov.ptis.constants.PropertyTaxConstants.STR_FOR_CASH;
import static org.egov.ptis.constants.PropertyTaxConstants.STR_FOR_CASH_ADJUSTMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.STR_FOR_SUBMISSION;
import static org.egov.ptis.constants.PropertyTaxConstants.STR_INSTRUMENTTYPE_CHEQUE;
import static org.egov.ptis.constants.PropertyTaxConstants.STR_INSTRUMENTTYPE_DD;
import static org.egov.ptis.constants.PropertyTaxConstants.STR_REALIZATION;
import static org.egov.ptis.constants.PropertyTaxConstants.STR_WITH_AMOUNT;
import static org.egov.ptis.constants.PropertyTaxConstants.SUPER_STRUCTURE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.integration.models.BillAccountDetails.PURPOSE;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.BillReceiptInfoImpl;
import org.egov.collection.integration.models.ReceiptAccountInfo;
import org.egov.collection.integration.models.ReceiptAmountInfo;
import org.egov.collection.integration.models.ReceiptInstrumentInfo;
import org.egov.collection.integration.services.CollectionIntegrationService;
import org.egov.commons.Installment;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.dao.EgDemandDetailsDao;
import org.egov.demand.integration.TaxCollection;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.notification.service.NotificationService;
import org.egov.infra.utils.MoneyUtils;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.client.bill.PTBillServiceImpl;
import org.egov.ptis.client.service.CollectionApportioner;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.service.property.RebateService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class is used to persist Collections .This is used for the integration of Collections and Bills and property tax.
 */
@Transactional
public class PropertyTaxCollection extends TaxCollection {

    private static final Logger LOGGER = Logger.getLogger(PropertyTaxCollection.class);
    private PersistenceService persistenceService;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private Installment currInstallment = null;

    @Autowired
    private ModuleService moduleDao;

    @Autowired
    private EgBillDao egBillDAO;

    @Autowired
    private DemandGenericDao demandGenericDAO;

    @Autowired
    private PersistenceService<Property, Long> propertyImplService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private FunctionHibernateDAO functionDAO;

    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsDAO;

    private PTBillServiceImpl ptBillServiceImpl;

    @Autowired
    PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @Autowired
    private CollectionIntegrationService collectionService;

    @Autowired
    private EgDemandDetailsDao demanddetailsDao;

    @Autowired
    private RebateService rebateService;

    @Override
    protected Module module() {
        return moduleDao.getModuleByName(PTMODULENAME);
    }

    @Override
    public void updateDemandDetails(final BillReceiptInfo billRcptInfo) throws ApplicationRuntimeException {
        totalAmount = billRcptInfo.getTotalAmount();
        currInstallment = propertyTaxCommonUtils.getCurrentInstallment();
        LOGGER.debug("updateDemandDetails : Updating Demand Details Started, billRcptInfo : " + billRcptInfo);
        try {
            final EgDemand demand = getCurrentDemand(Long.valueOf(billRcptInfo.getBillReferenceNum()));
            final String assessmentNo = ((BillReceiptInfoImpl) billRcptInfo).getReceiptMisc().getReceiptHeader()
                    .getConsumerCode();
            LOGGER.info("updateDemandDetails : Demand before proceeding : " + demand);
            LOGGER.info("updateDemandDetails : collection back update started for property : " + assessmentNo
                    + " and receipt event is " + billRcptInfo.getEvent() + ". Total Receipt amount is." + totalAmount
                    + " with receipt no." + billRcptInfo.getReceiptNum());

            if (billRcptInfo.getEvent().equals(EVENT_RECEIPT_CREATED)) {
                updateCollForRcptCreate(demand, billRcptInfo);
                activateDemand(demand);
                buildSMS(demand, billRcptInfo);

            } else if (billRcptInfo.getEvent().equals(EVENT_RECEIPT_CANCELLED))
                updateCollForRcptCancel(demand, billRcptInfo);
            else if (billRcptInfo.getEvent().equals(EVENT_INSTRUMENT_BOUNCED))
                updateCollForChequeBounce(demand, billRcptInfo);
            LOGGER.info("updateDemandDetails : Demand after processed : " + demand);
        } catch (final Exception e) {

            throw new ApplicationRuntimeException("Error occured during back update of DCB : " + e.getMessage(), e);
        }
        LOGGER.debug("updateDemandDetails : Updating Demand Details Finished...");
    }

    private void buildSMS(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
        final Property property = ((Ptdemand) demand).getEgptProperty();
        final User user = property.getBasicProperty().getPrimaryOwner();
        final String mobileNumber = user.getMobileNumber();
        final StringBuilder smsMsg = new StringBuilder(100);
        String instNumber = "";
        final List<String> instrumentType = new ArrayList<String>();

        for (final ReceiptInstrumentInfo instrumentInfo : billRcptInfo.getInstrumentDetails()) {
            instrumentType.add(instrumentInfo.getInstrumentType());
            instNumber = instrumentInfo.getInstrumentNumber();
        }
        if (instrumentType.contains("cheque"))
            smsMsg.append(STR_INSTRUMENTTYPE_CHEQUE).append(instNumber).append(STR_WITH_AMOUNT)
                    .append(billRcptInfo.getTotalAmount()).append(STR_FOR_SUBMISSION)
                    .append(((BillReceiptInfoImpl) billRcptInfo).getReceiptMisc().getReceiptHeader().getConsumerCode())
                    .append(STR_REALIZATION);
        else if (instrumentType.contains("dd"))
            smsMsg.append(STR_INSTRUMENTTYPE_DD).append(instNumber).append(STR_WITH_AMOUNT)
                    .append(billRcptInfo.getTotalAmount()).append(STR_FOR_SUBMISSION)
                    .append(((BillReceiptInfoImpl) billRcptInfo).getReceiptMisc().getReceiptHeader().getConsumerCode())
                    .append(STR_REALIZATION);
        else if (instrumentType.contains("cash"))
            smsMsg.append(STR_FOR_CASH).append(billRcptInfo.getTotalAmount()).append(STR_FOR_CASH_ADJUSTMENT)
                    .append(((BillReceiptInfoImpl) billRcptInfo).getReceiptMisc().getReceiptHeader().getConsumerCode());

        if (mobileNumber != null)
            notificationService.sendSMS(mobileNumber, smsMsg.toString());

    }

    /**
     * This method is invoked from Collections end when an event related to receipt in bill generation occurs.
     */
    @Override
    public void updateReceiptDetails(final Set<BillReceiptInfo> billReceipts) {
        LOGGER.debug("updateReceiptDetails : Updating Receipt Details Started, billReceipts : " + billReceipts);
        final Boolean status = false;
        if (billReceipts != null)
            super.updateReceiptDetails(billReceipts);
        LOGGER.debug("updateReceiptDetails : Updating Receipt Details Finished, status : " + status);
    }

    /**
     * Adds the collected amounts in the appropriate buckets.
     */
    private void updateCollForRcptCreate(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
        LOGGER.debug("updateCollForRcptCreate : Updating Collection Started For Demand : " + demand
                + " with BillReceiptInfo - " + billRcptInfo);
        LOGGER.info("updateCollForRcptCreate : Total amount collected : " + totalAmount);
        demand.addCollected(totalAmount);
        try {
            if (demand.getMinAmtPayable() != null && demand.getMinAmtPayable().compareTo(BigDecimal.ZERO) > 0)
                demand.setMinAmtPayable(BigDecimal.ZERO);
            updateDemandDetailForReceiptCreate(billRcptInfo.getAccountDetails(), demand, billRcptInfo);
        } catch (final Exception e) {

            throw new ApplicationRuntimeException(
                    "Error occured during back update of DCB : updateCollForRcptCreate() " + e.getMessage(), e);
        }
        LOGGER.debug("updateCollForRcptCreate : Updating Demand For Collection finished...");
    }

    /**
     * Deducts the collected amounts as per the amount of the cancelled receipt.
     */
    private void updateCollForRcptCancel(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
        LOGGER.debug("reconcileCollForRcptCancel : Updating Collection Started For Demand : " + demand
                + " with BillReceiptInfo - " + billRcptInfo);
        cancelBill(Long.valueOf(billRcptInfo.getBillReferenceNum()));

        if (demand.getAmtCollected() != null)
            demand.setAmtCollected(demand.getAmtCollected().subtract(billRcptInfo.getTotalAmount()));

        updateDmdDetForRcptCancel(demand, billRcptInfo);
        LOGGER.debug("reconcileCollForRcptCancel : Updating Collection finished For Demand : " + demand);
    }

    /**
     * Deducts the collected amounts as per the amount of the bounced cheque, and also imposes a cheque-bounce penalty.
     */
    private void updateCollForChequeBounce(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
        LOGGER.debug("reconcileCollForChequeBounce : Updating Collection Started For Demand : " + demand
                + " with BillReceiptInfo - " + billRcptInfo);
        final BigDecimal totalCollChqBounced = getTotalChequeAmt(billRcptInfo);
        final BigDecimal chqBouncePenalty = getChqBouncePenaltyAmt(totalCollChqBounced);
        cancelBill(Long.valueOf(billRcptInfo.getBillReferenceNum()));
        EgDemandDetails dmdDet = null;

        final EgDemandDetails penaltyDmdDet = ptBillServiceImpl.getDemandDetail(demand, currInstallment,
                DEMANDRSN_STR_CHQ_BOUNCE_PENALTY);
        if (penaltyDmdDet == null)
            dmdDet = ptBillServiceImpl.insertDemandDetails(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY, chqBouncePenalty,
                    currInstallment);
        else {
            BigDecimal existDmdDetAmt = penaltyDmdDet.getAmount();
            existDmdDetAmt = existDmdDetAmt == null || existDmdDetAmt.equals(BigDecimal.ZERO) ? existDmdDetAmt = BigDecimal.ZERO
                    : existDmdDetAmt;
            penaltyDmdDet.setAmount(existDmdDetAmt.add(chqBouncePenalty));
            dmdDet = penaltyDmdDet;
        }

        // setting this min amount into demand to check next payment should be
        // min of this amount with mode of payment cash or DD
        demand.setMinAmtPayable(totalCollChqBounced.add(chqBouncePenalty));
        demand.setAmtCollected(demand.getAmtCollected().subtract(billRcptInfo.getTotalAmount()));
        demand.setBaseDemand(demand.getBaseDemand().add(chqBouncePenalty));
        demand.setStatus(DMD_STATUS_CHEQUE_BOUNCED);
        demand.addEgDemandDetails(dmdDet);
        updateDmdDetForRcptCancel(demand, billRcptInfo);
        LOGGER.debug("reconcileCollForChequeBounce : Updating Collection finished For Demand : " + demand);
    }

    /**
     * Update the collection to respective account heads paid
     *
     * @param accountDetails
     * @param demand
     * @param billRcptInfo
     */
    @SuppressWarnings("unchecked")
    private void updateDemandDetailForReceiptCreate(final Set<ReceiptAccountInfo> accountDetails,
            final EgDemand demand, final BillReceiptInfo billRcptInfo) {
        LOGGER.debug("Entering method saveCollectionDetails");
        BigDecimal rebateAmount = BigDecimal.ZERO;
        for (final ReceiptAccountInfo accInfo : accountDetails)
            if (accInfo.getDescription() != null)
                if (accInfo.getDescription().contains("REBATE"))
                    rebateAmount = accInfo.getDrAmount();
        LOGGER.info("saveCollectionDetails : Start get demandDetailList");

        final List<EgDemandDetails> demandDetailList = persistenceService.findAllBy(
                "select dmdet FROM EgDemandDetails dmdet " + "left join fetch dmdet.egDemandReason dmdRsn "
                        + "left join fetch dmdRsn.egDemandReasonMaster dmdRsnMstr "
                        + "left join fetch dmdRsn.egInstallmentMaster installment WHERE dmdet.egDemand = ?",
                demand);

        LOGGER.info("saveCollectionDetails : End get demandDetailList");

        final Map<String, Map<String, EgDemandDetails>> installmentWiseDemandDetailsByReason = new HashMap<String, Map<String, EgDemandDetails>>();
        Map<String, EgDemandDetails> demandDetailByReason = new HashMap<String, EgDemandDetails>();

        EgDemandReason dmdRsn = null;
        String installmentDesc = null;
        Map<String, Installment> currInstallments = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
        for (final EgDemandDetails dmdDtls : demandDetailList)
            if (dmdDtls.getAmount().compareTo(BigDecimal.ZERO) > 0
                    || dmdDtls.getEgDemandReason().getEgDemandReasonMaster().getCode().equalsIgnoreCase(DEMANDRSN_CODE_ADVANCE)) {

                dmdRsn = dmdDtls.getEgDemandReason();
                installmentDesc = dmdRsn.getEgInstallmentMaster().getDescription();
                demandDetailByReason = new HashMap<String, EgDemandDetails>();

                if (installmentWiseDemandDetailsByReason.get(installmentDesc) == null) {
                    demandDetailByReason.put(dmdRsn.getEgDemandReasonMaster().getReasonMaster(), dmdDtls);
                    installmentWiseDemandDetailsByReason.put(installmentDesc, demandDetailByReason);
                } else
                    installmentWiseDemandDetailsByReason.get(installmentDesc).put(
                            dmdRsn.getEgDemandReasonMaster().getReasonMaster(), dmdDtls);
            } else
                LOGGER.info("saveCollectionDetails - demand detail amount is zero " + dmdDtls);

        LOGGER.info("saveCollectionDetails - installment demandDetails size = "
                + installmentWiseDemandDetailsByReason.size());

        EgDemandDetails demandDetail = null;

        for (final ReceiptAccountInfo rcptAccInfo : accountDetails)
            if (rcptAccInfo.getDescription() != null && !rcptAccInfo.getDescription().isEmpty())
                if (rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1) {
                    final String[] desc = rcptAccInfo.getDescription().split("-", 2);
                    final String reason = desc[0].trim();
                    final String instDesc = desc[1].trim();

                    if (reason.equalsIgnoreCase(DEMANDRSN_STR_ADVANCE))
                        demandDetail = installmentWiseDemandDetailsByReason
                                .get(currInstallments.get(CURRENTYEAR_SECOND_HALF).getDescription())
                                .get(reason);
                    else
                        demandDetail = installmentWiseDemandDetailsByReason.get(instDesc).get(reason);

                    if (rcptAccInfo.getGlCode().equalsIgnoreCase(PropertyTaxConstants.GLCODE_FOR_PENALTY)) {

                        if (demandDetail == null)
                            throw new ApplicationRuntimeException("Demand Details for reason " + reason
                                    + " and with installment " + instDesc + " is null ");
                        else
                            demandDetail.addCollected(rcptAccInfo.getCrAmount());

                    } else if (rcptAccInfo.getGlCode().equalsIgnoreCase(PropertyTaxConstants.GLCODE_FOR_ADVANCE)) {
                        if (demandDetail != null)
                            demandDetail.setAmtCollected(demandDetail.getAmtCollected().add(rcptAccInfo.getCrAmount()));
                        else {
                            demandDetail = insertAdvanceCollection(DEMANDRSN_CODE_ADVANCE, rcptAccInfo.getCrAmount(),
                                    currInstallments.get(CURRENTYEAR_SECOND_HALF));

                            demand.addEgDemandDetails(demandDetail);
                            persistenceService.getSession().flush();

                            if (installmentWiseDemandDetailsByReason
                                    .get(currInstallments.get(CURRENTYEAR_SECOND_HALF).getDescription()) == null) {
                                final Map<String, EgDemandDetails> reasonAndDemandDetail = new HashMap<String, EgDemandDetails>();
                                reasonAndDemandDetail.put(DEMANDRSN_STR_ADVANCE, demandDetail);
                                installmentWiseDemandDetailsByReason.put(
                                        currInstallments.get(CURRENTYEAR_SECOND_HALF).getDescription(),
                                        reasonAndDemandDetail);
                            } else
                                installmentWiseDemandDetailsByReason
                                        .get(currInstallments.get(CURRENTYEAR_SECOND_HALF).getDescription()).put(
                                                DEMANDRSN_STR_ADVANCE, demandDetail);
                        }
                    } else {
                        demandDetail.addCollectedWithOnePaisaTolerance(rcptAccInfo.getCrAmount());
                        if (rebateAmount.compareTo(BigDecimal.ZERO) > 0
                                && instDesc.equals(currInstallments.get(CURRENTYEAR_FIRST_HALF).getDescription())
                                && (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                        .equals(DEMANDRSN_CODE_GENERAL_TAX)
                                        || demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                                .equals(DEMANDRSN_CODE_VACANT_TAX))) {
                            demandDetail.setAmtRebate(rebateAmount);
                            rebateAmount = BigDecimal.ZERO;
                        }
                    }

                    persistCollectedReceipts(demandDetail, billRcptInfo.getReceiptNum(), totalAmount,
                            billRcptInfo.getReceiptDate(), demandDetail.getAmtCollected());
                    LOGGER.info("Persisted demand and receipt details for tax : " + reason + " installment : "
                            + instDesc + " with receipt No : " + billRcptInfo.getReceiptNum() + " for Rs. "
                            + rcptAccInfo.getCrAmount());
                }
        if (rebateAmount.compareTo(BigDecimal.ZERO) > 0) {
            demandDetail = installmentWiseDemandDetailsByReason.get(currInstallments.get(CURRENTYEAR_FIRST_HALF).getDescription())
                    .get(PropertyTaxConstants.DEMANDRSN_STR_GENERAL_TAX);
            if (demandDetail == null)
                demandDetail = installmentWiseDemandDetailsByReason
                        .get(currInstallments.get(CURRENTYEAR_FIRST_HALF).getDescription())
                        .get(PropertyTaxConstants.DEMANDRSN_STR_VACANT_TAX);
            demandDetail.setAmtRebate(rebateAmount);
            demanddetailsDao.update(demandDetail);
            LOGGER.info("Persisted demand and receipt details for 2nd half tax : "
                    + demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                    + rebateAmount);
        }

        LOGGER.debug("Exiting method saveCollectionDetails");
    }

    /**
     * Reconciles the collection for respective account heads thats been paid with given cancel receipt
     *
     * @param demand
     * @param billRcptInfo
     */
    private void updateDmdDetForRcptCancel(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
        LOGGER.debug("Entering method updateDmdDetForRcptCancel");
        ReceiptAccountInfo rebateRcptAccInfo = null;

        final Map<String, ReceiptAccountInfo> rebateReceiptAccInfoByInstallment = getRebteReceiptAccountInfosByInstallment(
                billRcptInfo);

        for (final ReceiptAccountInfo rcptAccInfo : billRcptInfo.getAccountDetails())
            if (rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1
                    && !rcptAccInfo.getIsRevenueAccount()) {
                final String[] desc = rcptAccInfo.getDescription().split("-", 2);
                final String reason = desc[0].trim();
                final String installment = desc[1].trim();
                EgDemandReasonMaster demandReasonMaster = null;

                rebateRcptAccInfo = rebateReceiptAccInfoByInstallment.get(installment);

                for (final EgDemandDetails demandDetail : demand.getEgDemandDetails()) {

                    demandReasonMaster = demandDetail.getEgDemandReason().getEgDemandReasonMaster();

                    if (reason.equalsIgnoreCase(demandReasonMaster.getReasonMaster()))
                        if (reason.equalsIgnoreCase(DEMANDRSN_CODE_ADVANCE)
                                || installment.equals(demandDetail.getEgDemandReason().getEgInstallmentMaster()
                                        .getDescription())) {

                            if (rebateRcptAccInfo != null)
                                if (demandDetail.getAmtRebate().compareTo(BigDecimal.ZERO) > 0
                                        && (demandReasonMaster.getCode().equals(DEMANDRSN_CODE_GENERAL_TAX) || demandReasonMaster
                                                .getCode().equalsIgnoreCase(DEMANDRSN_CODE_ADVANCE)))
                                    demandDetail.setAmtRebate(demandDetail.getAmtRebate().subtract(
                                            rebateRcptAccInfo.getDrAmount()));

                            if (demandDetail.getAmtCollected().compareTo(rcptAccInfo.getCrAmount()) < 0)
                                throw new ApplicationRuntimeException(
                                        "updateDmdDetForRcptCancel : Exception while updating cancel receipt, "
                                                + "to be deducted amount " + rcptAccInfo.getCrAmount()
                                                + " is greater than the collected amount "
                                                + demandDetail.getAmtCollected() + " for demandDetail " + demandDetail);

                            demandDetail.setAmtCollected(demandDetail.getAmtCollected().subtract(
                                    rcptAccInfo.getCrAmount()));

                            LOGGER.info("Deducted Collected amount Rs." + rcptAccInfo.getCrAmount() + " for tax : "
                                    + reason + " and installment : " + installment);
                        }
                }
            }
        updateReceiptStatusWhenCancelled(billRcptInfo.getReceiptNum());
        LOGGER.debug("Exiting method updateDmdDetForRcptCancel");
    }

    /**
     * Returns a map of Installment description and ReceiptAccountInfo
     *
     * @param billRcptInfo
     * @return
     */
    private Map<String, ReceiptAccountInfo> getRebteReceiptAccountInfosByInstallment(final BillReceiptInfo billRcptInfo) {
        final Map<String, ReceiptAccountInfo> rebateReceiptAccInfoByInstallment = new HashMap<String, ReceiptAccountInfo>();

        for (final ReceiptAccountInfo rcptAccInfo : billRcptInfo.getAccountDetails())
            if (rcptAccInfo.getGlCode().equalsIgnoreCase(GLCODE_FOR_TAXREBATE)
                    || rcptAccInfo.getGlCode().equalsIgnoreCase(PropertyTaxConstants.GLCODE_FOR_ADVANCE_REBATE))
                rebateReceiptAccInfoByInstallment
                        .put(rcptAccInfo.getDescription().split("-", 2)[1].trim(), rcptAccInfo);

        return rebateReceiptAccInfoByInstallment;
    }

    @Override
    public void apportionCollection(final String billRefNo, final BigDecimal amtPaid,
            final List<ReceiptDetail> receiptDetails) {
        boolean isEligibleForCurrentRebate = false;
        final boolean isEligibleForAdvanceRebate = false;

        if (rebateService.isEarlyPayRebateActive(receiptDetails.get(0).getReceiptHeader() != null
                ? receiptDetails.get(0).getReceiptHeader().getReceiptDate() : new Date()))
            isEligibleForCurrentRebate = true;

        final CollectionApportioner apportioner = new CollectionApportioner(isEligibleForCurrentRebate,
                isEligibleForAdvanceRebate, BigDecimal.ZERO);
        final Map<String, BigDecimal> instDemand = getInstDemand(receiptDetails);
        apportioner.apportion(amtPaid, receiptDetails, instDemand);
    }

    private EgDemand cancelBill(final Long billId) {
        final EgDemand egDemand = null;
        if (billId != null) {
            final EgBill egBill = egBillDAO.findById(billId, false);
            egBill.setIs_Cancelled("Y");
        }
        return egDemand;
    }

    /**
     * Calculates Early Payment Rebate for given Tax Amount
     *
     * @param rebateApplTaxAmt for which Rebate has to be calculated
     * @return rebate amount.
     */
    public BigDecimal calcEarlyPayRebate(final BigDecimal instTaxAmount, final BigDecimal rebateApplTaxAmt,
            final BigDecimal collection) {
        BigDecimal rebate = BigDecimal.ZERO;
        final Date today = new Date();
        final Calendar firstRebateDate = Calendar.getInstance();
        final BigDecimal halfYearTax = instTaxAmount.divide(new BigDecimal(2));
        LOGGER.debug("calcEarlyPayRebate instTaxAmount " + instTaxAmount + " halfYearTax " + halfYearTax
                + " rebateApplTaxAmt " + rebateApplTaxAmt + " collection " + collection);
        final int currMonth = firstRebateDate.get(Calendar.MONTH);
        if (currMonth <= 2)
            firstRebateDate.set(Calendar.YEAR, firstRebateDate.get(Calendar.YEAR) - 1);
        firstRebateDate.set(Calendar.DAY_OF_MONTH, 31);
        firstRebateDate.set(Calendar.MONTH, Calendar.MAY);
        firstRebateDate.set(Calendar.HOUR_OF_DAY, 23);
        firstRebateDate.set(Calendar.MINUTE, 59);
        firstRebateDate.set(Calendar.SECOND, 59);

        final Calendar secondRebateDate = Calendar.getInstance();
        if (currMonth <= 2)
            secondRebateDate.set(Calendar.YEAR, secondRebateDate.get(Calendar.YEAR) - 1);
        secondRebateDate.set(Calendar.DAY_OF_MONTH, 30);
        secondRebateDate.set(Calendar.MONTH, Calendar.NOVEMBER);
        secondRebateDate.set(Calendar.HOUR_OF_DAY, 23);
        secondRebateDate.set(Calendar.MINUTE, 59);
        secondRebateDate.set(Calendar.SECOND, 59);

        if (today.before(firstRebateDate.getTime()) || today.equals(firstRebateDate.getTime())) {
            if (collection.compareTo(BigDecimal.ZERO) == 1) {
                if (collection.compareTo(halfYearTax) <= 0)
                    rebate = MoneyUtils.roundOff(rebateApplTaxAmt.multiply(SECOND_REBATETAX_PERC).divide(
                            BigDecimal.valueOf(100)));
                else
                    rebate = BigDecimal.ZERO;
            } else
                rebate = MoneyUtils.roundOff(rebateApplTaxAmt.multiply(FIRST_REBATETAX_PERC).divide(
                        BigDecimal.valueOf(100)));
        } else if (today.before(secondRebateDate.getTime()) || today.equals(secondRebateDate.getTime()))
            if (collection.compareTo(halfYearTax) <= 0)
                rebate = MoneyUtils.roundOff(rebateApplTaxAmt.multiply(SECOND_REBATETAX_PERC).divide(
                        BigDecimal.valueOf(100)));
            else
                rebate = BigDecimal.ZERO;
        LOGGER.debug("calcEarlyPayRebate rebate " + rebate);
        return rebate;
    }

    /**
     * Gives the tax amount of Account head for which Rebate applicable
     *
     * @param List of <code>ReceiptDetail</code>
     * @return rebate applicable tax amount.
     */
    public BigDecimal getRebateApplAmount(final List<ReceiptDetail> receiptDetails) {
        BigDecimal taxAmount = BigDecimal.ZERO;
        for (final ReceiptDetail rd : receiptDetails)
            if (rd.getAccounthead().getGlcode().equals(GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_GENERAL_TAX))) {
                /*
                 * getting rebate amount from getCramountToBePaid() because before receipt created CrAmount is Zero and it will
                 * updated as part of receipt creation.
                 */
                taxAmount = rd.getCramountToBePaid();
                break;
            }
        return taxAmount;
    }

    public Map<String, BigDecimal> getInstDemand(final List<ReceiptDetail> receiptDetails) {
        final Map<String, BigDecimal> retMap = new HashMap<String, BigDecimal>();
        String installment = "";
        String[] desc;

        for (final ReceiptDetail rd : receiptDetails) {
            final String glCode = rd.getAccounthead().getGlcode();
            installment = "";
            desc = rd.getDescription().split("-", 2);
            installment = desc[1].trim();

            if (!glCode.equalsIgnoreCase(GLCODE_FOR_TAXREBATE)
                    && (GLCODEMAP_FOR_ARREARTAX.containsValue(glCode) || GLCODEMAP_FOR_CURRENTTAX.containsValue(glCode)))
                if (retMap.get(installment) == null)
                    retMap.put(installment, rd.getCramountToBePaid());
                else
                    retMap.put(installment, retMap.get(installment).add(rd.getCramountToBePaid()));
            if (GLCODES_FOR_CURRENTTAX.contains(glCode) || GLCODES_FOR_ARREARTAX.contains(glCode))
                prepareTaxMap(retMap, installment, rd, "FULLTAX");
            else if (PropertyTaxConstants.GLCODE_FOR_ADVANCE.equalsIgnoreCase(glCode))
                prepareTaxMap(retMap, installment, rd, "ADVANCE");
        }
        return retMap;
    }

    /**
     * @param retMap
     * @param installment
     * @param rd
     */
    private void prepareTaxMap(final Map<String, BigDecimal> retMap, final String installment, final ReceiptDetail rd,
            final String type) {
        if (retMap.get(installment + type) == null)
            retMap.put(installment + type, rd.getCramountToBePaid());
        else
            retMap.put(installment + type, retMap.get(installment + type).add(rd.getCramountToBePaid()));
    }

    /**
     * Method used to calculate the Total Cheque amount from he BillreceiptInfo object which is received from Collections Module.
     *
     * @param billRcptInfo
     * @return Total Cheque amount
     * @exception ApplicationRuntimeException
     */

    @Override
    public BigDecimal getTotalChequeAmt(final BillReceiptInfo billRcptInfo) {
        BigDecimal totalCollAmt = BigDecimal.ZERO;
        try {
            if (billRcptInfo != null)
                for (final ReceiptInstrumentInfo rctInst : billRcptInfo.getBouncedInstruments())
                    if (rctInst.getInstrumentAmount() != null)
                        totalCollAmt = totalCollAmt.add(rctInst.getInstrumentAmount());
        } catch (final ApplicationRuntimeException e) {
            throw new ApplicationRuntimeException("Exception in calculate Total Collected Amt" + e);
        }

        return totalCollAmt;
    }

    /**
     * Gives the Cheque bounce penalty charges for given cheque amount
     *
     * @param totalChqAmount
     * @return {@link BigDecimal}
     */
    public BigDecimal getChqBouncePenaltyAmt(final BigDecimal totalChqAmount) {
        return CHQ_BOUNCE_PENALTY;
    }

    /**
     * Method used to create new EgDemandDetail Object depending upon the EgDemandReason , Collected amount and Demand
     * amount(which are compulsory),Other wise returns Empty EgDemandDetails Object.
     *
     * @param egDemandReason
     * @param amtCollected
     * @param dmdAmount
     * @return New EgDemandDetails Object
     */

    public EgDemandDetails createDemandDetails(final EgDemandReason egDemandReason, final BigDecimal amtCollected,
            final BigDecimal dmdAmount) {
        return EgDemandDetails.fromReasonAndAmounts(dmdAmount, egDemandReason, amtCollected);
    }

    /**
     * Gives the Current EgDemand for billId
     *
     * @param upicNo
     * @return EgDemand
     */
    @SuppressWarnings("unchecked")
    public EgDemand getCurrentDemand(final Long billId) {
        LOGGER.debug("Entered into getCurrentDemand");

        final EgBill egBill = egBillDAO.findById(billId, false);

        final String query = "SELECT ptd FROM Ptdemand ptd " + "WHERE ptd.egInstallmentMaster = ? "
                + "AND ptd.egptProperty.basicProperty.upicNo = ? "
                + "AND (ptd.egptProperty.status = 'I' OR ptd.egptProperty.status = 'A') "
                + "AND ptd.egptProperty.basicProperty.active = true";

        final EgDemand egDemand = (EgDemand) persistenceService.find(query, currInstallment, egBill.getConsumerId());

        LOGGER.debug("Exiting from getCurrentDemand");
        return egDemand;
    }

    /**
     * Method used to insert advance collection in EgDemandDetail table.
     *
     * @see createDemandDetails() -- EgDemand Details are created
     * @return New EgDemandDetails Object
     */
    public EgDemandDetails insertAdvanceCollection(final String demandReason, final BigDecimal advanceCollectionAmount,
            final Installment installment) {
        EgDemandDetails demandDetail = null;

        if (advanceCollectionAmount != null && advanceCollectionAmount.compareTo(BigDecimal.ZERO) > 0) {

            final EgDemandReasonMaster egDemandReasonMaster = demandGenericDAO.getDemandReasonMasterByCode(
                    demandReason, module());

            if (egDemandReasonMaster == null)
                throw new ApplicationRuntimeException(
                        " Advance Demand reason Master is null in method  insertAdvanceCollection");

            final EgDemandReason egDemandReason = demandGenericDAO.getDmdReasonByDmdReasonMsterInstallAndMod(
                    egDemandReasonMaster, installment, module());

            if (egDemandReason == null)
                throw new ApplicationRuntimeException(
                        " Advance Demand reason is null in method  insertAdvanceCollection ");

            demandDetail = createDemandDetails(egDemandReason, advanceCollectionAmount, BigDecimal.ZERO);
        }
        return demandDetail;
    }

    // Activating the demand on payment
    @Transactional
    private void activateDemand(final EgDemand demand) {
        final Property property = ((Ptdemand) demand).getEgptProperty();
        if (property.getStatus().equals(PropertyTaxConstants.STATUS_DEMAND_INACTIVE)) {
            property.setStatus(PropertyTaxConstants.STATUS_ISACTIVE);
            propertyImplService.persist(property);
        }
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public List<ReceiptDetail> reconstructReceiptDetail(final String billReferenceNumber,
            final BigDecimal actualAmountPaid, final List<ReceiptDetail> receiptDetailList) {
        final Long billID = Long.valueOf(billReferenceNumber);
        final List<EgBillDetails> billDetails = new ArrayList<EgBillDetails>(0);
        final EgBill bill = ptBillServiceImpl.updateBillWithLatest(billID);
        LOGGER.debug("Reconstruct consumer code :" + bill.getConsumerId() + ", with bill reference number: "
                + billReferenceNumber + ", for Amount Paid :" + actualAmountPaid);
        boolean isEligibleForCurrentRebate = false;
        final boolean isEligibleForAdvanceRebate = false;
        if (rebateService.isEarlyPayRebateActive(bill.getCreateDate() != null ? bill.getCreateDate() : new Date()))
            isEligibleForCurrentRebate = true;

        final CollectionApportioner apportioner = new CollectionApportioner(isEligibleForCurrentRebate,
                isEligibleForAdvanceRebate, actualAmountPaid);
        billDetails.addAll(bill.getEgBillDetails());
        return apportioner.reConstruct(actualAmountPaid, billDetails, functionDAO, chartOfAccountsDAO);
    }

    @Override
    public String constructAdditionalInfoForReceipt(final BillReceiptInfo billReceiptInfo) {
        final EgBill egBill = egBillDAO.findById(Long.valueOf(billReceiptInfo.getBillReferenceNum()), false);
        return (egBill != null && egBill.getDescription().contains(SUPER_STRUCTURE)) ? SUPER_STRUCTURE : null;
    }

    @Override
    public ReceiptAmountInfo receiptAmountBifurcation(final BillReceiptInfo billReceiptInfo) {
        final ReceiptAmountInfo receiptAmountInfo = new ReceiptAmountInfo();
        BigDecimal currentInstallmentAmount = BigDecimal.ZERO;
        BigDecimal arrearAmount = BigDecimal.ZERO;
        BigDecimal latePaymentCharges = BigDecimal.ZERO;
        BigDecimal arrearLibCess = BigDecimal.ZERO;
        BigDecimal currLibCess = BigDecimal.ZERO;
        BigDecimal rebateAmount = BigDecimal.ZERO;
        final EgBill egBill = egBillDAO.findById(Long.valueOf(billReceiptInfo.getBillReferenceNum()), false);
        final List<EgBillDetails> billDetails = new ArrayList<EgBillDetails>(egBill.getEgBillDetails());
        final List<ReceiptDetail> reciptDetailList = collectionService.getReceiptDetailListByReceiptNumber(billReceiptInfo
                .getReceiptNum());

        for (final ReceiptAccountInfo rcptAccInfo : billReceiptInfo.getAccountDetails())
            if (rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1) {
                final String[] desc = rcptAccInfo.getDescription().split("-", 2);
                final String reason = desc[0];
                if ((rcptAccInfo.getPurpose().equals(PURPOSE.ARREAR_LATEPAYMENT_CHARGES.toString())
                        || rcptAccInfo.getPurpose().equals(PURPOSE.CURRENT_LATEPAYMENT_CHARGES.toString()))
                        && reason.equals(DEMANDRSN_STR_PENALTY_FINES))
                    latePaymentCharges = latePaymentCharges.add(rcptAccInfo.getCrAmount());
                else if (rcptAccInfo.getPurpose().equals(PURPOSE.CURRENT_AMOUNT.toString())
                        && reason.equals(DEMANDRSN_STR_LIBRARY_CESS))
                    currLibCess = currLibCess.add(rcptAccInfo.getCrAmount());
                else if (rcptAccInfo.getPurpose().equals(PURPOSE.ARREAR_AMOUNT.toString())
                        && reason.equals(DEMANDRSN_STR_LIBRARY_CESS))
                    arrearLibCess = arrearLibCess.add(rcptAccInfo.getCrAmount());
                else if (rcptAccInfo.getPurpose().equals(PURPOSE.ARREAR_AMOUNT.toString()))
                    arrearAmount = arrearAmount.add(rcptAccInfo.getCrAmount());
                else
                    currentInstallmentAmount = currentInstallmentAmount.add(rcptAccInfo.getCrAmount());
            } else if (rcptAccInfo.getDrAmount() != null && rcptAccInfo.getDrAmount().compareTo(BigDecimal.ZERO) == 1)
                if (rcptAccInfo.getPurpose().equals(PURPOSE.REBATE.toString()))
                    rebateAmount = rebateAmount.add(rcptAccInfo.getDrAmount());

        for (final EgBillDetails billDet : egBill.getEgBillDetails()) {
            final String[] desc = billDet.getDescription().split("-", 2);
            if (billDet.getOrderNo() == 1) {
                receiptAmountInfo.setInstallmentFrom(desc[1]);
                if (billDetails.size() == 1) {
                    receiptAmountInfo.setInstallmentTo(desc[1]);
                    break;
                }
            }
            if (billDetails.size() > 1)
                if (billDet.getCrAmount().compareTo(BigDecimal.ZERO) == 1
                        && reciptDetailList.get(0).getOrdernumber().equals(Long.valueOf(billDet.getOrderNo())))
                    receiptAmountInfo.setInstallmentTo(desc[1]);
        }
        String revenueWard = (String) persistenceService.find(
                "select bp.propertyID.ward.name from BasicPropertyImpl bp where bp.upicNo = ?",
                reciptDetailList.get(0).getReceiptHeader().getConsumerCode());

        String courtCaseQuery = "select (case when exists (select assessmentno from egpt_courtcases where assessmentno=:assessmentNo) then 1 else 0 end)";
        final Query query = persistenceService.getSession().createSQLQuery(courtCaseQuery);
        query.setString("assessmentNo", reciptDetailList.get(0).getReceiptHeader().getConsumerCode());
        int courtcase = (int) query.uniqueResult();

        receiptAmountInfo.setCurrentInstallmentAmount(currentInstallmentAmount);
        receiptAmountInfo.setLatePaymentCharges(latePaymentCharges);
        receiptAmountInfo.setArrearsAmount(arrearAmount);
        receiptAmountInfo.setCurrentCess(currLibCess);
        receiptAmountInfo.setArrearCess(arrearLibCess);
        receiptAmountInfo.setReductionAmount(rebateAmount);
        receiptAmountInfo.setRevenueWard(revenueWard);
        receiptAmountInfo.setConflict(courtcase);
        return receiptAmountInfo;
    }

    public void setPtBillServiceImpl(PTBillServiceImpl ptBillServiceImpl) {
        this.ptBillServiceImpl = ptBillServiceImpl;
    }

}
