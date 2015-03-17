/**
 * 
 */
package org.egov.eis.service;

import org.egov.eis.entity.PositionHierarchy;
import org.egov.eis.repository.PositionHierarchyRepository;
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
public class PositionHierarchyService {
    
    private final PositionHierarchyRepository positionHierarchyRepository;
    
    @Autowired
    public PositionHierarchyService(final PositionHierarchyRepository positionHierarchyRepository) {
        this.positionHierarchyRepository = positionHierarchyRepository;
    }
    
    @Transactional
    public void createPositionHierarchy(PositionHierarchy positionHierarchy) {
        positionHierarchyRepository.save(positionHierarchy);
    }
    
    @Transactional
    public void updatePositionHierarchy(PositionHierarchy positionHierarchy) {
        positionHierarchyRepository.save(positionHierarchy);
    }
    
    @Transactional
    public void deletePositionHierarchy(PositionHierarchy positionHierarchy) {
        positionHierarchyRepository.delete(positionHierarchy);
    }
    
    public PositionHierarchy getPositionHierarchyByPosAndObjectType(Integer posId,Integer objectId) {
        return positionHierarchyRepository.getPositionHierarchyByPosAndObjectType(posId, objectId);
    }
    
    public PositionHierarchy getPosHirByPosAndObjectTypeAndObjectSubType(Integer posId,Integer objectId,String objectSubType) {
        return positionHierarchyRepository.getPosHirByPosAndObjectTypeAndObjectSubType(posId, objectId, objectSubType);
    }

}
