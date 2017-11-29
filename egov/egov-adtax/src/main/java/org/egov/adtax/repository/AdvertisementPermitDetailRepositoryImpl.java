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

import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.entity.enums.AdvertisementStatus;
import org.egov.adtax.search.contract.HoardingSearch;
import org.egov.infra.utils.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("all")
public class AdvertisementPermitDetailRepositoryImpl implements AdvertisementPermitDetailRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AdvertisementPermitDetail> searchAdvertisementPermitDetailLike(final HoardingSearch hoardingSearch,
            final String hoardingType) {
        final Criteria hoardingCriteria = entityManager.unwrap(Session.class).createCriteria(AdvertisementPermitDetail.class,
                "permit");
        hoardingCriteria.createAlias("permit.advertisement", "advertisement");

        if (hoardingType != null && hoardingType.equalsIgnoreCase("searchLegacyRecord")){
            hoardingCriteria.add(Restrictions.eq("advertisement.legacy", Boolean.TRUE));
            hoardingCriteria.add(Restrictions.isNull("previousapplicationid"));
        }
        if (hoardingSearch.getAgency() != null)
            hoardingCriteria.add(Restrictions.eq("agency.id", hoardingSearch.getAgency()));
        if (isNotBlank(hoardingSearch.getAdvertisementNumber()))
            hoardingCriteria.add(Restrictions.eq("advertisement.advertisementNumber", hoardingSearch.getAdvertisementNumber()));
        if (isNotBlank(hoardingSearch.getApplicationNumber()))
            hoardingCriteria.add(Restrictions.eq("applicationNumber", hoardingSearch.getApplicationNumber()));
        if (isNotBlank(hoardingSearch.getPermissionNumber()))
            hoardingCriteria.add(Restrictions.eq("permissionNumber", hoardingSearch.getPermissionNumber()));
        if (hoardingSearch.getAdminBoundryParent() != null)
            hoardingCriteria.add(Restrictions.eq("advertisement.locality.id", hoardingSearch.getAdminBoundryParent()));
        if (hoardingSearch.getAdminBoundry() != null)
            hoardingCriteria.add(Restrictions.eq("advertisement.ward.id", hoardingSearch.getAdminBoundry()));
        if (hoardingSearch.getCategory() != null)
            hoardingCriteria.add(Restrictions.eq("advertisement.category.id", hoardingSearch.getCategory()));
        if (hoardingSearch.getSubCategory() != null)
            hoardingCriteria.add(Restrictions.eq("advertisement.subCategory.id", hoardingSearch.getSubCategory()));
        if (hoardingSearch.getRevenueInspector() != null)
            hoardingCriteria.add(Restrictions.eq("advertisement.revenueInspector.id", hoardingSearch.getRevenueInspector()));
        if (null != hoardingSearch.getStatus() && !hoardingSearch.getStatus().equals(AdvertisementStatus.ACTIVE))
            hoardingCriteria.add(Restrictions.eq("advertisement.status", hoardingSearch.getStatus()));
        else {
            hoardingCriteria.add(Restrictions.eq("advertisement.status", AdvertisementStatus.ACTIVE));
            hoardingCriteria.add(Restrictions.eq("permit.isActive", Boolean.TRUE));
        }
        if (hoardingSearch.getApplicationFromDate() != null)
            hoardingCriteria
                    .add(Restrictions.ge("applicationDate", DateUtils.startOfDay(hoardingSearch.getApplicationFromDate())));
        if (hoardingSearch.getApplicationToDate() != null)
            hoardingCriteria.add(Restrictions.le("applicationDate", DateUtils.endOfDay(hoardingSearch.getApplicationToDate())));
        if (hoardingSearch.getOwnerDetail() != null)
            hoardingCriteria.add(Restrictions.ilike("ownerDetail", hoardingSearch.getOwnerDetail(),MatchMode.ANYWHERE));
        return hoardingCriteria.list();
    }

    /*
     * @Override public List<Object[]> fetchAdvertisementBySearchType(final Advertisement hoarding, final String searchType) {
     * final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(AdvertisementPermitDetail.class, "permit")
     * .createAlias("permit.advertisement", "advertisement") .createAlias("advertisement.ward",
     * "ward").createAlias("advertisement.locality", "locality") .createAlias("advertisement.category",
     * "category").createAlias("advertisement.subCategory", "subCategory") .createAlias("advertisement.revenueInspector",
     * "revenueInspector").createAlias("permit.agency", "agency"); if ("agency".equalsIgnoreCase(searchType))
     * criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("agency"), "agency")
     * .add(Projections.rowCount(), "count")).setResultTransformer(Transformers.aliasToBean(AgencyWiseResult.class)); if (null !=
     * hoarding.getAdvertisementNumber() && !hoarding.getAdvertisementNumber().isEmpty())
     * criteria.add(Restrictions.eq("hoarding.advertisementNumber", hoarding.getAdvertisementNumber())); if (null !=
     * hoarding.getLocality()) criteria.add(Restrictions.eq("locality.id", hoarding.getLocality().getId())); if (null !=
     * hoarding.getWard()) criteria.add(Restrictions.eq("ward.id", hoarding.getWard().getId())); if (null !=
     * hoarding.getCategory()) criteria.add(Restrictions.eq("category.id", hoarding.getCategory().getId())); if (null !=
     * hoarding.getSubCategory()) criteria.add(Restrictions.eq("subCategory.id", hoarding.getSubCategory().getId())); if (null !=
     * hoarding.getAgency() && null!=hoarding.getAgency().getId()) criteria.add(Restrictions.eq("agency.id",
     * hoarding.getAgency().getId())); if (null != hoarding.getStatus()) criteria.add(Restrictions.eq("hoarding.status",
     * hoarding.getStatus())); if (null != hoarding.getRevenueInspector()) criteria.add(Restrictions.eq("revenueInspector.id",
     * hoarding.getRevenueInspector().getId())); return criteria.list(); }
     */
    @Override
    public List<AdvertisementPermitDetail> searchAdvertisementPermitDetailBySearchParams(
            final AdvertisementPermitDetail advertisementPermitDetail) {

        final Criteria hoardingCriteria = entityManager.unwrap(Session.class)
                .createCriteria(AdvertisementPermitDetail.class, "permit")
                .createAlias("permit.advertisement", "advertisement")
                .createAlias("advertisement.category", "category").createAlias("advertisement.subCategory", "subCategory")
                .createAlias("advertisement.revenueInspector", "revenueInspector");
                //.createAlias("permit.status", "permitStatus");
        if (advertisementPermitDetail.getAdvertisement() != null) {
            if (advertisementPermitDetail.getAdvertisement() != null
                    && advertisementPermitDetail.getAdvertisement().getAdvertisementNumber() != null
                    && !advertisementPermitDetail.getAdvertisement().getAdvertisementNumber().isEmpty())
                hoardingCriteria.add(Restrictions.eq("advertisement.advertisementNumber",
                        advertisementPermitDetail.getAdvertisement().getAdvertisementNumber()));
            if (advertisementPermitDetail.getAdvertisement().getLocality() != null)
                hoardingCriteria.add(Restrictions.eq("advertisement.locality.id",
                        advertisementPermitDetail.getAdvertisement().getLocality().getId()));
            if (advertisementPermitDetail.getAdvertisement().getWard() != null)
                hoardingCriteria.add(
                        Restrictions.eq("advertisement.ward.id", advertisementPermitDetail.getAdvertisement().getWard().getId()));
            if (advertisementPermitDetail.getAdvertisement().getCategory() != null)
                hoardingCriteria.add(Restrictions.eq("advertisement.category.id",
                        advertisementPermitDetail.getAdvertisement().getCategory().getId()));
            if (advertisementPermitDetail.getAdvertisement().getSubCategory() != null)
                hoardingCriteria.add(Restrictions.eq("advertisement.subCategory.id",
                        advertisementPermitDetail.getAdvertisement().getSubCategory().getId()));
            if (advertisementPermitDetail.getAgency() != null && advertisementPermitDetail.getAgency().getId() != null)
                hoardingCriteria.add(Restrictions.eq("agency.id", advertisementPermitDetail.getAgency().getId()));
          
            if (advertisementPermitDetail.getAdvertisement().getStatus() != null) {
                hoardingCriteria
                        .add(Restrictions.eq("advertisement.status", advertisementPermitDetail.getAdvertisement().getStatus()));

            } else {
                hoardingCriteria
                        .add(Restrictions.eq("advertisement.status", AdvertisementStatus.ACTIVE));
                hoardingCriteria
                        .add(Restrictions.eq("isActive", true));// permit status is active.
            }
                
            if (advertisementPermitDetail.getAdvertisement().getRevenueInspector() != null)
                hoardingCriteria.add(Restrictions.eq("advertisement.revenueInspector.id",
                        advertisementPermitDetail.getAdvertisement().getRevenueInspector().getId()));
            if(advertisementPermitDetail.getOwnerDetail() != null){
                hoardingCriteria.add(Restrictions.ilike("ownerDetail",advertisementPermitDetail.getOwnerDetail(),MatchMode.ANYWHERE));
                
            }
            
        }
        //TODO: commented . Check any particular reason for hard coding the status ?
       // hoardingCriteria.add(Restrictions.eq("permitStatus.code", AdvertisementTaxConstants.APPLICATION_STATUS_APPROVED));
        return hoardingCriteria.list();

    }

  /*  @Override
    public List<AdvertisementPermitDetail> searchAdvertisementPermitDetailBySearchParamsAndStatusApproved(
            final AdvertisementPermitDetail advertisementPermitDetail) {

        final Criteria hoardingCriteria = entityManager.unwrap(Session.class)
                .createCriteria(AdvertisementPermitDetail.class, "permit")
                .createAlias("permit.advertisement", "advertisement")
                .createAlias("permit.status", "statuscode");
        hoardingCriteria.add(Restrictions.eq("advertisement.type", AdvertisementStructureType.PERMANENT));
        hoardingCriteria.add(Restrictions.eq("statuscode.code", "APPROVED"));

        if (advertisementPermitDetail.getAdvertisement() != null) {
            if (advertisementPermitDetail.getAdvertisement() != null
                    && advertisementPermitDetail.getAdvertisement().getAdvertisementNumber() != null
                    && !advertisementPermitDetail.getAdvertisement().getAdvertisementNumber().isEmpty())
                hoardingCriteria.add(Restrictions.eq("advertisement.advertisementNumber",
                        advertisementPermitDetail.getAdvertisement().getAdvertisementNumber()));
            if (advertisementPermitDetail.getAdvertisement().getLocality() != null)
                hoardingCriteria.add(Restrictions.eq("advertisement.locality.id",
                        advertisementPermitDetail.getAdvertisement().getLocality().getId()));
            if (advertisementPermitDetail.getAdvertisement().getWard() != null)
                hoardingCriteria.add(
                        Restrictions.eq("advertisement.ward.id", advertisementPermitDetail.getAdvertisement().getWard().getId()));
            if (advertisementPermitDetail.getAdvertisement().getCategory() != null)
                hoardingCriteria.add(Restrictions.eq("advertisement.category.id",
                        advertisementPermitDetail.getAdvertisement().getCategory().getId()));
            if (advertisementPermitDetail.getAdvertisement().getSubCategory() != null)
                hoardingCriteria.add(Restrictions.eq("advertisement.subCategory.id",
                        advertisementPermitDetail.getAdvertisement().getSubCategory().getId()));
        }
        return hoardingCriteria.list();

    }*/

 /*   @Override
    public List<AdvertisementPermitDetail> renewalSearchAdvertisementPermitDetailBySearchParams(
            final AdvertisementPermitDetail advertisementPermitDetail) {

        final Criteria hoardingCriteria = entityManager.unwrap(Session.class)
                .createCriteria(AdvertisementPermitDetail.class, "permit")
                .createAlias("permit.advertisement", "advertisement")
                .createAlias("advertisement.category", "category").createAlias("advertisement.subCategory", "subCategory")
                .createAlias("advertisement.revenueInspector", "revenueInspector");

        if (advertisementPermitDetail.getAdvertisement() != null) {
            if (advertisementPermitDetail.getAdvertisement() != null
                    && advertisementPermitDetail.getAdvertisement().getAdvertisementNumber() != null
                    && !advertisementPermitDetail.getAdvertisement().getAdvertisementNumber().isEmpty())
                hoardingCriteria.add(Restrictions.eq("advertisement.advertisementNumber",
                        advertisementPermitDetail.getAdvertisement().getAdvertisementNumber()));
            if (advertisementPermitDetail.getAdvertisement().getLocality() != null)
                hoardingCriteria.add(Restrictions.eq("advertisement.locality.id",
                        advertisementPermitDetail.getAdvertisement().getLocality().getId()));
            if (advertisementPermitDetail.getAdvertisement().getWard() != null)
                hoardingCriteria.add(
                        Restrictions.eq("advertisement.ward.id", advertisementPermitDetail.getAdvertisement().getWard().getId()));
            if (advertisementPermitDetail.getAdvertisement().getCategory() != null)
                hoardingCriteria.add(Restrictions.eq("advertisement.category.id",
                        advertisementPermitDetail.getAdvertisement().getCategory().getId()));
            if (advertisementPermitDetail.getAdvertisement().getSubCategory() != null)
                hoardingCriteria.add(Restrictions.eq("advertisement.subCategory.id",
                        advertisementPermitDetail.getAdvertisement().getSubCategory().getId()));
            if (advertisementPermitDetail.getAgency() != null && advertisementPermitDetail.getAgency().getId() != null)
                hoardingCriteria.add(Restrictions.eq("agency.id", advertisementPermitDetail.getAgency().getId()));
            if (advertisementPermitDetail.getAdvertisement().getStatus() != null)
                hoardingCriteria
                        .add(Restrictions.eq("advertisement.status", advertisementPermitDetail.getAdvertisement().getStatus()));
            if (advertisementPermitDetail.getAdvertisement().getRevenueInspector() != null)
                hoardingCriteria.add(Restrictions.eq("advertisement.revenueInspector.id",
                        advertisementPermitDetail.getAdvertisement().getRevenueInspector().getId()));
        }
        hoardingCriteria.add(Restrictions.eq("advertisement.status", AdvertisementStatus.ACTIVE));

        return hoardingCriteria.list();

    }*/
    
    
    @Override
    public List<AdvertisementPermitDetail> searchActiveAdvertisementPermitDetailBySearchParams(
            final AdvertisementPermitDetail advertisementPermitDetail) {

        final Criteria hoardingCriteria = entityManager.unwrap(Session.class)
                .createCriteria(AdvertisementPermitDetail.class, "permit")
                .createAlias("permit.advertisement", "advertisement")
                .createAlias("advertisement.category", "category").createAlias("advertisement.subCategory", "subCategory")
                .createAlias("advertisement.revenueInspector", "revenueInspector");
          
        if (advertisementPermitDetail.getAdvertisement() != null) {
            if (advertisementPermitDetail.getAdvertisement() != null
                    && advertisementPermitDetail.getAdvertisement().getAdvertisementNumber() != null
                    && !advertisementPermitDetail.getAdvertisement().getAdvertisementNumber().isEmpty())
                hoardingCriteria.add(Restrictions.eq("advertisement.advertisementNumber",
                        advertisementPermitDetail.getAdvertisement().getAdvertisementNumber()));
            if (advertisementPermitDetail.getAdvertisement().getLocality() != null)
                hoardingCriteria.add(Restrictions.eq("advertisement.locality.id",
                        advertisementPermitDetail.getAdvertisement().getLocality().getId()));
            if (advertisementPermitDetail.getAdvertisement().getWard() != null)
                hoardingCriteria.add(
                        Restrictions.eq("advertisement.ward.id", advertisementPermitDetail.getAdvertisement().getWard().getId()));
            if (advertisementPermitDetail.getAdvertisement().getCategory() != null)
                hoardingCriteria.add(Restrictions.eq("advertisement.category.id",
                        advertisementPermitDetail.getAdvertisement().getCategory().getId()));
            if (advertisementPermitDetail.getAdvertisement().getSubCategory() != null)
                hoardingCriteria.add(Restrictions.eq("advertisement.subCategory.id",
                        advertisementPermitDetail.getAdvertisement().getSubCategory().getId()));
            if (advertisementPermitDetail.getAgency() != null && advertisementPermitDetail.getAgency().getId() != null)
                hoardingCriteria.add(Restrictions.eq("agency.id", advertisementPermitDetail.getAgency().getId()));
            if (advertisementPermitDetail.getAdvertisement().getRevenueInspector() != null)
                hoardingCriteria.add(Restrictions.eq("advertisement.revenueInspector.id",
                        advertisementPermitDetail.getAdvertisement().getRevenueInspector().getId()));
            if(advertisementPermitDetail.getOwnerDetail() != null){
                hoardingCriteria.add(Restrictions.ilike("ownerDetail",advertisementPermitDetail.getOwnerDetail(),MatchMode.ANYWHERE));
                
            }
            if (advertisementPermitDetail.getAdvertisement().getStatus() != null)
                hoardingCriteria
                        .add(Restrictions.eq("advertisement.status", advertisementPermitDetail.getAdvertisement().getStatus()));
            else
            hoardingCriteria.add(Restrictions.eq("advertisement.status", AdvertisementStatus.ACTIVE));
            hoardingCriteria.add(Restrictions.eq("isActive",true));
        }

        return hoardingCriteria.list();
    }
}
