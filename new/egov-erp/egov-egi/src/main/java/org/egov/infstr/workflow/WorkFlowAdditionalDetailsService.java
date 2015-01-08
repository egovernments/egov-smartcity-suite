/*
 * @(#)WorkFlowAdditionalDetailsService.java 3.0, 17 Jun, 2013 4:35:19 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.workflow;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.infstr.models.WorkflowTypes;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class WorkFlowAdditionalDetailsService extends PersistenceService<WorkFlowAdditionalRule, Long> {

	private PersistenceService persistenceService;
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkFlowAdditionalDetailsService.class);

	public PersistenceService getPersistenceService() {
		return this.persistenceService;
	}

	public void setPersistenceService(final PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public WorkflowTypes getobjectTypebyId(final Long objectTypeId) {

		return (WorkflowTypes) this.persistenceService.find("from org.egov.infstr.models.WorkflowTypes where id=? order by type asc", objectTypeId);
	}

	public List getAllModuleTypeforStatus() {

		return this.persistenceService.findAllBy(" select distinct(moduletype) from EgwStatus order by moduletype asc");
	}

	public List<WorkflowTypes> getobjectTypeList() {

		return this.persistenceService.findAllBy("from org.egov.infstr.models.WorkflowTypes order by type asc");
	}

	public WorkFlowAdditionalRule save(WorkFlowAdditionalRule wfAdditionalRule) {
		LOGGER.info("save Method is called");
		if (wfAdditionalRule.getId() == null) {
			wfAdditionalRule = persist(wfAdditionalRule);
		} else {
			wfAdditionalRule = merge(wfAdditionalRule);
		}
		LOGGER.info("save Method is Ended");
		return wfAdditionalRule;
	}

	public List<WorkFlowAdditionalRule> getAdditionalRulesbyObject(final Long objectType) {
		LOGGER.info("getAdditionalRulesbyObject Method is called");
		final Criteria crit = getSession().createCriteria(WorkFlowAdditionalRule.class);
		crit.add(Restrictions.eq("objecttypeid.id", objectType));
		LOGGER.info("getAdditionalRulesbyObject Method is ended");
		return crit.list();
	}

	public WorkFlowAdditionalRule getObjectbyTypeandRule(final Long objectType, final String additionalRules) {
		LOGGER.info("getObjectbyTypeandRule Method is called");
		final Criteria crit = getSession().createCriteria(WorkFlowAdditionalRule.class);
		crit.add(Restrictions.eq("objecttypeid.id", objectType));
		if (additionalRules == null || additionalRules.equals("-1")) {
			crit.add(Restrictions.isNull("additionalRule"));
		} else {
			crit.add(Restrictions.eq("additionalRule", additionalRules));
		}
		if (crit.list().size() != 0) {
			return (WorkFlowAdditionalRule) crit.list().get(0);
		} else {
			return null;
		}
	}

	public WorkFlowAdditionalRule getObjectbyTypeandRule(final Long ObjectId, final Long objectType, final String additionalRules) {
		LOGGER.info("getObjectbyTypeandRule Method is called");
		final Criteria crit = getSession().createCriteria(WorkFlowAdditionalRule.class);
		crit.add(Restrictions.eq("objecttypeid.id", objectType));
		crit.add(Restrictions.ne("id", ObjectId));
		if (additionalRules == null) {
			crit.add(Restrictions.isNull("additionalRule"));
		} else {
			crit.add(Restrictions.eq("additionalRule", additionalRules));
		}
		if (crit.list().size() != 0) {
			return (WorkFlowAdditionalRule) crit.list().get(0);
		} else {
			return null;
		}

	}
}
