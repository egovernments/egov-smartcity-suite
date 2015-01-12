/*
 * @(#)Action.java 3.0, 17 Jun, 2013 4:28:27 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.workflow;

import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.Script;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.services.SessionFactory;

public class Action extends BaseModel {
	
	private static final long serialVersionUID = 1L;
	public static final String BY_NAME_AND_TYPE = "BY_NAME_AND_TYPE";
	public static final String IN_NAMES_AND_TYPE = "IN_NAMES_AND_TYPE";
	private String name;
	private String description;
	private String type;
	private static final ScriptService scriptService = new ScriptService(1, 3, 10, 30);
	
	private Action() {
	}

	public Action(String name, String type, String description) {
		this.name = name;
		this.type = type;
		this.description = description;
	}

	public Object execute(StateAware item, PersistenceService service) {
		Script script = getScript(item, service);
		scriptService.setSessionFactory(new SessionFactory());
		return scriptService.executeScript(script,ScriptService.createContext("action", this, "wfItem", item, "persistenceService", service));
	}

	public Object execute(StateAware item, PersistenceService service, String comments) {
		Script script = getScript(item, service);
		scriptService.setSessionFactory(new SessionFactory());
		return scriptService.executeScript(script,ScriptService.createContext("action", this, "wfItem", item, "persistenceService", service, "comments", comments));
	}

	public Object execute(StateAware item, PersistenceService service, WorkflowService workflowService, String comments) {
		Script script = getScript(item, service);
		scriptService.setSessionFactory(new SessionFactory());
		return scriptService.executeScript(script,ScriptService.createContext("action", this, "wfItem", item, "persistenceService", service, "workflowService", workflowService, "comments", comments));
	}

	Script getScript(StateAware item, PersistenceService service) {
		List scripts = service.findAllByNamedQuery(Script.BY_NAME, item.getStateType() + ".workflow." + name);

		if (scripts == null || scripts.isEmpty()) {
			scripts = service.findAllByNamedQuery(Script.BY_NAME, item.getStateType() + ".workflow");
		}

		if (scripts == null || scripts.isEmpty()) {
			throw new EGOVRuntimeException("workflow.script.notfound");
		}
		Script script = (Script) scripts.get(0);
		return script;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getType() {
		return type;
	}

	// for hibernate
	private void setName(String name) {
		this.name = name;
	}

	private void setType(String type) {
		this.type = type;
	}

	private void setDescription(String description) {
		this.description = description;
	}

}
