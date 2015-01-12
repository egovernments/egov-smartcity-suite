/*
 * @(#)RulesDAO.java 3.0, 15 Jun, 2013 11:23:09 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.dao;

import org.egov.infstr.dao.GenericDAO;
import org.egov.lib.rrbac.model.Rules;

public interface RulesDAO extends GenericDAO {

	Rules findRulesByName(String name);
}
