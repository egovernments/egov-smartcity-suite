/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
package org.egov.restapi.web.rest;

import static java.math.RoundingMode.HALF_UP;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.model.RestPropertyTaxDetails;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.ptis.service.utils.PropertyTaxService;
import org.egov.restapi.constants.RestApiConstants;
import org.egov.restapi.util.JsonConvertor;
import org.egov.restapi.web.security.oauth2.utils.TokenServiceUtils;
import org.egov.wtms.application.entity.WaterConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.application.service.WaterConnectionService;
import org.egov.wtms.application.service.collection.WaterTaxExternalService;
import org.egov.wtms.masters.entity.PayWaterTaxDetails;
import org.egov.wtms.masters.entity.WaterConnectionRequestDetails;
import org.egov.wtms.masters.entity.WaterReceiptDetails;
import org.egov.wtms.masters.entity.WaterTaxDetails;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestWaterConnectionCollection {
    private static final Logger LOG = Logger.getLogger(RestWaterConnectionCollection.class);

    @Autowired
    private WaterTaxExternalService waterTaxExternalService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private WaterConnectionService waterConnectionService;

    @Autowired
    private PropertyExternalService propertyExternalService;

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @Autowired
    private TokenServiceUtils tokenServiceUtils;
    
    @Autowired
    private PropertyTaxService propertyTaxService;

    /**
     * This method is used to pay the water tax.
     *
     * @param payWaterTaxDetails
     * @param request
     * @return responseJson - server response in JSON format
     * @throws IOException
     */

    @PostMapping(value = "/v1.0/watercharges/paywatertax", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String payWaterConnectionTaxDetails(@Valid @RequestBody PayWaterTaxDetails payWaterTaxDetails,
            HttpServletRequest request, final OAuth2Authentication authentication) {
        return payWaterTaxDetails(payWaterTaxDetails, request, authentication);
    }

    @PostMapping(value = "/watercharges/paywatertax", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String payWaterTax(@Valid @RequestBody PayWaterTaxDetails payWaterTaxDetails,
            HttpServletRequest request) {
        return payWaterTaxDetails(payWaterTaxDetails, request, null);
    }

    public String payWaterTaxDetails(PayWaterTaxDetails payWaterTaxDetails, HttpServletRequest request,
            final OAuth2Authentication authentication) {
        WaterReceiptDetails waterReceiptDetails = null;
        try {
            ErrorDetails errorDetails = validatePaymentDetails(payWaterTaxDetails);
            if (errorDetails != null && isNotBlank(errorDetails.getErrorCode()))
                return JsonConvertor.convert(errorDetails);
            else {
                if (authentication == null)
                    payWaterTaxDetails.setSource(request.getSession().getAttribute("source") != null
                            ? request.getSession().getAttribute("source").toString() : "");
                else {
                    Object source = tokenServiceUtils.getSource(authentication);
                    payWaterTaxDetails.setSource(source == null ? "" : source.toString());
                }
                payWaterTaxDetails.setPaymentAmount(payWaterTaxDetails.getPaymentAmount().setScale(0, HALF_UP));
                waterReceiptDetails = waterTaxExternalService.payWaterTax(payWaterTaxDetails);
            }
        } catch (ValidationException e) {

            List<ErrorDetails> errorList = new ArrayList<>(0);

            List<ValidationError> errors = e.getErrors();
            for (ValidationError ve : errors) {
                ErrorDetails er = new ErrorDetails();
                er.setErrorCode(ve.getKey());
                er.setErrorMessage(ve.getMessage());
                errorList.add(er);
            }
            JsonConvertor.convert(errorList);
        } catch (Exception e) {

            List<ErrorDetails> errorList = new ArrayList<>(0);
            ErrorDetails er = new ErrorDetails();
            er.setErrorCode(e.getMessage());
            er.setErrorMessage(e.getMessage());
            errorList.add(er);
            JsonConvertor.convert(errorList);
        }
        return JsonConvertor.convert(waterReceiptDetails);
    }

    @GetMapping(value = "/v1.0/watercharges/getwatertaxdetails", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String getWaterTaxDetails(@Valid PayWaterTaxDetails payWaterTaxDetails) {
        return getWaterConnectionTaxDetails(payWaterTaxDetails);
    }

    @PostMapping(value = "/watercharges/getwatertaxdetails", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String getWaterTaxDetailsByAppLicationOrConsumerNumber(@Valid @RequestBody PayWaterTaxDetails payWaterTaxDetails) {
        return getWaterConnectionTaxDetails(payWaterTaxDetails);
    }

    public String getWaterConnectionTaxDetails(PayWaterTaxDetails payWaterTaxDetails) {
        ErrorDetails errorDetails = validateConsumerAndApplicationNumber(payWaterTaxDetails);
        if (isNotBlank(errorDetails.getErrorCode()) && isNotBlank(errorDetails.getErrorMessage()))
            return JsonConvertor.convert(errorDetails);
        else
            return JsonConvertor.convert(getWaterTaxDetails(waterTaxExternalService.getWaterTaxDemandDet(payWaterTaxDetails)));
    }

    @PostMapping(value = "/watercharges/getwatertaxdetailsByOwnerDetails", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String getWaterTaxDetailsByAppLicationOrConsumerNumberByOwnerDetails(
            @Valid @RequestBody WaterConnectionRequestDetails waterConnectionRequestDetails) {
        List<WaterTaxDetails> waterTaxDetailsList = new ArrayList<>();
        ErrorDetails errorDetails = null;
        if (isNotBlank(waterConnectionRequestDetails.getConsumerNo()))
            errorDetails = validateConsumerNumber(waterConnectionRequestDetails.getConsumerNo());
        if (isNotBlank(waterConnectionRequestDetails.getAssessmentNo()))
            errorDetails = validateAssessmentNumber(waterConnectionRequestDetails.getAssessmentNo());
        if (errorDetails != null) {
            WaterTaxDetails watertaxDetails = new WaterTaxDetails();
            watertaxDetails.setErrorDetails(errorDetails);
            waterTaxDetailsList.add(watertaxDetails);
            return JsonConvertor.convert(waterTaxDetailsList);
        } else {
            List<String> consumerCodesList = waterConnectionDetailsService
                    .getConnectionsByOwnerOrMobileNumber(waterConnectionRequestDetails);
            if (consumerCodesList.isEmpty() || consumerCodesList.size() > 100) {
                return JsonConvertor.convert(isEmptyWaterTaxDetails());
            } else {
                waterTaxDetailsList = waterTaxExternalService.getWaterTaxDemandDetListByConsumerCodes(consumerCodesList);
                for (WaterTaxDetails waterTaxDetails : waterTaxDetailsList) {
                    if (waterTaxDetails.getErrorDetails() == null) {
                        ErrorDetails errordetails = new ErrorDetails();
                        errordetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_SUCCESS);
                        errordetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_SUCCESS);
                        waterTaxDetails.setErrorDetails(errorDetails);
                    }
                }
                return JsonConvertor.convert(waterTaxDetailsList);
            }
        }
    }

    public ErrorDetails validatePaymentDetails(PayWaterTaxDetails payWaterTaxDetails) {

        WaterConnectionDetails waterConnDetailsObj = null;
        ErrorDetails errorDetails = validateConsumerAndApplicationNumber(payWaterTaxDetails);
        String applicationNo = payWaterTaxDetails.getApplicationNumber();
        String consumerCode = payWaterTaxDetails.getConsumerNo();
        if (isNotBlank(payWaterTaxDetails.getApplicationNumber()) || isNotBlank(consumerCode))
            waterConnDetailsObj = waterConnectionDetailsService.findByApplicationNumberOrConsumerCodeAndStatus(
                    isBlank(applicationNo) ? consumerCode : applicationNo, ConnectionStatus.ACTIVE);
        if (waterConnDetailsObj == null) {
            waterConnDetailsObj = waterConnectionDetailsService.findByConsumerCodeAndConnectionStatus(consumerCode,
                    ConnectionStatus.INACTIVE);
            if (waterConnDetailsObj != null) {
                errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_INACTIVE_CONSUMERNO);
                errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_INACTIVE_CONSUMERNO);
            } else {
                errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_CONSUMER_NO_VALID);
                errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONSUMER_NO_VALID);
            }
        }
        if (waterConnDetailsObj != null && waterConnDetailsObj.getConnectionType().equals(ConnectionType.NON_METERED)) {
            BigDecimal totalAmountDue = waterConnectionDetailsService.getWaterTaxDueAmount(waterConnDetailsObj);
            LOG.info("totalAmountDue:" + totalAmountDue);

            LOG.info("payWaterTaxDetails.getTotalAmount():" + payWaterTaxDetails.getTotalAmount());

            LOG.info("compare " + totalAmountDue.compareTo(payWaterTaxDetails.getTotalAmount()));

            LOG.info("compare " + totalAmountDue.compareTo(payWaterTaxDetails.getTotalAmount()));

        }
        if (isBlank(payWaterTaxDetails.getTransactionId())) {
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_TRANSANCTIONID_REQUIRED);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_TRANSANCTIONID_REQUIRED);
        } else if (isNotBlank(payWaterTaxDetails.getTransactionId())) {
            BillReceiptInfo billReceipt = waterTaxExternalService
                    .validateTransanctionIdPresent(payWaterTaxDetails.getTransactionId());
            if (billReceipt != null) {
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_TRANSANCTIONID_VALIDATE);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_TRANSANCTIONID_VALIDATE);

            }
        }
        String paymentMode = payWaterTaxDetails.getPaymentMode().trim();
        if (isBlank(payWaterTaxDetails.getPaymentMode())) {
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PAYMENT_MODE_REQUIRED);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PAYMENT_MODE_REQUIRED);
        } else if (!PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CASH.equalsIgnoreCase(paymentMode)
                && !PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CHEQUE.equalsIgnoreCase(paymentMode)
                && !PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_DD.equalsIgnoreCase(paymentMode)) {
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PAYMENT_MODE_INVALID);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PAYMENT_MODE_INVALID);
        }

        if (isNotBlank(payWaterTaxDetails.getPaymentMode())
                && (PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CHEQUE.equalsIgnoreCase(paymentMode)
                        || PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_DD.equalsIgnoreCase(paymentMode)))
            if (isBlank(payWaterTaxDetails.getChqddNo())) {
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_CHQDD_NO_REQUIRED);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_CHQDD_NO_REQUIRED);
            } else if (isBlank(payWaterTaxDetails.getChqddDate())) {
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_CHQDD_DATE_REQUIRED);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_CHQDD_DATE_REQUIRED);
            } else if (isBlank(payWaterTaxDetails.getBankName())) {
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_BANKNAME_REQUIRED);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_BANKNAME_REQUIRED);
            } else if (isBlank(payWaterTaxDetails.getBranchName())) {
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_BRANCHNAME_REQUIRED);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_BRANCHNAME_REQUIRED);
            }

        return errorDetails;
    }

    private ErrorDetails validateConsumerAndApplicationNumber(PayWaterTaxDetails payWaterTaxDetails) {
        ErrorDetails errorDetails = new ErrorDetails();
        if (isBlank(payWaterTaxDetails.getConsumerNo())) {
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_CONSUMER_NO_REQUIRED);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONSUMER__NO_REQUIRED);
        } else if (isNotBlank(payWaterTaxDetails.getConsumerNo())
                && payWaterTaxDetails.getConsumerNo().trim().length() < 10) {
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_CONSUMER_NO_LEN);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONSUMER_NO_LEN);
        }
        if (isBlank(payWaterTaxDetails.getConsumerNo()) && isBlank(payWaterTaxDetails.getApplicationNumber())) {
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_APPLICATION_NO_REQUIRED);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_APPLICATION__NO_REQUIRED);
        } else if (isNotBlank(payWaterTaxDetails.getApplicationNumber())
                && payWaterTaxDetails.getApplicationNumber().trim().length() < 13) {
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_APPLICATION_NO_LEN);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_APPLICATION_NO_LEN);
        }
        WaterConnectionDetails waterConnDetailsObj = null;
        if (isNotBlank(payWaterTaxDetails.getConsumerNo()))
            waterConnDetailsObj = waterConnectionDetailsService.findByConsumerCodeAndConnectionStatus(
                    payWaterTaxDetails.getConsumerNo(), ConnectionStatus.INACTIVE);
        if (waterConnDetailsObj != null) {
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_INACTIVE_CONSUMERNO);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_INACTIVE_CONSUMERNO);
        }
        return errorDetails;
    }

    private ErrorDetails validateConsumerNumber(String consumerCode) {
        ErrorDetails errorDetails = null;
        if (consumerCode.trim().length() > 0 && consumerCode.trim().length() < 10) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_CONSUMER_NO_LEN);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONSUMER_NO_LEN);
        }
        if (isNotBlank(consumerCode)) {
            final WaterConnection waterConnection = waterConnectionService.findByConsumerCode(consumerCode);
            if (waterConnection == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_CONSUMERCODE_NOT_EXIST);
                errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONSUMERCODE_NOT_EXIST);
            }
        }
        if (isNotBlank(consumerCode)) {
            WaterConnectionDetails waterConnDetailsObj = waterConnectionDetailsService
                    .findByConsumerCodeAndConnectionStatus(consumerCode, ConnectionStatus.INACTIVE);
            if (waterConnDetailsObj != null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_INACTIVE_CONSUMERNO);
                errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_INACTIVE_CONSUMERNO);
            }
        }
        return errorDetails;
    }

    private List<WaterTaxDetails> isEmptyWaterTaxDetails() {
        List<WaterTaxDetails> waterTaxDetailsList = new ArrayList<>();
        WaterTaxDetails watertaxDetails = new WaterTaxDetails();
        ErrorDetails errordetails = new ErrorDetails();
        errordetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_WATERTAXDETAILS_SIZE);
        errordetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_WATERTAXDETAILS_SIZE);
        watertaxDetails.setErrorDetails(errordetails);
        waterTaxDetailsList.add(watertaxDetails);
        return waterTaxDetailsList;
    }

    private ErrorDetails validateAssessmentNumber(String assessmentNumber) {
        ErrorDetails errorDetails = null;
        if (assessmentNumber.trim().length() > 0 && assessmentNumber.trim().length() < 10) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_LEN);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_LEN);
        }
        if (!basicPropertyDAO.isAssessmentNoExist(assessmentNumber)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_NOT_FOUND);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_NOT_FOUND);
        }
        return errorDetails;
    }

    private WaterTaxDetails getWaterTaxDetails(WaterTaxDetails waterTaxDetails) {
        if (isBlank(waterTaxDetails.getConsumerNo()))
            waterTaxDetails.setConsumerNo(EMPTY);
        if (isBlank(waterTaxDetails.getOwnerName()))
            waterTaxDetails.setOwnerName(EMPTY);
        if (isBlank(waterTaxDetails.getLocalityName()))
            waterTaxDetails.setLocalityName(EMPTY);
        if (isBlank(waterTaxDetails.getPropertyAddress()))
            waterTaxDetails.setPropertyAddress(EMPTY);
        if (waterTaxDetails.getTaxDetails() == null) {
            RestPropertyTaxDetails ar = new RestPropertyTaxDetails();
            List<RestPropertyTaxDetails> taxDetails = new ArrayList<>(0);
            taxDetails.add(ar);
            waterTaxDetails.setTaxDetails(taxDetails);
        }
        return waterTaxDetails;
    }

}