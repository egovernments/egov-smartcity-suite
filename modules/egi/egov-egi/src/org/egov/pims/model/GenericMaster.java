/*
 * @(#)GenericMaster.java 3.0, 7 Jun, 2013 5:07:51 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pims.model;

import java.io.Serializable;
import java.util.Date;

public interface GenericMaster extends Serializable {
	Integer getId();

	void setId(Integer id);

	String getName();

	void setName(String name);

	Date getFromDate();

	void setFromDate(Date fromDate);

	Date getToDate();

	void setToDate(Date toDate);
}
