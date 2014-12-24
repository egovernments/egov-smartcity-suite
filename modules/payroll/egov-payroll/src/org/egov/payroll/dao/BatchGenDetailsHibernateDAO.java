package org.egov.payroll.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

public class BatchGenDetailsHibernateDAO extends GenericHibernateDAO implements BatchGenDetailsDAO{

	public BatchGenDetailsHibernateDAO(Class persistentClass, Session session) {
	        
		super(persistentClass, session);
		
	}

}
