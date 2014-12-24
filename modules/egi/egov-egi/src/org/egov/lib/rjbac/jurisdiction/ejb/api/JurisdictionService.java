/*
 * @(#)IJurisdictionManager.java 3.0, 16 Jun, 2013 10:09:57 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.jurisdiction.ejb.api;

import org.egov.lib.rjbac.jurisdiction.Jurisdiction;
import org.egov.lib.rjbac.jurisdiction.JurisdictionValues;

public interface JurisdictionService {

	void removeJurisdiction(Jurisdiction jur);

	void updateJurisdiction(Jurisdiction jur);

	void deleteJurisdiction(Jurisdiction jur);

	void deleteJurisdictionValues(JurisdictionValues jur);
}
