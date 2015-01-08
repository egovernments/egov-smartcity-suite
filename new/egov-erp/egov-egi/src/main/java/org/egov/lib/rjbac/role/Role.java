package org.egov.lib.rjbac.role;

import java.util.Date;
import java.util.Set;

import org.egov.lib.rrbac.model.Action;

/**
 * The Interface Role.
 */
public interface Role {
	
	/**
	 * Gets the id.
	 * 
	 * @return Returns the id.
	 */
	Integer getId();
	
	/**
	 * Sets the id.
	 * 
	 * @param id The id to set.
	 */
	void setId(Integer id);
	
	/**
	 * Gets the parent.
	 * 
	 * @return Returns the role to which this role reports to.
	 */
	Role getParent();
	
	/**
	 * Sets the parent.
	 * 
	 * @param reportsTo The reportsTo to set.
	 */
	void setParent(Role reportsTo);
	
	/**
	 * Gets the role desc.
	 * 
	 * @return Returns the roleDesc.
	 */
	String getRoleDesc();
	
	/**
	 * Sets the role desc.
	 * 
	 * @param roleDesc The roleDesc to set.
	 */
	void setRoleDesc(String roleDesc);
	
	/**
	 * Gets the role desc local.
	 * 
	 * @return Returns the roleDescLocal.
	 */
	String getRoleDescLocal();
	
	/**
	 * Sets the role desc local.
	 * 
	 * @param roleDescLocal The roleDescLocal to set.
	 */
	void setRoleDescLocal(String roleDescLocal);
	
	/**
	 * Gets the role name.
	 * 
	 * @return Returns the roleName.
	 */
	String getRoleName();
	
	/**
	 * Sets the role name.
	 * 
	 * @param roleName The roleName to set.
	 */
	void setRoleName(String roleName);
	
	/**
	 * Gets the role name local.
	 * 
	 * @return Returns the roleNameLocal.
	 */
	String getRoleNameLocal();
	
	/**
	 * Sets the role name local.
	 * 
	 * @param roleNameLocal The roleNameLocal to set.
	 */
	void setRoleNameLocal(String roleNameLocal);
	
	/**
	 * Gets the update time.
	 * 
	 * @return Returns the updateTime.
	 */
	Date getUpdateTime();
	
	/**
	 * Sets the update time.
	 * 
	 * @param updateTime The updateTime to set.
	 */
	void setUpdateTime(Date updateTime);
	
	/**
	 * Gets the update user id.
	 * 
	 * @return Returns the updateUserid.
	 */
	Integer getUpdateUserId();
	
	/**
	 * Sets the update user id.
	 * 
	 * @param updateUserid The updateUserid to set.
	 */
	void setUpdateUserId(Integer updateUserid);
	
	
	/**
	 * Gets the actions.
	 * 
	 * @return the actions
	 */
	Set getActions();
	
	/**
	 * Sets the actions.
	 * 
	 * @param actions the new actions
	 */
	void setActions(Set actions);
	
	/**
	 * Adds the action.
	 * 
	 * @param action the action
	 */
	void addAction(Action action);
	
	/**
	 * Removes the action.
	 * 
	 * @param action the action
	 */
	void removeAction(Action action);
}