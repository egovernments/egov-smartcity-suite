/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2016>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.stms.transactions.service;

import java.math.BigDecimal;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.rest.client.SimpleRestClient;
import org.egov.infra.web.utils.WebUtils;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional(readOnly = true)
public class SewerageThirdPartyServices {
    @Autowired
    private SimpleRestClient simpleRestClient;

    private static final String WTMS_TAXDUE_RESTURL = "%s/wtms/rest/watertax/due/byptno/%s";

    public AssessmentDetails getPropertyDetails(final String assessmentNumber, final HttpServletRequest request) {
        final RestTemplate restTemplate = new RestTemplate();
        final String url = "http://" + request.getServerName() + ":" + request.getServerPort()
                + "/ptis/rest/property/{assessmentNumber}";
        final AssessmentDetails propertyOwnerDetails = restTemplate.getForObject(url, AssessmentDetails.class,
                assessmentNumber);
        return propertyOwnerDetails;
    }

    public HashMap<String, Object> getWaterTaxDueAndCurrentTax(final String assessmentNo,
            final HttpServletRequest request) {

        final HashMap<String, Object> result = new HashMap<>();
        result.put("WATERTAXDUE", BigDecimal.ZERO);
        result.put("CURRENTWATERCHARGE", BigDecimal.ZERO);
        final String wtmsRestURL = String.format(WTMS_TAXDUE_RESTURL, WebUtils.extractRequestDomainURL(request, false),
                assessmentNo);
        final HashMap<String, Object> waterTaxInfo = simpleRestClient.getRESTResponseAsMap(wtmsRestURL);
        result.put(
                "WATERTAXDUE",
                waterTaxInfo.get("totalTaxDue") == null ? BigDecimal.ZERO : new BigDecimal(Double
                        .valueOf((Double) waterTaxInfo.get("totalTaxDue"))));
        result.put("CURRENTWATERCHARGE", waterTaxInfo.get("currentDemand") == null ? BigDecimal.ZERO : new BigDecimal(
                Double.valueOf((Double) waterTaxInfo.get("currentDemand"))));
        result.put("PROPERTYID", waterTaxInfo.get("propertyID"));
        result.put("CONSUMERCODE", waterTaxInfo.get("consumerCode"));
        return result;
    }

    public BigDecimal getCurrentWaterTax(final String assessmentNo, final HttpServletRequest request) {
        final String wtmsRestURL = String.format(WTMS_TAXDUE_RESTURL, WebUtils.extractRequestDomainURL(request, false),
                assessmentNo);
        final HashMap<String, Object> waterTaxInfo = simpleRestClient.getRESTResponseAsMap(wtmsRestURL);
        return waterTaxInfo.get("currentDemand") == null ? BigDecimal.ZERO : new BigDecimal(
                Double.valueOf((Double) waterTaxInfo.get("currentDemand")));
    }

}
