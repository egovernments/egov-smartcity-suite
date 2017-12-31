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
package org.egov.model.instrument;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.Transient;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.infstr.models.BaseModel;

@Entity
@Table(name = "EGF_DISHONORCHEQUE_DETAIL")
@SequenceGenerator(name = DishonorChequeDetails.SEQ_EGF_DISHONORCHEQUE_DETAIL, sequenceName = DishonorChequeDetails.SEQ_EGF_DISHONORCHEQUE_DETAIL, allocationSize = 1)
public class DishonorChequeDetails extends BaseModel {

    private static final long serialVersionUID = -6790212647262088197L;

    public static final String SEQ_EGF_DISHONORCHEQUE_DETAIL = "SEQ_EGF_DISHONORCHQDET";

    @Id
    @GeneratedValue(generator = SEQ_EGF_DISHONORCHEQUE_DETAIL, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "headerid")
    private DishonorCheque header;

    @ManyToOne
    @JoinColumn(name = "glcodeid")
    private CChartOfAccounts glcodeId;

    private BigDecimal debitAmt;

    @Column(name = "creditamt")
    private BigDecimal creditAmount;

    private Integer functionId;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "details", targetEntity = DishonorChequeSubLedgerDetails.class)
    private Set<DishonorChequeSubLedgerDetails> subLedgerDetails = new HashSet<DishonorChequeSubLedgerDetails>();

    @Transient
    private CFunction function;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public Integer getFunctionId() {
        return functionId;
    }

    public void setFunctionId(final Integer functionId) {
        this.functionId = functionId;
    }

    public DishonorCheque getHeader() {
        return header;
    }

    public void setHeader(final DishonorCheque header) {
        this.header = header;
    }

    public CChartOfAccounts getGlcodeId() {
        return glcodeId;
    }

    public void setGlcodeId(final CChartOfAccounts glcodeId) {
        this.glcodeId = glcodeId;
    }

    public BigDecimal getDebitAmt() {
        return debitAmt;
    }

    public void setDebitAmt(final BigDecimal debitAmt) {
        this.debitAmt = debitAmt;
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

    public Set<DishonorChequeSubLedgerDetails> getSubLedgerDetails() {
        return subLedgerDetails;
    }

    public void setSubLedgerDetails(final Set<DishonorChequeSubLedgerDetails> subLedgerDetails) {
        this.subLedgerDetails = subLedgerDetails;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (creditAmount == null ? 0 : creditAmount.hashCode());
        result = prime * result + (debitAmt == null ? 0 : debitAmt.hashCode());
        result = prime * result + (glcodeId == null ? 0 : glcodeId.hashCode());
        result = prime * result + (id == null ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final DishonorChequeDetails other = (DishonorChequeDetails) obj;
        if (creditAmount == null) {
            if (other.creditAmount != null)
                return false;
        } else if (!creditAmount.equals(other.creditAmount))
            return false;
        if (debitAmt == null) {
            if (other.debitAmt != null)
                return false;
        } else if (!debitAmt.equals(other.debitAmt))
            return false;
        if (glcodeId == null) {
            if (other.glcodeId != null)
                return false;
        } else if (!glcodeId.equals(other.glcodeId))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
