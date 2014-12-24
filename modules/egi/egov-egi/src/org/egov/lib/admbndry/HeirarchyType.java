/*
 * @(#)HeirarchyType.java 3.0, 16 Jun, 2013 3:35:59 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry;

import java.io.Serializable;
import java.util.Date;

public interface HeirarchyType extends Serializable {
	Integer getId();

	void setId(Integer id);

	void setName(String name);

	String getName();

	void setCode(String code);

	String getCode();

	Date getUpdatedTime();

	void setUpdatedTime(Date updatedtime);

}
