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
package org.egov.eis.web.controller.masters.employee;

import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.dao.AccountdetailtypeHibernateDAO;
import org.egov.commons.service.AccountDetailKeyService;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.entity.EmployeeType;
import org.egov.eis.entity.Jurisdiction;
import org.egov.eis.entity.enums.EmployeeStatus;
import org.egov.eis.repository.EmployeeTypeRepository;
import org.egov.eis.service.DeptDesigService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.EmployeeService;
import org.egov.eis.service.PositionMasterService;
import org.egov.eis.utils.constants.EisConstants;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.pims.commons.DeptDesig;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
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

import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Controller
@RequestMapping(value = "/employeeMaster")
public class CreateEmployeeDataEntryController {
    private static final Logger LOGGER = Logger.getLogger(CreateEmployeeDataEntryController.class);

    public static final SimpleDateFormat DDMMYYYYFORMATS = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private AccountdetailtypeHibernateDAO accountdetailtypeHibernateDAO;

    @Autowired
    private EmployeeTypeRepository employeeTypeRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private BoundaryTypeService boundaryTypeService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private DeptDesigService deptDesigService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AccountDetailKeyService accountDetailKeyService;

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(final Model model) {
        model.addAttribute("employee", new Employee());
        setDropDownValues(model);
        model.addAttribute("mode", "create");
        return "employee-form-simple";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String createEmployee(@Valid @ModelAttribute final Employee employee, final BindingResult errors,
            final RedirectAttributes redirectAttrs, @RequestParam final MultipartFile file,
            @RequestParam final String designationName, @RequestParam final Long deptId, final Model model) {

        final Boolean codeExists = employeeService.validateEmployeeCode(employee);
        if (codeExists)
            errors.rejectValue("code", "Unique.employee.code");

        if (errors.hasErrors()) {
            setDropDownValues(model);
            model.addAttribute("mode", "create");
            return "employee-form-simple";
        }
        final Department department = departmentService.getDepartmentById(deptId);
        final Designation designation = designationService.getDesignationByName(designationName);
        final EmployeeType empType = employeeTypeRepository.findByName(EisConstants.EMPLOYEE_TYPE_PERMANENT);
        final EmployeeStatus empStatus = EmployeeStatus.EMPLOYED;
        try {
            employee.setEmployeeStatus(empStatus);
            employee.setEmployeeType(empType);
            employee.setActive(EisConstants.ISACTIVE_TRUE);
            employee.setSignature(file.getBytes());
        } catch (final IOException e) {
            LOGGER.error("Error in loading Employee Signature" + e.getMessage(), e);
        }
        DeptDesig departmentDesignation = deptDesigService.findByDepartmentAndDesignation(department.getId(),
                designation.getId());
        final Position position = new Position();

        if (departmentDesignation != null) {
            departmentDesignation.setSanctionedPosts(departmentDesignation.getSanctionedPosts() != null
                    ? departmentDesignation.getSanctionedPosts() + 1 : 1);
            position.setDeptDesig(departmentDesignation);
        } else {
            departmentDesignation = new DeptDesig();
            departmentDesignation.setDepartment(department);
            departmentDesignation.setDesignation(designation);
            departmentDesignation.setSanctionedPosts(Integer.valueOf(1));
            departmentDesignation.setOutsourcedPosts(Integer.valueOf(0));
            position.setDeptDesig(departmentDesignation);
        }

        final String positionName = positionMasterService.generatePositionByDeptDesig(department, designation);
        position.setName(positionName);
        positionMasterService.createPosition(position);

        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = DDMMYYYYFORMATS.parse(EisConstants.FROM_DATE);
            toDate = DDMMYYYYFORMATS.parse(EisConstants.TO_DATE);
        } catch (final ParseException e) {
            LOGGER.error("Error in getting fromDate and toDate" + e.getMessage(), e);
        }

        List<User> user = new ArrayList<User>();

        final Set<Role> roles = designationService.getRolesByDesignation(designation.getName());
        for (final Role role : roles) {
            user = userService.getUsersByUsernameAndRolename(employee.getUsername(),
                    roleService.getRoleByName(role.getName()).getName());
            if (fromDate.before(new Date()) && toDate.after(new Date()))
                if (user.isEmpty() || null == user)
                    employee.addRole(roleService.getRoleByName(role.getName()));
        }

        final List<Assignment> assignment = new ArrayList<Assignment>();
        final Assignment assign = new Assignment();
        assign.setDepartment(department);
        assign.setDesignation(designation);
        assign.setEmployee(employee);
        assign.setFromDate(fromDate);
        assign.setToDate(toDate);
        assign.setPrimary(EisConstants.IS_PRIMARY_TRUE);
        assign.setPosition(position);
        assignment.add(assign);

        final List<Jurisdiction> jurisdictions = new ArrayList<Jurisdiction>();
        final Jurisdiction jurisdiction = new Jurisdiction();
        jurisdiction.setEmployee(employee);
        jurisdiction.setBoundaryType(boundaryTypeService.getBoundaryTypeByNameAndHierarchyTypeName(
                EisConstants.BOUNDARY_TYPE_CITY, EisConstants.HIERARCHY_TYPE_ADMIN));
        jurisdiction.setBoundary(boundaryService.getBoundaryByBndryTypeNameAndHierarchyTypeName(
                EisConstants.BOUNDARY_TYPE_CITY, EisConstants.HIERARCHY_TYPE_ADMIN));
        jurisdictions.add(jurisdiction);

        employee.setAssignments(assignment);
        employee.setJurisdictions(jurisdictions);
        employeeService.createEmployeeData(employee);

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
        model.addAttribute("department", departmentService.getAllDepartments());
    }

}
