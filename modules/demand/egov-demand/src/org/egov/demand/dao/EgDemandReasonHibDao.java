package org.egov.demand.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

public class EgDemandReasonHibDao extends GenericHibernateDAO implements EgDemandReasonDao{

	public EgDemandReasonHibDao(Class persistentClass, Session session) {
		super(persistentClass, session);
	}
}
