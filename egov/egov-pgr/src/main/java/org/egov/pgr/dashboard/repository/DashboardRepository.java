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

package org.egov.pgr.dashboard.repository;

import org.egov.pgr.config.properties.GrievanceApplicationSettings;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
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
public class DashboardRepository {

    private static final String WEEKLY_RESOLUTION_TREND = "pgr.comp.resolution.weekly.trend";
    private static final String WEEKLY_REGISTRATION_TREND = "pgr.comp.reg.weekly.trend";
    private static final String LAST_SIX_MONTH_AGGR = "pgr.comp.six.month.aggr";
    private static final String GRIEVANCE_TYPEWISE_PERCENTAGE = "pgr.comp.type.wise.perc";
    private static final String WARDWISE_PERFORMANCE = "pgr.wardwiseperformance";
    private static final String OPEN_GRIEVANCE_AGGR = "pgr.open.comp.aggr";
    private static final String BOUNDARYWISE_PERCENTAGE = "pgr.bndry.wise.perc";
    private static final String TOP_GRIEVANCE_BY_TYPE = "pgr.top.comp.types";
    private static final String CURRENT_MONTH_TOP_GRIEVANCE_BY_TYPE = "pgr.top.comp.types.current.month";
    private static final String SIX_MONTHS_WARDWISE_REGISTRATION = "pgr.comp.reg.six.month.wardwise";
    private static final String SIX_MONTHS_WARDWISE_RESOLUTION = "pgr.comp.redressed.six.month.wardwise";
    private static final String SIX_MONTHS_WARDWISE_GRIEVANCES_PER_PROPERTY = "pgr.comp.per.property.six.month.wardwise";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private GrievanceApplicationSettings grievanceApplicationSettings;

    public List<Object[]> fetchComplaintResolutionTrendBetween(Date fromDate, Date toDate) {
        return fetchDateRangeData(WEEKLY_RESOLUTION_TREND, fromDate, toDate);
    }

    public List<Object[]> fetchComplaintRegistrationTrendBetween(Date fromDate, Date toDate) {
        return fetchDateRangeData(WEEKLY_REGISTRATION_TREND, fromDate, toDate);
    }

    public List<Object[]> fetchMonthlyAggregateBetween(Date fromDate, Date toDate) {
        return fetchDateRangeData(LAST_SIX_MONTH_AGGR, fromDate, toDate);
    }

    public List<Object[]> fetchComplaintTypeWiseBetween(Date fromDate, Date toDate) {
        return fetchDateRangeData(GRIEVANCE_TYPEWISE_PERCENTAGE, fromDate, toDate);
    }

    public List<Object[]> fetchWardwisePerformanceTill(DateTime toDate) {
        NativeQuery qry = getQuery(WARDWISE_PERFORMANCE);
        qry.setParameter("thirteenDaysBefore", endOfGivenDate(toDate.minusDays(13)).toDate());
        qry.setParameter("fourteenDaysBefore", startOfGivenDate(toDate.minusDays(14)).toDate());
        qry.setParameter("currentDate", endOfGivenDate(toDate).toDate());
        return qry.list();
    }

    public Object[] fetchComplaintAgeing(String querykey, String wardName) {
        NativeQuery qry = getQuery(querykey);
        DateTime currentDate = new DateTime();
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

    public List<Object[]> fetchOpenComplaintAggregateBetween(DateTime fromDate, DateTime toDate) {
        return fetchDateRangeData(OPEN_GRIEVANCE_AGGR, fromDate.toDate(), toDate.toDate());
    }

    public List<Object[]> fetchComplaintsByComplaintTypeGroupByWard(Long complaintTypeId, DateTime fromDate,
                                                                    DateTime toDate) {
        NativeQuery qry = getQuery(BOUNDARYWISE_PERCENTAGE);
        qry.setParameter("fromDate", fromDate.toDate());
        qry.setParameter("toDate", toDate.toDate());
        qry.setParameter("compTypeId", complaintTypeId);
        return qry.list();
    }

    public List<Object[]> fetchTopComplaintsBetween(Date fromDate, Date toDate) {
        return fetchDateRangeData(TOP_GRIEVANCE_BY_TYPE, fromDate, toDate);
    }

    private List<Object[]> fetchDateRangeData(String query, Date fromDate, Date toDate) {
        NativeQuery qry = getQuery(query);
        qry.setParameter("fromDate", fromDate);
        qry.setParameter("toDate", toDate);
        return qry.list();
    }

    public List<Object[]> fetchGISCompPerPropertyWardWise() {
        return getQuery(SIX_MONTHS_WARDWISE_GRIEVANCES_PER_PROPERTY).list();
    }

    public List<Object[]> fetchGISCompRedressedWardWise() {
        return getQuery(SIX_MONTHS_WARDWISE_RESOLUTION).list();
    }

    public List<Object[]> fetchGISRegCompWardWise() {
        return getQuery(SIX_MONTHS_WARDWISE_REGISTRATION).list();
    }

    private NativeQuery getQuery(String sqlKey) {
        return entityManager.unwrap(Session.class)
                .createNativeQuery(grievanceApplicationSettings.getValue(sqlKey));
    }

    public List<Object[]> fetchTopComplaintsForCurrentMonthBetween(Date fromDate, Date toDate) {
        return fetchDateRangeData(CURRENT_MONTH_TOP_GRIEVANCE_BY_TYPE, fromDate, toDate);

    }
}
