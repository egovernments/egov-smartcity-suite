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
package org.egov.model.advance;

import org.egov.commons.EgwStatus;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.pims.commons.Position;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class EgAdvanceRequisition extends StateAware<Position> implements java.io.Serializable {

    private static final long serialVersionUID = 5350085164408760402L;
    private Long id;
    private String advanceRequisitionNumber;
    private EgwStatus status;
    private Date advanceRequisitionDate;
    private BigDecimal advanceRequisitionAmount;
    private String narration;
    private String arftype;

    private EgAdvanceRequisitionMis egAdvanceReqMises;
    private Set<EgAdvanceRequisitionDetails> egAdvanceReqDetailses = new HashSet<>();

    public EgAdvanceRequisition() {
    }

    public EgAdvanceRequisition(final String advanceRequisitionNumber, final EgwStatus status,
                                final Date advanceRequisitionDate, final BigDecimal advanceRequisitionAmount, final String narration,
                                final String arftype, final EgAdvanceRequisitionMis egAdvanceReqMises,
                                final Set<EgAdvanceRequisitionDetails> egAdvanceReqDetailses) {
        super();
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

    public Set<EgAdvanceRequisitionDetails> getEgAdvanceReqDetailses() {
        return egAdvanceReqDetailses;
    }

    public void setEgAdvanceReqDetailses(final Set<EgAdvanceRequisitionDetails> egAdvanceReqDetailses) {
        this.egAdvanceReqDetailses = egAdvanceReqDetailses;
    }

    public void addEgAdvanceReqDetails(final EgAdvanceRequisitionDetails advanceReqDetail) {
        if (advanceReqDetail != null)
            getEgAdvanceReqDetailses().add(advanceReqDetail);
    }

    @Override
    public String toString() {
        return "EgAdvanceRequisition ( Id :  " + (null != getId() ? getId() : "") + "EgAdvanceRequisition arftype: "
                + (null != getArftype() ? getArftype() : "") + ")";
    }

}
