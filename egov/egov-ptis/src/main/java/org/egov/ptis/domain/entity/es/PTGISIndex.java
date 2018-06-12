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

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;

import static org.egov.infra.utils.ApplicationConstant.DEFAULT_TIMEZONE;
import static org.egov.infra.utils.ApplicationConstant.ES_DATE_FORMAT;

import java.util.Date;

import org.egov.infra.config.core.ApplicationThreadLocals;
import org.springframework.data.annotation.Id;

@Document(indexName = "propertysurveydetails", type = "propertysurveydetails")
public class PTGISIndex {
    @Id
    private String id;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityCode;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityName;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String districtName;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String regionName;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revenueWard;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String electionWard;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String blockName;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String localityName;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String doorNo;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String source;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date applicationDate;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String applicationNo;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String applicationType;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String assessmentNo;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String assistantName;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String riName;
    @Field(type = FieldType.Double)
    private Double systemTax;
    @Field(type = FieldType.Double)
    private Double gisTax;
    @Field(type = FieldType.Double)
    private Double applicationTax;
    @Field(type = FieldType.Boolean)
    private Boolean thirdPrtyFlag;
    @Field(type = FieldType.Double)
    private Double approvedTax;
    @Field(type = FieldType.Double)
    private Double taxVariance;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String applicationStatus;
    @Field(type = FieldType.Boolean)
    private Boolean isApproved;
    @Field(type = FieldType.Boolean)
    private Boolean isCancelled;
    @Field(type = FieldType.Boolean)
    private Boolean sentToThirdParty;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date completionDate;
    @Field(type = FieldType.Integer)
    private Integer ageOfCompletion;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String functionaryName;

    public static Builder builder() {
        return new Builder();
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

    public String getRevenueWard() {
        return revenueWard;
    }

    public void setRevenueWard(String revenueWard) {
        this.revenueWard = revenueWard;
    }

    public String getElectionWard() {
        return electionWard;
    }

    public void setElectionWard(String electionWard) {
        this.electionWard = electionWard;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public String getAssessmentNo() {
        return assessmentNo;
    }

    public void setAssessmentNo(String assessmentNo) {
        this.assessmentNo = assessmentNo;
    }

    public String getAssistantName() {
        return assistantName;
    }

    public void setAssistantName(String assistantName) {
        this.assistantName = assistantName;
    }

    public String getRiName() {
        return riName;
    }

    public void setRiName(String riName) {
        this.riName = riName;
    }

    public Double getSystemTax() {
        return systemTax;
    }

    public void setSystemTax(Double systemTax) {
        this.systemTax = systemTax;
    }

    public Double getGisTax() {
        return gisTax;
    }

    public void setGisTax(Double gisTax) {
        this.gisTax = gisTax;
    }

    public Double getApplicationTax() {
        return applicationTax;
    }

    public void setApplicationTax(Double applicationTax) {
        this.applicationTax = applicationTax;
    }

    public Boolean getThirdPrtyFlag() {
        return thirdPrtyFlag;
    }

    public void setThirdPrtyFlag(Boolean thirdPrtyFlag) {
        this.thirdPrtyFlag = thirdPrtyFlag;
    }

    public Double getApprovedTax() {
        return approvedTax;
    }

    public void setApprovedTax(Double approvedTax) {
        this.approvedTax = approvedTax;
    }

    public Double getTaxVariance() {
        return taxVariance;
    }

    public void setTaxVariance(Double taxVariance) {
        this.taxVariance = taxVariance;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public Boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Boolean getSentToThirdParty() {
        return sentToThirdParty;
    }

    public void setSentToThirdParty(Boolean sentToThirdParty) {
        this.sentToThirdParty = sentToThirdParty;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public Integer getAgeOfCompletion() {
        return ageOfCompletion;
    }

    public void setAgeOfCompletion(Integer ageOfCompletion) {
        this.ageOfCompletion = ageOfCompletion;
    }

    public String getFunctionaryName() {
        return functionaryName;
    }

    public void setFunctionaryName(String functionaryName) {
        this.functionaryName = functionaryName;
    }

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = ApplicationThreadLocals.getCityCode().concat("_").concat(applicationNo);
    }

    public static final class Builder {
        private String revenueWard;
        private String source;
        private Date applicationDate;
        private String applicationNo;
        private String applicationType;
        private String assessmentNo;
        private Double systemTax;
        private Double gisTax;
        private Double applicationTax;
        private Boolean thirdPrtyFlag;
        private Double approvedTax;
        private Double taxVariance;
        private String applicationStatus;
        private Boolean isApproved;
        private Boolean isCancelled;
        private Date completionDate;
        private Integer ageOfCompletion;
        private String electionWard;
        private String blockName;
        private String localityName;
        private String doorNo;
        private String assistantName;
        private String riName;
        private Boolean sentToThirdParty;
        private String functionaryName;

        private Builder() {

        }

        public Builder withRevenueWard(final String revenueWard) {
            this.revenueWard = revenueWard;
            return this;
        }

        public Builder withSource(final String source) {
            this.source = source;
            return this;
        }

        public Builder withApplicationdate(final Date applicationDate) {
            this.applicationDate = applicationDate;
            return this;
        }

        public Builder withApplicationNo(final String applicationNo) {
            this.applicationNo = applicationNo;
            return this;
        }

        public Builder withApplicationType(final String applicationType) {
            this.applicationType = applicationType;
            return this;
        }

        public Builder withAssessmentNo(final String assessmentNo) {
            this.assessmentNo = assessmentNo;
            return this;
        }

        public Builder withSystemTax(final Double systemTax) {
            this.systemTax = systemTax;
            return this;
        }

        public Builder withGisTax(final Double gisTax) {
            this.gisTax = gisTax;
            return this;
        }

        public Builder withApplicationTax(final Double applicationTax) {
            this.applicationTax = applicationTax;
            return this;
        }

        public Builder withApprovedTax(final Double approvedTax) {
            this.approvedTax = approvedTax;
            return this;
        }

        public Builder withTaxVariance(final Double taxVariance) {
            this.taxVariance = taxVariance;
            return this;
        }

        public Builder withApplicationStatus(final String applicationStatus) {
            this.applicationStatus = applicationStatus;
            return this;
        }

        public Builder withThirdPrtyFlag(final Boolean thirdPrtyFlag) {
            this.thirdPrtyFlag = thirdPrtyFlag;
            return this;
        }

        public Builder withIsApproved(final Boolean isApproved) {
            this.isApproved = isApproved;
            return this;
        }

        public Builder withIsCancelled(final Boolean isCancelled) {
            this.isCancelled = isCancelled;
            return this;
        }

        public Builder withCompletionDate(final Date completionDate) {
            this.completionDate = completionDate;
            return this;
        }

        public Builder withAgeOfCompletion(final Integer ageOfCompletion) {
            this.ageOfCompletion = ageOfCompletion;
            return this;
        }

        public Builder withElectionWard(final String electionWard) {
            this.electionWard = electionWard;
            return this;
        }

        public Builder withBlockName(final String blockName) {
            this.blockName = blockName;
            return this;
        }

        public Builder withLocalityName(final String localityName) {
            this.localityName = localityName;
            return this;
        }

        public Builder withDoorNo(final String doorNo) {
            this.doorNo = doorNo;
            return this;
        }

        public Builder withAssistantName(final String assistantName) {
            this.assistantName = assistantName;
            return this;
        }

        public Builder withRiName(final String riName) {
            this.riName = riName;
            return this;
        }

        public Builder withSentToThirdParty(final Boolean sentToThirdParty) {
            this.sentToThirdParty = sentToThirdParty;
            return this;
        }

        public Builder withFunctionaryName(final String functionaryName) {
            this.functionaryName = functionaryName;
            return this;
        }

        public PTGISIndex build() {
            final PTGISIndex ptGisIndex = new PTGISIndex();
            ptGisIndex.setRevenueWard(revenueWard);
            ptGisIndex.setSource(source);
            ptGisIndex.setApplicationDate(applicationDate);
            ptGisIndex.setApplicationNo(applicationNo);
            ptGisIndex.setApplicationType(applicationType);
            ptGisIndex.setAssessmentNo(assessmentNo);
            ptGisIndex.setGisTax(gisTax);
            ptGisIndex.setSystemTax(systemTax);
            ptGisIndex.setApplicationTax(applicationTax);
            ptGisIndex.setApprovedTax(approvedTax);
            ptGisIndex.setTaxVariance(taxVariance);
            ptGisIndex.setApplicationStatus(applicationStatus);
            ptGisIndex.setThirdPrtyFlag(thirdPrtyFlag);
            ptGisIndex.setIsApproved(isApproved);
            ptGisIndex.setIsCancelled(isCancelled);
            ptGisIndex.setCompletionDate(completionDate);
            ptGisIndex.setAgeOfCompletion(ageOfCompletion);
            ptGisIndex.setId();
            ptGisIndex.setElectionWard(electionWard);
            ptGisIndex.setBlockName(blockName);
            ptGisIndex.setLocalityName(localityName);
            ptGisIndex.setDoorNo(doorNo);
            ptGisIndex.setAssistantName(assistantName);
            ptGisIndex.setRiName(riName);
            ptGisIndex.setSentToThirdParty(sentToThirdParty);
            ptGisIndex.setFunctionaryName(functionaryName);

            return ptGisIndex;
        }

    }

    @Override
    public String toString() {
        return "PTGISIndex [id=" + id + ", cityCode=" + cityCode + ", cityName=" + cityName + ", districtName=" + districtName
                + ", regionName=" + regionName + ", revenueWard=" + revenueWard + ", electionWard=" + electionWard
                + ", blockName=" + blockName + ", localityName=" + localityName + ", doorNo=" + doorNo + ", source=" + source
                + ", applicationDate=" + applicationDate + ", applicationNo=" + applicationNo + ", applicationType="
                + applicationType + ", assessmentNo=" + assessmentNo + ", assistantName=" + assistantName + ", riName=" + riName
                + ", systemTax=" + systemTax + ", gisTax=" + gisTax + ", applicationTax=" + applicationTax + ", thirdPrtyFlag="
                + thirdPrtyFlag + ", approvedTax=" + approvedTax + ", taxVariance=" + taxVariance + ", applicationStatus="
                + applicationStatus + ", isApproved=" + isApproved + ", isCancelled=" + isCancelled + ", sentToThirdParty="
                + sentToThirdParty + ", completionDate=" + completionDate + ", ageOfCompletion=" + ageOfCompletion + "]";
    }

}
