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
import java.util.List;

import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.pims.commons.Position;

public class LegalcaseDepartment {

	private Long id;
	@DateFormat(message = "invalid.fieldvalue.dateOfReceipt")
	private Date receiptOfPwr;
	private Legalcase legalcase;
	@Required(message = "case.dept.null")
	private Department department;
	@Required(message = "case.position.null")
	private Position position;
	private String positionAndEmpName;
	private boolean isPrimaryDepartment;
	private List<Reminder> legalcaseReminders = new ArrayList<Reminder>();
	@DateFormat(message = "invalid.fieldvalue.assignOnDate")
	private Date assignOn;

	public boolean getIsPrimaryDepartment() {
		return isPrimaryDepartment;
	}

	public void setIsPrimaryDepartment(final boolean isPrimaryDepartment) {
		this.isPrimaryDepartment = isPrimaryDepartment;
	}

	public Date getReceiptOfPwr() {
		return receiptOfPwr;
	}

	public void setReceiptOfPwr(final Date receiptOfPwr) {
		this.receiptOfPwr = receiptOfPwr;
	}

	public Legalcase getLegalcase() {
		return legalcase;
	}

	public void setLegalcase(final Legalcase legalcase) {
		this.legalcase = legalcase;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(final Position position) {
		this.position = position;
	}

	public List<ValidationError> validate() {
		final List<ValidationError> errors = new ArrayList<ValidationError>();
		if (legalcase != null && !DateUtils.compareDates(getReceiptOfPwr(), legalcase.getCasedate()))
			errors.add(new ValidationError("dateOfReceipt", "dateOfReceipt.less.casedate"));
		if (legalcase != null && !DateUtils.compareDates(getAssignOn(), legalcase.getCasedate()))
			errors.add(new ValidationError("assignOnDate", "assignOn.less.casedate"));

		return errors;
	}

	public String getPositionAndEmpName() {
		return positionAndEmpName;
	}

	public void setPositionAndEmpName(final String positionAndEmpName) {
		this.positionAndEmpName = positionAndEmpName;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(final Department department) {
		this.department = department;
	}

	public List<Reminder> getLegalcaseReminders() {
		return legalcaseReminders;
	}

	public void setLegalcaseReminders(final List<Reminder> legalcaseReminders) {
		this.legalcaseReminders = legalcaseReminders;
	}

	public void addReminder(final Reminder reminder) {
		getLegalcaseReminders().add(reminder);
	}

	public void removeReminder(final Reminder reminder) {
		getLegalcaseReminders().remove(reminder);
	}

	public Date getAssignOn() {
		return assignOn;
	}

	public void setAssignOn(final Date assignOn) {
		this.assignOn = assignOn;
	}

}
