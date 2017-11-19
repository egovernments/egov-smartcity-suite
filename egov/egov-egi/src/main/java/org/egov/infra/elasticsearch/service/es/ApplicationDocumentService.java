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

package org.egov.infra.elasticsearch.service.es;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.config.mapper.BeanMapperConfiguration;
import org.egov.infra.elasticsearch.entity.ApplicationIndex;
import org.egov.infra.elasticsearch.entity.bean.ApplicationDetails;
import org.egov.infra.elasticsearch.entity.bean.ApplicationIndexRequest;
import org.egov.infra.elasticsearch.entity.bean.ApplicationIndexResponse;
import org.egov.infra.elasticsearch.entity.bean.ApplicationInfo;
import org.egov.infra.elasticsearch.entity.bean.ServiceDetails;
import org.egov.infra.elasticsearch.entity.bean.ServiceGroupDetails;
import org.egov.infra.elasticsearch.entity.bean.ServiceGroupTrend;
import org.egov.infra.elasticsearch.entity.bean.SourceTrend;
import org.egov.infra.elasticsearch.entity.bean.Trend;
import org.egov.infra.elasticsearch.entity.es.ApplicationDocument;
import org.egov.infra.elasticsearch.repository.es.ApplicationDocumentRepository;
import org.egov.infra.utils.DateUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ApplicationDocumentService {

    private static final String OWNER_NAME = "ownerName";

    private static final String CITY_CODE = "cityCode";

    private static final String CITY_GRADE = "cityGrade";

    private static final String APPLICATION_DATE = "applicationDate";

    private static final String CITY_NAME = "cityName";

    private static final String SOURCE_SYSTEM = "SYSTEM";

    private static final String SOURCE_ONLINE = "ONLINE";

    private static final String SOURCE_MEESEVA = "MEESEVA";

    private static final String SOURCE_CSC = "CSC";

    private static final String SLA_GAP = "slaGap";

    private static final String OTHERS_TOTAL = "othersTotal";

    private static final String ULB_TOTAL = "ulbTotal";

    private static final String ONLINE_TOTAL = "onlineTotal";

    private static final String MEESEVA_TOTAL = "meesevaTotal";

    private static final String CSC_TOTAL = "cscTotal";

    private static final String SLAB4BEYOND_SLA = "slab4beyondSLA";

    private static final String SLAB3BEYOND_SLA = "slab3beyondSLA";

    private static final String SLAB2BEYOND_SLA = "slab2beyondSLA";

    private static final String SLAB1BEYOND_SLA = "slab1beyondSLA";

    private static final String OPEN_BEYOND_SLA = "openBeyondSLA";

    private static final String OPEN_WITHIN_SLA = "openWithinSLA";

    private static final String CLOSED_BEYOND_SLA = "closedBeyondSLA";

    private static final String CLOSED_WITHIN_SLA = "closedWithinSLA";

    private static final String CHANNEL = "channel";

    private static final String APPLICATION_TYPE = "applicationType";

    private static final String REGION_NAME = "regionName";

    private static final String MODULE_NAME = "moduleName";

    private static final String APPLICATION_NUMBER = "applicationNumber";

    private static final String APPLICATIONS_INDEX = "applications";

    private static final String IS_CLOSED = "isClosed";

    private static final String DATE_AGGR = "date_aggr";

    private static final String DISTRICT_NAME = "districtName";

    private static final String OPEN = "open";

    private static final String CLOSED = "closed";

    private static final String RECEIVED = "received";

    private static final String TOTAL_COUNT = "total_count";

    private static final String TOTAL_BEYOND_SLA = "totalBeyondSLA";

    private static final String TOTAL_WITHIN_SLA = "totalWithinSLA"; 
    
    private static final String MODULE_PROPERTY_TAX = "Property Tax";
    
    private static final String MODULE_WATER_TAX = "Water Charges";
    
    private static final String MODULE_TRADE_LICENSE = "Trade License";
    
    private static final String MODULE_ADVERTISEMENT_TAX = "Advertisement Tax";
    
    private static final String MODULE_MARRIAGE_REGISTRATION = "Marriage Registration";
    
    private static final String SLA = "sla";

    private final ApplicationDocumentRepository applicationDocumentRepository;

    @Autowired
    private BeanMapperConfiguration beanMapperConfiguration;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    private static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
    private static final SimpleDateFormat DATEFORMATTER_YYYY_MM_DD = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD);

    @Autowired
    public ApplicationDocumentService(ApplicationDocumentRepository applicationDocumentRepository) {
        this.applicationDocumentRepository = applicationDocumentRepository;
    }

    @Transactional
    public ApplicationDocument createOrUpdateApplicationDocument(ApplicationIndex applicationIndex) {
        ApplicationDocument applicationDocument = beanMapperConfiguration.map(applicationIndex, ApplicationDocument.class);
        return applicationDocumentRepository.save(applicationDocument);
    }

    /**
     * Provides Applications details
     * @param applicationIndexRequest
     * @return ApplicationIndexResponse
     */
    public ApplicationIndexResponse findAllApplications(ApplicationIndexRequest applicationIndexRequest) {
        ApplicationIndexResponse applicationIndexResponse = new ApplicationIndexResponse();
        Aggregations aggregation;
        ValueCount valueCount;
        Date fromDate = null;
        Date toDate = null;
        if (StringUtils.isNotBlank(applicationIndexRequest.getFromDate())
                && StringUtils.isNotBlank(applicationIndexRequest.getToDate())) {
            fromDate = DateUtils.getDate(applicationIndexRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(DateUtils.getDate(applicationIndexRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        }
        aggregation = getDocumentCounts(applicationIndexRequest, fromDate, toDate, StringUtils.EMPTY, RECEIVED, StringUtils.EMPTY,
                0);
        if (aggregation != null) {
            valueCount = aggregation.get(TOTAL_COUNT);
            applicationIndexResponse.setTotalReceived(valueCount != null ? valueCount.getValue() : 0);
        }
        aggregation = getDocumentCounts(applicationIndexRequest, fromDate, toDate, StringUtils.EMPTY, CLOSED, StringUtils.EMPTY,
                0);
        if (aggregation != null) {
            valueCount = aggregation.get(TOTAL_COUNT);
            applicationIndexResponse.setTotalClosed(valueCount != null ? valueCount.getValue() : 0);
        }
        aggregation = getDocumentCounts(applicationIndexRequest, fromDate, toDate, StringUtils.EMPTY, OPEN, StringUtils.EMPTY, 0);
        if (aggregation != null) {
            valueCount = aggregation.get(TOTAL_COUNT);
            applicationIndexResponse.setTotalOpen(valueCount != null ? valueCount.getValue() : 0);
        }

        // For today's counts
        fromDate = new Date();
        toDate = DateUtils.addDays(new Date(), 1);
        aggregation = getDocumentCounts(applicationIndexRequest, fromDate, toDate, StringUtils.EMPTY, CLOSED, StringUtils.EMPTY,
                0);
        if (aggregation != null) {
            valueCount = aggregation.get(TOTAL_COUNT);
            applicationIndexResponse.setTodaysClosed(valueCount != null ? valueCount.getValue() : 0);
        }
        aggregation = getDocumentCounts(applicationIndexRequest, fromDate, toDate, StringUtils.EMPTY, RECEIVED, StringUtils.EMPTY,
                0);
        if (aggregation != null) {
            valueCount = aggregation.get(TOTAL_COUNT);
            applicationIndexResponse.setTodaysReceived(valueCount != null ? valueCount.getValue() : 0);
        }
        List<Trend> applicationTrends = getMonthwiseApplicationTrends(applicationIndexRequest);
        applicationIndexResponse.setTrend(applicationTrends);
        List<ApplicationDetails> applicationDetails = getAnalysisTableResponse(applicationIndexRequest);
        applicationIndexResponse.setDetails(applicationDetails);
        return applicationIndexResponse;
    }

    /**
     * Provides Service group wise applications details
     * 
     * @param applicationIndexRequest
     * @return ApplicationIndexResponse
     */
    public ApplicationIndexResponse findServiceGroupWiseApplications(ApplicationIndexRequest applicationIndexRequest) {
        ApplicationIndexResponse applicationIndexResponse = new ApplicationIndexResponse();
        Aggregations aggregation;
        ValueCount valueCount;
        Date fromDate = null;
        Date toDate = null;
        List<ServiceGroupDetails> serviceGroups = new ArrayList<>();
        if (StringUtils.isNotBlank(applicationIndexRequest.getFromDate())
                && StringUtils.isNotBlank(applicationIndexRequest.getToDate())) {
            fromDate = DateUtils.getDate(applicationIndexRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(DateUtils.getDate(applicationIndexRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        }
        aggregation = getDocumentCounts(applicationIndexRequest, fromDate, toDate, MODULE_NAME, RECEIVED, MODULE_NAME,
                0);
        if (aggregation != null) {
            StringTerms aggr = aggregation.get(MODULE_NAME);
            for (Terms.Bucket entry : aggr.getBuckets()) {
                ServiceGroupDetails serviceGroup = new ServiceGroupDetails();
                valueCount = entry.getAggregations().get(TOTAL_COUNT);
                serviceGroup.setServiceGroup(String.valueOf(entry.getKey()));
                serviceGroup.setTotalReceived(valueCount.getValue());
                serviceGroups.add(serviceGroup);
            }
        }
        aggregation = getDocumentCounts(applicationIndexRequest, fromDate, toDate, MODULE_NAME, OPEN, MODULE_NAME, 0);
        if (aggregation != null) {
            StringTerms aggr = aggregation.get(MODULE_NAME);
            for (Terms.Bucket entry : aggr.getBuckets()) {
                String moduleName = String.valueOf(entry.getKey());
                for (ServiceGroupDetails serviceGroup : serviceGroups) {
                    if (serviceGroup.getServiceGroup().equals(moduleName)) {
                        valueCount = entry.getAggregations().get(TOTAL_COUNT);
                        serviceGroup.setTotalOpen(valueCount.getValue());
                        break;
                    }
                }
            }
        }
        applicationIndexResponse.setServiceGroupDetails(serviceGroups);
        List<ServiceGroupTrend> serviceGroupTrend = getMonthwiseServiceGroupApplicationTrends(applicationIndexRequest);
        applicationIndexResponse.setServiceGroupTrend(serviceGroupTrend);
        return applicationIndexResponse;
    }

    /**
     * Provides month-wise service group application trends - received/open
     * @param applicationIndexRequest
     * @return list
     */
    public List<ServiceGroupTrend> getMonthwiseServiceGroupApplicationTrends(
            ApplicationIndexRequest applicationIndexRequest) {
        Date fromDate = null;
        Date toDate = null;
        Map<Integer, String> monthValuesMap = DateUtils.getAllMonthsWithFullNames();
        List<ServiceGroupTrend> receivedApplications = new ArrayList<>();
        Aggregations aggregation;

        if (StringUtils.isNotBlank(applicationIndexRequest.getFromDate())
                && StringUtils.isNotBlank(applicationIndexRequest.getToDate())) {
            fromDate = DateUtils.getDate(applicationIndexRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(DateUtils.getDate(applicationIndexRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        }

        aggregation = getMonthwiseServiceGroupApplications(applicationIndexRequest, fromDate,
                toDate, RECEIVED);

        Histogram aggr = aggregation.get(DATE_AGGR);
        prepareMonthwiseServiceGroupDetails(monthValuesMap, receivedApplications, aggr);

        return receivedApplications;
    }

    /**
     * Provides Source wise applications details
     * 
     * @param applicationIndexRequest
     * @return ApplicationIndexResponse
     */
    public ApplicationIndexResponse findSourceWiseApplicationDetails(ApplicationIndexRequest applicationIndexRequest) {
        ApplicationIndexResponse applicationIndexResponse = new ApplicationIndexResponse();
        Aggregations aggregation;
        ValueCount valueCount;
        Date fromDate = null;
        Date toDate = null;
        if (StringUtils.isNotBlank(applicationIndexRequest.getFromDate())
                && StringUtils.isNotBlank(applicationIndexRequest.getToDate())) {
            fromDate = DateUtils.getDate(applicationIndexRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(DateUtils.getDate(applicationIndexRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        }
        aggregation = getDocumentCounts(applicationIndexRequest, fromDate, toDate, StringUtils.EMPTY, RECEIVED, StringUtils.EMPTY,
                0);
        if (aggregation != null) {
            valueCount = aggregation.get(TOTAL_COUNT);
            applicationIndexResponse.setTotalReceived(valueCount != null ? valueCount.getValue() : 0);
        }
        aggregation = getDocumentCounts(applicationIndexRequest, fromDate, toDate, StringUtils.EMPTY, MEESEVA_TOTAL,
                StringUtils.EMPTY,
                0);
        if (aggregation != null) {
            valueCount = aggregation.get(TOTAL_COUNT);
            applicationIndexResponse.setTotalMeeseva(valueCount != null ? valueCount.getValue() : 0);
        }
        aggregation = getDocumentCounts(applicationIndexRequest, fromDate, toDate, StringUtils.EMPTY, CSC_TOTAL,
                StringUtils.EMPTY,
                0);
        if (aggregation != null) {
            valueCount = aggregation.get(TOTAL_COUNT);
            applicationIndexResponse.setTotalCsc(valueCount != null ? valueCount.getValue() : 0);
        }
        aggregation = getDocumentCounts(applicationIndexRequest, fromDate, toDate, StringUtils.EMPTY, ULB_TOTAL,
                StringUtils.EMPTY,
                0);
        if (aggregation != null) {
            valueCount = aggregation.get(TOTAL_COUNT);
            applicationIndexResponse.setTotalUlb(valueCount != null ? valueCount.getValue() : 0);
        }
        aggregation = getDocumentCounts(applicationIndexRequest, fromDate, toDate, StringUtils.EMPTY, ONLINE_TOTAL,
                StringUtils.EMPTY,
                0);
        if (aggregation != null) {
            valueCount = aggregation.get(TOTAL_COUNT);
            applicationIndexResponse.setTotalOnline(valueCount != null ? valueCount.getValue() : 0);
        }
        aggregation = getDocumentCounts(applicationIndexRequest, fromDate, toDate, StringUtils.EMPTY, OTHERS_TOTAL,
                StringUtils.EMPTY,
                0);
        if (aggregation != null) {
            valueCount = aggregation.get(TOTAL_COUNT);
            applicationIndexResponse.setTotalOthers(valueCount != null ? valueCount.getValue() : 0);
        }

        List<SourceTrend> sourceTrend = getMonthwiseSourceApplicationTrends(applicationIndexRequest);
        applicationIndexResponse.setSourceTrend(sourceTrend);
        return applicationIndexResponse;
    }

    /**
     * Provides month-wise source application trends - received/open
     * @param applicationIndexRequest
     * @return list
     */
    public List<SourceTrend> getMonthwiseSourceApplicationTrends(
            ApplicationIndexRequest applicationIndexRequest) {
        Date fromDate = null;
        Date toDate = null;
        Map<Integer, String> monthValuesMap = DateUtils.getAllMonthsWithFullNames();
        List<SourceTrend> receivedApplications = new ArrayList<>();
        Aggregations aggregation;

        if (StringUtils.isNotBlank(applicationIndexRequest.getFromDate())
                && StringUtils.isNotBlank(applicationIndexRequest.getToDate())) {
            fromDate = DateUtils.getDate(applicationIndexRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(DateUtils.getDate(applicationIndexRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        }

        aggregation = getMonthwiseSourceApplications(applicationIndexRequest, fromDate,
                toDate, RECEIVED);

        Histogram aggr = aggregation.get(DATE_AGGR);
        prepareMonthwiseSourceDetails(monthValuesMap, receivedApplications, aggr);

        return receivedApplications;
    }

    private void prepareMonthwiseSourceDetails(Map<Integer, String> monthValuesMap,
            List<SourceTrend> applications,
            Histogram aggr) {
        String[] dateArr;
        Integer month;
        String monthName;
        for (Histogram.Bucket entry : aggr.getBuckets()) {
            SourceTrend sourceTrend = new SourceTrend();
            dateArr = entry.getKeyAsString().split("T");
            month = Integer.valueOf(dateArr[0].split("-", 3)[1]);
            monthName = monthValuesMap.get(month);
            StringTerms termAggr = entry.getAggregations().get(CHANNEL);
            for (Bucket term : termAggr.getBuckets()) {
                populateTotal(sourceTrend, term, term.getKeyAsString());
            }
            sourceTrend.setMonth(monthName);
            applications.add(sourceTrend);
        }
    }

    private void populateTotal(final SourceTrend sourceTrend, final Bucket term, final String channel) {
        final ValueCount countAggr = term.getAggregations().get(TOTAL_COUNT);
        if (SOURCE_CSC.equals(channel))
            sourceTrend.setTotalCsc(countAggr.getValue());
        else if (SOURCE_MEESEVA.equals(channel))
            sourceTrend.setTotalMeeseva(countAggr.getValue());
        else if (SOURCE_SYSTEM.equals(channel))
            sourceTrend.setTotalUlb(countAggr.getValue());
        else if (SOURCE_ONLINE.equals(channel))
            sourceTrend.setTotalOnline(countAggr.getValue());
        else
            sourceTrend.setTotalOthers(countAggr.getValue());
    }

    @SuppressWarnings("rawtypes")
    private Aggregations getMonthwiseSourceApplications(ApplicationIndexRequest applicationIndexRequest,
            Date fromDate, Date toDate, String status) {

        BoolQueryBuilder boolQuery = prepareWhereClause(applicationIndexRequest, fromDate, toDate);

        if (CLOSED.equalsIgnoreCase(status))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(IS_CLOSED, 1));
        else if (OPEN.equalsIgnoreCase(status))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(IS_CLOSED, 0));

        AggregationBuilder aggregationBuilder = AggregationBuilders.dateHistogram(DATE_AGGR).field(APPLICATION_DATE)
                .interval(DateHistogramInterval.MONTH);

        aggregationBuilder.subAggregation(AggregationBuilders.terms(CHANNEL).field(CHANNEL)
                .subAggregation(AggregationBuilders.count(TOTAL_COUNT).field(APPLICATION_NUMBER)));

        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(APPLICATIONS_INDEX)
                .withQuery(boolQuery)
                .addAggregation(aggregationBuilder).build();

        return elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());
    }

    private void prepareMonthwiseServiceGroupDetails(Map<Integer, String> monthValuesMap,
            List<ServiceGroupTrend> applications,
            Histogram aggr) {
        String[] dateArr;
        Integer month;
        ValueCount countAggr;
        String monthName;
        for (Histogram.Bucket entry : aggr.getBuckets()) {
            ServiceGroupTrend serviceGroupTrend = new ServiceGroupTrend();
            dateArr = entry.getKeyAsString().split("T");
            month = Integer.valueOf(dateArr[0].split("-", 3)[1]);
            monthName = monthValuesMap.get(month);
            StringTerms termAggr = entry.getAggregations().get(MODULE_NAME);
            List<ServiceGroupDetails> serviceGroupDetails = new ArrayList<>();
            for (Bucket term : termAggr.getBuckets()) {
                ServiceGroupDetails details = new ServiceGroupDetails();
                countAggr = term.getAggregations().get(TOTAL_COUNT);
                details.setServiceGroup(term.getKeyAsString());
                details.setTotalReceived(countAggr.getValue());
                serviceGroupDetails.add(details);
            }
            serviceGroupTrend.setMonth(monthName);
            serviceGroupTrend.setServiceGroupDetails(serviceGroupDetails);
            applications.add(serviceGroupTrend);
        }
    }

    private BoolQueryBuilder prepareWhereClause(ApplicationIndexRequest applicationIndexRequest, Date fromDate, Date toDate) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.matchQuery(APPLICATION_NUMBER, StringUtils.EMPTY));

        if (StringUtils.isNotBlank(applicationIndexRequest.getRegion()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery(REGION_NAME, applicationIndexRequest.getRegion()));
        if (StringUtils.isNotBlank(applicationIndexRequest.getDistrict()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery(DISTRICT_NAME, applicationIndexRequest.getDistrict()));
        if (StringUtils.isNotBlank(applicationIndexRequest.getGrade()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery(CITY_GRADE, applicationIndexRequest.getGrade()));
        if (StringUtils.isNotBlank(applicationIndexRequest.getUlbCode()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery(CITY_CODE, applicationIndexRequest.getUlbCode()));
        if (StringUtils.isNotBlank(applicationIndexRequest.getServiceGroup()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery(MODULE_NAME, applicationIndexRequest.getServiceGroup()));
        if (StringUtils.isNotBlank(applicationIndexRequest.getService()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery(APPLICATION_TYPE, applicationIndexRequest.getService()));
        if (StringUtils.isNotBlank(applicationIndexRequest.getSource()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery(CHANNEL, applicationIndexRequest.getSource()));
        if (fromDate != null && toDate != null)
            boolQuery = boolQuery
                    .filter(QueryBuilders.rangeQuery(APPLICATION_DATE).gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                            .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false));
        if (StringUtils.isNotBlank(applicationIndexRequest.getFunctionaryCode())) {
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery(OWNER_NAME, applicationIndexRequest.getFunctionaryCode()));
            if (StringUtils.isNotBlank(applicationIndexRequest.getClosed())) {
                if ("Y".equalsIgnoreCase(applicationIndexRequest.getClosed()))
                    boolQuery = boolQuery
                            .filter(QueryBuilders.matchQuery(IS_CLOSED, 1));
                else
                    boolQuery = boolQuery
                            .filter(QueryBuilders.matchQuery(IS_CLOSED, 0));
            }
            if (StringUtils.isNotBlank(applicationIndexRequest.getBeyondSLA()))
                boolQuery = filterBasedOnSLAForFunctionary(applicationIndexRequest, boolQuery);
        }

        return boolQuery;
    }

    private BoolQueryBuilder filterBasedOnSLAForFunctionary(ApplicationIndexRequest applicationIndexRequest,
            BoolQueryBuilder boolQuery) {
        BoolQueryBuilder slaQuery = boolQuery;
        if ("Y".equalsIgnoreCase(applicationIndexRequest.getBeyondSLA())) {
            if (StringUtils.isNotBlank(applicationIndexRequest.getAgeing())) {
                if ("0-1Wdays".equalsIgnoreCase(applicationIndexRequest.getAgeing()))
                    slaQuery = slaQuery
                            .filter(QueryBuilders.rangeQuery(SLA_GAP).gte(1).lt(8));
                else if ("1W-1M".equalsIgnoreCase(applicationIndexRequest.getAgeing()))
                    slaQuery = slaQuery
                            .filter(QueryBuilders.rangeQuery(SLA_GAP).gte(8).lt(31));
                else if ("1M-3M".equalsIgnoreCase(applicationIndexRequest.getAgeing()))
                    slaQuery = slaQuery
                            .filter(QueryBuilders.rangeQuery(SLA_GAP).gte(31).lt(91));
                else
                    slaQuery = slaQuery.filter(QueryBuilders.rangeQuery(SLA_GAP).gte(91));
            } else
                slaQuery = slaQuery
                        .filter(QueryBuilders.rangeQuery(SLA_GAP).gt(0));
        } else
            slaQuery = slaQuery
                    .filter(QueryBuilders.rangeQuery(SLA_GAP).lte(0));
        return slaQuery;
    }

    private Map<String, Long> getAggregationWiseApplicationCounts(ApplicationIndexRequest applicationIndexRequest, Date fromDate,
            Date toDate,
            String aggregationName, String status, String aggregationField, int size) {
        Map<String, Long> aggregationResults = new HashMap<>();
        ValueCount valueCount;
        Aggregations aggregation = getDocumentCounts(applicationIndexRequest, fromDate, toDate, aggregationName, status,
                aggregationField, size);
        StringTerms cityAggr = aggregation.get(aggregationName);
        for (Terms.Bucket entry : cityAggr.getBuckets()) {
            valueCount = entry.getAggregations().get(TOTAL_COUNT);
            aggregationResults.put(String.valueOf(entry.getKey()), valueCount.getValue());
        }
        return aggregationResults;
    }

    /**
     * Provides analysis table response
     * @param applicationIndexRequest
     * @return list
     */
    private List<ApplicationDetails> getAnalysisTableResponse(ApplicationIndexRequest applicationIndexRequest) {
        List<ApplicationDetails> applicationDetailsList = new ArrayList<>();
        Date fromDate = null;
        Date toDate = null;
        String aggregationField = DISTRICT_NAME;
        int size = 15;
        String name;
        ApplicationDetails applicationDetails;
        Map detailsForAggregationField = Collections.emptyMap();
        Map<String, Long> delayDaysMap = new HashMap<>();
        Map<String, List<ApplicationDetails>> moduleWiseDetailsMap = new LinkedHashMap<>();
        List<ApplicationDetails> modulewiseDetails;

        if (StringUtils.isNotBlank(applicationIndexRequest.getAggregationLevel())) {
            Map<String, String> aggrTypeAndSize = fetchAggregationTypeAndSize(applicationIndexRequest.getAggregationLevel());
            aggregationField = aggrTypeAndSize.get("aggregationField");
            size = Integer.parseInt(aggrTypeAndSize.get("size"));
        }

        if (StringUtils.isNotBlank(applicationIndexRequest.getFromDate())
                && StringUtils.isNotBlank(applicationIndexRequest.getToDate())) {
            fromDate = DateUtils.getDate(applicationIndexRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(DateUtils.getDate(applicationIndexRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        }

        Map<String, Long> totalRcvdApplications = getAggregationWiseApplicationCounts(applicationIndexRequest, fromDate, toDate,
                "totalReceived", RECEIVED, aggregationField, size);
        Map<String, Long> totalClosedApplications = getAggregationWiseApplicationCounts(applicationIndexRequest, fromDate, toDate,
                "totalClosed", CLOSED, aggregationField, size);
        Map<String, Long> totalOpenApplications = getAggregationWiseApplicationCounts(applicationIndexRequest, fromDate, toDate,
                "totalOpen", OPEN, aggregationField, size);
        Map<String, Long> closedWithinSLAApplications = getAggregationWiseApplicationCounts(applicationIndexRequest, fromDate,
                toDate,
                CLOSED_WITHIN_SLA, CLOSED_WITHIN_SLA, aggregationField, size);
        Map<String, Long> closedBeyondSLAApplications = getAggregationWiseApplicationCounts(applicationIndexRequest, fromDate,
                toDate, CLOSED_BEYOND_SLA, CLOSED_BEYOND_SLA, aggregationField, size);
        Map<String, Long> openWithinSLAApplications = getAggregationWiseApplicationCounts(applicationIndexRequest, fromDate,
                toDate,
                OPEN_WITHIN_SLA, OPEN_WITHIN_SLA, aggregationField, size);
        Map<String, Long> openBeyondSLAApplications = getAggregationWiseApplicationCounts(applicationIndexRequest, fromDate,
                toDate,
                OPEN_BEYOND_SLA, OPEN_BEYOND_SLA, aggregationField, size);
        Map<String, Long> slab1beyondSLAApplications = getAggregationWiseApplicationCounts(applicationIndexRequest, fromDate,
                toDate,
                SLAB1BEYOND_SLA, SLAB1BEYOND_SLA, aggregationField, size);
        Map<String, Long> slab2beyondSLAApplications = getAggregationWiseApplicationCounts(applicationIndexRequest, fromDate,
                toDate,
                SLAB2BEYOND_SLA, SLAB2BEYOND_SLA, aggregationField, size);
        Map<String, Long> slab3beyondSLAApplications = getAggregationWiseApplicationCounts(applicationIndexRequest, fromDate,
                toDate,
                SLAB3BEYOND_SLA, SLAB3BEYOND_SLA, aggregationField, size);
        Map<String, Long> slab4beyondSLAApplications = getAggregationWiseApplicationCounts(applicationIndexRequest, fromDate,
                toDate,
                SLAB4BEYOND_SLA, SLAB4BEYOND_SLA, aggregationField, size);
        Map<String, Long> cscApplications = getAggregationWiseApplicationCounts(applicationIndexRequest, fromDate, toDate,
                CSC_TOTAL, CSC_TOTAL, aggregationField, size);
        Map<String, Long> meesevaApplications = getAggregationWiseApplicationCounts(applicationIndexRequest, fromDate,
                toDate,
                MEESEVA_TOTAL, MEESEVA_TOTAL, aggregationField, size);
        Map<String, Long> onlineApplications = getAggregationWiseApplicationCounts(applicationIndexRequest, fromDate, toDate,
                ONLINE_TOTAL, ONLINE_TOTAL, aggregationField, size);
        Map<String, Long> ulbApplications = getAggregationWiseApplicationCounts(applicationIndexRequest, fromDate, toDate,
                ULB_TOTAL, ULB_TOTAL, aggregationField, size);
        Map<String, Long> otherApplications = getAggregationWiseApplicationCounts(applicationIndexRequest, fromDate, toDate,
                OTHERS_TOTAL, OTHERS_TOTAL, aggregationField, size);

        if (APPLICATION_TYPE.equalsIgnoreCase(aggregationField) || OWNER_NAME.equalsIgnoreCase(aggregationField))
            delayDaysMap = getDelayedDaysAggregate(applicationIndexRequest, fromDate, toDate, aggregationField, size);

        for (Map.Entry<String, Long> entry : totalRcvdApplications.entrySet()) {
            applicationDetails = new ApplicationDetails();
            name = entry.getKey();
            if (DISTRICT_NAME.equalsIgnoreCase(aggregationField) || CITY_NAME.equalsIgnoreCase(aggregationField)
                    || APPLICATION_TYPE.equalsIgnoreCase(aggregationField) || OWNER_NAME.equalsIgnoreCase(aggregationField))
                detailsForAggregationField = getDetailsForAggregationType(applicationIndexRequest, fromDate, toDate, name,
                        aggregationField);

            if (DISTRICT_NAME.equalsIgnoreCase(aggregationField)) {
                applicationDetails.setDistrict(name);
                if (!detailsForAggregationField.isEmpty())
                    applicationDetails.setRegion(detailsForAggregationField.get(REGION_NAME).toString());
            } else if (REGION_NAME.equalsIgnoreCase(aggregationField))
                applicationDetails.setRegion(name);
            else if (CITY_NAME.equalsIgnoreCase(aggregationField)) {
                applicationDetails.setUlbName(name);
                if (!detailsForAggregationField.isEmpty()) {
                    applicationDetails.setRegion(detailsForAggregationField.get(REGION_NAME).toString());
                    applicationDetails.setDistrict(detailsForAggregationField.get(DISTRICT_NAME).toString());
                    applicationDetails.setGrade(detailsForAggregationField.get(CITY_GRADE).toString());
                    applicationDetails.setUlbCode(detailsForAggregationField.get(CITY_CODE).toString());
                }
            } else if (APPLICATION_TYPE.equalsIgnoreCase(aggregationField)) {
                applicationDetails.setServiceType(name);
                if (!detailsForAggregationField.isEmpty()) {
                    applicationDetails.setServiceGroup(detailsForAggregationField.get(MODULE_NAME).toString());
                    applicationDetails.setSlaPeriod((Integer) detailsForAggregationField.get(SLA));
                }
                if (!delayDaysMap.isEmpty() && delayDaysMap.get(name) != null)
                    applicationDetails.setDelayedDays(delayDaysMap.get(name));
            } else if (CITY_GRADE.equalsIgnoreCase(aggregationField))
                applicationDetails.setGrade(name);
            else if (MODULE_NAME.equalsIgnoreCase(aggregationField))
                applicationDetails.setServiceGroup(name);
            else if (CHANNEL.equalsIgnoreCase(aggregationField))
                applicationDetails.setSource(name);
            else if (OWNER_NAME.equalsIgnoreCase(aggregationField)) {
                applicationDetails.setFunctionaryName(name);
                if (StringUtils.isNotBlank(applicationIndexRequest.getService()))
                    applicationDetails.setServiceType(applicationIndexRequest.getService());
                if (!detailsForAggregationField.isEmpty()) {
                    applicationDetails.setUlbName(detailsForAggregationField.get(CITY_NAME).toString());
                    applicationDetails.setSlaPeriod((Integer) detailsForAggregationField.get(SLA));
                }
                if (!delayDaysMap.isEmpty() && delayDaysMap.get(name) != null)
                    applicationDetails.setDelayedDays(delayDaysMap.get(name));
            }

            applicationDetails.setTotalReceived(entry.getValue());
            applicationDetails.setTotalClosed(totalClosedApplications.get(name) == null ? 0 : totalClosedApplications.get(name));
            applicationDetails.setTotalOpen(totalOpenApplications.get(name) == null ? 0 : totalOpenApplications.get(name));
            applicationDetails.setClosedWithinSLA(
                    closedWithinSLAApplications.get(name) == null ? 0 : closedWithinSLAApplications.get(name));
            applicationDetails.setClosedBeyondSLA(
                    closedBeyondSLAApplications.get(name) == null ? 0 : closedBeyondSLAApplications.get(name));
            applicationDetails
                    .setOpenWithinSLA(openWithinSLAApplications.get(name) == null ? 0 : openWithinSLAApplications.get(name));
            applicationDetails
                    .setOpenBeyondSLA(openBeyondSLAApplications.get(name) == null ? 0 : openBeyondSLAApplications.get(name));
            applicationDetails
                    .setSlab1beyondSLA(slab1beyondSLAApplications.get(name) == null ? 0 : slab1beyondSLAApplications.get(name));
            applicationDetails
                    .setSlab2beyondSLA(slab2beyondSLAApplications.get(name) == null ? 0 : slab2beyondSLAApplications.get(name));
            applicationDetails
                    .setSlab3beyondSLA(slab3beyondSLAApplications.get(name) == null ? 0 : slab3beyondSLAApplications.get(name));
            applicationDetails
                    .setSlab4beyondSLA(slab4beyondSLAApplications.get(name) == null ? 0 : slab4beyondSLAApplications.get(name));
            applicationDetails.setCscTotal(cscApplications.get(name) == null ? 0 : cscApplications.get(name));
            applicationDetails.setMeesevaTotal(meesevaApplications.get(name) == null ? 0 : meesevaApplications.get(name));
            applicationDetails.setOnlineTotal(onlineApplications.get(name) == null ? 0 : onlineApplications.get(name));
            applicationDetails.setUlbTotal(ulbApplications.get(name) == null ? 0 : ulbApplications.get(name));
            applicationDetails.setOthersTotal(otherApplications.get(name) == null ? 0 : otherApplications.get(name));

            if (APPLICATION_TYPE.equalsIgnoreCase(aggregationField)) {
                if (moduleWiseDetailsMap.get(applicationDetails.getServiceGroup()) == null) {
                    modulewiseDetails = new ArrayList<>();
                    modulewiseDetails.add(applicationDetails);
                    moduleWiseDetailsMap.put(applicationDetails.getServiceGroup(), modulewiseDetails);
                } else
                    moduleWiseDetailsMap.get(applicationDetails.getServiceGroup()).add(applicationDetails);
            } else
                applicationDetailsList.add(applicationDetails);
        }

        if (APPLICATION_TYPE.equalsIgnoreCase(aggregationField)) {
            if (moduleWiseDetailsMap.get(MODULE_PROPERTY_TAX) != null)
                applicationDetailsList.addAll(moduleWiseDetailsMap.get(MODULE_PROPERTY_TAX));
            if (moduleWiseDetailsMap.get(MODULE_WATER_TAX) != null)
                applicationDetailsList.addAll(moduleWiseDetailsMap.get(MODULE_WATER_TAX));
            if (moduleWiseDetailsMap.get(MODULE_TRADE_LICENSE) != null)
                applicationDetailsList.addAll(moduleWiseDetailsMap.get(MODULE_TRADE_LICENSE));
            if (moduleWiseDetailsMap.get(MODULE_ADVERTISEMENT_TAX) != null)
                applicationDetailsList.addAll(moduleWiseDetailsMap.get(MODULE_ADVERTISEMENT_TAX));
            if (moduleWiseDetailsMap.get(MODULE_MARRIAGE_REGISTRATION) != null)
                applicationDetailsList.addAll(moduleWiseDetailsMap.get(MODULE_MARRIAGE_REGISTRATION));
        }
        return applicationDetailsList;
    }

    private Map<String, String> fetchAggregationTypeAndSize(String aggregationLevel) {
        Map<String, String> aggrTypeAndSize = new HashMap<>();
        String aggregationField = DISTRICT_NAME;
        int size = 15;
        if ("region".equalsIgnoreCase(aggregationLevel)) {
            aggregationField = REGION_NAME;
            size = 4;
        } else if ("district".equalsIgnoreCase(aggregationLevel)) {
            aggregationField = DISTRICT_NAME;
            size = 15;
        } else if ("grade".equalsIgnoreCase(aggregationLevel)) {
            aggregationField = CITY_GRADE;
            size = 7;
        } else if ("ulb".equalsIgnoreCase(aggregationLevel)) {
            aggregationField = CITY_NAME;
            size = 112;
        } else if ("module".equalsIgnoreCase(aggregationLevel)) {
            aggregationField = MODULE_NAME;
            size = 6;
        } else if ("service".equalsIgnoreCase(aggregationLevel)) {
            aggregationField = APPLICATION_TYPE;
            size = 27;
        } else if ("source".equalsIgnoreCase(aggregationLevel)) {
            aggregationField = CHANNEL;
            size = 5;
        } else if ("functionary".equalsIgnoreCase(aggregationLevel)) {
            aggregationField = OWNER_NAME;
            size = 1000;
        }
        aggrTypeAndSize.put("aggregationField", aggregationField);
        aggrTypeAndSize.put("size", String.valueOf(size));
        return aggrTypeAndSize;
    }

    /**
     * Provides month-wise Application trends - received/closed/open
     * @param applicationIndexRequest
     * @return list
     */
    public List<Trend> getMonthwiseApplicationTrends(ApplicationIndexRequest applicationIndexRequest) {
        List<Trend> trendsList = new ArrayList<>();
        Date fromDate = null;
        Date toDate = null;
        Map<Integer, String> monthValuesMap = DateUtils.getAllMonthsWithFullNames();
        Map<String, Long> receivedApplications = new LinkedHashMap<>();
        Map<String, Long> closedApplications = new LinkedHashMap<>();
        Map<String, Long> openApplications = new LinkedHashMap<>();
        Aggregations aggregation;

        if (StringUtils.isNotBlank(applicationIndexRequest.getFromDate())
                && StringUtils.isNotBlank(applicationIndexRequest.getToDate())) {
            fromDate = DateUtils.getDate(applicationIndexRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(DateUtils.getDate(applicationIndexRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        }

        aggregation = getMonthwiseApplications(applicationIndexRequest, fromDate,
                toDate, RECEIVED);
        Histogram dateaggs = aggregation.get(DATE_AGGR);
        prepareMonthwiseDetails(monthValuesMap, receivedApplications, dateaggs);
        aggregation = getMonthwiseApplications(applicationIndexRequest, fromDate,
                toDate, CLOSED);
        dateaggs = aggregation.get(DATE_AGGR);
        prepareMonthwiseDetails(monthValuesMap, closedApplications, dateaggs);

        aggregation = getMonthwiseApplications(applicationIndexRequest, fromDate,
                toDate, OPEN);
        dateaggs = aggregation.get(DATE_AGGR);
        prepareMonthwiseDetails(monthValuesMap, openApplications, dateaggs);
        Trend trend;

        if (StringUtils.isBlank(applicationIndexRequest.getFromDate())
                && StringUtils.isBlank(applicationIndexRequest.getToDate())) {
            for (Map.Entry<Integer, String> entry : DateUtils.getAllFinancialYearMonthsWithFullNames().entrySet()) {
                trend = new Trend();
                trend.setMonth(entry.getValue());
                trend.setTotalReceived(
                        receivedApplications.get(entry.getValue()) == null ? 0 : receivedApplications.get(entry.getValue()));
                trend.setTotalClosed(
                        closedApplications.get(entry.getValue()) == null ? 0 : closedApplications.get(entry.getValue()));
                trend.setTotalOpen(openApplications.get(entry.getValue()) == null ? 0 : openApplications.get(entry.getValue()));
                trendsList.add(trend);
            }
        } else {
            for (Map.Entry<String, Long> entry : receivedApplications.entrySet()) {
                trend = new Trend();
                trend.setMonth(entry.getKey());
                trend.setTotalReceived(
                        receivedApplications.get(entry.getKey()) == null ? 0 : receivedApplications.get(entry.getKey()));
                trend.setTotalClosed(closedApplications.get(entry.getKey()) == null ? 0 : closedApplications.get(entry.getKey()));
                trend.setTotalOpen(openApplications.get(entry.getKey()) == null ? 0 : openApplications.get(entry.getKey()));
                trendsList.add(trend);
            }
        }
        return trendsList;
    }

    private void prepareMonthwiseDetails(Map<Integer, String> monthValuesMap, Map<String, Long> applications,
            Histogram dateaggs) {
        String[] dateArr;
        Integer month,year;
        ValueCount countAggr;
        String monthName;
        Map <String,Long> map = new LinkedHashMap<>();
        for (Histogram.Bucket entry : dateaggs.getBuckets()) {
            dateArr = entry.getKeyAsString().split("T");
            year = Integer.valueOf(dateArr[0].split("-", 3)[0]);
            month = Integer.valueOf(dateArr[0].split("-", 3)[1]);
            monthName = monthValuesMap.get(month);
            countAggr = entry.getAggregations().get(TOTAL_COUNT);
            map.put(monthName + " " + year, countAggr.getValue());
        }
        applications.putAll(map);
    }
    private Aggregations getMonthwiseApplications(ApplicationIndexRequest applicationIndexRequest,
            Date fromDate, Date toDate, String status) {
        BoolQueryBuilder boolQuery = prepareWhereClause(applicationIndexRequest, fromDate, toDate);

        if (CLOSED.equalsIgnoreCase(status))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(IS_CLOSED, 1));
        else if (OPEN.equalsIgnoreCase(status))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(IS_CLOSED, 0));

        AggregationBuilder aggregationBuilder = AggregationBuilders.dateHistogram(DATE_AGGR).field(APPLICATION_DATE)
                .interval(DateHistogramInterval.MONTH)
                .subAggregation(AggregationBuilders.count(TOTAL_COUNT).field(APPLICATION_NUMBER));

        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(APPLICATIONS_INDEX)
                .withQuery(boolQuery)
                .addAggregation(aggregationBuilder).build();

        return elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());
    }

    @SuppressWarnings("rawtypes")
    private Aggregations getMonthwiseServiceGroupApplications(ApplicationIndexRequest applicationIndexRequest,
            Date fromDate, Date toDate, String status) {

        BoolQueryBuilder boolQuery = prepareWhereClause(applicationIndexRequest, fromDate, toDate);

        if (CLOSED.equalsIgnoreCase(status))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(IS_CLOSED, 1));
        else if (OPEN.equalsIgnoreCase(status))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(IS_CLOSED, 0));

        AggregationBuilder aggregationBuilder = AggregationBuilders.dateHistogram(DATE_AGGR).field(APPLICATION_DATE)
                .interval(DateHistogramInterval.MONTH);

        aggregationBuilder.subAggregation(AggregationBuilders.terms(MODULE_NAME).field(MODULE_NAME)
                .subAggregation(AggregationBuilders.count(TOTAL_COUNT).field(APPLICATION_NUMBER)));

        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(APPLICATIONS_INDEX)
                .withQuery(boolQuery)
                .addAggregation(aggregationBuilder).build();

        return elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());
    }

    private Aggregations getDocumentCounts(ApplicationIndexRequest applicationIndexRequest, Date fromDate,
            Date toDate, String aggregationName, String applicationStatus, String aggregationField, int size) {
        AggregationBuilder aggregation = null;
        SearchQuery searchQueryColl;
        ValueCountBuilder countBuilder = AggregationBuilders.count(TOTAL_COUNT).field(APPLICATION_NUMBER);
        BoolQueryBuilder boolQuery = prepareWhereClause(applicationIndexRequest, fromDate, toDate);

        if (StringUtils.isNotBlank(applicationStatus))
            boolQuery = prepareQueryForApplicationStatus(applicationStatus, boolQuery);

        if (StringUtils.isNotBlank(aggregationName))
            aggregation = AggregationBuilders.terms(aggregationName).field(aggregationField).size(size)
                    .subAggregation(countBuilder);

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withIndices(APPLICATIONS_INDEX)
                .withQuery(boolQuery).addAggregation(countBuilder);
        if (StringUtils.isNotBlank(aggregationName))
            searchQueryColl = queryBuilder.addAggregation(aggregation).build();
        else
            searchQueryColl = queryBuilder.build();

        return elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());
    }

    private BoolQueryBuilder prepareQueryForApplicationStatus(String applicationStatus, BoolQueryBuilder boolQuery) {
        BoolQueryBuilder appStatusQuery = boolQuery;
        if (CLOSED.equalsIgnoreCase(applicationStatus))
            appStatusQuery = appStatusQuery.filter(QueryBuilders.matchQuery(IS_CLOSED, 1));
        else if (OPEN.equalsIgnoreCase(applicationStatus))
            appStatusQuery = appStatusQuery.filter(QueryBuilders.matchQuery(IS_CLOSED, 0));
        else if (CLOSED_WITHIN_SLA.equalsIgnoreCase(applicationStatus))
            appStatusQuery = appStatusQuery.filter(QueryBuilders.matchQuery(IS_CLOSED, 1))
                    .must(QueryBuilders.rangeQuery(SLA_GAP).lte(0));
        else if (CLOSED_BEYOND_SLA.equalsIgnoreCase(applicationStatus))
            appStatusQuery = appStatusQuery.filter(QueryBuilders.matchQuery(IS_CLOSED, 1))
                    .must(QueryBuilders.rangeQuery(SLA_GAP).gt(0));
        else if (TOTAL_BEYOND_SLA.equalsIgnoreCase(applicationStatus))
            appStatusQuery = appStatusQuery.filter(QueryBuilders.rangeQuery(SLA_GAP).gt(0));
        else if (TOTAL_WITHIN_SLA.equalsIgnoreCase(applicationStatus))
            appStatusQuery = appStatusQuery.filter(QueryBuilders.rangeQuery(SLA_GAP).lte(0));
        else if (OPEN_WITHIN_SLA.equalsIgnoreCase(applicationStatus))
            appStatusQuery = appStatusQuery.filter(QueryBuilders.matchQuery(IS_CLOSED, 0))
                    .must(QueryBuilders.rangeQuery(SLA_GAP).lte(0));
        else if (OPEN_BEYOND_SLA.equalsIgnoreCase(applicationStatus))
            appStatusQuery = appStatusQuery.filter(QueryBuilders.matchQuery(IS_CLOSED, 0))
                    .must(QueryBuilders.rangeQuery(SLA_GAP).gt(0));
        else if (SLAB1BEYOND_SLA.equalsIgnoreCase(applicationStatus))
            appStatusQuery = appStatusQuery.filter(QueryBuilders.rangeQuery(SLA_GAP).gte(1).lt(8));
        else if (SLAB2BEYOND_SLA.equalsIgnoreCase(applicationStatus))
            appStatusQuery = appStatusQuery.filter(QueryBuilders.rangeQuery(SLA_GAP).gte(8).lt(31));
        else if (SLAB3BEYOND_SLA.equalsIgnoreCase(applicationStatus))
            appStatusQuery = appStatusQuery.filter(QueryBuilders.rangeQuery(SLA_GAP).gte(31).lt(91));
        else if (SLAB4BEYOND_SLA.equalsIgnoreCase(applicationStatus))
            appStatusQuery = appStatusQuery.filter(QueryBuilders.rangeQuery(SLA_GAP).gte(91));
        else if (CSC_TOTAL.equalsIgnoreCase(applicationStatus))
            appStatusQuery = appStatusQuery.filter(QueryBuilders.matchQuery(CHANNEL, SOURCE_CSC));
        else if (MEESEVA_TOTAL.equalsIgnoreCase(applicationStatus))
            appStatusQuery = appStatusQuery.filter(QueryBuilders.matchQuery(CHANNEL, SOURCE_MEESEVA));
        else if (ONLINE_TOTAL.equalsIgnoreCase(applicationStatus))
            appStatusQuery = appStatusQuery.filter(QueryBuilders.matchQuery(CHANNEL, SOURCE_ONLINE));
        else if (ULB_TOTAL.equalsIgnoreCase(applicationStatus))
            appStatusQuery = appStatusQuery.filter(QueryBuilders.matchQuery(CHANNEL, SOURCE_SYSTEM));
        else if (OTHERS_TOTAL.equalsIgnoreCase(applicationStatus))
            appStatusQuery = appStatusQuery.mustNot(
                    QueryBuilders.termsQuery(CHANNEL, Arrays.asList(SOURCE_CSC, SOURCE_MEESEVA, SOURCE_ONLINE, SOURCE_SYSTEM)));
        return appStatusQuery;
    }

    /**
     * Provides the details required for the aggregation Field
     * @param applicationIndexRequest
     * @param fromDate
     * @param toDate
     * @param value
     * @param aggregationField
     * @return map
     */
    private Map getDetailsForAggregationType(ApplicationIndexRequest applicationIndexRequest, Date fromDate, Date toDate,
            String value, String aggregationField) {
        String[] requiredFields = null;
        BoolQueryBuilder boolQuery = prepareWhereClause(applicationIndexRequest, fromDate, toDate);

        if (DISTRICT_NAME.equalsIgnoreCase(aggregationField)) {
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("districtName", value));
            requiredFields = new String[1];
            requiredFields[0] = REGION_NAME;
        } else if (CITY_NAME.equalsIgnoreCase(aggregationField)) {
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("cityName", value));
            requiredFields = new String[4];
            requiredFields[0] = REGION_NAME;
            requiredFields[1] = DISTRICT_NAME;
            requiredFields[2] = CITY_GRADE;
            requiredFields[3] = CITY_CODE;
        } else if (APPLICATION_TYPE.equalsIgnoreCase(aggregationField)) {
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(APPLICATION_TYPE, value));
            requiredFields = new String[2];
            requiredFields[0] = MODULE_NAME;
            requiredFields[1] = SLA;
        } else if (OWNER_NAME.equalsIgnoreCase(aggregationField)){
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(OWNER_NAME, value));
            requiredFields = new String[2];
            requiredFields[0] = CITY_NAME;
            requiredFields[1] = SLA;
        }

        Map applicationTypeDetails = new HashMap<>();
        SearchResponse response = elasticsearchTemplate.getClient()
                .prepareSearch(APPLICATIONS_INDEX)
                .setQuery(boolQuery).setSize(1)
                .setFetchSource(requiredFields, null)
                .execute().actionGet();

        for (SearchHit hit : response.getHits())
            applicationTypeDetails = hit.sourceAsMap();

        return applicationTypeDetails;
    }

    public ApplicationIndexResponse findServiceWiseDetails(final ApplicationIndexRequest applicationIndexRequest) {
        final ApplicationIndexResponse applicationIndexResponse = new ApplicationIndexResponse();
        Aggregations aggregation;
        ValueCount valueCount;
        Date fromDate = null;
        Date toDate = null;
        if (StringUtils.isNotBlank(applicationIndexRequest.getFromDate())
                && StringUtils.isNotBlank(applicationIndexRequest.getToDate())) {
            fromDate = DateUtils.getDate(applicationIndexRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = org.apache.commons.lang3.time.DateUtils.addDays(
                    DateUtils.getDate(applicationIndexRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        }
        aggregation = getDocumentCounts(applicationIndexRequest, fromDate, toDate, StringUtils.EMPTY, RECEIVED, StringUtils.EMPTY,
                0);
        if (aggregation != null) {
            valueCount = aggregation.get(TOTAL_COUNT);
            applicationIndexResponse.setTotalReceived(valueCount != null ? valueCount.getValue() : 0);
        }
        aggregation = getDocumentCounts(applicationIndexRequest, fromDate, toDate, StringUtils.EMPTY, CLOSED, StringUtils.EMPTY,
                0);
        if (aggregation != null) {
            valueCount = aggregation.get(TOTAL_COUNT);
            applicationIndexResponse.setTotalClosed(valueCount != null ? valueCount.getValue() : 0);
        }
        aggregation = getDocumentCounts(applicationIndexRequest, fromDate, toDate, StringUtils.EMPTY, OPEN, StringUtils.EMPTY, 0);
        if (aggregation != null) {
            valueCount = aggregation.get(TOTAL_COUNT);
            applicationIndexResponse.setTotalOpen(valueCount != null ? valueCount.getValue() : 0);
        }
        aggregation = getDocumentCounts(applicationIndexRequest, fromDate, toDate, StringUtils.EMPTY, TOTAL_BEYOND_SLA,
                StringUtils.EMPTY, 0);
        if (aggregation != null) {
            valueCount = aggregation.get(TOTAL_COUNT);
            applicationIndexResponse.setTotalBeyondSLA(valueCount != null ? valueCount.getValue() : 0);
        }
        aggregation = getDocumentCounts(applicationIndexRequest, fromDate, toDate, StringUtils.EMPTY, TOTAL_WITHIN_SLA,
                StringUtils.EMPTY, 0);
        if (aggregation != null) {
            valueCount = aggregation.get(TOTAL_COUNT);
            applicationIndexResponse.setTotalWithinSLA(valueCount != null ? valueCount.getValue() : 0);
        }
        aggregation = getDocumentCounts(applicationIndexRequest, fromDate, toDate, StringUtils.EMPTY, OPEN_BEYOND_SLA,
                StringUtils.EMPTY, 0);
        if (aggregation != null) {
            valueCount = aggregation.get(TOTAL_COUNT);
            applicationIndexResponse.setOpenBeyondSLA(valueCount != null ? valueCount.getValue() : 0);
        }
        aggregation = getDocumentCounts(applicationIndexRequest, fromDate, toDate, StringUtils.EMPTY, CLOSED_BEYOND_SLA,
                StringUtils.EMPTY, 0);
        if (aggregation != null) {
            valueCount = aggregation.get(TOTAL_COUNT);
            applicationIndexResponse.setClosedBeyondSLA(valueCount != null ? valueCount.getValue() : 0);
        }

        final List<ServiceDetails> serviceDetails = getServiceDetails(applicationIndexRequest, fromDate, toDate);
        applicationIndexResponse.setServiceDetails(serviceDetails);
        return applicationIndexResponse;
    }

    @SuppressWarnings("rawtypes")
    private List<ServiceDetails> getServiceDetails(final ApplicationIndexRequest applicationIndexRequest, final Date fromDate,
            final Date toDate) {
        final List<ServiceDetails> serviceDetailsList = new ArrayList<>();
        AggregationBuilder aggregation;
        SearchQuery searchQueryColl;
        final ValueCountBuilder countBuilder = AggregationBuilders.count(TOTAL_COUNT).field(APPLICATION_NUMBER);
        final BoolQueryBuilder boolQuery = prepareWhereClause(applicationIndexRequest, fromDate, toDate);

        aggregation = AggregationBuilders.terms("by_service1").field(APPLICATION_TYPE).size(5)
                .subAggregation(countBuilder)
                .order(Terms.Order.aggregation(TOTAL_COUNT, false));

        final NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withIndices(APPLICATIONS_INDEX)
                .withQuery(boolQuery).addAggregation(aggregation);
        searchQueryColl = queryBuilder.build();

        final Aggregations aggr = elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());
        final Map<String, Long> aggregationResults = new LinkedHashMap<>();
        final StringTerms serviceAggr = aggr.get("by_service1");
        ValueCount valueCount;
        for (final Terms.Bucket entry : serviceAggr.getBuckets()) {
            valueCount = entry.getAggregations().get(TOTAL_COUNT);
            aggregationResults.put(String.valueOf(entry.getKey()), valueCount.getValue());
        }
        ServiceDetails serviceDetails;
        for (final Map.Entry<String, Long> entry : aggregationResults.entrySet()) {
            serviceDetails = new ServiceDetails();
            serviceDetails.setServiceName(entry.getKey());
            serviceDetails
                    .setBeyondSLA(aggregationResults.get(entry.getKey()) == null ? 0 : aggregationResults.get(entry.getKey()));
            serviceDetailsList.add(serviceDetails);
        }

        return serviceDetailsList;
    }

    /**
     * Fetch delayed days for aggregation based on service type
     * @param applicationIndexRequest
     * @param fromDate
     * @param toDate
     * @param aggregationField
     * @param size
     * @return map
     */
    private Map<String, Long> getDelayedDaysAggregate(ApplicationIndexRequest applicationIndexRequest, Date fromDate, Date toDate,
            String aggregationField, int size) {
        Sum sumAggr;
        Map<String, Long> delayMap = new HashMap<>();
        BoolQueryBuilder boolQuery = prepareWhereClause(applicationIndexRequest, fromDate, toDate);
        boolQuery = boolQuery.must(QueryBuilders.rangeQuery(SLA_GAP).gt(0));

        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("by_service").field(aggregationField).size(size)
                .subAggregation(AggregationBuilders.sum(TOTAL_COUNT).field(SLA_GAP));

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withIndices(APPLICATIONS_INDEX)
                .withQuery(boolQuery);
        SearchQuery searchQueryColl = queryBuilder.addAggregation(aggregationBuilder).build();

        Aggregations aggregation = elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());

        StringTerms stringTerms = aggregation.get("by_service");
        for (Terms.Bucket entry : stringTerms.getBuckets()) {
            sumAggr = entry.getAggregations().get(TOTAL_COUNT);
            delayMap.put(String.valueOf(entry.getKey()), (long) sumAggr.getValue());
        }
        return delayMap;
    }

    /**
     * Provides the details of the applications
     * @param applicationIndexRequest
     * @return list
     */
    public List<ApplicationInfo> getApplicationInfo(ApplicationIndexRequest applicationIndexRequest) {
        List<ApplicationInfo> applications = new ArrayList<>();
        List<Map> appDetails = new ArrayList<>();
        ApplicationInfo appInfo;
        Date fromDate = null;
        Date toDate = null;
        if (StringUtils.isNotBlank(applicationIndexRequest.getFromDate())
                && StringUtils.isNotBlank(applicationIndexRequest.getToDate())) {
            fromDate = DateUtils.getDate(applicationIndexRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(DateUtils.getDate(applicationIndexRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        }
        BoolQueryBuilder boolQuery = prepareWhereClause(applicationIndexRequest, fromDate, toDate);

        SearchResponse response = elasticsearchTemplate.getClient()
                .prepareSearch(APPLICATIONS_INDEX)
                .setQuery(boolQuery)
                .execute().actionGet();

        int size = (int) response.getHits().totalHits();
        response = elasticsearchTemplate.getClient()
                .prepareSearch(APPLICATIONS_INDEX)
                .setQuery(boolQuery).setSize(size)
                .addSort(APPLICATION_DATE, SortOrder.DESC)
                .setFetchSource(new String[] { APPLICATION_DATE, APPLICATION_NUMBER, APPLICATION_TYPE, "applicantName",
                        "applicantAddress", "status", CHANNEL, SLA, MODULE_NAME, SLA_GAP, CITY_NAME, OWNER_NAME,"url",CITY_CODE }, null)
                .execute().actionGet();

        for (SearchHit hit : response.getHits())
            appDetails.add(hit.sourceAsMap());

        if (!appDetails.isEmpty()) {
            for (Map details : appDetails) {
                appInfo = new ApplicationInfo();
                appInfo.setAppDate(details.get(APPLICATION_DATE).toString().split("T")[0]);
                appInfo.setAppNo(details.get(APPLICATION_NUMBER).toString());
                appInfo.setService(details.get(APPLICATION_TYPE).toString());
                appInfo.setApplicantName(details.get("applicantName").toString());
                appInfo.setApplicantAddress(details.get("applicantAddress").toString());
                appInfo.setAppStatus(details.get("status").toString());
                appInfo.setSource(details.get(CHANNEL).toString());
                appInfo.setSla(details.get(SLA) == null ? 0 : (int) details.get(SLA));
                appInfo.setServiceGroup(details.get(MODULE_NAME).toString());
                appInfo.setAge(details.get(SLA_GAP) == null ? 0 : (int) details.get(SLA_GAP));
                appInfo.setPendingWith(details.get(OWNER_NAME).toString());
                appInfo.setUlbName(details.get(CITY_NAME).toString());
                appInfo.setUrl(details.get("url").toString());
                appInfo.setCityCode(details.get(CITY_CODE).toString());
                applications.add(appInfo);
            }
        }
        return applications;
    }

}
