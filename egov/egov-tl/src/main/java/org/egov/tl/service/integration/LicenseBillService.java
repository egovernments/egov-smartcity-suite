/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.tl.service.integration;

import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.integration.models.BillAccountDetails;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.BillReceiptInfoImpl;
import org.egov.collection.integration.models.ReceiptAccountInfo;
import org.egov.collection.integration.models.ReceiptAmountInfo;
import org.egov.collection.integration.models.ReceiptInstrumentInfo;
import org.egov.collection.integration.services.BillingIntegrationService;
import org.egov.commons.Installment;
import org.egov.commons.dao.EgwStatusHibernateDAO;
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
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.tl.entity.FeeType;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.service.FeeTypeService;
import org.egov.tl.service.LicenseApplicationService;
import org.egov.tl.service.LicenseCitizenPortalService;
import org.egov.tl.service.LicenseConfigurationService;
import org.egov.tl.service.LicenseStatusService;
import org.egov.tl.service.PenaltyRatesService;
import org.egov.tl.service.TradeLicenseSmsAndEmailService;
import org.egov.tl.service.es.LicenseApplicationIndexService;
import org.egov.tl.utils.LicenseNumberUtils;
import org.egov.tl.utils.LicenseUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ZERO;
import static org.egov.demand.utils.DemandConstants.DEMAND_REASON_CATEGORY_FEE;
import static org.egov.demand.utils.DemandConstants.DEMAND_REASON_CATEGORY_PENALTY;
import static org.egov.tl.utils.Constants.*;

@Service("TLCollectionsInterface")
@Transactional(readOnly = true)
public class LicenseBillService extends BillServiceInterface implements BillingIntegrationService {
    private static final Logger LOG = LoggerFactory.getLogger(LicenseBillService.class);
    private static final String TL_FUNCTION_CODE = "1500";

    @Autowired
    protected EgBillDetailsDao egBillDetailsDao;

    @Autowired
    @Qualifier("tradeLicenseWorkflowService")
    protected SimpleWorkflowService tradeLicenseWorkflowService;

    @Autowired
    protected SecurityUtils securityUtils;

    @Autowired
    protected EgBillReceiptDao egBillReceiptDao;

    @Autowired
    protected LicenseApplicationIndexService licenseApplicationIndexService;

    @Autowired
    protected TradeLicenseSmsAndEmailService tradeLicenseSmsAndEmailService;

    @Autowired
    protected EgBillDao egBillDao;

    @Autowired
    protected DemandGenericDao demandGenericDao;

    @Autowired
    protected EgdmCollectedReceiptDao egdmCollectedReceiptDao;

    @Autowired
    protected InstallmentDao installmentDao;

    @Autowired
    protected LicenseUtils licenseUtils;

    @Autowired
    protected PenaltyRatesService penaltyRatesService;

    @Autowired
    protected LicenseNumberUtils licenseNumberUtils;

    @Autowired
    @Qualifier("licenseApplicationService")
    protected LicenseApplicationService licenseApplicationService;

    @Autowired
    protected LicenseCitizenPortalService licenseCitizenPortalService;

    @Autowired
    protected LicenseStatusService licenseStatusService;

    @Autowired
    protected EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    protected LicenseConfigurationService licenseConfigurationService;

    @Autowired
    protected FeeTypeService feeTypeService;

    @Transactional
    public String createLicenseBillXML(TradeLicense license) {
        LicenseBill licenseBill = new LicenseBill();
        licenseBill.setLicense(license);
        licenseBill.setModuleName(TRADE_LICENSE);
        licenseBill.setServiceCode(TL_SERVICE_CODE);
        licenseBill.setModule(licenseUtils.getModule());
        licenseBill.setBillType(egBillDao.getBillTypeByCode(BILL_TYPE_AUTO));
        licenseBill.setDepartmentCode(licenseConfigurationService.getDepartmentCodeForBillGenerate());
        licenseBill.setCollModesNotAllowed(licenseConfigurationService.getValueByKey(DISABLED_PAYMENT_MODES));
        licenseBill.setUserId(ApplicationThreadLocals.getUserId() == null ?
                securityUtils.getCurrentUser().getId() : ApplicationThreadLocals.getUserId());
        licenseBill.setReferenceNumber(licenseNumberUtils.generateBillNumber());
        return getBillXML(licenseBill);
    }

    @Override
    @Transactional
    public List<EgBillDetails> getBilldetails(final Billable billObj) {
        final List<EgBillDetails> billDetails = new ArrayList<>();
        final LicenseBill billable = (LicenseBill) billObj;
        final TradeLicense license = billable.getLicense();
        final EgDemand demand = billObj.getCurrentDemand();
        final Date currentDate = new Date();
        final Map installmentWise = new HashMap<Installment, List<EgDemandDetails>>();
        final Set<Installment> sortedInstallmentSet = new TreeSet<>();
        Module module = licenseUtils.getModule();
        getCurrentInstallment(module);
        final List<EgDemandDetails> orderedDetailsList = new ArrayList<>();
        licenseApplicationService.applyPenalty(license, demand);
        license.recalculateBaseDemand();
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
                    .compareTo(demandDetail.getAmtCollected()) > 0) {
                final EgBillDetails billdetail = new EgBillDetails();
                final EgBillDetails billdetailRebate = new EgBillDetails();
                if (demandDetail.getAmtRebate() != null && demandDetail.getAmtRebate().compareTo(ZERO) != 0) {
                    final EgReasonCategory reasonCategory = demandGenericDao.getReasonCategoryByCode("REBATE");
                    final List<EgDemandReasonMaster> demandReasonMasterByCategory = demandGenericDao
                            .getDemandReasonMasterByCategoryAndModule(reasonCategory, module);
                    for (final EgDemandReasonMaster demandReasonMaster : demandReasonMasterByCategory) {
                        final EgDemandReason reasonDed = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(
                                demandReasonMaster, installment, module);
                        if (demandDetail.getEgDemandReason().getId().equals(reasonDed.getEgDemandReason().getId())) {
                            billdetailRebate.setDrAmount(demandDetail.getAmtRebate());
                            billdetailRebate.setCrAmount(ZERO);
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
                    billdetail.setDrAmount(ZERO);
                    billdetail.setCrAmount(demandDetail.getAmount().subtract(demandDetail.getAmtCollected()));
                }

                if (LOG.isDebugEnabled())
                    LOG.debug("Demand Reason : {}, GLCode : {}", demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster(),
                            demandDetail.getEgDemandReason().getGlcodeId());
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
        if (LOG.isDebugEnabled())
            LOG.debug("License Bill Details Created");
        return billDetails;
    }

    @Override
    public void updateReceiptDetails(final Set<BillReceiptInfo> billReceipts) {
        if (billReceipts != null)
            try {
                updateNewReceipt(billReceipts);
            } catch (ValidationException e) {
                throw e;
            } catch (Exception e) {
                LOG.error("Error occurred while updating receipt details for License", e);
                throw new ApplicationRuntimeException("Update License Receipt Failed", e);
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
                final TradeLicense tradeLicense = licenseApplicationService.getLicenseByDemand(demand);
                final Map<String, Map<String, EgDemandDetails>> installmentWiseDemandDetailsByReason = new HashMap<>();
                Map<String, EgDemandDetails> demandDetailByReason;
                EgDemandReason dmdRsn;
                String installmentDesc;

                for (final EgDemandDetails dmdDtls : demand.getEgDemandDetails())
                    if (dmdDtls.getAmount().compareTo(ZERO) > 0) {
                        dmdRsn = dmdDtls.getEgDemandReason();
                        installmentDesc = dmdRsn.getEgInstallmentMaster().getDescription();
                        demandDetailByReason = new HashMap<>();
                        if (installmentWiseDemandDetailsByReason.get(installmentDesc) == null) {
                            demandDetailByReason.put(dmdRsn.getEgDemandReasonMaster().getReasonMaster(), dmdDtls);
                            installmentWiseDemandDetailsByReason.put(installmentDesc, demandDetailByReason);
                        } else
                            installmentWiseDemandDetailsByReason.get(installmentDesc).put(
                                    dmdRsn.getEgDemandReasonMaster().getReasonMaster(), dmdDtls);
                    }

                EgDemandDetails demandDetail;

                for (final ReceiptAccountInfo rcptAccInfo : billReceipt.getAccountDetails())
                    if (rcptAccInfo.getDescription() != null && !rcptAccInfo.getDescription().isEmpty()
                            && rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(ZERO) > 0) {
                        final String[] desc = rcptAccInfo.getDescription().split("-", 2);
                        final String reason = desc[0].trim();
                        final String instDesc = desc[1].trim();
                        demandDetail = installmentWiseDemandDetailsByReason.get(instDesc).get(reason);
                        demandDetail.addCollectedWithOnePaisaTolerance(rcptAccInfo.getCrAmount());
                        if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getIsDemand())
                            demand.addCollected(rcptAccInfo.getCrAmount());
                        persistCollectedReceipts(demandDetail, billReceipt.getReceiptNum(), billReceipt.getTotalAmount(),
                                billReceipt.getReceiptDate(), demandDetail.getAmtCollected());
                        if (LOG.isDebugEnabled())
                            LOG.debug("Created demand and receipt for Reason : {}, Installment : {}, Receipt No.: {}, Amount : {}"
                                    , reason, instDesc, billReceipt.getReceiptNum(), rcptAccInfo.getCrAmount());
                    }

                if (tradeLicense.hasState()) {
                    if (tradeLicense.transitionCompleted())
                        throw new ValidationException("TL-008", "License application may be already cancelled");
                    if (tradeLicense.isNewWorkflow()) {
                        tradeLicense.setCollectionPending(false);
                        licenseApplicationService.collectionTransition(tradeLicense);
                    } else
                        updateWorkflowState(tradeLicense);
                }
                licenseCitizenPortalService.onUpdate(tradeLicense);
                licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(tradeLicense);
                tradeLicenseSmsAndEmailService.sendSMsAndEmailOnCollection(tradeLicense, billReceipt.getTotalAmount());
            } else if (billReceipt.getEvent().equals(EVENT_RECEIPT_CANCELLED))
                reconcileCollForRcptCancel(demand, billReceipt);
            else if (billReceipt.getEvent().equals(EVENT_INSTRUMENT_BOUNCED))
                reconcileCollForChequeBounce(demand, billReceipt);// needs to be
        } catch (ValidationException e) {
            throw e;
        } catch (final Exception e) {
            LOG.error("Error occurred while updating License demand details", e);
            throw new ApplicationRuntimeException("Updating License Demand Details Failed", e);
        }

        return true;
    }

    /**
     * update Application status and workflow
     */
    @Transactional
    public void updateWorkflowState(final TradeLicense licenseObj) {
        final DateTime currentDate = new DateTime();
        final User user = securityUtils.getCurrentUser();
        WorkFlowMatrix wfmatrix = null;
        if (licenseConfigurationService.digitalSignEnabled() && !licenseObj.getEgwStatus().getCode().equals(APPLICATION_STATUS_CREATED_CODE)) {
            licenseObj.setEgwStatus(egwStatusHibernateDAO
                    .getStatusByModuleAndCode(TRADELICENSEMODULE, APPLICATION_STATUS_APPROVED_CODE));
            licenseObj.transition().progressWithStateCopy().withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                    .withComments(WF_SECOND_LVL_FEECOLLECTED)
                    .withStateValue(DIGI_ENABLED_WF_SECOND_LVL_FEECOLLECTED).withDateInfo(currentDate.toDate())
                    .withNextAction(WF_ACTION_DIGI_PENDING);

        } else {
            licenseObj.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_UNDERWORKFLOW));
            if (licenseObj.getEgwStatus().getCode().equals(APPLICATION_STATUS_CREATED_CODE))
                licenseObj.setEgwStatus(egwStatusHibernateDAO
                        .getStatusByModuleAndCode(TRADELICENSEMODULE, APPLICATION_STATUS_FIRSTCOLLECTIONDONE_CODE));
            else
                licenseObj.setEgwStatus(egwStatusHibernateDAO
                        .getStatusByModuleAndCode(TRADELICENSEMODULE, APPLICATION_STATUS_APPROVED_CODE));
            if (licenseObj.isReNewApplication()) {
                if (licenseObj.getEgwStatus().getCode().equals(APPLICATION_STATUS_FIRSTCOLLECTIONDONE_CODE))
                    wfmatrix = tradeLicenseWorkflowService.getWfMatrix(TRADELICENSE, null, null, RENEW_ADDITIONAL_RULE,
                            WF_LICENSE_CREATED, null);
                else if (licenseObj.getEgwStatus().getCode().equals(APPLICATION_STATUS_APPROVED_CODE))
                    wfmatrix = tradeLicenseWorkflowService.getWfMatrix(TRADELICENSE, null, null, RENEW_ADDITIONAL_RULE,
                            WF_STATE_COMMISSIONER_APPROVED_STR, null);
            } else if (licenseObj.isNewApplication())
                if (licenseObj.getEgwStatus().getCode().equals(APPLICATION_STATUS_FIRSTCOLLECTIONDONE_CODE))
                    wfmatrix = tradeLicenseWorkflowService.getWfMatrix(TRADELICENSE, null, null, NEW_ADDITIONAL_RULE,
                            WF_LICENSE_CREATED, null);
                else if (licenseObj.getEgwStatus().getCode().equals(APPLICATION_STATUS_APPROVED_CODE))
                    wfmatrix = tradeLicenseWorkflowService.getWfMatrix(TRADELICENSE, null, null, NEW_ADDITIONAL_RULE,
                            WF_STATE_COMMISSIONER_APPROVED_STR, null);
            if (wfmatrix != null)
                licenseObj.transition().progressWithStateCopy().withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                        .withComments(wfmatrix.getNextStatus()).withNatureOfTask(licenseObj.getLicenseAppType().getName())
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate())
                        .withOwner(licenseObj.getState().getInitiatorPosition())
                        .withNextAction(wfmatrix.getNextAction());
        }
    }

    /**
     * Deducts the collected amounts as per the amount of the cancelled receipt.
     */
    private void reconcileCollForRcptCancel(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
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
        for (final ReceiptAccountInfo rcptAccInfo : billRcptInfo.getAccountDetails())
            if (rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(ZERO) > 0
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

                        if (LOG.isDebugEnabled())
                            LOG.debug("Deducted collected amount for Reason : {}, Installment : {}, Receipt No.: {}, Amount : {}"
                                    , reason, installment, billRcptInfo.getReceiptNum(), demandDetail.getAmtCollected());
                    }
            }
        updateReceiptStatusWhenCancelled(billRcptInfo.getReceiptNum());
    }

    private void updateReceiptStatusWhenCancelled(final String receiptNumber) {
        final List<EgdmCollectedReceipt> egdmCollectedReceipts = demandGenericDao
                .getAllEgdmCollectedReceipts(receiptNumber);
        if (egdmCollectedReceipts != null && !egdmCollectedReceipts.isEmpty())
            for (final EgdmCollectedReceipt egDmCollectedReceipt : egdmCollectedReceipts) {
                egDmCollectedReceipt.setStatus(DemandConstants.CANCELLED_RECEIPT);
            }
    }

    private BigDecimal reconcileCollForChequeBounce(final EgDemand demand, final BillReceiptInfo billRcptInfo) {

        /**
         * Deducts the collected amounts as per the amount of the bounced cheque, and also imposes a cheque-bounce penalty.
         */
        if (LOG.isDebugEnabled())
            LOG.debug("Updating collection for License Demand : {} with BillReceiptInfo - {} ", demand, billRcptInfo);
        BigDecimal totalCollChqBounced = getTotalChequeAmt(billRcptInfo);
        final BigDecimal chqBouncePenalty = licenseConfigurationService.chequeBouncePenalty();
        EgDemandDetails dmdDet;
        final EgDemandDetails penaltyDmdDet = getDemandDetail(demand, "CHEQUE BOUNCE PENALTY");
        if (penaltyDmdDet == null)
            dmdDet = insertPenalty(chqBouncePenalty, licenseUtils.getModule());
        else {
            BigDecimal existDmdDetAmt = penaltyDmdDet.getAmount();
            existDmdDetAmt = existDmdDetAmt == null || existDmdDetAmt.compareTo(ZERO) == 0 ? ZERO
                    : existDmdDetAmt;
            penaltyDmdDet.setAmount(existDmdDetAmt.add(chqBouncePenalty));
            dmdDet = penaltyDmdDet;
        }

        // setting this min amount into demand to check next payment should be
        // min of this amount with mode of payment cash or DD
        demand.setMinAmtPayable(totalCollChqBounced.add(chqBouncePenalty));
        demand.setAmtCollected(demand.getAmtCollected().subtract(billRcptInfo.getTotalAmount()));
        demand.setBaseDemand(demand.getBaseDemand().add(chqBouncePenalty));
        demand.setStatus('B');
        demand.addEgDemandDetails(dmdDet);
        totalCollChqBounced = updateDmdDetForChqBounce(demand, totalCollChqBounced);
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
                dd.setAmtCollected(ZERO);
                demand.setBaseDemand(demand.getBaseDemand().subtract(amtCollected));
            } else {
                dd.setAmtCollected(amtCollected.subtract(totalCollChqBounced));
                demand.setBaseDemand(demand.getBaseDemand().subtract(totalCollChqBounced));
                totalCollChqBounced = ZERO;

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
        for (BillReceiptInfo bri : billReceipts) {
            linkBillToReceipt(bri);
            updateBillDetails(bri);
            updateDemandDetails(bri);
        }
        return true;
    }

    private EgBill updateBill(final BillReceiptInfo bri, final EgBill egBill, final BigDecimal totalCollectedAmt) {
        if (bri != null) {
            for (final EgBillDetails billDet : egBill.getEgBillDetails()) {
                Boolean glCodeExist = false;
                for (final ReceiptAccountInfo acctDet : bri.getAccountDetails())
                    if (billDet.getGlcode().equals(acctDet.getGlCode())) {
                        glCodeExist = true;
                        BigDecimal amtCollected = billDet.getCollectedAmount();
                        if (amtCollected == null)
                            amtCollected = ZERO;
                        billDet.setCollectedAmount(acctDet.getCrAmount().subtract(amtCollected));
                        egBillDetailsDao.update(billDet);
                    }
                if (!glCodeExist)
                    throw new ApplicationRuntimeException("GlCode does not exist for " + billDet.getGlcode());
            }
            egBill.setTotalCollectedAmount(totalCollectedAmt);
            egBillDao.update(egBill);
        }
        return egBill;
    }

    private BigDecimal getEgBillDetailCollection(final EgBillDetails billdet) {
        BigDecimal collectedAmt = billdet.getCollectedAmount();
        if (billdet.getCollectedAmount() == null)
            collectedAmt = ZERO;
        return collectedAmt;

    }

    private EgBill updateBillForChqBounce(final EgBill egBill, final BigDecimal totalChqAmt) {
        final BigDecimal zeroVal = ZERO;
        if (totalChqAmt != null && totalChqAmt.compareTo(zeroVal) != 0 && egBill != null) {
            final List<EgBillDetails> billList = new ArrayList<>(egBill.getEgBillDetails());
            // Reversed the list because the knocking off the amount should
            // start from current Installment to least Installment.
            Collections.reverse(billList);
            BigDecimal carry = totalChqAmt;
            for (final EgBillDetails billdet : billList) {
                BigDecimal remAmount = ZERO;
                final BigDecimal balanceAmt = getEgBillDetailCollection(billdet);
                if (balanceAmt != null && balanceAmt.compareTo(zeroVal) > 0) {
                    if (carry.compareTo(zeroVal) > 0 && carry.subtract(balanceAmt).compareTo(zeroVal) > 0) {
                        carry = carry.subtract(balanceAmt);
                        remAmount = balanceAmt;
                    } else if (carry.compareTo(zeroVal) > 0 && carry.subtract(balanceAmt).compareTo(zeroVal) <= 0) {
                        remAmount = carry;
                        carry = ZERO;
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

    private void updateBillDetails(final BillReceiptInfo bri) {
        if (bri == null)
            throw new ApplicationRuntimeException(" Bill Receipt Info not found");
        EgBill egBill = egBillDao.findById(Long.valueOf(bri.getBillReferenceNum()), false);
        final List<EgBillDetails> billDetList = egBillDetailsDao.getBillDetailsByBill(egBill);

        if (bri.getEvent() != null && bri.getEvent().equals(BillingIntegrationService.EVENT_RECEIPT_CREATED)) {
            final BigDecimal totalCollectedAmt = calculateTotalCollectedAmt(bri, billDetList);
            updateBill(bri, egBill, totalCollectedAmt);
        } else if (bri.getEvent() != null && bri.getEvent().equals(BillingIntegrationService.EVENT_INSTRUMENT_BOUNCED))
            updateBillForChqBounce(egBill, getTotalChequeAmt(bri));
    }

    private BigDecimal getTotalChequeAmt(final BillReceiptInfo bri) {
        BigDecimal totalCollAmt = ZERO;
        try {
            for (final ReceiptInstrumentInfo rctInst : bri.getBouncedInstruments())
                if (rctInst.getInstrumentAmount() != null)
                    totalCollAmt = totalCollAmt.add(rctInst.getInstrumentAmount());
        } catch (final ApplicationRuntimeException e) {
            throw new ApplicationRuntimeException("Exception in calculate Total Collected Amt", e);
        }

        return totalCollAmt;
    }

    private BigDecimal calculateTotalCollectedAmt(final BillReceiptInfo bri, final List<EgBillDetails> billDetList) {
        BigDecimal totalCollAmt = ZERO;
        try {
            if (bri != null && billDetList != null)
                for (final EgBillDetails billDet : billDetList) {
                    Boolean glCodeExist = false;
                    for (final ReceiptAccountInfo acctDet : bri.getAccountDetails())
                        if (billDet.getGlcode().equals(acctDet.getGlCode()) && billDet.getDescription().equals(acctDet.getDescription())) {
                            glCodeExist = true;
                            totalCollAmt = totalCollAmt.add(acctDet.getCrAmount());
                        }
                    if (!glCodeExist)
                        throw new ApplicationRuntimeException("GlCode does not exist for " + billDet.getGlcode());
                }
        } catch (ApplicationRuntimeException e) {
            throw new ApplicationRuntimeException("Exception in calculate Total Collected Amt", e);
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

    private BillReceipt linkBillToReceipt(final BillReceiptInfo bri) {
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

    private EgdmCollectedReceipt persistCollectedReceipts(final EgDemandDetails egDemandDetails,
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
        if (chqBouncePenalty != null && chqBouncePenalty.compareTo(ZERO) > 0) {
            final Installment currInstallment = getCurrentInstallment(module);
            final EgDemandReasonMaster egDemandReasonMaster = demandGenericDao.getDemandReasonMasterByCode(
                    "CHQ_BUNC_PENALTY", module);
            if (egDemandReasonMaster == null)
                throw new ApplicationRuntimeException(" Penalty Demand reason Master is null in method  insertPenalty");
            final EgDemandReason egDemandReason = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(
                    egDemandReasonMaster, currInstallment, module);
            if (egDemandReason == null)
                throw new ApplicationRuntimeException(" Penalty Demand reason is null in method  insertPenalty ");
            demandDetail = EgDemandDetails.fromReasonAndAmounts(chqBouncePenalty, egDemandReason, ZERO);
        }
        return demandDetail;
    }

    private Installment getCurrentInstallment(final Module module) {
        return installmentDao.getInsatllmentByModuleForGivenDate(module, new Date());
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
        final ReceiptAmountInfo receiptAmountInfo = new ReceiptAmountInfo();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        final EgBill egBill = egBillDao.findById(Long.valueOf(billReceiptInfo.getBillReferenceNum()), false);
        final Set<EgBillDetails> billDetails = egBill.getEgBillDetails();
        BigDecimal currentInstallmentAmount = ZERO;
        BigDecimal arrearAmount = ZERO;
        BigDecimal latePaymentCharges = ZERO;
        final TradeLicense tradeLicense = licenseApplicationService.getLicenseByDemand(egBill.getEgDemand());
        Installment currentInstallment = tradeLicense.getDemand().getEgInstallmentMaster();
        List<String> feeTypes = feeTypeService.findAll().stream().map(FeeType::getName).collect(Collectors.toList());
        for (final EgBillDetails billdetail : billDetails)
            if (billdetail.getCrAmount() != null && billdetail.getCrAmount().compareTo(ZERO) > 0) {
                final String[] desc = billdetail.getDescription().split("-", 2);
                final String dmdReason = desc[0].trim();
                final String installment = desc[1].trim();
                if (feeTypes.contains(dmdReason) && installment.equals(currentInstallment.getDescription()))
                    currentInstallmentAmount = currentInstallmentAmount.add(billdetail.getCrAmount());
                else if (PENALTY_DMD_REASON_CODE.equals(dmdReason))
                    latePaymentCharges = latePaymentCharges.add(billdetail.getCrAmount());
                else
                    arrearAmount = arrearAmount.add(billdetail.getCrAmount());
            }
        List<EgBillDetails> filteredBillDetails = billDetails.stream().filter(bd -> bd.getEgDemandReason()
                .getEgDemandReasonMaster().getEgReasonCategory().getCode().equals(DEMAND_REASON_CATEGORY_FEE)).
                sorted(Comparator.comparing(EgBillDetails::getOrderNo)).collect(Collectors.toList());
        Integer billDetailsSize = filteredBillDetails.size();
        if (!filteredBillDetails.isEmpty() && filteredBillDetails.size() == 1) {
            receiptAmountInfo.setInstallmentFrom(formatter.format(filteredBillDetails.get(0).getInstallmentStartDate()));
            receiptAmountInfo.setInstallmentTo(formatter.format(filteredBillDetails.get(0).getInstallmentEndDate()));
        } else if (filteredBillDetails.size() > 1) {
            receiptAmountInfo.setInstallmentFrom(formatter.format(filteredBillDetails.get(0).getInstallmentStartDate()));
            receiptAmountInfo.setInstallmentTo(formatter.format(filteredBillDetails.get(billDetailsSize - 1).getInstallmentEndDate()));
        }

        String revenueWard = tradeLicense.getParentBoundary() == null ? "NA" : tradeLicense.getParentBoundary().getName();
        receiptAmountInfo.setArrearsAmount(arrearAmount);
        receiptAmountInfo.setCurrentInstallmentAmount(currentInstallmentAmount);
        receiptAmountInfo.setLatePaymentCharges(latePaymentCharges);
        receiptAmountInfo.setRevenueWard(revenueWard);
        return receiptAmountInfo;
    }

    public Map<String, Map<String, BigDecimal>> getPaymentFee(final TradeLicense license) {
        final Map<String, Map<String, BigDecimal>> outstandingFee = new LinkedHashMap<>();
        final EgDemand egDemand = license.getCurrentDemand();
        egDemand.getEgDemandDetails()
                .stream()
                .sorted(Comparator.comparing(EgDemandDetails::getInstallmentStartDate))
                .forEach(demandDetail -> {
                            Date fromDate = license.isNewApplication() ? license.getCommencementDate() : demandDetail.getEgDemandReason()
                                    .getEgInstallmentMaster().getFromDate();
                            String demandReason = demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster();
                            Installment installmentYear = demandDetail.getEgDemandReason().getEgInstallmentMaster();
                            Map<String, BigDecimal> feeByTypes;
                            if (!DEMAND_REASON_CATEGORY_PENALTY.equals(demandDetail.getReasonCategory())
                                    && demandDetail.getAmount().subtract(demandDetail.getAmtCollected()).signum() == 1) {
                                if (outstandingFee.containsKey(installmentYear.getDescription()))
                                    feeByTypes = outstandingFee.get(installmentYear.getDescription());
                                else {
                                    feeByTypes = new HashMap<>();
                                    feeByTypes.put(demandReason, ZERO);
                                }
                                BigDecimal demandAmount = demandDetail.getAmount().subtract(demandDetail.getAmtCollected());
                                feeByTypes.put(demandReason, demandAmount.setScale(0, RoundingMode.HALF_UP));
                                BigDecimal penaltyAmt = penaltyRatesService.calculatePenalty(license, fromDate, new Date(),
                                        demandDetail.getAmount());
                                if (penaltyAmt.compareTo(ZERO) > 0)
                                    feeByTypes.put(PENALTY_DMD_REASON_CODE, penaltyAmt.setScale(0, RoundingMode.HALF_UP));
                                outstandingFee.put(installmentYear.getDescription(), feeByTypes);
                            }
                        }
                );
        recalculatePenaltyAmt(outstandingFee, egDemand);
        return outstandingFee;
    }

    private void recalculatePenaltyAmt(Map<String, Map<String, BigDecimal>> outstandingFee, EgDemand egDemand) {
        for (final EgDemandDetails demandDetails : egDemand.getEgDemandDetails()) {
            final Installment installmentYear = demandDetails.getEgDemandReason().getEgInstallmentMaster();
            if (DEMAND_REASON_CATEGORY_PENALTY.equals(demandDetails.getReasonCategory())) {
                Map<String, BigDecimal> feeType = outstandingFee.get(installmentYear.getDescription());
                if (feeType != null && feeType.containsKey(PENALTY_DMD_REASON_CODE)) {
                    feeType.put(PENALTY_DMD_REASON_CODE, feeType.get(PENALTY_DMD_REASON_CODE)
                            .subtract(demandDetails.getAmtCollected()).setScale(0, RoundingMode.HALF_UP));
                    outstandingFee.put(installmentYear.getDescription(), feeType);
                }
            }
        }
    }
}