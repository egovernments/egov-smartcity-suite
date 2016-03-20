/**
 * eGov suite of products aim to improve the internal efficiency,transparency, accountability and the service delivery of the
 * government organizations.
 *
 * Copyright (C) <2015> eGovernments Foundation
 *
 * The updated version of eGov suite of products as by eGovernments Foundation is available at http://www.egovernments.org
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * http://www.gnu.org/licenses/ or http://www.gnu.org/licenses/gpl.html .
 *
 * In addition to the terms of the GPL license to be adhered to in using this program, the following additional terms are to be
 * complied with:
 *
 * 1) All versions of this program, verbatim or modified must carry this Legal Notice.
 *
 * 2) Any misrepresentation of the origin of the material is prohibited. It is required that all modified versions of this
 * material be marked in reasonable ways as different from the original version.
 *
 * 3) This license does not grant any rights to any user of the program with regards to rights under trademark law for use of the
 * trade names or trademarks of eGovernments Foundation.
 *
 * In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.collection.integration.pgi;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.egov.collection.config.properties.CollectionApplicationProperties;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.OnlinePayment;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infstr.models.ServiceDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

// import com.billdesk.pgidsk.PGIUtil;

/**
 * The PaymentRequestAdaptor class frames the request object for the payment
 * service.
 */
@Service
public class AxisAdaptor implements PaymentGatewayAdaptor {

    private static final Logger LOGGER = Logger.getLogger(AxisAdaptor.class);
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CollectionApplicationProperties collectionApplicationProperties;
    public static final BigDecimal PAISE_RUPEE_CONVERTER = new BigDecimal(100);

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
        LOGGER.debug("inside createPaymentRequest");
        final Map<String, String> fields = new HashMap<String, String>(0);
        fields.put(CollectionConstants.AXIS_VERSION, collectionApplicationProperties.axisVersion().toString());
        fields.put(CollectionConstants.AXIS_COMMAND, collectionApplicationProperties.axisCommand());
        fields.put(CollectionConstants.AXIS_ACCESS_CODE, collectionApplicationProperties.axisAccessCode());
        fields.put(CollectionConstants.AXIS_MERCHANT_TXN_REF, receiptHeader.getId().toString());
        fields.put(CollectionConstants.AXIS_MERCHANT, collectionApplicationProperties.axisMerchant());
        fields.put(CollectionConstants.AXIS_LOCALE, collectionApplicationProperties.axisLocale());
        fields.put(CollectionConstants.AXIS_TICKET_NO, receiptHeader.getConsumerCode());
        fields.put(CollectionConstants.AXIS_ORDER_INFO,
                EgovThreadLocals.getCityCode() + "-" + EgovThreadLocals.getCityName());
        final StringBuilder returnUrl = new StringBuilder();
        returnUrl.append(paymentServiceDetails.getCallBackurl()).append("?paymentServiceId=")
        .append(paymentServiceDetails.getId());
        fields.put(CollectionConstants.AXIS_RETURN_URL, returnUrl.toString());
        final BigDecimal amount = receiptHeader.getTotalAmount();
        final float rupees = Float.parseFloat(amount.toString());
        final Integer rupee = (int) rupees;
        final Float exponent = rupees - (float) rupee;
        final Integer paise = (int) (rupee * PAISE_RUPEE_CONVERTER.intValue() + exponent * PAISE_RUPEE_CONVERTER.intValue());
        fields.put(CollectionConstants.AXIS_AMOUNT, paise.toString());
        final String axisSecureSecret = collectionApplicationProperties.axisSecureSecret();
        if (axisSecureSecret != null) {
            final String secureHash = hashAllFields(fields);
            fields.put(CollectionConstants.AXIS_SECURE_HASH, secureHash);
        }
        final StringBuffer buf = new StringBuffer();
        buf.append(paymentServiceDetails.getServiceUrl()).append('?');
        appendQueryFields(buf, fields);
        paymentRequest.setParameter(CollectionConstants.ONLINEPAYMENT_INVOKE_URL, buf);
        LOGGER.info("paymentRequest: " + paymentRequest.getRequestParameters());
        return paymentRequest;
    }

    String hashAllFields(final Map<String, String> fields) {

        // create a list and sort it
        final List<String> fieldNames = new ArrayList<String>(fields.keySet());
        Collections.sort(fieldNames);

        // create a buffer for the md5 input and add the secure secret first
        final StringBuffer buf = new StringBuffer();
        final String axisSecureSecret = collectionApplicationProperties.axisSecureSecret();
        buf.append(axisSecureSecret);

        // iterate through the list and add the remaining field values
        final Iterator<String> itr = fieldNames.iterator();

        while (itr.hasNext()) {
            final String fieldName = itr.next();
            final String fieldValue = fields.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0)
                buf.append(fieldValue);
        }

        MessageDigest md5 = null;
        byte[] ba = null;

        // create the md5 hash and UTF-8 encode it
        try {
            md5 = MessageDigest.getInstance("MD5");
            ba = md5.digest(buf.toString().getBytes("UTF-8"));
        } catch (final Exception e) {
        } // wont happen

        // return buf.toString();
        return hex(ba);

    } // end hashAllFields()

    /**
     * Returns Hex output of byte array
     */
    static String hex(final byte[] input) {
        // create a StringBuffer 2x the size of the hash array
        final StringBuffer sb = new StringBuffer(input.length * 2);

        // retrieve the byte array data, convert it to hex
        // and add it to the StringBuffer
        for (final byte element : input) {
            sb.append(CollectionConstants.AXIS_HEX_TABLE[element >> 4 & 0xf]);
            sb.append(CollectionConstants.AXIS_HEX_TABLE[element & 0xf]);
        }
        return sb.toString();
    }

    /**
     * This method parses the given response string into a AXIS payment response
     * object.
     *
     * @param a
     *            <code>String</code> representation of the response.
     * @return an instance of <code></code> containing the response information
     */
    @Override
    public PaymentResponse parsePaymentResponse(final String response) {
        LOGGER.info("Response message from Axis Payment gateway: " + response);
        final PaymentResponse axisResponse = new DefaultPaymentResponse();
        final String[] keyValueStr = response.replace("{", "").replace("}", "").split(",");
        final Map<String, String> fields = new HashMap<String, String>(0);

        for (final String pair : keyValueStr) {
            final String[] entry = pair.split("=");
            if (entry.length == 2)
                fields.put(entry[0].trim(), entry[1].trim());
        }
        // AXIS Payment Gateway returns Response Code 0(Zero) for successful
        // transactions, so converted it to 0300
        // as that is being followed as a standard in other payment gateways.
        final String receiptId = fields.get("vpc_MerchTxnRef");
        final Query qry = entityManager.createQuery("from ReceiptHeader rh where rh.id =?");
        qry.setParameter(1, Long.valueOf(receiptId));
        final ReceiptHeader receiptHeader = (ReceiptHeader) qry.getSingleResult();
        axisResponse.setAuthStatus(fields.get(CollectionConstants.AXIS_TXN_RESPONSE_CODE).equals("0") ? "0300" : fields
                .get(CollectionConstants.AXIS_TXN_RESPONSE_CODE));
        axisResponse.setErrorDescription(fields.get(CollectionConstants.AXIS_RESP_MESSAGE));
        axisResponse.setAdditionalInfo6(receiptHeader.getConsumerCode());
        axisResponse.setReceiptId(receiptId);
        axisResponse.setTxnAmount(new BigDecimal(fields.get(CollectionConstants.AXIS_AMOUNT)).divide(PAISE_RUPEE_CONVERTER));
        axisResponse.setTxnReferenceNo(fields.get(CollectionConstants.AXIS_TXN_NO));

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        Date transactionDate = null;
        try {
            transactionDate = sdf.parse(fields.get(CollectionConstants.AXIS_BATCH_NO));
            axisResponse.setTxnDate(transactionDate);
        } catch (final ParseException e) {
            LOGGER.error("Error occured in parsing the transaction date [" + fields.get("vpc_BatchNo") + "]", e);
            throw new ApplicationRuntimeException(".transactiondate.parse.error", e);
        }

        /*
         * If there has been a merchant secret set then sort and loop through
         * all the data in the Virtual Payment Client response. while we have
         * the data, we can append all the fields that contain values (except
         * the secure hash) so that we can create a hash and validate it against
         * the secure hash in the Virtual Payment Client response. NOTE: If the
         * vpc_TxnResponseCode in not a single character then there was a
         * Virtual Payment Client error and we cannot accurately validate the
         * incoming data from the secure hash.
         */

        // remove the vpc_TxnResponseCode code from the response fields as we do
        // not
        // want to include this field in the hash calculation
        final String vpc_Txn_Secure_Hash = null2unknown(fields.remove("vpc_SecureHash"));
        // defines if error message should be output
        boolean errorExists = false;
        final String axisSecureSecret = collectionApplicationProperties.axisSecureSecret();
        if (axisSecureSecret != null
                && (fields.get(CollectionConstants.AXIS_TXN_RESPONSE_CODE) != null || fields
                .get(CollectionConstants.AXIS_TXN_RESPONSE_CODE) != "No Value Returned")) {

            // create secure hash and append it to the hash map if it was
            // created
            // remember if SECURE_SECRET = "" it wil not be created
            final String secureHash = hashAllFields(fields);

            // Validate the Secure Hash (remember MD5 hashes are not case
            // sensitive)
            if (vpc_Txn_Secure_Hash.equalsIgnoreCase(secureHash)) {
            } else
                // Secure Hash validation failed, add a data field to be
                // displayed later.
                errorExists = true;
        } else {
        }

        null2unknown(fields.get("Title"));
        null2unknown(fields.get("AgainLink"));
        null2unknown(fields.get("vpc_Amount"));
        null2unknown(fields.get("vpc_Locale"));
        null2unknown(fields.get("vpc_BatchNo"));
        null2unknown(fields.get("vpc_Command"));
        null2unknown(fields.get("vpc_Message"));
        null2unknown(fields.get("vpc_Version"));
        null2unknown(fields.get("vpc_Card"));
        null2unknown(fields.get("vpc_OrderInfo"));
        null2unknown(fields.get("vpc_ReceiptNo"));
        null2unknown(fields.get("vpc_Merchant"));
        null2unknown(fields.get("vpc_MerchTxnRef"));
        null2unknown(fields.get("vpc_AuthorizeId"));
        null2unknown(fields.get("vpc_TransactionNo"));
        null2unknown(fields.get("vpc_AcqResponseCode"));
        final String txnResponseCode = null2unknown(fields.get("vpc_TxnResponseCode"));

        null2unknown(fields.get("vpc_CSCResultCode"));
        null2unknown(fields.get("vpc_CSCRequestCode"));
        null2unknown(fields.get("vpc_AcqCSCRespCode"));

        null2unknown(fields.get("vpc_AVS_City"));
        null2unknown(fields.get("vpc_AVS_Country"));
        null2unknown(fields.get("vpc_AVS_Street01"));
        null2unknown(fields.get("vpc_AVS_PostCode"));
        null2unknown(fields.get("vpc_AVS_StateProv"));
        null2unknown(fields.get("vpc_AVSResultCode"));
        null2unknown(fields.get("vpc_AVSRequestCode"));
        null2unknown(fields.get("vpc_AcqAVSRespCode"));

        null2unknown(fields.get("vpc_VerType"));
        null2unknown(fields.get("vpc_VerStatus"));
        null2unknown(fields.get("vpc_VerToken"));
        null2unknown(fields.get("vpc_VerSecurityLevel"));
        null2unknown(fields.get("vpc_3DSenrolled"));
        null2unknown(fields.get("vpc_3DSXID"));
        null2unknown(fields.get("vpc_3DSECI"));
        null2unknown(fields.get("vpc_3DSstatus"));

        // Show this page as an error page if error condition
        if (txnResponseCode.equals("7") || txnResponseCode.equals("No Value Returned") || errorExists) {
        }

        // FINISH TRANSACTION - Process the VPC Response Data

        return axisResponse;
    }

    /*
     * This method takes a data String and returns a predefined value if empty
     * If data Sting is null, returns string "No Value Returned", else returns
     * input
     * @param in String containing the data String
     * @return String containing the output String
     */
    private static String null2unknown(final String in) {
        if (in == null || in.length() == 0)
            return "No Value Returned";
        else
            return in;
    } // null2unknown()

    /**
     * This method is for creating a URL query string.
     *
     * @param buf
     *            is the inital URL for appending the encoded fields to
     * @param fields
     *            is the input parameters from the order page
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    // Method for creating a URL query string
    void appendQueryFields(final StringBuffer buf, final Map fields) {

        // create a list
        final List fieldNames = new ArrayList(fields.keySet());
        final Iterator itr = fieldNames.iterator();

        // move through the list and create a series of URL key/value pairs
        while (itr.hasNext()) {
            final String fieldName = (String) itr.next();
            final String fieldValue = (String) fields.get(fieldName);

            if (fieldValue != null && fieldValue.length() > 0)
                // append the URL parameters
                try {
                    buf.append(URLEncoder.encode(fieldName, "UTF-8"));
                    buf.append('=');
                    buf.append(URLEncoder.encode(fieldValue, "UTF-8"));
                } catch (final UnsupportedEncodingException e) {
                    throw new RuntimeException(e.getMessage());
                }
            // add a '&' to the end if we have more fields coming.
            if (itr.hasNext())
                buf.append('&');
        }
    } // appendQueryFields()

    public PaymentResponse createOfflinePaymentRequest(final ServiceDetails paymentServiceDetails,
            final OnlinePayment onlinePayment) {
        LOGGER.debug("Inside createOfflinePaymentRequest");

        final PaymentResponse axisResponse = new DefaultPaymentResponse();
        try {
            final Client client = Client.create();
            final WebResource resource = client.resource(collectionApplicationProperties.axisReconcileUrl());
            final Form formData = new Form();
            formData.add(CollectionConstants.AXIS_VERSION, collectionApplicationProperties.axisVersion());
            formData.add(CollectionConstants.AXIS_COMMAND, collectionApplicationProperties.axisCommandQuery());
            formData.add(CollectionConstants.AXIS_ACCESS_CODE, collectionApplicationProperties.axisAccessCode());
            formData.add(CollectionConstants.AXIS_MERCHANT, collectionApplicationProperties.axisMerchant());
            formData.add(CollectionConstants.AXIS_MERCHANT_TXN_REF, onlinePayment.getReceiptHeader().getId().toString());
            formData.add(CollectionConstants.AXIS_OPERATOR_ID, collectionApplicationProperties.axisOperator());
            formData.add(CollectionConstants.AXIS_PASSWORD, collectionApplicationProperties.axisPassword());
            final String responseAxis = resource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(String.class,
                    formData);
            final Map<String, String> responseAxisMap = new LinkedHashMap<String, String>();
            final String[] pairs = responseAxis.split("&");
            for (final String pair : pairs) {
                final int idx = pair.indexOf("=");
                try {
                    responseAxisMap.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                            URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
                } catch (final UnsupportedEncodingException e) {
                    LOGGER.error("Error Decoding Axis Bank Response" + e.getMessage());
                }
            }
            axisResponse.setAdditionalInfo6(onlinePayment.getReceiptHeader().getConsumerCode().replace("-", "")
                    .replace("/", ""));
            LOGGER.info("ResponseAXIS: " + responseAxis.toString());
            if (null != responseAxisMap.get(CollectionConstants.AXIS_TXN_RESPONSE_CODE)
                    && !"".equals(responseAxisMap.get(CollectionConstants.AXIS_TXN_RESPONSE_CODE))) {
                axisResponse.setAuthStatus(null != responseAxisMap.get(CollectionConstants.AXIS_TXN_RESPONSE_CODE)
                        && responseAxisMap.get(CollectionConstants.AXIS_TXN_RESPONSE_CODE).equals("0") ? CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS
                                : responseAxisMap.get(CollectionConstants.AXIS_TXN_RESPONSE_CODE));
                axisResponse.setErrorDescription(responseAxisMap.get(CollectionConstants.AXIS_RESP_MESSAGE));
                axisResponse.setReceiptId(responseAxisMap.get(CollectionConstants.AXIS_MERCHANT_TXN_REF));
                if (CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS.equals(axisResponse.getAuthStatus())) {
                    axisResponse.setTxnReferenceNo(responseAxisMap.get(CollectionConstants.AXIS_TXN_NO));
                    axisResponse.setTxnAmount(new BigDecimal(responseAxisMap.get(CollectionConstants.AXIS_AMOUNT)));
                    axisResponse.setAdditionalInfo2(responseAxisMap.get(CollectionConstants.AXIS_ORDER_INFO));
                    final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                    Date transactionDate = null;
                    try {
                        transactionDate = sdf.parse(responseAxisMap.get(CollectionConstants.AXIS_BATCH_NO));
                        axisResponse.setTxnDate(transactionDate);
                    } catch (final ParseException e) {
                        LOGGER.error(
                                "Error occured in parsing the transaction date [" + responseAxisMap.get(CollectionConstants.AXIS_BATCH_NO)
                                + "]", e);
                        throw new ApplicationException(".transactiondate.parse.error", e);
                    }
                }
            }
            LOGGER.debug("receiptid=" + axisResponse.getReceiptId() + "consumercode="
                    + axisResponse.getAdditionalInfo6());
        } catch (final Exception exp) {
            exp.printStackTrace();
            LOGGER.error(exp.getMessage());
        }
        return axisResponse;
    }

}
