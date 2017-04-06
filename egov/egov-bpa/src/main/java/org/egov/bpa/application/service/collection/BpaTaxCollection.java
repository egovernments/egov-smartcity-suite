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
package org.egov.bpa.application.service.collection;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.bpa.application.entity.BpaApplication;
import org.egov.bpa.application.entity.CollectionApportioner;
import org.egov.bpa.application.service.ApplicationBpaBillService;
import org.egov.bpa.application.service.ApplicationBpaService;
import org.egov.bpa.application.workflow.BpaApplicationWorkflowCustomDefaultImpl;
import org.egov.bpa.service.BpaUtils;
import org.egov.bpa.utils.BpaConstants;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.BillReceiptInfoImpl;
import org.egov.collection.integration.models.ReceiptAccountInfo;
import org.egov.collection.integration.models.ReceiptAmountInfo;
import org.egov.collection.integration.models.ReceiptInstrumentInfo;
import org.egov.collection.integration.services.CollectionIntegrationService;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.integration.TaxCollection;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BpaTaxCollection extends TaxCollection {
    private static final Logger LOGGER = Logger.getLogger(BpaTaxCollection.class);
    @Autowired
    private EgBillDao egBillDAO;
    @Autowired
    private ModuleService moduleService;
    
    @Autowired
    private BpaUtils bpaUtils;

    @Autowired
    private ApplicationBpaService applicationBpaService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<BpaApplication> waterConnectionWorkflowService;

    @Autowired
    private ApplicationBpaBillService applicationBpaBillService;

    @Autowired
    private CollectionIntegrationService collectionService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private FinancialYearDAO financialYearDAO;

    @Autowired
    private FunctionHibernateDAO functionDAO;

    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsDAO;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    @Transactional
    public void updateDemandDetails(final BillReceiptInfo billRcptInfo) {
        final BigDecimal totalAmount = billRcptInfo.getTotalAmount();
        final EgDemand demand = getCurrentDemand(Long.valueOf(billRcptInfo.getBillReferenceNum()));
        final String indexNo = ((BillReceiptInfoImpl) billRcptInfo).getReceiptMisc().getReceiptHeader()
                .getConsumerCode();
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("updateDemandDetails : Demand before proceeding : " + demand);
                LOGGER.debug("updateDemandDetails : collection back update started for property : " + indexNo
                        + " and receipt event is " + billRcptInfo.getEvent() + ". Total Receipt amount is."
                        + totalAmount + " with receipt no." + billRcptInfo.getReceiptNum());
            }

            if (billRcptInfo.getEvent().equals(EVENT_RECEIPT_CREATED)) {
                updateCollForRcptCreate(demand, billRcptInfo, totalAmount);
                updateBpaApplication(demand);

            } else if (billRcptInfo.getEvent().equals(EVENT_RECEIPT_CANCELLED))
                updateCollectionForRcptCancel(demand, billRcptInfo);
            else if (billRcptInfo.getEvent().equals(EVENT_INSTRUMENT_BOUNCED))
                updateCollForChequeBounce(demand, billRcptInfo);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("updateDemandDetails : Demand after processed : " + demand);
        } catch (final Exception e) {

            throw new ApplicationRuntimeException("Error occured during back update of DCB : " + e.getMessage(), e);
        }
    }

    private void updateCollForChequeBounce(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
        cancelBill(Long.valueOf(billRcptInfo.getBillReferenceNum()));
        demand.setStatus(BpaConstants.DMD_STATUS_CHEQUE_BOUNCED);
        updateDmdDetForRcptCancelAndCheckBounce(demand, billRcptInfo);
        LOGGER.debug("reconcileCollForChequeBounce : Updating Collection finished For Demand : " + demand);
    }

    @Transactional
    public void updateDmdDetForRcptCancelAndCheckBounce(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
        LOGGER.debug("Entering method updateDmdDetForRcptCancelAndCheckBounce");
        String installment;
        for (final ReceiptAccountInfo rcptAccInfo : billRcptInfo.getAccountDetails())
            if (rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1
                    && !rcptAccInfo.getIsRevenueAccount()) {
                final String[] desc = rcptAccInfo.getDescription().split("-", 2);
                final String reason = desc[0].trim();
                final String[] installsplit = desc[1].split("#");
                installment = installsplit[0].trim();
                for (final EgDemandDetails demandDetail : demand.getEgDemandDetails())
                    if (reason.equalsIgnoreCase(
                            demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster())
                            && installment.equalsIgnoreCase(
                                    demandDetail.getEgDemandReason().getEgInstallmentMaster().getDescription())) {
                        for (final ReceiptInstrumentInfo instrumentHeader : billRcptInfo.getInstrumentDetails()) {
                            if (instrumentHeader != null) {
                                demandDetail.setAmtCollected(demandDetail.getAmtCollected()
                                        .subtract(instrumentHeader.getInstrumentAmount()));
                                if (demand.getAmtCollected() != null
                                        && demand.getAmtCollected().compareTo(BigDecimal.ZERO) > 0
                                        && demandDetail.getEgDemandReason().getEgDemandReasonMaster().getIsDemand())
                                    demand.setAmtCollected(
                                            demand.getAmtCollected().subtract(instrumentHeader.getInstrumentAmount()));
                            }
                            LOGGER.info("Deducted Collected amount Rs." + rcptAccInfo.getCrAmount() + " for tax : "
                                    + reason + " and installment : " + installment);
                            break;
                        }
                        break;
                    }
                break;
            }

        updateReceiptStatusWhenCancelled(billRcptInfo.getReceiptNum());
        LOGGER.debug("Exiting method updateDmdDetForRcptCancelAndCheckBounce");
    }

    /**
     * @param demand Updates WaterConnectionDetails Object once Collection Is done. send Record move to Commissioner and Send SMS
     * and Email after Collection
     */
    @Transactional
    public void updateBpaApplication(final EgDemand demand) {
        final BpaApplication application = applicationBpaService
                .getApplicationByDemand(demand);
           Long approvalPosition ;
           WorkFlowMatrix  wfmatrix = bpaUtils.getWfMatrixByCurrentState(application, BpaConstants.WF_NEW_STATE);

            final BpaApplicationWorkflowCustomDefaultImpl applicationWorkflowCustomDefaultImpl = bpaUtils
                    .getInitialisedWorkFlowBean();
            approvalPosition = bpaUtils.getUserPositionByZone(wfmatrix.getNextDesignation(),application.getSiteDetail().get(0)!=null ? application.getSiteDetail().get(0).getAdminBoundary():null);
            applicationWorkflowCustomDefaultImpl.createCommonWorkflowTransition(application,
                    approvalPosition,"BPA Admission fees collected",
                    BpaConstants.CREATE_ADDITIONAL_RULE_CREATE, null);
        // update status and initialize workflow
        applicationBpaService.saveAndFlushApplication(application);

    }

    @Transactional
    public void updateCollForRcptCreate(final EgDemand demand, final BillReceiptInfo billRcptInfo,
            final BigDecimal totalAmount) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("updateCollForRcptCreate : Updating Collection Started For Demand : " + demand
                    + " with BillReceiptInfo - " + billRcptInfo);
        try {
            updateDemandDetailForReceiptCreate(billRcptInfo.getAccountDetails(), demand, billRcptInfo, totalAmount);
        } catch (final Exception e) {

            throw new ApplicationRuntimeException(
                    "Error occured during back update of DCB : updateCollForRcptCreate() " + e.getMessage(), e);
        }
    }

    @Transactional
    public void updateDemandDetailForReceiptCreate(final Set<ReceiptAccountInfo> accountDetails, final EgDemand demand,
            final BillReceiptInfo billRcptInfo, final BigDecimal totalAmount) {
        final StringBuilder query = new StringBuilder(
                "select dmdet FROM EgDemandDetails dmdet left join fetch dmdet.egDemandReason dmdRsn ")
                        .append("left join fetch dmdRsn.egDemandReasonMaster dmdRsnMstr left join fetch dmdRsn.egInstallmentMaster installment ")
                        .append("WHERE dmdet.egDemand.id = :demand");
        final List<EgDemandDetails> demandDetailList = getCurrentSession().createQuery(query.toString())
                .setLong("demand", demand.getId()).list();
        final Map<String, Map<String, EgDemandDetails>> installmentWiseDemandDetailsByReason = new HashMap<String, Map<String, EgDemandDetails>>();
        Map<String, EgDemandDetails> demandDetailByReason;
        EgDemandReason dmdRsn;
        String installmentDesc;

        for (final EgDemandDetails dmdDtls : demandDetailList) {
            dmdRsn = dmdDtls.getEgDemandReason();
            installmentDesc = dmdRsn.getEgInstallmentMaster().getDescription();
            demandDetailByReason = new HashMap<>(0);
            if (installmentWiseDemandDetailsByReason.get(installmentDesc) == null) {
                demandDetailByReason.put(dmdRsn.getEgDemandReasonMaster().getReasonMaster(), dmdDtls);
                installmentWiseDemandDetailsByReason.put(installmentDesc, demandDetailByReason);
            } else
                installmentWiseDemandDetailsByReason.get(installmentDesc)
                        .put(dmdRsn.getEgDemandReasonMaster().getReasonMaster(), dmdDtls);
        }

        EgDemandDetails demandDetail;

        for (final ReceiptAccountInfo rcptAccInfo : accountDetails)
            if (rcptAccInfo.getDescription() != null && !rcptAccInfo.getDescription().isEmpty())
                if (rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1) {
                    final String[] desc = rcptAccInfo.getDescription().split("-", 2);
                    final String[] installsplit = desc[1].split("#");
                    final String reason = desc[0].trim();
                    final String instDesc = installsplit[0].trim();
                    demandDetail = installmentWiseDemandDetailsByReason.get(instDesc).get(reason);
                    demandDetail.addCollectedWithOnePaisaTolerance(rcptAccInfo.getCrAmount());
                    if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getIsDemand())
                        demand.addCollected(rcptAccInfo.getCrAmount());

                    persistCollectedReceipts(demandDetail, billRcptInfo.getReceiptNum(), totalAmount,
                            billRcptInfo.getReceiptDate(), demandDetail.getAmtCollected());
                }

    }

    public EgDemandDetails createDemandDetails(final EgDemandReason egDemandReason, final BigDecimal amtCollected,
            final BigDecimal dmdAmount) {
        return EgDemandDetails.fromReasonAndAmounts(dmdAmount, egDemandReason, amtCollected);
    }

    @Override
    protected Module module() {
        return moduleService.getModuleByName(BpaConstants.EGMODULE_NAME);
    }

    public EgDemand getCurrentDemand(final Long billId) {
        final EgBill egBill = egBillDAO.findById(billId, false);
        final BpaApplication application = null;
        EgDemand demand;
        if (egBill.getEgDemand() != null && egBill.getEgDemand().getIsHistory() != null
                && egBill.getEgDemand().getIsHistory().equals(BpaConstants.DEMANDISHISTORY))
            demand = egBill.getEgDemand();
        else
            demand = application.getDemand();
        return demand;
    }

    // Receipt cancellation ,updating bill,demanddetails,demand
    @Transactional
    public void updateCollectionForRcptCancel(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
        LOGGER.debug("reconcileCollForRcptCancel : Updating Collection Started For Demand : " + demand
                + " with BillReceiptInfo - " + billRcptInfo);
        try {
            cancelBill(Long.valueOf(billRcptInfo.getBillReferenceNum()));
            updateDmdDetForRcptCancel(demand, billRcptInfo);
            LOGGER.debug("reconcileCollForRcptCancel : Updating Collection finished For Demand : " + demand);
        } catch (final Exception e) {

            throw new ApplicationRuntimeException("Error occured during back update of DCB : " + e.getMessage(), e);
        }
    }

    @Transactional
    public void cancelBill(final Long billId) {
        if (billId != null) {
            final EgBill egBill = egBillDAO.findById(billId, false);
            egBill.setIs_Cancelled("Y");
        }
    }

    @Transactional
    public void updateDmdDetForRcptCancel(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
        String installment;
        for (final ReceiptAccountInfo rcptAccInfo : billRcptInfo.getAccountDetails())
            if (rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1
                    && !rcptAccInfo.getIsRevenueAccount()) {

                final String[] desc = rcptAccInfo.getDescription().split("-", 2);
                final String reason = desc[0].trim();
                final String[] installsplit = desc[1].split("#");
                installment = installsplit[0].trim();

                for (final EgDemandDetails demandDetail : demand.getEgDemandDetails())
                    if (reason.equalsIgnoreCase(
                            demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster())
                            && installment.equalsIgnoreCase(
                                    demandDetail.getEgDemandReason().getEgInstallmentMaster().getDescription())) {
                        if (demandDetail.getAmtCollected().compareTo(rcptAccInfo.getCrAmount()) < 0)
                            throw new ApplicationRuntimeException(
                                    "updateDmdDetForRcptCancel : Exception while updating cancel receipt, "
                                            + "to be deducted amount " + rcptAccInfo.getCrAmount()
                                            + " is greater than the collected amount " + demandDetail.getAmtCollected()
                                            + " for demandDetail " + demandDetail);

                        demandDetail
                                .setAmtCollected(demandDetail.getAmtCollected().subtract(rcptAccInfo.getCrAmount()));
                        if (demand.getAmtCollected() != null && demand.getAmtCollected().compareTo(BigDecimal.ZERO) > 0
                                && demandDetail.getEgDemandReason().getEgDemandReasonMaster().getIsDemand())
                            demand.setAmtCollected(demand.getAmtCollected().subtract(rcptAccInfo.getCrAmount()));

                        LOGGER.info("Deducted Collected amount Rs." + rcptAccInfo.getCrAmount() + " for tax : " + reason
                                + " and installment : " + installment);
                    }
            }
        updateReceiptStatusWhenCancelled(billRcptInfo.getReceiptNum());
        LOGGER.debug("Exiting method updateDmdDetForRcptCancel");
    }

    @Override
    public List<ReceiptDetail> reconstructReceiptDetail(final String billReferenceNumber,
            final BigDecimal actualAmountPaid, final List<ReceiptDetail> receiptDetailList) {
        final Long billID = Long.valueOf(billReferenceNumber);
        final List<EgBillDetails> billDetails = new ArrayList<>(0);
        final EgBill bill = applicationBpaBillService.updateBillWithLatest(billID);
        LOGGER.debug("Reconstruct consumer code :" + bill.getConsumerId() + ", with bill reference number: "
                + billReferenceNumber + ", for Amount Paid :" + actualAmountPaid);
        final CollectionApportioner apportioner = new CollectionApportioner();
        billDetails.addAll(bill.getEgBillDetails());
        return apportioner.reConstruct(actualAmountPaid, billDetails, functionDAO, chartOfAccountsDAO);
    }

    @Override
    public String constructAdditionalInfoForReceipt(final BillReceiptInfo billReceiptInfo) {
        StringBuilder additionalInfo = new StringBuilder("Paid From");
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        final EgBill egBill = egBillDAO.findById(Long.valueOf(billReceiptInfo.getBillReferenceNum()), false);
        final BigDecimal amounttobeCalc = egBill.getTotalAmount().subtract(egBill.getTotalCollectedAmount());
        final List<ReceiptDetail> reciptDetailList = collectionService
                .getReceiptDetailListByReceiptNumber(billReceiptInfo.getReceiptNum());

        for (final EgBillDetails billDet : egBill.getEgBillDetails()) {
            if (billDet.getOrderNo() == 1) {
                additionalInfo.append(" ")
                        .append(formatter.format(billDet.getEgDemandReason().getEgInstallmentMaster().getFromDate()))
                        .append(" To ");
                if (egBill.getEgBillDetails().size() == 1) {
                    additionalInfo
                            .append(formatter.format(billDet.getEgDemandReason().getEgInstallmentMaster().getToDate()));
                    break;
                }

            }
            if (egBill.getEgBillDetails().size() > 1 && billDet.getCrAmount().compareTo(BigDecimal.ZERO) == 1
                    && reciptDetailList.get(0).getOrdernumber().equals(Long.valueOf(billDet.getOrderNo()))) {
                additionalInfo
                        .append(formatter.format(billDet.getEgDemandReason().getEgInstallmentMaster().getToDate()));
                break;
            }
        }
        if (amounttobeCalc.compareTo(BigDecimal.ZERO) == 1)
            additionalInfo = additionalInfo.append(" (Partialy)");

        return additionalInfo.toString();
    }

    @Override
    public ReceiptAmountInfo receiptAmountBifurcation(final BillReceiptInfo billReceiptInfo) {
        final ReceiptAmountInfo receiptAmountInfo = new ReceiptAmountInfo();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        final EgBill egBill = egBillDAO.findById(Long.valueOf(billReceiptInfo.getBillReferenceNum()), false);
        final List<EgBillDetails> billDetails = new ArrayList<>(egBill.getEgBillDetails());
        financialYearDAO.getFinancialYearByDate(new Date());
        BigDecimal currentInstallmentAmount = BigDecimal.ZERO;
        final BigDecimal advanceInstallmentAmount = BigDecimal.ZERO;
        final BigDecimal arrearAmount = BigDecimal.ZERO;
        applicationBpaService
                .getApplicationByDemand(egBill.getEgDemand());
        final List<ReceiptDetail> reciptDetailList = collectionService
                .getReceiptDetailListByReceiptNumber(billReceiptInfo.getReceiptNum());
        for (final ReceiptAccountInfo rcptAccInfo : billReceiptInfo.getAccountDetails())
            if (rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1)
                currentInstallmentAmount = currentInstallmentAmount.add(rcptAccInfo.getCrAmount());

        for (final EgBillDetails billDet : egBill.getEgBillDetails()) {
            if (billDet.getOrderNo() == 1) {
                receiptAmountInfo.setInstallmentFrom(
                        formatter.format(billDet.getEgDemandReason().getEgInstallmentMaster().getFromDate()));
                if (billDetails.size() == 1) {
                    receiptAmountInfo.setInstallmentTo(
                            formatter.format(billDet.getEgDemandReason().getEgInstallmentMaster().getToDate()));
                    break;
                }

            }
            if (egBill.getEgBillDetails().size() > 1 && billDet.getCrAmount().compareTo(BigDecimal.ZERO) == 1
                    && reciptDetailList.get(0).getOrdernumber().equals(Long.valueOf(billDet.getOrderNo()))) {
                receiptAmountInfo.setInstallmentTo(
                        formatter.format(billDet.getEgDemandReason().getEgInstallmentMaster().getToDate()));
                break;
            }
        }
        final String revenueWard = "Election Ward 1";
        receiptAmountInfo.setArrearsAmount(arrearAmount);
        receiptAmountInfo.setAdvanceAmount(advanceInstallmentAmount);
        receiptAmountInfo.setCurrentInstallmentAmount(currentInstallmentAmount);
        receiptAmountInfo.setRevenueWard(revenueWard);
        return receiptAmountInfo;
    }

}