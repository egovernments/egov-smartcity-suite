/*
 * @(#)ActionDAO.java 3.0, 14 Jun, 2013 5:38:45 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.dao;

import java.util.List;

import org.egov.infstr.dao.GenericDAO;
import org.egov.lib.rrbac.model.Action;

public interface ActionDAO extends GenericDAO {

	List<Action> getActionWithRoles();

	List<Action> getActionWithRG();

	Action findActionByName(String name);

	Action findActionByURL(String contextPath, String url);

}
