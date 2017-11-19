/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.infra.workflow.inbox;

import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.microservice.utils.MicroserviceUtils;
import org.egov.infra.web.support.ui.Inbox;
import org.egov.infra.workflow.entity.OwnerGroup;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.entity.WorkflowAction;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.egov.infra.workflow.service.OwnerGroupService;
import org.egov.infra.workflow.service.StateService;
import org.egov.infra.workflow.service.WorkflowActionService;
import org.egov.infra.workflow.service.WorkflowTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.egov.infra.config.core.ApplicationThreadLocals.getUserId;

@Service
@Transactional(readOnly = true)
public class InboxRenderServiceDelegate<T extends StateAware> {
    private static final Logger LOG = LoggerFactory.getLogger(InboxRenderServiceDelegate.class);
    private static final String INBOX_RENDER_SERVICE_SUFFIX = "%sInboxRenderService";
    private static final Map<String, WorkflowTypes> WORKFLOW_TYPE_CACHE = new ConcurrentHashMap<>();

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private StateService stateService;

    @Autowired
    @Qualifier("eisService")
    private OwnerGroupService<? extends OwnerGroup> ownerGroupService;

    @Autowired
    private WorkflowTypeService workflowTypeService;

    @Autowired
    private WorkflowActionService workflowActionService;

    @Autowired
    private MicroserviceUtils microserviceUtils;

    @ReadOnly
    public List<Inbox> getCurrentUserInboxItems() {
        return buildInbox(getAssignedWorkflowItems())
                .parallelStream()
                .filter(item -> !item.isDraft())
                .collect(Collectors.toList());
    }

    @ReadOnly
    public List<Inbox> getCurrentUserDraftItems() {
        return buildInbox(getAssignedWorkflowDrafts());
    }

    @ReadOnly
    public List<Inbox> getWorkflowHistoryItems(Long stateId) {
        List<Inbox> inboxHistoryItems = new LinkedList<>();
        for (StateHistory stateHistory : getStateHistory(stateId)) {
            inboxHistoryItems.add(Inbox
                    .buildHistory(stateHistory, getWorkflowType(stateHistory.getState().getType())));
        }
        return inboxHistoryItems;
    }

    @ReadOnly
    public List<T> getAssignedWorkflowItems() {
        return getAssignedWorkflowItems(false);
    }

    @ReadOnly
    public List<T> getAssignedWorkflowDrafts() {
        return getAssignedWorkflowItems(true);
    }

    @ReadOnly
    public List<StateHistory> getStateHistory(Long stateId) {
        return new LinkedList<>(stateService.getStateById(stateId).getHistory());
    }

    private List<T> getAssignedWorkflowItems(boolean draft) {
        List<T> workflowItems = new ArrayList<>();
        List<Long> owners = currentUserPositionIds();
        if (!owners.isEmpty()) {
            List<String> types = stateService.getAssignedWorkflowTypeNames(owners);
            for (String type : types) {
                Optional<InboxRenderService<T>> inboxRenderService = this.getInboxRenderService(type);
                if (inboxRenderService.isPresent()) {
                    InboxRenderService<T> renderService = inboxRenderService.get();
                    workflowItems.addAll(draft ? renderService.getDraftWorkflowItems(owners) :
                            renderService.getAssignedWorkflowItems(owners));
                }
            }
        }
        return workflowItems;
    }

    private List<Inbox> buildInbox(List<T> items) {
        List<Inbox> inboxItems = new ArrayList<>();
        for (StateAware stateAware : items) {
            inboxItems.add(Inbox
                    .build(stateAware,
                            getWorkflowType(stateAware.getStateType()),
                            getNextAction(stateAware.getState())));
        }
        inboxItems.addAll(microserviceUtils.getInboxItems());
        return inboxItems
                .stream()
                .sorted(comparing(Inbox::getCreatedDate).reversed())
                .collect(toList());
    }

    private Optional<InboxRenderService<T>> getInboxRenderService(String type) {
        InboxRenderService<T> inboxRenderService = null;
        try {
            if (getWorkflowType(type) != null)
                inboxRenderService = applicationContext.getBean(String.format(INBOX_RENDER_SERVICE_SUFFIX, type), InboxRenderService.class);
        } catch (BeansException e) {
            LOG.warn("{}InboxRenderService bean not defined", type, e);
        }
        return Optional.ofNullable(inboxRenderService);
    }

    private String getNextAction(State state) {
        String nextAction = EMPTY;
        if (state.getNextAction() != null) {
            WorkflowAction workflowAction = workflowActionService.getWorkflowActionByNameAndType(state.getNextAction(), state.getType());
            if (workflowAction == null)
                nextAction = state.getNextAction();
            else
                nextAction = workflowAction.getDescription() == null ? state.getNextAction() : workflowAction.getDescription();
        }
        return nextAction;
    }

    private WorkflowTypes getWorkflowType(String type) {
        WorkflowTypes workflowType = WORKFLOW_TYPE_CACHE.get(type);
        if (workflowType == null) {
            workflowType = workflowTypeService.getEnabledWorkflowTypeByType(type);
            if (workflowType != null)
                WORKFLOW_TYPE_CACHE.put(type, workflowType);
        }
        return workflowType;
    }

    private List<Long> currentUserPositionIds() {
        return this.ownerGroupService.getOwnerGroupsByUserId(getUserId())
                .parallelStream()
                .map(OwnerGroup::getId)
                .collect(Collectors.toList());
    }
}