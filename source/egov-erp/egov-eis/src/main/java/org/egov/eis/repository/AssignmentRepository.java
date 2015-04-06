/**
 * 
 */
package org.egov.eis.repository;

import java.util.Date;
import java.util.List;

import org.egov.pims.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Vaibhav.K
 *
 */
@Repository
public interface AssignmentRepository extends JpaRepository<Assignment,Long>{
	
	/*Get all assignments for an employee by emp id*/
	@Query(" from Assignment A where A.employee.idPersonalInformation =:idPersonalInformation order by A.fromDate")
	public List<Assignment> getAllAssignmentsByEmpId(@Param("idPersonalInformation")Integer idPersonalInformation);
	
	/*Get all active assignments 
	 * for an employee as on sysdate*/
	@Query(" from Assignment A where A.fromDate<=current_date and A.toDate>=current_date and A.employee.idPersonalInformation =:idPersonalInformation order by A.fromDate")
	public List<Assignment> getAllActiveAssignmentsByEmpId(@Param("idPersonalInformation")Integer idPersonalInformation);
	
	@Query(" from Assignment A where A.fromDate<=:givenDate and A.toDate>=:givenDate and A.position.id=:posId order by A.fromDate")
	public List<Assignment> getAssignmentsForPosition(@Param("posId")Long posId,@Param("givenDate")Date givenDate);
	
	@Query(" from Assignment A where A.fromDate<=current_date and A.toDate>=current_date and A.position.id=:posId")
	public Assignment getPrimaryAssignmentForPosition(@Param("posId")Long posId);
	
	@Query(" from Assignment A where A.fromDate<=current_date and A.toDate>=current_date and A.isPrimary='Y' and A.employee.userMaster.id=:userId ")
    public Assignment getPrimaryAssignmentForUser(@Param("userId")Long userId);

}
