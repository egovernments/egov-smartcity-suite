/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.web.controller;

import com.google.gson.GsonBuilder;
import org.egov.infra.microservice.utils.MicroserviceUtils;
import org.egov.infra.web.support.ui.Inbox;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.egov.infra.workflow.inbox.InboxRenderServiceDeligate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.egov.infra.utils.DateUtils.toDefaultDateTimeFormat;
import static org.egov.infra.utils.StringUtils.escapeSpecialChars;

@Controller
@RequestMapping("/inbox")
public class InboxController {

    @Autowired
    private InboxRenderServiceDeligate<StateAware> inboxRenderServiceDeligate;

    @Autowired
    private MicroserviceUtils microserviceUtils;

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String showInbox() {
        return createInboxData(inboxRenderServiceDeligate.getInboxItems());
    }

    @GetMapping(value = "/draft", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String showInboxDraft() {
        return createInboxData(inboxRenderServiceDeligate.getInboxDraftItems());
    }

    @GetMapping(value = "/history", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String showInboxHistory(@RequestParam Long stateId) {
        return createInboxHistoryData(inboxRenderServiceDeligate.getWorkflowHistory(stateId));
    }

    private String createInboxData(List<StateAware> inboxStates) {
        List<Inbox> inboxItems = new ArrayList<>();
        for (StateAware stateAware : inboxStates) {
            State state = stateAware.getCurrentState();
            WorkflowTypes workflowTypes = inboxRenderServiceDeligate.getWorkflowType(stateAware.getStateType());
            Inbox inboxItem = new Inbox();
            inboxItem.setId(workflowTypes.isGrouped() ? EMPTY : state.getId() + "#" + workflowTypes.getId());
            inboxItem.setDate(toDefaultDateTimeFormat(state.getCreatedDate()));
            inboxItem.setSender(state.getSenderName());
            inboxItem.setTask(isBlank(state.getNatureOfTask()) ? workflowTypes.getDisplayName() : state.getNatureOfTask());
            String nextAction = inboxRenderServiceDeligate.getNextAction(state);
            inboxItem.setStatus(state.getValue() + (isBlank(nextAction) ? EMPTY : " - " + nextAction));
            inboxItem.setDetails(isBlank(stateAware.getStateDetails()) ? EMPTY : stateAware.getStateDetails());
            inboxItem.setLink(workflowTypes.getLink().replace(":ID", stateAware.myLinkId()));
            inboxItem.setCreatedDate(state.getCreatedDate());
            inboxItems.add(inboxItem);
        }
        inboxItems.addAll(microserviceUtils.getInboxItems());
        inboxItems = inboxItems.stream().sorted(comparing(Inbox::getCreatedDate).reversed()).collect(toList());
        return "{ \"data\":" + new GsonBuilder().create().toJson(inboxItems) + "}";
    }

    private String createInboxHistoryData(List<StateHistory> stateHistories) {
        List<Inbox> inboxHistoryItems = new LinkedList<>();
        for (StateHistory stateHistory : stateHistories) {
            WorkflowTypes workflowTypes = inboxRenderServiceDeligate
                    .getWorkflowType(stateHistory.getState().getType());
            Inbox inboxHistoryItem = new Inbox();
            inboxHistoryItem.setId(stateHistory.getState().getId().toString());
            inboxHistoryItem.setDate(toDefaultDateTimeFormat(stateHistory.getLastModifiedDate()));
            inboxHistoryItem.setSender(stateHistory.getSenderName());
            inboxHistoryItem.setTask(isBlank(stateHistory.getNatureOfTask()) ? workflowTypes.getDisplayName()
                    : stateHistory.getNatureOfTask());
            inboxHistoryItem.setStatus(stateHistory.getValue()
                    + (isBlank(stateHistory.getNextAction()) ? EMPTY : "-" + stateHistory.getNextAction()));
            inboxHistoryItem.setDetails(
                    isBlank(stateHistory.getComments()) ? EMPTY : escapeSpecialChars(stateHistory.getComments()));
            inboxHistoryItem.setLink(EMPTY);
            inboxHistoryItems.add(inboxHistoryItem);
        }

        return "{ \"data\":" + new GsonBuilder().disableHtmlEscaping().create().toJson(inboxHistoryItems) + "}";
    }

}
