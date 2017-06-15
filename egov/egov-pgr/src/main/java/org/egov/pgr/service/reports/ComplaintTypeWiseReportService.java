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

import static org.egov.pgr.utils.constants.PGRConstants.FROMDATE;
import static org.egov.pgr.utils.constants.PGRConstants.TODATE;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.utils.DateUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ComplaintTypeWiseReportService {

    @PersistenceContext
    private EntityManager entityManager;

    @ReadOnly
    public SQLQuery getComplaintTypeWiseReportQuery(final Date fromDate, final Date toDate,
            final String complaintType, final String complaintDateType) {

        final StringBuilder query = new StringBuilder();

        query.append(
                "SELECT ctype.id as  complainttypeid, ctype.name as name,COUNT(CASE WHEN cs.name IN ('REGISTERED') THEN 1 END) registered ,  COUNT(CASE WHEN cs.name IN ('FORWARDED','PROCESSING','NOTCOMPLETED') THEN 1 END) inprocess,  COUNT(CASE WHEN cs.name IN ('COMPLETED','CLOSED') THEN 1 END) Completed, COUNT(CASE WHEN cs.name IN ('REOPENED') THEN 1 END) reopened,   COUNT(CASE WHEN cs.name IN ('WITHDRAWN','REJECTED') THEN 1 END) Rejected, ");
        query.append(
                "SUM(CASE WHEN state.value in ('COMPLETED','REJECTED','WITHDRAWN') AND (state.lastmodifieddate - cd.createddate) < (interval '1h' * ctype.slahours) THEN 1 WHEN (state.value not in ('COMPLETED','REJECTED','WITHDRAWN') AND (now() - cd.createddate) < (interval '1h' * ctype.slahours)) THEN 1 else 0 END) withinsla, ");
        query.append(
                "SUM(CASE WHEN state.value in ('COMPLETED','REJECTED','WITHDRAWN') AND (state.lastmodifieddate - cd.createddate) > (interval '1h' * ctype.slahours) THEN 1 WHEN (state.value not in ('COMPLETED','REJECTED','WITHDRAWN') AND (now() - cd.createddate ) > (interval '1h' * ctype.slahours)) THEN 1 ELSE 0 END) beyondsla ");
        query.append("FROM egpgr_complaintstatus cs ,egpgr_complainttype ctype ,eg_wf_states state  ,egpgr_complaint cd");
        buildWhereClause(fromDate, toDate, complaintType, complaintDateType, query);
        query.append(" group by ctype.name,ctype.id ");

        return setParameterForComplaintTypeReportQuery(query.toString(), fromDate, toDate, complaintDateType);

    }

    private void buildWhereClause(final Date fromDate, final Date toDate, final String complaintType,
            final String complaintDateType, final StringBuilder query) {

        query.append(" WHERE cd.status = cs.id and cd.complainttype= ctype.id  and cd.state_id = state.id");
        if (fromDate != null && toDate != null)
            query.append(" and ( cd.createddate BETWEEN :fromDates and :toDates) ");
        else if (fromDate != null || "lastsevendays".equals(complaintDateType) || "lastthirtydays".equals(complaintDateType)
                || "lastninetydays".equals(complaintDateType))
            query.append(" and cd.createddate >=   :fromDates ");
        else if (toDate != null)
            query.append(" and cd.createddate <=  :toDates ");
        if (complaintType != null && !"".equals(complaintType)) {
            query.append(" and (ctype.id)= '");
            query.append(complaintType.toUpperCase()).append("' ");
        }

    }

    private SQLQuery setParameterForComplaintTypeReportQuery(final String querykey, final Date fromDate,
            final Date toDate, final String complaintDateType) {
        final SQLQuery qry = entityManager.unwrap(Session.class).createSQLQuery(querykey);

        if ("lastsevendays".equals(complaintDateType))
            qry.setParameter(FROMDATE,  DateUtils.endOfToday().minusDays(8).toDate());
        else if ("lastthirtydays".equals(complaintDateType))
            qry.setParameter(FROMDATE,  DateUtils.endOfToday().minusDays(31).toDate());
        else if ("lastninetydays".equals(complaintDateType))
            qry.setParameter(FROMDATE,  DateUtils.endOfToday().minusDays(91).toDate());
        else if (fromDate != null && toDate != null) {
            qry.setParameter(FROMDATE, DateUtils.startOfDay(fromDate));
            qry.setParameter(TODATE, DateUtils.endOfDay(toDate));
        } else if (fromDate != null)
            qry.setParameter(FROMDATE, DateUtils.startOfDay(fromDate));
        else if (toDate != null)
            qry.setParameter(TODATE, DateUtils.endOfDay(toDate));
        return qry;

    }

    public SQLQuery getComplaintTypeWiseReportQuery(final Date fromDate, final Date toDate,
            final String complaintDateType, final String complaintTypeWithStatus, final String status) {

        final StringBuilder query = new StringBuilder();

        query.append(
                " SELECT  distinct complainant.id as complaintid, crn,cd.createddate,complainant.name as complaintname,cd.details,cs.name as status , bndry.name || ' - ' || childlocation.name as boundaryname, cd.citizenfeedback as feedback,");
        query.append(
                "CASE WHEN state.value IN ('COMPLETED','REJECTED','WITHDRAWN') AND (state.lastmodifieddate - cd.createddate) < (interval '1h' * ctype.slahours) THEN 'Yes' WHEN (state.value NOT IN ('COMPLETED','REJECTED','WITHDRAWN') ");
        query.append(
                "AND (now() - cd.createddate) < (interval '1h' * ctype.slahours)) THEN 'Yes' ELSE 'No' END as issla  ");
        query.append(
                "FROM egpgr_complaintstatus cs ,egpgr_complainttype ctype ,eg_wf_states state, egpgr_complaint cd left JOIN eg_boundary bndry ");
        query.append(
                "on cd.location =bndry.id left JOIN eg_boundary childlocation on  cd.childlocation=childlocation.id  left JOIN eg_department dept on cd.department =dept.id  ");
        query.append(
                "left join eg_position pos on cd.assignee=pos.id left join view_egeis_employee emp on pos.id=emp.position , egpgr_complainant complainant ");

        buildWhereClause(fromDate, toDate, complaintTypeWithStatus, complaintDateType, query);
        query.append(" and complainant.id=cd.complainant   ");
        if (status != null && !"".equals(status))
            if ("registered".equalsIgnoreCase(status))
                query.append(" and cs.name in ('REGISTERED')");
            else if ("inprocess".equalsIgnoreCase(status))
                query.append(" and cs.name in ('FORWARDED','PROCESSING','NOTCOMPLETED')");
            else if ("rejected".equalsIgnoreCase(status))
                query.append(" and cs.name in ('WITHDRAWN','REJECTED')");
            else if ("completed".equalsIgnoreCase(status))
                query.append(" and cs.name in ('COMPLETED','CLOSED')");
            else if ("reopened".equalsIgnoreCase(status))
                query.append(" and cs.name in ('REOPENED')");
            else if ("Within SLA".equalsIgnoreCase(status)) {
                query.append(
                        "and case when state.value IN ('COMPLETED','REJECTED','WITHDRAWN') then (state.lastmodifieddate - cd.createddate) < (interval '1h' * ctype.slahours)");
                query.append(
                        "when state.value NOT IN ('COMPLETED','REJECTED','WITHDRAWN') then (now() - cd.createddate) < (interval '1h' * ctype.slahours) end ");
            } else if ("Beyond SLA".equalsIgnoreCase(status)) {
                query.append(
                        "and case when state.value IN ('COMPLETED','REJECTED','WITHDRAWN') then (state.lastmodifieddate - cd.createddate) > (interval '1h' * ctype.slahours)"
                        +"when state.value NOT IN ('COMPLETED','REJECTED','WITHDRAWN') then (now() - cd.createddate) > (interval '1h' * ctype.slahours) end ");
            }

        return setParameterForComplaintTypeReportQuery(query.toString(), fromDate, toDate, complaintDateType);

    }

}
