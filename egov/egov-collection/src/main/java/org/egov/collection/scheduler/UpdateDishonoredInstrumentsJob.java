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
package org.egov.collection.scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.scheduler.quartz.AbstractQuartzJob;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.services.instrument.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;

public class UpdateDishonoredInstrumentsJob extends AbstractQuartzJob {
    private static final Logger LOGGER = Logger.getLogger(UpdateDishonoredInstrumentsJob.class);
    private static final long serialVersionUID = 1L;

    protected PersistenceService persistenceService;
    @Autowired
    private AppConfigValueService appConfigValuesService;
    @Autowired
    private ReceiptHeaderService receiptHeaderService;

    // private List<ReceiptHeader> receiptHeaders = new
    // ArrayList<ReceiptHeader>();
    private final Set<BillReceiptInfo> billReceipts = new HashSet<BillReceiptInfo>();
    // private final Set<ReceiptPayeeDetails> receiptPayeeDetails = new
    // HashSet<ReceiptPayeeDetails>();
    private final CollectionsUtil collectionsUtil = new CollectionsUtil();
    private boolean testMode = false;

    public UpdateDishonoredInstrumentsJob() {
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
        collectionsUtil.setPersistenceService(persistenceService);
    }

    public void setInstrumentService(final InstrumentService instrumentService) {
    }

    @Override
    public void executeJob() {
        processDishonoredInstruments();
    }

    /**
     * This method gets the bounced cheque instruments from financials' and
     * sends the corresponding update to the billing system as a batch at the
     * end of day
     */
    public void processDishonoredInstruments() {
        LOGGER.debug("Started batch update process");
        Date bouncedToDate = new Date();
        Date bouncedFromDate = null;

        /*
         * if(!testMode){ HibernateUtil.getCurrentSession().beginTransaction();
         * }
         */
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            final String strDate = sdf.format(bouncedToDate);
            bouncedToDate = sdf.parse(strDate);
            /**
             * Add one day to current date as the default Date format returns
             * date in 00:00:00 format and hence doesn't fetches records marked
             * as dishonoured on the current day
             */

            bouncedToDate = DateUtils.add(bouncedToDate, Calendar.DAY_OF_MONTH, 1);

            final AppConfigValues appData = appConfigValuesService.getConfigValuesByModuleAndKey(
                    CollectionConstants.MODULE_NAME_COLLECTIONS, CollectionConstants.BOUNCEDINSTRUPDATE_RECONDATE).get(
                            0);
            bouncedFromDate = sdf.parse(appData.getValue());

            bouncedFromDate = DateUtils.add(bouncedFromDate, Calendar.DAY_OF_MONTH, -1);

            /*
             * List<InstrumentVoucher> bouncedChequeVouchers = instrumentService
             * .getBouncedCheques(bouncedFromDate, bouncedToDate);
             * LOGGER.debug("Bounced cheque vouchers from " + bouncedFromDate +
             * " till " + bouncedToDate + " : " + bouncedChequeVouchers); //
             * perform batch update if there are any bounced instruments if
             * (bouncedChequeVouchers != null &&
             * !(bouncedChequeVouchers.isEmpty()) &&
             * bouncedChequeVouchers.get(0) != null) { // get list of vouchers
             * corresponding to the bounced cheques List<Long> voucherHeaderIds
             * = new ArrayList<Long>(); for (InstrumentVoucher instrVoucher :
             * bouncedChequeVouchers) {
             * voucherHeaderIds.add(instrVoucher.getVoucherHeaderId() .getId());
             * } EgwStatus status = collectionsUtil.getReceiptStatusForCode(
             * CollectionConstants.RECEIPT_STATUS_CODE_INSTRUMENT_BOUNCED); //
             * for each instrument get list of receipt headers
             * List<ReceiptHeader> receiptHeaders =
             * persistenceService.findAllByNamedQuery(
             * CollectionConstants.QUERY_RECEIPTS_FOR_VOUCHERS,
             * voucherHeaderIds); List<ReceiptHeader> receiptHeaders =
             * persistenceService.findAllByNamedQuery(
             * CollectionConstants.QUERY_RECEIPTS_FOR_BOUNCED_INSTRUMENTS
             * ,status.getCode(), voucherHeaderIds); // update receipts - set
             * status to INSTR_BOUNCED and recon flag to false
             * updateReceiptHeaderStatus(receiptHeaders, status, false);
             * LOGGER.debug("Updated receipt status to " + status.getCode() +
             * " set reconcilation to false"); //get receipts with recon status
             * as false and update billing system receiptHeaders =
             * persistenceService.findAllByNamedQuery(
             * CollectionConstants.QUERY_RECEIPTS_BY_RECONSTATUS, false); //
             * update the billing system if
             * (updateDetailsToBillingSystems(receiptHeaders)) {
             * LOGGER.debug("All billing systems have been updated successfully"
             * ); // change the batch reconcilation date to today's date.
             * appData.setValue(strDate);
             * genericDao.getAppDataDAO().updateAppDataValue(appData);
             * LOGGER.debug("Batch update completed on : " + strDate); } else {
             * LOGGER
             * .debug("All billing systems have not been updated successfully");
             * } }
             */
        } catch (final ParseException e) {
            LOGGER.error("Exception occured : " + e.getMessage());
        }
        /*
         * if(!isTestMode()){ HibernateUtil.getcommitTransaction(); }
         */

    }

    public boolean isTestMode() {
        return testMode;
    }

    public void setTestMode(final boolean testMode) {
        this.testMode = testMode;
    }

}
