package org.egov.pims.empLeave.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.pims.empLeave.model.Attendence;
import org.egov.pims.empLeave.model.AttendenceType;
import org.egov.pims.empLeave.model.HolidaysUlb;
import org.egov.pims.empLeave.model.LeaveApplication;
import org.egov.pims.model.TecnicalQualification;
import org.egov.pims.utils.EisManagersUtill;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 */
public class AttendenceTypeHibernateDAO extends GenericHibernateDAO implements AttendenceTypeDAO
{
    
    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public AttendenceTypeHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public AttendenceType getAttendenceTypeByID(Integer id) 
	{
		
			Query qry = getSession().createQuery("from AttendenceType B where B.id =:id ");
			qry.setInteger("id", id);
			return (AttendenceType)qry.uniqueResult();
		
	}
	public AttendenceType getAttendenceTypeByName(String typeName) 
	{
		Query qry = getSession().createQuery("from AttendenceType B where B.name =:typeName ");
		qry.setString("typeName", typeName);
		return (AttendenceType)qry.uniqueResult();
		
	}
}



