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
package org.egov.collection.integration.pgi;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.egov.collection.config.properties.CollectionApplicationProperties;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.OnlinePayment;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.models.ServiceDetails;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.JsonObject;

/**
 * The PaymentRequestAdaptor class frames the request object for the payment service.
 */
public class SbimopsAdaptor implements PaymentGatewayAdaptor {
    private static final Logger LOGGER = Logger.getLogger(SbimopsAdaptor.class);
    private static final String REQUEST_CONTENT_TYPE = "application/json";

    public static final String SBIMOPS_DC = "DC";
    public static final String SBIMOPS_DTID = "DTID";
    public static final String SBIMOPS_RN = "RN";
    public static final String SBIMOPS_RID = "RID";
    public static final String SBIMOPS_TA = "TA";
    public static final String SBIMOPS_CH = "Ch";
    public static final String SBIMOPS_RURL = "RUrl";
    public static final String SBIMOPS_CFMS_TRID = "CFMS_TRID";
    public static final String SBIMOPS_BANKTIME_STAMP = "BankTimeStamp";
    public static final String SBIMOPS_STATUS = "Status";

    // SBIMOPS reconciliation parameters name
    public static final String SBIMOPS_DEPTCODE = "DEPTCODE";
    public static final String SBIMOPS_DEPTTID = "DEPTTID";
    public static final String SBIMOPS_ROW = "ROW";
    public static final String SBIMOPS_RECORDSET = "RECORDSET";
    public static final String SBIMOPS_CFMSID = "CFMSID";
    public static final String SBIMOPS_TAMT = "TAMT";
    public static final String SBIMOPS_BNKDT = "BNKDT";
    private static final String UTF8 = "UTF-8";

    public static final String MESSAGEKEY_SBIMOPS_DC = "sbimops.department.code";

    private static final ArrayList<String> SBIMOPS_CODES_WAITINGFOR_PG_RESPONSE = new ArrayList<String>() {
        {
            add("PENDING");
            add("P");
            add("Z");
        }
    };

    @Autowired
    private CollectionApplicationProperties collectionApplicationProperties;

    /**
     * This method invokes APIs to frame request object for the payment service passed as parameter
     *
     * @param serviceDetails
     * @param receiptHeader
     * @return
     */
    @Override
    public PaymentRequest createPaymentRequest(final ServiceDetails paymentServiceDetails, final ReceiptHeader receiptHeader) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" Inside SbimopsAdaptor-createPaymentRequest ");
        final String billServiceCode = receiptHeader.getService().getCode();
        final DefaultPaymentRequest sbiPaymentRequest = new DefaultPaymentRequest();
        final LinkedHashMap<String, String> requestParameterMap = new LinkedHashMap<>();
        requestParameterMap.put(SBIMOPS_DC,
                collectionApplicationProperties.sbimopsDepartmentcode(MESSAGEKEY_SBIMOPS_DC));
        StringBuilder transactionId = new StringBuilder(receiptHeader.getId().toString())
                .append(CollectionConstants.SEPARATOR_HYPHEN)
                .append(receiptHeader.getConsumerCode().replace("-", "").replace("/", ""));
        requestParameterMap.put(SBIMOPS_DTID, transactionId.toString());
        requestParameterMap.put(SBIMOPS_RN, receiptHeader.getPayeeName());
        requestParameterMap.put(SBIMOPS_RID, receiptHeader.getConsumerCode());
        requestParameterMap.put(SBIMOPS_TA, receiptHeader.getTotalAmount().toString());
        final StringBuilder chStringBuilder = new StringBuilder(
                collectionApplicationProperties.sbimopsHoa(ApplicationThreadLocals.getCityCode(), billServiceCode));
        chStringBuilder.append(CollectionConstants.SEPARATOR_COMMA)
                .append(collectionApplicationProperties.sbimopsDdocode(ApplicationThreadLocals.getCityCode()))
                .append(CollectionConstants.SEPARATOR_COMMA)
                .append(collectionApplicationProperties.sbimopsServiceCode(billServiceCode))
                .append(CollectionConstants.SEPARATOR_COMMA)
                .append(receiptHeader.getTotalAmount().toString());
        requestParameterMap.put(SBIMOPS_CH, chStringBuilder.toString());
        final StringBuilder returnUrl = new StringBuilder(paymentServiceDetails.getCallBackurl());
        returnUrl.append("?paymentServiceId=").append(paymentServiceDetails.getId());
        requestParameterMap.put(SBIMOPS_RURL, returnUrl.toString());
        final StringBuilder requestURL = new StringBuilder(paymentServiceDetails.getServiceUrl()).append("?");
        appendQueryFields(requestURL, requestParameterMap);
        LOGGER.info(SBIMOPS_DC + "=" + requestParameterMap.get(SBIMOPS_DC) + "|" +
                SBIMOPS_DTID + "=" + requestParameterMap.get(SBIMOPS_DTID) + "|" +
                SBIMOPS_RN + "=" + requestParameterMap.get(SBIMOPS_RN) + "|" +
                SBIMOPS_RID + "=" + requestParameterMap.get(SBIMOPS_RID) + "|" +
                SBIMOPS_TA + "=" + requestParameterMap.get(SBIMOPS_TA)
                + "|" +
                SBIMOPS_CH + "=" + requestParameterMap.get(SBIMOPS_CH)
                + "|" +
                SBIMOPS_RURL + "=" + requestParameterMap.get(SBIMOPS_RURL) + "|" +
                CollectionConstants.ONLINEPAYMENT_INVOKE_URL + "="
                + requestParameterMap.get(CollectionConstants.ONLINEPAYMENT_INVOKE_URL));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("End SbimopsAdaptor-createPaymentRequest");
        sbiPaymentRequest.setParameter(CollectionConstants.ONLINEPAYMENT_INVOKE_URL, requestURL);
        LOGGER.info("SBIMOPS request URL:" + sbiPaymentRequest.getRequestParameters());
        return sbiPaymentRequest;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void appendQueryFields(final StringBuilder requestURL, final LinkedHashMap<String, String> fields) {
        final List fieldNames = new ArrayList(fields.keySet());
        final Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            final String fieldName = (String) itr.next();
            final String fieldValue = fields.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0)
                // append the URL parameters
                try {
                requestURL.append(URLEncoder.encode(fieldName, UTF8));
                requestURL.append('=');
                requestURL.append(URLEncoder.encode(fieldValue, UTF8));
                } catch (final UnsupportedEncodingException e) {
                LOGGER.error("Error appending QueryFields", e);
                throw new ApplicationRuntimeException(e.getMessage());
                }
            if (itr.hasNext())
                requestURL.append('&');
        }
    }

    @Override
    public PaymentResponse parsePaymentResponse(final String response) {
        final DefaultPaymentResponse sbiPaymentResponse = new DefaultPaymentResponse();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Insider SbimopsAdaptor-parsePaymentResponse");

        if (isNotBlank(response)) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Sbimops realtime transaction response : " + response);
            final String[] keyValueStr = response.replace("{", "").replace("}", "").split(",");
            final LinkedHashMap<String, String> responseMap = new LinkedHashMap<>(0);

            for (final String pair : keyValueStr) {
                final String[] entry = pair.split("=");
                if (entry.length == 2)
                    responseMap.put(entry[0].trim(), entry[1].trim());
            }

            if (responseMap.containsKey(SBIMOPS_STATUS) && responseMap.containsKey(SBIMOPS_DTID)) {
                sbiPaymentResponse.setAuthStatus(getTransactionStatus(responseMap.get(SBIMOPS_STATUS)));
                sbiPaymentResponse.setErrorDescription(responseMap.get(SBIMOPS_STATUS));
                final String[] consumercodeTransactionId = responseMap.get(SBIMOPS_DTID)
                        .split(CollectionConstants.SEPARATOR_HYPHEN);
                sbiPaymentResponse.setReceiptId(consumercodeTransactionId[0]);
                sbiPaymentResponse.setAdditionalInfo6(consumercodeTransactionId[1]);

                if (sbiPaymentResponse.getAuthStatus().equals(CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS)) {
                    sbiPaymentResponse.setTxnAmount(new BigDecimal(responseMap.get(SBIMOPS_TA)));
                    sbiPaymentResponse.setTxnReferenceNo(responseMap.get(SBIMOPS_CFMS_TRID));
                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault());
                    Date transDate = null;
                    try {
                        transDate = simpleDateFormat.parse(responseMap.get(SBIMOPS_BANKTIME_STAMP));
                        sbiPaymentResponse.setTxnDate(transDate);
                    } catch (final ParseException e) {
                        LOGGER.error(
                                "Error in parsing transaction date [" + responseMap.get(SBIMOPS_BANKTIME_STAMP)
                                        + "]",
                                e);
                        throw new ApplicationRuntimeException(".transactiondate.parse.error", e);
                    }
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("End SbimopsAdaptor-parsePaymentResponse");
                }
            } else {
                LOGGER.error(" DTID/Status are not found in the response.Sbimops realtime transaction response : " + response);
                throw new ApplicationRuntimeException("Sbimops DTID/Status are not found in the response.");
            }
        } else {
            LOGGER.info("Sbimops relatime transaction response is null or empty");
            throw new ApplicationRuntimeException("SBIMOPS response is null or empty");
        }
        return sbiPaymentResponse;
    }

    public PaymentResponse createOfflinePaymentRequest(final OnlinePayment onlinePayment) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("inside sbimops :createOfflinePaymentRequest");
        final PaymentResponse sbimopsResponse = new DefaultPaymentResponse();

        CloseableHttpResponse response = reconciliationResponse(onlinePayment);

        if (response == null) {
            LOGGER.info("Sbimops reconciliation response is null");
            throw new ApplicationRuntimeException("SBIMOPS reconciliation response is null.");
        } else {
            final Map<String, String> responseParameterMap = prepareResponseMap(response);
            sbimopsResponse.setAdditionalInfo6(onlinePayment.getReceiptHeader().getConsumerCode().replace("-", "")
                    .replace("/", ""));
            sbimopsResponse.setReceiptId(onlinePayment.getReceiptHeader().getId().toString());
            if (responseParameterMap != null && responseParameterMap.containsKey(SBIMOPS_STATUS.toUpperCase())) {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("Sbimops reconciliation transaction response : " + responseParameterMap.toString());
                final String transactionStatus = responseParameterMap.get(SBIMOPS_STATUS.toUpperCase());
                sbimopsResponse.setAuthStatus(getTransactionStatus(transactionStatus));
                sbimopsResponse.setErrorDescription(transactionStatus);
                if (sbimopsResponse.getAuthStatus().equals(CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS)) {
                    sbimopsResponse
                            .setTxnAmount(new BigDecimal(Double.valueOf(responseParameterMap.get(SBIMOPS_TAMT))));
                    sbimopsResponse.setTxnReferenceNo(responseParameterMap.get(SBIMOPS_CFMSID).toString());
                    // CFMS is not sending the bank transaction date. Setting receipt date as transaction date.
                    sbimopsResponse.setTxnDate(onlinePayment.getReceiptHeader().getReceiptDate());
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("End SbimopsAdaptor-parsePaymentResponse");
                }
            } else {
                // If not getting the status in the response then keep the transaction as pending
                sbimopsResponse.setAuthStatus(getTransactionStatus(CollectionConstants.PGI_AUTHORISATION_CODE_PENDING));
                sbimopsResponse.setErrorDescription(CollectionConstants.PGI_AUTHORISATION_CODE_PENDING);
            }
            return sbimopsResponse;
        }

    }

    private CloseableHttpResponse reconciliationResponse(OnlinePayment onlinePayment) {
        CloseableHttpResponse response;
        try {
            final HttpPost httpPost = new HttpPost(collectionApplicationProperties.sbimopsReconcileUrl());
            StringEntity stringEntity = new StringEntity(prepeareReconciliationRequest(onlinePayment),
                    CollectionConstants.UTF_ENCODING);
            stringEntity.setContentType(REQUEST_CONTENT_TYPE);
            httpPost.setEntity(stringEntity);

            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
                    collectionApplicationProperties.sbimopsReconcileUsername(),
                    collectionApplicationProperties.sbimopsReconcilePassword());

            if (LOGGER.isInfoEnabled())
                LOGGER.info("Sbimops reconciliation URL:" + collectionApplicationProperties.sbimopsReconcileUrl()
                        + " |username: " + collectionApplicationProperties.sbimopsReconcileUsername()
                        + " |password: " + collectionApplicationProperties.sbimopsReconcilePassword());

            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, credentials);
            HttpClient client = HttpClientBuilder.create()
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .build();
            response = (CloseableHttpResponse) client.execute(httpPost);
        } catch (IOException e) {
            LOGGER.error(
                    "SBIMOPS reconciliation, error while sending the request for SBIMOPS reconciliation", e);
            throw new ApplicationRuntimeException(
                    "SBIMOPS reconciliation, error while sending the request for SBIMOPS reconciliation", e);
        }
        return response;
    }

    private String prepeareReconciliationRequest(final OnlinePayment onlinePayment) {
        final JsonObject deptCodeJson = new JsonObject();
        deptCodeJson.addProperty(SBIMOPS_DEPTCODE,
                collectionApplicationProperties.sbimopsDepartmentcode(MESSAGEKEY_SBIMOPS_DC));
        final JsonObject transactionIdJson = new JsonObject();
        StringBuilder transactionId = new StringBuilder(onlinePayment.getReceiptHeader().getId().toString())
                .append(CollectionConstants.SEPARATOR_HYPHEN)
                .append(onlinePayment.getReceiptHeader().getConsumerCode().replace("-", "").replace("/", ""));
        transactionIdJson.addProperty(SBIMOPS_DEPTTID, transactionId.toString());
        final JsonObject requestJson = new JsonObject();
        deptCodeJson.add(SBIMOPS_ROW, transactionIdJson);
        requestJson.add(SBIMOPS_RECORDSET, deptCodeJson);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("SBIMOPS reconciliation request parameters:" + requestJson.toString());
        return requestJson.toString();
    }

    private Map<String, String> prepareResponseMap(CloseableHttpResponse response) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(response.getEntity().getContent());
            BufferedReader reader = new BufferedReader(inputStreamReader);

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Map<String, Object>> responseMap = null;

            try {
                responseMap = objectMapper.readValue(reader.readLine(),
                        new TypeReference<Map<String, Map<String, Object>>>() {
                        });
            } finally {
                reader.close();
                inputStreamReader.close();
            }
            if (responseMap == null || responseMap.isEmpty()) {
                LOGGER.info("Sbimops reconciliation response is null or empty");
                throw new ApplicationRuntimeException("SBIMOPS reconciliation response is null or empty");
            } else {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("Sbimops reconciliation response : " + responseMap);
                Map<String, Object> responseParameterMap = (Map<String, Object>) responseMap.get(SBIMOPS_RECORDSET)
                        .get(SBIMOPS_ROW);
                final Map<String, String> responseSbimopsMap = new LinkedHashMap<>();
                responseParameterMap.forEach((key, value) -> responseSbimopsMap.put(key, value.toString()));
                return responseSbimopsMap;
            }
        } catch (JsonParseException e) {
            LOGGER.error("SBIMOPS reconciliation, error while parsing the response content", e);
        } catch (IOException e) {
            LOGGER.error("SBIMOPS reconciliation, error while reading the response content", e);
        }
        return null;
    }

    /**
     * Real time successful transaction status is "Success". Reconciliation successful transaction status is "S". Transactions
     * with statuses P and Pending are Pending transactions. Transaction statuses F, Failure and Invalid Department ID are failed
     * transaction
     * @param transactionStatus
     * @return Transaction Status <code>String</code>>
     */
    private String getTransactionStatus(String transactionStatus) {

        if (CollectionConstants.ONLINEPAYMENT_STATUS_DESC_SUCCESS.equalsIgnoreCase(transactionStatus)
                || "S".equalsIgnoreCase(transactionStatus))
            return CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS;
        else if (SBIMOPS_CODES_WAITINGFOR_PG_RESPONSE.contains(transactionStatus.trim().toUpperCase()))
            return CollectionConstants.PGI_AUTHORISATION_CODE_PENDING;
        else
            return CollectionConstants.PGI_AUTHORISATION_CODE_FAILED;
    }

    @Override
    public PaymentResponse repayReconciliation(OnlinePayment onlinePayment) {
        return createOfflinePaymentRequest(onlinePayment);
    }

}