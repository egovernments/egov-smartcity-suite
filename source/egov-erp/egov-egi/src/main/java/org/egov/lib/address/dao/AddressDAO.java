/*
 * @(#)AddressDAO.java 3.0, 7 Jun, 2013 9:00:02 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.address.dao;

import org.egov.lib.address.model.Address;

public interface AddressDAO extends org.egov.infstr.dao.GenericDAO {
	public Address getAddress(Integer Id);
}
