/*
 * Created on Jun 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.database.utils.EgovDatabaseManager;

import com.exilant.exility.common.TaskFailedException;

/**
 * @author sahinab
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class User {
	private static final Logger LOGGER = Logger.getLogger(User.class);
	private String userName;
	private String role;

	/**
	 * @return Returns the role.
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role
	 *            The role to set.
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @return Returns the userId.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @param userId
	 */
	public User(String userName) {
		super();
		this.userName = userName;
	}

	// this method gets the assigned role for the user from the database.
	public String getRole(Connection con) throws TaskFailedException {
		// if(LOGGER.isDebugEnabled())     LOGGER.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>domain user");
		String query = "select r.Role_name as role from EG_ROLES r, EG_USER u,EG_USERROLE ur where u.user_name=? and ur.id_role=r.id_role and u.id_user=ur.id_user ";
		String role = "";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, this.userName);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				role = rs.getString("role");
		} catch (SQLException ex) {
			LOGGER.error("Task Failed Error" + ex.getMessage(),ex);
			throw new TaskFailedException();
		}
		return role;
	}

	public int getId(Connection con) throws TaskFailedException {
		String query = "select id_user from EG_USER where user_name=? ";
		int userId = 0;
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, this.userName);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				userId = rs.getInt("id_user");
		} catch (SQLException ex) {
			LOGGER.error("EXP in getId" + ex.getMessage());
			throw new TaskFailedException();
		}
		return userId;
	}

	public int getId() throws TaskFailedException {
		int userId = 0;
		Connection conn = null;
		try {
			conn = EgovDatabaseManager.openConnection();
			userId = getId(conn);
		} 
		catch (Exception ex) {
			LOGGER.error("EXP in setting up connection" + ex.getMessage());
			throw new TaskFailedException();
		}finally {
			EgovDatabaseManager.releaseConnection(conn, null);
		}
		return userId;
	}

}
