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
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.EgwStatus;
import org.egov.commons.Functionary;
import org.egov.infra.persistence.validator.annotation.CompareDates;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.lcms.masters.entity.CasetypeMaster;
import org.egov.lcms.masters.entity.CourtMaster;
import org.egov.lcms.masters.entity.PetitiontypeMaster;
import org.egov.lcms.utils.LcmsConstants;
import org.egov.pims.commons.Position;
import org.hibernate.validator.constraints.Length;

@CompareDates(fromDate = "caseReceivingDate", toDate = "casedate", dateFormat = "dd/MM/yyyy", message = "caseReceivingDate.greaterThan.casedate")
@Unique(fields = { "casenumber", "lcnumber" }, id = "id", tableName = "EGLC_LEGALCASE", columnName = { "CASENUMBER",
		"LCNUMBER" }, message = "casenumber.name.isunique")
public class Legalcase extends BaseModel {
	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;

	@DateFormat(message = "invalid.fieldvalue.model.nextDate")
	private Date nextDate;
	@Required(message = "case.sectionNumber.null")
	private Functionary functionary;
	@Required(message = "case.casetype.null")
	private CasetypeMaster casetypeMaster;
	@Required(message = "case.court.null")
	private CourtMaster courtMaster;
	private EgwStatus egwStatus;
	@Required(message = "case.petitiontype.null")
	private PetitiontypeMaster petitiontypeMaster;
	@Required(message = "case.number.null")
	@Length(max = 50, message = "casenumber.length")
	@OptionalPattern(regex = LcmsConstants.caseNumberRegx, message = "case.number.alphanumeric")
	private String casenumber;
	@Required(message = "case.casedate.null")
	@DateFormat(message = "invalid.fieldvalue.model.casedate")
	@ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.case.date")
	private Date casedate;
	@Required(message = "case.title.null")
	@Length(max = 1024, message = "casetitle.length")
	private String casetitle;
	@Length(max = 50, message = "appealnum.length")
	private String appealnum;
	@Length(max = 1024, message = "remarks.length")
	private String remarks;
	@DateFormat(message = "invalid.fieldvalue.model.caseReceivingDate")
	@ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.caseReceivingDate.date")
	private Date caseReceivingDate;
	private boolean isfiledbycorporation;
	@OptionalPattern(regex = LcmsConstants.alphaNumericwithSlashes, message = "case.lcnumber.invalid")
	@Length(max = 50, message = "lcnumber.length")
	private String lcnumber;
	@Required(message = "case.prayer.null")
	@Length(max = 1024, message = "prayer.length")
	private String prayer;
	private boolean isSenioradvrequired;
	private Long assigntoIdboundary;
	private Set<Judgment> eglcJudgments = new HashSet<Judgment>(0);
	private Set<Pwr> eglcPwrs = new HashSet<Pwr>(0);
	private Set<Lcinterimorder> eglcLcinterimorders = new HashSet<Lcinterimorder>(0);
	private Set<Contempt> eglcContempts = new HashSet<Contempt>(0);
	private Set<BipartisanDetails> bipartisanDetails = new LinkedHashSet<BipartisanDetails>(0);
	private Set<LegalcaseAdvocate> eglcLegalcaseAdvocates = new LinkedHashSet<LegalcaseAdvocate>(0);
	private Set<Appeal> eglcAppeals = new HashSet<Appeal>(0);
	private Set<Hearings> hearings = new HashSet<Hearings>(0);
	@OptionalPattern(regex = LcmsConstants.mixedChar, message = "oppPartyAdvocate.alphanumeric")
	@Length(max = 128, message = "oppPartyAdvocate.length")
	private String oppPartyAdvocate;
	@OptionalPattern(regex = LcmsConstants.mixedChar, message = "representedby.alphanumeric")
	@Length(max = 256, message = "representedby.length")
	private String representedby;
	private String lcNumberType;
	private Set<LegalcaseDisposal> legalcaseDisposal = new HashSet<LegalcaseDisposal>(0);
	private Set<LegalcaseDepartment> legalcaseDepartment = new LinkedHashSet<LegalcaseDepartment>(0);
	private Set<Batchcase> batchCaseSet = new LinkedHashSet<Batchcase>(0);
	private Set<PaperBook> paperBookSet = new HashSet<PaperBook>(0);
	private Set<ProcessRegister> processRegisterSet = new HashSet<ProcessRegister>(0);
	private Long documentNum;
	@DateFormat(message = "invalid.fieldvalue.model.firstAppearenceDate")
	private Date firstAppearanceDate;
	private Set<LegalcaseMiscDetails> legalcaseMiscDetails = new HashSet<LegalcaseMiscDetails>(0);
	@DateFormat(message = "invalid.fieldvalue.model.previousDate")
	private Date previousDate;

	@DateFormat(message = "invalid.fieldvalue.model.petFirstAppDate")
	@ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.petFirstAppDate.date")
	private Date petFirstAppDate;
	@Length(max = 50, message = "stampNumber.length")
	private String stampNumber;

	public Long getDocumentNum() {

		return documentNum;
	}

	public void setDocumentNum(final Long documentNum) {
		this.documentNum = documentNum;
	}

	public String getOppPartyAdvocate() {
		return oppPartyAdvocate;
	}

	public String getRepresentedby() {
		return representedby;
	}

	public void setOppPartyAdvocate(final String oppPartyAdvocate) {
		this.oppPartyAdvocate = oppPartyAdvocate;
	}

	public void setRepresentedby(final String representedby) {
		this.representedby = representedby;
	}

	public Functionary getFunctionary() {

		return functionary;
	}

	public void setFunctionary(final Functionary functionary) {
		this.functionary = functionary;
	}

	public EgwStatus getEgwStatus() {
		return egwStatus;
	}

	public void setEgwStatus(final EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}

	public String getCasenumber() {
		return casenumber;
	}

	public void setCasenumber(final String casenumber) {
		this.casenumber = casenumber;
	}

	public Date getCasedate() {
		return casedate;
	}

	public void setCasedate(final Date casedate) {
		this.casedate = casedate;
	}

	public String getCasetitle() {
		return casetitle;
	}

	public void setCasetitle(final String casetitle) {
		this.casetitle = casetitle;
	}

	public String getAppealnum() {
		return appealnum;
	}

	public void setAppealnum(final String appealnum) {
		this.appealnum = appealnum;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(final String remarks) {
		this.remarks = remarks;
	}

	public Date getCaseReceivingDate() {
		return caseReceivingDate;
	}

	public void setCaseReceivingDate(final Date caseReceivingDate) {
		this.caseReceivingDate = caseReceivingDate;
	}

	public boolean getIsfiledbycorporation() {
		return isfiledbycorporation;
	}

	public void setIsfiledbycorporation(final boolean isfiledbycorporation) {
		this.isfiledbycorporation = isfiledbycorporation;
	}

	public String getLcnumber() {
		return lcnumber;
	}

	public void setLcnumber(final String lcnumber) {
		this.lcnumber = lcnumber;
	}

	public String getPrayer() {
		return prayer;
	}

	public void setPrayer(final String prayer) {
		this.prayer = prayer;
	}

	public boolean getIsSenioradvrequired() {
		return isSenioradvrequired;
	}

	public void setIsSenioradvrequired(final boolean isSenioradvrequired) {
		this.isSenioradvrequired = isSenioradvrequired;
	}

	public Long getAssigntoIdboundary() {
		return assigntoIdboundary;
	}

	public void setAssigntoIdboundary(final Long assigntoIdboundary) {
		this.assigntoIdboundary = assigntoIdboundary;
	}

	@Valid
	public Set<Pwr> getEglcPwrs() {
		return eglcPwrs;
	}

	public void setEglcPwrs(final Set<Pwr> eglcPwrs) {
		this.eglcPwrs = eglcPwrs;
	}

	public Set<Lcinterimorder> getEglcLcinterimorders() {
		return eglcLcinterimorders;
	}

	public void setEglcLcinterimorders(final Set<Lcinterimorder> eglcLcinterimorders) {
		this.eglcLcinterimorders = eglcLcinterimorders;
	}

	public Set<Contempt> getEglcContempts() {
		return eglcContempts;
	}

	public void setEglcContempts(final Set<Contempt> eglcContempts) {
		this.eglcContempts = eglcContempts;
	}

	@Valid
	public Set<LegalcaseAdvocate> getEglcLegalcaseAdvocates() {
		return eglcLegalcaseAdvocates;
	}

	public void setEglcLegalcaseAdvocates(final Set<LegalcaseAdvocate> eglcLegalcaseAdvocates) {
		this.eglcLegalcaseAdvocates = eglcLegalcaseAdvocates;
	}

	public Set<Appeal> getEglcAppeals() {
		return eglcAppeals;
	}

	public void setEglcAppeals(final Set<Appeal> eglcAppeals) {
		this.eglcAppeals = eglcAppeals;
	}

	public void addAdvocates(final LegalcaseAdvocate legaladv) {
		getEglcLegalcaseAdvocates().add(legaladv);
	}

	public void addPwrs(final Pwr pwr) {
		getEglcPwrs().add(pwr);
	}

	public void addJudgment(final Judgment judgment) {
		getEglcJudgments().add(judgment);
	}

	@Override
	public List<ValidationError> validate() {
		final List<ValidationError> errors = new ArrayList<ValidationError>();
		if (getPetFirstAppDate() != null)
			if (!DateUtils.compareDates(getPetFirstAppDate(), getCasedate()))
				errors.add(new ValidationError("petFirstAppDate", "petFirstAppDate.greaterThan.casedate"));
		if (getIsfiledbycorporation() == true && getStampNumber().length() == 0)
			errors.add(new ValidationError("stampNumber", "case.stampNumber.invalid"));
		for (final BipartisanDetails bipartisanDetails2 : getBipartisanDetails()) {
			final BipartisanDetails element = bipartisanDetails2;
			errors.addAll(element.validate());
		}
		for (final Pwr pwr : getEglcPwrs()) {
			final Pwr element = pwr;
			errors.addAll(element.validate());
		}
		for (final LegalcaseAdvocate legalcaseAdvocate : getEglcLegalcaseAdvocates()) {
			final LegalcaseAdvocate element = legalcaseAdvocate;
			errors.addAll(element.validate());
		}
		for (final Judgment judgment : getEglcJudgments()) {
			final Judgment element = judgment;
			errors.addAll(element.validate());
		}
		legalcaseDeptValidation(errors);
		reminderValidation(errors);

		int isResGovt = 0;
		for (final BipartisanDetails bipartisanDetails : getBipartisanDetails())
			if (bipartisanDetails.getIsrespondentgovernment())
				isResGovt++;
		if (getBipartisanDetails().size() == isResGovt)
			errors.add(new ValidationError("bipartisanDetails", "govtDept.govtDept"));

		batchCaseValidation(errors);

		for (final Batchcase batchCase : getBatchCaseSet())
			errors.addAll(batchCase.validate());

		return errors;
	}

	/**
	 * @param errors
	 */
	protected void legalcaseDeptValidation(final List<ValidationError> errors) {
		boolean isPrimaryDepartment = false;
		boolean deptPositionUniqueCheck = false;
		int i = 0;
		for (final LegalcaseDepartment legalcaseDept : getLegalcaseDepartment()) {
			int j = 0;
			for (final LegalcaseDepartment legalcaseDeptDuplicateCheck : getLegalcaseDepartment()) {
				if (i != j && legalcaseDept.getDepartment() != null && legalcaseDept.getPosition() != null
						&& legalcaseDeptDuplicateCheck.getDepartment() != null
						&& legalcaseDeptDuplicateCheck.getPosition() != null
						&& legalcaseDept.getDepartment().getName().concat(legalcaseDept.getPosition().getName())
								.equals(legalcaseDeptDuplicateCheck.getDepartment().getName()
										.concat(legalcaseDeptDuplicateCheck.getPosition().getName())))
					deptPositionUniqueCheck = true;
				j++;
			}
			if (legalcaseDept.getIsPrimaryDepartment())
				isPrimaryDepartment = true;
			errors.addAll(legalcaseDept.validate());

			i++;

		}
		if (!isPrimaryDepartment)
			errors.add(new ValidationError("legalcaseDept.isPrimaryDept", "isPrimary.is.null"));
		if (deptPositionUniqueCheck)
			errors.add(new ValidationError("legalcaseDept", "legalcaseDept.duplicate.exists"));
	}

	/**
	 * @param errors
	 *            Validation Check for Batch case:
	 */
	protected void batchCaseValidation(final List<ValidationError> errors) {
		boolean duplicateCaseNumberCheck = false;
		int i = 0;
		for (final Batchcase batchcase : getBatchCaseSet()) {
			/*
			 * Both the batch case number and primary case number should not be
			 * same
			 */
			if (StringUtils.isNotBlank(getCasenumber()) && StringUtils.isNotBlank(batchcase.getCasenumber())
					&& getCasenumber().equals(batchcase.getCasenumber())) {
				errors.add(new ValidationError("batchcaseSet", "both.casenumber.batchcasenumber"));
				break;
			}
			/*
			 * Duplicate batch case number should not exist
			 */
			int j = 0;
			for (final Batchcase casenumberDuplicateCheck : getBatchCaseSet()) {
				if (i != j && batchcase.getCasenumber() != null
						&& batchcase.getCasenumber().equals(casenumberDuplicateCheck.getCasenumber()))
					duplicateCaseNumberCheck = true;
				j++;
			}
			i++;
		}

		if (duplicateCaseNumberCheck)
			errors.add(new ValidationError("batchcaseSet", "batchcasenumber.duplicate.exists"));
	}

	@Valid
	public Set<Judgment> getEglcJudgments() {
		return eglcJudgments;
	}

	public void setEglcJudgments(final Set<Judgment> eglcJudgments) {
		this.eglcJudgments = eglcJudgments;
	}

	@Valid
	public Set<Hearings> getHearings() {
		return hearings;
	}

	public void setHearings(final Set<Hearings> hearings) {
		this.hearings = hearings;
	}

	public Set<LegalcaseDisposal> getLegalcaseDisposal() {
		return legalcaseDisposal;
	}

	public void setLegalcaseDisposal(final Set<LegalcaseDisposal> legalcaseDisposal) {
		this.legalcaseDisposal = legalcaseDisposal;
	}

	public String getLcNumberType() {
		return lcNumberType;
	}

	public void setLcNumberType(final String lcNumberType) {
		this.lcNumberType = lcNumberType;
	}

	public CasetypeMaster getCasetypeMaster() {
		return casetypeMaster;
	}

	public CourtMaster getCourtMaster() {
		return courtMaster;
	}

	public PetitiontypeMaster getPetitiontypeMaster() {
		return petitiontypeMaster;
	}

	public void setCasetypeMaster(final CasetypeMaster casetypeMaster) {
		this.casetypeMaster = casetypeMaster;
	}

	public void setCourtMaster(final CourtMaster courtMaster) {
		this.courtMaster = courtMaster;
	}

	public void setPetitiontypeMaster(final PetitiontypeMaster petitiontypeMaster) {
		this.petitiontypeMaster = petitiontypeMaster;
	}

	@Valid
	public Set<BipartisanDetails> getBipartisanDetails() {
		return bipartisanDetails;
	}

	public void setBipartisanDetails(final Set<BipartisanDetails> bipartisanDetails) {
		this.bipartisanDetails = bipartisanDetails;
	}

	public void addBipartisanDetails(final BipartisanDetails bipartisanDetails) {
		getBipartisanDetails().add(bipartisanDetails);
	}

	public void addLegalcaseDepartment(final LegalcaseDepartment legalcaseDepartment) {
		getLegalcaseDepartment().add(legalcaseDepartment);
	}

	@Valid
	public Set<LegalcaseDepartment> getLegalcaseDepartment() {
		return legalcaseDepartment;
	}

	public void setLegalcaseDepartment(final Set<LegalcaseDepartment> legalcaseDepartment) {
		this.legalcaseDepartment = legalcaseDepartment;
	}

	@Valid
	public Set<Batchcase> getBatchCaseSet() {
		return batchCaseSet;
	}

	public void setBatchCaseSet(final Set<Batchcase> batchCaseSet) {
		this.batchCaseSet = batchCaseSet;
	}

	public void addBatchCase(final Batchcase batchCase) {
		getBatchCaseSet().add(batchCase);
	}

	public Date getCaDueDate() {
		// iterate through the case's PWRs and return the first non-null date
		if (eglcPwrs != null)
			for (final Pwr pwr : eglcPwrs)
				if (pwr.getCaDueDate() != null)
					return pwr.getCaDueDate();
		return null;
	}

	public Date getPwrDate() {
		// iterate through the case's PWRs and return the first non-null date
		if (getEglcPwrs() != null)
			for (final Pwr pwr : getEglcPwrs())
				if (pwr.getPwrDueDate() != null)
					return pwr.getPwrDueDate();
		return null;
	}

	public List<BipartisanDetails> getPetitioners() {
		// iterate through this.getBipartisan and return only petitioners (based
		// on isRespondent=0)
		final List<BipartisanDetails> petitionerslist = new ArrayList<BipartisanDetails>();
		for (final BipartisanDetails bp : getBipartisanDetails())
			if (!bp.getIsrepondent())
				petitionerslist.add(bp);
		return petitionerslist;
	}

	public List<BipartisanDetails> getRespondents() {
		// iterate through this.getBipartisan and return only petitioners (based
		// on isRespondent=1)

		final List<BipartisanDetails> respondentlist = new ArrayList<BipartisanDetails>();
		for (final BipartisanDetails bp : getBipartisanDetails())
			if (bp.getIsrepondent())
				respondentlist.add(bp);
		return respondentlist;
	}

	public Judgment getJudgmentValue() {
		Judgment judgmentValue = null;
		for (final Judgment j : getEglcJudgments())
			if (!j.getSapAccepted())
				judgmentValue = j;
		return judgmentValue;
	}

	public String getDepartmentName() {
		for (final LegalcaseDepartment ld : legalcaseDepartment)
			if (ld != null && ld.getDepartment().getName() != null) {
				final String dep = ld.getDepartment().getName();

				return dep;
			}
		return null;
	}

	public String getDepartmentName(final Hashtable hs) {
		final ArrayList<String> arr = (ArrayList) hs.get(id);
		for (final LegalcaseDepartment ld : legalcaseDepartment)
			if (ld != null && ld.getDepartment().getName() != null) {
				final String dep = ld.getDepartment().getName();
				if (!arr.contains(dep))
					return dep;
			}
		return null;
	}

	public Position getPossition() {
		for (final LegalcaseDepartment ld : legalcaseDepartment)
			if (ld != null && ld.getPosition() != null)
				return ld.getPosition();
		return null;
	}

	public Position getPossition(final Hashtable hs) {
		final ArrayList<Position> arr = (ArrayList<Position>) hs.get(id);
		for (final LegalcaseDepartment ld : legalcaseDepartment)
			if (ld != null && ld.getPosition() != null) {
				final Position p = ld.getPosition();
				if (!arr.contains(p))
					return p;
			}
		return null;
	}

	public Set<PaperBook> getPaperBookSet() {
		return paperBookSet;
	}

	public void setPaperBookSet(final Set<PaperBook> paperBookSet) {
		this.paperBookSet = paperBookSet;
	}

	public void addPaperBook(final PaperBook paperBook) {
		getPaperBookSet().add(paperBook);
	}

	public Set<ProcessRegister> getProcessRegisterSet() {
		return processRegisterSet;
	}

	public void setProcessRegisterSet(final Set<ProcessRegister> processRegisterSet) {
		this.processRegisterSet = processRegisterSet;
	}

	public void addProcessRegister(final ProcessRegister processRegister) {
		getProcessRegisterSet().add(processRegister);
	}

	public Date getFirstAppearanceDate() {
		return firstAppearanceDate;
	}

	public void setFirstAppearanceDate(final Date firstAppearanceDate) {
		this.firstAppearanceDate = firstAppearanceDate;
	}

	public Set<LegalcaseMiscDetails> getLegalcaseMiscDetails() {
		return legalcaseMiscDetails;
	}

	public void setLegalcaseMiscDetails(final Set<LegalcaseMiscDetails> legalcaseMiscDetails) {
		this.legalcaseMiscDetails = legalcaseMiscDetails;
	}

	public Date getPreviousDate() {
		return previousDate;
	}

	public void setPreviousDate(final Date previousDate) {
		this.previousDate = previousDate;
	}

	public Date getNextDate() {
		return nextDate;
	}

	public void setNextDate(final Date nextDate) {
		this.nextDate = nextDate;
	}

	public Date getPetFirstAppDate() {
		return petFirstAppDate;
	}

	public void setPetFirstAppDate(final Date petFirstAppDate) {
		this.petFirstAppDate = petFirstAppDate;
	}

	public String getStampNumber() {
		return stampNumber;
	}

	public void setStampNumber(final String stampNumber) {
		this.stampNumber = stampNumber;
	}

	private void reminderValidation(final List<ValidationError> errors) {
		for (final LegalcaseDepartment legalcaseDept : getLegalcaseDepartment())
			for (final Reminder reminder : legalcaseDept.getLegalcaseReminders())
				errors.addAll(reminder.validate());
	}

}