/*
 * @(#)AddressTypeDAO.java 3.0, 7 Jun, 2013 9:05:55 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.address.dao;

import org.egov.lib.address.model.AddressTypeMaster;

public interface AddressTypeDAO extends org.egov.infstr.dao.GenericDAO {
	AddressTypeMaster getAddressType(String addTypeName);
}
