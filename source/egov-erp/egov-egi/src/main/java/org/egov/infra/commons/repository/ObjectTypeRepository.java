/**
 * 
 */
package org.egov.infra.commons.repository;

import org.egov.commons.ObjectType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Vaibhav.K
 *
 */
public interface ObjectTypeRepository extends JpaRepository<ObjectType, Integer>{
    
    public ObjectType findByType(String type);

}
