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
package org.egov.lcms.entity.es;

import static org.egov.infra.utils.ApplicationConstant.DEFAULT_TIMEZONE;
import static org.egov.infra.utils.ApplicationConstant.ES_DATE_FORMAT_WITHOUT_TS;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(indexName = "hearingsdocument", type = "hearingsdocument")
public class HearingsDocument {

    @Id
    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT_WITHOUT_TS, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT_WITHOUT_TS)
    private Date hearingDate;

    @Field(type = FieldType.String)
    private String purposeOfHearing;

    @Field(type = FieldType.String)
    private String hearingOutcome;

    @Field(type = FieldType.Boolean)
    private boolean standingCounselPresent;

    @Field(type = FieldType.String)
    private String additionalLawyer;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String employeeNames;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lcNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT_WITHOUT_TS, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT_WITHOUT_TS)
    private Date createdDate;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityCode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityGrade;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String districtName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String regionName;

    public String getCityGrade() {
        return cityGrade;
    }

    public void setCityGrade(final String cityGrade) {
        this.cityGrade = cityGrade;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(final String districtName) {
        this.districtName = districtName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(final String regionName) {
        this.regionName = regionName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(final String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(final String cityCode) {
        this.cityCode = cityCode;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getLcNumber() {
        return lcNumber;
    }

    public void setLcNumber(final String lcNumber) {
        this.lcNumber = lcNumber;
    }

    public Date getHearingDate() {
        return hearingDate;
    }

    public void setHearingDate(final Date hearingDate) {
        this.hearingDate = hearingDate;
    }

    public String getPurposeOfHearing() {
        return purposeOfHearing;
    }

    public void setPurposeOfHearing(final String purposeOfHearing) {
        this.purposeOfHearing = purposeOfHearing;
    }

    public String getHearingOutcome() {
        return hearingOutcome;
    }

    public void setHearingOutcome(final String hearingOutcome) {
        this.hearingOutcome = hearingOutcome;
    }

    public String getAdditionalLawyer() {
        return additionalLawyer;
    }

    public void setAdditionalLawyer(final String additionalLawyer) {
        this.additionalLawyer = additionalLawyer;
    }

    public String getEmployeeNames() {
        return employeeNames;
    }

    public void setEmployeeNames(final String employeeNames) {
        this.employeeNames = employeeNames;
    }

    public Boolean getStandingCounselPresent() {
        return standingCounselPresent;
    }

    public void setStandingCounselPresent(final Boolean standingCounselPresent) {
        this.standingCounselPresent = standingCounselPresent;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String lcNumber;
        private Date hearingDate;
        private String purposeOfHearing;
        private String hearingOutcome;
        private String additionalLawyer;
        private String employeeNames;
        private Boolean standingCounselPresent;
        private String cityName;
        private String cityCode;
        private String id;
        private Date createdDate;
        private String cityGrade;
        private String regionName;
        private String districtName;

        private Builder() {
        }

        public Builder withLcNumber(final String lcNumber) {
            this.lcNumber = lcNumber;
            return this;
        }

        public Builder withHearingDate(final Date hearingDate) {
            this.hearingDate = hearingDate;
            return this;
        }

        public Builder withHearingOutcome(final String hearingOutcome) {
            this.hearingOutcome = hearingOutcome;
            return this;
        }

        public Builder withPurposeOfHearing(final String purposeOfHearing) {
            this.purposeOfHearing = purposeOfHearing;
            return this;
        }

        public Builder withEmployeeNames(final String employeeNames) {
            this.employeeNames = employeeNames;
            return this;
        }

        public Builder withAdditionalLawyer(final String additionalLawyer) {
            this.additionalLawyer = additionalLawyer;
            return this;
        }

        public Builder withStandingCounselPresent(final Boolean standingCounselPresent) {
            this.standingCounselPresent = standingCounselPresent;
            return this;
        }

        public Builder withCreatedDate(final Date createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder withCityCode(final String citycode) {
            cityCode = citycode;
            return this;
        }

        public Builder withId(final String id) {
            this.id = id;
            return this;
        }

        public Builder withCityGrade(final String cityGrade) {
            this.cityGrade = cityGrade;
            return this;
        }

        public Builder withRegionName(final String regionname) {
            regionName = regionname;
            return this;
        }

        public Builder withDistrictName(final String districtname) {
            districtName = districtname;
            return this;
        }

        public Builder withCityName(final String cityname) {
            cityName = cityname;
            return this;
        }

        public HearingsDocument build() {
            final HearingsDocument hearingsIndex = new HearingsDocument();
            hearingsIndex.setLcNumber(lcNumber);
            hearingsIndex.setHearingDate(hearingDate);
            hearingsIndex.setId(id);
            hearingsIndex.setHearingOutcome(hearingOutcome);
            hearingsIndex.setPurposeOfHearing(purposeOfHearing);
            hearingsIndex.setStandingCounselPresent(standingCounselPresent);
            hearingsIndex.setEmployeeNames(employeeNames);
            hearingsIndex.setAdditionalLawyer(additionalLawyer);
            hearingsIndex.setCityCode(cityCode);
            hearingsIndex.setCityName(cityName);
            hearingsIndex.setCreatedDate(createdDate);
            hearingsIndex.setCityGrade(cityGrade);
            hearingsIndex.setDistrictName(districtName);
            hearingsIndex.setRegionName(regionName);
            return hearingsIndex;
        }

    }
}
