/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.lcms.transactions.entity;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "EGLC_PWR")
@SequenceGenerator(name = Pwr.SEQ_EGLC_PWR, sequenceName = Pwr.SEQ_EGLC_PWR, allocationSize = 1)
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate") })
public class Pwr extends AbstractAuditable {

    private static final long serialVersionUID = 1517694643078084884L;
    public static final String SEQ_EGLC_PWR = "seq_eglc_pwr";

    @Id
    @GeneratedValue(generator = SEQ_EGLC_PWR, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "legalcase", nullable = false)
    @Audited
    private LegalCase legalCase;

    @Temporal(TemporalType.DATE)
    @Column(name = "cafilingdate")
    @Audited
    private Date caFilingDate;

    @OneToMany(mappedBy = "pwr", fetch = FetchType.LAZY)
    @NotAudited
    private List<PwrDocuments> pwrDocuments = new ArrayList<PwrDocuments>(0);

    @Temporal(TemporalType.DATE)
    @Column(name = "caduedate")
    @Audited
    private Date caDueDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "pwrduedate")
    @Audited
    private Date pwrDueDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "pwrapprovaldate")
    @Audited
    private Date pwrApprovalDate;

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
        if (!DateUtils.compareDates(getCaDueDate(), legalCase.getCaseDate()))
            errors.add(new ValidationError("caDueDate", "caDueDate.less.casedate"));
        if (!DateUtils.compareDates(getCaFilingDate(), legalCase.getCaseDate()))
            errors.add(new ValidationError("caFilingDate", "caFilingDate.less.casedate"));
        if (!DateUtils.compareDates(getPwrDueDate(), legalCase.getCaseDate()))
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

    public LegalCase getLegalCase() {
        return legalCase;
    }

    public void setLegalCase(final LegalCase legalCase) {
        this.legalCase = legalCase;
    }

    public List<PwrDocuments> getPwrDocuments() {
        return pwrDocuments;
    }

    public void setPwrDocuments(final List<PwrDocuments> pwrDocuments) {
        this.pwrDocuments = pwrDocuments;
    }

    public Date getCaFilingDate() {
        return caFilingDate;
    }

    public void setCaFilingDate(final Date caFilingDate) {
        this.caFilingDate = caFilingDate;
    }

    public Date getPwrApprovalDate() {
        return pwrApprovalDate;
    }

    public void setPwrApprovalDate(final Date pwrApprovalDate) {
        this.pwrApprovalDate = pwrApprovalDate;
    }

}