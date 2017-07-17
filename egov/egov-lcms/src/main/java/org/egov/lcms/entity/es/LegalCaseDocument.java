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
package org.egov.lcms.entity.es;

import static org.egov.infra.utils.ApplicationConstant.DEFAULT_TIMEZONE;
import static org.egov.infra.utils.ApplicationConstant.ES_DATE_FORMAT_WITHOUT_TS;

import java.util.Date;

import org.egov.infra.config.core.ApplicationThreadLocals;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(indexName = "legalcasedocument", type = "legalcasedocument")
public class LegalCaseDocument {

    @Id
    private String id;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String caseNumber;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lcNumber;

    @Field(type = FieldType.String)
    private String caseTitle;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String caseType;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String courtName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String courtType;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String petitionType;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String officerIncharge;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT_WITHOUT_TS, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT_WITHOUT_TS)
    private Date caseDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT_WITHOUT_TS, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT_WITHOUT_TS)
    private Date caseReceivingDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT_WITHOUT_TS, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT_WITHOUT_TS)
    private Date nextDate;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String previousCaseNumber;

    @Field(type = FieldType.Boolean)
    private boolean filedByULB;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String status;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String subStatus;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String petitionerNames;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String respondantNames;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT_WITHOUT_TS, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT_WITHOUT_TS)
    private Date caFilingDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT_WITHOUT_TS, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT_WITHOUT_TS)
    private Date pwrDueDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT_WITHOUT_TS, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT_WITHOUT_TS)
    private Date caDueDate;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String advocateName;

    @Field(type = FieldType.Boolean)
    private boolean seniorAdvocate;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String interimOrderType;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String judgmentOutcome;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT_WITHOUT_TS, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT_WITHOUT_TS)
    private Date judgmentDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT_WITHOUT_TS, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT_WITHOUT_TS)
    private Date orderSentToDeptDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT_WITHOUT_TS, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT_WITHOUT_TS)
    private Date deadLineImplementByDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT_WITHOUT_TS, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT_WITHOUT_TS)
    private Date judgmentImplDate;

    @Field(type = FieldType.String)
    private String judgmentImplemented;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String implementationFailure;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT_WITHOUT_TS, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT_WITHOUT_TS)
    private Date disposalDate;

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

    public String getId() {
        return id;
    }

    public void setId() {
        id = ApplicationThreadLocals.getCityCode().concat("_").concat(lcNumber);
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(final String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getLcNumber() {
        return lcNumber;
    }

    public void setLcNumber(final String lcNumber) {
        this.lcNumber = lcNumber;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(final String caseType) {
        this.caseType = caseType;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(final String courtName) {
        this.courtName = courtName;
    }

    public String getCourtType() {
        return courtType;
    }

    public void setCourtType(final String courtType) {
        this.courtType = courtType;
    }

    public String getPetitionType() {
        return petitionType;
    }

    public void setPetitionType(final String petitionType) {
        this.petitionType = petitionType;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(final String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public String getOfficerIncharge() {
        return officerIncharge;
    }

    public void setOfficerIncharge(final String officerIncharge) {
        this.officerIncharge = officerIncharge;
    }

    public Date getCaseDate() {
        return caseDate;
    }

    public void setCaseDate(final Date caseDate) {
        this.caseDate = caseDate;
    }

    public Date getCaseReceivingDate() {
        return caseReceivingDate;
    }

    public void setCaseReceivingDate(final Date caseReceivingDate) {
        this.caseReceivingDate = caseReceivingDate;
    }

    public Date getNextDate() {
        return nextDate;
    }

    public void setNextDate(final Date nextDate) {
        this.nextDate = nextDate;
    }

    public String getPreviousCaseNumber() {
        return previousCaseNumber;
    }

    public void setPreviousCaseNumber(final String previousCaseNumber) {
        this.previousCaseNumber = previousCaseNumber;
    }

    public Boolean isFiledByULB() {
        return filedByULB;
    }

    public void setFiledByULB(final Boolean filedByULB) {
        this.filedByULB = filedByULB;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(final String subStatus) {
        this.subStatus = subStatus;
    }

    public String getPetitionerNames() {
        return petitionerNames;
    }

    public void setPetitionerNames(final String petitionerNames) {
        this.petitionerNames = petitionerNames;
    }

    public String getRespondantNames() {
        return respondantNames;
    }

    public void setRespondantNames(final String respondantNames) {
        this.respondantNames = respondantNames;
    }

    public Date getCaFilingDate() {
        return caFilingDate;
    }

    public void setCaFilingDate(final Date caFilingDate) {
        this.caFilingDate = caFilingDate;
    }

    public String getAdvocateName() {
        return advocateName;
    }

    public void setAdvocateName(final String advocateName) {
        this.advocateName = advocateName;
    }

    public Boolean isSeniorAdvocate() {
        return seniorAdvocate;
    }

    public void setSeniorAdvocate(final Boolean seniorAdvocate) {
        this.seniorAdvocate = seniorAdvocate;
    }

    public String getInterimOrderType() {
        return interimOrderType;
    }

    public void setInterimOrderType(final String interimOrderType) {
        this.interimOrderType = interimOrderType;
    }

    public String getJudgmentOutcome() {
        return judgmentOutcome;
    }

    public void setJudgmentOutcome(final String judgmentOutcome) {
        this.judgmentOutcome = judgmentOutcome;
    }

    public Date getJudgmentDate() {
        return judgmentDate;
    }

    public void setJudgmentDate(final Date judgmentDate) {
        this.judgmentDate = judgmentDate;
    }

    public Date getOrderSentToDeptDate() {
        return orderSentToDeptDate;
    }

    public void setOrderSentToDeptDate(final Date orderSentToDeptDate) {
        this.orderSentToDeptDate = orderSentToDeptDate;
    }

    public Date getDeadLineImplementByDate() {
        return deadLineImplementByDate;
    }

    public void setDeadLineImplementByDate(final Date deadLineImplementByDate) {
        this.deadLineImplementByDate = deadLineImplementByDate;
    }

    public Date getJudgmentImplDate() {
        return judgmentImplDate;
    }

    public void setJudgmentImplDate(final Date judgmentImplDate) {
        this.judgmentImplDate = judgmentImplDate;
    }

    public String getJudgmentImplemented() {
        return judgmentImplemented;
    }

    public void setJudgmentImplemented(final String judgmentImplemented) {
        this.judgmentImplemented = judgmentImplemented;
    }

    public String getImplementationFailure() {
        return implementationFailure;
    }

    public void setImplementationFailure(final String implementationFailure) {
        this.implementationFailure = implementationFailure;
    }

    public Date getDisposalDate() {
        return disposalDate;
    }

    public void setDisposalDate(final Date disposalDate) {
        this.disposalDate = disposalDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getPwrDueDate() {
        return pwrDueDate;
    }

    public void setPwrDueDate(final Date pwrDueDate) {
        this.pwrDueDate = pwrDueDate;
    }

    public Date getCaDueDate() {
        return caDueDate;
    }

    public void setCaDueDate(final Date caDueDate) {
        this.caDueDate = caDueDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String caseNumber;
        private String lcNumber;
        private String caseTitle;
        private String caseType;
        private String courtName;
        private String courtType;
        private String petitionType;
        private Date caseDate;
        private Date caseReceivingDate;
        private String previousCaseNumber;
        private String officerIncharge;
        private Date nextDate;
        private Boolean filedByULB;
        private String status;
        private String subStatus;
        private String petitionerNames;
        private String respondantNames;
        private Date pwrDueDate;
        private Date caDueDate;
        private String cityName;
        private String cityCode;
        private String regionName;
        private String districtName;
        private String cityGrade;
        private Date createdDate;

        private Builder() {
        }

        public Builder withCaseNumber(final String caseNumber) {
            this.caseNumber = caseNumber;
            return this;
        }

        public Builder withLcNumber(final String lcNumber) {
            this.lcNumber = lcNumber;
            return this;
        }

        public Builder withCaseTitle(final String caseTitle) {
            this.caseTitle = caseTitle;
            return this;
        }

        public Builder withCaseType(final String caseType) {
            this.caseType = caseType;
            return this;
        }

        public Builder withCourtName(final String courtName) {
            this.courtName = courtName;
            return this;
        }

        public Builder withCourtType(final String courtType) {
            this.courtType = courtType;
            return this;
        }

        public Builder withPetitionType(final String petitionType) {
            this.petitionType = petitionType;
            return this;
        }

        public Builder withPreviousCaseNumber(final String previousCaseNumber) {
            this.previousCaseNumber = previousCaseNumber;
            return this;
        }

        public Builder withCaseDate(final Date caseDate) {
            this.caseDate = caseDate;
            return this;
        }

        public Builder withCaseReceivingDate(final Date caseReceivingDate) {
            this.caseReceivingDate = caseReceivingDate;
            return this;
        }

        public Builder withOfficerIncharge(final String officerIncharge) {
            this.officerIncharge = officerIncharge;
            return this;
        }

        public Builder withNextDate(final Date nextDate) {
            this.nextDate = nextDate;
            return this;
        }

        public Builder withFiledByULB(final Boolean filedByULB) {
            this.filedByULB = filedByULB;
            return this;
        }

        public Builder withStatus(final String status) {
            this.status = status;
            return this;
        }

        public Builder withSubStatus(final String subStatus) {
            this.subStatus = subStatus;
            return this;
        }

        public Builder withPetitionerNames(final String petitionerNames) {
            this.petitionerNames = petitionerNames;
            return this;

        }

        public Builder withPwrDueDate(final Date pwrDueDate) {
            this.pwrDueDate = pwrDueDate;
            return this;

        }

        public Builder withCaDueDate(final Date caDueDate) {
            this.caDueDate = caDueDate;
            return this;

        }

        public Builder withRespondantNames(final String respondantNames) {
            this.respondantNames = respondantNames;
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

        public Builder withCityName(final String cityname) {
            cityName = cityname;
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

        public LegalCaseDocument build() {
            final LegalCaseDocument legalCaseIndex = new LegalCaseDocument();
            legalCaseIndex.setCaseNumber(caseNumber);
            legalCaseIndex.setLcNumber(lcNumber);
            legalCaseIndex.setId();
            legalCaseIndex.setCaseTitle(caseTitle);
            legalCaseIndex.setCaseType(caseType);
            legalCaseIndex.setCourtName(courtName);
            legalCaseIndex.setCourtType(courtType);
            legalCaseIndex.setPetitionType(petitionType);
            legalCaseIndex.setPreviousCaseNumber(previousCaseNumber);
            legalCaseIndex.setCaseDate(caseDate);
            legalCaseIndex.setCaseReceivingDate(caseReceivingDate);
            legalCaseIndex.setFiledByULB(filedByULB);
            legalCaseIndex.setNextDate(nextDate);
            legalCaseIndex.setOfficerIncharge(officerIncharge);
            legalCaseIndex.setStatus(status);
            legalCaseIndex.setSubStatus(subStatus);
            legalCaseIndex.setPetitionerNames(petitionerNames);
            legalCaseIndex.setRespondantNames(respondantNames);
            legalCaseIndex.setPwrDueDate(pwrDueDate);
            legalCaseIndex.setCaDueDate(caDueDate);
            legalCaseIndex.setCityCode(cityCode);
            legalCaseIndex.setCityName(cityName);
            legalCaseIndex.setCityGrade(cityGrade);
            legalCaseIndex.setRegionName(regionName);
            legalCaseIndex.setDistrictName(districtName);
            legalCaseIndex.setCreatedDate(createdDate);
            return legalCaseIndex;
        }

    }
}
