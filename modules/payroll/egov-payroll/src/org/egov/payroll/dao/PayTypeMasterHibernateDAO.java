/**
 * PayTypeMasterHibernateDAO.java
 * 7 May, 2008 10:22:57 PM
 * Eluri
 */
package org.egov.payroll.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.payroll.model.PayTypeMaster;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author Eluri
 *
 */
public class PayTypeMasterHibernateDAO extends GenericHibernateDAO implements PayTypeMasterDAO{

	public PayTypeMasterHibernateDAO(Class arg0, Session arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	/* (non-Javadoc)
	 * @see org.egov.payroll.dao.PayTypeMasterDAO#getPayTypeMasterByPaytype(java.lang.String)
	 */
	public PayTypeMaster getPayTypeMasterByPaytype(String paytype) {
		Query qry = getSession().createQuery("from PayTypeMaster  where paytype =:paytype ");
		
		qry.setString("paytype",paytype);
		
		return (PayTypeMaster)qry.uniqueResult();
	}
}
