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

package org.egov.ptis.domain.entity.property.view;

import org.egov.infra.admin.master.entity.Boundary;
import org.hibernate.annotations.Immutable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Entity
@Immutable
@Table(name = "EGPT_VIEW_PROPERTYINFO")
public class PropertyMVInfo implements Serializable {

	private static final long serialVersionUID = -6146352214041057969L;

	@Id
	private Integer basicPropertyID;

	@Column(name = "UPICNO")
	private String propertyId;

	@Column(name = "OLDMUNCIPALNUMBER")
	private String oldMuncipalNum;

	@Column(name = "OWNERSNAME")
	private String ownerName;

	private String houseNo;

	@Column(name = "ADDRESS")
	private String propertyAddress;

	@Column(name = "PROPTYMASTER")
	private Long propType;

	@ManyToOne
	@JoinColumn(name = "WARDID")
	private Boundary ward;
	
	@ManyToOne
	@JoinColumn(name = "ELECTIONWARDID")
	private Boundary electionWard;
	

	@ManyToOne
	@JoinColumn(name = "ZONEID")
	private Boundary zone;

	@ManyToOne
	@JoinColumn(name = "STREETID")
	private Boundary street;

	@ManyToOne
	@JoinColumn(name = "BLOCKID")
	private Boundary block;

	@ManyToOne
	@JoinColumn(name = "LOCALITYID")
	private Boundary locality;

	@Column(name = "SOURCE_ID")
	private Integer sourceID;

	@Column(name = "SITAL_AREA")
	private BigDecimal sitalArea;

	@Column(name = "TOTAL_BUILTUP_AREA")
	private BigDecimal toalBuiltUpArea;

	@Column(name = "LATEST_STATUS")
	private Integer latestStatus;

	@Column(name = "AGGREGATE_CURRENT_FIRSTHALF_DEMAND")
	private BigDecimal aggrCurrFirstHalfDmd;

	@Column(name = "AGGREGATE_CURRENT_SECONDHALF_DEMAND")
	private BigDecimal aggrCurrSecondHalfDmd;

	@Column(name = "AGGREGATE_ARREAR_DEMAND")
	private BigDecimal aggrArrDmd;

	@Column(name = "CURRENT_FIRSTHALF_COLLECTION")
	private BigDecimal aggrCurrFirstHalfColl;

	@Column(name = "CURRENT_SECONDHALF_COLLECTION")
	private BigDecimal aggrCurrSecondHalfColl;

	@Column(name = "ARREARCOLLECTION")
	private BigDecimal aggrArrColl;

	@Column(name = "PEN_AGGR_ARREAR_DEMAND")
	private BigDecimal aggrArrearPenaly;

	@Column(name = "PEN_AGGR_ARR_COLL")
	private BigDecimal aggrArrearPenalyColl;

	@Column(name = "PEN_AGGR_CURRENT_FIRSTHALF_DEMAND")
	private BigDecimal aggrCurrFirstHalfPenaly;

	@Column(name = "PEN_AGGR_CURRENT_FIRSTHALF_COLL")
	private BigDecimal aggrCurrFirstHalfPenalyColl;

	@Column(name = "PEN_AGGR_CURRENT_SECONDHALF_DEMAND")
	private BigDecimal aggrCurrSecondHalfPenaly;

	@Column(name = "PEN_AGGR_CURRENT_SECONDHALF_COLL")
	private BigDecimal aggrCurrSecondHalfPenalyColl;

	@Column(name = "ARREAR_DEMAND")
	private BigDecimal arrearDemand;

	@Column(name = "ARREAR_COLLECTION")
	private BigDecimal arrearCollection;

	private String gisRefNo;

	@OneToMany(targetEntity = InstDmdCollInfo.class, cascade = CascadeType.ALL, mappedBy = "propMatView")
	private Set<InstDmdCollInfo> instDmdColl;

	private BigDecimal alv;

	private Boolean isExempted;

	private String usage;

	private Character source;

	@OneToMany(targetEntity = FloorDetailsInfo.class, cascade = CascadeType.ALL, mappedBy = "propMatView")
	private Set<FloorDetailsInfo> floorDetails;

	@Column(name = "MOBILENO")
	private String mobileNumber;

	private Boolean isActive;

	@Column(name = "IS_UNDER_COURTCASE")
	private Boolean isUnderCourtCase;

	@Column(name = "CATEGORY_TYPE")
	private String categoryType;

	@Column(name = "REGD_DOC_NO")
	private String regdDocNo;

	@Column(name = "REGD_DOC_DATE")
	private Date regdDocDate;

	private String pattaNo;

	private BigDecimal marketValue;

	@Column(name = "CAPITALVALUE")
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

	public Long getPropType() {
		return propType;
	}

	public void setPropType(final Long propType) {
		this.propType = propType;
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
		return aggrArrDmd;
	}

	public String getGisRefNo() {
		return gisRefNo;
	}

	public void setGisRefNo(final String gisRefNo) {
		this.gisRefNo = gisRefNo;
	}

	public Set<InstDmdCollInfo> getInstDmdColl() {
		return instDmdColl;
	}

	public void setInstDmdColl(final Set<InstDmdCollInfo> instDmdColl) {
		this.instDmdColl = instDmdColl;
	}

	public BigDecimal getAlv() {
		return alv;
	}

	public void setAlv(final BigDecimal alv) {
		this.alv = alv;
	}

	/*
	 * @Override public String toString() { final StringBuilder objStr = new
	 * StringBuilder();
	 * 
	 * objStr.append("BasicPropertyId: " + getBasicPropertyID()).append(
	 * "|PropertyId: ").append(getPropertyId()) .append("|SitalArea: "
	 * ).append(getSitalArea()).append("|AggCurr1stHalfDemand: "
	 * ).append(getAggrCurrFirstHalfDmd()) .append("|AggCurr2ndHalfDemand: "
	 * ).append(getAggrCurrSecondHalfDmd()) .append("|AggArrDemand: "
	 * ).append(getAggrArrDmd()).append("|AggCurr1stHalfColl: "
	 * ).append(getAggrCurrFirstHalfColl()) .append("|AggCurr2ndHalfColl: "
	 * ).append(getAggrCurrSecondHalfColl()) .append("|AggArrColl: "
	 * ).append(getAggrArrColl()).append("|TotalDemand: "
	 * ).append(getTotalDemand());
	 * 
	 * return objStr.toString(); }
	 */
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

	public void setIsExempted(final Boolean isExempted) {
		this.isExempted = isExempted;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(final String usage) {
		this.usage = usage;
	}

	public Character getSource() {
		return source;
	}

	public void setSource(final Character source) {
		this.source = source;
	}

	public Set<FloorDetailsInfo> getFloorDetails() {
		return floorDetails;
	}

	public void setFloorDetails(final Set<FloorDetailsInfo> floorDetails) {
		this.floorDetails = floorDetails;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(final String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(final boolean isActive) {
		this.isActive = isActive;
	}

	public BigDecimal getAggrArrearPenaly() {
		return aggrArrearPenaly;
	}

	public void setAggrArrearPenaly(final BigDecimal aggrArrearPenaly) {
		this.aggrArrearPenaly = aggrArrearPenaly;
	}

	public BigDecimal getAggrArrearPenalyColl() {
		return aggrArrearPenalyColl;
	}

	public void setAggrArrearPenalyColl(final BigDecimal aggrArrearPenalyColl) {
		this.aggrArrearPenalyColl = aggrArrearPenalyColl;
	}

	public BigDecimal getAggrCurrFirstHalfDmd() {
		return aggrCurrFirstHalfDmd;
	}

	public void setAggrCurrFirstHalfDmd(final BigDecimal aggrCurrFirstHalfDmd) {
		this.aggrCurrFirstHalfDmd = aggrCurrFirstHalfDmd;
	}

	public BigDecimal getAggrCurrSecondHalfDmd() {
		return aggrCurrSecondHalfDmd;
	}

	public void setAggrCurrSecondHalfDmd(final BigDecimal aggrCurrSecondHalfDmd) {
		this.aggrCurrSecondHalfDmd = aggrCurrSecondHalfDmd;
	}

	public BigDecimal getAggrCurrFirstHalfColl() {
		return aggrCurrFirstHalfColl;
	}

	public void setAggrCurrFirstHalfColl(final BigDecimal aggrCurrFirstHalfColl) {
		this.aggrCurrFirstHalfColl = aggrCurrFirstHalfColl;
	}

	public BigDecimal getAggrCurrSecondHalfColl() {
		return aggrCurrSecondHalfColl;
	}

	public void setAggrCurrSecondHalfColl(final BigDecimal aggrCurrSecondHalfColl) {
		this.aggrCurrSecondHalfColl = aggrCurrSecondHalfColl;
	}

	public BigDecimal getAggrCurrFirstHalfPenaly() {
		return aggrCurrFirstHalfPenaly;
	}

	public void setAggrCurrFirstHalfPenaly(final BigDecimal aggrCurrFirstHalfPenaly) {
		this.aggrCurrFirstHalfPenaly = aggrCurrFirstHalfPenaly;
	}

	public BigDecimal getAggrCurrFirstHalfPenalyColl() {
		return aggrCurrFirstHalfPenalyColl;
	}

	public void setAggrCurrFirstHalfPenalyColl(final BigDecimal aggrCurrFirstHalfPenalyColl) {
		this.aggrCurrFirstHalfPenalyColl = aggrCurrFirstHalfPenalyColl;
	}

	public BigDecimal getAggrCurrSecondHalfPenaly() {
		return aggrCurrSecondHalfPenaly;
	}

	public void setAggrCurrSecondHalfPenaly(final BigDecimal aggrCurrSecondHalfPenaly) {
		this.aggrCurrSecondHalfPenaly = aggrCurrSecondHalfPenaly;
	}

	public BigDecimal getAggrCurrSecondHalfPenalyColl() {
		return aggrCurrSecondHalfPenalyColl;
	}

	public void setAggrCurrSecondHalfPenalyColl(final BigDecimal aggrCurrSecondHalfPenalyColl) {
		this.aggrCurrSecondHalfPenalyColl = aggrCurrSecondHalfPenalyColl;
	}

	public BigDecimal getArrearDemand() {
		return arrearDemand;
	}

	public void setArrearDemand(final BigDecimal arrearDemand) {
		this.arrearDemand = arrearDemand;
	}

	public BigDecimal getArrearCollection() {
		return arrearCollection;
	}

	public void setArrearCollection(final BigDecimal arrearCollection) {
		this.arrearCollection = arrearCollection;
	}

	public Boolean getIsUnderCourtCase() {
		return isUnderCourtCase;
	}

	public void setIsUnderCourtCase(final Boolean isUnderCourtCase) {
		this.isUnderCourtCase = isUnderCourtCase;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(final String categoryType) {
		this.categoryType = categoryType;
	}

	public String getRegdDocNo() {
		return regdDocNo;
	}

	public void setRegdDocNo(final String regdDocNo) {
		this.regdDocNo = regdDocNo;
	}

	public Date getRegdDocDate() {
		return regdDocDate;
	}

	public void setRegdDocDate(final Date regdDocDate) {
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

	public void setSurveyNo(final String surveyNo) {
		this.surveyNo = surveyNo;
	}

	public String getDuePeriod() {
		return duePeriod;
	}

	public void setDuePeriod(final String duePeriod) {
		this.duePeriod = duePeriod;
	}

	public BigDecimal getAdvance() {
		return advance;
	}

	public void setAdvance(final BigDecimal advance) {
		this.advance = advance;
	}

	public String getOldMuncipalNum() {
		return oldMuncipalNum;
	}

	public void setOldMuncipalNum(final String oldMuncipalNum) {
		this.oldMuncipalNum = oldMuncipalNum;
	}

	public BigDecimal getRebate() {
		return rebate;
	}

	public void setRebate(final BigDecimal rebate) {
		this.rebate = rebate;
	}

	public BigDecimal getAdjustment() {
		return adjustment;
	}

	public void setAdjustment(final BigDecimal adjustment) {
		this.adjustment = adjustment;
	}
	
	public Boundary getElectionWard() {
		return electionWard;
	}

	public void setElectionWard(Boundary electionWard) {
		this.electionWard = electionWard;
	}

}
