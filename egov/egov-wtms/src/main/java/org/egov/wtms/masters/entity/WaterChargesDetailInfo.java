/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.wtms.masters.entity;

import java.io.Serializable;

public class WaterChargesDetailInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 7083364740584612941L;
    private String propertyID;
    private String ulbCode;
    private String consumerCode;

    private String reasonforChangeOfUsage;

    private String waterSource;
    private String connectionType;
    private String propertyType;
    private String category;
    private String usageType;
    private String pipeSize;

    private Long sumpCapacity;
    private Integer numberOfRooms;
    private Integer numberOfPersons;

    public String getPropertyID() {
        return propertyID;
    }

    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public void setPropertyID(final String propertyID) {
        this.propertyID = propertyID;
    }

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(final String ulbCode) {
        this.ulbCode = ulbCode;
    }

    public String getReasonforChangeOfUsage() {
        return reasonforChangeOfUsage;
    }

    public void setReasonforChangeOfUsage(final String reasonforChangeOfUsage) {
        this.reasonforChangeOfUsage = reasonforChangeOfUsage;
    }

    public String getWaterSource() {
        return waterSource;
    }

    public void setWaterSource(final String waterSource) {
        this.waterSource = waterSource;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(final String connectionType) {
        this.connectionType = connectionType;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(final String propertyType) {
        this.propertyType = propertyType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(final String usageType) {
        this.usageType = usageType;
    }

    public String getPipeSize() {
        return pipeSize;
    }

    public void setPipeSize(final String pipeSize) {
        this.pipeSize = pipeSize;
    }

    public Long getSumpCapacity() {
        return sumpCapacity;
    }

    public void setSumpCapacity(final Long sumpCapacity) {
        this.sumpCapacity = sumpCapacity;
    }

    public Integer getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(final Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public Integer getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(final Integer numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }
    
    @Override
    public String toString() {
        return "WaterChargesDetailInfo [propertyID=" + propertyID + ", ulbCode=" + ulbCode + ", waterSource="
                + waterSource + ", connectionType=" + connectionType + ", propertyType=" + propertyType + ", category=" + category
                + ", usageType=" + usageType + ", pipeSize=" + pipeSize + ", reasonforChangeOfUsage=" + reasonforChangeOfUsage
                + ", consumerCode=" + consumerCode + "]";
    }

}