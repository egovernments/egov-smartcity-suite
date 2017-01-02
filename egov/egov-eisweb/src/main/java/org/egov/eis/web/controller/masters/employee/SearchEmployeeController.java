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

import org.apache.commons.io.IOUtils;
import org.egov.eis.entity.Employee;
import org.egov.eis.entity.EmployeeAdaptor;
import org.egov.eis.entity.EmployeeSearchDTO;
import org.egov.eis.entity.enums.EmployeeStatus;
import org.egov.eis.repository.EmployeeTypeRepository;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.EmployeeService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.egov.infra.utils.JsonUtils.toJSON;

@Controller
@RequestMapping(value = "/employee")
public class SearchEmployeeController {

    public static final String CONTENTTYPE_JSON = "application/json";

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeTypeRepository employeeTypeRepository;

    @Autowired
    private DesignationService designationService;

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public String searchForm(final Model model) {
        setDropDownValues(model);
        model.addAttribute("employee", new EmployeeSearchDTO());
        return "employeesearch-form";
    }

    @RequestMapping(value = "ajax/employees", method = RequestMethod.GET)
    public @ResponseBody void springPaginationDataTables(final HttpServletRequest request,
            final HttpServletResponse response, final EmployeeSearchDTO employee) throws IOException {
        final List<Employee> employees = employeeService.searchEmployees(employee);
        final StringBuilder employeeJSONData = new StringBuilder("{\"data\":").append(toJSON(employees, Employee.class, EmployeeAdaptor.class)).append("}");
        response.setContentType(CONTENTTYPE_JSON);
        IOUtils.write(employeeJSONData, response.getWriter());
    }

    private void setDropDownValues(final Model model) {
        model.addAttribute("employeeStatus", Arrays.asList(EmployeeStatus.values()));
        model.addAttribute("department", departmentService.getAllDepartments());
        model.addAttribute("employeeTypes", employeeTypeRepository.findAll());
        model.addAttribute("fundList", employeeService.getAllFunds());
        model.addAttribute("functionaryList", employeeService.getAllFunctionaries());
        model.addAttribute("functionList", employeeService.getAllFunctions());
        model.addAttribute("gradeList", employeeService.getAllGrades());
        model.addAttribute("desigList",designationService.getAllDesignations());
    }

}
