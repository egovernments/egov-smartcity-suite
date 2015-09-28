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
package org.egov.adtax.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.adtax.entity.AgencyWiseResult;
import org.egov.adtax.entity.Hoarding;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

@SuppressWarnings("all")
public class HoardingRepositoryImpl implements HoardingRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Hoarding> fetchHoardingsLike(final Hoarding hoarding) {
        return entityManager.unwrap(Session.class).createCriteria(Hoarding.class).add(Example.create(hoarding)).list();
    }

    @Override
    public List<Object[]> fetchHoardingsBySearchType(final Hoarding hoarding, final String searchType) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Hoarding.class, "hoarding")
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
