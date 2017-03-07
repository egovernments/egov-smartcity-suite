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

package org.egov.tl.service.integration;

import static java.math.BigDecimal.ZERO;
import static org.egov.tl.utils.Constants.APPLICATION_STATUS_DIGUPDATE_CODE;
import static org.egov.tl.utils.Constants.CHQ_BOUNCE_PENALTY;
import static org.egov.tl.utils.Constants.DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY;
import static org.egov.tl.utils.Constants.DEMANDRSN_STR_CHQ_BOUNCE_PENALTY;
import static org.egov.tl.utils.Constants.DMD_STATUS_CHEQUE_BOUNCED;
import static org.egov.tl.utils.Constants.PENALTY_DMD_REASON_CODE;
import static org.egov.tl.utils.Constants.TRADELICENSE;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.egov.InvalidAccountHeadException;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.integration.models.BillAccountDetails;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.BillReceiptInfoImpl;
import org.egov.collection.integration.models.ReceiptAccountInfo;
import org.egov.collection.integration.models.ReceiptAmountInfo;
import org.egov.collection.integration.models.ReceiptInstrumentInfo;
import org.egov.collection.integration.services.BillingIntegrationService;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.dao.EgBillDetailsDao;
import org.egov.demand.dao.EgBillReceiptDao;
import org.egov.demand.dao.EgdmCollectedReceiptDao;
import org.egov.demand.interfaces.BillServiceInterface;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.BillReceipt;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.demand.model.EgReasonCategory;
import org.egov.demand.model.EgdmCollectedReceipt;
import org.egov.demand.utils.DemandConstants;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseDemand;
import org.egov.tl.service.PenaltyRatesService;
import org.egov.tl.service.TradeLicenseSmsAndEmailService;
import org.egov.tl.service.es.LicenseApplicationIndexService;
import org.egov.tl.utils.Constants;
import org.egov.tl.utils.LicenseUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LicenseBillService extends BillServiceInterface implements BillingIntegrationService {
    public static final String TL_FUNCTION_CODE = "1500";
    private static final Logger LOG = LoggerFactory.getLogger(LicenseBillService.class);
    protected License license;
    @Autowired
    private EgBillDetailsDao egBillDetailsDao;

    @Autowired
    @Qualifier("tradeLicenseWorkflowService")
    private SimpleWorkflowService tradeLicenseWorkflowService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private EgBillReceiptDao egBillReceiptDao;

    @Autowired
    private LicenseApplicationIndexService licenseApplicationIndexService;

    @Autowired
    private TradeLicenseSmsAndEmailService tradeLicenseSmsAndEmailService;

    @Autowired
    private EgBillDao egBillDao;

    @Autowired
    private DemandGenericDao demandGenericDao;

    @Autowired
    private EgdmCollectedReceiptDao egdmCollectedReceiptDao;

    @Autowired
    private InstallmentDao installmentDao;

    @Autowired
    @Qualifier("entityQueryService")
    private PersistenceService entityQueryService;

    @Autowired
    private LicenseUtils licenseUtils;

    @Autowired
    private PenaltyRatesService penaltyRatesService;

    public void setLicense(final License license) {
        this.license = license;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<EgBillDetails> getBilldetails(final Billable billObj) {
        final List<EgBillDetails> billDetails = new ArrayList<>();
        final LicenseBill billable = (LicenseBill) billObj;
        final EgDemand demand = billObj.getCurrentDemand();
        final Date currentDate = new Date();
        final Map installmentWise = new HashMap<Installment, List<EgDemandDetails>>();
        final Set<Installment> sortedInstallmentSet = new TreeSet<>();
        Module module = license.getTradeName() != null && license.getTradeName().getLicenseType() != null
                ? license.getTradeName().getLicenseType().getModule() : null;
        if (module == null)
            module = moduleService.getModuleByName(Constants.TRADELICENSE_MODULENAME);
        getCurrentInstallment(module);
        final List<EgDemandDetails> orderedDetailsList = new ArrayList<>();
        Map<Installment, BigDecimal> installmentPenalty = new HashMap<>();
        Map<Installment, EgDemandDetails> installmentWisePenaltyDemandDetail;
        if (license.isNewApplication())
            installmentPenalty = billable.getCalculatedPenalty(license.getCommencementDate(), new Date(), demand);
        else if (license.isReNewApplication())
            installmentPenalty = billable.getCalculatedPenalty(null, new Date(), demand);
        installmentWisePenaltyDemandDetail = getInstallmentWisePenaltyDemandDetails(license.getCurrentDemand());
        for (final Map.Entry<Installment, BigDecimal> penalty : installmentPenalty.entrySet()) {
            EgDemandDetails penaltyDemandDetail;
            if (penalty.getValue().signum() > 0) {
                penaltyDemandDetail = installmentWisePenaltyDemandDetail.get(penalty.getKey());
                if (penaltyDemandDetail != null)
                    penaltyDemandDetail.setAmount(penalty.getValue().setScale(0, RoundingMode.HALF_UP));
                else {
                    penaltyDemandDetail = insertPenaltyAndBillDetails(penalty.getValue().setScale(0, RoundingMode.HALF_UP),
                            penalty.getKey());
                    if (penaltyDemandDetail != null)
                        demand.getEgDemandDetails().add(penaltyDemandDetail);
                }
            }
        }
        license.getLicenseDemand().recalculateBaseDemand();
        for (final EgDemandDetails demandDetail : demand.getEgDemandDetails()) {
            final Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();
            if (installmentWise.get(installment) == null) {
                final List<EgDemandDetails> detailsList = new ArrayList<>();
                detailsList.add(demandDetail);
                installmentWise.put(demandDetail.getEgDemandReason().getEgInstallmentMaster(), detailsList);
                sortedInstallmentSet.add(installment);
            } else
                ((List<EgDemandDetails>) installmentWise.get(demandDetail.getEgDemandReason().getEgInstallmentMaster()))
                        .add(demandDetail);
        }
        for (final Installment i : sortedInstallmentSet) {
            final List<EgDemandDetails> installmentWiseDetails = (List<EgDemandDetails>) installmentWise.get(i);

            installmentWiseDetails.sort(Comparator
                    .comparing(demandDetails -> demandDetails.getEgDemandReason().getEgDemandReasonMaster().getOrderId()));
            orderedDetailsList.addAll(installmentWiseDetails);
        }

        int i = 1;
        for (final EgDemandDetails demandDetail : orderedDetailsList) {

            final EgDemandReason reason = demandDetail.getEgDemandReason();
            final Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();
            if ("N".equalsIgnoreCase(demandDetail.getEgDemandReason().getEgDemandReasonMaster().getIsDebit())
                    && demandDetail.getAmount().subtract(demandDetail.getAmtRebate())
                    .compareTo(demandDetail.getAmtCollected()) != 0) {
                final EgBillDetails billdetail = new EgBillDetails();
                final EgBillDetails billdetailRebate = new EgBillDetails();
                if (demandDetail.getAmtRebate() != null && demandDetail.getAmtRebate().compareTo(BigDecimal.ZERO) != 0) {
                    final EgReasonCategory reasonCategory = demandGenericDao
                            .getReasonCategoryByCode(Constants.DEMANDRSN_REBATE);
                    final List<EgDemandReasonMaster> demandReasonMasterByCategory = demandGenericDao
                            .getDemandReasonMasterByCategoryAndModule(reasonCategory, module);
                    for (final EgDemandReasonMaster demandReasonMaster : demandReasonMasterByCategory) {
                        final EgDemandReason reasonDed = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(
                                demandReasonMaster, installment, module);
                        if (demandDetail.getEgDemandReason().getId().equals(reasonDed.getEgDemandReason().getId())) {
                            billdetailRebate.setDrAmount(demandDetail.getAmtRebate());
                            billdetailRebate.setCrAmount(BigDecimal.ZERO);
                            billdetailRebate.setGlcode(reasonDed.getGlcodeId().getGlcode());
                            billdetailRebate.setEgDemandReason(demandDetail.getEgDemandReason());
                            billdetailRebate.setAdditionalFlag(1);
                            billdetailRebate.setCreateDate(currentDate);
                            billdetailRebate.setModifiedDate(currentDate);
                            billdetailRebate.setOrderNo(i++);
                            billdetailRebate.setDescription(reasonDed.getEgDemandReasonMaster().getReasonMaster()
                                    + " - " + installment.getDescription());
                            billdetailRebate.setFunctionCode(TL_FUNCTION_CODE);
                            billdetailRebate.setPurpose(BillAccountDetails.PURPOSE.REBATE.toString());
                            billDetails.add(billdetailRebate);
                        }
                    }
                }
                if (demandDetail.getAmount() != null) {
                    billdetail.setDrAmount(BigDecimal.ZERO);
                    billdetail.setCrAmount(demandDetail.getAmount().subtract(demandDetail.getAmtCollected()));
                }

                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("demandDetail.getEgDemandReason()"
                            + demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster() + " glcodeerror"
                            + demandDetail.getEgDemandReason().getGlcodeId());
                billdetail.setGlcode(demandDetail.getEgDemandReason().getGlcodeId().getGlcode());
                billdetail.setEgDemandReason(demandDetail.getEgDemandReason());
                billdetail.setAdditionalFlag(1);
                billdetail.setCreateDate(currentDate);
                billdetail.setModifiedDate(currentDate);
                billdetail.setOrderNo(i++);
                billdetail.setDescription(reason.getEgDemandReasonMaster().getReasonMaster() + " - "
                        + installment.getDescription());
                billdetail.setFunctionCode(TL_FUNCTION_CODE);
                billdetail.setPurpose(BillAccountDetails.PURPOSE.CURRENT_AMOUNT.toString());
                billDetails.add(billdetail);
            }

        }
        if (LOGGER.isDebugEnabled())
            LOG.debug("created Bill Details");
        return billDetails;
    }

    public Map<Installment, EgDemandDetails> getInstallmentWisePenaltyDemandDetails(final EgDemand currentDemand) {
        final Map<Installment, EgDemandDetails> installmentWisePenaltyDemandDetails = new TreeMap<>();
        if (currentDemand != null)
            for (final EgDemandDetails dmdDet : currentDemand.getEgDemandDetails())
                if (dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode().equals(PENALTY_DMD_REASON_CODE))
                    installmentWisePenaltyDemandDetails.put(dmdDet.getEgDemandReason().getEgInstallmentMaster(), dmdDet);

        return installmentWisePenaltyDemandDetails;
    }

    private EgDemandDetails insertPenaltyAndBillDetails(final BigDecimal penalty, final Installment installment) {
        return insertPenaltyDmdDetail(installment, penalty);
    }

    private EgDemandDetails insertPenaltyDmdDetail(final Installment inst, final BigDecimal penaltyAmount) {
        EgDemandDetails demandDetail = null;
        if (penaltyAmount != null && penaltyAmount.compareTo(BigDecimal.ZERO) > 0) {
            final Module module = license.getTradeName().getLicenseType().getModule();
            final EgDemandReasonMaster egDemandReasonMaster = demandGenericDao.getDemandReasonMasterByCode(
                    PENALTY_DMD_REASON_CODE,
                    module);
            if (egDemandReasonMaster == null)
                throw new ApplicationRuntimeException(" Penalty Demand reason Master is null in method  insertPenalty");

            final EgDemandReason egDemandReason = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(
                    egDemandReasonMaster, inst, module);

            if (egDemandReason == null)
                throw new ApplicationRuntimeException(" Penalty Demand reason is null in method  insertPenalty ");

            demandDetail = createDemandDetails(egDemandReason, BigDecimal.ZERO, penaltyAmount);
        }
        return demandDetail;
    }

    public EgDemandDetails createDemandDetails(final EgDemandReason egDemandReason, final BigDecimal amtCollected,
                                               final BigDecimal dmdAmount) {
        return EgDemandDetails.fromReasonAndAmounts(dmdAmount, egDemandReason, amtCollected);
    }

    @Override
    public void updateReceiptDetails(final Set<BillReceiptInfo> billReceipts) {
        if (billReceipts != null)
            try {
                updateNewReceipt(billReceipts);
            } catch (final Exception e) {
                LOGGER.error("Error occurred while updating receipt details for License");
                throw new ApplicationRuntimeException("Update Receipt Failed", e);
            }
    }

    @Transactional
    public Boolean updateDemandDetails(final BillReceiptInfo billReceipt) {

        try {
            BillReceiptInfoImpl billReceiptInfoImpl;
            billReceiptInfoImpl = (BillReceiptInfoImpl) billReceipt;
            final EgBill bill = egBillDao.findById(Long.valueOf(billReceiptInfoImpl.getBillReferenceNum()), false);
            final EgDemand demand = bill.getEgDemand();
            if (billReceipt.getEvent().equals(EVENT_RECEIPT_CREATED)) {
                final LicenseDemand ld = (LicenseDemand) entityQueryService.load(demand.getId(), LicenseDemand.class);
                ld.getLicense().getTradeName().getLicenseType().getModule();

                final Map<String, Map<String, EgDemandDetails>> installmentWiseDemandDetailsByReason = new HashMap<>();
                Map<String, EgDemandDetails> demandDetailByReason;

                EgDemandReason dmdRsn;
                String installmentDesc;

                for (final EgDemandDetails dmdDtls : demand.getEgDemandDetails())
                    if (dmdDtls.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                        dmdRsn = dmdDtls.getEgDemandReason();
                        installmentDesc = dmdRsn.getEgInstallmentMaster().getDescription();
                        demandDetailByReason = new HashMap<>();
                        if (installmentWiseDemandDetailsByReason.get(installmentDesc) == null) {
                            demandDetailByReason.put(dmdRsn.getEgDemandReasonMaster().getReasonMaster(), dmdDtls);
                            installmentWiseDemandDetailsByReason.put(installmentDesc, demandDetailByReason);
                        } else
                            installmentWiseDemandDetailsByReason.get(installmentDesc).put(
                                    dmdRsn.getEgDemandReasonMaster().getReasonMaster(), dmdDtls);
                    } else if (LOGGER.isDebugEnabled())
                        LOGGER.debug("saveCollectionDetails - demand detail amount is zero " + dmdDtls);

                EgDemandDetails demandDetail;

                for (final ReceiptAccountInfo rcptAccInfo : billReceipt.getAccountDetails())
                    if (rcptAccInfo.getDescription() != null && !rcptAccInfo.getDescription().isEmpty()
                            && rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) > 0) {
                        final String[] desc = rcptAccInfo.getDescription().split("-", 2);
                        final String reason = desc[0].trim();
                        final String instDesc = desc[1].trim();
                        demandDetail = installmentWiseDemandDetailsByReason.get(instDesc).get(reason);
                        demandDetail.addCollectedWithOnePaisaTolerance(rcptAccInfo.getCrAmount());
                        if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getIsDemand())
                            demand.addCollected(rcptAccInfo.getCrAmount());
                        persistCollectedReceipts(demandDetail, billReceipt.getReceiptNum(), billReceipt.getTotalAmount(),
                                billReceipt.getReceiptDate(), demandDetail.getAmtCollected());
                        if (LOGGER.isDebugEnabled())
                            LOGGER.debug("Persisted demand and receipt details for tax : " + reason + " installment : "
                                    + instDesc + " with receipt No : " + billReceipt.getReceiptNum() + " for Rs. "
                                    + rcptAccInfo.getCrAmount());
                    }

                if (ld.getLicense().getState() != null)
                    updateWorkflowState(ld.getLicense());
                licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(ld.getLicense());
                tradeLicenseSmsAndEmailService.sendSMsAndEmailOnCollection(ld.getLicense(), billReceipt.getTotalAmount());
            } else if (billReceipt.getEvent().equals(EVENT_RECEIPT_CANCELLED))
                reconcileCollForRcptCancel(demand, billReceipt);
            else if (billReceipt.getEvent().equals(EVENT_INSTRUMENT_BOUNCED))
                reconcileCollForChequeBounce(demand, billReceipt);// needs to be
        } catch (final Exception e) {
            LOGGER.error("Error occurred while updating demand details", e);
            throw new ApplicationRuntimeException("Updating Demand Details Failed", e);
        }

        return true;
    }

    /**
     * update Application status and workflow
     */
    @Transactional
    public void updateWorkflowState(final License licenseObj) {
        final DateTime currentDate = new DateTime();
        final User user = securityUtils.getCurrentUser();
        final Boolean digitalSignEnabled = licenseUtils.isDigitalSignEnabled();
        WorkFlowMatrix wfmatrix = null;
        final String natureOfWork = licenseObj.isReNewApplication()
                ? Constants.RENEWAL_NATUREOFWORK : Constants.NEW_NATUREOFWORK;
        if (digitalSignEnabled && !licenseObj.getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_CREATED_CODE)) {
            licenseUtils.applicationStatusChange(licenseObj, APPLICATION_STATUS_DIGUPDATE_CODE);
            final Position position = licenseUtils.getCityLevelCommissioner();
            licenseUtils.applicationStatusChange(licenseObj, Constants.APPLICATION_STATUS_APPROVED_CODE);
            if (licenseObj.isReNewApplication())
                licenseObj.transition(true).withSenderName(user.getUsername() + Constants.DELIMITER_COLON + user.getName())
                        .withComments(Constants.WF_SECOND_LVL_FEECOLLECTED)
                        .withStateValue(Constants.DIGI_ENABLED_WF_SECOND_LVL_FEECOLLECTED).withDateInfo(currentDate.toDate())
                        .withOwner(position).withNextAction(Constants.WF_ACTION_DIGI_PENDING);
            else
                licenseObj.transition(true).withSenderName(user.getUsername() + Constants.DELIMITER_COLON + user.getName())
                        .withComments(Constants.WF_SECOND_LVL_FEECOLLECTED)
                        .withStateValue(Constants.DIGI_ENABLED_WF_SECOND_LVL_FEECOLLECTED).withDateInfo(currentDate.toDate())
                        .withOwner(position).withNextAction(Constants.WF_ACTION_DIGI_PENDING);
        } else {
            licenseUtils.licenseStatusUpdate(licenseObj, Constants.STATUS_UNDERWORKFLOW);
            if (licenseObj.getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_CREATED_CODE))
                licenseUtils.applicationStatusChange(licenseObj, Constants.APPLICATION_STATUS_FIRSTCOLLECTIONDONE_CODE);
            else
                licenseUtils.applicationStatusChange(licenseObj, Constants.APPLICATION_STATUS_APPROVED_CODE);
            if (licenseObj.isReNewApplication()) {
                if (licenseObj.getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_FIRSTCOLLECTIONDONE_CODE))
                    wfmatrix = tradeLicenseWorkflowService.getWfMatrix(TRADELICENSE, null, null, Constants.RENEW_ADDITIONAL_RULE,
                            Constants.WF_LICENSE_CREATED, null);
                else if (licenseObj.getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_APPROVED_CODE))
                    wfmatrix = tradeLicenseWorkflowService.getWfMatrix(TRADELICENSE, null, null, Constants.RENEW_ADDITIONAL_RULE,
                            Constants.WF_STATE_COMMISSIONER_APPROVED_STR, null);
            } else if (licenseObj.isNewApplication())
                if (licenseObj.getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_FIRSTCOLLECTIONDONE_CODE))
                    wfmatrix = tradeLicenseWorkflowService.getWfMatrix(TRADELICENSE, null, null, Constants.NEW_ADDITIONAL_RULE,
                            Constants.WF_LICENSE_CREATED, null);
                else if (licenseObj.getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_APPROVED_CODE))
                    wfmatrix = tradeLicenseWorkflowService.getWfMatrix(TRADELICENSE, null, null, Constants.NEW_ADDITIONAL_RULE,
                            Constants.WF_STATE_COMMISSIONER_APPROVED_STR, null);
            if (wfmatrix != null)
                licenseObj.transition(true).withSenderName(user.getUsername() + Constants.DELIMITER_COLON + user.getName())
                        .withComments(wfmatrix.getNextStatus()).withNatureOfTask(natureOfWork)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate())
                        .withOwner(licenseObj.getState().getInitiatorPosition())
                        .withNextAction(wfmatrix.getNextAction());
        }
    }

    /**
     * Deducts the collected amounts as per the amount of the cancelled receipt.
     */
    protected void reconcileCollForRcptCancel(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
        cancelBill(Long.valueOf(billRcptInfo.getBillReferenceNum()));
        demand.setAmtCollected(demand.getAmtCollected().subtract(billRcptInfo.getTotalAmount()));
        updateDmdDetForRcptCancel(demand, billRcptInfo);
    }

    private EgDemand cancelBill(final Long billId) {
        final EgDemand egDemand = null;
        if (billId != null) {
            final EgBill egBill = egBillDao.findById(billId, false);
            egBill.setIs_Cancelled("Y");
        }
        return egDemand;
    }

    /**
     * Reconciles the collection for respective account heads thats been paid with given cancel receipt
     *
     * @param demand
     * @param billRcptInfo
     */
    private void updateDmdDetForRcptCancel(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
        LOGGER.debug("Entering method updateDmdDetForRcptCancel");
        for (final ReceiptAccountInfo rcptAccInfo : billRcptInfo.getAccountDetails())
            if (rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) > 0
                    && !rcptAccInfo.getIsRevenueAccount()) {
                final String[] desc = rcptAccInfo.getDescription().split("-", 2);
                final String reason = desc[0].trim();
                final String installment = desc[1].trim();
                for (final EgDemandDetails demandDetail : demand.getEgDemandDetails())
                    if (reason.equals(demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster())
                            && installment.equals(demandDetail.getEgDemandReason().getEgInstallmentMaster()
                            .getDescription())) {
                        demandDetail
                                .setAmtCollected(demandDetail.getAmtCollected().subtract(rcptAccInfo.getCrAmount()));
                        LOGGER.info("Deducted Collected amount and receipt details for tax : " + reason
                                + " installment : " + installment + " with receipt No : "
                                + billRcptInfo.getReceiptNum() + " for Rs. " + demandDetail.getAmtCollected());
                    }
            }
        updateReceiptStatusWhenCancelled(billRcptInfo.getReceiptNum());
        LOGGER.debug("Exiting method saveCollectionAndDemandDetails");
    }

    protected void updateReceiptStatusWhenCancelled(final String receiptNumber) {
        final List<EgdmCollectedReceipt> egdmCollectedReceipts = demandGenericDao
                .getAllEgdmCollectedReceipts(receiptNumber);
        if (egdmCollectedReceipts != null && !egdmCollectedReceipts.isEmpty())
            for (final EgdmCollectedReceipt egDmCollectedReceipt : egdmCollectedReceipts) {
                egDmCollectedReceipt.setStatus(DemandConstants.CANCELLED_RECEIPT);
                egDmCollectedReceipt.setUpdatedTime(new Date());
            }
    }

    protected BigDecimal reconcileCollForChequeBounce(final EgDemand demand, final BillReceiptInfo billRcptInfo) {

        /**
         * Deducts the collected amounts as per the amount of the bounced cheque, and also imposes a cheque-bounce penalty.
         */
        LOGGER.debug("updateDemandForChequeBounce : Updating Collection For Demand : Demand - " + demand
                + " with BillReceiptInfo - " + billRcptInfo);
        final LicenseDemand ld = (LicenseDemand) demand;
        BigDecimal totalCollChqBounced = getTotalChequeAmt(billRcptInfo);
        final BigDecimal chqBouncePenalty = CHQ_BOUNCE_PENALTY;
        EgDemandDetails dmdDet;
        final EgDemandDetails penaltyDmdDet = getDemandDetail(demand, DEMANDRSN_STR_CHQ_BOUNCE_PENALTY);
        if (penaltyDmdDet == null)
            dmdDet = insertPenalty(chqBouncePenalty, ld.getLicense().getTradeName().getLicenseType().getModule());
        else {
            BigDecimal existDmdDetAmt = penaltyDmdDet.getAmount();
            existDmdDetAmt = existDmdDetAmt == null || existDmdDetAmt.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
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
        totalCollChqBounced = updateDmdDetForChqBounce(demand, totalCollChqBounced);
        LOGGER.debug("updateDemandForChequeBounce : Updated Collection For Demand : " + demand);
        return totalCollChqBounced;
    }

    /**
     * reverse the amount collected for each demand detail
     *
     * @param demand
     * @param totalCollChqBounced
     */

    private BigDecimal updateDmdDetForChqBounce(final EgDemand demand, BigDecimal totalCollChqBounced) {
        final List<EgDemandDetails> demandList = (List<EgDemandDetails>) demand.getEgDemandDetails();
        demandList.sort(Comparator.comparing(
                (final EgDemandDetails demandDetails) -> demandDetails.getEgDemandReason().getEgDemandReasonMaster().getOrderId())
                .reversed());
        for (final EgDemandDetails dd : demandList) {
            final BigDecimal amtCollected = dd.getAmtCollected();
            totalCollChqBounced = totalCollChqBounced.subtract(amtCollected);
            if (totalCollChqBounced.longValue() >= 0) {
                dd.setAmtCollected(BigDecimal.ZERO);
                demand.setBaseDemand(demand.getBaseDemand().subtract(amtCollected));
            } else {
                dd.setAmtCollected(amtCollected.subtract(totalCollChqBounced));
                demand.setBaseDemand(demand.getBaseDemand().subtract(totalCollChqBounced));
                totalCollChqBounced = BigDecimal.ZERO;

            }

        }
        return totalCollChqBounced;
    }

    private EgDemandDetails getDemandDetail(final EgDemand demand,
                                            final String demandrsnStrChqBouncePenalty) {
        EgDemandDetails chqBounceDemand = null;
        for (final EgDemandDetails dd : demand.getEgDemandDetails())
            if (dd.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()
                    .equalsIgnoreCase(demandrsnStrChqBouncePenalty)) {
                chqBounceDemand = dd;
                break;
            }
        return chqBounceDemand;
    }

    @Transactional
    public boolean updateNewReceipt(final Set<BillReceiptInfo> billReceipts) {
        try {
            for (final BillReceiptInfo bri : billReceipts) {
                linkBillToReceipt(bri);
                updateBillDetails(bri);
                updateDemandDetails(bri);
            }
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Error occurred while updating receipt for license", e);
        }
        return true;

    }

    private EgBill updateBill(final BillReceiptInfo bri, final EgBill egBill, final BigDecimal totalCollectedAmt)
            throws InvalidAccountHeadException {
        if (bri != null) {
            for (final EgBillDetails billDet : egBill.getEgBillDetails()) {
                Boolean glCodeExist = false;
                for (final ReceiptAccountInfo acctDet : bri.getAccountDetails())
                    if (billDet.getGlcode().equals(acctDet.getGlCode())) {
                        glCodeExist = true;
                        BigDecimal amtCollected = billDet.getCollectedAmount();
                        if (amtCollected == null)
                            amtCollected = BigDecimal.ZERO;
                        billDet.setCollectedAmount(acctDet.getCrAmount().subtract(amtCollected));
                        egBillDetailsDao.update(billDet);
                    }
                if (!glCodeExist)
                    throw new InvalidAccountHeadException("GlCode does not exist for " + billDet.getGlcode());
            }
            egBill.setTotalCollectedAmount(totalCollectedAmt);
            egBillDao.update(egBill);
        }
        return egBill;
    }

    public BigDecimal getEgBillDetailCollection(final EgBillDetails billdet) {
        BigDecimal collectedAmt = billdet.getCollectedAmount();
        if (billdet.getCollectedAmount() == null)
            collectedAmt = BigDecimal.ZERO;
        return collectedAmt;

    }

    public EgBill updateBillForChqBounce(final EgBill egBill, final BigDecimal totalChqAmt) {
        final BigDecimal zeroVal = BigDecimal.ZERO;
        if (totalChqAmt != null && totalChqAmt.compareTo(zeroVal) != 0 && egBill != null) {
            final List<EgBillDetails> billList = new ArrayList<>(egBill.getEgBillDetails());
            // Reversed the list because the knocking off the amount should
            // start from current Installment to least Installment.
            Collections.reverse(billList);
            BigDecimal carry = totalChqAmt;
            for (final EgBillDetails billdet : billList) {
                BigDecimal remAmount = BigDecimal.ZERO;
                final BigDecimal balanceAmt = getEgBillDetailCollection(billdet);
                if (balanceAmt != null && balanceAmt.compareTo(zeroVal) > 0) {
                    if (carry.compareTo(zeroVal) > 0 && carry.subtract(balanceAmt).compareTo(zeroVal) > 0) {
                        carry = carry.subtract(balanceAmt);
                        remAmount = balanceAmt;
                    } else if (carry.compareTo(zeroVal) > 0 && carry.subtract(balanceAmt).compareTo(zeroVal) <= 0) {
                        remAmount = carry;
                        carry = BigDecimal.ZERO;
                    }
                    if (remAmount.compareTo(zeroVal) > 0) {
                        billdet.setCollectedAmount(remAmount);
                        egBillDetailsDao.update(billdet);
                    }
                }
            }
            egBill.setTotalCollectedAmount(totalChqAmt);
            egBillDao.update(egBill);
        }
        return egBill;
    }

    public EgBill updateBillDetails(final BillReceiptInfo bri) throws InvalidAccountHeadException {
        EgBill egBill;
        if (bri == null)
            throw new ApplicationRuntimeException(" Bill Receipt Info not found");
        egBill = egBillDao.findById(new Long(bri.getBillReferenceNum()), false);
        final List<EgBillDetails> billDetList = egBillDetailsDao.getBillDetailsByBill(egBill);

        if (bri.getEvent() != null && bri.getEvent().equals(BillingIntegrationService.EVENT_RECEIPT_CREATED)) {
            final BigDecimal totalCollectedAmt = calculateTotalCollectedAmt(bri, billDetList);
            egBill = updateBill(bri, egBill, totalCollectedAmt);
        } else if (bri.getEvent() != null && bri.getEvent().equals(BillingIntegrationService.EVENT_INSTRUMENT_BOUNCED))
            egBill = updateBillForChqBounce(egBill, getTotalChequeAmt(bri));
        return egBill;
    }

    public BigDecimal getTotalChequeAmt(final BillReceiptInfo bri) {
        BigDecimal totalCollAmt = BigDecimal.ZERO;
        try {
            if (bri != null)
                for (final ReceiptInstrumentInfo rctInst : bri.getBouncedInstruments())
                    if (rctInst.getInstrumentAmount() != null)
                        totalCollAmt = totalCollAmt.add(rctInst.getInstrumentAmount());
        } catch (final ApplicationRuntimeException e) {
            throw new ApplicationRuntimeException("Exception in calculate Total Collected Amt" + e);
        }

        return totalCollAmt;
    }

    public BigDecimal calculateTotalCollectedAmt(final BillReceiptInfo bri, final List<EgBillDetails> billDetList)
            throws InvalidAccountHeadException {
        BigDecimal totalCollAmt = BigDecimal.ZERO;
        try {
            if (bri != null && billDetList != null)
                for (final EgBillDetails billDet : billDetList) {
                    Boolean glCodeExist = false;
                    for (final ReceiptAccountInfo acctDet : bri.getAccountDetails())
                        if (billDet.getGlcode().equals(acctDet.getGlCode())) {
                            glCodeExist = true;
                            totalCollAmt = totalCollAmt.add(acctDet.getCrAmount());
                        }
                    if (!glCodeExist)
                        throw new InvalidAccountHeadException("GlCode does not exist for " + billDet.getGlcode());
                }
        } catch (final ApplicationRuntimeException e) {
            throw new ApplicationRuntimeException("Exception in calculate Total Collected Amt" + e);
        }

        return totalCollAmt;
    }

    private BillReceipt prepareBillReceiptBean(final BillReceiptInfo bri, final EgBill egBill,
                                               final BigDecimal totalCollectedAmt) {

        BillReceipt billRecpt = null;
        if (bri != null && egBill != null && totalCollectedAmt != null) {
            billRecpt = new BillReceipt();
            billRecpt.setBillId(egBill);
            billRecpt.setReceiptAmt(totalCollectedAmt);
            billRecpt.setReceiptNumber(bri.getReceiptNum());
            billRecpt.setReceiptDate(bri.getReceiptDate());
            billRecpt.setCollectionStatus(bri.getReceiptStatus().getCode());
            billRecpt.setCreatedBy(bri.getCreatedBy());
            billRecpt.setModifiedBy(bri.getModifiedBy());
            billRecpt.setCreatedDate(new Date());
            billRecpt.setModifiedDate(new Date());
            billRecpt.setIsCancelled(Boolean.FALSE);
        }
        return billRecpt;
    }

    private BillReceipt linkBillToReceipt(final BillReceiptInfo bri) throws InvalidAccountHeadException {
        BillReceipt billRecpt = null;
        if (bri == null)
            throw new ApplicationRuntimeException(" BillReceiptInfo Object is null ");
        final EgBill egBill = egBillDao.findById(new Long(bri.getBillReferenceNum()), false);
        if (egBill == null)
            throw new ApplicationRuntimeException(" EgBill Object is null for the Bill Number"
                    + bri.getBillReferenceNum());
        final List<EgBillDetails> billDetList = egBillDetailsDao.getBillDetailsByBill(egBill);
        final BigDecimal totalCollectedAmt = calculateTotalCollectedAmt(bri, billDetList);
        if (bri.getEvent() == null)
            throw new ApplicationRuntimeException(" Event in BillReceiptInfo Object is Null");
        if (bri.getEvent().equals(BillingIntegrationService.EVENT_RECEIPT_CREATED)) {
            billRecpt = prepareBillReceiptBean(bri, egBill, totalCollectedAmt);
            egBillReceiptDao.create(billRecpt);

        } else if (bri.getEvent().equals(BillingIntegrationService.EVENT_RECEIPT_CANCELLED))
            billRecpt = updateBillReceiptForCancellation(bri, egBill, totalCollectedAmt);
        return billRecpt;
    }

    private BillReceipt updateBillReceiptForCancellation(final BillReceiptInfo bri, final EgBill egBill,
                                                         final BigDecimal totalCollectedAmt) {
        BillReceipt billRecpt;
        if (bri == null)
            throw new ApplicationRuntimeException(" BillReceiptInfo Object is null ");
        if (egBill != null && totalCollectedAmt != null) {
            billRecpt = egBillReceiptDao.getBillReceiptByEgBill(egBill);
            if (billRecpt == null)
                throw new ApplicationRuntimeException(" Bill receipt Object is null for the EgBill " + egBill.getId());
            if (bri.getEvent().equals(BillingIntegrationService.EVENT_RECEIPT_CANCELLED))
                billRecpt.setIsCancelled(Boolean.TRUE);
            billRecpt.setReceiptAmt(totalCollectedAmt.subtract(billRecpt.getReceiptAmt()));
        } else
            throw new ApplicationRuntimeException(" EgBill Object is null for the Bill Number"
                    + bri.getBillReferenceNum() + "in updateBillReceiptForCancellation method");
        return billRecpt;
    }

    protected EgdmCollectedReceipt persistCollectedReceipts(final EgDemandDetails egDemandDetails,
                                                            final String receiptNumber, final BigDecimal receiptAmount, final Date receiptDate,
                                                            final BigDecimal reasonAmount) {
        final EgdmCollectedReceipt egDmCollectedReceipt = new EgdmCollectedReceipt();
        egDmCollectedReceipt.setReceiptNumber(receiptNumber);
        egDmCollectedReceipt.setReceiptDate(receiptDate);
        egDmCollectedReceipt.setAmount(receiptAmount);
        egDmCollectedReceipt.setReasonAmount(reasonAmount);
        egDmCollectedReceipt.setStatus(DemandConstants.NEWRECEIPT);
        egDmCollectedReceipt.setEgdemandDetail(egDemandDetails);
        egdmCollectedReceiptDao.create(egDmCollectedReceipt);
        return egDmCollectedReceipt;
    }

    @Override
    public void apportionPaidAmount(final String billReferenceNumber, final BigDecimal actualAmountPaid,
                                    final ArrayList<ReceiptDetail> receiptDetailsArray) {
        // No logic now
    }

    /**
     * Method used to insert penalty in EgDemandDetail table. Penalty Amount will be calculated depending upon the cheque Amount.
     *
     * @param chqBouncePenalty
     * @return New EgDemandDetails Object
     */
    EgDemandDetails insertPenalty(final BigDecimal chqBouncePenalty, final Module module) {
        EgDemandDetails demandDetail = null;
        if (chqBouncePenalty != null && chqBouncePenalty.compareTo(BigDecimal.ZERO) > 0) {
            final Installment currInstallment = getCurrentInstallment(module);
            final EgDemandReasonMaster egDemandReasonMaster = demandGenericDao.getDemandReasonMasterByCode(
                    DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY, module);
            if (egDemandReasonMaster == null)
                throw new ApplicationRuntimeException(" Penalty Demand reason Master is null in method  insertPenalty");
            final EgDemandReason egDemandReason = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(
                    egDemandReasonMaster, currInstallment, module);
            if (egDemandReason == null)
                throw new ApplicationRuntimeException(" Penalty Demand reason is null in method  insertPenalty ");
            demandDetail = EgDemandDetails.fromReasonAndAmounts(chqBouncePenalty, egDemandReason, BigDecimal.ZERO);
        }
        return demandDetail;
    }

    protected Installment getInstallmentForDate(final Date date, final Module module) {
        return installmentDao.getInsatllmentByModuleForGivenDate(module, date);
    }

    protected Installment getCurrentInstallment(final Module module) {
        return getInstallmentForDate(new Date(), module);
    }

    @Override
    public void cancelBill() {
        // No logic now
    }

    @Override
    public List<ReceiptDetail> reconstructReceiptDetail(final String billReferenceNumber,
                                                        final BigDecimal actualAmountPaid, final List<ReceiptDetail> receiptDetailList) {
        return Collections.emptyList();
    }

    @Override
    public String constructAdditionalInfoForReceipt(final BillReceiptInfo billReceiptInfo) {
        return null;
    }

    @Override
    public ReceiptAmountInfo receiptAmountBifurcation(final BillReceiptInfo billReceiptInfo) {
        return new ReceiptAmountInfo();
    }


    public Map<String, Map<String, BigDecimal>> getPaymentFee(final License license) {
        final Map<String, Map<String, BigDecimal>> outstandingFee = new HashMap<>();
        final LicenseDemand licenseDemand = license.getCurrentDemand();

        for (final EgDemandDetails demandDetail : licenseDemand.getEgDemandDetails()) {
            Date fromDate = license.isNewApplication() ? license.getCommencementDate() : demandDetail.getEgDemandReason().getEgInstallmentMaster().getFromDate();
            final String demandReason = demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster();
            final Installment installmentYear = demandDetail.getEgDemandReason().getEgInstallmentMaster();
            Map<String, BigDecimal> feeByTypes;
            if (!demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode().equals(Constants.PENALTY_DMD_REASON_CODE)
                    && demandDetail.getAmount().subtract(demandDetail.getAmtCollected()).signum() == 1) {
                if (outstandingFee.containsKey(installmentYear.getDescription()))
                    feeByTypes = outstandingFee.get(installmentYear.getDescription());
                else {
                    feeByTypes = new HashMap<>();
                    feeByTypes.put(demandReason, ZERO);
                }
                final BigDecimal demandAmount = demandDetail.getAmount().subtract(demandDetail.getAmtCollected());
                feeByTypes.put(demandReason, demandAmount);
                BigDecimal penaltyAmt = licenseUtils.calculatePenalty(license, fromDate, new Date(), demandDetail.getAmount());
                if (penaltyAmt.compareTo(BigDecimal.ZERO) > 0)
                    feeByTypes.put("Penalty", penaltyAmt);
                outstandingFee.put(installmentYear.getDescription(), feeByTypes);
            }
        }
        return outstandingFee;

    }
}