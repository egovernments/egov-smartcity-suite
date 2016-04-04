/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.tl.entity;

import java.util.Date;

import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infstr.models.BaseModel;
import org.hibernate.validator.constraints.Length;

/**
 * The Class Schedule.
 */
public class Schedule extends BaseModel {
    private static final long serialVersionUID = 1L;
    private Long id;
    @Required(message = "masters.schedule.schedulecode.null")
    @Length(max = 16, message = "masters.schedule.schedulecode.length")
    // @OptionalPattern(regex = ValidatorConstants.alphaNumericwithSpace, message = "tradelicense.error.schedulecode.text")
    private String scheduleCode;
    @Required(message = "tradelicense.error.master.schedulename")
    @Length(max = 256, message = "masters.schedule.schedulename.length")
    //@OptionalPattern(regex = ValidatorConstants.alphaNumericwithSpace, message = "tradelicense.error.schedulename.text")
    private String schedulename;
    //TODO -- change the attribute values according to the requirement 
    @ValidateDate(message = "invalid.fieldvalue.model.orderDate", allowPast = false, dateFormat = "")
    private Date orderDate;

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getScheduleCode() {
        return scheduleCode;
    }

    public void setScheduleCode(final String scheduleCode) {
        this.scheduleCode = scheduleCode;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(final Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getSchedulename() {
        return schedulename;
    }

    public void setSchedulename(final String schedulename) {
        this.schedulename = schedulename;
    }
}
