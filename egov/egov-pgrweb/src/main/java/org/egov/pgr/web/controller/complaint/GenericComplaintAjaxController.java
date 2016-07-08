/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.pgr.web.controller.complaint;

import org.egov.eis.service.DesignationService;
import org.egov.eis.service.EmployeeViewService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.ReceivingCenter;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.ReceivingCenterService;
import org.egov.pims.commons.Designation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class GenericComplaintAjaxController {

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private CrossHierarchyService crossHierarchyService;

    @Autowired
    private ReceivingCenterService receivingCenterService;

    @Autowired
    private ComplaintTypeService complaintTypeService;

    @Autowired
    private EmployeeViewService employeeViewService;

    @RequestMapping(value = { "/complaint/citizen/complaintTypes", "/complaint/citizen/anonymous/complaintTypes",
            "/complaint/officials/complaintTypes", "/complaint/router/complaintTypes", "/complaint/escalationTime/complaintTypes" },
            method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody List<ComplaintType> getAllActiveComplaintTypesByNameLike(@RequestParam final String complaintTypeName) {
        return complaintTypeService.findAllActiveByNameLike(complaintTypeName);
    }

    @RequestMapping(value = { "/complaint/citizen/complainttypes-by-category", "/complaint/citizen/anonymous/complainttypes-by-category",
            "officials/complainttypes-by-category" }, method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody List<ComplaintType> complaintTypesByCategory(@RequestParam final Long categoryId) {
        return complaintTypeService.findActiveComplaintTypesByCategory(categoryId);
    }

    @RequestMapping(value = { "/public/complaint/complaintTypes", "search/complaintTypes",
            "search/complaintTypes" }, method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody List<ComplaintType> getAllComplaintTypesByNameLike(@RequestParam final String complaintTypeName) {
        return complaintTypeService.findAllActiveByNameLike(complaintTypeName);
    }

    @RequestMapping(value = "/complaint/escalationTime/ajax-approvalDesignations", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody List<Designation> getAllDesignationsByName(@RequestParam final String designationName) {
        return designationService.getAllDesignationsByNameLike(designationName);
    }

    @RequestMapping(value = "/complaint/officials/isCrnRequired", method = GET)
    public @ResponseBody boolean isCrnRequired(@RequestParam final Long receivingCenterId) {
        final ReceivingCenter receivingCenter = receivingCenterService.findByRCenterId(receivingCenterId);
        return receivingCenter == null ? Boolean.TRUE : receivingCenter.isCrnRequired();
    }

    @RequestMapping(value = { "/complaint/citizen/locations", "citizen/anonymous/locations",
            "officials/locations" }, method = GET, produces = TEXT_PLAIN_VALUE)
    public @ResponseBody String getAllLocationJSON(@RequestParam final String locationName) {
        final StringBuilder locationJSONData = new StringBuilder("[");
        final String locationNameLike = "%" + locationName + "%";
        crossHierarchyService
                .getChildBoundaryNameAndBndryTypeAndHierarchyType("Locality", "Location", "Administration", locationNameLike)
                .stream().forEach(location -> {
                    locationJSONData.append("{\"name\":\"");

                    locationJSONData.append(location.getChild().getName() + " - " + location.getParent().getName());
                    locationJSONData.append("\",\"id\":").append(location.getId()).append("},");
                });

        if (locationJSONData.lastIndexOf(",") != -1)
            locationJSONData.deleteCharAt(locationJSONData.lastIndexOf(","));

        locationJSONData.append("]");
        return locationJSONData.toString();
    }

    @RequestMapping(value = { "/complaint/router/position", "/complaint/escalation/position" }, method = GET, produces = TEXT_PLAIN_VALUE)
    public @ResponseBody String getAllPositionByNameLike(@RequestParam final String positionName,
            final HttpServletResponse response) throws IOException {
        final String likePositionName = "%" + positionName.toUpperCase() + "%";
        final StringBuilder positionUser = new StringBuilder("[");
        employeeViewService.findByUserNameLikeOrCodeLikeOrPosition_NameLike(likePositionName,
                likePositionName, likePositionName, new Date()).stream().forEach(position -> {
                    positionUser.append("{\"name\":\"");
                    positionUser
                            .append(position.getPosition().getName() + '-' + position.getName()+ '-' + position.getCode());
                    positionUser.append("\",\"id\":").append(position.getPosition().getId()).append("},");
                });
        if (positionUser.lastIndexOf(",") != -1)
            positionUser.deleteCharAt(positionUser.lastIndexOf(","));
        positionUser.append("]");
        return positionUser.toString();

    }

    @RequestMapping(value = { "/complaint/router/boundaries-by-type",
            "/complaint/escalation/boundaries-by-type" }, method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody List<Boundary> getBoundariesbyType(@RequestParam final String boundaryName,
            @RequestParam final Long boundaryTypeId, final HttpServletResponse response) throws IOException {
        final String likeBoundaryName = "%" + boundaryName + "%";
        return boundaryService.getBondariesByNameAndType(likeBoundaryName, boundaryTypeId);
    }

}