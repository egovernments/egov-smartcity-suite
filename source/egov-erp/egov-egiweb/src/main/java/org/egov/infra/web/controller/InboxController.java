package org.egov.infra.web.controller;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.egov.infstr.utils.DateUtils.getFormattedDate;
import static org.egov.infstr.utils.StringUtils.escapeSpecialChars;

import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.web.support.ui.Inbox;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.egov.infra.workflow.inbox.InboxRenderService;
import org.egov.infra.workflow.inbox.InboxRenderServiceDeligate;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/inbox")
public class InboxController {

    @Autowired
    private InboxRenderServiceDeligate<StateAware> inboxRenderServiceDeligate;

    @Autowired
    private SecurityUtils securityUtils;

    @RequestMapping(produces=MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String showInbox() {
        return createInboxData(inboxRenderServiceDeligate.getInboxItems(securityUtils.getCurrentUser().getId()));
    }

    @RequestMapping(value="/draft",produces=MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String showInboxDraft() {
        return createInboxData(inboxRenderServiceDeligate.getInboxDraftItems(securityUtils.getCurrentUser().getId()));
    }

    @RequestMapping(value="/history",produces=MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String showInboxHistory(@RequestParam final Long stateId) {
        return createInboxHistoryData(inboxRenderServiceDeligate.getWorkflowHistory(stateId));
    }

    private String createInboxData(final List<StateAware> inboxStates) {
        final List<Inbox> inboxItems = new LinkedList<>();
        inboxStates.sort(byCreatedDate());
        for (final StateAware stateAware : inboxStates) {
            final State state = stateAware.getCurrentState();
            final WorkflowTypes workflowTypes = inboxRenderServiceDeligate.getWorkflowType(stateAware.getStateType());
            final Position position = inboxRenderServiceDeligate.getStateUserPosition(state);
            final User user = inboxRenderServiceDeligate.getStateUser(state, position);
            final Inbox inboxItem = new Inbox();
            inboxItem.setId(InboxRenderService.GROUP_Y.equals(workflowTypes.getGroupYN()) ? EMPTY : state.getId() + "#" + workflowTypes.getId());
            inboxItem.setDate(getFormattedDate(state.getCreatedDate().toDate(), "dd/MM/yyyy hh:mm a"));
            inboxItem.setSender(inboxRenderServiceDeligate.prettyPrintSenderName(position, user));
            inboxItem.setTask(workflowTypes.getDisplayName());
            final String nextAction = inboxRenderServiceDeligate.getNextAction(state);
            inboxItem.setStatus(state.getValue() + (EMPTY.equals(nextAction) ? EMPTY : " - " + nextAction));
            inboxItem.setDetails(stateAware.getStateDetails() == null ? EMPTY : stateAware.getStateDetails());
            inboxItem.setLink(workflowTypes.getLink().replace(":ID", stateAware.myLinkId()));
            inboxItems.add(inboxItem);
        }
        return "{ \"data\":" + new GsonBuilder().create().toJson(inboxItems) + "}";
    }

    private String createInboxHistoryData(final List<StateHistory> stateHistories) {
        final List<Inbox> inboxHistoryItems = new LinkedList<>();
        for (final StateHistory stateHistory : stateHistories) {
            final Position position = stateHistory.getOwnerPosition();
            final User user = inboxRenderServiceDeligate.getStateUser(stateHistory.getState(), position);
            final WorkflowTypes workflowTypes = inboxRenderServiceDeligate.getWorkflowType(stateHistory.getState()
                    .getType());

            final Inbox inboxHistoryItem = new Inbox();
            inboxHistoryItem.setId(stateHistory.getState().getId().toString());
            inboxHistoryItem.setDate(getFormattedDate(stateHistory.getCreatedDate(), "dd/MM/yyyy hh:mm a"));
            inboxHistoryItem.setSender(inboxRenderServiceDeligate.prettyPrintSenderName(position, user));
            inboxHistoryItem.setTask(workflowTypes.getDisplayName());
            final String nextAction = inboxRenderServiceDeligate.getNextAction(stateHistory.getState());
            inboxHistoryItem.setStatus(stateHistory.getValue() + (EMPTY.equals(nextAction) ? EMPTY : "~" + nextAction));
            inboxHistoryItem.setDetails(stateHistory.getComments() == null ? EMPTY : escapeSpecialChars(stateHistory
                    .getComments()));
            inboxHistoryItem.setLink(EMPTY);
            inboxHistoryItems.add(inboxHistoryItem);
        }

        return "{ \"data\":" + new GsonBuilder().disableHtmlEscaping().create().toJson(inboxHistoryItems) + "}";
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

}
