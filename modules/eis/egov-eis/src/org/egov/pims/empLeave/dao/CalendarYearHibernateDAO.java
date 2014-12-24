package org.egov.pims.empLeave.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.empLeave.model.CalendarYear;
import org.hibernate.Query;
import org.hibernate.Session;

public class CalendarYearHibernateDAO extends GenericHibernateDAO implements CalendarYearDao 
{
	private Session session;
	
    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public CalendarYearHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public String getCurrentYearId() {
		Date dt=new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
		String currentDate=formatter.format(dt);
		session = HibernateUtil.getCurrentSession();
        String result=null;
        Query query=session.createQuery("select cal.id from CalendarYear cal where cal.startingDate <= '"+currentDate+"' and cal.endingDate >= '"+currentDate+"' ");
        ArrayList list= (ArrayList)query.list();
        if(!list.isEmpty())
        {
        	result=list.get(0).toString();
        }
        return result;
		
	}
	public String getYearIdByGivenDate(String estDate)
	{
		session = HibernateUtil.getCurrentSession();
        String result="";
        Query query=session.createQuery("select cal.id from CalendarYear cal where cal.startingDate <= '"+estDate+"' and cal.endingDate >= '"+estDate+"' ");
        ArrayList list= (ArrayList)query.list();
        if(!list.isEmpty())
        {
        	result=list.get(0).toString();
        }
        return result;
		
	}
	public CalendarYear getCalendarYearById(Long id)
	{
		Query qry = getSession().createQuery("from CalendarYear cal where cal.id =:id ");
		qry.setLong("id", id);
		return (CalendarYear)qry.uniqueResult();
		
	}
	

}
