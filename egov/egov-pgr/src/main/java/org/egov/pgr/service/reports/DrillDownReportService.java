package org.egov.pgr.service.reports;

import java.util.Date;

import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.SQLQuery;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DrillDownReportService {
    private static final Logger LOG = LoggerFactory.getLogger(DrillDownReportService.class);
    String COMPLAINTSTATUS_COMPLETED = "Completed";

    public SQLQuery getDrillDownReportQuery(DateTime fromDate, DateTime toDate, String complaintDateType,
 String groupBy) {

        StringBuffer query = new StringBuffer();

        if (groupBy != null && !"".equals(groupBy) && groupBy.equalsIgnoreCase("ByBoundary")) {
            query.append("SELECT bndryparent.name as name, ");// TODO CHECK
                                                              // DEPARTMENTWISE
                                                              // OR ZONE WISE
        } else {
            query.append("SELECT dept.name as name, ");
        }

        query.append("   COUNT(CASE WHEN cs.name IN ('REGISTERED') THEN 1 END) registered , "
                + " COUNT(CASE WHEN cs.name IN ('FORWARDED','PROCESSING','REOPENED','NOTCOMPLETED') THEN 1 END) inprocess, "
                + " COUNT(CASE WHEN cs.name IN ('COMPLETED','WITHDRAWN','CLOSED') THEN 1 END) Completed, "
                + " COUNT(CASE WHEN cs.name IN ('REJECTED') THEN 1 END) Rejected ");

        if (groupBy != null && !"".equals(groupBy) && groupBy.equalsIgnoreCase("ByBoundary")) {
            query.append("  FROM egpgr_complaintstatus cs ,egpgr_complainttype ctype ,egpgr_complaint cd  left JOIN eg_boundary bndry on cd.location =bndry.id left JOIN eg_boundary bndryparent on  bndry.parent=bndryparent.id ");
        } else {
            query.append(" FROM egpgr_complaintstatus cs ,egpgr_complainttype ctype ,egpgr_complaint cd  left JOIN eg_department dept on cd.department =dept.id ");
        }

        buildWhereClause(fromDate, toDate, complaintDateType, query);

        if (groupBy != null && !"".equals(groupBy) && groupBy.equalsIgnoreCase("ByBoundary")) {
            query.append("  group by bndryparent.name ");

        } else {
            query.append("  group by dept.name ");
        }

        return getAgeingData(query.toString(), fromDate, toDate, complaintDateType);
    }

    private void buildWhereClause(DateTime fromDate, DateTime toDate, String complaintDateType, StringBuffer query) {

        query.append(" WHERE cd.status  = cs.id and cd.complainttype= ctype.id  ");

        if (complaintDateType != null && complaintDateType.equals("lastsevendays")) {
            query.append(" and cd.createddate >=   :fromDates ");

        } else if (complaintDateType != null && complaintDateType.equals("lastthirtydays")) {
            query.append(" and cd.createddate >=   :fromDates ");

        } else if (complaintDateType != null && complaintDateType.equals("lastninetydays")) {
            query.append(" and cd.createddate >=   :fromDates ");

        } else if (fromDate != null && toDate != null) {
            query.append(" and ( cd.createddate BETWEEN :fromDates and :toDates) ");

        } else if (fromDate != null) {
            query.append(" and cd.createddate >=   :fromDates ");

        } else if (toDate != null) {
            query.append(" and cd.createddate <=  :toDates ");
        }
    }

    private SQLQuery getAgeingData(final String querykey, DateTime fromDate, DateTime toDate, String complaintDateType) {
        final SQLQuery qry = HibernateUtil.getCurrentSession().createSQLQuery(querykey);

        if (complaintDateType != null && complaintDateType.equals("lastsevendays")) {
            qry.setParameter("fromDates", endOfDayFromCurrentDate().minusDays(7).toDate());
        } else if (complaintDateType != null && complaintDateType.equals("lastthirtydays")) {
            qry.setParameter("fromDates", endOfDayFromCurrentDate().minusDays(30).toDate());
        } else if (complaintDateType != null && complaintDateType.equals("lastninetydays")) {
            qry.setParameter("fromDates", endOfDayFromCurrentDate().minusDays(90).toDate());
        } else if (fromDate != null && toDate != null) {
            qry.setParameter("fromDates", getEndOfDayByDate(fromDate));
            qry.setParameter("toDates", getEndOfDayByDate(toDate));

        } else if (fromDate != null) {
            qry.setParameter("fromDates", getEndOfDayByDate(fromDate));

        } else if (toDate != null) {
            qry.setParameter("toDates", getEndOfDayByDate(toDate));

        }
        return qry;

    }

    private Date getEndOfDayByDate(DateTime fromDate) {
        return fromDate.withTime(23, 59, 59, 999).toDate();
    }

    private DateTime endOfDayFromCurrentDate() {
        return new LocalDateTime().withTime(23, 59, 59, 999).toDateTime();
    }

}
