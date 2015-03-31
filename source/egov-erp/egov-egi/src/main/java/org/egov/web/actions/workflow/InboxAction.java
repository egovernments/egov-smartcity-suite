package org.egov.web.actions.workflow;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.egov.infstr.utils.DateUtils.getFormattedDate;
import static org.egov.infstr.utils.StringUtils.escapeSpecialChars;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.egov.infra.workflow.inbox.InboxRenderService;
import org.egov.infra.workflow.inbox.InboxRenderServiceDeligate;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.pims.commons.Position;
import org.egov.web.actions.BaseFormAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ParentPackage("egov")
public class InboxAction extends BaseFormAction {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(InboxAction.class);
    private transient final Map<Long, String> senderList = new HashMap<Long, String>();
    private transient final Map<String, String> taskList = new HashMap<String, String>();
    private transient InboxRenderServiceDeligate<StateAware> inboxRenderServiceDeligate;
    private transient StringBuilder inboxData;
    private transient StringBuilder inboxDraft;
    private transient StringBuilder inboxHistory;
    private transient String stateId;
    private transient Long sender;
    private transient String task;
    private transient Date fromDate;
    private transient Date toDate;

    public void setInboxRenderServiceDeligate(final InboxRenderServiceDeligate<StateAware> inboxRenderServiceDeligate) {
        this.inboxRenderServiceDeligate = inboxRenderServiceDeligate;
    }

    public void setSender(final Long sender) {
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
        return inboxData == null ? "" : inboxData.toString();
    }

    public String getInboxDraft() {
        return inboxDraft == null ? "" : inboxDraft.toString();
    }

    public String getInboxHistory() {
        return inboxHistory == null ? "" : inboxHistory.toString();
    }

    @Override
    public Object getModel() {
        return null;
    }

    @Override
    public String execute() {
        return SUCCESS;
    }

    public String filterInboxData() throws EGOVRuntimeException, IOException {
        try {
            final List<Position> positions = inboxRenderServiceDeligate.getPositionForUser(getLoginUserId(), new Date());
            final List<StateAware> filteredInboxItem = new ArrayList<StateAware>();
            for (final Position position : positions)
                filteredInboxItem.addAll(inboxRenderServiceDeligate.getFilteredInboxItems(position.getId(), getLoginUserId(), sender,
                        task, fromDate, toDate));
            inboxData = loadInboxData(filteredInboxItem);
            writeToAjaxResponse(getInboxData());
        } catch (final Exception e) {
            LOG.error("Error occurred while getting filtered Inbox Items, Cause : " + e.getMessage(), e);
            writeToAjaxResponse(ERROR);
        }
        return null;
    }

    @Action("/workflow/inbox-pollDraft")
    public String pollDraft() throws EGOVRuntimeException, IOException {
        try {
            final Long userId = getLoginUserId();
            final List<Position> positions = inboxRenderServiceDeligate.getPositionForUser(userId, new Date());
            final List<StateAware> inboxDraftItem = new ArrayList<StateAware>();
            for (final Position position : positions)
                inboxDraftItem.addAll(inboxRenderServiceDeligate.getDraftItems(position.getId(), userId, null));
            inboxDraft = loadInboxData(inboxDraftItem);
            writeToAjaxResponse(getInboxDraft());
        } catch (final Exception e) {
            LOG.error("Error occurred while getting Inbox Draft Items, Cause : " + e.getMessage(), e);
            writeToAjaxResponse(ERROR);
        }
        return null;
    }

    @Action("/workflow/inbox-pollInbox")
    public String pollInbox() throws EGOVRuntimeException, IOException {
        try {
            final Long userId = getLoginUserId();
            final List<Position> positions = inboxRenderServiceDeligate.getPositionForUser(userId, new Date());
            final List<StateAware> inboxItem = new ArrayList<StateAware>();
            for (final Position position : positions)
                inboxItem.addAll(inboxRenderServiceDeligate.getWorkflowItems(position.getId(), userId, null));
            inboxData = loadInboxData(inboxItem);
            writeToAjaxResponse(getInboxData());
        } catch (final Exception e) {
            LOG.error("Error occurred while getting Inbox Items, Cause : " + e.getMessage(), e);
            writeToAjaxResponse(ERROR);
        }
        return null;
    }

    @Action("/workflow/inbox-populateHistory")
    public String populateHistory() throws EGOVRuntimeException, IOException {
        try {
            final State state = inboxRenderServiceDeligate.getStateById(Long.parseLong(stateId));
            inboxHistory = loadInboxHistoryData(state);
            writeToAjaxResponse(getInboxHistory());
        } catch (final Exception e) {
            LOG.error("Error occurred while getting Inbox History Items, Cause : " + e.getMessage(), e);
            writeToAjaxResponse(ERROR);
        }
        return null;
    }

    public String getUserName() {
        final HttpServletRequest request = ServletActionContext.getRequest();
        return (String) request.getSession().getAttribute("com.egov.user.LoginUserName");
    }

    public Map<String, String> getTaskList() {
        return taskList;
    }

    public Map<Long, String> getSenderList() {
        return senderList;
    }

    public String getStateId() {
        return stateId;
    }

    private StringBuilder loadInboxData(final List<StateAware> inboxStates) throws EGOVRuntimeException {
        final StringBuilder inboxItem = new StringBuilder("");
        if (inboxStates != null && !inboxStates.isEmpty()) {
            inboxStates.sort(byCreatedDate());
            inboxItem.append("[");
            for (final StateAware stateAware : inboxStates) {
                final State state = stateAware.getCurrentState();
                final WorkflowTypes workflowTypes = inboxRenderServiceDeligate.getWorkflowType(stateAware.getStateType());
                final Position position = inboxRenderServiceDeligate.getStateUserPosition(state);
                final User user = inboxRenderServiceDeligate.getStateUser(state, position);
                taskList.put(workflowTypes.getType(), workflowTypes.getDisplayName());
                senderList.put(position.getId(), position.getName());
                inboxItem.append("{Id:'")
                        .append(InboxRenderService.GROUP_Y.equals(workflowTypes.getGroupYN()) ? EMPTY : state.getId())
                        .append("#").append(workflowTypes.getId()).append("',");
                inboxItem.append("Date:'").append(getFormattedDate(state.getCreatedDate().toDate(), "dd/MM/yyyy hh:mm a"))
                        .append("',");
                inboxItem.append("Sender:'").append(inboxRenderServiceDeligate.prettyPrintSenderName(position, user)).append("',");
                inboxItem.append("Task:'").append(workflowTypes.getDisplayName()).append("',");
                final String nextAction = inboxRenderServiceDeligate.getNextAction(state);
                inboxItem.append("Status:'").append(state.getValue())
                        .append(EMPTY.equals(nextAction) ? EMPTY : " - " + nextAction).append("',");
                inboxItem
                        .append("Details:'")
                        .append(stateAware.getStateDetails() == null ? EMPTY : escapeSpecialChars(stateAware
                                .getStateDetails())).append("',");
                inboxItem.append("Link:'").append(workflowTypes.getLink().replace(":ID", stateAware.myLinkId()))
                        .append("'},");
            }
            inboxItem.deleteCharAt(inboxItem.length() - 1);
            inboxItem.append("]");
        }
        return inboxItem;
    }

    private Comparator<? super StateAware> byCreatedDate() {
        return (stateAware_1, stateAware_2) -> {
            int returnVal = 1;
            if (stateAware_1 == null)
                returnVal = stateAware_2 == null ? 0 : -1;
            else if (stateAware_2 == null)
                returnVal = 1;
            else {
                final Date first_date = stateAware_1.getState().getCreatedDate().toDate();
                final Date second_date = stateAware_2.getState().getCreatedDate().toDate();
                if (first_date.after(second_date))
                    returnVal = -1;
                else if (first_date.equals(second_date))
                    returnVal = 0;
            }
            return returnVal;
        };
    }

    private StringBuilder loadInboxHistoryData(final State states) throws EGOVRuntimeException {
        final StringBuilder inboxHistoryItem = new StringBuilder("");
        if (states != null) {
            final List<StateHistory> stateHistories = states.getHistory();
            inboxHistoryItem.append("[");
            for (final StateHistory stateHistory : stateHistories) {
                final Position position = stateHistory.getOwnerPosition();
                final User user = inboxRenderServiceDeligate.getStateUser(stateHistory.getState(), position);
                final WorkflowTypes workflowTypes = inboxRenderServiceDeligate.getWorkflowType(stateHistory.getState().getType());
                inboxHistoryItem.append("{Id:'").append(stateHistory.getState().getId()).append("',");
                inboxHistoryItem.append("Date:'")
                        .append(getFormattedDate(stateHistory.getCreatedDate(), "dd/MM/yyyy hh:mm a")).append("',");
                inboxHistoryItem.append("Sender:'").append(inboxRenderServiceDeligate.prettyPrintSenderName(position, user))
                        .append("',");
                inboxHistoryItem.append("Task:'").append(workflowTypes.getDisplayName()).append("',");
                final String nextAction = inboxRenderServiceDeligate.getNextAction(stateHistory.getState());
                inboxHistoryItem.append("Status:'").append(stateHistory.getValue())
                        .append(EMPTY.equals(nextAction) ? EMPTY : "~" + nextAction).append("',");
                inboxHistoryItem
                        .append("Details:'")
                        .append(stateHistory.getComments() == null ? EMPTY : escapeSpecialChars(stateHistory
                                .getComments())).append("',");
                inboxHistoryItem
                        .append("Signature:'")
                        .append("<img src=\"/egi/common/imageRenderer!getUserSignature.action?id=")
                        .append(user != null ? user.getId() : "")
                        .append("\" height=\"50\" width=\"150\" alt=\"No User Signature\" onerror=\"this.parentNode.removeChild(this);\"/>")
                        .append("',");
                inboxHistoryItem.append("Link:''},");
            }
            inboxHistoryItem.deleteCharAt(inboxHistoryItem.length() - 1);
            inboxHistoryItem.append("]");
        }
        return inboxHistoryItem;
    }

    private Long getLoginUserId() {
        return Long.valueOf(EGOVThreadLocals.getUserId());
    }

    private void writeToAjaxResponse(final String response) throws EGOVRuntimeException, IOException {
        final HttpServletResponse httpResponse = ServletActionContext.getResponse();
        httpResponse.getWriter().write(response);
    }
}
