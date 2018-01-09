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

package org.egov.stms.service.es;

import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_COLLECTINSPECTIONFEE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_ESTIMATENOTICEGEN;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_SANCTIONED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.COLLECTDONATIONCHARHGES;
import static org.egov.stms.utils.constants.SewerageTaxConstants.MODIFYLEGACYCONNECTION;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.utils.DateUtils;
import org.egov.stms.elasticsearch.entity.SewerageConnSearchRequest;
import org.egov.stms.elasticsearch.entity.SewerageNoticeSearchRequest;
import org.egov.stms.elasticsearch.entity.SewerageSearchResult;
import org.egov.stms.entity.es.SewerageIndex;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.utils.SewerageActionDropDownUtil;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SeweragePaginationService {

    private static final String SHSC_NUMBER = "shscNumber";
    private static final String YYYY_MM_DD = "yyyy-MM-dd";
    @Autowired
    private SewerageIndexService sewerageIndexService;
    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;
    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;

    public Page<SewerageIndex> searchResultObj(SewerageConnSearchRequest searchRequest,
            List<SewerageSearchResult> searchResultFomatted) {
        final Map<String, String> actionMap = new HashMap<>();
        final List<String> roleList = new ArrayList<>();
        final BoolQueryBuilder boolQuery = sewerageIndexService.getActiveApplications(searchRequest);
        for (final Role userRole : sewerageTaxUtils.getLoginUserRoles())
            roleList.add(userRole.getName());
        SewerageApplicationDetails sewerageApplicationDetails = null;
        SewerageSearchResult searchActions = null;
        final FieldSortBuilder sort = new FieldSortBuilder(SHSC_NUMBER).order(SortOrder.DESC);
        Page<SewerageIndex> searchResult = sewerageIndexService.getSearchResultByBoolQuery(boolQuery, sort, searchRequest);
        for (final SewerageIndex sewerageIndexObject : searchResult) {
            SewerageSearchResult searchResultObject = new SewerageSearchResult();
            buildSearchResult(sewerageIndexObject, searchResultObject);
            if (sewerageIndexObject.getApplicationNumber() != null)
                sewerageApplicationDetails = sewerageApplicationDetailsService
                        .findByApplicationNumber(sewerageIndexObject.getApplicationNumber());
            if (sewerageApplicationDetails != null)
                searchActions = SewerageActionDropDownUtil.getSearchResultWithActions(roleList,
                        sewerageIndexObject.getApplicationStatus(),
                        sewerageApplicationDetails);
            if (searchActions != null && !searchActions.getActions().isEmpty())
                getActions(searchActions, actionMap, searchRequest);
            searchResultObject.setActions(actionMap);
            searchResultFomatted.add(searchResultObject);
        }
        return searchResult;

    }

    private void buildSearchResult(final SewerageIndex sewerageIndexObject, final SewerageSearchResult searchResultObject) {
        searchResultObject.setApplicationNumber(sewerageIndexObject.getApplicationNumber());
        searchResultObject.setApplicationDate(DateUtils.toDefaultDateFormat(sewerageIndexObject.getApplicationDate()));
        searchResultObject.setAssessmentNumber(sewerageIndexObject.getPropertyIdentifier());
        searchResultObject.setShscNumber(sewerageIndexObject.getShscNumber());
        searchResultObject.setApplicantName(sewerageIndexObject.getConsumerName());
        searchResultObject.setApplicationType(sewerageIndexObject.getApplicationType());
        searchResultObject.setPropertyType(sewerageIndexObject.getPropertyType().replace("_", " "));
        searchResultObject.setRevenueWard(sewerageIndexObject.getWard());
        searchResultObject.setAddress(sewerageIndexObject.getAddress());
        searchResultObject.setApplicationStatus(sewerageIndexObject.getApplicationStatus());
    }

    private void getActions(final SewerageSearchResult searchActions, final Map<String, String> actionMap,
            final SewerageConnSearchRequest searchRequest) {
        for (final Map.Entry<String, String> entry : searchActions.getActions().entrySet())
            if (entry.getValue().equals(MODIFYLEGACYCONNECTION) && searchRequest.getLegacy() == null)
                actionMap.remove(entry.getKey(), entry.getValue());
            else if (!entry.getValue().equals(COLLECTDONATIONCHARHGES))
                actionMap.put(entry.getKey(), entry.getValue());

    }

    public Page<SewerageIndex> searchSewerageApplnsHasCollectionPending(final SewerageConnSearchRequest searchRequest,
            final List<SewerageSearchResult> searchResultList, final List<String> roleList, final Map<String, String> actionMap) {
        final BoolQueryBuilder boolQuery = sewerageIndexService.searchQueryFilterHasCollectionPending(searchRequest);
        final FieldSortBuilder sort = new FieldSortBuilder(SHSC_NUMBER).order(SortOrder.DESC);
        SewerageApplicationDetails sewerageApplicationDetails = null;
        Page<SewerageIndex> resultList = sewerageIndexService.getCollectSearchResult(boolQuery, sort, searchRequest);
        for (final SewerageIndex sewerageIndex : resultList) {
            SewerageSearchResult searchResult = new SewerageSearchResult();
            buildSearchResult(sewerageIndex, searchResult);
            if (sewerageIndex.getApplicationNumber() != null)
                sewerageApplicationDetails = sewerageApplicationDetailsService
                        .findByApplicationNumber(sewerageIndex.getApplicationNumber());
            for (final Role role : sewerageTaxUtils.getLoginUserRoles())
                roleList.add(role.getName());
            if (sewerageApplicationDetails != null
                    && (APPLICATION_STATUS_COLLECTINSPECTIONFEE.equals(sewerageApplicationDetails.getStatus().getCode()) ||
                            "FEECOLLECTIONPENDING".equals(sewerageApplicationDetails.getStatus().getCode()) ||
                            APPLICATION_STATUS_ESTIMATENOTICEGEN.equals(sewerageApplicationDetails.getStatus().getCode()) ||
                            APPLICATION_STATUS_SANCTIONED.equalsIgnoreCase(sewerageApplicationDetails.getStatus().getCode())
                                    && sewerageApplicationDetails.getCurrentDemand() != null)) {
                SewerageSearchResult searchActions = SewerageActionDropDownUtil.getSearchResultWithActions(roleList,
                        sewerageIndex.getApplicationStatus(), sewerageApplicationDetails);
                if (searchActions != null && searchActions.getActions() != null)
                    for (final Map.Entry<String, String> entry : searchActions.getActions().entrySet())
                        if (COLLECTDONATIONCHARHGES.equals(entry.getValue()))
                            actionMap.put(entry.getKey(), entry.getValue());
                searchResult.setActions(actionMap);
            }
            searchResultList.add(searchResult);
        }
        return resultList;
    }

    public Pageable sewerageNoticeSearch(final SewerageNoticeSearchRequest searchRequest,
            final List<SewerageSearchResult> searchResultFomatted, final Page<SewerageIndex> searchResult) {
        final Pageable pageable = new PageRequest(searchRequest.pageNumber(),
                searchRequest.pageSize(), searchRequest.orderDir(), searchRequest.orderBy());
        for (final SewerageIndex sewerageIndexObject : searchResult) {
            SewerageSearchResult searchResultObject = new SewerageSearchResult();
            searchResultObject.setApplicationNumber(sewerageIndexObject.getApplicationNumber());
            if (searchRequest.getNoticeType() != null)
                if (searchRequest.getNoticeType().equals(SewerageTaxConstants.NOTICE_WORK_ORDER)) {
                    searchResultObject.setNoticeNumber(sewerageIndexObject.getWorkOrderNumber());
                    searchResultObject
                            .setNoticeDate(DateUtils.getFormattedDate(sewerageIndexObject.getWorkOrderDate(), YYYY_MM_DD));
                } else if (searchRequest.getNoticeType().equals(SewerageTaxConstants.NOTICE_ESTIMATION)) {
                    searchResultObject.setNoticeNumber(sewerageIndexObject.getEstimationNumber());
                    searchResultObject
                            .setNoticeDate(DateUtils.getFormattedDate(sewerageIndexObject.getEstimationDate(), YYYY_MM_DD));
                } else if (searchRequest.getNoticeType().equals(SewerageTaxConstants.NOTICE_CLOSE_CONNECTION)) {
                    searchResultObject.setNoticeNumber(sewerageIndexObject.getClosureNoticeNumber());
                    searchResultObject
                            .setNoticeDate(DateUtils.getFormattedDate(sewerageIndexObject.getClosureNoticeDate(), YYYY_MM_DD));
                } else if (searchRequest.getNoticeType().equals(SewerageTaxConstants.NOTICE_REJECTION)) {
                    searchResultObject.setNoticeNumber(sewerageIndexObject.getRejectionNoticeNumber());
                    searchResultObject.setNoticeDate(
                            DateUtils.getFormattedDate(sewerageIndexObject.getRejectionNoticeDate(), YYYY_MM_DD));
                }
            searchResultObject.setShscNumber(sewerageIndexObject.getShscNumber());
            searchResultObject.setDoorNumber(sewerageIndexObject.getDoorNo());
            searchResultObject.setAddress(sewerageIndexObject.getAddress());
            searchResultObject.setApplicantName(sewerageIndexObject.getConsumerName());
            searchResultFomatted.add(searchResultObject);
        }
        return pageable;
    }

    public Page<SewerageIndex> buildPaymentSearch(final SewerageConnSearchRequest sewerageConnSearchRequest,
            final List<SewerageSearchResult> searchResultList) {
        final BoolQueryBuilder boolQuery = sewerageIndexService.getActiveApplications(sewerageConnSearchRequest);
        final FieldSortBuilder sort = new FieldSortBuilder(SHSC_NUMBER).order(SortOrder.DESC);
        Page<SewerageIndex> resultList = sewerageIndexService.getOnlinePaymentSearchResult(boolQuery, sort,
                sewerageConnSearchRequest);
        for (final SewerageIndex sewerageIndex : resultList) {
            SewerageSearchResult searchResult = new SewerageSearchResult();
            searchResult.setApplicationNumber(sewerageIndex.getApplicationNumber());
            searchResult.setAssessmentNumber(sewerageIndex.getPropertyIdentifier());
            searchResult.setShscNumber(sewerageIndex.getShscNumber());
            searchResult.setApplicantName(sewerageIndex.getConsumerName());
            searchResultList.add(searchResult);
        }
        return resultList;
    }

}
