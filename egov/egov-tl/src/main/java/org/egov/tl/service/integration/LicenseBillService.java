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

import org.egov.InvalidAccountHeadException;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.BillReceiptInfoImpl;
import org.egov.collection.integration.models.ReceiptAccountInfo;
import org.egov.collection.integration.models.ReceiptAmountInfo;
import org.egov.collection.integration.models.ReceiptInstrumentInfo;
import org.egov.collection.integration.services.BillingIntegrationService;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.DemandGenericHibDao;
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
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
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
import org.egov.tl.service.TradeLicenseSmsAndEmailService;
import org.egov.tl.service.TradeLicenseUpdateIndexService;
import org.egov.tl.utils.Constants;
import org.egov.tl.utils.LicenseUtils;
import org.elasticsearch.common.joda.time.DateTime;
import org.hibernate.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

@Service
@Transactional(readOnly = true)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LicenseBillService extends BillServiceInterface implements BillingIntegrationService {
    private static final Logger LOG = LoggerFactory.getLogger(LicenseBillService.class);

    protected License license;
    public static final String TL_FUNCTION_CODE = "10151500";

    @Autowired
    private EgBillDetailsDao egBillDetailsDao;

    @Autowired
    private SimpleWorkflowService<License> transferWorkflowService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private EgBillReceiptDao egBillReceiptDao;

    @Autowired
    private AssignmentService assignmentService;

    private TradeLicenseUpdateIndexService updateIndexService;

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
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    private LicenseUtils licenseUtils;

    public void setLicense(final License license) {
        this.license = license;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<EgBillDetails> getBilldetails(final Billable billObj) {
        final List<EgBillDetails> billDetails = new ArrayList<EgBillDetails>();
        final LicenseBill billable = (LicenseBill) billObj;
        final EgDemand demand = billObj.getCurrentDemand();
        final Date currentDate = new Date();
        final Map installmentWise = new HashMap<Installment, List<EgDemandDetails>>();
        final Set<Installment> sortedInstallmentSet = new TreeSet<Installment>();
        final DemandComparatorByOrderId demandComparatorByOrderId = new DemandComparatorByOrderId();
        Module module = license.getTradeName() != null && license.getTradeName().getLicenseType() != null
                ? license.getTradeName().getLicenseType().getModule() : null;
        if (module == null)
            module = moduleService.getModuleByName(Constants.TRADELICENSE_MODULENAME);
        getCurrentInstallment(module);
        final List<EgDemandDetails> orderedDetailsList = new ArrayList<EgDemandDetails>();
        Map<Installment, BigDecimal> installmentPenalty = new HashMap<Installment, BigDecimal>();
        Map<Installment, EgDemandDetails> installmentWisePenaltyDemandDetail = new TreeMap<Installment, EgDemandDetails>();
        if ("New".equals(license.getLicenseAppType().getName()))
            installmentPenalty = billable.getCalculatedPenalty(license.getCommencementDate(), new Date(), demand);
        else if ("Renew".equals(license.getLicenseAppType().getName()))
            installmentPenalty = billable.getCalculatedPenalty(null, new Date(), demand);
        installmentWisePenaltyDemandDetail = getInstallmentWisePenaltyDemandDetails(license.getCurrentDemand());
        for (final Map.Entry<Installment, BigDecimal> penalty : installmentPenalty.entrySet()) {
            EgDemandDetails penaltyDemandDetail = null;
            if (penalty.getValue().signum() > 0) {
                penaltyDemandDetail = installmentWisePenaltyDemandDetail.get(penalty.getKey());
                if (penaltyDemandDetail != null)
                    penaltyDemandDetail.setAmount(penalty.getValue());
                else {
                    penaltyDemandDetail = insertPenaltyAndBillDetails(billDetails, billable, penalty.getValue(),
                            penalty.getKey());
                    if (penaltyDemandDetail != null) {
                        demand.getEgDemandDetails().add(penaltyDemandDetail);
                        demand.addBaseDemand(penaltyDemandDetail.getAmount());
                    }
                }
            }
        }
        for (final EgDemandDetails demandDetail : demand.getEgDemandDetails()) {
            final Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();
            if (installmentWise.get(installment) == null) {
                final List<EgDemandDetails> detailsList = new ArrayList<EgDemandDetails>();
                detailsList.add(demandDetail);
                installmentWise.put(demandDetail.getEgDemandReason().getEgInstallmentMaster(), detailsList);
                sortedInstallmentSet.add(installment);
            } else
                ((List<EgDemandDetails>) installmentWise.get(demandDetail.getEgDemandReason().getEgInstallmentMaster()))
                        .add(demandDetail);
        }
        for (final Installment i : sortedInstallmentSet) {
            final List<EgDemandDetails> installmentWiseDetails = (List<EgDemandDetails>) installmentWise.get(i);
            Collections.sort(installmentWiseDetails, demandComparatorByOrderId);
            orderedDetailsList.addAll(installmentWiseDetails);
        }

        int i = 1;
        for (final EgDemandDetails demandDetail : orderedDetailsList) {

            final EgDemandReason reason = demandDetail.getEgDemandReason();
            final Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();
            if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getIsDebit().equalsIgnoreCase("N")
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
                            billDetails.add(billdetailRebate);
                        }
                    }
                }
                if (demandDetail.getAmount() != null) {
                    billdetail.setDrAmount(BigDecimal.ZERO);
                    billdetail.setCrAmount(demandDetail.getAmount());
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
                // billdetail.setOrderNo(Integer.valueOf(demandDetail.getEgDemandReason().getEgDemandReasonMaster().getOrderId().toString()));
                billdetail.setOrderNo(i++);
                billdetail.setDescription(reason.getEgDemandReasonMaster().getReasonMaster() + " - "
                        + installment.getDescription());
                billdetail.setFunctionCode(TL_FUNCTION_CODE);
                billDetails.add(billdetail);
            }

        }
        if (LOGGER.isDebugEnabled())
            LOG.debug("created Bill Details");
        return billDetails;
    }

    public Map<Installment, EgDemandDetails> getInstallmentWisePenaltyDemandDetails(final EgDemand currentDemand) {
        final Map<Installment, EgDemandDetails> installmentWisePenaltyDemandDetails = new TreeMap<Installment, EgDemandDetails>();
        if (currentDemand == null) {
        } else
            for (final EgDemandDetails dmdDet : currentDemand.getEgDemandDetails())
                if (dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode().equals(Constants.PENALTY_DMD_REASON_CODE))
                    installmentWisePenaltyDemandDetails.put(dmdDet.getEgDemandReason().getEgInstallmentMaster(), dmdDet);

        return installmentWisePenaltyDemandDetails;
    }

    private EgDemandDetails insertPenaltyAndBillDetails(final List<EgBillDetails> billDetails, final LicenseBill billable,
            final BigDecimal penalty,
            final Installment installment) {

        EgDemandDetails insertPenDmdDetail = null;

        insertPenDmdDetail = insertPenaltyDmdDetail(installment, penalty);
        return insertPenDmdDetail;
    }

    private EgDemandDetails insertPenaltyDmdDetail(final Installment inst, final BigDecimal penaltyAmount) {
        EgDemandDetails demandDetail = null;
        if (penaltyAmount != null && penaltyAmount.compareTo(BigDecimal.ZERO) > 0) {
            final Module module = license.getTradeName().getLicenseType().getModule();
            final EgDemandReasonMaster egDemandReasonMaster = demandGenericDao.getDemandReasonMasterByCode(
                    Constants.PENALTY_DMD_REASON_CODE,
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
                final LicenseDemand ld = (LicenseDemand) persistenceService.find("from LicenseDemand where id=?",
                        demand.getId());
                ld.getLicense().getTradeName().getLicenseType().getModule();

                final Map<String, Map<String, EgDemandDetails>> installmentWiseDemandDetailsByReason = new HashMap<String, Map<String, EgDemandDetails>>();
                Map<String, EgDemandDetails> demandDetailByReason = null;

                EgDemandReason dmdRsn = null;
                String installmentDesc = null;

                for (final EgDemandDetails dmdDtls : demand.getEgDemandDetails())
                    if (dmdDtls.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                        dmdRsn = dmdDtls.getEgDemandReason();
                        installmentDesc = dmdRsn.getEgInstallmentMaster().getDescription();
                        demandDetailByReason = new HashMap<String, EgDemandDetails>();
                        if (installmentWiseDemandDetailsByReason.get(installmentDesc) == null) {
                            demandDetailByReason.put(dmdRsn.getEgDemandReasonMaster().getReasonMaster(), dmdDtls);
                            installmentWiseDemandDetailsByReason.put(installmentDesc, demandDetailByReason);
                        } else
                            installmentWiseDemandDetailsByReason.get(installmentDesc).put(
                                    dmdRsn.getEgDemandReasonMaster().getReasonMaster(), dmdDtls);
                    } else if (LOGGER.isDebugEnabled())
                        LOGGER.debug("saveCollectionDetails - demand detail amount is zero " + dmdDtls);

                EgDemandDetails demandDetail = null;

                for (final ReceiptAccountInfo rcptAccInfo : billReceipt.getAccountDetails())
                    if (rcptAccInfo.getDescription() != null && !rcptAccInfo.getDescription().isEmpty())
                        if (rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1) {
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

                final License license = ld.getLicense();
                if (license.getState() != null)
                    updateWorkflowState(license);
                tradeLicenseSmsAndEmailService.sendSMsAndEmailOnCollection(license, billReceipt.getReceiptDate(),
                        license.getCurrentLicenseFee());
                updateIndexService.updateTradeLicenseIndexes(license);
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
    public void updateWorkflowState(License licenseObj) {
        final Assignment wfInitiator = assignmentService.getPrimaryAssignmentForUser(licenseObj.getCreatedBy().getId());
        Position pos = wfInitiator.getPosition();
        final DateTime currentDate = new DateTime();
        final User user = securityUtils.getCurrentUser();
        final Boolean digitalSignEnabled = licenseUtils.isDigitalSignEnabled();
        WorkFlowMatrix wfmatrix = null;

        if (digitalSignEnabled) {
            licenseObj = licenseUtils.applicationStatusChange(licenseObj, Constants.APPLICATION_STATUS_DIGUPDATE_CODE);
            pos = licenseUtils.getCityLevelCommissioner();
            if (licenseObj.getLicenseAppType() != null
                    && licenseObj.getLicenseAppType().getName().equals(Constants.RENEWAL_LIC_APPTYPE)) {
                wfmatrix = transferWorkflowService.getWfMatrix("TradeLicense", null, null, "RENEWALTRADE",
                        Constants.WF_STATE_DIGITAL_SIGN_RENEWAL, null);
                licenseObj.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(Constants.WORKFLOW_STATE_COLLECTED)
                        .withStateValue(Constants.WF_STATE_DIGITAL_SIGN_RENEWAL).withDateInfo(currentDate.toDate())
                        .withOwner(pos).withNextAction(wfmatrix.getNextAction());
            } else {
                wfmatrix = transferWorkflowService.getWfMatrix("TradeLicense", null, null, null,
                        Constants.WF_STATE_DIGITAL_SIGN_NEWTL, null);
                licenseObj.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(Constants.WORKFLOW_STATE_COLLECTED)
                        .withStateValue(Constants.WF_STATE_DIGITAL_SIGN_NEWTL).withDateInfo(currentDate.toDate())
                        .withOwner(pos).withNextAction(wfmatrix.getNextAction());
            }
        } else {
            licenseObj = licenseUtils.applicationStatusChange(licenseObj, Constants.APPLICATION_STATUS_APPROVED_CODE);
            if (licenseObj.getLicenseAppType() != null
                    && licenseObj.getLicenseAppType().getName().equals(Constants.RENEWAL_LIC_APPTYPE))
                wfmatrix = transferWorkflowService.getWfMatrix("TradeLicense", null, null, "RENEWALTRADE",
                        Constants.WF_STATE_RENEWAL_COMM_APPROVED, null);
            else
                wfmatrix = transferWorkflowService.getWfMatrix("TradeLicense", null, null, null,
                        Constants.WF_STATE_COLLECTION_PENDING, null);
            licenseObj.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(Constants.WORKFLOW_STATE_COLLECTED)
                    .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                    .withNextAction(wfmatrix.getNextAction());
        }
    }

    /**
     * Deducts the collected amounts as per the amount of the cancelled receipt.
     */
    protected void reconcileCollForRcptCancel(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
        LOGGER.debug("updateDemandForCancellation : Updating Collection For Demand : Demand - " + demand
                + " with BillReceiptInfo - " + billRcptInfo);
        cancelBill(Long.valueOf(billRcptInfo.getBillReferenceNum()));
        demand.setAmtCollected(demand.getAmtCollected().subtract(billRcptInfo.getTotalAmount()));
        updateDmdDetForRcptCancel(demand, billRcptInfo);
        LOGGER.debug("updateDemandForCancellation : Updated Collection For Demand : " + demand);
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
            if (rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1
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
        final DemandGenericDao demandGenericDao = new DemandGenericHibDao();
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
        final BigDecimal chqBouncePenalty = Constants.CHQ_BOUNCE_PENALTY;
        // cancelBill(Long.valueOf(billRcptInfo.getBillReferenceNum()));
        EgDemandDetails dmdDet = null;
        final EgDemandDetails penaltyDmdDet = getDemandDetail(demand, getCurrentInstallment(ld.getLicense()
                .getTradeName().getLicenseType().getModule()), Constants.DEMANDRSN_STR_CHQ_BOUNCE_PENALTY);
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
        demand.setStatus(Constants.DMD_STATUS_CHEQUE_BOUNCED);
        demand.addEgDemandDetails(dmdDet);
        totalCollChqBounced = updateDmdDetForChqBounce(demand, billRcptInfo, totalCollChqBounced);
        LOGGER.debug("updateDemandForChequeBounce : Updated Collection For Demand : " + demand);
        return totalCollChqBounced;
    }

    /**
     * reverse the amount collected for each demand detail
     *
     * @param demand
     * @param c
     * @param totalCollChqBounced
     */

    private BigDecimal updateDmdDetForChqBounce(final EgDemand demand, final BillReceiptInfo c,
            BigDecimal totalCollChqBounced) {
        new TreeSet<EgDemandDetails>();
        List<EgDemandDetails> demandList = new ArrayList<EgDemandDetails>();
        demandList = (List<EgDemandDetails>) demand.getEgDemandDetails();
        Collections.sort(demandList, new DemandComparatorByOrderId());
        Collections.reverse(demandList);
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

    private EgDemandDetails getDemandDetail(final EgDemand demand, final Installment currentInstallment,
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
    public boolean updateNewReceipt(final Set<BillReceiptInfo> billReceipts) throws InvalidAccountHeadException,
            ObjectNotFoundException {
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

    public EgBill updateBillForChqBounce(final BillReceiptInfo bri, final EgBill egBill, final BigDecimal totalChqAmt) {
        final BigDecimal zeroVal = BigDecimal.ZERO;
        if (totalChqAmt != null && totalChqAmt.compareTo(zeroVal) != 0 && egBill != null) {
            final List<EgBillDetails> billList = new ArrayList<EgBillDetails>(egBill.getEgBillDetails());
            // Reversed the list because the knocking off the amount should
            // start from current Installment to least Installment.
            Collections.reverse(billList);
            BigDecimal carry = totalChqAmt;
            for (final EgBillDetails billdet : billList) {
                BigDecimal balanceAmt = BigDecimal.ZERO;
                BigDecimal remAmount = BigDecimal.ZERO;
                balanceAmt = getEgBillDetailCollection(billdet);
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
        EgBill egBill = null;
        if (bri == null)
            throw new ApplicationRuntimeException(" BillReceiptInfo Object is null ");
        egBill = egBillDao.findById(new Long(bri.getBillReferenceNum()), false);
        final List<EgBillDetails> billDetList = egBillDetailsDao.getBillDetailsByBill(egBill);

        if (bri.getEvent() != null && bri.getEvent().equals(BillingIntegrationService.EVENT_RECEIPT_CREATED)) {
            final BigDecimal totalCollectedAmt = calculateTotalCollectedAmt(bri, billDetList);
            egBill = updateBill(bri, egBill, totalCollectedAmt);
        } else if (bri.getEvent() != null && bri.getEvent().equals(BillingIntegrationService.EVENT_INSTRUMENT_BOUNCED))
            egBill = updateBillForChqBounce(bri, egBill, getTotalChequeAmt(bri));
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

    private BillReceipt linkBillToReceipt(final BillReceiptInfo bri) throws InvalidAccountHeadException,
            ObjectNotFoundException {
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
        BillReceipt billRecpt = null;
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
    }

    /**
     * Method used to insert penalty in EgDemandDetail table. Penalty Amount will be calculated depending upon the cheque Amount.
     *
     * @see createDemandDetails() -- EgDemand Details are created
     * @see getPenaltyAmount() --Penalty Amount is calculated
     * @param chqBouncePenalty
     * @return New EgDemandDetails Object
     */
    EgDemandDetails insertPenalty(final BigDecimal chqBouncePenalty, final Module module) {
        EgDemandDetails demandDetail = null;
        if (chqBouncePenalty != null && chqBouncePenalty.compareTo(BigDecimal.ZERO) > 0) {
            final DemandGenericDao demandGenericDao = new DemandGenericHibDao();
            final Installment currInstallment = getCurrentInstallment(module);
            final EgDemandReasonMaster egDemandReasonMaster = demandGenericDao.getDemandReasonMasterByCode(
                    Constants.DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY, module);
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

    }

    @Override
    public List<ReceiptDetail> reconstructReceiptDetail(final String billReferenceNumber,
            final BigDecimal actualAmountPaid, final List<ReceiptDetail> receiptDetailList) {
        // TODO Auto-generated method stub
        return null;
    }

    public void setLicenseUtils(final LicenseUtils licenseUtils) {
        this.licenseUtils = licenseUtils;
    }

    @Override
    public String constructAdditionalInfoForReceipt(final BillReceiptInfo billReceiptInfo) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ReceiptAmountInfo receiptAmountBifurcation(final BillReceiptInfo billReceiptInfo) {
        return new ReceiptAmountInfo();
    }

    public LicenseUtils getLicenseUtils() {
        return licenseUtils;
    }

    public TradeLicenseUpdateIndexService getUpdateIndexService() {
        return updateIndexService;
    }

    public void setUpdateIndexService(final TradeLicenseUpdateIndexService updateIndexService) {
        this.updateIndexService = updateIndexService;
    }

    public TradeLicenseSmsAndEmailService getTradeLicenseSmsAndEmailService() {
        return tradeLicenseSmsAndEmailService;
    }

    public void setTradeLicenseSmsAndEmailService(final TradeLicenseSmsAndEmailService tradeLicenseSmsAndEmailService) {
        this.tradeLicenseSmsAndEmailService = tradeLicenseSmsAndEmailService;
    }

}