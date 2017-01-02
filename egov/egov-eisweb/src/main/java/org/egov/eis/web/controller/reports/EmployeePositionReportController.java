/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.eis.web.controller.reports;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.reports.entity.EmployeePositionResult;
import org.egov.eis.reports.entity.EmployeePositionSearch;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.PositionMasterService;
import org.egov.eis.web.adaptor.EmployeePositionReportAdaptor;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.pims.commons.Position;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/report")
public class EmployeePositionReportController {

    public static final String CONTENTTYPE_JSON = "application/json";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private AssignmentService assignmentService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/employeePositionReport")
    public String searchEmployeePositionForm(final Model model) {
        setDropDownValues(model);
        model.addAttribute("employee", new EmployeePositionSearch());
        return "employeePositionReport-form";
    }

    @RequestMapping(value = "/empPositionList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody void search(final HttpServletRequest request, final HttpServletResponse response,
            final EmployeePositionSearch employeeSearch, final Model model)
            throws IOException {
        final List<EmployeePositionResult> empPosResultList = new ArrayList<>();
        final List<Assignment> assignList = assignmentService.getAssignmentList(employeeSearch);
        for (final Assignment assign : assignList) {
            final EmployeePositionResult empPosition = new EmployeePositionResult();
            empPosition.setCode(assign.getEmployee().getCode());
            empPosition.setName(assign.getEmployee().getName());
            empPosition.setDepartment(assign.getDepartment());
            empPosition.setDesignation(assign.getDesignation());
            empPosition.setPosition(assign.getPosition());
            empPosition.setIsPrimary(assign.getPrimary());
            empPosition.setFromDate(assign.getFromDate());
            empPosition.setToDate(assign.getToDate());

            empPosResultList.add(empPosition);
        }

        final StringBuilder employeePositionJSONData = new StringBuilder("{\"data\":")
                .append(toJSON(empPosResultList, EmployeePositionResult.class, EmployeePositionReportAdaptor.class))
                .append("}");
        response.setContentType(CONTENTTYPE_JSON);
        IOUtils.write(employeePositionJSONData, response.getWriter());
    }

    private void setDropDownValues(final Model model) {
        model.addAttribute("department", departmentService.getAllDepartments());
        model.addAttribute("desigList", designationService.getAllDesignations());
        model.addAttribute("position", positionMasterService.getAllPositions());
    }

    @RequestMapping(value = "/positions", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Position> getPositionByDepartmentAndDesignation(@RequestParam final Long deptId,
            @RequestParam final Long desigId) {
        if (deptId != null && desigId != null)
            return positionMasterService.getPositionsByDepartmentAndDesignation(deptId, desigId);
        else if (deptId != null)
            return positionMasterService.getPositionsByDepartment(deptId);
        else if (desigId != null)
            return positionMasterService.getPositionsByDesignation(desigId);
        else
            return positionMasterService.getAllPositions();
    }

}