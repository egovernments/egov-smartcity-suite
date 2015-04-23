/*
 * Created on Aug 16, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.ptis.domain.dao.property;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

/**
 * @author Admin
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PropertyMutationHibDAO extends GenericHibernateDAO implements PropertyMutationDAO{

	public PropertyMutationHibDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}

}
