/*
 * @(#)InboxAction.java 3.0, 14 Jun, 2013 1:16:52 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.actions.workflow;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.egov.infstr.utils.DateUtils.getFormattedDate;
import static org.egov.infstr.utils.StringUtils.escapeSpecialChars;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.models.State;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.models.WorkflowTypes;
import org.egov.infstr.workflow.inbox.InboxComparator;
import org.egov.infstr.workflow.inbox.InboxService;
import org.egov.lib.rjbac.user.User;
import org.egov.pims.commons.Position;
import org.egov.web.actions.BaseFormAction;

@ParentPackage("egov")
public class InboxAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(InboxAction.class);
	private transient final Map<Integer, String> senderList = new HashMap<Integer, String>();
	private transient final Map<String, String> taskList = new HashMap<String, String>();
	private transient InboxService<StateAware> inboxService; // Delegate to inbox service
	private transient InboxComparator inboxComparator;
	private transient StringBuilder inboxData; // To hold inbox data
	private transient StringBuilder inboxDraft; // To hold inbox draft data
	private transient StringBuilder inboxHistory; // To hold history data
	private transient String stateId; // State Id parameter to fetch the State history
	private transient Integer sender;
	private transient String task;
	private transient Date fromDate;
	private transient Date toDate;

	public void setInboxComparator(final InboxComparator inboxComparator) {
		this.inboxComparator = inboxComparator;
	}

	public void setInboxService(final InboxService<StateAware> inboxService) {
		this.inboxService = inboxService;
	}

	public void setSender(final Integer sender) {
		this.sender = sender;
	}

	public void setStateId(final String stateId) {
		this.stateId = stateId;
	}

	public void setTask(final String task) {
		this.task = task;
	}

	public void setToDate(final Date toDate) {
		this.toDate = toDate;
	}

	public void setFromDate(final Date fromDate) {
		this.fromDate = fromDate;
	}

	public String getInboxData() {
		return this.inboxData == null ? "" : this.inboxData.toString();
	}

	public String getInboxDraft() {
		return this.inboxDraft == null ? "" : this.inboxDraft.toString();
	}

	public String getInboxHistory() {
		return this.inboxHistory == null ? "" : this.inboxHistory.toString();
	}

	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * Initial Inbox Rendering
	 */
	@Override
	public String execute() {
		final Integer userId = this.getLoginUserId();
		final List<Position> positions = this.inboxService.getPositionForUser(userId, new Date());
		final List<StateAware> inboxItem = new ArrayList<StateAware>();
		final List<StateAware> inboxDraftItem = new ArrayList<StateAware>();
		for (final Position position : positions) {
			inboxItem.addAll(this.inboxService.getWorkflowItems(position.getId(), userId, null));
			inboxDraftItem.addAll(this.inboxService.getDraftItems(position.getId(), userId, null));
		}
		this.inboxData = this.loadInboxData(inboxItem);
		this.inboxDraft = this.loadInboxData(inboxDraftItem);
		return SUCCESS;
	}

	/**
	 * Filter Inbox Data
	 */
	public String filterInboxData() throws EGOVRuntimeException, IOException {
		try {
			final List<Position> positions = this.inboxService.getPositionForUser(this.getLoginUserId(), new Date());
			final List<StateAware> filteredInboxItem = new ArrayList<StateAware>();
			for (final Position position : positions) {
				filteredInboxItem.addAll(this.inboxService.getFilteredInboxItems(position.getId(), this.getLoginUserId(), this.sender, this.task, this.fromDate, this.toDate));
			}
			this.inboxData = this.loadInboxData(filteredInboxItem);
			this.writeToAjaxResponse(this.getInboxData());
		} catch (final Exception e) {
			LOG.error("Error occurred while getting filtered Inbox Items, Cause : " + e.getMessage(), e);
			this.writeToAjaxResponse(ERROR);
		}
		return null;
	}

	/**
	 * Polling inbox draft using refresh button and auto polling every 30 mints
	 */
	public String pollDraft() throws EGOVRuntimeException, IOException {
		try {
			final Integer userId = this.getLoginUserId();
			final List<Position> positions = this.inboxService.getPositionForUser(userId, new Date());
			final List<StateAware> inboxDraftItem = new ArrayList<StateAware>();
			for (final Position position : positions) {
				inboxDraftItem.addAll(this.inboxService.getDraftItems(position.getId(), userId, null));
			}
			this.inboxDraft = this.loadInboxData(inboxDraftItem);
			this.writeToAjaxResponse(this.getInboxDraft());
		} catch (final Exception e) {
			LOG.error("Error occurred while getting Inbox Draft Items, Cause : " + e.getMessage(), e);
			this.writeToAjaxResponse(ERROR);
		}
		return null;
	}

	/**
	 * Polling inbox using refresh button and auto polling every 30 mints
	 */
	public String pollInbox() throws EGOVRuntimeException, IOException {
		try {
			final Integer userId = this.getLoginUserId();
			final List<Position> positions = this.inboxService.getPositionForUser(userId, new Date());
			final List<StateAware> inboxItem = new ArrayList<StateAware>();
			for (final Position position : positions) {
				inboxItem.addAll(this.inboxService.getWorkflowItems(position.getId(), userId, null));
			}
			this.inboxData = this.loadInboxData(inboxItem);
			this.writeToAjaxResponse(this.getInboxData());
		} catch (final Exception e) {
			LOG.error("Error occurred while getting Inbox Items, Cause : " + e.getMessage(), e);
			this.writeToAjaxResponse(ERROR);
		}
		return null;
	}

	/**
	 * Ajax call to populate state history (Audit trail) of StateAware
	 */
	public String populateHistory() throws EGOVRuntimeException, IOException {
		try {
			final State state = this.inboxService.getStateById(Long.parseLong(this.stateId));
			this.inboxHistory = this.loadInboxHistoryData(state);
			this.writeToAjaxResponse(this.getInboxHistory());
		} catch (final Exception e) {
			LOG.error("Error occurred while getting Inbox History Items, Cause : " + e.getMessage(), e);
			this.writeToAjaxResponse(ERROR);
		}
		return null;
	}

	/**
	 * To get User Name from the HTTP Session attribute.
	 */
	public String getUserName() {
		final HttpServletRequest request = ServletActionContext.getRequest();
		return (String) request.getSession().getAttribute("com.egov.user.LoginUserName");
	}

	/**
	 * To get all the Task Name (Workflow Type Display Name)
	 */
	public Map<String, String> getTaskList() {
		return this.taskList;
	}

	/**
	 * To get all the Sender Name
	 */
	public Map<Integer, String> getSenderList() {
		return this.senderList;
	}

	/**
	 * To get the State Id
	 */
	public String getStateId() {
		return this.stateId;
	}

	/**
	 * JSON String creation of Inbox Data
	 */
	private StringBuilder loadInboxData(final List<StateAware> inboxStates) throws EGOVRuntimeException {
		final StringBuilder inboxItem = new StringBuilder("");
		if ((inboxStates != null) && (!inboxStates.isEmpty())) {
			Collections.sort(inboxStates, this.inboxComparator);
			inboxItem.append("[");
			for (final StateAware stateAware : inboxStates) {
				final State state = stateAware.getCurrentState();
				final WorkflowTypes workflowTypes = this.inboxService.getWorkflowType(stateAware.getStateType());
				final Position position = this.inboxService.getStateUserPosition(state);
				final User user = this.inboxService.getStateUser(state, position);
				this.taskList.put(workflowTypes.getType(), workflowTypes.getDisplayName());
				this.senderList.put(position.getId(), position.getName());
				inboxItem.append("{Id:'").append(InboxService.GROUP_Y.equals(workflowTypes.getGroupYN()) ? EMPTY : state.getId()).append("#").append(workflowTypes.getId()).append("',");
				inboxItem.append("Date:'").append(getFormattedDate(state.getCreatedDate(), "dd/MM/yyyy hh:mm a")).append("',");
				inboxItem.append("Sender:'").append(this.inboxService.buildSenderName(position, user)).append("',");
				inboxItem.append("Task:'").append(workflowTypes.getDisplayName()).append("',");
				final String nextAction = this.inboxService.getNextAction(state);
				inboxItem.append("Status:'").append(state.getValue()).append(EMPTY.equals(nextAction) ? EMPTY : " - " + nextAction).append("',");
				inboxItem.append("Details:'").append(stateAware.getStateDetails() == null ? EMPTY : escapeSpecialChars(stateAware.getStateDetails())).append("',");
				inboxItem.append("Link:'").append(workflowTypes.getLink().replace(":ID", stateAware.myLinkId())).append("'},");
			}
			inboxItem.deleteCharAt(inboxItem.length() - 1);
			inboxItem.append("]");
		}
		return inboxItem;
	}

	/**
	 * JSON String creation of History Data.
	 * @param states the states
	 * @return the string builder
	 * @throws EGOVRuntimeException the eGOV runtime exception
	 */
	private StringBuilder loadInboxHistoryData(final State states) throws EGOVRuntimeException {
		final StringBuilder inboxHistoryItem = new StringBuilder("");
		if (states != null) {
			final List<State> stateHistory = states.getHistory();
			Collections.reverse(stateHistory);
			inboxHistoryItem.append("[");
			for (final State state : stateHistory) {
				final Position position = this.inboxService.getStateUserPosition(state);
				final User user = this.inboxService.getStateUser(state, position);
				final WorkflowTypes workflowTypes = this.inboxService.getWorkflowType(state.getType());
				inboxHistoryItem.append("{Id:'").append(state.getId()).append("',");
				inboxHistoryItem.append("Date:'").append(getFormattedDate(state.getCreatedDate(), "dd/MM/yyyy hh:mm a")).append("',");
				inboxHistoryItem.append("Sender:'").append(this.inboxService.buildSenderName(position, user)).append("',");
				inboxHistoryItem.append("Task:'").append(workflowTypes.getDisplayName()).append("',");
				final String nextAction = this.inboxService.getNextAction(state);
				inboxHistoryItem.append("Status:'").append(state.getValue()).append(EMPTY.equals(nextAction) ? EMPTY : "~" + nextAction).append("',");
				inboxHistoryItem.append("Details:'").append(state.getText1() == null ? EMPTY : escapeSpecialChars(state.getText1())).append("',");
				inboxHistoryItem.append("Signature:'").append("<img src=\"/egi/common/imageRenderer!getUserSignature.action?id=").append(user.getId())
						.append("\" height=\"50\" width=\"150\" alt=\"No User Signature\" onerror=\"this.parentNode.removeChild(this);\"/>").append("',");
				inboxHistoryItem.append("Link:''},");
			}
			inboxHistoryItem.deleteCharAt(inboxHistoryItem.length() - 1);
			inboxHistoryItem.append("]");
		}
		return inboxHistoryItem;
	}

	/**
	 * Helper method to get the User Id
	 * @return Integer userId
	 */
	private Integer getLoginUserId() {
		return Integer.valueOf(EGOVThreadLocals.getUserId());
	}

	/**
	 * Helper method to write Ajax response string
	 * @return
	 */
	private void writeToAjaxResponse(final String response) throws EGOVRuntimeException, IOException {
		final HttpServletResponse httpResponse = ServletActionContext.getResponse();
		httpResponse.getWriter().write(response);
	}
}
