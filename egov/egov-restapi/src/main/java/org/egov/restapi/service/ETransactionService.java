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

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.restapi.constants.RestApiConstants;
import org.egov.restapi.model.ETransactionResponse;
import org.egov.restapi.util.ValidationUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filters.Filters;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
public class ETransactionService {

    private static final String INDEX_APPLICATIONS = "applications";
    private static final String INDEX_RECEIPTS = "receipts";
    private static final String RANGE_APPLICATION = "applications";
    private static final String AGG_MODULE_NAME = "by_moduleName";
    private static final String AGG_APPLICATION_TYPE = "by_applicationType";
    private static final String AGG_APPLICATION_COUNT = "noOfApplication";
    private static final String FILTER_ONLINE_RECEIPTS = "onlineReceipts";
    private static final String RANGE_DATE = "dateRange";
    private static final String AGG_RECEIPT_COUNT = "noOfReceipts";
    private static final String INDEX_COMPLAINT = "complaint";
    private static final String RANGE_COMPLAINTS = "complaintsWithin";
    private static final String AGG_COMPLAINT_COUNT = "complaintCount";
    /**
     * Map of elasticsearch result to Service ID & Service Description
     */
    private static final Map<String, Pair<Long, String>> FIELD_NAME_TO_SID_DESCRIPTION_MAP = new HashMap<String, Pair<Long, String>>(
            35) {

        private static final long serialVersionUID = 8214999325840656456L;

        {
            put("Complaint", Pair.of(1L, "Number of complaint registered"));
            put("Collection", Pair.of(2L, "Number of online payment collected"));
            put("Marriage Registration - CERTIFICATEISSUE", Pair.of(3L, "Marriage Registration - Certificate Issue"));
            put("Marriage Registration - REGISTRATION", Pair.of(4L, "Marriage Registration Fee Collection"));
            put("PT Mutation Fee", Pair.of(5L, "PT Mutation Fee Collection"));
            put("Property Tax (On Land)", Pair.of(6L, "Property Tax (On Land) Collection"));
            put("Property Tax - Alter_Assessment", Pair.of(7L, "Property Tax - Alter Assessment Application"));
            put("Property Tax - Amalgamation", Pair.of(8L, "Property Tax - Amalgamation Application"));
            put("Property Tax - Bifuracate_Assessment", Pair.of(9L, "Property Tax - Bifuracate Assessment Application"));
            put("Property Tax - Demolition", Pair.of(10L, "Property Tax - Demolition Application"));
            put("Property Tax - General_Revision_Petition", Pair.of(11L, "Property Tax - General Revision Petition Application"));
            put("Property Tax - New_Assessment", Pair.of(12L, "New Assessment of Property Tax Application"));
            put("Property Tax - Revision_Petition", Pair.of(13L, "Property Tax - Revision Petition Application"));
            put("Property Tax - Tax_Exemption", Pair.of(14L, "Property Tax - Tax Exemption Application"));
            put("Property Tax - Transfer_of_Ownership", Pair.of(15L, "Property Tax - Transfer of Ownership Application"));
            put("Property Tax", Pair.of(16L, "Property Tax Collection"));
            put("Property Tax - Vacancy_Remission", Pair.of(17L, "Property Tax - Vacancy Remission Fee Collection"));
            put("Sewerage Tax - New Sewerage Connection", Pair.of(18L, "Sewerage Tax - New Sewerage Connection Application"));
            put("Sewerage Tax", Pair.of(19L, "Sewerage Tax Collection"));
            put("Trade License - License Closure", Pair.of(20L, "Trade License - License Closure Application"));
            put("Trade License - License Renewal", Pair.of(21L, "Trade License - License Renewal Application"));
            put("Trade License - New License", Pair.of(22L, "Trade License - New License Application"));
            put("Trade License", Pair.of(23L, "Trade License Fee Collection"));
            put("Water Charges - Additional connection", Pair.of(24L, "Water Charges - Additional connection Application"));
            put("Water Charges - Change of use", Pair.of(25L, "Water Charges - Change of use Application"));
            put("Water Charges - Closing connection", Pair.of(26L, "Water Charges - Closing connection Application"));
            put("Water Charges - New connection", Pair.of(27L, "Water Charges - New connection Application"));
            put("Water Charges - Regularization connection",
                    Pair.of(28L, "Water Charges - Regularization connection Application"));
            put("Water Charges", Pair.of(29L, "Water Charges Collection  Application"));
            put("Water Estimation Charges", Pair.of(30L, "Water Estimation Charges Collection Application"));
            put("Advertisement Tax - Advertisement", Pair.of(31L, "Advertisement Tax Application"));
        }
    };

    private static final Logger LOGGER = Logger.getLogger(ETransactionService.class);

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    ValidationUtil validationUtil;

    private AggregationBuilder<?> getApplicationsAggregation(Date fromDate, Date toDate) {
        return AggregationBuilders
                .dateRange(RANGE_APPLICATION)
                .field("applicationDate")
                .addRange(fromDate, toDate)
                .subAggregation(
                        AggregationBuilders.terms(AGG_MODULE_NAME).field("moduleName")
                                .subAggregation(
                                        AggregationBuilders.terms(AGG_APPLICATION_TYPE).field("applicationType")
                                                .subAggregation(AggregationBuilders.count(AGG_APPLICATION_COUNT))));
    }

    private AggregationBuilder<?> getReceiptsAggregation(Date fromDate, Date toDate) {
        BoolQueryBuilder boolqueryBuilder = QueryBuilders.boolQuery();
        boolqueryBuilder.filter(QueryBuilders.termQuery("paymentMode", "online"));
        boolqueryBuilder.filter(QueryBuilders.termQuery("status", "Approved"));
        return AggregationBuilders.filters(FILTER_ONLINE_RECEIPTS)
                .filter(boolqueryBuilder)
                .subAggregation(
                        AggregationBuilders.dateRange(RANGE_DATE)
                                .field("createdDate")
                                .addRange(fromDate, toDate)
                                .subAggregation(
                                        AggregationBuilders.count(AGG_RECEIPT_COUNT)));
    }

    private AggregationBuilder<?> getComplaintsAggregation(Date fromDate, Date toDate) {
        return AggregationBuilders.dateRange(RANGE_COMPLAINTS).field("createdDate")
                .addRange(fromDate, toDate)
                .subAggregation(AggregationBuilders.count(AGG_COMPLAINT_COUNT));
    }

    private Map<String, Long> getApplication(Aggregations applicationRangeAggs) {
        Map<String, Long> applicationsCountMap = new HashMap<>();
        Range applicationDateRange = applicationRangeAggs.get(RANGE_APPLICATION);
        if (!applicationDateRange.getBuckets().isEmpty()) {
            StringTerms moduleTerms = applicationDateRange.getBuckets().get(0).getAggregations().get(AGG_MODULE_NAME);
            for (Terms.Bucket bucket : moduleTerms.getBuckets()) {
                String moduleName = bucket.getKeyAsString();
                for (Aggregation agg : bucket.getAggregations()) {
                    StringTerms applicationTerms = (StringTerms) agg;
                    for (Terms.Bucket applicationBucket : applicationTerms.getBuckets()) {
                        String applicationType = applicationBucket.getKeyAsString();
                        ValueCount countAgg = applicationBucket.getAggregations().get(AGG_APPLICATION_COUNT);
                        String descriptionKey = moduleName + " - " + applicationType;
                        applicationsCountMap.put(descriptionKey, countAgg.getValue());
                    }
                }
            }
        }
        return applicationsCountMap;
    }

    private Map<String, Long> getOnlineTransaction(Aggregations onlineReceiptFilterAggs) {
        Map<String, Long> receiptsCountMap = new HashMap<>();
        Filters onlineReceiptFilter = onlineReceiptFilterAggs.get(FILTER_ONLINE_RECEIPTS);
        if (!onlineReceiptFilter.getBuckets().isEmpty()) {
            Range dateRange = onlineReceiptFilter.getBuckets().get(0).getAggregations().get(RANGE_DATE);
            ValueCount valueCount = dateRange.getBuckets().get(0).getAggregations().get(AGG_RECEIPT_COUNT);
            receiptsCountMap.put("Collection", valueCount.getValue());
        }
        return receiptsCountMap;
    }

    private Map<String, Long> getComplaintTransaction(Aggregations srComplaintsAggs) {
        Map<String, Long> complaintMap = new HashMap<>();
        Range complaintRange = srComplaintsAggs.get(RANGE_COMPLAINTS);
        if (!complaintRange.getBuckets().isEmpty()) {
            ValueCount valueCount = complaintRange.getBuckets().get(0).getAggregations().get(AGG_COMPLAINT_COUNT);
            complaintMap.put("Complaint", valueCount.getValue());
        }
        return complaintMap;
    }

    private void extractIntoList(List<ETransactionResponse> resultList, Map<String, Long> typeCount) {
        for (String key : typeCount.keySet()) {
            Pair<Long, String> sidDescriptionPair = FIELD_NAME_TO_SID_DESCRIPTION_MAP.get(key);
            if (sidDescriptionPair == null) {
                LOGGER.error(format("UNKNOWN Transaction Type found. Put a SID & description for \"%s\" in this class", key));
                continue;
            }

            resultList.add(new ETransactionResponse(sidDescriptionPair.getFirst(), sidDescriptionPair.getSecond(),
                    typeCount.get(key).longValue()));
        }
    }

    public List<ETransactionResponse> getETransactionCount(Date fromDate, Date toDate) {
        List<ETransactionResponse> tnxInfoList;
        Client esClient = elasticsearchTemplate.getClient();

        SearchResponse srApps = esClient.prepareSearch()
                .addAggregation(getApplicationsAggregation(fromDate, toDate))
                .setIndices(INDEX_APPLICATIONS)
                .execute().actionGet();

        SearchResponse srReceipts = esClient.prepareSearch()
                .addAggregation(getReceiptsAggregation(fromDate, toDate))
                .setIndices(INDEX_RECEIPTS)
                .execute().actionGet();

        SearchResponse srComplaints = esClient.prepareSearch()
                .addAggregation(getComplaintsAggregation(fromDate, toDate))
                .setIndices(INDEX_COMPLAINT)
                .execute().actionGet();

        Map<String, Long> applications = getApplication(srApps.getAggregations());
        Map<String, Long> receipts = getOnlineTransaction(srReceipts.getAggregations());
        Map<String, Long> complaints = getComplaintTransaction(srComplaints.getAggregations());

        tnxInfoList = new ArrayList<>(applications.size() + receipts.size() + complaints.size());
        extractIntoList(tnxInfoList, complaints);
        extractIntoList(tnxInfoList, applications);
        extractIntoList(tnxInfoList, receipts);
        Collections.sort(tnxInfoList);
        return tnxInfoList;
    }

    /*
     * @param errorList Appends ValidationError to errorList
     */
    public void validateETransactionRequest(final List<ValidationError> errorList, String ulbCode, Date fromDate, Date toDate) {

        if (DateUtils.compareDates(fromDate, toDate))
            errorList.add(new ValidationError("INVALID_DATE_RANGE", "toDate must be greater or equal to fromDate"));
        Date endOfToday = DateUtils.endOfToday().toDate();

        if (toDate.after(endOfToday))
            errorList.add(new ValidationError("NO_FUTURE_DATE",
                    String.format("%s(%s) must be less or equal to today (%s)",
                            "toDate", validationUtil.convertDateToString(toDate),
                            validationUtil.convertDateToString(endOfToday))));
        if (fromDate.after(endOfToday))
            errorList.add(new ValidationError("NO_FUTURE_DATE",
                    String.format("%s(%s) must be less or equal to today (%s)",
                            "fromDate", validationUtil.convertDateToString(fromDate),
                            validationUtil.convertDateToString(endOfToday))));
        if (!ApplicationThreadLocals.getCityCode().equals(ulbCode))
            errorList.add(
                    new ValidationError(RestApiConstants.THIRD_PARTY_ERR_CODE_ULBCODE_NO_REQUIRED, "Invalid ULB Code"));
    }

}