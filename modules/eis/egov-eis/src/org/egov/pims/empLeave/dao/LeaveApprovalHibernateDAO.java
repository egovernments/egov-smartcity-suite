package org.egov.pims.empLeave.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.pims.empLeave.model.LeaveApproval;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the PersonalInformationDAO for the Hibernate specific
 * Implementation
 *
 
 */
public class LeaveApprovalHibernateDAO extends GenericHibernateDAO implements LeaveApprovalDAO
{
    
    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public LeaveApprovalHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public LeaveApproval getLeaveApprovalByID(Integer id)
	{
		Query qry = getSession().createQuery("from LeaveApproval B where B.id =:id ");
		qry.setInteger("id", id);
		return (LeaveApproval)qry.uniqueResult();
	}
	public LeaveApproval getLeaveApprovalByApplicationID(Integer id)
	{
		Query qry = getSession().createQuery("from LeaveApproval B where B.appId.id =:id ");
		qry.setInteger("id", id);
		return (LeaveApproval)qry.uniqueResult();
	}
	
}



