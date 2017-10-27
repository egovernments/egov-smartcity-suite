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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

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

    @GetMapping(value = {"/complaint/citizen/complaintTypes", "/complaint/citizen/anonymous/complaintTypes",
            "/complaint/officials/complaintTypes", "/complaint/router/complaintTypes", "/complaint/escalationTime/complaintTypes"},
            produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ComplaintType> getAllActiveComplaintTypesByNameLike(@RequestParam String complaintTypeName) {
        return complaintTypeService.findAllActiveByNameLike(complaintTypeName);
    }

    @GetMapping(value = {"/complaint/citizen/complainttypes-by-category", "/complaint/citizen/anonymous/complainttypes-by-category",
            "/complaint/officials/complainttypes-by-category"}, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ComplaintType> complaintTypesByCategory(@RequestParam Long categoryId) {
        return complaintTypeService.findActiveComplaintTypesByCategory(categoryId);
    }

    @GetMapping(value = "/complaint/complaintTypes", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ComplaintType> getAllComplaintTypesByNameLike(@RequestParam String complaintTypeName) {
        return complaintTypeService.findAllActiveByNameLike(complaintTypeName);
    }

    @GetMapping(value = "/complaint/escalationTime/ajax-approvalDesignations", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Designation> getAllDesignationsByName(@RequestParam String designationName) {
        return designationService.getAllDesignationsByNameLike(designationName);
    }

    @GetMapping("/complaint/officials/isCrnRequired")
    @ResponseBody
    public boolean isCrnRequired(@RequestParam Long receivingCenterId) {
        ReceivingCenter receivingCenter = receivingCenterService.findByRCenterId(receivingCenterId);
        return receivingCenter == null || receivingCenter.isCrnRequired();
    }

    @GetMapping(value = {"/complaint/citizen/locations", "/complaint/citizen/anonymous/locations",
            "/complaint/officials/locations"}, produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getAllLocationJSON(@RequestParam String locationName) {
        StringBuilder locationJSONData = new StringBuilder();
        locationJSONData.append("[");
        crossHierarchyService
                .getChildBoundaryNameAndBndryTypeAndHierarchyType("Locality", "Location", "Administration", "%" + locationName + "%")
                .stream()
                .filter(ch -> ch.getParent().isActive() && ch.getChild().isActive())
                .forEach(location ->
                        locationJSONData.append("{\"name\":\"")
                                .append(location.getChild().getName()).append(" - ").append(location.getParent().getName())
                                .append("\",\"id\":").append(location.getId()).append("},")
                );

        if (locationJSONData.lastIndexOf(",") != -1)
            locationJSONData.deleteCharAt(locationJSONData.lastIndexOf(","));

        locationJSONData.append("]");
        return locationJSONData.toString();
    }

    @GetMapping(value = {"/complaint/router/position", "/complaint/escalation/position"}, produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getAllPositionByNameLike(@RequestParam String positionName) {
        StringBuilder positionUser = new StringBuilder();
        positionUser.append("[");
        String likePositionName = "%" + positionName.toUpperCase() + "%";
        employeeViewService
                .getEmployeeByNameOrCodeOrPositionLike(likePositionName, likePositionName, likePositionName, new Date())
                .stream()
                .forEach(employee ->
                        positionUser.append("{\"name\":\"")
                                .append(employee.getPosition().getName()).append('-')
                                .append(employee.getName()).append('-').append(employee.getCode())
                                .append("\",\"id\":").append(employee.getPosition().getId()).append("},")
                );
        if (positionUser.lastIndexOf(",") != -1)
            positionUser.deleteCharAt(positionUser.lastIndexOf(","));
        positionUser.append("]");
        return positionUser.toString();

    }

    @GetMapping(value = {"/complaint/router/boundaries-by-type", "/complaint/escalation/boundaries-by-type"},
            produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Boundary> getBoundariesByType(@RequestParam String boundaryName,
                                              @RequestParam Long boundaryTypeId) {
        return boundaryService.getBondariesByNameAndTypeOrderByBoundaryNumAsc("%" + boundaryName + "%", boundaryTypeId);
    }

}