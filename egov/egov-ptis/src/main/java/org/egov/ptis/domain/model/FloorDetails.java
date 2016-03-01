package org.egov.ptis.domain.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FloorDetails implements Serializable {
        private String floorNoCode;
        private String buildClassificationCode;
        private String natureOfUsageCode;
        private String occupancyCode;
        private String occupantName;
        private String constructionDate;
        private Float plinthArea;
        private Float plinthLength;
        private Float plinthBreadth;
        private String exemptionCategoryCode;
        private Boolean drainageCode;
        private Boolean unstructuredLand;
        private Integer noOfSeats;

        public String getFloorNoCode() {
                return floorNoCode;
        }

        public void setFloorNoCode(String floorNoCode) {
                this.floorNoCode = floorNoCode;
        }

        public String getBuildClassificationCode() {
                return buildClassificationCode;
        }

        public void setBuildClassificationCode(String buildClassificationCode) {
                this.buildClassificationCode = buildClassificationCode;
        }

        public String getNatureOfUsageCode() {
                return natureOfUsageCode;
        }

        public void setNatureOfUsageCode(String natureOfUsageCode) {
                this.natureOfUsageCode = natureOfUsageCode;
        }

        public String getOccupancyCode() {
                return occupancyCode;
        }

        public void setOccupancyCode(String occupancyCode) {
                this.occupancyCode = occupancyCode;
        }

        public String getOccupantName() {
                return occupantName;
        }

        public void setOccupantName(String occupantName) {
                this.occupantName = occupantName;
        }

        public String getConstructionDate() {
                return constructionDate;
        }

        public void setConstructionDate(String constructionDate) {
                this.constructionDate = constructionDate;
        }

        public Float getPlinthArea() {
                return plinthArea;
        }

        public void setPlinthArea(Float plinthArea) {
                this.plinthArea = plinthArea;
        }

        public String getExemptionCategoryCode() {
                return exemptionCategoryCode;
        }

        public void setExemptionCategoryCode(String exemptionCategoryCode) {
                this.exemptionCategoryCode = exemptionCategoryCode;
        }

        public Boolean getDrainageCode() {
                return drainageCode;
        }

        public void setDrainageCode(Boolean drainageCode) {
                this.drainageCode = drainageCode;
        }

        public Integer getNoOfSeats() {
                return noOfSeats;
        }

        public void setNoOfSeats(Integer noOfSeats) {
                this.noOfSeats = noOfSeats;
        }

        @Override
        public String toString() {
                return "FloorDetails [floorNoCode=" + floorNoCode + ", buildClassificationCode=" + buildClassificationCode
                                + ", natureOfUsageCode=" + natureOfUsageCode + ", occupancyCode=" + occupancyCode + ", occupantName="
                                + occupantName + ", constructionDate=" + constructionDate + ", plinthArea=" + plinthArea
                                + ", exemptionCategoryCode=" + exemptionCategoryCode + ", drainageCode=" + drainageCode + ", noOfSeats="
                                + noOfSeats + "]";
        }

    public Float getPlinthLength() {
        return plinthLength;
    }

    public void setPlinthLength(Float plinthLength) {
        this.plinthLength = plinthLength;
    }

    public Float getPlinthBreadth() {
        return plinthBreadth;
    }

    public void setPlinthBreadth(Float plinthBreadth) {
        this.plinthBreadth = plinthBreadth;
    }

    public Boolean getUnstructuredLand() {
        return unstructuredLand;
    }

    public void setUnstructuredLand(Boolean unstructuredLand) {
        this.unstructuredLand = unstructuredLand;
    }

}