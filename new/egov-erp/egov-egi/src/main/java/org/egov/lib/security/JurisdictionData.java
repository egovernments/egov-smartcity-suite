/*
 * @(#)JurisdictionData.java 3.0, 14 Jun, 2013 3:04:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.security;

import java.util.List;

public interface JurisdictionData {
	/**
	 * Returns the list of all Boundary objects, the implementor belongs to.
	 * @return java.util.List
	 */
	public List<? extends org.egov.lib.admbndry.Boundary> getJurisdictionData();

}
