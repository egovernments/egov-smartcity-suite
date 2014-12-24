/*
 *
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.client;
import org.apache.struts.action.ActionForm;
public class GenericForm extends ActionForm
{

	public String id;
	public String name;
	public String fromDate;
	public String toDate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
			return toDate;
	}
	public void setToDate(String toDate) {
			this.toDate = toDate;
	}
}