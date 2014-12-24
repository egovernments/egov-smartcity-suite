/*
 * @(#)AttributeDAO.java 3.0, 17 Jun, 2013 12:43:50 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.flexfields;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;

public class AttributeDAO implements AttributeIF {

	private static final Logger LOG = LoggerFactory.getLogger(AttributeDAO.class);

	@Override
	public void insert(final Attribute obj) throws EGOVRuntimeException {

		try {
			HibernateUtil.getCurrentSession().doWork(new Work() {

				@Override
				public void execute(final Connection conn) throws SQLException {
					final PreparedStatement stmt = conn.prepareStatement("INSERT INTO EG_ATTRIBUTEVALUES (ID,APPL_DOMAINID,ATT_TYPEID,ATT_VALUE,DOMAINTXNID)" + "VALUES(SEQ_EG_ATTRIBUTEVALUES.nextval,?,?,?,?) ");
					stmt.setInt(1, obj.getApplDomainId());
					stmt.setInt(2, obj.getAttributeTypeId());
					stmt.setString(3, obj.getAttributeTypeValue());
					stmt.setInt(4, obj.getDomainTxnId());
					stmt.execute();

				}
			});

		} catch (final Exception ex) {
			LOG.error("Error occurred while creating Attribute",ex);
			throw new EGOVRuntimeException("Error occurred while creating Attribute",ex);
		}
	}

	@Override
	public void update(final Attribute obj) throws EGOVRuntimeException {
		try {
			HibernateUtil.getCurrentSession().doWork(new Work() {

				@Override
				public void execute(final Connection conn) throws SQLException {
					final PreparedStatement stmt = conn.prepareStatement("UPDATE EG_ATTRIBUTEVALUES SET APPL_DOMAINID = ?,ATT_TYPEID = ?,ATT_VALUE = ?,DOMAINTXNID=? WHERE ID = ?");
					stmt.setInt(1, obj.getApplDomainId());
					stmt.setInt(2, obj.getAttributeTypeId());
					stmt.setString(3, obj.getAttributeTypeValue());
					stmt.setInt(4, obj.getId());
					stmt.setInt(4, obj.getDomainTxnId());
					stmt.execute();

				}
			});
		} catch (final Exception ex) {
			LOG.error("Error occurred while updating Attribute",ex);
			throw new EGOVRuntimeException("Error occurred while updating Attribute",ex);
		}
	}

	@Override
	public void delete(final Attribute obj) throws EGOVRuntimeException {
		try {
			HibernateUtil.getCurrentSession().doWork(new Work() {

				@Override
				public void execute(final Connection conn) throws SQLException {
					final PreparedStatement stmt = conn.prepareStatement("DELETE FROM EG_ATTRIBUTEVALUES WHERE ID = ?");
					stmt.setInt(1, obj.getId());
					stmt.execute();

				}
			});

		} catch (final Exception ex) {
			LOG.error("Error occurred while deleting Attribute",ex);
			throw new EGOVRuntimeException("Error occurred while deleting Attribute",ex);
		}
	}

	@Override
	public Attribute getAttribute(final int attId) throws EGOVRuntimeException {
		try {
			return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<Attribute>() {
				@Override
				public Attribute execute(final Connection conn) {
					try {
						final PreparedStatement stmt = conn.prepareStatement("SELECT * FROM EG_ATTRIBUTEVALUES WHERE ID = ?");
						stmt.setInt(1, attId);
						final ResultSet rs = stmt.executeQuery();
						if (rs.next()) {
							final Attribute obj = new Attribute();
							obj.setApplDomainId(rs.getInt("APPL_DOMAINID"));
							obj.setAttributeTypeId(rs.getInt("ATT_TYPEID"));
							obj.setAttributeTypeValue(rs.getString("ATT_VALUE"));
							obj.setDomainTxnId(rs.getInt("DOMAINTXNID"));
							return obj;
						}
						return null;
					} catch (final SQLException e) {
						LOG.error("SQL Error occurred while getting Attribute",e);
						throw new EGOVRuntimeException("SQL Error occurred while getting Attribute",e);
					}
				}
			});
		} catch (final Exception ex) {
			LOG.error("Error occurred while getting Attribute",ex);
			throw new EGOVRuntimeException("Error occurred while getting Attribute",ex);
		}

	}

	@Override
	public List<Attribute> getDomainAttributes(final int domainId) throws EGOVRuntimeException {
		try {

			return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<List<Attribute>>() {
				@Override
				public List<Attribute> execute(final Connection conn) {
					try {
						final PreparedStatement stmt = conn.prepareStatement("SELECT * FROM EG_ATTRIBUTEVALUES WHERE APPL_DOMAINID = ?");
						stmt.setInt(1, domainId);
						final ResultSet rs = stmt.executeQuery();
						final List<Attribute> list = new ArrayList<Attribute>();
						while (rs.next()) {
							final Attribute obj = new Attribute();
							obj.setId(rs.getInt("ID"));
							obj.setAttributeTypeId(rs.getInt("ATT_TYPEID"));
							obj.setAttributeTypeValue(rs.getString("ATT_VALUE"));
							obj.setDomainTxnId(rs.getInt("DOMAINTXNID"));
							list.add(obj);
						}
						return list;
					} catch (final SQLException e) {
						LOG.error("SQL Error occurred while getting Domain Attribute",e);
						throw new EGOVRuntimeException("SQL Error occurred while getting Domain Attribute",e);
					}
				}
			});
		} catch (final Exception ex) {
			LOG.error("Error occurred while getting Domain Attribute",ex);
			throw new EGOVRuntimeException("Error occurred while getting Domain Attribute",ex);
		}
	}

	@Override
	public List<Attribute> getDomainTxnAttributes(final int domainId, final int domainTxnId) throws EGOVRuntimeException {
		try {

			return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<List<Attribute>>() {
				@Override
				public List<Attribute> execute(final Connection conn) {
					try {
						final PreparedStatement stmt = conn.prepareStatement("SELECT * FROM EG_ATTRIBUTEVALUES WHERE APPL_DOMAINID = ? AND DOMAINTXNID = ?");
						stmt.setInt(1, domainId);
						stmt.setInt(2, domainTxnId);
						final ResultSet rs = stmt.executeQuery();
						final List<Attribute> list = new ArrayList<Attribute>();
						while (rs.next()) {
							final Attribute obj = new Attribute();
							obj.setId(rs.getInt("ID"));
							obj.setAttributeTypeId(rs.getInt("ATT_TYPEID"));
							obj.setAttributeTypeValue(rs.getString("ATT_VALUE"));
							obj.setDomainTxnId(rs.getInt("DOMAINTXNID"));
							list.add(obj);
						}
						return list;
					} catch (final SQLException e) {
						LOG.error("SQL Error occurred while getting Domain Txn Attribute",e);
						throw new EGOVRuntimeException("SQL Error occurred while getting Domain Txn Attribute",e);
					}
				}
			});
		} catch (final Exception e) {
			LOG.error("Error occurred while getting Domain Txn Attribute",e);
			throw new EGOVRuntimeException("Error occurred while getting Domain Txn Attribute",e);
		}
	}

	@Override
	public Attribute getAttributeProperties(final int domainId, final int domainTxnId, final int attrTypeId) throws EGOVRuntimeException {

		try {
			return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<Attribute>() {
				@Override
				public Attribute execute(final Connection conn) {
					try {
						final PreparedStatement stmt = conn.prepareStatement("SELECT * FROM EG_ATTRIBUTEVALUES WHERE APPL_DOMAINID = ? AND DOMAINTXNID = ? AND ATT_TYPEID = ?");
						stmt.setInt(1, domainId);
						stmt.setInt(2, domainTxnId);
						stmt.setInt(3, attrTypeId);
						final ResultSet rs = stmt.executeQuery();
						if (rs.next()) {
							final Attribute obj = new Attribute();
							obj.setApplDomainId(rs.getInt("APPL_DOMAINID"));
							obj.setAttributeTypeId(rs.getInt("ATT_TYPEID"));
							obj.setAttributeTypeValue(rs.getString("ATT_VALUE"));
							obj.setDomainTxnId(rs.getInt("DOMAINTXNID"));
							return obj;
						}
						return null;
					} catch (final SQLException e) {
						LOG.error("SQL Error occurred while getting Attribute Properties",e);
						throw new EGOVRuntimeException("SQL Error occurred while getting Attribute Properties",e);
					}
				}
			});
		} catch (final Exception ex) {
			LOG.error("Error occurred while getting Attribute Properties",ex);
			throw new EGOVRuntimeException("Error occurred while getting Attribute Properties",ex);
		}
	}
}
