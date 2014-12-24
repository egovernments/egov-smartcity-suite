/*
 * @(#)TaskDAO.java 3.0, 15 Jun, 2013 11:28:00 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.dao;

import org.egov.infstr.dao.GenericDAO;
import org.egov.lib.rrbac.model.Task;

public interface TaskDAO extends GenericDAO {

	Task findTaskByName(String name);
}
