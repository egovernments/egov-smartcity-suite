/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wtms.application.service.collection;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.BillReceiptInfoImpl;
import org.egov.collection.integration.models.ReceiptAccountInfo;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.integration.TaxCollection;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.pims.commons.Position;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.application.service.WaterConnectionSmsAndEmailService;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WaterTaxCollection extends TaxCollection {
    private static final Logger LOGGER = Logger.getLogger(WaterTaxCollection.class);
    private BigDecimal totalAmount = BigDecimal.ZERO;
    @Autowired
    private EgBillDao egBillDAO;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private WaterConnectionDetailsRepository waterConnectionDetailsRepository;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private SimpleWorkflowService<WaterConnectionDetails> waterConnectionWorkflowService;
    
    @Autowired
    private WaterConnectionSmsAndEmailService waterConnectionSmsAndEmailService;

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    @Transactional
    public void updateDemandDetails(final BillReceiptInfo billRcptInfo) {
        totalAmount = billRcptInfo.getTotalAmount();
        final EgDemand demand = getCurrentDemand(Long.valueOf(billRcptInfo.getBillReferenceNum()));
        final String indexNo = ((BillReceiptInfoImpl) billRcptInfo).getReceiptMisc().getReceiptHeader()
                .getConsumerCode();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("updateDemandDetails : Demand before proceeding : " + demand);
            LOGGER.debug("updateDemandDetails : collection back update started for property : " + indexNo
                    + " and receipt event is " + billRcptInfo.getEvent() + ". Total Receipt amount is." + totalAmount
                    + " with receipt no." + billRcptInfo.getReceiptNum());
        }

        if (billRcptInfo.getEvent().equals(EVENT_RECEIPT_CREATED)) {
            updateCollForRcptCreate(demand, billRcptInfo);
            updateWaterConnectionDetails(demand);
            updateWaterTaxIndexes(demand);
        } else if (billRcptInfo.getEvent().equals(EVENT_RECEIPT_CANCELLED)) {
            updateCollectionForRcptCancel(demand, billRcptInfo);
            updateWaterConnDetailsStatus(demand, billRcptInfo);
            updateWaterTaxIndexes(demand);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("updateDemandDetails : Demand after processed : " + demand);

    }

    @Transactional
    public void updateWaterConnectionDetails(final EgDemand demand) {
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .getWaterConnectionDetailsByDemand(demand);
        if (!waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.ACTIVE)) {
            waterConnectionDetails.setStatus(waterTaxUtils
                    .getStatusByCodeAndModuleType(WaterTaxConstants.APPLICATION_STATUS_FEEPAID, WaterTaxConstants.MODULETYPE));
            Long approvalPosition = Long.valueOf(0);
            final WorkFlowMatrix wfmatrix = waterConnectionWorkflowService.getWfMatrix(waterConnectionDetails
                    .getStateType(), null, null, WaterTaxConstants.NEW_CONNECTION_MATRIX_ADDL_RULE,
                    waterConnectionDetails.getCurrentState().getValue(), null);
            final Position posobj = waterTaxUtils.getCityLevelCommissionerPosition(wfmatrix.getNextDesignation());
            if (posobj != null)
                approvalPosition = posobj.getId();
            waterConnectionDetailsService.createMatrixWorkflowTransition(waterConnectionDetails,
                    approvalPosition, WaterTaxConstants.FEE_COLLECTION_COMMENT, WaterTaxConstants.NEW_CONNECTION_MATRIX_ADDL_RULE,
                    null);
            waterConnectionSmsAndEmailService.sendSmsAndEmail(waterConnectionDetails, null);
            waterConnectionDetailsRepository.saveAndFlush(waterConnectionDetails);
        }
    }

    @Transactional
    private void updateCollForRcptCreate(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("updateCollForRcptCreate : Updating Collection Started For Demand : " + demand
                    + " with BillReceiptInfo - " + billRcptInfo);
        updateDemandDetailForReceiptCreate(billRcptInfo.getAccountDetails(), demand, billRcptInfo);
    }

    @Transactional
    private void updateDemandDetailForReceiptCreate(final Set<ReceiptAccountInfo> accountDetails, final EgDemand demand,
            final BillReceiptInfo billRcptInfo) {

        final StringBuffer query = new StringBuffer(
                "select dmdet FROM EgDemandDetails dmdet left join fetch dmdet.egDemandReason dmdRsn ")
                        .append("left join fetch dmdRsn.egDemandReasonMaster dmdRsnMstr left join fetch dmdRsn.egInstallmentMaster installment ")
                        .append("WHERE dmdet.egDemand.id = :demand");
        final List<EgDemandDetails> demandDetailList = getCurrentSession().createQuery(query.toString())
                .setLong("demand", demand.getId()).list();

        final Map<String, Map<String, EgDemandDetails>> installmentWiseDemandDetailsByReason = new HashMap<String, Map<String, EgDemandDetails>>();
        Map<String, EgDemandDetails> demandDetailByReason = null;

        EgDemandReason dmdRsn = null;
        String installmentDesc = null;

        for (final EgDemandDetails dmdDtls : demandDetailList)
            if (dmdDtls.getAmount().compareTo(BigDecimal.ZERO) > 0) {

                dmdRsn = dmdDtls.getEgDemandReason();
                installmentDesc = dmdRsn.getEgInstallmentMaster().getDescription();
                demandDetailByReason = new HashMap<String, EgDemandDetails>();

                if (installmentWiseDemandDetailsByReason.get(installmentDesc) == null) {
                    demandDetailByReason.put(dmdRsn.getEgDemandReasonMaster().getReasonMaster(), dmdDtls);
                    installmentWiseDemandDetailsByReason.put(installmentDesc, demandDetailByReason);
                } else
                    installmentWiseDemandDetailsByReason.get(installmentDesc)
                            .put(dmdRsn.getEgDemandReasonMaster().getReasonMaster(), dmdDtls);
            } else if (LOGGER.isDebugEnabled())
                LOGGER.debug("saveCollectionDetails - demand detail amount is zero " + dmdDtls);

        EgDemandDetails demandDetail = null;

        for (final ReceiptAccountInfo rcptAccInfo : accountDetails)
            if (rcptAccInfo.getDescription() != null && !rcptAccInfo.getDescription().isEmpty())
                if (rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1) {
                    final String[] desc = rcptAccInfo.getDescription().split("-", 2);
                    final String reason = desc[0].trim();
                    final String instDesc = desc[1].trim();

                    demandDetail = installmentWiseDemandDetailsByReason.get(instDesc).get(reason);
                    demandDetail.addCollectedWithOnePaisaTolerance(rcptAccInfo.getCrAmount());
                    if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getIsDemand())
                        demand.addCollected(rcptAccInfo.getCrAmount());
                    persistCollectedReceipts(demandDetail, billRcptInfo.getReceiptNum(), totalAmount,
                            billRcptInfo.getReceiptDate(), demandDetail.getAmtCollected());
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("Persisted demand and receipt details for tax : " + reason + " installment : "
                                + instDesc + " with receipt No : " + billRcptInfo.getReceiptNum() + " for Rs. "
                                + rcptAccInfo.getCrAmount());
                }

    }

    @Override
    protected Module module() {
        return moduleService.getModuleByName(WaterTaxConstants.EGMODULE_NAME);
    }

    public EgDemand getCurrentDemand(final Long billId) {
        final EgBill egBill = egBillDAO.findById(billId, false);
        return egBill.getEgDemand();
    }

    // Receipt cancellation ,updating bill,demanddetails,demand

    @Transactional
    private void updateCollectionForRcptCancel(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
        LOGGER.debug("reconcileCollForRcptCancel : Updating Collection Started For Demand : " + demand
                + " with BillReceiptInfo - " + billRcptInfo);
        cancelBill(Long.valueOf(billRcptInfo.getBillReferenceNum()));

        if (demand.getAmtCollected() != null && demand.getAmtCollected() != BigDecimal.ZERO)
            demand.setAmtCollected(demand.getAmtCollected().subtract(billRcptInfo.getTotalAmount()));

        updateDmdDetForRcptCancel(demand, billRcptInfo);
        LOGGER.debug("reconcileCollForRcptCancel : Updating Collection finished For Demand : " + demand);
    }

    @Transactional
    private void cancelBill(final Long billId) {
        if (billId != null) {
            final EgBill egBill = egBillDAO.findById(billId, false);
            egBill.setIs_Cancelled("Y");
        }
    }

    @Transactional
    private void updateDmdDetForRcptCancel(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
        LOGGER.debug("Entering method updateDmdDetForRcptCancel");

        for (final ReceiptAccountInfo rcptAccInfo : billRcptInfo.getAccountDetails())
            if (rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1
                    && !rcptAccInfo.getIsRevenueAccount()) {
                final String[] desc = rcptAccInfo.getDescription().split("-", 2);
                final String reason = desc[0].trim();
                final String installment = desc[1].trim();

                for (final EgDemandDetails demandDetail : demand.getEgDemandDetails())
                    if (reason.equalsIgnoreCase(demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster())) {
                        if (demandDetail.getAmtCollected().compareTo(rcptAccInfo.getCrAmount()) < 0)
                            throw new EGOVRuntimeException(
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
        updateReceiptStatusWhenCancelled(billRcptInfo.getReceiptNum());
        LOGGER.debug("Exiting method updateDmdDetForRcptCancel");
    }

    @Transactional
    private void updateWaterConnDetailsStatus(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .getWaterConnectionDetailsByDemand(demand);
        StateHistory stateHistory = null;
        if (waterConnectionDetails.getStatus().getCode().equalsIgnoreCase(WaterTaxConstants.APPLICATION_STATUS_FEEPAID)) {
            waterConnectionDetails.setStatus(waterTaxUtils
                    .getStatusByCodeAndModuleType(WaterTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN,
                            WaterTaxConstants.MODULETYPE));
            Long approvalPosition = Long.valueOf(0);
            if (!waterConnectionDetails.getStateHistory().isEmpty()
                    && waterConnectionDetails.getStateHistory() != null)
                Collections.reverse(waterConnectionDetails.getStateHistory());
            stateHistory = waterConnectionDetails.getStateHistory().get(0);
            final Position owner = stateHistory.getOwnerPosition();
            if (owner != null)
                approvalPosition = owner.getId();
            waterConnectionDetailsService.createMatrixWorkflowTransition(waterConnectionDetails,
                    approvalPosition, "Receipt Cancelled", WaterTaxConstants.NEW_CONNECTION_MATRIX_ADDL_RULE,
                    null);
        }

    }

    private void updateWaterTaxIndexes(final EgDemand demand) {
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .getWaterConnectionDetailsByDemand(demand);
        waterConnectionDetailsService.updateIndexes(waterConnectionDetails);
    }
}