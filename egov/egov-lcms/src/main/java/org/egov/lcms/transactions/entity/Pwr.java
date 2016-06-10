package org.egov.lcms.transactions.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.utils.LcmsConstants;

/**
 * Pwr entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Pwr {

	// Fields

	private Long id;
	private Legalcase eglcLegalcase;
	private String uploadPwr;
	@DateFormat(message = "invalid.fieldvalue.caFilingdate")
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.cafiling.date")
	private Date caFilingdate;
	private String uploadCa;
	@DateFormat(message = "invalid.fieldvalue.caDueDate")
	private Date caDueDate;
	@DateFormat(message = "invalid.fieldvalue.pwrDueDate")
	private Date pwrDueDate;

	public Legalcase getEglcLegalcase() {
		return this.eglcLegalcase;
	}

	public void setEglcLegalcase(Legalcase eglcLegalcase) {
		this.eglcLegalcase = eglcLegalcase;
	}

	public String getUploadPwr() {
		return this.uploadPwr;
	}

	public void setUploadPwr(String uploadPwr) {
		this.uploadPwr = uploadPwr;
	}

	public Date getCaFilingdate() {
		return this.caFilingdate;
	}

	public void setCaFilingdate(Date caFilingdate) {
		this.caFilingdate = caFilingdate;
	}

	public String getUploadCa() {
		return this.uploadCa;
	}

	public void setUploadCa(String uploadCa) {
		this.uploadCa = uploadCa;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCaDueDate() {
		return caDueDate;
	}

	public void setCaDueDate(Date caDueDate) {
		this.caDueDate = caDueDate;
	}

	public List<ValidationError> validate() {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		if (!DateUtils
				.compareDates(getCaDueDate(), eglcLegalcase.getCasedate())) {
			errors.add(new ValidationError("caDueDate",
					"caDueDate.less.casedate"));
		}
		if (!DateUtils.compareDates(getCaFilingdate(), eglcLegalcase
				.getCasedate())) {
			errors.add(new ValidationError("caFilingDate",
					"caFilingDate.less.casedate"));
		}
		if (!DateUtils.compareDates(getPwrDueDate(), eglcLegalcase
				.getCasedate())) {
			errors.add(new ValidationError("pwrDueDate",
					"pwrDueDate.less.casedate"));
		}
		if (!DateUtils.compareDates(getCaDueDate(), getPwrDueDate()))
			errors.add(new ValidationError("caDueDate",
					"caDueDate.greaterThan.pwrDueDate"));
		return errors;
	}

	public Date getPwrDueDate() {
		return pwrDueDate;
	}

	public void setPwrDueDate(Date pwrDueDate) {
		this.pwrDueDate = pwrDueDate;
	}

}