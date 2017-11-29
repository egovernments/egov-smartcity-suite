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

import org.apache.log4j.Logger;
import org.egov.collection.config.properties.CollectionApplicationProperties;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.models.ServiceDetails;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The PaymentRequestAdaptor class frames the request object for the payment service.
 */
public class SbimopsAdaptor implements PaymentGatewayAdaptor {
    private static final Logger LOGGER = Logger.getLogger(SbimopsAdaptor.class);
    @Autowired
    private CollectionApplicationProperties collectionApplicationProperties;

    private static final String SBIMOPS_HOAPREFIX = "NVN";
    private static final String SBIMOPS_PMD = "P";

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
        sbiPaymentRequest.setParameter(CollectionConstants.SBIMOPS_MD, SBIMOPS_PMD);
        sbiPaymentRequest.setParameter(CollectionConstants.SBIMOPS_DEPTCODE, ApplicationThreadLocals.getCityCode());
        sbiPaymentRequest.setParameter(CollectionConstants.SBIMOPS_DDCODE,
                collectionApplicationProperties.sbimopsDdocode(ApplicationThreadLocals.getCityCode()).toString());
        final StringBuilder hoa = new StringBuilder(
                collectionApplicationProperties.sbimopsHoa(ApplicationThreadLocals.getCityCode()));
        int hoaLength = hoa.length() + 3;
        while (hoaLength < 22) { // length of the HOA should be 22
            hoa.append("0");
            hoaLength = hoa.length() + 3;
        }
        hoa.append(SBIMOPS_HOAPREFIX);
        sbiPaymentRequest.setParameter(CollectionConstants.SBIMOPS_DEPTTRANSID, receiptHeader.getId().toString());
        sbiPaymentRequest.setParameter(CollectionConstants.SBIMOPS_REMITTER_NAME, receiptHeader.getConsumerCode());
        sbiPaymentRequest.setParameter(CollectionConstants.SBIMOPS_TAMOUNT, receiptHeader.getTotalAmount());
        sbiPaymentRequest.setParameter(CollectionConstants.SBIMOPS_UAMOUNT, new BigDecimal(0));
        sbiPaymentRequest.setParameter(CollectionConstants.SBIMOPS_BANK_NAME, CollectionConstants.SERVICECODE_SBIMOPS);
        sbiPaymentRequest.setParameter(CollectionConstants.SBIMOPS_HOA, hoa);
        final StringBuilder returnUrl = new StringBuilder(paymentServiceDetails.getCallBackurl());
        returnUrl.append("?paymentServiceId=").append(paymentServiceDetails.getId());
        sbiPaymentRequest.setParameter(CollectionConstants.SBIMOPS_DRU, returnUrl.toString());
        sbiPaymentRequest.setParameter(CollectionConstants.ONLINEPAYMENT_INVOKE_URL, paymentServiceDetails.getServiceUrl());
        final Map<String, Object> requestParameters = sbiPaymentRequest.getRequestParameters();
        LOGGER.info(CollectionConstants.SBIMOPS_MD + "=" + requestParameters.get(CollectionConstants.SBIMOPS_MD) + "|" +
                CollectionConstants.SBIMOPS_DEPTCODE + "=" + requestParameters.get(CollectionConstants.SBIMOPS_DEPTCODE) + "|" +
                CollectionConstants.SBIMOPS_DDCODE + "=" + requestParameters.get(CollectionConstants.SBIMOPS_DDCODE) + "|" +
                CollectionConstants.SBIMOPS_HOA + "=" + requestParameters.get(CollectionConstants.SBIMOPS_HOA) + "|" +
                CollectionConstants.SBIMOPS_DEPTTRANSID + "=" + requestParameters.get(CollectionConstants.SBIMOPS_DEPTTRANSID)
                + "|" +
                CollectionConstants.SBIMOPS_REMITTER_NAME + "=" + requestParameters.get(CollectionConstants.SBIMOPS_REMITTER_NAME)
                + "|" +
                CollectionConstants.SBIMOPS_TAMOUNT + "=" + requestParameters.get(CollectionConstants.SBIMOPS_TAMOUNT) + "|" +
                CollectionConstants.SBIMOPS_DRU + "=" + requestParameters.get(CollectionConstants.SBIMOPS_DRU) + "|" +
                CollectionConstants.ONLINEPAYMENT_INVOKE_URL + "="
                + requestParameters.get(CollectionConstants.ONLINEPAYMENT_INVOKE_URL));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("End SbimopsAdaptor-createPaymentRequest");

        return sbiPaymentRequest;
    }

    @Override
    public PaymentResponse parsePaymentResponse(final String response) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Insider SbimopsAdaptor-parsePaymentResponse");
        final DefaultPaymentResponse sbiPaymentResponse = new DefaultPaymentResponse();
        final Map<String, String> fields = new HashMap<String, String>(0);
        sbiPaymentResponse.setAuthStatus(fields.get(CollectionConstants.SBIMOPS_BANKSTATUS).equalsIgnoreCase("Success")
                ? CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS : fields.get(CollectionConstants.SBIMOPS_BANKSTATUS));
        sbiPaymentResponse.setErrorDescription(fields.get(CollectionConstants.SBIMOPS_BANKSTATUS));
        sbiPaymentResponse.setReceiptId(fields.get(CollectionConstants.SBIMOPS_DEPTTRANSID));
        sbiPaymentResponse.setAdditionalInfo6(fields.get(CollectionConstants.SBIMOPS_REMITTER_NAME));
        sbiPaymentResponse.setTxnAmount(new BigDecimal(fields.get(CollectionConstants.SBIMOPS_BANK_AMOUNT)));
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("", Locale.getDefault());
        Date transDate = null;
        try {
            transDate = simpleDateFormat.parse(fields.get(CollectionConstants.SBIMOPS_BANK_DATE));
            sbiPaymentResponse.setTxnDate(transDate);
        } catch (final ParseException e) {
            LOGGER.error("Error in parsing transaction date [" + fields.get(CollectionConstants.SBIMOPS_BANK_DATE) + "]", e);
            throw new ApplicationRuntimeException(".transactiondate.parse.error", e);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("End SbimopsAdaptor-parsePaymentResponse");
        return sbiPaymentResponse;
    }

}
