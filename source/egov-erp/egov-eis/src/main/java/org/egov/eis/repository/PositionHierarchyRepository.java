/**
 * 
 */
package org.egov.eis.repository;

import org.egov.eis.entity.PositionHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Vaibhav.K
 *
 */
@Repository
public interface PositionHierarchyRepository extends JpaRepository<PositionHierarchy, Integer>{

    @Query(" from PositionHierarchy P where P.fromPosition.id=:fromPosition and P.objectType.id=:objectType")
    PositionHierarchy getPositionHierarchyByPosAndObjectType(@Param("fromPosition")Long fromPosition,@Param("objectType")Integer objectType);
    
    @Query(" from PositionHierarchy P where P.fromPosition.id=:fromPosition and P.objectType.id=:objectType and P.objectSubType=:objectSubType")
    PositionHierarchy getPosHirByPosAndObjectTypeAndObjectSubType(@Param("fromPosition")Long fromPosition,@Param("objectType")Integer objectType,@Param("objectSubType")String objectSubType);
    
}
