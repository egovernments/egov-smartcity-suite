package org.egov.demand.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

public class EgReasonCategoryHibernateDao extends GenericHibernateDAO implements EgReasonCategoryDao{
	
	public EgReasonCategoryHibernateDao(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}

}
