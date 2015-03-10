/**
 * 
 */
package org.egov.eis.repository;

import java.util.List;

import org.egov.pims.commons.DesignationMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Vaibhav.K
 *
 */
@Repository
public interface DesignationMasterRepository extends JpaRepository<DesignationMaster,Integer>{
	
	DesignationMaster findByDesignationName(String designationName);
	List<DesignationMaster> findByDesignationNameContainingIgnoreCase(String designationName);

}
