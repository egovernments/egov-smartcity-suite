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

package org.egov.council.service.es;

import org.apache.commons.lang.StringUtils;
import org.egov.council.entity.CouncilMeeting;
import org.egov.council.entity.MeetingAttendence;
import org.egov.council.entity.MeetingMOM;
import org.egov.council.entity.es.CouncilMeetingDetailsSearchRequest;
import org.egov.council.entity.es.CouncilMeetingIndex;
import org.egov.council.repository.es.CouncilMeetingIndexRepository;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static java.lang.Math.toIntExact;
import static org.egov.council.utils.constants.CouncilConstants.PREAMBLE_STATUS_ADJOURNED;
import static org.egov.council.utils.constants.CouncilConstants.PREAMBLE_STATUS_APPROVED;
import static org.egov.council.utils.constants.CouncilConstants.REJECTED;

@Service
@Transactional(readOnly = true)
public class CouncilMeetingIndexService {

    private static final String COUNCILMEETING = "councilmeeting";

    private static final String MEETING_NUMBER = "meetingNumber";

    private static final String COMMITTEE_TYPE = "committeeType";

    private static final String APPLICATION_COUNT = "application_count";

    @Autowired
    private CityService cityService;
   
    @Autowired
    private CouncilMeetingIndexRepository councilMeetingIndexRepository;
    
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    
    
    public CouncilMeetingIndex createCouncilMeetingIndex(final CouncilMeeting councilMeeting) throws ParseException{
        int noOfMembersPresent = 0;
        int noOfMembersAbsent = 0;
        int noOfPreamblesApproved= 0;
        int noOfPreamblesPostponed =0;
        int noOfPreamblesRejected =0;
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        CouncilMeetingIndex meetingIndex = new CouncilMeetingIndex();
        meetingIndex.setDistrictName(cityWebsite.getDistrictName());
        meetingIndex.setUlbGrade(cityWebsite.getGrade());
        meetingIndex.setUlbCode(cityWebsite.getCode());
        meetingIndex.setRegionName(cityWebsite.getRegionName());
        meetingIndex.setUlbName(cityWebsite.getName());
        final SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        if(councilMeeting != null){
            meetingIndex.setId(cityWebsite.getCode().concat("-").concat(councilMeeting.getMeetingNumber()));
            meetingIndex.setCommitteeType(councilMeeting.getCommitteeType().getName()!= null ?councilMeeting.getCommitteeType().getName():"");
           String meetingDate = dateFormat.format(councilMeeting.getMeetingDate());
            meetingIndex.setMeetingDate(dateFormat.parse(meetingDate));
            meetingIndex.setCreatedDate(councilMeeting.getCreatedDate());
            meetingIndex.setMeetingLocation(councilMeeting.getMeetingLocation()!= null ?councilMeeting.getMeetingLocation():"");
            meetingIndex.setMeetingNumber(councilMeeting.getMeetingNumber() != null ?councilMeeting.getMeetingNumber():"");
            meetingIndex.setMeetingTime(councilMeeting.getMeetingTime() != null ?councilMeeting.getMeetingTime():"");
            meetingIndex.setStatus(councilMeeting.getStatus() != null ?councilMeeting.getStatus().getCode():"");
            meetingIndex.setTotalNoOfCommitteMembers(councilMeeting != null && councilMeeting.getMeetingAttendence()!= null ?councilMeeting.getMeetingAttendence().size():0);
            if(councilMeeting.getMeetingAttendence() != null){
                for (MeetingAttendence attendence : councilMeeting.getMeetingAttendence()) {
                    if(attendence.getAttendedMeeting()) {
                        noOfMembersPresent++;
                    } else {
                        noOfMembersAbsent++;
                    }
                }
            }
            meetingIndex.setNoOfCommitteMembersPresent(noOfMembersPresent);
            meetingIndex.setNoOfCommitteMembersAbsent(noOfMembersAbsent);
            
            if(!councilMeeting.getMeetingMOMs().isEmpty()){
                for(MeetingMOM mom : councilMeeting.getMeetingMOMs()){
                   if(PREAMBLE_STATUS_APPROVED.equals(mom.getResolutionStatus().getCode())){
                       noOfPreamblesApproved++;
                   } else if(PREAMBLE_STATUS_ADJOURNED.equals(mom.getResolutionStatus().getCode())){
                       noOfPreamblesPostponed++;
                   }else if(REJECTED.equals(mom.getResolutionStatus().getCode())){
                       noOfPreamblesRejected++;
                    }
                }
            }
           meetingIndex.setTotalNoOfPreamblesUsed(councilMeeting.getMeetingMOMs().size());
           meetingIndex.setNoOfPreamblesApproved(noOfPreamblesApproved);
           meetingIndex.setNoOfPreamblesPostponed(noOfPreamblesPostponed);
           meetingIndex.setNoOfPreamblesRejected(noOfPreamblesRejected);
        }
        councilMeetingIndexRepository.save(meetingIndex);
        return meetingIndex;
    }
    
    public List<CouncilMeetingIndex> getQueryFilterForMeetingDetails(final CouncilMeetingDetailsSearchRequest searchRequest){
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (searchRequest.getFrom() != null && searchRequest.getTo() != null){
        boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery("meetingDate")
                .from(searchRequest.getFrom())
                .to(searchRequest.getTo()));
        }
        if (StringUtils.isNotBlank(searchRequest.getCommitteeType()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(COMMITTEE_TYPE, searchRequest.getCommitteeType()));
        if (StringUtils.isNotBlank(searchRequest.getMeetingNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(MEETING_NUMBER, searchRequest.getMeetingNumber()));
        final FieldSortBuilder sort = new FieldSortBuilder(COMMITTEE_TYPE).order(SortOrder.DESC);
        
        return getSearchResultByBoolQuery(boolQuery,sort);
    }

    public List<CouncilMeetingIndex> getSearchResultByBoolQuery(final BoolQueryBuilder boolQuery, final FieldSortBuilder sort) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .addAggregation(AggregationBuilders.count(APPLICATION_COUNT).field(MEETING_NUMBER))
                .withIndices(COUNCILMEETING).withQuery(boolQuery).build();
        final Aggregations applicationCountAggr = elasticsearchTemplate.query(searchQuery, SearchResponse::getAggregations);
        final ValueCount aggr = applicationCountAggr.get(APPLICATION_COUNT);
        searchQuery = new NativeSearchQueryBuilder().withIndices(COUNCILMEETING)
                .withQuery(boolQuery)
                .addAggregation(AggregationBuilders.count(APPLICATION_COUNT).field(MEETING_NUMBER)).withSort(sort)
                .withPageable(new PageRequest(0, toIntExact(aggr.getValue() == 0 ? 1 : aggr.getValue())))
                .build();
        return elasticsearchTemplate.queryForList(searchQuery, CouncilMeetingIndex.class);
    }
    
    public BoolQueryBuilder prepareWhereClause(final CouncilMeetingDetailsSearchRequest searchRequest){
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.matchQuery("ulbName", searchRequest.getUlbName()));
        if (searchRequest.getFrom() != null && searchRequest.getTo() != null){
            boolQuery = boolQuery.filter(QueryBuilders.rangeQuery("meetingDate")
                    .from(searchRequest.getFrom())
                    .to(searchRequest.getTo()));
        }
        if (StringUtils.isNotBlank(searchRequest.getCommitteeType()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(COMMITTEE_TYPE, searchRequest.getCommitteeType()));
        if (StringUtils.isNotBlank(searchRequest.getMeetingNumber()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(MEETING_NUMBER, searchRequest.getMeetingNumber()));
        
        return boolQuery;
    }
}