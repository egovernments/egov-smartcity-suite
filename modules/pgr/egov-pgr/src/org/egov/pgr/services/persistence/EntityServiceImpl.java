/*
 * @(#)EntityServiceImpl.java 3.0, 23 Jul, 2013 3:29:43 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.services.persistence;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class EntityServiceImpl<T extends Object, ID extends Serializable> extends PersistenceService<T, ID> implements EntityService<T, ID> {

	@Override
	public Session getSession() {
		return HibernateUtil.getCurrentSession();
	}

	@Override
	public PersistenceService<T, ID> getGenericService() {
		return this;
	}

	/**
	 * Public constructor for creating a new BaseServiceImpl.
	 * @param genericDAO
	 */
	public EntityServiceImpl(final Class<T> type) {
		super.setType(type);
	}

	/**
	 * This method retrieves the <code>CFinancialYear</code> for the given date.
	 * @param date an instance of <code>Date</code> for which the financial year is to be retrieved.
	 * @return
	 */
	@Override
	public CFinancialYear getCurrentFinancialYear(final Date date) {
		final List<CFinancialYear> financialYear = this.getSession().createQuery("from CFinancialYear cfinancialyear where ? between cfinancialyear.startingDate and cfinancialyear.endingDate").setDate(0, date).list();

		if (financialYear.isEmpty()) {
			throw new ValidationException(Arrays.asList(new ValidationError("financialyear", "estimate.estimateDate.financialyear.invalid")));
		} else {
			return financialYear.get(0);
		}
	}

	@Override
	public boolean isExist(String keyIdentity, Object keyValue) {
		return !getSession().createCriteria(this.type).add(Restrictions.eq(keyIdentity, keyValue)).list().isEmpty();
	}

}
