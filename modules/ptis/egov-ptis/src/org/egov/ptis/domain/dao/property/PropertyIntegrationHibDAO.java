package org.egov.ptis.domain.dao.property;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

public class PropertyIntegrationHibDAO extends GenericHibernateDAO implements PropertyIntegrationDAO
{
	public PropertyIntegrationHibDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	
}
