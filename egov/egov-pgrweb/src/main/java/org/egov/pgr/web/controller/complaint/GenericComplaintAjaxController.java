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

package org.egov.pgr.web.controller.complaint;

import org.egov.eis.service.EmployeeViewService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
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
    private EmployeeViewService employeeViewService;

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