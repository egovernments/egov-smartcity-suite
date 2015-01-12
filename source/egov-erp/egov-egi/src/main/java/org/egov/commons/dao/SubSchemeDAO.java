/*
 * @(#)SubSchemeDAO.java 3.0, 14 Jun, 2013 11:04:35 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import org.egov.commons.SubScheme;
import org.egov.infstr.dao.GenericDAO;

public interface SubSchemeDAO extends GenericDAO {
	public SubScheme getSubSchemeById(Integer id);

	public SubScheme getSubSchemeByCode(String code);

}
