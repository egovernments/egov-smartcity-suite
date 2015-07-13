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
import java.util.Date;
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
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.integration.TaxCollection;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.wtms.application.service.ConnectionDemandService;
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
    private ConnectionDemandService connectionDemandService;
    @Autowired
    private InstallmentDao installmentDao;

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public void updateDemandDetails(final BillReceiptInfo billRcptInfo) {
        totalAmount = billRcptInfo.getTotalAmount();
        LOGGER.debug("updateDemandDetails : Updating Demand Details Started, billRcptInfo : " + billRcptInfo);
        final EgDemand demand = getCurrentDemand(Long.valueOf(billRcptInfo.getBillReferenceNum()));
        final String indexNo = ((BillReceiptInfoImpl) billRcptInfo).getReceiptMisc().getReceiptHeader()
                .getConsumerCode();
        LOGGER.info("updateDemandDetails : Demand before proceeding : " + demand);
        LOGGER.info("updateDemandDetails : collection back update started for property : " + indexNo
                + " and receipt event is " + billRcptInfo.getEvent() + ". Total Receipt amount is." + totalAmount
                + " with receipt no." + billRcptInfo.getReceiptNum());

        if (billRcptInfo.getEvent().equals(EVENT_RECEIPT_CREATED))
            updateCollForRcptCreate(demand, billRcptInfo);

        LOGGER.info("updateDemandDetails : Demand after processed : " + demand);
        LOGGER.debug("updateDemandDetails : Updating Demand Details Finished...");

    }

    private void updateCollForRcptCreate(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
        LOGGER.debug("updateCollForRcptCreate : Updating Collection Started For Demand : " + demand
                + " with BillReceiptInfo - " + billRcptInfo);
        LOGGER.info("updateCollForRcptCreate : Total amount collected : " + totalAmount);
        demand.addCollected(totalAmount);
        if (demand.getMinAmtPayable() != null && demand.getMinAmtPayable().compareTo(BigDecimal.ZERO) > 0)
            demand.setMinAmtPayable(BigDecimal.ZERO);
        updateDemandDetailForReceiptCreate(billRcptInfo.getAccountDetails(), demand, billRcptInfo);
        LOGGER.debug("updateCollForRcptCreate : Updating Demand For Collection finished...");
    }

    private void updateDemandDetailForReceiptCreate(final Set<ReceiptAccountInfo> accountDetails, final EgDemand demand,
            final BillReceiptInfo billRcptInfo) {
        LOGGER.debug("Entering method saveCollectionDetails");

        LOGGER.info("saveCollectionDetails : Start get demandDetailList");
        final StringBuffer query = new StringBuffer("select dmdet FROM EgDemandDetails dmdet left join fetch dmdet.egDemandReason dmdRsn ").
                        append("left join fetch dmdRsn.egDemandReasonMaster dmdRsnMstr left join fetch dmdRsn.egInstallmentMaster installment ").
                        append("WHERE dmdet.egDemand.id = :demand");
        final List<EgDemandDetails> demandDetailList = getCurrentSession().createQuery(query.toString())
                .setLong("demand", demand.getId()).list();

        LOGGER.info("saveCollectionDetails : End get demandDetailList");

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

                    demandDetail = installmentWiseDemandDetailsByReason.get(instDesc).get(reason);
                    demandDetail.addCollectedWithOnePaisaTolerance(rcptAccInfo.getCrAmount());

                    persistCollectedReceipts(demandDetail, billRcptInfo.getReceiptNum(), totalAmount,
                            billRcptInfo.getReceiptDate().toDate(), demandDetail.getAmtCollected());
                    LOGGER.info("Persisted demand and receipt details for tax : " + reason + " installment : "
                            + instDesc + " with receipt No : " + billRcptInfo.getReceiptNum() + " for Rs. "
                            + rcptAccInfo.getCrAmount());
                }

        LOGGER.debug("Exiting method saveCollectionDetails");
    }

    /*
     * private void updateCollForRcptCancel(EgDemand demand, BillReceiptInfo
     * billRcptInfo) { LOGGER.debug(
     * "reconcileCollForRcptCancel : Updating Collection Started For Demand : "
     * + demand + " with BillReceiptInfo - " + billRcptInfo);
     * cancelBill(Long.valueOf(billRcptInfo.getBillReferenceNum())); if
     * (demand.getAmtCollected() != null) {
     * demand.setAmtCollected(demand.getAmtCollected().subtract(billRcptInfo.
     * getTotalAmount())); } updateDmdDetForRcptCancel(demand, billRcptInfo);
     * LOGGER.debug(
     * "reconcileCollForRcptCancel : Updating Collection finished For Demand : "
     * + demand); } private EgDemand cancelBill(Long billId) { EgDemand egDemand
     * = null; if (billId != null) { EgBill egBill = egBillDAO.findById(billId,
     * false); egBill.setIs_Cancelled("Y"); } return egDemand; } private void
     * updateDmdDetForRcptCancel(EgDemand demand, BillReceiptInfo billRcptInfo)
     * { LOGGER.debug("Entering method updateDmdDetForRcptCancel");
     * ReceiptAccountInfo rebateRcptAccInfo = null; Map<String,
     * ReceiptAccountInfo> rebateReceiptAccInfoByInstallment =
     * getRebteReceiptAccountInfosByInstallment(billRcptInfo); for
     * (ReceiptAccountInfo rcptAccInfo : billRcptInfo.getAccountDetails()) { if
     * ((rcptAccInfo.getCrAmount() != null &&
     * rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1) &&
     * !rcptAccInfo.getIsRevenueAccount()) { String[] desc =
     * rcptAccInfo.getDescription().split("-", 2); String reason =
     * desc[0].trim(); String installment = desc[1].trim(); EgDemandReasonMaster
     * demandReasonMaster = null; rebateRcptAccInfo =
     * rebateReceiptAccInfoByInstallment.get(installment); for (EgDemandDetails
     * demandDetail : demand.getEgDemandDetails()) { demandReasonMaster =
     * demandDetail.getEgDemandReason().getEgDemandReasonMaster(); if
     * (reason.equals(demandReasonMaster.getReasonMaster())) { if
     * (reason.equalsIgnoreCase(DEMANDRSN_CODE_ADVANCE) ||
     * installment.equals(demandDetail.getEgDemandReason().
     * getEgInstallmentMaster() .getDescription())) { if (rebateRcptAccInfo !=
     * null) { if (demandDetail.getAmtRebate().compareTo(BigDecimal.ZERO) > 0 &&
     * (demandReasonMaster.getCode().equals(DEMANDRSN_CODE_GENERAL_TAX) ||
     * demandReasonMaster .getCode().equalsIgnoreCase(DEMANDRSN_CODE_ADVANCE)))
     * { demandDetail.setAmtRebate(demandDetail.getAmtRebate().subtract(
     * rebateRcptAccInfo.getDrAmount())); } } if
     * (demandDetail.getAmtCollected().compareTo(rcptAccInfo.getCrAmount()) < 0)
     * { throw new EGOVRuntimeException(
     * "updateDmdDetForRcptCancel : Exception while updating cancel receipt, " +
     * "to be deducted amount " + rcptAccInfo.getCrAmount() +
     * " is greater than the collected amount " + demandDetail.getAmtCollected()
     * + " for demandDetail " + demandDetail); }
     * demandDetail.setAmtCollected(demandDetail.getAmtCollected().subtract(
     * rcptAccInfo.getCrAmount())); LOGGER.info("Deducted Collected amount Rs."
     * + rcptAccInfo.getCrAmount() + " for tax : " + reason +
     * " and installment : " + installment); } } } } }
     * updateReceiptStatusWhenCancelled(billRcptInfo.getReceiptNum());
     * LOGGER.debug("Exiting method updateDmdDetForRcptCancel"); } private
     * Map<String, ReceiptAccountInfo>
     * getRebteReceiptAccountInfosByInstallment(BillReceiptInfo billRcptInfo) {
     * Map<String, ReceiptAccountInfo> rebateReceiptAccInfoByInstallment = new
     * HashMap<String, ReceiptAccountInfo>(); for (ReceiptAccountInfo
     * rcptAccInfo : billRcptInfo.getAccountDetails()) { if
     * (rcptAccInfo.getGlCode().equalsIgnoreCase(GLCODE_FOR_TAXREBATE) ||
     * rcptAccInfo.getGlCode().equalsIgnoreCase(PropertyTaxConstants.
     * GLCODE_FOR_ADVANCE_REBATE)) { rebateReceiptAccInfoByInstallment
     * .put(rcptAccInfo.getDescription().split("-", 2)[1].trim(), rcptAccInfo);
     * } } return rebateReceiptAccInfoByInstallment; }
     */
    @Override
    protected Module module() {
        return moduleService.getModuleByName(WaterTaxConstants.EGMODULE_NAME);
    }

    public EgDemand getCurrentDemand(final Long billId) {
        LOGGER.debug("Entered into getCurrentDemand");

        final EgBill egBill = egBillDAO.findById(billId, false);
        final EgDemand egDemand = connectionDemandService.getDemandByInstAndConsumerCode(getCurrentInstallment(),egBill.getConsumerId());
        LOGGER.debug("Exiting from getCurrentDemand");
        return egDemand;
    }

    @Override
    public Installment getCurrentInstallment() {
        return installmentDao.getInsatllmentByModuleForGivenDate(
                moduleService.getModuleByName(WaterTaxConstants.EGMODULE_NAME), new Date());
    }
}
