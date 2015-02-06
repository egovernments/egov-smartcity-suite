/*
 * @(#)RbacServiceImpl.java 3.0, 14 Jun, 2013 4:13:59 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.services;

import org.egov.lib.rrbac.dao.RBCDAOFactory;
import org.egov.lib.rrbac.model.Action;
import org.egov.lib.rrbac.model.Entity;
import org.egov.lib.rrbac.model.RuleGroup;
import org.egov.lib.rrbac.model.RuleType;
import org.egov.lib.rrbac.model.Rules;
import org.egov.lib.rrbac.model.Task;

import java.util.List;


public class RbacServiceImpl implements RbacService {

	/**
	 * To create an Entity.
	 * @param ent the ent
	 */
	@Override
	public void createEntity(final Entity ent) {
		RBCDAOFactory.getDAOFactory().getEntityDAO().create(ent);
	}

	/**
	 * Gets the entity by id.
	 * @param id the id
	 * @return Entity object
	 */
	@Override
	public Entity getEntityByID(final Integer id) {
		return (Entity) RBCDAOFactory.getDAOFactory().getEntityDAO().findById(id, false);
	}

	/**
	 * Gets the entity by name.
	 * @param name the name
	 * @return entity object
	 */
	@Override
	public Entity getEntityByName(final String name) {
		return RBCDAOFactory.getDAOFactory().getEntityDAO().findEntityByName(name);

	}

	/**
	 * Returns collection of all entity.
	 * @return List
	 */
	@Override
	public List<Entity> getEntityList() {
		return RBCDAOFactory.getDAOFactory().getEntityDAO().findAll();
	}

	/**
	 * To update an Entity.
	 * @param ent the ent
	 */
	@Override
	public void updateEntity(final Entity ent) {
		RBCDAOFactory.getDAOFactory().getEntityDAO().update(ent);
	}

	/**
	 * To create an Task.
	 * @param tsk the tsk
	 */
	@Override
	public void createTask(final Task tsk) {
		RBCDAOFactory.getDAOFactory().getTaskDAO().create(tsk);
	}

	/**
	 * Gets the task by id.
	 * @param id the id
	 * @return Task object
	 */
	@Override
	public Task getTaskByID(final Integer id) {
		return (Task) RBCDAOFactory.getDAOFactory().getTaskDAO().findById(id, false);
	}

	/**
	 * Returns collection of all Task.
	 * @return List
	 */
	@Override
	public List<Task> getTaskList() {
		return RBCDAOFactory.getDAOFactory().getTaskDAO().findAll();
	}

	/**
	 * Gets the task by name.
	 * @param name the name
	 * @return task object
	 */
	@Override
	public Task getTaskByName(final String name) {
		return RBCDAOFactory.getDAOFactory().getTaskDAO().findTaskByName(name);

	}

	/**
	 * To update an Task.
	 * @param tsk the tsk
	 */
	@Override
	public void updateTask(final Task tsk) {
		RBCDAOFactory.getDAOFactory().getTaskDAO().update(tsk);
	}

	/**
	 * To create an Action.
	 * @param ac Action object
	 */
	@Override
	public void createAction(final Action ac) {
		RBCDAOFactory.getDAOFactory().getActionDAO().create(ac);
	}

	/**
	 * To delete an Action.
	 * @param ac the ac
	 */
	@Override
	public void deleteAction(final Action ac) {
		RBCDAOFactory.getDAOFactory().getActionDAO().delete(ac);
	}

	/**
	 * Gets the action by id.
	 * @param id the id
	 * @return Action object
	 */
	@Override
	public Action getActionById(final Integer id) {
		return (Action) RBCDAOFactory.getDAOFactory().getActionDAO().findById(id, false);

	}

	/**
	 * Gets the action by name.
	 * @param name the name
	 * @return Action object
	 */
	@Override
	public Action getActionByName(final String name) {
		return RBCDAOFactory.getDAOFactory().getActionDAO().findActionByName(name);
	}

	/**
	 * Gets the action by url.
	 * @param contextPath the context path
	 * @param url the url
	 * @return Action object
	 */
	@Override
	public Action getActionByURL(final String contextPath, final String url) {
		return RBCDAOFactory.getDAOFactory().getActionDAO().findActionByURL(contextPath, url);
	}

	/**
	 * Returns collection of all actions.
	 * @return List
	 */
	@Override
	public List<Action> getActionList() {
		return RBCDAOFactory.getDAOFactory().getActionDAO().findAll();

	}

	/**
	 * Returns collection of all actions with Roles.
	 * @return List
	 */
	@Override
	public List<Action> getActionListWithRoles() {
		return RBCDAOFactory.getDAOFactory().getActionDAO().getActionWithRoles();
	}

	/**
	 * Returns collection of all actions with Rulegroup.
	 * @return List
	 */
	@Override
	public List<Action> getActionListWithRG() {
		return RBCDAOFactory.getDAOFactory().getActionDAO().getActionWithRG();
	}

	/**
	 * To update an Action.
	 * @param act the act
	 */
	@Override
	public void updateAction(final Action act) {
		RBCDAOFactory.getDAOFactory().getActionDAO().update(act);
	}

	/**
	 * Gets the rule group by id.
	 * @param ruleGrpId the rule grp id
	 * @return RuleGroup object
	 */
	@Override
	public RuleGroup getRuleGroupById(final Integer ruleGrpId) {
		return (RuleGroup) RBCDAOFactory.getDAOFactory().getRuleGroupDAO().findById(ruleGrpId, false);

	}

	/**
	 * Gets the rule group by name.
	 * @param name the name
	 * @return RuleGroup object
	 */
	@Override
	public RuleGroup getRuleGroupByName(final String name) {
		return RBCDAOFactory.getDAOFactory().getRuleGroupDAO().findRuleGroupByName(name);

	}

	/**
	 * Gets the rule by name.
	 * @param name the name
	 * @return Rules object
	 */
	@Override
	public Rules getRuleByName(final String name) {
		return RBCDAOFactory.getDAOFactory().getRulesDAO().findRulesByName(name);

	}

	/**
	 * Returns collection of all ruleGroups.
	 * @return List
	 */
	@Override
	public List<RuleGroup> getRuleGroupList() {
		return RBCDAOFactory.getDAOFactory().getRuleGroupDAO().findAll();
	}

	/**
	 * Returns collection of all RuleType.
	 * @return List
	 */
	@Override
	public List<RuleType> getRuleTypeList() {
		return RBCDAOFactory.getDAOFactory().getRuleTypeDAO().findAll();
	}
}