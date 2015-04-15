/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
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
        final List<StateAware> stateAwares = inboxRenderServiceDeligate.fetchInboxItems(wfType, stateId);
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
