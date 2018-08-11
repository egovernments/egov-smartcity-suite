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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
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
    private static final String SBIMOPS_HOA_FORMAT = "%-19sVN";
    private static final String REQUEST_CONTENT_TYPE = "application/json";

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
        final DefaultPaymentRequest sbiPaymentRequest = new DefaultPaymentRequest();
        sbiPaymentRequest.setParameter(CollectionConstants.SBIMOPS_DC,
                collectionApplicationProperties.sbimopsDepartmentcode(CollectionConstants.MESSAGEKEY_SBIMOPS_DC));
        StringBuilder transactionId = new StringBuilder(receiptHeader.getConsumerCode())
                .append(CollectionConstants.SEPARATOR_HYPHEN).append(receiptHeader.getId());
        sbiPaymentRequest.setParameter(CollectionConstants.SBIMOPS_DTID, transactionId.toString());
        sbiPaymentRequest.setParameter(CollectionConstants.SBIMOPS_RN, receiptHeader.getPayeeName());
        sbiPaymentRequest.setParameter(CollectionConstants.SBIMOPS_RID, receiptHeader.getConsumerCode());
        sbiPaymentRequest.setParameter(CollectionConstants.SBIMOPS_TA, receiptHeader.getTotalAmount());
        final StringBuilder chStringBuilder = new StringBuilder((String.format(SBIMOPS_HOA_FORMAT,
                collectionApplicationProperties.sbimopsHoa(ApplicationThreadLocals.getCityCode()))).replace(' ', '0'));
        chStringBuilder.append(CollectionConstants.SEPARATOR_COMMA)
                .append(collectionApplicationProperties.sbimopsDdocode(ApplicationThreadLocals.getCityCode()).toString())
                .append(CollectionConstants.SEPARATOR_COMMA)
                .append(collectionApplicationProperties.sbimopsServiceCode(receiptHeader.getService().getCode()))
                .append(CollectionConstants.SEPARATOR_COMMA)
                .append(receiptHeader.getTotalAmount().toString());
        sbiPaymentRequest.setParameter(CollectionConstants.SBIMOPS_CH, chStringBuilder.toString());
        final StringBuilder returnUrl = new StringBuilder(paymentServiceDetails.getCallBackurl());
        returnUrl.append("?paymentServiceId=").append(paymentServiceDetails.getId());
        sbiPaymentRequest.setParameter(CollectionConstants.SBIMOPS_RURL, returnUrl.toString());

        sbiPaymentRequest.setParameter(CollectionConstants.ONLINEPAYMENT_INVOKE_URL, paymentServiceDetails.getServiceUrl());
        final Map<String, Object> requestParameters = sbiPaymentRequest.getRequestParameters();
        LOGGER.info(CollectionConstants.SBIMOPS_DC + "=" + requestParameters.get(CollectionConstants.SBIMOPS_DC) + "|" +
                CollectionConstants.SBIMOPS_DTID + "=" + requestParameters.get(CollectionConstants.SBIMOPS_DTID) + "|" +
                CollectionConstants.SBIMOPS_RN + "=" + requestParameters.get(CollectionConstants.SBIMOPS_RN) + "|" +
                CollectionConstants.SBIMOPS_RID + "=" + requestParameters.get(CollectionConstants.SBIMOPS_RID) + "|" +
                CollectionConstants.SBIMOPS_TA + "=" + requestParameters.get(CollectionConstants.SBIMOPS_TA)
                + "|" +
                CollectionConstants.SBIMOPS_CH + "=" + requestParameters.get(CollectionConstants.SBIMOPS_CH)
                + "|" +
                CollectionConstants.SBIMOPS_RURL + "=" + requestParameters.get(CollectionConstants.SBIMOPS_RURL) + "|" +
                CollectionConstants.ONLINEPAYMENT_INVOKE_URL + "="
                + requestParameters.get(CollectionConstants.ONLINEPAYMENT_INVOKE_URL));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("End SbimopsAdaptor-createPaymentRequest");

        return sbiPaymentRequest;
    }

    @Override
    public PaymentResponse parsePaymentResponse(final String response) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Insider SbimopsAdaptor-parsePaymentResponse");
        }
        final String[] keyValueStr = response.replace("{", "").replace("}", "").split(",");
        final LinkedHashMap<String, String> responseMap = new LinkedHashMap<>(0);

        for (final String pair : keyValueStr) {
            final String[] entry = pair.split("=");
            if (entry.length == 2)
                responseMap.put(entry[0].trim(), entry[1].trim());
        }
        final DefaultPaymentResponse sbiPaymentResponse = new DefaultPaymentResponse();
        sbiPaymentResponse.setAuthStatus("Success".equalsIgnoreCase(responseMap.get(CollectionConstants.SBIMOPS_STATUS))
                ? CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS : responseMap.get(CollectionConstants.SBIMOPS_STATUS));
        sbiPaymentResponse.setErrorDescription(responseMap.get(CollectionConstants.SBIMOPS_STATUS));
        final String[] consumercodeTransactionId = responseMap.get(CollectionConstants.SBIMOPS_DTID)
                .split(CollectionConstants.SEPARATOR_HYPHEN);
        sbiPaymentResponse.setAdditionalInfo6(consumercodeTransactionId[0]);
        sbiPaymentResponse.setReceiptId(consumercodeTransactionId[1]);
        if (sbiPaymentResponse.getAuthStatus().equals(CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS)) {
            sbiPaymentResponse.setTxnAmount(new BigDecimal(responseMap.get(CollectionConstants.SBIMOPS_TA)));
            sbiPaymentResponse.setTxnReferenceNo(responseMap.get(CollectionConstants.SBIMOPS_CFMS_TRID));
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DDMMYYYYHHMMSS", Locale.getDefault());
            Date transDate = null;
            try {
                transDate = simpleDateFormat.parse(responseMap.get(CollectionConstants.SBIMOPS_BANKTIME_STAMP));
                sbiPaymentResponse.setTxnDate(transDate);
            } catch (final ParseException e) {
                LOGGER.error("Error in parsing transaction date [" + responseMap.get(CollectionConstants.SBIMOPS_BANK_DATE) + "]",
                        e);
                throw new ApplicationRuntimeException(".transactiondate.parse.error", e);
            }
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("End SbimopsAdaptor-parsePaymentResponse");

        }
        return sbiPaymentResponse;
    }

    public PaymentResponse createOfflinePaymentRequest(final OnlinePayment onlinePayment) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("inside sbimops :createOfflinePaymentRequest");
        final PaymentResponse sbimopsResponse = new DefaultPaymentResponse();

        CloseableHttpResponse response = reconciliationResponse(onlinePayment);

        if (response == null)
            return sbimopsResponse;
        else {
            final Map<String, String> responseParameterMap = prepareResponseMap(response);
            sbimopsResponse.setAdditionalInfo6(onlinePayment.getReceiptHeader().getConsumerCode().replace("-", "")
                    .replace("/", ""));
            sbimopsResponse.setReceiptId(onlinePayment.getReceiptHeader().getId().toString());
            final String transactionStatus = responseParameterMap.get(CollectionConstants.SBIMOPS_STATUS.toUpperCase());
            sbimopsResponse.setAuthStatus("S".equalsIgnoreCase(transactionStatus)
                    ? CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS : transactionStatus);

            if (sbimopsResponse.getAuthStatus().equals(CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS)) {
                sbimopsResponse
                        .setTxnAmount(new BigDecimal(Double.valueOf(responseParameterMap.get(CollectionConstants.SBIMOPS_TAMT))));
                sbimopsResponse.setTxnReferenceNo(responseParameterMap.get(CollectionConstants.SBIMOPS_CFMSID).toString());
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                Date transDate = null;
                try {
                    transDate = simpleDateFormat.parse(responseParameterMap.get(CollectionConstants.SBIMOPS_BNKDT).toString());
                    sbimopsResponse.setTxnDate(transDate);
                } catch (final ParseException e) {
                    LOGGER.error(
                            "Error in parsing transaction date ["
                                    + responseParameterMap.get(CollectionConstants.SBIMOPS_BANK_DATE)
                                    + "]",
                            e);
                    throw new ApplicationRuntimeException(".transactiondate.parse.error", e);
                }
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("End SbimopsAdaptor-parsePaymentResponse");
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

            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, credentials);
            HttpClient client = HttpClientBuilder.create()
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .build();
            response = (CloseableHttpResponse) client.execute(httpPost);
        } catch (IOException e) {
            LOGGER.error(
                    "SBIMOPS reconciliation, error while sending the request for SBIMOPS reconciliation");
            throw new ApplicationRuntimeException(
                    "SBIMOPS reconciliation, error while sending the request for SBIMOPS reconciliation", e);
        }
        return response;
    }

    private String prepeareReconciliationRequest(final OnlinePayment onlinePayment) {
        final JsonObject deptCodeJson = new JsonObject();
        deptCodeJson.addProperty(CollectionConstants.SBIMOPS_DEPTCODE,
                collectionApplicationProperties.sbimopsDepartmentcode(CollectionConstants.MESSAGEKEY_SBIMOPS_DC));
        final JsonObject transactionIdJson = new JsonObject();
        final StringBuilder transactionId = new StringBuilder(onlinePayment.getReceiptHeader().getConsumerCode())
                .append(CollectionConstants.SEPARATOR_HYPHEN).append(onlinePayment.getReceiptHeader().getId());
        transactionIdJson.addProperty(CollectionConstants.SBIMOPS_DEPTTID, transactionId.toString());
        final JsonObject requestJson = new JsonObject();
        deptCodeJson.add(CollectionConstants.SBIMOPS_ROW, transactionIdJson);
        requestJson.add(CollectionConstants.SBIMOPS_RECORDSET, deptCodeJson);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("SBIMOPS reconciliation request:" + requestJson.toString());
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
            Map<String, Object> responseParameterMap = (Map<String, Object>) responseMap.get("RECORDSET").get("ROW");
            final Map<String, String> responseSbimopsMap = new LinkedHashMap<>();
            responseParameterMap.forEach((k, v) -> responseSbimopsMap.put(k, v.toString()));
            return responseSbimopsMap;
        } catch (IOException e) {
            LOGGER.error("SBIMOPS reconciliation, error while reading the response content");
            throw new ApplicationRuntimeException(" SBIMOPS reconciliation, error while reading the response content", e);
        }
    }

}