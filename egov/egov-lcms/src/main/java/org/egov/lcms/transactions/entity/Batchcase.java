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

import org.apache.commons.lang.StringUtils;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.utils.LcmsConstants;

public class Batchcase {

	private Long id;
	private Legalcase legalcase;
	@DateFormat(message = "invalid.fieldvalue.model.batchCaseDate")
	@ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.batchcase.date")
	private Date batchCaseDate;
	@OptionalPattern(regex = LcmsConstants.caseNumberRegx, message = "batchcase.number.alphanumeric")
	private String casenumber;
	@OptionalPattern(regex = LcmsConstants.mixedCharType1withComma, message = "petitionerName.batchcase.mixedChar")
	private String petitionerName;
	private String caseNumberForDisplay;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Legalcase getLegalcase() {
		return legalcase;
	}

	public void setLegalcase(final Legalcase legalcase) {
		this.legalcase = legalcase;
	}

	public Date getBatchCaseDate() {
		return batchCaseDate;
	}

	public void setBatchCaseDate(final Date batchCaseDate) {
		this.batchCaseDate = batchCaseDate;
	}

	public String getPetitionerName() {
		return petitionerName;
	}

	public void setPetitionerName(final String petitionerName) {
		this.petitionerName = petitionerName;
	}

	public String getCasenumber() {
		return casenumber;
	}

	public void setCasenumber(final String casenumber) {
		this.casenumber = casenumber;
	}

	public String getCaseNumberForDisplay() {
		if (getCasenumber() != null) {
			final int lastOccurance = getCasenumber().indexOf("/", 0);
			setCaseNumberForDisplay(getCasenumber().substring(lastOccurance + 1, getCasenumber().length()));
		}
		return caseNumberForDisplay;
	}

	public void setCaseNumberForDisplay(final String caseNumberForDisplay) {
		this.caseNumberForDisplay = caseNumberForDisplay;
	}

	/*
	 * If they entered either one of the value then validation should throw for
	 * rest of the fields. That check is done over here.
	 */
	public List<ValidationError> validate() {
		final List<ValidationError> errors = new ArrayList<ValidationError>();
		if (StringUtils.isBlank(getCaseNumberForDisplay()) && getBatchCaseDate() == null
				&& StringUtils.isBlank(getPetitionerName()))
			errors.add(new ValidationError("caseNumber", "batchcase.null"));
		else {
			if (StringUtils.isBlank(getPetitionerName()))
				errors.add(new ValidationError("petitionerName", "batchcase.petitiontName.null"));
			if (getBatchCaseDate() == null)
				errors.add(new ValidationError("caseDate", "batchcase.caseDate.null"));
			if (StringUtils.isBlank(getCaseNumberForDisplay()))
				errors.add(new ValidationError("casenumber", "batchcase.casenumber.null"));
		}
		return errors;
	}

}
