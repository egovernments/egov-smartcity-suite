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


package org.egov.deduction.dao;

import org.egov.deduction.model.Generalledgerdetail;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional(readOnly = true)
public class GeneralledgerdetailHibernateDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Generalledgerdetail update(final Generalledgerdetail entity) {
        getCurrentSession().update(entity);
        return entity;
    }

    @Transactional
    public Generalledgerdetail create(final Generalledgerdetail entity) {
        getCurrentSession().persist(entity);
        return entity;
    }

    @Transactional
    public void delete(Generalledgerdetail entity) {
        getCurrentSession().delete(entity);
    }

    public Generalledgerdetail findById(Number id, boolean lock) {
        return (Generalledgerdetail) getCurrentSession().load(Generalledgerdetail.class, id);
    }

    public List<Generalledgerdetail> findAll() {
        return (List<Generalledgerdetail>) getCurrentSession().createQuery("from Generalledgerdetail").list();
    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }


    public List<Generalledgerdetail> getGeneralledgerdetailByFilterBy(final Integer voucherHeaderId,
                                                                      final Integer purposeId) {
        return getCurrentSession()
                .createQuery(new StringBuilder(" from Generalledgerdetail gld")
                        .append(" where gld.generalledger.voucherHeaderId = :voucherHeaderId")
                        .append(" and gld.generalledger.glcodeId in (select id from CChartOfAccounts where purposeId = :purposeId) ").toString())
                .setParameter("voucherHeaderId", voucherHeaderId, IntegerType.INSTANCE)
                .setParameter("purposeId", purposeId, IntegerType.INSTANCE)
                .list();
    }

    public List<Generalledgerdetail> getGeneralledgerdetailByGlCodeId(final Integer glcodeId) {
        return getCurrentSession().createQuery(" from Generalledgerdetail gld where gld.generalledger.glcodeId = :glcodeId")
                .setParameter("glcodeId", glcodeId, IntegerType.INSTANCE)
                .list();
    }

    public List<Generalledgerdetail> getGeneralledgerdetailByVhId(final Integer vhId) {
        return getCurrentSession().createQuery(" from Generalledgerdetail gld where gld.generalledger.voucherHeaderId = :vhId")
                .setParameter("vhId", vhId, IntegerType.INSTANCE)
                .list();
    }
}