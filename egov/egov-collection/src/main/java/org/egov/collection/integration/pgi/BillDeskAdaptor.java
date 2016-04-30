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
package org.egov.collection.integration.pgi;

import org.apache.log4j.Logger;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.utils.EGovConfig;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//import com.billdesk.pgidsk.PGIUtil;

/**
 * The PaymentRequestAdaptor class frames the request object for the payment
 * service.
 */

public class BillDeskAdaptor implements PaymentGatewayAdaptor {

    private static final Logger LOGGER = Logger.getLogger(BillDeskAdaptor.class);

    /**
     * This method invokes APIs to frame request object for the payment service
     * passed as parameter
     *
     * @param serviceDetails
     * @param receiptHeader
     * @return
     */
    @Override
    public PaymentRequest createPaymentRequest(final ServiceDetails paymentServiceDetails,
            final ReceiptHeader receiptHeader) {
        final DefaultPaymentRequest paymentRequest = new DefaultPaymentRequest();

        final StringBuffer paymentReqMsg = new StringBuffer(20);
        paymentReqMsg
        .append(CollectionConstants.ONLINE_PAYMENT_BILLDESK_MERCHANTID)
        // MerchantID
        .append(CollectionConstants.PIPE_SEPARATOR)
        .append(receiptHeader.getReferencenumber())
        // txtCustomerID
        .append(CollectionConstants.PIPE_SEPARATOR)
        .append(CollectionConstants.PAYMENT_REQUEST_MSG_NA)
        .append(CollectionConstants.PIPE_SEPARATOR)
        .append(receiptHeader.getTotalAmount().setScale(CollectionConstants.AMOUNT_PRECISION_DEFAULT,
                BigDecimal.ROUND_UP))
                // txtTxnAmount
                .append(CollectionConstants.PIPE_SEPARATOR)
                .append(CollectionConstants.PAYMENT_REQUEST_MSG_NA)
                .append(CollectionConstants.PIPE_SEPARATOR)
                .append(CollectionConstants.PAYMENT_REQUEST_MSG_NA)
                .append(CollectionConstants.PIPE_SEPARATOR)
                .append(CollectionConstants.PAYMENT_REQUEST_MSG_NA)
                .append(CollectionConstants.PIPE_SEPARATOR)
                .append("INR")
                .append(CollectionConstants.PIPE_SEPARATOR)
                .append(CollectionConstants.PAYMENT_REQUEST_MSG_NA)
                .append(CollectionConstants.PIPE_SEPARATOR)
                .append('R')
                .append(CollectionConstants.PIPE_SEPARATOR)
                .append(CollectionConstants.PAYMENT_REQUEST_SECURITY_ID)
                .append(CollectionConstants.PIPE_SEPARATOR)
                .append(CollectionConstants.PAYMENT_REQUEST_MSG_NA)
                .append(CollectionConstants.PIPE_SEPARATOR)
                .append(CollectionConstants.PAYMENT_REQUEST_MSG_NA)
                .append(CollectionConstants.PIPE_SEPARATOR)
                .append('F')
                .append(CollectionConstants.PIPE_SEPARATOR)
                .append(receiptHeader.getId())
                // txtAdditionalInfo1
                .append(CollectionConstants.PIPE_SEPARATOR)
                .append(receiptHeader.getTotalAmount().setScale(CollectionConstants.AMOUNT_PRECISION_DEFAULT,
                        BigDecimal.ROUND_UP))
                        // txtAdditionalInfo2
                        .append(CollectionConstants.PIPE_SEPARATOR)
                        .append(EGovConfig.getMessage(CollectionConstants.CUSTOMPROPERTIES_FILENAME,
                                CollectionConstants.MESSAGEKEY_BILLDESK_REV_HEAD_ + receiptHeader.getService().getCode()))
                                // txtAdditionalInfo3
                                .append(CollectionConstants.PIPE_SEPARATOR)
                                .append(paymentServiceDetails.getCode())
                                // txtAdditionalInfo4
                                .append(CollectionConstants.PIPE_SEPARATOR)
                                .append(receiptHeader.getService().getCode())
                                // txtAdditionalInfo5
                                .append(CollectionConstants.PIPE_SEPARATOR).append(CollectionConstants.PAYMENT_REQUEST_MSG_NA)
                                .append(CollectionConstants.PIPE_SEPARATOR).append(CollectionConstants.PAYMENT_REQUEST_MSG_NA)
                                .append(CollectionConstants.PIPE_SEPARATOR)
                                .append(paymentServiceDetails.getCallBackurl() + "?serviceCode=")
                                .append(paymentServiceDetails.getCode()); // RU

        final String checkSumValue = null;// =
        // PGIUtil.doDigest(paymentReqMsg.toString(),CollectionConstants.UNIQUE_CHECKSUM_KEY);
        paymentReqMsg.append(CollectionConstants.PIPE_SEPARATOR).append(checkSumValue);

        paymentRequest.setParameter(CollectionConstants.PAYMENT_REQUEST_MESSAGE_KEY, paymentReqMsg.toString());
        paymentRequest
        .setParameter(CollectionConstants.ONLINEPAYMENT_INVOKE_URL, paymentServiceDetails.getServiceUrl());

        LOGGER.info("paymentRequest: " + paymentRequest.toString());
        return paymentRequest;
    }

    /**
     * This method parses the given response string into a bill desk payment
     * response object.
     *
     * @param a
     *            <code>String</code> representation of the response.
     * @return an instance of <code></code> containing the response information
     */
    @Override
    public PaymentResponse parsePaymentResponse(final String response) {

        final String[] messages = response.split("\\|", -1);

        final String errorKeyPrefix = messages[20] + ".pgi." + messages[19];
        if (!isValidChecksum(response.substring(0, response.lastIndexOf('|')), messages[25])) {
            LOGGER.error("Error occured due to check sum mismatch");
            throw new ApplicationRuntimeException(errorKeyPrefix + ".checksum.mismatch");
        }

        final PaymentResponse billDeskResponse = new DefaultPaymentResponse();

        billDeskResponse.setCustomerId(messages[1]);
        billDeskResponse.setTxnReferenceNo(messages[2]);
        billDeskResponse.setTxnAmount(new BigDecimal(messages[4]));

        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date transactionDate = null;
        try {
            transactionDate = sdf.parse(messages[13]);
        } catch (final ParseException e) {
            LOGGER.error("Error occured in parsing the transaction date [" + messages[13] + "]", e);

            throw new ApplicationRuntimeException(errorKeyPrefix + ".transactiondate.parse.error", e);
        }
        billDeskResponse.setTxnDate(transactionDate);

        billDeskResponse.setAuthStatus(messages[14]);
        billDeskResponse.setReceiptId(messages[16]);
        billDeskResponse.setChecksum(messages[25]);

        return billDeskResponse;
    }

    public boolean isValidChecksum(final String testString, final String testChecksum) {
        final String actualChecksum = null;// =
        // PGIUtil.doDigest(testString,CollectionConstants.UNIQUE_CHECKSUM_KEY);
        return actualChecksum.equals(testChecksum);
    }
}
