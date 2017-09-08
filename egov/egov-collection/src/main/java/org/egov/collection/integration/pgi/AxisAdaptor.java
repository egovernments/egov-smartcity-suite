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
package org.egov.collection.integration.pgi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.egov.collection.config.properties.CollectionApplicationProperties;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.OnlinePayment;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.models.ServiceDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The PaymentRequestAdaptor class frames the request object for the payment
 * service.
 */
@Service
public class AxisAdaptor implements PaymentGatewayAdaptor {

    private static final Logger LOGGER = Logger.getLogger(AxisAdaptor.class);
    private static final BigDecimal PAISE_RUPEE_CONVERTER = BigDecimal.valueOf(100);
    private static final String UTF8 = "UTF-8";
    private static final String NO_VALUE_RETURNED = "No Value Returned";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CollectionApplicationProperties collectionApplicationProperties;

    @Autowired
    private CityService cityService;

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
        final Map<String, String> fields = new HashMap<>(0);
        fields.put(CollectionConstants.AXIS_VERSION, collectionApplicationProperties.axisVersion());
        fields.put(CollectionConstants.AXIS_COMMAND, collectionApplicationProperties.axisCommand());
        fields.put(CollectionConstants.AXIS_ACCESS_CODE, collectionApplicationProperties.axisAccessCode());
        fields.put(CollectionConstants.AXIS_MERCHANT_TXN_REF, ApplicationThreadLocals.getCityCode()
                + CollectionConstants.SEPARATOR_HYPHEN + receiptHeader.getId().toString());
        fields.put(CollectionConstants.AXIS_MERCHANT, collectionApplicationProperties.axisMerchant());
        fields.put(CollectionConstants.AXIS_LOCALE, collectionApplicationProperties.axisLocale());
        fields.put(CollectionConstants.AXIS_TICKET_NO, receiptHeader.getConsumerCode());
        fields.put(CollectionConstants.AXIS_ORDER_INFO, ApplicationThreadLocals.getCityCode()
                + CollectionConstants.SEPARATOR_HYPHEN + ApplicationThreadLocals.getCityName());
        final StringBuilder returnUrl = new StringBuilder();
        returnUrl.append(paymentServiceDetails.getCallBackurl()).append("?paymentServiceId=")
                .append(paymentServiceDetails.getId());
        fields.put(CollectionConstants.AXIS_RETURN_URL, returnUrl.toString());
        final BigDecimal amount = receiptHeader.getTotalAmount();
        final float rupees = Float.parseFloat(amount.toString());
        final Integer rupee = (int) rupees;
        final Float exponent = rupees - (float) rupee;
        final Integer paise = (int) (rupee * PAISE_RUPEE_CONVERTER.intValue()
                + exponent * PAISE_RUPEE_CONVERTER.intValue());
        fields.put(CollectionConstants.AXIS_AMOUNT, paise.toString());
        final String axisSecureSecret = collectionApplicationProperties.axisSecureSecret();
        if (axisSecureSecret != null) {
            final String secureHash = hashAllFields(fields);
            fields.put(CollectionConstants.AXIS_SECURE_HASH, secureHash);
        }
        final StringBuilder buf = new StringBuilder();
        buf.append(paymentServiceDetails.getServiceUrl()).append('?');
        appendQueryFields(buf, fields);
        paymentRequest.setParameter(CollectionConstants.ONLINEPAYMENT_INVOKE_URL, buf);
        LOGGER.info("paymentRequest: " + paymentRequest.getRequestParameters());
        return paymentRequest;
    }

    private String hashAllFields(final Map<String, String> fields) {

        // create a list and sort it
        final List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);

        // create a buffer for the md5 input and add the secure secret first
        final StringBuilder buf = new StringBuilder();
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
            ba = md5.digest(buf.toString().getBytes(UTF8));
        } catch (final Exception e) {
            LOGGER.error("Error in hashAllFields" + e);
        } // wont happen

        return hex(ba);

    } // end hashAllFields()

    /**
     * Returns Hex output of byte array
     */
    private static String hex(final byte[] input) {
        // create a StringBuilder 2x the size of the hash array
        final StringBuilder sb = new StringBuilder(input.length * 2);

        // retrieve the byte array data, convert it to hex
        // and add it to the StringBuilder
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
        final String[] keyValueStr = response.replace("{", "").replace("}", "").split(",");
        final Map<String, String> fields = new HashMap<>(0);

        for (final String pair : keyValueStr) {
            final String[] entry = pair.split("=");
            if (entry.length == 2)
                fields.put(entry[0].trim(), entry[1].trim());
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
        final String vpcTxnSecureHash = null2unknown(fields.remove(CollectionConstants.AXIS_SECURE_HASH));
        // defines if error message should be output
        final String axisSecureSecret = collectionApplicationProperties.axisSecureSecret();
        if (axisSecureSecret != null && (fields.get(CollectionConstants.AXIS_TXN_RESPONSE_CODE) != null
                || NO_VALUE_RETURNED.equals(fields.get(CollectionConstants.AXIS_TXN_RESPONSE_CODE)))) {

            // create secure hash and append it to the hash map if it was
            // created
            // remember if SECURE_SECRET = "" it wil not be created
            final String secureHash = hashAllFields(fields);

            // Validate the Secure Hash (remember MD5 hashes are not case
            // sensitive)
            if (!vpcTxnSecureHash.equalsIgnoreCase(secureHash)) {
                // Secure Hash validation failed, add a data field to be
                // displayed later.
                // throw new ApplicationRuntimeException("Axis Bank Payment
                // Secure Hash validation failed");
            }
        }
        return preparePaymentResponse(fields);
    }

    private PaymentResponse preparePaymentResponse(final Map<String, String> fields) {
        final PaymentResponse axisResponse = new DefaultPaymentResponse();
        try {
            // AXIS Payment Gateway returns Response Code 0(Zero) for successful
            // transactions, so converted it to 0300
            // as that is being followed as a standard in other payment
            // gateways.
            final String[] merchantRef = fields.get(CollectionConstants.AXIS_MERCHANT_TXN_REF)
                    .split(CollectionConstants.SEPARATOR_HYPHEN);
            final String receiptId = merchantRef[1];
            final String ulbCode = merchantRef[0];
            final ReceiptHeader receiptHeader;
            final Query qry = entityManager.createNamedQuery(CollectionConstants.QUERY_RECEIPT_BY_ID_AND_CITYCODE);
            qry.setParameter(1, Long.valueOf(receiptId));
            qry.setParameter(2, ulbCode);
            receiptHeader = (ReceiptHeader) qry.getSingleResult();
            axisResponse.setAuthStatus("0".equals(fields.get(CollectionConstants.AXIS_TXN_RESPONSE_CODE)) ? "0300"
                    : fields.get(CollectionConstants.AXIS_TXN_RESPONSE_CODE));
            axisResponse.setErrorDescription(fields.get(CollectionConstants.AXIS_RESP_MESSAGE));
            axisResponse.setAdditionalInfo6(receiptHeader.getConsumerCode().replace("-", "").replace("/", ""));
            axisResponse.setReceiptId(receiptId);
            axisResponse.setTxnAmount(
                    new BigDecimal(fields.get(CollectionConstants.AXIS_AMOUNT)).divide(PAISE_RUPEE_CONVERTER));
            axisResponse.setTxnReferenceNo(fields.get(CollectionConstants.AXIS_TXN_NO));
            axisResponse.setAdditionalInfo2(fields.get(CollectionConstants.AXIS_ORDER_INFO));
            axisResponse.setTxnDate(getTransactionDate(fields.get(CollectionConstants.AXIS_BATCH_NO)));
        } catch (final Exception exp) {
            LOGGER.error(exp);
            throw new ApplicationRuntimeException("Exception during prepare payment response" + exp.getMessage());
        }
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
            return NO_VALUE_RETURNED;
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
    private void appendQueryFields(final StringBuilder buf, final Map fields) {

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
                buf.append(URLEncoder.encode(fieldName, UTF8));
                buf.append('=');
                buf.append(URLEncoder.encode(fieldValue, UTF8));
                } catch (final UnsupportedEncodingException e) {
                LOGGER.error("Error appending QueryFields" + e);
                throw new ApplicationRuntimeException(e.getMessage());
                }
            // add a '&' to the end if we have more fields coming.
            if (itr.hasNext())
                buf.append('&');
        }
    } // appendQueryFields()

    @Transactional
    public PaymentResponse createOfflinePaymentRequest(final OnlinePayment onlinePayment) {
        LOGGER.debug("Inside createOfflinePaymentRequest");
        final PaymentResponse axisResponse = new DefaultPaymentResponse();
        try {
            final HttpPost httpPost = new HttpPost(collectionApplicationProperties.axisReconcileUrl());
            httpPost.setEntity(prepareEncodedFormEntity(onlinePayment));
            final CloseableHttpClient httpclient = HttpClients.createDefault();
            CloseableHttpResponse response;
            HttpEntity responseAxis;
            response = httpclient.execute(httpPost);
            LOGGER.debug("Response Status >>>>>" + response.getStatusLine());
            responseAxis = response.getEntity();
            final Map<String, String> responseAxisMap = prepareResponseMap(responseAxis.getContent());
            axisResponse.setAdditionalInfo6(
                    onlinePayment.getReceiptHeader().getConsumerCode().replace("-", "").replace("/", ""));
            axisResponse.setReceiptId(onlinePayment.getReceiptHeader().getId().toString());
            if (null != responseAxisMap.get(CollectionConstants.AXIS_TXN_RESPONSE_CODE)
                    && !"".equals(responseAxisMap.get(CollectionConstants.AXIS_TXN_RESPONSE_CODE))) {
                axisResponse.setAuthStatus(null != responseAxisMap.get(CollectionConstants.AXIS_TXN_RESPONSE_CODE)
                        && "0".equals(responseAxisMap.get(CollectionConstants.AXIS_TXN_RESPONSE_CODE))
                                ? CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS
                                : responseAxisMap.get(CollectionConstants.AXIS_TXN_RESPONSE_CODE));
                axisResponse.setErrorDescription(responseAxisMap.get(CollectionConstants.AXIS_RESP_MESSAGE));

                if (CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS.equals(axisResponse.getAuthStatus())) {
                    axisResponse.setTxnReferenceNo(responseAxisMap.get(CollectionConstants.AXIS_TXN_NO));
                    axisResponse.setTxnAmount(new BigDecimal(responseAxisMap.get(CollectionConstants.AXIS_AMOUNT))
                            .divide(PAISE_RUPEE_CONVERTER));
                    axisResponse.setAdditionalInfo2(responseAxisMap.get(CollectionConstants.AXIS_ORDER_INFO));
                    axisResponse.setTxnDate(getTransactionDate(responseAxisMap.get(CollectionConstants.AXIS_BATCH_NO)));
                }
            } else if (null != responseAxisMap.get(CollectionConstants.AXIS_CHECK_DR_EXISTS)
                    && "N".equals(responseAxisMap.get(CollectionConstants.AXIS_CHECK_DR_EXISTS))) {
                axisResponse.setErrorDescription(CollectionConstants.AXIS_FAILED_ABORTED_MESSAGE);
                axisResponse.setAuthStatus(CollectionConstants.AXIS_ABORTED_AUTH_STATUS);
            }
            LOGGER.debug(
                    "receiptid=" + axisResponse.getReceiptId() + "consumercode=" + axisResponse.getAdditionalInfo6());
        } catch (final Exception exp) {
            LOGGER.error(exp);
            throw new ApplicationRuntimeException("Exception during create offline requests" + exp.getMessage());
        }
        return axisResponse;
    }

    private UrlEncodedFormEntity prepareEncodedFormEntity(final OnlinePayment onlinePayment) {
        final List<NameValuePair> formData = new ArrayList<>();
        formData.add(new BasicNameValuePair(CollectionConstants.AXIS_VERSION,
                collectionApplicationProperties.axisVersion()));
        formData.add(new BasicNameValuePair(CollectionConstants.AXIS_COMMAND,
                collectionApplicationProperties.axisCommandQuery()));
        formData.add(new BasicNameValuePair(CollectionConstants.AXIS_ACCESS_CODE,
                collectionApplicationProperties.axisAccessCode()));
        formData.add(new BasicNameValuePair(CollectionConstants.AXIS_MERCHANT,
                collectionApplicationProperties.axisMerchant()));
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        formData.add(new BasicNameValuePair(CollectionConstants.AXIS_MERCHANT_TXN_REF, cityWebsite.getCode()
                + CollectionConstants.SEPARATOR_HYPHEN + onlinePayment.getReceiptHeader().getId().toString()));
        formData.add(new BasicNameValuePair(CollectionConstants.AXIS_OPERATOR_ID,
                collectionApplicationProperties.axisOperator()));
        formData.add(new BasicNameValuePair(CollectionConstants.AXIS_PASSWORD,
                collectionApplicationProperties.axisPassword()));
        formData.add(new BasicNameValuePair(CollectionConstants.AXIS_ORDER_INFO,
                cityWebsite.getCode() + CollectionConstants.SEPARATOR_HYPHEN + cityWebsite.getName()));
        UrlEncodedFormEntity urlEncodedFormEntity = null;
        try {
            urlEncodedFormEntity = new UrlEncodedFormEntity(formData);
        } catch (final UnsupportedEncodingException e1) {
            LOGGER.error("Error in Create Offline Payment Request" + e1);
        }
        return urlEncodedFormEntity;
    }

    private Date getTransactionDate(final String transDate) throws ApplicationException {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        try {
            return sdf.parse(transDate);
        } catch (final ParseException e) {
            LOGGER.error("Error occured in parsing the transaction date [" + transDate + "]", e);
            throw new ApplicationException(".transactiondate.parse.error", e);
        }
    }

    private Map<String, String> prepareResponseMap(final InputStream responseContent) {
        String[] pairs;
        final BufferedReader reader = new BufferedReader(new InputStreamReader(responseContent));
        final StringBuilder data = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null)
                data.append(line);
            reader.close();
        } catch (final IOException e) {
            LOGGER.error("Error Reading InsputStrem from Axis Bank Response" + e);
        }
        LOGGER.info("ResponseAXIS: " + data.toString());
        pairs = data.toString().split("&");
        final Map<String, String> responseAxisMap = new LinkedHashMap<>();
        for (final String pair : pairs) {
            final int idx = pair.indexOf('=');
            try {
                responseAxisMap.put(URLDecoder.decode(pair.substring(0, idx), UTF8),
                        URLDecoder.decode(pair.substring(idx + 1), UTF8));
            } catch (final UnsupportedEncodingException e) {
                LOGGER.error("Error Decoding Axis Bank Response" + e);
            }
        }
        return responseAxisMap;
    }

}
