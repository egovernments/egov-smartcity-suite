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
package org.egov.commons.dao;

import org.egov.commons.EgwTypeOfWork;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class EgwTypeOfWorkHibernateDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public List getAllTypeOfWork() {
        return getCurrentSession().createQuery("from EgwTypeOfWork typeOfWork order by upper(code)").list();
    }

    public List getAllTypeOfWorkOrderByDesc() {
        return getCurrentSession().createQuery("from EgwTypeOfWork typeOfWork order by upper(description)").list();
    }

    public EgwTypeOfWork getTypeOfWorkById(final Long id) {
        final Query qry = getCurrentSession().createQuery("from EgwTypeOfWork typeOfWork where typeOfWork.id =:id");
        qry.setLong("id", id);
        return (EgwTypeOfWork) qry.uniqueResult();
    }

    public List getSubTypeOfWorkByParentId(final Long id) {
        final Query query = getCurrentSession().createQuery(
                "from EgwTypeOfWork typeOfWork where typeOfWork.parentid =:id and egPartytype.description='Contractor' order by upper(description)");
        query.setLong("id", id);
        return query.list();
    }

    public EgwTypeOfWork findByCode(final String code) {
        final Query qry = getCurrentSession().createQuery("from EgwTypeOfWork typeOfWork where upper(typeOfWork.code) =:code");
        qry.setString("code", code.toUpperCase().trim());
        return (EgwTypeOfWork) qry.uniqueResult();
    }

    /**
     * @param code
     * @param parentCode
     * @param description
     * @return list of EgwTypeOfWork filtered by optional conditions
     */
    public List<EgwTypeOfWork> getTypeOfWorkDetailFilterBy(final String code, final String parentCode, final String description) {
        final StringBuffer qryStr = new StringBuffer();
        qryStr.append("select distinct typeOfWork From EgwTypeOfWork typeOfWork where typeOfWork.createdby is not null ");
        if (code != null && !code.equals(""))
            qryStr.append(" and (upper(typeOfWork.code) like :code)");
        if (parentCode != null && !parentCode.equals(""))
            qryStr.append(" and (upper(typeOfWork.parentid.code) like :parentCode)");
        if (description != null && !description.equals(""))
            qryStr.append(" and (upper(typeOfWork.description) like :description)");
        final Query qry = getCurrentSession().createQuery(qryStr.toString());
        if (code != null && !code.equals(""))
            qry.setString("code", "%" + code.toUpperCase().trim() + "%");
        if (parentCode != null && !parentCode.equals(""))
            qry.setString("parentCode", "%" + parentCode.toUpperCase().trim() + "%");
        if (description != null && !description.equals(""))
            qry.setString("description", "%" + description.toUpperCase().trim() + "%");

        return qry.list();
    }

    /**
     * @param code
     * @param parentCode
     * @param description
     * @param partyTypeCode
     * @return list of EgwTypeOfWork filtered by optional conditions
     */
    public List<EgwTypeOfWork> getTypeOfWorkDetailFilterByParty(final String code, final String parentCode,
            final String description, final String partyTypeCode) {
        final StringBuffer qryStr = new StringBuffer();
        qryStr.append("select distinct typeOfWork From EgwTypeOfWork typeOfWork where typeOfWork.egPartytype is not null ");

        if (code != null && !code.equals(""))
            qryStr.append(" and (upper(typeOfWork.code) like :code)");
        if (parentCode != null && !parentCode.equals(""))
            qryStr.append(" and (upper(typeOfWork.parentid.code) like :parentCode)");
        if (description != null && !description.equals(""))
            qryStr.append(" and (upper(typeOfWork.description) like :description)");
        if (partyTypeCode != null && !partyTypeCode.equals(""))
            qryStr.append(" and (upper(typeOfWork.egPartytype.code) like :partyTypeCode)");
        final Query qry = getCurrentSession().createQuery(qryStr.toString());
        if (code != null && !code.equals(""))
            qry.setString("code", "%" + code.toUpperCase().trim() + "%");
        if (parentCode != null && !parentCode.equals(""))
            qry.setString("parentCode", "%" + parentCode.toUpperCase().trim() + "%");
        if (description != null && !description.equals(""))
            qry.setString("description", "%" + description.toUpperCase().trim() + "%");
        if (partyTypeCode != null && !partyTypeCode.equals(""))
            qry.setString("partyTypeCode", "%" + partyTypeCode.toUpperCase().trim() + "%");

        return qry.list();
    }

    public List<EgwTypeOfWork> getAllParentOrderByCode() {
        return getCurrentSession()
                .createQuery(
                        "from EgwTypeOfWork etw1 where etw1.parentid is null and etw1.id in (select etw2.parentid from EgwTypeOfWork etw2 where etw2.parentid = etw1.id) order by upper(code)")
                .list();
    }

    public List<EgwTypeOfWork> getTypeOfWorkForPartyTypeContractor() {
        return getCurrentSession()
                .createQuery(
                        "from EgwTypeOfWork etw1 where etw1.parentid is null and etw1.id in (select etw2.parentid from EgwTypeOfWork etw2 where etw2.parentid = etw1.id) and egPartytype.description='Contractor' order by upper(description)")
                .list();
    }

    public List findAllParentPartyType() {
        return getCurrentSession()
                .createQuery(
                        "from EgwTypeOfWork tw where tw.parentid is null and tw.egPartytype is not null order by upper(code)")
                .list();
    }

    public List findAllChildPartyType() {
        return getCurrentSession()
                .createQuery(
                        "from EgwTypeOfWork tw where tw.parentid is not null and tw.egPartytype is not null order by upper(code)")
                .list();
    }

}
