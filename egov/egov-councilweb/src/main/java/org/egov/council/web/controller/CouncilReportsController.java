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

package org.egov.council.web.controller;

import static org.egov.council.utils.constants.CouncilConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.council.utils.constants.CouncilConstants.WARD;
import static org.egov.infra.utils.JsonUtils.toJSON;

import java.util.ArrayList;
import java.util.List;

import org.egov.council.entity.CommitteeType;
import org.egov.council.entity.CouncilMeeting;
import org.egov.council.entity.CouncilPreamble;
import org.egov.council.entity.es.CouncilMeetingDetailsSearchRequest;
import org.egov.council.entity.es.CouncilMeetingDetailsSearchResult;
import org.egov.council.entity.es.CouncilMeetingIndex;
import org.egov.council.service.CommitteeTypeService;
import org.egov.council.service.CouncilMeetingService;
import org.egov.council.service.CouncilPreambleService;
import org.egov.council.service.es.CouncilMeetingIndexService;
import org.egov.council.web.adaptor.CouncilMeetingDetailsReportJsonAdaptor;
import org.egov.council.web.adaptor.CouncilPreambleJsonAdaptor;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/councilreports")
public class CouncilReportsController {

	private static final String COUNCILPREAMBLE_WARDWISE_SEARCH = "preamblewardwise-report";
	private static final String COUNCILMEETING_DETAILS_SEARCH = "meetingdetails-report";

	@Autowired
	private CouncilPreambleService councilPreambleService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private BoundaryService boundaryService;
	@Autowired
	private CityService cityService;
	@Autowired
	private CouncilMeetingIndexService councilMeetingIndexService;
	@Autowired
        private CommitteeTypeService committeeTypeService;
	
	@Autowired
	private CouncilMeetingService councilMeetingService;
	
	@ModelAttribute("committeeType") public List<CommitteeType> getCommitteTypeList() {
            return committeeTypeService.getActiveCommiteeType();
	}

	@ModelAttribute("departments") public List<Department> getDepartmentList() {
		return departmentService.getAllDepartments();
	}

	@ModelAttribute("wards") public List<Boundary> getWardsList() {
		return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WARD, REVENUE_HIERARCHY_TYPE);
	}

	@RequestMapping(value = "/preamblewardwise/search", method = RequestMethod.GET)
	public String getSearchView(Model model) {
		model.addAttribute("councilPreamble", new CouncilPreamble());
		return COUNCILPREAMBLE_WARDWISE_SEARCH;
	}

	@RequestMapping(value = "/preamblewardwise/search-result", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody public String searchPreambleWardwise(Model model,
			@ModelAttribute final CouncilPreamble councilPreamble, final BindingResult errors) {
		if (errors.hasErrors()) {
			return COUNCILPREAMBLE_WARDWISE_SEARCH;
		}
		List<CouncilPreamble> searchResultList = councilPreambleService
				.searchPreambleForWardwiseReport(councilPreamble);
		return  new StringBuilder("{\"data\":")
                        .append(toJSON(searchResultList, CouncilPreamble.class, CouncilPreambleJsonAdaptor.class)).append("}")
                        .toString();
	}
	
    @RequestMapping(value = "/meetingdetails/search", method = RequestMethod.GET)
    public String getMeetingDetails(Model model) {
            model.addAttribute("searchRequest", new CouncilMeetingDetailsSearchRequest());
            return COUNCILMEETING_DETAILS_SEARCH;
    }
	
    @RequestMapping(value = "/meetingdetails/search", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchApplication(
            @ModelAttribute final CouncilMeetingDetailsSearchRequest searchRequest) {
        final List<CouncilMeetingDetailsSearchResult> searchResultFomatted = new ArrayList<>();
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        if (cityWebsite != null)
            searchRequest.setUlbName(cityWebsite.getName());
        final BoolQueryBuilder boolQuery = councilMeetingIndexService.prepareWhereClause(searchRequest);
        final FieldSortBuilder sort = new FieldSortBuilder("committeeType").order(SortOrder.DESC);
        List<CouncilMeetingIndex> detailsSearchResults = councilMeetingIndexService.getSearchResultByBoolQuery(boolQuery, sort);
        for (CouncilMeetingIndex councilMeetingIndex : detailsSearchResults) {
            CouncilMeeting councilmeeting =null;
            CouncilMeetingDetailsSearchResult searchResult = new CouncilMeetingDetailsSearchResult();
            searchResult.setCommitteeType(councilMeetingIndex.getCommitteeType());
            searchResult.setTotalPreambles(councilMeetingIndex.getTotalNoOfPreamblesUsed());
            searchResult.setApprovedPreambles(councilMeetingIndex.getNoOfPreamblesApproved());
            searchResult.setAdjournedPreambles(councilMeetingIndex.getNoOfPreamblesPostponed());
            searchResult.setRejectedPreambles(councilMeetingIndex.getNoOfPreamblesRejected());
            searchResult.setTotalNoOfCommitteMembers(councilMeetingIndex.getTotalNoOfCommitteMembers());
            searchResult.setNoOfCommitteMembersPresent(councilMeetingIndex.getNoOfCommitteMembersPresent());
            searchResult.setNoOfCommitteMembersAbsent(councilMeetingIndex.getNoOfCommitteMembersAbsent());
            searchResult.setMeetingDate(councilMeetingIndex.getMeetingDate());
            searchResult.setMeetingLocation(councilMeetingIndex.getMeetingLocation());
            searchResult.setMeetingNumber(councilMeetingIndex.getMeetingNumber());
            searchResult.setMeetingTime(councilMeetingIndex.getMeetingTime());
            if (councilMeetingIndex.getMeetingNumber() != null) {
                councilmeeting = councilMeetingService.findByMeetingNumber(councilMeetingIndex.getMeetingNumber());

            }
            if (councilmeeting != null) {
                searchResult.setId(councilmeeting.getId());
            } else
                searchResult.setId(null);
            searchResultFomatted.add(searchResult);
        }
        return  new StringBuilder("{\"data\":")
                .append(toJSON(searchResultFomatted, CouncilMeetingDetailsSearchResult.class, CouncilMeetingDetailsReportJsonAdaptor.class)).append("}")
                .toString();
    }
}
