/*
 * @(#)EgAttributetypeDao.java 3.0, 17 Jun, 2013 1:18:22 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.flexfields.dao;

import org.egov.infstr.dao.GenericDAO;
import org.egov.infstr.flexfields.model.EgAttributetype;

public interface EgAttributetypeDao extends GenericDAO {
	public EgAttributetype getAttributeTypeByDomainNameAndAttrName(String domainName, String attributeName);
}
