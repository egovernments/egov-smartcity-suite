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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.utils.constants.LcmsConstants;

/**
 * Pwr entity.
 *
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EGLC_PWR")
@SequenceGenerator(name = Pwr.SEQ_EGLC_1PWR, sequenceName = Pwr.SEQ_EGLC_1PWR, allocationSize = 1)
public class Pwr extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 1517694643078084884L;
    public static final String SEQ_EGLC_1PWR = "seq_eglc_pwr";

    @Id
    @GeneratedValue(generator = SEQ_EGLC_1PWR, strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "LEGALCASE", nullable = false)
    private Legalcase legalcase;
    @Transient
    private String uploadPwr;
    @DateFormat(message = "invalid.fieldvalue.caFilingdate")
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.cafiling.date")
    @Column(name="cafilingdate")
    private Date caFilingdate;
    @Transient
    private String uploadCa;
    @DateFormat(message = "invalid.fieldvalue.caDueDate")
    @Column(name="caduedate")
    private Date caDueDate;
    @DateFormat(message = "invalid.fieldvalue.pwrDueDate")
    @Column(name="pwrduedate")
    private Date pwrDueDate;

    public Legalcase getLegalcase() {
        return legalcase;
    }

    public void setLegalcase(final Legalcase legalcase) {
        this.legalcase = legalcase;
    }

    public String getUploadPwr() {
        return uploadPwr;
    }

    public void setUploadPwr(final String uploadPwr) {
        this.uploadPwr = uploadPwr;
    }

  

    public Date getCaFilingdate() {
        return caFilingdate;
    }

    public void setCaFilingdate(Date caFilingdate) {
        this.caFilingdate = caFilingdate;
    }

    public String getUploadCa() {
        return uploadCa;
    }

    public void setUploadCa(final String uploadCa) {
        this.uploadCa = uploadCa;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public Date getCaDueDate() {
        return caDueDate;
    }

    public void setCaDueDate(final Date caDueDate) {
        this.caDueDate = caDueDate;
    }

    public List<ValidationError> validate() {
        final List<ValidationError> errors = new ArrayList<ValidationError>();
        if (!DateUtils.compareDates(getCaDueDate(), legalcase.getCasedate()))
            errors.add(new ValidationError("caDueDate", "caDueDate.less.casedate"));
        if (!DateUtils.compareDates(getCaFilingdate(), legalcase.getCasedate()))
            errors.add(new ValidationError("caFilingDate", "caFilingDate.less.casedate"));
        if (!DateUtils.compareDates(getPwrDueDate(), legalcase.getCasedate()))
            errors.add(new ValidationError("pwrDueDate", "pwrDueDate.less.casedate"));
        if (!DateUtils.compareDates(getCaDueDate(), getPwrDueDate()))
            errors.add(new ValidationError("caDueDate", "caDueDate.greaterThan.pwrDueDate"));
        return errors;
    }

    public Date getPwrDueDate() {
        return pwrDueDate;
    }

    public void setPwrDueDate(final Date pwrDueDate) {
        this.pwrDueDate = pwrDueDate;
    }

}