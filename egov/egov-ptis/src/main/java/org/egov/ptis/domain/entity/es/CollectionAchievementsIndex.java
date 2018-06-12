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
package org.egov.ptis.domain.entity.es;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "collectionachievements", type = "collectionachievements")
public class CollectionAchievementsIndex {

    @Id
    private String id;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityCode;
    
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityGrade;
    
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String districtName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String regionName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String billCollector;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String billCollectorMobileNo;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String billCollectorCode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revenueInspector;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revenueInspectorMobileNo;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revenueInspectorCode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revenueOfficer;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revenueOfficerMobileNo;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revenueOfficerCode;
    
    @Field(type = FieldType.Double)
    private Double totalDemand;
    
    @Field(type = FieldType.Double)
    private Double cytdDemand;
    
    @Field(type = FieldType.Double)
    private Double cytdColl;
    
    @Field(type = FieldType.Double)
    private Double achievement;
    
    @Field(type = FieldType.Double)
    private Double cytdBalDemand;
    
    @Field(type = FieldType.Double)
    private Double lytdColl;
    
    @Field(type = FieldType.Double)
    private Double lyVar;
    
    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public void setId() {
        String userCode = StringUtils.EMPTY;
        if(StringUtils.isNotBlank(billCollectorCode))
            userCode = "BC".concat("_").concat(billCollectorCode);
        else if(StringUtils.isNotBlank(revenueInspectorCode))
            userCode = "RI".concat("_").concat(revenueInspectorCode);
        else if(StringUtils.isNotBlank(revenueOfficerCode))
            userCode = "RO".concat("_").concat(revenueOfficerCode);
        this.id = cityCode.concat("_").concat(userCode);
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityGrade() {
        return cityGrade;
    }

    public void setCityGrade(String cityGrade) {
        this.cityGrade = cityGrade;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getBillCollector() {
        return billCollector;
    }

    public void setBillCollector(String billCollector) {
        this.billCollector = billCollector;
    }

    public String getBillCollectorMobileNo() {
        return billCollectorMobileNo;
    }

    public void setBillCollectorMobileNo(String billCollectorMobileNo) {
        this.billCollectorMobileNo = billCollectorMobileNo;
    }

    public String getBillCollectorCode() {
        return billCollectorCode;
    }

    public void setBillCollectorCode(String billCollectorCode) {
        this.billCollectorCode = billCollectorCode;
    }

    public String getRevenueInspector() {
        return revenueInspector;
    }

    public void setRevenueInspector(String revenueInspector) {
        this.revenueInspector = revenueInspector;
    }

    public String getRevenueInspectorMobileNo() {
        return revenueInspectorMobileNo;
    }

    public void setRevenueInspectorMobileNo(String revenueInspectorMobileNo) {
        this.revenueInspectorMobileNo = revenueInspectorMobileNo;
    }

    public String getRevenueInspectorCode() {
        return revenueInspectorCode;
    }

    public void setRevenueInspectorCode(String revenueInspectorCode) {
        this.revenueInspectorCode = revenueInspectorCode;
    }

    public String getRevenueOfficer() {
        return revenueOfficer;
    }

    public void setRevenueOfficer(String revenueOfficer) {
        this.revenueOfficer = revenueOfficer;
    }

    public String getRevenueOfficerMobileNo() {
        return revenueOfficerMobileNo;
    }

    public void setRevenueOfficerMobileNo(String revenueOfficerMobileNo) {
        this.revenueOfficerMobileNo = revenueOfficerMobileNo;
    }

    public String getRevenueOfficerCode() {
        return revenueOfficerCode;
    }

    public void setRevenueOfficerCode(String revenueOfficerCode) {
        this.revenueOfficerCode = revenueOfficerCode;
    }

    public Double getTotalDemand() {
        return totalDemand;
    }

    public void setTotalDemand(Double totalDemand) {
        this.totalDemand = totalDemand;
    }

    public Double getCytdDemand() {
        return cytdDemand;
    }

    public void setCytdDemand(Double cytdDemand) {
        this.cytdDemand = cytdDemand;
    }

    public Double getCytdColl() {
        return cytdColl;
    }

    public void setCytdColl(Double cytdColl) {
        this.cytdColl = cytdColl;
    }

    public Double getAchievement() {
        return achievement;
    }

    public void setAchievement(Double achievement) {
        this.achievement = achievement;
    }

    public Double getCytdBalDemand() {
        return cytdBalDemand;
    }

    public void setCytdBalDemand(Double cytdBalDemand) {
        this.cytdBalDemand = cytdBalDemand;
    }

    public Double getLytdColl() {
        return lytdColl;
    }

    public void setLytdColl(Double lytdColl) {
        this.lytdColl = lytdColl;
    }

    public Double getLyVar() {
        return lyVar;
    }

    public void setLyVar(Double lyVar) {
        this.lyVar = lyVar;
    }
    
    
    public static final class Builder {
        private String cityCode;
        private String billCollector;
        private String billCollectorMobileNo;
        private String billCollectorCode;
        private String revenueInspector;
        private String revenueInspectorMobileNo;
        private String revenueInspectorCode;
        private String revenueOfficer;
        private String revenueOfficerMobileNo;
        private String revenueOfficerCode;
        private Double totalDemand;
        private Double cytdDemand;
        private Double cytdColl;
        private Double cytdBalDemand;
        private Double achievement;
        private Double lytdColl;
        private Double lyVar;

        private Builder() {

        }

        public Builder withCityCode(String cityCode){
            this.cityCode = cityCode;
            return this;
        }
        
        public Builder withBillCollector(String billCollector) {
            this.billCollector = billCollector;
            return this;
        }

        public Builder withBillCollectorMobNo(String billCollectorMobileNo) {
            this.billCollectorMobileNo = billCollectorMobileNo;
            return this;
        }

        public Builder withBillCollectorCode(String billCollectorCode) {
            this.billCollectorCode = billCollectorCode;
            return this;
        }

        public Builder withRevenueInspector(String revenueInspector) {
            this.revenueInspector = revenueInspector;
            return this;
        }

        public Builder withRevenueInspectorMobNo(String revenueInspectorMobileNo) {
            this.revenueInspectorMobileNo = revenueInspectorMobileNo;
            return this;
        }

        public Builder withRevenueInspectorCode(String revenueInspectorCode) {
            this.revenueInspectorCode = revenueInspectorCode;
            return this;
        }

        public Builder withRevenueOfficer(String revenueOfficer) {
            this.revenueOfficer = revenueOfficer;
            return this;
        }

        public Builder withRevenueOfficerMobNo(String revenueOfficerMobileNo) {
            this.revenueOfficerMobileNo = revenueOfficerMobileNo;
            return this;
        }

        public Builder withRevenueOfficerCode(String revenueOfficerCode) {
            this.revenueOfficerCode = revenueOfficerCode;
            return this;
        }

        public Builder withTotalDemand(Double totalDemand) {
            this.totalDemand = totalDemand;
            return this;
        }

        public Builder withCytdDemand(Double cytdDemand) {
            this.cytdDemand = cytdDemand;
            return this;
        }

        public Builder withCytdColl(Double cytdColl) {
            this.cytdColl = cytdColl;
            return this;
        }

        public Builder withCytdBalDemand(Double cytdBalDemand) {
            this.cytdBalDemand = cytdBalDemand;
            return this;
        }

        public Builder withAchievement(Double achievement) {
            this.achievement = achievement;
            return this;
        }
        
        public Builder withLytdColl(Double lytdColl) {
            this.lytdColl = lytdColl;
            return this;
        }
        
        public Builder withLyVar(Double lyVar) {
            this.lyVar = lyVar;
            return this;
        }

        public CollectionAchievementsIndex build() {
            CollectionAchievementsIndex collAchievementIndex = new CollectionAchievementsIndex();
            
            collAchievementIndex.setCityCode(cityCode);
            collAchievementIndex.setBillCollector(billCollector);
            collAchievementIndex.setBillCollectorMobileNo(billCollectorMobileNo);
            collAchievementIndex.setBillCollectorCode(billCollectorCode);
            collAchievementIndex.setRevenueInspector(revenueInspector);
            collAchievementIndex.setRevenueInspectorMobileNo(revenueInspectorMobileNo);
            collAchievementIndex.setRevenueInspectorCode(revenueInspectorCode);
            collAchievementIndex.setRevenueOfficer(revenueOfficer);
            collAchievementIndex.setRevenueOfficerMobileNo(revenueOfficerMobileNo);
            collAchievementIndex.setRevenueOfficerCode(revenueOfficerCode);
            collAchievementIndex.setId();
            collAchievementIndex.setTotalDemand(totalDemand);
            collAchievementIndex.setCytdDemand(cytdDemand);
            collAchievementIndex.setCytdColl(cytdColl);
            collAchievementIndex.setCytdBalDemand(cytdBalDemand);
            collAchievementIndex.setAchievement(achievement);
            collAchievementIndex.setLytdColl(lytdColl);
            collAchievementIndex.setLyVar(lyVar);
            
            return collAchievementIndex;
        }
    }


    @Override
    public String toString() {
        return "CollectionAchievementsIndex [id=" + id + ", cityCode=" + cityCode + ", cityName=" + cityName + ", cityGrade="
                + cityGrade + ", districtName=" + districtName + ", regionName=" + regionName + ", billCollector=" + billCollector
                + ", billCollectorMobileNo=" + billCollectorMobileNo + ", billCollectorCode=" + billCollectorCode
                + ", revenueInspector=" + revenueInspector + ", revenueInspectorMobileNo=" + revenueInspectorMobileNo
                + ", revenueInspectorCode=" + revenueInspectorCode + ", revenueOfficer=" + revenueOfficer
                + ", revenueOfficerMobileNo=" + revenueOfficerMobileNo + ", revenueOfficerCode=" + revenueOfficerCode
                + ", totalDemand=" + totalDemand + ", cytdDemand=" + cytdDemand + ", cytdColl=" + cytdColl + ", achievement="
                + achievement + ", cytdBalDemand=" + cytdBalDemand + ", lytdColl=" + lytdColl + ", lyVar=" + lyVar + "]";
    }
    
    
    
}
