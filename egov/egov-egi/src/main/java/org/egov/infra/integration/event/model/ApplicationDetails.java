/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.infra.integration.event.model;

import java.util.Date;

import org.egov.infra.integration.event.model.enums.ApplicationStatus;
import org.egov.infra.integration.event.model.enums.TransactionStatus;

public class ApplicationDetails {

	private static final long serialVersionUID = 1L;
	private String transactionId;
	private TransactionStatus transactionStatus;
	private String remark;
	private String applicationNumber;
	private ApplicationStatus applicationStatus;
	private String viewLink;
	private Date dateOfCompletion;

	private ApplicationDetails() {

	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {

		private String transactionId;
		private TransactionStatus transactionStatus;
		private String remark;
		private String applicationNumber;
		private ApplicationStatus applicationStatus;
		private String viewLink;
		private Date dateOfCompletion;

		private Builder() {
		}

		public Builder withTransactionId(final String transactionId) {
			this.transactionId = transactionId;
			return this;
		}

		public Builder withTransactionStatus(final TransactionStatus transactionStatus) {
			this.transactionStatus = transactionStatus;
			return this;
		}

		public Builder withRemark(final String remark) {
			this.remark = remark;
			return this;
		}

		public Builder withApplicationNumber(final String applicationNumber) {
			this.applicationNumber = applicationNumber;
			return this;
		}

		public Builder withApplicationStatus(final ApplicationStatus applicationStatus) {
			this.applicationStatus = applicationStatus;
			return this;
		}

		public Builder withViewLink(final String viewLink) {
			this.viewLink = viewLink;
			return this;
		}

		public Builder withDateOfCompletion(final Date dateOfCompletion) {
			this.dateOfCompletion = dateOfCompletion;
			return this;
		}

		public ApplicationDetails build() {
			final ApplicationDetails wSApplicationDetails = new ApplicationDetails();
			wSApplicationDetails.transactionId = this.transactionId;
			wSApplicationDetails.transactionStatus = this.transactionStatus;
			wSApplicationDetails.remark = this.remark;
			wSApplicationDetails.applicationNumber = this.applicationNumber;
			wSApplicationDetails.applicationStatus = this.applicationStatus;
			wSApplicationDetails.viewLink = this.viewLink;
			wSApplicationDetails.dateOfCompletion = this.dateOfCompletion;
			return wSApplicationDetails;
		}

	}

	public String getTransactionId() {
		return transactionId;
	}

	public TransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	public String getRemark() {
		return remark;
	}

	public String getApplicationNumber() {
		return applicationNumber;
	}

	public ApplicationStatus getApplicationStatus() {
		return applicationStatus;
	}

	public String getViewLink() {
		return viewLink;
	}

	public Date getDateOfCompletion() {
		return dateOfCompletion;
	}
}
