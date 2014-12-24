package org.egov.demand.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

public class EgDemandReasonMasterHibDao extends GenericHibernateDAO implements EgDemandReasonMasterDao{

	public EgDemandReasonMasterHibDao(Class persistentClass, Session session) {
		super(persistentClass, session);
	}

}
