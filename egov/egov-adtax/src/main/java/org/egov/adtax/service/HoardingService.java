/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.adtax.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.adtax.entity.AgencyWiseResult;
import org.egov.adtax.entity.Hoarding;
import org.egov.adtax.repository.HoardingRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HoardingService {

    private final HoardingRepository hoardingRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }
    @Autowired
    private AdvertisementDemandService advertisementDemandService;
    
    @Autowired
    public HoardingService(final HoardingRepository hoardingRepository) {
        this.hoardingRepository = hoardingRepository;
    }

    @Transactional
    public Hoarding createHoarding(final Hoarding hoarding) {
        
        if(hoarding!=null && hoarding.getId()==null)
            hoarding.setDemandId(advertisementDemandService.createDemand(hoarding));
        
        
        return hoardingRepository.save(hoarding);
    }

    public List<Object[]> searchBySearchType(final Hoarding hoarding, final String searchType) {
        final Criteria criteria = getCurrentSession().createCriteria(Hoarding.class, "hoarding")
                .createAlias("hoarding.adminBoundry", "ward").createAlias("hoarding.adminBoundry.parent", "zone")
                .createAlias("hoarding.category", "category").createAlias("hoarding.subCategory", "subCategory")
                .createAlias("hoarding.revenueInspector", "revenueInspector").createAlias("hoarding.agency", "agency");
        if ("agency".equalsIgnoreCase(searchType))
            criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("agency"), "agency")
                    .add(Projections.rowCount(), "count")).setResultTransformer(Transformers.aliasToBean(AgencyWiseResult.class));
        if (null != hoarding.getHoardingNumber() && !hoarding.getHoardingNumber().isEmpty())
            criteria.add(Restrictions.eq("hoarding.hoardingNumber", hoarding.getHoardingNumber()));
        if (null != hoarding.getAdminBoundry().getParent())
            criteria.add(Restrictions.eq("zone.id", hoarding.getAdminBoundry().getParent().getId()));
        if (null != hoarding.getAdminBoundry().getId())
            criteria.add(Restrictions.eq("ward.id", hoarding.getAdminBoundry().getId()));
        if (null != hoarding.getCategory())
            criteria.add(Restrictions.eq("category.id", hoarding.getCategory().getId()));
        if (null != hoarding.getSubCategory())
            criteria.add(Restrictions.eq("subCategory.id", hoarding.getSubCategory().getId()));
        if (null != hoarding.getAgency().getName() && !hoarding.getAgency().getName().isEmpty())
            criteria.add(Restrictions.eq("agency.name", hoarding.getAgency().getName()));
        if (null != hoarding.getStatus())
            criteria.add(Restrictions.eq("hoarding.status", hoarding.getStatus()));
        if (null != hoarding.getRevenueInspector())
            criteria.add(Restrictions.eq("revenueInspector.id", hoarding.getRevenueInspector().getId()));
        return criteria.list();
    }

}
