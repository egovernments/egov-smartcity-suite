/*
 * @(#)RuleGroupDAO.java 3.0, 14 Jun, 2013 5:47:19 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.dao;

import org.egov.infstr.dao.GenericDAO;
import org.egov.lib.rrbac.model.RuleGroup;

public interface RuleGroupDAO extends GenericDAO {

	RuleGroup findRuleGroupByName(String name);

}
