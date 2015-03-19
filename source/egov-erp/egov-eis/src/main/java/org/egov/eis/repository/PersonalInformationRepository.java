/**
 * 
 */
package org.egov.eis.repository;

import org.egov.pims.model.PersonalInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Vaibhav.K
 *
 */
@Repository
public interface PersonalInformationRepository extends JpaRepository<PersonalInformation, Integer>{
	
	public PersonalInformation findByIdPersonalInformation(Integer idPersonalInformation);
	
	@Query("from PersonalInformation P where P.userMaster.id =:userId")
	public PersonalInformation getPersonalInformationByUserId(@Param("userId")Long userId);
	
}
