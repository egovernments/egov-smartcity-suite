/*
 * @(#)Action.java 3.0, 14 Jun, 2013 4:35:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.egov.exceptions.RBACException;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infstr.commons.Module;

public class Action implements Comparable<Action> {

	private Integer id;
	private String name;
	private String url;
	private String queryParams;
	private Integer urlOrderId;
	private Date updatedTime;
	private Set roles = new HashSet();
	private Set ruleGroup = new HashSet();
	private Entity entityId;
	private Task taskId;
	private Module module;
	private Integer orderNumber;
	private String displayName;
	private Integer isEnabled;
	private String helpURL;
	private String contextRoot;

	/**
	 * @return Returns the helpURL.
	 */
	public String getHelpURL() {
		return helpURL;
	}

	/**
	 * @param helpURL The helpURL to set.
	 */
	public void setHelpURL(String helpURL) {
		this.helpURL = helpURL;
	}

	/**
	 * @return Returns the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id The id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return Returns the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return Returns the queryParams.
	 */
	public String getQueryParams() {
		return queryParams;
	}

	/**
	 * @param queryParams The queryParams to set.
	 */
	public void setQueryParams(String queryParams) {
		this.queryParams = queryParams;
	}

	/**
	 * @return Returns the urlOrderId.
	 */
	public Integer getUrlOrderId() {
		return urlOrderId;
	}

	/**
	 * @param urlOrderId The urlOrderId to set.
	 */
	public void setUrlOrderId(Integer urlOrderId) {
		this.urlOrderId = urlOrderId;
	}

	/**
	 * @return Returns the UpdatedTime.
	 */
	public Date getUpdatedTime() {
		return updatedTime;
	}

	/**
	 * @param UpdatedTime The UpdatedTime to set.
	 */
	public void setUpdatedTime(Date updatedtime) {
		this.updatedTime = updatedtime;
	}

	/**
	 * @param Entity To set entity for an Action
	 */
	public void setEntityId(Entity entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return Return Entity
	 */
	public Entity getEntityId() {
		return this.entityId;
	}

	/**
	 * @param Task To set task for an Action
	 */
	public void setTaskId(Task taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return Return Task
	 */
	public Task getTaskId() {
		return this.taskId;
	}

	/**
	 * @return Returns collections of roles associated with the action.
	 */
	public Set getRoles() {
		return roles;
	}

	/**
	 * @param roles The roles to set
	 */
	public void setRoles(Set roles) {
		this.roles = roles;
	}

	/**
	 * @param role The roles to add
	 */
	public void addRole(Role role) {
		getRoles().add(role);
		role.addAction(this);
	}

	/**
	 * @param role The roles to Remove
	 */
	public void removeRole(Role role) {

		role.removeAction(this);
		if (getRoles().contains(role))
			getRoles().remove(role);

	}

	/**
	 * @return Returns the ruleGroup.
	 */
	public Set getRuleGroup() {
		return ruleGroup;
	}

	/**
	 * @param ruleGroup To set ruleGroup for an action
	 */
	public void setRuleGroup(Set ruleGroup) {
		this.ruleGroup = ruleGroup;
	}

	/**
	 * @param ruleGroup The ruleGroup to add
	 */
	public void addRuleGroup(RuleGroup rg) {
		getRuleGroup().add(rg);
	}

	/**
	 * @param ruleGroup The ruleGroup to Remove
	 */
	public void removeRuleGroup(RuleGroup rg) {

		if (getRuleGroup().contains(rg))
			getRuleGroup().remove(rg);

	}

	/**
	 * @return Returns the displayName.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName The displayName to set.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return Returns the module.
	 */
	public Module getModule() {
		return module;
	}

	/**
	 * @param module The module to set.
	 */
	public void setModule(Module module) {
		this.module = module;
	}

	/**
	 * @return Returns the orderNumber.
	 */
	public Integer getOrderNumber() {
		return orderNumber;
	}

	/**
	 * @param orderNumber The orderNumber to set.
	 */
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * @return Returns the isEnabled.
	 */
	public Integer getIsEnabled() {
		return isEnabled;
	}

	/**
	 * @param isEnabled The isEnabled to set.
	 */
	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getContextRoot() {
		return contextRoot;
	}

	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}

	/**
	 * @return Returns if the given Object is equal to Action
	 */
	public boolean equals(Object obj) {

		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (!(obj instanceof Action))
			return false;

		final Action other = (Action) obj;

		if (getName().equals(other.getName())) {
			return true;
		} else
			return false;
	}

	/**
	 * @return Returns the hashCode
	 */
	public int hashCode() {
		int hashCode = 0;
		hashCode = hashCode + this.getName().hashCode();

		return hashCode;
	}

	public int compareTo(Action action) throws ClassCastException {
		return (action == null || action.getName() == null) ? -1 : this.name.compareTo(action.getName());
	}

	/**
	 * @return Returns true if conditions are satisfied for the action for a given role and action if rulegroup is specified then action is validated against rules in that rulegroup
	 */
	public boolean isValid(RuleData obj, Role role) throws RBACException {
		Set rg = this.getRuleGroup();
		boolean retVal = true;
		for (Iterator ite = rg.iterator(); ite.hasNext();) {
			RuleGroup ruleGroup = (RuleGroup) ite.next();
			Role roleObj = ruleGroup.getRoleId();
			if (role.getId().equals(roleObj.getId())) {
				retVal = false;
				String ruleGroupName = ruleGroup.getName();
				Set ruleList = ruleGroup.getRules();
				for (Iterator iterator = ruleList.iterator(); iterator.hasNext();) {
					Rules rules = (Rules) iterator.next();
					try {
						retVal = rules.isValid(obj);
					} catch (RBACException e) {
						throw e;

					}

				}
			}
		}
		return retVal;
	}

	/**
	 * @return Returns true if action is valid for role
	 */
	public boolean isActionValid(Role role) throws RBACException {
		if (this.roles.contains(role))
			return true;
		else {
			throw new RBACException("Unauthorized to Perform this Action ");
		}
	}

	@Override
	public String toString() {
		StringBuffer actionString = new StringBuffer();
		actionString.append("Action: ( ").append("ID: " + id).append(" name: " + name).append(" URl : " + url).append(" queryparams: " + queryParams + " )");
		return actionString.toString();
	}
}