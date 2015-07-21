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
public class AgeingReportService {
    private static final Logger LOG = LoggerFactory
            .getLogger(AgeingReportService.class);
    String COMPLAINTSTATUS_COMPLETED = "Completed";
      
    public SQLQuery getageingReport(DateTime fromDate, DateTime toDate, String typeofReport, String complaintDateType,String groupBy) {

        StringBuffer query = new StringBuffer();
        
        if(groupBy!=null && !"".equals(groupBy) && groupBy.equalsIgnoreCase("ByBoundary"))
        {        query.append("SELECT bndryparent.name as name, ");//TODO CHECK DEPARTMENTWISE OR ZONE WISE
        }else
        {         query.append("SELECT ctype.name as name, ");
        }
        
        if (typeofReport != null && !"".equals(typeofReport) && typeofReport.equalsIgnoreCase(COMPLAINTSTATUS_COMPLETED)) {
            query.append(" COUNT(CASE WHEN date_part('day',(cd.createddate - state.createddate)) > :grtthn90 THEN 1 END) grtthn90, "
                    + " COUNT(CASE WHEN date_part('day',(cd.createddate - state.createddate)) BETWEEN :grtthn45 AND :lsthn90 THEN 1 END) btw45to90, "
                    + " COUNT(CASE WHEN date_part('day',(cd.createddate - state.createddate)) BETWEEN :grtthn15 AND :lsthn45 THEN 1 END) btw15to45, "
                    + " COUNT(CASE WHEN date_part('day',(cd.createddate - state.createddate)) BETWEEN :zero AND :lsthn15 THEN 1 END) lsthn15 "
                    + " FROM egpgr_complaintstatus cs  ,egpgr_complainttype ctype, eg_wf_states state, egpgr_complaint cd  " );
           
        }else
        {       query.append(" COUNT(CASE WHEN cd.createddate < :grtthn90 THEN 1 END) grtthn90, "
                + " COUNT(CASE WHEN cd.createddate BETWEEN :lsthn90 AND :grtthn45 THEN 1 END) btw45to90, "
                + " COUNT(CASE WHEN cd.createddate BETWEEN :grtthn15 AND :lsthn45 THEN 1 END) btw15to45, "
                + " COUNT(CASE WHEN cd.createddate BETWEEN :lsthn15 AND :currdate THEN 1 END) lsthn15 "
                + " FROM egpgr_complaintstatus cs  ,egpgr_complainttype ctype ,egpgr_complaint cd  ");
       
        }
        
        if(groupBy!=null && !"".equals(groupBy) && groupBy.equalsIgnoreCase("ByBoundary"))
        {        query.append("  left JOIN eg_boundary bndry on cd.location =bndry.id left JOIN eg_boundary bndryparent on  bndry.parent=bndryparent.id ");
        }else
        {     
        }
        
        
        if (typeofReport != null && !"".equals(typeofReport) && typeofReport.equalsIgnoreCase(COMPLAINTSTATUS_COMPLETED)) {
            query.append(" WHERE  cd.state_id=state.id and  cd.status  = cs.id and cd.complainttype= ctype.id  ");
            query.append(" AND cs.name IN ('COMPLETED','REJECTED', 'WITHDRAWN','CLOSED','CLOSE') ");
        }else
        {
            query.append(" WHERE cd.status  = cs.id and cd.complainttype= ctype.id  ");
            query.append(" AND cs.name IN ('REGISTERED','FORWARDED', 'PROCESSING','REOPENED') ");
        }
        
             
        if (complaintDateType!=null && complaintDateType.equals("lastsevendays"))
        {
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

        if(groupBy!=null && !"".equals(groupBy) && groupBy.equalsIgnoreCase("ByBoundary"))
        {          query.append("  group by bndryparent.name ");
  
        }else
        {        query.append("  group by ctype.name ");
        }
      

        return getAgeingData(query.toString(), typeofReport, fromDate, toDate,complaintDateType);
    }

    private SQLQuery getAgeingData(final String querykey, final String typeofReport, DateTime fromDate,
            DateTime toDate, String complaintDateType) {
        final SQLQuery qry = HibernateUtil.getCurrentSession().createSQLQuery(querykey);
        
        if (typeofReport != null && !"".equals(typeofReport) && typeofReport.equalsIgnoreCase(COMPLAINTSTATUS_COMPLETED)) {
            qry.setParameter("grtthn90", 90);
            qry.setParameter("lsthn90", 90);
            qry.setParameter("grtthn45", 45.0001);
            qry.setParameter("grtthn15", 15.0001);
            qry.setParameter("lsthn45", 45);
            qry.setParameter("lsthn15",15);
            qry.setParameter("zero", 0);
       
        }else
        {
            
            qry.setParameter("grtthn90", endOfDayFromCurrentDate().minusDays(90).toDate());
            qry.setParameter("lsthn90", endOfDayFromCurrentDate().minusDays(90).toDate());
            qry.setParameter("grtthn45", endOfDayFromCurrentDate().minusDays(45).toDate());
            qry.setParameter("grtthn15", endOfDayFromCurrentDate().minusDays(15).toDate());
            qry.setParameter("lsthn45", endOfDayFromCurrentDate().minusDays(45).toDate());
            qry.setParameter("lsthn15", endOfDayFromCurrentDate().minusDays(15).toDate());
            qry.setParameter("currdate", endOfDayFromCurrentDate().toDate());
        }
        
        if (complaintDateType!=null && complaintDateType.equals("lastsevendays"))
        {
            qry.setParameter("fromDates", endOfDayFromCurrentDate().minusDays(7).toDate()); 
        }else if (complaintDateType!=null && complaintDateType.equals("lastthirtydays"))
        {
            qry.setParameter("fromDates", endOfDayFromCurrentDate().minusDays(30).toDate());
        }else if (complaintDateType!=null && complaintDateType.equals("lastninetydays"))
        {
            qry.setParameter("fromDates", endOfDayFromCurrentDate().minusDays(90).toDate());
        }
        else if (fromDate != null && toDate != null) {
            qry.setParameter("fromDates", getEndOfDayByDate(fromDate));
            qry.setParameter("toDates", getEndOfDayByDate(toDate));

        } else if (fromDate != null) {
            qry.setParameter("fromDates", getEndOfDayByDate(fromDate));

        } else if (toDate != null) {
            qry.setParameter("toDates", getEndOfDayByDate(toDate));

        }
        return qry;
      /*  qry.setResultTransformer(Transformers.aliasToBean(AgeingReportResult.class));
        return qry.list();*/

    }
    private Date getEndOfDayByDate(DateTime fromDate) {
        return fromDate.withTime(23, 59, 59, 999).toDate();
    }  
    private  DateTime endOfDayFromCurrentDate(){
        return new LocalDateTime().withTime(23, 59, 59, 999).toDateTime();
}

}
