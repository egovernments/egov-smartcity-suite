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
package org.egov.infstr.flexfields;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DomainDAO implements DomainIF {

	private SessionFactory sessionFactory;

	public DomainDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	private static final Logger LOG = LoggerFactory.getLogger(DomainDAO.class);

	@Override
	public void createDomain(final Domain obj) throws EGOVRuntimeException {
		try {
			getCurrentSession().doWork(new Work() {

				@Override
				public void execute(final Connection conn) throws SQLException {
					final PreparedStatement stmt = conn.prepareStatement("INSERT INTO EG_APPL_DOMAIN (ID,NAME,DESCRIPTION) VALUES(SEQ_EG_DOMAIN.nextval,?,?) ");
					stmt.setString(1, obj.getDomainName());
					stmt.setString(2, obj.getDomainDesc());
					stmt.execute();

					EgovMasterDataCaching.getInstance().removeFromCache("egi-domain");
					EgovMasterDataCaching.getInstance().removeFromCache("egi-domainwtoref");
				}
			});
		} catch (final Exception e) {
			LOG.error("Error occurred while creating Domain", e);
			throw new EGOVRuntimeException("Error occurred while creating Domain", e);
		}
	}

	@Override
	public void updateDomain(final Domain obj) throws EGOVRuntimeException {
		try {
			getCurrentSession().doWork(new Work() {

				@Override
				public void execute(final Connection conn) throws SQLException {
					final PreparedStatement stmt = conn.prepareStatement("UPDATE EG_APPL_DOMAIN SET NAME=?,DESCRIPTION=? WHERE ID=?");
					stmt.setString(1, obj.getDomainName());
					stmt.setString(2, obj.getDomainDesc());
					stmt.setInt(3, obj.getId());
					stmt.execute();

					EgovMasterDataCaching.getInstance().removeFromCache("egi-domain");
					EgovMasterDataCaching.getInstance().removeFromCache("egi-domainwtoref");
				}
			});
		} catch (final Exception e) {
			LOG.error("Error occurred while updating Domain", e);
			throw new EGOVRuntimeException("Error occurred while updating Domain", e);
		}
	}

	@Override
	public void deleteDomain(final int id) throws EGOVRuntimeException {
		try {
			getCurrentSession().doWork(new Work() {

				@Override
				public void execute(final Connection conn) throws SQLException {
					final PreparedStatement stmt = conn.prepareStatement("DELETE FROM EG_APPL_DOMAIN WHERE ID=?");
					stmt.setInt(1, id);
					stmt.execute();

					EgovMasterDataCaching.getInstance().removeFromCache("egi-domain");
					EgovMasterDataCaching.getInstance().removeFromCache("egi-domainwtoref");
				}
			});
		} catch (final Exception e) {
			LOG.error("Error occurred while deleting Domain", e);
			throw new EGOVRuntimeException("Error occurred while deleting Domain", e);
		}
	}

	@Override
	public Domain getDomain(final int id) throws EGOVRuntimeException {
		try {
			return getCurrentSession().doReturningWork(new ReturningWork<Domain>() {

				@Override
				public Domain execute(final Connection conn) throws SQLException {
					final PreparedStatement stmt = conn.prepareStatement("SELECT * FROM EG_APPL_DOMAIN WHERE ID=?");
					stmt.setInt(1, id);
					final ResultSet rs = stmt.executeQuery();
					if (rs.next()) {
						final Domain obj = new Domain();
						obj.setDomainName(rs.getString("NAME"));
						obj.setDomainDesc(rs.getString("DESCRIPTION"));
						return obj;
					}
					return null;
				}
			});
		} catch (final Exception e) {
			LOG.error("Error occurred while getting Domain", e);
			throw new EGOVRuntimeException("Error occurred while getting Domain", e);
		}
	}
}
