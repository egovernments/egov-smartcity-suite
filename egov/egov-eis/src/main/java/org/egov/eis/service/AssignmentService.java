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

import org.egov.eis.repository.AssignmentRepository;
import org.egov.pims.model.Assignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service class is used to query all employee related assignments
 * 
 * @author Vaibhav.K
 *
 */
@Service
@Transactional(readOnly = true)
public class AssignmentService {
    
    private final AssignmentRepository assignmentRepository;
    
    @Autowired
    public AssignmentService(final AssignmentRepository assignmentRepository) {
        this.assignmentRepository=assignmentRepository;
    }
    
    public Assignment getAssignmentById(final Long Id) {
        return assignmentRepository.findOne(Id);
    }
    
    public List<Assignment> getAllAssignmentsByEmpId(final Integer Id) {
        return assignmentRepository.getAllAssignmentsByEmpId(Id);
    }
    
    public List<Assignment> getAllActiveEmployeeAssignmentsByEmpId(final Integer Id) {
        return assignmentRepository.getAllActiveAssignmentsByEmpId(Id);
    }
    
    public Assignment getAssignmentForPosition(final Long posId,final Date givenDate) {
        return assignmentRepository.getAssignmentsForPosition(posId,givenDate).get(0);
    }
    
    public Assignment getPrimaryAssignmentForPositon(final Long posId) {
        return assignmentRepository.getPrimaryAssignmentForPosition(posId);
    }
    
    @Transactional
    public void createAssignment(final Assignment assignment) {
        assignmentRepository.save(assignment);
    }
    
    @Transactional
    public void updateAssignment(final Assignment assignment) {
        assignmentRepository.save(assignment);
    }
    
    public Assignment getPrimaryAssignmentForUser(final Long userId) {
        return assignmentRepository.getPrimaryAssignmentForUser(userId);
    }
    
    public Assignment getPriamryAssignmentForEmployee(final Integer empId) {
        return assignmentRepository.getPrimaryAssignmentForEmployee(empId);
    }

    public Assignment getPrimaryAssignmentForEmployeeByToDate(final Integer empId,final Date toDate) {
        return assignmentRepository.getAssignmentByEmpAndDate(empId,toDate);
    }
}
