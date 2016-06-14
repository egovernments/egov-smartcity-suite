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
package org.egov.lcms.transactions.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.lcms.utils.LcmsConstants;
import org.hibernate.validator.constraints.Length;

/**
 * Judgmentimpl entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class Judgmentimpl extends BaseModel {
	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;

	// Fields
	private Judgment eglcJudgment;
	private Long isCompiled;
	@DateFormat(message = "invalid.fieldvalue.model.dateofcompliance")
	@ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.compliance.date")
	private Date dateofcompliance;
	@Length(max = 1024, message = "compliancereport.maxlength")
	private String compliancereport;
	private String reason;
	@Length(max = 128, message = "details.maxlength")
	private String details;
	private Set<Contempt> contempt = new HashSet<Contempt>();
	private Set<Appeal> appeal = new HashSet<Appeal>();

	public void addContempt(final Contempt contempt) {
		getContempt().add(contempt);
	}

	public void addAppeal(final Appeal appeal) {
		getAppeal().add(appeal);
	}

	@Valid
	public Set<Contempt> getContempt() {
		return contempt;
	}

	public void setContempt(final Set<Contempt> contempt) {
		this.contempt = contempt;
	}

	@Valid
	public Set<Appeal> getAppeal() {
		return appeal;
	}

	public void setAppeal(final Set<Appeal> appeal) {
		this.appeal = appeal;
	}

	public Judgment getEglcJudgment() {
		return eglcJudgment;
	}

	public void setEglcJudgment(final Judgment eglcJudgment) {
		this.eglcJudgment = eglcJudgment;
	}

	public Long getIsCompiled() {
		return isCompiled;
	}

	public void setIsCompiled(final Long isCompiled) {
		this.isCompiled = isCompiled;
	}

	public Date getDateofcompliance() {
		return dateofcompliance;
	}

	public void setDateofcompliance(final Date dateofcompliance) {
		this.dateofcompliance = dateofcompliance;
	}

	public String getCompliancereport() {
		return compliancereport;
	}

	public void setCompliancereport(final String compliancereport) {
		this.compliancereport = compliancereport;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason
	 *            the reason to set
	 */
	public void setReason(final String reason) {
		this.reason = reason;
	}

	@Override
	public List<ValidationError> validate() {
		final List<ValidationError> errors = new ArrayList<ValidationError>();
		if (getDateofcompliance() != null
				&& !DateUtils.compareDates(getDateofcompliance(), eglcJudgment.getOrderDate()))
			errors.add(new ValidationError("dateofcompliance", "dateofcompliance.less.orderDate"));
		for (final Contempt contempt : getContempt())
			errors.addAll(contempt.validate());
		for (final Appeal appeal : getAppeal())
			errors.addAll(appeal.validate());
		return errors;
	}

	/*
	 * public String getImplementationdetails() { return implementationdetails;
	 * } public void setImplementationdetails(String implementationdetails) {
	 * this.implementationdetails = implementationdetails; }
	 */

	public String getDetails() {
		return details;
	}

	public void setDetails(final String details) {
		this.details = details;
	}

}