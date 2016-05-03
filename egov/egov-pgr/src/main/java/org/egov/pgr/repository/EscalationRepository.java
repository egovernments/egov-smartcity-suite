/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.repository;

import org.egov.pgr.entity.Escalation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Vaibhav.K
 *
 */
@Repository
public interface EscalationRepository extends JpaRepository<Escalation, Long> {
    
    @Query(" from Escalation E where E.designation.id=:designationId and E.complaintType.id=:comTypeId")
    public Escalation findByDesignationAndComplaintType(@Param("designationId")Long designationId,@Param("comTypeId")Long comTypeId);
    
    @Query(" from Escalation E where  E.complaintType.id=:comTypeId")
    public List <Escalation> findEscalationByComplaintTypeId(@Param("comTypeId")Long comTypeId);

	@Query("select cr from Escalation cr where cr.designation.id=:designationId and cr.complaintType.id=:complaintTypeId")
	public Page<Escalation> findEscalationBycomplaintTypeAndDesignation(@Param("complaintTypeId") Long complaintTypeId,
			@Param("designationId") Long designationId,
		    Pageable pageable);

	@Query("select cr from Escalation cr ")
	public Page<Escalation> findEscalationByAll(Pageable pageable);

	@Query("select cr from Escalation cr where  cr.complaintType.id=:complaintTypeId")
	public Page<Escalation> findEscalationBycomplaintType(@Param("complaintTypeId")Long complaintTypeId,
			Pageable pageable);
	
	@Query("select cr from Escalation cr where cr.designation.id=:designationId ")
	public Page<Escalation> findEscalationByDesignation(@Param("designationId") Long designationId,
			Pageable pageable);

}
