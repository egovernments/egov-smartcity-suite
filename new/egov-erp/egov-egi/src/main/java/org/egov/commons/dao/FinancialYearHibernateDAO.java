/*
 * @(#)FinancialYearHibernateDAO.java 3.0, 11 Jun, 2013 2:45:20 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

public class FinancialYearHibernateDAO extends GenericHibernateDAO implements FinancialYearDAO {

	public FinancialYearHibernateDAO() {
		super(CFinancialYear.class, null);
	}

	public FinancialYearHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);

	}

	@Override
	public String getCurrYearFiscalId() {
		final Query query = getSession().createQuery("select cfinancialyear.id from CFinancialYear cfinancialyear where cfinancialyear.startingDate <= :startDate and cfinancialyear.endingDate >= :endDate ");
		final Date currentDate = new Date();
		query.setDate("startDate", currentDate);
		query.setDate("endDate", currentDate);
		return  query.list().get(0).toString();
	}

	@Override
	public String getCurrYearStartDate() {
		String result = "";
		final Query query = getSession().createQuery("select cfinancialyear.startingDate from CFinancialYear cfinancialyear where cfinancialyear.startingDate <= :startDate and cfinancialyear.endingDate >= :endDate ");
		final Date currentDate = new Date();
		query.setDate("startDate", currentDate);
		query.setDate("endDate", currentDate);
		final ArrayList list = (ArrayList) query.list();
		if (list.size() > 0) {
			if (list.get(0) == null) {
				return 0.0 + "";
			} else {
				result = list.get(0).toString();
			}
		} else {
			return 0.0 + "";
		}
		return result;
	}

	@Override
	public String getPrevYearFiscalId() {
		final Calendar prevYearDate = Calendar.getInstance();
		prevYearDate.setTime(new Date());
		prevYearDate.add(Calendar.YEAR, -1);
		String result = "";
		final Query query = getSession().createQuery("select cfinancialyear.id from CFinancialYear cfinancialyear where cfinancialyear.startingDate <= :startDate and cfinancialyear.endingDate >= :endDate ");
		query.setDate("startDate", prevYearDate.getTime());
		query.setDate("endDate", prevYearDate.getTime());
		final ArrayList list = (ArrayList) query.list();
		if (list.size() > 0) {
			if (list.get(0) == null) {
				return 0.0 + "";
			} else {
				result = list.get(0).toString();
			}
		} else {
			return 0.0 + "";
		}
		return result;
	}

	@Override
	public CFinancialYear getFinancialYearByFinYearRange(final String finYearRange) {

		final Query query = getSession().createQuery("from CFinancialYear cfinancialyear where cfinancialyear.finYearRange=:finYearRange");
		query.setString("finYearRange", finYearRange);
		return (CFinancialYear) query.uniqueResult();
	}

	@Override
	public List<CFinancialYear> getAllActiveFinancialYearList() {
		return getSession().createQuery("from CFinancialYear cfinancialyear where isActive=1 order by id desc").list();
	}

	@Override
	public List<CFinancialYear> getAllActivePostingFinancialYear() {
		return getSession().createQuery("from CFinancialYear cfinancialyear where isActive=1 and isActiveForPosting=1 order by id desc").list();
	}

	@Override
	public CFinancialYear getFinancialYearById(final Long id) {
		final Query query = getSession().createQuery("from CFinancialYear cfinancialyear where id=:id");
		query.setLong("id", id);
		return (CFinancialYear) query.uniqueResult();
	}

	@Override
	public CFinancialYear getFinancialYearByDate(final Date date) {

		CFinancialYear cFinancialYear = null;
		final Query query = getSession().createQuery(" from CFinancialYear cfinancialyear where cfinancialyear.startingDate <=:sDate and cfinancialyear.endingDate >=:eDate  and cfinancialyear.isActiveForPosting=1");
		query.setDate("sDate", date);
		query.setDate("eDate", date);
		final ArrayList list = (ArrayList) query.list();
		if (list.size() > 0) {
			cFinancialYear = (CFinancialYear) list.get(0);
		}
		if (null == cFinancialYear) {
			throw new EGOVRuntimeException("Financial Year Id does not exist.");
		}
		return cFinancialYear;
	}

	@Override
	public CFinancialYear getFinYearByDate(final Date date) {

		final Query query = getSession().createQuery(" from CFinancialYear cfinancialyear where cfinancialyear.startingDate <=:sDate and cfinancialyear.endingDate >=:eDate");
		query.setDate("sDate", date);
		query.setDate("eDate", date);
		final ArrayList list = (ArrayList) query.list();
		if (list.isEmpty()) {
			throw new EGOVRuntimeException("Financial Year Id does not exist.");
		} else {
			return (CFinancialYear)list.get(0);
		}
	}

	public CFinancialYear getPreviousFinancialYearByDate(final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, -1);
		return getFinYearByDate(cal.getTime());
	}

	/**
	 * checks whether two dates fall in same financial Year
	 */
	public boolean isSameFinancialYear(final Date fromDate, final Date toDate) {
		return getFinYearByDate(fromDate).getId().longValue() == getFinYearByDate(toDate).getId().longValue();

	}
}
