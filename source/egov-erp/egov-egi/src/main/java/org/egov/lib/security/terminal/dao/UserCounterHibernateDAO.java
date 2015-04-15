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
package org.egov.lib.security.terminal.dao;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.security.terminal.model.UserCounterMap;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class UserCounterHibernateDAO extends GenericHibernateDAO<UserCounterMap, Integer> implements UserCounterDAO {

	private static final Logger LOG = LoggerFactory.getLogger(UserCounterHibernateDAO.class);
	private SessionFactory sessionFactory;

	public UserCounterHibernateDAO(SessionFactory sessionFactory) {
		super(UserCounterMap.class, null);
		this.sessionFactory = sessionFactory;
	}

	@Override
	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Deprecated
	public UserCounterHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	@Override
	public UserCounterMap create(final UserCounterMap userCounterMap) {
		super.create(userCounterMap);
		EgovMasterDataCaching.getInstance().removeFromCache("egi-usercountermap");
		return userCounterMap;
	}

	@Override
	public UserCounterMap update(final UserCounterMap userCounterMap) {
		super.update(userCounterMap);
		EgovMasterDataCaching.getInstance().removeFromCache("egi-usercountermap");
		return userCounterMap;
	}

	@Override
	public void delete(final UserCounterMap userCounterMap) {
		super.delete(userCounterMap);
		EgovMasterDataCaching.getInstance().removeFromCache("egi-usercountermap");
	}

	@Override
	public void deleteCounters(final int counterId) {
		try {
			final Query qry = getCurrentSession().createQuery("delete from UserCounterMap map where map.counterId=:counterId");
			qry.setLong("counterId", counterId);
			qry.executeUpdate();
			EgovMasterDataCaching.getInstance().removeFromCache("egi-usercountermap");
		} catch (final Exception e) {			
			LOG.error(" Exception in deleteCounters", e);
			throw new EGOVRuntimeException("Exception in deleteCounters", e);

		}
	}

	@Override
	public List<UserCounterMap> getLocationBasedUserCounterMapForCurrentDate(final Integer locId) {
		try {
			final Query qry = getCurrentSession().createQuery(" from UserCounterMap map where map.counterId=:locId and ((map.toDate is null ) or  (map.toDate >= :toDate)) ");
			qry.setDate("toDate", new Date());
			qry.setInteger("locId", locId);
			return qry.list();
		} catch (final Exception e) {			
			LOG.error(" Exception in getUserCounterMapForCurrentDate", e);
			throw new EGOVRuntimeException("Exception in getUserCounterMapForCurrentDate", e);

		}

	}

	@Override
	public List<UserCounterMap> getTerminalBasedUserCounterMapForCurrentDate(final Integer locId) {
		try {
			final Query qry = getCurrentSession().createQuery("select map from UserCounterMap map,Location loc where  map.counterId=loc.id and loc.locationId=:locId and ((map.toDate is null ) or  (map.toDate >= :toDate)) ");
			qry.setDate("toDate", new Date());
			qry.setInteger("locId", locId);
			return qry.list();

		} catch (final Exception e) {			
			LOG.error(" Exception in getTerminalBasedUserCounterMapForCurrentDate", e);
			throw new EGOVRuntimeException("Exception in getTerminalBasedUserCounterMapForCurrentDate", e);

		}
	}

	@Override
	public List<UserCounterMap> getUserCounterMapForLocationId(final Integer Id) {
		try {
			final Query qry = getCurrentSession().createQuery(" from UserCounterMap map where  map.counterId.id =:Id ");
			qry.setInteger("Id", Id);
			return qry.list();

		} catch (final Exception e) {			
			LOG.error(" Exception in getUserCounterMapForLocationId", e);
			throw new EGOVRuntimeException("Exception in getUserCounterMapForLocationId", e);

		}

	}

	@Override
	public List<UserCounterMap> getUserCounterMapForTerminalId(final Integer Id) {
		try {
			final Query qry = getCurrentSession().createQuery(" select map from UserCounterMap map,Location loc where  map.counterId=loc.id and loc.locationId=:Id");
			qry.setInteger("Id", Id);
			return qry.list();

		} catch (final Exception e) {			
			LOG.error(" Exception in getUserCounterMapForTerminalId", e);
			throw new EGOVRuntimeException("Exception in getUserCounterMapForTerminalId", e);
		}

	}

	@Override
	public List<UserCounterMap> getUserCounterMapForUserId(final Integer Id) {
		try {
			final Query qry = getCurrentSession().createQuery(" from UserCounterMap map where map.userId.id =:Id");
			qry.setInteger("Id", Id);
			return qry.list();

		} catch (final Exception e) {			
			LOG.error(" Exception in getUserCounterMapForUserId", e);
			throw new EGOVRuntimeException("Exception in getUserCounterMapForUserId", e);

		}

	}

	/**
	 * API checks whether the user is overlapped in that period. 
	 * case 1: whenever both fromdate and todate are given 
	 * a)The API checks whether the fromdate and todate falls inbetween the db dates. 
	 * b)The API checks whether the todate in db overlaps with the entered input 
	 * c)The API checks whether the fromdate in db overlaps with the entered input case 
	 * 2: only the fromdate is given 
	 * a)The API checks whether the entered fromdate is overlaps any of the todates present in the db. 
	 * result : if the given dates doesn't overlaps with the period in the database then it returns true else false.
	 */
	@Override
	public boolean checkUserCounter(final Long userId, final Date fromDate, final Date toDate) {
		boolean b = false;
		// If there is no user, there is no check required
		if (userId == null) {
			return false;
		}
		try {

			StringBuffer queryStr = new StringBuffer("select ev.userId  from UserCounterMap ev  where ev.userId = :userId ");
			if (fromDate != null && toDate != null) {
				queryStr = queryStr.append(" and " + "((ev.toDate is null ) or " + " (ev.fromDate <= :fromDate and ev.toDate >= :toDate) or " + " (ev.toDate <= :toDate and ev.toDate >= :fromDate) or "
						+ " (ev.fromDate >= :fromDate and ev.fromDate <= :toDate))  ");
			} else if (fromDate != null && toDate == null) {
				queryStr = queryStr.append("and ((ev.toDate is null ) or " + "(ev.toDate >= :fromDate))  ");

			}
			final Query qry = HibernateUtil.getCurrentSession().createQuery(queryStr.toString());
			qry.setLong("userId", userId);

			if (fromDate != null && toDate != null) {
				qry.setDate("fromDate", new java.sql.Date(fromDate.getTime()));
				qry.setDate("toDate", new java.sql.Date(toDate.getTime()));

			} else if (fromDate != null && toDate == null) {
				qry.setDate("fromDate", new java.sql.Date(fromDate.getTime()));
			}

			if (qry.list() != null && !qry.list().isEmpty()) {
				b = true;
			}

		} catch (final HibernateException he) {
			throw new EGOVRuntimeException("Exception in checkUserCounter", he);
		} catch (final Exception he) {
			throw new EGOVRuntimeException("Exception in checkUserCounter", he);
		}
		return b;

	}
}
