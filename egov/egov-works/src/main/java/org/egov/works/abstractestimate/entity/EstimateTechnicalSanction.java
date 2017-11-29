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

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

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
import java.util.Date;

@Entity
@Table(name = "EGW_ESTIMATE_TECHNICALSANCTION")
@SequenceGenerator(name = EstimateTechnicalSanction.SEQ_EGW_ESTIMATE_TECHNICALSANCTION, sequenceName = EstimateTechnicalSanction.SEQ_EGW_ESTIMATE_TECHNICALSANCTION, allocationSize = 1)
public class EstimateTechnicalSanction extends AbstractAuditable {

    private static final long serialVersionUID = -3985903396885356703L;

    public static final String SEQ_EGW_ESTIMATE_TECHNICALSANCTION = "SEQ_EGW_ESTIMATE_TECHNICALSANCTION";

    @Id
    @GeneratedValue(generator = SEQ_EGW_ESTIMATE_TECHNICALSANCTION, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "abstractestimate", nullable = false)
    private AbstractEstimate abstractEstimate;

    @NotNull
    @SafeHtml
    @Length(max = 50)
    private String technicalSanctionNumber;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date technicalSanctionDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technicalSanctionBy")
    private User technicalSanctionBy;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public AbstractEstimate getAbstractEstimate() {
        return abstractEstimate;
    }

    public void setAbstractEstimate(final AbstractEstimate abstractEstimate) {
        this.abstractEstimate = abstractEstimate;
    }

    public String getTechnicalSanctionNumber() {
        return technicalSanctionNumber;
    }

    public void setTechnicalSanctionNumber(final String technicalSanctionNumber) {
        this.technicalSanctionNumber = technicalSanctionNumber;
    }

    public Date getTechnicalSanctionDate() {
        return technicalSanctionDate;
    }

    public void setTechnicalSanctionDate(final Date technicalSanctionDate) {
        this.technicalSanctionDate = technicalSanctionDate;
    }

    public User getTechnicalSanctionBy() {
        return technicalSanctionBy;
    }

    public void setTechnicalSanctionBy(final User technicalSanctionBy) {
        this.technicalSanctionBy = technicalSanctionBy;
    }

}
