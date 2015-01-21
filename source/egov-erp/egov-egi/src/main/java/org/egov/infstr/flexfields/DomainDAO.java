/*
 * @(#)DomainDAO.java 3.0, 17 Jun, 2013 2:34:46 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
