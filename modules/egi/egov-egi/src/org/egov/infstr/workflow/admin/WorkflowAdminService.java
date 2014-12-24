/*
 * @(#)WorkflowAdminService.java 3.0, 17 Jun, 2013 4:45:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.workflow.admin;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.egov.infstr.models.State;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.models.WorkflowTypes;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.infstr.workflow.inbox.InboxService;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.pims.commons.Position;

public class WorkflowAdminService {
	
	private transient PersistenceService daoService;
	private transient EISServeable eisService;
	private transient UserService userService;
	private transient InboxService<StateAware> inboxService; // Delegate to inbox service
	private transient WorkflowService<StateAware> workflowService;
	
	/**
	 * Gets the all user by user name.
	 * @param userName the user name
	 * @return the all user by user name
	 */
	public List<User> getAllUserByUserName(final String userName) {
		return this.userService.getAllUserByUserNameLike(userName);
	}
	
	/**
	 * Gets the workflow state values.
	 * @param wfType the wf type
	 * @return the workflow state values
	 */
	public List<String> getWorkflowStateValues(final String wfType) {
		return this.daoService.findAllBy("select state.value from org.egov.infstr.models.State as state where state.type=? and state.value != ? and state.value != ? group by state.value", wfType, State.NEW, State.END);
	}
	
	/**
	 * Gets the workflow types.
	 * @param name the name
	 * @return the workflow types
	 */
	public List<WorkflowTypes> getWorkflowTypes(final String name) {
		return this.daoService.findAllByNamedQuery(WorkflowTypes.TYPE_LIKE_NAME, name.toLowerCase(Locale.ENGLISH) + "%");
	}
	
	/**
	 * Reassign wf item.
	 * @param wfType the wf type
	 * @param stateId the state id
	 * @param newUserId the new user id
	 * @return the string
	 */
	public String reassignWFItem(final String wfType, final String stateId, final Integer newUserId) {
		String status = "ERROR";
		List<StateAware> stateAwares = inboxService.getWorkflowItems(wfType, stateId);
		if (!stateAwares.isEmpty()) {
			final Position newOwner = this.eisService.getPrimaryPositionForUser(newUserId, new Date());
			if (stateAwares.get(0).getCurrentState().getOwner().getId().equals(newOwner.getId())) {
				status = "OWNER-SAME";
			} else {
				status = "RE-ASSIGNED";
				for (StateAware stateAware : stateAwares) {
					this.workflowService.setType(stateAware.getClass());
					this.workflowService.transition(stateAware, newOwner, status);
				}				
			}
		}
		return status;
	}
	
	
	/**
	 * To set EisService.
	 * @param eisService the new eis service
	 */
	public void setEisService(final EISServeable eisService) {
		this.eisService = eisService;
	}
	
	/**
	 * Sets the inbox service.
	 * @param inboxService the new inbox service
	 */
	public void setInboxService(final InboxService<StateAware> inboxService) {
		this.inboxService = inboxService;
	}
	
	/**
	 * To set StatePersistenceService.
	 * @param daoService the new persistence service
	 */
	public void setPersistenceService(final PersistenceService daoService) {
		this.daoService = daoService;
	}
	
	/**
	 * To set User Manager.
	 * @param userManager the new user manager
	 */
	public void setUserService(final UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * Sets the workflow service.
	 * @param workflowService the new workflow service
	 */
	public void setWorkflowService(final WorkflowService<StateAware> workflowService) {
		this.workflowService = workflowService;
	}
}
