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
package org.egov.eis.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.entity.HeadOfDepartments;
import org.egov.eis.entity.enums.EmployeeStatus;
import org.egov.eis.repository.EmployeeRepository;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public EmployeeService(final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public void create(final Employee employee) {
        employee.setPwdExpiryDate(new DateTime().plus(90).toDate());
        // Following is added to prevent null values and empty assignment objects getting persisted
        employee.setAssignments(employee.getAssignments().parallelStream()
                .filter(assignment -> assignment.getPosition() != null).collect(Collectors.toList()));
        for (final Assignment assign : employee.getAssignments()) {
            assign.setEmployee(employee);
            for(HeadOfDepartments hod:assign.getDeptSet()){
                hod.setAssignment(assign);
            }
        }
        employeeRepository.save(employee);
    }

    @Transactional
    public void update(final Employee employee) {
        //Following is added to prevent null values and empty assignment objects getting persisted
        employee.setAssignments(employee.getAssignments().parallelStream()
                .filter(assignment -> assignment.getPosition() != null).collect(Collectors.toList()));
        for (final Assignment assign : employee.getAssignments()){
            assign.setEmployee(employee);
            for(HeadOfDepartments hod:assign.getDeptSet()){
                hod.setAssignment(assign);
            }
        }
        //employee.getAssignments().retainAll(employee.getAssignments());
        
        employeeRepository.saveAndFlush(employee);
    }
    
    @Transactional
    public List<Employee> searchEmployee(final String searchText) {
       
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);
            // create native Lucene query unsing the query DSL
            // alternatively you can write the Lucene query using the Lucene query parser
            // or the Lucene programmatic API. The Hibernate Search DSL is recommended though
            QueryBuilder qb = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Employee.class).get();
            org.apache.lucene.search.Query luceneQuery = qb
              .keyword()
              .onFields("name","code","mobileNumber","aadhaarNumber","emailId","pan",
                      "assignments.department.name", "assignments.designation.name","assignments.position.name",
                      "assignments.fund.name","assignments.function.name","assignments.functionary.name")
              .matching(searchText)
              .createQuery();

            // wrap Lucene query in a javax.persistence.Query
            javax.persistence.Query jpaQuery =
                fullTextEntityManager.createFullTextQuery(luceneQuery, Employee.class);

            // execute search
            return jpaQuery.getResultList();
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

}
