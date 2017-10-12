/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2017  eGovernments Foundation
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

package org.egov.infra.workflow.inbox;

import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.Arrays;
import java.util.List;

import static org.egov.infra.config.core.ApplicationThreadLocals.getUserId;
import static org.egov.infra.workflow.entity.State.StateStatus.INPROGRESS;
import static org.egov.infra.workflow.entity.State.StateStatus.STARTED;
import static org.hibernate.FetchMode.JOIN;
import static org.hibernate.FlushMode.MANUAL;

/**
 * Every module which is having StateAware should initialize this with their own
 * StateAware persistence service<br/>
 * eg:
 * <p>
 * <pre>
 *      &lt;bean id="myStateAwarePersistenceService" parent="persistenceService"&gt;
 *                 &lt;property name="type" value="org.egov.infra.web.struts.actions.common.MyStateAware" /&gt;
 *         &lt;/bean>
 *
 *         &lt;bean id="MyStateAwareInboxRenderService" class="org.egov.infra.workflow.inbox.DefaultInboxRenderServiceImpl"&gt;
 *                 &lt;constructor-arg index="0" ref="myStateAwarePersistenceService"/&gt;
 *         &lt;/bean&gt;
 * </pre>
 * <p>
 * <br/>
 * id or name attribute value of the workflowTypeService bean definition should
 * follow a strict naming convention as follows<br/>
 * <code>
 * <YourStateAwareClassName>InboxRenderService
 * </code> This is how, {@link InboxRenderServiceDelegate} will detect the
 * appropriate {@link InboxRenderService} and render the inbox items.
 **/
@SuppressWarnings("all")
public class DefaultInboxRenderServiceImpl<T extends StateAware> implements InboxRenderService<T> {

    private final Class<T> stateAwareType;
    private final PersistenceService<T, Long> stateAwarePersistenceService;

    public DefaultInboxRenderServiceImpl(PersistenceService<T, Long> stateAwarePersistenceService) {
        this.stateAwarePersistenceService = stateAwarePersistenceService;
        this.stateAwareType = stateAwarePersistenceService.getType();
    }

    @Override
    public List<T> getAssignedWorkflowItems(List<Long> owners) {
        return this.stateAwarePersistenceService.getSession().createCriteria(this.stateAwareType)
                .setFetchMode("state", JOIN).createAlias("state", "state")
                .setFlushMode(MANUAL).setReadOnly(true).setCacheable(true)
                .add(Restrictions.eq("state.type", this.stateAwareType.getSimpleName()))
                .add(Restrictions.in("state.ownerPosition.id", owners))
                .add(Restrictions.in("state.status", Arrays.asList(INPROGRESS, STARTED)))
                .addOrder(Order.desc("state.createdDate"))
                .list();
    }

    @Override
    public List<T> getDraftWorkflowItems(List<Long> owners) {
        return this.stateAwarePersistenceService.getSession().createCriteria(this.stateAwareType)
                .setFetchMode("state", JOIN).createAlias("state", "state")
                .setFlushMode(MANUAL).setReadOnly(true).setCacheable(true)
                .add(Restrictions.eq("state.type", this.stateAwareType.getSimpleName()))
                .add(Restrictions.in("state.ownerPosition.id", owners))
                .add(Restrictions.eq("state.status", STARTED))
                .add(Restrictions.eq("state.createdBy.id", getUserId()))
                .addOrder(Order.asc("state.createdDate"))
                .list();
    }
}
