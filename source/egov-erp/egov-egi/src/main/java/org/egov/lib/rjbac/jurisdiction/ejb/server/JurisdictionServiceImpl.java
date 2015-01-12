/*
 * @(#)JurisdictionManagerBean.java 3.0, 16 Jun, 2013 10:11:21 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.jurisdiction.ejb.server;

import org.egov.lib.rjbac.jurisdiction.Jurisdiction;
import org.egov.lib.rjbac.jurisdiction.JurisdictionValues;
import org.egov.lib.rjbac.jurisdiction.dao.JurisdictionDAO;
import org.egov.lib.rjbac.jurisdiction.ejb.api.JurisdictionService;

public class JurisdictionServiceImpl implements JurisdictionService {

	@Override
	public void removeJurisdiction(final Jurisdiction jur) {
		new JurisdictionDAO().removeJurisdiction(jur);
	}

	@Override
	public void updateJurisdiction(final Jurisdiction jur) {
		new JurisdictionDAO().updateJurisdiction(jur);
	}

	@Override
	public void deleteJurisdiction(final Jurisdiction jur) {
		new JurisdictionDAO().deleteJurisdiction(jur);
	}

	@Override
	public void deleteJurisdictionValues(final JurisdictionValues jur) {
		new JurisdictionDAO().deleteJurisdictionValues(jur);
	}
}
