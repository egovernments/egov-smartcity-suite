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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.utils.LcmsConstants;
import org.hibernate.validator.constraints.Length;

/**
 * Appeal entity.
 *
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EGLC_APPEAL")
@SequenceGenerator(name = Appeal.SEQ_EGLC_APPEAL, sequenceName = Appeal.SEQ_EGLC_APPEAL, allocationSize = 1)
public class Appeal extends AbstractAuditable {

    private static final long serialVersionUID = 1517694643078084884L;
    public static final String SEQ_EGLC_APPEAL = "SEQ_EGLC_APPEAL";

    @Id
    @GeneratedValue(generator = SEQ_EGLC_APPEAL, strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "JUDGMENTIMPL")
    private Judgmentimpl judgmentimpl1;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "STATUS")
    private EgwStatus egwStatus;
    @Required(message = "srnumber.null")
    @Length(max = 50, message = "srnumber.length")
    @OptionalPattern(regex = LcmsConstants.alphaNumeric, message = "srnumber.alpha")
    private String srnumber;
    @Required(message = "appealfiledon.null")
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.appeal.date")
    private Date appealfiledon;
    @Required(message = "appealfiledby.null")
    @Length(max = 100, message = "appealfiledby.length")
    @OptionalPattern(regex = LcmsConstants.alphaNumeric, message = "appealfiledby.alpha")
    private String appealfiledby;

    public EgwStatus getEgwStatus() {
        return egwStatus;
    }

    public void setEgwStatus(final EgwStatus egwStatus) {
        this.egwStatus = egwStatus;
    }

    public String getSrnumber() {
        return srnumber;
    }

    public void setSrnumber(final String srnumber) {
        this.srnumber = srnumber;
    }

    public Date getAppealfiledon() {
        return appealfiledon;
    }

    public void setAppealfiledon(final Date appealfiledon) {
        this.appealfiledon = appealfiledon;
    }

    public String getAppealfiledby() {
        return appealfiledby;
    }

    public void setAppealfiledby(final String appealfiledby) {
        this.appealfiledby = appealfiledby;
    }

   
    public Judgmentimpl getJudgmentimpl1() {
        return judgmentimpl1;
    }

    public void setJudgmentimpl1(Judgmentimpl judgmentimpl1) {
        this.judgmentimpl1 = judgmentimpl1;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public List<ValidationError> validate() {
        final List<ValidationError> errors = new ArrayList<ValidationError>();
        if (getAppealfiledon() != null
                && !DateUtils.compareDates(getAppealfiledon(), getJudgmentimpl1().getEglcJudgment().getOrderDate()))
            errors.add(new ValidationError("appealfiledon", "appealfiledon.less.orderDate"));
        return errors;
    }
}