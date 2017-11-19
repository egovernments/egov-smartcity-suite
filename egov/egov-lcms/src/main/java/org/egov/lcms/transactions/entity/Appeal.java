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
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Length;

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
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "EGLC_APPEAL")
@SequenceGenerator(name = Appeal.SEQ_EGLC_APPEAL, sequenceName = Appeal.SEQ_EGLC_APPEAL, allocationSize = 1)
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
    @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate") })
public class Appeal extends AbstractAuditable {

    private static final long serialVersionUID = 1517694643078084884L;
    public static final String SEQ_EGLC_APPEAL = "SEQ_EGLC_APPEAL";

    @Id
    @GeneratedValue(generator = SEQ_EGLC_APPEAL, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "judgmentimpl")
    @Audited
    private JudgmentImpl judgmentImpl;

    @Length(max = 50)
    @NotNull
    @Column(name = "srnumber")
    @Audited
    private String srNumber;

    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT)
    @Temporal(TemporalType.DATE)
    @Column(name = "appealfiledon")
    @Audited
    private Date appealFiledOn;

    @Length(max = 100)
    @Column(name = "appealfiledby")
    @Audited
    private String appealFiledBy;

    @OneToMany(mappedBy = "appeal", fetch = FetchType.LAZY)
    @NotAudited
    private List<AppealDocuments> appealDocuments = new ArrayList<AppealDocuments>(0);

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getSrNumber() {
        return srNumber;
    }

    public void setSrNumber(final String srNumber) {
        this.srNumber = srNumber;
    }

    public Date getAppealFiledOn() {
        return appealFiledOn;
    }

    public void setAppealFiledOn(final Date appealFiledOn) {
        this.appealFiledOn = appealFiledOn;
    }

    public String getAppealFiledBy() {
        return appealFiledBy;
    }

    public void setAppealFiledBy(final String appealFiledBy) {
        this.appealFiledBy = appealFiledBy;
    }

    public JudgmentImpl getJudgmentImpl() {
        return judgmentImpl;
    }

    public void setJudgmentImpl(final JudgmentImpl judgmentImpl) {
        this.judgmentImpl = judgmentImpl;
    }

    public List<AppealDocuments> getAppealDocuments() {
        return appealDocuments;
    }

    public void setAppealDocuments(final List<AppealDocuments> appealDocuments) {
        this.appealDocuments = appealDocuments;
    }

    public List<ValidationError> validate() {
        final List<ValidationError> errors = new ArrayList<ValidationError>();
        if (getAppealFiledOn() != null
                && !DateUtils.compareDates(getAppealFiledOn(), getJudgmentImpl().getJudgment().getOrderDate()))
            errors.add(new ValidationError("appealfiledon", "appealfiledon.less.orderDate"));
        return errors;
    }

}