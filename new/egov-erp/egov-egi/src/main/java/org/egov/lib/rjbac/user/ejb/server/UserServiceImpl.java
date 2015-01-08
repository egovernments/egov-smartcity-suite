package org.egov.lib.rjbac.user.ejb.server;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.jurisdiction.Jurisdiction;
import org.egov.lib.rjbac.jurisdiction.JurisdictionValues;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.UserDetail;
import org.egov.lib.rjbac.user.UserRole;
import org.egov.lib.rjbac.user.dao.TerminalDAO;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.hibernate.Query;

public class UserServiceImpl implements UserService {

	@Override
	public void createUser(final User usr) {
		new UserDAO().createOrUpdateUserWithPwdEncryption(usr);
	}

	@Override
	public void updateUser(final User usr) {
		new UserDAO().createOrUpdateUserWithPwdEncryption(usr);
	}

	@Override
	public Set<Role> getValidRoles(final Integer userID, final Date roleDate) {
		return new UserDAO().getValidRoles(userID, roleDate);
	}

	@Override
	public Set<UserRole> getAllRolesForUser(final String userName) {
		return new UserDAO().getAllRolesForUser(userName);
	}

	@Override
	public User getUserByID(final Integer id) {
		return new UserDAO().getUserByID(id);
	}

	/**
	 * Gets the user by name.
	 * @param userName the user name
	 * @return the user by name
	 * @see org.egov.lib.rjbac.user.ejb.api.UserService#getUserByName(java.lang.String)
	 */
	@Override
	public List<User> getUserByName(final String userName) {
		return new UserDAO().getUserByName(userName);
	}

	/**
	 * Gets the user by user name.
	 * @param userName the user name
	 * @return the user by user name
	 * @see org.egov.lib.rjbac.user.ejb.api.UserService#getUserByUserName(java.lang.String)
	 */

	@Override
	public User getUserByUserName(final String userName) {
		return new UserDAO().getUserByUserName(userName);

	}

	/**
	 * Returns the list of User's for the given like User Name
	 * @param userName the user name
	 * @return List of User matching like the user name
	 */
	@Override
	public List<User> getAllUserByUserNameLike(final String userName) {
		return new UserDAO().getAllUserByUserNameLike(userName);
	}

	/**
	 * Gets the user detail by id.
	 * @param userID the user id
	 * @return the user detail by id
	 * @see org.egov.lib.rjbac.user.ejb.api.UserService#getUserDetailByID(java.lang.Integer)
	 */

	@Override
	public UserDetail getUserDetailByID(final Integer userID) {
		return new UserDAO().getUserDetailByID(userID);
	}

	/**
	 * Gets the user det by user name.
	 * @param userName the user name
	 * @return the user det by user name
	 * @see org.egov.lib.rjbac.user.ejb.api.UserService#getUserDetByUserName(java.lang.String)
	 */
	@Override
	public UserDetail getUserDetByUserName(final String userName) {
		return new UserDAO().getUserDetByUserName(userName);
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.rjbac.user.ejb.api.IUserManager#getAllJurisdictionsForUser(java.lang.Integer)
	 */
	@Override
	public Set<JurisdictionValues> getAllJurisdictionsForUser(final Integer userid) {
		return new UserDAO().getAllJurisdictionsForUser(userid);
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.rjbac.user.ejb.api.IUserManager#getJurisdictionsForUser(java.lang.Integer, java.util.Date)
	 */
	@Override
	public Set<JurisdictionValues> getJurisdictionsForUser(final Integer userid, final Date jurDate) {
		return new UserDAO().getJurisdictionsForUser(userid, jurDate);
	}

	/**
	 * Removes the user.
	 * @param usrID the usr id
	 * @see org.egov.lib.rjbac.user.ejb.api.UserService#removeUser(java.lang.Integer)
	 */
	@Override
	public void removeUser(final Integer usrID) {
		new UserDAO().removeUser(usrID);
	}

	/**
	 * Removes the user.
	 * @param usr the usr
	 * @see org.egov.lib.rjbac.user.ejb.api.UserService#removeUser(org.egov.lib.rjbac.user.User)
	 */
	@Override
	public void removeUser(final User usr) {
		new UserDAO().removeUser(usr);
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
		return new UserDAO().getJurisdictionValueByBndryIdAndUserId(bndryId, userId);
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.rjbac.user.ejb.api.IUserManager#getJurisdictionByBndryTypeIdAndUserId(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Jurisdiction getJurisdictionByBndryTypeIdAndUserId(final Integer bndryTypeId, final Integer userId) {
		return new UserDAO().getJurisdictionByBndryTypeIdAndUserId(bndryTypeId, userId);
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
		return new UserDAO().getAllUsersForJurisdictionType(bt, topLvlBndryID);

	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.rjbac.user.ejb.api.IUserManager#getAllUsersForJurisdictionTypeFullResolve(org.egov.lib.admbndry.BoundaryType, java.lang.Integer)
	 */
	@Override
	public Map getAllUsersForJurisdictionTypeFullResolve(final BoundaryType bt, final Integer topLevelBoundaryID) {
		return new UserDAO().getAllUsersForJurisdictionTypeFullResolve(bt, topLevelBoundaryID);

	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.rjbac.user.ejb.api.IUserManager#getAllUsersForJurisdictionFullResolve(java.util.List, java.lang.Integer)
	 */
	@Override
	public Map getAllUsersForJurisdictionFullResolve(final List boundaryTypeList, final Integer topLevelBoundaryID) {
		return new UserDAO().getAllUsersForJurisdictionFullResolve(boundaryTypeList, topLevelBoundaryID);

	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.rjbac.user.ejb.api.IUserManager#getUsersByDepartment(org.egov.lib.rjbac.dept.Department)
	 */
	@Override
	public List<User> getUsersByDepartment(final Department department) {
		return new UserDAO().getUsersByDepartment(department);
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.rjbac.user.ejb.api.IUserManager#findIpAddress(java.lang.String)
	 */
	@Override
	public List findIpAddress(final String ipAddr) {
		return new TerminalDAO().getTerminal(ipAddr);
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.lib.rjbac.user.ejb.api.IUserManager#getAllUsers()
	 */
	@Override
	public List<User> getAllUsers() {
		return new UserDAO().getAllUsers();

	}

	/**
	 * Gets the superior user id.
	 * @param userId the user id
	 * @return the superior user id
	 * @throws Exception the exception
	 */
	public String getSuperiorUserId(final String userId) throws Exception {
		try {
			final Query query = HibernateUtil.getCurrentSession().createQuery("SELECT REPORTSTO FROM EG_USER WHERE ID_USER := userId");
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
			final Query query = HibernateUtil.getCurrentSession().createQuery("SELECT ID_USER FROM EG_USER WHERE REPORTSTO := userId");
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
		return new UserDAO().getAllUsersByRole(roleList, topBoundaryID, roleDate);
	}
}
