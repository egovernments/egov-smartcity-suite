/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.commons.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
	
	public FinancialYearHibernateDAO(final Session session) {
		super(CFinancialYear.class, session);
	}
	
	@PersistenceContext
	private EntityManager entityManager;
	
	
	public Session getCurrentSession()
	{
		return entityManager.unwrap(Session.class);
	}

	@Override
	public String getCurrYearFiscalId() {
		final Query query = getCurrentSession().createQuery("select cfinancialyear.id from CFinancialYear cfinancialyear where cfinancialyear.startingDate <= :startDate and cfinancialyear.endingDate >= :endDate ");
		final Date currentDate = new Date();
		query.setDate("startDate", currentDate);
		query.setDate("endDate", currentDate);
		return  query.list().get(0).toString();
	}

	@Override
	public String getCurrYearStartDate() {
		String result = "";
		final Query query = getCurrentSession().createQuery("select cfinancialyear.startingDate from CFinancialYear cfinancialyear where cfinancialyear.startingDate <= :startDate and cfinancialyear.endingDate >= :endDate ");
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
		final Query query = getCurrentSession().createQuery("select cfinancialyear.id from CFinancialYear cfinancialyear where cfinancialyear.startingDate <= :startDate and cfinancialyear.endingDate >= :endDate ");
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

		final Query query = getCurrentSession().createQuery("from CFinancialYear cfinancialyear where cfinancialyear.finYearRange=:finYearRange");
		query.setString("finYearRange", finYearRange);
		return (CFinancialYear) query.uniqueResult();
	}

	@Override
	public List<CFinancialYear> getAllActiveFinancialYearList() {
		return getCurrentSession().createQuery("from CFinancialYear cfinancialyear where isActive=1 order by id desc").list();
	}

	@Override
	public List<CFinancialYear> getAllActivePostingFinancialYear() {
		return getCurrentSession().createQuery("from CFinancialYear cfinancialyear where isActive=1 and isActiveForPosting=1 order by id desc").list();
	}

	@Override
	public CFinancialYear getFinancialYearById(final Long id) {
		final Query query = getCurrentSession().createQuery("from CFinancialYear cfinancialyear where id=:id");
		query.setLong("id", id);
		return (CFinancialYear) query.uniqueResult();
	}

	@Override
	public CFinancialYear getFinancialYearByDate(final Date date) {

		CFinancialYear cFinancialYear = null;
		final Query query = getCurrentSession().createQuery(" from CFinancialYear cfinancialyear where cfinancialyear.startingDate <=:sDate and cfinancialyear.endingDate >=:eDate  and cfinancialyear.isActiveForPosting=1");
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

		final Query query = getCurrentSession().createQuery(" from CFinancialYear cfinancialyear where cfinancialyear.startingDate <=:sDate and cfinancialyear.endingDate >=:eDate");
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
