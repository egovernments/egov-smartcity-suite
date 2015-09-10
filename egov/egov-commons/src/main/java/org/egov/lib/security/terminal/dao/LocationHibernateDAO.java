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

import java.util.ArrayList;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.lib.security.terminal.model.Location;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocationHibernateDAO extends GenericHibernateDAO implements LocationDAO {

	private static final Logger LOG = LoggerFactory.getLogger(LocationHibernateDAO.class);

	/**
	 * Instantiates a new location hibernate dao.
	 * @param persistentClass the persistent class
	 * @param session the session
	 */
	public LocationHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	@Override
	public Location getLocationIdByLocationNameAndCounter(final String locationName, final String counterName) {
		final Query qry = getCurrentSession().createQuery("select loc1 from Location loc,Location loc1 where loc.id=loc1.locationId and loc.name = :locationName and loc1.name= :counterName");
		qry.setString("locationName", locationName);
		qry.setString("counterName", counterName);
		return (Location) qry.uniqueResult();
	}

	@Override
	public ArrayList<Location> getCountersByLocation(final int locationId) {
		try {
			final Query qry = getCurrentSession().createQuery("from Location loc where loc.locationId=:locationId");
			qry.setLong("locationId", locationId);
			final ArrayList<Location> locList = new ArrayList<Location>(qry.list());
			return locList;
		} catch (final Exception e) {
			LOG.error("Exception in getting Counters By Location", e);
			throw new ApplicationRuntimeException("Exception in getCountersByLocation", e);

		}
	}

	/**
	 * API checks whether user entered ip address name is already presented in database. returns true --> 
	 * If the record is found in the database for user input returns false --> If the record is not 
	 * found in the database for user input
	 * @param ipValue the ip value
	 * @return true, if successful
	 */
	@Override
	public boolean checkIPAddress(final String ipValue) {
		boolean b = false;
		try {
			final Query qry = getCurrentSession().createQuery("from LocationIPMap ip where ip.ipAddress=:ipValue");
			qry.setString("ipValue", ipValue);
			if (qry.uniqueResult() != null) {
				b = true;
			}

		} catch (final HibernateException he) {
			LOG.error("Exception occurred while check IP Address", he);
			throw new ApplicationRuntimeException("Exception occurred in checkIPAddress", he);
		} catch (final Exception e) {
			LOG.error("Exception occurred while check IP Address", e);
			throw new ApplicationRuntimeException("Exception occurred in checkIPAddress", e);
		}
		return b;

	}

	/**
	 * API checks whether user entered counter name is already presented in database. returns true --> 
	 * If the record is found in the database for user input returns false --> 
	 * If the record is not found in the database for user input
	 * @param name the name
	 * @return true, if successful
	 */
	@Override
	public boolean checkCounter(final String name) {
		boolean b = false;
		try {
			final Query qry = getCurrentSession().createQuery("from Location loc where loc.name=:name");
			qry.setString("name", name);
			if (qry.uniqueResult() != null) {
				b = true;
			}

		} catch (final HibernateException he) {
			LOG.error("Exception occurred while check Counter", he);
			throw new ApplicationRuntimeException("Exception occurred in checkCounter", he);
		} catch (final Exception e) {
			LOG.error("Exception occurred while check Counter", e);
			throw new ApplicationRuntimeException("Exception occurred in checkCounter", e);
		}
		return b;

	}
}
