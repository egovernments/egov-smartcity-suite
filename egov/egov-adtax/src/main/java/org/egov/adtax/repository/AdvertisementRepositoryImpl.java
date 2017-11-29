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

import org.egov.adtax.entity.Advertisement;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.entity.AgencyWiseResult;
import org.egov.adtax.entity.enums.AdvertisementStatus;
import org.egov.adtax.entity.enums.AdvertisementStructureType;
import org.egov.adtax.search.contract.HoardingSearch;
import org.egov.commons.Installment;
import org.egov.infra.utils.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("all")
@Transactional(readOnly = true)
public class AdvertisementRepositoryImpl implements AdvertisementRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Advertisement> fetchAdvertisementLike(final HoardingSearch hoardingSearch) {
        final Criteria hoardingCriteria = entityManager.unwrap(Session.class).createCriteria(Advertisement.class, "hoarding");
      //  hoardingCriteria.createAlias("hoarding.location", "location").createAlias("hoarding.ward", "ward");
        if (hoardingSearch.getAgency() != null)
            hoardingCriteria.add(Restrictions.eq("agency.id", hoardingSearch.getAgency()));
        if (isNotBlank(hoardingSearch.getAdvertisementNumber()))
            hoardingCriteria.add(Restrictions.eq("advertisementNumber", hoardingSearch.getAdvertisementNumber()));
        if (isNotBlank(hoardingSearch.getApplicationNumber()))
            hoardingCriteria.add(Restrictions.eq("applicationNumber", hoardingSearch.getApplicationNumber()));
        if (isNotBlank(hoardingSearch.getPermissionNumber()))
            hoardingCriteria.add(Restrictions.eq("permissionNumber", hoardingSearch.getPermissionNumber()));
        if (hoardingSearch.getAdminBoundryParent() != null) 
            hoardingCriteria.add(Restrictions.eq("locality.id", hoardingSearch.getAdminBoundryParent()));
        if (hoardingSearch.getAdminBoundry() != null)
            hoardingCriteria.add(Restrictions.eq("ward.id", hoardingSearch.getAdminBoundry()));
        if (hoardingSearch.getCategory() != null)
            hoardingCriteria.add(Restrictions.eq("category.id", hoardingSearch.getCategory()));
        if (hoardingSearch.getSubCategory() != null)
            hoardingCriteria.add(Restrictions.eq("subCategory.id", hoardingSearch.getSubCategory()));
        if (hoardingSearch.getRevenueInspector() != null)
            hoardingCriteria.add(Restrictions.eq("revenueInspector.id", hoardingSearch.getRevenueInspector()));
        if (hoardingSearch.getStatus() != null)
            hoardingCriteria.add(Restrictions.eq("status", hoardingSearch.getStatus()));
        if (hoardingSearch.getApplicationFromDate() != null)
            
            hoardingCriteria.add(Restrictions.ge("applicationDate", DateUtils.startOfDay(hoardingSearch.getApplicationFromDate())));
        if (hoardingSearch.getApplicationToDate() != null)
            hoardingCriteria.add(Restrictions.le("applicationDate", DateUtils.endOfDay(hoardingSearch.getApplicationToDate())));

        return hoardingCriteria.list();
    }

    @Override
    public List<Object[]> fetchAdvertisementBySearchType(final Advertisement hoarding, final String searchType) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(AdvertisementPermitDetail.class, "permit")
                .createAlias("permit.advertisement", "advertisement")
                .createAlias("advertisement.ward", "ward").createAlias("advertisement.locality", "locality")
                .createAlias("advertisement.category", "category").createAlias("advertisement.subCategory", "subCategory")
                .createAlias("advertisement.revenueInspector", "revenueInspector").createAlias("permit.agency", "agency");
        if ("agency".equalsIgnoreCase(searchType))
            criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("agency"), "agency")
                    .add(Projections.rowCount(), "count")).setResultTransformer(Transformers.aliasToBean(AgencyWiseResult.class));
        if (null != hoarding.getAdvertisementNumber() && !hoarding.getAdvertisementNumber().isEmpty())
            criteria.add(Restrictions.eq("hoarding.advertisementNumber", hoarding.getAdvertisementNumber()));
        if (null != hoarding.getLocality())
            criteria.add(Restrictions.eq("locality.id", hoarding.getLocality().getId()));
        if (null != hoarding.getWard())
            criteria.add(Restrictions.eq("ward.id", hoarding.getWard().getId()));
        if (null != hoarding.getCategory())
            criteria.add(Restrictions.eq("category.id", hoarding.getCategory().getId()));
        if (null != hoarding.getSubCategory())
            criteria.add(Restrictions.eq("subCategory.id", hoarding.getSubCategory().getId()));
   /*     if (null != hoarding.getAgency() && null!=hoarding.getAgency().getId())
            criteria.add(Restrictions.eq("agency.id", hoarding.getAgency().getId()));
    */    if (null != hoarding.getStatus())
            criteria.add(Restrictions.eq("hoarding.status", hoarding.getStatus()));
        if (null != hoarding.getRevenueInspector())
            criteria.add(Restrictions.eq("revenueInspector.id", hoarding.getRevenueInspector().getId()));
        return criteria.list();
    }
  
    @Override
    public int findActivePermanentAdvertisementsByCurrentInstallment(Installment installment) {
        final Criteria advtCriteria = entityManager.unwrap(Session.class)
                .createCriteria(Advertisement.class, "advertise").createAlias("advertise.demandId", "demand");
                //.createAlias("demand.egInstallmentMaster", "installment");

        advtCriteria.add(Restrictions.eq("status", AdvertisementStatus.ACTIVE));
        advtCriteria.add(Restrictions.eq("type", AdvertisementStructureType.PERMANENT));
        if (installment != null)
            advtCriteria.add(Restrictions.eq("demand.egInstallmentMaster.id", installment.getId()));

        return advtCriteria.list().size();
    }

    @Override
    public List<Advertisement> findActivePermanentAdvertisementsByCurrentInstallmentAndNumberOfResultToFetch(
            Installment installment, int noOfResultToFetch) {
        final Criteria advtCriteria = entityManager.unwrap(Session.class)
                .createCriteria(Advertisement.class, "advertise").createAlias("advertise.demandId", "demand");
               // .createAlias("demand.egInstallmentMaster", "installment");
       // advtCriteria.setFetchMode("advertise.demandId", FetchMode.SELECT);

        advtCriteria.add(Restrictions.eq("advertise.status", AdvertisementStatus.ACTIVE));
        advtCriteria.add(Restrictions.eq("advertise.type", AdvertisementStructureType.PERMANENT));
        if (installment != null)
            advtCriteria.add(Restrictions.eq("demand.egInstallmentMaster.id", installment.getId()));
        advtCriteria.setMaxResults(noOfResultToFetch);
        advtCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return advtCriteria.list();
    }
    @Override
    public List<Advertisement> fetchAdvertisementBySearchParams(final Advertisement hoarding) {

          final Criteria hoardingCriteria = entityManager.unwrap(Session.class).createCriteria(AdvertisementPermitDetail.class, "permit")
                  .createAlias("permit.advertisement", "advertisement")
                  .createAlias("advertisement.category", "category").createAlias("advertisement.subCategory", "subCategory")
                  .createAlias("advertisement.revenueInspector", "revenueInspector").createAlias("permit.agency", "agency");
        
          
        if (null != hoarding.getAdvertisementNumber() && !hoarding.getAdvertisementNumber().isEmpty())
            hoardingCriteria.add(Restrictions.eq("advertisement.advertisementNumber", hoarding.getAdvertisementNumber()));
        if (null != hoarding.getLocality())
            hoardingCriteria.add(Restrictions.eq("locality.id", hoarding.getLocality().getId()));
        if (null != hoarding.getWard())
            hoardingCriteria.add(Restrictions.eq("ward.id", hoarding.getWard().getId()));
     if (null != hoarding.getCategory())
            hoardingCriteria.add(Restrictions.eq("category.id", hoarding.getCategory().getId()));
        if (null != hoarding.getSubCategory())
            hoardingCriteria.add(Restrictions.eq("subCategory.id", hoarding.getSubCategory().getId()));
    /*    if (null != hoarding.getAgency() && null!=hoarding.getAgency().getId())
            hoardingCriteria.add(Restrictions.eq("agency.id", hoarding.getAgency().getId()));
    */    if (null != hoarding.getStatus())
            hoardingCriteria.add(Restrictions.eq("advertisement.status", hoarding.getStatus()));
        if (null != hoarding.getRevenueInspector())
            hoardingCriteria.add(Restrictions.eq("revenueInspector.id", hoarding.getRevenueInspector().getId()));

        return hoardingCriteria.list();
    
    }

}
