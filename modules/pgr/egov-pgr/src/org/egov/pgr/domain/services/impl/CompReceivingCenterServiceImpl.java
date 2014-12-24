/*
 * @(#)CompReceivingCenterServiceImpl.java 3.0, 23 Jul, 2013 3:29:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.services.impl;

import java.util.List;

import org.egov.pgr.domain.entities.ComplaintReceivingCenter;
import org.egov.pgr.domain.services.CompReceivingCenterService;
import org.egov.pgr.services.persistence.EntityServiceImpl;

public class CompReceivingCenterServiceImpl extends EntityServiceImpl<ComplaintReceivingCenter, Long> implements CompReceivingCenterService {

	public CompReceivingCenterServiceImpl() {
		super(ComplaintReceivingCenter.class);

	}

	@Override
	public List<ComplaintReceivingCenter> getAllComplaintReceivingCenter() {
		return findAllByNamedQuery("getAllCompReceivingCenter");
	}

}
