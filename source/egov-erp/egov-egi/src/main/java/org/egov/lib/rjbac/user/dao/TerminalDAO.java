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
package org.egov.lib.rjbac.user.dao;

import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.lib.rjbac.user.Terminal;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TerminalDAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TerminalDAO.class);
	private SessionFactory sessionFactory;

	public TerminalDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * This method called getTerminal()
	 * @param java.lang.String ipaddress
	 * @return java.util.List of array objects
	 * @exception:HibernateException
	 */
	public List<Terminal> getTerminal(final String ipAddr) {
		try {
			final Query qry = sessionFactory.getCurrentSession().createQuery("FROM TerminalImpl TI WHERE TI.ipAddress = :ipAddr ");
			qry.setString("ipAddr", ipAddr);
			return qry.list();
		} catch (final HibernateException e) {
			LOGGER.error("Exception encountered in getTerminal", e);
			throw new EGOVRuntimeException("Exception encountered in getTerminal", e);
		}
	}
}
