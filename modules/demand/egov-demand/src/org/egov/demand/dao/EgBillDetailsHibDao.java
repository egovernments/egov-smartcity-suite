package org.egov.demand.dao;

import java.util.List;

import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

public class EgBillDetailsHibDao extends GenericHibernateDAO implements EgBillDetailsDao{

	public EgBillDetailsHibDao(Class persistentClass, Session session) {
		super(persistentClass, session);

	}


	public List<EgBillDetails> getBillDetailsByBill(EgBill egBill)
	{
		List<EgBillDetails> billDetList = null;
		Query qry2  = HibernateUtil.getCurrentSession().createQuery("from EgBillDetails billDet where billDet.egBill =:bill");

		if(egBill!=null)
		{
			qry2.setEntity("bill", egBill);
			billDetList= qry2.list();
		}
		return billDetList;
	}

}
