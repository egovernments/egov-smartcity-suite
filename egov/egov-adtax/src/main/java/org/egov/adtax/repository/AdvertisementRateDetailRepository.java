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

package org.egov.adtax.repository;

import org.egov.adtax.entity.AdvertisementRatesDetails;
import org.egov.adtax.entity.HoardingCategory;
import org.egov.adtax.entity.RatesClass;
import org.egov.adtax.entity.SubCategory;
import org.egov.adtax.entity.UnitOfMeasure;
import org.egov.commons.CFinancialYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementRateDetailRepository extends JpaRepository<AdvertisementRatesDetails, Long> {

    @Query("select A from AdvertisementRatesDetails A where A.advertisementRate.category=:category and A.advertisementRate.classtype=:ratesClass and A.advertisementRate.unitofmeasure=:uom and A.advertisementRate.subCategory=:subCategory and A.advertisementRate.active=true and A.advertisementRate.financialyear=:financialYear")
    List<AdvertisementRatesDetails> findScheduleOfRateDetailsByCategorySubcategoryUomAndClass(
            @Param("category") HoardingCategory category,
            @Param("subCategory") SubCategory subCategory, @Param("uom") UnitOfMeasure unitOfMeasure,
            @Param("ratesClass") RatesClass ratesClass, @Param("financialYear") CFinancialYear financialYear);

    @Query("select A.amount from AdvertisementRatesDetails A where A.advertisementRate.category=:category and A.unitFrom < :units and A.unitTo >= :units  and A.advertisementRate.classtype=:ratesClass and A.advertisementRate.unitofmeasure=:uom and A.advertisementRate.subCategory=:subCategory and A.advertisementRate.active=true and A.advertisementRate.financialyear=:financialYear")
    Double getAmountByCategorySubcategoryUomAndClass(@Param("category") HoardingCategory category,
            @Param("subCategory") SubCategory subCategory, @Param("uom") UnitOfMeasure unitOfMeasure,
            @Param("ratesClass") RatesClass ratesClass, @Param("units") Double units,
            @Param("financialYear") CFinancialYear financialYear);

    @Query("select A.amount from AdvertisementRatesDetails A where  A.unitFrom < :units and A.unitTo >= :units  and A.advertisementRate.classtype.id=:ratesClass and A.advertisementRate.unitofmeasure.id=:uom and A.advertisementRate.subCategory.id=:subCategory and A.advertisementRate.active=true")
    Double getAmountBySubcategoryUomClassAndMeasurement(@Param("units") Double measurement,
            @Param("subCategory") Long subCategoryId, @Param("uom") Long unitOfMeasureId,
            @Param("ratesClass") Long rateClassId);

    @Query("select A from AdvertisementRatesDetails A where A.advertisementRate.category.id=:category and A.advertisementRate.classtype.id=:ratesClass and A.advertisementRate.unitofmeasure.id=:uom and A.advertisementRate.subCategory.id=:subCategory and A.advertisementRate.active=true and A.advertisementRate.financialyear.id=:financialYear")
    List<AdvertisementRatesDetails> findScheduleOfRateDetailsByCategorySubcategoryUomAndClassId(
            @Param("category") Long category,
            @Param("subCategory") Long subCategory, @Param("uom") Long unitOfMeasure,
            @Param("ratesClass") Long classtype, @Param("financialYear") Long financialYear);
    
    @Query("select A from AdvertisementRatesDetails A where  A.advertisementRate.classtype.id=:ratesClass and A.unitFrom < :units and A.unitTo >= :units and A.advertisementRate.unitofmeasure.id=:uom and A.advertisementRate.subCategory.id=:subCategory and A.advertisementRate.active=true and A.advertisementRate.financialyear.id=:financialYear")
    List<AdvertisementRatesDetails> getRatesBySubcategoryUomClassFinancialYearAndMeasurement(@Param("units") Double units,
            @Param("subCategory") Long subCategoryId, @Param("uom") Long unitOfMeasureId,
            @Param("ratesClass") Long rateClassId,  @Param("financialYear")  Long cfinancialYear);
    
    @Query("select A from AdvertisementRatesDetails A where  A.advertisementRate.classtype.id=:ratesClass and A.unitFrom < :units and A.unitTo >= :units and A.advertisementRate.unitofmeasure.id=:uom and A.advertisementRate.subCategory.id=:subCategory and A.advertisementRate.active=true  and A.advertisementRate.financialyear.startingDate < current_date  order by  A.advertisementRate.financialyear.startingDate desc ")
    List<AdvertisementRatesDetails> getRatesBySubcategoryUomClassMeasurementLessthanCurrentFinancialYearAndFinancialYearInDecendingOrder(@Param("units") Double units,
            @Param("subCategory") Long subCategoryId, @Param("uom") Long unitOfMeasureId,
            @Param("ratesClass") Long rateClassId);
}
