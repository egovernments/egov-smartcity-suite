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
package org.egov.restapi.web.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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
import org.egov.ptis.domain.model.PropertyTaxDetails;
import org.egov.ptis.domain.model.RestPropertyTaxDetails;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.restapi.constants.RestApiConstants;
import org.egov.restapi.util.JsonConvertor;
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
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    /**
     * This method is used to pay the water tax.
     *
     * @param payWaterTaxDetails
     * @param request
     * @return responseJson - server response in JSON format
     * @throws IOException
     */

    @RequestMapping(value = "/watercharges/paywatertax", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String payWaterTax(@Valid @RequestBody final PayWaterTaxDetails payWaterTaxDetails,
            final HttpServletRequest request) {
        WaterReceiptDetails waterReceiptDetails = null;
        try {
            final ErrorDetails errorDetails = validatePaymentDetails(payWaterTaxDetails);
            if (null != errorDetails)
                return JsonConvertor.convert(errorDetails);
            else {
                payWaterTaxDetails.setSource(request.getSession().getAttribute("source") != null
                        ? request.getSession().getAttribute("source").toString() : "");
                waterReceiptDetails = waterTaxExternalService.payWaterTax(payWaterTaxDetails);
            }
        } catch (final ValidationException e) {

            final List<ErrorDetails> errorList = new ArrayList<>(0);

            final List<ValidationError> errors = e.getErrors();
            for (final ValidationError ve : errors) {
                final ErrorDetails er = new ErrorDetails();
                er.setErrorCode(ve.getKey());
                er.setErrorMessage(ve.getMessage());
                errorList.add(er);
            }
            JsonConvertor.convert(errorList);
        } catch (final Exception e) {

            final List<ErrorDetails> errorList = new ArrayList<>(0);
            final ErrorDetails er = new ErrorDetails();
            er.setErrorCode(e.getMessage());
            er.setErrorMessage(e.getMessage());
            errorList.add(er);
            JsonConvertor.convert(errorList);
        }
        return JsonConvertor.convert(waterReceiptDetails);
    }

    @RequestMapping(value = "/watercharges/getwatertaxdetails", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String getWaterTaxDetailsByAppLicationOrConsumerNumber(
            @Valid @RequestBody final PayWaterTaxDetails payWaterTaxDetails)
            throws IOException, BindException {
        final ErrorDetails errorDetails = validateConsumerAndApplicationNumber(payWaterTaxDetails);
        if (null != errorDetails)
            return JsonConvertor.convert(errorDetails);
        else {
            final WaterTaxDetails waterTaxDetails = waterTaxExternalService.getWaterTaxDemandDet(payWaterTaxDetails);
            return JsonConvertor.convert(getWaterTaxDetails(waterTaxDetails));
        }
    }

    @RequestMapping(value = "/watercharges/getwatertaxdetailsByOwnerDetails", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String getWaterTaxDetailsByAppLicationOrConsumerNumberByOwnerDetails(
            @Valid @RequestBody final WaterConnectionRequestDetails waterConnectionRequestDetails)
            throws IOException, BindException {
        final List<WaterTaxDetails> waterTaxDetailsList = new ArrayList<>();
        ErrorDetails errorDetails = null;
        if (!waterConnectionRequestDetails.getConsumerNo().isEmpty())
            errorDetails = validateConsumerNumber(waterConnectionRequestDetails.getConsumerNo());
        if (!waterConnectionRequestDetails.getAssessmentNo().isEmpty())
            errorDetails = validateAssessmentNumber(waterConnectionRequestDetails.getAssessmentNo());
        if (null != errorDetails) {
            final WaterTaxDetails watertaxDetails = new WaterTaxDetails();
            watertaxDetails.setErrorDetails(errorDetails);
            waterTaxDetailsList.add(watertaxDetails);
            return JsonConvertor.convert(waterTaxDetailsList);
        } else {
            List<PropertyTaxDetails> propertyTaxDetailsList = new ArrayList<>();
            String assessmentNo = "";

            Boolean consumerExists = false;
            Boolean ownerdetailsnotexists = false;
            waterConnectionRequestDetails.setAssessmentNo(waterConnectionRequestDetails.getAssessmentNo() == null
                    ? assessmentNo : waterConnectionRequestDetails.getAssessmentNo());
            if (!waterConnectionRequestDetails.getAssessmentNo().isEmpty()
                    && !waterConnectionRequestDetails.getConsumerNo().isEmpty()) {
                ownerdetailsnotexists = true;
                List<WaterConnection> waterconnectionList = waterConnectionService
                        .findByPropertyIdentifier(waterConnectionRequestDetails.getAssessmentNo());
                for (final WaterConnection waterconnection : waterconnectionList)
                    if (waterconnection.getConsumerCode()
                            .equalsIgnoreCase(waterConnectionRequestDetails.getConsumerNo())) {
                        consumerExists = true;
                        ownerdetailsnotexists = false;
                        break;
                    }
            } else if (waterConnectionRequestDetails.getAssessmentNo().isEmpty()
                    && !waterConnectionRequestDetails.getConsumerNo().isEmpty()) {
                consumerExists = true;
                ownerdetailsnotexists = false;
            } else
                assessmentNo = waterConnectionRequestDetails.getAssessmentNo();
            if (!consumerExists && ownerdetailsnotexists)
                return JsonConvertor.convert(isEmptyWaterTaxDetails());
            if (!consumerExists) {
                propertyTaxDetailsList = propertyExternalService.getPropertyTaxDetails(assessmentNo,
                        waterConnectionRequestDetails.getOwnerName(), waterConnectionRequestDetails.getMobileNo(), null,
                        null);
                if (propertyTaxDetailsList == null || propertyTaxDetailsList.isEmpty())
                    return JsonConvertor.convert(isEmptyWaterTaxDetails());
            }
            final List<String> consumerCodesList = new ArrayList<>();
            if (consumerExists && !ownerdetailsnotexists)
                consumerCodesList.add(waterConnectionRequestDetails.getConsumerNo());
            else
                for (final PropertyTaxDetails propertyTaxDetails : propertyTaxDetailsList) {
                    List<WaterConnection> waterConnectionList = waterConnectionService
                            .findByPropertyIdentifier(propertyTaxDetails.getAssessmentNo());
                    for (final WaterConnection waterconnection : waterConnectionList)
                        consumerCodesList.add(waterconnection.getConsumerCode());
                }
            if (consumerCodesList.size() == 0 || consumerCodesList.size() > 100)
                return JsonConvertor.convert(isEmptyWaterTaxDetails());
            else {
                for (final String consumerCode : consumerCodesList) {
                    WaterTaxDetails watertaxdetails = waterTaxExternalService.getWaterTaxDemandDetByConsumerCode(consumerCode);
                    waterTaxDetailsList.add(getWaterTaxDetails(watertaxdetails));
                    if (watertaxdetails.getErrorDetails() == null) {
                        final ErrorDetails errordetails = new ErrorDetails();
                        errordetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_SUCCESS);
                        errordetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_SUCCESS);
                        watertaxdetails.setErrorDetails(errorDetails);
                    }
                }
                return JsonConvertor.convert(waterTaxDetailsList);
            }
        }
    }

    public ErrorDetails validatePaymentDetails(final PayWaterTaxDetails payWaterTaxDetails) {
        ErrorDetails errorDetails;
        WaterConnectionDetails waterConnDetailsObj = null;
        errorDetails = validateConsumerAndApplicationNumber(payWaterTaxDetails);
        if (payWaterTaxDetails.getApplicaionNumber() != null && !"".equals(payWaterTaxDetails.getApplicaionNumber()))
            waterConnDetailsObj = waterConnectionDetailsService
                    .findByApplicationNumber(payWaterTaxDetails.getApplicaionNumber());
        else if (payWaterTaxDetails.getConsumerNo() != null)
            waterConnDetailsObj = waterConnectionDetailsService
                    .findByConsumerCodeAndConnectionStatus(payWaterTaxDetails.getConsumerNo(), ConnectionStatus.ACTIVE);
        if (waterConnDetailsObj == null) {
            waterConnDetailsObj = waterConnectionDetailsService.findByConsumerCodeAndConnectionStatus(
                    payWaterTaxDetails.getConsumerNo(), ConnectionStatus.INACTIVE);
            if (waterConnDetailsObj != null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_INACTIVE_CONSUMERNO);
                errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_INACTIVE_CONSUMERNO);
            } else {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_CONSUMER_NO_VALID);
                errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONSUMER_NO_VALID);
            }
        }
        if (waterConnDetailsObj != null && waterConnDetailsObj.getConnectionType().equals(ConnectionType.NON_METERED)) {
            final BigDecimal totalAmountDue = waterConnectionDetailsService.getTotalAmount(waterConnDetailsObj);
            LOG.info("totalAmountDue:" + totalAmountDue);

            LOG.info("payWaterTaxDetails.getTotalAmount():" + payWaterTaxDetails.getTotalAmount());

            LOG.info("compare " + totalAmountDue.compareTo(payWaterTaxDetails.getTotalAmount()));

            LOG.info("compare " + totalAmountDue.compareTo(payWaterTaxDetails.getTotalAmount()));

        }
        if (payWaterTaxDetails.getTransactionId() == null || "".equals(payWaterTaxDetails.getTransactionId())) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_TRANSANCTIONID_REQUIRED);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_TRANSANCTIONID_REQUIRED);
        } else if (payWaterTaxDetails.getTransactionId() != null || !"".equals(payWaterTaxDetails.getTransactionId())) {
            final BillReceiptInfo billReceipt = waterTaxExternalService
                    .validateTransanctionIdPresent(payWaterTaxDetails.getTransactionId());
            if (billReceipt != null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_TRANSANCTIONID_VALIDATE);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_TRANSANCTIONID_VALIDATE);

            }
        }
        if (payWaterTaxDetails.getPaymentMode() == null || payWaterTaxDetails.getPaymentMode().trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PAYMENT_MODE_REQUIRED);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PAYMENT_MODE_REQUIRED);
        } else if (!PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CASH
                .equalsIgnoreCase(payWaterTaxDetails.getPaymentMode().trim())
                && !PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CHEQUE
                        .equalsIgnoreCase(payWaterTaxDetails.getPaymentMode().trim())

                && !PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_DD
                        .equalsIgnoreCase(payWaterTaxDetails.getPaymentMode().trim())) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PAYMENT_MODE_INVALID);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PAYMENT_MODE_INVALID);
        }

        if (payWaterTaxDetails.getPaymentMode() != null && (PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CHEQUE
                .equalsIgnoreCase(payWaterTaxDetails.getPaymentMode().trim())
                || PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_DD
                        .equalsIgnoreCase(payWaterTaxDetails.getPaymentMode().trim())))
            if (payWaterTaxDetails.getChqddNo() == null || payWaterTaxDetails.getChqddNo().trim().length() == 0) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_CHQDD_NO_REQUIRED);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_CHQDD_NO_REQUIRED);
            } else

            if (payWaterTaxDetails.getChqddDate() == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_CHQDD_DATE_REQUIRED);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_CHQDD_DATE_REQUIRED);
            } else

            if (payWaterTaxDetails.getBankName() == null || payWaterTaxDetails.getBankName().trim().length() == 0) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_BANKNAME_REQUIRED);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_BANKNAME_REQUIRED);
            } else

            if (payWaterTaxDetails.getBranchName() == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_BRANCHNAME_REQUIRED);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_BRANCHNAME_REQUIRED);
            }

        return errorDetails;
    }

    private ErrorDetails validateConsumerAndApplicationNumber(final PayWaterTaxDetails payWaterTaxDetails) {
        ErrorDetails errorDetails = null;
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
        WaterConnectionDetails waterConnDetailsObj = null;
        if (payWaterTaxDetails.getConsumerNo() != null)
            waterConnDetailsObj = waterConnectionDetailsService.findByConsumerCodeAndConnectionStatus(
                    payWaterTaxDetails.getConsumerNo(), ConnectionStatus.INACTIVE);
        if (waterConnDetailsObj != null) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_INACTIVE_CONSUMERNO);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_INACTIVE_CONSUMERNO);
        }
        return errorDetails;
    }

    private ErrorDetails validateConsumerNumber(final String consumerCode) {
        ErrorDetails errorDetails = null;
        if (consumerCode.trim().length() > 0 && consumerCode.trim().length() < 10) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_CONSUMER_NO_LEN);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONSUMER_NO_LEN);
        }
        if (!consumerCode.isEmpty()) {
            WaterConnection waterConnection = waterConnectionService.findByConsumerCode(consumerCode);
            if (waterConnection == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_CONSUMERCODE_NOT_EXIST);
                errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONSUMERCODE_NOT_EXIST);
            }
        }
        if (!consumerCode.isEmpty()) {
            WaterConnectionDetails waterConnDetailsObj = waterConnectionDetailsService.findByConsumerCodeAndConnectionStatus(consumerCode,
                    ConnectionStatus.INACTIVE);
            if (waterConnDetailsObj != null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_INACTIVE_CONSUMERNO);
                errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_INACTIVE_CONSUMERNO);
            }
        }
        return errorDetails;
    }

    private List<WaterTaxDetails> isEmptyWaterTaxDetails() {
        final List<WaterTaxDetails> waterTaxDetailsList = new ArrayList<>();
        final WaterTaxDetails watertaxDetails = new WaterTaxDetails();
        final ErrorDetails errordetails = new ErrorDetails();
        errordetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_WATERTAXDETAILS_SIZE);
        errordetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_WATERTAXDETAILS_SIZE);
        watertaxDetails.setErrorDetails(errordetails);
        waterTaxDetailsList.add(watertaxDetails);
        return waterTaxDetailsList;
    }

    private ErrorDetails validateAssessmentNumber(final String assessmentNumber) {
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

    private WaterTaxDetails getWaterTaxDetails(final WaterTaxDetails waterTaxDetails) {
        if (waterTaxDetails.getConsumerNo() == null || "".equals(waterTaxDetails.getConsumerNo()))
            waterTaxDetails.setConsumerNo("");
        if (waterTaxDetails.getOwnerName() == null)
            waterTaxDetails.setOwnerName("");
        if (waterTaxDetails.getLocalityName() == null)
            waterTaxDetails.setLocalityName("");
        if (waterTaxDetails.getPropertyAddress() == null)
            waterTaxDetails.setPropertyAddress("");
        if (waterTaxDetails.getTaxDetails() == null) {
            final RestPropertyTaxDetails ar = new RestPropertyTaxDetails();
            final List<RestPropertyTaxDetails> taxDetails = new ArrayList<RestPropertyTaxDetails>(0);
            taxDetails.add(ar);
            waterTaxDetails.setTaxDetails(taxDetails);
        }
        return waterTaxDetails;
    }

}