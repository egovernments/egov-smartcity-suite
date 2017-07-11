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

import org.egov.pgr.entity.Complaint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    Complaint findByCrn(String crn);

    Page<Complaint> findByCreatedByIdOrderByCreatedDateDesc(Long createdBy, Pageable pageable);

    Page<Complaint> findByCreatedByIdAndStatusNameInOrderByCreatedDateDesc(Long createdBy, String[] statuses, Pageable pageable);

    List<Complaint> findByStatusNameIn(List<String> statusList);

    Long countByStatusNameIn(String[] statuses);

    Long countByCreatedByIdAndStatusNameIn(Long createdBy, String[] statuses);

    Long countByCreatedById(Long createdBy);

    Page<Complaint> findByCreatedByIdNotOrderByCreatedDateDesc(Long createdBy, Pageable pageable);

    @Query(value = "select * FROM egpgr_complaint WHERE createdBy<> :createdBy AND earth_box( ll_to_earth( :lat, :lng), :distance) @> " +
            "ll_to_earth(egpgr_complaint.lat, egpgr_complaint.lng) order by createddate DESC limit :limit offset :offset", nativeQuery = true)
    List<Complaint> findByNearestComplaint(@Param("createdBy") Long createdBy, @Param("lat") Float lat,
                                           @Param("lng") Float lng, @Param("distance") Long distance,
                                           @Param("limit") Long limit, @Param("offset") Long offset);

}
