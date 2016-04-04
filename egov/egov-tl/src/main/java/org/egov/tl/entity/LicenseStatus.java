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

import org.egov.infra.exception.ApplicationRuntimeException;

public class LicenseStatus implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 22395010799520683L;

    private Integer ID = null;

    private String name = null;

    private Date lastUpdatedTimeStamp = null;

    private String statusCode = null;
    private boolean active;
    private Integer orderId;

    /**
     * @return Returns the iD.
     */
    public Integer getID() {
        return ID;
    }

    /**
     * @param id The iD to set.
     */
    public void setID(final Integer id) {
        ID = id;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return Returns the lastUpdatedTimeStamp.
     */
    public Date getLastUpdatedTimeStamp() {
        return lastUpdatedTimeStamp;
    }

    /**
     * @param lastUpdatedTimeStamp The lastUpdatedTimeStamp to set.
     */
    public void setLastUpdatedTimeStamp(final Date lastUpdatedTimeStamp) {
        this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
    }

    /**
     * @return the statusCode
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(final String statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(final boolean active) {
        this.active = active;
    }

    /**
     * @return the orderId
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(final Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * @return Returns if the given Object is equal to PropertyStatus
     */
    @Override
    public boolean equals(final Object that)
    {
        if (that == null)
            return false;

        if (this == that)
            return true;

        if (that.getClass() != this.getClass())
            return false;
        final LicenseStatus thatPropStatus = (LicenseStatus) that;

        if (getID() != null && thatPropStatus.getID() != null)
        {
            if (getID().equals(thatPropStatus.getID()))
                return true;
            else
                return false;
        }
        else if (getName() != null && thatPropStatus.getName() != null)
        {
            if (getName().equals(thatPropStatus.getName()))
                return true;
            else
                return false;
        }
        else
            return false;
    }

    /**
     * @return Returns the hashCode
     */
    @Override
    public int hashCode()
    {
        int hashCode = 0;
        if (getID() != null)
            hashCode += getID().hashCode();
        if (getName() != null)
            hashCode += getName().hashCode();
        return hashCode;
    }

    /**
     * @return Returns the boolean after validating the current object
     */
    public boolean validate()
    {
        if (getName() == null)
            throw new ApplicationRuntimeException("In LicenseStatus Validate : 'Status Name' Attribute is Not Set, Please Check !!");
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("LicenseStatus={");
        str.append("ID=").append(ID);
        str.append("name=").append(name == null ? "null" : name.toString());
        str.append("lastUpdatedTimeStamp=").append(lastUpdatedTimeStamp == null ? "null" : lastUpdatedTimeStamp.toString());
        str.append("statusCode=").append(statusCode == null ? "null" : statusCode.toString());
        str.append("active=").append(active);
        str.append("orderId=").append(orderId == null ? "null" : orderId.toString());
        str.append("}");
        return str.toString();
    }

}
