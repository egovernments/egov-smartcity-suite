package org.egov.pims.empLeave.dao;

import java.util.List;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.pims.empLeave.model.LeaveMaster;
import org.hibernate.Query;
import org.hibernate.Session;



/**
 * This Class implememets the PersonalInformationDAO for the Hibernate specific
 * Implementation
 *
 * @author Neetu
 * @version 2.00
 */
public class LeaveMasterHibernateDAO extends GenericHibernateDAO implements LeaveMasterDAO
{
  
    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public LeaveMasterHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public LeaveMaster getLeaveMasterByID(Integer id)
	{
		Query qry = getSession().createQuery("from LeaveMaster B where B.id =:id ");
		qry.setInteger("id", id);
		return (LeaveMaster)qry.uniqueResult();
	}
	public List getListOfLeaveMastersForDesID(Integer desigId)
	{
		List al=null;
		Query qry = getSession().createQuery("from LeaveMaster B where B.desigId =:desigId ");
		qry.setInteger("desigId", desigId);
		al = qry.list();
		return al;
	}
	public Integer getNoOfDaysForDesIDandType(Integer desigId,Integer typeId) 
	{
		LeaveMaster lm = null;
		Integer in = Integer.valueOf(0);
		Query qry = getSession().createQuery("from LeaveMaster B where B.desigId.designationId =:desigId and B.typeOfLeaveMstr.id =:typeOfLeaveMstr");
		qry.setInteger("desigId", desigId);
		qry.setInteger("typeOfLeaveMstr", typeId);
		lm = (LeaveMaster)qry.uniqueResult();
		if(lm!=null)
		{
			lm = (LeaveMaster)qry.uniqueResult();
			in = lm.getNoOfDays();
		}
		return in;
		
	}
	
}



