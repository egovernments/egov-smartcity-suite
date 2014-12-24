package org.egov.demand.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

public class EgdmCollectedReceiptHibDao extends GenericHibernateDAO implements EgdmCollectedReceiptDao{

	public EgdmCollectedReceiptHibDao(Class persistentClass, Session session) {
		super(persistentClass, session);
	}

}
