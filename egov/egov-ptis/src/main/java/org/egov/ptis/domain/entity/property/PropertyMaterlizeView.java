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

import org.egov.infra.admin.master.entity.Boundary;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public class PropertyMaterlizeView implements Serializable {

    private static final long serialVersionUID = -6146352214041057969L;
    private Integer basicPropertyID;
    private String propertyId;
    private String oldMuncipalNum;
    private String ownerName;
    private String houseNo;
    private String propertyAddress;
    private PropertyTypeMaster propTypeMstrID;
    private Boundary ward;
    private Boundary zone;
    private Boundary street;
    private Boundary block;
    private Boundary locality;
    private Integer sourceID;
    private BigDecimal sitalArea;
    private BigDecimal toalBuiltUpArea;
    private Integer latestStatus;
    private BigDecimal aggrCurrFirstHalfDmd;
    private BigDecimal aggrCurrSecondHalfDmd;
    private BigDecimal aggrArrDmd;
    private BigDecimal aggrCurrFirstHalfColl;
    private BigDecimal aggrCurrSecondHalfColl;
    private BigDecimal aggrArrColl;
    private BigDecimal totalDemand;
    private BigDecimal aggrArrearPenaly;
    private BigDecimal aggrArrearPenalyColl;
    private BigDecimal aggrCurrFirstHalfPenaly;
    private BigDecimal aggrCurrFirstHalfPenalyColl; 
    private BigDecimal aggrCurrSecondHalfPenaly;
    private BigDecimal aggrCurrSecondHalfPenalyColl; 
    private BigDecimal arrearDemand;
    private BigDecimal arrearCollection; 
    private String gisRefNo;
    private Set<InstDmdCollMaterializeView> instDmdColl;
    private BigDecimal alv;
    private Boolean isExempted;
    private Character source;
    private Set<FloorDetailsView> floorDetails;
    private String mobileNumber;
    private Boolean isActive;
    private Boolean isUnderCourtCase;
    private String categoryType;
    private String regdDocNo;
    private Date regdDocDate; 
    private String pattaNo;
    private BigDecimal marketValue;
    private BigDecimal capitalValue;
    private Date assessmentDate;
    private Date lastUpdated;
    private String surveyNo;
    private String duePeriod;
    private BigDecimal advance;
    private BigDecimal rebate;
    private BigDecimal adjustment;

    
    public Integer getBasicPropertyID() {
        return basicPropertyID;
    }

    public void setBasicPropertyID(final Integer tbasicPropertyID) {
        basicPropertyID = tbasicPropertyID;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(final String propertyId) {
        this.propertyId = propertyId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(final String houseNo) {
        this.houseNo = houseNo;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(final String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public Boundary getWard() {
        return ward;
    }

    public void setWard(final Boundary ward) {
        this.ward = ward;
    }

    public Boundary getZone() {
        return zone;
    }

    public void setZone(final Boundary zone) {
        this.zone = zone;
    }

    public Boundary getStreet() {
        return street;
    }

    public void setStreet(final Boundary street) {
        this.street = street;
    }

    public Integer getSourceID() {
        return sourceID;
    }

    public void setSourceID(final Integer sourceID) {
        this.sourceID = sourceID;
    }

    public BigDecimal getSitalArea() {
        return sitalArea;
    }

    public void setSitalArea(final BigDecimal sitalArea) {
        this.sitalArea = sitalArea;
    }

    public BigDecimal getToalBuiltUpArea() {
        return toalBuiltUpArea;
    }

    public void setToalBuiltUpArea(final BigDecimal toalBuiltUpArea) {
        this.toalBuiltUpArea = toalBuiltUpArea;
    }

    public Integer getLatestStatus() {
        return latestStatus;
    }

    public void setLatestStatus(final Integer latestStatus) {
        this.latestStatus = latestStatus;
    }

    public PropertyTypeMaster getPropTypeMstrID() {
        return propTypeMstrID;
    }

    public void setPropTypeMstrID(final PropertyTypeMaster propTypeMstrID) {
        this.propTypeMstrID = propTypeMstrID;
    }

    public BigDecimal getAggrArrDmd() {
        return aggrArrDmd;
    }

    public void setAggrArrDmd(final BigDecimal aggrArrDmd) {
        this.aggrArrDmd = aggrArrDmd;
    }

    public BigDecimal getAggrArrColl() {
        return aggrArrColl;
    }

    public void setAggrArrColl(final BigDecimal aggrArrColl) {
        this.aggrArrColl = aggrArrColl;
    }

    public BigDecimal getTotalDemand() {
        return totalDemand;
    }

    public void setTotalDemand(final BigDecimal totalDemand) {
        this.totalDemand = totalDemand;
    }

    public String getGisRefNo() {
        return gisRefNo;
    }

    public void setGisRefNo(final String gisRefNo) {
        this.gisRefNo = gisRefNo;
    }

    public Set<InstDmdCollMaterializeView> getInstDmdColl() {
        return instDmdColl;
    }

    public void setInstDmdColl(final Set<InstDmdCollMaterializeView> instDmdColl) {
        this.instDmdColl = instDmdColl;
    }

    public BigDecimal getAlv() {
        return alv;
    }

    public void setAlv(final BigDecimal alv) {
        this.alv = alv;
    }

    @Override
    public String toString() {
        final StringBuilder objStr = new StringBuilder();

        objStr.append("BasicPropertyId: " + getBasicPropertyID()).append("|PropertyId: ").append(getPropertyId())
        .append("|SitalArea: ").append(getSitalArea()).append("|AggCurr1stHalfDemand: ").append(getAggrCurrFirstHalfDmd())
        .append("|AggCurr2ndHalfDemand: ").append(getAggrCurrSecondHalfDmd())
        .append("|AggArrDemand: ").append(getAggrArrDmd()).append("|AggCurr1stHalfColl: ").append(getAggrCurrFirstHalfColl())
        .append("|AggCurr2ndHalfColl: ").append(getAggrCurrSecondHalfColl())
        .append("|AggArrColl: ").append(getAggrArrColl()).append("|TotalDemand: ").append(getTotalDemand());

        return objStr.toString();
    }

    public Boundary getBlock() {
        return block;
    }

    public void setBlock(final Boundary block) {
        this.block = block;
    }

    public Boundary getLocality() {
        return locality;
    }

    public void setLocality(final Boundary locality) {
        this.locality = locality;
    }

    public Boolean getIsExempted() {
        return isExempted;
    }

    public void setIsExempted(Boolean isExempted) {
        this.isExempted = isExempted;
    }

    public Character getSource() {
        return source;
    }

    public void setSource(Character source) {
        this.source = source;
    }

    public Set<FloorDetailsView> getFloorDetails() {
        return floorDetails;
    }

    public void setFloorDetails(Set<FloorDetailsView> floorDetails) {
        this.floorDetails = floorDetails;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public BigDecimal getAggrArrearPenaly() {
        return aggrArrearPenaly;
    }

    public void setAggrArrearPenaly(BigDecimal aggrArrearPenaly) {
        this.aggrArrearPenaly = aggrArrearPenaly;
    }

    public BigDecimal getAggrArrearPenalyColl() {
        return aggrArrearPenalyColl;
    }

    public void setAggrArrearPenalyColl(BigDecimal aggrArrearPenalyColl) {
        this.aggrArrearPenalyColl = aggrArrearPenalyColl;
    }

    public BigDecimal getAggrCurrFirstHalfDmd() {
        return aggrCurrFirstHalfDmd;
    }

    public void setAggrCurrFirstHalfDmd(BigDecimal aggrCurrFirstHalfDmd) {
        this.aggrCurrFirstHalfDmd = aggrCurrFirstHalfDmd;
    }

    public BigDecimal getAggrCurrSecondHalfDmd() {
        return aggrCurrSecondHalfDmd;
    }

    public void setAggrCurrSecondHalfDmd(BigDecimal aggrCurrSecondHalfDmd) {
        this.aggrCurrSecondHalfDmd = aggrCurrSecondHalfDmd;
    }

    public BigDecimal getAggrCurrFirstHalfColl() {
        return aggrCurrFirstHalfColl;
    }

    public void setAggrCurrFirstHalfColl(BigDecimal aggrCurrFirstHalfColl) {
        this.aggrCurrFirstHalfColl = aggrCurrFirstHalfColl;
    }

    public BigDecimal getAggrCurrSecondHalfColl() {
        return aggrCurrSecondHalfColl;
    }

    public void setAggrCurrSecondHalfColl(BigDecimal aggrCurrSecondHalfColl) {
        this.aggrCurrSecondHalfColl = aggrCurrSecondHalfColl;
    }

    public BigDecimal getAggrCurrFirstHalfPenaly() {
        return aggrCurrFirstHalfPenaly;
    }

    public void setAggrCurrFirstHalfPenaly(BigDecimal aggrCurrFirstHalfPenaly) {
        this.aggrCurrFirstHalfPenaly = aggrCurrFirstHalfPenaly;
    }

    public BigDecimal getAggrCurrFirstHalfPenalyColl() {
        return aggrCurrFirstHalfPenalyColl;
    }

    public void setAggrCurrFirstHalfPenalyColl(BigDecimal aggrCurrFirstHalfPenalyColl) {
        this.aggrCurrFirstHalfPenalyColl = aggrCurrFirstHalfPenalyColl;
    }

    public BigDecimal getAggrCurrSecondHalfPenaly() {
        return aggrCurrSecondHalfPenaly;
    }

    public void setAggrCurrSecondHalfPenaly(BigDecimal aggrCurrSecondHalfPenaly) {
        this.aggrCurrSecondHalfPenaly = aggrCurrSecondHalfPenaly;
    }

    public BigDecimal getAggrCurrSecondHalfPenalyColl() {
        return aggrCurrSecondHalfPenalyColl;
    }

    public void setAggrCurrSecondHalfPenalyColl(BigDecimal aggrCurrSecondHalfPenalyColl) {
        this.aggrCurrSecondHalfPenalyColl = aggrCurrSecondHalfPenalyColl;
    }

    public BigDecimal getArrearDemand() {
        return arrearDemand;
    }

    public void setArrearDemand(BigDecimal arrearDemand) {
        this.arrearDemand = arrearDemand;
    }

    public BigDecimal getArrearCollection() {
        return arrearCollection;
    }

    public void setArrearCollection(BigDecimal arrearCollection) {
        this.arrearCollection = arrearCollection;
    }
    
    public Boolean getIsUnderCourtCase() {
        return isUnderCourtCase;
    }

    public void setIsUnderCourtCase(Boolean isUnderCourtCase) {
        this.isUnderCourtCase = isUnderCourtCase;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getRegdDocNo() {
        return regdDocNo;
    }

    public void setRegdDocNo(String regdDocNo) {
        this.regdDocNo = regdDocNo;
    }

    public Date getRegdDocDate() {
        return regdDocDate;
    }

    public void setRegdDocDate(Date regdDocDate) {
        this.regdDocDate = regdDocDate;
    }

    public BigDecimal getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(final BigDecimal marketValue) {
        this.marketValue = marketValue;
    }

    public String getPattaNo() {
        return pattaNo;
    }

    public void setPattaNo(final String pattaNo) {
        this.pattaNo = pattaNo;
    }

    public BigDecimal getCapitalValue() {
        return capitalValue;
    }

    public void setCapitalValue(final BigDecimal capitalValue) {
        this.capitalValue = capitalValue;
    }

    public Date getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(final Date assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(final Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

        public String getSurveyNo() {
                return surveyNo;
        }

        public void setSurveyNo(String surveyNo) {
                this.surveyNo = surveyNo;
        }

    public String getDuePeriod() {
        return duePeriod;
    }

    public void setDuePeriod(String duePeriod) {
        this.duePeriod = duePeriod;
    }

    public BigDecimal getAdvance() {
        return advance;
    }

    public void setAdvance(BigDecimal advance) {
        this.advance = advance;
    }
    public String getOldMuncipalNum() {
        return oldMuncipalNum;
    }

    public void setOldMuncipalNum(String oldMuncipalNum) {
        this.oldMuncipalNum = oldMuncipalNum;
    }

    public BigDecimal getRebate() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
    }

    public BigDecimal getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(BigDecimal adjustment) {
        this.adjustment = adjustment;
    }
      
}
