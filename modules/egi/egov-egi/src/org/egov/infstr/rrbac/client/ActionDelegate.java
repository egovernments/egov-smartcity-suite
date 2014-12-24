/*
 * @(#)ActionDelegate.java 3.0, 17 Jun, 2013 4:50:01 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.rrbac.client;

import org.egov.lib.rrbac.model.Action;
import org.egov.lib.rrbac.model.Entity;
import org.egov.lib.rrbac.model.Task;
import org.egov.lib.rrbac.services.RbacService;
import org.egov.lib.rrbac.services.RbacServiceImpl;

public final class ActionDelegate {

	private static ActionDelegate actionDelegate = new ActionDelegate(); // a singleton instance
	private final RbacService rbacService = new RbacServiceImpl();

	private ActionDelegate() {

	}

	public static ActionDelegate getInstance() {
		return actionDelegate;
	}

	public Action getAction(final String name) {
		return this.rbacService.getActionByName(name);

	}

	public void createAction(final String actName, final Integer entId, final Integer tskId) {
		final String actionName = actName;
		final Integer entityId = entId;
		final Integer taskId = tskId;
		final Entity ent = this.rbacService.getEntityByID(entityId);
		final Task tsk = this.rbacService.getTaskByID(taskId);
		final Action act = new Action();
		act.setName(actionName);
		act.setEntityId(ent);
		act.setTaskId(tsk);
		this.rbacService.createAction(act);
	}

	public void updateAction(final String actName, final Integer actId, final Integer entId, final Integer tskId) {

		final String actionName = actName;
		final Integer actionId = actId;
		final Integer entityId = entId;
		final Integer taskId = tskId;
		final Action action = this.rbacService.getActionById(actionId);
		final Entity ent = this.rbacService.getEntityByID(entityId);
		final Task tsk = this.rbacService.getTaskByID(taskId);
		action.setName(actionName);
		action.setEntityId(ent);
		action.setTaskId(tsk);
		this.rbacService.updateAction(action);

	}

}
