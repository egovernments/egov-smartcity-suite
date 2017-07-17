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

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.egov.pgr.utils.constants.PGRConstants.GO_ROLE_NAME;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.pgr.entity.ComplaintStatus;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.es.ComplaintIndex;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintStatusService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.ReceivingModeService;
import org.egov.pgr.service.es.ComplaintIndexService;
import org.egov.pgr.web.contract.ComplaintSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ComplaintSearchController {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private ComplaintStatusService complaintStatusService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private ComplaintTypeService complaintTypeService;

    @Autowired
    private ComplaintIndexService complaintIndexService;

    @Autowired
    private ReceivingModeService receivingModeService;

    @ModelAttribute("complaintTypedropdown")
    public List<ComplaintType> complaintTypes() {
        return complaintTypeService.findActiveComplaintTypes();
    }

    @ModelAttribute("complaintTypeDepartments")
    public List<Department> complaintTypeDepartments() {
        return departmentService.getAllDepartments();
    }

    @ModelAttribute("complaintStatuses")
    public List<ComplaintStatus> complaintStatuses() {
        return complaintStatusService.getAllComplaintStatus();
    }

    @ModelAttribute("complaintReceivingModes")
    public List complaintReceivingModes() {
        return receivingModeService.getReceivingModes();
    }

    @ModelAttribute("currentLoggedUser")
    public String currentLoggedUser() {
        final User user = securityUtils.getCurrentUser();
        return user != null ? user.getUsername() : EMPTY;
    }

    @ModelAttribute("isGrievanceOfficer")
    public Boolean validateForGo() {
        final User user = securityUtils.getCurrentUser();
        if (user != null)
            for (final Role role : user.getRoles())
                if (GO_ROLE_NAME.equalsIgnoreCase(role.getName()))
                    return Boolean.TRUE;
        return Boolean.FALSE;
    }

    @ModelAttribute("employeeposition")
    public Long employeePosition() {
        final User user = securityUtils.getCurrentUser();
        return user != null && !assignmentService.getAllActiveEmployeeAssignmentsByEmpId(user.getId()).isEmpty()
                ? assignmentService.getAllActiveEmployeeAssignmentsByEmpId(user.getId()).get(0).getPosition().getId()
                : 0L;
    }

    @ModelAttribute
    public ComplaintSearchRequest searchRequest() {
        return new ComplaintSearchRequest();
    }

    @ModelAttribute("currentUlb")
    public String getCurrentUlb() {
        return ApplicationThreadLocals.getCityName();
    }

    @RequestMapping(method = GET, value = { "/complaint/search", "/complaint/citizen/anonymous/search" })
    public String showSearch(final HttpServletRequest request, final Model model) {
        model.addAttribute("isMore", Boolean.parseBoolean(request.getParameter("isMore")));
        return "complaint-search";
    }

    @RequestMapping(method = POST, value = { "/complaint/search", "/complaint/citizen/anonymous/search" })
    @ResponseBody
    public Iterable<ComplaintIndex> searchComplaints(@ModelAttribute final ComplaintSearchRequest searchRequest) {
        return complaintIndexService.searchComplaintIndex(searchRequest.query());
    }
}