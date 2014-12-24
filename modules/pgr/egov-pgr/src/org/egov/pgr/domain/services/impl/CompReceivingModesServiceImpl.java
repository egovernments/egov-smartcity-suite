/*
 * @(#)CompReceivingModesServiceImpl.java 3.0, 23 Jul, 2013 3:29:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.services.impl;

import java.util.List;

import org.egov.pgr.domain.entities.CompReceivingModes;
import org.egov.pgr.domain.services.CompReceivingModesService;
import org.egov.pgr.services.persistence.EntityServiceImpl;

public class CompReceivingModesServiceImpl extends EntityServiceImpl<CompReceivingModes, Long> implements CompReceivingModesService {

	public CompReceivingModesServiceImpl() {
		super(CompReceivingModes.class);

	}

	@Override
	public List<CompReceivingModes> getAllCompReceivingModes() {
		return findAllByNamedQuery("getAllCompReceivingModes");
	}

}
