/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2016>  eGovernments Foundation
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
package org.egov.ptis.domain.repository.master.structureclassification;

import java.util.List;

import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StructureClassificationRepository extends JpaRepository<StructureClassification, Long> {

	List<StructureClassification> findByIsActiveTrueOrderByTypeName();

	List<StructureClassification> findByConstrTypeCodeLike(String structureCode);

	@Query(value = "select * from egpt_struc_cl where upper(code) = upper(:constrTypeCode) and id <> :id", nativeQuery = true)
	List<StructureClassification> findByCodeAndNotInId(@Param("constrTypeCode") String constrTypeCode,
			@Param("id") Long id);
	
	@Query(value = "select * from egpt_struc_cl where upper(constr_type) = upper(:constrType) and id <> :id", nativeQuery = true)
        List<StructureClassification> findByNameAndNotInId(@Param("constrType") String constrType,
                        @Param("id") Long id);
	
	@Query(value = "select isActive from egpt_struc_cl where code = :constrTypeCode ", nativeQuery = true)
    Boolean findIsActiveByCode(@Param("constrTypeCode") String constrTypeCode);
	
	@Query(value = "select * from egpt_struc_cl  where code = :constrTypeCode ", nativeQuery = true)
	StructureClassification findClassificationByCode(@Param("constrTypeCode") String constrTypeCode);

	@Query(value = "from Category where structureClass.id = :id and isActive = true")
	List<Category> findByUsageUnitRateActive(@Param("id") Long id);
}