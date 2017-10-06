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
package org.egov.collection.integration.services;

import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.ReceiptAmountInfo;
import org.egov.collection.integration.models.ReceiptCancellationInfo;
import org.egov.infra.exception.ApplicationRuntimeException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This interface needs to be implemented by any billing application that integrates with the eGov collection system. <br>
 * <br>
 * For internal applications, the methods can use direct API calls. <br>
 * For external applications, the integration can be through web-service/REST calls.<br>
 * <br>
 * The convention to be followed: a bean named "&lt;servicename&gt;CollectionsInterface" needs to be available in the application
 * context. Service name is the 'code' provided for the billing service in <code>ServiceDetails</code> class. <br>
 * <br>
 * The method <code>updateReceiptDetails</code> will be called by collections system whenever an event occurs that needs to be
 * communicated to the billing system e.g. receipt creation, receipt cancellation or instrument (related to a receipt) bounced. <br>
 * <br>
 * The <code>BillReceiptInfo</code> object, apart from the receipt information, also provides this event code.
 */
public interface BillingIntegrationService {

    /**
     * A <code>String</code> indicating the event when the instrument for this receipt has bounced
     */
    String EVENT_INSTRUMENT_BOUNCED = "INSTRUMENT_BOUNCED";

    /**
     * A <code>String</code> indicating the event that the receipt has been created at the Collections end
     */
    String EVENT_RECEIPT_CREATED = "RECEIPT_CREATED";

    /**
     * A <code>String</code> indicating the event that the receipt has been cancelled at the Collections end
     */
    String EVENT_RECEIPT_CANCELLED = "RECEIPT_CANCELLED";

    /**
     * This method needs to be implemented in order to update the billing system with the bill receipt information
     *
     * @param billReceipts a <code>Set</code> of <code>BillReceiptInfo</code> containing the bill receipt, receipt account and
     * receipt instrument information. The information also includes the event for which the billing system needs to be updated.
     * @return a <code>Boolean</code> indicating if the update has taken place successfully
     */
    public void updateReceiptDetails(Set<BillReceiptInfo> billReceipts) throws ApplicationRuntimeException;

    /**
     * Collection system will invoke this method when the billing system send "&lt;enablebillapportioning&gt; as true in the
     * bill-xml. In case of any exception occurred while apportioning, billing system will throw ValidationException.
     *
     * @param billReferenceNumber Bill Reference Number of the bill send by billing system
     * @param actualAmountPaid Acutal amount paid at the counter
     * @param receiptDetailsArray List of ReceiptDetails object associated with this bill
     * @return void
     */

    public void apportionPaidAmount(String billReferenceNumber, BigDecimal actualAmountPaid,
            ArrayList<ReceiptDetail> receiptDetailsArray);

    /**
     * Collection system will invoke this method only when this receipt status is PENDING and there is one more receipt in system
     * created on later/same day with status as APPROVED/SUBMITTED/TO_BE_SUBMITTED
     * @param billReferenceNumber Bill Reference Number of the bill send by billing system
     * @param actualAmountPaid Actual amount paid by the citizen
     * @param receiptDetailList List of existing receipt details
     * @return Reconstructed List of ReceiptDetail objects
     *
     */
    public List<ReceiptDetail> reconstructReceiptDetail(String billReferenceNumber, BigDecimal actualAmountPaid,
            List<ReceiptDetail> receiptDetailList);

    /**
     * Collection system will invoke billing system to frame up the additional message to be printed in receipt
     * @param billReceiptInfo
     * @return Message to be printed in receipt
     */
    public String constructAdditionalInfoForReceipt(BillReceiptInfo billReceiptInfo);

    /**
     * Collection system invokes billing system to get the amount bifurcation information from <BillReceiptInfo> object passed as
     * parameter.
     * @param billReceiptInfo
     * @return
     */
    public ReceiptAmountInfo receiptAmountBifurcation(BillReceiptInfo billReceiptInfo);
    
    /**
     * Collection system invokes billing system to validate receipt cancellation allowed or not. 
     * parameter.
     * @param receiptNumber
     * @return
     */
    default ReceiptCancellationInfo validateCancelReceipt(String receiptNumber) {
        return new ReceiptCancellationInfo();
    }
}