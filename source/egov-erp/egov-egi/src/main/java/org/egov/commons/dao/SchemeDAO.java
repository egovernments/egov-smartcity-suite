/*
 * @(#)SchemeDAO.java 3.0, 14 Jun, 2013 10:59:52 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import org.egov.commons.Scheme;
import org.egov.infstr.dao.GenericDAO;

public interface SchemeDAO extends GenericDAO {
	
	public Scheme getSchemeById(Integer id);

	public Scheme getSchemeByCode(String code);

}
