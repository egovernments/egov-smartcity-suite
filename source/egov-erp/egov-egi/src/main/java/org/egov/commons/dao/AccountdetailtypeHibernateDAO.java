/*
 * @(#)AccountdetailtypeHibernateDAO.java 3.0, 10 Jun, 2013 7:25:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.lang.reflect.Field;

import org.egov.commons.Accountdetailtype;
import org.egov.exceptions.EGOVException;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

public class AccountdetailtypeHibernateDAO extends GenericHibernateDAO {

	public AccountdetailtypeHibernateDAO() {
		super(Accountdetailtype.class, null);
	}

	public AccountdetailtypeHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	/**
	 * This API will return the detailtypeid for the object that is passed. If this master is mapped in the accountdetailtype , 
	 * it will return the id else null. Assumption: Any object that is passed to this API needs to have an instance variable by 
	 * name "tablename". tablename is the table that object refers to. If this instance variable is not present in the object, 
	 * its assumed that this object is not set as a subledgertype. If there is a value for instance variable "tablename" , 
	 * this will be checked against accountdetailtype.tablename.
	 * @param master
	 * @return
	 * @throws EGOVException
	 */
	public Integer getDetailtypeforObject(final Object master) throws EGOVException {
		if (null == master) {
			throw new EGOVException("The object supplied is null");
		}
		try {
			final Field tableNameField = Class.forName(master.getClass().getName()).getDeclaredField("tablename");
			tableNameField.setAccessible(true);
			final Query query = HibernateUtil.getCurrentSession().createQuery("select adt.id from Accountdetailtype adt where UPPER(tablename)=:tableName");
			query.setString("tableName", ((String) tableNameField.get(master)).toUpperCase());
			Integer detailtypeid = null;
			if (query.uniqueResult() != null) {
				detailtypeid = (Integer) query.uniqueResult();
			}
			return detailtypeid;
		} catch (final NoSuchFieldException e) {
			return null;// return the null if the object passed doesnot have the instance variable tablename.
		} catch (final Exception e) {
			throw new EGOVException("Exception occured while getting detailtypeid ", e);
		}

	}
}