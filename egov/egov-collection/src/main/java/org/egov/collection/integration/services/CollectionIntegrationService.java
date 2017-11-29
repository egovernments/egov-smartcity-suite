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
package org.egov.collection.integration.services;

import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.integration.models.BillInfo;
import org.egov.collection.integration.models.BillInfoImpl;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.PaymentInfo;
import org.egov.collection.integration.models.PaymentInfoRequest;
import org.egov.collection.integration.models.PaymentInfoSearchRequest;
import org.egov.collection.integration.models.RestAggregatePaymentInfo;
import org.egov.collection.integration.models.RestReceiptInfo;
import org.egov.collection.integration.pgi.PaymentRequest;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface exposed by collections system to other systems (typically billing systems)
 */
public interface CollectionIntegrationService {
    /**
     * Returns the list of bill receipt information objects for given reference number (typically bill number). One bill can be
     * associated with multiple receipts in case of part payments.
     *
     * @param serviceCode The service code of the billing system
     * @param refNum The bill reference number
     * @return The bill receipt information
     */
     List<BillReceiptInfo> getBillReceiptInfo(String serviceCode, String refNum);

    /**
     * Returns the bill receipt information for given set of reference numbers (typically bill numbers)
     *
     * @param serviceCode The service code of the billing system
     * @param refNums Set of bill reference numbers
     * @return Map of bill receipt information objects. Key = bill reference number, value = List of Bill receipt information
     * objects
     */
     Map<String, List<BillReceiptInfo>> getBillReceiptInfo(String serviceCode, Set<String> refNums);

    /**
     * Returns the bill receipt information for given receipt number
     *
     * @param serviceCode The service code of the billing system
     * @param receiptNum The Collections or Manual Receipt Number
     * @return The bill receipt information
     */
     BillReceiptInfo getReceiptInfo(String serviceCode, String receiptNum);

    /**
     * Returns HashMap of the bill receipt information for given set of receipt numbers
     *
     * @param serviceCode The service code of the billing system
     * @param receiptNums Set of Collections or Manual Receipt Number
     * @return Map of bill receipt information objects. Key = bill reference number, value = Bill receipt information object
     */
     Map<String, BillReceiptInfo> getReceiptInfo(String serviceCode, Set<String> receiptNums);

    /**
     * Returns set of bill receipt information for given instrument number. One instrument might have been used to pay for more
     * than one bills, hence may belong to more than one receipts.
     *
     * @param serviceCode The service code of the billing system
     * @param instrumentNum The instrument number
     * @return The set of bill receipt information objects for given instrument number
     */
     List<BillReceiptInfo> getInstrumentReceiptInfo(String serviceCode, String instrumentNum);

    /**
     * Returns the bill receipt information for given set of instrument numbers. Since one instrument may belong to more than one
     * receipts, this method will return a map of instrument number to set of bill receipt information objects.
     *
     * @param serviceCode The service code of the billing system
     * @param instrumentNums Set of instrument numbers
     * @return Map of bill receipt information objects. Key = instrument number, value = Set of Bill receipt information objects
     */
     Map<String, List<BillReceiptInfo>> getInstrumentReceiptInfo(String serviceCode, Set<String> instrumentNums);

    /**
     * This method creates the receipt for the given bill and payment information
     *
     * @param bill an instance of <code>BillInfo</code> containing the bill information.
     * @param paymentInfoList a <code>List</code> of <code>PaymentInfo</code> containing the payment information. A List is used
     * in order to accommodate payment using multiple cheques/DDs for a single receipt. In all other cases, it should be taken
     * care that only a single mode of payment is permitted, i.e., the List should contain a single entry.
     * @return an instance of <code>BillReceiptInfo</code> containing all details of the created receipt.
     */
     BillReceiptInfo createReceipt(BillInfo bill, List<PaymentInfo> paymentInfoList);

    /**
     * Returns the online pending bill receipt information for given service and consumer code
     *
     * @param serviceCode The service code of the billing system
     * @param consumerCode The consumer code of the billing system
     * @return List of Pending Bill receipt information objects
     */
     List<BillReceiptInfo> getOnlinePendingReceipts(String serviceCode, String consumerCode);

    /**
     * This method creates the miscellaneous receipt for the given bill and payment information
     *
     * @param bill an instance of <code>BillInfo</code> containing the bill information.
     * @param paymentInfoList a <code>List</code> of <code>PaymentInfo</code> containing the payment information. A List is used
     * in order to accommodate payment using multiple cheques/DDs for a single receipt. In all other cases, it should be taken
     * care that only a single mode of payment is permitted, i.e., the List should contain a single entry.
     * @return an instance of <code>BillReceiptInfo</code> containing all details of the created receipt.
     */
     BillReceiptInfo createMiscellaneousReceipt(BillInfo bill, List<PaymentInfo> paymentInfoList);

    /**
     * This method returns the count of receipts created with the sum total of receipt amount for the given date range
     * @param fromDate From Date to Search the Aggregate Payment
     * @param toDate To Date to Search the Aggregate Payment
     * @return List of <code>RestAggregatePaymentInfo</code> containing aggregate information of receipts
     */
     List<RestAggregatePaymentInfo> getAggregateReceiptTotal(PaymentInfoSearchRequest paymentInfoSearchRequest);

    /**
     * This method returns the list of receipt created in the system for the given date range and service code of the billing
     * system.
     * @param fromDate From Date to Search the Aggregate Paymentss
     * @param toDate To Date to Search the Aggregate Payment
     * @param serviceCode The service code of the billing system
     * @return List of <code>RestReceiptInfo</code> containing details of Receipt Information
     */
     List<RestReceiptInfo> getReceiptDetailsByDateAndService(PaymentInfoSearchRequest paymentInfoSearchRequest);

    /**
     * This method cancel the receipt information provided by the billing system
     * @param paymentInfoSearchRequest
     * @return success/failure based on the cancellation status
     */
     String cancelReceipt(PaymentInfoSearchRequest paymentInfoSearchRequest);

    /**
     * This method returns the receipt information for the transaction id and channel information provided by the billing system.
     * @param paymentInfoSearchRequest
     * @return <code>RestReceiptInfo</code> containing details of Receipt Information
     */
     RestReceiptInfo getDetailsByTransactionId(PaymentInfoSearchRequest paymentInfoSearchRequest);

    /**
     * This method returns sorted list of only the paid receipt detail for the receipt number provided
     * @param receiptNumber
     * @return List of <code>ReceiptDetail</code>
     */
     List<ReceiptDetail> getReceiptDetailListByReceiptNumber(String receiptNumber);

    /**
     * This method returns Payment Request to process payments through mobile
     * @param billInfo
     * @return PaymentRequest
     */
     PaymentRequest processMobilePayments(BillInfoImpl billInfo);

    /**
     * @param paymentInfoRequest
     * @return
     */
     List<RestReceiptInfo> getDetailsByUserServiceAndConsumerCode( PaymentInfoRequest paymentInfoRequest);

    
     byte[] downloadReceiptByReceiptAndConsumerNo(String receiptNumber, String consumerCode);

}
