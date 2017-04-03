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

package org.egov.adtax.service;

import org.apache.commons.lang.StringUtils;
import org.egov.adtax.entity.AdvertisementRate;
import org.egov.adtax.entity.AdvertisementRatesDetails;
import org.egov.adtax.entity.HoardingCategory;
import org.egov.adtax.entity.RatesClass;
import org.egov.adtax.entity.ScheduleOfRateSearch;
import org.egov.adtax.entity.SubCategory;
import org.egov.adtax.entity.UnitOfMeasure;
import org.egov.adtax.repository.AdvertisementRateDetailRepository;
import org.egov.adtax.repository.AdvertisementRateRepository;
import org.egov.commons.CFinancialYear;
import org.egov.commons.repository.CFinancialYearRepository;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AdvertisementRateService {
    private final AdvertisementRateRepository ratesRepository;
    private final AdvertisementRateDetailRepository rateDetailRepository;
    private final CFinancialYearRepository cFinancialYearRepository;

    @PersistenceContext
    private EntityManager entityManager;
    
    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }
    
    @Autowired
    public AdvertisementRateService(final AdvertisementRateRepository ratesRepository,
            final AdvertisementRateDetailRepository rateDetailRepository,
            final CFinancialYearRepository cFinancialYearRepository) {
        this.ratesRepository = ratesRepository;
        this.rateDetailRepository = rateDetailRepository;
        this.cFinancialYearRepository = cFinancialYearRepository;
    }

    public AdvertisementRate getScheduleOfRateById(final Long id) {
        return ratesRepository.findOne(id);
    }

    @ReadOnly
    public List<AdvertisementRatesDetails> findScheduleOfRateDetailsByCategorySubcategoryUomAndClass(
            final HoardingCategory category, final SubCategory subCategory, final UnitOfMeasure unitOfMeasure,
            final RatesClass ratesClass, final CFinancialYear financialYear) {
        return rateDetailRepository.findScheduleOfRateDetailsByCategorySubcategoryUomAndClass(category, subCategory,
                unitOfMeasure, ratesClass, financialYear);
    }

    @Transactional
    public AdvertisementRate createScheduleOfRate(final AdvertisementRate rate) {

        // if(rate!=null && rate.getId()!=null)
        return ratesRepository.save(rate);
    }

    public AdvertisementRate findScheduleOfRateByCategorySubcategoryUomAndClass(final HoardingCategory category,
            final SubCategory subCategory, final UnitOfMeasure unitofmeasure, final RatesClass classtype,
            final CFinancialYear financialYear) {
        return ratesRepository.findScheduleOfRateByCategorySubcategoryUomAndClass(category, subCategory, unitofmeasure,
                classtype, financialYear);
    }

    public void deleteAllInBatch(final List<AdvertisementRatesDetails> existingRateDetails) {
        rateDetailRepository.deleteInBatch(existingRateDetails);

    }

    public Double getAmountByCategorySubcategoryUomAndClass(final HoardingCategory category,
            final SubCategory subCategory, final UnitOfMeasure unitofmeasure, final RatesClass classtype, final Double units,
            final CFinancialYear financialYear) {
        Double rate = Double.valueOf(0);

        if (units != null && category != null && subCategory != null && unitofmeasure != null && classtype != null)
            rate = rateDetailRepository.getAmountByCategorySubcategoryUomAndClass(category, subCategory, unitofmeasure,
                    classtype, units, financialYear);

        return rate;
    }

    public Double getAmountBySubcategoryUomClassAndMeasurement(final Long subCategoryId, final Long unitOfMeasureId,
            final Long rateClassId, final Double measurement) {
        Double rate = Double.valueOf(0);

        if (measurement != null && subCategoryId != null && unitOfMeasureId != null && rateClassId != null)
            rate = rateDetailRepository.getAmountBySubcategoryUomClassAndMeasurement(measurement, subCategoryId, unitOfMeasureId,
                    rateClassId);
        if (rate == null)
            return Double.valueOf(0);
        return rate;
    }
    public AdvertisementRatesDetails getRatesBySubcategoryUomClassFinancialYearAndMeasurement(final Long subCategoryId, final Long unitOfMeasureId,
            final Long rateClassId, final Double units,CFinancialYear cfinancialYear) {
        List<AdvertisementRatesDetails> rate = null;
        if (units != null && subCategoryId != null && unitOfMeasureId != null && rateClassId != null)
            rate = rateDetailRepository.getRatesBySubcategoryUomClassFinancialYearAndMeasurement(units, subCategoryId, unitOfMeasureId,
                    rateClassId,cfinancialYear.getId());
        if(rate!=null && rate.size()>0)
            return rate.get(0);
        
        return null;
    }
    
    public AdvertisementRatesDetails getRatesBySubcategoryUomClassAndMeasurementByFinancialYearInDecendingOrder(final Long subCategoryId, final Long unitOfMeasureId,
            final Long rateClassId, final Double units) {
        List<AdvertisementRatesDetails> rate = null;

        if (units != null && subCategoryId != null && unitOfMeasureId != null && rateClassId != null)
            rate = rateDetailRepository.getRatesBySubcategoryUomClassMeasurementLessthanCurrentFinancialYearAndFinancialYearInDecendingOrder(units, subCategoryId, unitOfMeasureId,
                    rateClassId);
        if(rate!=null && rate.size()>0)
            return rate.get(0);
        
        return null;
    }
    

   @SuppressWarnings("unchecked")
   @ReadOnly
    public List<ScheduleOfRateSearch> getScheduleOfRateSearchResult(final String category, final String subCategory,final String unitofmeasure,
            final String classtype, final String finyear) {
        
        StringBuilder queryString = new StringBuilder();
        queryString.append("Select A From AdvertisementRatesDetails A where ");
        Boolean var = Boolean.FALSE;
        if(!StringUtils.isEmpty(category))
        {
            queryString.append(" A.advertisementRate.category.id=:category ");
            var = Boolean.TRUE;
        }
        
        if(!StringUtils.isEmpty(subCategory))
        {       if(var) {
            queryString.append(" and " );
        }
            queryString.append(" A.advertisementRate.subCategory.id=:subCategory ");
            var = Boolean.TRUE;
        }
        
        if(!StringUtils.isEmpty(unitofmeasure))
        {
            if(var) {
                queryString.append(" and " );
            }
            queryString.append(" A.advertisementRate.unitofmeasure.id=:unitofmeasure ");
            var = Boolean.TRUE;
        }
        
        if(!StringUtils.isEmpty(classtype))
        {
            if(var) {
                queryString.append(" and " );
            }
            queryString.append(" A.advertisementRate.classtype.id=:classtype ");
            var = Boolean.TRUE;

        }
        
        if(!StringUtils.isEmpty(finyear))
        {
            if(var) {
                queryString.append(" and " );
            }
            queryString.append(" A.advertisementRate.financialyear.id=:finyear ");
            
        }
        
        Query query = entityManager.unwrap(Session.class).createQuery(queryString.toString());        
        if(!StringUtils.isEmpty(category)){
              query.setParameter("category", Long.parseLong(category));
        }
        if(!StringUtils.isEmpty(subCategory)){
            query.setParameter("subCategory", Long.parseLong(subCategory));
        }
        if(!StringUtils.isEmpty(unitofmeasure)){
             query.setParameter("unitofmeasure", Long.parseLong(unitofmeasure));
        }
        if(!StringUtils.isEmpty(classtype)){
              query.setParameter("classtype",  Long.parseLong(classtype));
        }
        if(!StringUtils.isEmpty(finyear)){
              query.setParameter("finyear",  Long.parseLong(finyear));
        }
        List<AdvertisementRatesDetails>  rateList = query.list();
        List<ScheduleOfRateSearch> scheduleOfRateList = new ArrayList<ScheduleOfRateSearch>();
        rateList.forEach(result ->{
            
           ScheduleOfRateSearch scheduleOfRateSearch = new ScheduleOfRateSearch();
           scheduleOfRateSearch.setCategory(result.getAdvertisementRate().getCategory().getCode());
           scheduleOfRateSearch.setSubCategory(result.getAdvertisementRate().getSubCategory().getCode());
           scheduleOfRateSearch.setUnitofmeasure(result.getAdvertisementRate().getUnitofmeasure().getCode());
           scheduleOfRateSearch.setClasstype(result.getAdvertisementRate().getClasstype().getDescription());
           scheduleOfRateSearch.setFinancialyear(result.getAdvertisementRate().getFinancialyear().getFinYearRange());
           scheduleOfRateSearch.setUnitfrom(result.getUnitFrom());
           scheduleOfRateSearch.setUnitto(result.getUnitTo());
           scheduleOfRateSearch.setAmount(result.getAmount());
           scheduleOfRateSearch.setUnitfactor(result.getAdvertisementRate().getUnitrate());
           scheduleOfRateList.add(scheduleOfRateSearch);
       });
        
        return scheduleOfRateList;
    }

    public List<CFinancialYear> getAllFinancialYears() {
        return cFinancialYearRepository.getAllFinancialYears();
    }
}
