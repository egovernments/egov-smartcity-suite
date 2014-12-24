package org.egov.demand.dao;

import org.egov.demand.model.BillReceipt;
import org.egov.demand.model.EgBill;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class EgBillReceiptHibDao extends GenericHibernateDAO implements EgBillReceiptDao{

	public EgBillReceiptHibDao(Class persistentClass, Session session) {
		super(persistentClass, session);
	}

	public BillReceipt getBillReceiptByEgBill(EgBill bill)
	{
		BillReceipt billRecpt = null;
		if(bill!=null)
		{
			Criteria criteria =getSession().createCriteria(BillReceipt.class)
			.add(Restrictions.eq("billId", bill));
			if(criteria.list()!=null && !criteria.list().isEmpty())
			{
				billRecpt=(BillReceipt)criteria.list().get(0);
			}
		}

		return billRecpt;

	}

}
