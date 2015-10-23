/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this 
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It 
           is required that all modified versions of this material be marked in 
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program 
           with regards to rights under trademark law for use of the trade names 
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.restapi.model;

import javax.validation.constraints.NotNull;

public class ConnectionInfo {
    @NotNull
    private String propertyID;
    
    private String consumerCode;
    
    private String reasonforChangeOfUsage;
    
    @NotNull
    private String waterSource;
    @NotNull
    private String connectionType;
    @NotNull
    private String propertyType;
    @NotNull
    private String category;
    @NotNull
    private String usageType;
    @NotNull
    private String pipeSize;
    
    private Long sumpCapacity;
    private Integer numberOfRooms;
    private Integer numberOfPersons;
    
    public String getReasonforChangeOfUsage() {
        return reasonforChangeOfUsage;
    }
    public void setReasonforChangeOfUsage(String reasonforChangeOfUsage) {
        this.reasonforChangeOfUsage = reasonforChangeOfUsage;
    }
    /**
     * @return the propertyID
     */
    public String getPropertyID() {
        return propertyID;
    }
    /**
     * @param propertyID the propertyID to set
     */
    public void setPropertyID(String propertyID) {
        this.propertyID = propertyID;
    }
    /**
     * @return the connectionType
     */
    public String getConnectionType() {
        return connectionType;
    }
    /**
     * @param connectionType the connectionType to set
     */
    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }
    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }
    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }
    /**
     * @return the usageType
     */
    public String getUsageType() {
        return usageType;
    }
    /**
     * @param usageType the usageType to set
     */
    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }
    /**
     * @return the propertyType
     */
    public String getPropertyType() {
        return propertyType;
    }
    /**
     * @param propertyType the propertyType to set
     */
    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }
    /**
     * @return the waterSource
     */
    public String getWaterSource() {
        return waterSource;
    }
    /**
     * @param waterSource the waterSource to set
     */
    public void setWaterSource(String waterSource) {
        this.waterSource = waterSource;
    }
    /**
     * @return the pipeSize
     */
    public String getPipeSize() {
        return pipeSize;
    }
    /**
     * @param pipeSize the pipeSize to set
     */
    public void setPipeSize(String pipeSize) {
        this.pipeSize = pipeSize;
    }
    /**
     * @return the sumpCapacity
     */
    public Long getSumpCapacity() {
        return sumpCapacity;
    }
    /**
     * @param sumpCapacity the sumpCapacity to set
     */
    public void setSumpCapacity(Long sumpCapacity) {
        this.sumpCapacity = sumpCapacity;
    }
    /**
     * @return the numberOfRooms
     */
    public Integer getNumberOfRooms() {
        return numberOfRooms;
    }
    /**
     * @param numberOfRooms the numberOfRooms to set
     */
    public void setNumberOfRooms(Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }
    /**
     * @return the numberOfPersons
     */
    public Integer getNumberOfPersons() {
        return numberOfPersons;
    }
    /**
     * @param numberOfPersons the numberOfPersons to set
     */
    public void setNumberOfPersons(Integer numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }
    public String getConsumerCode() {
        return consumerCode;
    }
    public void setConsumerCode(String consumerCode) {
        this.consumerCode = consumerCode;
    }
    
    
    
    
}
