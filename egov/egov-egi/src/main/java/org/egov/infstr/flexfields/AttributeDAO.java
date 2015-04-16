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
import java.util.ArrayList;
import java.util.List;

public class AttributeDAO implements AttributeIF {

	private static final Logger LOG = LoggerFactory.getLogger(AttributeDAO.class);
	private SessionFactory sessionFactory;

	public AttributeDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public void insert(final Attribute obj) throws EGOVRuntimeException {

		try {
			getSession().doWork(new Work() {

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
			getSession().doWork(new Work() {

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
			getSession().doWork(new Work() {

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
			return getSession().doReturningWork(new ReturningWork<Attribute>() {
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
						LOG.error("SQL Error occurred while getting Attribute", e);
						throw new EGOVRuntimeException("SQL Error occurred while getting Attribute", e);
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

			return getSession().doReturningWork(new ReturningWork<List<Attribute>>() {
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
						LOG.error("SQL Error occurred while getting Domain Attribute", e);
						throw new EGOVRuntimeException("SQL Error occurred while getting Domain Attribute", e);
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

			return getSession().doReturningWork(new ReturningWork<List<Attribute>>() {
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
						LOG.error("SQL Error occurred while getting Domain Txn Attribute", e);
						throw new EGOVRuntimeException("SQL Error occurred while getting Domain Txn Attribute", e);
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
			return getSession().doReturningWork(new ReturningWork<Attribute>() {
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
						LOG.error("SQL Error occurred while getting Attribute Properties", e);
						throw new EGOVRuntimeException("SQL Error occurred while getting Attribute Properties", e);
					}
				}
			});
		} catch (final Exception ex) {
			LOG.error("Error occurred while getting Attribute Properties",ex);
			throw new EGOVRuntimeException("Error occurred while getting Attribute Properties",ex);
		}
	}
}
