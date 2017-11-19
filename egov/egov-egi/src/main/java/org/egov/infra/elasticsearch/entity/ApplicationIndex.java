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

package org.egov.infra.elasticsearch.entity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.egov.infra.elasticsearch.entity.enums.ApprovalStatus;
import org.egov.infra.elasticsearch.entity.enums.ClosureStatus;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.utils.DateUtils;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static org.egov.infra.elasticsearch.entity.ApplicationIndex.SEQ_APPLICATIONINDEX;
import static org.egov.infra.validation.ValidatorUtils.assertNotNull;

@Entity
@Table(name = "EG_APPLICATIONINDEX")
@SequenceGenerator(name = SEQ_APPLICATIONINDEX, sequenceName = SEQ_APPLICATIONINDEX, allocationSize = 1)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "version")
public class ApplicationIndex extends AbstractAuditable {

    public static final String SEQ_APPLICATIONINDEX = "SEQ_EG_APPLICATIONINDEX";
    private static final long serialVersionUID = -5846090185417446039L;

    @Id
    @GeneratedValue(generator = SEQ_APPLICATIONINDEX, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Length(max = 50)
    private String moduleName;

    @NotNull
    @Length(max = 50)
    private String applicationNumber;

    @NotNull
    private Date applicationDate;

    @NotNull
    @Length(max = 150)
    private String applicationType;

    @NotNull
    @Length(max = 250)
    private String applicantName;

    @Length(max = 250)
    private String applicantAddress;

    /*
     * Actual Disposal Date which should be updated when application is closed
     */
    private Date disposalDate;

    @NotNull
    @Length(max = 50)
    private String status;

    @NotNull
    @Length(max = 250)
    private String url;

    @Length(max = 50)
    private String consumerCode;

    @Length(min = 10, max = 50)
    private String mobileNumber;

    private String ownerName;

    @Length(min = 10, max = 50)
    private String aadharNumber;

    private Integer elapsedDays;

    @Length(max = 50)
    @Enumerated(EnumType.STRING)
    private ClosureStatus closed;

    @Length(max = 50)
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approved;

    @Length(max = 50)
    private String channel;

    @Length(max = 4)
    private String cityCode;

    @NotNull
    @Length(max = 250)
    private String cityName;

    @Length(max = 50)
    private String cityGrade;

    @Length(max = 250)
    private String districtName;

    @Length(max = 50)
    private String regionName;

    private Integer isClosed;

    private Integer sla;

    private Integer slaGap;

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(final String moduleName) {
        assertNotNull(moduleName, "Module Name is mandatory");
        this.moduleName = moduleName;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(final String applicationNumber) {
        assertNotNull(applicationNumber, "Application Number is mandatory");
        this.applicationNumber = applicationNumber;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(final Date applicationDate) {
        assertNotNull(applicationDate, "Application Date is mandatory");
        this.applicationDate = applicationDate;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(final String applicationType) {
        assertNotNull(applicationType, "Application Type is mandatory");
        this.applicationType = applicationType;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(final String applicantName) {
        assertNotNull(applicantName, "Applicant Name is mandatory");
        this.applicantName = applicantName;
    }

    public String getApplicantAddress() {
        return applicantAddress;
    }

    public void setApplicantAddress(final String applicantAddress) {
        this.applicantAddress = applicantAddress;
    }

    public Date getDisposalDate() {
        if (closed.name().equals(ClosureStatus.YES.toString()))
            disposalDate = new Date();
        else if (closed.name().equals(ClosureStatus.NO.toString()))
            disposalDate = null;
        return disposalDate;
    }

    public void setDisposalDate(final Date disposalDate) {
        this.disposalDate = disposalDate;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(final String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(final String districtName) {
        this.districtName = districtName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        assertNotNull(status, "Application Status is mandatory");
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        assertNotNull(url, "URL is mandatory");
        this.url = url;
    }

    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(final String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(final String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(final String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public Integer getElapsedDays() {
        if (closed.name().equals(ClosureStatus.YES.toString()))
            elapsedDays = DateUtils.daysBetween(getCreatedDate(), disposalDate);
        else if (closed.name().equals(ClosureStatus.NO.toString()))
            elapsedDays = DateUtils.daysBetween(getCreatedDate(), new Date());
        return elapsedDays;
    }

    public void setElapsedDays(final Integer elapsedDays) {
        this.elapsedDays = elapsedDays;
    }

    public ClosureStatus getClosed() {
        return closed;
    }

    public void setClosed(final ClosureStatus closed) {
        this.closed = closed;
        if (this.closed.toString().equals(ClosureStatus.YES.toString()))
            setIsClosed(1);
        else
            setIsClosed(0);
    }

    public ApprovalStatus getApproved() {
        return approved;
    }

    public void setApproved(final ApprovalStatus approved) {
        this.approved = approved;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(final String channel) {
        assertNotNull(channel, "Channel is mandatory");
        this.channel = channel;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(final String cityCode) {
        this.cityCode = cityCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(final String regionName) {
        this.regionName = regionName;
    }

    public Integer getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(final Integer isClosed) {
        this.isClosed = isClosed;
    }

    public String getCityGrade() {
        return cityGrade;
    }

    public void setCityGrade(final String cityGrade) {
        this.cityGrade = cityGrade;
    }

    public Integer getSla() {
        return sla;
    }

    public void setSla(final Integer sla) {
        this.sla = sla;
    }

    public Integer getSlaGap() {
        return slaGap;
    }

    public void setSlaGap(final Integer slaGap) {
        this.slaGap = slaGap;
    }

    public static final class Builder {
        private String moduleName;
        private String applicationNumber;
        private Date applicationDate;
        private String applicationType;
        private String applicantName;
        private String applicantAddress;
        private Date disposalDate;
        private String status;
        private String url;
        private String consumerCode;
        private String mobileNumber;
        private String ownerName;
        private String aadharNumber;
        private Integer elapsedDays = 0;
        private ClosureStatus closed;
        private ApprovalStatus approved;
        private String channel;
        private Integer sla = 0;

        private Builder() {
        }

        public Builder withModuleName(final String moduleName) {
            this.moduleName = moduleName;
            return this;
        }

        public Builder withApplicationNumber(final String applicationNumber) {
            this.applicationNumber = applicationNumber;
            return this;
        }

        public Builder withApplicationDate(final Date applicationDate) {
            this.applicationDate = applicationDate;
            return this;
        }

        public Builder withApplicationType(final String applicationType) {
            this.applicationType = applicationType;
            return this;
        }

        public Builder withApplicantName(final String applicantName) {
            this.applicantName = applicantName;
            return this;
        }

        public Builder withApplicantAddress(final String applicantAddress) {
            this.applicantAddress = applicantAddress;
            return this;
        }

        public Builder withDisposalDate(final Date disposalDate) {
            this.disposalDate = disposalDate;
            return this;
        }

        public Builder withStatus(final String status) {
            this.status = status;
            return this;
        }

        public Builder withUrl(final String url) {
            this.url = url;
            return this;
        }

        public Builder withConsumerCode(final String consumerCode) {
            this.consumerCode = consumerCode;
            return this;
        }

        public Builder withMobileNumber(final String mobileNumber) {
            this.mobileNumber = mobileNumber;
            return this;
        }

        public Builder withOwnername(final String ownerName) {
            this.ownerName = ownerName;
            return this;
        }

        public Builder withAadharNumber(final String aadharNumber) {
            this.aadharNumber = aadharNumber;
            return this;
        }

        public Builder withElapsedDays(final Integer elapsedDays) {
            this.elapsedDays = elapsedDays;
            return this;
        }

        public Builder withClosed(final ClosureStatus closed) {
            this.closed = closed;
            return this;
        }

        public Builder withApproved(final ApprovalStatus approved) {
            this.approved = approved;
            return this;
        }

        public Builder withChannel(final String channel) {
            this.channel = channel;
            return this;
        }

        public Builder withSla(final Integer sla) {
            this.sla = sla;
            return this;
        }

        public ApplicationIndex build() {
            final ApplicationIndex applicationIndex = new ApplicationIndex();
            applicationIndex.setModuleName(moduleName);
            applicationIndex.setApplicationNumber(applicationNumber);
            applicationIndex.setApplicationDate(applicationDate);
            applicationIndex.setApplicationType(applicationType);
            applicationIndex.setApplicantName(applicantName);
            applicationIndex.setApplicantAddress(applicantAddress);
            applicationIndex.setDisposalDate(disposalDate);
            applicationIndex.setStatus(status);
            applicationIndex.setUrl(url);
            applicationIndex.setConsumerCode(consumerCode);
            applicationIndex.setMobileNumber(mobileNumber);
            applicationIndex.setOwnerName(ownerName);
            applicationIndex.setAadharNumber(aadharNumber);
            applicationIndex.setElapsedDays(elapsedDays);
            applicationIndex.setClosed(closed);
            applicationIndex.setApproved(approved);
            applicationIndex.setChannel(channel);
            applicationIndex.setClosed(closed);
            applicationIndex.setSla(sla);
            applicationIndex.setSlaGap(elapsedDays - sla);
            return applicationIndex;
        }
    }
}