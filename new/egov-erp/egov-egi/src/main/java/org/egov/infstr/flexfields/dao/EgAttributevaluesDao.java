/*
 * @(#)EgAttributevaluesDao.java 3.0, 17 Jun, 2013 1:19:03 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.flexfields.dao;

import java.util.List;

import org.egov.infstr.dao.GenericDAO;
import org.egov.infstr.flexfields.model.EgAttributevalues;

public interface EgAttributevaluesDao extends GenericDAO {
	public List<EgAttributevalues> getAttributeValuesForDomainTxn(String DomainName, String domainTxnId);
}
