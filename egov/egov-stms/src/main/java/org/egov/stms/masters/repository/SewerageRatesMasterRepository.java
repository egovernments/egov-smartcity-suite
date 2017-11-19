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
package org.egov.stms.masters.repository;

import org.egov.stms.masters.entity.SewerageRatesMaster;
import org.egov.stms.masters.entity.enums.PropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SewerageRatesMasterRepository extends JpaRepository<SewerageRatesMaster, Long> {

    List<SewerageRatesMaster> findAllByPropertyType(PropertyType propertyType);

    SewerageRatesMaster findByPropertyTypeAndFromDateAndActive(PropertyType propertyType, Date fromDate, boolean active);

    SewerageRatesMaster findByPropertyTypeAndActive(PropertyType propertyType, boolean active);

    @Query("select srm.amount from SewerageRatesMasterDetails srm where srm.sewerageratemaster.propertyType =:propertyType and srm.noOfClosets =:noofclosets and srm.sewerageratemaster.active = true and srm.sewerageratemaster.fromDate <=current_date and (srm.sewerageratemaster.toDate >= current_date or srm.sewerageratemaster.toDate is null)")
    Double getSewerageMonthlyRatesBytNoOfClosetsAndPropertytype(@Param("noofclosets") Integer noofclosets,
            @Param("propertyType") PropertyType propertyType);

    @Query("select srm.monthlyRate from SewerageRatesMaster srm where srm.propertyType =:propertyType and srm.active = true and srm.fromDate <=current_date and (srm.toDate >= current_date or srm.toDate is null)")
    Double getSewerageMonthlyRatesByPropertytype(@Param("propertyType") PropertyType propertyType);

    @Query("select S from SewerageRatesMaster S where S.propertyType=:propertyType and S.active=:active and (S.fromDate<=:date or (S.toDate is null or S.toDate<=:date)) order by S.fromDate desc")
    List<SewerageRatesMaster> getLatestActiveRecord(@Param("propertyType") PropertyType propertyType,
            @Param("active") boolean active, @Param("date") Date date);

    // TODO : rename to findAllFromDateByPropertyType
    @Query("select distinct(S.fromDate) from SewerageRatesMaster S where S.propertyType=:propertyType order by S.fromDate asc")
    List<Date> findFromDateByPropertyType(@Param("propertyType") PropertyType propertyType);

}
