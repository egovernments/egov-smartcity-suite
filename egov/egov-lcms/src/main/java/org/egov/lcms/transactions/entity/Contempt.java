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

import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGLC_CONTEMPT")
@SequenceGenerator(name = Contempt.SEQ_EGLC_CONTEMPT, sequenceName = Contempt.SEQ_EGLC_CONTEMPT, allocationSize = 1)
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
    @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate") })
public class Contempt extends AbstractAuditable{
    private static final long serialVersionUID = 1517694643078084884L;
    public static final String SEQ_EGLC_CONTEMPT = "SEQ_EGLC_CONTEMPT";

    @Id
    @GeneratedValue(generator = SEQ_EGLC_CONTEMPT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "judgmentimpl")
    @Audited
    private JudgmentImpl judgmentImpl;

    @Length(max = 50)
    @NotNull
    @Column(name = "canumber")
    @Audited
    private String caNumber;

    @Temporal(TemporalType.DATE)
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT)
    @Column(name = "receivingdate")
    @Audited
    private Date receivingDate;

    @Column(name = "iscommapprrequired")
    @Audited
    private Boolean iscommapprRequired = false;

    @Temporal(TemporalType.DATE)
    @Column(name = "commappdate")
    @Audited
    private Date commappDate;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public JudgmentImpl getJudgmentImpl() {
        return judgmentImpl;
    }

    public void setJudgmentImpl(final JudgmentImpl judgmentImpl) {
        this.judgmentImpl = judgmentImpl;
    }

    public boolean getIscommapprRequired() {
        return iscommapprRequired;
    }

    public void setIscommapprRequired(final boolean iscommapprRequired) {
        this.iscommapprRequired = iscommapprRequired;
    }

    public Date getCommappDate() {
        return commappDate;
    }

    public void setCommappDate(final Date commappDate) {
        this.commappDate = commappDate;
    }

    public String getCaNumber() {
        return caNumber;
    }

    public void setCaNumber(final String caNumber) {
        this.caNumber = caNumber;
    }

    public Date getReceivingDate() {
        return receivingDate;
    }

    public void setReceivingDate(final Date receivingDate) {
        this.receivingDate = receivingDate;
    }

    public List<ValidationError> validate() {
        final List<ValidationError> errors = new ArrayList<ValidationError>();
        if (getReceivingDate() != null) {
            if (!DateUtils.compareDates(getReceivingDate(), getJudgmentImpl().getJudgment().getOrderDate()))
                errors.add(new ValidationError("receivingDate", "receivingDate.less.orderDate"));
            if (!DateUtils.compareDates(getCommappDate(), getReceivingDate()))
                errors.add(new ValidationError("receivingDate", "commappDate.greaterThan.receivingDate"));
        }
        return errors;
    }

}