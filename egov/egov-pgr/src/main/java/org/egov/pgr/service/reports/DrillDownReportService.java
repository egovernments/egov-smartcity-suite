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
 String groupBy, String department, String boundary, String complainttype, String selecteduser) {

        StringBuffer query = new StringBuffer();

        if(boundary!=null && !"".equals(boundary) )
        {
            if(department!=null && !"".equals(department) )
            {
                
                if(complainttype!=null && !"".equals(complainttype) ){
                    query.append("  SELECT   emp.name||'~'|| pos.name    as name, ");              // Next is userwise.
                }else
                    query.append(" SELECT ctype.name as name, "); //mean user selected boundary and department. Next is complaint type.
             }else
                query.append(" SELECT dept.name as name, ");  //Mean get department list .
        }
        else if(department!=null && !"".equals(department))
        {
            if(complainttype!=null && !"".equals(complainttype)){
                query.append("  SELECT   emp.name||'~'|| pos.name    as name, ");
            }else
                query.append(" SELECT ctype.name as name, ");
        }
        else if(complainttype!=null && !"".equals(complainttype) )
        {
            query.append(" SELECT ctype.name as name, ");
        }
        else if(selecteduser!=null  && !"".equals(selecteduser))
        {
            query.append("  SELECT   emp.name||'~'|| pos.name    as name, ");  
        }
        else if (groupBy != null && !"".equals(groupBy) && groupBy.equalsIgnoreCase("ByBoundary")) {
            query.append("SELECT bndryparent.name as name, ");
        } else {
            query.append("SELECT dept.name as name, ");
        }

        query.append("   COUNT(CASE WHEN cs.name IN ('REGISTERED') THEN 1 END) registered , "
                + " COUNT(CASE WHEN cs.name IN ('FORWARDED','PROCESSING','REOPENED','NOTCOMPLETED') THEN 1 END) inprocess, "
                + " COUNT(CASE WHEN cs.name IN ('COMPLETED','WITHDRAWN','CLOSED') THEN 1 END) Completed, "
                + " COUNT(CASE WHEN cs.name IN ('REJECTED') THEN 1 END) Rejected ");

        query.append("  FROM egpgr_complaintstatus cs ,egpgr_complainttype ctype ,egpgr_complaint cd  left JOIN eg_boundary bndry on cd.location =bndry.id left JOIN eg_boundary bndryparent on  bndry.parent=bndryparent.id  left JOIN eg_department dept on cd.department =dept.id left join eg_position pos on cd.assignee=pos.id  left join view_egeis_employee emp on pos.id=emp.position ");

        buildWhereClause(fromDate, toDate, complaintDateType, query, department, boundary, complainttype, selecteduser);

        buildGroupByClause(groupBy, department, boundary, complainttype, selecteduser, query);

        return setParameterForDrillDownReportQuery(query.toString(), fromDate, toDate, complaintDateType);
    }

    private void buildGroupByClause(String groupBy, String department, String boundary, String complainttype,
            String selecteduser, StringBuffer query) {
        if (boundary != null && !"".equals(boundary)) {
            if (department != null && !"".equals(department)) {
                if (complainttype != null && !"".equals(complainttype)) {
                    query.append("  group by emp.name||'~'|| pos.name ");
                } else

                    query.append("  group by ctype.name ");

            } else
                query.append("  group by dept.name ");
        } else if (department != null && !"".equals(department)) {

            if (complainttype != null && !"".equals(complainttype)) {
                query.append("  group by emp.name||'~'|| pos.name ");
            } else
                query.append("  group by ctype.name ");
        } else if (complainttype != null && !"".equals(complainttype)) {
            query.append(" group by ctype.name  ");

        }  else if (groupBy != null && !"".equals(groupBy) && groupBy.equalsIgnoreCase("ByBoundary")) {
            query.append("  group by bndryparent.name ");

        } else {
            query.append("  group by dept.name ");
        }
    }

    private void buildWhereClause(DateTime fromDate, DateTime toDate, String complaintDateType, StringBuffer query,
            String department, String boundary, String complainttype, String selecteduser) {

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

        if (boundary != null && !"".equals(boundary)) {

            if (boundary.equalsIgnoreCase("NOT AVAILABLE"))
                query.append(" and  bndryparent.name is null ");
            else {
                query.append(" and upper(bndryparent.name)= '");
                query.append(boundary.toUpperCase()).append("' ");
            }

        }
        if (department != null && !"".equals(department)) {
            if (department.equalsIgnoreCase("NOT AVAILABLE"))
                query.append(" and  dept.name is null ");
            else {
                query.append(" and upper(dept.name)=  '");
                query.append(department.toUpperCase()).append("' ");
            }
        }
        if (complainttype != null && !"".equals(complainttype)) {
            query.append(" and upper(ctype.name)= '");
            query.append(complainttype.toUpperCase()).append("' ");
        }

    }

    private SQLQuery setParameterForDrillDownReportQuery(final String querykey, DateTime fromDate, DateTime toDate,
            String complaintDateType) {
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

    public SQLQuery getDrillDownReportQuery(DateTime fromDate, DateTime toDate, String complaintDateType,
            String department, String boundary, String complainttype, String selecteduser) {
        StringBuffer query = new StringBuffer();

        query.append(" SELECT  distinct complainant.id as complaintid, crn,cd.createddate,complainant.name as complaintname,cd.details,cs.name as status , bndry.name as boundaryname FROM egpgr_complaintstatus cs ,egpgr_complainttype ctype ,egpgr_complaint cd left JOIN eg_boundary bndry on cd.location =bndry.id left JOIN eg_boundary bndryparent on  bndry.parent=bndryparent.id  left JOIN eg_department dept on cd.department =dept.id  left join eg_position pos on cd.assignee=pos.id left join view_egeis_employee emp on pos.id=emp.position ,"
                + " egpgr_complainant complainant ");

        buildWhereClause(fromDate, toDate, complaintDateType, query, department, boundary, complainttype, selecteduser);
        query.append(" and complainant.id=cd.complainant   ");
        if (selecteduser != null && !"".equals(selecteduser)) {
            query.append(" and upper(emp.name)= '");
            query.append(selecteduser.toUpperCase()).append("' ");
        }

        return setParameterForDrillDownReportQuery(query.toString(), fromDate, toDate, complaintDateType);
    }

}
