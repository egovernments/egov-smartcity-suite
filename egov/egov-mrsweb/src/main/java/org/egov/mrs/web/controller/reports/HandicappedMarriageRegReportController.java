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

package org.egov.mrs.web.controller.reports;

import static org.egov.infra.utils.JsonUtils.toJSON;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.entity.es.MarriageRegIndexSearchResult;
import org.egov.mrs.entity.es.MarriageRegistrationIndex;
import org.egov.mrs.service.es.MarriageRegistrationIndexService;
import org.egov.mrs.web.adaptor.HandicappedReportJsonAdaptor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller to show report of Registration status
 *
 * @author malathi
 *
 */
@Controller
@RequestMapping(value = "/report")
public class HandicappedMarriageRegReportController {

    private static final String HANDICAPPED_MARRIAGE_REPORT = "handicapped-marriage-report";
    @Autowired
    private CityService cityService;
    @Autowired
    private MarriageRegistrationIndexService marriageRegistrationIndexService;

    @RequestMapping(value = "/handicapped-report", method = RequestMethod.GET)
    public String searchHandicappedApplications(final Model model)
            throws ParseException {
        return HANDICAPPED_MARRIAGE_REPORT;
    }

    private List<MarriageRegistrationIndex> getSearchResult(final String applicantType) {
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        String ulbName = "";
        if (cityWebsite != null)
            ulbName = cityWebsite.getName();
        final BoolQueryBuilder boolQuery = marriageRegistrationIndexService.getQueryFilterForHandicap(applicantType, ulbName);
        return marriageRegistrationIndexService.getHandicapSearchResultByBoolQuery(boolQuery);
    }

    @RequestMapping(value = "/handicapped-report-search", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String showHandicappedApplicationDetails(@RequestParam("applicantType") final String applicantType, final Model model,
            @ModelAttribute final MarriageRegistration registration) {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY");
        final List<MarriageRegIndexSearchResult> searchResultFomatted = new ArrayList<>();
        MarriageRegIndexSearchResult mrisr;
        for (final MarriageRegistrationIndex mrsIndexObj : getSearchResult(applicantType)) {
            mrisr = new MarriageRegIndexSearchResult();
            mrisr.setApplicationNumber(mrsIndexObj.getApplicationNo());
            mrisr.setRegistrationNumber(mrsIndexObj.getRegistrationNo());
            mrisr.setRegistrationDate(formatter.format(mrsIndexObj.getApplicationCreatedDate()));
            mrisr.setZone(mrsIndexObj.getZone());
            mrisr.setStatus(mrsIndexObj.getApplicationStatus());
            mrisr.setMarriageDate(formatter.format(mrsIndexObj.getRegistrationDate()));
            mrisr.setHusbandName(mrsIndexObj.getHusbandName());
            mrisr.setWifeName(mrsIndexObj.getWifeName());
            searchResultFomatted.add(mrisr);
        }

        return new StringBuilder("{\"data\":")
                .append(toJSON(searchResultFomatted, MarriageRegIndexSearchResult.class, HandicappedReportJsonAdaptor.class))
                .append("}")
                .toString();
    }

}
