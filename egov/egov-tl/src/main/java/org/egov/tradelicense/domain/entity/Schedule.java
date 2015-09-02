package org.egov.tradelicense.domain.entity;

import java.util.Date;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.CheckDateFormat;
import org.egov.infstr.models.validator.OptionalPattern;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.models.validator.constants.ValidatorConstants;
import org.hibernate.validator.Length;

/**
 * The Class Schedule.
 */
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
		StringBuilder str = new StringBuilder();
		str.append("Schedule={ ");
		str.append("serialVersionUID=").append(serialVersionUID);
		str.append("Id=").append(id);
		str.append("scheduleCode=").append(scheduleCode == null ? "null" : scheduleCode.toString());
		str.append("orderDate=").append(orderDate == null ? "null" : orderDate.toString());
		str.append("schedulename=").append(schedulename == null ? "null" : schedulename.toString());
		str.append("}");
		return str.toString();
	}
	
	public Schedule() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getScheduleCode() {
		return this.scheduleCode;
	}

	public void setScheduleCode(String scheduleCode) {
		this.scheduleCode = scheduleCode;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getSchedulename() {
		return schedulename;
	}

	public void setSchedulename(String schedulename) {
		this.schedulename = schedulename;
	}
}