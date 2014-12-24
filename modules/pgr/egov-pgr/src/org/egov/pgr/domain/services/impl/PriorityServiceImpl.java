/*
 * @(#)PriorityServiceImpl.java 3.0, 23 Jul, 2013 3:29:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.services.impl;

import org.egov.pgr.domain.entities.Priority;
import org.egov.pgr.domain.services.PriorityService;
import org.egov.pgr.services.persistence.EntityServiceImpl;

public class PriorityServiceImpl extends EntityServiceImpl<Priority, Long> implements PriorityService {

	public PriorityServiceImpl() {
		super(Priority.class);

	}

}
