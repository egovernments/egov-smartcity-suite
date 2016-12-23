/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2016  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *          1) All versions of this program, verbatim or modified must carry this
 *             Legal Notice.
 *
 *          2) Any misrepresentation of the origin of the material is prohibited. It
 *             is required that all modified versions of this material be marked in
 *             reasonable ways as different from the original version.
 *
 *          3) This license does not grant any rights to any user of the program
 *             with regards to rights under trademark law for use of the trade names
 *             or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.service.reports;

import org.egov.pgr.entity.dto.RouterEscalationForm;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Service
@Transactional(readOnly = true)
public class RouterEscalationService {
    @PersistenceContext
    private EntityManager entityManager;

    public List<RouterEscalationForm> search(final RouterEscalationForm routerEscalationForm) {
        final SQLQuery finalQuery = prepareQuery(routerEscalationForm);
        finalQuery.setResultTransformer(new AliasToBeanResultTransformer(RouterEscalationForm.class));
        if (routerEscalationForm.getCategory() != null)
            finalQuery.setParameter("categoryId", routerEscalationForm.getCategory());
        if (routerEscalationForm.getComplainttype() != null)
            finalQuery.setParameter("ctnameId", routerEscalationForm.getComplainttype());
        if (routerEscalationForm.getBoundary() != null)
            finalQuery.setParameter("bndryId", routerEscalationForm.getBoundary());
        if (routerEscalationForm.getPosition() != null)
            finalQuery.setParameter("posId", routerEscalationForm.getPosition());
        return finalQuery.list();
    }

    private SQLQuery prepareQuery(final RouterEscalationForm routerEscalationForm) {
        StringBuilder whereQry = new StringBuilder();
        final StringBuilder selectQry = new StringBuilder("select \"ctname\", \"bndryname\", \"routerposname\", \"esclvl1posname\", \"esclvl2posname\", \"esclvl3posname\" from pgr_router_escalation_view where 1=1 ");
        if (routerEscalationForm.getCategory() != null)
            whereQry = whereQry.append(" and categoryid = :categoryId");
        if (routerEscalationForm.getComplainttype() != null)
            whereQry = whereQry.append(" and ctid = :ctnameId");
        if (routerEscalationForm.getBoundary() != null)
            whereQry = whereQry.append(" and bndryid = :bndryId");
        if (routerEscalationForm.getPosition() != null)
            whereQry = whereQry.append(" and routerpos = :posId");
        return entityManager.unwrap(Session.class).createSQLQuery(selectQry.append(whereQry).toString());
    }

}
