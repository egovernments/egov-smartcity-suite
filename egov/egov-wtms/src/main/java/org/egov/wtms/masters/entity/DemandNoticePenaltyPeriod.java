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
package org.egov.wtms.masters.entity;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.wtms.masters.entity.enums.DemandNoticeIssueFrequency;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "egwtr_demandnotice_penalty_period")
@SequenceGenerator(name = DemandNoticePenaltyPeriod.SEQ_DEMANDNOTICEPENALTYPERIOD, sequenceName = DemandNoticePenaltyPeriod.SEQ_DEMANDNOTICEPENALTYPERIOD, allocationSize = 1)
public class DemandNoticePenaltyPeriod extends AbstractAuditable {

    private static final long serialVersionUID = 7262414027940915738L;
    public static final String SEQ_DEMANDNOTICEPENALTYPERIOD = "SEQ_EGWTR_DEMANDNOTICE_PENALTY_PERIOD";

    @Id
    @GeneratedValue(generator = SEQ_DEMANDNOTICEPENALTYPERIOD, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private DemandNoticeIssueFrequency issueOfDamendNotice;

    private Integer penaltyPeriod;

    @Column(name = "min_con_holding_months")
    private Integer minConnectionHoldingMonths;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public DemandNoticeIssueFrequency getIssueOfDamendNotice() {
        return issueOfDamendNotice;
    }

    public void setIssueOfDamendNotice(final DemandNoticeIssueFrequency issueOfDamendNotice) {
        this.issueOfDamendNotice = issueOfDamendNotice;
    }

    public Integer getPenaltyPeriod() {
        return penaltyPeriod;
    }

    public void setPenaltyPeriod(final Integer penaltyPeriod) {
        this.penaltyPeriod = penaltyPeriod;
    }

    public Integer getMinConnectionHoldingMonths() {
        return minConnectionHoldingMonths;
    }

    public void setMinConnectionHoldingMonths(final Integer minConnectionHoldingMonths) {
        this.minConnectionHoldingMonths = minConnectionHoldingMonths;
    }

}