/*
 * @(#)Schedule.java 3.0, 29 Jul, 2013 1:24:26 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.entity;

import java.util.Date;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.CheckDateFormat;
import org.egov.infstr.models.validator.OptionalPattern;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.models.validator.constants.ValidatorConstants;
import org.hibernate.validator.constraints.Length;

public class Schedule extends BaseModel {
	private static final long serialVersionUID = 1L;
	private Long id;
	@Required(message = "masters.schedule.schedulecode.null")
	@Length(max = 16, message = "masters.schedule.schedulecode.length")
	@OptionalPattern(regex = ValidatorConstants.alphaNumericwithSpace, message = "tradelicense.error.schedulecode.text")
	private String scheduleCode;
	@Required(message = "tradelicense.error.master.schedulename")
	@Length(max = 256, message = "masters.schedule.schedulename.length")
	@OptionalPattern(regex = ValidatorConstants.alphaNumericwithSpace, message = "tradelicense.error.schedulename.text")
	private String schedulename;
	@CheckDateFormat(message = "invalid.fieldvalue.model.orderDate")
	private Date orderDate;

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("Schedule={ ");
		str.append("serialVersionUID=").append(serialVersionUID);
		str.append("Id=").append(this.id);
		str.append("scheduleCode=").append(this.scheduleCode == null ? "null" : this.scheduleCode.toString());
		str.append("orderDate=").append(this.orderDate == null ? "null" : this.orderDate.toString());
		str.append("schedulename=").append(this.schedulename == null ? "null" : this.schedulename.toString());
		str.append("}");
		return str.toString();
	}

	public Schedule() {
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	public String getScheduleCode() {
		return this.scheduleCode;
	}

	public void setScheduleCode(final String scheduleCode) {
		this.scheduleCode = scheduleCode;
	}

	public Date getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(final Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getSchedulename() {
		return this.schedulename;
	}

	public void setSchedulename(final String schedulename) {
		this.schedulename = schedulename;
	}
}