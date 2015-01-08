/*
 * @(#)DefaultWorkflowTypeService.java 3.0, 17 Jun, 2013 4:43:21 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.workflow.inbox;

import static org.egov.infstr.utils.DateUtils.constructDateRange;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.models.State;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.FlushMode;
import org.hibernate.Query;

/**
 * The Class DefaultRenderService.
 * @param <T> the generic type
 */
public class DefaultWorkflowTypeService<T extends StateAware> implements WorkflowTypeService<T> {
	
	private Class<?> workflowType;
	private transient final PersistenceService wfDAOService;
	
	/**
	 * Instantiates a new default render service.
	 * @param daoService the dao service
	 */
	public DefaultWorkflowTypeService(final PersistenceService daoService) {
		this.wfDAOService = daoService;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<T> getAssignedWorkflowItems(final Integer owner, final Integer userId, final String order) {
		this.wfDAOService.getSession().setFlushMode(FlushMode.MANUAL);
		final StringBuilder query = new StringBuilder("FROM ");
		query.append(this.workflowType.getName()).append(" WF where WF.state.type=:wfType and WF.state.owner =:owner and WF.state.next is null and WF.state.value !=:end and not (WF.state.value =:new and WF.createdBy =:userId) order by WF.state.createdDate DESC");
		final Query qry = this.wfDAOService.getSession().createQuery(query.toString());
		qry.setInteger(OWNER, owner);
		qry.setString(WFTYPE, this.workflowType.getSimpleName());
		qry.setString("end", State.END);
		qry.setString("new", State.NEW);
		qry.setInteger("userId", userId);
		qry.setReadOnly(true);
		return qry.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<T> getDraftWorkflowItems(final Integer owner, final Integer userId, final String order) {
		this.wfDAOService.getSession().setFlushMode(FlushMode.MANUAL);
		final StringBuilder query = new StringBuilder("FROM ");
		query.append(this.workflowType.getName()).append(" WF where WF.state.type=:wfType and WF.state.owner =:owner and WF.createdBy =:userId and WF.state.value =:new and WF.state.next is null");
		final Query qry = this.wfDAOService.getSession().createQuery(query.toString());
		qry.setInteger(OWNER, owner);
		qry.setString(WFTYPE, this.workflowType.getSimpleName());
		qry.setInteger("userId", userId);
		qry.setString("new", State.NEW);
		qry.setReadOnly(true);
		return qry.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<T> getFilteredWorkflowItems(final Integer owner, final Integer userId, final Integer sender, final Date fromDate, final Date toDate) {
		this.wfDAOService.getSession().setFlushMode(FlushMode.MANUAL);
		final StringBuilder query = new StringBuilder("from ");
		query.append(this.workflowType.getName()).append(" WF where WF.state.type=:wfType and WF.state.owner =:owner ").append((((sender == null) || (sender == 0)) ? "" : "and WF.state.previous.owner=:sender ")).append(" and WF.state.createdDate ");
		query.append((((fromDate == null) && (toDate == null)) ? "IS NOT NULL " : " >= :fromDate and WF.state.createdDate <:toDate "));
		query.append(" and WF.state.next is null and WF.state.value !=:end and not (WF.state.value =:newState and WF.createdBy =:userId) order by WF.state.createdDate DESC");
		final Query qry = this.wfDAOService.getSession().createQuery(query.toString());
		qry.setInteger(OWNER, owner);
		qry.setString(WFTYPE, this.workflowType.getSimpleName());
		if ((sender != null) && (sender != 0)) {
			qry.setInteger(SENDER, sender);
		}
		Date[] dates = null;
		final boolean isFrmDtNtNull = fromDate != null;
		final boolean isToDtNtNull = toDate != null;
		if (isFrmDtNtNull && isToDtNtNull) {
			dates = constructDateRange(fromDate, toDate);
		} else if (isFrmDtNtNull) {
			dates = constructDateRange(fromDate, fromDate);
		} else if (isToDtNtNull) {
			dates = constructDateRange(toDate, toDate);
		}
		if ((isFrmDtNtNull || isToDtNtNull)) {
			qry.setDate(FROMDATE, dates[0]);
			qry.setDate(TODATE, dates[1]);
		}
		qry.setString("end", State.END);
		qry.setString("newState", State.NEW);
		qry.setInteger("userId", userId);
		qry.setReadOnly(true);
		return qry.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<T> getWorkflowItems(final Map<String, Object> criteria) {
		this.wfDAOService.getSession().setFlushMode(FlushMode.MANUAL);
		final StringBuilder queryString = new StringBuilder("from ");
		queryString.append(this.workflowType.getName()).append(" WF where WF.state.type =:wfType and WF.state.value !=:end and WF.state.value !=:new ");
		if (criteria.containsKey(OWNER)) {
			queryString.append("and WF.state.owner in (:owner) ");
		}
		if (criteria.containsKey(SENDER)) {
			queryString.append("and WF.state.previous.owner in (:sender) ");
		}
		if (criteria.containsKey(WFSTATE)) {
			queryString.append("and WF.state.value =:wfState ");
		}
		
		if (criteria.containsKey(IDENTIFIER)){
			queryString.append("and WF.").append(criteria.get("searchField")).append(getOperator((String)criteria.get(SEARCH_OP))).append(((String)criteria.get(SEARCH_OP)).equals("between") ? " :identifier and :identifier2 " : criteria.get(SEARCH_OP).toString().equals("in") ? " (:identifier) ": " :identifier ");
		}
		
		if (criteria.containsKey(FROMDATE) || criteria.containsKey(TODATE)) {
			queryString.append("and WF.state.createdDate >= :fromDate and WF.state.createdDate < :toDate ");
		} else {
			queryString.append("and WF.state.createdDate IS NOT NULL ");
		}
		
		
		Date[] dates = null;
		if (criteria.containsKey(FROMDATE) && criteria.containsKey(TODATE)) {
			dates = constructDateRange((Date) criteria.get(FROMDATE), (Date) criteria.get(TODATE));
		} else if (criteria.containsKey(FROMDATE)) {
			dates = constructDateRange((Date) criteria.get(FROMDATE), (Date) criteria.get(FROMDATE));
		} else if (criteria.containsKey(TODATE)) {
			dates = constructDateRange((Date) criteria.get(TODATE), (Date) criteria.get(TODATE));
		}
		queryString.append("order by WF.state.createdDate DESC");
		final Query query = this.wfDAOService.getSession().createQuery(queryString.toString());
		query.setString(WFTYPE, criteria.get(WFTYPE).toString());
		query.setString("end", State.END);
		query.setString("new", State.NEW);
		if (criteria.containsKey(OWNER)) {
			query.setParameterList(OWNER, (List) criteria.get(OWNER));
		}
		if (criteria.containsKey(SENDER)) {
			query.setParameterList(SENDER, (List) criteria.get(SENDER));
		}
		if (criteria.containsKey(WFSTATE)) {
			query.setString(WFSTATE, criteria.get(WFSTATE).toString());
		}
		
		if (criteria.containsKey(IDENTIFIER)){
			if (criteria.get(SEARCH_OP).toString().equals("equals")) {
				query.setString(IDENTIFIER,criteria.get(IDENTIFIER).toString());
			} else if (criteria.get(SEARCH_OP).toString().equals("contains")) {
				query.setString(IDENTIFIER,"%"+criteria.get(IDENTIFIER)+"%");
			} else if (criteria.get(SEARCH_OP).toString().equals("startsWith")) {
				query.setString(IDENTIFIER,criteria.get(IDENTIFIER)+"%");
			} else if (criteria.get(SEARCH_OP).toString().equals("endsWith")) {
				query.setString(IDENTIFIER,"%"+criteria.get(IDENTIFIER));
			} else if ( criteria.get(SEARCH_OP).toString().equals("between")) {
				final String [] betweenVal = criteria.get(IDENTIFIER).toString().split("and");
				try { 
					if (this.workflowType.getDeclaredField(criteria.get("searchField").toString()).getType() == Date.class) {
						final Date [] dateAry = constructDateRange(betweenVal[0].trim(), betweenVal[1].trim()); 
						query.setDate(IDENTIFIER, dateAry[0]);
						query.setDate("identifier2", dateAry[1]);
					} else {
						query.setString(IDENTIFIER, betweenVal[0]);
						query.setString("identifier2", betweenVal[1]);
					}
				} catch (ParseException e) {
					throw new EGOVRuntimeException("Invalid identifier date range entered",e);
				} catch (NoSuchFieldException e) {
					throw new EGOVRuntimeException("Invalid identifier rage entered",e);
				}
				
			} 
			 
		}
		
		if (criteria.containsKey(FROMDATE) || criteria.containsKey(TODATE)) {
			query.setDate(FROMDATE, dates[0]);
			query.setDate(TODATE, dates[1]);
		}
		query.setReadOnly(true);
		return query.list();
	}
	
	/**
	 * Gets all StateAware items.
	 * @param myLinkId the StateAware myLinkId property
	 * @return all StateAware
	 */
	public List<T> getWorkflowItems(final String myLinkId) {
		final StringBuilder queryStr = new StringBuilder(" FROM ");
		queryStr.append(this.workflowType.getName()).append(" WHERE id = :id ");
		final Query query = this.wfDAOService.getSession().createQuery(queryStr.toString());
		query.setLong("id", Long.valueOf(myLinkId));
		return query.list();
	}
	
	/**
	 * To get the Workflow Type associated with the RenderService
	 * @return workflowType Workflow Type
	 */
	public Class<?> getWorkflowType() {
		return this.workflowType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setWorkflowType(final Class<?> type) {
		this.workflowType = type;
	}
	
	/**
	 * To covert the Search annotation operator to DB specific operator 
	 * @param searchOpVal the search operator value
	 * @return DB specific operator
	 **/
	private String getOperator(final String searchOpVal) {
		String operator = null;
		if ("equals".equals(searchOpVal)) {
			operator = " = ";
		} else if ("contains".equals(searchOpVal) || "startsWith".equals(searchOpVal) || "endsWith".equals(searchOpVal)) {
			operator = " like ";
		} else if ("between".equals(searchOpVal)) {
			operator = " between ";
		} 
		
		return operator;
	}
}
