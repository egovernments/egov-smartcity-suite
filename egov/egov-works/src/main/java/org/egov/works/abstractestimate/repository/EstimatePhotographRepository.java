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
package org.egov.works.abstractestimate.repository;

import java.util.List;

import org.egov.works.abstractestimate.entity.EstimatePhotographs;
import org.egov.works.abstractestimate.entity.EstimatePhotographs.WorkProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EstimatePhotographRepository extends JpaRepository<EstimatePhotographs, Long> {

    List<EstimatePhotographs> findByLineEstimateDetails_id(final Long lineEstimateDetailId);

    @Query("select distinct(ep) from EstimatePhotographs as ep where ep.workProgress = :workProgress and ep.lineEstimateDetails.id = :lineEstimateDetailId")
    List<EstimatePhotographs> findByEstimatePhotographAndLineEstimateDetails(@Param("workProgress") WorkProgress workProgress,
            @Param("lineEstimateDetailId") Long lineEstimateDetailId);

    EstimatePhotographs findByFileStore_id(final Long filestoreId);

    @Query("select distinct(ep.lineEstimateDetails.estimateNumber) from EstimatePhotographs as ep where upper(ep.lineEstimateDetails.estimateNumber) like upper(:estimateNumber) and ep.lineEstimateDetails.lineEstimate.status.code != :lineEstimateStatus")
    List<String> findEstimateNumbersForViewEstimatePhotograph(@Param("estimateNumber") String estimateNumber,
            @Param("lineEstimateStatus") String lineEstimateStatus);

    @Query("select distinct(ep.lineEstimateDetails.projectCode.code) from EstimatePhotographs as ep where upper(ep.lineEstimateDetails.projectCode.code) like upper(:workIdentificationNumber) and ep.lineEstimateDetails.lineEstimate.status.code != :lineEstimateStatus")
    List<String> findWorkIdentificationNumberForViewEstimatePhotograph(
            @Param("workIdentificationNumber") String workIdentificationNumber,
            @Param("lineEstimateStatus") String lineEstimateStatus);

    @Query("select distinct(ae.estimateNumber) from AbstractEstimate as ae where upper(ae.estimateNumber) like upper(:estimateNumber) and ae.egwStatus.code != :abstractEstimateStatus")
    List<String> findEstimateNumbersToViewEstimatePhotograph(@Param("estimateNumber") String estimateNumber,
            @Param("abstractEstimateStatus") String abstractEstimateStatus);

    @Query("select distinct(ae.projectCode.code) from AbstractEstimate as ae where upper(ae.projectCode.code) like upper(:workIdentificationNumber) and ae.egwStatus.code != :abstractEstimateStatus")
    List<String> findWorkIdentificationNumberToViewEstimatePhotograph(
            @Param("workIdentificationNumber") String workIdentificationNumber,
            @Param("abstractEstimateStatus") String abstractEstimateStatus);

    @Query("select distinct(ep) from EstimatePhotographs as ep where ep.workProgress = :workProgress and ep.abstractestimate.id = :abstractEstimateId")
    List<EstimatePhotographs> findByEstimatePhotographAndAbstractEstimate(
            @Param("workProgress") WorkProgress estimatePhotographtrackStage,
            @Param("abstractEstimateId") Long abstractEstimateId);

}
