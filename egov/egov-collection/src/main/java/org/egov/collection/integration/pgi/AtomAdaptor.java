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
import org.egov.collection.integration.models.ResponseAtomMmp;
import org.egov.collection.integration.models.ResponseAtomParam;
import org.egov.collection.integration.models.ResponseAtomReconcilation;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.utils.DateUtils;
import org.egov.infstr.models.ServiceDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The PaymentRequestAdaptor class frames the request object for the payment service.
 */
@Service
public class AtomAdaptor implements PaymentGatewayAdaptor {

    private static final Logger LOGGER = Logger.getLogger(AtomAdaptor.class);
    @Autowired
    private CollectionApplicationProperties collectionApplicationProperties;
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method invokes APIs to frame request object for the payment service passed as parameter
     *
     * @param serviceDetails
     * @param receiptHeader
     * @return
     */
    @Override
    public PaymentRequest createPaymentRequest(final ServiceDetails paymentServiceDetails,
            final ReceiptHeader receiptHeader) {
        LOGGER.debug("inside  AtomAdaptor createPaymentRequest");
        final DefaultPaymentRequest paymentRequest = new DefaultPaymentRequest();
        String ttype = null, tempTxnId = null, token = null, txnStage = null;
        final HttpPost httpPost = new HttpPost(paymentServiceDetails.getServiceUrl());
        final List<NameValuePair> formData = new ArrayList<>(0);
        formData.add(new BasicNameValuePair(CollectionConstants.ATOM_LOGIN, collectionApplicationProperties.atomLogin()));
        formData.add(new BasicNameValuePair(CollectionConstants.ATOM_PASS, collectionApplicationProperties.atomPass()));
        formData.add(new BasicNameValuePair(CollectionConstants.ATOM_TTYPE, collectionApplicationProperties.atomTtype()));
        formData.add(new BasicNameValuePair(CollectionConstants.ATOM_PRODID, collectionApplicationProperties.atomProdid()));
        formData.add(new BasicNameValuePair(CollectionConstants.ATOM_AMT, receiptHeader.getTotalAmount()
                .setScale(CollectionConstants.AMOUNT_PRECISION_DEFAULT, BigDecimal.ROUND_UP).toString()));
        formData.add(new BasicNameValuePair(CollectionConstants.ATOM_TXNCURR, collectionApplicationProperties.atomTxncurr()));
        formData.add(new BasicNameValuePair(CollectionConstants.ATOM_TXNSCAMT, collectionApplicationProperties.atomTxnscamt()));
        formData.add(
                new BasicNameValuePair(CollectionConstants.ATOM_CLIENTCODE, collectionApplicationProperties.atomClientcode()));
        formData.add(new BasicNameValuePair(CollectionConstants.ATOM_TXNID, receiptHeader.getId().toString()));
        formData.add(new BasicNameValuePair(CollectionConstants.ATOM_DATE,
                DateUtils.getFormattedDate(receiptHeader.getCreatedDate(), "dd/MM/yyyy HH:mm:ss")));
        formData.add(new BasicNameValuePair(CollectionConstants.ATOM_CUSTACC, collectionApplicationProperties.atomCustacc()));
        formData.add(new BasicNameValuePair(CollectionConstants.ATOM_MDD, collectionApplicationProperties.atomMdd()));
        StringBuilder returnUrl = new StringBuilder();
        returnUrl.append(paymentServiceDetails.getCallBackurl()).append("?paymentServiceId=")
                .append(paymentServiceDetails.getId());
        formData.add(new BasicNameValuePair(CollectionConstants.ATOM_RU, returnUrl.toString()));
        formData.add(new BasicNameValuePair(CollectionConstants.ATOM_UDF9, (ApplicationThreadLocals.getCityCode() + "|"
                + receiptHeader.getConsumerCode().replace("-", "").replace("/", ""))));
        LOGGER.info("First request ATOM: " + formData);
        UrlEncodedFormEntity urlEncodedFormEntity = null;
        try {
            urlEncodedFormEntity = new UrlEncodedFormEntity(formData);
            httpPost.setEntity(urlEncodedFormEntity);
            final CloseableHttpClient httpclient = HttpClients.createDefault();
            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity responseAtom = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseAtom.getContent()));
            final StringBuilder data = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                data.append(line);
            reader.close();

            LOGGER.info("First Response ATOM: " + data.toString());
            JAXBContext jaxbContext = JAXBContext.newInstance(ResponseAtomMmp.class);
            Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
            StringReader strReader = new StringReader(data.toString());
            ResponseAtomMmp responseMmp = (ResponseAtomMmp) unMarshaller.unmarshal(strReader);

            // Setting first request response values to second request
            // parameters.
            for (ResponseAtomParam responseParam : responseMmp.getMERCHANT().getRESPONSE().getParam()) {
                if (null != responseParam.getName() && !responseParam.getName().isEmpty()) {
                    if (responseParam.getName().equals(CollectionConstants.ATOM_TTYPE))
                        ttype = responseParam.getValue();
                    else if (responseParam.getName().equals(CollectionConstants.ATOM_TEMPTXNID))
                        tempTxnId = responseParam.getValue();
                    else if (responseParam.getName().equals(CollectionConstants.ATOM_TOKEN))
                        token = responseParam.getValue();
                    else if (responseParam.getName().equals(CollectionConstants.ATOM_TXNSTAGE))
                        txnStage = responseParam.getValue();
                }
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
        String secondRequestStr = paymentServiceDetails.getServiceUrl() + "?ttype=" + ttype + "&tempTxnId=" + tempTxnId
                + "&token=" + token + "&txnStage=" + txnStage;
        LOGGER.debug("Second request ATOM : " + secondRequestStr);
        paymentRequest.setParameter(CollectionConstants.ONLINEPAYMENT_INVOKE_URL, secondRequestStr);
        LOGGER.info("Second paymentRequest: " + paymentRequest.getRequestParameters());
        return paymentRequest;
    }

    @Override
    public PaymentResponse parsePaymentResponse(final String response) {
        LOGGER.debug("inside  ATOM createPaymentRequest");
        LOGGER.info("Response message from Atom Payment gateway: " + response);
        String[] keyValueStr = response.replace("{", "").replace("}", "").split(",");
        PaymentResponse atomResponse = new DefaultPaymentResponse();
        Map<String, String> responseMap = new HashMap<String, String>(0);
        for (String pair : keyValueStr) {
            String[] entry = pair.split("=");
            responseMap.put(entry[0].trim(), entry[1].trim());
        }
        atomResponse.setAuthStatus(responseMap.get(CollectionConstants.ATOM_F_CODE).equalsIgnoreCase("Ok")
                ? CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS : responseMap.get(CollectionConstants.ATOM_F_CODE));
        atomResponse.setErrorDescription(responseMap.get(CollectionConstants.ATOM_F_CODE));
        atomResponse.setReceiptId(responseMap.get(CollectionConstants.ATOM_MER_TXN));
        atomResponse.setTxnAmount(new BigDecimal(responseMap.get(CollectionConstants.ATOM_AMT)));
        atomResponse.setTxnReferenceNo(responseMap.get(CollectionConstants.ATOM_MMP_TXN));
        if (responseMap.get(CollectionConstants.ATOM_UDF9) != null) {
            String[] udf9 = responseMap.get(CollectionConstants.ATOM_UDF9).split("\\|");
            atomResponse.setAdditionalInfo6(udf9[1]);
            atomResponse.setAdditionalInfo2(udf9[0]);
        } else {
            final String receiptId = responseMap.get(CollectionConstants.ATOM_MER_TXN);
            final String ulbCode = ApplicationThreadLocals.getCityCode();
            final ReceiptHeader receiptHeader;
            final Query qry = entityManager.createNamedQuery(CollectionConstants.QUERY_RECEIPT_BY_ID_AND_CITYCODE);
            qry.setParameter(1, Long.valueOf(receiptId));
            qry.setParameter(2, ulbCode);
            receiptHeader = (ReceiptHeader) qry.getSingleResult();
            atomResponse.setAdditionalInfo6(receiptHeader.getConsumerCode().replace("-", "").replace("/", ""));
            atomResponse.setAdditionalInfo2(ulbCode);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault());
        Date transactionDate = null;
        try {
            transactionDate = sdf.parse(responseMap.get(CollectionConstants.ATOM_DATE));
            atomResponse.setTxnDate(transactionDate);
        } catch (ParseException e) {
            LOGGER.error("Error occured in parsing the transaction date [" + transactionDate + "]", e);
            try {
                throw new ApplicationException(".transactiondate.parse.error", e);
            } catch (ApplicationException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return atomResponse;
    }

    @Transactional
    public PaymentResponse createOfflinePaymentRequest(final OnlinePayment onlinePayment) {
        LOGGER.debug("Inside AtomAdaptor createOfflinePaymentRequest");
        PaymentResponse atomResponse = new DefaultPaymentResponse();
        try {
            final HttpPost httpPost = new HttpPost(collectionApplicationProperties.atomReconcileUrl());
            final List<NameValuePair> formData = new ArrayList<>(0);
            formData.add(
                    new BasicNameValuePair(CollectionConstants.ATOM_MERCHANTID, collectionApplicationProperties.atomLogin()));
            formData.add(new BasicNameValuePair(CollectionConstants.ATOM_MERCHANT_TXNID,
                    onlinePayment.getReceiptHeader().getId().toString()));
            formData.add(new BasicNameValuePair(CollectionConstants.ATOM_AMT, onlinePayment.getReceiptHeader().getTotalAmount()
                    .setScale(CollectionConstants.AMOUNT_PRECISION_DEFAULT, BigDecimal.ROUND_UP).toString()));
            formData.add(new BasicNameValuePair(CollectionConstants.ATOM_TDATE,
                    DateUtils.getFormattedDate(onlinePayment.getCreatedDate(), "yyyy-MM-dd")));
            LOGGER.debug("ATOM  Reconcilation request : " + formData);
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(formData);
            httpPost.setEntity(urlEncodedFormEntity);
            final CloseableHttpClient httpclient = HttpClients.createDefault();
            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity responseAtom = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseAtom.getContent()));
            final StringBuilder data = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                data.append(line);
            reader.close();
            LOGGER.info("ATOM Reconcile Response : " + data.toString());
            JAXBContext jaxbContext = JAXBContext.newInstance(ResponseAtomReconcilation.class);
            Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
            StringReader strReader = new StringReader(data.toString());
            ResponseAtomReconcilation responseAtomReconcilation = (ResponseAtomReconcilation) unMarshaller
                    .unmarshal(strReader);
            atomResponse.setAuthStatus(
                    (null != responseAtomReconcilation.getVerified() && responseAtomReconcilation.getVerified().equals("SUCCESS"))
                            ? CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS
                            : responseAtomReconcilation.getVerified());
            atomResponse.setErrorDescription(responseAtomReconcilation.getVerified());
            atomResponse.setReceiptId(responseAtomReconcilation.getMerchantTxnID());
            if (CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS.equals(atomResponse.getAuthStatus())) {
                atomResponse.setTxnReferenceNo(responseAtomReconcilation.getAtomtxnId());
                atomResponse.setTxnAmount(new BigDecimal(responseAtomReconcilation.getAmt()));
                String[] udf9 = responseAtomReconcilation.getUdf9().split("\\|");
                atomResponse.setAdditionalInfo6(udf9[1]);
                atomResponse.setAdditionalInfo2(udf9[0]);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date transactionDate = null;
                try {
                    transactionDate = sdf.parse(responseAtomReconcilation.getTxnDate());
                    atomResponse.setTxnDate(transactionDate);
                } catch (ParseException e) {
                    LOGGER.error("Error occured in parsing the transaction date [" + responseAtomReconcilation.getTxnDate() + "]",
                            e);
                    throw new ApplicationException(".transactiondate.parse.error", e);
                }
            } else {
                atomResponse
                        .setAdditionalInfo6(onlinePayment.getReceiptHeader().getConsumerCode().replace("-", "").replace("/", ""));
                atomResponse.setAdditionalInfo2(ApplicationThreadLocals.getCityCode());
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return atomResponse;
    }
}
