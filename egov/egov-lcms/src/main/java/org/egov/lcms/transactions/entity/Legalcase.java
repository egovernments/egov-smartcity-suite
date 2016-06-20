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
import java.util.Hashtable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.EgwStatus;
import org.egov.commons.Functionary;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.masters.entity.CasetypeMaster;
import org.egov.lcms.masters.entity.CourtMaster;
import org.egov.lcms.masters.entity.PetitionTypeMaster;
import org.egov.lcms.utils.LcmsConstants;
import org.egov.pims.commons.Position;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGLC_LEGALCASE")
// @CompareDates(fromDate = "caseReceivingDate", toDate = "caseDate", dateFormat
// = "dd/MM/yyyy", message = "fgfgf ggffg date")
@Unique(fields = { "casenumber", "lcNumber" }, id = "id", tableName = "EGLC_LEGALCASE", columnName = { "CASENUMBER",
        "LCNUMBER" }, message = "casenumber.name.isunique")
@SequenceGenerator(name = Legalcase.SEQ_LEGALCASE_TYPE, sequenceName = Legalcase.SEQ_LEGALCASE_TYPE, allocationSize = 1)
public class Legalcase extends AbstractAuditable {
    private static final long serialVersionUID = 1517694643078084884L;
    public static final String SEQ_LEGALCASE_TYPE = "SEQ_EGLC_LEGALCASE";

    @Id
    @GeneratedValue(generator = SEQ_LEGALCASE_TYPE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @DateFormat(message = "invalid.fieldvalue.model.nextDate")
    private Date nextDate;
    // @Required(message = "case.sectionNumber.null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FUNCTIONARY", nullable = false)
    private Functionary functionary;
    @Required(message = "case.casetype.null")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "CASETYPE", nullable = false)
    private CasetypeMaster casetypeMaster;
    @Required(message = "case.court.null")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "COURT", nullable = false)
    private CourtMaster courtMaster;
    @ManyToOne
    @JoinColumn(name = "STATUS", nullable = false)
    private EgwStatus status;
    @Required(message = "case.petitiontype.null")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "PETITIONTYPE", nullable = false)
    private PetitionTypeMaster petitiontypeMaster;
    @Required(message = "case.number.null")
    @Length(max = 50, message = "casenumber.length")
    @OptionalPattern(regex = LcmsConstants.caseNumberRegx, message = "case.number.alphanumeric")
    private String casenumber;
    @Required(message = "case.casedate.null")
    @DateFormat(message = "invalid.fieldvalue.model.casedate")
    // @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT,
    // message = "invalid.case.date")
    private Date caseDate;
    @Required(message = "case.title.null")
    @Length(max = 1024, message = "casetitle.length")
    @Column(name = "casetitle")
    private String caseTitle;
    @Length(max = 50, message = "appealnum.length")
    @Column(name = "appealNum")
    private String appealNum;
    @Length(max = 1024, message = "remarks.length")
    private String remarks;
    @DateFormat(message = "invalid.fieldvalue.model.caseReceivingDate")
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.caseReceivingDate.date")
    @Column(name = "caseReceivingDate")
    private Date caseReceivingDate;
    private Boolean isfiledbycorporation;
    @OptionalPattern(regex = LcmsConstants.alphaNumericwithSlashes, message = "case.lcnumber.invalid")
    @Length(max = 50, message = "lcnumber.length")
    @Column(name = "lcnumber")
    private String lcNumber;
    @Required(message = "case.prayer.null")
    @Length(max = 1024, message = "prayer.length")
    private String prayer;
    @Column(name = "isSenioradvrequired")
    private Boolean isSenioradvrequired = true;
    @Column(name = "assigntoIdboundary")
    private Long assigntoIdboundary;

    @OneToMany(mappedBy = "legalcase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Judgment> eglcJudgments = new ArrayList<Judgment>(0);

    @OneToMany(mappedBy = "legalcase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pwr> eglcPwrs = new ArrayList<Pwr>(0);

    @OneToMany(mappedBy = "legalcase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lcinterimorder> eglcLcinterimorders = new ArrayList<Lcinterimorder>(0);

    @Transient
    private String wpYear;
    @OneToMany(mappedBy = "legalcase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BipartisanDetails> bipartisanDetails = new ArrayList<BipartisanDetails>(0);
    @OneToMany(mappedBy = "legalcase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LegalcaseAdvocate> eglcLegalcaseAdvocates = new ArrayList<LegalcaseAdvocate>(0);

    @OneToMany(mappedBy = "legalcase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hearings> hearings = new ArrayList<Hearings>(0);

    @OptionalPattern(regex = LcmsConstants.mixedChar, message = "oppPartyAdvocate.alphanumeric")
    @Length(max = 128, message = "oppPartyAdvocate.length")
    @Column(name = "oppPartyAdvocate")
    private String oppPartyAdvocate;
    @OptionalPattern(regex = LcmsConstants.mixedChar, message = "representedby.alphanumeric")
    @Length(max = 256, message = "representedby.length")
    @Column(name = "representedby")
    private String representedby;
    @Column(name = "lcNumberType")
    private String lcNumberType;
    @OneToMany(mappedBy = "legalcase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LegalcaseDisposal> legalcaseDisposal = new ArrayList<LegalcaseDisposal>(0);

    @OneToMany(mappedBy = "legalcase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LegalcaseDepartment> legalcaseDepartment = new ArrayList<LegalcaseDepartment>(0);

    @OneToMany(mappedBy = "legalcase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Batchcase> batchCaseSet = new ArrayList<Batchcase>(0);

    @OneToMany(mappedBy = "legalcase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaperBook> paperBookSet = new ArrayList<PaperBook>(0);

    @OneToMany(mappedBy = "legalcase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProcessRegister> processRegisterSet = new ArrayList<ProcessRegister>(0);

    @DateFormat(message = "invalid.fieldvalue.model.firstAppearenceDate")
    private Date casefirstappearancedate;

    @OneToMany(mappedBy = "legalcase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LegalcaseMiscDetails> legalcaseMiscDetails = new ArrayList<LegalcaseMiscDetails>(0);

    @DateFormat(message = "invalid.fieldvalue.model.previousDate")
    @Column(name = "previousDate")
    private Date previousDate;

    @Length(max = 50, message = "stampNumber.length")
    @Column(name = "stampNumber")
    private String stampNumber;

    @Transient
    private String functionaryCode;

    public List<ValidationError> validate() {
        final List<ValidationError> errors = new ArrayList<ValidationError>();
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
        Boolean isPrimaryDepartment = false;
        Boolean deptPositionUniqueCheck = false;
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
        Boolean duplicateCaseNumberCheck = false;
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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public Date getNextDate() {
        return nextDate;
    }

    public void setNextDate(final Date nextDate) {
        this.nextDate = nextDate;
    }

    public Functionary getFunctionary() {
        return functionary;
    }

    public void setFunctionary(final Functionary functionary) {
        this.functionary = functionary;
    }

    public CasetypeMaster getCasetypeMaster() {
        return casetypeMaster;
    }

    public void setCasetypeMaster(final CasetypeMaster casetypeMaster) {
        this.casetypeMaster = casetypeMaster;
    }

    public CourtMaster getCourtMaster() {
        return courtMaster;
    }

    public void setCourtMaster(final CourtMaster courtMaster) {
        this.courtMaster = courtMaster;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(final EgwStatus status) {
        this.status = status;
    }

    public PetitionTypeMaster getPetitiontypeMaster() {
        return petitiontypeMaster;
    }

    public void setPetitiontypeMaster(final PetitionTypeMaster petitiontypeMaster) {
        this.petitiontypeMaster = petitiontypeMaster;
    }

    public void setIsSenioradvrequired(final Boolean isSenioradvrequired) {
        this.isSenioradvrequired = isSenioradvrequired;
    }

    public String getCasenumber() {
        return casenumber;
    }

    public void setCasenumber(final String casenumber) {
        this.casenumber = casenumber;
    }

    public Date getCasedate() {
        return caseDate;
    }

    public void setCasedate(final Date casedate) {
        caseDate = casedate;
    }

    public String getCasetitle() {
        return caseTitle;
    }

    public void setCasetitle(final String casetitle) {
        caseTitle = casetitle;
    }

    public String getAppealnum() {
        return appealNum;
    }

    public void setAppealnum(final String appealnum) {
        appealNum = appealnum;
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

    public Boolean getIsfiledbycorporation() {
        return isfiledbycorporation;
    }

    public void setIsfiledbycorporation(final Boolean isfiledbycorporation) {
        this.isfiledbycorporation = isfiledbycorporation;
    }

    public String getLcnumber() {
        return lcNumber;
    }

    public void setLcnumber(final String lcnumber) {
        lcNumber = lcnumber;
    }

    public String getPrayer() {
        return prayer;
    }

    public void setPrayer(final String prayer) {
        this.prayer = prayer;
    }

    public Boolean getIsSenioradvrequired() {
        return isSenioradvrequired;
    }

    public void setSenioradvrequired(final Boolean isSenioradvrequired) {
        this.isSenioradvrequired = isSenioradvrequired;
    }

    public Long getAssigntoIdboundary() {
        return assigntoIdboundary;
    }

    public void setAssigntoIdboundary(final Long assigntoIdboundary) {
        this.assigntoIdboundary = assigntoIdboundary;
    }

    public List<Lcinterimorder> getEglcLcinterimorders() {
        return eglcLcinterimorders;
    }

    public void setEglcLcinterimorders(final List<Lcinterimorder> eglcLcinterimorders) {
        this.eglcLcinterimorders = eglcLcinterimorders;
    }

    /*
     * public List<Contempt> getEglcContempts() { return eglcContempts; } public
     * void setEglcContempts(final List<Contempt> eglcContempts) {
     * this.eglcContempts = eglcContempts; }
     */

    public List<BipartisanDetails> getBipartisanDetails() {
        return bipartisanDetails;
    }

    public void setBipartisanDetails(final List<BipartisanDetails> bipartisanDetails) {
        this.bipartisanDetails = bipartisanDetails;
    }

    public List<LegalcaseAdvocate> getEglcLegalcaseAdvocates() {
        return eglcLegalcaseAdvocates;
    }

    public void setEglcLegalcaseAdvocates(final List<LegalcaseAdvocate> eglcLegalcaseAdvocates) {
        this.eglcLegalcaseAdvocates = eglcLegalcaseAdvocates;
    }

    /*
     * public List<Appeal> getEglcAppeals() { return eglcAppeals; } public void
     * setEglcAppeals(final List<Appeal> eglcAppeals) { this.eglcAppeals =
     * eglcAppeals; }
     */

    public List<Hearings> getHearings() {
        return hearings;
    }

    public void setHearings(final List<Hearings> hearings) {
        this.hearings = hearings;
    }

    public String getOppPartyAdvocate() {
        return oppPartyAdvocate;
    }

    public void setOppPartyAdvocate(final String oppPartyAdvocate) {
        this.oppPartyAdvocate = oppPartyAdvocate;
    }

    public String getRepresentedby() {
        return representedby;
    }

    public void setRepresentedby(final String representedby) {
        this.representedby = representedby;
    }

    public String getLcNumberType() {
        return lcNumberType;
    }

    public void setLcNumberType(final String lcNumberType) {
        this.lcNumberType = lcNumberType;
    }

    public List<LegalcaseDisposal> getLegalcaseDisposal() {
        return legalcaseDisposal;
    }

    public void setLegalcaseDisposal(final List<LegalcaseDisposal> legalcaseDisposal) {
        this.legalcaseDisposal = legalcaseDisposal;
    }

    public List<LegalcaseDepartment> getLegalcaseDepartment() {
        return legalcaseDepartment;
    }

    public void setLegalcaseDepartment(final List<LegalcaseDepartment> legalcaseDepartment) {
        this.legalcaseDepartment = legalcaseDepartment;
    }

    public List<Batchcase> getBatchCaseSet() {
        return batchCaseSet;
    }

    public void setBatchCaseSet(final List<Batchcase> batchCaseSet) {
        this.batchCaseSet = batchCaseSet;
    }

    public List<PaperBook> getPaperBookSet() {
        return paperBookSet;
    }

    public void setPaperBookSet(final List<PaperBook> paperBookSet) {
        this.paperBookSet = paperBookSet;
    }

    public List<ProcessRegister> getProcessRegisterSet() {
        return processRegisterSet;
    }

    public void setProcessRegisterSet(final List<ProcessRegister> processRegisterSet) {
        this.processRegisterSet = processRegisterSet;
    }

    /*
     * public Long getDocumentNum() { return documentNum; } public void
     * setDocumentNum(final Long documentNum) { this.documentNum = documentNum;
     * }
     */

    public List<LegalcaseMiscDetails> getLegalcaseMiscDetails() {
        return legalcaseMiscDetails;
    }

    public Date getCasefirstappearancedate() {
        return casefirstappearancedate;
    }

    public void setCasefirstappearancedate(final Date casefirstappearancedate) {
        this.casefirstappearancedate = casefirstappearancedate;
    }

    public void setLegalcaseMiscDetails(final List<LegalcaseMiscDetails> legalcaseMiscDetails) {
        this.legalcaseMiscDetails = legalcaseMiscDetails;
    }

    public Date getPreviousDate() {
        return previousDate;
    }

    public void setPreviousDate(final Date previousDate) {
        this.previousDate = previousDate;
    }

    /*
     * public Date getPetFirstAppDate() { return petFirstAppDate; } public void
     * setPetFirstAppDate(final Date petFirstAppDate) { this.petFirstAppDate =
     * petFirstAppDate; }
     */

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

    public List<Judgment> getEglcJudgments() {
        return eglcJudgments;
    }

    public void setEglcJudgments(final List<Judgment> eglcJudgments) {
        this.eglcJudgments = eglcJudgments;
    }

    public List<Pwr> getEglcPwrs() {
        return eglcPwrs;
    }

    public void setEglcPwrs(final List<Pwr> eglcPwrs) {
        this.eglcPwrs = eglcPwrs;
    }

    public Date getCaseDate() {
        return caseDate;
    }

    public void setCaseDate(final Date caseDate) {
        this.caseDate = caseDate;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(final String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public String getAppealNum() {
        return appealNum;
    }

    public void setAppealNum(final String appealNum) {
        this.appealNum = appealNum;
    }

    public String getFunctionaryCode() {
        return functionaryCode;
    }

    public void setFunctionaryCode(final String functionaryCode) {
        this.functionaryCode = functionaryCode;
    }

    public String getLcNumber() {
        return lcNumber;
    }

    public void setLcNumber(final String lcNumber) {
        this.lcNumber = lcNumber;
    }

    public String getWpYear() {
        return wpYear;
    }

    public void setWpYear(final String wpYear) {
        this.wpYear = wpYear;
    }

}