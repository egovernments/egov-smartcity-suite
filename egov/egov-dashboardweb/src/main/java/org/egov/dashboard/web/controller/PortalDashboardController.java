/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.dashboard.web.controller;

import java.util.List;

import org.egov.infra.elasticsearch.entity.bean.ApplicationIndexRequest;
import org.egov.infra.elasticsearch.entity.bean.ApplicationIndexResponse;
import org.egov.infra.elasticsearch.entity.bean.ApplicationInfo;
import org.egov.infra.elasticsearch.service.es.ApplicationDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = { "/public/portal", "/portal" })
public class PortalDashboardController {

    @Autowired
    private ApplicationDocumentService applicationDocumentService;

    /**
     * Provides application index details
     * @return response JSON
     */
    @RequestMapping(value = "/applicationdetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ApplicationIndexResponse getApplicationDetails(final ApplicationIndexRequest applicationIndexRequest) {
        return applicationDocumentService.findAllApplications(applicationIndexRequest);
    }

    /**
     * Provides service group wise application index details
     * @return response JSON
     */
    @RequestMapping(value = "/serviceGroupWiseApplicationdetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ApplicationIndexResponse getServiceGroupWiseApplicationDetails(final ApplicationIndexRequest applicationIndexRequest) {
        return applicationDocumentService.findServiceGroupWiseApplications(applicationIndexRequest);
    }

    @RequestMapping(value = "/servicewisedetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ApplicationIndexResponse getServiceWiseDetails(ApplicationIndexRequest applicationIndexRequest) {
        return applicationDocumentService.findServiceWiseDetails(applicationIndexRequest);
    }
    
    @RequestMapping(value = "/sourceWiseApplicationDetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ApplicationIndexResponse getSourceWiseApplicationDetails(ApplicationIndexRequest applicationIndexRequest) {
        return applicationDocumentService.findSourceWiseApplicationDetails(applicationIndexRequest);
    }
    
    @RequestMapping(value = "/applications", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ApplicationInfo> getApplications(ApplicationIndexRequest applicationIndexRequest) {
        return applicationDocumentService.getApplicationInfo(applicationIndexRequest);
    }
}
