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
package org.egov.restapi.web.rest;

import java.io.IOException;
import java.math.BigDecimal;

import javax.validation.Valid;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.restapi.constants.RestApiConstants;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.application.service.WaterConnectionService;
import org.egov.wtms.application.service.collection.WaterTaxExternalService;
import org.egov.wtms.masters.entity.PayWaterTaxDetails;
import org.egov.wtms.masters.entity.WaterReceiptDetails;
import org.egov.wtms.masters.entity.WaterTaxDetails;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestWaterConnectionCollection {

    @Autowired
    private WaterTaxExternalService waterTaxExternalService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private WaterConnectionService waterConnectionService;

    /**
     * This method is used to pay the water tax.
     *
     * @param consumerNo
     *            - consumer number
     * @param paymentMode
     *            - mode of payment
     * @param totalAmount
     *            - total amount paid
     * @param paidBy
     *            - payer's name
     * @return responseJson - server response in JSON format
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/watercharges/payWaterTax", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public String payWateTax(@Valid @RequestBody final PayWaterTaxDetails payWaterTaxDetails)
            throws JsonGenerationException, JsonMappingException, IOException {
        final ErrorDetails errorDetails = validatePaymentDetails(payWaterTaxDetails);
        if (null != errorDetails)
            return getJSONResponse(errorDetails);
        else {
            final WaterReceiptDetails waterReceiptDetails = waterTaxExternalService.payWaterTax(payWaterTaxDetails);
            return getJSONResponse(waterReceiptDetails);
        }
    }

    @RequestMapping(value = "/watercharges/getWatertaxDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public String getWaterTaxDetailsByAppLicationOrConsumerNumber(
            @Valid @RequestBody final PayWaterTaxDetails payWaterTaxDetails) throws JsonGenerationException,
            JsonMappingException, IOException {
        final ErrorDetails errorDetails = validatePaymentDetBeforCollection(payWaterTaxDetails);
        if (null != errorDetails)
            return getJSONResponse(errorDetails);
        else {
            final WaterTaxDetails waterTaxDetails = waterTaxExternalService.getWaterTaxDemandDet(payWaterTaxDetails);
            return getJSONResponse(waterTaxDetails);
        }
    }

    private String getJSONResponse(final Object obj) throws JsonGenerationException, JsonMappingException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        final String jsonResponse = objectMapper.writeValueAsString(obj);
        return jsonResponse;
    }

    public ErrorDetails validatePaymentDetBeforCollection(final PayWaterTaxDetails payWaterTaxDetails) {
        ErrorDetails errorDetails = null;
        WaterConnectionDetails waterConnDetailsObj = null;
        errorDetails = validateConsumerAndApplicationNumber(payWaterTaxDetails, errorDetails);
        if (payWaterTaxDetails.getApplicaionNumber() != null && !"".equals(payWaterTaxDetails.getApplicaionNumber()))
            waterConnDetailsObj = waterConnectionDetailsService.findByApplicationNumber(payWaterTaxDetails
                    .getApplicaionNumber());
        else if (payWaterTaxDetails.getConsumerNo() != null)
            waterConnDetailsObj = waterConnectionDetailsService.findByApplicationNumberOrConsumerCodeAndStatus(
                    payWaterTaxDetails.getConsumerNo(), ConnectionStatus.ACTIVE);
        if (waterConnDetailsObj == null) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_CONSUMER_NO_VALID);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONSUMER_NO_VALID);
        }
        return errorDetails;
    }

    public ErrorDetails validatePaymentDetails(final PayWaterTaxDetails payWaterTaxDetails) {
        ErrorDetails errorDetails = null;
        WaterConnectionDetails waterConnDetailsObj = null;
        errorDetails = validateConsumerAndApplicationNumber(payWaterTaxDetails, errorDetails);
        if (payWaterTaxDetails.getApplicaionNumber() != null && !"".equals(payWaterTaxDetails.getApplicaionNumber()))
            waterConnDetailsObj = waterConnectionDetailsService.findByApplicationNumber(payWaterTaxDetails
                    .getApplicaionNumber());
        else if (payWaterTaxDetails.getConsumerNo() != null)
            waterConnDetailsObj = waterConnectionDetailsService.findByApplicationNumberOrConsumerCodeAndStatus(
                    payWaterTaxDetails.getConsumerNo(), ConnectionStatus.ACTIVE);
        if (waterConnDetailsObj == null) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_CONSUMER_NO_VALID);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONSUMER_NO_VALID);
        }
        if (waterConnDetailsObj != null) {
            final BigDecimal totalAmountDue = waterConnectionDetailsService.getTotalAmount(waterConnDetailsObj);
            if (totalAmountDue.compareTo(payWaterTaxDetails.getTotalAmount()) == 1
                    || totalAmountDue.compareTo(payWaterTaxDetails.getTotalAmount()) == -1) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_DEMAND_AMOUNT_VALID);
                errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_DEMAND_AMOUNT_VALID);
            }

        }
        
        
        
        if (payWaterTaxDetails.getPaymentMode() == null || payWaterTaxDetails.getPaymentMode().trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PAYMENT_MODE_REQUIRED);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PAYMENT_MODE_REQUIRED);
        } else if (!PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CASH.equalsIgnoreCase(payWaterTaxDetails
                .getPaymentMode().trim())
                && !PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CHEQUE.equalsIgnoreCase(payWaterTaxDetails
                        .getPaymentMode().trim())) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PAYMENT_MODE_INVALID);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PAYMENT_MODE_INVALID);
        }
        
        if (payWaterTaxDetails.getChqddNo() == null || payWaterTaxDetails.getChqddNo().trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_CHQDD_NO_REQUIRED);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_CHQDD_NO_REQUIRED);
        }
        
        if (payWaterTaxDetails.getChqddDate() == null ) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_CHQDD_DATE_REQUIRED);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_CHQDD_DATE_REQUIRED);
        }
        
        if (payWaterTaxDetails.getBankName() == null || payWaterTaxDetails.getBankName().trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_BANKNAME_REQUIRED);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_BANKNAME_REQUIRED);
        }
        
        if (payWaterTaxDetails.getChqddDate() == null ) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_BRANCHNAME_REQUIRED);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_BRANCHNAME_REQUIRED);
        }
        
        
        
        return errorDetails;
    }

    private ErrorDetails validateConsumerAndApplicationNumber(final PayWaterTaxDetails payWaterTaxDetails,
            ErrorDetails errorDetails) {
        if (payWaterTaxDetails.getConsumerNo() == null || payWaterTaxDetails.getConsumerNo().trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_CONSUMER_NO_REQUIRED);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONSUMER__NO_REQUIRED);
        } else if (payWaterTaxDetails.getConsumerNo().trim().length() > 0
                && payWaterTaxDetails.getConsumerNo().trim().length() < 10) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_CONSUMER_NO_LEN);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONSUMER_NO_LEN);
        }
        if (payWaterTaxDetails.getConsumerNo() == null && payWaterTaxDetails.getApplicaionNumber() == null) {
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_APPLICATION_NO_REQUIRED);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_APPLICATION__NO_REQUIRED);
        } else if (payWaterTaxDetails.getApplicaionNumber() != null
                && payWaterTaxDetails.getApplicaionNumber().trim().length() > 0
                && payWaterTaxDetails.getApplicaionNumber().trim().length() < 13) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_APPLICATION_NO_LEN);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_APPLICATION_NO_LEN);
        }
        return errorDetails;
    }

}
