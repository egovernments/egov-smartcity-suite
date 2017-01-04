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
package org.egov.commons.repository;

import java.util.List;

import org.egov.commons.EgwTypeOfWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeOfWorkRepository extends JpaRepository<EgwTypeOfWork, Long> {

    EgwTypeOfWork findByCodeIgnoreCase(final String code);

    List<EgwTypeOfWork> findByNameContainingIgnoreCase(final String name);

    @Query("select tw from EgwTypeOfWork as tw where tw.parentid is null and upper(tw.egPartytype.code) = :partyTypeCode order by upper(tw.name) asc")
    List<EgwTypeOfWork> findByPartyType(@Param("partyTypeCode") final String partyTypeCode);

    @Query("select tw from EgwTypeOfWork as tw where tw.parentid.id =:parentId and upper(tw.egPartytype.code) = :partyTypeCode order by upper(tw.name) asc")
    List<EgwTypeOfWork> findByParentidAndEgPartytype(@Param("parentId") final Long parentId,
            @Param("partyTypeCode") final String partyTypeCode);

    List<EgwTypeOfWork> findByParentid_idIsNullAndEgPartytype_codeContainingIgnoreCaseAndActiveTrueOrderByName(
            final String partyTypeCode);

    List<EgwTypeOfWork> findByParentid_idAndEgPartytype_codeContainingIgnoreCaseAndActiveTrueOrderByName(
            final Long parentid, final String partyTypeCode);
}
