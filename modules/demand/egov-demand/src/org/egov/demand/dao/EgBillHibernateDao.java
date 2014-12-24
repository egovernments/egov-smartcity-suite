package org.egov.demand.dao;

import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillType;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

public class EgBillHibernateDao extends GenericHibernateDAO implements EgBillDao {

	public EgBillHibernateDao(Class persistentClass, Session session) {
		super(persistentClass, session);
	}

	public EgBillHibernateDao() {
		super(EgBill.class, HibernateUtil.getCurrentSession());
	}

	public EgBillType getBillTypeByCode(String code) {
		final Query qry = this.getSession().createQuery("from EgBillType bt where bt.code =:BTCODE");
		qry.setString("BTCODE", code);
		qry.setMaxResults(1);
		return (EgBillType) qry.uniqueResult();
	}
}
