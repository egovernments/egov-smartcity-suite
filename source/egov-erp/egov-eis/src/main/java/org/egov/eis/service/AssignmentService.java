/**
 * 
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

}
