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

package org.egov.restapi.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.restapi.model.ImpactAnalysisResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class ImpactAnalysisService {

    private static final Logger LOGGER = Logger.getLogger(ImpactAnalysisService.class);
    private static final int DATA_SIZE = 5;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String RECEIPTCOUNT = "receiptcount";
    private static final String RECEIPTAMOUNT = "receiptamount";
    private static final String COMPLAINTCOUNT = "complaintcount";
    private static final String CITIZENCOUNT = "citizenRegistered";
    private static final String EMPLOYEECOUNT = "employeeRegistered";
    private static final String APPLICATIONCOUNT = "applicationcount";
    private static final String VOUCHERCOUNT = "vouchercount";
    private static final String IMPACTANALYSIS_INDEX = "impactanalysis";
    private static final String ASONDATE = "asonDate";
    private static final String FROMDATE = "fromdate";
    private static final String TODATE = "todate";
    private static final String REVENUEMODULE = "AP";
    private static final Integer ULB_COUNT = 110;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public List<ImpactAnalysisResponse> getImpactAnalysisData(Long date, Long interval) {
        List<ImpactAnalysisResponse> impactAnalysisResponseList = new ArrayList<>();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getImpactAnalysisData date:" + date + " interval:" + interval);
        for (Map<String, String> dateRangeMap : getDateRangeList(date, interval))
            impactAnalysisResponseList.add(fetchImpactAnalysisData(dateRangeMap.get(FROMDATE), dateRangeMap.get(TODATE)));
        return impactAnalysisResponseList;
    }

    private LinkedList<Map<String, String>> getDateRangeList(Long date, Long interval) {
        LinkedList<Map<String, String>> dateRangeList = new LinkedList<>();
        int numberofIntervals = 1;
        Long fromEpoch = date;
        while (numberofIntervals <= DATA_SIZE) {
            HashMap<String, String> dateRangeMap = new HashMap<>();
            LocalDate toDate = Instant.ofEpochMilli(fromEpoch)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate fromDate = Instant.ofEpochMilli(fromEpoch).minusMillis(interval)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            numberofIntervals++;
            dateRangeMap.put(FROMDATE, formatter.format(fromDate));
            dateRangeMap.put(TODATE, formatter.format(toDate));
            dateRangeList.add(dateRangeMap);
            fromEpoch = fromDate.atStartOfDay().atZone(ZoneId.systemDefault())
                    .toInstant().toEpochMilli();
        }
        return dateRangeList;
    }

    public ImpactAnalysisResponse fetchImpactAnalysisData(String fromDate, String toDate) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" fromDate:" + fromDate + " toDate:" + toDate);
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery(ASONDATE).gte(fromDate)
                        .lt(toDate));
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(IMPACTANALYSIS_INDEX)
                .withQuery(boolQuery)
                .addAggregation(AggregationBuilders.sum(RECEIPTCOUNT).field(RECEIPTCOUNT))
                .addAggregation(AggregationBuilders.sum(RECEIPTAMOUNT).field(RECEIPTAMOUNT))
                .addAggregation(AggregationBuilders.sum(COMPLAINTCOUNT).field(COMPLAINTCOUNT))
                .addAggregation(AggregationBuilders.sum(CITIZENCOUNT).field(CITIZENCOUNT))
                .addAggregation(AggregationBuilders.sum(EMPLOYEECOUNT).field(EMPLOYEECOUNT))
                .addAggregation(AggregationBuilders.sum(APPLICATIONCOUNT).field(APPLICATIONCOUNT))
                .addAggregation(AggregationBuilders.sum(VOUCHERCOUNT).field(VOUCHERCOUNT))
                .build();
        final Aggregations aggregations = elasticsearchTemplate.query(searchQuery,
                response -> response.getAggregations());
        return setImpactAnalysisResponse(aggregations, fromDate);
    }

    private ImpactAnalysisResponse setImpactAnalysisResponse(Aggregations aggregations, String fromDate) {
        ImpactAnalysisResponse impactAnalysisResponse = new ImpactAnalysisResponse(REVENUEMODULE, ULB_COUNT);
        impactAnalysisResponse.setReceiptcount(((Sum) aggregations.get(RECEIPTCOUNT)).getValue());
        impactAnalysisResponse.setRevenueCollected(((Sum) aggregations.get(RECEIPTAMOUNT)).getValue());
        impactAnalysisResponse.setComplaintcount(((Sum) aggregations.get(COMPLAINTCOUNT)).getValue());
        impactAnalysisResponse.setNoOfCitizenRegistered(((Sum) aggregations.get(CITIZENCOUNT)).getValue());
        impactAnalysisResponse.setEmployeeRegistered(((Sum) aggregations.get(EMPLOYEECOUNT)).getValue());
        impactAnalysisResponse.setServicesApplied(((Sum) aggregations.get(APPLICATIONCOUNT)).getValue());
        impactAnalysisResponse.setVouchercount(((Sum) aggregations.get(VOUCHERCOUNT)).getValue());
        impactAnalysisResponse.setDate((LocalDate.parse(fromDate, formatter)).atStartOfDay().atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli());
        return impactAnalysisResponse;
    }

}