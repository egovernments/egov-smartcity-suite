/**
 * 
 */
package org.egov.eis.service;

import java.util.Date;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.pims.commons.DesignationMaster;
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
    
    public Position getSuperiorPositionByObjectTypeAndPositionFrom(final Integer objectId,final Long posId) {
        return positionHierarchyService.getPositionHierarchyByPosAndObjectType(posId,objectId).getToPosition();
    }
    
    public Position getSuperiorPositionByObjectAndObjectSubTypeAndPositionFrom(final Integer objectId,final String objectSubType,final Long posId) {
        return positionHierarchyService.getPosHirByPosAndObjectTypeAndObjectSubType(posId, objectId, objectSubType).getToPosition();
    }
    
    public User getUserForPosition(final Long posId,final Date givenDate) {
        try{
            return assignmentService.getAssignmentForPosition(posId,givenDate).getEmployee().getUserMaster();
        }
        catch(NullPointerException e) {
            throw new EGOVRuntimeException("User Not Found");
        }
        catch(Exception e) {
            throw new EGOVRuntimeException(e.getMessage());
        }
    }

    public DesignationMaster getEmployeeDesignation(final Long posId) {
        return assignmentService.getPrimaryAssignmentForPositon(posId).getDesigId();
    }
    
}
