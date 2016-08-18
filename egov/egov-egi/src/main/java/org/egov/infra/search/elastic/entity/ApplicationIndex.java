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

package org.egov.infra.search.elastic.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.search.elastic.entity.enums.ApprovalStatus;
import org.egov.infra.search.elastic.entity.enums.ClosureStatus;
import org.egov.search.domain.Searchable;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.validator.constraints.Length;

/**
 * ApplicationIndex class
 *
 * @author rishi
 */

@Entity
@Table(name = "EG_APPLICATIONINDEX")
@SequenceGenerator(name = ApplicationIndex.SEQ_APPLICATIONINDEX, sequenceName = ApplicationIndex.SEQ_APPLICATIONINDEX, allocationSize = 1)
public class ApplicationIndex extends AbstractAuditable {

    private static final long serialVersionUID = 1L;
    public static final String SEQ_APPLICATIONINDEX = "SEQ_EG_APPLICATIONINDEX";

    @DocumentId
    @Id
    @GeneratedValue(generator = SEQ_APPLICATIONINDEX, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Length(max = 50)
    @Searchable(name = "modulename", group = Searchable.Group.CLAUSES)
    private String moduleName;

    @NotNull
    @Length(max = 50)
    @Searchable(name = "applicationnumber")
    private String applicationNumber;

    @NotNull
    @Searchable(name = "applicationdate")
    private Date applicationDate;

    @NotNull
    @Length(max = 150)
    @Searchable(name = "applicationtype", group = Searchable.Group.CLAUSES)
    private String applicationType;

    @NotNull
    @Length(max = 100)
    @Searchable(name = "applicantname")
    private String applicantName;

    @Length(max = 250)
    @Searchable
    private String applicantAddress;

    private Date disposalDate;

    @NotNull
    @Length(max = 50)
    @Searchable(group = Searchable.Group.CLAUSES)
    private String status;

    @NotNull
    @Length(max = 250)
    @Searchable
    private String url;

    @Length(max = 50)
    @Searchable(name = "consumercode")
    private String consumerCode;

    @Length(min = 10, max = 50)
    @Searchable(name = "mobilenumber")
    private String mobileNumber;

    @Searchable(name = "ownername", group = Searchable.Group.CLAUSES)
    private String ownername;

    @Length(min = 10, max = 50)
    @Searchable(name = "aadharnumber", group = Searchable.Group.SEARCHABLE)
    private String aadharNumber;

    @Searchable(name = "elapseddays", group = Searchable.Group.CLAUSES)
    private Integer elapsedDays;

    @Length(max = 50)
    @Enumerated(EnumType.STRING)
    @Searchable(name = "closed", group = Searchable.Group.CLAUSES)
    private ClosureStatus closed;

    @Length(max = 50)
    @Enumerated(EnumType.STRING)
    @Searchable(name = "approved", group = Searchable.Group.CLAUSES)
    private ApprovalStatus approved;

    @Length(max = 50)
    @Searchable(name = "channel", group = Searchable.Group.CLAUSES)
    private String channel;

    @Transient
    @Searchable(name = "citycode", group = Searchable.Group.CLAUSES)
    private String cityCode;

    @NotNull
    @Length(max = 250)
    @Searchable(name = "cityname", group = Searchable.Group.CLAUSES)
    private String cityName;

    @Transient
    @Searchable(name = "citygrade", group = Searchable.Group.CLAUSES)
    private String cityGrade;

    @Length(max = 250)
    @Searchable(name = "districtname", group = Searchable.Group.CLAUSES)
    private String districtName;

    @Transient
    @Searchable(name = "regionname", group = Searchable.Group.CLAUSES)
    private String regionName;

    @Transient
    @Searchable(name = "isclosed", group = Searchable.Group.SEARCHABLE)
    private Integer isClosed;

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
        this.moduleName = moduleName;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(final String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(final Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(final String applicationType) {
        this.applicationType = applicationType;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(final String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantAddress() {
        return applicantAddress;
    }

    public void setApplicantAddress(final String applicantAddress) {
        this.applicantAddress = applicantAddress;
    }

    public Date getDisposalDate() {
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
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
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

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(final String ownername) {
        this.ownername = ownername;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(final String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public Integer getElapsedDays() {
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
            isClosed = 0;
        else
            isClosed = 1;
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

    public String getCityGrade() {
        return cityGrade;
    }

    public void setCityGrade(final String cityGrade) {
        this.cityGrade = cityGrade;
    }

}