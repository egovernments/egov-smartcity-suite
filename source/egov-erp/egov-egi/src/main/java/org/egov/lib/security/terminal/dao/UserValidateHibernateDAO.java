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

import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.security.utils.CryptoHelper;
import org.egov.lib.security.terminal.model.Location;
import org.egov.lib.security.terminal.model.UserValidate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Date;

public class UserValidateHibernateDAO implements UserValidateDAO {

	private SessionFactory sessionFactory;

	public UserValidateHibernateDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}


	@Override
	public boolean validateUser(final UserValidate obj) {
		final Query qry = getCurrentSession().createQuery("from User user where user.username = :username and user.password = :password");
		qry.setString("username", obj.getUsername());
		qry.setString("password", CryptoHelper.encrypt(obj.getPassword()));
		return !qry.list().isEmpty();
	}

	@Override
	public boolean validateUserLocation(final UserValidate obj) {
		final StringBuilder queryStr = new StringBuilder();
		queryStr.append("Select user from Location loc,LocationIPMap locIP,User user, UserCounterMap map where map.userId = user.id and locIP.ipAddress = :ipaddress and  loc.id=locIP.location.id and ");
		queryStr.append("map.counterId = loc.id and loc.isActive=1 and user.isActive =true and user.username = :username and user.password = :password ");
		queryStr.append("and loc.id = :locationId And ((map.toDate IS NULL AND map.fromDate <= :currDate) OR (map.fromDate <= :currDate AND map.toDate >= :currDate))");
		final Query qry = getCurrentSession().createQuery(queryStr.toString());
		qry.setString("username", obj.getUsername());
		qry.setString("password", CryptoHelper.encrypt(obj.getPassword()));
		qry.setString("ipaddress", obj.getIpAddress());
		qry.setDate("currDate", new Date());
		qry.setInteger("locationId", obj.getLocationId());
		return !qry.list().isEmpty();
	}

	@Override
	public boolean validateUserTerminal(final UserValidate obj) {
		final StringBuilder queryStr = new StringBuilder();
		queryStr.append("select user from Location loc,LocationIPMap locIP, User user, UserCounterMap map where map.userId = user.id and locIP.ipAddress = :ipaddress and  loc.id=locIP.location.id and ");
		queryStr.append("map.counterId = loc.id and loc.isActive=1 and user.isActive = true and user.username = :username and user.password = :password ");
		queryStr.append("and loc.id = :counterId And ((map.toDate IS NULL AND map.fromDate <= :currDate) OR (map.fromDate <= :currDate AND map.toDate >= :currDate))");
		final Query qry = getCurrentSession().createQuery(queryStr.toString());
		qry.setString("username", obj.getUsername());
		qry.setString("password", CryptoHelper.encrypt(obj.getPassword()));
		qry.setString("ipaddress", obj.getIpAddress());
		qry.setDate("currDate", new Date());
		qry.setInteger("counterId", obj.getCounterId());
		return !qry.list().isEmpty();
	}

	@Override
	public Location getLocationByIP(final String ipAddress) {
		final Query qry = getCurrentSession().createQuery("select loc from Location loc,LocationIPMap locIP where locIP.ipAddress = :ipAddress and loc.locationId is null and loc.id=locIP.location.id and loc.isActive=1");
		qry.setString("ipAddress", ipAddress);
		return (Location) qry.uniqueResult();
	}

	@Override
	public Location getTerminalByIP(final String ipAddress) {
		final Query qry = getCurrentSession().createQuery("select loc from Location loc,LocationIPMap locIP where locIP.ipAddress = :ipAddress and loc.locationId is not null and loc.id=locIP.location.id and loc.isActive=1");
		qry.setString("ipAddress", ipAddress);
		return (Location) qry.uniqueResult();
	}

	/**
	 * API validtes whenther the given user is valid as if for current date and also the user should active in that period. 
	 * if true -- > the user is valid and acitve as of current date if false -- > 
	 * the user is not valid or not acitve as of current date
	 * @return boolean value
	 */
	@Override
	public boolean validateActiveUserForPeriod(final String userName) {
		final Query qry = getCurrentSession().createQuery(
				"select user from User user where user.username = :username and user.isActive = true");
		qry.setString("username", userName);
		final User user = (User) qry.uniqueResult();
		return user != null;
	}
}
