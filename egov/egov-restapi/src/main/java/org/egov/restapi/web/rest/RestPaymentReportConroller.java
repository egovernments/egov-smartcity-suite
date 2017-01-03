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
package org.egov.restapi.web.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.integration.models.PaymentInfoRequest;
import org.egov.collection.integration.models.PaymentInfoSearchRequest;
import org.egov.collection.integration.models.RestAggregatePaymentInfo;
import org.egov.collection.integration.models.RestReceiptInfo;
import org.egov.collection.integration.services.CollectionIntegrationService;
import org.egov.commons.Bank;
import org.egov.commons.dao.BankHibernateDAO;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infstr.models.ServiceCategory;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.restapi.constants.RestApiConstants;
import org.egov.restapi.model.RestErrors;
import org.egov.restapi.model.RestResponse;
import org.egov.restapi.util.JsonConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestPaymentReportConroller {

    private static final Logger LOGGER = Logger.getLogger(RestPaymentReportConroller.class);

    @Autowired
    private CollectionIntegrationService collectionService;

    @Autowired
    private PersistenceService<ServiceCategory, Long> serviceCategoryService;

    @Autowired
    private BankHibernateDAO bankHibernateDAO;

    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping(value = "/reconciliation/paymentdetails/transaction", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String searchPaymentByTransactionId(@RequestBody final PaymentInfoSearchRequest paymentInfoSearchRequest,
            final HttpServletRequest request) {
        paymentInfoSearchRequest.setSource(request.getSession().getAttribute("source") != null ? request.getSession()
                .getAttribute("source").toString() : "");

        LOGGER.info(request.getSession().getAttribute("source"));
        final RestResponse detailsByTransactionId = new RestResponse();
        final RestErrors err = new RestErrors();
        try {
            final RestReceiptInfo detailsByTransactionId2 = collectionService
                    .getDetailsByTransactionId(paymentInfoSearchRequest);
            detailsByTransactionId.setStatus(RestApiConstants.THIRD_PARTY_ACTION_SUCCESS);
            detailsByTransactionId.setAmount(detailsByTransactionId2.getAmount());
            detailsByTransactionId.setReceiptNo(detailsByTransactionId2.getReceiptNo());
            detailsByTransactionId.setReferenceNo(detailsByTransactionId2.getReferenceNo());
            detailsByTransactionId.setTransactionId(detailsByTransactionId2.getTransactionId());
            detailsByTransactionId.setPaymentPeriod(detailsByTransactionId2.getPaymentPeriod());
            detailsByTransactionId.setPaymentType(detailsByTransactionId2.getPaymentType());
            err.setErrorMessage(RestApiConstants.THIRD_PARTY_ACTION_SUCCESS);
            err.setErrorCode(RestApiConstants.THIRD_PARTY_ACTION_SUCCESS);
            detailsByTransactionId.getErrorDetails().add(err);

        } catch (final Exception e) {
            detailsByTransactionId.setStatus(RestApiConstants.THIRD_PARTY_ACTION_FAILURE);
            err.setErrorMessage(e.getMessage());
            err.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_DATA_FOUND);
            detailsByTransactionId.getErrorDetails().add(err);
        }
        return JsonConvertor.convert(detailsByTransactionId);

    }

    @RequestMapping(value = "/getPaymentByUserServiceAndConsumerCode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPaymentByUserServiceAndConsumerCode(@Valid @RequestBody final PaymentInfoRequest paymentInfoRequest) {

        RestResponse restResponse;
        final List<RestResponse> restResponseList = new ArrayList<>();

        final RestErrors err = new RestErrors();
        try {
            final List<RestReceiptInfo> restReceiptInfo = collectionService
                    .getDetailsByUserServiceAndConsumerCode(paymentInfoRequest);
            for (final RestReceiptInfo restReceipt : restReceiptInfo) {
                restResponse = new RestResponse();
                restResponse.setStatus(RestApiConstants.THIRD_PARTY_ACTION_SUCCESS);
                restResponse.setAmount(restReceipt.getAmount());
                restResponse.setReceiptNo(restReceipt.getReceiptNo());
                restResponse.setReferenceNo(restReceipt.getReferenceNo());
                restResponse.setServiceName(restReceipt.getServiceName());
                restResponse.setTxnDate(restReceipt.getTxnDate());
                restResponse.setPayeeName(restReceipt.getPayeeName());
                restResponse.setReceiptStatus(restReceipt.getReceiptStatus());
                err.setErrorMessage(RestApiConstants.THIRD_PARTY_ACTION_SUCCESS);
                err.setErrorCode(RestApiConstants.THIRD_PARTY_ACTION_SUCCESS);
                restResponse.getErrorDetails().add(err);
                restResponseList.add(restResponse);
            }

        } catch (final Exception e) {
            restResponse = new RestResponse();
            restResponse.setStatus(RestApiConstants.THIRD_PARTY_ACTION_FAILURE);
            err.setErrorMessage(e.getMessage());
            err.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_NO_DATA_FOUND);
            restResponse.getErrorDetails().add(err);
            restResponseList.add(restResponse);
        }
        return JsonConvertor.convert(restResponseList);
    }

    @RequestMapping(value = "/reconciliation/paymentaggregate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String searchAggregatePaymentsByDate(@RequestBody final PaymentInfoSearchRequest paymentInfoSearchRequest,
            final HttpServletRequest request) throws JsonGenerationException, JsonMappingException, IOException {
        LOGGER.info(request.getSession().getAttribute("source"));
        paymentInfoSearchRequest.setSource(request.getSession().getAttribute("source") != null ? request.getSession()
                .getAttribute("source").toString() : "");
        final List<RestAggregatePaymentInfo> listAggregatePaymentInfo = collectionService
                .getAggregateReceiptTotal(paymentInfoSearchRequest);
        return JsonConvertor.convert(listAggregatePaymentInfo);
    }

    @RequestMapping(value = "/reconciliation/paymentdetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String searchPaymentDetailsByServiceAndDate(
            @RequestBody final PaymentInfoSearchRequest paymentInfoSearchRequest, final HttpServletRequest request)
            throws JsonGenerationException, JsonMappingException, IOException {
        paymentInfoSearchRequest.setSource(request.getSession().getAttribute("source") != null ? request.getSession()
                .getAttribute("source").toString() : "");
        final List<RestReceiptInfo> receiptInfoList = collectionService
                .getReceiptDetailsByDateAndService(paymentInfoSearchRequest);
        return JsonConvertor.convert(receiptInfoList);
    }

    @RequestMapping(value = "/cancelReceipt", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String cancelReceipt(@RequestBody final PaymentInfoSearchRequest paymentInfoSearchRequest,
            final BindingResult errors) {

        final ErrorDetails successDetail = new ErrorDetails();
        try {

            validateCancelReceipt(paymentInfoSearchRequest);
            if (paymentInfoSearchRequest.getReceiptNo() != null && !paymentInfoSearchRequest.getReceiptNo().isEmpty()) {
                ApplicationThreadLocals.setUserId(Long.valueOf("2"));
                final String cancelReceipt = collectionService.cancelReceipt(paymentInfoSearchRequest);
                successDetail.setErrorCode(RestApiConstants.THIRD_PARTY_ACTION_SUCCESS);
                successDetail.setErrorMessage(cancelReceipt);
            } else {
                final ErrorDetails errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(RestApiConstants.THIRD_PARTY_ERR_CODE_RECEIPT_NO_REQUIRED);
                errorDetails.setErrorMessage(RestApiConstants.THIRD_PARTY_ERR_CODE_RECEIPT_NO_REQ_MSG);
                return JsonConvertor.convert(errorDetails);
            }

        } catch (final Exception e) {
            final ErrorDetails er = new ErrorDetails();
            er.setErrorCode(RestApiConstants.THIRD_PARTY_ACTION_FAILURE);
            er.setErrorMessage(e.getMessage());
            return JsonConvertor.convert(er);
        }
        return JsonConvertor.convert(successDetail);

    }

    private void validateCancelReceipt(final PaymentInfoSearchRequest cancelReq) {
        if (cancelReq.getTransactionId() == null || cancelReq.getTransactionId().isEmpty())
            throw new RuntimeException(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_TRANSANCTIONID_REQUIRED);
        else if (cancelReq.getReceiptNo() == null || cancelReq.getReceiptNo().isEmpty())
            throw new RuntimeException(RestApiConstants.THIRD_PARTY_ERR_CODE_RECEIPT_NO_REQ_MSG);
        else if (cancelReq.getUlbCode() == null || cancelReq.getUlbCode().isEmpty())
            throw new RuntimeException(RestApiConstants.THIRD_PARTY_ERR_CODE_ULBCODE_NO_REQ_MSG);
        else if (cancelReq.getReferenceNo() == null || cancelReq.getReferenceNo().isEmpty())
            throw new RuntimeException(RestApiConstants.THIRD_PARTY_ERR_CODE_REFNO_NO_REQ_MSG);

    }

    @RequestMapping(value = "/banks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String bankNames() {
        List<Bank> banks = null;
        try {
            banks = bankHibernateDAO.findAll();

        } catch (final Exception e) {
            final ErrorDetails er = new ErrorDetails();
            er.setErrorCode(e.getMessage());
            er.setErrorMessage(e.getMessage());
            return JsonConvertor.convert(er);
        }
        return JsonConvertor.convert(banks);

    }

    @RequestMapping(value = "/downloadReceipt", method = RequestMethod.POST)
    public ResponseEntity<byte[]> downloadReceiptByReceiptAndConsumerNo(
            @RequestBody final PaymentInfoSearchRequest paymentInfoSearchRequest) {
        ResponseEntity<byte[]> receipt = null;
        byte[] receiptPdf = null;

        if (paymentInfoSearchRequest.getReceiptNo() != null && !paymentInfoSearchRequest.getReceiptNo().isEmpty()
                && paymentInfoSearchRequest.getReferenceNo() != null
                && !paymentInfoSearchRequest.getReferenceNo().isEmpty()) {
            receiptPdf = collectionService.downloadReceiptByReceiptAndConsumerNo(
                    paymentInfoSearchRequest.getReceiptNo(), paymentInfoSearchRequest.getReferenceNo());
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            headers.add("content-disposition", "inline;filename=Receipt.pdf");
            receipt = new ResponseEntity<byte[]>(receiptPdf, headers, HttpStatus.CREATED);
        } else if (receipt == null)
            receipt = new ResponseEntity("File Not Found", HttpStatus.OK);
        return receipt;
    }

    @RequestMapping(value = "/services", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String services() throws JsonGenerationException, JsonMappingException, IOException {

        Map<String, String> serviceCategory = null;
        List<ServiceCategory> services;
        try {
            services = serviceCategoryService.findAllByNamedQuery(CollectionConstants.QUERY_ACTIVE_SERVICE_CATEGORY);
            if (services != null && services.size() >= 0) {
                serviceCategory = new LinkedHashMap<String, String>();
                for (final ServiceCategory scs : services)
                    serviceCategory.put(scs.getCode(), scs.getName());
            }
        } catch (final Exception e) {
            final ErrorDetails er = new ErrorDetails();
            er.setErrorCode(e.getMessage());
            er.setErrorMessage(e.getMessage());
            return JsonConvertor.convert(er);

        }
        return JsonConvertor.convert(serviceCategory);

    }
}
