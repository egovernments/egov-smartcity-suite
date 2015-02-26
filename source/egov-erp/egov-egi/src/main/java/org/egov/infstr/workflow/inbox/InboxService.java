/*
 * @(#)InboxService.java 3.0, 17 Jun, 2013 4:43:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.workflow.inbox;

import static org.apache.commons.lang.StringUtils.EMPTY;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.models.State;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.models.WorkflowTypes;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.Action;
import org.egov.lib.rjbac.user.User;
import org.egov.pims.commons.Position;

public class InboxService<T extends StateAware> {
	
	/** The Constant WF_TYPE_SERVICE_SFX. Suffix for all workflow type service for unique identification*/
	public static final String WF_TYPE_SERVICE_SFX = "WorkflowTypeService";
	
	/** The Constant SERVICE_NOT_FOUND. Error property if Render Service not found*/
	public static final String SERVICE_NOT_FOUND = "workflowtype.service.not.found";
	
	/** The Constant RENDER_Y. To check WF Item to be rendered in inbox*/
	public static final Character RENDER_Y = Character.valueOf('Y');
	
	/** The Constant GROUP_Y. To check WF Item to be grouped*/
	public static final Character GROUP_Y = Character.valueOf('Y');
	
	/** The Constant UNKNOWN. */
	public static final String UNKNOWN = "Unknown";
	
	/** The Constant SLASH. */
	public static final String SLASH = " / ";
	
	/** The W f_ types. WF Type cache to store all Workflow Types*/
	private static final Map<String, WorkflowTypes> WF_TYPES = new TreeMap<String, WorkflowTypes>();
	
	/** The workflow type services map. Delegate to WF Type Service*/
	private transient Map<String, WorkflowTypeService<T>> wfTypeServices;
	
	/** The state persistence service. Delegate to State Persistence Service*/
	private transient PersistenceService stateService;
	
	/** The workflow type service. Delegate to WorkflowTypes Persistence Service */
	private transient PersistenceService<org.egov.infstr.models.WorkflowTypes, Long> wfTypeDaoService;
	
	/** The eis service. Delegate to EIS Service*/
	private transient EISServeable eisService;
	
	
	/**
	 * To set EisService.
	 * @param eisService the new eis service
	 */
	public void setEisService(final EISServeable eisService) {
		this.eisService = eisService;
	}
	
	/**
	 * To set StatePersistenceService.
	 * @param stateService the new state persistence service
	 */
	public void setStatePersistenceService(final PersistenceService stateService) {
		this.stateService = stateService;
	}
	
	
	/**
	 * To set WorkflowTypeService.
	 * @param wfTypeService the wf type service
	 */
	public void setWorkflowTypeService(final PersistenceService<WorkflowTypes, Long> wfTypeService) {
		this.wfTypeDaoService = wfTypeService;
		this.initWorkflowTypes();
	}
	
	/**
	 * To set WorkflowTypeService Map.
	 * @param wfTypeServices the wf render services
	 */
	public void setWorkflowTypeServicesMap(final Map<String, WorkflowTypeService<T>> wfTypeServices) {
		this.wfTypeServices = wfTypeServices;
	}
	
	/**
	 * Initiate WF_TYPES HashMap to include all WorkflowTypes.
	 */
	public void initWorkflowTypes() {
		final List<WorkflowTypes> workflowTypesList = this.wfTypeDaoService.findAll();
		for (final WorkflowTypes workflowType : workflowTypesList) {
			if (RENDER_Y.equals(workflowType.getRenderYN())) {
				WF_TYPES.put(workflowType.getType(), workflowType);
			}
		}
	}
	
	/**
	 * To get the WorkflowTypee for a given WFType string.
	 * @param wfType the wf type
	 * @return WorkflowTypes
	 */
	public WorkflowTypes getWorkflowType(final String wfType) {
		return  WF_TYPES.get(wfType);
	}
	
	/**
	 * Return a list of the distinct workflow types that are assigned to the given position.
	 * @param posId the pos id
	 * @return the assigned workflow types
	 */
	public List<String> getAssignedWorkflowTypes(final Integer posId) {
		return this.stateService.findAllByNamedQuery(State.WORKFLOWTYPES_QRY, posId);
	}
	
	/**
	 * To get the RenderService for the given WorkflowType, no specific WFRenderService found return
	 * the DefaultRenderService.
	 * @param wfType the wf type
	 * @return {@link WorkflowTypeService}
	 */
	public WorkflowTypeService<T> getAssociatedService(final String wfType) {
		// check if wfTypeServices contains <workflowtype>Service
		// or else use default render service
		WorkflowTypeService<T> wfTypeService = null;
		if (WF_TYPES.containsKey(wfType)) {
			if (this.wfTypeServices.containsKey(wfType.concat(WF_TYPE_SERVICE_SFX))) {
				wfTypeService = this.wfTypeServices.get(wfType.concat(WF_TYPE_SERVICE_SFX));
			} else {
				final DefaultWorkflowTypeService<T> defaultService = (DefaultWorkflowTypeService<T>)this.wfTypeServices.get("default".concat(WF_TYPE_SERVICE_SFX));
				if (defaultService == null) {
					throw new EGOVRuntimeException(SERVICE_NOT_FOUND);
				}
				try {
					defaultService.setWorkflowType(Thread.currentThread().getContextClassLoader().loadClass(WF_TYPES.get(wfType).getFullyQualifiedName()));
				} catch (final ClassNotFoundException e) {
					throw new EGOVRuntimeException(SERVICE_NOT_FOUND, e);
				} catch (final Exception e) {
					throw new EGOVRuntimeException(SERVICE_NOT_FOUND, e);
				}
				wfTypeService = defaultService;
			}
		}
		return wfTypeService;
	}
	
	/**
	 * To get all Draft Item for the given arguments.
	 * @param owner the owner
	 * @param userId the user id
	 * @param order the order
	 * @return List of StateAware objects
	 */
	public List<T> getDraftItems(final Integer owner, final Integer userId, final String order) {
		final List<T> draftWfItems = new ArrayList<T>();
		final List<String> wfTypes = this.getAssignedWorkflowTypes(owner);
		for (final String wfType : wfTypes) {
			final WorkflowTypeService<T> wfTypeService = this.getAssociatedService(wfType);
			if (wfTypeService != null) {
				draftWfItems.addAll(wfTypeService.getDraftWorkflowItems(owner, userId, order));
			}
		}
		return draftWfItems;
	}
	
	/**
	 * To Filter out Inbox Items for the given criterial argument.
	 * @param owner the owner
	 * @param userId the user id
	 * @param sender the sender
	 * @param taskName the task name
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return List of {@link StateAware}
	 */
	public List<T> getFilteredInboxItems(final Integer owner, final Integer userId, final Integer sender, final String taskName, final Date fromDate, final Date toDate) {
		final List<T> filteredWFItems = new ArrayList<T>();
		List<String> wfTypes = null;
		
		if ((taskName == null) || EMPTY.equals(taskName.trim())) {
			wfTypes = this.getAssignedWorkflowTypes(owner);
		} else {
			wfTypes = new ArrayList<String>();
			final WorkflowTypes wfType = this.wfTypeDaoService.find("from org.egov.infstr.models.WorkflowTypes  where displayName=?", taskName);
			wfTypes.add(wfType.getType());
		}
		for (final String wfType : wfTypes) {
			final WorkflowTypeService<T> wfTypeService = this.getAssociatedService(wfType);
			if (wfTypeService != null) {
				filteredWFItems.addAll(wfTypeService.getFilteredWorkflowItems(owner, userId, sender, fromDate, toDate));
			}
		}
		return filteredWFItems;
	}
	
	/**
	 * Returns a list of workflow items that for the given criteria
	 * @param criteria the search criteria
	 * #mandatory key in criteria
	 * 1] wfType StateAware Type name
	 * #other non mandatory keys in criteria
	 * 1] sender Position list
	 * 2] user   Position list
	 * 3] fromDate
	 * 4] toDate
	 * 5] wfState
	 * @return the List of workflow items
	 */
	public List<T> getWorkflowItems(final Map<String,Object> criteria) {
		return this.getAssociatedService(criteria.get("wfType").toString()).getWorkflowItems(criteria);
	}
	
	public List<T> getWorkflowItems(final String wfType, final String myLinkId) {
		final WorkflowTypeService<T> wfTypeService = this.getAssociatedService(wfType);
		List <T> stateAwares = new ArrayList<T>();
 		if (wfTypeService != null) {
 			stateAwares = wfTypeService.getWorkflowItems(myLinkId);
		}
 		return stateAwares;
	}
	/**
	 * To get all Inbox Item for the given arguments.
	 * @param owner the owner
	 * @param userId the user id
	 * @param order the order
	 * @return List of StateAware objects
	 */
	public List<T> getWorkflowItems(final Integer owner, final Integer userId, final String order) {
		final List<T> assignedWFItems = new ArrayList<T>();
		final List<String> wfTypes = this.getAssignedWorkflowTypes(owner);
		for (final String wfType : wfTypes) {
			final WorkflowTypeService<T> wfTypeService = this.getAssociatedService(wfType);
			if (wfTypeService != null) {
				assignedWFItems.addAll(wfTypeService.getAssignedWorkflowItems(owner, userId, order));
			}
		}
		return assignedWFItems;
	}
	
	
	/**
	 * To get the Next Action user friendly value using the state's nextAction value to be queried
	 * with EG_WF_ACTION if exist return the corresponding Action Description else simply returns
	 * the State's nextAction value.
	 * @param state the state
	 * @return Next Action
	 */
	public String getNextAction(final State state) {
		String nextAction = EMPTY;
		if (state.getNextAction() != null) {
			final Action action = (Action) this.stateService.findByNamedQuery(Action.BY_NAME_AND_TYPE, state.getNextAction(), state.getType());
			if (action == null) {
				nextAction = state.getNextAction();
			} else {
				nextAction = (action.getDescription() == null ?  state.getNextAction() : action.getDescription());
			}
		}
		return nextAction;
	}
	
	/**
	 * To get the Inbox Item Sender.
	 * @param state the state
	 * @return Position
	 */
	public Position getStateUserPosition(final State state) {
		return state.getHistory() == null ? 
		       this.getPrimaryPositionForUser(state.getCreatedBy().getId(), state.getCreatedDate()) 
		           : state.getHistory().get(state.getHistory().size()-1).getOwnerPosition();
		
	}
	
	/**
	 * Gets the state user.
	 * @param state the state
	 * @param position the position
	 * @return the state user
	 */
	public User getStateUser(final State state, final Position position) {
		return this.getUserForPosition(position.getId(), state.getCreatedDate());
	}
	
	/**
	 * Helper method to create Sender Name.
	 * @param position the position
	 * @param user the user
	 * @return String sender name
	 */
	public String buildSenderName (final Position position, final User user) {
		final StringBuilder senderName = new StringBuilder();
		senderName.append( position == null ? UNKNOWN : position.getName()).append(SLASH).append(user == null ? UNKNOWN : user.getFirstName() + " " + (user.getLastName() == null ? EMPTY : user.getLastName()));
		return senderName.toString();
	}
	
	/**
	 * Delegate to EISService to get the Positions for the given User Id.
	 * @param userId the user id
	 * @param forDate the for date
	 * @return List of {@link Position}
	 */
	public List<Position> getPositionForUser(final Integer userId, final Date forDate) {
		return this.eisService.getPositionsForUser(userId, forDate);
	}
	
	/**
	 * Delegate to EISService to get the Primary Position for the given User Id.
	 * @param userId the user id
	 * @param forDate the for date
	 * @return {@link Position}
	 */
	public Position getPrimaryPositionForUser(final Integer userId, final Date forDate) {
		return this.eisService.getPrimaryPositionForUser(userId, forDate);
	}
	
	/**
	 * Delegate to EISService to get the User for the given Position Id.
	 * @param posId the pos id
	 * @param forDate the for date
	 * @return {@link User}
	 */
	public User getUserForPosition(final Integer posId, final Date forDate) {
		return this.eisService.getUserForPosition(posId, forDate);
	}
	
	/**
	 * To get the State object for the given Id.
	 * @param stateId the state id
	 * @return the state by id
	 */
	public State getStateById(final Long stateId) {
		return (State) this.stateService.findById(stateId, false);
	}
	
}
