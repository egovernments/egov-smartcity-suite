package org.egov.pgr.service.reports;

import static org.egov.pgr.utils.constants.PGRConstants.FROMDATE;
import static org.egov.pgr.utils.constants.PGRConstants.TODATE;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FunctionaryWiseReportService {
    @PersistenceContext
    private EntityManager entityManager;

    @ReadOnly
    public SQLQuery getFunctionaryWiseReportQuery(final DateTime fromDate, final DateTime toDate, final String usrid,
            final String complaintDateType) {

        final StringBuilder query = new StringBuilder();

        query.append(
                "SELECT cast(usr.employee as bigint) as  usrid, usr.name as name,COUNT(CASE WHEN cs.name IN ('REGISTERED') THEN 1 END) registered ,  COUNT(CASE WHEN cs.name IN ('FORWARDED','PROCESSING','NOTCOMPLETED') THEN 1 END) inprocess, COUNT(CASE WHEN cs.name IN ('COMPLETED','CLOSED') THEN 1 END) Completed, COUNT(CASE WHEN cs.name IN ('REOPENED') THEN 1 END) reopened, COUNT(CASE WHEN cs.name IN ('WITHDRAWN','REJECTED') THEN 1 END) Rejected ,");
        query.append(
                "SUM(CASE WHEN state.value in ('COMPLETED','REJECTED','WITHDRAWN') AND (cd.createddate - state.lastmodifieddate) < (interval '1h' * ctype.slahours) THEN 1 WHEN (state.value not in ('COMPLETED','REJECTED','WITHDRAWN') AND (cd.createddate - CURRENT_DATE) < (interval '1h' * ctype.slahours)) THEN 1 else 0 END) withinsla, ");
        query.append(
                "SUM(CASE WHEN state.value in ('COMPLETED','REJECTED','WITHDRAWN') AND (cd.createddate - state.lastmodifieddate) > (interval '1h' * ctype.slahours) THEN 1 WHEN (state.value not in ('COMPLETED','REJECTED','WITHDRAWN') AND (cd.createddate - CURRENT_DATE ) > (interval '1h' * ctype.slahours)) THEN 1 ELSE 0 END) beyondsla ");
        query.append(
                " FROM egpgr_complaintstatus cs ,egpgr_complainttype ctype,view_egeis_employee usr ,egpgr_complaint cd ,eg_wf_states state ");
        buildWhereClause(fromDate, toDate, usrid, complaintDateType, query);
        query.append(" group by usr.employee,usr.name ");

        return setParameterForFunctionaryReportQuery(query.toString(), fromDate, toDate, complaintDateType);

    }

    private void buildWhereClause(final DateTime fromDate, final DateTime toDate, final String userid,
            final String complaintDateType, final StringBuilder query) {

        query.append(
                " WHERE cd.status = cs.id and cd.complainttype= ctype.id  and cd.state_id = state.id and cd.assignee= usr.position and usr.todate >= :currdate");

        if (complaintDateType != null && "lastsevendays".equals(complaintDateType))
            query.append(" and cd.createddate >=   :fromDates ");
        else if (complaintDateType != null && "lastthirtydays".equals(complaintDateType))
            query.append(" and cd.createddate >=   :fromDates ");
        else if (complaintDateType != null && "lastninetydays".equals(complaintDateType))
            query.append(" and cd.createddate >=   :fromDates ");
        else if (fromDate != null && toDate != null)
            query.append(" and ( cd.createddate BETWEEN :fromDates and :toDates) ");
        else if (fromDate != null)
            query.append(" and cd.createddate >=   :fromDates ");
        else if (toDate != null)
            query.append(" and cd.createddate <=  :toDates ");

        if (userid != null && !"".equals(userid)) {
            query.append(" and (usr.employee)= '");
            query.append(userid.toUpperCase()).append("' ");
        }
    }

    private SQLQuery setParameterForFunctionaryReportQuery(final String querykey, final DateTime fromDate,
            final DateTime toDate, final String complaintDateType) {
        final SQLQuery qry = entityManager.unwrap(Session.class).createSQLQuery(querykey);

        if (complaintDateType != null && complaintDateType.equals("lastsevendays"))
            qry.setParameter(FROMDATE, getCurrentDateWithOutTime().minusDays(7).toDate());
        else if (complaintDateType != null && complaintDateType.equals("lastthirtydays"))
            qry.setParameter(FROMDATE, getCurrentDateWithOutTime().minusDays(30).toDate());
        else if (complaintDateType != null && complaintDateType.equals("lastninetydays"))
            qry.setParameter(FROMDATE, getCurrentDateWithOutTime().minusDays(90).toDate());
        else if (fromDate != null && toDate != null) {
            qry.setParameter(FROMDATE, resetTimeByPassingDate(fromDate));
            qry.setParameter(TODATE, getEndOfDayByDate(toDate));
        } else if (fromDate != null)
            qry.setParameter(FROMDATE, resetTimeByPassingDate(fromDate));
        else if (toDate != null)
            qry.setParameter(TODATE, getEndOfDayByDate(toDate));
        qry.setParameter("currdate", getCurrentDateWithOutTime().toDate());
        return qry;

    }

    private Date getEndOfDayByDate(final DateTime fromDate) {
        return fromDate.withTime(23, 59, 59, 999).toDate();
    }

    private Date resetTimeByPassingDate(final DateTime fromDate) {
        return fromDate.withTime(0, 0, 0, 0).toDate();
    }

    private DateTime getCurrentDateWithOutTime() {
        return new LocalDateTime().withTime(0, 0, 0, 0).toDateTime();
    }

    @ReadOnly
    public SQLQuery getFunctionaryWiseReportQuery(final DateTime fromDate, final DateTime toDate, final String usrid,
            final String complaintDateType, final String status) {

        final StringBuilder query = new StringBuilder();

        query.append(
                " SELECT  distinct complainant.id as complaintid, crn,cd.createddate,complainant.name as complaintname,cd.details,cs.name as status , bndry.name || ' - ' || childlocation.name as boundaryname, cd.citizenfeedback as feedback,");
        query.append(
                "CASE WHEN state.value IN ('COMPLETED','REJECTED','WITHDRAWN') AND (cd.createddate - state.lastmodifieddate) < (interval '1h' * ctype.slahours) THEN 'Yes' WHEN (state.value NOT IN ('COMPLETED','REJECTED','WITHDRAWN') ");
        query.append(
                "AND (cd.createddate - CURRENT_DATE) < (interval '1h' * ctype.slahours)) THEN 'Yes' ELSE 'No' END as issla  ");
        query.append(
                "FROM egpgr_complaintstatus cs ,egpgr_complainttype ctype ,eg_wf_states state, view_egeis_employee usr, egpgr_complaint cd left JOIN eg_boundary bndry ");
        query.append(
                "on cd.location =bndry.id left JOIN eg_boundary childlocation on  cd.childlocation=childlocation.id  left JOIN eg_department dept on cd.department =dept.id  ");
        query.append(
                "left join eg_position pos on cd.assignee=pos.id left join view_egeis_employee emp on pos.id=emp.position , egpgr_complainant complainant ");

        buildWhereClause(fromDate, toDate, usrid, complaintDateType, query);
        query.append(" and complainant.id=cd.complainant   ");
        if (status != null && !"".equals(status))
            if (status.equalsIgnoreCase("registered"))
                query.append(" and cs.name in ('REGISTERED')");
            else if (status.equalsIgnoreCase("inprocess"))
                query.append(" and cs.name in ('FORWARDED','PROCESSING','NOTCOMPLETED')");
            else if (status.equalsIgnoreCase("rejected"))
                query.append(" and cs.name in ('WITHDRAWN','REJECTED')");
            else if (status.equalsIgnoreCase("completed"))
                query.append(" and cs.name in ('COMPLETED','CLOSED')");
            else if (status.equalsIgnoreCase("reopened"))
                query.append(" and cs.name in ('REOPENED')");

        return setParameterForFunctionaryReportQuery(query.toString(), fromDate, toDate, complaintDateType);

    }

}
