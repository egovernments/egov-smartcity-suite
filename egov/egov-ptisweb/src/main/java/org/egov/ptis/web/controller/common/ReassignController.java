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
package org.egov.ptis.web.controller.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.ptis.bean.ReassignInfo;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.service.reassign.ReassignService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/reassign/{modelIdAndApplicationType}")
public class ReassignController {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private ReassignService reassignService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    private static final String SUCCESSMESSAGE = "successMessage";

    @ModelAttribute
    public ReassignInfo reassign() {
        return new ReassignInfo();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getReassign(@ModelAttribute("reassign") final ReassignInfo reassignInfo, final Model model,
            @PathVariable final String modelIdAndApplicationType,
            final HttpServletRequest request) {
        final Department department = departmentService.getDepartmentByCode("REV");
        final Map<Long, String> employeeWithPosition = new HashMap<>();
        final List<Assignment> assignments = new ArrayList<>();
        final List<Designation> designations = designationService
                .getDesignationsByNames(Arrays.asList(PropertyTaxConstants.JUNIOR_ASSISTANT.toUpperCase(),
                        PropertyTaxConstants.SENIOR_ASSISTANT.toUpperCase()));
        for (final Designation designation : designations)
            assignments.addAll(assignmentService.findAllAssignmentsByDeptDesigAndDates(department.getId(),
                    designation.getId(), new Date()));
        List<Long> loggedInUserPositionIds = propertyTaxCommonUtils.getPositionForUser(ApplicationThreadLocals.getUserId());
        for (final Assignment assignment : assignments)
            if(loggedInUserPositionIds.isEmpty() || !loggedInUserPositionIds.contains(assignment.getPosition().getId()))
                employeeWithPosition.put(assignment.getPosition().getId(), assignment.getEmployee().getName().concat("/")
                        .concat(assignment.getPosition().getName()));
        model.addAttribute("assignments", employeeWithPosition);
        model.addAttribute("stateAwareId", Long.valueOf(modelIdAndApplicationType.split("&")[1]));
        model.addAttribute("transactionType", modelIdAndApplicationType.split("&")[0]);
        return "reassign";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String update(@ModelAttribute("reassign") final ReassignInfo reassignInfo, final Model model,
            @Valid final BindingResult errors, final HttpServletRequest request) {
        String successMessage;
        final Long positionId = Long.valueOf(request.getParameter("approvalPosition"));
        final Position position = positionMasterService.getPositionById(positionId);
        final Assignment assignment = assignmentService.getAssignmentsForPosition(positionId).get(0);
        if (reassignService.getStateObject(reassignInfo, position)) {
            successMessage = "Reassigned successfully to "
                    + assignment.getEmployee().getName();
            model.addAttribute(SUCCESSMESSAGE, successMessage);
        } else
            model.addAttribute(SUCCESSMESSAGE, "Reassign Failed!");
        return "reassign-success";
    }

}
