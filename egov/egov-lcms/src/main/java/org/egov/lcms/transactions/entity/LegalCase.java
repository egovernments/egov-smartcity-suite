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
import java.util.List;
import java.util.Set;

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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.masters.entity.CaseTypeMaster;
import org.egov.lcms.masters.entity.CourtMaster;
import org.egov.lcms.masters.entity.PetitionTypeMaster;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.egov.pims.commons.Position;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGLC_LEGALCASE")
@SequenceGenerator(name = LegalCase.SEQ_LEGALCASE_TYPE, sequenceName = LegalCase.SEQ_LEGALCASE_TYPE, allocationSize = 1)
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate") })
public class LegalCase extends AbstractAuditable {
    private static final long serialVersionUID = 1517694643078084884L;
    public static final String SEQ_LEGALCASE_TYPE = "SEQ_EGLC_LEGALCASE";

    @Id
    @GeneratedValue(generator = SEQ_LEGALCASE_TYPE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Audited
    private Date nextDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "CASETYPE", nullable = false)
    @Audited
    private CaseTypeMaster caseTypeMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "COURT", nullable = false)
    @Audited
    private CourtMaster courtMaster;

    @ManyToOne
    @JoinColumn(name = "STATUS", nullable = false)
    @NotAudited
    private EgwStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "PETITIONTYPE", nullable = false)
    @Audited
    private PetitionTypeMaster petitionTypeMaster;

    @NotNull
    @Column(name = "casenumber", unique = true)
    @Audited
    private String caseNumber;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "casedate")
    @Audited
    private Date caseDate;

    @NotNull
    @Length(max = 1024)
    @Column(name = "casetitle")
    @Audited
    private String caseTitle;

    @Length(max = 50)
    @Column(name = "appealnum")
    @Audited
    private String appealNum;

    @Length(max = 1024)
    @Audited
    private String remarks;

    @Column(name = "casereceivingdate")
    @Temporal(TemporalType.DATE)
    @Audited
    private Date caseReceivingDate;

    @Audited
    private Boolean isFiledByCorporation;

    @Length(max = 50)
    @Column(name = "lcnumber")
    @Audited
    private String lcNumber;

    @NotNull
    @Length(max = 1024)
    @Audited
    private String prayer;

    @Column(name = "isSenioradvrequired")
    @Audited
    private Boolean isSenioradvrequired = Boolean.FALSE;

    @Column(name = "assigntoIdboundary")
    @Audited
    private Long assigntoIdboundary;

    @Length(max = 128)
    @Column(name = "oppPartyAdvocate")
    @Audited
    private String oppPartyAdvocate;

    @Length(max = 256)
    @Column(name = "representedby")
    @Audited
    private String representedby;

    @Temporal(TemporalType.DATE)
    @Column(name = "previousDate")
    @Audited
    private Date previousDate;

    @Column(name = "stampNumber")
    @Audited
    private String stampNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "officerincharge")
    private Position officerIncharge;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "noticedate")
    @Audited
    private Date noticeDate;

    @Temporal(TemporalType.DATE)
    @Audited
    private Date casefirstappearancedate;

    @Transient
    private String functionaryCode;

    @Transient
    private String wpYear;

    @Transient
    private String finwpYear;

    @Audited
    private String oldReferenceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportstatus")
    @Audited
    private ReportStatus reportStatus;

    @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Audited
    private List<Judgment> judgment = new ArrayList<Judgment>(0);

    @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY)
    @NotAudited
    private List<LegalCaseUploadDocuments> legalCaseUploadDocuments = new ArrayList<LegalCaseUploadDocuments>();

    @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Audited
    private final List<Pwr> pwrList = new ArrayList<Pwr>(0);

    @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Audited
    private final List<CounterAffidavit> counterAffidavits = new ArrayList<CounterAffidavit>(0);

    @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Audited
    private List<LegalCaseInterimOrder> legalCaseInterimOrder = new ArrayList<LegalCaseInterimOrder>(0);

    @Audited
    @OrderBy("name asc")
    @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BipartisanDetails> bipartisanDetails = new ArrayList<BipartisanDetails>(0);

    @OrderBy("id")
    @OneToMany(mappedBy = "legalCase", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Audited
    private List<LegalCaseAdvocate> legalCaseAdvocates = new ArrayList<LegalCaseAdvocate>(0);

    @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Audited
    private List<Hearings> hearings = new ArrayList<Hearings>(0);

    @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Audited
    private List<LegalCaseDisposal> legalCaseDisposal = new ArrayList<LegalCaseDisposal>(0);

    @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Audited
    private final List<LegalCaseDepartment> legalCaseDepartment = new ArrayList<LegalCaseDepartment>(0);

    @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BatchCase> batchCaseSet = new ArrayList<BatchCase>(0);

    // TODO:need to enable when we start work on PaperBook and ProcessRegister
    // object
    /*
     * @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true) private
     * List<PaperBook> paperBookSet = new ArrayList<PaperBook>(0);
     * @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true) private
     * List<ProcessRegister> processRegisterSet = new ArrayList<ProcessRegister>(0);
     */

    @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LegalCaseMiscDetails> legalCaseMiscDetails = new ArrayList<LegalCaseMiscDetails>(0);

    @Transient
    @Audited
    @OrderBy("name asc")
    private List<BipartisanDetails> bipartisanRespondentDetailsList = new ArrayList<BipartisanDetails>(0);

    @Transient
    @Audited
    @OrderBy("name asc")
    private List<BipartisanDetails> bipartisanPetitionerDetailsList = new ArrayList<BipartisanDetails>(0);

    @Transient
    private List<Judgment> judgmentsBeanList = new ArrayList<Judgment>(0);

    public List<ValidationError> validate() {
        final List<ValidationError> errors = new ArrayList<ValidationError>();
        if (getIsFiledByCorporation() == true && getStampNumber().length() == 0)
            errors.add(new ValidationError("stampNumber", "case.stampNumber.invalid"));
        for (final BipartisanDetails bipartisanDetails2 : getBipartisanDetails()) {
            final BipartisanDetails element = bipartisanDetails2;
            errors.addAll(element.validate());
        }
        for (final Pwr pwr : getPwrList()) {
            final Pwr element = pwr;
            errors.addAll(element.validate());
        }
        for (final LegalCaseAdvocate legalcaseAdvocate : getLegalCaseAdvocates()) {
            final LegalCaseAdvocate element = legalcaseAdvocate;
            errors.addAll(element.validate());
        }
        for (final Judgment judgment : getJudgment()) {
            final Judgment element = judgment;
            errors.addAll(element.validate());
        }
        legalcaseDeptValidation(errors);
        reminderValidation(errors);

        int isResGovt = 0;
        for (final BipartisanDetails bipartisanDetails : getBipartisanDetails())
            if (bipartisanDetails.getIsRespondentGovernment())
                isResGovt++;
        if (getBipartisanDetails().size() == isResGovt)
            errors.add(new ValidationError("bipartisanDetails", "govtDept.govtDept"));

        batchCaseValidation(errors);

        for (final BatchCase batchCase : getBatchCaseSet())
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
        for (final LegalCaseDepartment legalcaseDept : getLegalCaseDepartment()) {
            int j = 0;
            for (final LegalCaseDepartment legalcaseDeptDuplicateCheck : getLegalCaseDepartment()) {
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
     * @param errors Validation Check for Batch case:
     */
    protected void batchCaseValidation(final List<ValidationError> errors) {
        Boolean duplicateCaseNumberCheck = false;
        int i = 0;
        for (final BatchCase batchcase : getBatchCaseSet()) {
            /*
             * Both the batch case number and primary case number should not be same
             */
            if (StringUtils.isNotBlank(getCaseNumber()) && StringUtils.isNotBlank(batchcase.getCasenumber())
                    && getCaseNumber().equals(batchcase.getCasenumber())) {
                errors.add(new ValidationError("batchcaseSet", "both.casenumber.batchcasenumber"));
                break;
            }
            /*
             * Duplicate batch case number should not exist
             */
            int j = 0;
            for (final BatchCase casenumberDuplicateCheck : getBatchCaseSet()) {
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
        if (pwrList != null)
            for (final Pwr pwr : pwrList)
                if (pwr.getCaDueDate() != null)
                    return pwr.getCaDueDate();
        return null;
    }

    public Date getPwrDate() {
        // iterate through the case's PWRs and return the first non-null date
        if (getPwrList() != null)
            for (final Pwr pwr : getPwrList())
                if (pwr.getPwrDueDate() != null)
                    return pwr.getPwrDueDate();
        return null;
    }

    public List<BipartisanDetails> getPetitioners() {
        // iterate through this.getBipartisan and return only petitioners (based
        // on isRespondent=0)

        final List<BipartisanDetails> tempList = new ArrayList<BipartisanDetails>();
        for (final BipartisanDetails temp : bipartisanDetails)
            if (!temp.getIsRepondent())
                tempList.add(temp);
        final Set<BipartisanDetails> tempset = new HashSet<BipartisanDetails>(tempList);
        bipartisanPetitionerDetailsList = new ArrayList<BipartisanDetails>(tempset);
        return bipartisanPetitionerDetailsList;

    }

    public List<BipartisanDetails> getRespondents() {
        // iterate through this.getBipartisan and return only petitioners (based
        // on isRespondent=1)
        final List<BipartisanDetails> tempList = new ArrayList<BipartisanDetails>();
        for (final BipartisanDetails temp : bipartisanDetails)
            if (temp.getIsRepondent())
                tempList.add(temp);
        final Set<BipartisanDetails> tempset = new HashSet<BipartisanDetails>(tempList);
        bipartisanRespondentDetailsList = new ArrayList<BipartisanDetails>(tempset);
        return bipartisanRespondentDetailsList;
    }

    public String getRespondantNames() {
        final StringBuilder tempStr = new StringBuilder();
        for (final BipartisanDetails temp : bipartisanDetails)
            if (temp.getIsRepondent())
                if (tempStr.length() == 0)
                    tempStr.append(temp.getName());
                else
                    tempStr.append(LcmsConstants.APPENDSEPERATE).append(temp.getName());
        return tempStr.toString();
    }

    public String getPetitionersNames() {
        final StringBuilder tempStr = new StringBuilder();
        for (final BipartisanDetails temp : bipartisanDetails)
            if (!temp.getIsRepondent())
                if (tempStr.length() == 0)
                    tempStr.append(temp.getName());
                else
                    tempStr.append(LcmsConstants.APPENDSEPERATE).append(temp.getName());
        return tempStr.toString();
    }

    public Judgment getJudgmentValue() {
        Judgment judgmentValue = null;
        for (final Judgment j : getJudgment())
            if (!j.getSapAccepted())
                judgmentValue = j;
        return judgmentValue;
    }

    public String getDepartmentName() {
        for (final LegalCaseDepartment ld : legalCaseDepartment)
            if (ld != null && ld.getDepartment().getName() != null) {
                final String dep = ld.getDepartment().getName();

                return dep;
            }
        return null;
    }

    public String getDepartmentName(final Hashtable hs) {
        final ArrayList<String> arr = (ArrayList) hs.get(id);
        for (final LegalCaseDepartment ld : legalCaseDepartment)
            if (ld != null && ld.getDepartment().getName() != null) {
                final String dep = ld.getDepartment().getName();
                if (!arr.contains(dep))
                    return dep;
            }
        return null;
    }

    public Position getPossition() {
        for (final LegalCaseDepartment ld : legalCaseDepartment)
            if (ld != null && ld.getPosition() != null)
                return ld.getPosition();
        return null;
    }

    public Position getPossition(final Hashtable hs) {
        final ArrayList<Position> arr = (ArrayList<Position>) hs.get(id);
        for (final LegalCaseDepartment ld : legalCaseDepartment)
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

    public void setIsSenioradvrequired(final Boolean isSenioradvrequired) {
        this.isSenioradvrequired = isSenioradvrequired;
    }

    public String getCasetitle() {
        return caseTitle;
    }

    public void setCasetitle(final String casetitle) {
        caseTitle = casetitle;
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

    public List<LegalCaseInterimOrder> getLegalCaseInterimOrder() {
        return legalCaseInterimOrder;
    }

    public void setLegalCaseInterimOrder(final List<LegalCaseInterimOrder> legalCaseInterimOrder) {
        this.legalCaseInterimOrder = legalCaseInterimOrder;
    }

    public void addBipartisanDetails(final BipartisanDetails bipartisanDetails) {
        this.bipartisanDetails.add(bipartisanDetails);
    }

    public void removeBipartisanDetails(final BipartisanDetails bipartisanDetails) {
        this.bipartisanDetails.remove(bipartisanDetails);
    }

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

    public List<LegalCaseDisposal> getLegalcaseDisposal() {
        return legalCaseDisposal;
    }

    public void setLegalcaseDisposal(final List<LegalCaseDisposal> legalcaseDisposal) {
        legalCaseDisposal = legalcaseDisposal;
    }

    public List<BatchCase> getBatchCaseSet() {
        return batchCaseSet;
    }

    public void setBatchCaseSet(final List<BatchCase> batchCaseSet) {
        this.batchCaseSet = batchCaseSet;
    }

    /*
     * public List<PaperBook> getPaperBookSet() { return paperBookSet; } public void setPaperBookSet(final List<PaperBook>
     * paperBookSet) { this.paperBookSet = paperBookSet; } public List<ProcessRegister> getProcessRegisterSet() { return
     * processRegisterSet; } public void setProcessRegisterSet(final List<ProcessRegister> processRegisterSet) {
     * this.processRegisterSet = processRegisterSet; }
     */

    /*
     * public Long getDocumentNum() { return documentNum; } public void setDocumentNum(final Long documentNum) { this.documentNum
     * = documentNum; }
     */

    public List<BipartisanDetails> getBipartisanDetails() {
        return bipartisanDetails;
    }

    public void setBipartisanDetails(final List<BipartisanDetails> bipartisanDetails) {
        this.bipartisanDetails = bipartisanDetails;
    }

    public Date getCasefirstappearancedate() {
        return casefirstappearancedate;
    }

    public void setCasefirstappearancedate(final Date casefirstappearancedate) {
        this.casefirstappearancedate = casefirstappearancedate;
    }

    public Date getPreviousDate() {
        return previousDate;
    }

    public void setPreviousDate(final Date previousDate) {
        this.previousDate = previousDate;
    }

    /*
     * public Date getPetFirstAppDate() { return petFirstAppDate; } public void setPetFirstAppDate(final Date petFirstAppDate) {
     * this.petFirstAppDate = petFirstAppDate; }
     */

    public String getStampNumber() {
        return stampNumber;
    }

    public void setStampNumber(final String stampNumber) {
        this.stampNumber = stampNumber;
    }

    private void reminderValidation(final List<ValidationError> errors) {
        for (final LegalCaseDepartment legalcaseDept : getLegalCaseDepartment())
            for (final Reminder reminder : legalcaseDept.getLegalCaseReminders())
                errors.addAll(reminder.validate());
    }

    public List<Judgment> getJudgment() {
        return judgment;
    }

    public void setJudgment(final List<Judgment> judgment) {
        this.judgment = judgment;
    }

    public List<Pwr> getPwrList() {
        return pwrList;
    }

    public void setPwrList(final List<Pwr> pwrs) {
        pwrList.clear();
        if (pwrs != null)
            pwrList.addAll(pwrs);
    }

    public void addPwrList(final Pwr pwr) {
        pwrList.add(pwr);
    }

    public void removePwrList(final Pwr pwr) {
        pwrList.remove(pwr);
    }

    public List<CounterAffidavit> getCounterAffidavits() {
        return counterAffidavits;
    }

    public void setCounterAffidavits(final List<CounterAffidavit> counterAffidavitsList) {
        counterAffidavits.clear();
        if (counterAffidavits != null)
            counterAffidavits.addAll(counterAffidavitsList);
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

    public String getFinwpYear() {
        return finwpYear;
    }

    public void setFinwpYear(final String finwpYear) {
        this.finwpYear = finwpYear;
    }

    public CaseTypeMaster getCaseTypeMaster() {
        return caseTypeMaster;
    }

    public void setCaseTypeMaster(final CaseTypeMaster caseTypeMaster) {
        this.caseTypeMaster = caseTypeMaster;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(final String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public List<Judgment> getJudgmentsBeanList() {
        return judgmentsBeanList;
    }

    public void setJudgmentsBeanList(final List<Judgment> judgmentsBeanList) {
        this.judgmentsBeanList = judgmentsBeanList;
    }

    public List<LegalCaseUploadDocuments> getLegalCaseUploadDocuments() {
        return legalCaseUploadDocuments;
    }

    public void setLegalCaseUploadDocuments(final List<LegalCaseUploadDocuments> legalCaseUploadDocuments) {
        this.legalCaseUploadDocuments = legalCaseUploadDocuments;
    }

    public List<LegalCaseDisposal> getLegalCaseDisposal() {
        return legalCaseDisposal;
    }

    public void setLegalCaseDisposal(final List<LegalCaseDisposal> legalCaseDisposal) {
        this.legalCaseDisposal = legalCaseDisposal;
    }

    public List<LegalCaseDepartment> getLegalCaseDepartment() {
        return legalCaseDepartment;
    }

    public void setLegalCaseDepartment(final List<LegalCaseDepartment> legalCaseDepartment) {
        this.legalCaseDepartment.clear();
        if (legalCaseDepartment != null)
            this.legalCaseDepartment.addAll(legalCaseDepartment);
    }

    public void addLegalCaseDepartment(final LegalCaseDepartment legalCaseDepartment) {
        this.legalCaseDepartment.add(legalCaseDepartment);
    }

    public void removeLegalCaseDepartment(final LegalCaseDepartment legalCaseDepartment) {
        this.legalCaseDepartment.remove(legalCaseDepartment);
    }

    public List<LegalCaseMiscDetails> getLegalCaseMiscDetails() {
        return legalCaseMiscDetails;
    }

    public void setLegalCaseMiscDetails(final List<LegalCaseMiscDetails> legalCaseMiscDetails) {
        this.legalCaseMiscDetails = legalCaseMiscDetails;
    }

    public PetitionTypeMaster getPetitionTypeMaster() {
        return petitionTypeMaster;
    }

    public void setPetitionTypeMaster(final PetitionTypeMaster petitionTypeMaster) {
        this.petitionTypeMaster = petitionTypeMaster;
    }

    public List<BipartisanDetails> getBipartisanRespondentDetailsList() {
        return bipartisanRespondentDetailsList;
    }

    public void setBipartisanRespondentDetailsList(final List<BipartisanDetails> bipartisanRespondentDetailsList) {
        this.bipartisanRespondentDetailsList = bipartisanRespondentDetailsList;
    }

    public List<BipartisanDetails> getBipartisanPetitionerDetailsList() {
        return bipartisanPetitionerDetailsList;
    }

    public void setBipartisanPetitionerDetailsList(final List<BipartisanDetails> bipartisanPetitionerDetailsList) {
        this.bipartisanPetitionerDetailsList = bipartisanPetitionerDetailsList;
    }

    public Date getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(final Date noticeDate) {
        this.noticeDate = noticeDate;
    }

    public Boolean getIsFiledByCorporation() {
        return isFiledByCorporation;
    }

    public void setIsFiledByCorporation(final Boolean isFiledByCorporation) {
        this.isFiledByCorporation = isFiledByCorporation;
    }

    public List<LegalCaseAdvocate> getLegalCaseAdvocates() {
        return legalCaseAdvocates;
    }

    public void setLegalCaseAdvocates(final List<LegalCaseAdvocate> legalCaseAdvocates) {
        this.legalCaseAdvocates = legalCaseAdvocates;
    }

    public String getOldReferenceNumber() {
        return oldReferenceNumber;
    }

    public void setOldReferenceNumber(final String oldReferenceNumber) {
        this.oldReferenceNumber = oldReferenceNumber;
    }

    public ReportStatus getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(final ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    public Position getOfficerIncharge() {
        return officerIncharge;
    }

    public void setOfficerIncharge(Position officerIncharge) {
        this.officerIncharge = officerIncharge;
    }

}