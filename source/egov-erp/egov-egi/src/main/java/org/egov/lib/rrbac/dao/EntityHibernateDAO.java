package org.egov.lib.rrbac.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.lib.rrbac.model.Entity;
import org.hibernate.Query;
import org.hibernate.Session;

public class EntityHibernateDAO extends GenericHibernateDAO implements EntityDAO {
	
	public EntityHibernateDAO() {
		super(Entity.class, null);
	}
	
	public EntityHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	@Override
	public Entity findEntityByName(final String name) {
		final Query qry = getCurrentSession().createQuery("from Entity ent where ent.name =:name ");
		qry.setString("name", name);
		return (Entity) qry.uniqueResult();

	}
}
