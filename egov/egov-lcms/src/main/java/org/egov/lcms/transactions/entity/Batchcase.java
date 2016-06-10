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

	public void setId(Long id) {
		this.id = id;
	}

	public Legalcase getLegalcase() {
		return legalcase;
	}

	public void setLegalcase(Legalcase legalcase) {
		this.legalcase = legalcase;
	}

	public Date getBatchCaseDate() {
		return batchCaseDate;
	}

	public void setBatchCaseDate(Date batchCaseDate) {
		this.batchCaseDate = batchCaseDate;
	}

	public String getPetitionerName() {
		return petitionerName;
	}

	public void setPetitionerName(String petitionerName) {
		this.petitionerName = petitionerName;
	}

	public String getCasenumber() {
		return casenumber;
	}

	public void setCasenumber(String casenumber) {
		this.casenumber = casenumber;
	}

	public String getCaseNumberForDisplay() {
		if (getCasenumber() != null) {
			int lastOccurance = getCasenumber().indexOf("/", 0);
			setCaseNumberForDisplay(getCasenumber().substring(
					lastOccurance + 1, getCasenumber().length()));
		}
		return caseNumberForDisplay;
	}

	public void setCaseNumberForDisplay(String caseNumberForDisplay) {
		this.caseNumberForDisplay = caseNumberForDisplay;
	}

	/*
	 * If they entered either one of the value then validation should throw for
	 * rest of the fields. That check is done over here.
	 */
	public List<ValidationError> validate() {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		if (StringUtils.isBlank(getCaseNumberForDisplay())
				&& getBatchCaseDate() == null
				&& StringUtils.isBlank(getPetitionerName()))
			errors.add(new ValidationError("caseNumber", "batchcase.null"));
		else {
			if (StringUtils.isBlank(getPetitionerName()))
				errors.add(new ValidationError("petitionerName",
						"batchcase.petitiontName.null"));
			if (getBatchCaseDate() == null)
				errors.add(new ValidationError("caseDate",
						"batchcase.caseDate.null"));
			if (StringUtils.isBlank(getCaseNumberForDisplay()))
				errors.add(new ValidationError("casenumber",
						"batchcase.casenumber.null"));
		}
		return errors;
	}

}
