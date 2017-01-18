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
package org.egov.eis.web.controller.masters.employee;

import java.io.IOException;
import java.util.Arrays;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.entity.enums.EmployeeStatus;
import org.egov.eis.repository.EmployeeTypeRepository;
import org.egov.eis.repository.HeadOfDepartmentsRepository;
import org.egov.eis.service.EmployeeService;
import org.egov.eis.service.JurisdictionService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.postgresql.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

@Controller
@RequestMapping(value = "/employee")
public class ViewAndUpdateEmployeController {
    private static final String EMPLOYEEDETAILS_FORM = "employeedetails-form";
    private static final String EMPLOYEESUCCESS = "employee-success";
    private static final Logger LOGGER = Logger.getLogger(ViewAndUpdateEmployeController.class);
    private static final String IMAGE = "image";
    private static final String EMPLOYEEFORM = "employee-form";

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeTypeRepository employeeTypeRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private BoundaryTypeService boundaryTypeService;

    @Autowired
    private JurisdictionService jurisdictionService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private HeadOfDepartmentsRepository headOfDepartmentsRepository;

    @ModelAttribute
    public Employee employeeModel(@PathVariable final String code) {
        return employeeService.getEmployeeByCode(code);
    }

    @RequestMapping(value = "/update/{code}", method = RequestMethod.GET)
    public String edit(final Model model, @PathVariable final String code) {

        setDropDownValues(model);
        model.addAttribute("mode", "update");
        final Employee employee = employeeService.getEmployeeByCode(code);
        for (final Assignment assign : employee.getAssignments())
            assign.setDeptSet(headOfDepartmentsRepository.getAllHodDepartments(assign.getId()));
        String image = null;
        if (null != employee.getSignature())
            image = Base64.encodeBytes(employee.getSignature());
        model.addAttribute(IMAGE, image);
        return EMPLOYEEFORM;
    }

    @RequestMapping(value = "/update/{code}", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final Employee employee, final BindingResult errors,
            final RedirectAttributes redirectAttrs, final Model model, @RequestParam final MultipartFile file,
            @RequestParam final String removedJurisdictionIds, @RequestParam final String removedassignIds) {

        final Boolean codeExists = employeeService.validateEmployeeCode(employee);
        if (codeExists)
            errors.rejectValue("code", "Unique.employee.code");

        try {
            if (!file.isEmpty())
                employee.setSignature(file.getBytes());
        } catch (final IOException e) {
            LOGGER.error("Error in loading Employee Signature" + e.getMessage(), e);
        }
        jurisdictionService.removeDeletedJurisdictions(employee, removedJurisdictionIds);
        final String positionName = employeeService.validatePosition(employee, removedassignIds);
        if (StringUtils.isNotBlank(positionName)) {
            setDropDownValues(model);
            final String fieldError = messageSource.getMessage("position.exists.workflow",
                    new String[] { positionName }, null);
            model.addAttribute("error", fieldError);
            return EMPLOYEEFORM;
        }
        if (!employeeService.primaryAssignmentExists(employee) && employee.isActive())
            errors.rejectValue("assignments", "primary.assignment");

        if (errors.hasErrors()) {
            setDropDownValues(model);
            model.addAttribute("mode", "update");
            return EMPLOYEEFORM;
        }

        String image = null;
        if (null != employee.getSignature())
            image = Base64.encodeBytes(employee.getSignature());
        model.addAttribute(IMAGE, image);

        employeeService.update(employee);
        redirectAttrs.addFlashAttribute("employee", employee);
        model.addAttribute("message", "Employee updated successfully");
        return EMPLOYEESUCCESS;
    }

    @RequestMapping(value = "/updatecontact/{code}", method = RequestMethod.GET)
    public String editContact(final Model model, @PathVariable final String code) {
        setDropDownValues(model);
        return EMPLOYEEDETAILS_FORM;
    }

    @RequestMapping(value = "/updatecontact/{code}", method = RequestMethod.POST)
    public String updateContact(@Valid @ModelAttribute final Employee employee, final BindingResult errors,
            final RedirectAttributes redirectAttrs, final Model model) {
        if (errors.hasErrors()) {
            setDropDownValues(model);
            return EMPLOYEEDETAILS_FORM;
        }
        employeeService.updateEmployeeDetails(employee);
        redirectAttrs.addFlashAttribute("employee", employee);
        model.addAttribute("message", "Employee updated successfully");
        return EMPLOYEESUCCESS;
    }

    @RequestMapping(value = "/view/{code}", method = RequestMethod.GET)
    public String view(@PathVariable final String code, final Model model) {
        final Employee employee = employeeService.getEmployeeByCode(code);
        String image = null;
        for (final Assignment assign : employee.getAssignments())
            assign.setDeptSet(headOfDepartmentsRepository.getAllHodDepartments(assign.getId()));
        if (null != employee.getSignature())
            image = Base64.encodeBytes(employee.getSignature());
        model.addAttribute(IMAGE, image);
        return EMPLOYEESUCCESS;
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
