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
package org.egov.wtms.application.rest;

import java.math.BigDecimal;

public class WaterChargesDetails {
    private String propertyID;
    private BigDecimal totalTaxDue;
    private String consumerCode;
    private String connectionType;
    private String connectionStatus;
    private String waterSupplyType;
    private String usageType;
    private String waterSource;
    private String propertytype;
    private String applicationType;
    private Boolean isPrimaryConnection;
    private String category;
    private String pipesize;
    private Long sumpCapacity;
    private String ulbCode;
    private Integer noOfPerson;

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(final String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public String getPropertyID() {
        return org.apache.commons.lang.StringUtils.defaultIfEmpty(propertyID, "");
    }

    public void setPropertyID(final String propertyID) {
        this.propertyID = propertyID;
    }

    public BigDecimal getTotalTaxDue() {
        return totalTaxDue;
    }

    public void setTotalTaxDue(final BigDecimal totalTaxDue) {
        this.totalTaxDue = totalTaxDue;
    }

    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(final String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(final String connectionType) {
        this.connectionType = connectionType;
    }

    public String getWaterSupplyType() {
        return waterSupplyType;
    }

    public void setWaterSupplyType(String waterSupplyType) {
        this.waterSupplyType = waterSupplyType;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public String getWaterSource() {
        return waterSource;
    }

    public void setWaterSource(String waterSource) {
        this.waterSource = waterSource;
    }

    public String getPropertytype() {
        return propertytype;
    }

    public void setPropertytype(String propertytype) {
        this.propertytype = propertytype;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPipesize() {
        return pipesize;
    }

    public void setPipesize(String pipesize) {
        this.pipesize = pipesize;
    }

    
    public Long getSumpCapacity() {
        return sumpCapacity;
    }

    public void setSumpCapacity(Long sumpCapacity) {
        this.sumpCapacity = sumpCapacity;
    }

    public Integer getNoOfPerson() {
        return noOfPerson;
    }

    public void setNoOfPerson(Integer noOfPerson) {
        this.noOfPerson = noOfPerson;
    }

    public Boolean getIsPrimaryConnection() {
        return isPrimaryConnection;
    }

    public void setIsPrimaryConnection(Boolean isPrimaryConnection) {
        this.isPrimaryConnection = isPrimaryConnection;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(String ulbCode) {
        this.ulbCode = ulbCode;
    }
    
}
