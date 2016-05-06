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


package org.egov.ptis.domain.entity.property;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infstr.models.BaseModel;

import java.util.Date;

/**
 * <p>
 * This class defines Property Usage i.e A Property is linked to a PropertyUsage
 * indicating its current usage. Property Usage can be Residential,
 * Non-Residential, Industrial etc.
 * </p>
 * 
 * @author Gayathri Joshi
 * @version 2.00
 * @since 2.00
 */
@Unique(id = "id", tableName = "egpt_property_usage_master", fields = { "usageName" }, columnName = { "usg_name" }, enableDfltMsg = true, message = "propertyusage.isunique")
public class PropertyUsage extends BaseModel {

    private String usageName;
    private String usageCode;
    private Integer orderId;
    private Float usagePercentage;
    private Date lastUpdatedTimeStamp;
    private Date fromDate;
    private Date toDate;
    private Integer isEnabled;
    private Boolean isResidential;
    private Boolean isActive;

    /**
     * @return Returns if the given Object is equal to PropertyUsage
     */
    public boolean equals(Object that) {

        if (that == null)
            return false;

        if (this == that)
            return true;

        if (that.getClass() != this.getClass())
            return false;
        final PropertyUsage thatPropUsage = (PropertyUsage) that;

        if (this.getId() != null && thatPropUsage.getId() != null) {
            if (getId().equals(thatPropUsage.getId()))
                return true;
            else
                return false;
        } else if (this.getUsageName() != null && thatPropUsage.getUsageName() != null) {
            if (getUsageName().equals(thatPropUsage.getUsageName()))
                return true;
            else
                return false;
        } else {
            return false;
        }
    }

    /**
     * @return Returns the hashCode
     */
    public int hashCode() {

        int hashCode = 0;
        if (this.getId() != null)
            hashCode += this.getId().hashCode();

        if (this.getUsageName() != null)
            hashCode += this.getUsageName().hashCode();
        return hashCode;
    }

    /**
     * @return Returns the boolean after validating the current object
     */
    public boolean validatePropUsage() {

        if (getUsageName() == null)
            throw new ApplicationRuntimeException(
                    "In PropertyUsage Validate :Attribute 'Usage Name' is not set, Please Check !!");
        if (getUsagePercentage() == null)
            throw new ApplicationRuntimeException(
                    "In PropertyUsage Validate :Attribute 'Usage Percentage / Factor' is not set, Please Check !!");
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append("|").append(usageCode).append("|").append(usagePercentage);
        return sb.toString();
    }

    public String getUsageName() {
        return usageName;
    }

    public void setUsageName(String usageName) {
        this.usageName = usageName;
    }

    public String getUsageCode() {
        return usageCode;
    }

    public void setUsageCode(String usageCode) {
        this.usageCode = usageCode;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Float getUsagePercentage() {
        return usagePercentage;
    }

    public void setUsagePercentage(Float usagePercentage) {
        this.usagePercentage = usagePercentage;
    }

    public Date getLastUpdatedTimeStamp() {
        return lastUpdatedTimeStamp;
    }

    public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
        this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Boolean getIsResidential() {
        return isResidential;
    }

    public void setIsResidential(Boolean isResidential) {
        this.isResidential = isResidential;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
