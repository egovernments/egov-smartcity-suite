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

package org.egov.pgr.service.reports;

import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINTTYPE_SELECT_QRY;
import static org.egov.pgr.utils.constants.PGRConstants.DEPT_SELECT_QRY;
import static org.egov.pgr.utils.constants.PGRConstants.FROMDATE;
import static org.egov.pgr.utils.constants.PGRConstants.USER_SELECT_QRY;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.utils.DateUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DrillDownReportService {

    @PersistenceContext
    private EntityManager entityManager;

    public static final String NOT_AVAILABLE = "NOT AVAILABLE";
    public static final String LOCALITY_SELECT_QRY = "select coalesce(cbndry.name,'GIS_LOCATION') as name, ";

    @ReadOnly
    public SQLQuery getDrillDownReportQuery(final Date fromDate, final Date toDate,
            final String complaintDateType, final String groupBy, final String department, final String boundary,
            final String complainttype, final String selecteduser, final String locality) {

        final StringBuilder query = new StringBuilder();
        if (StringUtils.isNotBlank(boundary)) {
            if (StringUtils.isNotBlank(locality)) {
                if (StringUtils.isNotBlank(department)) {
                    if (StringUtils.isNotBlank(complainttype))
                        query.append(USER_SELECT_QRY);
                    // Next is userwise.
                    else
                        query.append(COMPLAINTTYPE_SELECT_QRY);

                    // * means user selected boundary and department. Next is complaint type.

                } else
                    query.append(DEPT_SELECT_QRY);
            } else
                query.append(LOCALITY_SELECT_QRY);
        }

        else if (StringUtils.isNotBlank(department)) {
            if (StringUtils.isNotBlank(complainttype))
                query.append(USER_SELECT_QRY);
            else
                query.append(COMPLAINTTYPE_SELECT_QRY);
        } else if (StringUtils.isNotBlank(complainttype))
            query.append(COMPLAINTTYPE_SELECT_QRY);
        else if (StringUtils.isNotBlank(selecteduser))
            query.append(USER_SELECT_QRY);
        else if ("ByBoundary".equals(groupBy))
            query.append("SELECT bndry.name as name, ");
        else
            query.append(DEPT_SELECT_QRY);

        query.append("   COUNT(CASE WHEN cs.name IN ('REGISTERED') THEN 1 END) registered , "
                + " COUNT(CASE WHEN cs.name IN ('FORWARDED','PROCESSING','REOPENED','NOTCOMPLETED') THEN 1 END) inprocess, "
                + " COUNT(CASE WHEN cs.name IN ('COMPLETED','WITHDRAWN','CLOSED') THEN 1 END) Completed, "
                + " COUNT(CASE WHEN cs.name IN ('REJECTED') THEN 1 END) Rejected ,");
        query.append("SUM(CASE WHEN state.value in ('COMPLETED','REJECTED','WITHDRAWN') AND "
                + "(state.lastmodifieddate - cd.createddate) < (interval '1h' * ctype.slahours) THEN 1 "
                + "WHEN (state.value not in ('COMPLETED','REJECTED','WITHDRAWN') AND "
                + "(now() - cd.createddate) < (interval '1h' * ctype.slahours)) THEN 1 else 0 END) withinsla, ");
        query.append(" SUM(CASE WHEN state.value in ('COMPLETED','REJECTED','WITHDRAWN') AND "
                + "(state.lastmodifieddate - cd.createddate) > (interval '1h' * ctype.slahours) THEN 1 "
                + "WHEN (state.value not in ('COMPLETED','REJECTED','WITHDRAWN') AND "
                + "(now() - cd.createddate ) > (interval '1h' * ctype.slahours)) THEN 1 ELSE 0 END) beyondsla ");
        query.append(
                " FROM egpgr_complaintstatus cs ,egpgr_complainttype ctype , eg_wf_states state, egpgr_complaint cd "
                        + " left JOIN eg_boundary bndry on cd.location =bndry.id left JOIN eg_boundary cbndry on cd.childlocation=cbndry.id"
                        + "  left JOIN eg_department dept on cd.department =dept.id left join eg_position pos on cd.assignee=pos.id "
                        + " left join view_egeis_employee emp on pos.id=emp.position ");

        buildWhereClause(fromDate, toDate, complaintDateType, query, department, boundary, complainttype, locality);

        buildGroupByClause(groupBy, department, boundary, complainttype, query, locality);

        return setParameterForDrillDownReportQuery(query.toString(), fromDate, toDate, complaintDateType);
    }

    private void buildGroupByClause(final String groupBy, final String department, final String boundary,
            final String complainttype, final StringBuilder query, final String locality) {

        if (StringUtils.isNotBlank(boundary)) {
            if (StringUtils.isNotBlank(locality)) {
                if (StringUtils.isNotBlank(department)) {
                    if (StringUtils.isNotBlank(complainttype))
                        query.append("  group by emp.name||'~'|| pos.name ");
                    else

                        query.append("  group by ctype.name ");

                } else
                    query.append(" group by dept.name ");
            } else
                query.append("group by cbndry.name");

        }

        else if (StringUtils.isNotBlank(department)) {

            if (StringUtils.isNotBlank(complainttype))
                query.append("  group by emp.name||'~'|| pos.name ");
            else
                query.append("  group by ctype.name ");
        } else if (StringUtils.isNotBlank(complainttype))
            query.append(" group by ctype.name  ");
        else if ("ByBoundary".equals(groupBy))
            query.append("  group by bndry.name ");
        else
            query.append("  group by dept.name ");
    }

    private void buildWhereClause(final Date fromDate, final Date toDate, final String complaintDateType,
            final StringBuilder query, final String department, final String boundary, final String complainttype,
            final String locality) {

        query.append(
                " WHERE cd.status  = cs.id and cd.complainttype= ctype.id  and cd.state_id = state.id and emp.isprimary = true and emp.todate >= :currdate");

        if (fromDate != null && toDate != null)
            query.append(" and ( cd.createddate BETWEEN :fromDates and :toDates) ");

        else if (fromDate != null || "lastsevendays".equals(complaintDateType) || "lastthirtydays".equals(complaintDateType)
                || "lastninetydays".equals(complaintDateType))
            query.append(" and cd.createddate >=   :fromDates ");
        else if (toDate != null)
            query.append(" and cd.createddate <=  :toDates ");

        if (StringUtils.isNotBlank(boundary))
            if (NOT_AVAILABLE.equals(boundary))
                query.append(" and  bndry.name is null ");
            else {
                query.append(" and upper(trim(bndry.name))= '");
                query.append(boundary.toUpperCase()).append("' ");
            }
        if (StringUtils.isNotBlank(locality))
            if ("GIS_LOCATION".equals(locality))
                query.append("and cbndry.name is null");
            else {
                query.append("and upper(trim(cbndry.name))= '");
                query.append(locality.toUpperCase()).append("' ");
            }
        if (StringUtils.isNotBlank(department))
            if (NOT_AVAILABLE.equals(department))
                query.append(" and  dept.name is null ");
            else {
                query.append(" and upper(trim(dept.name))=  '");
                query.append(department.toUpperCase()).append("' ");
            }
        if (StringUtils.isNotBlank(complainttype)) {
            query.append(" and upper(trim(ctype.name))= '");
            query.append(complainttype.toUpperCase()).append("' ");
        }

    }

    private SQLQuery setParameterForDrillDownReportQuery(final String querykey, final Date fromDate,
            final Date toDate, final String complaintDateType) {
        final SQLQuery qry = entityManager.unwrap(Session.class).createSQLQuery(querykey);

        if ("lastsevendays".equals(complaintDateType))
            qry.setParameter(FROMDATE, DateUtils.endOfToday().minusDays(8).toDate());
        else if ("lastthirtydays".equals(complaintDateType))
            qry.setParameter(FROMDATE, DateUtils.endOfToday().minusDays(31).toDate());
        else if ("lastninetydays".equals(complaintDateType))
            qry.setParameter(FROMDATE, DateUtils.endOfToday().minusDays(91).toDate());
        else if (fromDate != null && toDate != null) {
            qry.setParameter(FROMDATE, DateUtils.startOfDay(fromDate));
            qry.setParameter("toDates", DateUtils.endOfDay(toDate));
        } else if (fromDate != null)
            qry.setParameter(FROMDATE, DateUtils.startOfDay(fromDate));
        else if (toDate != null)
            qry.setParameter("toDates", DateUtils.endOfDay(toDate));
        qry.setParameter("currdate", DateUtils.endOfToday().minusDays(1).toDate());
        return qry;

    }

    @ReadOnly
    public SQLQuery getDrillDownReportQuery(final Date fromDate, final Date toDate,
            final String complaintDateType, final String department, final String boundary, final String complainttype,
            final String selecteduser, final String locality) {
        final StringBuilder query = new StringBuilder();

        query.append(
                "SELECT distinct complainant.id as complaintid, crn,cd.createddate,complainant.name as complaintname,cd.details,cs.name as status , bndry.name || ' - ' || childlocation.name AS boundaryname , cd.citizenfeedback as feedback ,");
        query.append(
                "CASE WHEN state.value IN ('COMPLETED','REJECTED','WITHDRAWN') AND (state.lastmodifieddate - cd.createddate) < (interval '1h' * ctype.slahours) THEN 'Yes' WHEN (state.value NOT IN ('COMPLETED','REJECTED','WITHDRAWN') ");
        query.append(
                "AND (now() - cd.createddate) < (interval '1h' * ctype.slahours)) THEN 'Yes' ELSE 'No' END as issla ");
        query.append(
                "FROM egpgr_complaintstatus cs ,egpgr_complainttype ctype ,eg_wf_states state ,egpgr_complaint cd left JOIN eg_boundary bndry "
                        + "on cd.location =bndry.id left JOIN eg_boundary childlocation on cd.childlocation = childlocation.id  left JOIN eg_boundary cbndry on cd.childlocation=cbndry.id left JOIN eg_department dept "
                        + "on cd.department =dept.id  left join eg_position pos on cd.assignee=pos.id left join view_egeis_employee emp "
                        + "on pos.id=emp.position , egpgr_complainant complainant ");

        buildWhereClause(fromDate, toDate, complaintDateType, query, department, boundary, complainttype, locality);
        query.append(" and complainant.id=cd.complainant   ");
        if (selecteduser != null && !"".equals(selecteduser)) {
            query.append(" and upper(emp.name)= '");
            query.append(selecteduser.toUpperCase()).append("' ");
        }

        return setParameterForDrillDownReportQuery(query.toString(), fromDate, toDate, complaintDateType);
    }

}
