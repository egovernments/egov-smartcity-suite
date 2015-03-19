/*
 * @(#)UserService.java 3.0, 16 Jun, 2013 12:40:12 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.user.ejb.api;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.exceptions.DuplicateElementException;
import org.egov.infra.admin.master.entity.User;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.jurisdiction.Jurisdiction;
import org.egov.lib.rjbac.jurisdiction.JurisdictionValues;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.user.UserDetail;
import org.egov.lib.rjbac.user.UserRole;
import org.egov.lib.security.JurisdictionCheck;

public interface UserService {

	/**
	 * Creates a User in the System.
	 * @param usr the usr
	 * @throws DuplicateElementException the duplicate element exception
	 */
	void createUser(User usr) throws DuplicateElementException;;

	/**
	 * Updates the user object and also encrypts the user's password. Assumption is that the user object passed in does not encrypt the password
	 * @param usr the usr
	 */
	void updateUser(User usr);

	/**
	 * Gets the valid roles.
	 * @param userID the user id
	 * @param roleDate the role date
	 * @return the valid roles
	 */
	Set<Role> getValidRoles(Integer userID, Date roleDate);

	/**
	 * Gets the all roles for user.
	 * @param userName the user name
	 * @return the all roles for user
	 */
	Set<UserRole> getAllRolesForUser(String userName);

	/**
	 * Returns a user with the given userid.
	 * @param id the id
	 * @return User with the given id
	 */
	User getUserByID(Integer id);

	/**
	 * Returns a UserDetail with the given userid.
	 * @param userID the user id
	 * @return UserDetail with the given id
	 */
	UserDetail getUserDetailByID(Integer userID);

	/**
	 * Returns User with the given username. Each user is uniquely identified by a username in the system. The function returns the exact match of the given username
	 * @param userName the user name
	 * @return User matching the username
	 */
	@JurisdictionCheck
	User getUserByUserName(String userName);

	/**
	 * Returns the list of User's for the given like User Name
	 * @param userName the user name
	 * @return List of User matching like the user name
	 */
	List<User> getAllUserByUserNameLike(String userName);

	/**
	 * Returns the list of Users who match the given name. The name is matched in the first, middle OR the last name.
	 * @param name the name
	 * @return List of users matching the given criteria
	 */
	List<User> getUserByName(String name);

	/**
	 * This method returns the all jurisdictions of the user. Historical data as well
	 * @param userid the userid
	 * @return the all jurisdictions for user
	 * @return
	 */
	Set<JurisdictionValues> getAllJurisdictionsForUser(Integer userid);

	/**
	 * This method returns the jurisdiction based as on date and userid.
	 * @param userid the userid
	 * @param jurDate the jur date
	 * @return the jurisdictions for user
	 * @return
	 */
	Set<JurisdictionValues> getJurisdictionsForUser(Integer userid, Date jurDate);

	/**
	 * Returns the UserDetail with the given username in the first, middle OR the last name.
	 * @param userName The real name of the user to be searched in the system(First/Middle/Last Name)
	 * @return List of UserDetails matching the given criteria
	 */
	UserDetail getUserDetByUserName(String userName);

	/**
	 * deletes the user from the system.
	 * @param usr to be deleted
	 */
	void removeUser(User usr);

	/**
	 * deletes the user from the system.
	 * @param usrID the usr id
	 */
	void removeUser(Integer usrID);

	/**
	 * Gets the juridiction levels of the user.
	 * @param userid The userid whose jurisdiction levels are needed
	 * @return Set of the Jurisdiction Levels of this user
	 */
	Set<JurisdictionValues> getUserJurisdictions(Integer userid);

	/**
	 * Gets the all users for jurisdiction type.
	 * @param bt the bt
	 * @param topLvlBndryID the top lvl bndry id
	 * @return the all users for jurisdiction type
	 */
	Map getAllUsersForJurisdictionType(BoundaryType bt, Integer topLvlBndryID);

	/**
	 * Gets the all users for jurisdiction type full resolve.
	 * @param bt the bt
	 * @param topLevelBoundaryID the top level boundary id
	 * @return the all users for jurisdiction type full resolve
	 */
	Map getAllUsersForJurisdictionTypeFullResolve(BoundaryType bt, Integer topLevelBoundaryID);

	/**
	 * Gets the all users for jurisdiction full resolve.
	 * @param boundaryTypeList the boundary type list
	 * @param topLevelBoundaryID the top level boundary id
	 * @return the all users for jurisdiction full resolve
	 */
	Map getAllUsersForJurisdictionFullResolve(List boundaryTypeList, Integer topLevelBoundaryID);

	/**
	 * Gets the users by department.
	 * @param department the department
	 * @return the users by department
	 */
	List getUsersByDepartment(Department department);

	/**
	 * Find ip address.
	 * @param ipAddr the ip addr
	 * @return the list
	 */
	List findIpAddress(String ipAddr);

	/**
	 * Gets the all users.
	 * @return the all users
	 */
	List getAllUsers();

	/**
	 * Gets the jurisdiction value by bndry id and user id.
	 * @param bndryId the bndry id
	 * @param userId the user id
	 * @return the jurisdiction value by bndry id and user id
	 */
	JurisdictionValues getJurisdictionValueByBndryIdAndUserId(Integer bndryId, Integer userId);

	/**
	 * Gets the jurisdiction by bndry type id and user id.
	 * @param bndryTypeId the bndry type id
	 * @param userId the user id
	 * @return the jurisdiction by bndry type id and user id
	 */
	Jurisdiction getJurisdictionByBndryTypeIdAndUserId(Integer bndryTypeId, Integer userId);

	/**
	 * Gets the all users by role.
	 * @param roleList the role list
	 * @param topBoundaryID the top boundary id
	 * @param roleDate the role date
	 * @return the all users by role
	 */
	List getAllUsersByRole(List roleList, int topBoundaryID, Date roleDate);
}
