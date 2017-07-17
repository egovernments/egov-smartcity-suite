/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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

package org.egov.ptis.domain.entity.transactions;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.commons.Installment;
import org.egov.infra.persistence.entity.AbstractAuditable;

@Entity
@Table(name = "egpt_installment_demand_info")
@SequenceGenerator(name = PropertyInstallmentDemandInfo.SEQ_INSTALLMENT_DEMAND_INFO, sequenceName = PropertyInstallmentDemandInfo.SEQ_INSTALLMENT_DEMAND_INFO, allocationSize = 1)
public class PropertyInstallmentDemandInfo extends AbstractAuditable {

    /**
     * 
     */
    private static final long serialVersionUID = 403786043368830355L;
    protected static final String SEQ_INSTALLMENT_DEMAND_INFO = "SEQ_EGPT_INSTALLMENT_DEMAND_INFO";

    @Id
    @GeneratedValue(generator = SEQ_INSTALLMENT_DEMAND_INFO, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "assessment_transactions", nullable = false)
    private AssessmentTransactions assessmentTransactions;

    private Installment installment;
    private BigDecimal demand;

    @Column(name = "totalcollection")
    private BigDecimal totalCollection;
    private BigDecimal advance;

    @Column(name = "writeoff")
    private BigDecimal writeOff;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public AssessmentTransactions getAssessmentTransactions() {
        return assessmentTransactions;
    }

    public void setAssessmentTransactions(AssessmentTransactions assessmentTransactions) {
        this.assessmentTransactions = assessmentTransactions;
    }

    public Installment getInstallment() {
        return installment;
    }

    public void setInstallment(Installment installment) {
        this.installment = installment;
    }

    public BigDecimal getDemand() {
        return demand;
    }

    public void setDemand(BigDecimal demand) {
        this.demand = demand;
    }

    public BigDecimal getTotalCollection() {
        return totalCollection;
    }

    public void setTotalCollection(BigDecimal totalCollection) {
        this.totalCollection = totalCollection;
    }

    public BigDecimal getAdvance() {
        return advance;
    }

    public void setAdvance(BigDecimal advance) {
        this.advance = advance;
    }

    public BigDecimal getWriteOff() {
        return writeOff;
    }

    public void setWriteOff(BigDecimal writeOff) {
        this.writeOff = writeOff;
    }

}
