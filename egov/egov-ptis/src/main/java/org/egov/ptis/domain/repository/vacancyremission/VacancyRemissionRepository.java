/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.ptis.domain.repository.vacancyremission;

import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.VacancyRemission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacancyRemissionRepository extends JpaRepository<VacancyRemission, Long> {

    @Query("select vr from VacancyRemission vr where vr.basicProperty.upicNo=:upicNo and vr.status = 'APPROVED'")
    VacancyRemission findByUpicNo(@Param("upicNo") String name);

    @Query("select vr from VacancyRemission vr where vr.basicProperty.upicNo=:upicNo and vr.status = 'Rejection Acknowledgement Generated' order by id desc ")
    List<VacancyRemission> findAllRejectionAckGeneratedForUpicNo(@Param("upicNo") String name);

    @Query("select vr from VacancyRemission vr,VacancyRemissionApproval vra where vr.basicProperty.upicNo=:upicNo and vr.id=vra.vacancyRemission order by vr.id desc ")
    List<VacancyRemission> findAllSpecialNoticesGeneratedForUpicNo(@Param("upicNo") String name);

    @Query("select vr from VacancyRemission vr where vr.basicProperty.upicNo=:upicNo and vr.status = 'REJECTED'")
    VacancyRemission findRejectedByUpicNo(@Param("upicNo") String name);

    @Query("select vr from VacancyRemission vr where vr.basicProperty.upicNo=:upicNo order by id asc ")
    List<VacancyRemission> getAllVacancyRemissionByUpicNo(@Param("upicNo") String name);

    @Query("select DT from DocumentType DT where DT.name=:name")
    DocumentType findDocumentTypeByName(@Param("name") String name);
    
    @Query("select dt from DocumentType dt where dt.name= :name and dt.transactionType= :transactionType")
    DocumentType findDocumentTypeByNameAndTransactionType(@Param("name") String name, @Param("transactionType") TransactionType transactionType);
    
    @Query("select vr from VacancyRemission vr where vr.basicProperty.upicNo=:upicNo and vr.status = 'IN_WORKFLOW'")
    VacancyRemission getVRUnderWorkflowByUpicNo(@Param("upicNo") String name);
}
