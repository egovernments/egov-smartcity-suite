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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import org.hibernate.annotations.Cascade;
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
import javax.validation.Valid;

import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGLC_HEARINGS")
@SequenceGenerator(name = Hearings.SEQ_EGLC_HEARINGS, sequenceName = Hearings.SEQ_EGLC_HEARINGS, allocationSize = 1)
public class Hearings extends AbstractAuditable {

    private static final long serialVersionUID = 1517694643078084884L;
    public static final String SEQ_EGLC_HEARINGS = "SEQ_EGLC_HEARINGS";

    @Id
    @GeneratedValue(generator = SEQ_EGLC_HEARINGS, strategy = GenerationType.SEQUENCE)
    private Long id;

    private Date hearingDate;
	
    @ManyToOne
    @Valid
    @JoinColumn(name = "legalcase", nullable = false)
    private LegalCase legalCase;

    @Column(name = "isstandingcounselpresent")
    private boolean isStandingCounselPresent;

    @Length(max = 128)
    @OptionalPattern(regex = LcmsConstants.mixedChar, message = "hearing.additionalLawyerName.text")
    private String additionalLawyers;

    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY,mappedBy="hearing", orphanRemoval = true)
    Set<EmployeeHearing> employeeHearingList = new HashSet<EmployeeHearing>(0);
    
    @Transient
    List<EmployeeHearing> positionTemplList = new ArrayList<EmployeeHearing>();
    
    @Transient
    List<EmployeeHearing> positionEditHearinglList = new ArrayList<EmployeeHearing>();
    
    @Length(max = 1024)
    private String hearingOutcome;

    private boolean isSeniorStandingCounselPresent;

    @Length(max = 1024)
    @Column(name = "purposeofhearing")
    private String purposeofHearings;

    @ManyToOne
    @Valid
    @JoinColumn(name = "STATUS", nullable = false)
    private EgwStatus status;

    @Length(max = 50)
    private String referenceNumber;

    /*
     * public void addEmployee(final PersonalInformation empObj) {
     * eglcEmployeehearings.add(empObj); }
     */

    public Date getHearingDate() {
        return hearingDate;
    }

    public void setHearingDate(final Date hearingDate) {
        this.hearingDate = hearingDate;
    }

    public LegalCase getLegalCase() {
        return legalCase;
    }

    public void setLegalCase(final LegalCase legalCase) {
        this.legalCase = legalCase;
    }

    public String getAdditionalLawyers() {
        return additionalLawyers;
    }

    public void setAdditionalLawyers(final String additionalLawyers) {
        this.additionalLawyers = additionalLawyers;
    }

    /*
     * public Set<PersonalInformation> getEglcEmployeehearings() { return
     * eglcEmployeehearings; } public void setEglcEmployeehearings(final
     * Set<PersonalInformation> eglcEmployeehearings) {
     * this.eglcEmployeehearings = eglcEmployeehearings; }
     */

    public String getHearingOutcome() {
        return hearingOutcome;
    }

    public void setHearingOutcome(final String hearingOutcome) {
        this.hearingOutcome = hearingOutcome;
    }

    public boolean getIsSeniorStandingcounselpresent() {
        return isSeniorStandingCounselPresent;
    }

    public void setIsSeniorStandingcounselpresent(final boolean isSeniorStandingcounselpresent) {
        isSeniorStandingCounselPresent = isSeniorStandingcounselpresent;
    }

    public String getPurposeofHearings() {
        return purposeofHearings;
    }

    public void setPurposeofHearings(final String purposeofHearings) {
        this.purposeofHearings = purposeofHearings;
    }

    public Date getCaDueDate() {
        Date caDueDate = null;
        for (final Pwr pwr : getLegalCase().getPwrList())
            caDueDate = pwr.getCaDueDate();
        return caDueDate;
    }

    public List<ValidationError> validate() {
        final List<ValidationError> errors = new ArrayList<ValidationError>();
        if (getHearingDate() != null) {

            if (getCaDueDate() != null && !DateUtils.compareDates(getHearingDate(), getCaDueDate()))
                errors.add(new ValidationError("hearingDate", "hearingDate.greaterThan.caDueDate"));
            if (legalCase.getCaseReceivingDate() != null
                    && !DateUtils.compareDates(getHearingDate(), legalCase.getCaseReceivingDate()))
                errors.add(new ValidationError("hearingDate", "hearingDate.greaterThan.caseReceivingDate"));
            if (!DateUtils.compareDates(getHearingDate(), legalCase.getCaseDate()))
                errors.add(new ValidationError("hearingDate", "hearingDate.greaterThan.caseDate"));

        }
        return errors;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(final EgwStatus egwStatus) {
        status = egwStatus;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(final String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public boolean getIsStandingCounselPresent() {
        return isStandingCounselPresent;
    }

    public void setIsStandingCounselPresent(final boolean isStandingCounselPresent) {
        this.isStandingCounselPresent = isStandingCounselPresent;
    }

    public void setSeniorStandingCounselPresent(final boolean isSeniorStandingCounselPresent) {
        this.isSeniorStandingCounselPresent = isSeniorStandingCounselPresent;
    }

    public void setStandingCounselPresent(final boolean isStandingCounselPresent) {
        this.isStandingCounselPresent = isStandingCounselPresent;
    }

    public List<EmployeeHearing> getTempEmplyeeHearing() {
         final List<EmployeeHearing> tempList = new ArrayList<EmployeeHearing>();
        for (final EmployeeHearing temp : employeeHearingList)
           tempList.add(temp);
        return tempList;

    }
	

	public Set<EmployeeHearing> getEmployeeHearingList() {
		return employeeHearingList;
	}

	public void setEmployeeHearingList(Set<EmployeeHearing> employeeHearingList) {
		this.employeeHearingList = employeeHearingList;
	}

	public List<EmployeeHearing> getPositionTemplList() {
		return positionTemplList;
	}

	public void setPositionTemplList(List<EmployeeHearing> positionTemplList) {
		this.positionTemplList = positionTemplList;
	}

	public List<EmployeeHearing> getPositionEditHearinglList() {
		return positionEditHearinglList;
	}

	public void setPositionEditHearinglList(List<EmployeeHearing> positionEditHearinglList) {
		this.positionEditHearinglList = positionEditHearinglList;
	}

}