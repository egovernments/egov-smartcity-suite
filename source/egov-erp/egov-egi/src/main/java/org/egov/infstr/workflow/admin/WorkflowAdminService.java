/*
 * @(#)WorkflowAdminService.java 3.0, 17 Jun, 2013 4:45:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved.
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.workflow.admin;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.workflow.entity.State.StateStatus;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.egov.infra.workflow.inbox.InboxRenderServiceDeligate;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;

@SuppressWarnings("all")
public class WorkflowAdminService {

    private transient PersistenceService daoService;
    private transient EISServeable eisService;
    private transient UserService userService;
    private transient InboxRenderServiceDeligate<StateAware> inboxRenderServiceDeligate;
    
    public Set<User> getAllUserByUserName(final String userName) {
        return userService.getUsersByUsernameLike(userName);
    }

    public List<String> getWorkflowStateValues(final String wfType) {
        return daoService
                .findAllBy(
                        "select state.value from org.egov.infstr.models.State as state where state.type=? and state.status != ? and state.status != ? group by state.value",
                        wfType, StateStatus.STARTED, StateStatus.ENDED);
    }

    public List<WorkflowTypes> getWorkflowTypes(final String name) {
        return daoService.findAllByNamedQuery(WorkflowTypes.TYPE_LIKE_NAME, name.toLowerCase(Locale.ENGLISH) + "%");
    }

    public String reassignWFItem(final String wfType, final String stateId, final Long newUserId) {
        String status = "ERROR";
        final List<StateAware> stateAwares = inboxRenderServiceDeligate.getWorkflowItems(wfType, stateId);
        if (!stateAwares.isEmpty()) {
            final Position newOwner = eisService.getPrimaryPositionForUser(newUserId, new Date());
            if (stateAwares.get(0).getCurrentState().getOwnerPosition().getId().equals(newOwner.getId()))
                status = "OWNER-SAME";
            else {
                status = "RE-ASSIGNED";
                for (final StateAware stateAware : stateAwares)
                    stateAware.withOwner(newOwner).withComments(status);
                // FIXME persist the stateaware with respective persistence
                // service
            }
        }
        return status;
    }

    public void setEisService(final EISServeable eisService) {
        this.eisService = eisService;
    }

    public void setInboxRenderServiceDeligate(final InboxRenderServiceDeligate<StateAware> inboxRenderServiceDeligate) {
        this.inboxRenderServiceDeligate = inboxRenderServiceDeligate;
    }
    
    public void setPersistenceService(final PersistenceService daoService) {
        this.daoService = daoService;
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

}
