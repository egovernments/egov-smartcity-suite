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

package org.egov.dao.bills;

import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.model.bills.EgBilldetails;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


@Repository
@Transactional(readOnly = true)
public class EgBilldetailsHibernateDAO implements EgBilldetailsDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public EgBilldetails update(final EgBilldetails entity) {
        getCurrentSession().update(entity);
        return entity;
    }

    @Transactional
    public EgBilldetails create(final EgBilldetails entity) {
        getCurrentSession().persist(entity);
        return entity;
    }

    @Transactional
    public void delete(EgBilldetails entity) {
        getCurrentSession().delete(entity);
    }

    public EgBilldetails findById(Number id, boolean lock) {
        return getCurrentSession().load(EgBilldetails.class, id);
    }

    public List<EgBilldetails> findAll() {
        return getCurrentSession().createQuery("from EgBilldetails").list();
    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public BigDecimal getOtherBillsAmount(final Long minGlCodeId, final Long maxGlCodeId, final Long majGlCodeId,
                                          final String finYearID, final String functionId, final String schemeId, final String subSchemeId,
                                          final String asOnDate, final String billType) {
        final StringBuilder qryStr = new StringBuilder();
        final BigDecimal result = new BigDecimal("0.00");
        try {
            qryStr.append("select sum(bd.debitamount)")
                    .append(" from EgBilldetails bd, EgBillregister br, EgBillregistermis brm")
                    .append(" where br.id = bd.egBillregister.id and br.id = brm.egBillregister.id and bd.egBillregister.id = brm.egBillregister.id")
                    .append(" and brm.financialyear.id = :finYearID and br.expendituretype not in (:billType) and br.status.id not in")
                    .append(" (SELECT es.id FROM EgwStatus es WHERE UPPER(es.description) LIKE '%CANCELLED%') ");
            if (isNotBlank(asOnDate))
                qryStr.append(" and br.billdate <= :asOnDate");

            if (isNotBlank(functionId))
                qryStr.append(" and bd.functionid = :functionId");

            if (isNotBlank(schemeId) && isBlank(subSchemeId))
                qryStr.append(" and brm.scheme = :schemeId");

            if (isNotBlank(schemeId) && isNotBlank(subSchemeId))
                qryStr.append(" and brm.scheme = :schemeId and brm.subScheme = :subSchemeId");

            if (minGlCodeId != 0 && maxGlCodeId != 0)
                qryStr.append(" and bd.glcodeid between :minGlCodeId and :maxGlCodeId");
            else if (maxGlCodeId != 0)
                qryStr.append(" and bd.glcodeid = :maxGlCodeId");
            else if (majGlCodeId != 0)
                qryStr.append(" and bd.glcodeid = :majGlCodeId");

            Query qry = getCurrentSession().createQuery(qryStr.toString());
            if (isNotBlank(functionId))
                qry.setParameter("functionId", functionId, StringType.INSTANCE);
            if (isNotBlank(schemeId) && isBlank(subSchemeId))
                qry.setParameter("schemeId", schemeId, StringType.INSTANCE);
            if (isNotBlank(schemeId) && isNotBlank(subSchemeId)) {
                qry.setParameter("schemeId", schemeId, StringType.INSTANCE);
                qry.setParameter("subSchemeId", subSchemeId, StringType.INSTANCE);
            }
            if (isNotBlank(asOnDate))
                qry.setParameter("asOnDate", asOnDate, StringType.INSTANCE);
            if (minGlCodeId != 0 && maxGlCodeId != 0) {
                qry.setParameter("minGlCodeId", minGlCodeId, LongType.INSTANCE);
                qry.setParameter("maxGlCodeId", maxGlCodeId, LongType.INSTANCE);
            } else if (maxGlCodeId != 0)
                qry.setParameter("maxGlCodeId", maxGlCodeId, LongType.INSTANCE);
            else if (majGlCodeId != 0)
                qry.setParameter("majGlCodeId", majGlCodeId, LongType.INSTANCE);
            qry.setParameter("finYearID", finYearID, StringType.INSTANCE);
            qry.setParameter("billType", billType, StringType.INSTANCE);
            Object res = qry.uniqueResult();
            return res != null ? new BigDecimal(res.toString()) : result;
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Error occurred while getting other bill amount", e);
        }
    }

    @Override
    public EgBilldetails getBillDetails(final Long billId, final List glcodeIdList) throws Exception {

        try {
            StringBuilder qryStr = new StringBuilder("from EgBilldetails bd where bd.creditamount > 0 AND bd.glcodeid IN (:glcodeIds) AND billid = :billId ");
            return (EgBilldetails) getCurrentSession().createQuery(qryStr.toString())
                    .setParameterList("glcodeIds", glcodeIdList)
                    .setParameter("billId", billId, LongType.INSTANCE)
                    .uniqueResult();
        } catch (final Exception e) {
            throw new ApplicationException(e.getMessage());
        }
    }
}