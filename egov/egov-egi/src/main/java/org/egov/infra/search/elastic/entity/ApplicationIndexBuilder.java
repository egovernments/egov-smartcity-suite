/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infra.search.elastic.entity;

import java.util.Date;

import org.egov.exceptions.EGOVRuntimeException;

/**
 * Builder class for Application Index
 * 
 * @author rishi
 *
 */
public class ApplicationIndexBuilder {

	private ApplicationIndex applicationIndex;

	public ApplicationIndexBuilder(String moduleName, String applicationNumber, Date applicationDate, String applicationType, String applicantName, String status, String url) {
		applicationIndex =  new ApplicationIndex();
		applicationIndex.setModuleName(moduleName);
		applicationIndex.setApplicationNumber(applicationNumber);
		applicationIndex.setApplicationDate(applicationDate);
		applicationIndex.setApplicationType(applicationType);
		applicationIndex.setApplicantName(applicantName);
		applicationIndex.setStatus(status);
		applicationIndex.setUrl(url);
	}
	
	public ApplicationIndexBuilder applicationAddress(String applicantAddress) {
		applicationIndex.setApplicantAddress(applicantAddress);
		return this;
	}
	
	public ApplicationIndexBuilder disposalDate(Date disposalDate) {
		applicationIndex.setDisposalDate(disposalDate);
		return this;
	}
	
	public ApplicationIndexBuilder consumerCode(String consumerCode) {
		applicationIndex.setConsumerCode(consumerCode);
		return this;
	}
	
	public ApplicationIndexBuilder mobileNumber(String mobileNumber) {
		applicationIndex.setMobileNumber(mobileNumber);
		return this;
	}
	
	public ApplicationIndex build() throws EGOVRuntimeException {
		validate();
		return applicationIndex;
	}

	private void validate() throws EGOVRuntimeException {
		if (applicationIndex.getModuleName() == null) {
			throw new EGOVRuntimeException("Module Name is mandatory");
		}
		if (applicationIndex.getApplicationNumber() == null) {
			throw new EGOVRuntimeException("Application Number is mandatory");
		}
		if (applicationIndex.getApplicationDate() == null) {
			throw new EGOVRuntimeException("Application Date is mandatory");
		}
		if (applicationIndex.getApplicationType() == null) {
			throw new EGOVRuntimeException("Application Type is mandatory");
		}
		if (applicationIndex.getApplicantName() == null) {
			throw new EGOVRuntimeException("Applicant Name is mandatory");
		}
		if (applicationIndex.getStatus() == null) {
			throw new EGOVRuntimeException("Application Status is mandatory");
		}
		if (applicationIndex.getUrl() == null) {
			throw new EGOVRuntimeException("URL is required");
		}
	}
}
