/**
 * 
 */
package org.egov.eis.service;

import java.util.Date;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vaibhav.K
 *
 */
@Service
@Transactional(readOnly = true)
public class EisCommonService {
    
    @Autowired
    private PositionHierarchyService positionHierarchyService;
    
    @Autowired
    private AssignmentService assignmentService;
    
    @Autowired
    private UserService userService;
    
    public Position getSuperiorPositionByObjectTypeAndPositionFrom(final Integer objectId,final Integer posId) {
        return positionHierarchyService.getPositionHierarchyByPosAndObjectType(posId,objectId).getToPosition();
    }
    
    public Position getSuperiorPositionByObjectAndObjectSubTypeAndPositionFrom(final Integer objectId,final Integer posId,final String objectSubType) {
        return positionHierarchyService.getPosHirByPosAndObjectTypeAndObjectSubType(posId, objectId, objectSubType).getToPosition();
    }
    
    public User getUserForPositionId(final Integer posId,final Date givenDate) {
        try{
            return assignmentService.getAssignmentsForPositionId(posId,givenDate).getEmployee().getUserMaster();
        }
        catch(NullPointerException e) {
            throw new EGOVRuntimeException("User Not Found");
        }
        catch(Exception e) {
            throw new EGOVRuntimeException(e.getMessage());
        }
    }

}
