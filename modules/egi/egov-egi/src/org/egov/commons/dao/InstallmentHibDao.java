/*
 * @(#)InstallmentHibDao.java 3.0, 11 Jun, 2013 4:11:52 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.egov.commons.Installment;
import org.egov.infstr.commons.Module;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.DateUtils;
import org.hibernate.Query;
import org.hibernate.Session;

public class InstallmentHibDao<T, id extends Serializable> extends GenericHibernateDAO implements InstallmentDao {

	public InstallmentHibDao() {
		super(Installment.class, null);
	}

	/**
	 * @param persistentClass
	 * @param session
	 */
	public InstallmentHibDao(final Class<T> persistentClass, final Session session) {
		super(persistentClass, session);
	}

	@Override
	public List getInsatllmentByModule(final Module module) {
		final Query qry = getSession().createQuery("from Installment I where I.module=:module");
		qry.setEntity("module", module);

		return qry.list();
	}

	@Override
	public List getInsatllmentByModule(final Module module, final Date year) {
		final Query qry = getSession().createQuery("from Installment I where I.module=:module and I.installmentYear=:year");
		qry.setEntity("module", module);
		qry.setDate("year", year);

		return qry.list();
	}

	@Override
	public Installment getInsatllmentByModule(final Module module, final Date year, final Integer installmentNumber) {
		final Query qry = getSession().createQuery("from Installment I where I.module=:module and I.installmentYear=:year and I.installmentNumber =:instNum");
		qry.setEntity("module", module);
		qry.setDate("year", year);
		qry.setInteger("instNum", installmentNumber);

		return (Installment) qry.uniqueResult();
	}

	@Override
	public Installment getInsatllmentByModuleForGivenDate(final Module module, final Date installmentDate) {
		final Query qry = getSession().createQuery("from Installment I where I.module=:module and (I.fromDate <= :fromYear and I.toDate >=:toYear)");
		qry.setEntity("module", module);
		qry.setDate("fromYear", installmentDate);
		qry.setDate("toYear", installmentDate);

		return (Installment) qry.uniqueResult();

	}

	@Override
	public List<Installment> getEffectiveInstallmentsforModuleandDate(final Date dateToCompare, final int noOfMonths, final Module mod) {
		final Query qry = getSession().createQuery("from org.egov.commons.Installment inst where  inst.toDate >= :dateToCompare and inst.toDate < :dateToComparemax   and inst.module=:module");
		qry.setDate("dateToCompare", dateToCompare);
		qry.setEntity("module", mod);
		final Date dateToComparemax = DateUtils.add(dateToCompare, Calendar.MONTH, noOfMonths);
		qry.setDate("dateToComparemax", dateToComparemax);

		return qry.list();
	}
}
