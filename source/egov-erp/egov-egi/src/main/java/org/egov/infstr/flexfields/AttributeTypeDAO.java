/*
 * @(#)AttributeTypeDAO.java 3.0, 17 Jun, 2013 1:12:52 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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

public class AttributeTypeDAO implements AttributeTypeIF {

	private static final Logger LOG = LoggerFactory.getLogger(AttributeTypeDAO.class);

	private SessionFactory sessionFactory;

	public AttributeTypeDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Deprecated
	public AttributeTypeDAO() {
	}


	@Override
	public void createAttributeType(final AttributeType typeObj) throws EGOVRuntimeException {
		try {
			getSession().doWork(new Work() {
				@Override
				public void execute(final Connection conn) throws SQLException {
					final PreparedStatement stmt = conn.prepareStatement("INSERT INTO EG_ATTRIBUTETYPE (ID,APPL_DOMAINID,ATT_NAME,ATT_DATATYPE,DEFAULT_VALUE,ISREQUIRED,ISLIST)" + "VALUES(SEQ_EG_ATTRIBUTETYPE.nextval,?,?,?,?,?,?) ");
					stmt.setInt(1, typeObj.getApplDomainId());
					stmt.setString(2, typeObj.getAttributeName());
					stmt.setString(3, typeObj.getAttributeDataType());
					stmt.setString(4, typeObj.getDefaultValue());
					stmt.setString(5, typeObj.getIsRequired());
					stmt.setString(6, typeObj.getIsList());
					stmt.execute();
					if (typeObj.getIsList().equalsIgnoreCase("1")) {
						final PreparedStatement stmt2 = conn.prepareStatement("SELECT ID FROM EG_ATTRIBUTETYPE WHERE APPL_DOMAINID=? AND ATT_NAME=? ORDER BY ID DESC");
						stmt2.setInt(1, typeObj.getApplDomainId());
						stmt2.setString(2, typeObj.getAttributeName());
						final ResultSet rs = stmt2.executeQuery();
						if (rs.next()) {
							typeObj.setId(rs.getInt("ID"));
						}

						final String key[] = typeObj.getKey();
						final String value[] = typeObj.getValue();

						for (int i = 0; i < key.length; i++) {
							final PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO EG_ATTRIBUTELIST (ID,ATT_TYPEID,ATT_KEY,ATT_VALUE) VALUES (SEQ_EG_ATTRIBUTELIST.nextval,?,?,?)");
							stmt3.setInt(1, typeObj.getId());
							stmt3.setString(2, key[i]);
							stmt3.setString(3, value[i]);
							stmt3.execute();
						}
					}
				}
			});
		} catch (final Exception ex) {
			LOG.error("Error occurred while creating Attribute Type", ex);
			throw new EGOVRuntimeException("Error occurred while creating Attribute Type", ex);
		}
	}

	@Override
	public void updateAttributeType(final AttributeType typeObj) throws EGOVRuntimeException {
		try {
			getSession().doWork(new Work() {
				@Override
				public void execute(final Connection conn) throws SQLException {
					final PreparedStatement stmt = conn.prepareStatement("UPDATE EG_ATTRIBUTETYPE SET APPL_DOMAINID=?,ATT_NAME=?,ATT_DATATYPE=?,DEFAULT_VALUE=?,ISREQUIRED=?,ISLIST=? WHERE ID=?");
					stmt.setInt(1, typeObj.getApplDomainId());
					stmt.setString(2, typeObj.getAttributeName());
					stmt.setString(3, typeObj.getAttributeDataType());
					stmt.setString(4, typeObj.getDefaultValue());
					stmt.setString(5, typeObj.getIsRequired());
					stmt.setString(6, typeObj.getIsList());
					stmt.setInt(7, typeObj.getId());
					stmt.execute();
					if (typeObj.getIsList().equalsIgnoreCase("1")) {
						final PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM EG_ATTRIBUTELIST WHERE ATT_TYPEID=?");
						stmt2.setInt(1, typeObj.getId());
						stmt2.execute();

						final String key[] = typeObj.getKey();
						final String value[] = typeObj.getValue();

						for (int i = 0; i < key.length; i++) {
							final PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO EG_ATTRIBUTELIST (ID,ATT_TYPEID,ATT_KEY,ATT_VALUE) VALUES (SEQ_EG_ATTRIBUTELIST.nextval,?,?,?)");
							stmt3.setInt(1, typeObj.getId());
							stmt3.setString(2, key[i]);
							stmt3.setString(3, value[i]);
							stmt3.execute();
						}
					} else if (typeObj.getIsList().equalsIgnoreCase("0")) {
						final PreparedStatement stmt4 = conn.prepareStatement("DELETE FROM EG_ATTRIBUTELIST WHERE ATT_TYPEID=?");
						stmt4.setInt(1, typeObj.getId());
						stmt4.execute();
					}
				}
			});
		} catch (final Exception ex) {
			LOG.error("Error occurred while updating Attribute Type", ex);
			throw new EGOVRuntimeException("Error occurred while updating Attribute Type", ex);
		}
	}

	@Override
	public void deleteAttributeType(final int id) throws EGOVRuntimeException {
		try {
			getSession().doWork(new Work() {
				@Override
				public void execute(final Connection conn) throws SQLException {
					PreparedStatement stmt = conn.prepareStatement("DELETE FROM EG_ATTRIBUTELIST WHERE ATT_TYPEID=?");
					stmt.setInt(1, id);
					stmt.execute();
					stmt = null;
					stmt = conn.prepareStatement("DELETE FROM EG_ATTRIBUTETYPE WHERE ID=?");
					stmt.setInt(1, id);
					stmt.execute();
				}
			});
		} catch (final Exception ex) {
			LOG.error("Error occurred while deleting Attribute Type", ex);
			throw new EGOVRuntimeException("Error occurred while deleting Attribute Type", ex);
		}
	}

	@Override
	public AttributeType getAttributeType(final int id) throws EGOVRuntimeException {

		try {
			return getSession().doReturningWork(new ReturningWork<AttributeType>() {
				@Override
				public AttributeType execute(final Connection conn) {
					try {
						PreparedStatement stmt = conn.prepareStatement("SELECT * FROM EG_ATTRIBUTETYPE WHERE ID=?");
						stmt.setInt(1, id);
						ResultSet rs = stmt.executeQuery();
						if (rs.next()) {
							final AttributeType obj = new AttributeType();
							obj.setApplDomainId(rs.getInt("APPL_DOMAINID"));
							obj.setAttributeName(rs.getString("ATT_NAME"));
							obj.setAttributeDataType(rs.getString("ATT_DATATYPE"));
							obj.setDefaultValue(rs.getString("DEFAULT_VALUE"));
							obj.setIsRequired(rs.getString("ISREQUIRED"));
							obj.setIsList(rs.getString("ISLIST"));

							final ArrayList<String> keylist = new ArrayList<String>();
							final ArrayList<String> valuelist = new ArrayList<String>();

							if (rs.getString("ISLIST").equalsIgnoreCase("1")) {
								stmt = null;
								stmt = conn.prepareStatement("SELECT * FROM EG_ATTRIBUTELIST WHERE ATT_TYPEID=? ORDER BY ID");
								stmt.setInt(1, id);
								rs = stmt.executeQuery();
								while (rs.next()) {
									keylist.add(rs.getString("ATT_KEY"));
									valuelist.add(rs.getString("ATT_VALUE"));
								}
							}
							final String[] key = new String[keylist.size()];
							final String[] value = new String[valuelist.size()];

							for (int i = 0; i < keylist.size(); i++) {
								key[i] = keylist.get(i);
								value[i] = valuelist.get(i);
							}
							obj.setKey(key);
							obj.setValue(value);
							return obj;
						} else {
							return null;
						}
					} catch (final SQLException e) {
						LOG.error("SQL Error occurred while getting Attribute Type", e);
						throw new EGOVRuntimeException("SQL Error occurred while getting Attribute Type", e);
					}
				}
			});
		} catch (final Exception e) {
			LOG.error("Error occurred while getting Attribute Type", e);
			throw new EGOVRuntimeException("Error occurred while getting Attribute Type", e);
		}
	}

}
