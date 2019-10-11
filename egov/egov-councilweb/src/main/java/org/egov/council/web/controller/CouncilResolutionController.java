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

package org.egov.council.web.controller;

import static java.lang.String.format;
import static org.egov.council.utils.constants.CouncilConstants.COUNCIL_RESOLUTION_DETAILS_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.egov.council.entity.CouncilPreambleResponse;
import org.egov.council.entity.CouncilResolutionsResponse;
import org.egov.council.entity.MeetingMOM;
import org.egov.council.service.CouncilMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CouncilResolutionController {

    @Autowired
    private CouncilMeetingService councilMeetingService;

    @RequestMapping(value = "/councilresolution", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public CouncilPreambleResponse getCouncilPreambleDetails(@RequestParam("councilResolutionNo") String councilResolutionNo) {
        final MeetingMOM meetingMom = councilMeetingService.findByResolutionNumber(councilResolutionNo);
        if (meetingMom == null)
            return new CouncilPreambleResponse(HttpStatus.BAD_REQUEST.toString(), "Invalid Resolution Number", false);
        else
            return new CouncilPreambleResponse(meetingMom.getPreamble().getPreambleNumber(),
                    meetingMom.getPreamble().getGistOfPreamble(), HttpStatus.OK.toString(), "Success", true);
    }
    
    @RequestMapping(value = "/councilresolutiondetails", method = GET, produces = APPLICATION_JSON_VALUE)
    public CouncilResolutionsResponse getResolutionDetails(@RequestParam String resolutionNo,
            @RequestParam String committeeType) {
        CouncilResolutionsResponse councilResolutionsResponse;
        MeetingMOM meetingMom = councilMeetingService.findByResolutionNumberAndCommitteeType(resolutionNo, committeeType);
        if (meetingMom != null) {
            councilResolutionsResponse = new CouncilResolutionsResponse();
            councilResolutionsResponse.setResolutionNo(meetingMom.getResolutionNumber());
            councilResolutionsResponse.setCommitteeType(meetingMom.getAgenda().getCommitteeType().getCode());
            councilResolutionsResponse.setResolutionDate(meetingMom.getMeeting().getMeetingDate());
            councilResolutionsResponse
                    .setCouncilResolutionUrl(format(COUNCIL_RESOLUTION_DETAILS_URL, meetingMom.getMeeting().getId()));
        } else {
            councilResolutionsResponse = new CouncilResolutionsResponse();
            councilResolutionsResponse.setErrorMessage("COUNCIL RESOLUTION DOES NOT EXIST");
        }
        return councilResolutionsResponse;
    }

}
