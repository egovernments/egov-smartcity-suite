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
package org.egov.collection.entity;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.infstr.models.BaseModel;

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
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "EGCL_DISHONORCHEQUEDETAIL")
@SequenceGenerator(name = CollectionDishonorChequeDetails.SEQ_EGCL_DISHONORCHEQUEDETAIL, sequenceName = CollectionDishonorChequeDetails.SEQ_EGCL_DISHONORCHEQUEDETAIL, allocationSize = 1)
public class CollectionDishonorChequeDetails extends BaseModel {

    private static final long serialVersionUID = -6790212647262088197L;

    public static final String SEQ_EGCL_DISHONORCHEQUEDETAIL = "SEQ_EGCL_DISHONORCHEQUEDETAIL";

    @Id
    @GeneratedValue(generator = SEQ_EGCL_DISHONORCHEQUEDETAIL, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dishonorcheque")
    private CollectionDishonorCheque dishonorcheque;

    @ManyToOne
    @JoinColumn(name = "chartofaccounts")
    private CChartOfAccounts chartofaccounts;

    @Column(name = "debitamt")
    private BigDecimal debitAmount;

    @Column(name = "creditamt")
    private BigDecimal creditAmount;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dishonorchequedetail", targetEntity = CollectionDishonorChequeSubLedgerDetails.class)
    private Set<CollectionDishonorChequeSubLedgerDetails> subLedgerDetails = new HashSet<CollectionDishonorChequeSubLedgerDetails>();

    @ManyToOne
    @JoinColumn(name = "function")
    private CFunction function;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CollectionDishonorCheque getDishonorcheque() {
        return dishonorcheque;
    }

    public void setDishonorcheque(CollectionDishonorCheque dishonorcheque) {
        this.dishonorcheque = dishonorcheque;
    }

    public CChartOfAccounts getChartofaccounts() {
        return chartofaccounts;
    }

    public void setChartofaccounts(CChartOfAccounts chartofaccounts) {
        this.chartofaccounts = chartofaccounts;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(final BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(final CFunction function) {
        this.function = function;
    }

    public Set<CollectionDishonorChequeSubLedgerDetails> getSubLedgerDetails() {
        return subLedgerDetails;
    }

    public void setSubLedgerDetails(Set<CollectionDishonorChequeSubLedgerDetails> subLedgerDetails) {
        this.subLedgerDetails = subLedgerDetails;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final CollectionDishonorChequeDetails other = (CollectionDishonorChequeDetails) obj;
        if (debitAmount == null && creditAmount == null) {
            if (debitAmount != null || creditAmount != null)
                return false;
        } else if (!(debitAmount.compareTo(other.debitAmount) == 0 && creditAmount.compareTo(other.creditAmount) == 0))
            return false;
        return true;
    }

}
