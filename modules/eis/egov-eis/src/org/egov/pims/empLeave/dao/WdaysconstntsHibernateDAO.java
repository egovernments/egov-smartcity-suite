package org.egov.pims.empLeave.dao;
import java.util.Iterator;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.pims.empLeave.model.SaturdayHoliday;
import org.egov.pims.empLeave.model.SecondSaturdayHoliday;
import org.egov.pims.empLeave.model.Wdaysconstnts;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 */
public class WdaysconstntsHibernateDAO extends GenericHibernateDAO implements WdaysconstntsDAO
{
   
    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public WdaysconstntsHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public Wdaysconstnts getWdaysconstnts()  
	{
			Wdaysconstnts wdaysconstnts=null;
			Query qry = getSession().createQuery("from Wdaysconstnts B where B.isactive =:isactive ");
			qry.setCharacter("isactive", Character.valueOf('1'));
			try
			{
				for(Iterator iter = qry.list().iterator();iter.hasNext();)
				{
					wdaysconstnts = (Wdaysconstnts)iter.next();
					if(wdaysconstnts.getName().equals("SecondSaturdayHoliday"))
					{
						wdaysconstnts =  	new SecondSaturdayHoliday();
					}
					else if(wdaysconstnts.getName().equals("SaturdayHoliday"))
					{
						wdaysconstnts =  	new SaturdayHoliday();
					}
				}
			}catch(Exception e)
			{
				
				throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
			}
			return wdaysconstnts;
	}
	
	public Wdaysconstnts getWdaysconstntsByID(Integer id)
	{
		Query qry = null;
		try {
			qry = getSession().createQuery("from Wdaysconstnts B where B.id =:id ");
			qry.setInteger("id", id);
		} catch (HibernateException e) {
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
		return (Wdaysconstnts)qry.uniqueResult();
	}
		
	
}



