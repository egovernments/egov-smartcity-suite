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
package org.egov.wtms.web.controller.rest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.egov.wtms.application.rest.WaterChargesDetails;
import org.egov.wtms.application.rest.WaterTaxDue;
import org.egov.wtms.application.service.ConnectionDetailService;
import org.egov.wtms.masters.entity.WaterTaxDetailRequest;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.egov.ptis.constants.PropertyTaxConstants.WATER_TAX_INDEX_NAME;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class RestWaterTaxController {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ConnectionDetailService connectionDetailService;

    /*
     * Returns Total tax due for the water connection for a given ConsumerCode
     */
    @RequestMapping(value = "rest/watertax/due/bycode/{consumerCode}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public WaterTaxDue getWaterTaxDueByConsumerCode(@PathVariable final String consumerCode)
            throws JsonGenerationException, JsonMappingException, IOException {
        return connectionDetailService.getDueDetailsByConsumerCode(consumerCode);

    }

    /*
     * Returns Total tax due for the water connection for a given ConsumerCode from ELastic Search
     */
    @RequestMapping(value = "rest/watertax/totaldemandamount/", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public WaterTaxDue getTotalDemand() throws JsonGenerationException, JsonMappingException, IOException {
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(WATER_TAX_INDEX_NAME)
                .addAggregation(AggregationBuilders.sum("totaldemand").field("totalDemand")).build();
        final Aggregations aggregations = elasticsearchTemplate.query(searchQuery,
                response -> response.getAggregations());
        final Sum aggr = aggregations.get("totaldemand");
        final WaterTaxDue waterTaxDue = new WaterTaxDue();
        waterTaxDue.setCurrentDemand(BigDecimal.valueOf(aggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
        return waterTaxDue;
    }

    /*
     * Returns Total tax due for list of water connections for a given PropertyIdentifier
     */
    @RequestMapping(value = {
            "rest/watertax/due/byptno/{assessmentNumber}" }, method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public WaterTaxDue getWaterTaxDueByPropertyId(@PathVariable final String assessmentNumber)
            throws JsonGenerationException, JsonMappingException, IOException {
        return connectionDetailService.getDueDetailsByPropertyId(assessmentNumber);

    }

    @RequestMapping(value = {
            "rest/watertax/connectiondetails/byptno/{assessmentNumber}" }, method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public List<WaterChargesDetails> getWaterConnectionDetailsByPropertyId(@PathVariable final String assessmentNumber)
            throws JsonGenerationException, JsonMappingException, IOException {
        return connectionDetailService.getWaterTaxDetailsByPropertyId(assessmentNumber, null, null);

    }

    @RequestMapping(value = "rest/watertax/updateConnectionForAmulgamation", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public String updateWaterConnectionForAmalagamation(@RequestBody final WaterTaxDetailRequest waterTaxDetailRequest)
            throws IOException {
        return connectionDetailService.updateWaterConnectionDetails(waterTaxDetailRequest);
    }
}