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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.commons.EgwStatus;
import org.egov.infra.workflow.entity.StateAware;

@Entity
@Table(name = "EG_ADVANCEREQUISITION")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = EgAdvanceRequisition.SEQ_EG_ADVANCEREQUISITION, sequenceName = EgAdvanceRequisition.SEQ_EG_ADVANCEREQUISITION, allocationSize = 1)
public class EgAdvanceRequisition extends StateAware {

    private static final long serialVersionUID = 5350085164408760402L;

    public static final String SEQ_EG_ADVANCEREQUISITION = "SEQ_EG_ADVANCEREQUISITION";

    @Id
    @GeneratedValue(generator = SEQ_EG_ADVANCEREQUISITION, strategy = GenerationType.SEQUENCE)
    private Long id;

    private String advanceRequisitionNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusid", nullable = false)
    private EgwStatus status;

    private Date advanceRequisitionDate;

    private BigDecimal advanceRequisitionAmount;

    private String narration;

    private String arftype;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "egAdvanceRequisition", targetEntity = EgAdvanceRequisitionMis.class)
    private EgAdvanceRequisitionMis egAdvanceReqMises;

    @OrderBy("id")
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "egAdvanceRequisition", targetEntity = EgAdvanceRequisitionDetails.class)
    private List<EgAdvanceRequisitionDetails> egAdvanceReqDetailses = new ArrayList<EgAdvanceRequisitionDetails>(0);

    public EgAdvanceRequisition() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public String getStateDetails() {
        return getAdvanceRequisitionNumber();
    }

    public String getAdvanceRequisitionNumber() {
        return advanceRequisitionNumber;
    }

    public void setAdvanceRequisitionNumber(final String advanceRequisitionNumber) {
        this.advanceRequisitionNumber = advanceRequisitionNumber;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(final EgwStatus status) {
        this.status = status;
    }

    public Date getAdvanceRequisitionDate() {
        return advanceRequisitionDate;
    }

    public void setAdvanceRequisitionDate(final Date advanceRequisitionDate) {
        this.advanceRequisitionDate = advanceRequisitionDate;
    }

    public BigDecimal getAdvanceRequisitionAmount() {
        return advanceRequisitionAmount;
    }

    public void setAdvanceRequisitionAmount(final BigDecimal advanceRequisitionAmount) {
        this.advanceRequisitionAmount = advanceRequisitionAmount;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(final String narration) {
        this.narration = narration;
    }

    public String getArftype() {
        return arftype;
    }

    public void setArftype(final String arftype) {
        this.arftype = arftype;
    }

    public EgAdvanceRequisitionMis getEgAdvanceReqMises() {
        return egAdvanceReqMises;
    }

    public void setEgAdvanceReqMises(final EgAdvanceRequisitionMis egAdvanceReqMises) {
        this.egAdvanceReqMises = egAdvanceReqMises;
    }

    public List<EgAdvanceRequisitionDetails> getEgAdvanceReqDetailses() {
        return egAdvanceReqDetailses;
    }

    public void setEgAdvanceReqDetailses(final List<EgAdvanceRequisitionDetails> egAdvanceReqDetailses) {
        this.egAdvanceReqDetailses = egAdvanceReqDetailses;
    }

    public void addEgAdvanceReqDetails(final EgAdvanceRequisitionDetails advanceReqDetail) {
        if (advanceReqDetail != null)
            getEgAdvanceReqDetailses().add(advanceReqDetail);
    }

    public EgAdvanceRequisition(final Long id, final String advanceRequisitionNumber, final EgwStatus status,
            final Date advanceRequisitionDate, final BigDecimal advanceRequisitionAmount, final String narration,
            final String arftype, final EgAdvanceRequisitionMis egAdvanceReqMises,
            final List<EgAdvanceRequisitionDetails> egAdvanceReqDetailses) {
        super();
        // this.id = id;
        this.advanceRequisitionNumber = advanceRequisitionNumber;
        this.status = status;
        this.advanceRequisitionDate = advanceRequisitionDate;
        this.advanceRequisitionAmount = advanceRequisitionAmount;
        this.narration = narration;
        this.arftype = arftype;
        this.egAdvanceReqMises = egAdvanceReqMises;
        this.egAdvanceReqDetailses = egAdvanceReqDetailses;
    }

    @Override
    public String toString() {
        return "EgAdvanceRequisition ( Id :  " + (null != getId() ? getId() : "") + "EgAdvanceRequisition arftype: "
                + (null != getArftype() ? getArftype() : "") + ")";
    }

}
