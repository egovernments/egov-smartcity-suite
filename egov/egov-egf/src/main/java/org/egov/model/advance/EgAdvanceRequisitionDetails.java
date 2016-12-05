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
package org.egov.model.advance;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EG_ADVANCEREQUISITIONDETAILS")
@SequenceGenerator(name = EgAdvanceRequisitionDetails.SEQ_EG_ADVANCEREQDETAILS, sequenceName = EgAdvanceRequisitionDetails.SEQ_EG_ADVANCEREQDETAILS, allocationSize = 1)
public class EgAdvanceRequisitionDetails extends AbstractAuditable {

    private static final long serialVersionUID = 9104415562626900594L;
    
    public static final String SEQ_EG_ADVANCEREQDETAILS = "SEQ_EG_ADVANCEREQDETAILS";
    
    @Id
    @GeneratedValue(generator = SEQ_EG_ADVANCEREQDETAILS, strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GLCODEID")
    private CChartOfAccounts chartofaccounts;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADVANCEREQUISITIONID")
    private EgAdvanceRequisition egAdvanceRequisition;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FUNCTIONID")
    private CFunction function;
    
    private BigDecimal creditamount = BigDecimal.ZERO;
    
    private BigDecimal debitamount = BigDecimal.ZERO;
    
    @Length(max = 256)
    private String narration;
    
    @OrderBy("id")
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "egAdvanceRequisitionDetails", targetEntity = EgAdvanceReqPayeeDetails.class)
    private Set<EgAdvanceReqPayeeDetails> egAdvanceReqpayeeDetailses = new HashSet<EgAdvanceReqPayeeDetails>(0);

    public EgAdvanceRequisitionDetails(final Long id, final Date lastupdatedtime,
            final CChartOfAccounts chartofaccounts,
            final EgAdvanceRequisition egAdvanceRequisition, final CFunction function,
            final BigDecimal creditamount, final BigDecimal debitamount, final String narration,
            final Set<EgAdvanceReqPayeeDetails> egAdvanceReqpayeeDetailses) {
        super();
        this.id = id;
        this.chartofaccounts = chartofaccounts;
        this.egAdvanceRequisition = egAdvanceRequisition;
        this.function = function;
        this.creditamount = creditamount;
        this.debitamount = debitamount;
        this.narration = narration;
        this.egAdvanceReqpayeeDetailses = egAdvanceReqpayeeDetailses;
    }

    public EgAdvanceRequisitionDetails() {
        super();

    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public CChartOfAccounts getChartofaccounts() {
        return chartofaccounts;
    }

    public void setChartofaccounts(final CChartOfAccounts chartofaccounts) {
        this.chartofaccounts = chartofaccounts;
    }

    public EgAdvanceRequisition getEgAdvanceRequisition() {
        return egAdvanceRequisition;
    }

    public void setEgAdvanceRequisition(final EgAdvanceRequisition egAdvanceRequisition) {
        this.egAdvanceRequisition = egAdvanceRequisition;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(final CFunction function) {
        this.function = function;
    }

    public BigDecimal getCreditamount() {
        return creditamount;
    }

    public void setCreditamount(final BigDecimal creditamount) {
        this.creditamount = creditamount;
    }

    public BigDecimal getDebitamount() {
        return debitamount;
    }

    public void setDebitamount(final BigDecimal debitamount) {
        this.debitamount = debitamount;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(final String narration) {
        this.narration = narration;
    }

    public Set<EgAdvanceReqPayeeDetails> getEgAdvanceReqpayeeDetailses() {
        return egAdvanceReqpayeeDetailses;
    }

    public void setEgAdvanceReqpayeeDetailses(
            final Set<EgAdvanceReqPayeeDetails> egAdvanceReqpayeeDetailses) {
        this.egAdvanceReqpayeeDetailses = egAdvanceReqpayeeDetailses;
    }

}
