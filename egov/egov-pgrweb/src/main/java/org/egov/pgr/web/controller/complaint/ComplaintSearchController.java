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

import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.pgr.entity.ComplaintStatus;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintStatusService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.utils.constants.PGRConstants;
import org.egov.pgr.web.contract.ComplaintSearchRequest;
import org.egov.search.domain.Document;
import org.egov.search.domain.Page;
import org.egov.search.domain.SearchResult;
import org.egov.search.domain.Sort;
import org.egov.search.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static java.util.Arrays.asList;

@Controller
// @RequestMapping(value = "/complaint/citizen/anonymous/search")
// @RequestMapping(value = "/complaint/search")
public class ComplaintSearchController {

    private final SearchService searchService;

    private final ComplaintService complaintService;

    private final ComplaintStatusService complaintStatusService;

    private final AssignmentService assignmentService;

    private final DepartmentService departmentService;

    private final SecurityUtils securityUtils;

    private static final Logger logger = LoggerFactory.getLogger(ComplaintSearchController.class);

    @Autowired
    private final CityService cityService;

    private final ComplaintTypeService complaintTypeService;

    @Autowired
    public ComplaintSearchController(final SearchService searchService, final ComplaintService complaintService,
            final ComplaintStatusService complaintStatusService, final ComplaintTypeService complaintTypeService,
            final AssignmentService assignmentService, final SecurityUtils securityUtils,
            final CityService cityService, final DepartmentService departmentService) {
        this.searchService = searchService;
        this.complaintService = complaintService;
        this.complaintStatusService = complaintStatusService;
        this.assignmentService = assignmentService;
        this.securityUtils = securityUtils;
        this.cityService = cityService;
        this.departmentService = departmentService;
        this.complaintTypeService = complaintTypeService;
    }

    public @ModelAttribute("complaintTypedropdown") List<ComplaintType> complaintTypes() {
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
        return complaintService.getAllReceivingModes();
    }

    @ModelAttribute("currentLoggedUser")
    public String currentLoggedUser() {
        final User user = securityUtils.getCurrentUser();
        if (null != user)
            return securityUtils.getCurrentUser().getUsername();
        else
            return "";
    }

    @ModelAttribute("isGrievanceOfficer")
    public Boolean validateForGo() {
        Boolean isGoRole = Boolean.FALSE;
        final User user = securityUtils.getCurrentUser();
        if (null != user)
            for (final Role role : user.getRoles())
                if (PGRConstants.GO_ROLE_NAME.equalsIgnoreCase(role.getName())) {
                    isGoRole = Boolean.TRUE;
                    break;
                }
        return isGoRole;
    }

    @ModelAttribute("employeeposition")
    public Long employeePosition() {
        logger.debug("User is :" + currentLoggedUser());
        final User user = securityUtils.getCurrentUser();
        if (null != user) {
            logger.debug("Assignment" + assignmentService
                    .getAllActiveEmployeeAssignmentsByEmpId(securityUtils.getCurrentUser().getId()).size());

            if (assignmentService.getAllActiveEmployeeAssignmentsByEmpId(user.getId()).size() < 1)
                return 0l;
            logger.debug("Position " + assignmentService.getAllActiveEmployeeAssignmentsByEmpId(user.getId()).get(0)
                    .getPosition().getId());
            return assignmentService.getAllActiveEmployeeAssignmentsByEmpId(user.getId()).get(0).getPosition().getId();
        } else
            return 0l;
    }

    @ModelAttribute
    public ComplaintSearchRequest searchRequest() {
        return new ComplaintSearchRequest();
    }

    @ModelAttribute("currentUlb")
    public String getCurrentUlb() {

        final City cityWebsite = cityService.getCityByURL(EgovThreadLocals.getDomainName());
        if (null != cityWebsite) {
            logger.debug("logged in as " + cityWebsite.getName());
            return cityWebsite.getName();
        } else
            return "";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/complaint/search")
    public String showSearchFormOffical() {
        return "complaint-search";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/complaint/citizen/anonymous/search")
    public String showSearchFormCitizen() {
        return "complaint-search";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/complaint/search")
    @ResponseBody
    public List<Document> searchComplaintsOffical(@ModelAttribute final ComplaintSearchRequest searchRequest) {
        SearchResult searchResult = null;

        searchResult = searchService.search(asList(Index.PGR.toString()), asList(IndexType.COMPLAINT.toString()),
                searchRequest.searchQuery(), searchRequest.searchFilters(), Sort.NULL, Page.NULL);
        

        return searchResult.getDocuments();

    }

    @RequestMapping(method = RequestMethod.POST, value = "/complaint/citizen/anonymous/search")
    @ResponseBody
    public List<Document> searchComplaintsCitizen(@ModelAttribute final ComplaintSearchRequest searchRequest) {
        final SearchResult searchResult = searchService.search(asList(Index.PGR.toString()),
                asList(IndexType.COMPLAINT.toString()), searchRequest.searchQuery(), searchRequest.searchFilters(),
                Sort.NULL, Page.NULL);

        return searchResult.getDocuments();

    }
}
