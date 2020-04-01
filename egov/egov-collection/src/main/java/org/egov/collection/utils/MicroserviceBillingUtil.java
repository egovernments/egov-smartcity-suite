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

package org.egov.collection.utils;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.collection.config.properties.CollectionApplicationProperties;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.integration.models.BillReceiptInfoReq;
import org.egov.collection.integration.models.BillReceiptReq;
import org.egov.collection.integration.models.ReceiptAccountDetailsResponse;
import org.egov.collection.integration.models.ReceiptAmountInfo;
import org.egov.collection.integration.models.ReceiptCancelValidateRequest;
import org.egov.collection.integration.models.ReceiptCancellationInfo;
import org.egov.collection.integration.models.ReconstructReceiptDetailsRequest;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.microservice.utils.MicroserviceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MicroserviceBillingUtil {
    private static final Logger LOGGER = Logger.getLogger(MicroserviceBillingUtil.class);
    @Autowired
    private CollectionApplicationProperties collectionApplicationProperties;
    @Autowired
    MicroserviceUtils microserviceUtils;
    @Autowired
    private FunctionHibernateDAO functionDAO;
    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;
  
    @Autowired
    private MicroserviceCollectionUtil microserviceCollectionUtil;

    public ReceiptAmountInfo updateReceiptDetailsAndGetReceiptAmountInfo(final BillReceiptReq billReceipt,
            final String serviceCode) {
        billReceipt.setTenantId(microserviceUtils.getTanentId());
        final BillReceiptInfoReq billReceiptInfoReq = new BillReceiptInfoReq();
        billReceiptInfoReq.setBillReceiptInfo(billReceipt);
        billReceiptInfoReq.setRequestInfo(microserviceUtils.createRequestInfo());
        final RestTemplate restTemplate = new RestTemplate();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("updateReceiptDetailsAndGetReceiptAmountInfo - before calling LAMS update");
        final String url = collectionApplicationProperties.getLamsServiceUrl().concat(
                collectionApplicationProperties.getUpdateDemandUrl(serviceCode.toLowerCase()));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("updateReceiptDetailsAndGetReceiptAmountInfo - url" + url);
        ReceiptAmountInfo receiptAmountInfo = null;
        try {
            receiptAmountInfo = restTemplate.postForObject(url, billReceiptInfoReq, ReceiptAmountInfo.class);
		} catch (final Exception e) {
            final String rollBackUrl = collectionApplicationProperties.getLamsServiceUrl()
                    .concat(collectionApplicationProperties.getRollBackDemandUrl(serviceCode.toLowerCase()));
            microserviceCollectionUtil.rollBackDemand(rollBackUrl, billReceiptInfoReq);

            final String errMsg = "Exception while updateReceiptDetailsAndGetReceiptAmountInfo for bill number  ["
					+ billReceipt.getBillReferenceNum() + "]!";
			LOGGER.error(errMsg, e);
			throw new ApplicationRuntimeException(errMsg, e);
		}
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("updateReceiptDetailsAndGetReceiptAmountInfo - response" + receiptAmountInfo);
        return receiptAmountInfo;
    }

    public ReceiptCancellationInfo validateCancelReceipt(String receiptNumber, String consumerCode, String serviceCode) {
        ReceiptCancelValidateRequest receiptCancelValidateRequest = new ReceiptCancelValidateRequest();
        receiptCancelValidateRequest.setTenantId(microserviceUtils.getTanentId());
        receiptCancelValidateRequest.setRequestInfo(microserviceUtils.createRequestInfo());
        receiptCancelValidateRequest.setConsumerCode(consumerCode);
        receiptCancelValidateRequest.setReceiptNumber(receiptNumber);
        final RestTemplate restTemplate = new RestTemplate();
        ReceiptCancellationInfo receiptCancellationInfo;
        final String url = collectionApplicationProperties.getLamsServiceUrl().concat(
                collectionApplicationProperties.getReceiptCancelValidateUrl(serviceCode.toLowerCase()));
        try {
            receiptCancellationInfo = restTemplate.postForObject(url, receiptCancelValidateRequest,
                    ReceiptCancellationInfo.class);
        } catch (final Exception e) {
            final String errMsg = "Exception while validate receipt cancellation for receiptnumer:" + receiptNumber;
            LOGGER.error(errMsg, e);
            throw new ApplicationRuntimeException(errMsg, e);
        }
        return receiptCancellationInfo;
    }

    public List<ReceiptDetail> getReconstructReceiptDetails(final ReceiptHeader receiptHeader) {
        final RestTemplate restTemplate = new RestTemplate();
        // Prepare request
        ReconstructReceiptDetailsRequest reconstructReceiptDetailsRequest = new ReconstructReceiptDetailsRequest();
        reconstructReceiptDetailsRequest.setRequestInfo(microserviceUtils.createRequestInfo());
        reconstructReceiptDetailsRequest.setTotalAmount(receiptHeader.getTotalAmount());
        reconstructReceiptDetailsRequest.setBillId(receiptHeader.getReferencenumber());
        reconstructReceiptDetailsRequest.setTenantId(microserviceUtils.getTanentId());

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("reconstruct receipt details - before calling LAMS");
        final String url = collectionApplicationProperties.getLamsServiceUrl().concat(
                collectionApplicationProperties
                        .getReconstructReceiptDetailUrl(receiptHeader.getService().getCode().toLowerCase()));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("reconstruct receipt details - url:" + url);
        ReceiptAccountDetailsResponse receiptAccountDetailsResponse = restTemplate.postForObject(url,
                reconstructReceiptDetailsRequest, ReceiptAccountDetailsResponse.class);
        if (receiptAccountDetailsResponse == null)
            return Collections.emptyList();
        // Setting chartofaccounts and function
        else {
            if (!receiptAccountDetailsResponse.getReceiptDetailsList().isEmpty()) {
                final CFunction function = functionDAO
                        .getFunctionByCode(receiptAccountDetailsResponse.getReceiptDetailsList().get(0).getFunction().getCode());
                for (ReceiptDetail receiptDetail : receiptAccountDetailsResponse.getReceiptDetailsList()) {
                    final CChartOfAccounts account = chartOfAccountsHibernateDAO
                            .getCChartOfAccountsByGlCode(receiptDetail.getAccounthead().getGlcode());
                    receiptDetail.setAccounthead(account);
                    receiptDetail.setFunction(function);
                }
            }
            return receiptAccountDetailsResponse.getReceiptDetailsList();
        }
    }

}