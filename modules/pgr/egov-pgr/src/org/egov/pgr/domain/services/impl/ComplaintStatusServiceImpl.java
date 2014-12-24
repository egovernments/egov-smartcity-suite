/*
 * @(#)ComplaintStatusServiceImpl.java 3.0, 23 Jul, 2013 3:29:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.pgr.constant.PGRConstants;
import org.egov.pgr.domain.entities.ComplaintStatus;
import org.egov.pgr.domain.services.ComplaintStatusService;
import org.egov.pgr.services.persistence.EntityServiceImpl;

public class ComplaintStatusServiceImpl extends EntityServiceImpl<ComplaintStatus, Long> implements ComplaintStatusService {

	private AppConfigValuesDAO appConfigValuesDao;

	public ComplaintStatusServiceImpl() {
		super(ComplaintStatus.class);

	}

	@Override
	public List<ComplaintStatus> getAllComplaintStatus() {
		return findAllByNamedQuery("getAllComplaintStatus");
	}

	@Override
	public List<ComplaintStatus> getStatusByUserOrCitizen() {

		String keyName = "";
		if (null != EGOVThreadLocals.getUserId()) {
			keyName = "ULB_USER_COMPLAINT_STATUS_ACTION";
		} else {
			keyName = "CITIZEN_COMPLAINT_STATUS_ACTION";
		}
		final List<AppConfigValues> appConfigValueList = this.appConfigValuesDao.getConfigValuesByModuleAndKey(PGRConstants.MODULE_NAME, keyName);
		final Set<String> status = new HashSet<String>();

		for (final AppConfigValues appval : appConfigValueList) {
			status.add(appval.getValue());
		}

		return findAllByNamedQuery("getStatusByListName", status);

	}

	public void setAppConfigValuesDao(AppConfigValuesDAO appConfigValuesDao) {
		this.appConfigValuesDao = appConfigValuesDao;
	}

	

}
