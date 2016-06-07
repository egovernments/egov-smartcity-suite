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

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.workflow.entity.State.StateStatus;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.FetchMode;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.egov.infra.utils.DateUtils.constructDateRange;

/**
 * Every module which is having StateAware should initialize this with their own
 * StateAware persistence service<br/>
 * eg:
 * 
 * <pre>
 *      &lt;bean id="myStateAwarePersistenceService" parent="persistenceService"&gt;
 *                 &lt;property name="type" value="org.egov.infra.web.struts.actions.common.MyStateAware" /&gt;
 *         &lt;/bean>
 *         
 *         &lt;bean id="MyStateAwareInboxRenderService" class="org.egov.infra.workflow.inbox.DefaultInboxRenderServiceImpl"&gt;
 *                 &lt;constructor-arg index="0" ref="myStateAwarePersistenceService"/&gt;
 *         &lt;/bean&gt;
 * </pre>
 * 
 * <br/>
 * id or name attribute value of the workflowTypeService bean definition should
 * follow a strict naming convention as follows<br/>
 * <code>
 * <YourStateAwareClassName>InboxRenderService
 * </code> This is how, {@link InboxRenderServiceDeligate} will detect the
 * appropriate {@link InboxRenderService} and render the inbox items.
 **/
@SuppressWarnings("all")
public class DefaultInboxRenderServiceImpl<T extends StateAware> implements InboxRenderService<T> {

    private final Class<T> stateAwareType;
    private final PersistenceService<T, Long> stateAwarePersistenceService;

    public DefaultInboxRenderServiceImpl(final PersistenceService<T, Long> stateAwarePersistenceService) {
        this.stateAwarePersistenceService = stateAwarePersistenceService;
        this.stateAwareType = stateAwarePersistenceService.getType();
    }

    @Override
    public List<T> getAssignedWorkflowItems(final Long userId, final List<Long> owners) {
        return this.stateAwarePersistenceService.getSession().createCriteria(this.stateAwareType)
                .setFetchMode("state", FetchMode.JOIN).createAlias("state", "state")
                .setFlushMode(FlushMode.MANUAL).setReadOnly(true).setCacheable(true)
                .add(Restrictions.eq("state.type", this.stateAwareType.getSimpleName()))
                .add(Restrictions.in("state.ownerPosition.id", owners))
                .add(Restrictions.ne("state.status", StateStatus.ENDED))
                .add(Restrictions.not(Restrictions.conjunction().add(Restrictions.eq("state.status", StateStatus.STARTED))
                        .add(Restrictions.eq("createdBy.id", userId)))).addOrder(Order.desc("state.createdDate"))
                        .list();
    }

    @Override
    public List<T> getDraftWorkflowItems(final Long userId, final List<Long> owners) {
        return this.stateAwarePersistenceService.getSession().createCriteria(this.stateAwareType)
                .setFetchMode("state", FetchMode.JOIN).createAlias("state", "state")
                .setFlushMode(FlushMode.MANUAL).setReadOnly(true).setCacheable(true)
                .add(Restrictions.eq("state.type", this.stateAwareType.getSimpleName()))
                .add(Restrictions.in("state.ownerPosition.id", owners)).add(Restrictions.eq("createdBy.id", userId))
                .add(Restrictions.eq("state.status", StateStatus.STARTED)).addOrder(Order.asc("state.createdDate"))
                .list();
    }

    @Override
    public List<T> getFilteredWorkflowItems(final Long owner, final Long userId, final Long sender, final Date fromDate,
            final Date toDate) {

        // FIXME this wont work with current design need to change
        this.stateAwarePersistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        final StringBuilder query = new StringBuilder("from ");
        query.append(this.stateAwareType.getName()).append(" WF where WF.state.type=:wfType and WF.state.ownerPosition =:owner ")
        .append(sender == 0 ? "" : "and WF.state.senderName=:sender ").append(" and WF.state.createdDate ");
        query.append(fromDate == null && toDate == null ? "IS NOT NULL " : " >= :fromDate and WF.state.createdDate <:toDate ");
        query.append("  and WF.state.value !=:end and not (WF.state.value =:newState and WF.createdBy =:userId) order by WF.state.createdDate DESC");
        final Query qry = this.stateAwarePersistenceService.getSession().createQuery(query.toString());
        qry.setLong(OWNER, owner);
        qry.setString(WFTYPE, this.stateAwareType.getSimpleName());
        if (sender != 0)
            qry.setLong(SENDER, sender);
        Date[] dates = null;
        final boolean isFrmDtNtNull = fromDate != null;
        final boolean isToDtNtNull = toDate != null;
        if (isFrmDtNtNull && isToDtNtNull)
            dates = constructDateRange(fromDate, toDate);
        else if (isFrmDtNtNull)
            dates = constructDateRange(fromDate, fromDate);
        else if (isToDtNtNull)
            dates = constructDateRange(toDate, toDate);
        if (isFrmDtNtNull || isToDtNtNull) {
            qry.setDate(FROMDATE, dates[0]);
            qry.setDate(TODATE, dates[1]);
        }
        qry.setParameter("end", StateStatus.ENDED);
        qry.setParameter("newState", StateStatus.STARTED);
        qry.setLong("userId", userId);
        qry.setReadOnly(true);
        return qry.list();
    }

    @Override
    public List<T> getWorkflowItems(final Map<String, Object> criteria) {
        // FIXME won't work with current
        this.stateAwarePersistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        final StringBuilder queryString = new StringBuilder("from ");
        queryString.append(this.stateAwareType.getName()).append(
                " WF where WF.state.type =:wfType and WF.state.status !=:end and WF.state.status !=:new ");
        if (criteria.containsKey(OWNER))
            queryString.append("and WF.state.owner in (:owner) ");
        if (criteria.containsKey(SENDER))
            queryString.append("and WF.state.previous.owner in (:sender) ");
        if (criteria.containsKey(WFSTATE))
            queryString.append("and WF.state.value =:wfState ");

        if (criteria.containsKey(IDENTIFIER))
            queryString
            .append("and WF.")
            .append(criteria.get("searchField"))
            .append(getOperator((String) criteria.get(SEARCH_OP)))
            .append(((String) criteria.get(SEARCH_OP)).equals("between") ? " :identifier and :identifier2 " : criteria
                            .get(SEARCH_OP).toString().equals("in") ? " (:identifier) " : " :identifier ");

        if (criteria.containsKey(FROMDATE) || criteria.containsKey(TODATE))
            queryString.append("and WF.state.createdDate >= :fromDate and WF.state.createdDate < :toDate ");
        else
            queryString.append("and WF.state.createdDate IS NOT NULL ");

        Date[] dates = null;
        if (criteria.containsKey(FROMDATE) && criteria.containsKey(TODATE))
            dates = constructDateRange((Date) criteria.get(FROMDATE), (Date) criteria.get(TODATE));
        else if (criteria.containsKey(FROMDATE))
            dates = constructDateRange((Date) criteria.get(FROMDATE), (Date) criteria.get(FROMDATE));
        else if (criteria.containsKey(TODATE))
            dates = constructDateRange((Date) criteria.get(TODATE), (Date) criteria.get(TODATE));
        queryString.append("order by WF.state.createdDate DESC");
        final Query query = this.stateAwarePersistenceService.getSession().createQuery(queryString.toString());
        query.setString(WFTYPE, criteria.get(WFTYPE).toString());
        query.setParameter("end", StateStatus.ENDED);
        query.setParameter("new", StateStatus.STARTED);
        if (criteria.containsKey(OWNER))
            query.setParameterList(OWNER, (List) criteria.get(OWNER));
        if (criteria.containsKey(SENDER))
            query.setParameterList(SENDER, (List) criteria.get(SENDER));
        if (criteria.containsKey(WFSTATE))
            query.setString(WFSTATE, criteria.get(WFSTATE).toString());

        if (criteria.containsKey(IDENTIFIER))
            if (criteria.get(SEARCH_OP).toString().equals("equals"))
                query.setString(IDENTIFIER, criteria.get(IDENTIFIER).toString());
            else if (criteria.get(SEARCH_OP).toString().equals("contains"))
                query.setString(IDENTIFIER, "%" + criteria.get(IDENTIFIER) + "%");
            else if (criteria.get(SEARCH_OP).toString().equals("startsWith"))
                query.setString(IDENTIFIER, criteria.get(IDENTIFIER) + "%");
            else if (criteria.get(SEARCH_OP).toString().equals("endsWith"))
                query.setString(IDENTIFIER, "%" + criteria.get(IDENTIFIER));
            else if (criteria.get(SEARCH_OP).toString().equals("between")) {
                final String[] betweenVal = criteria.get(IDENTIFIER).toString().split("and");
                try {
                    if (this.stateAwareType.getDeclaredField(criteria.get("searchField").toString()).getType() == Date.class) {
                        final Date[] dateAry = constructDateRange(betweenVal[0].trim(), betweenVal[1].trim());
                        query.setDate(IDENTIFIER, dateAry[0]);
                        query.setDate("identifier2", dateAry[1]);
                    } else {
                        query.setString(IDENTIFIER, betweenVal[0]);
                        query.setString("identifier2", betweenVal[1]);
                    }
                } catch (final ParseException e) {
                    throw new ApplicationRuntimeException("Invalid identifier date range entered", e);
                } catch (final NoSuchFieldException e) {
                    throw new ApplicationRuntimeException("Invalid identifier rage entered", e);
                }

            }

        if (criteria.containsKey(FROMDATE) || criteria.containsKey(TODATE)) {
            query.setDate(FROMDATE, dates[0]);
            query.setDate(TODATE, dates[1]);
        }
        query.setReadOnly(true);
        return query.list();
    }

    @Override
    public List<T> getWorkflowItems(final String myLinkId) {
        final StringBuilder queryStr = new StringBuilder(" FROM ");
        queryStr.append(this.stateAwareType.getName()).append(" WHERE id = :id ");
        final Query query = this.stateAwarePersistenceService.getSession().createQuery(queryStr.toString());
        query.setLong("id", Long.valueOf(myLinkId));
        return query.list();
    }

    public Class<?> getWorkflowType() {
        return this.stateAwareType;
    }

    /**
     * To covert the Search annotation operator to DB specific operator
     *
     * @param searchOpVal
     *            the search operator value
     * @return DB specific operator
     **/
    private String getOperator(final String searchOpVal) {
        String operator = null;
        if ("equals".equals(searchOpVal))
            operator = " = ";
        else if ("contains".equals(searchOpVal) || "startsWith".equals(searchOpVal) || "endsWith".equals(searchOpVal))
            operator = " like ";
        else if ("between".equals(searchOpVal))
            operator = " between ";

        return operator;
    }
}
