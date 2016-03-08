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

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.search.elastic.entity.enums.ApprovalStatus;
import org.egov.infra.search.elastic.entity.enums.ClosureStatus;

/**
 * Builder class for Application Index
 *
 */
public class ApplicationIndexBuilder {

    private final ApplicationIndex applicationIndex;

    public ApplicationIndexBuilder(final String moduleName, final String applicationNumber, final Date applicationDate,
            final String applicationType, final String applicantName, final String status, final String url,
            final String applicantAddress, final String ownername, final String channel) {

        applicationIndex = new ApplicationIndex();
        applicationIndex.setModuleName(moduleName);
        applicationIndex.setApplicationNumber(applicationNumber);
        applicationIndex.setApplicationDate(applicationDate);
        applicationIndex.setApplicationType(moduleName.concat("-".concat(applicationType)));
        applicationIndex.setApplicantName(applicantName);
        applicationIndex.setStatus(status);
        applicationIndex.setUrl(url);
        applicationIndex.setApplicantAddress(applicantAddress);
        applicationIndex.setOwnername(ownername);
        applicationIndex.setChannel(channel);
    }

    public ApplicationIndexBuilder applicationAddress(final String applicantAddress) {
        applicationIndex.setApplicantAddress(applicantAddress);
        return this;
    }

    public ApplicationIndexBuilder disposalDate(final Date disposalDate) {
        applicationIndex.setDisposalDate(disposalDate);
        return this;
    }

    public ApplicationIndexBuilder consumerCode(final String consumerCode) {
        applicationIndex.setConsumerCode(consumerCode);
        return this;
    }

    public ApplicationIndexBuilder mobileNumber(final String mobileNumber) {
        applicationIndex.setMobileNumber(mobileNumber);
        return this;
    }

    public ApplicationIndexBuilder aadharNumber(final String aadharNumber) {
        applicationIndex.setAadharNumber(aadharNumber);
        return this;
    }

    public ApplicationIndexBuilder elapsedDays(final Integer numberOfDays) {
        applicationIndex.setElapsedDays(numberOfDays);
        return this;
    }

    public ApplicationIndexBuilder closed(final ClosureStatus closed) {
        applicationIndex.setClosed(closed);
        return this;
    }

    public ApplicationIndexBuilder approved(final ApprovalStatus approved) {
        applicationIndex.setApproved(approved);
        return this;
    }

    public ApplicationIndex build() throws ApplicationRuntimeException {
        validate();
        return applicationIndex;
    }

    private void validate() throws ApplicationRuntimeException {
        if (applicationIndex.getModuleName() == null)
            throw new ApplicationRuntimeException("Module Name is mandatory");
        if (applicationIndex.getApplicationNumber() == null)
            throw new ApplicationRuntimeException("Application Number is mandatory");
        if (applicationIndex.getApplicationDate() == null)
            throw new ApplicationRuntimeException("Application Date is mandatory");
        if (applicationIndex.getApplicationType() == null)
            throw new ApplicationRuntimeException("Application Type is mandatory");
        if (applicationIndex.getApplicantName() == null)
            throw new ApplicationRuntimeException("Applicant Name is mandatory");
        if (applicationIndex.getStatus() == null)
            throw new ApplicationRuntimeException("Application Status is mandatory");
        if (applicationIndex.getUrl() == null)
            throw new ApplicationRuntimeException("URL is mandatory");
        if (applicationIndex.getChannel() == null)
            throw new ApplicationRuntimeException("Channel is mandatory");
    }
}
