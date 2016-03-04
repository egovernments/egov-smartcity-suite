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
package org.egov.collection.integration.services;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.BillReceiptInfoImpl;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.collection.utils.FinancialsUtil;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.services.instrument.FinancialIntegrationService;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;

public class DishonorChequeService implements FinancialIntegrationService {

    private static final Logger LOGGER = Logger.getLogger(DishonorChequeService.class);

    private CollectionsUtil collectionsUtil;
    private FinancialsUtil financialsUtil;
    private PersistenceService persistenceService;
    private ReceiptHeaderService receiptHeaderService;

    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    @Override
    public void updateCollectionsOnInstrumentDishonor(final Long instrumentHeaderId) {
        LOGGER.debug("Update Collection and Billing system for dishonored instrument id: " + instrumentHeaderId);
        final EgwStatus receiptInstrumentBounceStatus = collectionsUtil
                .getReceiptStatusForCode(CollectionConstants.RECEIPT_STATUS_CODE_INSTRUMENT_BOUNCED);
        final EgwStatus receiptCancellationStatus = collectionsUtil
                .getReceiptStatusForCode(CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED);
        final ReceiptHeader receiptHeader = (ReceiptHeader) persistenceService
                .find("select DISTINCT (receipt) from ReceiptHeader receipt "
                        + "join receipt.receiptInstrument as instruments where instruments.id=? and instruments.statusId.code not in (?,?)",
                        Long.valueOf(instrumentHeaderId), receiptInstrumentBounceStatus.getCode(),
                        receiptCancellationStatus.getCode());
        final InstrumentHeader instrumentHeader = (InstrumentHeader) persistenceService.findByNamedQuery(
                CollectionConstants.QUERY_GET_INSTRUMENTHEADER_BY_ID, instrumentHeaderId);
        instrumentHeader.setStatusId(getDishonoredStatus());
        financialsUtil.updateInstrumentHeader(instrumentHeader);
        // update receipts - set status to INSTR_BOUNCED and recon flag to false
        updateReceiptHeaderStatus(receiptHeader, receiptInstrumentBounceStatus, false);
        LOGGER.debug("Updated receipt status to " + receiptInstrumentBounceStatus.getCode()
                + " set reconcilation to false");

        // update the billing system.
        if (updateDetailsToBillingSystem(receiptHeader))
            LOGGER.debug("Billing system have been updated successfully");
        else {
            final String errorMsg = "Billing system have not been updated successfully for receipt number: "
                    + receiptHeader.getReceiptnumber() + receiptHeader.getConsumerCode() != null ? "and consumer code : "
                            + receiptHeader.getConsumerCode()
                            : "";
                            LOGGER.debug(errorMsg);
                            throw new ApplicationRuntimeException(errorMsg);
        }

    }

    /**
     * This method updates the status and reconciliation flag for the given receipt
     *
     * @param receiptHeader <code>ReceiptHeader</code> objects whose status and reconciliation flag have to be modified
     * @param status a <code>EgwStatus</code> instance representing the state to which the receipt has to be updated with
     * @param isReconciled a <code>Boolean</code> flag indicating the value for the reconciliation status
     */
    private void updateReceiptHeaderStatus(final ReceiptHeader receiptHeader, final EgwStatus status,
            final boolean isReconciled) {
        if (status != null)
            receiptHeader.setStatus(status);
        receiptHeader.setIsReconciled(isReconciled);
        receiptHeaderService.update(receiptHeader);
    }

    private EgwStatus getDishonoredStatus() {
        return collectionsUtil.getStatusForModuleAndCode(FinancialConstants.STATUS_MODULE_INSTRUMENT,
                FinancialConstants.INSTRUMENT_DISHONORED_STATUS);
    }

    /**
     * <p>
     * This method populates the receipt details into the bill objects to send to the billing system. A map is created with key as
     * the service code, and value as Set of <code>BillReceiptInfo</code> for the receipt to be updated to that billing system.
     * <p>
     * Only if the billing system is updated successfully, the reconciliation status of those receipts are set to true
     *
     * @return a <code>Boolean</code> flag indicating true only if the updates to all the billing system have taken place
     * successfully
     */
    private Boolean updateDetailsToBillingSystem(final ReceiptHeader receiptHeader) {

        Boolean flag = true;
        final BillReceiptInfo billReceipt = new BillReceiptInfoImpl(receiptHeader, chartOfAccountsHibernateDAO);
        final Set<BillReceiptInfo> billReceiptInfo = new HashSet<BillReceiptInfo>();
        billReceiptInfo.add(billReceipt);
        final String serviceCode = receiptHeader.getService().getCode();
        try {
            receiptHeaderService.updateBillingSystem(serviceCode, billReceiptInfo);
            updateReceiptHeaderStatus(receiptHeader, null, true);
            LOGGER.debug("Updated reconcilation status of receipts to true for the  billing system " + serviceCode
                    + " and receipt number " + receiptHeader.getReceiptnumber());
        } catch (final Exception e) {
            // mark the flag as false and continue with updating rest of the
            // billing systems
            LOGGER.error("Exception in update to billing system " + serviceCode + " successful." + e.getMessage());
            flag = false;
        }

        // returns true only if batch update to all systems was successful
        return flag;
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

    public void setReceiptHeaderService(final ReceiptHeaderService receiptHeaderService) {
        this.receiptHeaderService = receiptHeaderService;
    }

    public void setFinancialsUtil(final FinancialsUtil financialsUtil) {
        this.financialsUtil = financialsUtil;
    }

    @Override
    public void updateSourceInstrumentVoucher(final String event, final Long instrumentHeaderId) {
        // TODO Auto-generated method stub

    }
}
