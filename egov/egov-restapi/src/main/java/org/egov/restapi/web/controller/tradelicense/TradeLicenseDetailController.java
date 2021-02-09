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

package org.egov.restapi.web.controller.tradelicense;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.integration.event.model.enums.ApplicationStatus;
import org.egov.restapi.model.RestErrors;
import org.egov.restapi.web.contracts.tradelicense.LicenseSimpleDeskRequest;
import org.egov.restapi.web.contracts.tradelicense.TradeLicenseDetailRequest;
import org.egov.restapi.web.contracts.tradelicense.TradeLicenseDetailResponse;
import org.egov.restapi.web.contracts.tradelicense.TradeLicenseSimpleDeskResponse;
import org.egov.tl.service.TradeLicenseService;
import org.egov.wardsecretary.transactions.entity.WSTransactionRequest;
import org.egov.wardsecretary.transactions.entity.contracts.TransactionReconcileSingleDeskRequest;
import org.egov.wardsecretary.transactions.entity.contracts.TransactionReconcileSingleDeskResponse;
import org.egov.wardsecretary.transactions.service.WSTransactionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import static org.egov.infra.config.core.ApplicationThreadLocals.getCityName;
import static org.egov.wardsecretary.utils.constants.WardSecretaryConstants.BLANK;
import static org.egov.wardsecretary.utils.constants.WardSecretaryConstants.TRANSACTION_RECONCILE_001;
import static org.egov.wardsecretary.utils.constants.WardSecretaryConstants.TRANSACTION_RECONCILE_002;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/tradelicense")
public class TradeLicenseDetailController {

    @Autowired
    @Qualifier("tradeLicenseService")
    private TradeLicenseService tradeLicenseService;
    
    @Autowired
	private WSTransactionRequestService wSTransactionRequestService;
    
    @Autowired
	private CityService cityService;

    @GetMapping(value = "/details", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public List<TradeLicenseDetailResponse> tradeLicenseDetails(TradeLicenseDetailRequest request) {
        return tradeLicenseService.getLicenses(request.tradeLicenseLike())
                .parallelStream()
                .map(TradeLicenseDetailResponse::new)
                .collect(Collectors.toList());
    }
    
    @PostMapping(value = "/licenseappicationdetails", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public List<TradeLicenseSimpleDeskResponse> licenseAppicationDetails(@RequestBody LicenseSimpleDeskRequest request) {
    	return tradeLicenseService.getLicenses(request.tradeLicenseLikeSimpledesk())
        		.parallelStream()
        		.map(TradeLicenseSimpleDeskResponse::new)
        		.collect(Collectors.toList());
    }
    @PostMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
	public TransactionReconcileSingleDeskResponse getTransactionStatus(@RequestBody final TransactionReconcileSingleDeskRequest transactionVerifyRequest) {
		
    		TransactionReconcileSingleDeskResponse response = new TransactionReconcileSingleDeskResponse();
	
    		if (StringUtils.isNotBlank(transactionVerifyRequest.getTransactionId())
				|| StringUtils.isNotBlank(transactionVerifyRequest.getApplicationNumber())) {

			WSTransactionRequest wSTransactionRequest = wSTransactionRequestService.getRequestByRequestParametersSingleDesk(transactionVerifyRequest);
			if (null == wSTransactionRequest) {
				response.setTransactionId(transactionVerifyRequest.getTransactionId());
				response.setApplicationNumber(transactionVerifyRequest.getApplicationNumber());
				response.setRemarks(TRANSACTION_RECONCILE_002);
			} else {
				buildResponse(response, wSTransactionRequest);
			}
		} else {
			response.setRemarks(TRANSACTION_RECONCILE_001);
		}
		return response;
	}

	private void buildResponse(TransactionReconcileSingleDeskResponse response, final WSTransactionRequest wSTransactionRequest) {

		response.setTransactionId(wSTransactionRequest.getTpTransactionId());
		response.setTransactionStatus(wSTransactionRequest.getTransactionStatus() == null ? BLANK
				: wSTransactionRequest.getTransactionStatus().toString());
		response.setTransactionDate(wSTransactionRequest.getCreatedDate());
		response.setApplicationStatus(wSTransactionRequest.getApplicationStatus() == null ? BLANK
				: wSTransactionRequest.getApplicationStatus().toString());
		response.setApplicationNumber(
				wSTransactionRequest.getApplicationNo() == null ? BLANK : wSTransactionRequest.getApplicationNo());
		if (wSTransactionRequest.getApplicationStatus() == null)
			response.setApplicationStatus(BLANK);
		else {
			response.setApplicationStatus(wSTransactionRequest.getApplicationStatus().toString());
			if ((wSTransactionRequest.getApplicationStatus().toString().equals(ApplicationStatus.REJECTED.toString())
					|| wSTransactionRequest.getApplicationStatus().toString()
							.equals(ApplicationStatus.APPROVED.toString())
							&& wSTransactionRequest.getDateOfCompletion() != null))
				response.setDateOfCompletion(wSTransactionRequest.getDateOfCompletion());
		}
		response.setDistrict(cityService.getDistrictName() == null ? BLANK : cityService.getDistrictName());
		response.setCityName(getCityName());
		response.setServiceCode(
				wSTransactionRequest.getService() == null ? BLANK : wSTransactionRequest.getService().getCode());
		response.setServiceName(
				wSTransactionRequest.getService() == null ? BLANK : wSTransactionRequest.getService().getName());
		response.setViewLink(wSTransactionRequest.getViewLink() == null ? BLANK : wSTransactionRequest.getViewLink());
		response.setRemarks(wSTransactionRequest.getRemarks());

	}
    @ExceptionHandler(RuntimeException.class)
    public RestErrors restErrors(RuntimeException runtimeException) {
        return new RestErrors("LICENSE NOT EXIST", runtimeException.getMessage());
    }
}
