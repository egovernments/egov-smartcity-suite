package org.egov.demand.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

public class EgDemandDetailsHibDao  extends GenericHibernateDAO implements EgDemandDetailsDao{

	public EgDemandDetailsHibDao(Class persistentClass, Session session) {
		super(persistentClass, session);
	}

}
