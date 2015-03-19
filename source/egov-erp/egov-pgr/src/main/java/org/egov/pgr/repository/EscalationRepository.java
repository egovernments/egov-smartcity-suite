/**
 * 
 */
package org.egov.pgr.repository;

import org.egov.pgr.entity.Escalation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Vaibhav.K
 *
 */
@Repository
public interface EscalationRepository extends JpaRepository<Escalation, Long> {
    
    @Query(" from Escalation E where E.designation.designationId=:designationId and E.complaintType.id=:comTypeId")
    public Escalation findByDesignationAndComplaintType(@Param("designationId")Integer designationId,@Param("comTypeId")Long comTypeId);

}
