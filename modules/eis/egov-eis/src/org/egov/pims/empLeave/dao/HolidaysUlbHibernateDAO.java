package org.egov.pims.empLeave.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.pims.empLeave.model.CalendarYear;
import org.egov.pims.empLeave.model.HolidaysUlb;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 */
public class HolidaysUlbHibernateDAO extends GenericHibernateDAO implements HolidaysUlbDAO
{
    
    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public HolidaysUlbHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public HolidaysUlb getHolidaysUlbByID(Integer id)
	{
		Query qry = getSession().createQuery("from HolidaysUlb B where B.id =:id ");
		qry.setInteger("id", id);
		return (HolidaysUlb)qry.uniqueResult();
	}

	public List getHolidayListByFinalsialYearId(CFinancialYear cFinancialYear)
		{
			Query qry = getSession().createQuery("from HolidaysUlb B where B.financialId =:cFinancialYear ");
			qry.setEntity("cFinancialYear", cFinancialYear);
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
			List l = new ArrayList();
			for(Iterator iter = qry.list().iterator();iter.hasNext();)
			{
				HolidaysUlb holidaysUlb = (HolidaysUlb)iter.next();
				l.add(sdf.format(holidaysUlb.getHolidayDate()));
			}
			return l;
			
	}
	
	public List getHolidayListByCalendarYearID(CalendarYear calendarYear)
	{
		
		Query qry=null;
		qry=getSession().createQuery("from HolidaysUlb B where B.calendarId =:calendarId");
		qry.setEntity("calendarId", calendarYear);
		
		return qry.list();
	}
	public HolidaysUlb getHolidaysUlbByDate(Date date) 
	{
		Query qry = getSession().createQuery("from HolidaysUlb B where B.holidayDate =:holidayDate ");
		qry.setDate("holidayDate", new java.sql.Date (date.getTime()));
		return (HolidaysUlb)qry.uniqueResult();
	}
	public List getHolidaysUlbByMonth(Integer monthId) 
	{
		Query qry = getSession().createQuery("from HolidaysUlb B where B.month =:month ");
		List l = new ArrayList();
		qry.setInteger("month", monthId);
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
		for(Iterator iter = qry.list().iterator();iter.hasNext();)
		{
			HolidaysUlb holidaysUlb = (HolidaysUlb)iter.next();
			l.add(sdf.format(holidaysUlb.getHolidayDate()));
		}
		return l;
		
	}
	public List getHolidaysUlbsFotFinalsialYearId(CFinancialYear cFinancialYear)
	{
		Query qry = getSession().createQuery("from HolidaysUlb B where B.financialId =:cFinancialYear ");
		qry.setEntity("cFinancialYear", cFinancialYear);
		return qry.list();
		
}
}



