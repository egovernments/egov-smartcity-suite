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
package org.egov.wtms.application.service;

import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.wtms.application.entity.DefaultersReport;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DefaultersWTReportService {

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @ReadOnly
    public List<DefaultersReport> getDefaultersReportDetails(final String fromAmount, final String toAmount,
            final String ward, final String topDefaulters, final int startsFrom, final int maxResults)
            throws ParseException {
        StringBuilder queryStr = new StringBuilder();
        queryStr = queryStr
                .append("select dcbinfo.hscno as \"hscNo\", dcbinfo.demand as \"demandId\", dcbinfo.username as \"ownerName\",wardboundary.name as \"wardName\", ")
                .append("dcbinfo.houseno as \"houseNo\" , localboundary.localname as \"locality\", dcbinfo.mobileno as \"mobileNumber\", ")
                .append("dcbinfo.arr_balance as \"arrearsDue\" ,  dcbinfo.curr_balance as \"currentDue\" , dcbinfo.arr_balance+dcbinfo.curr_balance as \"totalDue\" ")
                .append("from egwtr_mv_dcb_view dcbinfo INNER JOIN eg_boundary wardboundary on dcbinfo.wardid = wardboundary.id INNER JOIN eg_boundary localboundary on dcbinfo.locality = localboundary.id");

        if (Double.parseDouble(toAmount) == 0)
            queryStr.append(" where dcbinfo.arr_balance+dcbinfo.curr_balance >=" + fromAmount);
        else
            queryStr.append(" where dcbinfo.arr_balance+dcbinfo.curr_balance >=" + fromAmount
                    + " and dcbinfo.arr_balance+dcbinfo.curr_balance <=" + toAmount);
        queryStr.append(" and dcbinfo.connectionstatus = '" + ConnectionStatus.ACTIVE.toString() + "'");
        if (ward != null && !ward.isEmpty())
            queryStr.append(" and wardboundary.id = '" + ward + "'");

        queryStr.append(" and dcbinfo.demand IS NOT NULL");
        if (!topDefaulters.isEmpty())
            queryStr.append(" order by dcbinfo.arr_balance+dcbinfo.curr_balance desc ");
        final SQLQuery finalQuery = getCurrentSession().createSQLQuery(queryStr.toString());
        finalQuery.setFirstResult(startsFrom);
        finalQuery.setMaxResults(maxResults);
        finalQuery.setResultTransformer(new AliasToBeanResultTransformer(DefaultersReport.class));
        return finalQuery.list();
    }

    public long getTotalCount(final String fromAmount, final String toAmount, final String ward) throws ParseException {

        StringBuilder queryStr = new StringBuilder();
        queryStr = queryStr.append("select count(dcbinfo.hscno) from egwtr_mv_dcb_view dcbinfo")
                .append(" INNER JOIN eg_boundary wardboundary on dcbinfo.wardid = wardboundary.id INNER JOIN eg_boundary localboundary")
                .append(" on dcbinfo.locality = localboundary.id");
        if (Double.parseDouble(toAmount) == 0)
            queryStr.append(" where dcbinfo.arr_balance+dcbinfo.curr_balance >=" + fromAmount);
        else
            queryStr.append(" where dcbinfo.arr_balance+dcbinfo.curr_balance >=" + fromAmount
                    + " and dcbinfo.arr_balance+dcbinfo.curr_balance <=" + toAmount);
        queryStr.append(" and dcbinfo.connectionstatus = '" + ConnectionStatus.ACTIVE.toString() + "'");
        if (ward != null && !ward.isEmpty())
            queryStr.append(" and wardboundary.id = '" + ward + "'");
        final SQLQuery finalQuery = getCurrentSession().createSQLQuery(queryStr.toString());
        final Long count = ((BigInteger) finalQuery.uniqueResult()).longValue();
        return count;
    }

    public long getTotalCountFromLimit(final String fromAmount, final String toAmount, final String ward,
            final String topDefaulters) throws ParseException {

        StringBuilder queryStr = new StringBuilder();
        queryStr = queryStr.append("select count(*) from (select * from egwtr_mv_dcb_view dcbinfo")
                .append(" INNER JOIN eg_boundary wardboundary on dcbinfo.wardid = wardboundary.id INNER JOIN eg_boundary localboundary")
                .append(" on dcbinfo.locality = localboundary.id");
        if (Double.parseDouble(toAmount) == 0)
            queryStr.append(" where dcbinfo.arr_balance+dcbinfo.curr_balance >=" + fromAmount);
        else
            queryStr.append(" where dcbinfo.arr_balance+dcbinfo.curr_balance >=" + fromAmount
                    + " and dcbinfo.arr_balance+dcbinfo.curr_balance <=" + toAmount);
        queryStr.append(" and dcbinfo.connectionstatus = '" + ConnectionStatus.ACTIVE.toString() + "'");
        if (ward != null && !ward.isEmpty())
            queryStr.append(" and wardboundary.id = '" + ward + "'");
        if (!topDefaulters.isEmpty())
            queryStr.append(" limit " + topDefaulters);
        queryStr.append(") as count");
        final SQLQuery finalQuery = getCurrentSession().createSQLQuery(queryStr.toString());
        final Long count = ((BigInteger) finalQuery.uniqueResult()).longValue();
        return count;
    }
}