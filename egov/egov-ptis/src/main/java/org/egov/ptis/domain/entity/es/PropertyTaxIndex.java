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

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "propertytax", type = "propertytaxdetails")
public class PropertyTaxIndex {

    @Id
    private String consumerCode;

    @Field(type = FieldType.Double, index = FieldIndex.not_analyzed)
    private Double builtupArea;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityGrade;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String districtName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityCode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String adminWardName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String consumerType;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String regionName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revenueWard;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revZoneName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revenueBlock;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String sitalArea;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String propertyUsage;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String propertyCategory;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String locationName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String aadhaarNumber;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String mobileNumber;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ptAssesmentNo;

    /*
     * @GeoPointField private GeoPoint propertygeo;
     * @GeoPointField private GeoPoint boundarygeo;
     */

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String propertyAddress;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String billCollector;

    @Field(type = FieldType.Double)
    private Double annualCollection;

    @Field(type = FieldType.Double)
    private Double annualDemand;

    @Field(type = FieldType.Double)
    private Double arrearBalance;

    @Field(type = FieldType.Double)
    private Double arrearCollection;

    @Field(type = FieldType.Double)
    private Double arrearDemand;

    @Field(type = FieldType.Double)
    private Double arvAmount;

    @Field(type = FieldType.Double)
    private Double firstInstallmentDemand;

    @Field(type = FieldType.Double)
    private Double firstInstallmentCollection;

    @Field(type = FieldType.Double)
    private Double secondInstallmentDemand;

    @Field(type = FieldType.Double)
    private Double secondInstallmentCollection;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String consumerName;

    @Field(type = FieldType.Double)
    private Double totalBalance;

    @Field(type = FieldType.Double)
    private Double annualBalance;

    @Field(type = FieldType.Double)
    private Double totalDemand;

    @Field(type = FieldType.Double)
    private Double arrearInterestDemand;

    @Field(type = FieldType.Double)
    private Double arrearInterestCollection;

    @Field(type = FieldType.Double)
    private Double currentInterestDemand;

    @Field(type = FieldType.Double)
    private Double currentInterestCollection;

    @Field(type = FieldType.Double)
    private Double currentyearcoll;

    @Field(type = FieldType.Boolean)
    private Boolean isActive;

    @Field(type = FieldType.Boolean)
    private Boolean isUnderCourtcase;

    @Field(type = FieldType.Double)
    private Double totalCollection;
    
    @Field(type = FieldType.Boolean)
    private Boolean isExempted;
    
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String duePeriod;

    @Field(type = FieldType.Double)
    private Double advance;
    
    @Field(type = FieldType.Double)
    private Double rebate;
    
    @Field(type = FieldType.Double)
    private Double adjustment;
    
    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public Double getBuiltupArea() {
        return builtupArea;
    }

    public void setBuiltupArea(Double builtupArea) {
        this.builtupArea = builtupArea;
    }

    public String getCityGrade() {
        return cityGrade;
    }

    public void setCityGrade(String cityGrade) {
        this.cityGrade = cityGrade;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAdminWardName() {
        return adminWardName;
    }

    public void setAdminWardName(String adminWardName) {
        this.adminWardName = adminWardName;
    }

    public String getConsumerType() {
        return consumerType;
    }

    public void setConsumerType(String consumerType) {
        this.consumerType = consumerType;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRevenueWard() {
        return revenueWard;
    }

    public void setRevenueWard(String revenueWard) {
        this.revenueWard = revenueWard;
    }

    public String getRevZoneName() {
        return revZoneName;
    }

    public void setRevZoneName(String revZoneName) {
        this.revZoneName = revZoneName;
    }

    public String getRevenueBlock() {
        return revenueBlock;
    }

    public void setRevenueBlock(String revenueBlock) {
        this.revenueBlock = revenueBlock;
    }

    public String getSitalArea() {
        return sitalArea;
    }

    public void setSitalArea(String sitalArea) {
        this.sitalArea = sitalArea;
    }

    public String getPropertyUsage() {
        return propertyUsage;
    }

    public void setPropertyUsage(String propertyUsage) {
        this.propertyUsage = propertyUsage;
    }

    public String getPropertyCategory() {
        return propertyCategory;
    }

    public void setPropertyCategory(String propertyCategory) {
        this.propertyCategory = propertyCategory;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPtAssesmentNo() {
        return ptAssesmentNo;
    }

    public void setPtAssesmentNo(String ptAssesmentNo) {
        this.ptAssesmentNo = ptAssesmentNo;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public String getBillCollector() {
        return billCollector;
    }

    public void setBillCollector(String billCollector) {
        this.billCollector = billCollector;
    }

    public Double getAnnualCollection() {
        return annualCollection;
    }

    public void setAnnualCollection(Double annualCollection) {
        this.annualCollection = annualCollection;
    }

    public Double getAnnualDemand() {
        return annualDemand;
    }

    public void setAnnualDemand(Double annualDemand) {
        this.annualDemand = annualDemand;
    }

    public Double getArrearBalance() {
        return arrearBalance;
    }

    public void setArrearBalance(Double arrearBalance) {
        this.arrearBalance = arrearBalance;
    }

    public Double getArrearCollection() {
        return arrearCollection;
    }

    public void setArrearCollection(Double arrearCollection) {
        this.arrearCollection = arrearCollection;
    }

    public Double getArrearDemand() {
        return arrearDemand;
    }

    public void setArrearDemand(Double arrearDemand) {
        this.arrearDemand = arrearDemand;
    }

    public Double getArvAmount() {
        return arvAmount;
    }

    public void setArvAmount(Double arvAmount) {
        this.arvAmount = arvAmount;
    }

    public Double getFirstInstallmentDemand() {
        return firstInstallmentDemand;
    }

    public void setFirstInstallmentDemand(Double firstInstallmentDemand) {
        this.firstInstallmentDemand = firstInstallmentDemand;
    }

    public Double getFirstInstallmentCollection() {
        return firstInstallmentCollection;
    }

    public void setFirstInstallmentCollection(Double firstInstallmentCollection) {
        this.firstInstallmentCollection = firstInstallmentCollection;
    }

    public Double getSecondInstallmentDemand() {
        return secondInstallmentDemand;
    }

    public void setSecondInstallmentDemand(Double secondInstallmentDemand) {
        this.secondInstallmentDemand = secondInstallmentDemand;
    }

    public Double getSecondInstallmentCollection() {
        return secondInstallmentCollection;
    }

    public void setSecondInstallmentCollection(Double secondInstallmentCollection) {
        this.secondInstallmentCollection = secondInstallmentCollection;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public Double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(Double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public Double getAnnualBalance() {
        return annualBalance;
    }

    public void setAnnualBalance(Double annualBalance) {
        this.annualBalance = annualBalance;
    }

    public Double getTotalDemand() {
        return totalDemand;
    }

    public void setTotalDemand(Double totalDemand) {
        this.totalDemand = totalDemand;
    }

    public Double getArrearInterestDemand() {
        return arrearInterestDemand;
    }

    public void setArrearInterestDemand(Double arrearInterestDemand) {
        this.arrearInterestDemand = arrearInterestDemand;
    }

    public Double getArrearInterestCollection() {
        return arrearInterestCollection;
    }

    public void setArrearInterestCollection(Double arrearInterestCollection) {
        this.arrearInterestCollection = arrearInterestCollection;
    }

    public Double getCurrentInterestDemand() {
        return currentInterestDemand;
    }

    public void setCurrentInterestDemand(Double currentInterestDemand) {
        this.currentInterestDemand = currentInterestDemand;
    }

    public Double getCurrentInterestCollection() {
        return currentInterestCollection;
    }

    public void setCurrentInterestCollection(Double currentInterestCollection) {
        this.currentInterestCollection = currentInterestCollection;
    }

    public Double getCurrentyearcoll() {
        return currentyearcoll;
    }

    public void setCurrentyearcoll(Double currentyearcoll) {
        this.currentyearcoll = currentyearcoll;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsUnderCourtcase() {
        return isUnderCourtcase;
    }

    public void setIsUnderCourtcase(Boolean isUnderCourtcase) {
        this.isUnderCourtcase = isUnderCourtcase;
    }

    public Double getTotalCollection() {
        return totalCollection;
    }

    public void setTotalCollection(Double totalCollection) {
        this.totalCollection = totalCollection;
    }

    public Boolean getIsExempted() {
        return isExempted;
    }

    public void setIsExempted(Boolean isExempted) {
        this.isExempted = isExempted;
    }

    public String getDuePeriod() {
        return duePeriod;
    }

    public void setDuePeriod(String duePeriod) {
        this.duePeriod = duePeriod;
    }

    public Double getAdvance() {
        return advance;
    }

    public void setAdvance(Double advance) {
        this.advance = advance;
    }

    public Double getRebate() {
        return rebate;
    }

    public void setRebate(Double rebate) {
        this.rebate = rebate;
    }

    public Double getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(Double adjustment) {
        this.adjustment = adjustment;
    }
}
