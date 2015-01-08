/*
 * @(#)RbacService.java 3.0, 14 Jun, 2013 4:01:16 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.services;

import java.util.List;

import org.egov.lib.rrbac.model.Action;
import org.egov.lib.rrbac.model.Entity;
import org.egov.lib.rrbac.model.RuleGroup;
import org.egov.lib.rrbac.model.RuleType;
import org.egov.lib.rrbac.model.Rules;
import org.egov.lib.rrbac.model.Task;

public interface RbacService {

	/**
	 * To create an Entity.
	 * @param ent the ent
	 */
	public void createEntity(Entity ent);

	/**
	 * Gets the entity by id.
	 * @param id the id
	 * @return Entity object
	 */
	public Entity getEntityByID(Integer id);

	/**
	 * Gets the entity by name.
	 * @param name the name
	 * @return entity object
	 */
	public Entity getEntityByName(String name);

	/**
	 * Returns collection of all Entity.
	 * @return List
	 */
	public List<Entity> getEntityList();

	/**
	 * To update an Entity.
	 * @param ent the ent
	 */
	public void updateEntity(Entity ent);

	/**
	 * To create an task.
	 * @param tsk the tsk
	 */
	public void createTask(Task tsk);

	/**
	 * Gets the task by id.
	 * @param id the id
	 * @return task object
	 */
	public Task getTaskByID(Integer id);

	/**
	 * Gets the task by name.
	 * @param name the name
	 * @return task object
	 */
	public Task getTaskByName(String name);

	/**
	 * Returns collection of all task.
	 * @return List
	 */
	public List<Task> getTaskList();

	/**
	 * To update an Task.
	 * @param tsk the tsk
	 */
	public void updateTask(Task tsk);

	/**
	 * Gets the action by id.
	 * @param id the id
	 * @return Action object
	 */
	public Action getActionById(Integer id);

	/**
	 * Gets the action by name.
	 * @param name the name
	 * @return Action object
	 */
	public Action getActionByName(String name);

	/**
	 * Gets the action by url.
	 * @param contextPath - the root context path of the web application
	 * @param url - the url of the web page stripped of server name, port and context path. Contains query parameters
	 * @return Action object
	 */
	public Action getActionByURL(String contextPath, String url);

	/**
	 * To create an Action.
	 * @param ac Action object
	 */
	public void createAction(Action ac);

	/**
	 * To delete an Action.
	 * @param ac the ac
	 */
	public void deleteAction(Action ac);

	/**
	 * Returns collection of all actions.
	 * @return List
	 */
	public List<Action> getActionList();

	/**
	 * Returns collection of all actions with Roles.
	 * @return List
	 */
	public List<Action> getActionListWithRoles();

	/**
	 * Returns collection of all actions with Rulegroup.
	 * @return List
	 */
	public List<Action> getActionListWithRG();

	/**
	 * To update an Action.
	 * @param act the act
	 */
	public void updateAction(Action act);

	/**
	 * Gets the rule group by id.
	 * @param id the id
	 * @return RuleGroup object
	 */
	public RuleGroup getRuleGroupById(Integer id);

	/**
	 * Gets the rule group by name.
	 * @param name the name
	 * @return RuleGroup object
	 */
	public RuleGroup getRuleGroupByName(String name);

	/**
	 * Gets the rule by name.
	 * @param name the name
	 * @return Rules object
	 */
	public Rules getRuleByName(String name);

	/**
	 * Returns collection of all ruleGroups.
	 * @return List
	 */
	public List<RuleGroup> getRuleGroupList();

	/**
	 * Returns collection of all RuleType.
	 * @return List
	 */
	public List<RuleType> getRuleTypeList();

}