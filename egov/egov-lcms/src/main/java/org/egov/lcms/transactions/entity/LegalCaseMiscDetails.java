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

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.Required;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGLC_LEGALCASE_MISCDETAILS")
@SequenceGenerator(name = LegalCaseMiscDetails.SEQ_EGLC_LEGALCASE_MISCDETAILS, sequenceName = LegalCaseMiscDetails.SEQ_EGLC_LEGALCASE_MISCDETAILS, allocationSize = 1)
public class LegalCaseMiscDetails extends AbstractAuditable {

    private static final long serialVersionUID = 1517694643078084884L;
    public static final String SEQ_EGLC_LEGALCASE_MISCDETAILS = "SEQ_EGLC_LEGALCASE_MISCDETAILS";

    @Id
    @GeneratedValue(generator = SEQ_EGLC_LEGALCASE_MISCDETAILS, strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    @NotNull
    @Valid
    @JoinColumn(name = "legalcase", nullable = false)
    private LegalCase legalCase;
    @Required(message = "miscDetails.date.null")
    @DateFormat(message = "invalid.fieldvalue.model.miscDate")
    private Date miscDate;
    @Length(max = 50, message = "miscDetails.referencenumber.maxlength")
    private String referenceNumber;
    @Length(max = 1024, message = "miscDetails.remarks.maxlength")
    private String remarks;
    @ManyToOne
    @NotNull
    @Valid
    @JoinColumn(name = "STATUS", nullable = false)
    private EgwStatus status;

    public Date getMiscDate() {
        return miscDate;
    }

    public void setMiscDate(final Date miscDate) {
        this.miscDate = miscDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(final EgwStatus status) {
        this.status = status;
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

    public LegalCase getLegalCase() {
        return legalCase;
    }

    public void setLegalCase(final LegalCase legalCase) {
        this.legalCase = legalCase;
    }

}
