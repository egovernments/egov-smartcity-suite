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

import org.egov.commons.EgwStatus;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EgwStatusHibernateDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return this.entityManager.unwrap(Session.class);
    }

    public List<EgwStatus> getEgwStatusFilterByStatus(ArrayList<Integer> statusId) {
        Query qry = this.getCurrentSession().createQuery("from EgwStatus egs where egs.id in (:statusId)  order by orderId");
        qry.setParameterList("statusId", statusId);
        return qry.list();
    }

    /**
     * @param moduleType Module type
     * @param statusCode Status code
     * @return EgwStatus object for given module type and status code
     */
    public EgwStatus getStatusByModuleAndCode(String moduleType, String code) {
        Query qry = this.getCurrentSession().createQuery("from EgwStatus S where S.moduletype =:moduleType and S.code =:code");
        qry.setString("moduleType", moduleType);
        qry.setString("code", code);
        return (EgwStatus) qry.uniqueResult();
    }

    public List<EgwStatus> getStatusByModule(String moduleType) {
        Query qry = this.getCurrentSession().createQuery("from EgwStatus S where S.moduletype =:moduleType  order by orderId");
        qry.setString("moduleType", moduleType);
        return qry.list();
    }

    /**
     * @param moduleType Module type
     * @param codeList List of status codes
     * @return List of all EgwStatus objects filtered by given module type and list of status codes
     */
    public List<EgwStatus> getStatusListByModuleAndCodeList(String moduleType, List codeList) {
        Query qry = this.getCurrentSession().createQuery("from EgwStatus S where S.moduletype =:moduleType and S.code in(:codeList)  order by orderId");
        qry.setString("moduleType", moduleType);
        qry.setParameterList("codeList", codeList);
        return qry.list();
    }

    public EgwStatus getEgwStatusByCode(String code) {
        Query qry = this.getCurrentSession().createQuery("from EgwStatus S where S.code =:code ");
        qry.setString("code", code);
        return (EgwStatus) qry.uniqueResult();
    }

    public EgwStatus findById(final Integer integer, final boolean b) {
        return (EgwStatus) getCurrentSession().get(EgwStatus.class, integer);
    }

}
