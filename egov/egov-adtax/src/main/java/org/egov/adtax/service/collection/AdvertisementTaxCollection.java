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
package org.egov.adtax.service.collection;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.adtax.service.AdvertisementDemandService;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.BillReceiptInfoImpl;
import org.egov.collection.integration.models.ReceiptAccountInfo;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.integration.TaxCollection;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdvertisementTaxCollection extends TaxCollection {
    private static final Logger LOGGER = Logger.getLogger(AdvertisementTaxCollection.class);
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Autowired
    private EgBillDao egBillDAO;
    @Autowired
    private AdvertisementDemandService advertisementDemandService;

    @Override
    public List<ReceiptDetail> reconstructReceiptDetail(final String billReferenceNumber,
            final BigDecimal actualAmountPaid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateDemandDetails(final BillReceiptInfo billRcptInfo) {
        totalAmount = billRcptInfo.getTotalAmount();
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
            updateDemandWithcollectdTaxDetails(demand, billRcptInfo, EVENT_RECEIPT_CREATED);
        else if (billRcptInfo.getEvent().equals(EVENT_RECEIPT_CANCELLED)) {
            updateDemandWithcollectdTaxDetails(demand, billRcptInfo, EVENT_RECEIPT_CANCELLED);
            updateReceiptStatusWhenCancelled(billRcptInfo.getReceiptNum());

        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("updateDemandDetails : Demand after processed : " + demand);

    }

    private BigDecimal updateDemandWithcollectdTaxDetails(final EgDemand demand, final BillReceiptInfo billReceiptInfo,
            final String eventType) {

        BigDecimal totalAmountCollected = BigDecimal.ZERO;

        for (final ReceiptAccountInfo recAccInfo : billReceiptInfo.getAccountDetails()) {
            String demandMasterReasonDesc = null;
            if (recAccInfo.getDescription() != null) {
                demandMasterReasonDesc = recAccInfo
                        .getDescription()
                        .substring(
                                0,
                                recAccInfo.getDescription().indexOf(
                                        AdvertisementTaxConstants.COLL_RECEIPTDETAIL_DESC_PREFIX)).trim();
                if (eventType.equals(EVENT_RECEIPT_CREATED))
                    totalAmountCollected = totalAmountCollected.add(createOrUpdateDemandDetails(demandMasterReasonDesc,
                            demand, billReceiptInfo, recAccInfo));
            }
        }
        LOGGER.info("Demand before updateDemandDetails() processing: " + demand.getAmtCollected() + demand);

        if (eventType.equals(EVENT_RECEIPT_CANCELLED)) {
            cancelBill(Long.valueOf(billReceiptInfo.getBillReferenceNum()));
            demand.setAmtCollected(demand.getAmtCollected().subtract(totalAmountCollected));
            updateDmdDetForRcptCancel(demand, billReceiptInfo);
        }
        demand.setModifiedDate(new Date());
        return totalAmountCollected;
    }

    @Transactional
    private void updateDmdDetForRcptCancel(final EgDemand demand, final BillReceiptInfo billRcptInfo) {
        LOGGER.debug("Entering method updateDmdDetForRcptCancel");
        String demandMasterReasonDesc = null;
        for (final ReceiptAccountInfo rcptAccInfo : billRcptInfo.getAccountDetails())
            if (rcptAccInfo.getCrAmount() != null && rcptAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) == 1
            && !rcptAccInfo.getIsRevenueAccount()) {
                demandMasterReasonDesc = billRcptInfo
                        .getDescription()
                        .substring(
                                0,
                                billRcptInfo.getDescription().indexOf(
                                        AdvertisementTaxConstants.COLL_RECEIPTDETAIL_DESC_PREFIX)).trim();

                for (final EgDemandDetails demandDetail : demand.getEgDemandDetails())
                    if (demandMasterReasonDesc.equalsIgnoreCase(demandDetail.getEgDemandReason()
                            .getEgDemandReasonMaster().getReasonMaster())) {
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

    private BigDecimal createOrUpdateDemandDetails(final String demandMasterReasonDesc, final EgDemand demand,
            final BillReceiptInfo billReceiptInfo, final ReceiptAccountInfo recAccInfo) {
        BigDecimal totalAmountCollected = BigDecimal.ZERO;

        Boolean demandReasonPartOfDemand = false;

        if (recAccInfo.getCrAmount() != null && recAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) > 0) {
            // updating the existing demand detail..
            for (final EgDemandDetails demandDetail : demand.getEgDemandDetails())
                if (demandDetail.getEgDemandReason() != null
                && demandDetail.getEgDemandReason().getEgDemandReasonMaster() != null
                        && demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster().trim()
                                .equalsIgnoreCase(demandMasterReasonDesc)) {
                    // && (demandDetail.getAmount().compareTo(BigDecimal.ZERO) >
                    // 0)) {

                    if (AdvertisementTaxConstants.DEMANDREASON_PENALTY.equalsIgnoreCase(demandMasterReasonDesc))
                        demandDetail.setAmount(demandDetail.getAmount().add(recAccInfo.getCrAmount()));
                    demandDetail.addCollected(recAccInfo.getCrAmount());
                    /*
                     * Save bill detail and demand deatail relation in the
                     * intermediate table.
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
                final EgDemandDetails demandDetail = advertisementDemandService.createDemandDetails(recAccInfo
                        .getCrAmount(), advertisementDemandService.getDemandReasonByCodeAndInstallment(
                        demandMasterReasonDesc, advertisementDemandService.getCurrentInstallment()), recAccInfo
                        .getCrAmount());
                demand.addEgDemandDetails(demandDetail);
            }
            demand.setModifiedDate(new Date());
        }
        return totalAmountCollected;
    }

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
        // TODO Auto-generated method stub
        return null;
    }

}
