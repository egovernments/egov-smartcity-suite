package org.egov.pims.empLeave.dao;
import java.util.List;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.pims.empLeave.model.TypeOfLeaveMaster;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the PersonalInformationDAO for the Hibernate specific
 * Implementation
 *
 * @author Neetu
 * @version 2.00
 */
public class TypeOfLeaveMasterHibernateDAO extends GenericHibernateDAO implements TypeOfLeaveMasterDAO
{
   
    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public TypeOfLeaveMasterHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public TypeOfLeaveMaster getTypeOfLeaveMasterByID(Integer id)
	{
		Query qry = getSession().createQuery("from TypeOfLeaveMaster B where B.id =:id ");
		qry.setInteger("id", id);
		return (TypeOfLeaveMaster)qry.uniqueResult();
	}
	public TypeOfLeaveMaster getTypeOfLeaveMasterByName(String  name)
	{
		Query qry = getSession().createQuery("from TypeOfLeaveMaster B where B.name =:name ");
		qry.setString("name", name);
		return (TypeOfLeaveMaster)qry.uniqueResult();
	}
	public List getListOfAccumulativeTypeOfLeaves()
	{
		Query qry = getSession().createQuery("from TypeOfLeaveMaster B where B.accumulate = 1");		
		return (List)qry.list();
	}
	
	
	
	
}