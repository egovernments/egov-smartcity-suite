package org.egov.pims.empLeave.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.empLeave.model.AttendenceType;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class AttendenceTypeVsName
{
	private static HashMap attendenceTypeVsName = new HashMap();

	static
	{
		 Session session;
		session = HibernateUtil.getCurrentSession();
		
		try
		{
			Query qry = session.createQuery("from AttendenceType B ");
			
			if(qry.list()!=null&&!qry.list().isEmpty())
			{
				AttendenceType obj = null;
				for(Iterator iter = qry.list().iterator();iter.hasNext();)
				{
					obj = (AttendenceType)iter.next();
					attendenceTypeVsName.put(obj.getName(), obj);
				}

			}

		}
		catch (HibernateException he) {
			throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
		} catch (Exception he) {
			throw new EGOVRuntimeException("Exception:" + he.getMessage(),he);
		}



	}
	public static Map getAttendenceTypeVsName()
	{
		return attendenceTypeVsName;
	}

}