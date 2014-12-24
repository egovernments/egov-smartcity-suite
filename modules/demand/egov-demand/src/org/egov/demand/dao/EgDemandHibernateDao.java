package org.egov.demand.dao;


import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

public class EgDemandHibernateDao  extends GenericHibernateDAO implements EgDemandDao{

	public EgDemandHibernateDao(Class persistentClass, Session session) {
		super(persistentClass, session);
	}

}
