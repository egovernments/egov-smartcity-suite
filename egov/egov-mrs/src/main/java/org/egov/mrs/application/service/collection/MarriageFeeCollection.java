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
package org.egov.mrs.application.service.collection;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
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
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.mrs.application.MarriageConstants;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class which provides API's for collection back update
 * 
 * @author nayeem
 *
 */
@Service
@Transactional(readOnly = true)
public class MarriageFeeCollection extends TaxCollection {
    private static final Logger LOGGER = Logger.getLogger(MarriageFeeCollection.class);

    @Autowired
    private EgBillDao egBillDAO;
    @Autowired
    private ModuleService moduleService;
    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    @Transactional
    public void updateDemandDetails(final BillReceiptInfo billRcptInfo) {
        final BigDecimal totalAmount = billRcptInfo.getTotalAmount();

        final EgDemand demand = getCurrentDemand(Long.valueOf(billRcptInfo.getBillReferenceNum()));
        final String registrationNo = ((BillReceiptInfoImpl) billRcptInfo).getReceiptMisc().getReceiptHeader()
                .getConsumerCode();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("updateDemandDetails : Demand before proceeding : " + demand);

            LOGGER.debug(String.format(
                    " updateDemandDetails : collection back update started for Marriage Registration No : %s, for %s, amounting to Rs. %s and with Receipt No %s",
                    registrationNo, billRcptInfo.getEvent(), totalAmount, billRcptInfo.getReceiptNum()));
        }

        if (billRcptInfo.getEvent().equals(EVENT_RECEIPT_CREATED)) {
            updateCollForRcptCreate(demand, billRcptInfo, totalAmount);
        }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("updateDemandDetails : Demand after processed : " + demand);

    }

    @Transactional
    public void updateCollForRcptCreate(final EgDemand demand, final BillReceiptInfo billRcptInfo,
            final BigDecimal totalAmount) {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug(
                    String.format(" updateCollForRcptCreate : Updating Collection Started For Demand=%s with BillReceiptInfo=%s ",
                            demand, billRcptInfo));
        updateDemandDetailForReceiptCreate(billRcptInfo.getAccountDetails(), demand, billRcptInfo, totalAmount);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public void updateDemandDetailForReceiptCreate(final Set<ReceiptAccountInfo> accountDetails,
            final EgDemand demand, final BillReceiptInfo billRcptInfo, final BigDecimal totalAmount) {

        final StringBuilder query = new StringBuilder(500)
                .append("select dmdet FROM EgDemandDetails dmdet  "
                        + "left join fetch dmdet.egDemandReason dmdRsn "
                        + "left join fetch dmdRsn.egDemandReasonMaster dmdRsnMstr "
                        + "left join fetch dmdRsn.egInstallmentMaster installment "
                        + "WHERE dmdet.egDemand.id = :demand");

        final List<EgDemandDetails> demandDetails = getCurrentSession().createQuery(query.toString())
                .setLong("demand", demand.getId()).list();

        for (final ReceiptAccountInfo receiptAccount : accountDetails) {
            if (org.apache.commons.lang.StringUtils.isNotBlank(receiptAccount.getDescription())
                    && receiptAccount.getCrAmount() != null
                    && receiptAccount.getCrAmount().compareTo(BigDecimal.ZERO) == 1) {

                final String[] desc = receiptAccount.getDescription().split("-", 2);
                final String reason = desc[0].trim();
                final String installment = desc[1].trim();

                for (final EgDemandDetails demandDetail : demandDetails) {
                    if (reason.equalsIgnoreCase(
                            demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster())
                            && installment.equalsIgnoreCase(
                                    demandDetail.getEgDemandReason().getEgInstallmentMaster().getDescription())) {
                        demandDetail.addCollectedWithOnePaisaTolerance(receiptAccount.getCrAmount());

                        if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getIsDemand())
                            demand.addCollected(receiptAccount.getCrAmount());

                        persistCollectedReceipts(demandDetail, billRcptInfo.getReceiptNum(), totalAmount,
                                billRcptInfo.getReceiptDate(), demandDetail.getAmtCollected());
                    }
                }

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format(
                            "Persisted demand and receipt details for tax=%s, installment=%s, receipt no=%s, for Rs. %s ",
                            reason, installment, billRcptInfo.getReceiptNum(), receiptAccount.getCrAmount()));
                }
            }
        }
    }

    @Override
    protected Module module() {
        return moduleService.getModuleByName(MarriageConstants.MODULE_NAME);
    }

    public EgDemand getCurrentDemand(final Long billId) {
        final EgBill egBill = egBillDAO.findById(billId, false);
        return egBill.getEgDemand();
    }

    // Receipt cancellation ,updating bill,demanddetails,demand
    @Transactional
    public void updateCollectionForRcptCancel(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("reconcileCollForRcptCancel : Updating Collection Started For Demand : " + demand
                    + " with BillReceiptInfo - " + billRcptInfo);
        }
        cancelBill(Long.valueOf(billRcptInfo.getBillReferenceNum()));
        updateDmdDetForRcptCancel(demand, billRcptInfo);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("reconcileCollForRcptCancel : Updating Collection finished For Demand : " + demand);
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
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entering method updateDmdDetForRcptCancel");
        }
        String installment;
        for (final ReceiptAccountInfo rcptAccInfo : billRcptInfo.getAccountDetails())
            if (rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1
                    && !rcptAccInfo.getIsRevenueAccount()) {
                final String[] desc = rcptAccInfo.getDescription().split("-", 2);
                final String reason = desc[0].trim();
                installment = desc[1].trim();

                for (final EgDemandDetails demandDetail : demand.getEgDemandDetails())
                    if (reason.equalsIgnoreCase(demandDetail.getEgDemandReason().getEgDemandReasonMaster()
                            .getReasonMaster())
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

                        LOGGER.info("Deducted Collected amount Rs." + rcptAccInfo.getCrAmount() + " for tax : "
                                + reason + " and installment : " + installment);
                    }
            }
        updateReceiptStatusWhenCancelled(billRcptInfo.getReceiptNum());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Exiting method updateDmdDetForRcptCancel");
        }
    }

    @Override
    public List<ReceiptDetail> reconstructReceiptDetail(String billReferenceNumber, BigDecimal actualAmountPaid,
            List<ReceiptDetail> receiptDetailList) {
        return receiptDetailList;
    }

    @Override
    public String constructAdditionalInfoForReceipt(BillReceiptInfo billReceiptInfo) {
        return null;
    }

    @Override
    public ReceiptAmountInfo receiptAmountBifurcation(BillReceiptInfo billReceiptInfo) {
        final ReceiptAmountInfo receiptAmountInfo = new ReceiptAmountInfo();
        BigDecimal currentInstallmentAmount = BigDecimal.ZERO;
        if (billReceiptInfo != null && billReceiptInfo.getBillReferenceNum() != null) {
            for (final ReceiptAccountInfo rcptAccInfo : billReceiptInfo.getAccountDetails()) {
                if (rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1) {
                    currentInstallmentAmount = currentInstallmentAmount.add(rcptAccInfo.getCrAmount());
                }
            }
        }
        receiptAmountInfo.setCurrentInstallmentAmount(currentInstallmentAmount);
        return receiptAmountInfo;
    }

}