/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.infra.workflow.inbox;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.egov.infra.workflow.inbox.InboxRenderService.INBOX_RENDER_SERVICE_SUFFIX;
import static org.egov.infra.workflow.inbox.InboxRenderService.RENDER_Y;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.entity.WorkflowAction;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class InboxRenderServiceDeligate<T extends StateAware> {
    private static final Logger LOG = LoggerFactory.getLogger(InboxRenderServiceDeligate.class);

    public static final String UNKNOWN = "Unknown";
    public static final String SLASH_DELIMIT = " / ";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PersistenceService<State, Long> statePersistenceService;

    @Autowired
    private PersistenceService<WorkflowTypes, Long> workflowTypePersistenceService;

    @Qualifier("entityQueryService")
    private @Autowired PersistenceService entityQueryService;

    @Qualifier("eisService")
    private @Autowired EISServeable eisService;

    private static final Map<String, WorkflowTypes> WORKFLOWTYPE_CACHE = new ConcurrentHashMap<>();

    public List<T> getInboxItems(final Long userId) {
        return fetchInboxItems(userId, this.eisService.getPositionsForUser(userId, new Date()).parallelStream()
                .map(position -> position.getId()).collect(Collectors.toList()));
    }

    public List<T> getInboxDraftItems(final Long userId) {
        return fetchInboxDraftItems(userId, this.eisService.getPositionsForUser(userId, new Date()).parallelStream()
                .map(position -> position.getId()).collect(Collectors.toList()));
    }

    public List<StateHistory> getWorkflowHistory(final Long stateId) {
        return new LinkedList<>(this.statePersistenceService.findById(stateId, false).getHistory());
    }

    public List<T> fetchInboxItems(final String wfType, final String myLinkId) {
        final Optional<InboxRenderService<T>> inboxRenderService = this.getWorkflowTypeService(wfType);
        List<T> stateAwares = new ArrayList<T>();
        if (inboxRenderService.isPresent())
            stateAwares = inboxRenderService.get().getWorkflowItems(myLinkId);
        return stateAwares;
    }

    public List<T> fetchInboxItems(final Long userId, final List<Long> owners) {
        final List<T> assignedWFItems = new ArrayList<T>();
        if(!owners.isEmpty()){
        	final List<String> wfTypes = this.getAssignedWorkflowTypes(owners);
            for (final String wfType : wfTypes) {
                final Optional<InboxRenderService<T>> wfTypeService = this.getWorkflowTypeService(wfType);
                if (wfTypeService.isPresent())
                    assignedWFItems.addAll(wfTypeService.get().getAssignedWorkflowItems(userId, owners));
            }
        }
        return assignedWFItems;
    }

    public List<T> fetchInboxDraftItems(final Long userId, final List<Long> owners) {
        final List<T> draftWfItems = new ArrayList<T>();
        if(!owners.isEmpty()){
        	final List<String> wfTypes = this.getAssignedWorkflowTypes(owners);
            for (final String wfType : wfTypes) {
                final Optional<InboxRenderService<T>> wfTypeService = getWorkflowTypeService(wfType);
                if (wfTypeService.isPresent())
                    draftWfItems.addAll(wfTypeService.get().getDraftWorkflowItems(userId, owners));
            }
        }
        return draftWfItems;
    }

    public WorkflowTypes getWorkflowType(final String wfType) {
        WorkflowTypes workflowType = WORKFLOWTYPE_CACHE.get(wfType);
        if (workflowType == null) {
            workflowType = (WorkflowTypes) this.workflowTypePersistenceService.getSession().createCriteria(WorkflowTypes.class)
                    .add(Restrictions.eq("renderYN", RENDER_Y)).add(Restrictions.eq("type", wfType))
                    .setProjection(Projections.projectionList().add(Projections.property("type"), "type")
                            .add(Projections.property("link"), "link").add(Projections.property("displayName"), "displayName")
                            .add(Projections.property("renderYN"), "renderYN").add(Projections.property("groupYN"), "groupYN"))
                    .setReadOnly(true).setResultTransformer(Transformers.aliasToBean(WorkflowTypes.class)).uniqueResult();
            if (workflowType != null)
                WORKFLOWTYPE_CACHE.put(wfType, workflowType);
        }
        return workflowType;
    }

    public List<String> getAssignedWorkflowTypes(final List<Long> owners) {
        return this.entityQueryService.findAllByNamedQuery(State.WORKFLOWTYPES_QRY, owners);
    }

    public Optional<InboxRenderService<T>> getWorkflowTypeService(final String wfType) {
        InboxRenderService<T> workflowTypeService = null;
        try {
            if (getWorkflowType(wfType) != null)
                workflowTypeService = (InboxRenderService<T>) applicationContext
                        .getBean(wfType.concat(INBOX_RENDER_SERVICE_SUFFIX));
        } catch (final BeansException e) {
            LOG.warn("InboxRenderService bean for {} not found, have you defined {}InboxRenderService bean ?", wfType, wfType);
        }
        return Optional.ofNullable(workflowTypeService);
    }

    public List<T> getFilteredInboxItems(final Long owner, final Long userId, final Long sender, final String taskName,
            final Date fromDate, final Date toDate) {
        final List<T> filteredWFItems = new ArrayList<T>();
        List<String> wfTypes = null;

        if (taskName == null || EMPTY.equals(taskName.trim()))
            wfTypes = this.getAssignedWorkflowTypes(Arrays.asList(owner));
        else {
            wfTypes = new ArrayList<String>();
            final WorkflowTypes wfType = this.workflowTypePersistenceService
                    .find("from org.egov.infstr.models.WorkflowTypes  where displayName=?", taskName);
            wfTypes.add(wfType.getType());
        }
        for (final String wfType : wfTypes) {
            final Optional<InboxRenderService<T>> wfTypeService = this.getWorkflowTypeService(wfType);
            if (wfTypeService.isPresent())
                filteredWFItems.addAll(wfTypeService.get().getFilteredWorkflowItems(owner, userId, sender, fromDate, toDate));
        }
        return filteredWFItems;
    }

    /**
     * Returns a list of workflow items that for the given criteria
     *
     * @param criteria
     *            the search criteria #mandatory key in criteria 1] wfType
     *            StateAware Type name #other non mandatory keys in criteria 1]
     *            sender Position list 2] user Position list 3] fromDate 4]
     *            toDate 5] wfState
     * @return the List of workflow items
     */
    public List<T> getWorkflowItems(final Map<String, Object> criteria) {
        return this.getWorkflowTypeService(criteria.get("wfType").toString()).get().getWorkflowItems(criteria);
    }

    public String getNextAction(final State state) {
        String nextAction = EMPTY;
        if (state.getNextAction() != null) {
            final WorkflowAction workflowAction = (WorkflowAction) this.entityQueryService.findByNamedQuery(WorkflowAction.BY_NAME_AND_TYPE,
                    state.getNextAction(), state.getType());
            if (workflowAction == null)
                nextAction = state.getNextAction();
            else
                nextAction = workflowAction.getDescription() == null ? state.getNextAction() : workflowAction.getDescription();
        }
        return nextAction;
    }

}