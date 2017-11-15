/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2017  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.tl.repository;

import org.egov.tl.entity.contracts.BaseRegisterRequest;
import org.egov.tl.entity.view.BaseRegister;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class BaseRegisterReportRepositoryImpl implements BaseRegisterReportRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Object[] findByBaseRegisterRequest(final BaseRegisterRequest baseRegisterRequest) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        final Root<BaseRegister> root = criteriaQuery.from(BaseRegister.class);
        final List<Predicate> predicates = new ArrayList<>();
        if (baseRegisterRequest.getCategoryId() != null)
            predicates.add(criteriaBuilder.equal(root.get("categoryId"), baseRegisterRequest.getCategoryId()));
        if (baseRegisterRequest.getSubCategoryId() != null)
            predicates.add(criteriaBuilder.equal(root.get("subCategoryId"), baseRegisterRequest.getSubCategoryId()));
        if (baseRegisterRequest.getWardId() != null)
            predicates.add(criteriaBuilder.equal(root.get("wardId"), baseRegisterRequest.getWardId()));
        if ("Defaulters".equals(baseRegisterRequest.getFilterName()))
            predicates.add(criteriaBuilder.or(criteriaBuilder.greaterThan(root.get("arrearlicensefee"), 0),
                    criteriaBuilder.greaterThan(root.get("arrearpenaltyfee"), 0),
                    criteriaBuilder.greaterThan(root.get("curlicensefee"), 0),
                    criteriaBuilder.greaterThan(root.get("curpenaltyfee"), 0)));
        if (baseRegisterRequest.getStatusId() != null)
            predicates.add(criteriaBuilder.equal(root.get("statusId"), baseRegisterRequest.getStatusId()));
        if (baseRegisterRequest.getStatusId() == null)
            predicates.add(criteriaBuilder.and(criteriaBuilder.notLike(root.get("statusname"), "Cancelled"),
                    criteriaBuilder.notLike(root.get("statusname"), "Suspended")));

        criteriaQuery.multiselect(criteriaBuilder.sumAsLong(root.get("arrearlicensefee")),
                criteriaBuilder.sumAsLong(root.get("arrearpenaltyfee")),
                criteriaBuilder.sumAsLong(root.get("curlicensefee")),
                criteriaBuilder.sumAsLong(root.get("curpenaltyfee"))).where(predicates.toArray(new Predicate[]{}));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }
}
