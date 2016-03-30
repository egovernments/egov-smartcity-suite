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

import org.apache.log4j.Logger;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.collection.utils.FinancialsUtil;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
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
        receiptHeaderService.updateDishonoredInstrumentStatus(receiptHeader, instrumentHeader, receiptInstrumentBounceStatus,
                false);
        LOGGER.debug("Updated receipt status to " + receiptInstrumentBounceStatus.getCode()
                + " set reconcilation to false");

    }

    private EgwStatus getDishonoredStatus() {
        return collectionsUtil.getStatusForModuleAndCode(FinancialConstants.STATUS_MODULE_INSTRUMENT,
                FinancialConstants.INSTRUMENT_DISHONORED_STATUS);
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
