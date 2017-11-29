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

package org.egov.adtax.web.controller.hoarding;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.egov.adtax.entity.AdvertisementReassignDetails;
import org.egov.adtax.service.ReassignAdvertisementService;
import org.egov.adtax.workflow.AdvertisementWorkFlowService;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/reassignadvertisement/{applicationId}/{applicationType}")
public class AdvertisementReassignController {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private ReassignAdvertisementService reassignAdvertisementService;

    @Autowired
    private AdvertisementWorkFlowService advertisementWorkFlowService;

    @Autowired
    private PositionMasterService positionMasterService;

    private static final String SUCCESSMESSAGE = "successMessage";

    @Autowired
    protected ResourceBundleMessageSource messageSource;

    @ModelAttribute
    public AdvertisementReassignDetails reassign() {
        return new AdvertisementReassignDetails();
    }

    public Long getLoggedInPositiontionId() {
        Position position = null;
        Long userId = ApplicationThreadLocals.getUserId();
        if (userId != null && userId.intValue() != 0) {
            position = positionMasterService.getPositionByUserId(userId);
        }
        return position.getId();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getReassign(@ModelAttribute("reassign") final AdvertisementReassignDetails reassignInfo, final Model model,
            @PathVariable final String applicationType, @PathVariable final Long applicationId, final HttpServletRequest request) {

        final String designationStr = advertisementWorkFlowService.getDesignationForCscOperatorWorkFlow();
        final String departmentStr = advertisementWorkFlowService.getDepartmentForCscOperatorWorkFlow();
        final String[] departments = departmentStr.split(",");
        final String[] designations = designationStr.split(",");
        Department dept = null;
        Map<Long, String> employeeWithPosition = new HashMap<>();

        for (String department : departments) {
            dept = departmentService.getDepartmentByName(department);
            for (String designationName : Arrays.asList(designations)) {
                List<Designation> desig = designationService.getDesignationsByName(designationName);
                for (Designation designation : desig) {
                    List<Assignment> assignments = assignmentService.findAllAssignmentsByDeptDesigAndDates(dept.getId(),
                            designation.getId(), new Date());
                    if (!assignments.isEmpty()) {
                        for (Assignment assignment : assignments) {
                            if (assignment != null && assignment.getPosition() != null && !getLoggedInPositiontionId().equals(assignment.getPosition().getId())) 
                                {
                                    employeeWithPosition.put(assignment.getPosition().getId(),
                                            assignment.getEmployee().getName().concat("/")
                                                    .concat(assignment.getPosition().getName()));
                                    model.addAttribute("assignments", employeeWithPosition);
                                }
                            }
                        }
                    }
                }
            }
        
        if (employeeWithPosition.isEmpty()) {
            model.addAttribute("message", messageSource.getMessage("notexists.position",
                    new String[] {}, null));
        }

        reassignInfo.setApplicationId(applicationId);
        reassignInfo.setStateType(applicationType);
        return "advertisement-reassign";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String update(@ModelAttribute("reassign") final AdvertisementReassignDetails reassignInfo, final Model model,
            @Valid final BindingResult errors, final HttpServletRequest request) {
        String successMessage;
        Long positionId = Long.valueOf(request.getParameter("approvalPosition"));
        Position position = positionMasterService.getPositionById(positionId);
        Assignment assignment = assignmentService.getAssignmentsForPosition(positionId).get(0);
        String appNo = reassignAdvertisementService.getStateObject(reassignInfo, position);

        if (StringUtils.isNotEmpty(appNo)) {
            successMessage = "Avertisement Tax application with application number : " + appNo + " successfully re-assigned to "
                    + assignment.getEmployee().getName() + "~" + assignment.getDesignation().getName() + ":"
                    + assignment.getDepartment().getCode();
            model.addAttribute(SUCCESSMESSAGE, successMessage);
        } else {
            model.addAttribute(SUCCESSMESSAGE, "Reassign Failed!");
        }
        return "reassign-success";
    }

}