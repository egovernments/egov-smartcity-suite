/**
 * 
 */
package org.egov.eis.service;

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
    
    public Position getSuperiorPositionByObjectTypeAndPositionFrom(Integer objectId,Integer posId) {
        return positionHierarchyService.getPositionHierarchyByPosAndObjectType(posId,objectId).getToPosition();
    }
    
    public Position getSuperiorPositionByObjectAndObjectSubTypeAndPositionFrom(Integer objectId,Integer posId,String objectSubType) {
        return positionHierarchyService.getPosHirByPosAndObjectTypeAndObjectSubType(posId, objectId, objectSubType).getToPosition();
    }

}
