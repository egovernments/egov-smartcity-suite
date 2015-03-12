/**
 * 
 */
package org.egov.eis.service;

import static org.junit.Assert.*;

import java.util.List;

import org.egov.eis.EISAbstractSpringIntegrationTest;
import org.egov.pims.model.Assignment;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Vaibhav.K
 *
 */
public class AssignmentServiceTest extends EISAbstractSpringIntegrationTest {

    @Autowired
    private AssignmentService assignmentService;
    
    @Test
    public void getAssignmentById() {
        Assignment assign = assignmentService.getAssignmentById(Long.valueOf(1));
        
        assertNotNull(assign);
    }
    
    @Test
    public void getListOfAssignments() {
        List<Assignment> assignList = assignmentService.getAllAssignmentsByEmpId(1);
        
        assertNotEquals(0,assignList.size());
    }
    
    @Test 
    public void getListOfActiveAssignments() {
        List<Assignment> assignList = assignmentService.getAllActiveEmployeeAssignmentsByEmpId(1);
        
        assertNotEquals(0,assignList.size());
    }
}
