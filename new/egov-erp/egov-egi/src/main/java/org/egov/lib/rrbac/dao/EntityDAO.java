/*
 * @(#)EntityDAO.java 3.0, 14 Jun, 2013 5:43:59 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.dao;

import org.egov.infstr.dao.GenericDAO;
import org.egov.lib.rrbac.model.Entity;

public interface EntityDAO extends GenericDAO {

	Entity findEntityByName(String name);
}
