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

import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.dao.AccountdetailtypeHibernateDAO;
import org.egov.commons.service.AccountDetailKeyService;
import org.egov.eis.entity.Employee;
import org.egov.eis.entity.enums.EmployeeStatus;
import org.egov.eis.repository.EmployeeTypeRepository;
import org.egov.eis.service.EmployeeService;
import org.egov.eis.utils.constants.EisConstants;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.postgresql.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/employee")
public class CreateEmployeeController {
    private static final Logger LOGGER = Logger.getLogger(ViewAndUpdateEmployeController.class);

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeTypeRepository employeeTypeRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private BoundaryTypeService boundaryTypeService;

    @Autowired
    private AccountdetailtypeHibernateDAO accountdetailtypeHibernateDAO;
    @Autowired
    private AccountDetailKeyService accountDetailKeyService;

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(final Model model) {
        model.addAttribute("employee", new Employee());
        setDropDownValues(model);
        model.addAttribute("mode", "create");
        return "employee-form";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String createEmployee(@Valid @ModelAttribute final Employee employee, final BindingResult errors,
            final RedirectAttributes redirectAttrs, @RequestParam final MultipartFile file, final Model model) {

        final Boolean codeExists = employeeService.validateEmployeeCode(employee);
        if (codeExists)
            errors.rejectValue("code", "Unique.employee.code");

        if (!employeeService.primaryAssignmentExists(employee) && employee.isActive())
            errors.rejectValue("assignments", "primary.assignment");

        if (errors.hasErrors()) {
            setDropDownValues(model);
            model.addAttribute("mode", "create");
            return "employee-form";
        }

        try {
            employee.setSignature(file.getBytes());
        } catch (final IOException e) {
            LOGGER.error("Error in loading Employee Signature" + e.getMessage(), e);
        }
        employeeService.create(employee);

        final Accountdetailtype accountdetailtype = accountdetailtypeHibernateDAO
                .getAccountdetailtypeByName(EisConstants.ROLE_EMPLOYEE);
        final Accountdetailkey adk = new Accountdetailkey();
        adk.setAccountdetailtype(accountdetailtype);
        adk.setGroupid(1);
        adk.setDetailkey(employee.getId().intValue());
        adk.setDetailname(accountdetailtype.getAttributename());
        accountDetailKeyService.createAccountDetailKey(adk);

        String image = null;
        if (null != employee.getSignature())
            image = Base64.encodeBytes(employee.getSignature());
        model.addAttribute("image", image);
        redirectAttrs.addFlashAttribute("employee", employee);
        model.addAttribute("message", "Employee created successfully");
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
