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

package org.egov.stms.transactions.service.collection;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.BillReceiptInfoImpl;
import org.egov.collection.integration.models.ReceiptAccountInfo;
import org.egov.collection.integration.models.ReceiptAmountInfo;
import org.egov.commons.Installment;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.integration.TaxCollection;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.stms.masters.entity.enums.SewerageConnectionStatus;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageDemandConnection;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageDemandConnectionService;
import org.egov.stms.transactions.service.SewerageDemandService;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SewerageTaxCollection extends TaxCollection {
    private static final Logger LOGGER = Logger.getLogger(SewerageTaxCollection.class);

    @Autowired
    private EgBillDao egBillDAO;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    private SewerageDemandService sewerageDemandService;
    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;

    @Autowired
    private SewerageDemandConnectionService sewerageDemandConnectionService;

    @Override
    public List<ReceiptDetail> reconstructReceiptDetail(final String billReferenceNumber,
            final BigDecimal actualAmountPaid, final List<ReceiptDetail> receiptDetailList) {

        return null;
    }

    /**
     * Calling common api to update demand.
     */
    @Override
    @Transactional
    public void updateDemandDetails(final BillReceiptInfo billRcptInfo) {
        final BigDecimal totalAmount = billRcptInfo.getTotalAmount();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("updateDemandDetails : Demand updation for advertisement started. ");

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
            updateDemandWithcollectdTaxDetails(demand, billRcptInfo, EVENT_INSTRUMENT_BOUNCED, totalAmount);
        else if (billRcptInfo.getEvent().equals(EVENT_RECEIPT_CREATED))
            updateDemandWithcollectdTaxDetails(demand, billRcptInfo, EVENT_RECEIPT_CREATED, totalAmount);
        else if (billRcptInfo.getEvent().equals(EVENT_RECEIPT_CANCELLED))
            updateDemandWithcollectdTaxDetails(demand, billRcptInfo, EVENT_RECEIPT_CANCELLED, totalAmount);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("updateDemandDetails : Demand updation processed. ");

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

        for (final ReceiptAccountInfo recAccInfo : billReceiptInfo.getAccountDetails()) {
            String demandMasterReasonDesc = null;
            String financialYearDesc = null;
            if (recAccInfo.getDescription() != null) {
                demandMasterReasonDesc = recAccInfo
                        .getDescription()
                        .substring(
                                0,
                                recAccInfo.getDescription().indexOf(
                                        SewerageTaxConstants.COLL_RECEIPTDETAIL_DESC_PREFIX))
                        .trim();
                financialYearDesc = recAccInfo
                        .getDescription()
                        .substring(
                                recAccInfo.getDescription().indexOf(
                                        SewerageTaxConstants.COLL_RECEIPTDETAIL_DESC_PREFIX)
                                        + SewerageTaxConstants.COLL_RECEIPTDETAIL_DESC_PREFIX.length())
                        .trim();

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
        } else if (eventType.equals(EVENT_INSTRUMENT_BOUNCED)) {// TODO ADD check bounce penalty, if required in future.
            cancelBill(Long.valueOf(billReceiptInfo.getBillReferenceNum()));
            demand.setAmtCollected(demand.getAmtCollected().subtract(totalAmount));
            updateDmdDetForRcptCancel(demand, billReceiptInfo);
        }
        demand.setModifiedDate(new Date());
        return totalAmountCollected;
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

        for (final ReceiptAccountInfo rcptAccInfo : billRcptInfo.getAccountDetails())
            if (rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1
                    && !rcptAccInfo.getIsRevenueAccount())
                if (rcptAccInfo.getDescription() != null) {
                    demandMasterReasonDesc = rcptAccInfo
                            .getDescription().substring(0, rcptAccInfo.getDescription().indexOf(
                                    SewerageTaxConstants.COLL_RECEIPTDETAIL_DESC_PREFIX))
                            .trim();
                    financialYearDesc = rcptAccInfo.getDescription().substring(
                            rcptAccInfo.getDescription().indexOf(
                                    SewerageTaxConstants.COLL_RECEIPTDETAIL_DESC_PREFIX)
                                    + SewerageTaxConstants.COLL_RECEIPTDETAIL_DESC_PREFIX.length())
                            .trim();
                    for (final EgDemandDetails demandDetail : demand.getEgDemandDetails())
                        if (demandMasterReasonDesc.equalsIgnoreCase(demandDetail.getEgDemandReason()
                                .getEgDemandReasonMaster().getReasonMaster()) && financialYearDesc != null &&
                                demandDetail.getEgDemandReason()
                                        .getEgInstallmentMaster().getDescription().equalsIgnoreCase(financialYearDesc)) {
                            if (demandDetail.getAmtCollected().compareTo(rcptAccInfo.getCrAmount()) < 0)
                                throw new ApplicationRuntimeException(
                                        "updateDmdDetForRcptCancel : Exception while updating cancel receipt, "
                                                + "to be deducted amount " + rcptAccInfo.getCrAmount()
                                                + " is greater than the collected amount " + demandDetail.getAmtCollected()
                                                + " for demandDetail " + demandDetail);

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
                        && financialYearDesc
                                .equalsIgnoreCase(demandDetail.getEgDemandReason().getEgInstallmentMaster().getDescription())) {

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
                final EgDemandDetails demandDetail = sewerageDemandService.createDemandDetails(recAccInfo
                        .getCrAmount(), sewerageDemandService.getDemandReasonByCodeAndInstallment(
                                demandMasterReasonDesc,
                                sewerageDemandService.getInstallmentByDescription(financialYearDesc).getId()),
                        recAccInfo
                                .getCrAmount());
                demand.addEgDemandDetails(demandDetail);
                getCurrentSession().flush();
                persistCollectedReceipts(demandDetail, billReceiptInfo.getReceiptNum(), totalAmount,
                        billReceiptInfo.getReceiptDate(), recAccInfo.getCrAmount());

            }
            demand.setModifiedDate(new Date());
        }
        updateWorkflowState(demand);
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
            final SewerageApplicationDetails sewerageApplicationDetails = sewerageDemandConnectionService
                    .getSewerageDemandConnectionByDemand(demand).getApplicationDetails();

            if (sewerageApplicationDetails != null
                    && sewerageApplicationDetails.getStatus() != null) {

                if (sewerageApplicationDetails.getStatus().getCode()
                        .equalsIgnoreCase(SewerageTaxConstants.APPLICATION_STATUS_COLLECTINSPECTIONFEE)) {
                    sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                            SewerageTaxConstants.APPLICATION_STATUS_INSPECTIONFEEPAID, SewerageTaxConstants.MODULETYPE));
                    if (sewerageApplicationDetails.getState() != null)
                        sewerageApplicationDetailsService.updateStateTransition(sewerageApplicationDetails,
                                sewerageApplicationDetails.getState().getOwnerPosition().getId(),
                                SewerageTaxConstants.COLLECTION_REMARKS,
                                sewerageApplicationDetails.getApplicationType().getCode(),
                                SewerageTaxConstants.WFLOW_ACTION_STEP_FORWARD);
                } else if (sewerageApplicationDetails.getStatus().getCode()
                        .equalsIgnoreCase(SewerageTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN))
                    sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                            SewerageTaxConstants.APPLICATION_STATUS_FEEPAID, SewerageTaxConstants.MODULETYPE));
                sewerageApplicationDetailsService.save(sewerageApplicationDetails);
                if(sewerageApplicationDetailsService.getPortalInbox(sewerageApplicationDetails.getApplicationNumber()) != null)
                    sewerageApplicationDetailsService.updatePortalMessage(sewerageApplicationDetails);

                sewerageApplicationDetailsService.updateIndexes(sewerageApplicationDetails);
                // Sms and email not sending after doing demand collection,later must be fix

                /*
                 * if(sewerageApplicationDetails.getStatus().getCode().equals( SewerageTaxConstants.APPLICATION_STATUS_FEEPAID)){
                 * try{ sewerageConnectionSmsAndEmailService.sendSmsAndEmail(sewerageApplicationDetails,
                 * ServletActionContext.getRequest()); } catch (final Exception e) { final String errMsg =
                 * "Exception while sending sms and email!"; LOGGER.error(errMsg, e); //throw new
                 * ApplicationRuntimeException(errMsg, e); } }
                 */
            }
        }
    }

    @Override
    public ReceiptAmountInfo receiptAmountBifurcation(final BillReceiptInfo billReceiptInfo) {
        final ReceiptAmountInfo receiptAmountInfo = new ReceiptAmountInfo();

        BigDecimal currentInstallmentAmount = BigDecimal.ZERO;
        BigDecimal advanceInstallmentAmount = BigDecimal.ZERO;
        BigDecimal arrearAmount = BigDecimal.ZERO;

        if (billReceiptInfo != null && billReceiptInfo.getBillReferenceNum() != null) {
            final EgBill egBill = egBillDAO.findById(Long.valueOf(billReceiptInfo.getBillReferenceNum()), false);

            if (egBill != null) {
                final Installment currentInstallment = sewerageDemandService.getCurrentInstallment();
                final Installment nextInstallment = sewerageDemandService.getNextInstallment();

                final SewerageDemandConnection sewerageDemandConnection = sewerageDemandConnectionService
                        .getSewerageDemandConnectionByDemand(egBill.getEgDemand());

                for (final ReceiptAccountInfo rcptAccInfo : billReceiptInfo.getAccountDetails())
                    if (rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1) {

                        final String installment = rcptAccInfo
                                .getDescription()
                                .substring(
                                        rcptAccInfo.getDescription().indexOf(
                                                SewerageTaxConstants.COLL_RECEIPTDETAIL_DESC_PREFIX)
                                                + SewerageTaxConstants.COLL_RECEIPTDETAIL_DESC_PREFIX.length())
                                .trim();

                        if (sewerageDemandConnection != null && sewerageDemandConnection.getApplicationDetails()
                                .getConnection().getStatus().equals(SewerageConnectionStatus.INPROGRESS))
                            if (installment.equals(currentInstallment.toString()))
                                currentInstallmentAmount = currentInstallmentAmount.add(rcptAccInfo.getCrAmount());
                            else if (nextInstallment != null && installment.equals(nextInstallment.toString()))
                                advanceInstallmentAmount = advanceInstallmentAmount.add(rcptAccInfo.getCrAmount());
                            else
                                arrearAmount = arrearAmount.add(rcptAccInfo.getCrAmount());
                    }
            }
        }
        receiptAmountInfo.setArrearsAmount(arrearAmount);
        receiptAmountInfo.setAdvanceAmount(advanceInstallmentAmount);
        receiptAmountInfo.setCurrentInstallmentAmount(currentInstallmentAmount);
        return receiptAmountInfo;
    }

    @Override
    @Transactional
    public void apportionCollection(final String billRefNo, final BigDecimal amtPaid,
            final List<ReceiptDetail> receiptDetails) {

        final SewerageCollectionApportioner apportioner = new SewerageCollectionApportioner();
        apportioner.apportion(amtPaid, receiptDetails);
    }
}
