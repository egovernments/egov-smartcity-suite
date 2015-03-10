/**
 * 
 */
package org.egov.eis.repository;

import java.util.List;

import org.egov.pims.commons.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Vaibhav.K
 *
 */
@Repository
public interface PositionMasterRepository extends JpaRepository<Position, Integer>{
	
	Position findByName(String name);
	List<Position> findByNameContainingIgnoreCase(String name);

}
