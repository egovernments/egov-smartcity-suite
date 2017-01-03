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

package org.egov.tl.repository;

import org.egov.tl.entity.FeeMatrix;
import org.egov.tl.entity.FeeMatrixDetail;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.persistence.criteria.*;

import java.util.ArrayList;

import java.util.List;

public class FeeMatrixDetailRepositoryImpl implements FeeMatrixDetailRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public FeeMatrixDetail findFeeDetailList(final FeeMatrix feeMatrix, final Integer uom) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<FeeMatrixDetail> cq = cb
                .createQuery(FeeMatrixDetail.class);

        Root<FeeMatrixDetail> feeMatrixDetailRoot = cq.from(FeeMatrixDetail.class);
        List<Predicate> predicates = new ArrayList<>();
        if (feeMatrix != null)
            predicates.add(cb.equal(feeMatrixDetailRoot.get("feeMatrix"), feeMatrix));
        if (uom != null) {
            predicates.add(cb.lessThan(feeMatrixDetailRoot.get("uomFrom"), uom));
            predicates.add(cb.greaterThanOrEqualTo(feeMatrixDetailRoot.get("uomTo"), uom));
        }
        cq.select(feeMatrixDetailRoot).where(predicates.toArray(new Predicate[]{}));
        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    public List<FeeMatrixDetail> findByParams(final Long licenseCategory, final Long subCategory, final Long financialYear) {
        final Criteria feeMatrixDetailCriteria = entityManager.unwrap(Session.class)
                .createCriteria(FeeMatrixDetail.class, "feeMatrixDetail")
                .createAlias("feeMatrixDetail.feeMatrix", "feeMatrix")
                .createAlias("feeMatrixDetail.feeMatrix.licenseCategory", "licenseCategory")
                .createAlias("feeMatrixDetail.feeMatrix.subCategory", "subCategory")
                .createAlias("feeMatrixDetail.feeMatrix.financialYear", "financialYear");
        if (licenseCategory != null)
            feeMatrixDetailCriteria.add(Restrictions.eq("licenseCategory.id", licenseCategory));
        if (subCategory != null)
            feeMatrixDetailCriteria.add(Restrictions.eq("subCategory.id", subCategory));
        if (financialYear != null)
            feeMatrixDetailCriteria.add(Restrictions.eq("financialYear.id", financialYear));

        return feeMatrixDetailCriteria.list();
    }

}