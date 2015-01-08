/*
 * @(#)EgApplDomainDao.java 3.0, 17 Jun, 2013 1:17:37 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.flexfields.dao;

import org.egov.infstr.dao.GenericDAO;
import org.egov.infstr.flexfields.model.EgApplDomain;

public interface EgApplDomainDao extends GenericDAO {
	public EgApplDomain getApplDomainForDomainNameAndAttrName(String domainName, String attrName);
}
