package org.egov.dao.bills;

import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.hibernate.Session;

public class BillsHibernateDaoFactory extends BillsDaoFactory 
{
	public BillsHibernateDaoFactory()
	{
		super();
	}
	protected Session getCurrentSession()
	{
		HibernateUtil.beginTransaction();
		return HibernateUtil.getCurrentSession();
	}
	@Override
	public EgBillRegisterHibernateDAO getEgBillRegisterHibernateDAO() {
		return new EgBillRegisterHibernateDAO(EgBillregister.class,getCurrentSession());
	}
	@Override
	public EgBilldetailsDAO getEgBilldetailsDAO() {
		return new EgBilldetailsHibernateDAO(EgBilldetails.class, getCurrentSession());  
	}

	
}
