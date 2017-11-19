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

import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.commons.Bank;
import org.egov.commons.dao.BankHibernateDAO;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.restapi.constants.RestApiConstants;
import org.egov.restapi.util.JsonConvertor;
import org.egov.stms.entity.PaySewerageTaxDetails;
import org.egov.stms.entity.SewerageReceiptDetails;
import org.egov.stms.entity.SewerageTaxDetails;
import org.egov.stms.entity.SewerageTaxPaidDetails;
import org.egov.stms.masters.entity.enums.SewerageConnectionStatus;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageDemandService;
import org.egov.stms.transactions.service.SewerageThirdPartyServices;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SewerageCollectionController {
    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;
    @Autowired
    private SewerageThirdPartyServices sewerageThirdPartyServices;
    @Autowired
    private SewerageDemandService sewerageDemandService;
    @Autowired
    private BankHibernateDAO bankHibernateDAO;

    @RequestMapping(value = "/sewerage/payseweragetax", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String paySewerageTax(@Valid @RequestBody final PaySewerageTaxDetails paySewerageTaxDetails,
            final HttpServletRequest request) {
        SewerageReceiptDetails sewerageReceiptDetails = null;
        try {

            final ErrorDetails errorDetails = validatePaymentDetails(paySewerageTaxDetails);
            if (null != errorDetails)
                return JsonConvertor.convert(errorDetails);
            else {
                paySewerageTaxDetails.setSource(request.getSession().getAttribute("source") != null
                        ? request.getSession().getAttribute("source").toString() : "");
                sewerageReceiptDetails = sewerageThirdPartyServices.paySewerageTax(paySewerageTaxDetails, request);
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
            e.printStackTrace();
            final List<ErrorDetails> errorList = new ArrayList<>(0);
            final ErrorDetails er = new ErrorDetails();
            er.setErrorCode(e.getMessage());
            er.setErrorMessage(e.getMessage());
            errorList.add(er);
            JsonConvertor.convert(errorList);
        }
        return JsonConvertor.convert(sewerageReceiptDetails);
    }

    public ErrorDetails validatePaymentDetails(final PaySewerageTaxDetails paySewerageTaxDetails) {
        ErrorDetails errorDetails;
        SewerageApplicationDetails sewerageApplicationDtlObject = null;
        errorDetails = validateConsumerAndApplicationNumber(paySewerageTaxDetails);

        if (paySewerageTaxDetails.getApplicaionNumber() != null && !"".equals(paySewerageTaxDetails.getApplicaionNumber()))
            sewerageApplicationDtlObject = sewerageApplicationDetailsService
                    .findByApplicationNumber(paySewerageTaxDetails.getApplicaionNumber());
        else if (paySewerageTaxDetails.getConsumerNo() != null)
            sewerageApplicationDtlObject = sewerageApplicationDetailsService
                    .findByConnectionShscNumberAndConnectionStatus(paySewerageTaxDetails.getConsumerNo(),
                            SewerageConnectionStatus.ACTIVE);
        if (sewerageApplicationDtlObject == null) {
            sewerageApplicationDtlObject = sewerageApplicationDetailsService.findByConnectionShscNumberAndConnectionStatus(
                    paySewerageTaxDetails.getConsumerNo(), SewerageConnectionStatus.INACTIVE);
            if (sewerageApplicationDtlObject != null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_INACTIVE_CONSUMERNO);
                errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_INACTIVE_CONSUMERNO);
            } else {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_CONSUMER_NO_VALID);
                errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONSUMER_NO_VALID);
            }
        } else {
            BigDecimal totalAmt = BigDecimal.ZERO;
            if (sewerageApplicationDtlObject != null
                    && sewerageApplicationDtlObject.getConnection().getStatus().equals(SewerageConnectionStatus.ACTIVE)) {
                EgDemand sewerageDemand = sewerageApplicationDtlObject.getCurrentDemand();
                if (sewerageDemand != null) {
                    for (final EgDemandDetails demandDtl : sewerageDemand.getEgDemandDetails()) {

                        if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                .equalsIgnoreCase(SewerageTaxConstants.FEES_SEWERAGETAX_CODE)
                                && demandDtl.getAmount()
                                        .compareTo((demandDtl.getAmtCollected() == null) ? BigDecimal.ZERO
                                                : demandDtl.getAmtCollected()) > 0) {
                            totalAmt = totalAmt.add(demandDtl.getAmount().subtract(
                                    (demandDtl.getAmtCollected() == null) ? BigDecimal.ZERO : demandDtl.getAmtCollected()));
                        }
                    }
                }
            }

            if (totalAmt.compareTo(BigDecimal.ZERO) > 0) {
                if (totalAmt.compareTo(paySewerageTaxDetails.getTotalAmount()) < 0) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_ADVANCE_NOTALLOWED);
                    errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_ADVANCE_NOTALLOWED);
                }
            } else if (totalAmt.compareTo(BigDecimal.ZERO) == 0) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_NOPAYMENT_PENDING);
                errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_NOPAYMENT_PENDING);
            }
        }

        if (paySewerageTaxDetails.getTransactionId() == null || "".equals(paySewerageTaxDetails.getTransactionId())) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_TRANSANCTIONID_REQUIRED);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_TRANSANCTIONID_REQUIRED);
        } else if (paySewerageTaxDetails.getTransactionId() != null || !"".equals(paySewerageTaxDetails.getTransactionId())) {
            final BillReceiptInfo billReceipt = sewerageThirdPartyServices
                    .validateTransanctionIdPresent(paySewerageTaxDetails.getTransactionId());
            if (billReceipt != null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_TRANSANCTIONID_VALIDATE);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_TRANSANCTIONID_VALIDATE);

            }
        }
        if (paySewerageTaxDetails.getPaymentMode() == null || paySewerageTaxDetails.getPaymentMode().trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PAYMENT_MODE_REQUIRED);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PAYMENT_MODE_REQUIRED);
        } else if (!PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CASH
                .equalsIgnoreCase(paySewerageTaxDetails.getPaymentMode().trim())
                && !PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CHEQUE
                        .equalsIgnoreCase(paySewerageTaxDetails.getPaymentMode().trim())

                && !PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_DD
                        .equalsIgnoreCase(paySewerageTaxDetails.getPaymentMode().trim())) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PAYMENT_MODE_INVALID);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PAYMENT_MODE_INVALID);
        }

        if (paySewerageTaxDetails.getPaymentMode() != null && (PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CHEQUE
                .equalsIgnoreCase(paySewerageTaxDetails.getPaymentMode().trim())
                || PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_DD
                        .equalsIgnoreCase(paySewerageTaxDetails.getPaymentMode().trim()))) {
            if (paySewerageTaxDetails.getChqddNo() == null || paySewerageTaxDetails.getChqddNo().trim().length() == 0) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_CHQDD_NO_REQUIRED);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_CHQDD_NO_REQUIRED);
            } else {

                if (paySewerageTaxDetails.getChqddDate() == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_CHQDD_DATE_REQUIRED);
                    errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_CHQDD_DATE_REQUIRED);
                } else {

                    if (paySewerageTaxDetails.getBankName() == null || paySewerageTaxDetails.getBankName().trim().length() == 0) {
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_BANKNAME_REQUIRED);
                        errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_BANKNAME_REQUIRED);
                    } else {

                        if (paySewerageTaxDetails.getBranchName() == null) {
                            errorDetails = new ErrorDetails();
                            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_BRANCHNAME_REQUIRED);
                            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_BRANCHNAME_REQUIRED);
                        } else {
                            if (!validateBank(paySewerageTaxDetails.getBankName())) {
                                errorDetails = new ErrorDetails();
                                errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_WRONG_BANK_NAME_CODE);
                                errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_WRONG_BANK_NAME_CODE);
                            }

                        }
                    }
                }
            }
        }
        return errorDetails;
    }

    private Boolean validateBank(final String bankCodeOrName) {

        Bank bank = bankHibernateDAO.getBankByCode(bankCodeOrName);
        return bank != null ? true : false;

    }

    @RequestMapping(value = "/sewerage/getseweragedetails", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String getSewerageTaxDetailsByAppLicationOrConsumerNumber(
            @Valid @RequestBody final PaySewerageTaxDetails paySewerageTaxDetails)
            throws IOException, BindException {

        final ErrorDetails errorDetails = validateConsumerAndApplicationNumber(paySewerageTaxDetails);
        if (null != errorDetails)
            return JsonConvertor.convert(errorDetails);
        else {
            final SewerageTaxDetails sewerageTaxDetails = sewerageDemandService
                    .getSeweragTaxeDemandDetails(paySewerageTaxDetails);
            return JsonConvertor.convert(getSewerageTaxDetails(sewerageTaxDetails));
        }
    }

    private ErrorDetails validateConsumerAndApplicationNumber(final PaySewerageTaxDetails paySewerageTaxDetails) {
        ErrorDetails errorDetails = null;
        if (paySewerageTaxDetails.getConsumerNo() == null || paySewerageTaxDetails.getConsumerNo().trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_CONSUMER_NO_REQUIRED);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONSUMER__NO_REQUIRED);
        } else if (paySewerageTaxDetails.getConsumerNo().trim().length() > 0
                && paySewerageTaxDetails.getConsumerNo().trim().length() < 10) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_CONSUMER_NO_LEN);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_CONSUMER_NO_LEN);
        }
        if (paySewerageTaxDetails.getConsumerNo() == null && paySewerageTaxDetails.getApplicaionNumber() == null) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_APPLICATION_NO_REQUIRED);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_APPLICATION__NO_REQUIRED);
        } else if (paySewerageTaxDetails.getApplicaionNumber() != null
                && paySewerageTaxDetails.getApplicaionNumber().trim().length() > 0
                && paySewerageTaxDetails.getApplicaionNumber().trim().length() < 13) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_APPLICATION_NO_LEN);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_APPLICATION_NO_LEN);
        }
        SewerageApplicationDetails sewerageApplicationDetails = null;
        if (paySewerageTaxDetails.getConsumerNo() != null)
            sewerageApplicationDetails = sewerageApplicationDetailsService.findByConnectionShscNumberAndConnectionStatus(
                    paySewerageTaxDetails.getConsumerNo(), SewerageConnectionStatus.INACTIVE);
        if (sewerageApplicationDetails != null) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_INACTIVE_CONSUMERNO);
            errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_MSG_INACTIVE_CONSUMERNO);
        }
        return errorDetails;
    }

    private SewerageTaxDetails getSewerageTaxDetails(final SewerageTaxDetails sewerageTaxDetails) {
        if (sewerageTaxDetails.getConsumerNo() == null || "".equals(sewerageTaxDetails.getConsumerNo()))
            sewerageTaxDetails.setConsumerNo("");
        if (sewerageTaxDetails.getOwnerName() == null)
            sewerageTaxDetails.setOwnerName("");
        if (sewerageTaxDetails.getLocalityName() == null)
            sewerageTaxDetails.setLocalityName("");
        if (sewerageTaxDetails.getPropertyAddress() == null)
            sewerageTaxDetails.setPropertyAddress("");
        if (sewerageTaxDetails.getTaxDetails() == null) {
            final SewerageTaxPaidDetails ar = new SewerageTaxPaidDetails();
            final List<SewerageTaxPaidDetails> taxDetails = new ArrayList<>(0);
            taxDetails.add(ar);
            sewerageTaxDetails.setTaxDetails(taxDetails);
        }
        return sewerageTaxDetails;
    }
}
