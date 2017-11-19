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
package org.egov.works.abstractestimate.entity;

import org.egov.commons.Fundsource;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.validation.exception.ValidationError;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "EGW_ESTIMATE_FINANCINGSOURCE")
@SequenceGenerator(name = FinancingSource.SEQ_EGW_ESTIMATEFINANCINGSOURCE, sequenceName = FinancingSource.SEQ_EGW_ESTIMATEFINANCINGSOURCE, allocationSize = 1)
public class FinancingSource extends AbstractAuditable {

    private static final long serialVersionUID = -2860972695192985495L;

    public static final String SEQ_EGW_ESTIMATEFINANCINGSOURCE = "SEQ_EGW_ESTIMATE_FINANCINGSOURCE";

    @Id
    @GeneratedValue(generator = SEQ_EGW_ESTIMATEFINANCINGSOURCE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Min(value = 0, message = "financingsource.percentage.not.negative")
    private double percentage;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fundSource")
    private Fundsource fundSource;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financialDetail")
    private FinancialDetail financialDetail;

    public FinancingSource() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(final double percentage) {
        this.percentage = percentage;
    }

    public Fundsource getFundSource() {
        return fundSource;
    }

    public void setFundSource(final Fundsource fundSource) {
        this.fundSource = fundSource;
    }

    public FinancialDetail getFinancialDetail() {
        return financialDetail;
    }

    public void setFinancialDetail(final FinancialDetail financialDetail) {
        this.financialDetail = financialDetail;
    }

    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();

        if (fundSource == null || fundSource.getCode() == null)
            validationErrors.add(new ValidationError("invalidpercentage", "financingsource.fundsource.null"));

        if (percentage <= 0.0 || percentage > 100)
            validationErrors.add(new ValidationError("invalidpercentage", "financingsource.invalid.percentage"));
        return validationErrors;
    }
}
