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
package org.egov.eis.web.controller.masters.employee;

import org.apache.log4j.Logger;
import org.egov.eis.entity.Employee;
import org.egov.eis.entity.enums.EmployeeStatus;
import org.egov.eis.repository.EmployeeTypeRepository;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EmployeeService;
import org.egov.eis.service.JurisdictionService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.postgresql.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;

@Controller
@RequestMapping(value = "/employee")
public class ViewAndUpdateEmployeController {
    private static final Logger LOGGER = Logger.getLogger(ViewAndUpdateEmployeController.class);
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeTypeRepository employeeTypeRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private BoundaryTypeService boundaryTypeService;

    @Autowired
    private JurisdictionService jurisdictionService;

    @ModelAttribute
    public Employee employeeModel(@PathVariable final String code) {
        return employeeService.getEmployeeByCode(code);
    }

    @RequestMapping(value = "/update/{code}", method = RequestMethod.GET)
    public String edit(final Model model, @PathVariable final String code) {

        setDropDownValues(model);
        model.addAttribute("mode", "update");
        final Employee employee = employeeService.getEmployeeByCode(code);
        String image = null;
        if (null != employee.getSignature())
            image = Base64.encodeBytes(employee.getSignature());
        model.addAttribute("image", image);
        return "employee-form";
    }

    @RequestMapping(value = "/update/{code}", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute Employee employee, final BindingResult errors,
            final RedirectAttributes redirectAttrs, final Model model, @RequestParam final MultipartFile file,
            @RequestParam final String removedJurisdictionIds, @RequestParam final String removedassignIds) {
        if (errors.hasErrors()) {
            setDropDownValues(model);
            model.addAttribute("mode", "update");
            return "employee-form";
        }
        try {
            if (!file.isEmpty())
                employee.setSignature(file.getBytes());
        } catch (final IOException e) {
            LOGGER.error("Error in loading Employee Signature" + e.getMessage(), e);
        }
        String image = null;
        if (null != employee.getSignature())
            image = Base64.encodeBytes(employee.getSignature());
        model.addAttribute("image", image);
        employee = jurisdictionService.removeDeletedJurisdictions(employee, removedJurisdictionIds);
        employee = assignmentService.removeDeletedAssignments(employee, removedassignIds);
        employeeService.update(employee);
        redirectAttrs.addFlashAttribute("employee", employee);
        model.addAttribute("message", "Employee updated successfully");
        return "employee-success";
    }

    @RequestMapping(value = "/view/{code}", method = RequestMethod.GET)
    public String view(@PathVariable final String code, final Model model) {
        final Employee employee = employeeService.getEmployeeByCode(code);
        String image = null;
        if (null != employee.getSignature())
            image = Base64.encodeBytes(employee.getSignature());
        model.addAttribute("image", image);
        return "employee-success";
    }

    private void setDropDownValues(final Model model) {
        model.addAttribute("employeeStatus", Arrays.asList(EmployeeStatus.values()));
        model.addAttribute("department", departmentService.getAllDepartments());
        model.addAttribute("employeeTypes", employeeTypeRepository.findAll());
        model.addAttribute("fundList", employeeService.getAllFunds());
        model.addAttribute("functionaryList", employeeService.getAllFunctionaries());
        model.addAttribute("functionList", employeeService.getAllFunctions());
        model.addAttribute("gradeList", employeeService.getAllGrades());
        model.addAttribute("boundaryType", boundaryTypeService.getBoundaryTypeByHierarchyTypeName("ADMINISTRATION"));
    }

}
