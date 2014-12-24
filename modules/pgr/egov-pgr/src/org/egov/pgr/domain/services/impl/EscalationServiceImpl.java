/*
 * @(#)EscalationServiceImpl.java 3.0, 23 Jul, 2013 3:29:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.services.impl;

import org.egov.lib.rjbac.role.Role;
import org.egov.pgr.domain.entities.ComplaintTypes;
import org.egov.pgr.domain.entities.Escalation;
import org.egov.pgr.domain.services.EscalationService;
import org.egov.pgr.services.persistence.EntityServiceImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class EscalationServiceImpl extends EntityServiceImpl<Escalation, Long> implements EscalationService {

	public EscalationServiceImpl() {
		super(Escalation.class);

	}

	@Override
	public Integer getNoOfDaysForEscByRoleAndCompType(final ComplaintTypes complaintType, final Role role) {

		final Criteria criteria = getSession().createCriteria(Escalation.class, "escalation");
		criteria.add(Restrictions.eq("complaintType", complaintType));
		criteria.add(Restrictions.eq("role", role));
		final Escalation escalation = (Escalation) criteria.uniqueResult();
		return null != escalation ? escalation.getNoOfDays() : null;
	}

}
