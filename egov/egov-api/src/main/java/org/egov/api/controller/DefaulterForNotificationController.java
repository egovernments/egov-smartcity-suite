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
package org.egov.api.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.egov.api.controller.core.ApiController;
import org.egov.api.controller.core.ApiResponse;
import org.egov.ptis.domain.entity.property.contract.TaxDefaultersRequest;
import org.egov.ptis.domain.entity.property.view.PropertyMVInfo;
import org.egov.ptis.domain.service.report.PropertyTaxReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class DefaulterForNotificationController extends ApiController{
    
    private static final String INVALID_PAGE_NUMBER_ERROR = "Invalid Page Number";
    private static final String HAS_NEXT_PAGE = "hasNextPage";
    private static final Logger LOGGER = Logger.getLogger(DefaulterForNotificationController.class);
    private static final String EGOV_API_ERROR = "EGOV-API ERROR ";
    private static final String SERVER_ERROR = "server.error";
    
    @Autowired
    private PropertyTaxReportService propertyTaxReportService;
    
    public ApiResponse getResponseHandler() {
        return ApiResponse.newInstance();
    }
    
    @PostMapping(value = "/property/taxDefaulters", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPropertyTaxDefaulters(@RequestBody TaxDefaultersRequest defaultersRequest, final HttpServletRequest request){
        if (defaultersRequest.getPage() < 1)
            return getResponseHandler().error(INVALID_PAGE_NUMBER_ERROR);
        try {
            final Page<PropertyMVInfo> pagelist = propertyTaxReportService.getLatest(defaultersRequest.getMobileOnly(), defaultersRequest.getPage(), defaultersRequest.getPageSize());
            final boolean hasNextPage = pagelist.getTotalElements() > defaultersRequest.getPage() * defaultersRequest.getPageSize();
            return getResponseHandler().putStatusAttribute(HAS_NEXT_PAGE, String.valueOf(hasNextPage))
                    .setDataAdapter(new org.egov.api.adapter.DefaultersResultAdapter()).success(pagelist.getContent());
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR));
        }
    }
}