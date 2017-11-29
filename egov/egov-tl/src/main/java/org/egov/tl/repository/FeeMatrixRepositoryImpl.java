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

package org.egov.tl.repository;

import org.egov.tl.entity.FeeMatrix;
import org.egov.tl.entity.FeeType;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseAppType;
import org.egov.tl.entity.NatureOfBusiness;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class FeeMatrixRepositoryImpl implements FeeMatrixRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FeeMatrix> searchFeeMatrix(Long categoryId, Long subcategoryId, Long financialYearId) {
        Criteria feeMatrixCriteria = entityManager.unwrap(Session.class)
                .createCriteria(FeeMatrix.class, "feeMatrix")
                .createAlias("feeMatrix.licenseCategory", "licenseCategory")
                .createAlias("feeMatrix.subCategory", "subCategory")
                .createAlias("feeMatrix.financialYear", "financialYear")
                .createAlias("feeMatrix.licenseAppType", "licenseAppType")
                .createAlias("feeMatrix.natureOfBusiness", "natureOfBusiness");
        if (categoryId != null)
            feeMatrixCriteria.add(Restrictions.eq("licenseCategory.id", categoryId));
        if (subcategoryId != null)
            feeMatrixCriteria.add(Restrictions.eq("subCategory.id", subcategoryId));
        if (financialYearId != null)
            feeMatrixCriteria.add(Restrictions.eq("financialYear.id", financialYearId));
        feeMatrixCriteria.addOrder(Order.desc("financialYear.id")).addOrder(Order.asc("licenseAppType.name"))
                .addOrder(Order.asc("natureOfBusiness.name"));
        return feeMatrixCriteria.list();
    }

    @Override
    public Optional<FeeMatrix> findFeeMatrix(License license, NatureOfBusiness natureOfBusiness,
                                             FeeType feeType, LicenseAppType appType, Date effectiveDate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FeeMatrix> criteriaQuery = criteriaBuilder.createQuery(FeeMatrix.class);
        Root<FeeMatrix> feeMatrixRoot = criteriaQuery.from(FeeMatrix.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(feeMatrixRoot.get("natureOfBusiness"), natureOfBusiness));
        predicates.add(criteriaBuilder.equal(feeMatrixRoot.get("licenseCategory"), license.getCategory()));
        predicates.add(criteriaBuilder.equal(feeMatrixRoot.get("subCategory"), license.getTradeName()));
        predicates.add(criteriaBuilder.equal(feeMatrixRoot.get("licenseAppType"), appType));
        predicates.add(criteriaBuilder.equal(feeMatrixRoot.get("feeType"), feeType));
        predicates.add(criteriaBuilder.lessThanOrEqualTo(feeMatrixRoot.get("effectiveFrom"), effectiveDate));
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(feeMatrixRoot.get("effectiveTo"), effectiveDate));
        criteriaQuery.select(feeMatrixRoot).where(predicates.toArray(new Predicate[]{}));
        return entityManager.createQuery(criteriaQuery).getResultList().stream().findFirst();
    }


}
