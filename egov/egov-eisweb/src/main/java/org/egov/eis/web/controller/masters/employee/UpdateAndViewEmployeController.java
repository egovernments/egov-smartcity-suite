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

import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.egov.eis.entity.Employee;
import org.egov.eis.entity.enums.EmployeeStatus;
import org.egov.eis.repository.EmployeeTypeRepository;
import org.egov.eis.service.EmployeeService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value="/employee")
public class UpdateAndViewEmployeController {

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeTypeRepository employeeTypeRepository;

    @Autowired
    private EmployeeService employeeService;

    @ModelAttribute
    public Employee employeeModel(@PathVariable final String code) {
        return employeeService.getEmployeeByCode(code);
    }

    @RequestMapping(value = "/update/{code}", method = RequestMethod.GET)
    public String updateForm(final Model model,@PathVariable final String code) {
        model.addAttribute("employeeStatus", Arrays.asList(EmployeeStatus.values()));
        model.addAttribute("department", departmentService.getAllDepartments());
        model.addAttribute("employeeTypes", employeeTypeRepository.findAll());
        model.addAttribute("fundList",
                getCurrentSession().createQuery("from Fund where isactive = 1 and isNotLeaf!=1 order by upper(name)")
                .list());
        model.addAttribute("functionaryList",
                getCurrentSession().createQuery("from Functionary where isactive=1 order by upper(name)").list());
        model.addAttribute(
                "functionList",
                getCurrentSession().createQuery(
                        "from CFunction where isactive = 1 AND isnotleaf=0 order by upper(name)").list());
        model.addAttribute("gradeList", getCurrentSession().createQuery("from GradeMaster order by name").list());
        model.addAttribute("mode", "update");
        return "employee-form";
    }

    @RequestMapping(value = "/update/{code}",method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final Employee employee, final BindingResult errors,
            final RedirectAttributes redirectAttrs, final Model model) {
        if (errors.hasErrors()){
            model.addAttribute("employeeStatus", Arrays.asList(EmployeeStatus.values()));
            model.addAttribute("department", departmentService.getAllDepartments());
            model.addAttribute("employeeTypes", employeeTypeRepository.findAll());
            model.addAttribute("fundList",
                    getCurrentSession().createQuery("from Fund where isactive = 1 and isNotLeaf!=1 order by upper(name)")
                    .list());
            model.addAttribute("functionaryList",
                    getCurrentSession().createQuery("from Functionary where isactive=1 order by upper(name)").list());
            model.addAttribute(
                    "functionList",
                    getCurrentSession().createQuery(
                            "from CFunction where isactive = 1 AND isnotleaf=0 order by upper(name)").list());
            model.addAttribute("gradeList", getCurrentSession().createQuery("from GradeMaster order by name").list());
            model.addAttribute("mode", "update");
            return "employee-form";
        }
        employeeService.update(employee);
        redirectAttrs.addFlashAttribute("employee", employee);
        model.addAttribute("message", "Employee updated successfully");
        return "employee-success";
    }

    @RequestMapping(value = "/view/{code}", method = RequestMethod.GET)
    public String view(@PathVariable final String code,final Model model) {
        return "employee-success";
    }

}
