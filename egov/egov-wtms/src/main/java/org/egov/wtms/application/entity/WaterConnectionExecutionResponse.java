package org.egov.wtms.application.entity;

public class WaterConnectionExecutionResponse {

    private WaterConnExecutionDetails[] executeWaterApplicationDetails;

    public WaterConnExecutionDetails[] getExecuteWaterApplicationDetails() {
        return executeWaterApplicationDetails;
    }

    public void setExecuteWaterApplicationDetails(final WaterConnExecutionDetails[] executeWaterApplicationDetails) {
        this.executeWaterApplicationDetails = executeWaterApplicationDetails;
    }

}
