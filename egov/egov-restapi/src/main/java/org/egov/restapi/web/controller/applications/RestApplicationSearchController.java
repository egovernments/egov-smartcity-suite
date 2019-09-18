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
package org.egov.restapi.web.controller.applications;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.search.elasticsearch.entity.ApplicationIndex;
import org.egov.search.elasticsearch.entity.enums.ApprovalStatus;
import org.egov.search.elasticsearch.service.ApplicationIndexService;
import org.egov.restapi.web.contracts.applications.ApplicationSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApplicationSearchController {

    @Autowired
    private ApplicationIndexService applicationIndexService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/application/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ApplicationSearchResponse> fetchApplicationsForUser(@RequestParam String ulbCode, @RequestParam Long userId,
            @RequestParam ApprovalStatus status) {
        return fetch(userId, status);
    }

    @RequestMapping(value = "/v1.0/application/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ApplicationSearchResponse> securedFetchAplicationsForUser(@RequestParam String ulbCode, @RequestParam Long userId,
            @RequestParam ApprovalStatus status, OAuth2Authentication auth2Authentication) {
        return fetch(userId, status);
    }

    /**
     * @param userId
     * @param status
     * @return
     */
    public List<ApplicationSearchResponse> fetch(Long userId, ApprovalStatus status) {
        List<ApprovalStatus> approvalStatuses;
        List<ApplicationSearchResponse> responseList = new ArrayList<>();

        User user = userService.getUserById(userId);
        if (status != null)
            approvalStatuses = Arrays.asList(status);
        else
            approvalStatuses = Arrays.asList(ApprovalStatus.values());

        List<ApplicationIndex> applications = applicationIndexService.findAllByCityNameAndCreatedByAndApprovalStatus(user,
                approvalStatuses);

        ApplicationSearchResponse searchResponse;
        for (ApplicationIndex application : applications) {
            searchResponse = new ApplicationSearchResponse(application.getModuleName(), application.getApplicationNumber(),
                    application.getApplicationDate(), application.getApplicationType(), application.getApplicantName(),
                    application.getApplicantAddress(), application.getDisposalDate(), application.getStatus(),
                    application.getConsumerCode(), application.getMobileNumber(), application.getOwnerName(),
                    application.getAadharNumber(), application.getApproved().name(), application.getChannel(),
                    application.getCityCode(), application.getCityName());
            responseList.add(searchResponse);
        }
        if (!responseList.isEmpty()) {
            Collections.sort(responseList, new Comparator<ApplicationSearchResponse>() {
                @Override
                public int compare(ApplicationSearchResponse response1, ApplicationSearchResponse response2) {
                    if (response1.getApplicationDate() != null && response2.getApplicationDate() != null)
                        return response2.getApplicationDate().compareTo(response1.getApplicationDate());
                    return 0;
                }
            });
        }

        return responseList;
    }
}
