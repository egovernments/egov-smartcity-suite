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
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.properties.ApplicationProperties;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.service.StateHistoryService;
import org.egov.infra.workflow.service.StateService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EmployeeService implements EntityTypeService {

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    private final EmployeeRepository employeeRepository;
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
    public EmployeeService(final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
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
     * since it is mapped to only one AccountDetailType -creditor it ignores the
     * input parameter
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
        employee.setAssignments(employee.getAssignments().parallelStream()
                .filter(assignment -> assignment.getPosition() != null).collect(Collectors.toList()));
        List<User> user = new ArrayList<User>();

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

        final Criteria criteria = getCurrentSession().createCriteria(Assignment.class, "assignment")
                .createAlias("assignment.employee", "employee");
        if (null != searchParams.getCode() && !searchParams.getCode().isEmpty())
            criteria.add(Restrictions.eq("employee.code", searchParams.getCode()));
        if (null != searchParams.getName() && !searchParams.getName().isEmpty())
            criteria.add(Restrictions.eq("employee.name", searchParams.getName()));
        if (null != searchParams.getAadhaar() && !searchParams.getAadhaar().isEmpty())
            criteria.add(Restrictions.eq("employee.aadhaar", searchParams.getAadhaar()));
        if (null != searchParams.getMobileNumber() && !searchParams.getMobileNumber().isEmpty())
            criteria.add(Restrictions.eq("employee.mobileNumber", searchParams.getMobileNumber()));
        if (null != searchParams.getPan() && !searchParams.getPan().isEmpty())
            criteria.add(Restrictions.eq("employee.pan", searchParams.getPan()));
        if (null != searchParams.getEmail() && !searchParams.getEmail().isEmpty())
            criteria.add(Restrictions.eq("employee.emailId", searchParams.getEmail()));
        if (null != searchParams.getStatus() && !searchParams.getStatus().isEmpty())
            criteria.add(Restrictions.eq("employee.employeeStatus", EmployeeStatus.valueOf(searchParams.getStatus())));
        if (null != searchParams.getEmployeeType() && !searchParams.getEmployeeType().isEmpty()) {
            criteria.createAlias("employee.employeeType", "employeeType");
            criteria.add(Restrictions.eq("employeeType.name", searchParams.getEmployeeType()));
        }
        if (null != searchParams.getDepartment() && !searchParams.getDepartment().isEmpty()) {
            criteria.createAlias("assignment.department", "department");
            criteria.add(Restrictions.eq("department.name", searchParams.getDepartment()));
        }
        if (null != searchParams.getDesignation() && !searchParams.getDesignation().isEmpty()) {
            criteria.createAlias("assignment.designation", "designation");
            criteria.add(Restrictions.eq("designation.name", searchParams.getDesignation()));
        }
        if (null != searchParams.getFunctionary() && !searchParams.getFunctionary().isEmpty()) {
            criteria.createAlias("assignment.functionary", "functionary");
            criteria.add(Restrictions.eq("functionary.name", searchParams.getFunctionary()));
        }
        if (null != searchParams.getFunction() && !searchParams.getFunction().isEmpty()) {
            criteria.createAlias("assignment.function", "function");
            criteria.add(Restrictions.eq("function.name", searchParams.getFunction()));
        }

        final ProjectionList projections = Projections.projectionList()
                .add(Projections.property("assignment.employee"));
        criteria.setProjection(projections);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        return criteria.list();

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

    public String validatePosition(final Employee employee, final String removedassignIds) {
        boolean positionExistsInWF = false;
        boolean positionExistsInWFHistory = false;
        final List<Position> updatedPositionList = positionMasterService.getPositionsForEmployee(employee.getId());
        if (StringUtils.isNotBlank(removedassignIds)) {
            final String[] deletedAssignIds = removedassignIds.split(",");
            for (final String assignId : deletedAssignIds) {
                final Assignment assignment = assignmentService.getAssignmentById(Long.valueOf(assignId));
                if (assignment != null && !assignment.equals("")) {
                    positionExistsInWF = stateService.isPositionUnderWorkflow(assignment.getPosition().getId());
                    positionExistsInWFHistory = stateHistoryService
                            .isPositionUnderWorkflowHistory(assignment.getPosition().getId());
                }
                if (positionExistsInWF || positionExistsInWFHistory)
                    return assignment.getPosition().getName();
            }
        }
        assignmentService.removeDeletedAssignments(employee, removedassignIds);
        getCurrentSession().evict(employee);
        final Employee updatedEmployee = getEmployeeById(employee.getId());
        final List<Position> oldPositionList = positionMasterService.getPositionsForEmployee(updatedEmployee.getId());
        oldPositionList.removeAll(updatedPositionList);
        for (final Position position : oldPositionList) {
            positionExistsInWF = stateService.isPositionUnderWorkflow(position.getId());
            positionExistsInWFHistory = stateHistoryService.isPositionUnderWorkflowHistory(position.getId());
            if (positionExistsInWF || positionExistsInWFHistory)
                return position.getName();
        }
        return StringUtils.EMPTY;
    }

}
