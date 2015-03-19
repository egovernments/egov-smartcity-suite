package org.egov.lib.rjbac.user.ejb.server;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.jurisdiction.Jurisdiction;
import org.egov.lib.rjbac.jurisdiction.JurisdictionValues;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.user.UserDetail;
import org.egov.lib.rjbac.user.UserRole;
import org.egov.lib.rjbac.user.dao.TerminalDAO;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserServiceImpl implements UserService {

	private SessionFactory sessionFactory;
	private UserDAO userDAO;

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public UserServiceImpl(SessionFactory sessionFactory, UserDAO userDAO) {
		this.sessionFactory = sessionFactory;
		this.userDAO = userDAO;
	}

	@Override
	public void createUser(final User usr) {
		userDAO.createOrUpdateUserWithPwdEncryption(usr);
	}

	@Override
	public void updateUser(final User usr) {
		userDAO.createOrUpdateUserWithPwdEncryption(usr);
	}

	@Override
	public Set<Role> getValidRoles(final Integer userID, final Date roleDate) {
		return userDAO.getValidRoles(userID, roleDate);
	}

	@Override
	public Set<UserRole> getAllRolesForUser(final String userName) {
		return userDAO.getAllRolesForUser(userName);
	}

	@Override
	public User getUserByID(final Integer id) {
		return userDAO.getUserByID(id);
	}

	/**
	 * Gets the user by name.
	 * @param userName the user name
	 * @return the user by name
	 * @see org.egov.lib.rjbac.user.ejb.api.UserService#getUserByName(java.lang.String)
	 */
	@Override
	public List<User> getUserByName(final String userName) {
		return userDAO.getUserByName(userName);
	}

	/**
	 * Gets the user by user name.
	 * @param userName the user name
	 * @return the user by user name
	 * @see org.egov.lib.rjbac.user.ejb.api.UserService#getUserByUserName(java.lang.String)
	 */

	@Override
	public User getUserByUserName(final String userName) {
		return userDAO.getUserByUserName(userName);

	}

	/**
	 * Returns the list of User's for the given like User Name
	 * @param userName the user name
	 * @return List of User matching like the user name
	 */
	@Override
	public List<User> getAllUserByUserNameLike(final String userName) {
		return userDAO.getAllUserByUserNameLike(userName);
	}

	/**
	 * Gets the user detail by id.
	 * @param userID the user id
	 * @return the user detail by id
	 * @see org.egov.lib.rjbac.user.ejb.api.UserService#getUserDetailByID(java.lang.Integer)
	 */

	@Override
	public UserDetail getUserDetailByID(final Integer userID) {
		return userDAO.getUserDetailByID(userID);
	}

	/**
	 * Gets the user det by user name.
	 * @param userName the user name
	 * @return the user det by user name
	 * @see org.egov.lib.rjbac.user.ejb.api.UserService#getUserDetByUserName(java.lang.String)
	 */
	@Override
	public UserDetail getUserDetByUserName(final String userName) {
		return userDAO.getUserDetByUserName(userName);
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.rjbac.user.ejb.api.IUserManager#getAllJurisdictionsForUser(java.lang.Integer)
	 */
	@Override
	public Set<JurisdictionValues> getAllJurisdictionsForUser(final Integer userid) {
		return userDAO.getAllJurisdictionsForUser(userid);
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.rjbac.user.ejb.api.IUserManager#getJurisdictionsForUser(java.lang.Integer, java.util.Date)
	 */
	@Override
	public Set<JurisdictionValues> getJurisdictionsForUser(final Integer userid, final Date jurDate) {
		return userDAO.getJurisdictionsForUser(userid, jurDate);
	}

	/**
	 * Removes the user.
	 * @param usrID the usr id
	 * @see org.egov.lib.rjbac.user.ejb.api.UserService#removeUser(java.lang.Integer)
	 */
	@Override
	public void removeUser(final Integer usrID) {
		userDAO.removeUser(usrID);
	}

	/**
	 * Removes the user.
	 * @param usr the usr
	 * @see org.egov.lib.rjbac.user.ejb.api.UserService#removeUser(org.egov.infra.admin.master.entity.User)
	 */
	@Override
	public void removeUser(final User usr) {
		userDAO.removeUser(usr);
	}

	/**
	 * Gets the jurisdiction value by bndry id and user id.
	 * @param bndryId the bndry id
	 * @param userId the user id
	 * @return the jurisdiction value by bndry id and user id
	 * @see org.egov.lib.rjbac.user.ejb.api.UserService#getUserJurisdictionLevels(java.lang.Integer)
	 */
	@Override
	public JurisdictionValues getJurisdictionValueByBndryIdAndUserId(final Integer bndryId, final Integer userId) {
		return userDAO.getJurisdictionValueByBndryIdAndUserId(bndryId, userId);
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.rjbac.user.ejb.api.IUserManager#getJurisdictionByBndryTypeIdAndUserId(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Jurisdiction getJurisdictionByBndryTypeIdAndUserId(final Integer bndryTypeId, final Integer userId) {
		return userDAO.getJurisdictionByBndryTypeIdAndUserId(bndryTypeId, userId);
	}

	/**
	 * Gets the user jurisdictions.
	 * @param userid the userid
	 * @return the user jurisdictions
	 * @see org.egov.lib.rjbac.user.ejb.api.UserService#getUserJurisdictionLevels(java.lang.Integer)
	 */
	@Override
	public Set<JurisdictionValues> getUserJurisdictions(final Integer userid) {
		return getJurisdictionsForUser(userid, new java.util.Date());
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.rjbac.user.ejb.api.IUserManager#getAllUsersForJurisdictionType(org.egov.lib.admbndry.BoundaryType, java.lang.Integer)
	 */
	@Override
	public Map getAllUsersForJurisdictionType(final BoundaryType bt, final Integer topLvlBndryID) {
		return userDAO.getAllUsersForJurisdictionType(bt, topLvlBndryID);

	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.rjbac.user.ejb.api.IUserManager#getAllUsersForJurisdictionTypeFullResolve(org.egov.lib.admbndry.BoundaryType, java.lang.Integer)
	 */
	@Override
	public Map getAllUsersForJurisdictionTypeFullResolve(final BoundaryType bt, final Integer topLevelBoundaryID) {
		return userDAO.getAllUsersForJurisdictionTypeFullResolve(bt, topLevelBoundaryID);

	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.rjbac.user.ejb.api.IUserManager#getAllUsersForJurisdictionFullResolve(java.util.List, java.lang.Integer)
	 */
	@Override
	public Map getAllUsersForJurisdictionFullResolve(final List boundaryTypeList, final Integer topLevelBoundaryID) {
		return userDAO.getAllUsersForJurisdictionFullResolve(boundaryTypeList, topLevelBoundaryID);

	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.rjbac.user.ejb.api.IUserManager#getUsersByDepartment(org.egov.lib.rjbac.dept.Department)
	 */
	@Override
	public List<User> getUsersByDepartment(final Department department) {
		return userDAO.getUsersByDepartment(department);
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.rjbac.user.ejb.api.IUserManager#findIpAddress(java.lang.String)
	 */
	@Override
	public List findIpAddress(final String ipAddr) {
		return new TerminalDAO(sessionFactory).getTerminal(ipAddr);
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.rjbac.user.ejb.api.IUserManager#getAllUsers()
	 */
	@Override
	public List<User> getAllUsers() {
		return userDAO.getAllUsers();

	}

	/**
	 * Gets the superior user id.
	 * @param userId the user id
	 * @return the superior user id
	 * @throws Exception the exception
	 */
	public String getSuperiorUserId(final String userId) throws Exception {
		try {
			final Query query = getSession().createQuery("SELECT REPORTSTO FROM EG_USER WHERE ID_USER := userId");
			query.setString("userId", userId);
			return (String) query.uniqueResult();
		} catch (final Exception ex) {
			throw new EGOVRuntimeException("Exception in getSuperiorUserId()", ex);
		}
	}

	/**
	 * Gets the inferior user id.
	 * @param userId the user id
	 * @return the inferior user id
	 * @throws Exception the exception
	 */
	public String getInferiorUserId(final String userId) throws Exception {
		try {
			final Query query = getSession().createQuery("SELECT ID_USER FROM EG_USER WHERE REPORTSTO := userId");
			query.setString("userId", userId);
			return (String) query.uniqueResult();
		} catch (final Exception ex) {
			throw new EGOVRuntimeException("Exception in getInferiorUserId()", ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.rjbac.user.ejb.api.IUserManager#getAllUsersByRole(java.util.List, int, java.util.Date)
	 */
	@Override
	public List<User> getAllUsersByRole(final List roleList, final int topBoundaryID, final Date roleDate) {
		return userDAO.getAllUsersByRole(roleList, topBoundaryID, roleDate);
	}
}
