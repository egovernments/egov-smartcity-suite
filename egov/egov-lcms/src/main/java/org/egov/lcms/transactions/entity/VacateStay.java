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
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "EGLC_VACATESTAY_PETITION")
@SequenceGenerator(name = VacateStay.SEQ_EGLC_VACATESTAY_PETITION, sequenceName = VacateStay.SEQ_EGLC_VACATESTAY_PETITION, allocationSize = 1)
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
    @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate") })
public class VacateStay extends AbstractAuditable {

    private static final long serialVersionUID = 1517694643078084884L;
    public static final String SEQ_EGLC_VACATESTAY_PETITION = "SEQ_EGLC_VACATESTAY_PETITION";

    @Id
    @GeneratedValue(generator = SEQ_EGLC_VACATESTAY_PETITION, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "lcinterimorder", nullable = false)
    @Audited
    private LegalCaseInterimOrder legalCaseInterimOrder;

    @Temporal(TemporalType.DATE)
    @Column(name = "receivedfromstandingcounsel")
    @Audited
    private Date vsReceivedFromStandingCounsel;

    @Temporal(TemporalType.DATE)
    @Column(name = "sendtostandingcounsel")
    @Audited
    private Date vsSendToStandingCounsel;

    @NotNull
    @Temporal(TemporalType.DATE)
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT)
    @Column(name = "petitionfiledon")
    @Audited
    private Date vsPetitionFiledOn;

    @Length(max = 1024)
    @Audited
    private String remarks;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public LegalCaseInterimOrder getLegalCaseInterimOrder() {
        return legalCaseInterimOrder;
    }

    public void setLegalCaseInterimOrder(final LegalCaseInterimOrder legalCaseInterimOrder) {
        this.legalCaseInterimOrder = legalCaseInterimOrder;
    }

    public Date getVsReceivedFromStandingCounsel() {
        return vsReceivedFromStandingCounsel;
    }

    public void setVsReceivedFromStandingCounsel(final Date vsReceivedFromStandingCounsel) {
        this.vsReceivedFromStandingCounsel = vsReceivedFromStandingCounsel;
    }

    public Date getVsSendToStandingCounsel() {
        return vsSendToStandingCounsel;
    }

    public void setVsSendToStandingCounsel(final Date vsSendToStandingCounsel) {
        this.vsSendToStandingCounsel = vsSendToStandingCounsel;
    }

    public Date getVsPetitionFiledOn() {
        return vsPetitionFiledOn;
    }

    public void setVsPetitionFiledOn(final Date vsPetitionFiledOn) {
        this.vsPetitionFiledOn = vsPetitionFiledOn;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public List<ValidationError> validate() {
        final List<ValidationError> errors = new ArrayList<ValidationError>();

        if (!DateUtils.compareDates(getVsReceivedFromStandingCounsel(), getLegalCaseInterimOrder().getIoDate()))
            errors.add(new ValidationError("iodate", "iodate.greaterThan.vsReceivedFromStandingCounsel"));

        if (!DateUtils.compareDates(getVsPetitionFiledOn(), getLegalCaseInterimOrder().getIoDate()))
            errors.add(new ValidationError("iodate", "iodate.greaterThan.petitionFiledOn"));

        if (!DateUtils.compareDates(getVsSendToStandingCounsel(), getLegalCaseInterimOrder().getIoDate()))
            errors.add(new ValidationError("iodate", "iodate.greaterThan.vsSendToStandingCounsel"));

        if (!DateUtils.compareDates(getVsReceivedFromStandingCounsel(), getVsSendToStandingCounsel()))
            errors.add(new ValidationError("vsReceivedFromStandingCounsel",
                    "vsReceivedFromStandingCounsel.greaterThan.vsSendToStandingCounsel"));

        if (!DateUtils.compareDates(getVsSendToStandingCounsel(), getVsPetitionFiledOn()))
            errors.add(new ValidationError("petitionFiledOn", "vsSendToStandingCounsel.greaterThan.petitionFiledOn"));

        return errors;
    }

}
