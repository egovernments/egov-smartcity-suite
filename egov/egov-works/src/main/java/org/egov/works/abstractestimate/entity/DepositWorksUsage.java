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
package org.egov.works.abstractestimate.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.works.masters.entity.DepositCode;

@Entity
@Table(name = "EGW_DEPOSITWORKS_USAGE")
@NamedQueries({
        @NamedQuery(name = DepositWorksUsage.GET_DEPOSITWORKSUSAGE_AMOUNT, query = " select sum(dwu.consumedAmount-dwu.releasedAmount) from DepositWorksUsage dwu inner join dwu.abstractEstimate.financialDetails fd where dwu.abstractEstimate.depositCode.id=? and fd.fund.id=? and fd.coa.id=? ")
})
@SequenceGenerator(name = DepositWorksUsage.SEQ_EGW_DEPOSITWORKSUSAGE, sequenceName = DepositWorksUsage.SEQ_EGW_DEPOSITWORKSUSAGE, allocationSize = 1)
public class DepositWorksUsage extends AbstractAuditable {

    private static final long serialVersionUID = -1250530369473146349L;

    public static final String SEQ_EGW_DEPOSITWORKSUSAGE = "SEQ_EGW_DEPOSITWORKS_USAGE";

    public static final String GET_DEPOSITWORKSUSAGE_AMOUNT = "getDepositWorksUsageAmount";

    @Id
    @GeneratedValue(generator = SEQ_EGW_DEPOSITWORKSUSAGE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "abstractestimate", nullable = false)
    private AbstractEstimate abstractEstimate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depositcode", nullable = false)
    private DepositCode depositCode;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coa", nullable = false)
    private CChartOfAccounts coa;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financialyear", nullable = false)
    private CFinancialYear financialYear;

    @NotNull
    private BigDecimal totalDepositAmount;

    private BigDecimal consumedAmount;

    private BigDecimal releasedAmount;

    @NotNull
    private String appropriationNumber;

    @NotNull
    @Temporal(value = TemporalType.DATE)
    private Date appropriationDate;

    @Override
    protected void setId(final Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public AbstractEstimate getAbstractEstimate() {
        return abstractEstimate;
    }

    public void setAbstractEstimate(final AbstractEstimate abstractEstimate) {
        this.abstractEstimate = abstractEstimate;
    }

    public DepositCode getDepositCode() {
        return depositCode;
    }

    public void setDepositCode(final DepositCode depositCode) {
        this.depositCode = depositCode;
    }

    public CChartOfAccounts getCoa() {
        return coa;
    }

    public void setCoa(final CChartOfAccounts coa) {
        this.coa = coa;
    }

    public CFinancialYear getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(final CFinancialYear financialYear) {
        this.financialYear = financialYear;
    }

    public BigDecimal getTotalDepositAmount() {
        return totalDepositAmount;
    }

    public void setTotalDepositAmount(final BigDecimal totalDepositAmount) {
        this.totalDepositAmount = totalDepositAmount;
    }

    public BigDecimal getConsumedAmount() {
        return consumedAmount;
    }

    public void setConsumedAmount(final BigDecimal consumedAmount) {
        this.consumedAmount = consumedAmount;
    }

    public BigDecimal getReleasedAmount() {
        return releasedAmount;
    }

    public void setReleasedAmount(final BigDecimal releasedAmount) {
        this.releasedAmount = releasedAmount;
    }

    public String getAppropriationNumber() {
        return appropriationNumber;
    }

    public void setAppropriationNumber(final String appropriationNumber) {
        this.appropriationNumber = appropriationNumber;
    }

    public Date getAppropriationDate() {
        return appropriationDate;
    }

    public void setAppropriationDate(final Date appropriationDate) {
        this.appropriationDate = appropriationDate;
    }

}
