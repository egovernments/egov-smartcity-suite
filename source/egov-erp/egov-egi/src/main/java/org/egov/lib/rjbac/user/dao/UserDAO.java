/*
 * @(#)UserDAO.java 3.0, 16 Jun, 2013 12:51:47 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.user.dao;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.security.utils.CryptoHelper;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.jurisdiction.Jurisdiction;
import org.egov.lib.rjbac.jurisdiction.JurisdictionValues;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.user.UserRole;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class UserDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);
	private SessionFactory sessionFactory;

	@Deprecated
	public UserDAO() {
	}

	public UserDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Gets the session.
	 * @return the session
	 */
	private Session getSession() {
		return HibernateUtil.getCurrentSession();
	}

	/**
	 * user pwd is encrypted and stored so that in business logic every one need not encrypt the pwd.
	 * @param usr the usr
	 */
	public void createOrUpdateUserWithPwdEncryption(final User usr) {
		final String encpassword = CryptoHelper.encrypt(usr.getPassword());
		usr.setPassword(encpassword);
		if (usr.getFromDate() == null) {
			usr.setFromDate(new Date());
		}
		this.getSession().saveOrUpdate(usr);
	}

	/**
	 * Gets the all roles for user.
	 * @param userName the user name
	 * @return the all roles for user
	 */
	public Set<UserRole> getAllRolesForUser(final String userName) {
		try {
			final Query qry = this.getSession().createQuery("FROM UserRole rol where rol.user.userName=:userName and rol.isHistory='N')");
			qry.setString("userName", userName);
			return new HashSet<UserRole>(qry.list());
		} catch (final HibernateException e) {
			LOGGER.error("Exception encountered in getAllRolesForUser", e);
			throw new EGOVRuntimeException("Exception encountered in getAllRolesForUser", e);
		}
	}

	/**
	 * Gets the users by department.
	 * @param department the department
	 * @return the users by department
	 */
	public List<User> getUsersByDepartment(final Department department) {
		try {
			final Query qry = this.getSession().createQuery("FROM User UI where UI.department = :department");
			qry.setEntity("department", department);
			return qry.list();
		} catch (final HibernateException e) {
			LOGGER.error("Exception encountered in getUsersByDepartment", e);
			throw new EGOVRuntimeException("Exception encountered in getUsersByDepartment", e);
		}
	}

	/**
	 * Gets the jurisdiction value by bndry id and user id.
	 * @param bndryId the bndry id
	 * @param userId the user id
	 * @return the jurisdiction value by bndry id and user id
	 */
	public JurisdictionValues getJurisdictionValueByBndryIdAndUserId(final Integer bndryId, final Long userId) {
		try {
			final Query qry = this.getSession().createQuery("FROM JurisdictionValues jur where jur.userJurLevel.user.id=:userid and jur.boundary.id=:bndryId and jur.isHistory='N'");
			qry.setInteger("bndryId", bndryId);
			qry.setLong("userid", userId);
			return (JurisdictionValues) qry.uniqueResult();
		} catch (final HibernateException e) {
			LOGGER.error("Exception encountered in getJurisdictionValueByBndryIdAndUserId", e);
			throw new EGOVRuntimeException("Exception encountered in getJurisdictionValueByBndryIdAndUserId", e);
		}
	}

	/**
	 * Gets the jurisdiction by bndry type id and user id.
	 * @param bndryTypeId the bndry type id
	 * @param userId the user id
	 * @return the jurisdiction by bndry type id and user id
	 */
	public Jurisdiction getJurisdictionByBndryTypeIdAndUserId(final Integer bndryTypeId, final Long userId) {
		try {
			final Query qry = this.getSession().createQuery("FROM Jurisdiction jur where jur.user.id=:userid and jur.jurisdictionLevel.id=:bndryTypeId ");
			qry.setInteger("bndryTypeId", bndryTypeId);
			qry.setLong("userid", userId);
			return (Jurisdiction) qry.uniqueResult();
		} catch (final HibernateException e) {
			LOGGER.error("Exception encountered in getJurisdictionByBndryTypeIdAndUserId", e);
			throw new EGOVRuntimeException("Exception encountered in getJurisdictionByBndryTypeIdAndUserId", e);
		}
	}

	/**
	 * Gets the all jurisdictions for user.
	 * @param userid the userid
	 * @return the all jurisdictions for user
	 */
	public Set<JurisdictionValues> getAllJurisdictionsForUser(final Long userid) {
		try {
			final Query qry = this.getSession().createQuery("FROM JurisdictionValues jur where jur.userJurLevel.user.id=:userid and jur.isHistory='N')");
			qry.setLong("userid", userid);
			return new HashSet<JurisdictionValues>(qry.list());
		} catch (final HibernateException e) {
			LOGGER.error("Exception encountered in getAllJurisdictionsForUser", e);
			throw new EGOVRuntimeException("Exception encountered in getAllJurisdictionsForUser", e);
		}
	}

	/**
	 * Gets the jurisdictions for user.
	 * @param userid the userid
	 * @param jurDate the jur date
	 * @return the jurisdictions for user
	 */
	public Set<JurisdictionValues> getJurisdictionsForUser(final Long userid, final Date jurDate) {
		try {
			final Query qry = this.getSession().createQuery(
					"FROM JurisdictionValues jurs  where jurs.userJurLevel.user.id = :userid  and jurs.isHistory='N' AND ((jurs.toDate IS NULL AND jurs.fromDate <= :currDate) OR (jurs.fromDate <= :currDate AND jurs.toDate >= :currDate)) ");
			qry.setLong("userid", userid);
			qry.setDate("currDate", jurDate);
			return new HashSet<JurisdictionValues>(qry.list());
		} catch (final HibernateException e) {
			LOGGER.error("Exception encountered in getJurisdictionsForUser", e);
			throw new EGOVRuntimeException("Exception encountered in getJurisdictionsForUser", e);
		}
	}

	
	/**
	 * Gets the user by id.
	 * @param userID the user id
	 * @return the user by id
	 */
	public User getUserByID(final Long userID) {
		try {
			return (User) this.getSession().get(User.class, userID);
		} catch (final HibernateException e) {
			LOGGER.error("Exception encountered in getUserByID", e);
			throw new EGOVRuntimeException("Exception encountered in getUserByID", e);
		}
	}

	/**
	 * Returns the list of User's for the given like User Name
	 * @param userName the user name
	 * @return List of User matching like the user name
	 */
	public List<User> getAllUserByUserNameLike(final String userName) {
		final Query query = this.getSession().createQuery("FROM User where lower(userName) like ? order by userName asc");
		query.setString(0, userName.toLowerCase() + "%");
		return query.list();
	}

	/**
	 * Gets the user by user name.
	 * @param userName the user name
	 * @return the user by user name
	 */
	public User getUserByUserName(final String userName) {
		try {
			final Query qry = this.getSession().createQuery("FROM User UI WHERE UI.userName = :usrName ");
			qry.setString("usrName", userName);
			return (User) qry.uniqueResult();
		} catch (final HibernateException e) {
			LOGGER.error("Exception encountered in getUserByUserName", e);
			throw new EGOVRuntimeException("Exception encountered in getUserByUserName", e);
		}
	}

	/**
	 * Gets the user by name.
	 * @param userName the user name
	 * @return the user by name
	 */
	public List<User> getUserByName(final String userName) {
		try {
			final Query qry = this.getSession().createQuery("FROM User UI WHERE UI.firstName like :userName OR UI.middleName like :userName OR UI.lastName like :userName");
			qry.setString("userName", "%" + userName + "%");
			return qry.list();
		} catch (final HibernateException e) {
			LOGGER.error("Exception encountered in getUserByName", e);
			throw new EGOVRuntimeException("Exception encountered in getUserByName", e);
		}
	}

	/**
	 * Removes the user.
	 * @param usr the usr
	 */
	public void removeUser(final User usr) {
		try {
			this.getSession().delete(usr);
		} catch (final HibernateException e) {
			LOGGER.error("Exception encountered in removeUser", e);
			throw new EGOVRuntimeException("Exception encountered in removeUser", e);
		}
	}

	/**
	 * Removes the user.
	 * @param usrID the usr id
	 */
	public void removeUser(final Long usrID) {
		final User usr = this.getUserByID(usrID);
		this.removeUser(usr);
	}

	/**
	 * Gets the users by city.
	 * @param cityID the city id
	 * @return the users by city
	 */
	public Collection getUsersByCity(final int cityID) {
		return null;
	}

	
	/**
	 * Gets the all users for jurisdiction type.
	 * @param bt the bt
	 * @param topLevelBoundaryID the top level boundary id
	 * @return the all users for jurisdiction type
	 */
	public Map getAllUsersForJurisdictionType(final BoundaryType bt, final Integer topLevelBoundaryID) {
		/*try {
			final Date currDate = new Date();
			final Query qry = this
					.getSession()
					.createQuery(
							"Select UI.userName FROM User UI join UI.allJurisdictions as jurs where jurs.jurisdictionLevel = :bndryType  AND ((jurs.jurisdictionValues.toDate IS NULL AND jurs.jurisdictionValues.fromDate <= :currDate) OR (jurs.jurisdictionValues.fromDate <= :currDate AND jurs.jurisdictionValues.toDate >= :currDate))");
			qry.setEntity("bndryType", bt);
			qry.setDate("currDate", currDate);
			final Map retMap = new HashMap();
			List userList = new ArrayList();
			Boundary bn = null;
			for (final Iterator iter = qry.iterate(); iter.hasNext();) {
				final Object obj = iter.next();
				final User user = this.getUserByUserName(obj.toString());
				// User user = (User)obj;
				Set bnSet = new HashSet();
				if (user != null) {
					bnSet = user.getAllJurisdictionsForLevel(bt);
				}
				for (final Iterator iterator = bnSet.iterator(); iterator.hasNext();) {
					final Boundary element = (Boundary) iterator.next();
					bn = element;
					if ((bn.getParent() == null) && bn.getId().equals(Integer.valueOf(topLevelBoundaryID))) {
						if (retMap.containsKey(element)) {
							userList = (List) retMap.get(element);
						} else {
							userList = new ArrayList();
						}
						userList.add(user);
						retMap.put(element, userList);
					}
					while (bn.getParent() != null) {
						final Boundary parent = bn.getParent();
						if (parent.getId().equals(Integer.valueOf(topLevelBoundaryID))) {
							if (retMap.containsKey(element)) {
								userList = (List) retMap.get(element);
							} else {
								userList = new ArrayList();
							}
							userList.add(user);
							retMap.put(element, userList);
							break;
						}
						bn = parent;
					}
				}
			}
			return retMap;
		} catch (final HibernateException e) {
			LOGGER.error("Exception encountered in getAllUsersForJurisdictionType", e);
			throw new EGOVRuntimeException("Exception encountered in getAllUsersForJurisdictionType", e);
		}*/
	        return Collections.emptyMap();
	}

	/**
	 * Gets the all users for jurisdiction type full resolve.
	 * @param bt the bt
	 * @param topLevelBoundaryID the top level boundary id
	 * @return the all users for jurisdiction type full resolve
	 */
	public Map getAllUsersForJurisdictionTypeFullResolve(final BoundaryType bt, final Integer topLevelBoundaryID) {
		/*try {
			final Date currDate = new Date();
			final Query qry = this
					.getSession()
					.createQuery(
							"Select UI.userName FROM User UI join UI.allJurisdictions as jurs where jurs.jurisdictionLevel = :bndryType AND ((jurs.jurisdictionValues.toDate IS NULL AND jurs.jurisdictionValues.fromDate <= :currDate) OR (jurs.jurisdictionValues.fromDate <= :currDate AND jurs.jurisdictionValues.toDate >= :currDate))");
			qry.setEntity("bndryType", bt);
			qry.setDate("currDate", currDate);
			final Map retMap = new HashMap();
			List userList = new ArrayList();
			Boundary bn = null;
			for (final Iterator iter = qry.iterate(); iter.hasNext();) {
				final Object obj = iter.next();
				final User user = this.getUserByUserName(obj.toString());
				// User user = (User)obj;
				Set bnSet = new HashSet();
				if (user != null) {
					bnSet = user.getAllJurisdictionsForLevelFullReslove(bt);
				}
				for (final Iterator iterator = bnSet.iterator(); iterator.hasNext();) {
					final Boundary element = (Boundary) iterator.next();
					bn = element;
					if ((bn.getParent() == null) && bn.getId().equals(Integer.valueOf(topLevelBoundaryID))) {
						if (retMap.containsKey(element)) {
							userList = (List) retMap.get(element);
						} else {
							userList = new ArrayList();
						}
						userList.add(user);
						retMap.put(element, userList);
					}
					while (bn.getParent() != null) {
						final Boundary parent = bn.getParent();
						if (parent.getId().equals(Integer.valueOf(topLevelBoundaryID))) {
							if (retMap.containsKey(element)) {
								userList = (List) retMap.get(element);
							} else {
								userList = new ArrayList();
							}
							userList.add(user);
							retMap.put(element, userList);
							break;
						}
						bn = parent;
					}
				}
			}
			return retMap;
		} catch (final HibernateException e) {
			LOGGER.error("Exception encountered in getAllUsersForJurisdictionTypeFullResolve", e);
			throw new EGOVRuntimeException("Exception encountered in getAllUsersForJurisdictionTypeFullResolve", e);
		}*/
	    return  Collections.emptyMap();
	}

	/**
	 * Gets the all users for jurisdiction full resolve.
	 * @param boundaryTypeList the boundary type list
	 * @param topLevelBoundaryID the top level boundary id
	 * @return the all users for jurisdiction full resolve
	 */
	public Map getAllUsersForJurisdictionFullResolve(final List boundaryTypeList, final Integer topLevelBoundaryID) {
		/*try {
			final StringBuilder queryPart = new StringBuilder();
			for (int i = 0; i < boundaryTypeList.size(); i++) {
				queryPart.append(" :boundaryType").append(i).append(",");
			}
			queryPart.deleteCharAt(queryPart.lastIndexOf(","));
			final StringBuilder queryStr = new StringBuilder();
			queryStr.append("Select UI.userName FROM User UI join UI.allJurisdictions as jurs where jurs.jurisdictionLevel IN ( ");
			queryStr.append(queryPart).append(" ) and " + "UI.topBoundaryID =:topBoundaryID AND ( ");
			queryStr.append("(jurs.jurisdictionValues.toDate IS NULL AND jurs.jurisdictionValues.fromDate <= :currDate) OR (jurs.jurisdictionValues.fromDate <= :currDate AND jurs.jurisdictionValues.toDate >= :currDate))");
			final Query qry = this.getSession().createQuery(queryStr.toString());
			qry.setDate("currDate", new Date());
			final Iterator itr = boundaryTypeList.iterator();
			int i = 0;
			while (itr.hasNext()) {
				final BoundaryType bndType = (BoundaryType) itr.next();
				final String txt = "boundaryType" + i;
				qry.setEntity(txt, bndType);
				i++;
			}
			qry.setInteger("topBoundaryID", topLevelBoundaryID);
			final Map retMap = new HashMap();
			List userList = new ArrayList();
			Boundary bn = null;
			for (final Iterator iter = qry.iterate(); iter.hasNext();) {
				final Object obj = iter.next();
				final User user = this.getUserByUserName(obj.toString());
				// User user = (User)obj;
				Set bnSet = new HashSet();
				if (user != null) {
					bnSet = user.getAllJurisdictionsFullReslove();
				}
				for (final Iterator iterator = bnSet.iterator(); iterator.hasNext();) {
					final Boundary element = (Boundary) iterator.next();
					bn = element;
					if ((bn.getParent() == null) && bn.getId().equals(Integer.valueOf(topLevelBoundaryID))) {
						if (retMap.containsKey(element)) {
							userList = (List) retMap.get(element);
						} else {
							userList = new ArrayList();
						}
						userList.add(user);
						retMap.put(element, userList);
					}
					while (bn.getParent() != null) {
						final Boundary parent = bn.getParent();
						if (parent.getId().equals(Integer.valueOf(topLevelBoundaryID))) {
							if (retMap.containsKey(element)) {
								userList = (List) retMap.get(element);
							} else {
								userList = new ArrayList();
							}
							userList.add(user);
							retMap.put(element, userList);
							break;
						}
						bn = parent;
					}
				}
			}
			return retMap;
		} catch (final HibernateException e) {
			LOGGER.error("Exception encountered in getAllUsersForJurisdictionFullResolve", e);
			throw new EGOVRuntimeException("Exception encountered in getAllUsersForJurisdictionFullResolve", e);
		}*/
	        return  Collections.emptyMap();
	}

	/**
	 * Gets the valid roles.
	 * @param userID the user id
	 * @param roleDate the role date
	 * @return the valid roles
	 */
	public Set<Role> getValidRoles(final Long userID, final Date roleDate) {
		try {
			final Query qry = this.getSession().createQuery("FROM UserRole rol Where rol.user.id=:userID and rol.isHistory='N' AND ((rol.toDate IS NULL AND rol.fromDate <= :currDate) OR (rol.fromDate <= :currDate AND rol.toDate >= :currDate)) ");
			qry.setLong("userID", userID);
			qry.setDate("currDate", roleDate);
			final Set<Role> validRoles = new HashSet<Role>();
			for (final Iterator iter = qry.list().iterator(); iter.hasNext();) {
				final UserRole userrole = (UserRole) iter.next();
				final Role role = userrole.getRole();
				validRoles.add(role);
			}
			return validRoles;
		} catch (final HibernateException e) {
			LOGGER.error("Exception encountered in getValidRoles", e);
			throw new EGOVRuntimeException("Exception encountered in getValidRoles", e);
		}
	}

	/**
	 * This API returns the user objects for specified roles in egov_config.xml as per current date
	 * @param includeRolesList - list of roles as comma separated from egovconfig
	 * @param roleDate - date to check for valid roles in that period.
	 * @return list of valid users obj
	 */
	public List<User> getAllUserForRoles(final String includeRolesList, final Date roleDate) {
		try {
			final String[] includeRolesArray = includeRolesList.split(",");
			final StringBuilder queryPart = new StringBuilder();
			for (int i = 0; i < includeRolesArray.length; i++) {
				queryPart.append(" :roleName").append(i).append(",");
			}
			queryPart.deleteCharAt(queryPart.lastIndexOf(","));
			final StringBuilder query = new StringBuilder();
			query.append("select distinct UI FROM User UI  left join UI.userRoles ur left join ur.role r " + "where r.roleName in (");
			query.append(queryPart).append(")  AND UI.isActive=1 AND ur.isHistory='N' AND ( ");
			query.append("(ur.toDate IS NULL AND ur.fromDate <= :currDate)  OR (ur.fromDate <= :currDate AND ur.toDate >= :currDate)) AND ( ");
			query.append("(UI.toDate IS NULL AND UI.fromDate <= :currDate) OR (UI.fromDate <= :currDate AND UI.toDate >= :currDate))");
			final Query qry = this.getSession().createQuery(query.toString());
			qry.setDate("currDate", roleDate);
			for (int i = 0; i < includeRolesArray.length; i++) {
				qry.setString("roleName" + i, includeRolesArray[i]);
			}
			return qry.list();
		} catch (final HibernateException e) {
			LOGGER.error("Exception encountered in getAllUserForRoles", e);
			throw new EGOVRuntimeException("Exception encountered in getAllUserForRoles", e);
		}
	}

	/**
	 * Gets the all users.
	 * @return the all users
	 */
	public List<User> getAllUsers() {
		try {
			final Query qry = this.getSession().createQuery("FROM User ");
			return qry.list();
		} catch (final HibernateException e) {
			LOGGER.error("Exception encountered in getAllUsers", e);
			throw new EGOVRuntimeException("Exception encountered in getAllUsers", e);
		}
	}

	/**
	 * Gets the all passwords.
	 * @return the all passwords
	 */
	public List<User> getAllPasswords() {
		try {
			final Query qry = this.getSession().createQuery("FROM User UI where UI.pwd is null ");
			return qry.list();
		} catch (final HibernateException e) {
			LOGGER.error("Exception encountered in getAllPasswords", e);
			throw new EGOVRuntimeException("Exception encountered in getAllPasswords", e);
		}
	}

	/**
	 * Gets the all user for roles.
	 * @return the all user for roles
	 */
	public List<User> getAllUserForRoles() {
		try {
			final String[] includeRolesArray = EGovConfig.getProperty("INCLUDE_ROLES", "", "IP-BASED-LOGIN").split(",");
			final StringBuilder queryPart = new StringBuilder();
			for (int i = 0; i < includeRolesArray.length; i++) {
				queryPart.append(" :roleName").append(i).append(",");
			}
			queryPart.deleteCharAt(queryPart.lastIndexOf(","));
			final Query qry = this.getSession().createQuery(new StringBuilder().append("FROM User UI where UI.roles.roleName  IN (").append(queryPart).append(")").toString());
			for (int i = 0; i < includeRolesArray.length; i++) {
				qry.setString("roleName" + i, includeRolesArray[i]);
			}
			return qry.list();
		} catch (final HibernateException e) {
			LOGGER.error("Exception encountered in getAllUserForRoles", e);
			throw new EGOVRuntimeException("Exception encountered in getAllUserForRoles", e);
		}
	}

	/**
	 * This API returns the user objects for specified roles in egov_config.xml as per current date
	 * @param roleList - list of roles as comma separated from egovconfig
	 * @param topBoundaryID - it will returns list according to bndry id.
	 * @param roleDate - date to check for valid roles in that period.
	 * @return list of valid users obj
	 */
	public List<User> getAllUsersByRole(final List roleList, final int topBoundaryID, final Date roleDate) {
		if ((roleList == null) || roleList.isEmpty() || (roleDate == null)) {
			throw new EGOVRuntimeException("role.list.null");
		}
		try {
			final StringBuilder queryStr = new StringBuilder();
			queryStr.append("select distinct UI FROM User UI left join UI.userRoles ur left join ur.role r where r in (:roleList) AND UI.isActive=1 AND ur.isHistory='N' ");
			queryStr.append(" AND (" + "(ur.toDate IS NULL AND ur.fromDate <= :currDate) OR (ur.fromDate <= :currDate AND ur.toDate >= :currDate)) AND (");
			queryStr.append("(UI.toDate IS NULL AND UI.fromDate <= :currDate) OR (UI.fromDate <= :currDate AND UI.toDate >= :currDate))");
			if (topBoundaryID != -1) {
				// add in the topBoundary check
				queryStr.append(" and UI.topBoundaryID = :topBoundaryID");
			}
			final Query query = this.getSession().createQuery(queryStr.toString());
			query.setDate("currDate", roleDate);
			query.setParameterList("roleList", roleList);
			if (topBoundaryID != -1) {
				query.setInteger("topBoundaryID", topBoundaryID);
			}
			return query.list();
		} catch (final Exception e) {
			LOGGER.error("Exception encountered in getAllUsersByRole", e);
			throw new EGOVRuntimeException("system.error", e);
		}
	}
}
