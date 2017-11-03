/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2017  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.web.controller.complaint;

import org.egov.eis.entity.EmployeeView;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.JsonUtils;
import org.egov.pgr.entity.contract.ProcessOwnerAdaptor;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.pims.service.EisUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ComplaintProcessOwnerSelectionController {

    @Autowired
    private EisUtilService eisService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private CrossHierarchyService crossHierarchyService;

    @Autowired
    private SecurityUtils securityUtils;

    @GetMapping(value = "/ajax-getChildLocation", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Boundary> getChildBoundariesById(@RequestParam Long id) {
        return crossHierarchyService.getActiveChildBoundariesByParentId(id);
    }

    @GetMapping(value = {"/ajax-approvalDesignations", "/ajax-designationsByDepartment"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Designation> getDesignations(
            @ModelAttribute("designations") @RequestParam Long approvalDepartment) {
        return designationService.getAllDesignationByDepartment(approvalDepartment);
    }

    @GetMapping(value = "/ajax-positionsByDepartmentAndDesignation", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Position> getPositionByDepartmentAndDesignation(@RequestParam Long approvalDepartment,
                                                                @RequestParam Long approvalDesignation) {
        return positionMasterService.getPositionsByDepartmentAndDesignationId(approvalDepartment, approvalDesignation);
    }

    @GetMapping(value = "/ajax-approvalPositions", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getPositions(@RequestParam Integer approvalDepartment,
                               @RequestParam Integer approvalDesignation) {
        if (approvalDepartment != null && approvalDepartment != 0 && approvalDesignation != null && approvalDesignation != 0) {
            HashMap<String, String> paramMap = new HashMap<>();
            paramMap.put("departmentId", String.valueOf(approvalDepartment));
            paramMap.put("designationId", String.valueOf(approvalDesignation));
            List<EmployeeView> employeeViewData = eisService.getEmployeeInfoList(paramMap);
            Set<EmployeeView> processOwners = employeeViewData
                    .stream()
                    .filter(employeeView -> (employeeView.getEmployee().hasRole("Redressal Officer")
                            || employeeView.getEmployee().hasRole("Grievance Officer")
                            || employeeView.getEmployee().hasRole("Grievance Routing Officer")) && !securityUtils.getCurrentUser().getName().equals(employeeView.getName()))
                    .collect(Collectors.toSet());
            return JsonUtils.toJSON(processOwners, EmployeeView.class, ProcessOwnerAdaptor.class);
        }
        return "[]";
    }
}
