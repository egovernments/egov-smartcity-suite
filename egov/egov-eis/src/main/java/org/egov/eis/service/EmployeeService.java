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
package org.egov.eis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.service.EntityTypeService;
import org.egov.commons.utils.EntityType;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.entity.EmployeeSearchDTO;
import org.egov.eis.entity.HeadOfDepartments;
import org.egov.eis.entity.Jurisdiction;
import org.egov.eis.entity.enums.EmployeeStatus;
import org.egov.eis.repository.AssignmentRepository;
import org.egov.eis.repository.EmployeeRepository;
import org.egov.eis.utils.constants.EisConstants;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.repository.UserRepository;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.properties.ApplicationProperties;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.service.StateHistoryService;
import org.egov.infra.workflow.service.StateService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EmployeeService implements EntityTypeService {

    private final EmployeeRepository employeeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private UserService userService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private StateService stateService;

    @Autowired
    private StateHistoryService stateHistoryService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    public EmployeeService(final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @SuppressWarnings("unchecked")
    public List<CFunction> getAllFunctions() {
        return getCurrentSession()
                .createQuery("from CFunction where isactive = true AND isnotleaf=false order by upper(name)").list();
    }

    @SuppressWarnings("unchecked")
    public List<Functionary> getAllFunctionaries() {
        return getCurrentSession().createQuery("from Functionary where isactive=true order by upper(name)").list();
    }

    @SuppressWarnings("unchecked")
    public List<Fund> getAllFunds() {
        return getCurrentSession()
                .createQuery("from Fund where isactive = true and isNotLeaf!=true order by upper(name)").list();
    }

    @SuppressWarnings("unchecked")
    public List<Fund> getAllGrades() {
        return getCurrentSession().createQuery("from GradeMaster order by name").list();
    }

    /**
     * since it is mapped to only one AccountDetailType -creditor it ignores the input parameter
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<EntityType> getAllActiveEntities(final Integer employeeId) {
        final List<EntityType> entities = new ArrayList<EntityType>();
        final List<Employee> employees = getAllEmployees();
        entities.addAll(employees);
        return entities;
    }

    @Transactional
    public void create(final Employee employee) {
        employee.updateNextPwdExpiryDate(applicationProperties.userPasswordExpiryInDays());

        employee.setPassword(passwordEncoder.encode(EisConstants.DEFAULT_EMPLOYEE_PWD));

        List<User> user = new ArrayList<User>();
        // Following is added to prevent null values and empty assignment
        // objects getting persisted
        employee.setAssignments(employee.getAssignments().parallelStream()
                .filter(assignment -> assignment.getPosition() != null).collect(Collectors.toList()));
        for (final Assignment assign : employee.getAssignments()) {
            assign.setEmployee(employee);
            assign.setDepartment(assign.getDepartment());

            final Set<Role> roles = designationService.getRolesByDesignation(assign.getDesignation().getName());
            for (final Role role : roles) {
                user = userService.getUsersByUsernameAndRolename(employee.getUsername(),
                        roleService.getRoleByName(role.getName()).getName());
                if (assign.getFromDate().before(new Date()) && assign.getToDate().after(new Date()))
                    if (user.isEmpty() || user == null)
                        employee.addRole(roleService.getRoleByName(role.getName()));
            }
            for (final HeadOfDepartments hod : assign.getDeptSet())
                hod.setAssignment(assign);
        }
        employee.setJurisdictions(employee.getJurisdictions().parallelStream()
                .filter(Jurisdictions -> Jurisdictions.getBoundaryType() != null && Jurisdictions.getBoundary() != null)
                .collect(Collectors.toList()));
        for (final Jurisdiction jurisdiction : employee.getJurisdictions()) {
            jurisdiction.setEmployee(employee);
            jurisdiction.setBoundaryType(jurisdiction.getBoundaryType());
            jurisdiction.setBoundary(jurisdiction.getBoundary());
        }
        employee.getRoles().add(roleService.getRoleByName(EisConstants.ROLE_EMPLOYEE));

        employeeRepository.save(employee);
    }

    @Transactional
    public void createEmployeeData(final Employee employee) {
        employee.updateNextPwdExpiryDate(applicationProperties.userPasswordExpiryInDays());
        employee.setPassword(passwordEncoder.encode(EisConstants.DEFAULT_EMPLOYEE_PWD));
        employee.getRoles().add(roleService.getRoleByName(EisConstants.ROLE_EMPLOYEE));
        employeeRepository.save(employee);
    }

    @Transactional
    public void update(final Employee employee) {
        // Following is added to prevent null values and empty assignment
        // objects getting persisted

        for (final Assignment assign : employee.getAssignments()) {

            assign.getDeptSet().clear();
            for (final HeadOfDepartments hod : assign.getHodList()) {
                hod.setAssignment(assign);
                hod.setHod(departmentService.getDepartmentById(hod.getHod().getId()));
                assign.getDeptSet().add(hod);
            }
            assign.setEmployee(employee);
            assign.setDepartment(assign.getDepartment());
        }
        for (final Jurisdiction jurisdiction : employee.getJurisdictions()) {
            jurisdiction.setEmployee(employee);
            jurisdiction.setBoundaryType(jurisdiction.getBoundaryType());
            jurisdiction.setBoundary(jurisdiction.getBoundary());
        }
        employeeRepository.saveAndFlush(employee);
    }

    @Transactional
    public void updateEmployeeDetails(final Employee employee) {
        employeeRepository.saveAndFlush(employee);
    }

    @Transactional
    public void addOrRemoveUserRole() {
        final List<Employee> employee = getAllEmployees();

        for (final Employee emp : employee) {
            final Set<Role> userRole = userService.getRolesByUsername(emp.getUsername());

            // List Of Expired Roles
            final Set<Role> expiredRoleList = assignmentService.getRolesForExpiredAssignmentsByEmpId(emp.getId());
            // List Of Active Roles
            final Set<Role> activeRoleList = assignmentService.getRolesForActiveAssignmentsByEmpId(emp.getId());

            // Remove activeRoles from ExpiredRoles List
            expiredRoleList.removeAll(activeRoleList);

            // Remove Expired Roles
            userRole.removeAll(expiredRoleList);

            // Add Roles
            userRole.addAll(activeRoleList);

            emp.setRoles(userRole);
            employeeRepository.save(emp);
        }

    }

    public List<Employee> searchEmployees(final EmployeeSearchDTO searchParams) {

        final StringBuilder queryString = new StringBuilder();
        queryString.append("select distinct(assign.employee) from Assignment assign where assign.id is not null ");
        if (StringUtils.isNotBlank(searchParams.getCode()))
            queryString.append(" AND assign.employee.code =:code ");
        if (StringUtils.isNotBlank(searchParams.getName()))
            queryString.append(" AND assign.employee.name like :name ");
        if (StringUtils.isNotBlank(searchParams.getAadhaar()))
            queryString.append(" AND assign.employee.aadhaar = :aadhaar");
        if (StringUtils.isNotBlank(searchParams.getMobileNumber()))
            queryString.append(" AND assign.employee.mobileNumber = :mobileNumber");
        if (StringUtils.isNotBlank(searchParams.getPan()))
            queryString.append(" AND assign.employee.pan = :pan");
        if (StringUtils.isNotBlank(searchParams.getEmail()))
            queryString.append(" AND assign.employee.emailId = :email");
        if (StringUtils.isNotBlank(searchParams.getStatus()))
            queryString.append(" AND assign.employee.employeeStatus = :status");
        if (StringUtils.isNotBlank(searchParams.getEmployeeType()))
            queryString.append(" AND assign.employee.employeeType.name = :type");
        if (searchParams.getDepartment() != null)
            queryString.append(" AND assign.department.name =:department ");
        if (searchParams.getDesignation() != null)
            queryString.append(" AND assign.designation.name =:designation ");
        if (searchParams.getFunctionary() != null)
            queryString.append(" AND assign.functionary.name =:functionary ");
        if (searchParams.getFunction() != null)
            queryString.append(" AND assign.function.name =:function ");
        if (searchParams.getIsHOD())
            queryString.append(" and assign.id  in (select assignment.id from HeadOfDepartments )");
        queryString.append(" Order by assign.employee.code, assign.employee.name ");
        Query query = entityManager.unwrap(Session.class).createQuery(queryString.toString());
        query = setParametersToQuery(searchParams, query);
        final List<Employee> employees = query.list();
        return employees;
    }

    public Query setParametersToQuery(final EmployeeSearchDTO searchParams, final Query query) {
        if (StringUtils.isNotBlank(searchParams.getCode()))
            query.setParameter("code", searchParams.getCode());
        if (StringUtils.isNotBlank(searchParams.getName()))
            query.setParameter("name", searchParams.getName());
        if (StringUtils.isNotBlank(searchParams.getAadhaar()))
            query.setParameter("aadhaar", searchParams.getAadhaar());
        if (StringUtils.isNotBlank(searchParams.getMobileNumber()))
            query.setParameter("mobileNumber", searchParams.getMobileNumber());
        if (StringUtils.isNotBlank(searchParams.getPan()))
            query.setParameter("pan", searchParams.getPan());
        if (StringUtils.isNotBlank(searchParams.getEmail()))
            query.setParameter("email", searchParams.getEmail());
        if (StringUtils.isNotBlank(searchParams.getStatus()))
            query.setParameter("status", searchParams.getStatus());
        if (StringUtils.isNotBlank(searchParams.getEmployeeType()))
            query.setParameter("aadhaar", searchParams.getEmployeeType());
        return setAssignmentParameter(searchParams, query);
    }

    public Query setAssignmentParameter(final EmployeeSearchDTO searchParams, final Query assignQuery) {
        if (searchParams.getDepartment() != null)
            assignQuery.setParameter("department", searchParams.getDepartment());
        if (searchParams.getDesignation() != null)
            assignQuery.setParameter("designation", searchParams.getDesignation());
        if (searchParams.getFunctionary() != null)
            assignQuery.setParameter("functionary", searchParams.getFunctionary());
        if (searchParams.getFunction() != null)
            assignQuery.setParameter("function", searchParams.getFunction());
        return assignQuery;
    }

    @Transactional
    public void delete(final Employee employee) {
        employeeRepository.delete(employee);
    }

    /**
     * Get employee object by id
     *
     * @param id
     * @return Employee Object
     */
    public Employee getEmployeeById(final Long id) {
        return employeeRepository.findOne(id);
    }

    /**
     * Get employee object by code
     *
     * @param code
     * @return Employee Object
     */
    public Employee getEmployeeByCode(final String code) {
        return employeeRepository.findByCode(code);
    }

    /**
     * Get list of employee objects by employee status
     *
     * @param status
     * @return List of employee objects
     */
    public List<Employee> getEmployeesByStatus(final EmployeeStatus status) {
        return employeeRepository.findByEmployeeStatus(status);
    }

    /**
     * Get list of employee objects by employee type
     *
     * @param id
     * @return List of employee objects
     */
    public List<Employee> getEmployeesByType(final Long id) {
        return employeeRepository.findByEmployeeType_Id(id);
    }

    /**
     * Get employee by user name
     *
     * @param userName
     * @return Employee Object
     */
    public Employee getEmployeeByUserName(final String userName) {
        return employeeRepository.findByUsername(userName);
    }

    /**
     * Get List of employee objects by department ,designation and boundary ids
     *
     * @param deptId
     * @param desigId
     * @param boundaryId
     * @return List of employee objects
     */
    public List<Employee> findByDepartmentDesignationAndBoundary(final Long deptId, final Long desigId,
            final Long boundaryId) {
        final Set<Long> bndIds = new HashSet<Long>();
        final List<Boundary> boundaries = boundaryService.findActiveChildrenWithParent(boundaryId);
        boundaries.forEach((bndry) -> bndIds.add(bndry.getId()));
        return employeeRepository.findByDepartmentDesignationAndBoundary(deptId, desigId, bndIds);
    }

    /**
     * Returns list of employee for a given position
     *
     * @param posId
     * @return List of PersonalInformation
     */
    public List<Employee> getEmployeesForPosition(final Long posId) {
        final Set<Employee> employees = new HashSet<Employee>();
        final List<Assignment> assignList = assignmentRepository.getAssignmentsForPosition(posId);
        for (final Assignment assign : assignList)
            employees.add(assign.getEmployee());
        return new ArrayList<Employee>(employees);
    }

    /**
     * Returns primary assignment's employee for position
     *
     * @param posId
     * @return Employee object
     */
    public Employee getPrimaryAssignmentEmployeeForPos(final Long posId) {
        return assignmentRepository.getPrimaryAssignmentForPosition(posId).getEmployee();
    }

    /**
     * Returns employee object for position id and given date
     *
     * @param posId
     * @param givenDate
     * @return Employee object
     */
    public Employee getEmployeeForPositionAndDate(final Long posId, final Date givenDate) {
        return assignmentRepository.getPrimaryAssignmentForPositionAndDate(posId, givenDate).getEmployee();
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public List<? extends EntityType> filterActiveEntities(final String filterKey, final int maxRecords,
            final Integer accountDetailTypeId) {
        return employeeRepository.findByNameLikeOrCodeLike(filterKey + "%", filterKey + "%");
    }

    @Override
    public List getAssetCodesForProjectCode(final Integer accountdetailkey) throws ValidationException {
        return null;
    }

    @Override
    public List<? extends EntityType> validateEntityForRTGS(final List<Long> idsList) throws ValidationException {
        return null;
    }

    @Override
    public List<? extends EntityType> getEntitiesById(final List<Long> idsList) throws ValidationException {
        return null;
    }

    public String generateUserNameByDeptDesig(final Department department, final Designation designation) {
        String name = department.getCode() + designation.getCode();
        name += userRepository.getUserSerialNumberByName(name) + 1;
        return name;
    }

    public List<Employee> findEmployeeByCodeLike(final String code) {
        return employeeRepository.findEmployeeByCodeLike(code);
    }

    public List<Employee> findActiveEmployeeByCodeLike(final String code) {
        return employeeRepository.findActiveEmployeeByCodeLike(code);
    }

    public String validatePosition(final Employee employee, final String removedassignIds) {
        boolean positionExistsInWF = false;
        boolean positionExistsInWFHistory = false;
        final List<Position> updatedPositionList = positionMasterService.getPositionsForEmployee(employee.getId());

        if (StringUtils.isNotBlank(removedassignIds)) {
            final String[] deletedAssignIds = removedassignIds.split(",");
            for (final String assignId : deletedAssignIds) {
                final Assignment assignment = assignmentService.getAssignmentById(Long.valueOf(assignId));
                if (assignment != null && !assignment.equals("")) {
                    positionExistsInWF = stateService.isPositionUnderWorkflow(assignment.getPosition().getId(),
                            assignment.getFromDate());
                    positionExistsInWFHistory = stateHistoryService
                            .isPositionUnderWorkflowHistory(assignment.getPosition().getId(), assignment.getFromDate());
                }
                if (positionExistsInWF || positionExistsInWFHistory)
                    return assignment.getPosition().getName();
            }
        }
        assignmentService.removeDeletedAssignments(employee, removedassignIds);
        employee.setAssignments(employee.getAssignments().parallelStream()
                .filter(assignment -> assignment.getPosition() != null).collect(Collectors.toList()));

        employee.setJurisdictions(employee.getJurisdictions().parallelStream()
                .filter(Jurisdictions -> Jurisdictions.getBoundaryType() != null && Jurisdictions.getBoundary() != null)
                .collect(Collectors.toList()));

        getCurrentSession().evict(employee);
        final Employee updatedEmployee = getEmployeeById(employee.getId());
        final List<Assignment> oldAssignmentList = assignmentService.getAllAssignmentsByEmpId(updatedEmployee.getId());
        final List<Position> oldPositionList = positionMasterService.getPositionsForEmployee(updatedEmployee.getId());
        oldPositionList.removeAll(updatedPositionList);
        for (final Assignment assign : oldAssignmentList)
            if (oldPositionList.contains(assign.getPosition())) {
                positionExistsInWF = stateService.isPositionUnderWorkflow(assign.getPosition().getId(), assign.getFromDate());
                positionExistsInWFHistory = stateHistoryService.isPositionUnderWorkflowHistory(assign.getPosition().getId(),
                        assign.getFromDate());
                if (positionExistsInWF || positionExistsInWFHistory)
                    return assign.getPosition().getName();
            }
        return StringUtils.EMPTY;
    }

    public Boolean validateEmployeeCode(final Employee employee) {
        final String employeeCode = employee.getCode().replaceFirst("^0+(?!$)", "");

        final List<Employee> employeeList = findActiveEmployeeByCodeLike(employeeCode);

        if (!employeeList.isEmpty())
            for (final Employee emp : employeeList) {
                final String empCode = emp.getCode().replaceFirst("^0+(?!$)", "");
                if (!emp.getCode().equals(employee.getCode()) && employeeCode.equals(empCode)
                        && !emp.getId().equals(employee.getId()))
                    return true;

            }
        return false;
    }

    public Boolean primaryAssignmentExists(final Employee employee) {
        for (final Assignment assignment : employee.getAssignments())
            if (assignment.getPrimary())
                return true;
        return false;
    }

}
