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
package org.egov.works.revisionestimate.repository;

import java.util.Date;
import java.util.List;

import org.egov.infra.admin.master.entity.User;
import org.egov.works.revisionestimate.entity.RevisionAbstractEstimate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RevisionEstimateRepository extends JpaRepository<RevisionAbstractEstimate, Long> {

    @Query("from RevisionAbstractEstimate re where re.parent.id=:id and re.egwStatus.code=:status order by re.id")
    List<RevisionAbstractEstimate> findByParent_IdAndStatus(@Param("id") final Long id, @Param("status") final String status);

    @Query("from RevisionAbstractEstimate re where re.parent.id=:id and re.egwStatus.code=:status and re.id<:reId order by re.id")
    List<RevisionAbstractEstimate> findByParent_IdAndStatusForView(@Param("id") final Long id, @Param("reId") final Long reId,
            @Param("status") final String status);

    List<RevisionAbstractEstimate> findByParent_Id(final Long id);

    @Query("select distinct(re.createdBy) from RevisionAbstractEstimate as re")
    List<User> findRevisionEstimateCreatedByUsers();

    List<RevisionAbstractEstimate> findByParent_idAndCreatedDateAfterAndEgwStatus_codeNotLike(
            @Param("parentId") final Long parentId,
            @Param("createdDate") final Date createdDate, @Param("status") final String status);

    @Query("select distinct(re.estimateNumber) from RevisionAbstractEstimate as re where upper(re.estimateNumber) like upper(:estimateNumber)")
    List<String> findDistinctEstimateNumberContainingIgnoreCase(@Param("estimateNumber") final String estimateNumber);

    @Query("select distinct(re.estimateNumber) from RevisionAbstractEstimate re where re.parent is not null and re.egwStatus.code =:statusApproved and upper(re.estimateNumber) like upper(:code) and exists (select estimate from WorkOrderEstimate woe where woe.estimate.id = re.id OR exists(select mbh.workOrderEstimate from MBHeader mbh where egwStatus.code =:statusCancelled))")
    List<String> getRENumbersToCancel(@Param("code") String code, @Param("statusApproved") String statusApproved,
            @Param("statusCancelled") String statusCancelled);

    RevisionAbstractEstimate findByParent_IdAndEgwStatus_codeEquals(Long estimateId, String statusCode);

    @Query("select re from RevisionAbstractEstimate as re where re.parent.id =:estimateId and egwStatus.code not in (:cancelledStatus, :approvedStatus, :newStatus)")
    RevisionAbstractEstimate findByParentAndStatus(@Param("estimateId") Long estimateId,
            @Param("cancelledStatus") String cancelledStatus, @Param("approvedStatus") String approvedStatus,
            @Param("newStatus") String newStatus);

    List<RevisionAbstractEstimate> findByParent_IdAndEgwStatus_codeNotLike(@Param("id") final Long id,
            @Param("status") String status);

}
