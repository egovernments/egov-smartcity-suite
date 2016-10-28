/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2016  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *          1) All versions of this program, verbatim or modified must carry this
 *             Legal Notice.
 *
 *          2) Any misrepresentation of the origin of the material is prohibited. It
 *             is required that all modified versions of this material be marked in
 *             reasonable ways as different from the original version.
 *
 *          3) This license does not grant any rights to any user of the program
 *             with regards to rights under trademark law for use of the trade names
 *             or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.stms.service.es;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.stms.elasticSearch.entity.SewerageCollectFeeSearchRequest;
import org.egov.stms.elasticSearch.entity.SewerageConnSearchRequest;
import org.egov.stms.entity.es.SewerageIndex;
import org.egov.stms.repository.es.SewerageIndexRepository;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SewerageIndexService {

    @Autowired
    private CityService cityService;

    @Autowired
    private SewerageIndexRepository sewerageIndexRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public SewerageIndex createSewarageIndex(final SewerageApplicationDetails sewerageApplicationDetails,
            final AssessmentDetails assessmentDetails) {

        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());

        final SewerageIndex sewarageIndex = new SewerageIndex();
        sewarageIndex.setUlbName(cityWebsite.getName());
        sewarageIndex.setApplicationCreatedBy(sewerageApplicationDetails.getCreatedBy().getName());
        sewarageIndex.setId(cityWebsite.getCode().concat("-").concat(sewerageApplicationDetails.getApplicationNumber()));
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        sewarageIndex.setApplicationDate(formatDate(formatter.format(sewerageApplicationDetails.getApplicationDate())));
        sewarageIndex.setApplicationNumber(sewerageApplicationDetails.getApplicationNumber());
        sewarageIndex.setApplicationStatus(sewerageApplicationDetails.getStatus() != null
                ? sewerageApplicationDetails.getStatus().getDescription() : "");
        sewarageIndex.setConsumerNumber(sewerageApplicationDetails.getApplicationNumber());
        sewarageIndex.setApplicationType(sewerageApplicationDetails.getApplicationType() != null
                ? sewerageApplicationDetails.getApplicationType().getName() : "");
        sewarageIndex.setConnectionStatus(sewerageApplicationDetails.getConnection().getStatus() != null
                ? sewerageApplicationDetails.getConnection().getStatus().name() : "");
        sewarageIndex.setCreatedDate(formatDate(formatter.format(sewerageApplicationDetails.getCreatedDate())));
        sewarageIndex.setShscNumber(sewerageApplicationDetails.getConnection().getShscNumber() != null
                ? sewerageApplicationDetails.getConnection().getShscNumber() : "");
        sewarageIndex.setDisposalDate(formatDate(formatter.format(sewerageApplicationDetails.getDisposalDate())));
        sewarageIndex
                .setExecutionDate(formatDate(formatter.format(sewerageApplicationDetails.getConnection().getExecutionDate())));
        sewarageIndex.setIslegacy(sewerageApplicationDetails.getConnection().getLegacy());
        sewarageIndex.setNoOfClosets_nonResidential(
                sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsNonResidential());
        sewarageIndex
                .setNoOfClosets_residential(sewerageApplicationDetails.getConnectionDetail().getNoOfClosetsResidential());
        sewarageIndex.setPropertyIdentifier(sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier() != null
                ? sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier() : "");
        sewarageIndex.setPropertyType(sewerageApplicationDetails.getConnectionDetail().getPropertyType() != null
                ? sewerageApplicationDetails.getConnectionDetail().getPropertyType().name() : "");
        if (sewerageApplicationDetails.getEstimationDate() != null)
            sewarageIndex.setEstimationDate(formatDate(formatter.format(sewerageApplicationDetails.getEstimationDate())));
        sewarageIndex
                .setEstimationNumber(sewerageApplicationDetails.getEstimationNumber() != null ? sewerageApplicationDetails
                        .getEstimationNumber() : "");
        if (sewerageApplicationDetails.getWorkOrderDate() != null)
            sewarageIndex.setWorkOrderDate(formatDate(formatter.format(sewerageApplicationDetails.getWorkOrderDate())));
        sewarageIndex
                .setWorkOrderNumber(sewerageApplicationDetails.getWorkOrderNumber() != null ? sewerageApplicationDetails
                        .getWorkOrderNumber() : "");
        if (sewerageApplicationDetails.getClosureNoticeDate() != null)
            sewarageIndex
                    .setClosureNoticeDate(formatDate(formatter.format(sewerageApplicationDetails.getClosureNoticeDate())));
        sewarageIndex
                .setClosureNoticeNumber(
                        sewerageApplicationDetails.getClosureNoticeNumber() != null ? sewerageApplicationDetails
                                .getClosureNoticeNumber() : "");
        Iterator<OwnerName> ownerNameItr = null;
        if (null != assessmentDetails.getOwnerNames())
            ownerNameItr = assessmentDetails.getOwnerNames().iterator();
        final StringBuilder consumerName = new StringBuilder();
        final StringBuilder mobileNumber = new StringBuilder();
        if (null != ownerNameItr && ownerNameItr.hasNext()) {
            final OwnerName primaryOwner = ownerNameItr.next();
            consumerName.append(primaryOwner.getOwnerName() != null ? primaryOwner.getOwnerName() : "");
            mobileNumber.append(primaryOwner.getMobileNumber() != null ? primaryOwner.getMobileNumber() : "");
            while (ownerNameItr.hasNext()) {
                final OwnerName secondaryOwner = ownerNameItr.next();
                consumerName.append(",").append(secondaryOwner.getOwnerName() != null ? secondaryOwner.getOwnerName() : "");
                mobileNumber.append(",")
                        .append(secondaryOwner.getMobileNumber() != null ? secondaryOwner.getMobileNumber() : "");
            }
        }
        sewarageIndex.setMobileNumber(mobileNumber.toString());
        sewarageIndex.setConsumerName(consumerName.toString());
        sewarageIndex.setDoorNo(assessmentDetails.getHouseNo() != null ? assessmentDetails.getHouseNo() : "");
        sewarageIndex.setWard(
                assessmentDetails.getBoundaryDetails() != null ? assessmentDetails.getBoundaryDetails().getWardName() : "");
        sewarageIndex
                .setAddress(assessmentDetails.getPropertyAddress() != null ? assessmentDetails.getPropertyAddress() : "");
        // Setting application status is active or in-active
        sewarageIndex.setActive(sewerageApplicationDetails.isActive());
        sewerageIndexRepository.save(sewarageIndex);
        return sewarageIndex;
    }
// CHECK ANY EGI API PRESENT TO CONVERT THIS DATE FORMAT.
    public Date formatDate(final String Date) {
        final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        try {
            return formatter.parse(Date);
        } catch (final ParseException e) {

        }
        return null;
    }
//TODO: CHECK LIKE CASES WORKING OR NOT IN CASE OF SEARCH BY CONSUMER NAME
    public BoolQueryBuilder getQueryFilter(final SewerageConnSearchRequest searchRequest) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.matchQuery("active", true));
        if (org.apache.commons.lang.StringUtils.isNotBlank(searchRequest.getConsumerNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("consumerNumber", searchRequest.getConsumerNumber()));
        if (org.apache.commons.lang.StringUtils.isNotBlank(searchRequest.getShscNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("shscNumber", searchRequest.getShscNumber()));
        if (org.apache.commons.lang.StringUtils.isNotBlank(searchRequest.getApplicantName()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("consumerName", searchRequest.getApplicantName()));
        if (org.apache.commons.lang.StringUtils.isNotBlank(searchRequest.getMobileNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("mobileNumber", searchRequest.getMobileNumber()));
        if (org.apache.commons.lang.StringUtils.isNotBlank(searchRequest.getRevenueWard()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("revenueWard", searchRequest.getRevenueWard()));
        if (org.apache.commons.lang.StringUtils.isNotBlank(searchRequest.getDoorNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("doorNumber", searchRequest.getDoorNumber()));
        return boolQuery;
    }
  //TODO: GET SORTING ORDER FIELD NAME AND ORDER TYPE AS NEW PARAMETER. PAGINATION REQUIRED ? Use query for list. No need to iterate again.

    public List<SewerageIndex> getSearchResultByBoolQuery(final BoolQueryBuilder boolQuery) {
        final List<SewerageIndex> resultList = new ArrayList<>();
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices("sewerage").withQuery(boolQuery)
                .withSort(new FieldSortBuilder("shscNumber").order(SortOrder.DESC))/*.withPageable(new PageRequest(0, 100))*/.build();
        final Page<SewerageIndex> sewerageIndexRecords = elasticsearchTemplate.queryForPage(searchQuery, SewerageIndex.class);

        for (final SewerageIndex indexRecord : sewerageIndexRecords)
            resultList.add(indexRecord);
        return resultList;
    }

    public BoolQueryBuilder getSearchQueryFilter(final SewerageCollectFeeSearchRequest searchRequest) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.matchQuery("ulbName", searchRequest.getUlbName()));
        if (org.apache.commons.lang.StringUtils.isNotBlank(searchRequest.getConsumerNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("consumerNumber", searchRequest.getConsumerNumber()));
        if (org.apache.commons.lang.StringUtils.isNotBlank(searchRequest.getShscNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("shscNumber", searchRequest.getShscNumber()));
        if (org.apache.commons.lang.StringUtils.isNotBlank(searchRequest.getApplicantName()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("consumerName", searchRequest.getApplicantName()));
        if (org.apache.commons.lang.StringUtils.isNotBlank(searchRequest.getMobileNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("mobileNumber", searchRequest.getMobileNumber()));
        if (org.apache.commons.lang.StringUtils.isNotBlank(searchRequest.getRevenueWard()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("ward", searchRequest.getRevenueWard()));
        if (org.apache.commons.lang.StringUtils.isNotBlank(searchRequest.getDoorNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("doorNo", searchRequest.getDoorNumber()));
        return boolQuery;
    }
//TODO: GET SORTING ORDER FIELD NAME AND ORDER TYPE AS NEW PARAMETER. PAGINATION REQUIRED ? Use query for list. No need to iterate again.
    public List<SewerageIndex> getCollectSearchResult(final BoolQueryBuilder boolQuery) {
        final List<SewerageIndex> resultList = new ArrayList<>();
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices("sewerage").withQuery(boolQuery)
                .withSort(new FieldSortBuilder("shscNumber").order(SortOrder.DESC)).build();
        final Page<SewerageIndex> seweragePage = elasticsearchTemplate.queryForPage(searchQuery, SewerageIndex.class);
        for (final SewerageIndex index : seweragePage)
            resultList.add(index);
        return resultList;
    }

}