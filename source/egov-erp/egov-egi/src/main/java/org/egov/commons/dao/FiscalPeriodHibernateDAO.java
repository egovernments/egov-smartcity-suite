/*
 * @(#)FiscalPeriodHibernateDAO.java 3.0, 11 Jun, 2013 2:48:37 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.util.ArrayList;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

public class FiscalPeriodHibernateDAO extends GenericHibernateDAO implements FiscalPeriodDAO {
	
	public FiscalPeriodHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);

	}

	@Override
	public String getFiscalPeriodIds(final String financialYearId) {
		final StringBuffer result = new StringBuffer();
		final Query query = HibernateUtil.getCurrentSession().createQuery("select cfiscalperiod.id from CFiscalPeriod cfiscalperiod where cfiscalperiod.financialYearId = '" + financialYearId + "'  ");
		final ArrayList list = (ArrayList) query.list();
		if (list.size() > 0) {
			if (list.get(0) == null) {
				return 0.0 + "";
			} else {
				for (int i = 0; i < list.size(); i++) {
					result.append(list.get(i).toString());
					if (list.size() - i != 1) {
						result.append(",");
					}
				}
			}
		} else {
			return 0.0 + "";
		}
		return result.toString();
	}

}
