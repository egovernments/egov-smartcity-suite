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
