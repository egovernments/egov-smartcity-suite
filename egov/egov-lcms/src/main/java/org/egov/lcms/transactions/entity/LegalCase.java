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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.masters.entity.CaseTypeMaster;
import org.egov.lcms.masters.entity.CourtMaster;
import org.egov.lcms.masters.entity.PetitionTypeMaster;
import org.egov.lcms.masters.entity.enums.LCNumberType;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.egov.pims.commons.Position;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGLC_LEGALCASE")
// @CompareDates(fromDate = "caseReceivingDate", toDate = "caseDate", dateFormat
// = "dd/MM/yyyy", message = "fgfgf ggffg date")
@Unique(fields = { "caseNumber", "lcNumber" }, id = "id", tableName = "EGLC_LEGALCASE", columnName = { "CASENUMBER",
        "LCNUMBER" }, message = "casenumber.name.isunique")
@SequenceGenerator(name = LegalCase.SEQ_LEGALCASE_TYPE, sequenceName = LegalCase.SEQ_LEGALCASE_TYPE, allocationSize = 1)
public class LegalCase extends AbstractAuditable {
    private static final long serialVersionUID = 1517694643078084884L;
    public static final String SEQ_LEGALCASE_TYPE = "SEQ_EGLC_LEGALCASE";

    @Id
    @GeneratedValue(generator = SEQ_LEGALCASE_TYPE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @DateFormat(message = "invalid.fieldvalue.model.nextDate")
    private Date nextDate;
    @Required(message = "case.casetype.null")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "CASETYPE", nullable = false)
    private CaseTypeMaster caseTypeMaster;
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
    private PetitionTypeMaster petitionTypeMaster;
    @NotNull
    @Column(name = "casenumber")
    private String caseNumber;
    @Required(message = "case.casedate.null")
    @DateFormat(message = "invalid.fieldvalue.model.casedate")
    // @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT,
    // message = "invalid.case.date")
    @Column(name = "casedate")
    private Date caseDate;
    @Required(message = "case.title.null")
    @Length(max = 1024, message = "casetitle.length")
    @Column(name = "casetitle")
    private String caseTitle;
    @Length(max = 50, message = "appealnum.length")
    @Column(name = "appealnum")
    private String appealNum;
    @Length(max = 1024, message = "remarks.length")
    private String remarks;
    @DateFormat(message = "invalid.fieldvalue.model.caseReceivingDate")
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.caseReceivingDate.date")
    @Column(name = "casereceivingdate")
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
    private Boolean isSenioradvrequired = Boolean.FALSE;
    @Column(name = "assigntoIdboundary")
    private Long assigntoIdboundary;

    @Transient
    private List<BipartisanDetails> bipartisanDetailsBeanList = new ArrayList<BipartisanDetails>(0);
    
    @Transient
    private List<BipartisanDetails> bipartisanPetitionDetailsList = new ArrayList<BipartisanDetails>(0);

    @Transient
    private List<Judgment> judgmentsBeanList = new ArrayList<Judgment>(0);

    @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Judgment> judgment = new ArrayList<Judgment>(0);

    @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LegalCaseDocuments> legalCaseDocuments = new ArrayList<LegalCaseDocuments>(0);

    @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pwr> eglcPwrs = new ArrayList<Pwr>(0);

    @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LegalCaseInterimOrder> legalCaseInterimOrder = new ArrayList<LegalCaseInterimOrder>(0);

    @Transient
    private String wpYear;

    @Transient
    private String finwpYear;
    
    @OneToMany(mappedBy = "legalCase", cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval = true)
    private List<BipartisanDetails> bipartisanDetails=null;
    
    @OrderBy("id")
    @OneToMany(mappedBy = "legalCase", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)    
    private List<LegalCaseAdvocate> eglcLegalcaseAdvocates = new ArrayList<LegalCaseAdvocate>(0);

    @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
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
    @Enumerated(EnumType.STRING)
    private LCNumberType lcNumberType;
    @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LegalCaseDisposal> legalCaseDisposal = new ArrayList<LegalCaseDisposal>(0);

    @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY)
    private List<LegalCaseDepartment> legalCaseDepartment = new ArrayList<LegalCaseDepartment>(0);

    @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BatchCase> batchCaseSet = new ArrayList<BatchCase>(0);

    @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaperBook> paperBookSet = new ArrayList<PaperBook>(0);

    @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProcessRegister> processRegisterSet = new ArrayList<ProcessRegister>(0);

    @DateFormat(message = "invalid.fieldvalue.model.firstAppearenceDate")
    private Date casefirstappearancedate;

    @OneToMany(mappedBy = "legalCase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LegalCaseMiscDetails> legalCaseMiscDetails = new ArrayList<LegalCaseMiscDetails>(0);

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
        for (final LegalCaseAdvocate legalcaseAdvocate : getEglcLegalcaseAdvocates()) {
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
     * @param errors
     *            Validation Check for Batch case:
     */
    protected void batchCaseValidation(final List<ValidationError> errors) {
        Boolean duplicateCaseNumberCheck = false;
        int i = 0;
        for (final BatchCase batchcase : getBatchCaseSet()) {
            /*
             * Both the batch case number and primary case number should not be
             * same
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
    
    	List<BipartisanDetails>tempList=new ArrayList<BipartisanDetails>();
    	for(BipartisanDetails temp:bipartisanDetails)
    	{
    		if(!temp.getIsRepondent()){
    			tempList.add(temp);
    		}
    	}
    	Set<BipartisanDetails>tempset=new HashSet<BipartisanDetails>(tempList);
    	bipartisanPetitionDetailsList = new ArrayList<BipartisanDetails>(tempset);
    	return bipartisanPetitionDetailsList;
        
    }

    public List<BipartisanDetails> getRespondents() {
        // iterate through this.getBipartisan and return only petitioners (based
        // on isRespondent=1)
    	List<BipartisanDetails>tempList=new ArrayList<BipartisanDetails>();
    	for(BipartisanDetails temp:bipartisanDetails)
    	{
    		if(temp.getIsRepondent()){
    			tempList.add(temp);
    			
    		}
    	}
    	Set<BipartisanDetails>tempset=new HashSet<BipartisanDetails>(tempList);
    	bipartisanDetailsBeanList=new ArrayList<BipartisanDetails>(tempset);
        return bipartisanDetailsBeanList;
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

    /*
     * public List<Contempt> getEglcContempts() { return eglcContempts; } public
     * void setEglcContempts(final List<Contempt> eglcContempts) {
     * this.eglcContempts = eglcContempts; }
     */

    public List<LegalCaseInterimOrder> getLegalCaseInterimOrder() {
        return legalCaseInterimOrder;
    }

    public void setLegalCaseInterimOrder(final List<LegalCaseInterimOrder> legalCaseInterimOrder) {
        this.legalCaseInterimOrder = legalCaseInterimOrder;
    }

    public List<BipartisanDetails> getBipartisanDetails() {
    	
        return bipartisanDetails;
    }

    public void setBipartisanDetails(final List<BipartisanDetails> bipartisanDetails) {
    	if (this.bipartisanDetails == null) {
    		 this.bipartisanDetails = bipartisanDetails;
    	  } else {
    	    this.bipartisanDetails.retainAll(bipartisanDetails);
    	   this.bipartisanDetails.addAll(bipartisanDetails);
    	  }
      
    }

    public void addBipartisanDetails(BipartisanDetails bipartisanDetails)
    {
    	this.bipartisanDetails.add(bipartisanDetails);
    }
    public void removeBipartisanDetails(BipartisanDetails bipartisanDetails)
    {
    	this.bipartisanDetails.remove(bipartisanDetails);
    }
      
    public List<LegalCaseAdvocate> getEglcLegalcaseAdvocates() {
        return eglcLegalcaseAdvocates;
    }

    public void setEglcLegalcaseAdvocates(final List<LegalCaseAdvocate> eglcLegalcaseAdvocates) {
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

    public LCNumberType getLcNumberType() {
        return lcNumberType;
    }

    public void setLcNumberType(final LCNumberType lcNumberType) {
        this.lcNumberType = lcNumberType;
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

    public List<Pwr> getEglcPwrs() {
        return eglcPwrs;
    }

   
    public void setEglcPwrs(final List<Pwr> eglcPwrs) {
    	this.eglcPwrs.clear();
        if (eglcPwrs != null) {        
            this.eglcPwrs.addAll(eglcPwrs);
        }   
    }

    public void addEglcPwrs(Pwr eglcPwrs)
    {
    	this.eglcPwrs.add(eglcPwrs);
    }
    public void removeEglcPwrs(Pwr eglcPwrs)
    {
    	this.eglcPwrs.remove(eglcPwrs);
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

    public List<LegalCaseDocuments> getLegalCaseDocuments() {
        return legalCaseDocuments;
    }

    public void setLegalCaseDocuments(final List<LegalCaseDocuments> legalCaseDocuments) {
        this.legalCaseDocuments = legalCaseDocuments;
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
        if (legalCaseDepartment != null) {        
            this.legalCaseDepartment.addAll(legalCaseDepartment);
        }   
    }

    public void addEglcPwrs(LegalCaseDepartment legalCaseDepartment)
    {
    	this.legalCaseDepartment.add(legalCaseDepartment);
    }
    public void removeEglcPwrs(LegalCaseDepartment legalCaseDepartment)
    {
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

	public List<BipartisanDetails> getBipartisanDetailsBeanList() {
		return bipartisanDetailsBeanList;
	}

	public void setBipartisanDetailsBeanList(List<BipartisanDetails> bipartisanDetailsBeanList) {
		this.bipartisanDetailsBeanList = bipartisanDetailsBeanList;
	}

	public List<BipartisanDetails> getBipartisanPetitionDetailsList() {
		return bipartisanPetitionDetailsList;
	}

	public void setBipartisanPetitionDetailsList(List<BipartisanDetails> bipartisanPetitionDetailsList) {
		this.bipartisanPetitionDetailsList = bipartisanPetitionDetailsList;
	}

	
	

}