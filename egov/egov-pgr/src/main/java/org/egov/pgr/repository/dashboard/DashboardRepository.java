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

package org.egov.pgr.repository.dashboard;

import org.egov.pgr.config.properties.PgrApplicationProperties;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

import static org.egov.infra.utils.DateUtils.endOfGivenDate;
import static org.egov.infra.utils.DateUtils.startOfGivenDate;

@Repository
@SuppressWarnings("all")
public class DashboardRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PgrApplicationProperties pgrApplicationProperties;

    public List<Object[]> fetchComplaintResolutionTrendBetween(final Date fromDate, final Date toDate) {
        return fetchDateRangeData("pgr.comp.resolution.weekly.trend", fromDate, toDate);
    }

    public List<Object[]> fetchComplaintRegistrationTrendBetween(final Date fromDate, final Date toDate) {
        return fetchDateRangeData("pgr.comp.reg.weekly.trend", fromDate, toDate);
    }

    public List<Object[]> fetchMonthlyAggregateBetween(final Date fromDate, final Date toDate) {
        return fetchDateRangeData("pgr.comp.six.month.aggr", fromDate, toDate);
    }

    public List<Object[]> fetchComplaintTypeWiseBetween(final Date fromDate, final Date toDate) {
        return fetchDateRangeData("pgr.comp.type.wise.perc", fromDate, toDate);
    }

    public List<Object[]> fetchWardwisePerformanceTill(final DateTime toDate) {
        final SQLQuery qry = getQuery("pgr.wardwiseperformance");
        qry.setParameter("thirteenDaysBefore", endOfGivenDate(toDate.minusDays(13)).toDate());
        qry.setParameter("fourteenDaysBefore", startOfGivenDate(toDate.minusDays(14)).toDate());
        qry.setParameter("currentDate", endOfGivenDate(toDate).toDate());
        return qry.list();
    }

    public Object[] fetchComplaintAgeing(final String querykey, final String wardName) {
        final SQLQuery qry = getQuery(querykey);
        final DateTime currentDate = new DateTime();
        qry.setParameter("grtthn90", endOfGivenDate(currentDate.minusDays(90)).toDate());
        qry.setParameter("lsthn90", endOfGivenDate(currentDate.minusDays(90)).toDate());
        qry.setParameter("grtthn45", endOfGivenDate(currentDate.minusDays(45)).toDate());
        qry.setParameter("grtthn15", endOfGivenDate(currentDate.minusDays(15)).toDate());
        qry.setParameter("lsthn45", endOfGivenDate(currentDate.minusDays(45)).toDate());
        qry.setParameter("lsthn15", endOfGivenDate(currentDate.minusDays(15)).toDate());
        qry.setParameter("currdate", endOfGivenDate(currentDate).toDate());
        if (wardName != null)
            qry.setParameter("wardName", wardName);
        return (Object[]) qry.uniqueResult();
    }

    public List<Object[]> fetchOpenComplaintAggregateBetween(final DateTime fromDate, final DateTime toDate) {
        return fetchDateRangeData("pgr.open.comp.aggr", fromDate.toDate(), toDate.toDate());
    }

    public List<Object[]> fetchComplaintsByComplaintTypeGroupByWard(final Long complaintTypeId, final DateTime fromDate,
            final DateTime toDate) {
        final SQLQuery qry = getQuery("pgr.bndry.wise.perc");
        qry.setParameter("fromDate", fromDate.toDate());
        qry.setParameter("toDate", toDate.toDate());
        qry.setParameter("compTypeId", complaintTypeId);
        return qry.list();
    }

    public List<Object[]> fetchTopComplaintsBetween(final Date fromDate, final Date toDate) {
        return fetchDateRangeData("pgr.top.comp.types", fromDate, toDate);
    } 

    private List<Object[]> fetchDateRangeData(final String query, final Date fromDate, final Date toDate) {
        final SQLQuery qry = getQuery(query);
        qry.setParameter("fromDate", fromDate);
        qry.setParameter("toDate", toDate);
        return qry.list();
    }

    public List<Object[]> fetchGISCompPerPropertyWardWise() {
        final SQLQuery qry = getQuery("pgr.comp.per.property.six.month.wardwise");
        return qry.list();
    }

    public List<Object[]> fetchGISCompRedressedWardWise() {
        final SQLQuery qry = getQuery("pgr.comp.redressed.six.month.wardwise");
        return qry.list();
    }

    public List<Object[]> fetchGISRegCompWardWise() {
        final SQLQuery qry = getQuery("pgr.comp.reg.six.month.wardwise");
        return qry.list();
    }

    private SQLQuery getQuery(final String sqlKey) {
        return entityManager.unwrap(Session.class)
                .createSQLQuery(pgrApplicationProperties.getValue(sqlKey));
    }

    public List<Object[]> fetchTopComplaintsForCurrentMonthBetween(final Date fromDate, final Date toDate) {
        return fetchDateRangeData("pgr.top.comp.types.current.month", fromDate, toDate);

    }

}
